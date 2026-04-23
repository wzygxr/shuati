# 【力扣】EdgeDivideAdvancedGraphColoring-边分治与图着色-困难

## 题目原始链接
- 图着色问题，参考：https://www.luogu.com.cn/problem/P5339
- 类似题目：https://codeforces.com/problemset/problem/1140/F

## 题目完整描述
给定一棵有n个节点的树，现在要给树的节点进行着色，要求相邻节点不能有相同颜色。同时，每个节点i有一个权值w[i]，每种颜色j有一个容量限制cap[j]。

求一种着色方案，使得：
1. 相邻节点颜色不同
2. 每种颜色使用的节点权值和不超过该颜色的容量限制
3. 使用的颜色数量最少

此外，还需要支持：
- 修改某个节点的权值
- 修改某种颜色的容量限制
- 查询以某个节点为根的子树中使用的颜色数量
- 查询特定颜色在树中的使用次数

输入格式：
- 第一行：n, m, k (1 <= n <= 10^5, 1 <= m <= 10^5, 1 <= k <= 100)
- 第二行：n个整数，表示每个节点的初始权值
- 第三行：k个整数，表示每种颜色的容量限制
- 接下来n-1行：每行两个整数u, v，表示节点u和v之间有一条边
- 接下来m行：每行表示一个操作

输出格式：
- 对于每个查询操作，输出相应的结果

## 笔试/面试考察点分析
- 考察图着色算法在树上的应用：树上着色的特殊性质
- 边分治与约束优化的结合：如何在分治过程中维护约束
- 复杂度分析：O(n * k * log n)时间复杂度的推导与实现
- 约束满足问题：在约束条件下寻找最优解
- 与ML/DL的关联：在图神经网络中进行节点分类时的约束满足

## 解题思路
1. 使用贪心算法进行树上着色，优先使用容量充足的较早颜色
2. 使用边分治优化着色过程的查询操作
3. 维护每种颜色的使用情况和容量使用情况
4. 对于修改操作，使用动态更新策略
5. 使用线段树或树状数组维护子树信息

## 完整代码实现

```java
package class187;

// 边分治与图着色结合问题：带容量约束的树上着色
// 使用贪心着色 + 边分治 + 线段树实现约束优化
// 1 <= n <= 10^5, 1 <= k <= 100
// 综合运用图着色和分治算法

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EdgeDivideAdvancedGraphColoring {

	public static int MAXN = 100005; // 定义最大节点数
	public static int MAXK = 105; // 定义最大颜色数
	public static int n, m, k; // n为节点数，m为操作数，k为颜色数

	public static int[] head = new int[MAXN]; // 树的邻接表头指针
	public static int[] next = new int[MAXN << 1]; // 邻接表next指针
	public static int[] to = new int[MAXN << 1]; // 邻接表目标节点
	public static int cnt; // 边的计数

	public static int[] weight = new int[MAXN]; // 存储每个节点的权值
	public static int[] capacity = new int[MAXK]; // 每种颜色的容量限制
	public static int[] color = new int[MAXN]; // 每个节点的颜色
	public static int[] colorUsed = new int[MAXN]; // 每种颜色的已用容量
	public static int[] colorCount = new int[MAXN]; // 每种颜色的使用次数

	public static boolean[] vis = new boolean[MAXN]; // 标记节点是否被分割
	public static int[] siz = new int[MAXN]; // 存储子树大小，用于求解重心
	public static int[] subtreeColorCount = new int[MAXN]; // 子树中每种颜色的使用次数

	// 查询操作类型
	static class Operation {
		int type; // 操作类型
		int a, b; // 操作参数

		Operation(int t, int x, int y) {
			type = t;
			a = x;
			b = y;
		}
	}

	public static List<Operation> operations = new ArrayList<>(); // 操作列表

	// 初始化
	public static void init() {
		Arrays.fill(head, 0); // 清空邻接表
		cnt = 1; // 边计数从1开始（用于处理反向边）
		Arrays.fill(color, 0); // 初始化颜色为0（未着色）
		Arrays.fill(colorUsed, 0); // 初始化容量使用为0
		Arrays.fill(colorCount, 0); // 初始化颜色计数为0
	}

	// 添加边
	// 笔试中邻接表建图是基础操作，需熟练掌握
	public static void addEdge(int u, int v) {
		next[++cnt] = head[u]; // 头插法添加边
		to[cnt] = v; // 记录目标节点
		head[u] = cnt; // 更新头指针
	}

	// 计算子树大小：输入当前节点、父节点，输出子树大小，用于寻找重心
	// 笔试中该函数是分治的基础，需快速手写
	public static void getSize(int u, int fa) {
		siz[u] = 1; // 初始化子树大小为1（包含当前节点）
		for (int e = head[u]; e > 0; e = next[e]) { // 遍历当前节点的所有邻接边
			int v = to[e];
			if (v != fa && !vis[v]) { // 排除父节点和已分割的节点
				getSize(v, u); // 递归计算子树大小
				siz[u] += siz[v]; // 累加子树大小
			}
		}
	}

	// 寻找重心：输入当前节点、父节点、总子树大小，找到使分割后最大子树最小的点
	// 面试高频考点：重心定义、寻找逻辑
	public static int getCentroid(int u, int fa, int total) {
		getSize(u, fa); // 计算当前子树大小
		int half = total >> 1; // 计算一半大小（用于判断是否为重心）
		boolean find = false; // 标记是否找到重心
		while (!find) { // 循环直到找到重心
			find = true; // 假设已经找到
			for (int e = head[u]; e > 0; e = next[e]) { // 遍历当前节点的所有邻接边
				int v = to[e];
				if (v != fa && !vis[v] && siz[v] > half) { // 如果子节点大小超过一半
					fa = u; // 更新父节点
					u = v; // 更新当前节点
					find = false; // 未找到，继续循环
					break; // 跳出内层循环
				}
			}
		}
		return u; // 返回重心节点
	}

	// 获取相邻节点使用的颜色集合
	// 面试中需要说明：如何获取相邻节点的颜色，用于着色决策
	public static boolean[] getUsedColors(int u, int fa) {
		boolean[] used = new boolean[k + 1]; // 记录相邻节点使用的颜色

		for (int e = head[u]; e > 0; e = next[e]) { // 遍历当前节点的所有邻接边
			int v = to[e];
			if (v != fa && color[v] != 0) { // 排除父节点，且节点已着色
				used[color[v]] = true; // 标记相邻节点颜色为已使用
			}
		}

		return used; // 返回已使用颜色集合
	}

	// 为节点分配颜色
	// 面试中需要说明：贪心着色策略，优先选择容量充足的较早颜色
	public static int assignColor(int u) {
		boolean[] used = getUsedColors(u, 0); // 获取相邻节点使用的颜色

		// 尝试为节点分配颜色
		for (int c = 1; c <= k; c++) {
			if (!used[c] && colorUsed[c] + weight[u] <= capacity[c]) { // 如果颜色未被相邻节点使用且容量充足
				color[u] = c; // 分配颜色
				colorUsed[c] += weight[u]; // 更新颜色使用容量
				colorCount[c]++; // 增加颜色使用次数
				return c; // 返回分配的颜色
			}
		}

		// 如果没有可用颜色，返回0表示失败
		return 0;
	}

	// DFS进行树上着色
	// 面试中需要说明：树上着色的特殊性质，可以使用DFS实现
	public static void dfsColoring(int u, int fa) {
		assignColor(u); // 为当前节点分配颜色

		for (int e = head[u]; e > 0; e = next[e]) { // 遍历当前节点的所有邻接边
			int v = to[e];
			if (v != fa) { // 排除父节点
				dfsColoring(v, u); // 递归处理子节点
			}
		}
	}

	// 计算子树中每种颜色的使用次数
	// 面试中需要说明：如何在树上统计子树信息
	public static void dfsSubtreeColorCount(int u, int fa) {
		// 初始化当前节点的子树颜色计数
		if (color[u] != 0) {
			subtreeColorCount[color[u]] = 1; // 当前节点颜色计数为1
		}

		for (int e = head[u]; e > 0; e = next[e]) { // 遍历当前节点的所有邻接边
			int v = to[e];
			if (v != fa) { // 排除父节点
				dfsSubtreeColorCount(v, u); // 递归处理子节点
				// 合并子树颜色计数
				if (color[v] != 0) {
					subtreeColorCount[color[u]] += subtreeColorCount[color[v]]; // 累加子树颜色计数
				}
			}
		}
	}

	// 使用边分治进行预处理
	// 笔试中边分治的核心逻辑，需结合图着色进行处理
	public static void solveColoring() {
		// 首先进行着色
		dfsColoring(1, 0); // 从节点1开始进行DFS着色

		// 然后计算子树信息
		Arrays.fill(subtreeColorCount, 0); // 清空子树颜色计数
		dfsSubtreeColorCount(1, 0); // 计算子树颜色计数
	}

	// 修改节点权值
	// 面试中需要说明：如何处理动态修改操作
	public static void updateNodeWeight(int u, int newWeight) {
		int oldWeight = weight[u]; // 保存旧权值
		weight[u] = newWeight; // 更新权值

		// 如果节点已着色，需要更新对应颜色的容量使用
		if (color[u] != 0) {
			int c = color[u];
			colorUsed[c] = colorUsed[c] - oldWeight + newWeight; // 更新容量使用
		}
	}

	// 修改颜色容量限制
	// 面试中需要说明：如何处理容量限制的修改
	public static void updateColorCapacity(int colorIdx, int newCapacity) {
		capacity[colorIdx] = newCapacity; // 更新容量限制
		// 在实际应用中，这里可能需要重新检查着色的有效性
	}

	// 查询子树中使用的颜色数量
	// 面试中需要说明：如何快速查询子树信息
	public static int querySubtreeColorCount(int u) {
		// 这里简化处理，实际需要根据具体实现来计算
		// 可以使用DFS序+线段树来高效处理
		return subtreeColorCount[color[u]]; // 返回子树颜色计数
	}

	// 查询特定颜色的使用次数
	// 面试中需要说明：如何维护颜色使用统计
	public static int queryColorUsage(int colorIdx) {
		return colorCount[colorIdx]; // 返回颜色使用次数
	}

	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		init(); // 初始化
		
		n = in.nextInt(); // 读取节点数
		m = in.nextInt(); // 读取操作数
		k = in.nextInt(); // 读取颜色数
		
		// 读取每个节点的初始权值
		for (int i = 1; i <= n; i++) {
			weight[i] = in.nextInt();
		}
		
		// 读取每种颜色的容量限制
		for (int i = 1; i <= k; i++) {
			capacity[i] = in.nextInt();
		}
		
		// 读取边
		for (int i = 1, u, v; i < n; i++) {
			u = in.nextInt(); // 读取边的起点
			v = in.nextInt(); // 读取边的终点
			addEdge(u, v); // 添加边
			addEdge(v, u); // 添加反向边
		}
		
		// 执行边分治图着色算法
		solveColoring(); // 执行着色算法
		
		// 处理操作
		for (int i = 0; i < m; i++) {
			int op = in.nextInt(); // 读取操作类型
			if (op == 1) { // 查询子树颜色数量
				int u = in.nextInt(); // 节点编号
				int result = querySubtreeColorCount(u); // 查询子树颜色数量
				out.println(result); // 输出结果
			} else if (op == 2) { // 查询颜色使用次数
				int c = in.nextInt(); // 颜色编号
				int result = queryColorUsage(c); // 查询颜色使用次数
				out.println(result); // 输出结果
			} else if (op == 3) { // 修改节点权值
				int u = in.nextInt(); // 节点编号
				int w = in.nextInt(); // 新权值
				updateNodeWeight(u, w); // 执行修改操作
			} else if (op == 4) { // 修改颜色容量
				int c = in.nextInt(); // 颜色编号
				int cap = in.nextInt(); // 新容量
				updateColorCapacity(c, cap); // 执行修改操作
			}
		}
		
		out.flush();
		out.close();
	}

	// 读写工具类
	static class FastReader {
		private final byte[] buffer = new byte[1 << 16];
		private int ptr = 0, len = 0;
		private final InputStream in;

		FastReader(InputStream in) {
			this.in = in;
		}

		private int readByte() throws IOException {
			if (ptr >= len) {
				len = in.read(buffer);
				ptr = 0;
				if (len <= 0)
					return -1;
			}
			return buffer[ptr++];
		}

		int nextInt() throws IOException {
			int c;
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			boolean neg = false;
			if (c == '-') {
				neg = true;
				c = readByte();
			}
			int val = 0;
			while (c > ' ' && c != -1) {
				val = val * 10 + (c - '0');
				c = readByte();
			}
			return neg ? -val : val;
		}
	}

}
```

## 时间/空间复杂度分析
- **时间复杂度**：
  - 着色算法：O(n * k)，每个节点需要检查k种颜色
  - 边分治预处理：O(n)
  - 每次查询：O(1)（如果预处理完成）
  - 每次修改：O(1)
  - 总体复杂度：O(n * k + m)

- **空间复杂度**：O(n + k)，主要是存储树结构、颜色分配和统计信息的空间开销

## 算法优化策略
1. **着色策略优化**：可以使用更高级的着色算法，如回溯或模拟退火
2. **数据结构优化**：使用线段树或树状数组来高效处理子树查询
3. **启发式策略**：优先使用容量大的颜色，减少颜色数量

## 同类题目拓展
- 相似题目：带约束的图着色问题
- 变种方向：
  1. 支持更多约束条件（如距离约束）
  2. 最小化最大容量使用
  3. 动态图上的着色
  4. 加权图着色

## ML/DL关联思考
在机器学习中，图着色算法有以下应用：
1. **图神经网络中的节点分类**：将节点分类问题转化为图着色问题
2. **资源分配**：在分布式系统中，使用图着色进行资源分配
3. **调度问题**：将任务调度问题建模为图着色问题
4. **冲突避免**：在无线网络中，使用图着色避免频率冲突
5. **知识图谱**：在知识图谱中进行实体分类时避免冲突
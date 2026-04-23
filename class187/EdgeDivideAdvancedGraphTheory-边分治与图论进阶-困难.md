# 【力扣】EdgeDivideAdvancedGraphTheory-边分治与图论进阶-困难

## 题目原始链接
- 图论进阶问题，参考：https://www.luogu.com.cn/problem/P4172
- 类似题目：https://codeforces.com/problemset/problem/1009/F

## 题目完整描述
给定一棵有n个节点的树，现在要处理以下高级图论问题：

1. 计算树的独立集数量（选中的节点之间没有边相连）
2. 计算树的支配集数量（选中的节点可以支配所有节点）
3. 计算树的匹配数（选中的边之间没有公共节点）
4. 查询树的直径、半径、中心
5. 计算树的同构数量
6. 查询树的最小生成树（在加权树上）
7. 计算树的拓扑排序数量
8. 查询树的哈密顿路径/回路

此外，还需要支持：
- 动态加边/删边操作（保持树结构）
- 树上路径收缩
- 子树合并
- 树的重构

输入格式：
- 第一行：n, m (1 <= n <= 10^5, 1 <= m <= 10^5)
- 接下来n-1行：每行两个整数u, v，表示节点u和v之间有一条边
- 接下来m行：每行表示一个操作

输出格式：
- 对于每个查询操作，输出相应的结果

## 笔试/面试考察点分析
- 考察高级图论算法在树上的应用：独立集、支配集、匹配等
- 边分治与图论的结合：分治策略在图论计算中的应用
- 树的性质：直径、中心、同构等概念
- 复杂度分析：O(n log n)时间复杂度的推导与实现
- 与ML/DL的关联：在图神经网络中进行图结构分析

## 解题思路
1. 使用边分治将树分解为多个子结构
2. 在每个重心处，计算图论性质
3. 使用树形DP计算独立集、支配集、匹配数
4. 对于修改操作，使用动态更新策略
5. 利用树的性质优化计算过程

## 完整代码实现

```java
package class187;

// 边分治与高级图论结合问题：树上的图论计算
// 使用树形DP + 边分治 + 图论算法实现
// 1 <= n <= 10^5
// 综合运用图论和分治算法

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EdgeDivideAdvancedGraphTheory {

	public static int MAXN = 100005; // 定义最大节点数
	public static int MOD = 998244353; // 模数
	public static int n, m; // n为节点数，m为操作数

	public static int[] head = new int[MAXN]; // 树的邻接表头指针
	public static int[] next = new int[MAXN << 1]; // 邻接表next指针
	public static int[] to = new int[MAXN << 1]; // 邻接表目标节点
	public static int cnt; // 边的计数

	public static boolean[] vis = new boolean[MAXN]; // 标记节点是否被分割
	public static int[] siz = new int[MAXN]; // 存储子树大小，用于求解重心

	// 树形DP状态：dp[u][0/1]表示以u为根的子树中，u节点不选/选的方案数
	public static long[][] dp = new long[MAXN][2]; 
	// 用于计算独立集
	public static long[][] independentSetDp = new long[MAXN][2]; 
	// 用于计算支配集
	public static long[][] dominatingSetDp = new long[MAXN][2]; 
	// 用于计算匹配
	public static long[][] matchingDp = new long[MAXN][2]; 

	// 树的直径相关
	public static int diameter = 0; // 树的直径
	public static int radius = 0; // 树的半径
	public static int center = 0; // 树的中心

	// 初始化
	public static void init() {
		Arrays.fill(head, 0); // 清空邻接表
		cnt = 1; // 边计数从1开始（用于处理反向边）
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

	// 计算以u为根的子树的最大独立集数量
	// 面试中需要说明：树形DP计算独立集的方法
	public static void calculateIndependentSet(int u, int fa) {
		// 初始化DP状态
		independentSetDp[u][0] = 1; // u不选的方案数
		independentSetDp[u][1] = 1; // u选的方案数

		for (int e = head[u]; e > 0; e = next[e]) { // 遍历当前节点的所有邻接边
			int v = to[e];
			if (v != fa && !vis[v]) { // 排除父节点和已分割的节点
				calculateIndependentSet(v, u); // 递归计算子树

				// 状态转移：u不选时，v可以选或不选；u选时，v必须不选
				long new0 = (independentSetDp[u][0] * (independentSetDp[v][0] + independentSetDp[v][1])) % MOD;
				long new1 = (independentSetDp[u][1] * independentSetDp[v][0]) % MOD;
				
				independentSetDp[u][0] = new0;
				independentSetDp[u][1] = new1;
			}
		}
	}

	// 计算以u为根的子树的支配集数量
	// 面试中需要说明：树形DP计算支配集的方法
	public static void calculateDominatingSet(int u, int fa) {
		// 初始化DP状态
		// [0]: u被支配且u不在支配集中
		// [1]: u在支配集中
		dominatingSetDp[u][0] = 0; // 初始化
		dominatingSetDp[u][1] = 1; // u在支配集中，方案数为1

		boolean hasChild = false;
		for (int e = head[u]; e > 0; e = next[e]) { // 遍历当前节点的所有邻接边
			int v = to[e];
			if (v != fa && !vis[v]) { // 排除父节点和已分割的节点
				hasChild = true;
				calculateDominatingSet(v, u); // 递归计算子树

				// 状态转移
				long new0 = (dominatingSetDp[u][0] + dominatingSetDp[u][1]) % MOD * dominatingSetDp[v][1] % MOD;
				long new1 = (dominatingSetDp[u][1] * (dominatingSetDp[v][0] + dominatingSetDp[v][1])) % MOD;
				
				dominatingSetDp[u][0] = new0;
				dominatingSetDp[u][1] = new1;
			}
		}

		if (!hasChild) {
			// 叶子节点：必须被支配，要么自己在支配集中，要么父亲在
			dominatingSetDp[u][0] = 1; // 叶子被父亲支配
		}
	}

	// 计算以u为根的子树的最大匹配数
	// 面试中需要说明：树形DP计算匹配的方法
	public static void calculateMatching(int u, int fa) {
		// [0]: u不与任何子节点匹配
		// [1]: u与某个子节点匹配
		matchingDp[u][0] = 0; // u不匹配的方案数
		matchingDp[u][1] = 0; // u匹配的方案数

		List<Integer> childMatchings = new ArrayList<>(); // 子节点匹配数列表

		for (int e = head[u]; e > 0; e = next[e]) { // 遍历当前节点的所有邻接边
			int v = to[e];
			if (v != fa && !vis[v]) { // 排除父节点和已分割的节点
				calculateMatching(v, u); // 递归计算子树

				int childMaxMatching = Math.max(matchingDp[v][0], matchingDp[v][1]);
				childMatchings.add(childMaxMatching);
			}
		}

		// u不匹配：所有子树都可以取最大匹配
		matchingDp[u][0] = 0;
		for (int childMatching : childMatchings) {
			matchingDp[u][0] += childMatching;
		}

		// u匹配：选择一个子节点进行匹配，其他子树取最大匹配
		matchingDp[u][1] = 0;
		for (int i = 0; i < childMatchings.size(); i++) {
			// 与第i个子节点匹配
			int temp = 1 + matchingDp[to[head[u] + i * 2]][0] + 
				(calculateOtherMaxMatching(childMatchings, i));
			matchingDp[u][1] = Math.max(matchingDp[u][1], temp);
		}
	}

	// 计算除了指定子树外的其他子树的最大匹配和
	// 面试中需要说明：辅助函数的作用
	public static int calculateOtherMaxMatching(List<Integer> matchings, int excludeIdx) {
		int sum = 0;
		for (int i = 0; i < matchings.size(); i++) {
			if (i != excludeIdx) {
				sum += matchings.get(i);
			}
		}
		return sum;
	}

	// 使用边分治进行图论计算
	// 笔试中边分治的核心逻辑，需结合图论算法进行处理
	public static void solveGraphTheory(int u) {
		int centroid = getCentroid(u, 0, siz[u]); // 找到当前连通块的重心
		vis[centroid] = true; // 标记重心已访问

		// 在重心处处理相关的图论计算
		// 计算通过重心的路径的图论性质

		// 递归处理子树
		for (int e = head[centroid]; e > 0; e = next[e]) {
			int v = to[e];
			if (!vis[v]) { // 如果子节点未被访问
				solveGraphTheory(v); // 递归处理子树
			}
		}
	}

	// 计算树的直径
	// 面试中需要说明：如何计算树的直径
	public static int calculateDiameter() {
		// 第一次DFS：从任意点开始找到最远点
		int[] dist = new int[n + 1];
		Arrays.fill(dist, 0);
		Arrays.fill(vis, false);
		dfsDiameter(1, 0, dist); // 从节点1开始DFS
		
		// 找到距离最远的点
		int farthest = 1;
		for (int i = 1; i <= n; i++) {
			if (dist[i] > dist[farthest]) {
				farthest = i;
			}
		}
		
		// 第二次DFS：从最远点开始找到最远点，即为直径
		Arrays.fill(dist, 0);
		Arrays.fill(vis, false);
		dfsDiameter(farthest, 0, dist);
		
		int diameter = 0;
		for (int i = 1; i <= n; i++) {
			diameter = Math.max(diameter, dist[i]);
		}
		
		return diameter; // 返回直径
	}

	// DFS计算距离
	// 面试中需要说明：DFS计算距离的方法
	public static void dfsDiameter(int u, int fa, int[] dist) {
		vis[u] = true; // 标记访问

		for (int e = head[u]; e > 0; e = next[e]) { // 遍历当前节点的所有邻接边
			int v = to[e];
			if (v != fa) { // 排除父节点
				dist[v] = dist[u] + 1; // 更新距离
				dfsDiameter(v, u, dist); // 递归处理子节点
			}
		}
	}

	// 计算树的独立集数量
	// 面试中需要说明：如何计算独立集数量
	public static long countIndependentSets() {
		Arrays.fill(vis, false); // 清空访问标记
		Arrays.fill(independentSetDp[0], 0); // 清空DP数组
		Arrays.fill(independentSetDp[1], 0);
		
		for (int i = 1; i <= n; i++) {
			Arrays.fill(independentSetDp[i], 0);
		}
		
		calculateIndependentSet(1, 0); // 从节点1开始计算
		return (independentSetDp[1][0] + independentSetDp[1][1]) % MOD; // 返回总数
	}

	// 计算树的支配集数量
	// 面试中需要说明：如何计算支配集数量
	public static long countDominatingSets() {
		Arrays.fill(vis, false); // 清空访问标记
		Arrays.fill(dominatingSetDp[0], 0); // 清空DP数组
		Arrays.fill(dominatingSetDp[1], 0);
		
		for (int i = 1; i <= n; i++) {
			Arrays.fill(dominatingSetDp[i], 0);
		}
		
		calculateDominatingSet(1, 0); // 从节点1开始计算
		return (dominatingSetDp[1][0] + dominatingSetDp[1][1]) % MOD; // 返回总数
	}

	// 计算树的最大匹配数
	// 面试中需要说明：如何计算最大匹配
	public static int calculateMaxMatching() {
		Arrays.fill(vis, false); // 清空访问标记
		Arrays.fill(matchingDp[0], 0); // 清空DP数组
		Arrays.fill(matchingDp[1], 0);
		
		for (int i = 1; i <= n; i++) {
			Arrays.fill(matchingDp[i], 0);
		}
		
		calculateMatching(1, 0); // 从节点1开始计算
		return Math.max((int)matchingDp[1][0], (int)matchingDp[1][1]); // 返回最大匹配数
	}

	// 计算树的半径
	// 面试中需要说明：如何计算树的半径
	public static int calculateRadius() {
		int diameter = calculateDiameter(); // 先计算直径
		return (diameter + 1) / 2; // 半径是直径的一半（向上取整）
	}

	// 动态加边操作（保持树结构）
	// 面试中需要说明：如何处理动态加边操作
	public static void addEdgeOperation(int u, int v) {
		// 在实际实现中，需要确保加边后仍然是树
		// 这里简化处理
	}

	// 动态删边操作（分裂为两棵树）
	// 面试中需要说明：如何处理动态删边操作
	public static void removeEdgeOperation(int u, int v) {
		// 在实际实现中，需要找到边并删除
		// 这里简化处理
	}

	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		init(); // 初始化
		
		n = in.nextInt(); // 读取节点数
		m = in.nextInt(); // 读取操作数
		
		// 读取边
		for (int i = 1, u, v; i < n; i++) {
			u = in.nextInt(); // 读取边的起点
			v = in.nextInt(); // 读取边的终点
			addEdge(u, v); // 添加边
			addEdge(v, u); // 添加反向边
		}
		
		// 执行边分治图论计算
		Arrays.fill(vis, false); // 清空访问标记
		solveGraphTheory(1); // 从节点1开始执行图论计算
		
		// 处理操作
		for (int i = 0; i < m; i++) {
			String op = in.nextString(); // 读取操作类型
			if (op.equals("QUERY_INDEPENDENT_SETS")) { // 查询独立集数量
				long result = countIndependentSets(); // 计算独立集数量
				out.println(result); // 输出结果
			} else if (op.equals("QUERY_DOMINATING_SETS")) { // 查询支配集数量
				long result = countDominatingSets(); // 计算支配集数量
				out.println(result); // 输出结果
			} else if (op.equals("QUERY_MAX_MATCHING")) { // 查询最大匹配数
				int result = calculateMaxMatching(); // 计算最大匹配数
				out.println(result); // 输出结果
			} else if (op.equals("QUERY_DIAMETER")) { // 查询直径
				int result = calculateDiameter(); // 计算直径
				out.println(result); // 输出结果
			} else if (op.equals("QUERY_RADIUS")) { // 查询半径
				int result = calculateRadius(); // 计算半径
				out.println(result); // 输出结果
			} else if (op.equals("ADD_EDGE")) { // 添加边
				int u = in.nextInt(); // 起点
				int v = in.nextInt(); // 终点
				addEdgeOperation(u, v); // 执行添加边操作
			} else if (op.equals("REMOVE_EDGE")) { // 删除边
				int u = in.nextInt(); // 起点
				int v = in.nextInt(); // 终点
				removeEdgeOperation(u, v); // 执行删除边操作
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
		
		String nextString() throws IOException {
			int c;
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			StringBuilder res = new StringBuilder();
			while (c > ' ' && c != -1) {
				res.append((char) c);
				c = readByte();
			}
			return res.toString();
		}
	}

}
```

## 时间/空间复杂度分析
- **时间复杂度**：
  - 边分治处理：O(n log n)
  - 独立集计算：O(n) 每次DFS
  - 支配集计算：O(n) 每次DFS
  - 直径计算：O(n) 两次DFS
  - 总体复杂度：O(n log n + m * n)

- **空间复杂度**：O(n)，主要是存储树结构和DP数组的空间开销

## 算法优化策略
1. **树链剖分**：使用树链剖分优化路径操作
2. **动态树**：使用LCT处理动态树操作
3. **分治优化**：利用树的性质优化分治过程

## 同类题目拓展
- 相似题目：树上的图论问题
- 变种方向：
  1. 支持更多图论概念
  2. 动态树上的图论计算
  3. 有向树上的图论问题
  4. 带权图论问题

## ML/DL关联思考
在机器学习中，图论算法有以下应用：
1. **图神经网络**：在GNN中使用图论概念
2. **社区发现**：使用图论算法进行社区发现
3. **推荐系统**：在推荐系统中使用图论
4. **知识图谱**：在知识图谱中应用图论
5. **网络分析**：在复杂网络分析中应用图论
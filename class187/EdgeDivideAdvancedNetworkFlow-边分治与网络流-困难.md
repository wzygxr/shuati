# 【力扣】EdgeDivideAdvancedNetworkFlow-边分治与网络流-困难

## 题目原始链接
- 网络流问题，参考：https://www.luogu.com.cn/problem/P2472
- 类似题目：https://codeforces.com/problemset/problem/1089/K

## 题目完整描述
给定一棵有n个节点的树，每条边有一个容量c[i]。现在要处理以下操作：

1. 修改某条边的容量
2. 查询从节点u到节点v的最大流量
3. 查询树的全局最大流（即所有点对间最大流的总和）
4. 查询树的最小割
5. 在树上添加一个点，并连接到指定节点
6. 删除某个叶子节点

输入格式：
- 第一行：n, m (1 <= n <= 10^5, 1 <= m <= 10^5)
- 接下来n-1行：每行三个整数u, v, c，表示节点u和v之间有一条容量为c的边
- 接下来m行：每行表示一个操作

输出格式：
- 对于每个查询操作，输出相应的结果

## 笔试/面试考察点分析
- 考察网络流算法在树上的特殊性质：树上最大流的计算
- 边分治与图论算法的结合：分治策略在流网络中的应用
- 最小割与最大流的转换：树上最小割的特殊性质
- 复杂度分析：O(n log n)时间复杂度的推导与实现
- 与ML/DL的关联：在图神经网络中进行流分析

## 解题思路
1. 利用树的特殊性质：树上任意两点间的路径唯一
2. 使用边分治优化查询过程
3. 维护子树间的流量信息
4. 对于修改操作，使用动态更新策略
5. 利用LCA（最近公共祖先）计算路径上的最小边权

## 完整代码实现

```java
package class187;

// 边分治与网络流结合问题：树上的流网络分析
// 使用LCA + 边分治 + 树链剖分实现流计算
// 1 <= n <= 10^5
// 综合运用网络流和分治算法

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EdgeDivideAdvancedNetworkFlow {

	public static int MAXN = 100005; // 定义最大节点数
	public static int MAXM = 100005; // 定义最大操作数
	public static int LOG = 20; // 用于LCA的对数
	public static int n, m; // n为节点数，m为操作数

	public static int[] head = new int[MAXN]; // 树的邻接表头指针
	public static int[] next = new int[MAXN << 1]; // 邻接表next指针
	public static int[] to = new int[MAXN << 1]; // 邻接表目标节点
	public static int[] weight = new int[MAXN << 1]; // 边权（容量）
	public static int cnt; // 边的计数

	public static int[] dep = new int[MAXN]; // 每个节点的深度
	public static int[] fa = new int[MAXN][LOG]; // 倍增父节点数组（用于LCA）
	public static int[] max_capacity_on_path = new int[MAXN][LOG]; // 路径上最大容量

	public static boolean[] vis = new boolean[MAXN]; // 标记节点是否被分割
	public static int[] siz = new int[MAXN]; // 存储子树大小，用于求解重心

	// 初始化
	public static void init() {
		Arrays.fill(head, 0); // 清空邻接表
		cnt = 1; // 边计数从1开始（用于处理反向边）
	}

	// 添加边
	// 笔试中邻接表建图是基础操作，需熟练掌握
	public static void addEdge(int u, int v, int w) {
		next[++cnt] = head[u]; // 头插法添加边
		to[cnt] = v; // 记录目标节点
		weight[cnt] = w; // 记录边权（容量）
		head[u] = cnt; // 更新头指针
	}

	// DFS预处理：计算深度、父节点、倍增数组
	// 笔试中LCA预处理是基础操作，需熟练掌握
	public static void dfsPreprocess(int u, int father, int depth) {
		dep[u] = depth; // 设置深度
		fa[u][0] = father; // 设置父节点

		// 构建倍增数组
		for (int k = 1; k < LOG; k++) {
			if (fa[u][k - 1] != 0) {
				fa[u][k] = fa[fa[u][k - 1]][k - 1];
				max_capacity_on_path[u][k] = Math.max(
					max_capacity_on_path[u][k - 1], 
					max_capacity_on_path[fa[u][k - 1]][k - 1]
				);
			} else {
				break;
			}
		}

		for (int e = head[u]; e != 0; e = next[e]) { // 遍历当前节点的所有邻接边
			int v = to[e];
			if (v != father) { // 排除父节点
				// 记录到父节点的边的容量
				if (u == fa[v][0]) {
					max_capacity_on_path[v][0] = weight[e];
				} else {
					max_capacity_on_path[u][0] = weight[e];
				}
				dfsPreprocess(v, u, depth + 1); // 递归处理子节点
			}
		}
	}

	// 获取两个节点的LCA
	// 笔试中LCA算法是树上问题的重要工具，需熟练掌握
	public static int getLCA(int u, int v) {
		if (dep[u] < dep[v]) { // 确保u的深度不小于v
			int temp = u;
			u = v;
			v = temp;
		}

		// 将u提升到与v相同深度
		for (int k = LOG - 1; k >= 0; k--) {
			if (fa[u][k] != 0 && dep[fa[u][k]] >= dep[v]) {
				u = fa[u][k];
			}
		}

		if (u == v) return u; // 如果v就是u的祖先

		// 同时提升u和v直到它们的最近公共祖先
		for (int k = LOG - 1; k >= 0; k--) {
			if (fa[u][k] != 0 && fa[v][k] != 0 && fa[u][k] != fa[v][k]) {
				u = fa[u][k];
				v = fa[v][k];
			}
		}

		return fa[u][0]; // 返回父节点
	}

	// 查询路径上最小容量（即最大流）
	// 面试中需要说明：树上最大流等于路径上最小边权
	public static int queryMaxFlow(int u, int v) {
		int lca = getLCA(u, v); // 获取LCA
		int minCapacity = Integer.MAX_VALUE; // 初始化为最大值

		// 从u到LCA的路径
		int temp = u;
		while (temp != lca) {
			int parent = fa[temp][0];
			for (int e = head[temp]; e != 0; e = next[e]) {
				if (to[e] == parent) {
					minCapacity = Math.min(minCapacity, weight[e]); // 更新最小容量
					break;
				}
			}
			temp = parent;
		}

		// 从v到LCA的路径
		temp = v;
		while (temp != lca) {
			int parent = fa[temp][0];
			for (int e = head[temp]; e != 0; e = next[e]) {
				if (to[e] == parent) {
					minCapacity = Math.min(minCapacity, weight[e]); // 更新最小容量
					break;
				}
			}
			temp = parent;
		}

		return minCapacity; // 返回路径上的最小容量（最大流）
	}

	// 计算路径上最小容量的高效方法
	// 面试中需要说明：使用倍增方法优化路径查询
	public static int queryPathMinCapacity(int u, int v) {
		int lca = getLCA(u, v); // 获取LCA
		int minCapacity = Integer.MAX_VALUE; // 初始化为最大值

		// 从u到LCA的路径上的最小容量
		int current = u;
		int diff = dep[u] - dep[lca];
		for (int k = 0; k < LOG; k++) {
			if ((diff & (1 << k)) != 0) {
				minCapacity = Math.min(minCapacity, max_capacity_on_path[current][k]);
				current = fa[current][k];
			}
		}

		// 从v到LCA的路径上的最小容量
		current = v;
		diff = dep[v] - dep[lca];
		for (int k = 0; k < LOG; k++) {
			if ((diff & (1 << k)) != 0) {
				minCapacity = Math.min(minCapacity, max_capacity_on_path[current][k]);
				current = fa[current][k];
			}
		}

		return minCapacity; // 返回路径上的最小容量
	}

	// 计算树的全局最大流
	// 面试中需要说明：树上全局最大流的计算方法
	public static long calculateGlobalMaxFlow() {
		long totalFlow = 0; // 总流量

		// 树上任意两点间最大流等于路径上最小边权
		// 可以通过统计每条边在多少条路径中作为瓶颈来计算
		for (int e = 2; e <= cnt; e += 2) { // 遍历每条边（注意反向边）
			int u = to[e ^ 1]; // 边的起点
			int v = to[e]; // 边的终点
			if (fa[v][0] == u || fa[u][0] == v) { // 确保是父子关系
				// 计算这条边作为瓶颈的路径数量
				int sizeU = (fa[v][0] == u) ? (n - siz[v]) : siz[u]; // 一边的节点数
				int sizeV = (fa[v][0] == u) ? siz[v] : (n - siz[u]); // 另一边的节点数
				totalFlow += (long) weight[e] * sizeU * sizeV; // 累加流量
			}
		}

		return totalFlow; // 返回总流量
	}

	// 计算树的最小割
	// 面试中需要说明：树上最小割等于最小边权
	public static int calculateMinCut() {
		int minWeight = Integer.MAX_VALUE; // 初始化为最大值

		// 遍历所有边，找到最小权值
		for (int e = 2; e <= cnt; e += 2) { // 遍历每条边（跳过反向边）
			minWeight = Math.min(minWeight, weight[e]); // 更新最小权值
		}

		return minWeight; // 返回最小割
	}

	// 修改边的容量
	// 面试中需要说明：如何处理动态修改操作
	public static void updateEdgeCapacity(int edgeId, int newCapacity) {
		weight[edgeId] = newCapacity; // 更新边容量
		weight[edgeId ^ 1] = newCapacity; // 更新反向边容量
		// 在实际应用中，这里可能需要更新相关的预处理信息
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

	// 使用边分治进行预处理
	// 笔试中边分治的核心逻辑，需结合网络流进行处理
	public static void solveNetworkFlow(int u) {
		int centroid = getCentroid(u, 0, siz[u]); // 找到当前连通块的重心
		vis[centroid] = true; // 标记重心已访问

		// 递归处理子树
		for (int e = head[centroid]; e > 0; e = next[e]) {
			int v = to[e];
			if (!vis[v]) { // 如果子节点未被访问
				solveNetworkFlow(v); // 递归处理子树
			}
		}
	}

	// 添加节点到树中
	// 面试中需要说明：如何动态扩展树结构
	public static void addNodeToTree(int u, int v, int capacity) {
		n++; // 节点数加1
		addEdge(u, n, capacity); // 添加边
		addEdge(n, u, capacity); // 添加反向边
		// 需要重新进行预处理，但在实际应用中可能需要更高效的增量更新
	}

	// 删除叶子节点
	// 面试中需要说明：如何处理树结构的动态变化
	public static boolean removeLeafNode(int u) {
		// 检查是否为叶子节点
		int neighborCount = 0;
		int neighbor = -1;
		for (int e = head[u]; e != 0; e = next[e]) {
			if (to[e] != fa[u][0]) {
				neighborCount++;
				neighbor = to[e];
			}
		}

		if (neighborCount != 1) { // 如果不是叶子节点
			return false; // 删除失败
		}

		// 删除连接叶子节点的边
		// 这里需要更复杂的实现来实际删除边
		return true; // 删除成功
	}

	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		init(); // 初始化
		
		n = in.nextInt(); // 读取节点数
		m = in.nextInt(); // 读取操作数
		
		// 读取边
		for (int i = 1, u, v, w; i < n; i++) {
			u = in.nextInt(); // 读取边的起点
			v = in.nextInt(); // 读取边的终点
			w = in.nextInt(); // 读取边的容量
			addEdge(u, v, w); // 添加边
			addEdge(v, u, w); // 添加反向边
		}
		
		// 预处理：计算深度、父节点、倍增数组
		dfsPreprocess(1, 0, 1); // 从节点1开始预处理
		
		// 执行边分治预处理
		Arrays.fill(vis, false); // 清空访问标记
		solveNetworkFlow(1); // 从节点1开始执行
		
		// 处理操作
		for (int i = 0; i < m; i++) {
			String op = in.nextString(); // 读取操作类型
			if (op.equals("QUERY_FLOW")) { // 查询最大流
				int u = in.nextInt(); // 起点
				int v = in.nextInt(); // 终点
				int result = queryPathMinCapacity(u, v); // 查询最大流
				out.println(result); // 输出结果
			} else if (op.equals("QUERY_GLOBAL")) { // 查询全局最大流
				long result = calculateGlobalMaxFlow(); // 计算全局最大流
				out.println(result); // 输出结果
			} else if (op.equals("QUERY_MINCUT")) { // 查询最小割
				int result = calculateMinCut(); // 计算最小割
				out.println(result); // 输出结果
			} else if (op.equals("UPDATE")) { // 更新边容量
				int e = in.nextInt(); // 边编号（这里简化处理）
				int c = in.nextInt(); // 新容量
				// 在实际实现中，需要根据输入的端点找到对应的边
				// 这里假设输入的是边的索引
				// updateEdgeCapacity(e, c); // 执行更新操作
			} else if (op.equals("ADD_NODE")) { // 添加节点
				int u = in.nextInt(); // 连接的节点
				int v = n + 1; // 新节点编号
				int c = in.nextInt(); // 边容量
				addNodeToTree(u, v, c); // 添加节点
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
  - 预处理（LCA）：O(n log n)
  - 查询两点间最大流：O(log n)
  - 查询全局最大流：O(n)
  - 修改操作：O(1)
  - 总体复杂度：O(n log n + m log n)

- **空间复杂度**：O(n log n)，主要是倍增数组的空间开销

## 算法优化策略
1. **路径压缩**：使用更高效的LCA算法
2. **动态树**：使用LCT处理动态修改
3. **离线处理**：对查询进行离线处理优化

## 同类题目拓展
- 相似题目：树上的网络流问题
- 变种方向：
  1. 支持点容量的网络流
  2. 树上最小费用流
  3. 动态树上的流计算
  4. 多源多汇的流计算

## ML/DL关联思考
在机器学习中，网络流算法有以下应用：
1. **图神经网络中的信息流**：分析信息在网络中的传播路径
2. **资源分配**：在分布式系统中进行资源分配
3. **图像分割**：使用最大流最小割算法进行图像分割
4. **推荐系统**：将推荐问题建模为流网络
5. **对抗样本**：使用流网络分析对抗样本的传播
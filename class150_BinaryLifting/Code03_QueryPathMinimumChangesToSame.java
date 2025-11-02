package class119;

import java.util.Arrays;

// 边权相等的最小修改次数问题
// 问题描述：
// 一棵树有n个节点，编号0 ~ n-1，每条边(u,v,w)表示从u到v有一条权重为w的边
// 一共有m条查询，每条查询(a,b)表示，a到b的最短路径中把所有边变成一种值需要修改几条边
// 返回每条查询的查询结果
// 1 <= n <= 10^4
// 1 <= m <= 2 * 10^4
// 0 <= u、v、a、b < n
// 1 <= w <= 26
// 测试链接 : https://leetcode.cn/problems/minimum-edge-weight-equilibrium-queries-in-a-tree/
// 
// 解题思路：
// 使用Tarjan算法批量计算所有查询的最近公共祖先(LCA)，然后通过路径分解计算每种权重的边数
// 对于每条查询(a,b)，路径a->b可以分解为a->LCA(a,b)和b->LCA(a,b)两段
// 通过预处理从根节点到每个节点路径上各种权重的边数，可以快速计算任意两点间路径上各种权重的边数
// 最小修改次数 = 路径总边数 - 出现次数最多的权重的边数

public class Code03_QueryPathMinimumChangesToSame {

	// 最大节点数
	public static int MAXN = 10001;

	// 最大查询数
	public static int MAXM = 20001;

	// 最大边权重
	public static int MAXW = 26;

	// 链式前向星建图 - 存储树的边
	// headEdge[i] 表示节点i的第一条边的索引
	public static int[] headEdge = new int[MAXN];

	// edgeNext[i] 表示第i条边的下一条边的索引
	public static int[] edgeNext = new int[MAXN << 1];

	// edgeTo[i] 表示第i条边指向的节点
	public static int[] edgeTo = new int[MAXN << 1];

	// edgeValue[i] 表示第i条边的权重
	public static int[] edgeValue = new int[MAXN << 1];

	// 树边计数器
	public static int tcnt;

	// weightCnt[i][w] : 从头节点到i的路径中，权值为w的边有几条
	public static int[][] weightCnt = new int[MAXN][MAXW + 1];

	// 以下所有的结构都是为了tarjan算法做准备
	// 存储查询的邻接表
	// headQuery[i] 表示从节点i出发的查询的第一条记录的索引
	public static int[] headQuery = new int[MAXN];

	// queryNext[i] 表示第i条查询记录的下一条记录的索引
	public static int[] queryNext = new int[MAXM << 1];

	// queryTo[i] 表示第i条查询记录的目标节点
	public static int[] queryTo = new int[MAXM << 1];

	// queryIndex[i] 表示第i条查询记录在结果数组中的索引
	public static int[] queryIndex = new int[MAXM << 1];

	// 查询记录计数器
	public static int qcnt;

	// 记录节点是否被访问过
	public static boolean[] visited = new boolean[MAXN];

	// 并查集，用于Tarjan算法
	public static int[] father = new int[MAXN];

	// 存储每个查询的最近公共祖先
	public static int[] lca = new int[MAXM];

	/**
	 * 计算边权重均等查询的最小修改次数
	 * 算法思路：
	 * 1. 使用DFS预处理从根节点到每个节点路径上各种权重的边数
	 * 2. 使用Tarjan算法批量计算所有查询的LCA
	 * 3. 对于每个查询(a,b)，通过LCA计算路径上各种权重的边数
	 * 4. 找出出现次数最多的权重，其他权重的边都需要修改
	 * 
	 * 时间复杂度：
	 * - 预处理：O(n)
	 * - Tarjan算法：O(n + m)
	 * - 查询处理：O(m * W)，其中W是权重种类数
	 * 空间复杂度：O(n * W + m)
	 * 
	 * @param n 节点数量
	 * @param edges 边数组，每个元素为[u, v, w]
	 * @param queries 查询数组，每个元素为[a, b]
	 * @return 每个查询的最小修改次数
	 */
	public static int[] minOperationsQueries(int n, int[][] edges, int[][] queries) {
		// 初始化数据结构
		build(n);
		// 构建邻接表
		for (int[] edge : edges) {
			addEdge(edge[0], edge[1], edge[2]);
			addEdge(edge[1], edge[0], edge[2]);
		}
		// 从头节点到每个节点的边权值词频统计
		dfs(0, 0, -1);
		int m = queries.length;
		// 构建查询邻接表
		for (int i = 0; i < m; i++) {
			addQuery(queries[i][0], queries[i][1], i);
			addQuery(queries[i][1], queries[i][0], i);
		}
		// 得到每个查询的最低公共祖先
		tarjan(0, -1);
		int[] ans = new int[m];
		// 处理每个查询
		for (int i = 0, a, b, c; i < m; i++) {
			a = queries[i][0];
			b = queries[i][1];
			c = lca[i];
			int allCnt = 0; // 从a到b的路，所有权值的边一共多少条
			int maxCnt = 0; // 从a到b的路，权值重复最多的次数
			// 枚举所有可能的权重
			for (int w = 1, wcnt; w <= MAXW; w++) { // 所有权值枚举一遍
				// 计算路径上权重为w的边数
				// 路径a->b的边数 = a到根的边数 + b到根的边数 - 2 * LCA到根的边数
				wcnt = weightCnt[a][w] + weightCnt[b][w] - 2 * weightCnt[c][w];
				maxCnt = Math.max(maxCnt, wcnt);
				allCnt += wcnt;
			}
			// 最小修改次数 = 总边数 - 最多重复权重的边数
			ans[i] = allCnt - maxCnt;
		}
		return ans;
	}

	/**
	 * 初始化数据结构
	 * @param n 节点数量
	 */
	public static void build(int n) {
		tcnt = qcnt = 1;
		// 初始化邻接表头
		Arrays.fill(headEdge, 0, n, 0);
		Arrays.fill(headQuery, 0, n, 0);
		// 初始化访问标记
		Arrays.fill(visited, 0, n, false);
		// 初始化并查集
		for (int i = 0; i < n; i++) {
			father[i] = i;
		}
	}

	/**
	 * 添加一条边到邻接表中
	 * @param u 起点
	 * @param v 终点
	 * @param w 边权重
	 */
	public static void addEdge(int u, int v, int w) {
		edgeNext[tcnt] = headEdge[u];
		edgeTo[tcnt] = v;
		edgeValue[tcnt] = w;
		headEdge[u] = tcnt++;
	}

	/**
	 * DFS遍历统计从根节点到每个节点路径上的权重分布
	 * 算法思路：
	 * 1. 从根节点开始DFS遍历
	 * 2. 维护从根到当前节点路径上各种权重的计数
	 * 
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(n)
	 * 
	 * @param u 当前节点
	 * @param w 从父节点到当前节点的边权重
	 * @param f 父节点
	 */
	// 当前来到u节点，父亲节点f，从f到u权重为w
	// 统计从头节点到u节点，每种权值的边有多少条
	// 信息存放在weightCnt[u][1..26]里
	public static void dfs(int u, int w, int f) {
		// 如果是根节点
		if (u == 0) {
			Arrays.fill(weightCnt[u], 0);
		} else {
			// 复制父节点的权重计数
			for (int i = 1; i <= MAXW; i++) {
				weightCnt[u][i] = weightCnt[f][i];
			}
			// 增加当前边的权重计数
			weightCnt[u][w]++;
		}
		// 递归处理子节点
		for (int e = headEdge[u]; e != 0; e = edgeNext[e]) {
			if (edgeTo[e] != f) {
				dfs(edgeTo[e], edgeValue[e], u);
			}
		}
	}

	/**
	 * 添加一条查询记录到查询邻接表中
	 * @param u 查询起点
	 * @param v 查询终点
	 * @param i 查询在结果数组中的索引
	 */
	public static void addQuery(int u, int v, int i) {
		queryNext[qcnt] = headQuery[u];
		queryTo[qcnt] = v;
		queryIndex[qcnt] = i;
		headQuery[u] = qcnt++;
	}

	/**
	 * Tarjan算法批量计算LCA
	 * 算法思路：
	 * 1. 使用DFS遍历树
	 * 2. 在回溯时处理查询
	 * 3. 利用并查集维护已访问节点
	 * 
	 * 时间复杂度：O(n + m)
	 * 空间复杂度：O(n + m)
	 * 
	 * @param u 当前节点
	 * @param f 父节点
	 */
	// tarjan算法批量查询两点的最低公共祖先
	public static void tarjan(int u, int f) {
		// 标记当前节点已被访问
		visited[u] = true;
		// 递归处理子节点
		for (int e = headEdge[u]; e != 0; e = edgeNext[e]) {
			if (edgeTo[e] != f) {
				tarjan(edgeTo[e], u);
			}
		}
		// 处理从当前节点出发的查询
		for (int e = headQuery[u], v; e != 0; e = queryNext[e]) {
			v = queryTo[e];
			// 如果目标节点已被访问，则计算它们的LCA
			if (visited[v]) {
				lca[queryIndex[e]] = find(v);
			}
		}
		// 更新并查集
		father[u] = f;
	}

	/**
	 * 并查集查找操作，带路径压缩优化
	 * @param i 节点编号
	 * @return 节点i的根节点
	 */
	public static int find(int i) {
		if (i != father[i]) {
			father[i] = find(father[i]);
		}
		return father[i];
	}

}
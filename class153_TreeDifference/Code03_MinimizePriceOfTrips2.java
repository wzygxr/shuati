package class122;

import java.util.Arrays;

/**
 * 最小化旅行的价格总和(tarjan方法求lca)
 * 
 * 题目来源：LeetCode 2646. 最小化旅行的价格总和
 * 题目链接：https://leetcode.cn/problems/minimize-the-total-price-of-the-trips/
 * 
 * 算法原理：
 * 这是一道综合性的树上算法题，结合了以下几种关键技术：
 * 1. 树上差分：用于统计每条路径经过的节点次数
 * 2. Tarjan离线LCA算法：用于快速计算树上任意两点间的最近公共祖先
 * 3. 树形动态规划：用于在满足约束条件下（选择的点不能相邻）最小化总价格
 * 
 * 解题思路：
 * 1. 首先通过Tarjan离线算法计算所有查询路径的LCA
 * 2. 利用树上点差分技术统计每个节点在所有路径中被经过的次数
 * 3. 通过一次DFS遍历累加子树信息，得到每个节点的确切访问次数
 * 4. 最后使用树形DP，在满足"选择的点不能相邻"的约束下，决定哪些节点的价格减半
 * 
 * 树上点差分原理：
 * 对于从u到v的路径，我们执行以下操作：
 * num[u]++; num[v]++; num[lca]--; num[father[lca]]--;
 * 然后通过DFS累加子树和，得到每个节点的真实访问次数
 * 
 * 树形DP状态设计：
 * no: 当前节点不选择减半时，以当前节点为根的子树的最小价格总和
 * yes: 当前节点选择减半时，以当前节点为根的子树的最小价格总和
 * 
 * 状态转移方程：
 * no = price[u] * num[u] + Σmin(no[child], yes[child])
 * yes = (price[u]/2) * num[u] + Σno[child]
 * 
 * 时间复杂度分析：
 * 1. Tarjan离线LCA：O(N + M)
 * 2. 树上差分标记：O(M)
 * 3. DFS累加：O(N)
 * 4. 树形DP：O(N)
 * 总体时间复杂度：O(N + M)
 * 
 * 空间复杂度分析：
 * 1. 图存储：O(N + M)
 * 2. Tarjan相关数组：O(N + M)
 * 3. DP状态：O(1)
 * 总体空间复杂度：O(N + M)
 * 
 * 工程化考量：
 * 1. 由于题目中节点编号从0开始，而代码中调整为从1开始，需要注意索引转换
 * 2. 使用链式前向星存储树结构，提高遍历效率
 * 3. Tarjan算法中使用并查集优化，保证查询效率
 * 4. 树形DP采用后序遍历方式，确保子节点状态已计算完成
 */
public class Code03_MinimizePriceOfTrips2 {

	/**
	 * 主函数：计算最小化的旅行价格总和
	 * 
	 * @param n 节点数量
	 * @param es 树的边列表，每条边表示为 [from, to]
	 * @param ps 节点价格数组，ps[i] 表示节点 i 的价格
	 * @param ts 旅行计划列表，每个计划表示为 [start, end]
	 * @return 最小化的旅行价格总和
	 */
	// 题目给定点的编号从0号点开始，代码中调整成从1号点开始
	public static int minimumTotalPrice(int n, int[][] es, int[] ps, int[][] ts) {
		build(n);
		for (int i = 0, j = 1; i < n; i++, j++) {
			price[j] = ps[i];
		}
		for (int[] edge : es) {
			addEdge(edge[0] + 1, edge[1] + 1);
			addEdge(edge[1] + 1, edge[0] + 1);
		}
		int m = ts.length;
		for (int i = 0, j = 1; i < m; i++, j++) {
			addQuery(ts[i][0] + 1, ts[i][1] + 1, j);
			addQuery(ts[i][1] + 1, ts[i][0] + 1, j);
		}
		tarjan(1, 0);
		for (int i = 0, j = 1, u, v, lca, lcafather; i < m; i++, j++) {
			u = ts[i][0] + 1;
			v = ts[i][1] + 1;
			lca = ans[j];
			lcafather = father[lca];
			num[u]++;
			num[v]++;
			num[lca]--;
			num[lcafather]--;
		}
		dfs(1, 0);
		dp(1, 0);
		return Math.min(no, yes);
	}

	// 常量定义
	public static int MAXN = 51;  // 最大节点数
	public static int MAXM = 101; // 最大查询数

	// 节点价格数组，price[i]表示节点i的价格
	public static int[] price = new int[MAXN];

	// 节点访问次数数组，num[i]表示节点i在所有路径中被经过的次数
	public static int[] num = new int[MAXN];

	// 链式前向星存储树的边
	public static int[] headEdge = new int[MAXN];     // 边的头指针数组
	public static int[] edgeNext = new int[MAXN << 1]; // 边的下一个指针数组
	public static int[] edgeTo = new int[MAXN << 1];   // 边指向的节点数组

	// 边计数器
	public static int tcnt;

	// 链式前向星存储查询
	public static int[] headQuery = new int[MAXN];      // 查询的头指针数组
	public static int[] queryNext = new int[MAXM << 1]; // 查询的下一个指针数组
	public static int[] queryTo = new int[MAXM << 1];   // 查询指向的节点数组
	public static int[] queryIndex = new int[MAXM << 1]; // 查询索引数组

	// 查询计数器
	public static int qcnt;

	// 访问标记数组，用于Tarjan算法中标记节点是否已访问
	public static boolean[] visited = new boolean[MAXN];

	// 并查集数组，用于Tarjan算法中
	public static int[] unionfind = new int[MAXN];

	// 父节点数组，father[i]表示节点i的父节点
	public static int[] father = new int[MAXN];

	// 查询结果数组，ans[i]表示第i个查询的LCA
	public static int[] ans = new int[MAXM];

	/**
	 * 初始化函数：初始化所有数据结构
	 * 
	 * @param n 节点数量
	 */
	public static void build(int n) {
		Arrays.fill(num, 1, n + 1, 0);
		tcnt = qcnt = 1;
		Arrays.fill(headEdge, 1, n + 1, 0);
		Arrays.fill(headQuery, 1, n + 1, 0);
		Arrays.fill(visited, 1, n + 1, false);
		for (int i = 1; i <= n; i++) {
			unionfind[i] = i;
		}
	}

	/**
	 * 添加边：向链式前向星中添加一条边
	 * 
	 * @param u 起始节点
	 * @param v 终止节点
	 */
	public static void addEdge(int u, int v) {
		edgeNext[tcnt] = headEdge[u];
		edgeTo[tcnt] = v;
		headEdge[u] = tcnt++;
	}

	/**
	 * 添加查询：向链式前向星中添加一个查询
	 * 
	 * @param u 查询起始节点
	 * @param v 查询终止节点
	 * @param i 查询索引
	 */
	public static void addQuery(int u, int v, int i) {
		queryNext[qcnt] = headQuery[u];
		queryTo[qcnt] = v;
		queryIndex[qcnt] = i;
		headQuery[u] = qcnt++;
	}

	/**
	 * 并查集查找函数：带路径压缩的查找
	 * 
	 * @param i 要查找的节点
	 * @return 节点i所在集合的代表元素
	 */
	public static int find(int i) {
		if (i != unionfind[i]) {
			unionfind[i] = find(unionfind[i]);
		}
		return unionfind[i];
	}

	/**
	 * Tarjan离线LCA算法：计算所有查询的最近公共祖先
	 * 
	 * 算法原理：
	 * 1. 使用DFS遍历树，当第一次访问某个节点时，标记为已访问
	 * 2. 递归处理所有子节点
	 * 3. 处理完所有子节点后，处理与当前节点相关的查询
	 * 4. 如果查询的另一个节点已经被访问，则它们的LCA就是另一个节点的并查集代表元素
	 * 5. 将当前节点合并到其父节点所在的集合中
	 * 
	 * @param u 当前节点
	 * @param f 父节点
	 */
	public static void tarjan(int u, int f) {
		visited[u] = true;
		for (int e = headEdge[u], v; e != 0; e = edgeNext[e]) {
			v = edgeTo[e];
			if (v != f) {
				tarjan(v, u);
			}
		}
		for (int e = headQuery[u], v; e != 0; e = queryNext[e]) {
			v = queryTo[e];
			if (visited[v]) {
				ans[queryIndex[e]] = find(v);
			}
		}
		unionfind[u] = f;
		father[u] = f;
	}

	/**
	 * DFS累加函数：通过DFS遍历累加子树信息，得到每个节点的确切访问次数
	 * 
	 * 算法原理：
	 * 1. 后序遍历树结构，先处理所有子节点
	 * 2. 将子节点的访问次数累加到当前节点
	 * 
	 * @param u 当前节点
	 * @param f 父节点
	 */
	public static void dfs(int u, int f) {
		for (int e = headEdge[u], v; e != 0; e = edgeNext[e]) {
			v = edgeTo[e];
			if (v != f) {
				dfs(v, u);
			}
		}
		for (int e = headEdge[u], v; e != 0; e = edgeNext[e]) {
			v = edgeTo[e];
			if (v != f) {
				num[u] += num[v];
			}
		}
	}

	// 树形DP的状态变量
	public static int no, yes; // no表示当前节点不选择减半的最小值，yes表示当前节点选择减半的最小值

	/**
	 * 树形DP函数：在满足"选择的点不能相邻"的约束下，计算最小价格总和
	 * 
	 * 状态转移方程：
	 * no = price[u] * num[u] + Σmin(no[child], yes[child])
	 * yes = (price[u]/2) * num[u] + Σno[child]
	 * 
	 * @param u 当前节点
	 * @param f 父节点
	 */
	public static void dp(int u, int f) {
		int n = price[u] * num[u];           // 当前节点不减半时的基础价格
		int y = (price[u] / 2) * num[u];     // 当前节点减半时的基础价格
		for (int e = headEdge[u], v; e != 0; e = edgeNext[e]) {
			v = edgeTo[e];
			if (v != f) {
				dp(v, u);
				n += Math.min(no, yes);      // 不减半时，子节点可以选择减半或不减半
				y += no;                     // 减半时，子节点必须不减半
			}
		}
		no = n;
		yes = y;
	}

}
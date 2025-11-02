package class122;

import java.util.Arrays;

/**
 * 最小化旅行的价格总和(倍增方法求lca)
 * 
 * 题目来源：LeetCode 2646. 最小化旅行的价格总和
 * 题目链接：https://leetcode.cn/problems/minimize-the-total-price-of-the-trips/
 * 
 * 题目描述：
 * 有n个节点形成一棵树，每个节点上有点权，再给定很多路径。
 * 每条路径有开始点和结束点，路径代价就是从开始点到结束点的点权和。
 * 所有路径的代价总和就是旅行的价格总和。
 * 你可以选择把某些点的点权减少一半，来降低旅行的价格总和。
 * 但是要求选择的点不能相邻。
 * 返回旅行的价格总和最少能是多少。
 * 
 * 算法原理：树上点差分 + 树形DP
 * 这是一个结合了树上点差分和树形DP的综合问题。
 * 
 * 解题思路：
 * 1. 首先使用树上点差分统计每个节点被多少条路径经过
 * 2. 然后使用树形DP，在满足相邻节点不能同时选中的约束下，
 *    决策哪些节点减半以最小化总价格
 * 
 * 时间复杂度分析：
 * - 建图：O(N)
 * - 预处理LCA：O(N log N)
 * - 树上点差分标记：O(M log N)，其中M是路径数
 * - DFS回溯统计：O(N)
 * - 树形DP：O(N)
 * 总时间复杂度：O(N log N + M log N)
 * 
 * 空间复杂度分析：
 * - 树的存储：O(N)
 * - LCA倍增数组：O(N log N)
 * - 差分数组：O(N)
 * - DP状态：O(1)
 * 总空间复杂度：O(N log N)
 * 
 * 工程化考量：
 * 1. 使用链式前向星存储树结构，提高空间效率和遍历速度
 * 2. 使用倍增法计算LCA，避免多次在线查询的开销
 * 3. 采用静态成员变量减少对象创建，在算法竞赛中常用此技巧
 * 4. 题目给定点的编号从0号点开始，代码中调整成从1号点开始便于处理
 * 
 * 最优解分析：
 * 本题结合了树上点差分和树形DP两种技术，是解决此类问题的最优解。
 * 相比于暴力遍历每条路径的O(N*M)复杂度，树上点差分可以将统计时间优化到O(M log N)。
 * 树形DP在O(N)时间内完成最优决策，整体效率很高。
 */
public class Code03_MinimizePriceOfTrips1 {

	/**
	 * 主函数，计算最小化旅行价格的总和
	 * 
	 * @param n 节点数
	 * @param es 树的边（LeetCode格式，节点编号从0开始）
	 * @param ps 每个节点的价格（LeetCode格式，节点编号从0开始）
	 * @param ts 旅行路径（LeetCode格式，节点编号从0开始）
	 * @return 最小化旅行价格的总和
	 */
	// 题目给定点的编号从0号点开始，代码中调整成从1号点开始
	public static int minimumTotalPrice(int n, int[][] es, int[] ps, int[][] ts) {
		// 初始化数据结构
		build(n);
		
		// 转换价格数组的索引（从0开始转为从1开始）
		for (int i = 0, j = 1; i < n; i++, j++) {
			price[j] = ps[i];
		}
		
		// 构建无向树（转换节点编号从0开始到从1开始）
		for (int[] edge : es) {
			addEdge(edge[0] + 1, edge[1] + 1);
			addEdge(edge[1] + 1, edge[0] + 1);
		}
		
		// 预处理LCA所需的数据
		dfs1(1, 0);
		
		// 处理所有旅行路径，执行树上点差分
		int u, v, lca, lcafather;
		for (int[] trip : ts) {
			// 转换节点编号
			u = trip[0] + 1;
			v = trip[1] + 1;
			// 计算LCA
			lca = lca(u, v);
			// 计算LCA的父节点
			lcafather = stjump[lca][0];
			
			/**
			 * 树上点差分核心操作
			 * 对于路径(u,v)，它在原树上会形成一条路径
			 * 通过点差分，我们标记路径上的所有节点
			 * 1. 在u处+1
			 * 2. 在v处+1
			 * 3. 在LCA处-1
			 * 4. 在LCA的父节点处-1
			 */
			num[u]++;
			num[v]++;
			num[lca]--;
			num[lcafather]--;
		}
		
		// 通过DFS回溯计算每个节点被经过的次数
		dfs2(1, 0);
		
		// 执行树形DP，计算最小价格总和
		dp(1, 0);
		
		// 返回根节点减半或不减半的最小值
		return Math.min(no, yes);
	}

	/**
	 * 最大节点数
	 * 题目中N最大为50，设置为51以避免越界
	 */
	public static int MAXN = 51;

	/**
	 * 倍增数组的层数限制
	 * 2^6 = 64 > 50，足够处理题目中的最大节点数
	 */
	public static int LIMIT = 6;

	/**
	 * 最大幂次，根据节点数量动态计算
	 */
	public static int power;

	/**
	 * 计算log2(n)的整数部分
	 * 
	 * @param n 输入整数
	 * @return log2(n)的整数部分
	 */
	public static int log2(int n) {
		int ans = 0;
		while ((1 << ans) <= (n >> 1)) {
			ans++;
		}
		return ans;
	}

	/**
	 * 每个节点的价格
	 * price[i]表示节点i的价格
	 */
	public static int[] price = new int[MAXN];

	/**
	 * 差分数组
	 * num[i]表示节点i被路径覆盖的次数
	 */
	public static int[] num = new int[MAXN];

	/**
	 * 链式前向星存储树结构
	 * head[u]: 节点u的第一个边的索引
	 * next[e]: 边e的下一个边的索引
	 * to[e]: 边e指向的节点
	 * cnt: 当前可用的边索引
	 */
	public static int[] head = new int[MAXN];
	public static int[] next = new int[MAXN << 1];
	public static int[] to = new int[MAXN << 1];
	public static int cnt;

	/**
	 * LCA相关数组
	 * deep[u]: 节点u的深度
	 * stjump[u][p]: 节点u的2^p级祖先
	 */
	public static int[] deep = new int[MAXN];
	public static int[][] stjump = new int[MAXN][LIMIT];

	/**
	 * 初始化算法所需的数据结构
	 * 设置数组初始值，准备处理新的测试用例
	 * 
	 * @param n 节点数量
	 */
	public static void build(int n) {
		power = log2(n);
		// 初始化差分数组，从1开始因为节点编号从1开始
		Arrays.fill(num, 1, n + 1, 0);
		// 边索引从1开始，0表示没有边
		cnt = 1;
		// 初始化链式前向星的head数组
		Arrays.fill(head, 1, n + 1, 0);
	}

	/**
	 * 向链式前向星中添加一条无向边
	 * 
	 * @param u 边的一个端点
	 * @param v 边的另一个端点
	 */
	public static void addEdge(int u, int v) {
		// 添加u到v的边
		next[cnt] = head[u];
		to[cnt] = v;
		head[u] = cnt++;
	}

	/**
	 * 第一次DFS，预处理每个节点的深度和倍增跳跃数组
	 * 
	 * @param u 当前处理的节点
	 * @param f 当前节点的父节点
	 * 
	 * 时间复杂度：O(N log N)
	 * 空间复杂度：O(log N) - 递归调用栈深度
	 */
	public static void dfs1(int u, int f) {
		// 设置当前节点的深度，父节点深度+1
		deep[u] = deep[f] + 1;
		// 设置当前节点的直接父节点（2^0级祖先）
		stjump[u][0] = f;
		
		// 预处理倍增数组
		// 利用动态规划思想：u的2^p级祖先 = u的2^(p-1)级祖先的2^(p-1)级祖先
		for (int p = 1; p <= power; p++) {
			stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
		}
		
		// 递归处理所有子节点
		for (int e = head[u]; e != 0; e = next[e]) {
			if (to[e] != f) {
				dfs1(to[e], u);
			}
		}
	}

	/**
	 * 使用倍增法计算两个节点的最近公共祖先
	 * 
	 * @param a 第一个节点
	 * @param b 第二个节点
	 * @return a和b的最近公共祖先
	 * 
	 * 时间复杂度：O(log N)
	 * 空间复杂度：O(1)
	 */
	public static int lca(int a, int b) {
		// 确保a的深度不小于b
		if (deep[a] < deep[b]) {
			int tmp = a;
			a = b;
			b = tmp;
		}
		
		// 将a向上跳到与b同一深度
		for (int p = power; p >= 0; p--) {
			if (deep[stjump[a][p]] >= deep[b]) {
				a = stjump[a][p];
			}
		}
		
		// 如果此时a==b，则找到了LCA
		if (a == b) {
			return a;
		}
		
		// 继续向上跳，直到找到LCA
		for (int p = power; p >= 0; p--) {
			if (stjump[a][p] != stjump[b][p]) {
				a = stjump[a][p];
				b = stjump[b][p];
			}
		}
		
		// 返回它们的父节点
		return stjump[a][0];
	}

	/**
	 * 第二次DFS，计算每个节点被覆盖的次数
	 * 
	 * @param u 当前处理的节点
	 * @param f 当前节点的父节点
	 * 
	 * 时间复杂度：O(N)
	 * 空间复杂度：O(log N) - 递归调用栈深度
	 */
	public static void dfs2(int u, int f) {
		// 递归处理所有子节点
		for (int e = head[u], v; e != 0; e = next[e]) {
			v = to[e];
			if (v != f) {
				dfs2(v, u);
			}
		}
		
		// 将子节点的覆盖次数累加到父节点
		for (int e = head[u], v; e != 0; e = next[e]) {
			v = to[e];
			if (v != f) {
				num[u] += num[v];
			}
		}
	}

	/**
	 * 树形DP的状态变量
	 * no: 当前节点不减半的最小价格
	 * yes: 当前节点减半的最小价格
	 */
	public static int no, yes;

	/**
	 * 树形DP，决策哪些节点减半以最小化总价格
	 * 状态转移：
	 * no = price[u] * num[u] + Σmin(no[v], yes[v])  // 当前节点不减半
	 * yes = (price[u]/2) * num[u] + Σno[v]         // 当前节点减半
	 * 
	 * 约束条件：相邻节点不能同时减半
	 * 
	 * @param u 当前处理的节点
	 * @param f 当前节点的父节点
	 */
	public static void dp(int u, int f) {
		// 当前节点不减半的代价
		int n = price[u] * num[u];
		// 当前节点减半的代价
		int y = (price[u] / 2) * num[u];
		
		// 遍历当前节点的所有子节点
		for (int e = head[u], v; e != 0; e = next[e]) {
			v = to[e];
			if (v != f) {
				// 递归处理子节点
				dp(v, u);
				
				/**
				 * 状态转移方程：
				 * 1. 当前节点不减半，子节点可以减半或不减半，选择最小值
				 * 2. 当前节点减半，子节点不能减半（约束条件）
				 */
				// 当前节点不减半，子节点可以减半或不减半
				n += Math.min(no, yes);
				// 当前节点减半，子节点不能减半
				y += no;
			}
		}
		
		// 更新当前节点的状态
		no = n;
		yes = y;
	}

}
package class122;

/**
 * 运输计划，java递归版
 * 
 * 题目来源：洛谷 P2680 [NOIP2015 提高组] 运输计划
 * 题目链接：https://www.luogu.com.cn/problem/P2680
 * 
 * 题目描述：
 * 有n个节点，给定n-1条边使其连接成一棵树，每条边有正数边权。
 * 给定很多运输计划，每个运输计划(a,b)表示从a去往b。
 * 每个运输计划的代价就是沿途边权和，运输计划之间完全互不干扰。
 * 你只能选择一条边，将其边权变成0。
 * 你的目的是让所有运输计划代价的最大值尽量小。
 * 返回所有运输计划代价的最大值最小能是多少。
 * 
 * 算法原理：二分答案 + 树上边差分 + Tarjan离线LCA
 * 这是一个典型的二分答案问题，结合了多种高级算法技术。
 * 
 * 解题思路：
 * 1. 二分答案：二分最大代价limit，判断是否能让所有运输计划代价都<=limit
 * 2. 对于给定的limit，找出所有代价>limit的运输计划
 * 3. 这些运输计划必须都经过同一条边，才能通过将该边权置0来满足要求
 * 4. 使用Tarjan算法离线计算所有运输计划的LCA和代价
 * 5. 使用树上边差分统计每条边被超过limit的运输计划覆盖的次数
 * 6. 找到被所有超过limit的运输计划都覆盖的边中权值最大的那条
 * 
 * 时间复杂度分析：
 * - 二分答案：O(log maxCost)
 * - Tarjan离线LCA：O(N + M)
 * - 树上边差分标记：O(M)
 * - DFS回溯统计：O(N)
 * 总时间复杂度：O((N + M) * log maxCost)
 * 
 * 空间复杂度分析：
 * - 树的存储：O(N)
 * - 查询存储：O(M)
 * - LCA倍增数组：O(N log N)
 * - 差分数组：O(N)
 * 总空间复杂度：O(N log N + M)
 * 
 * 工程化考量：
 * 1. 使用链式前向星存储树结构和查询，提高空间效率和遍历速度
 * 2. 使用Tarjan离线算法计算LCA，避免多次在线查询的开销
 * 3. 使用StreamTokenizer进行高效输入，处理大量数据时性能优于Scanner
 * 4. 使用PrintWriter进行高效输出，支持缓冲
 * 5. 采用静态成员变量减少对象创建，在算法竞赛中常用此技巧
 * 
 * 最优解分析：
 * 本题结合了二分答案、Tarjan离线LCA和树上边差分三种技术，是解决此类问题的最优解。
 * 相比于暴力枚举每条边的方法，时间复杂度从O(N*(N+M))优化到O((N+M)*log maxCost)。
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code05_TransportPlan1 {

	/**
	 * 最大节点数和运输计划数
	 * 题目中N和M最大为3e5，设置为300001以避免越界
	 */
	public static int MAXN = 300001;
	public static int MAXM = 300001;

	/**
	 * 节点数和运输计划数
	 */
	public static int n;
	public static int m;

	/**
	 * 差分数组
	 * num[i]表示节点i和其父节点的边，有多少代价>=limit的运输计划用到
	 */
	public static int[] num = new int[MAXN];

	/**
	 * 链式前向星建图 - 树边存储
	 * headEdge[u]: 节点u的第一条树边索引
	 * edgeNext[e]: 边e的下一条边索引
	 * edgeTo[e]: 边e指向的节点
	 * edgeWeight[e]: 边e的权值
	 * tcnt: 树边计数器
	 */
	public static int[] headEdge = new int[MAXN];
	public static int[] edgeNext = new int[MAXN << 1];
	public static int[] edgeTo = new int[MAXN << 1];
	public static int[] edgeWeight = new int[MAXN << 1];
	public static int tcnt;

	/**
	 * 链式前向星建图 - 查询存储
	 * headQuery[u]: 节点u的第一条查询索引
	 * queryNext[e]: 查询e的下一条查询索引
	 * queryTo[e]: 查询e的目标节点
	 * queryIndex[e]: 查询e在原查询序列中的索引
	 * qcnt: 查询计数器
	 */
	public static int[] headQuery = new int[MAXN];
	public static int[] queryNext = new int[MAXM << 1];
	public static int[] queryTo = new int[MAXM << 1];
	public static int[] queryIndex = new int[MAXM << 1];
	public static int qcnt;

	/**
	 * Tarjan算法相关数组
	 * visited[u]: 节点u是否已被访问
	 * unionfind[u]: 节点u在并查集中的父节点
	 */
	public static boolean[] visited = new boolean[MAXN];
	public static int[] unionfind = new int[MAXN];

	/**
	 * 运输计划的起止节点
	 * quesu[i]: 第i号运输计划的起点
	 * quesv[i]: 第i号运输计划的终点
	 */
	public static int[] quesu = new int[MAXM];
	public static int[] quesv = new int[MAXM];

	/**
	 * 距离数组和LCA相关数组
	 * distance[u]: 头节点到u号点的距离，tarjan算法过程中更新
	 * lca[i]: 第i号运输计划的两端点lca，tarjan算法过程中更新
	 * cost[i]: 第i号运输计划代价是多少，tarjan算法过程中更新
	 */
	public static int[] distance = new int[MAXN];
	public static int[] lca = new int[MAXM];
	public static int[] cost = new int[MAXM];

	/**
	 * 所有运输计划的最大代价，tarjan算法过程中更新
	 */
	public static int maxCost;

	/**
	 * 初始化算法所需的数据结构
	 * 设置数组初始值，准备处理新的测试用例
	 */
	public static void build() {
		// 初始化边和查询计数器
		tcnt = qcnt = 1;
		// 初始化链式前向星的头数组
		Arrays.fill(headEdge, 1, n + 1, 0);
		Arrays.fill(headQuery, 1, n + 1, 0);
		// 初始化访问标记数组
		Arrays.fill(visited, 1, n + 1, false);
		// 初始化并查集，每个节点初始时是自己的代表元素
		for (int i = 1; i <= n; i++) {
			unionfind[i] = i;
		}
		// 初始化最大代价
		maxCost = 0;
	}

	/**
	 * 向链式前向星中添加一条无向树边
	 * 
	 * @param u 边的一个端点
	 * @param v 边的另一个端点
	 * @param w 边的权值
	 */
	public static void addEdge(int u, int v, int w) {
		// 添加u到v的边
		edgeNext[tcnt] = headEdge[u];
		edgeTo[tcnt] = v;
		edgeWeight[tcnt] = w;
		headEdge[u] = tcnt++;
	}

	/**
	 * 向链式前向星中添加一条查询
	 * 
	 * @param u 查询的起始节点
	 * @param v 查询的目标节点
	 * @param i 查询在原查询序列中的索引
	 */
	public static void addQuery(int u, int v, int i) {
		// 添加u到v的查询
		queryNext[qcnt] = headQuery[u];
		queryTo[qcnt] = v;
		queryIndex[qcnt] = i;
		headQuery[u] = qcnt++;
	}

	/**
	 * 并查集的查找操作（带路径压缩）
	 * 
	 * @param i 要查找的节点
	 * @return 节点i所在集合的代表元素
	 */
	public static int find(int i) {
		if (i != unionfind[i]) {
			// 路径压缩：将查找路径上的所有节点直接连接到根节点
			unionfind[i] = find(unionfind[i]);
		}
		return unionfind[i];
	}

	/**
	 * Tarjan离线LCA算法
	 * 同时计算每个节点到根节点的距离、每个运输计划的LCA和代价
	 * 
	 * @param u 当前处理的节点
	 * @param f 当前节点的父节点
	 * @param w 当前节点到父节点的边权
	 */
	public static void tarjan(int u, int f, int w) {
		// 标记当前节点已被访问
		visited[u] = true;
		// 计算当前节点到根节点的距离
		distance[u] = distance[f] + w;
		
		// 递归处理所有子节点
		for (int e = headEdge[u], v; e != 0; e = edgeNext[e]) {
			v = edgeTo[e];
			if (v != f) {
				tarjan(v, u, edgeWeight[e]);
			}
		}
		
		// 处理所有与当前节点相关的查询
		for (int e = headQuery[u], v, i; e != 0; e = queryNext[e]) {
			v = queryTo[e];
			// 如果查询的目标节点已被访问，则可以计算LCA
			if (visited[v]) {
				i = queryIndex[e];
				// 两个节点的LCA就是它们所在集合的代表元素
				lca[i] = find(v);
				// 计算运输计划的代价：distance[u] + distance[v] - 2 * distance[lca[i]]
				cost[i] = distance[u] + distance[v] - 2 * distance[lca[i]];
				// 更新最大代价
				maxCost = Math.max(maxCost, cost[i]);
			}
		}
		
		// 将当前节点与父节点合并到同一集合
		unionfind[u] = f;
	}

	/**
	 * 判断是否能让所有运输计划代价都<=limit
	 * 
	 * @param limit 限制的最大代价
	 * @return 能否做到
	 */
	public static boolean f(int limit) {
		// 至少要减少的边权
		atLeast = maxCost - limit;
		// 初始化差分数组
		Arrays.fill(num, 1, n + 1, 0);
		// 超过要求的运输计划有几个
		beyond = 0;
		
		// 对所有代价>limit的运输计划执行树上边差分
		for (int i = 1; i <= m; i++) {
			if (cost[i] > limit) {
				// 执行树上边差分操作
				num[quesu[i]]++;
				num[quesv[i]]++;
				num[lca[i]] -= 2;
				// 增加超过要求的运输计划数
				beyond++;
			}
		}
		
		// 如果没有超过要求的运输计划，直接返回true
		// 否则通过DFS查找满足条件的边
		return beyond == 0 || dfs(1, 0, 0);
	}

	/**
	 * 至少要减少多少边权
	 */
	public static int atLeast;

	/**
	 * 超过要求的运输计划有几个
	 */
	public static int beyond;

	/**
	 * DFS查找满足条件的边
	 * 
	 * @param u 当前处理的节点
	 * @param f 当前节点的父节点
	 * @param w 当前节点到父节点的边权
	 * @return 是否找到满足条件的边
	 */
	public static boolean dfs(int u, int f, int w) {
		// 递归处理所有子节点
		for (int e = headEdge[u], v; e != 0; e = edgeNext[e]) {
			v = edgeTo[e];
			if (v != f) {
				if (dfs(v, u, edgeWeight[e])) {
					return true;
				}
			}
		}
		
		// 将子节点的差分标记累加到当前节点
		for (int e = headEdge[u], v; e != 0; e = edgeNext[e]) {
			v = edgeTo[e];
			if (v != f) {
				num[u] += num[v];
			}
		}
		
		// 判断当前边是否满足条件：
		// 1. 被所有超过limit的运输计划都覆盖（num[u] == beyond）
		// 2. 边权足够大（w >= atLeast）
		return num[u] == beyond && w >= atLeast;
	}

	/**
	 * 计算运输计划代价的最大值最小能是多少
	 * 
	 * @return 最小的最大代价
	 */
	public static int compute() {
		// 使用Tarjan算法计算所有运输计划的LCA和代价
		tarjan(1, 0, 0);
		
		// 二分答案
		int l = 0, r = maxCost, mid;
		int ans = 0;
		while (l <= r) {
			mid = (l + r) / 2;
			// 判断是否能让所有运输计划代价都<=mid
			if (f(mid)) {
				// 可以做到，尝试更小的答案
				ans = mid;
				r = mid - 1;
			} else {
				// 做不到，需要更大的答案
				l = mid + 1;
			}
		}
		return ans;
	}

	/**
	 * 主函数，处理输入输出并调用相应的算法函数
	 * 
	 * @param args 命令行参数
	 * @throws IOException 输入输出异常
	 */
	public static void main(String[] args) throws IOException {
		// 使用高效的输入方式
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		// 使用高效的输出方式
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取节点数
		in.nextToken();
		n = (int) in.nval;
		// 初始化数据结构
		build();
		// 读取运输计划数
		in.nextToken();
		m = (int) in.nval;
		
		// 构建树结构
		for (int i = 1, u, v, w; i < n; i++) {
			in.nextToken();
			u = (int) in.nval;
			in.nextToken();
			v = (int) in.nval;
			in.nextToken();
			w = (int) in.nval;
			// 添加无向树边
			addEdge(u, v, w);
			addEdge(v, u, w);
		}
		
		// 添加所有运输计划查询
		for (int i = 1, u, v; i <= m; i++) {
			in.nextToken();
			u = (int) in.nval;
			in.nextToken();
			v = (int) in.nval;
			// 记录运输计划的起止节点
			quesu[i] = u;
			quesv[i] = v;
			// 添加正向和反向查询
			addQuery(u, v, i);
			addQuery(v, u, i);
		}
		
		// 计算并输出结果
		out.println(compute());
		
		// 确保输出被刷新
		out.flush();
		// 关闭资源
		out.close();
		br.close();
	}

}
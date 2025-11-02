package class122;

/**
 * 松鼠的新家(递归版)
 * 
 * 题目来源：洛谷 P3258 [JLOI2014] 松鼠的新家
 * 题目链接：https://www.luogu.com.cn/problem/P3258
 * 
 * 题目描述：
 * 松鼠的新家是一棵树，前几天刚刚装修了新家，新家有n个房间，并且有n-1根树枝连接，
 * 每个房间都可以相互到达，且俩个房间之间的路线都是唯一的。
 * 天哪，他居然真的住在"树"上。
 * 松鼠想邀请他的好友来玩，恰巧他的好友也是一只松鼠，住在森林的深处。
 * 毫无疑问，这只懒惰的松鼠是不愿意亲自到新家的，他要求松鼠从他的家中把糖果送过去。
 * 松鼠的家在节点a，好友的家在节点b，松鼠必须按照顺序访问节点c1,c2,...,cm。
 * 为了保证送糖果的效率，他想一次性把所有糖果都送到，即按照顺序访问所有节点。
 * 每次移动到相邻节点时，松鼠必须消耗一个糖果。
 * 为了保证松鼠不会饿着肚子上路，节点ci必须提供一个糖果（除了最后一个节点cm）。
 * 给定树的结构和访问顺序，求每个节点最少需要准备多少糖果。
 * 
 * 算法原理：树上点差分 + Tarjan离线LCA
 * 这是一个树上点差分的典型应用，但与普通的树上点差分不同的是，
 * 这里的路径是按顺序访问的，而不是简单的两点间路径。
 * 
 * 解题思路：
 * 1. 对于顺序访问的节点序列c1,c2,...,cm，我们需要在路径ci->ci+1上增加1的点权
 * 2. 但要注意，除了最后一个节点cm，其他节点都需要消耗一个糖果
 * 3. 使用Tarjan算法离线计算所有相邻节点对的LCA
 * 4. 使用树上点差分标记路径，然后通过DFS回溯计算每个节点的最终值
 * 
 * 时间复杂度分析：
 * - 建图：O(N)
 * - Tarjan离线LCA：O(N + M)，其中M是查询数量
 * - 树上点差分标记：O(M)
 * - DFS回溯统计：O(N)
 * 总时间复杂度：O(N + M)
 * 
 * 空间复杂度分析：
 * - 树的存储：O(N)
 * - 查询存储：O(M)
 * - 并查集：O(N)
 * - 差分数组：O(N)
 * 总空间复杂度：O(N + M)
 * 
 * 工程化考量：
 * 1. 使用链式前向星存储树结构和查询，提高空间效率和遍历速度
 * 2. 使用Tarjan离线算法计算LCA，避免多次在线查询的开销
 * 3. 使用StreamTokenizer进行高效输入，处理大量数据时性能优于Scanner
 * 4. 使用PrintWriter进行高效输出，支持缓冲
 * 5. 采用静态成员变量减少对象创建，在算法竞赛中常用此技巧
 * 
 * 最优解分析：
 * 本题结合了Tarjan离线LCA和树上点差分两种技术，是解决此类问题的最优解。
 * 相比于在线LCA查询的方法，离线算法可以将时间复杂度从O(M log N)优化到O(N + M)。
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code02_SquirrelHome1 {

	/**
	 * 最大节点数量
	 * 题目中节点数量范围：1 <= N <= 300000
	 */
	public static int MAXN = 300001;

	/**
	 * 依次去往节点的顺序
	 * travel[i]表示第i个访问的节点编号
	 */
	public static int[] travel = new int[MAXN];

	/**
	 * 每个节点需要分配多少糖果
	 * num[i]表示节点i需要准备的糖果数量
	 */
	public static int[] num = new int[MAXN];

	/**
	 * 链式前向星建图 - 树边存储
	 * headEdge[u]: 节点u的第一条树边索引
	 * edgeNext[e]: 边e的下一条边索引
	 * edgeTo[e]: 边e指向的节点
	 * tcnt: 树边计数器
	 */
	public static int[] headEdge = new int[MAXN];
	public static int[] edgeNext = new int[MAXN << 1];
	public static int[] edgeTo = new int[MAXN << 1];
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
	public static int[] queryNext = new int[MAXN << 1];
	public static int[] queryTo = new int[MAXN << 1];
	public static int[] queryIndex = new int[MAXN << 1];
	public static int qcnt;

	/**
	 * Tarjan算法相关数组
	 * visited[u]: 节点u是否已被访问
	 * unionfind[u]: 节点u在并查集中的父节点
	 * father[u]: 节点u在DFS过程中的父节点
	 * ans[i]: 第i次查询的LCA结果
	 */
	public static boolean[] visited = new boolean[MAXN];
	public static int[] unionfind = new int[MAXN];
	public static int[] father = new int[MAXN];
	public static int[] ans = new int[MAXN];

	/**
	 * 初始化算法所需的数据结构
	 * 设置数组初始值，准备处理新的测试用例
	 * 
	 * @param n 节点数量
	 */
	public static void build(int n) {
		// 初始化糖果数组
		Arrays.fill(num, 1, n + 1, 0);
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
	}

	/**
	 * 向链式前向星中添加一条无向树边
	 * 
	 * @param u 边的一个端点
	 * @param v 边的另一个端点
	 */
	public static void addEdge(int u, int v) {
		// 添加u到v的边
		edgeNext[tcnt] = headEdge[u];
		edgeTo[tcnt] = v;
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
	 * 
	 * @param u 当前处理的节点
	 * @param f 当前节点的父节点
	 */
	public static void tarjan(int u, int f) {
		// 标记当前节点已被访问
		visited[u] = true;
		
		// 递归处理所有子节点
		for (int e = headEdge[u], v; e != 0; e = edgeNext[e]) {
			v = edgeTo[e];
			if (v != f) {
				tarjan(v, u);
			}
		}
		
		// 处理所有与当前节点相关的查询
		for (int e = headQuery[u], v; e != 0; e = queryNext[e]) {
			v = queryTo[e];
			// 如果查询的目标节点已被访问，则可以计算LCA
			if (visited[v]) {
				// 两个节点的LCA就是它们所在集合的代表元素
				ans[queryIndex[e]] = find(v);
			}
		}
		
		// 将当前节点与父节点合并到同一集合
		unionfind[u] = f;
		// 记录当前节点的父节点
		father[u] = f;
	}

	/**
	 * DFS回溯计算每个节点的最终糖果数量
	 * 
	 * @param u 当前处理的节点
	 * @param f 当前节点的父节点
	 */
	public static void dfs(int u, int f) {
		// 递归处理所有子节点
		for (int e = headEdge[u], v; e != 0; e = edgeNext[e]) {
			v = edgeTo[e];
			if (v != f) {
				dfs(v, u);
			}
		}
		
		// 将子节点的糖果数量累加到父节点
		for (int e = headEdge[u], v; e != 0; e = edgeNext[e]) {
			v = edgeTo[e];
			if (v != f) {
				num[u] += num[v];
			}
		}
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
		int n = (int) in.nval;
		
		// 初始化数据结构
		build(n);
		
		// 读取访问顺序
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			travel[i] = (int) in.nval;
		}
		
		// 构建树结构
		for (int i = 1, u, v; i < n; i++) {
			in.nextToken();
			u = (int) in.nval;
			in.nextToken();
			v = (int) in.nval;
			addEdge(u, v);
			addEdge(v, u);  // 无向图，添加反向边
		}
		
		// 添加所有查询（相邻节点对的LCA查询）
		for (int i = 1; i < n; i++) {
			// 添加正向查询
			addQuery(travel[i], travel[i + 1], i);
			// 添加反向查询
			addQuery(travel[i + 1], travel[i], i);
		}
		
		// 执行计算
		compute(n);
		
		// 输出每个节点需要的糖果数量
		for (int i = 1; i <= n; i++) {
			out.println(num[i]);
		}
		
		// 确保输出被刷新
		out.flush();
		// 关闭资源
		out.close();
		br.close();
	}

	/**
	 * 执行核心计算逻辑
	 * 
	 * @param n 节点数量
	 */
	public static void compute(int n) {
		// 使用Tarjan算法计算所有查询的LCA
		tarjan(1, 0);
		
		// 对每一条路径执行树上点差分操作
		for (int i = 1, u, v, lca, lcafather; i < n; i++) {
			// 获取路径的两个端点
			u = travel[i];
			v = travel[i + 1];
			// 获取两个端点的LCA
			lca = ans[i];
			// 获取LCA的父节点
			lcafather = father[lca];
			
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
		
		// 通过DFS回溯计算每个节点的最终糖果数量
		dfs(1, 0);
		
		// 调整除最后一个节点外的所有访问节点的糖果数量
		// 因为这些节点在访问时需要消耗一个糖果
		for (int i = 2; i <= n; i++) {
			num[travel[i]]--;
		}
	}

}
package class119;

// 货车运输问题
// 问题描述：
// 一共有n座城市，编号1 ~ n
// 一共有m条双向道路，每条道路(u, v, w)表示有一条限重为w，从u到v的双向道路
// 从一点到另一点的路途中，汽车载重不能超过每一条道路的限重
// 每条查询(a, b)表示从a到b的路线中，汽车允许的最大载重是多少
// 如果从a到b无法到达，那么认为答案是-1
// 一共有q条查询，返回答案数组
// 1 <= n <= 10^4
// 1 <= m <= 5 * 10^4
// 1 <= q <= 3 * 10^4
// 0 <= w <= 10^5
// 1 <= u, v, a, b <= n
// 测试链接 : https://www.luogu.com.cn/problem/P1967
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

/**
 * 货车运输问题解决方案
 * 算法思路：
 * 1. 使用Kruskal算法构建最大生成树，确保连通的城市间路径具有最大载重能力
 * 2. 在生成树上使用树上倍增算法，预处理每个节点的祖先信息和路径最小权重
 * 3. 对于每次查询，使用LCA算法计算两点间路径上的最小权重（即最大载重）
 * 
 * 时间复杂度：
 * - 构建最大生成树：O(m log m)
 * - 预处理：O(n log n)
 * - 单次查询：O(log n)
 * 空间复杂度：O(n log n + m)
 */
public class Code02_Trucking {

	// 最大节点数
	public static int MAXN = 10001;

	// 最大边数
	public static int MAXM = 50001;

	// 最大跳跃级别
	public static int LIMIT = 21;

	// 实际使用的最大跳跃级别
	public static int power;

	// 存储边信息的数组，edges[i][0]表示起点，edges[i][1]表示终点，edges[i][2]表示权重
	public static int[][] edges = new int[MAXM][3];

	// 并查集，用于Kruskal算法
	public static int[] father = new int[MAXN];

	// 给的树有可能是森林，所以需要判断节点是否访问过了
	public static boolean[] visited = new boolean[MAXN];

	// 最大生成树建图，使用链式前向星存储邻接表
	public static int[] head = new int[MAXN];

	// next[i] 表示第i条边的下一条边的索引
	public static int[] next = new int[MAXM << 1];

	// to[i] 表示第i条边指向的节点
	public static int[] to = new int[MAXM << 1];

	// weight[i] 表示第i条边的权重
	public static int[] weight = new int[MAXM << 1];

	// 边的计数器
	public static int cnt;

	// deep[i] : i节点在第几层
	public static int[] deep = new int[MAXN];

	// stjump[u][p] : u节点往上跳2的p次方步，到达什么节点
	public static int[][] stjump = new int[MAXN][LIMIT];

	// stmin[u][p] : u节点往上跳2的p次方步的路径中，最小的权值
	public static int[][] stmin = new int[MAXN][LIMIT];

	/**
	 * 计算log2(n)的值
	 * @param n 输入值
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
	 * 初始化数据结构
	 * @param n 节点数量
	 */
	public static void build(int n) {
		power = log2(n);
		cnt = 1;
		// 初始化并查集
		for (int i = 1; i <= n; i++) {
			father[i] = i;
		}
		// 初始化访问标记和邻接表头
		Arrays.fill(visited, 1, n + 1, false);
		Arrays.fill(head, 1, n + 1, 0);
	}

	/**
	 * 使用Kruskal算法构建最大生成树
	 * 算法思路：
	 * 1. 将所有边按权重从大到小排序
	 * 2. 使用并查集判断是否形成环
	 * 3. 不形成环的边加入生成树
	 * 
	 * 时间复杂度：O(m log m)
	 * 空间复杂度：O(m)
	 * 
	 * @param n 节点数量
	 * @param m 边数量
	 */
	public static void kruskal(int n, int m) {
		// 按权重从大到小排序
		Arrays.sort(edges, 1, m + 1, (a, b) -> b[2] - a[2]);
		// 遍历所有边
		for (int i = 1, a, b, fa, fb; i <= m; i++) {
			a = edges[i][0];
			b = edges[i][1];
			// 查找两个节点的根节点
			fa = find(a);
			fb = find(b);
			// 如果不在同一连通分量中，则将这条边加入生成树
			if (fa != fb) {
				father[fa] = fb;
				// 添加双向边到邻接表中
				addEdge(a, b, edges[i][2]);
				addEdge(b, a, edges[i][2]);
			}
		}
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

	/**
	 * 添加一条边到邻接表中
	 * @param u 起点
	 * @param v 终点
	 * @param w 边权重
	 */
	public static void addEdge(int u, int v, int w) {
		next[cnt] = head[u];
		to[cnt] = v;
		weight[cnt] = w;
		head[u] = cnt++;
	}

	/**
	 * DFS遍历构建倍增表
	 * 算法思路：
	 * 1. 遍历树的每个节点
	 * 2. 构建深度、跳跃表和路径最小权重表
	 * 
	 * 时间复杂度：O(n log n)
	 * 空间复杂度：O(n log n)
	 * 
	 * @param u 当前节点
	 * @param w 到父节点的边权重
	 * @param f 父节点
	 */
	public static void dfs(int u, int w, int f) {
		visited[u] = true;
		// 如果是根节点
		if (f == 0) {
			deep[u] = 1;
			stjump[u][0] = u;
			stmin[u][0] = Integer.MAX_VALUE;
		} else {
			// 记录深度、直接父节点和到父节点的边权重
			deep[u] = deep[f] + 1;
			stjump[u][0] = f;
			stmin[u][0] = w;
		}
		// 构建倍增表
		for (int p = 1; p <= power; p++) {
			// 跳2^p步到达的节点 = 跳2^(p-1)步后再跳2^(p-1)步到达的节点
			stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
			// 路径上的最小权重 = 两段路径最小权重的较小值
			stmin[u][p] = Math.min(stmin[u][p - 1], stmin[stjump[u][p - 1]][p - 1]);
		}
		// 递归处理子节点
		for (int e = head[u]; e != 0; e = next[e]) {
			if (!visited[to[e]]) {
				dfs(to[e], weight[e], u);
			}
		}
	}

	/**
	 * 查询两点间路径上的最小权重（即最大载重）
	 * 算法思路：
	 * 1. 判断两点是否连通
	 * 2. 使用倍增算法找到LCA
	 * 3. 计算路径上的最小权重
	 * 
	 * 时间复杂度：O(log n)
	 * 空间复杂度：O(1)
	 * 
	 * @param a 起点
	 * @param b 终点
	 * @return 两点间路径上的最小权重，如果不连通则返回-1
	 */
	public static int lca(int a, int b) {
		// 判断是否连通
		if (find(a) != find(b)) {
			return -1;
		}
		// 确保a是深度更深的节点
		if (deep[a] < deep[b]) {
			int tmp = a;
			a = b;
			b = tmp;
		}
		// 记录路径上的最小权重
		int ans = Integer.MAX_VALUE;
		// 调整a到与b同一深度，并更新最小权重
		for (int p = power; p >= 0; p--) {
			if (deep[stjump[a][p]] >= deep[b]) {
				ans = Math.min(ans, stmin[a][p]);
				a = stjump[a][p];
			}
		}
		// 如果a和b已经在同一节点，直接返回
		if (a == b) {
			return ans;
		}
		// 同时向上跳跃找到LCA，并更新最小权重
		for (int p = power; p >= 0; p--) {
			if (stjump[a][p] != stjump[b][p]) {
				ans = Math.min(ans, Math.min(stmin[a][p], stmin[b][p]));
				a = stjump[a][p];
				b = stjump[b][p];
			}
		}
		// 更新最后一步的最小权重
		ans = Math.min(ans, Math.min(stmin[a][0], stmin[b][0]));
		return ans;
	}

	/**
	 * 主函数，处理输入和输出
	 * @param args 命令行参数
	 * @throws IOException IO异常
	 */
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		int n = (int) in.nval;
		in.nextToken();
		int m = (int) in.nval;
		// 读取所有边信息
		for (int i = 1; i <= m; i++) {
			in.nextToken();
			edges[i][0] = (int) in.nval;
			in.nextToken();
			edges[i][1] = (int) in.nval;
			in.nextToken();
			edges[i][2] = (int) in.nval;
		}
		// 初始化数据结构
		build(n);
		// 使用Kruskal算法构建最大生成树
		kruskal(n, m);
		// 处理可能的森林情况，对每个连通分量进行DFS
		for (int i = 1; i <= n; i++) {
			if (!visited[i]) {
				dfs(i, 0, 0);
			}
		}
		in.nextToken();
		int q = (int) in.nval;
		// 处理查询
		for (int i = 1, a, b; i <= q; i++) {
			in.nextToken();
			a = (int) in.nval;
			in.nextToken();
			b = (int) in.nval;
			out.println(lca(a, b));
		}
		out.flush();
		out.close();
		br.close();
	}

}
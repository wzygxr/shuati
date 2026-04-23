package class192;

// 只能一个方向，java版
// 给定一张无向图，一共n个点、m条边，图上可能有多个连通区
// 给定q条要求，格式 x y : 从点x出发，要求可以去往点y
// 你必须把每条无向边变成有向边，也就是每条边确定唯一的方向
// 如果改造后能满足所有要求，打印"Yes"，如果不存在方案，打印"No"
// 1 <= n、m <= 2 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/CF555E
// 测试链接 : https://codeforces.com/problemset/problem/555/E
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

// 引入Java IO相关的类
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Code06_OneDirection1 - 只能一个方向问题
 * 
 * 问题描述：
 * - 给定一张无向图，n个节点，m条边，可能有多个连通分量
 * - 有q条要求，每条要求为(x, y)：从点x出发要能到达点y
 * - 你需要将每条无向边改为有向边（确定唯一方向）
 * - 判断是否存在一种改边方案，使得所有要求都能满足
 * 
 * 算法思路：
 * 1. 使用Tarjan算法求边双连通分量（E-DCC）
 * 2. 将边双连通分量缩点，形成一棵树（森林）
 * 3. 对于每条要求(x, y)：
 *    - 如果x和y不在同一个连通分量，无解
 *    - 将路径上的边标记方向：从x到lca的方向是向上的，从lca到y的方向是向下的
 * 4. 使用upCnt和downCnt数组记录每个节点需要的向上和向下方向边的数量
 * 5. 最后检查每个节点是否同时需要向上和向下的方向：
 *    - 如果同时需要，说明该节点的两条不同子树都需要不同方向的边，矛盾，无解
 *    - 如果只有一种方向，或者没有要求，则有解
 */
public class Code06_OneDirection1 {

	// ------------------- 静态常量定义 -------------------
	
	// 最大节点数量
	public static int MAXN = 200001;
	// 最大边数量
	public static int MAXM = 200001;
	// 最大二进制位数
	public static int MAXP = 20;
	
	// ------------------- 图的输入数据 -------------------
	
	// 节点数量、边数量、查询数量
	public static int n, m, q;
	// 边的端点数组
	public static int[] a = new int[MAXM];
	public static int[] b = new int[MAXM];

	// ------------------- 邻接表数据结构 -------------------
	
	// 头指针数组
	public static int[] head = new int[MAXN];
	// 下一条边指针数组
	public static int[] nxt = new int[MAXM << 1];
	// 边的终点数组
	public static int[] to = new int[MAXM << 1];
	// 当前边的编号计数器
	public static int cntg;

	// ------------------- Tarjan算法相关变量 -------------------
	
	// 深度优先搜索编号数组
	public static int[] dfn = new int[MAXN];
	// Low值数组
	public static int[] low = new int[MAXN];
	// 时间戳计数器
	public static int cntd;

	// ------------------- 栈相关变量 -------------------
	
	// Tarjan算法使用的栈
	public static int[] sta = new int[MAXN];
	// 栈顶指针
	public static int top;

	// ------------------- 边双连通分量相关变量 -------------------
	
	// 节点所属的边双连通分量编号
	public static int[] belong = new int[MAXN];
	// 边双连通分量的数量计数器
	public static int ebccCnt;

	// ------------------- LCA相关变量 -------------------
	
	// block[u]表示节点u属于哪个连通块（树）
	public static int[] block = new int[MAXN];
	// dep[u]表示节点u在树中的深度
	public static int[] dep = new int[MAXN];
	// stjump[u][p]表示节点u的第2^p个祖先
	public static int[][] stjump = new int[MAXN][MAXP];

	// ------------------- 方向标记相关变量 -------------------
	
	// vis[u]表示节点u是否已经被检查过
	public static boolean[] vis = new boolean[MAXN];
	// upCnt[u]表示有多少条路径需要从u向上（朝向父节点方向）
	public static int[] upCnt = new int[MAXN];
	// downCnt[u]表示有多少条路径需要从u向下（朝向子节点方向）
	public static int[] downCnt = new int[MAXN];

	/**
	 * 添加一条无向边到邻接表中
	 * 
	 * @param u 边的第一个端点
	 * @param v 边的第二个端点
	 */
	public static void addEdge(int u, int v) {
		nxt[++cntg] = head[u];
		to[cntg] = v;
		head[u] = cntg;
	}

	/**
	 * Tarjan算法求边双连通分量（E-DCC）
	 * 
	 * @param u 当前节点
	 * @param preEdge 来自父节点的边编号
	 */
	public static void tarjan(int u, int preEdge) {
		dfn[u] = low[u] = ++cntd;
		sta[++top] = u;
		for (int e = head[u]; e > 0; e = nxt[e]) {
			if ((e ^ 1) == preEdge) {
				continue;
			}
			int v = to[e];
			if (dfn[v] == 0) {
				tarjan(v, e);
				low[u] = Math.min(low[u], low[v]);
			} else {
				low[u] = Math.min(low[u], dfn[v]);
			}
		}
		if (dfn[u] == low[u]) {
			ebccCnt++;
			int pop;
			do {
				pop = sta[top--];
				belong[pop] = ebccCnt;
			} while (pop != u);
		}
	}

	/**
	 * 将原图缩点，构建边双连通分量树（森林）
	 */
	public static void condense() {
		cntg = 0;
		for (int i = 1; i <= ebccCnt; i++) {
			head[i] = 0;
		}
		for (int i = 1; i <= m; i++) {
			int ebcc1 = belong[a[i]];
			int ebcc2 = belong[b[i]];
			if (ebcc1 != ebcc2) {
				addEdge(ebcc1, ebcc2);
				addEdge(ebcc2, ebcc1);
			}
		}
	}

	/**
	 * 在边双连通分量森林上进行DFS，预处理LCA
	 * 
	 * @param u 当前节点
	 * @param fa 父节点
	 * @param bid 连通块编号
	 */
	public static void dfs(int u, int fa, int bid) {
		block[u] = bid;
		dep[u] = dep[fa] + 1;
		stjump[u][0] = fa;
		for (int p = 1; p < MAXP; p++) {
			stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
		}
		for (int e = head[u]; e > 0; e = nxt[e]) {
			int v = to[e];
			if (v != fa) {
				dfs(v, u, bid);
			}
		}
	}

	/**
	 * 求两个节点的最近公共祖先（LCA）
	 * 
	 * @param a 第一个节点
	 * @param b 第二个节点
	 * @return 最近公共祖先
	 */
	public static int getLca(int a, int b) {
		if (dep[a] < dep[b]) {
			int tmp = a; a = b; b = tmp;
		}
		for (int p = MAXP - 1; p >= 0; p--) {
			if (dep[stjump[a][p]] >= dep[b]) {
				a = stjump[a][p];
			}
		}
		if (a == b) {
			return a;
		}
		for (int p = MAXP - 1; p >= 0; p--) {
			if (stjump[a][p] != stjump[b][p]) {
				a = stjump[a][p];
				b = stjump[b][p];
			}
		}
		return stjump[a][0];
	}

	/**
	 * 检查某个节点及其子树是否满足方向要求
	 * 
	 * @param u 当前节点
	 * @param fa 父节点
	 * @return 是否满足要求
	 */
	public static boolean check(int u, int fa) {
		vis[u] = true;
		for (int e = head[u]; e > 0; e = nxt[e]) {
			int v = to[e];
			if (v != fa) {
				if (!check(v, u)) {
					return false;
				}
				// 累加子节点的计数
				upCnt[u] += upCnt[v];
				downCnt[u] += downCnt[v];
			}
		}
		// 如果同时需要向上和向下的方向，则矛盾
		return upCnt[u] == 0 || downCnt[u] == 0;
	}

	/**
	 * 主函数，程序入口
	 * 
	 * @param args 命令行参数
	 * @throws Exception 输入输出异常
	 */
	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		cntg = 1;
		n = in.nextInt();
		m = in.nextInt();
		q = in.nextInt();
		
		// 读取所有边
		for (int i = 1; i <= m; i++) {
			a[i] = in.nextInt();
			b[i] = in.nextInt();
			addEdge(a[i], b[i]);
			addEdge(b[i], a[i]);
		}
		
		// 对每个连通分量运行Tarjan算法
		for (int i = 1; i <= n; i++) {
			if (dfn[i] == 0) {
				tarjan(i, 0);
			}
		}
		
		// 构建边双连通分量森林
		condense();
		
		// 对每个连通块进行DFS，预处理LCA
		for (int i = 1, b = 0; i <= ebccCnt; i++) {
			if (block[i] == 0) {
				dfs(i, 0, ++b);
			}
		}
		
		boolean ans = true;
		
		// 处理每条要求
		for (int i = 1, x, y, xylca; i <= q; i++) {
			x = in.nextInt();
			y = in.nextInt();
			
			// 将节点转换为它们所属的边双连通分量编号
			x = belong[x];
			y = belong[y];
			
			// 如果x和y不在同一个连通块，无解
			if (block[x] != block[y]) {
				ans = false;
				break;
			}
			
			// 求x和y的最近公共祖先
			xylca = getLca(x, y);
			
			// 路径(x, y)可以分成两段：
			// 1. x到xylca：方向向上（从子节点到父节点）
			// 2. xylca到y：方向向下（从父节点到子节点）
			
			// x到xylca的路径上，所有节点都需要向上方向
			upCnt[x]++;
			upCnt[xylca]--;
			
			// xylca到y的路径上，所有节点都需要向下方向
			downCnt[y]++;
			downCnt[xylca]--;
		}
		
		// 检查是否满足方向要求
		if (ans) {
			for (int i = 1; i <= ebccCnt; i++) {
				if (!vis[i] && !check(i, 0)) {
					ans = false;
					break;
				}
			}
		}
		
		// 输出结果
		out.println(ans ? "Yes" : "No");
		out.flush();
		out.close();
	}

	/**
	 * FastReader - 高效输入读取器
	 */
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

package class164;

/**
 * 加边直到连通 - Java实现
 * 
 * 题目来源：Codeforces 1706E Qpwoeirut and Vertices
 * 题目链接：https://codeforces.com/problemset/problem/1706/E
 * 洛谷链接：https://www.luogu.com.cn/problem/CF1706E
 * 
 * 题目描述：
 * 图里有n个点，m条无向边，点的编号1~n，边的编号1~m，所有点都连通
 * 一共有q条查询，每条查询格式如下
 * 查询 l r : 至少要加完编号前多少的边，才能使得[l, r]中的所有点连通
 * 
 * 算法核心思想：
 * 本题是Kruskal重构树与ST表的结合应用。
 * 通过构建按边编号升序的Kruskal重构树，可以快速找到使区间内所有节点连通所需的最少边数。
 * 结合DFS序和ST表，可以快速找到区间内节点的最小和最大DFS序号，
 * 然后通过LCA找到对应的祖先节点，其权值即为答案。
 * 
 * 解题思路：
 * 1. 构建按边编号升序的Kruskal重构树，节点权值为边的编号
 * 2. 在重构树上进行DFS，记录每个节点的DFS序号
 * 3. 构建ST表用于快速查询区间内的最小和最大DFS序号
 * 4. 对于每个查询[l, r]，找到区间内节点的最小和最大DFS序号对应的节点
 * 5. 计算这两个节点的LCA，其权值即为使区间内所有节点连通所需的最少边数
 * 
 * 时间复杂度分析：
 * - 构建Kruskal重构树：O(m log m)
 * - DFS预处理：O(n)
 * - 构建ST表：O(n log n)
 * - 每次查询：O(1)（ST表查询）+ O(log n)（LCA查询）
 * 总复杂度：O(m log m + n log n + q log n)
 * 
 * 空间复杂度分析：
 * - 存储边：O(m)
 * - 存储图和重构树：O(n)
 * - 倍增表：O(n log n)
 * - ST表：O(n log n)
 * 总空间复杂度：O(n log n + m)
 * 
 * 工程化考量：
 * 1. 异常处理：处理各种查询情况的边界条件
 * 2. 性能优化：使用快速IO和路径压缩并查集
 * 3. 内存管理：合理分配数组空间
 * 4. 特殊情况：处理l == r的特殊情况
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class Code05_UntilConnect1 {

	public static int MAXN = 100001;  // 最大节点数
	public static int MAXK = 200001;  // 最大重构树节点数
	public static int MAXM = 200001;  // 最大边数
	public static int MAXH = 20;      // 倍增表最大层数(2^20 > 1e5)
	public static int t, n, m, q;     // 测试用例数、节点数、边数、查询数
	public static int[][] edge = new int[MAXM][3]; // 边信息：起点、终点、边编号

	// 并查集 - 用于构建Kruskal重构树
	public static int[] father = new int[MAXK];

	// Kruskal重构树 - 邻接表存储
	public static int[] head = new int[MAXK];  // 重构树邻接表头节点
	public static int[] next = new int[MAXK];  // 重构树下一条边
	public static int[] to = new int[MAXK];    // 重构树边的目标节点
	public static int cntg;                    // 重构树边计数器
	public static int[] nodeKey = new int[MAXK]; // 重构树节点权值（对应原图中的边编号）
	public static int cntu;                    // 重构树节点总数

	// 树上信息
	public static int[] dep = new int[MAXK];        // 节点深度
	public static int[] dfn = new int[MAXK];        // 节点DFS序号
	public static int[] seg = new int[MAXK];        // seg[i] = j，代表DFS序号为i的节点对应原始节点编号j
	public static int[][] stjump = new int[MAXK][MAXH]; // 倍增表
	public static int cntd;                         // DFS序号计数器

	// ST表 - 用于快速查询区间内的最小和最大DFS序号
	public static int[] lg2 = new int[MAXN];      // 预处理log2值
	public static int[][] stmax = new int[MAXN][MAXH]; // 区间最大值ST表
	public static int[][] stmin = new int[MAXN][MAXH]; // 区间最小值ST表

	/**
	 * 清理函数 - 初始化图和计数器
	 */
	public static void clear() {
		cntg = cntd = 0;
		Arrays.fill(head, 1, n * 2, 0);
	}

	/**
	 * 并查集查找函数 - 带路径压缩优化
	 * 时间复杂度：近似O(1)
	 * @param i 要查找的节点
	 * @return 节点所在集合的根节点
	 */
	public static int find(int i) {
		if (i != father[i]) {
			father[i] = find(father[i]);
		}
		return father[i];
	}

	/**
	 * 重构树添加边函数
	 * @param u 边的起点
	 * @param v 边的终点
	 */
	public static void addEdge(int u, int v) {
		next[++cntg] = head[u];
		to[cntg] = v;
		head[u] = cntg;
	}

	/**
	 * 构建Kruskal重构树（按边编号升序）
	 * 
	 * 实现细节：
	 * 1. 初始化并查集
	 * 2. 按边编号升序排序
	 * 3. 遍历排序后的边，使用并查集检查连通性
	 * 4. 当发现不在同一连通分量的边时，创建新节点并构建重构树
	 */
	public static void kruskalRebuild() {
		// 初始化并查集
		for (int i = 1; i <= n; i++) {
			father[i] = i;
		}
		// 按边编号升序排序
		Arrays.sort(edge, 1, m + 1, (a, b) -> a[2] - b[2]);
		cntu = n;
		for (int i = 1, fx, fy; i <= m; i++) {
			fx = find(edge[i][0]);
			fy = find(edge[i][1]);
			if (fx != fy) {
				// 合并两个连通分量
				father[fx] = father[fy] = ++cntu;
				father[cntu] = cntu;
				// 新节点的权值为当前边的编号
				nodeKey[cntu] = edge[i][2];
				// 建立重构树的父子关系
				addEdge(cntu, fx);
				addEdge(cntu, fy);
			}
		}
	}

	/**
	 * DFS函数 - 记录每个节点的DFS序号，构建倍增表
	 * 
	 * 功能说明：
	 * 1. 记录每个节点的深度和DFS序号
	 * 2. 构建倍增表用于后续LCA查询
	 * 
	 * @param u 当前节点
	 * @param fa 父节点
	 */
	public static void dfs(int u, int fa) {
		dep[u] = dep[fa] + 1;
		dfn[u] = ++cntd;
		seg[cntd] = u;
		stjump[u][0] = fa;
		for (int p = 1; p < MAXH; p++) {
			stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
		}
		for (int e = head[u]; e > 0; e = next[e]) {
			dfs(to[e], u);
		}
	}

	/**
	 * 构建ST表 - 用于快速查询区间内的最小和最大DFS序号
	 * 
	 * 实现思路：
	 * 1. 预处理log2值
	 * 2. 初始化ST表的第一层
	 * 3. 动态规划构建ST表的其他层
	 */
	// 构建数组上的st表，讲解117进行了详细的讲述
	public static void buildst() {
		lg2[0] = -1;
		for (int i = 1; i <= n; i++) {
			lg2[i] = lg2[i >> 1] + 1;
			stmax[i][0] = dfn[i];
			stmin[i][0] = dfn[i];
		}
		for (int p = 1; p <= lg2[n]; p++) {
			for (int i = 1; i + (1 << p) - 1 <= n; i++) {
				stmax[i][p] = Math.max(stmax[i][p - 1], stmax[i + (1 << (p - 1))][p - 1]);
				stmin[i][p] = Math.min(stmin[i][p - 1], stmin[i + (1 << (p - 1))][p - 1]);
			}
		}
	}

	/**
	 * ST表查询函数 - 查询区间[l..r]内的最小DFS序号
	 * @param l 区间左端点
	 * @param r 区间右端点
	 * @return 区间内的最小DFS序号
	 */
	// 根据st表，[l..r]范围上的最小值，讲解117进行了详细的讲述
	public static int dfnmin(int l, int r) {
		int p = lg2[r - l + 1];
		int ans = Math.min(stmin[l][p], stmin[r - (1 << p) + 1][p]);
		return ans;
	}

	/**
	 * ST表查询函数 - 查询区间[l..r]内的最大DFS序号
	 * @param l 区间左端点
	 * @param r 区间右端点
	 * @return 区间内的最大DFS序号
	 */
	// 根据st表，[l..r]范围上的最大值，讲解117进行了详细的讲述
	public static int dfnmax(int l, int r) {
		int p = lg2[r - l + 1];
		int ans = Math.max(stmax[l][p], stmax[r - (1 << p) + 1][p]);
		return ans;
	}

	/**
	 * 倍增法查询LCA（最近公共祖先）
	 * 
	 * 功能说明：
	 * 查找两个节点的最近公共祖先
	 * 
	 * @param a 第一个节点
	 * @param b 第二个节点
	 * @return 两节点的最近公共祖先
	 */
	public static int lca(int a, int b) {
		// 保证a在更深的位置
		if (dep[a] < dep[b]) {
			int tmp = a;
			a = b;
			b = tmp;
		}
		// 将a提升到和b同一深度
		for (int p = MAXH - 1; p >= 0; p--) {
			if (dep[stjump[a][p]] >= dep[b]) {
				a = stjump[a][p];
			}
		}
		// 如果此时a==b，说明b是a的祖先，直接返回
		if (a == b) {
			return a;
		}
		// 同时向上提升a和b，直到它们的父节点相同
		for (int p = MAXH - 1; p >= 0; p--) {
			if (stjump[a][p] != stjump[b][p]) {
				a = stjump[a][p];
				b = stjump[b][p];
			}
		}
		// 返回共同的父节点，即为LCA
		return stjump[a][0];
	}

	/**
	 * 查询函数 - 计算使区间[l, r]内所有节点连通所需的最少边数
	 * 
	 * 实现思路：
	 * 1. 找到区间内节点的最小和最大DFS序号对应的节点
	 * 2. 计算这两个节点的LCA
	 * 3. LCA的权值即为答案
	 * 
	 * @param l 区间左端点
	 * @param r 区间右端点
	 * @return 使区间内所有节点连通所需的最少边数
	 */
	public static int query(int l, int r) {
		int x = seg[dfnmin(l, r)];  // 区间内最小DFS序号对应的节点
		int y = seg[dfnmax(l, r)];  // 区间内最大DFS序号对应的节点
		return nodeKey[lca(x, y)];  // LCA的权值即为答案
	}

	public static void main(String[] args) {
		FastIO io = new FastIO(System.in, System.out);
		t = io.nextInt();
		for (int test = 1; test <= t; test++) {
			n = io.nextInt();
			m = io.nextInt();
			q = io.nextInt();
			for (int i = 1; i <= m; i++) {
				edge[i][0] = io.nextInt();
				edge[i][1] = io.nextInt();
				edge[i][2] = i;  // 边的编号就是i
			}
			clear();
			kruskalRebuild();
			dfs(cntu, 0);
			buildst();
			for (int i = 1, l, r; i <= q; i++) {
				l = io.nextInt();
				r = io.nextInt();
				if (l == r) {
					io.write("0 ");
				} else {
					io.write(query(l, r) + " ");
				}
			}
			io.write("\n");
		}
		io.flush();
	}

	// 读写工具类
	static class FastIO {
		private final InputStream is;
		private final OutputStream os;
		private final byte[] inbuf = new byte[1 << 16];
		private int lenbuf = 0;
		private int ptrbuf = 0;
		private final StringBuilder outBuf = new StringBuilder();

		public FastIO(InputStream is, OutputStream os) {
			this.is = is;
			this.os = os;
		}

		private int readByte() {
			if (ptrbuf >= lenbuf) {
				ptrbuf = 0;
				try {
					lenbuf = is.read(inbuf);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				if (lenbuf == -1) {
					return -1;
				}
			}
			return inbuf[ptrbuf++] & 0xff;
		}

		private int skip() {
			int b;
			while ((b = readByte()) != -1) {
				if (b > ' ') {
					return b;
				}
			}
			return -1;
		}

		public int nextInt() {
			int b = skip();
			if (b == -1) {
				throw new RuntimeException("No more integers (EOF)");
			}
			boolean negative = false;
			if (b == '-') {
				negative = true;
				b = readByte();
			}
			int val = 0;
			while (b >= '0' && b <= '9') {
				val = val * 10 + (b - '0');
				b = readByte();
			}
			return negative ? -val : val;
		}

		public void write(String s) {
			outBuf.append(s);
		}

		public void writeInt(int x) {
			outBuf.append(x);
		}

		public void writelnInt(int x) {
			outBuf.append(x).append('\n');
		}

		public void flush() {
			try {
				os.write(outBuf.toString().getBytes());
				os.flush();
				outBuf.setLength(0);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
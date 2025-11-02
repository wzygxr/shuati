package class164;

/**
 * 归程 - Java实现
 * 
 * 题目来源：NOI2018 归程 (洛谷P4768)
 * 题目链接：https://www.luogu.com.cn/problem/P4768
 * 
 * 题目描述：
 * 一共有n个点，m条无向边，原图连通，每条边有长度l和海拔a
 * 一共有q条查询，格式如下
 * 查询 x y : 起初走过海拔 > y的边免费，可视为开车，但是车不能走海拔 <= y的边
 *            你可以在任意节点下车，车不能再用
 *            下车后经过每条边的长度(包括海拔 > y 的边)，都算入步行长度
 *            你想从点x到1号点，打印最小步行长度
 * 
 * 算法核心思想：
 * 本题是Kruskal重构树与最短路算法的结合应用。
 * 通过构建按海拔降序的Kruskal重构树，可以快速找到从某个节点出发，
 * 在海拔限制下能到达的所有节点，然后在这些节点中找到到1号节点的最短距离。
 * 
 * 解题思路：
 * 1. 首先使用Dijkstra算法预处理出每个节点到1号节点的最短距离
 * 2. 构建按海拔降序的Kruskal重构树，节点权值为边的海拔
 * 3. 在重构树上进行DFS，计算每个节点子树中到1号节点的最小距离
 * 4. 对于每个查询，从查询节点向上跳找到满足海拔条件的最浅祖先节点
 * 5. 该祖先节点子树中到1号节点的最小距离即为答案
 * 
 * 时间复杂度分析：
 * - Dijkstra算法：O((n + m) log n)
 * - 构建Kruskal重构树：O(m log m)
 * - DFS预处理：O(n)
 * - 每次查询：O(log n)
 * 总复杂度：O((n + m) log n + m log m + q log n)
 * 
 * 空间复杂度分析：
 * - 存储边：O(m)
 * - 存储图和重构树：O(n)
 * - 倍增表：O(n log n)
 * - 最短路数组：O(n)
 * 总空间复杂度：O(n log n + m)
 * 
 * 工程化考量：
 * 1. 异常处理：处理各种查询情况的边界条件
 * 2. 性能优化：使用优先队列优化Dijkstra算法，快速IO优化输入输出
 * 3. 内存管理：合理分配数组空间
 * 4. 强制在线：正确处理强制在线查询的参数转换
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.PriorityQueue;

public class Code04_Journey1 {

	public static int MAXN = 200001;  // 最大节点数
	public static int MAXK = 400001;  // 最大重构树节点数
	public static int MAXM = 400001;  // 最大边数
	public static int MAXH = 20;      // 倍增表最大层数(2^20 > 1e5)
	public static int INF = 2000000001; // 无穷大
	public static int t, n, m, q, k, s; // 测试用例数、节点数、边数、查询数、参数k、参数s
	public static int[][] edge = new int[MAXM][4]; // 边信息：起点、终点、长度、海拔

	// 原图建图 - 邻接表存储
	public static int[] headg = new int[MAXN];      // 原图邻接表头节点
	public static int[] nextg = new int[MAXM << 1]; // 原图下一条边
	public static int[] tog = new int[MAXM << 1];   // 原图边的目标节点
	public static int[] weightg = new int[MAXM << 1]; // 原图边的权重
	public static int cntg;                         // 原图边计数器

	// Dijkstra算法相关
	public static int[] dist = new int[MAXN];           // 到1号节点的最短距离
	public static boolean[] visit = new boolean[MAXN];  // 访问标记
	public static PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[1] - b[1]); // 优先队列

	// 并查集 - 用于构建Kruskal重构树
	public static int[] father = new int[MAXK];
	public static int[] stack = new int[MAXK];

	// Kruskal重构树 - 邻接表存储
	public static int[] headk = new int[MAXK];  // 重构树邻接表头节点
	public static int[] nextk = new int[MAXK];  // 重构树下一条边
	public static int[] tok = new int[MAXK];    // 重构树边的目标节点
	public static int cntk;                     // 重构树边计数器
	public static int[] nodeKey = new int[MAXK]; // 重构树节点权值（对应原图中的海拔）
	public static int cntu;                     // 重构树节点总数

	// 树上dfs信息
	public static int[] mindist = new int[MAXK];     // 每个节点子树中到1号节点的最小距离
	public static int[][] stjump = new int[MAXK][MAXH]; // 倍增表

	/**
	 * 清理函数 - 初始化图和重构树
	 */
	public static void clear() {
		cntg = cntk = 0;
		Arrays.fill(headg, 1, n + 1, 0);
		Arrays.fill(headk, 1, n * 2, 0);
	}

	/**
	 * 原图添加边函数
	 * @param u 边的起点
	 * @param v 边的终点
	 * @param w 边的权重
	 */
	public static void addEdgeG(int u, int v, int w) {
		nextg[++cntg] = headg[u];
		tog[cntg] = v;
		weightg[cntg] = w;
		headg[u] = cntg;
	}

	/**
	 * Dijkstra算法 - 计算每个节点到1号节点的最短距离
	 */
	public static void dijkstra() {
		// 建立原图
		for (int i = 1; i <= m; i++) {
			addEdgeG(edge[i][0], edge[i][1], edge[i][2]);
			addEdgeG(edge[i][1], edge[i][0], edge[i][2]);
		}
		// 初始化距离数组和访问数组
		Arrays.fill(dist, 1, n + 1, INF);
		Arrays.fill(visit, 1, n + 1, false);
		dist[1] = 0;
		heap.add(new int[] { 1, 0 });
		int[] cur;
		int x, v;
		while (!heap.isEmpty()) {
			cur = heap.poll();
			x = cur[0];
			v = cur[1];
			if (!visit[x]) {
				visit[x] = true;
				for (int e = headg[x], y, w; e > 0; e = nextg[e]) {
					y = tog[e];
					w = weightg[e];
					if (!visit[y] && dist[y] > v + w) {
						dist[y] = v + w;
						heap.add(new int[] { y, dist[y] });
					}
				}
			}
		}
	}

	/**
	 * 重构树添加边函数
	 * @param u 边的起点
	 * @param v 边的终点
	 */
	public static void addEdgeK(int u, int v) {
		nextk[++cntk] = headk[u];
		tok[cntk] = v;
		headk[u] = cntk;
	}

	/**
	 * 并查集查找函数 - 带路径压缩优化（迭代版）
	 * 时间复杂度：近似O(1)
	 * @param i 要查找的节点
	 * @return 节点所在集合的根节点
	 */
	// 并查集的find方法，需要改成迭代版不然会爆栈，C++实现不需要
	public static int find(int i) {
		int size = 0;
		while (i != father[i]) {
			stack[size++] = i;
			i = father[i];
		}
		while (size > 0) {
			father[stack[--size]] = i;
		}
		return i;
	}

	/**
	 * 构建Kruskal重构树（按海拔降序）
	 * 
	 * 实现细节：
	 * 1. 初始化并查集
	 * 2. 按海拔降序排序
	 * 3. 遍历排序后的边，使用并查集检查连通性
	 * 4. 当发现不在同一连通分量的边时，创建新节点并构建重构树
	 */
	public static void kruskalRebuild() {
		// 初始化并查集
		for (int i = 1; i <= n; i++) {
			father[i] = i;
		}
		// 按海拔降序排序
		Arrays.sort(edge, 1, m + 1, (a, b) -> b[3] - a[3]);
		cntu = n;
		for (int i = 1, fx, fy; i <= m; i++) {
			fx = find(edge[i][0]);
			fy = find(edge[i][1]);
			if (fx != fy) {
				// 合并两个连通分量
				father[fx] = father[fy] = ++cntu;
				father[cntu] = cntu;
				// 新节点的权值为当前边的海拔
				nodeKey[cntu] = edge[i][3];
				// 建立重构树的父子关系
				addEdgeK(cntu, fx);
				addEdgeK(cntu, fy);
			}
		}
	}

	// dfs1是递归函数，需要改成迭代版不然会爆栈，C++实现不需要
	public static void dfs1(int u, int fa) {
		stjump[u][0] = fa;
		for (int p = 1; p < MAXH; p++) {
			stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
		}
		for (int e = headk[u]; e > 0; e = nextk[e]) {
			dfs1(tok[e], u);
		}
		if (u <= n) {
			mindist[u] = dist[u];
		} else {
			mindist[u] = INF;
		}
		for (int e = headk[u]; e > 0; e = nextk[e]) {
			mindist[u] = Math.min(mindist[u], mindist[tok[e]]);
		}
	}

	public static int[][] ufe = new int[MAXK][3];

	public static int stacksize, u, f, e;

	public static void push(int u, int f, int e) {
		ufe[stacksize][0] = u;
		ufe[stacksize][1] = f;
		ufe[stacksize][2] = e;
		stacksize++;
	}

	public static void pop() {
		--stacksize;
		u = ufe[stacksize][0];
		f = ufe[stacksize][1];
		e = ufe[stacksize][2];
	}

	/**
	 * DFS迭代版本 - 计算每个节点子树中到1号节点的最小距离
	 * 
	 * 功能说明：
	 * 1. 构建倍增表用于后续查询
	 * 2. 计算每个节点子树中到1号节点的最小距离
	 * 
	 * @param cur 当前节点
	 * @param fa 父节点
	 */
	// dfs2是dfs1的迭代版
	public static void dfs2(int cur, int fa) {
		stacksize = 0;
		push(cur, fa, -1);
		while (stacksize > 0) {
			pop();
			if (e == -1) {
				stjump[u][0] = f;
				for (int p = 1; p < MAXH; p++) {
					stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
				}
				e = headk[u];
			} else {
				e = nextk[e];
			}
			if (e != 0) {
				push(u, f, e);
				push(tok[e], u, -1);
			} else {
				if (u <= n) {
					mindist[u] = dist[u];
				} else {
					mindist[u] = INF;
				}
				for (int ei = headk[u]; ei > 0; ei = nextk[ei]) {
					mindist[u] = Math.min(mindist[u], mindist[tok[ei]]);
				}
			}
		}
	}

	/**
	 * 查询函数 - 从节点node出发，在海拔>line的限制下能到达的节点中到1号节点的最小距离
	 * 
	 * 实现思路：
	 * 1. 从节点node开始，向上跳找到满足海拔条件的最浅祖先节点
	 * 2. 该祖先节点子树中到1号节点的最小距离即为答案
	 * 
	 * @param node 起始节点
	 * @param line 海拔限制
	 * @return 到1号节点的最小步行距离
	 */
	public static int query(int node, int line) {
		for (int p = MAXH - 1; p >= 0; p--) {
			if (stjump[node][p] > 0 && nodeKey[stjump[node][p]] > line) {
				node = stjump[node][p];
			}
		}
		return mindist[node];
	}

	public static void main(String[] args) {
		FastIO io = new FastIO(System.in, System.out);
		t = io.nextInt();
		for (int test = 1; test <= t; test++) {
			n = io.nextInt();
			m = io.nextInt();
			clear();
			for (int i = 1; i <= m; i++) {
				edge[i][0] = io.nextInt();
				edge[i][1] = io.nextInt();
				edge[i][2] = io.nextInt();
				edge[i][3] = io.nextInt();
			}
			dijkstra();
			kruskalRebuild();
			dfs2(cntu, 0);
			q = io.nextInt();
			k = io.nextInt();
			s = io.nextInt();
			for (int i = 1, x, y, lastAns = 0; i <= q; i++) {
				x = (io.nextInt() + k * lastAns - 1) % n + 1;
				y = (io.nextInt() + k * lastAns) % (s + 1);
				lastAns = query(x, y);
				io.writelnInt(lastAns);
			}
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
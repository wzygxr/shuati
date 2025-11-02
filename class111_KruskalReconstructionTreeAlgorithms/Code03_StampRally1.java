package class164;

/**
 * 边的最大编号的最小值 - Java实现
 * 
 * 题目来源：AtCoder AGC002D Stamp Rally
 * 题目链接：https://atcoder.jp/contests/agc002/tasks/agc002_d
 * 洛谷链接：https://www.luogu.com.cn/problem/AT_agc002_d
 * 
 * 题目描述：
 * 图里有n个点，m条无向边，边的编号1~m，没有边权，所有点都连通
 * 一共有q条查询，查询的格式如下
 * 查询 x y z : 从两个点x和y出发，希望经过的点数量等于z
 *              每个点可以重复经过，但是重复经过只计算一次
 *              经过边的最大编号，最小是多少
 * 
 * 算法核心思想：
 * 本题是Kruskal重构树的经典应用之一，结合了二分答案的思想。
 * 通过构建以边编号为权值的Kruskal重构树，可以快速判断在只使用编号不超过某个值的边时，
 * 从两个点出发能到达的节点数量是否满足要求。
 * 
 * 解题思路：
 * 1. 将边按编号从小到大排序，构建Kruskal重构树，节点权值为边编号
 * 2. 在重构树上进行DFS，计算每个节点子树中叶节点的数量
 * 3. 对于每个查询，二分答案mid，检查使用编号不超过mid的边是否能满足要求
 * 4. 检查函数中，从x和y分别向上跳找到对应的祖先节点
 * 5. 如果两个祖先节点相同，说明x和y在同一连通分量中，计算该连通分量的节点数
 * 6. 如果两个祖先节点不同，说明x和y在不同连通分量中，计算两个连通分量的节点数之和
 * 
 * 时间复杂度分析：
 * - 构建Kruskal重构树：O(m log m)
 * - DFS预处理：O(n)
 * - 每次查询：O(log m * log n)
 * 总复杂度：O(m log m + q * log m * log n)
 * 
 * 空间复杂度分析：
 * - 存储边：O(m)
 * - 存储图和重构树：O(n)
 * - 倍增表：O(n log n)
 * 总空间复杂度：O(n log n + m)
 * 
 * 工程化考量：
 * 1. 异常处理：处理各种查询情况的边界条件
 * 2. 性能优化：使用快速IO和路径压缩并查集
 * 3. 内存管理：合理分配数组空间
 * 4. 算法优化：结合二分答案减少查询复杂度
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class Code03_StampRally1 {

	public static int MAXK = 200001;  // 最大节点数（重构树节点数最多为2n-1）
	public static int MAXM = 100001;  // 最大边数
	public static int MAXH = 20;      // 倍增表最大层数(2^20 > 1e5)
	public static int n, m, q;        // 节点数、边数、查询数
	public static int[][] edge = new int[MAXM][3];
	
	// 并查集 - 用于维护连通性
	public static int[] father = new int[MAXK];

	// Kruskal重构树 - 邻接表存储
	public static int[] head = new int[MAXK];  // 邻接表头节点
	public static int[] next = new int[MAXK];  // 下一条边
	public static int[] to = new int[MAXK];    // 边的目标节点
	public static int cntg = 0;                // 边计数器
	public static int[] nodeKey = new int[MAXK]; // 节点权值（对应原图中的边编号）
	public static int cntu;                    // 重构树节点总数

	// 树上dfs信息
	public static int[] leafsiz = new int[MAXK];     // 每个节点子树中叶节点的数量
	public static int[][] stjump = new int[MAXK][MAXH]; // 倍增表

	/**
	 * 邻接表添加边函数
	 * 采用头插法构建邻接表
	 * @param u 边的起点
	 * @param v 边的终点
	 */
	public static void addEdge(int u, int v) {
		next[++cntg] = head[u];
		to[cntg] = v;
		head[u] = cntg;
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
	 * DFS函数 - 计算每个节点子树中叶节点的数量，构建倍增表
	 * 
	 * 功能说明：
	 * 1. 构建倍增表用于后续查询
	 * 2. 计算每个节点子树中叶节点的数量
	 * 
	 * @param u 当前节点
	 * @param fa 父节点
	 */
	public static void dfs(int u, int fa) {
		stjump[u][0] = fa;
		for (int p = 1; p < MAXH; p++) {
			stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
		}
		for (int e = head[u]; e > 0; e = next[e]) {
			dfs(to[e], u);
		}
		if (u <= n) {
			leafsiz[u] = 1;
		} else {
			leafsiz[u] = 0;
		}
		for (int e = head[u]; e > 0; e = next[e]) {
			leafsiz[u] += leafsiz[to[e]];
		}
	}

	/**
	 * 检查函数 - 检查使用编号不超过limit的边是否能满足要求
	 * 
	 * 实现思路：
	 * 1. 从x和y分别向上跳找到对应的祖先节点
	 * 2. 如果两个祖先节点相同，说明x和y在同一连通分量中，计算该连通分量的节点数
	 * 3. 如果两个祖先节点不同，说明x和y在不同连通分量中，计算两个连通分量的节点数之和
	 * 4. 判断节点数是否大于等于z
	 * 
	 * @param x 起始节点1
	 * @param y 起始节点2
	 * @param z 需要访问的节点数
	 * @param limit 边编号上限
	 * @return 是否能满足要求
	 */
	public static boolean check(int x, int y, int z, int limit) {
		// 从x向上跳找到对应的祖先节点
		for (int p = MAXH - 1; p >= 0; p--) {
			if (stjump[x][p] > 0 && nodeKey[stjump[x][p]] <= limit) {
				x = stjump[x][p];
			}
		}
		// 从y向上跳找到对应的祖先节点
		for (int p = MAXH - 1; p >= 0; p--) {
			if (stjump[y][p] > 0 && nodeKey[stjump[y][p]] <= limit) {
				y = stjump[y][p];
			}
		}
		// 判断两个祖先节点是否相同
		if (x == y) {
			// 在同一连通分量中
			return leafsiz[x] >= z;
		} else {
			// 在不同连通分量中
			return leafsiz[x] + leafsiz[y] >= z;
		}
	}

	/**
	 * 查询函数 - 二分答案找到满足要求的最小边编号
	 * 
	 * 实现思路：
	 * 1. 二分答案mid，范围为[1, m]
	 * 2. 使用check函数检查mid是否满足要求
	 * 3. 根据检查结果调整二分边界
	 * 
	 * @param x 起始节点1
	 * @param y 起始节点2
	 * @param z 需要访问的节点数
	 * @return 满足要求的最小边编号
	 */
	public static int query(int x, int y, int z) {
		int l = 1, r = m, mid, ans = 0;
		while (l <= r) {
			mid = (l + r) / 2;
			if (check(x, y, z, mid)) {
				ans = mid;
				r = mid - 1;
			} else {
				l = mid + 1;
			}
		}
		return ans;
	}

	public static void main(String[] args) {
		FastIO io = new FastIO(System.in, System.out);
		n = io.nextInt();
		m = io.nextInt();
		for (int i = 1; i <= m; i++) {
			edge[i][0] = io.nextInt();
			edge[i][1] = io.nextInt();
			edge[i][2] = i;  // 边的编号就是i
		}
		kruskalRebuild();
		dfs(cntu, 0);
		q = io.nextInt();
		for (int i = 1, x, y, z; i <= q; i++) {
			x = io.nextInt();
			y = io.nextInt();
			z = io.nextInt();
			io.writelnInt(query(x, y, z));
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
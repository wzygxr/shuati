package class164;

/**
 * youyou的军训 - Java实现
 * 
 * 题目来源：洛谷P9638
 * 题目链接：https://www.luogu.com.cn/problem/P9638
 * 
 * 题目描述：
 * 图里有n个点，m条无向边，每条边给定不同的边权，图里可能有若干个连通的部分
 * 一开始limit = 0，接下来有q条操作，每种操作的格式如下
 * 操作 1 x   : 所有修改操作生效，然后limit设置成x
 * 操作 2 x   : 从点x出发，只能走 边权 >= limit 的边，查询最多到达几个点
 * 操作 3 x y : 第x条边的边权修改为y，不是立刻生效，等到下次操作1发生时生效
 * 题目保证边权不管如何修改，所有边权都不相等，并且每条边的边权排名不发生变化
 * 
 * 算法核心思想：
 * 本题是Kruskal重构树的动态应用，结合了离线处理的思想。
 * 通过构建按边权降序的Kruskal重构树，可以快速查询从某个点出发，
 * 在边权限制下能到达的节点数量。
 * 
 * 解题思路：
 * 1. 构建按边权降序的Kruskal重构树
 * 2. 在重构树上进行DFS，计算每个节点子树中叶节点的数量
 * 3. 对于操作2，从查询节点向上跳，找到满足边权条件的最浅祖先节点
 * 4. 该祖先节点子树中的叶节点数量即为答案
 * 5. 对于操作3，将修改操作缓存，等到操作1时批量生效
 * 
 * 时间复杂度分析：
 * - 构建Kruskal重构树：O(m log m)
 * - DFS预处理：O(n)
 * - 每次查询：O(log n)
 * 总复杂度：O(m log m + q log n)
 * 
 * 空间复杂度分析：
 * - 存储边：O(m)
 * - 存储图和重构树：O(n)
 * - 倍增表：O(n log n)
 * 总空间复杂度：O(n log n + m)
 * 
 * 工程化考量：
 * 1. 异常处理：处理各种操作类型的边界情况
 * 2. 性能优化：使用快速IO和路径压缩并查集
 * 3. 内存管理：合理分配数组空间
 * 4. 离线处理：将修改操作缓存，批量处理提高效率
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class Code02_Training1 {

	public static int MAXK = 800001;  // 最大节点数（重构树节点数最多为2n-1）
	public static int MAXM = 400001;  // 最大边数
	public static int MAXH = 20;      // 倍增表最大层数(2^20 > 1e5)
	public static int n, m, q;        // 节点数、边数、操作数

	// 每条边的信息，节点u、节点v、边权w、边的编号i
	public static int[][] edge = new int[MAXM][4];
	// 边的编号对应重构树上的点的编号
	public static int[] edgeToTree = new int[MAXM];

	// 边权的修改操作先不生效，等到下次操作1发生时生效
	// 修改了哪些边
	public static int[] pendEdge = new int[MAXM];
	// 修改成了什么边权
	public static int[] pendVal = new int[MAXM];
	// 修改操作的个数
	public static int cntp = 0;

	// 并查集 - 用于维护连通性
	public static int[] father = new int[MAXK];
	public static int[] stack = new int[MAXK];

	// Kruskal重构树 - 邻接表存储
	public static int[] head = new int[MAXK];  // 邻接表头节点
	public static int[] next = new int[MAXK];  // 下一条边
	public static int[] to = new int[MAXK];    // 边的目标节点
	public static int cntg = 0;                // 边计数器
	public static int[] nodeKey = new int[MAXK]; // 节点权值（对应原图中的边权）
	public static int cntu;                    // 重构树节点总数

	// 树上dfs信息
	public static int[] leafsiz = new int[MAXK];     // 每个节点子树中叶节点的数量
	public static int[][] stjump = new int[MAXK][MAXH]; // 倍增表

	/**
	 * 并查集查找函数 - 带路径压缩优化（迭代版）
	 * 时间复杂度：近似O(1)
	 * @param i 要查找的节点
	 * @return 节点所在集合的根节点
	 */
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
	 * 构建Kruskal重构树（按边权降序）
	 * 
	 * 实现细节：
	 * 1. 初始化并查集
	 * 2. 按边权降序排序
	 * 3. 遍历排序后的边，使用并查集检查连通性
	 * 4. 当发现不在同一连通分量的边时，创建新节点并构建重构树
	 * 5. 记录每条边在重构树中对应的节点编号
	 */
	public static void kruskalRebuild() {
		// 初始化并查集
		for (int i = 1; i <= n; i++) {
			father[i] = i;
		}
		// 按边权降序排序
		Arrays.sort(edge, 1, m + 1, (a, b) -> b[2] - a[2]);
		cntu = n;
		for (int i = 1, fx, fy; i <= m; i++) {
			fx = find(edge[i][0]);
			fy = find(edge[i][1]);
			if (fx != fy) {
				// 合并两个连通分量
				father[fx] = father[fy] = ++cntu;
				father[cntu] = cntu;
				// 新节点的权值为当前边的边权
				nodeKey[cntu] = edge[i][2];
				// 建立重构树的父子关系
				addEdge(cntu, fx);
				addEdge(cntu, fy);
				// 记录边在重构树中对应的节点编号
				edgeToTree[edge[i][3]] = cntu;
			}
		}
	}

	// dfs1是递归函数，需要改成迭代版，不然会爆栈，C++实现不需要
	public static void dfs1(int u, int fa) {
		stjump[u][0] = fa;
		for (int p = 1; p < MAXH; p++) {
			stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
		}
		for (int e = head[u]; e > 0; e = next[e]) {
			dfs1(to[e], u);
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
	 * DFS迭代版本 - 计算每个节点子树中叶节点的数量
	 * 
	 * 功能说明：
	 * 1. 构建倍增表用于后续查询
	 * 2. 计算每个节点子树中叶节点的数量
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
				e = head[u];
			} else {
				e = next[e];
			}
			if (e != 0) {
				push(u, f, e);
				push(to[e], u, -1);
			} else {
				if (u <= n) {
					leafsiz[u] = 1;
				} else {
					leafsiz[u] = 0;
				}
				for (int ei = head[u]; ei > 0; ei = next[ei]) {
					leafsiz[u] += leafsiz[to[ei]];
				}
			}
		}
	}

	/**
	 * 查询函数 - 从节点u出发，在边权>=limit的限制下能到达的节点数
	 * 
	 * 实现思路：
	 * 1. 从节点u开始，向上跳找到满足条件的最浅祖先节点
	 * 2. 该祖先节点子树中的叶节点数量即为答案
	 * 
	 * @param u 起始节点
	 * @param limit 边权限制
	 * @return 能到达的节点数
	 */
	public static int query(int u, int limit) {
		for (int p = MAXH - 1; p >= 0; p--) {
			if (stjump[u][p] > 0 && nodeKey[stjump[u][p]] >= limit) {
				u = stjump[u][p];
			}
		}
		return leafsiz[u];
	}

	public static void main(String[] args) {
		FastIO io = new FastIO(System.in, System.out);
		n = io.nextInt();
		m = io.nextInt();
		q = io.nextInt();
		for (int i = 1; i <= m; i++) {
			edge[i][0] = io.nextInt();
			edge[i][1] = io.nextInt();
			edge[i][2] = io.nextInt();
			edge[i][3] = i;
		}
		kruskalRebuild();
		for (int i = 1; i <= cntu; i++) {
			if (i == father[i]) {
				dfs2(i, 0);
			}
		}
		int op, x, y, limit = 0;
		for (int i = 1; i <= q; i++) {
			op = io.nextInt();
			if (op == 1) {
				// 收集的修改操作生效
				for (int k = 1; k <= cntp; k++) {
					nodeKey[edgeToTree[pendEdge[k]]] = pendVal[k];
				}
				cntp = 0;
				limit = io.nextInt();
			} else if (op == 2) {
				x = io.nextInt();
				io.writelnInt(query(x, limit));
			} else {
				x = io.nextInt();
				y = io.nextInt();
				// 收集修改操作
				if (edgeToTree[x] != 0) {
					pendEdge[++cntp] = x;
					pendVal[cntp] = y;
				}
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
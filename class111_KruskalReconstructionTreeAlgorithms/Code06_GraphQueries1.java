package class164;

/**
 * 删边和查询 - Java实现
 * 
 * 题目来源：Codeforces 1416D Graph and Queries
 * 题目链接：https://codeforces.com/problemset/problem/1416/D
 * 洛谷链接：https://www.luogu.com.cn/problem/CF1416D
 * 
 * 题目描述：
 * 图里有n个点，m条无向边，初始时点权都不同，图里可能有若干个连通的部分
 * 一共有q条操作，每条操作是如下两种类型中的一种
 * 操作 1 x : 点x所在的连通区域中，假设点y拥有最大的点权
 *            打印y的点权，然后把y的点权修改为0
 * 操作 2 x : 删掉第x条边
 * 
 * 算法核心思想：
 * 本题是Kruskal重构树与线段树的结合应用，采用了离线处理的思想。
 * 通过将删除操作转化为添加操作（时光倒流），构建Kruskal重构树，
 * 然后结合线段树维护每个连通分量中的最大点权节点。
 * 
 * 解题思路：
 * 1. 离线处理：将删除操作转化为添加操作，从后往前处理
 * 2. 构建按边权升序的Kruskal重构树，节点权值为边的添加时间
 * 3. 在重构树上进行DFS，记录每个节点的DFS序号和子树信息
 * 4. 构建线段树维护DFS序号范围内的最大点权节点
 * 5. 对于操作1，找到节点x所在连通分量的祖先节点，查询其子树内的最大点权节点
 * 6. 对于操作2，减少边权上限，相当于删除边
 * 
 * 时间复杂度分析：
 * - 离线处理：O(q)
 * - 构建Kruskal重构树：O(m log m)
 * - DFS预处理：O(n)
 * - 构建线段树：O(n)
 * - 每次查询：O(log n)
 * 总复杂度：O(m log m + (n + q) log n)
 * 
 * 空间复杂度分析：
 * - 存储边：O(m)
 * - 存储图和重构树：O(n)
 * - 倍增表：O(n log n)
 * - 线段树：O(n)
 * 总空间复杂度：O(n log n + m)
 * 
 * 工程化考量：
 * 1. 异常处理：处理各种操作情况的边界条件
 * 2. 性能优化：使用快速IO和路径压缩并查集
 * 3. 内存管理：合理分配数组空间
 * 4. 离线处理：将删除操作转化为添加操作，简化问题
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class Code06_GraphQueries1 {

	public static int MAXN = 200001;  // 最大节点数
	public static int MAXK = 400001;  // 最大重构树节点数
	public static int MAXM = 300001;  // 最大边数
	public static int MAXQ = 500001;  // 最大操作数
	public static int MAXH = 20;      // 倍增表最大层数(2^20 > 1e5)
	public static int n, m, q;        // 节点数、边数、操作数

	// 节点值的数组，需要记录，线段树也要使用
	public static int[] node = new int[MAXN];
	// 所有边的数组，逆序处理删除操作，设置每条边的权值
	public static int[][] edge = new int[MAXM][3];
	// 记录所有操作
	public static int[][] ques = new int[MAXQ][2];

	// 并查集 - 用于构建Kruskal重构树
	public static int[] father = new int[MAXK];

	// Kruskal重构树 - 邻接表存储
	public static int[] head = new int[MAXK];  // 重构树邻接表头节点
	public static int[] next = new int[MAXK];  // 重构树下一条边
	public static int[] to = new int[MAXK];    // 重构树边的目标节点
	public static int cntg = 0;                // 重构树边计数器
	public static int[] nodeKey = new int[MAXK]; // 重构树节点权值（对应边的添加时间）
	public static int cntu;                    // 重构树节点总数

	// 树上信息
	public static int[][] stjump = new int[MAXK][MAXH]; // 倍增表
	public static int[] leafsiz = new int[MAXK];        // 每个节点子树中叶节点的数量
	public static int[] leafDfnMin = new int[MAXK];     // 每个节点子树中叶节点的最小DFS序号
	public static int[] leafseg = new int[MAXK];        // leafseg[i] = j，表示DFS序号为i的叶节点对应原始节点编号j
	public static int cntd = 0;                         // DFS序号计数器

	// 线段树 - 维护DFS序号范围内的最大点权节点
	public static int[] maxValueDfn = new int[MAXN << 2]; // 线段树节点，维护范围内拥有最大点权的DFS序号

	/**
	 * 预处理函数 - 离线处理删除操作
	 * 
	 * 实现思路：
	 * 1. 标记所有删除操作涉及的边
	 * 2. 为未被删除的边分配权值
	 * 3. 从后往前处理，为删除操作涉及的边分配权值
	 */
	public static void prepare() {
		// 标记所有删除操作涉及的边
		for (int i = 1; i <= q; i++) {
			if (ques[i][0] == 2) {
				edge[ques[i][1]][2] = -1;
			}
		}
		// 为未被删除的边分配权值
		int weight = 0;
		for (int i = 1; i <= m; i++) {
			if (edge[i][2] != -1) {
				edge[i][2] = ++weight;
			}
		}
		// 从后往前处理，为删除操作涉及的边分配权值
		for (int i = q; i >= 1; i--) {
			if (ques[i][0] == 2) {
				edge[ques[i][1]][2] = ++weight;
			}
		}
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
	 * 构建Kruskal重构树（按边权升序）
	 * 
	 * 实现细节：
	 * 1. 初始化并查集
	 * 2. 按边权升序排序
	 * 3. 遍历排序后的边，使用并查集检查连通性
	 * 4. 当发现不在同一连通分量的边时，创建新节点并构建重构树
	 */
	public static void kruskalRebuild() {
		// 初始化并查集
		for (int i = 1; i <= n; i++) {
			father[i] = i;
		}
		// 按边权升序排序
		Arrays.sort(edge, 1, m + 1, (a, b) -> a[2] - b[2]);
		cntu = n;
		for (int i = 1, fx, fy; i <= m; i++) {
			fx = find(edge[i][0]);
			fy = find(edge[i][1]);
			if (fx != fy) {
				// 合并两个连通分量
				father[fx] = father[fy] = ++cntu;
				father[cntu] = cntu;
				// 新节点的权值为当前边的权值
				nodeKey[cntu] = edge[i][2];
				// 建立重构树的父子关系
				addEdge(cntu, fx);
				addEdge(cntu, fy);
			}
		}
	}

	/**
	 * DFS函数 - 记录每个节点的子树信息
	 * 
	 * 功能说明：
	 * 1. 构建倍增表用于后续查询
	 * 2. 记录每个节点子树中叶节点的数量和最小DFS序号
	 * 3. 建立DFS序号与原始节点编号的映射关系
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
			// 叶节点
			leafsiz[u] = 1;
			leafDfnMin[u] = ++cntd;
			leafseg[cntd] = u;
		} else {
			// 非叶节点
			leafsiz[u] = 0;
			leafDfnMin[u] = n + 1;
		}
		for (int e = head[u]; e > 0; e = next[e]) {
			leafsiz[u] += leafsiz[to[e]];
			leafDfnMin[u] = Math.min(leafDfnMin[u], leafDfnMin[to[e]]);
		}
	}

	/**
	 * 获取祖先节点函数 - 找到在边权限制下能到达的最浅祖先节点
	 * 
	 * 实现思路：
	 * 从节点u开始，向上跳找到满足边权条件的最浅祖先节点
	 * 
	 * @param u 起始节点
	 * @param limit 边权限制
	 * @return 满足条件的最浅祖先节点
	 */
	public static int getAncestor(int u, int limit) {
		for (int p = MAXH - 1; p >= 0; p--) {
			if (stjump[u][p] > 0 && nodeKey[stjump[u][p]] <= limit) {
				u = stjump[u][p];
			}
		}
		return u;
	}

	/**
	 * 线段树上传函数 - 更新父节点信息
	 * @param i 线段树节点编号
	 */
	public static void up(int i) {
		int l = i << 1;
		int r = i << 1 | 1;
		if (node[leafseg[maxValueDfn[l]]] > node[leafseg[maxValueDfn[r]]]) {
			maxValueDfn[i] = maxValueDfn[l];
		} else {
			maxValueDfn[i] = maxValueDfn[r];
		}
	}

	/**
	 * 构建线段树函数 - 初始化线段树
	 * @param l 区间左端点
	 * @param r 区间右端点
	 * @param i 线段树节点编号
	 */
	public static void build(int l, int r, int i) {
		if (l == r) {
			maxValueDfn[i] = l;
		} else {
			int mid = (l + r) / 2;
			build(l, mid, i << 1);
			build(mid + 1, r, i << 1 | 1);
			up(i);
		}
	}

	/**
	 * 线段树更新函数 - 更新指定DFS序号节点的点权
	 * 
	 * @param jobi DFS序号
	 * @param jobv 新的点权值
	 * @param l 区间左端点
	 * @param r 区间右端点
	 * @param i 线段树节点编号
	 */
	// dfn序号为jobi，点权更新成jobv
	public static void update(int jobi, int jobv, int l, int r, int i) {
		if (l == r) {
			node[leafseg[jobi]] = jobv;
		} else {
			int mid = (l + r) / 2;
			if (jobi <= mid) {
				update(jobi, jobv, l, mid, i << 1);
			} else {
				update(jobi, jobv, mid + 1, r, i << 1 | 1);
			}
			up(i);
		}
	}

	/**
	 * 线段树查询函数 - 查询指定DFS序号范围内的最大点权节点
	 * 
	 * @param jobl DFS序号范围左端点
	 * @param jobr DFS序号范围右端点
	 * @param l 区间左端点
	 * @param r 区间右端点
	 * @param i 线段树节点编号
	 * @return 最大点权节点的DFS序号
	 */
	// dfn范围[jobl..jobr]，哪个节点拥有最大点权，返回该节点的dfn序号
	public static int query(int jobl, int jobr, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			return maxValueDfn[i];
		} else {
			int mid = (l + r) / 2;
			int ldfn = 0, rdfn = 0;
			if (jobl <= mid) {
				ldfn = query(jobl, jobr, l, mid, i << 1);
			}
			if (jobr > mid) {
				rdfn = query(jobl, jobr, mid + 1, r, i << 1 | 1);
			}
			if (node[leafseg[ldfn]] > node[leafseg[rdfn]]) {
				return ldfn;
			} else {
				return rdfn;
			}
		}
	}

	/**
	 * 查询并更新函数 - 查询节点x所在连通分量的最大点权节点并将其点权设为0
	 * 
	 * 实现思路：
	 * 1. 找到节点x在边权限制下能到达的最浅祖先节点
	 * 2. 查询该祖先节点子树内的最大点权节点
	 * 3. 将该节点的点权更新为0
	 * 
	 * @param x 起始节点
	 * @param limit 边权限制
	 * @return 最大点权值
	 */
	public static int queryAndUpdate(int x, int limit) {
		int anc = getAncestor(x, limit);
		int dfn = query(leafDfnMin[anc], leafDfnMin[anc] + leafsiz[anc] - 1, 1, n, 1);
		int ans = node[leafseg[dfn]];
		update(dfn, 0, 1, n, 1);
		return ans;
	}

	public static void main(String[] args) {
		FastIO io = new FastIO(System.in, System.out);
		n = io.nextInt();
		m = io.nextInt();
		q = io.nextInt();
		for (int i = 1; i <= n; i++) {
			node[i] = io.nextInt();
		}
		for (int i = 1; i <= m; i++) {
			edge[i][0] = io.nextInt();
			edge[i][1] = io.nextInt();
			edge[i][2] = 0;
		}
		for (int i = 1; i <= q; i++) {
			ques[i][0] = io.nextInt();
			ques[i][1] = io.nextInt();
		}
		prepare();
		kruskalRebuild();
		for (int i = 1; i <= cntu; i++) {
			if (i == father[i]) {
				dfs(i, 0);
			}
		}
		build(1, n, 1);
		int limit = m;
		for (int i = 1; i <= q; i++) {
			if (ques[i][0] == 1) {
				io.writelnInt(queryAndUpdate(ques[i][1], limit));
			} else {
				limit--;
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
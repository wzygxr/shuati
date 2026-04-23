package class191;

// 网络，java版
// 给定一张无向图，一共n个点、m条边，保证所有点连通
// 一共q条操作，格式 x y : 点x和点y之间新增一条边，打印此时割边的数量
// 1 <= n <= 10^5
// 1 <= m <= 2 * 10^5
// 测试链接 : https://acm.hdu.edu.cn/showproblem.php?pid=2460
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

// 导入IO相关类
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code06_Network1 {

	// 定义常量：最大节点数和最大边数
	public static int MAXN = 100001;
	public static int MAXM = 200001;
	// 测试用例编号t，节点数n，边数m，操作数q
	public static int t, n, m, q;
	// 存储每条边的两个端点
	public static int[] a = new int[MAXM];
	public static int[] b = new int[MAXM];

	// 邻接表存储（原始图）
	public static int[] head = new int[MAXN];
	public static int[] nxt = new int[MAXM << 1];
	public static int[] to = new int[MAXM << 1];
	public static int cntg;

	// Tarjan算法核心数组
	public static int[] dfn = new int[MAXN]; // 发现时间
	public static int[] low = new int[MAXN]; // 追溯到的最小dfn
	public static int cntd; // 时间戳

	// 栈：用于存储DFS遍历过程中的节点
	public static int[] sta = new int[MAXN];
	public static int top; // 栈顶指针

	// belong[i]：节点i所属的边双连通分量编号
	public static int[] belong = new int[MAXN];
	// 边双连通分量的数量
	public static int ebccCnt;

	// 缩点后的树相关数组
	public static int[] up = new int[MAXN]; // 父节点
	public static int[] dep = new int[MAXN]; // 深度
	public static int[] fa = new int[MAXN]; // 并查集父节点

	/**
	 * 初始化/准备函数
	 * 在每次测试用例开始时调用，重置所有变量
	 */
	public static void prepare() {
		cntg = 1;
		cntd = top = ebccCnt = 0;
		// 重置所有节点的邻接表头和dfn、low数组
		for (int i = 1; i <= n; i++) {
			head[i] = dfn[i] = low[i] = 0;
		}
	}

	/**
	 * 添加无向边到邻接表
	 */
	public static void addEdge(int u, int v) {
		nxt[++cntg] = head[u];
		to[cntg] = v;
		head[u] = cntg;
	}

	/**
	 * Tarjan算法 - 求边双连通分量
	 * 
	 * 边双连通分量（EBCC）：
	 * 如果一个连通子图中任意两点都存在两条及以上不重合的路径，则为EBCC
	 * 
	 * @param u 当前访问的节点
	 * @param preEdge 从父节点来的边编号
	 */
	public static void tarjan(int u, int preEdge) {
		// 初始化dfn和low
		dfn[u] = low[u] = ++cntd;
		// 将当前节点压入栈
		sta[++top] = u;
		// 遍历所有邻接边
		for (int e = head[u]; e > 0; e = nxt[e]) {
			// 跳过来自父节点的边
			if ((e ^ 1) == preEdge) {
				continue;
			}
			int v = to[e];
			if (dfn[v] == 0) {
				// 树边：递归处理
				tarjan(v, e);
				// 回溯更新low值
				low[u] = Math.min(low[u], low[v]);
			} else {
				// 回边或弃边
				low[u] = Math.min(low[u], dfn[v]);
			}
		}
		// 判断是否为边双连通分量的根
		if (dfn[u] == low[u]) {
			// 发现一个新的EBCC
			ebccCnt++;
			int pop;
			// 从栈中弹出节点直到u
			do {
				pop = sta[top--];
				// 标记节点所属的EBCC
				belong[pop] = ebccCnt;
			} while (pop != u);
		}
	}

	/**
	 * 缩点函数
	 * 将边双连通分量缩成单个点，构建缩点树
	 */
	public static void condense() {
		// 重置边的计数（用于缩点图）
		cntg = 0;
		// 初始化缩点图的邻接表
		for (int i = 1; i <= ebccCnt; i++) {
			head[i] = 0;
		}
		// 遍历原图的所有边
		for (int i = 1; i <= m; i++) {
			// 获取边的两端点所属的EBCC
			int ebcc1 = belong[a[i]];
			int ebcc2 = belong[b[i]];
			// 如果两端点不在同一个EBCC中，则在缩点树中添加边
			if (ebcc1 != ebcc2) {
				addEdge(ebcc1, ebcc2);
				addEdge(ebcc2, ebcc1);
			}
		}
	}

	/**
	 * DFS遍历缩点树
	 * 预处理每个节点的父节点和深度
	 * @param u 当前节点
	 * @param f 父节点
	 */
	public static void dfs(int u, int f) {
		// 设置深度为父节点深度+1
		dep[u] = dep[f] + 1;
		// 设置父节点
		up[u] = f;
		// 初始化并查集的父节点为自己
		fa[u] = u;
		// 遍历所有邻接边
		for (int e = head[u]; e > 0; e = nxt[e]) {
			int v = to[e];
			// 避免回到父节点
			if (v != f) {
				dfs(v, u);
			}
		}
	}

	/**
	 * 并查集-find函数
	 * 路径压缩
	 * @param i 要查找的节点
	 * @return 所在集合的根节点
	 */
	public static int find(int i) {
		if (i != fa[i]) {
			fa[i] = find(fa[i]);
		}
		return fa[i];
	}

	/**
	 * 并查集-union函数
	 * 按深度合并两个集合
	 * @param x 节点x
	 * @param y 节点y
	 */
	public static void union(int x, int y) {
		x = find(x);
		y = find(y);
		if (x != y) {
			// 将深度较小的合并到深度较大的
			if (dep[x] < dep[y]) {
				fa[y] = x;
			} else {
				fa[x] = y;
			}
		}
	}

	/**
	 * 连接两个节点所在的EBCC
	 * 
	 * 核心思想：
	 * 1. 每次添加新边(x,y)，会使得x和y所在EBCC之间的路径上的所有EBCC合并
	 * 2. 使用并查集合并，同时沿路径向上合并
	 * 3. 每合并一次，割边数量-1
	 * 
	 * @param x 节点x
	 * @param y 节点y
	 */
	public static void link(int x, int y) {
		// 获取x和y所在EBCC的编号
		x = find(belong[x]);
		y = find(belong[y]);
		// 当x和y不在同一个集合时，向上合并
		while (x != y) {
			if (dep[x] >= dep[y]) {
				// 深度较大的节点向父节点合并
				union(x, up[x]);
				x = find(x);
			} else {
				union(y, up[y]);
				y = find(y);
			}
			// 每次合并，割边数量减少1
			ebccCnt--;
		}
	}

	/**
	 * 主函数：程序入口
	 */
	public static void main(String[] args) throws Exception {
		// 快速读写初始化
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		// 测试用例编号从0开始
		t = 0;
		// 读取第一个测试用例的n和m
		n = in.nextInt();
		m = in.nextInt();
		// 处理多组测试用例（n=0且m=0时结束）
		while (n != 0 || m != 0) {
			// 初始化
			prepare();
			// 读取所有边，构建邻接表
			for (int i = 1; i <= m; i++) {
				a[i] = in.nextInt();
				b[i] = in.nextInt();
				addEdge(a[i], b[i]);
				addEdge(b[i], a[i]);
			}
			// 求边双连通分量
			tarjan(1, 0);
			// 缩点，构建缩点树
			condense();
			// DFS预处理父节点和深度
			dfs(1, 0);
			// 输出Case编号
			out.println("Case " + (++t) + ":");
			// 读取操作数q
			q = in.nextInt();
			// 处理每个添加边的操作
			for (int i = 1, x, y; i <= q; i++) {
				x = in.nextInt();
				y = in.nextInt();
				// 添加新边，合并路径上的EBCC
				link(x, y);
				// 输出当前割边数量（EBCC数量-1）
				out.println(ebccCnt - 1);
			}
			// 每个测试用例后输出空行
			out.println();
			// 读取下一个测试用例
			n = in.nextInt();
			m = in.nextInt();
		}
		// 刷新并关闭输出流
		out.flush();
		out.close();
	}

	/**
	 * 快速读写工具类
	 * 使用自定义缓冲区实现高效IO
	 */
	// 读写工具类
	static class FastReader {
		// 缓冲区：64KB
		private final byte[] buffer = new byte[1 << 16];
		private int ptr = 0, len = 0;
		private final InputStream in;

		FastReader(InputStream in) {
			this.in = in;
		}

		/**
		 * 从输入流读取一个字节
		 */
		private int readByte() throws IOException {
			if (ptr >= len) {
				len = in.read(buffer);
				ptr = 0;
				if (len <= 0)
					return -1;
			}
			return buffer[ptr++];
		}

		/**
		 * 读取下一个整数
		 */
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

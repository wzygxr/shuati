package class191;

// 关键网络线路，java版
// 给定一张无向图，一共n个点、m条边，保证所有点连通
// 给定k个提供A服务的节点，给定l个提供B服务的节点，保证k和l都是正数
// 一个节点可能不提供服务，也可能提供A服务或者B服务或者两种都有
// 每个节点可以通过边，获得任何节点提供的服务，但是必须同时获得两种服务
// 如果断开某一条边，使得某些节点无法同时获得两种服务，这样的边叫关键边
// 打印关键边的数量，打印每条关键边的两个端点
// 1 <= n <= 10^5
// 1 <= m <= 10^6
// 测试链接 : https://www.luogu.com.cn/problem/P7687
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

// 导入Java IO相关类，用于快速读写
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code03_CriticalNetworkLines1 {

	// 定义常量：最大节点数和最大边数
	public static int MAXN = 100001;
	public static int MAXM = 1000001;
	// 图的节点数n、边数m、提供A服务的节点数k、提供B服务的节点数l
	public static int n, m, k, l;
	// acnt[x]：节点x所在子树中提供A服务的节点数量
	public static int[] acnt = new int[MAXN];
	// bcnt[x]：节点x所在子树中提供B服务的节点数量
	public static int[] bcnt = new int[MAXN];

	// 邻接表存储
	public static int[] head = new int[MAXN];
	public static int[] nxt = new int[MAXM << 1];
	public static int[] to = new int[MAXM << 1];
	public static int cntg;

	// Tarjan算法核心数组
	public static int[] dfn = new int[MAXN];
	public static int[] low = new int[MAXN];
	public static int cntd;

	// 存储关键边的答案数组
	public static int[] ans1 = new int[MAXN];
	public static int[] ans2 = new int[MAXN];
	// 关键边的数量
	public static int cnta;

	// 迭代版需要的栈，讲解118讲了递归改迭代的技巧
	// 四个并行数组模拟函数调用栈
	public static int[] stau = new int[MAXN]; // 节点编号
	public static int[] stap = new int[MAXN]; // 父边编号
	public static int[] stas = new int[MAXN]; // 状态标记
	public static int[] stae = new int[MAXN]; // 当前边编号
	// 当前处理的节点、来自父节点的边、状态、当前边
	public static int u, preEdge, status, e;
	// 栈的大小
	public static int stasiz;

	/**
	 * 将状态压入模拟栈
	 */
	public static void push(int u, int preEdge, int status, int e) {
		stau[stasiz] = u;
		stap[stasiz] = preEdge;
		stas[stasiz] = status;
		stae[stasiz] = e;
		stasiz++;
	}

	/**
	 * 从模拟栈中弹出状态
	 */
	public static void pop() {
		stasiz--;
		u = stau[stasiz];
		preEdge = stap[stasiz];
		status = stas[stasiz];
		e = stae[stasiz];
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
	 * 递归版Tarjan算法 - 求关键边
	 * 思路：结合割边判断和服务节点计数
	 * 如果一条边是割边，且满足特定条件（子树中服务节点数为0或全部），则为关键边
	 */
	// 递归版
	public static void tarjan1(int u, int preEdge) {
		// 初始化dfn和low
		dfn[u] = low[u] = ++cntd;
		// 遍历所有邻接边
		for (int e = head[u]; e > 0; e = nxt[e]) {
			// 跳过来自父节点的边
			if ((e ^ 1) == preEdge) {
				continue;
			}
			int v = to[e];
			if (dfn[v] == 0) {
				// 树边：递归处理子节点
				tarjan1(v, e);
				// 累加子树的A服务节点数到当前节点
				low[u] = Math.min(low[u], low[v]);
				// 判断割边条件
				if (low[v] > dfn[u]) {
					// 边(u,v)是割边，检查子树中的服务节点情况
					// 如果子树中A服务节点数为0或全部，或B服务节点数为0或全部
					// 则断开这条边会导致某些节点无法同时获得两种服务
					if (acnt[v] == 0 || acnt[v] == k || bcnt[v] == 0 || bcnt[v] == l) {
						cnta++; // 关键边数量+1
						ans1[cnta] = v; // 记录关键边的端点
						ans2[cnta] = u;
					}
				}
				// 回溯累加子树的A/B服务节点数到当前节点
				acnt[u] += acnt[v];
				bcnt[u] += bcnt[v];
			} else {
				// 回边或弃边
				low[u] = Math.min(low[u], dfn[v]);
			}
		}
	}

	/**
	 * 迭代版Tarjan算法 - 求关键边
	 * 手动模拟递归调用栈，避免栈溢出
	 */
	// 迭代版
	public static void tarjan2(int node, int pree) {
		// 初始化栈
		stasiz = 0;
		// 压入初始状态
		push(node, pree, -1, -1);
		int v;
		while (stasiz > 0) {
			pop();
			if (status == -1) {
				// 刚进入节点
				dfn[u] = low[u] = ++cntd;
				e = head[u];
			} else {
				// 处理子节点回溯
				v = to[e];
				if (status == 0) {
					// 树边回溯
					low[u] = Math.min(low[u], low[v]);
					if (low[v] > dfn[u]) {
						// 判断是否为关键边
						if (acnt[v] == 0 || acnt[v] == k || bcnt[v] == 0 || bcnt[v] == l) {
							cnta++;
							ans1[cnta] = v;
							ans2[cnta] = u;
						}
					}
					// 累加服务节点数
					acnt[u] += acnt[v];
					bcnt[u] += bcnt[v];
				} else {
					// 回边或弃边
					low[u] = Math.min(low[u], dfn[v]);
				}
				e = nxt[e];
			}
			// 跳过反向边
			if ((e ^ 1) == preEdge) {
				e = nxt[e];
			}
			if (e != 0) {
				v = to[e];
				if (dfn[v] == 0) {
					// 压入状态：当前边处理完等回溯 + 进入子节点
					push(u, preEdge, 0, e);
					push(v, e, -1, -1);
				} else {
					// 回边
					push(u, preEdge, 1, e);
				}
			}
		}
	}

	/**
	 * 主函数：程序入口
	 */
	public static void main(String[] args) throws Exception {
		// 快速读写初始化
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		// 初始化边的编号
		cntg = 1;
		// 读取n, m, k, l
		n = in.nextInt();
		m = in.nextInt();
		k = in.nextInt();
		l = in.nextInt();
		// 读取提供A服务的节点，标记acnt为1表示该节点提供A服务
		for (int i = 1, x; i <= k; i++) {
			x = in.nextInt();
			acnt[x] = 1;
		}
		// 读取提供B服务的节点，标记bcnt为1表示该节点提供B服务
		for (int i = 1, x; i <= l; i++) {
			x = in.nextInt();
			bcnt[x] = 1;
		}
		// 读取所有边，构建邻接表
		for (int i = 1, u, v; i <= m; i++) {
			u = in.nextInt();
			v = in.nextInt();
			addEdge(u, v);
			addEdge(v, u);
		}
		// tarjan1(1, 0);
		tarjan2(1, 0); // 从节点1开始（保证连通）
		// 输出关键边数量
		out.println(cnta);
		// 输出每条关键边
		for (int i = 1; i <= cnta; i++) {
			out.println(ans1[i] + " " + ans2[i]);
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

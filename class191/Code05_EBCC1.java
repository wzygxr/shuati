package class191;

// 边双连通分量模版题，java版
// 给定一张无向图，一共n个点、m条边
// 图中可能存在多个连通区，对每个连通区求边双连通分量
// 先打印边双连通分量的总数量，然后对每个边双连通分量
// 打印节点个数，然后任意顺序打印该边双连通分量的节点
// 请保证原图即使有重边和自环，答案依然正确
// 1 <= n <= 5 * 10^5
// 1 <= m <= 2 * 10^6
// 测试链接 : https://www.luogu.com.cn/problem/P8436
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

// 导入IO相关类
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code05_EBCC1 {

	// 定义常量：最大节点数和最大边数
	public static int MAXN = 500001;
	public static int MAXM = 2000001;
	// 节点数n和边数m
	public static int n, m;

	// 邻接表存储
	public static int[] head = new int[MAXN];
	public static int[] nxt = new int[MAXM << 1];
	public static int[] to = new int[MAXM << 1];
	public static int cntg;

	// Tarjan算法核心数组
	public static int[] dfn = new int[MAXN]; // 发现时间（dfs序）
	public static int[] low = new int[MAXN]; // 追溯到的最小dfn
	public static int cntd; // 时间戳

	// 栈：用于存储DFS遍历过程中的节点
	public static int[] sta = new int[MAXN];
	public static int top; // 栈顶指针

	// 边双连通分量（EBCC）相关数组
	public static int[] ebccSiz = new int[MAXN]; // 每个EBCC的节点数
	public static int[] ebccArr = new int[MAXN]; // 存储所有EBCC的节点（扁平化存储）
	public static int[] ebccl = new int[MAXN]; // 每个EBCC在ebccArr中的左边界
	public static int[] ebccr = new int[MAXN]; // 每个EBCC在ebccArr中的右边界
	public static int idx; // ebccArr的当前索引
	public static int ebccCnt; // 边双连通分量的数量

	// 迭代版需要的栈，讲解118讲了递归改迭代的技巧
	// 模拟函数调用栈：保存(u, preEdge, status, e)四个状态
	public static int[][] stack = new int[MAXN][4];
	// 当前处理的状态变量
	public static int u, preEdge, status, e;
	public static int stacksize; // 模拟栈的大小

	/**
	 * 将状态压入模拟栈
	 */
	public static void push(int u, int preEdge, int status, int e) {
		stack[stacksize][0] = u;
		stack[stacksize][1] = preEdge;
		stack[stacksize][2] = status;
		stack[stacksize][3] = e;
		stacksize++;
	}

	/**
	 * 从模拟栈中弹出状态
	 */
	public static void pop() {
		stacksize--;
		u = stack[stacksize][0];
		preEdge = stack[stacksize][1];
		status = stack[stacksize][2];
		e = stack[stacksize][3];
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
	 * 递归版Tarjan算法 - 求边双连通分量
	 * 
	 * 边双连通分量（Edge-Biconnected Component, EBCC）：
	 * 如果一个连通子图中任意两点都存在两条及以上不重合的路径，则为EBCC
	 * 
	 * 求法：Tarjan算法中，当dfn[u] == low[u]时，
	 * 从栈中弹出的节点构成一个边双连通分量
	 */
	// 递归版
	public static void tarjan1(int u, int preEdge) {
		// 初始化dfn和low
		dfn[u] = low[u] = ++cntd;
		// 将当前节点压入栈中
		sta[++top] = u;
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
				// 回溯更新low值
				low[u] = Math.min(low[u], low[v]);
			} else {
				// 回边或弃边
				low[u] = Math.min(low[u], dfn[v]);
			}
		}
		// 判断是否为边双连通分量的根
		// 当dfn[u] == low[u]时，表示u是某个EBCC的根
		if (dfn[u] == low[u]) {
			// 发现一个新的边双连通分量
			ebccCnt++;
			// 初始化该EBCC的大小为0
			ebccSiz[ebccCnt] = 0;
			// 记录该EBCC在ebccArr中的起始位置
			ebccl[ebccCnt] = idx + 1;
			int pop;
			// 从栈中弹出节点，直到弹出u本身
			do {
				pop = sta[top--];
				// 该EBCC的节点数+1
				ebccSiz[ebccCnt]++;
				// 将节点加入ebccArr数组
				ebccArr[++idx] = pop;
			} while (pop != u);
			// 记录该EBCC在ebccArr中的结束位置
			ebccr[ebccCnt] = idx;
		}
	}

	/**
	 * 迭代版Tarjan算法 - 求边双连通分量
	 * 手动模拟递归调用栈
	 */
	// 迭代版
	public static void tarjan2(int node, int pree) {
		// 初始化模拟栈
		stacksize = 0;
		// 压入初始状态
		push(node, pree, -1, -1);
		int v;
		while (stacksize > 0) {
			pop();
			if (status == -1) {
				// 刚进入节点u
				dfn[u] = low[u] = ++cntd;
				// 将节点压入栈
				sta[++top] = u;
				// 从第一条边开始处理
				e = head[u];
			} else {
				// 处理子节点回溯
				v = to[e];
				if (status == 0) {
					// 树边回溯：更新low值
					low[u] = Math.min(low[u], low[v]);
				} else {
					// 回边或弃边
					low[u] = Math.min(low[u], dfn[v]);
				}
				// 处理下一条边
				e = nxt[e];
			}
			// 跳过反向边
			if ((e ^ 1) == preEdge) {
				e = nxt[e];
			}
			if (e != 0) {
				v = to[e];
				if (dfn[v] == 0) {
					// 树边：压入状态
					push(u, preEdge, 0, e);
					push(v, e, -1, -1);
				} else {
					// 回边
					push(u, preEdge, 1, e);
				}
			} else {
				// 所有边处理完毕，判断是否形成EBCC
				if (dfn[u] == low[u]) {
					// 发现新的边双连通分量
					ebccCnt++;
					ebccSiz[ebccCnt] = 0;
					ebccl[ebccCnt] = idx + 1;
					int pop;
					do {
						pop = sta[top--];
						ebccSiz[ebccCnt]++;
						ebccArr[++idx] = pop;
					} while (pop != u);
					ebccr[ebccCnt] = idx;
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
		// 边的编号从1开始
		cntg = 1;
		// 读取节点数和边数
		n = in.nextInt();
		m = in.nextInt();
		// 读取所有边，构建无向图邻接表
		for (int i = 1, u, v; i <= m; i++) {
			u = in.nextInt();
			v = in.nextInt();
			addEdge(u, v);
			addEdge(v, u);
		}
		// 遍历所有节点，处理非连通图
		for (int i = 1; i <= n; i++) {
			if (dfn[i] == 0) {
				// tarjan1(i, 0);
				tarjan2(i, 0); // 使用迭代版
			}
		}
		// 输出边双连通分量的数量
		out.println(ebccCnt);
		// 对每个EBCC，输出节点数和节点列表
		for (int i = 1; i <= ebccCnt; i++) {
			// 输出该EBCC的节点数
			out.println(ebccSiz[i]);
			// 输出该EBCC的所有节点
			for (int j = ebccl[i]; j <= ebccr[i]; j++) {
				out.print(ebccArr[j] + " ");
			}
			// 每个EBCC输出后换行
			out.println();
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

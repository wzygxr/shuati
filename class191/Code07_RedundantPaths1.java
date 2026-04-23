package class191;

// 冗余路径，java版
// 给定一张无向图，一共n个点、m条边，保证所有点连通
// 打印至少添加几条边可以让整张图变成一个边双连通分量
// 1 <= n <= 5000
// 1 <= m <= 10000
// 测试链接 : https://www.luogu.com.cn/problem/P2860
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

// 导入IO相关类
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code07_RedundantPaths1 {

	// 定义常量：最大节点数和最大边数
	public static int MAXN = 5001;
	public static int MAXM = 10001;
	// 节点数n和边数m
	public static int n, m;
	// 存储每条边的两个端点
	public static int[] a = new int[MAXM];
	public static int[] b = new int[MAXM];

	// 邻接表存储
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

	// degree[i]：第i个EBCC的度数（连接其他EBCC的边数）
	public static int[] degree = new int[MAXN];

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
	 * 计算缩点树中度为1的节点（叶子）数量
	 * 
	 * 思路：
	 * 1. 将每个EBCC视为一个节点，构成缩点树（树结构）
	 * 2. 统计每个EBCC的度数（与其他EBCC的连接数）
	 * 3. 度为1的节点就是叶子节点
	 * 4. 答案为 (叶子数 + 1) / 2
	 *    （每添加一条边可以将两个叶子配对，最多减少两个叶子）
	 * 
	 * @return 叶子节点的数量
	 */
	public static int getLeaf() {
		// 遍历所有边，统计每个EBCC的度数
		for (int i = 1; i <= m; i++) {
			// 获取边的两端点所属的EBCC编号
			int ebcc1 = belong[a[i]];
			int ebcc2 = belong[b[i]];
			// 如果两端点不在同一个EBCC中，则这两个EBCC之间有一条边
			if (ebcc1 != ebcc2) {
				degree[ebcc1]++; // EBCC1的度数+1
				degree[ebcc2]++; // EBCC2的度数+1
			}
		}
		// 统计叶子节点（度为1的EBCC）的数量
		int ans = 0;
		for (int i = 1; i <= ebccCnt; i++) {
			if (degree[i] == 1) {
				ans++;
			}
		}
		return ans;
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
		for (int i = 1; i <= m; i++) {
			a[i] = in.nextInt();
			b[i] = in.nextInt();
			addEdge(a[i], b[i]);
			addEdge(b[i], a[i]);
		}
		// 执行Tarjan算法，求边双连通分量
		tarjan(1, 0);
		// 计算叶子节点数量
		int leafCnt = getLeaf();
		// 输出至少需要添加的边数
		// 公式：(叶子数 + 1) / 2
		// 解释：每添加一条边，可以将两个叶子节点配对（连接在一起）
		// 如果叶子数为奇数，最后会剩下一个，需要再加一条边
		out.println((leafCnt + 1) / 2);
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

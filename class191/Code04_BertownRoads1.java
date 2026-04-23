package class191;

// 贝尔敦道路，java版
// 给定一张无向图，一共n个点、m条边，保证所有点连通
// 每条无向边需要指定一个方向，变成有向边，还要保证任意两点互相可达
// 如果不存在方案打印0，如果存在方案，打印m条有向边
// 可以任意次序打印有向边，如果方案不只一种，打印其中一种即可
// 1 <= n <= 10^5
// 1 <= m <= 3 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/CF118E
// 测试链接 : https://codeforces.com/problemset/problem/118/E
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

// 导入IO相关类
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code04_BertownRoads1 {

	// 定义常量：最大节点数和最大边数
	public static int MAXN = 100001;
	public static int MAXM = 300001;
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

	// 标记是否可以实现（有解为true，无解为false）
	public static boolean check;
	// 存储有向边的答案
	public static int[] ans1 = new int[MAXM]; // 边的起点
	public static int[] ans2 = new int[MAXM]; // 边的终点
	// 当前收集的边数量
	public static int cnta;

	/**
	 * 添加无向边到邻接表
	 * @param u 边的起点
	 * @param v 边的终点
	 */
	public static void addEdge(int u, int v) {
		nxt[++cntg] = head[u];
		to[cntg] = v;
		head[u] = cntg;
	}

	/**
	 * Tarjan算法变形 - 求强连通分量并确定边的方向
	 * 
	 * 核心思路：
	 * 1. 对无向图进行DFS，生成有向边
	 * 2. 对于树边(u,v)，生成方向 u -> v
	 * 3. 对于回边(u,v)且dfn[v] < dfn[u]，生成方向 v -> u
	 * 4. 如果发现割边（low[v] > dfn[u]），则无法满足强连通要求，标记为无解
	 * 
	 * 这种定向方法可以保证任意两点互相可达（强连通）
	 */
	public static void tarjan(int u, int preEdge) {
		// 初始化发现时间和low值
		dfn[u] = low[u] = ++cntd;
		// 遍历所有邻接边
		for (int e = head[u]; e > 0; e = nxt[e]) {
			// 跳过来自父节点的边（反向边）
			if ((e ^ 1) == preEdge) {
				continue;
			}
			int v = to[e];
			// 对于树边和dfn[v] < dfn[u]的回边，添加有向边
			// 树边和回边需要处理，弃边不要处理
			if (dfn[v] == 0 || dfn[v] < dfn[u]) {
				cnta++; // 收集一条有向边
				ans1[cnta] = u; // 起点
				ans2[cnta] = v; // 终点
			}
			if (dfn[v] == 0) {
				// 树边：递归处理子节点
				tarjan(v, e);
				// 回溯更新low值
				low[u] = Math.min(low[u], low[v]);
				// 判断是否为割边
				// 如果存在割边，则无法让任意两点互相可达
				if (low[v] > dfn[u]) {
					check = false; // 标记为无解
				}
			} else {
				// 回边或弃边：用dfn[v]更新low[u]
				low[u] = Math.min(low[u], dfn[v]);
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
		// 初始化为有解状态
		check = true;
		// 从节点1开始执行Tarjan算法（保证连通）
		tarjan(1, 0);
		// 如果无解，输出0
		if (!check) {
			out.println(0);
		} else {
			// 如果有解，输出所有有向边
			for (int i = 1; i <= m; i++) {
				out.println(ans1[i] + " " + ans2[i]);
			}
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

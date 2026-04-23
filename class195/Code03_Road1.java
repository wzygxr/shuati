package class195;

// 道路，java版
// 一共n个点，一共m次操作，格式如下
// 操作 a b c d w: a~b范围每个点与c~d范围每个点之间，都增加权值为w的无向边
// 给定数字k，表示有k次机会，每次在通过一条边时，不用支付这条边的代价
// 所有操作完成后，打印1号点到n号点的最低代价
// 如果不存在通路，打印"CreationAugust is a sb!"
// 1 <= n <= 5 * 10^4
// 1 <= m <= 10^5
// 1 <= k <= 10
// 1 <= w <= 10^3
// 测试链接 : https://acm.hdu.edu.cn/showproblem.php?pid=5669
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

// ===================== 分层图+线段树优化建图核心知识点 =====================
// 【问题分析】
// 本题包含两个难点：
// 1. 区间到区间的连边需要优化（使用线段树优化建图）
// 2. 有k次免费通过边的机会（使用分层图技术）
//
// 【分层图核心思想】
// 将问题转化为k+1层的图，第i层表示已经使用了i次免费机会
// 状态转移：
// 1. 正常通过边：从(i, u) -> (i, v)，代价+w
// 2. 使用免费机会：从(i, u) -> (i+1, v)，代价+0
//
// 【状态设计】
// dist[node][used] = 到达node节点，已经使用used次免费机会的最小代价
//
// 【ML/DL关联价值】
// 1. 强化学习中的分层状态空间设计，不同层代表不同的资源消耗
// 2. 路径规划中的多目标优化，同时考虑时间和资源约束
// 3. 动态规划与图算法的结合思想

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.PriorityQueue;

public class Code03_Road1 {

	// ===================== 常量定义区 =====================
	// MAXN: 原始节点最大数量
	public static int MAXN = 50001;
	// MAXT: 线段树节点最大数量
	public static int MAXT = MAXN * 10;
	// MAXE: 最大边数
	public static int MAXE = MAXN * 20;
	// MAXK: 最大免费次数+1（k<=10，所以取11）
	public static int MAXK = 11;
	// INF: 无穷大值
	public static int INF = 1 << 30;

	// ===================== 输入变量区 =====================
	// t: 测试用例数量
	public static int t;
	// n: 原始图的节点数量
	public static int n;
	// m: 操作数量
	public static int m;
	// k: 免费通过边的次数
	public static int k;

	// ===================== 链式前向星存图区 =====================
	// head[u]: 节点u的第一条边的编号
	public static int[] head = new int[MAXT];
	// nxt[e]: 边e的下一条边的编号
	public static int[] nxt = new int[MAXE];
	// to[e]: 边e指向的目标节点
	public static int[] to = new int[MAXE];
	// weight[e]: 边e的权值
	public static int[] weight = new int[MAXE];
	// cntg: 边的计数器
	public static int cntg;

	// ===================== 线段树优化建图核心变量区 =====================
	// ls[i]: 节点i的左子节点编号
	public static int[] ls = new int[MAXT];
	// rs[i]: 节点i的右子节点编号
	public static int[] rs = new int[MAXT];
	// rootOut: 出树的根节点编号
	public static int rootOut;
	// rootIn: 入树的根节点编号
	public static int rootIn;
	// cntt: 当前总节点数
	public static int cntt;

	// ===================== 分层图Dijkstra变量区 =====================
	// dist[i][j]: 到达节点i，使用j次免费机会的最小代价
	public static int[][] dist = new int[MAXT][MAXK];
	// vis[i][j]: 节点i使用j次免费机会的状态是否已访问
	public static boolean[][] vis = new boolean[MAXT][MAXK];
	// heap: 优先队列，存储[node, usedTimes, cost]
	public static PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[2] - b[2]);

	// ===================== 核心函数：链式前向星加边 =====================
	public static void addEdge(int u, int v, int w) {
		nxt[++cntg] = head[u];
		to[cntg] = v;
		weight[cntg] = w;
		head[u] = cntg;
	}

	// ===================== 核心函数：构建出树 =====================
	public static int buildOut(int l, int r) {
		int rt;
		if (l == r) {
			rt = l;
		} else {
			rt = ++cntt;
			int mid = (l + r) >> 1;
			ls[rt] = buildOut(l, mid);
			rs[rt] = buildOut(mid + 1, r);
			addEdge(ls[rt], rt, 0);
			addEdge(rs[rt], rt, 0);
		}
		return rt;
	}

	// ===================== 核心函数：构建入树 =====================
	public static int buildIn(int l, int r) {
		int rt;
		if (l == r) {
			rt = l;
		} else {
			rt = ++cntt;
			int mid = (l + r) >> 1;
			ls[rt] = buildIn(l, mid);
			rs[rt] = buildIn(mid + 1, r);
			addEdge(rt, ls[rt], 0);
			addEdge(rt, rs[rt], 0);
		}
		return rt;
	}

	// ===================== 核心函数：单点→区间连边 =====================
	public static void xToRange(int jobx, int jobl, int jobr, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			addEdge(jobx, i, 0);
		} else {
			int mid = (l + r) >> 1;
			if (jobl <= mid) {
				xToRange(jobx, jobl, jobr, l, mid, ls[i]);
			}
			if (jobr > mid) {
				xToRange(jobx, jobl, jobr, mid + 1, r, rs[i]);
			}
		}
	}

	// ===================== 核心函数：区间→单点连边 =====================
	public static void rangeToX(int jobl, int jobr, int jobx, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			addEdge(i, jobx, 0);
		} else {
			int mid = (l + r) >> 1;
			if (jobl <= mid) {
				rangeToX(jobl, jobr, jobx, l, mid, ls[i]);
			}
			if (jobr > mid) {
				rangeToX(jobl, jobr, jobx, mid + 1, r, rs[i]);
			}
		}
	}

	// ===================== 核心函数：区间→区间连边 =====================
	public static void rangeToRange(int a, int b, int c, int d, int w) {
		int x = ++cntt;
		int y = ++cntt;
		rangeToX(a, b, x, 1, n, rootOut);
		xToRange(y, c, d, 1, n, rootIn);
		addEdge(x, y, w);
	}

	// ===================== 核心函数：分层图Dijkstra =====================
	// 【功能】计算从start到target的最小代价，可以使用k次免费机会
	// 【分层图思想】dist[node][used]表示到达node，已使用used次免费的最小代价
	// 【状态转移】
	// 1. 正常走边：(node, used) -> (v, used)，代价+w
	// 2. 使用免费：(node, used) -> (v, used+1)，代价+0
	public static int dijkstra(int start, int target) {
		// 初始化距离数组
		for (int i = 1; i <= cntt; i++) {
			for (int j = 0; j <= k; j++) {
				dist[i][j] = INF;
				vis[i][j] = false;
			}
		}
		// 起点状态：节点start，使用0次免费，代价0
		dist[start][0] = 0;
		heap.add(new int[] { start, 0, 0 });
		// Dijkstra主循环
		while (!heap.isEmpty()) {
			int[] cur = heap.poll();
			int node = cur[0];
			int time = cur[1];  // 已使用的免费次数
			int cost = cur[2];  // 当前总代价
			// 如果已经访问过，跳过
			if (!vis[node][time]) {
				vis[node][time] = true;
				// 到达目标，返回代价
				if (node == target) {
					return cost;
				}
				// 遍历所有邻接边
				for (int e = head[node]; e > 0; e = nxt[e]) {
					int v = to[e];
					int w = weight[e];
					// 【转移1】不使用免费机会，正常支付代价
					if (!vis[v][time] && dist[v][time] > cost + w) {
						dist[v][time] = cost + w;
						heap.add(new int[] { v, time, dist[v][time] });
					}
					// 【转移2】使用免费机会，代价不变，使用次数+1
					if (time < k && !vis[v][time + 1] && dist[v][time + 1] > cost) {
						dist[v][time + 1] = cost;
						heap.add(new int[] { v, time + 1, dist[v][time + 1] });
					}
				}
			}
		}
		return -1;  // 无法到达
	}

	// ===================== 核心函数：清空图 =====================
	public static void clear() {
		for (int i = 1; i <= cntt; i++) {
			head[i] = 0;
		}
		cntg = cntt = 0;
		heap.clear();
	}

	// ===================== 主函数 =====================
	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		t = in.nextInt();
		for (int test = 1; test <= t; test++) {
			n = in.nextInt();
			m = in.nextInt();
			k = in.nextInt();
			cntt = n;
			rootOut = buildOut(1, n);
			rootIn = buildIn(1, n);
			for (int i = 1, a, b, c, d, w; i <= m; i++) {
				a = in.nextInt();
				b = in.nextInt();
				c = in.nextInt();
				d = in.nextInt();
				w = in.nextInt();
				rangeToRange(a, b, c, d, w);
				rangeToRange(c, d, a, b, w);
			}
			int ans = dijkstra(1, n);
			if (ans == -1) {
				out.println("CreationAugust is a sb!");
			} else {
				out.println(ans);
			}
			clear();
		}
		out.flush();
		out.close();
	}

	// ===================== 快速读入工具类 =====================
	static class FastReader {
		private final byte[] buffer = new byte[1 << 16];
		private int ptr = 0, len = 0;
		private final InputStream in;

		FastReader(InputStream in) {
			this.in = in;
		}

		private int readByte() throws IOException {
			if (ptr >= len) {
				len = in.read(buffer);
				ptr = 0;
				if (len <= 0)
					return -1;
			}
			return buffer[ptr++];
		}

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

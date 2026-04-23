package class195;

// 炸弹，java版
// 一共有n个炸弹，所有炸弹排成一条直线，给定每个炸弹的坐标xi、爆炸半径ri
// 炸弹A引爆时，如果炸弹B在其影响范围里，那么炸弹B也会引爆，进而引发一连串的爆炸
// 炸弹i如果作为初始引爆的炸弹，最终会引爆多少个炸弹记为query(i)
// 计算i = 1 2 .. n时，i * query(i)的累加和，答案对 1000000007 取余
// 1 <= n <= 5 * 10^5
// -(10^18) <= xi <= +(10^18)，题目依次输入的坐标保证严格递增
// 0 <= ri <= 2 * 10^18
// 测试链接 : https://www.luogu.com.cn/problem/P5025
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

// ===================== Tarjan强连通分量+线段树优化建图核心知识点 =====================
// 【问题分析】
// 本题需要计算每个炸弹作为起点时，最终能引爆多少个炸弹
// 炸弹之间的引爆关系形成有向图，需要处理强连通分量(SCC)
//
// 【线段树优化建图】
// 每个炸弹i可以引爆坐标在[xi-ri, xi+ri]范围内的所有炸弹
// 如果直接连边，边数为O(n^2)，需要线段树优化到O(nlogn)
//
// 【Tarjan算法】
// 求强连通分量，将图缩点形成DAG
// 在DAG上可以进行动态规划，计算每个SCC能到达的范围
//
// 【递归vs迭代】
// 本题提供递归版和迭代版两种Tarjan实现
// 迭代版用于防止栈溢出，处理大规模数据
//
// 【ML/DL关联价值】
// 1. 图神经网络中的社区发现(对应SCC检测)
// 2. 大规模图数据的压缩和降维(对应缩点)
// 3. 迭代算法的栈空间优化技巧

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code05_Bomb1 {

	// ===================== 常量定义区 =====================
	// MAXN: 原始节点最大数量
	public static int MAXN = 500001;
	// MAXT: 线段树节点最大数量
	public static int MAXT = MAXN * 5;
	// MAXE: 最大边数
	public static int MAXE = MAXN * 20;
	// INF: 无穷大值
	public static int INF = 1 << 30;
	// MOD: 取模数
	public static int MOD = 1000000007;

	// ===================== 输入变量区 =====================
	// n: 炸弹数量
	public static int n;
	// location[i]: 炸弹i的坐标
	public static long[] location = new long[MAXN];
	// radius[i]: 炸弹i的爆炸半径
	public static long[] radius = new long[MAXN];

	// ===================== 边存储区 =====================
	// a[i], b[i]: 第i条边的起点和终点（用于缩点）
	public static int[] a = new int[MAXE];
	public static int[] b = new int[MAXE];
	// cnte: 边的计数器
	public static int cnte;

	// ===================== 链式前向星存图区 =====================
	// head[u]: 节点u的第一条边的编号
	public static int[] head = new int[MAXT];
	// nxt[e]: 边e的下一条边的编号
	public static int[] nxt = new int[MAXE];
	// to[e]: 边e指向的目标节点
	public static int[] to = new int[MAXE];
	// cntg: 边的计数器
	public static int cntg;

	// ===================== 线段树优化建图核心变量区 =====================
	// rangel[i], ranger[i]: 节点i代表的区间范围
	public static int[] rangel = new int[MAXT];
	public static int[] ranger = new int[MAXT];
	// ls[i], rs[i]: 节点i的左右子节点
	public static int[] ls = new int[MAXT];
	public static int[] rs = new int[MAXT];
	// root: 线段树根节点
	public static int root;
	// cntt: 当前总节点数
	public static int cntt;

	// ===================== Tarjan算法变量区 =====================
	// dfn[i]: 节点i的访问时间戳
	public static int[] dfn = new int[MAXT];
	// low[i]: 节点i能到达的最小时间戳
	public static int[] low = new int[MAXT];
	// cntd: 时间戳计数器
	public static int cntd;
	// sta: 栈，用于Tarjan
	public static int[] sta = new int[MAXT];
	// top: 栈顶指针
	public static int top;

	// ===================== 强连通分量变量区 =====================
	// belong[i]: 节点i所属的SCC编号
	public static int[] belong = new int[MAXT];
	// mostl[i], mostr[i]: SCC i能到达的最左/最右位置
	public static int[] mostl = new int[MAXT];
	public static int[] mostr = new int[MAXT];
	// sccCnt: SCC数量
	public static int sccCnt;

	// ===================== 迭代版Tarjan栈区 =====================
	// stack[i][0]: 节点u, stack[i][1]: 状态, stack[i][2]: 边e
	public static int[][] stack = new int[MAXT][3];
	public static int u, status, e;
	public static int stacksize;

	// ===================== 迭代版Tarjan辅助函数 =====================
	public static void push(int u, int status, int e) {
		stack[stacksize][0] = u;
		stack[stacksize][1] = status;
		stack[stacksize][2] = e;
		stacksize++;
	}

	public static void pop() {
		stacksize--;
		u = stack[stacksize][0];
		status = stack[stacksize][1];
		e = stack[stacksize][2];
	}

	// ===================== 核心函数：链式前向星加边 =====================
	public static void addEdge(int u, int v) {
		nxt[++cntg] = head[u];
		to[cntg] = v;
		head[u] = cntg;
	}

	// ===================== 核心函数：保存边（用于缩点） =====================
	public static void saveEdge(int u, int v) {
		a[++cnte] = u;
		b[cnte] = v;
	}

	// ===================== 核心函数：二分查找 =====================
	// 功能：找到第一个坐标>=num的炸弹编号
	public static int lower(long num) {
		int l = 1, r = n, mid, ans = n + 1;
		while (l <= r) {
			mid = (l + r) >> 1;
			if (location[mid] >= num) {
				ans = mid;
				r = mid - 1;
			} else {
				l = mid + 1;
			}
		}
		return ans;
	}

	// ===================== 核心函数：构建线段树 =====================
	public static int build(int l, int r) {
		int rt;
		if (l == r) {
			rt = l;
		} else {
			rt = ++cntt;
			int mid = (l + r) >> 1;
			ls[rt] = build(l, mid);
			rs[rt] = build(mid + 1, r);
			// 子节点向父节点连边
			addEdge(ls[rt], rt);
			addEdge(rs[rt], rt);
			// 保存边用于后续缩点
			saveEdge(ls[rt], rt);
			saveEdge(rs[rt], rt);
		}
		// 记录节点代表的区间范围
		rangel[rt] = l;
		ranger[rt] = r;
		return rt;
	}

	// ===================== 核心函数：区间→单点连边 =====================
	public static void rangeToX(int jobl, int jobr, int jobx, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			addEdge(i, jobx);
			saveEdge(i, jobx);
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

	// ===================== 核心函数：Tarjan递归版 =====================
	public static void tarjan1(int u) {
		dfn[u] = low[u] = ++cntd;
		sta[++top] = u;
		for (int e = head[u]; e > 0; e = nxt[e]) {
			int v = to[e];
			if (dfn[v] == 0) {
				tarjan1(v);
				low[u] = Math.min(low[u], low[v]);
			} else {
				if (belong[v] == 0) {
					low[u] = Math.min(low[u], dfn[v]);
				}
			}
		}
		// 找到一个SCC
		if (dfn[u] == low[u]) {
			sccCnt++;
			mostl[sccCnt] = INF;
			mostr[sccCnt] = -INF;
			int pop;
			do {
				pop = sta[top--];
				belong[pop] = sccCnt;
				// 更新SCC的区间范围
				mostl[sccCnt] = Math.min(mostl[sccCnt], rangel[pop]);
				mostr[sccCnt] = Math.max(mostr[sccCnt], ranger[pop]);
			} while (pop != u);
		}
	}

	// ===================== 核心函数：Tarjan迭代版 =====================
	public static void tarjan2(int node) {
		stacksize = 0;
		push(node, -1, -1);
		int v;
		while (stacksize > 0) {
			pop();
			if (status == -1) {
				dfn[u] = low[u] = ++cntd;
				sta[++top] = u;
				e = head[u];
			} else {
				v = to[e];
				if (status == 0) {
					low[u] = Math.min(low[u], low[v]);
				}
				if (status == 1 && belong[v] == 0) {
					low[u] = Math.min(low[u], dfn[v]);
				}
				e = nxt[e];
			}
			if (e != 0) {
				v = to[e];
				if (dfn[v] == 0) {
					push(u, 0, e);
					push(v, -1, -1);
				} else {
					push(u, 1, e);
				}
			} else {
				if (dfn[u] == low[u]) {
					sccCnt++;
					mostl[sccCnt] = INF;
					mostr[sccCnt] = -INF;
					int pop;
					do {
						pop = sta[top--];
						belong[pop] = sccCnt;
						mostl[sccCnt] = Math.min(mostl[sccCnt], rangel[pop]);
						mostr[sccCnt] = Math.max(mostr[sccCnt], ranger[pop]);
					} while (pop != u);
				}
			}
		}
	}

	// ===================== 核心函数：缩点 =====================
	public static void condense() {
		cntg = 0;
		for (int i = 1; i <= sccCnt; i++) {
			head[i] = 0;
		}
		// 重建图，SCC之间连边
		for (int i = 1; i <= cnte; i++) {
			int scc1 = belong[a[i]];
			int scc2 = belong[b[i]];
			if (scc1 != scc2) {
				addEdge(scc1, scc2);
			}
		}
	}

	// ===================== 核心函数：DAG上DP =====================
	// 功能：计算每个SCC能到达的最左和最右位置
	public static void dpOnDAG() {
		for (int u = sccCnt; u > 0; u--) {
			for (int e = head[u]; e > 0; e = nxt[e]) {
				int v = to[e];
				// 传递区间范围
				mostl[v] = Math.min(mostl[v], mostl[u]);
				mostr[v] = Math.max(mostr[v], mostr[u]);
			}
		}
	}

	// ===================== 核心函数：查询 =====================
	public static int query(int u) {
		int scc = belong[u];
		int num = mostr[scc] - mostl[scc] + 1;
		return num;
	}

	// ===================== 主函数 =====================
	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		// 读入n
		n = in.nextInt();
		cntt = n;
		// 读入每个炸弹的坐标和半径
		for (int i = 1; i <= n; i++) {
			location[i] = in.nextLong();
			radius[i] = in.nextLong();
		}
		// 构建线段树
		root = build(1, n);
		// 建立炸弹之间的引爆关系
		for (int i = 1; i <= n; i++) {
			// 计算炸弹i能引爆的范围
			int l = lower(location[i] - radius[i]);
			int r = lower(location[i] + radius[i] + 1) - 1;
			rangeToX(l, r, i, 1, n, root);
		}
		// 运行Tarjan求SCC
		for (int i = 1; i <= cntt; i++) {
			if (dfn[i] == 0) {
				// tarjan1(i);  // 递归版
				tarjan2(i);     // 迭代版
			}
		}
		// 缩点
		condense();
		// DAG上DP
		dpOnDAG();
		// 计算答案
		long ans = 0;
		for (int i = 1; i <= n; i++) {
			ans = (ans + 1L * query(i) * i) % MOD;
		}
		out.println(ans);
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

		long nextLong() throws IOException {
			int c;
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			boolean neg = false;
			if (c == '-') {
				neg = true;
				c = readByte();
			}
			long val = 0;
			while (c > ' ' && c != -1) {
				val = val * 10 + (c - '0');
				c = readByte();
			}
			return neg ? -val : val;
		}

	}

}

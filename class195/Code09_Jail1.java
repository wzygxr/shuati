package class195;

// 监狱，java版
// 一共有n个房间，给定n-1条无向边，所有房间组成一棵树
// 一共有m个囚犯，每个囚犯给出卧室房间号和工作室房间号，囚犯初始时都在卧室
// 每个囚犯的卧室和工作室一定不同，任何两个囚犯既不共用卧室，也不共用工作室
// 但是有可能某个房间，作为一个囚犯的卧室，同时作为另一个囚犯的工作室
// 你的任务是让所有囚犯从自己的卧室出发，只走最短路去自己的工作室
// 你可以随意下达指令，每条指令只能选择一个囚犯，沿一条边移动一步
// 任何时刻不能让任何房间里出现两个囚犯，打印你能否完成任务
// 1 <= n、m <= 1.2 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P9520
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

// ===================== 监狱问题核心知识点 =====================
// 【问题分析】
// 本题需要判断是否存在一种调度方案，使得所有囚犯能够同时从卧室移动到工作室
// 且不发生冲突（同一时刻同一房间不能有多个囚犯）
//
// 【约束建模】
// 将每个囚犯建模为一个节点，需要建立囚犯之间的相对顺序约束
// 如果两个囚犯的路径有交集，则需要确定他们的先后顺序
//
// 【倍增优化建图】
// 使用倍增表来高效处理路径上的约束关系
// stout[u][p]: 出表节点
// stin[u][p]: 入表节点
//
// 【拓扑排序判环】
// 将所有约束转化为有向边后，用拓扑排序检测环
// 无环则说明存在合法调度方案
//
// 【ML/DL关联价值】
// 1. 多智能体路径规划
// 2. 约束满足问题(CSP)
// 3. 冲突检测与消解

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code09_Jail1 {

	// ===================== 常量定义区 =====================
	public static int MAXN = 120001;
	public static int MAXT = MAXN * 50;
	public static int MAXE = MAXN * 200;
	public static int MAXP = 18;
	public static int t, n, m;

	// ===================== 标签编号区 =====================
	// startTag[i]: 房间i作为卧室的标签
	public static int[] startTag = new int[MAXN];
	// endTag[i]: 房间i作为工作室的标签
	public static int[] endTag = new int[MAXN];

	// ===================== 原始树存储区 =====================
	public static int[] head1 = new int[MAXN];
	public static int[] next1 = new int[MAXN << 1];
	public static int[] to1 = new int[MAXN << 1];
	public static int cnt1;

	// ===================== 关系图存储区 =====================
	public static int[] indegree = new int[MAXT];
	public static int[] head2 = new int[MAXT];
	public static int[] next2 = new int[MAXE];
	public static int[] to2 = new int[MAXE];
	public static int cnt2;

	// ===================== 树上倍增变量区 =====================
	public static int[] dep = new int[MAXN];
	public static int[] dfn = new int[MAXN];
	public static int[] siz = new int[MAXN];
	public static int[][] stjump = new int[MAXN][MAXP];
	public static int cntd;

	// ===================== 倍增优化建图变量区 =====================
	public static int[][] stout = new int[MAXN][MAXP];
	public static int[][] stin = new int[MAXN][MAXP];
	public static int cntt;

	// ===================== 拓扑排序变量区 =====================
	public static int[] que = new int[MAXT];

	// ===================== 迭代DFS栈区 =====================
	public static int[][] ufe = new int[MAXN][3];
	public static int stacksize, u, fa, e;

	// ===================== 核心函数：栈操作 =====================
	public static void push(int u, int fa, int e) {
		ufe[stacksize][0] = u;
		ufe[stacksize][1] = fa;
		ufe[stacksize][2] = e;
		stacksize++;
	}

	public static void pop() {
		--stacksize;
		u = ufe[stacksize][0];
		fa = ufe[stacksize][1];
		e = ufe[stacksize][2];
	}

	// ===================== 核心函数：原始树加边 =====================
	public static void addEdge1(int u, int v) {
		next1[++cnt1] = head1[u];
		to1[cnt1] = v;
		head1[u] = cnt1;
	}

	// ===================== 核心函数：关系图加边 =====================
	public static void addEdge2(int u, int v) {
		indegree[v]++;
		next2[++cnt2] = head2[u];
		to2[cnt2] = v;
		head2[u] = cnt2;
	}

	// ===================== 核心函数：递归版构建倍增表 =====================
	public static void build1(int u, int fa) {
		dep[u] = dep[fa] + 1;
		dfn[u] = ++cntd;
		siz[u] = 1;
		stjump[u][0] = fa;
		// 构建出表节点
		stout[u][0] = ++cntt;
		addEdge2(startTag[u], cntt);
		addEdge2(startTag[fa], cntt);
		// 构建入表节点
		stin[u][0] = ++cntt;
		addEdge2(cntt, endTag[u]);
		addEdge2(cntt, endTag[fa]);
		// 构建更高层
		for (int p = 1; p < MAXP; p++) {
			stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
			stout[u][p] = ++cntt;
			addEdge2(stout[u][p - 1], cntt);
			addEdge2(stout[stjump[u][p - 1]][p - 1], cntt);
			stin[u][p] = ++cntt;
			addEdge2(cntt, stin[u][p - 1]);
			addEdge2(cntt, stin[stjump[u][p - 1]][p - 1]);
		}
		// 递归处理子节点
		for (int e = head1[u]; e > 0; e = next1[e]) {
			int v = to1[e];
			if (v != fa) {
				build1(v, u);
				siz[u] += siz[v];
			}
		}
	}

	// ===================== 核心函数：迭代版构建倍增表 =====================
	public static void build2(int cur, int father) {
		stacksize = 0;
		push(cur, father, -1);
		while (stacksize > 0) {
			pop();
			if (e == -1) {
				// 首次访问该节点
				dep[u] = dep[fa] + 1;
				dfn[u] = ++cntd;
				siz[u] = 1;
				stjump[u][0] = fa;
				// 构建出表节点
				stout[u][0] = ++cntt;
				addEdge2(startTag[u], cntt);
				addEdge2(startTag[fa], cntt);
				// 构建入表节点
				stin[u][0] = ++cntt;
				addEdge2(cntt, endTag[u]);
				addEdge2(cntt, endTag[fa]);
				// 构建更高层
				for (int p = 1; p < MAXP; p++) {
					stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
					stout[u][p] = ++cntt;
					addEdge2(stout[u][p - 1], cntt);
					addEdge2(stout[stjump[u][p - 1]][p - 1], cntt);
					stin[u][p] = ++cntt;
					addEdge2(cntt, stin[u][p - 1]);
					addEdge2(cntt, stin[stjump[u][p - 1]][p - 1]);
				}
				e = head1[u];
			} else {
				e = next1[e];
			}
			if (e != 0) {
				push(u, fa, e);
				if (to1[e] != fa) {
					push(to1[e], u, -1);
				}
			} else {
				// 处理完所有子节点，更新子树大小
				for (int ei = head1[u]; ei > 0; ei = next1[ei]) {
					int v = to1[ei];
					if (v != fa) {
						siz[u] += siz[v];
					}
				}
			}
		}
	}

	// ===================== 核心函数：判断祖先关系 =====================
	public static boolean isAncestor(int a, int b) {
		return dfn[a] <= dfn[b] && dfn[b] < dfn[a] + siz[a];
	}

	// ===================== 核心函数：求k级祖先 =====================
	public static int kthAncestor(int x, int k) {
		for (int p = 0; p < MAXP; p++) {
			if (((k >> p) & 1) != 0) {
				x = stjump[x][p];
			}
		}
		return x;
	}

	// ===================== 核心函数：求最近公共祖先方向 =====================
	public static int nearest(int x, int y) {
		if (isAncestor(y, x)) {
			return kthAncestor(x, dep[x] - dep[y] - 1);
		} else {
			return stjump[y][0];
		}
	}

	// ===================== 核心函数：路径约束设置 =====================
	public static void pathSet(int x, int y, int prisoner) {
		if (dep[x] < dep[y]) {
			int tmp = x;
			x = y;
			y = tmp;
		}
		addEdge2(startTag[y], prisoner);
		addEdge2(prisoner, endTag[y]);
		// 利用倍增表将x向上跳到与y同深度
		for (int p = MAXP - 1; p >= 0; p--) {
			if (dep[stjump[x][p]] >= dep[y]) {
				addEdge2(stout[x][p], prisoner);
				addEdge2(prisoner, stin[x][p]);
				x = stjump[x][p];
			}
		}
		if (x == y) {
			return;
		}
		// x和y同时向上跳
		for (int p = MAXP - 1; p >= 0; p--) {
			if (stjump[x][p] != stjump[y][p]) {
				addEdge2(stout[x][p], prisoner);
				addEdge2(stout[y][p], prisoner);
				addEdge2(prisoner, stin[x][p]);
				addEdge2(prisoner, stin[y][p]);
				x = stjump[x][p];
				y = stjump[y][p];
			}
		}
		addEdge2(stout[x][0], prisoner);
		addEdge2(prisoner, stin[x][0]);
	}

	// ===================== 核心函数：建立囚犯约束 =====================
	public static void link(int x, int y) {
		// 创建囚犯节点
		int prisoner = ++cntt;
		// 囚犯与卧室、工作室的约束
		addEdge2(prisoner, startTag[x]);
		addEdge2(prisoner, endTag[x]);
		addEdge2(startTag[y], prisoner);
		addEdge2(endTag[y], prisoner);
		// 如果x和y不是父子关系，需要处理路径上的约束
		if (stjump[x][0] != y && stjump[y][0] != x) {
			int a = nearest(y, x);
			int b = nearest(x, y);
			pathSet(a, b, prisoner);
		}
	}

	// ===================== 核心函数：拓扑排序判环 =====================
	public static boolean topo() {
		int qi = 1, qsiz = 0;
		// 将所有入度为0的节点入队
		for (int i = 1; i <= cntt; i++) {
			if (indegree[i] == 0) {
				que[++qsiz] = i;
			}
		}
		// 拓扑排序主循环
		while (qi <= qsiz) {
			int u = que[qi++];
			// 遍历所有邻接边
			for (int e = head2[u]; e > 0; e = next2[e]) {
				int v = to2[e];
				if (--indegree[v] == 0) {
					que[++qsiz] = v;
				}
			}
		}
		// 如果所有节点都被访问，说明无环
		return qsiz == cntt;
	}

	// ===================== 核心函数：清空数据 =====================
	public static void clear() {
		for (int i = 1; i <= n; i++) {
			head1[i] = 0;
		}
		for (int i = 1; i <= cntt; i++) {
			head2[i] = indegree[i] = 0;
		}
		cnt1 = cnt2 = cntt = cntd = 0;
		dep[1] = 0;
	}

	// ===================== 主函数 =====================
	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		// 读入测试用例数
		t = in.nextInt();
		for (int c = 1; c <= t; c++) {
			// 读入n
			n = in.nextInt();
			cntt = n << 1;
			// 初始化标签
			for (int i = 1; i <= n; i++) {
				startTag[i] = i;
				endTag[i] = i + n;
			}
			// 读入树的边
			for (int i = 1, u, v; i < n; i++) {
				u = in.nextInt();
				v = in.nextInt();
				addEdge1(u, v);
				addEdge1(v, u);
			}
			// 构建倍增表（使用迭代版避免栈溢出）
			// build1(1, 1);
			build2(1, 1);
			// 读入m个囚犯
			m = in.nextInt();
			for (int i = 1, x, y; i <= m; i++) {
				x = in.nextInt();
				y = in.nextInt();
				link(x, y);
			}
			// 拓扑排序判断是否存在合法方案
			boolean ans = topo();
			out.println(ans ? "Yes" : "No");
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
				if (len <= 0) {
					return -1;
				}
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

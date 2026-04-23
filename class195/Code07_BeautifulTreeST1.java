package class195;

// 美丽的树，倍增优化建图，java版
// 一共n个节点，给定n-1条无向边，所有节点组成一棵树，1号节点是根
// 你需要给每个节点赋值，但是不能破坏如下的m条关系，关系的格式如下
// 关系 1 a b c : 节点a到节点b的路径上，值最小的节点必须是节点c，输入保证c一定在路径上
// 关系 2 a b c : 节点a到节点b的路径上，值最大的节点必须是节点c，输入保证c一定在路径上
// 如果存在赋值方案，并且这些值是1到n的一个排列，打印一种方案即可，否则打印-1
// 2 <= n、m <= 2 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/CF1904F
// 测试链接 : https://codeforces.com/problemset/problem/1904/F
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

// ===================== 倍增+区间表优化建图核心知识点 =====================
// 【问题分析】
// 本题需要在树上给节点赋值，满足路径上的最值约束
// 与树剖版本不同，这里使用倍增+区间表来优化建图
//
// 【倍增算法】
// 预处理每个节点的2^k级祖先，支持O(logn)的LCA查询
// 倍增表可以将任意路径拆分为O(logn)个区间
//
// 【区间表优化建图】
// 类似线段树，但使用倍增表来组织区间
// stout[u][p]: 表示从u向上2^p层的出表节点
// stin[u][p]: 表示从u向上2^p层的入表节点
//
// 【路径处理】
// 利用倍增表将路径拆分为若干区间，然后建立约束边
//
// 【ML/DL关联价值】
// 1. 层次化表示学习
// 2. 树结构数据的快速检索

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code07_BeautifulTreeST1 {

	// ===================== 常量定义区 =====================
	// MAXN: 原始节点最大数量
	public static int MAXN = 200001;
	// MAXT: 总节点最大数量
	public static int MAXT = MAXN * 41;
	// MAXE: 最大边数
	public static int MAXE = MAXN * 201;
	// MAXP: 倍增层数，log2(2e5) ≈ 18
	public static int MAXP = 17;
	// MAXK: 倍增表总大小
	public static int MAXK = MAXN * MAXP;

	// ===================== 输入变量区 =====================
	public static int n, m;

	// ===================== 原树存储区 =====================
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
	// stjump[u][p]: 节点u的2^p级祖先
	public static int[] stjump = new int[MAXK];
	public static int cntd;

	// ===================== 倍增优化建图变量区 =====================
	// stout[u][p]: 出表节点，用于区间→单点连边
	public static int[] stout = new int[MAXK];
	// stin[u][p]: 入表节点，用于单点→区间连边
	public static int[] stin = new int[MAXK];
	public static int cntt;

	// ===================== 拓扑排序与答案区 =====================
	public static int[] que = new int[MAXT];
	public static int[] ans = new int[MAXN];

	// ===================== 核心函数：计算索引 =====================
	// 将二维索引(u, p)映射到一维数组
	public static int idx(int u, int p) {
		return u * MAXP + p;
	}

	// ===================== 核心函数：原树加边 =====================
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

	// ===================== 核心函数：构建倍增表和区间表 =====================
	public static void build(int u, int fa) {
		dep[u] = dep[fa] + 1;
		dfn[u] = ++cntd;
		siz[u] = 1;
		// 初始化2^0级祖先
		stjump[idx(u, 0)] = fa;
		// 构建出表节点
		stout[idx(u, 0)] = ++cntt;
		addEdge2(u, cntt);
		addEdge2(fa, cntt);
		// 构建入表节点
		stin[idx(u, 0)] = ++cntt;
		addEdge2(cntt, u);
		addEdge2(cntt, fa);
		// 构建更高层的倍增表
		for (int p = 1; p < MAXP; p++) {
			// 计算2^p级祖先
			stjump[idx(u, p)] = stjump[idx(stjump[idx(u, p - 1)], p - 1)];
			// 构建出表节点
			stout[idx(u, p)] = ++cntt;
			addEdge2(stout[idx(u, p - 1)], cntt);
			addEdge2(stout[idx(stjump[idx(u, p - 1)], p - 1)], cntt);
			// 构建入表节点
			stin[idx(u, p)] = ++cntt;
			addEdge2(cntt, stin[idx(u, p - 1)]);
			addEdge2(cntt, stin[idx(stjump[idx(u, p - 1)], p - 1)]);
		}
		// 递归处理子节点
		for (int e = head1[u]; e > 0; e = next1[e]) {
			int v = to1[e];
			if (v != fa) {
				build(v, u);
				siz[u] += siz[v];
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
				x = stjump[idx(x, p)];
			}
		}
		return x;
	}

	// ===================== 核心函数：求最近公共祖先方向 =====================
	// 返回从y走向x的下一个节点
	public static int nearest(int x, int y) {
		if (isAncestor(y, x)) {
			// y是x的祖先，返回x的(dep[x]-dep[y]-1)级祖先
			return kthAncestor(x, dep[x] - dep[y] - 1);
		} else {
			// y不是x的祖先，返回y的父节点
			return stjump[idx(y, 0)];
		}
	}

	// ===================== 核心函数：路径出表连边 =====================
	// 建立从路径x到y上所有节点（除y外）到c的边
	public static void pathOut(int x, int y, int c) {
		if (dep[x] < dep[y]) {
			int tmp = x;
			x = y;
			y = tmp;
		}
		// y直接连边
		addEdge2(y, c);
		// 利用倍增表将x向上跳到与y同深度
		for (int p = MAXP - 1; p >= 0; p--) {
			if (dep[stjump[idx(x, p)]] >= dep[y]) {
				addEdge2(stout[idx(x, p)], c);
				x = stjump[idx(x, p)];
			}
		}
		if (x == y) {
			return;
		}
		// x和y同时向上跳
		for (int p = MAXP - 1; p >= 0; p--) {
			if (stjump[idx(x, p)] != stjump[idx(y, p)]) {
				addEdge2(stout[idx(x, p)], c);
				addEdge2(stout[idx(y, p)], c);
				x = stjump[idx(x, p)];
				y = stjump[idx(y, p)];
			}
		}
		// 最后一步
		addEdge2(stout[idx(x, 0)], c);
	}

	// ===================== 核心函数：路径入表连边 =====================
	// 建立从c到路径x到y上所有节点（除y外）的边
	public static void pathIn(int x, int y, int c) {
		if (dep[x] < dep[y]) {
			int tmp = x;
			x = y;
			y = tmp;
		}
		// y直接连边
		addEdge2(c, y);
		// 利用倍增表将x向上跳到与y同深度
		for (int p = MAXP - 1; p >= 0; p--) {
			if (dep[stjump[idx(x, p)]] >= dep[y]) {
				addEdge2(c, stin[idx(x, p)]);
				x = stjump[idx(x, p)];
			}
		}
		if (x == y) {
			return;
		}
		// x和y同时向上跳
		for (int p = MAXP - 1; p >= 0; p--) {
			if (stjump[idx(x, p)] != stjump[idx(y, p)]) {
				addEdge2(c, stin[idx(x, p)]);
				addEdge2(c, stin[idx(y, p)]);
				x = stjump[idx(x, p)];
				y = stjump[idx(y, p)];
			}
		}
		// 最后一步
		addEdge2(c, stin[idx(x, 0)]);
	}

	// ===================== 核心函数：路径最小值约束 =====================
	public static void pathMin(int a, int b, int c) {
		if (a != c) {
			pathIn(a, nearest(a, c), c);
		}
		if (b != c) {
			pathIn(b, nearest(b, c), c);
		}
	}

	// ===================== 核心函数：路径最大值约束 =====================
	public static void pathMax(int a, int b, int c) {
		if (a != c) {
			pathOut(a, nearest(a, c), c);
		}
		if (b != c) {
			pathOut(b, nearest(b, c), c);
		}
	}

	// ===================== 核心函数：拓扑排序 =====================
	public static boolean topo() {
		int qi = 1, qsiz = 0;
		// 将所有入度为0的节点入队
		for (int i = 1; i <= cntt; i++) {
			if (indegree[i] == 0) {
				que[++qsiz] = i;
			}
		}
		int val = 0;
		// 拓扑排序主循环
		while (qi <= qsiz) {
			int u = que[qi++];
			// 如果是原始节点，赋予当前值
			if (u <= n) {
				ans[u] = ++val;
			}
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

	// ===================== 主函数 =====================
	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		// 读入n和m
		n = in.nextInt();
		m = in.nextInt();
		cntt = n;
		// 读入树的边
		for (int i = 1, u, v; i < n; i++) {
			u = in.nextInt();
			v = in.nextInt();
			addEdge1(u, v);
			addEdge1(v, u);
		}
		// 构建倍增表和区间表
		build(1, 1);
		// 处理m条关系
		for (int i = 1, op, a, b, c; i <= m; i++) {
			op = in.nextInt();
			a = in.nextInt();
			b = in.nextInt();
			c = in.nextInt();
			if (op == 1) {
				pathMin(a, b, c);
			} else {
				pathMax(a, b, c);
			}
		}
		// 拓扑排序判断并计算答案
		boolean check = topo();
		if (check) {
			for (int i = 1; i <= n; i++) {
				out.print(ans[i]);
				out.print(" ");
			}
		} else {
			out.println(-1);
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

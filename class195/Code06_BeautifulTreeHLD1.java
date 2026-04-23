package class195;

// 美丽的树，树剖优化建图，java版
// 一共n个节点，给定n-1条无向边，所有节点组成一棵树，1号节点是根
// 你需要给每个节点赋值，但是不能破坏如下的m条关系，关系的格式如下
// 关系 1 a b c : 节点a到节点b的路径上，值最小的节点必须是节点c，输入保证c一定在路径上
// 关系 2 a b c : 节点a到节点b的路径上，值最大的节点必须是节点c，输入保证c一定在路径上
// 如果存在赋值方案，并且这些值是1到n的一个排列，打印一种方案即可，否则打印-1
// 2 <= n、m <= 2 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/CF1904F
// 测试链接 : https://codeforces.com/problemset/problem/1904/F
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

// ===================== 树链剖分+线段树优化建图核心知识点 =====================
// 【问题分析】
// 本题需要在树上给节点赋值，满足路径上的最值约束
// 约束类型：路径上最小/最大值必须是特定节点
//
// 【树链剖分(HLD)】
// 将树分解为若干条重链，将路径查询转化为O(logn)个区间
// 配合线段树，可以高效处理路径上的区间操作
//
// 【线段树优化建图】
// 对于"路径上除c外所有节点值 > c"的约束
// 需要建立c到路径上其他所有节点的边
// 使用线段树优化，将O(n)条边优化到O(logn)条
//
// 【拓扑排序判环】
// 将所有约束转化为有向边后，用拓扑排序检测环
// 无环则说明存在合法赋值方案
//
// 【ML/DL关联价值】
// 1. 树结构数据的层次化处理
// 2. 约束传播与一致性检验
// 3. 图神经网络中的树形结构建模

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code06_BeautifulTreeHLD1 {

	// ===================== 常量定义区 =====================
	// MAXN: 原始节点最大数量
	public static int MAXN = 200001;
	// MAXT: 线段树节点最大数量
	public static int MAXT = MAXN * 10;
	// MAXE: 最大边数
	public static int MAXE = MAXN * 50;

	// ===================== 输入变量区 =====================
	// n: 节点数量
	public static int n;
	// m: 关系数量
	public static int m;

	// ===================== 原始树的存储区 =====================
	// head1[u]: 节点u的第一条边的编号
	public static int[] head1 = new int[MAXN];
	// next1[e]: 边e的下一条边的编号
	public static int[] next1 = new int[MAXN << 1];
	// to1[e]: 边e指向的目标节点
	public static int[] to1 = new int[MAXN << 1];
	// cnt1: 边的计数器
	public static int cnt1;

	// ===================== 关系图存储区 =====================
	// indegree[v]: 节点v的入度
	public static int[] indegree = new int[MAXT];
	// head2[u]: 节点u的第一条边的编号
	public static int[] head2 = new int[MAXT];
	// next2[e]: 边e的下一条边的编号
	public static int[] next2 = new int[MAXE];
	// to2[e]: 边e指向的目标节点
	public static int[] to2 = new int[MAXE];
	// cnt2: 边的计数器
	public static int cnt2;

	// ===================== 线段树优化建图核心变量区 =====================
	// ls[i], rs[i]: 节点i的左右子节点
	public static int[] ls = new int[MAXT];
	public static int[] rs = new int[MAXT];
	// rootOut: 出树的根节点
	public static int rootOut;
	// rootIn: 入树的根节点
	public static int rootIn;
	// cntt: 当前总节点数
	public static int cntt;

	// ===================== 树链剖分变量区 =====================
	// fa[u]: 节点u的父节点
	public static int[] fa = new int[MAXN];
	// dep[u]: 节点u的深度
	public static int[] dep = new int[MAXN];
	// siz[u]: 以u为根的子树大小
	public static int[] siz = new int[MAXN];
	// son[u]: 节点u的重儿子
	public static int[] son = new int[MAXN];
	// top[u]: 节点u所在重链的顶端
	public static int[] top = new int[MAXN];
	// dfn[u]: 节点u的DFS序
	public static int[] dfn = new int[MAXN];
	// seg[i]: DFS序为i的节点编号
	public static int[] seg = new int[MAXN];
	// cntd: DFS序计数器
	public static int cntd;

	// ===================== 拓扑排序与答案区 =====================
	// que: 拓扑排序队列
	public static int[] que = new int[MAXT];
	// ans[i]: 节点i的最终赋值
	public static int[] ans = new int[MAXN];

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
			// 子节点向父节点连边
			addEdge2(ls[rt], rt);
			addEdge2(rs[rt], rt);
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
			// 父节点向子节点连边
			addEdge2(rt, ls[rt]);
			addEdge2(rt, rs[rt]);
		}
		return rt;
	}

	// ===================== 核心函数：单点→区间连边 =====================
	public static void xToRange(int jobx, int jobl, int jobr, int l, int r, int i) {
		if (jobl > jobr) {
			return;
		}
		if (jobl <= l && r <= jobr) {
			addEdge2(jobx, i);
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
		if (jobl > jobr) {
			return;
		}
		if (jobl <= l && r <= jobr) {
			addEdge2(i, jobx);
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

	// ===================== 核心函数：树链剖分第一次DFS =====================
	// 功能：计算每个节点的父节点、深度、子树大小、重儿子
	public static void dfs1(int u, int f) {
		fa[u] = f;
		dep[u] = dep[f] + 1;
		siz[u] = 1;
		for (int e = head1[u], v; e > 0; e = next1[e]) {
			v = to1[e];
			if (v != f) {
				dfs1(v, u);
				siz[u] += siz[v];
				// 更新重儿子
				if (son[u] == 0 || siz[son[u]] < siz[v]) {
					son[u] = v;
				}
			}
		}
	}

	// ===================== 核心函数：树链剖分第二次DFS =====================
	// 功能：确定每个节点的DFS序和所在重链的顶端
	public static void dfs2(int u, int t) {
		top[u] = t;
		dfn[u] = ++cntd;
		seg[cntd] = u;
		// 叶子节点直接返回
		if (son[u] == 0) {
			return;
		}
		// 先遍历重儿子
		dfs2(son[u], t);
		// 再遍历轻儿子
		for (int e = head1[u], v; e > 0; e = next1[e]) {
			v = to1[e];
			if (v != fa[u] && v != son[u]) {
				dfs2(v, v);
			}
		}
	}

	// ===================== 核心函数：路径约束设置 =====================
	// op=1: c是路径上的最小值，其他节点值 > c
	// op=2: c是路径上的最大值，其他节点值 < c
	public static void pathSet(int op, int x, int y, int z) {
		if (op == 1) {
			// c是最小值，c要小于路径上其他所有节点
			if (x <= z && z <= y) {
				// c在路径中间，分两段连边
				xToRange(z, x, z - 1, 1, n, rootIn);
				xToRange(z, z + 1, y, 1, n, rootIn);
			} else {
				// c在路径端点，直接连边
				xToRange(z, x, y, 1, n, rootIn);
			}
		} else {
			// c是最大值，c要大于路径上其他所有节点
			if (x <= z && z <= y) {
				// c在路径中间，分两段连边
				rangeToX(x, z - 1, z, 1, n, rootOut);
				rangeToX(z + 1, y, z, 1, n, rootOut);
			} else {
				// c在路径端点，直接连边
				rangeToX(x, y, z, 1, n, rootOut);
			}
		}
	}

	// ===================== 核心函数：树上路径处理 =====================
	// 功能：处理从a到b的路径上的约束，c是路径上的最值点
	public static void link(int op, int a, int b, int c) {
		// 将路径拆分为若干条重链区间
		while (top[a] != top[b]) {
			// 每次处理深度较大的重链
			if (dep[top[a]] < dep[top[b]]) {
				int tmp = a;
				a = b;
				b = tmp;
			}
			// 处理从top[a]到a的重链区间
			pathSet(op, dfn[top[a]], dfn[a], dfn[c]);
			a = fa[top[a]];
		}
		// 处理最后一段在同一重链上的路径
		pathSet(op, Math.min(dfn[a], dfn[b]), Math.max(dfn[a], dfn[b]), dfn[c]);
	}

	// ===================== 核心函数：拓扑排序 =====================
	// 功能：判断是否存在合法赋值方案，如果存在则计算答案
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
				ans[seg[u]] = ++val;
			}
			// 遍历所有邻接边
			for (int e = head2[u]; e > 0; e = next2[e]) {
				int v = to2[e];
				if (--indegree[v] == 0) {
					que[++qsiz] = v;
				}
			}
		}
		// 如果所有节点都被访问，说明无环，存在合法方案
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
		// 构建线段树
		rootOut = buildOut(1, n);
		rootIn = buildIn(1, n);
		// 读入树的边
		for (int i = 1, u, v; i < n; i++) {
			u = in.nextInt();
			v = in.nextInt();
			addEdge1(u, v);
			addEdge1(v, u);
		}
		// 树链剖分
		dfs1(1, 0);
		dfs2(1, 1);
		// 处理m条关系
		for (int i = 1, op, a, b, c; i <= m; i++) {
			op = in.nextInt();
			a = in.nextInt();
			b = in.nextInt();
			c = in.nextInt();
			link(op, a, b, c);
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

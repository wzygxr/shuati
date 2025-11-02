package class161;

// 染色，java版
// 题目来源：洛谷P2486 [SDOI2011]染色
// 题目链接：https://www.luogu.com.cn/problem/P2486
// 
// 题目描述：
// 一共有n个节点，给定n-1条边，节点连成一棵树，每个节点给定初始颜色值
// 连续相同颜色被认为是一段，变化了就认为是另一段
// 比如，112221，有三个颜色段，分别为 11、222、1
// 一共有m条操作，每种操作是如下2种类型中的一种
// 操作 C x y z : x到y的路径上，每个节点的颜色都改为z
// 操作 Q x y   : x到y的路径上，打印有几个颜色段
// 1 <= n、m <= 10^5
// 1 <= 任何时候的颜色值 <= 10^9
//
// 解题思路：
// 这是一道经典的树链剖分应用题，需要处理树上路径的染色和查询操作。
// 树上路径操作可以通过树链剖分转化为区间操作，再结合线段树进行高效处理。
// 关键在于线段树节点需要维护区间左端颜色、右端颜色以及颜色段数，并正确处理区间合并时的边界情况。
//
// 算法步骤：
// 1. 构建树结构，进行树链剖分（dfs1计算重儿子，dfs2计算dfn序）
// 2. 使用线段树维护每个区间的颜色信息：
//    - 区间颜色段数
//    - 区间左端颜色
//    - 区间右端颜色
//    - 懒标记（颜色更新）
// 3. 对于染色操作：更新路径上所有节点的颜色
// 4. 对于查询操作：统计路径上的颜色段数，注意路径连接处颜色相同的合并
//
// 时间复杂度分析：
// - 树链剖分预处理：O(n)
// - 每次操作：O(log²n)
// - 总体复杂度：O(m log²n)
// 空间复杂度：O(n)
//
// 是否为最优解：
// 是的，这是该问题的最优解之一。树链剖分能够将树上路径操作转化为区间操作，
// 再结合线段树的数据结构，可以高效处理大量查询和更新操作。
//
// 相关题目链接：
// 1. 洛谷P2486 [SDOI2011]染色（本题）：https://www.luogu.com.cn/problem/P2486
// 2. 洛谷P2146 [NOI2015]软件包管理器：https://www.luogu.com.cn/problem/P2146
// 3. 洛谷P2590 [ZJOI2008]树的统计：https://www.luogu.com.cn/problem/P2590
// 4. Codeforces 916E Jamie and Tree：https://codeforces.com/problemset/problem/916/E
// 5. HackerEarth Tree Query：https://www.hackerearth.com/practice/algorithms/graphs/tree-graphs/practice-problems/algorithm/tree-query/
//
// Java实现参考：Code05_Coloring1.java（当前文件）
// Python实现参考：Code_LuoguP2486_Coloring.py
// C++实现参考：Code05_Coloring2.java

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Code05_Coloring1 {

	public static int MAXN = 100001;
	public static int n, m;
	public static int[] arr = new int[MAXN];

	public static int[] head = new int[MAXN];
	public static int[] next = new int[MAXN << 1];
	public static int[] to = new int[MAXN << 1];
	public static int cntg = 0;

	public static int[] fa = new int[MAXN];
	public static int[] dep = new int[MAXN];
	public static int[] siz = new int[MAXN];
	public static int[] son = new int[MAXN];
	public static int[] top = new int[MAXN];
	public static int[] dfn = new int[MAXN];
	public static int[] seg = new int[MAXN];
	public static int cntd = 0;

	public static int[] sum = new int[MAXN << 2];
	public static int[] lcolor = new int[MAXN << 2];
	public static int[] rcolor = new int[MAXN << 2];
	// change是线段树的懒更新信息
	// change[i] == 0 代表没有懒更新信息
	// change[i] != 0 代表颜色重置为change[i]
	public static int[] change = new int[MAXN << 2];

	public static void addEdge(int u, int v) {
		next[++cntg] = head[u];
		to[cntg] = v;
		head[u] = cntg;
	}

	// 递归版，C++可以通过，java会爆栈
	public static void dfs1(int u, int f) {
		fa[u] = f;
		dep[u] = dep[f] + 1;
		siz[u] = 1;
		for (int e = head[u], v; e > 0; e = next[e]) {
			v = to[e];
			if (v != f) {
				dfs1(v, u);
			}
		}
		for (int e = head[u], v; e > 0; e = next[e]) {
			v = to[e];
			if (v != f) {
				siz[u] += siz[v];
				if (son[u] == 0 || siz[son[u]] < siz[v]) {
					son[u] = v;
				}
			}
		}
	}

	// 递归版，C++可以通过，java会爆栈
	public static void dfs2(int u, int t) {
		top[u] = t;
		dfn[u] = ++cntd;
		seg[cntd] = u;
		if (son[u] == 0) {
			return;
		}
		dfs2(son[u], t);
		for (int e = head[u], v; e > 0; e = next[e]) {
			v = to[e];
			if (v != fa[u] && v != son[u]) {
				dfs2(v, v);
			}
		}
	}

	// 不会改迭代版，去看讲解118，详解了从递归版改迭代版
	public static int[][] fse = new int[MAXN][3];

	public static int stacksize, first, second, edge;

	public static void push(int fir, int sec, int edg) {
		fse[stacksize][0] = fir;
		fse[stacksize][1] = sec;
		fse[stacksize][2] = edg;
		stacksize++;
	}

	public static void pop() {
		--stacksize;
		first = fse[stacksize][0];
		second = fse[stacksize][1];
		edge = fse[stacksize][2];
	}

	// dfs1的迭代版
	public static void dfs3() {
		stacksize = 0;
		push(1, 0, -1);
		while (stacksize > 0) {
			pop();
			if (edge == -1) {
				fa[first] = second;
				dep[first] = dep[second] + 1;
				siz[first] = 1;
				edge = head[first];
			} else {
				edge = next[edge];
			}
			if (edge != 0) {
				push(first, second, edge);
				if (to[edge] != second) {
					push(to[edge], first, -1);
				}
			} else {
				for (int e = head[first], v; e > 0; e = next[e]) {
					v = to[e];
					if (v != second) {
						siz[first] += siz[v];
						if (son[first] == 0 || siz[son[first]] < siz[v]) {
							son[first] = v;
						}
					}
				}
			}
		}
	}

	// dfs2的迭代版
	public static void dfs4() {
		stacksize = 0;
		push(1, 1, -1);
		while (stacksize > 0) {
			pop();
			if (edge == -1) { // edge == -1，表示第一次来到当前节点，并且先处理重儿子
				top[first] = second;
				dfn[first] = ++cntd;
				seg[cntd] = first;
				if (son[first] == 0) {
					continue;
				}
				push(first, second, -2);
				push(son[first], second, -1);
				continue;
			} else if (edge == -2) { // edge == -2，表示处理完当前节点的重儿子，回到了当前节点
				edge = head[first];
			} else { // edge >= 0, 继续处理其他的边
				edge = next[edge];
			}
			if (edge != 0) {
				push(first, second, edge);
				if (to[edge] != fa[first] && to[edge] != son[first]) {
					push(to[edge], to[edge], -1);
				}
			}
		}
	}

	public static void up(int i) {
		sum[i] = sum[i << 1] + sum[i << 1 | 1];
		if (rcolor[i << 1] == lcolor[i << 1 | 1]) {
			sum[i]--;
		}
		lcolor[i] = lcolor[i << 1];
		rcolor[i] = rcolor[i << 1 | 1];
	}

	public static void lazy(int i, int v) {
		sum[i] = 1;
		lcolor[i] = v;
		rcolor[i] = v;
		change[i] = v;
	}

	public static void down(int i) {
		if (change[i] != 0) {
			lazy(i << 1, change[i]);
			lazy(i << 1 | 1, change[i]);
			change[i] = 0;
		}
	}

	public static void build(int l, int r, int i) {
		if (l == r) {
			sum[i] = 1;
			lcolor[i] = arr[seg[l]];
			rcolor[i] = arr[seg[l]];
		} else {
			int mid = (l + r) / 2;
			build(l, mid, i << 1);
			build(mid + 1, r, i << 1 | 1);
			up(i);
		}
	}

	public static void update(int jobl, int jobr, int jobv, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			lazy(i, jobv);
		} else {
			down(i);
			int mid = (l + r) / 2;
			if (jobl <= mid) {
				update(jobl, jobr, jobv, l, mid, i << 1);
			}
			if (jobr > mid) {
				update(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
			}
			up(i);
		}
	}

	public static int query(int jobl, int jobr, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			return sum[i];
		}
		down(i);
		int mid = (l + r) / 2;
		if (jobr <= mid) {
			return query(jobl, jobr, l, mid, i << 1);
		} else if (jobl > mid) {
			return query(jobl, jobr, mid + 1, r, i << 1 | 1);
		} else {
			int ans = query(jobl, jobr, l, mid, i << 1) + query(jobl, jobr, mid + 1, r, i << 1 | 1);
			if (rcolor[i << 1] == lcolor[i << 1 | 1]) {
				ans--;
			}
			return ans;
		}
	}

	// 查询单点颜色，jobi为节点的dfn序号
	public static int pointColor(int jobi, int l, int r, int i) {
		if (l == r) {
			return lcolor[i];
		}
		down(i);
		int mid = (l + r) / 2;
		if (jobi <= mid) {
			return pointColor(jobi, l, mid, i << 1);
		} else {
			return pointColor(jobi, mid + 1, r, i << 1 | 1);
		}
	}

	public static void pathUpdate(int x, int y, int v) {
		while (top[x] != top[y]) {
			if (dep[top[x]] <= dep[top[y]]) {
				update(dfn[top[y]], dfn[y], v, 1, n, 1);
				y = fa[top[y]];
			} else {
				update(dfn[top[x]], dfn[x], v, 1, n, 1);
				x = fa[top[x]];
			}
		}
		update(Math.min(dfn[x], dfn[y]), Math.max(dfn[x], dfn[y]), v, 1, n, 1);
	}

	public static int pathColors(int x, int y) {
		int ans = 0, sonc, fac;
		while (top[x] != top[y]) {
			if (dep[top[x]] <= dep[top[y]]) {
				ans += query(dfn[top[y]], dfn[y], 1, n, 1);
				sonc = pointColor(dfn[top[y]], 1, n, 1);
				fac = pointColor(dfn[fa[top[y]]], 1, n, 1);
				y = fa[top[y]];
			} else {
				ans += query(dfn[top[x]], dfn[x], 1, n, 1);
				sonc = pointColor(dfn[top[x]], 1, n, 1);
				fac = pointColor(dfn[fa[top[x]]], 1, n, 1);
				x = fa[top[x]];
			}
			if (sonc == fac) {
				ans--;
			}
		}
		ans += query(Math.min(dfn[x], dfn[y]), Math.max(dfn[x], dfn[y]), 1, n, 1);
		return ans;
	}

	public static void main(String[] args) {
		Kattio io = new Kattio();
		n = io.nextInt();
		m = io.nextInt();
		for (int i = 1; i <= n; i++) {
			arr[i] = io.nextInt();
		}
		for (int i = 1, u, v; i < n; i++) {
			u = io.nextInt();
			v = io.nextInt();
			addEdge(u, v);
			addEdge(v, u);
		}
		dfs3();
		dfs4();
		build(1, n, 1);
		String op;
		int x, y, z;
		for (int i = 1; i <= m; i++) {
			op = io.next();
			x = io.nextInt();
			y = io.nextInt();
			if (op.equals("C")) {
				z = io.nextInt();
				pathUpdate(x, y, z);
			} else {
				io.println(pathColors(x, y));
			}
		}
		io.flush();
		io.close();
	}

	// 读写工具类
	public static class Kattio extends PrintWriter {
		private BufferedReader r;
		private StringTokenizer st;

		public Kattio() {
			this(System.in, System.out);
		}

		public Kattio(InputStream i, OutputStream o) {
			super(o);
			r = new BufferedReader(new InputStreamReader(i));
		}

		public Kattio(String intput, String output) throws IOException {
			super(output);
			r = new BufferedReader(new FileReader(intput));
		}

		public String next() {
			try {
				while (st == null || !st.hasMoreTokens())
					st = new StringTokenizer(r.readLine());
				return st.nextToken();
			} catch (Exception e) {
			}
			return null;
		}

		public int nextInt() {
			return Integer.parseInt(next());
		}

		public double nextDouble() {
			return Double.parseDouble(next());
		}

		public long nextLong() {
			return Long.parseLong(next());
		}
	}

}
package class161;

// 重链剖分模版题，java版
// 题目来源：洛谷P3384 【模板】重链剖分/树链剖分
// 题目链接：https://www.luogu.com.cn/problem/P3384
//
// 题目描述：
// 如题，已知一棵包含N个结点的树（连通且无环），每个节点上包含一个数值，需要支持以下操作：
// 操作 1 x y z : x到y的路径上，每个节点值增加z
// 操作 2 x y   : x到y的路径上，打印所有节点值的累加和
// 操作 3 x z   : x为头的子树上，每个节点值增加z
// 操作 4 x     : x为头的子树上，打印所有节点值的累加和
// 1 <= n、m <= 10^5
// 1 <= MOD <= 2^30
// 输入的值都为int类型
// 查询操作时，打印(查询结果 % MOD)，题目会给定MOD值
//
// 解题思路：
// 使用树链剖分将树上问题转化为线段树问题
// 1. 树链剖分：通过两次DFS将树划分为多条重链
// 2. 线段树：维护区间和，支持区间修改和区间查询
// 3. 路径操作：将树上路径操作转化为多个区间操作
//
// 算法步骤：
// 1. 构建树结构，进行树链剖分（dfs1计算重儿子，dfs2计算dfn序）
// 2. 使用线段树维护每个区间的权值和，支持区间加法操作
// 3. 对于路径加法操作：将路径分解为多段重链进行区间更新
// 4. 对于子树加法操作：直接对子树对应的连续区间进行更新
// 5. 对于路径查询操作：将路径分解为多段重链进行区间查询
// 6. 对于子树查询操作：直接对子树对应的连续区间进行查询
//
// 时间复杂度分析：
// - 树链剖分预处理：O(n)
// - 每次操作：O(log²n)
// - 总体复杂度：O(m log²n)
// 空间复杂度：O(n)
//
// 是否为最优解：
// 是的，树链剖分是解决此类树上路径操作问题的经典方法，
// 时间复杂度已经达到了理论下限，是最优解之一。
//
// 相关题目链接：
// 1. 洛谷P3384 【模板】重链剖分/树链剖分（本题）：https://www.luogu.com.cn/problem/P3384
// 2. 洛谷P2590 [ZJOI2008]树的统计：https://www.luogu.com.cn/problem/P2590
// 3. 洛谷P3178 [HAOI2015]树上操作：https://www.luogu.com.cn/problem/P3178
// 4. 洛谷P2146 [NOI2015]软件包管理器：https://www.luogu.com.cn/problem/P2146
// 5. Codeforces 916E Jamie and Tree：https://codeforces.com/problemset/problem/916/E
//
// Java实现参考：Code01_HLD1.java（当前文件）
// Python实现参考：Code01_HLD1.py

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code01_HLD1 {

	public static int MAXN = 100001;
	public static int n, m, root, MOD;
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

	public static long[] sum = new long[MAXN << 2];
	public static long[] addTag = new long[MAXN << 2];

	public static void addEdge(int u, int v) {
		next[++cntg] = head[u];
		to[cntg] = v;
		head[u] = cntg;
	}

	// 递归版，C++可以通过，java会爆栈
	// 来到节点u，节点u树上的父节点是f
	// dfs1的过程去设置 fa dep siz son
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
	// 来到节点u，节点u所在重链的头节点是t
	// dfs2的过程去设置 top dfn seg
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
		push(root, 0, -1);
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
		push(root, root, -1);
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
		sum[i] = (sum[i << 1] + sum[i << 1 | 1]) % MOD;
	}

	public static void lazy(int i, long v, int n) {
		sum[i] = (sum[i] + v * n) % MOD;
		addTag[i] = (addTag[i] + v) % MOD;
	}

	public static void down(int i, int ln, int rn) {
		if (addTag[i] != 0) {
			lazy(i << 1, addTag[i], ln);
			lazy(i << 1 | 1, addTag[i], rn);
			addTag[i] = 0;
		}
	}

	public static void build(int l, int r, int i) {
		if (l == r) {
			sum[i] = arr[seg[l]] % MOD;
		} else {
			int mid = (l + r) / 2;
			build(l, mid, i << 1);
			build(mid + 1, r, i << 1 | 1);
			up(i);
		}
	}

	public static void add(int jobl, int jobr, int jobv, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			lazy(i, jobv, r - l + 1);
		} else {
			int mid = (l + r) / 2;
			down(i, mid - l + 1, r - mid);
			if (jobl <= mid) {
				add(jobl, jobr, jobv, l, mid, i << 1);
			}
			if (jobr > mid) {
				add(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
			}
			up(i);
		}
	}

	public static long query(int jobl, int jobr, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			return sum[i];
		}
		int mid = (l + r) / 2;
		down(i, mid - l + 1, r - mid);
		long ans = 0;
		if (jobl <= mid) {
			ans = (ans + query(jobl, jobr, l, mid, i << 1)) % MOD;
		}
		if (jobr > mid) {
			ans = (ans + query(jobl, jobr, mid + 1, r, i << 1 | 1)) % MOD;
		}
		return ans;
	}

	// 从x到y的路径上，所有节点的值增加v
	public static void pathAdd(int x, int y, int v) {
		while (top[x] != top[y]) {
			if (dep[top[x]] <= dep[top[y]]) {
				add(dfn[top[y]], dfn[y], v, 1, n, 1);
				y = fa[top[y]];
			} else {
				add(dfn[top[x]], dfn[x], v, 1, n, 1);
				x = fa[top[x]];
			}
		}
		add(Math.min(dfn[x], dfn[y]), Math.max(dfn[x], dfn[y]), v, 1, n, 1);
	}

	// x的子树上，所有节点的值增加v
	public static void subtreeAdd(int x, int v) {
		add(dfn[x], dfn[x] + siz[x] - 1, v, 1, n, 1);
	}

	// 从x到y的路径上，查询所有节点的累加和
	public static long pathSum(int x, int y) {
		long ans = 0;
		while (top[x] != top[y]) {
			if (dep[top[x]] <= dep[top[y]]) {
				ans = (ans + query(dfn[top[y]], dfn[y], 1, n, 1)) % MOD;
				y = fa[top[y]];
			} else {
				ans = (ans + query(dfn[top[x]], dfn[x], 1, n, 1)) % MOD;
				x = fa[top[x]];
			}
		}
		ans = (ans + query(Math.min(dfn[x], dfn[y]), Math.max(dfn[x], dfn[y]), 1, n, 1)) % MOD;
		return ans;
	}

	// x的子树上，查询所有节点的累加和
	public static long subtreeSum(int x) {
		return query(dfn[x], dfn[x] + siz[x] - 1, 1, n, 1);
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		n = (int) in.nval;
		in.nextToken();
		m = (int) in.nval;
		in.nextToken();
		root = (int) in.nval;
		in.nextToken();
		MOD = (int) in.nval;
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			arr[i] = (int) in.nval;
		}
		for (int i = 1, u, v; i < n; i++) {
			in.nextToken();
			u = (int) in.nval;
			in.nextToken();
			v = (int) in.nval;
			addEdge(u, v);
			addEdge(v, u);
		}
		dfs3(); // dfs3() 等同于 dfs1(root, 0)，调用迭代版防止爆栈
		dfs4(); // dfs4() 等同于 dfs2(root, root)，调用迭代版防止爆栈
		build(1, n, 1);
		for (int i = 1, op, x, y, v; i <= m; i++) {
			in.nextToken();
			op = (int) in.nval;
			if (op == 1) {
				in.nextToken();
				x = (int) in.nval;
				in.nextToken();
				y = (int) in.nval;
				in.nextToken();
				v = (int) in.nval;
				pathAdd(x, y, v);
			} else if (op == 2) {
				in.nextToken();
				x = (int) in.nval;
				in.nextToken();
				y = (int) in.nval;
				out.println(pathSum(x, y));
			} else if (op == 3) {
				in.nextToken();
				x = (int) in.nval;
				in.nextToken();
				v = (int) in.nval;
				subtreeAdd(x, v);
			} else {
				in.nextToken();
				x = (int) in.nval;
				out.println(subtreeSum(x));
			}
		}
		out.flush();
		out.close();
		br.close();
	}
}
package class161;

// 重链剖分解决LCA查询，java版
// 题目来源：洛谷P3379 【模板】最近公共祖先（LCA）
// 题目链接：https://www.luogu.com.cn/problem/P3379
//
// 题目描述：
// 如题，给定一棵有根多叉树，请求出指定两个点直接最近的公共祖先。
// 一共有n个节点，给定n-1条边，节点连成一棵树，给定头节点编号root
// 一共有m条查询，每条查询给定a和b，打印a和b的最低公共祖先
// 请用树链剖分的方式实现
// 1 <= n、m <= 5 * 10^5
//
// 解题思路：
// 使用树链剖分解决LCA问题
// 1. 树链剖分：通过两次DFS将树划分为多条重链
// 2. LCA查询：利用树链剖分的性质，当两个节点不在同一重链上时，
//    将深度较大的节点跳到其重链顶端的父节点，直到两个节点在同一重链上
// 3. 迭代实现：为避免递归爆栈，使用迭代方式实现DFS
//
// 算法步骤：
// 1. 构建树结构，进行树链剖分（dfs1计算重儿子，dfs2计算重链顶端）
// 2. 对于LCA查询：
//    - 当两个节点不在同一重链上时，将深度较大的节点跳到其重链顶端的父节点
//    - 重复此过程直到两个节点在同一重链上
//    - 此时深度较小的节点即为LCA
//
// 时间复杂度分析：
// - 树链剖分预处理：O(n)
// - 每次LCA查询：O(log n)
// - 总体复杂度：O(n + m log n)
// 空间复杂度：O(n)
//
// 是否为最优解：
// 树链剖分解决LCA问题是一种高效的解决方案，
// 时间复杂度已经达到了理论下限，是最优解之一。
//
// 相关题目链接：
// 1. 洛谷P3379 【模板】最近公共祖先（LCA）（本题）：https://www.luogu.com.cn/problem/P3379
// 2. 洛谷P3384 【模板】重链剖分/树链剖分：https://www.luogu.com.cn/problem/P3384
// 3. LeetCode 1483. 树节点的第 K 个祖先：https://leetcode.cn/problems/kth-ancestor-of-a-tree-node/
// 4. Codeforces 916E Jamie and Tree：https://codeforces.com/problemset/problem/916/E
//
// Java实现参考：Code02_LCA1.java（当前文件）
// Python实现参考：Code02_LCA1.py
// C++实现参考：Code02_LCA2.java（注释部分）

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code02_LCA1 {

	public static int MAXN = 500001;
	public static int n, m, root;

	public static int[] head = new int[MAXN];
	public static int[] next = new int[MAXN << 1];
	public static int[] to = new int[MAXN << 1];
	public static int cnt = 0;

	public static int[] fa = new int[MAXN];
	public static int[] dep = new int[MAXN];
	public static int[] siz = new int[MAXN];
	public static int[] son = new int[MAXN];
	public static int[] top = new int[MAXN];

	public static void addEdge(int u, int v) {
		next[++cnt] = head[u];
		to[cnt] = v;
		head[u] = cnt;
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

	public static int lca(int a, int b) {
		while (top[a] != top[b]) {
			if (dep[top[a]] <= dep[top[b]]) {
				b = fa[top[b]];
			} else {
				a = fa[top[a]];
			}
		}
		return dep[a] <= dep[b] ? a : b;
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
		for (int i = 1, a, b; i <= m; i++) {
			in.nextToken();
			a = (int) in.nval;
			in.nextToken();
			b = (int) in.nval;
			out.println(lca(a, b));
		}
		out.flush();
		out.close();
		br.close();
	}

}
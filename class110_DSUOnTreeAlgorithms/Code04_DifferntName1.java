package class163;

// 不同名字数量，java版
// 题目来源: Codeforces 246E / 洛谷 CF246E
// 题目链接: https://codeforces.com/problemset/problem/246/E
// 题目链接: https://www.luogu.com.cn/problem/CF246E
// 
// 题目大意:
// 一共有n个节点，编号1~n，给定每个节点的名字和父亲节点编号
// 名字是string类型，如果父亲节点编号为0，说明当前节点是某棵树的头节点
// 注意，n个节点组成的是森林结构，可能有若干棵树
// 一共有m条查询，每条查询 x k，含义如下
// 以x为头的子树上，到x距离为k的所有节点中，打印不同名字的数量
// 1 <= n、m <= 10^5
//
// 解题思路:
// 使用DSU on Tree(树上启发式合并)算法
// 1. 建树，处理出每个节点的子树大小、重儿子等信息
// 2. 对每个节点，维护其子树中每个深度上的不同名字集合
// 3. 使用树上启发式合并优化，保证每个节点最多被访问O(logn)次
// 4. 离线处理所有查询
//
// 时间复杂度: O(n log n)
// 空间复杂度: O(n)
//
// 算法详解:
// DSU on Tree是一种优化的暴力算法，通过重链剖分的思想，将轻重儿子的信息合并过程进行优化
// 使得每个节点最多被访问O(log n)次，从而将时间复杂度从O(n²)优化到O(n log n)
//
// 核心思想:
// 1. 重链剖分预处理：计算每个节点的子树大小，确定重儿子
// 2. 启发式合并处理：
//    - 先处理轻儿子的信息，然后清除贡献
//    - 再处理重儿子的信息并保留贡献
//    - 最后重新计算轻儿子的贡献
// 3. 通过这种方式，保证每个节点最多被访问O(log n)次
//
// 深度处理:
// 1. 维护每个深度上的名字集合(depSet)
// 2. 通过相对深度计算查询结果
// 3. 使用HashSet快速统计不同名字数量
//
// 工程化实现要点:
// 1. 边界处理：注意空树、单节点树等特殊情况
// 2. 内存优化：合理使用全局数组，避免重复分配内存
// 3. 常数优化：使用位运算、减少函数调用等优化常数
// 4. 可扩展性：设计通用模板，便于适应不同类型的查询问题
//
// 测试链接 : https://www.luogu.com.cn/problem/CF246E
// 测试链接 : https://codeforces.com/problemset/problem/246/E
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

public class Code04_DifferntName1 {

	public static int MAXN = 100001;
	public static int n, m;

	public static HashMap<String, Integer> nameId = new HashMap<>();
	public static boolean[] root = new boolean[MAXN];
	public static int[] id = new int[MAXN];

	// 链式前向星
	public static int[] headg = new int[MAXN];
	public static int[] nextg = new int[MAXN];
	public static int[] tog = new int[MAXN];
	public static int cntg;

	// 问题列表
	public static int[] headq = new int[MAXN];
	public static int[] nextq = new int[MAXN];
	public static int[] ansiq = new int[MAXN];
	public static int[] kq = new int[MAXN];
	public static int cntq;

	// 树链剖分
	public static int[] fa = new int[MAXN];
	public static int[] siz = new int[MAXN];
	public static int[] dep = new int[MAXN];
	public static int[] son = new int[MAXN];

	// 树上启发式合并
	public static ArrayList<HashSet<Integer>> depSet = new ArrayList<>();
	public static int[] ans = new int[MAXN];

	public static int getNameId(String name) {
		if (nameId.containsKey(name)) {
			return nameId.get(name);
		}
		nameId.put(name, nameId.size() + 1);
		return nameId.size();
	}

	public static void addId(int deep, int id) {
		depSet.get(deep).add(id);
	}

	public static void removeId(int deep, int id) {
		depSet.get(deep).remove(id);
	}

	public static int sizeOfDeep(int deep) {
		if (deep > n) {
			return 0;
		}
		return depSet.get(deep).size();
	}

	public static void addEdge(int u, int v) {
		nextg[++cntg] = headg[u];
		tog[cntg] = v;
		headg[u] = cntg;
	}

	public static void addQuestion(int u, int ansi, int k) {
		nextq[++cntq] = headq[u];
		ansiq[cntq] = ansi;
		kq[cntq] = k;
		headq[u] = cntq;
	}

	public static void dfs1(int u, int f) {
		fa[u] = f;
		siz[u] = 1;
		dep[u] = dep[f] + 1;
		for (int e = headg[u]; e > 0; e = nextg[e]) {
			dfs1(tog[e], u);
		}
		for (int e = headg[u], v; e > 0; e = nextg[e]) {
			v = tog[e];
			siz[u] += siz[v];
			if (son[u] == 0 || siz[son[u]] < siz[v]) {
				son[u] = v;
			}
		}
	}

	public static void effect(int u) {
		addId(dep[u], id[u]);
		for (int e = headg[u]; e > 0; e = nextg[e]) {
			effect(tog[e]);
		}
	}

	public static void cancel(int u) {
		removeId(dep[u], id[u]);
		for (int e = headg[u]; e > 0; e = nextg[e]) {
			cancel(tog[e]);
		}
	}

	public static void dfs2(int u, int keep) {
		for (int e = headg[u], v; e > 0; e = nextg[e]) {
			v = tog[e];
			if (v != son[u]) {
				dfs2(v, 0);
			}
		}
		if (son[u] != 0) {
			dfs2(son[u], 1);
		}
		addId(dep[u], id[u]);
		for (int e = headg[u], v; e > 0; e = nextg[e]) {
			v = tog[e];
			if (v != son[u]) {
				effect(v);
			}
		}
		for (int i = headq[u]; i > 0; i = nextq[i]) {
			ans[ansiq[i]] = sizeOfDeep(dep[u] + kq[i]);
		}
		if (keep == 0) {
			cancel(u);
		}
	}

	public static void main(String[] args) throws IOException {
		Kattio io = new Kattio();
		n = io.nextInt();
		String name;
		int father;
		for (int i = 1; i <= n; i++) {
			name = io.next();
			father = io.nextInt();
			id[i] = getNameId(name);
			if (father == 0) {
				root[i] = true;
			} else {
				addEdge(father, i);
			}
		}
		for (int i = 1; i <= n; i++) {
			if (root[i]) {
				dfs1(i, 0);
			}
		}
		for (int i = 0; i <= n; i++) {
			depSet.add(new HashSet<>());
		}
		m = io.nextInt();
		for (int i = 1, node, k; i <= m; i++) {
			node = io.nextInt();
			k = io.nextInt();
			addQuestion(node, i, k);
		}
		for (int i = 1; i <= n; i++) {
			if (root[i]) {
				dfs2(i, 0);
			}
		}
		for (int i = 1; i <= m; i++) {
			io.println(ans[i]);
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
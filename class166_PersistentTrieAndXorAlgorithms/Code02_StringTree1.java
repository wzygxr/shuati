package class159;

// 字符串树，java版
// 一共有n个节点，n-1条边，组成一棵树，每条边的边权为字符串
// 一共有m条查询，每条查询的格式为
// u v s : 查询节点u到节点v的路径中，有多少边的字符串以字符串s作为前缀
// 1 <= n、m <= 10^5
// 所有字符串长度不超过10，并且都由字符a~z组成
// 测试链接 : https://www.luogu.com.cn/problem/P6088
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

// 补充题目1: 字符串前缀查询
// 给定一个字符串数组和多个查询，每个查询包含一个字符串，要求找出数组中以该字符串为前缀的字符串数量
// 可以使用Trie树解决
// 相关题目:
// - https://leetcode.cn/problems/longest-common-prefix/
// - https://leetcode.cn/problems/implement-trie-prefix-tree/
// - https://www.luogu.com.cn/problem/P2580

// 补充题目2: 树上路径字符串查询
// 在树结构中，每条边有权值（字符串），查询两点间路径上满足特定条件的边数量
// 相关题目:
// - https://www.luogu.com.cn/problem/P6088
// - https://codeforces.com/problemset/problem/1076/E
// - https://www.hdu.edu.cn/problem/6394

// 补充题目3: LCA应用 - 树上路径查询
// 利用最近公共祖先(LCA)算法解决树上路径查询问题
// 相关题目:
// - https://www.luogu.com.cn/problem/P3379
// - https://codeforces.com/problemset/problem/1304/E
// - https://www.spoj.com/problems/LCA/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Code02_StringTree1 {

	// 最大节点数
	public static int MAXN = 100001;

	// Trie树最大节点数
	public static int MAXT = 1000001;

	// 倍增数组最大高度
	public static int MAXH = 20;

	// 节点数和查询数
	public static int n, m;

	// 链式前向星需要的数组
	// head[i]表示节点i的第一条边的编号
	public static int[] head = new int[MAXN];
	// next[i]表示第i条边的下一条边的编号
	public static int[] next = new int[MAXN << 1];
	// to[i]表示第i条边指向的节点
	public static int[] to = new int[MAXN << 1];
	// weight[i]表示第i条边的权值（字符串）
	public static String[] weight = new String[MAXN << 1];
	// 边的计数器
	public static int cntg = 0;

	// 可持久化前缀树需要的数组
	// root[i]表示节点i对应的可持久化Trie树根节点编号
	public static int[] root = new int[MAXN];
	// tree[i][j]表示Trie树节点i的第j个子节点编号（1-26对应a-z，0表示空）
	public static int[][] tree = new int[MAXT][27];
	// pass[i]表示经过Trie树节点i的字符串数量
	public static int[] pass = new int[MAXT];
	// Trie树节点计数器
	public static int cntt = 0;

	// 树上倍增和LCA需要的数组
	// deep[i]表示节点i的深度
	public static int[] deep = new int[MAXN];
	// stjump[i][j]表示节点i向上跳2^j步到达的节点
	public static int[][] stjump = new int[MAXN][MAXH];

	/**
	 * 添加一条无向边到链式前向星
	 * @param u 起点
	 * @param v 终点
	 * @param w 边权（字符串）
	 */
	public static void addEdge(int u, int v, String w) {
		// 创建新边
		next[++cntg] = head[u];
		to[cntg] = v;
		weight[cntg] = w;
		head[u] = cntg;
	}

	/**
	 * 将字符转换为数字（a->1, b->2, ..., z->26）
	 * @param cha 字符
	 * @return 对应的数字
	 */
	public static int num(char cha) {
		return cha - 'a' + 1;
	}

	/**
	 * 克隆Trie树节点
	 * @param i 要克隆的节点编号
	 * @return 新节点编号
	 */
	public static int clone(int i) {
		// 创建新节点
		int rt = ++cntt;
		// 复制子节点信息
		for (int cha = 1; cha <= 26; cha++) {
			tree[rt][cha] = tree[i][cha];
		}
		// 复制经过该节点的字符串数量
		pass[rt] = pass[i];
		return rt;
	}

	/**
	 * 在可持久化Trie树中插入字符串
	 * @param str 要插入的字符串
	 * @param i 前一个版本的根节点编号
	 * @return 新版本的根节点编号
	 */
	public static int insert(String str, int i) {
		// 克隆根节点
		int rt = clone(i);
		// 经过根节点的字符串数量加1
		pass[rt]++;
		// 逐字符插入字符串
		for (int j = 0, path, pre = rt, cur; j < str.length(); j++, pre = cur) {
			// 获取当前字符对应的数字
			path = num(str.charAt(j));
			// 获取前一个版本中对应子节点
			i = tree[i][path];
			// 克隆子节点
			cur = clone(i);
			// 经过该节点的字符串数量加1
			pass[cur]++;
			// 连接父子节点
			tree[pre][path] = cur;
		}
		return rt;
	}

	/**
	 * 在Trie树中查询以指定字符串为前缀的字符串数量
	 * @param str 查询的前缀字符串
	 * @param i Trie树根节点编号
	 * @return 匹配的字符串数量
	 */
	public static int query(String str, int i) {
		// 逐字符匹配前缀
		for (int j = 0, path; j < str.length(); j++) {
			// 获取当前字符对应的数字
			path = num(str.charAt(j));
			// 移动到子节点
			i = tree[i][path];
			// 如果节点不存在，返回0
			if (i == 0) {
				return 0;
			}
		}
		// 返回经过该节点的字符串数量
		return pass[i];
	}

	// 递归版DFS，C++可以通过，java无法通过，递归会爆栈
	// public static void dfs1(int u, int fa, String path) {
	//     root[u] = insert(path, root[fa]);
	//     deep[u] = deep[fa] + 1;
	//     stjump[u][0] = fa;
	//     for (int p = 1; p < MAXH; p++) {
	//         stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
	//     }
	//     for (int e = head[u]; e > 0; e = next[e]) {
	//         if (to[e] != fa) {
	//             dfs1(to[e], u, weight[e]);
	//         }
	//     }
	// }

	// 迭代版，都可以通过
	// 讲解118，详解了从递归版改迭代版
	// 使用栈模拟递归过程的数组
	public static int[] us = new int[MAXN];  // 节点编号
	public static int[] fs = new int[MAXN];  // 父节点编号
	public static int[] es = new int[MAXN];  // 边的编号
	public static String[] ps = new String[MAXN];  // 路径字符串
	public static int stackSize;  // 栈大小
	// 栈顶元素
	public static int u;
	public static int f;
	public static int e;
	public static String p;

	/**
	 * 将元素压入栈
	 * @param u 节点编号
	 * @param f 父节点编号
	 * @param e 边的编号
	 * @param p 路径字符串
	 */
	public static void push(int u, int f, int e, String p) {
		us[stackSize] = u;
		fs[stackSize] = f;
		es[stackSize] = e;
		ps[stackSize] = p;
		stackSize++;
	}

	/**
	 * 弹出栈顶元素
	 */
	public static void pop() {
		--stackSize;
		u = us[stackSize];
		f = fs[stackSize];
		e = es[stackSize];
		p = ps[stackSize];
	}

	/**
	 * DFS遍历树，构建可持久化Trie树和LCA所需信息（迭代版）
	 */
	public static void dfs2() {
		stackSize = 0;
		// 将根节点压入栈
		push(1, 0, -1, "");
		while (stackSize > 0) {
			// 弹出栈顶元素
			pop();
			// 如果是第一次访问该节点
			if (e == -1) {
				// 在父节点的Trie树基础上插入路径字符串
				root[u] = insert(p, root[f]);
				// 计算节点深度
				deep[u] = deep[f] + 1;
				// 设置直接父节点
				stjump[u][0] = f;
				// 倍增计算祖先节点
				for (int p = 1; p < MAXH; p++) {
					stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
				}
				// 获取第一条边
				e = head[u];
			} else {
				// 获取下一条边
				e = next[e];
			}
			// 如果还有边未处理
			if (e != 0) {
				// 将当前状态重新压入栈
				push(u, f, e, p);
				// 如果不是父节点，则将子节点压入栈
				if (to[e] != f) {
					push(to[e], u, -1, weight[e]);
				}
			}
		}
	}

	/**
	 * 计算两个节点的最近公共祖先(LCA)
	 * @param a 节点a
	 * @param b 节点b
	 * @return 最近公共祖先节点编号
	 */
	public static int lca(int a, int b) {
		// 确保a节点深度不小于b节点
		if (deep[a] < deep[b]) {
			int tmp = a;
			a = b;
			b = tmp;
		}
		// 将a节点向上跳到与b节点同一深度
		for (int p = MAXH - 1; p >= 0; p--) {
			if (deep[stjump[a][p]] >= deep[b]) {
				a = stjump[a][p];
			}
		}
		// 如果a和b在同一节点，直接返回
		if (a == b) {
			return a;
		}
		// 同时向上跳，直到找到最近公共祖先
		for (int p = MAXH - 1; p >= 0; p--) {
			if (stjump[a][p] != stjump[b][p]) {
				a = stjump[a][p];
				b = stjump[b][p];
			}
		}
		// 返回最近公共祖先的父节点
		return stjump[a][0];
	}

	/**
	 * 计算树上路径中以指定字符串为前缀的边数量
	 * 利用容斥原理：u到v路径上的边 = (根到u的路径) + (根到v的路径) - 2*(根到lca的路径)
	 * @param u 起点
	 * @param v 终点
	 * @param s 查询的前缀字符串
	 * @return 匹配的边数量
	 */
	public static int compute(int u, int v, String s) {
		return query(s, root[u]) + query(s, root[v]) - 2 * query(s, root[lca(u, v)]);
	}

	public static void main(String[] args) {
		Kattio io = new Kattio();
		n = io.nextInt();
		int u, v;
		String s;
		// 读入树的边信息
		for (int i = 1; i < n; i++) {
			u = io.nextInt();
			v = io.nextInt();
			s = io.next();
			// 添加无向边
			addEdge(u, v, s);
			addEdge(v, u, s);
		}
		// DFS遍历树（使用迭代版防止爆栈）
		dfs2();
		m = io.nextInt();
		// 处理查询
		for (int i = 1; i <= m; i++) {
			u = io.nextInt();
			v = io.nextInt();
			s = io.next();
			// 输出查询结果
			io.println(compute(u, v, s));
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
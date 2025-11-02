package class162;

// 边权转化为点权的模版题，java版
// 一共有n个节点，给定n-1条边，节点连成一棵树，初始时所有边的权值为0
// 一共有m条操作，每条操作是如下2种类型中的一种
// 操作 P x y : x到y的路径上，每条边的权值增加1
// 操作 Q x y : x和y保证是直接连接的，查询他们之间的边权
// 1 <= n、m <= 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P3038
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

/*
 * 问题分析:
 * 这是一道树链剖分的经典题目，主要涉及:
 * 1. 边权转点权的技巧
 * 2. 树链剖分的基本操作
 * 3. 线段树区间更新和单点查询
 *
 * 解题思路:
 * 1. 边权转点权: 对于每条边(u,v)，将其权值记录在深度较大的节点上
 * 2. 树链剖分: 对树进行重链剖分，将树上路径操作转化为区间操作
 * 3. 线段树: 使用线段树维护区间加法和单点查询操作
 *
 * 时间复杂度: O(m * log²n)
 * 空间复杂度: O(n)
 *
 * 算法详解:
 * 1. 树链剖分:
 *    - 第一次DFS(dfs1/dfs3): 计算每个节点的父节点、深度、子树大小，并确定重儿子
 *    - 第二次DFS(dfs2/dfs4): 进行重链剖分，为每个节点分配dfs序和链顶节点
 * 2. 线段树操作:
 *    - 区间加法: 对路径上的所有节点增加权值
 *    - 单点查询: 查询特定节点的权值
 * 3. 路径操作:
 *    - pathAdd: 在x到y的路径上增加权值
 *    - edgeQuery: 查询x和y之间边的权值
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Code01_GrassPlanting1 {

	public static int MAXN = 100001;
	public static int n, m;

	// 链式前向星 - 用于存储树的邻接关系
	// head[u]: 节点u的第一条边的索引
	// next[i]: 第i条边的下一条边索引
	// to[i]: 第i条边指向的节点
	// cntg: 边的计数器
	public static int[] head = new int[MAXN];
	public static int[] next = new int[MAXN << 1];
	public static int[] to = new int[MAXN << 1];
	public static int cntg = 0;

	// 重链剖分相关数组
	// fa[u]: 节点u的父节点
	// dep[u]: 节点u的深度
	// siz[u]: 以u为根的子树大小
	// son[u]: 节点u的重儿子（子树大小最大的子节点）
	// top[u]: 节点u所在链的顶部节点
	// dfn[u]: 节点u的dfs序号
	// cntd: dfs序计数器
	public static int[] fa = new int[MAXN];
	public static int[] dep = new int[MAXN];
	public static int[] siz = new int[MAXN];
	public static int[] son = new int[MAXN];
	public static int[] top = new int[MAXN];
	public static int[] dfn = new int[MAXN];
	public static int cntd = 0;

	// 线段树数组
	// sum[i]: 线段树节点i维护的区间和
	// addTag[i]: 线段树节点i的懒惰标记（区间加法标记）
	public static int[] sum = new int[MAXN << 2];
	public static int[] addTag = new int[MAXN << 2];

	// 添加边到链式前向星结构中
	// u: 起点, v: 终点
	public static void addEdge(int u, int v) {
		next[++cntg] = head[u];
		to[cntg] = v;
		head[u] = cntg;
	}

	// 第一次DFS - 递归版本
	// 计算每个节点的父节点、深度、子树大小，并确定重儿子
	// u: 当前节点, f: 父节点
	public static void dfs1(int u, int f) {
		fa[u] = f;
		dep[u] = dep[f] + 1;
		siz[u] = 1;
		// 遍历u的所有子节点
		for (int e = head[u], v; e > 0; e = next[e]) {
			v = to[e];
			if (v != f) {
				dfs1(v, u);
				siz[u] += siz[v]; // 累加子树大小
				// 更新重儿子：选择子树最大的子节点
				if (son[u] == 0 || siz[son[u]] < siz[v]) {
					son[u] = v;
				}
			}
		}
	}

	// 第二次DFS - 递归版本
	// 进行重链剖分，为每个节点分配dfs序和链顶节点
	// u: 当前节点, t: 当前链的顶部节点
	public static void dfs2(int u, int t) {
		top[u] = t; // 设置链顶
		dfn[u] = ++cntd; // 分配dfs序
		if (son[u] == 0) { // 如果没有重儿子，说明是叶子节点
			return;
		}
		// 优先处理重儿子，保持重链的连续性
		dfs2(son[u], t);
		// 处理所有轻儿子，每个轻儿子作为新链的顶部
		for (int e = head[u], v; e > 0; e = next[e]) {
			v = to[e];
			if (v != fa[u] && v != son[u]) {
				dfs2(v, v);
			}
		}
	}

	// 栈结构用于迭代版DFS
	// fse[stacksize][0]: 当前节点
	// fse[stacksize][1]: 父节点
	// fse[stacksize][2]: 边的索引
	public static int[][] fse = new int[MAXN][3];

	public static int stacksize, first, second, edge;

	// 将节点信息压入栈中
	public static void push(int fir, int sec, int edg) {
		fse[stacksize][0] = fir;
		fse[stacksize][1] = sec;
		fse[stacksize][2] = edg;
		stacksize++;
	}

	// 从栈中弹出节点信息
	public static void pop() {
		--stacksize;
		first = fse[stacksize][0];
		second = fse[stacksize][1];
		edge = fse[stacksize][2];
	}

	// dfs1的迭代版 - 避免递归深度过大导致栈溢出
	// 通过显式栈模拟递归过程
	public static void dfs3() {
		stacksize = 0;
		push(1, 0, -1); // 从根节点1开始，父节点为0，边索引为-1表示初次访问
		while (stacksize > 0) {
			pop();
			if (edge == -1) { // 初次访问节点
				fa[first] = second;
				dep[first] = dep[second] + 1;
				siz[first] = 1;
				edge = head[first]; // 获取第一条边
			} else {
				edge = next[edge]; // 获取下一条边
			}
			if (edge != 0) { // 如果还有边未处理
				push(first, second, edge);
				if (to[edge] != second) { // 如果不是回到父节点
					push(to[edge], first, -1); // 将子节点压入栈中
				}
			} else { // 所有子节点已处理完毕，计算子树信息
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

	// dfs2的迭代版 - 避免递归深度过大导致栈溢出
	public static void dfs4() {
		stacksize = 0;
		push(1, 1, -1); // 从根节点1开始，链顶为1
		while (stacksize > 0) {
			pop();
			if (edge == -1) { // 初次访问节点
				top[first] = second;
				dfn[first] = ++cntd;
				if (son[first] == 0) { // 如果没有重儿子
					continue;
				}
				push(first, second, -2); // 标记需要处理轻儿子
				push(son[first], second, -1); // 优先处理重儿子
				continue;
			} else if (edge == -2) { // 需要处理轻儿子
				edge = head[first];
			} else {
				edge = next[edge]; // 获取下一条边
			}
			if (edge != 0) {
				push(first, second, edge);
				// 处理轻儿子，每个轻儿子作为新链的顶部
				if (to[edge] != fa[first] && to[edge] != son[first]) {
					push(to[edge], to[edge], -1);
				}
			}
		}
	}

	// 线段树向上更新 - 合并子节点信息
	// i: 当前节点索引
	public static void up(int i) {
		sum[i] = sum[i << 1] + sum[i << 1 | 1];
	}

	// 线段树懒惰标记 - 设置区间加法标记
	// i: 当前节点索引, v: 加法值, n: 区间长度
	public static void lazy(int i, int v, int n) {
		sum[i] += v * n;
		addTag[i] += v;
	}

	// 线段树下传懒惰标记 - 将标记传递给子节点
	// i: 当前节点索引, ln: 左子区间长度, rn: 右子区间长度
	public static void down(int i, int ln, int rn) {
		if (addTag[i] != 0) {
			lazy(i << 1, addTag[i], ln);
			lazy(i << 1 | 1, addTag[i], rn);
			addTag[i] = 0;
		}
	}

	// 线段树区间增加操作
	// jobl: 操作区间左端点, jobr: 操作区间右端点, jobv: 增加的值
	// l: 当前区间左端点, r: 当前区间右端点, i: 当前节点索引
	public static void add(int jobl, int jobr, int jobv, int l, int r, int i) {
		if (jobl <= l && r <= jobr) { // 当前区间完全包含在操作区间内
			lazy(i, jobv, r - l + 1);
		} else {
			int mid = (l + r) / 2;
			down(i, mid - l + 1, r - mid); // 下传懒惰标记
			if (jobl <= mid) { // 递归处理左子树
				add(jobl, jobr, jobv, l, mid, i << 1);
			}
			if (jobr > mid) { // 递归处理右子树
				add(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
			}
			up(i); // 向上更新
		}
	}

	// 线段树单点查询操作
	// jobi: 查询位置
	// l: 当前区间左端点, r: 当前区间右端点, i: 当前节点索引
	public static int query(int jobi, int l, int r, int i) {
		if (l == r) { // 到达叶子节点
			return sum[i];
		}
		int mid = (l + r) / 2;
		down(i, mid - l + 1, r - mid); // 下传懒惰标记
		if (jobi <= mid) { // 递归查询左子树
			return query(jobi, l, mid, i << 1);
		} else { // 递归查询右子树
			return query(jobi, mid + 1, r, i << 1 | 1);
		}
	}

	// x到y的路径上，每条边的边权增加v
	// 通过树链剖分将路径操作转化为区间操作
	public static void pathAdd(int x, int y, int v) {
		// 当两个节点不在同一链上时，不断跳转到链顶
		while (top[x] != top[y]) {
			// 选择深度较大的链顶进行操作
			if (dep[top[x]] <= dep[top[y]]) {
				// 对y到其链顶的区间进行操作
				add(dfn[top[y]], dfn[y], v, 1, n, 1);
				y = fa[top[y]]; // 跳转到链顶的父节点
			} else {
				// 对x到其链顶的区间进行操作
				add(dfn[top[x]], dfn[x], v, 1, n, 1);
				x = fa[top[x]]; // 跳转到链顶的父节点
			}
		}
		// 两个节点在同一链上时，对区间进行操作
		// 注意：x和y的最低公共祖先，点权不增加！
		add(Math.min(dfn[x], dfn[y]) + 1, Math.max(dfn[x], dfn[y]), v, 1, n, 1);
	}

	// 返回x和y之间这条边的边权
	// 通过查询深度较大的节点的点权来获取边权
	public static int edgeQuery(int x, int y) {
		int down = Math.max(dfn[x], dfn[y]); // 深度较大的节点
		return query(down, 1, n, 1);
	}

	public static void main(String[] args) {
		Kattio io = new Kattio();
		n = io.nextInt();
		m = io.nextInt();
		// 读入所有边，构建链式前向星
		for (int i = 1, u, v; i < n; i++) {
			u = io.nextInt();
			v = io.nextInt();
			addEdge(u, v);
			addEdge(v, u);
		}
		dfs3(); // 迭代版第一次DFS
		dfs4(); // 迭代版第二次DFS
		String op;
		// 处理所有操作
		for (int i = 1, x, y; i <= m; i++) {
			op = io.next();
			x = io.nextInt();
			y = io.nextInt();
			if (op.equals("P")) {
				pathAdd(x, y, 1);
			} else {
				io.println(edgeQuery(x, y));
			}
		}
		io.flush();
		io.close();
	}

	// 读写工具类 - 提供高效的输入输出
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
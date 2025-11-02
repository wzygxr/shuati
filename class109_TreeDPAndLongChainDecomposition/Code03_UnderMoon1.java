package class162;

// 月下毛景树，java版
// 一共有n个节点，节点编号从1到n，所有节点连成一棵树
// 给定n-1条边，边的编号从1到n-1，每条边给定初始边权
// 会进行若干次操作，每条操作的类型是如下4种类型中的一种
// 操作 Change x v  : 第x条边的边权改成v
// 操作 Cover x y v : x号点到y号点的路径上，所有边权改成v
// 操作 Add x y v   : x号点到y号点的路径上，所有边权增加v
// 操作 Max x y     : x号点到y号点的路径上，打印最大的边权
// 1 <= n <= 10^5
// 任何时候的边权 <= 10^9
// 测试链接 : https://www.luogu.com.cn/problem/P4315
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

/*
 * 问题分析:
 * 这是一道树链剖分的高阶题目，主要涉及:
 * 1. 边权转点权的技巧
 * 2. 树链剖分的基本操作
 * 3. 线段树区间更新、区间查询（最大值）
 * 4. 区间加法和区间覆盖两种操作的结合
 *
 * 解题思路:
 * 1. 边权转点权: 对于每条边(u,v)，将其权值记录在深度较大的节点上
 * 2. 树链剖分: 对树进行重链剖分，将树上路径操作转化为区间操作
 * 3. 线段树: 使用线段树维护区间最大值，同时支持区间加法和区间覆盖
 *    注意：区间覆盖操作会取消之前的区间加法操作
 *
 * 时间复杂度: O(m * log²n)
 * 空间复杂度: O(n)
 *
 * 算法详解:
 * 1. 树链剖分:
 *    - 第一次DFS(dfs1/dfs3): 计算每个节点的父节点、深度、子树大小，并确定重儿子
 *    - 第二次DFS(dfs2/dfs4): 进行重链剖分，为每个节点分配dfs序和链顶节点
 * 2. 线段树操作:
 *    - 区间更新: 支持区间加法和区间覆盖两种操作
 *    - 区间查询: 支持最大值查询
 *    - 懒惰标记处理: 正确处理区间加法和区间覆盖标记的优先级关系
 * 3. 路径操作:
 *    - edgeUpdate: 更新特定边的权值
 *    - pathUpdate: 将路径上所有边权改为指定值
 *    - pathAdd: 将路径上所有边权增加指定值
 *    - pathMax: 查询路径上边权的最大值
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Code03_UnderMoon1 {

	public static int MAXN = 100001;
	public static int n;
	public static int[][] arr = new int[MAXN][3];

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

	// 线段树查询区间最大值
	// 但是需要同时兼顾，区间增加、区间重置，这两种操作
	// 那么就牵扯到两种操作相互影响的问题
	// 因为区间重置操作会取消之前的区间增加操作
	// 讲解110，线段树章节，题目5、题目6，重点讲了这种线段树
	// 不会的同学可以看看，讲的非常清楚
	// max[i]: 线段树节点i维护的区间最大值
	// addTag[i]: 线段树节点i的区间加法标记
	// updateTag[i]: 线段树节点i的区间覆盖标记
	// change[i]: 线段树节点i的覆盖值
	public static int[] max = new int[MAXN << 2];
	public static int[] addTag = new int[MAXN << 2];
	public static boolean[] updateTag = new boolean[MAXN << 2];
	public static int[] change = new int[MAXN << 2];

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
		max[i] = Math.max(max[i << 1], max[i << 1 | 1]);
	}

	// 线段树懒惰标记 - 设置区间加法标记
	// i: 当前节点索引, v: 加法值
	public static void addLazy(int i, int v) {
		max[i] += v; // 更新最大值
		addTag[i] += v; // 设置加法标记
	}

	// 线段树懒惰标记 - 设置区间覆盖标记
	// i: 当前节点索引, v: 覆盖值
	public static void updateLazy(int i, int v) {
		max[i] = v; // 更新最大值
		addTag[i] = 0; // 清除加法标记
		updateTag[i] = true; // 设置覆盖标记
		change[i] = v; // 设置覆盖值
	}

	// 线段树下传懒惰标记 - 将标记传递给子节点
	// i: 当前节点索引
	public static void down(int i) {
		// 注意：区间覆盖操作优先级高于区间加法操作
		if (updateTag[i]) { // 如果有覆盖标记
			updateLazy(i << 1, change[i]);  // 传递给左子节点
			updateLazy(i << 1 | 1, change[i]); // 传递给右子节点
			updateTag[i] = false; // 清除当前节点的覆盖标记
		}
		if (addTag[i] != 0) { // 如果有加法标记
			addLazy(i << 1, addTag[i]);  // 传递给左子节点
			addLazy(i << 1 | 1, addTag[i]); // 传递给右子节点
			addTag[i] = 0; // 清除当前节点的加法标记
		}
	}

	// 线段树区间覆盖操作
	// jobl: 操作区间左端点, jobr: 操作区间右端点, jobv: 覆盖值
	// l: 当前区间左端点, r: 当前区间右端点, i: 当前节点索引
	public static void update(int jobl, int jobr, int jobv, int l, int r, int i) {
		if (jobl <= l && r <= jobr) { // 当前区间完全包含在操作区间内
			updateLazy(i, jobv); // 直接标记覆盖
		} else {
			int mid = (l + r) >> 1;
			down(i); // 下传懒惰标记
			if (jobl <= mid) { // 递归处理左子树
				update(jobl, jobr, jobv, l, mid, i << 1);
			}
			if (jobr > mid) { // 递归处理右子树
				update(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
			}
			up(i); // 向上更新
		}
	}

	// 线段树区间加法操作
	// jobl: 操作区间左端点, jobr: 操作区间右端点, jobv: 加法值
	// l: 当前区间左端点, r: 当前区间右端点, i: 当前节点索引
	public static void add(int jobl, int jobr, int jobv, int l, int r, int i) {
		if (jobl <= l && r <= jobr) { // 当前区间完全包含在操作区间内
			addLazy(i, jobv); // 直接标记加法
		} else {
			int mid = (l + r) >> 1;
			down(i); // 下传懒惰标记
			if (jobl <= mid) { // 递归处理左子树
				add(jobl, jobr, jobv, l, mid, i << 1);
			}
			if (jobr > mid) { // 递归处理右子树
				add(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
			}
			up(i); // 向上更新
		}
	}

	// 线段树区间最大值查询
	// jobl: 查询区间左端点, jobr: 查询区间右端点
	// l: 当前区间左端点, r: 当前区间右端点, i: 当前节点索引
	public static int query(int jobl, int jobr, int l, int r, int i) {
		if (jobl <= l && r <= jobr) { // 当前区间完全包含在查询区间内
			return max[i];
		}
		int mid = (l + r) >> 1;
		down(i); // 下传懒惰标记
		int ans = Integer.MIN_VALUE;
		if (jobl <= mid) { // 递归查询左子树
			ans = Math.max(ans, query(jobl, jobr, l, mid, i << 1));
		}
		if (jobr > mid) { // 递归查询右子树
			ans = Math.max(ans, query(jobl, jobr, mid + 1, r, i << 1 | 1));
		}
		return ans;
	}

	// 更新第ei条边的权值为val
	// 通过边权转点权的方式，将边权存储在深度较大的节点上
	public static void edgeUpdate(int ei, int val) {
		int x = arr[ei][0];
		int y = arr[ei][1];
		int down = Math.max(dfn[x], dfn[y]); // 深度较大的节点
		update(down, down, val, 1, n, 1); // 单点更新
	}

	// 将x到y路径上所有边权改为v
	public static void pathUpdate(int x, int y, int v) {
		// 当两个节点不在同一链上时，不断跳转到链顶
		while (top[x] != top[y]) {
			// 选择深度较大的链顶进行操作
			if (dep[top[x]] <= dep[top[y]]) {
				// 对y到其链顶的区间进行覆盖操作
				update(dfn[top[y]], dfn[y], v, 1, n, 1);
				y = fa[top[y]]; // 跳转到链顶的父节点
			} else {
				// 对x到其链顶的区间进行覆盖操作
				update(dfn[top[x]], dfn[x], v, 1, n, 1);
				x = fa[top[x]]; // 跳转到链顶的父节点
			}
		}
		// 两个节点在同一链上时，对区间进行覆盖操作
		update(Math.min(dfn[x], dfn[y]) + 1, Math.max(dfn[x], dfn[y]), v, 1, n, 1);
	}

	// 将x到y路径上所有边权增加v
	public static void pathAdd(int x, int y, int v) {
		// 当两个节点不在同一链上时，不断跳转到链顶
		while (top[x] != top[y]) {
			// 选择深度较大的链顶进行操作
			if (dep[top[x]] <= dep[top[y]]) {
				// 对y到其链顶的区间进行加法操作
				add(dfn[top[y]], dfn[y], v, 1, n, 1);
				y = fa[top[y]]; // 跳转到链顶的父节点
			} else {
				// 对x到其链顶的区间进行加法操作
				add(dfn[top[x]], dfn[x], v, 1, n, 1);
				x = fa[top[x]]; // 跳转到链顶的父节点
			}
		}
		// 两个节点在同一链上时，对区间进行加法操作
		add(Math.min(dfn[x], dfn[y]) + 1, Math.max(dfn[x], dfn[y]), v, 1, n, 1);
	}

	// 查询x到y路径上所有边权的最大值
	public static int pathMax(int x, int y) {
		int ans = Integer.MIN_VALUE;
		// 当两个节点不在同一链上时，不断跳转到链顶
		while (top[x] != top[y]) {
			// 选择深度较大的链顶进行操作
			if (dep[top[x]] <= dep[top[y]]) {
				// 更新y到其链顶区间的最大值
				ans = Math.max(ans, query(dfn[top[y]], dfn[y], 1, n, 1));
				y = fa[top[y]]; // 跳转到链顶的父节点
			} else {
				// 更新x到其链顶区间的最大值
				ans = Math.max(ans, query(dfn[top[x]], dfn[x], 1, n, 1));
				x = fa[top[x]]; // 跳转到链顶的父节点
			}
		}
		// 两个节点在同一链上时，更新区间最大值
		ans = Math.max(ans, query(Math.min(dfn[x], dfn[y]) + 1, Math.max(dfn[x], dfn[y]), 1, n, 1));
		return ans;
	}

	// 预处理函数 - 构建树结构并初始化边权
	public static void prepare() {
		// 构建链式前向星
		for (int i = 1; i < n; i++) {
			addEdge(arr[i][0], arr[i][1]);
			addEdge(arr[i][1], arr[i][0]);
		}
		dfs3(); // 迭代版第一次DFS
		dfs4(); // 迭代版第二次DFS
		// 初始化所有边权
		for (int i = 1; i < n; i++) {
			edgeUpdate(i, arr[i][2]);
		}
	}

	public static void main(String[] args) {
		Kattio io = new Kattio();
		n = io.nextInt();
		// 读入所有边信息
		for (int i = 1; i < n; i++) {
			arr[i][0] = io.nextInt();
			arr[i][1] = io.nextInt();
			arr[i][2] = io.nextInt();
		}
		prepare(); // 预处理
		String op = io.next();
		int x, y, v;
		// 处理所有操作，直到遇到Stop命令
		while (!op.equals("Stop")) {
			if (op.equals("Change")) { // 更新边权操作
				x = io.nextInt();
				v = io.nextInt();
				edgeUpdate(x, v);
			} else if (op.equals("Cover")) { // 区间覆盖操作
				x = io.nextInt();
				y = io.nextInt();
				v = io.nextInt();
				pathUpdate(x, y, v);
			} else if (op.equals("Add")) { // 区间加法操作
				x = io.nextInt();
				y = io.nextInt();
				v = io.nextInt();
				pathAdd(x, y, v);
			} else { // 最大值查询操作
				x = io.nextInt();
				y = io.nextInt();
				io.println(pathMax(x, y));
			}
			op = io.next();
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
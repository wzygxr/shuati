package class181;

// 雨天的尾巴，java版
// 测试链接 : https://www.luogu.com.cn/problem/P4556
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * 雨天的尾巴问题 (Rainy Day Tail)
 * 
 * 题目来源: Vani有约会 洛谷P4556
 * 题目链接: https://www.luogu.com.cn/problem/P4556
 * 
 * 题目描述:
 * 给定一棵 n 个节点的树和 m 次操作，每次操作在两点间路径上投放某种类型的物品。
 * 要求最后统计每个节点收到最多物品的类型。
 * 
 * 解题思路:
 * 1. 利用树上差分技术，在路径端点和LCA处打标记
 * 2. 为每个节点建立线段树，维护各类型物品的数量
 * 3. 自底向上合并子树信息，查询最大值对应的类型
 * 
 * 算法复杂度:
 * - 时间复杂度: O((n + m) log n)
 * - 空间复杂度: O(n log n)
 * 
 * 树上差分核心思想:
 * 1. 对于路径 u->v，在 u 和 v 处 +1，在 lca(u,v) 和 fa[lca(u,v)] 处 -1
 * 2. 通过DFS遍历，子树内的标记和即为该节点的物品数量
 */
public class Code02_RainyDayTail1 {

	public static int MAXN = 100001;
	public static int MAXV = 100000; // 物品类型值域上限

	public static int MAXT = MAXN * 50; // 线段树节点数上限

	public static int MAXP = 20; // 倍增数组大小
	public static int n, m;

	// 邻接表存储树结构
	public static int[] head = new int[MAXN];
	public static int[] nxt = new int[MAXN << 1];
	public static int[] to = new int[MAXN << 1];
	public static int cntg;

	// 节点深度和倍增跳转表（用于求LCA）
	public static int[] dep = new int[MAXN];
	public static int[][] stjump = new int[MAXN][MAXP];

	// 每个节点对应的线段树根节点及相关数组
	public static int[] root = new int[MAXN];
	public static int[] ls = new int[MAXT];
	public static int[] rs = new int[MAXT];
	public static int[] maxCnt = new int[MAXT]; // 维护区间最大值
	public static int cntt;

	// 答案数组
	public static int[] ans = new int[MAXN];

	// 递归改迭代需要
	public static int[][] ufe = new int[MAXN][3];
	public static int stacksize, u, f, e;

	public static void push(int u, int f, int e) {
		ufe[stacksize][0] = u;
		ufe[stacksize][1] = f;
		ufe[stacksize][2] = e;
		stacksize++;
	}

	public static void pop() {
		--stacksize;
		u = ufe[stacksize][0];
		f = ufe[stacksize][1];
		e = ufe[stacksize][2];
	}

	/**
	 * 添加边到邻接表
	 * @param u 起点
	 * @param v 终点
	 */
	public static void addEdge(int u, int v) {
		nxt[++cntg] = head[u];
		to[cntg] = v;
		head[u] = cntg;
	}

	// 递归版，java会爆栈，C++可以通过
	public static void dfs1(int u, int fa) {
		dep[u] = dep[fa] + 1;
		stjump[u][0] = fa;
		for (int p = 1; p < MAXP; p++) {
			stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
		}
		for (int e = head[u]; e > 0; e = nxt[e]) {
			int v = to[e];
			if (v != fa) {
				dfs1(v, u);
			}
		}
	}

	// dfs1改迭代
	public static void dfs2() {
		stacksize = 0;
		push(1, 0, -1);
		while (stacksize > 0) {
			pop();
			if (e == -1) {
				dep[u] = dep[f] + 1;
				stjump[u][0] = f;
				for (int p = 1; p < MAXP; p++) {
					stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
				}
				e = head[u];
			} else {
				e = nxt[e];
			}
			if (e != 0) {
				push(u, f, e);
				if (to[e] != f) {
					push(to[e], u, -1);
				}
			}
		}
	}

	/**
	 * 求两个节点的最近公共祖先(LCA)
	 * @param a 节点a
	 * @param b 节点b
	 * @return LCA节点
	 */
	public static int getLca(int a, int b) {
		// 保证a的深度不小于b
		if (dep[a] < dep[b]) {
			int tmp = a;
			a = b;
			b = tmp;
		}
		// 将a向上跳到与b同一深度
		for (int p = MAXP - 1; p >= 0; p--) {
			if (dep[stjump[a][p]] >= dep[b]) {
				a = stjump[a][p];
			}
		}
		// 如果a就是b，说明b是a的祖先
		if (a == b) {
			return a;
		}
		// a和b一起向上跳，直到它们的父节点相同
		for (int p = MAXP - 1; p >= 0; p--) {
			if (stjump[a][p] != stjump[b][p]) {
				a = stjump[a][p];
				b = stjump[b][p];
			}
		}
		return stjump[a][0]; // 返回父节点即为LCA
	}

	/**
	 * 更新线段树节点信息（维护区间最大值）
	 * @param i 节点索引
	 */
	public static void up(int i) {
		maxCnt[i] = Math.max(maxCnt[ls[i]], maxCnt[rs[i]]);
	}

	/**
	 * 在线段树中添加/删除一个值
	 * @param jobi 要操作的值（物品类型）
	 * @param jobv 操作值（+1表示添加，-1表示删除）
	 * @param l 区间左端点
	 * @param r 区间右端点
	 * @param i 当前节点索引
	 * @return 更新后的节点索引
	 */
	public static int add(int jobi, int jobv, int l, int r, int i) {
		int rt = i;
		if (rt == 0) {
			rt = ++cntt; // 动态开点
		}
		if (l == r) {
			maxCnt[rt] += jobv; // 叶子节点更新计数
		} else {
			int mid = (l + r) >> 1;
			if (jobi <= mid) {
				ls[rt] = add(jobi, jobv, l, mid, ls[rt]); // 递归更新左子树
			} else {
				rs[rt] = add(jobi, jobv, mid + 1, r, rs[rt]); // 递归更新右子树
			}
			up(rt); // 更新当前节点信息
		}
		return rt;
	}

	/**
	 * 合并两棵线段树
	 * @param l 区间左端点
	 * @param r 区间右端点
	 * @param t1 第一棵线段树根节点
	 * @param t2 第二棵线段树根节点
	 * @return 合并后的线段树根节点
	 */
	public static int merge(int l, int r, int t1, int t2) {
		// 边界条件：如果其中一个节点为空，返回另一个节点
		if (t1 == 0 || t2 == 0) {
			return t1 + t2;
		}
		// 叶子节点：合并节点信息
		if (l == r) {
			maxCnt[t1] += maxCnt[t2]; // 累加计数
		} else {
			// 递归合并左右子树
			int mid = (l + r) >> 1;
			ls[t1] = merge(l, mid, ls[t1], ls[t2]);
			rs[t1] = merge(mid + 1, r, rs[t1], rs[t2]);
			up(t1); // 更新当前节点信息
		}
		return t1;
	}

	/**
	 * 查询最大值对应的物品类型
	 * @param l 区间左端点
	 * @param r 区间右端点
	 * @param i 当前节点索引
	 * @return 最大值对应的物品类型
	 */
	public static int query(int l, int r, int i) {
		// 叶子节点：返回该类型
		if (l == r) {
			return l;
		}
		int mid = (l + r) >> 1;
		// 根据左右子树的最大值决定递归方向
		if (maxCnt[i] == maxCnt[ls[i]]) {
			return query(l, mid, ls[i]);
		} else {
			return query(mid + 1, r, rs[i]);
		}
	}

	// 递归版，java会爆栈，C++可以通过
	public static void calc1(int u, int fa) {
		for (int e = head[u]; e > 0; e = nxt[e]) {
			int v = to[e];
			if (v != fa) {
				calc1(v, u);
			}
		}
		for (int ei = head[u]; ei > 0; ei = nxt[ei]) {
			int v = to[ei];
			if (v != fa) {
				root[u] = merge(1, MAXV, root[u], root[v]);
			}
		}
		if (maxCnt[root[u]] > 0) {
			ans[u] = query(1, MAXV, root[u]);
		}
	}

	// calc1改迭代
	public static void calc2() {
		stacksize = 0;
		push(1, 0, -1);
		while (stacksize > 0) {
			pop();
			if (e == -1) {
				e = head[u];
			} else {
				e = nxt[e];
			}
			if (e != 0) {
				push(u, f, e);
				if (to[e] != f) {
					push(to[e], u, -1);
				}
			} else {
				for (int ei = head[u]; ei > 0; ei = nxt[ei]) {
					int v = to[ei];
					if (v != f) {
						root[u] = merge(1, MAXV, root[u], root[v]);
					}
				}
				if (maxCnt[root[u]] > 0) {
					ans[u] = query(1, MAXV, root[u]);
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		n = in.nextInt();
		m = in.nextInt();
		for (int i = 1, u, v; i < n; i++) {
			u = in.nextInt();
			v = in.nextInt();
			addEdge(u, v);
			addEdge(v, u);
		}
		// dfs1(1, 0);
		dfs2();
		for (int i = 1; i <= m; i++) {
			int x = in.nextInt();
			int y = in.nextInt();
			int food = in.nextInt();
			int lca = getLca(x, y);
			int lcafa = stjump[lca][0];
			// 树上差分：在路径端点和LCA处打标记
			root[x] = add(food, 1, 1, MAXV, root[x]);
			root[y] = add(food, 1, 1, MAXV, root[y]);
			root[lca] = add(food, -1, 1, MAXV, root[lca]);
			root[lcafa] = add(food, -1, 1, MAXV, root[lcafa]);
		}
		// calc1(1, 0);
		calc2();
		for (int i = 1; i <= n; i++) {
			out.println(ans[i]);
		}
		out.flush();
		out.close();
	}

	// 读写工具类
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
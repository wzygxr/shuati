package class158;

/**
 * 路径上的第k小，java版
 * 
 * 题目来源：洛谷 P2633 - Count on a tree
 * 题目链接：https://www.luogu.com.cn/problem/P2633
 * 
 * 题目描述:
 * 有n个节点，编号1~n，每个节点有权值，有n-1条边，所有节点组成一棵树
 * 一共有m条查询，每条查询 u v k : 打印u号点到v号点的路径上，第k小的点权
 * 
 * 解题思路:
 * 使用树上可持久化线段树（树上主席树）结合LCA解决该问题。
 * 1. 对节点权值进行离散化处理
 * 2. 通过DFS遍历树，为每个节点建立主席树
 * 3. 利用DFS序和LCA算法计算树上路径信息
 * 4. 对于查询u到v的路径，利用容斥原理计算路径上的第k小值
 * 
 * 强制在线处理:
 * 题目有强制在线的要求，上一次打印的答案为lastAns，初始时lastAns = 0
 * 每次给定的u、v、k，按照如下方式得到真实的u、v、k，查询完成后更新lastAns
 * 真实u = 给定u ^ lastAns
 * 真实v = 给定v
 * 真实k = 给定k
 * 
 * 时间复杂度: O((n + m) log n)
 * 空间复杂度: O(n log n)
 * 
 * 示例:
 * 输入:
 * 5 3
 * 1 2 3 4 5
 * 1 2
 * 1 3
 * 2 4
 * 2 5
 * 4 5 2
 * 3 4 3
 * 1 2 1
 * 
 * 输出:
 * 3
 * 4
 * 1
 * 
 * 解释:
 * 查询4 5 2：节点4到节点5的路径为[4,2,5]，点权为[4,2,5]，第2小为3
 * 查询3 4 3：节点3到节点4的路径为[3,1,2,4]，点权为[3,1,2,4]，第3小为4
 * 查询1 2 1：节点1到节点2的路径为[1,2]，点权为[1,2]，第1小为1
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code04_CountOnTree1 {

	public static int MAXN = 100001;

	public static int MAXH = 20;

	public static int MAXT = MAXN * MAXH;

	public static int n, m, s;

	// 各个节点权值
	public static int[] arr = new int[MAXN];

	// 收集权值排序并且去重做离散化
	public static int[] sorted = new int[MAXN];

	// 链式前向星需要
	public static int[] head = new int[MAXN];

	public static int[] to = new int[MAXN << 1];

	public static int[] next = new int[MAXN << 1];

	public static int cntg = 0;

	// 可持久化线段树需要
	public static int[] root = new int[MAXN];

	public static int[] left = new int[MAXT];

	public static int[] right = new int[MAXT];

	public static int[] size = new int[MAXT];

	public static int cntt = 0;

	// 树上倍增找lca需要
	public static int[] deep = new int[MAXN];

	public static int[][] stjump = new int[MAXN][MAXH];

	/**
	 * 二分查找数字num在sorted数组中的位置
	 * @param num 要查找的数字
	 * @return 数字在sorted数组中的位置
	 */
	public static int kth(int num) {
		int left = 1, right = s, mid;
		while (left <= right) {
			mid = (left + right) / 2;
			if (sorted[mid] == num) {
				return mid;
			} else if (sorted[mid] < num) {
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		return -1;
	}

	/**
	 * 构建空线段树
	 * @param l 区间左端点
	 * @param r 区间右端点
	 * @return 根节点编号
	 */
	public static int build(int l, int r) {
		int rt = ++cntt;
		size[rt] = 0;
		if (l < r) {
			int mid = (l + r) / 2;
			left[rt] = build(l, mid);
			right[rt] = build(mid + 1, r);
		}
		return rt;
	}

	/**
	 * 预处理，对节点权值进行离散化
	 */
	public static void prepare() {
		for (int i = 1; i <= n; i++) {
			sorted[i] = arr[i];
		}
		Arrays.sort(sorted, 1, n + 1);
		s = 1;
		for (int i = 2; i <= n; i++) {
			if (sorted[s] != sorted[i]) {
				sorted[++s] = sorted[i];
			}
		}
		root[0] = build(1, s);
	}

	/**
	 * 添加边
	 * @param u 起点
	 * @param v 终点
	 */
	public static void addEdge(int u, int v) {
		next[++cntg] = head[u];
		to[cntg] = v;
		head[u] = cntg;
	}

	/**
	 * 更新线段树节点
	 * @param jobi 要更新的位置
	 * @param l 当前区间左端点
	 * @param r 当前区间右端点
	 * @param i 前一个版本的节点编号
	 * @return 新版本的根节点编号
	 */
	public static int insert(int jobi, int l, int r, int i) {
		int rt = ++cntt;
		left[rt] = left[i];
		right[rt] = right[i];
		size[rt] = size[i] + 1;
		if (l < r) {
			int mid = (l + r) / 2;
			if (jobi <= mid) {
				left[rt] = insert(jobi, l, mid, left[rt]);
			} else {
				right[rt] = insert(jobi, mid + 1, r, right[rt]);
			}
		}
		return rt;
	}

	/**
	 * 查询路径上第k小的点权
	 * @param jobk 要查询的排名
	 * @param l 当前区间左端点
	 * @param r 当前区间右端点
	 * @param u 节点u的根节点
	 * @param v 节点v的根节点
	 * @param lca 节点u和v的LCA的根节点
	 * @param lcafa LCA父节点的根节点
	 * @return 第k小的点权在离散化数组中的位置
	 */
	public static int query(int jobk, int l, int r, int u, int v, int lca, int lcafa) {
		if (l == r) {
			return l;
		}
		// 计算左子树中数的个数
		int lsize = size[left[u]] + size[left[v]] - size[left[lca]] - size[left[lcafa]];
		int mid = (l + r) / 2;
		if (lsize >= jobk) {
			return query(jobk, l, mid, left[u], left[v], left[lca], left[lcafa]);
		} else {
			return query(jobk - lsize, mid + 1, r, right[u], right[v], right[lca], right[lcafa]);
		}
	}

	// 递归版，C++可以通过，java无法通过，递归会爆栈
	public static void dfs1(int u, int f) {
		root[u] = insert(kth(arr[u]), 1, s, root[f]);
		deep[u] = deep[f] + 1;
		stjump[u][0] = f;
		for (int p = 1; p < MAXH; p++) {
			stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
		}
		for (int ei = head[u]; ei > 0; ei = next[ei]) {
			if (to[ei] != f) {
				dfs1(to[ei], u);
			}
		}
	}

	// 迭代版，都可以通过
	// 讲解118，详解了从递归版改迭代版
	public static int[][] ufe = new int[MAXN][3];

	public static int stackSize, u, f, e;

	public static void push(int u, int f, int e) {
		ufe[stackSize][0] = u;
		ufe[stackSize][1] = f;
		ufe[stackSize][2] = e;
		stackSize++;
	}

	public static void pop() {
		--stackSize;
		u = ufe[stackSize][0];
		f = ufe[stackSize][1];
		e = ufe[stackSize][2];
	}

	/**
	 * dfs1的迭代版
	 */
	public static void dfs2() {
		stackSize = 0;
		push(1, 0, -1);
		while (stackSize > 0) {
			pop();
			if (e == -1) {
				root[u] = insert(kth(arr[u]), 1, s, root[f]);
				deep[u] = deep[f] + 1;
				stjump[u][0] = f;
				for (int p = 1; p < MAXH; p++) {
					stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
				}
				e = head[u];
			} else {
				e = next[e];
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
	 * 计算节点a和节点b的最近公共祖先(LCA)
	 * @param a 节点a
	 * @param b 节点b
	 * @return 节点a和节点b的LCA
	 */
	public static int lca(int a, int b) {
		if (deep[a] < deep[b]) {
			int tmp = a;
			a = b;
			b = tmp;
		}
		// 将a提升到与b同一深度
		for (int p = MAXH - 1; p >= 0; p--) {
			if (deep[stjump[a][p]] >= deep[b]) {
				a = stjump[a][p];
			}
		}
		if (a == b) {
			return a;
		}
		// 同时提升a和b直到它们的父节点相同
		for (int p = MAXH - 1; p >= 0; p--) {
			if (stjump[a][p] != stjump[b][p]) {
				a = stjump[a][p];
				b = stjump[b][p];
			}
		}
		return stjump[a][0];
	}

	/**
	 * 查询节点u到节点v路径上第k小的点权
	 * @param u 起点
	 * @param v 终点
	 * @param k 要查询的排名
	 * @return 第k小的点权
	 */
	public static int kth(int u, int v, int k) {
		int lca = lca(u, v);
		int i = query(k, 1, s, root[u], root[v], root[lca], root[stjump[lca][0]]);
		return sorted[i];
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		n = (int) in.nval;
		in.nextToken();
		m = (int) in.nval;
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			arr[i] = (int) in.nval;
		}
		prepare();
		for (int i = 1, u, v; i < n; i++) {
			in.nextToken();
			u = (int) in.nval;
			in.nextToken();
			v = (int) in.nval;
			addEdge(u, v);
			addEdge(v, u);
		}
		dfs2(); // 使用迭代版防止爆栈
		for (int i = 1, u, v, k, lastAns = 0; i <= m; i++) {
			in.nextToken();
			u = (int) in.nval ^ lastAns;
			in.nextToken();
			v = (int) in.nval;
			in.nextToken();
			k = (int) in.nval;
			lastAns = kth(u, v, k);
			out.println(lastAns);
		}
		out.flush();
		out.close();
		br.close();
	}

}
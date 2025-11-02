package class181;

// 晋升者计数，java版
// 测试链接 : https://www.luogu.com.cn/problem/P3605
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * 晋升者计数问题 (Promotion Counting)
 * 
 * 题目来源: USACO 2017 January Contest, Platinum Problem 1. Promotion Counting
 * 题目链接: https://www.luogu.com.cn/problem/P3605
 * 
 * 题目描述:
 * 给定一棵 n 个节点的树，每个节点有一个能力值。对于每个节点，统计其子树中有多少个节点的能力值
 * 严格大于该节点的能力值。
 * 
 * 解题思路:
 * 1. 使用线段树合并技术解决树上统计问题
 * 2. 为每个节点建立一棵权值线段树，维护子树中各能力值的出现次数
 * 3. 从叶子节点开始，自底向上合并子树的线段树
 * 4. 查询当前节点线段树中大于该节点能力值的节点数量
 * 
 * 算法复杂度:
 * - 时间复杂度: O(n log n)，其中 n 是节点数量
 * - 空间复杂度: O(n log n)
 * 
 * 线段树合并核心思想:
 * 1. 对于两棵线段树的对应节点，如果只有一棵树有该节点，则直接使用该节点
 * 2. 如果两棵树都有该节点，则递归合并左右子树，并更新当前节点信息
 * 3. 合并过程类似于可并堆的合并方式
 */
public class Code01_PromotionCounting1 {

	// 最大节点数
	public static int MAXN = 100001;

	// 线段树节点数上限
	public static int MAXT = MAXN * 40;

	// 节点数量
	public static int n;

	// 邻接表存储树结构
	public static int[] head = new int[MAXN];
	public static int[] nxt = new int[MAXN << 1];
	public static int[] to = new int[MAXN << 1];
	public static int cntg;

	// 节点能力值数组和排序后的数组
	public static int[] arr = new int[MAXN];
	public static int[] sorted = new int[MAXN];

	// 离散化后的不同值数量
	public static int cntv;

	// 每个节点对应的线段树根节点
	public static int[] root = new int[MAXN];
	
	// 线段树左右子节点数组
	public static int[] ls = new int[MAXT];
	public static int[] rs = new int[MAXT];
	
	// 线段树节点维护的子树大小（该区间内节点数量）
	public static int[] siz = new int[MAXT];
	
	// 线段树节点计数器
	public static int cntt;

	// 答案数组
	public static int[] ans = new int[MAXN];

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

	/**
	 * 二分查找数字在排序数组中的位置
	 * @param num 要查找的数字
	 * @return 位置索引
	 */
	public static int kth(int num) {
		int left = 1, right = cntv, mid, ret = 0;
		while (left <= right) {
			mid = (left + right) >> 1;
			if (sorted[mid] <= num) {
				ret = mid;
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		return ret;
	}

	/**
	 * 更新线段树节点信息（父节点信息由子节点信息推导）
	 * @param i 节点索引
	 */
	public static void up(int i) {
		siz[i] = siz[ls[i]] + siz[rs[i]];
	}

	/**
	 * 在线段树中添加一个值
	 * @param jobi 要添加的值（离散化后的索引）
	 * @param l 区间左端点
	 * @param r 区间右端点
	 * @param i 当前节点索引
	 * @return 更新后的节点索引
	 */
	public static int add(int jobi, int l, int r, int i) {
		int rt = i;
		if (rt == 0) {
			rt = ++cntt; // 动态开点
		}
		if (l == r) {
			siz[rt]++; // 叶子节点计数加1
		} else {
			int mid = (l + r) >> 1;
			if (jobi <= mid) {
				ls[rt] = add(jobi, l, mid, ls[rt]); // 递归更新左子树
			} else {
				rs[rt] = add(jobi, mid + 1, r, rs[rt]); // 递归更新右子树
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
			siz[t1] += siz[t2]; // 累加计数
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
	 * 查询区间[jobl, jobr]内的节点数量
	 * @param jobl 查询区间左端点
	 * @param jobr 查询区间右端点
	 * @param l 当前区间左端点
	 * @param r 当前区间右端点
	 * @param i 当前节点索引
	 * @return 区间内节点数量
	 */
	public static int query(int jobl, int jobr, int l, int r, int i) {
		// 边界条件：查询区间无效或节点为空
		if (jobl > jobr || i == 0) {
			return 0;
		}
		// 完全覆盖：当前区间完全在查询区间内
		if (jobl <= l && r <= jobr) {
			return siz[i];
		}
		int mid = (l + r) >> 1;
		int ret = 0;
		// 递归查询左右子树
		if (jobl <= mid) {
			ret += query(jobl, jobr, l, mid, ls[i]);
		}
		if (jobr > mid) {
			ret += query(jobl, jobr, mid + 1, r, rs[i]);
		}
		return ret;
	}

	// 递归版，java会爆栈，C++可以通过
	public static void calc1(int u, int fa) {
		for (int e = head[u]; e > 0; e = nxt[e]) {
			int v = to[e];
			if (v != fa) {
				calc1(v, u);
			}
		}
		for (int e = head[u]; e > 0; e = nxt[e]) {
			int v = to[e];
			if (v != fa) {
				root[u] = merge(1, cntv, root[u], root[v]);
			}
		}
		ans[u] = query(arr[u] + 1, cntv, 1, cntv, root[u]);
	}

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
						root[u] = merge(1, cntv, root[u], root[v]);
					}
				}
				ans[u] = query(arr[u] + 1, cntv, 1, cntv, root[u]);
			}
		}
	}

	public static void compute() {
		for (int i = 1; i <= n; i++) {
			sorted[i] = arr[i];
		}
		Arrays.sort(sorted, 1, n + 1);
		cntv = 1;
		for (int i = 2; i <= n; i++) {
			if (sorted[cntv] != sorted[i]) {
				sorted[++cntv] = sorted[i];
			}
		}
		for (int i = 1; i <= n; i++) {
			arr[i] = kth(arr[i]);
		}
		for (int i = 1; i <= n; i++) {
			root[i] = add(arr[i], 1, cntv, root[i]);
		}
		// calc1(1, 0);
		calc2();
	}

	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		n = in.nextInt();
		for (int i = 1; i <= n; i++) {
			arr[i] = in.nextInt();
		}
		for (int i = 2, fa; i <= n; i++) {
			fa = in.nextInt();
			addEdge(fa, i);
			addEdge(i, fa);
		}
		compute();
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
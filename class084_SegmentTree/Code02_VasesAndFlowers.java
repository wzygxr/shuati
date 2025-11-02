package class111;

import java.io.*;

// 瓶子里的花朵 (Vases and Flowers)
// 题目来源: HDU 4614. Vases and Flowers
// 题目链接: https://acm.hdu.edu.cn/showproblem.php?pid=4614
// 
// 题目描述:
// 给定n个瓶子，编号从0~n-1，一开始所有瓶子都是空的
// 每个瓶子最多插入一朵花，实现以下两种类型的操作
// 操作 1 from flower : 一共有flower朵花，从from位置开始依次插入花朵，已经有花的瓶子跳过
//                     如果一直到最后的瓶子，花也没有用完，就丢弃剩下的花朵
//                     返回这次操作插入的首个空瓶的位置 和 最后空瓶的位置
//                     如果从from开始所有瓶子都有花，打印"Can not put any one."
// 操作 2 left right  : 从left位置开始到right位置的瓶子，变回空瓶，返回清理花朵的数量
//
// 解题思路:
// 1. 使用线段树维护区间和，表示每个区间内花朵的数量
// 2. 对于插入操作，需要找到从指定位置开始的第k个空位置
// 3. 对于清理操作，直接查询区间和并更新区间为0
// 4. 利用线段树的区间查询和更新功能高效处理操作
//
// 时间复杂度: O(m log n)，其中m为操作次数，n为瓶子数量
// 空间复杂度: O(n)
public class Code02_VasesAndFlowers {

	public static int MAXN = 50001;

	public static int[] sum = new int[MAXN << 2];

	public static int[] change = new int[MAXN << 2];

	public static boolean[] update = new boolean[MAXN << 2];

	public static int n;

	// 向上更新节点值（区间和）
	public static void up(int i) {
		sum[i] = sum[i << 1] + sum[i << 1 | 1];
	}

	// 下发懒惰标记
	public static void down(int i, int ln, int rn) {
		if (update[i]) {
			lazy(i << 1, change[i], ln);
			lazy(i << 1 | 1, change[i], rn);
			update[i] = false;
		}
	}

	// 设置懒惰标记
	public static void lazy(int i, int v, int n) {
		sum[i] = v * n;
		change[i] = v;
		update[i] = true;
	}

	// 构建线段树
	public static void build(int l, int r, int i) {
		if (l < r) {
			int mid = (l + r) >> 1;
			build(l, mid, i << 1);
			build(mid + 1, r, i << 1 | 1);
		}
		sum[i] = 0;
		update[i] = false;
	}

	// 区间更新操作
	public static void update(int jobl, int jobr, int jobv, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			lazy(i, jobv, r - l + 1);
		} else {
			int mid = (l + r) >> 1;
			down(i, mid - l + 1, r - mid);
			if (jobl <= mid) {
				update(jobl, jobr, jobv, l, mid, i << 1);
			}
			if (jobr > mid) {
				update(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
			}
			up(i);
		}
	}

	// 区间查询操作（查询区间和）
	public static int query(int jobl, int jobr, int l, int r, int i) {
		if (jobl <= l && r <= jobr) {
			return sum[i];
		}
		int mid = (l + r) >> 1;
		down(i, mid - l + 1, r - mid);
		int ans = 0;
		if (jobl <= mid) {
			ans += query(jobl, jobr, l, mid, i << 1);
		}
		if (jobr > mid) {
			ans += query(jobl, jobr, mid + 1, r, i << 1 | 1);
		}
		return ans;
	}

	// 插入花朵操作
	public static int[] insert(int from, int flowers) {
		// 题目给的位置从0开始
		// 线段树下标从1开始
		from++;
		int start, end;
		int zeros = n - from + 1 - query(from, n, 1, n, 1);
		if (zeros == 0) {
			start = 0;
			end = 0;
		} else {
			start = findZero(from, 1);
			end = findZero(from, Math.min(zeros, flowers));
			update(start, end, 1, 1, n, 1);
		}
		// 题目需要从0开始的下标
		start--;
		end--;
		return new int[] { start, end };
	}

	// 在s~n范围内查找第k个空位置
	public static int findZero(int s, int k) {
		int l = s, r = n, mid;
		int ans = 0;
		while (l <= r) {
			mid = (l + r) >> 1;
			if (mid - s + 1 - query(s, mid, 1, n, 1) >= k) {
				ans = mid;
				r = mid - 1;
			} else {
				l = mid + 1;
			}
		}
		return ans;
	}

	// 清理花朵操作
	// 注意题目给的下标从0开始
	// 线段树下标从1开始
	public static int clear(int left, int right) {
		left++;
		right++;
		int ans = query(left, right, 1, n, 1);
		update(left, right, 0, 1, n, 1);
		return ans;
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		int t = (int) in.nval;
		for (int i = 1; i <= t; i++) {
			in.nextToken();
			n = (int) in.nval;
			in.nextToken();
			int m = (int) in.nval;
			build(1, n, 1);
			for (int j = 1; j <= m; j++) {
				in.nextToken();
				int op = (int) in.nval;
				if (op == 1) {
					in.nextToken();
					int from = (int) in.nval;
					in.nextToken();
					int flowers = (int) in.nval;
					int[] ans = insert(from, flowers);
					if (ans[0] == -1) {
						out.println("Can not put any one.");
					} else {
						out.println(ans[0] + " " + ans[1]);
					}
				} else {
					in.nextToken();
					int left = (int) in.nval;
					in.nextToken();
					int right = (int) in.nval;
					out.println(clear(left, right));
				}
			}
			out.println();
		}
		out.flush();
		out.close();
		br.close();
	}

}
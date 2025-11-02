// 区间最大匹配 - 普通莫队算法实现 (Java版本)
// 题目来源: LibreOJ #6273. 「LibreOJ NOI Round #1」区间最大匹配
// 题目链接: https://loj.ac/p/6273
// 题目大意: 给定一个数组，每次查询区间[l,r]内的最大匹配数
// 解题思路: 使用普通莫队算法，通过维护区间信息来回答查询
// 时间复杂度: O(n*sqrt(m))
// 空间复杂度: O(n)
//
// 相关题目链接:
// 1. LibreOJ #6273. 「LibreOJ NOI Round #1」区间最大匹配 - https://loj.ac/p/6273
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code04_MaximumMatch1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code04_MaximumMatch2.java
//
// 2. SPOJ DQUERY - https://www.spoj.com/problems/DQUERY/
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery3.py
//
// 3. 洛谷 P1494 [国家集训队]小Z的袜子 - https://www.luogu.com.cn/problem/P1494
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks3.py
//
// 4. Codeforces 617E XOR and Favorite Number - https://codeforces.com/problemset/problem/617/E
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber3.py
//
// 5. Codeforces 86D Powerful Array - https://codeforces.com/problemset/problem/86/D
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray3.py
//
// 6. 洛谷 P1972 [SDOI2009] HH的项链 - https://www.luogu.com.cn/problem/P1972
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code14_DQuery1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code14_DQuery2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code14_DQuery3.py
//
// 7. BZOJ 2120 数颜色 - https://www.luogu.com.cn/problem/B3202
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors3.py
//
// 8. 洛谷 P1903 [国家集训队]数颜色/维护队列 - https://www.luogu.com.cn/problem/P1903
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance3.py
//
// 9. SPOJ COT2 - Count on a tree II - https://www.luogu.com.cn/problem/SP10707
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code17_TreeMo1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code17_TreeMo2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code17_TreeMo3.py
//
// 10. AtCoder JOI 2014 Day1 历史研究 - https://www.luogu.com.cn/problem/AT_joisc2014_c
//     Java解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch1.java
//     C++解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch2.cpp
//     Python解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch3.py

package class179;

// 区间最大匹配，java版
// 给定长度为n的数组a、长度为m的数组b、一个正数z
// 数组a中数字x、数组b中数字y，如果x + y <= z，那么构成一个匹配
// 已经匹配的数字，不可以重复使用，一共有q条查询，格式如下
// 查询 l r : 数组b[l..r]范围上的数字，随意选择数组a中的数字，打印最多匹配数
// 1 <= n <= 152501
// 1 <= m、q <= 52501
// 1 <= a[i]、b[i]、z <= 10^9
// 测试链接 : https://www.luogu.com.cn/problem/P4477
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;

public class Code04_MaximumMatch1 {

	public static int MAXN = 152502;
	public static int MAXM = 52502;
	public static int MAXQ = 52502;
	public static int n, m, z, q;
	public static int[] a = new int[MAXN];
	public static int[] b = new int[MAXM];
	public static int[][] query = new int[MAXQ][3];
	public static int[] bi = new int[MAXM];

	// 线段树维护匹配信息，a[l..r]和b数组的数字进行匹配，l..r对应的信息在i位置
	// match[i] = v，a[l..r]和b数组的数字，一共匹配了v对
	// resta[i] = v，a[l..r]中还有v个数，可用于匹配b数组的数字
	// overb[i] = v，a[l..r]已经耗尽，但是还有v个b数组的数字没有满足
	public static int[] match = new int[MAXN << 2];
	public static int[] resta = new int[MAXN << 2];
	public static int[] overb = new int[MAXN << 2];

	public static int[] ans = new int[MAXQ];

	public static class QueryCmp implements Comparator<int[]> {
		@Override
		public int compare(int[] a, int[] b) {
			if (bi[a[0]] != bi[b[0]]) {
				return bi[a[0]] - bi[b[0]];
			}
			if ((bi[a[0]] & 1) == 1) {
				return a[1] - b[1];
			} else {
				return b[1] - a[1];
			}
		}
	}

	public static void up(int i) {
		int l = i << 1;
		int r = i << 1 | 1;
		int newMatch = Math.min(resta[l], overb[r]);
		resta[i] = resta[l] + resta[r] - newMatch;
		overb[i] = overb[l] + overb[r] - newMatch;
		match[i] = match[l] + match[r] + newMatch;
	}

	public static void build(int l, int r, int i) {
		if (l == r) {
			match[i] = 0;
			resta[i] = 1;
			overb[i] = 0;
		} else {
			int mid = (l + r) >> 1;
			build(l, mid, i << 1);
			build(mid + 1, r, i << 1 | 1);
			up(i);
		}
	}

	public static void add(int jobv, int l, int r, int i) {
		if (l == r) {
			if (resta[i] == 1) {
				match[i] = 1;
				resta[i] = 0;
			} else {
				overb[i]++;
			}
		} else {
			int mid = (l + r) >> 1;
			if (jobv + a[mid + 1] <= z) {
				add(jobv, mid + 1, r, i << 1 | 1);
			} else if (jobv + a[l] <= z) {
				add(jobv, l, mid, i << 1);
			}
			up(i);
		}
	}

	public static void del(int jobv, int l, int r, int i) {
		if (l == r) {
			if (overb[i] > 0) {
				overb[i]--;
			} else {
				match[i] = 0;
				resta[i] = 1;
			}
		} else {
			int mid = (l + r) >> 1;
			if (jobv + a[mid + 1] <= z) {
				del(jobv, mid + 1, r, i << 1 | 1);
			} else if (jobv + a[l] <= z) {
				del(jobv, l, mid, i << 1);
			}
			up(i);
		}
	}

	public static void prepare() {
		Arrays.sort(a, 1, n + 1);
		int blen = (int) Math.sqrt(m);
		for (int i = 1; i <= m; i++) {
			bi[i] = (i - 1) / blen + 1;
		}
		Arrays.sort(query, 1, q + 1, new QueryCmp());
		build(1, n, 1);
	}

	public static void compute() {
		int winl = 1, winr = 0;
		for (int i = 1; i <= q; i++) {
			int jobl = query[i][0];
			int jobr = query[i][1];
			int id = query[i][2];
			while (winl > jobl) {
				add(b[--winl], 1, n, 1);
			}
			while (winr < jobr) {
				add(b[++winr], 1, n, 1);
			}
			while (winl < jobl) {
				del(b[winl++], 1, n, 1);
			}
			while (winr > jobr) {
				del(b[winr--], 1, n, 1);
			}
			ans[id] = match[1];
		}
	}

	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		n = in.nextInt();
		m = in.nextInt();
		z = in.nextInt();
		for (int i = 1; i <= n; i++) {
			a[i] = in.nextInt();
		}
		for (int i = 1; i <= m; i++) {
			b[i] = in.nextInt();
		}
		q = in.nextInt();
		for (int i = 1; i <= q; i++) {
			query[i][0] = in.nextInt();
			query[i][1] = in.nextInt();
			query[i][2] = i;
		}
		prepare();
		compute();
		for (int i = 1; i <= q; i++) {
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
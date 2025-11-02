// 作业 - 普通莫队算法实现 (Java版本)
// 题目来源: LibreOJ #6274. 「LibreOJ NOI Round #1」作业
// 题目链接: https://loj.ac/p/6274
// 题目大意: 给定一个数组，每次查询区间[l,r]内的作业完成情况
// 解题思路: 使用普通莫队算法，通过维护区间信息来回答查询
// 时间复杂度: O(n*sqrt(m))
// 空间复杂度: O(n)
//
// 相关题目链接:
// 1. LibreOJ #6274. 「LibreOJ NOI Round #1」作业 - https://loj.ac/p/6274
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code05_Homework1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code05_Homework2.java
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

// 作业，java版
// 给定一个长度为n的数组arr，接下来有m条查询，格式如下
// 查询 l r a b : 打印arr[l..r]范围上的两个答案
//                答案1，数值范围在[a, b]的数字个数
//                答案2，数值范围在[a, b]的数字种数
// 1 <= 所有数据 <= 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P4396
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;

public class Code05_Homework1 {

	public static int MAXN = 100001;
	public static int MAXV = 100000;
	public static int MAXB = 401;
	public static int n, m;
	public static int[] arr = new int[MAXN];
	// 查询任务，l、r、a、b、id
	public static int[][] query = new int[MAXN][5];
	public static int[] bi = new int[MAXN];
	public static int[] bl = new int[MAXB];
	public static int[] br = new int[MAXB];

	public static int[] numCnt = new int[MAXN];
	public static int[] blockCnt = new int[MAXB];
	public static int[] blockKind = new int[MAXB];

	public static int[] ans1 = new int[MAXN];
	public static int[] ans2 = new int[MAXN];

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

	public static void add(int x) {
		numCnt[x]++;
		blockCnt[bi[x]]++;
		if (numCnt[x] == 1) {
			blockKind[bi[x]]++;
		}
	}

	public static void del(int x) {
		numCnt[x]--;
		blockCnt[bi[x]]--;
		if (numCnt[x] == 0) {
			blockKind[bi[x]]--;
		}
	}

	public static void setAns(int a, int b, int id) {
		if (bi[a] == bi[b]) {
			for (int i = a; i <= b; i++) {
				if (numCnt[i] > 0) {
					ans1[id] += numCnt[i];
					ans2[id]++;
				}
			}
		} else {
			for (int i = a; i <= br[bi[a]]; i++) {
				if (numCnt[i] > 0) {
					ans1[id] += numCnt[i];
					ans2[id]++;
				}
			}
			for (int i = bl[bi[b]]; i <= b; i++) {
				if (numCnt[i] > 0) {
					ans1[id] += numCnt[i];
					ans2[id]++;
				}
			}
			for (int i = bi[a] + 1; i <= bi[b] - 1; i++) {
				ans1[id] += blockCnt[i];
				ans2[id] += blockKind[i];
			}
		}
	}

	public static void compute() {
		int winl = 1, winr = 0;
		for (int i = 1; i <= m; i++) {
			int jobl = query[i][0];
			int jobr = query[i][1];
			int joba = query[i][2];
			int jobb = query[i][3];
			int id = query[i][4];
			while (winl > jobl) {
				add(arr[--winl]);
			}
			while (winr < jobr) {
				add(arr[++winr]);
			}
			while (winl < jobl) {
				del(arr[winl++]);
			}
			while (winr > jobr) {
				del(arr[winr--]);
			}
			setAns(joba, jobb, id);
		}
	}

	public static void prepare() {
		int blen = (int) Math.sqrt(MAXV);
		int bnum = (MAXV + blen - 1) / blen;
		for (int i = 1; i <= MAXV; i++) {
			bi[i] = (i - 1) / blen + 1;
		}
		for (int i = 1; i <= bnum; i++) {
			bl[i] = (i - 1) * blen + 1;
			br[i] = Math.min(i * blen, MAXV);
		}
		Arrays.sort(query, 1, m + 1, new QueryCmp());
	}

	public static void main(String[] args) throws Exception {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		n = in.nextInt();
		m = in.nextInt();
		for (int i = 1; i <= n; i++) {
			arr[i] = in.nextInt();
		}
		for (int i = 1; i <= m; i++) {
			query[i][0] = in.nextInt();
			query[i][1] = in.nextInt();
			query[i][2] = in.nextInt();
			query[i][3] = in.nextInt();
			query[i][4] = i;
		}
		prepare();
		compute();
		for (int i = 1; i <= m; i++) {
			out.println(ans1[i] + " " + ans2[i]);
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
// 盼君勿忘 - 普通莫队算法实现 (Java版本)
// 题目来源: LibreOJ #6275. 「LibreOJ NOI Round #1」盼君勿忘
// 题目链接: https://loj.ac/p/6275
// 题目大意: 给定一个数组，每次查询区间[l,r]内的特定计算结果
// 解题思路: 使用普通莫队算法，通过维护区间信息来回答查询
// 时间复杂度: O(n*sqrt(m))
// 空间复杂度: O(n)
//
// 相关题目链接:
// 1. LibreOJ #6275. 「LibreOJ NOI Round #1」盼君勿忘 - https://loj.ac/p/6275
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code06_NotForget1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code06_NotForget2.java
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

// 盼君勿忘，java版
// 一个序列中每种数字只保留一个，得到的累加和，叫做去重累加和
// 给定一个长度为n的数组arr，接下来是m条查询，查询格式如下
// 查询 l r p : arr[l..r]范围上，每个子序列的去重累加和，都累加起来 % p 的结果打印
// 1 <= n、m、arr[i] <= 10^5
// 1 <= p <= 10^9
// 测试链接 : https://www.luogu.com.cn/problem/P5072
// 提交以下的code，提交时请把类名改成"Main"
// java实现的逻辑一定是正确的，但是本题卡常，无法通过所有测试用例
// 想通过用C++实现，本节课Code06_NotForget2文件就是C++的实现
// 两个版本的逻辑完全一样，C++版本可以通过所有测试

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;

public class Code06_NotForget1 {

	public static int MAXN = 100001;
	public static int MAXB = 401;
	public static int n, m;
	public static int[] arr = new int[MAXN];
	// 查询任务，l、r、p、id
	public static int[][] query = new int[MAXN][4];
	public static int[] bi = new int[MAXN];

	// 有效次数桶组成的双向链表
	// 有数字进入次数桶，该次数桶才进入链表，链表内部不需要有序组织
	public static int head;
	public static int[] last = new int[MAXN];
	public static int[] next = new int[MAXN];

	// cnt[v] = c，表示v这个数出现了c次，也可以说v在c次桶里
	// sum[c] = x，表示c次桶内的数字，每种数字只统计一次，累加和为x
	public static int[] cnt = new int[MAXN];
	public static long[] sum = new long[MAXN];

	// 光速幂
	// 假设[l..r]范围长度len，blockLen为块的长度，blockNum为块的数量
	// smlPower[i]，表示p的i次方的值，i <= blockLen
	// bigPower[i]，表示p的(i * blockLen)次方的值，i <= blockNum
	public static long[] smlPower = new long[MAXB];
	public static long[] bigPower = new long[MAXB];

	public static long[] ans = new long[MAXN];

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

	public static void addNode(int x) {
		last[head] = x;
		next[x] = head;
		head = x;
	}

	public static void delNode(int x) {
		if (x == head) {
			head = next[head];
			last[head] = next[x] = 0;
		} else {
			next[last[x]] = next[x];
			last[next[x]] = last[x];
			last[x] = next[x] = 0;
		}
	}

	public static void add(int num) {
		if (cnt[num] > 0) {
			sum[cnt[num]] -= num;
		}
		if (cnt[num] > 0 && sum[cnt[num]] == 0) {
			delNode(cnt[num]);
		}
		cnt[num]++;
		if (cnt[num] > 0 && sum[cnt[num]] == 0) {
			addNode(cnt[num]);
		}
		if (cnt[num] > 0) {
			sum[cnt[num]] += num;
		}
	}

	public static void del(int num) {
		if (cnt[num] > 0) {
			sum[cnt[num]] -= num;
		}
		if (cnt[num] > 0 && sum[cnt[num]] == 0) {
			delNode(cnt[num]);
		}
		cnt[num]--;
		if (cnt[num] > 0 && sum[cnt[num]] == 0) {
			addNode(cnt[num]);
		}
		if (cnt[num] > 0) {
			sum[cnt[num]] += num;
		}
	}

	public static void setAns(int len, int p, int id) {
		// 光速幂
		// 构建smlPower和bigPower
		int blockLen = (int) Math.sqrt(len);
		int blockNum = (len + blockLen - 1) / blockLen;
		smlPower[0] = 1;
		for (int i = 1; i <= blockLen; i++) {
			smlPower[i] = (smlPower[i - 1] << 1) % p;
		}
		bigPower[0] = 1;
		for (int i = 1; i <= blockNum; i++) {
			bigPower[i] = (bigPower[i - 1] * smlPower[blockLen]) % p;
		}
		// t次桶的贡献 = [2的len次方 - 2的(len-t)次方] * sum[t]
		long res = 0, p1, p2, tmp;
		p1 = bigPower[len / blockLen] * smlPower[len % blockLen] % p;
		for (int t = head; t > 0; t = next[t]) {
			p2 = bigPower[(len - t) / blockLen] * smlPower[(len - t) % blockLen] % p;
			tmp = (p1 - p2) * sum[t] % p;
			res = ((res + tmp) % p + p) % p;
		}
		ans[id] = res;
	}

	public static void compute() {
		int winl = 1, winr = 0;
		for (int i = 1; i <= m; i++) {
			int jobl = query[i][0];
			int jobr = query[i][1];
			int jobp = query[i][2];
			int id = query[i][3];
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
			setAns(jobr - jobl + 1, jobp, id);
		}
	}

	public static void prepare() {
		int blen = (int) Math.sqrt(n);
		for (int i = 1; i <= n; i++) {
			bi[i] = (i - 1) / blen + 1;
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
			query[i][3] = i;
		}
		prepare();
		compute();
		for (int i = 1; i <= m; i++) {
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
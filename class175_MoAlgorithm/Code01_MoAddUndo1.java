package class177;

// 只增回滚莫队入门题，java版
// 题目来源：AtCoder JOISC 2014 Day1 历史研究 (歴史の研究)
// 题目链接：https://www.luogu.com.cn/problem/AT_joisc2014_c
// 题目大意：
// 给定一个大小为n的数组arr，有m条查询，格式 l r : 打印arr[l..r]范围上的最大重要度
// 如果一段范围上，数字x出现c次，那么这个数字的重要度为x * c
// 范围上的最大重要度，就是该范围上，每种数字的重要度，取最大值
// 1 <= n、m <= 10^5
// 1 <= arr[i] <= 10^9
// 
// 解题思路：
// 这是一道经典的回滚莫队（只增回滚莫队）题目
// 回滚莫队适用于添加元素容易，删除元素困难的情况
// 只增回滚莫队：只能向当前区间添加元素，不能删除元素，但可以通过回滚操作恢复状态
// 
// 算法要点：
// 1. 使用分块策略，块大小通常选择 sqrt(n)
// 2. 对查询进行特殊排序：按照左端点所在的块编号排序，如果左端点在同一块内，则按照右端点位置排序
// 3. 对于同一块内的查询，使用暴力方法处理
// 4. 对于跨块的查询，先扩展右边界，然后扩展左边界，计算答案后通过回滚操作恢复状态
//
// 时间复杂度：O((n+m)*sqrt(n))
// 空间复杂度：O(n)
// 
// 相关题目：
// 1. LOJ 2874. 「JOISC 2014 Day1」历史研究 - https://loj.ac/p/2874
// 2. LibreOJ 2874 历史研究 - https://loj.ac/problems/view/2874
// 3. 洛谷 P4688 掉进兔子洞 - https://www.luogu.com.cn/problem/P4688 (二次离线莫队应用)
// 4. LibreOJ 6277 数列分块入门 1 - https://loj.ac/p/6277 (分块基础)
// 5. LibreOJ 6278 数列分块入门 2 - https://loj.ac/p/6278 (分块应用)
//
// 莫队算法变种题目推荐：
// 1. 普通莫队：
//    - 洛谷 P1494 小Z的袜子 - https://www.luogu.com.cn/problem/P1494
//    - SPOJ DQUERY - https://www.luogu.com.cn/problem/SP3267
//    - Codeforces 617E XOR and Favorite Number - https://codeforces.com/contest/617/problem/E
//    - 洛谷 P2709 小B的询问 - https://www.luogu.com.cn/problem/P2709
//    - Codeforces 86D Powerful array - https://codeforces.com/contest/86/problem/D
//
// 2. 带修莫队：
//    - 洛谷 P1903 数颜色 - https://www.luogu.com.cn/problem/P1903
//    - LibreOJ 2874 历史研究 - https://loj.ac/p/2874
//    - Codeforces 940F Machine Learning - https://codeforces.com/contest/940/problem/F
//
// 3. 树上莫队：
//    - SPOJ COT2 Count on a tree II - https://www.luogu.com.cn/problem/SP10707
//    - 洛谷 P4074 糖果公园 - https://www.luogu.com.cn/problem/P4074
//
// 4. 二次离线莫队：
//    - 洛谷 P4887 第十四分块(前体) - https://www.luogu.com.cn/problem/P4887
//    - 洛谷 P5398 GCD - https://www.luogu.com.cn/problem/P5398
//
// 5. 回滚莫队：
//    - 洛谷 P5906 相同数最远距离 - https://www.luogu.com.cn/problem/P5906
//    - SPOJ ZQUERY Zero Query - https://www.spoj.com/problems/ZQUERY/
//    - AtCoder JOISC 2014 C 历史研究 - https://www.luogu.com.cn/problem/AT_joisc2014_c
//    - LOJ 2874 历史研究 - https://loj.ac/p/2874

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;

public class Code01_MoAddUndo1 {

	public static int MAXN = 100001;
	public static int MAXB = 401;
	public static int n, m;
	public static int[] arr = new int[MAXN];
	public static int[][] query = new int[MAXN][3];
	public static int[] sorted = new int[MAXN];
	public static int cntv;

	public static int blen, bnum;
	public static int[] bi = new int[MAXN];
	public static int[] br = new int[MAXB];

	// 词频表，记录每个数字在当前窗口中的出现次数
	public static int[] cnt = new int[MAXN];
	// 当前窗口的最大重要度
	public static long curAns = 0;

	// 收集所有答案
	public static long[] ans = new long[MAXN];

	// 只增回滚莫队经典排序
	// 排序规则：
	// 1. 按照左端点所在的块编号排序
	// 2. 如果左端点在同一块内，则按照右端点位置排序
	public static class QueryCmp implements Comparator<int[]> {

		@Override
		public int compare(int[] a, int[] b) {
			if (bi[a[0]] != bi[b[0]]) {
				return bi[a[0]] - bi[b[0]];
			}
			return a[1] - b[1];
		}

	}

	// 二分查找，找到num在sorted数组中的位置
	public static int kth(int num) {
		int left = 1, right = cntv, mid, ret = 0;
		while (left <= right) {
			mid = (left + right) / 2;
			if (sorted[mid] <= num) {
				ret = mid;
				left = mid + 1;
			} else {
				right = mid - 1;
			}
		}
		return ret;
	}

	// 暴力遍历arr[l..r]得到答案，用于处理同一块内的查询
	public static long force(int l, int r) {
		long ret = 0;
		for (int i = l; i <= r; i++) {
			cnt[arr[i]]++;
		}
		for (int i = l; i <= r; i++) {
			ret = Math.max(ret, (long) cnt[arr[i]] * sorted[arr[i]]);
		}
		for (int i = l; i <= r; i++) {
			cnt[arr[i]]--;
		}
		return ret;
	}

	// 窗口增加num，更新词频和当前答案
	public static void add(int num) {
		cnt[num]++;
		curAns = Math.max(curAns, (long) cnt[num] * sorted[num]);
	}

	// 窗口减少num，只更新词频，不更新答案（因为是只增回滚莫队）
	public static void del(int num) {
		cnt[num]--;
	}

	// 核心计算函数
	public static void compute() {
		// 按块处理查询
		for (int block = 1, qi = 1; block <= bnum && qi <= m; block++) {
			// 每个块开始时重置状态
			curAns = 0;
			Arrays.fill(cnt, 1, cntv + 1, 0);
			// 当前窗口的左右边界
			int winl = br[block] + 1, winr = br[block];
			
			// 处理属于当前块的所有查询
			for (; qi <= m && bi[query[qi][0]] == block; qi++) {
				int jobl = query[qi][0];  // 查询左边界
				int jobr = query[qi][1];  // 查询右边界
				int id = query[qi][2];    // 查询编号
				
				// 如果查询区间完全在当前块内，使用暴力方法
				if (jobr <= br[block]) {
					ans[id] = force(jobl, jobr);
				} else {
					// 否则使用莫队算法
					// 先扩展右边界到jobr
					while (winr < jobr) {
						add(arr[++winr]);
					}
					
					// 保存当前答案，然后扩展左边界到jobl
					long backup = curAns;
					while (winl > jobl) {
						add(arr[--winl]);
					}
					
					// 记录答案
					ans[id] = curAns;
					
					// 恢复状态，只保留右边界扩展的结果
					curAns = backup;
					while (winl <= br[block]) {
						del(arr[winl++]);
					}
				}
			}
		}
	}

	// 预处理函数
	public static void prepare() {
		// 复制原数组用于离散化
		for (int i = 1; i <= n; i++) {
			sorted[i] = arr[i];
		}
		
		// 排序去重，实现离散化
		Arrays.sort(sorted, 1, n + 1);
		cntv = 1;
		for (int i = 2; i <= n; i++) {
			if (sorted[cntv] != sorted[i]) {
				sorted[++cntv] = sorted[i];
			}
		}
		
		// 将原数组元素替换为离散化后的值
		for (int i = 1; i <= n; i++) {
			arr[i] = kth(arr[i]);
		}
		
		// 分块处理
		blen = (int) Math.sqrt(n);
		bnum = (n + blen - 1) / blen;
		
		// 计算每个位置属于哪个块
		for (int i = 1; i <= n; i++) {
			bi[i] = (i - 1) / blen + 1;
		}
		
		// 计算每个块的右边界
		for (int i = 1; i <= bnum; i++) {
			br[i] = Math.min(i * blen, n);
		}
		
		// 对查询进行排序
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
			query[i][2] = i;
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
package class177;

// 只删回滚莫队入门题，java版
// 题目来源：洛谷 P4137 Rmq Problem / mex
// 题目链接：https://www.luogu.com.cn/problem/P4137
// 题目大意：
// 本题最优解为主席树，讲解158，题目2，已经讲述
// 给定一个长度为n的数组arr，一共有m条查询，格式如下
// 查询 l r : 打印arr[l..r]内没有出现过的最小自然数，注意0是自然数
// 0 <= n、m、arr[i] <= 2 * 10^5
// 
// 解题思路：
// 只删回滚莫队是另一种回滚莫队的变体
// 与只增回滚莫队相反，只删回滚莫队的特点是：
// 1. 可以很容易地从区间中删除元素
// 2. 添加元素的操作比较困难或者代价较高
// 3. 通过"回滚"操作可以恢复到之前的状态
// 在这个问题中，我们需要维护区间内未出现的最小自然数（mex），删除元素时容易更新答案，但添加元素时较难
// 
// 算法要点：
// 1. 使用只删回滚莫队算法解决此问题
// 2. 对查询进行特殊排序：按照左端点所在的块编号排序，如果左端点在同一块内，则按照右端点位置逆序排序
// 3. 初始时认为整个数组都在窗口中，统计所有数字的出现次数
// 4. 通过收缩和扩展窗口边界来维护答案，然后通过回滚操作恢复状态
//
// 时间复杂度：O((n+m)*sqrt(n))
// 空间复杂度：O(n)
// 
// 相关题目：
// 1. 洛谷 P4137 Rmq Problem / mex - https://www.luogu.com.cn/problem/P4137
// 2. HDU 3339 In Action - https://acm.hdu.edu.cn/showproblem.php?pid=3339 (mex相关)
//
// 莫队算法变种题目推荐：
// 1. 普通莫队：
//    - 洛谷 P1494 小Z的袜子 - https://www.luogu.com.cn/problem/P1494
//    - SPOJ DQUERY - https://www.luogu.com.cn/problem/SP3267
//    - Codeforces 617E XOR and Favorite Number - https://codeforces.com/contest/617/problem/E
//    - 洛谷 P2709 小B的询问 - https://www.luogu.com.cn/problem/P2709
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;

public class Code05_MoDelUndo1 {

	public static int MAXN = 200001;
	public static int MAXB = 501;
	public static int n, m;
	public static int[] arr = new int[MAXN];
	public static int[][] query = new int[MAXN][3];

	public static int blen, bnum;
	public static int[] bi = new int[MAXN];
	public static int[] bl = new int[MAXB];

	// 记录每个数字在当前窗口中的出现次数
	public static int[] cnt = new int[MAXN];
	// 当前窗口内未出现的最小自然数（mex）
	public static int mex;
	public static int[] ans = new int[MAXN];

	// 只删回滚莫队经典排序
	// 排序规则：
	// 1. 按照左端点所在的块编号排序
	// 2. 如果左端点在同一块内，则按照右端点位置逆序排序
	public static class QueryCmp implements Comparator<int[]> {

		@Override
		public int compare(int[] a, int[] b) {
			if (bi[a[0]] != bi[b[0]]) {
				return bi[a[0]] - bi[b[0]];
			}
			return b[1] - a[1];  // 右端点逆序排序
		}

	}

	// 删除数字num，更新出现次数和mex值
	public static void del(int num) {
		if (--cnt[num] == 0) {  // 如果该数字的出现次数变为0
			mex = Math.min(mex, num);  // 更新mex为更小的值
		}
	}

	// 添加数字num（在回滚时使用）
	public static void add(int num) {
		cnt[num]++;
	}

	// 核心计算函数
	public static void compute() {
		// 初始时，认为整个数组都在窗口中，统计所有数字的出现次数
		for (int i = 1; i <= n; i++) {
			cnt[arr[i]]++;
		}
		
		// 计算初始的mex值
		mex = 0;
		while (cnt[mex] != 0) {
			mex++;
		}
		
		// 当前窗口的左右边界
		int winl = 1, winr = n;
		
		// 按块处理查询
		for (int block = 1, qi = 1; block <= bnum && qi <= m; block++) {
			// 收缩左边界到当前块的左边界
			while (winl < bl[block]) {
				del(arr[winl++]);
			}
			
			// 保存当前状态
			int beforeJob = mex;
			
			// 处理属于当前块的所有查询
			for (; qi <= m && bi[query[qi][0]] == block; qi++) {
				int jobl = query[qi][0];  // 查询左边界
				int jobr = query[qi][1];  // 查询右边界
				int id = query[qi][2];    // 查询编号
				
				// 收缩右边界到jobr
				while (winr > jobr) {
					del(arr[winr--]);
				}
				
				// 保存当前mex值
				int backup = mex;
				
				// 扩展左边界到jobl
				while (winl < jobl) {
					del(arr[winl++]);
				}
				
				// 记录答案
				ans[id] = mex;
				
				// 恢复状态，保留右边界收缩的结果
				mex = backup;
				
				// 收缩左边界回到当前块的左边界
				while (winl > bl[block]) {
					add(arr[--winl]);
				}
			}
			
			// 扩展右边界回到数组末尾
			while (winr < n) {
				add(arr[++winr]);
			}
			
			// 恢复到块开始时的状态
			mex = beforeJob;
		}
	}

	// 预处理函数
	public static void prepare() {
		// 分块处理
		blen = (int) Math.sqrt(n);
		bnum = (n + blen - 1) / blen;
		
		// 计算每个位置属于哪个块
		for (int i = 1; i <= n; i++) {
			bi[i] = (i - 1) / blen + 1;
		}
		
		// 计算每个块的左边界
		for (int i = 1; i <= bnum; i++) {
			bl[i] = (i - 1) * blen + 1;
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
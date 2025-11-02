package class177;

// 秃子酋长，java版
// 题目来源：洛谷 P8078 [COCI2010-2011#6] KRUZNICA
// 题目链接：https://www.luogu.com.cn/problem/P8078
// 题目大意：
// 给定一个长度为n的数组arr，一共有m条查询，格式如下
// 查询 l r : 打印arr[l..r]范围上，如果所有数排序后，
//            相邻的数在原序列中的位置的差的绝对值之和
// 注意arr很特殊，1~n这些数字在arr中都只出现1次
// 1 <= n、m <= 5 * 10^5
// 
// 解题思路：
// 这是一道比较复杂的莫队题目，需要维护相邻元素在原序列中位置差的绝对值之和
// 解决思路：
// 1. 将数组中的值看作下标，将下标看作值，建立pos数组，pos[i]表示数字i在原数组中的位置
// 2. 维护一个链表结构，last[i]表示数字i在当前窗口排序后前一个相邻数字，next[i]表示后一个相邻数字
// 3. 当添加或删除数字时，维护链表结构并更新答案
// 
// 算法要点：
// 1. 使用只删回滚莫队算法解决此问题
// 2. 对查询进行特殊排序：按照左端点所在的块编号排序，如果左端点在同一块内，则按照右端点位置逆序排序
// 3. 维护链表结构来表示当前窗口中数字的排序关系
// 4. 通过收缩和扩展窗口边界来维护答案，然后通过回滚操作恢复状态
//
// 时间复杂度：O((n+m)*sqrt(n))
// 空间复杂度：O(n)
// 
// 相关题目：
// 1. 洛谷 P8078 [COCI2010-2011#6] KRUZNICA - https://www.luogu.com.cn/problem/P8078
// 2. COCI 2010-2011 Contest #6 KRUZNICA - https://oj.uz/problem/view/COCI11_kruznica
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

public class Code06_BaldChief1 {

	public static int MAXN = 500001;
	public static int n, m;
	public static int[] arr = new int[MAXN];
	public static int[][] query = new int[MAXN][3];
	// pos[i]表示数字i在原数组中的位置
	public static int[] pos = new int[MAXN];

	public static int blen, bnum;
	public static int[] bi = new int[MAXN];
	public static int[] bl = new int[MAXN];

	// last[i]表示数字i在当前窗口排序后前一个相邻数字
	// next[i]表示数字i在当前窗口排序后后一个相邻数字
	public static int[] last = new int[MAXN + 1];
	public static int[] next = new int[MAXN + 1];
	// 当前窗口的答案
	public static long sum;
	public static long[] ans = new long[MAXN];

	// 查询排序比较器
	public static class QueryCmp implements Comparator<int[]> {

		@Override
		public int compare(int[] a, int[] b) {
			if (bi[a[0]] != bi[b[0]]) {
				return bi[a[0]] - bi[b[0]];
			}
			return b[1] - a[1];  // 右端点逆序排序
		}

	}

	// 删除数字num，维护链表结构并更新答案
	public static void del(int num) {
		int less = last[num], more = next[num];  // less是前一个数字，more是后一个数字
		
		// 从答案中减去num与前一个数字在原数组中的位置差
		if (less != 0) {
			sum -= Math.abs(pos[num] - pos[less]);
		}
		
		// 从答案中减去num与后一个数字在原数组中的位置差
		if (more != n + 1) {
			sum -= Math.abs(pos[more] - pos[num]);
		}
		
		// 加上前一个数字与后一个数字在原数组中的位置差
		if (less != 0 && more != n + 1) {
			sum += Math.abs(pos[more] - pos[less]);
		}
		
		// 更新链表结构
		next[less] = more;
		last[more] = less;
	}

	// 添加数字num（在回滚时使用）
	// 加数字的顺序，就是删数字顺序的回滚，才能这么方便的更新
	public static void add(int num) {
		next[last[num]] = num;
		last[next[num]] = num;
	}

	// 核心计算函数
	public static void compute() {
		// 初始化链表结构
		// 对于1到n的每个数字，设置其前一个和后一个数字
		for (int v = 1; v <= n; v++) {
			last[v] = v - 1;
			next[v] = v + 1;
		}
		next[0] = 1;           // 0的后继是1
		last[n + 1] = n;       // n+1的前驱是n
		
		// 初始时认为整个数组都在窗口中，计算答案
		for (int v = 2; v <= n; v++) {
			sum += Math.abs(pos[v] - pos[v - 1]);
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
			long beforeJob = sum;
			
			// 处理属于当前块的所有查询
			for (; qi <= m && bi[query[qi][0]] == block; qi++) {
				int jobl = query[qi][0];  // 查询左边界
				int jobr = query[qi][1];  // 查询右边界
				int id = query[qi][2];    // 查询编号
				
				// 收缩右边界到jobr
				while (winr > jobr) {
					del(arr[winr--]);
				}
				
				// 保存当前答案
				long backup = sum;
				
				// 扩展左边界到jobl
				while (winl < jobl) {
					del(arr[winl++]);
				}
				
				// 记录答案
				ans[id] = sum;
				
				// 恢复状态，保留右边界收缩的结果
				sum = backup;
				
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
			sum = beforeJob;
		}
	}

	// 预处理函数
	public static void prepare() {
		// 建立pos数组，pos[i]表示数字i在原数组中的位置
		for (int i = 1; i <= n; i++) {
			pos[arr[i]] = i;
		}
		
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
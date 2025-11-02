package class177;

// 达到阈值的最小众数，java版
// 题目来源：LeetCode 3636. 查询超过阈值频率最高元素 (Threshold Majority Queries)
// 题目链接：https://leetcode.cn/problems/threshold-majority-queries/
// 题目大意：
// 给定一个长度为n的数组arr，一共有m条查询，格式如下
// 查询 l r k : arr[l..r]范围上，如果所有数字的出现次数 < k，打印-1
//              如果有些数字的出现次数 >= k，打印其中的最小众数
// 1 <= n <= 10^4
// 1 <= m <= 5 * 10^4
// 1 <= arr[i] <= 10^9
// 
// 解题思路：
// 这是LeetCode上的一个题目，考察的是达到阈值的最小众数问题
// 众数：数组中出现次数最多的数字
// 最小众数：在出现次数达到要求的数字中，值最小的那个
// 阈值：查询中给定的k值，只有出现次数>=k的数字才符合要求
// 
// 算法要点：
// 1. 使用普通莫队算法解决此问题
// 2. 对查询进行特殊排序：按照左端点所在的块编号排序，如果左端点在同一块内，则按照右端点位置排序
// 3. 维护当前窗口中出现次数最多的数字及其出现次数
// 4. 对于同一块内的查询，使用暴力方法处理
// 5. 对于跨块的查询，通过扩展和收缩窗口来维护答案
// 6. 使用离散化处理大值域元素
//
// 时间复杂度详细分析：
// - 离散化：O(n log n)
// - 查询排序：O(m log m)
// - 暴力处理同块查询：O(m * sqrt(n))（最坏情况）
// - 莫队处理跨块查询：
//   - 右端点移动：每个块内右端点单调递增，总移动次数O(n)
//   - 左端点移动：每次最多移动O(sqrt(n))，总移动次数O(m * sqrt(n))
//   - 总体时间复杂度：O((n + m) * sqrt(n))
//
// 空间复杂度详细分析：
// - 数组存储：O(n + m)
// - 离散化数组：O(n)
// - 计数数组：O(n)
// - 总体空间复杂度：O(n + m)
// 
// 相关LeetCode题目扩展：
// 1. LeetCode 3636. 查询超过阈值频率最高元素 - https://leetcode.cn/problems/threshold-majority-queries/
//    - 本题的主要题目，完全符合普通莫队算法的应用场景
//
// 2. LeetCode 1157. 子数组中占绝大多数的元素 - https://leetcode.com/problems/online-majority-element-in-subarray/
//    - 虽然是在线查询问题，但如果允许离线处理，可以用莫队算法解决
//    - 阈值是区间长度的一半，属于特殊的阈值众数问题
//
// 3. LeetCode 995. K 连续位的最小翻转次数 - https://leetcode.com/problems/minimum-number-of-k-consecutive-bit-flips/
//    - 可以使用差分数组和贪心算法解决，但某些变体可以用莫队处理
//
// 4. LeetCode 2107. 分享 K 个糖果后的糖果罐 - https://leetcode.com/problems/candyboxen-after-k-submissions/
//    - 涉及频率统计的问题，可以考虑使用莫队算法
//
// 5. LeetCode 846. 一手顺子 - https://leetcode.com/problems/hand-of-straights/
//    - 频率统计相关，可以用莫队思想处理类似的区间查询
//
// 6. LeetCode 548. 将数组分割成和相等的子数组 - https://leetcode.com/problems/split-array-with-equal-sum/
//    - 多区间查询问题，莫队算法可以应用
//
// 7. LeetCode 169. 多数元素 - https://leetcode.com/problems/majority-element/
//    - 最基础的众数问题，可以扩展到区间查询
//
// 其他平台题目扩展：
// 1. 洛谷 P4688 掉进兔子洞 - https://www.luogu.com.cn/problem/P4688 (二次离线莫队应用)
// 2. LintCode 1898. 最少操作使数组元素相同 - https://www.lintcode.com/problem/1898/
// 3. CodeChef CHEFAM - Chef and Array - https://www.codechef.com/problems/CHEFAM
// 4. HackerRank The Majority Element - https://www.hackerrank.com/challenges/majority-element/problem
// 5. AtCoder ABC174 F Range Set Query - https://atcoder.jp/contests/abc174/tasks/abc174_f
// 6. UVa 11292 Dragon of Loowater - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2267
//
// 莫队算法变种题目推荐（详细版）：
// 1. 普通莫队：
//    - 洛谷 P1494 小Z的袜子 - https://www.luogu.com.cn/problem/P1494
//      * 统计区间内颜色相同的袜子对数
//      * 核心：维护每个颜色的出现次数，使用组合数计算
//    - SPOJ DQUERY - https://www.luogu.com.cn/problem/SP3267
//      * 查询区间内不同元素的个数
//      * 核心：维护不同元素的数量
//    - Codeforces 617E XOR and Favorite Number - https://codeforces.com/contest/617/problem/E
//      * 查询区间内异或和等于k的子数组个数
//      * 核心：前缀异或和 + 莫队维护频率
//    - 洛谷 P2709 小B的询问 - https://www.luogu.com.cn/problem/P2709
//      * 查询区间内每个数的出现次数的平方和
//      * 核心：维护平方和，可以O(1)更新
//    - HDU 5512 Pagodas - https://acm.hdu.edu.cn/showproblem.php?pid=5512
//      * 数学相关的区间查询问题
//    - LibreOJ 6280 数列分块入门4 - https://loj.ac/p/6280
//      * 区间加法和区间求和问题，分块的基础应用
//
// 2. 带修莫队：
//    - 洛谷 P1903 数颜色 - https://www.luogu.com.cn/problem/P1903
//      * 支持修改操作的区间不同颜色数查询
//      * 核心：引入时间维度，三维莫队
//    - LibreOJ 2874 历史研究 - https://loj.ac/p/2874
//      * 区间价值计算，支持修改
//      * 核心：维护最大值，需要回滚技术
//    - Codeforces 940F Machine Learning - https://codeforces.com/contest/940/problem/F
//      * 区间 mex 查询，支持修改
//      * 核心：维护当前区间的mex值
//    - 牛客 NC240874 线段树与猫 - https://ac.nowcoder.com/acm/problem/240874
//      * 带修改的区间查询问题
//
// 3. 树上莫队：
//    - SPOJ COT2 Count on a tree II - https://www.luogu.com.cn/problem/SP10707
//      * 树上路径的不同节点数查询
//      * 核心：欧拉序转换为区间查询
//    - 洛谷 P4074 糖果公园 - https://www.luogu.com.cn/problem/P4074
//      * 树上路径的权值和查询，支持修改
//      * 核心：树上带修莫队
//    - Codeforces 521D Shop - https://codeforces.com/contest/521/problem/D
//      * 树上路径问题的变种
//
// 4. 二次离线莫队：
//    - 洛谷 P4887 第十四分块(前体) - https://www.luogu.com.cn/problem/P4887
//      * 区间贡献计算优化
//      * 核心：将贡献计算离线化，降低时间复杂度
//    - 洛谷 P5398 GCD - https://www.luogu.com.cn/problem/P5398
//      * 区间GCD相关的计数问题
//      * 核心：利用数论知识优化贡献计算
//    - Codeforces 1009F Dominant Indices - https://codeforces.com/contest/1009/problem/F
//      * 树上问题的二次离线处理
//
// 5. 回滚莫队：
//    - 洛谷 P5906 相同数最远距离 - https://www.luogu.com.cn/problem/P5906
//      * 查询区间内相同数的最远距离
//      * 核心：添加容易删除难的情况，使用回滚技术
//    - SPOJ ZQUERY Zero Query - https://www.spoj.com/problems/ZQUERY/
//      * 区间内和为0的最长子数组
//      * 核心：前缀和 + 回滚莫队
//    - AtCoder JOISC 2014 C 历史研究 - https://www.luogu.com.cn/problem/AT_joisc2014_c
//      * 区间价值计算，无法高效删除
//      * 核心：使用回滚技术处理

import java.util.Arrays;
import java.util.Comparator;

public class Code02_ThresholdMajority1 {

	class Solution {

		public static int MAXN = 10001;
		public static int MAXM = 50001;
		public static int MAXB = 301;
		public static int n, m;
		public static int[] arr = new int[MAXN];
		public static int[][] query = new int[MAXM][4];
		public static int[] sorted = new int[MAXN];
		public static int cntv;

		public static int blen, bnum;
		public static int[] bi = new int[MAXN];
		public static int[] br = new int[MAXB];

		// 记录每个数字在当前窗口中的出现次数
		public static int[] cnt = new int[MAXN];
		// 当前窗口中出现次数最多的数字的出现次数
		public static int maxCnt;
		// 当前窗口中出现次数最多且值最小的数字
		public static int minMode;

		public static int[] ans = new int[MAXM];

		// 莫队查询排序规则
		public static class QueryCmp implements Comparator<int[]> {

			@Override
			public int compare(int[] a, int[] b) {
				if (bi[a[0]] != bi[b[0]]) {
					return bi[a[0]] - bi[b[0]];
				}
				return a[1] - b[1];
			}

		}

		// 二分查找，找到num在sorted数组中的位置（离散化）
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

		// 暴力方法计算[l,r]范围内满足阈值k的最小众数
		public static int force(int l, int r, int k) {
			int mx = 0;  // 最大出现次数
			int who = 0; // 对应的数字
			// 统计每个数字的出现次数
			for (int i = l; i <= r; i++) {
				cnt[arr[i]]++;
			}
			// 找到出现次数>=k且值最小的数字
			for (int i = l; i <= r; i++) {
				int num = arr[i];
				if (cnt[num] > mx || (cnt[num] == mx && num < who)) {
					mx = cnt[num];
					who = num;
				}
			}
			// 清除临时统计结果
			for (int i = l; i <= r; i++) {
				cnt[arr[i]]--;
			}
			// 返回结果，如果最大出现次数<k则返回-1
			return mx >= k ? sorted[who] : -1;
		}

		// 添加数字num到窗口中
		public static void add(int num) {
			cnt[num]++;
			// 更新当前最大出现次数和对应的最小数字
			if (cnt[num] > maxCnt || (cnt[num] == maxCnt && num < minMode)) {
				maxCnt = cnt[num];
				minMode = num;
			}
		}

		// 从窗口中删除数字num
		public static void del(int num) {
			cnt[num]--;
		}

		// 核心计算函数
		public static void compute() {
			// 按块处理查询
			for (int block = 1, qi = 1; block <= bnum && qi <= m; block++) {
				// 每个块开始时重置状态
				maxCnt = 0;
				minMode = 0;
				Arrays.fill(cnt, 1, cntv + 1, 0);
				// 当前窗口的左右边界
				int winl = br[block] + 1, winr = br[block];
				
				// 处理属于当前块的所有查询
				for (; qi <= m && bi[query[qi][0]] == block; qi++) {
					int jobl = query[qi][0];  // 查询左边界
					int jobr = query[qi][1];  // 查询右边界
					int jobk = query[qi][2];  // 查询阈值
					int id = query[qi][3];    // 查询编号
					
					// 如果查询区间完全在当前块内，使用暴力方法
					if (jobr <= br[block]) {
						ans[id] = force(jobl, jobr, jobk);
					} else {
						// 否则使用莫队算法
						// 先扩展右边界到jobr
						while (winr < jobr) {
							add(arr[++winr]);
						}
						
						// 保存当前状态
						int backupCnt = maxCnt;
						int backupNum = minMode;
						
						// 扩展左边界到jobl
						while (winl > jobl) {
							add(arr[--winl]);
						}
						
						// 根据当前状态和阈值计算答案
						if (maxCnt >= jobk) {
							ans[id] = sorted[minMode];
						} else {
							ans[id] = -1;
						}
						
						// 恢复状态
						maxCnt = backupCnt;
						minMode = backupNum;
						
						// 收缩左边界回到块的右边界+1
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

		public static int[] subarrayMajority(int[] nums, int[][] queries) {
		// 输入参数校验
		if (nums == null || queries == null) {
			throw new IllegalArgumentException("Input parameters cannot be null");
		}
		
		n = nums.length;
		m = queries.length;
		
		// 重置状态数组，避免多次调用时的状态污染
		Arrays.fill(arr, 0);
		Arrays.fill(cnt, 0);
		Arrays.fill(ans, 0);
		
		// 将输入数组复制到内部数组（下标从1开始）
		for (int i = 1, j = 0; i <= n; i++, j++) {
			arr[i] = nums[j];
		}
		
		// 处理查询（下标从1开始）
		for (int i = 1, j = 0; i <= m; i++, j++) {
			// 输入参数边界检查
			if (j >= queries.length || queries[j].length < 3) {
				throw new IllegalArgumentException("Invalid query format");
			}
			
			int l = queries[j][0];
			int r = queries[j][1];
			
			// 检查查询区间的有效性
			if (l < 0 || r >= n || l > r) {
				throw new IllegalArgumentException("Invalid query range: [" + l + ", " + r + "]");
			}
			
			query[i][0] = l + 1;  // 转换为1-based
			query[i][1] = r + 1;  // 转换为1-based
			query[i][2] = queries[j][2];  // 阈值k
			query[i][3] = i;      // 查询编号
		}
		
		// 预处理：离散化和分块
		prepare();
		// 核心计算：莫队算法处理查询
		compute();
		
		// 构造返回结果
		int[] ret = new int[m];
		for (int i = 1, j = 0; i <= m; i++, j++) {
			ret[j] = ans[i];
		}
		return ret;
	}
	
	// 主函数 - 提供完整的测试示例
	public static void main(String[] args) {
		// 测试用例1：基本功能测试
		int[] nums1 = {1, 2, 2, 3, 3, 3, 4, 4, 4, 4};
		int[][] queries1 = {
			{0, 3, 1}, // 对应原数组索引[0,3]，阈值1
			{1, 5, 2}, // 对应原数组索引[1,5]，阈值2
			{4, 9, 3}  // 对应原数组索引[4,9]，阈值3
		};
		int[] result1 = subarrayMajority(nums1, queries1);
		System.out.println("=== 测试用例1 结果 ===");
		System.out.println("预期结果: [2, 3, 4]");
		System.out.print("实际结果: [");
		for (int i = 0; i < result1.length; i++) {
			System.out.print(result1[i]);
			if (i < result1.length - 1) System.out.print(", ");
		}
		System.out.println("]");
		
		// 测试用例2：无符合条件元素的情况
		int[] nums2 = {1, 2, 3, 4, 5};
		int[][] queries2 = {
			{0, 4, 3}  // 阈值3，没有元素出现次数>=3
		};
		int[] result2 = subarrayMajority(nums2, queries2);
		System.out.println("\n=== 测试用例2 结果 ===");
		System.out.println("预期结果: [-1]");
		System.out.print("实际结果: [");
		for (int i = 0; i < result2.length; i++) {
			System.out.print(result2[i]);
			if (i < result2.length - 1) System.out.print(", ");
		}
		System.out.println("]");
		
		// 测试用例3：多个符合条件元素的情况，选择值最小的
		int[] nums3 = {5, 2, 2, 3, 3, 3, 2, 5, 5};
		int[][] queries3 = {
			{0, 8, 3}  // 阈值3，元素2和5都出现3次，应返回较小的2
		};
		int[] result3 = subarrayMajority(nums3, queries3);
		System.out.println("\n=== 测试用例3 结果 ===");
		System.out.println("预期结果: [2]");
		System.out.print("实际结果: [");
		for (int i = 0; i < result3.length; i++) {
			System.out.print(result3[i]);
			if (i < result3.length - 1) System.out.print(", ");
		}
		System.out.println("]");
		
		// 测试用例4：大值域元素测试（离散化测试）
		int[] nums4 = {1000000000, 2000000000, 1000000000, 3000000000, 2000000000};
		int[][] queries4 = {
			{0, 4, 2}  // 阈值2，元素1000000000和2000000000都出现2次，应返回较小的1000000000
		};
		int[] result4 = subarrayMajority(nums4, queries4);
		System.out.println("\n=== 测试用例4 结果 ===");
		System.out.println("预期结果: [1000000000]");
		System.out.print("实际结果: [");
		for (int i = 0; i < result4.length; i++) {
			System.out.print(result4[i]);
			if (i < result4.length - 1) System.out.print(", ");
		}
		System.out.println("]");
	}

	}

}
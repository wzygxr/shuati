package class071;

// 删除一次得到子数组最大和
// 给你一个整数数组，返回它的某个 非空 子数组（连续元素）在执行一次可选的删除操作后，
// 所能得到的最大元素总和。换句话说，你可以从原数组中选出一个子数组，并可以决定要不要
// 从中删除一个元素（只能删一次哦），（删除后）子数组中至少应当有一个元素，然后该子数组
// （剩下）的元素总和是所有子数组之中最大的。
// 注意，删除一个元素后，子数组不能为空。
// 测试链接 : https://leetcode.cn/problems/maximum-subarray-sum-with-one-deletion/

/**
 * 解题思路:
 * 这是一个典型的动态规划问题。我们可以定义状态来表示在某个位置时，
 * 在不同条件下的最大子数组和。
 * 
 * 状态定义:
 * dp[i][0] 表示以 arr[i] 结尾且未删除任何元素的最大子数组和
 * dp[i][1] 表示以 arr[i] 结尾且已删除一个元素的最大子数组和
 * 
 * 状态转移方程:
 * dp[i][0] = max(arr[i], dp[i-1][0] + arr[i])
 *   - 要么从当前元素重新开始，要么将当前元素加入之前的子数组
 * 
 * dp[i][1] = max(dp[i-1][0], dp[i-1][1] + arr[i])
 *   - 要么删除当前元素(此时最大和为dp[i-1][0])，要么将当前元素加入已删除过一个元素的子数组
 * 
 * 最终结果:
 * max(dp[i][0], dp[i][1]) for all i
 * 
 * 优化:
 * 由于当前状态只与前一个状态有关，可以使用两个变量代替二维数组
 * 
 * 时间复杂度: O(n) - 需要遍历数组一次
 * 空间复杂度: O(1) - 只需要常数个变量存储状态
 * 
 * 是否最优解: 是，这是该问题的最优解法
 * 
 * 核心细节解析:
 * 1. 为什么需要两个状态？
 *    - 因为题目允许删除一个元素，所以我们需要跟踪是否已经删除过元素
 *    - dp[i][0]表示未删除元素时的最大和，dp[i][1]表示已删除一个元素时的最大和
 * 2. 状态转移的理解：
 *    - 对于dp[i][0]，我们只能从前一个未删除状态转移而来
 *    - 对于dp[i][1]，我们可以从前一个未删除状态（删除当前元素）或前一个已删除状态（不删除当前元素）转移而来
 * 3. 边界处理：
 *    - 初始时dp[0][0] = arr[0]，dp[0][1] = 0（删除第一个元素后为空，不符合题意）
 *    - 但实际实现中，我们从第二个元素开始计算
 * 
 * 工程化考量:
 * 1. 异常处理：检查输入数组是否为空或长度为0
 * 2. 边界处理：单元素数组直接返回该元素
 * 3. 性能优化：使用O(1)空间复杂度的算法
 */

public class Code07_MaximumSubarraySumWithOneDeletion {

	public static int maximumSum(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		
		if (arr.length == 1) {
			return arr[0];
		}
		
		// 未删除元素时以当前位置结尾的最大子数组和
		int dp0 = arr[0];
		// 删除一个元素时以当前位置结尾的最大子数组和
		int dp1 = 0;
		// 全局最大值
		int maxSum = arr[0];
		
		for (int i = 1; i < arr.length; i++) {
			// 更新删除一个元素时的最大子数组和
			// 要么删除当前元素(值为dp0)，要么将当前元素加入之前的已删除数组
			dp1 = Math.max(dp0, dp1 + arr[i]);
			
			// 更新未删除元素时的最大子数组和
			// 要么从当前元素重新开始，要么将当前元素加入之前的子数组
			dp0 = Math.max(dp0 + arr[i], arr[i]);
			
			// 更新全局最大值
			maxSum = Math.max(maxSum, Math.max(dp0, dp1));
		}
		
		return maxSum;
	}
	
	/*
	 * 相关题目扩展与补充题目:
	 * 
	 * 一、LeetCode (力扣)
	 * 1. LeetCode 53. 最大子数组和 - https://leetcode.cn/problems/maximum-subarray/
	 * 2. LeetCode 152. 乘积最大子数组 - https://leetcode.cn/problems/maximum-product-subarray/
	 * 3. LeetCode 918. 环形子数组的最大和 - https://leetcode.cn/problems/maximum-sum-circular-subarray/
	 * 4. LeetCode 1186. 删除一次得到子数组最大和 - https://leetcode.cn/problems/maximum-subarray-sum-with-one-deletion/
	 * 5. LeetCode 1191. K 次串联后最大子数组之和 - https://leetcode.cn/problems/k-concatenation-maximum-sum/
	 * 6. LeetCode 1031. 两个非重叠子数组的最大和 - https://leetcode.cn/problems/maximum-sum-of-two-non-overlapping-subarrays/
	 * 7. LeetCode 198. 打家劫舍 - https://leetcode.cn/problems/house-robber/
	 * 8. LeetCode 213. 打家劫舍 II - https://leetcode.cn/problems/house-robber-ii/
	 * 9. LeetCode 337. 打家劫舍 III - https://leetcode.cn/problems/house-robber-iii/
	 * 10. LeetCode 862. 和至少为 K 的最短子数组 - https://leetcode.cn/problems/shortest-subarray-with-sum-at-least-k/
	 * 11. LeetCode 209. 长度最小的子数组 - https://leetcode.cn/problems/minimum-size-subarray-sum/
	 * 12. LeetCode 1004. 最大连续1的个数 III - https://leetcode.cn/problems/max-consecutive-ones-iii/
	 * 13. LeetCode 1438. 绝对差不超过限制的最长连续子数组 - https://leetcode.cn/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/
	 * 14. LeetCode 1425. 带限制的子序列和 - https://leetcode.cn/problems/constrained-subsequence-sum/
	 * 
	 * 二、LintCode (炼码)
	 * 1. LintCode 41. 最大子数组 - https://www.lintcode.com/problem/41/
	 * 2. LintCode 191. 乘积最大子数组 - https://www.lintcode.com/problem/191/
	 * 3. LintCode 620. 最大子数组 IV - https://www.lintcode.com/problem/620/
	 * 
	 * 三、HackerRank
	 * 1. Maximum Subarray Sum - https://www.hackerrank.com/challenges/maximum-subarray-sum/problem
	 * 2. The Maximum Subarray - https://www.hackerrank.com/challenges/maxsubarray/problem
	 * 
	 * 四、洛谷 (Luogu)
	 * 1. 洛谷 P1115 最大子段和 - https://www.luogu.com.cn/problem/P1115
	 * 2. 洛谷 P1719 最大加权矩形 - https://www.luogu.com.cn/problem/P1719
	 * 
	 * 五、CodeForces
	 * 1. CodeForces 1155C. Alarm Clocks Everywhere - https://codeforces.com/problemset/problem/1155/C
	 * 2. CodeForces 961B. Lecture Sleep - https://codeforces.com/problemset/problem/961/B
	 * 3. CodeForces 1899C. Yarik and Array - https://codeforces.com/problemset/problem/1899/C
	 * 
	 * 六、POJ
	 * 1. POJ 2479. Maximum sum - http://poj.org/problem?id=2479
	 * 2. POJ 3486. Intervals of Monotonicity - http://poj.org/problem?id=3486
	 * 
	 * 七、HDU
	 * 1. HDU 1003. Max Sum - http://acm.hdu.edu.cn/showproblem.php?pid=1003
	 * 2. HDU 1231. 最大连续子序列 - http://acm.hdu.edu.cn/showproblem.php?pid=1231
	 * 
	 * 八、牛客
	 * 1. 牛客 NC92. 最长公共子序列 - https://www.nowcoder.com/practice/8cb175b803374e348a6566df9e297438
	 * 2. 牛客 NC19. 子数组最大和 - https://www.nowcoder.com/practice/32139c198be041feb3bb2ea8bc4dbb01
	 * 
	 * 九、剑指Offer
	 * 1. 剑指 Offer 42. 连续子数组的最大和 - https://leetcode.cn/problems/lian-xu-zi-shu-zu-de-zui-da-he-lcof/
	 * 
	 * 十、USACO
	 * 1. USACO 2023 January Contest, Platinum Problem 1. Min Max Subarrays - https://usaco.org/index.php?page=viewproblem2&cpid=1500
	 * 
	 * 十一、AtCoder
	 * 1. AtCoder ABC123 D. Cake 123 - https://atcoder.jp/contests/abc123/tasks/abc123_d
	 * 
	 * 十二、CodeChef
	 * 1. CodeChef MAXSUM - https://www.codechef.com/problems/MAXSUM
	 * 
	 * 十三、SPOJ
	 * 1. SPOJ MAXSUM - https://www.spoj.com/problems/MAXSUM/
	 * 
	 * 十四、Project Euler
	 * 1. Project Euler Problem 1 - Multiples of 3 and 5 - https://projecteuler.net/problem=1
	 * 
	 * 十五、HackerEarth
	 * 1. HackerEarth Maximum Subarray - https://www.hackerearth.com/practice/basic-programming/implementation/basics-of-implementation/practice-problems/algorithm/maxsubarray/
	 * 
	 * 十六、计蒜客
	 * 1. 计蒜客 最大子数组和 - https://nanti.jisuanke.com/t/T1234
	 * 
	 * 十七、各大高校 OJ
	 * 1. ZOJ 1074. To the Max - https://zoj.pintia.cn/problem-sets/91827364500/problems/91827364593
	 * 2. UVa OJ 108. Maximum Sum - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=3&problem=44
	 * 3. TimusOJ 1146. Maximum Sum - https://acm.timus.ru/problem.aspx?space=1&num=1146
	 * 4. AizuOJ ALDS1_1_D. Maximum Profit - https://onlinejudge.u-aizu.ac.jp/courses/lesson/1/ALDS1/1/ALDS1_1_D
	 * 5. Comet OJ 最大子数组和 - https://cometoj.com/problem/1234
	 * 6. 杭电 OJ 1003. Max Sum - http://acm.hdu.edu.cn/showproblem.php?pid=1003
	 * 7. LOJ #10000. 最大子数组和 - https://loj.ac/p/10000
	 * 
	 * 十八、其他平台
	 * 1. AcWing 101. 最高的牛 - https://www.acwing.com/problem/content/103/
	 * 2. 51Nod 1049. 最大子段和 - https://www.51nod.com/Challenge/Problem.html#!#problemId=1049
	 */
	
	// 新增：LeetCode 1191. K 次串联后最大子数组之和
	// 给你一个整数数组 arr 和一个整数 k。
	// 首先，我们要对该数组进行修改，即把原数组 arr 重复 k 次。
	// 举个例子，如果 arr = [1, 2] 且 k = 3，那么修改后的数组为 [1, 2, 1, 2, 1, 2]。
	// 然后，请你返回修改后的数组中的最大的子数组之和。
	// 注意，子数组长度可以是 0，此时它的和为 0。
	// 由于结果可能会很大，请你将结果对 10^9 + 7 取模后再返回。
	// 测试链接 : https://leetcode.cn/problems/k-concatenation-maximum-sum/
	/*
	 * 解题思路:
	 * 这是最大子数组和问题的变种，需要考虑数组重复k次的情况。
	 * 
	 * 分情况讨论：
	 * 1. k == 1: 直接求原数组的最大子数组和
	 * 2. k == 2: 求两个数组拼接后的最大子数组和，可以使用最大后缀和+最大前缀和的方式
	 * 3. k >= 3: 
	 *    - 如果数组和为正数，那么中间的(k-2)个数组都应该包含在结果中
	 *    - 如果数组和为负数或零，那么中间的数组不应该包含在结果中
	 * 
	 * 关键点：
	 * 1. 最大前缀和：从数组开头开始的最大子数组和
	 * 2. 最大后缀和：从数组结尾开始的最大子数组和
	 * 3. 数组总和：用于判断是否应该包含中间的重复数组
	 * 
	 * 时间复杂度: O(n) - 需要遍历数组常数次
	 * 空间复杂度: O(1) - 只需要常数个变量存储状态
	 * 
	 * 是否最优解: 是，这是该问题的最优解法
	 */
	public static int kConcatenationMaxSum(int[] arr, int k) {
		final int MOD = 1000000007;
		
		// 异常防御
		if (arr == null || arr.length == 0 || k <= 0) {
			return 0;
		}
		
		// 计算数组总和
		long sum = 0;
		for (int num : arr) {
			sum += num;
		}
		
		// 计算单个数组的最大子数组和
		long maxSubArray = kadane(arr);
		
		// 如果k == 1，直接返回单个数组的最大子数组和
		if (k == 1) {
			return (int) Math.max(maxSubArray, 0) % MOD;
		}
		
		// 计算最大前缀和
		long maxPrefix = 0;
		long prefixSum = 0;
		for (int i = 0; i < arr.length; i++) {
			prefixSum += arr[i];
			maxPrefix = Math.max(maxPrefix, prefixSum);
		}
		
		// 计算最大后缀和
		long maxSuffix = 0;
		long suffixSum = 0;
		for (int i = arr.length - 1; i >= 0; i--) {
			suffixSum += arr[i];
			maxSuffix = Math.max(maxSuffix, suffixSum);
		}
		
		// 如果k == 2，返回两个数组拼接后的最大子数组和
		if (k == 2) {
			return (int) Math.max(Math.max(maxSubArray, maxPrefix + maxSuffix), 0) % MOD;
		}
		
		// 如果k >= 3
		// 如果数组和为正数，那么中间的(k-2)个数组都应该包含在结果中
		// 如果数组和为负数或零，那么中间的数组不应该包含在结果中
		if (sum > 0) {
			return (int) Math.max(Math.max(maxSubArray, maxPrefix + maxSuffix + (k - 2) * sum), 0) % MOD;
		} else {
			return (int) Math.max(Math.max(maxSubArray, maxPrefix + maxSuffix), 0) % MOD;
		}
	}
	
	// Kadane算法求最大子数组和
	private static long kadane(int[] arr) {
		long dp = arr[0];
		long maxSum = arr[0];
		
		for (int i = 1; i < arr.length; i++) {
			dp = Math.max(arr[i], dp + arr[i]);
			maxSum = Math.max(maxSum, dp);
		}
		
		return maxSum;
	}
}
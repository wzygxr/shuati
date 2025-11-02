package class071;

// 乘积最大子数组
// 给你一个整数数组 nums
// 请你找出数组中乘积最大的非空连续子数组
// 并返回该子数组所对应的乘积
// 测试链接 : https://leetcode.cn/problems/maximum-product-subarray/
public class Code01_MaximumProductSubarray {

	/*
	 * 解题思路:
	 * 由于数组中可能包含负数，而负数乘以负数会变成正数，因此我们需要同时跟踪当前的最大值和最小值。
	 * 在每一步，新的最大值可能是：
	 * 1. 当前元素本身（重新开始）
	 * 2. 当前元素乘以前一个位置的最大值
	 * 3. 当前元素乘以前一个位置的最小值（当当前元素为负数时）
	 * 同样地，新的最小值也可能是以上三种情况之一。
	 * 
	 * 为了处理整数溢出问题，我们使用double类型来存储中间结果。
	 * 
	 * 时间复杂度: O(n) - 需要遍历数组一次
	 * 空间复杂度: O(1) - 只需要几个变量存储状态
	 * 
	 * 是否最优解: 是，这是该问题的最优解法
	 * 
	 * 核心细节解析:
	 * 1. 负数的特殊处理：由于负数的存在，我们需要同时维护最大值和最小值
	 * 2. 零的处理：当遇到零时，当前最大值和最小值会被重置
	 * 3. 整数溢出：使用double来避免中间结果溢出
	 * 
	 * 工程化考量:
	 * 1. 鲁棒性：处理了空数组、单元素数组等边界情况
	 * 2. 性能优化：使用O(1)空间复杂度的算法
	 */

	// 这节课讲完之后，测试数据又增加了
	// 用int类型的变量会让中间结果溢出
	// 所以改成用double类型的变量
	// 思路是不变的
	public static int maxProduct(int[] nums) {
		double ans = nums[0], min = nums[0], max = nums[0], curmin, curmax;
		for (int i = 1; i < nums.length; i++) {
			curmin = Math.min(nums[i], Math.min(min * nums[i], max * nums[i]));
			curmax = Math.max(nums[i], Math.max(min * nums[i], max * nums[i]));
			min = curmin;
			max = curmax;
			ans = Math.max(ans, max);
		}
		return (int) ans;
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
	 * 7. LeetCode 628. 三个数的最大乘积 - https://leetcode.cn/problems/maximum-product-of-three-numbers/
	 * 8. LeetCode 198. 打家劫舍 - https://leetcode.cn/problems/house-robber/
	 * 9. LeetCode 213. 打家劫舍 II - https://leetcode.cn/problems/house-robber-ii/
	 * 10. LeetCode 337. 打家劫舍 III - https://leetcode.cn/problems/house-robber-iii/
	 * 11. LeetCode 862. 和至少为 K 的最短子数组 - https://leetcode.cn/problems/shortest-subarray-with-sum-at-least-k/
	 * 12. LeetCode 209. 长度最小的子数组 - https://leetcode.cn/problems/minimum-size-subarray-sum/
	 * 13. LeetCode 1004. 最大连续1的个数 III - https://leetcode.cn/problems/max-consecutive-ones-iii/
	 * 14. LeetCode 1438. 绝对差不超过限制的最长连续子数组 - https://leetcode.cn/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/
	 * 15. LeetCode 1425. 带限制的子序列和 - https://leetcode.cn/problems/constrained-subsequence-sum/
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
	
	// 新增：LeetCode 53. 最大子数组和
	// 给你一个整数数组 nums ，请你找出一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
	// 子数组 是数组中的一个连续部分。
	// 测试链接 : https://leetcode.cn/problems/maximum-subarray/
	/*
	 * 解题思路:
	 * 这是经典的Kadane算法问题。
	 * 
	 * 状态定义:
	 * dp[i] 表示以 nums[i] 结尾的最大子数组和
	 * 
	 * 状态转移:
	 * dp[i] = max(nums[i], dp[i-1] + nums[i])
	 * 即要么从当前元素重新开始，要么将当前元素加入之前的子数组
	 * 
	 * 优化:
	 * 由于当前状态只与前一个状态有关，可以使用一个变量代替数组
	 * 
	 * 时间复杂度: O(n) - 需要遍历数组一次
	 * 空间复杂度: O(1) - 只需要常数个变量存储状态
	 * 
	 * 是否最优解: 是，这是该问题的最优解法
	 * 
	 * 核心细节解析:
	 * 1. 为什么选择max(nums[i], dp[i-1] + nums[i])？
	 *    - 如果dp[i-1]是负数，那么从当前元素重新开始会更好
	 *    - 如果dp[i-1]是正数，那么将当前元素加入之前的子数组会更好
	 * 2. 边界处理：初始时dp和maxSum都设为nums[0]
	 * 3. 异常场景：全负数数组时，会选择最大的那个负数
	 */
	public static int maxSubArray(int[] nums) {
		// 异常防御：处理空数组情况
		if (nums == null || nums.length == 0) {
			throw new IllegalArgumentException("Input array cannot be null or empty");
		}
		
		// dp表示以当前元素结尾的最大子数组和
		int dp = nums[0];
		// maxSum表示全局最大子数组和
		int maxSum = nums[0];
		
		// 从第二个元素开始遍历
		for (int i = 1; i < nums.length; i++) {
			// 关键步骤：要么从当前元素重新开始，要么将当前元素加入之前的子数组
			dp = Math.max(nums[i], dp + nums[i]);
			// 更新全局最大值
			maxSum = Math.max(maxSum, dp);
		}
		
		return maxSum;
	}
	
	// 新增：LeetCode 628. 三个数的最大乘积
	// 给你一个整型数组 nums ，在数组中找出由三个数组成的最大乘积，并输出这个乘积。
	// 测试链接 : https://leetcode.cn/problems/maximum-product-of-three-numbers/
	/*
	 * 解题思路:
	 * 考虑到负数的存在，最大乘积可能是以下两种情况之一：
	 * 1. 最大的三个正数的乘积
	 * 2. 最小的两个负数（绝对值最大）和最大的正数的乘积
	 * 
	 * 时间复杂度: O(n) - 只需要一次遍历找出五个关键值
	 * 空间复杂度: O(1) - 只需要常数个变量
	 * 
	 * 是否最优解: 是，这是该问题的最优解法
	 */
	public static int maximumProduct(int[] nums) {
		// 异常防御
		if (nums == null || nums.length < 3) {
			throw new IllegalArgumentException("Input array must have at least 3 elements");
		}
		
		// 初始化五个关键变量
		int max1 = Integer.MIN_VALUE; // 最大
		int max2 = Integer.MIN_VALUE; // 第二大
		int max3 = Integer.MIN_VALUE; // 第三大
		int min1 = Integer.MAX_VALUE; // 最小
		int min2 = Integer.MAX_VALUE; // 第二小
		
		// 遍历数组找出五个关键值
		for (int num : nums) {
			// 更新最大值
			if (num > max1) {
				max3 = max2;
				max2 = max1;
				max1 = num;
			} else if (num > max2) {
				max3 = max2;
				max2 = num;
			} else if (num > max3) {
				max3 = num;
			}
			
			// 更新最小值
			if (num < min1) {
				min2 = min1;
				min1 = num;
			} else if (num < min2) {
				min2 = num;
			}
		}
		
		// 比较两种情况的乘积
		// 情况1：最大的三个正数
		// 情况2：最小的两个负数和最大的正数
		return Math.max(max1 * max2 * max3, min1 * min2 * max1);
	}
	
	// 新增：LeetCode 198. 打家劫舍
	// 你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，
	// 影响你偷窃的唯一制约因素就是相邻的房屋装有相互连通的防盗系统，
	// 如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。
	// 给定一个代表每个房屋存放金额的非负整数数组，计算你不触动警报装置的情况下，
	// 一夜之内能够偷窃到的最高金额。
	// 测试链接 : https://leetcode.cn/problems/house-robber/
	/*
	 * 解题思路:
	 * 这是一个典型的动态规划问题。对于每个房屋，我们有两个选择：
	 * 1. 偷窃当前房屋：那么不能偷窃前一个房屋，最大金额为dp[i-2] + nums[i]
	 * 2. 不偷窃当前房屋：那么最大金额为dp[i-1]
	 * 
	 * 状态定义:
	 * dp[i] 表示偷窃到第i个房屋时能获得的最大金额
	 * 
	 * 状态转移:
	 * dp[i] = max(dp[i-1], dp[i-2] + nums[i])
	 * 
	 * 优化:
	 * 由于当前状态只与前两个状态有关，可以使用两个变量代替数组
	 * 
	 * 时间复杂度: O(n) - 需要遍历数组一次
	 * 空间复杂度: O(1) - 只需要常数个变量存储状态
	 * 
	 * 是否最优解: 是，这是该问题的最优解法
	 */
	public static int rob(int[] nums) {
		// 异常防御
		if (nums == null || nums.length == 0) {
			return 0;
		}
		
		if (nums.length == 1) {
			return nums[0];
		}
		
		// prev2表示dp[i-2]，prev1表示dp[i-1]
		int prev2 = nums[0];
		int prev1 = Math.max(nums[0], nums[1]);
		
		// 从第三个房屋开始遍历
		for (int i = 2; i < nums.length; i++) {
			// 当前房屋的最大金额 = max(不偷当前房屋, 偷当前房屋)
			int current = Math.max(prev1, prev2 + nums[i]);
			// 更新状态
			prev2 = prev1;
			prev1 = current;
		}
		
		return prev1;
	}
}
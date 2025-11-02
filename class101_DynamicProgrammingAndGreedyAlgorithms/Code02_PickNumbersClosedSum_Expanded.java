package class087;

// 选择k个数字使得两集合累加和相差不超过1问题扩展实现
// 给定一个正数n，表示1~n这些数字都可以选择
// 给定一个正数k，表示要从1~n中选择k个数字组成集合A，剩下数字组成集合B
// 希望做到集合A和集合B的累加和相差不超过1
// 如果能做到，返回集合A选择了哪些数字，任何一种方案都可以
// 如果不能做到，返回长度为0的数组
// 2 <= n <= 10^6
// 1 <= k <= n
// 来自真实大厂笔试，没有测试链接，用对数器验证

import java.util.*;

public class Code02_PickNumbersClosedSum_Expanded {
	
	/*
	 * 类似题目1：分割等和子集（LeetCode 416）
	 * 题目描述：
	 * 给定一个非空的正整数数组 nums，请判断能否将这些数字分成元素和相等的两部分。
	 * 
	 * 示例：
	 * 输入: nums = [1, 5, 11, 5]
	 * 输出: true
	 * 解释: nums 可以分割成 [1, 5, 5] 和 [11]。
	 * 
	 * 解题思路：
	 * 这是一个经典的0-1背包问题。
	 * 如果数组总和为sum，我们需要找到一个子集和为sum/2。
	 * dp[i][j] 表示前i个数字能否组成和为j的子集。
	 * 状态转移方程：
	 * dp[i][j] = dp[i-1][j] || dp[i-1][j-nums[i-1]]
	 */
	
	// 分割等和子集 - 解法一：二维动态规划
	// 时间复杂度: O(n * target)，其中n是数组长度，target是数组总和的一半
	// 空间复杂度: O(n * target)
	public static boolean canPartition1(int[] nums) {
		int sum = 0;
		for (int num : nums) {
			sum += num;
		}
		
		// 如果总和是奇数，不可能分成两部分
		if ((sum & 1) == 1) {
			return false;
		}
		
		int target = sum / 2;
		// dp[i][j] 表示前i个数字能否组成和为j的子集
		boolean[][] dp = new boolean[nums.length + 1][target + 1];
		
		// 初始化：前0个数字组成和为0是可以的
		for (int i = 0; i <= nums.length; i++) {
			dp[i][0] = true;
		}
		
		// 状态转移
		for (int i = 1; i <= nums.length; i++) {
			for (int j = 1; j <= target; j++) {
				// 不选择当前数字
				dp[i][j] = dp[i-1][j];
				// 选择当前数字（如果当前数字不超过目标和）
				if (j >= nums[i-1]) {
					dp[i][j] = dp[i][j] || dp[i-1][j - nums[i-1]];
				}
			}
		}
		
		return dp[nums.length][target];
	}
	
	// 分割等和子集 - 解法二：一维动态规划（空间优化）
	// 时间复杂度: O(n * target)，其中n是数组长度，target是数组总和的一半
	// 空间复杂度: O(target)
	public static boolean canPartition2(int[] nums) {
		int sum = 0;
		for (int num : nums) {
			sum += num;
		}
		
		// 如果总和是奇数，不可能分成两部分
		if ((sum & 1) == 1) {
			return false;
		}
		
		int target = sum / 2;
		// dp[j] 表示是否能组成和为j的子集
		boolean[] dp = new boolean[target + 1];
		dp[0] = true;
		
		// 状态转移
		for (int i = 0; i < nums.length; i++) {
			// 从后往前遍历，避免重复使用当前数字
			for (int j = target; j >= nums[i]; j--) {
				dp[j] = dp[j] || dp[j - nums[i]];
			}
		}
		
		return dp[target];
	}
	
	/*
	 * 类似题目2：目标和（LeetCode 494）
	 * 题目描述：
	 * 给定一个非负整数数组，a1, a2, ..., an, 和一个目标数，S。
	 * 现在你有两个符号 + 和 -。对于数组中的每个数字，你都可以选择一个符号。
	 * 将所有符号组合起来，得到的表达式的值等于S的方案数。
	 * 
	 * 示例：
	 * 输入: nums: [1, 1, 1, 1, 1], S: 3
	 * 输出: 5
	 * 解释: 有5种方法让最终目标和为3。
	 * 
	 * 解题思路：
	 * 设正数集合为P，负数集合为N，则sum(P) - sum(N) = S
	 * 又因为sum(P) + sum(N) = sum(nums)
	 * 联立得：sum(P) = (S + sum(nums)) / 2
	 * 问题转化为：在数组中选择一些数字，使其和为(S + sum(nums)) / 2的方案数
	 * 这是一个0-1背包求方案数的问题
	 */
	
	// 目标和 - 动态规划解法
	// 时间复杂度: O(n * target)，其中n是数组长度，target是(S + sum(nums)) / 2
	// 空间复杂度: O(target)
	public static int findTargetSumWays(int[] nums, int S) {
		int sum = 0;
		for (int num : nums) {
			sum += num;
		}
		
		// 边界条件检查
		if (Math.abs(S) > sum || (sum + S) % 2 == 1) {
			return 0;
		}
		
		int target = (sum + S) / 2;
		// dp[j] 表示组成和为j的方案数
		int[] dp = new int[target + 1];
		dp[0] = 1;
		
		// 状态转移
		for (int i = 0; i < nums.length; i++) {
			for (int j = target; j >= nums[i]; j--) {
				dp[j] += dp[j - nums[i]];
			}
		}
		
		return dp[target];
	}
	
	/*
	 * 类似题目3：数字和为sum的方法数
	 * 题目描述：
	 * 给定一个有n个正整数的数组A和一个整数sum，求选择数组A中部分数字和为sum的方案数。
	 * 当两种选取方案有一个数字的下标不一样，我们就认为是不同的组成方案。
	 * 
	 * 示例：
	 * 输入: n = 5, sum = 10, A = [2, 3, 5, 6, 8]
	 * 输出: 2
	 * 解释: 有两种方案使得和为10: [2, 3, 5] 和 [2, 8]
	 * 
	 * 解题思路：
	 * 这是一个0-1背包求方案数的问题
	 * dp[i][j] 表示前i个数字组成和为j的方案数
	 * 状态转移方程：
	 * dp[i][j] = dp[i-1][j] + dp[i-1][j-A[i-1]]
	 */
	
	// 数字和为sum的方法数 - 解法
	// 时间复杂度: O(n * sum)，其中n是数组长度
	// 空间复杂度: O(sum)
	public static int getWays(int[] A, int sum) {
		// dp[j] 表示组成和为j的方案数
		int[] dp = new int[sum + 1];
		dp[0] = 1;
		
		// 状态转移
		for (int i = 0; i < A.length; i++) {
			for (int j = sum; j >= A[i]; j--) {
				dp[j] += dp[j - A[i]];
			}
		}
		
		return dp[sum];
	}
	
	/*
	 * 类似题目4：零钱兑换问题（LeetCode 322）
	 * 题目描述：
	 * 给你一个整数数组 coins ，表示不同面额的硬币；以及一个整数 amount ，表示总金额。
	 * 计算并返回可以凑成总金额所需的最少的硬币个数。如果没有任何一种硬币组合能组成总金额，返回 -1 。
	 * 你可以认为每种硬币的数量是无限的。
	 * 
	 * 示例：
	 * 输入：coins = [1, 2, 5], amount = 11
	 * 输出：3
	 * 解释：11 = 5 + 5 + 1
	 * 
	 * 解题思路：
	 * 这是一个完全背包问题。
	 * dp[i] 表示凑成金额i所需的最少硬币数
	 * 状态转移方程：
	 * dp[i] = min(dp[i], dp[i - coin] + 1) for each coin in coins
	 */
	
	// 零钱兑换问题 - 动态规划解法
	// 时间复杂度: O(amount * coins.length)
	// 空间复杂度: O(amount)
	public static int coinChange(int[] coins, int amount) {
		// dp[i] 表示凑成金额i所需的最少硬币数
		int[] dp = new int[amount + 1];
		// 初始化为amount+1，表示不可能达到的值
		Arrays.fill(dp, amount + 1);
		dp[0] = 0;
		
		// 状态转移
		for (int i = 1; i <= amount; i++) {
			for (int coin : coins) {
				if (coin <= i) {
					dp[i] = Math.min(dp[i], dp[i - coin] + 1);
				}
			}
		}
		
		return dp[amount] > amount ? -1 : dp[amount];
	}
	
	/*
	 * 类似题目5：零钱兑换II（LeetCode 518）
	 * 题目描述：
	 * 给你一个整数数组 coins 表示不同面额的硬币，另给一个整数 amount 表示总金额。
	 * 请你计算并返回可以凑成总金额的硬币组合数。如果任何硬币组合都无法凑出总金额，返回 0 。
	 * 假设每一种面额的硬币有无限个。
	 * 
	 * 示例：
	 * 输入：amount = 5, coins = [1, 2, 5]
	 * 输出：4
	 * 解释：有四种方式可以凑成总金额：
	 * 5=5
	 * 5=2+2+1
	 * 5=2+1+1+1
	 * 5=1+1+1+1+1
	 * 
	 * 解题思路：
	 * 这是一个完全背包求方案数的问题。
	 * dp[i] 表示凑成金额i的组合数
	 * 状态转移方程：
	 * dp[i] += dp[i - coin] for each coin in coins
	 */
	
	// 零钱兑换II - 动态规划解法
	// 时间复杂度: O(amount * coins.length)
	// 空间复杂度: O(amount)
	public static int change(int amount, int[] coins) {
		// dp[i] 表示凑成金额i的组合数
		int[] dp = new int[amount + 1];
		dp[0] = 1;
		
		// 状态转移
		for (int coin : coins) {
			for (int i = coin; i <= amount; i++) {
				dp[i] += dp[i - coin];
			}
		}
		
		return dp[amount];
	}
	
	// 测试方法
	public static void main(String[] args) {
		// 测试分割等和子集
		int[] nums1 = {1, 5, 11, 5};
		System.out.println("分割等和子集解法一结果: " + canPartition1(nums1));
		System.out.println("分割等和子集解法二结果: " + canPartition2(nums1));
		
		// 测试目标和
		int[] nums2 = {1, 1, 1, 1, 1};
		int target = 3;
		System.out.println("目标和结果: " + findTargetSumWays(nums2, target));
		
		// 测试数字和为sum的方法数
		int[] A = {2, 3, 5, 6, 8};
		int sum = 10;
		System.out.println("数字和为sum的方法数结果: " + getWays(A, sum));
		
		// 测试零钱兑换问题
		int[] coins1 = {1, 2, 5};
		int amount1 = 11;
		System.out.println("零钱兑换问题结果: " + coinChange(coins1, amount1));
		
		// 测试零钱兑换II
		int[] coins2 = {1, 2, 5};
		int amount2 = 5;
		System.out.println("零钱兑换II结果: " + change(amount2, coins2));
	}
}
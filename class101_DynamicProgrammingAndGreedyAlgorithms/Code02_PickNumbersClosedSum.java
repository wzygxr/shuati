package class087;

import java.util.Arrays;

/**
 * 选择k个数字使得两集合累加和相差不超过1
 * 
 * 问题描述:
 * 给定一个正数n，表示1~n这些数字都可以选择
 * 给定一个正数k，表示要从1~n中选择k个数字组成集合A，剩下数字组成集合B
 * 希望做到集合A和集合B的累加和相差不超过1
 * 如果能做到，返回集合A选择了哪些数字，任何一种方案都可以
 * 如果不能做到，返回长度为0的数组
 * 
 * 约束条件:
 * 2 <= n <= 10^6
 * 1 <= k <= n
 * 
 * 解题思路:
 * 基于数学构造的贪心算法，直接构造最优解
 * 时间复杂度达到理论下界O(k)
 * 
 * 来自真实大厂笔试，没有测试链接，用对数器验证
 */
public class Code02_PickNumbersClosedSum {

	/**
	 * 正式方法 - 最优解
	 * 
	 * 算法思路: 基于数学构造的贪心算法
	 * 1. 计算总和sum = n*(n+1)/2
	 * 2. 目标让集合A的和接近sum/2
	 * 3. 先尝试让集合A的和为sum/2，如果失败且总和为奇数，再尝试(sum/2)+1
	 * 
	 * 时间复杂度: O(k)
	 * 空间复杂度: O(k)
	 * 
	 * @param n 数字范围上限
	 * @param k 需要选择的数字个数
	 * @return 选择的数字数组，如果无解返回空数组
	 */
	public static int[] pick(int n, int k) {
		// 计算1~n的总和
		long sum = (n + 1) * n / 2;
		
		// 尝试让集合A的和为sum/2
		int[] ans = generate(sum / 2, n, k);
		
		// 如果失败且总和为奇数，尝试(sum/2)+1
		if (ans.length == 0 && (sum & 1) == 1) {
			ans = generate(sum / 2 + 1, n, k);
		}
		
		return ans;
	}

	/**
	 * 生成满足条件的数字集合
	 * 
	 * 算法原理:
	 * 1. 最小可能的k个数字和: minKSum = k*(k+1)/2
	 * 2. 最大可能的k个数字和: maxKSum = minKSum + (n-k)*k
	 * 3. 如果目标sum不在[minKSum, maxKSum]范围内，无解
	 * 4. 使用贪心构造方法生成解
	 * 
	 * 构造策略:
	 * 1. 选择最小的leftSize个数字: 1, 2, ..., leftSize
	 * 2. 选择最大的rightSize个数字: n, n-1, ..., n-rightSize+1
	 * 3. 如果还有剩余需求，选择一个中间数字
	 * 
	 * @param sum 目标和
	 * @param n 数字范围上限
	 * @param k 需要选择的数字个数
	 * @return 选择的数字数组，如果无解返回空数组
	 */
	public static int[] generate(long sum, int n, int k) {
		// 计算最小k个数字的和
		long minKSum = (k + 1) * k / 2;
		int range = n - k;
		
		// 检查目标sum是否在可行范围内
		if (sum < minKSum || sum > minKSum + (long) range * k) {
			return new int[0];
		}
		
		// 计算需要额外增加的和
		long need = sum - minKSum;
		
		// 计算右半部分的大小（选择最大的几个数字）
		int rightSize = (int) (need / range);
		
		// 计算中间索引位置
		int midIndex = (k - rightSize) + (int) (need % range);
		
		// 计算左半部分的大小
		int leftSize = k - rightSize - (need % range == 0 ? 0 : 1);
		
		// 构造结果数组
		int[] ans = new int[k];
		
		// 填充左半部分（最小的几个数字）
		for (int i = 0; i < leftSize; i++) {
			ans[i] = i + 1;
		}
		
		// 如果有中间元素，填充中间元素
		if (need % range != 0) {
			ans[leftSize] = midIndex;
		}
		
		// 填充右半部分（最大的几个数字）
		for (int i = k - 1, j = 0; j < rightSize; i--, j++) {
			ans[i] = n - j;
		}
		
		return ans;
	}

	/**
	 * 验证结果是否正确
	 * 检验生成的集合是否满足条件
	 * 
	 * 验证逻辑:
	 * 1. 如果返回空数组，检查是否真的无解
	 * 2. 如果返回非空数组，检查:
	 *    a. 数组长度是否为k
	 *    b. 集合A和集合B的和差是否不超过1
	 * 
	 * @param n 数字范围上限
	 * @param k 需要选择的数字个数
	 * @param ans 生成的数字数组
	 * @return 验证结果
	 */
	public static boolean pass(int n, int k, int[] ans) {
		if (ans.length == 0) {
			// 如果返回空数组，检查是否真的无解
			if (canSplit(n, k)) {
				return false;  // 实际有解但算法未找到
			} else {
				return true;   // 确实无解
			}
		} else {
			// 检查数组长度是否正确
			if (ans.length != k) {
				return false;
			}
			
			// 计算总和
			int sum = (n + 1) * n / 2;
			int pickSum = 0;
			for (int num : ans) {
				pickSum += num;
			}
			
			// 检查集合A和集合B的和差是否不超过1
			return Math.abs(pickSum - (sum - pickSum)) <= 1;
		}
	}

	/**
	 * 记忆化搜索方法（用于验证）
	 * 
	 * 算法说明: 不是最优解，只是为了验证正确性
	 * 使用三维动态规划判断是否能选出k个数字使其和接近sum/2
	 * 
	 * 状态定义: dp[i][k][s]表示考虑前i个数字，选出k个数字和为s是否可能
	 * 
	 * @param n 数字范围上限
	 * @param k 需要选择的数字个数
	 * @return 是否能做到
	 */
	public static boolean canSplit(int n, int k) {
		// 计算总和
		int sum = (n + 1) * n / 2;
		
		// 目标和，如果总和为奇数则目标为(sum/2)+1，否则为sum/2
		int wantSum = (sum / 2) + ((sum & 1) == 0 ? 0 : 1);
		
		// 使用三维数组进行记忆化搜索
		int[][][] dp = new int[n + 1][k + 1][wantSum + 1];
		
		return f(n, 1, k, wantSum, dp);
	}

	/**
	 * 深度优先搜索辅助函数
	 * 
	 * 状态转移:
	 * 1. 不选择数字i: f(n, i+1, k, s, dp)
	 * 2. 选择数字i: f(n, i+1, k-1, s-i, dp)
	 * 
	 * 记忆化:
	 * dp[i][k][s] = 0: 未计算
	 * dp[i][k][s] = 1: 可行
	 * dp[i][k][s] = -1: 不可行
	 * 
	 * @param n 数字范围上限
	 * @param i 当前考虑的数字
	 * @param k 还需要选择的数字个数
	 * @param s 还需要达到的和
	 * @param dp 记忆化数组
	 * @return 是否可行
	 */
	public static boolean f(int n, int i, int k, int s, int[][][] dp) {
		// 边界条件：如果需要的数字个数或和为负数，则不可行
		if (k < 0 || s < 0) {
			return false;
		}
		
		// 边界条件：已经考虑完所有数字
		if (i == n + 1) {
			return k == 0 && s == 0;
		}
		
		// 记忆化：如果已经计算过，直接返回结果
		if (dp[i][k][s] != 0) {
			return dp[i][k][s] == 1;
		}
		
		// 状态转移：不选择当前数字 或 选择当前数字
		boolean ans = f(n, i + 1, k, s, dp) || f(n, i + 1, k - 1, s - i, dp);
		
		// 记忆化存储结果
		dp[i][k][s] = ans ? 1 : -1;
		
		return ans;
	}

	/**
	 * 对数器 - 用于验证算法正确性
	 * 
	 * 测试策略:
	 * 1. 生成随机测试用例
	 * 2. 使用pick方法求解
	 * 3. 使用pass方法验证结果正确性
	 * 
	 * 工程化考量:
	 * - 使用随机数据进行大规模测试
	 * - 及时发现算法错误
	 */
	public static void main(String[] args) {
		// 测试参数设置
		int N = 60;        // n的最大值
		int testTime = 5000;  // 测试次数
		
		System.out.println("测试开始");
		
		// 大规模随机测试
		for (int i = 0; i < testTime; i++) {
			// 生成随机测试用例
			int n = (int) (Math.random() * N) + 2;
			int k = (int) (Math.random() * n) + 1;
			
			// 使用算法求解
			int[] ans = pick(n, k);
			
			// 验证结果正确性
			if (!pass(n, k, ans)) {
				System.out.println("出错了!");
			}
		}
		
		System.out.println("测试结束");
	}
	
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
		boolean[][] dp = new boolean[nums.length + 1][target + 1];
		
		// 初始化
		for (int i = 0; i <= nums.length; i++) {
			dp[i][0] = true;
		}
		
		// 状态转移
		for (int i = 1; i <= nums.length; i++) {
			for (int j = 1; j <= target; j++) {
				// 不选择当前数字
				dp[i][j] = dp[i-1][j];
				// 选择当前数字
				if (j >= nums[i-1]) {
					dp[i][j] = dp[i][j] || dp[i-1][j - nums[i-1]];
				}
			}
		}
		
		return dp[nums.length][target];
	}
	
	// 分割等和子集 - 解法二：一维动态规划（空间优化）
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
	public static int getWays(int[] A, int sum) {
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
}
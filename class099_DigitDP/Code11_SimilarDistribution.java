package class084;

// 洛谷P4127 [AHOI2009] 同类分布
// 题目链接: https://www.luogu.com.cn/problem/P4127
// 题目描述: 给出两个数a,b，求出[a,b]中各位数字之和能整除原数的数的个数。
public class Code11_SimilarDistribution {

	/**
	 * 数位DP解法
	 * 时间复杂度: O(log(b) * 162 * 162 * 2 * 2)
	 * 空间复杂度: O(log(b) * 162 * 162 * 2 * 2)
	 * 
	 * 解题思路:
	 * 1. 将问题转化为统计[0, b]中符合条件的数的个数减去[0, a-1]中符合条件的数的个数
	 * 2. 由于数位和s的最大可能值为9*18=162（假设最多18位数），我们可以枚举数位和s
	 * 3. 对于每个数位和s，使用数位DP统计满足以下条件的数x的个数：
	 *    - x的数位和等于s
	 *    - x能被s整除
	 * 4. 状态需要记录：当前处理到第几位、当前数位和、当前数值对s的余数、是否受到上界限制、是否已经开始填数字
	 * 5. 通过记忆化搜索避免重复计算
	 * 
	 * 最优解分析:
	 * 该解法是标准的数位DP解法，能够高效处理大范围的输入，是解决此类问题的最优通用方法。
	 * 时间复杂度中的162来自于数位和的最大可能值（9*18）。
	 */
	public static int similarDistribution(long a, long b) {
		// 计算[0, b]中符合条件的数的个数减去[0, a-1]中符合条件的数的个数
		return countValidNumbers(b) - countValidNumbers(a - 1);
	}

	// 计算[0, n]中符合条件的数的个数
	private static int countValidNumbers(long n) {
		if (n < 1) {
			return 0; // 0不符合条件，因为不能除以0
		}

		String s = String.valueOf(n);
		int len = s.length();
		int maxSum = len * 9; // 最大可能的数位和
		int result = 0;

		// 枚举所有可能的数位和s
		for (int s_sum = 1; s_sum <= maxSum; s_sum++) {
			// 对于每个数位和s_sum，统计满足条件的数的个数
			result += countNumbersWithSumDivisibleBy(s.toCharArray(), s_sum);
		}

		return result;
	}

	// 统计数位和等于s_sum且能被s_sum整除的数的个数
	private static int countNumbersWithSumDivisibleBy(char[] digits, int s_sum) {
		int len = digits.length;
		// dp[pos][sum][mod][isLimit][isNum]
		// pos: 当前处理到第几位
		// sum: 当前数位和
		// mod: 当前数值对s_sum的余数
		// isLimit: 是否受到上界限制
		// isNum: 是否已开始填数字
		int[][][][][] dp = new int[len][s_sum + 1][s_sum][2][2];

		// 初始化dp为-1，表示未计算过
		for (int i = 0; i < len; i++) {
			for (int j = 0; j <= s_sum; j++) {
				for (int k = 0; k < s_sum; k++) {
					for (int l = 0; l < 2; l++) {
						for (int m = 0; m < 2; m++) {
							dp[i][j][k][l][m] = -1;
						}
					}
				}
			}
		}

		return dfs(digits, 0, 0, 0, true, false, s_sum, dp);
	}

	/**
	 * 数位DP递归函数
	 * 
	 * @param digits 数字的字符数组
	 * @param pos 当前处理到第几位
	 * @param sum 当前数位和
	 * @param mod 当前数值对s_sum的余数
	 * @param isLimit 是否受到上界限制
	 * @param isNum 是否已开始填数字（处理前导零）
	 * @param s_sum 目标数位和
	 * @param dp 记忆化数组
	 * @return 从当前状态开始，符合条件的数的个数
	 */
	private static int dfs(char[] digits, int pos, int sum, int mod, boolean isLimit, boolean isNum, int s_sum, int[][][][][] dp) {
		// 递归终止条件
		if (pos == digits.length) {
			// 只有当已经填了数字，且数位和等于s_sum，且数值能被s_sum整除时才算符合条件
			return isNum && sum == s_sum && mod == 0 ? 1 : 0;
		}

		// 记忆化搜索
		if (!isLimit && isNum && dp[pos][sum][mod][0][0] != -1) {
			return dp[pos][sum][mod][0][0];
		}

		int ans = 0;

		// 如果还没开始填数字，可以选择跳过当前位（处理前导零）
		if (!isNum) {
			ans += dfs(digits, pos + 1, sum, mod, false, false, s_sum, dp);
		}

		// 确定当前位可以填入的数字范围
		int upper = isLimit ? digits[pos] - '0' : 9;

		// 枚举当前位可以填入的数字
		for (int d = isNum ? 0 : 1; d <= upper; d++) {
			int newSum = sum + d;
			// 如果新的数位和已经超过了s_sum，可以提前剪枝
			if (newSum > s_sum) {
				continue;
			}
			
			// 更新当前数值对s_sum的余数
			int newMod = (mod * 10 + d) % s_sum;
			boolean newIsLimit = isLimit && (d == upper);
			boolean newIsNum = isNum || (d > 0);

			// 递归处理下一位
			ans += dfs(digits, pos + 1, newSum, newMod, newIsLimit, newIsNum, s_sum, dp);
		}

		// 记忆化存储
		if (!isLimit && isNum) {
			dp[pos][sum][mod][0][0] = ans;
		}

		return ans;
	}

	/**
	 * 优化版本：减少一维状态
	 * 注意到我们只关心数位和等于s_sum的情况，所以可以在递归过程中进行剪枝
	 * 当sum > s_sum时，可以直接跳过
	 */
	private static int countNumbersWithSumDivisibleByOptimized(char[] digits, int s_sum) {
		int len = digits.length;
		// dp[pos][mod][isLimit][isNum]
		// pos: 当前处理到第几位
		// mod: 当前数值对s_sum的余数
		// isLimit: 是否受到上界限制
		// isNum: 是否已开始填数字
		int[][][][] dp = new int[len][s_sum][2][2];

		// 初始化dp为-1，表示未计算过
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < s_sum; j++) {
				for (int k = 0; k < 2; k++) {
					for (int l = 0; l < 2; l++) {
						dp[i][j][k][l] = -1;
					}
				}
			}
		}

		return dfsOptimized(digits, 0, 0, 0, true, false, s_sum, dp);
	}

	private static int dfsOptimized(char[] digits, int pos, int sum, int mod, boolean isLimit, boolean isNum, int s_sum, int[][][][] dp) {
		// 递归终止条件
		if (pos == digits.length) {
			// 只有当已经填了数字，且数位和等于s_sum，且数值能被s_sum整除时才算符合条件
			return isNum && sum == s_sum && mod == 0 ? 1 : 0;
		}

		// 记忆化搜索
		if (!isLimit && isNum && dp[pos][mod][0][0] != -1) {
			return dp[pos][mod][0][0];
		}

		int ans = 0;

		// 如果还没开始填数字，可以选择跳过当前位（处理前导零）
		if (!isNum) {
			ans += dfsOptimized(digits, pos + 1, sum, mod, false, false, s_sum, dp);
		}

		// 确定当前位可以填入的数字范围
		int upper = isLimit ? digits[pos] - '0' : 9;

		// 枚举当前位可以填入的数字
		for (int d = isNum ? 0 : 1; d <= upper; d++) {
			int newSum = sum + d;
			// 如果新的数位和已经超过了s_sum，可以提前剪枝
			if (newSum > s_sum) {
				continue;
			}
			
			// 更新当前数值对s_sum的余数
			int newMod = (mod * 10 + d) % s_sum;
			boolean newIsLimit = isLimit && (d == upper);
			boolean newIsNum = isNum || (d > 0);

			// 递归处理下一位
			ans += dfsOptimized(digits, pos + 1, newSum, newMod, newIsLimit, newIsNum, s_sum, dp);
		}

		// 记忆化存储
		if (!isLimit && isNum) {
			dp[pos][mod][0][0] = ans;
		}

		return ans;
	}

	// 测试代码
	public static void main(String[] args) {
		// 测试用例1: a=1, b=20
		// 预期输出: 19 (所有数都符合条件，除了那些数位和为0的数)
		long a1 = 1, b1 = 20;
		int result1 = similarDistribution(a1, b1);
		System.out.println("测试用例1: a=" + a1 + ", b=" + b1);
		System.out.println("符合条件的数的个数: " + result1);

		// 测试用例2: a=1, b=100
		long a2 = 1, b2 = 100;
		int result2 = similarDistribution(a2, b2);
		System.out.println("\n测试用例2: a=" + a2 + ", b=" + b2);
		System.out.println("符合条件的数的个数: " + result2);
	}
}
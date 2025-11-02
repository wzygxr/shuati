package class084;

// 范围中美丽整数的数目
// 给你两个正整数：low 和 high 。如果一个整数满足以下条件，我们称它为美丽整数：
// 1. 偶数数位的数目与奇数数位的数目相同；
// 2. 这个整数能被 k 整除。
// 请你返回范围 [low, high] 中美丽整数的数目。
// 测试链接 : https://leetcode.cn/problems/number-of-beautiful-integers-in-the-range/
public class Code07_NumberOfBeautifulIntegersInTheRange {

	/**
	 * 数位DP解法
	 * 时间复杂度: O(log(high) * 2 * 2 * 10 * 10 * k)
	 * 空间复杂度: O(log(high) * 2 * 2 * 10 * 10 * k)
	 * 
	 * 解题思路:
	 * 1. 使用数位DP框架，逐位确定数字
	 * 2. 状态需要记录：
	 *    - 当前处理到第几位
	 *    - 是否受到上界限制
	 *    - 是否已开始填数字（处理前导零）
	 *    - 奇数数位的个数
	 *    - 偶数数位的个数
	 *    - 当前数字对k的余数
	 * 3. 通过记忆化搜索避免重复计算
	 * 
	 * 最优解分析:
	 * 该解法是标准的数位DP解法，时间复杂度与状态数相关，是解决此类问题的最优通用方法。
	 */
	public static int numberOfBeautifulIntegers(int low, int high, int k) {
		// 答案为[0, high]中的美丽整数个数减去[0, low-1]中的美丽整数个数
		return beautifulIntegers(high, k) - beautifulIntegers(low - 1, k);
	}
	
	// 计算[0, n]中美丽整数的个数
	private static int beautifulIntegers(int n, int k) {
		if (n < 0) {
			return 0;
		}
		
		char[] s = String.valueOf(n).toCharArray();
		int len = s.length;
		
		// dp[pos][isLimit][isNum][oddCount][evenCount][remainder]
		// pos: 当前处理到第几位
		// isLimit: 是否受到上界限制
		// isNum: 是否已开始填数字
		// oddCount: 奇数数位的个数
		// evenCount: 偶数数位的个数
		// remainder: 当前数字对k的余数
		int[][][][][][] dp = new int[len][2][2][10][10][k];
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < 2; j++) {
				for (int l = 0; l < 2; l++) {
					for (int m = 0; m < 10; m++) {
						for (int o = 0; o < 10; o++) {
							for (int p = 0; p < k; p++) {
								dp[i][j][l][m][o][p] = -1;
							}
						}
					}
				}
			}
		}
		
		return dfs(s, 0, true, false, 0, 0, 0, k, dp);
	}
	
	// 数位DP递归函数
	private static int dfs(char[] s, int pos, boolean isLimit, boolean isNum, 
						  int oddCount, int evenCount, int remainder, int k, 
						  int[][][][][][] dp) {
		// 递归终止条件
		if (pos == s.length) {
			// 只有当已经填了数字，且奇数数位和偶数数位个数相等，且能被k整除时才算一个美丽整数
			return isNum && oddCount == evenCount && remainder == 0 ? 1 : 0;
		}
		
		// 记忆化搜索
		if (!isLimit && isNum && dp[pos][0][0][oddCount][evenCount][remainder] != -1) {
			return dp[pos][0][0][oddCount][evenCount][remainder];
		}
		
		int ans = 0;
		
		// 如果还没开始填数字，可以选择跳过当前位（处理前导零）
		if (!isNum) {
			ans += dfs(s, pos + 1, false, false, oddCount, evenCount, remainder, k, dp);
		}
		
		// 确定当前位可以填入的数字范围
		int up = isLimit ? s[pos] - '0' : 9;
		
		// 枚举当前位可以填入的数字
		for (int d = isNum ? 0 : 1; d <= up; d++) {
			// 根据数字的奇偶性更新奇数数位和偶数数位的个数
			int newOddCount = oddCount + (d % 2 == 1 ? 1 : 0);
			int newEvenCount = evenCount + (d % 2 == 0 ? 1 : 0);
			
			// 更新当前数字对k的余数
			int newRemainder = (remainder * 10 + d) % k;
			
			// 递归处理下一位
			ans += dfs(s, pos + 1, isLimit && d == up, true, 
					  newOddCount, newEvenCount, newRemainder, k, dp);
		}
		
		// 记忆化存储
		if (!isLimit && isNum) {
			dp[pos][0][0][oddCount][evenCount][remainder] = ans;
		}
		
		return ans;
	}

	// 测试方法
	public static void main(String[] args) {
		// 测试用例1
		int low1 = 10, high1 = 20, k1 = 3;
		System.out.println("low = " + low1 + ", high = " + high1 + ", k = " + k1);
		System.out.println("美丽整数的数目: " + numberOfBeautifulIntegers(low1, high1, k1));
		// 预期输出: 2 (11和19是美丽整数)
		
		// 测试用例2
		int low2 = 1, high2 = 10, k2 = 1;
		System.out.println("low = " + low2 + ", high = " + high2 + ", k = " + k2);
		System.out.println("美丽整数的数目: " + numberOfBeautifulIntegers(low2, high2, k2));
		// 预期输出: 1 (10是美丽整数)
		
		// 测试用例3
		int low3 = 5, high3 = 5, k3 = 2;
		System.out.println("low = " + low3 + ", high = " + high3 + ", k = " + k3);
		System.out.println("美丽整数的数目: " + numberOfBeautifulIntegers(low3, high3, k3));
		// 预期输出: 0 (没有美丽整数)
	}
}
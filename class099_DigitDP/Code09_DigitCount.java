package class084;

// ZJOI2010 数字计数
// 给定两个正整数a和b，求在[a,b]中的所有整数中，每个数码（digit）各出现了多少次。
// 测试链接 : https://www.luogu.com.cn/problem/P2602
public class Code09_DigitCount {

	/**
	 * 数位DP解法
	 * 时间复杂度: O(log(b) * 2 * 2 * 10 * 10)
	 * 空间复杂度: O(log(b) * 2 * 2 * 10)
	 * 
	 * 解题思路:
	 * 1. 使用数位DP框架，逐位确定数字
	 * 2. 对于每个数字0-9，分别计算其出现次数
	 * 3. 状态需要记录：
	 *    - 当前处理到第几位
	 *    - 是否受到上界限制
	 *    - 是否已开始填数字（处理前导零）
	 *    - 当前数字的出现次数
	 * 4. 通过记忆化搜索避免重复计算
	 * 
	 * 最优解分析:
	 * 该解法是标准的数位DP解法，时间复杂度与状态数相关，是解决此类问题的最优通用方法。
	 */
	public static long[] countDigits(long a, long b) {
		// 答案为[0, b]中的数字计数减去[0, a-1]中的数字计数
		long[] countB = countDigitsUpTo(b);
		long[] countA = countDigitsUpTo(a - 1);
		
		long[] result = new long[10];
		for (int i = 0; i < 10; i++) {
			result[i] = countB[i] - countA[i];
		}
		
		return result;
	}
	
	// 计算[0, n]中每个数字的出现次数
	private static long[] countDigitsUpTo(long n) {
		if (n < 0) {
			return new long[10];
		}
		
		char[] s = String.valueOf(n).toCharArray();
		int len = s.length;
		
		// dp[pos][isLimit][isNum][digit]
		// pos: 当前处理到第几位
		// isLimit: 是否受到上界限制
		// isNum: 是否已开始填数字
		// digit: 当前统计的数字(0-9)
		long[][][][] dp = new long[len][2][2][10];
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < 2; j++) {
				for (int k = 0; k < 2; k++) {
					for (int l = 0; l < 10; l++) {
						dp[i][j][k][l] = -1;
					}
				}
			}
		}
		
		long[] result = new long[10];
		for (int digit = 0; digit < 10; digit++) {
			result[digit] = dfs(s, 0, true, false, digit, dp);
		}
		
		return result;
	}
	
	// 数位DP递归函数
	private static long dfs(char[] s, int pos, boolean isLimit, boolean isNum, int targetDigit, long[][][][] dp) {
		// 递归终止条件
		if (pos == s.length) {
			return 0;
		}
		
		// 记忆化搜索
		if (!isLimit && isNum && dp[pos][0][0][targetDigit] != -1) {
			return dp[pos][0][0][targetDigit];
		}
		
		long ans = 0;
		
		// 如果还没开始填数字，可以选择跳过当前位（处理前导零）
		if (!isNum) {
			ans += dfs(s, pos + 1, false, false, targetDigit, dp);
		}
		
		// 确定当前位可以填入的数字范围
		int up = isLimit ? s[pos] - '0' : 9;
		
		// 枚举当前位可以填入的数字
		for (int d = isNum ? 0 : 1; d <= up; d++) {
			// 如果当前位填的是目标数字，则计数加1
			long count = (d == targetDigit) ? 1 : 0;
			
			// 递归处理下一位
			ans += count + dfs(s, pos + 1, isLimit && d == up, true, targetDigit, dp);
		}
		
		// 记忆化存储
		if (!isLimit && isNum) {
			dp[pos][0][0][targetDigit] = ans;
		}
		
		return ans;
	}

	// 测试方法
	public static void main(String[] args) {
		// 测试用例1
		long a1 = 1, b1 = 9;
		System.out.println("a = " + a1 + ", b = " + b1);
		long[] result1 = countDigits(a1, b1);
		for (int i = 0; i < 10; i++) {
			System.out.print(result1[i] + " ");
		}
		System.out.println();
		// 预期输出: 0 1 1 1 1 1 1 1 1 1
		
		// 测试用例2
		long a2 = 1, b2 = 99;
		System.out.println("a = " + a2 + ", b = " + b2);
		long[] result2 = countDigits(a2, b2);
		for (int i = 0; i < 10; i++) {
			System.out.print(result2[i] + " ");
		}
		System.out.println();
		// 预期输出: 9 20 20 20 20 20 20 20 20 20
		
		// 测试用例3
		long a3 = 1, b3 = 1000;
		System.out.println("a = " + a3 + ", b = " + b3);
		long[] result3 = countDigits(a3, b3);
		for (int i = 0; i < 10; i++) {
			System.out.print(result3[i] + " ");
		}
		System.out.println();
	}
}
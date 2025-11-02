package class084;

// 统计各位数字都不同的数字个数
// 给你一个整数n，代表十进制数字最多有n位
// 如果某个数字，每一位都不同，那么这个数字叫做有效数字
// 返回有效数字的个数，不统计负数范围
// 测试链接 : https://leetcode.cn/problems/count-numbers-with-unique-digits/
public class Code01_CountNumbersWithUniqueDigits {

	/**
	 * 数学方法（排列组合）
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(1)
	 * 
	 * 解题思路:
	 * 1. 对于范围[0, 10^n)，我们可以按位数分别计算：
	 *    - 0位数：0 (1个)
	 *    - 1位数：1-9共9个
	 *    - 2位数：第一位有9种选择(1-9)，第二位有9种选择(0-9中除去第一位) = 9*9=81个
	 *    - 3位数：第一位有9种选择，第二位有9种选择，第三位有8种选择 = 9*9*8个
	 *    - 以此类推...
	 * 2. 将所有位数的结果累加
	 * 
	 * 优点：时间复杂度低，代码简洁
	 * 缺点：不够通用，难以扩展到其他约束条件
	 */
	public static int countNumbersWithUniqueDigits(int n) {
		if (n == 0) {
			return 1;
		}
		int ans = 10; // 0位数(0) + 1位数(1-9) = 10个
		int permutations = 9; // 2位数的第一位选择(1-9)
		
		// 计算2位数到n位数的排列数
		for (int i = 2; i <= n; i++) {
			permutations *= (11 - i); // 第i位有(11-i)种选择
			ans += permutations;
		}
		return ans;
	}
	
	/**
	 * 数位DP方法（更通用的解法）
	 * 时间复杂度: O(10 * 2^10 * n) 
	 * 空间复杂度: O(2^10 * n)
	 * 
	 * 解题思路:
	 * 1. 使用数位DP框架，逐位确定数字
	 * 2. 用位掩码记录已使用的数字
	 * 3. 通过记忆化避免重复计算
	 * 
	 * 优点：方法通用，容易扩展到其他约束条件
	 * 缺点：时间和空间复杂度较高
	 */
	public static int countNumbersWithUniqueDigitsDP(int n) {
		if (n == 0) {
			return 1;
		}
		
		// 构造上界字符串 (10^n - 1)
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			sb.append("9");
		}
		String upper = sb.toString();
		
		// dp[pos][mask][isLimit][isNum]
		// pos: 当前处理到第几位
		// mask: 已使用数字的位掩码
		// isLimit: 是否受到上界限制
		// isNum: 是否已开始填数字
		int[][][][] dp = new int[upper.length()][1 << 10][2][2];
		for (int i = 0; i < upper.length(); i++) {
			for (int j = 0; j < (1 << 10); j++) {
				dp[i][j][0][0] = -1;
				dp[i][j][0][1] = -1;
				dp[i][j][1][0] = -1;
				dp[i][j][1][1] = -1;
			}
		}
		
		return dfs(upper.toCharArray(), 0, 0, true, false, dp);
	}
	
	private static int dfs(char[] s, int pos, int mask, boolean isLimit, boolean isNum, int[][][][] dp) {
		// 递归终止条件
		if (pos == s.length) {
			return isNum ? 1 : 1;  // 如果已经填了数字，返回1个有效数字；如果没有填数字，也返回1（表示数字0）
		}
		
		// 记忆化搜索
		if (!isLimit && isNum && dp[pos][mask][0][0] != -1) {
			return dp[pos][mask][0][0];
		}
		
		int ans = 0;
		
		// 如果还没开始填数字，可以选择跳过当前位（处理前导零）
		if (!isNum) {
			ans += dfs(s, pos + 1, mask, false, false, dp);
		}
		
		// 确定当前位可以填入的数字范围
		int up = isLimit ? s[pos] - '0' : 9;
		
		// 枚举当前位可以填入的数字
		for (int d = isNum ? 0 : 1; d <= up; d++) {
			// 如果数字d还没有被使用过
			if ((mask & (1 << d)) == 0) {
				// 递归处理下一位
				ans += dfs(s, pos + 1, mask | (1 << d), isLimit && d == up, true, dp);
			}
		}
		
		// 记忆化存储
		if (!isLimit && isNum) {
			dp[pos][mask][0][0] = ans;
		}
		
		return ans;
	}

	// 测试方法
	public static void main(String[] args) {
		// 测试用例1
		int n1 = 2;
		System.out.println("n = " + n1);
		System.out.println("数学方法结果: " + countNumbersWithUniqueDigits(n1));
		System.out.println("数位DP方法结果: " + countNumbersWithUniqueDigitsDP(n1));
		// 预期输出: 91 (0-99中除11,22,...,99外的所有数字)
		
		// 测试用例2
		int n2 = 0;
		System.out.println("n = " + n2);
		System.out.println("数学方法结果: " + countNumbersWithUniqueDigits(n2));
		System.out.println("数位DP方法结果: " + countNumbersWithUniqueDigitsDP(n2));
		// 预期输出: 1 (只有数字0)
	}
}
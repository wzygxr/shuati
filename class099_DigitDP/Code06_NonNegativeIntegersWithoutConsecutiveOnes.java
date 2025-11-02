package class084;

// 不含连续1的非负整数
// 给定一个正整数n，返回在[0, n]范围内不含连续1的非负整数的个数
// 测试链接 : https://leetcode.cn/problems/non-negative-integers-without-consecutive-ones/
public class Code06_NonNegativeIntegersWithoutConsecutiveOnes {

	// 数位DP方法
	// 时间复杂度: O(log n) 每个数位最多计算几次(受限/不受限, 前一位是0/1)
	// 空间复杂度: O(log n) 递归栈深度
	public static int findIntegers(int n) {
		// 将数字n转换为字符数组，方便按位处理
		char[] s = Integer.toBinaryString(n).toCharArray();
		int len = s.length;
		// dp[i][prev][isLimit][isNum] 
		// 表示处理到第i位，前一位是prev，当前是否受限制，是否已填数字时的方案数
		// -1表示未计算过
		int[][][][] dp = new int[len][2][2][2];
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < 2; j++) {
				for (int k = 0; k < 2; k++) {
					dp[i][j][k][0] = -1;
					dp[i][j][k][1] = -1;
				}
			}
		}
		return f(s, 0, 0, true, false, dp);
	}

	// s: 数字的二进制字符数组表示
	// i: 当前处理到第几位
	// prev: 前一位填的数字
	// isLimit: 当前位是否受到上限限制
	// isNum: 是否已开始填数字（处理前导零）
	// dp: 记忆化数组
	private static int f(char[] s, int i, int prev, boolean isLimit, boolean isNum, int[][][][] dp) {
		// 递归终止条件：已经处理完所有数位
		if (i == s.length) {
			// 如果已经填了数字，返回1个有效数字，否则返回0
			return isNum ? 1 : 0;
		}

		// 记忆化：如果已经计算过该状态，直接返回结果
		if (!isLimit && isNum && dp[i][prev][0][0] != -1) {
			return dp[i][prev][0][0];
		}

		// 确定当前位可以填入的数字范围
		// 如果受限制，最大只能填入s[i]对应的数字，否则可以填入0-1
		int up = isLimit ? s[i] - '0' : 1;
		int ans = 0;

		// 如果前面没有填数字，可以跳过当前位（继续前导零）
		if (!isNum) {
			ans += f(s, i + 1, 0, false, false, dp);
		}

		// 枚举当前位可以填入的数字
		for (int d = isNum ? 0 : 1; d <= up; d++) {
			// 不能有连续的1
			if (prev == 1 && d == 1) {
				continue;
			}
			// 递归处理下一位
			// 下一位是否受限制：当前位受限制且填入了上限值
			ans += f(s, i + 1, d, isLimit && d == up, true, dp);
		}

		// 记忆化存储结果
		if (!isLimit && isNum) {
			dp[i][prev][0][0] = ans;
		}
		return ans;
	}

	// 测试方法
	public static void main(String[] args) {
		// 测试用例1
		int n1 = 5;
		System.out.println("n = " + n1 + ", 不含连续1的非负整数个数: " + findIntegers(n1));
		// 预期输出: 5 (0, 1, 2, 4, 5 满足条件，3的二进制是11，不满足)

		// 测试用例2
		int n2 = 1;
		System.out.println("n = " + n2 + ", 不含连续1的非负整数个数: " + findIntegers(n2));
		// 预期输出: 2 (0, 1 满足条件)

		// 测试用例3
		int n3 = 2;
		System.out.println("n = " + n3 + ", 不含连续1的非负整数个数: " + findIntegers(n3));
		// 预期输出: 3 (0, 1, 2 满足条件)
	}
}
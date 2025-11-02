package class084;

// 统计数字1的个数
// 给定一个整数n，计算所有小于等于n的非负整数中数字1出现的个数
// 测试链接 : https://leetcode.cn/problems/number-of-digit-one/
public class Code05_CountDigitOne {

	// 数位DP方法
	// 时间复杂度: O(log n) 每个数位最多计算两次(受限/不受限)
	// 空间复杂度: O(log n) 递归栈深度
	public static int countDigitOne(int n) {
		if (n <= 0) {
			return 0;
		}
		// 将数字n转换为字符数组，方便按位处理
		char[] s = String.valueOf(n).toCharArray();
		int len = s.length;
		// dp[i][count][isLimit] 表示处理到第i位，已经出现了count个1，当前是否受限制时的方案数
		// -1表示未计算过
		int[][][] dp = new int[len][len][2];
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				dp[i][j][0] = -1;
				dp[i][j][1] = -1;
			}
		}
		return f(s, 0, 0, true, dp);
	}

	// s: 数字的字符数组表示
	// i: 当前处理到第几位
	// count: 当前已经统计到的1的个数
	// isLimit: 当前位是否受到上限限制
	// dp: 记忆化数组
	private static int f(char[] s, int i, int count, boolean isLimit, int[][][] dp) {
		// 递归终止条件：已经处理完所有数位
		if (i == s.length) {
			return count;
		}

		// 记忆化：如果已经计算过该状态，直接返回结果
		if (!isLimit && dp[i][count][0] != -1) {
			return dp[i][count][0];
		}

		// 确定当前位可以填入的数字范围
		// 如果受限制，最大只能填入s[i]对应的数字，否则可以填入0-9
		int up = isLimit ? s[i] - '0' : 9;
		int ans = 0;

		// 枚举当前位可以填入的数字
		for (int d = 0; d <= up; d++) {
			// 递归处理下一位
			// 如果当前位填入1，则count+1
			// 下一位是否受限制：当前位受限制且填入了上限值
			ans += f(s, i + 1, count + (d == 1 ? 1 : 0), isLimit && d == up, dp);
		}

		// 记忆化存储结果
		if (!isLimit) {
			dp[i][count][0] = ans;
		}
		return ans;
	}

	// 测试方法
	public static void main(String[] args) {
		// 测试用例1
		int n1 = 13;
		System.out.println("n = " + n1 + ", 数字1出现的次数: " + countDigitOne(n1));
		// 预期输出: 6 (数字1, 10, 11, 12, 13中1出现了6次)

		// 测试用例2
		int n2 = 0;
		System.out.println("n = " + n2 + ", 数字1出现的次数: " + countDigitOne(n2));
		// 预期输出: 0

		// 测试用例3
		int n3 = 100;
		System.out.println("n = " + n3 + ", 数字1出现的次数: " + countDigitOne(n3));
		// 预期输出: 21
	}
}
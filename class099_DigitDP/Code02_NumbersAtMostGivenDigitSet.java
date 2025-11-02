package class084;

// 最大为N的数字组合
// 给定一个按 非递减顺序 排列的数字数组 digits
// 已知digits一定不包含'0'，可能包含'1' ~ '9'，且无重复字符
// 你可以用任意次数 digits[i] 来写的数字
// 例如，如果 digits = ['1','3','5']
// 我们可以写数字，如 '13', '551', 和 '1351315'
// 返回 可以生成的小于或等于给定整数 n 的正整数的个数
// 测试链接 : https://leetcode.cn/problems/numbers-at-most-n-given-digit-set/
public class Code02_NumbersAtMostGivenDigitSet {

	/**
	 * 方法一：递归+记忆化搜索
	 * 时间复杂度: O(log n * 2 * 2 * |digits|)
	 * 空间复杂度: O(log n * 2 * 2)
	 * 
	 * 解题思路:
	 * 1. 将问题转化为数位DP问题
	 * 2. 逐位确定数字，确保生成的数字不超过n
	 * 3. 使用记忆化搜索避免重复计算
	 */
	public static int atMostNGivenDigitSet1(String[] strs, int num) {
		int tmp = num / 10;
		int len = 1;
		int offset = 1;
		while (tmp > 0) {
			tmp /= 10;
			len++;
			offset *= 10;
		}
		int m = strs.length;
		int[] digits = new int[m];
		for (int i = 0; i < m; i++) {
			digits[i] = Integer.valueOf(strs[i]);
		}
		return f1(digits, num, offset, len, 0, 0);
	}

	// offset是辅助变量，完全由len决定，只是为了方便提取num中某一位数字，不是关键变量
	// 还剩下len位没有决定
	// 如果之前的位已经确定比num小，那么free == 1，表示接下的数字可以自由选择
	// 如果之前的位和num一样，那么free == 0，表示接下的数字不能大于num当前位的数字
	// 如果之前的位没有使用过数字，fix == 0
	// 如果之前的位已经使用过数字，fix == 1
	// 返回最终<=num的可能性有多少种
	public static int f1(int[] digits, int num, int offset, int len, int free, int fix) {
		if (len == 0) {
			return fix == 1 ? 1 : 0;
		}
		int ans = 0;
		// num在当前位的数字
		int cur = (num / offset) % 10;
		if (fix == 0) {
			// 之前从来没有选择过数字
			// 当前依然可以不要任何数字，累加后续的可能性
			ans += f1(digits, num, offset / 10, len - 1, 1, 0);
		}
		if (free == 0) {
			// 不能自由选择的情况
			for (int i : digits) {
				if (i < cur) {
					ans += f1(digits, num, offset / 10, len - 1, 1, 1);
				} else if (i == cur) {
					ans += f1(digits, num, offset / 10, len - 1, 0, 1);
				} else {
					// i > cur
					break;
				}
			}
		} else {
			// 可以自由选择的情况
			ans += digits.length * f1(digits, num, offset / 10, len - 1, 1, 1);
		}
		return ans;
	}

	/**
	 * 方法二：优化的数位DP解法
	 * 时间复杂度: O(log n * |digits|)
	 * 空间复杂度: O(log n)
	 * 
	 * 解题思路:
	 * 1. 分两部分计算结果：
	 *    - 位数小于n的数字个数（可以直接计算）
	 *    - 位数等于n的数字个数（使用数位DP）
	 * 2. 使用预处理优化重复计算
	 */
	public static int atMostNGivenDigitSet2(String[] strs, int num) {
		int m = strs.length;
		int[] digits = new int[m];
		for (int i = 0; i < m; i++) {
			digits[i] = Integer.valueOf(strs[i]);
		}
		int len = 1;
		int offset = 1;
		int tmp = num / 10;
		while (tmp > 0) {
			tmp /= 10;
			len++;
			offset *= 10;
		}
		// cnt[i] : 已知前缀比num小，剩下i位没有确定，请问前缀确定的情况下，一共有多少种数字排列
		// cnt[0] = 1，表示后续已经没有了，前缀的状况都已确定，那么就是1种
		// cnt[1] = m
		// cnt[2] = m * m
		// cnt[3] = m * m * m
		// ...
		int[] cnt = new int[len];
		cnt[0] = 1;
		int ans = 0;
		for (int i = m, k = 1; k < len; k++, i *= m) {
			cnt[k] = i;
			ans += i;
		}
		return ans + f2(digits, cnt, num, offset, len);
	}

	// offset是辅助变量，由len确定，方便提取num中某一位数字
	// 还剩下len位没有决定，之前的位和num一样
	// 返回最终<=num的可能性有多少种
	public static int f2(int[] digits, int[] cnt, int num, int offset, int len) {
		if (len == 0) {
			// num自己
			return 1;
		}
		// cur是num当前位的数字
		int cur = (num / offset) % 10;
		int ans = 0;
		for (int i : digits) {
			if (i < cur) {
				ans += cnt[len - 1];
			} else if (i == cur) {
				ans += f2(digits, cnt, num, offset / 10, len - 1);
			} else {
				break;
			}
		}
		return ans;
	}
	
	/**
	 * 方法三：标准数位DP解法
	 * 时间复杂度: O(log n * 2 * 2 * |digits|)
	 * 空间复杂度: O(log n * 2 * 2)
	 * 
	 * 解题思路:
	 * 1. 使用标准数位DP框架
	 * 2. 用isLimit处理上界约束
	 * 3. 用isNum处理前导零
	 */
	public static int atMostNGivenDigitSet3(String[] digits, int n) {
		char[] s = String.valueOf(n).toCharArray();
		int m = digits.length;
		int[] digitValues = new int[m];
		for (int i = 0; i < m; i++) {
			digitValues[i] = digits[i].charAt(0) - '0';
		}
		
		// dp[i][isLimit][isNum] 表示处理到第i位，是否受限制，是否已开始填数字时的方案数
		int[][][] dp = new int[s.length][2][2];
		for (int i = 0; i < s.length; i++) {
			dp[i][0][0] = -1;
			dp[i][0][1] = -1;
			dp[i][1][0] = -1;
			dp[i][1][1] = -1;
		}
		
		return dfs(s, digitValues, 0, true, false, dp);
	}
	
	private static int dfs(char[] s, int[] digits, int pos, boolean isLimit, boolean isNum, int[][][] dp) {
		// 边界条件
		if (pos == s.length) {
			return isNum ? 1 : 0;
		}
		
		// 记忆化搜索
		if (!isLimit && isNum && dp[pos][0][0] != -1) {
			return dp[pos][0][0];
		}
		
		int ans = 0;
		
		// 如果还没开始填数字，可以选择跳过当前位（处理前导零）
		if (!isNum) {
			ans += dfs(s, digits, pos + 1, false, false, dp);
		}
		
		// 确定当前位可以填入的数字范围
		int up = isLimit ? s[pos] - '0' : 9;
		
		// 枚举当前位可以填入的数字（必须来自给定的digits数组）
		for (int d : digits) {
			if (d <= up) {
				// 递归处理下一位
				ans += dfs(s, digits, pos + 1, isLimit && d == up, true, dp);
			} else {
				// digits是排序的，后面的数字更大，可以提前退出
				break;
			}
		}
		
		// 记忆化存储
		if (!isLimit && isNum) {
			dp[pos][0][0] = ans;
		}
		
		return ans;
	}

	// 测试方法
	public static void main(String[] args) {
		// 测试用例1
		String[] digits1 = {"1", "3", "5", "7"};
		int n1 = 100;
		System.out.println("digits = [\"1\",\"3\",\"5\",\"7\"], n = " + n1);
		System.out.println("方法一结果: " + atMostNGivenDigitSet1(digits1, n1));
		System.out.println("方法二结果: " + atMostNGivenDigitSet2(digits1, n1));
		System.out.println("方法三结果: " + atMostNGivenDigitSet3(digits1, n1));
		// 预期输出: 20

		// 测试用例2
		String[] digits2 = {"1", "4", "9"};
		int n2 = 1000000000;
		System.out.println("digits = [\"1\",\"4\",\"9\"], n = " + n2);
		System.out.println("方法二结果: " + atMostNGivenDigitSet2(digits2, n2));
		System.out.println("方法三结果: " + atMostNGivenDigitSet3(digits2, n2));
		// 预期输出: 29523

		// 测试用例3
		String[] digits3 = {"7"};
		int n3 = 8;
		System.out.println("digits = [\"7\"], n = " + n3);
		System.out.println("方法一结果: " + atMostNGivenDigitSet1(digits3, n3));
		System.out.println("方法二结果: " + atMostNGivenDigitSet2(digits3, n3));
		System.out.println("方法三结果: " + atMostNGivenDigitSet3(digits3, n3));
		// 预期输出: 1
	}
}
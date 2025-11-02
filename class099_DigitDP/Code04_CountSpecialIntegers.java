package class084;

// 完全没有重复的数字个数
// 给定正整数n，返回在[1, n]范围内每一位都互不相同的正整数个数
// 测试链接 : https://leetcode.cn/problems/count-special-integers/
public class Code04_CountSpecialIntegers {

	/**
	 * 数位DP解法
	 * 时间复杂度: O(log n * 2^10)
	 * 空间复杂度: O(log n * 2^10)
	 * 
	 * 解题思路:
	 * 1. 使用数位DP框架
	 * 2. 用位掩码记录已使用的数字
	 * 3. 当前位不能选择已使用的数字
	 * 4. 通过记忆化搜索避免重复计算
	 */
	public static int countSpecialNumbers(int n) {
		int len = 1;
		int offset = 1;
		int tmp = n / 10;
		while (tmp > 0) {
			len++;
			offset *= 10;
			tmp /= 10;
		}
		// cnt[i] :
		// 一共长度为len，还剩i位没有确定，确定的前缀为len-i位，且确定的前缀不为空
		// 0~9一共10个数字，没有选择的数字剩下10-(len-i)个
		// 那么在后续的i位上，有多少种排列
		// 比如：len = 4
		// cnt[4]不计算
		// cnt[3] = 9 * 8 * 7
		// cnt[2] = 8 * 7
		// cnt[1] = 7
		// cnt[0] = 1，表示前缀已确定，后续也没有了，那么就是1种排列，就是前缀的状况
		// 再比如：len = 6
		// cnt[6]不计算
		// cnt[5] = 9 * 8 * 7 * 6 * 5
		// cnt[4] = 8 * 7 * 6 * 5
		// cnt[3] = 7 * 6 * 5
		// cnt[2] = 6 * 5
		// cnt[1] = 5
		// cnt[0] = 1，表示前缀已确定，后续也没有了，那么就是1种排列，就是前缀的状况
		// 下面for循环就是求解cnt的代码
		int[] cnt = new int[len];
		cnt[0] = 1;
		for (int i = 1, k = 10 - len + 1; i < len; i++, k++) {
			cnt[i] = cnt[i - 1] * k;
		}
		int ans = 0;
		if (len >= 2) {
			// 如果n的位数是len位，先计算位数少于len的数中，每一位都互不相同的正整数个数，并累加
			// 所有1位数中，每一位都互不相同的正整数个数 = 9
			// 所有2位数中，每一位都互不相同的正整数个数 = 9 * 9
			// 所有3位数中，每一位都互不相同的正整数个数 = 9 * 9 * 8
			// 所有4位数中，每一位都互不相同的正整数个数 = 9 * 9 * 8 * 7
			// ...比len少的位数都累加...
			ans = 9;
			for (int i = 2, a = 9, b = 9; i < len; i++, b--) {
				a *= b;
				ans += a;
			}
		}
		// 如果n的位数是len位，已经计算了位数少于len个的情况
		// 下面计算一定有len位的数字中，<=n且每一位都互不相同的正整数个数
		int first = n / offset;
		// 小于num最高位数字的情况
		ans += (first - 1) * cnt[len - 1];
		// 后续累加上，等于num最高位数字的情况
		ans += f(cnt, n, len - 1, offset / 10, 1 << first);
		return ans;
	}

	// 之前已经确定了和num一样的前缀，且确定的部分一定不为空
	// 还有len位没有确定
	// 哪些数字已经选了，哪些数字没有选，用status表示
	// 返回<=num且每一位数字都不一样的正整数有多少个
	public static int f(int[] cnt, int num, int len, int offset, int status) {
		if (len == 0) {
			// num自己
			return 1;
		}
		int ans = 0;
		// first是num当前位的数字
		int first = (num / offset) % 10;
		for (int cur = 0; cur < first; cur++) {
			if ((status & (1 << cur)) == 0) {
				ans += cnt[len - 1];
			}
		}
		if ((status & (1 << first)) == 0) {
			ans += f(cnt, num, len - 1, offset / 10, status | (1 << first));
		}
		return ans;
	}
	
	/**
	 * 标准数位DP解法
	 * 时间复杂度: O(log n * 2^10)
	 * 空间复杂度: O(log n * 2^10)
	 * 
	 * 解题思路:
	 * 1. 使用标准数位DP框架
	 * 2. 用位掩码记录已使用的数字
	 * 3. 当前位不能选择已使用的数字
	 * 4. 通过记忆化搜索避免重复计算
	 */
	public static int countSpecialNumbersDP(int n) {
		char[] s = String.valueOf(n).toCharArray();
		
		// dp[i][mask][isLimit][isNum] 
		// 表示处理到第i位，已使用数字的掩码为mask，是否受限制，是否已开始填数字时的方案数
		int[][][][] dp = new int[s.length][1 << 10][2][2];
		for (int i = 0; i < s.length; i++) {
			for (int j = 0; j < (1 << 10); j++) {
				dp[i][j][0][0] = -1;
				dp[i][j][0][1] = -1;
				dp[i][j][1][0] = -1;
				dp[i][j][1][1] = -1;
			}
		}
		
		return dfs(s, 0, 0, true, false, dp);
	}
	
	private static int dfs(char[] s, int pos, int mask, boolean isLimit, boolean isNum, int[][][][] dp) {
		// 边界条件
		if (pos == s.length) {
			return isNum ? 1 : 0;  // 如果已经填了数字，返回1个有效数字
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
		int n1 = 20;
		System.out.println("n = " + n1);
		System.out.println("原方法结果: " + countSpecialNumbers(n1));
		System.out.println("数位DP方法结果: " + countSpecialNumbersDP(n1));
		// 预期输出: 19 (除了11外的所有数字)

		// 测试用例2
		int n2 = 5;
		System.out.println("n = " + n2);
		System.out.println("原方法结果: " + countSpecialNumbers(n2));
		System.out.println("数位DP方法结果: " + countSpecialNumbersDP(n2));
		// 预期输出: 5 (1,2,3,4,5都满足条件)

		// 测试用例3
		int n3 = 100;
		System.out.println("n = " + n3);
		System.out.println("原方法结果: " + countSpecialNumbers(n3));
		System.out.println("数位DP方法结果: " + countSpecialNumbersDP(n3));
		// 预期输出: 90
	}
}
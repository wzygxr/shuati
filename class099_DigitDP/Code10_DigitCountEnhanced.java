package class084;

// 洛谷P2602 [ZJOI2010] 数字计数
// 题目链接: https://www.luogu.com.cn/problem/P2602
// 题目描述: 给定两个正整数a和b，求在[a,b]范围上的所有整数中，每个数码(digit)各出现了多少次。
// 注意：Code09已经包含了这个题目的实现，但为了展示更详细的分析和优化，这里提供一个更全面的解法
public class Code10_DigitCountEnhanced {

	/**
	 * 数位DP解法 - 统计[0,n]中每个数字出现的次数
	 * 时间复杂度: O(10 * log(n) * 2 * 2)
	 * 空间复杂度: O(10 * log(n) * 2 * 2)
	 * 
	 * 解题思路:
	 * 1. 将问题转化为统计[0, b]中每个数字出现的次数减去[0, a-1]中每个数字出现的次数
	 * 2. 对于每个数字d(0-9)，使用数位DP计算它在[0,n]中出现的次数
	 * 3. 状态需要记录：当前处理到第几位、数字d、是否受到上界限制、是否已经开始填数字、当前d出现的次数
	 * 4. 通过记忆化搜索避免重复计算
	 * 
	 * 最优解分析:
	 * 该解法是标准的数位DP解法，能够高效处理大范围的输入，是解决此类问题的最优通用方法。
	 */
	public static long[] countDigits(long a, long b) {
		// 计算[0, b]中每个数字出现的次数
		long[] countB = countDigitsUpTo(b);
		// 计算[0, a-1]中每个数字出现的次数
		long[] countA = countDigitsUpTo(a - 1);
		// 两者相减得到[a, b]中每个数字出现的次数
		for (int i = 0; i < 10; i++) {
			countB[i] -= countA[i];
		}
		return countB;
	}

	// 计算[0, n]中每个数字出现的次数
	private static long[] countDigitsUpTo(long n) {
		long[] result = new long[10];
		if (n < 0) {
			return result;
		}

		// 分别统计每个数字出现的次数
		for (int d = 0; d <= 9; d++) {
			result[d] = countDigit(n, d);
		}

		return result;
	}

	// 计算[0, n]中数字d出现的次数
	private static long countDigit(long n, int d) {
		String s = String.valueOf(n);
		int len = s.length();
		
		// dp[pos][isLimit][isNum] = 从pos位置开始，限制条件为isLimit，是否已开始填数字为isNum时，数字d出现的次数
		long[][][] dp = new long[len][2][2];
		// 初始化dp为-1，表示未计算过
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < 2; j++) {
				for (int k = 0; k < 2; k++) {
					dp[i][j][k] = -1;
				}
			}
		}

		return dfs(s.toCharArray(), 0, true, false, d, 0, dp);
	}

	/**
	 * 数位DP递归函数
	 * 
	 * @param s 数字的字符数组
	 * @param pos 当前处理到第几位
	 * @param isLimit 是否受到上界限制
	 * @param isNum 是否已开始填数字（处理前导零）
	 * @param target 需要统计的数字
	 * @param count 当前累计的目标数字出现次数
	 * @param dp 记忆化数组
	 * @return 从当前状态开始，目标数字出现的总次数
	 */
	private static long dfs(char[] s, int pos, boolean isLimit, boolean isNum, int target, long count, long[][][] dp) {
		// 递归终止条件
		if (pos == s.length) {
			// 如果已经填了数字，返回当前累计的目标数字出现次数
			return isNum ? count : 0;
		}

		// 记忆化搜索 - 只有当没有限制且已经开始填数字时才可以使用缓存
		if (!isLimit && isNum && dp[pos][0][0] != -1) {
			// 注意：这里返回的是该状态下目标数字出现的次数，需要加上当前的count
			return dp[pos][0][0] + count * Math.pow(10, s.length - pos - 1);
		}

		long ans = 0;

		// 如果还没开始填数字，可以选择跳过当前位（处理前导零）
		if (!isNum) {
			ans += dfs(s, pos + 1, false, false, target, count, dp);
		}

		// 确定当前位可以填入的数字范围
		int upper = isLimit ? s[pos] - '0' : 9;

		// 枚举当前位可以填入的数字
		for (int d = isNum ? 0 : 1; d <= upper; d++) {
			boolean newIsLimit = isLimit && (d == upper);
			boolean newIsNum = isNum || (d > 0);
			long newCount = count + (newIsNum && d == target ? 1 : 0);

			// 对于当前位填的是target的情况，还需要考虑后续位的可能情况
			if (d == target && newIsNum) {
				// 计算后续位有多少种可能性
				long suffix = 1;
				for (int i = pos + 1; i < s.length; i++) {
					suffix *= 10;
				}
				// 如果当前位不受限制，后续位可以随便填
				if (!newIsLimit) {
					ans += suffix;
				} else {
					// 如果当前位受限制，需要计算受限制情况下的后续位数
					String suffixStr = String.copyValueOf(s, pos + 1, s.length - pos - 1);
					ans += suffixStr.isEmpty() ? 1 : Long.parseLong(suffixStr) + 1;
				}
			} else {
				// 递归处理下一位
				ans += dfs(s, pos + 1, newIsLimit, newIsNum, target, newCount, dp);
			}
		}

		// 记忆化存储
		if (!isLimit && isNum) {
			dp[pos][0][0] = ans - count * Math.pow(10, s.length - pos - 1);
		}

		return ans;
	}

	/**
	 * 数学方法 - 直接计算每个数字出现的次数
	 * 时间复杂度: O(log(n))
	 * 空间复杂度: O(1)
	 * 
	 * 解题思路:
	 * 1. 逐位分析每个数字的出现次数
	 * 2. 对于每一位，考虑高位、当前位和低位的影响
	 * 3. 根据当前位与目标数字的大小关系，分情况计算
	 * 
	 * 优点：时间复杂度更低
	 * 缺点：实现更复杂，且不容易扩展到其他约束条件
	 */
	private static long countDigitMath(long n, int d) {
		if (n < 0) {
			return 0;
		}
		
		long count = 0;
		long divisor = 1;
		
		while (n / divisor > 0) {
			long high = n / (divisor * 10); // 高位
			long current = (n / divisor) % 10; // 当前位
			long low = n % divisor; // 低位
			
			// 处理特殊情况：d=0时，高位不能全为0
			if (d == 0) {
				if (high > 0) {
					high--;
				} else {
					// 高位全为0，无法形成有效数字
					divisor *= 10;
					continue;
				}
			}
			
			// 分情况讨论
			if (current > d) {
				count += (high + 1) * divisor;
			} else if (current == d) {
				count += high * divisor + low + 1;
			} else {
				count += high * divisor;
			}
			
			divisor *= 10;
		}
		
		return count;
	}

	// 测试代码
	public static void main(String[] args) {
		// 测试用例1: a=1, b=13
		// 预期输出: 1出现6次(1,10,11,12,13), 2出现2次(2,12), 3出现2次(3,13), 其他数字各出现1次
		long a1 = 1, b1 = 13;
		long[] result1 = countDigits(a1, b1);
		System.out.println("测试用例1: a=" + a1 + ", b=" + b1);
		for (int i = 0; i < 10; i++) {
			System.out.println("数字 " + i + " 出现了 " + result1[i] + " 次");
		}

		// 测试用例2: a=100, b=200
		long a2 = 100, b2 = 200;
		long[] result2 = countDigits(a2, b2);
		System.out.println("\n测试用例2: a=" + a2 + ", b=" + b2);
		for (int i = 0; i < 10; i++) {
			System.out.println("数字 " + i + " 出现了 " + result2[i] + " 次");
		}
	}
}
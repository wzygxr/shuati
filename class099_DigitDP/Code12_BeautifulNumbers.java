package class084;

// Codeforces 55D. Beautiful numbers
// 题目链接: https://codeforces.com/problemset/problem/55/D
// 题目描述: 如果一个正整数能被它的所有非零数字整除，那么这个数就是美丽的。给定区间[l,r]，求其中美丽数字的个数。
public class Code12_BeautifulNumbers {

	/**
	 * 数位DP解法
	 * 时间复杂度: O(log(r) * 2520 * 2520 * 2 * 2)
	 * 空间复杂度: O(log(r) * 2520 * 2520 * 2 * 2)
	 * 
	 * 解题思路:
	 * 1. 将问题转化为统计[0, r]中美丽数字的个数减去[0, l-1]中美丽数字的个数
	 * 2. 关键观察：一个数能被其所有非零数字整除等价于这个数能被这些数字的最小公倍数(LCM)整除
	 * 3. 由于1-9的最小公倍数是2520，而任意几个数字的LCM一定是2520的因数
	 * 4. 状态需要记录：当前处理到第几位、当前数字的最小公倍数、当前数字对2520的余数、是否受到上界限制、是否已经开始填数字
	 * 5. 通过记忆化搜索避免重复计算
	 * 
	 * 最优解分析:
	 * 该解法是标准的数位DP解法，通过数学观察（使用LCM和模数2520）来优化状态设计，
	 * 是解决此类问题的最优通用方法。时间复杂度中的2520来自于1-9的最小公倍数。
	 */
	private static final int MOD = 2520; // 1-9的最小公倍数

	public static long beautifulNumbers(long l, long r) {
		// 计算[0, r]中美丽数字的个数减去[0, l-1]中美丽数字的个数
		return countBeautifulNumbers(r) - countBeautifulNumbers(l - 1);
	}

	// 计算[0, n]中美丽数字的个数
	private static long countBeautifulNumbers(long n) {
		if (n < 1) {
			return 0; // 0不是正整数，不符合条件
		}

		String s = String.valueOf(n);
		int len = s.length();

		// 预处理：获取2520的所有因数
		int[] factors = new int[48]; // 2520有48个因数
		int idx = 0;
		for (int i = 1; i <= MOD; i++) {
			if (MOD % i == 0) {
				factors[idx++] = i;
			}
		}

		// 映射函数：将LCM值映射到其在factors数组中的索引
		int[] lcmToIndex = new int[MOD + 1];
		for (int i = 0; i < factors.length; i++) {
			lcmToIndex[factors[i]] = i;
		}

		// dp[pos][lcm][mod][isLimit][isNum]
		// pos: 当前处理到第几位
		// lcm: 当前数字的最小公倍数的索引
		// mod: 当前数字对MOD的余数
		// isLimit: 是否受到上界限制
		// isNum: 是否已开始填数字
		long[][][][] dp = new long[len][factors.length][MOD][2];

		// 初始化dp为-1，表示未计算过
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < factors.length; j++) {
				for (int k = 0; k < MOD; k++) {
					for (int l = 0; l < 2; l++) {
						dp[i][j][k][l] = -1;
					}
				}
			}
		}

		return dfs(s.toCharArray(), 0, 0, 0, true, false, factors, lcmToIndex, dp);
	}

	/**
	 * 计算两个数的最大公约数
	 */
	private static int gcd(int a, int b) {
		return b == 0 ? a : gcd(b, a % b);
	}

	/**
	 * 计算两个数的最小公倍数
	 */
	private static int lcm(int a, int b) {
		if (a == 0 || b == 0) {
			return a + b;
		}
		return a / gcd(a, b) * b;
	}

	/**
	 * 数位DP递归函数
	 * 
	 * @param digits 数字的字符数组
	 * @param pos 当前处理到第几位
	 * @param lcmIndex 当前数字的最小公倍数的索引
	 * @param mod 当前数字对MOD的余数
	 * @param isLimit 是否受到上界限制
	 * @param isNum 是否已开始填数字（处理前导零）
	 * @param factors MOD的因数数组
	 * @param lcmToIndex LCM到索引的映射
	 * @param dp 记忆化数组
	 * @return 从当前状态开始，美丽数字的个数
	 */
	private static long dfs(char[] digits, int pos, int lcmIndex, int mod, boolean isLimit, boolean isNum, 
					  int[] factors, int[] lcmToIndex, long[][][][] dp) {
		// 递归终止条件
		if (pos == digits.length) {
			// 只有当已经填了数字，且当前数字能被其最小公倍数整除时才算美丽数字
			return isNum && (mod % factors[lcmIndex] == 0) ? 1 : 0;
		}

		// 记忆化搜索
		if (!isLimit && isNum && dp[pos][lcmIndex][mod][0] != -1) {
			return dp[pos][lcmIndex][mod][0];
		}

		long ans = 0;

		// 如果还没开始填数字，可以选择跳过当前位（处理前导零）
		if (!isNum) {
			ans += dfs(digits, pos + 1, lcmIndex, mod, false, false, factors, lcmToIndex, dp);
		}

		// 确定当前位可以填入的数字范围
		int upper = isLimit ? digits[pos] - '0' : 9;

		// 枚举当前位可以填入的数字
		for (int d = isNum ? 0 : 1; d <= upper; d++) {
			boolean newIsLimit = isLimit && (d == upper);
			boolean newIsNum = isNum || (d > 0);
			
			int newMod = (mod * 10 + d) % MOD;
			int newLcmIndex;
			
			if (!newIsNum) {
				// 还没有开始填数字，LCM保持不变
				newLcmIndex = lcmIndex;
			} else if (!isNum) {
				// 第一次填数字
				newLcmIndex = lcmToIndex[d];
			} else if (d == 0) {
				// 当前位是0，不影响LCM
				newLcmIndex = lcmIndex;
			} else {
				// 计算新的LCM
				int currentLcm = factors[lcmIndex];
				int newLcm = lcm(currentLcm, d);
				newLcmIndex = lcmToIndex[newLcm];
			}

			// 递归处理下一位
			ans += dfs(digits, pos + 1, newLcmIndex, newMod, newIsLimit, newIsNum, factors, lcmToIndex, dp);
		}

		// 记忆化存储
		if (!isLimit && isNum) {
			dp[pos][lcmIndex][mod][0] = ans;
		}

		return ans;
	}

	/**
	 * 优化版本：使用更紧凑的状态设计
	 * 1. 由于我们只关心当前数字是否能被其非零数字的LCM整除，
	 *    我们可以直接记录当前数字的LCM，而不是其索引
	 * 2. 使用哈希表或数组来存储已经计算过的状态
	 */
	private static long countBeautifulNumbersOptimized(long n) {
		if (n < 1) {
			return 0;
		}

		String s = String.valueOf(n);
		int len = s.length();

		// dp[pos][lcm][mod][isLimit][isNum]
		long[][][][] dp = new long[len][MOD + 1][MOD][2];

		// 初始化dp为-1，表示未计算过
		for (int i = 0; i < len; i++) {
			for (int j = 0; j <= MOD; j++) {
				for (int k = 0; k < MOD; k++) {
					for (int l = 0; l < 2; l++) {
						dp[i][j][k][l] = -1;
					}
				}
			}
		}

		return dfsOptimized(s.toCharArray(), 0, 1, 0, true, false, dp);
	}

	private static long dfsOptimized(char[] digits, int pos, int currentLcm, int mod, boolean isLimit, boolean isNum, 
					    long[][][][] dp) {
		// 递归终止条件
		if (pos == digits.length) {
			// 只有当已经填了数字，且当前数字能被其最小公倍数整除时才算美丽数字
			return isNum && (mod % currentLcm == 0) ? 1 : 0;
		}

		// 记忆化搜索
		if (!isLimit && isNum && dp[pos][currentLcm][mod][0] != -1) {
			return dp[pos][currentLcm][mod][0];
		}

		long ans = 0;

		// 如果还没开始填数字，可以选择跳过当前位（处理前导零）
		if (!isNum) {
			ans += dfsOptimized(digits, pos + 1, currentLcm, mod, false, false, dp);
		}

		// 确定当前位可以填入的数字范围
		int upper = isLimit ? digits[pos] - '0' : 9;

		// 枚举当前位可以填入的数字
		for (int d = isNum ? 0 : 1; d <= upper; d++) {
			boolean newIsLimit = isLimit && (d == upper);
			boolean newIsNum = isNum || (d > 0);
			
			int newMod = (mod * 10 + d) % MOD;
			int newLcm = currentLcm;
			
			if (newIsNum && d > 0) {
				// 计算新的LCM
				newLcm = lcm(currentLcm, d);
			}

			// 递归处理下一位
			ans += dfsOptimized(digits, pos + 1, newLcm, newMod, newIsLimit, newIsNum, dp);
		}

		// 记忆化存储
		if (!isLimit && isNum) {
			dp[pos][currentLcm][mod][0] = ans;
		}

		return ans;
	}

	// 测试代码
	public static void main(String[] args) {
		// 测试用例1: l=1, r=10
		// 预期输出: 10 (所有数字都能被其非零数字整除)
		long l1 = 1, r1 = 10;
		long result1 = beautifulNumbers(l1, r1);
		System.out.println("测试用例1: l=" + l1 + ", r=" + r1);
		System.out.println("美丽数字的个数: " + result1);

		// 测试用例2: l=12, r=15
		// 预期输出: 2 (12能被1和2整除，15能被1和5整除，但13不能被3整除，14不能被4整除)
		long l2 = 12, r2 = 15;
		long result2 = beautifulNumbers(l2, r2);
		System.out.println("\n测试用例2: l=" + l2 + ", r=" + r2);
		System.out.println("美丽数字的个数: " + result2);
	}
}
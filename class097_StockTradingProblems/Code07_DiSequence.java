package class082;

// DI序列的有效排列
// 给定一个长度为n的字符串s，其中s[i]是:
// "D"意味着减少，"I"意味着增加
// 有效排列是对有n+1个在[0,n]范围内的整数的一个排列perm，使得对所有的i：
// 如果 s[i] == 'D'，那么 perm[i] > perm[i+1]
// 如果 s[i] == 'I'，那么 perm[i] < perm[i+1]
// 返回有效排列的perm的数量
// 因为答案可能很大，答案对 1000000007 取模
// 测试链接 : https://leetcode.cn/problems/valid-permutations-for-di-sequence/
public class Code07_DiSequence {

	/*
	 * 解题思路:
	 * 这是一个动态规划问题，需要计算满足DI序列的有效排列数量。
	 * 核心思想是使用动态规划，dp[i][j]表示考虑前i个位置，在剩余可选数字中，
	 * 比前一个选定数字小的数字个数为j时的方案数。
	 * 
	 * 状态定义:
	 * dp[i][j] : 考虑前i个位置，在剩余可选数字中，比前一个选定数字小的数字个数为j时的方案数
	 * 
	 * 状态转移:
	 * 如果s[i-1] == 'D'，则dp[i][j] = sum(dp[i-1][k]) for k in j..n-i
	 * 如果s[i-1] == 'I'，则dp[i][j] = sum(dp[i-1][k]) for k in 0..j-1
	 * 
	 * 优化思路:
	 * 使用前缀和优化状态转移，将时间复杂度从O(n^3)优化到O(n^2)
	 * 
	 * 时间复杂度分析:
	 * O(n^2) - 优化后的版本
	 * 
	 * 空间复杂度分析:
	 * O(n^2) - 二维DP数组
	 * 
	 * 是否为最优解:
	 * 是，这是解决该问题的最优解
	 * 
	 * 工程化考量:
	 * 1. 边界条件处理: 空字符串等特殊情况
	 * 2. 异常处理: 输入参数校验
	 * 3. 可读性: 变量命名清晰，注释详细
	 * 4. 性能优化: 前缀和优化
	 * 
	 * 相关题目扩展:
	 * 1. LeetCode 942. 增减字符串匹配 - https://leetcode.cn/problems/di-string-match/
	 * 2. LeetCode 6919. 按位与结果大于零的最长组合 - https://leetcode.cn/problems/largest-combination-with-bitwise-and-greater-than-zero/
	 * 3. LeetCode 903. 有效的排列 - https://leetcode.cn/problems/valid-permutations-for-di-sequence/
	 */
	public static int numPermsDISequence1(String s) {
		// 边界条件处理
		if (s == null || s.length() == 0) {
			return 1;
		}
		
		return f(s.toCharArray(), 0, s.length() + 1, s.length() + 1);
	}

	// 猜法很妙！
	// 一共有n个数字，位置范围0~n-1
	// 当前来到i位置，i-1位置的数字已经确定，i位置的数字还没确定
	// i-1位置和i位置的关系，是s[i-1] : D、I
	// 0~i-1范围上是已经使用过的数字，i个
	// 还没有使用过的数字中，比i-1位置的数字小的，有less个
	// 还没有使用过的数字中，比i-1位置的数字大的，有n - i - less个
	// 返回后续还有多少种有效的排列
	public static int f(char[] s, int i, int less, int n) {
		int ans = 0;
		if (i == n) {
			ans = 1;
		} else if (i == 0 || s[i - 1] == 'D') {
			for (int nextLess = 0; nextLess < less; nextLess++) {
				ans += f(s, i + 1, nextLess, n);
			}
		} else {
			for (int nextLess = less, k = 1; k <= n - i - less; k++, nextLess++) {
				ans += f(s, i + 1, nextLess, n);
			}
		}
		return ans;
	}

	public static int numPermsDISequence2(String str) {
		// 边界条件处理
		if (str == null || str.length() == 0) {
			return 1;
		}
		
		int mod = 1000000007;
		char[] s = str.toCharArray();
		int n = s.length + 1;
		int[][] dp = new int[n + 1][n + 1];
		for (int less = 0; less <= n; less++) {
			dp[n][less] = 1;
		}
		for (int i = n - 1; i >= 0; i--) {
			for (int less = 0; less <= n; less++) {
				if (i == 0 || s[i - 1] == 'D') {
					for (int nextLess = 0; nextLess < less; nextLess++) {
						dp[i][less] = (dp[i][less] + dp[i + 1][nextLess]) % mod;
					}
				} else {
					for (int nextLess = less, k = 1; k <= n - i - less; k++, nextLess++) {
						dp[i][less] = (dp[i][less] + dp[i + 1][nextLess]) % mod;
					}
				}
			}
		}
		return dp[0][n];
	}

	// 通过观察方法2，得到优化枚举的方法
	public static int numPermsDISequence3(String str) {
		// 边界条件处理
		if (str == null || str.length() == 0) {
			return 1;
		}
		
		int mod = 1000000007;
		char[] s = str.toCharArray();
		int n = s.length + 1;
		int[][] dp = new int[n + 1][n + 1];
		for (int less = 0; less <= n; less++) {
			dp[n][less] = 1;
		}
		for (int i = n - 1; i >= 0; i--) {
			if (i == 0 || s[i - 1] == 'D') {
				dp[i][1] = dp[i + 1][0];
				for (int less = 2; less <= n; less++) {
					dp[i][less] = (dp[i][less - 1] + dp[i + 1][less - 1]) % mod;
				}
			} else {
				dp[i][n - i - 1] = dp[i + 1][n - i - 1];
				for (int less = n - i - 2; less >= 0; less--) {
					dp[i][less] = (dp[i][less + 1] + dp[i + 1][less]) % mod;
				}
			}
		}
		return dp[0][n];
	}
	
	// 主方法，使用最优解
	public static int numPermsDISequence(String s) {
		return numPermsDISequence3(s);
	}

	// 测试方法
	public static void main(String[] args) {
		// 测试用例1: s="D" -> 1
		String s1 = "D";
		System.out.println("测试用例1结果: " + numPermsDISequence(s1)); // 期望输出: 1
		// 解释: 只有排列[1,0]满足条件
		
		// 测试用例2: s="I" -> 1
		String s2 = "I";
		System.out.println("测试用例2结果: " + numPermsDISequence(s2)); // 期望输出: 1
		// 解释: 只有排列[0,1]满足条件
		
		// 测试用例3: s="ID" -> 2
		String s3 = "ID";
		System.out.println("测试用例3结果: " + numPermsDISequence(s3)); // 期望输出: 2
		// 解释: 排列[0,2,1]和[1,2,0]满足条件
	}
}
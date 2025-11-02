package class076;

/**
 * LeetCode 面试题 08.14. 布尔运算
 * 题目链接：https://leetcode.cn/problems/boolean-evaluation-lcci/
 * 
 * 题目描述：
 * 给定一个布尔表达式和一个期望的布尔结果 result。
 * 布尔表达式由 0 (false)、1 (true)、& (AND)、 | (OR) 和 ^ (XOR) 符号组成。
 * 布尔表达式一定是正确的，不需要检查有效性。
 * 但是其中没有任何括号来表示优先级。
 * 你可以随意添加括号来改变逻辑优先级。
 * 目的是让表达式能够最终得出 result 的结果。
 * 返回最终得出 result 有多少种不同的逻辑计算顺序。
 * 
 * 解题思路：
 * 这是一个区间动态规划问题，需要计算每个子表达式能得到 true 和 false 的方案数。
 * 定义状态 dp[i][j][0/1] 表示子表达式 s[i...j] 得到 false/true 的方案数。
 * 状态转移方程：
 * 枚举每个运算符作为最后计算的运算符，将表达式分为左右两部分：
 * 1. AND 运算：dp[i][j][1] += dp[i][k-1][1] * dp[k+1][j][1]
 *    dp[i][j][0] += dp[i][k-1][0] * dp[k+1][j][0] + dp[i][k-1][0] * dp[k+1][j][1] + dp[i][k-1][1] * dp[k+1][j][0]
 * 2. OR 运算：dp[i][j][1] += dp[i][k-1][1] * dp[k+1][j][1] + dp[i][k-1][0] * dp[k+1][j][1] + dp[i][k-1][1] * dp[k+1][j][0]
 *    dp[i][j][0] += dp[i][k-1][0] * dp[k+1][j][0]
 * 3. XOR 运算：dp[i][j][1] += dp[i][k-1][0] * dp[k+1][j][1] + dp[i][k-1][1] * dp[k+1][j][0]
 *    dp[i][j][0] += dp[i][k-1][0] * dp[k+1][j][0] + dp[i][k-1][1] * dp[k+1][j][1]
 * 
 * 时间复杂度：O(n³)
 * 空间复杂度：O(n³)
 * 
 * 工程化考量：
 * 1. 边界条件处理：单个字符的情况
 * 2. 优化：可以使用记忆化搜索减少重复计算
 * 3. 输入验证：检查表达式是否符合格式要求
 * 
 * 相关题目扩展：
 * 1. LeetCode 面试题 08.14. 布尔运算 - https://leetcode.cn/problems/boolean-evaluation-lcci/
 * 2. LeetCode 224. 基本计算器 - https://leetcode.cn/problems/basic-calculator/
 * 3. LeetCode 227. 基本计算器 II - https://leetcode.cn/problems/basic-calculator-ii/
 * 4. LeetCode 772. 基本计算器 III - https://leetcode.cn/problems/basic-calculator-iii/
 * 5. LeetCode 150. 逆波兰表达式求值 - https://leetcode.cn/problems/evaluate-reverse-polish-notation/
 * 6. LintCode 1494. 布尔运算 - https://www.lintcode.com/problem/1494/
 * 7. LintCode 978. 基本计算器 - https://www.lintcode.com/problem/978/
 * 8. HackerRank - Arithmetic Expressions - https://www.hackerrank.com/challenges/arithmetic-expressions/problem
 * 9. Codeforces 1327D - Infinite Path - https://codeforces.com/problemset/problem/1327/D
 * 10. AtCoder ABC144D - Water Bottle - https://atcoder.jp/contests/abc144/tasks/abc144_d
 */
public class Code06_BooleanEvaluation {

	// 记忆化搜索
	public static int countEval(String str, int result) {
		char[] s = str.toCharArray();
		int n = s.length;
		int[][][] dp = new int[n][n][];
		int[] ft = f(s, 0, n - 1, dp);
		return ft[result];
	}

	// s[l...r]是表达式的一部分，且一定符合范式
	// 0/1  逻  0/1   逻       0/1
	//  l  l+1  l+2  l+3........r
	// s[l...r]  0 : ?
	//           1 : ?
	// ans : int[2] ans[0] = false方法数 ans[0] = true方法数
	public static int[] f(char[] s, int l, int r, int[][][] dp) {
		if (dp[l][r] != null) {
			return dp[l][r];
		}
		int f = 0;
		int t = 0;
		if (l == r) {
			// 只剩一个字符，0/1
			f = s[l] == '0' ? 1 : 0;
			t = s[l] == '1' ? 1 : 0;
		} else {
			int[] tmp;
			for (int k = l + 1, a, b, c, d; k < r; k += 2) {
				// l ... r
				// 枚举每一个逻辑符号最后执行 k = l+1 ... r-1  k+=2
				tmp = f(s, l, k - 1, dp);
				a = tmp[0];
				b = tmp[1];
				tmp = f(s, k + 1, r, dp);
				c = tmp[0];
				d = tmp[1];
				if (s[k] == '&') {
					f += a * c + a * d + b * c;
					t += b * d;
				} else if (s[k] == '|') {
					f += a * c;
					t += a * d + b * c + b * d;
				} else {
					f += a * c + b * d;
					t += a * d + b * c;
				}
			}
		}
		int[] ft = new int[] { f, t };
		dp[l][r] = ft;
		return ft;
	}

}
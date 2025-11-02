package class076;

/**
 * LeetCode 1312. 让字符串成为回文串的最少插入次数
 * 题目链接：https://leetcode.cn/problems/minimum-insertion-steps-to-make-a-string-palindrome/
 * 
 * 题目描述：
 * 给你一个字符串 s，每一次操作你都可以在字符串的任意位置插入任意字符。
 * 请你返回让 s 成为回文串的最少操作次数。
 * 
 * 解题思路：
 * 这是一个经典的区间动态规划问题。我们可以定义状态 dp[i][j] 表示将子串 s[i...j] 变成回文串所需的最少插入次数。
 * 状态转移方程：
 * 1. 如果 s[i] == s[j]，则 dp[i][j] = dp[i+1][j-1]
 * 2. 如果 s[i] != s[j]，则 dp[i][j] = min(dp[i+1][j], dp[i][j-1]) + 1
 * 
 * 时间复杂度：O(n²)
 * 空间复杂度：O(n²)
 * 
 * 工程化考量：
 * 1. 边界条件处理：单个字符本身就是回文串
 * 2. 空间优化：可以使用滚动数组将空间复杂度优化到 O(n)
 * 3. 输入验证：检查字符串是否为空
 * 
 * 相关题目扩展：
 * 1. LeetCode 1312. 让字符串成为回文串的最少插入次数 - https://leetcode.cn/problems/minimum-insertion-steps-to-make-a-string-palindrome/
 * 2. LeetCode 516. 最长回文子序列 - https://leetcode.cn/problems/longest-palindromic-subsequence/
 * 3. LeetCode 1216. 验证回文字符串 III - https://leetcode.cn/problems/valid-palindrome-iii/
 * 4. LeetCode 1246. 删除回文子数组 - https://leetcode.cn/problems/palindrome-removal/
 * 5. LeetCode 1682. 最长回文子序列 II - https://leetcode.cn/problems/longest-palindromic-subsequence-ii/
 * 6. LintCode 1419. 最少行程 - https://www.lintcode.com/problem/1419/
 * 7. LintCode 1797. 模糊坐标 - https://www.lintcode.com/problem/1797/
 * 8. HackerRank - Palindrome Index - https://www.hackerrank.com/challenges/palindrome-index/problem
 * 9. Codeforces 1373C - Pluses and Minuses - https://codeforces.com/problemset/problem/1373/C
 * 10. AtCoder ABC161D - Lunlun Number - https://atcoder.jp/contests/abc161/tasks/abc161_d
 */
public class Code01_MinimumInsertionToPalindrome {

	// 暴力尝试
	public static int minInsertions1(String str) {
		char[] s = str.toCharArray();
		int n = s.length;
		return f1(s, 0, n - 1);
	}

	// s[l....r]这个范围上的字符串，整体都变成回文串
	// 返回至少插入几个字符
	public static int f1(char[] s, int l, int r) {
		// l <= r
		if (l == r) {
			return 0;
		}
		if (l + 1 == r) {
			return s[l] == s[r] ? 0 : 1;
		}
		// l...r不只两个字符
		if (s[l] == s[r]) {
			return f1(s, l + 1, r - 1);
		} else {
			return Math.min(f1(s, l, r - 1), f1(s, l + 1, r)) + 1;
		}
	}

	// 记忆化搜索
	public static int minInsertions2(String str) {
		char[] s = str.toCharArray();
		int n = s.length;
		int[][] dp = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = i; j < n; j++) {
				dp[i][j] = -1;
			}
		}
		return f2(s, 0, n - 1, dp);
	}

	public static int f2(char[] s, int l, int r, int[][] dp) {
		if (dp[l][r] != -1) {
			return dp[l][r];
		}
		int ans;
		if (l == r) {
			ans = 0;
		} else if (l + 1 == r) {
			ans = s[l] == s[l + 1] ? 0 : 1;
		} else {
			if (s[l] == s[r]) {
				ans = f2(s, l + 1, r - 1, dp);
			} else {
				ans = Math.min(f2(s, l, r - 1, dp), f2(s, l + 1, r, dp)) + 1;
			}
		}
		dp[l][r] = ans;
		return ans;
	}

	// 严格位置依赖的动态规划
	public static int minInsertions3(String str) {
		char[] s = str.toCharArray();
		int n = s.length;
		int[][] dp = new int[n][n];
		for (int l = 0; l < n - 1; l++) {
			dp[l][l + 1] = s[l] == s[l + 1] ? 0 : 1;
		}
		for (int l = n - 3; l >= 0; l--) {
			for (int r = l + 2; r < n; r++) {
				if (s[l] == s[r]) {
					dp[l][r] = dp[l + 1][r - 1];
				} else {
					dp[l][r] = Math.min(dp[l][r - 1], dp[l + 1][r]) + 1;
				}
			}
		}
		return dp[0][n - 1];
	}

	// 空间压缩
	// 本题有关空间压缩的实现，可以参考讲解067，题目4，最长回文子序列问题的讲解
	// 这两个题空间压缩写法高度相似
	// 因为之前的课多次讲过空间压缩的内容，所以这里不再赘述
	public static int minInsertions4(String str) {
		char[] s = str.toCharArray();
		int n = s.length;
		if (n < 2) {
			return 0;
		}
		int[] dp = new int[n];
		dp[n - 1] = s[n - 2] == s[n - 1] ? 0 : 1;
		for (int l = n - 3, leftDown, backUp; l >= 0; l--) {
			leftDown = dp[l + 1];
			dp[l + 1] = s[l] == s[l + 1] ? 0 : 1;
			for (int r = l + 2; r < n; r++) {
				backUp = dp[r];
				if (s[l] == s[r]) {
					dp[r] = leftDown;
				} else {
					dp[r] = Math.min(dp[r - 1], dp[r]) + 1;
				}
				leftDown = backUp;
			}
		}
		return dp[n - 1];
	}

}
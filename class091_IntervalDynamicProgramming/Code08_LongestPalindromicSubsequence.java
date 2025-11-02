package class076;

/**
 * LeetCode 516. 最长回文子序列
 * 题目链接：https://leetcode.cn/problems/longest-palindromic-subsequence/
 * 
 * 题目描述：
 * 给你一个字符串 s ，找出其中最长的回文子序列，并返回该序列的长度。
 * 子序列定义为：不改变剩余字符顺序的情况下，删除某些字符或者不删除任何字符形成的一个序列。
 * 
 * 解题思路：
 * 这是一个经典的区间动态规划问题。
 * 定义状态 dp[i][j] 表示子串 s[i...j] 中最长回文子序列的长度。
 * 状态转移方程：
 * 1. 如果 s[i] == s[j]，则 dp[i][j] = dp[i+1][j-1] + 2
 * 2. 如果 s[i] != s[j]，则 dp[i][j] = max(dp[i+1][j], dp[i][j-1])
 * 
 * 时间复杂度：O(n²)
 * 空间复杂度：O(n²)
 * 
 * 工程化考量：
 * 1. 边界条件处理：单个字符的回文子序列长度为1，两个字符相等时长度为2
 * 2. 优化：可以使用空间优化将空间复杂度降低到 O(n)
 * 3. 输入验证：检查字符串是否为空
 * 
 * 相关题目扩展：
 * 1. LeetCode 516. 最长回文子序列 - https://leetcode.cn/problems/longest-palindromic-subsequence/
 * 2. LeetCode 1312. 让字符串成为回文串的最少插入次数 - https://leetcode.cn/problems/minimum-insertion-steps-to-make-a-string-palindrome/
 * 3. LeetCode 1216. 验证回文字符串 III - https://leetcode.cn/problems/valid-palindrome-iii/
 * 4. LeetCode 1246. 删除回文子数组 - https://leetcode.cn/problems/palindrome-removal/
 * 5. LeetCode 1682. 最长回文子序列 II - https://leetcode.cn/problems/longest-palindromic-subsequence-ii/
 * 6. LintCode 1419. 最少行程 - https://www.lintcode.com/problem/1419/
 * 7. LintCode 1797. 模糊坐标 - https://www.lintcode.com/problem/1797/
 * 8. HackerRank - Palindrome Index - https://www.hackerrank.com/challenges/palindrome-index/problem
 * 9. Codeforces 1373C - Pluses and Minuses - https://codeforces.com/problemset/problem/1373/C
 * 10. AtCoder ABC161D - Lunlun Number - https://atcoder.jp/contests/abc161/tasks/abc161_d
 */
public class Code08_LongestPalindromicSubsequence {

	// 解法一：记忆化搜索
	public static int longestPalindromeSubseq1(String s) {
		char[] str = s.toCharArray();
		int n = str.length;
		int[][] dp = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				dp[i][j] = -1;
			}
		}
		return f(str, 0, n - 1, dp);
	}
	
	// str[l...r]范围上最长回文子序列长度
	public static int f(char[] str, int l, int r, int[][] dp) {
		if (dp[l][r] != -1) {
			return dp[l][r];
		}
		int ans = 0;
		if (l == r) {
			ans = 1;
		} else if (l + 1 == r) {
			ans = str[l] == str[r] ? 2 : 1;
		} else {
			if (str[l] == str[r]) {
				ans = f(str, l + 1, r - 1, dp) + 2;
			} else {
				ans = Math.max(f(str, l + 1, r, dp), f(str, l, r - 1, dp));
			}
		}
		dp[l][r] = ans;
		return ans;
	}
	
	// 解法二：严格位置依赖的动态规划
	public static int longestPalindromeSubseq2(String s) {
		char[] str = s.toCharArray();
		int n = str.length;
		int[][] dp = new int[n][n];
		
		// 初始化
		for (int i = 0; i < n; i++) {
			dp[i][i] = 1;
		}
		for (int i = 0; i < n - 1; i++) {
			dp[i][i + 1] = str[i] == str[i + 1] ? 2 : 1;
		}
		
		// 填表
		for (int l = n - 3; l >= 0; l--) {
			for (int r = l + 2; r < n; r++) {
				if (str[l] == str[r]) {
					dp[l][r] = dp[l + 1][r - 1] + 2;
				} else {
					dp[l][r] = Math.max(dp[l + 1][r], dp[l][r - 1]);
				}
			}
		}
		
		return dp[0][n - 1];
	}
	
	// 解法三：空间压缩
	public static int longestPalindromeSubseq3(String s) {
		char[] str = s.toCharArray();
		int n = str.length;
		int[] dp = new int[n];
		
		// 初始化
		for (int i = 0; i < n; i++) {
			dp[i] = 1;
		}
		
		// 填表
		for (int l = n - 2, leftDown; l >= 0; l--) {
			leftDown = 0;
			for (int r = l + 1; r < n; r++) {
				int tmp = dp[r];
				if (str[l] == str[r]) {
					dp[r] = leftDown + 2;
				} else {
					dp[r] = Math.max(dp[r], dp[r - 1]);
				}
				leftDown = tmp;
			}
		}
		
		return dp[n - 1];
	}
	
	// 测试函数
	public static void main(String[] args) {
		String s1 = "bbbab";
		System.out.println("字符串: " + s1);
		System.out.println("最长回文子序列长度 (解法一): " + longestPalindromeSubseq1(s1));
		System.out.println("最长回文子序列长度 (解法二): " + longestPalindromeSubseq2(s1));
		System.out.println("最长回文子序列长度 (解法三): " + longestPalindromeSubseq3(s1));
		
		String s2 = "cbbd";
		System.out.println("\n字符串: " + s2);
		System.out.println("最长回文子序列长度 (解法一): " + longestPalindromeSubseq1(s2));
		System.out.println("最长回文子序列长度 (解法二): " + longestPalindromeSubseq2(s2));
		System.out.println("最长回文子序列长度 (解法三): " + longestPalindromeSubseq3(s2));
	}

}
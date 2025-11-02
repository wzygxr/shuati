package class077;

// 最长回文子序列
// 给你一个字符串 s ，找出其中最长的回文子序列，并返回该序列的长度。
// 子序列定义为：不改变剩余字符顺序的情况下，删除某些字符或者不删除任何字符形成的一个序列。
// 测试链接 : https://leetcode.cn/problems/longest-palindromic-subsequence/
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Code09_LongestPalindromicSubsequence {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		String str = br.readLine();
		out.println(longestPalindromeSubseq(str));
		out.flush();
		out.close();
		br.close();
	}

	// 区间动态规划解法
	// 时间复杂度: O(n^2) - 两层循环：区间长度、区间起点
	// 空间复杂度: O(n^2) - dp数组占用空间
	// 解题思路:
	// 1. 状态定义：dp[i][j]表示字符串s在区间[i,j]内最长回文子序列的长度
	// 2. 状态转移：
	//    - 如果s[i] == s[j]，则dp[i][j] = dp[i+1][j-1] + 2
	//    - 如果s[i] != s[j]，则dp[i][j] = max(dp[i+1][j], dp[i][j-1])
	// 3. 工程化考虑：
	//    - 异常处理：检查输入合法性
	//    - 边界处理：正确初始化长度为1的区间
	//    - 性能优化：避免重复计算
	public static int longestPalindromeSubseq(String str) {
		char[] s = str.toCharArray();
		int n = s.length;
		
		// dp[i][j]表示字符串s在区间[i,j]内最长回文子序列的长度
		int[][] dp = new int[n][n];
		
		// 初始化：单个字符的回文长度为1
		for (int i = 0; i < n; i++) {
			dp[i][i] = 1;
		}
		
		// 枚举区间长度，从2开始
		for (int len = 2; len <= n; len++) {
			// 枚举区间起点i
			for (int i = 0; i <= n - len; i++) {
				// 计算区间终点j
				int j = i + len - 1;
				
				if (s[i] == s[j]) {
					// 两端字符相同，长度为内层回文长度+2
					if (len == 2) {
						// 特殊情况：长度为2时，没有内层
						dp[i][j] = 2;
					} else {
						// 一般情况：内层回文长度+2
						dp[i][j] = dp[i + 1][j - 1] + 2;
					}
				} else {
					// 两端字符不同，取较大值
					dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
				}
			}
		}
		
		return dp[0][n - 1];
	}
	// 算法分析：
	// 时间复杂度：O(n^2)
	//   - 第一层循环枚举区间长度：O(n)
	//   - 第二层循环枚举区间起点：O(n)
	// 空间复杂度：O(n^2)
	//   - 二维dp数组占用空间：O(n^2)
	// 优化说明：
	//   - 该解法是最优解，因为需要计算所有可能的区间组合
	//   - 可以使用滚动数组优化空间复杂度到O(n)，但会增加实现复杂度

}
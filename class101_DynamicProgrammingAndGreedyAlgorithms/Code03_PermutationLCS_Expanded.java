package class087;

// 两个排列的最长公共子序列长度问题扩展实现
// 给出由1~n这些数字组成的两个排列
// 求它们的最长公共子序列长度
// n <= 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P1439

import java.util.*;

public class Code03_PermutationLCS_Expanded {
	
	/*
	 * 类似题目1：最长公共子序列（LeetCode 1143）
	 * 题目描述：
	 * 给定两个字符串 text1 和 text2，返回这两个字符串的最长公共子序列的长度。
	 * 若这两个字符串没有公共子序列，则返回 0。
	 * 
	 * 示例：
	 * 输入：text1 = "abcde", text2 = "ace"
	 * 输出：3
	 * 解释：最长公共子序列是 "ace"，它的长度为 3。
	 * 
	 * 解题思路：
	 * 这是经典的LCS问题，使用动态规划解决。
	 * dp[i][j] 表示text1的前i个字符和text2的前j个字符的最长公共子序列长度
	 * 状态转移方程：
	 * 如果text1[i-1] == text2[j-1]，则dp[i][j] = dp[i-1][j-1] + 1
	 * 否则dp[i][j] = max(dp[i-1][j], dp[i][j-1])
	 */
	
	// 最长公共子序列 - 二维动态规划解法
	// 时间复杂度: O(m * n)，其中m和n分别是两个字符串的长度
	// 空间复杂度: O(m * n)
	public static int longestCommonSubsequence1(String text1, String text2) {
		int m = text1.length();
		int n = text2.length();
		
		// dp[i][j] 表示text1的前i个字符和text2的前j个字符的最长公共子序列长度
		int[][] dp = new int[m + 1][n + 1];
		
		// 状态转移
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				if (text1.charAt(i-1) == text2.charAt(j-1)) {
					dp[i][j] = dp[i-1][j-1] + 1;
				} else {
					dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]);
				}
			}
		}
		
		return dp[m][n];
	}
	
	// 最长公共子序列 - 一维动态规划解法（空间优化）
	// 时间复杂度: O(m * n)，其中m和n分别是两个字符串的长度
	// 空间复杂度: O(n)
	public static int longestCommonSubsequence2(String text1, String text2) {
		int m = text1.length();
		int n = text2.length();
		
		// 使用一维数组优化空间
		int[] dp = new int[n + 1];
		
		// 状态转移
		for (int i = 1; i <= m; i++) {
			int pre = 0; // 保存dp[i-1][j-1]的值
			for (int j = 1; j <= n; j++) {
				int temp = dp[j]; // 保存当前dp[j]的值，用于下一次循环作为dp[i-1][j-1]
				if (text1.charAt(i-1) == text2.charAt(j-1)) {
					dp[j] = pre + 1;
				} else {
					dp[j] = Math.max(dp[j], dp[j-1]);
				}
				pre = temp;
			}
		}
		
		return dp[n];
	}
	
	/*
	 * 类似题目2：最长重复子数组（LeetCode 718）
	 * 题目描述：
	 * 给两个整数数组 A 和 B ，返回两个数组中公共的、长度最长的子数组的长度。
	 * 
	 * 示例：
	 * 输入：A = [1,2,3,2,1], B = [3,2,1,4,7]
	 * 输出：3
	 * 解释：长度最长的公共子数组是 [3,2,1]。
	 * 
	 * 解题思路：
	 * 这个问题与LCS类似，但要求是连续的子数组。
	 * dp[i][j] 表示以A[i-1]和B[j-1]结尾的公共子数组的长度
	 * 状态转移方程：
	 * 如果A[i-1] == B[j-1]，则dp[i][j] = dp[i-1][j-1] + 1
	 * 否则dp[i][j] = 0
	 */
	
	// 最长重复子数组 - 动态规划解法
	// 时间复杂度: O(m * n)，其中m和n分别是两个数组的长度
	// 空间复杂度: O(m * n)
	public static int findLength(int[] A, int[] B) {
		int m = A.length;
		int n = B.length;
		
		// dp[i][j] 表示以A[i-1]和B[j-1]结尾的公共子数组的长度
		int[][] dp = new int[m + 1][n + 1];
		int maxLen = 0;
		
		// 状态转移
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				if (A[i-1] == B[j-1]) {
					dp[i][j] = dp[i-1][j-1] + 1;
					maxLen = Math.max(maxLen, dp[i][j]);
				} else {
					dp[i][j] = 0;
				}
			}
		}
		
		return maxLen;
	}
	
	// 最长重复子数组 - 一维动态规划解法（空间优化）
	// 时间复杂度: O(m * n)，其中m和n分别是两个数组的长度
	// 空间复杂度: O(n)
	public static int findLength2(int[] A, int[] B) {
		int m = A.length;
		int n = B.length;
		
		// 使用一维数组优化空间
		int[] dp = new int[n + 1];
		int maxLen = 0;
		
		// 状态转移
		for (int i = 1; i <= m; i++) {
			int pre = 0; // 保存dp[i-1][j-1]的值
			for (int j = 1; j <= n; j++) {
				int temp = dp[j]; // 保存当前dp[j]的值，用于下一次循环作为dp[i-1][j-1]
				if (A[i-1] == B[j-1]) {
					dp[j] = pre + 1;
					maxLen = Math.max(maxLen, dp[j]);
				} else {
					dp[j] = 0;
				}
				pre = temp;
			}
		}
		
		return maxLen;
	}
	
	/*
	 * 类似题目3：不同的子序列（LeetCode 115）
	 * 题目描述：
	 * 给定一个字符串 s 和一个字符串 t ，计算在 s 的子序列中 t 出现的个数。
	 * 
	 * 示例：
	 * 输入：s = "rabbbit", t = "rabbit"
	 * 输出：3
	 * 解释：有3种可以从 s 中得到 "rabbit" 的方案。
	 * 
	 * 解题思路：
	 * 这是一个动态规划问题，类似于LCS但求的是方案数。
	 * dp[i][j] 表示s的前i个字符的子序列中t的前j个字符出现的次数
	 * 状态转移方程：
	 * 如果s[i-1] == t[j-1]，则dp[i][j] = dp[i-1][j-1] + dp[i-1][j]
	 * 否则dp[i][j] = dp[i-1][j]
	 */
	
	// 不同的子序列 - 动态规划解法
	// 时间复杂度: O(m * n)，其中m和n分别是两个字符串的长度
	// 空间复杂度: O(m * n)
	public static int numDistinct(String s, String t) {
		int m = s.length();
		int n = t.length();
		
		// dp[i][j] 表示s的前i个字符的子序列中t的前j个字符出现的次数
		long[][] dp = new long[m + 1][n + 1];
		
		// 初始化：空字符串是任何字符串的一个子序列
		for (int i = 0; i <= m; i++) {
			dp[i][0] = 1;
		}
		
		// 状态转移
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				if (s.charAt(i-1) == t.charAt(j-1)) {
					dp[i][j] = dp[i-1][j-1] + dp[i-1][j];
				} else {
					dp[i][j] = dp[i-1][j];
				}
			}
		}
		
		return (int)dp[m][n];
	}
	
	/*
	 * 类似题目4：编辑距离（LeetCode 72）
	 * 题目描述：
	 * 给你两个单词 word1 和 word2， 请返回将 word1 转换成 word2 所使用的最少操作数 。
	 * 你可以对一个单词进行如下三种操作：
	 * 插入一个字符
	 * 删除一个字符
	 * 替换一个字符
	 * 
	 * 示例：
	 * 输入：word1 = "horse", word2 = "ros"
	 * 输出：3
	 * 解释：
	 * horse -> rorse (将 'h' 替换为 'r')
	 * rorse -> rose (删除 'r')
	 * rose -> ros (删除 'e')
	 * 
	 * 解题思路：
	 * 这是一个经典的动态规划问题。
	 * dp[i][j] 表示word1的前i个字符转换成word2的前j个字符所需的最少操作数
	 * 状态转移方程：
	 * 如果word1[i-1] == word2[j-1]，则dp[i][j] = dp[i-1][j-1]
	 * 否则dp[i][j] = min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]) + 1
	 */
	
	// 编辑距离 - 动态规划解法
	// 时间复杂度: O(m * n)，其中m和n分别是两个字符串的长度
	// 空间复杂度: O(m * n)
	public static int minDistance(String word1, String word2) {
		int m = word1.length();
		int n = word2.length();
		
		// dp[i][j] 表示word1的前i个字符转换成word2的前j个字符所需的最少操作数
		int[][] dp = new int[m + 1][n + 1];
		
		// 初始化
		for (int i = 0; i <= m; i++) {
			dp[i][0] = i;
		}
		for (int j = 0; j <= n; j++) {
			dp[0][j] = j;
		}
		
		// 状态转移
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				if (word1.charAt(i-1) == word2.charAt(j-1)) {
					dp[i][j] = dp[i-1][j-1];
				} else {
					dp[i][j] = Math.min(Math.min(dp[i-1][j], dp[i][j-1]), dp[i-1][j-1]) + 1;
				}
			}
		}
		
		return dp[m][n];
	}
	
	/*
	 * 类似题目5：最长递增子序列（LeetCode 300）
	 * 题目描述：
	 * 给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
	 * 子序列是由数组派生而来的序列，删除（或不删除）数组中的元素而不改变其余元素的顺序。
	 * 
	 * 示例：
	 * 输入：nums = [10,9,2,5,3,7,101,18]
	 * 输出：4
	 * 解释：最长递增子序列是 [2,3,7,101]，长度为4。
	 * 
	 * 解题思路：
	 * 经典的LIS问题，可以使用贪心+二分查找优化到O(n log n)时间复杂度。
	 * 维护一个数组tails，其中tails[i]表示长度为i+1的递增子序列的末尾元素的最小值。
	 */
	
	// 最长递增子序列 - 贪心 + 二分查找解法
	// 时间复杂度: O(n log n)，其中n是数组长度
	// 空间复杂度: O(n)
	public static int lengthOfLIS(int[] nums) {
		int n = nums.length;
		if (n == 0) return 0;
		
		// tails[i]表示长度为i+1的递增子序列的末尾元素的最小值
		int[] tails = new int[n];
		int len = 0; // 当前最长递增子序列的长度
		
		for (int num : nums) {
			// 二分查找在tails数组中找到第一个大于等于num的位置
			int left = 0, right = len;
			while (left < right) {
				int mid = left + (right - left) / 2;
				if (tails[mid] < num) {
					left = mid + 1;
				} else {
					right = mid;
				}
			}
			
			// 更新tails数组
			tails[left] = num;
			// 如果插入的位置是当前长度，说明找到了更长的子序列
			if (left == len) {
				len++;
			}
		}
		
		return len;
	}
	
	/*
	 * 类似题目6：通配符匹配（LeetCode 44）
	 * 题目描述：
	 * 给定一个字符串 (s) 和一个字符模式 (p) ，实现一个支持 '?' 和 '*' 的通配符匹配。
	 * '?' 可以匹配任何单个字符。
	 * '*' 可以匹配任意字符串（包括空字符串）。
	 * 两个字符串完全匹配才算匹配成功。
	 * 
	 * 示例：
	 * 输入：s = "adceb", p = "*a*b"
	 * 输出：true
	 * 解释：第一个 '*' 可以匹配空字符串，第二个 '*' 可以匹配 "dce"。
	 * 
	 * 解题思路：
	 * 使用动态规划解决。
	 * dp[i][j] 表示s的前i个字符和p的前j个字符是否匹配。
	 */
	
	// 通配符匹配 - 动态规划解法
	// 时间复杂度: O(m * n)，其中m和n分别是字符串s和p的长度
	// 空间复杂度: O(m * n)
	public static boolean isMatch(String s, String p) {
		int m = s.length();
		int n = p.length();
		
		// dp[i][j] 表示s的前i个字符和p的前j个字符是否匹配
		boolean[][] dp = new boolean[m + 1][n + 1];
		
		// 空字符串和空模式匹配
		dp[0][0] = true;
		
		// 处理p以若干个*开头的情况
		for (int j = 1; j <= n; j++) {
			if (p.charAt(j-1) == '*') {
				dp[0][j] = dp[0][j-1];
			}
		}
		
		// 状态转移
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				char pChar = p.charAt(j-1);
				if (pChar == '*') {
					// '*'可以匹配0个或多个字符
					dp[i][j] = dp[i][j-1] || dp[i-1][j];
				} else if (pChar == '?' || pChar == s.charAt(i-1)) {
					// '?'匹配任意单个字符，或者字符相等
					dp[i][j] = dp[i-1][j-1];
				}
				// 其他情况默认为false
			}
		}
		
		return dp[m][n];
	}
	
	/*
	 * 类似题目7：交错字符串（LeetCode 97）
	 * 题目描述：
	 * 给定三个字符串 s1、s2、s3，请你帮忙验证 s3 是否是由 s1 和 s2 交错组成的。
	 * 两个字符串 s 和 t 交错的定义与过程如下，其中每个字符串都会被分割成若干非空子字符串：
	 * s = s1 + s2 + ... + sn
	 * t = t1 + t2 + ... + tm
	 * |n - m| <= 1
	 * 交错 是 s1 + t1 + s2 + t2 + s3 + t3 + ... 或者 t1 + s1 + t2 + s2 + t3 + s3 + ...
	 * 
	 * 示例：
	 * 输入：s1 = "aabcc", s2 = "dbbca", s3 = "aadbbcbcac"
	 * 输出：true
	 * 
	 * 解题思路：
	 * 使用动态规划解决。
	 * dp[i][j] 表示s1的前i个字符和s2的前j个字符是否能组成s3的前i+j个字符。
	 */
	
	// 交错字符串 - 动态规划解法
	// 时间复杂度: O(m * n)，其中m和n分别是字符串s1和s2的长度
	// 空间复杂度: O(m * n)
	public static boolean isInterleave(String s1, String s2, String s3) {
		int m = s1.length();
		int n = s2.length();
		int len = s3.length();
		
		// 长度不匹配，直接返回false
		if (m + n != len) {
			return false;
		}
		
		// dp[i][j] 表示s1的前i个字符和s2的前j个字符是否能组成s3的前i+j个字符
		boolean[][] dp = new boolean[m + 1][n + 1];
		
		// 空字符串和空字符串可以组成空字符串
		dp[0][0] = true;
		
		// 初始化第一行：只使用s2的情况
		for (int j = 1; j <= n; j++) {
			dp[0][j] = dp[0][j-1] && (s2.charAt(j-1) == s3.charAt(j-1));
		}
		
		// 初始化第一列：只使用s1的情况
		for (int i = 1; i <= m; i++) {
			dp[i][0] = dp[i-1][0] && (s1.charAt(i-1) == s3.charAt(i-1));
		}
		
		// 状态转移
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				// 可以从s1转移过来或从s2转移过来
				dp[i][j] = (dp[i-1][j] && s1.charAt(i-1) == s3.charAt(i+j-1)) ||
				           (dp[i][j-1] && s2.charAt(j-1) == s3.charAt(i+j-1));
			}
		}
		
		return dp[m][n];
	}
	
	// 测试方法
	public static void main(String[] args) {
		// 测试最长公共子序列
		String text1 = "abcde";
		String text2 = "ace";
		System.out.println("最长公共子序列解法一结果: " + longestCommonSubsequence1(text1, text2));
		System.out.println("最长公共子序列解法二结果: " + longestCommonSubsequence2(text1, text2));
		
		// 测试最长重复子数组
		int[] A = {1,2,3,2,1};
		int[] B = {3,2,1,4,7};
		System.out.println("最长重复子数组解法一结果: " + findLength(A, B));
		System.out.println("最长重复子数组解法二结果: " + findLength2(A, B));
		
		// 测试不同的子序列
		String s = "rabbbit";
		String t = "rabbit";
		System.out.println("不同的子序列结果: " + numDistinct(s, t));
		
		// 测试编辑距离
		String word1 = "horse";
		String word2 = "ros";
		System.out.println("编辑距离结果: " + minDistance(word1, word2));
		
		// 测试最长递增子序列
		int[] nums = {10,9,2,5,3,7,101,18};
		System.out.println("最长递增子序列结果: " + lengthOfLIS(nums));
		
		// 测试通配符匹配
		String sPattern = "adceb";
		String pPattern = "*a*b";
		System.out.println("通配符匹配结果: " + isMatch(sPattern, pPattern));
		
		// 测试交错字符串
		String s1 = "aabcc";
		String s2 = "dbbca";
		String s3 = "aadbbcbcac";
		System.out.println("交错字符串结果: " + isInterleave(s1, s2, s3));
	}
}
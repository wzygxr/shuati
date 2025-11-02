/**
 * 最长回文子序列（Longest Palindromic Subsequence）
 * 给你一个字符串 s ，找出其中最长的回文子序列，并返回该子序列的长度
 * 子序列定义为：不改变剩余字符顺序的情况下，删除某些字符或者不删除任何字符形成的一个序列
 * 
 * 题目来源：LeetCode 516. 最长回文子序列
 * 测试链接：https://leetcode.cn/problems/longest-palindromic-subsequence/
 * 
 * 算法核心思想：
 * 使用动态规划解决最长回文子序列问题，通过构建二维DP表来计算最长回文子序列长度
 * 
 * 时间复杂度分析：
 * - 基础版本：O(n²)，其中n为字符串s的长度
 * - 空间优化版本：O(n²)时间，O(n)空间
 * 
 * 空间复杂度分析：
 * - 基础版本：O(n²)
 * - 空间优化版本：O(n)
 * 
 * 最优解判定：✅ 是最优解，时间复杂度无法进一步优化，空间复杂度已优化到O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数合法性
 * 2. 边界条件：处理空字符串和极端情况
 * 3. 性能优化：使用滚动数组减少空间占用
 * 4. 代码可读性：添加详细注释和测试用例
 * 
 * 与其他领域的联系：
 * - 生物信息学：DNA序列分析和基因结构识别
 * - 数据压缩：回文结构识别和编码优化
 * - 密码学：回文检测和对称性分析
 */
public class Code11_LongestPalindromicSubsequence {

	/*
	 * 算法思路：
	 * 使用动态规划解决最长回文子序列问题
	 * dp[i][j] 表示字符串s在区间[i,j]内的最长回文子序列长度
	 * 
	 * 状态转移方程：
	 * 如果 s[i] == s[j]，则可以将这两个字符加入回文子序列中
	 *   dp[i][j] = dp[i+1][j-1] + 2
	 * 如果 s[i] != s[j]，则取左右两边的最大值
	 *   dp[i][j] = max(dp[i+1][j], dp[i][j-1])
	 * 
	 * 边界条件：
	 * dp[i][i] = 1，表示单个字符是长度为1的回文子序列
	 * dp[i][j] = 0，当i > j时
	 * 
	 * 时间复杂度：O(n²)，其中n为字符串s的长度
	 * 空间复杂度：O(n²)
	 */
	public static int longestPalindromeSubseq1(String s) {
		// 输入验证
		if (s == null) {
			throw new IllegalArgumentException("输入字符串不能为null");
		}
		
		if (s.length() == 0) {
			return 0;
		}
		
		char[] str = s.toCharArray();
		int n = str.length;
		
		// dp[i][j] 表示str[i...j]范围上的最长回文子序列长度
		int[][] dp = new int[n][n];
		
		// 初始化对角线上的元素，表示单个字符是回文
		for (int i = 0; i < n; i++) {
			dp[i][i] = 1;
		}
		
		// 初始化次对角线，两个字符的情况
		for (int i = 0; i < n - 1; i++) {
			dp[i][i + 1] = (str[i] == str[i + 1]) ? 2 : 1;
		}
		
		// 按长度由小到大填充dp表
		// 从长度为3的子串开始计算
		for (int len = 3; len <= n; len++) {
			// i是子串的起始位置，j是子串的结束位置
			for (int i = 0, j; (j = i + len - 1) < n; i++) {
				if (str[i] == str[j]) {
					// 首尾字符相同，可以同时加入回文子序列
					// 最长回文子序列长度等于内部子串的最长回文子序列长度加2
					dp[i][j] = dp[i + 1][j - 1] + 2;
				} else {
					// 首尾字符不同，不能同时加入回文子序列
					// 取去掉首字符或尾字符后的子串中最长回文子序列长度的最大值
					dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
				}
			}
		}
		
		return dp[0][n - 1];
	}

	/*
	 * 空间优化版本
	 * 观察状态转移方程，dp[i][j]依赖于dp[i+1][j-1]、dp[i+1][j]和dp[i][j-1]
	 * 对于从左到右、从下到上的填充方式，可以优化到一维数组
	 * 
	 * 时间复杂度：O(n²)
	 * 空间复杂度：O(n)
	 */
	public static int longestPalindromeSubseq2(String s) {
		// 输入验证
		if (s == null) {
			throw new IllegalArgumentException("输入字符串不能为null");
		}
		
		if (s.length() == 0) {
			return 0;
		}
		
		char[] str = s.toCharArray();
		int n = str.length;
		
		// 使用一维数组存储当前行的数据
		int[] dp = new int[n];
		
		// 存储左上角的值(dp[i+1][j-1])
		int temp = 0;
		
		// 存储上一次的temp值
		int pre = 0;
		
		// 从下到上，从左到右填充
		// 这种填充顺序可以确保在计算dp[i][j]时，所需的dp[i+1][j-1]、dp[i+1][j]和dp[i][j-1]都已经计算过
		for (int i = n - 1; i >= 0; i--) {
			// 单个字符的情况
			dp[i] = 1;
			pre = 0; // 重置pre
			
			// j从i+1开始向右扩展
			for (int j = i + 1; j < n; j++) {
				temp = dp[j]; // 保存当前dp[j]，即dp[i+1][j]
				
				if (str[i] == str[j]) {
					// 当前dp[j] = dp[i+1][j-1] + 2
					// pre保存的是dp[i+1][j-1]的值
					dp[j] = pre + 2;
				} else {
					// 当前dp[j] = max(dp[i+1][j], dp[i][j-1])
					// dp[j]保存的是dp[i+1][j]的值
					// dp[j-1]保存的是dp[i][j-1]的值
					dp[j] = Math.max(dp[j], dp[j - 1]);
				}
				
				pre = temp; // 更新pre为下一轮的左上角值
			}
		}
		
		return dp[n - 1];
	}
	
	/*
	 * 单元测试示例
	 * 测试边界情况和常见情况
	 */
	public static void main(String[] args) {
		// 测试用例1: "bbbab"
		// 预期输出: 4 ("bbbb")
		System.out.println("Test 1: " + longestPalindromeSubseq1("bbbab")); // 应输出4
		System.out.println("Test 1 (Space Optimized): " + longestPalindromeSubseq2("bbbab")); // 应输出4
		
		// 测试用例2: "cbbd"
		// 预期输出: 2 ("bb")
		System.out.println("Test 2: " + longestPalindromeSubseq1("cbbd")); // 应输出2
		System.out.println("Test 2 (Space Optimized): " + longestPalindromeSubseq2("cbbd")); // 应输出2
		
		// 边界测试: 空字符串
		System.out.println("Test 3 (Empty String): " + longestPalindromeSubseq1("")); // 应输出0
		System.out.println("Test 3 (Empty String, Space Optimized): " + longestPalindromeSubseq2("")); // 应输出0
		
		// 边界测试: 单字符
		System.out.println("Test 4 (Single Char): " + longestPalindromeSubseq1("a")); // 应输出1
		System.out.println("Test 4 (Single Char, Space Optimized): " + longestPalindromeSubseq2("a")); // 应输出1
		
		// 边界测试: 全部相同字符
		System.out.println("Test 5 (All Same): " + longestPalindromeSubseq1("aaaaa")); // 应输出5
		System.out.println("Test 5 (All Same, Space Optimized): " + longestPalindromeSubseq2("aaaaa")); // 应输出5
		
		// 边界测试: 全部不同字符
		System.out.println("Test 6 (All Different): " + longestPalindromeSubseq1("abcde")); // 应输出1
		System.out.println("Test 6 (All Different, Space Optimized): " + longestPalindromeSubseq2("abcde")); // 应输出1
	}
}
/**
 * 最长回文子串（Longest Palindromic Substring）
 * 给你一个字符串 s，找到 s 中最长的回文子串
 * 
 * 题目来源：LeetCode 5. 最长回文子串
 * 测试链接：https://leetcode.cn/problems/longest-palindromic-substring/
 * 
 * 算法核心思想：
 * 提供两种解法：动态规划和中心扩展法
 * 1. 动态规划：通过构建二维DP表判断每个子串是否为回文
 * 2. 中心扩展法：枚举每个可能的回文中心，向两边扩展寻找最长回文子串
 * 
 * 时间复杂度分析：
 * - 动态规划版本：O(n²)，其中n为字符串s的长度
 * - 中心扩展版本：O(n²)
 * 
 * 空间复杂度分析：
 * - 动态规划版本：O(n²)
 * - 中心扩展版本：O(1)
 * 
 * 最优解判定：
 * - 动态规划：适合需要记录所有回文子串信息的场景
 * - 中心扩展法：✅ 是最优解，空间复杂度更优
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数合法性
 * 2. 边界条件：处理空字符串、单字符和极端情况
 * 3. 性能优化：提供空间优化的中心扩展法
 * 4. 代码可读性：添加详细注释和测试用例
 * 
 * 与其他领域的联系：
 * - 生物信息学：DNA序列中的回文结构识别
 * - 数据结构：字符串处理和模式匹配
 * - 算法设计：动态规划和枚举算法对比
 */
public class Code13_LongestPalindromicSubstring {

	/*
	 * 算法思路：
	 * 使用动态规划解决最长回文子串问题
	 * dp[i][j] 表示字符串s在区间[i,j]内是否是回文子串
	 * 
	 * 状态转移方程：
	 * 如果 s[i] == s[j]，则取决于中间子串是否为回文
	 *   dp[i][j] = dp[i+1][j-1]
	 * 特殊情况：当子串长度小于等于3时，只需检查首尾字符是否相等
	 *   dp[i][j] = (s[i] == s[j])
	 * 
	 * 边界条件：
	 * dp[i][i] = true，表示单个字符是回文子串
	 * dp[i][i+1] = (s[i] == s[i+1])，表示两个字符的回文判断
	 * 
	 * 时间复杂度：O(n²)，其中n为字符串s的长度
	 * 空间复杂度：O(n²)
	 */
	public static String longestPalindrome1(String s) {
		// 输入验证
		if (s == null) {
			throw new IllegalArgumentException("输入字符串不能为null");
		}
		
		// 边界情况处理
		if (s.length() < 2) {
			return s;
		}
		
		char[] str = s.toCharArray();
		int n = str.length;
		
		// dp[i][j] 表示str[i...j]是否是回文子串
		boolean[][] dp = new boolean[n][n];
		
		// 记录最长回文子串的起始位置和长度
		int maxLen = 1;
		int start = 0;
		
		// 初始化：单个字符和两个字符的情况
		for (int i = 0; i < n; i++) {
			dp[i][i] = true; // 单个字符是回文
			
			// 初始化两个字符的情况
			if (i < n - 1 && str[i] == str[i + 1]) {
				dp[i][i + 1] = true;
				maxLen = 2;
				start = i;
			}
		}
		
		// 按子串长度由小到大填充dp表
		// 从长度为3的子串开始计算
		for (int len = 3; len <= n; len++) {
			// i是子串的起始位置
			for (int i = 0; i <= n - len; i++) {
				// j是子串的结束位置
				int j = i + len - 1;
				
				// 首尾字符相等，且中间子串是回文，则整个子串是回文
				if (str[i] == str[j] && dp[i + 1][j - 1]) {
					dp[i][j] = true;
					
					// 更新最长回文子串信息
					if (len > maxLen) {
						maxLen = len;
						start = i;
					}
				}
			}
		}
		
		return s.substring(start, start + maxLen);
	}

	/*
	 * 中心扩展法
	 * 回文串都是从中心向两边对称的，可以枚举每一个可能的中心点，然后向两边扩展
	 * 注意：中心点可能是一个字符（奇数长度）或两个字符之间的位置（偶数长度）
	 * 
	 * 时间复杂度：O(n²)
	 * 空间复杂度：O(1)
	 */
	public static String longestPalindrome2(String s) {
		// 输入验证
		if (s == null) {
			throw new IllegalArgumentException("输入字符串不能为null");
		}
		
		// 边界情况处理
		if (s.length() < 2) {
			return s;
		}
		
		char[] str = s.toCharArray();
		int n = str.length;
		
		// 记录最长回文子串的起始位置和长度
		int maxLen = 1;
		int start = 0;
		
		// 枚举每一个可能的中心点
		for (int i = 0; i < n; i++) {
			// 以单个字符为中心（奇数长度回文）
			int[] res1 = expandAroundCenter(str, i, i);
			
			// 以两个字符之间为中心（偶数长度回文）
			int[] res2 = expandAroundCenter(str, i, i + 1);
			
			// 更新最长回文子串
			if (res1[1] > maxLen) {
				maxLen = res1[1];
				start = res1[0];
			}
			if (res2[1] > maxLen) {
				maxLen = res2[1];
				start = res2[0];
			}
		}
		
		return s.substring(start, start + maxLen);
	}
	
	/*
	 * 从中心向两边扩展寻找回文子串
	 * 
	 * @param str 字符数组
	 * @param left 左边界
	 * @param right 右边界
	 * @return [起始索引, 长度]
	 */
	private static int[] expandAroundCenter(char[] str, int left, int right) {
		int n = str.length;
		
		// 向两边扩展，直到字符不相等或越界
		while (left >= 0 && right < n && str[left] == str[right]) {
			left--;
			right++;
		}
		
		// 返回起始索引和长度
		// left+1是实际的起始位置，right-left-1是实际的长度
		return new int[]{left + 1, right - left - 1};
	}
	
	/*
	 * 单元测试示例
	 * 测试边界情况和常见情况
	 */
	public static void main(String[] args) {
		// 测试用例1: "babad"
		// 预期输出: "bab" 或 "aba"
		System.out.println("Test 1: " + longestPalindrome1("babad"));
		System.out.println("Test 1 (Expand Around Center): " + longestPalindrome2("babad"));
		
		// 测试用例2: "cbbd"
		// 预期输出: "bb"
		System.out.println("Test 2: " + longestPalindrome1("cbbd"));
		System.out.println("Test 2 (Expand Around Center): " + longestPalindrome2("cbbd"));
		
		// 边界测试: 单字符
		System.out.println("Test 3 (Single Char): " + longestPalindrome1("a")); // 应输出"a"
		System.out.println("Test 3 (Single Char, Expand Around Center): " + longestPalindrome2("a")); // 应输出"a"
		
		// 边界测试: 全部相同字符
		System.out.println("Test 4 (All Same): " + longestPalindrome1("aaaaa")); // 应输出"aaaaa"
		System.out.println("Test 4 (All Same, Expand Around Center): " + longestPalindrome2("aaaaa")); // 应输出"aaaaa"
		
		// 测试用例5: "ac"
		// 预期输出: "a" 或 "c"
		System.out.println("Test 5: " + longestPalindrome1("ac"));
		System.out.println("Test 5 (Expand Around Center): " + longestPalindrome2("ac"));
	}
}
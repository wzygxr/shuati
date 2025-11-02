package class067;

/**
 * 最长回文子序列（Longest Palindromic Subsequence）
 * 
 * 题目描述：
 * 给你一个字符串 s ，找出其中最长的回文子序列，并返回该序列的长度。
 * 
 * 题目来源：LeetCode 516. 最长回文子序列
 * 题目链接：https://leetcode.cn/problems/longest-palindromic-subsequence/
 * 
 * 解题思路分析：
 * 1. 暴力递归：从字符串两端向中间递归，但存在大量重复计算
 * 2. 记忆化搜索：在暴力递归基础上增加缓存，避免重复计算
 * 3. 严格位置依赖的动态规划：自底向上填表，避免递归开销
 * 4. 空间优化版本：利用滚动数组思想，只保存必要的状态
 * 
 * 时间复杂度分析：
 * - 暴力递归：O(2^n) - 存在大量重复计算
 * - 记忆化搜索：O(n²) - 每个状态只计算一次
 * - 动态规划：O(n²) - 需要遍历所有可能的子串区间
 * - 空间优化DP：O(n²) - 需要遍历所有可能的子串区间
 * 
 * 空间复杂度分析：
 * - 暴力递归：O(n) - 递归栈深度
 * - 记忆化搜索：O(n²) - DP数组 + 递归栈
 * - 动态规划：O(n²) - DP数组
 * - 空间优化DP：O(n) - 只使用一维数组
 * 
 * 是否最优解：是 - 区间动态规划是解决此类回文问题的标准方法
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数合法性，处理空字符串等特殊情况
 * 2. 边界处理：处理单字符、空字符串等边界情况
 * 3. 性能优化：空间压缩降低内存使用，减少不必要的计算
 * 4. 可测试性：提供完整的测试用例，覆盖各种边界场景
 * 5. 可维护性：代码结构清晰，注释详细，便于理解和维护
 * 
 * 跨语言差异：
 * - Java：自动内存管理，强类型检查
 * - C++：需要手动管理内存，性能更高
 * - Python：语法简洁，动态类型，性能相对较慢
 * 
 * 极端场景处理：
 * - 空输入：返回0
 * - 单字符字符串：直接返回1
 * - 长字符串：使用空间优化版本避免内存溢出
 * 
 * 调试技巧：
 * - 打印中间DP表状态，验证状态转移正确性
 * - 使用小规模测试用例验证算法正确性
 * - 对比不同方法的计算结果，确保一致性
 * 
 * 与机器学习联系：
 * - 序列对称性分析在模式识别中有应用
 * - 动态规划思想在序列标注任务中体现
 * - 字符串相似度计算与自然语言处理相关
 */

public class Code04_LongestPalindromicSubsequence {

	/**
	 * 方法1：暴力递归
	 * 时间复杂度：O(2^n) - 存在大量重复计算
	 * 空间复杂度：O(n) - 递归栈深度
	 * 该方法在大数据量时会超时，仅用于理解问题本质
	 */
	public static int longestPalindromeSubseq1(String str) {
		// 输入验证
		if (str == null || str.length() == 0) {
			return 0;
		}
		
		char[] s = str.toCharArray();
		int n = s.length;
		return f1(s, 0, n - 1);
	}

	/**
	 * s[l...r]最长回文子序列长度
	 * l <= r
	 * @param s 字符串字符数组
	 * @param l 左边界
	 * @param r 右边界
	 * @return 最长回文子序列长度
	 */
	public static int f1(char[] s, int l, int r) {
		// 基础情况：只有一个字符
		if (l == r) {
			return 1;
		}
		
		// 基础情况：只有两个字符
		if (l + 1 == r) {
			return s[l] == s[r] ? 2 : 1;
		}
		
		// 如果两端字符相等
		if (s[l] == s[r]) {
			// 最长回文子序列长度 = 中间部分的最长回文子序列长度 + 2
			return 2 + f1(s, l + 1, r - 1);
		} else {
			// 如果两端字符不相等
			// 最长回文子序列长度 = max(去掉左端字符, 去掉右端字符)
			return Math.max(f1(s, l + 1, r), f1(s, l, r - 1));
		}
	}

	/**
	 * 方法2：记忆化搜索
	 * 时间复杂度：O(n²) - 每个状态只计算一次
	 * 空间复杂度：O(n²) - DP数组 + 递归栈
	 * 通过缓存已计算的结果避免重复计算
	 */
	public static int longestPalindromeSubseq2(String str) {
		// 输入验证
		if (str == null || str.length() == 0) {
			return 0;
		}
		
		char[] s = str.toCharArray();
		int n = s.length;
		
		// 创建DP数组并初始化为0，表示未计算
		int[][] dp = new int[n][n];
		
		return f2(s, 0, n - 1, dp);
	}

	/**
	 * 带记忆化的递归函数
	 * dp[l][r] 表示s[l...r]的最长回文子序列长度
	 */
	public static int f2(char[] s, int l, int r, int[][] dp) {
		// 如果已经计算过，直接返回结果
		if (dp[l][r] != 0) {
			return dp[l][r];
		}
		
		int ans;
		// 基础情况：只有一个字符
		if (l == r) {
			ans = 1;
		} else if (l + 1 == r) {
			// 基础情况：只有两个字符
			ans = s[l] == s[r] ? 2 : 1;
		} else if (s[l] == s[r]) {
			// 如果两端字符相等
			ans = 2 + f2(s, l + 1, r - 1, dp);
		} else {
			// 如果两端字符不相等
			ans = Math.max(f2(s, l + 1, r, dp), f2(s, l, r - 1, dp));
		}
		
		// 缓存结果并返回
		dp[l][r] = ans;
		return ans;
	}

	/**
	 * 方法3：严格位置依赖的动态规划
	 * 时间复杂度：O(n²) - 需要遍历所有可能的子串区间
	 * 空间复杂度：O(n²) - 使用二维DP数组
	 * 自底向上填表，避免递归开销
	 * 
	 * 填表顺序：按区间长度从小到大填表
	 */
	public static int longestPalindromeSubseq3(String str) {
		// 输入验证
		if (str == null || str.length() == 0) {
			return 0;
		}
		
		char[] s = str.toCharArray();
		int n = s.length;
		
		// 创建DP数组
		int[][] dp = new int[n][n];
		
		// 按区间长度从小到大填表
		for (int l = n - 1; l >= 0; l--) {
			// 初始化长度为1的区间
			dp[l][l] = 1;
			
			// 初始化长度为2的区间
			if (l + 1 < n) {
				dp[l][l + 1] = s[l] == s[l + 1] ? 2 : 1;
			}
			
			// 填充长度大于2的区间
			for (int r = l + 2; r < n; r++) {
				if (s[l] == s[r]) {
					// 如果两端字符相等
					dp[l][r] = 2 + dp[l + 1][r - 1];
				} else {
					// 如果两端字符不相等
					dp[l][r] = Math.max(dp[l + 1][r], dp[l][r - 1]);
				}
			}
		}
		
		// 返回整个字符串的最长回文子序列长度
		return dp[0][n - 1];
	}

	/**
	 * 方法4：严格位置依赖的动态规划 + 空间压缩
	 * 时间复杂度：O(n²) - 需要遍历所有可能的子串区间
	 * 空间复杂度：O(n) - 只使用一维数组
	 * 利用滚动数组思想，只保存必要的状态
	 */
	public static int longestPalindromeSubseq4(String str) {
		// 输入验证
		if (str == null || str.length() == 0) {
			return 0;
		}
		
		char[] s = str.toCharArray();
		int n = s.length;
		
		// 创建一维DP数组
		int[] dp = new int[n];
		
		// 按区间长度从小到大填表
		for (int l = n - 1, leftDown = 0, backup; l >= 0; l--) {
			// dp[l] : 想象中的dp[l][l]
			dp[l] = 1;
			
			// 初始化长度为2的区间
			if (l + 1 < n) {
				leftDown = dp[l + 1];
				// dp[l+1] : 想象中的dp[l][l+1]
				dp[l + 1] = s[l] == s[l + 1] ? 2 : 1;
			}
			
			// 填充长度大于2的区间
			for (int r = l + 2; r < n; r++) {
				backup = dp[r];
				if (s[l] == s[r]) {
					// 如果两端字符相等
					dp[r] = 2 + leftDown;
				} else {
					// 如果两端字符不相等
					dp[r] = Math.max(dp[r], dp[r - 1]);
				}
				leftDown = backup;
			}
		}
		
		// 返回整个字符串的最长回文子序列长度
		return dp[n - 1];
	}
	
	/**
	 * 测试方法：验证最长回文子序列算法的正确性
	 * 
	 * 测试用例设计：
	 * 1. 正常情况测试：存在回文子序列
	 * 2. 边界情况测试：单字符、空字符串等
	 * 3. 特殊情况测试：全相同字符、无回文等
	 * 4. 复杂情况测试：长字符串
	 * 
	 * 测试目的：确保各种实现方法结果一致，验证算法正确性
	 */
	public static void main(String[] args) {
		System.out.println("=== 最长回文子序列算法测试 ===");
		
		// 测试用例1：正常情况 - 存在回文子序列
		String str1 = "bbbab";
		System.out.println("测试用例1 - 正常情况:");
		System.out.println("字符串: " + str1);
		System.out.println("暴力递归: " + longestPalindromeSubseq1(str1));
		System.out.println("记忆化搜索: " + longestPalindromeSubseq2(str1));
		System.out.println("动态规划: " + longestPalindromeSubseq3(str1));
		System.out.println("空间优化DP: " + longestPalindromeSubseq4(str1));
		System.out.println("预期结果: 4");
		System.out.println();
		
		// 测试用例2：存在回文子序列
		String str2 = "cbbd";
		System.out.println("测试用例2 - 存在回文子序列:");
		System.out.println("字符串: " + str2);
		System.out.println("暴力递归: " + longestPalindromeSubseq1(str2));
		System.out.println("记忆化搜索: " + longestPalindromeSubseq2(str2));
		System.out.println("动态规划: " + longestPalindromeSubseq3(str2));
		System.out.println("空间优化DP: " + longestPalindromeSubseq4(str2));
		System.out.println("预期结果: 2");
		System.out.println();
		
		// 测试用例3：单字符
		String str3 = "a";
		System.out.println("测试用例3 - 单字符:");
		System.out.println("字符串: " + str3);
		System.out.println("记忆化搜索: " + longestPalindromeSubseq2(str3));
		System.out.println("动态规划: " + longestPalindromeSubseq3(str3));
		System.out.println("空间优化DP: " + longestPalindromeSubseq4(str3));
		System.out.println("预期结果: 1");
		System.out.println();
		
		// 测试用例4：全相同字符
		String str4 = "aaaa";
		System.out.println("测试用例4 - 全相同字符:");
		System.out.println("字符串: " + str4);
		System.out.println("记忆化搜索: " + longestPalindromeSubseq2(str4));
		System.out.println("动态规划: " + longestPalindromeSubseq3(str4));
		System.out.println("空间优化DP: " + longestPalindromeSubseq4(str4));
		System.out.println("预期结果: 4");
		System.out.println();
		
		// 测试用例5：空字符串
		String str5 = "";
		System.out.println("测试用例5 - 空字符串:");
		System.out.println("字符串: \"\"");
		System.out.println("记忆化搜索: " + longestPalindromeSubseq2(str5));
		System.out.println("动态规划: " + longestPalindromeSubseq3(str5));
		System.out.println("空间优化DP: " + longestPalindromeSubseq4(str5));
		System.out.println("预期结果: 0");
		
		System.out.println("\n=== 测试完成 ===");
	}
}
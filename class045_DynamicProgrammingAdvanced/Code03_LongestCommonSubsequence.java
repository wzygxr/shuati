package class067;

/**
 * 最长公共子序列（Longest Common Subsequence）
 * 
 * 题目描述：
 * 给定两个字符串text1和text2，返回这两个字符串的最长公共子序列的长度。
 * 如果不存在公共子序列，返回0。
 * 两个字符串的公共子序列是这两个字符串所共同拥有的子序列。
 * 
 * 题目来源：LeetCode 1143. 最长公共子序列
 * 题目链接：https://leetcode.cn/problems/longest-common-subsequence/
 * 
 * 解题思路分析：
 * 1. 暴力递归：从两个字符串的末尾开始递归，但存在大量重复计算
 * 2. 记忆化搜索：在暴力递归基础上增加缓存，避免重复计算
 * 3. 严格位置依赖的动态规划：自底向上填表，避免递归开销
 * 4. 空间优化版本：利用滚动数组思想，只保存必要的状态
 * 
 * 时间复杂度分析：
 * - 暴力递归：O(2^(m+n)) - 存在大量重复计算
 * - 记忆化搜索：O(m*n) - 每个状态只计算一次
 * - 动态规划：O(m*n) - 需要遍历整个DP表
 * - 空间优化DP：O(m*n) - 需要遍历整个DP表
 * 
 * 空间复杂度分析：
 * - 暴力递归：O(m+n) - 递归栈深度
 * - 记忆化搜索：O(m*n) - DP数组 + 递归栈
 * - 动态规划：O(m*n) - DP数组
 * - 空间优化DP：O(min(m,n)) - 只使用一维数组
 * 
 * 是否最优解：是 - 动态规划是解决此类字符串匹配问题的标准方法
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
 * - 单字符字符串：直接比较字符
 * - 长字符串：使用空间优化版本避免内存溢出
 * 
 * 调试技巧：
 * - 打印中间DP表状态，验证状态转移正确性
 * - 使用小规模测试用例验证算法正确性
 * - 对比不同方法的计算结果，确保一致性
 * 
 * 与机器学习联系：
 * - 序列比对问题在生物信息学中有广泛应用
 * - 动态规划思想在序列标注任务中体现
 * - 字符串相似度计算与自然语言处理相关
 */

public class Code03_LongestCommonSubsequence {

	/**
	 * 方法1：基于下标的暴力递归
	 * 时间复杂度：O(2^(m+n)) - 存在大量重复计算
	 * 空间复杂度：O(m+n) - 递归栈深度
	 * 该方法在大数据量时会超时，仅用于理解问题本质
	 */
	public static int longestCommonSubsequence1(String str1, String str2) {
		// 输入验证
		if (str1 == null || str2 == null) {
			return 0;
		}
		
		char[] s1 = str1.toCharArray();
		char[] s2 = str2.toCharArray();
		int n = s1.length;
		int m = s2.length;
		return f1(s1, s2, n - 1, m - 1);
	}

	/**
	 * s1[0....i1]与s2[0....i2]最长公共子序列长度
	 * @param s1 第一个字符串的字符数组
	 * @param s2 第二个字符串的字符数组
	 * @param i1 第一个字符串的当前索引
	 * @param i2 第二个字符串的当前索引
	 * @return 最长公共子序列长度
	 */
	public static int f1(char[] s1, char[] s2, int i1, int i2) {
		// 基础情况：某个字符串已经遍历完
		if (i1 < 0 || i2 < 0) {
			return 0;
		}
		
		// 四种可能性：
		// p1: 不考虑s1[i1]和s2[i2]，直接递归到(i1-1, i2-1)
		int p1 = f1(s1, s2, i1 - 1, i2 - 1);
		
		// p2: 不考虑s1[i1]，递归到(i1-1, i2)
		int p2 = f1(s1, s2, i1 - 1, i2);
		
		// p3: 不考虑s2[i2]，递归到(i1, i2-1)
		int p3 = f1(s1, s2, i1, i2 - 1);
		
		// p4: 如果s1[i1] == s2[i2]，则可以同时考虑两个字符
		int p4 = s1[i1] == s2[i2] ? (p1 + 1) : 0;
		
		// 返回四种可能性的最大值
		return Math.max(Math.max(p1, p2), Math.max(p3, p4));
	}

	/**
	 * 方法2：基于长度的递归（避免边界讨论）
	 * 时间复杂度：O(2^(m+n)) - 存在大量重复计算
	 * 空间复杂度：O(m+n) - 递归栈深度
	 * 该方法在大数据量时会超时，仅用于理解问题本质
	 * 
	 * 为了避免很多边界讨论，很多时候往往不用下标来定义尝试，
	 * 而是用长度来定义尝试。因为长度最短是0，而下标越界的话讨论起来就比较麻烦。
	 */
	public static int longestCommonSubsequence2(String str1, String str2) {
		// 输入验证
		if (str1 == null || str2 == null) {
			return 0;
		}
		
		char[] s1 = str1.toCharArray();
		char[] s2 = str2.toCharArray();
		int n = s1.length;
		int m = s2.length;
		return f2(s1, s2, n, m);
	}

	/**
	 * s1[前缀长度为len1]对应s2[前缀长度为len2]的最长公共子序列长度
	 * @param s1   第一个字符串的字符数组
	 * @param s2   第二个字符串的字符数组
	 * @param len1 第一个字符串的前缀长度
	 * @param len2 第二个字符串的前缀长度
	 * @return 最长公共子序列长度
	 */
	public static int f2(char[] s1, char[] s2, int len1, int len2) {
		// 基础情况：某个字符串的前缀长度为0
		if (len1 == 0 || len2 == 0) {
			return 0;
		}
		
		// 如果最后一个字符相等
		if (s1[len1 - 1] == s2[len2 - 1]) {
			// 最长公共子序列长度 = 去掉最后一个字符后的最长公共子序列长度 + 1
			return f2(s1, s2, len1 - 1, len2 - 1) + 1;
		} else {
			// 最长公共子序列长度 = max(去掉s1最后一个字符, 去掉s2最后一个字符)
			return Math.max(f2(s1, s2, len1 - 1, len2), f2(s1, s2, len1, len2 - 1));
		}
	}

	/**
	 * 方法3：记忆化搜索
	 * 时间复杂度：O(m*n) - 每个状态只计算一次
	 * 空间复杂度：O(m*n) - DP数组 + 递归栈
	 * 通过缓存已计算的结果避免重复计算
	 */
	public static int longestCommonSubsequence3(String str1, String str2) {
		// 输入验证
		if (str1 == null || str2 == null) {
			return 0;
		}
		
		char[] s1 = str1.toCharArray();
		char[] s2 = str2.toCharArray();
		int n = s1.length;
		int m = s2.length;
		
		// 创建DP数组并初始化为-1，表示未计算
		int[][] dp = new int[n + 1][m + 1];
		for (int i = 0; i <= n; i++) {
			for (int j = 0; j <= m; j++) {
				dp[i][j] = -1;
			}
		}
		
		return f3(s1, s2, n, m, dp);
	}

	/**
	 * 带记忆化的递归函数
	 * dp[len1][len2] 表示s1[前缀长度为len1]与s2[前缀长度为len2]的最长公共子序列长度
	 */
	public static int f3(char[] s1, char[] s2, int len1, int len2, int[][] dp) {
		// 如果已经计算过，直接返回结果
		if (dp[len1][len2] != -1) {
			return dp[len1][len2];
		}
		
		int ans;
		// 基础情况：某个字符串的前缀长度为0
		if (len1 == 0 || len2 == 0) {
			ans = 0;
		} else if (s1[len1 - 1] == s2[len2 - 1]) {
			// 如果最后一个字符相等
			ans = f3(s1, s2, len1 - 1, len2 - 1, dp) + 1;
		} else {
			// 如果最后一个字符不相等
			ans = Math.max(f3(s1, s2, len1 - 1, len2, dp), f3(s1, s2, len1, len2 - 1, dp));
		}
		
		// 缓存结果并返回
		dp[len1][len2] = ans;
		return ans;
	}

	/**
	 * 方法4：严格位置依赖的动态规划
	 * 时间复杂度：O(m*n) - 需要遍历整个DP表
	 * 空间复杂度：O(m*n) - 使用二维DP数组
	 * 自底向上填表，避免递归开销
	 */
	public static int longestCommonSubsequence4(String str1, String str2) {
		// 输入验证
		if (str1 == null || str2 == null) {
			return 0;
		}
		
		char[] s1 = str1.toCharArray();
		char[] s2 = str2.toCharArray();
		int n = s1.length;
		int m = s2.length;
		
		// 创建DP数组
		int[][] dp = new int[n + 1][m + 1];
		
		// 填充DP表
		for (int len1 = 1; len1 <= n; len1++) {
			for (int len2 = 1; len2 <= m; len2++) {
				if (s1[len1 - 1] == s2[len2 - 1]) {
					// 如果最后一个字符相等
					dp[len1][len2] = 1 + dp[len1 - 1][len2 - 1];
				} else {
					// 如果最后一个字符不相等
					dp[len1][len2] = Math.max(dp[len1 - 1][len2], dp[len1][len2 - 1]);
				}
			}
		}
		
		// 返回结果
		return dp[n][m];
	}

	/**
	 * 方法5：严格位置依赖的动态规划 + 空间压缩
	 * 时间复杂度：O(m*n) - 需要遍历整个DP表
	 * 空间复杂度：O(min(m,n)) - 只使用一维数组
	 * 利用滚动数组思想，只保存必要的状态
	 */
	public static int longestCommonSubsequence5(String str1, String str2) {
		// 输入验证
		if (str1 == null || str2 == null) {
			return 0;
		}
		
		char[] s1, s2;
		// 为了优化空间，让较长的字符串作为s1
		if (str1.length() >= str2.length()) {
			s1 = str1.toCharArray();
			s2 = str2.toCharArray();
		} else {
			s1 = str2.toCharArray();
			s2 = str1.toCharArray();
		}
		
		int n = s1.length;
		int m = s2.length;
		
		// 创建一维DP数组
		int[] dp = new int[m + 1];
		
		// 填充DP数组
		for (int len1 = 1; len1 <= n; len1++) {
			int leftUp = 0, backup;
			for (int len2 = 1; len2 <= m; len2++) {
				backup = dp[len2];
				if (s1[len1 - 1] == s2[len2 - 1]) {
					// 如果最后一个字符相等
					dp[len2] = 1 + leftUp;
				} else {
					// 如果最后一个字符不相等
					dp[len2] = Math.max(dp[len2], dp[len2 - 1]);
				}
				leftUp = backup;
			}
		}
		
		// 返回结果
		return dp[m];
	}
	
	/**
	 * 测试方法：验证最长公共子序列算法的正确性
	 * 
	 * 测试用例设计：
	 * 1. 正常情况测试：存在公共子序列
	 * 2. 边界情况测试：不存在公共子序列
	 * 3. 特殊情况测试：空字符串、单字符等
	 * 4. 复杂情况测试：长字符串
	 * 
	 * 测试目的：确保各种实现方法结果一致，验证算法正确性
	 */
	public static void main(String[] args) {
		System.out.println("=== 最长公共子序列算法测试 ===");
		
		// 测试用例1：正常情况 - 存在公共子序列
		String str1 = "abcde";
		String str2 = "ace";
		System.out.println("测试用例1 - 正常情况:");
		System.out.println("字符串1: " + str1);
		System.out.println("字符串2: " + str2);
		System.out.println("暴力递归: " + longestCommonSubsequence1(str1, str2));
		System.out.println("记忆化搜索: " + longestCommonSubsequence3(str1, str2));
		System.out.println("动态规划: " + longestCommonSubsequence4(str1, str2));
		System.out.println("空间优化DP: " + longestCommonSubsequence5(str1, str2));
		System.out.println("预期结果: 3");
		System.out.println();
		
		// 测试用例2：不存在公共子序列
		String str3 = "abc";
		String str4 = "def";
		System.out.println("测试用例2 - 不存在公共子序列:");
		System.out.println("字符串1: " + str3);
		System.out.println("字符串2: " + str4);
		System.out.println("记忆化搜索: " + longestCommonSubsequence3(str3, str4));
		System.out.println("动态规划: " + longestCommonSubsequence4(str3, str4));
		System.out.println("空间优化DP: " + longestCommonSubsequence5(str3, str4));
		System.out.println("预期结果: 0");
		System.out.println();
		
		// 测试用例3：相同字符串
		String str5 = "abc";
		String str6 = "abc";
		System.out.println("测试用例3 - 相同字符串:");
		System.out.println("字符串1: " + str5);
		System.out.println("字符串2: " + str6);
		System.out.println("记忆化搜索: " + longestCommonSubsequence3(str5, str6));
		System.out.println("动态规划: " + longestCommonSubsequence4(str5, str6));
		System.out.println("空间优化DP: " + longestCommonSubsequence5(str5, str6));
		System.out.println("预期结果: 3");
		System.out.println();
		
		// 测试用例4：空字符串
		String str7 = "";
		String str8 = "abc";
		System.out.println("测试用例4 - 空字符串:");
		System.out.println("字符串1: \"\"");
		System.out.println("字符串2: " + str8);
		System.out.println("记忆化搜索: " + longestCommonSubsequence3(str7, str8));
		System.out.println("动态规划: " + longestCommonSubsequence4(str7, str8));
		System.out.println("空间优化DP: " + longestCommonSubsequence5(str7, str8));
		System.out.println("预期结果: 0");
		System.out.println();
		
		// 测试用例5：单字符
		String str9 = "a";
		String str10 = "a";
		System.out.println("测试用例5 - 单字符:");
		System.out.println("字符串1: " + str9);
		System.out.println("字符串2: " + str10);
		System.out.println("记忆化搜索: " + longestCommonSubsequence3(str9, str10));
		System.out.println("动态规划: " + longestCommonSubsequence4(str9, str10));
		System.out.println("空间优化DP: " + longestCommonSubsequence5(str9, str10));
		System.out.println("预期结果: 1");
		
		System.out.println("\n=== 测试完成 ===");
	}
}
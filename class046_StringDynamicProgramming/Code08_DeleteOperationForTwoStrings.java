/**
 * 两个字符串的删除操作（Delete Operation for Two Strings）
 * 给定两个单词 word1 和 word2 ，返回使得 word1 和 word2 相同所需的最小步数。
 * 每步可以删除任意一个字符串中的一个字符。
 * 
 * 题目来源：LeetCode 583. 两个字符串的删除操作
 * 测试链接：https://leetcode.cn/problems/delete-operation-for-two-strings/
 * 
 * 算法核心思想：
 * 使用动态规划解决两个字符串的删除操作问题，通过构建二维DP表来计算最小删除步数
 * 
 * 时间复杂度分析：
 * - 基础版本：O(n*m)，其中n为word1的长度，m为word2的长度
 * - 空间优化版本：O(n*m)时间，O(m)空间
 * 
 * 空间复杂度分析：
 * - 基础版本：O(n*m)
 * - 空间优化版本：O(m)
 * 
 * 最优解判定：✅ 是最优解，时间复杂度无法进一步优化，空间复杂度已优化到O(min(n,m))
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数合法性
 * 2. 边界条件：处理空字符串和极端情况
 * 3. 性能优化：使用滚动数组减少空间占用
 * 4. 代码可读性：添加详细注释和测试用例
 * 
 * 与其他领域的联系：
 * - 文本处理：文档差异比较和版本控制
 * - 生物信息学：DNA序列比对和基因分析
 * - 数据同步：文件差异计算和同步算法
 */
public class Code08_DeleteOperationForTwoStrings {

	/*
	 * 两个字符串的删除操作 - 动态规划解法
	 * 使用动态规划解决两个字符串的删除操作问题
	 * dp[i][j] 表示使字符串word1的前i个字符与字符串word2的前j个字符相同所需的最小删除步数
	 * 
	 * 状态转移方程：
	 * 如果 word1[i-1] == word2[j-1]：
	 *   dp[i][j] = dp[i-1][j-1]
	 * 否则：
	 *   dp[i][j] = min(dp[i-1][j], dp[i][j-1]) + 1
	 * 
	 * 解释：
	 * 当当前字符相等时，不需要删除操作，结果等于前面子串的删除步数
	 * 当当前字符不相等时，可以选择删除word1的字符或删除word2的字符，取较小值加1
	 * 
	 * 边界条件：
	 * dp[i][0] = i，表示将word1的前i个字符删除为空字符串需要i步
	 * dp[0][j] = j，表示将空字符串变为word2的前j个字符需要j步
	 * 
	 * 时间复杂度：O(n*m)，其中n为word1的长度，m为word2的长度
	 * 空间复杂度：O(n*m)
	 */
	public static int minDistance(String word1, String word2) {
		// 输入验证
		if (word1 == null || word2 == null) {
			throw new IllegalArgumentException("输入字符串不能为null");
		}
		
		int n = word1.length();
		int m = word2.length();
		
		// 边界情况处理
		if (n == 0) return m; // word1为空，需要删除word2的所有字符
		if (m == 0) return n; // word2为空，需要删除word1的所有字符
		
		// dp[i][j] 表示使word1的前i个字符与word2的前j个字符相同所需的最小删除步数
		int[][] dp = new int[n + 1][m + 1];
		
		// 边界条件
		for (int i = 1; i <= n; i++) {
			dp[i][0] = i; // 删除word1的前i个字符
		}
		for (int j = 1; j <= m; j++) {
			dp[0][j] = j; // 删除word2的前j个字符
		}
		
		// 填充dp表
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= m; j++) {
				if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
					// 当前字符相等，不需要删除操作
					dp[i][j] = dp[i - 1][j - 1];
				} else {
					// 当前字符不相等，选择删除word1或word2的字符
					// 删除word1的字符：dp[i-1][j] + 1
					// 删除word2的字符：dp[i][j-1] + 1
					dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + 1;
				}
			}
		}
		
		return dp[n][m];
	}

	/*
	 * 空间优化版本
	 * 使用滚动数组优化空间复杂度
	 * 
	 * 时间复杂度：O(n*m)
	 * 空间复杂度：O(m)
	 */
	public static int minDistanceOptimized(String word1, String word2) {
		// 输入验证
		if (word1 == null || word2 == null) {
			throw new IllegalArgumentException("输入字符串不能为null");
		}
		
		int n = word1.length();
		int m = word2.length();
		
		// 边界情况处理
		if (n == 0) return m;
		if (m == 0) return n;
		
		// 只需要一行数组
		int[] dp = new int[m + 1];
		
		// 初始化第一行
		for (int j = 1; j <= m; j++) {
			dp[j] = j;
		}
		
		// 填充dp表
		for (int i = 1; i <= n; i++) {
			int prev = dp[0];  // 保存dp[i-1][j-1]的值
			dp[0] = i;         // 更新dp[i][0]的值
			
			for (int j = 1; j <= m; j++) {
				int temp = dp[j];  // 保存当前dp[j]的值，用于下一次循环
				
				if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
					// 当前字符相等，不需要删除操作
					dp[j] = prev;
				} else {
					// 当前字符不相等，选择删除word1或word2的字符
					// 删除word1的字符：dp[i-1][j] + 1 对应 dp[j] + 1
					// 删除word2的字符：dp[i][j-1] + 1 对应 dp[j-1] + 1
					dp[j] = Math.min(dp[j], dp[j - 1]) + 1;
				}
				
				prev = temp;  // 更新prev为原来的dp[j]值
			}
		}
		
		return dp[m];
	}

	/**
	 * 测试函数
	 */
	// 测试函数
	public static void main(String[] args) {
		// 测试用例
		String[][] testCases = {
			{"sea", "eat"},     // 2
			{"leetcode", "etco"}, // 4
			{"", "a"},          // 1
			{"a", ""},          // 1
			{"", ""}            // 0
		};
		
		System.out.println("两个字符串的删除操作测试:");
		for (String[] testCase : testCases) {
			String word1 = testCase[0];
			String word2 = testCase[1];
			int result1 = minDistance(word1, word2);
			int result2 = minDistanceOptimized(word1, word2);
			System.out.printf("word1=\"%s\", word2=\"%s\" => %d (optimized: %d)\n", word1, word2, result1, result2);
		}
	}
}
/**
 * 两个字符串的最小ASCII删除和（Minimum ASCII Delete Sum for Two Strings）
 * 给定两个字符串s1 和 s2，返回使两个字符串相等所需删除字符的 ASCII 值的最小和。
 * 
 * 题目来源：LeetCode 712. 两个字符串的最小ASCII删除和
 * 测试链接：https://leetcode.cn/problems/minimum-ascii-delete-sum-for-two-strings/
 * 
 * 算法核心思想：
 * 使用动态规划解决两个字符串的最小ASCII删除和问题，通过构建二维DP表来计算最小删除ASCII值和
 * 
 * 时间复杂度分析：
 * - 基础版本：O(n*m)，其中n为s1的长度，m为s2的长度
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
 * - 数据压缩：最小编辑距离计算
 * - 优化理论：约束优化问题
 */
public class Code09_MinimumASCIIDeleteSumForTwoStrings {

	/*
	 * 两个字符串的最小ASCII删除和 - 动态规划解法
	 * 使用动态规划解决两个字符串的最小ASCII删除和问题
	 * dp[i][j] 表示使字符串s1的前i个字符与字符串s2的前j个字符相同所需删除字符的ASCII值的最小和
	 * 
	 * 状态转移方程：
	 * 如果 s1[i-1] == s2[j-1]：
	 *   dp[i][j] = dp[i-1][j-1]
	 * 否则：
	 *   dp[i][j] = min(dp[i-1][j] + s1[i-1], dp[i][j-1] + s2[j-1])
	 * 
	 * 解释：
	 * 当当前字符相等时，不需要删除操作，结果等于前面子串的删除ASCII和
	 * 当当前字符不相等时，可以选择删除s1的字符或删除s2的字符，取ASCII值较小的方案
	 * 
	 * 边界条件：
	 * dp[i][0] = dp[i-1][0] + s1[i-1]，表示将s1的前i个字符删除所需的ASCII和
	 * dp[0][j] = dp[0][j-1] + s2[j-1]，表示将s2的前j个字符删除所需的ASCII和
	 * 
	 * 时间复杂度：O(n*m)，其中n为s1的长度，m为s2的长度
	 * 空间复杂度：O(n*m)
	 */
	public static int minimumDeleteSum(String s1, String s2) {
		// 输入验证
		if (s1 == null || s2 == null) {
			throw new IllegalArgumentException("输入字符串不能为null");
		}
		
		int n = s1.length();
		int m = s2.length();
		
		// 边界情况处理
		if (n == 0 && m == 0) return 0;
		if (n == 0) {
			// s1为空，需要删除s2的所有字符
			int sum = 0;
			for (int i = 0; i < m; i++) {
				sum += s2.charAt(i);
			}
			return sum;
		}
		if (m == 0) {
			// s2为空，需要删除s1的所有字符
			int sum = 0;
			for (int i = 0; i < n; i++) {
				sum += s1.charAt(i);
			}
			return sum;
		}
		
		// dp[i][j] 表示使s1的前i个字符与s2的前j个字符相同所需删除字符的ASCII值的最小和
		int[][] dp = new int[n + 1][m + 1];
		
		// 边界条件
		for (int i = 1; i <= n; i++) {
			dp[i][0] = dp[i - 1][0] + s1.charAt(i - 1); // 删除s1的前i个字符
		}
		for (int j = 1; j <= m; j++) {
			dp[0][j] = dp[0][j - 1] + s2.charAt(j - 1); // 删除s2的前j个字符
		}
		
		// 填充dp表
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= m; j++) {
				if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
					// 当前字符相等，不需要删除操作
					dp[i][j] = dp[i - 1][j - 1];
				} else {
					// 当前字符不相等，选择删除ASCII值较小的字符
					// 删除s1的字符：dp[i-1][j] + s1[i-1]
					// 删除s2的字符：dp[i][j-1] + s2[j-1]
					dp[i][j] = Math.min(dp[i - 1][j] + s1.charAt(i - 1), dp[i][j - 1] + s2.charAt(j - 1));
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
	public static int minimumDeleteSumOptimized(String s1, String s2) {
		// 输入验证
		if (s1 == null || s2 == null) {
			throw new IllegalArgumentException("输入字符串不能为null");
		}
		
		int n = s1.length();
		int m = s2.length();
		
		// 边界情况处理
		if (n == 0 && m == 0) return 0;
		if (n == 0) {
			// s1为空，需要删除s2的所有字符
			int sum = 0;
			for (int i = 0; i < m; i++) {
				sum += s2.charAt(i);
			}
			return sum;
		}
		if (m == 0) {
			// s2为空，需要删除s1的所有字符
			int sum = 0;
			for (int i = 0; i < n; i++) {
				sum += s1.charAt(i);
			}
			return sum;
		}
		
		// 只需要一行数组
		int[] dp = new int[m + 1];
		
		// 初始化第一行
		for (int j = 1; j <= m; j++) {
			dp[j] = dp[j - 1] + s2.charAt(j - 1);
		}
		
		// 填充dp表
		for (int i = 1; i <= n; i++) {
			int prev = dp[0];  // 保存dp[i-1][j-1]的值
			dp[0] = dp[0] + s1.charAt(i - 1);  // 更新dp[i][0]的值
			
			for (int j = 1; j <= m; j++) {
				int temp = dp[j];  // 保存当前dp[j]的值，用于下一次循环
				
				if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
					// 当前字符相等，不需要删除操作
					dp[j] = prev;
				} else {
					// 当前字符不相等，选择删除ASCII值较小的字符
					// 删除s1的字符：dp[i-1][j] + s1[i-1] 对应 temp + s1.charAt(i-1)
					// 删除s2的字符：dp[i][j-1] + s2[j-1] 对应 dp[j-1] + s2.charAt(j-1)
					dp[j] = Math.min(temp + s1.charAt(i - 1), dp[j - 1] + s2.charAt(j - 1));
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
			{"sea", "eat"},     // 231
			{"delete", "leet"}, // 403
			{"", "a"},          // 97
			{"a", ""},          // 97
			{"", ""}            // 0
		};
		
		System.out.println("两个字符串的最小ASCII删除和测试:");
		for (String[] testCase : testCases) {
			String s1 = testCase[0];
			String s2 = testCase[1];
			int result1 = minimumDeleteSum(s1, s2);
			int result2 = minimumDeleteSumOptimized(s1, s2);
			System.out.printf("s1=\"%s\", s2=\"%s\" => %d (optimized: %d)\n", s1, s2, result1, result2);
		}
	}
}
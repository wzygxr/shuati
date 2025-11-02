/**
 * 交错字符串（Interleaving String）
 * 给定三个字符串 s1、s2、s3，请验证s3是否由s1和s2交错组成
 * 
 * 题目来源：LeetCode 97. 交错字符串
 * 测试链接：https://leetcode.cn/problems/interleaving-string/
 * 
 * 算法核心思想：
 * 使用动态规划判断s3是否由s1和s2交错组成
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
 * 4. 代码可读性：添加详细注释和清晰的变量命名
 * 
 * 与其他领域的联系：
 * - 编译原理：语法分析中的字符串匹配
 * - 生物信息学：DNA序列分析
 * - 文件处理：多文件合并验证
 */
public class Code03_InterleavingString {

	/*
	 * 交错字符串判断算法
	 * 使用动态规划判断s3是否由s1和s2交错组成
	 * dp[i][j] 表示s1的前i个字符和s2的前j个字符是否能交错组成s3的前i+j个字符
	 * 
	 * 状态转移方程：
	 * dp[i][j] = (s1[i-1] == s3[i+j-1] && dp[i-1][j]) || 
	 *            (s2[j-1] == s3[i+j-1] && dp[i][j-1])
	 * 
	 * 解释：
	 * 如果s1的第i个字符等于s3的第i+j个字符，并且s1前i-1个字符和s2前j个字符能交错组成s3前i+j-1个字符
	 * 或者s2的第j个字符等于s3的第i+j个字符，并且s1前i个字符和s2前j-1个字符能交错组成s3前i+j-1个字符
	 * 
	 * 边界条件：
	 * dp[0][0] = true，表示两个空字符串可以交错组成一个空字符串
	 * dp[i][0] = s1[0..i-1] == s3[0..i-1]
	 * dp[0][j] = s2[0..j-1] == s3[0..j-1]
	 * 
	 * 时间复杂度：O(n*m)，其中n为s1的长度，m为s2的长度
	 * 空间复杂度：O(n*m)
	 */
	// 已经展示太多次从递归到动态规划了
	// 直接写动态规划吧
	public static boolean isInterleave1(String str1, String str2, String str3) {
		// 输入验证
		if (str1 == null || str2 == null || str3 == null) {
			throw new IllegalArgumentException("输入字符串不能为null");
		}
		
		// 长度检查：s1和s2的长度之和必须等于s3的长度
		if (str1.length() + str2.length() != str3.length()) {
			return false;
		}
		
		char[] s1 = str1.toCharArray();
		char[] s2 = str2.toCharArray();
		char[] s3 = str3.toCharArray();
		int n = s1.length;
		int m = s2.length;
		
		// 边界情况处理
		if (n == 0) return str2.equals(str3);
		if (m == 0) return str1.equals(str3);
		
		// dp[i][j]: s1[前缀长度为i]和s2[前缀长度为j]，能否交错组成出s3[前缀长度为i+j]
		boolean[][] dp = new boolean[n + 1][m + 1];
		
		// 初始化边界条件
		dp[0][0] = true;
		
		// 初始化第一列：s1的前i个字符是否能组成s3的前i个字符
		for (int i = 1; i <= n; i++) {
			if (s1[i - 1] != s3[i - 1]) {
				break;
			}
			dp[i][0] = true;
		}
		
		// 初始化第一行：s2的前j个字符是否能组成s3的前j个字符
		for (int j = 1; j <= m; j++) {
			if (s2[j - 1] != s3[j - 1]) {
				break;
			}
			dp[0][j] = true;
		}
		
		// 填充DP表
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= m; j++) {
				// 状态转移方程：
				// 1. 如果s1的第i个字符等于s3的第i+j个字符，并且s1前i-1个字符和s2前j个字符能交错组成s3前i+j-1个字符
				// 2. 或者s2的第j个字符等于s3的第i+j个字符，并且s1前i个字符和s2前j-1个字符能交错组成s3前i+j-1个字符
				dp[i][j] = (s1[i - 1] == s3[i + j - 1] && dp[i - 1][j]) || 
						  (s2[j - 1] == s3[i + j - 1] && dp[i][j - 1]);
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
	// 空间压缩
	public static boolean isInterleave2(String str1, String str2, String str3) {
		// 输入验证
		if (str1 == null || str2 == null || str3 == null) {
			throw new IllegalArgumentException("输入字符串不能为null");
		}
		
		// 长度检查：s1和s2的长度之和必须等于s3的长度
		if (str1.length() + str2.length() != str3.length()) {
			return false;
		}
		
		char[] s1 = str1.toCharArray();
		char[] s2 = str2.toCharArray();
		char[] s3 = str3.toCharArray();
		int n = s1.length;
		int m = s2.length;
		
		// 边界情况处理
		if (n == 0) return str2.equals(str3);
		if (m == 0) return str1.equals(str3);
		
		// 只需要一维数组
		boolean[] dp = new boolean[m + 1];
		
		// 初始化第一行
		dp[0] = true;
		for (int j = 1; j <= m; j++) {
			if (s2[j - 1] != s3[j - 1]) {
				break;
			}
			dp[j] = true;
		}
		
		// 填充DP表
		for (int i = 1; i <= n; i++) {
			// 更新第一列的值
			dp[0] = s1[i - 1] == s3[i - 1] && dp[0];
			for (int j = 1; j <= m; j++) {
				// 状态转移方程：
				// 1. 如果s1的第i个字符等于s3的第i+j个字符，并且s1前i-1个字符和s2前j个字符能交错组成s3前i+j-1个字符
				// 2. 或者s2的第j个字符等于s3的第i+j个字符，并且s1前i个字符和s2前j-1个字符能交错组成s3前i+j-1个字符
				dp[j] = (s1[i - 1] == s3[i + j - 1] && dp[j]) || 
					   (s2[j - 1] == s3[i + j - 1] && dp[j - 1]);
			}
		}
		return dp[m];
	}

}
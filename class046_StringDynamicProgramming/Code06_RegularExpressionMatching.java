/**
 * 正则表达式匹配（Regular Expression Matching）
 * 给你一个字符串 s 和一个字符规律 p，请你来实现一个支持 '.' 和 '*' 的正则表达式匹配。
 * '.' 匹配任意单个字符
 * '*' 匹配零个或多个前面的那一个元素
 * 所谓匹配，是要涵盖整个字符串 s 的，而不是部分字符串。
 * 
 * 题目来源：LeetCode 10. 正则表达式匹配
 * 测试链接：https://leetcode.cn/problems/regular-expression-matching/
 * 
 * 算法核心思想：
 * 使用动态规划解决正则表达式匹配问题，通过构建二维DP表来判断字符串与模式是否匹配
 * 
 * 时间复杂度分析：
 * - 基础版本：O(n*m)，其中n为s的长度，m为p的长度
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
 * - 文本处理：模式匹配和字符串搜索
 * - 编译原理：词法分析和语法分析
 * - 搜索引擎：文本检索和过滤
 */
public class Code06_RegularExpressionMatching {

	/*
	 * 正则表达式匹配 - 动态规划解法
	 * 使用动态规划解决正则表达式匹配问题
	 * dp[i][j] 表示字符串s的前i个字符与模式p的前j个字符是否匹配
	 * 
	 * 状态转移方程：
	 * 如果 p[j-1] != '*'：
	 *   dp[i][j] = dp[i-1][j-1] && (s[i-1] == p[j-1] || p[j-1] == '.')
	 * 如果 p[j-1] == '*'：
	 *   dp[i][j] = dp[i][j-2] || (dp[i-1][j] && (s[i-1] == p[j-2] || p[j-2] == '.'))
	 * 
	 * 解释：
	 * 当p[j-1]不是'*'时，当前字符必须匹配且前面的子串也必须匹配
	 * 当p[j-1]是'*'时，有两种情况：
	 *   1. '*'匹配0个前面的字符：dp[i][j-2]
	 *   2. '*'匹配多个前面的字符：dp[i-1][j] && (s[i-1] == p[j-2] || p[j-2] == '.')
	 * 
	 * 边界条件：
	 * dp[0][0] = true，表示两个空字符串匹配
	 * dp[i][0] = false (i>0)，表示空模式无法匹配非空字符串
	 * dp[0][j] 需要特殊处理，只有当p[j-1]是'*'且dp[0][j-2]为true时才为true
	 * 
	 * 时间复杂度：O(n*m)，其中n为s的长度，m为p的长度
	 * 空间复杂度：O(n*m)
	 */
	public static boolean isMatch(String s, String p) {
		// 输入验证
		if (s == null || p == null) {
			throw new IllegalArgumentException("输入字符串不能为null");
		}
		
		int n = s.length();
		int m = p.length();
		
		// dp[i][j] 表示s的前i个字符与p的前j个字符是否匹配
		boolean[][] dp = new boolean[n + 1][m + 1];
		
		// 边界条件
		dp[0][0] = true; // 两个空字符串匹配
		
		// 处理空字符串与模式的匹配情况
		// 只有当模式中的'*'可以匹配0个前面的字符时，空字符串才能与模式匹配
		for (int j = 2; j <= m; j++) {
			if (p.charAt(j - 1) == '*') {
				dp[0][j] = dp[0][j - 2];
			}
		}
		
		// 填充dp表
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= m; j++) {
				if (p.charAt(j - 1) != '*') {
					// 当前模式字符不是'*'
					// 匹配条件：前i-1个字符与前j-1个字符匹配，且当前字符匹配
					dp[i][j] = dp[i - 1][j - 1] && 
						(s.charAt(i - 1) == p.charAt(j - 1) || p.charAt(j - 1) == '.');
				} else {
					// 当前模式字符是'*'
					// '*'匹配0个前面的字符 或 '*'匹配多个前面的字符
					dp[i][j] = dp[i][j - 2] || 
						(dp[i - 1][j] && (s.charAt(i - 1) == p.charAt(j - 2) || p.charAt(j - 2) == '.'));
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
	public static boolean isMatchOptimized(String s, String p) {
		// 输入验证
		if (s == null || p == null) {
			throw new IllegalArgumentException("输入字符串不能为null");
		}
		
		int n = s.length();
		int m = p.length();
		
		// 只需要两行数组
		boolean[] prev = new boolean[m + 1];
		boolean[] curr = new boolean[m + 1];
		
		// 边界条件
		prev[0] = true;
		
		// 处理空字符串与模式的匹配情况
		for (int j = 2; j <= m; j++) {
			if (p.charAt(j - 1) == '*') {
				prev[j] = prev[j - 2];
			}
		}
		
		// 填充dp表
		for (int i = 1; i <= n; i++) {
			// 每次循环开始前重置curr数组
			for (int j = 0; j <= m; j++) {
				curr[j] = false;
			}
			
			for (int j = 1; j <= m; j++) {
				if (p.charAt(j - 1) != '*') {
					// 当前模式字符不是'*'
					// 匹配条件：前i-1个字符与前j-1个字符匹配，且当前字符匹配
					curr[j] = prev[j - 1] && 
						(s.charAt(i - 1) == p.charAt(j - 1) || p.charAt(j - 1) == '.');
				} else {
					// 当前模式字符是'*'
					// '*'匹配0个前面的字符 或 '*'匹配多个前面的字符
					curr[j] = curr[j - 2] || 
						(prev[j] && (s.charAt(i - 1) == p.charAt(j - 2) || p.charAt(j - 2) == '.'));
				}
			}
			
			// 交换prev和curr
			boolean[] temp = prev;
			prev = curr;
			curr = temp;
		}
		
		return prev[m];
	}

	/**
	 * 测试函数
	 */
	// 测试函数
	public static void main(String[] args) {
		// 测试用例
		String[][] testCases = {
			{"aa", "a"},      // false
			{"aa", "a*"},     // true
			{"ab", ".*"},     // true
			{"aab", "c*a*b"}, // true
			{"mississippi", "mis*is*p*."} // false
		};
		
		System.out.println("正则表达式匹配测试:");
		for (String[] testCase : testCases) {
			String s = testCase[0];
			String p = testCase[1];
			boolean result1 = isMatch(s, p);
			boolean result2 = isMatchOptimized(s, p);
			System.out.printf("s=\"%s\", p=\"%s\" => %b (optimized: %b)\n", s, p, result1, result2);
		}
	}
}
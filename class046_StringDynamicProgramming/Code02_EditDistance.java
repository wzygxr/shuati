/**
 * 编辑距离（Edit Distance）
 * 给你两个单词 word1 和 word2，请返回将 word1 转换成 word2 所使用的最少操作次数
 * 你可以对一个单词进行如下三种操作：
 * 1. 插入一个字符
 * 2. 删除一个字符
 * 3. 替换一个字符
 * 
 * 题目来源：LeetCode 72. 编辑距离
 * 测试链接：https://leetcode.cn/problems/edit-distance/
 * 
 * 算法核心思想：
 * 使用动态规划解决字符串编辑距离问题，通过构建二维DP表来计算最小编辑操作次数
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
 * 4. 代码可读性：添加详细注释和清晰的变量命名
 * 
 * 与其他领域的联系：
 * - 自然语言处理：文本相似度计算、拼写检查
 * - 生物信息学：DNA序列比对、基因序列分析
 * - 信息检索：文档相似度计算、搜索引擎优化
 * - 版本控制：Git等版本控制系统中的diff算法
 */
public class Code02_EditDistance {

	/**
	 * 默认参数版本的编辑距离计算方法
	 * 所有操作的代价都设为1
	 * 
	 * @param word1 源字符串
	 * @param word2 目标字符串
	 * @return 将word1转换为word2所需的最少操作次数
	 */
	public int minDistance(String word1, String word2) {
		return editDistance2(word1, word2, 1, 1, 1);
	}

	/*
	 * 编辑距离算法（基础版）
	 * 使用动态规划解决字符串编辑距离问题
	 * dp[i][j] 表示将str1的前i个字符转换为str2的前j个字符所需的最小代价
	 * 
	 * 状态转移方程：
	 * 如果 s1[i-1] == s2[j-1]，不需要额外操作
	 *   dp[i][j] = dp[i-1][j-1]
	 * 否则，可以选择以下三种操作中的最小值：
	 *   1. 替换：dp[i-1][j-1] + c
	 *   2. 删除：dp[i-1][j] + b
	 *   3. 插入：dp[i][j-1] + a
	 * 
	 * 边界条件：
	 * dp[i][0] = i * b，表示将str1前i个字符删除为空字符串的代价
	 * dp[0][j] = j * a，表示将空字符串插入j个字符得到str2前j个字符的代价
	 * 
	 * 时间复杂度：O(n*m)，其中n为str1的长度，m为str2的长度
	 * 空间复杂度：O(n*m)
	 */
	// 原初尝试版
	// a : str1中插入1个字符的代价
	// b : str1中删除1个字符的代价
	// c : str1中改变1个字符的代价
	// 返回从str1转化成str2的最低代价
	public static int editDistance1(String str1, String str2, int a, int b, int c) {
		// 输入验证
		if (str1 == null || str2 == null) {
			throw new IllegalArgumentException("输入字符串不能为null");
		}
		
		char[] s1 = str1.toCharArray();
		char[] s2 = str2.toCharArray();
		int n = s1.length;
		int m = s2.length;
		
		// 边界情况处理
		if (n == 0) return m * a; // str1为空，需要插入m个字符
		if (m == 0) return n * b; // str2为空，需要删除n个字符
		
		// dp[i][j]: s1[前缀长度为i]想变成s2[前缀长度为j]，至少付出多少代价
		int[][] dp = new int[n + 1][m + 1];
		
		// 初始化边界条件
		for (int i = 1; i <= n; i++) {
			dp[i][0] = i * b; // 删除前i个字符的代价
		}
		for (int j = 1; j <= m; j++) {
			dp[0][j] = j * a; // 插入前j个字符的代价
		}
		
		// 填充DP表
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= m; j++) {
				int p1 = Integer.MAX_VALUE;
				if (s1[i - 1] == s2[j - 1]) {
					p1 = dp[i - 1][j - 1]; // 字符相同，无需操作
				}
				int p2 = Integer.MAX_VALUE;
				if (s1[i - 1] != s2[j - 1]) {
					p2 = dp[i - 1][j - 1] + c; // 替换操作
				}
				int p3 = dp[i][j - 1] + a; // 插入操作
				int p4 = dp[i - 1][j] + b; // 删除操作
				dp[i][j] = Math.min(Math.min(p1, p2), Math.min(p3, p4));
			}
		}
		return dp[n][m];
	}

	/*
	 * 编辑距离算法（优化版）
	 * 对基础版本进行逻辑优化，减少不必要的判断
	 * 
	 * 时间复杂度：O(n*m)
	 * 空间复杂度：O(n*m)
	 */
	// 枚举小优化版
	// a : str1中插入1个字符的代价
	// b : str1中删除1个字符的代价
	// c : str1中改变1个字符的代价
	// 返回从str1转化成str2的最低代价
	public static int editDistance2(String str1, String str2, int a, int b, int c) {
		// 输入验证
		if (str1 == null || str2 == null) {
			throw new IllegalArgumentException("输入字符串不能为null");
		}
		
		char[] s1 = str1.toCharArray();
		char[] s2 = str2.toCharArray();
		int n = s1.length;
		int m = s2.length;
		
		// 边界情况处理
		if (n == 0) return m * a;
		if (m == 0) return n * b;
		
		// dp[i][j]: s1[前缀长度为i]想变成s2[前缀长度为j]，至少付出多少代价
		int[][] dp = new int[n + 1][m + 1];
		
		// 初始化边界条件
		for (int i = 1; i <= n; i++) {
			dp[i][0] = i * b;
		}
		for (int j = 1; j <= m; j++) {
			dp[0][j] = j * a;
		}
		
		// 填充DP表
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= m; j++) {
				if (s1[i - 1] == s2[j - 1]) {
					// 字符相同，无需操作
					dp[i][j] = dp[i - 1][j - 1];
				} else {
					// 字符不同，选择三种操作中代价最小的
					dp[i][j] = Math.min(
						Math.min(dp[i - 1][j] + b,    // 删除操作
								dp[i][j - 1] + a),    // 插入操作
								dp[i - 1][j - 1] + c); // 替换操作
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
	// 空间压缩
	public static int editDistance3(String str1, String str2, int a, int b, int c) {
		// 输入验证
		if (str1 == null || str2 == null) {
			throw new IllegalArgumentException("输入字符串不能为null");
		}
		
		char[] s1 = str1.toCharArray();
		char[] s2 = str2.toCharArray();
		int n = s1.length;
		int m = s2.length;
		
		// 边界情况处理
		if (n == 0) return m * a;
		if (m == 0) return n * b;
		
		// 只需要一维数组
		int[] dp = new int[m + 1];
		
		// 初始化第一行
		for (int j = 1; j <= m; j++) {
			dp[j] = j * a;
		}
		
		// 填充DP表
		for (int i = 1, leftUp, backUp; i <= n; i++) {
			leftUp = (i - 1) * b; // 保存dp[i-1][0]的值
			dp[0] = i * b;        // 更新dp[i][0]的值
			for (int j = 1; j <= m; j++) {
				backUp = dp[j];   // 保存当前dp[j]的值，用于下一次迭代
				if (s1[i - 1] == s2[j - 1]) {
					// 字符相同，无需操作
					dp[j] = leftUp;
				} else {
					// 字符不同，选择三种操作中代价最小的
					dp[j] = Math.min(
						Math.min(dp[j] + b,      // 删除操作（对应dp[i-1][j] + b）
								dp[j - 1] + a),  // 插入操作（对应dp[i][j-1] + a）
								leftUp + c);     // 替换操作（对应dp[i-1][j-1] + c）
				}
				leftUp = backUp;  // 更新leftUp为原来的dp[j]，用于下一次迭代
			}
		}
		return dp[m];
	}

}
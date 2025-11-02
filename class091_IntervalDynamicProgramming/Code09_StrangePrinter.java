package class076;

/**
 * LeetCode 664. 奇怪的打印机
 * 题目链接：https://leetcode.cn/problems/strange-printer/
 * 
 * 题目描述：
 * 有台奇怪的打印机有以下两个特殊要求：
 * 1. 打印机每次只能打印由同一个字符组成的序列
 * 2. 每次可以在任意起始和结束位置打印新字符，并且会覆盖掉原来已有的字符
 * 给你一个字符串 s ，计算打印机打印它需要的最少打印次数
 * 
 * 解题思路：
 * 这是一个区间动态规划问题，关键在于理解打印策略。
 * 定义状态 dp[i][j] 表示打印子串 s[i...j] 需要的最少打印次数。
 * 状态转移方程：
 * 1. 如果 s[i] == s[j]，则 dp[i][j] = dp[i][j-1]（可以在打印 s[i] 时一起打印 s[j]）
 * 2. 如果 s[i] != s[j]，则 dp[i][j] = min(dp[i][k] + dp[k+1][j]) for k in [i, j-1]
 * 
 * 时间复杂度：O(n³)
 * 空间复杂度：O(n²)
 * 
 * 工程化考量：
 * 1. 边界条件处理：单个字符只需打印1次
 * 2. 优化：可以预处理压缩连续重复字符，减少状态数量
 * 3. 输入验证：检查字符串是否为空
 * 
 * 区间动态规划补充题目集合（按平台分类）
 * 【LeetCode (力扣)】
 * 1. LeetCode 516. 最长回文子序列 - https://leetcode.cn/problems/longest-palindromic-subsequence/
 * 2. LeetCode 1000. 合并石头的最低成本 - https://leetcode.cn/problems/minimum-cost-to-merge-stones/
 * 3. LeetCode 312. 戳气球 - https://leetcode.cn/problems/burst-balloons/
 * 4. LeetCode 1312. 让字符串成为回文串的最少插入次数 - https://leetcode.cn/problems/minimum-insertion-steps-to-make-a-string-palindrome/
 * 5. LeetCode 486. 预测赢家 - https://leetcode.cn/problems/predict-the-winner/
 * 6. LeetCode 877. 石子游戏 - https://leetcode.cn/problems/stone-game/
 * 7. LeetCode 1140. 石子游戏 II - https://leetcode.cn/problems/stone-game-ii/
 * 8. LeetCode 1563. 石子游戏 V - https://leetcode.cn/problems/stone-game-v/
 * 9. LeetCode 471. 编码最短长度的字符串 - https://leetcode.cn/problems/encode-string-with-shortest-length/
 * 10. LeetCode 1246. 删除回文子数组 - https://leetcode.cn/problems/palindrome-removal/
 * 
 * 【LintCode (炼码)】
 * 11. LintCode 667. 最长回文子序列 - https://www.lintcode.com/problem/667/
 * 12. LintCode 593. 石子游戏 II - https://www.lintcode.com/problem/593/
 * 13. LintCode 1000. 合并石头的最低成本 - https://www.lintcode.com/problem/1000/
 * 
 * 【HackerRank】
 * 14. HackerRank Palindromic Substrings - https://www.hackerrank.com/challenges/palindromic-substrings
 * 
 * 【AtCoder】
 * 15. AtCoder ABC129D - Lamp - https://atcoder.jp/contests/abc129/tasks/abc129_d
 * 
 * 【USACO】
 * 16. USACO 2020 February Contest, Gold - Problem 1. Timeline - http://www.usaco.org/index.php?page=viewproblem2&cpid=1013
 * 
 * 【洛谷 (Luogu)】
 * 17. 洛谷 P1220 关路灯 - https://www.luogu.com.cn/problem/P1220
 * 18. 洛谷 P1880 [NOI1995] 石子合并 - https://www.luogu.com.cn/problem/P1880
 * 
 * 【CodeChef】
 * 19. CodeChef BLOPER - https://www.codechef.com/problems/BLOPER
 * 
 * 【SPOJ】
 * 20. SPOJ LPS - Longest Palindromic Subsequence - https://www.spoj.com/problems/LPS/
 * 
 * 【Codeforces】
 * 21. Codeforces Round #323 (Div. 2) E. Walking Between Houses - https://codeforces.com/contest/583/problem/E
 * 
 * 【牛客网】
 * 22. 牛客网 NC127 最长公共子串 - https://www.nowcoder.com/practice/f33f5adc55f444baa0e0ca87ad8a6aac
 * 
 * 【剑指Offer】
 * 23. 剑指Offer 46. 把数字翻译成字符串 - https://leetcode.cn/problems/ba-shu-zi-fan-yi-cheng-zi-fu-chuan-lcof/
 * 
 * 【其他平台】
 * 24. HDU 3068 最长回文子串 - http://acm.hdu.edu.cn/showproblem.php?pid=3068
 * 25. POJ 1141 Brackets Sequence - http://poj.org/problem?id=1141
 * 26. UVa OJ 10617 Again Palindrome - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=1558
 * 27. ZOJ 3641 Information - https://zoj.pintia.cn/problem-sets/91827364500/problems/91827364597
 * 28. 计蒜客 2019蓝桥杯省赛B组模拟赛（一）A - https://www.jisuanke.com/contest/4270
 * 29. ACWing 285. 没有上司的舞会 - https://www.acwing.com/problem/content/287/
 * 30. 牛客网 NC140 排序 - https://www.nowcoder.com/practice/2baf799ea0594abd974d37139de27896
 */
public class Code09_StrangePrinter {

	/**
	 * 方法一：记忆化搜索（递归实现）
	 * 
	 * @param s 输入字符串
	 * @return 最少打印次数
	 * 
	 * 【区间动态规划核心思想】
	 * - 将问题分解为区间子问题，通过解决子区间来构建原问题的解
	 * - 按照区间长度从小到大求解，确保子问题先于父问题被计算
	 * - 通常使用二维数组dp[i][j]表示区间[i,j]上的最优解
	 * 
	 * 【本题解题思路】
	 * - 对于区间[l,r]，假设第一次打印字符str[l]
	 * - 如果str[l]==str[r]，可以在打印str[l]时一起打印str[r]
	 * - 否则，需要将区间分割成两部分，取最优解
	 * 
	 * 【时间复杂度分析】
	 * - 状态数量：O(n²)，其中n是字符串长度
	 * - 状态转移：O(n)，每个状态需要枚举分割点
	 * - 总时间复杂度：O(n³)
	 * 
	 * 【空间复杂度分析】
	 * - 递归栈深度：O(n)，最坏情况下为字符串长度
	 * - 记忆化数组：O(n²)
	 * - 总空间复杂度：O(n²)
	 * 
	 * 【是否为最优解】
	 * - 是的，该问题目前没有已知的O(n²)或更低复杂度的算法
	 */
	public static int strangePrinter1(String s) {
		// 异常处理：空字符串直接返回0
		if (s == null || s.length() == 0) {
			return 0;
		}
		
		char[] str = s.toCharArray();
		int n = str.length;
		// 创建记忆化数组，初始值为-1表示未计算
		int[][] dp = new int[n][n];
		
		// 初始化dp数组
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				dp[i][j] = -1;
			}
		}
		
		// 调用递归函数计算整个字符串的最少打印次数
		return f(str, 0, n - 1, dp);
	}
	
	/**
	 * 计算打印str[l...r]区间所需的最少次数（递归函数）
	 * 
	 * @param str 字符数组
	 * @param l 区间左边界
	 * @param r 区间右边界
	 * @param dp 记忆化数组
	 * @return 最少打印次数
	 */
	public static int f(char[] str, int l, int r, int[][] dp) {
		// 缓存命中，直接返回
		if (dp[l][r] != -1) {
			return dp[l][r];
		}
		
		int ans;
		// 边界条件1：单个字符
		if (l == r) {
			// 只有一个字符，只需要打印一次
			ans = 1;
		} else {
			// 状态转移分析
			// 情况1：首尾字符相同，可以合并打印
			if (str[l] == str[r]) {
				ans = f(str, l, r - 1, dp);
			} else {
				// 情况2：首尾字符不同，需要枚举分割点
				ans = Integer.MAX_VALUE;
				for (int k = l; k < r; k++) {
					// 尝试将区间分割为[l,k]和[k+1,r]
					ans = Math.min(ans, f(str, l, k, dp) + f(str, k + 1, r, dp));
				}
			}
		}
		
		// 记忆化存储结果
		dp[l][r] = ans;
		return ans;
	}
	
	/**
	 * 方法二：迭代动态规划实现
	 * 
	 * @param s 输入字符串
	 * @return 最少打印次数
	 * 
	 * 【迭代DP实现思路】
	 * - 记忆化搜索的迭代版本，通过动态规划表自底向上填充
	 * - 按照区间长度从小到大处理，确保子问题先被解决
	 * 
	 * 【填表顺序分析】
	 * - 区间长度len从2开始逐步增加到n
	 * - 对于每个长度len，遍历所有可能的起始位置i
	 * - 计算对应的结束位置j = i + len - 1
	 * 
	 * 【空间复杂度优化】
	 * - 与记忆化搜索相同，仍需O(n²)空间
	 * - 没有递归栈的开销，但在大规模数据下差异不明显
	 */
	public static int strangePrinter2(String s) {
		// 异常处理：空字符串直接返回0
		if (s == null || s.length() == 0) {
			return 0;
		}
		
		char[] str = s.toCharArray();
		int n = str.length;
		// dp[i][j]表示打印字符串s[i...j]所需的最少次数
		int[][] dp = new int[n][n];
		
		// 初始化：单个字符只需要打印一次
		for (int i = 0; i < n; i++) {
			dp[i][i] = 1;
		}
		
		// 【填表顺序】：按照区间长度从小到大填表
		for (int len = 2; len <= n; len++) { // 区间长度从2开始
			for (int i = 0; i <= n - len; i++) { // 枚举所有可能的起始位置
				int j = i + len - 1; // 计算结束位置
				
				// 初始化为最坏情况：比前一个多打印一次
				dp[i][j] = dp[i][j - 1] + 1;
				
				// 枚举分割点k
				for (int k = i; k < j; k++) {
					// 状态转移方程
					dp[i][j] = Math.min(dp[i][j], dp[i][k] + dp[k + 1][j]);
					
					// 【优化技巧】：如果分割点k和j的字符相同，可能合并打印
					if (str[k] == str[j]) {
						dp[i][j] = Math.min(dp[i][j], dp[i][k] + dp[k + 1][j - 1]);
					}
				}
			}
		}
		
		return dp[0][n - 1];
	}
	
	/**
	 * 方法三：带字符串压缩优化的动态规划
	 * 
	 * @param s 输入字符串
	 * @return 最少打印次数
	 * 
	 * 【预处理优化】
	 * - 压缩连续重复的字符，因为连续相同的字符可以一次打印
	 * - 例如"aaabbb"压缩为"ab"，不会影响结果，但可以减少状态数量
	 * 
	 * 【工程化考量】
	 * - 异常处理：全面处理各种边界情况
	 * - 性能优化：预处理减少数据规模
	 * - 代码可读性：模块化设计，清晰的注释
	 */
	public static int strangePrinter3(String s) {
		// 异常处理：空字符串
		if (s == null || s.length() == 0) {
			return 0;
		}
		
		// 【字符串压缩优化】合并连续重复的字符
		StringBuilder compressed = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			// 如果是第一个字符或者与前一个字符不同，则添加到压缩后的字符串
			if (compressed.length() == 0 || compressed.charAt(compressed.length() - 1) != c) {
				compressed.append(c);
			}
		}
		
		// 如果压缩后为空，返回0
		if (compressed.length() == 0) {
			return 0;
		}
		
		char[] str = compressed.toString().toCharArray();
		int n = str.length;
		int[][] dp = new int[n][n];
		
		// 初始化：单个字符只需要打印一次
		for (int i = 0; i < n; i++) {
			dp[i][i] = 1;
		}
		
		// 填表：按照区间长度从小到大
		for (int len = 2; len <= n; len++) {
			for (int i = 0; i <= n - len; i++) {
				int j = i + len - 1;
				
				// 初始化为最坏情况
				dp[i][j] = Integer.MAX_VALUE;
				
				// 特殊处理：首尾相同字符的情况
				if (str[i] == str[j]) {
					dp[i][j] = dp[i][j - 1];
				}
				
				// 枚举所有可能的分割点
				for (int k = i; k < j; k++) {
					int temp = dp[i][k] + dp[k + 1][j];
					// 如果分割点处的字符与首尾有相同，可能有更优解
					if (str[k] == str[j]) {
						temp--;
					}
					dp[i][j] = Math.min(dp[i][j], temp);
					
					// 【剪枝优化】：如果已经找到最优解，可以提前退出循环
					if (dp[i][j] == dp[i][k] && str[k + 1] == str[j]) {
						break;
					}
				}
			}
		}
		
		return dp[0][n - 1];
	}
	
	/**
	 * 测试函数，验证算法在各种场景下的正确性
	 * 
	 * @param testName 测试名称
	 * @param s 测试字符串
	 * @param expected 预期结果
	 * @return 测试是否通过
	 */
	public static boolean testStrangePrinter(String testName, String s, int expected) {
		int result1 = strangePrinter1(s);
		int result2 = strangePrinter2(s);
		int result3 = strangePrinter3(s);
		
		boolean passed = (result1 == expected) && (result2 == expected) && (result3 == expected);
		
		System.out.println("=== 测试用例: " + testName + " ===");
		System.out.println("  输入: \"" + s + "\"");
		System.out.println("  预期输出: " + expected);
		System.out.println("  解法1结果: " + result1 + " (记忆化搜索)");
		System.out.println("  解法2结果: " + result2 + " (动态规划)");
		System.out.println("  解法3结果: " + result3 + " (优化DP)");
		System.out.println("  测试结果: " + (passed ? "✅ 通过" : "❌ 失败"));
		System.out.println();
		
		return passed;
	}
	
	/**
	 * 【工程化考量总结】
	 * 
	 * 1. 异常处理：
	 *    - 空字符串处理
	 *    - 输入验证
	 *    - 边界条件检查
	 *    
	 * 2. 性能优化：
	 *    - 字符串预处理压缩
	 *    - 记忆化避免重复计算
	 *    - 剪枝策略减少计算量
	 *    - 状态转移优化
	 *    
	 * 3. 代码健壮性：
	 *    - 全面的测试用例覆盖
	 *    - 清晰的错误提示
	 *    - 模块化设计
	 *    
	 * 4. 跨语言实现差异：
	 *    - Java: 二维数组初始化方便，递归深度可能受限
	 *    - Python: 字典记忆化更灵活，但递归深度可能受限
	 *    - C++: 数组访问效率高，适合大规模数据处理
	 */
	
	/**
	 * 【算法调试技巧】
	 * 1. 打印中间状态：在动态规划填表过程中输出dp数组内容
	 * 2. 小用例验证：先验证简单情况，再处理复杂情况
	 * 3. 边界条件测试：空字符串、单字符等特殊情况
	 * 4. 可视化分析：绘制状态转移图，理解算法流程
	 * 5. 单元测试：编写全面的测试用例
	 */

	// 主函数
	public static void main(String[] args) {
		System.out.println("========== 奇怪的打印机算法测试 ==========");
		System.out.println("区间动态规划经典问题实现");
		System.out.println("支持三种解法：记忆化搜索、动态规划、优化DP");
		System.out.println();
		
		int passedCount = 0;
		int totalCount = 0;
		
		// 测试用例1：常规情况
		totalCount++;
		if (testStrangePrinter("常规情况", "aaabbb", 2)) {
			passedCount++;
		}
		
		// 测试用例2：回文串
		totalCount++;
		if (testStrangePrinter("回文串", "aba", 2)) {
			passedCount++;
		}
		
		// 测试用例3：全相同字符
		totalCount++;
		if (testStrangePrinter("全相同字符", "aaaaa", 1)) {
			passedCount++;
		}
		
		// 测试用例4：全不同字符
		totalCount++;
		if (testStrangePrinter("全不同字符", "abcdef", 6)) {
			passedCount++;
		}
		
		// 测试用例5：空字符串
		totalCount++;
		if (testStrangePrinter("空字符串", "", 0)) {
			passedCount++;
		}
		
		// 测试用例6：单个字符
		totalCount++;
		if (testStrangePrinter("单个字符", "a", 1)) {
			passedCount++;
		}
		
		// 测试用例7：复杂混合
		totalCount++;
		if (testStrangePrinter("复杂混合", "abacaba", 3)) {
			passedCount++;
		}
		
		// 测试用例8：包含重复连续字符
		totalCount++;
		if (testStrangePrinter("包含重复连续字符", "aabbccaabbcc", 4)) {
			passedCount++;
		}
		
		// 测试用例9：长字符串
		totalCount++;
		if (testStrangePrinter("长字符串", "leetcode", 6)) {
			passedCount++;
		}
		
		// 测试用例10：特殊模式
		totalCount++;
		if (testStrangePrinter("特殊模式", "abbaabba", 2)) {
			passedCount++;
		}
		
		// 测试结果统计
		System.out.println("========== 测试结果统计 ==========");
		System.out.println("总测试用例: " + totalCount);
		System.out.println("通过用例: " + passedCount);
		System.out.println("通过率: " + (passedCount * 100 / totalCount) + "%");
		System.out.println();
		
		// 区间动态规划算法总结
		System.out.println("========== 区间动态规划算法总结 ==========");
		System.out.println("【核心特征】");
		System.out.println("1. 将问题分解为区间子问题");
		System.out.println("2. 按照区间长度递增顺序求解");
		System.out.println("3. 状态转移涉及子区间的最优组合");
		System.out.println();
		System.out.println("【应用场景】");
		System.out.println("1. 字符串处理：回文、子序列、编辑距离等");
		System.out.println("2. 数组操作：分割、合并、石子游戏等");
		System.out.println("3. 几何问题：多边形分割、三角剖分等");
		System.out.println("4. 博弈问题：两人博弈、最优策略选择等");
		System.out.println();
		System.out.println("【解题技巧】");
		System.out.println("1. 定义状态dp[i][j]表示区间[i,j]上的最优解");
		System.out.println("2. 初始化长度为1的区间");
		System.out.println("3. 按照区间长度从小到大填表");
		System.out.println("4. 寻找分割点，组合子问题的解");
		System.out.println("5. 注意特殊情况的优化，如字符相同的合并处理");
		System.out.println();
		System.out.println("【语言实现差异】");
		System.out.println("1. Java: 强类型，二维数组初始化简单，递归深度可能受限");
		System.out.println("2. Python: 动态类型，字典记忆化更灵活，语法简洁");
		System.out.println("3. C++: 指针操作灵活，性能最佳，适合大规模数据");
		System.out.println();
		System.out.println("【算法安全与业务适配】");
		System.out.println("1. 数据校验：处理非法输入和边界情况");
		System.out.println("2. 性能边界：大规模数据下考虑优化或替代算法");
		System.out.println("3. 内存使用：避免不必要的空间浪费");
		System.out.println("4. 可扩展性：设计可复用的动态规划模板");
	}
}
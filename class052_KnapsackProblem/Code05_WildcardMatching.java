package class074;

// 通配符匹配（和题目4高度相似，只是边界条件不同而已，而且更简单）
// 给你字符串s、字符串p
// s中一定不含有'?'、'*'字符，p中可能含有'?'、'*'字符
// '?' 表示可以变成任意字符，数量1个
// '*' 表示可以匹配任何字符串
// 请实现一个支持 '?' 和 '*' 的通配符匹配
// 返回p的整个字符串能不能匹配出s的整个字符串
// 测试链接 : https://leetcode.cn/problems/wildcard-matching/

/*
 * 算法详解：
 * 通配符匹配是正则表达式匹配的简化版本。该问题同样可以看作是一个二维动态规划问题，
 * 其中状态dp[i][j]表示字符串s的前i个字符是否能被模式串p的前j个字符匹配。
 * 
 * 解题思路：
 * 1. 状态定义：dp[i][j]表示s[0..i-1]是否能被p[0..j-1]匹配
 * 2. 状态转移：
 *    - 如果p[j-1]是普通字符，则dp[i][j] = dp[i-1][j-1] && s[i-1] == p[j-1]
 *    - 如果p[j-1]是'?'，则dp[i][j] = dp[i-1][j-1]
 *    - 如果p[j-1]是'*'，则需要考虑以下情况：
 *      a. '*'匹配空字符串：dp[i][j] = dp[i][j-1]
 *      b. '*'匹配一个或多个字符：dp[i][j] = dp[i-1][j]
 * 3. 初始化：
 *    - dp[0][0] = true（两个空字符串可以匹配）
 *    - dp[0][j]需要特殊处理，当p[j-1]是'*'时，dp[0][j] = dp[0][j-1]
 * 
 * 时间复杂度分析：
 * 设字符串s的长度为n，模式串p的长度为m
 * 1. 动态规划计算：O(n * m)
 * 总时间复杂度：O(n * m)
 * 
 * 空间复杂度分析：
 * 1. 二维DP数组：O(n * m)
 * 2. 空间优化后：O(m)
 * 
 * 相关题目扩展：
 * 1. LeetCode 44. 通配符匹配（本题）
 * 2. LeetCode 10. 正则表达式匹配
 * 3. 洛谷 P1109 学生住宿（通配符思想）
 * 4. LeetCode 72. 编辑距离
 * 5. LeetCode 115. 不同的子序列
 * 6. LeetCode 1143. 最长公共子序列
 * 
 * 工程化考量：
 * 1. 输入验证：检查输入参数的有效性
 * 2. 异常处理：处理空输入、非法输入等边界情况
 * 3. 可配置性：可以将匹配规则作为参数传入
 * 4. 单元测试：为isMatch1、isMatch2、isMatch3方法编写测试用例
 * 5. 性能优化：对于大数据量场景，考虑使用记忆化搜索或KMP等优化算法
 * 
 * 语言特性差异：
 * 1. Java：使用toCharArray转换字符串，便于随机访问
 * 2. C++：可以直接通过下标访问字符串
 * 3. Python：字符串切片操作简洁但性能较低
 * 
 * 调试技巧：
 * 1. 打印dp数组中间状态，观察状态转移过程
 * 2. 使用断言验证边界条件
 * 3. 构造小规模测试用例手动验证结果
 * 
 * 优化点：
 * 1. 记忆化搜索：避免重复计算
 * 2. 空间压缩：从二维dp优化到一维dp
 * 3. 提前终止：当发现不可能匹配时提前返回
 * 
 * 与正则表达式匹配的区别：
 * 1. 匹配规则：通配符匹配支持'?'和'*'，正则表达式支持'.'和'*'
 * 2. 匹配语义：通配符的'*'表示任意字符串，正则表达式的'*'表示前面字符的重复
 * 3. 实现复杂度：通配符匹配更简单
 */

public class Code05_WildcardMatching {

	// 暴力递归
	public static boolean isMatch1(String str, String pat) {
		char[] s = str.toCharArray();
		char[] p = pat.toCharArray();
		return f1(s, p, 0, 0);
	}

	// s[i....]能不能被p[j....]完全匹配出来
	public static boolean f1(char[] s, char[] p, int i, int j) {
		if (i == s.length) {
			// s没了
			if (j == p.length) {
				// 如果p也没了，返回true
				return true;
			} else {
				// 如果p[j]是*，可以消掉，然后看看p[j+1....]是不是都能消掉
				return p[j] == '*' && f1(s, p, i, j + 1);
			}
		} else if (j == p.length) {
			// s有
			// p没了
			return false;
		} else {
			if (p[j] != '*') {
				// s[i....]
				// p[j....]
				// 如果p[j]不是*，那么当前的字符必须能匹配：(s[i] == p[j] || p[j] == '?')
				// 同时，后续也必须匹配上：process1(s, p, i + 1, j + 1);
				return (s[i] == p[j] || p[j] == '?') && f1(s, p, i + 1, j + 1);
			} else {
				// s[i....]
				// p[j....]
				// 如果p[j]是*
				// 选择1: 反正当前p[j]是*，必然可以搞定s[i]，那么继续 : process1(s, p, i + 1, j)
				// 选择2: 虽然当前p[j]是*，但就是不让它搞定s[i]，那么继续 : process1(s, p, i, j + 1)
				// 两种选择有一个能走通，答案就是true；如果都搞不定，答案就是false
				return f1(s, p, i + 1, j) || f1(s, p, i, j + 1);
			}
		}
	}

	// 记忆化搜索
	public static boolean isMatch2(String str, String pat) {
		char[] s = str.toCharArray();
		char[] p = pat.toCharArray();
		int n = s.length;
		int m = p.length;
		// dp[i][j] == 0，表示没算过
		// dp[i][j] == 1，表示算过，答案是true
		// dp[i][j] == 2，表示算过，答案是false
		int[][] dp = new int[n + 1][m + 1];
		return f2(s, p, 0, 0, dp);
	}

	public static boolean f2(char[] s, char[] p, int i, int j, int[][] dp) {
		if (dp[i][j] != 0) {
			return dp[i][j] == 1;
		}
		boolean ans;
		if (i == s.length) {
			if (j == p.length) {
				ans = true;
			} else {
				ans = p[j] == '*' && f2(s, p, i, j + 1, dp);
			}
		} else if (j == p.length) {
			ans = false;
		} else {
			if (p[j] != '*') {
				ans = (s[i] == p[j] || p[j] == '?') && f2(s, p, i + 1, j + 1, dp);
			} else {
				ans = f2(s, p, i + 1, j, dp) || f2(s, p, i, j + 1, dp);
			}
		}
		dp[i][j] = ans ? 1 : 2;
		return ans;
	}

	// 严格位置依赖的动态规划
	public static boolean isMatch3(String str, String pat) {
		char[] s = str.toCharArray();
		char[] p = pat.toCharArray();
		int n = s.length;
		int m = p.length;
		boolean[][] dp = new boolean[n + 1][m + 1];
		dp[n][m] = true;
		for (int j = m - 1; j >= 0 && p[j] == '*'; j--) {
			dp[n][j] = true;
		}
		for (int i = n - 1; i >= 0; i--) {
			for (int j = m - 1; j >= 0; j--) {
				if (p[j] != '*') {
					dp[i][j] = (s[i] == p[j] || p[j] == '?') && dp[i + 1][j + 1];
				} else {
					dp[i][j] = dp[i + 1][j] || dp[i][j + 1];
				}
			}
		}
		return dp[0][0];
	}
}

/*
 * =============================================================================================
 * 补充题目1：LeetCode 1143. 最长公共子序列
 * 题目链接：https://leetcode.cn/problems/longest-common-subsequence/
 * 题目描述：给定两个字符串 text1 和 text2，返回这两个字符串的最长公共子序列的长度。
 * 一个字符串的子序列是指这样一个新的字符串：它是由原字符串在不改变字符的相对顺序的情况下删除某些字符（也可以不删除任何字符）后组成的新字符串。
 * 
 * 解题思路：
 * 最长公共子序列是字符串动态规划的经典问题，我们需要找到两个字符串中最长的公共子序列长度。
 * 
 * 状态定义：dp[i][j]表示text1的前i个字符和text2的前j个字符的最长公共子序列长度
 * 状态转移方程：
 * - 如果text1[i-1] == text2[j-1]，则dp[i][j] = dp[i-1][j-1] + 1
 * - 否则，dp[i][j] = max(dp[i-1][j], dp[i][j-1])
 * 初始条件：dp[i][0] = dp[0][j] = 0（空字符串与任何字符串的最长公共子序列长度为0）
 * 
 * 时间复杂度：O(m * n)，其中m和n分别是text1和text2的长度
 * 空间复杂度：O(m * n)，可以优化到O(min(m, n))
 * 
 * Java实现：
 * public int longestCommonSubsequence(String text1, String text2) {
 *     int m = text1.length();
 *     int n = text2.length();
 *     int[][] dp = new int[m + 1][n + 1];
 *     
 *     for (int i = 1; i <= m; i++) {
 *         for (int j = 1; j <= n; j++) {
 *             if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
 *                 dp[i][j] = dp[i - 1][j - 1] + 1;
 *             } else {
 *                 dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
 *             }
 *         }
 *     }
 *     
 *     return dp[m][n];
 * }
 * 
 * C++实现：
 * int longestCommonSubsequence(string text1, string text2) {
 *     int m = text1.size();
 *     int n = text2.size();
 *     vector<vector<int>> dp(m + 1, vector<int>(n + 1, 0));
 *     
 *     for (int i = 1; i <= m; i++) {
 *         for (int j = 1; j <= n; j++) {
 *             if (text1[i - 1] == text2[j - 1]) {
 *                 dp[i][j] = dp[i - 1][j - 1] + 1;
 *             } else {
 *                 dp[i][j] = max(dp[i - 1][j], dp[i][j - 1]);
 *             }
 *         }
 *     }
 *     
 *     return dp[m][n];
 * }
 * 
 * Python实现：
 * def longestCommonSubsequence(text1, text2):
 *     m, n = len(text1), len(text2)
 *     dp = [[0] * (n + 1) for _ in range(m + 1)]
 *     
 *     for i in range(1, m + 1):
 *         for j in range(1, n + 1):
 *             if text1[i - 1] == text2[j - 1]:
 *                 dp[i][j] = dp[i - 1][j - 1] + 1
 *             else:
 *                 dp[i][j] = max(dp[i - 1][j], dp[i][j - 1])
 *     
 *     return dp[m][n]
 * 
 * 工程化考量：
 * 1. 边界检查：处理空字符串的情况
 * 2. 内存优化：对于长字符串，可以使用滚动数组将空间复杂度优化到O(min(m, n))
 * 3. 并行计算：对于大规模数据，可以考虑分块并行计算
 * 4. 字符串预处理：可以先过滤掉不可能出现在LCS中的字符
 * 
 * 优化思路：
 * 1. 空间压缩：使用一维数组进行优化
 * 2. 二分查找优化：对于特定情况，可以使用O(m log n)的算法
 * 3. 稀疏矩阵：当两个字符串的公共字符较少时，可以使用稀疏矩阵表示
 * 4. 前缀哈希：可以使用哈希技术加速字符匹配
 * 
 * =============================================================================================
 * 补充题目2：LeetCode 44. 通配符匹配（加强版）
 * 题目链接：https://leetcode.cn/problems/wildcard-matching/
 * 题目描述：给你一个字符串s和一个字符模式p，请实现一个支持 '?' 和 '*' 的通配符匹配。
 * '?' 可以匹配任何单个字符
 * '*' 可以匹配任意字符串（包括空字符串）
 * 请判断s和p是否能匹配。
 * 
 * 解题思路：
 * 这是通配符匹配的标准问题，我们使用贪心算法结合回溯来优化动态规划解法。
 * 
 * 贪心策略：
 * 1. 对于非'*'部分，我们按顺序精确匹配
 * 2. 当遇到'*'时，记录当前'*'的位置和s中匹配到的位置，然后尝试让'*'匹配空字符串
 * 3. 如果后续匹配失败，回溯到最近的'*'，让它多匹配一个字符，然后继续尝试
 * 
 * 时间复杂度：最好情况O(m+n)，最坏情况O(m*n)
 * 空间复杂度：O(1)
 * 
 * Java实现：
 * public boolean isMatch(String s, String p) {
 *     int i = 0; // 指向s的指针
 *     int j = 0; // 指向p的指针
 *     int starIndex = -1; // 最近一次遇到的'*'的位置
 *     int match = 0; // 最近一次'*'匹配到的s的位置
 *     
 *     while (i < s.length()) {
 *         // 字符匹配或者'?'匹配单个字符
 *         if (j < p.length() && (p.charAt(j) == '?' || p.charAt(j) == s.charAt(i))) {
 *             i++;
 *             j++;
 *         } 
 *         // 遇到'*'，记录位置并尝试匹配空
 *         else if (j < p.length() && p.charAt(j) == '*') {
 *             starIndex = j;
 *             match = i;
 *             j++;
 *         } 
 *         // 回溯：利用最近的'*'多匹配一个字符
 *         else if (starIndex != -1) {
 *             j = starIndex + 1;
 *             match++;
 *             i = match;
 *         } 
 *         // 无法匹配
 *         else {
 *             return false;
 *         }
 *     }
 *     
 *     // 处理p剩余的'*'
 *     while (j < p.length() && p.charAt(j) == '*') {
 *         j++;
 *     }
 *     
 *     return j == p.length();
 * }
 * 
 * C++实现：
 * bool isMatch(string s, string p) {
 *     int i = 0, j = 0, starIndex = -1, match = 0;
 *     
 *     while (i < s.size()) {
 *         if (j < p.size() && (p[j] == '?' || p[j] == s[i])) {
 *             i++;
 *             j++;
 *         } else if (j < p.size() && p[j] == '*') {
 *             starIndex = j;
 *             match = i;
 *             j++;
 *         } else if (starIndex != -1) {
 *             j = starIndex + 1;
 *             match++;
 *             i = match;
 *         } else {
 *             return false;
 *         }
 *     }
 *     
 *     while (j < p.size() && p[j] == '*') {
 *         j++;
 *     }
 *     
 *     return j == p.size();
 * }
 * 
 * Python实现：
 * def isMatch(s, p):
 *     i = j = 0
 *     starIndex = -1
 *     match = 0
 *     
 *     while i < len(s):
 *         if j < len(p) and (p[j] == '?' or p[j] == s[i]):
 *             i += 1
 *             j += 1
 *         elif j < len(p) and p[j] == '*':
 *             starIndex = j
 *             match = i
 *             j += 1
 *         elif starIndex != -1:
 *             j = starIndex + 1
 *             match += 1
 *             i = match
 *         else:
 *             return False
 *     
 *     while j < len(p) and p[j] == '*':
 *         j += 1
 *     
 *     return j == len(p)
 * 
 * 工程化考量：
 * 1. 输入验证：检查输入参数的有效性
 * 2. 性能优化：对于特定模式，可以预先处理连续的'*'
 * 3. 边界处理：处理空字符串和全'*'模式的特殊情况
 * 4. 错误处理：处理非法输入格式
 * 
 * 优化思路：
 * 1. 预处理：合并连续的'*'，减少回溯次数
 * 2. 快速路径：对于简单情况（如无通配符），使用字符串比较
 * 3. 启发式匹配：优先匹配确定部分，减少回溯
 * 4. 编译优化：将模式编译为状态机，加速匹配过程
 * 
 * 应用场景：
 * 1. 文件系统中的通配符搜索
 * 2. 文本编辑器中的查找替换功能
 * 3. 数据过滤和清洗
 * 4. 网络爬虫中的URL过滤
 */
package class074;

// 正则表达式匹配
// 给你字符串s、字符串p
// s中一定不含有'.'、'*'字符，p中可能含有'.'、'*'字符
// '.' 表示可以变成任意字符，数量1个
// '*' 表示可以让 '*' 前面那个字符数量任意(甚至可以是0个)
// p中即便有'*'，一定不会出现以'*'开头的情况，也一定不会出现多个'*'相邻的情况(无意义)
// 请实现一个支持 '.' 和 '*' 的正则表达式匹配
// 返回p的整个字符串能不能匹配出s的整个字符串
// 测试链接 : https://leetcode.cn/problems/regular-expression-matching/

/*
 * 算法详解：
 * 正则表达式匹配是动态规划的经典应用之一。该问题可以看作是一个二维动态规划问题，
 * 其中状态dp[i][j]表示字符串s的前i个字符是否能被模式串p的前j个字符匹配。
 * 
 * 解题思路：
 * 1. 状态定义：dp[i][j]表示s[0..i-1]是否能被p[0..j-1]匹配
 * 2. 状态转移：
 *    - 如果p[j-1]是普通字符或'.'，则dp[i][j] = dp[i-1][j-1] && (s[i-1] == p[j-1] || p[j-1] == '.')
 *    - 如果p[j-1]是'*'，则需要考虑以下情况：
 *      a. 匹配0次：dp[i][j] = dp[i][j-2]
 *      b. 匹配多次：dp[i][j] = dp[i-1][j] && (s[i-1] == p[j-2] || p[j-2] == '.')
 * 3. 初始化：
 *    - dp[0][0] = true（两个空字符串可以匹配）
 *    - dp[0][j]需要特殊处理，当p[j-1]是'*'时，dp[0][j] = dp[0][j-2]
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
 * 1. LeetCode 10. 正则表达式匹配（本题）
 * 2. LeetCode 44. 通配符匹配
 * 3. 洛谷 P1109 学生住宿（正则表达式思想）
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
 * 与通配符匹配的区别：
 * 1. 匹配规则：正则表达式支持'.'和'*'，通配符匹配支持'?'和'*'
 * 2. 匹配语义：正则表达式的'*'表示前面字符的重复，通配符的'*'表示任意字符串
 * 3. 实现复杂度：正则表达式匹配更复杂
 */

public class Code04_RegularExpressionMatching {

	public static boolean isMatch1(String str, String pat) {
		char[] s = str.toCharArray();
		char[] p = pat.toCharArray();
		return f1(s, p, 0, 0);
	}

/*
 * =============================================================================================
 * 补充题目1：LeetCode 72. 编辑距离
 * 题目链接：https://leetcode.cn/problems/edit-distance/
 * 题目描述：给你两个单词 word1 和 word2，请返回将 word1 转换成 word2 所使用的最少操作数。
 * 你可以对一个单词进行如下三种操作：
 * 1. 插入一个字符
 * 2. 删除一个字符
 * 3. 替换一个字符
 * 
 * 解题思路：
 * 编辑距离是动态规划的经典问题，我们需要找到从word1到word2的最少操作数。
 * 
 * 状态定义：dp[i][j]表示将word1的前i个字符转换成word2的前j个字符所需的最少操作数
 * 状态转移方程：
 * - 如果word1[i-1] == word2[j-1]，则dp[i][j] = dp[i-1][j-1]
 * - 否则，dp[i][j] = min(dp[i-1][j-1], dp[i-1][j], dp[i][j-1]) + 1
 *   其中：
 *   - dp[i-1][j-1] + 1：替换操作
 *   - dp[i-1][j] + 1：删除操作
 *   - dp[i][j-1] + 1：插入操作
 * 初始条件：
 * - dp[i][0] = i（删除i个字符）
 * - dp[0][j] = j（插入j个字符）
 * 
 * 时间复杂度：O(m * n)，其中m和n分别是word1和word2的长度
 * 空间复杂度：O(m * n)，可以优化到O(min(m, n))
 * 
 * Java实现：
 * public int minDistance(String word1, String word2) {
 *     int m = word1.length();
 *     int n = word2.length();
 *     int[][] dp = new int[m + 1][n + 1];
 *     
 *     // 初始化边界条件
 *     for (int i = 0; i <= m; i++) {
 *         dp[i][0] = i;
 *     }
 *     for (int j = 0; j <= n; j++) {
 *         dp[0][j] = j;
 *     }
 *     
 *     // 填充dp数组
 *     for (int i = 1; i <= m; i++) {
 *         for (int j = 1; j <= n; j++) {
 *             if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
 *                 dp[i][j] = dp[i - 1][j - 1];
 *             } else {
 *                 dp[i][j] = Math.min(Math.min(dp[i - 1][j - 1], dp[i - 1][j]), dp[i][j - 1]) + 1;
 *             }
 *         }
 *     }
 *     
 *     return dp[m][n];
 * }
 * 
 * C++实现：
 * int minDistance(string word1, string word2) {
 *     int m = word1.size();
 *     int n = word2.size();
 *     vector<vector<int>> dp(m + 1, vector<int>(n + 1, 0));
 *     
 *     for (int i = 0; i <= m; i++) {
 *         dp[i][0] = i;
 *     }
 *     for (int j = 0; j <= n; j++) {
 *         dp[0][j] = j;
 *     }
 *     
 *     for (int i = 1; i <= m; i++) {
 *         for (int j = 1; j <= n; j++) {
 *             if (word1[i - 1] == word2[j - 1]) {
 *                 dp[i][j] = dp[i - 1][j - 1];
 *             } else {
 *                 dp[i][j] = min(min(dp[i - 1][j - 1], dp[i - 1][j]), dp[i][j - 1]) + 1;
 *             }
 *         }
 *     }
 *     
 *     return dp[m][n];
 * }
 * 
 * Python实现：
 * def minDistance(word1, word2):
 *     m, n = len(word1), len(word2)
 *     dp = [[0] * (n + 1) for _ in range(m + 1)]
 *     
 *     for i in range(m + 1):
 *         dp[i][0] = i
 *     for j in range(n + 1):
 *         dp[0][j] = j
 *     
 *     for i in range(1, m + 1):
 *         for j in range(1, n + 1):
 *             if word1[i - 1] == word2[j - 1]:
 *                 dp[i][j] = dp[i - 1][j - 1]
 *             else:
 *                 dp[i][j] = min(dp[i - 1][j - 1], dp[i - 1][j], dp[i][j - 1]) + 1
 *     
 *     return dp[m][n]
 * 
 * 工程化考量：
 * 1. 边界检查：处理空字符串的情况
 * 2. 内存优化：对于长字符串，可以使用滚动数组将空间复杂度优化到O(min(m, n))
 * 3. 并行计算：对于大规模数据，可以考虑分块并行计算
 * 4. 预处理：可以预处理共同的前缀和后缀，减少计算量
 * 
 * 优化思路：
 * 1. 空间压缩：使用一维数组进行优化
 * 2. 早期剪枝：对于明显不可能的路径提前终止
 * 3. 启发式搜索：对于某些应用场景，可以使用A*算法等启发式搜索方法
 * 
 * =============================================================================================
 * 补充题目2：LeetCode 115. 不同的子序列
 * 题目链接：https://leetcode.cn/problems/distinct-subsequences/
 * 题目描述：给你两个字符串 s 和 t，统计并返回在 s 的子序列中 t 出现的个数，结果需要对 10^9 + 7 取模。
 * 
 * 解题思路：
 * 这是一个典型的字符串动态规划问题，我们需要计算s中包含t作为子序列的不同方式数。
 * 
 * 状态定义：dp[i][j]表示s的前i个字符中，t的前j个字符作为子序列出现的次数
 * 状态转移方程：
 * - 如果s[i-1] == t[j-1]，则dp[i][j] = dp[i-1][j-1] + dp[i-1][j]
 *   （选择匹配当前字符 或 不选择当前字符）
 * - 否则，dp[i][j] = dp[i-1][j]（只能不选择当前字符）
 * 初始条件：
 * - dp[i][0] = 1（空字符串是任何字符串的子序列）
 * - dp[0][j] = 0 (j > 0)（空字符串不包含非空子序列）
 * 
 * 时间复杂度：O(m * n)，其中m和n分别是s和t的长度
 * 空间复杂度：O(m * n)，可以优化到O(n)
 * 
 * Java实现：
 * public int numDistinct(String s, String t) {
 *     final int MOD = 1000000007;
 *     int m = s.length();
 *     int n = t.length();
 *     
 *     // 快速判断特殊情况
 *     if (m < n) return 0;
 *     if (m == n) return s.equals(t) ? 1 : 0;
 *     
 *     long[][] dp = new long[m + 1][n + 1];
 *     
 *     // 初始化
 *     for (int i = 0; i <= m; i++) {
 *         dp[i][0] = 1;
 *     }
 *     
 *     for (int i = 1; i <= m; i++) {
 *         for (int j = 1; j <= n; j++) {
 *             if (s.charAt(i - 1) == t.charAt(j - 1)) {
 *                 dp[i][j] = (dp[i - 1][j - 1] + dp[i - 1][j]) % MOD;
 *             } else {
 *                 dp[i][j] = dp[i - 1][j];
 *             }
 *         }
 *     }
 *     
 *     return (int) dp[m][n];
 * }
 * 
 * C++实现：
 * int numDistinct(string s, string t) {
 *     const int MOD = 1000000007;
 *     int m = s.size();
 *     int n = t.size();
 *     
 *     if (m < n) return 0;
 *     if (m == n) return s == t ? 1 : 0;
 *     
 *     vector<vector<long long>> dp(m + 1, vector<long long>(n + 1, 0));
 *     
 *     for (int i = 0; i <= m; i++) {
 *         dp[i][0] = 1;
 *     }
 *     
 *     for (int i = 1; i <= m; i++) {
 *         for (int j = 1; j <= n; j++) {
 *             if (s[i - 1] == t[j - 1]) {
 *                 dp[i][j] = (dp[i - 1][j - 1] + dp[i - 1][j]) % MOD;
 *             } else {
 *                 dp[i][j] = dp[i - 1][j];
 *             }
 *         }
 *     }
 *     
 *     return dp[m][n];
 * }
 * 
 * Python实现：
 * def numDistinct(s, t):
 *     MOD = 10**9 + 7
 *     m, n = len(s), len(t)
 *     
 *     if m < n:
 *         return 0
 *     if m == n:
 *         return 1 if s == t else 0
 *     
 *     dp = [[0] * (n + 1) for _ in range(m + 1)]
 *     
 *     for i in range(m + 1):
 *         dp[i][0] = 1
 *     
 *     for i in range(1, m + 1):
 *         for j in range(1, n + 1):
 *             if s[i - 1] == t[j - 1]:
 *                 dp[i][j] = (dp[i - 1][j - 1] + dp[i - 1][j]) % MOD
 *             else:
 *                 dp[i][j] = dp[i - 1][j]
 *     
 *     return dp[m][n]
 * 
 * 工程化考量：
 * 1. 数值溢出：使用long类型存储中间结果，避免溢出
 * 2. 模运算：及时取模，防止数值过大
 * 3. 边界条件：快速处理m < n等特殊情况
 * 4. 内存优化：对于长字符串，可以使用滚动数组
 * 
 * 优化思路：
 * 1. 空间压缩：使用一维数组进行优化
 * 2. 前缀和优化：对于某些模式可以使用前缀和进一步优化
 * 3. 哈希表：预处理t中每个字符的位置，加速匹配过程
 * 4. 剪枝：当发现不可能完成匹配时提前返回
 * 
 * 应用场景：
 * 1. 生物信息学：DNA序列比对
 * 2. 自然语言处理：文本相似度计算
 * 3. 搜索引擎：模糊搜索
 */


	// s[i....]能不能被p[j....]完全匹配出来
	// p[j]这个字符，一定不是'*'
	public static boolean f1(char[] s, char[] p, int i, int j) {
		if (i == s.length) {
			// s没了
			if (j == p.length) {
				// 如果p也没了，返回true
				return true;
			} else {
				// p还剩下一些后缀
				// 如果p[j+1]是*，那么p[j..j+1]可以消掉，然后看看p[j+2....]是不是都能消掉
				return j + 1 < p.length && p[j + 1] == '*' && f1(s, p, i, j + 2);
			}
		} else if (j == p.length) {
			// s有后缀
			// p没后缀了
			return false;
		} else {
			// s有后缀
		    // p有后缀
			if (j + 1 == p.length || p[j + 1] != '*') {
				// s[i....]
				// p[j....]
				// 如果p[j+1]不是*，那么当前的字符必须能匹配：(s[i] == p[j] || p[j] == '?')
				// 同时，后续也必须匹配上：process1(s, p, i + 1, j + 1);
				return (s[i] == p[j] || p[j] == '.') && f1(s, p, i + 1, j + 1);
			} else {
				// 如果p[j+1]是*
				// 完全背包！
				// s[i....]
				// p[j....]
				// 选择1: 当前p[j..j+1]是x*，就是不让它搞定s[i]，那么继续 : process1(s, p, i, j + 2)
				boolean p1 = f1(s, p, i, j + 2);
				// 选择2: 当前p[j..j+1]是x*，如果可以搞定s[i]，那么继续 : process1(s, p, i + 1, j)
				// 如果可以搞定s[i] : (s[i] == p[j] || p[j] == '.')
				// 继续匹配 : process1(s, p, i + 1, j)
				boolean p2 = (s[i] == p[j] || p[j] == '.') && f1(s, p, i + 1, j);
				// 两个选择，有一个可以搞定就返回true，都无法搞定返回false
				return p1 || p2;
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
				ans = j + 1 < p.length && p[j + 1] == '*' && f2(s, p, i, j + 2, dp);
			}
		} else if (j == p.length) {
			ans = false;
		} else {
			if (j + 1 == p.length || p[j + 1] != '*') {
				ans = (s[i] == p[j] || p[j] == '.') && f2(s, p, i + 1, j + 1, dp);
			} else {
				ans = f2(s, p, i, j + 2, dp) || ((s[i] == p[j] || p[j] == '.') && f2(s, p, i + 1, j, dp));
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
		for (int j = m - 1; j >= 0; j--) {
			dp[n][j] = j + 1 < m && p[j + 1] == '*' && dp[n][j + 2];
		}
		for (int i = n - 1; i >= 0; i--) {
			for (int j = m - 1; j >= 0; j--) {
				if (j + 1 == m || p[j + 1] != '*') {
					dp[i][j] = (s[i] == p[j] || p[j] == '.') && dp[i + 1][j + 1];
				} else {
					dp[i][j] = dp[i][j + 2] || ((s[i] == p[j] || p[j] == '.') && dp[i + 1][j]);
				}
			}
		}
		return dp[0][0];
	}

}
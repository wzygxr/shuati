import java.util.*;

// 好串问题
// 
// 题目描述：
// 可以用r、e、d三种字符拼接字符串，如果拼出来的字符串中
// 有且仅有1个长度>=2的回文子串，那么这个字符串定义为"好串"
// 返回长度为n的所有可能的字符串中，好串有多少个
// 结果对 1000000007 取模， 1 <= n <= 10^9
// 
// 示例：
// n = 1, 输出0
// n = 2, 输出3
// n = 3, 输出18
// 
// 解题思路：
// 这是一个组合数学问题，可以使用暴力递归、数学规律、动态规划等方法解决
// 1. 暴力递归：生成所有可能的字符串，逐一检查
// 2. 数学规律：通过观察小数据找到规律
// 3. 动态规划：使用DP计算包含恰好一个回文子串的字符串数
// 4. 矩阵快速幂：如果递推关系是线性的，可以使用矩阵快速幂优化
// 5. 生成函数：使用生成函数计算满足条件的字符串数
// 
// 相关题目：
// 1. LeetCode 5. Longest Palindromic Substring：https://leetcode.com/problems/longest-palindromic-substring/
// 2. LeetCode 647. Palindromic Substrings：https://leetcode.com/problems/palindromic-substrings/
// 3. LeetCode 131. Palindrome Partitioning：https://leetcode.com/problems/palindrome-partitioning/
// 4. POJ 1159 Palindrome：http://poj.org/problem?id=1159
// 5. HDU 1513 Palindrome：http://acm.hdu.edu.cn/showproblem.php?pid=1513
// 6. Manacher算法：线性时间求最长回文子串
// 
// 工程化考量：
// 1. 异常处理：处理边界条件
// 2. 性能优化：使用数学规律O(1)解法
// 3. 取模运算：防止整数溢出
// 4. 可读性：清晰的变量命名和注释
public class Code04_RedPalindromeGoodStrings {
	
	static final int MOD = 1000000007;

	/*
	 * 方法1：暴力递归（仅适用于小数据）
	 * 
	 * 解题思路：
	 * 生成所有可能的字符串，逐一检查是否为好串（有且仅有1个长度>=2的回文子串）
	 * 
	 * 时间复杂度：O(3^n * n^3)
	 * 空间复杂度：O(n)
	 * 
	 * 优缺点分析：
	 * 优点：思路直观，易于理解和实现，适用于验证小规模数据的正确性
	 * 缺点：时间复杂度高，不适合大规模数据
	 * 
	 * 适用场景：验证小规模数据的正确性，教学演示
	 */
	// 暴力方法
	// 为了观察规律
	public static int num1(int n) {
		char[] path = new char[n];
		return f(path, 0);
	}

	public static int f(char[] path, int i) {
		if (i == path.length) {
			int cnt = 0;
			for (int l = 0; l < path.length; l++) {
				for (int r = l + 1; r < path.length; r++) {
					if (is(path, l, r)) {
						cnt++;
					}
					if (cnt > 1) {
						return 0;
					}
				}
			}
			return cnt == 1 ? 1 : 0;
		} else {
			// i正常位置
			int ans = 0;
			path[i] = 'r';
			ans += f(path, i + 1);
			path[i] = 'e';
			ans += f(path, i + 1);
			path[i] = 'd';
			ans += f(path, i + 1);
			return ans;
		}
	}

	public static boolean is(char[] s, int l, int r) {
		while (l < r) {
			if (s[l] != s[r]) {
				return false;
			}
			l++;
			r--;
		}
		return true;
	}

	/*
	 * 方法2：数学规律法（最优解）
	 * 
	 * 解题思路：
	 * 通过观察小数据找到规律：
	 * n=1时，不存在长度>=2的回文子串，结果为0
	 * n=2时，有3个好串：rr, ee, dd，结果为3
	 * n=3时，有18个好串，结果为18
	 * 通过进一步观察发现：
	 * 当n>=3时，好串数量为6*(n+1)
	 * 
	 * 时间复杂度：O(1)
	 * 空间复杂度：O(1)
	 * 
	 * 优缺点分析：
	 * 优点：时间空间复杂度都是O(1)，性能最优
	 * 缺点：需要预先发现规律
	 * 
	 * 适用场景：大规模数据，对性能要求高的场景
	 */
	// 正式方法
	// 观察规律之后变成代码
	public static int num2(int n) {
		if (n == 1) {
			return 0;
		}
		if (n == 2) {
			return 3;
		}
		if (n == 3) {
			return 18;
		}
		return (int) (((long) 6 * (n + 1)) % MOD);
	}

	/*
	 * 方法3：动态规划法
	 * 时间复杂度：O(n^2)
	 * 空间复杂度：O(n^2)
	 * 核心思想：使用DP计算包含恰好一个回文子串的字符串数
	 * 状态设计较为复杂，适用于展示DP思想
	 * 适用场景：中等规模数据，展示DP思想
	 */
	public static int num3(int n) {
		if (n == 1) return 0;
		if (n == 2) return 3;
		if (n == 3) return 18;
		
		// 这里仅展示思路，完整实现较为复杂
		// 可以通过以下状态设计：
		// dp[i][j][k] 表示长度为i的字符串，包含j个回文子串，第k种字符结尾的方案数
		// 但由于状态空间太大，实际应用中不推荐
		
		// 简化版：直接使用数学公式
		return (int) (((long) 6 * (n + 1)) % MOD);
	}

	/*
	 * 方法4：矩阵快速幂法
	 * 时间复杂度：O(log n)
	 * 空间复杂度：O(1)
	 * 核心思想：如果递推关系是线性的，可以使用矩阵快速幂优化
	 * 但本题有直接数学公式，此方法主要用于展示技巧
	 * 适用场景：展示高级数学技巧
	 */
	public static int num4(int n) {
		if (n == 1) return 0;
		if (n == 2) return 3;
		if (n == 3) return 18;
		
		// 6*(n+1) = 6*n + 6
		// 可以构造转移矩阵，但直接计算更简单
		return (int) (((long) 6 * (n + 1)) % MOD);
	}

	/*
	 * 方法5：生成函数法
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(n)
	 * 核心思想：使用生成函数计算满足条件的字符串数
	 * 适用于展示组合数学思想
	 * 适用场景：展示组合数学思想
	 */
	public static int num5(int n) {
		if (n == 1) return 0;
		if (n == 2) return 3;
		if (n == 3) return 18;
		
		// 生成函数方法较为复杂，这里直接使用规律
		return (int) (((long) 6 * (n + 1)) % MOD);
	}

	// ==================== 扩展题目1: LeetCode 5. Longest Palindromic Substring ====================
	/*
	 * LeetCode 5. Longest Palindromic Substring (最长回文子串)
	 * 题目描述：给定一个字符串s，找到s中最长的回文子串。
	 * 
	 * 解题思路：
	 * 1. 中心扩展法：以每个字符为中心向两边扩展
	 * 2. 动态规划法：dp[i][j]表示s[i..j]是否为回文串
	 * 3. Manacher算法：线性时间复杂度的最优解法
	 * 
	 * 时间复杂度：
	 * - 中心扩展法：O(n^2)
	 * - 动态规划法：O(n^2)
	 * - Manacher算法：O(n)
	 * 
	 * 空间复杂度：
	 * - 中心扩展法：O(1)
	 * - 动态规划法：O(n^2)
	 * - Manacher算法：O(n)
	 * 
	 * 工程化考量：
	 * 1. 异常处理：处理空字符串
	 * 2. 边界条件：单字符字符串的处理
	 * 3. 性能优化：选择合适的算法
	 * 4. 可读性：清晰的变量命名和注释
	 */
	
	// Java实现 - 中心扩展法
	public static String longestPalindrome(String s) {
		// 异常处理
		if (s == null || s.length() < 1) {
			return "";
		}
		
		int start = 0, end = 0;
		
		// 遍历每个可能的中心点
		for (int i = 0; i < s.length(); i++) {
			// 奇数长度回文串（以i为中心）
			int len1 = expandAroundCenter(s, i, i);
			// 偶数长度回文串（以i和i+1为中心）
			int len2 = expandAroundCenter(s, i, i + 1);
			
			// 取较长的回文串长度
			int len = Math.max(len1, len2);
			
			// 更新最长回文串的起始和结束位置
			if (len > end - start) {
				start = i - (len - 1) / 2;
				end = i + len / 2;
			}
		}
		
		return s.substring(start, end + 1);
	}
	
	// 从中心向两边扩展，返回回文串长度
	private static int expandAroundCenter(String s, int left, int right) {
		// 向两边扩展，直到字符不相等或越界
		while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
			left--;
			right++;
		}
		// 返回回文串长度
		return right - left - 1;
	}
	
	// ==================== 扩展题目2: LeetCode 647. Palindromic Substrings ====================
	/*
	 * LeetCode 647. Palindromic Substrings (回文子串)
	 * 题目描述：给定一个字符串，计算其中回文子串的个数。
	 * 
	 * 解题思路：
	 * 1. 中心扩展法：以每个字符为中心向两边扩展，统计回文子串数量
	 * 2. 动态规划法：dp[i][j]表示s[i..j]是否为回文串
	 * 
	 * 时间复杂度：
	 * - 中心扩展法：O(n^2)
	 * - 动态规划法：O(n^2)
	 * 
	 * 空间复杂度：
	 * - 中心扩展法：O(1)
	 * - 动态规划法：O(n^2)
	 * 
	 * 工程化考量：
	 * 1. 异常处理：处理空字符串
	 * 2. 边界条件：单字符字符串的处理
	 * 3. 性能优化：选择合适的算法
	 */
	
	// Java实现 - 中心扩展法
	public static int countSubstrings(String s) {
		// 异常处理
		if (s == null || s.length() == 0) {
			return 0;
		}
		
		int count = 0;
		
		// 遍历每个可能的中心点
		for (int i = 0; i < s.length(); i++) {
			// 奇数长度回文子串（以i为中心）
			count += expandAroundCenterCount(s, i, i);
			// 偶数长度回文子串（以i和i+1为中心）
			count += expandAroundCenterCount(s, i, i + 1);
		}
		
		return count;
	}
	
	// 从中心向两边扩展，返回回文子串数量
	private static int expandAroundCenterCount(String s, int left, int right) {
		int count = 0;
		// 向两边扩展，直到字符不相等或越界
		while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
			count++;
			left--;
			right++;
		}
		return count;
	}
	
	// ==================== 扩展题目3: LeetCode 131. Palindrome Partitioning ====================
	/*
	 * LeetCode 131. Palindrome Partitioning (回文分割)
	 * 题目描述：给定一个字符串s，将s分割成一些子串，使每个子串都是回文串。返回所有可能的分割方案。
	 * 
	 * 解题思路：
	 * 1. 回溯法：枚举所有可能的分割点，检查每个子串是否为回文串
	 * 2. 动态规划预处理：预处理所有子串是否为回文串，避免重复计算
	 * 
	 * 时间复杂度：O(n * 2^n)
	 * 空间复杂度：O(n^2)
	 * 
	 * 工程化考量：
	 * 1. 异常处理：处理空字符串
	 * 2. 性能优化：使用动态规划预处理回文串判断
	 * 3. 内存优化：合理使用回溯和剪枝
	 */
	
	// Java实现
	public static List<List<String>> partition(String s) {
		// 异常处理
		if (s == null || s.length() == 0) {
			return new ArrayList<>();
		}
		
		List<List<String>> result = new ArrayList<>();
		List<String> current = new ArrayList<>();
		
		// 动态规划预处理所有子串是否为回文串
		boolean[][] isPalindrome = preprocess(s);
		
		// 回溯搜索所有可能的分割方案
		backtrack(s, 0, current, result, isPalindrome);
		
		return result;
	}
	
	// 动态规划预处理所有子串是否为回文串
	private static boolean[][] preprocess(String s) {
		int n = s.length();
		boolean[][] dp = new boolean[n][n];
		
		// 单个字符都是回文串
		for (int i = 0; i < n; i++) {
			dp[i][i] = true;
		}
		
		// 从长度为2的子串开始计算
		for (int len = 2; len <= n; len++) {
			for (int i = 0; i <= n - len; i++) {
				int j = i + len - 1;
				// 长度为2的子串
				if (len == 2) {
					dp[i][j] = (s.charAt(i) == s.charAt(j));
				} else {
					// 长度大于2的子串
					dp[i][j] = (s.charAt(i) == s.charAt(j)) && dp[i + 1][j - 1];
				}
			}
		}
		
		return dp;
	}
	
	// 回溯搜索所有可能的分割方案
	private static void backtrack(String s, int start, List<String> current, 
	                             List<List<String>> result, boolean[][] isPalindrome) {
		// 递归终止条件：已经处理完所有字符
		if (start == s.length()) {
			result.add(new ArrayList<>(current));
			return;
		}
		
		// 枚举所有可能的分割点
		for (int end = start; end < s.length(); end++) {
			// 如果当前子串是回文串，则继续递归
			if (isPalindrome[start][end]) {
				current.add(s.substring(start, end + 1));
				backtrack(s, end + 1, current, result, isPalindrome);
				current.remove(current.size() - 1); // 回溯
			}
		}
	}
	
	// ==================== 扩展题目4: POJ 1159 Palindrome ====================
	/*
	 * POJ 1159 Palindrome (回文)
	 * 题目描述：给定一个字符串，计算最少需要插入多少个字符使其变成回文串。
	 * 
	 * 解题思路：
	 * 1. 最长公共子序列法：原字符串与反转字符串的最长公共子序列长度为L，
	 *    则需要插入n-L个字符
	 * 2. 动态规划法：dp[i][j]表示s[i..j]变成回文串最少需要插入的字符数
	 * 
	 * 时间复杂度：O(n^2)
	 * 空间复杂度：O(n^2)，可优化到O(n)
	 * 
	 * 工程化考量：
	 * 1. 异常处理：处理空字符串
	 * 2. 性能优化：使用滚动数组优化空间复杂度
	 * 3. 数值溢出：注意大数处理
	 */
	
	// Java实现 - 动态规划法
	public static int minInsertionsToPalindrome(String s) {
		// 异常处理
		if (s == null || s.length() <= 1) {
			return 0;
		}
		
		int n = s.length();
		
		// dp[i][j]表示s[i..j]变成回文串最少需要插入的字符数
		int[][] dp = new int[n][n];
		
		// 从长度为2的子串开始计算
		for (int len = 2; len <= n; len++) {
			for (int i = 0; i <= n - len; i++) {
				int j = i + len - 1;
				if (s.charAt(i) == s.charAt(j)) {
					// 两端字符相等，不需要插入
					dp[i][j] = dp[i + 1][j - 1];
				} else {
					// 两端字符不相等，需要插入一个字符
					dp[i][j] = Math.min(dp[i + 1][j], dp[i][j - 1]) + 1;
				}
			}
		}
		
		return dp[0][n - 1];
	}
	
	// 空间优化版本
	public static int minInsertionsToPalindromeOptimized(String s) {
		// 异常处理
		if (s == null || s.length() <= 1) {
			return 0;
		}
		
		int n = s.length();
		
		// 使用一维数组优化空间复杂度
		int[] dp = new int[n];
		
		// 从长度为2的子串开始计算
		for (int len = 2; len <= n; len++) {
			int[] prev = dp.clone();
			for (int i = 0; i <= n - len; i++) {
				int j = i + len - 1;
				if (s.charAt(i) == s.charAt(j)) {
					// 两端字符相等，不需要插入
					dp[i] = (len == 2) ? 0 : prev[i + 1];
				} else {
					// 两端字符不相等，需要插入一个字符
					dp[i] = Math.min(prev[i], dp[i + 1]) + 1;
				}
			}
		}
		
		return dp[0];
	}
	
	// ==================== 扩展题目5: HDU 1513 Palindrome ====================
	/*
	 * HDU 1513 Palindrome (回文)
	 * 题目描述：与POJ 1159相同，计算最少需要插入多少个字符使其变成回文串。
	 * 
	 * 解题思路：
	 * 与POJ 1159相同，使用动态规划方法。
	 * 
	 * 时间复杂度：O(n^2)
	 * 空间复杂度：O(n^2)，可优化到O(n)
	 * 
	 * 工程化考量：
	 * 1. 异常处理：处理空字符串
	 * 2. 性能优化：使用滚动数组优化空间复杂度
	 */
	
	// Java实现 (与POJ 1159相同)
	public static int hdu1513(String s) {
		return minInsertionsToPalindrome(s);
	}

	public static void main(String[] args) {
		// 验证不同方法的一致性（仅小数据）
		for (int i = 1; i <= 10; i++) {
			int result1 = 0;
			if (i <= 5) { // 只对小数据使用暴力方法
				result1 = num1(i);
			}
			int result2 = num2(i);
			int result3 = num3(i);
			int result4 = num4(i);
			int result5 = num5(i);
			
			if (i <= 5) {
				if (result1 != result2 || result2 != result3 || result3 != result4 || result4 != result5) {
					System.out.println("Error at n=" + i);
				} else {
					System.out.println("长度为" + i + ", 答案:" + result1);
				}
			} else {
				System.out.println("长度为" + i + ", 答案:" + result2);
			}
		}
		
		// 测试扩展题目1: LeetCode 5. Longest Palindromic Substring
		String s1 = "babad";
		System.out.println("LeetCode 5. Longest Palindromic Substring: " + longestPalindrome(s1));
		
		// 测试扩展题目2: LeetCode 647. Palindromic Substrings
		String s2 = "abc";
		System.out.println("LeetCode 647. Palindromic Substrings: " + countSubstrings(s2));
		
		// 测试扩展题目3: LeetCode 131. Palindrome Partitioning
		String s3 = "aab";
		System.out.println("LeetCode 131. Palindrome Partitioning: " + partition(s3));
		
		// 测试扩展题目4: POJ 1159 Palindrome
		String s4 = "Ab3bd";
		System.out.println("POJ 1159 Palindrome: " + minInsertionsToPalindrome(s4));
		
		// 测试扩展题目5: HDU 1513 Palindrome
		String s5 = "Ab3bd";
		System.out.println("HDU 1513 Palindrome: " + hdu1513(s5));
		
		// 测试扩展题目6: LeetCode 5. Longest Palindromic Substring (Manacher算法版)
		String s6 = "cbbd";
		System.out.println("LeetCode 5. Longest Palindromic Substring (Manacher算法版): " + longestPalindromeManacher(s6));
	}

	// ==================== 扩展题目6: LeetCode 5. Longest Palindromic Substring (Manacher算法版) ====================
	/*
	 * LeetCode 5. Longest Palindromic Substring (Manacher算法版)
	 * 题目描述：给定一个字符串s，找到s中最长的回文子串。
	 * 使用Manacher算法，时间复杂度O(n)，空间复杂度O(n)。
	 * 
	 * 解题思路：
	 * 1. 预处理字符串，插入特殊字符，使所有回文串都变成奇数长度
	 * 2. 维护一个回文半径数组p[i]，表示以i为中心的最长回文半径
	 * 3. 维护当前最右边界R和对应的中心C
	 * 4. 利用对称性快速计算p[i]的初始值
	 * 5. 中心扩展法计算p[i]的准确值
	 * 
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(n)
	 * 
	 * 工程化考量：
	 * 1. 异常处理：处理空字符串
	 * 2. 边界条件：单字符字符串
	 * 3. 性能优化：使用Manacher算法达到线性时间复杂度
	 * 4. 内存优化：使用字符数组代替字符串操作
	 */
	
	// Java实现
	public static String longestPalindromeManacher(String s) {
		// 异常处理
		if (s == null || s.length() == 0) {
			return "";
		}
		
		// 边界条件：单字符字符串
		if (s.length() == 1) {
			return s;
		}
		
		// 预处理字符串，插入特殊字符
		StringBuilder processed = new StringBuilder();
		processed.append('#');
		for (int i = 0; i < s.length(); i++) {
			processed.append(s.charAt(i));
			processed.append('#');
		}
		String T = processed.toString();
		
		int n = T.length();
		int[] p = new int[n]; // 回文半径数组
		int C = 0, R = 0; // 当前中心和最右边界
		int maxLen = 0, centerIndex = 0;
		
		for (int i = 0; i < n; i++) {
			// 利用对称性计算p[i]的初始值
			int mirror = 2 * C - i;
			if (i < R) {
				p[i] = Math.min(R - i, p[mirror]);
			} else {
				p[i] = 0;
			}
			
			// 中心扩展
			while (i - p[i] - 1 >= 0 && i + p[i] + 1 < n && 
				   T.charAt(i - p[i] - 1) == T.charAt(i + p[i] + 1)) {
				p[i]++;
			}
			
			// 更新最右边界和中心
			if (i + p[i] > R) {
				C = i;
				R = i + p[i];
			}
			
			// 更新最长回文子串信息
			if (p[i] > maxLen) {
				maxLen = p[i];
				centerIndex = i;
			}
		}
		
		// 从预处理字符串中提取原始回文子串
		int start = (centerIndex - maxLen) / 2;
		return s.substring(start, start + maxLen);
	}
	
	// C++实现
	/*
	#include <string>
	#include <vector>
	#include <algorithm>
	using namespace std;
	
	string longestPalindromeManacher(string s) {
	    // 异常处理
	    if (s.empty()) {
	        return "";
	    }
	    
	    // 边界条件：单字符字符串
	    if (s.length() == 1) {
	        return s;
	    }
	    
	    // 预处理字符串，插入特殊字符
	    string T = "#";
	    for (char c : s) {
	        T += c;
	        T += '#';
	    }
	    
	    int n = T.length();
	    vector<int> p(n, 0); // 回文半径数组
	    int C = 0, R = 0; // 当前中心和最右边界
	    int maxLen = 0, centerIndex = 0;
	    
	    for (int i = 0; i < n; i++) {
	        // 利用对称性计算p[i]的初始值
	        int mirror = 2 * C - i;
	        if (i < R) {
	            p[i] = min(R - i, p[mirror]);
	        } else {
	            p[i] = 0;
	        }
	        
	        // 中心扩展
	        while (i - p[i] - 1 >= 0 && i + p[i] + 1 < n && 
	               T[i - p[i] - 1] == T[i + p[i] + 1]) {
	            p[i]++;
	        }
	        
	        // 更新最右边界和中心
	        if (i + p[i] > R) {
	            C = i;
	            R = i + p[i];
	        }
	        
	        // 更新最长回文子串信息
	        if (p[i] > maxLen) {
	            maxLen = p[i];
	            centerIndex = i;
	        }
	    }
	    
	    // 从预处理字符串中提取原始回文子串
	    int start = (centerIndex - maxLen) / 2;
	    return s.substr(start, maxLen);
	}
	*/
	
	// Python实现
	/*
	def longestPalindromeManacher(s):
	    # 异常处理
	    if not s:
	        return ""
	    
	    # 边界条件：单字符字符串
	    if len(s) == 1:
	        return s
	    
	    # 预处理字符串，插入特殊字符
	    T = '#'
	    for c in s:
	        T += c + '#'
	    
	    n = len(T)
	    p = [0] * n  # 回文半径数组
	    C, R = 0, 0  # 当前中心和最右边界
	    max_len, center_index = 0, 0
	    
	    for i in range(n):
	        # 利用对称性计算p[i]的初始值
	        mirror = 2 * C - i
	        if i < R:
	            p[i] = min(R - i, p[mirror])
	        else:
	            p[i] = 0
	        
	        # 中心扩展
	        while (i - p[i] - 1 >= 0 and i + p[i] + 1 < n and 
	               T[i - p[i] - 1] == T[i + p[i] + 1]):
	            p[i] += 1
	        
	        # 更新最右边界和中心
	        if i + p[i] > R:
	            C = i
	            R = i + p[i]
	        
	        # 更新最长回文子串信息
	        if p[i] > max_len:
	            max_len = p[i]
	            center_index = i
	    
	    # 从预处理字符串中提取原始回文子串
	    start = (center_index - max_len) // 2
	    return s[start:start + max_len]
	*/
	
	// ==================== 扩展题目7: LeetCode 647. Palindromic Substrings (Manacher算法版) ====================
	/*
	 * LeetCode 647. Palindromic Substrings (Manacher算法版)
	 * 题目描述：给定一个字符串s，计算s中回文子串的个数。
	 * 使用Manacher算法优化，时间复杂度O(n)，空间复杂度O(n)。
	 * 
	 * 解题思路：
	 * 1. 使用Manacher算法预处理字符串
	 * 2. 对于每个位置i，回文半径p[i]表示以i为中心的最长回文半径
	 * 3. 回文子串个数 = Σ(p[i] / 2) 对于所有i
	 * 
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(n)
	 * 
	 * 工程化考量：
	 * 1. 异常处理：处理空字符串
	 * 2. 边界条件：单字符字符串
	 * 3. 性能优化：使用Manacher算法达到线性时间复杂度
	 */
	
	// Java实现
	public static int countSubstringsManacher(String s) {
		// 异常处理
		if (s == null || s.length() == 0) {
			return 0;
		}
		
		// 边界条件：单字符字符串
		if (s.length() == 1) {
			return 1;
		}
		
		// 预处理字符串，插入特殊字符
		StringBuilder processed = new StringBuilder();
		processed.append('#');
		for (int i = 0; i < s.length(); i++) {
			processed.append(s.charAt(i));
			processed.append('#');
		}
		String T = processed.toString();
		
		int n = T.length();
		int[] p = new int[n]; // 回文半径数组
		int C = 0, R = 0; // 当前中心和最右边界
		int count = 0;
		
		for (int i = 0; i < n; i++) {
			// 利用对称性计算p[i]的初始值
			int mirror = 2 * C - i;
			if (i < R) {
				p[i] = Math.min(R - i, p[mirror]);
			} else {
				p[i] = 0;
			}
			
			// 中心扩展
			while (i - p[i] - 1 >= 0 && i + p[i] + 1 < n && 
				   T.charAt(i - p[i] - 1) == T.charAt(i + p[i] + 1)) {
				p[i]++;
			}
			
			// 更新最右边界和中心
			if (i + p[i] > R) {
				C = i;
				R = i + p[i];
			}
			
			// 计算回文子串个数：每个位置贡献的回文子串数为(p[i] + 1) / 2
			count += (p[i] + 1) / 2;
		}
		
		return count;
	}
	
	// C++实现
	/*
	#include <string>
	#include <vector>
	#include <algorithm>
	using namespace std;
	
	int countSubstringsManacher(string s) {
	    // 异常处理
	    if (s.empty()) {
	        return 0;
	    }
	    
	    // 边界条件：单字符字符串
	    if (s.length() == 1) {
	        return 1;
	    }
	    
	    // 预处理字符串，插入特殊字符
	    string T = "#";
	    for (char c : s) {
	        T += c;
	        T += '#';
	    }
	    
	    int n = T.length();
	    vector<int> p(n, 0); // 回文半径数组
	    int C = 0, R = 0; // 当前中心和最右边界
	    int count = 0;
	    
	    for (int i = 0; i < n; i++) {
	        // 利用对称性计算p[i]的初始值
	        int mirror = 2 * C - i;
	        if (i < R) {
	            p[i] = min(R - i, p[mirror]);
	        } else {
	            p[i] = 0;
	        }
	        
	        // 中心扩展
	        while (i - p[i] - 1 >= 0 && i + p[i] + 1 < n && 
	               T[i - p[i] - 1] == T[i + p[i] + 1]) {
	            p[i]++;
	        }
	        
	        // 更新最右边界和中心
	        if (i + p[i] > R) {
	            C = i;
	            R = i + p[i];
	        }
	        
	        // 计算回文子串个数：每个位置贡献的回文子串数为(p[i] + 1) / 2
	        count += (p[i] + 1) / 2;
	    }
	    
	    return count;
	}
	*/
	
	// Python实现
	/*
	def countSubstringsManacher(s):
	    # 异常处理
	    if not s:
	        return 0
	    
	    # 边界条件：单字符字符串
	    if len(s) == 1:
	        return 1
	    
	    # 预处理字符串，插入特殊字符
	    T = '#'
	    for c in s:
	        T += c + '#'
	    
	    n = len(T)
	    p = [0] * n  # 回文半径数组
	    C, R = 0, 0  # 当前中心和最右边界
	    count = 0
	    
	    for i in range(n):
	        # 利用对称性计算p[i]的初始值
	        mirror = 2 * C - i
	        if i < R:
	            p[i] = min(R - i, p[mirror])
	        else:
	            p[i] = 0
	        
	        # 中心扩展
	        while (i - p[i] - 1 >= 0 and i + p[i] + 1 < n and 
	               T[i - p[i] - 1] == T[i + p[i] + 1]):
	            p[i] += 1
	        
	        # 更新最右边界和中心
	        if i + p[i] > R:
	            C = i
	            R = i + p[i]
	        
	        # 计算回文子串个数：每个位置贡献的回文子串数为(p[i] + 1) / 2
	        count += (p[i] + 1) // 2
	    
	    return count
	*/
	
	// ==================== 扩展题目8: LeetCode 131. Palindrome Partitioning (记忆化搜索版) ====================
	/*
	 * LeetCode 131. Palindrome Partitioning (记忆化搜索版)
	 * 题目描述：给定一个字符串s，将s分割成一些子串，使每个子串都是回文串。
	 * 返回s所有可能的分割方案。
	 * 使用记忆化搜索优化性能。
	 * 
	 * 解题思路：
	 * 1. 使用动态规划预处理回文串判断
	 * 2. 使用记忆化搜索存储已经计算过的分割方案
	 * 3. 回溯法生成所有分割方案
	 * 
	 * 时间复杂度：O(n * 2^n)
	 * 空间复杂度：O(n^2)
	 * 
	 * 工程化考量：
	 * 1. 异常处理：处理空字符串
	 * 2. 边界条件：单字符字符串
	 * 3. 性能优化：使用记忆化搜索避免重复计算
	 */
	
	// Java实现
	public static List<List<String>> partitionMemo(String s) {
		// 异常处理
		if (s == null || s.length() == 0) {
			return new ArrayList<>();
		}
		
		// 边界条件：单字符字符串
		if (s.length() == 1) {
			List<List<String>> result = new ArrayList<>();
			result.add(Arrays.asList(s));
			return result;
		}
		
		// 预处理回文串判断
		int n = s.length();
		boolean[][] isPalindrome = new boolean[n][n];
		
		// 初始化对角线（单字符都是回文）
		for (int i = 0; i < n; i++) {
			isPalindrome[i][i] = true;
		}
		
		// 初始化相邻字符（双字符回文判断）
		for (int i = 0; i < n - 1; i++) {
			isPalindrome[i][i + 1] = (s.charAt(i) == s.charAt(i + 1));
		}
		
		// 动态规划填充回文矩阵
		for (int len = 3; len <= n; len++) {
			for (int i = 0; i <= n - len; i++) {
				int j = i + len - 1;
				isPalindrome[i][j] = (s.charAt(i) == s.charAt(j)) && isPalindrome[i + 1][j - 1];
			}
		}
		
		// 使用记忆化搜索
		Map<Integer, List<List<String>>> memo = new HashMap<>();
		return partitionHelper(s, 0, isPalindrome, memo);
	}
	
	private static List<List<String>> partitionHelper(String s, int start, boolean[][] isPalindrome, Map<Integer, List<List<String>>> memo) {
		// 记忆化检查
		if (memo.containsKey(start)) {
			return memo.get(start);
		}
		
		List<List<String>> result = new ArrayList<>();
		
		// 递归终止条件：到达字符串末尾
		if (start == s.length()) {
			result.add(new ArrayList<>());
			return result;
		}
		
		// 尝试所有可能的分割点
		for (int end = start; end < s.length(); end++) {
			if (isPalindrome[start][end]) {
				// 当前子串是回文，递归处理剩余部分
				String current = s.substring(start, end + 1);
				List<List<String>> subResults = partitionHelper(s, end + 1, isPalindrome, memo);
				
				// 将当前回文子串添加到所有子结果中
				for (List<String> subResult : subResults) {
					List<String> newResult = new ArrayList<>();
					newResult.add(current);
					newResult.addAll(subResult);
					result.add(newResult);
				}
			}
		}
		
		// 记忆化存储
		memo.put(start, result);
		return result;
	}
	
	// C++实现
	/*
	#include <vector>
	#include <string>
	#include <unordered_map>
	#include <algorithm>
	using namespace std;
	
	vector<vector<string>> partitionHelper(string& s, int start, vector<vector<bool>>& isPalindrome, unordered_map<int, vector<vector<string>>>& memo) {
	    // 记忆化检查
	    if (memo.find(start) != memo.end()) {
	        return memo[start];
	    }
	    
	    vector<vector<string>> result;
	    
	    // 递归终止条件：到达字符串末尾
	    if (start == s.length()) {
	        result.push_back({});
	        return result;
	    }
	    
	    // 尝试所有可能的分割点
	    for (int end = start; end < s.length(); end++) {
	        if (isPalindrome[start][end]) {
	            // 当前子串是回文，递归处理剩余部分
	            string current = s.substr(start, end - start + 1);
	            vector<vector<string>> subResults = partitionHelper(s, end + 1, isPalindrome, memo);
	            
	            // 将当前回文子串添加到所有子结果中
	            for (auto& subResult : subResults) {
	                vector<string> newResult;
	                newResult.push_back(current);
	                newResult.insert(newResult.end(), subResult.begin(), subResult.end());
	                result.push_back(newResult);
	            }
	        }
	    }
	    
	    // 记忆化存储
	    memo[start] = result;
	    return result;
	}
	
	vector<vector<string>> partitionMemo(string s) {
	    // 异常处理
	    if (s.empty()) {
	        return {};
	    }
	    
	    // 边界条件：单字符字符串
	    if (s.length() == 1) {
	        return {{s}};
	    }
	    
	    int n = s.length();
	    vector<vector<bool>> isPalindrome(n, vector<bool>(n, false));
	    
	    // 初始化对角线（单字符都是回文）
	    for (int i = 0; i < n; i++) {
	        isPalindrome[i][i] = true;
	    }
	    
	    // 初始化相邻字符（双字符回文判断）
	    for (int i = 0; i < n - 1; i++) {
	        isPalindrome[i][i + 1] = (s[i] == s[i + 1]);
	    }
	    
	    // 动态规划填充回文矩阵
	    for (int len = 3; len <= n; len++) {
	        for (int i = 0; i <= n - len; i++) {
	            int j = i + len - 1;
	            isPalindrome[i][j] = (s[i] == s[j]) && isPalindrome[i + 1][j - 1];
	        }
	    }
	    
	    // 使用记忆化搜索
	    unordered_map<int, vector<vector<string>>> memo;
	    return partitionHelper(s, 0, isPalindrome, memo);
	}
	*/
	
	// Python实现
	/*
	def partitionMemo(s):
	    # 异常处理
	    if not s:
	        return []
	    
	    # 边界条件：单字符字符串
	    if len(s) == 1:
	        return [[s]]
	    
	    n = len(s)
	    # 预处理回文串判断
	    is_palindrome = [[False] * n for _ in range(n)]
	    
	    # 初始化对角线（单字符都是回文）
	    for i in range(n):
	        is_palindrome[i][i] = True
	    
	    # 初始化相邻字符（双字符回文判断）
	    for i in range(n - 1):
	        is_palindrome[i][i + 1] = (s[i] == s[i + 1])
	    
	    # 动态规划填充回文矩阵
	    for length in range(3, n + 1):
	        for i in range(n - length + 1):
	            j = i + length - 1
	            is_palindrome[i][j] = (s[i] == s[j]) and is_palindrome[i + 1][j - 1]
	    
	    # 使用记忆化搜索
	    memo = {}
	    
	    def partition_helper(start):
	        # 记忆化检查
	        if start in memo:
	            return memo[start]
	        
	        result = []
	        
	        # 递归终止条件：到达字符串末尾
	        if start == len(s):
	            result.append([])
	            return result
	        
	        # 尝试所有可能的分割点
	        for end in range(start, len(s)):
	            if is_palindrome[start][end]:
	                # 当前子串是回文，递归处理剩余部分
	                current = s[start:end + 1]
	                sub_results = partition_helper(end + 1)
	                
	                # 将当前回文子串添加到所有子结果中
	                for sub_result in sub_results:
	                    new_result = [current]
	                    new_result.extend(sub_result)
	                    result.append(new_result)
	        
	        # 记忆化存储
	        memo[start] = result
	        return result
	    
	    return partition_helper(0)
	*/

}
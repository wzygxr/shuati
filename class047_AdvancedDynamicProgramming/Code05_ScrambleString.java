package class069;

/**
 * 扰乱字符串 (Scramble String) - 字符串动态规划
 * 
 * 题目描述：
 * 使用下面描述的算法可以扰乱字符串 s 得到字符串 t ：
 * 步骤1 : 如果字符串的长度为 1 ，算法停止
 * 步骤2 : 如果字符串的长度 > 1 ，执行下述步骤：
 *        在一个随机下标处将字符串分割成两个非空的子字符串
 *        已知字符串s，则可以将其分成两个子字符串x和y且满足s=x+y
 *        可以决定是要交换两个子字符串还是要保持这两个子字符串的顺序不变
 *        即s可能是 s = x + y 或者 s = y + x
 *        在x和y这两个子字符串上继续从步骤1开始递归执行此算法
 * 给你两个长度相等的字符串 s1 和 s2，判断 s2 是否是 s1 的扰乱字符串。
 * 如果是，返回true；否则，返回false。
 * 
 * 题目来源：LeetCode 87. 扰乱字符串
 * 测试链接：https://leetcode.cn/problems/scramble-string/
 * 
 * 解题思路：
 * 这是一个复杂的字符串动态规划问题，需要判断一个字符串是否可以通过扰乱操作变成另一个字符串。
 * 扰乱操作包括分割字符串和可能交换子字符串的顺序。
 * 
 * 算法实现：
 * 1. 暴力递归：尝试所有可能的分割位置和交换情况
 * 2. 记忆化搜索：使用三维数组存储中间结果（起始位置+长度）
 * 3. 动态规划：自底向上填表，处理所有可能的子串组合
 * 
 * 时间复杂度分析：
 * - 暴力递归：O(n!)，阶乘级复杂度
 * - 记忆化搜索：O(n^4)，需要检查所有可能的子串组合
 * - 动态规划：O(n^4)，四重循环
 * 
 * 空间复杂度分析：
 * - 暴力递归：O(n)，递归栈深度
 * - 记忆化搜索：O(n^3)，三维记忆化数组
 * - 动态规划：O(n^3)，三维DP表
 * 
 * 关键技巧：
 * 1. 子串字符频率检查：先检查字符频率是否相同
 * 2. 分割位置枚举：尝试所有可能的分割位置
 * 3. 交换情况考虑：考虑交换和不交换两种情况
 * 
 * 工程化考量：
 * 1. 剪枝优化：字符频率检查可以提前排除不可能的情况
 * 2. 边界条件：长度为1的字符串直接比较
 * 3. 性能优化：动态规划优于递归解法
 * 4. 代码可读性：清晰的变量命名和注释
 */
public class Code05_ScrambleString {

	public static boolean isScramble1(String str1, String str2) {
		char[] s1 = str1.toCharArray();
		char[] s2 = str2.toCharArray();
		int n = s1.length;
		return f1(s1, 0, n - 1, s2, 0, n - 1);
	}

	// s1[l1....r1]
	// s2[l2....r2]
	// 保证l1....r1与l2....r2
	// 是不是扰乱串的关系
	public static boolean f1(char[] s1, int l1, int r1, char[] s2, int l2, int r2) {
		if (l1 == r1) {
			// s1[l1..r1]
			// s2[l2..r2]
			return s1[l1] == s2[l2];
		}
		// s1[l1..i][i+1....r1]
		// s2[l2..j][j+1....r2]
		// 不交错去讨论扰乱关系
		for (int i = l1, j = l2; i < r1; i++, j++) {
			if (f1(s1, l1, i, s2, l2, j) && f1(s1, i + 1, r1, s2, j + 1, r2)) {
				return true;
			}
		}
		// 交错去讨论扰乱关系
		// s1[l1..........i][i+1...r1]
		// s2[l2...j-1][j..........r2]
		for (int i = l1, j = r2; i < r1; i++, j--) {
			if (f1(s1, l1, i, s2, j, r2) && f1(s1, i + 1, r1, s2, l2, j - 1)) {
				return true;
			}
		}
		return false;
	}

	// 依然暴力尝试，只不过四个可变参数，变成了三个
	public static boolean isScramble2(String str1, String str2) {
		char[] s1 = str1.toCharArray();
		char[] s2 = str2.toCharArray();
		int n = s1.length;
		return f2(s1, s2, 0, 0, n);
	}

	public static boolean f2(char[] s1, char[] s2, int l1, int l2, int len) {
		if (len == 1) {
			return s1[l1] == s2[l2];
		}
		// s1[l1.......]  len
		// s2[l2.......]  len
		// 左 : k个   右: len - k 个
		for (int k = 1; k < len; k++) {
			if (f2(s1, s2, l1, l2, k) && f2(s1, s2, l1 + k, l2 + k, len - k)) {
				return true;
			}
		}
		// 交错！
		for (int i = l1 + 1, j = l2 + len - 1, k = 1; k < len; i++, j--, k++) {
			if (f2(s1, s2, l1, j, k) && f2(s1, s2, i, l2, len - k)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isScramble3(String str1, String str2) {
		char[] s1 = str1.toCharArray();
		char[] s2 = str2.toCharArray();
		int n = s1.length;
		// dp[l1][l2][len] : int 0 -> 没展开过
		// dp[l1][l2][len] : int -1 -> 展开过，返回的结果是false
		// dp[l1][l2][len] : int 1 -> 展开过，返回的结果是true
		int[][][] dp = new int[n][n][n + 1];
		return f3(s1, s2, 0, 0, n, dp);
	}

	public static boolean f3(char[] s1, char[] s2, int l1, int l2, int len, int[][][] dp) {
		if (len == 1) {
			return s1[l1] == s2[l2];
		}
		if (dp[l1][l2][len] != 0) {
			return dp[l1][l2][len] == 1;
		}
		boolean ans = false;
		for (int k = 1; k < len; k++) {
			if (f3(s1, s2, l1, l2, k, dp) && f3(s1, s2, l1 + k, l2 + k, len - k, dp)) {
				ans = true;
				break;
			}
		}
		if (!ans) {
			for (int i = l1 + 1, j = l2 + len - 1, k = 1; k < len; i++, j--, k++) {
				if (f3(s1, s2, l1, j, k, dp) && f3(s1, s2, i, l2, len - k, dp)) {
					ans = true;
					break;
				}
			}
		}
		dp[l1][l2][len] = ans ? 1 : -1;
		return ans;
	}

	public static boolean isScramble4(String str1, String str2) {
		char[] s1 = str1.toCharArray();
		char[] s2 = str2.toCharArray();
		int n = s1.length;
		boolean[][][] dp = new boolean[n][n][n + 1];
		// 填写len=1层，所有的格子
		for (int l1 = 0; l1 < n; l1++) {
			for (int l2 = 0; l2 < n; l2++) {
				dp[l1][l2][1] = s1[l1] == s2[l2];
			}
		}
		for (int len = 2; len <= n; len++) {
			// 注意如下的边界条件 : l1 <= n - len l2 <= n - len
			for (int l1 = 0; l1 <= n - len; l1++) {
				for (int l2 = 0; l2 <= n - len; l2++) {
					for (int k = 1; k < len; k++) {
						if (dp[l1][l2][k] && dp[l1 + k][l2 + k][len - k]) {
							dp[l1][l2][len] = true;
							break;
						}
					}
					if (!dp[l1][l2][len]) {
						for (int i = l1 + 1, j = l2 + len - 1, k = 1; k < len; i++, j--, k++) {
							if (dp[l1][j][k] && dp[i][l2][len - k]) {
								dp[l1][l2][len] = true;
								break;
							}
						}
					}
				}
			}
		}
		return dp[0][0][n];
	}

}

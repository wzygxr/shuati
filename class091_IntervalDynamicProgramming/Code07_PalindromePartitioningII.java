package class076;

/**
 * LeetCode 132. 分割回文串 II
 * 题目链接：https://leetcode.cn/problems/palindrome-partitioning-ii/
 * 
 * 题目描述：
 * 给你一个字符串 s，请你将 s 分割成一些子串，使每个子串都是回文串。
 * 返回符合要求的最少分割次数。
 * 
 * 解题思路：
 * 这是一个区间动态规划问题，可以分为两个步骤：
 * 1. 预处理：使用动态规划计算所有子串是否为回文串
 * 2. 分割：定义状态 dp[i] 表示 s[0...i] 最少分割次数
 *    状态转移方程：dp[i] = min(dp[j-1] + 1) for all j where s[j...i] is palindrome
 * 
 * 时间复杂度：O(n²)
 * 空间复杂度：O(n²)
 * 
 * 工程化考量：
 * 1. 边界条件处理：整个字符串就是回文串时分割次数为0
 * 2. 优化：可以使用中心扩展法优化预处理步骤
 * 3. 输入验证：检查字符串是否为空
 * 
 * 相关题目扩展：
 * 1. LeetCode 132. 分割回文串 II - https://leetcode.cn/problems/palindrome-partitioning-ii/
 * 2. LeetCode 131. 分割回文串 - https://leetcode.cn/problems/palindrome-partitioning/
 * 3. LeetCode 1278. 分割回文串 III - https://leetcode.cn/problems/palindrome-partitioning-iii/
 * 4. LeetCode 1745. 回文串分割 IV - https://leetcode.cn/problems/palindrome-partitioning-iv/
 * 5. LeetCode 2168. 每个数字的频率都相同的独特子串 - https://leetcode.cn/problems/unique-substrings-with-equal-digit-frequency/
 * 6. LintCode 108. 分割回文串 II - https://www.lintcode.com/problem/108/
 * 7. LintCode 136. 分割回文串 - https://www.lintcode.com/problem/136/
 * 8. HackerRank - Sherlock and the Valid String - https://www.hackerrank.com/challenges/sherlock-and-valid-string/problem
 * 9. Codeforces 1327D - Infinite Path - https://codeforces.com/problemset/problem/1327/D
 * 10. AtCoder ABC144D - Water Bottle - https://atcoder.jp/contests/abc144/tasks/abc144_d
 */
public class Code07_PalindromePartitioningII {

	// 解法一：预处理回文串 + 动态规划
	public static int minCut1(String s) {
		char[] str = s.toCharArray();
		int n = str.length;
		
		// 预处理所有子串是否为回文串
		boolean[][] isPalin = new boolean[n][n];
		for (int i = 0; i < n; i++) {
			isPalin[i][i] = true;
		}
		for (int i = 0; i < n - 1; i++) {
			isPalin[i][i + 1] = str[i] == str[i + 1];
		}
		for (int l = n - 3; l >= 0; l--) {
			for (int r = l + 2; r < n; r++) {
				isPalin[l][r] = str[l] == str[r] && isPalin[l + 1][r - 1];
			}
		}
		
		// dp[i] 表示 str[0...i] 最少分割次数
		int[] dp = new int[n];
		for (int i = 0; i < n; i++) {
			if (isPalin[0][i]) {
				dp[i] = 0;
			} else {
				dp[i] = i; // 最多分割 i 次
				for (int j = 1; j <= i; j++) {
					if (isPalin[j][i]) {
						dp[i] = Math.min(dp[i], dp[j - 1] + 1);
					}
				}
			}
		}
		return dp[n - 1];
	}
	
	// 解法二：中心扩展法 + 动态规划
	public static int minCut2(String s) {
		char[] str = s.toCharArray();
		int n = str.length;
		
		// dp[i] 表示 str[0...i] 最少分割次数
		int[] dp = new int[n];
		for (int i = 0; i < n; i++) {
			dp[i] = i; // 最多分割 i 次
		}
		
		// 中心扩展法检查回文串
		for (int i = 0; i < n; i++) {
			// 奇数长度回文串
			for (int l = i, r = i; l >= 0 && r < n && str[l] == str[r]; l--, r++) {
				if (l == 0) {
					dp[r] = 0;
				} else {
					dp[r] = Math.min(dp[r], dp[l - 1] + 1);
				}
			}
			
			// 偶数长度回文串
			for (int l = i, r = i + 1; l >= 0 && r < n && str[l] == str[r]; l--, r++) {
				if (l == 0) {
					dp[r] = 0;
				} else {
					dp[r] = Math.min(dp[r], dp[l - 1] + 1);
				}
			}
		}
		
		return dp[n - 1];
	}
	
	// 解法三：Manacher算法 + 动态规划
	public static int minCut3(String s) {
		char[] str = s.toCharArray();
		int n = str.length;
		
		// Manacher算法预处理
		char[] chs = new char[(n << 1) | 1];
		for (int i = 0; i < chs.length; i++) {
			chs[i] = (i & 1) == 0 ? '#' : str[i >> 1];
		}
		
		int[] pArr = new int[chs.length];
		int C = -1, R = -1;
		int[] dp = new int[n];
		
		for (int i = 0; i < n; i++) {
			dp[i] = i;
		}
		
		for (int i = 0; i < chs.length; i++) {
			pArr[i] = R > i ? Math.min(pArr[(C << 1) - i], R - i) : 1;
			while (i + pArr[i] < chs.length && i - pArr[i] > -1) {
				if (chs[i + pArr[i]] == chs[i - pArr[i]]) {
					pArr[i]++;
				} else {
					break;
				}
			}
			if (i + pArr[i] > R) {
				R = i + pArr[i];
				C = i;
			}
			
			// 更新dp数组
			int left = (i >> 1) - ((pArr[i] - 1) >> 1);
			int right = (i >> 1) + ((pArr[i] - 1) >> 1);
			if (left == 0) {
				dp[right] = 0;
			} else {
				dp[right] = Math.min(dp[right], dp[left - 1] + 1);
			}
		}
		
		return dp[n - 1];
	}
	
	// 测试函数
	public static void main(String[] args) {
		String s1 = "aab";
		System.out.println("字符串: " + s1);
		System.out.println("最少分割次数 (解法一): " + minCut1(s1));
		System.out.println("最少分割次数 (解法二): " + minCut2(s1));
		System.out.println("最少分割次数 (解法三): " + minCut3(s1));
		
		String s2 = "raceacar";
		System.out.println("\n字符串: " + s2);
		System.out.println("最少分割次数 (解法一): " + minCut1(s2));
		System.out.println("最少分割次数 (解法二): " + minCut2(s2));
		System.out.println("最少分割次数 (解法三): " + minCut3(s2));
	}

}
package class084;

// 找到所有好字符串
// 给你两个长度为 n 的字符串 s1 和 s2，以及一个字符串 evil。请你返回好字符串的数目。
// 好字符串的定义是：它的长度为 n，字典序大于等于 s1，字典序小于等于 s2，且不包含 evil 为子字符串。
// 由于答案可能很大，返回答案对 10^9 + 7 取余的结果。
// 测试链接 : https://leetcode.cn/problems/find-all-good-strings/
public class Code08_FindAllGoodStrings {

	public static int MOD = 1000000007;

	/**
	 * 数位DP + KMP解法
	 * 时间复杂度: O(n * m * 2 * 2 * 26) 其中n是字符串长度，m是evil字符串长度
	 * 空间复杂度: O(n * m * 2 * 2)
	 * 
	 * 解题思路:
	 * 1. 使用数位DP框架，逐位确定字符
	 * 2. 结合KMP算法避免包含evil字符串
	 * 3. 状态需要记录：
	 *    - 当前处理到第几位
	 *    - 是否受到上下界限制
	 *    - 当前已匹配evil字符串的前缀长度（使用KMP的next数组）
	 * 4. 通过记忆化搜索避免重复计算
	 * 
	 * 最优解分析:
	 * 该解法结合了数位DP和KMP算法，是解决此类问题的最优通用方法。
	 */
	public static int findGoodStrings(int n, String s1, String s2, String evil) {
		// 答案为[0, s2]中的好字符串个数减去[0, s1)中的好字符串个数，再加上s1本身是否是好字符串
		char[] cs1 = s1.toCharArray();
		char[] cs2 = s2.toCharArray();
		char[] cevil = evil.toCharArray();
		
		// 计算KMP的next数组
		int[] next = getNext(cevil);
		
		// 计算[0, s2]中的好字符串个数
		int[][][] dp2 = new int[n][2][evil.length()];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < 2; j++) {
				for (int k = 0; k < evil.length(); k++) {
					dp2[i][j][k] = -1;
				}
			}
		}
		int count2 = dfs(cs2, cevil, next, 0, true, 0, dp2);
		
		// 计算[0, s1)中的好字符串个数
		int[][][] dp1 = new int[n][2][evil.length()];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < 2; j++) {
				for (int k = 0; k < evil.length(); k++) {
					dp1[i][j][k] = -1;
				}
			}
		}
		int count1 = dfs(cs1, cevil, next, 0, true, 0, dp1);
		
		// 检查s1本身是否是好字符串
		int s1IsGood = (s1.indexOf(evil) == -1) ? 1 : 0;
		
		// 返回结果
		return (int) (((long) count2 - count1 + s1IsGood) % MOD + MOD) % MOD;
	}
	
	// 数位DP递归函数
	private static int dfs(char[] s, char[] evil, int[] next, int pos, boolean isLimit, int matchLen, int[][][] dp) {
		// 递归终止条件
		if (pos == s.length) {
			return 1;
		}
		
		// 记忆化搜索
		if (!isLimit && dp[pos][0][matchLen] != -1) {
			return dp[pos][0][matchLen];
		}
		
		int ans = 0;
		
		// 确定当前位可以填入的字符范围
		char up = isLimit ? s[pos] : 'z';
		
		// 枚举当前位可以填入的字符
		for (char c = 'a'; c <= up; c++) {
			// 使用KMP算法计算填入字符c后匹配evil的长度
			int newMatchLen = matchLen;
			while (newMatchLen > 0 && evil[newMatchLen] != c) {
				newMatchLen = next[newMatchLen];
			}
			if (evil[newMatchLen] == c) {
				newMatchLen++;
			}
			
			// 如果已经完全匹配evil，则不能填入这个字符
			if (newMatchLen < evil.length) {
				// 递归处理下一位
				ans = (ans + dfs(s, evil, next, pos + 1, isLimit && c == up, newMatchLen, dp)) % MOD;
			}
		}
		
		// 记忆化存储
		if (!isLimit) {
			dp[pos][0][matchLen] = ans;
		}
		
		return ans;
	}
	
	// 计算KMP的next数组
	private static int[] getNext(char[] pattern) {
		int n = pattern.length;
		int[] next = new int[n + 1];
		next[0] = -1;
		int i = 0, j = -1;
		
		while (i < n) {
			if (j == -1 || pattern[i] == pattern[j]) {
				i++;
				j++;
				next[i] = j;
			} else {
				j = next[j];
			}
		}
		
		return next;
	}

	// 测试方法
	public static void main(String[] args) {
		// 测试用例1
		int n1 = 2;
		String s11 = "aa", s21 = "da", evil1 = "b";
		System.out.println("n = " + n1 + ", s1 = \"" + s11 + "\", s2 = \"" + s21 + "\", evil = \"" + evil1 + "\"");
		System.out.println("好字符串的数目: " + findGoodStrings(n1, s11, s21, evil1));
		// 预期输出: 51
		
		// 测试用例2
		int n2 = 8;
		String s12 = "leetcode", s22 = "leetgoes", evil2 = "leet";
		System.out.println("n = " + n2 + ", s1 = \"" + s12 + "\", s2 = \"" + s22 + "\", evil = \"" + evil2 + "\"");
		System.out.println("好字符串的数目: " + findGoodStrings(n2, s12, s22, evil2));
		// 预期输出: 0
		
		// 测试用例3
		int n3 = 2;
		String s13 = "gx", s23 = "gz", evil3 = "x";
		System.out.println("n = " + n3 + ", s1 = \"" + s13 + "\", s2 = \"" + s23 + "\", evil = \"" + evil3 + "\"");
		System.out.println("好字符串的数目: " + findGoodStrings(n3, s13, s23, evil3));
		// 预期输出: 2
	}
}
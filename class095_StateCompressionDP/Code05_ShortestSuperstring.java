package class080;

// 最短超级串
// 给定一个字符串数组 words ，找到以 words 中每个字符串作为子字符串的最短字符串。
// 如果有多个有效最短字符串满足题目条件，返回其中 任意一个 即可。
// 我们可以假设 words 中没有字符串是 words 中另一个字符串的子字符串。
// 测试链接 : https://leetcode.cn/problems/find-the-shortest-superstring/
public class Code05_ShortestSuperstring {

	// 状态压缩动态规划解法
	// 这是解决最短超级串问题的经典状压DP方法
	// 时间复杂度: O(n^2 * 2^n + n * sum(len))
	// 空间复杂度: O(n * 2^n)
	// 其中n是字符串的数量，sum(len)是所有字符串长度之和
	public static String shortestSuperstring(String[] words) {
		int n = words.length;
		
		// 预处理计算重叠部分
		// overlap[i][j] 表示字符串words[i]的尾部与字符串words[j]的头部的最大重叠长度
		int[][] overlap = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i != j) {
					overlap[i][j] = getOverlap(words[i], words[j]);
				}
			}
		}
		
		// dp[mask][i] 表示使用mask代表的字符串集合，且最后一个字符串是words[i]时的最短超级字符串
		String[][] dp = new String[1 << n][n];
		
		// 初始化：只包含一个字符串的情况
		for (int i = 0; i < n; i++) {
			dp[1 << i][i] = words[i];
		}
		
		// 状态转移
		// 枚举所有可能的状态
		for (int mask = 1; mask < (1 << n); mask++) {
			// 枚举当前状态的最后一个字符串
			for (int last = 0; last < n; last++) {
				// 如果last字符串不在当前状态中，跳过
				if ((mask & (1 << last)) == 0) {
					continue;
				}
				
				// 如果当前状态不合法(还没有计算过)，跳过
				if (dp[mask][last] == null) {
					continue;
				}
				
				// 枚举下一个要添加的字符串
				for (int next = 0; next < n; next++) {
					// 如果next字符串已经在当前状态中，跳过
					if ((mask & (1 << next)) != 0) {
						continue;
					}
					
					// 新的状态
					int newMask = mask | (1 << next);
					// 新的超级字符串：当前字符串 + next字符串的非重叠部分
					String newString = dp[mask][last] + words[next].substring(overlap[last][next]);
					
					// 更新dp状态
					if (dp[newMask][next] == null || dp[newMask][next].length() > newString.length()) {
						dp[newMask][next] = newString;
					}
				}
			}
		}
		
		// 找到包含所有字符串的最短超级字符串
		String result = null;
		for (int i = 0; i < n; i++) {
			if (result == null || dp[(1 << n) - 1][i].length() < result.length()) {
				result = dp[(1 << n) - 1][i];
			}
		}
		
		return result;
	}
	
	// 计算字符串a的尾部与字符串b的头部的最大重叠长度
	// 例如：a="abc", b="bcd"，重叠部分为"bc"，返回2
	private static int getOverlap(String a, String b) {
		// 重叠长度最大为两个字符串长度的较小值
		for (int i = Math.min(a.length(), b.length()); i >= 0; i--) {
			// 检查a的后i个字符是否与b的前i个字符相同
			if (a.substring(a.length() - i).equals(b.substring(0, i))) {
				return i;
			}
		}
		return 0;
	}

}
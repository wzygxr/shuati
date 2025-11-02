package class104;


// 分割回文串
// 给你一个字符串 s，请你将 s 分割成一些子串，使每个子串都是回文串。
// 返回符合要求的 最少分割次数
// 测试链接 : https://leetcode.cn/problems/palindrome-partitioning-ii/
public class Code06_PalindromePartitioning {

	/**
	 * 使用Manacher算法优化的解法
	 * 
	 * 算法思路：
	 * 1. 首先使用Manacher算法计算所有位置的回文半径
	 * 2. 根据回文半径信息构建预处理数组，记录每个区间是否为回文
	 * 3. 使用动态规划计算最少分割次数
	 * 
	 * 时间复杂度：O(n^2)，其中n为字符串长度
	 * 空间复杂度：O(n^2)，用于存储回文信息和DP数组
	 * 
	 * 相比暴力解法的优势：
	 * 1. 使用Manacher算法预处理回文信息，避免重复计算
	 * 2. DP过程中直接查表判断回文，提高效率
	 */
	public static int minCut(String s) {
		if (s == null || s.length() < 2) {
			return 0;
		}
		
		// 使用Manacher算法获取回文信息
		manacher(s);
		
		int n = s.length();
		// isPalindrome[i][j] 表示 s[i..j] 是否为回文
		boolean[][] isPalindrome = new boolean[n][n];
		
		// 根据Manacher算法的结果填充回文判断表
		// 遍历扩展字符串中的每个位置
		for (int i = 0; i < 2 * n + 1; i++) {
			// 获取以位置i为中心的回文半径
			int radius = p[i];
			// 计算在原字符串中的实际中心位置和半径
			int center = i / 2;
			int actualRadius = (radius - 1) / 2;
			
			// 根据中心位置的奇偶性分别处理
			if (i % 2 == 0) {
				// 偶数位置对应原字符串字符之间的位置
				// 处理偶数长度回文
				for (int r = 0; r <= actualRadius; r++) {
					int left = center - r;
					int right = center + r - 1;
					if (left >= 0 && right < n) {
						isPalindrome[left][right] = true;
					}
				}
			} else {
				// 奇数位置对应原字符串中的字符位置
				// 处理奇数长度回文
				for (int r = 0; r <= actualRadius; r++) {
					int left = center - r;
					int right = center + r;
					if (left >= 0 && right < n) {
						isPalindrome[left][right] = true;
					}
				}
			}
		}
		
		// 动态规划计算最少分割次数
		// dp[i] 表示 s[0..i-1] 的最少分割次数
		int[] dp = new int[n + 1];
		for (int i = 1; i <= n; i++) {
			// 初始化为最多分割次数（每个字符分割）
			dp[i] = i - 1;
			// 尝试所有可能的最后一条分割线
			for (int j = 0; j < i; j++) {
				// 如果 s[j..i-1] 是回文，则可以在此处分割
				if (isPalindrome[j][i - 1]) {
					if (j == 0) {
						// 如果从头开始就是回文，不需要分割
						dp[i] = 0;
					} else {
						// 否则分割次数为前面部分的分割次数+1
						dp[i] = Math.min(dp[i], dp[j] + 1);
					}
				}
			}
		}
		
		return dp[n];
	}
	
	// 以下为Manacher算法的标准实现
	
	public static int MAXN = 2001;

	public static char[] ss = new char[MAXN << 1];

	public static int[] p = new int[MAXN << 1];

	public static int n;

	public static void manacher(String str) {
		manacherss(str.toCharArray());
		for (int i = 0, c = 0, r = 0, len; i < n; i++) {
			len = r > i ? Math.min(p[2 * c - i], r - i) : 1;
			while (i + len < n && i - len >= 0 && ss[i + len] == ss[i - len]) {
				len++;
			}
			if (i + len > r) {
				r = i + len;
				c = i;
			}
			p[i] = len;
		}
	}

	public static void manacherss(char[] a) {
		n = a.length * 2 + 1;
		for (int i = 0, j = 0; i < n; i++) {
			ss[i] = (i & 1) == 0 ? '#' : a[j++];
		}
	}
}
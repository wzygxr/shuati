package class104;

// 最短回文串
// 给定一个字符串 s，你可以通过在字符串前面添加字符将其转换为回文串。
// 找到并返回可以用这种方式转换的最短回文串。
// 测试链接 : https://leetcode.cn/problems/shortest-palindrome/
public class Code07_ShortestPalindrome {

	/**
	 * 使用Manacher算法解决最短回文串问题
	 * 
	 * 算法思路：
	 * 1. 将原字符串s与其反转字符串reverse组合成新字符串s + "#" + reverse
	 * 2. 在新字符串上运行Manacher算法，找出包含s开头的最长回文
	 * 3. 根据这个最长回文确定需要在前面添加的最少字符数
	 * 
	 * 时间复杂度：O(n)，其中n为字符串长度
	 * 空间复杂度：O(n)
	 * 
	 * 相比KMP算法的优势：
	 * 1. 更加直观，直接利用回文的性质
	 * 2. 一次Manacher算法即可解决问题
	 */
	public static String shortestPalindrome(String s) {
		if (s == null || s.length() <= 1) {
			return s;
		}
		
		// 将原字符串反转
		String reverse = new StringBuilder(s).reverse().toString();
		
		// 构造新字符串：原字符串 + 分隔符 + 反转字符串
		// 使用特殊字符'#'作为分隔符，确保不会产生虚假的回文匹配
		String combined = s + "#" + reverse;
		
		// 使用Manacher算法计算新字符串中每个位置的回文半径
		manacher(combined);
		
		// 找到包含原字符串开头的最长回文
		// 在combined字符串中，原字符串s占据位置[0, n-1]
		// 我们需要找到以位置0为起点的最长回文
		int n = s.length();
		int maxLen = 0;
		
		// 遍历所有可能的回文中心
		for (int i = 0; i < combined.length(); i++) {
			// 计算回文的起始位置
			int start = i - (p[i] - 1);
			
			// 如果回文包含位置0，则是一个候选解
			if (start <= 0) {
				// 计算这个回文在原字符串中的长度
				int lenInOriginal = p[i] - 1 - (-start);
				maxLen = Math.max(maxLen, lenInOriginal);
			}
		}
		
		// 获取需要添加到前面的后缀
		String suffix = s.substring(maxLen);
		
		// 将后缀反转并添加到原字符串前面
		return new StringBuilder(suffix).reverse().toString() + s;
	}
	
	/**
	 * Manacher算法主函数
	 * 
	 * 算法原理：
	 * 1. 预处理：在原字符串的每个字符之间插入特殊字符'#'
	 * 2. 利用回文串的对称性，避免重复计算
	 * 3. 维护当前最右回文边界r和对应的中心c，通过已计算的信息加速新位置的计算
	 * 
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(n)
	 */
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
	
	// Manacher算法相关变量
	
	public static int MAXN = 100001;

	public static char[] ss = new char[MAXN << 1];

	public static int[] p = new int[MAXN << 1];

	public static int n;
	
	/**
	 * 预处理函数，用于在字符间插入'#'
	 */
	public static void manacherss(char[] a) {
		n = a.length * 2 + 1;
		for (int i = 0, j = 0; i < n; i++) {
			ss[i] = (i & 1) == 0 ? '#' : a[j++];
		}
	}
}
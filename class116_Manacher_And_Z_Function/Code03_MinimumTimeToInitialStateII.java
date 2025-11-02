package class103;

// 将单词恢复初始状态所需的最短时间II
// 给你一个下标从0开始的字符串word和一个整数k
// 在每一秒，必须执行以下操作
// 移除word的前k个字符
// 在word的末尾添加k个任意字符
// 添加的字符不必和移除的字符相同
// 返回将word恢复到初始状态所需的最短时间
// 该时间必须大于零
// 测试链接 : https://leetcode.cn/problems/minimum-time-to-revert-word-to-initial-state-ii/
public class Code03_MinimumTimeToInitialStateII {

	/**
	 * 使用Z函数解决将单词恢复初始状态所需的最短时间问题
	 * 
	 * 算法思路：
	 * 1. 每次操作移除前k个字符，相当于在字符串上以步长k向前移动
	 * 2. 我们需要找到最小的移动次数，使得剩余的后缀能够通过添加字符恢复为原字符串
	 * 3. 这等价于找到最小的i（i是k的倍数），使得word.substring(i)与word的最长公共前缀
	 *    等于word.substring(i)的长度
	 * 4. 使用Z函数可以高效计算每个后缀与原字符串的最长公共前缀长度
	 * 
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(n)
	 * 
	 * @param word 输入字符串
	 * @param k 每次操作移除/添加的字符数
	 * @return 恢复初始状态所需的最短时间
	 */
	public static int minimumTimeToInitialState(String word, int k) {
		char[] s = word.toCharArray();
		int n = s.length;
		zArray(s, n);
		for (int i = k; i < n; i += k) {
			// 如果从位置i开始的后缀与原字符串的最长公共前缀长度等于后缀长度
			// 说明在第(i/k)步后可以恢复原字符串
			if (z[i] == n - i) {
				return i / k;
			}
		}
		// 最坏情况需要完全替换
		return (n + k - 1) / k;
	}

	// leetcode增加了数据量
	// 所以把这个值改成10^6规模
	public static int MAXN = 1000001;

	public static int[] z = new int[MAXN];

	/**
	 * Z函数计算
	 * Z函数z[i]表示字符串s从位置i开始与字符串s从位置0开始的最长公共前缀长度
	 * 
	 * 算法原理：
	 * 1. 维护一个匹配区间[l, r]，表示当前已知的最右匹配区间
	 * 2. 对于当前位置i，如果i <= r，可以利用已计算的信息优化
	 * 3. 利用对称性，z[i]至少为min(r - i + 1, z[i - l])
	 * 4. 在此基础之上继续向右扩展匹配
	 * 
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(n)
	 */
	public static void zArray(char[] s, int n) {
		z[0] = n;
		// l: 当前最右匹配区间的左边界
		// r: 当前最右匹配区间的右边界
		// len: 当前位置的Z值（最长公共前缀长度）
		for (int i = 1, c = 1, r = 1, len; i < n; i++) {
			// 利用已计算的信息优化
			// 如果i在当前匹配区间内
			len = r > i ? Math.min(r - i, z[i - c]) : 0;
			// 继续向右扩展匹配
			while (i + len < n && s[i + len] == s[len]) {
				len++;
			}
			// 更新最右匹配区间
			if (i + len > r) {
				r = i + len;
				c = i;
			}
			z[i] = len;
		}
	}
	
	/**
	 * LeetCode 2223. 构造字符串的总得分和（使用相同的Z函数）
	 * 你需要从空字符串开始构造一个长度为n的字符串s，构造过程为每次给当前字符串前面添加一个字符。
	 * 构造过程中得到的所有字符串编号为1到n，其中长度为i的字符串编号为si。
	 * si的得分为si和sn的最长公共前缀的长度（注意s == sn）。
	 * 请你返回每一个si的得分之和。
	 * 
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(n)
	 */
	public static long sumScores(String word) {
		char[] s = word.toCharArray();
		int n = s.length;
		
		// 复用zArray方法计算Z函数
		zArray(s, n);
		
		// 计算得分总和
		long sum = 0;
		for (int i = 0; i < n; i++) {
			// 每个si的得分就是z[i]
			sum += z[i];
		}
		
		return sum;
	}
	
	/**
	 * 洛谷 P5410 【模板】扩展 KMP（Z 函数）
	 * 题目描述：给定两个字符串 a,b，求：
	 * 1. b 与 b 每一个后缀串的最长公共前缀长度（即 b 的 Z 函数）
	 * 2. a 与 b 每一个后缀串的最长公共前缀长度（即扩展 KMP）
	 * 
	 * 输入格式：
	 * 第一行输入一个字符串 a
	 * 第二行输入一个字符串 b
	 * 
	 * 输出格式：
	 * 第一行输出 b 的 Z 函数的异或和
	 * 第二行输出 a 与 b 的扩展 KMP 的异或和
	 * 
	 * 时间复杂度: O(n + m)
	 * 空间复杂度: O(n + m)
	 */
	public static long[] extendedKMP(String a, String b) {
		char[] aChars = a.toCharArray();
		char[] bChars = b.toCharArray();
		int n = a.length();
		int m = b.length();
		
		// 计算b的Z函数
		zArray(bChars, m);
		
		// 计算a与b的扩展KMP
		int[] e = new int[n];
		for (int i = 0, c = 0, r = 0, len; i < n; i++) {
			// 利用已计算的信息优化
			len = r > i ? Math.min(r - i, z[i - c]) : 0;
			// 继续向右扩展匹配
			while (i + len < n && len < m && aChars[i + len] == bChars[len]) {
				len++;
			}
			// 更新最右匹配区间
			if (i + len > r) {
				r = i + len;
				c = i;
			}
			e[i] = len;
		}
		
		// 计算异或和
		long zXor = 0;
		for (int i = 0; i < m; i++) {
			zXor ^= (long) (i + 1) * (z[i] + 1);
		}
		
		long eXor = 0;
		for (int i = 0; i < n; i++) {
			eXor ^= (long) (i + 1) * (e[i] + 1);
		}
		
		return new long[]{zXor, eXor};
	}
}
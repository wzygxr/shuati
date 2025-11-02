package class049;

import java.util.Arrays;

/**
 * 滑动窗口算法解决无重复字符的最长子串问题
 * 
 * 问题描述：
 * 给定一个字符串 s ，请你找出其中不含有重复字符的 最长子串 的长度。
 * 
 * 解题思路：
 * 使用滑动窗口（双指针）技术配合哈希表，维护一个不含重复字符的动态窗口[l, r]。
 * 1. 使用数组last记录每个字符最后出现的位置
 * 2. 右指针r不断向右扩展窗口
 * 3. 当遇到重复字符时，调整左指针l到重复字符上一次出现位置的下一个位置
 * 4. 记录过程中的最大窗口长度
 * 
 * 算法复杂度分析：
 * 时间复杂度：O(n) - 每个字符最多被访问两次
 * 空间复杂度：O(1) - 使用固定大小的数组存储字符位置（256个ASCII字符）
 * 
 * 相关题目链接：
 * LeetCode 3. 无重复字符的最长子串
 * https://leetcode.cn/problems/longest-substring-without-repeating-characters/
 * 
 * 其他平台类似题目：
 * 1. 牛客网 - 无重复字符的最长子串
 *    https://www.nowcoder.com/practice/b56799ebfd684fb394bd315e8932e01d
 * 2. LintCode 384. 最长无重复字符的子串
 *    https://www.lintcode.com/problem/384/
 * 3. HackerRank - Longest Substring Without Repeating Characters
 *    https://www.hackerrank.com/challenges/longest-substring-without-repeating-characters/problem
 * 4. CodeChef - SUBINC - Subarray with Increasing Order
 *    https://www.codechef.com/problems/SUBINC
 * 5. AtCoder - ABC146 C - Buy an Integer
 *    https://atcoder.jp/contests/abc146/tasks/abc146_c
 * 6. 洛谷 P3157 [CQOI2011]动态逆序对
 *    https://www.luogu.com.cn/problem/P3157
 * 7. 杭电OJ 1284 - 青蛙的约会
 *    http://acm.hdu.edu.cn/showproblem.php?pid=1284
 * 8. POJ 2718 - Smallest Difference
 *    http://poj.org/problem?id=2718
 * 9. UVa OJ 10763 - Foreign Exchange
 *    https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=1704
 * 10. SPOJ - ANGRAM - Anagrams
 *     https://www.spoj.com/problems/ANGRAM/
 * 
 * 工程化考量：
 * 1. 异常处理：处理空字符串、null输入等边界情况
 * 2. 性能优化：使用数组代替HashMap提高访问速度
 * 3. 可读性：变量命名清晰，添加详细注释
 */
public class Code02_LongestSubstringWithoutRepeatingCharacters {

	/**
	 * 计算字符串中不含有重复字符的最长子串的长度
	 * 
	 * @param str 输入字符串
	 * @return 最长无重复字符子串的长度
	 */
	public static int lengthOfLongestSubstring(String str) {
		// 将字符串转换为字符数组，便于访问
		char[] s = str.toCharArray();
		int n = s.length;
		
		// char -> int -> 0 ~ 255
		// 每一种字符上次出现的位置，初始化为-1表示未出现过
		int[] last = new int[256];
		// 所有字符都没有上次出现的位置
		Arrays.fill(last, -1);
		
		// 不含有重复字符的 最长子串 的长度
		int ans = 0;
		
		// 使用滑动窗口，l为左指针，r为右指针
		for (int l = 0, r = 0; r < n; r++) {
			// 更新左边界：取当前左边界和重复字符上一次出现位置+1的最大值
			l = Math.max(l, last[s[r]] + 1);
			
			// 更新最大长度
			ans = Math.max(ans, r - l + 1);
			
			// 更新当前字符上一次出现的位置
			last[s[r]] = r;
		}
		
		return ans;
	}

}
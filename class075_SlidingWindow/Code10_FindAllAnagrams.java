package class049;

import java.util.*;

/**
 * 找到字符串中所有字母异位词问题解决方案
 * 
 * 问题描述：
 * 给定两个字符串 s 和 p，找到 s 中所有 p 的 异位词 的子串，返回这些子串的起始索引。
 * 异位词 指由相同字母重排列形成的字符串（包括相同的字符串）。
 * 
 * 解题思路：
 * 使用滑动窗口算法找到s中所有p的异位词：
 * 1. 统计p中各字符的频次
 * 2. 维护一个长度为p.length()的滑动窗口遍历s
 * 3. 当窗口内字符频次与p完全匹配时，说明找到了一个异位词
 * 
 * 算法复杂度分析：
 * 时间复杂度: O(n) - n为s的长度
 * 空间复杂度: O(1) - 只需要26个字母的统计数组
 * 
 * 是否最优解: 是
 * 
 * 相关题目链接：
 * LeetCode 438. 找到字符串中所有字母异位词
 * https://leetcode.cn/problems/find-all-anagrams-in-a-string/
 * 
 * 其他平台类似题目：
 * 1. 牛客网 - 找到字符串中所有字母异位词
 *    https://www.nowcoder.com/practice/432531b6fc7b483096e5f9170c862a49
 * 2. LintCode 647. 回文子串
 *    https://www.lintcode.com/problem/647/
 * 3. HackerRank - Find All Anagrams in a String
 *    https://www.hackerrank.com/challenges/find-all-anagrams-in-a-string/problem
 * 4. CodeChef - ANAGRAMS - Anagrams
 *    https://www.codechef.com/problems/ANAGRAMS
 * 5. AtCoder - ABC146 D - Enough Array
 *    https://atcoder.jp/contests/abc146/tasks/abc146_d
 * 6. 洛谷 P1886 滑动窗口
 *    https://www.luogu.com.cn/problem/P1886
 * 7. 杭电OJ 4193 Sliding Window
 *    http://acm.hdu.edu.cn/showproblem.php?pid=4193
 * 8. POJ 2823 Sliding Window
 *    http://poj.org/problem?id=2823
 * 9. UVa OJ 11536 - Smallest Sub-Array
 *    https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=2531
 * 10. SPOJ - ADAFRIEN - Ada and Friends
 *     https://www.spoj.com/problems/ADAFRIEN/
 * 
 * 工程化考量：
 * 1. 异常处理：处理空字符串、s长度小于p等边界情况
 * 2. 性能优化：使用滑动窗口避免重复计算，达到线性时间复杂度
 * 3. 可读性：变量命名清晰，添加详细注释，提供测试用例
 */
public class Code10_FindAllAnagrams {

	/**
	 * 找到s中所有p的异位词的起始索引
	 * 
	 * @param s 源字符串
	 * @param p 目标字符串
	 * @return 所有异位词的起始索引列表
	 */
	public static List<Integer> findAnagrams(String s, String p) {
		// 初始化结果列表
		List<Integer> result = new ArrayList<>();
		
		// 异常情况处理：如果s长度小于p，不可能包含p的异位词
		if (s.length() < p.length()) {
			return result;
		}
		
		// 统计p中各字符的频次
		int[] count = new int[26];
		for (char c : p.toCharArray()) {
			count[c - 'a']++;
		}
		
		int windowLen = p.length();
		char[] str = s.toCharArray();
		
		// 滑动窗口遍历s
		// l为左指针，r为右指针，diff为当前窗口与p的字符差异计数
		for (int l = 0, r = 0, diff = p.length(); r < s.length(); r++) {
			// 右边界字符进入窗口
			// 如果该字符在p中存在（count[str[r]-'a'] > 0），则减少差异计数
			if (count[str[r] - 'a']-- > 0) {
				// 如果是有效字符，减少差异计数
				diff--;
			}
			
			// 当窗口大小超过p长度时，左边界字符离开窗口
			// 此时需要移除窗口左边的字符
			if (r >= windowLen) {
				// 如果移除的字符在p中存在（count[str[l]-'a'] >= 0），则增加差异计数
				if (count[str[l] - 'a']++ >= 0) {
					// 如果是有效字符，增加差异计数
					diff++;
				}
				// 移动左指针
				l++;
			}
			
			// 如果没有差异，说明当前窗口内的字符与p的字符完全匹配，即找到了一个异位词
			if (diff == 0) {
				result.add(l);
			}
		}
		
		return result;
	}
	
	/**
	 * 测试用例
	 */
	public static void main(String[] args) {
		// 测试用例1
		String s1 = "cbaebabacd";
		String p1 = "abc";
		List<Integer> result1 = findAnagrams(s1, p1);
		System.out.println("s: " + s1 + ", p: " + p1);
		System.out.println("异位词起始索引: " + result1);
		// 预期输出: [0, 6]
		
		// 测试用例2
		String s2 = "abab";
		String p2 = "ab";
		List<Integer> result2 = findAnagrams(s2, p2);
		System.out.println("\ns: " + s2 + ", p: " + p2);
		System.out.println("异位词起始索引: " + result2);
		// 预期输出: [0, 1, 2]
		
		// 测试用例3：s长度小于p
		String s3 = "ab";
		String p3 = "abc";
		List<Integer> result3 = findAnagrams(s3, p3);
		System.out.println("\ns: " + s3 + ", p: " + p3);
		System.out.println("异位词起始索引: " + result3);
		// 预期输出: []
		
		// 测试用例4：完全匹配
		String s4 = "abcabc";
		String p4 = "abc";
		List<Integer> result4 = findAnagrams(s4, p4);
		System.out.println("\ns: " + s4 + ", p: " + p4);
		System.out.println("异位词起始索引: " + result4);
		// 预期输出: [0, 1, 2, 3]
	}
}
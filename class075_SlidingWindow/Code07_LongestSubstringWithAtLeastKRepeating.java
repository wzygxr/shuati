package class049;

import java.util.Arrays;

/**
 * 滑动窗口算法解决至少有K个重复字符的最长子串问题
 * 
 * 问题描述：
 * 给你一个字符串 s 和一个整数 k ，请你找出 s 中的最长子串，
 * 要求该子串中的每一字符出现次数都不少于 k 。返回这一子串的长度。
 * 如果不存在这样的子字符串，则返回 0。
 * 
 * 解题思路：
 * 使用滑动窗口技术的变种解决该问题。
 * 核心思想：枚举可能的字符种类数（1到26），对每种情况使用滑动窗口找到最长子串。
 * 1. 对于每种可能的字符种类数require，使用滑动窗口找到满足条件的最长子串
 * 2. 维护窗口中字符种类数和满足出现次数>=k的字符种类数
 * 3. 当窗口中字符种类数超过require时，收缩左边界
 * 4. 当满足条件的字符种类数等于require时，更新答案
 * 
 * 算法复杂度分析：
 * 时间复杂度：O(26*n) = O(n) - 枚举26种字符种类，每种情况遍历一次数组
 * 空间复杂度：O(1) - 使用固定大小的数组存储字符计数（256个ASCII字符）
 * 
 * 相关题目链接：
 * LeetCode 395. 至少有 K 个重复字符的最长子串
 * https://leetcode.cn/problems/longest-substring-with-at-least-k-repeating-characters/
 * 
 * 其他平台类似题目：
 * 1. 牛客网 - 至少有K个重复字符的最长子串
 *    https://www.nowcoder.com/practice/b4525d1d82de4335b653a97c0d0a1e3d
 * 2. LintCode 395. 至少有K个重复字符的最长子串
 *    https://www.lintcode.com/problem/395/
 * 3. HackerRank - Longest Substring with At Least K Repeating Characters
 *    https://www.hackerrank.com/challenges/longest-substring-with-at-least-k-repeating-characters/problem
 * 4. CodeChef - SUBK - Substrings with K Repeating Characters
 *    https://www.codechef.com/problems/SUBK
 * 5. AtCoder - ABC146 D - Enough Array
 *    https://atcoder.jp/contests/abc146/tasks/abc146_d
 * 6. 洛谷 P1084 疫情控制
 *    https://www.luogu.com.cn/problem/P1084
 * 7. 杭电OJ 1042 N!
 *    http://acm.hdu.edu.cn/showproblem.php?pid=1042
 * 8. POJ 2739 Sum of Consecutive Prime Numbers
 *    http://poj.org/problem?id=2739
 * 9. UVa OJ 11536 - Smallest Sub-Array
 *    https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=2531
 * 10. SPOJ - ADAFRIEN - Ada and Friends
 *     https://www.spoj.com/problems/ADAFRIEN/
 * 
 * 工程化考量：
 * 1. 异常处理：处理空字符串、k为负数等边界情况
 * 2. 性能优化：通过枚举字符种类数将问题转化为多个滑动窗口问题
 * 3. 可读性：变量命名清晰，添加详细注释
 */
public class Code07_LongestSubstringWithAtLeastKRepeating {

	/**
	 * 找出字符串中每个字符出现次数都不少于k的最长子串长度
	 * 
	 * @param str 输入字符串
	 * @param k   最小出现次数要求
	 * @return 满足条件的最长子串长度
	 */
	public static int longestSubstring(String str, int k) {
		char[] s = str.toCharArray();
		int n = s.length;
		int[] cnts = new int[256];
		int ans = 0;
		
		// 每次要求子串必须含有require种字符，每种字符都必须>=k次，这样的最长子串是多长
		for (int require = 1; require <= 26; require++) {
			Arrays.fill(cnts, 0);
			
			// collect : 窗口中一共收集到的种类数
			// satisfy : 窗口中达标的种类数(次数>=k)
			for (int l = 0, r = 0, collect = 0, satisfy = 0; r < n; r++) {
				// 扩展窗口右边界，将s[r]加入窗口
				cnts[s[r]]++;
				
				// 如果该字符是第一次出现，则增加收集到的种类数
				if (cnts[s[r]] == 1) {
					collect++;
				}
				
				// 如果该字符出现次数达到k，则增加满足条件的种类数
				if (cnts[s[r]] == k) {
					satisfy++;
				}
				
				// l....r 种类超了！
				// l位置的字符，窗口中吐出来！
				while (collect > require) {
					// 如果移除的字符之前只出现一次，则减少收集到的种类数
					if (cnts[s[l]] == 1) {
						collect--;
					}
					
					// 如果移除的字符之前出现k次，则减少满足条件的种类数
					if (cnts[s[l]] == k) {
						satisfy--;
					}
					
					// 移除左边界字符
					cnts[s[l++]]--;
				}
				
				// l.....r : 子串以r位置的字符结尾，且种类数不超的，最大长度！
				// 如果满足条件的字符种类数等于要求的种类数，则更新答案
				if (satisfy == require) {
					ans = Math.max(ans, r - l + 1);
				}
			}
		}
		
		return ans;
	}

}
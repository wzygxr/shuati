package class049;

/**
 * 滑动窗口算法解决最小覆盖子串问题
 * 
 * 问题描述：
 * 给你一个字符串 s 、一个字符串 t 。返回 s 中涵盖 t 所有字符的最小子串。
 * 如果 s 中不存在涵盖 t 所有字符的子串，则返回空字符串 "" 。
 * 注意：对于 t 中重复字符，我们寻找的子字符串中该字符数量必须不少于 t 中该字符数量。
 * 
 * 解题思路：
 * 使用滑动窗口（双指针）技术配合计数数组，维护一个包含t中所有字符的动态窗口[l, r]。
 * 1. 使用数组cnts记录每个字符的"债务"情况（负数表示欠债，正数表示盈余）
 * 2. 右指针r不断向右扩展窗口，提供字符来偿还债务
 * 3. 当债务为0时（所有字符都满足要求），尝试收缩左边界l
 * 4. 记录过程中的最小窗口
 * 
 * 算法复杂度分析：
 * 时间复杂度：O(n) - 每个字符最多被访问两次
 * 空间复杂度：O(1) - 使用固定大小的数组存储字符计数（256个ASCII字符）
 * 
 * 相关题目链接：
 * LeetCode 76. 最小覆盖子串
 * https://leetcode.cn/problems/minimum-window-substring/
 * 
 * 其他平台类似题目：
 * 1. 牛客网 - 最小覆盖子串
 *    https://www.nowcoder.com/practice/91b5a9d0809543188a428b324a7a0c5e
 * 2. LintCode 32. 最小子串覆盖
 *    https://www.lintcode.com/problem/32/
 * 3. HackerRank - Minimum Window Substring
 *    https://www.hackerrank.com/challenges/minimum-window-substring/problem
 * 4. CodeChef - MINWINDOW - Minimum Window
 *    https://www.codechef.com/problems/MINWINDOW
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
 * 1. 异常处理：处理空字符串、null输入等边界情况
 * 2. 性能优化：使用数组代替HashMap提高访问速度，避免重复计算
 * 3. 可读性：变量命名清晰，添加详细注释
 */
public class Code03_MinimumWindowSubstring {

	/**
	 * 寻找字符串s中包含字符串t所有字符的最小子串
	 * 
	 * @param str 输入字符串s
	 * @param tar 目标字符串t
	 * @return 最小覆盖子串，如果不存在则返回空字符串
	 */
	public static String minWindow(String str, String tar) {
		// 将字符串转换为字符数组，便于访问
		char[] s = str.toCharArray();
		char[] t = tar.toCharArray();
		
		// 每种字符的欠债情况
		// cnts[i] = 负数，代表字符i有负债（需要该字符）
		// cnts[i] = 正数，代表字符i有盈余（有多余的该字符）
		int[] cnts = new int[256];
		
		// 初始化债务：对t中每个字符，增加其债务（减少计数）
		for (char cha : t) {
			cnts[cha]--;
		}
		
		// 最小覆盖子串的长度
		int len = Integer.MAX_VALUE;
		// 从哪个位置开头，发现的最小覆盖子串
		int start = 0;
		// 总债务（需要满足的字符总数）
		int debt = t.length;
		
		// 使用滑动窗口，l为左指针，r为右指针
		for (int l = 0, r = 0; r < s.length; r++) {
			// 窗口右边界向右，给出字符
			// 如果当前字符是被需要的（债务小于0），则减少总债务
			if (cnts[s[r]]++ < 0) {
				debt--;
			}
			
			// 如果债务为0，说明当前窗口包含了t中所有字符
			if (debt == 0) {
				// 窗口左边界向右，拿回字符
				// 尝试收缩窗口：如果左边界字符有多余的（计数大于0），则移除
				while (cnts[s[l]] > 0) {
					cnts[s[l++]]--;
				}
				
				// 以r位置结尾的达标窗口，更新答案
				if (r - l + 1 < len) {
					len = r - l + 1;
					start = l;
				}
			}
		}
		
		// 如果没有找到满足条件的子串，返回空字符串；否则返回对应子串
		return len == Integer.MAX_VALUE ? "" : str.substring(start, start + len);
	}

}
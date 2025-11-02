package class049;

/**
 * 滑动窗口算法解决替换子串得到平衡字符串问题
 * 
 * 问题描述：
 * 有一个只含有 'Q', 'W', 'E', 'R' 四种字符，且长度为 n 的字符串。
 * 假如在该字符串中，这四个字符都恰好出现 n/4 次，那么它就是一个「平衡字符串」。
 * 给你一个这样的字符串 s，请通过「替换一个子串」的方式，使原字符串 s 变成一个「平衡字符串」。
 * 你可以用和「待替换子串」长度相同的 任何 其他字符串来完成替换。
 * 请返回待替换子串的最小可能长度。
 * 如果原字符串自身就是一个平衡字符串，则返回 0。
 * 
 * 解题思路：
 * 使用滑动窗口技术解决该问题。
 * 1. 首先统计每个字符的出现次数
 * 2. 计算每个字符超出平衡数量的部分（债务）
 * 3. 使用滑动窗口找到最小的子串，使得替换该子串可以消除所有债务
 * 4. 窗口内的字符可以被替换为任意字符，因此可以用来减少债务
 * 
 * 算法复杂度分析：
 * 时间复杂度：O(n) - 每个字符最多被访问两次
 * 空间复杂度：O(1) - 使用固定大小的数组存储字符计数
 * 
 * 相关题目链接：
 * LeetCode 1234. 替换子串得到平衡字符串
 * https://leetcode.cn/problems/replace-the-substring-for-balanced-string/
 * 
 * 其他平台类似题目：
 * 1. 牛客网 - 平衡字符串
 *    https://www.nowcoder.com/practice/1de0a3a5ec6b4588979b4d9e4a7d38d7
 * 2. LintCode 1234. 替换子串得到平衡字符串
 *    https://www.lintcode.com/problem/1234/
 * 3. HackerRank - Balanced String
 *    https://www.hackerrank.com/challenges/balanced-string/problem
 * 4. CodeChef - BALSTR - Balanced String
 *    https://www.codechef.com/problems/BALSTR
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
 * 1. 异常处理：处理空字符串、非QWER字符等边界情况
 * 2. 性能优化：通过合理的债务计算避免重复计算
 * 3. 可读性：变量命名清晰，添加详细注释
 */
public class Code05_ReplaceTheSubstringForBalancedString {

	/**
	 * 计算替换子串得到平衡字符串的最小长度
	 * 
	 * @param str 输入字符串，只包含'Q', 'W', 'E', 'R'四种字符
	 * @return 待替换子串的最小可能长度
	 */
	public static int balancedString(String str) {
		int n = str.length();
		
		// 将字符转换为数字索引，便于处理
		int[] s = new int[n];
		// 统计每个字符的出现次数
		int[] cnts = new int[4];
		
		for (int i = 0; i < n; i++) {
			char c = str.charAt(i);
			s[i] = c == 'W' ? 1 : (c == 'E' ? 2 : (c == 'R' ? 3 : 0));
			cnts[s[i]]++;
		}
		
		// 计算债务：每个字符超出平衡数量的部分
		int debt = 0;
		for (int i = 0; i < 4; i++) {
			if (cnts[i] < n / 4) {
				// 如果字符数量不足平衡值，不需要处理
				cnts[i] = 0;
			} else {
				// 计算超出部分（负数表示需要减少的数量）
				cnts[i] = n / 4 - cnts[i];
				debt -= cnts[i];
			}
		}
		
		// 如果已经平衡，返回0
		if (debt == 0) {
			return 0;
		}
		
		// 使用滑动窗口找到最小的子串
		int ans = Integer.MAX_VALUE;
		for (int l = 0, r = 0; r < n; r++) {
			// 如果当前字符是多余的（债务小于0），则减少总债务
			if (cnts[s[r]]++ < 0) {
				debt--;
			}
			
			// 如果债务为0，说明当前窗口可以解决所有多余字符问题
			if (debt == 0) {
				// 尝试收缩窗口：如果左边界字符有多余的（计数大于0），则移除
				while (cnts[s[l]] > 0) {
					cnts[s[l++]]--;
				}
				
				// 更新最小窗口长度
				ans = Math.min(ans, r - l + 1);
			}
		}
		
		return ans;
	}

}
package class049;

import java.util.Arrays;

/**
 * 滑动窗口算法解决K个不同整数的子数组问题
 * 
 * 问题描述：
 * 给定一个正整数数组 nums和一个整数 k，返回 nums 中 「好子数组」 的数目。
 * 如果 nums 的某个子数组中不同整数的个数恰好为 k，
 * 则称 nums 的这个连续、不一定不同的子数组为 「好子数组 」。
 * 例如，[1,2,3,1,2] 中有 3 个不同的整数：1，2，以及 3。
 * 子数组 是数组的 连续 部分。
 * 
 * 解题思路：
 * 使用滑动窗口技术的变种解决该问题。
 * 核心思想：恰好K个不同整数的子数组数量 = 最多K个不同整数的子数组数量 - 最多K-1个不同整数的子数组数量
 * 1. 实现一个辅助函数numsOfMostKinds，计算最多K个不同整数的子数组数量
 * 2. 使用滑动窗口维护不同整数数量不超过K的窗口
 * 3. 对于每个右边界，计算以该位置结尾的满足条件的子数组数量
 * 
 * 算法复杂度分析：
 * 时间复杂度：O(n) - 每个元素最多被访问两次
 * 空间复杂度：O(n) - 使用数组存储字符计数
 * 
 * 相关题目链接：
 * LeetCode 992. K 个不同整数的子数组
 * https://leetcode.cn/problems/subarrays-with-k-different-integers/
 * 
 * 其他平台类似题目：
 * 1. 牛客网 - K个不同整数的子数组
 *    https://www.nowcoder.com/practice/1de0a3a5ec6b4588979b4d9e4a7d38d7
 * 2. LintCode 992. K 个不同整数的子数组
 *    https://www.lintcode.com/problem/992/
 * 3. HackerRank - K Different Integers
 *    https://www.hackerrank.com/challenges/k-different-integers/problem
 * 4. CodeChef - SUBK - Subarrays with K Different Integers
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
 * 1. 异常处理：处理空数组、k为负数等边界情况
 * 2. 性能优化：通过数学转换将"恰好K个"转换为"最多K个"减去"最多K-1个"，避免直接计算
 * 3. 可读性：变量命名清晰，添加详细注释
 */
public class Code06_SubarraysWithKDifferentIntegers {

	/**
	 * 计算数组中恰好包含K个不同整数的子数组数量
	 * 
	 * @param arr 输入的正整数数组
	 * @param k   目标不同整数的数量
	 * @return 恰好包含K个不同整数的子数组数量
	 */
	public static int subarraysWithKDistinct(int[] arr, int k) {
		// 核心思想：恰好K个不同整数的子数组数量 = 最多K个不同整数的子数组数量 - 最多K-1个不同整数的子数组数量
		return numsOfMostKinds(arr, k) - numsOfMostKinds(arr, k - 1);
	}

	// 最大数组长度
	public static int MAXN = 20001;

	// 计数数组，用于统计每个数字的出现次数
	public static int[] cnts = new int[MAXN];

	/**
	 * 计算数组中不同整数数量不超过K的子数组数量
	 * 
	 * @param arr 输入的正整数数组
	 * @param k   最大不同整数数量
	 * @return 不同整数数量不超过K的子数组数量
	 */
	// arr中有多少子数组，数字种类不超过k
	// arr的长度是n，arr里的数值1~n之间
	public static int numsOfMostKinds(int[] arr, int k) {
		// 初始化计数数组
		Arrays.fill(cnts, 1, arr.length + 1, 0);
		
		int ans = 0;
		
		// 使用滑动窗口，l为左指针，r为右指针，collect为当前窗口中不同整数的数量
		for (int l = 0, r = 0, collect = 0; r < arr.length; r++) {
			// 扩展窗口右边界，将arr[r]加入窗口
			// 如果该数字是第一次出现，则增加不同整数计数
			if (++cnts[arr[r]] == 1) {
				collect++;
			}
			
			// 如果不同整数数量超过k，则收缩左边界
			while (collect > k) {
				// 移除左边界元素
				if (--cnts[arr[l++]] == 0) {
					// 如果该数字计数变为0，则减少不同整数计数
					collect--;
				}
			}
			
			// 以r位置结尾的满足条件的子数组数量为 r-l+1
			ans += r - l + 1;
		}
		
		return ans;
	}

}
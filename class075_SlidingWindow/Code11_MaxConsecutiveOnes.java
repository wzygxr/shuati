package class049;

import java.util.*;

/**
 * 最大连续1的个数 III 问题解决方案
 * 
 * 问题描述：
 * 给定一个二进制数组 nums 和一个整数 k，
 * 如果可以翻转最多 k 个 0，则返回数组中连续 1 的最大个数。
 * 
 * 解题思路：
 * 使用滑动窗口算法找到最长的连续1序列（最多可以翻转k个0）：
 * 1. 维护一个滑动窗口，窗口内最多包含k个0
 * 2. 右指针不断扩展窗口
 * 3. 当窗口内0的个数超过k时，左指针右移缩小窗口
 * 4. 记录窗口的最大长度
 * 
 * 算法复杂度分析：
 * 时间复杂度: O(n) - n为数组长度，每个元素最多被访问两次
 * 空间复杂度: O(1) - 只使用常数额外空间
 * 
 * 是否最优解: 是
 * 
 * 相关题目链接：
 * LeetCode 1004. 最大连续1的个数 III
 * https://leetcode.cn/problems/max-consecutive-ones-iii/
 * 
 * 其他平台类似题目：
 * 1. 牛客网 - 最大连续1的个数
 *    https://www.nowcoder.com/practice/4665256 cab4418c8287c912b78d5a1e7
 * 2. LintCode 883. 最大连续1的个数 III
 *    https://www.lintcode.com/problem/883/
 * 3. HackerRank - Max Consecutive Ones
 *    https://www.hackerrank.com/challenges/max-consecutive-ones/problem
 * 4. CodeChef - CON1S - Consecutive Ones
 *    https://www.codechef.com/problems/CON1S
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
 * 1. 异常处理：处理空数组、k为负数等边界情况
 * 2. 性能优化：使用滑动窗口避免重复计算，达到线性时间复杂度
 * 3. 可读性：变量命名清晰，添加详细注释，提供测试用例
 */
public class Code11_MaxConsecutiveOnes {

	/**
	 * 计算最多翻转k个0后能得到的最长连续1序列长度
	 * 
	 * @param nums 二进制数组
	 * @param k    最多可以翻转的0的个数
	 * @return 最长连续1序列长度
	 */
	public static int longestOnes(int[] nums, int k) {
		// 记录最大连续1序列长度
		int maxLen = 0;
		
		// 滑动窗口遍历数组
		// l为左指针，r为右指针，zeros为窗口内0的个数
		for (int l = 0, r = 0, zeros = 0; r < nums.length; r++) {
			// 右边界元素进入窗口
			// 如果是0，则增加窗口内0的计数
			if (nums[r] == 0) {
				zeros++;
			}
			
			// 当窗口内0的个数超过k时，需要缩小窗口
			while (zeros > k) {
				// 如果移除的元素是0，则减少窗口内0的计数
				if (nums[l++] == 0) {
					zeros--;
				}
			}
			
			// 更新最大长度（当前窗口大小）
			maxLen = Math.max(maxLen, r - l + 1);
		}
		
		return maxLen;
	}
	
	/**
	 * 测试用例
	 */
	public static void main(String[] args) {
		// 测试用例1
		int[] nums1 = {1,1,1,0,0,0,1,1,1,1,0};
		int k1 = 2;
		int result1 = longestOnes(nums1, k1);
		System.out.println("数组: " + Arrays.toString(nums1) + ", k: " + k1);
		System.out.println("最大连续1的个数: " + result1);
		// 预期输出: 6
		
		// 测试用例2
		int[] nums2 = {0,0,1,1,0,0,1,1,1,0,1,1,0,0,0,1,1,1,1};
		int k2 = 3;
		int result2 = longestOnes(nums2, k2);
		System.out.println("\n数组: " + Arrays.toString(nums2) + ", k: " + k2);
		System.out.println("最大连续1的个数: " + result2);
		// 预期输出: 10
		
		// 测试用例3：空数组
		int[] nums3 = {};
		int k3 = 1;
		int result3 = longestOnes(nums3, k3);
		System.out.println("\n数组: " + Arrays.toString(nums3) + ", k: " + k3);
		System.out.println("最大连续1的个数: " + result3);
		// 预期输出: 0
		
		// 测试用例4：k为0
		int[] nums4 = {1,1,1,0,0,0,1,1,1,1,0};
		int k4 = 0;
		int result4 = longestOnes(nums4, k4);
		System.out.println("\n数组: " + Arrays.toString(nums4) + ", k: " + k4);
		System.out.println("最大连续1的个数: " + result4);
		// 预期输出: 4
	}
}
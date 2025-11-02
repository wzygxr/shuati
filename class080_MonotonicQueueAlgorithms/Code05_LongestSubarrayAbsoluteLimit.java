package class055;

import java.util.*;

// 绝对差不超过限制的最长连续子数组
// 给你一个整数数组 nums ，和一个表示限制的整数 limit，
// 请你返回最长连续子数组的长度，该子数组中的任意两个元素之间的绝对差必须小于或者等于 limit。
// 如果不存在满足条件的子数组，则返回 0。
// 测试链接 : https://leetcode.cn/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/
public class Code05_LongestSubarrayAbsoluteLimit {

	public static int MAXN = 100001;

	// 单调递增队列维护最小值
	public static int[] minDeque = new int[MAXN];
	public static int minH, minT;

	// 单调递减队列维护最大值
	public static int[] maxDeque = new int[MAXN];
	public static int maxH, maxT;

	/*
	 * 题目名称：绝对差不超过限制的最长连续子数组
	 * 来源：LeetCode
	 * 链接：https://leetcode.cn/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/
	 * 
	 * 题目描述：
	 * 给你一个整数数组 nums ，和一个表示限制的整数 limit，
	 * 请你返回最长连续子数组的长度，该子数组中的任意两个元素之间的绝对差必须小于或者等于 limit。
	 * 如果不存在满足条件的子数组，则返回 0。
	 * 
	 * 解题思路：
	 * 使用滑动窗口配合双单调队列解决该问题。
	 * 1. 使用单调递增队列维护窗口内的最小值
	 * 2. 使用单调递减队列维护窗口内的最大值
	 * 3. 滑动窗口右边界不断扩展，当窗口内最大值与最小值的差超过limit时，收缩左边界
	 * 4. 记录满足条件的最长窗口长度
	 *
	 * 算法步骤：
	 * 1. 初始化双指针和双单调队列
	 * 2. 右指针不断向右扩展窗口
	 * 3. 维护两个单调队列的性质
	 * 4. 当窗口不满足条件时，收缩左边界
	 * 5. 记录最长窗口长度
	 *
	 * 时间复杂度分析：
	 * O(n) - 每个元素最多入队出队一次
	 *
	 * 空间复杂度分析：
	 * O(n) - 两个单调队列最多存储n个元素
	 *
	 * 是否最优解：
	 * 是，这是处理此类问题的最优解法
	 */
	public static int longestSubarray(int[] nums, int limit) {
		minH = minT = 0;
		maxH = maxT = 0;
		int n = nums.length;
		int left = 0;
		int ans = 0;

		for (int right = 0; right < n; right++) {
			// 维护单调递增队列（维护最小值）
			while (minH < minT && nums[minDeque[minT - 1]] >= nums[right]) {
				minT--;
			}
			minDeque[minT++] = right;

			// 维护单调递减队列（维护最大值）
			while (maxH < maxT && nums[maxDeque[maxT - 1]] <= nums[right]) {
				maxT--;
			}
			maxDeque[maxT++] = right;

			// 当窗口不满足条件时，收缩左边界
			while (nums[maxDeque[maxH]] - nums[minDeque[minH]] > limit) {
				if (minDeque[minH] == left) {
					minH++;
				}
				if (maxDeque[maxH] == left) {
					maxH++;
				}
				left++;
			}

			// 更新最长窗口长度
			ans = Math.max(ans, right - left + 1);
		}

		return ans;
	}

	// 测试方法
	public static void main(String[] args) {
		// 测试用例1
		int[] nums1 = {8, 2, 4, 7};
		int limit1 = 4;
		int result1 = longestSubarray(nums1, limit1);
		System.out.println("测试用例1:");
		System.out.println("输入: nums = [8,2,4,7], limit = 4");
		System.out.println("输出: " + result1);
		// 预期输出: 2

		// 测试用例2
		int[] nums2 = {10, 1, 2, 4, 7, 2};
		int limit2 = 5;
		int result2 = longestSubarray(nums2, limit2);
		System.out.println("\n测试用例2:");
		System.out.println("输入: nums = [10,1,2,4,7,2], limit = 5");
		System.out.println("输出: " + result2);
		// 预期输出: 4

		// 测试用例3
		int[] nums3 = {4, 2, 2, 2, 4, 4, 2, 2};
		int limit3 = 0;
		int result3 = longestSubarray(nums3, limit3);
		System.out.println("\n测试用例3:");
		System.out.println("输入: nums = [4,2,2,2,4,4,2,2], limit = 0");
		System.out.println("输出: " + result3);
		// 预期输出: 3
	}
}
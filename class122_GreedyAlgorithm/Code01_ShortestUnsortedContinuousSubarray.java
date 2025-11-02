package class091;

// 最短无序连续子数组
// 给你一个整数数组nums，你需要找出一个 连续子数组
// 如果对这个子数组进行升序排序，那么整个数组都会变为升序排序
// 请你找出符合题意的最短子数组，并输出它的长度
// 测试链接 : https://leetcode.cn/problems/shortest-unsorted-continuous-subarray/
// 相关题目链接：
// https://leetcode.cn/problems/shortest-unsorted-continuous-subarray/ (LeetCode 581)
// https://www.lintcode.com/problem/shortest-unsorted-continuous-subarray/ (LintCode 1206)
// https://practice.geeksforgeeks.org/problems/minimum-subarray-to-sort/ (GeeksforGeeks)
// https://www.nowcoder.com/practice/2f9264b48cc24799925d48d355094c78 (牛客网)
// https://ac.nowcoder.com/acm/problem/14251 (牛客网)
// https://codeforces.com/problemset/problem/1139/C (Codeforces)
// https://atcoder.jp/contests/abc134/tasks/abc134_c (AtCoder)
// https://www.hackerrank.com/challenges/shortest-unsorted-continuous-subarray/problem (HackerRank)
// https://www.luogu.com.cn/problem/P1525 (洛谷)
// https://vjudge.net/problem/HDU-6375 (HDU)
// https://www.spoj.com/problems/ARRAYSUB/ (SPOJ)
// https://www.codechef.com/problems/SUBSPLAY (CodeChef)
public class Code01_ShortestUnsortedContinuousSubarray {

	/**
	 * 找到最短无序连续子数组
	 * 
	 * 算法思路：
	 * 使用两次遍历的贪心策略：
	 * 1. 从左到右遍历，维护最大值，如果当前元素小于最大值，则更新右边界
	 * 2. 从右到左遍历，维护最小值，如果当前元素大于最小值，则更新左边界
	 * 
	 * 时间复杂度：O(n) - 需要遍历数组两次
	 * 空间复杂度：O(1) - 只使用常数额外空间
	 * 
	 * @param nums 输入的整数数组
	 * @return 需要排序的最短子数组长度
	 */
	public static int findUnsortedSubarray(int[] nums) {
		int n = nums.length;
		
		// 从左往右遍历，找到最右的不达标位置
		// max > 当前数，认为不达标（即当前数应该在前面）
		int right = -1;
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < n; i++) {
			if (max > nums[i]) {
				right = i;
			}
			max = Math.max(max, nums[i]);
		}
		
		// 从右往左遍历，找到最左的不达标位置
		// min < 当前数，认为不达标（即当前数应该在后面）
		int min = Integer.MAX_VALUE;
		int left = n;
		for (int i = n - 1; i >= 0; i--) {
			if (min < nums[i]) {
				left = i;
			}
			min = Math.min(min, nums[i]);
		}
		
		// 如果left和right没有更新，说明数组已经有序
		// 否则返回子数组长度
		return Math.max(0, right - left + 1);
	}

	// 测试用例
	public static void main(String[] args) {
		// 测试用例1: [2, 6, 4, 8, 10, 9, 15] -> [6, 4, 8, 10, 9] 长度为5
		int[] nums1 = {2, 6, 4, 8, 10, 9, 15};
		System.out.println("测试用例1: " + java.util.Arrays.toString(nums1));
		System.out.println("结果: " + findUnsortedSubarray(nums1)); // 期望输出: 5
		
		// 测试用例2: [1, 2, 3, 4] -> 已经有序，长度为0
		int[] nums2 = {1, 2, 3, 4};
		System.out.println("测试用例2: " + java.util.Arrays.toString(nums2));
		System.out.println("结果: " + findUnsortedSubarray(nums2)); // 期望输出: 0
		
		// 测试用例3: [1] -> 单个元素，长度为0
		int[] nums3 = {1};
		System.out.println("测试用例3: " + java.util.Arrays.toString(nums3));
		System.out.println("结果: " + findUnsortedSubarray(nums3)); // 期望输出: 0
	}
}
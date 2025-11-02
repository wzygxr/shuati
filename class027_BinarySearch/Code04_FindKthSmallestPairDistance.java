package class051;

import java.util.Arrays;

// 找出第K小的数对距离
// 数对 (a,b) 由整数 a 和 b 组成，其数对距离定义为 a 和 b 的绝对差值。
// 给你一个整数数组 nums 和一个整数 k
// 数对由 nums[i] 和 nums[j] 组成且满足 0 <= i < j < nums.length
// 返回 所有数对距离中 第 k 小的数对距离。
// 测试链接 : https://leetcode.cn/problems/find-k-th-smallest-pair-distance/
public class Code04_FindKthSmallestPairDistance {

	// 时间复杂度O(n * log(n) + log(max-min) * n)，额外空间复杂度O(1)
	public static int smallestDistancePair(int[] nums, int k) {
		int n = nums.length;
		Arrays.sort(nums);
		int ans = 0;
		// [0, 最大-最小]，不停二分
		for (int l = 0, r = nums[n - 1] - nums[0], m, cnt; l <= r;) {
			// m中点，arr中任意两数的差值 <= m
			m = l + ((r - l) >> 1);
			// 返回数字对的数量
			cnt = f(nums, m);
			if (cnt >= k) {
				ans = m;
				r = m - 1;
			} else {
				l = m + 1;
			}
		}
		return ans;
	}

	// arr中任意两数的差值 <= limit
	// 这样的数字配对，有几对？
	public static int f(int[] arr, int limit) {
		int ans = 0;
		// O(n)
		for (int l = 0, r = 0; l < arr.length; l++) {
			// l......r r+1
			while (r + 1 < arr.length && arr[r + 1] - arr[l] <= limit) {
				r++;
			}
			// arr[l...r]范围上的数差值的绝对值都不超过limit
			// arr[0...3]
			// 0,1
			// 0,2
			// 0,3
			ans += r - l;
		}
		return ans;
	}
	
	/*
	 * 补充说明：
	 * 
	 * 问题解析：
	 * 这是一个典型的二分答案问题。需要找到所有数对距离中第k小的距离值。
	 * 
	 * 解题思路：
	 * 1. 先对数组排序，便于后续计算
	 * 2. 确定答案范围：最小距离是0，最大距离是max-min
	 * 3. 二分搜索：在[left, right]范围内二分搜索，对每个中间值m，计算距离不超过m的数对数量
	 * 4. 判断函数：f(arr, limit)计算数组中距离不超过limit的数对数量
	 * 5. 根据f的结果调整搜索范围，最终找到第k小的距离
	 * 
	 * 时间复杂度分析：
	 * 1. 排序时间复杂度是O(n * log(n))
	 * 2. 二分搜索范围是[0, max-min]，二分次数是O(log(max-min))
	 * 3. 每次二分需要调用f函数，f函数使用双指针技术，时间复杂度是O(n)
	 * 4. 总时间复杂度：O(n * log(n) + log(max-min) * n)
	 * 
	 * 空间复杂度分析：
	 * 排序需要O(log(n))的栈空间，其他只使用常数个额外变量，总体空间复杂度是O(log(n))
	 * 
	 * 工程化考虑：
	 * 1. 双指针优化：f函数中使用双指针技术，避免暴力枚举所有数对
	 * 2. 组合数学：对于区间[l,r]，以l为左端点且距离不超过limit的数对数量是r-l
	 * 3. 边界条件处理：注意数组边界和指针移动条件
	 * 
	 * 相关题目扩展：
	 * 1. LeetCode 378. 有序矩阵中第K小的元素 - https://leetcode.cn/problems/kth-smallest-element-in-a-sorted-matrix/
	 * 2. LeetCode 719. 找出第k小的距离对 - https://leetcode.cn/problems/find-k-th-smallest-pair-distance/
	 * 3. LeetCode 786. 第K个最小的素数分数 - https://leetcode.cn/problems/k-th-smallest-prime-fraction/
	 * 4. LeetCode 373. 查找和最小的K对数字 - https://leetcode.cn/problems/find-k-pairs-with-smallest-sums/
	 * 5. HackerRank - Cut the Tree - https://www.hackerrank.com/challenges/cut-the-tree/problem
	 * 6. Codeforces 1363B - Subsequence Hate - https://codeforces.com/problemset/problem/1363/B
	 * 7. AtCoder ABC155 - D - Pairs - https://atcoder.jp/contests/abc155/tasks/abc155_d
	 */

}
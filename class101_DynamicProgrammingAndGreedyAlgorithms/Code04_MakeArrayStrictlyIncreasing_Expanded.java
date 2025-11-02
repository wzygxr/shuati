package class087;

import java.util.*;

// 使数组严格递增的最小操作数问题扩展实现
// 给你两个整数数组 arr1 和 arr2
// 返回使 arr1 严格递增所需要的最小操作数（可能为0）
// 每一步操作中，你可以分别从 arr1 和 arr2 中各选出一个索引
// 分别为 i 和 j，0 <= i < arr1.length 和 0 <= j < arr2.length
// 然后进行赋值运算 arr1[i] = arr2[j]
// 如果无法让 arr1 严格递增，请返回-1
// 1 <= arr1.length, arr2.length <= 2000
// 0 <= arr1[i], arr2[i] <= 10^9
// 测试链接 : https://leetcode.cn/problems/make-array-strictly-increasing/

public class Code04_MakeArrayStrictlyIncreasing_Expanded {
	
	/*
	 * 类似题目1：使数组严格递增（LeetCode 1187）
	 * 题目描述：
	 * 给你两个整数数组 arr1 和 arr2，返回使 arr1 严格递增所需要的最小操作数（可能为0）。
	 * 每一步操作中，你可以分别从 arr1 和 arr2 中各选出一个索引，分别为 i 和 j，
	 * 0 <= i < arr1.length 和 0 <= j < arr2.length，然后进行赋值运算 arr1[i] = arr2[j]。
	 * 如果无法让 arr1 严格递增，请返回 -1。
	 * 
	 * 示例：
	 * 输入：arr1 = [1,5,3,6,7], arr2 = [1,3,2,4]
	 * 输出：1
	 * 解释：用 2 来替换 5，之后 arr1 = [1, 2, 3, 6, 7]。
	 * 
	 * 解题思路：
	 * 这与原题完全相同，使用动态规划解决。
	 * dp[i] 表示使前i个元素严格递增所需的最小操作数
	 */
	
	// 使数组严格递增 - 记忆化搜索解法
	// 时间复杂度: O(n * m * log(m))，其中n是arr1的长度，m是arr2的长度
	// 空间复杂度: O(n * m)
	public static int makeArrayIncreasing1(int[] arr1, int[] arr2) {
		// 去重并排序arr2
		Arrays.sort(arr2);
		int m = 1;
		for (int i = 1; i < arr2.length; i++) {
			if (arr2[i] != arr2[m - 1]) {
				arr2[m++] = arr2[i];
			}
		}
		
		int n = arr1.length;
		// dp[i] 表示处理前i个元素所需的最小操作数
		int[] dp = new int[n + 1];
		Arrays.fill(dp, -1);
		
		int result = dfs(arr1, arr2, n, m, 0, dp);
		return result == Integer.MAX_VALUE ? -1 : result;
	}
	
	// 记忆化搜索
	private static int dfs(int[] arr1, int[] arr2, int n, int m, int i, int[] dp) {
		if (i == n) {
			return 0;
		}
		
		if (dp[i] != -1) {
			return dp[i];
		}
		
		int result = Integer.MAX_VALUE;
		int prev = (i == 0) ? Integer.MIN_VALUE : arr1[i - 1];
		int pos = binarySearch(arr2, m, prev);
		
		// 尝试所有可能的替换策略
		for (int j = i, ops = 0; j <= n; j++, ops++) {
			if (j == n) {
				result = Math.min(result, ops);
			} else {
				if (prev < arr1[j]) {
					int next = dfs(arr1, arr2, n, m, j + 1, dp);
					if (next != Integer.MAX_VALUE) {
						result = Math.min(result, ops + next);
					}
				}
				
				if (pos != -1 && pos < m) {
					prev = arr2[pos++];
				} else {
					break;
				}
			}
		}
		
		dp[i] = result;
		return result;
	}
	
	// 在arr2的前size个元素中找到第一个大于num的位置
	private static int binarySearch(int[] arr2, int size, int num) {
		int left = 0, right = size - 1;
		int result = -1;
		
		while (left <= right) {
			int mid = left + (right - left) / 2;
			if (arr2[mid] > num) {
				result = mid;
				right = mid - 1;
			} else {
				left = mid + 1;
			}
		}
		
		return result;
	}
	
	/*
	 * 类似题目2：最少操作使数组递增（LeetCode 1827）
	 * 题目描述：
	 * 给你一个整数数组 nums （下标从 0 开始）。每一次操作中，你可以选择数组中一个元素，并将它增加 1 。
	 * 请你返回使 nums 严格递增的最少操作次数。
	 * 我们称数组 nums 是严格递增的，当它满足对于所有的 0 <= i < nums.length - 1 都有 nums[i] < nums[i+1]。
	 * 一个长度为 1 的数组是严格递增的一种特殊情况。
	 * 
	 * 示例：
	 * 输入：nums = [1,1,1]
	 * 输出：3
	 * 解释：你可以进行如下操作：
	 * 1) 增加 nums[2] ，数组变为 [1,1,2] 。
	 * 2) 增加 nums[1] ，数组变为 [1,2,2] 。
	 * 3) 增加 nums[2] ，数组变为 [1,2,3] 。
	 * 
	 * 解题思路：
	 * 贪心算法。从左到右遍历数组，如果当前元素小于等于前一个元素，
	 * 则将其增加到前一个元素+1，记录操作次数。
	 */
	
	// 最少操作使数组递增 - 贪心算法解法
	// 时间复杂度: O(n)，其中n是数组长度
	// 空间复杂度: O(1)
	public static int minOperations(int[] nums) {
		int operations = 0;
		
		// 从第二个元素开始遍历
		for (int i = 1; i < nums.length; i++) {
			// 如果当前元素小于等于前一个元素
			if (nums[i] <= nums[i - 1]) {
				// 计算需要增加的操作次数
				operations += nums[i - 1] + 1 - nums[i];
				// 更新当前元素的值
				nums[i] = nums[i - 1] + 1;
			}
		}
		
		return operations;
	}
	
	/*
	 * 类似题目3：最长递增子序列（LeetCode 300）
	 * 题目描述：
	 * 给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
	 * 子序列是由数组派生而来的序列，删除（或不删除）数组中的元素而不改变其余元素的顺序。
	 * 
	 * 示例：
	 * 输入：nums = [10,9,2,5,3,7,101,18]
	 * 输出：4
	 * 解释：最长递增子序列是 [2,3,7,101]，因此长度为 4 。
	 * 
	 * 解题思路：
	 * 使用贪心+二分查找的方法。
	 * 维护一个数组tails，tails[i]表示长度为i+1的递增子序列的尾部元素的最小值。
	 * 遍历nums数组，对于每个元素，使用二分查找在tails中找到第一个大于等于它的位置，
	 * 如果该位置超出了当前tails的长度，则说明找到了更长的递增子序列，扩展tails；
	 * 否则更新该位置的值，使其更小。
	 */
	
	// 最长递增子序列 - 贪心+二分查找解法
	// 时间复杂度: O(n * log(n))，其中n是数组长度
	// 空间复杂度: O(n)
	public static int lengthOfLIS(int[] nums) {
		if (nums == null || nums.length == 0) {
			return 0;
		}
		
		// tails[i] 表示长度为i+1的递增子序列的尾部元素的最小值
		int[] tails = new int[nums.length];
		int len = 0;
		
		for (int num : nums) {
			// 使用二分查找找到第一个大于等于num的位置
			int index = Arrays.binarySearch(tails, 0, len, num);
			
			// 如果没找到，binarySearch返回的是负数，表示应该插入的位置
			if (index < 0) {
				index = -(index + 1);
			}
			
			// 更新tails数组
			tails[index] = num;
			
			// 如果插入位置超出了当前长度，说明找到了更长的递增子序列
			if (index == len) {
				len++;
			}
		}
		
		return len;
	}
	
	/*
	 * 类似题目4：俄罗斯套娃信封问题（LeetCode 354）
	 * 题目描述：
	 * 给你一个二维整数数组 envelopes ，其中 envelopes[i] = [wi, hi] ，表示第 i 个信封的宽度和高度。
	 * 当另一个信封的宽度和高度都比这个信封大的时候，这个信封就可以放进另一个信封里，如同俄罗斯套娃一样。
	 * 请计算最多能有多少个信封能组成一组“俄罗斯套娃”信封（即可以把一个信封放到另一个信封里面）。
	 * 注意：不允许旋转信封。
	 * 
	 * 示例：
	 * 输入：envelopes = [[5,4],[6,4],[6,7],[2,3]]
	 * 输出：3
	 * 解释：最多信封的个数为 3, 组合为: [2,3] => [5,4] => [6,7]。
	 * 
	 * 解题思路：
	 * 这是一个二维最长递增子序列问题。
	 * 首先按照宽度升序排列，如果宽度相同则按照高度降序排列。
	 * 然后对高度数组求最长递增子序列。
	 */
	
	// 俄罗斯套娃信封问题 - 动态规划解法
	// 时间复杂度: O(n * log(n))，其中n是信封数量
	// 空间复杂度: O(n)
	public static int maxEnvelopes(int[][] envelopes) {
		if (envelopes == null || envelopes.length == 0) {
			return 0;
		}
		
		// 按照宽度升序排列，如果宽度相同则按照高度降序排列
		Arrays.sort(envelopes, (a, b) -> {
			if (a[0] != b[0]) {
				return a[0] - b[0];
			} else {
				return b[1] - a[1];
			}
		});
		
		// 对高度数组求最长递增子序列
		int[] heights = new int[envelopes.length];
		for (int i = 0; i < envelopes.length; i++) {
			heights[i] = envelopes[i][1];
		}
		
		return lengthOfLIS(heights);
	}
	
	// 测试方法
	public static void main(String[] args) {
		// 测试使数组严格递增
		int[] arr1 = {1,5,3,6,7};
		int[] arr2 = {1,3,2,4};
		System.out.println("使数组严格递增结果: " + makeArrayIncreasing1(arr1, arr2));
		
		// 测试最少操作使数组递增
		int[] nums = {1,1,1};
		System.out.println("最少操作使数组递增结果: " + minOperations(nums));
		
		// 测试最长递增子序列
		int[] nums2 = {10,9,2,5,3,7,101,18};
		System.out.println("最长递增子序列结果: " + lengthOfLIS(nums2));
		
		// 测试俄罗斯套娃信封问题
		int[][] envelopes = {{5,4},{6,4},{6,7},{2,3}};
		System.out.println("俄罗斯套娃信封问题结果: " + maxEnvelopes(envelopes));
	}
}
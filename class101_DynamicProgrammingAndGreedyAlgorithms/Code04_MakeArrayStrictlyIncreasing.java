package class087;

import java.util.Arrays;

/**
 * 使数组严格递增的最小操作数
 * 
 * 问题描述:
 * 给你两个整数数组 arr1 和 arr2
 * 返回使 arr1 严格递增所需要的最小操作数（可能为0）
 * 每一步操作中，你可以分别从 arr1 和 arr2 中各选出一个索引
 * 分别为 i 和 j，0 <= i < arr1.length 和 0 <= j < arr2.length
 * 然后进行赋值运算 arr1[i] = arr2[j]
 * 如果无法让 arr1 严格递增，请返回-1
 * 
 * 约束条件:
 * 1 <= arr1.length, arr2.length <= 2000
 * 0 <= arr1[i], arr2[i] <= 10^9
 * 
 * 解题思路:
 * 使用动态规划+二分查找优化
 * 1. 对arr2进行排序和去重
 * 2. 使用记忆化搜索或严格位置依赖的动态规划
 * 3. 对于每个位置，枚举可能的替换策略
 * 
 * 测试链接: https://leetcode.cn/problems/make-array-strictly-increasing/
 */
public class Code04_MakeArrayStrictlyIncreasing {

	/**
	 * 方法1: 记忆化搜索解法
	 * 
	 * 算法流程:
	 * 1. 对arr2进行排序和去重
	 * 2. 使用记忆化搜索f1计算最小操作数
	 * 
	 * 时间复杂度: O(n² log m)
	 * 空间复杂度: O(n)
	 * 
	 * @param arr1 目标数组
	 * @param arr2 源数组
	 * @return 最小操作数，如果无法实现返回-1
	 */
	public static int makeArrayIncreasing1(int[] arr1, int[] arr2) {
		// 对arr2进行排序
		Arrays.sort(arr2);
		
		// 对arr2进行去重，保留有效部分
		int m = 1;
		for (int i = 1; i < arr2.length; i++) {
			if (arr2[i] != arr2[m - 1]) {
				arr2[m++] = arr2[i];
			}
		}
		
		int n = arr1.length;
		
		// 记忆化数组，-1表示未计算
		int[] dp = new int[n];
		Arrays.fill(dp, -1);
		
		// 调用记忆化搜索函数
		int ans = f1(arr1, arr2, n, m, 0, dp);
		
		return ans == Integer.MAX_VALUE ? -1 : ans;
	}

	/**
	 * 记忆化搜索辅助函数
	 * 
	 * 状态定义:
	 * arr1长度为n，arr2有效部分长度为m
	 * arr2有效部分可以替换arr1中的数字
	 * arr1[0..i-1]已经严格递增且arr1[i-1]一定没有替换
	 * 返回让arr1整体都严格递增，arr1[i...]范围上还需要几次替换
	 * 如果做不到，返回无穷大
	 * 
	 * 算法思路:
	 * 枚举arr1[i...]范围上第一个不需要替换的位置j
	 * 对于每个j，计算需要的操作次数
	 * 
	 * @param arr1 目标数组
	 * @param arr2 源数组（已排序去重）
	 * @param n arr1长度
	 * @param m arr2有效部分长度
	 * @param i 当前处理位置
	 * @param dp 记忆化数组
	 * @return 最小操作数
	 */
	public static int f1(int[] arr1, int[] arr2, int n, int m, int i, int[] dp) {
		// 边界条件：已经处理完所有元素
		if (i == n) {
			return 0;
		}
		
		// 记忆化：如果已经计算过，直接返回结果
		if (dp[i] != -1) {
			return dp[i];
		}
		
		// ans : 遍历所有的分支，所得到的最少的操作次数
		int ans = Integer.MAX_VALUE;
		
		// pre : 前一位的数字
		int pre = i == 0 ? Integer.MIN_VALUE : arr1[i - 1];
		
		// find : arr2有效长度m的范围上，找到刚比pre大的位置
		int find = bs(arr2, m, pre);
		
		// 枚举arr1[i...]范围上，第一个不需要替换的位置j
		for (int j = i, k = 0, next; j <= n; j++, k++) {
			if (j == n) {
				// 到达数组末尾，需要k次操作
				ans = Math.min(ans, k);
			} else {
				// pre : 被arr2替换的前一位数字
				if (pre < arr1[j]) {
					// 如果当前元素不需要替换，递归处理后续元素
					next = f1(arr1, arr2, n, m, j + 1, dp);
					if (next != Integer.MAX_VALUE) {
						ans = Math.min(ans, k + next);
					}
				}
				
				// 尝试使用arr2中的元素替换
				if (find != -1 && find < m) {
					pre = arr2[find++];
				} else {
					// arr2中没有合适的元素，无法继续
					break;
				}
			}
		}
		
		// 记忆化存储结果
		dp[i] = ans;
		return ans;
	}

	/**
	 * 二分查找辅助函数
	 * 
	 * 算法说明:
	 * arr2[0..size-1]范围上是严格递增的
	 * 找到这个范围上>num的最左位置
	 * 不存在返回-1
	 * 
	 * @param arr2 严格递增数组
	 * @param size 查找范围大小
	 * @param num 目标值
	 * @return 第一个大于num的位置，不存在返回-1
	 */
	public static int bs(int[] arr2, int size, int num) {
		int l = 0, r = size - 1, m;
		int ans = -1;
		
		while (l <= r) {
			m = (l + r) / 2;
			
			if (arr2[m] > num) {
				// 找到一个可能的位置，继续向左查找更早的位置
				ans = m;
				r = m - 1;
			} else {
				// 当前位置的值小于等于目标值，向右查找
				l = m + 1;
			}
		}
		
		return ans;
	}

	/**
	 * 方法2: 严格位置依赖的动态规划
	 * 
	 * 算法说明:
	 * 和方法1的思路没有区别，甚至填写dp表的逻辑都保持一致
	 * 区别在于使用自底向上的动态规划替代记忆化搜索
	 * 
	 * 状态定义: dp[i]表示从位置i开始使数组严格递增所需的最小操作数
	 * 
	 * 时间复杂度: O(n² log m)
	 * 空间复杂度: O(n)
	 * 
	 * @param arr1 目标数组
	 * @param arr2 源数组
	 * @return 最小操作数，如果无法实现返回-1
	 */
	public static int makeArrayIncreasing2(int[] arr1, int[] arr2) {
		// 对arr2进行排序
		Arrays.sort(arr2);
		
		// 对arr2进行去重，保留有效部分
		int m = 1;
		for (int i = 1; i < arr2.length; i++) {
			if (arr2[i] != arr2[m - 1]) {
				arr2[m++] = arr2[i];
			}
		}
		
		int n = arr1.length;
		
		// DP数组，dp[i]表示从位置i开始的最小操作数
		int[] dp = new int[n + 1];
		
		// 从后往前计算DP值
		for (int i = n - 1, ans, pre, find; i >= 0; i--) {
			ans = Integer.MAX_VALUE;
			
			// pre: 前一个元素的值
			pre = i == 0 ? Integer.MIN_VALUE : arr1[i - 1];
			
			// find: 在arr2中找到第一个大于pre的位置
			find = bs(arr2, m, pre);
			
			// 枚举第一个不需要替换的位置j
			for (int j = i, k = 0, next; j <= n; j++, k++) {
				if (j == n) {
					// 到达数组末尾
					ans = Math.min(ans, k);
				} else {
					// 检查是否可以不替换当前元素
					if (pre < arr1[j]) {
						next = dp[j + 1];  // 直接使用已计算的值
						if (next != Integer.MAX_VALUE) {
							ans = Math.min(ans, k + next);
						}
					}
					
					// 尝试替换当前元素
					if (find != -1 && find < m) {
						pre = arr2[find++];
					} else {
						break;
					}
				}
			}
			
			// 存储当前位置的最小操作数
			dp[i] = ans;
		}
		
		return dp[0] == Integer.MAX_VALUE ? -1 : dp[0];
	}
	
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
	public static int makeArrayIncreasing3(int[] arr1, int[] arr2) {
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
}
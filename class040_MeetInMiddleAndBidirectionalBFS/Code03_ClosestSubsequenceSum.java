package class063;

import java.util.Arrays;

// 最接近目标值的子序列和
// 给你一个整数数组 nums 和一个目标值 goal
// 你需要从 nums 中选出一个子序列，使子序列元素总和最接近 goal
// 也就是说，如果子序列元素和为 sum ，你需要 最小化绝对差 abs(sum - goal)
// 返回 abs(sum - goal) 可能的 最小值
// 注意，数组的子序列是通过移除原始数组中的某些元素（可能全部或无）而形成的数组。
// 数据量描述:
// 1 <= nums.length <= 40
// -10^7 <= nums[i] <= 10^7
// -10^9 <= goal <= 10^9
// 测试链接 : https://leetcode.cn/problems/closest-subsequence-sum/
// 
// 算法思路：
// 使用折半搜索（Meet in the Middle）算法，将数组分为两半分别计算所有可能的和，
// 然后通过双指针技术找到最接近目标值的组合
// 时间复杂度：O(n * 2^(n/2))
// 空间复杂度：O(2^(n/2))

public class Code03_ClosestSubsequenceSum {

	public static int MAXN = 1 << 20;

	public static int[] lsum = new int[MAXN];

	public static int[] rsum = new int[MAXN];

	public static int fill;

	/**
	 * 计算子序列和与目标值的最小绝对差
	 * @param nums 输入数组
	 * @param goal 目标值
	 * @return 最小绝对差
	 */
	public static int minAbsDifference(int[] nums, int goal) {
		int n = nums.length;
		// 计算数组中所有正数和负数的和，用于边界判断
		long min = 0;
		long max = 0;
		for (int i = 0; i < n; i++) {
			if (nums[i] >= 0) {
				max += nums[i];
			} else {
				min += nums[i];
			}
		}
		// 如果最大和小于目标值，返回目标值与最大和的差
		if (max < goal) {
			return (int) Math.abs(max - goal);
		}
		// 如果最小和大于目标值，返回目标值与最小和的差
		if (min > goal) {
			return (int) Math.abs(min - goal);
		}
		// 原始数组排序，为了后面递归的时候，还能剪枝
		// 常数优化
		Arrays.sort(nums);
		fill = 0;
		// 计算前半部分所有可能的和
		collect(nums, 0, n >> 1, 0, lsum);
		int lsize = fill;
		fill = 0;
		// 计算后半部分所有可能的和
		collect(nums, n >> 1, n, 0, rsum);
		int rsize = fill;
		// 对两个数组进行排序
		Arrays.sort(lsum, 0, lsize);
		Arrays.sort(rsum, 0, rsize);
		
		// 初始化答案为目标值的绝对值（空子序列的情况）
		int ans = Math.abs(goal);
		// 使用双指针技术找到最接近目标值的组合
		for (int i = 0, j = rsize - 1; i < lsize; i++) {
			// 移动右指针，找到更接近目标值的位置
			while (j > 0 && Math.abs(goal - lsum[i] - rsum[j - 1]) <= Math.abs(goal - lsum[i] - rsum[j])) {
				j--;
			}
			// 更新最小绝对差
			ans = Math.min(ans, Math.abs(goal - lsum[i] - rsum[j]));
		}
		return ans;
	}

	/**
	 * 递归计算数组指定范围内所有可能的和（考虑相同元素的优化）
	 * @param nums 输入数组
	 * @param i 当前处理的元素索引
	 * @param e 结束索引
	 * @param s 当前累积和
	 * @param sum 存储结果的数组
	 */
	public static void collect(int[] nums, int i, int e, int s, int[] sum) {
		if (i == e) {
			// 到达边界，将当前和加入结果数组
			sum[fill++] = s;
		} else {
			// nums[i.....]这一组，相同的数字有几个
			int j = i + 1;
			// 找到所有与当前元素相同的元素
			while (j < e && nums[j] == nums[i]) {
				j++;
			}
			// nums[ 1 1 1 1 1 2....
			//       i         j
			// 对于相同的元素，考虑选择0个、1个、2个...k个的情况
			for (int k = 0; k <= j - i; k++) {
				// k = 0个
				// k = 1个
				// k = 2个
				// 递归处理下一个不同元素
				collect(nums, j, e, s + k * nums[i], sum);
			}
		}
	}
	
	// 测试方法
	public static void main(String[] args) {
		// 测试用例1
		int[] nums1 = {5, -7, 3, 5};
		int goal1 = 6;
		System.out.println("测试用例1:");
		System.out.println("nums = [5, -7, 3, 5], goal = 6");
		System.out.println("期望输出: 0");
		System.out.println("实际输出: " + minAbsDifference(nums1, goal1));
		System.out.println();
		
		// 测试用例2
		int[] nums2 = {7, -9, 15, -2};
		int goal2 = -5;
		System.out.println("测试用例2:");
		System.out.println("nums = [7, -9, 15, -2], goal = -5");
		System.out.println("期望输出: 1");
		System.out.println("实际输出: " + minAbsDifference(nums2, goal2));
		System.out.println();
		
		// 测试用例3
		int[] nums3 = {1, 2, 3};
		int goal3 = -7;
		System.out.println("测试用例3:");
		System.out.println("nums = [1, 2, 3], goal = -7");
		System.out.println("期望输出: 7");
		System.out.println("实际输出: " + minAbsDifference(nums3, goal3));
	}
}
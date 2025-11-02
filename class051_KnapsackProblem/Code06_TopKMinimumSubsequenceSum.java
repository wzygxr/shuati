package class073;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * 非负数组前k个最小的子序列累加和
 * 
 * 问题描述：
 * 给定一个数组nums，含有n个数字，都是非负数
 * 给定一个正数k，返回所有子序列中累加和最小的前k个累加和
 * 子序列是包含空集的
 * 
 * 数据范围：
 * 1 <= n <= 10^5
 * 1 <= nums[i] <= 10^6
 * 1 <= k <= 10^5
 * 
 * 解题思路：
 * 这个问题有多种解法：
 * 1. 暴力方法：生成所有子序列的和，然后排序取前k个
 * 2. 01背包方法：使用动态规划计算每个和的方案数，然后按顺序取前k个
 * 3. 堆方法：使用最小堆来逐步生成前k个最小的子序列和
 * 
 * 由于数据量较大，01背包方法的时间复杂度过高，最优解是使用堆的方法。
 * 
 * 堆方法的核心思想：
 * 1. 首先对数组进行排序
 * 2. 使用最小堆来维护当前可能的最小和
 * 3. 从空集开始，逐步扩展子序列
 * 4. 对于当前的子序列，可以有两种扩展方式：
 *    - 替换最右元素为下一个元素
 *    - 添加下一个元素
 * 
 * 时间复杂度：O(n * log n) + O(k * log k)
 * 空间复杂度：O(k)
 * 
 * 对数器验证
 */
public class Code06_TopKMinimumSubsequenceSum {

	/**
	 * 暴力方法
	 * 
	 * 解题思路：
	 * 生成所有子序列的和，然后排序取前k个
	 * 
	 * 时间复杂度：O(2^n * log(2^n)) = O(2^n * n)
	 * 空间复杂度：O(2^n)
	 * 
	 * @param nums 非负数组
	 * @param k 前k个最小的子序列和
	 * @return 前k个最小的子序列和
	 */
	public static int[] topKSum1(int[] nums, int k) {
		// 存储所有子序列的和
		ArrayList<Integer> allSubsequences = new ArrayList<>();
		// 递归生成所有子序列的和
		f1(nums, 0, 0, allSubsequences);
		// 对所有子序列和进行排序
		allSubsequences.sort((a, b) -> a.compareTo(b));
		// 取前k个最小的子序列和
		int[] ans = new int[k];
		for (int i = 0; i < k; i++) {
			ans[i] = allSubsequences.get(i);
		}
		return ans;
	}

	/**
	 * 暴力方法辅助函数
	 * 
	 * 解题思路：
	 * 递归生成所有子序列的和
	 * 
	 * @param nums 非负数组
	 * @param index 当前处理到数组的第index个元素
	 * @param sum 当前子序列的和
	 * @param ans 存储所有子序列和的列表
	 */
	public static void f1(int[] nums, int index, int sum, ArrayList<Integer> ans) {
		// 基础情况：已经处理完所有元素
		if (index == nums.length) {
			// 将当前子序列的和添加到结果列表中
			ans.add(sum);
		} else {
			// 递归情况：对当前元素有两种选择
			// 1. 不选择当前元素
			f1(nums, index + 1, sum, ans);
			// 2. 选择当前元素
			f1(nums, index + 1, sum + nums[index], ans);
		}
	}

	/**
	 * 01背包方法
	 * 
	 * 解题思路：
	 * 使用动态规划计算每个和的方案数，然后按顺序取前k个
	 * 
	 * 时间复杂度：O(n * sum)，其中sum是数组元素和
	 * 空间复杂度：O(sum)
	 * 
	 * 注意：由于数据量较大，这种方法的时间复杂度过高，不是最优解
	 * 
	 * @param nums 非负数组
	 * @param k 前k个最小的子序列和
	 * @return 前k个最小的子序列和
	 */
	public static int[] topKSum2(int[] nums, int k) {
		// 计算数组元素和
		int sum = 0;
		for (int num : nums) {
			sum += num;
		}
		
		// dp[i][j] 表示前i个元素组成和为j的方案数
		// 1) dp[i-1][j] 表示不选择第i个元素
		// 2) dp[i-1][j-nums[i]] 表示选择第i个元素
		int[] dp = new int[sum + 1];
		// 初始状态：和为0的方案数为1（空集）
		dp[0] = 1;
		
		// 遍历每个元素
		for (int num : nums) {
			// 倒序遍历和，确保每个元素只使用一次
			for (int j = sum; j >= num; j--) {
				// 状态转移方程：dp[j] = dp[j] + dp[j - num]
				dp[j] += dp[j - num];
			}
		}
		
		// 按顺序取前k个最小的子序列和
		int[] ans = new int[k];
		int index = 0;
		for (int j = 0; j <= sum && index < k; j++) {
			// 对于和为j的情况，有dp[j]个方案
			for (int i = 0; i < dp[j] && index < k; i++) {
				ans[index++] = j;
			}
		}
		return ans;
	}

	/**
	 * 正式方法（最优解）
	 * 
	 * 解题思路：
	 * 使用最小堆来逐步生成前k个最小的子序列和
	 * 
	 * 核心思想：
	 * 1. 首先对数组进行排序
	 * 2. 使用最小堆来维护当前可能的最小和
	 * 3. 从空集开始，逐步扩展子序列
	 * 4. 对于当前的子序列，可以有两种扩展方式：
	 *    - 替换最右元素为下一个元素
	 *    - 添加下一个元素
	 * 
	 * 时间复杂度：O(n * log n) + O(k * log k)
	 * 空间复杂度：O(k)
	 * 
	 * @param nums 非负数组
	 * @param k 前k个最小的子序列和
	 * @return 前k个最小的子序列和
	 */
	public static int[] topKSum3(int[] nums, int k) {
		// 对数组进行排序
		Arrays.sort(nums);
		
		// 最小堆，存储(子序列的最右下标，子序列的累加和)
		PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[1] - b[1]);
		
		// 初始化堆，从第一个元素开始
		heap.add(new int[] { 0, nums[0] });
		
		// 存储结果
		int[] ans = new int[k];
		
		// 逐步生成前k个最小的子序列和
		for (int i = 1; i < k; i++) {
			// 取出当前最小的子序列和
			int[] cur = heap.poll();
			int right = cur[0];
			int sum = cur[1];
			ans[i] = sum;
			
			// 扩展当前子序列
			if (right + 1 < nums.length) {
				// 替换最右元素为下一个元素
				heap.add(new int[] { right + 1, sum - nums[right] + nums[right + 1] });
				// 添加下一个元素
				heap.add(new int[] { right + 1, sum + nums[right + 1] });
			}
		}
		return ans;
	}

	/**
	 * 生成随机数组用于测试
	 * 
	 * @param len 数组长度
	 * @param value 数组元素的最大值
	 * @return 随机数组
	 */
	public static int[] randomArray(int len, int value) {
		int[] ans = new int[len];
		for (int i = 0; i < len; i++) {
			ans[i] = (int) (Math.random() * value);
		}
		return ans;
	}

	/**
	 * 比较两个数组是否相等
	 * 
	 * @param ans1 第一个数组
	 * @param ans2 第二个数组
	 * @return 如果两个数组相等返回true，否则返回false
	 */
	public static boolean equals(int[] ans1, int[] ans2) {
		if (ans1.length != ans2.length) {
			return false;
		}
		for (int i = 0; i < ans1.length; i++) {
			if (ans1[i] != ans2[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 对数器测试
	 */
	public static void main(String[] args) {
		int n = 15;
		int v = 40;
		int testTime = 5000;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int len = (int) (Math.random() * n) + 1;
			int[] nums = randomArray(len, v);
			int k = (int) (Math.random() * ((1 << len) - 1)) + 1;
			int[] ans1 = topKSum1(nums, k);
			int[] ans2 = topKSum2(nums, k);
			int[] ans3 = topKSum3(nums, k);
			if (!equals(ans1, ans2) || !equals(ans1, ans3)) {
				System.out.println("出错了！");
			}
		}
		System.out.println("测试结束");
	}

}
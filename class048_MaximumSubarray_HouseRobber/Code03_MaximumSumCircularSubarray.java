package class070;

/**
 * 环形数组的子数组最大累加和
 * 给定一个数组nums，长度为n
 * nums是一个环形数组，下标0和下标n-1是连在一起的
 * 返回环形数组中，子数组最大累加和
 * 测试链接 : https://leetcode.cn/problems/maximum-sum-circular-subarray/
 * 
 * 算法核心思想：
 * 1. 环形数组的最大子数组和有两种情况：
 *    a) 最大子数组不跨越数组边界（普通Kadane算法）
 *    b) 最大子数组跨越数组边界（总和减去最小子数组和）
 * 2. 关键观察：环形数组的最大子数组和 = max(最大子数组和, 总和 - 最小子数组和)
 * 3. 特殊情况：当所有元素都是负数时，最小子数组和等于总和，此时返回最大子数组和
 * 
 * 时间复杂度分析：
 * - 最优时间复杂度：O(n) - 只需遍历数组一次
 * - 空间复杂度：O(1) - 优化后只需常数空间
 * 
 * 工程化考量：
 * 1. 边界处理：处理空数组、单元素数组等特殊情况
 * 2. 异常防御：处理数值溢出等极端情况
 * 3. 性能优化：单次遍历同时计算最大和最小子数组和
 */
public class Code03_MaximumSumCircularSubarray {

	/**
	 * 计算环形数组的最大子数组和
	 * 
	 * 算法原理：
	 * - 情况1：最大子数组不跨越边界，即普通Kadane算法的结果
	 * - 情况2：最大子数组跨越边界，即总和减去最小子数组和
	 * - 特殊情况：当所有元素都是负数时，最小子数组和等于总和
	 * 
	 * 时间复杂度：O(n) - 单次遍历
	 * 空间复杂度：O(1) - 常数空间
	 * 
	 * @param nums 输入整数数组（环形）
	 * @return 环形数组的最大子数组和
	 * @throws IllegalArgumentException 如果输入数组为空
	 */
	public static int maxSubarraySumCircular(int[] nums) {
		// 边界检查
		if (nums == null || nums.length == 0) {
			throw new IllegalArgumentException("输入数组不能为空");
		}
		
		int n = nums.length;
		// 特殊情况：单元素数组
		if (n == 1) {
			return nums[0];
		}
		
		// 初始化变量
		int totalSum = nums[0];        // 数组总和
		int maxSum = nums[0];          // 最大子数组和（不跨越边界）
		int minSum = nums[0];          // 最小子数组和
		int currentMax = nums[0];      // 当前最大子数组和
		int currentMin = nums[0];      // 当前最小子数组和
		
		// 单次遍历同时计算最大和最小子数组和
		for (int i = 1; i < n; i++) {
			// 累加总和
			totalSum += nums[i];
			
			// 更新最大子数组和（Kadane算法）
			currentMax = Math.max(nums[i], currentMax + nums[i]);
			maxSum = Math.max(maxSum, currentMax);
			
			// 更新最小子数组和
			currentMin = Math.min(nums[i], currentMin + nums[i]);
			minSum = Math.min(minSum, currentMin);
		}
		
		// 特殊情况处理：如果所有元素都是负数
		// 此时minSum == totalSum，应该返回maxSum（最大的单个负数）
		if (totalSum == minSum) {
			return maxSum;
		}
		
		// 返回两种情况的最大值
		// 情况1：不跨越边界的最大子数组和（maxSum）
		// 情况2：跨越边界的最大子数组和（totalSum - minSum）
		return Math.max(maxSum, totalSum - minSum);
	}
	
	/**
	 * 主函数用于测试和演示
	 */
	public static void main(String[] args) {
		// 测试用例1: 普通环形数组（最大子数组跨越边界）
		int[] test1 = {5, -3, 5};
		System.out.println("测试用例1: " + java.util.Arrays.toString(test1));
		System.out.println("结果: " + maxSubarraySumCircular(test1)); // 期望: 10
		
		// 测试用例2: 全负数数组
		int[] test2 = {-3, -2, -1};
		System.out.println("测试用例2: " + java.util.Arrays.toString(test2));
		System.out.println("结果: " + maxSubarraySumCircular(test2)); // 期望: -1
		
		// 测试用例3: 普通数组（最大子数组不跨越边界）
		int[] test3 = {1, -2, 3, -2};
		System.out.println("测试用例3: " + java.util.Arrays.toString(test3));
		System.out.println("结果: " + maxSubarraySumCircular(test3)); // 期望: 3
		
		// 测试用例4: 单元素数组
		int[] test4 = {5};
		System.out.println("测试用例4: " + java.util.Arrays.toString(test4));
		System.out.println("结果: " + maxSubarraySumCircular(test4)); // 期望: 5
		
		// 测试用例5: 混合情况
		int[] test5 = {3, -1, 2, -1};
		System.out.println("测试用例5: " + java.util.Arrays.toString(test5));
		System.out.println("结果: " + maxSubarraySumCircular(test5)); // 期望: 4
	}
	
	/*
	 * 扩展思考与深度分析：
	 * 
	 * 1. 算法正确性证明：
	 *    - 环形数组的最大子数组和要么不跨越边界（普通Kadane算法）
	 *    - 要么跨越边界（总和减去最小子数组和）
	 *    - 这两种情况覆盖了所有可能性
	 * 
	 * 2. 特殊情况处理：
	 *    - 全负数数组：最小子数组和等于总和，此时返回最大单个元素
	 *    - 单元素数组：直接返回该元素
	 *    - 全正数数组：最大子数组和就是整个数组的和
	 * 
	 * 3. 工程应用场景：
	 *    - 环形缓冲区数据处理
	 *    - 周期性信号分析
	 *    - 循环队列的最大和计算
	 * 
	 * 4. 性能优化技巧：
	 *    - 单次遍历同时计算最大和最小子数组和
	 *    - 避免多次遍历数组
	 *    - 使用原地计算，减少空间开销
	 */
	
	/*
	 * 相关题目扩展:
	 * 1. LeetCode 918. 环形子数组的最大和 - https://leetcode.cn/problems/maximum-sum-circular-subarray/
	 * 2. LeetCode 53. 最大子数组和 - https://leetcode.cn/problems/maximum-subarray/
	 * 3. LeetCode 1186. 删除一次得到子数组最大和 - https://leetcode.cn/problems/maximum-subarray-sum-with-one-deletion/
	 * 4. LeetCode 152. 乘积最大子数组 - https://leetcode.cn/problems/maximum-product-subarray/
	 * 5. LeetCode 2104. 子数组范围和 - https://leetcode.cn/problems/sum-of-subarray-ranges/
	 * 6. LeetCode 1749. 任意子数组和的绝对值的最大值 - https://leetcode.cn/problems/maximum-absolute-sum-of-any-subarray/
	 * 7. LeetCode 1191. K 次串联后最大子数组之和 - https://leetcode.cn/problems/k-concatenation-maximum-sum/
	 * 8. LeetCode 363. 矩形区域不超过 K 的最大数值和 - https://leetcode.cn/problems/max-sum-of-rectangle-no-larger-than-k/
	 * 9. LeetCode 1074. 元素和为目标值的子矩阵数量 - https://leetcode.cn/problems/number-of-submatrices-that-sum-to-target/
	 * 10. LintCode 944. 最大子矩阵 - https://www.lintcode.com/problem/944/
	 */
}
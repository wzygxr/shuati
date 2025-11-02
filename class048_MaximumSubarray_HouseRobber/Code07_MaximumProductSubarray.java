package class070;

/**
 * 乘积最大子数组
 * 给你一个整数数组 nums
 * 请你找出数组中乘积最大的非空连续子数组
 * 并返回该子数组所对应的乘积
 * 测试链接 : https://leetcode.cn/problems/maximum-product-subarray/
 * 
 * 算法核心思想：
 * 1. 与最大子数组和问题不同，乘积问题需要考虑负数的特性
 * 2. 关键观察：负数乘以负数会变成正数，所以需要同时维护最大和最小乘积
 * 3. 使用动态规划思想，维护当前的最大乘积和最小乘积
 * 4. 对于每个元素，有三种选择：从当前元素重新开始、乘以之前的最小乘积、乘以之前的最大乘积
 * 
 * 时间复杂度分析：
 * - 最优时间复杂度：O(n) - 只需遍历数组一次
 * - 空间复杂度：O(1) - 优化后只需常数空间
 * 
 * 工程化考量：
 * 1. 数值溢出处理：使用double类型避免整数溢出
 * 2. 边界处理：处理空数组、单元素数组等特殊情况
 * 3. 性能优化：单次遍历同时维护最大和最小乘积
 */
public class Code07_MaximumProductSubarray {

	/**
	 * 计算乘积最大子数组的乘积值
	 * 
	 * 算法原理：
	 * - 同时维护当前的最大乘积(max)和最小乘积(min)
	 * - 对于每个元素nums[i]，有三种可能：
	 *   1. 从当前元素重新开始：nums[i]
	 *   2. 乘以之前的最小乘积：min * nums[i]（负数情况）
	 *   3. 乘以之前的最大乘积：max * nums[i]（正数情况）
	 * - 更新max和min，并记录全局最大值ans
	 * 
	 * 时间复杂度：O(n) - 单次遍历
	 * 空间复杂度：O(1) - 常数空间
	 * 
	 * @param nums 输入整数数组
	 * @return 乘积最大子数组的乘积值
	 * @throws IllegalArgumentException 如果输入数组为空
	 */
	public static int maxProduct(int[] nums) {
		// 边界检查
		if (nums == null || nums.length == 0) {
			throw new IllegalArgumentException("输入数组不能为空");
		}
		
		// 使用double类型避免整数溢出
		double ans = nums[0];      // 全局最大乘积
		double min = nums[0];      // 当前最小乘积
		double max = nums[0];      // 当前最大乘积
		double curmin, curmax;     // 临时变量
		
		// 从第二个元素开始遍历
		for (int i = 1; i < nums.length; i++) {
			// 计算当前元素可能产生的三种乘积
			curmin = Math.min(nums[i], Math.min(min * nums[i], max * nums[i]));
			curmax = Math.max(nums[i], Math.max(min * nums[i], max * nums[i]));
			
			// 更新状态
			min = curmin;
			max = curmax;
			
			// 更新全局最大值
			ans = Math.max(ans, max);
		}
		
		// 返回整数结果
		return (int) ans;
	}
	
	/**
	 * 整数版本：处理整数溢出的另一种方法
	 * 使用long类型避免溢出，适用于不需要小数精度的场景
	 * 
	 * @param nums 输入整数数组
	 * @return 乘积最大子数组的乘积值
	 */
	public static int maxProductLong(int[] nums) {
		if (nums == null || nums.length == 0) {
			throw new IllegalArgumentException("输入数组不能为空");
		}
		
		long ans = nums[0];
		long min = nums[0];
		long max = nums[0];
		long curmin, curmax;
		
		for (int i = 1; i < nums.length; i++) {
			curmin = Math.min(nums[i], Math.min(min * nums[i], max * nums[i]));
			curmax = Math.max(nums[i], Math.max(min * nums[i], max * nums[i]));
			
			min = curmin;
			max = curmax;
			ans = Math.max(ans, max);
		}
		
		return (int) ans;
	}
	
	/**
	 * 主函数用于测试和演示
	 */
	public static void main(String[] args) {
		// 测试用例1: 包含负数的数组
		int[] test1 = {2, 3, -2, 4};
		System.out.println("测试用例1: " + java.util.Arrays.toString(test1));
		System.out.println("结果: " + maxProduct(test1)); // 期望: 6
		
		// 测试用例2: 全负数数组
		int[] test2 = {-2, -3, -1, -4};
		System.out.println("测试用例2: " + java.util.Arrays.toString(test2));
		System.out.println("结果: " + maxProduct(test2)); // 期望: 12
		
		// 测试用例3: 包含0的数组
		int[] test3 = {-2, 0, -1};
		System.out.println("测试用例3: " + java.util.Arrays.toString(test3));
		System.out.println("结果: " + maxProduct(test3)); // 期望: 0
		
		// 测试用例4: 单元素数组
		int[] test4 = {5};
		System.out.println("测试用例4: " + java.util.Arrays.toString(test4));
		System.out.println("结果: " + maxProduct(test4)); // 期望: 5
		
		// 测试用例5: 复杂情况
		int[] test5 = {2, -5, -2, -4, 3};
		System.out.println("测试用例5: " + java.util.Arrays.toString(test5));
		System.out.println("结果: " + maxProduct(test5)); // 期望: 24
	}
	
	/*
	 * 扩展思考与深度分析：
	 * 
	 * 1. 算法正确性证明：
	 *    - 为什么需要同时维护最大和最小乘积？
	 *      因为负数乘以负数会变成正数，当前的最小乘积可能成为后续的最大乘积
	 *    - 为什么这种方法能处理所有情况？
	 *      考虑了三种可能：重新开始、乘以最大、乘以最小，覆盖了所有选择
	 * 
	 * 2. 数值溢出处理：
	 *    - 使用double类型：避免整数溢出，但可能损失精度
	 *    - 使用long类型：避免溢出，保持整数精度
	 *    - 实际选择：根据题目要求和数据范围决定
	 * 
	 * 3. 工程应用场景：
	 *    - 信号处理：寻找信号中的最大乘积段
	 *    - 金融分析：计算收益率的最大连续乘积
	 *    - 数据分析：寻找具有最大乘积的数据子集
	 * 
	 * 4. 性能优化技巧：
	 *    - 单次遍历：同时维护最大和最小乘积
	 *    - 空间优化：使用常数空间代替数组
	 *    - 提前终止：对于特殊情况的快速处理
	 */
	
	/*
	 * 相关题目扩展:
	 * 1. LeetCode 152. 乘积最大子数组 - https://leetcode.cn/problems/maximum-product-subarray/
	 * 2. LeetCode 53. 最大子数组和 - https://leetcode.cn/problems/maximum-subarray/
	 * 3. LeetCode 918. 环形子数组的最大和 - https://leetcode.cn/problems/maximum-sum-circular-subarray/
	 * 4. LeetCode 1186. 删除一次得到子数组最大和 - https://leetcode.cn/problems/maximum-subarray-sum-with-one-deletion/
	 * 5. LeetCode 198. 打家劫舍 - https://leetcode.cn/problems/house-robber/
	 * 6. LeetCode 628. 三个数的最大乘积 - https://leetcode.cn/problems/maximum-product-of-three-numbers/
	 * 7. LeetCode 713. 乘积小于K的子数组 - https://leetcode.cn/problems/subarray-product-less-than-k/
	 * 8. LeetCode 238. 除自身以外数组的乘积 - https://leetcode.cn/problems/product-of-array-except-self/
	 * 9. LeetCode 135. 分发糖果 - https://leetcode.cn/problems/candy/
	 * 10. LeetCode 42. 接雨水 - https://leetcode.cn/problems/trapping-rain-water/
	 */
}
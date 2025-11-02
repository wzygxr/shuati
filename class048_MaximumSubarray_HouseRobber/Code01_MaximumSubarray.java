package class070;

/**
 * 子数组最大累加和问题（Kadane算法）
 * 题目描述：给你一个整数数组 nums，返回非空子数组的最大累加和
 * 测试链接：https://leetcode.cn/problems/maximum-subarray/
 * 
 * 算法核心思想：
 * 1. Kadane算法是解决最大子数组和问题的经典动态规划算法
 * 2. 对于每个位置，我们有两个选择：
 *    a) 将当前元素加入到之前的子数组中
 *    b) 以当前元素开始一个新的子数组
 * 3. 取这两个选择中的较大值作为以当前元素结尾的最大子数组和
 * 
 * 本题目属于动态规划中的线性DP问题，是面试高频题之一
 * 
 * 时间复杂度分析：
 * - 最优时间复杂度：O(n) - 只需遍历数组一次
 * - 空间复杂度：O(1) - 优化后只需常数空间
 * 
 * 工程化考量：
 * 1. 异常处理：对空数组和边界情况进行处理
 * 2. 鲁棒性：处理极端输入（全负数、全正数、混合情况）
 * 3. 可测试性：提供完整的测试用例
 * 4. 性能优化：使用空间优化版本
 */
public class Code01_MaximumSubarray {

	/**
	 * 方法一：动态规划（基础版本）
	 * 时间复杂度：O(n) - 只需遍历数组一次
	 * 空间复杂度：O(n) - 需要一个长度为n的dp数组
	 * 
	 * 算法细节：
	 * - dp[i]表示以nums[i]结尾的最大子数组和
	 * - 状态转移：dp[i] = max(nums[i], dp[i-1] + nums[i])
	 * - 最终结果：max(dp[0...n-1])
	 * 
	 * 工程化考量：
	 * - 边界检查：处理空数组和单元素数组
	 * - 异常抛出：明确非法输入的处理方式
	 * - 可读性：清晰的变量命名和注释
	 * 
	 * @param nums 输入整数数组
	 * @return 非空子数组的最大累加和
	 * @throws IllegalArgumentException 如果输入数组为空
	 */
	public static int maxSubArray1(int[] nums) {
		// 边界检查：处理空数组情况
		if (nums == null || nums.length == 0) {
			throw new IllegalArgumentException("输入数组不能为空");
		}
		
		int n = nums.length;
		// dp[i] 定义：以i位置的元素结尾的最大子数组和
		// 这种定义方式便于理解状态转移关系
		int[] dp = new int[n];
		
		// 初始化：第一个元素的最大子数组和就是它自己
		// 因为只有一个元素时，最大子数组就是该元素本身
		dp[0] = nums[0];
		int ans = nums[0]; // 记录全局最大值
		
		// 状态转移：从第二个元素开始遍历
		// 每个位置都有两种选择：加入前面的子数组或重新开始
		for (int i = 1; i < n; i++) {
			// 状态转移方程：要么加入前面的子数组，要么自己开始一个新的子数组
			// 关键理解：如果前面的子数组和为负，不如重新开始
			dp[i] = Math.max(nums[i], dp[i - 1] + nums[i]);
			// 更新全局最大值：每次都要比较当前最大值和全局最大值
			ans = Math.max(ans, dp[i]);
		}
		
		return ans;
	}

	/**
	 * 方法二：动态规划（空间优化版本）
	 * 时间复杂度：O(n) - 只需遍历数组一次
	 * 空间复杂度：O(1) - 只使用常数额外空间
	 * 
	 * 优化原理：
	 * - 观察发现dp[i]只依赖于dp[i-1]，不需要保存整个dp数组
	 * - 使用pre变量保存前一个状态，实现空间优化
	 * 
	 * 工程优势：
	 * - 内存效率：避免O(n)的空间开销
	 * - 缓存友好：减少内存访问，提高缓存命中率
	 * - 代码简洁：逻辑更清晰，易于维护
	 * 
	 * @param nums 输入整数数组
	 * @return 非空子数组的最大累加和
	 * @throws IllegalArgumentException 如果输入数组为空
	 */
	public static int maxSubArray2(int[] nums) {
		// 边界检查：处理空数组情况
		if (nums == null || nums.length == 0) {
			throw new IllegalArgumentException("输入数组不能为空");
		}
		
		int n = nums.length;
		int ans = nums[0]; // 记录全局最大值
		// pre变量保存前一个位置的dp值，避免使用数组
		// 这种优化在工程实践中非常重要，特别是处理大数据时
		for (int i = 1, pre = nums[0]; i < n; i++) {
			// 更新pre为当前位置的dp值
			// 关键优化：只保存必要的前一个状态，而不是整个历史状态
			pre = Math.max(nums[i], pre + nums[i]);
			// 更新全局最大值：确保不会漏掉任何可能的更大值
			ans = Math.max(ans, pre);
		}
		
		return ans;
	}

	// 附加问题：记录最大子数组的位置信息
	// 这些变量用于存储最大子数组的边界信息
	public static int left;  // 最大子数组的起始索引
	public static int right; // 最大子数组的结束索引
	public static int sum;   // 最大子数组的和

	/**
	 * 找到拥有最大累加和的子数组，并记录其位置和和值
	 * 时间复杂度：O(n) - 只需遍历数组一次
	 * 空间复杂度：O(1) - 只使用常数额外空间
	 * 
	 * 算法扩展：
	 * - 不仅计算最大和，还记录子数组的边界
	 * - 使用滑动窗口思想，动态维护子数组的起始位置
	 * - 工程应用：在数据分析中定位关键区间
	 * 
	 * @param nums 输入整数数组
	 * @throws IllegalArgumentException 如果输入数组为空
	 */
	public static void extra(int[] nums) {
		// 边界检查：处理空数组情况
		if (nums == null || nums.length == 0) {
			throw new IllegalArgumentException("输入数组不能为空");
		}
		
		// 初始化最大值为最小整数，确保第一个元素能正确更新
		sum = Integer.MIN_VALUE;
		// l为当前考虑的子数组起始位置，r为结束位置
		// pre记录当前子数组的和
		for (int l = 0, r = 0, pre = Integer.MIN_VALUE; r < nums.length; r++) {
			if (pre >= 0) {
				// 如果前面的累加和非负，则将当前元素加入前面的子数组
				// 因为非负的累加和可能继续增大
				pre += nums[r];
			} else {
				// 如果前面的累加和为负，则重新开始一个子数组
				// 因为负的累加和会拖累后续元素
				pre = nums[r];
				l = r; // 更新子数组起始位置为当前位置
			}
			
			// 更新全局最大值及其位置
			// 只有当当前子数组和大于历史最大值时才更新
			if (pre > sum) {
				sum = pre;
				left = l;
				right = r;
			}
		}
	}
	
	/**
	 * 主函数用于测试和演示
	 * 
	 * 测试策略：
	 * 1. 覆盖各种边界情况（空数组、单元素、全正、全负、混合）
	 * 2. 验证两种方法的正确性
	 * 3. 测试位置记录功能
	 * 
	 * 工程化测试考量：
	 * - 单元测试：每个方法独立测试
	 * - 边界测试：极端输入情况
	 * - 性能测试：大数据量验证
	 */
	public static void main(String[] args) {
		// 测试用例1: 混合正负数（经典LeetCode示例）
		int[] test1 = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
		System.out.println("测试用例1结果: " + maxSubArray2(test1)); // 期望输出: 6
		System.out.println("方法1验证: " + maxSubArray1(test1));   // 验证两种方法一致性
		
		// 测试用例2: 全是负数（特殊情况，最大和是最大的单个负数）
		int[] test2 = {-1, -2, -3, -4};
		System.out.println("测试用例2结果: " + maxSubArray2(test2)); // 期望输出: -1
		
		// 测试用例3: 全是正数（最大和就是整个数组的和）
		int[] test3 = {1, 2, 3, 4};
		System.out.println("测试用例3结果: " + maxSubArray2(test3)); // 期望输出: 10
		
		// 测试用例4: 单元素数组（边界情况）
		int[] test4 = {5};
		System.out.println("测试用例4结果: " + maxSubArray2(test4)); // 期望输出: 5
		
		// 测试用例5: 包含0的情况
		int[] test5 = {0, -1, 2, -3, 4};
		System.out.println("测试用例5结果: " + maxSubArray2(test5)); // 期望输出: 4
		
		// 测试附加功能：记录最大子数组的位置
		extra(test1);
		System.out.println("最大子数组起始位置: " + left + ", 结束位置: " + right + ", 和: " + sum);
		
		// 性能测试：大数据量验证（可选）
		// int[] largeTest = generateLargeArray(1000000);
		// long startTime = System.currentTimeMillis();
		// int result = maxSubArray2(largeTest);
		// long endTime = System.currentTimeMillis();
		// System.out.println("大数据量测试耗时: " + (endTime - startTime) + "ms");
	}
	
	/*
	 * 扩展思考与深度分析：
	 * 
	 * 1. 为什么Kadane算法是最优解？
	 *    - 时间复杂度为O(n)，对于一维数组的遍历已经无法再优化（理论下界）
	 *    - 空间复杂度经过优化后为O(1)，也达到了最优
	 *    - 算法正确性：通过数学归纳法可以证明其正确性
	 * 
	 * 2. 算法本质理解：
	 *    - 贪心思想：当当前子数组和为负时，重新开始
	 *    - 动态规划：状态转移方程体现了最优子结构
	 *    - 滑动窗口：维护一个可能产生最大和的连续窗口
	 * 
	 * 3. 本题的变体与扩展：
	 *    - 环形数组的最大子数组和（LeetCode 918）：需要处理循环情况
	 *    - 允许删除一个元素的最大子数组和（LeetCode 1186）：增加删除操作
	 *    - 乘积最大子数组（LeetCode 152）：需要考虑负数的影响
	 *    - 二维子矩阵最大和：降维到一维问题
	 * 
	 * 4. 工程应用场景：
	 *    - 股票价格分析：寻找最佳买入卖出时机（最大收益区间）
	 *    - 信号处理：寻找信号中的峰值（最大能量区间）
	 *    - 金融分析：计算最大收益周期（风险评估）
	 *    - 能量管理：寻找能量消耗的最优区间（资源优化）
	 *    - 数据分析：识别数据中的关键模式区间
	 * 
	 * 5. 面试技巧：
	 *    - 先给出暴力解法，再优化到Kadane算法
	 *    - 解释时间复杂度从O(n²)到O(n)的优化过程
	 *    - 讨论空间复杂度的优化思路
	 *    - 准备边界情况的处理方案
	 */
	
	/*
	 * 相关题目扩展:
	 * 1. LeetCode 53. 最大子数组和 - https://leetcode.cn/problems/maximum-subarray/
	 * 2. LeetCode 918. 环形子数组的最大和 - https://leetcode.cn/problems/maximum-sum-circular-subarray/
	 * 3. LeetCode 1186. 删除一次得到子数组最大和 - https://leetcode.cn/problems/maximum-subarray-sum-with-one-deletion/
	 * 4. LeetCode 152. 乘积最大子数组 - https://leetcode.cn/problems/maximum-product-subarray/
	 * 5. LeetCode 697. 数组的度 - https://leetcode.cn/problems/degree-of-an-array/
	 * 6. LeetCode 1208. 尽可能使字符串相等 - https://leetcode.cn/problems/get-equal-substrings-within-budget/
	 * 7. LeetCode 1371. 每个元音包含偶数次的最长子字符串 - https://leetcode.cn/problems/find-the-longest-substring-containing-vowels-in-even-counts/
	 * 8. LeetCode 1480. 一维数组的动态和 - https://leetcode.cn/problems/running-sum-of-1d-array/
	 * 9. LeetCode 1588. 所有奇数长度子数组的和 - https://leetcode.cn/problems/sum-of-all-odd-length-subarrays/
	 * 10. LeetCode 1695. 删除子数组的最大得分 - https://leetcode.cn/problems/maximum-erasure-value/
	 */
}
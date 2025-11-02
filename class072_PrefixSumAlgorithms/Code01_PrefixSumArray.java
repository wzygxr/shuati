package class046;

/**
 * 区域和检索 - 数组不可变 (Range Sum Query - Immutable)
 * 
 * 题目描述:
 * 给定一个整数数组 nums，处理以下类型的多个查询:
 * 计算索引 left 和 right（包含 left 和 right）之间的 nums 元素的和，其中 left <= right。
 * 
 * 实现 NumArray 类：
 * - NumArray(int[] nums) 使用数组 nums 初始化对象
 * - int sumRange(int i, int j) 返回数组 nums 中索引 left 和 right 之间的元素的总和
 * 
 * 示例:
 * 输入:
 * ["NumArray", "sumRange", "sumRange", "sumRange"]
 * [[[-2, 0, 3, -5, 2, -1]], [0, 2], [2, 5], [0, 5]]
 * 输出:
 * [null, 1, -1, -3]
 * 
 * 解释:
 * NumArray numArray = new NumArray([-2, 0, 3, -5, 2, -1]);
 * numArray.sumRange(0, 2); // return 1 ((-2) + 0 + 3)
 * numArray.sumRange(2, 5); // return -1 (3 + (-5) + 2 + (-1))
 * numArray.sumRange(0, 5); // return -3 ((-2) + 0 + 3 + (-5) + 2 + (-1))
 * 
 * 提示:
 * 1 <= nums.length <= 10^4
 * -10^5 <= nums[i] <= 10^5
 * 0 <= i <= j < nums.length
 * 最多调用 10^4 次 sumRange 方法
 * 
 * 题目链接: https://leetcode.cn/problems/range-sum-query-immutable/
 * 
 * 解题思路:
 * 使用前缀和技巧预处理数组，使得每次查询可以在O(1)时间内完成。
 * 1. 构建前缀和数组sum，其中sum[i]表示nums[0]到nums[i-1]的和
 * 2. 区间[left, right]的和等于sum[right+1] - sum[left]
 * 
 * 时间复杂度: 
 * - 初始化: O(n) - 需要遍历数组一次构建前缀和数组
 * - 查询: O(1) - 每次查询只需要常数时间
 * 空间复杂度: O(n) - 需要额外的前缀和数组空间
 * 
 * 工程化考量:
 * 1. 边界条件处理：空数组、单元素数组
 * 2. 性能优化：预处理前缀和，查询时O(1)时间
 * 3. 空间优化：必须存储前缀和数组，无法避免
 * 4. 大数处理：元素值可能很大，需要确保整数范围
 * 
 * 最优解分析:
 * 这是最优解，因为查询次数可能很多，预处理后可以实现O(1)查询时间。
 * 对于频繁查询的场景，预处理是必要的。
 * 
 * 算法核心:
 * 前缀和公式：sum[i] = sum[i-1] + nums[i-1]
 * 区间和公式：sumRange(left, right) = sum[right+1] - sum[left]
 * 
 * 算法调试技巧:
 * 1. 打印中间过程：显示前缀和数组的计算过程
 * 2. 边界测试：测试空数组、单元素数组等特殊情况
 * 3. 性能测试：测试大规模数组下的性能表现
 * 
 * 语言特性差异:
 * Java中数组是对象，可以直接访问length属性。
 * 与C++相比，Java有自动内存管理，无需手动释放数组内存。
 * 与Python相比，Java是静态类型语言，需要显式声明数组类型。
 */
public class Code01_PrefixSumArray {

	/**
	 * NumArray类用于处理区域和查询
	 */
	class NumArray {

		public int[] sum;  // 前缀和数组

		/**
		 * 构造函数，初始化前缀和数组
		 * 
		 * @param nums 输入数组
		 * 
		 * 异常场景处理:
		 * - 空数组：创建空的前缀和数组
		 * - 单元素数组：正常处理
		 * 
		 * 边界条件:
		 * - 数组为空
		 * - 数组只有一行或一列
		 * - 查询范围超出数组边界
		 */
		public NumArray(int[] nums) {
			// 创建前缀和数组，大小为nums.length+1
			// 使用nums.length+1可以避免边界检查
			sum = new int[nums.length + 1];
			
			// 计算前缀和，时间复杂度O(n)
			for (int i = 1; i <= nums.length; i++) {
				// 前缀和公式：当前前缀和 = 前一个前缀和 + 当前元素
				sum[i] = sum[i - 1] + nums[i - 1];
				// 调试打印：显示前缀和计算过程
				// System.out.println("位置 " + i + ": sum[" + i + "] = " + sum[i]);
			}
		}

		/**
		 * 计算子数组区域和
		 * 
		 * @param left 左边界（包含）
		 * @param right 右边界（包含）
		 * @return 子数组元素和
		 * 
		 * 边界条件:
		 * - 坐标超出范围（题目保证有效）
		 * - 单元素查询
		 * - 整个数组查询
		 */
		public int sumRange(int left, int right) {
			// 使用前缀和公式计算区域和，时间复杂度O(1)
			// 公式：区间和 = 右边界前缀和 - 左边界前缀和
			int result = sum[right + 1] - sum[left];
			
			// 调试打印：显示查询过程
			// System.out.println("查询区域 [" + left + ", " + right + "]: 结果 = " + result);
			
			return result;
		}
	}

}

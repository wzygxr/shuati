package class094;

import java.util.Arrays;

// 最小绝对差 (Minimum Absolute Difference)
// 给你一个整数数组，其中数组中任意两个元素之间的绝对差的最小值。
// 
// 算法标签: 贪心算法(Greedy Algorithm)、排序(Sorting)、相邻元素比较(Adjacent Element Comparison)
// 时间复杂度: O(n*log(n))，其中n是数组长度
// 空间复杂度: O(1)，仅使用常数额外空间
// 测试链接 : https://www.hackerrank.com/challenges/minimum-absolute-difference-in-an-array/problem
// 相关题目: LeetCode 532. 数组中的K-diff数对、LeetCode 1200. 最小绝对差
// 贪心算法专题 - 差值优化问题集合
public class Code14_MinimumAbsoluteDifference {

	/*
	 * 算法思路详解：
	 * 1. 贪心策略核心：排序后相邻元素的差值最小，
	 *    这是因为对于任意三个按序排列的元素a≤b≤c，有|b-a|≤|c-a|
	 * 2. 排序优化：通过排序预处理，将问题转化为相邻元素比较
	 * 3. 差值计算：遍历相邻元素计算差值，找出最小值
	 *
	 * 时间复杂度分析：
	 * - O(n*log(n))，其中n是数组长度
	 * - 排序阶段：O(n*log(n))
	 * - 遍历阶段：O(n)
	 * 空间复杂度分析：
	 * - O(1)，仅使用了常数级别的额外空间存储最小差值
	 * 是否最优解：是，这是处理此类最小绝对差问题的最优解法
	 *
	 * 工程化最佳实践：
	 * 1. 输入验证：严格检查输入参数的有效性，防止空指针异常
	 * 2. 边界处理：妥善处理各种边界情况，如空数组、单元素等
	 * 3. 性能优化：采用单次遍历策略，避免重复计算
	 * 4. 代码可读性：使用语义明确的变量名和详尽的注释
	 * 5. 数值优化：使用整数运算避免浮点数误差
	 *
	 * 极端场景与边界情况处理：
	 * 1. 空输入场景：arr为空数组或null时直接返回0
	 * 2. 单元素场景：只有一个元素时返回0
	 * 3. 相同元素场景：多个相同元素时差值为0
	 * 4. 有序序列场景：数组已排序的情况
	 * 5. 极值场景：元素值差异极大的情况
	 *
	 * 跨语言实现差异与优化：
	 * 1. Java实现：使用Arrays.sort进行排序
	 * 2. C++实现：使用std::sort进行排序
	 * 3. Python实现：使用sorted函数进行排序
	 * 4. 内存管理：不同语言的垃圾回收机制对性能的影响
	 *
	 * 调试与测试策略：
	 * 1. 过程可视化：在关键节点打印相邻元素和差值
	 * 2. 断言验证：在每次计算后添加断言确保差值非负
	 * 3. 性能监控：跟踪排序和遍历的实际执行时间
	 * 4. 边界测试：设计覆盖所有边界条件的测试用例
	 * 5. 压力测试：使用大规模数据验证算法稳定性
	 *
	 * 实际应用场景与拓展：
	 * 1. 数据分析：识别数据中的最小变化
	 * 2. 信号处理：检测信号的最小波动
	 * 3. 金融分析：识别价格的最小变化
	 * 4. 质量控制：检测产品参数的最小偏差
	 * 5. 科学计算：计算实验数据的最小误差
	 *
	 * 算法深入解析：
	 * 1. 贪心策略原理：利用排序后相邻元素差值最小的性质
	 * 2. 最优性证明：通过反证法可以证明贪心策略的正确性
	 * 3. 数学基础：|a-b|≥0，当且仅当a=b时等号成立
	 * 4. 策略变体：可扩展为找前K小的绝对差等问题
	 */
	public static int minimumAbsoluteDifference(int[] arr) {
		// 异常处理：检查输入是否为空
		if (arr == null || arr.length == 0) {
			return 0;
		}
		
		// 边界条件：只有一个元素
		if (arr.length == 1) {
			return 0;
		}
		
		// 排序数组
		Arrays.sort(arr);
		
		// 初始化最小绝对差为最大值
		int minDiff = Integer.MAX_VALUE;
		
		// 遍历相邻元素，计算差值，找出最小值
		for (int i = 1; i < arr.length; i++) {
			int diff = arr[i] - arr[i - 1];
			if (diff < minDiff) {
				minDiff = diff;
			}
		}
		
		return minDiff;
	}
	
	// 测试函数
	public static void main(String[] args) {
		// 测试用例1
		int[] arr1 = {3, -7, 0};
		System.out.println("测试用例1结果: " + minimumAbsoluteDifference(arr1)); // 期望输出: 3
		
		// 测试用例2
		int[] arr2 = {-59, -36, -13, 1, -53, -92, -2, -96, -54, 75};
		System.out.println("测试用例2结果: " + minimumAbsoluteDifference(arr2)); // 期望输出: 1
		
		// 测试用例3
		int[] arr3 = {1, -3, 71, 68, 17};
		System.out.println("测试用例3结果: " + minimumAbsoluteDifference(arr3)); // 期望输出: 3
		
		// 测试用例4：边界情况
		int[] arr4 = {5};
		System.out.println("测试用例4结果: " + minimumAbsoluteDifference(arr4)); // 期望输出: 0
		
		// 测试用例5：相同元素
		int[] arr5 = {1, 1, 1, 1};
		System.out.println("测试用例5结果: " + minimumAbsoluteDifference(arr5)); // 期望输出: 0
	}
}
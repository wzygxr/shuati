package class092;

import java.util.Arrays;
import java.util.Comparator;

// 最大数
// 给定一组非负整数 nums，重新排列每个数的顺序（每个数不可拆分）使之组成一个最大的整数。
// 注意：输出结果可能非常大，所以你需要返回一个字符串而不是整数。
// 测试链接 : https://leetcode.cn/problems/largest-number/
public class Code11_LargestNumber {

	/*
	 * 贪心算法解法
	 * 
	 * 核心思想：
	 * 1. 为了组成最大的数，我们需要将数字按照特定的规则排序
	 * 2. 对于两个数字a和b，如果ab > ba（字符串拼接），则a应该排在b前面
	 * 3. 例如：对于数字3和30，330 > 303，所以3应该排在30前面
	 * 
	 * 算法步骤：
	 * 1. 将整数数组转换为字符串数组
	 * 2. 自定义排序规则：对于两个字符串a和b，如果a+b > b+a，则a排在b前面
	 * 3. 按照排序后的顺序拼接字符串
	 * 4. 处理特殊情况：如果结果以0开头且长度大于1，则返回"0"
	 * 
	 * 时间复杂度：O(n log n * m) - 其中n是数字个数，m是数字的平均长度，主要是排序的时间复杂度
	 * 空间复杂度：O(n * m) - 需要额外的字符串数组存储数字
	 * 
	 * 为什么这是最优解？
	 * 1. 贪心策略保证了每一步都做出了当前看起来最好的选择
	 * 2. 通过数学归纳法可以证明这种策略能得到全局最优解
	 * 3. 无法在更少的时间内完成，因为至少需要排序一遍数组
	 * 
	 * 工程化考虑：
	 * 1. 边界条件处理：空数组、全0数组
	 * 2. 异常处理：输入参数验证
	 * 3. 可读性：变量命名清晰，注释详细
	 * 
	 * 算法调试技巧：
	 * 1. 可以通过打印每一步的排序结果来观察排序过程
	 * 2. 用断言验证中间结果是否符合预期
	 * 
	 * 与机器学习的联系：
	 * 1. 这种自定义排序的思想在机器学习中也有应用，如自定义距离度量
	 * 2. 在特征工程中，有时需要自定义特征的排序规则
	 */

	public static String largestNumber(int[] nums) {
		// 边界条件：如果数组为空，返回"0"
		if (nums == null || nums.length == 0) {
			return "0";
		}

		// 将整数数组转换为字符串数组
		String[] strs = new String[nums.length];
		for (int i = 0; i < nums.length; i++) {
			strs[i] = String.valueOf(nums[i]);
		}

		// 自定义排序规则：对于两个字符串a和b，如果a+b > b+a，则a排在b前面
		Arrays.sort(strs, new Comparator<String>() {
			@Override
			public int compare(String a, String b) {
				// 注意这里是降序排列，所以返回值要取反
				return (b + a).compareTo(a + b);
			}
		});

		// 拼接结果
		StringBuilder result = new StringBuilder();
		for (String str : strs) {
			result.append(str);
		}

		// 处理特殊情况：如果结果以0开头且长度大于1，则返回"0"
		// 例如输入[0,0]，结果应该是"0"而不是"00"
		if (result.charAt(0) == '0') {
			return "0";
		}

		return result.toString();
	}

	// 测试方法
	public static void main(String[] args) {
		// 测试用例1: [10,2] -> "210"
		int[] nums1 = { 10, 2 };
		System.out.println("测试用例1: " + Arrays.toString(nums1));
		System.out.println("预期结果: \"210\", 实际结果: \"" + largestNumber(nums1) + "\"");
		System.out.println();

		// 测试用例2: [3,30,34,5,9] -> "9534330"
		int[] nums2 = { 3, 30, 34, 5, 9 };
		System.out.println("测试用例2: " + Arrays.toString(nums2));
		System.out.println("预期结果: \"9534330\", 实际结果: \"" + largestNumber(nums2) + "\"");
		System.out.println();

		// 测试用例3: [1] -> "1"
		int[] nums3 = { 1 };
		System.out.println("测试用例3: " + Arrays.toString(nums3));
		System.out.println("预期结果: \"1\", 实际结果: \"" + largestNumber(nums3) + "\"");
		System.out.println();

		// 测试用例4: [0,0] -> "0"
		int[] nums4 = { 0, 0 };
		System.out.println("测试用例4: " + Arrays.toString(nums4));
		System.out.println("预期结果: \"0\", 实际结果: \"" + largestNumber(nums4) + "\"");
		System.out.println();

		// 测试用例5: [0] -> "0"
		int[] nums5 = { 0 };
		System.out.println("测试用例5: " + Arrays.toString(nums5));
		System.out.println("预期结果: \"0\", 实际结果: \"" + largestNumber(nums5) + "\"");
		System.out.println();

		// 测试用例6: [432,43] -> "43432"
		int[] nums6 = { 432, 43 };
		System.out.println("测试用例6: " + Arrays.toString(nums6));
		System.out.println("预期结果: \"43432\", 实际结果: \"" + largestNumber(nums6) + "\"");
		System.out.println();
	}
}
package class053;

import java.util.*;

// 下一个更大元素 II
// 给定一个循环数组 nums（nums[nums.length - 1] 的下一个元素是 nums[0]），返回 nums 中每个元素的下一个更大元素。
// 数字 x 的下一个更大的元素是按数组遍历顺序，这个数字之后的第一个比它更大的数，这意味着你应该循环地搜索它的下一个更大的数。
// 如果不存在，则输出 -1。
// 测试链接 : https://leetcode.cn/problems/next-greater-element-ii/
public class Code08_NextGreaterElementII {

	/*
	 * 解题思路：
	 * 这是一个循环数组问题，可以遍历数组两次来模拟循环效果。
	 * 使用单调递减栈来解决，栈中存储数组索引。
	 * 
	 * 具体步骤：
	 * 1. 初始化结果数组，所有元素默认为 -1
	 * 2. 遍历数组两次（即遍历 2 * n 次），使用取模运算处理索引：
	 *    - 如果当前元素比栈顶索引对应的元素大，说明找到了栈顶元素的下一个更大元素
	 *    - 弹出栈顶元素，并更新结果数组中对应位置的值
	 *    - 重复此过程直到栈为空或栈顶索引对应元素不小于当前元素
	 *    - 只有在第一遍遍历时才将索引入栈
	 * 
	 * 时间复杂度分析：
	 * O(n) - 虽然遍历了两次数组，但每个元素最多入栈和出栈各一次
	 * 
	 * 空间复杂度分析：
	 * O(n) - 栈的空间最多为n
	 * 
	 * 是否为最优解：
	 * 是，这是解决该问题的最优解
	 */

	public static int[] nextGreaterElements(int[] nums) {
		if (nums == null || nums.length == 0) {
			return new int[0];
		}

		int n = nums.length;
		// 初始化结果数组，所有元素默认为 -1
		int[] result = new int[n];
		Arrays.fill(result, -1);

		// 使用单调递减栈，存储数组索引
		Stack<Integer> stack = new Stack<>();

		// 遍历数组两次来处理循环数组
		for (int i = 0; i < 2 * n; i++) {
			int num = nums[i % n];
			// 如果当前元素比栈顶索引对应的元素大，说明找到了栈顶元素的下一个更大元素
			while (!stack.isEmpty() && nums[stack.peek()] < num) {
				// 弹出栈顶元素，并更新结果数组中对应位置的值
				result[stack.pop()] = num;
			}
			// 只有在第一遍遍历时才将索引入栈
			if (i < n) {
				stack.push(i);
			}
		}

		return result;
	}

	// 测试用例
	public static void main(String[] args) {
		// 测试用例1
		int[] nums1 = {1, 2, 1};
		System.out.println("测试用例1输出: " + Arrays.toString(nextGreaterElements(nums1))); 
		// 期望输出: [2, -1, 2]

		// 测试用例2
		int[] nums2 = {1, 2, 3, 4, 3};
		System.out.println("测试用例2输出: " + Arrays.toString(nextGreaterElements(nums2))); 
		// 期望输出: [2, 3, 4, -1, 4]
	}
}
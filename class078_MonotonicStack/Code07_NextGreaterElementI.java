package class053;

import java.util.*;

// 下一个更大元素 I
// nums1 中数字 x 的下一个更大元素是指 x 在 nums2 中对应位置右侧的第一个比 x 大的元素。
// 给你两个没有重复元素的数组 nums1 和 nums2，下标从 0 开始计数，其中 nums1 是 nums2 的子集。
// 对于每个 0 <= i < nums1.length，找出满足 nums1[i] == nums2[j] 的下标 j，并且在 nums2 中确定 nums2[j] 的下一个更大元素。
// 如果不存在下一个更大元素，那么本次查询的答案是 -1。
// 返回一个长度为 nums1.length 的数组 ans 作为答案，满足 ans[i] 是如上所述的下一个更大元素。
// 测试链接 : https://leetcode.cn/problems/next-greater-element-i/
public class Code07_NextGreaterElementI {

	/*
	 * 解题思路：
	 * 使用单调栈来解决这个问题。我们首先处理 nums2 数组，找出每个元素的下一个更大元素，
	 * 并将结果存储在哈希表中。然后遍历 nums1 数组，通过哈希表快速获取结果。
	 * 
	 * 具体步骤：
	 * 1. 遍历 nums2 数组，使用单调递减栈：
	 *    - 如果当前元素比栈顶元素大，说明找到了栈顶元素的下一个更大元素
	 *    - 弹出栈顶元素，并将其与当前元素的映射关系存储在哈希表中
	 *    - 重复此过程直到栈为空或栈顶元素不小于当前元素
	 *    - 将当前元素入栈
	 * 2. 遍历完 nums2 后，栈中剩余的元素都没有下一个更大元素，它们在哈希表中的值为 -1
	 * 3. 遍历 nums1 数组，通过哈希表获取每个元素的下一个更大元素
	 * 
	 * 时间复杂度分析：
	 * O(m + n) - m 是 nums1 的长度，n 是 nums2 的长度，每个元素最多入栈和出栈各一次
	 * 
	 * 空间复杂度分析：
	 * O(n) - 栈和哈希表的空间最多为n
	 * 
	 * 是否为最优解：
	 * 是，这是解决该问题的最优解
	 */

	public static int[] nextGreaterElement(int[] nums1, int[] nums2) {
		// 使用单调递减栈和哈希表
		Stack<Integer> stack = new Stack<>();
		Map<Integer, Integer> map = new HashMap<>();

		// 处理 nums2，找出每个元素的下一个更大元素
		for (int num : nums2) {
			// 如果当前元素比栈顶元素大，说明找到了栈顶元素的下一个更大元素
			while (!stack.isEmpty() && num > stack.peek()) {
				// 弹出栈顶元素，并建立映射关系
				map.put(stack.pop(), num);
			}
			// 将当前元素入栈
			stack.push(num);
		}

		// 栈中剩余的元素都没有下一个更大元素，已经在哈希表中默认为 -1

		// 构建结果数组
		int[] result = new int[nums1.length];
		for (int i = 0; i < nums1.length; i++) {
			// 通过哈希表获取下一个更大元素，如果没有则默认为 -1
			result[i] = map.getOrDefault(nums1[i], -1);
		}

		return result;
	}

	// 测试用例
	public static void main(String[] args) {
		// 测试用例1
		int[] nums1_1 = {4, 1, 2};
		int[] nums2_1 = {1, 3, 4, 2};
		System.out.println("测试用例1输出: " + Arrays.toString(nextGreaterElement(nums1_1, nums2_1))); 
		// 期望输出: [-1, 3, -1]

		// 测试用例2
		int[] nums1_2 = {2, 4};
		int[] nums2_2 = {1, 2, 3, 4};
		System.out.println("测试用例2输出: " + Arrays.toString(nextGreaterElement(nums1_2, nums2_2))); 
		// 期望输出: [3, -1]
	}
}
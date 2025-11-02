package class053;

import java.util.*;

// 柱状图中最大的矩形
// 给定 n 个非负整数，用来表示柱状图中各个柱子的高度。每个柱子彼此相邻，且宽度为 1 。
// 求在该柱状图中，能够勾勒出来的矩形的最大面积。
// 测试链接 : https://leetcode.cn/problems/largest-rectangle-in-histogram/
public class Code06_LargestRectangleInHistogram {

	/*
	 * 解题思路：
	 * 使用单调栈来解决这个问题。维护一个单调递增的栈，栈中存储的是数组的索引。
	 * 对于每个柱子，我们希望找到它左边和右边第一个比它矮的柱子，这样就能确定以当前柱子高度为高的最大矩形。
	 * 
	 * 具体步骤：
	 * 1. 遍历数组中的每个元素
	 * 2. 如果当前元素比栈顶元素对应的高度小，说明找到了栈顶元素右边第一个比它小的元素：
	 *    - 弹出栈顶元素作为当前考虑的矩形高度
	 *    - 当前元素索引就是右边界
	 *    - 新的栈顶元素索引+1就是左边界
	 *    - 计算面积并更新最大面积
	 * 3. 将当前元素索引入栈
	 * 4. 遍历完所有元素后，处理栈中剩余的元素
	 * 
	 * 时间复杂度分析：
	 * O(n) - 每个元素最多入栈和出栈各一次
	 * 
	 * 空间复杂度分析：
	 * O(n) - 栈的空间最多为n
	 * 
	 * 是否为最优解：
	 * 是，这是解决该问题的最优解之一
	 */

	public static int largestRectangleArea(int[] heights) {
		if (heights == null || heights.length == 0) {
			return 0;
		}

		// 使用栈存储索引，维护单调递增栈
		Stack<Integer> stack = new Stack<>();
		int maxArea = 0;

		for (int i = 0; i < heights.length; i++) {
			// 当前高度小于栈顶索引对应的高度时，开始计算面积
			while (!stack.isEmpty() && heights[i] < heights[stack.peek()]) {
				// 弹出栈顶元素作为矩形高度
				int height = heights[stack.pop()];
				// 右边界就是当前索引
				int right = i;
				// 左边界是新的栈顶元素索引+1，如果栈为空，则左边界为0
				int left = stack.isEmpty() ? 0 : stack.peek() + 1;
				// 计算宽度
				int width = right - left;
				// 计算面积并更新最大面积
				maxArea = Math.max(maxArea, height * width);
			}
			// 将当前索引入栈
			stack.push(i);
		}

		// 处理栈中剩余的元素
		while (!stack.isEmpty()) {
			// 弹出栈顶元素作为矩形高度
			int height = heights[stack.pop()];
			// 右边界是数组长度
			int right = heights.length;
			// 左边界是新的栈顶元素索引+1，如果栈为空，则左边界为0
			int left = stack.isEmpty() ? 0 : stack.peek() + 1;
			// 计算宽度
			int width = right - left;
			// 计算面积并更新最大面积
			maxArea = Math.max(maxArea, height * width);
		}

		return maxArea;
	}

	// 测试用例
	public static void main(String[] args) {
		// 测试用例1
		int[] heights1 = {2, 1, 5, 6, 2, 3};
		System.out.println("测试用例1输出: " + largestRectangleArea(heights1)); // 期望输出: 10

		// 测试用例2
		int[] heights2 = {2, 4};
		System.out.println("测试用例2输出: " + largestRectangleArea(heights2)); // 期望输出: 4
	}
}
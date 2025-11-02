package class053;

import java.util.*;

// 每日温度
// 给定一个整数数组 temperatures ，表示每天的温度，返回一个数组 answer ，
// 其中 answer[i] 是指对于第 i 天，下一个更高温度出现在几天后。
// 如果气温在这之后都不会升高，请在该位置用 0 来代替。
// 测试链接 : https://leetcode.cn/problems/daily-temperatures/
public class Code09_DailyTemperatures {

	/*
	 * 解题思路：
	 * 使用单调递减栈来解决这个问题，栈中存储数组索引。
	 * 对于每个温度，我们找到下一个更高温度出现在几天后。
	 * 
	 * 具体步骤：
	 * 1. 初始化结果数组，所有元素默认为 0
	 * 2. 遍历温度数组：
	 *    - 如果当前温度比栈顶索引对应的温度高，说明找到了栈顶索引的下一个更高温度
	 *    - 弹出栈顶元素，计算天数差并更新结果数组中对应位置的值
	 *    - 重复此过程直到栈为空或栈顶索引对应温度不小于当前温度
	 *    - 将当前索引入栈
	 * 
	 * 时间复杂度分析：
	 * O(n) - 每个元素最多入栈和出栈各一次
	 * 
	 * 空间复杂度分析：
	 * O(n) - 栈的空间最多为n
	 * 
	 * 是否为最优解：
	 * 是，这是解决该问题的最优解
	 */

	public static int[] dailyTemperatures(int[] temperatures) {
		if (temperatures == null || temperatures.length == 0) {
			return new int[0];
		}

		int n = temperatures.length;
		// 初始化结果数组，所有元素默认为 0
		int[] result = new int[n];

		// 使用单调递减栈，存储数组索引
		Stack<Integer> stack = new Stack<>();

		for (int i = 0; i < n; i++) {
			// 如果当前温度比栈顶索引对应的温度高，说明找到了栈顶索引的下一个更高温度
			while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
				// 弹出栈顶元素，计算天数差并更新结果数组中对应位置的值
				int index = stack.pop();
				result[index] = i - index;
			}
			// 将当前索引入栈
			stack.push(i);
		}

		return result;
	}

	// 测试用例
	public static void main(String[] args) {
		// 测试用例1
		int[] temperatures1 = {73, 74, 75, 71, 69, 72, 76, 73};
		System.out.println("测试用例1输出: " + Arrays.toString(dailyTemperatures(temperatures1))); 
		// 期望输出: [1, 1, 4, 2, 1, 1, 0, 0]

		// 测试用例2
		int[] temperatures2 = {30, 40, 50, 60};
		System.out.println("测试用例2输出: " + Arrays.toString(dailyTemperatures(temperatures2))); 
		// 期望输出: [1, 1, 1, 0]

		// 测试用例3
		int[] temperatures3 = {30, 60, 90};
		System.out.println("测试用例3输出: " + Arrays.toString(dailyTemperatures(temperatures3))); 
		// 期望输出: [1, 1, 0]
	}
}
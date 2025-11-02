package class092;

import java.util.Deque;
import java.util.LinkedList;

// 移掉K位数字
// 给你一个以字符串表示的非负整数 num 和一个整数 k ，移除这个数中的 k 位数字，
// 使得剩下的数字最小。请你以字符串形式返回这个最小的数字。
// 测试链接 : https://leetcode.cn/problems/remove-k-digits/
public class Code15_RemoveKDigits {

	/*
	 * 贪心算法 + 单调栈解法
	 * 
	 * 核心思想：
	 * 1. 使用贪心策略，每次尽可能选择小的数字放在高位
	 * 2. 使用单调栈维护当前已选择的数字
	 * 3. 遍历数字，对于每个数字，如果它比栈顶元素小，且还有删除次数，则弹出栈顶元素
	 * 4. 最后如果还有删除次数，从栈顶删除剩余的数字
	 * 
	 * 时间复杂度：O(n) - n是字符串的长度，每个字符最多入栈和出栈一次
	 * 空间复杂度：O(n) - 单调栈的空间复杂度
	 * 
	 * 为什么这是最优解？
	 * 1. 贪心策略保证了高位尽可能小，从而得到全局最小的数字
	 * 2. 单调栈的使用使得我们能够高效地维护当前的最优选择
	 * 3. 无法在更少的时间内完成，因为需要处理每个数字
	 * 
	 * 工程化考虑：
	 * 1. 边界条件处理：空字符串、k=0、k等于字符串长度等
	 * 2. 前导零处理：移除结果字符串开头的零
	 * 3. 空结果处理：如果结果为空，返回"0"
	 * 
	 * 算法调试技巧：
	 * 1. 可以打印每一步栈的状态来观察算法执行过程
	 * 2. 注意处理各种边界情况
	 */

	public static String removeKdigits(String num, int k) {
		// 边界条件：如果字符串为空或者k为0，直接返回原字符串
		if (num == null || num.isEmpty() || k == 0) {
			return num;
		}

		// 边界条件：如果k等于或大于字符串长度，返回"0"
		if (k >= num.length()) {
			return "0";
		}

		// 使用双端队列实现单调栈
		Deque<Character> stack = new LinkedList<>();

		// 遍历每个数字
		for (int i = 0; i < num.length(); i++) {
			char digit = num.charAt(i);

			// 当栈不为空，当前数字比栈顶元素小，且还有删除次数时，弹出栈顶元素
			while (!stack.isEmpty() && digit < stack.peek() && k > 0) {
				stack.pop();
				k--;
			}

			// 将当前数字入栈
			stack.push(digit);
		}

		// 如果还有剩余的删除次数，从栈顶删除
		while (k > 0) {
			stack.pop();
			k--;
		}

		// 构建结果字符串，注意需要反转，因为我们是从后往前构建的
		StringBuilder sb = new StringBuilder();
		while (!stack.isEmpty()) {
			sb.append(stack.pop());
		}
		sb.reverse();

		// 移除前导零
		int startIndex = 0;
		while (startIndex < sb.length() && sb.charAt(startIndex) == '0') {
			startIndex++;
		}

		// 如果结果为空，返回"0"
		if (startIndex == sb.length()) {
			return "0";
		}

		return sb.substring(startIndex);
	}

	// 优化版本，更简洁的实现
	public static String removeKdigitsOptimized(String num, int k) {
		// 边界条件处理
		if (k >= num.length()) return "0";

		// 使用字符数组模拟栈，避免反转操作
		char[] stack = new char[num.length()];
		int stackSize = 0;

		// 遍历每个数字
		for (char c : num.toCharArray()) {
			// 贪心策略：移除较大的高位数字
			while (stackSize > 0 && stack[stackSize - 1] > c && k > 0) {
				stackSize--;
				k--;
			}
			stack[stackSize++] = c;
		}

		// 移除剩余需要删除的数字（从末尾）
		stackSize -= k;

		// 移除前导零
		int start = 0;
		while (start < stackSize && stack[start] == '0') {
			start++;
		}

		// 处理特殊情况
		if (start == stackSize) return "0";

		return new String(stack, start, stackSize - start);
	}

	// 测试方法
	public static void main(String[] args) {
		// 测试用例1: num = "1432219", k = 3 -> "1219"
		String num1 = "1432219";
		int k1 = 3;
		System.out.println("测试用例1: num = " + num1 + ", k = " + k1);
		System.out.println("预期结果: 1219, 实际结果: " + removeKdigits(num1, k1));
		System.out.println("优化版本结果: " + removeKdigitsOptimized(num1, k1));
		System.out.println();

		// 测试用例2: num = "10200", k = 1 -> "200"
		String num2 = "10200";
		int k2 = 1;
		System.out.println("测试用例2: num = " + num2 + ", k = " + k2);
		System.out.println("预期结果: 200, 实际结果: " + removeKdigits(num2, k2));
		System.out.println("优化版本结果: " + removeKdigitsOptimized(num2, k2));
		System.out.println();

		// 测试用例3: num = "10", k = 2 -> "0"
		String num3 = "10";
		int k3 = 2;
		System.out.println("测试用例3: num = " + num3 + ", k = " + k3);
		System.out.println("预期结果: 0, 实际结果: " + removeKdigits(num3, k3));
		System.out.println("优化版本结果: " + removeKdigitsOptimized(num3, k3));
		System.out.println();

		// 测试用例4: num = "10", k = 1 -> "0"
		String num4 = "10";
		int k4 = 1;
		System.out.println("测试用例4: num = " + num4 + ", k = " + k4);
		System.out.println("预期结果: 0, 实际结果: " + removeKdigits(num4, k4));
		System.out.println("优化版本结果: " + removeKdigitsOptimized(num4, k4));
		System.out.println();

		// 测试用例5: num = "112", k = 1 -> "11"
		String num5 = "112";
		int k5 = 1;
		System.out.println("测试用例5: num = " + num5 + ", k = " + k5);
		System.out.println("预期结果: 11, 实际结果: " + removeKdigits(num5, k5));
		System.out.println("优化版本结果: " + removeKdigitsOptimized(num5, k5));
		System.out.println();

		// 测试用例6: 空输入
		System.out.println("测试用例6: num = null, k = 0");
		System.out.println("预期结果: null, 实际结果: " + removeKdigits(null, 0));
		System.out.println();

		// 测试用例7: k=0
		String num7 = "12345";
		int k7 = 0;
		System.out.println("测试用例7: num = " + num7 + ", k = " + k7);
		System.out.println("预期结果: 12345, 实际结果: " + removeKdigits(num7, k7));
		System.out.println("优化版本结果: " + removeKdigitsOptimized(num7, k7));
	}
}
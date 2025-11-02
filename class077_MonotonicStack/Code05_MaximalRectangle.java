package class052;

import java.util.Arrays;

// 最大矩形
// 给定一个仅包含 0 和 1 、大小为 rows * cols 的二维二进制矩阵
// 找出只包含 1 的最大矩形，并返回其面积
// 测试链接：https://leetcode.cn/problems/maximal-rectangle/

// 补充题目1: Maximal Square（最大正方形）
// 问题描述：在一个由 '0' 和 '1' 组成的二维矩阵中，找到只包含 '1' 的最大正方形，并返回其面积。
// 解题思路：这道题可以用动态规划解决，但也可以用单调栈方法。对每一行计算高度数组，
// 然后对每个位置计算以该位置为右下角的最大正方形边长。
// 时间复杂度：O(rows * cols)
// 空间复杂度：O(cols)
// 测试链接：https://leetcode.cn/problems/maximal-square/

// 补充题目2: Count Square Submatrices with All Ones（统计全为 1 的正方形子矩阵）
// 问题描述：给你一个 m * n 的矩阵，矩阵中的元素不是 0 就是 1，
// 请你统计并返回其中完全由 1 组成的正方形子矩阵的个数。
// 解题思路：对每个位置计算以该位置为右下角的最大正方形边长，然后累加所有边长。
// 时间复杂度：O(m * n)
// 空间复杂度：O(n)
// 测试链接：https://leetcode.cn/problems/count-square-submatrices-with-all-ones/

// 补充题目3: 85. Maximal Rectangle（最大矩形）
// 问题描述：同主题目，另一种描述
// 解题思路：逐行构建柱状图，然后使用单调栈求解每行对应的柱状图中的最大矩形面积
// 时间复杂度：O(rows * cols)
// 空间复杂度：O(cols)

// 补充题目4: 221. Maximal Square（最大正方形）
// 问题描述：同补充题目1，另一种描述
// 测试链接：https://leetcode.cn/problems/maximal-square/

// 补充题目5: 1277. Count Square Submatrices with All Ones（统计全为 1 的正方形子矩阵）
// 问题描述：同补充题目2，另一种描述
// 测试链接：https://leetcode.cn/problems/count-square-submatrices-with-all-ones/

// 补充题目6: 363. Max Sum of Rectangle No Larger Than K（矩形区域不超过 K 的最大数值和）
// 问题描述：给你一个 m x n 的矩阵 matrix 和一个整数 k ，找出并返回矩阵内部矩形区域的不超过 k 的最大数值和。
// 解题思路：使用二维前缀和和二分查找，或者逐行处理结合单调栈
// 时间复杂度：O(m^2 * n log n)
// 空间复杂度：O(n)
// 测试链接：https://leetcode.cn/problems/max-sum-of-rectangle-no-larger-than-k/

// 补充题目7: 844. Backspace String Compare（比较含退格的字符串）
// 问题描述：给定 s 和 t 两个字符串，当它们分别被输入到空白的文本编辑器后，如果两者相等，返回 true 。
// '#' 代表退格字符。
// 解题思路：使用栈模拟字符串处理过程。
// 时间复杂度：O(n + m)
// 空间复杂度：O(n + m)
// 测试链接：https://leetcode.cn/problems/backspace-string-compare/

// 补充题目8: 739. Daily Temperatures（每日温度）
// 问题描述：给定一个整数数组 temperatures ，表示每天的温度，返回一个数组 answer，
// 其中 answer[i] 是指对于第 i 天，下一个更高温度出现在几天后。如果气温在这之后都不会升高，请在该位置用 0 来代替。
// 解题思路：使用单调栈从右向左或从左向右遍历。
// 时间复杂度：O(n)
// 空间复杂度：O(n)
// 测试链接：https://leetcode.cn/problems/daily-temperatures/

public class Code05_MaximalRectangle {

	public static int MAXN = 201;

	public static int[] height = new int[MAXN];

	public static int[] stack = new int[MAXN];

	public static int r;

	// 最大矩形（二进制矩阵）的最优解
	// 时间复杂度: O(rows * cols)，对每一行应用单调栈算法
	// 空间复杂度: O(cols)，用于存储高度数组和栈
	public static int maximalRectangle(char[][] grid) {
		// 参数检查
		if (grid == null || grid.length == 0 || grid[0].length == 0) {
			return 0;
		}
		
		int rows = grid.length;
		int cols = grid[0].length;
		Arrays.fill(height, 0, cols, 0); // 初始化高度数组
		int maxArea = 0;
		
		// 逐行构建高度数组并计算最大矩形
		for (int i = 0; i < rows; i++) {
			// 更新高度数组：当前位置是0则重置为0，否则高度加1
			for (int j = 0; j < cols; j++) {
				height[j] = grid[i][j] == '0' ? 0 : height[j] + 1;
			}
			// 对当前高度数组应用单调栈算法计算最大矩形面积
			maxArea = Math.max(maxArea, largestRectangleArea(cols));
		}
		return maxArea;
	}

	// 柱状图中最大矩形面积的单调栈解法
	// 时间复杂度: O(n)
	// 空间复杂度: O(n)
	public static int largestRectangleArea(int m) {
		r = 0; // 重置栈顶指针
		int maxArea = 0;
		
		// 遍历每个元素，维护单调递增栈
		for (int i = 0; i < m; i++) {
			// 当栈不为空且当前高度小于等于栈顶高度时
			while (r > 0 && height[stack[r - 1]] >= height[i]) {
				int top = stack[--r]; // 弹出栈顶元素
				// 计算宽度：当前索引减去新的栈顶索引减1（如果栈为空则宽度为i）
				int width = r == 0 ? i : i - stack[r - 1] - 1;
				// 更新最大面积
				maxArea = Math.max(maxArea, height[top] * width);
			}
			// 将当前索引入栈
			stack[r++] = i;
		}
		
		// 处理栈中剩余元素
		while (r > 0) {
			int top = stack[--r]; // 弹出栈顶元素
			// 计算宽度：数组长度减去新的栈顶索引减1（如果栈为空则宽度为m）
			int width = r == 0 ? m : m - stack[r - 1] - 1;
			// 更新最大面积
			maxArea = Math.max(maxArea, height[top] * width);
		}
		return maxArea;
	}

	// 最大正方形（动态规划解法）
	// 时间复杂度: O(rows * cols)
	// 空间复杂度: O(rows * cols)
	public static int maximalSquare(char[][] matrix) {
		if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
			return 0;
		}
		
		int rows = matrix.length;
		int cols = matrix[0].length;
		// dp[i][j] 表示以(i,j)为右下角的最大正方形边长
		int[][] dp = new int[rows][cols];
		int maxSide = 0;
		
		// 初始化并填充dp数组
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (matrix[i][j] == '1') {
					if (i == 0 || j == 0) {
						dp[i][j] = 1; // 边界情况，第一行或第一列
					} else {
						// 取左、上、左上三个位置的最小值加1
						dp[i][j] = Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]) + 1;
					}
					maxSide = Math.max(maxSide, dp[i][j]);
				}
			}
		}
		return maxSide * maxSide; // 返回面积
	}

	// 统计全为1的正方形子矩阵数量（动态规划解法）
	// 时间复杂度: O(m * n)
	// 空间复杂度: O(m * n)
	public static int countSquares(char[][] matrix) {
		if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
			return 0;
		}
		
		int rows = matrix.length;
		int cols = matrix[0].length;
		// dp[i][j] 表示以(i,j)为右下角的最大正方形边长
		int[][] dp = new int[rows][cols];
		int count = 0;
		
		// 初始化并填充dp数组
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (matrix[i][j] == '1') {
					if (i == 0 || j == 0) {
						dp[i][j] = 1; // 边界情况
					} else {
						// 取左、上、左上三个位置的最小值加1
						dp[i][j] = Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]) + 1;
					}
					count += dp[i][j]; // 累加所有可能的正方形边长
				}
			}
		}
		return count;
	}

	// 比较含退格的字符串（栈解法）
	// 时间复杂度: O(n + m)
	// 空间复杂度: O(n + m)
	public static boolean backspaceCompare(String s, String t) {
		return processString(s).equals(processString(t));
	}

	// 辅助方法：处理字符串中的退格
	private static String processString(String str) {
		StringBuilder sb = new StringBuilder();
		for (char c : str.toCharArray()) {
			if (c == '#') {
				if (sb.length() > 0) {
					sb.deleteCharAt(sb.length() - 1); // 删除最后一个字符
				}
			} else {
				sb.append(c); // 添加字符
			}
		}
		return sb.toString();
	}

	// 每日温度问题（单调栈解法）
	// 时间复杂度: O(n)
	// 空间复杂度: O(n)
	public static int[] dailyTemperatures(int[] temperatures) {
		if (temperatures == null) {
			return new int[0];
		}
		
		int n = temperatures.length;
		int[] answer = new int[n];
		Stack<Integer> stack = new Stack<>(); // 存储索引
		
		// 从右往左遍历
		for (int i = n - 1; i >= 0; i--) {
			// 移除栈中所有温度低于等于当前温度的索引
			while (!stack.isEmpty() && temperatures[stack.peek()] <= temperatures[i]) {
				stack.pop();
			}
			// 计算天数差
			answer[i] = stack.isEmpty() ? 0 : stack.peek() - i;
			// 将当前索引入栈
			stack.push(i);
		}
		return answer;
	}

	// 测试方法
	public static void test() {
		// 测试最大矩形
		char[][] matrix1 = {
				{ '1', '0', '1', '0', '0' },
				{ '1', '0', '1', '1', '1' },
				{ '1', '1', '1', '1', '1' },
				{ '1', '0', '0', '1', '0' }
		};
		System.out.println("最大矩形面积: " + maximalRectangle(matrix1)); // 预期输出: 6
		
		// 测试最大正方形
		char[][] matrix2 = {
				{ '1', '0', '1', '0', '0' },
				{ '1', '0', '1', '1', '1' },
				{ '1', '1', '1', '1', '1' },
				{ '1', '0', '0', '1', '0' }
		};
		System.out.println("最大正方形面积: " + maximalSquare(matrix2)); // 预期输出: 4
		
		// 测试统计正方形子矩阵数量
		System.out.println("正方形子矩阵数量: " + countSquares(matrix2)); // 预期输出: 15
		
		// 测试比较含退格的字符串
		System.out.println("退格字符串比较: " + backspaceCompare("ab#c", "ad#c")); // 预期输出: true
		
		// 测试每日温度
		int[] temperatures = { 73, 74, 75, 71, 69, 72, 76, 73 };
		int[] result = dailyTemperatures(temperatures);
		System.out.print("每日温度结果: ");
		for (int num : result) {
			System.out.print(num + " "); // 预期输出: 1 1 4 2 1 1 0 0
		}
	}

	public static void main(String[] args) {
		test();
	}
}
package class052;

import java.util.Stack;

// 柱状图中最大的矩形
// 给定 n 个非负整数，用来表示柱状图中各个柱子的高度
// 每个柱子彼此相邻，且宽度为 1 。求在该柱状图中，能够勾勒出来的矩形的最大面积
// 测试链接：https://leetcode.cn/problems/largest-rectangle-in-histogram

// 补充题目1: 最大矩形 (Maximal Rectangle)
// 问题描述：给定一个仅包含 0 和 1 、大小为 rows * cols 的二维二进制矩阵，找出只包含 1 的最大矩形，并返回其面积。
// 解题思路：基于柱状图最大矩形问题的扩展，逐行构建高度数组，然后对每行应用单调栈算法。
// 时间复杂度：O(rows * cols)
// 空间复杂度：O(cols)
// 测试链接：https://leetcode.cn/problems/maximal-rectangle/

// 补充题目2: 子矩阵的最大面积
// 问题描述：给定一个二维矩阵，其中每个元素都是非负整数，求所有子矩阵中最大的矩形面积。
// 解题思路：类似于最大矩形问题，逐行构建高度数组，然后对每行应用单调栈算法。
// 时间复杂度：O(rows * cols)
// 空间复杂度：O(cols)

// 补充题目3: 雨水收集问题 (Trapping Rain Water)
// 问题描述：给定一个表示高度的数组，计算按此排列的柱子，下雨之后能接多少雨水。
// 解题思路：可以使用单调栈找到每个位置左右两侧的第一个更高柱子。
// 时间复杂度：O(n)
// 空间复杂度：O(n)
// 测试链接：https://leetcode.cn/problems/trapping-rain-water/

// 补充题目4: 132模式检测
// 问题描述：给定一个整数序列，判断其中是否存在132模式，即是否存在i < j < k，使得nums[i] < nums[k] < nums[j]。
// 解题思路：使用单调栈从右向左遍历，维护可能的中间元素和右侧最大值。
// 时间复杂度：O(n)
// 空间复杂度：O(n)
// 测试链接：https://leetcode.cn/problems/132-pattern/

// 补充题目5: P1796 汤姆斯的天堂梦（洛谷）
// 问题描述：给定一个序列，求其中满足条件的子序列的最大价值。
// 解题思路：使用单调栈预处理每个元素左右两侧第一个比它小的元素。
// 时间复杂度：O(n)
// 空间复杂度：O(n)
// 测试链接：https://www.luogu.com.cn/problem/P1796

// 补充题目6: P3245 [HNOI2016] 网络（洛谷）
// 问题描述：给定一棵树，支持路径加边权和查询路径上的边权最大值。
// 解题思路：使用树链剖分和单调栈维护区间最大值。
// 时间复杂度：O(n log^2 n)
// 空间复杂度：O(n)
// 测试链接：https://www.luogu.com.cn/problem/P3245

// 补充题目1: Maximal Rectangle（最大矩形）
// 问题描述：给定一个仅包含 0 和 1 、大小为 rows * cols 的二维二进制矩阵，找出只包含 1 的最大矩形，并返回其面积。
// 解题思路：这是柱状图中最大矩形问题的二维版本。可以对每一行计算高度数组，然后应用柱状图中最大矩形的解法。
// 时间复杂度：O(rows * cols)，需要遍历整个矩阵一次
// 空间复杂度：O(cols)，用于存储高度数组和单调栈
// 测试链接：https://leetcode.cn/problems/maximal-rectangle/

// 补充题目2: Count Submatrices With All Ones（统计全 1 子矩形）
// 问题描述：给你一个 m x n 的二进制矩阵 mat，请你统计并返回有多少个子矩形，其全部元素都是 1。
// 解题思路：对每一行计算高度数组，然后对每个位置计算以该位置为右下角的全1子矩形数量。
// 可以使用单调栈优化计算过程。
// 时间复杂度：O(m * n)，需要遍历整个矩阵一次
// 空间复杂度：O(n)，用于存储高度数组和单调栈
// 测试链接：https://leetcode.cn/problems/count-submatrices-with-all-ones/

// 补充题目3: Largest Rectangle in Histogram（柱状图中最大的矩形）- 变体
// 问题描述：在一个环形柱状图中，求能够勾勒出来的矩形的最大面积。
// 解题思路：环形柱状图可以看作是普通柱状图的两倍长度，通过取模操作处理索引。
// 时间复杂度：O(n)
// 空间复杂度：O(n)
public class Code04_LargestRectangleInHistogram {

	public static int MAXN = 100001;

	public static int[] stack = new int[MAXN];

	public static int r;

	// 柱状图中最大矩形的最优解 - 单调栈方法
	// 时间复杂度: O(n)，每个元素最多入栈出栈各一次
	// 空间复杂度: O(n)，用于存储栈
	public static int largestRectangleArea(int[] heights) {
		int n = heights.length;
		int maxArea = 0; // 最大面积
		r = 0; // 栈顶指针
		
		// 遍历每个柱子
		for (int i = 0; i < n; i++) {
			// 当栈不为空且当前柱子高度小于栈顶柱子高度时
			// 计算以栈顶柱子为高度的最大矩形面积
			while (r > 0 && heights[i] < heights[stack[r - 1]]) {
				int height = heights[stack[--r]]; // 弹出栈顶元素，获取高度
				// 计算宽度：当前索引减去栈顶索引减1（如果栈为空则宽度为i）
				int width = r == 0 ? i : i - stack[r - 1] - 1;
				maxArea = Math.max(maxArea, height * width); // 更新最大面积
			}
			stack[r++] = i; // 将当前索引入栈
		}
		
		// 处理栈中剩余的柱子
		while (r > 0) {
			int height = heights[stack[--r]]; // 弹出栈顶元素，获取高度
			// 计算宽度：数组长度减去栈顶索引减1（如果栈为空则宽度为n）
			int width = r == 0 ? n : n - stack[r - 1] - 1;
			maxArea = Math.max(maxArea, height * width); // 更新最大面积
		}
		
		return maxArea;
	}

	// 最大矩形（二维矩阵）的解法
	// 时间复杂度: O(rows * cols)，对每一行应用单调栈算法
	// 空间复杂度: O(cols)，用于存储高度数组和栈
	public static int maximalRectangle(char[][] matrix) {
		if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
			return 0;
		}
		int rows = matrix.length;
		int cols = matrix[0].length;
		int[] heights = new int[cols]; // 高度数组，记录每个位置上方连续的1的个数
		int maxArea = 0;
		
		// 逐行构建高度数组并计算最大矩形
		for (int i = 0; i < rows; i++) {
			// 更新高度数组
			for (int j = 0; j < cols; j++) {
				heights[j] = matrix[i][j] == '1' ? heights[j] + 1 : 0;
			}
			// 对当前高度数组应用单调栈算法计算最大矩形面积
			maxArea = Math.max(maxArea, largestRectangleArea(heights));
		}
		return maxArea;
	}

	// 雨水收集问题的单调栈解法
	// 时间复杂度: O(n)
	// 空间复杂度: O(n)
	public static int trap(int[] height) {
		if (height == null || height.length < 3) {
			return 0; // 至少需要3个柱子才能接雨水
		}
		int n = height.length;
		Stack<Integer> stack = new Stack<>(); // 使用标准库Stack
		int water = 0; // 总雨水量
		
		// 遍历每个柱子
		for (int i = 0; i < n; i++) {
			// 当栈不为空且当前柱子高度大于栈顶柱子高度时，说明可能形成了凹槽
			while (!stack.isEmpty() && height[i] > height[stack.peek()]) {
				int bottomIndex = stack.pop(); // 凹槽底部的索引
				if (stack.isEmpty()) {
					break; // 没有左边界，无法形成凹槽
				}
				// 计算宽度：当前柱子与左边界之间的距离
				int width = i - stack.peek() - 1;
				// 计算高度：左右边界的最小高度减去底部高度
				int h = Math.min(height[i], height[stack.peek()]) - height[bottomIndex];
				// 累加雨水量
				water += width * h;
			}
			stack.push(i); // 将当前索引入栈
		}
		return water;
	}

	// 132模式检测
	// 时间复杂度: O(n)
	// 空间复杂度: O(n)
	public static boolean find132pattern(int[] nums) {
		if (nums == null || nums.length < 3) {
			return false; // 至少需要3个元素才能形成132模式
		}
		int n = nums.length;
		Stack<Integer> stack = new Stack<>(); // 单调递减栈
		int last = Integer.MIN_VALUE; // 记录可能的3后面的最大2
		
		// 从右往左遍历
		for (int i = n - 1; i >= 0; i--) {
			// 如果当前元素小于last，说明找到了132模式
			if (nums[i] < last) {
				return true;
			}
			// 维护单调递减栈，找到更大的元素作为3，并更新last
			while (!stack.isEmpty() && nums[i] > nums[stack.peek()]) {
				last = nums[stack.pop()]; // 更新可能的2
			}
			stack.push(i); // 将当前索引入栈
		}
		return false; // 未找到132模式
	}

	// 测试方法
	public static void test() {
		// 测试柱状图中最大矩形
		int[] heights1 = {2, 1, 5, 6, 2, 3};
		System.out.println("柱状图中最大矩形面积: " + largestRectangleArea(heights1)); // 预期输出: 10
		
		// 测试雨水收集
		int[] heights2 = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
		System.out.println("能接的雨水量: " + trap(heights2)); // 预期输出: 6
		
		// 测试132模式检测
		int[] nums = {3, 1, 4, 2};
		System.out.println("是否存在132模式: " + find132pattern(nums)); // 预期输出: true
	}
}
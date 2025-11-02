package class052;

// 单调栈求每个位置左右两侧，离当前位置最近、且值严格小于的位置
// 给定一个可能含有重复值的数组 arr
// 找到每一个 i 位置左边和右边离 i 位置最近且值比 arr[i] 小的位置
// 返回所有位置相应的信息。
// 输入描述：
// 第一行输入一个数字 n，表示数组 arr 的长度。
// 以下一行输入 n 个数字，表示数组的值
// 输出描述：
// 输出n行，每行两个数字 L 和 R，如果不存在，则值为 -1，下标从 0 开始。
// 测试链接 : https://www.nowcoder.com/practice/2a2c00e7a88a498693568cef63a4b7bb
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过

// 补充题目1: Trapping Rain Water（接雨水）
// 问题描述：给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。
// 解题思路：使用单调栈来解决。维护一个单调递减的栈，当遇到比栈顶高的柱子时，说明可能形成了凹槽可以接雨水。
// 时间复杂度：O(n)，每个元素最多入栈和出栈各一次
// 空间复杂度：O(n)，栈的空间
// 测试链接：https://leetcode.cn/problems/trapping-rain-water/

// 补充题目2: P5788 【模板】单调栈（洛谷）
// 问题描述：给定一个长度为n的数组，打印每个位置的右侧，大于该位置数字的最近位置。
// 解题思路：使用单调递减栈存储索引，栈中元素保证从栈底到栈顶的数值递减。
// 时间复杂度：O(n)
// 空间复杂度：O(n)
// 测试链接：https://www.luogu.com.cn/problem/P5788

// 补充题目3: P1901 发射站（洛谷）
// 问题描述：一些学校搭建了无线电通信设施，每个通信站都有不同的高度和信号强度。
// 高的通信站可以向低的通信站发送信号，但只能发送到最近的比它高的通信站。
// 求每个通信站能接收到的信号总强度。
// 解题思路：使用单调栈分别计算每个通信站向左和向右能发送到的最近更高通信站。
// 时间复杂度：O(n)
// 空间复杂度：O(n)
// 测试链接：https://www.luogu.com.cn/problem/P1901

// 补充题目4: 907. Sum of Subarray Minimums（子数组的最小值之和）
// 问题描述：给定一个整数数组 arr，找到 min(b) 的总和，其中 b 的范围为 arr 的每个（连续）子数组。
// 解题思路：使用单调栈找到每个元素作为最小值能覆盖的区间范围，计算贡献。
// 时间复杂度：O(n)
// 空间复杂度：O(n)
// 测试链接：https://leetcode.cn/problems/sum-of-subarray-minimums/

// 补充题目5: 2281. 巫师的总力量和
// 问题描述：作为国王的统治者，你有一支巫师军队听你指挥。
// 给你一个下标从 0 开始的整数数组 strength ，其中 strength[i] 表示第 i 位巫师的力量值。
// 对于连续的一组巫师（也就是这些巫师的力量值组成了一个连续子数组），总力量为以下两个值的乘积：
// 巫师中最弱的能力值。
// 组中所有巫师的能力值的和。
// 请你返回所有可能的连续巫师组的总力量之和。
// 解题思路：结合单调栈和前缀和技术，找到每个元素作为最小值的区间，并计算对应的子数组和之和。
// 时间复杂度：O(n)
// 空间复杂度：O(n)
// 测试链接：https://leetcode.cn/problems/sum-of-total-strength-of-wizards/

// 补充题目6: 456. 132 模式
// 问题描述：给你一个整数数组 nums ，判断数组中是否存在 132 模式的子序列。
// 解题思路：使用单调栈维护可能的最大中间值（即3），从右往左遍历。
// 时间复杂度：O(n)
// 空间复杂度：O(n)
// 测试链接：https://leetcode.cn/problems/132-pattern/

// 补充题目7: 739. Daily Temperatures（每日温度）
// 问题描述：给定一个整数数组 temperatures ，表示每天的温度，返回一个数组 answer，其中 answer[i] 是指对于第 i 天，下一个更高温度出现在几天后。
// 解题思路：使用单调递减栈存储索引，当遇到更高温度时计算天数差。
// 时间复杂度：O(n)
// 空间复杂度：O(n)
// 测试链接：https://leetcode.cn/problems/daily-temperatures/

// 补充题目8: 84. Largest Rectangle in Histogram（柱状图中最大的矩形）
// 问题描述：给定 n 个非负整数，用来表示柱状图中各个柱子的高度。每个柱子彼此相邻，且宽度为 1 。求在该柱状图中，能够勾勒出来的矩形的最大面积。
// 解题思路：使用单调递增栈，当遇到更矮柱子时计算以栈顶柱子为高的矩形面积。
// 时间复杂度：O(n)
// 空间复杂度：O(n)
// 测试链接：https://leetcode.cn/problems/largest-rectangle-in-histogram/

// 补充题目9: 85. Maximal Rectangle（最大矩形）
// 问题描述：给定一个仅包含 0 和 1 、大小为 rows * cols 的二维二进制矩阵，找出只包含 1 的最大矩形，并返回其面积。
// 解题思路：逐行构建高度数组，然后应用柱状图中最大矩形的解法。
// 时间复杂度：O(rows * cols)
// 空间复杂度：O(cols)
// 测试链接：https://leetcode.cn/problems/maximal-rectangle/

// 补充题目10: 496. Next Greater Element I（下一个更大元素 I）
// 问题描述：nums1 中数字 x 的 下一个更大元素 是指 x 在 nums2 中对应位置右侧的第一个比 x 大的元素。
// 解题思路：使用单调栈预处理 nums2，并用哈希表存储结果以便快速查询。
// 时间复杂度：O(nums1.length + nums2.length)
// 空间复杂度：O(nums2.length)
// 测试链接：https://leetcode.cn/problems/next-greater-element-i/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code01_LeftRightLess {

	public static int MAXN = 1000001;

	public static int[] arr = new int[MAXN];

	public static int[] stack = new int[MAXN];

	public static int[][] ans = new int[MAXN][2];

	public static int n, r;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			n = (int) in.nval;
			for (int i = 0; i < n; i++) {
				in.nextToken();
				arr[i] = (int) in.nval;
			}
			compute();
			for (int i = 0; i < n; i++) {
				out.println(ans[i][0] + " " + ans[i][1]);
			}
		}
		out.flush();
		out.close();
		br.close();
	}

	// arr[0...n-1]
	public static void compute() {
		r = 0;
		int cur;
		// 遍历阶段
		for (int i = 0; i < n; i++) {
			// i -> arr[i]
			while (r > 0 && arr[stack[r - 1]] >= arr[i]) {
				cur = stack[--r];
				// cur当前弹出的位置，左边最近且小
				ans[cur][0] = r > 0 ? stack[r - 1] : -1;
				ans[cur][1] = i;
			}
			stack[r++] = i;
		}
		// 清算阶段
		while (r > 0) {
			cur = stack[--r];
			ans[cur][0] = r > 0 ? stack[r - 1] : -1;
			ans[cur][1] = -1;
		}
		// 修正阶段
		// 左侧的答案不需要修正一定是正确的，只有右侧答案需要修正
		// 从右往左修正，n-1位置的右侧答案一定是-1，不需要修正
		for (int i = n - 2; i >= 0; i--) {
			if (ans[i][1] != -1 && arr[ans[i][1]] == arr[i]) {
				ans[i][1] = ans[ans[i][1]][1];
			}
		}
	}

	// 接雨水问题的单调栈解法
	// 时间复杂度: O(n)，每个元素最多入栈和出栈各一次
	// 空间复杂度: O(n)，用于存储单调栈
	public static int trap(int[] height) {
		if (height == null || height.length < 3) {
			return 0; // 至少需要3个柱子才能接雨水
		}
		int n = height.length;
		int[] stack = new int[n]; // 用数组模拟栈，存储索引
		int top = -1; // 栈顶指针
		int water = 0; // 总雨水量
		
		// 遍历每个柱子
		for (int i = 0; i < n; i++) {
			// 当栈不为空且当前柱子高度大于栈顶柱子高度时，说明可能形成了凹槽
			while (top >= 0 && height[i] > height[stack[top]]) {
				int bottomIndex = stack[top--]; // 凹槽底部的索引
				if (top < 0) {
					break; // 没有左边界，无法形成凹槽
				}
				// 计算宽度：当前柱子与左边界之间的距离
				int width = i - stack[top] - 1;
				// 计算高度：左右边界的最小高度减去底部高度
				int h = Math.min(height[i], height[stack[top]]) - height[bottomIndex];
				// 累加雨水量
				water += width * h;
			}
			// 将当前索引入栈
			stack[++top] = i;
		}
		return water;
	}

	// 测试接雨水方法的辅助函数
	public static void testTrap() {
		// 测试用例1: [0,1,0,2,1,0,1,3,2,1,2,1]
		int[] heights1 = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
		System.out.println("测试用例1: " + trap(heights1)); // 预期输出: 6
		
		// 测试用例2: [4,2,0,3,2,5]
		int[] heights2 = {4, 2, 0, 3, 2, 5};
		System.out.println("测试用例2: " + trap(heights2)); // 预期输出: 9
	}

	// 132模式问题的解法
	// 时间复杂度: O(n)
	// 空间复杂度: O(n)
	public static boolean find132pattern(int[] nums) {
		if (nums == null || nums.length < 3) {
			return false;
		}
		int n = nums.length;
		int[] stack = new int[n];
		int top = -1;
		int last = Integer.MIN_VALUE; // 记录可能的3后面的最大2
		
		// 从右往左遍历
		for (int i = n - 1; i >= 0; i--) {
			// 如果当前元素小于last，说明找到了132模式
			if (nums[i] < last) {
				return true;
			}
			// 维护单调递减栈，找到更大的元素作为3，并更新last
			while (top >= 0 && nums[i] > nums[stack[top]]) {
				last = nums[stack[top--]];
			}
			stack[++top] = i;
		}
		return false;
	}

	// 测试132模式方法
	public static void test132Pattern() {
		int[] nums1 = {1, 2, 3, 4}; // 预期: false
		int[] nums2 = {3, 1, 4, 2}; // 预期: true
		int[] nums3 = {-1, 3, 2, 0}; // 预期: true
		System.out.println("132模式测试用例1: " + find132pattern(nums1));
		System.out.println("132模式测试用例2: " + find132pattern(nums2));
		System.out.println("132模式测试用例3: " + find132pattern(nums3));
	}
}
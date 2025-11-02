package class052;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

// 每日温度
// 给定一个整数数组 temperatures ，表示每天的温度，返回一个数组 answer
// 其中 answer[i] 是指对于第 i 天，下一个更高温度出现在几天后
// 如果气温在这之后都不会升高，请在该位置用 0 来代替。
// 测试链接 : https://leetcode.cn/problems/daily-temperatures/

// 补充题目1: Next Greater Element I（下一个更大元素 I）
// 问题描述：nums1 中数字 x 的 下一个更大元素 是指 x 在 nums2 中对应位置右侧的第一个比 x 大的元素。
// 给你两个没有重复元素的数组 nums1 和 nums2，下标从 0 开始计数，其中 nums1 是 nums2 的子集。
// 对于每个 0 <= i < nums1.length，找出满足 nums1[i] == nums2[j] 的下标 j，并且在 nums2 确定 nums2[j] 的下一个更大元素。
// 如果不存在下一个更大元素，那么本次查询的答案是 -1。
// 返回一个长度为 nums1.length 的数组 ans 作为答案，满足 ans[i] 是如上所述的下一个更大元素。
// 解题思路：使用单调栈预处理 nums2 数组，得到每个元素的下一个更大元素，然后通过哈希表快速查询。
// 时间复杂度：O(nums1.length + nums2.length)，需要遍历两个数组各一次
// 空间复杂度：O(nums2.length)，单调栈和哈希表的空间
// 测试链接：https://leetcode.cn/problems/next-greater-element-i/

// 补充题目2: Next Greater Element II（下一个更大元素 II）
// 问题描述：给定一个循环数组 nums（nums[nums.length - 1] 的下一个元素是 nums[0]），返回 nums 中每个元素的下一个更大元素。
// 数字 x 的下一个更大的元素是按数组遍历顺序，这个数字之后的第一个比它更大的数，这意味着你应该循环地搜索它的下一个更大的数。如果不存在，则输出 -1。
// 解题思路：使用单调栈处理循环数组，可以遍历数组两次来模拟循环效果。
// 时间复杂度：O(n)，每个元素最多入栈和出栈各两次
// 空间复杂度：O(n)，栈的空间
// 测试链接：https://leetcode.cn/problems/next-greater-element-ii/

// 补充题目3: 503. Next Greater Element III（下一个更大元素 III）
// 问题描述：给你一个正整数 n ，找出大于 n 且数字排列相同的最小正整数，若不存在则返回 -1。
// 解题思路：这是一个排列问题，可以使用类似单调栈的思想来找到下一个更大的排列。
// 时间复杂度：O(digits)，digits是数字的位数
// 空间复杂度：O(digits)
// 测试链接：https://leetcode.cn/problems/next-greater-element-iii/

// 补充题目4: 901. Online Stock Span（股票价格跨度）
// 问题描述：设计一个算法收集某些股票的每日报价，并返回该股票当日价格的跨度。
// 当日股票价格的跨度被定义为股票价格小于或等于今天价格的最大连续日数（从今天开始往回数，包括今天）。
// 解题思路：使用单调栈维护一个递减序列，栈中存储 (价格, 跨度) 对。
// 时间复杂度：O(n) 均摊，每个元素最多入栈和出栈各一次
// 空间复杂度：O(n)
// 测试链接：https://leetcode.cn/problems/online-stock-span/

// 补充题目5: 1019. Next Greater Node In Linked List（链表中的下一个更大节点）
// 问题描述：给定一个链表，返回链表中每个节点的下一个更大节点的值。如果不存在下一个更大节点，则相应节点的值为 0。
// 解题思路：将链表转换为数组，然后使用单调栈处理。
// 时间复杂度：O(n)
// 空间复杂度：O(n)
// 测试链接：https://leetcode.cn/problems/next-greater-node-in-linked-list/

// 补充题目6: P2947 [USACO09MAR] Look Up S（洛谷）
// 问题描述：约翰的奶牛按照编号排队，每头奶牛有一个高度。
// 对于每头奶牛，找到在队伍中排在它后面且高度大于它的第一头奶牛。如果不存在这样的奶牛，输出0。
// 解题思路：从右向左遍历，维护一个单调递减栈。
// 时间复杂度：O(n)
// 空间复杂度：O(n)
// 测试链接：https://www.luogu.com.cn/problem/P2947

// 补充题目7: P2866 [USACO06NOV] Bad Hair Day S（洛谷）
// 问题描述：农夫约翰的N头奶牛排成一排，每头奶牛有一个高度。
// 每头奶牛可以看到它右侧所有高度小于它的奶牛，直到遇到一头高度大于或等于它的奶牛为止。
// 求所有奶牛能看到的其他奶牛数量之和。
// 解题思路：使用单调递减栈。
// 时间复杂度：O(n)
// 空间复杂度：O(n)
// 测试链接：https://www.luogu.com.cn/problem/P2866

public class Code02_DailyTemperatures {

	public static int MAXN = 100001;

	public static int[] stack = new int[MAXN];

	public static int r;

	// 每日温度问题的标准解法
	// 时间复杂度: O(n)，每个元素最多入栈出栈各一次
	// 空间复杂度: O(n)，用于存储栈
	public static int[] dailyTemperatures(int[] nums) {
		int n = nums.length;
		int[] ans = new int[n]; // 初始化答案数组，默认值都是0
		r = 0; // 栈顶指针
		
		// 遍历每个元素
		for (int i = 0, cur; i < n; i++) {
			// 当栈不为空且当前温度大于栈顶温度时
			// 说明找到了栈顶元素的下一个更高温度
			while (r > 0 && nums[stack[r - 1]] < nums[i]) {
				cur = stack[--r]; // 弹出栈顶元素
				ans[cur] = i - cur; // 计算天数差
			}
			stack[r++] = i; // 将当前索引入栈
		}
		return ans;
	}

	// Next Greater Element I 的解法
	// 时间复杂度: O(n1 + n2)，n1是nums1的长度，n2是nums2的长度
	// 空间复杂度: O(n2)，用于存储哈希表和栈
	public static int[] nextGreaterElement(int[] nums1, int[] nums2) {
		// 使用哈希表存储nums2中每个元素的下一个更大元素
		HashMap<Integer, Integer> map = new HashMap<>();
		Stack<Integer> stk = new Stack<>();
		
		// 从右往左遍历nums2，维护单调递减栈
		for (int i = nums2.length - 1; i >= 0; i--) {
			// 弹出栈中所有小于等于当前元素的值
			while (!stk.isEmpty() && stk.peek() <= nums2[i]) {
				stk.pop();
			}
			// 栈顶元素就是当前元素的下一个更大元素
			map.put(nums2[i], stk.isEmpty() ? -1 : stk.peek());
			// 将当前元素入栈
			stk.push(nums2[i]);
		}
		
		// 构建答案数组
		int[] ans = new int[nums1.length];
		for (int i = 0; i < nums1.length; i++) {
			ans[i] = map.get(nums1[i]);
		}
		return ans;
	}

	// Next Greater Element II 的解法（循环数组）
	// 时间复杂度: O(n)，每个元素最多入栈出栈各两次
	// 空间复杂度: O(n)，用于存储栈
	public static int[] nextGreaterElements(int[] nums) {
		int n = nums.length;
		int[] ans = new int[n];
		Arrays.fill(ans, -1); // 初始化为-1
		Stack<Integer> stk = new Stack<>();
		
		// 遍历数组两次来模拟循环数组
		for (int i = 0; i < 2 * n; i++) {
			int index = i % n; // 实际索引
			
			// 维护单调递减栈
			while (!stk.isEmpty() && nums[stk.peek()] < nums[index]) {
				int prevIndex = stk.pop();
				if (ans[prevIndex] == -1) { // 只处理未找到的情况
					ans[prevIndex] = nums[index];
				}
			}
			
			// 只在第一次遍历时将索引加入栈中
			if (i < n) {
				stk.push(index);
			}
		}
		return ans;
	}

	// 股票价格跨度问题的实现
	// 为了演示，这里使用内部类实现StockSpanner
	public static class StockSpanner {
		private Stack<int[]> stack; // 存储 [价格, 跨度]
		
		public StockSpanner() {
			stack = new Stack<>();
		}
		
		// 计算当前价格的跨度
		// 时间复杂度: O(1) 均摊，每个元素最多入栈出栈各一次
		public int next(int price) {
			int span = 1; // 初始跨度为1（包括自己）
			
			// 弹出所有小于等于当前价格的元素，并累加它们的跨度
			while (!stack.isEmpty() && stack.peek()[0] <= price) {
				span += stack.pop()[1];
			}
			
			// 将当前价格和跨度入栈
			stack.push(new int[]{price, span});
			return span;
		}
	}

	// 测试方法
	public static void test() {
		// 测试每日温度
		int[] temperatures = {73, 74, 75, 71, 69, 72, 76, 73};
		int[] result1 = dailyTemperatures(temperatures);
		System.out.print("每日温度结果: ");
		for (int num : result1) {
			System.out.print(num + " ");
		}
		System.out.println();
		
		// 测试Next Greater Element I
		int[] nums1 = {4, 1, 2};
		int[] nums2 = {1, 3, 4, 2};
		int[] result2 = nextGreaterElement(nums1, nums2);
		System.out.print("Next Greater Element I结果: ");
		for (int num : result2) {
			System.out.print(num + " ");
		}
		System.out.println();
		
		// 测试Next Greater Element II
		int[] nums3 = {1, 2, 1};
		int[] result3 = nextGreaterElements(nums3);
		System.out.print("Next Greater Element II结果: ");
		for (int num : result3) {
			System.out.print(num + " ");
		}
		System.out.println();
	}
}
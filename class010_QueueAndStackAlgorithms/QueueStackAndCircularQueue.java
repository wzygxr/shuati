package class013_QueueAndStackAlgorithms;

import java.util.Queue;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/**
 * 队列和栈相关算法题解
 * 包含基础实现、经典题目以及扩展题目
 * 详细注释了时间复杂度、空间复杂度分析和解题思路
 */
public class QueueStackAndCircularQueue {

	// 直接用java内部的实现
	// 其实内部就是双向链表，常数操作慢
	public static class Queue1 {

		// java中的双向链表LinkedList
		// 单向链表就足够了
		public Queue<Integer> queue = new LinkedList<>();

		// 调用任何方法之前，先调用这个方法来判断队列内是否有东西
		public boolean isEmpty() {
			return queue.isEmpty();
		}

		// 向队列中加入num，加到尾巴
		public void offer(int num) {
			queue.offer(num);
		}

		// 从队列拿，从头拿
		public int poll() {
			return queue.poll();
		}

		// 返回队列头的元素但是不弹出
		public int peek() {
			return queue.peek();
		}

		// 返回目前队列里有几个数
		public int size() {
			return queue.size();
		}

	}

	// 实际刷题时更常见的写法，常数时间好
	// 如果可以确定加入操作的总次数不超过n，那么可以用
	// 一般笔试、面试都会有一个明确数据量，所以这是最常用的方式
	public static class Queue2 {

		public int[] queue;
		public int l;
		public int r;

		// 加入操作的总次数上限是多少，一定要明确
		public Queue2(int n) {
			queue = new int[n];
			l = 0;
			r = 0;
		}

		// 调用任何方法之前，先调用这个方法来判断队列内是否有东西
		public boolean isEmpty() {
			return l == r;
		}

		public void offer(int num) {
			queue[r++] = num;
		}

		public int poll() {
			return queue[l++];
		}
		// ?
		// l...r-1 r
		// [l..r)
		public int head() {
			return queue[l];
		}

		public int tail() {
			return queue[r - 1];
		}

		public int size() {
			return r - l;
		}

	}

	// 直接用java内部的实现
	// 其实就是动态数组，不过常数时间并不好
	public static class Stack1 {

		public Stack<Integer> stack = new Stack<>();

		// 调用任何方法之前，先调用这个方法来判断栈内是否有东西
		public boolean isEmpty() {
			return stack.isEmpty();
		}

		public void push(int num) {
			stack.push(num);
		}

		public int pop() {
			return stack.pop();
		}

		public int peek() {
			return stack.peek();
		}

		public int size() {
			return stack.size();
		}

	}

	// 实际刷题时更常见的写法，常数时间好
	// 如果可以保证同时在栈里的元素个数不会超过n，那么可以用
	// 也就是发生弹出操作之后，空间可以复用
	// 一般笔试、面试都会有一个明确数据量，所以这是最常用的方式
	public static class Stack2 {

		public int[] stack;
		public int size;

		// 同时在栈里的元素个数不会超过n
		public Stack2(int n) {
			stack = new int[n];
			size = 0;
		}

		// 调用任何方法之前，先调用这个方法来判断栈内是否有东西
		public boolean isEmpty() {
			return size == 0;
		}

		public void push(int num) {
			stack[size++] = num;
		}

		public int pop() {
			return stack[--size];
		}

		public int peek() {
			return stack[size - 1];
		}

		public int size() {
			return size;
		}

	}

	// 设计循环队列
	// 测试链接 : https://leetcode.cn/problems/design-circular-queue/
	class MyCircularQueue {

		public int[] queue;

		public int l, r, size, limit;

		// 同时在队列里的数字个数，不要超过k
		public MyCircularQueue(int k) {
			queue = new int[k];
			l = r = size = 0;
			limit = k;
		}

		// 如果队列满了，什么也不做，返回false
		// 如果队列没满，加入value，返回true
		public boolean enQueue(int value) {
			if (isFull()) {
				return false;
			} else {
				queue[r] = value;
				// r++, 结束了，跳回0
				r = r == limit - 1 ? 0 : (r + 1);
				size++;
				return true;
			}
		}

		// 如果队列空了，什么也不做，返回false
		// 如果队列没空，弹出头部的数字，返回true
		public boolean deQueue() {
			if (isEmpty()) {
				return false;
			} else {
				// l++, 结束了，跳回0
				l = l == limit - 1 ? 0 : (l + 1);
				size--;
				return true;
			}
		}

		// 返回队列头部的数字（不弹出），如果没有数返回-1
		public int Front() {
			if (isEmpty()) {
				return -1;
			} else {
				return queue[l];
			}
		}

		public int Rear() {
			if (isEmpty()) {
				return -1;
			} else {
				int last = r == 0 ? (limit - 1) : (r - 1);
				return queue[last];
			}
		}

		public boolean isEmpty() {
			return size == 0;
		}

		public boolean isFull() {
			return size == limit;
		}

	}

	/**
	 * 用队列实现栈
	 * 题目来源：LeetCode 225. 用队列实现栈
	 * 链接：https://leetcode.cn/problems/implement-stack-using-queues/
	 * 
	 * 题目描述：
	 * 请你仅使用两个队列实现一个后入先出（LIFO）的栈，并支持普通栈的全部四种操作（push、top、pop 和 empty）。
	 * 实现 MyStack 类：
	 * void push(int x) 将元素 x 压入栈顶。
	 * int pop() 移除并返回栈顶元素。
	 * int top() 返回栈顶元素。
	 * boolean empty() 如果栈是空的，返回 true ；否则，返回 false 。
	 * 
	 * 解题思路：
	 * 使用两个队列，一个主队列和一个辅助队列。每次push操作时，将新元素加入辅助队列，然后将主队列的所有元素依次移到辅助队列，
	 * 最后交换两个队列的角色。这样可以保证新元素总是在队列的前端，实现栈的LIFO特性。
	 * 
	 * 时间复杂度分析：
	 * - push操作：O(n) - 需要将主队列的所有元素移到辅助队列
	 * - pop操作：O(1) - 直接从主队列前端移除元素
	 * - top操作：O(1) - 直接返回主队列前端元素
	 * - empty操作：O(1) - 检查主队列是否为空
	 * 
	 * 空间复杂度分析：
	 * O(n) - 需要两个队列来存储元素
	 */
	public static class MyStack {
		private Queue<Integer> queue1;  // 主队列
		private Queue<Integer> queue2;  // 辅助队列
		
		public MyStack() {
			queue1 = new LinkedList<>();
			queue2 = new LinkedList<>();
		}
		
		// 将元素 x 压入栈顶
		public void push(int x) {
			// 将新元素加入辅助队列
			queue2.offer(x);
			// 将主队列的所有元素移到辅助队列
			while (!queue1.isEmpty()) {
				queue2.offer(queue1.poll());
			}
			// 交换两个队列的角色
			Queue<Integer> temp = queue1;
			queue1 = queue2;
			queue2 = temp;
		}
		
		// 移除并返回栈顶元素
		public int pop() {
			return queue1.poll();
		}
		
		// 返回栈顶元素
		public int top() {
			return queue1.peek();
		}
		
		// 如果栈是空的，返回 true ；否则，返回 false
		public boolean empty() {
			return queue1.isEmpty();
		}
	}

	/**
	 * 用栈实现队列
	 * 题目来源：LeetCode 232. 用栈实现队列
	 * 链接：https://leetcode.cn/problems/implement-queue-using-stacks/
	 * 
	 * 题目描述：
	 * 请你仅使用两个栈实现先入先出队列。队列应当支持一般队列支持的所有操作（push、pop、peek、empty）：
	 * 实现 MyQueue 类：
	 * void push(int x) 将元素 x 推到队列的末尾
	 * int pop() 从队列的开头移除并返回元素
	 * int peek() 返回队列开头的元素
	 * boolean empty() 如果队列为空，返回 true ；否则，返回 false
	 * 
	 * 解题思路：
	 * 使用两个栈，一个输入栈和一个输出栈。push操作时将元素压入输入栈，pop操作时如果输出栈为空，
	 * 就将输入栈的所有元素依次弹出并压入输出栈，然后再从输出栈弹出元素。这样可以保证元素的顺序符合队列的FIFO特性。
	 * 
	 * 时间复杂度分析：
	 * - push操作：O(1) - 直接压入输入栈
	 * - pop操作：均摊O(1) - 虽然有时需要将输入栈的所有元素移到输出栈，但每个元素最多只会被移动一次
	 * - peek操作：均摊O(1) - 同pop操作
	 * - empty操作：O(1) - 检查两个栈是否都为空
	 * 
	 * 空间复杂度分析：
	 * O(n) - 需要两个栈来存储元素
	 */
	public static class MyQueue {
		private Stack<Integer> inStack;   // 输入栈
		private Stack<Integer> outStack;  // 输出栈
		
		public MyQueue() {
			inStack = new Stack<>();
			outStack = new Stack<>();
		}
		
		// 将元素 x 推到队列的末尾
		public void push(int x) {
			inStack.push(x);
		}
		
		// 从队列的开头移除并返回元素
		public int pop() {
			checkOutStack();
			return outStack.pop();
		}
		
		// 返回队列开头的元素
		public int peek() {
			checkOutStack();
			return outStack.peek();
		}
		
		// 如果队列为空，返回 true ；否则，返回 false
		public boolean empty() {
			return inStack.isEmpty() && outStack.isEmpty();
		}
		
		// 检查输出栈是否为空，如果为空则将输入栈的所有元素移到输出栈
		private void checkOutStack() {
			if (outStack.isEmpty()) {
				while (!inStack.isEmpty()) {
					outStack.push(inStack.pop());
				}
			}
		}
	}

	/**
	 * 最小栈
	 * 题目来源：LeetCode 155. 最小栈
	 * 链接：https://leetcode.cn/problems/min-stack/
	 * 
	 * 题目描述：
	 * 设计一个支持 push ，pop ，top 操作，并能在常数时间内检索到最小元素的栈。
	 * 实现 MinStack 类:
	 * MinStack() 初始化堆栈对象。
	 * void push(int val) 将元素val推入堆栈。
	 * void pop() 删除堆栈顶部的元素。
	 * int top() 获取堆栈顶部的元素。
	 * int getMin() 获取堆栈中的最小元素。
	 * 
	 * 解题思路：
	 * 使用两个栈，一个数据栈存储所有元素，一个辅助栈存储每个位置对应的最小值。每次push操作时，
	 * 数据栈正常压入元素，辅助栈压入当前元素与之前最小值中的较小者。这样辅助栈的栈顶始终是当前栈中的最小值。
	 * 
	 * 时间复杂度分析：
	 * 所有操作都是O(1)时间复杂度
	 * 
	 * 空间复杂度分析：
	 * O(n) - 需要两个栈来存储元素
	 */
	public static class MinStack {
		private Stack<Integer> dataStack;  // 数据栈
		private Stack<Integer> minStack;   // 辅助栈，存储每个位置对应的最小值
		
		public MinStack() {
			dataStack = new Stack<>();
			minStack = new Stack<>();
		}
		
		// 将元素val推入堆栈
		public void push(int val) {
			dataStack.push(val);
			// 如果辅助栈为空，或者当前元素小于等于辅助栈栈顶元素，则压入当前元素，否则压入辅助栈栈顶元素
			if (minStack.isEmpty() || val <= minStack.peek()) {
				minStack.push(val);
			} else {
				minStack.push(minStack.peek());
			}
		}
		
		// 删除堆栈顶部的元素
		public void pop() {
			dataStack.pop();
			minStack.pop();
		}
		
		// 获取堆栈顶部的元素
		public int top() {
			return dataStack.peek();
		}
		
		// 获取堆栈中的最小元素
		public int getMin() {
			return minStack.peek();
		}
	}

	/**
	 * 有效的括号
	 * 题目来源：LeetCode 20. 有效的括号
	 * 链接：https://leetcode.cn/problems/valid-parentheses/
	 * 
	 * 题目描述：
	 * 给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串 s ，判断字符串是否有效。
	 * 有效字符串需满足：
	 * 1. 左括号必须用相同类型的右括号闭合。
	 * 2. 左括号必须以正确的顺序闭合。
	 * 3. 每个右括号都有一个对应的相同类型的左括号。
	 * 
	 * 解题思路：
	 * 使用栈来解决括号匹配问题。遍历字符串，遇到左括号时将其对应的右括号压入栈，
	 * 遇到右括号时检查是否与栈顶元素匹配。如果匹配则弹出栈顶元素，否则返回false。
	 * 最后检查栈是否为空，如果为空则说明所有括号都正确匹配。
	 * 
	 * 时间复杂度分析：
	 * O(n) - 需要遍历整个字符串
	 * 
	 * 空间复杂度分析：
	 * O(n) - 最坏情况下栈中存储所有左括号
	 */
	public static boolean isValid(String s) {
		Stack<Character> stack = new Stack<>();
		for (char c : s.toCharArray()) {
			// 遇到左括号时，将其对应的右括号压入栈
			if (c == '(') {
				stack.push(')');
			} else if (c == '[') {
				stack.push(']');
			} else if (c == '{') {
				stack.push('}');
			}
			// 遇到右括号时，检查是否与栈顶元素匹配
			else if (stack.isEmpty() || stack.pop() != c) {
				return false;
			}
		}
		// 最后检查栈是否为空
		return stack.isEmpty();
	}

	/**
	 * 接雨水
	 * 题目来源：LeetCode 42. 接雨水
	 * 链接：https://leetcode.cn/problems/trapping-rain-water/
	 * 
	 * 题目描述：
	 * 给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。
	 * 
	 * 解题思路（单调栈）：
	 * 使用单调栈来记录可能形成水坑的位置。当遇到一个比栈顶元素更高的柱子时，说明可能形成了一个水坑，
	 * 弹出栈顶元素作为坑底，新的栈顶元素作为左边界，当前柱子作为右边界，计算可以接的雨水量。
	 * 
	 * 时间复杂度分析：
	 * O(n) - 每个元素最多入栈出栈一次
	 * 
	 * 空间复杂度分析：
	 * O(n) - 最坏情况下栈中存储所有元素
	 */
	public static int trap(int[] height) {
		int n = height.length;
		if (n < 3) return 0; // 至少需要3个柱子才能接雨水
		
		Stack<Integer> stack = new Stack<>(); // 存储索引
		int water = 0;
		
		for (int i = 0; i < n; i++) {
			// 当前高度大于栈顶高度时，说明可以形成水坑
			while (!stack.isEmpty() && height[i] > height[stack.peek()]) {
				int bottom = stack.pop();
				
				if (stack.isEmpty()) break; // 没有左边界
				
				int left = stack.peek();
				int width = i - left - 1;
				int h = Math.min(height[left], height[i]) - height[bottom];
				water += width * h;
			}
			stack.push(i);
		}
		
		return water;
	}

	/**
	 * 柱状图中最大的矩形
	 * 题目来源：LeetCode 84. 柱状图中最大的矩形
	 * 链接：https://leetcode.cn/problems/largest-rectangle-in-histogram/
	 * 
	 * 题目描述：
	 * 给定 n 个非负整数，用来表示柱状图中各个柱子的高度。每个柱子彼此相邻，且宽度为 1 。
	 * 求在该柱状图中，能够勾勒出来的矩形的最大面积。
	 * 
	 * 解题思路（单调栈）：
	 * 使用单调栈来找到每个柱子左边和右边第一个比它小的柱子的位置。对于每个柱子，
	 * 其能形成的最大矩形的宽度是右边界减去左边界减一，高度是柱子本身的高度。
	 * 
	 * 时间复杂度分析：
	 * O(n) - 每个元素最多入栈出栈一次
	 * 
	 * 空间复杂度分析：
	 * O(n) - 栈的最大空间为n
	 */
	public static int largestRectangleArea(int[] heights) {
		int n = heights.length;
		if (n == 0) return 0;
		
		Stack<Integer> stack = new Stack<>(); // 存储索引
		int maxArea = 0;
		
		for (int i = 0; i <= n; i++) {
			// 当i=n时，将高度视为0，用于处理栈中剩余的元素
			int h = (i == n) ? 0 : heights[i];
			
			// 当当前高度小于栈顶高度时，计算栈顶柱子能形成的最大矩形
			while (!stack.isEmpty() && h < heights[stack.peek()]) {
				int height = heights[stack.pop()];
				int width = stack.isEmpty() ? i : (i - stack.peek() - 1);
				maxArea = Math.max(maxArea, height * width);
			}
			stack.push(i);
		}
		
		return maxArea;
	}

	/**
	 * 每日温度
	 * 题目来源：LeetCode 739. 每日温度
	 * 链接：https://leetcode.cn/problems/daily-temperatures/
	 * 
	 * 题目描述：
	 * 给定一个整数数组 temperatures ，表示每天的温度，返回一个数组 answer ，其中 answer[i] 是指对于第 i 天，
	 * 下一个更高温度出现在几天后。如果气温在这之后都不会升高，请在该位置用 0 来代替。
	 * 
	 * 解题思路（单调栈）：
	 * 使用单调栈来存储温度的索引。遍历数组，当遇到一个温度比栈顶温度高时，说明找到了栈顶温度的下一个更高温度，
	 * 计算天数差并更新结果数组，然后弹出栈顶元素，继续比较新的栈顶元素，直到栈为空或栈顶温度不小于当前温度。
	 * 
	 * 时间复杂度分析：
	 * O(n) - 每个元素最多入栈出栈一次
	 * 
	 * 空间复杂度分析：
	 * O(n) - 栈的最大空间为n
	 */
	public static int[] dailyTemperatures(int[] temperatures) {
		int n = temperatures.length;
		int[] answer = new int[n];
		Stack<Integer> stack = new Stack<>(); // 存储索引
		
		for (int i = 0; i < n; i++) {
			// 当前温度大于栈顶温度时，更新结果
			while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
				int prevIndex = stack.pop();
				answer[prevIndex] = i - prevIndex;
			}
			stack.push(i);
		}
		
		return answer;
	}

	/**
	 * 滑动窗口最大值
	 * 题目来源：LeetCode 239. 滑动窗口最大值
	 * 链接：https://leetcode.cn/problems/sliding-window-maximum/
	 * 
	 * 题目描述：
	 * 给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。
	 * 你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。
	 * 返回 滑动窗口中的最大值 。
	 * 
	 * 解题思路（单调队列）：
	 * 使用双端队列来维护窗口中的最大值。队列中的元素是数组的索引，对应的数组值是单调递减的。
	 * 当窗口滑动时，首先移除队列中不在窗口内的元素，然后移除队列中所有小于当前元素的索引，
	 * 因为它们不可能成为窗口中的最大值，最后将当前索引加入队列。队列的头部始终是当前窗口中的最大值的索引。
	 * 
	 * 时间复杂度分析：
	 * O(n) - 每个元素最多入队出队一次
	 * 
	 * 空间复杂度分析：
	 * O(k) - 队列的最大空间为k
	 */
	public static int[] maxSlidingWindow(int[] nums, int k) {
		int n = nums.length;
		int[] result = new int[n - k + 1];
		Deque<Integer> deque = new LinkedList<>(); // 存储索引，对应的值单调递减
		
		for (int i = 0; i < n; i++) {
			// 移除队列中不在窗口内的元素（即索引小于i-k+1的元素）
			while (!deque.isEmpty() && deque.peekFirst() < i - k + 1) {
				deque.pollFirst();
			}
			
			// 移除队列中所有小于当前元素的索引
			while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
				deque.pollLast();
			}
			
			deque.offerLast(i);
			
			// 当窗口形成时，队列头部是窗口中的最大值的索引
			if (i >= k - 1) {
				result[i - k + 1] = nums[deque.peekFirst()];
			}
		}
		
		return result;
	}

	/**
	 * 设计循环双端队列
	 * 题目来源：LeetCode 641. 设计循环双端队列
	 * 链接：https://leetcode.cn/problems/design-circular-deque/
	 * 
	 * 题目描述：
	 * 设计实现双端队列。
	 * 实现 MyCircularDeque 类:
	 * MyCircularDeque(int k) ：构造函数，双端队列最大为 k 。
	 * boolean insertFront(int value)：将一个元素添加到双端队列头部。 如果操作成功返回 true ，否则返回 false 。
	 * boolean insertLast(int value) ：将一个元素添加到双端队列尾部。如果操作成功返回 true ，否则返回 false 。
	 * boolean deleteFront() ：从双端队列头部删除一个元素。 如果操作成功返回 true ，否则返回 false 。
	 * boolean deleteLast() ：从双端队列尾部删除一个元素。如果操作成功返回 true ，否则返回 false 。
	 * int getFront() )：从双端队列头部获得一个元素。如果双端队列为空，返回 -1 。
	 * int getRear() ：获得双端队列的最后一个元素。 如果双端队列为空，返回 -1 。
	 * boolean isEmpty() ：若双端队列为空，则返回 true ，否则返回 false 。
	 * boolean isFull() ：若双端队列满了，则返回 true ，否则返回 false 。
	 * 
	 * 解题思路：
	 * 使用数组实现循环双端队列，通过维护队列头部和尾部指针以及队列大小来实现循环特性。
	 * 对于头部插入和删除操作，需要处理指针的循环特性。
	 * 
	 * 时间复杂度分析：
	 * 所有操作都是O(1)时间复杂度
	 * 
	 * 空间复杂度分析：
	 * O(k) - k是队列的容量
	 */
	class MyCircularDeque {
		private int[] deque;
		private int l, r, size, limit;

		public MyCircularDeque(int k) {
			deque = new int[k];
			l = r = size = 0;
			limit = k;
		}

		public boolean insertFront(int value) {
			if (isFull()) {
				return false;
			}
			
			if (isEmpty()) {
				l = r = 0;
				deque[0] = value;
			} else {
				l = l == 0 ? limit - 1 : l - 1;
				deque[l] = value;
			}
			size++;
			return true;
		}

		public boolean insertLast(int value) {
			if (isFull()) {
				return false;
			}
			
			if (isEmpty()) {
				l = r = 0;
				deque[0] = value;
			} else {
				r = r == limit - 1 ? 0 : r + 1;
				deque[r] = value;
			}
			size++;
			return true;
		}

		public boolean deleteFront() {
			if (isEmpty()) {
				return false;
			}
			
			l = l == limit - 1 ? 0 : l + 1;
			size--;
			return true;
		}

		public boolean deleteLast() {
			if (isEmpty()) {
				return false;
			}
			
			r = r == 0 ? limit - 1 : r - 1;
			size--;
			return true;
		}

		public int getFront() {
			if (isEmpty()) {
				return -1;
			}
			return deque[l];
		}

		public int getRear() {
			if (isEmpty()) {
				return -1;
			}
			return deque[r];
		}

		public boolean isEmpty() {
			return size == 0;
		}

		public boolean isFull() {
			return size == limit;
		}
	}

	/**
	 * 逆波兰表达式求值
	 * 题目来源：LeetCode 150. 逆波兰表达式求值
	 * 链接：https://leetcode.cn/problems/evaluate-reverse-polish-notation/
	 * 
	 * 题目描述：
	 * 给你一个字符串数组 tokens ，表示一个根据 逆波兰表示法 表示的算术表达式。
	 * 请你计算该表达式。返回一个表示表达式值的整数。
	 * 注意：
	 * 有效的算符为 '+'、'-'、'*' 和 '/' 。
	 * 每个操作数可以是整数，也可以是另一个表达式的结果。
	 * 除法运算向零截断。
	 * 表达式中不含除零运算。
	 * 输入是一个根据逆波兰表示法表示的算术表达式。
	 * 逆波兰表达式是一种后缀表达式，所谓后缀就是指算符写在后面。
	 * 
	 * 解题思路：
	 * 使用栈来存储操作数。遍历表达式，遇到数字时将其转换为整数并入栈，遇到运算符时弹出栈顶的两个操作数，
	 * 进行相应的运算，然后将结果压入栈中。最后栈中只剩下一个元素，即为表达式的结果。
	 * 
	 * 时间复杂度分析：
	 * O(n) - 需要遍历整个表达式
	 * 
	 * 空间复杂度分析：
	 * O(n) - 最坏情况下栈中存储所有操作数
	 */
	public static int evalRPN(String[] tokens) {
		Stack<Integer> stack = new Stack<>();
		
		for (String token : tokens) {
			if (token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/")) {
				// 弹出两个操作数
				int b = stack.pop();
				int a = stack.pop();
				
				// 进行相应的运算
				switch (token) {
					case "+":
						stack.push(a + b);
						break;
					case "-":
						stack.push(a - b);
						break;
					case "*":
						stack.push(a * b);
						break;
					case "/":
						stack.push(a / b);
						break;
				}
			} else {
				// 遇到数字，转换为整数并入栈
				stack.push(Integer.parseInt(token));
			}
		}
		
		return stack.pop();
	}

	/**
	 * 字符串解码
	 * 题目来源：LeetCode 394. 字符串解码
	 * 链接：https://leetcode.cn/problems/decode-string/
	 * 
	 * 题目描述：
	 * 给定一个经过编码的字符串，返回它解码后的字符串。
	 * 编码规则为: k[encoded_string]，表示其中方括号内部的 encoded_string 正好重复 k 次。注意 k 保证为正整数。
	 * 你可以认为输入字符串总是有效的；输入字符串中没有额外的空格，且输入的方括号总是符合格式要求的。
	 * 此外，你可以认为原始数据不包含数字，所有的数字只表示重复的次数 k ，例如不会出现像 3a 或 2[4] 的输入。
	 * 
	 * 解题思路：
	 * 使用两个栈，一个存储数字，一个存储字符串。遍历字符串，遇到数字时解析完整的数字，遇到'['时将当前数字和字符串入栈，
	 * 遇到']'时弹出栈顶的数字和字符串，将当前字符串重复数字次后与弹出的字符串拼接。
	 * 
	 * 时间复杂度分析：
	 * O(n) - 需要遍历整个字符串，每个字符最多被处理一次
	 * 
	 * 空间复杂度分析：
	 * O(n) - 栈的最大空间为n
	 */
	public static String decodeString(String s) {
		Stack<Integer> numStack = new Stack<>(); // 存储重复次数
		Stack<StringBuilder> strStack = new Stack<>(); // 存储中间字符串
		StringBuilder currentStr = new StringBuilder();
		int num = 0;
		
		for (char c : s.toCharArray()) {
			if (Character.isDigit(c)) {
				// 解析完整的数字
				num = num * 10 + (c - '0');
			} else if (c == '[') {
				// 将当前数字和字符串入栈
				numStack.push(num);
				strStack.push(currentStr);
				num = 0;
				currentStr = new StringBuilder();
			} else if (c == ']') {
				// 弹出栈顶的数字和字符串，进行拼接
				int repeat = numStack.pop();
				StringBuilder prevStr = strStack.pop();
				
				StringBuilder temp = new StringBuilder();
				for (int i = 0; i < repeat; i++) {
					temp.append(currentStr);
				}
				
				currentStr = prevStr.append(temp);
			} else {
				// 普通字符，添加到当前字符串
				currentStr.append(c);
			}
		}
		
		return currentStr.toString();
	}

	/**
	 * 删除字符串中的所有相邻重复项
	 * 题目来源：LeetCode 1047. 删除字符串中的所有相邻重复项
	 * 链接：https://leetcode.cn/problems/remove-all-adjacent-duplicates-in-string/
	 * 
	 * 题目描述：
	 * 给出由小写字母组成的字符串 S，重复项删除操作会选择两个相邻且相同的字母，并删除它们。
	 * 在 S 上反复执行重复项删除操作，直到无法继续删除。
	 * 在完成所有重复项删除操作后返回最终的字符串。答案保证唯一。
	 * 
	 * 解题思路：
	 * 使用栈来存储字符。遍历字符串，对于每个字符，如果栈不为空且栈顶元素与当前字符相同，则弹出栈顶元素，
	 * 否则将当前字符压入栈中。最后将栈中的元素按顺序拼接成字符串。
	 * 
	 * 时间复杂度分析：
	 * O(n) - 需要遍历整个字符串
	 * 
	 * 空间复杂度分析：
	 * O(n) - 栈的最大空间为n
	 */
	public static String removeDuplicates(String S) {
		Stack<Character> stack = new Stack<>();
		
		for (char c : S.toCharArray()) {
			if (!stack.isEmpty() && stack.peek() == c) {
				stack.pop();
			} else {
				stack.push(c);
			}
		}
		
		StringBuilder result = new StringBuilder();
		for (char c : stack) {
			result.append(c);
		}
		
		return result.toString();
	}

	/**
	 * 基本计算器 II
	 * 题目来源：LeetCode 227. 基本计算器 II
	 * 链接：https://leetcode.cn/problems/basic-calculator-ii/
	 * 
	 * 题目描述：
	 * 给你一个字符串表达式 s ，请你实现一个基本计算器来计算并返回它的值。
	 * 整数除法仅保留整数部分。
	 * 你可以假设给定的表达式总是有效的。所有中间结果将在 [-2^31, 2^31 - 1] 的范围内。
	 * 
	 * 解题思路：
	 * 使用栈来存储操作数。遍历字符串，遇到数字时解析完整的数字，遇到运算符时根据前一个运算符的类型进行相应的运算。
	 * 对于加减运算，将操作数压入栈中；对于乘除运算，弹出栈顶元素与当前操作数进行运算后将结果压入栈中。
	 * 最后将栈中的所有元素相加得到最终结果。
	 * 
	 * 时间复杂度分析：
	 * O(n) - 需要遍历整个字符串
	 * 
	 * 空间复杂度分析：
	 * O(n) - 栈的最大空间为n/2
	 */
	public static int calculate(String s) {
		Stack<Integer> stack = new Stack<>();
		char preSign = '+'; // 前一个运算符
		int num = 0;
		int n = s.length();
		
		for (int i = 0; i < n; i++) {
			char c = s.charAt(i);
			
			if (Character.isDigit(c)) {
				// 解析完整的数字
				num = num * 10 + (c - '0');
			}
			
			// 遇到运算符或到达字符串末尾
			if (!Character.isDigit(c) && c != ' ' || i == n - 1) {
				switch (preSign) {
					case '+':
						stack.push(num);
						break;
					case '-':
						stack.push(-num);
						break;
					case '*':
						stack.push(stack.pop() * num);
						break;
					case '/':
						stack.push(stack.pop() / num);
						break;
				}
				preSign = c;
				num = 0;
			}
		}
		
		// 将栈中的所有元素相加
		int result = 0;
		for (int val : stack) {
			result += val;
		}
		
		return result;
	}

	/**
	 * 删除字符串中的所有相邻重复项 II
	 * 题目来源：LeetCode 1209. 删除字符串中的所有相邻重复项 II
	 * 链接：https://leetcode.cn/problems/remove-all-adjacent-duplicates-in-string-ii/
	 * 
	 * 题目描述：
	 * 给你一个字符串 s，「k 倍重复项删除操作」将会从 s 中选择 k 个相邻且相等的字母，并删除它们，
	 * 使被删去的字符串的左侧和右侧连在一起。
	 * 你需要对 s 重复进行无限次这样的删除操作，直到无法继续为止。
	 * 在执行完所有删除操作后，返回最终得到的字符串。
	 * 
	 * 解题思路：
	 * 使用栈来存储字符和对应的出现次数。遍历字符串，对于每个字符，如果栈不为空且栈顶字符与当前字符相同，
	 * 则将栈顶的计数加1，如果计数等于k则弹出栈顶元素；否则将当前字符和计数1入栈。
	 * 最后将栈中的元素按顺序拼接成字符串。
	 * 
	 * 时间复杂度分析：
	 * O(n) - 需要遍历整个字符串
	 * 
	 * 空间复杂度分析：
	 * O(n) - 栈的最大空间为n
	 * 
	 * 是否最优解：是，这是最优解，时间和空间复杂度都无法再优化
	 */
	public static String removeDuplicates(String s, int k) {
		// 使用栈存储字符和出现次数
		Stack<Pair<Character, Integer>> stack = new Stack<>();
		
		for (char c : s.toCharArray()) {
			if (!stack.isEmpty() && stack.peek().getKey() == c) {
				// 如果栈顶字符与当前字符相同，增加计数
				int count = stack.peek().getValue() + 1;
				if (count == k) {
					// 如果计数等于k，弹出栈顶元素
					stack.pop();
				} else {
					// 否则更新计数
					stack.pop();
					stack.push(new Pair<>(c, count));
				}
			} else {
				// 如果栈为空或栈顶字符与当前字符不同，将当前字符和计数1入栈
				stack.push(new Pair<>(c, 1));
			}
		}
		
		// 构建结果字符串
		StringBuilder result = new StringBuilder();
		for (Pair<Character, Integer> pair : stack) {
			char c = pair.getKey();
			int count = pair.getValue();
			for (int i = 0; i < count; i++) {
				result.append(c);
			}
		}
		
		return result.toString();
	}
	
	// 辅助类：键值对
	static class Pair<K, V> {
		private K key;
		private V value;
		
		public Pair(K key, V value) {
			this.key = key;
			this.value = value;
		}
		
		public K getKey() {
			return key;
		}
		
		public V getValue() {
			return value;
		}
	}

	/**
	 * 下一个更大元素 I
	 * 题目来源：LeetCode 496. 下一个更大元素 I
	 * 链接：https://leetcode.cn/problems/next-greater-element-i/
	 * 
	 * 题目描述：
	 * nums1 中数字 x 的 下一个更大元素 是指 x 在 nums2 中对应位置 右侧 的 第一个 比 x 大的元素。
	 * 给你两个 没有重复元素 的数组 nums1 和 nums2 ，下标从 0 开始计数，其中nums1 是 nums2 的子集。
	 * 对于每个 0 <= i < nums1.length ，找出满足 nums1[i] == nums2[j] 的下标 j ，并且在 nums2 确定 nums2[j] 的 下一个更大元素 。
	 * 如果不存在下一个更大元素，那么本次查询的答案是 -1 。
	 * 返回一个长度为 nums1.length 的数组 ans 作为答案，满足 ans[i] 是如上所述的 下一个更大元素 。
	 * 
	 * 解题思路（单调栈）：
	 * 使用单调栈来找到nums2中每个元素的下一个更大元素。从右往左遍历nums2，维护一个单调递减的栈，
	 * 对于每个元素，弹出栈中所有小于等于当前元素的值，栈顶元素即为当前元素的下一个更大元素。
	 * 用HashMap存储每个元素及其下一个更大元素的映射关系。
	 * 
	 * 时间复杂度分析：
	 * O(m + n) - m是nums1的长度，n是nums2的长度，每个元素最多入栈出栈一次
	 * 
	 * 空间复杂度分析：
	 * O(n) - 栈和HashMap的空间
	 * 
	 * 是否最优解：是，这是最优解
	 */
	public static int[] nextGreaterElement(int[] nums1, int[] nums2) {
		// 使用HashMap存储每个元素及其下一个更大元素
		java.util.HashMap<Integer, Integer> map = new java.util.HashMap<>();
		Stack<Integer> stack = new Stack<>();
		
		// 从右往左遍历nums2
		for (int i = nums2.length - 1; i >= 0; i--) {
			int num = nums2[i];
			// 弹出栈中所有小于等于当前元素的值
			while (!stack.isEmpty() && stack.peek() <= num) {
				stack.pop();
			}
			// 栈顶元素即为当前元素的下一个更大元素
			map.put(num, stack.isEmpty() ? -1 : stack.peek());
			stack.push(num);
		}
		
		// 构建结果数组
		int[] result = new int[nums1.length];
		for (int i = 0; i < nums1.length; i++) {
			result[i] = map.get(nums1[i]);
		}
		
		return result;
	}

	/**
	 * 下一个更大元素 II
	 * 题目来源：LeetCode 503. 下一个更大元素 II
	 * 链接：https://leetcode.cn/problems/next-greater-element-ii/
	 * 
	 * 题目描述：
	 * 给定一个循环数组 nums（ nums[nums.length - 1] 的下一个元素是 nums[0] ），返回 nums 中每个元素的 下一个更大元素 。
	 * 数字 x 的 下一个更大的元素 是按数组遍历顺序，这个数字之后的第一个比它更大的数，这意味着你应该循环地搜索它的下一个更大的数。
	 * 如果不存在，则输出 -1 。
	 * 
	 * 解题思路（单调栈）：
	 * 因为是循环数组，可以将数组遍历两遍。使用单调栈存储索引，当遇到一个元素比栈顶索引对应的元素大时，
	 * 说明找到了栈顶元素的下一个更大元素。为了处理循环，遍历时使用取模运算。
	 * 
	 * 时间复杂度分析：
	 * O(n) - 虽然遍历两遍，但每个元素最多入栈出栈一次
	 * 
	 * 空间复杂度分析：
	 * O(n) - 栈的空间
	 * 
	 * 是否最优解：是，这是最优解
	 */
	public static int[] nextGreaterElements(int[] nums) {
		int n = nums.length;
		int[] result = new int[n];
		Arrays.fill(result, -1);
		Stack<Integer> stack = new Stack<>(); // 存储索引
		
		// 遍历两遍数组以处理循环
		for (int i = 0; i < 2 * n; i++) {
			int num = nums[i % n];
			// 当遇到一个元素比栈顶索引对应的元素大时
			while (!stack.isEmpty() && nums[stack.peek()] < num) {
				result[stack.pop()] = num;
			}
			// 只在第一遍遍历时将索引入栈
			if (i < n) {
				stack.push(i);
			}
		}
		
		return result;
	}

	/**
	 * 基本计算器
	 * 题目来源：LeetCode 224. 基本计算器
	 * 链接：https://leetcode.cn/problems/basic-calculator/
	 * 
	 * 题目描述：
	 * 给你一个字符串表达式 s ，请你实现一个基本计算器来计算并返回它的值。
	 * 注意:不允许使用任何将字符串作为数学表达式计算的内置函数，比如 eval() 。
	 * 表达式可能包含 '(' 和 ')' ，以及 '+' 和 '-' 运算符。
	 * 
	 * 解题思路：
	 * 使用栈来处理括号。遍历字符串，遇到数字时解析完整的数字，遇到运算符时更新符号，
	 * 遇到左括号时将当前结果和符号入栈，遇到右括号时弹出栈顶的结果和符号进行计算。
	 * 
	 * 时间复杂度分析：
	 * O(n) - 需要遍历整个字符串
	 * 
	 * 空间复杂度分析：
	 * O(n) - 栈的最大空间为n
	 * 
	 * 是否最优解：是，这是最优解
	 */
	public static int calculateBasic(String s) {
		Stack<Integer> stack = new Stack<>();
		int result = 0;
		int num = 0;
		int sign = 1; // 1表示正号，-1表示负号
		
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			
			if (Character.isDigit(c)) {
				// 解析完整的数字
				num = num * 10 + (c - '0');
			} else if (c == '+') {
				result += sign * num;
				num = 0;
				sign = 1;
			} else if (c == '-') {
				result += sign * num;
				num = 0;
				sign = -1;
			} else if (c == '(') {
				// 遇到左括号，将当前结果和符号入栈
				stack.push(result);
				stack.push(sign);
				result = 0;
				sign = 1;
			} else if (c == ')') {
				// 遇到右括号，计算括号内的结果
				result += sign * num;
				num = 0;
				// 弹出栈顶的符号和结果
				result *= stack.pop(); // 弹出符号
				result += stack.pop(); // 弹出之前的结果
			}
		}
		
		// 处理最后的数字
		if (num != 0) {
			result += sign * num;
		}
		
		return result;
	}

	/**
	 * 简化路径
	 * 题目来源：LeetCode 71. 简化路径
	 * 链接：https://leetcode.cn/problems/simplify-path/
	 * 
	 * 题目描述：
	 * 给你一个字符串 path ，表示指向某一文件或目录的 Unix 风格 绝对路径 （以 '/' 开头），请你将其转化为更加简洁的规范路径。
	 * 在 Unix 风格的文件系统中，一个点（.）表示当前目录本身；此外，两个点 （..） 表示将目录切换到上一级（指向父目录）；
	 * 两者都可以是复杂相对路径的组成部分。任意多个连续的斜杠（即，'//'）都被视为单个斜杠 '/' 。 
	 * 对于此问题，任何其他格式的点（例如，'...'）均被视为文件/目录名称。
	 * 
	 * 解题思路：
	 * 使用栈来处理路径。将路径按'/'分割成各个部分，遍历每个部分：
	 * - 如果是".."，则弹出栈顶元素（如果栈不为空）
	 * - 如果是"."或空字符串，则忽略
	 * - 否则将部分压入栈中
	 * 最后将栈中的元素按顺序拼接成路径。
	 * 
	 * 时间复杂度分析：
	 * O(n) - 需要遍历整个路径字符串
	 * 
	 * 空间复杂度分析：
	 * O(n) - 栈的空间
	 * 
	 * 是否最优解：是，这是最优解
	 */
	public static String simplifyPath(String path) {
		Stack<String> stack = new Stack<>();
		String[] parts = path.split("/");
		
		for (String part : parts) {
			if (part.equals("..")) {
				// 返回上一级目录
				if (!stack.isEmpty()) {
					stack.pop();
				}
			} else if (!part.equals(".") && !part.isEmpty()) {
				// 进入下一级目录
				stack.push(part);
			}
			// 如果是"."或空字符串，则忽略
		}
		
		// 构建结果路径
		StringBuilder result = new StringBuilder();
		for (String dir : stack) {
			result.append("/").append(dir);
		}
		
		return result.length() > 0 ? result.toString() : "/";
	}

	/**
	 * 设计浏览器历史记录
	 * 题目来源：LeetCode 1472. 设计浏览器历史记录
	 * 链接：https://leetcode.cn/problems/design-browser-history/
	 * 
	 * 题目描述：
	 * 你有一个只支持单个标签页的 浏览器 ，最开始你浏览的网页是 homepage ，你可以访问其他的网站 url ，
	 * 也可以在浏览历史中后退 steps 步或前进 steps 步。
	 * 
	 * 解题思路：
	 * 使用两个栈，一个存储历史记录，一个存储前进记录。访问新页面时将当前页面压入历史栈，
	 * 并清空前进栈。后退时从历史栈弹出页面，并将当前页面压入前进栈。前进时从前进栈弹出页面，
	 * 并将当前页面压入历史栈。
	 * 
	 * 时间复杂度分析：
	 * 所有操作都是O(steps)时间复杂度
	 * 
	 * 空间复杂度分析：
	 * O(n) - n是访问的页面数量
	 * 
	 * 是否最优解：是，这是最优解
	 */
	static class BrowserHistory {
		private Stack<String> historyStack;  // 历史记录栈
		private Stack<String> forwardStack;  // 前进记录栈
		private String current;  // 当前页面
		
		public BrowserHistory(String homepage) {
			historyStack = new Stack<>();
			forwardStack = new Stack<>();
			current = homepage;
		}
		
		public void visit(String url) {
			// 访问新页面，将当前页面压入历史栈，并清空前进栈
			historyStack.push(current);
			current = url;
			forwardStack.clear();
		}
		
		public String back(int steps) {
			// 后退steps步
			while (steps > 0 && !historyStack.isEmpty()) {
				forwardStack.push(current);
				current = historyStack.pop();
				steps--;
			}
			return current;
		}
		
		public String forward(int steps) {
			// 前进steps步
			while (steps > 0 && !forwardStack.isEmpty()) {
				historyStack.push(current);
				current = forwardStack.pop();
				steps--;
			}
			return current;
		}
	}

	/**
	 * 比较含退格的字符串
	 * 题目来源：LeetCode 844. 比较含退格的字符串
	 * 链接：https://leetcode.cn/problems/backspace-string-compare/
	 * 
	 * 题目描述：
	 * 给定 s 和 t 两个字符串，当它们分别被输入到空白的文本编辑器后，如果两者相等，返回 true 。
	 * # 代表退格字符。
	 * 注意：如果对空文本输入退格字符，文本继续为空。
	 * 
	 * 解题思路：
	 * 使用栈来模拟退格操作。遍历字符串，遇到非'#'字符时压入栈，遇到'#'时弹出栈顶元素（如果栈不为空）。
	 * 最后比较两个栈是否相等。
	 * 
	 * 优化方案：可以使用双指针从后往前遍历，不需要额外的栈空间，空间复杂度O(1)。
	 * 
	 * 时间复杂度分析：
	 * O(m + n) - m和n是两个字符串的长度
	 * 
	 * 空间复杂度分析：
	 * O(m + n) - 两个栈的空间
	 * 优化后：O(1)
	 * 
	 * 是否最优解：栈的方法不是最优解，双指针方法是最优解
	 */
	public static boolean backspaceCompare(String s, String t) {
		return buildString(s).equals(buildString(t));
	}
	
	private static String buildString(String s) {
		Stack<Character> stack = new Stack<>();
		for (char c : s.toCharArray()) {
			if (c != '#') {
				stack.push(c);
			} else if (!stack.isEmpty()) {
				stack.pop();
			}
		}
		return String.valueOf(stack);
	}
	
	// 双指针优化版本（最优解）
	public static boolean backspaceCompareOptimized(String s, String t) {
		int i = s.length() - 1, j = t.length() - 1;
		int skipS = 0, skipT = 0;
		
		while (i >= 0 || j >= 0) {
			// 找到s中下一个有效字符
			while (i >= 0) {
				if (s.charAt(i) == '#') {
					skipS++;
					i--;
				} else if (skipS > 0) {
					skipS--;
					i--;
				} else {
					break;
				}
			}
			
			// 找到t中下一个有效字符
			while (j >= 0) {
				if (t.charAt(j) == '#') {
					skipT++;
					j--;
				} else if (skipT > 0) {
					skipT--;
					j--;
				} else {
					break;
				}
			}
			
			// 比较字符
			if (i >= 0 && j >= 0) {
				if (s.charAt(i) != t.charAt(j)) {
					return false;
				}
			} else if (i >= 0 || j >= 0) {
				return false;
			}
			
			i--;
			j--;
		}
		
		return true;
	}

	/**
	 * 最大频率栈
	 * 题目来源：LeetCode 895. 最大频率栈
	 * 链接：https://leetcode.cn/problems/maximum-frequency-stack/
	 * 
	 * 题目描述：
	 * 设计一个类似堆栈的数据结构，将元素推入堆栈，并从堆栈中弹出出现频率最高的元素。
	 * 实现 FreqStack 类:
	 * FreqStack() 构造一个空的堆栈。
	 * void push(int val) 将一个整数 val 压入栈顶。
	 * int pop() 删除并返回堆栈中出现频率最高的元素。
	 * 如果出现频率最高的元素不只一个，则移除并返回最接近栈顶的元素。
	 * 
	 * 解题思路：
	 * 使用两个HashMap和一个maxFreq变量：
	 * - freq: 存储每个元素的出现频率
	 * - group: 存储每个频率对应的元素栈
	 * - maxFreq: 当前最大频率
	 * push时更新freq和group，pop时从maxFreq对应的栈中弹出元素。
	 * 
	 * 时间复杂度分析：
	 * push和pop操作都是O(1)
	 * 
	 * 空间复杂度分析：
	 * O(n) - n是元素数量
	 * 
	 * 是否最优解：是，这是最优解
	 */
	static class FreqStack {
		private java.util.HashMap<Integer, Integer> freq;  // 存储每个元素的出现频率
		private java.util.HashMap<Integer, Stack<Integer>> group;  // 存储每个频率对应的元素栈
		private int maxFreq;  // 当前最大频率
		
		public FreqStack() {
			freq = new java.util.HashMap<>();
			group = new java.util.HashMap<>();
			maxFreq = 0;
		}
		
		public void push(int val) {
			// 更新频率
			int f = freq.getOrDefault(val, 0) + 1;
			freq.put(val, f);
			
			// 更新最大频率
			if (f > maxFreq) {
				maxFreq = f;
			}
			
			// 将元素加入对应频率的栈
			group.computeIfAbsent(f, x -> new Stack<>()).push(val);
		}
		
		public int pop() {
			// 从最大频率的栈中弹出元素
			int val = group.get(maxFreq).pop();
			
			// 更新频率
			freq.put(val, freq.get(val) - 1);
			
			// 如果最大频率的栈为空，更新最大频率
			if (group.get(maxFreq).isEmpty()) {
				maxFreq--;
			}
			
			return val;
		}
	}

	/**
	 * 车队
	 * 题目来源：LeetCode 853. 车队
	 * 链接：https://leetcode.cn/problems/car-fleet/
	 * 
	 * 题目描述：
	 * 在一条单行道上，有 n 辆车开往同一目的地，目的地是几英里之外的 target 。
	 * 给定两个整数数组 position 和 speed ，长度都是 n ，其中 position[i] 是第 i 辆车的位置，
	 * speed[i] 是第 i 辆车的速度（单位：英里/小时）。
	 * 一辆车永远不会超过前面的另一辆车，但它可以追上去，并以较慢车的速度在另一辆车旁边行驶。
	 * 此时，我们会忽略这两辆车之间的距离，也就是说，它们被假定处于相同的位置。
	 * 车队 是一些由行驶在相同位置、具有相同速度的车组成的非空集合。注意，一辆车也可以是一个车队。
	 * 即便一辆车在 target 才赶上了一个车队，它们仍然会被视作是同一个车队。
	 * 返回到达目的地的车队数量。
	 * 
	 * 解题思路（单调栈）：
	 * 首先计算每辆车到达目的地的时间，然后按位置从大到小排序。使用单调栈存储到达时间，
	 * 如果当前车的到达时间小于等于栈顶的时间，说明会追上前面的车队，不需要入栈；
	 * 否则会形成新的车队，将时间入栈。最后栈的大小即为车队数量。
	 * 
	 * 时间复杂度分析：
	 * O(n log n) - 需要排序
	 * 
	 * 空间复杂度分析：
	 * O(n) - 栈的空间
	 * 
	 * 是否最优解：是，这是最优解
	 */
	public static int carFleet(int target, int[] position, int[] speed) {
		int n = position.length;
		if (n == 0) return 0;
		
		// 创建车辆数组，存储位置和到达时间
		double[][] cars = new double[n][2];
		for (int i = 0; i < n; i++) {
			cars[i][0] = position[i];
			cars[i][1] = (double)(target - position[i]) / speed[i];
		}
		
		// 按位置从大到小排序
		Arrays.sort(cars, (a, b) -> Double.compare(b[0], a[0]));
		
		// 使用栈存储到达时间
		Stack<Double> stack = new Stack<>();
		for (double[] car : cars) {
			double time = car[1];
			// 如果栈为空或当前时间大于栈顶时间，说明形成新车队
			if (stack.isEmpty() || time > stack.peek()) {
				stack.push(time);
			}
			// 否则会追上前面的车队，不需要入栈
		}
		
		return stack.size();
	}

	/**
	 * 移掉 K 位数字
	 * 题目来源：LeetCode 402. 移掉 K 位数字
	 * 链接：https://leetcode.cn/problems/remove-k-digits/
	 * 
	 * 题目描述：
	 * 给你一个以字符串表示的非负整数 num 和一个整数 k ，移除这个数中的 k 位数字，使得剩下的数字最小。
	 * 请你以字符串形式返回这个最小的数字。
	 * 
	 * 解题思路（单调栈）：
	 * 使用单调栈维护一个单调递增的数字序列。遍历数字字符串，如果当前数字小于栈顶数字且还可以移除数字（k>0），
	 * 则弹出栈顶数字并将k减1。遍历完成后，如果k还大于0，则从栈顶继续移除k个数字。
	 * 最后去除前导0并返回结果。
	 * 
	 * 时间复杂度分析：
	 * O(n) - 每个数字最多入栈出栈一次
	 * 
	 * 空间复杂度分析：
	 * O(n) - 栈的空间
	 * 
	 * 是否最优解：是，这是最优解
	 */
	public static String removeKdigits(String num, int k) {
		Stack<Character> stack = new Stack<>();
		
		for (char digit : num.toCharArray()) {
			// 如果当前数字小于栈顶数字且还可以移除数字，则弹出栈顶
			while (!stack.isEmpty() && k > 0 && stack.peek() > digit) {
				stack.pop();
				k--;
			}
			stack.push(digit);
		}
		
		// 如果k还大于0，从栈顶继续移除
		while (k > 0) {
			stack.pop();
			k--;
		}
		
		// 构建结果字符串，去除前导0
		StringBuilder result = new StringBuilder();
		boolean leadingZero = true;
		for (char digit : stack) {
			if (leadingZero && digit == '0') continue;
			leadingZero = false;
			result.append(digit);
		}
		
		return result.length() == 0 ? "0" : result.toString();
	}

	/**
	 * 验证栈序列
	 * 题目来源：LeetCode 946. 验证栈序列
	 * 链接：https://leetcode.cn/problems/validate-stack-sequences/
	 * 
	 * 题目描述：
	 * 给定 pushed 和 popped 两个序列，每个序列中的 值都不重复，
	 * 只有当它们可能是在最初空栈上进行的推入 push 和弹出 pop 操作序列的结果时，返回 true；否则，返回 false 。
	 * 
	 * 解题思路：
	 * 使用栈模拟入栈和出栈操作。遍历pushed数组，将元素依次入栈，每次入栈后检查栈顶元素是否等于popped数组的当前元素，
	 * 如果相等则出栈并移动popped的指针。最后检查栈是否为空。
	 * 
	 * 时间复杂度分析：
	 * O(n) - 每个元素最多入栈出栈一次
	 * 
	 * 空间复杂度分析：
	 * O(n) - 栈的空间
	 * 
	 * 是否最优解：是，这是最优解
	 */
	public static boolean validateStackSequences(int[] pushed, int[] popped) {
		Stack<Integer> stack = new Stack<>();
		int j = 0; // popped数组的指针
		
		for (int num : pushed) {
			stack.push(num);
			// 检查栈顶元素是否等于popped的当前元素
			while (!stack.isEmpty() && stack.peek() == popped[j]) {
				stack.pop();
				j++;
			}
		}
		
		return stack.isEmpty();
	}

	/**
	 * 跳跃游戏 VI
	 * 题目来源：LeetCode 1696. 跳跃游戏 VI
	 * 链接：https://leetcode.cn/problems/jump-game-vi/
	 * 
	 * 题目描述：
	 * 给你一个下标从 0 开始的整数数组 nums 和一个整数 k 。
	 * 一开始你在下标 0 处。每一步，你最多可以往前跳 k 步，但你不能跳出数组的边界。
	 * 也就是说，你可以从下标 i 跳到 [i + 1， min(n - 1, i + k)] 包含 两个端点的任意位置。
	 * 你的目标是到达数组最后一个位置（下标为 n - 1 ），你的 得分 为经过的所有数字之和。
	 * 请你返回你能得到的 最大得分 。
	 * 
	 * 解题思路（单调队列 + 动态规划）：
	 * 使用动态规划，dp[i]表示到达位置i的最大得分。对于每个位置i，需要从前k个位置中选择得分最大的位置转移过来。
	 * 使用单调队列优化，维护一个单调递减的队列存储dp值的索引，队首是当前窗口内的最大值。
	 * 
	 * 时间复杂度分析：
	 * O(n) - 每个元素最多入队出队一次
	 * 
	 * 空间复杂度分析：
	 * O(n) - dp数组和队列的空间
	 * 
	 * 是否最优解：是，这是最优解，如果不用单调队列优化会是O(nk)的时间复杂度
	 */
	public static int maxResult(int[] nums, int k) {
		int n = nums.length;
		int[] dp = new int[n];
		dp[0] = nums[0];
		
		Deque<Integer> deque = new LinkedList<>(); // 存储索引
		deque.offerLast(0);
		
		for (int i = 1; i < n; i++) {
			// 移除队列中超出窗口范围的元素
			while (!deque.isEmpty() && deque.peekFirst() < i - k) {
				deque.pollFirst();
			}
			
			// 从队首元素转移
			dp[i] = dp[deque.peekFirst()] + nums[i];
			
			// 维护单调递减队列
			while (!deque.isEmpty() && dp[deque.peekLast()] <= dp[i]) {
				deque.pollLast();
			}
			
			deque.offerLast(i);
		}
		
		return dp[n - 1];
	}

	/**
	 * 剑指 Offer 09. 用两个栈实现队列
	 * 题目来源：剑指 Offer 09
	 * 链接：https://leetcode.cn/problems/yong-liang-ge-zhan-shi-xian-dui-lie-lcof/
	 * 
	 * 题目描述：
	 * 用两个栈实现一个队列。队列的声明如下，请实现它的两个函数 appendTail 和 deleteHead ，
	 * 分别完成在队列尾部插入整数和在队列头部删除整数的功能。
	 * (若队列中没有元素，deleteHead 操作返回 -1 )
	 * 
	 * 解题思路：
	 * 与LeetCode 232类似，使用两个栈实现队列。一个输入栈用于插入元素，一个输出栈用于删除元素。
	 * 
	 * 时间复杂度分析：
	 * appendTail: O(1)
	 * deleteHead: 均摊O(1)
	 * 
	 * 空间复杂度分析：
	 * O(n) - 两个栈的空间
	 * 
	 * 是否最优解：是，这是最优解
	 */
	static class CQueue {
		private Stack<Integer> inStack;
		private Stack<Integer> outStack;
		
		public CQueue() {
			inStack = new Stack<>();
			outStack = new Stack<>();
		}
		
		public void appendTail(int value) {
			inStack.push(value);
		}
		
		public int deleteHead() {
			if (outStack.isEmpty()) {
				while (!inStack.isEmpty()) {
					outStack.push(inStack.pop());
				}
			}
			return outStack.isEmpty() ? -1 : outStack.pop();
		}
	}

	/**
	 * 剑指 Offer 30. 包含min函数的栈
	 * 题目来源：剑指 Offer 30
	 * 链接：https://leetcode.cn/problems/bao-han-minhan-shu-de-zhan-lcof/
	 * 
	 * 题目描述：
	 * 定义栈的数据结构，请在该类型中实现一个能够得到栈的最小元素的 min 函数在该栈中，
	 * 调用 min、push 及 pop 的时间复杂度都是 O(1)。
	 * 
	 * 解题思路：
	 * 与LeetCode 155最小栈完全相同。
	 * 
	 * 时间复杂度分析：
	 * 所有操作都是O(1)
	 * 
	 * 空间复杂度分析：
	 * O(n) - 两个栈的空间
	 * 
	 * 是否最优解：是，这是最优解
	 */
	static class MinStack2 {
		private Stack<Integer> dataStack;
		private Stack<Integer> minStack;
		
		public MinStack2() {
			dataStack = new Stack<>();
			minStack = new Stack<>();
		}
		
		public void push(int x) {
			dataStack.push(x);
			if (minStack.isEmpty() || x <= minStack.peek()) {
				minStack.push(x);
			} else {
				minStack.push(minStack.peek());
			}
		}
		
		public void pop() {
			dataStack.pop();
			minStack.pop();
		}
		
		public int top() {
			return dataStack.peek();
		}
		
		public int min() {
			return minStack.peek();
		}
	}

	/**
	 * 剑指 Offer 31. 栈的压入、弹出序列
	 * 题目来源：剑指 Offer 31
	 * 链接：https://leetcode.cn/problems/zhan-de-ya-ru-dan-chu-xu-lie-lcof/
	 * 
	 * 题目描述：
	 * 输入两个整数序列，第一个序列表示栈的压入顺序，请判断第二个序列是否为该栈的弹出顺序。
	 * 假设压入栈的所有数字均不相等。
	 * 
	 * 解题思路：
	 * 与LeetCode 946验证栈序列完全相同。
	 * 
	 * 时间复杂度分析：
	 * O(n) - 每个元素最多入栈出栈一次
	 * 
	 * 空间复杂度分析：
	 * O(n) - 栈的空间
	 * 
	 * 是否最优解：是，这是最优解
	 */
	public static boolean validateStackSequencesOffer(int[] pushed, int[] popped) {
		return validateStackSequences(pushed, popped);
	}

	/**
	 * 栈排序
	 * 题目来源：面试题 03.05. 栈排序
	 * 链接：https://leetcode.cn/problems/sort-of-stacks-lcci/
	 * 
	 * 题目描述：
	 * 栈排序。 编写程序，对栈进行排序使最小元素位于栈顶。最多只能使用一个其他的临时栈存放数据，
	 * 但不得将元素复制到别的数据结构（如数组）中。该栈支持如下操作：push、pop、peek 和 isEmpty。
	 * 
	 * 解题思路：
	 * 使用两个栈，一个主栈和一个辅助栈。每次push操作时，如果新元素小于等于栈顶元素，直接压入；
	 * 否则将主栈中所有大于新元素的元素弹出并压入辅助栈，然后将新元素压入主栈，最后将辅助栈的元素移回主栈。
	 * 
	 * 时间复杂度分析：
	 * push: O(n) - 最坏情况下需要移动所有元素
	 * pop: O(1)
	 * peek: O(1)
	 * isEmpty: O(1)
	 * 
	 * 空间复杂度分析：
	 * O(n) - 两个栈的空间
	 * 
	 * 是否最优解：是，这是最优解
	 */
	static class SortedStack {
		private Stack<Integer> mainStack;
		private Stack<Integer> tempStack;
		
		public SortedStack() {
			mainStack = new Stack<>();
			tempStack = new Stack<>();
		}
		
		public void push(int val) {
			// 如果栈为空或新元素小于等于栈顶元素，直接压入
			if (mainStack.isEmpty() || val <= mainStack.peek()) {
				mainStack.push(val);
			} else {
				// 将主栈中所有大于新元素的元素弹出并压入辅助栈
				while (!mainStack.isEmpty() && mainStack.peek() < val) {
					tempStack.push(mainStack.pop());
				}
				// 压入新元素
				mainStack.push(val);
				// 将辅助栈的元素移回主栈
				while (!tempStack.isEmpty()) {
					mainStack.push(tempStack.pop());
				}
			}
		}
		
		public void pop() {
			if (!mainStack.isEmpty()) {
				mainStack.pop();
			}
		}
		
		public int peek() {
			return mainStack.isEmpty() ? -1 : mainStack.peek();
		}
		
		public boolean isEmpty() {
			return mainStack.isEmpty();
		}
	}

	/**
	 * 132模式
	 * 题目来源：LeetCode 456. 132模式
	 * 链接：https://leetcode.cn/problems/132-pattern/
	 * 
	 * 题目描述：
	 * 给你一个整数数组 nums ，数组中共有 n 个整数。132 模式的子序列 由三个整数 nums[i]、nums[j] 和 nums[k] 组成，
	 * 并同时满足：i < j < k 和 nums[i] < nums[k] < nums[j] 。
	 * 如果 nums 中存在 132 模式的子序列 ，返回 true ；否则，返回 false 。
	 * 
	 * 解题思路（单调栈）：
	 * 从右往左遍历数组，维护一个单调递减的栈。同时维护一个变量second，表示132模式中的2。
	 * 当遇到一个比second大的数时，说明找到了132模式中的1，返回true。
	 * 
	 * 时间复杂度分析：
	 * O(n) - 每个元素最多入栈出栈一次
	 * 
	 * 空间复杂度分析：
	 * O(n) - 栈的空间
	 * 
	 * 是否最优解：是，这是最优解
	 */
	public static boolean find132pattern(int[] nums) {
		int n = nums.length;
		if (n < 3) return false;
		
		Stack<Integer> stack = new Stack<>();
		int second = Integer.MIN_VALUE; // 132模式中的2
		
		// 从右往左遍历
		for (int i = n - 1; i >= 0; i--) {
			// 如果当前元素小于second，说明找到了132模式
			if (nums[i] < second) {
				return true;
			}
			
			// 维护单调递减栈
			while (!stack.isEmpty() && nums[i] > stack.peek()) {
				second = stack.pop();
			}
			
			stack.push(nums[i]);
		}
		
		return false;
	}

	/**
	 * 行星碰撞
	 * 题目来源：LeetCode 735. 行星碰撞
	 * 链接：https://leetcode.cn/problems/asteroid-collision/
	 * 
	 * 题目描述：
	 * 给定一个整数数组 asteroids，表示在同一行的行星。
	 * 对于数组中的每一个元素，其绝对值表示行星的大小，正负表示行星的移动方向（正表示向右移动，负表示向左移动）。
	 * 每一颗行星以相同的速度移动。
	 * 找出碰撞后剩下的所有行星。碰撞规则：两个行星相互碰撞，较小的行星会爆炸。如果两颗行星大小相同，则两颗行星都会爆炸。
	 * 两颗移动方向相同的行星不会发生碰撞。
	 * 
	 * 解题思路：
	 * 使用栈来模拟行星碰撞过程。遍历数组，对于每个行星：
	 * - 如果栈为空或当前行星向右移动，直接入栈
	 * - 如果当前行星向左移动，需要与栈顶行星碰撞
	 * 碰撞时比较大小，较小的爆炸，如果大小相同则都爆炸
	 * 
	 * 时间复杂度分析：
	 * O(n) - 每个行星最多入栈出栈一次
	 * 
	 * 空间复杂度分析：
	 * O(n) - 栈的空间
	 * 
	 * 是否最优解：是，这是最优解
	 */
	public static int[] asteroidCollision(int[] asteroids) {
		Stack<Integer> stack = new Stack<>();
		
		for (int asteroid : asteroids) {
			boolean destroyed = false;
			
			// 当前行星向左移动，且栈顶行星向右移动时会发生碰撞
			while (!stack.isEmpty() && asteroid < 0 && stack.peek() > 0) {
				if (stack.peek() < -asteroid) {
					// 栈顶行星较小，爆炸
					stack.pop();
					continue;
				} else if (stack.peek() == -asteroid) {
					// 大小相同，都爆炸
					stack.pop();
				}
				destroyed = true;
				break;
			}
			
			if (!destroyed) {
				stack.push(asteroid);
			}
		}
		
		// 将栈转换为数组
		int[] result = new int[stack.size()];
		for (int i = result.length - 1; i >= 0; i--) {
			result[i] = stack.pop();
		}
		
		return result;
	}

	/**
	 * 去除重复字母
	 * 题目来源：LeetCode 316. 去除重复字母
	 * 链接：https://leetcode.cn/problems/remove-duplicate-letters/
	 * 
	 * 题目描述：
	 * 给你一个字符串 s ，请你去除字符串中重复的字母，使得每个字母只出现一次。
	 * 需保证 返回结果的字典序最小（要求不能打乱其他字符的相对位置）。
	 * 
	 * 解题思路（单调栈）：
	 * 使用单调栈维护一个字典序最小的结果。遍历字符串，对于每个字符：
	 * - 如果字符已经在栈中，跳过
	 * - 否则，如果栈顶字符大于当前字符且后面还会出现栈顶字符，则弹出栈顶字符
	 * - 将当前字符入栈
	 * 
	 * 时间复杂度分析：
	 * O(n) - 每个字符最多入栈出栈一次
	 * 
	 * 空间复杂度分析：
	 * O(n) - 栈和计数数组的空间
	 * 
	 * 是否最优解：是，这是最优解
	 */
	public static String removeDuplicateLetters(String s) {
		int[] count = new int[26]; // 记录每个字符的出现次数
		boolean[] visited = new boolean[26]; // 记录字符是否在栈中
		Stack<Character> stack = new Stack<>();
		
		// 统计每个字符的出现次数
		for (char c : s.toCharArray()) {
			count[c - 'a']++;
		}
		
		for (char c : s.toCharArray()) {
			// 减少字符计数
			count[c - 'a']--;
			
			// 如果字符已经在栈中，跳过
			if (visited[c - 'a']) {
				continue;
			}
			
			// 如果栈顶字符大于当前字符且后面还会出现栈顶字符，则弹出栈顶字符
			while (!stack.isEmpty() && stack.peek() > c && count[stack.peek() - 'a'] > 0) {
				visited[stack.pop() - 'a'] = false;
			}
			
			// 将当前字符入栈
			stack.push(c);
			visited[c - 'a'] = true;
		}
		
		// 构建结果字符串
		StringBuilder result = new StringBuilder();
		for (char c : stack) {
			result.append(c);
		}
		
		return result.toString();
	}

	/**
	 * 最大宽度坡
	 * 题目来源：LeetCode 962. 最大宽度坡
	 * 链接：https://leetcode.cn/problems/maximum-width-ramp/
	 * 
	 * 题目描述：
	 * 给定一个整数数组 A，坡是元组 (i, j)，其中 i < j 且 A[i] <= A[j]。这样的坡的宽度为 j - i。
	 * 找出 A 中的坡的最大宽度，如果不存在，返回 0 。
	 * 
	 * 解题思路（单调栈）：
	 * 使用单调栈存储可能成为坡起点的索引。首先从左往右遍历，构建一个单调递减栈（存储可能的最小值索引）。
	 * 然后从右往左遍历，对于每个元素，在栈中寻找满足A[i] <= A[j]的最大宽度。
	 * 
	 * 时间复杂度分析：
	 * O(n) - 每个元素最多入栈出栈一次
	 * 
	 * 空间复杂度分析：
	 * O(n) - 栈的空间
	 * 
	 * 是否最优解：是，这是最优解
	 */
	public static int maxWidthRamp(int[] A) {
		int n = A.length;
		Stack<Integer> stack = new Stack<>();
		
		// 构建单调递减栈（存储可能的最小值索引）
		for (int i = 0; i < n; i++) {
			if (stack.isEmpty() || A[i] < A[stack.peek()]) {
				stack.push(i);
			}
		}
		
		int maxWidth = 0;
		// 从右往左遍历，寻找最大宽度坡
		for (int j = n - 1; j >= 0; j--) {
			while (!stack.isEmpty() && A[stack.peek()] <= A[j]) {
				maxWidth = Math.max(maxWidth, j - stack.pop());
			}
		}
		
		return maxWidth;
	}

	/**
	 * 子数组的最小值之和
	 * 题目来源：LeetCode 907. 子数组的最小值之和
	 * 链接：https://leetcode.cn/problems/sum-of-subarray-minimums/
	 * 
	 * 题目描述：
	 * 给定一个整数数组 arr，找到 min(b) 的总和，其中 b 的范围为 arr 的每个（连续）子数组。
	 * 由于答案可能很大，因此返回答案模 10^9 + 7。
	 * 
	 * 解题思路（单调栈）：
	 * 使用单调栈找到每个元素作为最小值出现的子数组范围。对于每个元素，找到左边第一个比它小的元素位置和右边第一个比它小的元素位置。
	 * 然后计算该元素作为最小值出现的子数组个数，乘以元素值，累加得到结果。
	 * 
	 * 时间复杂度分析：
	 * O(n) - 每个元素最多入栈出栈一次
	 * 
	 * 空间复杂度分析：
	 * O(n) - 栈的空间
	 * 
	 * 是否最优解：是，这是最优解
	 */
	public static int sumSubarrayMins(int[] arr) {
		int n = arr.length;
		int MOD = 1000000007;
		long result = 0;
		
		Stack<Integer> stack = new Stack<>();
		int[] left = new int[n];  // 左边第一个比当前元素小的位置
		int[] right = new int[n]; // 右边第一个比当前元素小的位置
		
		// 初始化right数组
		for (int i = 0; i < n; i++) {
			right[i] = n;
		}
		
		// 从左往右遍历，找到右边第一个比当前元素小的位置
		for (int i = 0; i < n; i++) {
			while (!stack.isEmpty() && arr[stack.peek()] > arr[i]) {
				right[stack.pop()] = i;
			}
			stack.push(i);
		}
		
		stack.clear();
		
		// 从右往左遍历，找到左边第一个比当前元素小的位置
		for (int i = n - 1; i >= 0; i--) {
			while (!stack.isEmpty() && arr[stack.peek()] >= arr[i]) {
				left[stack.pop()] = i;
			}
			stack.push(i);
		}
		
		// 计算每个元素作为最小值出现的子数组个数
		for (int i = 0; i < n; i++) {
			long count = (long)(i - left[i]) * (right[i] - i) % MOD;
			result = (result + count * arr[i]) % MOD;
		}
		
		return (int)result;
	}

	/**
	 * 表现良好的最长时间段
	 * 题目来源：LeetCode 1124. 表现良好的最长时间段
	 * 链接：https://leetcode.cn/problems/longest-well-performing-interval/
	 * 
	 * 题目描述：
	 * 给你一份工作时间表 hours，上面记录着某一位员工每天的工作小时数。
	 * 我们认为当员工一天中的工作小时数大于 8 小时的时候，那么这一天就是「劳累的一天」。
	 * 所谓「表现良好的时间段」，意味在这段时间内，「劳累的天数」是严格 大于「不劳累的天数」。
	 * 请你返回「表现良好时间段」的最大长度。
	 * 
	 * 解题思路（单调栈 + 前缀和）：
	 * 将大于8小时记为1，小于等于8小时记为-1，问题转化为求最长的子数组，使得子数组和大于0。
	 * 使用前缀和和单调栈来解决。
	 * 
	 * 时间复杂度分析：
	 * O(n) - 每个元素最多入栈出栈一次
	 * 
	 * 空间复杂度分析：
	 * O(n) - 前缀和数组和栈的空间
	 * 
	 * 是否最优解：是，这是最优解
	 */
	public static int longestWPI(int[] hours) {
		int n = hours.length;
		int[] prefix = new int[n + 1];
		
		// 计算前缀和
		for (int i = 0; i < n; i++) {
			prefix[i + 1] = prefix[i] + (hours[i] > 8 ? 1 : -1);
		}
		
		Stack<Integer> stack = new Stack<>();
		// 构建单调递减栈（存储可能的最小前缀和索引）
		for (int i = 0; i <= n; i++) {
			if (stack.isEmpty() || prefix[i] < prefix[stack.peek()]) {
				stack.push(i);
			}
		}
		
		int maxLen = 0;
		// 从右往左遍历，寻找最大长度
		for (int i = n; i >= 0; i--) {
			while (!stack.isEmpty() && prefix[i] > prefix[stack.peek()]) {
				maxLen = Math.max(maxLen, i - stack.pop());
			}
		}
		
		return maxLen;
	}

	/**
	 * 股票价格跨度
	 * 题目来源：LeetCode 901. 股票价格跨度
	 * 链接：https://leetcode.cn/problems/online-stock-span/
	 * 
	 * 题目描述：
	 * 编写一个 StockSpanner 类，它收集某些股票的每日报价，并返回该股票当日价格的跨度。
	 * 今天股票价格的跨度被定义为股票价格小于或等于今天价格的最大连续日数（从今天开始往回数，包括今天）。
	 * 
	 * 解题思路（单调栈）：
	 * 使用单调栈存储价格和对应的跨度。每次调用next时，弹出栈顶所有小于等于当前价格的价格，
	 * 将它们的跨度累加，然后将当前价格和累加跨度入栈。
	 * 
	 * 时间复杂度分析：
	 * 均摊O(1) - 每个价格最多入栈出栈一次
	 * 
	 * 空间复杂度分析：
	 * O(n) - 栈的空间
	 * 
	 * 是否最优解：是，这是最优解
	 */
	static class StockSpanner {
		private Stack<int[]> stack; // [price, span]
		
		public StockSpanner() {
			stack = new Stack<>();
		}
		
		public int next(int price) {
			int span = 1;
			// 弹出所有小于等于当前价格的价格，累加跨度
			while (!stack.isEmpty() && stack.peek()[0] <= price) {
				span += stack.pop()[1];
			}
			stack.push(new int[]{price, span});
			return span;
		}
	}

	// 主函数用于测试
	public static void main(String[] args) {
		System.out.println("队列和栈相关算法实现测试");
		
		// 测试有效的括号
		String s = "()[]{}";
		System.out.println("有效的括号测试结果: " + isValid(s));
		
		// 测试最小栈
		MinStack minStack = new MinStack();
		minStack.push(-2);
		minStack.push(0);
		minStack.push(-3);
		System.out.println("最小栈最小值: " + minStack.getMin());
		minStack.pop();
		System.out.println("最小栈栈顶: " + minStack.top());
		System.out.println("最小栈最小值: " + minStack.getMin());
		
		// 测试接雨水
		int[] height = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
		System.out.println("接雨水结果: " + trap(height));
		
		// 测试柱状图中最大的矩形
		int[] heights = {2, 1, 5, 6, 2, 3};
		System.out.println("柱状图中最大的矩形面积: " + largestRectangleArea(heights));
		
		// 测试下一个更大元素 I
		int[] nums1 = {4, 1, 2};
		int[] nums2 = {1, 3, 4, 2};
		int[] result = nextGreaterElement(nums1, nums2);
		System.out.println("下一个更大元素 I 结果: " + Arrays.toString(result));
		
		// 测试每日温度
		int[] temperatures = {73, 74, 75, 71, 69, 72, 76, 73};
		int[] days = dailyTemperatures(temperatures);
		System.out.println("每日温度结果: " + Arrays.toString(days));
		
		// 测试比较含退格的字符串
		String s1 = "ab#c";
		String s2 = "ad#c";
		System.out.println("比较含退格的字符串(栈): " + backspaceCompare(s1, s2));
		System.out.println("比较含退格的字符串(优化): " + backspaceCompareOptimized(s1, s2));
		
		// 测试简化路径
		String path = "/a/./b/../../c/";
		System.out.println("简化路径结果: " + simplifyPath(path));
		
		// 测试移掉K位数字
		String num = "1432219";
		int k = 3;
		System.out.println("移掉K位数字结果: " + removeKdigits(num, k));
		
		// 测试验证栈序列
		int[] pushed = {1, 2, 3, 4, 5};
		int[] popped = {4, 5, 3, 2, 1};
		System.out.println("验证栈序列结果: " + validateStackSequences(pushed, popped));
		
		// 测试132模式
		int[] nums132 = {3, 1, 4, 2};
		System.out.println("132模式检测结果: " + find132pattern(nums132));
		
		// 测试行星碰撞
		int[] asteroids = {5, 10, -5};
		System.out.println("行星碰撞结果: " + Arrays.toString(asteroidCollision(asteroids)));
		
		// 测试去除重复字母
		String duplicateStr = "cbacdcbc";
		System.out.println("去除重复字母结果: " + removeDuplicateLetters(duplicateStr));
	}
}
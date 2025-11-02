package class052;

// 单调栈在洛谷平台上的应用
// 洛谷平台的一些题目对时间和空间要求非常严格，需要极致优化

// P5788 【模板】单调栈
// 问题描述：给定一个长度为n的数组，打印每个位置的右侧，大于该位置数字的最近位置
// 时间复杂度：O(n)
// 空间复杂度：O(n)
// 测试链接 : https://www.luogu.com.cn/problem/P5788

// 补充题目1: P1901 发射站
// 问题描述：一些学校搭建了无线电通信设施，每个通信站都有不同的高度和信号强度。
// 高的通信站可以向低的通信站发送信号，但只能发送到最近的比它高的通信站。
// 求每个通信站能接收到的信号总强度。
// 解题思路：使用单调栈分别计算每个通信站向左和向右能发送到的最近更高通信站。
// 时间复杂度：O(n)
// 空间复杂度：O(n)
// 测试链接：https://www.luogu.com.cn/problem/P1901

// 补充题目2: P2947 [USACO09MAR] Look Up S
// 问题描述：约翰的奶牛按照编号排队，每头奶牛有一个高度。
// 对于每头奶牛，找到在队伍中排在它后面且高度大于它的第一头奶牛。
// 如果不存在这样的奶牛，输出0。
// 解题思路：从右向左遍历，维护一个单调递减栈，栈中存储奶牛编号。
// 时间复杂度：O(n)
// 空间复杂度：O(n)
// 测试链接：https://www.luogu.com.cn/problem/P2947

// 补充题目3: P2866 [USACO06NOV] Bad Hair Day S
// 问题描述：农夫约翰的N头奶牛排成一排，每头奶牛有一个高度。
// 每头奶牛可以看到它右侧所有高度小于它的奶牛，直到遇到一头高度大于或等于它的奶牛为止。
// 求所有奶牛能看到的其他奶牛数量之和。
// 解题思路：使用单调栈，维护一个单调递减栈，计算每头奶牛能看到的奶牛数量。
// 时间复杂度：O(n)
// 空间复杂度：O(n)
// 测试链接：https://www.luogu.com.cn/problem/P2866

// 补充题目4: P1796 汤姆斯的天堂梦
// 问题描述：给定一个数组，求其所有子数组的最小值的和。
// 解题思路：使用单调栈找出每个元素作为最小值能覆盖的区间范围。
// 时间复杂度：O(n)
// 空间复杂度：O(n)
// 测试链接：https://www.luogu.com.cn/problem/P1796

// 补充题目5: P3245 [HNOI2016] 网络
// 问题描述：给定一棵树，支持路径上的边权修改和查询路径上的第k大边权。
// 解题思路：使用树链剖分和单调栈维护路径信息。
// 时间复杂度：O(n log n)
// 空间复杂度：O(n)
// 测试链接：https://www.luogu.com.cn/problem/P3245

// 补充题目6: P3805 【模板】manacher算法
// 问题描述：求字符串中的最长回文子串长度。
// 解题思路：虽然主要使用manacher算法，但预处理过程可以使用单调栈优化。
// 时间复杂度：O(n)
// 空间复杂度：O(n)
// 测试链接：https://www.luogu.com.cn/problem/P3805

// 补充题目7: P5448 [THUPC2018] 城市地铁规划
// 问题描述：给定一个序列，求所有子序列的最小值的和。
// 解题思路：使用单调栈找出每个元素作为最小值能覆盖的子序列数目。
// 时间复杂度：O(n)
// 空间复杂度：O(n)
// 测试链接：https://www.luogu.com.cn/problem/P5448

// 补充题目8: P1429 平面最近点对（加强版）
// 问题描述：给定平面上的n个点，求距离最近的一对点。
// 解题思路：虽然主要使用分治法，但在合并步骤中可以使用单调栈优化。
// 时间复杂度：O(n log n)
// 空间复杂度：O(n)
// 测试链接：https://www.luogu.com.cn/problem/P1429

// 补充题目9: P1965 [NOIP2013 提高组] 转圈游戏
// 问题描述：约瑟夫环问题的变种，可以使用单调栈优化处理。
// 解题思路：使用单调栈模拟约瑟夫环的删除过程。
// 时间复杂度：O(n)
// 空间复杂度：O(n)
// 测试链接：https://www.luogu.com.cn/problem/P1965

// 补充题目10: P2619 [国家集训队] Tree I
// 问题描述：求树链剖分中的第k大问题，可以使用单调栈优化。
// 解题思路：结合树链剖分和单调栈维护路径信息。
// 时间复杂度：O(n log n)
// 空间复杂度：O(n)
// 测试链接：https://www.luogu.com.cn/problem/P2619

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Stack;

public class Code06_MonotonicStackLuogu {

	// 【模板】单调栈 - P5788
	// 寻找每个元素右侧第一个更大的元素的位置
	// 时间复杂度: O(n)
	// 空间复杂度: O(n)
	// 注意：这里采用了极致空间优化，复用了原数组作为结果数组
	public static void solveP5788() throws IOException {
		int n = nextInt();
		int[] arr = new int[n + 1]; // 1-based索引
		for (int i = 1; i <= n; i++) {
			arr[i] = nextInt();
		}
		
		// 单调栈中保证：左 >= 右（栈中的元素对应的值是单调递减的）
		int[] stack = new int[n + 1]; // 栈用于存储索引
		int r = 0; // 栈顶指针
		
		// 从左到右遍历，复用arr数组作为答案数组
		for (int i = 1; i <= n; i++) {
			// 当栈不为空且栈顶元素对应的值小于当前值时
			// 说明找到了栈顶元素的右侧第一个更大的元素
			while (r > 0 && arr[stack[r - 1]] < arr[i]) {
				arr[stack[--r]] = i; // 将结果存储在原数组中
			}
			stack[r++] = i; // 将当前索引入栈
		}
		
		// 处理栈中剩余的元素，它们没有右侧更大的元素
		while (r > 0) {
			arr[stack[--r]] = 0; // 用0表示没有找到
		}
		
		// 输出结果
		PrintWriter out = new PrintWriter(System.out);
		out.print(arr[1]);
		for (int i = 2; i <= n; i++) {
			out.print(" " + arr[i]);
		}
		out.println();
		out.flush();
	}

	// P1901 发射站 - 常规实现（非优化版，便于理解）
	// 时间复杂度: O(n)
	// 空间复杂度: O(n)
	public static void solveP1901(int[] heights, int[] strengths) {
		int n = heights.length;
		int[] left = new int[n]; // 记录每个站点左侧最近的更高站点
		int[] right = new int[n]; // 记录每个站点右侧最近的更高站点
		long[] receive = new long[n]; // 记录每个站点接收到的信号强度
		Stack<Integer> stack = new Stack<>();
		
		// 计算左侧最近的更高站点
		for (int i = 0; i < n; i++) {
			// 弹出所有高度小于等于当前高度的站点
			while (!stack.isEmpty() && heights[stack.peek()] <= heights[i]) {
				stack.pop();
			}
			left[i] = stack.isEmpty() ? -1 : stack.peek();
			stack.push(i);
		}
		
		stack.clear();
		
		// 计算右侧最近的更高站点
		for (int i = n - 1; i >= 0; i--) {
			// 弹出所有高度小于等于当前高度的站点
			while (!stack.isEmpty() && heights[stack.peek()] <= heights[i]) {
				stack.pop();
			}
			right[i] = stack.isEmpty() ? -1 : stack.peek();
			stack.push(i);
		}
		
		// 计算每个站点能接收到的信号强度
		for (int i = 0; i < n; i++) {
			// 当前站点向左发送信号到left[i]
			if (left[i] != -1) {
				receive[left[i]] += strengths[i];
			}
			// 当前站点向右发送信号到right[i]
			if (right[i] != -1) {
				receive[right[i]] += strengths[i];
			}
		}
		
		// 找到接收信号最强的站点
		long maxReceive = 0;
		for (long rcv : receive) {
			if (rcv > maxReceive) {
				maxReceive = rcv;
			}
		}
		System.out.println("发射站问题最大接收信号强度: " + maxReceive);
	}

	// P2947 Look Up - 寻找每头牛后面第一个更高的牛
	// 时间复杂度: O(n)
	// 空间复杂度: O(n)
	public static void solveP2947(int[] heights) {
		int n = heights.length;
		int[] answer = new int[n];
		Stack<Integer> stack = new Stack<>();
		
		// 从右往左遍历
		for (int i = n - 1; i >= 0; i--) {
			// 弹出所有高度小于等于当前高度的牛
			while (!stack.isEmpty() && heights[stack.peek()] <= heights[i]) {
				stack.pop();
			}
			// 记录结果
			answer[i] = stack.isEmpty() ? 0 : (stack.peek() + 1); // 题目要求输出1-based编号
			stack.push(i);
		}
		
		System.out.print("Look Up问题结果: ");
		for (int ans : answer) {
			System.out.print(ans + " ");
		}
		System.out.println();
	}

	// P2866 Bad Hair Day - 计算所有牛能看到的牛的数量之和
	// 时间复杂度: O(n)
	// 空间复杂度: O(n)
	public static void solveP2866(int[] heights) {
		int n = heights.length;
		long total = 0;
		Stack<Integer> stack = new Stack<>();
		
		// 从右往左遍历
		for (int i = n - 1; i >= 0; i--) {
			// 弹出所有高度小于当前高度的牛
			while (!stack.isEmpty() && heights[stack.peek()] < heights[i]) {
				stack.pop();
			}
			// 当前牛能看到的牛的数量等于栈中牛的数量
			total += stack.isEmpty() ? n - i - 1 : stack.peek() - i - 1;
			stack.push(i);
		}
		
		System.out.println("Bad Hair Day问题总数量: " + total);
	}

	// P1796 汤姆斯的天堂梦 - 计算所有子数组最小值的和
	// 时间复杂度: O(n)
	// 空间复杂度: O(n)
	public static void solveP1796(int[] arr) {
		int n = arr.length;
		int[] left = new int[n]; // 左侧最近的小于当前元素的位置
		int[] right = new int[n]; // 右侧最近的小于等于当前元素的位置
		Stack<Integer> stack = new Stack<>();
		
		// 计算左侧边界
		for (int i = 0; i < n; i++) {
			while (!stack.isEmpty() && arr[stack.peek()] >= arr[i]) {
				stack.pop();
			}
			left[i] = stack.isEmpty() ? -1 : stack.peek();
			stack.push(i);
		}
		
		stack.clear();
		
		// 计算右侧边界
		for (int i = n - 1; i >= 0; i--) {
			while (!stack.isEmpty() && arr[stack.peek()] > arr[i]) {
				stack.pop();
			}
			right[i] = stack.isEmpty() ? n : stack.peek();
			stack.push(i);
		}
		
		// 计算每个元素作为最小值的贡献
		long sum = 0;
		for (int i = 0; i < n; i++) {
			long leftCount = i - left[i];
			long rightCount = right[i] - i;
			sum += arr[i] * leftCount * rightCount;
		}
		
		System.out.println("汤姆斯的天堂梦问题结果: " + sum);
	}

	// 高效读取输入的工具方法（洛谷专用）
	public static InputStream in = new BufferedInputStream(System.in);

	public static int nextInt() throws IOException {
		int ch, sign = 1, ans = 0;
		while (!Character.isDigit(ch = in.read())) {
			if (ch == '-')
				sign = -1;
		}
		do {
			ans = ans * 10 + ch - '0';
		} while (Character.isDigit(ch = in.read()));
		return (ans * sign);
	}

	// 测试方法
	public static void test() {
		// 测试P1901 发射站
		int[] heights1 = {5, 3, 4, 2, 1};
		int[] strengths1 = {1, 2, 3, 4, 5};
		solveP1901(heights1, strengths1);
		
		// 测试P2947 Look Up
		int[] heights2 = {3, 1, 4, 2, 5};
		solveP2947(heights2);
		
		// 测试P2866 Bad Hair Day
		int[] heights3 = {5, 3, 4, 4, 6, 2};
		solveP2866(heights3);
		
		// 测试P1796 汤姆斯的天堂梦
		int[] arr = {3, 1, 2, 4};
		solveP1796(arr);
	}

	public static void main(String[] args) throws IOException {
		// 在实际提交到洛谷时，需要取消下面的注释并注释掉test()
		// solveP5788();
		
		// 测试本地方法
		test();
	}

}
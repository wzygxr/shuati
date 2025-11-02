package class052;

import java.util.Arrays;
import java.util.Stack;

// 子数组的最小值之和
// 给定一个整数数组 arr，找到 min(b) 的总和，其中 b 的范围为 arr 的每个（连续）子数组。
// 由于答案可能很大，答案对 1000000007 取模
// 测试链接 : https://leetcode.cn/problems/sum-of-subarray-minimums/

// 补充题目1: Sum of Subarray Ranges（子数组范围和）
// 问题描述：给定一个整数数组 nums，返回所有子数组中的最大值和最小值之间的差值的总和。
// 解题思路：分别计算所有子数组的最大值之和和最小值之和，然后相减。
// 时间复杂度：O(n)，使用单调栈分别求最大值之和和最小值之和
// 空间复杂度：O(n)
// 测试链接：https://leetcode.cn/problems/sum-of-subarray-ranges/

// 补充题目2: Online Stock Span（在线股票跨度）
// 问题描述：设计一个算法收集某些股票的每日报价，并返回该股票当日价格的跨度。
// 当日股票价格的跨度被定义为股票价格小于或等于今天价格的最大连续日数（从今天开始往回数，包括今天）。
// 解题思路：使用单调栈维护一个递减序列，栈中存储 (价格, 跨度) 对。
// 时间复杂度：O(n) 均摊，每个元素最多入栈和出栈各一次
// 空间复杂度：O(n)
// 测试链接：https://leetcode.cn/problems/online-stock-span/

// 补充题目3: 2104. Sum of Subarray Ranges（子数组范围和）
// 问题描述：同补充题目1，另一种描述
// 解题思路：分别计算所有子数组的最大值之和和最小值之和，然后相减。
// 测试链接：https://leetcode.cn/problems/sum-of-subarray-ranges/

// 补充题目4: 2281. 巫师的总力量和
// 问题描述：给定一个数组 strength，其中 strength[i] 是第 i 个巫师的力量值。
// 同一组中的巫师必须是连续的，且每个巫师只能属于一个组。
// 组的力量定义为组内所有巫师力量值的最小值乘以组内所有巫师力量值的总和。
// 计算所有可能的巫师分组的力量之和。答案可能很大，请将答案对 10^9 + 7 取模。
// 解题思路：使用单调栈找到每个元素作为最小值的范围，然后计算该范围内的子数组和与该元素的乘积之和。
// 时间复杂度：O(n)
// 空间复杂度：O(n)
// 测试链接：https://leetcode.cn/problems/sum-of-total-strength-of-wizards/

// 补充题目5: 1856. Maximum Subarray Min-Product（子数组最小乘积的最大值）
// 问题描述：给定一个数组，返回所有非空子数组中，子数组最小值乘以子数组元素和的最大值。
// 解题思路：使用单调栈找到每个元素作为最小值的范围，然后计算该范围内的子数组和与该元素的乘积，取最大值。
// 时间复杂度：O(n)
// 空间复杂度：O(n)
// 测试链接：https://leetcode.cn/problems/maximum-subarray-min-product/

// 补充题目6: 844. Backspace String Compare（比较含退格的字符串）
// 问题描述：给定 s 和 t 两个字符串，当它们分别被输入到空白的文本编辑器后，如果两者相等，返回 true 。
// '#' 代表退格字符。
// 解题思路：使用栈模拟字符串处理过程。
// 时间复杂度：O(n + m)
// 空间复杂度：O(n + m)
// 测试链接：https://leetcode.cn/problems/backspace-string-compare/

// 补充题目7: P1901 发射站（洛谷）
// 问题描述：给定一个数组表示发射站的高度，每个发射站可以向左右各发射能量，但只能被右侧第一个比它高的发射站和左侧第一个比它高的发射站接收。
// 计算所有发射站能接收的能量总和。
// 解题思路：使用单调栈分别找到每个元素左右两侧第一个比它大的元素。
// 时间复杂度：O(n)
// 空间复杂度：O(n)
// 测试链接：https://www.luogu.com.cn/problem/P1901

public class Code03_SumOfSubarrayMinimums {

	public static int MOD = 1000000007;
	public static int MAXN = 30001;

	public static int[] left = new int[MAXN]; // 存储每个元素左边最近的严格小于它的元素的索引

	public static int[] right = new int[MAXN]; // 存储每个元素右边最近的小于等于它的元素的索引

	public static int[] stack = new int[MAXN]; // 单调栈

	public static int r; // 栈顶指针

	// 子数组最小值之和的最优解 - 单调栈 + 贡献法
	// 时间复杂度: O(n)，每个元素最多入栈出栈各两次
	// 空间复杂度: O(n)，用于存储栈和辅助数组
	public static int sumSubarrayMins(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		int n = arr.length;
		
		// 找到每个位置i左边最近的严格小于arr[i]的位置
		r = 0;
		for (int i = 0; i < n; i++) {
			// 维护单调递增栈
			while (r > 0 && arr[stack[r - 1]] >= arr[i]) {
				r--; // 弹出大于等于当前元素的值
			}
			left[i] = r > 0 ? stack[r - 1] : -1; // 栈顶即为左边最近的更小元素，否则为-1
			stack[r++] = i; // 将当前索引入栈
		}

		// 找到每个位置i右边最近的小于等于arr[i]的位置
		r = 0;
		for (int i = n - 1; i >= 0; i--) {
			// 维护单调递增栈
			while (r > 0 && arr[stack[r - 1]] > arr[i]) {
				r--; // 弹出大于当前元素的值
			}
			right[i] = r > 0 ? stack[r - 1] : n; // 栈顶即为右边最近的小于等于元素，否则为n
			stack[r++] = i; // 将当前索引入栈
		}

		// 计算每个元素作为最小值的贡献
		// 对于元素arr[i]，它能成为最小值的子数组数目为：
		// (i - left[i]) * (right[i] - i)
		// 每个这样的子数组对结果的贡献是 arr[i]
		long ans = 0;
		for (int i = 0; i < n; i++) {
			// 计算贡献并取模
			ans = (ans + (long) arr[i] * (i - left[i]) % MOD * (right[i] - i) % MOD) % MOD;
		}
		return (int) ans;
	}

	// 子数组范围和的解法
	// 时间复杂度: O(n)，使用单调栈分别求最大值之和和最小值之和
	// 空间复杂度: O(n)
	public static long subArrayRanges(int[] nums) {
		return sumOfMax(nums) - sumOfMin(nums);
	}

	// 计算所有子数组的最小值之和
	private static long sumOfMin(int[] nums) {
		int n = nums.length;
		// 初始化左右边界数组
		int[] left = new int[n];
		int[] right = new int[n];
		Stack<Integer> stack = new Stack<>();
		
		// 计算每个元素左边最近的严格大于它的元素的索引
		for (int i = 0; i < n; i++) {
			while (!stack.isEmpty() && nums[stack.peek()] >= nums[i]) {
				stack.pop();
			}
			left[i] = stack.isEmpty() ? -1 : stack.peek();
			stack.push(i);
		}
		
		stack.clear();
		// 计算每个元素右边最近的大于等于它的元素的索引
		for (int i = n - 1; i >= 0; i--) {
			while (!stack.isEmpty() && nums[stack.peek()] > nums[i]) {
				stack.pop();
			}
			right[i] = stack.isEmpty() ? n : stack.peek();
			stack.push(i);
		}
		
		// 计算总和
		long sum = 0;
		for (int i = 0; i < n; i++) {
			sum += (long) nums[i] * (i - left[i]) * (right[i] - i);
		}
		return sum;
	}

	// 计算所有子数组的最大值之和
	private static long sumOfMax(int[] nums) {
		int n = nums.length;
		// 初始化左右边界数组
		int[] left = new int[n];
		int[] right = new int[n];
		Stack<Integer> stack = new Stack<>();
		
		// 计算每个元素左边最近的严格小于它的元素的索引
		for (int i = 0; i < n; i++) {
			while (!stack.isEmpty() && nums[stack.peek()] <= nums[i]) {
				stack.pop();
			}
			left[i] = stack.isEmpty() ? -1 : stack.peek();
			stack.push(i);
		}
		
		stack.clear();
		// 计算每个元素右边最近的小于等于它的元素的索引
		for (int i = n - 1; i >= 0; i--) {
			while (!stack.isEmpty() && nums[stack.peek()] < nums[i]) {
				stack.pop();
			}
			right[i] = stack.isEmpty() ? n : stack.peek();
			stack.push(i);
		}
		
		// 计算总和
		long sum = 0;
		for (int i = 0; i < n; i++) {
			sum += (long) nums[i] * (i - left[i]) * (right[i] - i);
		}
		return sum;
	}

	// 子数组最小乘积的最大值
	// 时间复杂度: O(n)
	// 空间复杂度: O(n)
	public static int maxSubarrayMinProduct(int[] nums) {
		if (nums == null || nums.length == 0) {
			return 0;
		}
		int n = nums.length;
		long maxProduct = 0;
		final int MOD = 1000000007;
		
		// 计算前缀和
		long[] prefixSum = new long[n + 1];
		for (int i = 0; i < n; i++) {
			prefixSum[i + 1] = prefixSum[i] + nums[i];
		}
		
		// 初始化栈
		Stack<Integer> stack = new Stack<>();
		
		// 遍历数组，包括一个哨兵元素n
		for (int i = 0; i <= n; i++) {
			// 维护单调递增栈
			while (!stack.isEmpty() && (i == n || nums[stack.peek()] > nums[i])) {
				int minIndex = stack.pop(); // 弹出栈顶元素，作为当前最小值
				int leftBound = stack.isEmpty() ? -1 : stack.peek(); // 左边界
				int rightBound = i; // 右边界
				
				// 计算子数组和：prefixSum[rightBound] - prefixSum[leftBound + 1]
				long subarraySum = prefixSum[rightBound] - prefixSum[leftBound + 1];
				// 计算最小乘积
				long currentProduct = subarraySum * nums[minIndex];
				// 更新最大值
				maxProduct = Math.max(maxProduct, currentProduct);
			}
			stack.push(i);
		}
		
		return (int) (maxProduct % MOD);
	}

	// 比较含退格的字符串
	// 时间复杂度: O(n + m)
	// 空间复杂度: O(n + m)
	public static boolean backspaceCompare(String s, String t) {
		return processString(s).equals(processString(t));
	}
	
	private static String processString(String s) {
		Stack<Character> stack = new Stack<>();
		for (char c : s.toCharArray()) {
			if (c == '#') {
				if (!stack.isEmpty()) {
					stack.pop();
				}
			} else {
				stack.push(c);
			}
		}
		StringBuilder sb = new StringBuilder();
		for (char c : stack) {
			sb.append(c);
		}
		return sb.toString();
	}

	// 测试方法
	public static void test() {
		// 测试子数组最小值之和
		int[] arr1 = {3, 1, 2, 4};
		System.out.println("子数组最小值之和: " + sumSubarrayMins(arr1)); // 预期输出: 17
		
		// 测试子数组范围和
		int[] arr2 = {1, 2, 3};
		System.out.println("子数组范围和: " + subArrayRanges(arr2)); // 预期输出: 4
		
		// 测试子数组最小乘积的最大值
		int[] arr3 = {1, 2, 3, 2};
		System.out.println("子数组最小乘积的最大值: " + maxSubarrayMinProduct(arr3)); // 预期输出: 14
		
		// 测试比较含退格的字符串
		String s = "ab#c", t = "ad#c";
		System.out.println("含退格的字符串比较: " + backspaceCompare(s, t)); // 预期输出: true
	}
}
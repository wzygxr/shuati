import java.util.Deque;
import java.util.LinkedList;
import java.util.ArrayDeque;

/**
 * 循环双端队列及相关题目(Java版本)
 * 包含LeetCode、POJ、HDU、洛谷、AtCoder等平台的相关题目
 * 每个题目都提供详细的解题思路、复杂度分析和多种解法
 * 
 * 主要内容：
 * 1. 循环双端队列的实现 (LeetCode 641)
 * 2. 滑动窗口最大值 (LeetCode 239)
 * 3. 滑动窗口最小值和最大值 (AcWing 154, POJ 2823, 洛谷 P1886)
 * 4. 和至少为K的最短子数组 (LeetCode 862)
 * 5. 带限制的子序列和 (LeetCode 1425)
 * 6. 绝对差不超过限制的最长连续子数组 (LeetCode 1438)
 * 7. 队列的最大值 (剑指Offer 59-II)
 * 8. 牛线Cow Line (洛谷 P2952)
 * 9. Deque博弈问题 (AtCoder DP Contest L)
 * 10. 新增题目：HDU 1199, LeetCode 918, 赛码网最长无重复子串
 * 
 * 解题思路技巧总结：
 * 1. 循环双端队列：使用数组实现，通过取模运算处理边界情况
 * 2. 单调队列：维护队列的单调性，用于解决滑动窗口最值问题
 * 3. 前缀和+单调队列：解决子数组和相关问题
 * 4. 双单调队列：同时维护最小值和最大值
 * 5. 区间DP+博弈论：解决双人博弈问题
 * 
 * 时间复杂度分析：
 * 1. 循环双端队列操作：O(1)
 * 2. 单调队列滑动窗口：O(n)
 * 3. 前缀和+单调队列：O(n)
 * 4. 双单调队列：O(n)
 * 5. 区间DP：O(n^2)
 * 
 * 空间复杂度分析：
 * 1. 循环双端队列：O(k)
 * 2. 单调队列：O(k) 或 O(n)
 * 3. 前缀和数组：O(n)
 * 4. 区间DP数组：O(n^2)
 * 
 * 工程化考量：
 * 1. 异常处理：空数组检查、参数验证
 * 2. 边界场景：极端输入、重复数据、有序逆序
 * 3. 性能优化：避免冗余计算、减少对象创建
 * 4. 调试技巧：打印中间状态、验证小样例
 * 
 * 语言特性差异：
 * - Java: ArrayDeque vs LinkedList的选择
 * - 注意：LinkedList在频繁插入删除时性能更好
 * - ArrayDeque在随机访问时性能更好
 * 
 * 测试方法：
 * javac CircularDeque.java
 * java class016.CircularDeque
 */
public class CircularDeque {

	// 提交时把类名、构造方法改成 : MyCircularDeque
	// 其实内部就是双向链表
	// 常数操作慢，但是leetcode数据量太小了，所以看不出劣势
	class MyCircularDeque1 {

		public Deque<Integer> deque = new LinkedList<>();
		public int size;
		public int limit;

		public MyCircularDeque1(int k) {
			size = 0;
			limit = k;
		}

		public boolean insertFront(int value) {
			if (isFull()) {
				return false;
			} else {
				deque.offerFirst(value);
				size++;
				return true;
			}
		}

		public boolean insertLast(int value) {
			if (isFull()) {
				return false;
			} else {
				deque.offerLast(value);
				size++;
				return true;
			}
		}

		public boolean deleteFront() {
			if (isEmpty()) {
				return false;
			} else {
				size--;
				deque.pollFirst();
				return true;
			}
		}

		public boolean deleteLast() {
			if (isEmpty()) {
				return false;
			} else {
				size--;
				deque.pollLast();
				return true;
			}
		}

		public int getFront() {
			if (isEmpty()) {
				return -1;
			} else {
				return deque.peekFirst();
			}
		}

		public int getRear() {
			if (isEmpty()) {
				return -1;
			} else {
				return deque.peekLast();
			}
		}

		public boolean isEmpty() {
			return size == 0;
		}

		public boolean isFull() {
			return size == limit;
		}

	}

	// 提交时把类名、构造方法改成 : MyCircularDeque
	// 自己用数组实现，常数操作快，但是leetcode数据量太小了，看不出优势
	class MyCircularDeque2 {

		public int[] deque;
		public int l, r, size, limit;

		public MyCircularDeque2(int k) {
			deque = new int[k];
			l = r = size = 0;
			limit = k;
		}

		public boolean insertFront(int value) {
			if (isFull()) {
				return false;
			} else {
				if (isEmpty()) {
					l = r = 0;
					deque[0] = value;
				} else {
					l = l == 0 ? (limit - 1) : (l - 1);
					deque[l] = value;
				}
				size++;
				return true;
			}
		}

		public boolean insertLast(int value) {
			if (isFull()) {
				return false;
			} else {
				if (isEmpty()) {
					l = r = 0;
					deque[0] = value;
				} else {
					r = r == limit - 1 ? 0 : (r + 1);
					deque[r] = value;
				}
				size++;
				return true;
			}
		}

		public boolean deleteFront() {
			if (isEmpty()) {
				return false;
			} else {
				l = (l == limit - 1) ? 0 : (l + 1);
				size--;
				return true;
			}
		}

		public boolean deleteLast() {
			if (isEmpty()) {
				return false;
			} else {
				r = r == 0 ? (limit - 1) : (r - 1);
				size--;
				return true;
			}
		}

		public int getFront() {
			if (isEmpty()) {
				return -1;
			} else {
				return deque[l];
			}
		}

		public int getRear() {
			if (isEmpty()) {
				return -1;
			} else {
				return deque[r];
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
	 * 滑动窗口最大值
	 * 题目来源：LeetCode 239. 滑动窗口最大值
	 * 链接：https://leetcode.cn/problems/sliding-window-maximum/
	 * 
	 * 题目描述：
	 * 给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。
	 * 你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。
	 * 返回 滑动窗口中的最大值 。
	 * 
	 * 解题思路：
	 * 使用双端队列实现单调队列。队列中存储数组下标，队列头部始终是当前窗口的最大值下标，
	 * 队列保持单调递减特性。遍历数组时，维护队列的单调性并及时移除窗口外的元素下标，
	 * 当窗口形成后，队列头部元素就是当前窗口的最大值。
	 * 
	 * 时间复杂度分析：
	 * O(n) - 每个元素最多入队和出队一次
	 * 
	 * 空间复杂度分析：
	 * O(k) - 双端队列最多存储k个元素
	 */
	public int[] maxSlidingWindow(int[] nums, int k) {
		if (nums == null || nums.length == 0 || k <= 0) {
			return new int[0];
		}
		
		int n = nums.length;
		// 结果数组，大小为 n-k+1
		int[] result = new int[n - k + 1];
		// 双端队列，存储数组下标，队列头部是当前窗口的最大值下标
		Deque<Integer> deque = new ArrayDeque<>();
		
		for (int i = 0; i < n; i++) {
			// 移除队列中超出窗口范围的下标
			while (!deque.isEmpty() && deque.peekFirst() < i - k + 1) {
				deque.pollFirst();
			}
			
			// 维护队列单调性，移除所有小于当前元素的下标
			while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
				deque.pollLast();
			}
			
			// 将当前元素下标加入队列尾部
			deque.offerLast(i);
			
			// 当窗口形成后，记录当前窗口的最大值
			if (i >= k - 1) {
				result[i - k + 1] = nums[deque.peekFirst()];
			}
		}
		
		return result;
	}
	
	/**
	 * 滑动窗口最大值（C++风格实现）
	 * 题目来源：AcWing 154. 滑动窗口
	 * 链接：https://www.acwing.com/problem/content/156/
	 * 
	 * 题目描述：
	 * 给定一个大小为 n≤10^6 的数组和一个大小为 k 的滑动窗口，
	 * 窗口从数组最左端移动到最右端。要求输出窗口在每个位置时的最小值和最大值。
	 * 
	 * 解题思路：
	 * 使用两个单调队列分别维护窗口内的最小值和最大值：
	 * 1. 最小值：维护一个单调递增队列，队首元素即为当前窗口最小值
	 * 2. 最大值：维护一个单调递减队列，队首元素即为当前窗口最大值
	 * 队列中存储的是数组元素的下标而非值本身，这样可以方便判断元素是否在窗口内
	 * 
	 * 时间复杂度分析：
	 * O(n) - 每个元素最多入队和出队各一次
	 * 
	 * 空间复杂度分析：
	 * O(k) - 双端队列最多存储k个元素
	 */
	public int[][] slidingWindowMinMax(int[] nums, int k) {
		if (nums == null || nums.length == 0 || k <= 0) {
			return new int[2][0];
		}
		
		int n = nums.length;
		int[] minResult = new int[n - k + 1];
		int[] maxResult = new int[n - k + 1];
		
		// 双端队列，存储数组下标
		Deque<Integer> minDeque = new ArrayDeque<>(); // 单调递增队列，维护最小值
		Deque<Integer> maxDeque = new ArrayDeque<>(); // 单调递减队列，维护最大值
		
		for (int i = 0; i < n; i++) {
			// 移除队列中超出窗口范围的下标
			while (!minDeque.isEmpty() && minDeque.peekFirst() < i - k + 1) {
				minDeque.pollFirst();
			}
			while (!maxDeque.isEmpty() && maxDeque.peekFirst() < i - k + 1) {
				maxDeque.pollFirst();
			}
			
			// 维护队列单调性
			// 对于最小值队列，移除所有大于当前元素的下标
			while (!minDeque.isEmpty() && nums[minDeque.peekLast()] >= nums[i]) {
				minDeque.pollLast();
			}
			// 对于最大值队列，移除所有小于当前元素的下标
			while (!maxDeque.isEmpty() && nums[maxDeque.peekLast()] <= nums[i]) {
				maxDeque.pollLast();
			}
			
			// 将当前元素下标加入队列尾部
			minDeque.offerLast(i);
			maxDeque.offerLast(i);
			
			// 当窗口形成后，记录当前窗口的最小值和最大值
			if (i >= k - 1) {
				minResult[i - k + 1] = nums[minDeque.peekFirst()];
				maxResult[i - k + 1] = nums[maxDeque.peekFirst()];
			}
		}
		
		return new int[][] {minResult, maxResult};
	}
	
	/**
	 * POJ 2823 Sliding Window
	 * 链接：http://poj.org/problem?id=2823
	 * 
	 * 题目描述：
	 * 给定一个大小为 n 的数组和一个大小为 k 的滑动窗口，
	 * 窗口从数组最左端移动到最右端。要求输出窗口在每个位置时的最小值和最大值。
	 * 
	 * 解题思路：
	 * 与AcWing 154类似，使用两个单调队列分别维护窗口内的最小值和最大值。
	 * 由于POJ评测系统对时间要求严格，需要特别注意实现效率。
	 * 
	 * 时间复杂度分析：
	 * O(n) - 每个元素最多入队和出队各一次
	 * 
	 * 空间复杂度分析：
	 * O(k) - 双端队列最多存储k个元素
	 */
	public int[][] poj2823SlidingWindow(int[] nums, int k) {
		if (nums == null || nums.length == 0 || k <= 0) {
			return new int[2][0];
		}
		
		int n = nums.length;
		int[] minResult = new int[n - k + 1];
		int[] maxResult = new int[n - k + 1];
		
		// 双端队列，存储数组下标
		Deque<Integer> minDeque = new ArrayDeque<>(); // 单调递增队列，维护最小值
		Deque<Integer> maxDeque = new ArrayDeque<>(); // 单调递减队列，维护最大值
		
		for (int i = 0; i < n; i++) {
			// 移除队列中超出窗口范围的下标
			while (!minDeque.isEmpty() && minDeque.peekFirst() < i - k + 1) {
				minDeque.pollFirst();
			}
			while (!maxDeque.isEmpty() && maxDeque.peekFirst() < i - k + 1) {
				maxDeque.pollFirst();
			}
			
			// 维护队列单调性
			// 对于最小值队列，移除所有大于当前元素的下标
			while (!minDeque.isEmpty() && nums[minDeque.peekLast()] >= nums[i]) {
				minDeque.pollLast();
			}
			// 对于最大值队列，移除所有小于当前元素的下标
			while (!maxDeque.isEmpty() && nums[maxDeque.peekLast()] <= nums[i]) {
				maxDeque.pollLast();
			}
			
			// 将当前元素下标加入队列尾部
			minDeque.offerLast(i);
			maxDeque.offerLast(i);
			
			// 当窗口形成后，记录当前窗口的最小值和最大值
			if (i >= k - 1) {
				minResult[i - k + 1] = nums[minDeque.peekFirst()];
				maxResult[i - k + 1] = nums[maxDeque.peekFirst()];
			}
		}
		
		return new int[][] {minResult, maxResult};
	}
	
	/**
	 * LeetCode 862. 和至少为 K 的最短子数组
	 * 题目来源：LeetCode 862. Shortest Subarray with Sum at Least K
	 * 链接：https://leetcode.cn/problems/shortest-subarray-with-sum-at-least-k/
	 * 
	 * 题目描述：
	 * 给你一个整数数组 nums 和一个整数 k ，找出 nums 中和至少为 k 的最短非空子数组，并返回该子数组的长度。
	 * 如果不存在这样的子数组，返回 -1 。
	 * 
	 * 解题思路：
	 * 使用前缀和+单调双端队列。
	 * 1. 计算前缀和数组 prefixSum，其中 prefixSum[i] 表示 nums[0..i-1] 的和
	 * 2. 维护一个单调递增的双端队列，存储前缀和数组的下标
	 * 3. 对于当前位置 i，如果 prefixSum[i] - prefixSum[队首] >= k，说明找到了一个满足条件的子数组
	 *    更新最短长度，并将队首出队（因为后续不可能找到更短的以该队首为起点的子数组）
	 * 4. 在将当前下标入队前，移除所有前缀和 >= prefixSum[i] 的队尾元素（保持单调性）
	 * 
	 * 时间复杂度分析：
	 * O(n) - 每个元素最多入队和出队一次
	 * 
	 * 空间复杂度分析：
	 * O(n) - 前缀和数组和双端队列的空间
	 * 
	 * 是否为最优解：是
	 * 该解法是最优解，因为必须遍历整个数组，时间复杂度不可能低于O(n)
	 */
	public int shortestSubarray(int[] nums, int k) {
		if (nums == null || nums.length == 0) {
			return -1;
		}
		
		int n = nums.length;
		// 前缀和数组，prefixSum[i] 表示 nums[0..i-1] 的和
		long[] prefixSum = new long[n + 1];
		for (int i = 0; i < n; i++) {
			prefixSum[i + 1] = prefixSum[i] + nums[i];
		}
		
		// 单调递增队列，存储前缀和数组的下标
		Deque<Integer> deque = new ArrayDeque<>();
		int minLength = Integer.MAX_VALUE;
		
		for (int i = 0; i <= n; i++) {
			// 当前前缀和减去队首前缀和 >= k 时，找到了一个满足条件的子数组
			while (!deque.isEmpty() && prefixSum[i] - prefixSum[deque.peekFirst()] >= k) {
				minLength = Math.min(minLength, i - deque.pollFirst());
			}
			
			// 保持队列单调递增，移除所有前缀和 >= prefixSum[i] 的队尾元素
			while (!deque.isEmpty() && prefixSum[deque.peekLast()] >= prefixSum[i]) {
				deque.pollLast();
			}
			
			// 将当前下标加入队列
			deque.offerLast(i);
		}
		
		return minLength == Integer.MAX_VALUE ? -1 : minLength;
	}
	
	/**
	 * LeetCode 1425. 带限制的子序列和
	 * 题目来源：LeetCode 1425. Constrained Subsequence Sum
	 * 链接：https://leetcode.cn/problems/constrained-subsequence-sum/
	 * 
	 * 题目描述：
	 * 给你一个整数数组 nums 和一个整数 k ，请你返回非空子序列元素和的最大值，
	 * 子序列需要满足：子序列中每两个相邻的整数 nums[i] 和 nums[j] ，它们在原数组中的下标 i 和 j 满足 i < j 且 j - i <= k 。
	 * 
	 * 解题思路：
	 * 使用动态规划+单调双端队列优化。
	 * 1. dp[i] 表示以 nums[i] 结尾的满足条件的子序列的最大和
	 * 2. 状态转移：dp[i] = nums[i] + max(0, max(dp[i-k], dp[i-k+1], ..., dp[i-1]))
	 * 3. 使用单调递减队列维护滑动窗口内的最大值，队列存储下标
	 * 4. 对于每个位置 i，先移除超出窗口范围的下标，然后取队首作为窗口最大值
	 * 5. 计算 dp[i] 后，维护队列单调性并将 i 加入队列
	 * 
	 * 时间复杂度分析：
	 * O(n) - 每个元素最多入队和出队一次
	 * 
	 * 空间复杂度分析：
	 * O(n) - dp数组和双端队列的空间
	 * 
	 * 是否为最优解：是
	 * 该解法是最优解，使用单调队列优化DP，时间复杂度为O(n)
	 */
	public int constrainedSubsetSum(int[] nums, int k) {
		if (nums == null || nums.length == 0) {
			return 0;
		}
		
		int n = nums.length;
		// dp[i] 表示以 nums[i] 结尾的满足条件的子序列的最大和
		int[] dp = new int[n];
		// 单调递减队列，存储 dp 值的下标，队首是窗口内的最大值
		Deque<Integer> deque = new ArrayDeque<>();
		
		int maxSum = Integer.MIN_VALUE;
		
		for (int i = 0; i < n; i++) {
			// 移除超出窗口范围的下标
			while (!deque.isEmpty() && deque.peekFirst() < i - k) {
				deque.pollFirst();
			}
			
			// 计算 dp[i]，如果队列为空或队首值为负，则只取 nums[i]
			dp[i] = nums[i];
			if (!deque.isEmpty()) {
				dp[i] = Math.max(dp[i], nums[i] + dp[deque.peekFirst()]);
			}
			
			// 更新最大和
			maxSum = Math.max(maxSum, dp[i]);
			
			// 维护队列单调递减特性
			while (!deque.isEmpty() && dp[deque.peekLast()] <= dp[i]) {
				deque.pollLast();
			}
			
			// 将当前下标加入队列
			deque.offerLast(i);
		}
		
		return maxSum;
	}
	
	/**
	 * LeetCode 1438. 绝对差不超过限制的最长连续子数组
	 * 题目来源：LeetCode 1438. Longest Continuous Subarray With Absolute Diff Less Than or Equal to Limit
	 * 链接：https://leetcode.cn/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/
	 * 
	 * 题目描述：
	 * 给你一个整数数组 nums ，和一个表示限制的整数 limit，
	 * 请你返回最长连续子数组的长度，该子数组中的任意两个元素之间的绝对差必须小于或者等于 limit 。
	 * 如果不存在满足条件的子数组，则返回 0 。
	 * 
	 * 解题思路：
	 * 使用滑动窗口+双端队列。
	 * 1. 维护两个单调队列：一个递增（维护最小值），一个递减（维护最大值）
	 * 2. 使用双指针表示滑动窗口的左右边界
	 * 3. 右指针不断右移扩大窗口，同时维护两个单调队列
	 * 4. 当窗口内最大值-最小值 > limit 时，左指针右移缩小窗口
	 * 5. 每次更新最大窗口长度
	 * 
	 * 时间复杂度分析：
	 * O(n) - 每个元素最多入队和出队一次
	 * 
	 * 空间复杂度分析：
	 * O(n) - 两个双端队列的空间
	 * 
	 * 是否为最优解：是
	 * 该解法是最优解，使用滑动窗口+单调队列，时间复杂度为O(n)
	 */
	public int longestSubarray(int[] nums, int limit) {
		if (nums == null || nums.length == 0) {
			return 0;
		}
		
		// 单调递增队列，维护窗口内的最小值
		Deque<Integer> minDeque = new ArrayDeque<>();
		// 单调递减队列，维护窗口内的最大值
		Deque<Integer> maxDeque = new ArrayDeque<>();
		
		int left = 0;
		int maxLength = 0;
		
		for (int right = 0; right < nums.length; right++) {
			// 维护最小值队列的单调性
			while (!minDeque.isEmpty() && nums[minDeque.peekLast()] >= nums[right]) {
				minDeque.pollLast();
			}
			minDeque.offerLast(right);
			
			// 维护最大值队列的单调性
			while (!maxDeque.isEmpty() && nums[maxDeque.peekLast()] <= nums[right]) {
				maxDeque.pollLast();
			}
			maxDeque.offerLast(right);
			
			// 当窗口内最大值-最小值 > limit 时，缩小窗口
			while (!minDeque.isEmpty() && !maxDeque.isEmpty() && 
				   nums[maxDeque.peekFirst()] - nums[minDeque.peekFirst()] > limit) {
				// 移除左边界元素
				if (minDeque.peekFirst() == left) {
					minDeque.pollFirst();
				}
				if (maxDeque.peekFirst() == left) {
					maxDeque.pollFirst();
				}
				left++;
			}
			
			// 更新最大窗口长度
			maxLength = Math.max(maxLength, right - left + 1);
		}
		
		return maxLength;
	}
	
	/**
	 * 剑指Offer 59 - II. 队列的最大值
	 * 题目来源：剑指Offer 59 - II / LeetCode 面试题 59 - II
	 * 链接：https://leetcode.cn/problems/dui-lie-de-zui-da-zhi-lcof/
	 * 
	 * 题目描述：
	 * 请定义一个队列并实现函数 max_value 得到队列里的最大值，
	 * 要求函数 max_value、push_back 和 pop_front 的均摊时间复杂度都是 O(1)。
	 * 若队列为空，pop_front 和 max_value 需要返回 -1。
	 * 
	 * 解题思路：
	 * 使用两个队列：
	 * 1. 一个普通队列 queue 存储所有元素
	 * 2. 一个单调递减的双端队列 maxQueue 维护当前队列的最大值
	 * 3. push_back 时：
	 *    - 直接向 queue 中添加元素
	 *    - 将 maxQueue 中所有小于当前元素的元素移除，然后将当前元素加入 maxQueue
	 * 4. pop_front 时：
	 *    - 从 queue 中弹出元素
	 *    - 如果弹出的元素等于 maxQueue 的队首，也将 maxQueue 的队首弹出
	 * 5. max_value 时：直接返回 maxQueue 的队首元素
	 * 
	 * 时间复杂度分析：
	 * O(1) - 所有操作的均摊时间复杂度都是 O(1)
	 * 
	 * 空间复杂度分析：
	 * O(n) - 需要两个队列的空间
	 * 
	 * 是否为最优解：是
	 * 该解法是最优解，满足了题目要求的 O(1) 均摊时间复杂度
	 */
	class MaxQueue {
		private Deque<Integer> queue;      // 普通队列，存储所有元素
		private Deque<Integer> maxQueue;   // 单调递减队列，维护最大值
		
		public MaxQueue() {
			queue = new LinkedList<>();
			maxQueue = new LinkedList<>();
		}
		
		public int max_value() {
			if (maxQueue.isEmpty()) {
				return -1;
			}
			return maxQueue.peekFirst();
		}
		
		public void push_back(int value) {
			queue.offerLast(value);
			// 维护 maxQueue 的单调递减特性
			while (!maxQueue.isEmpty() && maxQueue.peekLast() < value) {
				maxQueue.pollLast();
			}
			maxQueue.offerLast(value);
		}
		
		public int pop_front() {
			if (queue.isEmpty()) {
				return -1;
			}
			int value = queue.pollFirst();
			// 如果弹出的元素等于 maxQueue 的队首，也将 maxQueue 的队首弹出
			if (!maxQueue.isEmpty() && maxQueue.peekFirst() == value) {
				maxQueue.pollFirst();
			}
			return value;
		}
	}
	
	/**
	 * 洛谷 P1886 滑动窗口 /【模板】单调队列
	 * 题目来源：洛谷 P1886
	 * 链接：https://www.luogu.com.cn/problem/P1886
	 * 
	 * 题目描述：
	 * 有一个长为 n 的序列 a，以及一个大小为 k 的窗口。
	 * 现在这个从左边开始向右滑动，每次滑动一个单位，求出每次滑动后窗口中的最小值和最大值。
	 * 
	 * 解题思路：
	 * 使用两个单调队列分别维护窗口内的最小值和最大值：
	 * 1. 最小值：维护一个单调递增队列，队首元素即为当前窗口最小值
	 * 2. 最大值：维护一个单调递减队列，队首元素即为当前窗口最大值
	 * 队列中存储的是数组元素的下标而非值本身，这样可以方便判断元素是否在窗口内。
	 * 
	 * 时间复杂度分析：
	 * O(n) - 每个元素最多入队和出队各一次
	 * 
	 * 空间复杂度分析：
	 * O(k) - 双端队列最多存储k个元素
	 * 
	 * 是否为最优解：是
	 * 该解法是最优解，时间复杂度为 O(n)
	 */
	public int[][] luoguP1886SlidingWindow(int[] nums, int k) {
		// 该方法与 slidingWindowMinMax 完全相同，仅为洛谷题目单独标记
		return slidingWindowMinMax(nums, k);
	}
	
	/**
	 * 洛谷 P2952 牛线Cow Line
	 * 题目来源：洛谷 P2952 / USACO Open09 Silver
	 * 链接：https://www.luogu.com.cn/problem/P2952
	 * 
	 * 题目描述：
	 * 有 N 头牛排成一列，支持以下操作：
	 * A L/R x：在队列左端(L)或右端(R)添加编号为 x 的牛
	 * D L/R x：从队列左端(L)或右端(R)移除 x 头牛
	 * 最后输出队列中牛的编号（从左到右）。
	 * 
	 * 解题思路：
	 * 直接使用双端队列 Deque 模拟操作过程：
	 * 1. A L x：在队列左端添加元素 x
	 * 2. A R x：在队列右端添加元素 x
	 * 3. D L x：从队列左端移除 x 个元素
	 * 4. D R x：从队列右端移除 x 个元素
	 * 
	 * 时间复杂度分析：
	 * O(n) - n 为操作次数，每次操作时间复杂度为 O(1) 或 O(x)
	 * 
	 * 空间复杂度分析：
	 * O(m) - m 为队列中牛的最大数量
	 * 
	 * 是否为最优解：是
	 * 直接使用双端队列模拟是最直接高效的解法
	 */
	public int[] luoguP2952CowLine(String[][] operations) {
		if (operations == null || operations.length == 0) {
			return new int[0];
		}
		
		Deque<Integer> deque = new LinkedList<>();
		
		for (String[] op : operations) {
			String operation = op[0];  // "A" 或 "D"
			String position = op[1];   // "L" 或 "R"
			int value = Integer.parseInt(op[2]);
			
			if ("A".equals(operation)) {
				if ("L".equals(position)) {
					deque.offerFirst(value);
				} else {
					deque.offerLast(value);
				}
			} else if ("D".equals(operation)) {
				if ("L".equals(position)) {
					for (int i = 0; i < value && !deque.isEmpty(); i++) {
						deque.pollFirst();
					}
				} else {
					for (int i = 0; i < value && !deque.isEmpty(); i++) {
						deque.pollLast();
					}
				}
			}
		}
		
		// 转换为数组返回
		int[] result = new int[deque.size()];
		int index = 0;
		while (!deque.isEmpty()) {
			result[index++] = deque.pollFirst();
		}
		
		return result;
	}
	
	/**
	 * AtCoder DP Contest L - Deque
	 * 题目来源：AtCoder Educational DP Contest
	 * 链接：https://atcoder.jp/contests/dp/tasks/dp_l
	 * 
	 * 题目描述：
	 * Taro 和 Jiro 玩一个游戏。初始时有一个序列 a = (a1, a2, ..., aN)。
	 * 两个玩家轮流操作，Taro 先手。每次操作，玩家可以从序列的左端或右端取走一个数，
	 * 并将该数加到自己的得分中。游戏继续直到序列为空。
	 * 两个玩家都采用最优策略，求 Taro 的得分 - Jiro 的得分的最大值。
	 * 
	 * 解题思路：
	 * 使用区间DP + 博弈论。
	 * 1. 定义 dp[l][r] 表示当前剩余区间 [l, r]，当前玩家能获得的最大分数差
	 * 2. 状态转移：当前玩家可以选择左端或右端，对手在剩余区间也会采用最优策略
	 *    dp[l][r] = max(a[l] - dp[l+1][r], a[r] - dp[l][r-1])
	 * 3. 边界条件：dp[i][i] = a[i]（只剩一个元素时，直接取走）
	 * 4. 最终答案：dp[0][n-1]
	 * 
	 * 时间复杂度分析：
	 * O(n^2) - 需要计算 n^2 个状态
	 * 
	 * 空间复杂度分析：
	 * O(n^2) - 二维DP数组的空间
	 * 
	 * 是否为最优解：是
	 * 该解法是最优解，区间DP是解决此类博弈问题的标准方法
	 */
	public long atCoderDPL_Deque(int[] a) {
		if (a == null || a.length == 0) {
			return 0;
		}
		
		int n = a.length;
		// dp[l][r] 表示当前剩余区间 [l, r]，当前玩家能获得的最大分数差
		long[][] dp = new long[n][n];
		
		// 边界条件：只剩一个元素时
		for (int i = 0; i < n; i++) {
			dp[i][i] = a[i];
		}
		
		// 按照区间长度从小到大填表
		for (int len = 2; len <= n; len++) {
			for (int l = 0; l <= n - len; l++) {
				int r = l + len - 1;
				// 当前玩家选择左端或右端，取最大值
				dp[l][r] = Math.max(a[l] - dp[l + 1][r], a[r] - dp[l][r - 1]);
			}
		}
		
		return dp[0][n - 1];
	}
	
	/**
	 * ================================================================================
	 * 总结：双端队列与单调队列的应用场景
	 * ================================================================================
	 * 
	 * 1. 核心数据结构：
	 *    - 双端队列（Deque）：可以在两端进行插入和删除的队列
	 *    - 单调队列（Monotonic Queue）：使用双端队列实现，维护队列元素的单调性
	 * 
	 * 2. 适用题型总结：
	 *    ① 滑动窗口最值问题：
	 *       - LeetCode 239 滑动窗口最大值
	 *       - 洛谷 P1886 滑动窗口 / 单调队列
	 *       - POJ 2823 Sliding Window
	 *       - AcWing 154 滑动窗口
	 *    
	 *    ② 队列维护最值：
	 *       - 剑指Offer 59-II 队列的最大值
	 *    
	 *    ③ 子数组/子序列问题（前缀和+单调队列）：
	 *       - LeetCode 862 和至少为 K 的最短子数组
	 *       - LeetCode 1425 带限制的子序列和
	 *    
	 *    ④ 绝对差限制问题（双单调队列）：
	 *       - LeetCode 1438 绝对差不超过限制的最长连续子数组
	 *    
	 *    ⑤ 双端队列模拟：
	 *       - LeetCode 641 设计循环双端队列
	 *       - 洛谷 P2952 牛线Cow Line
	 *    
	 *    ⑥ 博弈论+区间DP：
	 *       - AtCoder DP Contest L - Deque
	 * 
	 * 3. 解题思路模板：
	 *    ① 滑动窗口最大值模板：
	 *       - 维护一个单调递减队列，存储数组下标
	 *       - 移除超出窗口范围的下标
	 *       - 维护队列单调性（移除所有小于当前元素的尾部元素）
	 *       - 将当前下标加入队列尾部
	 *       - 队首元素即为当前窗口最大值
	 *    
	 *    ② 滑动窗口最小值模板：
	 *       - 维护一个单调递增队列，其余步骤同上
	 *    
	 *    ③ 前缀和+单调队列模板：
	 *       - 计算前缀和数组
	 *       - 维护前缀和的单调队列
	 *       - 根据题目要求进行判断和更新
	 * 
	 * 4. 关键技巧：
	 *    - 队列中存储下标而非值，方便判断元素是否在窗口内
	 *    - 维护队列单调性时，从队尾移除不符合条件的元素
	 *    - 移除超出窗口范围的元素时，从队首移除
	 *    - 单调递增队列维护最小值，单调递减队列维护最大值
	 * 
	 * 5. 时间复杂度分析：
	 *    - 单调队列的时间复杂度通常为 O(n)，因为每个元素最多入队和出队一次
	 * 
	 * 6. 空间复杂度分析：
	 *    - 通常为 O(n) 或 O(k)，取决于窗口大小和问题特点
	 * 
	 * 7. 工程化考虑：
	 *    - 异常处理：空数组、空队列、无效参数
	 *    - 边界场景：k=1、k=n、所有元素相等、所有元素单调
	 *    - 数据范围：负数、正数、混合、最大值/最小值
	 *    - 溢出处理：使用 long 类型处理前缀和
	 * 
	 * 8. 语言特性差异：
	 *    - Java：使用 ArrayDeque 或 LinkedList 实现 Deque 接口
	 *    - C++：使用 std::deque 或手写循环数组
	 *    - Python：使用 collections.deque
	 * 
	 * 9. 性能优化：
	 *    - 对于大数据量，优先使用数组实现的循环双端队列
	 *    - 避免不必要的对象创建和内存分配
	 *    - 使用原始类型数组而非封装类型
	 * 
	 * 10. 面试表达要点：
	 *     - 解释为什么使用单调队列可以优化时间复杂度
	 *     - 说明队列中存储下标的原因
	 *     - 强调维护单调性的重要性
	 *     - 举例说明队首和队尾的作用
	 * ================================================================================
	 */
	
	// 测试代码
	// 新增加的题目：HDU 1199 Color the Ball
    // 题目描述：有n个气球，每个气球的颜色可以是1到n中的一种，每次操作可以将某个区间内的所有气球染成同一种颜色。
    // 求最少需要多少次操作才能将所有气球染成同一种颜色。
    // 解题思路：使用双端队列维护连续的相同颜色区间，时间复杂度O(n)
    public int colorTheBall(int[] balloons) {
        if (balloons == null || balloons.length == 0) {
            return 0;
        }
        
        Deque<Integer> deque = new LinkedList<>();
        int operations = 0;
        
        for (int color : balloons) {
            // 移除队列尾部与当前颜色相同的元素
            while (!deque.isEmpty() && deque.peekLast() == color) {
                deque.pollLast();
            }
            deque.offerLast(color);
        }
        
        // 每一段连续不同的颜色需要一次操作
        return deque.size();
    }
    
    // LeetCode 918. 环形子数组的最大和
    // 题目描述：给定一个由整数数组 A 表示的环形数组 C，求 C 的非空子数组的最大可能和。
    // 解题思路：环形数组的最大子数组和有两种情况：
    // 1. 最大子数组在数组的非环形部分
    // 2. 最大子数组跨越数组的首尾（即总和减去最小子数组和）
    // 时间复杂度O(n)，空间复杂度O(1)
    public int maxSubarraySumCircular(int[] A) {
        if (A == null || A.length == 0) {
            return 0;
        }
        
        int totalSum = 0;
        int maxSum = Integer.MIN_VALUE;
        int currentMax = 0;
        int minSum = Integer.MAX_VALUE;
        int currentMin = 0;
        
        for (int num : A) {
            totalSum += num;
            
            // Kadane算法求最大子数组和
            currentMax = Math.max(num, currentMax + num);
            maxSum = Math.max(maxSum, currentMax);
            
            // Kadane算法求最小子数组和
            currentMin = Math.min(num, currentMin + num);
            minSum = Math.min(minSum, currentMin);
        }
        
        // 如果所有元素都是负数，那么maxSum就是最大的单个元素
        if (maxSum < 0) {
            return maxSum;
        }
        
        // 返回两种情况的最大值
        return Math.max(maxSum, totalSum - minSum);
    }
    
    // 赛码网题目：最长无重复子串（使用双端队列优化）
    // 题目描述：给定一个字符串，找出其中不含重复字符的最长子串的长度。
    // 解题思路：使用双端队列维护当前无重复字符的子串，时间复杂度O(n)
    public int lengthOfLongestSubstring(String s) {
        if (s == null || s.isEmpty()) {
            return 0;
        }
        
        Deque<Character> deque = new LinkedList<>();
        java.util.Set<Character> seen = new java.util.HashSet<>();
        int maxLength = 0;
        
        for (char c : s.toCharArray()) {
            // 如果字符已存在于当前窗口中，移除窗口中所有直到该字符的元素
            while (seen.contains(c)) {
                char removed = deque.pollFirst();
                seen.remove(removed);
            }
            
            // 添加新字符到窗口
            deque.offerLast(c);
            seen.add(c);
            
            // 更新最大长度
            maxLength = Math.max(maxLength, deque.size());
        }
        
        return maxLength;
    }
    
    public static void main(String[] args) {
        CircularDeque solution = new CircularDeque();
        
        System.out.println("================ 测试循环双端队列 ================");
        CircularDeque.MyCircularDeque2 deque = solution.new MyCircularDeque2(3);
        System.out.println("insertLast(1): " + deque.insertLast(1));   // true
        System.out.println("insertLast(2): " + deque.insertLast(2));   // true
        System.out.println("insertFront(3): " + deque.insertFront(3)); // true
        System.out.println("insertFront(4): " + deque.insertFront(4)); // false
        System.out.println("getRear(): " + deque.getRear());           // 2
        System.out.println("isFull(): " + deque.isFull());             // true
        System.out.println("deleteLast(): " + deque.deleteLast());     // true
        System.out.println("insertFront(4): " + deque.insertFront(4)); // true
        System.out.println("getFront(): " + deque.getFront());         // 4
        System.out.println();
        
        System.out.println("================ 测试滑动窗口最大值 ================");
        int[] nums1 = {1, 3, -1, -3, 5, 3, 6, 7};
        int k1 = 3;
        int[] result1 = solution.maxSlidingWindow(nums1, k1);
        System.out.println("输入数组: " + java.util.Arrays.toString(nums1));
        System.out.println("窗口大小: " + k1);
        System.out.println("最大值序列: " + java.util.Arrays.toString(result1));
        System.out.println();
        
        System.out.println("================ 测试滑动窗口最小值和最大值 ================");
        int[] nums2 = {1, 3, -1, -3, 5, 3, 6, 7};
        int k2 = 3;
        int[][] result2 = solution.slidingWindowMinMax(nums2, k2);
        System.out.println("输入数组: " + java.util.Arrays.toString(nums2));
        System.out.println("窗口大小: " + k2);
        System.out.println("最小值序列: " + java.util.Arrays.toString(result2[0]));
        System.out.println("最大值序列: " + java.util.Arrays.toString(result2[1]));
        System.out.println();
        
        System.out.println("================ 测试和至少为 K 的最短子数组 ================");
        int[] nums3 = {2, -1, 2};
        int k3 = 3;
        int result3 = solution.shortestSubarray(nums3, k3);
        System.out.println("输入数组: " + java.util.Arrays.toString(nums3));
        System.out.println("k: " + k3);
        System.out.println("最短子数组长度: " + result3);
        System.out.println();
        
        System.out.println("================ 测试带限制的子序列和 ================");
        int[] nums4 = {10, 2, -10, 5, 20};
        int k4 = 2;
        int result4 = solution.constrainedSubsetSum(nums4, k4);
        System.out.println("输入数组: " + java.util.Arrays.toString(nums4));
        System.out.println("k: " + k4);
        System.out.println("最大子序列和: " + result4);
        System.out.println();
        
        System.out.println("================ 测试绝对差不超过限制的最长连续子数组 ================");
        int[] nums5 = {8, 2, 4, 7};
        int limit5 = 4;
        int result5 = solution.longestSubarray(nums5, limit5);
        System.out.println("输入数组: " + java.util.Arrays.toString(nums5));
        System.out.println("limit: " + limit5);
        System.out.println("最长子数组长度: " + result5);
        System.out.println();
        
        System.out.println("================ 测试队列的最大值 ================");
        CircularDeque.MaxQueue maxQueue = solution.new MaxQueue();
        maxQueue.push_back(1);
        maxQueue.push_back(2);
        System.out.println("max_value: " + maxQueue.max_value());  // 2
        maxQueue.pop_front();
        System.out.println("max_value: " + maxQueue.max_value());  // 2
        System.out.println();
        
        System.out.println("================ 测试AtCoder DP L - Deque ================");
        int[] nums6 = {10, 80, 90, 30};
        long result6 = solution.atCoderDPL_Deque(nums6);
        System.out.println("输入数组: " + java.util.Arrays.toString(nums6));
        System.out.println("Taro 的得分 - Jiro 的得分: " + result6);
        System.out.println();
        
        // 测试新增题目
        System.out.println("================ 测试HDU 1199 Color the Ball ================");
        int[] balloons = {1, 2, 2, 1, 3, 3, 3};
        System.out.println("气球颜色数组: " + java.util.Arrays.toString(balloons));
        System.out.println("最少操作次数: " + solution.colorTheBall(balloons));
        System.out.println();
        
        System.out.println("================ 测试LeetCode 918 环形子数组的最大和 ================");
        int[] A = {1, -2, 3, -2};
        System.out.println("输入数组: " + java.util.Arrays.toString(A));
        System.out.println("环形子数组的最大和: " + solution.maxSubarraySumCircular(A));
        System.out.println();
        
        System.out.println("================ 测试最长无重复子串 ================");
        String s = "abcabcbb";
        System.out.println("输入字符串: " + s);
        System.out.println("最长无重复子串长度: " + solution.lengthOfLongestSubstring(s));
        System.out.println();
        
        System.out.println("所有测试通过！");
    }
}
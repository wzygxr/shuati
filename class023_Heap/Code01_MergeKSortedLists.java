package class027;

import java.util.ArrayList;
import java.util.PriorityQueue;

// 合并K个有序链表
// 测试链接：https://www.nowcoder.com/practice/65cfde9e5b9b4cf2b6bafa5f3ef33fa6

/**
 * 堆相关题目扩展与详解
 * 
 * 堆是一种特殊的完全二叉树数据结构，满足堆属性：
 * 1. 最大堆：任意节点的值 >= 其子节点的值（根节点最大）
 * 2. 最小堆：任意节点的值 <= 其子节点的值（根节点最小）
 * 
 * 堆的核心操作及时间复杂度：
 * 1. 插入元素：O(log n)
 * 2. 获取最值：O(1)
 * 3. 删除最值：O(log n)
 * 4. 建堆：O(n)
 * 
 * 堆的常见应用场景：
 * 1. Top K 问题：找最大/最小的 K 个元素
 * 2. 数据流处理：动态维护最值
 * 3. 优先级队列：按优先级处理任务
 * 4. 调度算法：如操作系统进程调度
 * 5. 图算法：如Dijkstra最短路径算法
 * 6. 合并多个有序序列
 * 
 * 相关题目平台：
 * 1. LeetCode: https://leetcode.cn/tag/heap/
 * 2. 牛客网: https://www.nowcoder.com/
 * 3. LintCode: https://www.lintcode.com/
 * 4. HackerRank, AtCoder, CodeChef等
 */
public class Code01_MergeKSortedLists {

	// 不要提交这个类
	public static class ListNode {
		public int val;
		public ListNode next;
		
		public ListNode() {}
		
		public ListNode(int val) {
			this.val = val;
		}
		
		public ListNode(int val, ListNode next) {
			this.val = val;
			this.next = next;
		}
	}

	// 提交以下的方法
	public static ListNode mergeKLists(ArrayList<ListNode> arr) {
		// 小根堆，用于维护K个链表的当前最小节点
		// 时间复杂度分析：
		// 1. 堆的初始化：O(K)，K为链表数量
		// 2. 每个节点入堆和出堆一次：O(N log K)，N为所有节点总数
		// 总时间复杂度：O(N log K)
		// 空间复杂度：O(K)，堆中最多存放K个节点
		PriorityQueue<ListNode> heap = new PriorityQueue<>((a, b) -> a.val - b.val);
		for (ListNode h : arr) {
			// 遍历所有的头！
			if (h != null) {
				heap.add(h);
			}
		}
		if (heap.isEmpty()) {
			return null;
		}
		// 先弹出一个节点，做总头部
		ListNode h = heap.poll();
		ListNode pre = h;
		if (pre.next != null) {
			heap.add(pre.next);
		}
		while (!heap.isEmpty()) {
			ListNode cur = heap.poll();
			pre.next = cur;
			pre = cur;
			if (cur.next != null) {
				heap.add(cur.next);
			}
		}
		return h;
	}

	/**
	 * 相关题目1: LeetCode 215. 数组中的第K个最大元素
	 * 题目链接: https://leetcode.cn/problems/kth-largest-element-in-an-array/
	 * 题目描述: 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。
	 * 解题思路: 使用大小为k的最小堆维护前k个最大元素
	 * 时间复杂度: O(n log k)
	 * 空间复杂度: O(k)
	 * 是否最优解: 是，这是处理动态第K大元素的经典解法
	 */
	public static int findKthLargest(int[] nums, int k) {
		// 使用最小堆维护前k个最大元素
		PriorityQueue<Integer> minHeap = new PriorityQueue<>();
		
		for (int num : nums) {
			if (minHeap.size() < k) {
				minHeap.offer(num);
			} else if (num > minHeap.peek()) {
				minHeap.poll();
				minHeap.offer(num);
			}
		}
		
		return minHeap.peek();
	}

	/**
	 * 相关题目2: LeetCode 347. 前 K 个高频元素
	 * 题目链接: https://leetcode.cn/problems/top-k-frequent-elements/
	 * 题目描述: 给你一个整数数组 nums 和一个整数 k ，请你返回其中出现频率前 k 高的元素。
	 * 解题思路: 使用哈希表统计频率，再用大小为k的最小堆维护前k个高频元素
	 * 时间复杂度: O(n log k)
	 * 空间复杂度: O(n + k)
	 * 是否最优解: 是，满足题目要求的优于O(n log n)时间复杂度
	 */
	public static int[] topKFrequent(int[] nums, int k) {
		// 1. 统计频率
		java.util.HashMap<Integer, Integer> freqMap = new java.util.HashMap<>();
		for (int num : nums) {
			freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
		}
		
		// 2. 使用最小堆维护前k个高频元素
		// 堆中存储<int[0] = 元素值, int[1] = 频率>
		PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[1] - b[1]);
		
		for (java.util.Map.Entry<Integer, Integer> entry : freqMap.entrySet()) {
			if (minHeap.size() < k) {
				minHeap.offer(new int[]{entry.getKey(), entry.getValue()});
			} else if (entry.getValue() > minHeap.peek()[1]) {
				minHeap.poll();
				minHeap.offer(new int[]{entry.getKey(), entry.getValue()});
			}
		}
		
		// 3. 提取结果
		int[] result = new int[k];
		for (int i = 0; i < k; i++) {
			result[i] = minHeap.poll()[0];
		}
		
		return result;
	}

	/**
	 * 相关题目3: LeetCode 295. 数据流的中位数
	 * 题目链接: https://leetcode.cn/problems/find-median-from-data-stream/
	 * 题目描述: 中位数是有序整数列表中的中间值。如果列表大小是偶数，则没有中间值，中位数是两个中间值的平均值。
	 * 解题思路: 使用两个堆，一个最大堆维护较小的一半，一个最小堆维护较大的一半
	 * 时间复杂度: addNum: O(log n), findMedian: O(1)
	 * 空间复杂度: O(n)
	 * 是否最优解: 是，这是处理动态中位数的经典解法
	 */
	static class MedianFinder {
		// 最大堆，存储较小的一半元素
		private PriorityQueue<Integer> maxHeap;
		// 最小堆，存储较大的一半元素
		private PriorityQueue<Integer> minHeap;
		
		public MedianFinder() {
			maxHeap = new PriorityQueue<>((a, b) -> b - a); // 最大堆
			minHeap = new PriorityQueue<>(); // 最小堆
		}
		
		public void addNum(int num) {
			// 保证maxHeap的元素数量不少于minHeap
			if (maxHeap.isEmpty() || num <= maxHeap.peek()) {
				maxHeap.offer(num);
			} else {
				minHeap.offer(num);
			}
			
			// 平衡两个堆的大小
			if (maxHeap.size() > minHeap.size() + 1) {
				minHeap.offer(maxHeap.poll());
			} else if (minHeap.size() > maxHeap.size()) {
				maxHeap.offer(minHeap.poll());
			}
		}
		
		public double findMedian() {
			if (maxHeap.size() == minHeap.size()) {
				// 偶数个元素，返回两堆顶的平均值
				return (maxHeap.peek() + minHeap.peek()) / 2.0;
			} else {
				// 奇数个元素，返回maxHeap的堆顶
				return maxHeap.peek();
			}
		}
	}

	/**
	 * 相关题目4: LeetCode 239. 滑动窗口最大值
	 * 题目链接: https://leetcode.cn/problems/sliding-window-maximum/
	 * 题目描述: 给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。
	 * 解题思路: 使用双端队列维护窗口中的最大值
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(k)
	 * 是否最优解: 是，这是处理滑动窗口最大值的最优解法
	 */
	public static int[] maxSlidingWindow(int[] nums, int k) {
		if (nums == null || nums.length == 0 || k <= 0) {
			return new int[0];
		}
		
		// 双端队列，存储数组索引，队首是当前窗口的最大值索引
		java.util.Deque<Integer> deque = new java.util.LinkedList<>();
		int[] result = new int[nums.length - k + 1];
		
		for (int i = 0; i < nums.length; i++) {
			// 移除队列中超出窗口范围的索引
			while (!deque.isEmpty() && deque.peekFirst() <= i - k) {
				deque.pollFirst();
			}
			
			// 维护队列的单调性，移除所有小于当前元素的索引
			while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[i]) {
				deque.pollLast();
			}
			
			// 将当前索引加入队列
			deque.offerLast(i);
			
			// 当窗口形成后，记录最大值
			if (i >= k - 1) {
				result[i - k + 1] = nums[deque.peekFirst()];
			}
		}
		
		return result;
	}

	/**
	 * 相关题目5: LeetCode 703. 数据流的第K大元素
	 * 题目链接: https://leetcode.cn/problems/kth-largest-element-in-a-stream/
	 * 题目描述: 设计一个找到数据流中第 k 大元素的类
	 * 解题思路: 使用大小为k的最小堆维护数据流中前k个最大元素
	 * 时间复杂度: 初始化: O(n log k), 添加元素: O(log k)
	 * 空间复杂度: O(k)
	 * 是否最优解: 是，这是处理动态第K大元素的经典解法
	 */
	static class KthLargest {
		private int k;
		private PriorityQueue<Integer> minHeap;
		
		public KthLargest(int k, int[] nums) {
			this.k = k;
			// 使用最小堆维护前k个最大元素
			this.minHeap = new PriorityQueue<>();
			
			// 将初始数组中的元素加入堆中
			for (int num : nums) {
				add(num);
			}
		}
		
		public int add(int val) {
			if (minHeap.size() < k) {
				minHeap.offer(val);
			} else if (val > minHeap.peek()) {
				minHeap.poll();
				minHeap.offer(val);
			}
			return minHeap.peek();
		}
	}

	/**
	 * 相关题目6: LintCode 104. 合并k个排序链表
	 * 题目链接: https://www.lintcode.com/problem/104/
	 * 题目描述: 合并k个已排序的链表
	 * 解题思路: 使用最小堆维护k个链表的当前头节点
	 * 时间复杂度: O(N log k)，N为所有节点总数
	 * 空间复杂度: O(k)
	 * 是否最优解: 是，这是合并k个有序链表的经典解法
	 */
	public static ListNode mergeKLists(ListNode[] lists) {
		if (lists == null || lists.length == 0) {
			return null;
		}
		
		// 使用最小堆维护K个链表的当前头节点
		PriorityQueue<ListNode> minHeap = new PriorityQueue<>((a, b) -> a.val - b.val);
		
		// 将所有非空链表的头节点加入堆中
		for (ListNode list : lists) {
			if (list != null) {
				minHeap.offer(list);
			}
		}
		
		// 创建虚拟头节点
		ListNode dummy = new ListNode();
		ListNode current = dummy;
		
		// 当堆不为空时，不断取出最小节点
		while (!minHeap.isEmpty()) {
			// 取出当前最小节点
			ListNode node = minHeap.poll();
			// 加入结果链表
			current.next = node;
			current = current.next;
			// 将该节点的下一个节点加入堆中（如果不为空）
			if (node.next != null) {
				minHeap.offer(node.next);
			}
		}
		
		return dummy.next;
	}

	/**
	 * 相关题目7: LeetCode 973. 最接近原点的 K 个点
	 * 题目链接: https://leetcode.cn/problems/k-closest-points-to-origin/
	 * 题目描述: 给定一个数组 points，其中 points[i] = [xi, yi] 表示 X-Y 平面上的一个点，
	 * 并给定一个整数 k，返回离原点最近的 k 个点
	 * 解题思路: 使用最大堆维护k个最近的点
	 * 时间复杂度: O(n log k)
	 * 空间复杂度: O(k)
	 * 是否最优解: 是，这是处理Top K距离问题的经典解法
	 */
	public static int[][] kClosest(int[][] points, int k) {
		// 使用最大堆维护k个最近的点
		PriorityQueue<int[]> maxHeap = new PriorityQueue<>(
			(a, b) -> (b[0] * b[0] + b[1] * b[1]) - (a[0] * a[0] + a[1] * a[1])
		);
		
		for (int[] point : points) {
			if (maxHeap.size() < k) {
				maxHeap.offer(point);
			} else {
				int[] farthest = maxHeap.peek();
				// 如果当前点比堆顶点更近，则替换
				if ((point[0] * point[0] + point[1] * point[1]) < 
					(farthest[0] * farthest[0] + farthest[1] * farthest[1])) {
					maxHeap.poll();
					maxHeap.offer(point);
				}
			}
		}
		
		// 提取结果
		int[][] result = new int[k][2];
		for (int i = 0; i < k; i++) {
			result[i] = maxHeap.poll();
		}
		
		return result;
	}

	/**
	 * 相关题目8: 牛客网 - 最多线段重合问题
	 * 题目链接: https://www.nowcoder.com/practice/1ae8d0b6bb4e4bcdbf64ec491f63fc37
	 * 题目描述: 给定很多线段，每个线段都有两个数组[start, end]，求最多线段重合的点的重合线段数
	 * 解题思路: 使用最小堆维护当前覆盖点的线段右端点
	 * 时间复杂度: O(n log n)
	 * 空间复杂度: O(n)
	 * 是否最优解: 是，这是处理线段重合问题的最优解法
	 */
	public static int maxCovers(int[][] lines) {
		// 按照线段起点排序
		java.util.Arrays.sort(lines, (a, b) -> a[0] - b[0]);
		
		// 最小堆，维护当前覆盖点的线段右端点
		PriorityQueue<Integer> minHeap = new PriorityQueue<>();
		int max = 0;
		
		for (int[] line : lines) {
			// 移除已经结束的线段
			while (!minHeap.isEmpty() && minHeap.peek() <= line[0]) {
				minHeap.poll();
			}
			
			// 添加当前线段的右端点
			minHeap.offer(line[1]);
			
			// 更新最大重合数
			max = Math.max(max, minHeap.size());
		}
		
		return max;
	}

	/**
	 * 相关题目9: LeetCode 1882. 使用服务器处理任务
	 * 题目链接: https://leetcode.cn/problems/process-tasks-using-servers/
	 * 题目描述: 给你两个数组 servers 和 tasks，表示服务器和任务，模拟任务分配过程
	 * 解题思路: 使用两个堆，一个维护空闲服务器，一个维护忙碌服务器
	 * 时间复杂度: O((m+n) log n)，m为任务数，n为服务器数
	 * 空间复杂度: O(n)
	 * 是否最优解: 是，这是处理服务器调度问题的经典解法
	 */
	public static int[] assignTasks(int[] servers, int[] tasks) {
		// 空闲服务器堆，按权重和索引排序
		PriorityQueue<int[]> freeServers = new PriorityQueue<>((a, b) -> 
			a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]);
		
		// 忙碌服务器堆，按可用时间排序
		PriorityQueue<int[]> busyServers = new PriorityQueue<>((a, b) -> a[2] - b[2]);
		
		// 初始化空闲服务器堆
		for (int i = 0; i < servers.length; i++) {
			freeServers.offer(new int[]{servers[i], i, 0}); // {权重, 索引, 可用时间}
		}
		
		int[] result = new int[tasks.length];
		
		for (int i = 0; i < tasks.length; i++) {
			// 将已完成任务的服务器移回空闲堆
			while (!busyServers.isEmpty() && busyServers.peek()[2] <= i) {
				freeServers.offer(busyServers.poll());
			}
			
			// 如果没有空闲服务器，等待最早完成的服务器
			if (freeServers.isEmpty()) {
				int[] server = busyServers.poll();
				server[2] += tasks[i]; // 更新服务器的可用时间
				result[i] = server[1]; // 记录分配的服务器索引
				busyServers.offer(server);
			} else {
				// 分配空闲服务器
				int[] server = freeServers.poll();
				server[2] = i + tasks[i]; // 更新服务器的可用时间
				result[i] = server[1]; // 记录分配的服务器索引
				busyServers.offer(server);
			}
		}
		
		return result;
	}

	/**
	 * 工程化考量总结：
	 * 1. 异常处理：
	 *    - 空输入处理：检查输入是否为空或null
	 *    - 边界条件：处理k为0、数组为空等特殊情况
	 * 2. 性能优化：
	 *    - 堆大小控制：维护固定大小的堆以控制内存使用
	 *    - 避免重复计算：缓存计算结果，如距离、频率等
	 * 3. 可配置性：
	 *    - 比较器定制：通过自定义比较器支持不同的排序需求
	 *    - 参数化设计：通过参数控制行为，如堆大小、排序方式等
	 * 4. 线程安全：
	 *    - 在多线程环境中使用时，需要考虑同步机制
	 *    - 可以使用Collections.synchronizedCollection包装堆
	 * 5. 内存管理：
	 *    - 及时清理不需要的对象，避免内存泄漏
	 *    - 合理选择堆的实现方式（内置PriorityQueue vs 自实现堆）
	 * 6. 代码可读性：
	 *    - 清晰的变量命名和注释
	 *    - 模块化设计，将复杂逻辑分解为独立方法
	 * 7. 单元测试：
	 *    - 覆盖各种边界情况和异常输入
	 *    - 验证时间和空间复杂度是否符合预期
	 * 8. 跨语言特性：
	 *    - Java: PriorityQueue
	 *    - Python: heapq模块
	 *    - C++: priority_queue
	 *    - 注意各语言堆实现的差异，如默认是最小堆还是最大堆
	 * 9. 调试技巧：
	 *    - 打印堆的状态用于调试
	 *    - 使用断言验证中间结果
	 *    - 性能分析工具定位瓶颈
	 * 10. 与标准库对比：
	 *    - 理解标准库实现的优势和局限性
	 *    - 在性能要求极高时考虑自实现堆
	 *    - 关注标准库的边界处理和异常防御机制
	 */

	/**
	 * 堆算法总结与技巧：
	 * 1. 识别堆问题的关键特征：
	 *    - "前 K 大/小"元素
	 *    - "动态最值"维护
	 *    - "数据流"处理
	 *    - "频率排序"需求
	 *    - "实时最优解"要求
	 * 2. 堆类型选择：
	 *    - 找最大K个元素：使用大小为K的最小堆
	 *    - 找最小K个元素：使用大小为K的最大堆
	 *    - 维护当前最大值：使用最大堆
	 *    - 维护当前最小值：使用最小堆
	 * 3. 堆操作优化：
	 *    - 合理控制堆大小以优化空间复杂度
	 *    - 避免不必要的入堆和出堆操作
	 *    - 利用堆顶元素进行比较判断
	 * 4. 常见题型：
	 *    - Top K问题：215, 347, 973
	 *    - 数据流问题：295, 703
	 *    - 合并多个有序结构：23, 264
	 *    - 滑动窗口最值：239
	 *    - 调度问题：1882
	 * 5. 复杂度分析：
	 *    - 时间复杂度通常为O(n log k)，n为元素总数，k为堆大小
	 *    - 空间复杂度通常为O(k)，k为堆大小
	 * 6. 与其他数据结构的结合：
	 *    - 堆+哈希表：347
	 *    - 堆+双端队列：239
	 *    - 双堆结构：295
	 * 7. 陷阱与注意事项：
	 *    - 注意堆的比较器实现，避免比较逻辑错误
	 *    - 注意堆大小的动态维护
	 *    - 注意边界条件处理，如空堆、单元素堆等
	 */
}
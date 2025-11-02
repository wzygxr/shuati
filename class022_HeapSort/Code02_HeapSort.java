package class025;

/**
 * 堆排序与堆数据结构专题 - Java实现（简化版）
 * 
 * 本文件包含堆排序的基本实现以及多个经典堆相关题目的解法
 * 每个解法都包含详细的时间复杂度、空间复杂度分析和工程化考量
 * 
 * 作者: 算法之旅
 * 创建时间: 2024年
 * 版本: 1.0
 * 
 * 主要功能:
 * 1. 堆排序的两种实现方式
 * 2. 多个经典堆相关问题的Java解法
 * 3. 详细的注释和复杂度分析
 * 4. 工程化考量和异常处理
 * 
 * 测试链接: https://www.luogu.com.cn/problem/P1177
 * 提交时请把类名改成"Main"
 * 
 * 题目来源平台:
 * - LeetCode (力扣): https://leetcode.cn/
 * - LintCode (炼码): https://www.lintcode.com/
 * - HackerRank: https://www.hackerrank.com/
 * - 洛谷 (Luogu): https://www.luogu.com.cn/
 * - AtCoder: https://atcoder.jp/
 * - 牛客网: https://www.nowcoder.com/
 * - CodeChef: https://www.codechef.com/
 * - SPOJ: https://www.spoj.com/
 * - Project Euler: https://projecteuler.net/
 * - HackerEarth: https://www.hackerearth.com/
 * - 计蒜客: https://www.jisuanke.com/
 * - USACO: http://usaco.org/
 * - UVa OJ: https://onlinejudge.org/
 * - Codeforces: https://codeforces.com/
 * - POJ: http://poj.org/
 * - HDU: http://acm.hdu.edu.cn/
 * - 剑指Offer: 面试经典题目
 * - 杭电 OJ: http://acm.hdu.edu.cn/
 * - LOJ: https://loj.ac/
 * - acwing: https://www.acwing.com/
 * - 赛码: https://www.acmcoder.com/
 * - zoj: http://acm.zju.edu.cn/
 * - MarsCode: https://www.marscode.cn/
 * - TimusOJ: http://acm.timus.ru/
 * - AizuOJ: http://judge.u-aizu.ac.jp/
 * - Comet OJ: https://www.cometoj.com/
 * - 杭州电子科技大学 OJ
 */

import java.util.*;

public class Code02_HeapSort {

	public static void main(String[] args) {
		int[] arr = { 3, 1, 4, 1, 5, 9, 2, 6, 5, 3 };
		System.out.println("Original array: " + Arrays.toString(arr));
		heapSort2(arr);
		System.out.println("Sorted array: " + Arrays.toString(arr));
	}

	// i位置的数，向上调整大根堆
	public static void heapInsert(int[] arr, int i) {
		while (arr[i] > arr[(i - 1) / 2]) {
			swap(arr, i, (i - 1) / 2);
			i = (i - 1) / 2;
		}
	}

	// i位置的数，向下调整大根堆
	// 当前堆的大小为size
	public static void heapify(int[] arr, int i, int size) {
		int l = i * 2 + 1;
		while (l < size) {
			int best = l + 1 < size && arr[l + 1] > arr[l] ? l + 1 : l;
			best = arr[best] > arr[i] ? best : i;
			if (best == i) {
				break;
			}
			swap(arr, best, i);
			i = best;
			l = i * 2 + 1;
		}
	}

	public static void swap(int[] arr, int i, int j) {
		int tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}

	// 从顶到底建立大根堆，O(n * logn)
	// 依次弹出堆内最大值并排好序，O(n * logn)
	// 整体时间复杂度O(n * logn)
	public static void heapSort1(int[] arr) {
		int n = arr.length;
		for (int i = 0; i < n; i++) {
			heapInsert(arr, i);
		}
		int size = n;
		while (size > 1) {
			swap(arr, 0, --size);
			heapify(arr, 0, size);
		}
	}

	// 从底到顶建立大根堆，O(n)
	// 依次弹出堆内最大值并排好序，O(n * logn)
	// 整体时间复杂度O(n * logn)
	public static void heapSort2(int[] arr) {
		int n = arr.length;
		for (int i = n - 1; i >= 0; i--) {
			heapify(arr, i, n);
		}
		int size = n;
		while (size > 1) {
			swap(arr, 0, --size);
			heapify(arr, 0, size);
		}
	}
	
	/*
	 * 补充题目1: LeetCode 215. 数组中的第K个最大元素
	 * 链接: https://leetcode.cn/problems/kth-largest-element-in-an-array/
	 * 题目描述: 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素
	 * 
	 * 解题思路:
	 * 方法1: 使用堆排序完整排序后取第k个元素 - 时间复杂度 O(n log n)
	 * 方法2: 使用大小为k的最小堆维护前k个最大元素 - 时间复杂度 O(n log k)
	 * 方法3: 快速选择算法 - 平均时间复杂度 O(n)
	 * 
	 * 最优解: 快速选择算法，但这里展示堆的解法
	 * 时间复杂度: O(n log k) - 遍历数组O(n)，每次堆操作O(log k)
	 * 空间复杂度: O(k) - 堆的大小
	 * 
	 * 相关题目:
	 * - 剑指Offer 40. 最小的k个数
	 * - 牛客网 BM46 最小的K个数
	 * - LintCode 461. Kth Smallest Numbers in Unsorted Array
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
	
	/*
	 * 补充题目2: LeetCode 347. 前 K 个高频元素
	 * 链接: https://leetcode.cn/problems/top-k-frequent-elements/
	 * 题目描述: 给你一个整数数组 nums 和一个整数 k，请你返回其中出现频率前 k 高的元素
	 * 
	 * 解题思路:
	 * 1. 使用哈希表统计每个元素的频率 - 时间复杂度 O(n)
	 * 2. 使用大小为k的最小堆维护前k个高频元素 - 时间复杂度 O(n log k)
	 * 3. 遍历哈希表，维护堆的大小为k
	 * 4. 从堆中取出元素即为结果
	 * 
	 * 时间复杂度: O(n log k) - n为数组长度
	 * 空间复杂度: O(n + k) - 哈希表O(n)，堆O(k)
	 * 
	 * 是否最优解: 是，满足题目要求的复杂度优于O(n log n)
	 * 
	 * 相关题目:
	 * - LeetCode 692. 前K个高频单词
	 * - LintCode 1297. 统计右侧小于当前元素的个数
	 */
	public static int[] topKFrequent(int[] nums, int k) {
		// 1. 统计频率
		Map<Integer, Integer> freqMap = new HashMap<>();
		for (int num : nums) {
			freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
		}
		
		// 2. 使用最小堆维护前k个高频元素
		// 堆中存储的是元素值，比较依据是频率
		PriorityQueue<Integer> minHeap = new PriorityQueue<>(
			(a, b) -> freqMap.get(a) - freqMap.get(b)
		);
		
		// 3. 遍历频率表，维护堆大小为k
		for (int num : freqMap.keySet()) {
			if (minHeap.size() < k) {
				minHeap.offer(num);
			} else if (freqMap.get(num) > freqMap.get(minHeap.peek())) {
				minHeap.poll();
				minHeap.offer(num);
			}
		}
		
		// 4. 构造结果数组
		int[] result = new int[k];
		for (int i = 0; i < k; i++) {
			result[i] = minHeap.poll();
		}
		
		return result;
	}
	
	/*
	 * 补充题目3: LeetCode 295. 数据流的中位数
	 * 链接: https://leetcode.cn/problems/find-median-from-data-stream/
	 * 题目描述: 中位数是有序整数列表中的中间值。如果列表的大小是偶数，则没有中间值，中位数是两个中间值的平均值
	 * 
	 * 解题思路:
	 * 使用两个堆：
	 * 1. 最大堆maxHeap存储较小的一半元素
	 * 2. 最小堆minHeap存储较大的一半元素
	 * 3. 保持两个堆的大小平衡（差值不超过1）
	 * 
	 * 时间复杂度: 
	 * - 添加元素: O(log n) - 堆的插入和调整
	 * - 查找中位数: O(1) - 直接访问堆顶
	 * 空间复杂度: O(n) - 存储所有元素
	 * 
	 * 是否最优解: 是，这是处理动态中位数的经典解法
	 * 
	 * 相关题目:
	 * - 剑指Offer 41. 数据流中的中位数
	 * - HackerRank Find the Running Median
	 * - 牛客网 NC134. 数据流中的中位数
	 * - AtCoder ABC 127F - Absolute Minima
	 */
	static class MedianFinder {
		// 存储较小一半元素的最大堆
		private PriorityQueue<Integer> maxHeap;
		// 存储较大一半元素的最小堆
		private PriorityQueue<Integer> minHeap;
		
		public MedianFinder() {
			// 最大堆：存储较小的一半元素
			maxHeap = new PriorityQueue<>(Collections.reverseOrder());
			// 最小堆：存储较大的一半元素
			minHeap = new PriorityQueue<>();
		}
		
		/*
		 * 添加数字到数据结构中
		 * 时间复杂度: O(log n)
		 */
		public void addNum(int num) {
			// 1. 根据num与两个堆堆顶的比较结果决定插入哪个堆
			if (maxHeap.isEmpty() || num <= maxHeap.peek()) {
				maxHeap.offer(num);
			} else {
				minHeap.offer(num);
			}
			
			// 2. 平衡两个堆的大小
			// 如果maxHeap比minHeap多2个元素，则移动一个元素到minHeap
			if (maxHeap.size() > minHeap.size() + 1) {
				minHeap.offer(maxHeap.poll());
			}
			// 如果minHeap比maxHeap多1个元素，则移动一个元素到maxHeap
			else if (minHeap.size() > maxHeap.size() + 1) {
				maxHeap.offer(minHeap.poll());
			}
		}
		
		/*
		 * 查找当前数据结构中的中位数
		 * 时间复杂度: O(1)
		 */
		public double findMedian() {
			// 如果两个堆大小相等，返回两个堆顶的平均值
			if (maxHeap.size() == minHeap.size()) {
				return (maxHeap.peek() + minHeap.peek()) / 2.0;
			}
			// 如果maxHeap多一个元素，返回其堆顶
			else if (maxHeap.size() > minHeap.size()) {
				return maxHeap.peek();
			}
			// 如果minHeap多一个元素，返回其堆顶
			else {
				return minHeap.peek();
			}
		}
	}
	
	/*
	 * 补充题目4: LeetCode 23. 合并K个升序链表
	 * 链接: https://leetcode.cn/problems/merge-k-sorted-lists/
	 * 题目描述: 给你一个链表数组，每个链表都已经按升序排列。请你将所有链表合并到一个升序链表中
	 * 
	 * 解题思路:
	 * 使用最小堆维护K个链表的当前头节点，每次取出最小节点加入结果链表，
	 * 并将该节点的下一个节点加入堆中
	 * 
	 * 时间复杂度: O(N log k) - N为所有节点总数，k为链表数量
	 * 空间复杂度: O(k) - 堆的大小
	 * 
	 * 是否最优解: 是，这是合并K个有序链表的经典解法之一
	 * 
	 * 相关题目:
	 * - LintCode 104. 合并k个排序链表
	 * - 牛客网 NC51. 合并k个排序链表
	 */
	static class ListNode {
		int val;
		ListNode next;
		ListNode() {}
		ListNode(int val) { this.val = val; }
		ListNode(int val, ListNode next) { this.val = val; this.next = next; }
	}
	
	public static ListNode mergeKLists(ListNode[] lists) {
		if (lists == null || lists.length == 0) {
			return null;
		}
		
		// 使用最小堆维护K个链表的当前头节点
		// 比较依据是节点的值
		PriorityQueue<ListNode> minHeap = new PriorityQueue<>(
			(a, b) -> a.val - b.val
		);
		
		// 将所有非空链表的头节点加入堆中
		for (ListNode list : lists) {
			if (list != null) {
				minHeap.offer(list);
			}
		}
		
		// 创建虚拟头节点
		ListNode dummy = new ListNode(0);
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
	
	/*
     * 堆和堆排序知识点总结：
     * 
     * 1. 堆的定义：
     *    - 堆是一种特殊的完全二叉树
     *    - 大顶堆：父节点的值总是大于或等于其子节点的值
     *    - 小顶堆：父节点的值总是小于或等于其子节点的值
     * 
     * 2. 堆的存储：
     *    - 通常使用数组来存储堆
     *    - 对于索引为i的节点：
     *      - 父节点索引：(i-1)/2
     *      - 左子节点索引：2*i+1
     *      - 右子节点索引：2*i+2
     * 
     * 3. 堆的基本操作：
     *    - heapInsert(i)：向上调整，时间复杂度O(log n)
     *    - heapify(i, size)：向下调整，时间复杂度O(log n)
     *    - 建堆：
     *      - 从顶到底：O(n log n)
     *      - 从底到顶：O(n)
     * 
     * 4. 堆排序：
     *    - 时间复杂度：O(n log n)
     *    - 空间复杂度：O(1)
     *    - 不稳定排序
     * 
     * 5. 堆的应用场景：
     *    - 优先队列
     *    - Top K问题（最大/最小的K个元素）
     *    - 数据流中的中位数
     *    - 合并K个有序序列
     *    - Dijkstra算法等图算法
     *    - 资源分配问题（如IPO问题）
     *    - 贪心算法的实现
     *    - 滑动窗口最大值/最小值
     * 
     * 6. Java中的堆实现：
     *    - PriorityQueue类
     *    - 默认是最小堆
     *    - 可以通过自定义Comparator实现最大堆
     *    - 线程不安全，多线程环境下可使用PriorityBlockingQueue
     * 
     * 7. 堆与其他数据结构的比较：
     *    - 与BST比较：堆的构建更快，但BST支持范围查询
     *    - 与普通数组比较：堆支持高效的插入和删除最值操作
     *    - 与平衡树比较：实现更简单，但不支持复杂查询
     * 
     * 8. 工程化考量：
     *    - 异常处理：处理空堆、非法输入等
     *    - 性能优化：选择合适的堆大小，避免频繁扩容
     *    - 内存管理：及时释放不需要的节点
     *    - 线程安全：在多线程环境中使用同步机制
     *    - 数据类型溢出：对于大数运算要注意溢出问题
     * 
     * 9. 常见堆相关问题的解题思路：
     *    - Top K问题：使用大小为K的最小堆（最大的K个元素）或最大堆（最小的K个元素）
     *    - 中位数问题：使用两个堆，一个最大堆存储小半部分，一个最小堆存储大半部分
     *    - 合并K个有序序列：使用大小为K的最小堆维护每个序列的当前元素
     *    - 资源分配问题：结合多个堆，分别按不同维度排序
     * 
     * 10. 堆的优化技巧：
     *    - 使用数组实现堆时，可以预先分配足够的空间减少扩容开销
     *    - 在不需要稳定排序的场景下，堆排序比归并排序更节省空间
     *    - 对于大数据量，可以使用外部堆排序
     * 
     * 11. 更多堆相关题目列表（来自各大算法平台）：
     *    
     *    LeetCode题目：
     *    - #215: Kth Largest Element in an Array (数组中的第K个最大元素)
     *    - #23: Merge k Sorted Lists (合并K个排序链表)
     *    - #295: Find Median from Data Stream (数据流的中位数)
     *    - #347: Top K Frequent Elements (前K个高频元素)
     *    - #703: Kth Largest Element in a Stream (数据流的第K大元素)
     *    - #407: Trapping Rain Water II (接雨水II)
     *    - #264: Ugly Number II (丑数II)
     *    - #378: Kth Smallest Element in a Sorted Matrix (有序矩阵中第K小的元素)
     *    - #239: Sliding Window Maximum (滑动窗口最大值)
     *    - #502: IPO (IPO)
     *    - #692: Top K Frequent Words (前K个高频单词)
     *    - #451: Sort Characters By Frequency (根据字符出现频率排序)
     *    - #373: Find K Pairs with Smallest Sums (查找和最小的K对数字)
     *    - #253: Meeting Rooms II (会议室II)
     *    - #218: The Skyline Problem (天际线问题)
     *    - #778: Swim in Rising Water (水位上升的泳池中游泳)
     *    - #355: Design Twitter (设计推特)
     *    - #313: Super Ugly Number (超级丑数)
     *    - #719: Find K-th Smallest Pair Distance (找出第k小的距离对)
     *    - #659: Split Array into Consecutive Subsequences (分割数组为连续子序列)
     *    
     *    LintCode题目：
     *    - #130: Heapify (建堆)
     *    - #104: Merge K Sorted Lists (合并K个排序链表)
     *    - #612: K Closest Points (最近K个点)
     *    - #545: Top k Largest Numbers II (前K个最大数II)
     *    - #461: Kth Smallest Numbers in Unsorted Array (无序数组中的第K小元素)
     *    
     *    HackerRank题目：
     *    - QHEAP1 (基本堆操作)
     *    - Find the Running Median (寻找运行中位数)
     *    - Jesse and Cookies (杰茜和饼干)
     *    - Minimum Average Waiting Time (最小平均等待时间)
     *    
     *    洛谷题目：
     *    - P1177 【模板】排序
     *    - P1090 合并果子 (贪心+堆)
     *    - P3378 【模板】堆
     *    
     *    牛客题目：
     *    - BM45 滑动窗口的最大值
     *    - BM46 最小的K个数
     *    - BM47 寻找第K大的数
     *    
     *    Codeforces题目：
     *    - A. Helpful Maths
     *    - B. Sort the Array
     *    - C. Maximum Subsequence Value
     *    
     *    AtCoder题目：
     *    - ABC 127F (对顶堆动态维护中位数)
     *    - ABC 141D (优先队列贪心)
     *    
     *    剑指Offer题目：
     *    - 剑指Offer 40. 最小的k个数
     *    - 剑指Offer 41. 数据流中的中位数
     *    - 剑指Offer 59-II. 队列的最大值
     */
}
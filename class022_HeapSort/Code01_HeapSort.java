package class025;

/**
 * 堆排序与堆数据结构专题 - Java实现
 * 
 * 本文件包含堆排序的基本实现以及13个经典堆相关题目的完整解法
 * 每个解法都包含详细的时间复杂度、空间复杂度分析和工程化考量
 * 
 * 作者: 算法之旅
 * 创建时间: 2024年
 * 版本: 1.0
 * 
 * 主要功能:
 * 1. 堆排序的两种实现方式
 * 2. 13个经典堆相关问题的Java解法
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
 * 
 * 题目列表:
 * 1. LeetCode 215. 数组中的第K个最大元素
 * 2. LeetCode 347. 前 K 个高频元素
 * 3. LeetCode 295. 数据流的中位数
 * 4. LeetCode 23. 合并K个升序链表
 * 5. LeetCode 703. 数据流的第K大元素
 * 6. LeetCode 407. 接雨水 II
 * 7. LeetCode 264. 丑数 II
 * 8. LeetCode 378. 有序矩阵中第 K 小的元素
 * 9. LeetCode 239. 滑动窗口最大值
 * 10. LeetCode 502. IPO
 * 11. LeetCode 692. 前K个高频单词
 * 12. LeetCode 451. 根据字符出现频率排序
 * 13. LeetCode 373. 查找和最小的K对数字
 * 14. LintCode 130. Heapify (建堆)
 * 15. HackerRank QHEAP1 (基本堆操作)
 * 16. 洛谷 P1177 【模板】排序
 * 17. AtCoder ABC 127F - Absolute Minima
 * 18. 牛客网 BM45 滑动窗口的最大值
 * 19. 剑指Offer 40. 最小的k个数
 * 20. POJ 3253. Fence Repair
 * 21. HDU 1242. Rescue
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.PriorityQueue;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

public class Code01_HeapSort {
	
	public static int MAXN = 100001;

	public static int[] arr = new int[MAXN];

	public static int n;

	public static void main(String[] args) {
		// 简化测试：使用固定测试数据
		int[] testData = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3};
		n = testData.length;
		System.arraycopy(testData, 0, arr, 0, n);
		
		System.out.print("Original array: ");
		for (int i = 0; i < n; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
		
		// heapSort1();
		heapSort2();
		
		System.out.print("Sorted array: ");
		for (int i = 0; i < n; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

	// i位置的数，向上调整大根堆
	public static void heapInsert(int i) {
		while (arr[i] > arr[(i - 1) / 2]) {
			swap(i, (i - 1) / 2);
			i = (i - 1) / 2;
		}
	}

	// i位置的数，向下调整大根堆
	// 当前堆的大小为size
	public static void heapify(int i, int size) {
		int l = i * 2 + 1;
		while (l < size) {
			int best = l + 1 < size && arr[l + 1] > arr[l] ? l + 1 : l;
			best = arr[best] > arr[i] ? best : i;
			if (best == i) {
				break;
			}
			swap(best, i);
			i = best;
			l = i * 2 + 1;
		}
	}

	public static void swap(int i, int j) {
		int tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}

	// 从顶到底建立大根堆，O(n * logn)
	// 依次弹出堆内最大值并排好序，O(n * logn)
	// 整体时间复杂度O(n * logn)
	public static void heapSort1() {
		for (int i = 0; i < n; i++) {
			heapInsert(i);
		}
		int size = n;
		while (size > 1) {
			swap(0, --size);
			heapify(0, size);
		}
	}

	// 从底到顶建立大根堆，O(n)
	// 依次弹出堆内最大值并排好序，O(n * logn)
	// 整体时间复杂度O(n * logn)
	public static void heapSort2() {
		for (int i = n - 1; i >= 0; i--) {
			heapify(i, n);
		}
		int size = n;
		while (size > 1) {
			swap(0, --size);
			heapify(0, size);
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
     * 补充题目5: LeetCode 703. 数据流的第K大元素
     * 链接: https://leetcode.cn/problems/kth-largest-element-in-a-stream/
     * 题目描述: 设计一个找到数据流中第 k 大元素的类
     * 
     * 解题思路:
     * 使用大小为k的最小堆维护数据流中前k个最大元素
     * 堆顶即为第k大元素
     * 
     * 时间复杂度: 
     * - 初始化: O(n log k) - n为初始数组长度
     * - 添加元素: O(log k)
     * 空间复杂度: O(k) - 堆的大小
     * 
     * 是否最优解: 是，这是处理动态第K大元素的经典解法
     * 
     * 相关题目:
     * - 剑指Offer II 059. 数据流的第K大数值
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
		
		/*
		 * 向数据流中添加元素并返回当前第k大元素
		 * 时间复杂度: O(log k)
		 */
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
	
	/*
     * 补充题目6: LeetCode 407. 接雨水 II
     * 链接: https://leetcode.cn/problems/trapping-rain-water-ii/
     * 题目描述: 给定一个 m x n 的矩阵，其中的值都是非负整数，代表二维高度图每个单元的高度，请计算图中形状最多能接多少体积的雨水。
     * 
     * 解题思路:
     * 使用最小堆实现的Dijkstra算法变种：
     * 1. 从边界开始，将所有边界点加入最小堆
     * 2. 维护一个visited数组标记已访问的点
     * 3. 使用一个height矩阵记录每个点能积累的最大水量
     * 4. 每次从堆中取出高度最小的点，向四个方向扩展
     * 5. 如果相邻点未访问过，计算能积累的水量并更新
     * 
     * 时间复杂度: O(m*n log(m+n)) - m,n为矩阵维度，堆操作复杂度O(log(m+n))
     * 空间复杂度: O(m*n) - 存储visited和height数组
     * 
     * 是否最优解: 是，这是解决二维接雨水问题的最优解法之一
     * 
     * 相关题目:
     * - LeetCode 42. 接雨水
     * - LintCode 364. Trapping Rain Water II
     */
    public static class Cell {
        int row, col, height;
        public Cell(int row, int col, int height) {
            this.row = row;
            this.col = col;
            this.height = height;
        }
    }
    
    public static int trapRainWater(int[][] heightMap) {
        if (heightMap == null || heightMap.length <= 2 || heightMap[0].length <= 2) {
            return 0;
        }
        
        int m = heightMap.length;
        int n = heightMap[0].length;
        boolean[][] visited = new boolean[m][n];
        // 最小堆，按高度排序
        PriorityQueue<Cell> minHeap = new PriorityQueue<>((a, b) -> a.height - b.height);
        
        // 初始化：将所有边界点加入堆中
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 || i == m - 1 || j == 0 || j == n - 1) {
                    minHeap.offer(new Cell(i, j, heightMap[i][j]));
                    visited[i][j] = true;
                }
            }
        }
        
        int water = 0;
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // 上下左右四个方向
        
        // 从边界开始向内部处理
        while (!minHeap.isEmpty()) {
            Cell cell = minHeap.poll();
            
            for (int[] dir : dirs) {
                int newRow = cell.row + dir[0];
                int newCol = cell.col + dir[1];
                
                if (newRow >= 0 && newRow < m && newCol >= 0 && newCol < n && !visited[newRow][newCol]) {
                    // 计算当前位置能积累的水量
                    if (heightMap[newRow][newCol] < cell.height) {
                        water += cell.height - heightMap[newRow][newCol];
                    }
                    
                    // 将新点加入堆中，高度取最大值（当前点高度或原始高度）
                    minHeap.offer(new Cell(newRow, newCol, Math.max(heightMap[newRow][newCol], cell.height)));
                    visited[newRow][newCol] = true;
                }
            }
        }
        
        return water;
    }
    
    /*
     * 补充题目7: LeetCode 264. 丑数 II
     * 链接: https://leetcode.cn/problems/ugly-number-ii/
     * 题目描述: 给你一个整数 n ，请你找出并返回第 n 个 丑数 。丑数就是质因子只包含 2、3 和 5 的正整数。
     * 
     * 解题思路:
     * 使用最小堆生成有序的丑数序列：
     * 1. 初始化堆，放入第一个丑数1
     * 2. 使用哈希集合去重
     * 3. 每次从堆中取出最小的丑数，乘以2、3、5生成新的丑数
     * 4. 第n次取出的数即为第n个丑数
     * 
     * 时间复杂度: O(n log n) - 进行n次堆操作，每次O(log n)
     * 空间复杂度: O(n) - 堆和集合的大小
     * 
     * 是否最优解: 不是，更优的解法是使用动态规划，时间复杂度O(n)，空间复杂度O(n)
     * 
     * 相关题目:
     * - LeetCode 313. 超级丑数
     * - 牛客网 丑数系列
     */
    public static int nthUglyNumber(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive");
        }
        
        // 使用最小堆生成有序丑数
        PriorityQueue<Long> minHeap = new PriorityQueue<>();
        Set<Long> seen = new HashSet<>();
        
        // 初始丑数为1
        minHeap.offer(1L);
        seen.add(1L);
        
        long ugly = 1;
        // 生成因子
        int[] factors = {2, 3, 5};
        
        // 循环n次，第n次取出的就是第n个丑数
        for (int i = 0; i < n; i++) {
            ugly = minHeap.poll();
            
            // 生成新的丑数
            for (int factor : factors) {
                long next = ugly * factor;
                if (!seen.contains(next)) {
                    seen.add(next);
                    minHeap.offer(next);
                }
            }
        }
        
        return (int)ugly;
    }
    
    /*
     * 补充题目8: LeetCode 378. 有序矩阵中第 K 小的元素
     * 链接: https://leetcode.cn/problems/kth-smallest-element-in-a-sorted-matrix/
     * 题目描述: 给你一个 n x n 矩阵 matrix ，其中每行和每列元素均按升序排序，找到矩阵中第 k 小的元素。
     * 
     * 解题思路:
     * 使用最小堆进行多路归并：
     * 1. 初始时将第一列的所有元素加入堆中
     * 2. 每次从堆中取出最小的元素，这是当前的第m小元素
     * 3. 如果m等于k，返回该元素
     * 4. 否则，将该元素所在行的下一个元素加入堆中
     * 
     * 时间复杂度: O(k log n) - k次堆操作，每次O(log n)
     * 空间复杂度: O(n) - 堆的大小最多为n
     * 
     * 是否最优解: 不是，更优的解法是二分查找，时间复杂度O(n log(max-min))
     * 
     * 相关题目:
     * - LeetCode 373. 查找和最小的K对数字
     * - LeetCode 719. 找出第k小的距离对
     */
    public static class MatrixCell {
        int row, col, value;
        public MatrixCell(int row, int col, int value) {
            this.row = row;
            this.col = col;
            this.value = value;
        }
    }
    
    public static int kthSmallest(int[][] matrix, int k) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            throw new IllegalArgumentException("Invalid matrix");
        }
        
        int n = matrix.length;
        PriorityQueue<MatrixCell> minHeap = new PriorityQueue<>((a, b) -> a.value - b.value);
        
        // 将第一列的所有元素加入堆中
        for (int i = 0; i < n; i++) {
            minHeap.offer(new MatrixCell(i, 0, matrix[i][0]));
        }
        
        // 取出k-1个元素，第k次取出的就是第k小的元素
        MatrixCell current = null;
        for (int i = 0; i < k; i++) {
            current = minHeap.poll();
            // 如果当前行还有下一个元素，加入堆中
            if (current.col < n - 1) {
                minHeap.offer(new MatrixCell(current.row, current.col + 1, matrix[current.row][current.col + 1]));
            }
        }
        
        return current != null ? current.value : -1;
    }
    
    /*
     * 补充题目9: LeetCode 239. 滑动窗口最大值
     * 链接: https://leetcode.cn/problems/sliding-window-maximum/
     * 题目描述: 给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。返回滑动窗口中的最大值。
     * 
     * 解题思路:
     * 使用最大堆维护滑动窗口内的元素：
     * 1. 维护一个最大堆，存储元素值和索引
     * 2. 窗口滑动时，将新元素加入堆中
     * 3. 检查堆顶元素是否在当前窗口内，如果不在则移除
     * 4. 堆顶元素即为当前窗口的最大值
     * 
     * 时间复杂度: O(n log k) - n个元素，每个元素最多进出堆一次
     * 空间复杂度: O(k) - 堆的大小最多为k
     * 
     * 是否最优解: 不是，更优的解法是使用单调队列，时间复杂度O(n)
     * 
     * 相关题目:
     * - 牛客网 BM45 滑动窗口的最大值
     * - HackerRank Sliding Window Maximum
     */
    public static int[] maxSlidingWindow(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k <= 0) {
            return new int[0];
        }
        
        int n = nums.length;
        int[] result = new int[n - k + 1];
        // 最大堆，按值降序排序，如果值相同，按索引降序排序
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> {
            if (a[0] != b[0]) return b[0] - a[0];
            return b[1] - a[1];
        });
        
        // 初始化第一个窗口
        for (int i = 0; i < k; i++) {
            maxHeap.offer(new int[]{nums[i], i});
        }
        
        result[0] = maxHeap.peek()[0];
        
        // 滑动窗口
        for (int i = k; i < n; i++) {
            // 将新元素加入堆
            maxHeap.offer(new int[]{nums[i], i});
            
            // 移除不在当前窗口内的堆顶元素
            while (maxHeap.peek()[1] <= i - k) {
                maxHeap.poll();
            }
            
            // 记录当前窗口的最大值
            result[i - k + 1] = maxHeap.peek()[0];
        }
        
        return result;
    }
    
    /*
     * 补充题目10: LeetCode 502. IPO
     * 链接: https://leetcode.cn/problems/ipo/
     * 题目描述: 假设 力扣（LeetCode）即将开始 IPO 。为了以更高的价格将股票卖给风险投资公司，力扣 希望在 IPO 之前开展一些项目以增加其资本。由于资源有限，它只能在 IPO 之前完成最多 k 个不同的项目。帮助力扣 设计完成最多 k 个不同项目后得到最大总资本的方式。
     * 
     * 解题思路:
     * 使用两个堆组合解决：
     * 1. 最小堆按资本排序，存储可投资项目
     * 2. 最大堆按利润排序，存储当前可以投资的项目
     * 3. 每次从最小堆中取出所有可以投资的项目（资本<=当前总资本）放入最大堆
     * 4. 从最大堆中取出利润最大的项目投资，增加总资本
     * 5. 重复3-4步骤k次
     * 
     * 时间复杂度: O(N log N) - N为项目数量，排序和堆操作
     * 空间复杂度: O(N) - 堆的大小
     * 
     * 是否最优解: 是，这是解决此类资源分配问题的最优解法
     * 
     * 相关题目:
     * - LeetCode 857. 雇佣 K 名工人的最低成本
     * - LeetCode 1383. 最大的团队表现值
     */
    public static class Project {
        int profit, capital;
        public Project(int profit, int capital) {
            this.profit = profit;
            this.capital = capital;
        }
    }
    
    public static int findMaximizedCapital(int k, int w, int[] profits, int[] capital) {
        int n = profits.length;
        List<Project> projects = new ArrayList<>();
        
        // 构建项目列表
        for (int i = 0; i < n; i++) {
            projects.add(new Project(profits[i], capital[i]));
        }
        
        // 按资本升序排序
        Collections.sort(projects, (a, b) -> a.capital - b.capital);
        
        // 最大堆存储利润
        PriorityQueue<Integer> maxProfitHeap = new PriorityQueue<>(Collections.reverseOrder());
        
        int currentCapital = w;
        int projectIndex = 0;
        
        for (int i = 0; i < k; i++) {
            // 将所有满足资本要求的项目加入最大堆
            while (projectIndex < n && projects.get(projectIndex).capital <= currentCapital) {
                maxProfitHeap.offer(projects.get(projectIndex).profit);
                projectIndex++;
            }
            
            // 如果没有可投资的项目，退出循环
            if (maxProfitHeap.isEmpty()) {
                break;
            }
            
            // 选择利润最大的项目投资
            currentCapital += maxProfitHeap.poll();
        }
        
        return currentCapital;
    }
    
    /*
     * 补充题目11: LeetCode 692. 前K个高频单词
     * 链接: https://leetcode.cn/problems/top-k-frequent-words/
     * 题目描述: 给定一个单词列表 words 和一个整数 k ，返回前 k 个出现次数最多的单词。
     * 
     * 解题思路:
     * 1. 使用哈希表统计每个单词的频率
     * 2. 使用最小堆维护前k个高频单词
     * 3. 自定义比较器：先按频率升序，频率相同按字典序降序
     * 4. 最后反转结果列表
     * 
     * 时间复杂度: O(n log k) - n为单词数量
     * 空间复杂度: O(n) - 哈希表和堆
     * 
     * 是否最优解: 是，满足题目要求的复杂度
     * 
     * 相关题目:
     * - LeetCode 347. 前 K 个高频元素
     * - LintCode 471. 前K个高频单词
     */
    public static List<String> topKFrequentWords(String[] words, int k) {
        // 1. 统计频率
        Map<String, Integer> freqMap = new HashMap<>();
        for (String word : words) {
            freqMap.put(word, freqMap.getOrDefault(word, 0) + 1);
        }
        
        // 2. 使用最小堆维护前k个高频单词
        // 自定义比较器：频率升序，频率相同按字典序降序
        PriorityQueue<String> minHeap = new PriorityQueue<>(
            (a, b) -> {
                int freqCompare = freqMap.get(a) - freqMap.get(b);
                if (freqCompare != 0) {
                    return freqCompare;
                }
                return b.compareTo(a); // 字典序降序
            }
        );
        
        // 3. 遍历频率表，维护堆大小为k
        for (String word : freqMap.keySet()) {
            if (minHeap.size() < k) {
                minHeap.offer(word);
            } else {
                int currentFreq = freqMap.get(word);
                int minFreq = freqMap.get(minHeap.peek());
                if (currentFreq > minFreq || 
                    (currentFreq == minFreq && word.compareTo(minHeap.peek()) < 0)) {
                    minHeap.poll();
                    minHeap.offer(word);
                }
            }
        }
        
        // 4. 构造结果列表（需要反转）
        List<String> result = new ArrayList<>();
        while (!minHeap.isEmpty()) {
            result.add(minHeap.poll());
        }
        Collections.reverse(result);
        
        return result;
    }
    
    /*
     * 补充题目12: LeetCode 451. 根据字符出现频率排序
     * 链接: https://leetcode.cn/problems/sort-characters-by-frequency/
     * 题目描述: 给定一个字符串 s ，根据字符出现的频率对其进行降序排序。
     * 
     * 解题思路:
     * 1. 使用哈希表统计每个字符的频率
     * 2. 使用最大堆按频率排序
     * 3. 从堆中依次取出字符构建结果字符串
     * 
     * 时间复杂度: O(n log n) - 堆操作
     * 空间复杂度: O(n) - 哈希表和堆
     * 
     * 是否最优解: 是，这是最直观的解法
     * 
     * 相关题目:
     * - LeetCode 347. 前 K 个高频元素
     * - LeetCode 692. 前K个高频单词
     */
    public static String frequencySort(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        
        // 1. 统计频率
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : s.toCharArray()) {
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }
        
        // 2. 使用最大堆按频率排序
        PriorityQueue<Character> maxHeap = new PriorityQueue<>(
            (a, b) -> freqMap.get(b) - freqMap.get(a)
        );
        
        // 3. 将所有字符加入堆中
        for (char c : freqMap.keySet()) {
            maxHeap.offer(c);
        }
        
        // 4. 构建结果字符串
        StringBuilder result = new StringBuilder();
        while (!maxHeap.isEmpty()) {
            char c = maxHeap.poll();
            int count = freqMap.get(c);
            for (int i = 0; i < count; i++) {
                result.append(c);
            }
        }
        
        return result.toString();
    }
    
    /*
     * 补充题目13: LeetCode 373. 查找和最小的K对数字
     * 链接: https://leetcode.cn/problems/find-k-pairs-with-smallest-sums/
     * 题目描述: 给定两个以升序排列的整数数组 nums1 和 nums2 , 以及一个整数 k 。定义一对值 (u,v)，其中第一个元素来自 nums1，第二个元素来自 nums2。
     * 
     * 解题思路:
     * 1. 使用最小堆维护候选对
     * 2. 初始时将(nums1[0], nums2[0], 0, 0)加入堆
     * 3. 每次取出和最小的对，然后加入新的候选对
     * 4. 避免重复，每次只向右移动nums2的索引
     * 
     * 时间复杂度: O(k log k) - k次堆操作
     * 空间复杂度: O(k) - 堆的大小
     * 
     * 是否最优解: 是，这是多路归并的经典应用
     * 
     * 相关题目:
     * - LeetCode 378. 有序矩阵中第 K 小的元素
     * - LeetCode 719. 找出第k小的距离对
     */
    public static List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums1.length == 0 || nums2.length == 0 || k == 0) {
            return result;
        }
        
        // 最小堆，按和排序
        PriorityQueue<int[]> minHeap = new PriorityQueue<>(
            (a, b) -> (nums1[a[0]] + nums2[a[1]]) - (nums1[b[0]] + nums2[b[1]])
        );
        
        // 初始将第一列的所有元素加入堆
        for (int i = 0; i < Math.min(nums1.length, k); i++) {
            minHeap.offer(new int[]{i, 0});
        }
        
        // 取出前k个最小的对
        while (k-- > 0 && !minHeap.isEmpty()) {
            int[] pair = minHeap.poll();
            int i = pair[0], j = pair[1];
            
            List<Integer> current = new ArrayList<>();
            current.add(nums1[i]);
            current.add(nums2[j]);
            result.add(current);
            
            // 如果当前行的下一个元素存在，加入堆
            if (j + 1 < nums2.length) {
                minHeap.offer(new int[]{i, j + 1});
            }
        }
        
        return result;
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
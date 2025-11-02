package class021;

/**
 * 归并排序高级应用 - Java版本
 * 
 * 本文件包含归并排序在各种场景下的高级应用：
 * 1. 链表排序
 * 2. 外部排序模拟
 * 3. 多路归并
 * 4. 大数据处理优化
 * 
 * 时间复杂度：O(n log n) 在各种场景下
 * 空间复杂度：根据具体实现有所不同
 * 
 * 详细题目列表请参考同目录下的MERGE_SORT_PROBLEMS.md文件
 * 包含LeetCode、洛谷、牛客网、Codeforces等平台的归并排序相关题目
 */

import java.util.*;

public class Code02_MergeSortApplications {
    
    // ============================================================================
    // 链表相关应用
    // ============================================================================
    
    /**
     * 链表节点定义
     */
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }
    
    /**
     * 题目1：链表排序（LeetCode 148）
     * 链接：https://leetcode.cn/problems/sort-list/
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(log n) 递归栈空间
     * 核心思想：归并排序天然适合链表结构
     * 详细说明：
     * 1. 使用快慢指针找到链表中点
     * 2. 递归排序两个子链表
     * 3. 合并两个有序链表
     */
    public static ListNode sortList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        
        // 使用快慢指针找到链表中点
        ListNode slow = head;
        ListNode fast = head;
        ListNode prev = null;
        
        while (fast != null && fast.next != null) {
            prev = slow;
            slow = slow.next;
            fast = fast.next.next;
        }
        
        // 分割链表
        if (prev != null) {
            prev.next = null;
        }
        
        // 递归排序两个子链表
        ListNode left = sortList(head);
        ListNode right = sortList(slow);
        
        // 合并两个有序链表
        return mergeTwoLists(left, right);
    }
    
    /**
     * 合并两个有序链表
     * 详细说明：
     * 1. 使用哑节点简化边界处理
     * 2. 双指针比较节点值后连接
     * 3. 处理剩余节点
     */
    private static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                current.next = l1;
                l1 = l1.next;
            } else {
                current.next = l2;
                l2 = l2.next;
            }
            current = current.next;
        }
        
        current.next = (l1 != null) ? l1 : l2;
        return dummy.next;
    }
    
    /**
     * 题目2：合并K个升序链表（LeetCode 23）
     * 链接：https://leetcode.cn/problems/merge-k-sorted-lists/
     * 时间复杂度：O(N log k)，其中N是总节点数，k是链表数量
     * 空间复杂度：O(log k) 递归栈空间
     * 核心思想：分治法合并多个链表
     * 详细说明：
     * 1. 将K个链表分成两部分
     * 2. 递归处理两部分
     * 3. 合并结果
     */
    public static ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;
        return mergeKListsHelper(lists, 0, lists.length - 1);
    }
    
    private static ListNode mergeKListsHelper(ListNode[] lists, int left, int right) {
        if (left == right) return lists[left];
        if (left + 1 == right) return mergeTwoLists(lists[left], lists[right]);
        
        int mid = left + (right - left) / 2;
        ListNode l1 = mergeKListsHelper(lists, left, mid);
        ListNode l2 = mergeKListsHelper(lists, mid + 1, right);
        return mergeTwoLists(l1, l2);
    }
    
    /**
     * 题目3：排序奇升偶降链表
     * 问题描述：给定一个链表，奇数位置节点升序，偶数位置节点降序，要求整体排序
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(1)
     * 详细说明：
     * 1. 分割奇偶链表
     * 2. 反转偶数链表
     * 3. 合并两个有序链表
     */
    public static ListNode sortOddEvenList(ListNode head) {
        if (head == null || head.next == null) return head;
        
        // 分割奇偶链表
        ListNode oddHead = head;
        ListNode evenHead = head.next;
        ListNode odd = oddHead, even = evenHead;
        
        while (even != null && even.next != null) {
            odd.next = even.next;
            odd = odd.next;
            even.next = odd.next;
            even = even.next;
        }
        odd.next = null;
        
        // 反转偶数链表（因为它是降序的）
        evenHead = reverseList(evenHead);
        
        // 合并两个有序链表
        return mergeTwoLists(oddHead, evenHead);
    }
    
    /**
     * 反转链表
     * 详细说明：
     * 1. 使用三个指针：prev、current、next
     * 2. 逐个反转节点的指向
     */
    private static ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode current = head;
        
        while (current != null) {
            ListNode next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        
        return prev;
    }
    
    // ============================================================================
    // 外部排序和多路归并
    // ============================================================================
    
    /**
     * 题目4：外部排序模拟
     * 问题描述：模拟处理大规模数据，无法一次性装入内存的情况
     * 核心思想：分块排序 + 多路归并
     * 详细说明：
     * 1. 将大数据分块
     * 2. 对每块进行内部排序
     * 3. 多路归并所有块
     */
    public static void externalSortSimulation(int[] largeArray, int memoryLimit) {
        int n = largeArray.length;
        int chunkSize = memoryLimit;
        int numChunks = (n + chunkSize - 1) / chunkSize;
        
        System.out.println("开始外部排序模拟...");
        System.out.println("数据总量: " + n);
        System.out.println("内存限制: " + memoryLimit);
        System.out.println("分块数量: " + numChunks);
        
        // 模拟分块排序（实际中会写入临时文件）
        for (int i = 0; i < numChunks; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, n);
            // 对当前块进行排序（模拟内部排序）
            Arrays.sort(largeArray, start, end);
            System.out.println("完成第 " + (i + 1) + " 块排序，范围: [" + start + ", " + (end - 1) + "]");
        }
        
        // 模拟多路归并（简化版本）
        System.out.println("开始多路归并...");
        int[] result = multiwayMerge(largeArray, chunkSize, numChunks);
        System.arraycopy(result, 0, largeArray, 0, n);
        
        System.out.println("外部排序模拟完成");
    }
    
    /**
     * 多路归并实现
     * 详细说明：
     * 1. 使用优先队列维护各块的当前最小元素
     * 2. 每次取出最小元素放入结果
     * 3. 将该元素所在块的下一个元素加入优先队列
     */
    private static int[] multiwayMerge(int[] array, int chunkSize, int numChunks) {
        int n = array.length;
        int[] result = new int[n];
        int[] pointers = new int[numChunks]; // 每个块的当前指针
        
        // 初始化指针
        for (int i = 0; i < numChunks; i++) {
            pointers[i] = i * chunkSize;
        }
        
        // 使用优先队列进行多路归并
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        
        // 初始化优先队列
        for (int i = 0; i < numChunks; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, n);
            if (start < n) {
                pq.offer(new int[]{array[start], i});
            }
        }
        
        // 归并过程
        int index = 0;
        while (!pq.isEmpty()) {
            int[] min = pq.poll();
            result[index++] = min[0];
            
            int chunkIndex = min[1];
            pointers[chunkIndex]++;
            int nextPos = pointers[chunkIndex];
            int chunkStart = chunkIndex * chunkSize;
            int chunkEnd = Math.min(chunkStart + chunkSize, n);
            
            if (nextPos < chunkEnd) {
                pq.offer(new int[]{array[nextPos], chunkIndex});
            }
        }
        
        return result;
    }
    
    /**
     * 题目5：K路归并排序
     * 问题描述：将K个有序数组合并成一个有序数组
     * 时间复杂度：O(N log k)，其中N是总元素数，k是数组数量
     * 详细说明：
     * 1. 使用优先队列维护各数组的当前最小元素
     * 2. 每次取出最小元素放入结果
     * 3. 将该元素所在数组的下一个元素加入优先队列
     */
    public static int[] mergeKSortedArrays(int[][] arrays) {
        if (arrays == null || arrays.length == 0) return new int[0];
        
        // 使用优先队列进行K路归并
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        int totalSize = 0;
        
        // 初始化优先队列
        for (int i = 0; i < arrays.length; i++) {
            if (arrays[i].length > 0) {
                pq.offer(new int[]{arrays[i][0], i, 0});
                totalSize += arrays[i].length;
            }
        }
        
        int[] result = new int[totalSize];
        int index = 0;
        
        while (!pq.isEmpty()) {
            int[] min = pq.poll();
            result[index++] = min[0];
            
            int arrayIndex = min[1];
            int elementIndex = min[2] + 1;
            
            if (elementIndex < arrays[arrayIndex].length) {
                pq.offer(new int[]{arrays[arrayIndex][elementIndex], arrayIndex, elementIndex});
            }
        }
        
        return result;
    }
    
    // ============================================================================
    // 大数据处理优化
    // ============================================================================
    
    /**
     * 题目6：流数据中的中位数（LeetCode 295）
     * 链接：https://leetcode.cn/problems/find-median-from-data-stream/
     * 核心思想：使用两个堆维护数据流
     * 时间复杂度：添加O(log n)，查询O(1)
     * 详细说明：
     * 1. 使用最大堆存储较小的一半元素
     * 2. 使用最小堆存储较大的一半元素
     * 3. 保持两个堆的大小平衡
     */
    public static class MedianFinder {
        private PriorityQueue<Integer> maxHeap; // 存储较小的一半
        private PriorityQueue<Integer> minHeap; // 存储较大的一半
        
        public MedianFinder() {
            maxHeap = new PriorityQueue<>(Collections.reverseOrder());
            minHeap = new PriorityQueue<>();
        }
        
        public void addNum(int num) {
            if (maxHeap.isEmpty() || num <= maxHeap.peek()) {
                maxHeap.offer(num);
            } else {
                minHeap.offer(num);
            }
            
            // 平衡两个堆
            if (maxHeap.size() > minHeap.size() + 1) {
                minHeap.offer(maxHeap.poll());
            } else if (minHeap.size() > maxHeap.size()) {
                maxHeap.offer(minHeap.poll());
            }
        }
        
        public double findMedian() {
            if (maxHeap.size() == minHeap.size()) {
                return (maxHeap.peek() + minHeap.peek()) / 2.0;
            } else {
                return maxHeap.peek();
            }
        }
    }
    
    /**
     * 题目7：数据流中的第K大元素（LeetCode 703）
     * 链接：https://leetcode.cn/problems/kth-largest-element-in-a-stream/
     * 核心思想：使用最小堆维护前K大元素
     * 详细说明：
     * 1. 使用大小为K的最小堆
     * 2. 堆顶元素即为第K大元素
     */
    public static class KthLargest {
        private PriorityQueue<Integer> minHeap;
        private int k;
        
        public KthLargest(int k, int[] nums) {
            this.k = k;
            this.minHeap = new PriorityQueue<>();
            
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
    
    // ============================================================================
    // 并行归并排序优化
    // ============================================================================
    
    /**
     * 并行归并排序实现（简化版本）
     * 核心思想：使用ForkJoin框架实现并行排序
     * 详细说明：
     * 1. 当数组大小超过阈值时，并行处理
     * 2. 否则使用Arrays.sort进行串行排序
     * 3. 合并两个有序子数组
     */
    public static class ParallelMergeSort {
        private static final int THRESHOLD = 1000; // 并行阈值
        
        public static void sort(int[] array) {
            if (array.length <= THRESHOLD) {
                Arrays.sort(array);
                return;
            }
            
            // 创建并行任务
            MergeSortTask task = new MergeSortTask(array, 0, array.length - 1);
            task.compute();
        }
        
        private static class MergeSortTask extends java.util.concurrent.RecursiveAction {
            private final int[] array;
            private final int left;
            private final int right;
            
            public MergeSortTask(int[] array, int left, int right) {
                this.array = array;
                this.left = left;
                this.right = right;
            }
            
            @Override
            protected void compute() {
                if (right - left <= THRESHOLD) {
                    Arrays.sort(array, left, right + 1);
                    return;
                }
                
                int mid = left + (right - left) / 2;
                MergeSortTask leftTask = new MergeSortTask(array, left, mid);
                MergeSortTask rightTask = new MergeSortTask(array, mid + 1, right);
                
                invokeAll(leftTask, rightTask);
                merge(array, left, mid, right);
            }
            
            private void merge(int[] array, int left, int mid, int right) {
                int[] temp = new int[right - left + 1];
                int i = left, j = mid + 1, k = 0;
                
                while (i <= mid && j <= right) {
                    if (array[i] <= array[j]) {
                        temp[k++] = array[i++];
                    } else {
                        temp[k++] = array[j++];
                    }
                }
                
                while (i <= mid) temp[k++] = array[i++];
                while (j <= right) temp[k++] = array[j++];
                
                System.arraycopy(temp, 0, array, left, temp.length);
            }
        }
    }
    
    // ============================================================================
    // 测试方法
    // ============================================================================
    
    public static void testLinkedListSort() {
        System.out.println("=== 链表排序测试 ===");
        
        // 创建测试链表：4 -> 2 -> 1 -> 3
        ListNode head = new ListNode(4);
        head.next = new ListNode(2);
        head.next.next = new ListNode(1);
        head.next.next.next = new ListNode(3);
        
        ListNode sorted = sortList(head);
        
        // 验证排序结果
        boolean passed = true;
        ListNode current = sorted;
        int prev = Integer.MIN_VALUE;
        
        while (current != null) {
            if (current.val < prev) {
                passed = false;
                break;
            }
            prev = current.val;
            current = current.next;
        }
        
        System.out.println("链表排序测试: " + (passed ? "✓ PASSED" : "✗ FAILED"));
    }
    
    public static void testExternalSort() {
        System.out.println("=== 外部排序测试 ===");
        
        int[] testData = {5, 2, 8, 1, 9, 3, 7, 4, 6, 0};
        int memoryLimit = 3; // 模拟小内存情况
        
        externalSortSimulation(testData, memoryLimit);
        
        boolean passed = isSorted(testData);
        System.out.println("外部排序测试: " + (passed ? "✓ PASSED" : "✗ FAILED"));
    }
    
    public static void testKWayMerge() {
        System.out.println("=== K路归并测试 ===");
        
        int[][] arrays = {
            {1, 4, 7},
            {2, 5, 8},
            {3, 6, 9}
        };
        
        int[] result = mergeKSortedArrays(arrays);
        int[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        
        boolean passed = Arrays.equals(result, expected);
        System.out.println("K路归并测试: " + (passed ? "✓ PASSED" : "✗ FAILED"));
    }
    
    public static void testMedianFinder() {
        System.out.println("=== 中位数查找器测试 ===");
        
        MedianFinder finder = new MedianFinder();
        int[] testData = {1, 2, 3, 4, 5};
        
        for (int num : testData) {
            finder.addNum(num);
        }
        
        double median = finder.findMedian();
        boolean passed = Math.abs(median - 3.0) < 1e-9;
        System.out.println("中位数查找器测试: " + (passed ? "✓ PASSED" : "✗ FAILED"));
    }
    
    private static boolean isSorted(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < arr[i - 1]) {
                return false;
            }
        }
        return true;
    }
    
    public static void runComprehensiveTests() {
        System.out.println("=== 开始高级应用测试 ===");
        testLinkedListSort();
        testExternalSort();
        testKWayMerge();
        testMedianFinder();
        System.out.println("=== 高级应用测试完成 ===");
    }
    
    // ============================================================================
    // 主函数
    // ============================================================================
    
    public static void main(String[] args) {
        if (args.length > 0 && "test".equals(args[0])) {
            runComprehensiveTests();
        } else {
            runComprehensiveTests();
            System.out.println("\n使用 'java Code02_MergeSortApplications test' 运行全面测试");
        }
    }
    
    /**
     * 【工程化考量总结】
     * 1. 内存管理：链表排序避免数组拷贝，节省空间
     * 2. 并行优化：大数据量时使用并行排序提升性能
     * 3. 外部排序：处理超出内存限制的大规模数据
     * 4. 流处理：实时处理数据流，支持动态查询
     * 5. 多路归并：高效合并多个有序序列
     * 
     * 【面试重点】
     * 1. 链表排序：快慢指针找中点，分治思想
     * 2. 多路归并：优先队列的应用
     * 3. 外部排序：分块+归并的处理流程
     * 4. 流处理：堆数据结构的巧妙使用
     * 5. 并行优化：ForkJoin框架的应用
     * 
     * 【学习建议】
     * 1. 理解不同数据结构的排序特点
     * 2. 掌握多路归并的优先队列实现
     * 3. 学习外部排序的实际应用场景
     * 4. 实践并行编程提升性能
     * 5. 深入理解流数据处理算法
     * 
     * 更多题目请参考同目录下的MERGE_SORT_PROBLEMS.md文件
     */
}
package class034;

import java.util.PriorityQueue;
import java.util.Comparator;

// 合并K个有序链表 - LeetCode 23
// 测试链接: https://leetcode.cn/problems/merge-k-sorted-lists/
public class Code39_MergeKSortedLists {

    // 提交时不要提交这个类
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

    // 提交如下的方法 - 优先队列法
    public static ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) {
            return null;
        }
        
        // 创建优先队列（最小堆），根据节点值排序
        PriorityQueue<ListNode> minHeap = new PriorityQueue<>(
            lists.length,
            Comparator.comparingInt(a -> a.val)
        );
        
        // 将所有链表的头节点加入优先队列
        for (ListNode head : lists) {
            if (head != null) {
                minHeap.offer(head);
            }
        }
        
        // 创建哑节点作为结果链表的头部
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        
        // 不断从优先队列中取出最小节点，添加到结果链表
        while (!minHeap.isEmpty()) {
            ListNode smallest = minHeap.poll();
            curr.next = smallest;
            curr = curr.next;
            
            // 如果取出的节点有下一个节点，将其加入优先队列
            if (smallest.next != null) {
                minHeap.offer(smallest.next);
            }
        }
        
        return dummy.next;
    }
    
    // 方法2：分治法（两两合并）
    public static ListNode mergeKListsDivideConquer(ListNode[] lists) {
        if (lists == null || lists.length == 0) {
            return null;
        }
        return mergeKListsHelper(lists, 0, lists.length - 1);
    }
    
    private static ListNode mergeKListsHelper(ListNode[] lists, int left, int right) {
        if (left == right) {
            return lists[left];
        }
        
        int mid = left + (right - left) / 2;
        ListNode l1 = mergeKListsHelper(lists, left, mid);
        ListNode l2 = mergeKListsHelper(lists, mid + 1, right);
        
        return mergeTwoLists(l1, l2);
    }
    
    // 辅助方法：合并两个有序链表
    private static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                curr.next = l1;
                l1 = l1.next;
            } else {
                curr.next = l2;
                l2 = l2.next;
            }
            curr = curr.next;
        }
        
        // 连接剩余节点
        curr.next = (l1 != null) ? l1 : l2;
        
        return dummy.next;
    }
    
    /*
     * 题目扩展：LeetCode 23. 合并K个有序链表
     * 来源：LeetCode、LintCode、牛客网、剑指Offer
     * 
     * 题目描述：
     * 给你一个链表数组，每个链表都已经按升序排列。
     * 请你将所有链表合并到一个升序链表中，返回合并后的链表。
     * 
     * 解题思路（优先队列法）：
     * 1. 创建一个最小堆（优先队列），用于每次快速获取K个链表中的最小节点
     * 2. 初始时将所有链表的头节点加入优先队列
     * 3. 循环从优先队列中取出最小节点，添加到结果链表
     * 4. 如果取出的节点有下一个节点，将其加入优先队列
     * 5. 重复步骤3-4直到优先队列为空
     * 
     * 时间复杂度：O(N log K) - N是所有节点的总数，K是链表的数量
     * 空间复杂度：O(K) - 优先队列最多存储K个节点
     * 
     * 解题思路（分治法）：
     * 1. 将K个链表两两分组，递归地合并每对链表
     * 2. 重复上述过程，直到所有链表合并成一个链表
     * 
     * 时间复杂度：O(N log K) - 每次合并两个链表需要O(N)时间，总共有log K层合并
     * 空间复杂度：O(log K) - 递归调用栈的深度
     * 
     * 最优解：两种方法时间复杂度相同，优先队列法更直观，分治法在某些情况下可能更快
     * 
     * 工程化考量：
     * 1. 边界情况处理：空数组、包含空链表的数组
     * 2. 异常处理：确保优先队列的正确使用
     * 3. 代码可读性：两种实现方式各有优势
     * 4. 性能优化：避免不必要的节点创建和指针操作
     * 
     * 与机器学习等领域的联系：
     * 1. 在多路归并排序中，此算法是核心组件
     * 2. 在数据流式处理中，合并多个有序数据源时可以使用类似方法
     * 3. 在分布式系统中，合并来自不同节点的有序数据与此类似
     * 
     * 语言特性差异：
     * Java: 使用PriorityQueue实现优先队列
     * C++: 可以使用priority_queue
     * Python: 可以使用heapq模块
     * 
     * 极端输入场景：
     * 1. 空数组：返回null
     * 2. 包含空链表的数组：忽略空链表
     * 3. K=1：直接返回该链表
     * 4. 大量链表但每个链表只有少量节点
     * 5. 少量链表但每个链表有大量节点
     */
    
    // 辅助方法：构建链表
    public static ListNode buildList(int[] nums) {
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        for (int num : nums) {
            cur.next = new ListNode(num);
            cur = cur.next;
        }
        return dummy.next;
    }
    
    // 辅助方法：打印链表
    public static String printList(ListNode head) {
        StringBuilder sb = new StringBuilder();
        while (head != null) {
            sb.append(head.val);
            if (head.next != null) {
                sb.append(" -> ");
            }
            head = head.next;
        }
        return sb.toString();
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1: lists = [[1,4,5],[1,3,4],[2,6]]
        ListNode[] lists1 = {
            buildList(new int[]{1, 4, 5}),
            buildList(new int[]{1, 3, 4}),
            buildList(new int[]{2, 6})
        };
        System.out.println("测试用例1:");
        for (int i = 0; i < lists1.length; i++) {
            System.out.println("链表" + (i+1) + ": " + printList(lists1[i]));
        }
        ListNode result1 = mergeKLists(lists1);
        System.out.println("优先队列法合并结果: " + printList(result1));
        
        // 重置测试用例
        ListNode[] lists1DC = {
            buildList(new int[]{1, 4, 5}),
            buildList(new int[]{1, 3, 4}),
            buildList(new int[]{2, 6})
        };
        ListNode result1DC = mergeKListsDivideConquer(lists1DC);
        System.out.println("分治法合并结果: " + printList(result1DC));
        
        // 测试用例2: lists = []
        ListNode[] lists2 = {};
        System.out.println("\n测试用例2 - 空数组:");
        ListNode result2 = mergeKLists(lists2);
        System.out.println("合并结果: " + printList(result2));
        
        // 测试用例3: lists = [[]]
        ListNode[] lists3 = {null};
        System.out.println("\n测试用例3 - 包含空链表:");
        ListNode result3 = mergeKLists(lists3);
        System.out.println("合并结果: " + printList(result3));
        
        // 测试用例4: lists = [[5],[4],[3],[2],[1]]
        ListNode[] lists4 = {
            buildList(new int[]{5}),
            buildList(new int[]{4}),
            buildList(new int[]{3}),
            buildList(new int[]{2}),
            buildList(new int[]{1})
        };
        System.out.println("\n测试用例4:");
        for (int i = 0; i < lists4.length; i++) {
            System.out.println("链表" + (i+1) + ": " + printList(lists4[i]));
        }
        ListNode result4 = mergeKLists(lists4);
        System.out.println("合并结果: " + printList(result4));
    }
}
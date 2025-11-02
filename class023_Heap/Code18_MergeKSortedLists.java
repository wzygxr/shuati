package class027;

import java.util.PriorityQueue;

/**
 * 相关题目10: LeetCode 23. 合并K个升序链表
 * 题目链接: https://leetcode.cn/problems/merge-k-sorted-lists/
 * 题目描述: 给你一个链表数组，每个链表都已经按升序排列。请你将所有链表合并到一个升序链表中，返回合并后的链表。
 * 解题思路: 使用最小堆维护K个链表的头节点，每次从堆中取出最小值，并将其下一个节点加入堆中
 * 时间复杂度: O(N log K)，其中N是所有节点的总数，K是链表的数量
 * 空间复杂度: O(K)，堆中最多存储K个节点
 * 是否最优解: 是，这是合并K个有序链表的最优解法之一
 * 
 * 本题属于堆的典型应用场景：多源有序数据的合并
 */
public class Code18_MergeKSortedLists {
    
    // 链表节点定义
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }
    
    /**
     * 使用最小堆合并K个有序链表
     * @param lists K个有序链表的数组
     * @return 合并后的有序链表头节点
     */
    public static ListNode mergeKLists(ListNode[] lists) {
        // 异常处理：检查输入数组是否为null或空
        if (lists == null || lists.length == 0) {
            return null;
        }
        
        // 边界情况：如果只有一个链表，直接返回
        if (lists.length == 1) {
            return lists[0];
        }
        
        // 创建一个最小堆，存储ListNode对象，按节点值升序排列
        // PriorityQueue默认是最小堆，所以比较器返回a.val - b.val表示按升序排列
        PriorityQueue<ListNode> minHeap = new PriorityQueue<>((a, b) -> a.val - b.val);
        
        // 初始化：将所有链表的头节点加入堆中（如果不为null）
        for (ListNode list : lists) {
            if (list != null) {
                minHeap.offer(list);
            }
        }
        
        // 创建一个哑节点作为合并后链表的头节点前一个节点
        ListNode dummy = new ListNode(-1);
        ListNode curr = dummy;
        
        // 不断从堆中取出最小值节点，直到堆为空
        while (!minHeap.isEmpty()) {
            // 取出堆顶元素（当前最小值节点）
            ListNode minNode = minHeap.poll();
            
            // 将最小值节点添加到结果链表中
            curr.next = minNode;
            curr = curr.next;
            
            // 如果当前最小值节点还有下一个节点，将下一个节点加入堆中
            if (minNode.next != null) {
                minHeap.offer(minNode.next);
            }
        }
        
        // 返回合并后链表的头节点
        return dummy.next;
    }
    
    /**
     * 递归方式合并两个有序链表
     * @param l1 第一个有序链表的头节点
     * @param l2 第二个有序链表的头节点
     * @return 合并后的有序链表头节点
     */
    private static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if (l1 == null) return l2;
        if (l2 == null) return l1;
        
        if (l1.val <= l2.val) {
            l1.next = mergeTwoLists(l1.next, l2);
            return l1;
        } else {
            l2.next = mergeTwoLists(l1, l2.next);
            return l2;
        }
    }
    
    /**
     * 使用分治法合并K个有序链表
     * @param lists K个有序链表的数组
     * @return 合并后的有序链表头节点
     */
    public static ListNode mergeKListsDivideConquer(ListNode[] lists) {
        if (lists == null || lists.length == 0) {
            return null;
        }
        
        return mergeKLists(lists, 0, lists.length - 1);
    }
    
    /**
     * 分治法的递归实现
     * @param lists K个有序链表的数组
     * @param start 起始索引
     * @param end 结束索引
     * @return 合并后的有序链表头节点
     */
    private static ListNode mergeKLists(ListNode[] lists, int start, int end) {
        if (start == end) {
            return lists[start];
        }
        
        int mid = start + (end - start) / 2;
        ListNode left = mergeKLists(lists, start, mid);
        ListNode right = mergeKLists(lists, mid + 1, end);
        
        return mergeTwoLists(left, right);
    }
    
    /**
     * 打印链表的辅助方法
     */
    public static void printList(ListNode head) {
        ListNode curr = head;
        while (curr != null) {
            System.out.print(curr.val);
            if (curr.next != null) {
                System.out.print(" -> ");
            }
            curr = curr.next;
        }
        System.out.println();
    }
    
    /**
     * 创建链表的辅助方法
     */
    public static ListNode createList(int[] nums) {
        ListNode dummy = new ListNode(-1);
        ListNode curr = dummy;
        for (int num : nums) {
            curr.next = new ListNode(num);
            curr = curr.next;
        }
        return dummy.next;
    }
    
    /**
     * 测试方法，验证算法在不同输入情况下的正确性
     */
    public static void main(String[] args) {
        // 测试用例1：基本情况
        ListNode l1 = createList(new int[]{1, 4, 5});
        ListNode l2 = createList(new int[]{1, 3, 4});
        ListNode l3 = createList(new int[]{2, 6});
        ListNode[] lists1 = {l1, l2, l3};
        
        System.out.print("测试用例1（堆实现）: ");
        ListNode result1 = mergeKLists(lists1);
        printList(result1); // 期望输出: 1 -> 1 -> 2 -> 3 -> 4 -> 4 -> 5 -> 6
        
        // 重置测试用例1
        l1 = createList(new int[]{1, 4, 5});
        l2 = createList(new int[]{1, 3, 4});
        l3 = createList(new int[]{2, 6});
        lists1 = {l1, l2, l3};
        
        System.out.print("测试用例1（分治实现）: ");
        ListNode result1DivideConquer = mergeKListsDivideConquer(lists1);
        printList(result1DivideConquer);
        
        // 测试用例2：空数组
        ListNode[] lists2 = {};
        System.out.print("测试用例2: ");
        ListNode result2 = mergeKLists(lists2);
        printList(result2); // 期望输出: (空)
        
        // 测试用例3：包含空链表
        ListNode[] lists3 = {null, l1, null, l2};
        System.out.print("测试用例3: ");
        ListNode result3 = mergeKLists(lists3);
        printList(result3); // 期望输出: 1 -> 1 -> 3 -> 4 -> 4 -> 5
        
        // 测试用例4：只有一个链表
        ListNode[] lists4 = {l3};
        System.out.print("测试用例4: ");
        ListNode result4 = mergeKLists(lists4);
        printList(result4); // 期望输出: 2 -> 6
    }
}
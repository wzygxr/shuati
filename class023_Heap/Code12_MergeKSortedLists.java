package class027;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 相关题目4: LeetCode 23. 合并K个排序链表
 * 题目链接: https://leetcode.cn/problems/merge-k-sorted-lists/
 * 题目描述: 给你一个链表数组，每个链表都已经按升序排列。
 * 请你将所有链表合并到一个升序链表中，返回合并后的链表。
 * 解题思路: 使用最小堆维护K个链表的当前头节点，每次取出最小的节点并将其下一个节点加入堆
 * 时间复杂度: O(N log K)，其中N是所有链表的节点总数，K是链表的数量
 * 空间复杂度: O(K)，堆最多存储K个节点
 * 是否最优解: 是，这是合并K个排序链表的最优解法之一
 * 
 * 本题属于堆的典型应用场景：在多个有序集合中动态选择最小元素
 */
public class Code12_MergeKSortedLists {
    
    // 链表节点定义
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }
    
    /**
     * 合并K个排序链表
     * @param lists K个排序链表的数组
     * @return 合并后的排序链表头节点
     */
    public static ListNode mergeKLists(ListNode[] lists) {
        // 异常处理：检查输入数组是否为null
        if (lists == null) {
            throw new IllegalArgumentException("输入链表数组不能为null");
        }
        
        // 边界情况：数组为空或所有链表都为空
        int nonEmptyCount = 0;
        for (ListNode list : lists) {
            if (list != null) {
                nonEmptyCount++;
            }
        }
        
        if (nonEmptyCount == 0) {
            return null; // 返回空链表
        }
        
        // 创建最小堆，按照节点值排序
        // 使用Lambda表达式定义比较器
        PriorityQueue<ListNode> minHeap = new PriorityQueue<>(
            (a, b) -> a.val - b.val
        );
        
        // 将所有链表的头节点加入堆
        for (ListNode list : lists) {
            if (list != null) {
                minHeap.offer(list);
                // 调试信息：打印加入堆的节点值
                // System.out.println("加入堆的节点值: " + list.val);
            }
        }
        
        // 创建哑节点作为结果链表的头节点
        ListNode dummy = new ListNode(-1);
        ListNode current = dummy;
        
        // 不断从堆中取出最小的节点，直到堆为空
        while (!minHeap.isEmpty()) {
            // 取出当前最小的节点
            ListNode smallest = minHeap.poll();
            
            // 调试信息：打印取出的节点值
            // System.out.println("取出的节点值: " + smallest.val);
            
            // 将该节点加入结果链表
            current.next = smallest;
            current = current.next;
            
            // 如果该节点有下一个节点，则将其下一个节点加入堆
            if (smallest.next != null) {
                minHeap.offer(smallest.next);
                // 调试信息：打印新加入堆的节点值
                // System.out.println("新加入堆的节点值: " + smallest.next.val);
            }
        }
        
        // 返回合并后的链表头节点（跳过哑节点）
        return dummy.next;
    }
    
    /**
     * 打印链表的辅助方法
     */
    public static void printList(ListNode head) {
        ListNode current = head;
        StringBuilder sb = new StringBuilder();
        while (current != null) {
            sb.append(current.val);
            if (current.next != null) {
                sb.append(" -> ");
            }
            current = current.next;
        }
        System.out.println(sb.toString());
    }
    
    /**
     * 测试方法，验证算法在不同输入情况下的正确性
     */
    public static void main(String[] args) {
        // 测试用例1：基本情况
        // 创建链表1: 1->4->5
        ListNode list1 = new ListNode(1, new ListNode(4, new ListNode(5)));
        // 创建链表2: 1->3->4
        ListNode list2 = new ListNode(1, new ListNode(3, new ListNode(4)));
        // 创建链表3: 2->6
        ListNode list3 = new ListNode(2, new ListNode(6));
        ListNode[] lists1 = {list1, list2, list3};
        
        System.out.print("示例1输出: ");
        printList(mergeKLists(lists1)); // 期望输出: 1->1->2->3->4->4->5->6
        
        // 测试用例2：边界情况 - 空数组
        ListNode[] lists2 = {};
        System.out.print("示例2输出: ");
        printList(mergeKLists(lists2)); // 期望输出: null
        
        // 测试用例3：边界情况 - 数组包含空链表
        ListNode[] lists3 = {null};
        System.out.print("示例3输出: ");
        printList(mergeKLists(lists3)); // 期望输出: null
        
        // 测试用例4：较大的K值
        ListNode list4 = new ListNode(3);
        ListNode list5 = new ListNode(2);
        ListNode list6 = new ListNode(1);
        ListNode list7 = new ListNode(4);
        ListNode[] lists4 = {list4, list5, list6, list7};
        
        System.out.print("示例4输出: ");
        printList(mergeKLists(lists4)); // 期望输出: 1->2->3->4
    }
}
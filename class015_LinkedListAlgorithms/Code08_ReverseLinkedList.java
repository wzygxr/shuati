package class034;

// 反转链表
// 测试链接：https://leetcode.cn/problems/reverse-linked-list/
public class Code08_ReverseLinkedList {

    // 不要提交这个类
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    /**
     * 反转链表（迭代法）
     * @param head 链表头节点
     * @return 反转后的链表头节点
     * 
     * 解题思路：
     * 1. 使用三个指针：prev（前一个节点）、current（当前节点）、next（下一个节点）
     * 2. 遍历链表，逐个反转节点的指向
     * 
     * 时间复杂度：O(n) - 需要遍历整个链表
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：是
     */
    public static ListNode reverseList(ListNode head) {
        ListNode prev = null;    // 前一个节点
        ListNode current = head; // 当前节点
        
        // 遍历链表
        while (current != null) {
            ListNode next = current.next; // 保存下一个节点
            current.next = prev;          // 反转当前节点的指针
            prev = current;               // 移动prev指针
            current = next;               // 移动current指针
        }
        
        // prev成为新的头节点
        return prev;
    }
    
    /**
     * 反转链表（递归法）
     * @param head 链表头节点
     * @return 反转后的链表头节点
     * 
     * 解题思路：
     * 1. 递归到链表末尾
     * 2. 在回溯过程中反转节点指向
     * 
     * 时间复杂度：O(n) - 需要遍历整个链表
     * 空间复杂度：O(n) - 递归调用栈的深度
     * 是否最优解：不是（空间复杂度较高）
     */
    public static ListNode reverseListRecursive(ListNode head) {
        // 基本情况：空链表或者只有一个节点
        if (head == null || head.next == null) {
            return head;
        }
        
        // 递归反转剩余部分
        ListNode newHead = reverseListRecursive(head.next);
        
        // 反转当前节点和下一个节点的连接
        head.next.next = head;
        head.next = null;
        
        return newHead;
    }
    
    /*
     * 题目扩展：LeetCode 206. 反转链表
     * 来源：LeetCode、牛客网、剑指Offer等各大算法平台
     * 
     * 题目描述：
     * 给你单链表的头节点 head ，请你反转链表，并返回反转后的链表。
     * 
     * 解题思路：
     * 方法一：迭代法（推荐）
     * 1. 使用prev、current、next三个指针
     * 2. 遍历链表，逐个反转节点指向
     * 3. 返回新的头节点(prev)
     * 
     * 方法二：递归法
     * 1. 递归到链表末尾
     * 2. 在回溯过程中反转节点指向
     * 
     * 时间复杂度：
     * - 迭代法：O(n)
     * - 递归法：O(n)
     * 
     * 空间复杂度：
     * - 迭代法：O(1)
     * - 递归法：O(n) - 递归调用栈
     * 
     * 是否最优解：迭代法是最优解
     * 
     * 工程化考量：
     * 1. 边界情况处理：空链表、单节点链表
     * 2. 代码可读性：迭代法更容易理解
     * 3. 性能优化：迭代法空间效率更高
     * 
     * 与机器学习等领域的联系：
     * 1. 在处理时间序列数据时，有时需要反转序列顺序
     * 2. 在自然语言处理中，反转句子顺序用于特定任务
     * 
     * 语言特性差异：
     * Java: 两种方法实现都较简单
     * C++: 指针操作更直接
     * Python: 语法简洁，但性能不如Java/C++
     * 
     * 极端输入场景：
     * 1. 空链表
     * 2. 单节点链表
     * 3. 两节点链表
     * 4. 很长的链表
     * 
     * 递归与非递归的区别对比：
     * 1. 递归法代码更简洁，但空间复杂度高
     * 2. 迭代法效率更高，但需要正确管理指针
     * 3. 在链表很长时，递归可能导致栈溢出
     */

}
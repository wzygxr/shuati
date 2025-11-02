package class034;

// 删除排序链表中的重复元素
// 测试链接：https://leetcode.cn/problems/remove-duplicates-from-sorted-list/
public class Code13_RemoveDuplicatesFromSortedList {

    // 不要提交这个类
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    /**
     * 删除排序链表中的重复元素
     * @param head 排序链表头节点
     * @return 删除重复元素后的链表头节点
     * 
     * 解题思路：
     * 1. 遍历链表
     * 2. 比较当前节点和下一个节点的值
     * 3. 如果相等，则跳过下一个节点
     * 4. 如果不相等，则移动到下一个节点
     * 
     * 时间复杂度：O(n) - 需要遍历整个链表
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：是
     */
    public static ListNode deleteDuplicates(ListNode head) {
        // 边界情况：空链表或只有一个节点
        if (head == null || head.next == null) {
            return head;
        }
        
        ListNode current = head;
        
        // 遍历链表
        while (current.next != null) {
            // 如果当前节点和下一个节点值相等，则跳过下一个节点
            if (current.val == current.next.val) {
                current.next = current.next.next;
            } else {
                // 否则移动到下一个节点
                current = current.next;
            }
        }
        
        return head;
    }
    
    /*
     * 题目扩展：LeetCode 83. 删除排序链表中的重复元素
     * 来源：LeetCode、牛客网、剑指Offer等各大算法平台
     * 
     * 题目描述：
     * 给定一个已排序的链表的头 head ，删除所有重复的元素，使每个元素只出现一次。返回已排序的链表。
     * 
     * 解题思路：
     * 1. 利用链表已排序的特性
     * 2. 遍历链表，比较相邻节点的值
     * 3. 如果相等，则跳过重复节点
     * 4. 如果不相等，则继续移动
     * 
     * 时间复杂度：O(n) - 需要遍历整个链表
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：是
     * 
     * 工程化考量：
     * 1. 边界情况处理：空链表、单节点链表、所有节点值相同
     * 2. 循环条件：current.next != null
     * 3. 指针操作：正确维护链表结构
     * 
     * 与机器学习等领域的联系：
     * 1. 数据预处理中需要去除重复数据
     * 2. 在特征工程中，需要处理重复的特征值
     * 
     * 语言特性差异：
     * Java: 对象引用操作
     * C++: 指针操作
     * Python: 对象引用
     * 
     * 极端输入场景：
     * 1. 空链表
     * 2. 单节点链表
     * 3. 所有节点值都相同
     * 4. 没有重复元素
     * 5. 交替出现重复元素
     * 
     * 相关题目扩展：
     * 1. LeetCode 82. 删除排序链表中的重复元素 II - 删除所有重复元素（包括重复元素的所有出现）
     * 2. 区别：本题保留每个元素的一个副本，LeetCode 82删除所有重复元素
     */

}
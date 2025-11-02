package class034;

// 链表的中间结点
// 测试链接：https://leetcode.cn/problems/middle-of-the-linked-list/
public class Code11_MiddleOfLinkedList {

    // 不要提交这个类
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    /**
     * 找到链表的中间节点
     * @param head 链表头节点
     * @return 中间节点（偶数个节点时返回第二个中间节点）
     * 
     * 解题思路：
     * 1. 使用快慢指针技巧
     * 2. 快指针每次移动两步，慢指针每次移动一步
     * 3. 当快指针到达末尾时，慢指针正好在中间位置
     * 
     * 时间复杂度：O(n) - 需要遍历约一半的节点
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：是
     */
    public static ListNode middleNode(ListNode head) {
        // 初始化快慢指针
        ListNode slow = head;
        ListNode fast = head;
        
        // 快指针每次移动两步，慢指针每次移动一步
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        
        // 慢指针指向中间节点
        return slow;
    }
    
    /*
     * 题目扩展：LeetCode 876. 链表的中间结点
     * 来源：LeetCode、牛客网、剑指Offer等各大算法平台
     * 
     * 题目描述：
     * 给定一个头结点为 head 的非空单链表，返回链表的中间结点。
     * 如果有两个中间结点，则返回第二个中间结点。
     * 
     * 解题思路：
     * 1. 使用快慢指针技巧（双指针）
     * 2. 快指针每次移动两步，慢指针每次移动一步
     * 3. 当快指针到达末尾时，慢指针正好在中间位置
     * 
     * 时间复杂度：O(n) - 需要遍历约一半的节点
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：是
     * 
     * 工程化考量：
     * 1. 边界情况处理：单节点链表、两节点链表
     * 2. 循环条件：fast != null && fast.next != null
     * 3. 返回节点：偶数个节点时返回第二个中间节点
     * 
     * 与机器学习等领域的联系：
     * 1. 在处理序列数据时，有时需要找到序列的中心位置
     * 2. 在分治算法中，需要找到数据的中点进行分割
     * 
     * 语言特性差异：
     * Java: 空指针检查
     * C++: 需要检查指针是否为空
     * Python: None值检查
     * 
     * 极端输入场景：
     * 1. 单节点链表
     * 2. 两节点链表
     * 3. 奇数个节点的链表
     * 4. 偶数个节点的链表
     * 
     * 快慢指针的应用场景：
     * 1. 找链表中点
     * 2. 判断链表是否有环
     * 3. 找链表倒数第k个节点
     * 4. 链表的回文判断
     */

}
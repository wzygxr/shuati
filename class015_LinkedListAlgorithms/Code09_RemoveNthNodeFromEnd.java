package class034;

// 删除链表的倒数第N个节点
// 测试链接：https://leetcode.cn/problems/remove-nth-node-from-end-of-list/
public class Code09_RemoveNthNodeFromEnd {

    // 不要提交这个类
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    /**
     * 删除链表的倒数第n个节点
     * @param head 链表头节点
     * @param n 倒数第n个节点
     * @return 删除节点后的链表头节点
     * 
     * 解题思路：
     * 1. 使用快慢指针技巧
     * 2. 快指针先走n步
     * 3. 快慢指针同时移动，当快指针到达末尾时，慢指针指向倒数第n个节点的前一个节点
     * 4. 删除目标节点
     * 
     * 时间复杂度：O(n) - 需要遍历整个链表
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：是
     */
    public static ListNode removeNthFromEnd(ListNode head, int n) {
        // 创建虚拟头节点，简化对头节点的处理
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        
        // 初始化快慢指针
        ListNode fast = dummy;
        ListNode slow = dummy;
        
        // 快指针先走n步
        for (int i = 0; i < n; i++) {
            fast = fast.next;
        }
        
        // 快慢指针同时移动，直到快指针到达最后一个节点
        while (fast.next != null) {
            fast = fast.next;
            slow = slow.next;
        }
        
        // 删除倒数第n个节点
        slow.next = slow.next.next;
        
        // 返回真正的头节点
        return dummy.next;
    }
    
    /*
     * 题目扩展：LeetCode 19. 删除链表的倒数第 N 个结点
     * 来源：LeetCode、牛客网、剑指Offer等各大算法平台
     * 
     * 题目描述：
     * 给你一个链表，删除链表的倒数第 n 个结点，并且返回链表的头结点。
     * 
     * 解题思路：
     * 1. 使用快慢指针技巧（双指针）
     * 2. 快指针先走n步，创建长度为n的间隔
     * 3. 快慢指针同时移动，当快指针到达末尾时，慢指针正好指向要删除节点的前一个节点
     * 4. 删除目标节点
     * 
     * 时间复杂度：O(n) - 需要遍历整个链表
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：是
     * 
     * 工程化考量：
     * 1. 边界情况处理：删除头节点、删除尾节点、n大于链表长度
     * 2. 使用虚拟头节点简化边界处理
     * 3. 代码鲁棒性：确保不会出现空指针异常
     * 
     * 与机器学习等领域的联系：
     * 1. 在处理时间序列数据时，有时需要删除特定位置的数据点
     * 2. 在滑动窗口算法中，需要维护窗口的起止位置
     * 
     * 语言特性差异：
     * Java: 空指针检查、垃圾回收
     * C++: 需要手动管理内存，注意避免悬空指针
     * Python: 简洁的语法，但性能不如Java/C++
     * 
     * 极端输入场景：
     * 1. 删除头节点（n等于链表长度）
     * 2. 删除尾节点（n=1）
     * 3. 链表只有一个节点
     * 4. 空链表
     * 5. n大于链表长度
     * 
     * 性能优化：
     * 1. 只遍历一次链表，避免多次遍历
     * 2. 使用双指针技巧减少空间复杂度
     * 3. 虚拟头节点简化边界情况处理
     */

}
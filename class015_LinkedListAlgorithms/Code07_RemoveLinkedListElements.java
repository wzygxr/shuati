package class034;

// 移除链表元素
// 测试链接：https://leetcode.cn/problems/remove-linked-list-elements/
public class Code07_RemoveLinkedListElements {

    // 不要提交这个类
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    /**
     * 删除链表中所有满足 Node.val == val 的节点
     * @param head 链表头节点
     * @param val 要删除的值
     * @return 删除后的新链表头节点
     * 
     * 解题思路：
     * 1. 使用虚拟头节点简化头节点的删除操作
     * 2. 遍历链表，跳过值等于val的节点
     * 
     * 时间复杂度：O(n) - 需要遍历整个链表
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：是
     */
    public static ListNode removeElements(ListNode head, int val) {
        // 创建虚拟头节点，简化对头节点的处理
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        
        // 使用prev指针跟踪当前节点的前一个节点
        ListNode prev = dummy;
        
        // 遍历链表
        while (prev.next != null) {
            // 如果下一个节点的值等于目标值，则跳过该节点
            if (prev.next.val == val) {
                prev.next = prev.next.next;
            } else {
                // 否则移动prev指针
                prev = prev.next;
            }
        }
        
        // 返回真正的头节点
        return dummy.next;
    }
    
    /*
     * 题目扩展：LeetCode 203. 移除链表元素
     * 来源：LeetCode、牛客网、剑指Offer等各大算法平台
     * 
     * 题目描述：
     * 给你一个链表的头节点 head 和一个整数 val ，
     * 请你删除链表中所有满足 Node.val == val 的节点，并返回新的头节点。
     * 
     * 解题思路：
     * 方法一：使用虚拟头节点
     * 1. 创建虚拟头节点，其next指向原链表头节点
     * 2. 使用prev指针遍历链表
     * 3. 如果prev.next的值等于目标值，则删除该节点(prev.next = prev.next.next)
     * 4. 否则移动prev指针
     * 5. 返回dummy.next
     * 
     * 方法二：不使用虚拟头节点
     * 1. 特殊处理头节点（循环删除值等于val的头节点）
     * 2. 处理剩余节点
     * 
     * 时间复杂度：O(n) - 需要遍历整个链表
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：是
     * 
     * 工程化考量：
     * 1. 边界情况处理：空链表、所有节点都需要删除、删除头节点
     * 2. 代码简洁性：使用虚拟头节点可以统一处理所有节点
     * 3. 内存管理：确保被删除节点能够被垃圾回收
     * 
     * 与机器学习等领域的联系：
     * 1. 数据清洗过程中需要删除不符合条件的数据节点
     * 2. 在图神经网络中，可能需要移除特定类型的节点
     * 
     * 语言特性差异：
     * Java: 垃圾回收自动回收无引用的对象
     * C++: 需要手动delete被删除的节点避免内存泄漏
     * Python: 垃圾回收机制自动处理
     * 
     * 极端输入场景：
     * 1. 空链表
     * 2. 所有节点值都等于目标值
     * 3. 没有节点值等于目标值
     * 4. 只有头节点值等于目标值
     * 5. 只有尾节点值等于目标值
     * 6. 交替出现目标值和非目标值
     */

}
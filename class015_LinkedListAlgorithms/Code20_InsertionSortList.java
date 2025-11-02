package class034;

// 对链表进行插入排序
// 测试链接：https://leetcode.cn/problems/insertion-sort-list/
public class Code20_InsertionSortList {

    // 不要提交这个类
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    /**
     * 对链表进行插入排序
     * @param head 链表头节点
     * @return 排序后的链表头节点
     * 
     * 解题思路：
     * 1. 使用虚拟头节点简化操作
     * 2. 维护已排序部分和未排序部分
     * 3. 从未排序部分取节点，插入到已排序部分的正确位置
     * 
     * 时间复杂度：O(n²) - n 是链表节点数量
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：对于链表插入排序是
     */
    public static ListNode insertionSortList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        
        // 创建虚拟头节点
        ListNode dummy = new ListNode(0);
        
        // 遍历原链表
        ListNode current = head;
        while (current != null) {
            // 保存下一个节点
            ListNode next = current.next;
            
            // 在已排序部分找到插入位置
            ListNode prev = dummy;
            while (prev.next != null && prev.next.val < current.val) {
                prev = prev.next;
            }
            
            // 插入当前节点
            current.next = prev.next;
            prev.next = current;
            
            // 移动到下一个未排序节点
            current = next;
        }
        
        // 返回排序后的链表
        return dummy.next;
    }
    
    /*
     * 题目扩展：LeetCode 147. 对链表进行插入排序
     * 来源：LeetCode、牛客网、剑指Offer等各大算法平台
     * 
     * 题目描述：
     * 给定单个链表的头 head ，使用插入排序对链表进行排序，并返回排序后链表的头。
     * 插入排序算法的步骤：
     * 插入排序是迭代的，每次只移动一个元素，直到所有元素可以形成一个有序的输出列表。
     * 每次迭代中，插入排序只从输入数据中移除一个待排序的元素，找到它在序列中适当的位置，并将其插入。
     * 重复直到所有输入数据插入完为止。
     * 
     * 解题思路：
     * 1. 使用虚拟头节点简化操作
     * 2. 维护已排序部分和未排序部分
     * 3. 从未排序部分取节点，插入到已排序部分的正确位置
     * 
     * 时间复杂度：O(n²) - n 是链表节点数量
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：对于链表插入排序是
     * 
     * 工程化考量：
     * 1. 边界情况处理：空链表、单节点链表
     * 2. 代码可读性：虚拟头节点简化逻辑
     * 3. 性能优化：提前终止条件优化
     * 
     * 与机器学习等领域的联系：
     * 1. 在在线学习算法中，需要动态维护有序数据结构
     * 2. 在流式数据处理中，需要逐步排序数据
     * 
     * 语言特性差异：
     * Java: 对象引用操作直观
     * C++: 指针操作更直接但需注意内存安全
     * Python: 语法简洁，但性能不如Java/C++
     * 
     * 极端输入场景：
     * 1. 空链表
     * 2. 已排序链表
     * 3. 逆序链表
     * 4. 所有元素相同
     * 
     * 设计的利弊：
     * 1. 优点：原地排序，空间复杂度低
     * 2. 缺点：时间复杂度较高，不适合大数据量
     * 
     * 为什么这么写：
     * 1. 虚拟头节点：简化插入操作，统一处理所有情况
     * 2. 双指针：维护已排序部分的尾部和插入位置
     * 3. 原地操作：不使用额外空间存储节点
     */
}
package class034;

// 奇偶链表
// 测试链接：https://leetcode.cn/problems/odd-even-linked-list/
public class Code16_OddEvenLinkedList {

    // 不要提交这个类
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    /**
     * 奇偶链表重排
     * @param head 链表头节点
     * @return 重排后的链表头节点
     * 
     * 解题思路：
     * 1. 使用两个指针分别追踪奇数位置和偶数位置的节点
     * 2. 分别构建奇数链表和偶数链表
     * 3. 将偶数链表连接到奇数链表后面
     * 
     * 时间复杂度：O(n) - 需要遍历整个链表
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：是
     */
    public static ListNode oddEvenList(ListNode head) {
        // 边界情况处理
        if (head == null || head.next == null) {
            return head;
        }
        
        // 初始化奇数链表和偶数链表的指针
        ListNode odd = head;           // 奇数链表的当前节点
        ListNode even = head.next;     // 偶数链表的当前节点
        ListNode evenHead = even;      // 偶数链表的头节点
        
        // 分别构建奇数链表和偶数链表
        while (even != null && even.next != null) {
            // 连接奇数节点
            odd.next = even.next;
            odd = odd.next;
            
            // 连接偶数节点
            even.next = odd.next;
            even = even.next;
        }
        
        // 将偶数链表连接到奇数链表后面
        odd.next = evenHead;
        
        return head;
    }
    
    /*
     * 题目扩展：LeetCode 328. 奇偶链表
     * 来源：LeetCode、牛客网、剑指Offer等各大算法平台
     * 
     * 题目描述：
     * 给定单链表的头节点 head ，将所有索引为奇数的节点和索引为偶数的节点分别组合在一起，
     * 然后返回重新排序的列表。第一个节点的索引被认为是奇数，第二个节点的索引为偶数，以此类推。
     * 注意：偶数组和奇数组内部的相对顺序应与输入时保持一致。
     * 
     * 解题思路：
     * 1. 使用两个指针分别追踪奇数位置和偶数位置的节点
     * 2. 分别构建奇数链表和偶数链表
     * 3. 将偶数链表连接到奇数链表后面
     * 
     * 时间复杂度：O(n) - 需要遍历整个链表
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：是
     * 
     * 工程化考量：
     * 1. 边界情况处理：空链表、单节点链表、两节点链表
     * 2. 指针操作：正确维护链表结构，避免形成环
     * 3. 代码可读性：变量命名清晰表达意图
     * 
     * 与机器学习等领域的联系：
     * 1. 在处理时间序列数据时，有时需要分别处理奇数位置和偶数位置的数据
     * 2. 在特征工程中，可能需要对特征按位置进行分组处理
     * 
     * 语言特性差异：
     * Java: 空指针检查
     * C++: 指针操作
     * Python: 对象引用
     * 
     * 极端输入场景：
     * 1. 空链表
     * 2. 单节点链表
     * 3. 两节点链表
     * 4. 奇数个节点的链表
     * 5. 偶数个节点的链表
     * 
     * 关键设计点：
     * 1. 双指针技巧：分别处理奇数位置和偶数位置节点
     * 2. 链表重组：保持原有相对顺序，只改变连接关系
     * 3. 边界条件：循环条件为 even != null && even.next != null
     */

}
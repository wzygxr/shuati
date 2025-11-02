package class034;

// 两数相加
// 测试链接：https://leetcode.cn/problems/add-two-numbers/
public class Code14_AddTwoNumbers {

    // 不要提交这个类
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    /**
     * 两数相加
     * @param l1 第一个数（逆序存储）
     * @param l2 第二个数（逆序存储）
     * @return 两数之和（逆序存储）
     * 
     * 解题思路：
     * 1. 同时遍历两个链表
     * 2. 对应位置相加，处理进位
     * 3. 处理长度不同的情况
     * 4. 处理最后的进位
     * 
     * 时间复杂度：O(max(m,n)) - m和n分别是两个链表的长度
     * 空间复杂度：O(1) - 不考虑返回结果的空间
     * 是否最优解：是
     */
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        // 创建虚拟头节点，简化边界处理
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        int carry = 0; // 进位
        
        // 同时遍历两个链表
        while (l1 != null || l2 != null) {
            // 获取当前节点的值（如果节点为空则为0）
            int x = (l1 != null) ? l1.val : 0;
            int y = (l2 != null) ? l2.val : 0;
            
            // 计算当前位的和
            int sum = x + y + carry;
            carry = sum / 10; // 计算进位
            
            // 创建新节点存储当前位的结果
            current.next = new ListNode(sum % 10);
            current = current.next;
            
            // 移动到下一个节点
            if (l1 != null) l1 = l1.next;
            if (l2 != null) l2 = l2.next;
        }
        
        // 处理最后的进位
        if (carry > 0) {
            current.next = new ListNode(carry);
        }
        
        // 返回结果链表的头节点
        return dummy.next;
    }
    
    /*
     * 题目扩展：LeetCode 2. 两数相加
     * 来源：LeetCode、牛客网、剑指Offer等各大算法平台
     * 
     * 题目描述：
     * 给你两个非空的链表，表示两个非负的整数。
     * 它们每位数字都是按照逆序的方式存储的，并且每个节点只能存储一位数字。
     * 请你将两个数相加，并以相同形式返回一个表示和的链表。
     * 
     * 解题思路：
     * 1. 模拟手工加法过程
     * 2. 同时遍历两个链表
     * 3. 对应位置相加，处理进位
     * 4. 处理长度不同的情况
     * 5. 处理最后的进位
     * 
     * 时间复杂度：O(max(m,n)) - m和n分别是两个链表的长度
     * 空间复杂度：O(1) - 不考虑返回结果的空间
     * 是否最优解：是
     * 
     * 工程化考量：
     * 1. 边界情况处理：空链表、不同长度链表、最后的进位
     * 2. 代码可读性：使用虚拟头节点简化操作
     * 3. 变量命名：carry表示进位，sum表示当前位的和
     * 
     * 与机器学习等领域的联系：
     * 1. 在处理大整数运算时，可能需要使用链表存储数字
     * 2. 在密码学中，大整数运算很常见
     * 
     * 语言特性差异：
     * Java: 三元运算符简化空节点处理
     * C++: 指针操作
     * Python: 简洁的条件表达式
     * 
     * 极端输入场景：
     * 1. 其中一个链表为空
     * 2. 两个链表长度不同
     * 3. 最后需要进位
     * 4. 所有位都需要进位（如999+1）
     * 
     * 相关题目扩展：
     * 1. LeetCode 445. 两数相加 II - 数字按正序存储
     * 2. 区别：本题数字按逆序存储，更方便计算；LeetCode 445需要先反转链表或使用栈
     */

}
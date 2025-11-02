package class034;

// 旋转链表
// 测试链接：https://leetcode.cn/problems/rotate-list/
public class Code15_RotateList {

    // 不要提交这个类
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    /**
     * 旋转链表
     * @param head 链表头节点
     * @param k 旋转步数
     * @return 旋转后的链表头节点
     * 
     * 解题思路：
     * 1. 遍历链表获取长度并形成环
     * 2. 计算实际需要旋转的步数
     * 3. 找到新的尾节点和头节点
     * 4. 断开环，形成新的链表
     * 
     * 时间复杂度：O(n) - 需要遍历链表
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：是
     */
    public static ListNode rotateRight(ListNode head, int k) {
        // 边界情况处理
        if (head == null || head.next == null || k == 0) {
            return head;
        }
        
        // 步骤1: 计算链表长度并找到尾节点
        ListNode tail = head;
        int length = 1;
        while (tail.next != null) {
            tail = tail.next;
            length++;
        }
        
        // 步骤2: 计算实际需要旋转的步数
        k = k % length;
        if (k == 0) {
            return head; // 不需要旋转
        }
        
        // 步骤3: 将链表首尾相连形成环
        tail.next = head;
        
        // 步骤4: 找到新的尾节点位置
        ListNode newTail = head;
        for (int i = 0; i < length - k - 1; i++) {
            newTail = newTail.next;
        }
        
        // 步骤5: 新的头节点是新尾节点的下一个节点
        ListNode newHead = newTail.next;
        
        // 步骤6: 断开环
        newTail.next = null;
        
        return newHead;
    }
    
    /*
     * 题目扩展：LeetCode 61. 旋转链表
     * 来源：LeetCode、牛客网、剑指Offer等各大算法平台
     * 
     * 题目描述：
     * 给你一个链表的头节点 head ，旋转链表，将链表每个节点向右移动 k 个位置。
     * 
     * 解题思路：
     * 1. 遍历链表获取长度并形成环
     * 2. 计算实际需要旋转的步数（k % length）
     * 3. 找到新的尾节点和头节点
     * 4. 断开环，形成新的链表
     * 
     * 时间复杂度：O(n) - 需要遍历链表
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：是
     * 
     * 工程化考量：
     * 1. 边界情况处理：空链表、单节点链表、k为0或链表长度的倍数
     * 2. 数学优化：使用模运算计算实际旋转步数
     * 3. 指针操作：正确维护链表结构
     * 
     * 与机器学习等领域的联系：
     * 1. 在循环神经网络中，有时需要对序列进行循环移位
     * 2. 在数据增强中，对序列数据进行旋转操作
     * 
     * 语言特性差异：
     * Java: 空指针检查、模运算
     * C++: 指针操作
     * Python: 简洁的模运算
     * 
     * 极端输入场景：
     * 1. 空链表
     * 2. 单节点链表
     * 3. k为0
     * 4. k大于链表长度
     * 5. k等于链表长度（相当于不旋转）
     * 
     * 关键设计点：
     * 1. 使用模运算优化：k % length
     * 2. 链表成环技巧：简化旋转操作
     * 3. 正确断开环：避免形成循环链表
     */

}
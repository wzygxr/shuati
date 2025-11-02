package class034;

// 合并两个有序链表
// 测试链接：https://leetcode.cn/problems/merge-two-sorted-lists/
public class Code10_MergeTwoSortedLists {

    // 不要提交这个类
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    /**
     * 合并两个有序链表（迭代法）
     * @param list1 第一个有序链表
     * @param list2 第二个有序链表
     * @return 合并后的有序链表
     * 
     * 解题思路：
     * 1. 使用虚拟头节点简化操作
     * 2. 使用双指针分别遍历两个链表
     * 3. 比较节点值，将较小的节点连接到结果链表
     * 4. 处理剩余节点
     * 
     * 时间复杂度：O(m+n) - m和n分别是两个链表的长度
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：是
     */
    public static ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        // 创建虚拟头节点，简化边界处理
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        
        // 双指针遍历两个链表
        while (list1 != null && list2 != null) {
            // 比较两个链表当前节点的值
            if (list1.val <= list2.val) {
                current.next = list1;
                list1 = list1.next;
            } else {
                current.next = list2;
                list2 = list2.next;
            }
            current = current.next;
        }
        
        // 连接剩余节点
        current.next = (list1 != null) ? list1 : list2;
        
        // 返回合并后的链表
        return dummy.next;
    }
    
    /**
     * 合并两个有序链表（递归法）
     * @param list1 第一个有序链表
     * @param list2 第二个有序链表
     * @return 合并后的有序链表
     * 
     * 解题思路：
     * 1. 递归处理链表
     * 2. 每次选择值较小的节点作为当前节点
     * 3. 递归处理剩余部分
     * 
     * 时间复杂度：O(m+n) - m和n分别是两个链表的长度
     * 空间复杂度：O(m+n) - 递归调用栈的深度
     * 是否最优解：不是（空间复杂度较高）
     */
    public static ListNode mergeTwoListsRecursive(ListNode list1, ListNode list2) {
        // 基本情况
        if (list1 == null) return list2;
        if (list2 == null) return list1;
        
        // 递归选择较小节点
        if (list1.val <= list2.val) {
            list1.next = mergeTwoListsRecursive(list1.next, list2);
            return list1;
        } else {
            list2.next = mergeTwoListsRecursive(list1, list2.next);
            return list2;
        }
    }
    
    /*
     * 题目扩展：LeetCode 21. 合并两个有序链表
     * 来源：LeetCode、牛客网、剑指Offer等各大算法平台
     * 
     * 题目描述：
     * 将两个升序链表合并为一个新的升序链表并返回。
     * 新链表是通过拼接给定的两个链表的所有节点组成的。
     * 
     * 解题思路：
     * 方法一：迭代法（推荐）
     * 1. 使用虚拟头节点简化边界处理
     * 2. 双指针分别遍历两个链表
     * 3. 比较节点值，将较小的节点连接到结果链表
     * 4. 处理剩余节点
     * 
     * 方法二：递归法
     * 1. 递归处理链表
     * 2. 每次选择值较小的节点作为当前节点
     * 3. 递归处理剩余部分
     * 
     * 时间复杂度：
     * - 迭代法：O(m+n)
     * - 递归法：O(m+n)
     * 
     * 空间复杂度：
     * - 迭代法：O(1)
     * - 递归法：O(m+n) - 递归调用栈
     * 
     * 是否最优解：迭代法是最优解
     * 
     * 工程化考量：
     * 1. 边界情况处理：空链表、一个链表遍历完
     * 2. 代码可读性：虚拟头节点简化逻辑
     * 3. 性能优化：迭代法空间效率更高
     * 
     * 与机器学习等领域的联系：
     * 1. 在归并排序算法中，需要合并两个有序数组/链表
     * 2. 在处理多个有序数据流时，需要合并操作
     * 
     * 语言特性差异：
     * Java: 三元运算符简化代码
     * C++: 指针操作更直接
     * Python: 语法简洁，但性能不如Java/C++
     * 
     * 极端输入场景：
     * 1. 其中一个链表为空
     * 2. 两个链表都为空
     * 3. 一个链表的所有元素都小于另一个链表
     * 4. 两个链表交替出现较小元素
     * 
     * 递归与非递归的区别对比：
     * 1. 递归法代码更简洁，但空间复杂度高
     * 2. 迭代法效率更高，适合处理大数据
     * 3. 在链表很长时，递归可能导致栈溢出
     */

}
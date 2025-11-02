package class034;

// 相交链表 - LeetCode 160
// 测试链接: https://leetcode.cn/problems/intersection-of-two-linked-lists/
public class Code40_IntersectionOfTwoLinkedLists {

    // 提交时不要提交这个类
    public static class ListNode {
        public int val;
        public ListNode next;
        
        public ListNode() {}
        
        public ListNode(int val) {
            this.val = val;
        }
        
        public ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    // 提交如下的方法 - 双指针法
    public static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) {
            return null;
        }
        
        ListNode pA = headA;
        ListNode pB = headB;
        
        // 当pA和pB不相等时继续循环
        // 如果链表相交，它们最终会在交点相遇
        // 如果不相交，它们最终都会变为null
        while (pA != pB) {
            // 如果pA到达链表A的末尾，则转向链表B的头部
            // 否则继续前进
            pA = (pA == null) ? headB : pA.next;
            
            // 如果pB到达链表B的末尾，则转向链表A的头部
            // 否则继续前进
            pB = (pB == null) ? headA : pB.next;
        }
        
        // 返回交点（如果不相交，pA和pB都会是null）
        return pA;
    }
    
    // 方法2：计算长度差法
    public static ListNode getIntersectionNodeByLength(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) {
            return null;
        }
        
        // 计算两个链表的长度
        int lenA = getLength(headA);
        int lenB = getLength(headB);
        
        // 调整较长链表的起始指针，使两个链表剩余长度相等
        ListNode pA = headA;
        ListNode pB = headB;
        
        if (lenA > lenB) {
            for (int i = 0; i < lenA - lenB; i++) {
                pA = pA.next;
            }
        } else if (lenB > lenA) {
            for (int i = 0; i < lenB - lenA; i++) {
                pB = pB.next;
            }
        }
        
        // 同时遍历两个链表，寻找交点
        while (pA != null && pA != pB) {
            pA = pA.next;
            pB = pB.next;
        }
        
        return pA; // 如果不相交，返回null
    }
    
    // 辅助方法：计算链表长度
    private static int getLength(ListNode head) {
        int length = 0;
        ListNode curr = head;
        while (curr != null) {
            length++;
            curr = curr.next;
        }
        return length;
    }
    
    /*
     * 题目扩展：LeetCode 160. 相交链表
     * 来源：LeetCode、LintCode、牛客网、剑指Offer
     * 
     * 题目描述：
     * 给你两个单链表的头节点 headA 和 headB，请你找出并返回两个单链表相交的起始节点。如果两个链表不存在相交节点，返回 null。
     * 题目数据 保证 整个链式结构中不存在环。
     * 注意，函数返回结果后，链表必须 保持其原始结构 。
     * 
     * 解题思路（双指针法）：
     * 1. 创建两个指针pA和pB分别指向headA和headB
     * 2. 同时遍历两个链表
     * 3. 当一个指针到达链表末尾时，将其重定向到另一个链表的头部
     * 4. 继续遍历，两个指针最终会相遇在交点（如果存在）或同时为null（如果不存在交点）
     * 原理：假设链表A长度为a+c，链表B长度为b+c，其中c是公共部分长度
     *      pA遍历a+c+b个节点，pB遍历b+c+a个节点，最终会在交点相遇
     * 
     * 时间复杂度：O(n+m) - n和m分别是两个链表的长度
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 
     * 解题思路（长度差法）：
     * 1. 计算两个链表的长度
     * 2. 调整较长链表的起始指针，使两个链表剩余长度相等
     * 3. 同时遍历两个链表，寻找第一个相同的节点
     * 
     * 时间复杂度：O(n+m)
     * 空间复杂度：O(1)
     * 
     * 最优解：两种方法时间复杂度相同，双指针法代码更简洁优雅
     * 
     * 工程化考量：
     * 1. 边界情况处理：空链表
     * 2. 异常处理：确保指针操作的安全性
     * 3. 代码可读性：双指针法思路巧妙但需要理解，长度差法更直观
     * 4. 性能优化：两种方法都是线性时间复杂度，效率相当
     * 
     * 与机器学习等领域的联系：
     * 1. 在图算法中，寻找公共节点的问题与此类似
     * 2. 在数据结构设计中，链表的相交问题需要特别注意内存管理
     * 3. 在路径规划算法中，寻找交点的思想有应用
     * 
     * 语言特性差异：
     * Java: 注意对象引用的比较（==比较的是引用，不是值）
     * C++: 可以直接比较指针
     * Python: 注意节点对象的比较方式
     * 
     * 极端输入场景：
     * 1. 空链表：返回null
     * 2. 两个链表不相交：返回null
     * 3. 其中一个链表是另一个链表的前缀
     * 4. 两个链表完全相同
     * 5. 相交点在链表的末尾
     */
    
    // 辅助方法：打印链表
    public static String printList(ListNode head) {
        StringBuilder sb = new StringBuilder();
        while (head != null) {
            sb.append(head.val);
            if (head.next != null) {
                sb.append(" -> ");
            }
            head = head.next;
        }
        return sb.toString();
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 创建测试用例1：相交链表
        // 公共部分: 8 -> 4 -> 5
        ListNode common = new ListNode(8);
        common.next = new ListNode(4);
        common.next.next = new ListNode(5);
        
        // 链表A: 4 -> 1 -> 8 -> 4 -> 5
        ListNode headA1 = new ListNode(4);
        headA1.next = new ListNode(1);
        headA1.next.next = common;
        
        // 链表B: 5 -> 6 -> 1 -> 8 -> 4 -> 5
        ListNode headB1 = new ListNode(5);
        headB1.next = new ListNode(6);
        headB1.next.next = new ListNode(1);
        headB1.next.next.next = common;
        
        System.out.println("测试用例1 - 相交链表:");
        System.out.println("链表A: " + printList(headA1));
        System.out.println("链表B: " + printList(headB1));
        
        ListNode intersection1 = getIntersectionNode(headA1, headB1);
        System.out.println("双指针法交点: " + (intersection1 != null ? intersection1.val : "null"));
        
        // 创建测试用例2：不相交链表
        ListNode headA2 = new ListNode(2);
        headA2.next = new ListNode(6);
        headA2.next.next = new ListNode(4);
        
        ListNode headB2 = new ListNode(1);
        headB2.next = new ListNode(5);
        
        System.out.println("\n测试用例2 - 不相交链表:");
        System.out.println("链表A: " + printList(headA2));
        System.out.println("链表B: " + printList(headB2));
        
        ListNode intersection2 = getIntersectionNodeByLength(headA2, headB2);
        System.out.println("长度差法交点: " + (intersection2 != null ? intersection2.val : "null"));
        
        // 创建测试用例3：一个链表为空
        ListNode headA3 = null;
        ListNode headB3 = new ListNode(1);
        
        System.out.println("\n测试用例3 - 空链表:");
        ListNode intersection3 = getIntersectionNode(headA3, headB3);
        System.out.println("双指针法交点: " + (intersection3 != null ? intersection3.val : "null"));
        
        // 创建测试用例4：一个链表是另一个的前缀
        ListNode common4 = new ListNode(3);
        common4.next = new ListNode(4);
        
        ListNode headA4 = common4;
        ListNode headB4 = new ListNode(1);
        headB4.next = new ListNode(2);
        headB4.next.next = common4;
        
        System.out.println("\n测试用例4 - 链表A是链表B的前缀:");
        System.out.println("链表A: " + printList(headA4));
        System.out.println("链表B: " + printList(headB4));
        
        ListNode intersection4 = getIntersectionNode(headA4, headB4);
        System.out.println("双指针法交点: " + (intersection4 != null ? intersection4.val : "null"));
    }
}
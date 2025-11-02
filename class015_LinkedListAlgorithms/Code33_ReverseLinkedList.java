package class034;

// 反转链表 - LeetCode 206
// 测试链接: https://leetcode.cn/problems/reverse-linked-list/
public class Code33_ReverseLinkedList {

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

    // 提交如下的方法 - 迭代法
    public static ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;
        
        while (curr != null) {
            ListNode nextTemp = curr.next; // 保存下一个节点
            curr.next = prev;              // 反转当前节点的指针
            prev = curr;                   // 移动prev指针
            curr = nextTemp;               // 移动curr指针
        }
        
        return prev; // prev现在指向新的头节点
    }
    
    // 方法2：递归法
    public static ListNode reverseListRecursive(ListNode head) {
        // 基本情况：空链表或单节点链表
        if (head == null || head.next == null) {
            return head;
        }
        
        // 递归反转剩余部分
        ListNode newHead = reverseListRecursive(head.next);
        
        // 反转当前节点与下一个节点的连接
        head.next.next = head;
        head.next = null;
        
        return newHead; // 返回新的头节点
    }
    
    // 方法3：头插法
    public static ListNode reverseListHeadInsert(ListNode head) {
        ListNode dummy = new ListNode(0); // 创建哑节点
        ListNode curr = head;
        
        while (curr != null) {
            ListNode nextTemp = curr.next; // 保存下一个节点
            curr.next = dummy.next;        // 将当前节点插入到dummy后面
            dummy.next = curr;
            curr = nextTemp;               // 移动到下一个节点
        }
        
        return dummy.next; // 返回新的头节点
    }
    
    /*
     * 题目扩展：LeetCode 206. 反转链表
     * 来源：LeetCode、LintCode、剑指Offer、牛客网
     * 
     * 题目描述：
     * 给你单链表的头节点 head ，请你反转链表，并返回反转后的链表。
     * 
     * 解题思路（迭代法）：
     * 1. 使用三个指针：prev（前驱）、curr（当前）、nextTemp（临时保存下一个节点）
     * 2. 遍历链表，逐个反转节点的指针
     * 3. 每次迭代后，prev和curr都向前移动
     * 4. 最终prev指向新的头节点
     * 
     * 时间复杂度：O(n) - 需要遍历链表一次
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 
     * 解题思路（递归法）：
     * 1. 递归反转链表的剩余部分
     * 2. 调整当前节点的指针指向
     * 3. 注意递归的终止条件
     * 
     * 时间复杂度：O(n) - 需要递归n次
     * 空间复杂度：O(n) - 递归调用栈的深度
     * 
     * 最优解：迭代法空间复杂度最优，递归法代码更简洁
     * 
     * 工程化考量：
     * 1. 边界情况处理：空链表、单节点链表
     * 2. 异常处理：确保指针操作的安全性
     * 3. 代码可读性：三种实现方式各有特点
     * 4. 性能优化：迭代法避免了递归调用栈的开销
     * 
     * 与机器学习等领域的联系：
     * 1. 在数据处理中，序列反转是基础操作
     * 2. 在递归神经网络中，序列处理的思想与此类似
     * 3. 在链表排序算法中，反转是重要的子操作
     * 
     * 语言特性差异：
     * Java: 需要手动管理指针，注意空指针问题
     * C++: 可以直接操作指针，效率更高
     * Python: 可以利用语言特性简化实现
     * 
     * 极端输入场景：
     * 1. 空链表：返回null
     * 2. 单节点链表：返回头节点本身
     * 3. 非常长的链表：递归法可能导致栈溢出
     * 4. 已经反转的链表：再次反转恢复原状
     */
    
    // 辅助方法：构建链表
    public static ListNode buildList(int[] nums) {
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        for (int num : nums) {
            cur.next = new ListNode(num);
            cur = cur.next;
        }
        return dummy.next;
    }
    
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
        // 测试用例1: [1,2,3,4,5]
        ListNode head1 = buildList(new int[]{1, 2, 3, 4, 5});
        System.out.println("原始链表1: " + printList(head1));
        ListNode result1 = reverseList(head1);
        System.out.println("迭代法反转后: " + printList(result1));
        
        // 测试用例2: [1,2]
        ListNode head2 = buildList(new int[]{1, 2});
        System.out.println("\n原始链表2: " + printList(head2));
        ListNode result2 = reverseListRecursive(head2);
        System.out.println("递归法反转后: " + printList(result2));
        
        // 测试用例3: []
        ListNode head3 = null;
        System.out.println("\n原始链表3: " + printList(head3));
        ListNode result3 = reverseList(head3);
        System.out.println("迭代法反转后: " + printList(result3));
        
        // 测试用例4: [5]
        ListNode head4 = new ListNode(5);
        System.out.println("\n原始链表4: " + printList(head4));
        ListNode result4 = reverseListHeadInsert(head4);
        System.out.println("头插法反转后: " + printList(result4));
    }
}
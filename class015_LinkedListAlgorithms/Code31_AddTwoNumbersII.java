package class034;

import java.util.Stack;

// 两数相加II - LeetCode 445
// 测试链接: https://leetcode.cn/problems/add-two-numbers-ii/
public class Code31_AddTwoNumbersII {

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

    // 提交如下的方法 - 栈解法
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        Stack<Integer> stack1 = new Stack<>();
        Stack<Integer> stack2 = new Stack<>();
        
        // 将两个链表的值分别压入栈中
        while (l1 != null) {
            stack1.push(l1.val);
            l1 = l1.next;
        }
        while (l2 != null) {
            stack2.push(l2.val);
            l2 = l2.next;
        }
        
        int carry = 0; // 进位
        ListNode dummy = null; // 用于构建结果链表
        
        // 同时弹出栈顶元素进行相加
        while (!stack1.isEmpty() || !stack2.isEmpty() || carry > 0) {
            int sum = carry;
            if (!stack1.isEmpty()) {
                sum += stack1.pop();
            }
            if (!stack2.isEmpty()) {
                sum += stack2.pop();
            }
            
            // 计算当前位的值和新的进位
            int digit = sum % 10;
            carry = sum / 10;
            
            // 创建新节点并插入到结果链表的头部（逆序构建）
            ListNode newNode = new ListNode(digit);
            newNode.next = dummy;
            dummy = newNode;
        }
        
        return dummy;
    }
    
    // 方法2：反转链表后相加
    public static ListNode addTwoNumbersReverse(ListNode l1, ListNode l2) {
        // 反转两个链表
        ListNode reversedL1 = reverseList(l1);
        ListNode reversedL2 = reverseList(l2);
        
        // 执行相加操作
        ListNode result = addTwoNumbersReversed(reversedL1, reversedL2);
        
        // 反转结果链表
        return reverseList(result);
    }
    
    // 反转链表的辅助方法
    private static ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;
        while (curr != null) {
            ListNode nextTemp = curr.next;
            curr.next = prev;
            prev = curr;
            curr = nextTemp;
        }
        return prev;
    }
    
    // 两个已反转的链表相加（低位在前）
    private static ListNode addTwoNumbersReversed(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        int carry = 0;
        
        while (l1 != null || l2 != null || carry > 0) {
            int sum = carry;
            if (l1 != null) {
                sum += l1.val;
                l1 = l1.next;
            }
            if (l2 != null) {
                sum += l2.val;
                l2 = l2.next;
            }
            
            curr.next = new ListNode(sum % 10);
            carry = sum / 10;
            curr = curr.next;
        }
        
        return dummy.next;
    }
    
    /*
     * 题目扩展：LeetCode 445. 两数相加II
     * 来源：LeetCode、牛客网
     * 
     * 题目描述：
     * 给你两个 非空 链表来代表两个非负整数。数字最高位位于链表开始位置。
     * 它们的每个节点只存储一位数字。将这两数相加会返回一个新的链表。
     * 你可以假设除了数字 0 之外，这两个数字都不会以零开头。
     * 
     * 解题思路（栈解法）：
     * 1. 使用两个栈分别存储两个链表的值
     * 2. 同时弹出栈顶元素进行相加，并处理进位
     * 3. 每次将计算结果插入到新链表的头部（因为是从低位到高位计算）
     * 
     * 时间复杂度：O(n + m) - n和m分别是两个链表的长度
     * 空间复杂度：O(n + m) - 需要使用两个栈存储节点值
     * 是否最优解：是，时间复杂度最优，空间复杂度也无法避免（除非使用链表反转）
     * 
     * 工程化考量：
     * 1. 边界情况处理：空链表、其中一个链表为空
     * 2. 异常处理：确保节点值在0-9范围内（题目已保证）
     * 3. 代码可读性：栈操作逻辑清晰
     * 4. 性能优化：避免重复遍历链表
     * 
     * 与机器学习等领域的联系：
     * 1. 在大数运算中，类似的栈操作常用于处理超出数据类型范围的计算
     * 2. 在数字信号处理中，栈用于反转序列
     * 
     * 语言特性差异：
     * Java: 使用Stack类或LinkedList实现栈
     * C++: 使用std::stack或std::vector
     * Python: 使用列表作为栈
     * 
     * 极端输入场景：
     * 1. 空链表：按题意不会出现
     * 2. 其中一个链表为0
     * 3. 结果产生新的最高位（如999+1=1000）
     * 4. 两个链表长度相差很大
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
        // 测试用例1: l1 = [7,2,4,3], l2 = [5,6,4] -> [7,8,0,7]
        ListNode l1 = buildList(new int[]{7, 2, 4, 3});
        ListNode l2 = buildList(new int[]{5, 6, 4});
        System.out.println("l1: " + printList(l1));
        System.out.println("l2: " + printList(l2));
        ListNode result = addTwoNumbers(l1, l2);
        System.out.println("相加结果: " + printList(result));
        
        // 测试用例2: l1 = [2,4,3], l2 = [5,6,4] -> [8,0,7]
        ListNode l3 = buildList(new int[]{2, 4, 3});
        ListNode l4 = buildList(new int[]{5, 6, 4});
        System.out.println("\nl1: " + printList(l3));
        System.out.println("l2: " + printList(l4));
        ListNode result2 = addTwoNumbers(l3, l4);
        System.out.println("相加结果: " + printList(result2));
        
        // 测试用例3: l1 = [0], l2 = [0] -> [0]
        ListNode l5 = new ListNode(0);
        ListNode l6 = new ListNode(0);
        System.out.println("\nl1: " + printList(l5));
        System.out.println("l2: " + printList(l6));
        ListNode result3 = addTwoNumbers(l5, l6);
        System.out.println("相加结果: " + printList(result3));
        
        // 测试反转链表方法
        System.out.println("\n反转链表方法测试:");
        ListNode l7 = buildList(new int[]{7, 2, 4, 3});
        ListNode l8 = buildList(new int[]{5, 6, 4});
        ListNode resultReverse = addTwoNumbersReverse(l7, l8);
        System.out.println("相加结果: " + printList(resultReverse));
    }
}
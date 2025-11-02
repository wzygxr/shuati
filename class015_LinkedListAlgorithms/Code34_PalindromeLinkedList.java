package class034;

import java.util.Stack;

// 回文链表 - LeetCode 234
// 测试链接: https://leetcode.cn/problems/palindrome-linked-list/
public class Code34_PalindromeLinkedList {

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
    public static boolean isPalindrome(ListNode head) {
        if (head == null || head.next == null) {
            return true; // 空链表或单节点链表是回文
        }
        
        Stack<Integer> stack = new Stack<>();
        ListNode curr = head;
        
        // 将所有节点值入栈
        while (curr != null) {
            stack.push(curr.val);
            curr = curr.next;
        }
        
        // 重新遍历链表，与栈顶元素比较
        curr = head;
        while (curr != null) {
            if (curr.val != stack.pop()) {
                return false; // 不是回文
            }
            curr = curr.next;
        }
        
        return true; // 是回文
    }
    
    // 方法2：快慢指针 + 反转链表（空间复杂度O(1)）
    public static boolean isPalindromeOptimal(ListNode head) {
        if (head == null || head.next == null) {
            return true;
        }
        
        // 找到链表的中点
        ListNode slow = head;
        ListNode fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        
        // 反转后半部分链表
        ListNode secondHalfHead = reverseList(slow.next);
        ListNode p1 = head;
        ListNode p2 = secondHalfHead;
        boolean isPalindrome = true;
        
        // 比较前半部分和反转后的后半部分
        while (p2 != null) {
            if (p1.val != p2.val) {
                isPalindrome = false;
                break;
            }
            p1 = p1.next;
            p2 = p2.next;
        }
        
        // 恢复链表（可选，但在实际应用中应该恢复）
        slow.next = reverseList(secondHalfHead);
        
        return isPalindrome;
    }
    
    // 辅助方法：反转链表
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
    
    // 方法3：递归解法
    public static boolean isPalindromeRecursive(ListNode head) {
        // 使用成员变量来跟踪前向指针
        ListNode[] front = {head};
        return checkRecursive(head, front);
    }
    
    private static boolean checkRecursive(ListNode curr, ListNode[] front) {
        if (curr == null) {
            return true;
        }
        
        // 先递归到链表末尾
        boolean isPalindrome = checkRecursive(curr.next, front);
        
        // 回溯时比较当前节点与前向指针指向的节点
        if (!isPalindrome) {
            return false;
        }
        
        boolean currentEqual = (curr.val == front[0].val);
        front[0] = front[0].next; // 前向指针前进
        return currentEqual;
    }
    
    /*
     * 题目扩展：LeetCode 234. 回文链表
     * 来源：LeetCode、LintCode、剑指Offer、牛客网
     * 
     * 题目描述：
     * 给你一个单链表的头节点 head ，请你判断该链表是否为回文链表。如果是，返回 true ；否则，返回 false 。
     * 
     * 解题思路（栈解法）：
     * 1. 将链表所有节点值压入栈中
     * 2. 再次遍历链表，同时弹出栈顶元素进行比较
     * 3. 如果所有比较都相等，则是回文链表
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     * 
     * 解题思路（快慢指针 + 反转链表）：
     * 1. 使用快慢指针找到链表的中点
     * 2. 反转后半部分链表
     * 3. 比较前半部分和反转后的后半部分
     * 4. 恢复链表（可选）
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     * 
     * 最优解：快慢指针 + 反转链表的方法，空间复杂度最优
     * 
     * 工程化考量：
     * 1. 边界情况处理：空链表、单节点链表
     * 2. 异常处理：确保指针操作的安全性
     * 3. 代码可读性：不同解法各有优势
     * 4. 性能优化：O(1)空间复杂度的解法更适合处理大型链表
     * 5. 链表恢复：在实际应用中，应恢复链表的原始状态
     * 
     * 与机器学习等领域的联系：
     * 1. 在自然语言处理中，回文检测是文本处理的基础任务
     * 2. 在计算机视觉中，对称性检测与此有相似之处
     * 3. 链表操作在数据流处理中很常见
     * 
     * 语言特性差异：
     * Java: 使用Stack类或数组模拟栈
     * C++: 可以使用std::stack或自定义栈
     * Python: 可以使用列表作为栈
     * 
     * 极端输入场景：
     * 1. 空链表：返回true
     * 2. 单节点链表：返回true
     * 3. 两节点链表：比较两个节点值
     * 4. 非常长的链表：栈解法可能导致内存不足
     * 5. 全相同值的链表：返回true
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
        // 测试用例1: [1,2,2,1] - 回文
        ListNode head1 = buildList(new int[]{1, 2, 2, 1});
        System.out.println("链表1: " + printList(head1));
        System.out.println("栈解法 - 是否回文: " + isPalindrome(head1));
        System.out.println("优化解法 - 是否回文: " + isPalindromeOptimal(head1));
        System.out.println("递归解法 - 是否回文: " + isPalindromeRecursive(head1));
        
        // 测试用例2: [1,2] - 非回文
        ListNode head2 = buildList(new int[]{1, 2});
        System.out.println("\n链表2: " + printList(head2));
        System.out.println("栈解法 - 是否回文: " + isPalindrome(head2));
        System.out.println("优化解法 - 是否回文: " + isPalindromeOptimal(head2));
        System.out.println("递归解法 - 是否回文: " + isPalindromeRecursive(head2));
        
        // 测试用例3: [1,2,3,2,1] - 回文
        ListNode head3 = buildList(new int[]{1, 2, 3, 2, 1});
        System.out.println("\n链表3: " + printList(head3));
        System.out.println("优化解法 - 是否回文: " + isPalindromeOptimal(head3));
        
        // 测试用例4: [] - 回文
        ListNode head4 = null;
        System.out.println("\n链表4: " + printList(head4));
        System.out.println("优化解法 - 是否回文: " + isPalindromeOptimal(head4));
        
        // 测试用例5: [5] - 回文
        ListNode head5 = new ListNode(5);
        System.out.println("\n链表5: " + printList(head5));
        System.out.println("优化解法 - 是否回文: " + isPalindromeOptimal(head5));
    }
}
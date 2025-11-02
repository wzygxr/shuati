package class034;

// 旋转链表 - LeetCode 61
// 测试链接: https://leetcode.cn/problems/rotate-list/
public class Code37_RotateList {

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

    // 提交如下的方法
    public static ListNode rotateRight(ListNode head, int k) {
        // 处理边界情况
        if (head == null || head.next == null || k == 0) {
            return head;
        }
        
        // 计算链表长度
        int length = 1;
        ListNode tail = head;
        while (tail.next != null) {
            length++;
            tail = tail.next;
        }
        
        // 优化k值，避免多余的旋转
        k = k % length;
        if (k == 0) {
            return head; // 不需要旋转
        }
        
        // 找到旋转点：倒数第k+1个节点
        ListNode newTail = head;
        for (int i = 0; i < length - k - 1; i++) {
            newTail = newTail.next;
        }
        
        // 重新连接链表
        ListNode newHead = newTail.next;
        newTail.next = null; // 断开链表
        tail.next = head;    // 连接成环，然后断开
        
        return newHead;
    }
    
    // 方法2：先连成环再断开
    public static ListNode rotateRightCircular(ListNode head, int k) {
        if (head == null || head.next == null || k == 0) {
            return head;
        }
        
        // 计算链表长度并找到尾节点
        int length = 1;
        ListNode tail = head;
        while (tail.next != null) {
            length++;
            tail = tail.next;
        }
        
        // 优化k值
        k = k % length;
        if (k == 0) {
            return head;
        }
        
        // 连成环
        tail.next = head;
        
        // 找到新的尾节点（旋转点）
        ListNode newTail = head;
        for (int i = 0; i < length - k - 1; i++) {
            newTail = newTail.next;
        }
        
        // 找到新的头节点并断开环
        ListNode newHead = newTail.next;
        newTail.next = null;
        
        return newHead;
    }
    
    /*
     * 题目扩展：LeetCode 61. 旋转链表
     * 来源：LeetCode、LintCode、牛客网
     * 
     * 题目描述：
     * 给你一个链表的头节点 head ，旋转链表，将链表每个节点向右移动 k 个位置。
     * 
     * 解题思路：
     * 1. 处理边界情况：空链表、单节点链表、k=0
     * 2. 计算链表长度，并找到尾节点
     * 3. 优化k值：由于每旋转length次就会回到原始状态，所以k = k % length
     * 4. 找到旋转点：新的头节点是倒数第k个节点，新的尾节点是倒数第k+1个节点
     * 5. 重新连接链表：将尾节点连接到头节点，然后在旋转点断开
     * 
     * 时间复杂度：O(n)
     * - 计算链表长度需要O(n)
     * - 找到旋转点需要O(n)
     * 
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 
     * 最优解：此解法已经是最优解
     * 
     * 工程化考量：
     * 1. 边界情况处理：空链表、单节点链表、k=0、k大于链表长度
     * 2. 异常处理：确保指针操作的安全性
     * 3. 代码可读性：逻辑清晰，注释充分
     * 4. 性能优化：通过k % length避免多余的旋转
     * 
     * 与机器学习等领域的联系：
     * 1. 在时间序列分析中，循环移位是常见的操作
     * 2. 在图像处理中，数组旋转与此有相似之处
     * 3. 链表操作在数据流处理中很常见
     * 
     * 语言特性差异：
     * Java: 注意空指针检查
     * C++: 可以利用指针操作的优势
     * Python: 可以使用更简洁的方式处理链表
     * 
     * 极端输入场景：
     * 1. 空链表：返回null
     * 2. 单节点链表：无论k是多少，都返回原链表
     * 3. k=0：不需要旋转，返回原链表
     * 4. k远大于链表长度：通过取模优化
     * 5. k等于链表长度：旋转后等于原链表
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
        // 测试用例1: head = [1,2,3,4,5], k = 2
        ListNode head1 = buildList(new int[]{1, 2, 3, 4, 5});
        System.out.println("原始链表1: " + printList(head1));
        ListNode result1 = rotateRight(head1, 2);
        System.out.println("旋转后链表1: " + printList(result1));
        
        // 测试用例2: head = [0,1,2], k = 4
        ListNode head2 = buildList(new int[]{0, 1, 2});
        System.out.println("\n原始链表2: " + printList(head2));
        ListNode result2 = rotateRightCircular(head2, 4);
        System.out.println("环形旋转后链表2: " + printList(result2));
        
        // 测试用例3: head = [1], k = 0
        ListNode head3 = new ListNode(1);
        System.out.println("\n原始链表3: " + printList(head3));
        ListNode result3 = rotateRight(head3, 0);
        System.out.println("旋转后链表3: " + printList(result3));
        
        // 测试用例4: head = [1], k = 100
        ListNode head4 = new ListNode(1);
        System.out.println("\n原始链表4: " + printList(head4));
        ListNode result4 = rotateRight(head4, 100);
        System.out.println("旋转后链表4: " + printList(result4));
        
        // 测试用例5: head = [], k = 5
        ListNode head5 = null;
        System.out.println("\n原始链表5: " + printList(head5));
        ListNode result5 = rotateRight(head5, 5);
        System.out.println("旋转后链表5: " + printList(result5));
    }
}
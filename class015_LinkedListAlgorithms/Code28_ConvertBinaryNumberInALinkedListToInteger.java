package class034;

// 二进制链表转整数 - LeetCode 1290
// 测试链接: https://leetcode.cn/problems/convert-binary-number-in-a-linked-list-to-integer/
public class Code28_ConvertBinaryNumberInALinkedListToInteger {

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

    // 提交如下的方法 - 方法1：迭代法
    public static int getDecimalValue(ListNode head) {
        int result = 0;
        ListNode curr = head;
        
        while (curr != null) {
            // 每次左移一位（乘以2），然后加上当前节点的值
            result = (result << 1) | curr.val;
            curr = curr.next;
        }
        
        return result;
    }
    
    // 方法2：递归法
    public static int getDecimalValueRecursive(ListNode head) {
        return helper(head, 0);
    }
    
    private static int helper(ListNode node, int value) {
        if (node == null) {
            return value;
        }
        // 先递归到链表末尾，再在回溯时计算值
        int nextValue = helper(node.next, value);
        // 当前位的值乘以对应的权值（2^position）
        return nextValue + (node.val << getLength(head, node));
    }
    
    // 辅助方法：计算节点在链表中的位置（从0开始）
    private static int getLength(ListNode head, ListNode target) {
        int length = 0;
        ListNode curr = head;
        while (curr != target) {
            length++;
            curr = curr.next;
        }
        // 返回从末尾开始计算的位置（权值）
        int totalLength = 0;
        curr = head;
        while (curr != null) {
            totalLength++;
            curr = curr.next;
        }
        return totalLength - 1 - length;
    }
    
    /*
     * 题目扩展：LeetCode 1290. 二进制链表转整数
     * 来源：LeetCode、LintCode、HackerRank
     * 
     * 题目描述：
     * 给你一个单链表的引用结点 head。链表中每个结点的值不是 0 就是 1。
     * 已知此链表是一个整数数字的二进制表示形式。
     * 请你返回该链表所表示数字的十进制值。
     * 
     * 解题思路：
     * 方法1（迭代法）：
     * 1. 遍历链表，对于每个节点，将当前结果左移一位（相当于乘以2）
     * 2. 然后加上当前节点的值（0或1）
     * 3. 时间复杂度O(n)，空间复杂度O(1)
     * 
     * 方法2（递归法）：
     * 1. 递归到链表末尾
     * 2. 在回溯过程中，根据节点位置计算对应的十进制值
     * 3. 时间复杂度O(n²)，空间复杂度O(n)
     * 
     * 最佳解法：迭代法
     * 时间复杂度：O(n) - 只需要遍历链表一次
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：是，无法再优化时间复杂度
     * 
     * 工程化考量：
     * 1. 边界情况处理：空链表、单节点链表
     * 2. 异常处理：确保节点值只有0和1（题目已保证）
     * 3. 代码可读性：位运算操作清晰明了
     * 4. 性能优化：使用位运算比乘除法更高效
     * 
     * 与机器学习等领域的联系：
     * 1. 在数字信号处理中，二进制到十进制的转换是基础操作
     * 2. 在机器学习模型中，位操作常用于特征工程
     * 
     * 语言特性差异：
     * Java: 位运算效率高，整数范围有限
     * C++: 可以处理更大范围的整数
     * Python: 支持大整数，实现更简洁
     * 
     * 极端输入场景：
     * 1. 空链表：返回0（根据题意，链表至少有一个节点）
     * 2. 单节点链表：返回节点值（0或1）
     * 3. 非常长的链表：注意整数溢出问题
     * 4. 全0链表：返回0
     * 5. 全1链表：返回2^n - 1
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
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1: [1,0,1] -> 5
        ListNode head1 = buildList(new int[]{1, 0, 1});
        System.out.println("二进制链表 [1,0,1] 转十进制: " + getDecimalValue(head1));
        
        // 测试用例2: [0] -> 0
        ListNode head2 = new ListNode(0);
        System.out.println("二进制链表 [0] 转十进制: " + getDecimalValue(head2));
        
        // 测试用例3: [1] -> 1
        ListNode head3 = new ListNode(1);
        System.out.println("二进制链表 [1] 转十进制: " + getDecimalValue(head3));
        
        // 测试用例4: [1,0,0,1,0,0,1,1,1,0,0,0,0,0,0] -> 18880
        ListNode head4 = buildList(new int[]{1, 0, 0, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0});
        System.out.println("二进制链表 [1,0,0,1,0,0,1,1,1,0,0,0,0,0,0] 转十进制: " + getDecimalValue(head4));
        
        // 测试递归方法
        System.out.println("\n递归方法测试:");
        System.out.println("二进制链表 [1,0,1] 转十进制: " + getDecimalValueRecursive(head1));
    }
}
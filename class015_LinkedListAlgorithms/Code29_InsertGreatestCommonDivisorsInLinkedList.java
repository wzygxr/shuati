package class034;

// 在链表中插入最大公约数 - LeetCode 2807
// 测试链接: https://leetcode.cn/problems/insert-greatest-common-divisors-in-linked-list/
public class Code29_InsertGreatestCommonDivisorsInLinkedList {

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
    public static ListNode insertGreatestCommonDivisors(ListNode head) {
        if (head == null || head.next == null) {
            return head; // 空链表或单节点链表直接返回
        }
        
        ListNode curr = head;
        while (curr.next != null) {
            int val1 = curr.val;
            int val2 = curr.next.val;
            int gcd = calculateGCD(val1, val2);
            
            // 创建新节点并插入
            ListNode newNode = new ListNode(gcd);
            newNode.next = curr.next;
            curr.next = newNode;
            
            // 移动到下一对节点
            curr = newNode.next;
        }
        
        return head;
    }
    
    // 计算最大公约数 - 欧几里得算法
    private static int calculateGCD(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
    
    // 递归实现最大公约数
    private static int calculateGCDRecursive(int a, int b) {
        if (b == 0) {
            return a;
        }
        return calculateGCDRecursive(b, a % b);
    }
    
    /*
     * 题目扩展：LeetCode 2807. 在链表中插入最大公约数
     * 来源：LeetCode
     * 
     * 题目描述：
     * 给你一个链表的头 head ，每个节点包含一个整数值。
     * 在每对相邻节点之间，插入一个新的节点，节点值为这对相邻节点值的 最大公约数 。
     * 返回插入后的链表的头节点。
     * 
     * 解题思路：
     * 1. 遍历链表，对于每对相邻节点
     * 2. 计算它们的最大公约数（使用欧几里得算法）
     * 3. 创建一个新节点，值为最大公约数
     * 4. 将新节点插入到这对节点之间
     * 5. 继续处理下一对节点
     * 
     * 时间复杂度：O(n) - 只需要遍历链表一次
     * 空间复杂度：O(1) - 只使用常数额外空间（不考虑插入的新节点）
     * 是否最优解：是，必须遍历所有节点一次
     * 
     * 工程化考量：
     * 1. 边界情况处理：空链表、单节点链表
     * 2. 异常处理：确保节点值为正整数（题目已保证）
     * 3. 代码可读性：GCD算法实现清晰
     * 4. 性能优化：欧几里得算法是计算GCD的最优算法
     * 
     * 与机器学习等领域的联系：
     * 1. 在数据预处理中，GCD可用于特征归一化
     * 2. 在密码学和安全算法中有重要应用
     * 3. 在图像处理中用于尺度变换
     * 
     * 语言特性差异：
     * Java: 可以使用Math类，但这里手动实现更清晰
     * C++: 可以使用std::gcd函数（C++17及以上）
     * Python: 可以使用math.gcd函数
     * 
     * 极端输入场景：
     * 1. 空链表：返回null
     * 2. 单节点链表：直接返回
     * 3. 节点值为1：GCD始终为1
     * 4. 节点值互质：GCD为1
     * 5. 节点值相等：GCD等于节点值
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
        // 测试用例1: [18,6,10,3]
        ListNode head1 = buildList(new int[]{18, 6, 10, 3});
        System.out.println("原始链表1: " + printList(head1));
        ListNode result1 = insertGreatestCommonDivisors(head1);
        System.out.println("插入GCD后链表1: " + printList(result1));
        
        // 测试用例2: [7]
        ListNode head2 = new ListNode(7);
        System.out.println("\n原始链表2: " + printList(head2));
        ListNode result2 = insertGreatestCommonDivisors(head2);
        System.out.println("插入GCD后链表2: " + printList(result2));
        
        // 测试用例3: [2,2,2,2]
        ListNode head3 = buildList(new int[]{2, 2, 2, 2});
        System.out.println("\n原始链表3: " + printList(head3));
        ListNode result3 = insertGreatestCommonDivisors(head3);
        System.out.println("插入GCD后链表3: " + printList(result3));
        
        // 测试用例4: [3,1]
        ListNode head4 = buildList(new int[]{3, 1});
        System.out.println("\n原始链表4: " + printList(head4));
        ListNode result4 = insertGreatestCommonDivisors(head4);
        System.out.println("插入GCD后链表4: " + printList(result4));
    }
}
package class034;

// 排序链表 - LeetCode 148
// 测试链接: https://leetcode.cn/problems/sort-list/
public class Code36_SortList {

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

    // 提交如下的方法 - 归并排序（自底向上）
    public static ListNode sortList(ListNode head) {
        if (head == null || head.next == null) {
            return head; // 空链表或单节点链表已经有序
        }
        
        // 计算链表长度
        int length = getLength(head);
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        
        // 自底向上归并排序
        // 步长从1开始，每次翻倍
        for (int step = 1; step < length; step *= 2) {
            ListNode prev = dummy;
            ListNode curr = dummy.next;
            
            while (curr != null) {
                // 找到第一个子链表的头
                ListNode head1 = curr;
                // 找到第一个子链表的尾
                for (int i = 1; i < step && curr.next != null; i++) {
                    curr = curr.next;
                }
                
                // 找到第二个子链表的头
                ListNode head2 = curr.next;
                // 断开第一个子链表
                curr.next = null;
                curr = head2;
                
                // 找到第二个子链表的尾
                for (int i = 1; i < step && curr != null && curr.next != null; i++) {
                    curr = curr.next;
                }
                
                // 保存下一个子链表的头
                ListNode next = null;
                if (curr != null) {
                    next = curr.next;
                    curr.next = null; // 断开第二个子链表
                }
                
                // 合并两个有序子链表
                ListNode merged = merge(head1, head2);
                // 将合并后的链表连接到结果中
                prev.next = merged;
                // 移动prev到合并后链表的尾部
                while (prev.next != null) {
                    prev = prev.next;
                }
                
                // 处理下一对子链表
                curr = next;
            }
        }
        
        return dummy.next;
    }
    
    // 辅助方法：合并两个有序链表
    private static ListNode merge(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                curr.next = l1;
                l1 = l1.next;
            } else {
                curr.next = l2;
                l2 = l2.next;
            }
            curr = curr.next;
        }
        
        // 连接剩余节点
        curr.next = (l1 != null) ? l1 : l2;
        
        return dummy.next;
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
    
    // 方法2：归并排序（递归版）
    public static ListNode sortListRecursive(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        
        // 使用快慢指针找到链表中点
        ListNode slow = head;
        ListNode fast = head.next;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        
        // 分割链表
        ListNode mid = slow.next;
        slow.next = null;
        
        // 递归排序两个子链表
        ListNode left = sortListRecursive(head);
        ListNode right = sortListRecursive(mid);
        
        // 合并两个有序链表
        return merge(left, right);
    }
    
    /*
     * 题目扩展：LeetCode 148. 排序链表
     * 来源：LeetCode、LintCode、牛客网
     * 
     * 题目描述：
     * 给你链表的头结点 head ，请将其按 升序 排列并返回 排序后的链表 。
     * 进阶：
     * 你可以在 O(n log n) 时间复杂度和常数级空间复杂度下，对链表进行排序吗？
     * 
     * 解题思路（自底向上归并排序）：
     * 1. 计算链表长度
     * 2. 步长从1开始，每次翻倍
     * 3. 对于每个步长，将链表分成若干长度为步长的子链表，两两合并
     * 4. 直到步长大于等于链表长度
     * 
     * 时间复杂度：O(n log n) - 符合排序算法的下界
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 
     * 解题思路（递归归并排序）：
     * 1. 找到链表中点，分割成两个子链表
     * 2. 递归排序两个子链表
     * 3. 合并两个有序子链表
     * 
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(log n) - 递归调用栈的深度
     * 
     * 最优解：自底向上归并排序，满足O(n log n)时间和O(1)空间的要求
     * 
     * 工程化考量：
     * 1. 边界情况处理：空链表、单节点链表
     * 2. 异常处理：确保指针操作的安全性
     * 3. 代码可读性：归并排序的实现逻辑清晰
     * 4. 性能优化：自底向上方法避免了递归调用栈的开销
     * 
     * 与机器学习等领域的联系：
     * 1. 归并排序是稳定的排序算法，在数据敏感性要求高的场景很有用
     * 2. 在分布式系统中，归并排序是外部排序的基础
     * 3. 在大数据处理中，归并排序的分治思想被广泛应用
     * 
     * 语言特性差异：
     * Java: 注意递归深度可能导致栈溢出
     * C++: 可以利用指针操作的优势
     * Python: 递归深度限制更严格，自底向上方法更适合
     * 
     * 极端输入场景：
     * 1. 空链表：返回null
     * 2. 单节点链表：直接返回
     * 3. 已经有序的链表
     * 4. 逆序的链表
     * 5. 包含重复值的链表
     * 6. 非常长的链表（递归版可能导致栈溢出）
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
        // 测试用例1: [4,2,1,3]
        ListNode head1 = buildList(new int[]{4, 2, 1, 3});
        System.out.println("原始链表1: " + printList(head1));
        ListNode result1 = sortList(head1);
        System.out.println("自底向上归并排序后: " + printList(result1));
        
        // 测试用例2: [-1,5,3,4,0]
        ListNode head2 = buildList(new int[]{-1, 5, 3, 4, 0});
        System.out.println("\n原始链表2: " + printList(head2));
        ListNode result2 = sortListRecursive(head2);
        System.out.println("递归归并排序后: " + printList(result2));
        
        // 测试用例3: []
        ListNode head3 = null;
        System.out.println("\n原始链表3: " + printList(head3));
        ListNode result3 = sortList(head3);
        System.out.println("自底向上归并排序后: " + printList(result3));
        
        // 测试用例4: [1]
        ListNode head4 = new ListNode(1);
        System.out.println("\n原始链表4: " + printList(head4));
        ListNode result4 = sortList(head4);
        System.out.println("自底向上归并排序后: " + printList(result4));
        
        // 测试用例5: [5,4,3,2,1]
        ListNode head5 = buildList(new int[]{5, 4, 3, 2, 1});
        System.out.println("\n原始链表5: " + printList(head5));
        ListNode result5 = sortList(head5);
        System.out.println("自底向上归并排序后: " + printList(result5));
    }
}
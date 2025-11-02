package class034;

// 删除排序链表中的重复元素 II - LeetCode 82
// 测试链接: https://leetcode.cn/problems/remove-duplicates-from-sorted-list-ii/
public class Code24_RemoveDuplicatesFromSortedListII {

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
    public static ListNode deleteDuplicates(ListNode head) {
        // 创建虚拟头节点，简化边界情况处理
        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        
        // prev指向当前已处理的最后一个不重复节点
        ListNode prev = dummy;
        // curr用于遍历链表
        ListNode curr = head;
        
        while (curr != null) {
            // 标记当前节点是否是重复节点
            boolean isDuplicate = false;
            
            // 检查当前节点是否有重复
            while (curr.next != null && curr.val == curr.next.val) {
                isDuplicate = true;
                curr = curr.next;
            }
            
            if (isDuplicate) {
                // 如果是重复节点，跳过当前节点
                prev.next = curr.next;
            } else {
                // 如果不是重复节点，更新prev
                prev = curr;
            }
            
            // 移动到下一个节点
            curr = curr.next;
        }
        
        return dummy.next;
    }
    
    /*
     * 题目扩展：LeetCode 82. 删除排序链表中的重复元素 II
     * 来源：LeetCode、LintCode、牛客网
     * 
     * 题目描述：
     * 给定一个已排序的链表的头 head ， 删除原始链表中所有重复数字的节点，
     * 只留下不同的数字 。返回 已排序的链表 。
     * 
     * 解题思路：
     * 1. 使用虚拟头节点简化头节点可能被删除的情况
     * 2. 使用prev指针跟踪上一个不重复的节点
     * 3. 遍历链表，对于每个节点：
     *    a. 检查是否有重复值
     *    b. 如果有重复，跳过所有重复节点
     *    c. 如果没有重复，将当前节点加入结果链表
     * 
     * 时间复杂度：O(n) - 每个节点只被访问一次
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：是，一次遍历即可完成
     * 
     * 工程化考量：
     * 1. 边界情况处理：空链表、单节点链表、全部重复节点
     * 2. 异常处理：输入参数校验
     * 3. 代码可读性：变量命名清晰，逻辑结构明确
     * 
     * 与机器学习等领域的联系：
     * 1. 在数据清洗中，常需要去除重复数据
     * 2. 类似于特征选择中的去重操作
     * 
     * 语言特性差异：
     * Java: 使用虚拟头节点简化逻辑
     * C++: 需要注意内存释放问题
     * Python: 实现方式类似，但语法更简洁
     * 
     * 极端输入场景：
     * 1. 空链表：返回null
     * 2. 单节点链表：直接返回
     * 3. 所有节点值都相同：返回空链表
     * 4. 没有重复节点：返回原链表
     * 5. 大量重复节点
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
        // 测试用例1: [1,2,3,3,4,4,5]
        ListNode head1 = buildList(new int[]{1, 2, 3, 3, 4, 4, 5});
        System.out.println("原始链表1: " + printList(head1));
        ListNode result1 = deleteDuplicates(head1);
        System.out.println("处理后链表1: " + printList(result1));
        
        // 测试用例2: [1,1,1,2,3]
        ListNode head2 = buildList(new int[]{1, 1, 1, 2, 3});
        System.out.println("\n原始链表2: " + printList(head2));
        ListNode result2 = deleteDuplicates(head2);
        System.out.println("处理后链表2: " + printList(result2));
        
        // 测试用例3: 空链表
        ListNode head3 = null;
        System.out.println("\n原始链表3: null");
        ListNode result3 = deleteDuplicates(head3);
        System.out.println("处理后链表3: " + (result3 == null ? "null" : printList(result3)));
        
        // 测试用例4: 单节点链表 [1]
        ListNode head4 = new ListNode(1);
        System.out.println("\n原始链表4: " + printList(head4));
        ListNode result4 = deleteDuplicates(head4);
        System.out.println("处理后链表4: " + printList(result4));
    }
}
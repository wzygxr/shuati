package class034;

// 分隔链表 - LeetCode 86
// 测试链接: https://leetcode.cn/problems/partition-list/
public class Code38_PartitionList {

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
    public static ListNode partition(ListNode head, int x) {
        // 创建两个哑节点，分别用于小于x和大于等于x的链表
        ListNode dummyLess = new ListNode(0);
        ListNode dummyGreater = new ListNode(0);
        
        // 用于遍历的指针
        ListNode less = dummyLess;
        ListNode greater = dummyGreater;
        ListNode curr = head;
        
        // 遍历原链表，将节点分配到两个子链表中
        while (curr != null) {
            if (curr.val < x) {
                less.next = curr;
                less = less.next;
            } else {
                greater.next = curr;
                greater = greater.next;
            }
            curr = curr.next;
        }
        
        // 确保大于等于x的链表尾部指向null
        greater.next = null;
        
        // 连接两个子链表
        less.next = dummyGreater.next;
        
        return dummyLess.next;
    }
    
    // 方法2：使用ArrayList辅助（可读性更好但空间复杂度更高）
    public static ListNode partitionWithList(ListNode head, int x) {
        // 创建两个列表分别存储小于x和大于等于x的节点
        java.util.ArrayList<ListNode> lessList = new java.util.ArrayList<>();
        java.util.ArrayList<ListNode> greaterList = new java.util.ArrayList<>();
        
        // 遍历原链表，将节点添加到相应的列表中
        ListNode curr = head;
        while (curr != null) {
            if (curr.val < x) {
                lessList.add(curr);
            } else {
                greaterList.add(curr);
            }
            curr = curr.next;
        }
        
        // 构建新的链表
        ListNode dummy = new ListNode(0);
        curr = dummy;
        
        // 添加小于x的节点
        for (ListNode node : lessList) {
            curr.next = node;
            curr = curr.next;
        }
        
        // 添加大于等于x的节点
        for (ListNode node : greaterList) {
            curr.next = node;
            curr = curr.next;
        }
        
        // 确保链表尾部指向null
        curr.next = null;
        
        return dummy.next;
    }
    
    /*
     * 题目扩展：LeetCode 86. 分隔链表
     * 来源：LeetCode、LintCode、牛客网
     * 
     * 题目描述：
     * 给你一个链表的头节点 head 和一个特定值 x ，请你对链表进行分隔，使得所有 小于 x 的节点都出现在 大于或等于 x 的节点之前。
     * 你应当 保留 两个分区中每个节点的初始相对位置。
     * 
     * 解题思路：
     * 1. 创建两个哑节点，分别用于构建小于x和大于等于x的子链表
     * 2. 遍历原链表，根据节点值将其分配到对应的子链表中
     * 3. 确保大于等于x的子链表尾部指向null，避免形成环
     * 4. 连接两个子链表，小于x的子链表在前，大于等于x的子链表在后
     * 5. 返回新链表的头节点
     * 
     * 时间复杂度：O(n) - 需要遍历链表一次
     * 空间复杂度：O(1) - 只使用常数额外空间（不考虑新创建的哑节点）
     * 
     * 最优解：此解法已经是最优解
     * 
     * 工程化考量：
     * 1. 边界情况处理：空链表、单节点链表
     * 2. 异常处理：确保指针操作的安全性
     * 3. 代码可读性：逻辑清晰，注释充分
     * 4. 性能优化：一次遍历完成所有操作
     * 5. 避免环的形成：确保最后一个节点的next为null
     * 
     * 与机器学习等领域的联系：
     * 1. 在数据预处理中，类似的分区操作常用于特征处理
     * 2. 在数据库查询中，条件过滤与此有相似之处
     * 3. 链表操作在数据流处理中很常见
     * 
     * 语言特性差异：
     * Java: 注意空指针检查和对象引用的正确管理
     * C++: 可以利用指针操作的优势
     * Python: 可以使用更简洁的方式处理链表
     * 
     * 极端输入场景：
     * 1. 空链表：返回null
     * 2. 单节点链表：直接返回
     * 3. 所有节点都小于x：返回原链表
     * 4. 所有节点都大于等于x：返回原链表
     * 5. 链表中存在等于x的节点：确保它们在右侧子链表中
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
        // 测试用例1: head = [1,4,3,2,5,2], x = 3
        ListNode head1 = buildList(new int[]{1, 4, 3, 2, 5, 2});
        System.out.println("原始链表1: " + printList(head1));
        ListNode result1 = partition(head1, 3);
        System.out.println("分隔后链表1: " + printList(result1));
        
        // 测试用例2: head = [2,1], x = 2
        ListNode head2 = buildList(new int[]{2, 1});
        System.out.println("\n原始链表2: " + printList(head2));
        ListNode result2 = partition(head2, 2);
        System.out.println("分隔后链表2: " + printList(result2));
        
        // 测试用例3: head = [], x = 0
        ListNode head3 = null;
        System.out.println("\n原始链表3: " + printList(head3));
        ListNode result3 = partition(head3, 0);
        System.out.println("分隔后链表3: " + printList(result3));
        
        // 测试用例4: head = [3,3,3], x = 3
        ListNode head4 = buildList(new int[]{3, 3, 3});
        System.out.println("\n原始链表4: " + printList(head4));
        ListNode result4 = partitionWithList(head4, 3);
        System.out.println("分隔后链表4: " + printList(result4));
        
        // 测试用例5: head = [1,2,3,4,5], x = 6
        ListNode head5 = buildList(new int[]{1, 2, 3, 4, 5});
        System.out.println("\n原始链表5: " + printList(head5));
        ListNode result5 = partition(head5, 6);
        System.out.println("分隔后链表5: " + printList(result5));
    }
}
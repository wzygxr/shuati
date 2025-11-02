package class034;

import java.util.HashMap;
import java.util.Map;

// 删除链表中的零和连续节点 - LeetCode 1171
// 测试链接: https://leetcode.cn/problems/remove-zero-sum-consecutive-nodes-from-linked-list/
public class Code27_RemoveZeroSumConsecutiveNodesFromLinkedList {

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
    public static ListNode removeZeroSumSublists(ListNode head) {
        // 创建虚拟头节点
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        
        // 使用哈希表记录前缀和及其对应的节点
        Map<Integer, ListNode> prefixSumMap = new HashMap<>();
        int prefixSum = 0;
        
        // 第一次遍历，记录所有前缀和
        for (ListNode curr = dummy; curr != null; curr = curr.next) {
            prefixSum += curr.val;
            prefixSumMap.put(prefixSum, curr);
        }
        
        // 重置前缀和，第二次遍历删除零和子链表
        prefixSum = 0;
        for (ListNode curr = dummy; curr != null; curr = curr.next) {
            prefixSum += curr.val;
            // 将当前节点的next指向相同前缀和的下一个节点，跳过中间的零和节点
            curr.next = prefixSumMap.get(prefixSum).next;
        }
        
        return dummy.next;
    }
    
    /*
     * 题目扩展：LeetCode 1171. 删除链表中的零和连续节点
     * 来源：LeetCode、LintCode
     * 
     * 题目描述：
     * 给你一个链表的头节点 head，请你编写代码，反复删去链表中由 总和 值为 0 的连续节点组成的序列，
     * 直到不存在这样的序列为止。删除完毕后，请你返回最终结果链表的头节点。
     * 
     * 解题思路：
     * 1. 使用前缀和的思想：如果两个节点的前缀和相同，说明这两个节点之间的所有节点之和为0
     * 2. 使用哈希表记录前缀和及其对应的最后一个节点
     * 3. 第一次遍历：计算前缀和并记录到哈希表中
     * 4. 第二次遍历：对于每个节点，将其next指针直接连接到相同前缀和的下一个节点
     * 
     * 时间复杂度：O(n) - 需要遍历链表两次
     * 空间复杂度：O(n) - 最坏情况下需要存储n个前缀和
     * 是否最优解：是，两次遍历即可完成所有零和子链表的删除
     * 
     * 工程化考量：
     * 1. 边界情况处理：空链表、全零链表
     * 2. 异常处理：确保哈希表操作的正确性
     * 3. 代码可读性：算法思路清晰，变量命名明确
     * 
     * 与机器学习等领域的联系：
     * 1. 前缀和技术在时间序列分析中有广泛应用
     * 2. 类似于特征选择中的零相关性特征去除
     * 
     * 语言特性差异：
     * Java: 使用HashMap存储前缀和映射
     * C++: 使用unordered_map，性能可能略好
     * Python: 使用字典，语法更简洁
     * 
     * 极端输入场景：
     * 1. 空链表：返回null
     * 2. 全零链表：返回空链表
     * 3. 多个重叠的零和子链表
     * 4. 链表开头或结尾有零和子链表
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
        // 测试用例1: [1,2,-3,3,1]
        ListNode head1 = buildList(new int[]{1, 2, -3, 3, 1});
        System.out.println("原始链表1: " + printList(head1));
        ListNode result1 = removeZeroSumSublists(head1);
        System.out.println("处理后链表1: " + printList(result1));
        
        // 测试用例2: [1,2,3,-3,4]
        ListNode head2 = buildList(new int[]{1, 2, 3, -3, 4});
        System.out.println("\n原始链表2: " + printList(head2));
        ListNode result2 = removeZeroSumSublists(head2);
        System.out.println("处理后链表2: " + printList(result2));
        
        // 测试用例3: [1,2,3,-3,-2]
        ListNode head3 = buildList(new int[]{1, 2, 3, -3, -2});
        System.out.println("\n原始链表3: " + printList(head3));
        ListNode result3 = removeZeroSumSublists(head3);
        System.out.println("处理后链表3: " + printList(result3));
        
        // 测试用例4: [0,0]
        ListNode head4 = buildList(new int[]{0, 0});
        System.out.println("\n原始链表4: " + printList(head4));
        ListNode result4 = removeZeroSumSublists(head4);
        System.out.println("处理后链表4: " + (result4 == null ? "null" : printList(result4)));
    }
}
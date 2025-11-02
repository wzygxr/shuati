package class034;

import java.util.Arrays;

// 分割链表 - LeetCode 725
// 测试链接: https://leetcode.cn/problems/split-linked-list-in-parts/
public class Code23_SplitLinkedListInParts {

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
    public static ListNode[] splitListToParts(ListNode head, int k) {
        // 1. 计算链表的总长度
        int len = 0;
        ListNode cur = head;
        while (cur != null) {
            len++;
            cur = cur.next;
        }
        
        // 2. 计算每个部分的基本长度和余数
        // baseSize: 每个部分至少有的节点数
        // extra: 需要额外分配1个节点的部分数量
        int baseSize = len / k;
        int extra = len % k;
        
        // 3. 创建结果数组
        ListNode[] ans = new ListNode[k];
        cur = head;
        
        // 4. 分割链表
        for (int i = 0; i < k && cur != null; i++) {
            ans[i] = cur; // 记录当前部分的头节点
            
            // 计算当前部分应该有多少节点
            int partSize = baseSize + (i < extra ? 1 : 0);
            
            // 移动到当前部分的最后一个节点
            for (int j = 0; j < partSize - 1; j++) {
                cur = cur.next;
            }
            
            // 断开链表
            ListNode next = cur.next;
            cur.next = null;
            cur = next;
        }
        
        return ans;
    }
    
    /*
     * 题目扩展：LeetCode 725. 分割链表
     * 来源：LeetCode
     * 
     * 题目描述：
     * 给你一个头节点为 head 的单链表和一个整数 k ，请你设计一个算法将链表分隔为 k 个连续的部分。
     * 每部分的长度应该尽可能的相等：任意两部分的长度差距不能超过 1 。这可能会导致有些部分为 null 。
     * 这 k 个部分应该按照在链表中出现的顺序排列，并且排在前面的部分的长度应该大于或等于排在后面的长度。
     * 
     * 解题思路：
     * 1. 首先计算链表的总长度 len
     * 2. 计算每个部分的基本长度 baseSize = len / k 和余数 extra = len % k
     * 3. 前 extra 个部分每个有 baseSize + 1 个节点，后面的部分每个有 baseSize 个节点
     * 4. 遍历链表，按上述规则分割并断开
     * 
     * 时间复杂度：O(n) - 需要遍历链表两次，第一次计算长度，第二次分割
     * 空间复杂度：O(k) - 返回的结果数组大小为 k
     * 是否最优解：是，无法再优化时间复杂度
     * 
     * 工程化考量：
     * 1. 边界情况处理：k大于链表长度时，后面的部分将为null
     * 2. 异常处理：空链表、k为0等情况
     * 3. 代码可读性：变量命名清晰，逻辑结构明确
     * 
     * 与机器学习等领域的联系：
     * 1. 在数据预处理中，经常需要将数据集分割成大小相近的批次
     * 2. 类似于并行计算中的数据分片策略
     * 
     * 语言特性差异：
     * Java: 使用数组存储结果，自动处理null值
     * C++: 需要动态分配内存，并注意内存管理
     * Python: 可以使用列表存储结果，处理更灵活
     * 
     * 极端输入场景：
     * 1. 空链表：返回全为null的数组
     * 2. k=1：返回整个链表作为唯一元素
     * 3. k>len：前len个部分各有1个节点，后面的为null
     * 4. len是k的倍数：每个部分大小相等
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
        // 测试用例1: [1,2,3], k=5
        ListNode head1 = buildList(new int[]{1, 2, 3});
        ListNode[] result1 = splitListToParts(head1, 5);
        System.out.println("测试用例1:");
        for (int i = 0; i < result1.length; i++) {
            System.out.println("部分 " + i + ": " + printList(result1[i]));
        }
        
        // 测试用例2: [1,2,3,4,5,6,7,8,9,10], k=3
        ListNode head2 = buildList(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        ListNode[] result2 = splitListToParts(head2, 3);
        System.out.println("\n测试用例2:");
        for (int i = 0; i < result2.length; i++) {
            System.out.println("部分 " + i + ": " + printList(result2[i]));
        }
    }
}
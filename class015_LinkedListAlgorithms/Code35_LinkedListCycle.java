package class034;

import java.util.HashSet;
import java.util.Set;

// 环形链表 - LeetCode 141
// 测试链接: https://leetcode.cn/problems/linked-list-cycle/
public class Code35_LinkedListCycle {

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

    // 提交如下的方法 - 双指针（快慢指针）法
    public static boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) {
            return false; // 空链表或单节点链表无环
        }
        
        ListNode slow = head;      // 慢指针
        ListNode fast = head.next; // 快指针
        
        while (slow != fast) {
            // 如果快指针到达链表末尾，说明无环
            if (fast == null || fast.next == null) {
                return false;
            }
            
            slow = slow.next;      // 慢指针每次走1步
            fast = fast.next.next; // 快指针每次走2步
        }
        
        // 如果快慢指针相遇，说明有环
        return true;
    }
    
    // 方法2：哈希表法
    public static boolean hasCycleHash(ListNode head) {
        Set<ListNode> visited = new HashSet<>();
        ListNode curr = head;
        
        while (curr != null) {
            // 如果当前节点已经在集合中，说明有环
            if (visited.contains(curr)) {
                return true;
            }
            // 将当前节点加入集合
            visited.add(curr);
            curr = curr.next;
        }
        
        return false; // 遍历完整个链表，没有环
    }
    
    // 方法3：Floyd's Cycle-Finding Algorithm (另一种快慢指针实现)
    public static boolean hasCycleFloyd(ListNode head) {
        if (head == null) {
            return false;
        }
        
        ListNode tortoise = head; // 龟（慢指针）
        ListNode hare = head;     // 兔（快指针）
        
        while (hare != null && hare.next != null) {
            tortoise = tortoise.next;      // 龟每次走1步
            hare = hare.next.next;         // 兔每次走2步
            
            if (tortoise == hare) {        // 相遇，说明有环
                return true;
            }
        }
        
        return false; // 没有相遇，无环
    }
    
    /*
     * 题目扩展：LeetCode 141. 环形链表
     * 来源：LeetCode、LintCode、剑指Offer、牛客网
     * 
     * 题目描述：
     * 给你一个链表的头节点 head ，判断链表中是否有环。
     * 如果链表中有某个节点，可以通过连续跟踪 next 指针再次到达，则链表中存在环。
     * 为了表示给定链表中的环，评测系统内部使用整数 pos 来表示链表尾连接到链表中的位置（索引从 0 开始）。
     * 如果 pos 是 -1，则在该链表中没有环。注意：pos 不作为参数进行传递，仅仅是为了标识链表的实际情况。
     * 如果链表中存在环，则返回 true 。 否则，返回 false 。
     * 
     * 解题思路（快慢指针法）：
     * 1. 使用两个指针，慢指针每次移动1步，快指针每次移动2步
     * 2. 如果链表中有环，两个指针最终会在环内相遇
     * 3. 如果快指针到达链表末尾（fast == null 或 fast.next == null），说明链表无环
     * 
     * 为什么快慢指针一定能相遇？
     * - 假设链表有环，快慢指针都会进入环中
     * - 每移动一次，快指针与慢指针的距离增加1
     * - 由于快指针比慢指针每次多走1步，它们之间的距离会以每次1步的速度减小
     * - 最终两者会相遇（距离变为0）
     * 
     * 时间复杂度：O(n)
     * - 无环情况：O(n)，快指针会遍历整个链表
     * - 有环情况：O(k + λ)，k是环前节点数，λ是环的长度
     * 
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 
     * 解题思路（哈希表法）：
     * 1. 使用哈希表记录已访问过的节点
     * 2. 遍历链表，检查当前节点是否已在哈希表中
     * 3. 如果存在，说明有环；否则将其加入哈希表
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n) - 需要存储已访问节点
     * 
     * 最优解：快慢指针法，空间复杂度最优
     * 
     * 工程化考量：
     * 1. 边界情况处理：空链表、单节点链表
     * 2. 异常处理：确保指针操作的安全性
     * 3. 代码可读性：算法逻辑清晰
     * 4. 性能优化：快慢指针法避免了额外的空间开销
     * 
     * 与机器学习等领域的联系：
     * 1. 在图算法中，环检测是基础问题
     * 2. 在数据处理中，避免循环依赖与此类似
     * 3. 在随机算法中，Floyd's算法是重要的工具
     * 
     * 语言特性差异：
     * Java: 使用==比较对象引用
     * C++: 直接比较指针地址
     * Python: 比较对象的id()
     * 
     * 极端输入场景：
     * 1. 空链表：返回false
     * 2. 单节点链表：返回false
     * 3. 单节点自环：返回true
     * 4. 非常长的无环链表
     * 5. 环非常小但链表很长
     */
    
    // 辅助方法：创建带环的链表用于测试
    public static ListNode createLinkedListWithCycle(int[] nums, int pos) {
        if (nums == null || nums.length == 0) {
            return null;
        }
        
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        ListNode cycleEntry = null;
        
        for (int i = 0; i < nums.length; i++) {
            curr.next = new ListNode(nums[i]);
            curr = curr.next;
            if (i == pos) {
                cycleEntry = curr;
            }
        }
        
        // 连接成环
        if (pos >= 0) {
            curr.next = cycleEntry;
        }
        
        return dummy.next;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1: [3,2,0,-4], pos = 1 - 有环
        ListNode head1 = createLinkedListWithCycle(new int[]{3, 2, 0, -4}, 1);
        System.out.println("测试用例1 - 快慢指针法: " + hasCycle(head1));
        
        // 重置链表进行测试，注意：由于有环，不能重复使用同一个链表进行多次哈希表测试
        ListNode head1Hash = createLinkedListWithCycle(new int[]{3, 2, 0, -4}, 1);
        System.out.println("测试用例1 - 哈希表法: " + hasCycleHash(head1Hash));
        
        ListNode head1Floyd = createLinkedListWithCycle(new int[]{3, 2, 0, -4}, 1);
        System.out.println("测试用例1 - Floyd算法: " + hasCycleFloyd(head1Floyd));
        
        // 测试用例2: [1,2], pos = 0 - 有环
        ListNode head2 = createLinkedListWithCycle(new int[]{1, 2}, 0);
        System.out.println("\n测试用例2 - 快慢指针法: " + hasCycle(head2));
        
        // 测试用例3: [1], pos = -1 - 无环
        ListNode head3 = createLinkedListWithCycle(new int[]{1}, -1);
        System.out.println("\n测试用例3 - 快慢指针法: " + hasCycle(head3));
        System.out.println("测试用例3 - 哈希表法: " + hasCycleHash(head3));
        
        // 测试用例4: [], pos = -1 - 无环
        ListNode head4 = null;
        System.out.println("\n测试用例4 - 快慢指针法: " + hasCycle(head4));
        
        // 测试用例5: [1], pos = 0 - 有环（单节点自环）
        ListNode head5 = createLinkedListWithCycle(new int[]{1}, 0);
        System.out.println("\n测试用例5 - 快慢指针法: " + hasCycle(head5));
    }
}
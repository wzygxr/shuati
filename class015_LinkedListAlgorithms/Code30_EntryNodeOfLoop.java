package class034;

// 链表中环的入口节点 - 剑指Offer
// 测试链接: https://leetcode.cn/problems/linked-list-cycle-ii/
public class Code30_EntryNodeOfLoop {

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
    public static ListNode detectCycle(ListNode head) {
        if (head == null || head.next == null) {
            return null; // 空链表或单节点链表无环
        }
        
        // 阶段1：检测是否有环并找到相遇点
        ListNode slow = head;
        ListNode fast = head;
        boolean hasCycle = false;
        
        while (fast != null && fast.next != null) {
            slow = slow.next;          // 慢指针每次走1步
            fast = fast.next.next;     // 快指针每次走2步
            
            if (slow == fast) {        // 相遇，说明有环
                hasCycle = true;
                break;
            }
        }
        
        // 如果没有环，直接返回null
        if (!hasCycle) {
            return null;
        }
        
        // 阶段2：找到环的入口
        // 理论基础：相遇点到环入口的距离等于头节点到环入口的距离
        ListNode ptr1 = head;
        ListNode ptr2 = slow; // 相遇点
        
        while (ptr1 != ptr2) {
            ptr1 = ptr1.next;
            ptr2 = ptr2.next;
        }
        
        return ptr1; // 返回环的入口
    }
    
    // 方法2：哈希表法
    public static ListNode detectCycleHash(ListNode head) {
        java.util.HashSet<ListNode> visited = new java.util.HashSet<>();
        ListNode curr = head;
        
        while (curr != null) {
            if (visited.contains(curr)) {
                return curr; // 找到环的入口
            }
            visited.add(curr);
            curr = curr.next;
        }
        
        return null; // 无环
    }
    
    /*
     * 题目扩展：剑指Offer - 链表中环的入口节点
     * 来源：剑指Offer、LeetCode 142
     * 
     * 题目描述：
     * 给一个链表，若其中包含环，请找出该链表的环的入口节点，否则，返回null。
     * 
     * 解题思路（快慢指针法）：
     * 1. 阶段1：使用快慢指针检测链表中是否有环
     *    - 慢指针每次移动1步，快指针每次移动2步
     *    - 如果有环，两个指针最终会在环内相遇
     *    - 如果快指针到达null，则无环
     * 
     * 2. 阶段2：找到环的入口节点
     *    - 设头节点到环入口的距离为a，环入口到相遇点的距离为b，相遇点到环入口的距离为c
     *    - 相遇时，慢指针走了a+b步，快指针走了a+b+n(b+c)步（n为快指针在环内多走的圈数）
     *    - 由于快指针速度是慢指针的2倍，有 2(a+b) = a+b+n(b+c)，化简得 a+b = n(b+c)
     *    - 进一步化简得 a = c+(n-1)(b+c)，即头节点到环入口的距离等于相遇点到环入口的距离加上(n-1)圈
     *    - 因此，让一个指针从头节点开始，另一个指针从相遇点开始，两者每次都走1步，最终会在环入口相遇
     * 
     * 时间复杂度：O(n) - 最多遍历链表两次
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：是，空间复杂度最优
     * 
     * 工程化考量：
     * 1. 边界情况处理：空链表、单节点链表、无环链表
     * 2. 异常处理：确保指针操作的安全性
     * 3. 代码可读性：算法逻辑清晰，注释充分
     * 4. 性能优化：双指针法比哈希表法空间效率更高
     * 
     * 与机器学习等领域的联系：
     * 1. 在图算法中，环检测是基础问题
     * 2. 在数据处理中，循环依赖检测与此类似
     * 
     * 语言特性差异：
     * Java: 对象引用比较使用==
     * C++: 需要比较指针地址
     * Python: 比较节点对象的id
     * 
     * 极端输入场景：
     * 1. 空链表：返回null
     * 2. 单节点链表自环：返回头节点
     * 3. 非常长的无环链表
     * 4. 环非常小但链表很长
     * 5. 环入口就是头节点
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
        // 测试用例1: [3,2,0,-4], pos = 1 (环的入口是节点2)
        ListNode head1 = createLinkedListWithCycle(new int[]{3, 2, 0, -4}, 1);
        ListNode result1 = detectCycle(head1);
        System.out.println("测试用例1 - 环的入口值: " + (result1 != null ? result1.val : "null"));
        
        // 测试用例2: [1,2], pos = 0 (环的入口是节点1)
        ListNode head2 = createLinkedListWithCycle(new int[]{1, 2}, 0);
        ListNode result2 = detectCycle(head2);
        System.out.println("测试用例2 - 环的入口值: " + (result2 != null ? result2.val : "null"));
        
        // 测试用例3: [1], pos = -1 (无环)
        ListNode head3 = createLinkedListWithCycle(new int[]{1}, -1);
        ListNode result3 = detectCycle(head3);
        System.out.println("测试用例3 - 环的入口值: " + (result3 != null ? result3.val : "null"));
        
        // 测试哈希表方法
        System.out.println("\n哈希表方法测试:");
        // 重新创建链表进行测试
        ListNode head1Hash = createLinkedListWithCycle(new int[]{3, 2, 0, -4}, 1);
        ListNode result1Hash = detectCycleHash(head1Hash);
        System.out.println("测试用例1 - 环的入口值: " + (result1Hash != null ? result1Hash.val : "null"));
    }
}
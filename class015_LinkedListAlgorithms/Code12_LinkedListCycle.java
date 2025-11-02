package class034;

// 环形链表
// 测试链接：https://leetcode.cn/problems/linked-list-cycle/
public class Code12_LinkedListCycle {

    // 不要提交这个类
    public static class ListNode {
        int val;
        ListNode next;
        ListNode(int x) {
            val = x;
            next = null;
        }
    }

    /**
     * 判断链表是否有环
     * @param head 链表头节点
     * @return 是否有环
     * 
     * 解题思路：
     * 1. 使用快慢指针技巧（Floyd判圈算法）
     * 2. 快指针每次移动两步，慢指针每次移动一步
     * 3. 如果有环，快指针最终会追上慢指针
     * 4. 如果无环，快指针会先到达末尾
     * 
     * 时间复杂度：O(n) - 最多遍历链表两次
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：是
     */
    public static boolean hasCycle(ListNode head) {
        // 边界情况：空链表或只有一个节点
        if (head == null || head.next == null) {
            return false;
        }
        
        // 初始化快慢指针
        ListNode slow = head;
        ListNode fast = head.next;
        
        // 快指针每次移动两步，慢指针每次移动一步
        while (slow != fast) {
            // 如果快指针到达末尾，说明无环
            if (fast == null || fast.next == null) {
                return false;
            }
            slow = slow.next;
            fast = fast.next.next;
        }
        
        // 快慢指针相遇，说明有环
        return true;
    }
    
    /*
     * 题目扩展：LeetCode 141. 环形链表
     * 来源：LeetCode、牛客网、剑指Offer等各大算法平台
     * 
     * 题目描述：
     * 给你一个链表的头节点 head ，判断链表中是否有环。
     * 如果链表中有某个节点，可以通过连续跟踪 next 指针再次到达，则链表中存在环。
     * 
     * 解题思路：
     * 1. 使用快慢指针技巧（Floyd判圈算法）
     * 2. 快指针每次移动两步，慢指针每次移动一步
     * 3. 如果有环，快指针最终会追上慢指针
     * 4. 如果无环，快指针会先到达末尾
     * 
     * 时间复杂度：O(n) - 最多遍历链表两次
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：是
     * 
     * 工程化考量：
     * 1. 边界情况处理：空链表、单节点链表
     * 2. 循环条件：注意检查fast和fast.next是否为空
     * 3. 初始化：快指针和慢指针初始位置不同
     * 
     * 与机器学习等领域的联系：
     * 1. 在图论中，环检测用于检测有向图中的循环
     * 2. 在推荐系统中，用于检测用户行为的循环模式
     * 
     * 语言特性差异：
     * Java: null值检查
     * C++: 空指针检查
     * Python: None值检查
     * 
     * 极端输入场景：
     * 1. 空链表
     * 2. 单节点自环
     * 3. 大环链表
     * 4. 无环链表
     * 
     * Floyd判圈算法的特点：
     * 1. 时间复杂度低：O(n)
     * 2. 空间复杂度低：O(1)
     * 3. 数学原理：快指针和慢指针的距离每次减少1，最终会相遇
     * 4. 应用广泛：不仅用于链表，还可用于检测伪随机数生成器的周期等
     */

}
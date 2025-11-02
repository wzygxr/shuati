package class034;

// 链表环检测进阶（Floyd判圈算法详细分析）
// 测试链接：https://leetcode.cn/problems/linked-list-cycle-ii/
public class Code44_LinkedListCycleDetectionAdvanced {

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
     * 链表环检测进阶 - Floyd判圈算法（龟兔赛跑算法）
     * @param head 链表头节点
     * @return 环的入口节点，如果没有环则返回null
     * 
     * 解题思路（Floyd判圈算法）：
     * 1. 使用快慢指针，快指针每次走两步，慢指针每次走一步
     * 2. 如果存在环，快慢指针一定会相遇
     * 3. 相遇后，将其中一个指针移回头节点，两个指针以相同速度前进
     * 4. 再次相遇的节点就是环的入口节点
     * 
     * 数学原理：
     * 设头节点到环入口距离为a，环入口到相遇点距离为b，相遇点到环入口距离为c
     * 快指针路程：a + n(b+c) + b = a + (n+1)b + nc
     * 慢指针路程：a + b
     * 快指针路程 = 2 * 慢指针路程
     * a + (n+1)b + nc = 2(a + b)
     * 化简得：a = (n-1)(b+c) + c
     * 说明从头节点到环入口的距离等于相遇点到环入口的距离加上n-1圈环长
     * 
     * 时间复杂度：O(n) - n 是链表节点数量
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：是
     */
    public static ListNode detectCycle(ListNode head) {
        if (head == null || head.next == null) {
            return null;
        }
        
        ListNode slow = head;
        ListNode fast = head;
        
        // 第一阶段：检测是否存在环
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            
            if (slow == fast) {
                // 第二阶段：找到环的入口
                ListNode ptr1 = head;
                ListNode ptr2 = slow;
                
                while (ptr1 != ptr2) {
                    ptr1 = ptr1.next;
                    ptr2 = ptr2.next;
                }
                
                return ptr1; // 环的入口节点
            }
        }
        
        return null; // 没有环
    }
    
    /**
     * 链表环检测（仅判断是否存在环）
     * @param head 链表头节点
     * @return 如果存在环返回true，否则返回false
     * 
     * 时间复杂度：O(n) - n 是链表节点数量
     * 空间复杂度：O(1) - 只使用常数额外空间
     */
    public static boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) {
            return false;
        }
        
        ListNode slow = head;
        ListNode fast = head;
        
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            
            if (slow == fast) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 计算环的长度
     * @param head 链表头节点
     * @return 环的长度，如果没有环返回0
     * 
     * 解题思路：
     * 1. 先找到环内的相遇点
     * 2. 固定一个指针，另一个指针移动直到再次相遇
     * 3. 移动的步数就是环的长度
     */
    public static int cycleLength(ListNode head) {
        ListNode meetingPoint = detectCycle(head);
        if (meetingPoint == null) {
            return 0;
        }
        
        ListNode current = meetingPoint.next;
        int length = 1;
        
        while (current != meetingPoint) {
            current = current.next;
            length++;
        }
        
        return length;
    }
    
    /*
     * 题目扩展：LeetCode 142. 环形链表 II
     * 来源：LeetCode、牛客网、剑指Offer等各大算法平台
     * 
     * 题目描述：
     * 给定一个链表的头节点 head ，返回链表开始入环的第一个节点。 如果链表无环，则返回 null。
     * 
     * Floyd判圈算法详细分析：
     * 
     * 第一阶段：检测环的存在
     * - 快指针每次移动两步，慢指针每次移动一步
     * - 如果存在环，快慢指针一定会相遇（快指针会追上慢指针）
     * - 如果快指针到达null，说明没有环
     * 
     * 第二阶段：找到环的入口
     * - 数学推导：a = (n-1)(b+c) + c
     * - 从头节点和相遇点同时出发，每次移动一步，相遇点就是环的入口
     * 
     * 时间复杂度分析：
     * - 最好情况：O(n) - 环在链表开头
     * - 最坏情况：O(n) - 环在链表末尾
     * - 平均情况：O(n)
     * 
     * 空间复杂度：O(1) - 只使用常数个指针
     * 
     * 工程化考量：
     * 1. 边界情况处理：空链表、单节点链表、双节点成环
     * 2. 异常处理：输入参数校验
     * 3. 性能优化：避免不必要的遍历
     * 
     * 与机器学习等领域的联系：
     * 1. 在图论中，环检测是基础算法
     * 2. 在状态机分析中，需要检测状态循环
     * 3. 在数据流分析中，检测数据循环依赖
     * 
     * 语言特性差异：
     * Java: 对象引用比较使用 ==
     * C++: 需要比较指针地址
     * Python: 比较节点对象的id
     * 
     * 极端输入场景：
     * 1. 空链表
     * 2. 单节点链表（自环）
     * 3. 双节点成环
     * 4. 大环小环混合
     * 5. 非常长的链表
     * 
     * 反直觉但关键的设计：
     * 1. 快指针走两步，慢指针走一步的设定
     * 2. 相遇后从头节点和相遇点同时出发的数学原理
     * 3. 环长度计算的巧妙方法
     * 
     * 工程选择依据：
     * 1. 数学正确性：基于严格的数学推导
     * 2. 效率最优：时间复杂度和空间复杂度都是最优
     * 3. 实现简洁：代码逻辑清晰易懂
     * 
     * 异常防御：
     * 1. 空指针检查
     * 2. 链表长度检查
     * 3. 环检测的完备性
     * 
     * 单元测试要点：
     * 1. 测试空链表
     * 2. 测试单节点自环
     * 3. 测试双节点成环
     * 4. 测试无环链表
     * 5. 测试环长度计算
     * 
     * 性能优化策略：
     * 1. 一次遍历完成环检测和入口查找
     * 2. 使用常数空间
     * 3. 避免递归调用
     * 
     * 算法安全与业务适配：
     * 1. 避免无限循环：确保算法能在有限步骤内结束
     * 2. 内存安全：处理大链表情况
     * 3. 正确性验证：数学证明算法的正确性
     * 
     * 与标准库实现的对比：
     * 1. 标准库通常不提供环检测功能
     * 2. Floyd算法是环检测的最优解
     * 3. 边界处理更加细致
     * 
     * 笔试解题效率：
     * 1. 模板化：掌握Floyd算法的通用模板
     * 2. 数学理解：理解算法背后的数学原理
     * 3. 代码简洁：实现简洁高效的算法
     * 
     * 面试深度表达：
     * 1. 解释数学原理：为什么这样能找到环入口
     * 2. 分析复杂度：时间和空间复杂度分析
     * 3. 讨论变种：环检测的其他方法
     * 4. 实际应用：环检测在工程中的应用
     * 
     * 调试技巧：
     * 1. 打印关键变量：跟踪快慢指针的位置
     * 2. 小例子验证：用简单例子验证算法正确性
     * 3. 边界测试：测试各种边界情况
     */
}
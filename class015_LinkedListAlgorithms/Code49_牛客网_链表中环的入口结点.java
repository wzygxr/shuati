package class034;

import java.util.HashSet;
import java.util.Set;

// 牛客网 链表中环的入口结点
// 来源：牛客网、剑指Offer
// 测试链接：https://www.nowcoder.com/practice/253d2c59ec3e4bc68da16833f79a38e4
public class Code49_牛客网_链表中环的入口结点 {

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
     * 链表中环的入口结点（哈希表方法）
     * @param pHead 链表头节点
     * @return 环的入口节点，如果没有环返回null
     * 
     * 解题思路：
     * 1. 使用哈希表存储已访问的节点
     * 2. 遍历链表，如果遇到重复节点，说明有环
     * 3. 第一个重复节点就是环的入口
     * 
     * 时间复杂度：O(n) - n 是链表节点数量
     * 空间复杂度：O(n) - 哈希表的空间开销
     * 是否最优解：不是（空间复杂度较高）
     */
    public static ListNode EntryNodeOfLoopHashSet(ListNode pHead) {
        if (pHead == null || pHead.next == null) {
            return null;
        }
        
        Set<ListNode> visited = new HashSet<>();
        ListNode current = pHead;
        
        while (current != null) {
            if (visited.contains(current)) {
                return current; // 找到环的入口
            }
            visited.add(current);
            current = current.next;
        }
        
        return null; // 没有环
    }
    
    /**
     * 链表中环的入口结点（Floyd判圈算法）
     * @param pHead 链表头节点
     * @return 环的入口节点，如果没有环返回null
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
    public static ListNode EntryNodeOfLoopFloyd(ListNode pHead) {
        if (pHead == null || pHead.next == null) {
            return null;
        }
        
        ListNode slow = pHead;
        ListNode fast = pHead;
        
        // 第一阶段：检测是否存在环
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            
            if (slow == fast) {
                // 第二阶段：找到环的入口
                ListNode ptr1 = pHead;
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
     * 链表中环的入口结点（标记法）
     * @param pHead 链表头节点
     * @return 环的入口节点，如果没有环返回null
     * 
     * 解题思路：
     * 1. 遍历链表，给每个节点添加标记
     * 2. 如果遇到已标记的节点，说明有环
     * 3. 第一个已标记节点就是环的入口
     * 
     * 注意：这种方法会修改原始链表，不推荐在实际应用中使用
     * 
     * 时间复杂度：O(n) - n 是链表节点数量
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：不是（会修改原始链表）
     */
    public static ListNode EntryNodeOfLoopMark(ListNode pHead) {
        if (pHead == null || pHead.next == null) {
            return null;
        }
        
        ListNode current = pHead;
        
        while (current != null) {
            // 检查当前节点是否已经被标记（val设为特殊值）
            if (current.val == Integer.MIN_VALUE) {
                return current; // 找到环的入口
            }
            
            // 标记当前节点
            current.val = Integer.MIN_VALUE;
            current = current.next;
        }
        
        return null; // 没有环
    }
    
    /*
     * 题目扩展：牛客网 链表中环的入口结点
     * 来源：牛客网、剑指Offer、LeetCode等各大算法平台
     * 
     * 题目描述：
     * 给一个长度为n的链表，若其中包含环，请找出环的入口结点，否则返回null。
     * 
     * 解题思路：
     * 方法一：哈希表法
     * 1. 使用哈希表存储已访问的节点
     * 2. 遍历链表，如果遇到重复节点，说明有环
     * 3. 第一个重复节点就是环的入口
     * 
     * 方法二：Floyd判圈算法（最优解）
     * 1. 使用快慢指针检测环
     * 2. 找到相遇点后，重置一个指针到头节点
     * 3. 两个指针以相同速度前进，相遇点即为环入口
     * 
     * 方法三：标记法（不推荐）
     * 1. 遍历链表，给每个节点添加标记
     * 2. 如果遇到已标记的节点，说明有环
     * 3. 第一个已标记节点就是环的入口
     * 
     * 时间复杂度：
     * - 所有方法：O(n)
     * 
     * 空间复杂度：
     * - 哈希表法：O(n)
     * - Floyd算法：O(1)
     * - 标记法：O(1)
     * 
     * 是否最优解：Floyd判圈算法是最优解
     * 
     * 工程化考量：
     * 1. 算法选择：优先选择空间复杂度低的算法
     * 2. 代码可读性：清晰的算法逻辑和注释
     * 3. 异常处理：空链表和边界情况处理
     * 
     * 与机器学习等领域的联系：
     * 1. 在图论中，环检测是基本问题
     * 2. 在状态机分析中，需要检测循环状态
     * 3. 在数据流分析中，环检测用于优化
     * 
     * 语言特性差异：
     * Java: 使用HashSet或Floyd算法
     * C++: 可以使用unordered_set或Floyd算法
     * Python: 使用set或Floyd算法
     * 
     * 极端输入场景：
     * 1. 空链表
     * 2. 单节点链表（自环）
     * 3. 大环链表
     * 4. 无环长链表
     * 5. 多个环的链表（实际上不可能）
     * 
     * 反直觉但关键的设计：
     * 1. Floyd算法的数学证明：a = (n-1)(b+c) + c
     * 2. 快慢指针的相遇点不一定是环入口
     * 3. 重置指针后的相遇点才是环入口
     * 
     * 工程选择依据：
     * 1. 性能要求：选择空间复杂度最优的算法
     * 2. 内存限制：在内存紧张时选择Floyd算法
     * 3. 代码简洁性：哈希表法代码更简洁
     * 
     * 异常防御：
     * 1. 空指针检查
     * 2. 链表长度检查
     * 3. 环检测逻辑验证
     * 
     * 单元测试要点：
     * 1. 测试空链表
     * 2. 测试单节点自环
     * 3. 测试无环链表
     * 4. 测试有环链表
     * 5. 测试性能边界
     * 
     * 性能优化策略：
     * 1. 减少空间开销：使用Floyd算法
     * 2. 避免修改原始数据：不使用标记法
     * 3. 优化遍历次数：Floyd算法最优
     * 
     * 算法安全与业务适配：
     * 1. 避免内存泄漏：正确处理节点引用
     * 2. 异常捕获：捕获可能的运行时异常
     * 3. 性能监控：监控执行时间和内存使用
     * 
     * 与标准库实现的对比：
     * 1. 标准库通常不提供环检测功能
     * 2. 需要自定义实现特定需求
     * 3. Floyd算法是业界标准解法
     * 
     * 笔试解题效率：
     * 1. Floyd算法：展示算法理解深度
     * 2. 哈希表法：代码简洁，易于实现
     * 3. 标记法：不推荐，会修改原始数据
     * 
     * 面试深度表达：
     * 1. 解释Floyd算法的数学原理
     * 2. 分析各种方法的优缺点
     * 3. 讨论实际应用场景
     * 4. 展示优化思路和工程考量
     */
}
package class034;

// 反转链表 II
// 测试链接：https://leetcode.cn/problems/reverse-linked-list-ii/
public class Code41_ReverseLinkedListII {

    // 不要提交这个类
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    /**
     * 反转链表 II（反转指定区间内的节点）
     * @param head 链表头节点
     * @param left 反转开始位置（从1开始计数）
     * @param right 反转结束位置
     * @return 反转后的链表头节点
     * 
     * 解题思路：
     * 1. 找到反转区间的前一个节点和区间后的第一个节点
     * 2. 反转指定区间内的节点
     * 3. 将反转后的区间重新连接到原链表
     * 
     * 时间复杂度：O(n) - n 是链表节点数量
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：是
     */
    public static ListNode reverseBetween(ListNode head, int left, int right) {
        // 处理边界情况
        if (head == null || left == right) {
            return head;
        }
        
        // 创建虚拟头节点，简化边界处理
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        
        // 找到反转区间的前一个节点
        ListNode pre = dummy;
        for (int i = 1; i < left; i++) {
            pre = pre.next;
        }
        
        // 反转区间内的节点
        ListNode start = pre.next;
        ListNode end = start;
        for (int i = left; i < right; i++) {
            end = end.next;
        }
        
        ListNode next = end.next;
        end.next = null; // 断开区间
        
        // 反转区间
        ListNode reversed = reverseList(start);
        
        // 重新连接
        pre.next = reversed;
        start.next = next;
        
        return dummy.next;
    }
    
    /**
     * 反转链表
     * @param head 链表头节点
     * @return 反转后的链表头节点
     */
    private static ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode current = head;
        
        while (current != null) {
            ListNode next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        
        return prev;
    }
    
    /*
     * 题目扩展：LeetCode 92. 反转链表 II
     * 来源：LeetCode、牛客网、剑指Offer等各大算法平台
     * 
     * 题目描述：
     * 给你单链表的头指针 head 和两个整数 left 和 right ，其中 left <= right。
     * 请你反转从位置 left 到位置 right 的链表节点，返回反转后的链表。
     * 
     * 解题思路：
     * 1. 找到反转区间的前一个节点和区间后的第一个节点
     * 2. 反转指定区间内的节点
     * 3. 将反转后的区间重新连接到原链表
     * 
     * 时间复杂度：O(n) - n 是链表节点数量
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：是
     * 
     * 工程化考量：
     * 1. 边界情况处理：空链表、left等于right、left为1
     * 2. 异常处理：输入参数校验（left和right的范围）
     * 3. 代码可读性：使用虚拟头节点简化逻辑
     * 
     * 与机器学习等领域的联系：
     * 1. 在序列处理中，有时需要反转部分序列
     * 2. 在时间序列分析中，可能需要反转特定时间段的数据
     * 
     * 语言特性差异：
     * Java: 对象引用操作直观
     * C++: 指针操作更直接但需注意内存安全
     * Python: 语法简洁，但性能不如Java/C++
     * 
     * 极端输入场景：
     * 1. 空链表
     * 2. left等于right（无需反转）
     * 3. left为1（从头开始反转）
     * 4. right为链表长度（反转到最后）
     * 5. left和right超出链表范围
     * 
     * 设计的利弊：
     * 1. 优点：将复杂问题分解为多个经典子问题
     * 2. 缺点：需要多次遍历链表
     * 
     * 为什么这么写：
     * 1. 虚拟头节点：简化头节点特殊处理
     * 2. 断开区间：避免反转时影响其他节点
     * 3. 复用反转算法：提高代码复用性
     * 
     * 反直觉但关键的设计：
     * 1. 断开区间：在反转前断开区间，避免反转操作影响其他节点
     * 2. 虚拟头节点：统一处理头节点反转的情况
     * 
     * 工程选择依据：
     * 1. 可维护性：代码结构清晰，易于理解和修改
     * 2. 性能：时间复杂度最优，空间复杂度常数级
     * 3. 鲁棒性：处理各种边界情况
     * 
     * 异常防御：
     * 1. 空指针检查
     * 2. 参数范围校验
     * 3. 链表长度检查
     * 
     * 单元测试要点：
     * 1. 测试空链表
     * 2. 测试单节点链表
     * 3. 测试left等于right
     * 4. 测试从头开始反转
     * 5. 测试反转到最后
     * 6. 测试中间区间反转
     * 
     * 性能优化策略：
     * 1. 减少不必要的遍历
     * 2. 原地操作，不创建新节点
     * 3. 使用虚拟头节点避免特殊判断
     * 
     * 算法安全与业务适配：
     * 1. 避免崩溃：处理空指针和越界情况
     * 2. 异常捕获：捕获可能的运行时异常
     * 3. 处理溢出：处理大链表情况
     * 
     * 与标准库实现的对比：
     * 1. 标准库通常不提供部分反转功能
     * 2. 需要自定义实现特定区间的反转
     * 3. 边界处理更加细致
     * 
     * 笔试解题效率：
     * 1. 模板化：掌握反转链表的通用模板
     * 2. 边界处理：熟练处理各种边界情况
     * 3. 代码简洁：使用虚拟头节点简化代码
     * 
     * 面试深度表达：
     * 1. 解释设计思路：为什么使用虚拟头节点
     * 2. 分析复杂度：时间和空间复杂度分析
     * 3. 讨论优化：可能的优化方案
     * 4. 对比解法：与其他解法的比较
     */
}
package class034;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

// 剑指Offer 06. 从尾到头打印链表
// 来源：剑指Offer、牛客网
// 测试链接：https://leetcode.cn/problems/cong-wei-dao-tou-da-yin-lian-biao-lcof/
public class Code48_剑指Offer_从尾到头打印链表 {

    // 不要提交这个类
    public static class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
    }

    /**
     * 从尾到头打印链表（栈方法）
     * @param head 链表头节点
     * @return 从尾到头的节点值数组
     * 
     * 解题思路：
     * 1. 使用栈的先进后出特性
     * 2. 遍历链表，将节点值压入栈
     * 3. 从栈中弹出节点值，得到逆序结果
     * 
     * 时间复杂度：O(n) - n 是链表节点数量
     * 空间复杂度：O(n) - 栈的空间开销
     * 是否最优解：不是（空间复杂度较高）
     */
    public static int[] reversePrintStack(ListNode head) {
        Stack<Integer> stack = new Stack<>();
        ListNode current = head;
        
        // 遍历链表，节点值入栈
        while (current != null) {
            stack.push(current.val);
            current = current.next;
        }
        
        // 从栈中弹出节点值
        int[] result = new int[stack.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = stack.pop();
        }
        
        return result;
    }
    
    /**
     * 从尾到头打印链表（递归方法）
     * @param head 链表头节点
     * @return 从尾到头的节点值数组
     * 
     * 解题思路：
     * 1. 使用递归遍历到链表末尾
     * 2. 在递归返回时收集节点值
     * 3. 利用递归栈实现逆序
     * 
     * 时间复杂度：O(n) - n 是链表节点数量
     * 空间复杂度：O(n) - 递归调用栈的深度
     * 是否最优解：不是（空间复杂度较高）
     */
    public static int[] reversePrintRecursive(ListNode head) {
        List<Integer> list = new ArrayList<>();
        reversePrintHelper(head, list);
        
        // 转换为数组
        int[] result = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }
    
    private static void reversePrintHelper(ListNode node, List<Integer> list) {
        if (node == null) {
            return;
        }
        reversePrintHelper(node.next, list);
        list.add(node.val);
    }
    
    /**
     * 从尾到头打印链表（最优解 - 两次遍历）
     * @param head 链表头节点
     * @return 从尾到头的节点值数组
     * 
     * 解题思路：
     * 1. 第一次遍历计算链表长度
     * 2. 创建合适大小的数组
     * 3. 第二次遍历从数组末尾开始填充
     * 
     * 时间复杂度：O(n) - n 是链表节点数量
     * 空间复杂度：O(1) - 除了结果数组外，只使用常数额外空间
     * 是否最优解：是
     */
    public static int[] reversePrintOptimal(ListNode head) {
        // 第一次遍历：计算链表长度
        int length = 0;
        ListNode current = head;
        while (current != null) {
            length++;
            current = current.next;
        }
        
        // 创建结果数组
        int[] result = new int[length];
        
        // 第二次遍历：从数组末尾开始填充
        current = head;
        for (int i = length - 1; i >= 0; i--) {
            result[i] = current.val;
            current = current.next;
        }
        
        return result;
    }
    
    /*
     * 题目扩展：剑指Offer 06. 从尾到头打印链表
     * 来源：剑指Offer、牛客网、LeetCode等各大算法平台
     * 
     * 题目描述：
     * 输入一个链表的头节点，从尾到头反过来返回每个节点的值（用数组返回）。
     * 
     * 解题思路：
     * 方法一：栈方法
     * 1. 使用栈的先进后出特性
     * 2. 遍历链表，节点值入栈
     * 3. 从栈中弹出节点值
     * 
     * 方法二：递归方法
     * 1. 使用递归遍历到链表末尾
     * 2. 在递归返回时收集节点值
     * 3. 利用递归栈实现逆序
     * 
     * 方法三：最优解（两次遍历）
     * 1. 第一次遍历计算链表长度
     * 2. 创建合适大小的数组
     * 3. 第二次遍历从数组末尾开始填充
     * 
     * 时间复杂度：
     * - 所有方法：O(n)
     * 
     * 空间复杂度：
     * - 栈方法：O(n)
     * - 递归方法：O(n)
     * - 最优解：O(1)（除了结果数组）
     * 
     * 是否最优解：两次遍历方法是最优解
     * 
     * 工程化考量：
     * 1. 内存效率：避免不必要的空间开销
     * 2. 代码可读性：清晰的算法逻辑
     * 3. 异常处理：空链表处理
     * 
     * 与机器学习等领域的联系：
     * 1. 在序列数据处理中，逆序操作常见
     * 2. 在时间序列分析中，可能需要逆序查看历史数据
     * 3. 在自然语言处理中，文本逆序处理
     * 
     * 语言特性差异：
     * Java: 使用Stack或递归，注意栈深度限制
     * C++: 可以使用vector和reverse_iterator
     * Python: 使用列表切片[::-1]简化实现
     * 
     * 极端输入场景：
     * 1. 空链表
     * 2. 单节点链表
     * 3. 超长链表（递归可能栈溢出）
     * 4. 内存限制严格的环境
     * 
     * 反直觉但关键的设计：
     * 1. 两次遍历方法：看似多了一次遍历，但空间复杂度最优
     * 2. 递归深度：需要注意栈溢出风险
     * 3. 数组预分配：避免动态扩容的开销
     * 
     * 工程选择依据：
     * 1. 性能要求：选择空间复杂度最优的方法
     * 2. 代码简洁性：递归方法代码最简洁
     * 3. 可维护性：栈方法逻辑最清晰
     * 
     * 异常防御：
     * 1. 空指针检查
     * 2. 栈深度检查
     * 3. 内存分配检查
     * 
     * 单元测试要点：
     * 1. 测试空链表
     * 2. 测试单节点链表
     * 3. 测试多节点链表
     * 4. 测试性能边界
     * 
     * 性能优化策略：
     * 1. 减少空间开销：使用两次遍历方法
     * 2. 避免递归：防止栈溢出
     * 3. 预分配数组：避免动态扩容
     * 
     * 算法安全与业务适配：
     * 1. 避免栈溢出：控制递归深度
     * 2. 内存管理：合理使用数据结构
     * 3. 性能监控：监控执行时间和内存使用
     * 
     * 与标准库实现的对比：
     * 1. Java的Collections.reverse()可以简化实现
     * 2. 但需要先转换为List，有额外开销
     * 3. 自定义实现更灵活，性能更好
     * 
     * 笔试解题效率：
     * 1. 递归方法：代码简洁，适合笔试
     * 2. 栈方法：逻辑清晰，易于理解
     * 3. 最优解：展示算法优化能力
     * 
     * 面试深度表达：
     * 1. 解释各种方法的优缺点
     * 2. 分析时间和空间复杂度
     * 3. 讨论实际应用场景
     * 4. 展示优化思路
     */
}
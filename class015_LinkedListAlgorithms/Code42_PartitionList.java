package class034;

// 分隔链表
// 测试链接：https://leetcode.cn/problems/partition-list/
public class Code42_PartitionList {

    // 不要提交这个类
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    /**
     * 分隔链表（根据给定值将链表分为两部分）
     * @param head 链表头节点
     * @param x 分隔值
     * @return 分隔后的链表头节点
     * 
     * 解题思路：
     * 1. 创建两个虚拟头节点，分别用于存储小于x和大于等于x的节点
     * 2. 遍历原链表，将节点分别连接到对应的链表中
     * 3. 将两个链表连接起来
     * 
     * 时间复杂度：O(n) - n 是链表节点数量
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：是
     */
    public static ListNode partition(ListNode head, int x) {
        // 创建两个虚拟头节点
        ListNode smallDummy = new ListNode(0);
        ListNode largeDummy = new ListNode(0);
        
        // 两个链表的当前节点
        ListNode smallCurr = smallDummy;
        ListNode largeCurr = largeDummy;
        
        // 遍历原链表
        ListNode curr = head;
        while (curr != null) {
            if (curr.val < x) {
                smallCurr.next = curr;
                smallCurr = smallCurr.next;
            } else {
                largeCurr.next = curr;
                largeCurr = largeCurr.next;
            }
            curr = curr.next;
        }
        
        // 连接两个链表
        smallCurr.next = largeDummy.next;
        largeCurr.next = null; // 防止链表成环
        
        return smallDummy.next;
    }
    
    /*
     * 题目扩展：LeetCode 86. 分隔链表
     * 来源：LeetCode、牛客网、剑指Offer等各大算法平台
     * 
     * 题目描述：
     * 给你一个链表的头节点 head 和一个特定值 x ，请你对链表进行分隔，
     * 使得所有小于 x 的节点都出现在大于或等于 x 的节点之前。
     * 你应当保留两个分区中每个节点的初始相对位置。
     * 
     * 解题思路：
     * 1. 创建两个虚拟头节点，分别用于存储小于x和大于等于x的节点
     * 2. 遍历原链表，将节点分别连接到对应的链表中
     * 3. 将两个链表连接起来
     * 
     * 时间复杂度：O(n) - n 是链表节点数量
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：是
     * 
     * 工程化考量：
     * 1. 边界情况处理：空链表、所有节点都小于x、所有节点都大于等于x
     * 2. 异常处理：输入参数校验
     * 3. 内存管理：避免链表成环
     * 
     * 与机器学习等领域的联系：
     * 1. 在数据预处理中，经常需要根据阈值对数据进行分隔
     * 2. 在特征工程中，可能需要根据特定值对特征进行分组
     * 
     * 语言特性差异：
     * Java: 垃圾回收自动管理内存
     * C++: 需要手动管理内存，注意避免内存泄漏
     * Python: 垃圾回收自动管理内存
     * 
     * 极端输入场景：
     * 1. 空链表
     * 2. 所有节点都小于x
     * 3. 所有节点都大于等于x
     * 4. 单节点链表
     * 5. x值超出链表节点值的范围
     * 
     * 设计的利弊：
     * 1. 优点：保持节点相对位置，时间复杂度最优
     * 2. 缺点：需要创建两个虚拟头节点
     * 
     * 为什么这么写：
     * 1. 虚拟头节点：简化边界处理
     * 2. 原地操作：不创建新节点，只改变指针指向
     * 3. 保持相对位置：按照原顺序连接节点
     * 
     * 反直觉但关键的设计：
     * 1. 最后需要将largeCurr.next设为null，防止链表成环
     * 2. 使用虚拟头节点避免对头节点的特殊处理
     * 
     * 工程选择依据：
     * 1. 可维护性：代码结构清晰，易于理解和修改
     * 2. 性能：时间复杂度最优，空间复杂度常数级
     * 3. 鲁棒性：处理各种边界情况
     * 
     * 异常防御：
     * 1. 空指针检查
     * 2. 参数范围校验
     * 3. 链表成环检查
     * 
     * 单元测试要点：
     * 1. 测试空链表
     * 2. 测试单节点链表
     * 3. 测试所有节点都小于x
     * 4. 测试所有节点都大于等于x
     * 5. 测试混合情况
     * 
     * 性能优化策略：
     * 1. 一次遍历完成分隔
     * 2. 原地操作，不创建新节点
     * 3. 使用虚拟头节点避免特殊判断
     * 
     * 算法安全与业务适配：
     * 1. 避免崩溃：处理空指针和越界情况
     * 2. 异常捕获：捕获可能的运行时异常
     * 3. 处理溢出：处理大链表情况
     * 
     * 与标准库实现的对比：
     * 1. 标准库通常不提供链表分隔功能
     * 2. 需要自定义实现特定值的分隔
     * 3. 边界处理更加细致
     * 
     * 笔试解题效率：
     * 1. 模板化：掌握链表分隔的通用模板
     * 2. 边界处理：熟练处理各种边界情况
     * 3. 代码简洁：使用虚拟头节点简化代码
     * 
     * 面试深度表达：
     * 1. 解释设计思路：为什么使用两个虚拟头节点
     * 2. 分析复杂度：时间和空间复杂度分析
     * 3. 讨论优化：可能的优化方案
     * 4. 对比解法：与其他解法的比较
     */
}
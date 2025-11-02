package class034;

// 删除链表中的节点
// 测试链接：https://www.hackerrank.com/challenges/delete-a-node-from-a-linked-list/problem
public class Code22_DeleteNode {

    // 不要提交这个类
    public static class SinglyLinkedListNode {
        public int data;
        public SinglyLinkedListNode next;

        public SinglyLinkedListNode(int nodeData) {
            this.data = nodeData;
            this.next = null;
        }
    }

    /**
     * 删除链表中的节点
     * @param head 链表头节点
     * @param position 要删除的节点位置（从0开始）
     * @return 删除节点后的链表头节点
     * 
     * 解题思路：
     * 1. 处理特殊情况：删除头节点
     * 2. 遍历链表找到要删除节点的前一个节点
     * 3. 调整指针完成删除操作
     * 
     * 时间复杂度：O(n) - n 是链表节点数量
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：是
     */
    public static SinglyLinkedListNode deleteNode(SinglyLinkedListNode head, int position) {
        // 特殊情况：删除头节点
        if (position == 0) {
            return head.next;
        }
        
        // 遍历链表找到要删除节点的前一个节点
        SinglyLinkedListNode current = head;
        for (int i = 0; i < position - 1; i++) {
            current = current.next;
        }
        
        // 调整指针完成删除操作
        current.next = current.next.next;
        
        // 返回链表头节点
        return head;
    }
    
    /*
     * 题目扩展：HackerRank - Delete a Node
     * 来源：HackerRank等各大算法平台
     * 
     * 题目描述：
     * 删除链表中给定位置的节点并返回头节点的引用。
     * 头节点位置为0。删除节点后链表可能为空。
     * 
     * 解题思路：
     * 1. 处理特殊情况：删除头节点
     * 2. 遍历链表找到要删除节点的前一个节点
     * 3. 调整指针完成删除操作
     * 
     * 时间复杂度：O(n) - n 是链表节点数量
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：是
     * 
     * 工程化考量：
     * 1. 边界情况处理：空链表、删除头节点、删除位置超出链表长度
     * 2. 代码可读性：清晰的变量命名和注释
     * 3. 异常处理：输入参数校验
     * 
     * 与机器学习等领域的联系：
     * 1. 在处理序列数据时，有时需要删除特定位置的元素
     * 2. 在动态图结构中，需要动态维护节点连接关系
     * 
     * 语言特性差异：
     * Java: 对象引用操作直观
     * C++: 指针操作更直接但需注意内存安全
     * Python: 语法简洁，但性能不如Java/C++
     * 
     * 极端输入场景：
     * 1. 空链表
     * 2. 删除位置为0
     * 3. 删除位置为链表末尾
     * 4. 删除位置超出链表长度（应抛出异常或特殊处理）
     * 
     * 设计的利弊：
     * 1. 优点：实现简单，时间复杂度合理
     * 2. 缺点：需要遍历链表找到删除位置
     * 
     * 为什么这么写：
     * 1. 特殊处理头部删除：避免额外的遍历
     * 2. 指针操作：通过调整指针跳过要删除的节点
     * 3. 原地操作：不使用额外空间存储节点
     */
}
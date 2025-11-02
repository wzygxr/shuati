// 在特定位置插入节点
// 测试链接：https://www.hackerrank.com/challenges/insert-a-node-at-a-specific-position-in-a-linked-list/problem

// 链表节点定义
struct SinglyLinkedListNode {
    int data;
    SinglyLinkedListNode* next;
    
    SinglyLinkedListNode(int nodeData) {
        this->data = nodeData;
        this->next = nullptr;
    }
};

class Code21_InsertNodeAtPosition {
public:
    /**
     * 在特定位置插入节点
     * @param head 链表头节点
     * @param data 要插入的节点数据
     * @param position 插入位置（从0开始）
     * @return 插入节点后的链表头节点
     * 
     * 解题思路：
     * 1. 处理特殊情况：在链表头部插入节点
     * 2. 遍历链表找到插入位置的前一个节点
     * 3. 调整指针完成插入操作
     * 
     * 时间复杂度：O(n) - n 是链表节点数量
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：是
     */
    static SinglyLinkedListNode* insertNodeAtPosition(SinglyLinkedListNode* head, int data, int position) {
        // 创建新节点
        SinglyLinkedListNode* newNode = new SinglyLinkedListNode(data);
        
        // 特殊情况：在链表头部插入节点
        if (position == 0) {
            newNode->next = head;
            return newNode;
        }
        
        // 遍历链表找到插入位置的前一个节点
        SinglyLinkedListNode* current = head;
        for (int i = 0; i < position - 1; i++) {
            current = current->next;
        }
        
        // 调整指针完成插入操作
        newNode->next = current->next;
        current->next = newNode;
        
        // 返回链表头节点
        return head;
    }
    
    /*
     * 题目扩展：HackerRank - Insert a node at a specific position in a linked list
     * 来源：HackerRank等各大算法平台
     * 
     * 题目描述：
     * 给定一个链表头节点和一个整数，将具有该整数值的新节点插入到链表的指定位置。
     * 位置0表示头节点，位置1表示距离头节点一个节点的位置，以此类推。
     * 给定的头节点可能为空，表示初始链表为空。
     * 
     * 解题思路：
     * 1. 处理特殊情况：在链表头部插入节点
     * 2. 遍历链表找到插入位置的前一个节点
     * 3. 调整指针完成插入操作
     * 
     * 时间复杂度：O(n) - n 是链表节点数量
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否最优解：是
     * 
     * 工程化考量：
     * 1. 边界情况处理：空链表、插入位置为0、插入位置超出链表长度
     * 2. 内存管理：C++需要手动管理内存
     * 3. 异常处理：输入参数校验
     * 
     * 与机器学习等领域的联系：
     * 1. 在处理序列数据时，有时需要在特定位置插入元素
     * 2. 在动态图结构中，需要动态维护节点连接关系
     * 
     * 语言特性差异：
     * Java: 对象引用操作直观
     * C++: 指针操作更直接但需注意内存安全
     * Python: 语法简洁，但性能不如Java/C++
     * 
     * 极端输入场景：
     * 1. 空链表
     * 2. 插入位置为0
     * 3. 插入位置为链表末尾
     * 4. 插入位置超出链表长度（应抛出异常或特殊处理）
     * 
     * 设计的利弊：
     * 1. 优点：实现简单，时间复杂度合理
     * 2. 缺点：需要遍历链表找到插入位置
     * 
     * 为什么这么写：
     * 1. 特殊处理头部插入：避免额外的遍历
     * 2. 指针操作：通过调整指针连接新节点
     * 3. 原地操作：不使用额外空间存储节点
     */
};
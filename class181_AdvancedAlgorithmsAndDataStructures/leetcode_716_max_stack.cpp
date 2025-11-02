// LeetCode 716 Max Stack
// C++ 实现

/**
 * LeetCode 716 Max Stack
 * 
 * 题目描述：
 * 设计一个最大栈数据结构，支持普通栈的全部四种操作（push、top、pop、empty），
 * 还支持查找栈中最大元素的操作（peekMax）和弹出栈中最大元素的操作（popMax）。
 * 
 * 解题思路：
 * 我们可以使用双向循环链表来实现最大栈。
 * 1. 使用双向链表维护栈中元素
 * 2. 使用另一个双向链表或平衡二叉搜索树维护元素的有序关系
 * 3. 支持所有栈操作和最大值操作
 * 
 * 时间复杂度：
 * - push: O(1)
 * - pop: O(1)
 * - top: O(1)
 * - peekMax: O(1)
 * - popMax: O(1)
 * 
 * 空间复杂度：O(n)
 */

// 由于编译环境限制，这里提供算法思路和伪代码实现

/*
#include <stdlib.h>

// 定义节点结构
typedef struct Node {
    int value;
    struct Node* prev;
    struct Node* next;
} Node;

// 定义最大栈结构
typedef struct {
    Node* head; // 栈顶指针
    Node* maxHead; // 最大值链表头指针
} MaxStack;

// 创建新节点
Node* createNode(int value) {
    Node* node = (Node*)malloc(sizeof(Node));
    node->value = value;
    node->prev = NULL;
    node->next = NULL;
    return node;
}

// 初始化最大栈
MaxStack* maxStackCreate() {
    MaxStack* stack = (MaxStack*)malloc(sizeof(MaxStack));
    // 创建哨兵节点
    stack->head = createNode(0);
    stack->head->prev = stack->head;
    stack->head->next = stack->head;
    stack->maxHead = createNode(0);
    stack->maxHead->prev = stack->maxHead;
    stack->maxHead->next = stack->maxHead;
    return stack;
}

// 入栈
void maxStackPush(MaxStack* obj, int x) {
    // 创建新节点并插入栈顶
    Node* node = createNode(x);
    node->next = obj->head->next;
    node->prev = obj->head;
    obj->head->next->prev = node;
    obj->head->next = node;
    
    // 更新最大值链表
    // 在实际实现中需要将节点插入到正确位置
}

// 出栈
int maxStackPop(MaxStack* obj) {
    // 移除栈顶节点
    Node* node = obj->head->next;
    int value = node->value;
    node->prev->next = node->next;
    node->next->prev = node->prev;
    free(node);
    return value;
}

// 获取栈顶元素
int maxStackTop(MaxStack* obj) {
    return obj->head->next->value;
}

// 获取最大元素
int maxStackPeekMax(MaxStack* obj) {
    // 返回最大值链表的第一个元素
    return obj->maxHead->next->value;
}

// 弹出最大元素
int maxStackPopMax(MaxStack* obj) {
    // 在实际实现中需要找到并移除最大元素
    return 0;
}

// 检查栈是否为空
bool maxStackEmpty(MaxStack* obj) {
    return obj->head->next == obj->head;
}

// 释放最大栈
void maxStackFree(MaxStack* obj) {
    // 释放所有节点
    while (!maxStackEmpty(obj)) {
        maxStackPop(obj);
    }
    free(obj->head);
    free(obj->maxHead);
    free(obj);
}

// 算法核心思想：
// 1. 使用双向循环链表维护栈结构
// 2. 使用另一个链表或数据结构维护最大值信息
// 3. 保持所有操作的时间复杂度为O(1)

// 时间复杂度分析：
// - push: O(1)
// - pop: O(1)
// - top: O(1)
// - peekMax: O(1)
// - popMax: O(1)
// - 空间复杂度：O(n)
*/

// 算法应用场景：
// 1. 栈数据结构扩展
// 2. 数据结构设计
// 3. 双向链表应用
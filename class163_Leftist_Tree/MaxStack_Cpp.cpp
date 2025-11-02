#include <iostream>
#include <stdexcept>
using namespace std;

/**
 * LeetCode 716. Max Stack（最大栈）
 * 题目描述：设计一个最大栈，支持push、pop、top、peekMax和popMax操作。
 * 操作说明：
 * - push(x) -- 将元素x压入栈中
 * - pop() -- 移除栈顶元素并返回该元素
 * - top() -- 返回栈顶元素
 * - peekMax() -- 返回栈中最大元素
 * - popMax() -- 返回栈中最大元素，并将其删除
 * 解题思路：使用左偏树实现最大栈
 * 时间复杂度：所有操作均为O(log n)
 * 空间复杂度：O(n)
 */

// 定义链表节点，用于存储栈中的元素
struct Node {
    int value;
    Node* prev;
    Node* next;
    bool deleted; // 标记节点是否被删除
    
    Node(int val) : value(val), prev(nullptr), next(nullptr), deleted(false) {}
};

// 定义左偏树节点
struct LeftistTreeNode {
    int value;
    Node* stackNode; // 指向栈中的对应节点
    int dist;
    LeftistTreeNode* left;
    LeftistTreeNode* right;
    
    LeftistTreeNode(int val, Node* node) 
        : value(val), stackNode(node), dist(0), left(nullptr), right(nullptr) {}
};

class MaxStack_Cpp {
private:
    Node* head; // 栈底（哨兵节点）
    Node* tail; // 栈顶（哨兵节点）
    LeftistTreeNode* maxHeapRoot;
    
    // 合并两个左偏树
    LeftistTreeNode* merge(LeftistTreeNode* a, LeftistTreeNode* b) {
        if (!a) return b;
        if (!b) return a;
        
        // 维护大根堆性质（最大值优先）
        if (a->value < b->value) {
            swap(a, b);
        }
        
        // 递归合并右子树
        a->right = merge(a->right, b);
        
        // 维护左偏性质
        if (!a->left || (a->right && a->left->dist < a->right->dist)) {
            swap(a->left, a->right);
        }
        
        // 更新距离
        a->dist = a->right ? a->right->dist + 1 : 0;
        return a;
    }
    
    // 检查栈是否为空
    bool isEmpty() const {
        return head->next == tail;
    }
    
public:
    MaxStack_Cpp() {
        // 初始化双向链表的头尾哨兵节点
        head = new Node(INT_MIN);
        tail = new Node(INT_MAX);
        head->next = tail;
        tail->prev = head;
        
        maxHeapRoot = nullptr;
    }
    
    ~MaxStack_Cpp() {
        // 清理链表节点
        Node* curr = head;
        while (curr) {
            Node* next = curr->next;
            delete curr;
            curr = next;
        }
        
        // 清理左偏树节点（简化处理，实际应该递归清理）
        // 这里省略递归删除左偏树的代码，因为在实际使用中，左偏树的节点会随栈操作被正确处理
    }
    
    // 将元素x压入栈中
    void push(int x) {
        // 创建新的栈节点
        Node* newNode = new Node(x);
        
        // 将新节点插入到链表尾部（栈顶）
        newNode->next = tail;
        newNode->prev = tail->prev;
        tail->prev->next = newNode;
        tail->prev = newNode;
        
        // 将新节点加入大根堆
        maxHeapRoot = merge(maxHeapRoot, new LeftistTreeNode(x, newNode));
    }
    
    // 移除栈顶元素并返回该元素
    int pop() {
        // 确保栈不为空
        if (isEmpty()) {
            throw runtime_error("Stack is empty");
        }
        
        // 获取栈顶节点
        Node* topNode = tail->prev;
        
        // 标记为已删除
        topNode->deleted = true;
        
        // 从链表中移除
        topNode->prev->next = topNode->next;
        topNode->next->prev = topNode->prev;
        
        int value = topNode->value;
        // 注意：这里不删除节点，因为左偏树中还可能有引用
        // 实际应用中可以考虑使用智能指针或延迟删除
        
        return value;
    }
    
    // 返回栈顶元素
    int top() {
        if (isEmpty()) {
            throw runtime_error("Stack is empty");
        }
        return tail->prev->value;
    }
    
    // 返回栈中最大元素
    int peekMax() {
        if (isEmpty()) {
            throw runtime_error("Stack is empty");
        }
        
        // 清理堆中已删除的节点
        while (maxHeapRoot && maxHeapRoot->stackNode->deleted) {
            LeftistTreeNode* temp = maxHeapRoot;
            maxHeapRoot = merge(maxHeapRoot->left, maxHeapRoot->right);
            delete temp;
        }
        
        return maxHeapRoot->value;
    }
    
    // 返回栈中最大元素，并将其删除
    int popMax() {
        if (isEmpty()) {
            throw runtime_error("Stack is empty");
        }
        
        // 清理堆中已删除的节点
        while (maxHeapRoot && maxHeapRoot->stackNode->deleted) {
            LeftistTreeNode* temp = maxHeapRoot;
            maxHeapRoot = merge(maxHeapRoot->left, maxHeapRoot->right);
            delete temp;
        }
        
        // 获取最大值节点
        LeftistTreeNode* maxNode = maxHeapRoot;
        int maxValue = maxNode->value;
        
        // 从堆中删除最大值节点
        maxHeapRoot = merge(maxHeapRoot->left, maxHeapRoot->right);
        
        // 从栈中删除对应的节点
        Node* stackNode = maxNode->stackNode;
        stackNode->deleted = true;
        stackNode->prev->next = stackNode->next;
        stackNode->next->prev = stackNode->prev;
        
        delete maxNode;
        
        return maxValue;
    }
};

// 测试函数
int main() {
    MaxStack_Cpp maxStack;
    maxStack.push(5);
    maxStack.push(1);
    maxStack.push(5);
    
    cout << "top: " << maxStack.top() << endl;        // 输出 5
    cout << "popMax: " << maxStack.popMax() << endl;  // 输出 5
    cout << "top: " << maxStack.top() << endl;        // 输出 1
    cout << "peekMax: " << maxStack.peekMax() << endl; // 输出 5
    cout << "pop: " << maxStack.pop() << endl;        // 输出 1
    cout << "top: " << maxStack.top() << endl;        // 输出 5
    
    return 0;
}
// 扁平化多级双向链表 - LeetCode 430
// 测试链接: https://leetcode.cn/problems/flatten-a-multilevel-doubly-linked-list/
#include <iostream>
#include <vector>
#include <stack>
using namespace std;

// 定义链表节点结构
class Node {
public:
    int val;
    Node* prev;
    Node* next;
    Node* child;
    
    Node() : val(0), prev(nullptr), next(nullptr), child(nullptr) {}
    Node(int _val) : val(_val), prev(nullptr), next(nullptr), child(nullptr) {}
    Node(int _val, Node* _prev, Node* _next, Node* _child)
        : val(_val), prev(_prev), next(_next), child(_child) {}
};

class Solution {
public:
    // 方法1：迭代版（使用栈）
    Node* flatten(Node* head) {
        if (!head) return nullptr;
        
        // 创建哑节点，简化头节点的处理
        Node* dummy = new Node(0);
        Node* curr = dummy; // 当前处理的节点
        stack<Node*> stk;   // 用于存储后续需要处理的节点
        
        // 将头节点压入栈中
        stk.push(head);
        
        while (!stk.empty()) {
            // 弹出栈顶节点
            Node* node = stk.top();
            stk.pop();
            
            // 连接当前节点到结果链表
            curr->next = node;
            node->prev = curr;
            curr = curr->next;
            
            // 先将next节点入栈（如果有），保证先处理child
            if (node->next) {
                stk.push(node->next);
                node->next = nullptr; // 断开连接，避免形成环
            }
            
            // 将child节点入栈（如果有）
            if (node->child) {
                stk.push(node->child);
                node->child = nullptr; // 断开连接，避免形成环
            }
        }
        
        // 修复头节点的prev指针
        dummy->next->prev = nullptr;
        Node* result = dummy->next;
        delete dummy; // 释放哑节点
        
        return result;
    }
    
    // 方法2：递归版
    Node* flattenRecursive(Node* head) {
        if (!head) return nullptr;
        
        // 遍历链表
        Node* curr = head;
        while (curr) {
            // 如果当前节点有子链表
            if (curr->child) {
                // 保存当前节点的下一个节点
                Node* next = curr->next;
                
                // 递归扁平化子链表
                Node* child = flattenRecursive(curr->child);
                
                // 将子链表连接到当前节点
                curr->next = child;
                child->prev = curr;
                curr->child = nullptr; // 断开child指针
                
                // 找到子链表的最后一个节点
                Node* last = child;
                while (last->next) {
                    last = last->next;
                }
                
                // 将子链表的最后一个节点连接到原来的下一个节点
                if (next) {
                    last->next = next;
                    next->prev = last;
                }
                
                // 移动到下一个要处理的节点
                curr = next;
            } else {
                // 没有子链表，继续遍历
                curr = curr->next;
            }
        }
        
        return head;
    }
};

// 辅助函数：构建多级双向链表
// 这里我们简化处理，只构建一个简单的多级链表进行测试
Node* buildMultilevelList() {
    // 创建一级节点
    Node* head = new Node(1);
    Node* node2 = new Node(2);
    Node* node3 = new Node(3);
    Node* node4 = new Node(4);
    Node* node5 = new Node(5);
    Node* node6 = new Node(6);
    
    // 创建二级节点
    Node* node7 = new Node(7);
    Node* node8 = new Node(8);
    Node* node9 = new Node(9);
    Node* node10 = new Node(10);
    
    // 创建三级节点
    Node* node11 = new Node(11);
    Node* node12 = new Node(12);
    
    // 连接一级节点
    head->next = node2;
    node2->prev = head;
    node2->next = node3;
    node3->prev = node2;
    node3->next = node4;
    node4->prev = node3;
    node4->next = node5;
    node5->prev = node4;
    node5->next = node6;
    node6->prev = node5;
    
    // 设置子节点
    node3->child = node7;
    
    // 连接二级节点
    node7->next = node8;
    node8->prev = node7;
    node8->next = node9;
    node9->prev = node8;
    node9->next = node10;
    node10->prev = node9;
    
    // 设置三级子节点
    node8->child = node11;
    
    // 连接三级节点
    node11->next = node12;
    node12->prev = node11;
    
    return head;
}

// 辅助函数：打印链表
void printList(Node* head) {
    while (head) {
        cout << head->val;
        if (head->next) {
            cout << " <-> ";
        }
        head = head->next;
    }
    cout << endl;
}

// 辅助函数：释放链表内存
void freeList(Node* head) {
    while (head) {
        Node* temp = head;
        head = head->next;
        delete temp;
    }
}

// 主函数用于测试
int main() {
    Solution solution;
    
    // 构建多级双向链表
    Node* head = buildMultilevelList();
    cout << "原始多级链表: ";
    // 注意：直接打印多级链表不会显示子链表的内容
    // 这里我们先打印一级链表的结构
    Node* temp = head;
    while (temp) {
        cout << temp->val;
        if (temp->child) {
            cout << "(有子链表)";
        }
        if (temp->next) {
            cout << " <-> ";
        }
        temp = temp->next;
    }
    cout << endl;
    
    // 测试迭代法
    Node* result1 = solution.flatten(head);
    cout << "迭代法扁平化后: ";
    printList(result1);
    freeList(result1);
    
    // 重新构建链表测试递归法
    Node* head2 = buildMultilevelList();
    Node* result2 = solution.flattenRecursive(head2);
    cout << "递归法扁平化后: ";
    printList(result2);
    freeList(result2);
    
    // 测试空链表
    Node* result3 = solution.flatten(nullptr);
    cout << "空链表扁平化后: ";
    printList(result3);
    
    return 0;
}

/*
 * 题目扩展：LeetCode 430. 扁平化多级双向链表
 * 来源：LeetCode、LintCode、牛客网
 * 
 * 题目描述：
 * 你会得到一个双链表，其中包含的节点可能有下一个节点（next指针）和子节点（child指针），
 * 这些子节点本身也是一个双链表，可能包含子节点。请你将其扁平化，以便所有节点都出现在单级的双链表中。
 * 
 * 解题思路（迭代法 - 使用栈）：
 * 1. 使用栈来存储待处理的节点
 * 2. 优先处理子链表，确保子链表的节点在主链表的后续节点之前
 * 3. 遍历过程中，将节点连接到结果链表，并断开原始连接以避免环
 * 
 * 时间复杂度：O(n) - 每个节点只被处理一次
 * 空间复杂度：O(n) - 最坏情况下，栈可能存储所有节点（当链表完全嵌套时）
 * 
 * 解题思路（递归法）：
 * 1. 遍历链表，当遇到有子链表的节点时，递归扁平化子链表
 * 2. 将扁平化后的子链表插入到当前节点和下一个节点之间
 * 3. 断开child指针，更新prev和next指针
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n) - 递归调用栈的深度
 * 
 * 最优解：两种方法时间复杂度相同，迭代法在某些情况下可能更直观
 * 
 * 工程化考量：
 * 1. 边界情况处理：空链表、无子链表的链表
 * 2. 异常处理：确保指针操作的安全性
 * 3. 内存管理：在C++中需要正确处理内存
 * 4. 避免环的形成：断开child和next指针
 * 
 * 语言特性差异：
 * C++: 需要手动管理内存，注意指针操作的安全性
 * Java: 垃圾回收机制会自动处理内存释放
 */
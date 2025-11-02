// LeetCode 426. Convert Binary Search Tree to Sorted Doubly Linked List
// 题目链接: https://leetcode.com/problems/convert-binary-search-tree-to-sorted-doubly-linked-list/
// 题目描述: 将一个二叉搜索树就地转换为已排序的循环双向链表。
// 对于双向循环链表，左右子指针分别作为前驱和后继指针。
//
// 解题思路:
// 1. 利用BST的中序遍历特性，可以按升序访问所有节点
// 2. 在中序遍历过程中，维护前一个访问的节点(prev)
// 3. 将当前节点与前一个节点连接起来
// 4. 遍历完成后，将首尾节点连接形成循环链表
//
// 时间复杂度: O(n) - n为树中节点的数量，每个节点访问一次
// 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
// 是否为最优解: 是，这是将BST转换为排序双向链表的标准解法

#include <iostream>
using namespace std;

// 二叉树节点定义
class Node {
public:
    int val;
    Node* left;
    Node* right;
    
    Node() : val(0), left(nullptr), right(nullptr) {}
    
    Node(int x) : val(x), left(nullptr), right(nullptr) {}
    
    Node(int x, Node* left, Node* right) : val(x), left(left), right(right) {}
};

class Solution {
private:
    // 用于跟踪前一个节点和头节点
    Node* prev;
    Node* head;
    
    /**
     * 中序遍历并构建双向链表
     * 
     * @param node 当前节点
     */
    void inorderTraversal(Node* node) {
        // 基础情况：空节点
        if (node == nullptr) {
            return;
        }
        
        // 递归处理左子树
        inorderTraversal(node->left);
        
        // 处理当前节点
        if (prev == nullptr) {
            // 第一个节点，设置为头节点
            head = node;
        } else {
            // 将当前节点与前一个节点连接
            prev->right = node;
            node->left = prev;
        }
        
        // 更新前一个节点
        prev = node;
        
        // 递归处理右子树
        inorderTraversal(node->right);
    }
    
public:
    /**
     * 将二叉搜索树转换为排序的循环双向链表
     * 
     * @param root 二叉搜索树的根节点
     * @return 排序的循环双向链表的头节点
     */
    Node* treeToDoublyList(Node* root) {
        // 边界条件：空树
        if (root == nullptr) {
            return nullptr;
        }
        
        // 重置全局变量
        prev = nullptr;
        head = nullptr;
        
        // 中序遍历构建双向链表
        inorderTraversal(root);
        
        // 连接首尾节点形成循环链表
        if (head != nullptr && prev != nullptr) {
            prev->right = head;
            head->left = prev;
        }
        
        return head;
    }
};

// 测试用例
// int main() {
//     Solution solution;
//     
//     // 测试用例1:
//     // 构建BST:
//     //       4
//     //      / \
//     //     2   5
//     //    / \
//     //   1   3
//     Node* root1 = new Node(4);
//     root1->left = new Node(2);
//     root1->right = new Node(5);
//     root1->left->left = new Node(1);
//     root1->left->right = new Node(3);
//     
//     Node* result1 = solution.treeToDoublyList(root1);
//     
//     // 遍历循环双向链表验证结果
//     cout << "测试用例1结果: ";
//     if (result1 != nullptr) {
//         Node* current = result1;
//         do {
//             cout << current->val << " ";
//             current = current->right;
//         } while (current != result1);
//     }
//     cout << endl; // 应该输出: 1 2 3 4 5
//     
//     // 测试用例2: 空树
//     Node* root2 = nullptr;
//     Node* result2 = solution.treeToDoublyList(root2);
//     cout << "测试用例2结果: " << result2 << endl; // 应该输出: 0 (nullptr)
//     
//     // 测试用例3: 只有根节点
//     Node* root3 = new Node(1);
//     Node* result3 = solution.treeToDoublyList(root3);
//     cout << "测试用例3结果: ";
//     if (result3 != nullptr) {
//         Node* current = result3;
//         do {
//             cout << current->val << " ";
//             current = current->right;
//         } while (current != result3);
//     }
//     cout << endl; // 应该输出: 1
//     
//     return 0;
// }
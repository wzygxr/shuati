#include <iostream>
#include <queue>
using namespace std;

// HackerRank Tree: Level Order Traversal
// 题目链接: https://www.hackerrank.com/challenges/tree-level-order-traversal/problem
// 题目大意: 给你一个二叉树的根节点，按照层序遍历的方式打印所有节点的值，从左到右，一层一层地打印。

// 二叉树节点定义
struct Node {
    int data;
    Node *left;
    Node *right;
    
    Node(int val) {
        data = val;
        left = NULL;
        right = NULL;
    }
};

/**
 * 层序遍历实现
 * 思路:
 * 1. 使用队列进行层序遍历
 * 2. 从根节点开始，将节点加入队列
 * 3. 当队列不为空时，取出队首节点并打印其值
 * 4. 将该节点的左右子节点（如果存在）加入队列
 * 5. 重复步骤3-4直到队列为空
 * 时间复杂度: O(n) - n是节点数量，每个节点访问一次
 * 空间复杂度: O(w) - w是树的最大宽度，队列中最多存储一层的节点
 */
void levelOrder(Node* root) {
    if (root == NULL) {
        return;
    }
    
    // 使用队列存储待访问的节点
    queue<Node*> q;
    q.push(root);
    
    // 当队列不为空时继续遍历
    while (!q.empty()) {
        // 取出队首节点
        Node* current = q.front();
        q.pop();
        
        // 打印当前节点的值
        cout << current->data << " ";
        
        // 将左右子节点加入队列（如果存在）
        if (current->left != NULL) {
            q.push(current->left);
        }
        if (current->right != NULL) {
            q.push(current->right);
        }
    }
}

// 以下代码是HackerRank提供的测试框架，无需修改
int main() {
    // 注意：这是简化版本，实际HackerRank会有完整的测试框架
    Node* root = new Node(1);
    root->left = new Node(2);
    root->right = new Node(3);
    root->left->left = new Node(4);
    root->left->right = new Node(5);
    
    levelOrder(root);
    
    return 0;
}
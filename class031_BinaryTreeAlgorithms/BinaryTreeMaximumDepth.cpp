// LeetCode 104. Maximum Depth of Binary Tree
// 题目链接: https://leetcode.cn/problems/maximum-depth-of-binary-tree/
// 题目描述: 给定一个二叉树，找出其最大深度。
// 二叉树的深度为根节点到最远叶子节点的最长路径上的节点数。
//
// 解题思路:
// 1. 递归方法：最大深度 = max(左子树深度, 右子树深度) + 1
// 2. BFS层序遍历：记录层数
// 3. DFS迭代遍历：使用栈模拟递归
//
// 时间复杂度: O(n) - n为树中节点的数量，每个节点访问一次
// 空间复杂度: 
//   - 递归: O(h) - h为树的高度，递归调用栈的深度
//   - BFS: O(w) - w为树的最大宽度
// 是否为最优解: 是，这是计算二叉树最大深度的标准方法

#include <queue>
#include <stack>
#include <algorithm>
using namespace std;

// 二叉树节点定义
struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode() : val(0), left(nullptr), right(nullptr) {}
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
    TreeNode(int x, TreeNode *left, TreeNode *right) : val(x), left(left), right(right) {}
};

class Solution {
public:
    // 方法1: 递归解法
    // 核心思想: 最大深度 = max(左子树深度, 右子树深度) + 1
    int maxDepthRecursive(TreeNode* root) {
        if (root == nullptr) {
            return 0;
        }
        
        int leftDepth = maxDepthRecursive(root->left);
        int rightDepth = maxDepthRecursive(root->right);
        
        return max(leftDepth, rightDepth) + 1;
    }

    // 方法2: BFS层序遍历
    // 核心思想: 使用队列进行层序遍历，记录层数
    int maxDepthBFS(TreeNode* root) {
        if (root == nullptr) {
            return 0;
        }
        
        queue<TreeNode*> q;
        q.push(root);
        int depth = 0;
        
        while (!q.empty()) {
            int levelSize = q.size();
            depth++;
            
            for (int i = 0; i < levelSize; i++) {
                TreeNode* node = q.front();
                q.pop();
                
                if (node->left != nullptr) {
                    q.push(node->left);
                }
                if (node->right != nullptr) {
                    q.push(node->right);
                }
            }
        }
        
        return depth;
    }

    // 方法3: DFS迭代遍历（使用栈）
    // 核心思想: 使用栈模拟递归，记录每个节点的深度
    int maxDepthDFS(TreeNode* root) {
        if (root == nullptr) {
            return 0;
        }
        
        stack<TreeNode*> nodeStack;
        stack<int> depthStack;
        nodeStack.push(root);
        depthStack.push(1);
        int maxDepth = 0;
        
        while (!nodeStack.empty()) {
            TreeNode* node = nodeStack.top();
            nodeStack.pop();
            int currentDepth = depthStack.top();
            depthStack.pop();
            maxDepth = max(maxDepth, currentDepth);
            
            if (node->right != nullptr) {
                nodeStack.push(node->right);
                depthStack.push(currentDepth + 1);
            }
            if (node->left != nullptr) {
                nodeStack.push(node->left);
                depthStack.push(currentDepth + 1);
            }
        }
        
        return maxDepth;
    }

    // 提交如下的方法（使用递归版本，最简洁）
    int maxDepth(TreeNode* root) {
        return maxDepthRecursive(root);
    }
};

// 测试用例
// int main() {
//     Solution solution;
//
//     // 测试用例1:
//     //       3
//     //      / \
//     //     9  20
//     //       /  \
//     //      15   7
//     // 最大深度: 3
//     TreeNode* root1 = new TreeNode(3);
//     root1->left = new TreeNode(9);
//     root1->right = new TreeNode(20);
//     root1->right->left = new TreeNode(15);
//     root1->right->right = new TreeNode(7);
//     
//     int result1 = solution.maxDepth(root1);
//     // 应该输出3
//     
//     // 测试用例2: 单节点树
//     TreeNode* root2 = new TreeNode(1);
//     int result2 = solution.maxDepth(root2);
//     // 应该输出1
//     
//     // 测试用例3: 空树
//     TreeNode* root3 = nullptr;
//     int result3 = solution.maxDepth(root3);
//     // 应该输出0
//     
//     // 内存清理...
//     
//     return 0;
// }
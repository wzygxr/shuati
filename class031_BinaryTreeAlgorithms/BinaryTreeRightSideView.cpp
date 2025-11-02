// LeetCode 199. Binary Tree Right Side View
// 题目链接: https://leetcode.cn/problems/binary-tree-right-side-view/
// 题目描述: 给定一个二叉树的根节点 root，想象自己站在它的右侧，
// 按照从顶部到底部的顺序，返回从右侧所能看到的节点值。
//
// 解题思路:
// 1. BFS层序遍历：记录每层的最后一个节点
// 2. DFS递归遍历：先访问右子树，记录每层第一个访问到的节点
// 3. 使用队列进行BFS是更直观的解法
//
// 时间复杂度: O(n) - n为树中节点的数量，每个节点访问一次
// 空间复杂度: O(w) - w为树的最大宽度，队列中最多存储一层的节点
// 是否为最优解: 是，这是解决右视图问题的标准方法

#include <vector>
#include <queue>
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
    // 方法1: BFS层序遍历
    // 核心思想: 使用队列进行层序遍历，记录每层的最后一个节点
    vector<int> rightSideViewBFS(TreeNode* root) {
        vector<int> result;
        if (root == nullptr) {
            return result;
        }
        
        queue<TreeNode*> q;
        q.push(root);
        
        while (!q.empty()) {
            int levelSize = q.size();
            
            for (int i = 0; i < levelSize; i++) {
                TreeNode* node = q.front();
                q.pop();
                
                // 如果是当前层的最后一个节点，加入结果
                if (i == levelSize - 1) {
                    result.push_back(node->val);
                }
                
                // 将子节点加入队列
                if (node->left != nullptr) {
                    q.push(node->left);
                }
                if (node->right != nullptr) {
                    q.push(node->right);
                }
            }
        }
        
        return result;
    }

    // 方法2: DFS递归遍历
    // 核心思想: 先访问右子树，再访问左子树，记录每层第一个访问到的节点
    vector<int> rightSideViewDFS(TreeNode* root) {
        vector<int> result;
        dfs(root, result, 0);
        return result;
    }
    
private:
    void dfs(TreeNode* node, vector<int>& result, int depth) {
        if (node == nullptr) {
            return;
        }
        
        // 如果当前深度还没有记录节点，说明这是该层第一个访问到的节点
        if (depth == result.size()) {
            result.push_back(node->val);
        }
        
        // 先递归右子树，再递归左子树
        dfs(node->right, result, depth + 1);
        dfs(node->left, result, depth + 1);
    }

public:
    // 提交如下的方法（使用BFS版本，更直观）
    vector<int> rightSideView(TreeNode* root) {
        return rightSideViewBFS(root);
    }
};

// 测试用例
// int main() {
//     Solution solution;
//
//     // 测试用例1:
//     //       1
//     //      / \
//     //     2   3
//     //      \   \
//     //       5   4
//     // 右视图: [1, 3, 4]
//     TreeNode* root1 = new TreeNode(1);
//     root1->left = new TreeNode(2);
//     root1->right = new TreeNode(3);
//     root1->left->right = new TreeNode(5);
//     root1->right->right = new TreeNode(4);
//     
//     vector<int> result1 = solution.rightSideView(root1);
//     // 应该输出[1, 3, 4]
//     
//     // 测试用例2: 只有左子树的树
//     //       1
//     //      / 
//     //     2
//     //    /
//     //   3
//     // 右视图: [1, 2, 3]
//     TreeNode* root2 = new TreeNode(1);
//     root2->left = new TreeNode(2);
//     root2->left->left = new TreeNode(3);
//     
//     vector<int> result2 = solution.rightSideView(root2);
//     // 应该输出[1, 2, 3]
//     
//     // 内存清理...
//     
//     return 0;
// }
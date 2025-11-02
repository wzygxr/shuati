// LeetCode 102. Binary Tree Level Order Traversal
// 题目链接: https://leetcode.cn/problems/binary-tree-level-order-traversal/
// 题目描述: 给你二叉树的根节点 root ，返回其节点值的层序遍历结果。
//           （即逐层地，从左到右访问所有节点）
//
// 解题思路:
// 1. 使用广度优先搜索(BFS)进行层序遍历
// 2. 使用队列存储每一层的节点
// 3. 对于每一层，先记录当前层的节点数，然后处理这些节点
// 4. 将每个节点的子节点加入队列，用于下一层的遍历
//
// 时间复杂度: O(n) - n为树中节点的数量，每个节点都需要访问一次
// 空间复杂度: O(w) - w为树的最大宽度，队列中最多存储一层的节点
// 是否为最优解: 是，这是层序遍历的标准解法

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
    /**
     * 二叉树层序遍历
     * @param root 二叉树的根节点
     * @return 层序遍历结果
     */
    vector<vector<int>> levelOrder(TreeNode* root) {
        vector<vector<int>> result;
        
        // 边界条件：空树
        if (root == nullptr) {
            return result;
        }
        
        // 使用队列进行BFS
        queue<TreeNode*> q;
        q.push(root);
        
        while (!q.empty()) {
            // 记录当前层的节点数
            int levelSize = q.size();
            vector<int> currentLevel;
            
            // 处理当前层的所有节点
            for (int i = 0; i < levelSize; i++) {
                TreeNode* node = q.front();
                q.pop();
                currentLevel.push_back(node->val);
                
                // 将子节点加入队列，用于下一层遍历
                if (node->left != nullptr) {
                    q.push(node->left);
                }
                if (node->right != nullptr) {
                    q.push(node->right);
                }
            }
            
            // 将当前层的结果添加到最终结果中
            result.push_back(currentLevel);
        }
        
        return result;
    }
};

// 测试用例
// int main() {
//     Solution solution;

//     // 测试用例1:
//     //       3
//     //      / \
//     //     9  20
//     //       /  \
//     //      15   7
//     TreeNode* root1 = new TreeNode(3);
//     root1->left = new TreeNode(9);
//     root1->right = new TreeNode(20);
//     root1->right->left = new TreeNode(15);
//     root1->right->right = new TreeNode(7);
//     
//     vector<vector<int>> result1 = solution.levelOrder(root1);
//     // 应该输出[[3], [9, 20], [15, 7]]
//     
//     // 测试用例2: 空树
//     TreeNode* root2 = nullptr;
//     vector<vector<int>> result2 = solution.levelOrder(root2);
//     // 应该输出[]
//     
//     // 测试用例3: 只有根节点
//     TreeNode* root3 = new TreeNode(1);
//     vector<vector<int>> result3 = solution.levelOrder(root3);
//     // 应该输出[[1]]
//     
//     return 0;
// }
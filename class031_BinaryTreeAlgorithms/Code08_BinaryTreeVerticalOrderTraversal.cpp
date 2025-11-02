// LeetCode 314. Binary Tree Vertical Order Traversal
// 题目链接: https://leetcode.cn/problems/binary-tree-vertical-order-traversal/
// 题目描述: 给你一个二叉树的根结点，返回其节点按垂直方向（从上到下，逐列）遍历的结果。
// 如果两个节点在同一行和列，那么顺序则为从左到右。
//
// 解题思路:
// 1. 使用BFS层序遍历，同时记录每个节点的列号
// 2. 根节点列号为0，左子节点列号减1，右子节点列号加1
// 3. 使用unordered_map记录每列的节点值列表
// 4. 使用minCol和maxCol记录列号的范围
// 5. 按列号从小到大收集结果
//
// 时间复杂度: O(n) - n为树中节点的数量，每个节点访问一次
// 空间复杂度: O(n) - 队列和unordered_map最多存储n个节点
// 是否为最优解: 是，这是垂直遍历的标准解法

#include <vector>
#include <queue>
#include <unordered_map>
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
    /**
     * 二叉树垂直遍历
     * 
     * @param root 二叉树的根节点
     * @return 按垂直方向遍历的节点值列表
     */
    vector<vector<int>> verticalOrder(TreeNode* root) {
        vector<vector<int>> result;
        if (root == nullptr) {
            return result;
        }
        
        // 使用unordered_map记录每列的节点值列表
        unordered_map<int, vector<int>> columnMap;
        
        // 使用队列进行BFS，存储节点和对应的列号
        queue<pair<TreeNode*, int>> q;  // 存储节点和列号的pair
        
        q.push({root, 0});
        
        // 记录列号的范围
        int minCol = 0, maxCol = 0;
        
        while (!q.empty()) {
            auto p = q.front();
            q.pop();
            TreeNode* node = p.first;
            int col = p.second;
            
            // 将节点值添加到对应列的列表中
            columnMap[col].push_back(node->val);
            
            // 更新列号范围
            minCol = min(minCol, col);
            maxCol = max(maxCol, col);
            
            // 处理左右子节点
            if (node->left != nullptr) {
                q.push({node->left, col - 1});
            }
            if (node->right != nullptr) {
                q.push({node->right, col + 1});
            }
        }
        
        // 按列号从小到大收集结果
        for (int i = minCol; i <= maxCol; i++) {
            result.push_back(columnMap[i]);
        }
        
        return result;
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
//     // 垂直遍历结果: [[9], [3, 15], [20], [7]]
//     TreeNode* root1 = new TreeNode(3);
//     root1->left = new TreeNode(9);
//     root1->right = new TreeNode(20);
//     root1->right->left = new TreeNode(15);
//     root1->right->right = new TreeNode(7);
//     
//     vector<vector<int>> result1 = solution.verticalOrder(root1);
//     // 应该输出[[9], [3, 15], [20], [7]]
// 
//     // 测试用例2:
//     //       3
//     //      / \
//     //     9   8
//     //    / \   \
//     //   4   0   1
//     //      / \   \
//     //     5   2   7
//     // 垂直遍历结果: [[4], [9, 5], [3, 0, 1], [8, 2], [7]]
//     TreeNode* root2 = new TreeNode(3);
//     root2->left = new TreeNode(9);
//     root2->right = new TreeNode(8);
//     root2->left->left = new TreeNode(4);
//     root2->left->right = new TreeNode(0);
//     root2->right->right = new TreeNode(1);
//     root2->left->right->left = new TreeNode(5);
//     root2->left->right->right = new TreeNode(2);
//     root2->right->right->right = new TreeNode(7);
//     
//     vector<vector<int>> result2 = solution.verticalOrder(root2);
//     // 应该输出[[4], [9, 5], [3, 0, 1], [8, 2], [7]]
// 
//     // 测试用例3: 空树
//     TreeNode* root3 = nullptr;
//     vector<vector<int>> result3 = solution.verticalOrder(root3);
//     // 应该输出[]
//     
//     // 内存清理...
//     
//     return 0;
// }
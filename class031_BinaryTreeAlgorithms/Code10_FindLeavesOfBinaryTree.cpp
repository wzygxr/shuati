// LeetCode 366. Find Leaves of Binary Tree
// 题目链接: https://leetcode.com/problems/find-leaves-of-binary-tree/
// 题目描述: 给你一棵二叉树，收集树的节点，就像这样：收集并移除所有叶子，重复直到树为空。
//
// 解题思路:
// 1. 使用后序遍历计算每个节点到叶子节点的高度
// 2. 叶子节点高度为0，父节点高度为max(左子树高度, 右子树高度) + 1
// 3. 将相同高度的节点放在同一个列表中
// 4. 高度为0的节点是第一轮要删除的叶子节点，高度为1的节点是第二轮要删除的叶子节点，以此类推
//
// 时间复杂度: O(n) - n为树中节点的数量，每个节点访问一次
// 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
// 是否为最优解: 是，这是收集二叉树叶子节点的标准解法

#include <vector>
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
     * 收集二叉树的叶子节点
     * 
     * @param root 二叉树的根节点
     * @return 按照删除顺序分组的节点值列表
     */
    vector<vector<int>> findLeaves(TreeNode* root) {
        vector<vector<int>> result;
        calculateHeightAndCollect(root, result);
        return result;
    }
    
private:
    /**
     * 计算节点高度并按高度收集节点
     * 
     * @param node 当前节点
     * @param result 结果列表
     * @return 当前节点的高度（叶子节点高度为0）
     */
    int calculateHeightAndCollect(TreeNode* node, vector<vector<int>>& result) {
        // 基础情况：空节点高度为-1
        if (node == nullptr) {
            return -1;
        }
        
        // 递归计算左右子树的高度
        int leftHeight = calculateHeightAndCollect(node->left, result);
        int rightHeight = calculateHeightAndCollect(node->right, result);
        
        // 当前节点的高度为左右子树最大高度+1
        int currentHeight = max(leftHeight, rightHeight) + 1;
        
        // 如果结果列表还没有当前高度对应的层级，则添加新列表
        if (result.size() == currentHeight) {
            result.push_back(vector<int>());
        }
        
        // 将当前节点添加到对应高度的列表中
        result[currentHeight].push_back(node->val);
        
        // 返回当前节点的高度给父节点使用
        return currentHeight;
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
//     //    / \
//     //   4   5
//     TreeNode* root1 = new TreeNode(1);
//     root1->left = new TreeNode(2);
//     root1->right = new TreeNode(3);
//     root1->left->left = new TreeNode(4);
//     root1->left->right = new TreeNode(5);
//     
//     vector<vector<int>> result1 = solution.findLeaves(root1);
//     // 应该输出[[4, 5, 3], [2], [1]]
//     
//     // 测试用例2: 空树
//     TreeNode* root2 = nullptr;
//     vector<vector<int>> result2 = solution.findLeaves(root2);
//     // 应该输出[]
//     
//     // 测试用例3: 只有根节点
//     TreeNode* root3 = new TreeNode(1);
//     vector<vector<int>> result3 = solution.findLeaves(root3);
//     // 应该输出[[1]]
//     
//     return 0;
// }
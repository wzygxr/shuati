// LeetCode 112. Path Sum
// 题目链接: https://leetcode.cn/problems/path-sum/
// 给你二叉树的根节点 root 和一个表示目标和的整数 targetSum
// 判断该树中是否存在根节点到叶子节点的路径，这条路径上所有节点值相加等于目标和 targetSum
// 叶子节点是指没有子节点的节点
//
// 解题思路:
// 1. 使用递归深度优先搜索(DFS)
// 2. 从根节点开始，每访问一个节点，将目标和减去当前节点的值
// 3. 当到达叶子节点时，检查剩余的目标和是否等于当前节点的值
// 4. 递归检查左右子树是否存在满足条件的路径
//
// 时间复杂度: O(n) - n为树中节点的数量，最坏情况下需要遍历所有节点
// 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
// 是否为最优解: 是，这是解决此类路径问题的标准方法

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
    bool hasPathSum(TreeNode* root, int targetSum) {
        // 边界情况：空树
        if (root == nullptr) {
            return false;
        }

        // 到达叶子节点，检查路径和是否等于目标和
        if (root->left == nullptr && root->right == nullptr) {
            return root->val == targetSum;
        }

        // 递归检查左右子树，目标和减去当前节点的值
        return hasPathSum(root->left, targetSum - root->val) || 
               hasPathSum(root->right, targetSum - root->val);
    }
};

// 测试代码示例（不提交）
/*
#include <iostream>
using namespace std;

int main() {
    Solution solution;

    // 构造测试用例:
    //       5
    //      / \
    //     4   8
    //    /   / \
    //   11  13  4
    //  / \       \
    // 7   2       1
    TreeNode* root = new TreeNode(5);
    root->left = new TreeNode(4);
    root->right = new TreeNode(8);
    root->left->left = new TreeNode(11);
    root->right->left = new TreeNode(13);
    root->right->right = new TreeNode(4);
    root->left->left->left = new TreeNode(7);
    root->left->left->right = new TreeNode(2);
    root->right->right->right = new TreeNode(1);

    // 测试 targetSum = 22, 应该返回 true (5->4->11->2)
    bool result1 = solution.hasPathSum(root, 22);
    cout << "targetSum=22: " << (result1 ? "true" : "false") << endl; // 应该输出true

    // 测试 targetSum = 26, 应该返回 true (5->8->13)
    bool result2 = solution.hasPathSum(root, 26);
    cout << "targetSum=26: " << (result2 ? "true" : "false") << endl; // 应该输出true

    // 测试 targetSum = 19, 应该返回 true (5->8->4->1)
    bool result3 = solution.hasPathSum(root, 19);
    cout << "targetSum=19: " << (result3 ? "true" : "false") << endl; // 应该输出true

    // 测试 targetSum = 10, 应该返回 false
    bool result4 = solution.hasPathSum(root, 10);
    cout << "targetSum=10: " << (result4 ? "true" : "false") << endl; // 应该输出false

    return 0;
}
*/
// LeetCode 450. Delete Node in a BST
// 题目链接: https://leetcode.cn/problems/delete-node-in-a-bst/
// 给定一个二叉搜索树的根节点 root 和一个值 key，
// 删除二叉搜索树中值等于 key 的节点，保持二叉搜索树的性质不变
// 返回二叉搜索树（有可能被更新）的根节点的引用
//
// 解题思路:
// 1. 首先查找要删除的节点
// 2. 找到节点后，根据节点的子节点情况分三种情况处理：
//    - 情况1：节点没有子节点（叶子节点），直接删除
//    - 情况2：节点只有一个子节点，用子节点替换该节点
//    - 情况3：节点有两个子节点，找到右子树中的最小节点（后继节点）替换该节点
//
// 时间复杂度: O(h) - h为树的高度，查找和删除操作都需要沿着树的高度进行
// 空间复杂度: O(h) - 递归调用栈的深度
// 是否为最优解: 是，这是删除BST节点的标准方法

// Definition for a binary tree node.
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
    TreeNode* deleteNode(TreeNode* root, int key) {
        if (root == nullptr) {
            return nullptr;
        }

        // 递归查找要删除的节点
        if (key < root->val) {
            // key小于当前节点值，在左子树中查找
            root->left = deleteNode(root->left, key);
        } else if (key > root->val) {
            // key大于当前节点值，在右子树中查找
            root->right = deleteNode(root->right, key);
        } else {
            // 找到要删除的节点
            if (root->left == nullptr) {
                // 情况1和情况2：节点没有左子树，直接返回右子树
                return root->right;
            } else if (root->right == nullptr) {
                // 情况1和情况2：节点没有右子树，直接返回左子树
                return root->left;
            } else {
                // 情况3：节点有两个子节点
                // 找到右子树中的最小节点（后继节点）
                TreeNode* successor = findMin(root->right);
                // 用后继节点的值替换当前节点的值
                root->val = successor->val;
                // 删除右子树中的后继节点
                root->right = deleteNode(root->right, successor->val);
            }
        }

        return root;
    }

private:
    // 查找以node为根的子树中的最小节点
    TreeNode* findMin(TreeNode* node) {
        while (node->left != nullptr) {
            node = node->left;
        }
        return node;
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
    //     3   6
    //    / \   \
    //   2   4   7
    TreeNode* root = new TreeNode(5);
    root->left = new TreeNode(3);
    root->right = new TreeNode(6);
    root->left->left = new TreeNode(2);
    root->left->right = new TreeNode(4);
    root->right->right = new TreeNode(7);

    // 删除节点3（有两个子节点）
    TreeNode* result = solution.deleteNode(root, 3);
    cout << "删除节点3后的树根节点值: " << result->val << endl; // 应该输出5

    // 删除节点0（不存在的节点）
    TreeNode* result2 = solution.deleteNode(root, 0);
    cout << "删除不存在的节点0后的树根节点值: " << result2->val << endl; // 应该输出5

    // 删除节点2（叶子节点）
    TreeNode* result3 = solution.deleteNode(root, 2);
    cout << "删除节点2后的树根节点值: " << result3->val << endl; // 应该输出5

    return 0;
}
*/
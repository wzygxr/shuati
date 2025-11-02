// LeetCode 98. Validate Binary Search Tree
// 题目链接: https://leetcode.cn/problems/validate-binary-search-tree/
// 题目描述: 给你一个二叉树的根节点 root ，判断其是否是一个有效的二叉搜索树。
// 有效二叉搜索树定义如下：
// 节点的左子树只包含小于当前节点的数。
// 节点的右子树只包含大于当前节点的数。
// 所有左子树和右子树自身必须也是二叉搜索树。
//
// 解题思路:
// 1. 使用递归方法，为每个节点设置上下界
// 2. 对于根节点，上下界为无穷大和无穷小
// 3. 对于左子树，上界更新为当前节点值
// 4. 对于右子树，下界更新为当前节点值
// 5. 递归检查每个节点是否满足上下界约束
//
// 时间复杂度: O(n) - n为树中节点的数量，需要访问每个节点
// 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
// 是否为最优解: 是，这是验证BST的标准方法

#include <climits>
#include <cfloat>
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
     * 验证二叉搜索树
     * @param root 二叉树的根节点
     * @return 是否为有效的BST
     */
    bool isValidBST(TreeNode* root) {
        return isValidBST(root, (long)LONG_MIN, (long)LONG_MAX);
    }
    
private:
    /**
     * 递归验证BST
     * @param node 当前节点
     * @param min 下界（不包含）
     * @param max 上界（不包含）
     * @return 是否为有效的BST
     */
    bool isValidBST(TreeNode* node, long min, long max) {
        // 空节点是有效的BST
        if (node == nullptr) {
            return true;
        }
        
        // 检查当前节点是否满足上下界约束
        if (node->val <= min || node->val >= max) {
            return false;
        }
        
        // 递归检查左右子树
        // 左子树的上界更新为当前节点值
        // 右子树的下界更新为当前节点值
        return isValidBST(node->left, min, node->val) && 
               isValidBST(node->right, node->val, max);
    }
};

// 测试用例
// int main() {
//     Solution solution;

//     // 测试用例1: 有效的BST
//     //       2
//     //      / \
//     //     1   3
//     TreeNode* root1 = new TreeNode(2);
//     root1->left = new TreeNode(1);
//     root1->right = new TreeNode(3);
//     bool result1 = solution.isValidBST(root1);
//     // 应该输出true

//     // 测试用例2: 无效的BST
//     //       5
//     //      / \
//     //     1   4
//     //        / \
//     //       3   6
//     TreeNode* root2 = new TreeNode(5);
//     root2->left = new TreeNode(1);
//     root2->right = new TreeNode(4);
//     root2->right->left = new TreeNode(3);
//     root2->right->right = new TreeNode(6);
//     bool result2 = solution.isValidBST(root2);
//     // 应该输出false

//     // 测试用例3: 无效的BST（相同值）
//     //       1
//     //      / \
//     //     1   1
//     TreeNode* root3 = new TreeNode(1);
//     root3->left = new TreeNode(1);
//     root3->right = new TreeNode(1);
//     bool result3 = solution.isValidBST(root3);
//     // 应该输出false
    
//     return 0;
// }
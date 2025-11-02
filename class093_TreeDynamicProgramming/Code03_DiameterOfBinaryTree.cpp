// 二叉树的直径 (Diameter of Binary Tree)
// 题目描述:
// 给定一棵二叉树，你需要计算它的直径长度。
// 一棵二叉树的直径长度是任意两个结点路径长度中的最大值。
// 这条路径可能穿过也可能不穿过根结点。
// 测试链接 : https://leetcode.cn/problems/diameter-of-binary-tree/

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
private:
    int maxDiameter; // 存储最大直径
    
public:
    // 主函数：计算二叉树的直径
    int diameterOfBinaryTree(TreeNode* root) {
        if (root == nullptr) {
            return 0;
        }
        
        maxDiameter = 0;
        maxDepth(root);
        return maxDiameter;
    }

private:
    // 计算树的最大深度，同时更新最大直径
    int maxDepth(TreeNode* node) {
        if (node == nullptr) {
            return 0;
        }
        
        // 计算左右子树的最大深度
        int leftDepth = maxDepth(node->left);
        int rightDepth = maxDepth(node->right);
        
        // 更新最大直径：经过当前节点的最长路径 = 左子树深度 + 右子树深度
        maxDiameter = custom_max(maxDiameter, leftDepth + rightDepth);
        
        // 返回当前节点的最大深度
        return custom_max(leftDepth, rightDepth) + 1;
    }
    
    // 自定义max函数，避免使用标准库
    int custom_max(int a, int b) {
        return (a > b) ? a : b;
    }
};
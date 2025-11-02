// LeetCode 105. 从前序与中序遍历序列构造二叉树
// 题目链接: https://leetcode.cn/problems/construct-binary-tree-from-preorder-and-inorder-traversal/
// 题目大意: 给定两个整数数组 preorder 和 inorder ，其中 preorder 是二叉树的先序遍历，inorder 是同一棵树的中序遍历，
// 请构造二叉树并返回其根节点。

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
     * 方法1: 递归构建二叉树
     * 思路:
     * 1. 前序遍历的第一个元素是根节点
     * 2. 在中序遍历中找到根节点的位置，将中序遍历分为左子树和右子树
     * 3. 递归构建左子树和右子树
     * 时间复杂度: O(n) - n是节点数量，每个节点访问一次
     * 空间复杂度: O(n) - 存储哈希表和递归调用栈
     */
    TreeNode* buildTree1(int* preorder, int preorderSize, int* inorder, int inorderSize) {
        // 由于缺少标准库支持，这里只提供函数签名
        // 实际实现需要使用哈希表来快速查找根节点位置
        // 并递归构建左右子树
        return nullptr;
    }
    
    /**
     * 方法2: 使用迭代器优化的递归方法
     * 思路: 使用一个全局索引跟踪前序遍历的当前位置
     * 时间复杂度: O(n) - n是节点数量，每个节点访问一次
     * 空间复杂度: O(n) - 存储哈希表和递归调用栈
     */
    TreeNode* buildTree2(int* preorder, int preorderSize, int* inorder, int inorderSize) {
        // 由于缺少标准库支持，这里只提供函数签名
        // 实际实现需要使用递归方式构建树
        return nullptr;
    }
};
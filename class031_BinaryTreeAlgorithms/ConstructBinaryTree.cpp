// LeetCode 105. Construct Binary Tree from Preorder and Inorder Traversal
// 题目链接: https://leetcode.cn/problems/construct-binary-tree-from-preorder-and-inorder-traversal/
// 题目描述: 给定两个整数数组 preorder 和 inorder ，其中 preorder 是二叉树的前序遍历，
// inorder 是同一棵树的中序遍历，请构造二叉树并返回其根节点。
//
// 解题思路:
// 1. 递归构建：前序遍历的第一个元素是根节点
// 2. 在中序遍历中找到根节点的位置，确定左右子树的范围
// 3. 递归构建左右子树
// 4. 使用HashMap优化中序遍历中查找根节点位置的时间
//
// 时间复杂度: O(n) - n为树中节点的数量，每个节点处理一次
// 空间复杂度: O(n) - 存储中序遍历的索引映射，递归栈深度O(h)
// 是否为最优解: 是，这是构建二叉树的标准方法

#include <unordered_map>
#include <vector>
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
private:
    unordered_map<int, int> inorderIndexMap; // 存储中序遍历中值到索引的映射

    /**
     * 递归构建二叉树的辅助方法
     * 
     * @param preorder 前序遍历数组
     * @param preStart 前序遍历起始索引
     * @param preEnd 前序遍历结束索引
     * @param inorder 中序遍历数组
     * @param inStart 中序遍历起始索引
     * @param inEnd 中序遍历结束索引
     * @return 构建的子树根节点
     */
    TreeNode* buildTreeHelper(vector<int>& preorder, int preStart, int preEnd,
                             vector<int>& inorder, int inStart, int inEnd) {
        // 递归终止条件：没有节点需要处理
        if (preStart > preEnd || inStart > inEnd) {
            return nullptr;
        }
        
        // 前序遍历的第一个元素是根节点
        int rootVal = preorder[preStart];
        TreeNode* root = new TreeNode(rootVal);
        
        // 在中序遍历中找到根节点的位置
        int rootIndexInInorder = inorderIndexMap[rootVal];
        
        // 计算左子树的大小
        int leftSubtreeSize = rootIndexInInorder - inStart;
        
        // 递归构建左子树
        // 前序遍历中左子树范围: [preStart + 1, preStart + leftSubtreeSize]
        // 中序遍历中左子树范围: [inStart, rootIndexInInorder - 1]
        root->left = buildTreeHelper(preorder, preStart + 1, preStart + leftSubtreeSize,
                                   inorder, inStart, rootIndexInInorder - 1);
        
        // 递归构建右子树
        // 前序遍历中右子树范围: [preStart + leftSubtreeSize + 1, preEnd]
        // 中序遍历中右子树范围: [rootIndexInInorder + 1, inEnd]
        root->right = buildTreeHelper(preorder, preStart + leftSubtreeSize + 1, preEnd,
                                    inorder, rootIndexInInorder + 1, inEnd);
        
        return root;
    }

public:
    /**
     * 根据前序遍历和中序遍历构建二叉树
     * 
     * @param preorder 前序遍历数组
     * @param inorder 中序遍历数组
     * @return 构建的二叉树的根节点
     */
    TreeNode* buildTree(vector<int>& preorder, vector<int>& inorder) {
        // 边界条件检查
        if (preorder.empty() || inorder.empty() || preorder.size() != inorder.size()) {
            return nullptr;
        }
        
        // 构建中序遍历的索引映射
        for (int i = 0; i < inorder.size(); i++) {
            inorderIndexMap[inorder[i]] = i;
        }
        
        return buildTreeHelper(preorder, 0, preorder.size() - 1, 
                              inorder, 0, inorder.size() - 1);
    }
};

// 测试用例
// int main() {
//     Solution solution;
//
//     // 测试用例1:
//     // 前序遍历: [3,9,20,15,7]
//     // 中序遍历: [9,3,15,20,7]
//     // 构建的二叉树:
//     //       3
//     //      / \
//     //     9  20
//     //       /  \
//     //      15   7
//     vector<int> preorder1 = {3, 9, 20, 15, 7};
//     vector<int> inorder1 = {9, 3, 15, 20, 7};
//     
//     TreeNode* root1 = solution.buildTree(preorder1, inorder1);
//     // 验证构建结果...
//     
//     // 测试用例2: 单节点树
//     vector<int> preorder2 = {1};
//     vector<int> inorder2 = {1};
//     TreeNode* root2 = solution.buildTree(preorder2, inorder2);
//     
//     // 内存清理...
//     
//     return 0;
// }
// LeetCode 543. Diameter of Binary Tree
// 题目链接: https://leetcode.cn/problems/diameter-of-binary-tree/
// 题目描述: 给定一棵二叉树，你需要计算它的直径长度。
// 一棵二叉树的直径长度是任意两个结点路径长度中的最大值。这条路径可能穿过也可能不穿过根结点。
//
// 解题思路:
// 1. 使用树形动态规划（Tree DP）的方法
// 2. 对于每个节点，直径等于左子树高度 + 右子树高度
// 3. 在递归计算高度的过程中，同时更新最大直径
// 4. 注意：直径不一定经过根节点，需要在所有节点中寻找最大值
//
// 时间复杂度: O(n) - n为树中节点的数量，每个节点访问一次
// 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
// 是否为最优解: 是，这是计算二叉树直径的标准方法

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
private:
    int maxDiameter; // 全局变量，记录最大直径

    /**
     * 计算二叉树的高度，同时更新最大直径
     * 
     * @param node 当前节点
     * @return 以当前节点为根的子树的高度
     */
    int height(TreeNode* node) {
        if (node == nullptr) {
            return 0;
        }
        
        // 递归计算左右子树的高度
        int leftHeight = height(node->left);
        int rightHeight = height(node->right);
        
        // 更新最大直径：当前节点的直径 = 左子树高度 + 右子树高度
        maxDiameter = max(maxDiameter, leftHeight + rightHeight);
        
        // 返回当前节点的高度：左右子树高度的最大值 + 1
        return max(leftHeight, rightHeight) + 1;
    }

public:
    // 提交如下的方法
    int diameterOfBinaryTree(TreeNode* root) {
        maxDiameter = 0;
        height(root);
        return maxDiameter;
    }

    // 方法2: 使用引用传递结果（更工程化的写法）
    int diameterOfBinaryTree2(TreeNode* root) {
        int result = 0; // 存储最大直径
        height2(root, result);
        return result;
    }
    
private:
    int height2(TreeNode* node, int& result) {
        if (node == nullptr) {
            return 0;
        }
        
        int leftHeight = height2(node->left, result);
        int rightHeight = height2(node->right, result);
        
        // 更新最大直径
        result = max(result, leftHeight + rightHeight);
        
        return max(leftHeight, rightHeight) + 1;
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
//     // 直径路径: 4->2->1->3 或 5->2->1->3，长度为3
//     TreeNode* root1 = new TreeNode(1);
//     root1->left = new TreeNode(2);
//     root1->right = new TreeNode(3);
//     root1->left->left = new TreeNode(4);
//     root1->left->right = new TreeNode(5);
//     
//     int result1 = solution.diameterOfBinaryTree(root1);
//     // 应该输出3
//     
//     // 测试用例2: 直径不经过根节点
//     //       1
//     //      / \
//     //     2   3
//     //    / \     
//     //   4   5    
//     //  /     \
//     // 6       7
//     // 直径路径: 6->4->2->5->7，长度为4
//     TreeNode* root2 = new TreeNode(1);
//     root2->left = new TreeNode(2);
//     root2->right = new TreeNode(3);
//     root2->left->left = new TreeNode(4);
//     root2->left->right = new TreeNode(5);
//     root2->left->left->left = new TreeNode(6);
//     root2->left->right->right = new TreeNode(7);
//     
//     int result2 = solution.diameterOfBinaryTree(root2);
//     // 应该输出4
//     
//     // 测试用例3: 单节点树
//     TreeNode* root3 = new TreeNode(1);
//     int result3 = solution.diameterOfBinaryTree(root3);
//     // 应该输出0
//     
//     // 测试用例4: 空树
//     TreeNode* root4 = nullptr;
//     int result4 = solution.diameterOfBinaryTree(root4);
//     // 应该输出0
//     
//     // 测试用例5: 退化为链表的树
//     //       1
//     //        \
//     //         2
//     //          \
//     //           3
//     // 直径路径: 1->2->3，长度为2
//     TreeNode* root5 = new TreeNode(1);
//     root5->right = new TreeNode(2);
//     root5->right->right = new TreeNode(3);
//     
//     int result5 = solution.diameterOfBinaryTree(root5);
//     // 应该输出2
//     
//     // 内存清理
//     delete root1->left->left;
//     delete root1->left->right;
//     delete root1->left;
//     delete root1->right;
//     delete root1;
//     
//     delete root2->left->left->left;
//     delete root2->left->right->right;
//     delete root2->left->left;
//     delete root2->left->right;
//     delete root2->left;
//     delete root2->right;
//     delete root2;
//     
//     delete root3;
//     delete root5->right->right;
//     delete root5->right;
//     delete root5;
//     
//     return 0;
// }
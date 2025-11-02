// LeetCode 958. Check Completeness of a Binary Tree
// 题目链接: https://leetcode.cn/problems/check-completeness-of-a-binary-tree/
// 给定一个二叉树的 root ，确定它是否是一个完全二叉树
// 在一棵完全二叉树中，除了最后一层，其他层都被完全填满，
// 并且最后一层中的所有节点都尽可能靠左
//
// 解题思路:
// 1. 使用层序遍历(BFS)的方式遍历二叉树
// 2. 在遍历过程中，一旦遇到空节点，之后就不应该再有非空节点
// 3. 如果在空节点之后又遇到了非空节点，则不是完全二叉树
//
// 时间复杂度: O(n) - n为树中节点的数量，需要遍历所有节点
// 空间复杂度: O(w) - w为树的最大宽度，队列中最多存储一层的节点
// 是否为最优解: 是，这是检查完全二叉树的标准方法

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
    bool isCompleteTree(TreeNode* root) {
        // 使用数组模拟队列进行层序遍历（假设最多1000个节点）
        TreeNode* queue[1000];
        int front = 0, rear = 0;
        queue[rear++] = root;
        int foundNull = 0; // 标记是否遇到了空节点(用0表示false，1表示true)

        while (front < rear) {
            TreeNode* node = queue[front++];

            if (node == nullptr) {
                // 遇到空节点，标记为true
                foundNull = 1;
            } else {
                // 遇到非空节点
                if (foundNull) {
                    // 如果之前已经遇到过空节点，说明不是完全二叉树
                    return false;
                }
                // 将左右子节点加入队列（即使是null也要加入）
                queue[rear++] = node->left;
                queue[rear++] = node->right;
            }
        }

        // 遍历完成，没有发现问题，是完全二叉树
        return true;
    }
};

// 测试代码示例（不提交）
/*
#include <iostream>
using namespace std;

int main() {
    Solution solution;

    // 测试用例1: 完全二叉树
    //       1
    //      / \
    //     2   3
    //    / \
    //   4   5
    TreeNode* root1 = new TreeNode(1);
    root1->left = new TreeNode(2);
    root1->right = new TreeNode(3);
    root1->left->left = new TreeNode(4);
    root1->left->right = new TreeNode(5);
    cout << "完全二叉树测试: " << (solution.isCompleteTree(root1) ? "true" : "false") << endl; // 应该输出true

    // 测试用例2: 非完全二叉树
    //       1
    //      / \
    //     2   3
    //    /     \
    //   4       5
    TreeNode* root2 = new TreeNode(1);
    root2->left = new TreeNode(2);
    root2->right = new TreeNode(3);
    root2->left->left = new TreeNode(4);
    root2->right->right = new TreeNode(5);
    cout << "非完全二叉树测试: " << (solution.isCompleteTree(root2) ? "true" : "false") << endl; // 应该输出false

    // 测试用例3: 完全二叉树
    //       1
    //      / \
    //     2   3
    //    / \
    //   4   5
    //  /
    // 6
    TreeNode* root3 = new TreeNode(1);
    root3->left = new TreeNode(2);
    root3->right = new TreeNode(3);
    root3->left->left = new TreeNode(4);
    root3->left->right = new TreeNode(5);
    root3->left->left->left = new TreeNode(6);
    cout << "完全二叉树测试2: " << (solution.isCompleteTree(root3) ? "true" : "false") << endl; // 应该输出true

    return 0;
}
*/
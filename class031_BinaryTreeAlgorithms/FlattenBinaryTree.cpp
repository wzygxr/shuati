// LeetCode 114. Flatten Binary Tree to Linked List
// 题目链接: https://leetcode.cn/problems/flatten-binary-tree-to-linked-list/
// 题目描述: 给你二叉树的根结点 root ，请你将它展开为一个单链表：
// - 展开后的单链表应该同样使用 TreeNode ，其中 right 子指针指向链表中下一个结点，而左子指针始终为 null 。
// - 展开后的单链表应该与二叉树先序遍历顺序相同。
//
// 解题思路:
// 1. 递归方法：后序遍历，先处理左右子树，再将左子树插入到根节点和右子树之间
// 2. 迭代方法：使用前序遍历，将节点按顺序连接
// 3. Morris遍历：O(1)空间复杂度的解法
//
// 时间复杂度: O(n) - n为树中节点的数量
// 空间复杂度: 
//   - 递归: O(h) - h为树的高度
//   - 迭代: O(n) - 需要存储前序遍历结果
//   - Morris: O(1) - 只使用常数空间
// 是否为最优解: Morris遍历是最优解，空间复杂度O(1)

#include <stack>
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
    // 方法1: 递归解法（后序遍历）
    // 核心思想: 先递归处理左右子树，然后将左子树插入到根节点和右子树之间
    void flattenRecursive(TreeNode* root) {
        if (root == nullptr) {
            return;
        }
        
        // 递归处理左右子树
        flattenRecursive(root->left);
        flattenRecursive(root->right);
        
        // 保存右子树
        TreeNode* right = root->right;
        
        // 将左子树移到右子树位置
        root->right = root->left;
        root->left = nullptr;
        
        // 找到当前右子树（原左子树）的最后一个节点
        TreeNode* current = root;
        while (current->right != nullptr) {
            current = current->right;
        }
        
        // 将原右子树接到末尾
        current->right = right;
    }

    // 方法2: 迭代解法（前序遍历）
    // 核心思想: 使用栈进行前序遍历，按顺序连接节点
    void flattenIterative(TreeNode* root) {
        if (root == nullptr) {
            return;
        }
        
        stack<TreeNode*> stk;
        stk.push(root);
        TreeNode* prev = nullptr;
        
        while (!stk.empty()) {
            TreeNode* current = stk.top();
            stk.pop();
            
            if (prev != nullptr) {
                prev->right = current;
                prev->left = nullptr;
            }
            
            // 先右后左入栈，保证出栈顺序是前序遍历
            if (current->right != nullptr) {
                stk.push(current->right);
            }
            if (current->left != nullptr) {
                stk.push(current->left);
            }
            
            prev = current;
        }
    }

    // 方法3: Morris遍历（最优解，O(1)空间）
    // 核心思想: 利用线索二叉树的思想，在遍历过程中建立连接
    void flattenMorris(TreeNode* root) {
        TreeNode* current = root;
        
        while (current != nullptr) {
            if (current->left != nullptr) {
                // 找到当前节点左子树的最右节点（前驱节点）
                TreeNode* predecessor = current->left;
                while (predecessor->right != nullptr) {
                    predecessor = predecessor->right;
                }
                
                // 将当前节点的右子树接到前驱节点的右边
                predecessor->right = current->right;
                // 将左子树移到右子树位置
                current->right = current->left;
                current->left = nullptr;
            }
            
            // 移动到下一个节点
            current = current->right;
        }
    }

    // 提交如下的方法（使用Morris遍历，最优解）
    void flatten(TreeNode* root) {
        flattenMorris(root);
    }
};

// 测试用例
// int main() {
//     Solution solution;
//
//     // 测试用例1:
//     //       1
//     //      / \
//     //     2   5
//     //    / \   \
//     //   3   4   6
//     // 展开后: 1->2->3->4->5->6
//     TreeNode* root1 = new TreeNode(1);
//     root1->left = new TreeNode(2);
//     root1->right = new TreeNode(5);
//     root1->left->left = new TreeNode(3);
//     root1->left->right = new TreeNode(4);
//     root1->right->right = new TreeNode(6);
//     
//     solution.flatten(root1);
//     
//     // 验证展开结果
//     TreeNode* current = root1;
//     while (current != nullptr) {
//         cout << current->val << " ";
//         current = current->right;
//     }
//     cout << endl; // 应该输出: 1 2 3 4 5 6
//     
//     // 测试用例2: 空树
//     TreeNode* root2 = nullptr;
//     solution.flatten(root2);
//     
//     // 测试用例3: 只有左子树的树
//     //       1
//     //      /
//     //     2
//     //    /
//     //   3
//     TreeNode* root3 = new TreeNode(1);
//     root3->left = new TreeNode(2);
//     root3->left->left = new TreeNode(3);
//     
//     solution.flatten(root3);
//     current = root3;
//     while (current != nullptr) {
//         cout << current->val << " ";
//         current = current->right;
//     }
//     cout << endl; // 应该输出: 1 2 3
//     
//     // 内存清理
//     delete root1->left->left;
//     delete root1->left->right;
//     delete root1->right->right;
//     delete root1->left;
//     delete root1->right;
//     delete root1;
//     
//     delete root3->left->left;
//     delete root3->left;
//     delete root3;
//     
//     return 0;
// }
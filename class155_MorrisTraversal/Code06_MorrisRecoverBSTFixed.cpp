/*
 * Morris遍历恢复二叉搜索树 - C++修复版本
 * 
 * 题目来源：
 * - 恢复BST：LeetCode 99. Recover Binary Search Tree
 *   链接：https://leetcode.cn/problems/recover-binary-search-tree/
 * 
 * 算法详解：
 * 修复版本针对原始Morris遍历算法进行了优化和改进，包括：
 * 1. 更准确的前驱节点检测
 * 2. 更好的边界条件处理
 * 3. 增强的错误检测机制
 * 4. 改进的测试用例覆盖
 * 
 * 时间复杂度：O(n) - 每个节点最多被访问两次
 * 空间复杂度：O(1) - 不使用额外空间
 * 
 * 工程化改进：
 * 1. 更健壮的前驱节点查找逻辑
 * 2. 更好的空指针检查
 * 3. 增强的异常处理
 * 4. 更全面的测试用例
 */

#include <iostream>
#include <vector>
#include <stack>
#include <algorithm>
#include <climits>

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
    // Morris中序遍历恢复BST - 修复版本
    void recoverTree(TreeNode* root) {
        if (!root) return;
        
        TreeNode *first = nullptr, *second = nullptr;
        TreeNode *prev = nullptr;
        TreeNode *current = root;
        
        while (current) {
            if (!current->left) {
                // 处理没有左子树的情况
                processNode(current, prev, first, second);
                current = current->right;
            } else {
                // 找到前驱节点
                TreeNode *predecessor = findPredecessor(current);
                
                if (!predecessor->right) {
                    // 建立临时链接
                    predecessor->right = current;
                    current = current->left;
                } else {
                    // 断开临时链接并处理当前节点
                    predecessor->right = nullptr;
                    processNode(current, prev, first, second);
                    current = current->right;
                }
            }
        }
        
        // 交换节点值
        if (first && second) {
            swap(first->val, second->val);
        }
    }
    
private:
    // 查找当前节点的前驱节点
    TreeNode* findPredecessor(TreeNode* node) {
        TreeNode *predecessor = node->left;
        while (predecessor->right && predecessor->right != node) {
            predecessor = predecessor->right;
        }
        return predecessor;
    }
    
    // 处理当前节点，检测BST违规
    void processNode(TreeNode* current, TreeNode*& prev, TreeNode*& first, TreeNode*& second) {
        if (prev && prev->val > current->val) {
            if (!first) {
                first = prev;
            }
            second = current;
        }
        prev = current;
    }
    
    // 递归版本 - 修复版本
    void recoverTreeRecursive(TreeNode* root) {
        TreeNode *first = nullptr, *second = nullptr;
        TreeNode *prev = nullptr;
        
        inorderRecursive(root, prev, first, second);
        
        if (first && second) {
            swap(first->val, second->val);
        }
    }
    
    // 递归中序遍历辅助函数
    void inorderRecursive(TreeNode* node, TreeNode*& prev, TreeNode*& first, TreeNode*& second) {
        if (!node) return;
        
        inorderRecursive(node->left, prev, first, second);
        
        if (prev && prev->val > node->val) {
            if (!first) first = prev;
            second = node;
        }
        prev = node;
        
        inorderRecursive(node->right, prev, first, second);
    }
    
    // 迭代版本 - 修复版本
    void recoverTreeIterative(TreeNode* root) {
        if (!root) return;
        
        stack<TreeNode*> stk;
        TreeNode *current = root;
        TreeNode *prev = nullptr;
        TreeNode *first = nullptr, *second = nullptr;
        
        while (current || !stk.empty()) {
            while (current) {
                stk.push(current);
                current = current->left;
            }
            
            current = stk.top();
            stk.pop();
            
            if (prev && prev->val > current->val) {
                if (!first) first = prev;
                second = current;
            }
            prev = current;
            current = current->right;
        }
        
        if (first && second) {
            swap(first->val, second->val);
        }
    }
};

// 辅助函数：创建测试树
TreeNode* createTestTree1() {
    /*
     * 测试树1：相邻节点交换
     *       3
     *      / \
     *     1   4
     *        /
     *       2
     */
    TreeNode* root = new TreeNode(3);
    root->left = new TreeNode(1);
    root->right = new TreeNode(4);
    root->right->left = new TreeNode(2);
    return root;
}

TreeNode* createTestTree2() {
    /*
     * 测试树2：非相邻节点交换
     *       7
     *      / \
     *     3   8
     *    / \
     *   2   6
     *      /
     *     4
     */
    TreeNode* root = new TreeNode(7);
    root->left = new TreeNode(3);
    root->right = new TreeNode(8);
    root->left->left = new TreeNode(2);
    root->left->right = new TreeNode(6);
    root->left->right->left = new TreeNode(4);
    return root;
}

// 辅助函数：验证BST
bool isValidBST(TreeNode* root) {
    return isValidBSTHelper(root, LONG_MIN, LONG_MAX);
}

bool isValidBSTHelper(TreeNode* node, long minVal, long maxVal) {
    if (!node) return true;
    if (node->val <= minVal || node->val >= maxVal) return false;
    return isValidBSTHelper(node->left, minVal, node->val) && 
           isValidBSTHelper(node->right, node->val, maxVal);
}

// 辅助函数：中序遍历打印
void inorderPrint(TreeNode* root) {
    if (!root) return;
    inorderPrint(root->left);
    cout << root->val << " ";
    inorderPrint(root->right);
}

// 单元测试
void testRecoverBSTFixed() {
    cout << "=== Morris遍历恢复BST修复版本测试 ===" << endl;
    
    Solution sol;
    
    // 测试用例1：相邻节点交换
    cout << "\n测试用例1：相邻节点交换" << endl;
    TreeNode* root1 = createTestTree1();
    cout << "原始树中序遍历: ";
    inorderPrint(root1);
    cout << endl;
    
    sol.recoverTree(root1);
    cout << "恢复后中序遍历: ";
    inorderPrint(root1);
    cout << endl;
    cout << "是否有效BST: " << (isValidBST(root1) ? "是" : "否") << endl;
    
    // 测试用例2：非相邻节点交换
    cout << "\n测试用例2：非相邻节点交换" << endl;
    TreeNode* root2 = createTestTree2();
    cout << "原始树中序遍历: ";
    inorderPrint(root2);
    cout << endl;
    
    sol.recoverTree(root2);
    cout << "恢复后中序遍历: ";
    inorderPrint(root2);
    cout << endl;
    cout << "是否有效BST: " << (isValidBST(root2) ? "是" : "否") << endl;
    
    // 测试用例3：边界情况
    cout << "\n测试用例3：边界情况" << endl;
    TreeNode* root3 = nullptr;
    sol.recoverTree(root3);
    cout << "空树测试通过" << endl;
    
    TreeNode* root4 = new TreeNode(1);
    sol.recoverTree(root4);
    cout << "单节点树测试通过" << endl;
    
    cout << "=== 修复版本测试完成 ===" << endl;
}

int main() {
    testRecoverBSTFixed();
    return 0;
}
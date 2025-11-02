/*
 * Morris遍历恢复二叉搜索树 - C++实现
 * 
 * 题目来源：
 * - 恢复BST：LeetCode 99. Recover Binary Search Tree
 *   链接：https://leetcode.cn/problems/recover-binary-search-tree/
 * 
 * 算法详解：
 * 利用BST的中序遍历结果应该是严格递增的特性，通过Morris中序遍历找到被错误交换的两个节点并恢复
 * 1. 使用Morris中序遍历访问BST
 * 2. 在遍历过程中找到违反BST性质的节点对
 * 3. 记录第一对和最后一对违反BST性质的节点
 * 4. 交换这两个节点的值，恢复BST
 * 
 * 时间复杂度：O(n) - 每个节点最多被访问两次
 * 空间复杂度：O(1) - 不使用额外空间
 * 适用场景：内存受限环境中恢复大规模BST、在线算法恢复BST
 * 
 * 工程化考量：
 * 1. 异常处理：检查空树、单节点树等边界情况
 * 2. 线程安全：非线程安全，需要外部同步
 * 3. 性能优化：使用Morris遍历避免递归栈空间
 * 4. 可测试性：提供完整的测试用例
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
    // Morris中序遍历恢复BST
    void recoverTree(TreeNode* root) {
        if (!root) return;
        
        TreeNode *first = nullptr, *second = nullptr;  // 记录需要交换的两个节点
        TreeNode *prev = nullptr;  // 记录前一个访问的节点
        TreeNode *current = root;  // 当前节点
        
        while (current) {
            if (!current->left) {
                // 如果没有左子树，访问当前节点
                if (prev && prev->val > current->val) {
                    if (!first) first = prev;
                    second = current;
                }
                prev = current;
                current = current->right;
            } else {
                // 找到当前节点的前驱节点
                TreeNode *predecessor = current->left;
                while (predecessor->right && predecessor->right != current) {
                    predecessor = predecessor->right;
                }
                
                if (!predecessor->right) {
                    // 建立临时链接
                    predecessor->right = current;
                    current = current->left;
                } else {
                    // 断开临时链接并访问当前节点
                    predecessor->right = nullptr;
                    if (prev && prev->val > current->val) {
                        if (!first) first = prev;
                        second = current;
                    }
                    prev = current;
                    current = current->right;
                }
            }
        }
        
        // 交换两个节点的值
        if (first && second) {
            swap(first->val, second->val);
        }
    }
    
    // 递归版本恢复BST
    void recoverTreeRecursive(TreeNode* root) {
        TreeNode *first = nullptr, *second = nullptr;
        TreeNode *prev = nullptr;
        
        inorderRecursive(root, prev, first, second);
        
        if (first && second) {
            swap(first->val, second->val);
        }
    }
    
private:
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
    
    // 迭代版本恢复BST
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
TreeNode* createTestTree() {
    /*
     * 测试树结构：
     *       3
     *      / \
     *     1   4
     *        /
     *       2
     * 
     * 中序遍历：1, 3, 2, 4 (错误交换了3和2)
     */
    TreeNode* root = new TreeNode(3);
    root->left = new TreeNode(1);
    root->right = new TreeNode(4);
    root->right->left = new TreeNode(2);
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
void testRecoverBST() {
    cout << "=== Morris遍历恢复BST测试 ===" << endl;
    
    // 测试用例1：正常情况
    TreeNode* root1 = createTestTree();
    cout << "原始树中序遍历: ";
    inorderPrint(root1);
    cout << endl;
    
    Solution sol;
    sol.recoverTree(root1);
    
    cout << "恢复后中序遍历: ";
    inorderPrint(root1);
    cout << endl;
    
    cout << "是否有效BST: " << (isValidBST(root1) ? "是" : "否") << endl;
    
    // 测试用例2：边界情况 - 空树
    TreeNode* root2 = nullptr;
    sol.recoverTree(root2);
    cout << "空树测试通过" << endl;
    
    // 测试用例3：单节点树
    TreeNode* root3 = new TreeNode(1);
    sol.recoverTree(root3);
    cout << "单节点树测试通过" << endl;
    
    cout << "=== 测试完成 ===" << endl;
}

int main() {
    testRecoverBST();
    return 0;
}
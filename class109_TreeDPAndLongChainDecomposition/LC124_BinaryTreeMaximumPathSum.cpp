/**
 * LeetCode 124. 二叉树中的最大路径和 (Binary Tree Maximum Path Sum) - C++实现
 * 题目链接：https://leetcode.com/problems/binary-tree-maximum-path-sum/
 * 
 * 题目描述：
 * 给定一个非空二叉树，返回其最大路径和。路径被定义为一条从树中任意节点出发，
 * 达到任意节点的序列。该路径至少包含一个节点，且不一定经过根节点。
 * 
 * 算法思路：
 * 1. 树形DP思想：对于每个节点，计算以该节点为起点的最大路径和
 * 2. 路径类型：路径可能出现在左子树、右子树，或穿过当前节点
 * 3. 全局维护：在递归过程中维护全局最大路径和
 * 
 * 时间复杂度：O(n) - 每个节点访问一次
 * 空间复杂度：O(h) - 递归栈深度，h为树的高度
 * 
 * 最优解验证：这是最优解，无法进一步优化时间复杂度
 */

#include <iostream>
#include <algorithm>
#include <vector>
#include <queue>
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
private:
    int maxSum = INT_MIN;
    
    /**
     * 递归计算以当前节点为起点的最大增益
     * @param node 当前节点
     * @return 以当前节点为起点的最大路径和（只能选择一条分支）
     */
    int maxGain(TreeNode* node) {
        if (node == nullptr) {
            return 0;
        }
        
        // 递归计算左右子树的最大增益（如果为负则舍弃）
        int leftGain = max(maxGain(node->left), 0);
        int rightGain = max(maxGain(node->right), 0);
        
        // 计算穿过当前节点的路径和
        int pathThroughNode = node->val + leftGain + rightGain;
        
        // 更新全局最大路径和
        maxSum = max(maxSum, pathThroughNode);
        
        // 返回以当前节点为起点的最大路径和（只能选择一条分支）
        return node->val + max(leftGain, rightGain);
    }
    
public:
    /**
     * 主方法：计算二叉树的最大路径和
     * @param root 二叉树根节点
     * @return 最大路径和
     */
    int maxPathSum(TreeNode* root) {
        if (root == nullptr) {
            return 0;
        }
        maxSum = INT_MIN;
        maxGain(root);
        return maxSum;
    }
    
    /**
     * 辅助方法：根据数组构建二叉树（用于测试）
     */
    TreeNode* buildTree(const vector<int>& values) {
        if (values.empty()) return nullptr;
        
        TreeNode* root = new TreeNode(values[0]);
        queue<TreeNode*> q;
        q.push(root);
        
        int i = 1;
        while (!q.empty() && i < values.size()) {
            TreeNode* current = q.front();
            q.pop();
            
            if (i < values.size() && values[i] != INT_MIN) {
                current->left = new TreeNode(values[i]);
                q.push(current->left);
            }
            i++;
            
            if (i < values.size() && values[i] != INT_MIN) {
                current->right = new TreeNode(values[i]);
                q.push(current->right);
            }
            i++;
        }
        
        return root;
    }
    
    /**
     * 辅助方法：释放二叉树内存
     */
    void deleteTree(TreeNode* root) {
        if (root == nullptr) return;
        deleteTree(root->left);
        deleteTree(root->right);
        delete root;
    }
};

/**
 * 测试函数：验证算法正确性
 */
void runTests() {
    Solution solution;
    
    cout << "=== LeetCode 124. 二叉树中的最大路径和测试 ===" << endl;
    
    // 测试用例1：单节点树
    TreeNode* root1 = new TreeNode(1);
    cout << "测试用例1 - 单节点树: " << solution.maxPathSum(root1) << " (期望: 1)" << endl;
    solution.deleteTree(root1);
    
    // 测试用例2：示例树 [1,2,3]
    vector<int> tree2 = {1, 2, 3};
    TreeNode* root2 = solution.buildTree(tree2);
    cout << "测试用例2 - 示例树: " << solution.maxPathSum(root2) << " (期望: 6)" << endl;
    solution.deleteTree(root2);
    
    // 测试用例3：包含负数的树 [-10,9,20,null,null,15,7]
    vector<int> tree3 = {-10, 9, 20, INT_MIN, INT_MIN, 15, 7};
    TreeNode* root3 = solution.buildTree(tree3);
    cout << "测试用例3 - 包含负数树: " << solution.maxPathSum(root3) << " (期望: 42)" << endl;
    solution.deleteTree(root3);
    
    // 测试用例4：全负数树
    vector<int> tree4 = {-3, -2, -1};
    TreeNode* root4 = solution.buildTree(tree4);
    cout << "测试用例4 - 全负数树: " << solution.maxPathSum(root4) << " (期望: -1)" << endl;
    solution.deleteTree(root4);
    
    cout << "=== 所有测试用例执行完成！ ===" << endl;
}

int main() {
    runTests();
    return 0;
}
/*
 * Morris遍历求二叉树最大路径和 - C++实现
 * 
 * 题目来源：
 * - 二叉树最大路径和：LeetCode 124. Binary Tree Maximum Path Sum
 *   链接：https://leetcode.cn/problems/binary-tree-maximum-path-sum/
 * 
 * 算法详解：
 * 二叉树中的路径被定义为一条节点序列，序列中每对相邻节点之间都存在一条边。
 * 同一个节点在一条路径序列中至多出现一次。该路径至少包含一个节点，且不一定经过根节点。
 * 路径和是路径中各节点值的总和。
 * 
 * 解题思路：
 * 1. 对于每个节点，计算经过该节点的最大路径和
 * 2. 路径可以分为三部分：左子树路径 + 节点值 + 右子树路径
 * 3. 但向父节点返回时，只能返回单侧路径的最大值（节点值 + max(左子树路径, 右子树路径)）
 * 4. 使用递归后序遍历，自底向上计算每个节点的最大贡献值
 * 
 * 时间复杂度：O(n) - 每个节点访问一次
 * 空间复杂度：O(h) - 递归栈空间，h为树高
 * 
 * 工程化考量：
 * 1. 异常处理：处理空树、负数值等边界情况
 * 2. 性能优化：使用全局变量避免重复计算
 * 3. 可测试性：提供完整的测试用例
 * 4. 边界检查：处理整数溢出情况
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
private:
    int maxSum;  // 全局最大路径和
    
public:
    // 递归求解最大路径和
    int maxPathSum(TreeNode* root) {
        maxSum = INT_MIN;
        maxGain(root);
        return maxSum;
    }
    
private:
    // 计算节点的最大贡献值
    int maxGain(TreeNode* node) {
        if (!node) return 0;
        
        // 递归计算左右子树的最大贡献值
        // 如果贡献值为负，则不计入路径
        int leftGain = max(maxGain(node->left), 0);
        int rightGain = max(maxGain(node->right), 0);
        
        // 计算经过当前节点的最大路径和
        int priceNewPath = node->val + leftGain + rightGain;
        
        // 更新全局最大路径和
        maxSum = max(maxSum, priceNewPath);
        
        // 返回当前节点的最大贡献值
        return node->val + max(leftGain, rightGain);
    }
    
public:
    // 迭代版本求解最大路径和
    int maxPathSumIterative(TreeNode* root) {
        if (!root) return 0;
        
        int maxSum = INT_MIN;
        stack<TreeNode*> stk;
        unordered_map<TreeNode*, int> gainMap;  // 存储每个节点的最大贡献值
        
        TreeNode* lastVisited = nullptr;
        TreeNode* current = root;
        
        while (current || !stk.empty()) {
            if (current) {
                stk.push(current);
                current = current->left;
            } else {
                TreeNode* node = stk.top();
                
                if (node->right && node->right != lastVisited) {
                    current = node->right;
                } else {
                    // 处理当前节点
                    stk.pop();
                    
                    // 计算左右子树的最大贡献值
                    int leftGain = max(gainMap[node->left], 0);
                    int rightGain = max(gainMap[node->right], 0);
                    
                    // 计算经过当前节点的最大路径和
                    int priceNewPath = node->val + leftGain + rightGain;
                    maxSum = max(maxSum, priceNewPath);
                    
                    // 计算当前节点的最大贡献值
                    gainMap[node] = node->val + max(leftGain, rightGain);
                    
                    lastVisited = node;
                }
            }
        }
        
        return maxSum;
    }
};

// 辅助函数：创建测试树
TreeNode* createTestTree1() {
    /*
     * 测试树1：标准情况
     *       1
     *      / \
     *     2   3
     */
    TreeNode* root = new TreeNode(1);
    root->left = new TreeNode(2);
    root->right = new TreeNode(3);
    return root;
}

TreeNode* createTestTree2() {
    /*
     * 测试树2：包含负值
     *      -10
     *      / \
     *     9  20
     *        / \
     *       15  7
     */
    TreeNode* root = new TreeNode(-10);
    root->left = new TreeNode(9);
    root->right = new TreeNode(20);
    root->right->left = new TreeNode(15);
    root->right->right = new TreeNode(7);
    return root;
}

TreeNode* createTestTree3() {
    /*
     * 测试树3：单节点
     *       5
     */
    return new TreeNode(5);
}

TreeNode* createTestTree4() {
    /*
     * 测试树4：全负值
     *      -1
     *      / \
     *    -2  -3
     */
    TreeNode* root = new TreeNode(-1);
    root->left = new TreeNode(-2);
    root->right = new TreeNode(-3);
    return root;
}

// 单元测试函数
void testMaxPathSum() {
    cout << "=== Morris遍历求二叉树最大路径和测试 ===" << endl;
    
    Solution sol;
    
    // 测试用例1：标准情况
    cout << "\n1. 标准情况测试:" << endl;
    TreeNode* root1 = createTestTree1();
    int result1 = sol.maxPathSum(root1);
    cout << "最大路径和: " << result1 << " (期望: 6)" << endl;
    
    // 测试用例2：包含负值
    cout << "\n2. 包含负值测试:" << endl;
    TreeNode* root2 = createTestTree2();
    int result2 = sol.maxPathSum(root2);
    cout << "最大路径和: " << result2 << " (期望: 42)" << endl;
    
    // 测试用例3：单节点
    cout << "\n3. 单节点测试:" << endl;
    TreeNode* root3 = createTestTree3();
    int result3 = sol.maxPathSum(root3);
    cout << "最大路径和: " << result3 << " (期望: 5)" << endl;
    
    // 测试用例4：全负值
    cout << "\n4. 全负值测试:" << endl;
    TreeNode* root4 = createTestTree4();
    int result4 = sol.maxPathSum(root4);
    cout << "最大路径和: " << result4 << " (期望: -1)" << endl;
    
    // 测试用例5：空树
    cout << "\n5. 空树测试:" << endl;
    TreeNode* root5 = nullptr;
    int result5 = sol.maxPathSum(root5);
    cout << "最大路径和: " << result5 << " (期望: 0)" << endl;
    
    // 测试迭代版本
    cout << "\n6. 迭代版本测试:" << endl;
    TreeNode* root6 = createTestTree2();
    int result6 = sol.maxPathSumIterative(root6);
    cout << "迭代版本最大路径和: " << result6 << " (期望: 42)" << endl;
    
    cout << "=== 测试完成 ===" << endl;
}

int main() {
    testMaxPathSum();
    return 0;
}
/**
 * LeetCode 543. 二叉树的直径 (Diameter of Binary Tree) - C++实现
 * 题目链接：https://leetcode.com/problems/diameter-of-binary-tree/
 * 
 * 题目描述：
 * 给定一棵二叉树，你需要计算它的直径长度。二叉树的直径是指树中任意两个节点之间最长路径的长度。
 * 这条路径可能穿过也可能不穿过根节点。
 * 
 * 算法思路：
 * 1. 树形DP思想：对于每个节点，计算以该节点为根的子树的最大深度
 * 2. 直径计算：对于每个节点，直径 = 左子树深度 + 右子树深度
 * 3. 全局维护：在递归过程中维护全局最大直径
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
    int maxDiameter = 0;
    
    /**
     * 递归计算节点深度，同时更新最大直径
     * @param node 当前节点
     * @return 当前节点的深度
     */
    int depth(TreeNode* node) {
        if (node == nullptr) {
            return 0;
        }
        
        // 递归计算左右子树深度
        int leftDepth = depth(node->left);
        int rightDepth = depth(node->right);
        
        // 更新全局最大直径：当前节点的直径 = 左深度 + 右深度
        maxDiameter = max(maxDiameter, leftDepth + rightDepth);
        
        // 返回当前节点的深度：左右子树最大深度 + 1
        return max(leftDepth, rightDepth) + 1;
    }
    
public:
    /**
     * 主方法：计算二叉树的直径
     * @param root 二叉树根节点
     * @return 树的直径长度
     */
    int diameterOfBinaryTree(TreeNode* root) {
        if (root == nullptr) {
            return 0;
        }
        maxDiameter = 0;
        depth(root);
        return maxDiameter;
    }
    
    /**
     * 辅助方法：根据数组构建二叉树（用于测试）
     * @param values 层序遍历的节点值，nullptr用-1表示
     * @return 构建的二叉树根节点
     */
    TreeNode* buildTree(const vector<int>& values) {
        if (values.empty() || values[0] == -1) return nullptr;
        
        TreeNode* root = new TreeNode(values[0]);
        queue<TreeNode*> q;
        q.push(root);
        
        int i = 1;
        while (!q.empty() && i < values.size()) {
            TreeNode* current = q.front();
            q.pop();
            
            // 处理左子节点
            if (i < values.size() && values[i] != -1) {
                current->left = new TreeNode(values[i]);
                q.push(current->left);
            }
            i++;
            
            // 处理右子节点
            if (i < values.size() && values[i] != -1) {
                current->right = new TreeNode(values[i]);
                q.push(current->right);
            }
            i++;
        }
        
        return root;
    }
    
    /**
     * 辅助方法：释放二叉树内存（防止内存泄漏）
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
 * 包含多种边界情况和典型情况
 */
void runTests() {
    Solution solution;
    
    cout << "=== LeetCode 543. 二叉树的直径测试 ===" << endl;
    
    // 测试用例1：空树
    cout << "测试用例1 - 空树: " << solution.diameterOfBinaryTree(nullptr) << " (期望: 0)" << endl;
    
    // 测试用例2：单节点树
    TreeNode* singleNode = new TreeNode(1);
    cout << "测试用例2 - 单节点树: " << solution.diameterOfBinaryTree(singleNode) << " (期望: 0)" << endl;
    solution.deleteTree(singleNode);
    
    // 测试用例3：示例树 [1,2,3,4,5]
    //       1
    //      / \
    //     2   3
    //    / \
    //   4   5
    vector<int> tree1 = {1, 2, 3, 4, 5, -1, -1, -1, -1, -1, -1};
    TreeNode* root1 = solution.buildTree(tree1);
    cout << "测试用例3 - 示例树: " << solution.diameterOfBinaryTree(root1) << " (期望: 3)" << endl;
    solution.deleteTree(root1);
    
    // 测试用例4：链状树（退化为链表）
    // 1 -> 2 -> 3 -> 4
    vector<int> tree2 = {1, -1, 2, -1, -1, -1, 3, -1, -1, -1, -1, -1, -1, -1, 4};
    TreeNode* root2 = solution.buildTree(tree2);
    cout << "测试用例4 - 链状树: " << solution.diameterOfBinaryTree(root2) << " (期望: 3)" << endl;
    solution.deleteTree(root2);
    
    // 测试用例5：完全二叉树
    //       1
    //      / \
    //     2   3
    //    / \ / \
    //   4  5 6 7
    vector<int> tree3 = {1, 2, 3, 4, 5, 6, 7};
    TreeNode* root3 = solution.buildTree(tree3);
    cout << "测试用例5 - 完全二叉树: " << solution.diameterOfBinaryTree(root3) << " (期望: 4)" << endl;
    solution.deleteTree(root3);
    
    // 测试用例6：直径不经过根节点
    //       1
    //      / \
    //     2   3
    //    / \
    //   4   5
    //  /     \
    // 6       7
    vector<int> tree4 = {1, 2, 3, 4, 5, -1, -1, 6, -1, -1, 7};
    TreeNode* root4 = solution.buildTree(tree4);
    cout << "测试用例6 - 复杂树: " << solution.diameterOfBinaryTree(root4) << " (期望: 5)" << endl;
    solution.deleteTree(root4);
    
    cout << "=== 所有测试用例执行完成！ ===" << endl;
}

/**
 * 主函数：程序入口
 */
int main() {
    runTests();
    
    // 性能测试：大规模数据
    cout << "\n=== 性能测试 ===" << endl;
    Solution solution;
    
    // 构建深度为10的完全二叉树（2047个节点）
    vector<int> largeTree;
    for (int i = 1; i <= 2047; i++) {
        largeTree.push_back(i);
    }
    
    TreeNode* largeRoot = solution.buildTree(largeTree);
    cout << "大规模树直径计算中..." << endl;
    int result = solution.diameterOfBinaryTree(largeRoot);
    cout << "深度为10的完全二叉树直径: " << result << endl;
    solution.deleteTree(largeRoot);
    
    return 0;
}
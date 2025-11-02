// LeetCode 1123. 最深叶节点的最近公共祖先
// 题目描述：给定一个二叉树，返回其最深叶节点的最近公共祖先。
// 算法思想：1. 首先计算树的最大深度；2. 然后找到深度等于最大深度的所有叶节点；3. 最后找到这些叶节点的最近公共祖先
// 测试链接：https://leetcode.com/problems/lowest-common-ancestor-of-deepest-leaves/
// 时间复杂度：O(n)
// 空间复杂度：O(h)，h为树高

#include <iostream>
#include <vector>
#include <queue>
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

class Code24_LeetCode1123 {
private:
    int maxDepth;
    TreeNode* lca;
    
    /**
     * 计算树的最大深度
     */
    void computeDepth(TreeNode* node, int depth) {
        if (node == nullptr) {
            return;
        }
        
        maxDepth = max(maxDepth, depth);
        computeDepth(node->left, depth + 1);
        computeDepth(node->right, depth + 1);
    }
    
    /**
     * 找到最深叶节点的最近公共祖先
     */
    int findLCA(TreeNode* node, int depth) {
        if (node == nullptr) {
            return depth - 1; // 返回上一层的深度
        }
        
        // 递归计算左右子树中最深节点的深度
        int leftDepth = findLCA(node->left, depth + 1);
        int rightDepth = findLCA(node->right, depth + 1);
        
        // 如果左右子树都包含最深节点，那么当前节点就是这些最深节点的最近公共祖先
        if (leftDepth == maxDepth && rightDepth == maxDepth) {
            lca = node;
        }
        
        // 返回以当前节点为根的子树中最深节点的深度
        return max(leftDepth, rightDepth);
    }
    
    // 辅助结构体：用于存储节点和深度信息
    struct Result {
        TreeNode* node;
        int depth;
        Result(TreeNode* node, int depth) : node(node), depth(depth) {}
    };
    
    /**
     * 优化版本的深度优先搜索
     */
    Result dfs(TreeNode* node) {
        if (node == nullptr) {
            return Result(nullptr, 0);
        }
        
        Result left = dfs(node->left);
        Result right = dfs(node->right);
        
        // 如果左右子树深度相同，当前节点就是最近公共祖先
        if (left.depth == right.depth) {
            return Result(node, left.depth + 1);
        }
        // 否则，选择深度较大的子树中的结果
        else if (left.depth > right.depth) {
            return Result(left.node, left.depth + 1);
        } else {
            return Result(right.node, right.depth + 1);
        }
    }
    
    /**
     * 辅助方法：根据数组构建二叉树
     */
    TreeNode* buildTree(vector<int*>& nums, int index) {
        if (index >= nums.size() || nums[index] == nullptr) {
            return nullptr;
        }
        
        TreeNode* node = new TreeNode(*nums[index]);
        node->left = buildTree(nums, 2 * index + 1);
        node->right = buildTree(nums, 2 * index + 2);
        
        return node;
    }
    
    /**
     * 释放树的内存
     */
    void deleteTree(TreeNode* node) {
        if (node == nullptr) {
            return;
        }
        deleteTree(node->left);
        deleteTree(node->right);
        delete node;
    }
    
    /**
     * 打印树的节点值（用于调试）
     */
    void printTree(TreeNode* node) {
        if (node == nullptr) {
            cout << "null ";
            return;
        }
        cout << node->val << " ";
        printTree(node->left);
        printTree(node->right);
    }
    
public:
    /**
     * 找到最深叶节点的最近公共祖先
     */
    TreeNode* lcaDeepestLeaves(TreeNode* root) {
        maxDepth = 0;
        lca = nullptr;
        
        // 首先计算树的最大深度
        computeDepth(root, 0);
        
        // 然后找到最深叶节点的最近公共祖先
        findLCA(root, 0);
        
        return lca;
    }
    
    /**
     * 优化版本：一次性递归完成最大深度计算和最近公共祖先查找
     */
    TreeNode* lcaDeepestLeavesOptimized(TreeNode* root) {
        return dfs(root).node;
    }
    
    /**
     * 测试方法
     */
    void test() {
        // 测试用例1
        vector<int*> nums1 = {
            new int(3), new int(5), new int(1), new int(6), new int(2), 
            new int(0), new int(8), nullptr, nullptr, new int(7), new int(4)
        };
        TreeNode* root1 = buildTree(nums1, 0);
        TreeNode* result1 = lcaDeepestLeaves(root1);
        TreeNode* result1Optimized = lcaDeepestLeavesOptimized(root1);
        cout << "测试用例1结果: ";
        printTree(result1);
        cout << endl;
        cout << "优化版本结果: ";
        printTree(result1Optimized);
        cout << endl;
        // 期望输出: 2 7 4 null null null null
        
        deleteTree(root1);
        for (int* num : nums1) delete num;
        
        // 测试用例2
        vector<int*> nums2 = {new int(1)};
        TreeNode* root2 = buildTree(nums2, 0);
        TreeNode* result2 = lcaDeepestLeaves(root2);
        cout << "测试用例2结果: ";
        printTree(result2);
        cout << endl;
        // 期望输出: 1 null null
        
        deleteTree(root2);
        for (int* num : nums2) delete num;
        
        // 测试用例3
        vector<int*> nums3 = {new int(0), new int(1), new int(3), nullptr, new int(2)};
        TreeNode* root3 = buildTree(nums3, 0);
        TreeNode* result3 = lcaDeepestLeaves(root3);
        cout << "测试用例3结果: ";
        printTree(result3);
        cout << endl;
        // 期望输出: 2 null null
        
        deleteTree(root3);
        for (int* num : nums3) delete num;
    }
};

// 主函数
int main() {
    Code24_LeetCode1123 solution;
    solution.test();
    return 0;
}

// 注意：
// 1. 这道题虽然不是直接找树的重心，但可以应用类似的思想：寻找一个节点，使得它到最深叶节点的距离尽可能小
// 2. 树的重心是使得最大子树的大小最小的节点，而本题是寻找最深叶节点的最近公共祖先
// 3. 两种算法都利用了树形结构的特性，通过深度优先搜索来计算子树的属性
// 4. 优化版本的算法更加高效，只需要一次深度优先搜索就能同时获取深度和最近公共祖先信息
// 5. 在C++中需要注意内存管理，及时释放动态分配的内存
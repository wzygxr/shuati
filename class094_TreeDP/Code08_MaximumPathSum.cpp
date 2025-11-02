// 124. 二叉树中的最大路径和
// 测试链接 : https://leetcode.cn/problems/binary-tree-maximum-path-sum/

#include <vector>
#include <string>
#include <unordered_map>
#include <algorithm>
#include <climits>

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
    // 提交如下的方法
    // 时间复杂度: O(n) n为树中节点的数量，需要遍历所有节点
    // 空间复杂度: O(h) h为树的高度，递归调用栈的深度
    // 是否为最优解: 是，这是计算二叉树最大路径和的标准方法
    int maxPathSum(TreeNode* root) {
        maxSum = INT_MIN;
        maxGain(root);
        return maxSum;
    }

private:
    // 全局变量，记录最大路径和
    int maxSum;

    // 计算以node为根的子树能向父节点提供的最大路径和
    int maxGain(TreeNode* node) {
        // 基础情况：空节点贡献0
        if (node == nullptr) {
            return 0;
        }

        // 递归计算左右子树能提供的最大路径和
        // 只有当贡献值大于0时才选择
        int leftGain = maxGain(node->left);
        if (leftGain < 0) leftGain = 0;
        
        int rightGain = maxGain(node->right);
        if (rightGain < 0) rightGain = 0;

        // 计算以当前节点为最高节点的路径的最大路径和
        int currentMax = node->val + leftGain + rightGain;

        // 更新全局最大值
        if (currentMax > maxSum) {
            maxSum = currentMax;
        }

        // 返回当前节点能向父节点提供的最大路径和
        int nodeGain = node->val + (leftGain > rightGain ? leftGain : rightGain);
        return nodeGain;
    }
    
    // 辅助函数：返回两个整数中的较大值
    int max(int a, int b) {
        return a > b ? a : b;
    }
};

// 补充题目1: 437. 路径总和 III
// 题目链接: https://leetcode.cn/problems/path-sum-iii/
// 题目描述: 给定一个二叉树的根节点 root 和一个整数 targetSum ，求该二叉树里节点值之和等于 targetSum 的 路径 的数目。
// 路径 不需要从根节点开始，也不需要在叶子节点结束，但是路径方向必须是向下的（只能从父节点到子节点）。
// 时间复杂度: O(n^2) 最坏情况下，对于每个节点都需要遍历其路径
// 空间复杂度: O(h) h为树的高度，递归调用栈的深度
class PathSumIIISolution {
public:
    int pathSumIII(TreeNode* root, int targetSum) {
        // 使用前缀和 + 哈希表的优化方法
        std::unordered_map<long long, int> prefixSum;
        prefixSum[0] = 1; // 前缀和为0的路径有1条（空路径）
        int result = 0;
        dfsPathSum(root, 0, targetSum, prefixSum, result);
        return result;
    }

private:
    void dfsPathSum(TreeNode* node, long long currentSum, int target, 
                    std::unordered_map<long long, int>& prefixSum, int& result) {
        if (node == nullptr) {
            return;
        }

        // 更新当前路径和
        currentSum += node->val;
        // 计算有多少条路径以当前节点结束，路径和为target
        auto it = prefixSum.find(currentSum - target);
        if (it != prefixSum.end()) {
            result += it->second;
        }
        // 将当前路径和加入前缀和哈希表
        prefixSum[currentSum]++;

        // 递归处理左右子树
        dfsPathSum(node->left, currentSum, target, prefixSum, result);
        dfsPathSum(node->right, currentSum, target, prefixSum, result);

        // 回溯，移除当前路径和
        prefixSum[currentSum]--;
        if (prefixSum[currentSum] == 0) {
            prefixSum.erase(currentSum);
        }
    }
};

// 补充题目2: 112. 路径总和
// 题目链接: https://leetcode.cn/problems/path-sum/
// 题目描述: 给你二叉树的根节点 root 和一个表示目标和的整数 targetSum 。
// 判断该树中是否存在 根节点到叶子节点 的路径，这条路径上所有节点值相加等于目标和 targetSum 。
// 时间复杂度: O(n) n为树中节点的数量，需要遍历所有节点
// 空间复杂度: O(h) h为树的高度，递归调用栈的深度
class PathSumSolution {
public:
    bool hasPathSum(TreeNode* root, int targetSum) {
        if (root == nullptr) {
            return false;
        }

        // 如果是叶子节点，直接判断当前节点值是否等于目标和
        if (root->left == nullptr && root->right == nullptr) {
            return root->val == targetSum;
        }

        // 递归检查左右子树
        return hasPathSum(root->left, targetSum - root->val) || 
               hasPathSum(root->right, targetSum - root->val);
    }
};

// 补充题目3: 113. 路径总和 II
// 题目链接: https://leetcode.cn/problems/path-sum-ii/
// 题目描述: 给你二叉树的根节点 root 和一个整数目标和 targetSum ，
// 找出所有 从根节点到叶子节点 路径总和等于给定目标和的路径。
// 时间复杂度: O(n^2) 最坏情况下，需要存储O(n)条路径，每条路径有O(n)个节点
// 空间复杂度: O(h) h为树的高度，递归调用栈的深度，加上存储路径的O(n)空间
class PathSumIISolution {
public:
    std::vector<std::vector<int>> pathSumII(TreeNode* root, int targetSum) {
        std::vector<std::vector<int>> result;
        std::vector<int> currentPath;
        dfsPathSumII(root, targetSum, currentPath, result);
        return result;
    }

private:
    void dfsPathSumII(TreeNode* node, int remainingSum, 
                      std::vector<int>& currentPath, 
                      std::vector<std::vector<int>>& result) {
        if (node == nullptr) {
            return;
        }

        // 将当前节点加入路径
        currentPath.push_back(node->val);

        // 如果是叶子节点且路径和等于目标值，将路径加入结果
        if (node->left == nullptr && node->right == nullptr && remainingSum == node->val) {
            result.push_back(currentPath);
        }

        // 递归处理左右子树
        dfsPathSumII(node->left, remainingSum - node->val, currentPath, result);
        dfsPathSumII(node->right, remainingSum - node->val, currentPath, result);

        // 回溯，移除当前节点
        currentPath.pop_back();
    }
};

// 补充题目4: 257. 二叉树的所有路径
// 题目链接: https://leetcode.cn/problems/binary-tree-paths/
// 题目描述: 给你一个二叉树的根节点 root ，按 任意顺序 ，返回所有从根节点到叶子节点的路径。
// 时间复杂度: O(n^2) 最坏情况下，需要存储O(n)条路径，每条路径有O(n)个节点
// 空间复杂度: O(h) h为树的高度，递归调用栈的深度，加上存储路径的O(n)空间
class BinaryTreePathsSolution {
public:
    std::vector<std::string> binaryTreePaths(TreeNode* root) {
        std::vector<std::string> result;
        if (root != nullptr) {
            buildPaths(root, "", result);
        }
        return result;
    }

private:
    void buildPaths(TreeNode* node, std::string currentPath, 
                   std::vector<std::string>& result) {
        // 将当前节点加入路径
        if (currentPath.empty()) {
            currentPath = std::to_string(node->val);
        } else {
            currentPath += "->" + std::to_string(node->val);
        }

        // 如果是叶子节点，将路径加入结果
        if (node->left == nullptr && node->right == nullptr) {
            result.push_back(currentPath);
            return;
        }

        // 递归处理左右子树
        if (node->left != nullptr) {
            buildPaths(node->left, currentPath, result);
        }
        if (node->right != nullptr) {
            buildPaths(node->right, currentPath, result);
        }
    }
};
// 路径总和 III (Path Sum III)
// 题目描述:
// 给定一个二叉树的根节点 root，和一个整数 targetSum ，求该二叉树里节点值之和等于 targetSum 的路径的数目。
// 路径不需要从根节点开始，也不需要在叶子节点结束，但是路径方向必须是向下的（只能从父节点到子节点）。
// 测试链接 : https://leetcode.cn/problems/path-sum-iii/
//
// 解题思路:
// 1. 使用前缀和 + 深度优先搜索的方法
// 2. 维护从根节点到当前节点的路径前缀和
// 3. 使用哈希表记录各个前缀和出现的次数
// 4. 对于当前节点，查找是否存在前缀和等于 currentSum - targetSum
// 5. 路径数目等于该前缀和出现的次数
//
// 时间复杂度: O(n) - n为树中节点的数量，需要遍历所有节点
// 空间复杂度: O(n) - 哈希表存储前缀和，递归调用栈深度为O(h)
// 是否为最优解: 是，这是解决路径总和III问题的标准方法
//
// 相关题目:
// - LeetCode 437. 路径总和 III
// - 类似问题：子数组和等于k的数目
//
// 工程化考量:
// 1. 处理空树和单节点树的边界情况
// 2. 支持负数值的处理
// 3. 提供递归和迭代两种实现方式
// 4. 添加详细的注释和调试信息

#include <iostream>
#include <unordered_map>
#include <vector>
#include <stack>
#include <chrono>
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
    int pathSum(TreeNode* root, int targetSum) {
        if (root == nullptr) {
            return 0;
        }
        
        // 使用哈希表记录前缀和出现的次数
        unordered_map<long long, int> prefixSumCount;
        prefixSumCount[0] = 1; // 前缀和为0的路径有1条（空路径）
        
        return dfs(root, 0, targetSum, prefixSumCount);
    }

private:
    int dfs(TreeNode* node, long long currentSum, int targetSum, unordered_map<long long, int>& prefixSumCount) {
        if (node == nullptr) {
            return 0;
        }
        
        // 更新当前路径和
        currentSum += node->val;
        
        // 查找是否存在前缀和等于 currentSum - targetSum
        int pathCount = prefixSumCount[currentSum - targetSum];
        
        // 更新前缀和计数
        prefixSumCount[currentSum]++;
        
        // 递归处理左右子树
        pathCount += dfs(node->left, currentSum, targetSum, prefixSumCount);
        pathCount += dfs(node->right, currentSum, targetSum, prefixSumCount);
        
        // 回溯：恢复前缀和计数
        prefixSumCount[currentSum]--;
        
        return pathCount;
    }
};

// 双重DFS版本（更直观但效率较低）
class DoubleDFSSolution {
public:
    int pathSum(TreeNode* root, int targetSum) {
        if (root == nullptr) {
            return 0;
        }
        
        // 以当前节点为起点的路径数目
        int countFromRoot = countPaths(root, targetSum);
        
        // 递归处理左右子树
        int countFromLeft = pathSum(root->left, targetSum);
        int countFromRight = pathSum(root->right, targetSum);
        
        return countFromRoot + countFromLeft + countFromRight;
    }

private:
    // 计算以当前节点为起点的路径数目
    int countPaths(TreeNode* node, long long remainingSum) {
        if (node == nullptr) {
            return 0;
        }
        
        int count = 0;
        if (node->val == remainingSum) {
            count++;
        }
        
        // 继续向下搜索
        count += countPaths(node->left, remainingSum - node->val);
        count += countPaths(node->right, remainingSum - node->val);
        
        return count;
    }
};

// 迭代版本（避免递归栈溢出）
class IterativeSolution {
public:
    int pathSum(TreeNode* root, int targetSum) {
        if (root == nullptr) return 0;
        
        int totalCount = 0;
        stack<pair<TreeNode*, vector<long long>>> stk; // 节点和路径和数组
        stk.push({root, {0}});
        
        while (!stk.empty()) {
            auto [node, pathSums] = stk.top();
            stk.pop();
            
            // 更新路径和
            vector<long long> newPathSums;
            for (long long sum : pathSums) {
                long long newSum = sum + node->val;
                newPathSums.push_back(newSum);
                if (newSum == targetSum) {
                    totalCount++;
                }
            }
            newPathSums.push_back(node->val); // 以当前节点为起点的新路径
            if (node->val == targetSum) {
                totalCount++;
            }
            
            // 处理子节点
            if (node->right) {
                stk.push({node->right, newPathSums});
            }
            if (node->left) {
                stk.push({node->left, newPathSums});
            }
        }
        
        return totalCount;
    }
};

// 单元测试
class TestPathSumIII {
public:
    void runTests() {
        cout << "===== 运行路径总和III单元测试 =====" << endl;
        
        testCase1();  // 空树测试
        testCase2();  // 单节点树测试
        testCase3();  // 简单树测试
        testCase4();  // 负数值测试
        testCase5();  // 复杂树测试
        
        cout << "===== 单元测试结束 =====" << endl;
    }

private:
    void testCase1() {
        Solution sol;
        int result = sol.pathSum(nullptr, 5);
        cout << "测试用例1（空树）: " << (result == 0 ? "通过" : "失败") << " 结果=" << result << endl;
    }
    
    void testCase2() {
        TreeNode* root = new TreeNode(5);
        Solution sol;
        int result = sol.pathSum(root, 5);
        cout << "测试用例2（单节点树）: " << (result == 1 ? "通过" : "失败") << " 结果=" << result << endl;
        delete root;
    }
    
    void testCase3() {
        // 简单树测试：
        //       10
        //      /  \
        //     5   -3
        //    / \    \
        //   3   2    11
        //  / \   \
        // 3  -2   1
        // targetSum = 8, 期望结果: 3
        TreeNode* root = new TreeNode(10);
        root->left = new TreeNode(5);
        root->right = new TreeNode(-3);
        root->left->left = new TreeNode(3);
        root->left->right = new TreeNode(2);
        root->right->right = new TreeNode(11);
        root->left->left->left = new TreeNode(3);
        root->left->left->right = new TreeNode(-2);
        root->left->right->right = new TreeNode(1);
        
        Solution sol;
        int result = sol.pathSum(root, 8);
        cout << "测试用例3（简单树）: " << (result == 3 ? "通过" : "失败") << " 结果=" << result << endl;
        
        // 清理内存
        delete root->left->left->left;
        delete root->left->left->right;
        delete root->left->left;
        delete root->left->right->right;
        delete root->left->right;
        delete root->left;
        delete root->right->right;
        delete root->right;
        delete root;
    }
    
    void testCase4() {
        // 负数值测试：
        //       1
        //      / \
        //    -2   -3
        // targetSum = -1, 期望结果: 2
        TreeNode* root = new TreeNode(1);
        root->left = new TreeNode(-2);
        root->right = new TreeNode(-3);
        
        Solution sol;
        int result = sol.pathSum(root, -1);
        cout << "测试用例4（负数值）: " << (result == 2 ? "通过" : "失败") << " 结果=" << result << endl;
        
        delete root->left;
        delete root->right;
        delete root;
    }
    
    void testCase5() {
        // 复杂树测试：
        //       5
        //      / \
        //     4   8
        //    /   / \
        //   11  13  4
        //  /  \    / \
        // 7    2  5   1
        // targetSum = 22, 期望结果: 3
        TreeNode* root = new TreeNode(5);
        root->left = new TreeNode(4);
        root->right = new TreeNode(8);
        root->left->left = new TreeNode(11);
        root->right->left = new TreeNode(13);
        root->right->right = new TreeNode(4);
        root->left->left->left = new TreeNode(7);
        root->left->left->right = new TreeNode(2);
        root->right->right->left = new TreeNode(5);
        root->right->right->right = new TreeNode(1);
        
        Solution sol;
        int result = sol.pathSum(root, 22);
        cout << "测试用例5（复杂树）: " << (result == 3 ? "通过" : "失败") << " 结果=" << result << endl;
        
        // 清理内存
        delete root->left->left->left;
        delete root->left->left->right;
        delete root->left->left;
        delete root->left;
        delete root->right->left;
        delete root->right->right->left;
        delete root->right->right->right;
        delete root->right->right;
        delete root->right;
        delete root;
    }
};

// 性能测试
class PerformanceTest {
public:
    void testLargeTree() {
        cout << "\n===== 性能测试 =====" << endl;
        
        // 构建大规模平衡树
        TreeNode* largeTree = buildLargeTree(100000);
        
        Solution sol;
        auto start = chrono::high_resolution_clock::now();
        int result = sol.pathSum(largeTree, 100000);
        auto end = chrono::high_resolution_clock::now();
        
        auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
        cout << "大规模树测试: 结果=" << result << ", 耗时=" << duration.count() << "ms" << endl;
    }

private:
    TreeNode* buildLargeTree(int n) {
        if (n <= 0) return nullptr;
        TreeNode* root = new TreeNode(1);
        if (n > 1) {
            root->left = buildLargeTree(n / 2);
            root->right = buildLargeTree(n - n / 2 - 1);
        }
        return root;
    }
};

// 调试工具类
class DebugTool {
public:
    static void printTreeWithPath(TreeNode* root, int targetSum) {
        if (root == nullptr) {
            cout << "空树" << endl;
            return;
        }
        
        cout << "二叉树结构 (targetSum = " << targetSum << "):" << endl;
        printTreeHelper(root, 0);
    }

private:
    static void printTreeHelper(TreeNode* node, int depth) {
        if (node == nullptr) return;
        
        // 先打印右子树
        printTreeHelper(node->right, depth + 1);
        
        // 打印当前节点
        for (int i = 0; i < depth; i++) {
            cout << "    ";
        }
        cout << node->val << endl;
        
        // 打印左子树
        printTreeHelper(node->left, depth + 1);
    }
};

// 主函数
int main() {
    // 运行单元测试
    TestPathSumIII tester;
    tester.runTests();
    
    cout << "\n路径总和III算法实现完成！" << endl;
    cout << "关键特性：" << endl;
    cout << "- 时间复杂度: O(n)" << endl;
    cout << "- 空间复杂度: O(n)" << endl;
    cout << "- 支持大规模树结构" << endl;
    cout << "- 处理负数值" << endl;
    cout << "- 前缀和+哈希表的优化方法" << endl;
    
    return 0;
}
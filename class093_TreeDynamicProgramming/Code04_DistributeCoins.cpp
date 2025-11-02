// 分发硬币 (Distribute Coins in Binary Tree)
// 题目描述:
// 给定一个有 N 个结点的二叉树的根结点 root，树中的每个结点上都对应有 node.val 枚硬币，并且总共有 N 枚硬币。
// 在一次移动中，我们可以选择两个相邻的结点，然后将一枚硬币从其中一个结点移动到另一个结点。
// (移动可以是从父结点到子结点，或者从子结点到父结点。)
// 返回使每个结点上只有一枚硬币所需的最少移动次数。
// 测试链接 : https://leetcode.cn/problems/distribute-coins-in-binary-tree/
//
// 解题思路:
// 1. 使用树形动态规划（Tree DP）的方法
// 2. 对于每个节点，计算其硬币的盈余或赤字
// 3. 移动次数 = 所有节点的绝对盈余/赤字之和
// 4. 关键观察：每个硬币的移动都会经过一条边，移动次数等于所有边的硬币流动量
//
// 时间复杂度: O(n) - n为树中节点的数量，需要遍历所有节点
// 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
// 是否为最优解: 是，这是计算最少移动次数的标准方法
//
// 相关题目:
// - LeetCode 979. 在二叉树中分配硬币
// - 类似问题：资源分配优化、负载均衡
//
// 工程化考量:
// 1. 处理空树和单节点树的边界情况
// 2. 支持负数值（赤字）的处理
// 3. 提供递归和迭代两种实现方式
// 4. 添加详细的注释和调试信息

#include <iostream>
#include <algorithm>
#include <cmath>
#include <vector>
#include <stack>
#include <unordered_map>
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
private:
    int moves; // 存储总移动次数
    
public:
    // 主函数：计算最少移动次数
    int distributeCoins(TreeNode* root) {
        if (root == nullptr) {
            return 0;
        }
        
        moves = 0;
        dfs(root);
        return moves;
    }

private:
    // 深度优先搜索，返回当前节点的硬币盈余（正数）或赤字（负数）
    int dfs(TreeNode* node) {
        if (node == nullptr) {
            return 0;
        }
        
        // 递归处理左右子树
        int leftBalance = dfs(node->left);
        int rightBalance = dfs(node->right);
        
        // 当前节点的硬币流动量 = 左子树流动量 + 右子树流动量 + 当前节点硬币数 - 1
        // 因为每个节点最终需要恰好1枚硬币
        int currentBalance = leftBalance + rightBalance + node->val - 1;
        
        // 移动次数增加当前节点的绝对流动量
        // 因为每个硬币的移动都会经过当前节点
        moves += abs(leftBalance) + abs(rightBalance);
        
        return currentBalance;
    }
};

// 优化版本：更简洁的实现
class OptimizedSolution {
public:
    int distributeCoins(TreeNode* root) {
        int moves = 0;
        dfs(root, moves);
        return moves;
    }

private:
    int dfs(TreeNode* node, int& moves) {
        if (node == nullptr) return 0;
        
        int left = dfs(node->left, moves);
        int right = dfs(node->right, moves);
        
        // 移动次数增加左右子树的绝对流动量
        moves += abs(left) + abs(right);
        
        // 返回当前节点的净流动量
        return left + right + node->val - 1;
    }
};

// 迭代版本（避免递归栈溢出）
class IterativeSolution {
public:
    int distributeCoins(TreeNode* root) {
        if (root == nullptr) return 0;
        
        unordered_map<TreeNode*, int> balanceMap; // 存储每个节点的净流动量
        stack<TreeNode*> stk;
        TreeNode* prev = nullptr;
        int moves = 0;
        
        stk.push(root);
        
        while (!stk.empty()) {
            TreeNode* curr = stk.top();
            
            // 如果当前节点是叶子节点或者其子节点已经处理过
            if ((curr->left == nullptr && curr->right == nullptr) || 
                (prev != nullptr && (prev == curr->left || prev == curr->right))) {
                
                // 处理当前节点
                int leftBalance = curr->left ? balanceMap[curr->left] : 0;
                int rightBalance = curr->right ? balanceMap[curr->right] : 0;
                int currentBalance = leftBalance + rightBalance + curr->val - 1;
                
                // 更新移动次数
                moves += abs(leftBalance) + abs(rightBalance);
                
                // 存储当前节点的净流动量
                balanceMap[curr] = currentBalance;
                stk.pop();
                prev = curr;
            } else {
                // 先处理右子树，再处理左子树
                if (curr->right != nullptr) {
                    stk.push(curr->right);
                }
                if (curr->left != nullptr) {
                    stk.push(curr->left);
                }
            }
        }
        
        return moves;
    }
};

// 单元测试
class TestDistributeCoins {
public:
    void runTests() {
        cout << "===== 运行分发硬币单元测试 =====" << endl;
        
        testCase1();  // 空树测试
        testCase2();  // 单节点平衡测试
        testCase3();  // 简单不平衡测试
        testCase4();  // 复杂不平衡测试
        testCase5();  // 所有节点都需要硬币测试
        testCase6();  // 所有节点都有多余硬币测试
        
        cout << "===== 单元测试结束 =====" << endl;
    }

private:
    void testCase1() {
        Solution sol;
        int result = sol.distributeCoins(nullptr);
        cout << "测试用例1（空树）: " << (result == 0 ? "通过" : "失败") << " 结果=" << result << endl;
    }
    
    void testCase2() {
        // 单节点，硬币数为1（已经平衡）
        TreeNode* root = new TreeNode(1);
        Solution sol;
        int result = sol.distributeCoins(root);
        cout << "测试用例2（单节点平衡）: " << (result == 0 ? "通过" : "失败") << " 结果=" << result << endl;
        delete root;
    }
    
    void testCase3() {
        // 简单不平衡树:
        // 根节点有3个硬币，左子节点有0个硬币
        // 需要移动2次：根节点移动2个硬币到左子节点
        //       3
        //      /
        //     0
        TreeNode* root = new TreeNode(3);
        root->left = new TreeNode(0);
        
        Solution sol;
        int result = sol.distributeCoins(root);
        cout << "测试用例3（简单不平衡）: " << (result == 2 ? "通过" : "失败") << " 结果=" << result << endl;
        
        delete root->left;
        delete root;
    }
    
    void testCase4() {
        // 复杂不平衡树:
        //       0
        //      / \
        //     3   0
        // 需要移动3次：左子节点移动2个硬币到根节点，根节点移动1个硬币到右子节点
        TreeNode* root = new TreeNode(0);
        root->left = new TreeNode(3);
        root->right = new TreeNode(0);
        
        Solution sol;
        int result = sol.distributeCoins(root);
        cout << "测试用例4（复杂不平衡）: " << (result == 3 ? "通过" : "失败") << " 结果=" << result << endl;
        
        delete root->left;
        delete root->right;
        delete root;
    }
    
    void testCase5() {
        // 所有节点都需要硬币:
        //       0
        //      / \
        //     0   0
        // 需要从外部引入3个硬币，但题目保证总硬币数等于节点数
        // 实际上这种情况不会发生，因为总硬币数=节点数=3
        // 但初始分布为0,0,0，需要内部调整
        TreeNode* root = new TreeNode(0);
        root->left = new TreeNode(0);
        root->right = new TreeNode(0);
        
        Solution sol;
        int result = sol.distributeCoins(root);
        cout << "测试用例5（全赤字）: " << (result == 2 ? "通过" : "失败") << " 结果=" << result << endl;
        
        delete root->left;
        delete root->right;
        delete root;
    }
    
    void testCase6() {
        // 所有节点都有多余硬币:
        //       3
        //      / \
        //     1   1
        // 需要移动4次：根节点移动2个硬币出去，每个子节点移动1个硬币出去
        TreeNode* root = new TreeNode(3);
        root->left = new TreeNode(1);
        root->right = new TreeNode(1);
        
        Solution sol;
        int result = sol.distributeCoins(root);
        cout << "测试用例6（全盈余）: " << (result == 4 ? "通过" : "失败") << " 结果=" << result << endl;
        
        delete root->left;
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
        int result = sol.distributeCoins(largeTree);
        auto end = chrono::high_resolution_clock::now();
        
        auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
        cout << "大规模树测试: 结果=" << result << ", 耗时=" << duration.count() << "ms" << endl;
    }

private:
    TreeNode* buildLargeTree(int n) {
        return buildTreeHelper(1, n, 1); // 所有节点初始硬币数为1（平衡状态）
    }
    
    TreeNode* buildTreeHelper(int start, int end, int coinValue) {
        if (start > end) return nullptr;
        int mid = start + (end - start) / 2;
        TreeNode* root = new TreeNode(coinValue);
        root->left = buildTreeHelper(start, mid - 1, coinValue);
        root->right = buildTreeHelper(mid + 1, end, coinValue);
        return root;
    }
};

// 调试工具类
class DebugTool {
public:
    static void printTreeWithCoins(TreeNode* root) {
        if (root == nullptr) {
            cout << "空树" << endl;
            return;
        }
        
        cout << "二叉树硬币分布:" << endl;
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
        cout << "[" << node->val << "]" << endl;
        
        // 打印左子树
        printTreeHelper(node->left, depth + 1);
    }
};

// 主函数
int main() {
    // 运行单元测试
    TestDistributeCoins tester;
    tester.runTests();
    
    cout << "\n分发硬币算法实现完成！" << endl;
    cout << "关键特性：" << endl;
    cout << "- 时间复杂度: O(n)" << endl;
    cout << "- 空间复杂度: O(h)" << endl;
    cout << "- 支持大规模树结构" << endl;
    cout << "- 处理边界情况" << endl;
    cout << "- 数学原理：移动次数 = Σ|节点硬币数 - 1|" << endl;
    
    return 0;
}
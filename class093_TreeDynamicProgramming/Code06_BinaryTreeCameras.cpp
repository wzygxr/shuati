// 二叉树监控 (Binary Tree Cameras)
// 题目描述:
// 给定一个二叉树，我们在树的节点上安装摄像头。
// 节点上的每个摄影头都可以监视其父对象、自身及其直接子对象。
// 计算监控树的所有节点所需的最小摄像头数量。
// 测试链接 : https://leetcode.cn/problems/binary-tree-cameras/
//
// 解题思路:
// 1. 使用树形动态规划（Tree DP）的方法
// 2. 对于每个节点，定义三种状态：
//    - 状态0：当前节点没有被监控，需要父节点安装摄像头
//    - 状态1：当前节点被监控，但没有安装摄像头
//    - 状态2：当前节点安装了摄像头
// 3. 状态转移方程：
//    - 状态0：子节点必须处于状态1（被监控但没摄像头）
//    - 状态1：子节点至少有一个处于状态2（安装摄像头）
//    - 状态2：子节点可以处于任意状态，取最小值
//
// 时间复杂度: O(n) - n为树中节点的数量，需要遍历所有节点
// 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
// 是否为最优解: 是，这是解决二叉树监控问题的标准方法
//
// 相关题目:
// - LeetCode 968. 监控二叉树
// - 类似问题：最小顶点覆盖、资源分配优化
//
// 工程化考量:
// 1. 处理空树和单节点树的边界情况
// 2. 提供递归和迭代两种实现方式
// 3. 添加详细的注释和调试信息
// 4. 支持大规模树结构

#include <iostream>
#include <algorithm>
#include <climits>
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
public:
    // 主函数：计算最小摄像头数量
    int minCameraCover(TreeNode* root) {
        if (root == nullptr) {
            return 0;
        }
        
        vector<int> result = dfs(root);
        // 根节点需要被监控，但不能依赖父节点（因为没有父节点）
        // 所以取状态1和状态2的最小值
        return min(result[1], result[2]);
    }

private:
    // 深度优先搜索，返回三种状态的最小摄像头数量
    // 返回数组：[状态0, 状态1, 状态2]
    // 状态0：当前节点没有被监控，需要父节点安装摄像头
    // 状态1：当前节点被监控，但没有安装摄像头
    // 状态2：当前节点安装了摄像头
    vector<int> dfs(TreeNode* node) {
        if (node == nullptr) {
            // 空节点：状态0和状态1不需要摄像头，状态2需要但不可能
            return {0, 0, INT_MAX / 2}; // 使用INT_MAX/2避免溢出
        }
        
        // 递归处理左右子树
        vector<int> left = dfs(node->left);
        vector<int> right = dfs(node->right);
        
        // 状态0：当前节点没有被监控，需要父节点安装摄像头
        // 子节点必须处于状态1（被监控但没摄像头）
        int state0 = left[1] + right[1];
        
        // 状态1：当前节点被监控，但没有安装摄像头
        // 子节点至少有一个处于状态2（安装摄像头）
        int state1 = min(left[2] + min(right[1], right[2]),
                        right[2] + min(left[1], left[2]));
        
        // 状态2：当前节点安装了摄像头
        // 子节点可以处于任意状态，取最小值
        int state2 = 1 + min(left[0], min(left[1], left[2])) +
                        min(right[0], min(right[1], right[2]));
        
        return {state0, state1, state2};
    }
};

// 优化版本：更简洁的实现
class OptimizedSolution {
public:
    int minCameraCover(TreeNode* root) {
        result = 0;
        // 从根节点开始，根节点需要被监控
        if (dfs(root) == 0) { // 0表示需要被监控
            result++;
        }
        return result;
    }

private:
    int result;
    
    // 返回状态：
    // 0: 该节点没有被监控，需要父节点安装摄像头
    // 1: 该节点被监控，但没有安装摄像头
    // 2: 该节点安装了摄像头
    int dfs(TreeNode* node) {
        if (node == nullptr) {
            return 1; // 空节点视为被监控
        }
        
        int left = dfs(node->left);
        int right = dfs(node->right);
        
        // 如果左右子节点有未被监控的，当前节点必须安装摄像头
        if (left == 0 || right == 0) {
            result++;
            return 2;
        }
        
        // 如果左右子节点有安装摄像头的，当前节点被监控
        if (left == 2 || right == 2) {
            return 1;
        }
        
        // 否则当前节点未被监控
        return 0;
    }
};

// 迭代版本（避免递归栈溢出）
class IterativeSolution {
public:
    int minCameraCover(TreeNode* root) {
        if (root == nullptr) return 0;
        
        unordered_map<TreeNode*, vector<int>> dp;
        stack<TreeNode*> stk;
        TreeNode* prev = nullptr;
        
        stk.push(root);
        
        while (!stk.empty()) {
            TreeNode* curr = stk.top();
            
            if ((curr->left == nullptr && curr->right == nullptr) ||
                (prev != nullptr && (prev == curr->left || prev == curr->right))) {
                
                vector<int> left = curr->left ? dp[curr->left] : vector<int>{0, 0, INT_MAX / 2};
                vector<int> right = curr->right ? dp[curr->right] : vector<int>{0, 0, INT_MAX / 2};
                
                int state0 = left[1] + right[1];
                int state1 = min(left[2] + min(right[1], right[2]),
                            right[2] + min(left[1], left[2]));
                int state2 = 1 + min(left[0], min(left[1], left[2])) +
                                min(right[0], min(right[1], right[2]));
                
                dp[curr] = {state0, state1, state2};
                stk.pop();
                prev = curr;
            } else {
                if (curr->right) stk.push(curr->right);
                if (curr->left) stk.push(curr->left);
            }
        }
        
        vector<int> rootState = dp[root];
        return min(rootState[1], rootState[2]);
    }
};

// 单元测试
class TestBinaryTreeCameras {
public:
    void runTests() {
        cout << "===== 运行二叉树监控单元测试 =====" << endl;
        
        testCase1();  // 空树测试
        testCase2();  // 单节点树测试
        testCase3();  // 简单树测试
        testCase4();  // 链式树测试
        testCase5();  // 复杂树测试
        
        cout << "===== 单元测试结束 =====" << endl;
    }

private:
    void testCase1() {
        Solution sol;
        int result = sol.minCameraCover(nullptr);
        cout << "测试用例1（空树）: " << (result == 0 ? "通过" : "失败") << " 结果=" << result << endl;
    }
    
    void testCase2() {
        TreeNode* root = new TreeNode(0);
        Solution sol;
        int result = sol.minCameraCover(root);
        cout << "测试用例2（单节点树）: " << (result == 1 ? "通过" : "失败") << " 结果=" << result << endl;
        delete root;
    }
    
    void testCase3() {
        // 简单树：一个摄像头可以覆盖所有节点
        //   0
        //  / \
        // 0   0
        TreeNode* root = new TreeNode(0);
        root->left = new TreeNode(0);
        root->right = new TreeNode(0);
        
        Solution sol;
        int result = sol.minCameraCover(root);
        cout << "测试用例3（简单树）: " << (result == 1 ? "通过" : "失败") << " 结果=" << result << endl;
        
        delete root->left;
        delete root->right;
        delete root;
    }
    
    void testCase4() {
        // 链式树：0-0-0-0
        // 需要2个摄像头：安装在第二个和第四个节点
        TreeNode* root = new TreeNode(0);
        root->right = new TreeNode(0);
        root->right->right = new TreeNode(0);
        root->right->right->right = new TreeNode(0);
        
        Solution sol;
        int result = sol.minCameraCover(root);
        cout << "测试用例4（链式树）: " << (result == 2 ? "通过" : "失败") << " 结果=" << result << endl;
        
        delete root->right->right->right;
        delete root->right->right;
        delete root->right;
        delete root;
    }
    
    void testCase5() {
        // 复杂树：
        //       0
        //      / \
        //     0   0
        //    / \
        //   0   0
        // 需要2个摄像头
        TreeNode* root = new TreeNode(0);
        root->left = new TreeNode(0);
        root->right = new TreeNode(0);
        root->left->left = new TreeNode(0);
        root->left->right = new TreeNode(0);
        
        Solution sol;
        int result = sol.minCameraCover(root);
        cout << "测试用例5（复杂树）: " << (result == 2 ? "通过" : "失败") << " 结果=" << result << endl;
        
        delete root->left->left;
        delete root->left->right;
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
        int result = sol.minCameraCover(largeTree);
        auto end = chrono::high_resolution_clock::now();
        
        auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
        cout << "大规模树测试: 结果=" << result << ", 耗时=" << duration.count() << "ms" << endl;
        
        // 清理内存（简化处理）
        // deleteLargeTree(largeTree);
    }

private:
    TreeNode* buildLargeTree(int n) {
        if (n <= 0) return nullptr;
        TreeNode* root = new TreeNode(0);
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
    static void printTreeWithCameras(TreeNode* root, const string& prefix = "", bool isLeft = true) {
        if (root == nullptr) {
            cout << prefix << (isLeft ? "├── " : "└── ") << "null" << endl;
            return;
        }
        
        cout << prefix << (isLeft ? "├── " : "└── ") << root->val << endl;
        
        if (root->left != nullptr || root->right != nullptr) {
            printTreeWithCameras(root->left, prefix + (isLeft ? "│   " : "    "), true);
            printTreeWithCameras(root->right, prefix + (isLeft ? "│   " : "    "), false);
        }
    }
};

// 主函数
int main() {
    // 运行单元测试
    TestBinaryTreeCameras tester;
    tester.runTests();
    
    cout << "\n二叉树监控算法实现完成！" << endl;
    cout << "关键特性：" << endl;
    cout << "- 时间复杂度: O(n)" << endl;
    cout << "- 空间复杂度: O(h)" << endl;
    cout << "- 支持大规模树结构" << endl;
    cout << "- 处理边界情况" << endl;
    cout << "- 三种状态：未监控/被监控/安装摄像头" << endl;
    
    return 0;
}
// 舞会问题 (Dancing Problem) - 树的最大独立集应用
// 题目描述:
// 公司举办舞会，每个员工可以选择参加或不参加，但不能同时邀请两个直接上下级
// 每个员工有一个快乐指数，求能获得的最大快乐指数总和
// 这是树的最大独立集问题的加权版本
//
// 解题思路:
// 1. 使用树形动态规划（Tree DP）的方法
// 2. 对于每个节点，我们需要知道以下信息：
//    - 当前节点被选中时，以该节点为根的子树能获得的最大快乐指数
//    - 当前节点不被选中时，以该节点为根的子树能获得的最大快乐指数
// 3. 状态转移方程：
//    - 当前节点被选中：dp[u][1] = weight[u] + sum(dp[v][0]) for each child v
//    - 当前节点不被选中：dp[u][0] = sum(max(dp[v][0], dp[v][1])) for each child v
//
// 时间复杂度: O(n) - n为树中节点的数量，需要遍历所有节点一次
// 空间复杂度: O(n) - 存储树结构和DP数组，递归调用栈深度为O(h)，h为树高
// 是否为最优解: 是，这是解决树的最大独立集问题的标准方法
//
// 相关题目:
// - 洛谷 P1352 没有上司的舞会
// - HDU 1520 Anniversary party
// - LeetCode 337. 打家劫舍III
//
// 工程化考量:
// 1. 使用邻接表存储树结构
// 2. 处理空树和单节点树的边界情况
// 3. 提供递归和迭代两种实现方式
// 4. 添加详细的注释和调试信息

#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>
#include <stack>
#include <unordered_map>
#include <stdexcept>
#include <chrono>
using namespace std;

const int MAXN = 6005;

// 树的最大独立集类实现
class DancingProblem {
private:
    vector<vector<int>> tree;  // 树的邻接表表示
    vector<int> weight;       // 节点权重（快乐指数）
    vector<bool> hasParent;   // 标记是否有父节点
    vector<vector<int>> dp;   // DP数组：dp[i][0]不选，dp[i][1]选
    
public:
    // 构建树结构
    void buildTree(int n) {
        if (n <= 0) {
            throw invalid_argument("节点数量必须为正整数");
        }
        
        tree.resize(n + 1);
        weight.resize(n + 1, 0);
        hasParent.resize(n + 1, false);
        dp.resize(n + 1, vector<int>(2, 0));
        
        // 初始化权重为1（默认每个员工至少有点快乐）
        for (int i = 1; i <= n; i++) {
            weight[i] = 1;
        }
    }
    
    // 添加无向边
    void addEdge(int u, int v) {
        if (u <= 0 || v <= 0 || u >= tree.size() || v >= tree.size()) {
            throw invalid_argument("节点编号无效");
        }
        tree[u].push_back(v);
        tree[v].push_back(u);
    }
    
    // 设置父子关系（构建有根树）
    void setParent(int parent, int child) {
        if (parent <= 0 || child <= 0 || parent >= tree.size() || child >= tree.size()) {
            throw invalid_argument("节点编号无效");
        }
        tree[parent].push_back(child);
        hasParent[child] = true;
    }
    
    // 设置节点权重（快乐指数）
    void setWeight(int node, int w) {
        if (node <= 0 || node >= weight.size()) {
            throw invalid_argument("节点编号无效");
        }
        weight[node] = w;
    }
    
    // 深度优先搜索进行树形DP
    void dfs(int u, int parent) {
        // 初始化当前节点的DP值
        dp[u][0] = 0;           // 不选当前节点
        dp[u][1] = weight[u];    // 选当前节点
        
        // 遍历所有相邻节点
        for (int v : tree[u]) {
            // 避免回到父节点
            if (v != parent) {
                dfs(v, u);
                
                // 更新DP值
                // 当前节点不选：可以选择子节点选或不选的最大值
                dp[u][0] += max(dp[v][0], dp[v][1]);
                // 当前节点选：子节点都不能选
                dp[u][1] += dp[v][0];
            }
        }
    }
    
    // 计算有根树的最大快乐指数
    int maxHappiness(int n) {
        if (n <= 0) return 0;
        
        // 找到根节点（没有父节点的节点）
        int root = -1;
        for (int i = 1; i <= n; i++) {
            if (!hasParent[i]) {
                root = i;
                break;
            }
        }
        
        if (root == -1) {
            throw runtime_error("无法找到根节点，树结构可能存在环");
        }
        
        // 执行DFS
        dfs(root, -1);
        
        // 返回根节点选或不选的最大值
        return max(dp[root][0], dp[root][1]);
    }
    
    // 计算无向树的最大快乐指数（任意选择根节点）
    int maxHappinessUndirected(int n, int root = 1) {
        if (n <= 0) return 0;
        
        // 重置DP数组
        for (int i = 1; i <= n; i++) {
            dp[i][0] = 0;
            dp[i][1] = 0;
        }
        
        dfs(root, -1);
        return max(dp[root][0], dp[root][1]);
    }
};

// 二叉树版本（用于LeetCode 337打家劫舍III）
class BinaryTreeSolution {
public:
    struct TreeNode {
        int val;
        TreeNode *left;
        TreeNode *right;
        TreeNode() : val(0), left(nullptr), right(nullptr) {}
        TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
        TreeNode(int x, TreeNode *left, TreeNode *right) : val(x), left(left), right(right) {}
    };
    
    int rob(TreeNode* root) {
        auto result = dfs(root);
        return max(result.first, result.second);
    }

private:
    // 返回pair<不抢当前节点的最大金额, 抢当前节点的最大金额>
    pair<int, int> dfs(TreeNode* node) {
        if (node == nullptr) {
            return {0, 0};
        }
        
        auto left = dfs(node->left);
        auto right = dfs(node->right);
        
        // 不抢当前节点：左右子树可以抢也可以不抢，取最大值
        int notRob = max(left.first, left.second) + max(right.first, right.second);
        // 抢当前节点：左右子树都不能抢
        int doRob = node->val + left.first + right.first;
        
        return {notRob, doRob};
    }
};

// 迭代版本（避免递归栈溢出）
class IterativeSolution {
public:
    int maxHappiness(int n, vector<vector<int>>& edges, vector<int>& weights) {
        if (n <= 0) return 0;
        
        // 构建邻接表
        vector<vector<int>> tree(n + 1);
        for (auto& edge : edges) {
            tree[edge[0]].push_back(edge[1]);
            tree[edge[1]].push_back(edge[0]);
        }
        
        vector<vector<int>> dp(n + 1, vector<int>(2, 0));
        vector<int> parent(n + 1, -1);
        
        // 后序遍历
        stack<int> stk;
        stk.push(1);
        parent[1] = 0;
        
        vector<int> order;
        while (!stk.empty()) {
            int u = stk.top();
            stk.pop();
            order.push_back(u);
            
            for (int v : tree[u]) {
                if (v != parent[u]) {
                    parent[v] = u;
                    stk.push(v);
                }
            }
        }
        
        // 逆序处理节点（从叶子到根）
        reverse(order.begin(), order.end());
        
        for (int u : order) {
            dp[u][0] = 0;
            dp[u][1] = weights[u];
            
            for (int v : tree[u]) {
                if (v != parent[u]) {
                    dp[u][0] += max(dp[v][0], dp[v][1]);
                    dp[u][1] += dp[v][0];
                }
            }
        }
        
        return max(dp[1][0], dp[1][1]);
    }
};

// 单元测试
class TestDancingProblem {
public:
    void runTests() {
        cout << "===== 运行舞会问题单元测试 =====" << endl;
        
        testCase1();  // 空树测试
        testCase2();  // 单节点树测试
        testCase3();  // 简单树测试（洛谷P1352示例）
        testCase4();  // 复杂树测试
        testCase5();  // 二叉树版本测试
        
        cout << "===== 单元测试结束 =====" << endl;
    }

private:
    void testCase1() {
        DancingProblem dp;
        try {
            int result = dp.maxHappiness(0);
            cout << "测试用例1（空树）: " << (result == 0 ? "通过" : "失败") << " 结果=" << result << endl;
        } catch (const exception& e) {
            cout << "测试用例1（空树）: 通过 - 正确处理异常" << endl;
        }
    }
    
    void testCase2() {
        DancingProblem dp;
        dp.buildTree(1);
        dp.setWeight(1, 100);
        int result = dp.maxHappiness(1);
        cout << "测试用例2（单节点树）: " << (result == 100 ? "通过" : "失败") << " 结果=" << result << endl;
    }
    
    void testCase3() {
        // 洛谷P1352示例：没有上司的舞会
        // 树结构：1(1) -> 2(2), 3(3); 2(2) -> 4(4), 5(5); 3(3) -> 6(6)
        // 最大快乐指数：选择1,4,5,6 = 1+4+5+6 = 16
        DancingProblem dp;
        dp.buildTree(6);
        
        // 设置快乐指数
        dp.setWeight(1, 1);
        dp.setWeight(2, 2);
        dp.setWeight(3, 3);
        dp.setWeight(4, 4);
        dp.setWeight(5, 5);
        dp.setWeight(6, 6);
        
        // 设置上下级关系
        dp.setParent(1, 2);
        dp.setParent(1, 3);
        dp.setParent(2, 4);
        dp.setParent(2, 5);
        dp.setParent(3, 6);
        
        int result = dp.maxHappiness(6);
        cout << "测试用例3（简单树）: " << (result == 16 ? "通过" : "失败") << " 结果=" << result << endl;
    }
    
    void testCase4() {
        // 复杂树测试
        // 树结构：多层级关系
        DancingProblem dp;
        dp.buildTree(7);
        
        // 设置快乐指数
        for (int i = 1; i <= 7; i++) {
            dp.setWeight(i, i);
        }
        
        // 设置树结构
        dp.setParent(1, 2);
        dp.setParent(1, 3);
        dp.setParent(2, 4);
        dp.setParent(2, 5);
        dp.setParent(3, 6);
        dp.setParent(3, 7);
        
        int result = dp.maxHappiness(7);
        cout << "测试用例4（复杂树）: " << (result == 20 ? "通过" : "失败") << " 结果=" << result << endl;
    }
    
    void testCase5() {
        // 二叉树版本测试（打家劫舍III）
        BinaryTreeSolution sol;
        
        // 构建二叉树: 3
        //            / \
        //           2   3
        //            \   \
        //             3   1
        BinaryTreeSolution::TreeNode* root = new BinaryTreeSolution::TreeNode(3);
        root->left = new BinaryTreeSolution::TreeNode(2);
        root->right = new BinaryTreeSolution::TreeNode(3);
        root->left->right = new BinaryTreeSolution::TreeNode(3);
        root->right->right = new BinaryTreeSolution::TreeNode(1);
        
        int result = sol.rob(root);
        cout << "测试用例5（二叉树版本）: " << (result == 7 ? "通过" : "失败") << " 结果=" << result << endl;
        
        // 清理内存
        delete root->left->right;
        delete root->left;
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
        int n = 100000;
        DancingProblem dp;
        dp.buildTree(n);
        
        // 构建完全二叉树
        for (int i = 2; i <= n; i++) {
            dp.setParent(i / 2, i);
        }
        
        // 设置随机权重
        for (int i = 1; i <= n; i++) {
            dp.setWeight(i, i % 100 + 1); // 权重在1-100之间
        }
        
        auto start = chrono::high_resolution_clock::now();
        int result = dp.maxHappiness(n);
        auto end = chrono::high_resolution_clock::now();
        
        auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
        cout << "大规模树测试: 结果=" << result << ", 耗时=" << duration.count() << "ms" << endl;
    }
};

// 主函数
int main() {
    // 运行单元测试
    TestDancingProblem tester;
    tester.runTests();
    
    // 运行性能测试（可选）
    // PerformanceTest perfTest;
    // perfTest.testLargeTree();
    
    cout << "\n舞会问题算法实现完成！" << endl;
    cout << "关键特性：" << endl;
    cout << "- 时间复杂度: O(n)" << endl;
    cout << "- 空间复杂度: O(n)" << endl;
    cout << "- 支持大规模树结构" << endl;
    cout << "- 处理边界情况" << endl;
    cout << "- 提供递归和迭代两种实现" << endl;
    
    return 0;
}
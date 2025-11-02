#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
#include <functional>
#include <queue>
#include <stack>
#include <unordered_map>
#include <utility> // for pair
#include <memory>  // for smart pointers

using namespace std;

/**
 * 树形DP实战练习题目 - C++版本
 * 包含各大OJ平台的经典树形DP问题实现
 * 
 * 题目来源：LeetCode, LintCode, Codeforces, 洛谷, POJ等
 * 算法类型：基础树形DP、换根DP、树形背包、虚树DP
 */

// 二叉树节点定义
struct TreeNode {
    int val;
    TreeNode* left;
    TreeNode* right;
    TreeNode() : val(0), left(nullptr), right(nullptr) {}
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
    TreeNode(int x, TreeNode* left, TreeNode* right) : val(x), left(left), right(right) {}
};

class TreeDPPractice {
public:
    /**
     * 1. LeetCode 337 - 打家劫舍 III（经典树形DP）
     * 题目链接：https://leetcode.cn/problems/house-robber-iii/
     * 时间复杂度: O(n), 空间复杂度: O(h)
     */
    int rob(TreeNode* root) {
        auto result = robHelper(root);
        return max(result.first, result.second);
    }
    
private:
    pair<int, int> robHelper(TreeNode* node) {
        if (!node) return {0, 0};
        
        auto left = robHelper(node->left);
        auto right = robHelper(node->right);
        
        // 不偷当前节点：左右子树可以偷或不偷
        int notRob = max(left.first, left.second) + max(right.first, right.second);
        // 偷当前节点：左右子树都不能偷
        int doRob = node->val + left.first + right.first;
        
        return {notRob, doRob};
    }
    
public:
    /**
     * 2. LeetCode 124 - 二叉树中的最大路径和
     * 题目链接：https://leetcode.cn/problems/binary-tree-maximum-path-sum/
     * 时间复杂度: O(n), 空间复杂度: O(h)
     */
    int maxPathSum(TreeNode* root) {
        int maxSum = INT_MIN;
        maxGain(root, maxSum);
        return maxSum;
    }
    
private:
    int maxGain(TreeNode* node, int& maxSum) {
        if (!node) return 0;
        
        int leftGain = max(maxGain(node->left, maxSum), 0);
        int rightGain = max(maxGain(node->right, maxSum), 0);
        
        maxSum = max(maxSum, node->val + leftGain + rightGain);
        return node->val + max(leftGain, rightGain);
    }
    
public:
    /**
     * 3. LeetCode 543 - 二叉树的直径
     * 题目链接：https://leetcode.cn/problems/diameter-of-binary-tree/
     * 时间复杂度: O(n), 空间复杂度: O(h)
     */
    int diameterOfBinaryTree(TreeNode* root) {
        int maxDiameter = 0;
        depth(root, maxDiameter);
        return maxDiameter;
    }
    
private:
    int depth(TreeNode* node, int& maxDiameter) {
        if (!node) return 0;
        
        int leftDepth = depth(node->left, maxDiameter);
        int rightDepth = depth(node->right, maxDiameter);
        
        maxDiameter = max(maxDiameter, leftDepth + rightDepth);
        return max(leftDepth, rightDepth) + 1;
    }
    
public:
    /**
     * 4. LeetCode 968 - 二叉树摄像头
     * 题目链接：https://leetcode.cn/problems/binary-tree-cameras/
     * 时间复杂度: O(n), 空间复杂度: O(h)
     */
    int minCameraCover(TreeNode* root) {
        auto result = minCameraCoverHelper(root);
        return min(result[0], result[1]);
    }
    
private:
    vector<int> minCameraCoverHelper(TreeNode* node) {
        if (!node) return {INT_MAX / 2, 0, 0};
        
        auto left = minCameraCoverHelper(node->left);
        auto right = minCameraCoverHelper(node->right);
        
        // 状态0：当前节点安装摄像头
        int install = 1 + min({left[0], left[1], left[2]}) + 
                        min({right[0], right[1], right[2]});
        
        // 状态1：当前节点被子节点监控
        int monitored = min({
            left[0] + right[0],
            left[0] + right[1],
            left[1] + right[0]
        });
        
        // 状态2：当前节点未被监控（需要父节点安装摄像头）
        int unmonitored = left[1] + right[1];
        
        return {install, monitored, unmonitored};
    }
    
public:
    /**
     * 5. LeetCode 834 - 树中距离之和（换根DP经典题）
     * 题目链接：https://leetcode.cn/problems/sum-of-distances-in-tree/
     * 时间复杂度: O(n), 空间复杂度: O(n)
     */
    vector<int> sumOfDistancesInTree(int n, vector<vector<int>>& edges) {
        // 构建图
        vector<vector<int>> graph(n);
        for (auto& edge : edges) {
            graph[edge[0]].push_back(edge[1]);
            graph[edge[1]].push_back(edge[0]);
        }
        
        vector<int> dp(n, 0); // 以节点i为根的子树的距离和
        vector<int> size(n, 0); // 子树大小
        vector<int> result(n, 0);
        
        dfs1(0, -1, graph, dp, size);
        dfs2(0, -1, graph, dp, size, result, n);
        
        return result;
    }
    
private:
    void dfs1(int u, int parent, vector<vector<int>>& graph, vector<int>& dp, vector<int>& size) {
        size[u] = 1;
        for (int v : graph[u]) {
            if (v != parent) {
                dfs1(v, u, graph, dp, size);
                size[u] += size[v];
                dp[u] += dp[v] + size[v];
            }
        }
    }
    
    void dfs2(int u, int parent, vector<vector<int>>& graph, vector<int>& dp, vector<int>& size, 
             vector<int>& result, int n) {
        result[u] = dp[u];
        for (int v : graph[u]) {
            if (v != parent) {
                // 保存原始值
                int dpU = dp[u], dpV = dp[v];
                int szU = size[u], szV = size[v];
                
                // 换根操作
                dp[u] = dp[u] - dp[v] - size[v];
                size[u] = size[u] - size[v];
                dp[v] = dp[v] + dp[u] + size[u];
                size[v] = size[v] + size[u];
                
                dfs2(v, u, graph, dp, size, result, n);
                
                // 恢复原始值
                dp[u] = dpU;
                dp[v] = dpV;
                size[u] = szU;
                size[v] = szV;
            }
        }
    }
    
public:
    /**
     * 6. 洛谷 P2014 - 选课（树形背包DP）
     * 题目链接：https://www.luogu.com.cn/problem/P2014
     * 时间复杂度: O(n*m²), 空间复杂度: O(n*m)
     */
    int courseSelection(int n, int m, vector<int>& prerequisites, vector<int>& credits) {
        // 构建树（0为虚拟根节点）
        vector<vector<int>> graph(n + 1);
        for (int i = 1; i <= n; i++) {
            int pre = prerequisites[i - 1];
            graph[pre].push_back(i);
        }
        
        vector<vector<int>> dp(n + 1, vector<int>(m + 1, 0));
        dfsCourse(0, graph, credits, dp, m);
        return dp[0][m];
    }
    
private:
    void dfsCourse(int u, vector<vector<int>>& graph, vector<int>& credits, 
                  vector<vector<int>>& dp, int m) {
        // 初始化：选择当前节点（如果u>0）
        if (u > 0) {
            for (int j = 1; j <= m; j++) {
                dp[u][j] = credits[u - 1];
            }
        }
        
        for (int v : graph[u]) {
            dfsCourse(v, graph, credits, dp, m);
            
            // 背包DP：从大到小遍历
            for (int j = m; j >= 0; j--) {
                for (int k = 1; k <= j; k++) {
                    if (u == 0) {
                        // 虚拟根节点，只能选择子节点
                        dp[u][j] = max(dp[u][j], dp[u][j - k] + dp[v][k]);
                    } else {
                        // 普通节点，可以选择当前节点和子节点
                        dp[u][j] = max(dp[u][j], dp[u][j - k] + dp[v][k] - credits[u - 1]);
                    }
                }
            }
        }
    }
    
public:
    /**
     * 7. Codeforces 1187E - Tree Painting（换根DP）
     * 题目链接：https://codeforces.com/contest/1187/problem/E
     * 时间复杂度: O(n), 空间复杂度: O(n)
     */
    long long treePainting(int n, vector<vector<int>>& edges) {
        vector<vector<int>> graph(n);
        for (auto& edge : edges) {
            graph[edge[0]].push_back(edge[1]);
            graph[edge[1]].push_back(edge[0]);
        }
        
        vector<long long> size(n, 0);
        vector<long long> dp(n, 0);
        
        // 第一次DFS：计算以0为根时的结果
        dfsPainting1(0, -1, graph, size, dp);
        
        long long maxScore = dp[0];
        // 第二次DFS：换根计算最大值
        dfsPainting2(0, -1, graph, size, dp, maxScore, n);
        
        return maxScore;
    }
    
private:
    void dfsPainting1(int u, int parent, vector<vector<int>>& graph, 
                     vector<long long>& size, vector<long long>& dp) {
        size[u] = 1;
        for (int v : graph[u]) {
            if (v != parent) {
                dfsPainting1(v, u, graph, size, dp);
                size[u] += size[v];
                dp[u] += dp[v];
            }
        }
        dp[u] += size[u];
    }
    
    void dfsPainting2(int u, int parent, vector<vector<int>>& graph, 
                     vector<long long>& size, vector<long long>& dp, 
                     long long& maxScore, int n) {
        maxScore = max(maxScore, dp[u]);
        
        for (int v : graph[u]) {
            if (v != parent) {
                // 保存原始值
                long long dpU = dp[u], dpV = dp[v];
                long long szU = size[u], szV = size[v];
                
                // 换根：u->v
                dp[u] = dp[u] - dp[v] - size[v];
                size[u] = size[u] - size[v];
                dp[v] = dp[v] + dp[u] + size[u];
                size[v] = size[v] + size[u];
                
                dfsPainting2(v, u, graph, size, dp, maxScore, n);
                
                // 恢复
                dp[u] = dpU;
                dp[v] = dpV;
                size[u] = szU;
                size[v] = szV;
            }
        }
    }
    
public:
    /**
     * 8. POJ 3107 - Godfather（树的重心）
     * 题目链接：http://poj.org/problem?id=3107
     * 时间复杂度: O(n), 空间复杂度: O(n)
     */
    vector<int> findCentroids(int n, vector<vector<int>>& edges) {
        vector<vector<int>> graph(n);
        for (auto& edge : edges) {
            graph[edge[0]].push_back(edge[1]);
            graph[edge[1]].push_back(edge[0]);
        }
        
        vector<int> size(n, 0);
        vector<int> centroids;
        vector<int> maxComponent(n, INT_MAX);
        
        dfsCentroid(0, -1, graph, size, centroids, maxComponent, n);
        return centroids;
    }
    
private:
    void dfsCentroid(int u, int parent, vector<vector<int>>& graph, vector<int>& size,
                    vector<int>& centroids, vector<int>& maxComponent, int n) {
        size[u] = 1;
        int maxSize = 0;
        
        for (int v : graph[u]) {
            if (v != parent) {
                dfsCentroid(v, u, graph, size, centroids, maxComponent, n);
                size[u] += size[v];
                maxSize = max(maxSize, size[v]);
            }
        }
        
        maxSize = max(maxSize, n - size[u]);
        maxComponent[u] = maxSize;
        
        // 如果是重心，加入结果
        if (maxSize <= n / 2) {
            centroids.push_back(u);
        }
    }
    
public:
    /**
     * 9. LeetCode 687 - 最长同值路径
     * 题目链接：https://leetcode.cn/problems/longest-univalue-path/
     * 时间复杂度: O(n), 空间复杂度: O(h)
     */
    int longestUnivaluePath(TreeNode* root) {
        int longestPath = 0;
        dfsUnivalue(root, longestPath);
        return longestPath;
    }
    
private:
    int dfsUnivalue(TreeNode* node, int& longestPath) {
        if (!node) return 0;
        
        int left = dfsUnivalue(node->left, longestPath);
        int right = dfsUnivalue(node->right, longestPath);
        
        int leftPath = 0, rightPath = 0;
        if (node->left && node->left->val == node->val) {
            leftPath = left + 1;
        }
        if (node->right && node->right->val == node->val) {
            rightPath = right + 1;
        }
        
        longestPath = max(longestPath, leftPath + rightPath);
        return max(leftPath, rightPath);
    }
    
public:
    /**
     * 10. LeetCode 979 - 在二叉树中分配硬币
     * 题目链接：https://leetcode.cn/problems/distribute-coins-in-binary-tree/
     * 时间复杂度: O(n), 空间复杂度: O(h)
     */
    int distributeCoins(TreeNode* root) {
        int moves = 0;
        dfsDistribute(root, moves);
        return moves;
    }
    
private:
    int dfsDistribute(TreeNode* node, int& moves) {
        if (!node) return 0;
        
        int left = dfsDistribute(node->left, moves);
        int right = dfsDistribute(node->right, moves);
        
        moves += abs(left) + abs(right);
        return node->val - 1 + left + right;
    }
};

// 单元测试函数
int main() {
    TreeDPPractice solver;
    
    // 测试打家劫舍III
    TreeNode* root = new TreeNode(3);
    root->left = new TreeNode(2);
    root->right = new TreeNode(3);
    root->left->right = new TreeNode(3);
    root->right->right = new TreeNode(1);
    
    cout << "打家劫舍III结果: " << solver.rob(root) << endl;
    
    // 测试二叉树直径
    TreeNode* diameterRoot = new TreeNode(1);
    diameterRoot->left = new TreeNode(2);
    diameterRoot->right = new TreeNode(3);
    diameterRoot->left->left = new TreeNode(4);
    diameterRoot->left->right = new TreeNode(5);
    
    cout << "二叉树直径: " << solver.diameterOfBinaryTree(diameterRoot) << endl;
    
    // 清理内存
    delete root->left->right;
    delete root->right->right;
    delete root->left;
    delete root->right;
    delete root;
    
    delete diameterRoot->left->left;
    delete diameterRoot->left->right;
    delete diameterRoot->left;
    delete diameterRoot->right;
    delete diameterRoot;
    
    return 0;
}
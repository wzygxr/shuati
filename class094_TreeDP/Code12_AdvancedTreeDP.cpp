#include <iostream>
#include <vector>
#include <queue>
#include <unordered_map>
#include <algorithm>
#include <climits>
#include <utility> // for pair
#include <functional> // for greater

using namespace std;

/**
 * 高级树形DP综合应用 - C++版本
 * 包含多个竞赛级别树形DP问题的解决方案
 * 
 * 时间复杂度分析：
 * - 基础树形DP: O(n)
 * - 换根DP: O(n) 
 * - 树形背包: O(n*m)
 * - 虚树DP: O(k log k)
 * 
 * 空间复杂度分析：
 * - 递归栈: O(h)
 * - DP数组: O(n)
 * - 图存储: O(n)
 */

class AdvancedTreeDP {
public:
    /**
     * 1. 树的最大独立集（Maximum Independent Set）
     * 问题描述：在树中选择最多的节点，使得任意两个被选节点都不相邻
     * 算法要点：树形DP，状态设计为选择/不选择当前节点
     * 时间复杂度: O(n), 空间复杂度: O(n)
     */
    int treeMaxIndependentSet(vector<vector<int>>& graph) {
        int n = graph.size();
        vector<vector<int>> dp(n, vector<int>(2, 0)); // dp[u][0]: 不选u, dp[u][1]: 选u
        vector<bool> visited(n, false);
        
        dfsMIS(0, -1, graph, dp, visited);
        return max(dp[0][0], dp[0][1]);
    }
    
    /**
     * 2. 树的最小顶点覆盖（Minimum Vertex Cover）
     * 问题描述：选择最少的节点，使得每条边至少有一个端点被选择
     * 算法要点：树形DP，状态转移与最大独立集相关
     * 时间复杂度: O(n), 空间复杂度: O(n)
     */
    int treeMinVertexCover(vector<vector<int>>& graph) {
        int n = graph.size();
        vector<vector<int>> dp(n, vector<int>(2, 0)); // dp[u][0]: u不被覆盖, dp[u][1]: u被覆盖
        vector<bool> visited(n, false);
        
        dfsMVC(0, -1, graph, dp, visited);
        return min(dp[0][0], dp[0][1]);
    }
    
    /**
     * 3. 树的最小支配集（Minimum Dominating Set）
     * 问题描述：选择最少的节点，使得每个节点要么被选择，要么与某个被选节点相邻
     * 算法要点：复杂状态设计的树形DP
     * 时间复杂度: O(n), 空间复杂度: O(n)
     */
    int treeMinDominatingSet(vector<vector<int>>& graph) {
        int n = graph.size();
        // dp[u][0]: u被选择, dp[u][1]: u未被选择但被父节点覆盖, dp[u][2]: u未被覆盖
        vector<vector<int>> dp(n, vector<int>(3, 0));
        vector<bool> visited(n, false);
        
        dfsMDS(0, -1, graph, dp, visited);
        return min(dp[0][0], dp[0][2]); // 根节点不能要求父节点覆盖
    }
    
    /**
     * 4. 树的带权最大独立集
     * 问题描述：每个节点有权重，选择权重和最大的独立集
     * 算法要点：带权树形DP
     * 时间复杂度: O(n), 空间复杂度: O(n)
     */
    int treeWeightedMaxIndependentSet(vector<vector<int>>& graph, vector<int>& weights) {
        int n = graph.size();
        vector<vector<int>> dp(n, vector<int>(2, 0)); // dp[u][0]: 不选u, dp[u][1]: 选u
        vector<bool> visited(n, false);
        
        dfsWMIS(0, -1, graph, weights, dp, visited);
        return max(dp[0][0], dp[0][1]);
    }
    
    /**
     * 5. 树的k着色问题
     * 问题描述：用k种颜色给树染色，相邻节点颜色不同，求方案数
     * 算法要点：组合数学 + 树形DP
     * 时间复杂度: O(n), 空间复杂度: O(n)
     */
    long long treeKColoring(int n, int k, vector<vector<int>>& edges) {
        // 构建图
        vector<vector<int>> graph(n);
        for (auto& edge : edges) {
            graph[edge[0]].push_back(edge[1]);
            graph[edge[1]].push_back(edge[0]);
        }
        
        // DP计算
        vector<vector<long long>> dp(n, vector<long long>(2, 0)); // dp[u][0]: u染特定颜色时的方案数
        vector<bool> visited(n, false);
        
        dfsColoring(0, -1, graph, k, dp, visited);
        return dp[0][0] * k; // 根节点有k种颜色选择
    }
    
    /**
     * 6. 树的直径（带权版本）
     * 问题描述：求带权树的最长路径（直径）
     * 算法要点：两次BFS
     * 时间复杂度: O(n), 空间复杂度: O(n)
     */
    int weightedTreeDiameter(vector<vector<pair<int, int>>>& graph) {
        int n = graph.size();
        // 第一次BFS找到最远点
        auto result1 = bfsFarthest(0, graph);
        int farthest = result1.first;
        // 第二次BFS找到直径
        auto result2 = bfsFarthest(farthest, graph);
        return result2.second;
    }
    
    /**
     * 7. 树的重心（Centroid）
     * 问题描述：找到删除后使得最大连通分量最小的节点
     * 算法要点：DFS计算子树大小
     * 时间复杂度: O(n), 空间复杂度: O(n)
     */
    vector<int> treeCentroids(vector<vector<int>>& graph) {
        int n = graph.size();
        vector<int> size(n, 0);
        vector<int> centroids;
        
        dfsCentroid(0, -1, graph, size, centroids, n);
        return centroids;
    }
    
    /**
     * 8. 树的路径统计问题
     * 问题描述：统计树上满足特定条件的路径数量
     * 算法要点：DFS + 哈希表
     * 时间复杂度: O(n), 空间复杂度: O(n)
     */
    int countPathsWithSum(TreeNode* root, int targetSum) {
        unordered_map<long long, int> prefixSum;
        prefixSum[0] = 1; // 空路径
        return dfsPathSum(root, 0, targetSum, prefixSum);
    }

private:
    // 二叉树节点定义
    struct TreeNode {
        int val;
        TreeNode* left;
        TreeNode* right;
        TreeNode() : val(0), left(nullptr), right(nullptr) {}
        TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
        TreeNode(int x, TreeNode* left, TreeNode* right) : val(x), left(left), right(right) {}
    };
    
    void dfsMIS(int u, int parent, vector<vector<int>>& graph, vector<vector<int>>& dp, vector<bool>& visited) {
        visited[u] = true;
        dp[u][1] = 1; // 选择当前节点
        
        for (int v : graph[u]) {
            if (v != parent && !visited[v]) {
                dfsMIS(v, u, graph, dp, visited);
                // 不选u时，v可选可不选
                dp[u][0] += max(dp[v][0], dp[v][1]);
                // 选u时，v不能选
                dp[u][1] += dp[v][0];
            }
        }
    }
    
    void dfsMVC(int u, int parent, vector<vector<int>>& graph, vector<vector<int>>& dp, vector<bool>& visited) {
        visited[u] = true;
        dp[u][1] = 1; // 选择当前节点
        
        for (int v : graph[u]) {
            if (v != parent && !visited[v]) {
                dfsMVC(v, u, graph, dp, visited);
                // u不被覆盖时，v必须被覆盖
                dp[u][0] += dp[v][1];
                // u被覆盖时，v可选可不选
                dp[u][1] += min(dp[v][0], dp[v][1]);
            }
        }
    }
    
    void dfsMDS(int u, int parent, vector<vector<int>>& graph, vector<vector<int>>& dp, vector<bool>& visited) {
        visited[u] = true;
        dp[u][0] = 1; // 选择当前节点
        dp[u][1] = 0; // 被父节点覆盖
        dp[u][2] = 0; // 未被覆盖
        
        int minDiff = INT_MAX;
        bool hasChild = false;
        
        for (int v : graph[u]) {
            if (v != parent && !visited[v]) {
                hasChild = true;
                dfsMDS(v, u, graph, dp, visited);
                
                // 状态转移
                dp[u][0] += min({dp[v][0], dp[v][1], dp[v][2]});
                dp[u][1] += min(dp[v][0], dp[v][2]);
                dp[u][2] += min(dp[v][0], dp[v][2]);
                
                // 记录最小差值，用于处理dp[u][2]的特殊情况
                minDiff = min(minDiff, dp[v][0] - min(dp[v][0], dp[v][2]));
            }
        }
        
        // 如果没有子节点，调整状态值
        if (!hasChild) {
            dp[u][1] = 0;
            dp[u][2] = INT_MAX / 2; // 避免溢出
        } else {
            // 处理dp[u][2]：至少有一个子节点被选择
            if (minDiff != INT_MAX) {
                dp[u][2] += minDiff;
            } else {
                dp[u][2] = INT_MAX / 2;
            }
        }
    }
    
    void dfsWMIS(int u, int parent, vector<vector<int>>& graph, vector<int>& weights, 
                 vector<vector<int>>& dp, vector<bool>& visited) {
        visited[u] = true;
        dp[u][1] = weights[u]; // 选择当前节点获得权重
        
        for (int v : graph[u]) {
            if (v != parent && !visited[v]) {
                dfsWMIS(v, u, graph, weights, dp, visited);
                // 不选u时，v可选可不选
                dp[u][0] += max(dp[v][0], dp[v][1]);
                // 选u时，v不能选
                dp[u][1] += dp[v][0];
            }
        }
    }
    
    void dfsColoring(int u, int parent, vector<vector<int>>& graph, int k, 
                    vector<vector<long long>>& dp, vector<bool>& visited) {
        visited[u] = true;
        dp[u][0] = 1; // 当前节点染特定颜色
        
        for (int v : graph[u]) {
            if (v != parent && !visited[v]) {
                dfsColoring(v, u, graph, k, dp, visited);
                // 子节点不能与父节点同色，所以有(k-1)种选择
                dp[u][0] *= (dp[v][0] * (k - 1));
            }
        }
    }
    
    pair<int, int> bfsFarthest(int start, vector<vector<pair<int, int>>>& graph) {
        int n = graph.size();
        vector<int> dist(n, -1);
        dist[start] = 0;
        
        queue<int> q;
        q.push(start);
        
        int farthest = start, maxDist = 0;
        
        while (!q.empty()) {
            int u = q.front();
            q.pop();
            
            for (auto& edge : graph[u]) {
                int v = edge.first, w = edge.second;
                if (dist[v] == -1) {
                    dist[v] = dist[u] + w;
                    if (dist[v] > maxDist) {
                        maxDist = dist[v];
                        farthest = v;
                    }
                    q.push(v);
                }
            }
        }
        
        return {farthest, maxDist};
    }
    
    void dfsCentroid(int u, int parent, vector<vector<int>>& graph, vector<int>& size, 
                    vector<int>& centroids, int n) {
        size[u] = 1;
        bool isCentroid = true;
        
        for (int v : graph[u]) {
            if (v != parent) {
                dfsCentroid(v, u, graph, size, centroids, n);
                size[u] += size[v];
                if (size[v] > n / 2) {
                    isCentroid = false;
                }
            }
        }
        
        // 检查删除u后的最大连通分量
        if (n - size[u] > n / 2) {
            isCentroid = false;
        }
        
        if (isCentroid) {
            centroids.push_back(u);
        }
    }
    
    int dfsPathSum(TreeNode* node, long long currentSum, int target, unordered_map<long long, int>& prefixSum) {
        if (!node) return 0;
        
        currentSum += node->val;
        int count = prefixSum.count(currentSum - target) ? prefixSum[currentSum - target] : 0;
        
        prefixSum[currentSum]++;
        
        count += dfsPathSum(node->left, currentSum, target, prefixSum);
        count += dfsPathSum(node->right, currentSum, target, prefixSum);
        
        prefixSum[currentSum]--;
        return count;
    }
};

// 单元测试
int main() {
    AdvancedTreeDP solver;
    
    // 测试用例1：简单树的最大独立集
    vector<vector<int>> graph1(4);
    graph1[0] = {1, 2};
    graph1[1] = {0, 3};
    graph1[2] = {0};
    graph1[3] = {1};
    
    cout << "最大独立集: " << solver.treeMaxIndependentSet(graph1) << endl;
    
    // 测试用例2：最小顶点覆盖
    cout << "最小顶点覆盖: " << solver.treeMinVertexCover(graph1) << endl;
    
    // 测试用例3：树的k着色
    vector<vector<int>> edges = {{0,1}, {0,2}, {1,3}};
    cout << "3着色方案数: " << solver.treeKColoring(4, 3, edges) << endl;
    
    return 0;
}
#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
#include <functional>
#include <queue>
#include <stack>
#include <unordered_map>
#include <unordered_set>
#include <set>
#include <memory>
#include <utility>

using namespace std;

/**
 * 树形DP综合应用 - C++版本
 * 包含虚树构建、复杂状态设计、多约束条件等高级技术
 * 
 * 题目来源：Codeforces, AtCoder, 洛谷高级题目等
 * 算法类型：虚树DP、多状态DP、组合优化等
 * 
 * 相关题目:
 * 1. https://codeforces.com/contest/1187/problem/E (Tree Painting)
 * 2. https://codeforces.com/contest/1324/problem/F (Maximum White Subtree)
 * 3. https://atcoder.jp/contests/abc160/tasks/abc160_f (Distributing Integers)
 * 4. https://www.luogu.com.cn/problem/P2495 (最小消耗)
 * 5. https://www.luogu.com.cn/problem/P3246 (序列)
 */

class TreeDPComprehensive {
public:
    /**
     * 1. 虚树构建与应用
     * 问题描述：给定关键点集合，构建包含这些关键点的最小连通子图（虚树）
     * 应用场景：大规模树上多次查询的优化
     * 时间复杂度: O(k log k), 空间复杂度: O(k)
     */
    class VirtualTree {
    private:
        vector<vector<int>> graph;
        vector<int> depth;
        vector<vector<int>> parent;
        vector<int> dfn;
        int timer;
        int n, log;
        
    public:
        VirtualTree(const vector<vector<int>>& originalGraph) {
            this->n = originalGraph.size();
            this->graph = originalGraph;
            preprocess();
        }
        
    private:
        void preprocess() {
            // 计算深度和DFS序
            depth.resize(n, 0);
            dfn.resize(n, 0);
            timer = 0;
            
            // 计算对数深度
            log = 1;
            while ((1 << log) < n) log++;
            parent.resize(n, vector<int>(log, -1));
            
            dfsLCA(0, -1);
        }
        
        void dfsLCA(int u, int p) {
            dfn[u] = timer++;
            parent[u][0] = p;
            for (int i = 1; i < log; i++) {
                if (parent[u][i-1] != -1) {
                    parent[u][i] = parent[parent[u][i-1]][i-1];
                }
            }
            
            for (int v : graph[u]) {
                if (v != p) {
                    depth[v] = depth[u] + 1;
                    dfsLCA(v, u);
                }
            }
        }
        
        int lca(int u, int v) {
            if (depth[u] < depth[v]) {
                swap(u, v);
            }
            
            for (int i = log-1; i >= 0; i--) {
                if (depth[u] - (1 << i) >= depth[v]) {
                    u = parent[u][i];
                }
            }
            
            if (u == v) return u;
            
            for (int i = log-1; i >= 0; i--) {
                if (parent[u][i] != parent[v][i]) {
                    u = parent[u][i];
                    v = parent[v][i];
                }
            }
            
            return parent[u][0];
        }
        
    public:
        vector<vector<int>> buildVirtualTree(const vector<int>& keyPoints) {
            // 按DFS序排序关键点
            vector<int> sortedKeyPoints = keyPoints;
            sort(sortedKeyPoints.begin(), sortedKeyPoints.end(), 
                [this](int a, int b) { return dfn[a] < dfn[b]; });
            
            // 添加LCA节点
            unordered_set<int> virtualNodes(sortedKeyPoints.begin(), sortedKeyPoints.end());
            for (int i = 1; i < sortedKeyPoints.size(); i++) {
                virtualNodes.insert(lca(sortedKeyPoints[i-1], sortedKeyPoints[i]));
            }
            
            vector<int> sortedNodes(virtualNodes.begin(), virtualNodes.end());
            sort(sortedNodes.begin(), sortedNodes.end(), 
                [this](int a, int b) { return dfn[a] < dfn[b]; });
            
            // 构建虚树
            vector<vector<int>> virtualTree(n);
            
            stack<int> stk;
            stk.push(sortedNodes[0]);
            
            for (int i = 1; i < sortedNodes.size(); i++) {
                int u = sortedNodes[i];
                while (stk.size() > 1 && depth[stk.top()] > depth[lca(stk.top(), u)]) {
                    int v = stk.top();
                    stk.pop();
                    virtualTree[stk.top()].push_back(v);
                }
                
                int lcaNode = lca(stk.top(), u);
                if (stk.top() != lcaNode) {
                    virtualTree[lcaNode].push_back(stk.top());
                    stk.pop();
                    stk.push(lcaNode);
                }
                stk.push(u);
            }
            
            while (stk.size() > 1) {
                int v = stk.top();
                stk.pop();
                virtualTree[stk.top()].push_back(v);
            }
            
            return virtualTree;
        }
    };
    
    /**
     * 2. 树上最大匹配（Maximum Matching）
     * 问题描述：选择最多的不相交边
     * 算法要点：树形DP + 匹配理论
     * 时间复杂度: O(n), 空间复杂度: O(n)
     */
    int treeMaximumMatching(const vector<vector<int>>& graph) {
        int n = graph.size();
        // dp[u][0]: u不参与匹配时的最大匹配数
        // dp[u][1]: u参与匹配时的最大匹配数
        vector<vector<int>> dp(n, vector<int>(2, 0));
        vector<bool> visited(n, false);
        
        dfsMatching(0, -1, graph, dp, visited);
        return max(dp[0][0], dp[0][1]);
    }
    
private:
    void dfsMatching(int u, int parent, const vector<vector<int>>& graph,
                    vector<vector<int>>& dp, vector<bool>& visited) {
        visited[u] = true;
        
        int sumNotMatched = 0;
        vector<int> children;
        
        for (int v : graph[u]) {
            if (v != parent && !visited[v]) {
                children.push_back(v);
                dfsMatching(v, u, graph, dp, visited);
                sumNotMatched += max(dp[v][0], dp[v][1]);
            }
        }
        
        dp[u][0] = sumNotMatched;
        
        // 计算u参与匹配的情况
        int maxWithMatching = 0;
        for (int v : children) {
            // u与v匹配，其他子节点可以自由选择
            int current = 1 + dp[v][0]; // u与v匹配
            for (int w : children) {
                if (w != v) {
                    current += max(dp[w][0], dp[w][1]);
                }
            }
            maxWithMatching = max(maxWithMatching, current);
        }
        
        dp[u][1] = maxWithMatching;
    }
    
public:
    /**
     * 3. 树上最小边覆盖
     * 问题描述：选择最少的边覆盖所有节点
     * 算法要点：树形DP，与最大匹配相关
     * 时间复杂度: O(n), 空间复杂度: O(n)
     */
    int treeMinimumEdgeCover(const vector<vector<int>>& graph) {
        int n = graph.size();
        // 最小边覆盖 = 节点数 - 最大匹配
        int maxMatching = treeMaximumMatching(graph);
        return n - 1 - maxMatching;
    }
    
    /**
     * 4. 树上带权最大匹配
     * 问题描述：每条边有权重，选择权重和最大的不相交边集合
     * 算法要点：带权树形DP
     * 时间复杂度: O(n), 空间复杂度: O(n)
     */
    int treeMaximumWeightedMatching(const vector<vector<pair<int, int>>>& weightedGraph) {
        int n = weightedGraph.size();
        // dp[u][0]: u不参与匹配的最大权重和
        // dp[u][1]: u参与匹配的最大权重和
        vector<vector<int>> dp(n, vector<int>(2, 0));
        vector<bool> visited(n, false);
        
        dfsWeightedMatching(0, -1, weightedGraph, dp, visited);
        return max(dp[0][0], dp[0][1]);
    }
    
private:
    void dfsWeightedMatching(int u, int parent, 
                            const vector<vector<pair<int, int>>>& weightedGraph,
                            vector<vector<int>>& dp, vector<bool>& visited) {
        visited[u] = true;
        
        int sumNotMatched = 0;
        vector<pair<int, int>> children; // pair<v, weight>
        
        for (auto& edge : weightedGraph[u]) {
            int v = edge.first, weight = edge.second;
            if (v != parent && !visited[v]) {
                children.push_back({v, weight});
                dfsWeightedMatching(v, u, weightedGraph, dp, visited);
                sumNotMatched += max(dp[v][0], dp[v][1]);
            }
        }
        
        dp[u][0] = sumNotMatched;
        
        // 计算u参与匹配的情况
        int maxWithMatching = 0;
        for (auto& child : children) {
            int v = child.first, weight = child.second;
            // u与v匹配
            int current = weight + dp[v][0]; // u与v匹配的权重
            for (auto& other : children) {
                if (other.first != v) {
                    current += max(dp[other.first][0], dp[other.first][1]);
                }
            }
            maxWithMatching = max(maxWithMatching, current);
        }
        
        dp[u][1] = maxWithMatching;
    }
    
public:
    /**
     * 5. 树上最小斯坦纳树（Steiner Tree）
     * 问题描述：连接关键点的最小权重子树
     * 算法要点：状态压缩DP + 树形DP
     * 时间复杂度: O(3^k * n + 2^k * n²), 空间复杂度: O(2^k * n)
     */
    int treeSteinerTree(const vector<vector<pair<int, int>>>& graph, 
                       const vector<int>& terminals) {
        int n = graph.size();
        int k = terminals.size();
        
        // 状态压缩：每个终端节点对应一个bit
        vector<vector<int>> dp(1 << k, vector<int>(n, INT_MAX / 2));
        
        // 初始化：单个终端节点
        for (int i = 0; i < k; i++) {
            int terminal = terminals[i];
            dp[1 << i][terminal] = 0;
        }
        
        // 状态转移
        for (int mask = 1; mask < (1 << k); mask++) {
            // 子树合并
            for (int u = 0; u < n; u++) {
                for (int submask = (mask - 1) & mask; submask > 0; submask = (submask - 1) & mask) {
                    dp[mask][u] = min(dp[mask][u], 
                        dp[submask][u] + dp[mask ^ submask][u]);
                }
            }
            
            // Dijkstra-like relaxation
            priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> pq;
            for (int u = 0; u < n; u++) {
                if (dp[mask][u] < INT_MAX / 2) {
                    pq.push({dp[mask][u], u});
                }
            }
            
            while (!pq.empty()) {
                auto [dist, u] = pq.top();
                pq.pop();
                if (dist > dp[mask][u]) continue;
                
                for (auto& edge : graph[u]) {
                    int v = edge.first, weight = edge.second;
                    int newDist = dist + weight;
                    if (newDist < dp[mask][v]) {
                        dp[mask][v] = newDist;
                        pq.push({newDist, v});
                    }
                }
            }
        }
        
        // 找到最小权重和
        int minCost = INT_MAX;
        for (int u = 0; u < n; u++) {
            minCost = min(minCost, dp[(1 << k) - 1][u]);
        }
        
        return minCost;
    }
    
    /**
     * 6. 树上路径覆盖问题
     * 问题描述：用最少的路径覆盖树的所有边，路径可以重叠
     * 算法要点：贪心 + 树形DP
     * 时间复杂度: O(n), 空间复杂度: O(n)
     */
    int treePathCover(const vector<vector<int>>& graph) {
        int n = graph.size();
        vector<int> dp(n, 0); // 以u为根的子树需要的最少路径数
        vector<bool> visited(n, false);
        
        dfsPathCover(0, -1, graph, dp, visited);
        return dp[0];
    }
    
private:
    void dfsPathCover(int u, int parent, const vector<vector<int>>& graph,
                     vector<int>& dp, vector<bool>& visited) {
        visited[u] = true;
        
        int leafCount = 0;
        int sumDp = 0;
        
        for (int v : graph[u]) {
            if (v != parent && !visited[v]) {
                dfsPathCover(v, u, graph, dp, visited);
                sumDp += dp[v];
                
                if (graph[v].size() == 1) { // 叶子节点
                    leafCount++;
                }
            }
        }
        
        if (graph[u].size() == 1 && parent != -1) {
            // 叶子节点（非根）
            dp[u] = 1;
        } else {
            // 内部节点
            dp[u] = sumDp - max(0, leafCount - 1);
        }
    }
};

// 单元测试函数
int main() {
    TreeDPComprehensive solver;
    
    // 测试最大匹配
    vector<vector<int>> graph = {
        {1, 2},
        {0, 3, 4},
        {0, 5},
        {1}, {1}, {2}
    };
    
    cout << "树上最大匹配数: " << solver.treeMaximumMatching(graph) << endl;
    
    // 测试最小边覆盖
    cout << "树上最小边覆盖: " << solver.treeMinimumEdgeCover(graph) << endl;
    
    // 测试虚树构建
    TreeDPComprehensive::VirtualTree vt(graph);
    vector<int> keyPoints = {3, 4, 5};
    auto virtualTree = vt.buildVirtualTree(keyPoints);
    
    cout << "虚树构建完成，节点数: " << virtualTree.size() << endl;
    
    return 0;
}
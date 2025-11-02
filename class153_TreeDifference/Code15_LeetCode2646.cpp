#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>
#include <cstring>
using namespace std;

/**
 * LeetCode 2646. 最小化旅行的价格
 * 题目描述：给定一棵树，每个节点有一个价格。可以选择将某些节点的价格减半。
 * 有多个旅行路径，每个路径从u到v。要求最小化所有旅行路径的总价格。
 * 使用树上差分统计每条边被经过的次数，然后使用树形DP决策哪些节点减半。
 */

class Solution {
private:
    vector<vector<int>> graph;
    vector<int> price;
    vector<int> count; // 节点被经过的次数
    vector<vector<int>> dp; // dp[u][0]:不减半, dp[u][1]:减半
    
    void dfsCount(int u, int parent) {
        for (int v : graph[u]) {
            if (v != parent) {
                dfsCount(v, u);
                count[u] += count[v];
            }
        }
    }
    
    void dfsDP(int u, int parent) {
        // 不减半的情况
        dp[u][0] = price[u] * count[u];
        // 减半的情况
        dp[u][1] = (price[u] / 2) * count[u];
        
        for (int v : graph[u]) {
            if (v != parent) {
                dfsDP(v, u);
                // 当前节点不减半，子节点可以减半或不减半
                dp[u][0] += min(dp[v][0], dp[v][1]);
                // 当前节点减半，子节点不能减半
                dp[u][1] += dp[v][0];
            }
        }
    }
    
    int getLCA(int u, int v, vector<vector<int>>& parent, vector<int>& depth, int LOG) {
        if (depth[u] < depth[v]) {
            swap(u, v);
        }
        
        // 将u提升到和v同一深度
        for (int j = LOG - 1; j >= 0; j--) {
            if (depth[u] - (1 << j) >= depth[v]) {
                u = parent[u][j];
            }
        }
        
        if (u == v) return u;
        
        // 同时向上提升
        for (int j = LOG - 1; j >= 0; j--) {
            if (parent[u][j] != parent[v][j]) {
                u = parent[u][j];
                v = parent[v][j];
            }
        }
        
        return parent[u][0];
    }
    
public:
    int minimumTotalPrice(int n, vector<vector<int>>& edges, vector<int>& price, vector<vector<int>>& trips) {
        // 构建图
        graph.resize(n);
        for (auto& edge : edges) {
            int u = edge[0], v = edge[1];
            graph[u].push_back(v);
            graph[v].push_back(u);
        }
        
        this->price = price;
        count.resize(n, 0);
        
        // 预处理LCA
        int LOG = 20;
        vector<vector<int>> parent(n, vector<int>(LOG, -1));
        vector<int> depth(n, -1);
        
        // BFS预处理深度和父节点
        depth[0] = 0;
        queue<int> q;
        q.push(0);
        
        while (!q.empty()) {
            int u = q.front();
            q.pop();
            for (int v : graph[u]) {
                if (depth[v] == -1) {
                    depth[v] = depth[u] + 1;
                    parent[v][0] = u;
                    q.push(v);
                }
            }
        }
        
        // 预处理倍增数组
        for (int j = 1; j < LOG; j++) {
            for (int i = 0; i < n; i++) {
                if (parent[i][j-1] != -1) {
                    parent[i][j] = parent[parent[i][j-1]][j-1];
                }
            }
        }
        
        // 树上差分统计每条边被经过的次数
        for (auto& trip : trips) {
            int u = trip[0], v = trip[1];
            int lca = getLCA(u, v, parent, depth, LOG);
            
            count[u]++;
            count[v]++;
            count[lca] -= 2;
        }
        
        // DFS统计每个节点被经过的次数
        dfsCount(0, -1);
        
        // 树形DP
        dp.resize(n, vector<int>(2, 0));
        dfsDP(0, -1);
        
        return min(dp[0][0], dp[0][1]);
    }
};

int main() {
    Solution solution;
    
    // 测试用例1
    int n1 = 4;
    vector<vector<int>> edges1 = {{0,1},{1,2},{1,3}};
    vector<int> price1 = {2,2,10,6};
    vector<vector<int>> trips1 = {{0,3},{2,1},{2,3}};
    cout << solution.minimumTotalPrice(n1, edges1, price1, trips1) << endl; // 输出: 23
    
    // 测试用例2
    int n2 = 2;
    vector<vector<int>> edges2 = {{0,1}};
    vector<int> price2 = {2,2};
    vector<vector<int>> trips2 = {{0,0}};
    cout << solution.minimumTotalPrice(n2, edges2, price2, trips2) << endl; // 输出: 1
    
    return 0;
}
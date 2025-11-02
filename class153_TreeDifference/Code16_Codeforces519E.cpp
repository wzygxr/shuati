#include <iostream>
#include <vector>
#include <cmath>
#include <algorithm>
using namespace std;

/**
 * Codeforces 519E. A and B and Lecture Rooms
 * 题目描述：给定一棵树，有多个查询，每个查询给出两个节点u和v，
 * 要求找到树上到u和v距离相等的节点数量。
 * 使用LCA和树上差分思想解决。
 */

class Solution {
private:
    vector<vector<int>> graph;
    vector<vector<int>> parent;
    vector<int> depth;
    vector<int> size; // 子树大小
    int LOG;
    int n;
    
    void dfs(int u, int p) {
        parent[u][0] = p;
        depth[u] = depth[p] + 1;
        size[u] = 1;
        
        for (int v : graph[u]) {
            if (v != p) {
                dfs(v, u);
                size[u] += size[v];
            }
        }
    }
    
    int getLCA(int u, int v) {
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
    
    int getKthParent(int u, int k) {
        for (int j = 0; j < LOG; j++) {
            if (k & (1 << j)) {
                u = parent[u][j];
            }
        }
        return u;
    }
    
    int query(int u, int v) {
        if (u == v) return n; // 所有节点都满足
        
        int lca = getLCA(u, v);
        int dist = depth[u] + depth[v] - 2 * depth[lca];
        
        if (dist % 2 == 1) return 0; // 距离为奇数，没有满足的节点
        
        int midDist = dist / 2;
        
        if (depth[u] == depth[v]) {
            // u和v在同一深度，中点在lca
            int uMid = getKthParent(u, midDist - 1);
            int vMid = getKthParent(v, midDist - 1);
            return n - size[uMid] - size[vMid];
        } else {
            // u和v在不同深度，找到中点
            if (depth[u] < depth[v]) {
                swap(u, v);
            }
            
            int mid = getKthParent(u, midDist);
            int prev = getKthParent(u, midDist - 1);
            return size[mid] - size[prev];
        }
    }
    
public:
    vector<int> solve(int n, vector<vector<int>>& edges, vector<vector<int>>& queries) {
        this->n = n;
        // 构建图（节点编号从1开始）
        graph.resize(n + 1);
        for (auto& edge : edges) {
            int u = edge[0], v = edge[1];
            graph[u].push_back(v);
            graph[v].push_back(u);
        }
        
        // 预处理
        LOG = log2(n) + 1;
        parent.resize(n + 1, vector<int>(LOG, 0));
        depth.resize(n + 1);
        size.resize(n + 1);
        
        // DFS预处理
        dfs(1, 0);
        
        // 预处理倍增数组
        for (int j = 1; j < LOG; j++) {
            for (int i = 1; i <= n; i++) {
                if (parent[i][j-1] != 0) {
                    parent[i][j] = parent[parent[i][j-1]][j-1];
                }
            }
        }
        
        vector<int> result;
        for (auto& q : queries) {
            int u = q[0], v = q[1];
            result.push_back(query(u, v));
        }
        
        return result;
    }
};

int main() {
    Solution solution;
    
    // 测试用例
    int n = 4;
    vector<vector<int>> edges = {{1,2}, {1,3}, {2,4}};
    vector<vector<int>> queries = {{1,2}, {2,3}, {3,4}, {2,4}};
    
    vector<int> result = solution.solve(n, edges, queries);
    for (int res : result) {
        cout << res << " ";
    }
    cout << endl; // 输出: 2 1 1 1
    
    return 0;
}
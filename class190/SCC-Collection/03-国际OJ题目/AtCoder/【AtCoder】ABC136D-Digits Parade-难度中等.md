# AtCoder SCC 相关题目集

## 说明

AtCoder 平台上直接以 SCC 为主题的题目相对较少，但以下题目涉及强连通分量的应用：

## 推荐题目

### 1. ABC 214 F - Substrings
- **链接**: https://atcoder.jp/contests/abc214/tasks/abc214_f
- **类型**: 字符串 + DAG DP
- **关联**: 可以通过 SCC 将问题转化为 DAG 上的 DP

### 2. ARC 100 E - Or Plus Max
- **链接**: https://atcoder.jp/contests/arc100/tasks/arc100_c
- **类型**: 位运算 + 图论

### 3. Educational DP Contest - Z - Frog 3
- **链接**: https://atcoder.jp/contests/dp/tasks/dp_z
- **类型**: DP优化

## SCC 相关练习建议

在 AtCoder 上练习 SCC，建议：

1. **ABC 280 F** - 并查集 + 图论
2. **ARC 119 C** - 图论基础
3. **典型 DP  contest** - 学习 DAG DP，为 SCC 缩点后的处理打基础

## 通用模板代码

```cpp
#include <bits/stdc++.h>
using namespace std;

// SCC 模板（AtCoder 风格）
struct SCC {
    int n;
    vector<vector<int>> adj, radj;
    vector<int> order, component;
    vector<bool> used;
    
    SCC(int n) : n(n) {
        adj.resize(n);
        radj.resize(n);
    }
    
    void add_edge(int u, int v) {
        adj[u].push_back(v);
        radj[v].push_back(u);
    }
    
    void dfs1(int v) {
        used[v] = true;
        for (int u : adj[v]) {
            if (!used[u]) dfs1(u);
        }
        order.push_back(v);
    }
    
    void dfs2(int v) {
        used[v] = true;
        component.push_back(v);
        for (int u : radj[v]) {
            if (!used[u]) dfs2(u);
        }
    }
    
    // 返回缩点后的图和每个节点所属的 SCC 编号
    pair<vector<vector<int>>, vector<int>> build() {
        used.assign(n, false);
        for (int i = 0; i < n; i++) {
            if (!used[i]) dfs1(i);
        }
        
        used.assign(n, false);
        reverse(order.begin(), order.end());
        
        vector<int> scc_id(n);
        int cnt = 0;
        
        for (int v : order) {
            if (!used[v]) {
                dfs2(v);
                for (int u : component) {
                    scc_id[u] = cnt;
                }
                component.clear();
                cnt++;
            }
        }
        
        // 构建缩点后的图
        vector<vector<int>> dag(cnt);
        for (int v = 0; v < n; v++) {
            for (int u : adj[v]) {
                if (scc_id[v] != scc_id[u]) {
                    dag[scc_id[v]].push_back(scc_id[u]);
                }
            }
        }
        
        // 去重
        for (auto& v : dag) {
            sort(v.begin(), v.end());
            v.erase(unique(v.begin(), v.end()), v.end());
        }
        
        return {dag, scc_id};
    }
};

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n, m;
    cin >> n >> m;
    
    SCC scc(n);
    for (int i = 0; i < m; i++) {
        int u, v;
        cin >> u >> v;
        u--; v--;  // 转为 0-based
        scc.add_edge(u, v);
    }
    
    auto [dag, scc_id] = scc.build();
    
    // 后续处理...
    
    return 0;
}
```

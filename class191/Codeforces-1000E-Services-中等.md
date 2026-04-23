# Codeforces-1000E-Services-中等

## 题目信息

- **题目链接**: https://codeforces.com/problemset/problem/1000/E
- **难度**: 中等
- **考察点**: 边双连通分量、LCA、树的直径

## 题目描述

### 题目描述

给定一个无向连通图，有n个节点和m条边。求图中任意两点间路径上的最大割边数量。

### 输入格式

```
n m
m行：u v
```

### 输出格式

输出最大割边数量。

## 解题思路

### 核心原理

1. 求所有割边
2. 边双缩点，得到缩点树
3. 在缩点树上求树的直径，即为答案

## 完整代码实现（C++）

```cpp
#include <bits/stdc++.h>
using namespace std;

// ==================== Codeforces 1000E Services ====================
// 考察点：割边+e-DCC缩点+树的直径+LCA
// 时间复杂度：O(n + m)
// 空间复杂度：O(n + m)

const int MAXN = 1000005;
const int MAXM = 2000005;

struct Edge {
    int to;
    int rev;
    int id;
    Edge(int _to, int _rev, int _id) : to(_to), rev(_rev), id(_id) {}
};

vector<Edge> adj[MAXN];
vector<int> tree[MAXN];

int n, m;
int a[MAXM], b[MAXM];

// Tarjan
int dfn[MAXN], low[MAXN], timestamp = 0;
bool is_bridge[MAXM];
int e_dcc_id[MAXN], dcc_cnt = 0;
stack<int> st;

// 树的直径
int max_dist = 0, far_node = 0;
int depth[MAXN];
int up[MAXN][20];

/**
 * Tarjan算法 - 求割边和e-DCC
 */
void tarjan(int u, int edge_id) {
    dfn[u] = low[u] = ++timestamp;
    st.push(u);
    
    for (auto &e : adj[u]) {
        int v = e.to;
        int id = e.id;
        
        if (!dfn[v]) {
            tarjan(v, id);
            low[u] = min(low[u], low[v]);
            
            if (low[v] > dfn[u]) {
                is_bridge[id] = true;
            }
        } else if (id != edge_id) {
            low[u] = min(low[u], dfn[v]);
        }
    }
    
    if (dfn[u] == low[u]) {
        dcc_cnt++;
        int pop;
        do {
            pop = st.top();
            st.pop();
            e_dcc_id[pop] = dcc_cnt;
        } while (pop != u);
    }
}

/**
 * 构建缩点树
 */
void buildTree() {
    for (int i = 1; i <= m; i++) {
        int u = a[i], v = b[i];
        if (is_bridge[i] && e_dcc_id[u] != e_dcc_id[v]) {
            tree[e_dcc_id[u]].push_back(e_dcc_id[v]);
            tree[e_dcc_id[v]].push_back(e_dcc_id[u]);
        }
    }
}

/**
 * DFS - 求树的直径
 */
void dfs(int u, int fa, int dist) {
    if (dist > max_dist) {
        max_dist = dist;
        far_node = u;
    }
    
    depth[u] = depth[fa] + 1;
    up[u][0] = fa;
    for (int i = 1; i < 20; i++) {
        up[u][i] = up[up[u][i-1]][i-1];
    }
    
    for (int v : tree[u]) {
        if (v != fa) {
            dfs(v, u, dist + 1);
        }
    }
}

/**
 * 获取树的直径
 */
int getDiameter() {
    max_dist = 0;
    dfs(1, 0, 0);
    max_dist = 0;
    dfs(far_node, 0, 0);
    return max_dist;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    cin >> n >> m;
    
    for (int i = 1; i <= m; i++) {
        int u, v;
        cin >> u >> v;
        a[i] = u;
        b[i] = v;
        adj[u].push_back(Edge(v, adj[v].size(), i));
        adj[v].push_back(Edge(u, adj[u].size() - 1, i));
    }
    
    // Tarjan
    tarjan(1, 0);
    
    // 缩点
    buildTree();
    
    // 求树的直径
    int ans = getDiameter();
    cout << ans << endl;
    
    return 0;
}
```

## 复杂度分析

- **时间复杂度**：O(n + m)
  - Tarjan：O(n + m)
  - 缩点：O(m)
  - 树的直径：O(n)

- **空间复杂度**：O(n + m)

## 机器学习关联

1. **图嵌入**：树的直径作为图的全局结构特征
2. **GNN**：缩点后计算更高效
3. **图分类**：直径可作为图级别特征

# 【Codeforces】CF652E - Pursuing Pursuits（边双缩点+路径查询）

## 一、题目信息

### 1.1 原始链接
- Codeforces：http://codeforces.com/problemset/problem/652/E

### 1.2 题目描述

给定一个无向图和若干查询，每个查询给出两个顶点，需要判断两点之间是否存在路径，且该路径上是否存在一条边的权值大于给定的阈值。

**输入格式**：
- 第一行：n, m, q
- 接下来 m 行：每行三个整数 u, v, w，表示无向边和权值
- 接下来 q 行：每行三个整数 s, t, k

**输出格式**：
- 对每个查询输出答案

---

## 二、算法分析

### 核心思路

```
1. 边双缩点得到树
2. 在树上处理路径查询
3. 使用线段树或ST表维护路径最值
```

---

## 三、代码实现（C++）

```cpp
#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>
using namespace std;

const int MAXN = 200005;

struct Edge {
    int to;
    int w;
    int next;
    int id;
} edges[MAXN << 1];

int head[MAXN], cnt = 0;
int dfn[MAXN], low[MAXN], timestamp = 0;
bool is_bridge[MAXN << 1];
int e_dcc_id[MAXN], dcc_cnt = 0;
vector<int> tree[MAXN];

void add_edge(int u, int v, int w) {
    edges[cnt].to = v;
    edges[cnt].w = w;
    edges[cnt].next = head[u];
    edges[cnt].id = cnt;
    head[u] = cnt++;
    
    edges[cnt].to = u;
    edges[cnt].w = w;
    edges[cnt].next = head[v];
    edges[cnt].id = cnt;
    head[v] = cnt++;
}

/**
 * Tarjan算法求割边
 */
void tarjan(int u, int parent_edge) {
    dfn[u] = low[u] = ++timestamp;
    
    for (int e = head[u]; e != -1; e = edges[e].next) {
        int v = edges[e].to;
        int edge_id = edges[e].id;
        
        if (edge_id == parent_edge) continue;
        
        if (!dfn[v]) {
            tarjan(v, edge_id);
            low[u] = min(low[u], low[v]);
            
            if (low[v] > dfn[u]) {
                is_bridge[edge_id] = true;
            }
        } else {
            low[u] = min(low[u], dfn[v]);
        }
    }
}

/**
 * 求解边双连通分量
 */
void dfs_dcc(int u) {
    e_dcc_id[u] = dcc_cnt;
    
    for (int e = head[u]; e != -1; e = edges[e].next) {
        int v = edges[e].to;
        int edge_id = edges[e].id;
        
        if (!e_dcc_id[v] && !is_bridge[edge_id]) {
            dfs_dcc(v);
        }
    }
}

/**
 * 边双缩点
 */
void shrink(int n) {
    for (int i = 1; i <= n; i++) {
        if (!e_dcc_id[i]) {
            dcc_cnt++;
            dfs_dcc(i);
        }
    }
    
    for (int u = 1; u <= n; u++) {
        for (int e = head[u]; e != -1; e = edges[e].next) {
            int v = edges[e].to;
            int edge_id = edges[e].id;
            
            if (is_bridge[edge_id] && e_dcc_id[u] != e_dcc_id[v]) {
                tree[e_dcc_id[u]].push_back(e_dcc_id[v]);
                tree[e_dcc_id[v]].push_back(e_dcc_id[u]);
            }
        }
    }
}

// LCA相关
const int LOG = 20;
int parent[MAXN][LOG];
int depth[MAXN];
int maxEdge[MAXN][LOG];

void dfs_lca(int u, int fa) {
    parent[u][0] = fa;
    
    for (int i = 1; i < LOG; i++) {
        parent[u][i] = parent[parent[u][i-1]][i-1];
        maxEdge[u][i] = max(maxEdge[u][i-1], maxEdge[parent[u][i-1]][i-1]);
    }
    
    for (int v : tree[u]) {
        if (v == fa) continue;
        depth[v] = depth[u] + 1;
        dfs_lca(v, u);
    }
}

/**
 * 查询路径上的最大边权
 */
int query_max(int u, int v) {
    int ans = 0;
    
    if (depth[u] < depth[v]) swap(u, v);
    
    int diff = depth[u] - depth[v];
    for (int i = LOG-1; i >= 0; i--) {
        if (diff & (1 << i)) {
            ans = max(ans, maxEdge[u][i]);
            u = parent[u][i];
        }
    }
    
    if (u == v) return ans;
    
    for (int i = LOG-1; i >= 0; i--) {
        if (parent[u][i] != parent[v][i]) {
            ans = max(ans, maxEdge[u][i]);
            ans = max(ans, maxEdge[v][i]);
            u = parent[u][i];
            v = parent[v][i];
        }
    }
    
    ans = max(ans, maxEdge[u][0]);
    ans = max(ans, maxEdge[v][0]);
    
    return ans;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n, m, q;
    cin >> n >> m >> q;
    
    memset(head, -1, sizeof(head));
    memset(dfn, 0, sizeof(dfn));
    
    // 读入边
    for (int i = 0; i < m; i++) {
        int u, v, w;
        cin >> u >> v >> w;
        add_edge(u, v, w);
    }
    
    // Tarjan
    for (int i = 1; i <= n; i++) {
        if (!dfn[i]) tarjan(i, -1);
    }
    
    // 缩点
    shrink(n);
    
    // 预处理LCA
    dfs_lca(1, 0);
    
    // 处理查询
    while (q--) {
        int s, t, k;
        cin >> s >> t >> k;
        
        s = e_dcc_id[s];
        t = e_dcc_id[t];
        
        int max_w = query_max(s, t);
        
        if (max_w > k) {
            cout << "YES\n";
        } else {
            cout << "NO\n";
        }
    }
    
    return 0;
}
```

---

## 四、时间复杂度

| 操作 | 复杂度 |
|------|--------|
| Tarjan | O(n+m) |
| 缩点 | O(n+m) |
| LCA预处理 | O(n log n) |
| 查询 | O(log n) |

---

## 五、机器学习/深度学习关联

### 路径查询在图神经网络中的应用

路径查询可用于：
- 图结构特征提取
- 节点间关系分析
- 路径注意力机制

---

## 六、同类题目

- CF555E
- CF1000E

---

*本文档详细分析了Codeforces CF652E题目。*

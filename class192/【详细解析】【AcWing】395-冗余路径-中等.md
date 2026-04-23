# 【AcWing】395 - 冗余路径（边双连通分量+缩点）

## 一、题目信息

### 1.1 原始链接
- AcWing：https://www.acwing.com/problem/content/description/395/

### 1.2 题目描述

给定一个连通的无向图，需要添加最少的边，使得整个图变成边双连通图。

**输入格式**：
- 第一行：n, m（节点数，边数）
- 接下来 m 行：每行两个整数 u, v，表示无向边

**输出格式**：
- 输出使图边双连通所需添加的最少边数

**数据范围**：
- 1 ≤ n ≤ 5000
- 1 ≤ m ≤ 10000

### 1.3 样例输入

```
3 2
1 2
2 3
```

### 1.4 样例输出

```
1
```

---

## 二、算法分析

### 核心思路

```
1. Tarjan求割边
2. 边双缩点得到树
3. 统计叶子节点数量
4. 答案 = (leaf + 1) / 2
```

### 割边判定定理
```
边 (u, v) 是割边 <=> low[v] > dfn[u]
```

---

## 三、代码实现（C++）

```cpp
#include <iostream>
#include <vector>
#include <cstring>
using namespace std;

const int MAXN = 5005;
const int MAXM = 10005;

struct Edge {
    int to;
    int next;
    int id;
} edges[MAXM << 1];

int head[MAXN], cnt = 0;
int dfn[MAXN], low[MAXN], timestamp = 0;
bool is_bridge[MAXM << 1];
int e_dcc_id[MAXN], dcc_cnt = 0;
vector<int> tree[MAXN];

void add_edge(int u, int v) {
    edges[cnt].to = v;
    edges[cnt].next = head[u];
    edges[cnt].id = cnt;
    head[u] = cnt++;
    
    edges[cnt].to = u;
    edges[cnt].next = head[v];
    edges[cnt].id = cnt;
    head[v] = cnt++;
}

/**
 * Tarjan算法求割边
 * 面试考点：
 * - 为什么用边编号而不是父节点
 * - 割边判定条件
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
            
            // 割边判定
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

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n, m;
    while (cin >> n >> m) {
        // 初始化
        memset(head, -1, sizeof(head));
        memset(dfn, 0, sizeof(dfn));
        memset(is_bridge, false, sizeof(is_bridge));
        memset(e_dcc_id, 0, sizeof(e_dcc_id));
        cnt = 0;
        timestamp = 0;
        dcc_cnt = 0;
        
        for (int i = 0; i <= n; i++) {
            tree[i].clear();
        }
        
        // 读入边
        for (int i = 0; i < m; i++) {
            int u, v;
            cin >> u >> v;
            add_edge(u, v);
        }
        
        // Tarjan
        tarjan(1, -1);
        
        // 缩点
        shrink(n);
        
        // 统计叶子
        if (dcc_cnt == 1) {
            cout << 0 << endl;
            continue;
        }
        
        int leaf = 0;
        for (int i = 1; i <= dcc_cnt; i++) {
            if (tree[i].size() == 1) leaf++;
        }
        
        cout << (leaf + 1) / 2 << endl;
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
| 叶子统计 | O(n) |

---

## 五、机器学习/深度学习关联

### 边双在社区检测中的应用

边双连通分量可以用于：
- 识别图的社区结构
- 图聚类任务
- 特征工程

---

## 六、同类题目

- POJ 3177
- POJ 3352
- 洛谷 P2860

---

*本文档详细分析了AcWing 395题目。*

# 【Codeforces】999E - Reachability from the Capital

## 题目信息

- **题目链接**: https://codeforces.com/problemset/problem/999/E
- **难度**: 中等 (Div2 E)
- **算法标签**: 强连通分量、缩点、贪心

## 题目描述

某国有n个城市，s是首都。有些城市之间有单向道路。

你需要添加尽可能少的道路，使得从首都s可以到达所有城市。

## 输入输出格式

### 输入格式
第一行三个整数n,m,s，表示城市数、道路数、首都
接下来m行，每行u,v表示u到v有一条道路

### 输出格式
输出需要添加的最少道路数

### 数据范围
- $1 \leq n \leq 5000$
- $0 \leq m \leq min(5000, n(n-1)/2)$
- $1 \leq s \leq n$

## 笔试/面试考察点分析

### 核心考察点
1. **缩点后入度为0的SCC**：需要连接的"源头"
2. **首都所在SCC的处理**：首都所在的SCC不需要额外连接
3. **贪心策略**：每个入度为0的SCC都需要至少一条入边

### 面试口述要点
- 求SCC并缩点，得到DAG
- 在DAG中，入度为0的SCC无法从其他SCC到达
- 需要确保从首都s可以到达所有入度为0的SCC
- 答案 = 入度为0的SCC数量 - (s所在SCC是否入度为0 ? 1 : 0)

## 解题思路

### 关键观察
- 缩点后，每个入度为0的SCC都需要至少一条入边才能被到达
- 从首都s向每个入度为0的SCC（除了s所在的SCC）添加一条边即可

### 算法流程
1. Tarjan求SCC
2. 缩点计算每个SCC的入度
3. 统计入度为0的SCC数量
4. 如果首都所在SCC入度为0，减1

## 完整代码实现

```cpp
#include <bits/stdc++.h>
using namespace std;

const int MAXN = 5005;

vector<int> adj[MAXN];

int dfn[MAXN], low[MAXN], timestamp;
stack<int> st;
bool in_stack[MAXN];

int scc_id[MAXN], scc_cnt;
int in_deg[MAXN];            // SCC的入度

void tarjan(int u) {
    dfn[u] = low[u] = ++timestamp;
    st.push(u);
    in_stack[u] = true;
    
    for (int v : adj[u]) {
        if (!dfn[v]) {
            tarjan(v);
            low[u] = min(low[u], low[v]);
        } else if (in_stack[v]) {
            low[u] = min(low[u], dfn[v]);
        }
    }
    
    if (dfn[u] == low[u]) {
        scc_cnt++;
        int v;
        do {
            v = st.top(); st.pop();
            in_stack[v] = false;
            scc_id[v] = scc_cnt;
        } while (v != u);
    }
}

void shrink(int n) {
    for (int u = 1; u <= n; u++) {
        for (int v : adj[u]) {
            if (scc_id[u] != scc_id[v]) {
                in_deg[scc_id[v]]++;
            }
        }
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n, m, s;
    cin >> n >> m >> s;
    
    for (int i = 0; i < m; i++) {
        int u, v;
        cin >> u >> v;
        adj[u].push_back(v);
    }
    
    memset(dfn, 0, sizeof(dfn));
    timestamp = 0;
    scc_cnt = 0;
    
    for (int i = 1; i <= n; i++) {
        if (!dfn[i]) tarjan(i);
    }
    
    shrink(n);
    
    // 统计入度为0的SCC
    int zero_in = 0;
    for (int i = 1; i <= scc_cnt; i++) {
        if (in_deg[i] == 0) {
            zero_in++;
        }
    }
    
    // 如果s所在的SCC入度为0，不需要额外加边
    if (in_deg[scc_id[s]] == 0) {
        zero_in--;
    }
    
    cout << zero_in << endl;
    
    return 0;
}
```

## 复杂度分析

- **时间**：$O(n + m)$
- **空间**：$O(n + m)$

## ML/DL关联

### 网络连通性优化
- **图神经网络**：用GNN预测需要添加的最少边
- **强化学习**：学习最优边添加策略

## 笔试面试问题

**Q: 为什么答案是入度为0的SCC数？**
> DAG中入度为0的节点无法从其他节点到达。要使所有节点可达，每个入度为0的SCC都需要至少一条入边。从首都向这些SCC各加一条边即可。

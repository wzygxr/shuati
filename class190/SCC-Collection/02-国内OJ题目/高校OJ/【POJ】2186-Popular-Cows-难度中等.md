# 【POJ】2186 - Popular Cows

## 题目信息

- **题目链接**: http://poj.org/problem?id=2186
- **难度**: 中等
- **算法标签**: 强连通分量、缩点

## 题目描述

每头奶牛都梦想成为牛棚里最受欢迎的奶牛。被所有奶牛喜欢的奶牛就是一头明星奶牛。

奶牛喜欢关系具有传递性。

求有多少头明星奶牛。

## 输入输出格式

### 输入格式
第一行两个整数N,M
接下来M行，每行A,B表示A喜欢B

### 输出格式
输出明星奶牛的数量

### 数据范围
- $1 \leq N \leq 10000$
- $1 \leq M \leq 50000$

## 解题思路

本题与AcWing 1174、洛谷P2341类似，都是求出度为0的SCC大小。

## 完整代码实现

```cpp
#include <iostream>
#include <vector>
#include <stack>
#include <cstring>
#include <algorithm>
using namespace std;

const int MAXN = 10005;

vector<int> adj[MAXN];
vector<int> dag[MAXN];

int dfn[MAXN], low[MAXN], timestamp;
stack<int> st;
bool in_stack[MAXN];

int scc_id[MAXN], scc_size[MAXN], scc_cnt;
int out_deg[MAXN];

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
            scc_size[scc_cnt]++;
        } while (v != u);
    }
}

void shrink(int n) {
    for (int u = 1; u <= n; u++) {
        for (int v : adj[u]) {
            if (scc_id[u] != scc_id[v]) {
                dag[scc_id[u]].push_back(scc_id[v]);
                out_deg[scc_id[u]]++;
            }
        }
    }
}

int main() {
    int n, m;
    cin >> n >> m;
    
    for (int i = 0; i < m; i++) {
        int u, v;
        cin >> u >> v;
        adj[u].push_back(v);
    }
    
    memset(dfn, 0, sizeof(dfn));
    memset(low, 0, sizeof(low));
    memset(in_stack, false, sizeof(in_stack));
    memset(scc_size, 0, sizeof(scc_size));
    memset(out_deg, 0, sizeof(out_deg));
    timestamp = 0;
    scc_cnt = 0;
    
    for (int i = 1; i <= n; i++) {
        if (!dfn[i]) tarjan(i);
    }
    
    shrink(n);
    
    int zero_out = 0, ans = 0;
    for (int i = 1; i <= scc_cnt; i++) {
        if (out_deg[i] == 0) {
            zero_out++;
            ans = scc_size[i];
        }
    }
    
    if (zero_out == 1) cout << ans << endl;
    else cout << 0 << endl;
    
    return 0;
}
```

## 复杂度分析

- **时间**：$O(N + M)$
- **空间**：$O(N + M)$

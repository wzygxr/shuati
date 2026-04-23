# 【AcWing】1174 - 受欢迎的牛

## 题目信息

- **题目链接**: https://www.acwing.com/problem/content/1176/
- **难度**: 中等
- **算法标签**: 强连通分量、缩点、图论

## 题目描述

每头奶牛都梦想成为牛棚里的明星。被所有奶牛喜欢的奶牛就是一头明星奶牛。

奶牛喜欢关系具有传递性：如果A喜欢B，B喜欢C，那么A也喜欢C。

给定N头奶牛和M对喜欢关系，求有多少头明星奶牛。

## 输入输出格式

### 输入格式
第一行两个整数N,M
接下来M行，每行两个整数A,B，表示A喜欢B

### 输出格式
输出明星奶牛的数量

### 数据范围
- $1 \leq N \leq 10000$
- $1 \leq M \leq 50000$

## 笔试/面试考察点分析

### 核心考察点
1. **传递闭包的理解**：喜欢关系的传递性
2. **出度为0的SCC**：明星奶牛所在SCC的特征
3. **唯一性判断**：需要唯一的出度为0的SCC

### 与P2863的对比
- P2863求"被邀请的群体"，看入度为0的SCC
- 本题求"被所有奶牛喜欢的奶牛"，看出度为0的SCC

## 解题思路

### 关键观察
- 明星奶牛必须被所有奶牛喜欢
- 缩点后，只有出度为0的SCC中的奶牛才可能被所有奶牛喜欢
- 如果有多个出度为0的SCC，则不存在明星奶牛

### 算法流程
1. Tarjan求SCC
2. 缩点构建DAG
3. 统计出度为0的SCC
4. 判断并输出结果

## 完整代码实现

```cpp
#include <bits/stdc++.h>
using namespace std;

const int MAXN = 10005;

vector<int> adj[MAXN];       // 原图邻接表
vector<int> dag[MAXN];       // 缩点后DAG

int dfn[MAXN], low[MAXN], timestamp;
stack<int> st;
bool in_stack[MAXN];

int scc_id[MAXN], scc_size[MAXN], scc_cnt;
int out_deg[MAXN];           // SCC的出度

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
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n, m;
    cin >> n >> m;
    
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

## ML/DL关联

### 社交网络分析
- **影响力最大化**：明星奶牛类似社交网络中的超级影响者
- **社区发现**：SCC对应紧密的社交圈子

## 笔试面试问题

**Q: 为什么看出度为0而不是入度为0？**
> 入度为0的SCC无法被其他SCC到达，不可能被所有奶牛喜欢。出度为0的SCC无法到达其他SCC，意味着所有其他SCC都能到达它（DAG中），所以它被所有奶牛喜欢。

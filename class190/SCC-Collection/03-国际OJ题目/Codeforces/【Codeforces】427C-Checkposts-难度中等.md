# 【Codeforces】427C - Checkposts

## 题目信息

- **题目链接**: https://codeforces.com/problemset/problem/427/C
- **难度**: 中等 (Div2 D)
- **算法标签**: 强连通分量、缩点、贪心

## 题目描述

某城市有n个区域，需要在一些区域建立检查点。每个区域有一个建立检查点的费用。

要求：
1. 每个区域必须被至少一个检查点监控
2. 如果区域a可以到达区域b，那么监控a的检查点也可以监控b

求最小总费用以及达到最小费用的方案数（对1e9+7取模）。

## 输入输出格式

### 输入格式
第一行n，表示区域数量
第二行n个数，表示每个区域建立检查点的费用
第三行m，表示道路数量
接下来m行，每行u,v表示u到v有一条有向道路

### 输出格式
输出两个数：最小总费用和方案数

### 数据范围
- $1 \leq n \leq 10^5$
- $1 \leq m \leq 3 \cdot 10^5$

## 笔试/面试考察点分析

### 核心考察点
1. **SCC内选最小值**：每个SCC内只需选一个最小费用点建检查点
2. **方案数计算**：最小值出现次数的乘积
3. **大数取模**：方案数需要对1e9+7取模

### 面试口述要点
- 首先求SCC，将图缩成DAG
- 在DAG中，入度为0的SCC必须建检查点（否则无法被监控）
- 实际上，每个SCC内只需要建一个检查点，选费用最小的
- 总费用 = 所有SCC最小费用之和
- 方案数 = 各SCC最小费用出现次数的乘积

## 解题思路

### 关键观察
- 同一SCC内的点互相可达，只需建一个检查点
- 选择SCC内费用最小的点建检查点
- 缩点后，每个SCC独立决策

### 算法流程
1. Tarjan求SCC
2. 对每个SCC，找到最小费用和出现次数
3. 累加最小费用，计算方案数乘积

## 完整代码实现

```cpp
#include <bits/stdc++.h>
using namespace std;

typedef long long ll;
const int MAXN = 100005;
const int MOD = 1e9 + 7;

vector<int> adj[MAXN];

int dfn[MAXN], low[MAXN], timestamp;
stack<int> st;
bool in_stack[MAXN];

int scc_id[MAXN], scc_cnt;
ll cost[MAXN];               // 每个区域的费用
ll min_cost[MAXN];           // 每个SCC的最小费用
int min_cnt[MAXN];           // 每个SCC最小费用出现次数

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
            
            // 更新SCC的最小费用
            if (cost[v] < min_cost[scc_cnt]) {
                min_cost[scc_cnt] = cost[v];
                min_cnt[scc_cnt] = 1;
            } else if (cost[v] == min_cost[scc_cnt]) {
                min_cnt[scc_cnt]++;
            }
        } while (v != u);
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n;
    cin >> n;
    
    // 读入费用
    for (int i = 1; i <= n; i++) {
        cin >> cost[i];
    }
    
    int m;
    cin >> m;
    
    // 建图
    for (int i = 0; i < m; i++) {
        int u, v;
        cin >> u >> v;
        adj[u].push_back(v);
    }
    
    // 初始化
    memset(dfn, 0, sizeof(dfn));
    memset(min_cost, 0x3f, sizeof(min_cost));  // 初始化为无穷大
    timestamp = 0;
    scc_cnt = 0;
    
    // 求SCC
    for (int i = 1; i <= n; i++) {
        if (!dfn[i]) tarjan(i);
    }
    
    // 计算答案
    ll total_cost = 0;
    ll ways = 1;
    
    for (int i = 1; i <= scc_cnt; i++) {
        total_cost += min_cost[i];
        ways = (ways * min_cnt[i]) % MOD;
    }
    
    cout << total_cost << " " << ways << endl;
    
    return 0;
}
```

## 复杂度分析

| 指标 | 复杂度 | 说明 |
|------|--------|------|
| 时间 | $O(n + m)$ | Tarjan线性算法 |
| 空间 | $O(n + m)$ | 邻接表+数组 |

## ML/DL关联

### 设施选址问题
- **图神经网络应用**：用GNN预测最优设施选址点
- **特征工程**：SCC相关的结构特征可以作为模型输入

### 资源分配优化
- **强化学习**：将SCC作为状态空间，学习最优资源分配策略
- **图卷积网络**：在缩点后的DAG上进行消息传递

## 笔试面试高频问题

**Q1: 为什么每个SCC只需要建一个检查点？**
> 同一SCC内的点互相可达，在一个点建检查点可以监控整个SCC。建多个是浪费。

**Q2: 如何理解"方案数"？**
> 如果某个SCC内有多个点都有最小费用，选择其中任意一个都可以。方案数是各SCC选择数的乘积。

**Q3: 如果不取模，方案数可能有多大？**
> 最坏情况下每个SCC都有很多最小值点，方案数是指数级的，必须用long long并取模。

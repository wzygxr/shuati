# AcWing 367 - 学校网络

## 题目链接
https://www.acwing.com/problem/content/369/

## 题目描述

一些学校连接在一个计算机网络上。学校之间存在软件支援协议，每个学校都有它应支援的学校名单（学校 A 支援学校 B，并不表示学校 B 一定要支援学校 A）。

当某校获得一个新软件时，无论是直接获得还是通过网络获得，该校都应将软件通过网络发送给所有它应支援的学校。

因此，一个新软件若想让所有学校都能使用，只需将其提供给一些学校即可。

**问题1**：初始至少需要向多少个学校发放软件，才能使软件通过网络到达所有学校？

**问题2**：至少需要添加几条支援关系，才能使得只向任意一个学校发放软件，就能使软件通过网络到达所有学校？

## 输入格式

第 1 行包含整数 N，表示学校数量。

第 2 到 N+1 行，每行包含若干个整数，第 i+1 行表示第 i 所学校应该支援的学校名单，以 0 结束（如果只有一个 0，表示该学校没有需要支援的学校）。

## 输出格式

输出两个问题的答案，每个答案占一行。

## 数据范围

2≤N≤100

## 样例输入
```
5
2 4 3 0
4 5 0
0
0
1 0
```

## 样例输出
```
1
2
```

## 解题思路

本题是 SCC 缩点的经典应用，涉及两个问题：

### 问题1：最少发放点

- 将强连通分量缩点后形成 DAG
- DAG 中入度为 0 的 SCC（源点）必须直接发放软件
- 答案 = 源点数量

### 问题2：最少添加边

- 目标：让整个图变成一个强连通分量
- 缩点后，设源点数量为 A，汇点数量为 B
- 答案 = max(A, B)
- 特殊情况：如果图本身已经强连通，答案为 0

## 代码实现

```cpp
#include <iostream>
#include <vector>
#include <stack>
#include <cstring>
#include <algorithm>
using namespace std;

const int MAXN = 105;

vector<int> adj[MAXN];
int dfn[MAXN], low[MAXN], timestamp = 0;
stack<int> st;
bool in_stack[MAXN];
int scc_id[MAXN], scc_cnt = 0;

void tarjan(int u) {
    dfn[u] = low[u] = ++timestamp;
    st.push(u);
    in_stack[u] = true;
    
    for (int v : adj[u]) {
        if (dfn[v] == 0) {
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

int main() {
    int n;
    cin >> n;
    
    for (int i = 1; i <= n; i++) {
        int x;
        while (cin >> x && x != 0) {
            adj[i].push_back(x);
        }
    }
    
    for (int i = 1; i <= n; i++) {
        if (dfn[i] == 0) tarjan(i);
    }
    
    // 统计入度和出度
    vector<int> indeg(scc_cnt + 1, 0), outdeg(scc_cnt + 1, 0);
    for (int u = 1; u <= n; u++) {
        for (int v : adj[u]) {
            if (scc_id[u] != scc_id[v]) {
                outdeg[scc_id[u]]++;
                indeg[scc_id[v]]++;
            }
        }
    }
    
    // 问题1：源点数量
    int source = 0, sink = 0;
    for (int i = 1; i <= scc_cnt; i++) {
        if (indeg[i] == 0) source++;
        if (outdeg[i] == 0) sink++;
    }
    
    cout << source << endl;
    
    // 问题2：如果只有一个 SCC，答案为 0；否则为 max(source, sink)
    if (scc_cnt == 1) {
        cout << 0 << endl;
    } else {
        cout << max(source, sink) << endl;
    }
    
    return 0;
}
```

## 复杂度分析

- 时间复杂度：O(N + M)
- 空间复杂度：O(N + M)

## 拓展思考

### 问题2 的证明

**目标**：通过添加最少的边，使得图强连通。

**分析**：
1. 缩点后形成 DAG，源点（入度为0）没有入边，汇点（出度为0）没有出边
2. 要将 DAG 变成强连通，需要让每个源点有入边，每个汇点有出边
3. 最优策略是将汇点连接到源点，形成环
4. 因此最少需要 max(源点数, 汇点数) 条边

# 【AcWing】368 - 鸣人的影分身（割边模板）

## 一、题目信息

### 1.1 原始链接
- AcWing：https://www.acwing.com/problem/content/368/

### 1.2 题目描述

给定无向图，求割边（桥）的数量。

---

## 二、考察点

| 考点 | 难度 |
|------|------|
| Tarjan割边 | ★☆☆☆☆ |
| 重边处理 | ★★☆☆☆ |
| 非连通图 | ★★☆☆☆ |

---

## 三、代码实现

```cpp
#include <bits/stdc++.h>
using namespace std;

const int MAXN = 100005;
const int MAXM = 200005;

struct Edge {
    int to, next, id;
};

Edge edges[MAXM << 1];
int head[MAXN], cnt;

int dfn[MAXN], low[MAXN], timestamp;
int bridgeCnt = 0;

void addEdge(int u, int v) {
    edges[cnt] = {v, head[u], cnt};
    head[u] = cnt++;
    edges[cnt] = {u, head[v], cnt};
    head[v] = cnt++;
}

void tarjan(int u, int parentEdge) {
    dfn[u] = low[u] = ++timestamp;
    for (int e = head[u]; e != -1; e = edges[e].next) {
        int v = edges[e].to;
        if (edges[e].id == parentEdge) continue;
        if (!dfn[v]) {
            tarjan(v, edges[e].id);
            low[u] = min(low[u], low[v]);
            if (low[v] > dfn[u]) {
                bridgeCnt++;
            }
        } else {
            low[u] = min(low[u], dfn[v]);
        }
    }
}

int main() {
    memset(head, -1, sizeof(head));
    int n, m;
    cin >> n >> m;
    for (int i = 0; i < m; i++) {
        int u, v;
        cin >> u >> v;
        addEdge(u, v);
    }
    for (int i = 1; i <= n; i++) {
        if (!dfn[i]) tarjan(i, -1);
    }
    cout << bridgeCnt << endl;
    return 0;
}
```

---

## 四、面试要点

### 必考点

- 割边判定：low[v] > dfn[u]
- 重边处理：边编号判断
- 时间复杂度：O(n+m)

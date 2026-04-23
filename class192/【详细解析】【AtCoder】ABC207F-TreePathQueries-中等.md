# 【AtCoder】ABC207F - Tree Path Queries（边双+路径查询）

## 一、题目信息

### 1.1 原始链接
- AtCoder：https://atcoder.jp/contests/abc207/tasks/abc207_f
- 题目难度：1800（中等）

### 1.2 题目描述

**题目背景**：在树上进行路径查询，是边双缩点后的经典操作。

**题目描述**：
- 给定一棵有 n 个节点的树
- 每条边有一个类型（颜色）
- 有 q 个查询，每个查询给出两个节点 u, v 和一种颜色 c
- 查询：从 u 到 v 的路径上，边类型为 c 的边有多少条？

**输入格式**：
- n, q
- n-1 条边：u, v, c
- q 个查询：u, v, c

**输出格式**：
- 每个查询输出答案

---

## 二、笔试面试考察点分析

### 2.1 核心考察点

| 考察点 | 说明 | 出现频率 |
|--------|------|----------|
| 边双连通分量 | 树上实际是点双，但可类比 | ★★★★★ |
| 缩点+路径查询 | LCA + 树上差分 | ★★★★★ |
| 路径统计 | 颜色统计 | ★★★★☆ |

### 2.2 面试高频提问

1. **如何在树上进行路径查询？**
   - LCA + 差分数组

2. **颜色统计如何实现？**
   - 预处理每个节点到根的累积

---

## 三、解题思路

### 3.1 算法流程

```
1. 建立树的邻接表
2. 预处理LCA（倍增/欧拉序+RMQ）
3. 预处理每个节点到根的路径上，每种颜色的边数
4. 查询时：ans = sum[u] + sum[v] - 2*sum[lca] + color(lca,parent)
```

### 3.2 核心原理

**树上路径查询模板**：
- 预处理：cnt[u][c] = 从根到u路径上颜色c的边数
- 查询：cnt[u][c] + cnt[v][c] - 2*cnt[lca][c]

---

## 四、代码实现（C++）

```cpp
#include <bits/stdc++.h>
using namespace std;

const int MAXN = 200005;
const int LOG = 20;  // log2(2e5) < 18

// 树的存储
struct Edge {
    int to;
    int color;
    int next;
} edges[MAXN * 2];

int head[MAXN];
int cnt_edges = 0;

// LCA预处理
int up[MAXN][LOG];
int depth[MAXN];

// 路径统计：cnt[u][c] = 从根到u路径上颜色c的边数
// 这里使用map压缩颜色
vector<int> colors;
int color_id[MAXN];  // 每条边的颜色编号
vector<int> cnt[MAXN];  // 动态数组

int n, q;

// 添加边
void add_edge(int u, int v, int c) {
    edges[cnt_edges].to = v;
    edges[cnt_edges].color = c;
    edges[cnt_edges].next = head[u];
    head[u] = cnt_edges++;
}

// DFS预处理
void dfs(int u, int parent) {
    for (int i = 1; i < LOG; i++) {
        up[u][i] = up[up[u][i-1]][i-1];
    }
    
    for (int e = head[u]; e != -1; e = edges[e].next) {
        int v = edges[e].to;
        if (v == parent) continue;
        
        depth[v] = depth[u] + 1;
        up[v][0] = u;
        
        // 复制并更新计数
        cnt[v] = cnt[u];
        int c = edges[e].color;
        cnt[v][c]++;
        
        dfs(v, u);
    }
}

// LCA查询
int lca(int u, int v) {
    if (depth[u] < depth[v]) swap(u, v);
    
    // 提升u到相同深度
    int diff = depth[u] - depth[v];
    for (int i = 0; i < LOG; i++) {
        if (diff & (1 << i)) {
            u = up[u][i];
        }
    }
    
    if (u == v) return u;
    
    for (int i = LOG - 1; i >= 0; i--) {
        if (up[u][i] != up[v][i]) {
            u = up[u][i];
            v = up[v][i];
        }
    }
    
    return up[u][0];
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    cin >> n >> q;
    
    memset(head, -1, sizeof(head));
    
    // 读边，建立树
    for (int i = 0; i < n - 1; i++) {
        int u, v, c;
        cin >> u >> v >> c;
        add_edge(u, v, c);
        colors.push_back(c);
    }
    
    // 颜色离散化
    sort(colors.begin(), colors.end());
    colors.erase(unique(colors.begin(), colors.end()), colors.end());
    
    for (int e = 0; e < cnt_edges; e++) {
        edges[e].color = lower_bound(colors.begin(), colors.end(), edges[e].color) - colors.begin();
    }
    
    // 初始化计数数组
    for (int i = 1; i <= n; i++) {
        cnt[i].assign(colors.size(), 0);
    }
    
    // 预处理
    depth[1] = 0;
    up[1][0] = 1;
    dfs(1, 0);
    
    // 处理查询
    for (int i = 0; i < q; i++) {
        int u, v, c;
        cin >> u >> v >> c;
        int color = lower_bound(colors.begin(), colors.end(), c) - colors.begin();
        
        int w = lca(u, v);
        int ans = cnt[u][color] + cnt[v][color] - 2 * cnt[w][color];
        
        cout << ans << endl;
    }
    
    return 0;
}
```

---

## 五、机器学习/深度学习关联

### 5.1 路径特征提取

**场景**：图神经网络的消息传递

**方法**：
- 路径查询 = 聚合邻居信息
- 预处理累积和加速查询
- 对应GNN的消息传递优化

---

## 六、同类题目

| 平台 | 题目 | 说明 |
|------|------|------|
| 洛谷 | P2783 | 边双+LCA距离 |
| 牛客 | NC23673 | 送信问题 |
| CF | 555E | 边双+LCA+方向 |

---

*本文档分析了AtCoder ABC207F题目，涵盖树上路径查询和LCA应用。*

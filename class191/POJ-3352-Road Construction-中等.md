# POJ-3352-Road Construction-中等

## 题目信息

- **题目链接**: http://poj.org/problem?id=3352
- **难度**: 中等
- **考察点**: 边双连通分量(e-DCC)、缩点、加边数量计算
- **原题名**: Road Construction

## 题目描述

### 背景

帮助Joe解决道路建设问题

### 题目描述

给定一个连通的无向图，求至少需要添加多少条边才能使整个图变成边双连通的（即不存在割边）。

### 输入格式

输入包含多个测试用例。每个测试用例：
- 第一行：n, r（节点数1≤n≤1000, 边数）
- 接下来r行：每行两个整数a, b，表示一条无向边

输入以EOF结束。

### 输出格式

对于每个测试用例，输出至少需要添加的边数。

### 样例输入

```
3 3
1 2
2 3
1 3
2 1
1 2
```

### 样例输出

```
0
1
```

## 笔试面试考察点分析

1. **边双连通分量缩点**：将e-DCC缩为点，构建缩点树
2. **树的叶子节点**：缩点后树的叶子节点数量
3. **加边公式**：答案为 (叶子数 + 1) / 2
4. **重边处理**：注意本题无重边，但需了解重边处理方法

## 解题思路

### 核心原理

1. **求e-DCC**：使用Tarjan算法求边双连通分量
2. **缩点**：将每个e-DCC缩为一个节点，割边作为新节点的边
3. **统计叶子**：缩点后是一棵树，统计度为1的节点（叶子）
4. **计算答案**：每添加一条边可以将两个叶子配对，所以答案为 (leaf + 1) / 2

### 具体步骤

```
1. Tarjan算法求e-DCC
2. 缩点，构建缩点树
3. 统计叶子节点数量 leaf
4. 输出 (leaf + 1) / 2
```

## 完整代码实现（C++）

```cpp
#include <bits/stdc++.h>
using namespace std;

// ==================== POJ3352 边双连通分量+缩点+加边 ====================
// 考察点：e-DCC缩点、叶子统计、加边数量计算
// 面试高频：缩点后树的性质证明
// 时间复杂度：O(n + m)
// 空间复杂度：O(n + m)

const int MAXN = 1005;     // 最大节点数
const int MAXM = 2005;     // 最大边数

// 边的结构体
struct Edge {
    int to;     // 边的终点
    int rev;    // 反向边索引
    int id;     // 边编号
    Edge(int _to, int _rev, int _id) : to(_to), rev(_rev), id(_id) {}
};

// 邻接表
vector<Edge> adj[MAXN];

// Tarjan核心数组
int dfn[MAXN], low[MAXN], timestamp = 0;
bool is_bridge[MAXM];
int bridge_cnt = 0;

// e-DCC相关
int e_dcc_id[MAXN];
int dcc_cnt = 0;
stack<int> st;

// degree：缩点后每个e-DCC的度数
int degree[MAXN];

/**
 * Tarjan算法 - 求割边和e-DCC
 * 
 * 核心逻辑：
 * 1. low[v] > dfn[u] => 边(u,v)是割边
 * 2. dfn[u] == low[u] => 从栈中弹出节点构成e-DCC
 */
void tarjan(int u, int edge_id) {
    dfn[u] = low[u] = ++timestamp;
    st.push(u);
    
    for (int i = 0; i < adj[u].size(); i++) {
        Edge &e = adj[u][i];
        int v = e.to;
        int id = e.id;
        
        if (!dfn[v]) {
            tarjan(v, id);
            low[u] = min(low[u], low[v]);
            
            // 割边判定
            if (low[v] > dfn[u]) {
                is_bridge[id] = true;
                bridge_cnt++;
            }
        } else if (id != edge_id) {
            low[u] = min(low[u], dfn[v]);
        }
    }
    
    // 发现e-DCC
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
 * 统计缩点后树的叶子节点数量
 * 
 * 核心原理：
 * 1. 每个e-DCC视为一个节点，割边连接不同e-DCC
 * 2. 度为1的节点即为叶子
 * 3. 答案为 (leaf + 1) / 2
 *    每添加一条边可以将两个叶子配对
 *    奇数叶子时最后剩下一个，需要再加一条边
 */
int countLeaves() {
    // 统计每个e-DCC的度数（连接其他e-DCC的割边数）
    for (int i = 1; i <= MAXM; i++) {
        // 注意：这里需要根据实际边数遍历
    }
    
    // 遍历所有边，统计度数
    // 需记录边两端点所在的e-DCC
    for (int u = 1; u < MAXN; u++) {
        for (auto &e : adj[u]) {
            int v = e.to;
            int id = e.id;
            // 如果是割边且两端属于不同e-DCC
            if (is_bridge[id] && e_dcc_id[u] != e_dcc_id[v]) {
                degree[e_dcc_id[u]]++;
                degree[e_dcc_id[v]]++;
            }
        }
    }
    
    // 统计叶子节点（度为1的e-DCC）
    int leaf = 0;
    for (int i = 1; i <= dcc_cnt; i++) {
        if (degree[i] == 1) {
            leaf++;
        }
    }
    
    return leaf;
}

// 记录边的端点（用于度数统计）
int a[MAXM], b[MAXM];
int edge_cnt = 0;

int main() {
    int n, r;
    // 读取输入直到EOF
    while (cin >> n >> r) {
        // 初始化
        memset(dfn, 0, sizeof(dfn));
        memset(low, 0, sizeof(low));
        memset(is_bridge, false, sizeof(is_bridge));
        memset(degree, 0, sizeof(degree));
        for (int i = 1; i <= n; i++) {
            adj[i].clear();
        }
        timestamp = bridge_cnt = dcc_cnt = 0;
        edge_cnt = 0;
        
        // 读取边
        for (int i = 1; i <= r; i++) {
            int u, v;
            cin >> u >> v;
            a[i] = u;
            b[i] = v;
            edge_cnt++;
            
            // 添加无向边
            adj[u].push_back(Edge(v, adj[v].size(), i));
            adj[v].push_back(Edge(u, adj[u].size() - 1, i));
        }
        
        // Tarjan求e-DCC
        for (int i = 1; i <= n; i++) {
            if (!dfn[i]) {
                tarjan(i, 0);
            }
        }
        
        // 统计叶子数
        int leaf = 0;
        for (int i = 1; i <= edge_cnt; i++) {
            int u = a[i], v = b[i];
            if (is_bridge[i] && e_dcc_id[u] != e_dcc_id[v]) {
                degree[e_dcc_id[u]]++;
                degree[e_dcc_id[v]]++;
            }
        }
        
        for (int i = 1; i <= dcc_cnt; i++) {
            if (degree[i] == 1) {
                leaf++;
            }
        }
        
        // 计算答案：每条边可将两个叶子配对
        cout << (leaf + 1) / 2 << endl;
    }
    
    return 0;
}
```

## 代码逐行注释要点

### 割边判定定理

```
if (low[v] > dfn[u]) {
    // low[v] > dfn[u] 说明：
    // 1. v及其子树中所有节点能追溯到的最早时间 > u的发现时间
    // 2. 意味着v无法通过任何回边回到u或u的祖先
    // 3. 因此删除边(u,v)后，v将与图的其余部分断开
    is_bridge[id] = true;
}
```

### e-DCC判定

```
if (dfn[u] == low[u]) {
    // dfn[u] == low[u] 说明：
    // 1. u是其所在e-DCC中最早被访问的节点
    // 2. 从栈中弹出节点直到u，这些节点构成一个完整的e-DCC
    // 3. 这些节点之间不存在割边
}
```

### 叶子数公式证明

```
设叶子数为leaf：
1. 每添加一条边(u, v)，可将u到v路径上的所有割边消除
2. 路径两端必须是叶子节点
3. 所以每条边可以消除2个叶子
4. 答案为 (leaf + 1) / 2
   - leaf为偶数：leaf/2条边
   - leaf为奇数：(leaf+1)/2条边
```

## 时间空间复杂度分析

- **时间复杂度**：O(n + m)
  - Tarjan：O(n + m)
  - 缩点+统计：O(m)

- **空间复杂度**：O(n + m)

## 同类题目

| 平台 | 题目 | 难度 |
|------|------|------|
| POJ | POJ3177 Redundant Paths | 中等 |
| HDU | HDU2460 Network | 困难 |
| 洛谷 | P2860 Redundant Paths | 中等 |

## 机器学习关联

### GNN优化

1. **图简化**：缩点后图更简单，GNN计算更快
2. **社区特征**：叶子数反映图的拓扑结构
3. **消息传递**：树结构的消息传递更高效

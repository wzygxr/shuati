### 题目链接
https://atcoder.jp/contests/abc193/tasks/abc193_f

### 题目描述
给定一个无向图，求删除每个节点后连通块的数量。

### 笔试/面试考察点分析
- 考察割点的应用
- 连通块数量的计算
- 时间复杂度分析（O(n+m)）
- 边界条件处理（如非连通图）

### 解题思路
1. 使用Tarjan算法遍历无向图
2. 对于每个节点，计算删除该节点后连通块的增加量
3. 最大连通块数量 = 原始连通块数 + 最大增加量

### 代码实现
```cpp
#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>
using namespace std;

const int MAXN = 1e5 + 5;
vector<int> adj[MAXN];
int dfn[MAXN], low[MAXN], timestamp = 0;
int cut_cnt[MAXN]; // 每个节点的子树数量
int original_components = 0;
int max_increase = 0;

void tarjan(int u, int fa) {
    dfn[u] = low[u] = ++timestamp;
    int child = 0;
    for (int v : adj[u]) {
        if (v == fa) continue;
        if (!dfn[v]) {
            child++;
            tarjan(v, u);
            low[u] = min(low[u], low[v]);
            if (low[v] >= dfn[u]) {
                cut_cnt[u]++;
            }
        } else {
            low[u] = min(low[u], dfn[v]);
        }
    }
    if (fa == -1) {
        cut_cnt[u] = child - 1;
    }
    max_increase = max(max_increase, cut_cnt[u]);
}

int main() {
    int n, m;
    cin >> n >> m;
    for (int i = 0; i < m; ++i) {
        int u, v;
        cin >> u >> v;
        adj[u].push_back(v);
        adj[v].push_back(u);
    }
    
    for (int i = 1; i <= n; ++i) {
        if (!dfn[i]) {
            original_components++;
            tarjan(i, -1);
        }
    }
    
    cout << original_components + max_increase << endl;
    
    return 0;
}
```

### 代码逐行注释
```cpp
#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>
using namespace std;

const int MAXN = 1e5 + 5;
vector<int> adj[MAXN]; // 邻接表存储无向图
int dfn[MAXN], low[MAXN], timestamp = 0; // 时间戳数组和计数器
int cut_cnt[MAXN]; // 每个节点的子树数量
int original_components = 0; // 原始连通块数
int max_increase = 0; // 最大连通块增加量

// Tarjan算法求割点和连通块增加量
void tarjan(int u, int fa) {
    dfn[u] = low[u] = ++timestamp; // 初始化时间戳
    int child = 0; // 统计子节点数量
    for (int v : adj[u]) { // 遍历所有邻接节点
        if (v == fa) continue; // 跳过父节点
        if (!dfn[v]) { // 邻接节点未被访问
            child++;
            tarjan(v, u); // 递归遍历子节点
            low[u] = min(low[u], low[v]); // 更新low值
            if (low[v] >= dfn[u]) { // 统计子树数量
                cut_cnt[u]++;
            }
        } else { // 邻接节点已被访问，更新low值
            low[u] = min(low[u], dfn[v]);
        }
    }
    if (fa == -1) { // 根节点特殊处理
        cut_cnt[u] = child - 1;
    }
    max_increase = max(max_increase, cut_cnt[u]); // 更新最大连通块增加量
}

int main() {
    int n, m;
    cin >> n >> m;
    for (int i = 0; i < m; ++i) {
        int u, v;
        cin >> u >> v;
        adj[u].push_back(v);
        adj[v].push_back(u);
    }
    
    // 遍历所有连通块
    for (int i = 1; i <= n; ++i) {
        if (!dfn[i]) {
            original_components++;
            tarjan(i, -1); // 根节点fa=-1
        }
    }
    
    // 输出最大连通块数量
    cout << original_components + max_increase << endl;
    
    return 0;
}
```

### 时间/空间复杂度分析
- 时间复杂度：O(n+m)，n为节点数，m为边数
- 空间复杂度：O(n+m)，存储邻接表和时间戳数组

### 同类题目拓展
- POJ 2117 电力（割点应用）
- 洛谷 3225 矿场搭建（点双连通分量）
- AcWing 396 矿场搭建（点双连通分量）

### ML/DL关联思考
- 割点分析可用于图结构数据的关键节点识别
- 连通块数量的计算可用于图的复杂度评估
- 在GNN中，连通块信息可作为图的结构特征，提升模型性能
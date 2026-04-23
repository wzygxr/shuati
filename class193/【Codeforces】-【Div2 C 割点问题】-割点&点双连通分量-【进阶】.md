### 题目链接
https://codeforces.com/contest/1000/problem/C

### 题目描述
给定一个无向图，求每个节点是否为割点，并输出割点数量。

### 笔试/面试考察点分析
- 考察割点的概念和Tarjan算法的应用
- 时间复杂度分析（O(n+m)）
- 空间复杂度分析（O(n+m)）
- 结果的输出格式（按节点编号顺序输出）

### 解题思路
1. 使用Tarjan算法遍历无向图
2. 对于每个节点，判断是否为割点：
   - 根节点有≥2个子树
   - 非根节点存在子节点v，low[v] ≥ dfn[u]
3. 统计割点总数并输出

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
bool is_cut[MAXN];
int cut_cnt = 0;

void tarjan(int u, int fa) {
    dfn[u] = low[u] = ++timestamp;
    int child = 0;
    for (int v : adj[u]) {
        if (v == fa) continue;
        if (!dfn[v]) {
            child++;
            tarjan(v, u);
            low[u] = min(low[u], low[v]);
            if (fa == -1 && child >= 2) {
                is_cut[u] = true;
            }
            if (fa != -1 && low[v] >= dfn[u]) {
                is_cut[u] = true;
            }
        } else {
            low[u] = min(low[u], dfn[v]);
        }
    }
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
            tarjan(i, -1);
        }
    }
    
    for (int i = 1; i <= n; ++i) {
        if (is_cut[i]) {
            cut_cnt++;
        }
    }
    
    cout << cut_cnt << endl;
    for (int i = 1; i <= n; ++i) {
        if (is_cut[i]) {
            cout << i << " ";
        }
    }
    cout << endl;
    
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

const int MAXN = 1e5 + 5; // 节点最大数量
vector<int> adj[MAXN]; // 邻接表存储无向图
int dfn[MAXN], low[MAXN], timestamp = 0; // 时间戳数组和计数器
bool is_cut[MAXN]; // 标记节点是否为割点
int cut_cnt = 0; // 割点总数统计

// Tarjan算法求割点
// u: 当前节点，fa: 父节点
void tarjan(int u, int fa) {
    dfn[u] = low[u] = ++timestamp; // 初始化时间戳
    int child = 0; // 统计子节点数量
    for (int v : adj[u]) { // 遍历所有邻接节点
        if (v == fa) continue; // 跳过父节点
        if (!dfn[v]) { // 邻接节点未被访问
            child++;
            tarjan(v, u); // 递归遍历子节点
            low[u] = min(low[u], low[v]); // 更新low值
            
            // 根节点割点判定：有≥2个子树
            if (fa == -1 && child >= 2) {
                is_cut[u] = true;
            }
            // 非根节点割点判定：存在子节点v，low[v] ≥ dfn[u]
            if (fa != -1 && low[v] >= dfn[u]) {
                is_cut[u] = true;
            }
        } else { // 邻接节点已被访问，更新low值
            low[u] = min(low[u], dfn[v]);
        }
    }
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
            tarjan(i, -1); // 根节点fa=-1
        }
    }
    
    // 统计割点总数
    for (int i = 1; i <= n; ++i) {
        if (is_cut[i]) {
            cut_cnt++;
        }
    }
    
    // 输出结果
    cout << cut_cnt << endl;
    for (int i = 1; i <= n; ++i) {
        if (is_cut[i]) {
            cout << i << " ";
        }
    }
    cout << endl;
    
    return 0;
}
```

### 时间/空间复杂度分析
- 时间复杂度：O(n+m)，n为节点数，m为边数
- 空间复杂度：O(n+m)，存储邻接表和时间戳数组

### 同类题目拓展
- POJ 1144 Network（割点基础题）
- POJ 3177 冗余路径（边双连通分量）
- POJ 2117 电力（割点应用）
- 洛谷 3225 矿场搭建（点双连通分量）

### ML/DL关联思考
- 割点识别可用于图结构数据的关键节点提取
- 点双连通分量可作为图社区划分的基础
- 在GNN中，割点和点双分量可作为图的结构特征，提升模型性能
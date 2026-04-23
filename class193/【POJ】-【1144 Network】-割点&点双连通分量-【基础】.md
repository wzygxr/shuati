### 题目链接
http://poj.org/problem?id=1144

### 题目描述
给定一个无向图，求割点数量。

### 笔试/面试考察点分析
- 考察割点判定的基本概念和Tarjan算法的应用
- 输入格式的特殊处理（节点编号从1开始）
- 时间复杂度分析（O(n+m)）
- 边界条件处理（如孤立节点、树结构）

### 解题思路
1. 使用Tarjan算法遍历无向图
2. 对于每个节点，判断是否为割点：
   - 根节点有≥2个子树
   - 非根节点存在子节点v，low[v] ≥ dfn[u]
3. 统计割点总数

### 代码实现
```cpp
#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>
using namespace std;

const int MAXN = 105;
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
    int n;
    while (cin >> n && n != 0) {
        memset(dfn, 0, sizeof(dfn));
        memset(low, 0, sizeof(low));
        memset(is_cut, false, sizeof(is_cut));
        cut_cnt = 0;
        timestamp = 0;
        for (int i = 1; i <= n; i++) adj[i].clear();
        
        int u, v;
        while (cin >> u && u != 0) {
            while (cin.peek() != '\n' && cin >> v) {
                adj[u].push_back(v);
                adj[v].push_back(u);
            }
        }
        
        for (int i = 1; i <= n; i++) {
            if (!dfn[i]) {
                tarjan(i, -1);
            }
        }
        
        for (int i = 1; i <= n; i++) {
            if (is_cut[i]) cut_cnt++;
        }
        
        cout << cut_cnt << endl;
    }
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

const int MAXN = 105; // 节点最大数量，适配题目N<100的要求
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
    int n;
    while (cin >> n && n != 0) { // 多组输入，直到n=0
        // 初始化数组
        memset(dfn, 0, sizeof(dfn));
        memset(low, 0, sizeof(low));
        memset(is_cut, false, sizeof(is_cut));
        cut_cnt = 0;
        timestamp = 0;
        for (int i = 1; i <= n; i++) adj[i].clear();
        
        int u, v;
        while (cin >> u && u != 0) { // 输入边，直到u=0
            while (cin.peek() != '\n' && cin >> v) { // 处理一行内的多个邻接节点
                adj[u].push_back(v);
                adj[v].push_back(u);
            }
        }
        
        // 遍历所有连通块
        for (int i = 1; i <= n; i++) {
            if (!dfn[i]) {
                tarjan(i, -1); // 根节点fa=-1
            }
        }
        
        // 统计割点总数
        for (int i = 1; i <= n; i++) {
            if (is_cut[i]) cut_cnt++;
        }
        
        cout << cut_cnt << endl;
    }
    return 0;
}
```

### 时间/空间复杂度分析
- 时间复杂度：O(n+m)，n为节点数，m为边数
- 空间复杂度：O(n)，存储邻接表和时间戳数组

### 同类题目拓展
- POJ 3177 冗余路径（边双连通分量）
- POJ 2117 电力（割点应用）
- 洛谷 3225 矿场搭建（点双连通分量）

### ML/DL关联思考
- 割点识别可用于图结构数据的关键节点提取
- 点双连通分量可作为图社区划分的基础
- 在GNN中，割点和点双分量可作为图的结构特征，提升模型性能
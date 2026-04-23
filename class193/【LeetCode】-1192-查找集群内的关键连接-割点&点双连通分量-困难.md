### 题目链接
[LeetCode 1192 查找集群内的关键连接](https://leetcode.cn/problems/critical-connections-in-a-network/)

### 题目描述
给定一个无向图，求所有割边。

### 笔试/面试考察点分析
- 考察割边判定的核心逻辑
- 考察无向图连通性分析
- 常见坑点：重边/自环处理、根节点特殊处理

### 解题思路
1. 使用Tarjan算法求割边
2. 统计割边数量

### 完整代码实现
```cpp
#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>
using namespace std;

const int MAXN = 100005; // 节点最大数量
vector<int> adj[MAXN]; // 邻接表
int dfn[MAXN], low[MAXN], timestamp; // 时间戳数组
bool is_bridge[MAXN * 2]; // 标记是否为割边
int bridge_cnt; // 割边总数

// Tarjan算法求割边
void tarjan(int u, int fa) {
    dfn[u] = low[u] = ++timestamp; // 初始化时间戳
    for (int i = 0; i < adj[u].size(); i++) { // 遍历邻接节点
        int v = adj[u][i];
        if (v == fa) continue; // 跳过父节点
        if (!dfn[v]) { // 未访问过
            tarjan(v, u); // 递归遍历
            low[u] = min(low[u], low[v]); // 更新low值
            // 割边判定
            if (low[v] > dfn[u]) {
                is_bridge[i] = true;
                is_bridge[(i ^ 1)] = true; // 无向图双向标记
                bridge_cnt++;
            }
        } else { // 已访问过，更新low值
            low[u] = min(low[u], dfn[v]);
        }
    }
}

int main() {
    int n, m;
    while (cin >> n >> m && n != 0) {
        // 初始化
        memset(dfn, 0, sizeof(dfn));
        memset(low, 0, sizeof(low));
        memset(is_bridge, false, sizeof(is_bridge));
        for (int i = 0; i < n; i++) adj[i].clear();
        timestamp = 0;
        bridge_cnt = 0;
        
        // 读取边
        for (int i = 0; i < m; i++) {
            int u, v;
            cin >> u >> v;
            adj[u].push_back(v);
            adj[v].push_back(u);
        }
        
        // 处理每个连通块
        for (int i = 0; i < n; i++) {
            if (!dfn[i]) {
                tarjan(i, -1);
            }
        }
        
        // 输出割边
        cout << bridge_cnt << endl;
        for (int u = 0; u < n; u++) {
            for (int i = 0; i < adj[u].size(); i++) {
                int v = adj[u][i];
                if (is_bridge[i] && u < v) {
                    cout << u << " " << v << endl;
                }
            }
        }
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

const int MAXN = 100005; // 节点最大数量，适配题目数据范围
vector<int> adj[MAXN]; // 邻接表存储无向图
int dfn[MAXN], low[MAXN], timestamp; // dfn[u]节点u的发现时间，low[u]节点u可回溯的最早时间
bool is_bridge[MAXN * 2]; // 标记是否为割边
int bridge_cnt; // 割边总数统计

// Tarjan算法求割边：核心逻辑
// u：当前节点，fa：父节点
void tarjan(int u, int fa) {
    dfn[u] = low[u] = ++timestamp; // 初始化时间戳：发现时间=可回溯最早时间
    for (int i = 0; i < adj[u].size(); i++) { // 遍历当前节点的所有邻接节点
        int v = adj[u][i]; // 邻接节点
        if (v == fa) continue; // 跳过父节点，避免循环访问
        if (!dfn[v]) { // 邻接节点未被访问过
            tarjan(v, u); // 递归遍历子节点
            low[u] = min(low[u], low[v]); // 回溯更新low[u]：取子节点可回溯的最早时间
            // 割边判定逻辑
            if (low[v] > dfn[u]) { // 当low[v] > dfn[u]时，u-v边为割边
                is_bridge[i] = true; // 标记当前边为割边
                is_bridge[(i ^ 1)] = true; // 无向图双向标记（i^1为反向边）
                bridge_cnt++; // 割边总数+1
            }
        } else { // 邻接节点已被访问过，更新low[u]为较小的时间戳
            low[u] = min(low[u], dfn[v]);
        }
    }
}

int main() {
    int n, m;
    while (cin >> n >> m && n != 0) { // 多组数据输入
        // 初始化核心数组，避免脏数据
        memset(dfn, 0, sizeof(dfn));
        memset(low, 0, sizeof(low));
        memset(is_bridge, false, sizeof(is_bridge));
        for (int i = 0; i < n; i++) adj[i].clear(); // 清空邻接表
        timestamp = 0; // 时间戳重置
        bridge_cnt = 0; // 割边总数重置
        
        // 读取边数据
        for (int i = 0; i < m; i++) {
            int u, v;
            cin >> u >> v;
            adj[u].push_back(v);
            adj[v].push_back(u); // 无向图双向存储
        }
        
        // 处理每个连通块（非连通图场景）
        for (int i = 0; i < n; i++) {
            if (!dfn[i]) { // 未访问过的节点作为连通块根节点
                tarjan(i, -1); // 根节点父节点设为-1
            }
        }
        
        // 输出割边
        cout << bridge_cnt << endl;
        for (int u = 0; u < n; u++) {
            for (int i = 0; i < adj[u].size(); i++) {
                int v = adj[u][i]; // 邻接节点
                if (is_bridge[i] && u < v) { // 避免重复输出
                    cout << u << " " << v << endl;
                }
            }
        }
    }
    return 0;
}
```

### 时间/空间复杂度分析
- 时间复杂度：O(V + E)，其中V为节点数，E为边数
- 空间复杂度：O(V + E)，用于存储邻接表和边双连通分量

### 同类题目拓展
- [POJ 1523 SPF](http://poj.org/problem?id=1523)（割点判定）
- [HDU 3844 Mining Your Own Business](http://acm.hdu.edu.cn/showproblem.php?pid=3844)（点双连通分量应用）

### ML/DL关联思考
- 边双连通分量可作为图社区划分的基础
- 割边判定可用于图结构数据的关键边提取
- 在GNN模型中，边双连通分量可作为局部聚合单元，提升模型对图拓扑的理解
- 割边的关键边特征可用于图异常检测任务，提升模型性能
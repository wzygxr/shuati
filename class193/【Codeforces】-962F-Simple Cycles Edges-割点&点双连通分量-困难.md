### 题目链接
[Codeforces 962F Simple Cycles Edges](https://codeforces.com/contest/962/problem/F)

### 题目描述
给定一个无向图，求所有在至少一个简单环上的边。

### 笔试/面试考察点分析
- 考察点双连通分量的应用
- 考察图的环分析
- 常见坑点：重边/自环处理、根节点特殊处理

### 解题思路
1. 使用Tarjan算法求点双连通分量
2. 对每个点双连通分量进行环分析
3. 判断每条边是否在至少一个简单环上
4. 统计符合条件的边数量

### 完整代码实现
```cpp
#include <iostream>
#include <vector>
#include <stack>
#include <algorithm>
#include <cstring>
using namespace std;

const int MAXN = 100005; // 节点最大数量
const int MAXM = 200005; // 边最大数量

struct Edge {
    int to, next, id;
} edge[MAXM];

int head[MAXN], tot;
int dfn[MAXN], low[MAXN], timestamp;
int dcc_cnt; // 点双连通分量数量
vector<int> dcc[MAXN]; // 存储每个点双连通分量的节点
vector<int> dcc_edges[MAXN]; // 存储每个点双连通分量的边
stack<pair<int, int>> st; // 存储边的栈
bool in_cycle[MAXM]; // 标记边是否在环上

// 初始化
void init() {
    tot = 0;
    memset(head, -1, sizeof(head));
    memset(dfn, 0, sizeof(dfn));
    memset(low, 0, sizeof(low));
    timestamp = 0;
    dcc_cnt = 0;
    memset(in_cycle, false, sizeof(in_cycle));
    while (!st.empty()) st.pop();
    for (int i = 0; i < MAXN; i++) {
        dcc[i].clear();
        dcc_edges[i].clear();
    }
}

// 添加边
void add_edge(int u, int v, int id) {
    edge[tot].to = v;
    edge[tot].next = head[u];
    edge[tot].id = id;
    head[u] = tot++;
}

// Tarjan算法求点双连通分量
void tarjan(int u, int fa) {
    dfn[u] = low[u] = ++timestamp;
    for (int i = head[u]; i != -1; i = edge[i].next) {
        int v = edge[i].to;
        int id = edge[i].id;
        if (v == fa) continue;
        if (!dfn[v]) {
            st.push({u, v});
            tarjan(v, u);
            low[u] = min(low[u], low[v]);
            // 点双连通分量提取
            if (low[v] >= dfn[u]) {
                dcc_cnt++;
                while (true) {
                    pair<int, int> e = st.top();
                    st.pop();
                    dcc[dcc_cnt].push_back(e.first);
                    dcc[dcc_cnt].push_back(e.second);
                    dcc_edges[dcc_cnt].push_back(id);
                    if (e.first == u && e.second == v) break;
                }
            }
        } else if (dfn[v] < dfn[u]) {
            st.push({u, v});
            low[u] = min(low[u], dfn[v]);
        }
    }
}

int main() {
    int n, m;
    while (cin >> n >> m && n != 0) {
        init();
        for (int i = 0; i < m; i++) {
            int u, v;
            cin >> u >> v;
            add_edge(u, v, i);
            add_edge(v, u, i);
        }
        // 处理每个连通块
        for (int i = 1; i <= n; i++) {
            if (!dfn[i]) {
                tarjan(i, -1);
            }
        }
        // 对每个点双连通分量进行环分析
        for (int i = 1; i <= dcc_cnt; i++) {
            int node_cnt = unique(dcc[i].begin(), dcc[i].end()) - dcc[i].begin();
            int edge_cnt = dcc_edges[i].size();
            if (edge_cnt >= node_cnt) { // 边数≥节点数，存在环
                for (int id : dcc_edges[i]) {
                    in_cycle[id] = true;
                }
            }
        }
        // 统计符合条件的边数量
        int ans = 0;
        for (int i = 0; i < m; i++) {
            if (in_cycle[i]) ans++;
        }
        cout << ans << endl;
        for (int i = 0; i < m; i++) {
            if (in_cycle[i]) cout << i + 1 << " ";
        }
        cout << endl;
    }
    return 0;
}
```

### 代码逐行注释
```cpp
#include <iostream>
#include <vector>
#include <stack>
#include <algorithm>
#include <cstring>
using namespace std;

const int MAXN = 100005; // 节点最大数量，适配题目数据范围
const int MAXM = 200005; // 边最大数量，无向图双向存储

struct Edge {
    int to, next, id;
} edge[MAXM]; // 边结构体

int head[MAXN], tot; // 邻接表头指针、边计数
int dfn[MAXN], low[MAXN], timestamp; // 时间戳数组
int dcc_cnt; // 点双连通分量数量
vector<int> dcc[MAXN]; // 存储每个点双连通分量的节点
vector<int> dcc_edges[MAXN]; // 存储每个点双连通分量的边
stack<pair<int, int>> st; // 存储边的栈，用于点双连通分量提取
bool in_cycle[MAXM]; // 标记边是否在环上

// 初始化函数
void init() {
    tot = 0;
    memset(head, -1, sizeof(head)); // 邻接表头初始化
    memset(dfn, 0, sizeof(dfn)); // 时间戳数组初始化
    memset(low, 0, sizeof(low)); // 可回溯最早时间数组初始化
    timestamp = 0; // 时间戳计数器重置
    dcc_cnt = 0; // 点双连通分量数量重置
    memset(in_cycle, false, sizeof(in_cycle)); // 环标记数组初始化
    while (!st.empty()) st.pop(); // 清空边栈
    for (int i = 0; i < MAXN; i++) {
        dcc[i].clear(); // 清空点双连通分量存储
        dcc_edges[i].clear(); // 清空点双连通分量边存储
    }
}

// 添加边函数
void add_edge(int u, int v, int id) {
    edge[tot].to = v; // 边的目标节点
    edge[tot].next = head[u]; // 边的下一条边指针
    edge[tot].id = id; // 边的编号
    head[u] = tot++; // 更新邻接表头指针
}

// Tarjan算法求点双连通分量
// u：当前节点，fa：父节点
void tarjan(int u, int fa) {
    dfn[u] = low[u] = ++timestamp; // 初始化时间戳
    for (int i = head[u]; i != -1; i = edge[i].next) { // 遍历邻接边
        int v = edge[i].to; // 邻接节点
        int id = edge[i].id; // 边的编号
        if (v == fa) continue; // 跳过父节点
        if (!dfn[v]) { // 邻接节点未被访问过
            st.push({u, v}); // 将边入栈
            tarjan(v, u); // 递归遍历子节点
            low[u] = min(low[u], low[v]); // 回溯更新low[u]
            // 点双连通分量提取
            if (low[v] >= dfn[u]) { // 当low[v] >= dfn[u]时，弹出边栈至当前边
                dcc_cnt++; // 点双连通分量数量+1
                while (true) {
                    pair<int, int> e = st.top(); // 取栈顶边
                    st.pop(); // 弹出栈顶边
                    dcc[dcc_cnt].push_back(e.first); // 将边的起点加入点双连通分量
                    dcc[dcc_cnt].push_back(e.second); // 将边的终点加入点双连通分量
                    dcc_edges[dcc_cnt].push_back(id); // 将边的编号加入点双连通分量边列表
                    if (e.first == u && e.second == v) break; // 弹出到当前边为止
                }
            }
        } else if (dfn[v] < dfn[u]) { // 邻接节点已被访问过，且是祖先节点
            st.push({u, v}); // 将边入栈
            low[u] = min(low[u], dfn[v]); // 更新low[u]
        }
    }
}

int main() {
    int n, m;
    while (cin >> n >> m && n != 0) { // 多组数据输入
        init(); // 初始化
        // 读取边数据
        for (int i = 0; i < m; i++) {
            int u, v;
            cin >> u >> v;
            add_edge(u, v, i); // 添加正向边
            add_edge(v, u, i); // 添加反向边（无向图）
        }
        // 处理每个连通块
        for (int i = 1; i <= n; i++) {
            if (!dfn[i]) { // 未访问过的节点作为连通块根节点
                tarjan(i, -1); // 根节点父节点设为-1
            }
        }
        // 对每个点双连通分量进行环分析
        for (int i = 1; i <= dcc_cnt; i++) {
            int node_cnt = unique(dcc[i].begin(), dcc[i].end()) - dcc[i].begin(); // 去重后节点数
            int edge_cnt = dcc_edges[i].size(); // 边数
            if (edge_cnt >= node_cnt) { // 边数≥节点数，存在环
                for (int id : dcc_edges[i]) {
                    in_cycle[id] = true; // 标记边在环上
                }
            }
        }
        // 统计符合条件的边数量
        int ans = 0;
        for (int i = 0; i < m; i++) {
            if (in_cycle[i]) ans++;
        }
        cout << ans << endl;
        for (int i = 0; i < m; i++) {
            if (in_cycle[i]) cout << i + 1 << " ";
        }
        cout << endl;
    }
    return 0;
}
```

### 时间/空间复杂度分析
- 时间复杂度：O(V + E)，其中V为节点数，E为边数
- 空间复杂度：O(V + E)，用于存储邻接表和点双连通分量

### 同类题目拓展
- [POJ 2942 Knights of the Round Table](http://poj.org/problem?id=2942)（点双连通分量+二分图染色）
- [Codeforces 97E Leaders](https://codeforces.com/contest/97/problem/E)（点双连通分量+奇环检测）

### ML/DL关联思考
- 点双连通分量可作为图社区划分的基础
- 环分析可用于图的结构分析
- 在GNN模型中，点双连通分量可作为局部聚合单元，提升模型对图拓扑的理解
- 环检测可用于图异常检测任务，提升模型性能
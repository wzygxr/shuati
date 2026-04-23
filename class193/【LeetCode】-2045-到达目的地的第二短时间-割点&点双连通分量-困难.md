### 题目链接
[LeetCode 2045 到达目的地的第二短时间](https://leetcode.cn/problems/second-minimum-time-to-reach-destination/)

### 题目描述
给定一个无向图，求从起点到终点的第二短路径。

### 笔试/面试考察点分析
- 考察点双连通分量的应用
- 考察图的最短路径分析
- 常见坑点：重边/自环处理、根节点特殊处理

### 解题思路
1. 使用Tarjan算法求点双连通分量
2. 对每个点双连通分量进行环分析
3. 利用环的特性计算第二短路径

### 完整代码实现
```cpp
#include <iostream>
#include <vector>
#include <stack>
#include <algorithm>
#include <cstring>
#include <queue>
using namespace std;

const int MAXN = 10005; // 节点最大数量
const int MAXM = 20005; // 边最大数量
const long long INF = 1e18; // 无穷大

struct Edge {
    int to, next;
    long long time;
} edge[MAXM];

int head[MAXN], tot;
int dfn[MAXN], low[MAXN], timestamp;
int dcc_cnt; // 点双连通分量数量
vector<int> dcc[MAXN]; // 存储每个点双连通分量的节点
stack<pair<int, int>> st; // 存储边的栈
long long dist1[MAXN], dist2[MAXN]; // 第一短和第二短路径

// 初始化
void init() {
    tot = 0;
    memset(head, -1, sizeof(head));
    memset(dfn, 0, sizeof(dfn));
    memset(low, 0, sizeof(low));
    timestamp = 0;
    dcc_cnt = 0;
    while (!st.empty()) st.pop();
    for (int i = 0; i < MAXN; i++) dcc[i].clear();
    fill(dist1, dist1 + MAXN, INF);
    fill(dist2, dist2 + MAXN, INF);
}

// 添加边
void add_edge(int u, int v, long long time) {
    edge[tot].to = v;
    edge[tot].time = time;
    edge[tot].next = head[u];
    head[u] = tot++;
}

// Tarjan算法求点双连通分量
void tarjan(int u, int fa) {
    dfn[u] = low[u] = ++timestamp; // 初始化时间戳
    for (int i = head[u]; i != -1; i = edge[i].next) { // 遍历邻接边
        int v = edge[i].to; // 邻接节点
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
                    if (e.first == u && e.second == v) break; // 弹出到当前边为止
                }
            }
        } else if (dfn[v] < dfn[u]) { // 邻接节点已被访问过，且是祖先节点
            st.push({u, v}); // 将边入栈
            low[u] = min(low[u], dfn[v]); // 更新low[u]
        }
    }
}

// 求第一短和第二短路径
void dijkstra(int start) {
    priority_queue<pair<long long, int>, vector<pair<long long, int>>, greater<pair<long long, int>>> pq;
    dist1[start] = 0;
    pq.push({0, start});
    while (!pq.empty()) {
        auto [d, u] = pq.top();
        pq.pop();
        if (d > dist2[u]) continue;
        for (int i = head[u]; i != -1; i = edge[i].next) {
            int v = edge[i].to;
            long long time = edge[i].time;
            if (dist1[v] > d + time) {
                dist2[v] = dist1[v];
                dist1[v] = d + time;
                pq.push({dist1[v], v});
            } else if (dist1[v] < d + time && dist2[v] > d + time) {
                dist2[v] = d + time;
                pq.push({dist2[v], v});
            }
        }
    }
}

int main() {
    int n, m;
    while (cin >> n >> m && n != 0) {
        init();
        for (int i = 0; i < m; i++) {
            int u, v;
            long long time;
            cin >> u >> v >> time;
            add_edge(u, v, time);
            add_edge(v, u, time);
        }
        // 处理每个连通块
        for (int i = 1; i <= n; i++) {
            if (!dfn[i]) {
                tarjan(i, -1);
            }
        }
        // 求第一短和第二短路径
        dijkstra(1);
        cout << dist2[n] << endl;
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
#include <queue>
using namespace std;

const int MAXN = 10005; // 节点最大数量，适配题目数据范围
const int MAXM = 20005; // 边最大数量，无向图双向存储
const long long INF = 1e18; // 无穷大，避免溢出

struct Edge {
    int to, next;
    long long time;
} edge[MAXM]; // 边结构体

int head[MAXN], tot; // 邻接表头指针、边计数
int dfn[MAXN], low[MAXN], timestamp; // 时间戳数组
int dcc_cnt; // 点双连通分量数量
vector<int> dcc[MAXN]; // 存储每个点双连通分量的节点
stack<pair<int, int>> st; // 存储边的栈，用于点双连通分量提取
long long dist1[MAXN], dist2[MAXN]; // 第一短和第二短路径

// 初始化函数
void init() {
    tot = 0;
    memset(head, -1, sizeof(head)); // 邻接表头初始化
    memset(dfn, 0, sizeof(dfn)); // 时间戳数组初始化
    memset(low, 0, sizeof(low)); // 可回溯最早时间数组初始化
    timestamp = 0; // 时间戳计数器重置
    dcc_cnt = 0; // 点双连通分量数量重置
    while (!st.empty()) st.pop(); // 清空边栈
    for (int i = 0; i < MAXN; i++) dcc[i].clear(); // 清空点双连通分量存储
    fill(dist1, dist1 + MAXN, INF); // 第一短路径初始化
    fill(dist2, dist2 + MAXN, INF); // 第二短路径初始化
}

// 添加边函数
void add_edge(int u, int v, long long time) {
    edge[tot].to = v; // 边的目标节点
    edge[tot].time = time; // 边的时间
    edge[tot].next = head[u]; // 边的下一条边指针
    head[u] = tot++; // 更新邻接表头指针
}

// Tarjan算法求点双连通分量
// u：当前节点，fa：父节点
void tarjan(int u, int fa) {
    dfn[u] = low[u] = ++timestamp; // 初始化时间戳
    for (int i = head[u]; i != -1; i = edge[i].next) { // 遍历邻接边
        int v = edge[i].to; // 邻接节点
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
                    if (e.first == u && e.second == v) break; // 弹出到当前边为止
                }
            }
        } else if (dfn[v] < dfn[u]) { // 邻接节点已被访问过，且是祖先节点
            st.push({u, v}); // 将边入栈
            low[u] = min(low[u], dfn[v]); // 更新low[u]
        }
    }
}

// 求第一短和第二短路径（Dijkstra算法）
void dijkstra(int start) {
    priority_queue<pair<long long, int>, vector<pair<long long, int>>, greater<pair<long long, int>>> pq;
    dist1[start] = 0;
    pq.push({0, start});
    while (!pq.empty()) {
        auto [d, u] = pq.top();
        pq.pop();
        if (d > dist2[u]) continue; // 剪枝
        for (int i = head[u]; i != -1; i = edge[i].next) {
            int v = edge[i].to; // 邻接节点
            long long time = edge[i].time; // 边的时间
            if (dist1[v] > d + time) { // 更新第一短路径
                dist2[v] = dist1[v];
                dist1[v] = d + time;
                pq.push({dist1[v], v});
            } else if (dist1[v] < d + time && dist2[v] > d + time) { // 更新第二短路径
                dist2[v] = d + time;
                pq.push({dist2[v], v});
            }
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
            long long time;
            cin >> u >> v >> time;
            add_edge(u, v, time); // 添加正向边
            add_edge(v, u, time); // 添加反向边（无向图）
        }
        // 处理每个连通块
        for (int i = 1; i <= n; i++) {
            if (!dfn[i]) { // 未访问过的节点作为连通块根节点
                tarjan(i, -1); // 根节点父节点设为-1
            }
        }
        // 求第一短和第二短路径
        dijkstra(1);
        cout << dist2[n] << endl; // 输出第二短路径
    }
    return 0;
}
```

### 时间/空间复杂度分析
- 时间复杂度：O((V + E) log V)，其中V为节点数，E为边数
- 空间复杂度：O(V + E)，用于存储邻接表和点双连通分量

### 同类题目拓展
- [POJ 1523 SPF](http://poj.org/problem?id=1523)（割点判定）
- [HDU 3844 Mining Your Own Business](http://acm.hdu.edu.cn/showproblem.php?pid=3844)（点双连通分量应用）

### ML/DL关联思考
- 点双连通分量可作为图社区划分的基础
- 最短路径分析可用于图结构数据的关键路径提取
- 在GNN模型中，点双连通分量可作为局部聚合单元，提升模型对图拓扑的理解
- 最短路径的关键路径特征可用于图异常检测任务，提升模型性能
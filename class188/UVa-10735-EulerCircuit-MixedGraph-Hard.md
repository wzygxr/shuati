# 【UVa】-10735-混合图欧拉回路-欧拉路径-困难

## 题目原始链接
https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1676

## 题目完整描述
给出一个混合图（既有有向边又有无向边），判断是否存在欧拉回路。如果存在，输出一个可行解。

混合图欧拉回路的定义是：从某个顶点出发，经过图中每条边恰好一次，最后回到起点的路径。

### 输入输出格式
- 输入：第一行包含测试用例数量T，每个测试用例包含顶点数n、边数m，接下来m行描述边的信息
- 输出：对于每个测试用例，如果存在欧拉回路则输出路径，否则输出 "No euler circuit exist"

### 数据范围
- 1 <= n <= 200
- 1 <= m <= 1000

### 样例输入输出
```
输入：
1
3 4
1 2 0  // 无向边
2 3 0  // 无向边
3 1 0  // 无向边
1 3 1  // 有向边 1->3

输出：
1 2 3 1 3 1
```

## 笔试/面试考察点分析
- **考察点1**：混合图欧拉回路判定算法（结合网络流分配无向边方向）
- **考察点2**：最大流算法应用（用于确定无向边的定向）
- **考察点3**：Hierholzer算法在混合图中的应用
- **考察点4**：图论综合应用（欧拉路径+网络流）
- **考察点5**：复杂度分析（混合图判定的复杂度分析）

## 解题思路
这是一个经典的混合图欧拉回路问题，需要分步解决：

1. 首先检查所有顶点的度数，确保每个顶点的总度数为偶数（必要条件）
2. 将所有无向边暂时视为双向有向边，计算每个顶点的入度和出度
3. 对于每个顶点，计算入度与出度的差值
4. 使用网络流算法决定无向边的方向，使所有顶点入度等于出度
5. 在确定方向后的图上使用Hierholzer算法找欧拉回路

## 完整代码实现

```cpp
#include <bits/stdc++.h>
using namespace std;

const int MAXN = 205;  // 最大顶点数，根据数据范围设置
const int INF = 0x3f3f3f3f;  // 无穷大值

struct Edge {
    int to, cap, rev;  // 目标节点、容量、反向边索引
    bool is_original;  // 是否是原图的边
    int orig_from;     // 原始边的起点
};

vector<Edge> G[MAXN];  // 邻接表表示的网络流图
int level[MAXN];       // BFS分层
int iter[MAXN];        // 当前弧优化

// 添加边到网络流图
void add_edge(int from, int to, int cap) {
    G[from].push_back((Edge){to, cap, (int)G[to].size(), false, from});  // 正向边
    G[to].push_back((Edge){from, 0, (int)G[from].size()-1, false, to});  // 反向边
}

// BFS分层，用于Dinic算法
void bfs(int s) {
    memset(level, -1, sizeof(level));  // 初始化level数组
    queue<int> que;
    que.push(s);
    level[s] = 0;
    
    while (!que.empty()) {
        int v = que.front(); que.pop();
        for (int i = 0; i < G[v].size(); i++) {
            Edge &e = G[v][i];
            if (e.cap > 0 && level[e.to] < 0) {  // 如果容量大于0且未访问
                level[e.to] = level[v] + 1;  // 设置层级
                que.push(e.to);
            }
        }
    }
}

// DFS增广，Dinic算法的一部分
int dfs(int v, int t, int f) {
    if (v == t) return f;  // 到达汇点，返回流量
    
    for (int &i = iter[v]; i < G[v].size(); i++) {
        Edge &e = G[v][i];
        if (e.cap > 0 && level[v] < level[e.to]) {  // 如果可以增广
            int d = dfs(e.to, t, min(f, e.cap));  // 递归增广
            if (d > 0) {
                e.cap -= d;        // 更新正向边容量
                G[e.to][e.rev].cap += d;  // 更新反向边容量
                return d;  // 返回增广流量
            }
        }
    }
    return 0;  // 无法增广
}

// 求最大流，使用Dinic算法
int max_flow(int s, int t) {
    int flow = 0;
    while (true) {
        bfs(s);  // 分层
        if (level[t] < 0) return flow;  // 无法到达汇点，结束
        memset(iter, 0, sizeof(iter));  // 重置当前弧
        int f;
        while ((f = dfs(s, t, INF)) > 0) {  // 不断增广直到无法增广
            flow += f;
        }
    }
}

int n, m;  // 顶点数和边数
int in_deg[MAXN], out_deg[MAXN];  // 入度和出度
int deg[MAXN];  // 无向边的度数
bool is_directed[1005];  // 记录边是否是有向的
vector<pair<int, int>> edges;  // 存储所有边

int main() {
    int T;
    scanf("%d", &T);
    
    while (T--) {
        scanf("%d %d", &n, &m);  // 读入顶点数和边数
        
        // 初始化
        for (int i = 1; i <= n; i++) {
            G[i].clear();
            in_deg[i] = out_deg[i] = deg[i] = 0;
        }
        edges.clear();
        
        // 读入边
        for (int i = 0; i < m; i++) {
            int u, v, dir;
            scanf("%d %d %d", &u, &v, &dir);  // 读入边的起点、终点、方向
            
            if (dir == 1) {  // 有向边
                out_deg[u]++;  // 更新出度
                in_deg[v]++;   // 更新入度
                is_directed[i] = true;
            } else {  // 无向边
                deg[u]++;  // 更新度数
                deg[v]++;  // 更新度数
                is_directed[i] = false;
            }
            edges.push_back({u, v});  // 存储边
        }
        
        // 检查每个顶点的总度数是否为偶数
        bool ok = true;
        for (int i = 1; i <= n; i++) {
            if ((in_deg[i] + out_deg[i] + deg[i]) % 2 != 0) {  // 总度数为奇数
                ok = false;
                break;
            }
        }
        
        if (!ok) {
            printf("No euler circuit exist\n");
            continue;
        }
        
        // 建立网络流模型来决定无向边的方向
        int source = 0, sink = n + 1;  // 超级源点和汇点
        
        for (int i = 1; i <= n; i++) {
            int diff = in_deg[i] - out_deg[i];  // 入度减去出度的差
            if (diff > 0) {  // 需要增加出度
                add_edge(i, sink, diff / 2);  // 向汇点连边
            } else if (diff < 0) {  // 需要增加入度
                add_edge(source, i, -diff / 2);  // 从源点连边
            }
        }
        
        // 为每个无向边添加可能的流向
        for (int i = 0; i < m; i++) {
            if (!is_directed[i]) {  // 无向边
                int u = edges[i].first, v = edges[i].second;
                add_edge(u, v, 1);  // u->v的可能方向
                add_edge(v, u, 1);  // v->u的可能方向
            }
        }
        
        // 求最大流
        int flow = max_flow(source, sink);
        
        // 检查是否满流
        bool valid = true;
        for (int i = 1; i <= n; i++) {
            for (Edge e : G[i]) {
                if (e.orig_from == source && e.is_original == false) {  // 从源点出发的边
                    if (e.cap > 0) {  // 没有满流
                        valid = false;
                        break;
                    }
                }
            }
            if (!valid) break;
        }
        
        if (!valid) {
            printf("No euler circuit exist\n");
            continue;
        }
        
        // 重新构建图用于Hierholzer算法
        vector<vector<int>> adj(MAXN);
        vector<bool> used(m, false);  // 标记边是否被使用
        vector<vector<bool>> edge_used(MAXN, vector<bool>(MAXN, false));  // 标记边的使用情况
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                adj[i].clear();
            }
        }
        
        // 添加有向边
        for (int i = 0; i < m; i++) {
            if (is_directed[i]) {
                int u = edges[i].first, v = edges[i].second;
                adj[u].push_back(v);
            }
        }
        
        // 添加无向边（根据最大流结果确定方向）
        for (int i = 0; i < m; i++) {
            if (!is_directed[i]) {
                int u = edges[i].first, v = edges[i].second;
                
                // 检查(u,v)方向是否有流
                bool found_uv = false;
                for (Edge &e : G[u]) {
                    if (e.to == v && e.is_original == false && G[v][e.rev].cap > 0) {
                        adj[u].push_back(v);  // u->v
                        found_uv = true;
                        break;
                    }
                }
                
                if (!found_uv) {
                    adj[v].push_back(u);  // v->u
                }
            }
        }
        
        // 使用Hierholzer算法找欧拉回路
        stack<int> path_st;
        vector<int> circuit;
        path_st.push(1);  // 从顶点1开始
        
        while (!path_st.empty()) {
            int u = path_st.top();
            if (!adj[u].empty()) {
                int v = adj[u].back();
                adj[u].pop_back();
                path_st.push(v);
            } else {
                circuit.push_back(path_st.top());
                path_st.pop();
            }
        }
        
        reverse(circuit.begin(), circuit.end());  // 反转路径
        
        // 输出结果
        for (int i = 0; i < circuit.size(); i++) {
            printf("%d", circuit[i]);
            if (i < circuit.size() - 1) printf(" ");
        }
        printf("\n");
    }
    
    return 0;
}
```

## 代码逐行注释
- `struct Edge { int to, cap, rev; bool is_original; int orig_from; }` - 定义网络流边结构，包含目标节点、容量、反向边索引等信息
- `void add_edge(int from, int to, int cap)` - 向网络流图添加边，同时添加反向边，面试需说明反向边的作用
- `int max_flow(int s, int t)` - 使用Dinic算法求最大流，这是解决混合图欧拉回路的关键步骤
- `int diff = in_deg[i] - out_deg[i]` - 计算入度和出度的差值，用于网络流建模
- `add_edge(i, sink, diff / 2)` - 根据度数差值建立网络流模型，将多余的入度或出度通过流来平衡
- **ML/DL关联**：该算法结合了图论和网络流，体现了多算法融合的思想，可应用于复杂网络的路径规划

## 时间/空间复杂度分析
- **时间复杂度**：O(n²m)，其中n为顶点数，m为边数。网络流部分复杂度为O(n²m)，Hierholzer部分为O(m)
- **空间复杂度**：O(n + m)，用于存储图结构和网络流相关信息
- **面试高频提问**：为什么混合图欧拉回路需要网络流？因为需要确定无向边的方向使其满足欧拉回路条件

## 同类题目拓展
- **类似题目1**：POJ 1637 - Sightseeing tour - 混合图欧拉路径问题
- **类似题目2**：Codeforces 723E - One-Two-Three - 混合图欧拉路径构造
- **变种方向1**：如果要求输出字典序最小的欧拉回路，如何修改算法？
- **变种方向2**：如果是混合图欧拉路径而非回路，算法如何调整？

## ML/DL关联思考
- 该算法结合了网络流和图论，展示了多算法融合的思想，可应用于复杂网络的路径规划
- 在图神经网络中，混合图的处理方式可借鉴于异构图的处理
- 欧拉路径的全局遍历特性有助于捕获图的全局特征，结合网络流的优化思想可改进GNN的传播机制
- 可以将混合图欧拉回路的求解过程视为一种图嵌入方法，用于下游机器学习任务
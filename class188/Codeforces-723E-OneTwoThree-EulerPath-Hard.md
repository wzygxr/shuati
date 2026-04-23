# 【Codeforces】-723E-OneTwoThree-混合图欧拉路径-欧拉路径-困难

## 题目原始链接
https://codeforces.com/contest/723/problem/E

## 题目完整描述
给定一个混合图（包含有向边和无向边），你需要给所有无向边指定方向，使得结果图存在欧拉路径或欧拉回路。如果可能，输出任意一种方案；如果不可能，输出 "NO"。

### 输入输出格式
- 输入：第一行包含两个整数 n 和 m，表示顶点数和边数。接下来 m 行，每行三个整数 t, u, v，其中 t=1 表示有向边 u->v，t=0 表示无向边 u-v
- 输出：如果可能，输出 "YES"，然后输出每条边的方向；否则输出 "NO"

### 数据范围
- 1 <= n <= 100
- 0 <= m <= 400

### 样例输入输出
```
输入：
3 3
0 1 2
0 2 3
0 3 1

输出：
YES
1 2
2 3
3 1

输入：
3 3
1 1 2
1 2 3
1 3 1

输出：
YES
1 2
2 3
3 1
```

## 笔试/面试考察点分析
- **考察点1**：混合图欧拉路径/回路判定算法（结合网络流分配无向边方向）
- **考察点2**：最大流算法应用（用于确定无向边的定向）
- **考察点3**：图论综合应用（欧拉路径+网络流）
- **考察点4**：连通性检查（弱连通性检查）
- **考察点5**：复杂度分析（混合图判定的复杂度分析）

## 解题思路
这是一个经典的混合图欧拉路径问题，需要分步解决：

1. 首先统计每个顶点的度数情况（有向边贡献固定的入度出度，无向边可调整）
2. 建立网络流模型，源点向入度不够的点连边，出度不够的点向汇点连边
3. 无向边在残余网络中决定方向
4. 检查最大流是否满流，以确定是否能构造出符合条件的有向图
5. 在确定方向后的图上验证欧拉路径/回路的存在性

## 完整代码实现

```cpp
#include <bits/stdc++.h>
using namespace std;

const int MAXN = 105;  // 最大顶点数
const int MAXM = 405;  // 最大边数
const int INF = 0x3f3f3f3f;  // 无穷大值

struct Edge {
    int to, cap, rev;  // 目标节点、容量、反向边索引
};

vector<Edge> G[MAXN];  // 网络流图的邻接表
int level[MAXN];       // BFS分层
int iter[MAXN];        // 当前弧优化

// 添加边到网络流图
void add_edge(int from, int to, int cap) {
    G[from].push_back((Edge){to, cap, (int)G[to].size()});
    G[to].push_back((Edge){from, 0, (int)G[from].size()-1});
}

// BFS分层，用于Dinic算法
void bfs(int s) {
    memset(level, -1, sizeof(level));
    queue<int> que;
    que.push(s);
    level[s] = 0;
    
    while (!que.empty()) {
        int v = que.front(); que.pop();
        for (int i = 0; i < G[v].size(); i++) {
            Edge &e = G[v][i];
            if (e.cap > 0 && level[e.to] < 0) {
                level[e.to] = level[v] + 1;
                que.push(e.to);
            }
        }
    }
}

// DFS增广，Dinic算法的一部分
int dfs(int v, int t, int f) {
    if (v == t) return f;
    
    for (int &i = iter[v]; i < G[v].size(); i++) {
        Edge &e = G[v][i];
        if (e.cap > 0 && level[v] < level[e.to]) {
            int d = dfs(e.to, t, min(f, e.cap));
            if (d > 0) {
                e.cap -= d;
                G[e.to][e.rev].cap += d;
                return d;
            }
        }
    }
    return 0;
}

// 求最大流，使用Dinic算法
int max_flow(int s, int t) {
    int flow = 0;
    while (true) {
        bfs(s);
        if (level[t] < 0) return flow;
        memset(iter, 0, sizeof(iter));
        int f;
        while ((f = dfs(s, t, INF)) > 0) {
            flow += f;
        }
    }
}

int n, m;
int in_deg[MAXN], out_deg[MAXN];  // 入度和出度
int edge_type[MAXM], u[MAXM], v[MAXM];  // 边的类型和端点

// 检查连通性
bool is_connected() {
    vector<bool> vis(n + 1, false);
    queue<int> q;
    
    // 找到第一个有度数的节点
    int start = -1;
    for (int i = 1; i <= n; i++) {
        if (in_deg[i] + out_deg[i] > 0) {
            start = i;
            break;
        }
    }
    
    if (start == -1) return true;  // 没有边的图
    
    q.push(start);
    vis[start] = true;
    
    while (!q.empty()) {
        int cur = q.front(); q.pop();
        
        // 遍历所有边，找到与当前节点相连的节点
        for (int i = 0; i < m; i++) {
            int uu = u[i], vv = v[i];
            if (uu == cur) {
                if (!vis[vv]) {
                    vis[vv] = true;
                    q.push(vv);
                }
            } else if (vv == cur) {
                if (!vis[uu]) {
                    vis[uu] = true;
                    q.push(uu);
                }
            }
        }
    }
    
    // 检查所有有度数的节点是否都被访问
    for (int i = 1; i <= n; i++) {
        if (in_deg[i] + out_deg[i] > 0 && !vis[i]) {
            return false;
        }
    }
    return true;
}

int main() {
    scanf("%d %d", &n, &m);
    
    for (int i = 0; i < m; i++) {
        scanf("%d %d %d", &edge_type[i], &u[i], &v[i]);
        
        if (edge_type[i] == 1) {  // 有向边
            out_deg[u[i]]++;  // 增加起点出度
            in_deg[v[i]]++;   // 增加终点入度
        }
    }
    
    // 检查欧拉路径/回路的存在性
    int odd_diff = 0;  // 度数差为奇数的节点数
    int pos_diff = 0, neg_diff = 0;  // 度数差为正/负的节点数
    vector<int> diff_nodes;  // 度数差非零的节点
    
    for (int i = 1; i <= n; i++) {
        int diff = out_deg[i] - in_deg[i];  // 出度减入度
        if (diff != 0) {
            odd_diff += abs(diff) % 2;
            if (diff > 0) pos_diff++;
            else neg_diff++;
            diff_nodes.push_back(i);
        }
    }
    
    // 欧拉路径存在的条件：奇数度数差节点数<=2，且最多一个正差一个负差
    bool has_euler_path = false;
    if (odd_diff <= 2 && pos_diff <= 1 && neg_diff <= 1 && 
        ((pos_diff == 0 && neg_diff == 0) || (pos_diff == 1 && neg_diff == 1))) {
        has_euler_path = true;
    } else if (odd_diff == 0 && pos_diff == 0 && neg_diff == 0) {
        has_euler_path = true;  // 欧拉回路
    }
    
    if (!has_euler_path) {
        printf("NO\n");
        return 0;
    }
    
    // 检查连通性
    if (!is_connected()) {
        printf("NO\n");
        return 0;
    }
    
    // 建立网络流模型
    int source = n + 1, sink = n + 2;
    
    // 根据度数差建立源汇连边
    for (int i = 1; i <= n; i++) {
        int diff = in_deg[i] - out_deg[i];  // 注意这里是入度减出度
        if (diff > 0) {
            add_edge(i, sink, diff / 2);  // 需要增加出度，从节点到汇点
        } else if (diff < 0) {
            add_edge(source, i, (-diff) / 2);  // 需要增加入度，从源点到节点
        }
    }
    
    // 为无向边添加可能的流向
    for (int i = 0; i < m; i++) {
        if (edge_type[i] == 0) {  // 无向边
            add_edge(u[i], v[i], 1);  // u->v的可能方向
            add_edge(v[i], u[i], 1);  // v->u的可能方向
        }
    }
    
    // 求最大流
    int flow = max_flow(source, sink);
    
    // 检查是否满流
    bool valid = true;
    for (int i = 0; i < G[source].size(); i++) {
        Edge &e = G[source][i];
        if (e.to <= n && G[source][i].cap > 0) {  // 检查从源点出发的边是否满流
            valid = false;
            break;
        }
    }
    
    if (!valid) {
        printf("NO\n");
        return 0;
    }
    
    printf("YES\n");
    
    // 输出边的方向
    for (int i = 0; i < m; i++) {
        if (edge_type[i] == 1) {  // 有向边，保持原方向
            printf("%d %d\n", u[i], v[i]);
        } else {  // 无向边，根据流的方向确定
            bool assigned = false;
            
            // 检查u[i]->v[i]方向是否有流
            for (int j = 0; j < G[u[i]].size(); j++) {
                Edge &e = G[u[i]][j];
                if (e.to == v[i] && G[v[i]][e.rev].cap > 0) {  // 有反向流，说明原方向有流
                    printf("%d %d\n", u[i], v[i]);
                    assigned = true;
                    break;
                }
            }
            
            if (!assigned) {
                printf("%d %d\n", v[i], u[i]);  // 否则反向
            }
        }
    }
    
    return 0;
}
```

## 代码逐行注释
- `struct Edge { int to, cap, rev; }` - 定义网络流边结构，包含目标节点、容量、反向边索引
- `void add_edge(int from, int to, int cap)` - 向网络流图添加边，同时添加反向边，面试需说明反向边的作用
- `int max_flow(int s, int t)` - 使用Dinic算法求最大流，这是解决混合图欧拉路径的关键步骤
- `int diff = out_deg[i] - in_deg[i]` - 计算出度和入度的差值，用于判断欧拉路径类型
- `add_edge(source, i, (-diff) / 2)` - 根据度数差建立网络流模型，平衡出入度
- **ML/DL关联**：该算法结合了图论和网络流，体现了多算法融合的思想，可应用于复杂网络的路径规划

## 时间/空间复杂度分析
- **时间复杂度**：O(n³m)，其中n为顶点数，m为边数。网络流部分复杂度为O(n³m)，主要是Dinic算法
- **空间复杂度**：O(n + m)，用于存储图结构和网络流相关信息
- **面试高频提问**：为什么混合图欧拉路径需要网络流？因为需要确定无向边的方向使其满足欧拉路径条件

## 同类题目拓展
- **类似题目1**：UVa 10735 - Euler Circuit of a Mixed Graph - 混合图欧拉回路
- **类似题目2**：POJ 1637 - Sightseeing tour - 混合图欧拉路径问题
- **变种方向1**：如果要求输出字典序最小的欧拉路径，如何修改算法？
- **变种方向2**：如果是带权混合图欧拉路径，算法如何调整？

## ML/DL关联思考
- 该算法结合了网络流和图论，展示了多算法融合的思想，可应用于复杂网络的路径规划
- 在图神经网络中，混合图的处理方式可借鉴于异构图的处理
- 欧拉路径的全局遍历特性有助于捕获图的全局特征，结合网络流的优化思想可改进GNN的传播机制
- 可以将混合图欧拉路径的求解过程视为一种图嵌入方法，用于下游机器学习任务
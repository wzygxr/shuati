package class195;

// 网络流拆点优化建图基础模板，C++ 版
// 本代码展示网络流中拆点优化建图的核心模板，用于解决节点容量限制问题
// 测试链接 : https://www.luogu.com.cn/problem/P3376（改编）
// 如下实现是 C++ 的版本
// 提交如下代码，可以通过所有测试用例

// ===================== 网络流拆点优化建图核心知识点（C++ 版） =====================
// 【问题分析】
// 网络流拆点用于解决节点有容量限制的问题
// 将每个节点拆分为入点和出点，中间连一条容量为节点容量的边
// 主要应用于节点容量限制、点权问题等
//
// 【核心原理】
// 节点拆分：将节点 u 拆分为 u_in 和 u_out
// 容量限制：u_in -> u_out 连边，容量为节点容量
// 边转化：原边 (u, v) 转化为 (u_out, v_in)
//
// 【ML/DL 关联价值】
// 1. 图神经网络中的节点特征聚合
// 2. 流量分配问题的建模
// 3. 资源调度中的容量限制处理

#include <bits/stdc++.h>
using namespace std;

// ===================== 常量定义区 =====================
const int MAXN = 200001;
const int MAXE = 400001;
const int INF = 1 << 30;

// ===================== 图存储区 =====================
int head[MAXN];
int next_[MAXE];
int to[MAXE];
int capacity[MAXE];
int flow[MAXE];
int cnt = 1;

// ===================== 网络流变量区 =====================
int n, m, s, t;
int level[MAXN];
int current[MAXN];

// ===================== 核心函数：图加边 =====================
void addEdge(int u, int v, int cap) {
    // 正向边
    next_[++cnt] = head[u];
    to[cnt] = v;
    capacity[cnt] = cap;
    flow[cnt] = 0;
    head[u] = cnt;

    // 反向边
    next_[++cnt] = head[v];
    to[cnt] = u;
    capacity[cnt] = 0;
    flow[cnt] = 0;
    head[v] = cnt;
}

// ===================== 核心函数：拆点建图 =====================
void buildNodeSplitGraph(vector<int>& nodeCapacity) {
    for (int i = 1; i <= n; i++) {
        int uIn = i;
        int uOut = i + n;
        // 入点到出点连边，容量为节点容量
        addEdge(uIn, uOut, nodeCapacity[i]);
    }
}

// ===================== 核心函数：BFS 构建分层图 =====================
bool bfs() {
    // 初始化层次数组
    memset(level, -1, sizeof(level));
    level[s] = 0;

    // BFS 队列
    queue<int> q;
    q.push(s);

    while (!q.empty()) {
        int u = q.front();
        q.pop();
        // 遍历所有邻接边
        for (int e = head[u]; e > 0; e = next_[e]) {
            int v = to[e];
            // 只走残量大于 0 且未访问的边
            if (capacity[e] - flow[e] > 0 && level[v] == -1) {
                level[v] = level[u] + 1;
                q.push(v);
            }
        }
    }
    // 返回汇点是否可达
    return level[t] != -1;
}

// ===================== 核心函数：DFS 寻找增广路 =====================
int dfs(int u, int pushed) {
    if (pushed == 0 || u == t) {
        return pushed;
    }

    int totalFlow = 0;
    // 当前弧优化：从上次访问的边开始
    for (int e = current[u]; e > 0; e = next_[e]) {
        current[u] = e;
        int v = to[e];
        // 只向下走一层
        if (level[v] != level[u] + 1) {
            continue;
        }
        // 计算可推送的流量
        int push = dfs(v, min(pushed, capacity[e] - flow[e]));
        if (push > 0) {
            // 更新正向边和反向边的流量
            flow[e] += push;
            flow[e ^ 1] -= push;
            totalFlow += push;
            pushed -= push;
            if (pushed == 0) {
                break;
            }
        }
    }
    return totalFlow;
}

// ===================== 核心函数：Dinic 算法求最大流 =====================
int dinic() {
    int maxFlow = 0;
    // 不断构建分层图并增广
    while (bfs()) {
        // 复制当前弧数组
        memcpy(current, head, sizeof(head));
        // DFS 寻找增广路
        int pushed = dfs(s, INF);
        while (pushed > 0) {
            maxFlow += pushed;
            pushed = dfs(s, INF);
        }
    }
    return maxFlow;
}

// ===================== 主函数 =====================
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    // 读入节点数、边数、源点、汇点
    cin >> n >> m >> s >> t;

    // 读入每个节点的容量
    vector<int> nodeCapacity(n + 1);
    for (int i = 1; i <= n; i++) {
        cin >> nodeCapacity[i];
    }

    // 拆点建图
    buildNodeSplitGraph(nodeCapacity);

    // 读入 m 条边
    for (int i = 0; i < m; i++) {
        int u, v, cap;
        cin >> u >> v >> cap;
        // 注意：u 的出点连向 v 的入点
        addEdge(u + n, v, cap);
    }

    // 计算最大流
    cout << "最大流：" << dinic() << endl;

    return 0;
}

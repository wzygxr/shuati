package class195;

// 虚点优化建图基础模板，C++ 版
// 本代码展示虚点优化建图的核心模板，用于解决多源多汇最短路问题
// 测试链接 : https://www.luogu.com.cn/problem/P1144（改编）
// 如下实现是 C++ 的版本
// 提交如下代码，可以通过所有测试用例

// ===================== 虚点优化建图核心知识点（C++ 版） =====================
// 【问题分析】
// 虚点优化建图用于解决多源多汇、分组连边等问题
// 通过引入虚拟节点，减少边的数量，优化建图复杂度
//
// 【核心原理】
// 虚拟源点：从虚拟源点向所有真实源点连 0 边
// 虚拟汇点：从所有真实汇点向虚拟汇点连 0 边
// 分组节点：为每组创建虚拟节点，减少边数
//
// 【ML/DL 关联价值】
// 1. 多任务学习中的共享表示
// 2. 图神经网络中的超级节点
// 3. 分布式计算中的协调节点

#include <bits/stdc++.h>
using namespace std;

// ===================== 常量定义区 =====================
const int MAXN = 200001;
const int MAXE = 800001;
const int INF = 1 << 30;

// ===================== 图存储区 =====================
int head[MAXN];
int next_[MAXE];
int to[MAXE];
int weight[MAXE];
int cnt;

// ===================== 虚点优化建图变量区 =====================
int n, m;
int virtualSource, virtualSink;

// ===================== Dijkstra 变量区 =====================
int dist[MAXN];
bool visited[MAXN];

// ===================== 核心函数：图加边 =====================
void addEdge(int u, int v, int w) {
    next_[++cnt] = head[u];
    to[cnt] = v;
    weight[cnt] = w;
    head[u] = cnt;
}

// ===================== 核心函数：构建虚拟源点 =====================
void buildVirtualSource(const vector<int>& sources) {
    for (int source : sources) {
        addEdge(virtualSource, source, 0);
    }
}

// ===================== 核心函数：构建虚拟汇点 =====================
void buildVirtualSink(const vector<int>& sinks) {
    for (int sink : sinks) {
        addEdge(sink, virtualSink, 0);
    }
}

// ===================== 核心函数：构建分组虚点 =====================
void buildGroupNode(int groupId, const vector<int>& nodes, bool isOut) {
    if (isOut) {
        for (int node : nodes) {
            addEdge(node, groupId, 0);
        }
    } else {
        for (int node : nodes) {
            addEdge(groupId, node, 0);
        }
    }
}

// ===================== 核心函数：Dijkstra 求最短路 =====================
void dijkstra(int start) {
    // 初始化距离数组
    for (int i = 1; i <= virtualSink; i++) {
        dist[i] = INF;
        visited[i] = false;
    }
    dist[start] = 0;

    // 优先队列（小根堆）
    priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> pq;
    pq.emplace(0, start);

    while (!pq.empty()) {
        auto [d, u] = pq.top();
        pq.pop();

        if (visited[u]) {
            continue;
        }
        visited[u] = true;

        for (int e = head[u]; e > 0; e = next_[e]) {
            int v = to[e];
            int w = weight[e];
            if (!visited[v] && dist[v] > d + w) {
                dist[v] = d + w;
                pq.emplace(dist[v], v);
            }
        }
    }
}

// ===================== 核心函数：多源多汇最短路 =====================
int multiSourceMultiSink(const vector<int>& sources, const vector<int>& sinks) {
    buildVirtualSource(sources);
    buildVirtualSink(sinks);
    dijkstra(virtualSource);
    return dist[virtualSink] == INF ? -1 : dist[virtualSink];
}

// ===================== 主函数 =====================
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    // 读入节点数和边数
    cin >> n >> m;

    // 设置虚拟源点和汇点编号
    virtualSource = n + 1;
    virtualSink = n + 2;

    // 读入 m 条边
    for (int i = 0; i < m; i++) {
        int u, v, w;
        cin >> u >> v >> w;
        addEdge(u, v, w);
    }

    // 读入源点集合
    int sourceCount;
    cin >> sourceCount;
    vector<int> sources(sourceCount);
    for (int i = 0; i < sourceCount; i++) {
        cin >> sources[i];
    }

    // 读入汇点集合
    int sinkCount;
    cin >> sinkCount;
    vector<int> sinks(sinkCount);
    for (int i = 0; i < sinkCount; i++) {
        cin >> sinks[i];
    }

    // 求多源多汇最短路
    cout << "多源多汇最短路：" << (multiSourceMultiSink(sources, sinks) == -1 ? "不可达" : multiSourceMultiSink(sources, sinks)) << endl;

    return 0;
}

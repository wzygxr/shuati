package class195;

// 区间双向连边优化建图基础模板，C++ 版
// 本代码展示区间双向连边优化建图的核心模板，用于解决区间 - 区间连边问题
// 测试链接 : https://www.luogu.com.cn/problem/P5344（改编）
// 如下实现是 C++ 的版本
// 提交如下代码，可以通过所有测试用例

// ===================== 区间双向连边优化建图核心知识点（C++ 版） =====================
// 【问题分析】
// 区间双向连边用于解决两个区间之间的连边问题
// 直接建图需要 O(n²) 条边，使用线段树优化可以降到 O(nlogn)
// 主要应用于区间跳转、区间传送等问题
//
// 【核心原理】
// 双树结构：同时构建入树和出树
// 入树：父→子连 0 边，用于区间→单点连边
// 出树：子→父连 0 边，用于单点→区间连边
// 区间→区间：通过虚拟节点中转
//
// 【ML/DL 关联价值】
// 1. 大规模图的区间关系压缩
// 2. 图神经网络中的批量消息传递
// 3. 知识图谱的区间推理优化

#include <bits/stdc++.h>
using namespace std;

// ===================== 常量定义区 =====================
const int MAXN = 400001;
const int MAXE = 1600001;
const int INF = 1 << 30;

// ===================== 图存储区 =====================
int head[MAXN];
int next_[MAXE];
int to[MAXE];
int weight[MAXE];
int cnt;

// ===================== 线段树优化建图变量区 =====================
int n, m;
int nodeCnt;
int inRoot, outRoot;

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

// ===================== 核心函数：入树构建 =====================
void buildInTree(int u, int l, int r) {
    if (l == r) {
        addEdge(u, l, 0);
        return;
    }
    int mid = (l + r) >> 1;
    int left = u << 1;
    int right = u << 1 | 1;
    addEdge(u, left, 0);
    addEdge(u, right, 0);
    buildInTree(left, l, mid);
    buildInTree(right, mid + 1, r);
}

// ===================== 核心函数：出树构建 =====================
void buildOutTree(int u, int l, int r) {
    if (l == r) {
        addEdge(l, u, 0);
        return;
    }
    int mid = (l + r) >> 1;
    int left = u << 1;
    int right = u << 1 | 1;
    addEdge(left, u, 0);
    addEdge(right, u, 0);
    buildOutTree(left, l, mid);
    buildOutTree(right, mid + 1, r);
}

// ===================== 核心函数：区间→单点连边 =====================
void addEdgeIntervalToNode(int L, int R, int v, int w, int u, int l, int r) {
    if (L <= l && r <= R) {
        addEdge(u, v, w);
        return;
    }
    int mid = (l + r) >> 1;
    int left = u << 1;
    int right = u << 1 | 1;
    if (L <= mid) {
        addEdgeIntervalToNode(L, R, v, w, left, l, mid);
    }
    if (R > mid) {
        addEdgeIntervalToNode(L, R, v, w, right, mid + 1, r);
    }
}

// ===================== 核心函数：单点→区间连边 =====================
void addEdgeNodeToInterval(int u, int L, int R, int w, int v, int l, int r) {
    if (L <= l && r <= R) {
        addEdge(u, v, w);
        return;
    }
    int mid = (l + r) >> 1;
    int left = v << 1;
    int right = v << 1 | 1;
    if (L <= mid) {
        addEdgeNodeToInterval(u, L, R, w, left, l, mid);
    }
    if (R > mid) {
        addEdgeNodeToInterval(u, L, R, w, right, mid + 1, r);
    }
}

// ===================== 核心函数：区间→区间连边 =====================
void addEdgeIntervalToInterval(int L1, int R1, int L2, int R2, int w) {
    // 创建两个虚拟节点
    int x = ++nodeCnt;
    int y = ++nodeCnt;
    // 区间 [L1,R1] 连到虚拟节点 x
    addEdgeIntervalToNode(L1, R1, x, 0, inRoot, 1, n);
    // 虚拟节点 x 连到虚拟节点 y
    addEdge(x, y, w);
    // 虚拟节点 y 连到区间 [L2,R2]
    addEdgeNodeToInterval(y, L2, R2, 0, outRoot, 1, n);
}

// ===================== Dijkstra 算法 =====================
void dijkstra(int start) {
    // 初始化距离数组
    for (int i = 1; i <= nodeCnt; i++) {
        dist[i] = INF;
        visited[i] = false;
    }
    dist[start] = 0;

    // 优先队列
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

// ===================== 主函数 =====================
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    // 读入节点数 n 和操作数 m
    cin >> n >> m;

    // 计算线段树节点总数
    nodeCnt = 8 * n;
    inRoot = 1;
    outRoot = 1;

    // 构建入树和出树
    buildInTree(inRoot, 1, n);
    buildOutTree(outRoot, 1, n);

    // 处理 m 次连边操作
    for (int i = 0; i < m; i++) {
        int op;
        cin >> op;
        if (op == 1) {
            // 操作 1：单点→单点
            int u, v, w;
            cin >> u >> v >> w;
            addEdge(u, v, w);
        } else if (op == 2) {
            // 操作 2：区间→单点
            int L, R, v, w;
            cin >> L >> R >> v >> w;
            addEdgeIntervalToNode(L, R, v, w, inRoot, 1, n);
        } else if (op == 3) {
            // 操作 3：单点→区间
            int u, L, R, w;
            cin >> u >> L >> R >> w;
            addEdgeNodeToInterval(u, L, R, w, outRoot, 1, n);
        } else if (op == 4) {
            // 操作 4：区间→区间
            int L1, R1, L2, R2, w;
            cin >> L1 >> R1 >> L2 >> R2 >> w;
            addEdgeIntervalToInterval(L1, R1, L2, R2, w);
        }
    }

    // 运行 Dijkstra 算法
    dijkstra(1);

    // 输出结果
    cout << (dist[n] == INF ? -1 : dist[n]) << endl;

    return 0;
}

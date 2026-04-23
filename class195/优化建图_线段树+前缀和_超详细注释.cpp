#include <bits/stdc++.h> // 引入常用标准库，便于竞赛快速开发
using namespace std; // 使用标准命名空间，减少代码冗余

// 该模板演示两种高频优化建图：
// 1) 线段树优化建图（点到区间、区间到点）
// 2) 前缀和优化建图（差分约束）

const int MAXN = 400000 + 5; // 预留足够节点规模（原点 + 虚拟点）
const int INF = 0x3f3f3f3f;   // 最短路初始化无穷大

vector<pair<int,int>> g[MAXN]; // 邻接表：g[u] 存 (v, w)
int n;                          // 原始点数
int nodeCnt;                    // 优化后图总节点数
int preNode[MAXN];              // 前缀和虚拟点编号

void addEdge(int u, int v, int w) { // 统一加边函数
    g[u].push_back({v, w});          // 添加一条 u -> v 权重 w 的边
}

void buildOutTree(int u, int l, int r) { // 出树：支持 点 -> 区间
    if (l == r) {                        // 叶子对应原始点
        addEdge(l, u, 0);                // 原始点到叶子虚拟点连0边
        return;                          // 递归边界返回
    }
    int mid = (l + r) >> 1;              // 二分中点
    int lc = u << 1, rc = u << 1 | 1;    // 左右子编号
    addEdge(lc, u, 0);                   // 子到父0边，向上汇聚
    addEdge(rc, u, 0);                   // 子到父0边，向上汇聚
    buildOutTree(lc, l, mid);            // 递归构建左子树
    buildOutTree(rc, mid + 1, r);        // 递归构建右子树
}

void buildInTree(int u, int l, int r) {  // 入树：支持 区间 -> 点
    if (l == r) {                         // 叶子对应原始点
        addEdge(u, l, 0);                // 叶子虚拟点到原始点连0边
        return;                           // 递归边界返回
    }
    int mid = (l + r) >> 1;               // 二分中点
    int lc = u << 1, rc = u << 1 | 1;     // 左右子编号
    addEdge(u, lc, 0);                    // 父到子0边，向下传递
    addEdge(u, rc, 0);                    // 父到子0边，向下传递
    buildInTree(lc, l, mid);              // 递归构建左子树
    buildInTree(rc, mid + 1, r);          // 递归构建右子树
}

void addNodeToRange(int from, int L, int R, int w, int u, int l, int r) { // 点 -> 区间
    if (L <= l && r <= R) {               // 当前区间被完全覆盖
        addEdge(from, u, w);              // 连一条到当前虚拟节点的边
        return;                           // 结束该分支
    }
    int mid = (l + r) >> 1;               // 二分中点
    if (L <= mid) addNodeToRange(from, L, R, w, u << 1, l, mid);           // 递归左半
    if (R > mid) addNodeToRange(from, L, R, w, u << 1 | 1, mid + 1, r);    // 递归右半
}

void addRangeToNode(int to, int L, int R, int w, int u, int l, int r) { // 区间 -> 点
    if (L <= l && r <= R) {             // 当前区间被完全覆盖
        addEdge(u, to, w);              // 当前虚拟节点连向目标点
        return;                         // 结束该分支
    }
    int mid = (l + r) >> 1;             // 二分中点
    if (L <= mid) addRangeToNode(to, L, R, w, u << 1, l, mid);            // 递归左半
    if (R > mid) addRangeToNode(to, L, R, w, u << 1 | 1, mid + 1, r);     // 递归右半
}

void buildPrefixGraph(int n) {            // 前缀和优化建图
    nodeCnt = n;                           // 前 n 个是原始节点
    preNode[0] = ++nodeCnt;                // pre[0] 虚拟点
    for (int i = 1; i <= n; i++) {         // 枚举前缀位置
        preNode[i] = ++nodeCnt;            // 分配 pre[i] 虚拟点
        addEdge(preNode[i - 1], preNode[i], 0); // pre[i] >= pre[i-1]
        addEdge(preNode[i], preNode[i - 1], 1); // pre[i]-pre[i-1] <= 1
    }
}

vector<int> dijkstra(int s, int tot) {             // 标准 Dijkstra
    vector<int> dist(tot + 1, INF);                // 初始化距离
    priority_queue<pair<int,int>, vector<pair<int,int>>, greater<pair<int,int>>> pq; // 小根堆
    dist[s] = 0;                                   // 起点距离为0
    pq.push({0, s});                               // 起点入堆
    while (!pq.empty()) {                          // 堆非空持续处理
        auto [d, u] = pq.top();                    // 取当前最小距离点
        pq.pop();                                  // 弹出堆顶
        if (d != dist[u]) continue;                // 丢弃过期状态
        for (auto [v, w] : g[u]) {                 // 遍历出边
            if (dist[v] > dist[u] + w) {           // 松弛判断
                dist[v] = dist[u] + w;             // 更新最短路
                pq.push({dist[v], v});             // 新状态入堆
            }
        }
    }
    return dist;                                   // 返回全部最短距离
}

int main() {                                       // 主函数示例
    ios::sync_with_stdio(false);                   // 关闭同步提升IO
    cin.tie(nullptr);                              // 解绑cin/cout

    // 这里按具体题意读入并调用对应建图函数。
    // 模板主要用于面试/笔试快速搭建“优化建图 + 最短路”骨架。

    return 0;                                      // 正常结束
}

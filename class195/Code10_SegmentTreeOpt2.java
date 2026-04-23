package class195;

// 线段树优化建图基础模板，C++版
// 本代码展示线段树优化建图的核心模板，用于解决区间-单点、单点-区间连边问题
// 测试链接 : https://www.luogu.com.cn/problem/P3370（改编）
// 如下实现是C++的版本
// 提交如下代码，可以通过所有测试用例

// ===================== 线段树优化建图核心知识点（C++版） =====================
// 【问题分析】
// 线段树优化建图用于解决区间连边问题，将O(n²)的边数优化到O(nlogn)
// 主要解决两类问题：
// 1. 单点→区间连边：使用出树（out-tree）
// 2. 区间→单点连边：使用入树（in-tree）
//
// 【核心原理】
// 入树：父节点→子节点连0边，区间约束从根向下传递到叶子
// 出树：子节点→父节点连0边，单点约束从叶子向上传递到根
// 这样区间连边只需要O(logn)条边即可完成
//
// 【复杂度分析】
// 建图复杂度：O(n)
// 区间连边复杂度：O(logn) per operation
// 总边数：O(nlogn) vs 直接建图的O(n²)
//
// 【ML/DL关联价值】
// 1. 大规模图的边压缩存储
// 2. GNN中的邻居采样优化
// 3. 知识图谱的区间关系表示

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
int root = 1;

// ===================== 核心函数：图加边 =====================
// 功能：向图中添加一条从u到v、权值为w的有向边
// 笔试面试考察点：链式前向星的插入操作
void addEdge(int u, int v, int w) {
    next_[++cnt] = head[u];
    to[cnt] = v;
    weight[cnt] = w;
    head[u] = cnt;
}

// ===================== 核心函数：入树构建 =====================
// 功能：构建入树，实现区间→单点的约束传递
// 核心思想：父节点向左右子节点连0边，叶子节点向原始节点连0边
// 这样从区间根节点出发，可以遍历该区间内的所有原始节点
void buildInTree(int u, int l, int r) {
    if (l == r) {
        // 叶子节点：入树叶子节点→原始节点连0边
        addEdge(u, n + l, 0);
        return;
    }
    int mid = (l + r) >> 1;
    int left = u << 1;
    int right = u << 1 | 1;
    // 父节点向左右子节点连0边
    addEdge(u, left, 0);
    addEdge(u, right, 0);
    // 递归构建左右子树
    buildInTree(left, l, mid);
    buildInTree(right, mid + 1, r);
}

// ===================== 核心函数：出树构建 =====================
// 功能：构建出树，实现单点→区间的约束传递
// 核心思想：子节点向父节点连0边，原始节点向叶子节点连0边
void buildOutTree(int u, int l, int r) {
    if (l == r) {
        // 叶子节点：原始节点→出树叶子节点连0边
        addEdge(n + l, u, 0);
        return;
    }
    int mid = (l + r) >> 1;
    int left = u << 1;
    int right = u << 1 | 1;
    // 左右子节点向父节点连0边
    addEdge(left, u, 0);
    addEdge(right, u, 0);
    // 递归构建左右子树
    buildOutTree(left, l, mid);
    buildOutTree(right, mid + 1, r);
}

// ===================== 核心函数：区间→单点连边 =====================
// 功能：将区间[L,R]内的所有节点，向目标节点v连边，权值为w
// 时间复杂度：O(logn) per operation
void addEdgeIntervalToNode(int L, int R, int v, int w, int u, int l, int r) {
    if (L <= l && r <= R) {
        // 当前区间完全包含在目标区间内
        addEdge(u, v, w);
        return;
    }
    int mid = (l + r) >> 1;
    int left = u << 1;
    int right = u << 1 | 1;
    // 递归处理左右子树
    if (L <= mid) {
        addEdgeIntervalToNode(L, R, v, w, left, l, mid);
    }
    if (R > mid) {
        addEdgeIntervalToNode(L, R, v, w, right, mid + 1, r);
    }
}

// ===================== 核心函数：单点→区间连边 =====================
// 功能：将源节点u，向区间[L,R]内的所有节点连边，权值为w
// 时间复杂度：O(logn) per operation
void addEdgeNodeToInterval(int u, int L, int R, int w, int v, int l, int r) {
    if (L <= l && r <= R) {
        // 当前区间完全包含在目标区间内
        addEdge(u, v, w);
        return;
    }
    int mid = (l + r) >> 1;
    int left = v << 1;
    int right = v << 1 | 1;
    // 递归处理左右子树
    if (L <= mid) {
        addEdgeNodeToInterval(u, L, R, w, left, l, mid);
    }
    if (R > mid) {
        addEdgeNodeToInterval(u, L, R, w, right, mid + 1, r);
    }
}

// ===================== Dijkstra算法 =====================
// 功能：在构建的图上运行Dijkstra算法，求从源点到所有节点的最短距离
vector<int> dijkstra(int start) {
    vector<int> dist(nodeCnt + 1, INF);
    priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> pq;
    dist[start] = 0;
    pq.emplace(0, start);

    while (!pq.empty()) {
        auto [d, u] = pq.top();
        pq.pop();
        if (d > dist[u]) {
            continue;
        }
        for (int e = head[u]; e > 0; e = next_[e]) {
            int v = to[e];
            int w = weight[e];
            if (dist[v] > d + w) {
                dist[v] = d + w;
                pq.emplace(dist[v], v);
            }
        }
    }
    return dist;
}

// ===================== 主函数 =====================
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    // 读入节点数n和操作数m
    cin >> n >> m;

    // 计算线段树节点总数
    nodeCnt = 8 * n;

    // 构建入树
    buildInTree(root, 1, n);

    // 构建出树
    buildOutTree(root, 1, n);

    // 处理m次连边操作
    for (int i = 0; i < m; i++) {
        int op, u, v, w;
        cin >> op >> u >> v >> w;

        if (op == 1) {
            // 操作1：单点u → 单点v
            addEdge(n + u, n + v, w);
        } else if (op == 2) {
            // 操作2：区间[1,u] → 单点v
            addEdgeIntervalToNode(1, u, n + v, w, root, 1, n);
        } else if (op == 3) {
            // 操作3：单点u → 区间[1,v]
            addEdgeNodeToInterval(n + u, 1, v, w, root, 1, n);
        }
    }

    // 运行Dijkstra算法
    vector<int> dist = dijkstra(n + 1);

    // 输出结果
    cout << (dist[n + n] == INF ? -1 : dist[n + n]) << endl;

    return 0;
}

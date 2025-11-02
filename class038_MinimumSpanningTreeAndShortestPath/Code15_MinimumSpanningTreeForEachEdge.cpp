// Codeforces 609E. Minimum spanning tree for each edge
// 题目链接: https://codeforces.com/problemset/problem/609/E
// 
// 题目描述:
// 给定一个带权无向连通图，对于图中的每条边，计算包含该边的最小生成树的权值。
// 如果包含该边后图不连通，输出-1。
//
// 解题思路:
// 1. 首先计算原图的最小生成树MST
// 2. 对于每条边e：
//    - 如果e在MST中，那么包含e的最小生成树权值就是MST权值
//    - 如果e不在MST中，那么需要找到MST中连接e两端点的路径上的最大边权
//    - 用e替换这条最大边，得到的新生成树权值为MST权值 - 最大边权 + e的权值
// 3. 使用LCA和树上倍增算法快速查询任意两点间路径的最大边权
//
// 时间复杂度: O((n + m) log n)，其中n是顶点数，m是边数
// 空间复杂度: O(n log n)
// 是否为最优解: 是，这是解决该问题的标准方法

#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
using namespace std;

const int MAXN = 200005;
const int LOG = 20;

struct Edge {
    int u, v, w, id;
    bool operator<(const Edge& other) const {
        return w < other.w;
    }
};

// 并查集
int parent[MAXN], rank_[MAXN];

void initDSU(int n) {
    for (int i = 1; i <= n; i++) {
        parent[i] = i;
        rank_[i] = 0;
    }
}

int find(int x) {
    if (parent[x] != x) {
        parent[x] = find(parent[x]);
    }
    return parent[x];
}

bool unite(int x, int y) {
    x = find(x);
    y = find(y);
    if (x == y) return false;
    if (rank_[x] < rank_[y]) {
        parent[x] = y;
    } else if (rank_[x] > rank_[y]) {
        parent[y] = x;
    } else {
        parent[y] = x;
        rank_[x]++;
    }
    return true;
}

// LCA相关数组
vector<pair<int, int>> tree[MAXN];
int depth[MAXN];
int up[MAXN][LOG];
int maxEdge[MAXN][LOG];

void dfs(int u, int p, int w) {
    depth[u] = depth[p] + 1;
    up[u][0] = p;
    maxEdge[u][0] = w;
    
    for (int i = 1; i < LOG; i++) {
        up[u][i] = up[up[u][i-1]][i-1];
        maxEdge[u][i] = max(maxEdge[u][i-1], maxEdge[up[u][i-1]][i-1]);
    }
    
    for (auto& edge : tree[u]) {
        int v = edge.first;
        int weight = edge.second;
        if (v != p) {
            dfs(v, u, weight);
        }
    }
}

int queryMaxEdge(int u, int v) {
    if (depth[u] < depth[v]) swap(u, v);
    
    int maxW = 0;
    
    // 将u提升到与v同一深度
    for (int i = LOG - 1; i >= 0; i--) {
        if (depth[u] - (1 << i) >= depth[v]) {
            maxW = max(maxW, maxEdge[u][i]);
            u = up[u][i];
        }
    }
    
    if (u == v) return maxW;
    
    // 同时提升u和v直到它们的父节点相同
    for (int i = LOG - 1; i >= 0; i--) {
        if (up[u][i] != up[v][i]) {
            maxW = max(maxW, maxEdge[u][i]);
            maxW = max(maxW, maxEdge[v][i]);
            u = up[u][i];
            v = up[v][i];
        }
    }
    
    maxW = max(maxW, maxEdge[u][0]);
    maxW = max(maxW, maxEdge[v][0]);
    
    return maxW;
}

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n, m;
    cin >> n >> m;
    
    vector<Edge> edges(m);
    for (int i = 0; i < m; i++) {
        cin >> edges[i].u >> edges[i].v >> edges[i].w;
        edges[i].id = i;
    }
    
    // 按权重排序用于Kruskal
    vector<Edge> sortedEdges = edges;
    sort(sortedEdges.begin(), sortedEdges.end());
    
    initDSU(n);
    long long mstWeight = 0;
    vector<bool> inMST(m, false);
    
    // 构建最小生成树
    for (auto& e : sortedEdges) {
        if (unite(e.u, e.v)) {
            mstWeight += e.w;
            inMST[e.id] = true;
            tree[e.u].push_back({e.v, e.w});
            tree[e.v].push_back({e.u, e.w});
        }
    }
    
    // 构建LCA结构
    depth[0] = -1;
    dfs(1, 0, 0);
    
    // 处理每条边
    vector<long long> result(m);
    for (int i = 0; i < m; i++) {
        if (inMST[i]) {
            result[i] = mstWeight;
        } else {
            int maxW = queryMaxEdge(edges[i].u, edges[i].v);
            result[i] = mstWeight - maxW + edges[i].w;
        }
    }
    
    for (int i = 0; i < m; i++) {
        cout << result[i] << "\n";
    }
    
    return 0;
}
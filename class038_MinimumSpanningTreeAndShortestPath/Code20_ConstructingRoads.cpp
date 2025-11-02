// POJ 2421. Constructing Roads
// 题目链接: http://poj.org/problem?id=2421
// 
// 题目描述:
// 有N个村庄，已知所有村庄之间的距离。
// 有些村庄之间已经存在道路。
// 求连接所有村庄的最小成本。
//
// 解题思路:
// 标准的最小生成树问题，但部分边已经存在：
// 1. 将已存在的边的权重设为0
// 2. 使用Kruskal算法计算最小生成树
// 3. 已存在的边会被优先选择（权重为0）
//
// 时间复杂度: O(E log E)，其中E是边数
// 空间复杂度: O(V + E)
// 是否为最优解: 是，这是解决该问题的标准方法

#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

class UnionFind {
private:
    vector<int> parent;
    vector<int> rank;
    
public:
    UnionFind(int n) {
        parent.resize(n);
        rank.resize(n, 0);
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }
    
    int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }
    
    bool unite(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        if (rootX == rootY) return false;
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
        return true;
    }
};

struct Edge {
    int u, v, w;
    Edge(int u, int v, int w) : u(u), v(v), w(w) {}
    bool operator<(const Edge& other) const {
        return w < other.w;
    }
};

int constructingRoads(int n, vector<vector<int>>& dist, vector<pair<int, int>>& existingRoads) {
    vector<Edge> edges;
    
    // 构建所有可能的边
    for (int i = 0; i < n; i++) {
        for (int j = i + 1; j < n; j++) {
            edges.push_back(Edge(i, j, dist[i][j]));
        }
    }
    
    // 将已存在道路的权重设为0
    for (auto& road : existingRoads) {
        int u = road.first - 1; // 转换为0-based索引
        int v = road.second - 1;
        // 找到对应的边并设置权重为0
        for (auto& edge : edges) {
            if ((edge.u == u && edge.v == v) || (edge.u == v && edge.v == u)) {
                edge.w = 0;
                break;
            }
        }
    }
    
    sort(edges.begin(), edges.end());
    
    UnionFind uf(n);
    int totalCost = 0;
    int edgesUsed = 0;
    
    for (auto& edge : edges) {
        if (uf.unite(edge.u, edge.v)) {
            totalCost += edge.w;
            edgesUsed++;
            
            if (edgesUsed == n - 1) {
                break;
            }
        }
    }
    
    return totalCost;
}

int main() {
    int n;
    cin >> n;
    
    vector<vector<int>> dist(n, vector<int>(n));
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            cin >> dist[i][j];
        }
    }
    
    int q;
    cin >> q;
    vector<pair<int, int>> existingRoads(q);
    for (int i = 0; i < q; i++) {
        cin >> existingRoads[i].first >> existingRoads[i].second;
    }
    
    int result = constructingRoads(n, dist, existingRoads);
    cout << result << endl;
    
    return 0;
}
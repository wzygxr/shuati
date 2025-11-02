// POJ 3522. Slim Span
// 题目链接: http://poj.org/problem?id=3522
// 
// 题目描述:
// 给定一个无向图，定义生成树的"苗条度"为最大边权与最小边权的差值。
// 求所有生成树中苗条度的最小值。
//
// 解题思路:
// 1. 枚举最小边，然后使用Kruskal算法构建包含该边的最小生成树
// 2. 记录每次生成树的最大边权，计算苗条度
// 3. 取所有可能苗条度中的最小值
//
// 时间复杂度: O(E^2 * α(V))，其中E是边数，V是顶点数
// 空间复杂度: O(V + E)
// 是否为最优解: 是，这是解决该问题的标准方法

#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
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

int slimSpan(int n, vector<Edge>& edges) {
    sort(edges.begin(), edges.end());
    int minSlim = INT_MAX;
    int m = edges.size();
    
    // 枚举最小边
    for (int i = 0; i < m; i++) {
        UnionFind uf(n);
        int edgeCount = 0;
        int maxWeight = edges[i].w;
        int minWeight = edges[i].w;
        
        // 从第i条边开始构建生成树
        for (int j = i; j < m; j++) {
            if (uf.unite(edges[j].u, edges[j].v)) {
                edgeCount++;
                maxWeight = max(maxWeight, edges[j].w);
                
                if (edgeCount == n - 1) {
                    minSlim = min(minSlim, maxWeight - minWeight);
                    break;
                }
            }
        }
    }
    
    return minSlim == INT_MAX ? -1 : minSlim;
}

int main() {
    int n, m;
    while (cin >> n >> m && (n != 0 || m != 0)) {
        vector<Edge> edges;
        for (int i = 0; i < m; i++) {
            int u, v, w;
            cin >> u >> v >> w;
            edges.push_back(Edge(u - 1, v - 1, w)); // 转换为0-based索引
        }
        
        int result = slimSpan(n, edges);
        cout << result << endl;
    }
    
    return 0;
}
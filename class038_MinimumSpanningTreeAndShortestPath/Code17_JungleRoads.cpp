// POJ 1251. Jungle Roads
// 题目链接: http://poj.org/problem?id=1251
// 
// 题目描述:
// 热带岛屿上的村庄之间需要修建道路。每个村庄用大写字母表示。
// 输入给出每个村庄到其他村庄的道路修建成本。
// 求连接所有村庄的最小成本。
//
// 解题思路:
// 标准的最小生成树问题：
// 1. 将村庄看作图中的节点
// 2. 将道路修建成本看作边的权重
// 3. 使用Kruskal或Prim算法计算最小生成树
//
// 时间复杂度: O(E log E)，其中E是边数
// 空间复杂度: O(V + E)，其中V是顶点数
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
        
        if (rootX == rootY) {
            return false;
        }
        
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

int jungleRoads(int n, vector<Edge>& edges) {
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
    while (cin >> n && n != 0) {
        vector<Edge> edges;
        
        for (int i = 0; i < n - 1; i++) {
            char village;
            int k;
            cin >> village >> k;
            
            int u = village - 'A';
            
            for (int j = 0; j < k; j++) {
                char neighbor;
                int cost;
                cin >> neighbor >> cost;
                
                int v = neighbor - 'A';
                edges.push_back(Edge(u, v, cost));
            }
        }
        
        int result = jungleRoads(n, edges);
        cout << result << endl;
    }
    
    return 0;
}
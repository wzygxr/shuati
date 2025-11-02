// 洛谷P2330 [SCOI2005]繁忙的都市 - Kruskal算法实现
// 题目链接: https://www.luogu.com.cn/problem/P2330
// 
// 题目描述:
// 城市之间有许多道路，政府要修建一些道路，使得任何两个城市都可以互相到达，并且总长度最小。
// 但是，市政府希望知道在这个方案中，最大的道路长度是多少。
//
// 解题思路:
// 使用Kruskal算法构建最小生成树，在构建过程中记录使用的最大边权值
// 这实际上是最小生成树的一个性质：在保证总权值最小的情况下，最大的边权值也是最小的
//
// 时间复杂度: O(m * log m)，其中m是道路数
// 空间复杂度: O(n + m)，其中n是城市数
// 是否为最优解: 是，Kruskal算法是解决此类问题的标准方法

#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

// 并查集类
class UnionFind {
private:
    vector<int> parent;
    vector<int> rank;
public:
    UnionFind(int n) {
        parent.resize(n + 1);
        rank.resize(n + 1, 0);
        for (int i = 1; i <= n; i++) {
            parent[i] = i;
        }
    }
    
    int find(int x) {
        // 路径压缩优化
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }
    
    bool unite(int x, int y) {
        int fx = find(x);
        int fy = find(y);
        if (fx != fy) {
            // 按秩合并优化
            if (rank[fx] < rank[fy]) {
                parent[fx] = fy;
            } else {
                parent[fy] = fx;
                if (rank[fx] == rank[fy]) {
                    rank[fx]++;
                }
            }
            return true;
        }
        return false;
    }
};

// 边的结构体
struct Edge {
    int u, v, weight;
    Edge(int u_node = 0, int v_node = 0, int w = 0) : u(u_node), v(v_node), weight(w) {}
    
    // 排序比较函数
    bool operator<(const Edge& other) const {
        return weight < other.weight;
    }
};

int main() {
    int n, m;
    cin >> n >> m;
    
    vector<Edge> edges;
    for (int i = 0; i < m; i++) {
        int u, v, w;
        cin >> u >> v >> w;
        edges.emplace_back(u, v, w);
    }
    
    // 按边权从小到大排序
    sort(edges.begin(), edges.end());
    
    UnionFind uf(n);
    int max_edge = 0;
    int edge_count = 0;
    
    // 执行Kruskal算法
    for (const Edge& edge : edges) {
        if (uf.unite(edge.u, edge.v)) {
            max_edge = max(max_edge, edge.weight);
            edge_count++;
            // 最小生成树有n-1条边
            if (edge_count == n - 1) {
                break;
            }
        }
    }
    
    // 输出结果
    cout << edge_count << " " << max_edge << endl;
    
    return 0;
}
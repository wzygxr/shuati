// UVa 10369. Arctic Network
// 题目链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=15&page=show_problem&problem=1310
// 
// 题目描述:
// 北极的哨所之间需要建立通信网络。有两种通信方式：
// 1. 无线电通信：距离不超过D
// 2. 卫星通信：不受距离限制，但只有S个卫星频道
// 求最小的D，使得所有哨所都能通信。
//
// 解题思路:
// 与无线通讯网类似的问题：
// 1. 构建完全图，边权为哨所之间的距离
// 2. 使用Kruskal算法计算最小生成树
// 3. 由于有S个卫星频道，可以省去S-1条最长的边
// 4. 最小生成树中第P-S大的边权就是答案
//
// 时间复杂度: O(P^2 * log P)，其中P是哨所数量
// 空间复杂度: O(P^2)
// 是否为最优解: 是，这是解决该问题的标准方法

#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <iomanip>
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

double distance(int x1, int y1, int x2, int y2) {
    return sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
}

double arcticNetwork(int s, int p, vector<pair<int, int>>& outposts) {
    vector<pair<double, pair<int, int>>> edges;
    
    for (int i = 0; i < p; i++) {
        for (int j = i + 1; j < p; j++) {
            double dist = distance(outposts[i].first, outposts[i].second,
                                 outposts[j].first, outposts[j].second);
            edges.push_back({dist, {i, j}});
        }
    }
    
    sort(edges.begin(), edges.end());
    
    UnionFind uf(p);
    vector<double> mstEdges;
    
    for (auto& edge : edges) {
        double dist = edge.first;
        int u = edge.second.first;
        int v = edge.second.second;
        
        if (uf.unite(u, v)) {
            mstEdges.push_back(dist);
        }
    }
    
    // 有S个卫星频道，可以省去S-1条最长的边
    return mstEdges[p - s - 1];
}

int main() {
    int n;
    cin >> n;
    
    while (n--) {
        int s, p;
        cin >> s >> p;
        
        vector<pair<int, int>> outposts(p);
        for (int i = 0; i < p; i++) {
            cin >> outposts[i].first >> outposts[i].second;
        }
        
        double result = arcticNetwork(s, p, outposts);
        cout << fixed << setprecision(2) << result << endl;
    }
    
    return 0;
}
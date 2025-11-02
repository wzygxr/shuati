// UVa 10034. Freckles
// 题目链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=12&page=show_problem&problem=975
// 
// 题目描述:
// 平面上有n个点（雀斑），要求用墨水笔连接这些点，使得所有点都连通，并且总墨水长度最小。
// 输出最小的总长度。
//
// 解题思路:
// 标准的最小生成树问题：
// 1. 将每个点看作图中的一个节点
// 2. 计算所有点对之间的距离作为边的权重
// 3. 使用Kruskal或Prim算法计算最小生成树
// 4. 最小生成树的总权重就是答案
//
// 时间复杂度: O(N^2 * log N)，其中N是点的数量
// 空间复杂度: O(N^2)
// 是否为最优解: 是，这是解决该问题的标准方法

#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <iomanip>
using namespace std;

// 并查集数据结构
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

// 计算两点之间的欧几里得距离
double distance(double x1, double y1, double x2, double y2) {
    return sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
}

double freckles(int n, vector<pair<double, double>>& points) {
    // 构建所有边
    vector<pair<double, pair<int, int>>> edges;
    
    for (int i = 0; i < n; i++) {
        for (int j = i + 1; j < n; j++) {
            double dist = distance(points[i].first, points[i].second, 
                                 points[j].first, points[j].second);
            edges.push_back({dist, {i, j}});
        }
    }
    
    // 按距离排序
    sort(edges.begin(), edges.end());
    
    UnionFind uf(n);
    double totalLength = 0.0;
    int edgesUsed = 0;
    
    // 构建最小生成树
    for (auto& edge : edges) {
        double dist = edge.first;
        int u = edge.second.first;
        int v = edge.second.second;
        
        if (uf.unite(u, v)) {
            totalLength += dist;
            edgesUsed++;
            
            if (edgesUsed == n - 1) {
                break;
            }
        }
    }
    
    return totalLength;
}

int main() {
    int t;
    cin >> t;
    
    while (t--) {
        int n;
        cin >> n;
        
        vector<pair<double, double>> points(n);
        for (int i = 0; i < n; i++) {
            cin >> points[i].first >> points[i].second;
        }
        
        double result = freckles(n, points);
        cout << fixed << setprecision(2) << result << endl;
        
        // 输出空行分隔测试用例（UVa格式要求）
        if (t > 0) {
            cout << endl;
        }
    }
    
    return 0;
}

/*
测试用例示例:
输入:
1

3
1.0 1.0
2.0 2.0
2.0 4.0

输出:
3.41
*/
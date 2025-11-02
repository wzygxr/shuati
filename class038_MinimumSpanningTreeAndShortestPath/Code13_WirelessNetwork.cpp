// 洛谷P1991. 无线通讯网
// 题目链接: https://www.luogu.com.cn/problem/P1991
// 
// 题目描述:
// 国防部计划用无线网络连接若干个边防哨所。2种不同的通讯技术用来搭建无线网络：
// 每个边防哨所都要配备无线电收发器；有一些哨所还可以增配卫星电话。
// 任意两个配备了一条卫星电话线路的哨所（两边都拥有卫星电话）均可以通话，无论他们相距多远。
// 而只通过无线电收发器通话的哨所之间的距离不能超过D，这是受收发器的功率限制。
// 收发器的功率越高，通话距离D会更远，但同时价格也会更贵。
// 收发器需要统一购买和安装，所以全部哨所只能选择安装一种型号的收发器。
// 换句话说，每一对哨所之间的通话距离都是同一个D。
// 你的任务是确定收发器必须的最小通话距离D，使得每一对哨所之间至少有一条通话路径（直接的或者间接的）。
//
// 解题思路:
// 1. 将问题转化为最小生成树问题
// 2. 构建完全图，边权为哨所之间的距离
// 3. 使用Kruskal算法计算最小生成树
// 4. 由于有s个卫星电话，可以省去s-1条最长的边
// 5. 最小生成树中第n-s大的边权就是答案
//
// 时间复杂度: O(P^2 * log(P))，其中P是哨所数量
// 空间复杂度: O(P^2)
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

// 计算两点之间的距离
double distance(int x1, int y1, int x2, int y2) {
    return sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
}

double wirelessNetwork(int s, int p, vector<pair<int, int>>& posts) {
    // 构建所有边
    vector<pair<double, pair<int, int>>> edges;
    
    for (int i = 0; i < p; i++) {
        for (int j = i + 1; j < p; j++) {
            double dist = distance(posts[i].first, posts[i].second, 
                                 posts[j].first, posts[j].second);
            edges.push_back({dist, {i, j}});
        }
    }
    
    // 按距离排序
    sort(edges.begin(), edges.end());
    
    UnionFind uf(p);
    vector<double> mstEdges;
    
    // 构建最小生成树
    for (auto& edge : edges) {
        double dist = edge.first;
        int u = edge.second.first;
        int v = edge.second.second;
        
        if (uf.unite(u, v)) {
            mstEdges.push_back(dist);
        }
    }
    
    // 由于有s个卫星电话，可以省去s-1条最长的边
    // 剩下的最长边就是答案
    return mstEdges[p - s - 1];
}

int main() {
    int s, p;
    cin >> s >> p;
    
    vector<pair<int, int>> posts(p);
    for (int i = 0; i < p; i++) {
        cin >> posts[i].first >> posts[i].second;
    }
    
    double result = wirelessNetwork(s, p, posts);
    cout << fixed << setprecision(2) << result << endl;
    
    return 0;
}

/*
测试用例示例:
输入:
2 4
0 100
0 300
0 600
150 750

输出:
212.13
*/
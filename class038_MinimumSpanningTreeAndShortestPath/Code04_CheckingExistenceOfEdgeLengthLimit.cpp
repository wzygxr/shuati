// LeetCode 1170. Checking Existence of Edge Length Limited Paths
// 题目链接: https://leetcode.cn/problems/checking-existence-of-edge-length-limited-paths/
// 
// 题目描述:
// 给你一个n个节点的无向图，每个节点编号为0到n-1。同时给你一个二维数组edges，其中edges[i] = [u_i, v_i, w_i]，表示节点u_i和v_i之间有一条权值为w_i的无向边。
// 再给你一个查询数组queries，其中queries[j] = [p_j, q_j, limit_j]，表示查询节点p_j和q_j之间是否存在一条路径，路径上的每一条边的权值都严格小于limit_j。
// 对于每个查询，请你返回布尔值，表示是否存在满足条件的路径。
//
// 解题思路:
// 使用离线查询和并查集的方法：
// 1. 将所有边按权值从小到大排序
// 2. 将所有查询按limit从小到大排序，并记录原始索引
// 3. 对于每个查询，按limit从小到大处理，将权值小于当前limit的边加入并查集
// 4. 检查当前查询的两个节点是否连通
//
// 时间复杂度: O(E log E + Q log Q + α(V) * (E + Q))，其中E是边数，Q是查询数，V是顶点数，α是阿克曼函数的反函数
// 空间复杂度: O(V + Q)
// 是否为最优解: 是，离线查询+并查集是解决此类问题的最优方法之一

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
        parent.resize(n);
        rank.resize(n, 0);
        for (int i = 0; i < n; i++) {
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
    
    void unite(int x, int y) {
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
        }
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

// 查询的结构体
struct Query {
    int p, q, limit, index;
    Query(int p_node = 0, int q_node = 0, int lim = 0, int idx = 0) : p(p_node), q(q_node), limit(lim), index(idx) {}
    
    // 排序比较函数
    bool operator<(const Query& other) const {
        return limit < other.limit;
    }
};

vector<bool> distanceLimitedPathsExist(int n, vector<vector<int>>& edges, vector<vector<int>>& queries) {
    // 转换边的格式并排序
    vector<Edge> sortedEdges;
    for (const auto& edge : edges) {
        sortedEdges.emplace_back(edge[0], edge[1], edge[2]);
    }
    sort(sortedEdges.begin(), sortedEdges.end());
    
    // 转换查询的格式，记录原始索引并排序
    vector<Query> sortedQueries;
    for (int i = 0; i < queries.size(); i++) {
        const auto& query = queries[i];
        sortedQueries.emplace_back(query[0], query[1], query[2], i);
    }
    sort(sortedQueries.begin(), sortedQueries.end());
    
    // 初始化并查集和结果数组
    UnionFind uf(n);
    vector<bool> result(queries.size(), false);
    int edgePtr = 0;
    
    // 处理每个查询
    for (const auto& query : sortedQueries) {
        // 将所有权值小于当前查询limit的边加入并查集
        while (edgePtr < sortedEdges.size() && sortedEdges[edgePtr].weight < query.limit) {
            uf.unite(sortedEdges[edgePtr].u, sortedEdges[edgePtr].v);
            edgePtr++;
        }
        
        // 检查p和q是否连通
        if (uf.find(query.p) == uf.find(query.q)) {
            result[query.index] = true;
        }
    }
    
    return result;
}

// 测试函数
void test() {
    // 测试用例1
    int n1 = 3;
    vector<vector<int>> edges1 = {{0, 1, 2}, {1, 2, 4}, {2, 0, 8}, {1, 0, 16}};
    vector<vector<int>> queries1 = {{0, 1, 2}, {0, 2, 5}};
    vector<bool> result1 = distanceLimitedPathsExist(n1, edges1, queries1);
    cout << "Test 1: [";
    for (size_t i = 0; i < result1.size(); i++) {
        cout << (result1[i] ? "true" : "false");
        if (i < result1.size() - 1) cout << ", ";
    }
    cout << "]" << endl;  // 预期输出: [false, true]
    
    // 测试用例2
    int n2 = 5;
    vector<vector<int>> edges2 = {{0, 1, 10}, {1, 2, 5}, {2, 3, 9}, {3, 4, 13}};
    vector<vector<int>> queries2 = {{0, 4, 14}, {1, 4, 13}};
    vector<bool> result2 = distanceLimitedPathsExist(n2, edges2, queries2);
    cout << "Test 2: [";
    for (size_t i = 0; i < result2.size(); i++) {
        cout << (result2[i] ? "true" : "false");
        if (i < result2.size() - 1) cout << ", ";
    }
    cout << "]" << endl;  // 预期输出: [true, false]
}

int main() {
    test();
    return 0;
}
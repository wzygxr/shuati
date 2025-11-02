/**
 * Tarjan离线算法实现最近公共祖先(LCA)查询
 * 时间复杂度: O(n + q)，其中n是节点数，q是查询次数
 * 空间复杂度: O(n + q)
 */

#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

class TarjanOfflineLCA {
private:
    vector<vector<int>> graph; // 图的邻接表表示
    vector<vector<pair<int, int>>> queries; // 每个节点相关的查询，pair<v, index>
    vector<int> parent; // 并查集的父节点数组
    vector<bool> visited; // 记录节点是否被访问过
    vector<int> ancestor; // 存储每个节点的祖先
    vector<int> result; // 存储查询结果
    int n; // 节点数量
    int q; // 查询数量

    /**
     * 并查集的查找操作，带路径压缩
     * @param x 要查找的节点
     * @return x所在集合的根节点
     */
    int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    /**
     * 并查集的合并操作
     * @param x 第一个节点
     * @param y 第二个节点
     */
    void unite(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        if (rootX != rootY) {
            parent[rootY] = rootX;
        }
    }

    /**
     * Tarjan离线算法的主要实现
     * @param u 当前节点
     * @param parentU u的父节点
     */
    void tarjan(int u, int parentU) {
        visited[u] = true;
        ancestor[u] = u;

        // 遍历u的所有邻接点
        for (int v : graph[u]) {
            // 避免回到父节点
            if (v != parentU) {
                tarjan(v, u);
                unite(u, v);
                ancestor[find(u)] = u;
            }
        }

        // 处理与u相关的查询
        for (auto& query : queries[u]) {
            int v = query.first;
            int index = query.second;
            // 如果v已经被访问过，那么它们的LCA就是ancestor[find(v)]
            if (visited[v]) {
                result[index] = ancestor[find(v)];
            }
        }
    }

public:
    /**
     * 构造函数
     * @param n 节点数量
     * @param edges 边集合
     * @param queryList 查询列表
     */
    TarjanOfflineLCA(int n, const vector<pair<int, int>>& edges, const vector<pair<int, int>>& queryList) {
        this->n = n;
        this->q = queryList.size();

        // 初始化图的邻接表
        graph.resize(n);
        for (auto& edge : edges) {
            int u = edge.first;
            int v = edge.second;
            graph[u].push_back(v);
            graph[v].push_back(u);
        }

        // 初始化查询数组
        queries.resize(n);
        for (int i = 0; i < queryList.size(); i++) {
            int u = queryList[i].first;
            int v = queryList[i].second;
            queries[u].emplace_back(v, i);
            queries[v].emplace_back(u, i);
        }

        // 初始化并查集和其他数组
        parent.resize(n);
        visited.resize(n, false);
        ancestor.resize(n);
        result.resize(q);

        // 初始化并查集，每个节点的父节点是自己
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            ancestor[i] = i;
        }
    }

    /**
     * 执行查询并返回结果
     * @return 查询结果数组
     */
    vector<int> solve() {
        // 从根节点开始遍历（假设根节点是0）
        tarjan(0, -1);
        return result;
    }
};

int main() {
    // 测试用例
    int n = 6;
    vector<pair<int, int>> edges = {
        {0, 1},
        {0, 2},
        {1, 3},
        {1, 4},
        {2, 5}
    };

    vector<pair<int, int>> queries = {
        {3, 4},
        {3, 5},
        {4, 5}
    };

    TarjanOfflineLCA tarjan(n, edges, queries);
    vector<int> results = tarjan.solve();

    cout << "LCA查询结果：" << endl;
    for (int i = 0; i < results.size(); i++) {
        int u = queries[i].first;
        int v = queries[i].second;
        cout << "LCA(" << u << ", " << v << ") = " << results[i] << endl;
    }

    return 0;
}
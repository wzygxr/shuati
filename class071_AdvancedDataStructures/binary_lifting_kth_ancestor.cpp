/**
 * Binary Lifting算法实现k-th祖先查询
 * 时间复杂度:
 *   - 预处理: O(n log n)
 *   - 查询: O(log k)
 * 空间复杂度: O(n log n)
 */

#include <iostream>
#include <vector>
#include <cmath>
using namespace std;

class BinaryLiftingKthAncestor {
private:
    vector<vector<int>> graph; // 图的邻接表表示
    vector<vector<int>> up; // up[k][u]表示u的2^k级祖先
    vector<int> depth; // 每个节点的深度
    int log; // 最大层数，log2(n)的上界
    int n; // 节点数量
    int root; // 根节点

    /**
     * DFS遍历，填充up[0]和depth数组
     * @param u 当前节点
     * @param parent 父节点
     * @param currentDepth 当前深度
     */
    void dfs(int u, int parent, int currentDepth) {
        up[0][u] = parent;
        depth[u] = currentDepth;

        for (int v : graph[u]) {
            if (v != parent) {
                dfs(v, u, currentDepth + 1);
            }
        }
    }

public:
    /**
     * 构造函数
     * @param n 节点数量
     * @param edges 边集合
     * @param root 根节点
     */
    BinaryLiftingKthAncestor(int n, const vector<pair<int, int>>& edges, int root) {
        this->n = n;
        this->root = root;

        // 计算需要的最大层数
        this->log = static_cast<int>(log2(n)) + 2;
        if (n == 0) {
            this->log = 1;
        }

        // 初始化邻接表
        graph.resize(n);
        for (const auto& edge : edges) {
            int u = edge.first;
            int v = edge.second;
            graph[u].push_back(v);
            graph[v].push_back(u);
        }

        // 初始化up表和深度数组
        up.resize(log, vector<int>(n, -1));
        depth.resize(n, 0);

        // 预处理
        dfs(root, -1, 0);

        // 填充up表
        for (int k = 1; k < log; k++) {
            for (int u = 0; u < n; u++) {
                if (up[k-1][u] != -1) {
                    up[k][u] = up[k-1][up[k-1][u]];
                }
            }
        }
    }

    /**
     * 获取节点u的k级祖先
     * @param u 节点
     * @param k 祖先的级数
     * @return u的k级祖先，如果不存在则返回-1
     */
    int getKthAncestor(int u, int k) {
        // 如果k大于节点深度，返回-1
        if (k > depth[u]) {
            return -1;
        }

        // 二进制分解k
        for (int i = 0; i < log; i++) {
            if (k & (1 << i)) {
                u = up[i][u];
                if (u == -1) {
                    return -1;
                }
            }
        }

        return u;
    }

    /**
     * 计算两个节点的最近公共祖先(LCA)
     * @param u 第一个节点
     * @param v 第二个节点
     * @return u和v的LCA
     */
    int lca(int u, int v) {
        // 确保u的深度大于等于v的深度
        if (depth[u] < depth[v]) {
            swap(u, v);
        }

        // 将u提升到与v同一深度
        u = getKthAncestor(u, depth[u] - depth[v]);

        // 如果u和v相同，说明已经找到LCA
        if (u == v) {
            return u;
        }

        // 同时提升u和v
        for (int k = log - 1; k >= 0; k--) {
            if (up[k][u] != -1 && up[k][u] != up[k][v]) {
                u = up[k][u];
                v = up[k][v];
            }
        }

        // LCA是u和v的父节点
        return up[0][u];
    }

    /**
     * 获取节点深度
     * @param u 节点
     * @return 节点的深度
     */
    int getDepth(int u) {
        return depth[u];
    }
};

int main() {
    // 测试用例
    int n = 6;
    int root = 0;
    vector<pair<int, int>> edges = {
        {0, 1},
        {0, 2},
        {1, 3},
        {1, 4},
        {2, 5}
    };

    BinaryLiftingKthAncestor bl(n, edges, root);

    // 测试k-th祖先查询
    cout << "k-th祖先查询结果：" << endl;
    cout << "节点3的1级祖先: " << bl.getKthAncestor(3, 1) << endl; // 应该是1
    cout << "节点3的2级祖先: " << bl.getKthAncestor(3, 2) << endl; // 应该是0
    cout << "节点3的3级祖先: " << bl.getKthAncestor(3, 3) << endl; // 应该是-1

    // 测试LCA查询
    cout << "\nLCA查询结果：" << endl;
    cout << "LCA(3, 4) = " << bl.lca(3, 4) << endl; // 应该是1
    cout << "LCA(3, 5) = " << bl.lca(3, 5) << endl; // 应该是0
    cout << "LCA(4, 5) = " << bl.lca(4, 5) << endl; // 应该是0

    return 0;
}
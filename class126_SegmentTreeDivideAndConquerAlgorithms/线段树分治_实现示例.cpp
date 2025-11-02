// 线段树分治算法的C++实现
// 包含可撤销并查集、动态图连通性和二分图判定问题的实现

#include <iostream>
#include <vector>
#include <algorithm>
#include <stack>
using namespace std;

// 可撤销并查集类
// 注意：为了支持回滚操作，不使用路径压缩优化
class RollbackDSU {
private:
    vector<int> father;  // 父节点数组
    vector<int> rank;    // 秩数组（树高上界）
    stack<pair<int, int>> history_father;  // 父节点操作历史
    stack<pair<int, int>> history_rank;    // 秩操作历史
    int version;         // 当前版本号

public:
    // 构造函数
    // 参数：size - 节点数量
    // 时间复杂度：O(n)
    RollbackDSU(int size) {
        father.resize(size);
        rank.resize(size, 1);
        // 初始化，每个节点的父节点是自身
        for (int i = 0; i < size; ++i) {
            father[i] = i;
        }
        version = 0;
    }

    // 查找节点的根节点
    // 参数：x - 要查找的节点
    // 返回值：节点x的根节点
    // 时间复杂度：O(log n) - 由于没有路径压缩
    int find(int x) {
        while (x != father[x]) {
            x = father[x];
        }
        return x;
    }

    // 合并两个集合
    // 参数：x, y - 要合并的两个节点
    // 返回值：如果x和y原来不在同一个集合中则返回true，否则返回false
    // 时间复杂度：O(log n)
    bool unite(int x, int y) {
        int fx = find(x);
        int fy = find(y);

        if (fx == fy) {
            return false;  // 已经在同一个集合中
        }

        // 按秩合并：将秩较小的树合并到秩较大的树上
        if (rank[fx] < rank[fy]) {
            swap(fx, fy);
        }

        // 记录操作前的状态，用于回滚
        history_father.push({fy, father[fy]});
        history_rank.push({fx, rank[fx]});
        version++;

        // 执行合并操作
        father[fy] = fx;

        // 如果两棵树的秩相等，则合并后树的秩加1
        if (rank[fx] == rank[fy]) {
            rank[fx]++;
        }

        return true;
    }

    // 回滚到指定版本
    // 参数：target_version - 要回滚到的版本号
    // 时间复杂度：O(当前版本 - 目标版本)
    void rollback(int target_version) {
        while (version > target_version) {
            // 恢复父节点状态
            auto [fy, prev_father] = history_father.top();
            history_father.pop();
            father[fy] = prev_father;

            // 恢复秩状态
            auto [fx, prev_rank] = history_rank.top();
            history_rank.pop();
            rank[fx] = prev_rank;

            version--;
        }
    }

    // 获取当前版本号
    // 返回值：当前版本号
    // 时间复杂度：O(1)
    int getVersion() const {
        return version;
    }
};

// 线段树分治类
class SegmentTreeDivideConquer {
private:
    int max_time;                   // 最大时间范围
    vector<vector<pair<int, int>>> operations;  // 存储每个线段树节点上的操作

    // 线段树更新操作，将边(u,v)添加到覆盖区间[l,r]的节点中
    // 参数：
    //   node - 当前节点编号
    //   node_l - 当前节点对应的左边界
    //   node_r - 当前节点对应的右边界
    //   l, r - 边的存在时间区间
    //   u, v - 边的两个端点
    // 时间复杂度：O(log Q)，其中Q是最大时间范围
    void update(int node, int node_l, int node_r, int l, int r, int u, int v) {
        if (node_r < l || node_l > r) {
            return;  // 当前节点的区间与操作区间不相交
        }

        if (l <= node_l && node_r <= r) {
            // 当前节点的区间完全包含在操作区间内，直接添加操作
            operations[node].emplace_back(u, v);
            return;
        }

        // 递归处理左右子节点
        int mid = (node_l + node_r) / 2;
        update(2 * node, node_l, mid, l, r, u, v);
        update(2 * node + 1, mid + 1, node_r, l, r, u, v);
    }

public:
    // 构造函数
    // 参数：max_time - 最大时间范围
    // 时间复杂度：O(Q)
    SegmentTreeDivideConquer(int max_time) : max_time(max_time) {
        // 为线段树分配足够的空间，4倍大小通常足够
        operations.resize(4 * (max_time + 1));
    }

    // 添加边操作
    // 参数：
    //   l, r - 边的存在时间区间
    //   u, v - 边的两个端点
    // 时间复杂度：O(log Q)
    void addEdge(int l, int r, int u, int v) {
        update(1, 1, max_time, l, r, u, v);
    }

    // 获取线段树节点上的操作
    // 参数：node - 线段树节点编号
    // 返回值：该节点上的所有操作
    // 时间复杂度：O(1)
    const vector<pair<int, int>>& getOperations(int node) const {
        return operations[node];
    }

    // 获取最大时间范围
    // 返回值：最大时间范围
    // 时间复杂度：O(1)
    int getMaxTime() const {
        return max_time;
    }
};

// 动态图连通性问题解决方案
// 问题描述：处理多个时间点的边添加/删除操作，并回答连通性查询
// 时间复杂度：O(m log n log Q + q α(n))，其中m是边数，q是查询数，Q是时间范围
void solveDynamicConnectivity() {
    int n, m, q;
    cin >> n >> m >> q;

    // 存储所有边及其时间区间
    vector<tuple<int, int, int, int>> edges(m);
    int max_time = 0;
    for (int i = 0; i < m; ++i) {
        int u, v, l, r;
        cin >> u >> v >> l >> r;
        u--;  // 转换为0-based索引
        v--;
        edges[i] = {u, v, l, r};
        max_time = max(max_time, r);
    }

    // 存储所有查询
    vector<tuple<int, int, int, int>> queries(q);
    for (int i = 0; i < q; ++i) {
        int u, v, t;
        cin >> u >> v >> t;
        u--;
        v--;
        queries[i] = {u, v, t, i};
    }

    // 按时间排序查询
    sort(queries.begin(), queries.end(), [](const auto& a, const auto& b) {
        return get<2>(a) < get<2>(b);
    });

    // 初始化线段树分治结构
    SegmentTreeDivideConquer stdc(max_time);

    // 添加边到线段树分治结构中
    for (const auto& [u, v, l, r] : edges) {
        stdc.addEdge(l, r, u, v);
    }

    // 初始化可撤销并查集
    RollbackDSU dsu(n);

    // 结果数组，按查询顺序存储答案
    vector<bool> results(q, false);
    
    // 当前处理的查询索引
    int current_query = 0;

    // DFS函数，处理线段树分治
    function<void(int, int, int)> dfs = [&](int node, int node_l, int node_r) {
        // 记录当前版本，用于回滚
        int current_version = dsu.getVersion();

        // 处理当前节点的所有边
        for (const auto& [u, v] : stdc.getOperations(node)) {
            dsu.unite(u, v);
        }

        // 处理当前时间点的所有查询
        if (node_l == node_r) {
            // 处理所有时间为node_l的查询
            while (current_query < q && get<2>(queries[current_query]) == node_l) {
                int u = get<0>(queries[current_query]);
                int v = get<1>(queries[current_query]);
                int idx = get<3>(queries[current_query]);
                // 检查u和v是否连通
                results[idx] = (dsu.find(u) == dsu.find(v));
                current_query++;
            }
        } else {
            // 递归处理左右子节点
            int mid = (node_l + node_r) / 2;
            dfs(2 * node, node_l, mid);
            dfs(2 * node + 1, mid + 1, node_r);
        }

        // 回滚到进入当前节点前的状态
        dsu.rollback(current_version);
    };

    // 执行DFS，处理所有查询
    dfs(1, 1, max_time);

    // 输出结果
    for (bool res : results) {
        cout << (res ? "YES" : "NO") << endl;
    }
}

// 二分图判定问题解决方案
// 问题描述：在动态图中判断每个时间点图是否是二分图
// 时间复杂度：O(m log n log Q)
void solveBipartiteChecking() {
    int n, m;
    cin >> n >> m;

    // 存储所有边及其时间区间
    vector<tuple<int, int, int, int>> edges(m);
    int max_time = 0;
    for (int i = 0; i < m; ++i) {
        int u, v, l, r;
        cin >> u >> v >> l >> r;
        u--;  // 转换为0-based索引
        v--;
        edges[i] = {u, v, l, r};
        max_time = max(max_time, r);
    }

    // 初始化线段树分治结构
    SegmentTreeDivideConquer stdc(max_time);

    // 添加边到线段树分治结构中
    for (const auto& [u, v, l, r] : edges) {
        stdc.addEdge(l, r, u, v);
    }

    // 初始化扩展域可撤销并查集
    // 扩展域：每个节点u有两个表示，u表示与集合根节点同色，u+n表示与集合根节点异色
    RollbackDSU dsu(2 * n);

    // 结果数组，记录每个时间点是否是二分图
    vector<bool> is_bipartite(max_time + 2, true);
    bool global_conflict = false;

    // DFS函数，处理二分图检测
    function<void(int, int, int, bool)> dfs_bipartite = [&](int node, int node_l, int node_r, bool conflict_inherited) {
        // 如果从父节点继承了冲突，则当前区间内所有时间点都不是二分图
        if (conflict_inherited) {
            for (int t = node_l; t <= node_r; ++t) {
                if (1 <= t && t <= max_time) {
                    is_bipartite[t] = false;
                }
            }
            return;
        }

        // 记录当前版本，用于回滚
        int current_version = dsu.getVersion();

        // 标记当前区间是否发生冲突
        bool conflict_in_this_node = false;

        // 处理当前节点的所有边
        for (const auto& [u, v] : stdc.getOperations(node)) {
            // 检查添加这条边是否会导致矛盾
            // 如果u和v已经在同一个集合中，或者u+n和v+n在同一个集合中，则存在奇环
            if (dsu.find(u) == dsu.find(v) || dsu.find(u + n) == dsu.find(v + n)) {
                conflict_in_this_node = true;
                // 标记该区间内所有时间点都不是二分图
                for (int t = node_l; t <= node_r; ++t) {
                    if (1 <= t && t <= max_time) {
                        is_bipartite[t] = false;
                    }
                }
                break;  // 已经发现冲突，可以跳过继续添加边
            }

            // 正常添加边：u和v必须异色，u+n和v必须同色，u和v+n必须同色
            dsu.unite(u, v + n);
            dsu.unite(u + n, v);
        }

        // 如果当前节点没有冲突，且不是叶子节点，则继续递归
        if (!conflict_in_this_node && node_l < node_r) {
            int mid = (node_l + node_r) / 2;
            dfs_bipartite(2 * node, node_l, mid, false);
            dfs_bipartite(2 * node + 1, mid + 1, node_r, false);
        }

        // 回滚到进入当前节点前的状态
        dsu.rollback(current_version);
    };

    // 执行二分图检测的DFS
    dfs_bipartite(1, 1, max_time, false);

    // 输出每个时间点的结果
    for (int t = 1; t <= max_time; ++t) {
        cout << (is_bipartite[t] ? "Yes" : "No") << endl;
    }
}

// 主函数
int main() {
    // 这里可以根据需要调用不同的解决方案函数
    // 例如：
    // solveDynamicConnectivity();
    // solveBipartiteChecking();
    
    return 0;
}
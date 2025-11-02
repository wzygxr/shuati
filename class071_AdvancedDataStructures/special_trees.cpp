#include <iostream>
#include <vector>
#include <stack>
#include <algorithm>
#include <climits>
#include <cstring>
using namespace std;

/**
 * 特殊树结构 C++ 实现
 * 包含：
 * 1. 支配树 (Dominator Tree) - 使用Lengauer-Tarjan算法
 * 2. 虚树 (Virtual Tree)
 * 
 * 时间复杂度：
 * - 支配树构建：O(E log V)
 * - 虚树构建：O(M log N)，其中M是关键点数量
 * 
 * 设计要点：
 * 1. 支配树用于表示节点间的支配关系
 * 2. 虚树用于压缩树结构，只保留关键点及其LCA
 * 3. 工程化考量：异常处理、边界检查
 * 
 * 典型应用场景：
 * - 程序分析中的控制流分析
 * - 网络流中的瓶颈分析
 * - 大规模树上的路径问题
 */

// 支配树实现（Lengauer-Tarjan算法）
class DominatorTree {
private:
    int n;                      // 节点数量
    int time_stamp;             // 时间戳
    vector<vector<int>> g;      // 原图
    vector<vector<int>> rg;     // 反向图
    vector<vector<int>> bucket; // 用于Lengauer-Tarjan算法的桶
    vector<int> semi;           // 半支配点
    vector<int> idom;           // 直接支配点
    vector<int> vertex;         // time -> 节点
    vector<int> label;          // 并查集的标签
    vector<int> dfn;            // 节点 -> time
    vector<int> parent;         // DFS树的父节点
    vector<vector<int>> dom_tree; // 支配树
    
    // 查找带路径压缩的最小值（按semi序）
    int find(int x) {
        if (label[x] != x) {
            int fx = find(label[x]);
            if (dfn[semi[fx]] < dfn[semi[label[x]]]) {
                label[x] = fx;
            }
        }
        return label[x];
    }
    
    // 深度优先搜索
    void dfs(int u) {
        dfn[u] = ++time_stamp;
        vertex[time_stamp] = u;
        for (int v : g[u]) {
            if (!dfn[v]) {
                parent[v] = u;
                dfs(v);
            }
        }
    }
    
public:
    /**
     * 构造函数
     * @param _n 节点数量（节点编号从0开始）
     */
    DominatorTree(int _n) : n(_n) {
        g.resize(n);
        rg.resize(n);
        bucket.resize(n);
        semi.resize(n);
        idom.resize(n);
        vertex.resize(n + 1); // time从1开始
        label.resize(n);
        dfn.resize(n, 0);
        parent.resize(n, -1);
        dom_tree.resize(n);
    }
    
    /**
     * 添加边
     * @param u 起点
     * @param v 终点
     */
    void add_edge(int u, int v) {
        if (u < 0 || u >= n || v < 0 || v >= n) {
            throw out_of_range("Node index out of range");
        }
        g[u].push_back(v);
        rg[v].push_back(u);
    }
    
    /**
     * 构建支配树
     * @param root 根节点
     */
    void build(int root) {
        if (root < 0 || root >= n) {
            throw out_of_range("Root node index out of range");
        }
        
        // 初始化
        time_stamp = 0;
        fill(dfn.begin(), dfn.end(), 0);
        fill(idom.begin(), idom.end(), -1);
        
        // DFS建立时间戳
        dfs(root);
        
        // 初始化semi和label
        for (int i = 1; i <= time_stamp; i++) {
            int u = vertex[i];
            semi[u] = u;
            label[u] = u;
        }
        
        // 按时间戳逆序处理节点
        for (int i = time_stamp; i >= 2; i--) {
            int u = vertex[i];
            
            // 计算semi[u]
            for (int v : rg[u]) {
                if (!dfn[v]) continue; // v没有被访问到
                
                if (dfn[v] < dfn[u]) {
                    if (dfn[semi[u]] > dfn[v]) {
                        semi[u] = v;
                    }
                } else {
                    find(v);
                    if (dfn[semi[u]] > dfn[semi[label[v]]]) {
                        semi[u] = semi[label[v]];
                    }
                }
            }
            
            bucket[semi[u]].push_back(u);
            
            // 处理bucket[parent[u]]
            for (int v : bucket[parent[u]]) {
                find(v);
                if (semi[label[v]] == semi[v]) {
                    idom[v] = semi[v];
                } else {
                    idom[v] = label[v];
                }
            }
            bucket[parent[u]].clear();
            
            label[u] = parent[u];
        }
        
        // 处理剩下的节点
        for (int i = 2; i <= time_stamp; i++) {
            int u = vertex[i];
            if (idom[u] != semi[u]) {
                idom[u] = idom[idom[u]];
            }
        }
        
        // 构建支配树
        for (int i = 0; i < n; i++) {
            if (i == root) continue;
            if (idom[i] != -1) {
                dom_tree[idom[i]].push_back(i);
            }
        }
    }
    
    /**
     * 判断u是否支配v
     * @param u 支配点
     * @param v 被支配点
     * @return 是否支配
     */
    bool is_dominating(int u, int v) {
        if (u == v) return true;
        if (idom[v] == -1) return false;
        
        // 沿着支配树向上查找
        while (v != -1 && v != u) {
            v = idom[v];
        }
        return v == u;
    }
    
    /**
     * 获取直接支配点
     * @param v 节点
     * @return 直接支配点
     */
    int get_idom(int v) {
        if (v < 0 || v >= n) {
            throw out_of_range("Node index out of range");
        }
        return idom[v];
    }
    
    /**
     * 打印支配树（用于调试）
     */
    void print_dom_tree() {
        for (int u = 0; u < n; u++) {
            if (dom_tree[u].empty()) continue;
            cout << "Node " << u << " -> ";
            for (int v : dom_tree[u]) {
                cout << v << " ";
            }
            cout << endl;
        }
    }
};

// 虚树实现
class VirtualTree {
private:
    int n;                      // 原树节点数量
    vector<vector<int>> g;      // 原树
    vector<int> depth;          // 深度
    vector<vector<int>> up;     // 倍增表，用于LCA查询
    vector<int> dfn;            // 时间戳
    int time_stamp;             // 时间戳计数器
    
    // 深度优先搜索，初始化LCA所需信息
    void dfs(int u, int parent_node) {
        dfn[u] = ++time_stamp;
        up[0][u] = parent_node;
        
        // 初始化倍增表
        for (int k = 1; k < up.size(); k++) {
            up[k][u] = up[k-1][up[k-1][u]];
        }
        
        // 递归处理子节点
        for (int v : g[u]) {
            if (v != parent_node) {
                depth[v] = depth[u] + 1;
                dfs(v, u);
            }
        }
    }
    
    // 计算LCA
    int lca(int u, int v) {
        if (depth[u] < depth[v]) {
            swap(u, v);
        }
        
        // 将u提升到与v同一深度
        for (int k = up.size() - 1; k >= 0; k--) {
            if (depth[u] - (1 << k) >= depth[v]) {
                u = up[k][u];
            }
        }
        
        if (u == v) return u;
        
        // 同时提升u和v
        for (int k = up.size() - 1; k >= 0; k--) {
            if (up[k][u] != up[k][v]) {
                u = up[k][u];
                v = up[k][v];
            }
        }
        
        return up[0][u];
    }
    
public:
    /**
     * 构造函数
     * @param _n 原树节点数量
     */
    VirtualTree(int _n) : n(_n) {
        g.resize(n);
        depth.resize(n, 0);
        
        // 计算log2(n)的上界
        int log_n = 1;
        while ((1 << log_n) <= n) {
            log_n++;
        }
        up.resize(log_n, vector<int>(n, 0));
        
        dfn.resize(n, 0);
        time_stamp = 0;
    }
    
    /**
     * 添加原树的边
     * @param u 节点u
     * @param v 节点v
     */
    void add_edge(int u, int v) {
        if (u < 0 || u >= n || v < 0 || v >= n) {
            throw out_of_range("Node index out of range");
        }
        g[u].push_back(v);
        g[v].push_back(u);
    }
    
    /**
     * 初始化LCA结构
     * @param root 原树的根节点
     */
    void build_lca(int root = 0) {
        if (root < 0 || root >= n) {
            throw out_of_range("Root node index out of range");
        }
        
        time_stamp = 0;
        depth[root] = 0;
        dfs(root, root);
    }
    
    /**
     * 构建虚树
     * @param key_nodes 关键点列表
     * @param virtual_g 输出参数，虚树的邻接表
     * @return 虚树的根节点
     */
    int build_virtual_tree(vector<int>& key_nodes, vector<vector<int>>& virtual_g) {
        if (key_nodes.empty()) {
            return -1;
        }
        
        // 按时间戳排序关键点
        sort(key_nodes.begin(), key_nodes.end(), [this](int a, int b) {
            return dfn[a] < dfn[b];
        });
        
        // 去重
        auto last = unique(key_nodes.begin(), key_nodes.end());
        key_nodes.erase(last, key_nodes.end());
        
        // 计算关键点之间的LCA并添加到关键点列表
        int m = key_nodes.size();
        for (int i = 0; i < m - 1; i++) {
            key_nodes.push_back(lca(key_nodes[i], key_nodes[i+1]));
        }
        
        // 再次排序和去重
        sort(key_nodes.begin(), key_nodes.end(), [this](int a, int b) {
            return dfn[a] < dfn[b];
        });
        last = unique(key_nodes.begin(), key_nodes.end());
        key_nodes.erase(last, key_nodes.end());
        
        // 初始化虚树的邻接表
        virtual_g.assign(n, vector<int>());
        
        // 用栈构建虚树
        stack<int> stk;
        stk.push(key_nodes[0]);
        
        for (size_t i = 1; i < key_nodes.size(); i++) {
            int u = key_nodes[i];
            int l = lca(u, stk.top());
            
            // 将栈中深度大于l的节点弹出，并建立边
            while (stk.size() > 1 && depth[stk.top()] > depth[l]) {
                int v = stk.top();
                stk.pop();
                if (depth[stk.top()] > depth[l]) {
                    virtual_g[stk.top()].push_back(v);
                    virtual_g[v].push_back(stk.top());
                } else {
                    virtual_g[l].push_back(v);
                    virtual_g[v].push_back(l);
                }
            }
            
            if (stk.top() != l) {
                stk.push(l);
            }
            stk.push(u);
        }
        
        // 处理栈中剩余的节点
        while (stk.size() > 1) {
            int u = stk.top();
            stk.pop();
            virtual_g[stk.top()].push_back(u);
            virtual_g[u].push_back(stk.top());
        }
        
        return stk.top(); // 虚树的根节点
    }
    
    /**
     * 获取节点的深度
     */
    int get_depth(int u) {
        if (u < 0 || u >= n) {
            throw out_of_range("Node index out of range");
        }
        return depth[u];
    }
    
    /**
     * 获取节点的时间戳
     */
    int get_dfn(int u) {
        if (u < 0 || u >= n) {
            throw out_of_range("Node index out of range");
        }
        return dfn[u];
    }
};

// 测试函数
int main() {
    cout << "===== 测试支配树 =====" << endl;
    // 构建一个简单的有向图
    DominatorTree dt(7);
    dt.add_edge(0, 1);
    dt.add_edge(0, 2);
    dt.add_edge(1, 3);
    dt.add_edge(2, 3);
    dt.add_edge(3, 4);
    dt.add_edge(3, 5);
    dt.add_edge(4, 6);
    dt.add_edge(5, 6);
    
    dt.build(0);
    cout << "支配树结构:" << endl;
    dt.print_dom_tree();
    
    cout << "节点0是否支配节点6: " << (dt.is_dominating(0, 6) ? "是" : "否") << endl;
    cout << "节点3是否支配节点6: " << (dt.is_dominating(3, 6) ? "是" : "否") << endl;
    cout << "节点1是否支配节点5: " << (dt.is_dominating(1, 5) ? "是" : "否") << endl;
    
    cout << "\n===== 测试虚树 =====" << endl;
    // 构建一个简单的无向树
    VirtualTree vt(7);
    vt.add_edge(0, 1);
    vt.add_edge(1, 2);
    vt.add_edge(1, 3);
    vt.add_edge(3, 4);
    vt.add_edge(3, 5);
    vt.add_edge(5, 6);
    
    vt.build_lca(0);
    
    // 关键点集合
    vector<int> key_nodes = {0, 2, 4, 6};
    vector<vector<int>> virtual_g;
    int root = vt.build_virtual_tree(key_nodes, virtual_g);
    
    cout << "虚树根节点: " << root << endl;
    cout << "虚树结构:" << endl;
    for (int u = 0; u < 7; u++) {
        if (!virtual_g[u].empty()) {
            cout << "Node " << u << " -> ";
            for (int v : virtual_g[u]) {
                cout << v << " ";
            }
            cout << endl;
        }
    }
    
    return 0;
}
#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

/**
 * 支配树算法实现
 * 
 * 支配树是一种用于分析有向图中必经点的数据结构。
 * 在有向图中，如果从起点s到终点t的所有路径都必须经过某个顶点u，则称u支配t，记为u dom t。
 * 支配树将这种支配关系组织成一棵树结构，其中每个节点的父节点是其最近的支配点（即直接支配点）。
 * 
 * 时间复杂度：O(n log n)，其中n是节点数
 * 空间复杂度：O(n)，用于存储图和辅助数组
 */

class DominatorTree {
private:
    int n;  // 节点数
    int start;  // 起点
    vector<vector<int>> graph;  // 原图
    vector<vector<int>> rev_graph;  // 反图
    int size;  // 访问的节点数
    vector<int> dfn;  // 发现时间
    vector<int> idx;  // 时间戳对应的节点
    vector<int> parent;  // DFS树中的父节点
    vector<int> semi;  // 半支配点
    vector<int> idom;  // 直接支配点
    vector<int> ancestor;  // 并查集中的祖先
    vector<int> best;  // 维护半支配点最小的节点
    vector<vector<int>> out;  // 支配树
    
    /**
     * 深度优先搜索，初始化相关信息
     * @param u 当前节点
     */
    void dfs(int u) {
        size++;
        dfn[u] = size;
        idx[size] = u;
        semi[size] = size;
        best[size] = size;
        ancestor[size] = 0;
        
        for (int v : graph[u]) {
            if (!dfn[v]) {
                parent[dfn[v]] = dfn[u];
                dfs(v);
            }
        }
    }
    
    /**
     * 并查集查询，路径压缩并维护best信息
     * @param u 当前节点
     * @return 根节点
     */
    int find(int u) {
        if (ancestor[u] == 0) {
            return u;
        }
        
        int root = find(ancestor[u]);
        if (semi[best[ancestor[u]]] < semi[best[u]]) {
            best[u] = best[ancestor[u]];
        }
        
        ancestor[u] = root;
        return root;
    }
    
    /**
     * 并查集合并操作
     * @param u 节点u
     * @param v 节点v
     */
    void unite(int u, int v) {
        ancestor[v] = u;
    }
    
public:
    /**
     * 构造函数
     * @param n 节点数
     * @param start 起点
     */
    DominatorTree(int n, int start) : n(n), start(start) {
        graph.resize(n + 1);
        rev_graph.resize(n + 1);
        dfn.resize(n + 1, 0);
        idx.resize(n + 2, 0);  // 时间戳从1开始
        parent.resize(n + 2, 0);
        semi.resize(n + 2, 0);
        idom.resize(n + 2, 0);
        ancestor.resize(n + 2, 0);
        best.resize(n + 2, 0);
        out.resize(n + 1);
        size = 0;
    }
    
    /**
     * 添加有向边u->v
     * @param u 起点
     * @param v 终点
     */
    void addEdge(int u, int v) {
        graph[u].push_back(v);
        rev_graph[v].push_back(u);
    }
    
    /**
     * 构建支配树
     * @return 支配树的邻接表表示
     */
    vector<vector<int>> build() {
        // 第一步：DFS初始化
        dfs(start);
        
        // 第二步：按照发现时间逆序处理节点
        for (int i = size; i > 1; --i) {
            // 计算半支配点
            int u = idx[i];
            for (int v : rev_graph[u]) {
                if (!dfn[v]) {
                    continue;
                }
                
                find(dfn[v]);
                if (semi[best[dfn[v]]] < semi[i]) {
                    semi[i] = semi[best[dfn[v]]];
                }
            }
            
            // 合并到父节点所在的集合
            unite(parent[i], i);
        }
        
        // 第三步：计算直接支配点
        for (int i = 2; i <= size; ++i) {
            if (semi[i] == semi[parent[i]]) {
                idom[i] = semi[i];
            } else {
                idom[i] = idom[parent[i]];
            }
        }
        
        // 第四步：构建支配树
        for (int i = 2; i <= size; ++i) {
            int u = idx[i];
            out[idx[idom[i]]].push_back(u);
        }
        
        return out;
    }
    
    /**
     * 获取所有支配v的节点
     * @param v 目标节点
     * @return 支配v的所有节点列表
     */
    vector<int> getDominators(int v) {
        vector<int> dominators;
        if (!dfn[v]) {
            return dominators;  // v不可达
        }
        
        while (v != start) {
            dominators.push_back(v);
            v = idx[idom[dfn[v]]];
        }
        dominators.push_back(start);
        reverse(dominators.begin(), dominators.end());
        return dominators;
    }
    
    /**
     * 判断u是否支配v
     * @param u 可能的支配点
     * @param v 被支配点
     * @return u是否支配v
     */
    bool isDominator(int u, int v) {
        if (!dfn[v]) {
            return false;  // v不可达
        }
        
        int current = v;
        while (current != start) {
            if (current == u) {
                return true;
            }
            current = idx[idom[dfn[current]]];
        }
        return u == start;
    }
    
    /**
     * 获取v的直接支配点
     * @param v 目标节点
     * @return v的直接支配点，如果不存在则返回-1
     */
    int getDirectDominator(int v) {
        if (!dfn[v] || v == start) {
            return -1;  // 不可达或为起点
        }
        return idx[idom[dfn[v]]];
    }
};

// 测试函数
int main() {
    // 示例：创建一个有向图并构建支配树
    DominatorTree dt(6, 1);
    dt.addEdge(1, 2);
    dt.addEdge(1, 3);
    dt.addEdge(2, 4);
    dt.addEdge(3, 4);
    dt.addEdge(4, 5);
    dt.addEdge(4, 6);
    dt.addEdge(5, 4);  // 形成环
    
    vector<vector<int>> dominatorTree = dt.build();
    cout << "支配树构建完成" << endl;
    
    // 输出支配树的邻接表
    for (int i = 1; i <= 6; i++) {
        cout << "节点 " << i << " 的子节点: ";
        for (int child : dominatorTree[i]) {
            cout << child << " ";
        }
        cout << endl;
    }
    
    // 测试支配关系
    cout << "节点1是否支配节点4: " << (dt.isDominator(1, 4) ? "是" : "否") << endl;
    cout << "节点2是否支配节点5: " << (dt.isDominator(2, 5) ? "是" : "否") << endl;
    cout << "节点4的直接支配点: " << dt.getDirectDominator(4) << endl;
    
    return 0;
}
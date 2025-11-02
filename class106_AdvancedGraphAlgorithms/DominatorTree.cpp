#include <iostream>
#include <vector>
#include <algorithm>
#include <queue>
#include <cstring>
#include <climits>
#include <functional>

/**
 * 支配树(Dominator Tree)实现 - C++版本
 * 
 * 支配树是图论中的一个重要概念，主要用于程序分析和编译器优化等领域。
 * 在有向图中，对于指定的源点s，如果从s到达节点w的所有路径都必须经过节点u，
 * 则称节点u支配节点w。
 * 
 * 本实现基于Lengauer-Tarjan算法，时间复杂度为O((V+E)log(V+E))
 * 
 * 应用场景：
 * 1. 编译器优化：控制流图分析，死代码消除，循环优化
 * 2. 程序分析：数据流分析，可达性分析
 * 3. 图论问题：关键节点识别，路径分析
 */

class DominatorTree {
private:
    int n;                      // 节点数量
    int root;                   // 根节点
    std::vector<std::vector<int>> graph;        // 原图邻接表
    std::vector<std::vector<int>> reverseGraph; // 反向图邻接表
    std::vector<int> dfn;       // DFS序
    std::vector<int> id;        // DFS序到节点的映射
    std::vector<int> fa;        // DFS树中的父节点
    std::vector<int> semi;      // 半支配点
    std::vector<int> idom;      // 立即支配点
    std::vector<int> best;      // 并查集优化用
    int dfsClock;               // DFS时钟
    std::vector<std::vector<int>> bucket;       // bucket[v]存储semi[v]相同的节点
    std::vector<std::vector<int>> tree;         // 支配树
    
public:
    /**
     * 构造函数
     * @param n 节点数量
     * @param root 根节点
     */
    DominatorTree(int n, int root) : n(n), root(root), dfsClock(0) {
        // 初始化邻接表
        graph.resize(n);
        reverseGraph.resize(n);
        bucket.resize(n);
        tree.resize(n);
        
        // 初始化数组
        dfn.resize(n, -1);
        id.resize(n);
        fa.resize(n);
        semi.resize(n, -1);
        idom.resize(n, -1);
        best.resize(n);
        
        // 初始化best数组
        for (int i = 0; i < n; i++) {
            best[i] = i;
        }
    }
    
    /**
     * 添加有向边
     * @param u 起点
     * @param v 终点
     */
    void addEdge(int u, int v) {
        graph[u].push_back(v);
        reverseGraph[v].push_back(u);
    }
    
    /**
     * DFS遍历，构建DFS树
     * @param u 当前节点
     */
    void dfs(int u) {
        dfn[u] = dfsClock;
        id[dfsClock] = u;
        dfsClock++;
        
        for (int v : graph[u]) {
            if (dfn[v] == -1) {
                fa[v] = u;
                dfs(v);
            }
        }
    }
    
    /**
     * 并查集查找操作（带路径压缩）
     * @param x 节点
     * @return 根节点
     */
    int find(int x) {
        if (x == fa[x]) {
            return x;
        }
        
        int root = find(fa[x]);
        
        // 路径压缩优化
        if (semi[best[fa[x]]] < semi[best[x]]) {
            best[x] = best[fa[x]];
        }
        
        return fa[x] = root;
    }
    
    /**
     * 构建支配树
     */
    void build() {
        // 1. DFS遍历，构建DFS树
        dfs(root);
        
        // 2. 从后向前处理每个节点
        for (int i = dfsClock - 1; i >= 0; i--) {
            int u = id[i];
            
            // 计算半支配点
            for (int v : reverseGraph[u]) {
                if (dfn[v] == -1) continue; // 节点v不在DFS树中
                
                if (dfn[v] < dfn[u]) {
                    // v是u的祖先
                    semi[u] = std::min(semi[u], dfn[v]);
                } else {
                    // v是u的后代，通过并查集找到v的祖先
                    find(v);
                    semi[u] = std::min(semi[u], semi[best[v]]);
                }
            }
            
            if (i > 0) {
                bucket[id[semi[u]]].push_back(u);
                
                // 处理bucket中的节点
                int w = fa[u];
                for (int v : bucket[w]) {
                    find(v);
                    if (semi[best[v]] == semi[v]) {
                        idom[v] = w;
                    } else {
                        idom[v] = best[v];
                    }
                }
                
                bucket[w].clear();
            }
        }
        
        // 3. 确定立即支配点
        for (int i = 1; i < dfsClock; i++) {
            int u = id[i];
            if (idom[u] != id[semi[u]]) {
                idom[u] = idom[idom[u]];
            }
        }
        
        // 4. 构建支配树
        for (int i = 1; i < dfsClock; i++) {
            int u = id[i];
            tree[idom[u]].push_back(u);
        }
    }
    
    /**
     * 获取节点u的支配节点
     * @param u 节点
     * @return 支配节点列表
     */
    std::vector<int> getDominatedNodes(int u) {
        std::vector<int> result;
        if (dfn[u] == -1) return result; // 节点不存在
        
        std::queue<int> queue;
        queue.push(u);
        
        while (!queue.empty()) {
            int v = queue.front();
            queue.pop();
            result.push_back(v);
            
            for (int w : tree[v]) {
                queue.push(w);
            }
        }
        
        return result;
    }
    
    /**
     * 检查节点u是否支配节点v
     * @param u 可能的支配节点
     * @param v 被支配节点
     * @return 是否支配
     */
    bool dominates(int u, int v) {
        if (dfn[u] == -1 || dfn[v] == -1) return false;
        
        // 从v向上追溯到根节点，检查是否经过u
        int current = v;
        while (current != root && current != -1) {
            if (current == u) return true;
            current = idom[current];
        }
        
        return current == u;
    }
    
    /**
     * 获取立即支配点
     * @param u 节点
     * @return 立即支配点，如果不存在返回-1
     */
    int getImmediateDominator(int u) {
        if (dfn[u] == -1 || u == root) return -1;
        return idom[u];
    }
    
    /**
     * 获取支配树
     * @return 支配树的邻接表表示
     */
    const std::vector<std::vector<int>>& getDominatorTree() const {
        return tree;
    }
    
    /**
     * 打印支配树结构
     */
    void printDominatorTree() {
        std::cout << "支配树结构:" << std::endl;
        for (int i = 0; i < n; i++) {
            if (!tree[i].empty()) {
                std::cout << "节点 " << i << " 支配: ";
                for (int child : tree[i]) {
                    std::cout << child << " ";
                }
                std::cout << std::endl;
            }
        }
    }
};

/**
 * 测试函数
 */
int main() {
    std::cout << "=== 支配树测试 ===" << std::endl;
    
    // 创建测试图
    // 0 -> 1 -> 2 -> 4
    //  \-> 3 --^
    DominatorTree dt(5, 0);
    dt.addEdge(0, 1);
    dt.addEdge(0, 3);
    dt.addEdge(1, 2);
    dt.addEdge(3, 2);
    dt.addEdge(2, 4);
    
    // 构建支配树
    dt.build();
    
    // 打印结果
    dt.printDominatorTree();
    
    // 测试支配关系
    std::cout << "\n支配关系测试:" << std::endl;
    std::cout << "节点0是否支配节点4: " << (dt.dominates(0, 4) ? "是" : "否") << std::endl;
    std::cout << "节点1是否支配节点4: " << (dt.dominates(1, 4) ? "是" : "否") << std::endl;
    std::cout << "节点2是否支配节点4: " << (dt.dominates(2, 4) ? "是" : "否") << std::endl;
    
    // 测试立即支配点
    std::cout << "\n立即支配点:" << std::endl;
    for (int i = 1; i < 5; i++) {
        int idom = dt.getImmediateDominator(i);
        std::cout << "节点" << i << "的立即支配点: " << (idom == -1 ? -1 : idom) << std::endl;
    }
    
    // 测试被支配节点
    std::cout << "\n被支配节点:" << std::endl;
    for (int i = 0; i < 5; i++) {
        std::vector<int> dominated = dt.getDominatedNodes(i);
        std::cout << "节点" << i << "支配的节点: ";
        for (int node : dominated) {
            std::cout << node << " ";
        }
        std::cout << std::endl;
    }
    
    return 0;
}
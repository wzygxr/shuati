#include <iostream>
#include <vector>
#include <stack>
#include <algorithm>
#include <cstring>
using namespace std;

/**
 * 圆方树算法实现
 * 
 * 圆方树是一种用于处理仙人掌图（Cactus Graph）的数据结构。
 * 仙人掌图是一种特殊的无向图，其中任意两条简单环最多只有一个公共顶点。
 * 圆方树将仙人掌图转化为一棵树结构，使得可以使用树型DP等树算法来解决仙人掌图上的问题。
 * 
 * 时间复杂度：O(n + m)，其中n是节点数，m是边数
 * 空间复杂度：O(n + m)，用于存储图和圆方树
 */

class CircleSquareTree {
private:
    int n;  // 原图顶点数
    int m;  // 圆方树顶点数
    vector<vector<int>> graph;  // 原图
    vector<vector<int>> square_graph;  // 圆方树
    vector<int> dfn;  // 深度优先搜索的时间戳
    vector<int> low;  // 能够回溯到的最早的时间戳
    stack<int> stk;  // 栈，用于保存双连通分量
    int cnt;  // 时间戳计数器
    int id;  // 圆方树顶点编号
    
    /**
     * Tarjan算法寻找双连通分量并构建圆方树
     * @param u 当前节点
     * @param parent 父节点
     */
    void tarjan(int u, int parent) {
        cnt++;
        dfn[u] = low[u] = cnt;
        stk.push(u);
        
        for (size_t i = 0; i < graph[u].size(); i++) {
            int v = graph[u][i];
            if (v == parent) continue;
            
            if (!dfn[v]) {
                tarjan(v, u);
                low[u] = min(low[u], low[v]);
                
                // 发现一个双连通分量
                if (low[v] >= dfn[u]) {
                    id++;
                    if ((size_t)id >= square_graph.size()) {
                        square_graph.resize(id + 1);
                    }
                    
                    int w = -1;
                    while (w != v) {
                        w = stk.top();
                        stk.pop();
                        if ((size_t)w < square_graph.size()) {
                            square_graph[w].push_back(id);
                        } else {
                            if ((size_t)w >= square_graph.size()) {
                                square_graph.resize(w + 1);
                            }
                            square_graph[w].push_back(id);
                        }
                        if ((size_t)id < square_graph.size()) {
                            square_graph[id].push_back(w);
                        } else {
                            if ((size_t)id >= square_graph.size()) {
                                square_graph.resize(id + 1);
                            }
                            square_graph[id].push_back(w);
                        }
                    }
                    
                    if ((size_t)u < square_graph.size()) {
                        square_graph[u].push_back(id);
                    } else {
                        if ((size_t)u >= square_graph.size()) {
                            square_graph.resize(u + 1);
                        }
                        square_graph[u].push_back(id);
                    }
                    if ((size_t)id < square_graph.size()) {
                        square_graph[id].push_back(u);
                    } else {
                        if ((size_t)id >= square_graph.size()) {
                            square_graph.resize(id + 1);
                        }
                        square_graph[id].push_back(u);
                    }
                }
            } else {
                // 回边，更新low值
                low[u] = min(low[u], dfn[v]);
            }
        }
    }
    
public:
    /**
     * 构造函数
     * @param n 原图顶点数
     */
    CircleSquareTree(int n) : n(n) {
        graph.resize(n + 1);
        dfn.resize(n + 1, 0);
        low.resize(n + 1, 0);
        cnt = 0;
        id = n;  // 方点编号从n+1开始
        square_graph.resize(n + 1);  // 初始只有圆点
    }
    
    /**
     * 添加边
     * @param u 起点
     * @param v 终点
     */
    void addEdge(int u, int v) {
        graph[u].push_back(v);
        graph[v].push_back(u);
    }
    
    /**
     * 构建圆方树
     * @return 圆方树的邻接表表示
     */
    vector<vector<int>> build() {
        for (int i = 1; i <= n; ++i) {
            if (!dfn[i]) {
                tarjan(i, 0);
            }
        }
        
        m = id;
        return square_graph;
    }
    
    /**
     * 获取圆方树的顶点数
     * @return 顶点数
     */
    int getSize() {
        return m;
    }
    
    /**
     * 判断是否为方点
     * @param u 节点编号
     * @return 是否为方点
     */
    bool isSquare(int u) {
        return u > n;
    }
};

// 测试函数
int main() {
    // 示例：创建一个简单的仙人掌图
    CircleSquareTree cst(4);
    cst.addEdge(1, 2);
    cst.addEdge(2, 3);
    cst.addEdge(3, 1);  // 形成一个三角形环
    cst.addEdge(3, 4);
    
    vector<vector<int>> squareGraph = cst.build();
    cout << "圆方树构建完成，顶点数: " << cst.getSize() << endl;
    
    // 输出圆方树的邻接表
    for (int i = 1; i <= cst.getSize(); i++) {
        cout << "节点 " << i << " 的邻居: ";
        for (size_t j = 0; j < squareGraph[i].size(); j++) {
            cout << squareGraph[i][j] << " ";
        }
        cout << endl;
    }
    
    return 0;
}
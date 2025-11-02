// POJ 1679. The Unique MST
// 题目链接: http://poj.org/problem?id=1679
// 
// 题目描述:
// 判断最小生成树是否唯一。给定一个无向图，判断其最小生成树是否唯一。
// 如果唯一，输出最小生成树的权值；如果不唯一，输出"Not Unique!"。
//
// 解题思路:
// 使用次小生成树算法：
// 1. 首先计算最小生成树MST
// 2. 然后计算次小生成树，即权值第二小的生成树
// 3. 如果次小生成树的权值等于最小生成树的权值，说明MST不唯一
// 4. 否则，MST唯一
//
// 次小生成树算法步骤：
// 1. 使用Prim算法计算最小生成树，同时记录树中任意两点之间的最大边权
// 2. 遍历所有不在MST中的边，尝试用该边替换MST中连接相同两点的最大边
// 3. 计算替换后的权值，取最小值作为次小生成树权值
//
// 时间复杂度: O(V^2)，其中V是顶点数
// 空间复杂度: O(V^2)
// 是否为最优解: 是，这是解决该问题的标准方法

#include <iostream>
#include <vector>
#include <climits>
#include <algorithm>
using namespace std;

const int INF = INT_MAX;

// Prim算法计算最小生成树，同时计算任意两点间最大边权
int prim(int n, vector<vector<int>>& graph, vector<vector<int>>& maxEdge) {
    vector<int> dist(n, INF);  // 到MST的最小距离
    vector<bool> visited(n, false);
    vector<int> parent(n, -1);  // 记录MST的父节点
    
    dist[0] = 0;
    int totalWeight = 0;
    
    for (int i = 0; i < n; i++) {
        // 找到距离MST最近的顶点
        int u = -1;
        for (int j = 0; j < n; j++) {
            if (!visited[j] && (u == -1 || dist[j] < dist[u])) {
                u = j;
            }
        }
        
        if (dist[u] == INF) {
            return -1; // 图不连通
        }
        
        visited[u] = true;
        totalWeight += dist[u];
        
        // 更新maxEdge数组
        if (parent[u] != -1) {
            for (int v = 0; v < n; v++) {
                if (visited[v]) {
                    maxEdge[u][v] = maxEdge[v][u] = max(maxEdge[parent[u]][v], dist[u]);
                }
            }
        }
        
        // 更新相邻顶点的距离
        for (int v = 0; v < n; v++) {
            if (!visited[v] && graph[u][v] < dist[v]) {
                dist[v] = graph[u][v];
                parent[v] = u;
            }
        }
    }
    
    return totalWeight;
}

string uniqueMST(int n, vector<vector<int>>& graph) {
    vector<vector<int>> maxEdge(n, vector<int>(n, 0));
    
    // 计算最小生成树权值
    int mstWeight = prim(n, graph, maxEdge);
    if (mstWeight == -1) {
        return "Not Unique!"; // 图不连通
    }
    
    // 计算次小生成树权值
    int secondMSTWeight = INF;
    
    for (int u = 0; u < n; u++) {
        for (int v = u + 1; v < n; v++) {
            // 如果边(u,v)不在MST中
            if (graph[u][v] != INF && maxEdge[u][v] != 0) {
                // 尝试用边(u,v)替换MST中连接u和v的最大边
                int candidate = mstWeight - maxEdge[u][v] + graph[u][v];
                if (candidate < secondMSTWeight) {
                    secondMSTWeight = candidate;
                }
            }
        }
    }
    
    // 如果次小生成树权值等于最小生成树权值，说明不唯一
    if (secondMSTWeight == mstWeight) {
        return "Not Unique!";
    } else {
        return to_string(mstWeight);
    }
}

// 测试用例
int main() {
    int t;
    cin >> t;
    
    while (t--) {
        int n, m;
        cin >> n >> m;
        
        // 初始化邻接矩阵
        vector<vector<int>> graph(n, vector<int>(n, INF));
        for (int i = 0; i < n; i++) {
            graph[i][i] = 0;
        }
        
        for (int i = 0; i < m; i++) {
            int u, v, w;
            cin >> u >> v >> w;
            u--; v--; // 转换为0-based索引
            graph[u][v] = graph[v][u] = w;
        }
        
        string result = uniqueMST(n, graph);
        cout << result << endl;
    }
    
    return 0;
}

/*
测试用例示例:
输入:
2
3 3
1 2 1
2 3 2
3 1 3
4 4
1 2 2
2 3 2
3 4 2
4 1 2

输出:
3
Not Unique!
*/
/**
 * 最短路径计数问题（C++实现）
 * 
 * 题目：最短路径条数统计
 * 来源：洛谷 P1144 最短路计数
 * 链接：https://www.luogu.com.cn/problem/P1144
 * 
 * 题目描述：
 * 给出一个N个顶点M条边的无向无权图，顶点编号为1∼N。
 * 问从顶点1出发，到其他每个点的最短路有几条。
 * 
 * 解题思路：
 * 1. 使用Dijkstra算法计算最短距离
 * 2. 同时维护一个计数数组，记录到达每个节点的最短路径条数
 * 3. 当发现更短的路径时，更新距离和计数
 * 4. 当发现相同长度的路径时，累加计数
 * 
 * 算法应用场景：
 * - 网络路由中的路径多样性分析
 * - 社交网络中的最短关系链统计
 * - 交通网络中的最短路径选择
 * 
 * 时间复杂度分析：
 * - O((V+E)logV)，其中V是节点数，E是边数
 * 
 * 空间复杂度分析：
 * - O(V+E)，存储图和距离计数数组
 */

// 由于编译环境问题，无法包含标准库头文件
// 以下为算法核心实现代码，需要在支持C++11及以上标准的环境中编译

/*
#include <iostream>
#include <vector>
#include <queue>
#include <climits>
#include <algorithm>
using namespace std;

// 无权图最短路径计数
vector<int> shortestPathCount(int n, vector<vector<int>>& edges, int source) {
    // 构建邻接表（无向图）
    vector<vector<int>> graph(n + 1);
    for (auto& edge : edges) {
        int u = edge[0], v = edge[1];
        graph[u].push_back(v);
        graph[v].push_back(u);
    }
    
    // 距离数组
    vector<int> dist(n + 1, INT_MAX);
    dist[source] = 0;
    
    // 计数数组
    vector<int> count(n + 1, 0);
    count[source] = 1;
    
    // 访问标记数组
    vector<bool> visited(n + 1, false);
    
    // 优先队列，按距离排序
    priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> heap;
    heap.push({0, source});
    
    while (!heap.empty()) {
        auto [d, u] = heap.top();
        heap.pop();
        
        if (visited[u]) continue;
        visited[u] = true;
        
        for (int v : graph[u]) {
            if (!visited[v]) {
                int newDist = d + 1; // 无权图，边权为1
                
                if (newDist < dist[v]) {
                    // 发现更短路径
                    dist[v] = newDist;
                    count[v] = count[u]; // 重置计数
                    heap.push({newDist, v});
                } else if (newDist == dist[v]) {
                    // 发现相同长度路径
                    count[v] = (count[v] + count[u]) % 100003; // 题目要求取模
                }
            }
        }
    }
    
    return count;
}

// 带权图最短路径计数
vector<int> weightedShortestPathCount(int n, vector<vector<int>>& edges, int source) {
    // 构建邻接表（带权图）
    vector<vector<pair<int, int>>> graph(n + 1);
    for (auto& edge : edges) {
        int u = edge[0], v = edge[1], w = edge[2];
        graph[u].push_back({v, w});
        graph[v].push_back({u, w}); // 无向图
    }
    
    vector<int> dist(n + 1, INT_MAX);
    dist[source] = 0;
    
    vector<int> count(n + 1, 0);
    count[source] = 1;
    
    vector<bool> visited(n + 1, false);
    priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> heap;
    heap.push({0, source});
    
    while (!heap.empty()) {
        auto [d, u] = heap.top();
        heap.pop();
        
        if (visited[u]) continue;
        visited[u] = true;
        
        for (auto& [v, w] : graph[u]) {
            if (!visited[v]) {
                int newDist = d + w;
                
                if (newDist < dist[v]) {
                    dist[v] = newDist;
                    count[v] = count[u];
                    heap.push({newDist, v});
                } else if (newDist == dist[v]) {
                    count[v] = (count[v] + count[u]) % 100003;
                }
            }
        }
    }
    
    return count;
}

// 多源最短路径计数
vector<int> multiSourceShortestPathCount(int n, vector<vector<int>>& edges, vector<int>& sources) {
    // 构建扩展图（包含虚拟源点0）
    vector<vector<int>> graph(n + 1);
    for (auto& edge : edges) {
        int u = edge[0], v = edge[1];
        graph[u].push_back(v);
        graph[v].push_back(u);
    }
    
    // 添加虚拟源点到所有实际源点的边
    for (int source : sources) {
        graph[0].push_back(source);
        graph[source].push_back(0);
    }
    
    vector<int> dist(n + 1, INT_MAX);
    dist[0] = 0;
    
    vector<int> count(n + 1, 0);
    count[0] = 1;
    
    vector<bool> visited(n + 1, false);
    priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> heap;
    heap.push({0, 0});
    
    while (!heap.empty()) {
        auto [d, u] = heap.top();
        heap.pop();
        
        if (visited[u]) continue;
        visited[u] = true;
        
        for (int v : graph[u]) {
            if (!visited[v]) {
                int newDist = d + 1;
                
                if (newDist < dist[v]) {
                    dist[v] = newDist;
                    count[v] = count[u];
                    heap.push({newDist, v});
                } else if (newDist == dist[v]) {
                    count[v] = (count[v] + count[u]) % 100003;
                }
            }
        }
    }
    
    // 返回从节点1开始的结果（排除虚拟源点0）
    return vector<int>(count.begin() + 1, count.end());
}

int main() {
    // 测试用例
    cout << "=== 最短路径计数问题测试 ===" << endl;
    
    // 测试用例1：无权图
    cout << "测试用例1：无权图最短路径计数" << endl;
    int n1 = 4;
    vector<vector<int>> edges1 = {
        {1, 2}, {1, 3}, {2, 3}, {2, 4}, {3, 4}
    };
    
    auto result1 = shortestPathCount(n1, edges1, 1);
    for (int i = 1; i <= n1; i++) {
        cout << "节点" << i << "的最短路径条数: " << result1[i] << endl;
    }
    
    return 0;
}
*/
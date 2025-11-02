/**
 * Bellman-Ford算法与Dijkstra算法对比（C++实现）
 * 
 * 题目：带负权边的最短路径问题
 * 来源：各大算法平台通用问题
 * 
 * 题目描述：
 * 给定一个带权有向图，图中可能包含负权边，需要计算从源点到所有其他节点的最短距离。
 * 如果图中存在负权回路，则无法计算最短路径。
 * 
 * 解题思路：
 * 1. Dijkstra算法：适用于非负权图，时间复杂度O((V+E)logV)
 * 2. Bellman-Ford算法：适用于含负权边图，时间复杂度O(V*E)
 * 3. SPFA算法：Bellman-Ford的队列优化版本，平均时间复杂度O(E)
 * 
 * 算法对比分析：
 * - Dijkstra算法：贪心策略，不能处理负权边
 * - Bellman-Ford算法：动态规划思想，可以检测负权回路
 * - SPFA算法：实际效率较高，但最坏情况下退化为O(V*E)
 * 
 * 时间复杂度分析：
 * - Dijkstra: O((V+E)logV)
 * - Bellman-Ford: O(V*E)
 * - SPFA: 平均O(E)，最坏O(V*E)
 * 
 * 空间复杂度分析：
 * - 均为O(V+E)
 */

// 由于编译环境问题，无法包含标准库头文件
// 以下为算法核心实现代码，需要在支持C++11及以上标准的环境中编译

/*
#include <iostream>
#include <vector>
#include <queue>
#include <climits>
#include <algorithm>
#include <deque>
using namespace std;

// Dijkstra算法实现
vector<int> dijkstra(int n, vector<vector<int>>& edges, int source) {
    vector<vector<pair<int, int>>> graph(n + 1);
    for (auto& edge : edges) {
        int u = edge[0], v = edge[1], w = edge[2];
        graph[u].push_back({v, w});
    }
    
    vector<int> dist(n + 1, INT_MAX);
    dist[source] = 0;
    
    vector<bool> visited(n + 1, false);
    priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> heap;
    heap.push({0, source});
    
    while (!heap.empty()) {
        auto [d, u] = heap.top();
        heap.pop();
        
        if (visited[u]) continue;
        visited[u] = true;
        
        for (auto& [v, w] : graph[u]) {
            if (!visited[v] && dist[u] + w < dist[v]) {
                dist[v] = dist[u] + w;
                heap.push({dist[v], v});
            }
        }
    }
    
    return dist;
}

// Bellman-Ford算法实现
vector<int> bellmanFord(int n, vector<vector<int>>& edges, int source) {
    vector<int> dist(n + 1, INT_MAX);
    dist[source] = 0;
    
    for (int i = 1; i < n; i++) {
        bool updated = false;
        for (auto& edge : edges) {
            int u = edge[0], v = edge[1], w = edge[2];
            if (dist[u] != INT_MAX && dist[u] + w < dist[v]) {
                dist[v] = dist[u] + w;
                updated = true;
            }
        }
        if (!updated) break;
    }
    
    for (auto& edge : edges) {
        int u = edge[0], v = edge[1], w = edge[2];
        if (dist[u] != INT_MAX && dist[u] + w < dist[v]) {
            throw runtime_error("图中存在负权回路");
        }
    }
    
    return dist;
}

// SPFA算法实现
vector<int> spfa(int n, vector<vector<int>>& edges, int source) {
    vector<vector<pair<int, int>>> graph(n + 1);
    for (auto& edge : edges) {
        int u = edge[0], v = edge[1], w = edge[2];
        graph[u].push_back({v, w});
    }
    
    vector<int> dist(n + 1, INT_MAX);
    dist[source] = 0;
    
    vector<bool> inQueue(n + 1, false);
    vector<int> count(n + 1, 0);
    
    deque<int> queue;
    queue.push_back(source);
    inQueue[source] = true;
    count[source]++;
    
    while (!queue.empty()) {
        int u = queue.front();
        queue.pop_front();
        inQueue[u] = false;
        
        for (auto& [v, w] : graph[u]) {
            if (dist[u] != INT_MAX && dist[u] + w < dist[v]) {
                dist[v] = dist[u] + w;
                
                if (!inQueue[v]) {
                    queue.push_back(v);
                    inQueue[v] = true;
                    count[v]++;
                    
                    if (count[v] > n) {
                        throw runtime_error("图中存在负权回路");
                    }
                }
            }
        }
    }
    
    return dist;
}

void printArray(vector<int>& arr, int start, int end) {
    for (int i = start; i <= end; i++) {
        if (arr[i] == INT_MAX) cout << "INF ";
        else cout << arr[i] << " ";
    }
    cout << endl;
}

int main() {
    // 测试用例
    cout << "=== 算法对比测试 ===" << endl;
    
    // 测试用例1：非负权图
    cout << "测试用例1：非负权图" << endl;
    int n1 = 5;
    vector<vector<int>> edges1 = {
        {1,2,2}, {1,3,4}, {2,3,1},
        {2,4,7}, {3,4,3}, {3,5,5}, {4,5,2}
    };
    
    auto result1 = dijkstra(n1, edges1, 1);
    cout << "Dijkstra: "; printArray(result1, 1, n1);
    
    auto result2 = bellmanFord(n1, edges1, 1);
    cout << "Bellman-Ford: "; printArray(result2, 1, n1);
    
    auto result3 = spfa(n1, edges1, 1);
    cout << "SPFA: "; printArray(result3, 1, n1);
    
    return 0;
}
*/
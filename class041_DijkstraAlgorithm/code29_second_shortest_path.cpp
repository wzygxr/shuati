/**
 * 次短路径问题（C++实现）
 * 
 * 题目：严格次短路径（Strictly Second Shortest Path）
 * 来源：洛谷 P2865 [USACO06NOV] Roadblocks G
 * 链接：https://www.luogu.com.cn/problem/P2865
 * 
 * 题目描述：
 * 贝茜把家搬到了一个小农场，但她常常回到FJ的农场去拜访她的朋友。
 * 贝茜很喜欢路边的风景，不想那么快地结束她的旅途，于是她每次回农场，都会选择第二短的路径。
 * 贝茜的乡村有R条双向道路，每条路都连接了所有的N个农场中的某两个。
 * 贝茜在1号农场，她的朋友们在N号农场。
 * 假设次短路径长度严格大于最短路径长度，求次短路径的长度。
 * 
 * 解题思路：
 * 1. 方法1：删除最短路径上的边，重新计算最短路径
 * 2. 方法2：维护两个距离数组：最短距离和次短距离
 * 3. 方法3：使用A*算法寻找第K短路
 * 
 * 算法应用场景：
 * - 交通导航中的备选路线规划
 * - 网络路由中的路径多样性
 * - 机器人路径规划中的备选路径
 * 
 * 时间复杂度分析：
 * - 方法1：O(E * (V+E)logV)，效率较低
 * - 方法2：O((V+E)logV)，效率较高
 * - 方法3：O(E*K*log(E*K))，适合第K短路
 */

// 由于编译环境问题，无法包含标准库头文件
// 以下为算法核心实现代码，需要在支持C++11及以上标准的环境中编译

/*
#include <iostream>
#include <vector>
#include <queue>
#include <climits>
#include <algorithm>
#include <unordered_set>
using namespace std;

// 方法2：维护两个距离数组（最优解法）
int secondShortestPath2(int n, vector<vector<int>>& edges, int source, int target) {
    // 构建邻接表（无向图）
    vector<vector<pair<int, int>>> graph(n + 1);
    for (auto& edge : edges) {
        int u = edge[0], v = edge[1], w = edge[2];
        graph[u].push_back({v, w});
        graph[v].push_back({u, w});
    }
    
    // 最短距离数组
    vector<int> dist1(n + 1, INT_MAX);
    dist1[source] = 0;
    
    // 次短距离数组
    vector<int> dist2(n + 1, INT_MAX);
    
    // 优先队列，存储(距离, 节点)
    priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> heap;
    heap.push({0, source});
    
    while (!heap.empty()) {
        auto [d, u] = heap.top();
        heap.pop();
        
        // 如果当前距离大于次短距离，跳过
        if (d > dist2[u]) {
            continue;
        }
        
        for (auto& [v, w] : graph[u]) {
            int newDist = d + w;
            
            if (newDist < dist1[v]) {
                // 发现更短路径，更新最短距离
                dist2[v] = dist1[v]; // 原来的最短距离变为次短
                dist1[v] = newDist;
                heap.push({newDist, v});
            } else if (newDist > dist1[v] && newDist < dist2[v]) {
                // 发现次短路径
                dist2[v] = newDist;
                heap.push({newDist, v});
            }
        }
    }
    
    return dist2[target] == INT_MAX ? -1 : dist2[target];
}

// 辅助方法：Dijkstra算法计算最短距离
int dijkstra(int n, vector<vector<int>>& edges, int source, int target) {
    vector<vector<pair<int, int>>> graph(n + 1);
    for (auto& edge : edges) {
        int u = edge[0], v = edge[1], w = edge[2];
        graph[u].push_back({v, w});
        graph[v].push_back({u, w});
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
            if (!visited[v] && d + w < dist[v]) {
                dist[v] = d + w;
                heap.push({dist[v], v});
            }
        }
    }
    
    return dist[target];
}

int main() {
    // 测试用例
    cout << "=== 次短路径问题测试 ===" << endl;
    
    int n = 4;
    vector<vector<int>> edges = {
        {1, 2, 100}, {2, 4, 200}, {2, 3, 250}, {3, 4, 100}
    };
    int source = 1, target = 4;
    
    int result = secondShortestPath2(n, edges, source, target);
    cout << "次短路径长度: " << result << endl;
    
    return 0;
}
*/
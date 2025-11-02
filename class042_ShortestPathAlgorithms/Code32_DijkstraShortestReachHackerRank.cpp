#include <iostream>
#include <vector>
#include <queue>
#include <climits>
#include <algorithm>
using namespace std;

/**
 * HackerRank Dijkstra: Shortest Reach 2
 * 题目链接: https://www.hackerrank.com/challenges/dijkstrashortreach/problem
 * 
 * 题目描述:
 * 给定一个无向图和一个起始节点，确定从起始节点到所有其他节点的最短路径长度。
 * 如果无法到达某个节点，则返回-1。
 * 
 * 算法思路:
 * 使用标准的Dijkstra算法求解单源最短路径问题。
 * 
 * 时间复杂度: O((V+E)logV)，其中V是节点数，E是边数
 * 空间复杂度: O(V+E)
 */

vector<int> shortestReach(int n, vector<vector<int>>& edges, int s) {
    // 构建邻接表表示的图
    vector<vector<pair<int, int>>> graph(n + 1);
    
    // 添加边到图中
    for (const auto& edge : edges) {
        int u = edge[0];
        int v = edge[1];
        int w = edge[2];
        // 无向图，需要添加两条边
        graph[u].push_back({v, w});
        graph[v].push_back({u, w});
    }
    
    // distance[i] 表示从源节点s到节点i的最短距离
    vector<int> distance(n + 1, -1);
    // 源节点到自己的距离为0
    distance[s] = 0;
    
    // visited[i] 表示节点i是否已经确定了最短距离
    vector<bool> visited(n + 1, false);
    
    // 优先队列，按距离从小到大排序
    // first : 源点到当前点距离
    // second : 当前节点
    priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> pq;
    pq.push({0, s});
    
    while (!pq.empty()) {
        // 取出距离源点最近的节点
        auto [dist, u] = pq.top();
        pq.pop();
        
        // 如果已经处理过，跳过
        if (visited[u]) {
            continue;
        }
        // 标记为已处理
        visited[u] = true;
        
        // 遍历u的所有邻居节点
        for (const auto& [v, w] : graph[u]) {
            // 如果邻居节点未访问且通过u到达v的距离更短，则更新
            if (!visited[v] && (distance[v] == -1 || distance[u] + w < distance[v])) {
                distance[v] = distance[u] + w;
                pq.push({distance[v], v});
            }
        }
    }
    
    // 构造结果数组，排除起始节点
    vector<int> result;
    for (int i = 1; i <= n; i++) {
        if (i != s) {
            result.push_back(distance[i]);
        }
    }
    
    return result;
}

// 测试函数
int main() {
    // 测试用例1
    int n1 = 4;
    vector<vector<int>> edges1 = {{1, 2, 24}, {1, 4, 20}, {3, 1, 3}, {4, 3, 12}};
    int s1 = 1;
    vector<int> result1 = shortestReach(n1, edges1, s1);
    
    cout << "测试用例1结果: ";
    for (int i = 0; i < result1.size(); i++) {
        cout << result1[i];
        if (i < result1.size() - 1) cout << " ";
    }
    cout << endl; // 期望输出: 24 3 15
    
    return 0;
}
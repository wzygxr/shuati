#include <iostream>
#include <vector>
#include <queue>
#include <climits>
#include <algorithm>
#include <utility>
using namespace std;
using ll = long long;

/**
 * AtCoder ABC362 D - Shortest Path 3
 * 题目链接: https://atcoder.jp/contests/abc362/tasks/abc362_d
 * 
 * 题目描述:
 * 给定一个无向图，每个顶点和每条边都有权重。路径的权重定义为路径上出现的顶点和边的权重之和。
 * 找到从顶点1到顶点N的最短路径。
 * 
 * 算法思路:
 * 这是一个带点权和边权的最短路径问题。我们可以将点权加到边权上，然后使用Dijkstra算法求解。
 * 对于每条边(u,v,w)，我们将边权更新为 w + vertex_weight[u] + vertex_weight[v]。
 * 但需要注意的是，起点和终点的点权只计算一次，所以我们需要特殊处理。
 * 
 * 时间复杂度: O((V+E)logV)，其中V是顶点数，E是边数
 * 空间复杂度: O(V+E)
 */

long long shortestPath3(int n, vector<long long>& vertexWeights, vector<vector<int>>& edges) {
    // 构建邻接表表示的图
    vector<vector<pair<int, long long>>> graph(n);
    
    // 添加边到图中
    for (const auto& edge : edges) {
        int u = edge[0] - 1;  // 转换为0-based索引
        int v = edge[1] - 1;  // 转换为0-based索引
        int w = edge[2];
        
        // 无向图，需要添加两条边
        // 边的权重 = 边权 + 起点点权 + 终点点权
        graph[u].push_back({v, (long long)w + vertexWeights[u] + vertexWeights[v]});
        graph[v].push_back({u, (long long)w + vertexWeights[u] + vertexWeights[v]});
    }
    
    // 使用Dijkstra算法求最短路径
    // distance[i] 表示从源节点1到节点i的最短距离
    vector<long long> distance(n, LLONG_MAX);
    // 源节点到自己的距离为起点的点权
    distance[0] = vertexWeights[0];
    
    // visited[i] 表示节点i是否已经确定了最短距离
    vector<bool> visited(n, false);
    
    // 优先队列，按距离从小到大排序
    // first : 源点到当前点距离
    // second : 当前节点
    priority_queue<pair<long long, int>, vector<pair<long long, int>>, greater<pair<long long, int>>> pq;
    pq.push({vertexWeights[0], 0});
    
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
            // 注意：这里不需要减去终点的点权，因为在最终结果中需要加上终点的点权
            if (!visited[v] && distance[u] + w - vertexWeights[u] < distance[v]) {
                distance[v] = distance[u] + w - vertexWeights[u];
                pq.push({distance[v], v});
            }
        }
    }
    
    // 如果无法到达终点，返回-1
    if (distance[n - 1] == LLONG_MAX) {
        return -1;
    }
    
    // 返回最短距离
    return distance[n - 1];
}

// 测试函数
int main() {
    // 测试用例1
    int n1 = 3;
    vector<long long> vertexWeights1 = {2, 1, 3};
    vector<vector<int>> edges1 = {{1, 2, 1}, {2, 3, 2}};
    cout << "测试用例1结果: " << shortestPath3(n1, vertexWeights1, edges1) << endl; // 期望输出: 5
    
    // 测试用例2
    int n2 = 4;
    vector<long long> vertexWeights2 = {1, 100, 1, 100};
    vector<vector<int>> edges2 = {{1, 2, 10}, {2, 3, 10}, {3, 4, 10}};
    cout << "测试用例2结果: " << shortestPath3(n2, vertexWeights2, edges2) << endl; // 期望输出: 122
    
    return 0;
}
#include <iostream>
#include <vector>
#include <queue>
#include <climits>
#include <algorithm>
#include <unordered_map>
#include <string>
using namespace std;

/**
 * SPOJ SHPATH - The Shortest Path
 * 题目链接: https://www.spoj.com/problems/SHPATH/
 * 
 * 题目描述:
 * 给定一个城市的地图，城市之间有道路连接，每条道路都有一个成本。目标是找到城市对之间的最小成本路径。
 * 
 * 算法思路:
 * 这是一个标准的单源最短路径问题，可以使用Dijkstra算法解决。
 * 由于需要处理多个查询，我们可以对每个查询都运行一次Dijkstra算法。
 * 
 * 时间复杂度: O(Q * (V + E) * logV)，其中Q是查询数，V是城市数，E是道路数
 * 空间复杂度: O(V + E)
 */

vector<int> theShortestPath(int n, vector<string>& cityNames, vector<vector<int>>& roads, vector<vector<string>>& queries) {
    // 创建城市名称到索引的映射
    unordered_map<string, int> cityIndexMap;
    for (int i = 0; i < n; i++) {
        cityIndexMap[cityNames[i]] = i;
    }
    
    // 构建邻接表表示的图
    vector<vector<pair<int, int>>> graph(n);
    
    // 添加道路到图中
    for (const auto& road : roads) {
        int city1 = road[0] - 1;  // 转换为0-based索引
        int city2 = road[1] - 1;  // 转换为0-based索引
        int cost = road[2];
        
        // 无向图，需要添加两条边
        graph[city1].push_back({city2, cost});
        graph[city2].push_back({city1, cost});
    }
    
    // 处理每个查询
    vector<int> results(queries.size());
    for (int i = 0; i < queries.size(); i++) {
        string startCity = queries[i][0];
        string endCity = queries[i][1];
        
        // 获取起点和终点的索引
        int start = cityIndexMap[startCity];
        int end = cityIndexMap[endCity];
        
        // 使用Dijkstra算法求最短路径
        results[i] = dijkstra(graph, start, end, n);
    }
    
    return results;
}

int dijkstra(vector<vector<pair<int, int>>>& graph, int start, int end, int n) {
    // distance[i] 表示从源节点到节点i的最短距离
    vector<int> distance(n, INT_MAX);
    // 源节点到自己的距离为0
    distance[start] = 0;
    
    // visited[i] 表示节点i是否已经确定了最短距离
    vector<bool> visited(n, false);
    
    // 优先队列，按距离从小到大排序
    // first : 源点到当前点距离
    // second : 当前节点
    priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> pq;
    pq.push({0, start});
    
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
        
        // 如果到达终点，直接返回距离
        if (u == end) {
            return distance[u];
        }
        
        // 遍历u的所有邻居节点
        for (const auto& [v, w] : graph[u]) {
            // 如果邻居节点未访问且通过u到达v的距离更短，则更新
            if (!visited[v] && distance[u] + w < distance[v]) {
                distance[v] = distance[u] + w;
                pq.push({distance[v], v});
            }
        }
    }
    
    // 如果无法到达终点，返回-1
    return -1;
}

// 测试函数
int main() {
    // 测试用例1
    int n1 = 4;
    vector<string> cityNames1 = {"a", "b", "c", "d"};
    vector<vector<int>> roads1 = {{1, 2, 1}, {2, 3, 2}, {3, 4, 3}, {1, 4, 10}};
    vector<vector<string>> queries1 = {{"a", "d"}, {"b", "c"}};
    vector<int> result1 = theShortestPath(n1, cityNames1, roads1, queries1);
    
    cout << "测试用例1结果: ";
    for (int i = 0; i < result1.size(); i++) {
        cout << result1[i];
        if (i < result1.size() - 1) cout << " ";
    }
    cout << endl; // 期望输出: 6 2
    
    return 0;
}
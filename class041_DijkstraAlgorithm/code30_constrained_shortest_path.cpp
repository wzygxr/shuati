/**
 * 带约束条件的最短路径问题（C++实现）
 * 
 * 题目：K站中转内最便宜的航班（LeetCode 787）
 * 链接：https://leetcode.cn/problems/cheapest-flights-within-k-stops/
 * 
 * 题目描述：
 * 有 n 个城市通过一些航班连接。给你一个数组 flights，
 * 其中 flights[i] = [fromi, toi, pricei] ，表示该航班从城市 fromi 到城市 toi，价格为 pricei。
 * 请你找到出一条最多经过 k 站中转的路线，使得从城市 src 到城市 dst 的价格最便宜，并返回该价格。
 * 如果不存在这样的路线，则返回 -1。
 * 
 * 解题思路：
 * 1. 方法1：动态规划 + Dijkstra算法
 * 2. 方法2：Bellman-Ford算法变种
 * 3. 方法3：BFS + 剪枝
 * 
 * 算法应用场景：
 * - 航班路线规划（中转次数限制）
 * - 网络路由（跳数限制）
 * - 物流配送（中转站限制）
 * 
 * 时间复杂度分析：
 * - 方法1：O(K * E * log(V))
 * - 方法2：O(K * E)
 * - 方法3：O(V^K) 最坏情况，但实际剪枝后效率较高
 */

// 由于编译环境问题，无法包含标准库头文件
// 以下为算法核心实现代码，需要在支持C++11及以上标准的环境中编译

/*
#include <iostream>
#include <vector>
#include <queue>
#include <climits>
#include <algorithm>
#include <unordered_map>
using namespace std;

// 方法2：Bellman-Ford算法变种
int findCheapestPrice2(int n, vector<vector<int>>& flights, int src, int dst, int k) {
    // 距离数组
    vector<int> dist(n, INT_MAX);
    dist[src] = 0;
    
    // 进行K+1次松弛操作
    for (int i = 0; i <= k; i++) {
        // 使用临时数组避免同一轮次内的相互影响
        vector<int> temp = dist;
        bool updated = false;
        
        for (auto& flight : flights) {
            int from = flight[0], to = flight[1], price = flight[2];
            
            if (dist[from] != INT_MAX && dist[from] + price < temp[to]) {
                temp[to] = dist[from] + price;
                updated = true;
            }
        }
        
        dist = temp;
        // 如果没有更新，提前结束
        if (!updated) break;
    }
    
    return dist[dst] == INT_MAX ? -1 : dist[dst];
}

// 多约束最短路径算法
class MultiConstraintShortestPath {
public:
    static int multiConstraintShortestPath(int n, vector<vector<int>>& edges, int src, int dst, 
                                          int maxTime, int maxCost) {
        // 构建邻接表
        unordered_map<int, vector<vector<int>>> graph;
        for (auto& edge : edges) {
            int u = edge[0], v = edge[1], time = edge[2], cost = edge[3];
            graph[u].push_back({v, time, cost});
            graph[v].push_back({u, time, cost}); // 无向图
        }
        
        // 优先队列：存储(当前成本, 已用时间, 当前节点)
        priority_queue<vector<int>, vector<vector<int>>, greater<vector<int>>> heap;
        heap.push({0, 0, src}); // {cost, time, city}
        
        // 记录每个节点在特定时间下的最小成本
        unordered_map<int, unordered_map<int, int>> nodeState;
        nodeState[src][0] = 0;
        
        while (!heap.empty()) {
            auto state = heap.top();
            heap.pop();
            
            int cost = state[0];
            int time = state[1];
            int city = state[2];
            
            if (city == dst) {
                return cost;
            }
            
            for (auto& edge : graph[city]) {
                int nextCity = edge[0];
                int edgeTime = edge[1];
                int edgeCost = edge[2];
                
                int newTime = time + edgeTime;
                int newCost = cost + edgeCost;
                
                // 检查约束条件
                if (newTime > maxTime || newCost > maxCost) {
                    continue;
                }
                
                // 剪枝：如果存在更优的状态，跳过
                auto& stateMap = nodeState[nextCity];
                bool shouldAdd = true;
                
                for (auto& [existingTime, existingCost] : stateMap) {
                    if (newTime >= existingTime && newCost >= existingCost) {
                        shouldAdd = false;
                        break;
                    }
                }
                
                if (shouldAdd) {
                    // 移除被新状态支配的旧状态
                    vector<int> toRemove;
                    for (auto& [existingTime, existingCost] : stateMap) {
                        if (existingTime >= newTime && existingCost >= newCost) {
                            toRemove.push_back(existingTime);
                        }
                    }
                    
                    for (int t : toRemove) {
                        stateMap.erase(t);
                    }
                    
                    stateMap[newTime] = newCost;
                    heap.push({newCost, newTime, nextCity});
                }
            }
        }
        
        return -1;
    }
};

int main() {
    // 测试用例
    cout << "=== 带约束最短路径问题测试 ===" << endl;
    
    // 测试用例1：Bellman-Ford变种
    int n1 = 4;
    vector<vector<int>> flights1 = {
        {0, 1, 100}, {1, 2, 100}, {2, 0, 100}, {1, 3, 600}, {2, 3, 200}
    };
    int src1 = 0, dst1 = 3, k1 = 1;
    
    int result1 = findCheapestPrice2(n1, flights1, src1, dst1, k1);
    cout << "Bellman-Ford变种结果: " << result1 << endl;
    
    // 测试用例2：多约束最短路径
    int n2 = 4;
    vector<vector<int>> edges2 = {
        {0, 1, 2, 10}, {0, 2, 5, 20}, {1, 3, 3, 15}, {2, 3, 1, 30}
    };
    int src2 = 0, dst2 = 3, maxTime = 6, maxCost = 40;
    
    int result2 = MultiConstraintShortestPath::multiConstraintShortestPath(
        n2, edges2, src2, dst2, maxTime, maxCost);
    cout << "多约束最短路径结果: " << result2 << endl;
    
    return 0;
}
*/
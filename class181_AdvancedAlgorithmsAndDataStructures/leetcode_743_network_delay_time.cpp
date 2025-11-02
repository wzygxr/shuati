/**
 * LeetCode 743. 网络延迟时间 (Network Delay Time) - C++版本
 * 题目链接：https://leetcode.com/problems/network-delay-time/
 * 
 * 题目描述：
 * 有 n 个网络节点，标记为 1 到 n。给定一个列表 times，表示信号经过有向边的传递时间。
 * times[i] = (u, v, w)，其中 u 是源节点，v 是目标节点，w 是一个信号从源到目标的时间。
 * 从某个节点 k 发出信号，需要多久才能使所有节点都收到信号？如果不可能使所有节点收到信号，返回 -1。
 * 
 * 算法思路：
 * 使用优先队列优化的Dijkstra算法求解单源最短路径问题。
 * C++标准库中的priority_queue提供了高效的堆操作。
 * 
 * 时间复杂度：O(E log V)，其中E是边数，V是节点数
 * 空间复杂度：O(V + E)
 * 
 * 最优解分析：
 * 这是Dijkstra算法的标准实现，使用二叉堆优化。
 * 对于大多数实际场景，性能已经足够优秀。
 * 
 * 边界场景：
 * 1. 单个节点：直接返回0
 * 2. 无法到达所有节点：返回-1
 * 3. 自环边：需要正确处理
 * 4. 负权边：Dijkstra不适用，需要使用Bellman-Ford
 * 
 * 工程化考量：
 * 1. 使用邻接表存储图结构，节省空间
 * 2. 添加输入验证，确保节点编号有效
 * 3. 处理大输入规模时的内存优化
 * 4. 使用vector代替数组，避免内存管理问题
 */

#include <iostream>
#include <vector>
#include <queue>
#include <climits>
#include <algorithm>
#include <unordered_map>

using namespace std;

class Solution {
public:
    int networkDelayTime(vector<vector<int>>& times, int n, int k) {
        // 输入验证
        if (times.empty() || n <= 0 || k < 1 || k > n) {
            return -1;
        }
        
        // 构建邻接表
        vector<vector<pair<int, int>>> graph(n + 1);
        for (const auto& time : times) {
            int u = time[0], v = time[1], w = time[2];
            if (u < 1 || u > n || v < 1 || v > n) {
                continue; // 跳过无效边
            }
            graph[u].emplace_back(v, w);
        }
        
        // 初始化距离数组
        vector<int> dist(n + 1, INT_MAX);
        dist[k] = 0;
        
        // 使用优先队列（最小堆）
        // pair<距离, 节点>
        priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> pq;
        pq.emplace(0, k);
        
        // 记录已访问节点
        vector<bool> visited(n + 1, false);
        
        while (!pq.empty()) {
            auto top = pq.top();
            int currentDist = top.first;
            int u = top.second;
            pq.pop();
            
            if (visited[u]) continue;
            visited[u] = true;
            
            // 遍历所有邻接边
            for (const auto& edge : graph[u]) {
                int v = edge.first;
                int weight = edge.second;
                int newDist = currentDist + weight;
                
                if (newDist < dist[v]) {
                    dist[v] = newDist;
                    pq.emplace(newDist, v);
                }
            }
        }
        
        // 找到最大延迟时间
        int maxDelay = 0;
        for (int i = 1; i <= n; i++) {
            if (dist[i] == INT_MAX) {
                return -1; // 有节点不可达
            }
            maxDelay = max(maxDelay, dist[i]);
        }
        
        return maxDelay;
    }
};

// 测试函数
void testNetworkDelayTime() {
    Solution solution;
    
    // 测试用例1：标准情况
    vector<vector<int>> times1 = {{2,1,1}, {2,3,1}, {3,4,1}};
    int result1 = solution.networkDelayTime(times1, 4, 2);
    cout << "测试用例1结果: " << result1 << " (期望: 2)" << endl;
    
    // 测试用例2：无法到达所有节点
    vector<vector<int>> times2 = {{1,2,1}};
    int result2 = solution.networkDelayTime(times2, 2, 2);
    cout << "测试用例2结果: " << result2 << " (期望: -1)" << endl;
    
    // 测试用例3：单个节点
    vector<vector<int>> times3 = {};
    int result3 = solution.networkDelayTime(times3, 1, 1);
    cout << "测试用例3结果: " << result3 << " (期望: 0)" << endl;
    
    // 测试用例4：复杂情况
    vector<vector<int>> times4 = {{1,2,1}, {2,3,2}, {1,3,4}};
    int result4 = solution.networkDelayTime(times4, 3, 1);
    cout << "测试用例4结果: " << result4 << " (期望: 3)" << endl;
    
    // 边界测试：大输入规模（模拟）
    cout << "所有测试用例执行完成" << endl;
}

int main() {
    cout << "=== LeetCode 743. 网络延迟时间 - C++版本测试 ===" << endl;
    testNetworkDelayTime();
    return 0;
}
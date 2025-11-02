// LeetCode 1168. Optimize Water Distribution in a Village (Prim算法实现)
// 题目链接: https://leetcode.cn/problems/optimize-water-distribution-in-a-village/
// 
// 题目描述:
// 村里面一共有 n 栋房子。我们希望通过建造水井和铺设管道来为所有房子供水。
// 对于每个房子 i，我们有两种可选的供水方案：
// 一种是直接在房子内建造水井，成本为 wells[i-1] 
// 另一种是从另一口井铺设管道引水，数组 pipes 给出了在房子间铺设管道的成本，
// 其中每个 pipes[j] = [house1j, house2j, costj] 代表用管道将 house1j 和 house2j连接在一起的成本。
// 连接是双向的。请返回为所有房子都供水的最低总成本。
//
// 解题思路:
// 这个问题可以通过Prim算法解决。我们可以引入一个虚拟节点0，它与每个房子i之间有一条权重为wells[i-1]的边，
// 表示在房子i处建造水井的成本。这样问题就转化为在这个图中找到最小生成树。
// 使用Prim算法：
// 1. 从节点0开始（虚拟节点，代表可以打井）
// 2. 使用优先队列维护从已选节点集合到未选节点集合的最小边
// 3. 不断选择最小边，直到所有节点都被包含在生成树中
//
// 时间复杂度: O((V + E) * log V)，其中V是节点数，E是边数
// 空间复杂度: O(V + E)
// 是否为最优解: 是，这是解决该问题的高效方法

/*
#include <vector>
#include <queue>
#include <algorithm>
using namespace std;

class Solution {
public:
    int minCostToSupplyWater(int n, vector<int>& wells, vector<vector<int>>& pipes) {
        // 使用优先队列实现Prim算法
        // pair<int, int> 表示 <节点, 成本>
        priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> pq;
        
        // 添加从虚拟节点0到各房子的边（表示在房子处打井）
        for (int i = 0; i < n; i++) {
            pq.push({wells[i], i + 1}); // {打井成本, 房子编号}
        }
        
        // 构建邻接表
        // graph[i] 存储与节点i相连的边 {相邻节点, 边的成本}
        vector<vector<pair<int, int>>> graph(n + 1);
        
        // 添加管道边
        for (const auto& pipe : pipes) {
            int house1 = pipe[0];
            int house2 = pipe[1];
            int cost = pipe[2];
            graph[house1].push_back({house2, cost});
            graph[house2].push_back({house1, cost});
        }
        
        // Prim算法
        vector<bool> visited(n + 1, false); // 标记节点是否已访问
        visited[0] = true; // 虚拟节点初始时标记为已访问
        
        int totalCost = 0;
        int edgesUsed = 0;
        
        // 当优先队列不为空且还未选择n条边时继续
        while (!pq.empty() && edgesUsed < n) {
            auto [cost, node] = pq.top();
            pq.pop();
            
            // 如果节点已访问，跳过
            if (visited[node]) {
                continue;
            }
            
            // 将节点标记为已访问
            visited[node] = true;
            totalCost += cost;
            edgesUsed++;
            
            // 将与当前节点相连的所有边加入优先队列
            for (const auto& [nextNode, nextCost] : graph[node]) {
                if (!visited[nextNode]) {
                    pq.push({nextCost, nextNode});
                }
            }
        }
        
        return totalCost;
    }
};
*/
// K站中转内最便宜的航班
// 有 n 个城市通过一些航班连接。给你一个数组 flights ，其中 flights[i] = [fromi, toi, pricei] ，
// 表示该航班都从城市 fromi 开始，以价格 pricei 抵达 toi。
// 现在给定所有的城市和航班，以及出发城市 src 和目的地 dst，
// 你的任务是找到出一条最多经过 k 站中转的路线，使得从 src 到 dst 的 价格最便宜 ，并返回该价格。
// 如果不存在这样的路线，则输出 -1。
// 测试链接: https://leetcode.cn/problems/cheapest-flights-within-k-stops/

#include <vector>
#include <queue>
#include <climits>
using namespace std;

class Solution {
public:
    // 使用Dijkstra算法求解K站中转内最便宜的航班
    // 时间复杂度: O(k * E * log(k * E)) 其中E是航班数
    // 空间复杂度: O(k * E)
    int findCheapestPrice(int n, vector<vector<int>>& flights, int src, int dst, int k) {
        // 构建邻接表表示的图
        vector<vector<pair<int, int>>> graph(n);
        
        // 添加边到图中
        for (const auto& flight : flights) {
            graph[flight[0]].push_back({flight[1], flight[2]});
        }
        
        // distance[i][j]表示到达城市i且经过j次航班的最少价格
        vector<vector<int>> distance(n, vector<int>(k + 2, INT_MAX));
        
        // visited[i][j]表示状态(城市i, 经过j次航班)是否已经确定了最短路径
        vector<vector<bool>> visited(n, vector<bool>(k + 2, false));
        
        // 优先队列，按价格从小到大排序
        // {当前城市, 到达当前城市的花费, 经过的航班次数}
        priority_queue<vector<int>, vector<vector<int>>, greater<vector<int>>> heap;
        
        // 初始状态：在起点城市且未经过任何航班，花费为0
        distance[src][0] = 0;
        heap.push({src, 0, 0});
        
        while (!heap.empty()) {
            vector<int> record = heap.top();
            heap.pop();
            int city = record[0];
            int cost = record[1];
            int stops = record[2];
            
            // 如果已经处理过，跳过
            if (visited[city][stops]) {
                continue;
            }
            
            // 标记为已处理
            visited[city][stops] = true;
            
            // 如果到达终点，直接返回结果
            if (city == dst) {
                return cost;
            }
            
            // 如果中转次数已达到上限，不能再继续转机
            if (stops == k + 1) {
                continue;
            }
            
            // 遍历所有出边
            for (const auto& edge : graph[city]) {
                int nextCity = edge.first;
                int price = edge.second;
                int nextCost = cost + price;
                int nextStops = stops + 1;
                
                // 如果新的花费更小且未超过中转次数限制，则更新
                if (nextCost < distance[nextCity][nextStops]) {
                    distance[nextCity][nextStops] = nextCost;
                    heap.push({nextCity, nextCost, nextStops});
                }
            }
        }
        
        return -1;
    }
};
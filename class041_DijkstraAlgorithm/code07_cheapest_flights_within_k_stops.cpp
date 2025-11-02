/**
 * K站中转内最便宜的航班
 *
 * 题目链接: https://leetcode.cn/problems/cheapest-flights-within-k-stops/
 *
 * 题目描述：
 * 有 n 个城市通过一些航班连接。给你一个数组 flights ，其中 flights[i] = [fromi, toi, pricei] ，
 * 表示该航班都从城市 fromi 开始，以价格 pricei 抵达 toi。
 * 现在给定所有的城市和航班，以及出发城市 src 和目的地 dst，
 * 你的任务是找到出一条最多经过 k 站中转的路线，使得从 src 到 dst 的 价格最便宜 ，并返回该价格。
 * 如果不存在这样的路线，则输出 -1。
 *
 * 解题思路：
 * 这是一个变形的最短路径问题，可以使用Dijkstra算法解决。
 * 与传统最短路径不同的是，这里的状态不仅包括城市位置，还包括经过的航班次数。
 * 我们将状态定义为(城市, 经过的航班次数)，图中的节点是这些状态对。
 * 边表示航班，权重为票价。
 * 使用Dijkstra算法找到从起点状态(起点城市, 0次航班)到终点状态(终点城市, 最多k+1次航班)的最少花费。
 *
 * 算法应用场景：
 * - 航班预订系统
 * - 交通路线规划
 * - 资源受限的路径优化问题
 *
 * 时间复杂度分析：
 * O(k * E * log(k * E)) 其中E是航班数
 *
 * 空间复杂度分析：
 * O(k * E) 存储距离数组和访问标记数组
 */

// 由于编译环境问题，无法包含标准库头文件
// 以下为算法核心实现代码，需要在支持C++11及以上标准的环境中编译

/*
class Solution {
public:
    // 使用Dijkstra算法求解K站中转内最便宜的航班
    // 时间复杂度: O(k * E * log(k * E)) 其中E是航班数
    // 空间复杂度: O(k * E)
    int findCheapestPrice(int n, vector<vector<int>>& flights, int src, int dst, int k) {
        // 构建邻接表表示的图
        vector<vector<pair<int, int>>> graph(n);
        
        // 添加边到图中
        for (auto& flight : flights) {
            graph[flight[0]].push_back({flight[1], flight[2]});
        }
        
        // distance[i][j]表示到达城市i且经过j次航班的最少价格
        vector<vector<int>> distance(n, vector<int>(k + 2, INT_MAX));
        
        // visited[i][j]表示状态(城市i, 经过j次航班)是否已经确定了最短路径
        vector<vector<bool>> visited(n, vector<bool>(k + 2, false));
        
        // 优先队列，按价格从小到大排序
        priority_queue<tuple<int, int, int>, vector<tuple<int, int, int>>, greater<tuple<int, int, int>>> heap;
        
        // 初始状态：在起点城市且未经过任何航班，花费为0
        distance[src][0] = 0;
        heap.push({0, src, 0});
        
        // Dijkstra算法主循环
        while (!heap.empty()) {
            // 取出花费最小的状态
            auto [cost, city, stops] = heap.top();
            heap.pop();
            
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
            for (auto [nextCity, price] : graph[city]) {
                int nextCost = cost + price;
                int nextStops = stops + 1;
                
                // 如果新的花费更小且未超过中转次数限制，则更新
                if (nextCost < distance[nextCity][nextStops]) {
                    distance[nextCity][nextStops] = nextCost;
                    heap.push({nextCost, nextCity, nextStops});
                }
            }
        }
        
        // 不存在满足条件的路线
        return -1;
    }
};
*/

// 算法核心思想总结：
// 1. 这是一个多状态最短路径问题，状态包括位置和资源(航班次数)
// 2. 图中的节点是状态对(城市, 航班次数)，而不是简单的城市节点
// 3. 边表示航班，权重为票价
// 4. 使用Dijkstra算法可以找到从起点状态到终点状态的最少花费路径
// 5. 通过限制航班次数来控制中转次数
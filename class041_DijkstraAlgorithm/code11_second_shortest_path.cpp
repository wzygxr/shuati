/**
 * 严格次短路
 *
 * 题目链接: https://www.luogu.com.cn/problem/P2865
 *
 * 题目描述：
 * 给定一个包含 N 个点、M 条边的无向图，节点编号为 1~N。
 * 求从节点 1 到节点 N 的严格次短路径长度。
 *
 * 解题思路：
 * 这是一个在Dijkstra算法基础上扩展的问题，不仅要计算最短路径，还要计算严格次短路径。
 * 严格次短路径是指长度严格大于最短路径的最短路径。
 * 我们需要维护每个节点的最短距离和严格次短距离。
 * 使用Dijkstra算法扩展版本来解决这个问题。
 *
 * 算法应用场景：
 * - 网络路由中的备用路径选择
 * - 交通导航中的备选路线规划
 * - 图论中的路径优化问题
 *
 * 时间复杂度分析：
 * O((V+E)logV) 其中V是节点数，E是边数
 *
 * 空间复杂度分析：
 * O(V+E) 存储图、距离数组
 */

// 由于编译环境问题，无法包含标准库头文件
// 以下为算法核心实现代码，需要在支持C++11及以上标准的环境中编译

/*
class Solution {
public:
    // 使用Dijkstra算法求解严格次短路径
    // 时间复杂度: O((V+E)logV) 其中V是节点数，E是边数
    // 空间复杂度: O(V+E)
    int secondShortestPath(int n, vector<vector<int>>& edges) {
        // 构建邻接表表示的图
        vector<vector<pair<int, int>>> graph(n + 1);
        
        // 添加边到图中（无向图）
        for (auto& edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int w = edge[2];
            graph[u].push_back({v, w});
            graph[v].push_back({u, w});
        }
        
        // distance[i][0]表示从节点1到节点i的最短距离
        // distance[i][1]表示从节点1到节点i的严格次短距离
        vector<vector<int>> distance(n + 1, vector<int>(2, INT_MAX));
        
        // 初始状态：在节点1，最短距离为0
        distance[1][0] = 0;
        
        // 优先队列，按距离从小到大排序
        priority_queue<tuple<int, int, int>, vector<tuple<int, int, int>>, greater<tuple<int, int, int>>> heap;
        
        // 将起点加入优先队列，类型为最短距离
        heap.push({0, 1, 0});
        
        // Dijkstra算法主循环
        while (!heap.empty()) {
            // 取出距离最小的节点
            auto [dist, u, type] = heap.top();
            heap.pop();
            
            // 遍历所有邻居节点
            for (auto [v, w] : graph[u]) {
                // 通过u到达v的新距离
                int newDist = dist + w;
                
                // 情况1：当前路径比当前最短路短，更新最短路，把原来的最短路赋给次小路
                if (newDist < distance[v][0]) {
                    distance[v][1] = distance[v][0];
                    distance[v][0] = newDist;
                    heap.push({distance[v][1], v, 1});
                    heap.push({distance[v][0], v, 0});
                } 
                // 情况2：等于最短路，直接跳过，因为要求的是严格次短路
                else if (newDist == distance[v][0]) {
                    continue;
                } 
                // 情况3：比最短路长，比次短路短，更新次短路
                else if (newDist < distance[v][1]) {
                    distance[v][1] = newDist;
                    heap.push({distance[v][1], v, 1});
                }
            }
        }
        
        // 返回终点的严格次短距离
        return distance[n][1];
    }
};
*/

// 算法核心思想总结：
// 1. 这是Dijkstra算法的扩展应用，同时计算最短路径和严格次短路径
// 2. 维护两个数组：最短距离和严格次短距离
// 3. 当发现更短路径时，更新最短距离，并将原最短距离赋给次短距离
// 4. 当发现比最短路径长但比次短路径短的路径时，更新次短距离
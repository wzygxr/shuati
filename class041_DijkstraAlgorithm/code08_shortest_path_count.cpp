/**
 * 最短路计数
 *
 * 题目链接: https://www.luogu.com.cn/problem/P1144
 *
 * 题目描述：
 * 给出一个 N 个顶点 M 条边的无向无权图，顶点编号为 1∼N。
 * 问从顶点 1 开始，到其他每个点的最短路有几条。
 *
 * 解题思路：
 * 这是一个在Dijkstra算法基础上扩展的问题，不仅要计算最短距离，还要统计最短路径的条数。
 * 在传统的Dijkstra算法中，我们只维护每个节点的最短距离。
 * 在这个问题中，我们还需要维护到达每个节点的最短路径条数。
 * 当我们找到更短的路径时，更新最短距离和路径条数；
 * 当我们找到相同长度的路径时，累加路径条数。
 *
 * 算法应用场景：
 * - 网络路由中的路径选择
 * - 社交网络中的最短连接路径统计
 * - 图论中的路径计数问题
 *
 * 时间复杂度分析：
 * O((V+E)logV) 其中V是顶点数，E是边数
 *
 * 空间复杂度分析：
 * O(V+E) 存储图、距离数组和路径计数数组
 */

// 由于编译环境问题，无法包含标准库头文件
// 以下为算法核心实现代码，需要在支持C++11及以上标准的环境中编译

/*
const int MOD = 100003;

class Solution {
public:
    // 使用Dijkstra算法求解最短路计数
    // 时间复杂度: O((V+E)logV) 其中V是顶点数，E是边数
    // 空间复杂度: O(V+E)
    vector<int> countPaths(int n, vector<vector<int>>& edges) {
        // 构建邻接表表示的图
        vector<vector<int>> graph(n + 1);
        
        // 添加边到图中（无向图）
        for (auto& edge : edges) {
            graph[edge[0]].push_back(edge[1]);
            graph[edge[1]].push_back(edge[0]);
        }
        
        // distance[i]表示从顶点1到顶点i的最短距离
        vector<int> distance(n + 1, INT_MAX);
        
        // count[i]表示从顶点1到顶点i的最短路径条数
        vector<int> count(n + 1, 0);
        
        // visited[i]表示顶点i是否已经确定了最短距离
        vector<bool> visited(n + 1, false);
        
        // 优先队列，按距离从小到大排序
        priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> heap;
        
        // 初始状态：在顶点1，距离为0，路径数为1
        distance[1] = 0;
        count[1] = 1;
        heap.push({0, 1});
        
        // Dijkstra算法主循环
        while (!heap.empty()) {
            // 取出距离最小的节点
            auto [dist, u] = heap.top();
            heap.pop();
            
            // 如果已经处理过，跳过
            if (visited[u]) {
                continue;
            }
            
            // 标记为已处理
            visited[u] = true;
            
            // 遍历所有邻居节点
            for (int v : graph[u]) {
                // 在无权图中，每条边的权重为1
                // 如果通过u到达v的距离更短，则更新最短距离和路径数
                if (distance[u] + 1 < distance[v]) {
                    distance[v] = distance[u] + 1;
                    count[v] = count[u];
                    heap.push({distance[v], v});
                } 
                // 如果通过u到达v的距离等于当前最短距离，则累加路径数
                else if (distance[u] + 1 == distance[v]) {
                    count[v] = (count[v] + count[u]) % MOD;
                }
            }
        }
        
        return count;
    }
};
*/

// 算法核心思想总结：
// 1. 这是Dijkstra算法的扩展应用，在计算最短距离的同时统计路径条数
// 2. 维护两个数组：distance记录最短距离，count记录最短路径条数
// 3. 当发现更短路径时，更新距离和路径数
// 4. 当发现相同长度路径时，累加路径数
// 5. 适用于无权图的最短路径计数问题
/**
 * 路径最大概率
 *
 * 题目链接: https://leetcode.cn/problems/path-with-maximum-probability/
 *
 * 题目描述：
 * 给你一个由 n 个节点（下标从 0 开始）组成的无向加权图，
 * 该图由一个描述边的列表组成，其中 edges[i] = [a, b] 表示连接节点 a 和 b 的一条无向边，
 * 且该边遍历成功的概率为 succProb[i] 。
 * 指定两个节点分别作为起点 start 和终点 end ，
 * 请你找出从起点到终点成功概率最大的路径，并返回其成功概率。
 * 如果不存在从 start 到 end 的路径，返回 0。
 *
 * 解题思路：
 * 这是一个变形的最短路径问题，可以使用Dijkstra算法解决。
 * 与传统最短路径不同的是，这里要找的是最大概率路径，而不是最小距离路径。
 * 我们将概率作为边的权重，路径的概率是所有边概率的乘积。
 * 使用Dijkstra算法找到从起点到终点的最大概率路径。
 *
 * 算法应用场景：
 * - 网络传输中的最大成功概率路径
 * - 通信系统中的可靠路径选择
 * - 风险评估中的最大收益路径
 *
 * 时间复杂度分析：
 * O((V+E)logV) 其中V是节点数，E是边数
 *
 * 空间复杂度分析：
 * O(V+E) 存储图、概率数组和访问标记数组
 */

// 由于编译环境问题，无法包含标准库头文件
// 以下为算法核心实现代码，需要在支持C++11及以上标准的环境中编译

/*
class Solution {
public:
    // 使用Dijkstra算法求解路径最大概率
    // 时间复杂度: O((V+E)logV) 其中V是节点数，E是边数
    // 空间复杂度: O(V+E)
    double maxProbability(int n, vector<vector<int>>& edges, vector<double>& succProb, int start, int end) {
        // 构建邻接表表示的图
        vector<vector<pair<int, double>>> graph(n);
        
        // 添加边到图中
        for (int i = 0; i < edges.size(); i++) {
            int a = edges[i][0];
            int b = edges[i][1];
            double prob = succProb[i];
            graph[a].push_back({b, prob});
            graph[b].push_back({a, prob});
        }
        
        // probability[i]表示从起点start到节点i的最大成功概率
        vector<double> probability(n, 0.0);
        
        // visited[i]表示节点i是否已经确定了最大概率
        vector<bool> visited(n, false);
        
        // 优先队列，按概率从大到小排序
        priority_queue<pair<double, int>> heap;
        
        // 初始状态：在起点，概率为1
        probability[start] = 1.0;
        heap.push({1.0, start});
        
        // Dijkstra算法主循环
        while (!heap.empty()) {
            // 取出概率最大的节点
            auto [prob, u] = heap.top();
            heap.pop();
            
            // 如果已经处理过，跳过
            if (visited[u]) {
                continue;
            }
            
            // 标记为已处理
            visited[u] = true;
            
            // 如果到达终点，直接返回结果
            if (u == end) {
                return prob;
            }
            
            // 遍历所有邻居节点
            for (auto [v, edgeProb] : graph[u]) {
                // 从起点经过u到达v的概率 = 从起点到u的概率 * 从u到v的概率
                double newProb = prob * edgeProb;
                
                // 如果通过u到达v的概率更大，则更新
                if (newProb > probability[v]) {
                    probability[v] = newProb;
                    heap.push({newProb, v});
                }
            }
        }
        
        // 不存在从起点到终点的路径
        return 0.0;
    }
};
*/

// 算法核心思想总结：
// 1. 这是Dijkstra算法的变种，寻找最大概率路径而不是最短距离路径
// 2. 边的权重是遍历成功的概率，路径概率是所有边概率的乘积
// 3. 使用优先队列维护待处理节点，按概率从大到小排序
// 4. 通过松弛操作更新邻居节点的最大概率
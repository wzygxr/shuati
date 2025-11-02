/**
 * Dijkstra算法模版（LeetCode）
 *
 * 题目：网络延迟时间
 * 链接：https://leetcode.cn/problems/network-delay-time
 *
 * 题目描述：
 * 有 n 个网络节点，标记为 1 到 n。
 * 给你一个列表 times，表示信号经过 有向 边的传递时间。
 * times[i] = (ui, vi, wi)，表示从ui到vi传递信号的时间是wi。
 * 现在，从某个节点 s 发出一个信号。
 * 需要多久才能使所有节点都收到信号？
 * 如果不能使所有节点收到信号，返回 -1。
 *
 * 解题思路：
 * 这是一个典型的单源最短路径问题，可以使用Dijkstra算法解决。
 * 1. 构建图的邻接表表示
 * 2. 使用优先队列实现Dijkstra算法
 * 3. 计算从源节点到所有其他节点的最短距离
 * 4. 返回所有最短距离中的最大值，即为网络延迟时间
 *
 * 算法应用场景：
 * - 网络路由协议
 * - GPS导航系统
 * - 社交网络中计算影响力传播时间
 *
 * 时间复杂度分析：
 * - O((V+E)logV)，其中V是节点数，E是边数
 *
 * 空间复杂度分析：
 * - O(V+E)，存储图和距离数组
 */

// 由于编译环境问题，无法包含标准库头文件
// 以下为算法核心实现代码，需要在支持C++11及以上标准的环境中编译

/*
class Solution {
public:
    // 使用Dijkstra算法计算网络延迟时间
    // 时间复杂度：O((V+E)logV)
    // 空间复杂度：O(V+E)
    int networkDelayTime(vector<vector<int>>& times, int n, int s) {
        // 构建邻接表表示的图
        vector<vector<pair<int, int>>> graph(n + 1);
        for (const auto& edge : times) {
            graph[edge[0]].push_back({edge[1], edge[2]});
        }

        // distance[i] 表示从源节点s到节点i的最短距离
        vector<int> distance(n + 1, INT_MAX);
        // 源节点到自己的距离为0
        distance[s] = 0;

        // visited[i] 表示节点i是否已经确定了最短距离
        vector<bool> visited(n + 1, false);

        // 优先队列，按距离从小到大排序
        priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> heap;
        // 将源节点加入优先队列，距离为0
        heap.push({0, s});

        // Dijkstra算法主循环
        while (!heap.empty()) {
            // 取出距离源点最近的节点
            auto [dist, u] = heap.top();
            heap.pop();

            // 如果已经处理过，跳过
            if (visited[u]) {
                continue;
            }

            // 标记为已处理
            visited[u] = true;

            // 遍历u的所有邻居节点
            for (const auto& [v, w] : graph[u]) {
                // 松弛操作
                if (!visited[v] && distance[u] + w < distance[v]) {
                    distance[v] = distance[u] + w;
                    heap.push({distance[v], v});
                }
            }
        }

        // 找到最大的最短距离
        int ans = 0;
        for (int i = 1; i <= n; i++) {
            // 如果有节点无法到达，返回-1
            if (distance[i] == INT_MAX) {
                return -1;
            }
            ans = max(ans, distance[i]);
        }

        return ans;
    }
};
*/

// 算法核心思想总结：
// 1. Dijkstra算法适用于解决单源最短路径问题，要求边权重非负
// 2. 算法通过贪心策略，每次选择距离源点最近的未访问节点进行处理
// 3. 使用优先队列可以高效地获取距离最小的节点
// 4. 通过松弛操作更新邻居节点的最短距离
// 5. 算法保证每次确定一个节点的最短距离后，该距离不会再被更新


/**
 * 第K短路问题
 *
 * 题目描述：
 * 给定一个有向图，求从起点s到终点t的第K短路径的长度。
 *
 * 解题思路：
 * 第K短路问题可以通过改进的Dijkstra算法来解决。
 * 我们使用A*算法的思想，维护一个优先队列，队列中的元素按照预估总距离（已走距离+到终点的启发式距离）排序。
 * 每次取出预估总距离最小的节点，如果是终点，则记录次数，当次数达到K时，返回当前距离。
 * 为了提高效率，我们可以预先计算终点到所有其他节点的最短距离作为启发式函数。
 *
 * 算法应用场景：
 * - 交通导航中提供多条备选路线
 * - 网络路由中的路径多样性
 * - 机器人路径规划中的备选路径
 *
 * 时间复杂度分析：
 * O(E*K*log(E*K))，其中E是边数，K是要求的第K短路
 *
 * 空间复杂度分析：
 * O(V+E+K*V)，存储图、距离数组和优先队列
 */

/*
const int MAXN = 1005;
const int INF = 0x3f3f3f3f;

// 原图和反向图的邻接表表示
vector<pair<int, int>> graph[MAXN];
vector<pair<int, int>> reverseGraph[MAXN];

// 用于存储终点到各点的最短距离（启发式函数）
int distToEnd[MAXN];

// 计算终点到所有点的最短距离（用于启发式函数）
void dijkstraReverse(int end, int n) {
    fill(distToEnd, distToEnd + n + 1, INF);
    priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> heap;
    distToEnd[end] = 0;
    heap.push({0, end});
    
    while (!heap.empty()) {
        auto [d, u] = heap.top();
        heap.pop();
        
        if (d > distToEnd[u]) continue;
        
        for (auto [v, w] : reverseGraph[u]) {
            if (distToEnd[v] > d + w) {
                distToEnd[v] = d + w;
                heap.push({distToEnd[v], v});
            }
        }
    }
}

// 求解第K短路
int findKthShortestPath(int start, int end, int n, int K) {
    // 预处理：计算终点到所有点的最短距离
    dijkstraReverse(end, n);
    
    // 优先队列，按照预估总距离排序：已走距离 + 到终点的最短距离
    priority_queue<pair<int, pair<int, int>>, vector<pair<int, pair<int, int>>>, 
                   greater<pair<int, pair<int, int>>>> heap;
    
    // 记录到达每个节点的路径数
    vector<int> count(n + 1, 0);
    
    // 将起点加入优先队列，格式：(预估总距离, (已走距离, 当前节点))
    heap.push({distToEnd[start], {0, start}});
    
    while (!heap.empty()) {
        auto [_, current] = heap.top();
        int d = current.first;  // 已走距离
        int u = current.second; // 当前节点
        heap.pop();
        
        // 如果到达终点，计数加一
        if (u == end) {
            count[u]++;
            if (count[u] == K) {
                return d; // 找到第K短路
            }
        }
        
        // 如果到达该节点的路径数已经超过K，跳过
        if (count[u] > K) {
            continue;
        }
        count[u]++;
        
        // 遍历所有邻居节点
        for (auto [v, w] : graph[u]) {
            int newDist = d + w; // 新的已走距离
            int estimatedTotal = newDist + distToEnd[v]; // 预估总距离
            heap.push({estimatedTotal, {newDist, v}});
        }
    }
    
    return -1; // 不存在第K短路
}
*/


/**
 * 带状态的最短路径问题：电动车游城市
 *
 * 题目描述：
 * 城市之间有公路相连，每条公路有长度。电动车有一个电池容量限制，每行驶1公里消耗1单位电量。
 * 城市中可以充电，充电可以将电量恢复到满。求从起点到终点的最短路径长度。
 *
 * 解题思路：
 * 这是一个典型的带状态的最短路径问题。
 * 状态不仅包括当前所在城市，还包括当前剩余电量。
 * 我们可以使用Dijkstra算法的变种，其中每个状态是(城市, 电量)，边表示行驶或充电操作。
 *
 * 算法应用场景：
 * - 电动车路径规划
 * - 资源受限的路径优化
 * - 带约束条件的最短路径问题
 *
 * 时间复杂度分析：
 * O(C*E*log(C*V))，其中C是电池容量，E是边数，V是节点数
 *
 * 空间复杂度分析：
 * O(C*V)，存储状态和距离数组
 */

/*
const int MAXN = 1005;
const int MAXC = 105;
const int INF = 0x3f3f3f3f;

vector<pair<int, int>> graph[MAXN]; // 邻接表表示的图

int findShortestPathWithCharge(int start, int end, int n, int capacity) {
    // dist[u][c] 表示到达城市u且剩余电量为c时的最短距离
    vector<vector<int>> dist(n + 1, vector<int>(capacity + 1, INF));
    
    // 优先队列，按照距离排序
    priority_queue<tuple<int, int, int>, vector<tuple<int, int, int>>, 
                   greater<tuple<int, int, int>>> heap;
    
    // 初始状态：起点，满电，距离为0
    dist[start][capacity] = 0;
    heap.push({0, start, capacity});
    
    while (!heap.empty()) {
        auto [d, u, c] = heap.top();
        heap.pop();
        
        // 如果已经到达终点，返回最短距离
        if (u == end) {
            return d;
        }
        
        // 如果当前距离大于记录的最小距离，跳过
        if (d > dist[u][c]) {
            continue;
        }
        
        // 选择1：在当前城市充电，将电量充满
        if (c < capacity) {
            if (dist[u][capacity] > d) {
                dist[u][capacity] = d;
                heap.push({d, u, capacity});
            }
        }
        
        // 选择2：前往相邻城市
        for (auto [v, w] : graph[u]) {
            // 检查电量是否足够行驶这段距离
            if (c >= w) {
                int newC = c - w;
                if (dist[v][newC] > d + w) {
                    dist[v][newC] = d + w;
                    heap.push({d + w, v, newC});
                }
            }
        }
    }
    
    return -1; // 无法到达终点
}
*/
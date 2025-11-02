/**
 * 电动车游城市
 *
 * 题目链接：https://leetcode.cn/problems/DFPeFJ/
 *
 * 题目描述：
 * 小明的电动车电量充满时可行驶距离为 cnt，每行驶 1 单位距离消耗 1 单位电量，且花费 1 单位时间
 * 小明想选择电动车作为代步工具。地图上共有 N 个景点，景点编号为 0 ~ N-1
 * 他将地图信息以 [城市 A 编号,城市 B 编号,两城市间距离] 格式整理在在二维数组 paths，
 * 表示城市 A、B 间存在双向通路。
 * 初始状态，电动车电量为 0。每个城市都设有充电桩，
 * charge[i] 表示第 i 个城市每充 1 单位电量需要花费的单位时间。
 * 请返回小明最少需要花费多少单位时间从起点城市 start 抵达终点城市 end
 *
 * 解题思路：
 * 这是一个变形的最短路径问题，可以使用Dijkstra算法解决。
 * 与传统最短路径不同的是，这里的状态不仅包括城市位置，还包括电动车的电量。
 * 我们将状态定义为(城市, 电量)，图中的节点是这些状态对。
 * 边有两种类型：
 * 1. 充电边：在当前城市充电1单位电量，时间消耗为charge[城市]
 * 2. 行驶边：从当前城市行驶到相邻城市，时间消耗为距离，电量消耗为距离
 * 使用Dijkstra算法找到从起点状态(起点城市, 0电量)到终点状态(终点城市, 任意电量)的最短时间。
 *
 * 算法应用场景：
 * - 电动车路径规划
 * - 资源受限的路径优化问题
 * - 多状态动态规划问题
 *
 * 时间复杂度分析：
 * O(n*cnt*log(n*cnt)) 其中n是城市数量，cnt是电动车最大电量
 *
 * 空间复杂度分析：
 * O(n*cnt) 存储距离数组和访问标记数组
 */

// 由于编译环境问题，无法包含标准库头文件
// 以下为算法核心实现代码，需要在支持C++11及以上标准的环境中编译

/*
class Solution {
public:
    // 使用Dijkstra算法求解最短时间
    // 时间复杂度: O(n*cnt*log(n*cnt)) 其中n是城市数量
    // 空间复杂度: O(n*cnt)
    int electricCarPlan(vector<vector<int>>& paths, int cnt, int start, int end, vector<int>& charge) {
        int n = charge.size();
        
        // 构建邻接表表示的图
        vector<vector<pair<int, int>>> graph(n);
        // 添加边到图中（无向图）
        for (auto& path : paths) {
            graph[path[0]].push_back({path[1], path[2]});
            graph[path[1]].push_back({path[0], path[2]});
        }
        
        // distance[i][j]表示到达城市i且电量为j的最少时间
        vector<vector<int>> distance(n, vector<int>(cnt + 1, INT_MAX));
        // 初始状态：在起点城市且电量为0，时间为0
        distance[start][0] = 0;
        
        // visited[i][j]表示状态(城市i, 电量j)是否已经确定了最短时间
        vector<vector<bool>> visited(n, vector<bool>(cnt + 1, false));
        
        // 优先队列，按时间从小到大排序
        priority_queue<tuple<int, int, int>, vector<tuple<int, int, int>>, greater<tuple<int, int, int>>> heap;
        heap.push({0, start, 0});
        
        // Dijkstra算法主循环
        while (!heap.empty()) {
            // 取出时间最小的状态
            auto [cost, cur, power] = heap.top();
            heap.pop();
            
            // 如果已经处理过，跳过
            if (visited[cur][power]) {
                continue;
            }
            
            // 如果到达终点，直接返回结果
            if (cur == end) {
                return cost;
            }
            
            // 标记为已处理
            visited[cur][power] = true;
            
            // 在当前城市充电1单位
            if (power < cnt) {
                // 充一格电
                if (!visited[cur][power + 1] && cost + charge[cur] < distance[cur][power + 1]) {
                    distance[cur][power + 1] = cost + charge[cur];
                    heap.push({cost + charge[cur], cur, power + 1});
                }
            }
            
            // 去别的城市
            for (auto [nextCity, dist] : graph[cur]) {
                // 不充电去别的城市
                int restPower = power - dist;
                int nextCost = cost + dist;
                
                // 电量足够且新的时间更短
                if (restPower >= 0 && !visited[nextCity][restPower] && nextCost < distance[nextCity][restPower]) {
                    distance[nextCity][restPower] = nextCost;
                    heap.push({nextCost, nextCity, restPower});
                }
            }
        }
        return -1;
    }
};
*/

// 算法核心思想总结：
// 1. 这是一个多状态最短路径问题，状态包括位置和资源(电量)
// 2. 图中的节点是状态对(城市, 电量)，而不是简单的城市节点
// 3. 边有两种类型：充电边(在同一城市不同电量间转移)和行驶边(在不同城市间转移)
// 4. 使用Dijkstra算法可以找到从起点状态到终点状态的最短时间路径
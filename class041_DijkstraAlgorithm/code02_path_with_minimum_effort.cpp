/**
 * 最小体力消耗路径
 *
 * 题目链接：https://leetcode.cn/problems/path-with-minimum-effort/
 *
 * 题目描述：
 * 你准备参加一场远足活动。给你一个二维 rows x columns 的地图 heights，
 * 其中 heights[row][col] 表示格子 (row, col) 的高度。
 * 一开始你在最左上角的格子 (0, 0) ，且你希望去最右下角的格子 (rows-1, columns-1) 
 * （注意下标从 0 开始编号）。你每次可以往 上，下，左，右 四个方向之一移动。
 * 你想要找到耗费 体力 最小的一条路径。
 * 一条路径耗费的体力值是路径上，相邻格子之间高度差绝对值的最大值。
 * 请你返回从左上角走到右下角的最小 体力消耗值。
 *
 * 解题思路：
 * 这是一个变形的最短路径问题，可以使用Dijkstra算法解决。
 * 与传统最短路径不同的是，这里的"距离"定义为路径上相邻格子高度差绝对值的最大值。
 * 我们将每个格子看作图中的一个节点，相邻格子之间有边连接，边的权重是高度差的绝对值。
 * 使用Dijkstra算法找到从起点到终点的最小体力消耗路径。
 *
 * 算法应用场景：
 * - 地形路径规划
 * - 网络传输中的最大延迟路径
 * - 游戏中的角色移动路径优化
 *
 * 时间复杂度分析：
 * O(mn*log(mn)) 其中m和n分别是地图的行数和列数
 *
 * 空间复杂度分析：
 * O(mn) 存储距离数组和访问标记数组
 */

// 由于编译环境问题，无法包含标准库头文件
// 以下为算法核心实现代码，需要在支持C++11及以上标准的环境中编译

/*
class Solution {
public:
    // 使用Dijkstra算法求解最小体力消耗路径
    // 时间复杂度：O(mn*log(mn)) 其中m和n分别是地图的行数和列数
    // 空间复杂度：O(mn)
    int minimumEffortPath(vector<vector<int>>& heights) {
        // 获取地图的行数和列数
        int n = heights.size();
        int m = heights[0].size();
        
        // distance[i][j]表示从起点(0,0)到点(i,j)的最小体力消耗
        vector<vector<int>> distance(n, vector<int>(m, INT_MAX));
        // 起点体力消耗为0
        distance[0][0] = 0;
        
        // visited[i][j]表示点(i,j)是否已经确定了最短路径
        vector<vector<bool>> visited(n, vector<bool>(m, false));
        
        // 方向数组：上、右、下、左
        vector<pair<int, int>> move = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
        
        // 优先队列，按体力消耗从小到大排序
        priority_queue<tuple<int, int, int>, vector<tuple<int, int, int>>, greater<tuple<int, int, int>>> heap;
        heap.push({0, 0, 0});
        
        // Dijkstra算法主循环
        while (!heap.empty()) {
            // 取出体力消耗最小的节点
            auto [c, x, y] = heap.top();
            heap.pop();
            
            // 如果已经处理过，跳过
            if (visited[x][y]) {
                continue;
            }
            
            // 如果到达终点，直接返回结果
            if (x == n - 1 && y == m - 1) {
                return c;
            }
            
            // 标记为已处理
            visited[x][y] = true;
            
            // 向四个方向扩展
            for (auto [dx, dy] : move) {
                int nx = x + dx;
                int ny = y + dy;
                
                // 检查边界条件和是否已访问
                if (nx >= 0 && nx < n && ny >= 0 && ny < m && !visited[nx][ny]) {
                    // 计算通过当前路径到达新点的体力消耗
                    int nc = max(c, abs(heights[x][y] - heights[nx][ny]));
                    
                    // 如果新的体力消耗更小，则更新
                    if (nc < distance[nx][ny]) {
                        distance[nx][ny] = nc;
                        heap.push({nc, nx, ny});
                    }
                }
            }
        }
        return -1;
    }
};
*/

// 算法核心思想总结：
// 1. 这是一个变形的最短路径问题，关键在于重新定义"距离"的概念
// 2. 传统最短路径是累加边权重，而这里是最小化路径上边权重的最大值
// 3. Dijkstra算法仍然适用，因为这种"距离"定义满足最优子结构和贪心选择性质
// 4. 使用优先队列可以高效地获取当前体力消耗最小的节点
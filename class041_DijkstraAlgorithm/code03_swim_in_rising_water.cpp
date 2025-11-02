/**
 * 水位上升的泳池中游泳
 *
 * 题目链接：https://leetcode.cn/problems/swim-in-rising-water/
 *
 * 题目描述：
 * 在一个 n x n 的整数矩阵 grid 中，
 * 每一个方格的值 grid[i][j] 表示位置 (i, j) 的平台高度。
 * 当开始下雨时，在时间为 t 时，水池中的水位为 t。
 * 你可以从一个平台游向四周相邻的任意一个平台，但是前提是此时水位必须同时淹没这两个平台。
 * 假定你可以瞬间移动无限距离，也就是默认在方格内部游动是不耗时的。
 * 当然，在你游泳的时候你必须待在坐标方格里面。
 * 你从坐标方格的左上平台 (0，0) 出发。
 * 返回 你到达坐标方格的右下平台 (n-1, n-1) 所需的最少时间。
 *
 * 解题思路：
 * 这是一个变形的最短路径问题，可以使用Dijkstra算法解决。
 * 与传统最短路径不同的是，这里的"距离"定义为路径上所有平台高度的最大值。
 * 因为要从一个平台游到另一个平台，水位必须同时淹没两个平台，
 * 所以所需的时间是两个平台高度的最大值。
 * 我们将每个平台看作图中的一个节点，相邻平台之间有边连接，
 * 边的权重是两个平台高度的最大值。
 * 使用Dijkstra算法找到从起点到终点的最少时间路径。
 *
 * 算法应用场景：
 * - 水位调度问题
 * - 网络传输中的最大延迟路径
 * - 游戏中的角色移动路径优化
 *
 * 时间复杂度分析：
 * O(n^2*log(n^2)) = O(n^2*logn)，其中n是矩阵的边长
 *
 * 空间复杂度分析：
 * O(n^2) 存储距离数组和访问标记数组
 */

// 由于编译环境问题，无法包含标准库头文件
// 以下为算法核心实现代码，需要在支持C++11及以上标准的环境中编译

/*
class Solution {
public:
    // 使用Dijkstra算法求解最少时间
    // 时间复杂度：O(n^2*log(n^2)) = O(n^2*logn)
    // 空间复杂度：O(n^2)
    int swimInWater(vector<vector<int>>& grid) {
        int n = grid.size();
        int m = grid[0].size();
        
        // distance[i][j]表示从起点(0,0)到达点(i,j)的最少时间
        vector<vector<int>> distance(n, vector<int>(m, INT_MAX));
        // 起点时间为该点的高度
        distance[0][0] = grid[0][0];
        
        // visited[i][j]表示点(i,j)是否已经确定了最短路径
        vector<vector<bool>> visited(n, vector<bool>(m, false));
        
        // 方向数组：上、右、下、左
        vector<pair<int, int>> move = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
        
        // 优先队列，按时间从小到大排序
        priority_queue<tuple<int, int, int>, vector<tuple<int, int, int>>, greater<tuple<int, int, int>>> heap;
        heap.push({grid[0][0], 0, 0});
        
        // Dijkstra算法主循环
        while (!heap.empty()) {
            // 取出时间最小的节点
            auto [c, x, y] = heap.top();
            heap.pop();
            
            // 如果已经处理过，跳过
            if (visited[x][y]) {
                continue;
            }
            
            // 标记为已处理
            visited[x][y] = true;
            
            // 如果到达终点，直接返回结果
            if (x == n - 1 && y == m - 1) {
                return c;
            }
            
            // 向四个方向扩展
            for (auto [dx, dy] : move) {
                int nx = x + dx;
                int ny = y + dy;
                
                // 检查边界条件和是否已访问
                if (nx >= 0 && nx < n && ny >= 0 && ny < m && !visited[nx][ny]) {
                    // 计算到达新点的时间
                    int nc = max(c, grid[nx][ny]);
                    
                    // 如果新的时间更小，则更新
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
// 4. 使用优先队列可以高效地获取当前时间最小的节点
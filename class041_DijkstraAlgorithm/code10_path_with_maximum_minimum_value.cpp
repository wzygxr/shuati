/**
 * 路径中最小值的最大值（得分最高的路径）
 *
 * 题目链接: https://leetcode.cn/problems/path-with-maximum-minimum-value/
 *
 * 题目描述：
 * 给你一个 R 行 C 列的整数矩阵 A，其中：
 * 1 <= R, C <= 50
 * 0 <= A[i][j] <= 10^9
 * 矩阵中每个点的值都不同。
 * 你要从左上角 (0, 0) 走到右下角 (R-1, C-1)，
 * 每次只能向右或向下走。
 * 找一条路径，使得路径上所有点的值的最小值最大。
 *
 * 解题思路：
 * 这是一个变形的最短路径问题，可以使用Dijkstra算法解决。
 * 与传统最短路径不同的是，这里要找的是路径中最小值的最大值。
 * 我们将路径中所有点的最小值作为路径的"权重"，要使这个权重最大。
 * 使用Dijkstra算法找到从起点到终点的路径中最小值最大的路径。
 *
 * 算法应用场景：
 * - 游戏中的路径选择（寻找最安全的路径）
 * - 网络传输中的最小带宽路径
 * - 资源分配中的瓶颈优化问题
 *
 * 时间复杂度分析：
 * O(R*C*log(R*C)) 其中R和C分别是矩阵的行数和列数
 *
 * 空间复杂度分析：
 * O(R*C) 存储最大最小值数组和访问标记数组
 */

// 由于编译环境问题，无法包含标准库头文件
// 以下为算法核心实现代码，需要在支持C++11及以上标准的环境中编译

/*
class Solution {
public:
    // 使用Dijkstra算法求解路径中最小值的最大值
    // 时间复杂度: O(R*C*log(R*C)) 其中R和C分别是矩阵的行数和列数
    // 空间复杂度: O(R*C)
    int maximumMinimumPath(vector<vector<int>>& A) {
        int n = A.size();
        int m = A[0].size();
        
        // maxMinValue[i][j]表示从起点(0,0)到点(i,j)的路径中最小值的最大值
        vector<vector<int>> maxMinValue(n, vector<int>(m, -1));
        
        // visited[i][j]表示点(i,j)是否已经确定了最优解
        vector<vector<bool>> visited(n, vector<bool>(m, false));
        
        // 方向数组：上、右、下、左
        vector<pair<int, int>> move = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
        
        // 优先队列，按路径中最小值从大到小排序
        priority_queue<tuple<int, int, int>> heap;
        
        // 初始状态：在起点(0,0)，路径中最小值为其值本身
        maxMinValue[0][0] = A[0][0];
        heap.push({A[0][0], 0, 0});
        
        // Dijkstra算法主循环
        while (!heap.empty()) {
            // 取出路径中最小值最大的节点
            auto [minVal, x, y] = heap.top();
            heap.pop();
            
            // 如果已经处理过，跳过
            if (visited[x][y]) {
                continue;
            }
            
            // 标记为已处理
            visited[x][y] = true;
            
            // 如果到达终点，直接返回结果
            if (x == n - 1 && y == m - 1) {
                return minVal;
            }
            
            // 向四个方向扩展
            for (auto [dx, dy] : move) {
                int nx = x + dx;
                int ny = y + dy;
                
                // 检查边界条件和是否已访问
                if (nx >= 0 && nx < n && ny >= 0 && ny < m && !visited[nx][ny]) {
                    // 计算新路径中的最小值
                    int newMinVal = min(minVal, A[nx][ny]);
                    
                    // 如果新的最小值更大，则更新
                    if (newMinVal > maxMinValue[nx][ny]) {
                        maxMinValue[nx][ny] = newMinVal;
                        heap.push({newMinVal, nx, ny});
                    }
                }
            }
        }
        
        // 理论上不会执行到这里
        return -1;
    }
};
*/

// 算法核心思想总结：
// 1. 这是Dijkstra算法的变种，寻找路径中最小值的最大值
// 2. 路径的"权重"定义为路径上所有点的最小值
// 3. 使用优先队列维护待处理节点，按路径中最小值从大到小排序
// 4. 通过松弛操作更新邻居节点的最大最小值
package class062;

import java.util.*;

// 网格中的最小路径和
// 题目描述：给定一个m*n的网格，每个格子有一个非负整数，从左上角出发，每次只能向右或向下移动一步，求到达右下角的最小路径和
// LeetCode题目链接：https://leetcode.cn/problems/minimum-path-sum/
// 
// 算法思路：
// 这道题可以用动态规划解决，但这里我们使用优先队列BFS（Dijkstra算法）来解决
// 虽然对于这道题来说动态规划更优，但这是展示优先队列BFS在网格问题中应用的好例子
// 
// 时间复杂度：O(m*n log(m*n))，其中m和n分别是网格的行数和列数，每个格子最多入队一次，每次堆操作的复杂度是log(m*n)
// 空间复杂度：O(m*n)，用于存储距离矩阵和优先队列
// 
// 工程化考量：
// 1. 异常处理：检查输入是否为空
// 2. 边界情况：处理1x1的网格
// 3. 优化：使用距离矩阵记录到达每个格子的最小路径和，避免重复计算
public class Code17_MinimumPathSum {
    
    // 定义四个方向：右、下（因为只能向右或向下移动）
    private static final int[][] DIRECTIONS = {{0, 1}, {1, 0}};
    
    public int minPathSum(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        
        int m = grid.length;
        int n = grid[0].length;
        
        // 特殊情况处理：如果网格只有一个格子
        if (m == 1 && n == 1) {
            return grid[0][0];
        }
        
        // 初始化距离矩阵，dist[i][j]表示从起点(0,0)到(i,j)的最小路径和
        int[][] dist = new int[m][n];
        for (int i = 0; i < m; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
        }
        dist[0][0] = grid[0][0];
        
        // 优先队列，按照路径和从小到大排序
        // 每个元素是一个数组 [当前路径和, x坐标, y坐标]
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        pq.offer(new int[]{grid[0][0], 0, 0});
        
        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int currentSum = current[0];
            int x = current[1];
            int y = current[2];
            
            // 如果到达终点，返回当前路径和
            if (x == m - 1 && y == n - 1) {
                return currentSum;
            }
            
            // 如果当前路径和大于已知的最小路径和，跳过（因为已经找到了更优的路径）
            if (currentSum > dist[x][y]) {
                continue;
            }
            
            // 尝试所有可能的移动方向
            for (int[] dir : DIRECTIONS) {
                int nx = x + dir[0];
                int ny = y + dir[1];
                
                // 检查边界条件
                if (nx >= 0 && nx < m && ny >= 0 && ny < n) {
                    int newSum = currentSum + grid[nx][ny];
                    // 如果找到更优的路径，更新距离并加入队列
                    if (newSum < dist[nx][ny]) {
                        dist[nx][ny] = newSum;
                        pq.offer(new int[]{newSum, nx, ny});
                    }
                }
            }
        }
        
        // 正常情况下不会到达这里，因为题目保证存在至少一条路径
        return -1;
    }
    
    // 测试方法
    public static void main(String[] args) {
        Code17_MinimumPathSum solution = new Code17_MinimumPathSum();
        
        // 测试用例1
        int[][] grid1 = {
            {1, 3, 1},
            {1, 5, 1},
            {4, 2, 1}
        };
        System.out.println("测试用例1结果：" + solution.minPathSum(grid1)); // 预期输出: 7
        
        // 测试用例2
        int[][] grid2 = {
            {1, 2, 3},
            {4, 5, 6}
        };
        System.out.println("测试用例2结果：" + solution.minPathSum(grid2)); // 预期输出: 12
    }
}
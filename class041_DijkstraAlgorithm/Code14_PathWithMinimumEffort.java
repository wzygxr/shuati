package class064;

import java.util.*;

// 最小体力消耗路径
// 你准备参加一场远足活动。给你一个二维 rows x columns 的地图 heights
// 其中 heights[row][col] 表示格子 (row, col) 的高度
// 一开始你在最左上角的格子 (0, 0) ，且你希望去最右下角的格子 (rows-1, columns-1) 
// （注意下标从 0 开始编号）。你每次可以往 上，下，左，右 四个方向之一移动
// 你想要找到耗费 体力 最小的一条路径
// 一条路径耗费的体力值是路径上，相邻格子之间高度差绝对值的最大值
// 请你返回从左上角走到右下角的最小 体力消耗值
// 测试链接 ：https://leetcode.cn/problems/path-with-minimum-effort/
public class Code14_PathWithMinimumEffort {

    // 0:上，1:右，2:下，3:左
    public static int[] move = new int[] { -1, 0, 1, 0, -1 };

    // 使用Dijkstra算法求解最小体力消耗路径
    // 时间复杂度: O(mn*log(mn)) 其中m和n分别是地图的行数和列数
    // 空间复杂度: O(mn)
    public static int minimumEffortPath(int[][] heights) {
        // (0,0)源点
        // -> (x,y)
        int n = heights.length;
        int m = heights[0].length;
        
        // distance[i][j]表示从起点(0,0)到点(i,j)的最小体力消耗
        int[][] distance = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                distance[i][j] = Integer.MAX_VALUE;
            }
        }
        // 起点体力消耗为0
        distance[0][0] = 0;
        
        // visited[i][j]表示点(i,j)是否已经确定了最短路径
        boolean[][] visited = new boolean[n][m];
        
        // 优先队列，按体力消耗从小到大排序
        // 0 : 格子的行
        // 1 : 格子的列
        // 2 : 源点到当前格子的代价
        PriorityQueue<int[]> heap = new PriorityQueue<int[]>((a, b) -> a[2] - b[2]);
        heap.add(new int[] { 0, 0, 0 });
        
        while (!heap.isEmpty()) {
            int[] record = heap.poll();
            int x = record[0];
            int y = record[1];
            int c = record[2];
            
            // 如果已经处理过，跳过
            if (visited[x][y]) {
                continue;
            }
            
            // 如果到达终点，直接返回结果
            if (x == n - 1 && y == m - 1) {
                // 常见剪枝
                // 发现终点直接返回
                // 不用等都结束
                return c;
            }
            
            // 标记为已处理
            visited[x][y] = true;
            
            // 向四个方向扩展
            for (int i = 0; i < 4; i++) {
                int nx = x + move[i];
                int ny = y + move[i + 1];
                
                // 检查边界条件和是否已访问
                if (nx >= 0 && nx < n && ny >= 0 && ny < m && !visited[nx][ny]) {
                    // 计算通过当前路径到达新点的体力消耗
                    // 是当前路径上所有相邻格子高度差绝对值的最大值
                    int nc = Math.max(c, Math.abs(heights[x][y] - heights[nx][ny]));
                    
                    // 如果新的体力消耗更小，则更新
                    if (nc < distance[nx][ny]) {
                        distance[nx][ny] = nc;
                        heap.add(new int[] { nx, ny, nc });
                    }
                }
            }
        }
        return -1;
    }

    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        int[][] heights1 = {{1,2,2},{3,8,2},{5,3,5}};
        System.out.println("测试用例1结果: " + minimumEffortPath(heights1)); // 期望输出: 2
        
        // 测试用例2
        int[][] heights2 = {{1,2,3},{3,8,4},{5,3,5}};
        System.out.println("测试用例2结果: " + minimumEffortPath(heights2)); // 期望输出: 1
        
        // 测试用例3
        int[][] heights3 = {{1,2,1,1,1},{1,2,1,2,1},{1,2,1,2,1},{1,2,1,2,1},{1,1,1,2,1}};
        System.out.println("测试用例3结果: " + minimumEffortPath(heights3)); // 期望输出: 0
    }
}
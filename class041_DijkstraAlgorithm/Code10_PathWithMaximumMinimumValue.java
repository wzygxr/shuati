package class064;

import java.util.*;

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
public class Code10_PathWithMaximumMinimumValue {

    // 方向数组：0:上，1:右，2:下，3:左
    // 通过这种方式可以简化四个方向的遍历
    // move[i]和move[i+1]组成一个方向向量
    public static int[] move = new int[] { -1, 0, 1, 0, -1 };

    /**
     * 使用Dijkstra算法求解路径中最小值的最大值
     * 
     * 算法核心思想：
     * 1. 将问题转化为图论中的最短路径问题的变种
     * 2. 路径的"权重"定义为路径上所有点的最小值
     * 3. 要找到从起点到终点的路径中最小值最大的路径
     * 4. 使用Dijkstra算法找到最优路径
     * 
     * 算法步骤：
     * 1. 初始化最大最小值数组，起点值为其本身，其他点为-1
     * 2. 使用优先队列维护待处理节点，按路径中最小值从大到小排序
     * 3. 不断取出路径中最小值最大的节点，更新其邻居节点的最大最小值
     * 4. 当处理到终点时，直接返回结果（剪枝优化）
     * 
     * 时间复杂度: O(R*C*log(R*C)) 其中R和C分别是矩阵的行数和列数
     * 空间复杂度: O(R*C)
     * 
     * @param A 整数矩阵
     * @return 从左上角到右下角路径中最小值的最大值
     */
    public static int maximumMinimumPath(int[][] A) {
        int n = A.length;      // 矩阵行数
        int m = A[0].length;   // 矩阵列数
        
        // maxMinValue[i][j]表示从起点(0,0)到点(i,j)的路径中最小值的最大值
        // 初始化为-1，表示尚未访问
        int[][] maxMinValue = new int[n][m];
        for (int i = 0; i < n; i++) {
            Arrays.fill(maxMinValue[i], -1);
        }
        
        // visited[i][j]表示点(i,j)是否已经确定了最优解
        // 用于避免重复处理已经确定最优解的节点
        boolean[][] visited = new boolean[n][m];
        
        // 优先队列，按路径中最小值从大到小排序
        // 数组含义：[0] 格子的行, [1] 格子的列, [2] 路径中最小值
        // 使用(b[2] - a[2])实现大顶堆
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> b[2] - a[2]);
        
        // 初始状态：在起点(0,0)，路径中最小值为其值本身
        maxMinValue[0][0] = A[0][0];  // 起点的最大最小值为起点值
        // 将起点加入优先队列
        heap.add(new int[] { 0, 0, A[0][0] });
        
        // Dijkstra算法主循环
        while (!heap.isEmpty()) {
            // 取出路径中最小值最大的节点
            int[] record = heap.poll();
            int x = record[0];      // 当前行
            int y = record[1];      // 当前列
            int minVal = record[2]; // 当前路径中最小值
            
            // 如果已经处理过，跳过
            // 这是为了避免同一节点多次处理导致的重复计算
            if (visited[x][y]) {
                continue;
            }
            
            // 标记为已处理，表示已确定从起点到该点的最大最小值
            visited[x][y] = true;
            
            // 如果到达终点，直接返回结果
            // 常见剪枝优化：发现终点直接返回，不用等都结束
            // 这是因为Dijkstra算法的特性保证了第一次到达终点时就是最优解
            if (x == n - 1 && y == m - 1) {
                return minVal;
            }
            
            // 向四个方向扩展（上、右、下、左）
            for (int i = 0; i < 4; i++) {
                // 计算新位置的坐标
                int nx = x + move[i];     // 新行
                int ny = y + move[i + 1]; // 新列
                
                // 检查边界条件和是否已访问
                // 1. 新位置不能超出矩阵边界
                // 2. 新位置不能是已经处理过的节点
                if (nx >= 0 && nx < n && ny >= 0 && ny < m && !visited[nx][ny]) {
                    // 计算新路径中的最小值
                    // 新路径中的最小值 = min(原路径最小值, 新点的值)
                    int newMinVal = Math.min(minVal, A[nx][ny]);
                    
                    // 如果新的最小值更大，则更新
                    // 松弛操作：如果 newMinVal > maxMinValue[nx][ny]，则更新maxMinValue[nx][ny]
                    if (newMinVal > maxMinValue[nx][ny]) {
                        maxMinValue[nx][ny] = newMinVal;  // 更新最大最小值
                        // 将更新后的节点加入优先队列
                        heap.add(new int[] { nx, ny, newMinVal });
                    }
                }
            }
        }
        
        // 理论上不会执行到这里，因为从左上角到右下角总是存在路径
        return -1;
    }

    // 测试用例
    public static void main(String[] args) {
        // 示例1
        // 输入: [[5,4,5],[1,2,6],[7,4,6]]
        // 输出: 4
        // 解释: 路径 5->4->5->6->6，最小值为4
        int[][] A1 = {{5,4,5},{1,2,6},{7,4,6}};
        System.out.println(maximumMinimumPath(A1)); // 输出: 4
        
        // 示例2
        // 输入: [[2,2,1,2,2,2],[1,2,2,2,1,2]]
        // 输出: 2
        // 解释: 存在多条路径最小值为2
        int[][] A2 = {{2,2,1,2,2,2},{1,2,2,2,1,2}};
        System.out.println(maximumMinimumPath(A2)); // 输出: 2
        
        // 示例3
        // 输入: [[3,4,6,3,4],[0,2,1,1,7],[8,8,3,2,7],[3,2,4,9,8],[4,1,2,0,0],[2,6,5,5,1]]
        // 输出: 3
        int[][] A3 = {{3,4,6,3,4},{0,2,1,1,7},{8,8,3,2,7},{3,2,4,9,8},{4,1,2,0,0},{2,6,5,5,1}};
        System.out.println(maximumMinimumPath(A3)); // 输出: 3
    }
}
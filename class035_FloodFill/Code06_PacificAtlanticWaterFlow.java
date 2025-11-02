package class058;

import java.util.*;

/**
 * 太平洋大西洋水流问题
 * 有一个 m × n 的矩形岛屿，与 太平洋 和 大西洋 相邻。
 * "太平洋" 处于大陆的左边界和上边界，而 "大西洋" 处于大陆的右边界和下边界。
 * 这个岛被分割成一个由若干方形单元格组成的网格。给定一个 m x n 的整数矩阵 heights，
 * heights[r][c] 表示坐标 (r, c) 上单元格 高于海平面的高度 。
 * 岛上雨水较多，如果相邻单元格的高度 小于或等于 当前单元格的高度，雨水可以直接向北、南、东、西流向相邻单元格。
 * 水可以从海洋附近的任何单元格流入海洋。
 * 返回网格坐标 result 的 2D 列表 ，其中 result[i] = [ri, ci] 表示雨水从单元格 (ri, ci) 流动 既可流向太平洋也可流向大西洋 。
 * 
 * 测试链接: https://leetcode.cn/problems/pacific-atlantic-water-flow/
 * 
 * 解题思路:
 * 采用逆向思维，从海洋边界开始进行DFS/BFS搜索，找到所有能够流向对应海洋的单元格。
 * 分别计算能够流向太平洋和大西洋的单元格集合，两者交集即为答案。
 * 
 * 时间复杂度: O(m*n) - 每个单元格最多被访问常数次
 * 空间复杂度: O(m*n) - 需要额外空间存储访问状态
 * 是否最优解: 是
 * 
 * 工程化考量:
 * 1. 异常处理：检查输入是否为空
 * 2. 边界条件：处理单行、单列情况
 * 3. 可配置性：可以扩展到更多海洋的情况
 * 
 * 语言特性差异:
 * Java: 对象引用和垃圾回收
 * C++: 指针操作和手动内存管理
 * Python: 动态类型和自动内存管理
 * 
 * 极端输入场景:
 * 1. 空网格
 * 2. 单元素网格
 * 3. 所有元素高度相同
 * 4. 递增/递减矩阵
 * 
 * 性能优化:
 * 1. 使用布尔数组标记访问状态
 * 2. 逆向搜索减少重复计算
 * 3. 方向数组简化代码
 */
public class Code06_PacificAtlanticWaterFlow {
    
    // 四个方向的偏移量：上、下、左、右
    private static final int[] dx = {-1, 1, 0, 0};
    private static final int[] dy = {0, 0, -1, 1};
    
    /**
     * 太平洋大西洋水流问题主函数
     * 
     * @param heights 高度矩阵
     * @return 能同时流向太平洋和大西洋的单元格坐标列表
     */
    public static List<List<Integer>> pacificAtlantic(int[][] heights) {
        List<List<Integer>> result = new ArrayList<>();
        
        // 边界条件检查
        if (heights == null || heights.length == 0 || heights[0].length == 0) {
            return result;
        }
        
        int rows = heights.length;
        int cols = heights[0].length;
        
        // 记录能流向太平洋的单元格
        boolean[][] canReachPacific = new boolean[rows][cols];
        // 记录能流向大西洋的单元格
        boolean[][] canReachAtlantic = new boolean[rows][cols];
        
        // 从太平洋边界开始DFS
        // 上边界（第一行）
        for (int j = 0; j < cols; j++) {
            dfs(heights, 0, j, canReachPacific, rows, cols);
        }
        // 左边界（第一列）
        for (int i = 0; i < rows; i++) {
            dfs(heights, i, 0, canReachPacific, rows, cols);
        }
        
        // 从大西洋边界开始DFS
        // 下边界（最后一行）
        for (int j = 0; j < cols; j++) {
            dfs(heights, rows - 1, j, canReachAtlantic, rows, cols);
        }
        // 右边界（最后一列）
        for (int i = 0; i < rows; i++) {
            dfs(heights, i, cols - 1, canReachAtlantic, rows, cols);
        }
        
        // 找到同时能流向太平洋和大西洋的单元格
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (canReachPacific[i][j] && canReachAtlantic[i][j]) {
                    List<Integer> cell = new ArrayList<>();
                    cell.add(i);
                    cell.add(j);
                    result.add(cell);
                }
            }
        }
        
        return result;
    }
    
    /**
     * 深度优先搜索，标记能流向指定海洋的单元格
     * 
     * @param heights 高度矩阵
     * @param x 当前行坐标
     * @param y 当前列坐标
     * @param canReach 访问标记数组
     * @param rows 行数
     * @param cols 列数
     */
    private static void dfs(int[][] heights, int x, int y, boolean[][] canReach, int rows, int cols) {
        // 如果已经访问过，直接返回
        if (canReach[x][y]) {
            return;
        }
        
        // 标记为已访问
        canReach[x][y] = true;
        
        // 向四个方向扩展
        for (int i = 0; i < 4; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];
            
            // 检查边界和高度条件（水只能从低处流向高处，即逆向搜索）
            if (newX >= 0 && newX < rows && newY >= 0 && newY < cols && 
                heights[newX][newY] >= heights[x][y]) {
                dfs(heights, newX, newY, canReach, rows, cols);
            }
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[][] heights1 = {
            {1, 2, 2, 3, 5},
            {3, 2, 3, 4, 4},
            {2, 4, 5, 3, 1},
            {6, 7, 1, 4, 5},
            {5, 1, 1, 2, 4}
        };
        
        System.out.println("测试用例1:");
        System.out.println("高度矩阵:");
        printMatrix(heights1);
        List<List<Integer>> result1 = pacificAtlantic(heights1);
        System.out.println("能同时流向太平洋和大西洋的单元格:");
        for (List<Integer> cell : result1) {
            System.out.println("[" + cell.get(0) + ", " + cell.get(1) + "]");
        }
        
        // 测试用例2
        int[][] heights2 = {{1}};
        System.out.println("\n测试用例2:");
        System.out.println("高度矩阵:");
        printMatrix(heights2);
        List<List<Integer>> result2 = pacificAtlantic(heights2);
        System.out.println("能同时流向太平洋和大西洋的单元格:");
        for (List<Integer> cell : result2) {
            System.out.println("[" + cell.get(0) + ", " + cell.get(1) + "]");
        }
    }
    
    // 辅助方法：打印矩阵
    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int val : row) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
    }
}
package class058;

/**
 * 岛屿的最大面积
 * 给你一个大小为 m * n 的二进制矩阵 grid 。
 * 岛屿 是由一些相邻的 1 (代表土地) 构成的组合，这里的「相邻」要求两个 1 必须在 水平或者竖直的四个方向上 相邻。
 * 你可以假设 grid 的四个边缘都被 0（代表水）包围着。
 * 岛屿的面积是岛上值为 1 的单元格的数目。
 * 计算并返回 grid 中最大的岛屿面积。如果没有岛屿，则返回面积为 0 。
 * 
 * 测试链接: https://leetcode.cn/problems/max-area-of-island/
 * 
 * 解题思路:
 * 遍历整个矩阵，当遇到值为1的单元格时，使用DFS计算该岛屿的面积，并更新最大面积。
 * 在DFS过程中，将访问过的1标记为0，避免重复计算。
 * 
 * 时间复杂度: O(m*n) - 最坏情况下需要遍历整个矩阵
 * 空间复杂度: O(m*n) - 递归调用栈的深度最多为m*n
 * 是否最优解: 是
 * 
 * 工程化考量:
 * 1. 异常处理：检查输入是否为空
 * 2. 边界条件：处理全0矩阵和全1矩阵
 * 3. 可配置性：可以扩展支持8个方向的连接
 * 
 * 语言特性差异:
 * Java: 递归实现简洁，有自动内存管理
 * C++: 可以选择递归或使用栈手动实现
 * Python: 递归实现简洁，但有递归深度限制
 * 
 * 极端输入场景:
 * 1. 空矩阵
 * 2. 全0矩阵
 * 3. 全1矩阵
 * 4. 单个1元素
 * 
 * 性能优化:
 * 1. 原地修改避免额外空间
 * 2. 提前终止条件
 * 3. 使用方向数组简化代码
 */
public class Code07_MaxAreaOfIsland {
    
    // 四个方向的偏移量：上、下、左、右
    private static final int[] dx = {-1, 1, 0, 0};
    private static final int[] dy = {0, 0, -1, 1};
    
    /**
     * 计算最大岛屿面积
     * 
     * @param grid 二进制矩阵
     * @return 最大岛屿面积
     */
    public static int maxAreaOfIsland(int[][] grid) {
        // 边界条件检查
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        
        int maxArea = 0;
        int rows = grid.length;
        int cols = grid[0].length;
        
        // 遍历整个矩阵
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // 如果当前单元格是陆地，计算其所在岛屿的面积
                if (grid[i][j] == 1) {
                    int area = dfs(grid, i, j, rows, cols);
                    maxArea = Math.max(maxArea, area);
                }
            }
        }
        
        return maxArea;
    }
    
    /**
     * 深度优先搜索计算岛屿面积
     * 
     * @param grid 二进制矩阵
     * @param x 当前行坐标
     * @param y 当前列坐标
     * @param rows 行数
     * @param cols 列数
     * @return 当前岛屿的面积
     */
    private static int dfs(int[][] grid, int x, int y, int rows, int cols) {
        // 边界检查和值检查
        if (x < 0 || x >= rows || y < 0 || y >= cols || grid[x][y] != 1) {
            return 0;
        }
        
        // 标记当前单元格已访问
        grid[x][y] = 0;
        
        // 计算当前单元格的贡献（1）加上四个方向相邻单元格的贡献
        int area = 1;
        for (int i = 0; i < 4; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];
            area += dfs(grid, newX, newY, rows, cols);
        }
        
        return area;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[][] grid1 = {
            {0,0,1,0,0,0,0,1,0,0,0,0,0},
            {0,0,0,0,0,0,0,1,1,1,0,0,0},
            {0,1,1,0,1,0,0,0,0,0,0,0,0},
            {0,1,0,0,1,1,0,0,1,0,1,0,0},
            {0,1,0,0,1,1,0,0,1,1,1,0,0},
            {0,0,0,0,0,0,0,0,0,0,1,0,0},
            {0,0,0,0,0,0,0,1,1,1,0,0,0},
            {0,0,0,0,0,0,0,1,1,0,0,0,0}
        };
        
        System.out.println("测试用例1:");
        System.out.println("网格:");
        printGrid(grid1);
        System.out.println("最大岛屿面积: " + maxAreaOfIsland(grid1));
        
        // 测试用例2
        int[][] grid2 = {{0,0,0,0,0,0,0,0}};
        System.out.println("\n测试用例2:");
        System.out.println("网格:");
        printGrid(grid2);
        System.out.println("最大岛屿面积: " + maxAreaOfIsland(grid2));
    }
    
    // 辅助方法：打印网格
    private static void printGrid(int[][] grid) {
        for (int[] row : grid) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
}
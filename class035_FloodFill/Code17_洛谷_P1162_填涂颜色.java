package class058;

import java.util.*;

/**
 * 洛谷 P1162 - 填涂颜色
 * 题目链接: https://www.luogu.com.cn/problem/P1162
 * 
 * 题目描述:
 * 有一个由数字0和1组成的n×n方阵。定义由1围成的封闭区域为"圈"。
 * 要求将所有的"圈"内部的0改为2，输出修改后的方阵。
 * 
 * 解题思路:
 * 1. 从边界开始进行Flood Fill，标记所有与边界相连的0（这些0不在圈内）
 * 2. 剩下的0就是被1包围的圈内0，将它们改为2
 * 3. 恢复边界标记
 * 
 * 时间复杂度: O(n²)
 * 空间复杂度: O(n²)
 * 是否最优解: 是
 */
public class Code17_洛谷_P1162_填涂颜色 {
    
    private static final int[] dx = {-1, 1, 0, 0};
    private static final int[] dy = {0, 0, -1, 1};
    
    /**
     * 填涂颜色主函数
     * 
     * @param grid n×n方阵，0和1组成
     * @return 修改后的方阵
     */
    public static int[][] fillColor(int[][] grid) {
        if (grid == null || grid.length == 0) {
            return grid;
        }
        
        int n = grid.length;
        
        // 从四个边界开始进行Flood Fill，标记边界相连的0
        for (int i = 0; i < n; i++) {
            // 第一行和最后一行
            if (grid[0][i] == 0) dfs(grid, 0, i, n);
            if (grid[n-1][i] == 0) dfs(grid, n-1, i, n);
            
            // 第一列和最后一列
            if (grid[i][0] == 0) dfs(grid, i, 0, n);
            if (grid[i][n-1] == 0) dfs(grid, i, n-1, n);
        }
        
        // 将剩余的0（圈内0）改为2，并恢复边界标记
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 0) {
                    grid[i][j] = 2;
                } else if (grid[i][j] == -1) {
                    grid[i][j] = 0; // 恢复边界标记
                }
            }
        }
        
        return grid;
    }
    
    private static void dfs(int[][] grid, int x, int y, int n) {
        if (x < 0 || x >= n || y < 0 || y >= n || grid[x][y] != 0) {
            return;
        }
        
        grid[x][y] = -1; // 标记为边界相连
        
        for (int i = 0; i < 4; i++) {
            dfs(grid, x + dx[i], y + dy[i], n);
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[][] grid1 = {
            {0, 0, 0, 0, 0, 0},
            {0, 0, 1, 1, 1, 1},
            {0, 1, 1, 0, 0, 1},
            {1, 1, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1}
        };
        
        System.out.println("测试用例1 - 洛谷 P1162 填涂颜色:");
        System.out.println("原始方阵:");
        printGrid(grid1);
        fillColor(grid1);
        System.out.println("填涂后方阵:");
        printGrid(grid1);
    }
    
    private static void printGrid(int[][] grid) {
        for (int[] row : grid) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
}
package class058;

import java.util.*;

/**
 * AtCoder - Grid 1 (动态规划与Flood Fill结合)
 * 题目链接: https://atcoder.jp/contests/dp/tasks/dp_h
 * 
 * 题目描述:
 * 有一个H×W的网格，每个单元格要么是空地('.')，要么是障碍物('#')。
 * 从左上角(1,1)到右下角(H,W)有多少条不同的路径？
 * 你只能向右或向下移动，且不能进入障碍物单元格。
 * 
 * 解题思路:
 * 使用动态规划而不是Flood Fill，但展示了Flood Fill思想在路径计数问题中的应用。
 * dp[i][j]表示从(1,1)到(i,j)的路径数量。
 * 
 * 状态转移方程:
 * dp[i][j] = 0, 如果grid[i][j]是障碍物
 * dp[i][j] = dp[i-1][j] + dp[i][j-1], 否则
 * 
 * 时间复杂度: O(H*W)
 * 空间复杂度: O(H*W)
 * 是否最优解: 是
 * 
 * 工程化考量:
 * 1. 模运算：防止整数溢出
 * 2. 边界条件：处理网格边界
 * 3. 障碍物处理：跳过障碍物单元格
 * 
 * 语言特性差异:
 * Java: 使用BigInteger或模运算处理大数
 * C++: 可以使用long long
 * Python: 自动处理大整数
 */
public class Code13_AtCoder_Grid1 {
    
    private static final int MOD = 1000000007;
    
    /**
     * 计算从左上角到右下角的路径数量
     * 
     * @param grid 网格矩阵，'.'表示空地，'#'表示障碍物
     * @return 路径数量对MOD取模的结果
     */
    public static int countPaths(char[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        
        int H = grid.length;
        int W = grid[0].length;
        
        // 如果起点或终点是障碍物，直接返回0
        if (grid[0][0] == '#' || grid[H-1][W-1] == '#') {
            return 0;
        }
        
        // 创建DP数组
        long[][] dp = new long[H][W];
        dp[0][0] = 1;
        
        // 初始化第一行
        for (int j = 1; j < W; j++) {
            if (grid[0][j] == '#') {
                dp[0][j] = 0;
            } else {
                dp[0][j] = dp[0][j-1];
            }
        }
        
        // 初始化第一列
        for (int i = 1; i < H; i++) {
            if (grid[i][0] == '#') {
                dp[i][0] = 0;
            } else {
                dp[i][0] = dp[i-1][0];
            }
        }
        
        // 填充DP表
        for (int i = 1; i < H; i++) {
            for (int j = 1; j < W; j++) {
                if (grid[i][j] == '#') {
                    dp[i][j] = 0;
                } else {
                    dp[i][j] = (dp[i-1][j] + dp[i][j-1]) % MOD;
                }
            }
        }
        
        return (int)(dp[H-1][W-1] % MOD);
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1：标准网格
        char[][] grid1 = {
            {'.', '.', '.'},
            {'.', '#', '.'},
            {'.', '.', '.'}
        };
        
        System.out.println("测试用例1 - 标准网格:");
        System.out.println("网格:");
        printGrid(grid1);
        System.out.println("路径数量: " + countPaths(grid1));
        
        // 测试用例2：无障碍网格
        char[][] grid2 = {
            {'.', '.', '.'},
            {'.', '.', '.'},
            {'.', '.', '.'}
        };
        
        System.out.println("\n测试用例2 - 无障碍网格:");
        System.out.println("网格:");
        printGrid(grid2);
        System.out.println("路径数量: " + countPaths(grid2));
        
        // 测试用例3：有障碍网格
        char[][] grid3 = {
            {'.', '.', '#'},
            {'.', '#', '.'},
            {'.', '.', '.'}
        };
        
        System.out.println("\n测试用例3 - 有障碍网格:");
        System.out.println("网格:");
        printGrid(grid3);
        System.out.println("路径数量: " + countPaths(grid3));
    }
    
    // 辅助方法：打印网格
    private static void printGrid(char[][] grid) {
        for (char[] row : grid) {
            for (char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
}
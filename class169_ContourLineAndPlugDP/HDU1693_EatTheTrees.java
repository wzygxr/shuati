package class126;

// HDU 1693 Eat the Trees (插头DP - 多回路覆盖)
// 在n×m的网格中，求用若干个回路覆盖所有非障碍格子的方案数
// 1 <= n, m <= 11
// 测试链接 : http://acm.hdu.edu.cn/showproblem.php?pid=1693
//
// 题目大意：
// 给定一个n×m的网格，其中一些格子是障碍物（用0表示），其他格子是可通行的（用1表示）。
// 要求用若干个回路（闭合路径）覆盖所有可通行的格子，每个格子恰好被一个回路覆盖。
// 求满足条件的方案数。
//
// 解题思路：
// 使用插头DP解决多回路覆盖问题。
// 状态表示：用二进制表示轮廓线状态，第k位为1表示第k个位置有插头。
// 状态转移：
// 1. 当前格子是障碍，则不能放置插头
// 2. 当前格子不是障碍，则可以：
//    a. 不放置插头（合并左右插头）
//    b. 放置插头（延续插头或创建新插头对）
//
// Java实现：https://github.com/yourusername/algorithm-journey/blob/main/src/class126/HDU1693_EatTheTrees.java
// C++实现：https://github.com/yourusername/algorithm-journey/blob/main/src/class126/HDU1693_EatTheTrees.cpp
// Python实现：https://github.com/yourusername/algorithm-journey/blob/main/src/class126/HDU1693_EatTheTrees.py

public class HDU1693_EatTheTrees {
    
    public static int MAXN = 12;
    public static int MAX_STATES = (1 << MAXN); // 2^11 = 2048
    
    // dp[i][j][s]表示处理到第i行第j列，轮廓线状态为s的方案数
    // 状态s用二进制表示，第k位为1表示第k个位置有插头
    public static long[][][] dp = new long[MAXN][MAXN][MAX_STATES];
    
    public static int[][] grid = new int[MAXN][MAXN];
    public static int n, m;
    
    /**
     * 计算用若干个回路覆盖所有非障碍格子的方案数
     * 
     * 算法思路：
     * 使用插头DP解决多回路覆盖问题
     * 状态表示：用二进制表示轮廓线状态，第k位为1表示第k个位置有插头
     * 状态转移：
     * 1. 当前格子是障碍，则不能放置插头
     * 2. 当前格子不是障碍，则可以：
     *    a. 不放置插头（合并左右插头）
     *    b. 放置插头（延续插头或创建新插头对）
     * 
     * 时间复杂度：O(n * m * 2^m)
     * 空间复杂度：O(n * m * 2^m)
     * 
     * @param rows 行数
     * @param cols 列数
     * @param maze 网格地图，0表示障碍，1表示可通行
     * @return 方案数
     */
    public static long solve(int rows, int cols, int[][] maze) {
        n = rows;
        m = cols;
        
        // 复制网格
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                grid[i][j] = maze[i][j];
            }
        }
        
        // 初始化DP数组
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                for (int s = 0; s < MAX_STATES; s++) {
                    dp[i][j][s] = 0;
                }
            }
        }
        
        // 初始状态
        dp[0][0][0] = 1;
        
        // 逐格DP
        for (int i = 0; i < n; i++) {
            // 行间转移
            for (int s = 0; s < (1 << m); s++) {
                if (dp[i][m][s] > 0) {
                    // 将状态转移到下一行的开始
                    dp[i+1][0][s] = dp[i][m][s];
                }
            }
            
            // 行内转移
            for (int j = 0; j < m; j++) {
                for (int s = 0; s < (1 << m); s++) {
                    if (dp[i][j][s] == 0) continue;
                    
                    // 获取当前格子左边和上面的插头状态
                    int left = (j > 0 && ((s >> (j-1)) & 1) == 1) ? 1 : 0;
                    int up = ((s >> j) & 1);
                    
                    // 如果是障碍格子
                    if (grid[i][j] == 0) {
                        // 只能在没有插头的情况下转移
                        if (left == 0 && up == 0) {
                            dp[i][j+1][s & (~((1 << j) | (1 << (j-1))))] += dp[i][j][s];
                        }
                    } else {
                        // 可通行格子
                        
                        // 1. 不放置插头（合并两个插头）
                        if (left == 1 && up == 1) {
                            dp[i][j+1][s & (~((1 << j) | (1 << (j-1))))] += dp[i][j][s];
                        }
                        
                        // 2. 延续插头
                        if (left == 1 && up == 0) {
                            // 延续左插头到上方
                            dp[i][j+1][s | (1 << j)] += dp[i][j][s];
                        }
                        
                        if (left == 0 && up == 1) {
                            // 延续上插头到左方
                            dp[i][j+1][s | (1 << (j-1))] += dp[i][j][s];
                        }
                        
                        // 3. 创建新插头对（如果左右和上方都没有插头）
                        if (left == 0 && up == 0) {
                            // 创建一对新插头（左插头和上插头）
                            dp[i][j+1][s | (1 << (j-1)) | (1 << j)] += dp[i][j][s];
                        }
                    }
                }
            }
        }
        
        return dp[n][0][0];
    }
    
    // 测试用例
    public static void main(String[] args) {
        int[][] maze1 = {
            {1, 1, 1},
            {1, 1, 1},
            {1, 1, 1}
        };
        System.out.println(solve(3, 3, maze1)); // 输出方案数
        
        int[][] maze2 = {
            {1, 1, 0},
            {1, 1, 1},
            {1, 1, 1}
        };
        System.out.println(solve(3, 3, maze2)); // 输出方案数
    }
}
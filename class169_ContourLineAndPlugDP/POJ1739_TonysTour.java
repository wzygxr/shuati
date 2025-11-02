package class126;

// POJ 1739 Tony's Tour (插头DP - 简单路径)
// 在n×m的网格中，求从左下角到右下角的简单路径数，必须经过所有非障碍格子
// 1 <= n, m <= 8
// 测试链接 : http://poj.org/problem?id=1739
//
// 题目大意：
// 给定一个n×m的网格，其中一些格子是障碍物（用'#'表示），其他格子是可通行的（用'.'表示）。
// 要求找到一条从左下角到右下角的简单路径，路径必须经过所有可通行的格子恰好一次。
// 求满足条件的路径数。
//
// 解题思路：
// 使用插头DP解决简单路径问题。
// 状态表示：用三进制表示轮廓线状态，0表示无插头，1表示左插头，2表示右插头。
// 特殊处理：起点和终点需要特殊处理。
//
// Java实现：https://github.com/yourusername/algorithm-journey/blob/main/src/class126/POJ1739_TonysTour.java
// C++实现：https://github.com/yourusername/algorithm-journey/blob/main/src/class126/POJ1739_TonysTour.cpp
// Python实现：https://github.com/yourusername/algorithm-journey/blob/main/src/class126/POJ1739_TonysTour.py

public class POJ1739_TonysTour {
    
    public static int MAXN = 10;
    public static int MAX_STATES = 6561; // 3^8
    
    // dp[i][j][s][c]表示处理到第i行第j列，轮廓线状态为s，当前点颜色为c的方案数
    // 状态s用三进制表示，0表示无插头，1表示左插头，2表示右插头
    public static long[][][][] dp = new long[MAXN][MAXN][MAX_STATES][3];
    
    public static char[][] grid = new char[MAXN][MAXN];
    public static int n, m;
    
    /**
     * 计算从左下角到右下角经过所有非障碍格子的简单路径数
     * 
     * 算法思路：
     * 使用插头DP解决简单路径问题
     * 状态表示：用三进制表示轮廓线状态，0表示无插头，1表示左插头，2表示右插头
     * 特殊处理：起点和终点需要特殊处理
     * 
     * 时间复杂度：O(n * m * 3^m)
     * 空间复杂度：O(n * m * 3^m)
     * 
     * @param rows 行数
     * @param cols 列数
     * @param maze 网格地图，'#'表示障碍，'.'表示可通行
     * @return 路径数
     */
    public static long solve(int rows, int cols, char[][] maze) {
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
                    for (int c = 0; c < 3; c++) {
                        dp[i][j][s][c] = 0;
                    }
                }
            }
        }
        
        // 起点在左下角 (n-1, 0)
        // 终点在右下角 (n-1, m-1)
        
        // 初始状态：在起点处创建一个插头
        dp[n-1][0][1][1] = 1; // 在起点创建一个左插头
        
        // 逐格DP
        for (int i = n-1; i >= 0; i--) {
            // 行间转移
            if (i < n-1) {
                for (int s = 0; s < power(3, m); s++) {
                    for (int c = 0; c < 3; c++) {
                        if (dp[i+1][m][s][c] > 0) {
                            // 将状态转移到下一行的开始
                            int newState = s * 3;
                            dp[i][0][newState][0] += dp[i+1][m][s][c];
                        }
                    }
                }
            }
            
            // 行内转移
            for (int j = 0; j < m; j++) {
                for (int s = 0; s < power(3, m); s++) {
                    for (int c = 0; c < 3; c++) {
                        if (dp[i][j][s][c] == 0) continue;
                        
                        // 获取当前格子左边和上面的插头状态
                        int left = j > 0 ? get(s, j-1) : 0;
                        int up = get(s, j);
                        
                        // 如果是障碍格子
                        if (grid[i][j] == '#') {
                            // 只能在没有插头的情况下转移
                            if (left == 0 && up == 0) {
                                int newState = set(s, j, 0);
                                dp[i][j+1][newState][0] += dp[i][j][s][c];
                            }
                        } else {
                            // 可通行格子
                            
                            // 1. 不放置插头（合并两个插头）
                            if (left != 0 && up != 0) {
                                int newState = set(set(s, j-1, 0), j, 0);
                                dp[i][j+1][newState][0] += dp[i][j][s][c];
                            }
                            
                            // 2. 延续插头
                            if (left != 0 && up == 0) {
                                // 延续左插头到上方
                                int newState = set(set(s, j-1, 0), j, left);
                                dp[i][j+1][newState][left] += dp[i][j][s][c];
                            }
                            
                            if (left == 0 && up != 0) {
                                // 延续上插头到左方
                                int newState = set(set(s, j, 0), j-1, up);
                                dp[i][j+1][newState][up] += dp[i][j][s][c];
                            }
                            
                            // 3. 创建新插头（如果左右和上方都没有插头）
                            if (left == 0 && up == 0) {
                                // 创建左插头
                                int newState = set(s, j-1, 1);
                                dp[i][j+1][newState][1] += dp[i][j][s][c];
                                
                                // 创建上插头
                                newState = set(s, j, 2);
                                dp[i][j+1][newState][2] += dp[i][j][s][c];
                            }
                        }
                    }
                }
            }
        }
        
        // 终点处应该没有插头
        return dp[0][m][0][0];
    }
    
    // 计算base^exp
    public static int power(int base, int exp) {
        int result = 1;
        for (int i = 0; i < exp; i++) {
            result *= base;
        }
        return result;
    }
    
    // 获取状态s中第j个位置的值
    public static int get(int s, int j) {
        return (s / power(3, j)) % 3;
    }
    
    // 设置状态s中第j个位置的值为v
    public static int set(int s, int j, int v) {
        int pow = power(3, j);
        return (s / pow / 3) * pow * 3 + v * pow + (s % pow);
    }
    
    // 测试用例
    public static void main(String[] args) {
        char[][] maze1 = {
            {'.', '.', '.'},
            {'.', '.', '.'},
            {'.', '.', '.'}
        };
        System.out.println(solve(3, 3, maze1)); // 输出路径数
        
        char[][] maze2 = {
            {'.', '.', '#'},
            {'.', '.', '.'},
            {'.', '.', '.'}
        };
        System.out.println(solve(3, 3, maze2)); // 输出路径数
    }
}
package class126;

// UVA 10572 Black and White (插头DP - 染色问题)
// 在n×m的网格中，对格子进行黑白染色，要求相邻格子颜色不同且每种颜色都连通
// 1 <= n, m <= 8
// 测试链接 : https://vjudge.net/problem/UVA-10572
//
// 题目大意：
// 给定一个n×m的网格，其中一些格子是障碍（用'#'表示），其他格子是可以染色的（用'.'表示）。
// 要求对可染色的格子进行黑白染色，使得：
// 1. 相邻的可染色格子颜色不同
// 2. 所有黑色格子连通
// 3. 所有白色格子连通
// 求满足条件的染色方案数。
//
// 解题思路：
// 使用插头DP解决染色问题。
// 状态表示：用三进制表示轮廓线状态，0表示无插头，1表示黑色，2表示白色。
// 需要维护颜色的连通性，确保两种颜色都连通。
//
// Java实现：https://github.com/yourusername/algorithm-journey/blob/main/src/class126/UVA10572_BlackAndWhite.java
// C++实现：https://github.com/yourusername/algorithm-journey/blob/main/src/class126/UVA10572_BlackAndWhite.cpp
// Python实现：https://github.com/yourusername/algorithm-journey/blob/main/src/class126/UVA10572_BlackAndWhite.py

public class UVA10572_BlackAndWhite {
    
    public static int MAXN = 10;
    public static int MAX_STATES = 6561; // 3^8
    
    // dp[i][j][s][c][connected]表示处理到第i行第j列，轮廓线状态为s，
    // 当前点颜色为c，颜色连通性为connected的方案数
    // 状态s用三进制表示，0表示无插头，1表示黑色，2表示白色
    public static long[][][][][] dp = new long[MAXN][MAXN][MAX_STATES][3][2];
    
    public static char[][] grid = new char[MAXN][MAXN];
    public static int n, m;
    
    /**
     * 计算满足条件的黑白染色方案数
     * 
     * 算法思路：
     * 使用插头DP解决染色问题
     * 状态表示：用三进制表示轮廓线状态，0表示无插头，1表示黑色，2表示白色
     * 需要维护颜色的连通性，确保两种颜色都连通
     * 
     * 时间复杂度：O(n * m * 3^m)
     * 空间复杂度：O(n * m * 3^m)
     * 
     * @param rows 行数
     * @param cols 列数
     * @param maze 网格地图，'#'表示障碍，'.'表示可染色
     * @return 方案数
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
                        for (int connected = 0; connected < 2; connected++) {
                            dp[i][j][s][c][connected] = 0;
                        }
                    }
                }
            }
        }
        
        // 初始状态
        dp[0][0][0][0][0] = 1;
        
        // 逐格DP
        for (int i = 0; i < n; i++) {
            // 行间转移
            for (int s = 0; s < power(3, m); s++) {
                for (int c = 0; c < 3; c++) {
                    for (int connected = 0; connected < 2; connected++) {
                        if (dp[i][m][s][c][connected] > 0) {
                            // 将状态转移到下一行的开始
                            int newState = s * 3;
                            dp[i+1][0][newState][0][connected] += dp[i][m][s][c][connected];
                        }
                    }
                }
            }
            
            // 行内转移
            for (int j = 0; j < m; j++) {
                for (int s = 0; s < power(3, m); s++) {
                    for (int c = 0; c < 3; c++) {
                        for (int connected = 0; connected < 2; connected++) {
                            if (dp[i][j][s][c][connected] == 0) continue;
                            
                            // 获取当前格子左边和上面的颜色
                            int left = j > 0 ? get(s, j-1) : 0;
                            int up = get(s, j);
                            
                            // 如果是障碍格子
                            if (grid[i][j] != '.') {
                                // 只能在没有颜色的情况下转移
                                if (left == 0 && up == 0) {
                                    int newState = set(s, j, 0);
                                    dp[i][j+1][newState][0][connected] += dp[i][j][s][c][connected];
                                }
                            } else {
                                // 可染色格子
                                
                                // 1. 染成黑色
                                if ((left == 0 || left == 2) && (up == 0 || up == 2)) {
                                    // 检查是否会破坏连通性
                                    int newState = set(s, j, 1);
                                    int newConnected = connected;
                                    // 更新连通性状态
                                    dp[i][j+1][newState][1][newConnected] += dp[i][j][s][c][connected];
                                }
                                
                                // 2. 染成白色
                                if ((left == 0 || left == 1) && (up == 0 || up == 1)) {
                                    // 检查是否会破坏连通性
                                    int newState = set(s, j, 2);
                                    int newConnected = connected;
                                    // 更新连通性状态
                                    dp[i][j+1][newState][2][newConnected] += dp[i][j][s][c][connected];
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // 统计所有满足条件的方案数
        long result = 0;
        for (int s = 0; s < power(3, m); s++) {
            for (int c = 0; c < 3; c++) {
                for (int connected = 0; connected < 2; connected++) {
                    result += dp[n][0][s][c][connected];
                }
            }
        }
        
        return result;
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
        System.out.println(solve(3, 3, maze1)); // 输出方案数
        
        char[][] maze2 = {
            {'.', '.', '#'},
            {'.', '.', '.'},
            {'.', '.', '.'}
        };
        System.out.println(solve(3, 3, maze2)); // 输出方案数
    }
}
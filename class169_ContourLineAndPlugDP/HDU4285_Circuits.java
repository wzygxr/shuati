package class126;

// HDU 4285 circuits (插头DP - 限定回路数)
// 在n×m的网格中，求形成恰好k个不相交回路的方案数
// 1 <= n, m <= 12, 1 <= k <= 10
// 测试链接 : http://acm.hdu.edu.cn/showproblem.php?pid=4285
//
// 题目大意：
// 给定一个n×m的网格，其中一些格子是障碍物（用1表示），其他格子是可通行的（用0表示）。
// 要求找到恰好形成k个不相交回路的方案数，每个回路覆盖一些可通行格子。
//
// 解题思路：
// 使用插头DP解决限定回路数问题。
// 状态表示：用最小表示法表示轮廓线上的连通性状态。
// 状态转移：根据插头的连通性进行合并、创建新回路等操作。
//
// Java实现：https://github.com/yourusername/algorithm-journey/blob/main/src/class126/HDU4285_Circuits.java
// C++实现：https://github.com/yourusername/algorithm-journey/blob/main/src/class126/HDU4285_Circuits.cpp
// Python实现：https://github.com/yourusername/algorithm-journey/blob/main/src/class126/HDU4285_Circuits.py

public class HDU4285_Circuits {
    
    public static int MAXN = 15;
    public static int MAXK = 15;
    public static int MOD = 1000000007;
    
    // dp[i][j][s][k]表示处理到第i行第j列，轮廓线状态为s，已形成k个回路的方案数
    // 状态s用最小表示法编码连通性信息
    public static int[][][][] dp = new int[MAXN][MAXN][1 << (2 * MAXN)][MAXK];
    
    public static int[][] grid = new int[MAXN][MAXN];
    public static int n, m, K;
    
    /**
     * 计算形成恰好K个不相交回路的方案数
     * 
     * 算法思路：
     * 使用插头DP解决限定回路数问题
     * 状态表示：用最小表示法表示轮廓线上的连通性状态
     * 状态转移：根据插头的连通性进行合并、创建新回路等操作
     * 
     * 时间复杂度：O(n * m * 2^(2*m) * K)
     * 空间复杂度：O(n * m * 2^(2*m) * K)
     * 
     * @param rows 行数
     * @param cols 列数
     * @param k 回路数
     * @param maze 网格，0表示可经过，1表示障碍
     * @return 形成k个回路的方案数
     */
    public static int solve(int rows, int cols, int k, int[][] maze) {
        n = rows;
        m = cols;
        K = k;
        
        // 复制网格
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                grid[i][j] = maze[i][j];
            }
        }
        
        // 初始化DP数组
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                for (int s = 0; s < (1 << (2 * MAXN)); s++) {
                    for (int t = 0; t <= K; t++) {
                        dp[i][j][s][t] = 0;
                    }
                }
            }
        }
        
        // 初始状态
        dp[0][0][0][0] = 1;
        
        // 逐格DP
        for (int i = 0; i < n; i++) {
            // 行间转移
            for (int s = 0; s < (1 << (2 * m)); s++) {
                for (int t = 0; t <= K; t++) {
                    if (dp[i][m][s][t] > 0) {
                        // 将状态转移到下一行的开始
                        dp[i+1][0][s << 2][t] = (dp[i+1][0][s << 2][t] + dp[i][m][s][t]) % MOD;
                    }
                }
            }
            
            // 行内转移
            for (int j = 0; j < m; j++) {
                for (int s = 0; s < (1 << (2 * m + 2)); s++) {
                    for (int t = 0; t <= K; t++) {
                        if (dp[i][j][s][t] == 0) continue;
                        
                        // 获取当前格子左边和上面的插头状态
                        int left = j > 0 ? ((s >> (2 * (j - 1))) & 3) : 0;
                        int up = (s >> (2 * j)) & 3;
                        
                        // 如果是障碍格子
                        if (grid[i][j] == 1) {
                            // 只能在没有插头的情况下转移
                            if (left == 0 && up == 0) {
                                int newState = s & (~((3 << (2 * (j - 1))) | (3 << (2 * j))));
                                dp[i][j+1][newState][t] = (dp[i][j+1][newState][t] + dp[i][j][s][t]) % MOD;
                            }
                        } else {
                            // 可通行格子
                            
                            // 1. 不放置插头（合并两个插头）
                            if (left != 0 && up != 0) {
                                int newState = s;
                                newState &= ~((3 << (2 * (j - 1))) | (3 << (2 * j)));
                                
                                // 如果两个插头属于不同连通分量，则合并
                                // 如果两个插头属于相同连通分量，则形成新回路
                                if (left == up) {
                                    // 形成新回路
                                    if (t + 1 <= K) {
                                        dp[i][j+1][newState][t+1] = (dp[i][j+1][newState][t+1] + dp[i][j][s][t]) % MOD;
                                    }
                                } else {
                                    // 合并连通分量
                                    // 需要重新编号保持最小表示法
                                    newState = renumber(newState, j-1, j, left, up);
                                    dp[i][j+1][newState][t] = (dp[i][j+1][newState][t] + dp[i][j][s][t]) % MOD;
                                }
                            }
                            
                            // 2. 延续插头
                            if (left != 0 && up == 0) {
                                // 延续左插头到上方
                                int newState = s;
                                newState &= ~(3 << (2 * (j - 1)));
                                newState |= (left << (2 * j));
                                dp[i][j+1][newState][t] = (dp[i][j+1][newState][t] + dp[i][j][s][t]) % MOD;
                            }
                            
                            if (left == 0 && up != 0) {
                                // 延续上插头到左方
                                int newState = s;
                                newState &= ~(3 << (2 * j));
                                newState |= (up << (2 * (j - 1)));
                                dp[i][j+1][newState][t] = (dp[i][j+1][newState][t] + dp[i][j][s][t]) % MOD;
                            }
                            
                            // 3. 创建新插头对（如果左右和上方都没有插头）
                            if (left == 0 && up == 0) {
                                // 创建一对新插头（左插头和上插头）
                                int newState = s | (1 << (2 * (j - 1))) | (1 << (2 * j));
                                dp[i][j+1][newState][t] = (dp[i][j+1][newState][t] + dp[i][j][s][t]) % MOD;
                            }
                        }
                    }
                }
            }
        }
        
        // 统计形成恰好K个回路的方案数
        int result = 0;
        for (int s = 0; s < (1 << (2 * m)); s++) {
            result = (result + dp[n][0][s][K]) % MOD;
        }
        return result;
    }
    
    /**
     * 重新编号以保持最小表示法
     */
    public static int renumber(int state, int pos1, int pos2, int id1, int id2) {
        // 合并两个连通分量，将id2的编号改为id1
        int minId = Math.min(id1, id2);
        int maxId = Math.max(id1, id2);
        
        int m = (state >> (2 * pos1)) & 3;
        if (m == maxId) {
            state &= ~(3 << (2 * pos1));
            state |= (minId << (2 * pos1));
        }
        
        m = (state >> (2 * pos2)) & 3;
        if (m == maxId) {
            state &= ~(3 << (2 * pos2));
            state |= (minId << (2 * pos2));
        }
        
        return state;
    }
    
    // 测试用例
    public static void main(String[] args) {
        int[][] maze1 = {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
        };
        System.out.println(solve(3, 3, 2, maze1)); // 输出形成2个回路的方案数
    }
}
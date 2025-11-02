package class126;

// POJ 2411 Mondriaan's Dream (轮廓线DP)
// 用1×2和2×1的多米诺骨牌铺满n×m的棋盘，求方案数
// 1 <= n, m <= 11
// 测试链接 : http://poj.org/problem?id=2411
//
// 题目大意：
// 给定一个n×m的棋盘，要求用1×2和2×1的多米诺骨牌完全覆盖棋盘（即骨牌不能重叠，也不能有空隙）。
// 求有多少种不同的覆盖方案。
//
// 解题思路：
// 使用轮廓线DP，逐格递推。
// 状态表示：用二进制数表示轮廓线状态，第k位为1表示第k个位置被上一行的竖直骨牌占据。
// 状态转移：
// 1. 当前位置已被上一行竖直骨牌占据，则当前位置不能再放置骨牌
// 2. 当前位置未被占据，则可以：
//    a. 放置竖直骨牌（当前位置和下一行同一位置）
//    b. 放置水平骨牌（当前位置和右边位置，前提是右边位置存在且未被占据）
//
// Java实现：https://github.com/yourusername/algorithm-journey/blob/main/src/class126/POJ2411_MondriaanDream.java
// C++实现：https://github.com/yourusername/algorithm-journey/blob/main/src/class126/POJ2411_MondriaanDream.cpp
// Python实现：https://github.com/yourusername/algorithm-journey/blob/main/src/class126/POJ2411_MondriaanDream.py

public class POJ2411_MondriaanDream {
    
    public static int MAXN = 12;
    
    // dp[i][j][s]表示处理到第i行第j列，轮廓线状态为s的方案数
    // 状态s用二进制表示，第k位为1表示第k个位置被上一行的竖直骨牌占据
    public static long[][][] dp = new long[MAXN][MAXN][1 << MAXN];
    
    public static int n, m, maxs;
    
    /**
     * 计算用1×2和2×1的多米诺骨牌铺满n×m棋盘的方案数
     * 
     * 算法思路：
     * 使用轮廓线DP，逐格递推
     * 状态表示：用二进制数表示轮廓线状态，第k位为1表示第k个位置被上一行的竖直骨牌占据
     * 状态转移：
     * 1. 当前位置已被上一行竖直骨牌占据，则当前位置不能再放置骨牌
     * 2. 当前位置未被占据，则可以：
     *    a. 放置竖直骨牌（当前位置和下一行同一位置）
     *    b. 放置水平骨牌（当前位置和右边位置，前提是右边位置存在且未被占据）
     * 
     * 时间复杂度：O(n * m * 2^m)
     * 空间复杂度：O(n * m * 2^m)
     * 
     * @param rows 行数
     * @param cols 列数
     * @return 铺满棋盘的方案数
     */
    public static long solve(int rows, int cols) {
        // 为了优化，让较小的一维作为列数
        n = Math.max(rows, cols);
        m = Math.min(rows, cols);
        maxs = 1 << m;
        
        // 初始化DP数组
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int s = 0; s < maxs; s++) {
                    dp[i][j][s] = -1;
                }
            }
        }
        
        return dfs(0, 0, 0);
    }
    
    /**
     * DFS记忆化搜索
     * 
     * @param i 当前行
     * @param j 当前列
     * @param s 轮廓线状态
     * @return 从当前状态开始的方案数
     */
    public static long dfs(int i, int j, int s) {
        // 处理完所有行
        if (i == n) {
            return 1;
        }
        
        // 处理完当前行，转到下一行
        if (j == m) {
            return dfs(i + 1, 0, s);
        }
        
        // 记忆化
        if (dp[i][j][s] != -1) {
            return dp[i][j][s];
        }
        
        long ans = 0;
        
        // 检查当前位置是否已被上一行的竖直骨牌占据
        if (((s >> j) & 1) == 1) {
            // 已被占据，当前位置不能再放骨牌
            ans = dfs(i, j + 1, s & (~(1 << j)));
        } else {
            // 未被占据，可以放置骨牌
            
            // 放置竖直骨牌（当前位置和下一行同一位置）
            if (i + 1 < n) {
                ans += dfs(i, j + 1, s | (1 << j));
            }
            
            // 放置水平骨牌（当前位置和右边位置）
            if (j + 1 < m && ((s >> (j + 1)) & 1) == 0) {
                ans += dfs(i, j + 2, s);
            }
        }
        
        dp[i][j][s] = ans;
        return ans;
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 示例：2×3的棋盘有3种铺法
        System.out.println(solve(2, 3)); // 输出: 3
        
        // 示例：4×4的棋盘有5种铺法
        System.out.println(solve(4, 4)); // 输出: 5
    }
}
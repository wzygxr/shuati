// -*- coding: utf-8 -*-
/**
 * 概率/期望DP算法
 *
 * 问题描述：
 * 概率/期望DP是一种处理概率和期望问题的动态规划方法。通常使用逆序DP（从最终状态向初始状态推导），
 * 特别适用于马尔可夫决策过程。
 *
 * 主要特点：
 * 1. 状态转移涉及概率
 * 2. 通常使用逆序DP，因为最终状态的概率或期望往往是已知的
 * 3. 涉及期望的线性性质和全概率公式
 *
 * 时间复杂度：根据具体问题而定，通常为O(n^2)或O(n^3)
 * 空间复杂度：O(n^2)，需要存储状态的概率或期望
 *
 * 相关题目：
 * 1. LeetCode 808. 分汤
 * 2. LeetCode 688. 骑士在棋盘上的概率
 * 3. LeetCode 576. 出界的路径数
 */

import java.util.HashMap;
import java.util.Map;

public class ProbabilityDP {
    
    /**
     * LeetCode 808. 分汤
     * 题目链接：https://leetcode-cn.com/problems/soup-servings/
     * 
     * 问题描述：
     * 有 A 和 B 两种类型的汤，一开始每种类型的汤有 N 毫升。有四种分配操作：
     * 1. 提供 100ml 的汤A和 0ml 的汤B
     * 2. 提供 75ml 的汤A和 25ml 的汤B
     * 3. 提供 50ml 的汤A和 50ml 的汤B
     * 4. 提供 25ml 的汤A和 75ml 的汤B
     * 
     * 当我们把汤分配给某人之后，汤就没有了。每个回合，我们将从四种概率均等的操作中选择一种，
     * 然后分配汤。如果汤的剩余量不足以完成某次操作，我们将尽可能分配。当两种类型的汤都分配完毕时，
     * 停止操作。
     * 
     * 返回汤A先分配完的概率加上汤A和汤B同时分配完的概率的一半。
     * 
     * 解题思路：
     * 使用记忆化搜索进行逆序DP。定义dp[i][j]表示当A有i毫升，B有j毫升时，所求的概率。
     * 最终状态是：当i=0且j>0时，概率为1；当i=0且j=0时，概率为0.5；当i>0且j=0时，概率为0。
     * 
     * @param n 初始时每种汤的毫升数
     * @return 所求的概率
     */
    public static double soupServings(int n) {
        // 当n很大时，概率趋近于1，可以直接返回1
        if (n >= 5000) {
            return 1.0;
        }
        
        // 将毫升数转换为25的倍数，减少状态数
        n = (n + 24) / 25;
        
        // 使用记忆化搜索
        Map<String, Double> memo = new HashMap<>();
        
        return dfs(n, n, memo);
    }
    
    private static double dfs(int a, int b, Map<String, Double> memo) {
        // 边界条件
        if (a <= 0 && b <= 0) {
            return 0.5;
        }
        if (a <= 0) {
            return 1.0;
        }
        if (b <= 0) {
            return 0.0;
        }
        
        // 生成键以检查是否已经计算过
        String key = a + "," + b;
        if (memo.containsKey(key)) {
            return memo.get(key);
        }
        
        // 四种操作，每种操作的概率是0.25
        double prob = 0.25 * (dfs(a-4, b, memo) + dfs(a-3, b-1, memo) + 
                             dfs(a-2, b-2, memo) + dfs(a-1, b-3, memo));
        memo.put(key, prob);
        return prob;
    }
    
    /**
     * LeetCode 688. 骑士在棋盘上的概率
     * 题目链接：https://leetcode-cn.com/problems/knight-probability-in-chessboard/
     * 
     * 问题描述：
     * 在一个n x n的国际象棋棋盘上，一个骑士从单元格 (row, column) 开始，并尝试进行 k 次移动。
     * 行和列是 0 索引的，所以左上单元格是 (0,0)，右下单元格是 (n-1, n-1)。
     * 象棋骑士有8种可能的走法，每次移动在基本方向上是两个单元格，然后在垂直方向上是一个单元格。
     * 每次骑士要移动时，它都会随机从8种可能的移动中选择一种（即使棋子会离开棋盘），然后移动到那里。
     * 骑士继续移动，直到它走了k步或离开了棋盘。
     * 返回骑士在k步移动后仍留在棋盘上的概率。
     * 
     * 解题思路：
     * 使用动态规划，定义dp[step][i][j]表示骑士在step步后位于(i,j)的概率。
     * 初始状态是dp[0][row][column] = 1。
     * 状态转移时，考虑骑士从当前位置的8种可能移动。
     * 
     * @param n 棋盘大小
     * @param k 移动次数
     * @param row 起始行
     * @param column 起始列
     * @return 骑士在k步移动后仍留在棋盘上的概率
     */
    public static double knightProbability(int n, int k, int row, int column) {
        // 定义骑士的8种移动方式
        int[][] directions = {{-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
                             {1, -2}, {1, 2}, {2, -1}, {2, 1}};
        
        // 使用二维DP数组，只需要保存上一步的状态
        double[][] dp_prev = new double[n][n];
        dp_prev[row][column] = 1.0;  // 初始位置的概率为1
        
        for (int step = 0; step < k; ++step) {
            double[][] dp_curr = new double[n][n];
            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < n; ++j) {
                    if (dp_prev[i][j] > 0) {
                        // 从当前位置出发，尝试8种移动
                        for (int[] dir : directions) {
                            int ni = i + dir[0];
                            int nj = j + dir[1];
                            // 如果移动后仍在棋盘内
                            if (ni >= 0 && ni < n && nj >= 0 && nj < n) {
                                dp_curr[ni][nj] += dp_prev[i][j] / 8.0;
                            }
                        }
                    }
                }
            }
            dp_prev = dp_curr;
        }
        
        // 所有留在棋盘上的位置的概率之和
        double total = 0.0;
        for (double[] rowProb : dp_prev) {
            for (double prob : rowProb) {
                total += prob;
            }
        }
        return total;
    }
    
    /**
     * LeetCode 576. 出界的路径数
     * 题目链接：https://leetcode-cn.com/problems/out-of-boundary-paths/
     * 
     * 问题描述：
     * 给你一个 m x n 的网格和一个球。球的起始坐标为 (start_row, start_column) 。
     * 你可以将球移到在四个方向上相邻的单元格内（可以穿过网格边界到达网格之外）。
     * 你最多可以移动 max_move 次球。
     * 找出并返回可以使球停留在边界之外的路径数量。答案可能非常大，返回对 10^9 + 7 取余后的结果。
     * 
     * 解题思路：
     * 使用动态规划，定义dp[step][i][j]表示在step步后位于(i,j)的路径数。
     * 初始状态是dp[0][start_row][start_column] = 1。
     * 状态转移时，考虑从当前位置的4种可能移动。
     * 统计所有出界的路径数。
     * 
     * @param m 网格行数
     * @param n 网格列数
     * @param max_move 最大移动次数
     * @param start_row 起始行
     * @param start_column 起始列
     * @return 出界的路径数对10^9 + 7取余的结果
     */
    public static int findPaths(int m, int n, int max_move, int start_row, int start_column) {
        final int MOD = 1000000007;
        
        // 定义四个方向
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        
        // 使用二维DP数组，只需要保存上一步的状态
        long[][] dp_prev = new long[m][n];
        dp_prev[start_row][start_column] = 1;  // 初始位置的路径数为1
        
        long out_count = 0;
        
        for (int step = 0; step < max_move; ++step) {
            long[][] dp_curr = new long[m][n];
            for (int i = 0; i < m; ++i) {
                for (int j = 0; j < n; ++j) {
                    if (dp_prev[i][j] > 0) {
                        // 从当前位置出发，尝试4种移动
                        for (int[] dir : directions) {
                            int ni = i + dir[0];
                            int nj = j + dir[1];
                            // 如果移动后出界
                            if (ni < 0 || ni >= m || nj < 0 || nj >= n) {
                                out_count = (out_count + dp_prev[i][j]) % MOD;
                            } else {
                                dp_curr[ni][nj] = (dp_curr[ni][nj] + dp_prev[i][j]) % MOD;
                            }
                        }
                    }
                }
            }
            dp_prev = dp_curr;
        }
        
        return (int) (out_count % MOD);
    }
    
    /**
     * 期望DP示例：爬楼梯问题的期望版本
     * 
     * 问题描述：
     * 有一个n级的楼梯，每次可以爬1级或2级，但是爬1级的概率是p，爬2级的概率是1-p。
     * 求爬到第n级楼梯的期望步数。
     * 
     * 解题思路：
     * 使用逆序DP，定义E[i]表示从第i级爬到第n级的期望步数。
     * 边界条件是E[n] = 0。
     * 状态转移方程是E[i] = 1 + p * E[i+1] + (1-p) * E[i+2]。
     * 
     * @param n 楼梯的级数
     * @return 爬到第n级楼梯的期望步数
     */
    public static double expectationDP(int n) {
        if (n <= 0) {
            return 0.0;
        }
        
        // 假设p=0.5，每次爬1级或2级的概率相等
        double p = 0.5;
        
        // 使用逆序DP
        double[] E = new double[n + 2];  // E[i]表示从第i级爬到第n级的期望步数
        
        // 从n-1级开始逆推
        for (int i = n - 1; i >= 0; --i) {
            if (i + 2 <= n) {
                E[i] = 1.0 + p * E[i+1] + (1 - p) * E[i+2];
            } else {
                // 当i+2 > n时，只能爬1级
                E[i] = 1.0 + E[i+1];
            }
        }
        
        return E[0];
    }
    
    // 测试代码
    public static void main(String[] args) {
        // 测试LeetCode 808
        System.out.printf("分汤概率 (n=500): %.6f\n", soupServings(500));
        
        // 测试LeetCode 688
        System.out.printf("骑士在棋盘上的概率: %.6f\n", knightProbability(3, 2, 0, 0));  // 应该输出 0.0625
        
        // 测试LeetCode 576
        System.out.println("出界的路径数: " + findPaths(2, 2, 2, 0, 0));  // 应该输出 6
        
        // 测试期望DP示例
        System.out.printf("爬楼梯的期望步数 (n=10): %.6f\n", expectationDP(10));
    }
}
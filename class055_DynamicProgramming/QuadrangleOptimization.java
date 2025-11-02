// -*- coding: utf-8 -*-
/**
 * 四边形不等式优化（区间DP降阶）
 *
 * 问题描述：
 * 四边形不等式优化是一种用于优化区间DP的方法，可以将时间复杂度从O(n^3)降低到O(n^2)。
 * 当区间DP满足四边形不等式性质和决策单调性时，可以使用这种优化方法。
 *
 * 四边形不等式性质：
 * 对于任意的a ≤ b ≤ c ≤ d，有w(a, d) + w(b, c) ≥ w(a, c) + w(b, d)
 *
 * 决策单调性：
 * 对于区间DP问题dp[i][j] = min{dp[i][k] + dp[k+1][j] + w(i, j)} (i ≤ k < j)
 * 如果决策点s[i][j]表示dp[i][j]取得最小值时的k值，
 * 且满足s[i][j-1] ≤ s[i][j] ≤ s[i+1][j]，则具有决策单调性。
 *
 * 时间复杂度：
 * 优化前：O(n^3)
 * 优化后：O(n^2)
 *
 * 空间复杂度：O(n^2)
 *
 * 相关题目：
 * 1. 石子合并问题
 * 2. LeetCode 312. 戳气球
 * 3. LeetCode 1000. 合并石头的最低成本
 */

import java.util.Arrays;

public class QuadrangleOptimization {
    
    /**
     * 石子合并问题（四边形不等式优化版）
     * 
     * 问题描述：
     * 有n堆石子排成一行，每堆石子有一定的数量。现在要将这些石子合并成一堆，
     * 每次只能合并相邻的两堆，合并的代价是两堆石子的总数。求最小的合并代价。
     * 
     * 解题思路：
     * 使用区间DP，定义dp[i][j]表示合并第i到第j堆石子的最小代价。
     * 状态转移方程：dp[i][j] = min{dp[i][k] + dp[k+1][j]} + sum(stones[i...j])，其中i ≤ k < j
     * 
     * 使用四边形不等式优化，记录最优决策点s[i][j]，表示dp[i][j]取得最小值时的k值。
     * 利用决策单调性s[i][j-1] ≤ s[i][j] ≤ s[i+1][j]来缩小k的搜索范围。
     * 
     * @param stones 每堆石子的数量数组
     * @return 最小的合并代价
     */
    public static int stoneGame(int[] stones) {
        int n = stones.length;
        // 计算前缀和
        int[] prefixSum = new int[n + 1];
        for (int i = 1; i <= n; ++i) {
            prefixSum[i] = prefixSum[i-1] + stones[i-1];
        }
        
        // dp[i][j]表示合并第i到第j堆石子的最小代价
        int[][] dp = new int[n + 1][n + 1];
        // s[i][j]记录dp[i][j]取得最小值时的k值
        int[][] s = new int[n + 1][n + 1];
        
        // 初始化
        for (int i = 1; i <= n; ++i) {
            s[i][i] = i;
        }
        
        // 枚举区间长度
        for (int length = 2; length <= n; ++length) {
            // 枚举起点
            for (int i = 1; i <= n - length + 1; ++i) {
                int j = i + length - 1;
                // 初始化dp[i][j]为无穷大
                dp[i][j] = Integer.MAX_VALUE;
                // 利用四边形不等式优化，缩小k的搜索范围
                for (int k = s[i][j-1]; k <= s[i+1][j]; ++k) {
                    int cost = dp[i][k] + dp[k+1][j] + (prefixSum[j] - prefixSum[i-1]);
                    if (cost < dp[i][j]) {
                        dp[i][j] = cost;
                        s[i][j] = k;
                    }
                }
            }
        }
        
        return dp[1][n];
    }
    
    /**
     * LeetCode 312. 戳气球
     * 题目链接：https://leetcode-cn.com/problems/burst-balloons/
     * 
     * 问题描述：
     * 有n个气球，编号为0到n-1，每个气球上都标有一个数字，这些数字存在数组nums中。
     * 现在要求你戳破所有的气球。戳破第i个气球，你可以获得nums[i-1] * nums[i] * nums[i+1]枚硬币。
     * 这里的i-1和i+1代表和i相邻的两个气球的序号。如果i-1或i+1超出了数组的边界，那么就当它是一个数字为1的气球。
     * 求所能获得硬币的最大数量。
     * 
     * 解题思路：
     * 使用区间DP，定义dp[i][j]表示戳破i到j之间的所有气球（不包括i和j）能获得的最大硬币数。
     * 状态转移方程：dp[i][j] = max{dp[i][k] + dp[k][j] + nums[i] * nums[k] * nums[j]}，其中i < k < j
     * 
     * 虽然这道题不严格满足四边形不等式，但可以使用类似的优化思路。
     * 
     * @param nums 气球上的数字数组
     * @return 能获得的最大硬币数
     */
    public static int maxCoins(int[] nums) {
        int n = nums.length;
        // 添加边界条件，将问题转化为在[0, n+1]之间戳气球
        int[] newNums = new int[n + 2];
        newNums[0] = 1;
        newNums[n + 1] = 1;
        for (int i = 1; i <= n; ++i) {
            newNums[i] = nums[i-1];
        }
        
        // dp[i][j]表示戳破i到j之间的所有气球（不包括i和j）能获得的最大硬币数
        int[][] dp = new int[n + 2][n + 2];
        
        // 枚举区间长度
        for (int length = 2; length <= n + 1; ++length) {
            // 枚举起点
            for (int i = 0; i <= n + 1 - length; ++i) {
                int j = i + length;
                // 枚举最后一个戳破的气球k
                for (int k = i + 1; k < j; ++k) {
                    dp[i][j] = Math.max(dp[i][j], dp[i][k] + dp[k][j] + newNums[i] * newNums[k] * newNums[j]);
                }
            }
        }
        
        return dp[0][n + 1];
    }
    
    /**
     * LeetCode 1000. 合并石头的最低成本
     * 题目链接：https://leetcode-cn.com/problems/minimum-cost-to-merge-stones/
     * 
     * 问题描述：
     * 有N堆石头排成一排，第i堆中有stones[i]块石头。
     * 每次移动需要将连续的K堆石头合并为一堆，而这个移动的成本为这K堆石头的总数。
     * 找出把所有石头合并成一堆的最低成本。如果不可能，返回-1。
     * 
     * 解题思路：
     * 首先检查是否能将所有石头合并成一堆，即(n-1) % (k-1) == 0。
     * 使用区间DP，定义dp[i][j]表示将第i到第j堆石头合并成最少堆数的最小成本。
     * 
     * @param stones 每堆石头的数量数组
     * @param k 每次合并的堆数
     * @return 最低成本，如果不可能返回-1
     */
    public static int mergeStones(int[] stones, int k) {
        int n = stones.length;
        // 检查是否能合并成一堆
        if ((n - 1) % (k - 1) != 0) {
            return -1;
        }
        
        // 计算前缀和
        int[] prefixSum = new int[n + 1];
        for (int i = 1; i <= n; ++i) {
            prefixSum[i] = prefixSum[i-1] + stones[i-1];
        }
        
        // dp[i][j]表示将第i到第j堆石头合并的最小成本
        int[][] dp = new int[n + 1][n + 1];
        
        // 初始化dp数组
        for (int i = 0; i <= n; ++i) {
            Arrays.fill(dp[i], Integer.MAX_VALUE);
        }
        for (int i = 1; i <= n; ++i) {
            dp[i][i] = 0;
        }
        
        // 枚举区间长度
        for (int length = k; length <= n; ++length) {
            // 枚举起点
            for (int i = 1; i <= n - length + 1; ++i) {
                int j = i + length - 1;
                // 枚举分割点，步长为k-1
                for (int m = i; m < j; m += k-1) {
                    if (dp[i][m] != Integer.MAX_VALUE && dp[m+1][j] != Integer.MAX_VALUE) {
                        dp[i][j] = Math.min(dp[i][j], dp[i][m] + dp[m+1][j]);
                    }
                }
                // 如果可以合并成一堆，加上总和
                if ((j - i) % (k - 1) == 0 && dp[i][j] != Integer.MAX_VALUE) {
                    dp[i][j] += prefixSum[j] - prefixSum[i-1];
                }
            }
        }
        
        return dp[1][n];
    }
    
    /**
     * 矩阵链乘法问题（四边形不等式优化版）
     * 
     * 问题描述：
     * 给定一系列矩阵A1, A2, ..., An，其中Ai的维度是dims[i-1] × dims[i]。
     * 求计算这n个矩阵的乘积所需的最少标量乘法次数。
     * 
     * 解题思路：
     * 使用区间DP，定义dp[i][j]表示计算矩阵Ai到Aj的乘积所需的最少标量乘法次数。
     * 状态转移方程：dp[i][j] = min{dp[i][k] + dp[k+1][j] + dims[i-1] * dims[k] * dims[j]}，其中i ≤ k < j
     * 
     * 使用四边形不等式优化，记录最优决策点s[i][j]，表示dp[i][j]取得最小值时的k值。
     * 
     * @param dims 矩阵的维度数组，其中dims[i-1] × dims[i]是第i个矩阵的维度
     * @return 最少标量乘法次数
     */
    public static int matrixChainMultiplication(int[] dims) {
        int n = dims.length - 1;  // 矩阵的数量
        
        // dp[i][j]表示计算矩阵Ai到Aj的乘积所需的最少标量乘法次数
        int[][] dp = new int[n + 1][n + 1];
        // s[i][j]记录dp[i][j]取得最小值时的k值
        int[][] s = new int[n + 1][n + 1];
        
        // 初始化
        for (int i = 1; i <= n; ++i) {
            s[i][i] = i;
        }
        
        // 枚举区间长度
        for (int length = 2; length <= n; ++length) {
            // 枚举起点
            for (int i = 1; i <= n - length + 1; ++i) {
                int j = i + length - 1;
                // 初始化dp[i][j]为无穷大
                dp[i][j] = Integer.MAX_VALUE;
                // 利用四边形不等式优化，缩小k的搜索范围
                for (int k = s[i][j-1]; k <= s[i+1][j]; ++k) {
                    int cost = dp[i][k] + dp[k+1][j] + dims[i-1] * dims[k] * dims[j];
                    if (cost < dp[i][j]) {
                        dp[i][j] = cost;
                        s[i][j] = k;
                    }
                }
            }
        }
        
        return dp[1][n];
    }
    
    // 测试代码
    public static void main(String[] args) {
        // 测试石子合并问题
        int[] stones1 = {4, 1, 1, 4};
        System.out.println("石子合并最小代价: " + stoneGame(stones1));  // 应该输出 18
        
        // 测试LeetCode 312
        int[] nums = {3, 1, 5, 8};
        System.out.println("戳气球最大硬币数: " + maxCoins(nums));  // 应该输出 167
        
        // 测试LeetCode 1000
        int[] stones2 = {3, 2, 4, 1};
        int k = 2;
        System.out.println("合并石头最低成本: " + mergeStones(stones2, k));  // 应该输出 20
        
        // 测试矩阵链乘法
        int[] dims = {30, 35, 15, 5, 10, 20, 25};
        System.out.println("矩阵链乘法最少标量乘法次数: " + matrixChainMultiplication(dims));  // 应该输出 15125
    }
}
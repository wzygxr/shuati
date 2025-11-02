package class076;

/**
 * LeetCode 1000. 合并石头的最低成本
 * 题目链接：https://leetcode.cn/problems/minimum-cost-to-merge-stones/
 * 难度：困难
 * 
 * 题目描述：
 * 有 N 堆石头排成一排，第 i 堆中有 stones[i] 块石头。
 * 每次移动（move）需要将连续的 K 堆石头合并为一堆，而这个移动的成本为这 K 堆石头的总数。
 * 找出把所有石头合并成一堆的最低成本。如果不可能，返回 -1。
 * 
 * 解题思路：
 * 这是一个经典的区间动态规划问题，需要处理K堆合并的特殊情况。
 * 状态定义：dp[i][j]表示将区间[i,j]的石头合并成若干堆的最小成本
 * 状态转移：枚举分割点k，将区间分成两部分进行合并
 * 
 * 时间复杂度：O(n^3 * K)，其中n为石头堆数
 * 空间复杂度：O(n^2)
 * 
 * 工程化考量：
 * 1. 边界条件处理：当K=1时直接返回0，当(n-1)%(K-1)!=0时返回-1
 * 2. 前缀和优化：使用前缀和数组快速计算区间和
 * 3. 状态压缩：可以优化空间复杂度到O(n)
 * 
 * 测试用例：
 * 输入：stones = [3,2,4,1], K = 2
 * 输出：20
 * 解释：合并过程为 [3, 2, 4, 1] -> [5, 4, 1] -> [5, 5] -> [10]，成本为 5+5+10=20
 * 
 * 相关题目扩展：
 * 1. LeetCode 1000. 合并石头的最低成本 - https://leetcode.cn/problems/minimum-cost-to-merge-stones/
 * 2. LeetCode 312. 戳气球 - https://leetcode.cn/problems/burst-balloons/
 * 3. LeetCode 1547. 切棍子的最小成本 - https://leetcode.cn/problems/minimum-cost-to-cut-a-stick/
 * 4. LeetCode 1039. 多边形三角剖分的最低得分 - https://leetcode.cn/problems/minimum-score-triangulation-of-polygon/
 * 5. LintCode 1000. 合并石头的最低成本 - https://www.lintcode.com/problem/1000/
 * 6. LintCode 1063. 凸多边形的三角剖分 - https://www.lintcode.com/problem/1063/
 * 7. HackerRank - Sherlock and Cost - https://www.hackerrank.com/challenges/sherlock-and-cost/problem
 * 8. Codeforces 1327D - Infinite Path - https://codeforces.com/problemset/problem/1327/D
 * 9. AtCoder ABC144D - Water Bottle - https://atcoder.jp/contests/abc144/tasks/abc144_d
 * 10. 洛谷 P1880 [NOI1995] 石子合并 - https://www.luogu.com.cn/problem/P1880
 */
public class Code10_MinimumCostToMergeStones {

    /**
     * 合并石头的最低成本 - 区间动态规划解法
     * @param stones 石头数组
     * @param K 每次合并的堆数
     * @return 最小成本，如果不可能返回-1
     */
    public static int mergeStones(int[] stones, int K) {
        int n = stones.length;
        
        // 特殊情况处理
        if (n == 1) return 0;
        if (K < 2 || (n - 1) % (K - 1) != 0) return -1;
        
        // 前缀和数组，用于快速计算区间和
        int[] prefixSum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + stones[i];
        }
        
        // dp[i][j]表示将区间[i,j]合并成若干堆的最小成本
        int[][] dp = new int[n][n];
        
        // 初始化：单个堆的成本为0
        for (int i = 0; i < n; i++) {
            dp[i][i] = 0;
        }
        
        // 按区间长度从小到大进行动态规划
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                dp[i][j] = Integer.MAX_VALUE;
                
                // 枚举分割点，步长为K-1，因为每次合并K堆会减少K-1堆
                for (int k = i; k < j; k += K - 1) {
                    dp[i][j] = Math.min(dp[i][j], dp[i][k] + dp[k + 1][j]);
                }
                
                // 如果当前区间可以合并成一堆，需要加上合并成本
                if ((len - 1) % (K - 1) == 0) {
                    dp[i][j] += prefixSum[j + 1] - prefixSum[i];
                }
            }
        }
        
        return dp[0][n - 1];
    }
    
    /**
     * 优化版本：使用三维DP，dp[i][j][m]表示将区间[i,j]合并成m堆的最小成本
     * 时间复杂度：O(n^3 * K)
     * 空间复杂度：O(n^2 * K)
     */
    public static int mergeStonesOptimized(int[] stones, int K) {
        int n = stones.length;
        if ((n - 1) % (K - 1) != 0) return -1;
        
        int[] prefixSum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + stones[i];
        }
        
        // dp[i][j][m]：区间[i,j]合并成m堆的最小成本
        int[][][] dp = new int[n][n][K + 1];
        
        // 初始化：单个堆合并成1堆的成本为0
        for (int i = 0; i < n; i++) {
            for (int m = 1; m <= K; m++) {
                if (m == 1) {
                    dp[i][i][m] = 0;
                } else {
                    dp[i][i][m] = Integer.MAX_VALUE / 2; // 防止溢出
                }
            }
        }
        
        // 按区间长度从小到大计算
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                
                // 初始化当前区间各种堆数的成本
                for (int m = 1; m <= K; m++) {
                    dp[i][j][m] = Integer.MAX_VALUE / 2;
                }
                
                // 只能合并成1堆的情况
                dp[i][j][1] = Integer.MAX_VALUE / 2;
                
                // 枚举分割点
                for (int k = i; k < j; k++) {
                    for (int m1 = 1; m1 <= K; m1++) {
                        for (int m2 = 1; m2 <= K; m2++) {
                            if (m1 + m2 <= K) {
                                dp[i][j][m1 + m2] = Math.min(
                                    dp[i][j][m1 + m2], 
                                    dp[i][k][m1] + dp[k + 1][j][m2]
                                );
                            }
                        }
                    }
                }
                
                // 如果可以合并成1堆，需要加上合并成本
                if (dp[i][j][K] < Integer.MAX_VALUE / 2) {
                    dp[i][j][1] = Math.min(
                        dp[i][j][1], 
                        dp[i][j][K] + prefixSum[j + 1] - prefixSum[i]
                    );
                }
            }
        }
        
        return dp[0][n - 1][1] < Integer.MAX_VALUE / 2 ? dp[0][n - 1][1] : -1;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1
        int[] stones1 = {3, 2, 4, 1};
        int K1 = 2;
        System.out.println("测试用例1: stones = [3,2,4,1], K = 2");
        System.out.println("预期结果: 20");
        System.out.println("实际结果: " + mergeStones(stones1, K1));
        System.out.println("优化版本: " + mergeStonesOptimized(stones1, K1));
        System.out.println();
        
        // 测试用例2
        int[] stones2 = {3, 2, 4, 1};
        int K2 = 3;
        System.out.println("测试用例2: stones = [3,2,4,1], K = 3");
        System.out.println("预期结果: -1 (因为(4-1)%(3-1)=1≠0)");
        System.out.println("实际结果: " + mergeStones(stones2, K2));
        System.out.println();
        
        // 测试用例3
        int[] stones3 = {3, 5, 1, 2, 6};
        int K3 = 3;
        System.out.println("测试用例3: stones = [3,5,1,2,6], K = 3");
        System.out.println("预期结果: 25");
        System.out.println("实际结果: " + mergeStones(stones3, K3));
        System.out.println("优化版本: " + mergeStonesOptimized(stones3, K3));
    }
}
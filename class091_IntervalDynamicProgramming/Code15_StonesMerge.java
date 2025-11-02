package class076;

/**
 * AcWing 282. 石子合并
 * 题目链接：https://www.acwing.com/problem/content/284/
 * 难度：简单
 * 
 * 题目描述：
 * 设有N堆石子排成一排，其编号为1，2，3，…，N。
 * 每堆石子有一定的质量，可以用一个整数来描述，现在要将这N堆石子合并成为一堆。
 * 每次只能合并相邻的两堆，合并的代价为这两堆石子的质量之和，合并后与这两堆石子相邻的石子将和新堆相邻。
 * 找出一种合理的方法，使总的代价最小，输出最小代价。
 * 
 * 解题思路：
 * 经典的区间动态规划问题，石子合并问题。
 * 状态定义：dp[i][j]表示合并区间[i,j]的石子所需的最小代价
 * 状态转移：dp[i][j] = min(dp[i][k] + dp[k+1][j] + sum[i][j])
 * 
 * 时间复杂度：O(n^3)
 * 空间复杂度：O(n^2)
 * 
 * 工程化考量：
 * 1. 使用前缀和优化区间和计算
 * 2. 四边形不等式优化可以将时间复杂度优化到O(n^2)
 * 3. 处理边界条件：单个石子代价为0
 * 
 * 相关题目扩展：
 * 1. AcWing 282. 石子合并 - https://www.acwing.com/problem/content/284/
 * 2. LeetCode 1000. 合并石头的最低成本 - https://leetcode.cn/problems/minimum-cost-to-merge-stones/
 * 3. 洛谷 P1880 [NOI1995] 石子合并 - https://www.luogu.com.cn/problem/P1880
 * 4. LintCode 1000. 合并石头的最低成本 - https://www.lintcode.com/problem/1000/
 * 5. LintCode 476. Stone Game - https://www.lintcode.com/problem/476/
 * 6. HackerRank - Sherlock and Cost - https://www.hackerrank.com/challenges/sherlock-and-cost/problem
 * 7. Codeforces 1327D - Infinite Path - https://codeforces.com/problemset/problem/1327/D
 * 8. AtCoder ABC144D - Water Bottle - https://atcoder.jp/contests/abc144/tasks/abc144_d
 * 9. POJ 1141 Brackets Sequence - http://poj.org/problem?id=1141
 * 10. HDU 4632 - Palindrome Subsequence - http://acm.hdu.edu.cn/showproblem.php?pid=4632
 */
public class Code15_StonesMerge {

    /**
     * 区间动态规划解法
     * @param stones 石子重量数组
     * @return 最小合并代价
     */
    public static int minCost(int[] stones) {
        int n = stones.length;
        if (n == 1) return 0;
        
        // 前缀和数组
        int[] prefixSum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + stones[i];
        }
        
        // dp[i][j]表示合并区间[i,j]的最小代价
        int[][] dp = new int[n][n];
        
        // 初始化：单个石子代价为0
        for (int i = 0; i < n; i++) {
            dp[i][i] = 0;
        }
        
        // 区间动态规划：按长度从小到大
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                dp[i][j] = Integer.MAX_VALUE;
                
                // 枚举分割点k
                for (int k = i; k < j; k++) {
                    int cost = dp[i][k] + dp[k + 1][j] + 
                              (prefixSum[j + 1] - prefixSum[i]);
                    dp[i][j] = Math.min(dp[i][j], cost);
                }
            }
        }
        
        return dp[0][n - 1];
    }
    
    /**
     * 四边形不等式优化版本
     * 时间复杂度：O(n^2)
     */
    public static int minCostOptimized(int[] stones) {
        int n = stones.length;
        if (n == 1) return 0;
        
        int[] prefixSum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + stones[i];
        }
        
        int[][] dp = new int[n][n];
        int[][] best = new int[n][n]; // 记录最优分割点
        
        // 初始化
        for (int i = 0; i < n; i++) {
            dp[i][i] = 0;
            best[i][i] = i;
        }
        
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                dp[i][j] = Integer.MAX_VALUE;
                
                // 四边形不等式优化：只在[best[i][j-1], best[i+1][j]]范围内枚举
                int left = best[i][j - 1];
                int right = (j - 1 < i + 1) ? i : best[i + 1][j];
                
                for (int k = left; k <= right; k++) {
                    int cost = dp[i][k] + dp[k + 1][j] + 
                              (prefixSum[j + 1] - prefixSum[i]);
                    if (cost < dp[i][j]) {
                        dp[i][j] = cost;
                        best[i][j] = k;
                    }
                }
            }
        }
        
        return dp[0][n - 1];
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1
        int[] stones1 = {1, 3, 5, 2};
        System.out.println("测试用例1: stones = [1,3,5,2]");
        System.out.println("预期结果: 22");
        System.out.println("DP解法: " + minCost(stones1));
        System.out.println("优化版本: " + minCostOptimized(stones1));
        System.out.println();
        
        // 测试用例2
        int[] stones2 = {4, 2, 1, 3};
        System.out.println("测试用例2: stones = [4,2,1,3]");
        System.out.println("预期结果: 20");
        System.out.println("DP解法: " + minCost(stones2));
        System.out.println("优化版本: " + minCostOptimized(stones2));
        System.out.println();
        
        // 测试用例3（经典例子）
        int[] stones3 = {1, 2, 3, 4, 5};
        System.out.println("测试用例3: stones = [1,2,3,4,5]");
        System.out.println("DP解法: " + minCost(stones3));
        System.out.println("优化版本: " + minCostOptimized(stones3));
    }
    
    /**
     * 复杂度分析：
     * 基本DP解法：
     * - 时间复杂度：O(n^3)
     * - 空间复杂度：O(n^2)
     * 
     * 四边形不等式优化：
     * - 时间复杂度：O(n^2)
     * - 空间复杂度：O(n^2)
     * 
     * 工程化建议：
     * 1. 对于小规模数据(n<100)，使用基本DP即可
     * 2. 对于大规模数据(n>1000)，使用四边形不等式优化
     * 3. 可以使用记忆化搜索简化代码
     */
}
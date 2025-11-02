package class096;

import java.util.Arrays;

// 石子游戏II (LeetCode 1140)
// 题目来源：LeetCode 1140. Stone Game II - https://leetcode.com/problems/stone-game-ii/
// 题目描述：爱丽丝和鲍勃继续他们的石子游戏。许多堆石子排成一行，每堆都有正整数颗石子 piles[i]。
// 游戏以谁手中的石子最多来决出胜负。爱丽丝先开始。
// 在每个玩家的回合中，该玩家可以拿走剩下的石子堆的前 X 堆，其中 1 <= X <= 2M。
// 然后，我们将 M 更新为 max(M, X)。游戏持续到所有石子堆都被拿走。
// 假设爱丽丝和鲍勃都发挥出最佳水平，返回爱丽丝可以得到的最大数量的石子。
//
// 算法核心思想：
// 1. 动态规划：dp[i][m]表示从第i堆开始，当前M值为m时，当前玩家能获得的最大石子数
// 2. 前缀和：使用前缀和数组快速计算区间和
// 3. 状态转移：dp[i][m] = max(当前玩家拿x堆 + 剩余石子总数 - 对手在i+x位置的最优解)
//
// 时间复杂度分析：
// 1. 时间复杂度：O(n^3) - 三重循环，但通过优化可以降低到O(n^2)
// 2. 空间复杂度：O(n^2) - 使用二维dp数组
//
// 工程化考量：
// 1. 异常处理：处理空数组和边界情况
// 2. 性能优化：使用前缀和和记忆化搜索
// 3. 可读性：添加详细注释说明算法原理
// 4. 可扩展性：支持不同的M值限制
public class Code19_StoneGameIILeetCode1140 {
    
    /**
     * 解决石子游戏II问题
     * @param piles 石子堆数组
     * @return 爱丽丝可以得到的最大石子数
     */
    public static int stoneGameII(int[] piles) {
        // 异常处理：处理空数组
        if (piles == null || piles.length == 0) {
            return 0;
        }
        
        int n = piles.length;
        
        // 创建前缀和数组，prefixSum[i]表示前i堆石子的总和
        int[] prefixSum = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            prefixSum[i] = prefixSum[i - 1] + piles[i - 1];
        }
        
        // 创建dp数组，dp[i][m]表示从第i堆开始，当前M值为m时，当前玩家能获得的最大石子数
        int[][] dp = new int[n + 1][n + 1];
        
        // 从后向前递推，因为后面的状态会影响前面的决策
        for (int i = n - 1; i >= 0; i--) {
            for (int m = 1; m <= n; m++) {
                // 如果当前玩家可以拿走所有剩余石子
                if (i + 2 * m >= n) {
                    dp[i][m] = prefixSum[n] - prefixSum[i];
                    continue;
                }
                
                // 当前玩家尝试拿1到2*m堆石子
                int maxStones = 0;
                for (int x = 1; x <= 2 * m; x++) {
                    if (i + x > n) break;
                    
                    // 当前玩家拿x堆石子，获得prefixSum[i+x] - prefixSum[i]石子
                    // 对手从i+x位置开始，新的M值为max(m, x)
                    int opponentStones = dp[i + x][Math.max(m, x)];
                    int currentStones = prefixSum[i + x] - prefixSum[i];
                    
                    // 当前玩家能获得的最大石子数 = 当前拿的石子数 + 剩余总石子数 - 对手获得的最优解
                    maxStones = Math.max(maxStones, currentStones + (prefixSum[n] - prefixSum[i + x] - opponentStones));
                }
                dp[i][m] = maxStones;
            }
        }
        
        return dp[0][1];
    }
    
    /**
     * 优化版本：使用记忆化搜索，避免重复计算
     * 时间复杂度：O(n^2)，空间复杂度：O(n^2)
     */
    public static int stoneGameIIOptimized(int[] piles) {
        if (piles == null || piles.length == 0) {
            return 0;
        }
        
        int n = piles.length;
        int[] prefixSum = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            prefixSum[i] = prefixSum[i - 1] + piles[i - 1];
        }
        
        // 记忆化数组
        int[][] memo = new int[n + 1][n + 1];
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }
        
        return dfs(0, 1, prefixSum, memo, n);
    }
    
    private static int dfs(int i, int m, int[] prefixSum, int[][] memo, int n) {
        // 如果已经处理完所有石子堆
        if (i >= n) {
            return 0;
        }
        
        // 如果当前玩家可以拿走所有剩余石子
        if (i + 2 * m >= n) {
            return prefixSum[n] - prefixSum[i];
        }
        
        // 检查记忆化数组
        if (memo[i][m] != -1) {
            return memo[i][m];
        }
        
        int maxStones = 0;
        // 当前玩家尝试拿1到2*m堆石子
        for (int x = 1; x <= 2 * m; x++) {
            if (i + x > n) break;
            
            // 当前玩家拿x堆石子
            int currentStones = prefixSum[i + x] - prefixSum[i];
            // 对手从i+x位置开始，新的M值为max(m, x)
            int opponentStones = dfs(i + x, Math.max(m, x), prefixSum, memo, n);
            
            // 当前玩家能获得的最大石子数 = 当前拿的石子数 + 剩余总石子数 - 对手获得的最优解
            maxStones = Math.max(maxStones, currentStones + (prefixSum[n] - prefixSum[i + x] - opponentStones));
        }
        
        memo[i][m] = maxStones;
        return maxStones;
    }
    
    // 测试函数
    public static void main(String[] args) {
        // 测试用例1：标准情况
        int[] piles1 = {2, 7, 9, 4, 4};
        System.out.println("测试用例1 [2,7,9,4,4]: " + stoneGameII(piles1)); // 应返回10
        
        // 测试用例2：边界情况
        int[] piles2 = {1, 2, 3, 4, 5, 100};
        System.out.println("测试用例2 [1,2,3,4,5,100]: " + stoneGameII(piles2)); // 应返回104
        
        // 测试用例3：两堆石子
        int[] piles3 = {1, 100};
        System.out.println("测试用例3 [1,100]: " + stoneGameII(piles3)); // 应返回101
        
        // 测试用例4：单堆石子
        int[] piles4 = {100};
        System.out.println("测试用例4 [100]: " + stoneGameII(piles4)); // 应返回100
        
        // 验证优化版本
        System.out.println("优化版本测试:");
        System.out.println("测试用例1 [2,7,9,4,4]: " + stoneGameIIOptimized(piles1));
        System.out.println("测试用例2 [1,2,3,4,5,100]: " + stoneGameIIOptimized(piles2));
        
        // 性能测试：大规模数据
        int[] largePiles = new int[100];
        Arrays.fill(largePiles, 1);
        System.out.println("大规模测试 [100个1]: " + stoneGameIIOptimized(largePiles));
    }
}
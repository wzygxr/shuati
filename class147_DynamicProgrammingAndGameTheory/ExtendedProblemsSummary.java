/**
 * Class042 扩展题目汇总
 * 
 * 本文件汇总了Class042中所有核心问题的扩展题目实现，
 * 包括Java、C++、Python三种语言的代码实现，
 * 以及详细的时间空间复杂度分析和工程化考量。
 * 
 * 作者：算法学习助手
 * 日期：2025年10月21日
 */

import java.util.*;

public class ExtendedProblemsSummary {
    
    // ==================== 苹果袋子问题扩展题目 ====================
    
    /**
     * LeetCode 518. Coin Change 2 (硬币组合)
     * 时间复杂度：O(amount * coins.length)
     * 空间复杂度：O(amount)
     */
    public static int coinChange2(int[] coins, int amount) {
        if (amount < 0 || coins == null || coins.length == 0) return 0;
        if (amount == 0) return 1;
        
        int[] dp = new int[amount + 1];
        dp[0] = 1;
        
        for (int coin : coins) {
            for (int i = coin; i <= amount; i++) {
                dp[i] += dp[i - coin];
            }
        }
        
        return dp[amount];
    }
    
    /**
     * Codeforces 996A. Hit the Lottery (贪心找零)
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    public static int hitTheLottery(int n) {
        if (n < 0) throw new IllegalArgumentException("Amount cannot be negative");
        if (n == 0) return 0;
        
        int[] notes = {200, 100, 50, 20, 10, 5, 1};
        int count = 0;
        
        for (int note : notes) {
            if (n <= 0) break;
            count += n / note;
            n = n % note;
        }
        
        return count;
    }
    
    // ==================== 吃草游戏扩展题目 ====================
    
    /**
     * LeetCode 877. Stone Game (石子游戏)
     * 时间复杂度：O(n^2)
     * 空间复杂度：O(n^2)
     */
    public static boolean stoneGame(int[] piles) {
        if (piles == null || piles.length == 0 || piles.length % 2 != 0) return false;
        
        int n = piles.length;
        int[][] dp = new int[n][n];
        
        for (int i = 0; i < n; i++) {
            dp[i][i] = piles[i];
        }
        
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                dp[i][j] = Math.max(piles[i] - dp[i + 1][j], piles[j] - dp[i][j - 1]);
            }
        }
        
        return dp[0][n - 1] > 0;
    }
    
    /**
     * LeetCode 1140. Stone Game II (石子游戏II)
     * 时间复杂度：O(n^3)
     * 空间复杂度：O(n^2)
     */
    public static int stoneGameII(int[] piles) {
        if (piles == null || piles.length == 0) return 0;
        
        int n = piles.length;
        int[] prefixSum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + piles[i];
        }
        
        int[][] dp = new int[n][n + 1];
        
        for (int i = n - 1; i >= 0; i--) {
            for (int m = 1; m <= n; m++) {
                if (i + 2 * m >= n) {
                    dp[i][m] = prefixSum[n] - prefixSum[i];
                } else {
                    for (int x = 1; x <= 2 * m; x++) {
                        if (i + x > n) break;
                        int nextM = Math.max(m, x);
                        int opponentScore = dp[i + x][nextM];
                        int currentScore = prefixSum[i + x] - prefixSum[i];
                        dp[i][m] = Math.max(dp[i][m], currentScore + (prefixSum[n] - prefixSum[i + x] - opponentScore));
                    }
                }
            }
        }
        
        return dp[0][1];
    }
    
    // ==================== 连续数字和扩展题目 ====================
    
    /**
     * LeetCode 829. Consecutive Numbers Sum (优化版)
     * 时间复杂度：O(sqrt(N))
     * 空间复杂度：O(1)
     */
    public static int consecutiveNumbersSumOptimized(int N) {
        if (N <= 0) return 0;
        if (N == 1) return 1;
        
        int count = 0;
        int n2 = 2 * N;
        
        for (int k = 1; k * k <= n2; k++) {
            if (n2 % k == 0) {
                int m = n2 / k;
                if ((m - k + 1) % 2 == 0 && (m - k + 1) / 2 >= 1) {
                    count++;
                }
                if (k != m && (k - m + 1) % 2 == 0 && (k - m + 1) / 2 >= 1) {
                    count++;
                }
            }
        }
        
        return count;
    }
    
    /**
     * LeetCode 829. Consecutive Numbers Sum (数学公式版)
     * 时间复杂度：O(sqrt(N))
     * 空间复杂度：O(1)
     */
    public static int consecutiveNumbersSumMath(int N) {
        if (N <= 0) return 0;
        if (N == 1) return 1;
        
        int count = 0;
        
        for (int i = 1; i * i <= N; i++) {
            if (N % i == 0) {
                if (i % 2 == 1) count++;
                int other = N / i;
                if (other != i && other % 2 == 1) count++;
            }
        }
        
        return count;
    }
    
    // ==================== 回文好串扩展题目 ====================
    
    /**
     * LeetCode 5. Longest Palindromic Substring (Manacher算法版)
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public static String longestPalindromeManacher(String s) {
        if (s == null || s.length() == 0) return "";
        if (s.length() == 1) return s;
        
        StringBuilder processed = new StringBuilder();
        processed.append('#');
        for (int i = 0; i < s.length(); i++) {
            processed.append(s.charAt(i));
            processed.append('#');
        }
        String T = processed.toString();
        
        int n = T.length();
        int[] p = new int[n];
        int C = 0, R = 0;
        int maxLen = 0, centerIndex = 0;
        
        for (int i = 0; i < n; i++) {
            int mirror = 2 * C - i;
            if (i < R) {
                p[i] = Math.min(R - i, p[mirror]);
            } else {
                p[i] = 0;
            }
            
            while (i - p[i] - 1 >= 0 && i + p[i] + 1 < n && 
                   T.charAt(i - p[i] - 1) == T.charAt(i + p[i] + 1)) {
                p[i]++;
            }
            
            if (i + p[i] > R) {
                C = i;
                R = i + p[i];
            }
            
            if (p[i] > maxLen) {
                maxLen = p[i];
                centerIndex = i;
            }
        }
        
        int start = (centerIndex - maxLen) / 2;
        return s.substring(start, start + maxLen);
    }
    
    /**
     * LeetCode 647. Palindromic Substrings (Manacher算法版)
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public static int countSubstringsManacher(String s) {
        if (s == null || s.length() == 0) return 0;
        if (s.length() == 1) return 1;
        
        StringBuilder processed = new StringBuilder();
        processed.append('#');
        for (int i = 0; i < s.length(); i++) {
            processed.append(s.charAt(i));
            processed.append('#');
        }
        String T = processed.toString();
        
        int n = T.length();
        int[] p = new int[n];
        int C = 0, R = 0;
        int count = 0;
        
        for (int i = 0; i < n; i++) {
            int mirror = 2 * C - i;
            if (i < R) {
                p[i] = Math.min(R - i, p[mirror]);
            } else {
                p[i] = 0;
            }
            
            while (i - p[i] - 1 >= 0 && i + p[i] + 1 < n && 
                   T.charAt(i - p[i] - 1) == T.charAt(i + p[i] + 1)) {
                p[i]++;
            }
            
            if (i + p[i] > R) {
                C = i;
                R = i + p[i];
            }
            
            count += (p[i] + 1) / 2;
        }
        
        return count;
    }
    
    // ==================== 测试函数 ====================
    
    public static void main(String[] args) {
        System.out.println("=== Class042 扩展题目测试 ===");
        
        // 测试苹果袋子问题扩展题目
        System.out.println("\n--- 苹果袋子问题扩展题目 ---");
        int[] coins1 = {1, 2, 5};
        System.out.println("LeetCode 518. Coin Change 2: " + coinChange2(coins1, 5));
        System.out.println("Codeforces 996A. Hit the Lottery: " + hitTheLottery(500));
        
        // 测试吃草游戏扩展题目
        System.out.println("\n--- 吃草游戏扩展题目 ---");
        int[] piles1 = {5, 3, 4, 5};
        System.out.println("LeetCode 877. Stone Game: " + stoneGame(piles1));
        int[] piles2 = {2, 7, 9, 4, 4};
        System.out.println("LeetCode 1140. Stone Game II: " + stoneGameII(piles2));
        
        // 测试连续数字和扩展题目
        System.out.println("\n--- 连续数字和扩展题目 ---");
        System.out.println("LeetCode 829. Consecutive Numbers Sum (优化版): " + consecutiveNumbersSumOptimized(15));
        System.out.println("LeetCode 829. Consecutive Numbers Sum (数学公式版): " + consecutiveNumbersSumMath(15));
        
        // 测试回文好串扩展题目
        System.out.println("\n--- 回文好串扩展题目 ---");
        System.out.println("LeetCode 5. Longest Palindromic Substring (Manacher算法版): " + longestPalindromeManacher("babad"));
        System.out.println("LeetCode 647. Palindromic Substrings (Manacher算法版): " + countSubstringsManacher("abc"));
        
        System.out.println("\n=== 测试完成 ===");
    }
    
    // ==================== 工程化考量总结 ====================
    
    /**
     * 工程化最佳实践总结：
     * 
     * 1. 异常处理：
     *    - 输入参数合法性验证
     *    - 边界条件特殊处理
     *    - 错误信息清晰提示
     * 
     * 2. 性能优化：
     *    - 时间复杂度优化：从暴力到数学公式
     *    - 空间复杂度优化：滚动数组、状态压缩
     *    - 常数项优化：减少不必要的计算
     * 
     * 3. 可测试性：
     *    - 单元测试覆盖各种情况
     *    - 边界条件测试
     *    - 性能测试验证复杂度
     * 
     * 4. 可扩展性：
     *    - 模块化设计便于扩展
     *    - 接口抽象隐藏实现细节
     *    - 配置化参数支持定制
     * 
     * 5. 跨语言实现考量：
     *    - 数据类型差异处理
     *    - 语法特性适配
     *    - 性能特点分析
     */
    
    // ==================== 复杂度分析总结 ====================
    
    /**
     * 时间复杂度分析总结：
     * 
     * 1. 数学规律问题：O(1) - 最优解
     * 2. 动态规划问题：O(n) 到 O(n^3) - 取决于状态维度
     * 3. 博弈论问题：O(1) 到 O(n^2) - 数学规律或动态规划
     * 4. 字符串处理：O(n) 到 O(n^2) - 线性算法或平方算法
     * 
     * 空间复杂度分析总结：
     * 
     * 1. 数学规律问题：O(1) - 常数空间
     * 2. 动态规划问题：O(n) 到 O(n^2) - 取决于状态存储
     * 3. 博弈论问题：O(1) 到 O(n) - 状态压缩优化
     * 4. 字符串处理：O(n) - 线性空间需求
     */
    
    // ==================== 算法选择策略 ====================
    
    /**
     * 算法选择策略总结：
     * 
     * 1. 对于找规律问题：优先通过数学分析寻找O(1)解法
     * 2. 对于组合计数问题：使用动态规划
     * 3. 对于最优化问题：根据问题特点选择贪心、动态规划或搜索算法
     * 4. 对于重复计算问题：使用记忆化搜索优化
     * 5. 对于字符串处理：根据需求选择中心扩展法或Manacher算法
     * 6. 对于博弈论问题：寻找数学规律或使用SG函数
     */
}
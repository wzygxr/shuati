package class076;

/**
 * LeetCode 1770. 执行乘法运算的最大分数
 * 题目链接：https://leetcode.cn/problems/maximum-score-from-performing-multiplication-operations/
 * 难度：中等
 * 
 * 题目描述：
 * 给你两个长度分别为 n 和 m 的整数数组 nums 和 multipliers。
 * 你需要执行恰好 m 步操作。在第 i 步操作（从 1 开始计数）中，你需要：
 * - 选择数组 nums 开头或者结尾的一个元素 x
 * - 获得 multipliers[i] * x 的分数，并将 x 从数组 nums 中移除
 * 
 * 解题思路：
 * 这是一个区间动态规划问题，需要处理从数组两端取元素的情况。
 * 状态定义：dp[i][j]表示已经取了i个开头元素和j个结尾元素时的最大分数
 * 状态转移：每次可以选择取开头或结尾的元素
 * 
 * 时间复杂度：O(m^2)
 * 空间复杂度：O(m^2)
 * 
 * 工程化考量：
 * 1. 空间优化：使用滚动数组将空间复杂度优化到O(m)
 * 2. 边界条件处理：m可能为0的情况
 * 3. 优化：只考虑必要的状态
 * 
 * 相关题目扩展：
 * 1. LeetCode 1770. 执行乘法运算的最大分数 - https://leetcode.cn/problems/maximum-score-from-performing-multiplication-operations/
 * 2. LeetCode 486. 预测赢家 - https://leetcode.cn/problems/predict-the-winner/
 * 3. LeetCode 877. 石子游戏 - https://leetcode.cn/problems/stone-game/
 * 4. LeetCode 1140. 石子游戏 II - https://leetcode.cn/problems/stone-game-ii/
 * 5. LeetCode 1406. 石子游戏 III - https://leetcode.cn/problems/stone-game-iii/
 * 6. LintCode 390. 石子游戏 - https://www.lintcode.com/problem/390/
 * 7. LintCode 1718. 石子游戏 VI - https://www.lintcode.com/problem/1718/
 * 8. HackerRank - Game of Stones - https://www.hackerrank.com/challenges/game-of-stones-1/problem
 * 9. Codeforces 1312C - Add One - https://codeforces.com/problemset/problem/1312/C
 * 10. AtCoder ABC144D - Water Bottle - https://atcoder.jp/contests/abc144/tasks/abc144_d
 */
public class Code13_MaximumScoreFromPerformingMultiplicationOperations {

    /**
     * 区间动态规划解法
     * @param nums 原始数组
     * @param multipliers 乘数数组
     * @return 最大分数
     */
    public static int maximumScore(int[] nums, int[] multipliers) {
        int n = nums.length;
        int m = multipliers.length;
        
        // dp[i][j]表示取了i个开头元素和j个结尾元素时的最大分数
        int[][] dp = new int[m + 1][m + 1];
        
        // 初始化：没有取任何元素时分数为0
        dp[0][0] = 0;
        
        // 动态规划
        for (int total = 1; total <= m; total++) {
            for (int left = 0; left <= total; left++) {
                int right = total - left;
                
                // 情况1：当前取的是开头元素（第left个开头元素）
                if (left > 0) {
                    int score1 = dp[left - 1][right] + 
                                multipliers[total - 1] * nums[left - 1];
                    if (dp[left][right] < score1) {
                        dp[left][right] = score1;
                    }
                }
                
                // 情况2：当前取的是结尾元素（第right个结尾元素）
                if (right > 0) {
                    int score2 = dp[left][right - 1] + 
                                multipliers[total - 1] * nums[n - right];
                    if (dp[left][right] < score2) {
                        dp[left][right] = score2;
                    }
                }
            }
        }
        
        // 找到最大分数
        int maxScore = Integer.MIN_VALUE;
        for (int left = 0; left <= m; left++) {
            int right = m - left;
            if (dp[left][right] > maxScore) {
                maxScore = dp[left][right];
            }
        }
        
        return maxScore;
    }
    
    /**
     * 优化版本：使用一维DP数组进行空间优化
     * 空间复杂度：O(m)
     */
    public static int maximumScoreOptimized(int[] nums, int[] multipliers) {
        int n = nums.length;
        int m = multipliers.length;
        
        // dp[i]表示取了i个开头元素时的最大分数
        int[] dp = new int[m + 1];
        
        // 初始化：没有取任何元素时分数为0
        dp[0] = 0;
        
        // 动态规划
        for (int op = 0; op < m; op++) {
            int[] nextDp = new int[m + 1];
            
            for (int left = 0; left <= op + 1; left++) {
                int right = op + 1 - left;
                
                // 情况1：当前取的是开头元素
                if (left > 0) {
                    int score1 = dp[left - 1] + multipliers[op] * nums[left - 1];
                    if (nextDp[left] < score1) {
                        nextDp[left] = score1;
                    }
                }
                
                // 情况2：当前取的是结尾元素
                if (right > 0) {
                    int score2 = dp[left] + multipliers[op] * nums[n - right];
                    if (nextDp[left] < score2) {
                        nextDp[left] = score2;
                    }
                }
            }
            
            dp = nextDp;
        }
        
        // 找到最大分数
        int maxScore = Integer.MIN_VALUE;
        for (int score : dp) {
            if (score > maxScore) {
                maxScore = score;
            }
        }
        
        return maxScore;
    }
    
    /**
     * 记忆化搜索解法
     */
    public static int maximumScoreMemo(int[] nums, int[] multipliers) {
        int n = nums.length;
        int m = multipliers.length;
        Integer[][] memo = new Integer[m][m];
        return dfs(nums, multipliers, 0, n - 1, 0, memo);
    }
    
    private static int dfs(int[] nums, int[] multipliers, int left, int right, int op, Integer[][] memo) {
        if (op == multipliers.length) {
            return 0;
        }
        
        if (memo[left][op] != null) {
            return memo[left][op];
        }
        
        // 选择左边元素
        int scoreLeft = nums[left] * multipliers[op] + 
                       dfs(nums, multipliers, left + 1, right, op + 1, memo);
        
        // 选择右边元素
        int scoreRight = nums[right] * multipliers[op] + 
                        dfs(nums, multipliers, left, right - 1, op + 1, memo);
        
        int maxScore = Math.max(scoreLeft, scoreRight);
        memo[left][op] = maxScore;
        return maxScore;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {1, 2, 3};
        int[] multipliers1 = {3, 2, 1};
        System.out.println("测试用例1: nums = [1,2,3], multipliers = [3,2,1]");
        System.out.println("预期结果: 14");
        System.out.println("DP解法: " + maximumScore(nums1, multipliers1));
        System.out.println("优化版本: " + maximumScoreOptimized(nums1, multipliers1));
        System.out.println("记忆化搜索: " + maximumScoreMemo(nums1, multipliers1));
        System.out.println();
        
        // 测试用例2
        int[] nums2 = {-5, -3, -3, -2, 7, 1};
        int[] multipliers2 = {-10, -5, 3, 4, 6};
        System.out.println("测试用例2: 复杂数组");
        System.out.println("DP解法: " + maximumScore(nums2, multipliers2));
        System.out.println("优化版本: " + maximumScoreOptimized(nums2, multipliers2));
        System.out.println("记忆化搜索: " + maximumScoreMemo(nums2, multipliers2));
        System.out.println();
        
        // 性能测试
        int[] largeNums = new int[1000];
        int[] largeMultipliers = new int[500];
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < largeNums.length; i++) {
            largeNums[i] = random.nextInt(2001) - 1000; // -1000到1000
        }
        for (int i = 0; i < largeMultipliers.length; i++) {
            largeMultipliers[i] = random.nextInt(2001) - 1000;
        }
        
        System.out.println("性能测试（nums长度1000，multipliers长度500）：");
        long start = System.currentTimeMillis();
        int result1 = maximumScoreOptimized(largeNums, largeMultipliers);
        long time1 = System.currentTimeMillis() - start;
        
        start = System.currentTimeMillis();
        int result2 = maximumScoreMemo(largeNums, largeMultipliers);
        long time2 = System.currentTimeMillis() - start;
        
        System.out.println("优化版本 - 结果: " + result1 + ", 时间: " + time1 + "ms");
        System.out.println("记忆化搜索 - 结果: " + result2 + ", 时间: " + time2 + "ms");
    }
    
    /**
     * 复杂度分析：
     * 基本DP解法：
     * - 时间复杂度：O(m^2)，其中m为multipliers的长度
     * - 空间复杂度：O(m^2)
     * 
     * 优化版本：
     * - 时间复杂度：O(m^2)
     * - 空间复杂度：O(m)
     * 
     * 记忆化搜索：
     * - 时间复杂度：O(m^2)
     * - 空间复杂度：O(m^2)
     * 
     * 工程化建议：
     * 1. 对于大规模数据，使用优化版本节省空间
     * 2. 记忆化搜索代码更简洁，但递归深度可能受限
     * 3. 注意处理负数情况，避免整数溢出
     */
}
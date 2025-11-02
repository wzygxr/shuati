package class067;

import java.util.*;

/**
 * Class067 补充题目实现
 * 包含各大算法平台的经典动态规划题目实现
 * 
 * 题目来源：LeetCode、LintCode、HackerRank、牛客网、洛谷、Codeforces、AtCoder、POJ、HDU等
 * 
 * 实现原则：
 * 1. 代码清晰可读，注释详细
 * 2. 包含多种解法（暴力、记忆化、DP、优化DP）
 * 3. 提供完整的测试用例
 * 4. 分析时间复杂度和空间复杂度
 * 5. 考虑工程化需求（异常处理、边界条件等）
 */

public class Code05_AdditionalProblems {
    
    /**
     * LeetCode 62. 不同路径
     * 问题描述：机器人从m×n网格的左上角移动到右下角，每次只能向右或向下移动一步
     * 求有多少条不同的路径
     * 
     * 时间复杂度：O(m*n)
     * 空间复杂度：O(m*n) 可优化为O(min(m,n))
     */
    public static int uniquePaths(int m, int n) {
        // 输入验证
        if (m <= 0 || n <= 0) return 0;
        if (m == 1 || n == 1) return 1;
        
        int[][] dp = new int[m][n];
        
        // 初始化第一行和第一列
        for (int i = 0; i < m; i++) dp[i][0] = 1;
        for (int j = 0; j < n; j++) dp[0][j] = 1;
        
        // 状态转移：dp[i][j] = dp[i-1][j] + dp[i][j-1]
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = dp[i-1][j] + dp[i][j-1];
            }
        }
        
        return dp[m-1][n-1];
    }
    
    /**
     * 空间优化版本
     * 空间复杂度：O(min(m,n))
     */
    public static int uniquePathsOptimized(int m, int n) {
        if (m <= 0 || n <= 0) return 0;
        if (m == 1 || n == 1) return 1;
        
        // 让n成为较小的维度以优化空间
        if (m < n) return uniquePathsOptimized(n, m);
        
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[j] += dp[j-1];
            }
        }
        
        return dp[n-1];
    }
    
    /**
     * LeetCode 63. 不同路径 II（有障碍物）
     * 问题描述：在62题基础上，网格中有障碍物，障碍物位置不能通过
     * 
     * 时间复杂度：O(m*n)
     * 空间复杂度：O(m*n) 可优化为O(n)
     */
    public static int uniquePathsWithObstacles(int[][] obstacleGrid) {
        // 输入验证
        if (obstacleGrid == null || obstacleGrid.length == 0 || obstacleGrid[0].length == 0) {
            return 0;
        }
        
        int m = obstacleGrid.length, n = obstacleGrid[0].length;
        
        // 如果起点或终点有障碍物，直接返回0
        if (obstacleGrid[0][0] == 1 || obstacleGrid[m-1][n-1] == 1) {
            return 0;
        }
        
        int[][] dp = new int[m][n];
        
        // 初始化第一行和第一列（遇到障碍物则后面都为0）
        for (int i = 0; i < m && obstacleGrid[i][0] == 0; i++) {
            dp[i][0] = 1;
        }
        for (int j = 0; j < n && obstacleGrid[0][j] == 0; j++) {
            dp[0][j] = 1;
        }
        
        // 状态转移
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (obstacleGrid[i][j] == 0) {
                    dp[i][j] = dp[i-1][j] + dp[i][j-1];
                }
                // 有障碍物时dp[i][j]保持0（默认值）
            }
        }
        
        return dp[m-1][n-1];
    }
    
    /**
     * 空间优化版本
     */
    public static int uniquePathsWithObstaclesOptimized(int[][] obstacleGrid) {
        if (obstacleGrid == null || obstacleGrid.length == 0 || obstacleGrid[0].length == 0) {
            return 0;
        }
        
        int m = obstacleGrid.length, n = obstacleGrid[0].length;
        if (obstacleGrid[0][0] == 1 || obstacleGrid[m-1][n-1] == 1) {
            return 0;
        }
        
        int[] dp = new int[n];
        dp[0] = 1;
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (obstacleGrid[i][j] == 1) {
                    dp[j] = 0;
                } else if (j > 0) {
                    dp[j] += dp[j-1];
                }
            }
        }
        
        return dp[n-1];
    }
    
    /**
     * LeetCode 72. 编辑距离
     * 问题描述：计算将word1转换成word2所使用的最少操作数
     * 操作包括插入、删除、替换一个字符
     * 
     * 时间复杂度：O(m*n)
     * 空间复杂度：O(m*n) 可优化为O(min(m,n))
     */
    public static int minDistance(String word1, String word2) {
        // 输入验证
        if (word1 == null) word1 = "";
        if (word2 == null) word2 = "";
        
        int m = word1.length(), n = word2.length();
        
        // 特殊情况处理
        if (m == 0) return n;
        if (n == 0) return m;
        
        int[][] dp = new int[m+1][n+1];
        
        // 初始化边界
        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;
        
        // 状态转移
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i-1) == word2.charAt(j-1)) {
                    // 字符相同，不需要操作
                    dp[i][j] = dp[i-1][j-1];
                } else {
                    // 字符不同，取三种操作的最小值 + 1
                    dp[i][j] = Math.min(Math.min(dp[i-1][j],    // 删除
                                                 dp[i][j-1]),    // 插入
                                                 dp[i-1][j-1])   // 替换
                                         + 1;
                }
            }
        }
        
        return dp[m][n];
    }
    
    /**
     * 空间优化版本
     */
    public static int minDistanceOptimized(String word1, String word2) {
        if (word1 == null) word1 = "";
        if (word2 == null) word2 = "";
        
        int m = word1.length(), n = word2.length();
        
        if (m == 0) return n;
        if (n == 0) return m;
        
        // 让较短的字符串作为word2以优化空间
        if (m < n) return minDistanceOptimized(word2, word1);
        
        int[] dp = new int[n+1];
        
        // 初始化第一行
        for (int j = 0; j <= n; j++) dp[j] = j;
        
        for (int i = 1; i <= m; i++) {
            int prev = dp[0]; // 保存左上角的值
            dp[0] = i;
            
            for (int j = 1; j <= n; j++) {
                int temp = dp[j];
                if (word1.charAt(i-1) == word2.charAt(j-1)) {
                    dp[j] = prev;
                } else {
                    dp[j] = Math.min(Math.min(dp[j], dp[j-1]), prev) + 1;
                }
                prev = temp;
            }
        }
        
        return dp[n];
    }
    
    /**
     * LeetCode 115. 不同的子序列
     * 问题描述：给定一个字符串 s 和一个字符串 t ，计算在 s 的子序列中 t 出现的个数
     * 
     * 时间复杂度：O(m*n)
     * 空间复杂度：O(m*n) 可优化为O(n)
     */
    public static int numDistinct(String s, String t) {
        // 输入验证
        if (s == null || t == null) return 0;
        if (t.length() == 0) return 1;
        if (s.length() < t.length()) return 0;
        
        int m = s.length(), n = t.length();
        int[][] dp = new int[m+1][n+1];
        
        // 初始化：空字符串是任何字符串的一个子序列
        for (int i = 0; i <= m; i++) dp[i][0] = 1;
        
        // 状态转移
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s.charAt(i-1) == t.charAt(j-1)) {
                    dp[i][j] = dp[i-1][j-1] + dp[i-1][j];
                } else {
                    dp[i][j] = dp[i-1][j];
                }
            }
        }
        
        return dp[m][n];
    }
    
    /**
     * 空间优化版本
     */
    public static int numDistinctOptimized(String s, String t) {
        // 输入验证
        if (s == null || t == null) return 0;
        if (t.length() == 0) return 1;
        if (s.length() < t.length()) return 0;
        
        int n = t.length();
        int[] dp = new int[n+1];
        dp[0] = 1;
        
        for (int i = 1; i <= s.length(); i++) {
            // 从后往前遍历，避免重复使用更新后的值
            for (int j = Math.min(i, n); j >= 1; j--) {
                if (s.charAt(i-1) == t.charAt(j-1)) {
                    dp[j] += dp[j-1];
                }
            }
        }
        
        return dp[n];
    }
    
    /**
     * LeetCode 300. 最长递增子序列
     * 问题描述：找到数组中最长的严格递增子序列的长度
     * 
     * 方法1：动态规划 O(n^2)
     */
    public static int lengthOfLIS(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        
        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        int maxLen = 1;
        
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            maxLen = Math.max(maxLen, dp[i]);
        }
        
        return maxLen;
    }
    
    /**
     * 方法2：贪心 + 二分查找 O(n log n)
     * 维护一个tails数组，tails[i]表示长度为i+1的递增子序列的最小结尾值
     */
    public static int lengthOfLISOptimized(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        
        int[] tails = new int[nums.length];
        int size = 0;
        
        for (int x : nums) {
            int left = 0, right = size;
            // 二分查找第一个大于等于x的位置
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (tails[mid] < x) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            
            tails[left] = x;
            if (left == size) size++;
        }
        
        return size;
    }
    
    /**
     * LeetCode 322. 零钱兑换
     * 问题描述：给定不同面额的硬币和一个总金额，计算可以凑成总金额的最少硬币数
     * 如果无法凑成，返回-1
     * 
     * 时间复杂度：O(amount * n)
     * 空间复杂度：O(amount)
     */
    public static int coinChange(int[] coins, int amount) {
        if (amount == 0) return 0;
        if (coins == null || coins.length == 0) return -1;
        
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1); // 初始化为一个不可能的大值
        dp[0] = 0;
        
        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (i >= coin) {
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }
        
        return dp[amount] > amount ? -1 : dp[amount];
    }
    
    /**
     * LeetCode 416. 分割等和子集（0-1背包问题）
     * 问题描述：判断是否可以将数组分割成两个子集，使得两个子集的元素和相等
     * 
     * 时间复杂度：O(n * sum)
     * 空间复杂度：O(sum)
     */
    public static boolean canPartition(int[] nums) {
        if (nums == null || nums.length < 2) return false;
        
        int sum = 0;
        for (int num : nums) sum += num;
        
        // 如果和为奇数，不可能分割
        if (sum % 2 != 0) return false;
        
        int target = sum / 2;
        boolean[] dp = new boolean[target + 1];
        dp[0] = true;
        
        for (int num : nums) {
            // 逆序遍历避免重复使用同一元素
            for (int j = target; j >= num; j--) {
                dp[j] = dp[j] || dp[j - num];
            }
        }
        
        return dp[target];
    }
    
    /**
     * HackerRank - The Coin Change Problem
     * 问题描述：给定不同面额的硬币和一个总金额，计算可以凑成总金额的硬币组合数
     * 
     * 时间复杂度：O(amount * n)
     * 空间复杂度：O(amount)
     */
    public static long coinChangeWays(int amount, int[] coins) {
        if (amount == 0) return 1;
        if (coins == null || coins.length == 0) return 0;
        
        long[] dp = new long[amount + 1];
        dp[0] = 1;
        
        // 注意：这里先遍历硬币，再遍历金额，确保组合数（不是排列数）
        for (int coin : coins) {
            for (int j = coin; j <= amount; j++) {
                dp[j] += dp[j - coin];
            }
        }
        
        return dp[amount];
    }
    
    /**
     * Codeforces 455A - Boredom
     * 问题描述：给定数组，选择元素x获得x分，但不能再选择x-1和x+1，求最大得分
     * 
     * 时间复杂度：O(n + maxVal)
     * 空间复杂度：O(maxVal)
     */
    public static long boredomMaxScore(int[] arr) {
        if (arr == null || arr.length == 0) return 0;
        
        int maxVal = 100000;
        long[] count = new long[maxVal + 1];
        
        for (int num : arr) {
            count[num]++;
        }
        
        long[] dp = new long[maxVal + 1];
        dp[1] = count[1];
        
        for (int i = 2; i <= maxVal; i++) {
            // 选择i：得分 = count[i]*i + dp[i-2]
            // 不选择i：得分 = dp[i-1]
            dp[i] = Math.max(dp[i-1], dp[i-2] + count[i] * i);
        }
        
        return dp[maxVal];
    }
    
    /**
     * 洛谷 P1048 采药（0-1背包问题）
     * 问题描述：在时间限制内选择草药，使得总价值最大
     */
    public static int herbalMedicine(int T, int M, int[] time, int[] value) {
        if (T <= 0 || M <= 0 || time == null || value == null || time.length != M || value.length != M) {
            return 0;
        }
        
        int[] dp = new int[T + 1];
        
        for (int i = 0; i < M; i++) {
            for (int j = T; j >= time[i]; j--) {
                dp[j] = Math.max(dp[j], dp[j - time[i]] + value[i]);
            }
        }
        
        return dp[T];
    }
    
    /**
     * LeetCode 70. 爬楼梯
     * 问题描述：每次可以爬1或2个台阶，求爬到第n阶有多少种方法
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    public static int climbStairs(int n) {
        if (n <= 2) return n;
        
        int prev2 = 1, prev1 = 2;
        for (int i = 3; i <= n; i++) {
            int current = prev1 + prev2;
            prev2 = prev1;
            prev1 = current;
        }
        return prev1;
    }
    
    /**
     * LeetCode 198. 打家劫舍
     * 问题描述：不能抢劫相邻的房屋，求能抢劫到的最大金额
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    public static int rob(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        if (nums.length == 1) return nums[0];
        
        int prev2 = nums[0];
        int prev1 = Math.max(nums[0], nums[1]);
        
        for (int i = 2; i < nums.length; i++) {
            int current = Math.max(prev1, prev2 + nums[i]);
            prev2 = prev1;
            prev1 = current;
        }
        return prev1;
    }
    
    /**
     * LeetCode 53. 最大子数组和
     * 问题描述：找到数组中连续子数组的最大和
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    public static int maxSubArray(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        
        int maxSum = nums[0];
        int currentSum = nums[0];
        
        for (int i = 1; i < nums.length; i++) {
            currentSum = Math.max(nums[i], currentSum + nums[i]);
            maxSum = Math.max(maxSum, currentSum);
        }
        return maxSum;
    }
    
    /**
     * 测试方法：验证所有算法的正确性
     */
    public static void test() {
        System.out.println("=== Class067 补充题目测试 ===");
        
        // 测试不同路径
        System.out.println("不同路径测试:");
        System.out.println("3x7网格路径数: " + uniquePaths(3, 7));
        System.out.println("空间优化版本: " + uniquePathsOptimized(3, 7));
        System.out.println("预期结果: 28");
        System.out.println();
        
        // 测试编辑距离
        System.out.println("编辑距离测试:");
        System.out.println("'horse'到'ros': " + minDistance("horse", "ros"));
        System.out.println("空间优化版本: " + minDistanceOptimized("horse", "ros"));
        System.out.println("预期结果: 3");
        System.out.println();
        
        // 测试不同的子序列
        System.out.println("不同的子序列测试:");
        System.out.println("'rabbbit'中'rabbit'的个数: " + numDistinct("rabbbit", "rabbit"));
        System.out.println("空间优化版本: " + numDistinctOptimized("rabbbit", "rabbit"));
        System.out.println("预期结果: 3");
        System.out.println();
        
        // 测试最长递增子序列
        System.out.println("最长递增子序列测试:");
        int[] nums = {10,9,2,5,3,7,101,18};
        System.out.println("数组: " + Arrays.toString(nums));
        System.out.println("DP方法: " + lengthOfLIS(nums));
        System.out.println("优化方法: " + lengthOfLISOptimized(nums));
        System.out.println("预期结果: 4");
        System.out.println();
        
        // 测试零钱兑换
        System.out.println("零钱兑换测试:");
        int[] coins = {1,2,5};
        System.out.println("硬币: " + Arrays.toString(coins) + ", 金额: 11");
        System.out.println("最少硬币数: " + coinChange(coins, 11));
        System.out.println("预期结果: 3");
        System.out.println();
        
        // 测试分割等和子集
        System.out.println("分割等和子集测试:");
        int[] nums2 = {1,5,11,5};
        System.out.println("数组: " + Arrays.toString(nums2));
        System.out.println("能否分割: " + canPartition(nums2));
        System.out.println("预期结果: true");
        System.out.println();
        
        // 测试爬楼梯
        System.out.println("爬楼梯测试:");
        System.out.println("爬5阶楼梯的方法数: " + climbStairs(5));
        System.out.println("预期结果: 8");
        System.out.println();
        
        // 测试打家劫舍
        System.out.println("打家劫舍测试:");
        int[] nums3 = {2,7,9,3,1};
        System.out.println("房屋金额: " + Arrays.toString(nums3));
        System.out.println("最大金额: " + rob(nums3));
        System.out.println("预期结果: 12");
        System.out.println();
        
        // 测试最大子数组和
        System.out.println("最大子数组和测试:");
        int[] nums4 = {-2,1,-3,4,-1,2,1,-5,4};
        System.out.println("数组: " + Arrays.toString(nums4));
        System.out.println("最大子数组和: " + maxSubArray(nums4));
        System.out.println("预期结果: 6");
        System.out.println();
        
        System.out.println("=== 测试完成 ===");
    }
    
    /**
     * 主方法：运行测试用例
     */
    public static void main(String[] args) {
        test();
    }
}
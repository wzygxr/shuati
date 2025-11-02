package class073;

// LeetCode 474. 一和零
// 题目描述：给你一个二进制字符串数组 strs 和两个整数 m 和 n 。请你找出并返回 strs 的最大子集的长度，
// 该子集中最多有 m 个 0 和 n 个 1 。如果所有字符串都不满足条件，返回 0 。
// 链接：https://leetcode.cn/problems/ones-and-zeroes/
// 
// 解题思路：
// 这是一个二维费用的0-1背包问题：
// - 每个字符串可以看作是一个物品
// - 物品的重量有两个维度：0的个数和1的个数
// - 背包的容量也有两个维度：m（最多m个0）和n（最多n个1）
// - 我们需要找出最多能放多少个物品，使得两个维度的重量都不超过背包容量
// 
// 状态定义：dp[i][j] 表示最多使用i个0和j个1时，可以组成的最大子集的长度
// 状态转移方程：对于每个字符串，计算其包含的0的个数zeros和1的个数ones
// dp[i][j] = max(dp[i][j], dp[i-zeros][j-ones] + 1)（当i >= zeros且j >= ones时）
// 初始状态：dp[0][0] = 0，表示不使用任何字符时，子集长度为0
// 
// 时间复杂度：O(l * m * n)，其中l是字符串数组的长度，m和n是给定的两个整数
// 空间复杂度：O(m * n)，使用二维DP数组

public class Code30_OnesAndZeroes {

    // 主方法，用于测试
    public static void main(String[] args) {
        // 测试用例1
        String[] strs1 = {"10", "0001", "111001", "1", "0"};
        int m1 = 5, n1 = 3;
        System.out.println("测试用例1结果: " + findMaxForm(strs1, m1, n1)); // 预期输出: 4
        
        // 测试用例2
        String[] strs2 = {"10", "0", "1"};
        int m2 = 1, n2 = 1;
        System.out.println("测试用例2结果: " + findMaxForm(strs2, m2, n2)); // 预期输出: 2
        
        // 测试用例3
        String[] strs3 = {"10", "0001", "111001", "1", "0"};
        int m3 = 4, n3 = 3;
        System.out.println("测试用例3结果: " + findMaxForm(strs3, m3, n3)); // 预期输出: 3
    }
    
    /**
     * 找出strs的最大子集长度，该子集中最多有m个0和n个1
     * @param strs 二进制字符串数组
     * @param m 最多允许的0的个数
     * @param n 最多允许的1的个数
     * @return 满足条件的最大子集长度
     */
    public static int findMaxForm(String[] strs, int m, int n) {
        // 参数验证
        if (strs == null || strs.length == 0) {
            return 0;
        }
        
        // 创建二维DP数组，dp[i][j]表示最多使用i个0和j个1时，可以组成的最大子集的长度
        int[][] dp = new int[m + 1][n + 1];
        
        // 对于每个字符串，计算其包含的0的个数和1的个数
        for (String str : strs) {
            int zeros = 0, ones = 0;
            for (char c : str.toCharArray()) {
                if (c == '0') {
                    zeros++;
                } else {
                    ones++;
                }
            }
            
            // 二维0-1背包问题，需要逆序遍历两个维度
            for (int i = m; i >= zeros; i--) {
                for (int j = n; j >= ones; j--) {
                    // 状态转移：选择当前字符串或不选择当前字符串，取最大值
                    dp[i][j] = Math.max(dp[i][j], dp[i - zeros][j - ones] + 1);
                }
            }
        }
        
        // 返回最多使用m个0和n个1时，可以组成的最大子集的长度
        return dp[m][n];
    }
    
    /**
     * 优化版本：预处理每个字符串的0和1的个数
     */
    public static int findMaxFormOptimized(String[] strs, int m, int n) {
        if (strs == null || strs.length == 0) {
            return 0;
        }
        
        int l = strs.length;
        // 预处理每个字符串的0和1的个数
        int[][] counts = new int[l][2]; // counts[i][0]表示第i个字符串中0的个数，counts[i][1]表示1的个数
        
        for (int i = 0; i < l; i++) {
            int zeros = 0, ones = 0;
            for (char c : strs[i].toCharArray()) {
                if (c == '0') {
                    zeros++;
                } else {
                    ones++;
                }
            }
            counts[i][0] = zeros;
            counts[i][1] = ones;
        }
        
        int[][] dp = new int[m + 1][n + 1];
        
        // 遍历每个字符串
        for (int[] count : counts) {
            int zeros = count[0];
            int ones = count[1];
            
            // 逆序遍历两个维度
            for (int i = m; i >= zeros; i--) {
                for (int j = n; j >= ones; j--) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - zeros][j - ones] + 1);
                }
            }
        }
        
        return dp[m][n];
    }
    
    /**
     * 三维DP数组实现（更直观但空间复杂度更高）
     * dp[k][i][j]表示考虑前k个字符串，最多使用i个0和j个1时，可以组成的最大子集的长度
     */
    public static int findMaxForm3D(String[] strs, int m, int n) {
        if (strs == null || strs.length == 0) {
            return 0;
        }
        
        int l = strs.length;
        // 预处理每个字符串的0和1的个数
        int[][] counts = new int[l][2];
        
        for (int i = 0; i < l; i++) {
            int zeros = 0, ones = 0;
            for (char c : strs[i].toCharArray()) {
                if (c == '0') {
                    zeros++;
                } else {
                    ones++;
                }
            }
            counts[i][0] = zeros;
            counts[i][1] = ones;
        }
        
        // 创建三维DP数组
        int[][][] dp = new int[l + 1][m + 1][n + 1];
        
        // 填充DP数组
        for (int k = 1; k <= l; k++) {
            int zeros = counts[k - 1][0];
            int ones = counts[k - 1][1];
            
            for (int i = 0; i <= m; i++) {
                for (int j = 0; j <= n; j++) {
                    // 不选择第k个字符串
                    dp[k][i][j] = dp[k - 1][i][j];
                    
                    // 选择第k个字符串（如果可以的话）
                    if (i >= zeros && j >= ones) {
                        dp[k][i][j] = Math.max(dp[k][i][j], dp[k - 1][i - zeros][j - ones] + 1);
                    }
                }
            }
        }
        
        return dp[l][m][n];
    }
    
    /**
     * 提前剪枝优化
     */
    public static int findMaxFormPruned(String[] strs, int m, int n) {
        if (strs == null || strs.length == 0) {
            return 0;
        }
        
        int l = strs.length;
        // 预处理每个字符串的0和1的个数，并过滤掉不可能被选中的字符串
        int[][] counts = new int[l][2];
        int validCount = 0;
        
        for (int i = 0; i < l; i++) {
            int zeros = 0, ones = 0;
            for (char c : strs[i].toCharArray()) {
                if (c == '0') {
                    zeros++;
                } else {
                    ones++;
                }
            }
            
            // 剪枝：如果字符串的0或1的个数超过给定的限制，则无法选择该字符串
            if (zeros <= m && ones <= n) {
                counts[validCount][0] = zeros;
                counts[validCount][1] = ones;
                validCount++;
            }
        }
        
        int[][] dp = new int[m + 1][n + 1];
        
        // 遍历有效的字符串
        for (int i = 0; i < validCount; i++) {
            int zeros = counts[i][0];
            int ones = counts[i][1];
            
            // 逆序遍历两个维度
            for (int j = m; j >= zeros; j--) {
                for (int k = n; k >= ones; k--) {
                    dp[j][k] = Math.max(dp[j][k], dp[j - zeros][k - ones] + 1);
                }
            }
        }
        
        return dp[m][n];
    }
    
    /**
     * 使用滚动数组优化空间复杂度
     * 注意：在这个问题中，滚动数组的优化已经在基本实现中完成（使用二维数组）
     * 这个方法只是为了展示如何进一步优化（尽管在这个问题中意义不大）
     */
    public static int findMaxFormScrolling(String[] strs, int m, int n) {
        if (strs == null || strs.length == 0) {
            return 0;
        }
        
        // 创建二维DP数组
        int[][] dp = new int[m + 1][n + 1];
        
        for (String str : strs) {
            int zeros = 0, ones = 0;
            for (char c : str.toCharArray()) {
                if (c == '0') {
                    zeros++;
                } else {
                    ones++;
                }
            }
            
            // 逆序遍历两个维度（这已经是滚动数组的思想）
            for (int i = m; i >= zeros; i--) {
                for (int j = n; j >= ones; j--) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - zeros][j - ones] + 1);
                }
            }
        }
        
        return dp[m][n];
    }
    
    /**
     * 递归+记忆化搜索实现
     * 注意：由于这个问题的参数范围较大，递归+记忆化搜索可能会超时
     * 这里仅作为一种实现方式展示
     */
    public static int findMaxFormDFS(String[] strs, int m, int n) {
        if (strs == null || strs.length == 0) {
            return 0;
        }
        
        int l = strs.length;
        // 预处理每个字符串的0和1的个数
        int[][] counts = new int[l][2];
        
        for (int i = 0; i < l; i++) {
            int zeros = 0, ones = 0;
            for (char c : strs[i].toCharArray()) {
                if (c == '0') {
                    zeros++;
                } else {
                    ones++;
                }
            }
            counts[i][0] = zeros;
            counts[i][1] = ones;
        }
        
        // 创建三维缓存数组，memo[k][i][j]表示考虑前k个字符串，最多使用i个0和j个1时，可以组成的最大子集的长度
        Integer[][][] memo = new Integer[l][m + 1][n + 1];
        
        // 调用递归辅助函数
        return dfs(counts, l - 1, m, n, memo);
    }
    
    /**
     * 递归辅助函数
     * @param counts 每个字符串的0和1的个数
     * @param index 当前考虑的字符串索引
     * @param m0 剩余可用的0的个数
     * @param n1 剩余可用的1的个数
     * @param memo 缓存数组
     * @return 满足条件的最大子集长度
     */
    private static int dfs(int[][] counts, int index, int m0, int n1, Integer[][][] memo) {
        // 基础情况：如果已经考虑完所有字符串，返回0
        if (index < 0) {
            return 0;
        }
        
        // 检查缓存
        if (memo[index][m0][n1] != null) {
            return memo[index][m0][n1];
        }
        
        // 不选择当前字符串
        int notChoose = dfs(counts, index - 1, m0, n1, memo);
        
        // 选择当前字符串（如果可以的话）
        int choose = 0;
        int zeros = counts[index][0];
        int ones = counts[index][1];
        
        if (zeros <= m0 && ones <= n1) {
            choose = 1 + dfs(counts, index - 1, m0 - zeros, n1 - ones, memo);
        }
        
        // 缓存结果
        memo[index][m0][n1] = Math.max(notChoose, choose);
        return memo[index][m0][n1];
    }
}
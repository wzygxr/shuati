package class073;

// LeetCode 474. 一和零
// 题目描述：给你一个二进制字符串数组 strs 和两个整数 m 和 n 。
// 请你找出并返回 strs 的最大子集的长度，该子集中 最多 有 m 个 0 和 n 个 1 。
// 如果 x 的所有元素也是 y 的元素，集合 x 是集合 y 的 子集 。
// 链接：https://leetcode.cn/problems/ones-and-zeroes/
// 
// 解题思路：
// 这是一个二维费用的0-1背包问题。每个字符串可以看作是一个物品，它有两个费用：0的数量和1的数量。
// 我们的背包有两个容量限制：最多可以使用m个0和n个1。我们的目标是选择尽可能多的物品（字符串）。
// 
// 状态定义：dp[i][j] 表示使用i个0和j个1时，最多可以选择的字符串数量
// 状态转移方程：dp[i][j] = max(dp[i][j], dp[i - zeros][j - ones] + 1)，其中zeros和ones是当前字符串的0和1的数量
// 初始状态：dp[0][0] = 0，其他初始化为0
// 
// 时间复杂度：O(l * m * n)，其中l是字符串数组的长度
// 空间复杂度：O(m * n)，使用二维DP数组

public class Code36_OnesAndZeroes {

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
    }
    
    /**
     * 计算最大子集的长度
     * @param strs 二进制字符串数组
     * @param m 最多可以使用的0的数量
     * @param n 最多可以使用的1的数量
     * @return 最大子集的长度
     */
    public static int findMaxForm(String[] strs, int m, int n) {
        if (strs == null || strs.length == 0) {
            return 0;
        }
        
        // 创建二维DP数组，dp[i][j]表示使用i个0和j个1时，最多可以选择的字符串数量
        int[][] dp = new int[m + 1][n + 1];
        
        // 遍历每个字符串
        for (String str : strs) {
            // 统计当前字符串中0和1的数量
            int[] counts = countZeroesOnes(str);
            int zeros = counts[0];
            int ones = counts[1];
            
            // 逆序遍历，避免重复使用同一个字符串
            for (int i = m; i >= zeros; i--) {
                for (int j = n; j >= ones; j--) {
                    // 更新状态：不选当前字符串 或 选当前字符串（如果可以的话）
                    dp[i][j] = Math.max(dp[i][j], dp[i - zeros][j - ones] + 1);
                }
            }
        }
        
        return dp[m][n];
    }
    
    /**
     * 统计字符串中0和1的数量
     * @param s 二进制字符串
     * @return 一个数组，第一个元素是0的数量，第二个元素是1的数量
     */
    private static int[] countZeroesOnes(String s) {
        int[] counts = new int[2];
        for (char c : s.toCharArray()) {
            counts[c - '0']++;
        }
        return counts;
    }
    
    /**
     * 使用三维DP数组的版本（更直观但空间效率较低）
     * @param strs 二进制字符串数组
     * @param m 最多可以使用的0的数量
     * @param n 最多可以使用的1的数量
     * @return 最大子集的长度
     */
    public static int findMaxForm3D(String[] strs, int m, int n) {
        if (strs == null || strs.length == 0) {
            return 0;
        }
        
        int l = strs.length;
        // 创建三维DP数组，dp[k][i][j]表示前k个字符串中，使用i个0和j个1时，最多可以选择的字符串数量
        int[][][] dp = new int[l + 1][m + 1][n + 1];
        
        // 遍历每个字符串
        for (int k = 1; k <= l; k++) {
            String str = strs[k - 1];
            int[] counts = countZeroesOnes(str);
            int zeros = counts[0];
            int ones = counts[1];
            
            // 遍历0的数量
            for (int i = 0; i <= m; i++) {
                // 遍历1的数量
                for (int j = 0; j <= n; j++) {
                    // 不选第k个字符串
                    dp[k][i][j] = dp[k - 1][i][j];
                    
                    // 选第k个字符串（如果可以的话）
                    if (i >= zeros && j >= ones) {
                        dp[k][i][j] = Math.max(dp[k][i][j], dp[k - 1][i - zeros][j - ones] + 1);
                    }
                }
            }
        }
        
        return dp[l][m][n];
    }
    
    /**
     * 优化的二维DP版本，将统计0和1的过程提前
     * @param strs 二进制字符串数组
     * @param m 最多可以使用的0的数量
     * @param n 最多可以使用的1的数量
     * @return 最大子集的长度
     */
    public static int findMaxFormOptimized(String[] strs, int m, int n) {
        if (strs == null || strs.length == 0) {
            return 0;
        }
        
        // 预先统计所有字符串中0和1的数量
        int[][] counts = new int[strs.length][2];
        for (int i = 0; i < strs.length; i++) {
            counts[i] = countZeroesOnes(strs[i]);
        }
        
        // 创建二维DP数组
        int[][] dp = new int[m + 1][n + 1];
        
        // 遍历每个字符串
        for (int[] count : counts) {
            int zeros = count[0];
            int ones = count[1];
            
            // 逆序遍历
            for (int i = m; i >= zeros; i--) {
                for (int j = n; j >= ones; j--) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - zeros][j - ones] + 1);
                }
            }
        }
        
        return dp[m][n];
    }
    
    /**
     * 使用递归+记忆化搜索实现
     * 这个方法对于较大的输入可能会超时，但展示了递归的思路
     * @param strs 二进制字符串数组
     * @param m 最多可以使用的0的数量
     * @param n 最多可以使用的1的数量
     * @return 最大子集的长度
     */
    public static int findMaxFormRecursive(String[] strs, int m, int n) {
        if (strs == null || strs.length == 0) {
            return 0;
        }
        
        // 预先统计所有字符串中0和1的数量
        int[][] counts = new int[strs.length][2];
        for (int i = 0; i < strs.length; i++) {
            counts[i] = countZeroesOnes(strs[i]);
        }
        
        // 创建三维记忆化缓存，memo[index][zeros][ones]表示从index开始的字符串中，剩余zeros和ones时能选的最大字符串数量
        Integer[][][] memo = new Integer[strs.length][m + 1][n + 1];
        
        return dfs(counts, 0, m, n, memo);
    }
    
    /**
     * 递归辅助函数
     * @param counts 每个字符串中0和1的数量
     * @param index 当前处理的字符串索引
     * @param zeros 剩余可用的0的数量
     * @param ones 剩余可用的1的数量
     * @param memo 记忆化缓存
     * @return 最大可以选择的字符串数量
     */
    private static int dfs(int[][] counts, int index, int zeros, int ones, Integer[][][] memo) {
        // 基础情况：处理完所有字符串
        if (index == counts.length) {
            return 0;
        }
        
        // 检查缓存
        if (memo[index][zeros][ones] != null) {
            return memo[index][zeros][ones];
        }
        
        // 不选当前字符串
        int notTake = dfs(counts, index + 1, zeros, ones, memo);
        
        // 选当前字符串（如果可以的话）
        int take = 0;
        int currentZeros = counts[index][0];
        int currentOnes = counts[index][1];
        if (zeros >= currentZeros && ones >= currentOnes) {
            take = 1 + dfs(counts, index + 1, zeros - currentZeros, ones - currentOnes, memo);
        }
        
        // 缓存结果
        memo[index][zeros][ones] = Math.max(notTake, take);
        return memo[index][zeros][ones];
    }
}
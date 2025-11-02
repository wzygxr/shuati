package class086;

// LeetCode 1143. 最长公共子序列
// 给定两个字符串 text1 和 text2，返回这两个字符串的最长公共子序列的长度。
// 如果不存在公共子序列，返回0。
// 测试链接 : https://leetcode.cn/problems/longest-common-subsequence/

import java.util.Arrays;

public class LeetCode1143_LCS_Length {
    
    /*
     * 算法详解：最长公共子序列长度（LeetCode 1143）
     * 
     * 问题描述：
     * 给定两个字符串 text1 和 text2，返回这两个字符串的最长公共子序列的长度。
     * 子序列是指在不改变字符相对顺序的前提下，删除某些字符后得到的新序列。
     * 
     * 算法思路：
     * 使用动态规划方法解决。
     * 1. 定义状态：dp[i][j]表示text1[0..i-1]和text2[0..j-1]的最长公共子序列长度
     * 2. 状态转移方程：
     *    - 如果text1[i-1] == text2[j-1]，则dp[i][j] = dp[i-1][j-1] + 1
     *    - 否则dp[i][j] = max(dp[i-1][j], dp[i][j-1])
     * 
     * 时间复杂度分析：
     * 1. 填充dp表：需要遍历两个字符串的所有字符组合，时间复杂度为O(m*n)
     * 2. 总体时间复杂度：O(m*n)
     * 
     * 空间复杂度分析：
     * 1. dp数组：需要存储m*n个状态值，空间复杂度为O(m*n)
     * 2. 总体空间复杂度：O(m*n)
     * 
     * 工程化考量：
     * 1. 异常处理：检查输入是否为空
     * 2. 空间优化：可以使用滚动数组将空间复杂度从O(m*n)优化到O(min(m,n))
     * 3. 边界处理：正确处理空字符串的情况
     * 
     * 极端场景验证：
     * 1. 输入字符串长度达到边界情况
     * 2. 两个字符串完全相同的情况
     * 3. 两个字符串完全不同的情况
     * 4. 一个字符串为空的情况
     * 5. 两个字符串都为空的情况
     */
    
    public static int longestCommonSubsequence(String text1, String text2) {
        // 异常处理：检查输入是否为空
        if (text1 == null || text2 == null || text1.length() == 0 || text2.length() == 0) {
            return 0;
        }
        
        int m = text1.length();
        int n = text2.length();
        
        // dp[i][j] 表示 text1[0..i-1] 和 text2[0..j-1] 的最长公共子序列长度
        int[][] dp = new int[m + 1][n + 1];
        
        // 初始化边界条件：空字符串与任何字符串的LCS长度为0
        // dp[0][j] = 0 (0 <= j <= n)
        // dp[i][0] = 0 (0 <= i <= m)
        // Java中int数组默认初始化为0，所以无需显式初始化
        
        // 填充dp表
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                // 状态转移方程的核心逻辑
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    // 如果当前字符相等，则LCS长度为前缀LCS长度加1
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    // 如果当前字符不相等，则取两种情况的最大值
                    // 1. 不包含text1[i-1]的LCS长度：dp[i-1][j]
                    // 2. 不包含text2[j-1]的LCS长度：dp[i][j-1]
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        // 返回最终结果
        return dp[m][n];
    }
    
    // 空间优化版本：使用滚动数组将空间复杂度优化到O(min(m,n))
    public static int longestCommonSubsequenceOptimized(String text1, String text2) {
        // 异常处理：检查输入是否为空
        if (text1 == null || text2 == null || text1.length() == 0 || text2.length() == 0) {
            return 0;
        }
        
        int m = text1.length();
        int n = text2.length();
        
        // 空间优化：确保text1是较短的字符串，减少空间使用
        if (m > n) {
            return longestCommonSubsequenceOptimized(text2, text1);
        }
        
        // 只使用两行数组来存储状态
        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];
        
        // 填充dp表
        for (int j = 1; j <= n; j++) {
            for (int i = 1; i <= m; i++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    curr[i] = prev[i - 1] + 1;
                } else {
                    curr[i] = Math.max(prev[i], curr[i - 1]);
                }
            }
            // 交换prev和curr数组
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }
        
        return prev[m];
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        String text1 = "abcde";
        String text2 = "ace";
        System.out.println("Test 1: " + longestCommonSubsequence(text1, text2)); // 期望输出: 3
        
        // 测试用例2
        text1 = "abc";
        text2 = "abc";
        System.out.println("Test 2: " + longestCommonSubsequence(text1, text2)); // 期望输出: 3
        
        // 测试用例3
        text1 = "abc";
        text2 = "def";
        System.out.println("Test 3: " + longestCommonSubsequence(text1, text2)); // 期望输出: 0
        
        // 测试用例4
        text1 = "";
        text2 = "abc";
        System.out.println("Test 4: " + longestCommonSubsequence(text1, text2)); // 期望输出: 0
        
        // 测试用例5
        text1 = "bl";
        text2 = "yby";
        System.out.println("Test 5: " + longestCommonSubsequence(text1, text2)); // 期望输出: 1
    }
}
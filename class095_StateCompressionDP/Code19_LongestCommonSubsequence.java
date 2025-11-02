package class080;

// 最长公共子序列 (Longest Common Subsequence)
// 给定两个字符串 text1 和 text2，返回这两个字符串的最长公共子序列的长度。
// 一个字符串的子序列是指这样一个新的字符串：它是由原字符串在不改变字符的相对顺序的情况下
// 删除某些字符（也可以不删除任何字符）后组成的新字符串。
// 测试链接 : https://leetcode.cn/problems/longest-common-subsequence/
public class Code19_LongestCommonSubsequence {

    // 使用动态规划解决最长公共子序列问题
    // 核心思想：通过二维DP表计算两个字符串的最长公共子序列长度
    // 时间复杂度: O(n * m)
    // 空间复杂度: O(n * m)
    public static int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length();
        int n = text2.length();
        
        // dp[i][j] 表示text1的前i个字符和text2的前j个字符的最长公共子序列长度
        int[][] dp = new int[m + 1][n + 1];
        
        // 状态转移：枚举两个字符串的每个位置
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                // 如果当前字符相同
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    // 最长公共子序列长度加1
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    // 否则取两种情况的最大值
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        // 返回最长公共子序列长度
        return dp[m][n];
    }

}

package class080;

// 最短公共超序列 (Shortest Common Supersequence)
// 给出两个字符串 str1 和 str2，返回同时以 str1 和 str2 作为子序列的最短字符串。
// 如果答案不止一个，则可以返回满足条件的任意一个答案。
// 测试链接 : https://leetcode.cn/problems/shortest-common-supersequence/

public class Code21_ShortestCommonSupersequence {
    
    // 使用动态规划解决最短公共超序列问题
    // 核心思想：先求最长公共子序列，然后根据LCS构造最短公共超序列
    // 时间复杂度: O(m * n)
    // 空间复杂度: O(m * n)
    public String shortestCommonSupersequence(String str1, String str2) {
        int m = str1.length();
        int n = str2.length();
        
        // dp[i][j] 表示str1前i个字符和str2前j个字符的最长公共子序列长度
        int[][] dp = new int[m + 1][n + 1];
        
        // 计算最长公共子序列长度
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        // 根据dp数组构造最短公共超序列
        StringBuilder result = new StringBuilder();
        int i = m, j = n;
        
        // 从后往前构造结果
        while (i > 0 || j > 0) {
            // 如果其中一个字符串已经处理完，添加另一个字符串的剩余字符
            if (i == 0) {
                result.append(str2.charAt(j - 1));
                j--;
            } else if (j == 0) {
                result.append(str1.charAt(i - 1));
                i--;
            }
            // 如果当前字符相同，添加该字符并同时移动两个指针
            else if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                result.append(str1.charAt(i - 1));
                i--;
                j--;
            }
            // 如果当前字符不同，根据dp值决定移动哪个指针
            else if (dp[i - 1][j] > dp[i][j - 1]) {
                result.append(str1.charAt(i - 1));
                i--;
            } else {
                result.append(str2.charAt(j - 1));
                j--;
            }
        }
        
        // 反转结果并返回
        return result.reverse().toString();
    }
    
    // 测试方法
    public static void main(String[] args) {
        Code21_ShortestCommonSupersequence solution = new Code21_ShortestCommonSupersequence();
        
        // 测试用例1
        String str1_1 = "abac";
        String str2_1 = "cab";
        String result1 = solution.shortestCommonSupersequence(str1_1, str2_1);
        System.out.println("测试用例1: str1=\"" + str1_1 + "\", str2=\"" + str2_1 + "\", 结果=\"" + result1 + "\"");
        
        // 测试用例2
        String str1_2 = "aaaaaaaa";
        String str2_2 = "aaaaaaaa";
        String result2 = solution.shortestCommonSupersequence(str1_2, str2_2);
        System.out.println("测试用例2: str1=\"" + str1_2 + "\", str2=\"" + str2_2 + "\", 结果=\"" + result2 + "\"");
    }
}
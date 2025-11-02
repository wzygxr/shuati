// 最长回文子序列 (Longest Palindromic Subsequence)
// 给你一个字符串 s ，找出其中最长的回文子序列，并返回该序列的长度。
// 子序列定义为：不改变剩余字符顺序的情况下，删除某些字符或者不删除任何字符形成的一个序列。
// 测试链接 : https://leetcode.cn/problems/longest-palindromic-subsequence/
public class Code12_LongestPalindromicSubsequence {

    // 方法1：暴力递归解法
    // 时间复杂度：O(2^n) - 指数级时间复杂度，效率极低
    // 空间复杂度：O(n) - 递归调用栈的深度
    // 问题：存在大量重复计算，效率低下
    public static int longestPalindromeSubseq1(String s) {
        return f1(s.toCharArray(), 0, s.length() - 1);
    }

    // str[i..j] 范围上的最长回文子序列长度
    public static int f1(char[] str, int i, int j) {
        // base case
        if (i > j) {
            return 0;
        }
        if (i == j) {
            return 1;
        }
        if (str[i] == str[j]) {
            // 首尾字符相同，都选
            return f1(str, i + 1, j - 1) + 2;
        } else {
            // 首尾字符不同，选择其中一个
            int case1 = f1(str, i + 1, j); // 不选i位置字符
            int case2 = f1(str, i, j - 1); // 不选j位置字符
            return Math.max(case1, case2);
        }
    }

    // 方法2：记忆化搜索（自顶向下动态规划）
    // 时间复杂度：O(n^2) - 每个状态只计算一次
    // 空间复杂度：O(n^2) - dp数组和递归调用栈
    // 优化：通过缓存已经计算的结果避免重复计算
    public static int longestPalindromeSubseq2(String s) {
        int n = s.length();
        int[][] dp = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dp[i][j] = -1;
            }
        }
        return f2(s.toCharArray(), 0, n - 1, dp);
    }

    // str[i..j] 范围上的最长回文子序列长度
    public static int f2(char[] str, int i, int j, int[][] dp) {
        if (i > j) {
            return 0;
        }
        if (i == j) {
            return 1;
        }
        if (dp[i][j] != -1) {
            return dp[i][j];
        }
        int ans;
        if (str[i] == str[j]) {
            // 首尾字符相同，都选
            ans = f2(str, i + 1, j - 1, dp) + 2;
        } else {
            // 首尾字符不同，选择其中一个
            int case1 = f2(str, i + 1, j, dp); // 不选i位置字符
            int case2 = f2(str, i, j - 1, dp); // 不选j位置字符
            ans = Math.max(case1, case2);
        }
        dp[i][j] = ans;
        return ans;
    }

    // 方法3：动态规划（自底向上）
    // 时间复杂度：O(n^2) - 需要填满整个dp表
    // 空间复杂度：O(n^2) - dp数组存储所有状态
    // 优化：避免了递归调用的开销
    public static int longestPalindromeSubseq3(String s) {
        int n = s.length();
        char[] str = s.toCharArray();
        // dp[i][j] 表示 str[i..j] 范围上的最长回文子序列长度
        int[][] dp = new int[n][n];
        
        // 初始化对角线
        for (int i = 0; i < n; i++) {
            dp[i][i] = 1;
        }
        
        // 填表过程，按区间长度从小到大填
        for (int l = 2; l <= n; l++) { // 区间长度
            for (int i = 0; i <= n - l; i++) { // 左端点
                int j = i + l - 1; // 右端点
                if (str[i] == str[j]) {
                    // 首尾字符相同，都选
                    dp[i][j] = dp[i + 1][j - 1] + 2;
                } else {
                    // 首尾字符不同，选择其中一个
                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[0][n - 1];
    }

    // 方法4：空间优化的动态规划
    // 时间复杂度：O(n^2) - 仍然需要计算所有状态
    // 空间复杂度：O(n) - 只保存必要的状态值
    // 优化：只保存必要的状态，大幅减少空间使用
    public static int longestPalindromeSubseq4(String s) {
        int n = s.length();
        char[] str = s.toCharArray();
        
        // 使用两个一维数组来代替二维数组
        int[] dp = new int[n];
        int[] pre = new int[n];
        
        // 初始化对角线
        for (int i = 0; i < n; i++) {
            pre[i] = 1;
        }
        
        // 填表过程，按区间长度从小到大填
        for (int l = 2; l <= n; l++) { // 区间长度
            for (int i = 0; i <= n - l; i++) { // 左端点
                int j = i + l - 1; // 右端点
                if (str[i] == str[j]) {
                    // 首尾字符相同，都选
                    dp[i] = pre[i + 1] + 2;
                } else {
                    // 首尾字符不同，选择其中一个
                    dp[i] = Math.max(pre[i], dp[i + 1]);
                }
            }
            // 交换dp和pre数组
            int[] temp = pre;
            pre = dp;
            dp = temp;
        }
        return pre[0];
    }

    // 测试用例和性能对比
    public static void main(String[] args) {
        System.out.println("测试最长回文子序列实现：");
        
        // 测试用例1
        String s1 = "bbbab";
        System.out.println("s = \"" + s1 + "\"");
        System.out.println("方法3 (动态规划): " + longestPalindromeSubseq3(s1));
        System.out.println("方法4 (空间优化): " + longestPalindromeSubseq4(s1));
        
        // 测试用例2
        String s2 = "cbbd";
        System.out.println("\ns = \"" + s2 + "\"");
        System.out.println("方法3 (动态规划): " + longestPalindromeSubseq3(s2));
        System.out.println("方法4 (空间优化): " + longestPalindromeSubseq4(s2));
        
        // 测试用例3
        String s3 = "a";
        System.out.println("\ns = \"" + s3 + "\"");
        System.out.println("方法3 (动态规划): " + longestPalindromeSubseq3(s3));
        System.out.println("方法4 (空间优化): " + longestPalindromeSubseq4(s3));
    }
}
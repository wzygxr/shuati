package class086;

// LeetCode 583. 两个字符串的删除操作
// 给定两个单词 word1 和 word2 ，返回使得 word1 和 word2 相同所需的最小步数。
// 每步 可以删除任意一个字符串中的一个字符。
// 测试链接 : https://leetcode.cn/problems/delete-operation-for-two-strings/

public class LeetCode583_Delete_Operation_For_Two_Strings {
    
    /*
     * 算法详解：两个字符串的删除操作（LeetCode 583）
     * 
     * 问题描述：
     * 给定两个单词 word1 和 word2 ，返回使得 word1 和 word2 相同所需的最小步数。
     * 每步可以删除任意一个字符串中的一个字符。
     * 
     * 算法思路：
     * 这个问题可以转换为LCS问题。要使两个字符串相同，我们需要保留它们的最长公共子序列，
     * 然后删除其他所有字符。
     * 1. 计算word1和word2的最长公共子序列长度
     * 2. 删除word1中不在LCS中的字符：需要word1.length() - lcs长度步
     * 3. 删除word2中不在LCS中的字符：需要word2.length() - lcs长度步
     * 4. 总步数 = (word1.length() - lcs) + (word2.length() - lcs) = word1.length() + word2.length() - 2*lcs
     * 
     * 时间复杂度分析：
     * 1. 计算LCS：O(m*n)
     * 2. 总体时间复杂度：O(m*n)
     * 
     * 空间复杂度分析：
     * 1. dp数组：O(m*n)
     * 2. 总体空间复杂度：O(m*n)
     * 
     * 工程化考量：
     * 1. 异常处理：检查输入是否为空
     * 2. 边界处理：正确处理空字符串的情况
     * 3. 空间优化：可以使用滚动数组将空间复杂度优化到O(min(m,n))
     * 
     * 极端场景验证：
     * 1. 输入字符串长度达到边界情况
     * 2. 两个字符串完全相同的情况
     * 3. 两个字符串完全不同的情况
     * 4. 一个字符串为空的情况
     * 5. 两个字符串都为空的情况
     */
    
    public static int minDistance(String word1, String word2) {
        // 异常处理：检查输入是否为空
        if (word1 == null || word2 == null) {
            return 0;
        }
        
        if (word1.length() == 0) {
            return word2.length();
        }
        
        if (word2.length() == 0) {
            return word1.length();
        }
        
        int m = word1.length();
        int n = word2.length();
        
        // 计算LCS长度
        int lcsLength = longestCommonSubsequence(word1, word2);
        
        // 返回删除步数
        return m + n - 2 * lcsLength;
    }
    
    // 计算两个字符串的最长公共子序列长度
    private static int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length();
        int n = text2.length();
        
        // dp[i][j] 表示 text1[0..i-1] 和 text2[0..j-1] 的最长公共子序列长度
        int[][] dp = new int[m + 1][n + 1];
        
        // 填充dp表
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        return dp[m][n];
    }
    
    // 直接使用动态规划解决删除操作问题（不通过LCS转换）
    public static int minDistanceDirect(String word1, String word2) {
        // 异常处理：检查输入是否为空
        if (word1 == null || word2 == null) {
            return 0;
        }
        
        if (word1.length() == 0) {
            return word2.length();
        }
        
        if (word2.length() == 0) {
            return word1.length();
        }
        
        int m = word1.length();
        int n = word2.length();
        
        // dp[i][j] 表示使word1[0..i-1]和word2[0..j-1]相同的最小删除步数
        int[][] dp = new int[m + 1][n + 1];
        
        // 初始化边界条件
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i; // 删除word1的所有字符
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j; // 删除word2的所有字符
        }
        
        // 填充dp表
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    // 字符相同，不需要删除
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // 字符不同，选择删除步数较少的操作
                    // 1. 删除word1[i-1]：dp[i-1][j] + 1
                    // 2. 删除word2[j-1]：dp[i][j-1] + 1
                    dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + 1;
                }
            }
        }
        
        return dp[m][n];
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        String word1 = "sea";
        String word2 = "eat";
        System.out.println("Test 1 (LCS method): " + minDistance(word1, word2)); 
        System.out.println("Test 1 (Direct method): " + minDistanceDirect(word1, word2)); 
        // 期望输出: 2
        
        // 测试用例2
        word1 = "leetcode";
        word2 = "etco";
        System.out.println("Test 2 (LCS method): " + minDistance(word1, word2)); 
        System.out.println("Test 2 (Direct method): " + minDistanceDirect(word1, word2)); 
        // 期望输出: 4
        
        // 测试用例3
        word1 = "abc";
        word2 = "abc";
        System.out.println("Test 3 (LCS method): " + minDistance(word1, word2)); 
        System.out.println("Test 3 (Direct method): " + minDistanceDirect(word1, word2)); 
        // 期望输出: 0
        
        // 测试用例4
        word1 = "abc";
        word2 = "def";
        System.out.println("Test 4 (LCS method): " + minDistance(word1, word2)); 
        System.out.println("Test 4 (Direct method): " + minDistanceDirect(word1, word2)); 
        // 期望输出: 6
        
        // 测试用例5
        word1 = "";
        word2 = "abc";
        System.out.println("Test 5 (LCS method): " + minDistance(word1, word2)); 
        System.out.println("Test 5 (Direct method): " + minDistanceDirect(word1, word2)); 
        // 期望输出: 3
    }
}
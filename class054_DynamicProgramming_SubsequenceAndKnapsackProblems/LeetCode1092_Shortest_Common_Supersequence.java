package class086;

// LeetCode 1092. 最短公共超序列
// 给你两个字符串 str1 和 str2，返回同时以 str1 和 str2 作为子序列的最短字符串。
// 如果答案不止一个，则可以返回满足条件的任意一个答案。
// 测试链接 : https://leetcode.cn/problems/shortest-common-supersequence/

public class LeetCode1092_Shortest_Common_Supersequence {
    
    /*
     * 算法详解：最短公共超序列（LeetCode 1092）
     * 
     * 问题描述：
     * 给你两个字符串 str1 和 str2，返回同时以 str1 和 str2 作为子序列的最短字符串。
     * 超序列是指包含给定序列为子序列的序列。
     * 
     * 算法思路：
     * 1. 首先计算str1和str2的最长公共子序列(LCS)
     * 2. 通过LCS构造最短公共超序列
     * 3. 使用双指针技术，分别指向str1和str2的开头
     * 4. 遍历LCS中的所有字符，对于每个字符：
     *    - 将str1中在该字符之前的部分添加到结果中
     *    - 将str2中在该字符之前的部分添加到结果中
     *    - 添加该字符本身
     * 5. 最后将str1和str2剩余的部分添加到结果中
     * 
     * 时间复杂度分析：
     * 1. 计算LCS：O(m*n)
     * 2. 构造超序列：O(m+n)
     * 3. 总体时间复杂度：O(m*n)
     * 
     * 空间复杂度分析：
     * 1. dp数组：O(m*n)
     * 2. LCS字符串：O(min(m,n))
     * 3. 结果字符串：O(m+n)
     * 4. 总体空间复杂度：O(m*n)
     * 
     * 工程化考量：
     * 1. 异常处理：检查输入是否为空
     * 2. 边界处理：正确处理空字符串的情况
     * 3. 内存优化：可复用部分计算结果
     * 
     * 极端场景验证：
     * 1. 输入字符串长度达到边界情况
     * 2. 两个字符串完全相同的情况
     * 3. 两个字符串完全不同的情况
     * 4. 一个字符串为空的情况
     * 5. 两个字符串都为空的情况
     */
    
    public static String shortestCommonSupersequence(String str1, String str2) {
        // 异常处理：检查输入是否为空
        if (str1 == null || str2 == null) {
            return "";
        }
        
        if (str1.length() == 0) {
            return str2;
        }
        
        if (str2.length() == 0) {
            return str1;
        }
        
        int m = str1.length();
        int n = str2.length();
        
        // 计算LCS长度和构造dp表
        int[][] dp = new int[m + 1][n + 1];
        
        // 填充dp表
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        // 通过dp表回溯构造LCS
        StringBuilder lcs = new StringBuilder();
        int i = m, j = n;
        while (i > 0 && j > 0) {
            if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                lcs.append(str1.charAt(i - 1));
                i--;
                j--;
            } else if (dp[i - 1][j] > dp[i][j - 1]) {
                i--;
            } else {
                j--;
            }
        }
        
        // 反转LCS字符串，因为我们是从后往前构造的
        String lcsStr = lcs.reverse().toString();
        
        // 通过LCS构造最短公共超序列
        StringBuilder result = new StringBuilder();
        int p1 = 0, p2 = 0;
        
        // 遍历LCS中的每个字符
        for (int k = 0; k < lcsStr.length(); k++) {
            char ch = lcsStr.charAt(k);
            
            // 将str1中在该字符之前的部分添加到结果中
            while (p1 < str1.length() && str1.charAt(p1) != ch) {
                result.append(str1.charAt(p1));
                p1++;
            }
            
            // 将str2中在该字符之前的部分添加到结果中
            while (p2 < str2.length() && str2.charAt(p2) != ch) {
                result.append(str2.charAt(p2));
                p2++;
            }
            
            // 添加该字符本身
            result.append(ch);
            p1++;
            p2++;
        }
        
        // 添加str1和str2剩余的部分
        while (p1 < str1.length()) {
            result.append(str1.charAt(p1));
            p1++;
        }
        
        while (p2 < str2.length()) {
            result.append(str2.charAt(p2));
            p2++;
        }
        
        return result.toString();
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        String str1 = "abac";
        String str2 = "cab";
        System.out.println("Test 1: " + shortestCommonSupersequence(str1, str2)); 
        // 期望输出: "cabac"
        
        // 测试用例2
        str1 = "aaaaaaaa";
        str2 = "aaaaaaaa";
        System.out.println("Test 2: " + shortestCommonSupersequence(str1, str2)); 
        // 期望输出: "aaaaaaaa"
        
        // 测试用例3
        str1 = "abc";
        str2 = "def";
        System.out.println("Test 3: " + shortestCommonSupersequence(str1, str2)); 
        // 期望输出: "abcdef"
        
        // 测试用例4
        str1 = "";
        str2 = "abc";
        System.out.println("Test 4: " + shortestCommonSupersequence(str1, str2)); 
        // 期望输出: "abc"
        
        // 测试用例5
        str1 = "abc";
        str2 = "";
        System.out.println("Test 5: " + shortestCommonSupersequence(str1, str2)); 
        // 期望输出: "abc"
    }
}
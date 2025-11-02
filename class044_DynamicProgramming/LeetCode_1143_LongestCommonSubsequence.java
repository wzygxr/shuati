package class066.supplementary_problems;

/**
 * LeetCode 1143. 最长公共子序列 (Longest Common Subsequence)
 * 
 * 题目来源：LeetCode 1143. Longest Common Subsequence
 * 题目链接：https://leetcode.cn/problems/longest-common-subsequence/
 * 
 * 题目描述：
 * 给定两个字符串 text1 和 text2，返回这两个字符串的最长公共子序列的长度。
 * 如果不存在公共子序列，返回 0。
 * 
 * 一个字符串的子序列是指这样一个新的字符串：
 * 它是由原字符串在不改变字符的相对顺序的情况下删除某些字符（也可以不删除任何字符）后组成的新字符串。
 * 例如，"ace" 是 "abcde" 的子序列，但 "aec" 不是 "abcde" 的子序列。
 * 两个字符串的公共子序列是这两个字符串所共同拥有的子序列。
 * 
 * 示例 1：
 * 输入：text1 = "abcde", text2 = "ace"
 * 输出：3
 * 解释：最长公共子序列是 "ace"，它的长度为 3。
 * 
 * 示例 2：
 * 输入：text1 = "abc", text2 = "abc"
 * 输出：3
 * 解释：最长公共子序列是 "abc"，它的长度为 3。
 * 
 * 示例 3：
 * 输入：text1 = "abc", text2 = "def"
 * 输出：0
 * 解释：两个字符串没有公共子序列，返回 0。
 * 
 * 提示：
 * 1 <= text1.length, text2.length <= 1000
 * text1 和 text2 仅由小写英文字符组成。
 * 
 * 解题思路：
 * 这是一个经典的二维动态规划问题。
 * 状态定义：dp[i][j]表示text1的前i个字符和text2的前j个字符的最长公共子序列长度
 * 状态转移：
 *   如果text1[i-1] == text2[j-1]，则dp[i][j] = dp[i-1][j-1] + 1
 *   否则dp[i][j] = max(dp[i-1][j], dp[i][j-1])
 * 边界条件：dp[0][j] = dp[i][0] = 0
 * 
 * 算法复杂度分析：
 * - 时间复杂度：O(m*n) - m和n分别是两个字符串的长度
 * - 空间复杂度：O(m*n) - 二维dp数组
 * 
 * 工程化考量：
 * 1. 边界处理：正确处理空字符串的情况
 * 2. 性能优化：可以使用滚动数组优化空间复杂度到O(min(m,n))
 * 3. 代码质量：清晰的变量命名和详细的注释说明
 * 
 * 相关题目：
 * - LeetCode 72. 编辑距离
 * - LeetCode 583. 两个字符串的删除操作
 * - LeetCode 712. 两个字符串的最小ASCII删除和
 * - AtCoder Educational DP Contest F - LCS
 */
public class LeetCode_1143_LongestCommonSubsequence {
    
    /**
     * 计算两个字符串的最长公共子序列长度
     * 
     * @param text1 第一个字符串
     * @param text2 第二个字符串
     * @return 最长公共子序列长度
     */
    public static int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length();
        int n = text2.length();
        
        // dp[i][j]表示text1的前i个字符和text2的前j个字符的最长公共子序列长度
        int[][] dp = new int[m + 1][n + 1];
        
        // 填充dp表
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                // 如果当前字符相同，则最长公共子序列长度加1
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    // 如果当前字符不同，则取两种情况的最大值
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        return dp[m][n]; // 返回最长公共子序列长度
    }
    
    /**
     * 空间优化版本
     * 
     * @param text1 第一个字符串
     * @param text2 第二个字符串
     * @return 最长公共子序列长度
     */
    public static int longestCommonSubsequenceOptimized(String text1, String text2) {
        int m = text1.length();
        int n = text2.length();
        
        // 只需要两行来存储状态
        int[] prev = new int[n + 1];
        int[] curr = new int[n + 1];
        
        // 填充dp表
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                // 如果当前字符相同，则最长公共子序列长度加1
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    curr[j] = prev[j - 1] + 1;
                } else {
                    // 如果当前字符不同，则取两种情况的最大值
                    curr[j] = Math.max(prev[j], curr[j - 1]);
                }
            }
            
            // 交换prev和curr
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }
        
        return prev[n]; // 返回最长公共子序列长度
    }
    
    // 测试用例
    public static void main(String[] args) {
        System.out.println("测试LeetCode 1143. 最长公共子序列：");
        
        // 测试用例1
        String text1_1 = "abcde";
        String text2_1 = "ace";
        System.out.println("text1 = \"" + text1_1 + "\", text2 = \"" + text2_1 + "\"");
        System.out.println("最长公共子序列长度（方法1）: " + longestCommonSubsequence(text1_1, text2_1));
        System.out.println("最长公共子序列长度（方法2）: " + longestCommonSubsequenceOptimized(text1_1, text2_1));
        System.out.println("预期结果: 3\n");
        
        // 测试用例2
        String text1_2 = "abc";
        String text2_2 = "abc";
        System.out.println("text1 = \"" + text1_2 + "\", text2 = \"" + text2_2 + "\"");
        System.out.println("最长公共子序列长度（方法1）: " + longestCommonSubsequence(text1_2, text2_2));
        System.out.println("最长公共子序列长度（方法2）: " + longestCommonSubsequenceOptimized(text1_2, text2_2));
        System.out.println("预期结果: 3\n");
        
        // 测试用例3
        String text1_3 = "abc";
        String text2_3 = "def";
        System.out.println("text1 = \"" + text1_3 + "\", text2 = \"" + text2_3 + "\"");
        System.out.println("最长公共子序列长度（方法1）: " + longestCommonSubsequence(text1_3, text2_3));
        System.out.println("最长公共子序列长度（方法2）: " + longestCommonSubsequenceOptimized(text1_3, text2_3));
        System.out.println("预期结果: 0");
    }
}
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

// 为避免编译问题，使用基本C++实现，不依赖STL容器
#define MAXN 1005

// 手动实现字符串长度函数
int strlen(const char* s) {
    int len = 0;
    while (s[len] != '\0') {
        len++;
    }
    return len;
}

// 手动实现最大值函数
int max(int a, int b) {
    return (a > b) ? a : b;
}

/**
 * 计算两个字符串的最长公共子序列长度
 * 
 * @param text1 第一个字符串
 * @param text2 第二个字符串
 * @return 最长公共子序列长度
 */
int longestCommonSubsequence(const char* text1, const char* text2) {
    int m = strlen(text1);
    int n = strlen(text2);
    
    // dp[i][j]表示text1的前i个字符和text2的前j个字符的最长公共子序列长度
    int dp[MAXN][MAXN] = {0};
    
    // 填充dp表
    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            // 如果当前字符相同，则最长公共子序列长度加1
            if (text1[i - 1] == text2[j - 1]) {
                dp[i][j] = dp[i - 1][j - 1] + 1;
            } else {
                // 如果当前字符不同，则取两种情况的最大值
                dp[i][j] = max(dp[i - 1][j], dp[i][j - 1]);
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
int longestCommonSubsequenceOptimized(const char* text1, const char* text2) {
    int m = strlen(text1);
    int n = strlen(text2);
    
    // 只需要两行来存储状态
    int prev[MAXN] = {0};
    int curr[MAXN] = {0};
    
    // 填充dp表
    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            // 如果当前字符相同，则最长公共子序列长度加1
            if (text1[i - 1] == text2[j - 1]) {
                curr[j] = prev[j - 1] + 1;
            } else {
                // 如果当前字符不同，则取两种情况的最大值
                curr[j] = max(prev[j], curr[j - 1]);
            }
        }
        
        // 交换prev和curr
        for (int k = 0; k <= n; k++) {
            prev[k] = curr[k];
        }
    }
    
    return prev[n]; // 返回最长公共子序列长度
}

// 由于C++环境限制，我们只提供函数实现，不包含main函数测试
// 在实际使用中，可以按以下方式调用：
// const char* text1 = "abcde";
// const char* text2 = "ace";
// int result1 = longestCommonSubsequence(text1, text2);
// int result2 = longestCommonSubsequenceOptimized(text1, text2);
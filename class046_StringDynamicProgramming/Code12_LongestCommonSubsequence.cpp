#include <iostream>
#include <vector>
#include <string>
#include <algorithm>

// 最长公共子序列
// 给定两个字符串 text1 和 text2，返回这两个字符串的最长公共子序列的长度
// 一个字符串的 子序列 是指这样一个新的字符串：它是由原字符串在不改变字符的相对顺序的情况下删除某些字符（也可以不删除任何字符）后组成的新字符串
// 测试链接 : https://leetcode.cn/problems/longest-common-subsequence/
class Solution {
public:
    /*
     * 算法思路：
     * 使用动态规划解决最长公共子序列问题
     * dp[i][j] 表示 text1 的前 i 个字符与 text2 的前 j 个字符的最长公共子序列的长度
     * 
     * 状态转移方程：
     * 如果 text1[i-1] == text2[j-1]，则当前字符可以加入公共子序列
     *   dp[i][j] = dp[i-1][j-1] + 1
     * 如果 text1[i-1] != text2[j-1]，则取两种情况的最大值
     *   dp[i][j] = max(dp[i-1][j], dp[i][j-1])
     * 
     * 边界条件：
     * dp[0][j] = 0，表示 text1 为空字符串时，与 text2 的最长公共子序列长度为 0
     * dp[i][0] = 0，表示 text2 为空字符串时，与 text1 的最长公共子序列长度为 0
     * 
     * 时间复杂度：O(n*m)，其中n为text1的长度，m为text2的长度
     * 空间复杂度：O(n*m)
     */
    int longestCommonSubsequence1(std::string text1, std::string text2) {
        if (text1.empty() || text2.empty()) {
            return 0;
        }
        int n = text1.length();
        int m = text2.length();
        // dp[i][j] 表示text1[0...i-1]和text2[0...j-1]的最长公共子序列长度
        std::vector<std::vector<int>> dp(n + 1, std::vector<int>(m + 1, 0));
        
        // 填充dp表
        for (int i = 1; i <= n; ++i) {
            for (int j = 1; j <= m; ++j) {
                if (text1[i - 1] == text2[j - 1]) {
                    // 当前字符相同，可以加入公共子序列
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    // 当前字符不同，取两种情况的最大值
                    dp[i][j] = std::max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        return dp[n][m];
    }

    /*
     * 空间优化版本
     * 观察状态转移方程，dp[i][j]只依赖于dp[i-1][j-1]、dp[i-1][j]和dp[i][j-1]
     * 可以使用一维数组优化空间复杂度
     * 
     * 时间复杂度：O(n*m)
     * 空间复杂度：O(min(n,m))
     */
    int longestCommonSubsequence2(std::string text1, std::string text2) {
        if (text1.empty() || text2.empty()) {
            return 0;
        }
        
        // 为了节省空间，让较短的字符串作为第二个参数
        if (text1.length() < text2.length()) {
            std::swap(text1, text2);
        }
        
        int n = text1.length();
        int m = text2.length();
        // 使用一维数组存储当前行的数据
        std::vector<int> dp(m + 1, 0);
        // 保存左上角的值(dp[i-1][j-1])
        int pre = 0;
        
        // 按行填充dp表
        for (int i = 1; i <= n; ++i) {
            pre = 0; // 每行开始时，左上角的值为0
            for (int j = 1; j <= m; ++j) {
                int temp = dp[j]; // 保存当前dp[j]，用于下一轮的pre
                if (text1[i - 1] == text2[j - 1]) {
                    // 当前字符相同
                    dp[j] = pre + 1;
                } else {
                    // 当前字符不同，取上方或左方的最大值
                    dp[j] = std::max(dp[j], dp[j - 1]);
                }
                pre = temp; // 更新pre为下一轮的左上角值
            }
        }
        
        return dp[m];
    }
};

// 单元测试
int main() {
    Solution solution;
    
    // 测试用例1: "abcde", "ace"
    // 预期输出: 3 ("ace")
    std::cout << "Test 1: " << solution.longestCommonSubsequence1("abcde", "ace") << std::endl; // 应输出3
    std::cout << "Test 1 (Space Optimized): " << solution.longestCommonSubsequence2("abcde", "ace") << std::endl; // 应输出3
    
    // 测试用例2: "abc", "abc"
    // 预期输出: 3 ("abc")
    std::cout << "Test 2: " << solution.longestCommonSubsequence1("abc", "abc") << std::endl; // 应输出3
    std::cout << "Test 2 (Space Optimized): " << solution.longestCommonSubsequence2("abc", "abc") << std::endl; // 应输出3
    
    // 测试用例3: "abc", "def"
    // 预期输出: 0 (无公共子序列)
    std::cout << "Test 3: " << solution.longestCommonSubsequence1("abc", "def") << std::endl; // 应输出0
    std::cout << "Test 3 (Space Optimized): " << solution.longestCommonSubsequence2("abc", "def") << std::endl; // 应输出0
    
    // 边界测试: 空字符串
    std::cout << "Test 4 (Empty String): " << solution.longestCommonSubsequence1("", "abc") << std::endl; // 应输出0
    std::cout << "Test 4 (Empty String, Space Optimized): " << solution.longestCommonSubsequence2("", "abc") << std::endl; // 应输出0
    
    // 边界测试: 单字符匹配
    std::cout << "Test 5 (Single Char Match): " << solution.longestCommonSubsequence1("a", "a") << std::endl; // 应输出1
    std::cout << "Test 5 (Single Char Match, Space Optimized): " << solution.longestCommonSubsequence2("a", "a") << std::endl; // 应输出1
    
    // 边界测试: 单字符不匹配
    std::cout << "Test 6 (Single Char No Match): " << solution.longestCommonSubsequence1("a", "b") << std::endl; // 应输出0
    std::cout << "Test 6 (Single Char No Match, Space Optimized): " << solution.longestCommonSubsequence2("a", "b") << std::endl; // 应输出0
    
    return 0;
}
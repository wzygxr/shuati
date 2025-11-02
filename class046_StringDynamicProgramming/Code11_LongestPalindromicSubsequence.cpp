#include <iostream>
#include <vector>
#include <string>
#include <algorithm>

// 最长回文子序列 (Longest Palindromic Subsequence)
// 给你一个字符串 s ，找出其中最长的回文子序列，并返回该子序列的长度
// 子序列定义为：不改变剩余字符顺序的情况下，删除某些字符或者不删除任何字符形成的一个序列
// 
// 题目来源：LeetCode 516. 最长回文子序列
// 测试链接：https://leetcode.cn/problems/longest-palindromic-subsequence/
//
// 算法核心思想：
// 使用动态规划解决最长回文子序列问题，通过构建二维DP表来计算最长回文子序列长度
//
// 时间复杂度分析：
// - 基础版本：O(n²)，其中n为s的长度
// - 空间优化版本：O(n²)时间，O(n)空间
//
// 空间复杂度分析：
// - 基础版本：O(n²)
// - 空间优化版本：O(n)
//
// 最优解判定：✅ 是最优解，时间复杂度无法进一步优化，空间复杂度已优化到O(n)
//
// 工程化考量：
// 1. 异常处理：检查输入参数合法性
// 2. 边界条件：处理空字符串和极端情况
// 3. 性能优化：使用滚动数组减少空间占用
// 4. 代码可读性：添加详细注释和测试用例
//
// 与其他领域的联系：
// - 生物信息学：DNA序列分析
// - 文本处理：回文检测
// - 密码学：回文密码分析

class Solution {
public:
    /*
     * 算法思路：
     * 使用动态规划解决最长回文子序列问题
     * dp[i][j] 表示字符串s在区间[i,j]内的最长回文子序列长度
     * 
     * 状态转移方程：
     * 如果 s[i] == s[j]，则可以将这两个字符加入回文子序列中
     *   dp[i][j] = dp[i+1][j-1] + 2
     * 如果 s[i] != s[j]，则取左右两边的最大值
     *   dp[i][j] = max(dp[i+1][j], dp[i][j-1])
     * 
     * 边界条件：
     * dp[i][i] = 1，表示单个字符是长度为1的回文子序列
     * dp[i][j] = 0，当i > j时
     * 
     * 时间复杂度：O(n²)，其中n为字符串s的长度
     * 空间复杂度：O(n²)
     */
    int longestPalindromeSubseq1(std::string s) {
        if (s.empty()) {
            return 0;
        }
        int n = s.length();
        // dp[i][j] 表示s[i...j]范围上的最长回文子序列长度
        std::vector<std::vector<int> > dp(n, std::vector<int>(n, 0));
        
        // 初始化对角线上的元素，表示单个字符是回文
        for (int i = 0; i < n; ++i) {
            dp[i][i] = 1;
        }
        
        // 初始化次对角线，两个字符的情况
        for (int i = 0; i < n - 1; ++i) {
            dp[i][i + 1] = (s[i] == s[i + 1]) ? 2 : 1;
        }
        
        // 按长度由小到大填充dp表
        for (int len = 3; len <= n; ++len) {
            for (int i = 0, j; (j = i + len - 1) < n; ++i) {
                if (s[i] == s[j]) {
                    // 首尾字符相同，可以同时加入回文子序列
                    dp[i][j] = dp[i + 1][j - 1] + 2;
                } else {
                    // 首尾字符不同，取左或右的最大值
                    dp[i][j] = std::max(dp[i + 1][j], dp[i][j - 1]);
                }
            }
        }
        
        return dp[0][n - 1];
    }

    /*
     * 空间优化版本
     * 观察状态转移方程，dp[i][j]依赖于dp[i+1][j-1]、dp[i+1][j]和dp[i][j-1]
     * 对于从左到右、从下到上的填充方式，可以优化到一维数组
     * 
     * 时间复杂度：O(n²)
     * 空间复杂度：O(n)
     */
    int longestPalindromeSubseq2(std::string s) {
        if (s.empty()) {
            return 0;
        }
        int n = s.length();
        // 使用一维数组存储当前行的数据
        std::vector<int> dp(n, 0);
        // 存储左上角的值(dp[i+1][j-1])
        int temp = 0;
        // 存储上一次的temp值
        int pre = 0;
        
        // 从下到上，从左到右填充
        for (int i = n - 1; i >= 0; --i) {
            // 单个字符的情况
            dp[i] = 1;
            pre = 0; // 重置pre
            // j从i+1开始向右扩展
            for (int j = i + 1; j < n; ++j) {
                temp = dp[j]; // 保存当前dp[j]，即dp[i+1][j]
                if (s[i] == s[j]) {
                    // 当前dp[j] = dp[i+1][j-1] + 2
                    dp[j] = pre + 2;
                } else {
                    // 当前dp[j] = max(dp[i+1][j], dp[i][j-1])
                    dp[j] = std::max(dp[j], dp[j - 1]);
                }
                pre = temp; // 更新pre为下一轮的左上角值
            }
        }
        
        return dp[n - 1];
    }
};

// 单元测试
int main() {
    Solution solution;
    
    // 测试用例1: "bbbab"
    // 预期输出: 4 ("bbbb")
    std::cout << "Test 1: " << solution.longestPalindromeSubseq1("bbbab") << std::endl; // 应输出4
    std::cout << "Test 1 (Space Optimized): " << solution.longestPalindromeSubseq2("bbbab") << std::endl; // 应输出4
    
    // 测试用例2: "cbbd"
    // 预期输出: 2 ("bb")
    std::cout << "Test 2: " << solution.longestPalindromeSubseq1("cbbd") << std::endl; // 应输出2
    std::cout << "Test 2 (Space Optimized): " << solution.longestPalindromeSubseq2("cbbd") << std::endl; // 应输出2
    
    // 边界测试: 空字符串
    std::cout << "Test 3 (Empty String): " << solution.longestPalindromeSubseq1("") << std::endl; // 应输出0
    std::cout << "Test 3 (Empty String, Space Optimized): " << solution.longestPalindromeSubseq2("") << std::endl; // 应输出0
    
    // 边界测试: 单字符
    std::cout << "Test 4 (Single Char): " << solution.longestPalindromeSubseq1("a") << std::endl; // 应输出1
    std::cout << "Test 4 (Single Char, Space Optimized): " << solution.longestPalindromeSubseq2("a") << std::endl; // 应输出1
    
    // 边界测试: 全部相同字符
    std::cout << "Test 5 (All Same): " << solution.longestPalindromeSubseq1("aaaaa") << std::endl; // 应输出5
    std::cout << "Test 5 (All Same, Space Optimized): " << solution.longestPalindromeSubseq2("aaaaa") << std::endl; // 应输出5
    
    // 边界测试: 全部不同字符
    std::cout << "Test 6 (All Different): " << solution.longestPalindromeSubseq1("abcde") << std::endl; // 应输出1
    std::cout << "Test 6 (All Different, Space Optimized): " << solution.longestPalindromeSubseq2("abcde") << std::endl; // 应输出1
    
    return 0;
}
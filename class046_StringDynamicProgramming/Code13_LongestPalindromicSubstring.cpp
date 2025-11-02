#include <iostream>
#include <vector>
#include <string>

// 最长回文子串
// 给你一个字符串 s，找到 s 中最长的回文子串
// 测试链接 : https://leetcode.cn/problems/longest-palindromic-substring/
class Solution {
public:
    /*
     * 算法思路：
     * 使用动态规划解决最长回文子串问题
     * dp[i][j] 表示字符串s在区间[i,j]内是否是回文子串
     * 
     * 状态转移方程：
     * 如果 s[i] == s[j]，则取决于中间子串是否为回文
     *   dp[i][j] = dp[i+1][j-1]
     * 特殊情况：当子串长度小于等于3时，只需检查首尾字符是否相等
     *   dp[i][j] = (s[i] == s[j])
     * 
     * 边界条件：
     * dp[i][i] = true，表示单个字符是回文子串
     * dp[i][i+1] = (s[i] == s[i+1])，表示两个字符的回文判断
     * 
     * 时间复杂度：O(n²)，其中n为字符串s的长度
     * 空间复杂度：O(n²)
     */
    std::string longestPalindrome1(std::string s) {
        if (s.empty() || s.length() < 2) {
            return s;
        }
        int n = s.length();
        // dp[i][j] 表示s[i...j]是否是回文子串
        std::vector<std::vector<bool>> dp(n, std::vector<bool>(n, false));
        int maxLen = 1;
        int start = 0;
        
        // 初始化：单个字符和两个字符的情况
        for (int i = 0; i < n; ++i) {
            dp[i][i] = true; // 单个字符是回文
            // 初始化两个字符的情况
            if (i < n - 1 && s[i] == s[i + 1]) {
                dp[i][i + 1] = true;
                maxLen = 2;
                start = i;
            }
        }
        
        // 按子串长度由小到大填充dp表
        for (int len = 3; len <= n; ++len) {
            for (int i = 0; i <= n - len; ++i) {
                int j = i + len - 1;
                // 首尾字符相等，且中间子串是回文，则整个子串是回文
                if (s[i] == s[j] && dp[i + 1][j - 1]) {
                    dp[i][j] = true;
                    if (len > maxLen) {
                        maxLen = len;
                        start = i;
                    }
                }
            }
        }
        
        return s.substr(start, maxLen);
    }

    /*
     * 中心扩展法
     * 回文串都是从中心向两边对称的，可以枚举每一个可能的中心点，然后向两边扩展
     * 注意：中心点可能是一个字符（奇数长度）或两个字符之间的位置（偶数长度）
     * 
     * 时间复杂度：O(n²)
     * 空间复杂度：O(1)
     */
    std::string longestPalindrome2(std::string s) {
        if (s.empty() || s.length() < 2) {
            return s;
        }
        int n = s.length();
        int maxLen = 1;
        int start = 0;
        
        // 枚举每一个可能的中心点
        for (int i = 0; i < n; ++i) {
            // 以单个字符为中心（奇数长度）
            std::pair<int, int> res1 = expandAroundCenter(s, i, i);
            // 以两个字符之间为中心（偶数长度）
            std::pair<int, int> res2 = expandAroundCenter(s, i, i + 1);
            
            // 更新最长回文子串
            if (res1.second > maxLen) {
                maxLen = res1.second;
                start = res1.first;
            }
            if (res2.second > maxLen) {
                maxLen = res2.second;
                start = res2.first;
            }
        }
        
        return s.substr(start, maxLen);
    }
    
    /*
     * 从中心向两边扩展寻找回文子串
     * 返回值：pair<起始索引, 长度>
     */
    std::pair<int, int> expandAroundCenter(const std::string& s, int left, int right) {
        int n = s.length();
        while (left >= 0 && right < n && s[left] == s[right]) {
            --left;
            ++right;
        }
        // 返回起始索引和长度
        return {left + 1, right - left - 1};
    }
};

// 单元测试
int main() {
    Solution solution;
    
    // 测试用例1: "babad"
    // 预期输出: "bab" 或 "aba"
    std::cout << "Test 1: " << solution.longestPalindrome1("babad") << std::endl;
    std::cout << "Test 1 (Expand Around Center): " << solution.longestPalindrome2("babad") << std::endl;
    
    // 测试用例2: "cbbd"
    // 预期输出: "bb"
    std::cout << "Test 2: " << solution.longestPalindrome1("cbbd") << std::endl;
    std::cout << "Test 2 (Expand Around Center): " << solution.longestPalindrome2("cbbd") << std::endl;
    
    // 边界测试: 单字符
    std::cout << "Test 3 (Single Char): " << solution.longestPalindrome1("a") << std::endl; // 应输出"a"
    std::cout << "Test 3 (Single Char, Expand Around Center): " << solution.longestPalindrome2("a") << std::endl; // 应输出"a"
    
    // 边界测试: 全部相同字符
    std::cout << "Test 4 (All Same): " << solution.longestPalindrome1("aaaaa") << std::endl; // 应输出"aaaaa"
    std::cout << "Test 4 (All Same, Expand Around Center): " << solution.longestPalindrome2("aaaaa") << std::endl; // 应输出"aaaaa"
    
    // 测试用例5: "ac"
    // 预期输出: "a" 或 "c"
    std::cout << "Test 5: " << solution.longestPalindrome1("ac") << std::endl;
    std::cout << "Test 5 (Expand Around Center): " << solution.longestPalindrome2("ac") << std::endl;
    
    return 0;
}
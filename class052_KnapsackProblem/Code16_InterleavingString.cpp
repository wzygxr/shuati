#include <iostream>
#include <vector>
#include <string>
using namespace std;

// 交错字符串
// 给定三个字符串 s1、s2、s3，请你帮忙验证 s3 是否是由 s1 和 s2 交错组成的
// 测试链接 : https://leetcode.cn/problems/interleaving-string/

/*
 * 算法详解：
 * 交错字符串问题是一个经典的动态规划问题，用于验证字符串s3是否由s1和s2交错组成。
 * 交错意味着s3中的字符顺序必须保持s1和s2中字符的相对顺序。
 * 
 * 解题思路：
 * 1. 状态定义：dp[i][j]表示s1的前i个字符和s2的前j个字符能否交错组成s3的前i+j个字符
 * 2. 状态转移方程：
 *    - 如果s1[i-1] == s3[i+j-1]，则dp[i][j] = dp[i][j] || dp[i-1][j]
 *    - 如果s2[j-1] == s3[i+j-1]，则dp[i][j] = dp[i][j] || dp[i][j-1]
 * 3. 初始化：
 *    - dp[0][0] = true（两个空字符串可以组成空字符串）
 *    - dp[i][0] = (s1的前i个字符等于s3的前i个字符)
 *    - dp[0][j] = (s2的前j个字符等于s3的前j个字符)
 * 
 * 时间复杂度分析：
 * 设s1长度为m，s2长度为n
 * 1. 动态规划计算：O(m * n)
 * 总时间复杂度：O(m * n)
 * 
 * 空间复杂度分析：
 * 1. 二维DP数组：O(m * n)
 * 2. 空间优化后：O(min(m, n))
 */

class Solution {
public:
    // 标准二维DP版本
    bool isInterleave(string s1, string s2, string s3) {
        int m = s1.size();
        int n = s2.size();
        int len = s3.size();
        
        // 长度检查
        if (m + n != len) return false;
        
        // 创建DP数组
        vector<vector<bool>> dp(m + 1, vector<bool>(n + 1, false));
        
        // 初始化
        dp[0][0] = true;
        
        // 初始化第一列：只使用s1
        for (int i = 1; i <= m; i++) {
            dp[i][0] = dp[i - 1][0] && (s1[i - 1] == s3[i - 1]);
        }
        
        // 初始化第一行：只使用s2
        for (int j = 1; j <= n; j++) {
            dp[0][j] = dp[0][j - 1] && (s2[j - 1] == s3[j - 1]);
        }
        
        // 填充DP数组
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                char c3 = s3[i + j - 1];
                
                // 检查是否可以从s1取字符
                if (s1[i - 1] == c3) {
                    dp[i][j] = dp[i][j] || dp[i - 1][j];
                }
                
                // 检查是否可以从s2取字符
                if (s2[j - 1] == c3) {
                    dp[i][j] = dp[i][j] || dp[i][j - 1];
                }
            }
        }
        
        return dp[m][n];
    }
    
    // 空间优化版本（使用一维数组）
    bool isInterleaveOptimized(string s1, string s2, string s3) {
        int m = s1.size();
        int n = s2.size();
        int len = s3.size();
        
        // 长度检查
        if (m + n != len) return false;
        
        // 为了节省空间，让s2作为较短的字符串
        if (m < n) {
            return isInterleaveOptimized(s2, s1, s3);
        }
        
        // 使用一维DP数组
        vector<bool> dp(n + 1, false);
        
        // 初始化
        dp[0] = true;
        
        // 初始化第一行：只使用s2
        for (int j = 1; j <= n; j++) {
            dp[j] = dp[j - 1] && (s2[j - 1] == s3[j - 1]);
        }
        
        // 填充DP数组
        for (int i = 1; i <= m; i++) {
            // 更新第一列：只使用s1
            dp[0] = dp[0] && (s1[i - 1] == s3[i - 1]);
            
            for (int j = 1; j <= n; j++) {
                char c3 = s3[i + j - 1];
                bool result = false;
                
                // 检查是否可以从s1取字符
                if (s1[i - 1] == c3) {
                    result = result || dp[j];  // 相当于dp[i-1][j]
                }
                
                // 检查是否可以从s2取字符
                if (s2[j - 1] == c3) {
                    result = result || dp[j - 1];  // 相当于dp[i][j-1]
                }
                
                dp[j] = result;
            }
        }
        
        return dp[n];
    }
};

// 测试函数
void testInterleavingString() {
    Solution solution;
    
    // 测试用例1
    string s1_1 = "aabcc";
    string s2_1 = "dbbca";
    string s3_1 = "aadbbcbcac";
    cout << "测试用例1:" << endl;
    cout << "标准版本: " << (solution.isInterleave(s1_1, s2_1, s3_1) ? "true" : "false") << endl;
    cout << "优化版本: " << (solution.isInterleaveOptimized(s1_1, s2_1, s3_1) ? "true" : "false") << endl;
    cout << "预期结果: true" << endl;
    cout << endl;
    
    // 测试用例2
    string s1_2 = "aabcc";
    string s2_2 = "dbbca";
    string s3_2 = "aadbbbaccc";
    cout << "测试用例2:" << endl;
    cout << "标准版本: " << (solution.isInterleave(s1_2, s2_2, s3_2) ? "true" : "false") << endl;
    cout << "优化版本: " << (solution.isInterleaveOptimized(s1_2, s2_2, s3_2) ? "true" : "false") << endl;
    cout << "预期结果: false" << endl;
    cout << endl;
    
    // 测试用例3：边界情况
    string s1_3 = "";
    string s2_3 = "";
    string s3_3 = "";
    cout << "测试用例3（空字符串）:" << endl;
    cout << "标准版本: " << (solution.isInterleave(s1_3, s2_3, s3_3) ? "true" : "false") << endl;
    cout << "优化版本: " << (solution.isInterleaveOptimized(s1_3, s2_3, s3_3) ? "true" : "false") << endl;
    cout << "预期结果: true" << endl;
    cout << endl;
    
    // 测试用例4：长度不匹配
    string s1_4 = "abc";
    string s2_4 = "def";
    string s3_4 = "abcd";
    cout << "测试用例4（长度不匹配）:" << endl;
    cout << "标准版本: " << (solution.isInterleave(s1_4, s2_4, s3_4) ? "true" : "false") << endl;
    cout << "优化版本: " << (solution.isInterleaveOptimized(s1_4, s2_4, s3_4) ? "true" : "false") << endl;
    cout << "预期结果: false" << endl;
}

int main() {
    testInterleavingString();
    return 0;
}

/*
 * =============================================================================================
 * 补充题目：LeetCode 115. 不同的子序列（C++实现）
 * 题目链接：https://leetcode.cn/problems/distinct-subsequences/
 * 
 * C++实现：
 * class Solution {
 * public:
 *     int numDistinct(string s, string t) {
 *         int m = s.size();
 *         int n = t.size();
 *         
 *         // 快速判断特殊情况
 *         if (m < n) return 0;
 *         if (m == n) return s == t ? 1 : 0;
 *         
 *         vector<vector<long long>> dp(m + 1, vector<long long>(n + 1, 0));
 *         
 *         // 初始化
 *         for (int i = 0; i <= m; i++) {
 *             dp[i][0] = 1;
 *         }
 *         
 *         for (int i = 1; i <= m; i++) {
 *             for (int j = 1; j <= n; j++) {
 *                 if (s[i - 1] == t[j - 1]) {
 *                     dp[i][j] = dp[i - 1][j - 1] + dp[i - 1][j];
 *                 } else {
 *                     dp[i][j] = dp[i - 1][j];
 *                 }
 *             }
 *         }
 *         
 *         return dp[m][n];
 *     }
 * };
 * 
 * 工程化考量：
 * 1. 使用long long类型防止整数溢出
 * 2. 使用引用避免不必要的字符串拷贝
 * 3. 添加异常处理机制
 * 4. 使用const引用作为函数参数
 */
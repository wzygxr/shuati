#include <iostream>
#include <vector>
#include <algorithm>
#include <string>
using namespace std;

// 最长回文子序列
// 给你一个字符串 s ，找出其中最长的回文子序列，并返回该序列的长度。
// 子序列定义为：不改变剩余字符顺序的情况下，删除某些字符或者不删除任何字符形成的一个序列。
// 测试链接 : https://leetcode.cn/problems/longest-palindromic-subsequence/

class Solution {
public:
    // 区间动态规划解法
    // 时间复杂度: O(n^2) - 两层循环：区间长度、区间起点
    // 空间复杂度: O(n^2) - dp数组占用空间
    // 解题思路:
    // 1. 状态定义：dp[i][j]表示字符串s在区间[i,j]内最长回文子序列的长度
    // 2. 状态转移：
    //    - 如果s[i] == s[j]，则dp[i][j] = dp[i+1][j-1] + 2
    //    - 如果s[i] != s[j]，则dp[i][j] = max(dp[i+1][j], dp[i][j-1])
    int longestPalindromeSubseq(string s) {
        int n = s.length();
        
        // dp[i][j]表示字符串s在区间[i,j]内最长回文子序列的长度
        vector<vector<int>> dp(n, vector<int>(n, 0));
        
        // 初始化：单个字符的回文长度为1
        for (int i = 0; i < n; i++) {
            dp[i][i] = 1;
        }
        
        // 枚举区间长度，从2开始
        for (int len = 2; len <= n; len++) {
            // 枚举区间起点i
            for (int i = 0; i <= n - len; i++) {
                // 计算区间终点j
                int j = i + len - 1;
                
                if (s[i] == s[j]) {
                    // 两端字符相同，长度为内层回文长度+2
                    if (len == 2) {
                        // 特殊情况：长度为2时，没有内层
                        dp[i][j] = 2;
                    } else {
                        // 一般情况：内层回文长度+2
                        dp[i][j] = dp[i + 1][j - 1] + 2;
                    }
                } else {
                    // 两端字符不同，取较大值
                    dp[i][j] = max(dp[i + 1][j], dp[i][j - 1]);
                }
            }
        }
        
        return dp[0][n - 1];
    }
};

int main() {
    // 读取输入
    string s;
    getline(cin, s);
    
    // 计算结果
    Solution solution;
    int result = solution.longestPalindromeSubseq(s);
    
    // 输出结果
    cout << result << endl;
    
    return 0;
}
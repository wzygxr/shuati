/**
 * LeetCode 132. 分割回文串 II
 * 
 * 题目描述:
 * 给你一个字符串 s，请你将 s 分割成一些子串，使每个子串都是回文串。返回符合要求的最少分割次数
 * 
 * 输入格式:
 * 字符串 s
 * 
 * 输出格式:
 * 整数，表示最少分割次数
 * 
 * 数据范围:
 * 1 <= s.length <= 2000
 * s 仅由小写英文字母组成
 * 
 * 题目链接: https://leetcode.cn/problems/palindrome-partitioning-ii/
 * 
 * 解题思路:
 * 使用Manacher算法预处理回文信息，然后结合动态规划求解最少分割次数
 * 
 * 算法步骤:
 * 1. 使用Manacher算法预处理字符串，得到每个位置的回文半径
 * 2. 构建动态规划数组dp，dp[i]表示前i个字符的最少分割次数
 * 3. 遍历字符串，对于每个位置i，如果s[0..i]是回文串，则dp[i] = 0
 * 4. 否则，遍历所有可能的分割点j，如果s[j+1..i]是回文串，则更新dp[i] = min(dp[i], dp[j] + 1)
 * 5. 返回dp[n-1]
 * 
 * 时间复杂度: O(n^2)
 * 空间复杂度: O(n)
 */

#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
#include <climits>
using namespace std;

/**
 * 使用Manacher算法预处理回文信息
 * 
 * @param s 输入字符串
 * @return 回文半径数组
 */
vector<int> manacher_preprocess(const string& s) {
    string processed = "#";
    for (char c : s) {
        processed += c;
        processed += '#';
    }
    
    int n = processed.size();
    vector<int> p(n, 0);
    int center = 0, right = 0;
    
    for (int i = 0; i < n; i++) {
        if (i < right) {
            int mirror = 2 * center - i;
            p[i] = min(right - i, p[mirror]);
        }
        
        while (i + p[i] + 1 < n && i - p[i] - 1 >= 0 && 
               processed[i + p[i] + 1] == processed[i - p[i] - 1]) {
            p[i]++;
        }
        
        if (i + p[i] > right) {
            center = i;
            right = i + p[i];
        }
    }
    
    return p;
}

/**
 * 判断子串s[left..right]是否为回文串
 * 
 * @param p Manacher预处理结果
 * @param left 子串左边界
 * @param right 子串右边界
 * @return 是否为回文串
 */
bool is_palindrome(const vector<int>& p, int left, int right) {
    // 将原字符串位置转换为预处理字符串位置
    int processed_left = left * 2 + 1;
    int processed_right = right * 2 + 1;
    int processed_center = (processed_left + processed_right) / 2;
    
    // 计算回文半径是否足够覆盖整个子串
    int radius = (right - left + 1) / 2;
    return p[processed_center] >= radius;
}

/**
 * 计算最少分割次数
 * 
 * @param s 输入字符串
 * @return 最少分割次数
 */
int minCut(string s) {
    int n = s.size();
    if (n <= 1) return 0;
    
    // 使用Manacher算法预处理
    vector<int> p = manacher_preprocess(s);
    
    // 动态规划数组
    vector<int> dp(n, INT_MAX);
    
    for (int i = 0; i < n; i++) {
        // 如果s[0..i]是回文串，不需要分割
        if (is_palindrome(p, 0, i)) {
            dp[i] = 0;
            continue;
        }
        
        // 遍历所有可能的分割点
        for (int j = 0; j < i; j++) {
            // 如果s[j+1..i]是回文串
            if (is_palindrome(p, j + 1, i)) {
                if (dp[j] != INT_MAX) {
                    dp[i] = min(dp[i], dp[j] + 1);
                }
            }
        }
    }
    
    return dp[n - 1];
}

/**
 * 优化版本：使用动态规划预处理回文信息
 * 
 * 时间复杂度: O(n^2)
 * 空间复杂度: O(n^2)
 */
int minCut_optimized(string s) {
    int n = s.size();
    if (n <= 1) return 0;
    
    // 预处理回文信息
    vector<vector<bool>> isPal(n, vector<bool>(n, false));
    
    // 初始化对角线（单个字符都是回文）
    for (int i = 0; i < n; i++) {
        isPal[i][i] = true;
    }
    
    // 填充回文表
    for (int len = 2; len <= n; len++) {
        for (int i = 0; i <= n - len; i++) {
            int j = i + len - 1;
            if (s[i] == s[j]) {
                if (len == 2 || isPal[i + 1][j - 1]) {
                    isPal[i][j] = true;
                }
            }
        }
    }
    
    // 动态规划
    vector<int> dp(n, INT_MAX);
    for (int i = 0; i < n; i++) {
        if (isPal[0][i]) {
            dp[i] = 0;
        } else {
            for (int j = 0; j < i; j++) {
                if (isPal[j + 1][i]) {
                    if (dp[j] != INT_MAX) {
                        dp[i] = min(dp[i], dp[j] + 1);
                    }
                }
            }
        }
    }
    
    return dp[n - 1];
}

/**
 * 测试用例和验证
 */
int main() {
    // 测试用例1
    string s1 = "aab";
    cout << "输入: \"" << s1 << "\", 输出: " << minCut(s1) << " (期望: 1)" << endl;
    cout << "优化版本: " << minCut_optimized(s1) << " (期望: 1)" << endl;
    
    // 测试用例2
    string s2 = "a";
    cout << "输入: \"" << s2 << "\", 输出: " << minCut(s2) << " (期望: 0)" << endl;
    cout << "优化版本: " << minCut_optimized(s2) << " (期望: 0)" << endl;
    
    // 测试用例3
    string s3 = "ab";
    cout << "输入: \"" << s3 << "\", 输出: " << minCut(s3) << " (期望: 1)" << endl;
    cout << "优化版本: " << minCut_optimized(s3) << " (期望: 1)" << endl;
    
    // 测试用例4
    string s4 = "aba";
    cout << "输入: \"" << s4 << "\", 输出: " << minCut(s4) << " (期望: 0)" << endl;
    cout << "优化版本: " << minCut_optimized(s4) << " (期望: 0)" << endl;
    
    // 测试用例5
    string s5 = "abcba";
    cout << "输入: \"" << s5 << "\", 输出: " << minCut(s5) << " (期望: 0)" << endl;
    cout << "优化版本: " << minCut_optimized(s5) << " (期望: 0)" << endl;
    
    return 0;
}

/**
 * 算法分析:
 * 
 * 1. Manacher+动态规划方法:
 *    - 时间复杂度: O(n^2) - 预处理O(n)，动态规划O(n^2)
 *    - 空间复杂度: O(n) - 存储预处理结果
 * 
 * 2. 纯动态规划方法:
 *    - 时间复杂度: O(n^2) - 填充回文表O(n^2)，动态规划O(n^2)
 *    - 空间复杂度: O(n^2) - 存储回文表
 * 
 * 3. 优化建议:
 *    - 对于大规模数据，使用Manacher+动态规划方法更优
 *    - 对于小规模数据，纯动态规划方法实现更简单
 *    - 可以根据实际数据规模选择合适的算法
 */
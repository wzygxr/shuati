/**
 * 扰乱字符串 (Scramble String) - 字符串动态规划 - C++实现
 * 
 * 题目描述：
 * 使用下面描述的算法可以扰乱字符串 s 得到字符串 t ：
 * 步骤1 : 如果字符串的长度为 1 ，算法停止
 * 步骤2 : 如果字符串的长度 > 1 ，执行下述步骤：
 *        在一个随机下标处将字符串分割成两个非空的子字符串
 *        已知字符串s，则可以将其分成两个子字符串x和y且满足s=x+y
 *        可以决定是要交换两个子字符串还是要保持这两个子字符串的顺序不变
 *        即s可能是 s = x + y 或者 s = y + x
 *        在x和y这两个子字符串上继续从步骤1开始递归执行此算法
 * 给你两个长度相等的字符串 s1 和 s2，判断 s2 是否是 s1 的扰乱字符串。
 * 如果是，返回true；否则，返回false。
 * 
 * 题目来源：LeetCode 87. 扰乱字符串
 * 测试链接：https://leetcode.cn/problems/scramble-string/
 * 
 * 解题思路：
 * 这是一个复杂的字符串动态规划问题，需要判断一个字符串是否可以通过扰乱操作变成另一个字符串。
 * 扰乱操作包括分割字符串和可能交换子字符串的顺序。
 * 
 * 算法实现：
 * 1. 记忆化搜索：递归检查所有可能的分割位置和交换情况
 * 2. 动态规划：自底向上填表，处理所有可能的子串组合
 * 
 * 时间复杂度分析：
 * - 记忆化搜索：O(n^4)，需要检查所有可能的子串组合
 * - 动态规划：O(n^4)，四重循环
 * 
 * 空间复杂度分析：
 * - 记忆化搜索：O(n^3)，三维记忆化数组
 * - 动态规划：O(n^3)，三维DP表
 */

#include <iostream>
#include <vector>
#include <string>
#include <unordered_map>
#include <functional>
using namespace std;

class Solution {
public:
    /**
     * 记忆化搜索解法
     * 
     * @param s1 字符串1
     * @param s2 字符串2
     * @return 是否是扰乱字符串
     */
    bool isScramble(string s1, string s2) {
        int n = s1.length();
        if (n != s2.length()) return false;
        if (s1 == s2) return true;
        
        // 检查字符频率是否相同
        vector<int> count(26, 0);
        for (int i = 0; i < n; i++) {
            count[s1[i] - 'a']++;
            count[s2[i] - 'a']--;
        }
        for (int i = 0; i < 26; i++) {
            if (count[i] != 0) return false;
        }
        
        // 记忆化数组
        vector<vector<vector<int>>> memo(n, 
            vector<vector<int>>(n, vector<int>(n + 1, -1)));
        
        function<bool(int, int, int)> dfs = [&](int i1, int i2, int len) -> bool {
            if (len == 1) {
                return s1[i1] == s2[i2];
            }
            
            if (memo[i1][i2][len] != -1) {
                return memo[i1][i2][len] == 1;
            }
            
            // 检查字符频率
            vector<int> charCount(26, 0);
            for (int i = 0; i < len; i++) {
                charCount[s1[i1 + i] - 'a']++;
                charCount[s2[i2 + i] - 'a']--;
            }
            for (int i = 0; i < 26; i++) {
                if (charCount[i] != 0) {
                    memo[i1][i2][len] = 0;
                    return false;
                }
            }
            
            // 尝试所有可能的分割位置
            for (int k = 1; k < len; k++) {
                // 不交换的情况
                if (dfs(i1, i2, k) && dfs(i1 + k, i2 + k, len - k)) {
                    memo[i1][i2][len] = 1;
                    return true;
                }
                // 交换的情况
                if (dfs(i1, i2 + len - k, k) && dfs(i1 + k, i2, len - k)) {
                    memo[i1][i2][len] = 1;
                    return true;
                }
            }
            
            memo[i1][i2][len] = 0;
            return false;
        };
        
        return dfs(0, 0, n);
    }
    
    /**
     * 动态规划解法
     * 
     * @param s1 字符串1
     * @param s2 字符串2
     * @return 是否是扰乱字符串
     */
    bool isScrambleDP(string s1, string s2) {
        int n = s1.length();
        if (n != s2.length()) return false;
        if (s1 == s2) return true;
        
        // dp[i][j][len] 表示s1从i开始，s2从j开始，长度为len的子串是否是扰乱字符串
        vector<vector<vector<bool>>> dp(n, 
            vector<vector<bool>>(n, vector<bool>(n + 1, false)));
        
        // 初始化：长度为1的子串
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dp[i][j][1] = (s1[i] == s2[j]);
            }
        }
        
        // 填充DP表
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                for (int j = 0; j <= n - len; j++) {
                    // 检查字符频率
                    vector<int> count(26, 0);
                    for (int k = 0; k < len; k++) {
                        count[s1[i + k] - 'a']++;
                        count[s2[j + k] - 'a']--;
                    }
                    bool valid = true;
                    for (int k = 0; k < 26; k++) {
                        if (count[k] != 0) {
                            valid = false;
                            break;
                        }
                    }
                    if (!valid) continue;
                    
                    // 尝试所有可能的分割位置
                    for (int k = 1; k < len; k++) {
                        // 不交换的情况
                        if (dp[i][j][k] && dp[i + k][j + k][len - k]) {
                            dp[i][j][len] = true;
                            break;
                        }
                        // 交换的情况
                        if (dp[i][j + len - k][k] && dp[i + k][j][len - k]) {
                            dp[i][j][len] = true;
                            break;
                        }
                    }
                }
            }
        }
        
        return dp[0][0][n];
    }
};

// 测试函数
int main() {
    Solution solution;
    
    // 测试用例1
    string s1_1 = "great", s2_1 = "rgeat";
    cout << "测试用例1:" << endl;
    cout << "s1 = \"" << s1_1 << "\", s2 = \"" << s2_1 << "\"" << endl;
    cout << "记忆化搜索结果: " << (solution.isScramble(s1_1, s2_1) ? "true" : "false") << endl;
    cout << "动态规划结果: " << (solution.isScrambleDP(s1_1, s2_1) ? "true" : "false") << endl;
    cout << endl;
    
    // 测试用例2
    string s1_2 = "abcde", s2_2 = "caebd";
    cout << "测试用例2:" << endl;
    cout << "s1 = \"" << s1_2 << "\", s2 = \"" << s2_2 << "\"" << endl;
    cout << "记忆化搜索结果: " << (solution.isScramble(s1_2, s2_2) ? "true" : "false") << endl;
    cout << "动态规划结果: " << (solution.isScrambleDP(s1_2, s2_2) ? "true" : "false") << endl;
    cout << endl;
    
    // 测试用例3
    string s1_3 = "a", s2_3 = "a";
    cout << "测试用例3:" << endl;
    cout << "s1 = \"" << s1_3 << "\", s2 = \"" << s2_3 << "\"" << endl;
    cout << "记忆化搜索结果: " << (solution.isScramble(s1_3, s2_3) ? "true" : "false") << endl;
    cout << "动态规划结果: " << (solution.isScrambleDP(s1_3, s2_3) ? "true" : "false") << endl;
    
    return 0;
}
#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <unordered_map>
using namespace std;

/*
 * 好串问题 - C++实现
 * 
 * 题目描述：
 * 可以用r、e、d三种字符拼接字符串，如果拼出来的字符串中
 * 有且仅有1个长度>=2的回文子串，那么这个字符串定义为"好串"
 * 返回长度为n的所有可能的字符串中，好串有多少个
 * 
 * 解题思路：
 * 这是一个组合数学问题，可以使用暴力递归、数学规律等方法解决
 * 1. 暴力递归：生成所有字符串并检查（仅适用于小数据）
 * 2. 数学规律：观察小数据找到规律公式
 * 3. 动态规划：状态设计复杂，适用于中等规模数据
 * 
 * 相关题目：
 * 1. LeetCode 5. Longest Palindromic Substring：https://leetcode.com/problems/longest-palindromic-substring/
 * 2. LeetCode 647. Palindromic Substrings：https://leetcode.com/problems/palindromic-substrings/
 * 3. LeetCode 131. Palindrome Partitioning：https://leetcode.com/problems/palindrome-partitioning/
 * 4. POJ 1159. Palindrome：http://poj.org/problem?id=1159
 * 5. Manacher算法：线性时间求最长回文子串
 * 
 * 工程化考量：
 * 1. 异常处理：处理边界条件
 * 2. 性能优化：使用数学规律O(1)解法
 * 3. 取模运算：防止整数溢出
 * 4. 可读性：清晰的变量命名和注释
 */

class RedPalindromeGoodStrings {
public:
    static const int MOD = 1000000007;

    // 方法1：暴力递归（仅适用于小数据）
    static int num1(int n) {
        if (n <= 0) return 0;
        string path(n, ' ');
        return f(path, 0);
    }

private:
    static int f(string& path, int i) {
        if (i == path.length()) {
            int cnt = 0;
            for (int l = 0; l < path.length(); l++) {
                for (int r = l + 1; r < path.length(); r++) {
                    if (isPalindrome(path, l, r)) {
                        cnt++;
                    }
                    if (cnt > 1) {
                        return 0;
                    }
                }
            }
            return cnt == 1 ? 1 : 0;
        } else {
            int ans = 0;
            path[i] = 'r';
            ans += f(path, i + 1);
            path[i] = 'e';
            ans += f(path, i + 1);
            path[i] = 'd';
            ans += f(path, i + 1);
            return ans;
        }
    }

    static bool isPalindrome(const string& s, int l, int r) {
        while (l < r) {
            if (s[l] != s[r]) {
                return false;
            }
            l++;
            r--;
        }
        return true;
    }

public:
    // 方法2：数学规律法（最优解）
    static int num2(int n) {
        if (n == 1) return 0;
        if (n == 2) return 3;
        if (n == 3) return 18;
        return (6LL * (n + 1)) % MOD;
    }

    // ==================== 扩展题目1: 最长回文子串 ====================
    /*
     * LeetCode 5. Longest Palindromic Substring
     * 题目：找到字符串中最长的回文子串
     * 网址：https://leetcode.com/problems/longest-palindromic-substring/
     * 
     * 中心扩展法：
     * 时间复杂度：O(n^2)
     * 空间复杂度：O(1)
     */
    static string longestPalindrome(const string& s) {
        if (s.empty()) return "";
        
        int start = 0, end = 0;
        
        for (int i = 0; i < s.length(); i++) {
            int len1 = expandAroundCenter(s, i, i);
            int len2 = expandAroundCenter(s, i, i + 1);
            int len = max(len1, len2);
            
            if (len > end - start) {
                start = i - (len - 1) / 2;
                end = i + len / 2;
            }
        }
        
        return s.substr(start, end - start + 1);
    }

private:
    static int expandAroundCenter(const string& s, int left, int right) {
        while (left >= 0 && right < s.length() && s[left] == s[right]) {
            left--;
            right++;
        }
        return right - left - 1;
    }

public:
    // ==================== 扩展题目2: 回文子串个数 ====================
    /*
     * LeetCode 647. Palindromic Substrings
     * 题目：计算字符串中回文子串的个数
     * 网址：https://leetcode.com/problems/palindromic-substrings/
     * 
     * 中心扩展法：
     * 时间复杂度：O(n^2)
     * 空间复杂度：O(1)
     */
    static int countSubstrings(const string& s) {
        if (s.empty()) return 0;
        
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            count += expandAndCount(s, i, i);
            count += expandAndCount(s, i, i + 1);
        }
        return count;
    }

private:
    static int expandAndCount(const string& s, int left, int right) {
        int count = 0;
        while (left >= 0 && right < s.length() && s[left] == s[right]) {
            count++;
            left--;
            right++;
        }
        return count;
    }

public:
    // ==================== 扩展题目3: 回文分割 ====================
    /*
     * LeetCode 131. Palindrome Partitioning
     * 题目：将字符串分割成回文子串，返回所有可能的分割方案
     * 网址：https://leetcode.com/problems/palindrome-partitioning/
     * 
     * 回溯+动态规划预处理：
     * 时间复杂度：O(n * 2^n)
     * 空间复杂度：O(n^2)
     */
    static vector<vector<string>> partition(const string& s) {
        vector<vector<string>> result;
        if (s.empty()) return result;
        
        vector<vector<bool>> isPalindrome = preprocess(s);
        vector<string> current;
        backtrack(s, 0, current, result, isPalindrome);
        return result;
    }

private:
    static vector<vector<bool>> preprocess(const string& s) {
        int n = s.length();
        vector<vector<bool>> dp(n, vector<bool>(n, false));
        
        for (int i = 0; i < n; i++) {
            dp[i][i] = true;
        }
        
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                if (len == 2) {
                    dp[i][j] = (s[i] == s[j]);
                } else {
                    dp[i][j] = (s[i] == s[j]) && dp[i + 1][j - 1];
                }
            }
        }
        return dp;
    }

    static void backtrack(const string& s, int start, vector<string>& current,
                         vector<vector<string>>& result, const vector<vector<bool>>& isPalindrome) {
        if (start == s.length()) {
            result.push_back(current);
            return;
        }
        
        for (int end = start; end < s.length(); end++) {
            if (isPalindrome[start][end]) {
                current.push_back(s.substr(start, end - start + 1));
                backtrack(s, end + 1, current, result, isPalindrome);
                current.pop_back();
            }
        }
    }

public:
    // ==================== 扩展题目4: 回文插入 ====================
    /*
     * POJ 1159. Palindrome
     * 题目：计算最少插入多少个字符能使字符串变成回文串
     * 网址：http://poj.org/problem?id=1159
     * 
     * 动态规划解法：
     * 时间复杂度：O(n^2)
     * 空间复杂度：O(n^2)
     */
    static int minInsertions(const string& s) {
        if (s.length() <= 1) return 0;
        
        int n = s.length();
        vector<vector<int>> dp(n, vector<int>(n, 0));
        
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                if (s[i] == s[j]) {
                    dp[i][j] = dp[i + 1][j - 1];
                } else {
                    dp[i][j] = min(dp[i + 1][j], dp[i][j - 1]) + 1;
                }
            }
        }
        
        return dp[0][n - 1];
    }

    // ==================== 扩展题目5: Manacher算法 ====================
    /*
     * Manacher算法：线性时间求最长回文子串
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    static string longestPalindromeManacher(const string& s) {
        if (s.empty()) return "";
        if (s.length() == 1) return s;
        
        string T = "#";
        for (char c : s) {
            T += c;
            T += '#';
        }
        
        int n = T.length();
        vector<int> p(n, 0);
        int C = 0, R = 0;
        int maxLen = 0, centerIndex = 0;
        
        for (int i = 0; i < n; i++) {
            int mirror = 2 * C - i;
            if (i < R) {
                p[i] = min(R - i, p[mirror]);
            }
            
            while (i - p[i] - 1 >= 0 && i + p[i] + 1 < n && 
                   T[i - p[i] - 1] == T[i + p[i] + 1]) {
                p[i]++;
            }
            
            if (i + p[i] > R) {
                C = i;
                R = i + p[i];
            }
            
            if (p[i] > maxLen) {
                maxLen = p[i];
                centerIndex = i;
            }
        }
        
        int start = (centerIndex - maxLen) / 2;
        return s.substr(start, maxLen);
    }
};

// 测试函数
int main() {
    cout << "=== 好串问题测试 ===" << endl;
    for (int i = 1; i <= 10; i++) {
        int result1 = 0;
        if (i <= 5) { // 只对小数据使用暴力方法
            result1 = RedPalindromeGoodStrings::num1(i);
        }
        int result2 = RedPalindromeGoodStrings::num2(i);
        cout << "n=" << i << ": " << result1 << " / " << result2 << endl;
    }
    
    cout << "\n=== 扩展题目测试 ===" << endl;
    
    // 测试最长回文子串
    cout << "Longest Palindrome (\"babad\"): " 
         << RedPalindromeGoodStrings::longestPalindrome("babad") << endl;
    
    // 测试回文子串个数
    cout << "Count Substrings (\"abc\"): " 
         << RedPalindromeGoodStrings::countSubstrings("abc") << endl;
    
    // 测试回文分割
    auto partitions = RedPalindromeGoodStrings::partition("aab");
    cout << "Palindrome Partitioning (\"aab\"): " << partitions.size() << " partitions" << endl;
    
    // 测试回文插入
    cout << "Min Insertions (\"abca\"): " 
         << RedPalindromeGoodStrings::minInsertions("abca") << endl;
    
    // 测试Manacher算法
    cout << "Longest Palindrome Manacher (\"cbbd\"): " 
         << RedPalindromeGoodStrings::longestPalindromeManacher("cbbd") << endl;
    
    return 0;
}
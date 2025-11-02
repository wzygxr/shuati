/**
 * LeetCode 214. 最短回文串
 * 
 * 题目描述:
 * 给定一个字符串 s，你可以通过在字符串前面添加字符将其转换为回文串。找到并返回可以用这种方式转换的最短回文串
 * 
 * 输入格式:
 * 字符串 s
 * 
 * 输出格式:
 * 最短回文串
 * 
 * 数据范围:
 * 0 <= s.length <= 5 * 10^4
 * s 仅由小写英文字母组成
 * 
 * 题目链接: https://leetcode.cn/problems/shortest-palindrome/
 * 
 * 解题思路:
 * 使用Manacher算法找到以字符串开头开始的最长回文前缀，然后在前面添加剩余部分的逆序
 * 
 * 算法步骤:
 * 1. 使用Manacher算法预处理字符串
 * 2. 找到以字符串开头开始的最长回文前缀
 * 3. 将剩余部分逆序后添加到字符串前面
 * 4. 返回结果
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 */

#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
using namespace std;

/**
 * 使用Manacher算法找到最长回文前缀
 * 
 * @param s 输入字符串
 * @return 最长回文前缀的长度
 */
int find_longest_palindrome_prefix(const string& s) {
    if (s.empty()) return 0;
    
    // 预处理字符串
    string processed = "#";
    for (char c : s) {
        processed += c;
        processed += '#';
    }
    
    int n = processed.size();
    vector<int> p(n, 0);
    int center = 0, right = 0;
    int max_prefix = 0;
    
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
        
        // 检查是否是以开头开始的最长回文前缀
        if (i - p[i] + 1 == 0) { // 回文串延伸到字符串开头
            max_prefix = max(max_prefix, p[i]);
        }
    }
    
    return max_prefix;
}

/**
 * 构造最短回文串
 * 
 * @param s 输入字符串
 * @return 最短回文串
 */
string shortestPalindrome(string s) {
    if (s.empty()) return "";
    
    // 找到最长回文前缀的长度
    int prefix_len = find_longest_palindrome_prefix(s);
    
    // 如果整个字符串已经是回文，直接返回
    if (prefix_len == s.length()) {
        return s;
    }
    
    // 获取需要添加到前面的部分（剩余部分的逆序）
    string remaining = s.substr(prefix_len);
    reverse(remaining.begin(), remaining.end());
    
    return remaining + s;
}

/**
 * 优化版本：使用KMP算法的思想
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 */
string shortestPalindrome_kmp(string s) {
    if (s.empty()) return "";
    
    string rev = s;
    reverse(rev.begin(), rev.end());
    string combined = s + "#" + rev;
    
    // 计算KMP的next数组
    int n = combined.size();
    vector<int> next(n, 0);
    
    for (int i = 1, j = 0; i < n; i++) {
        while (j > 0 && combined[i] != combined[j]) {
            j = next[j - 1];
        }
        if (combined[i] == combined[j]) {
            j++;
        }
        next[i] = j;
    }
    
    // 最长回文前缀的长度
    int prefix_len = next[n - 1];
    
    // 如果整个字符串已经是回文，直接返回
    if (prefix_len == s.length()) {
        return s;
    }
    
    // 获取需要添加到前面的部分
    string remaining = s.substr(prefix_len);
    reverse(remaining.begin(), remaining.end());
    
    return remaining + s;
}

/**
 * 测试用例和验证
 */
int main() {
    // 测试用例1
    string s1 = "aacecaaa";
    cout << "输入: \"" << s1 << "\", 输出: \"" << shortestPalindrome(s1) << "\" (期望: \"aaacecaaa\")" << endl;
    cout << "KMP版本: \"" << shortestPalindrome_kmp(s1) << "\" (期望: \"aaacecaaa\")" << endl;
    
    // 测试用例2
    string s2 = "abcd";
    cout << "输入: \"" << s2 << "\", 输出: \"" << shortestPalindrome(s2) << "\" (期望: \"dcbabcd\")" << endl;
    cout << "KMP版本: \"" << shortestPalindrome_kmp(s2) << "\" (期望: \"dcbabcd\")" << endl;
    
    // 测试用例3
    string s3 = "a";
    cout << "输入: \"" << s3 << "\", 输出: \"" << shortestPalindrome(s3) << "\" (期望: \"a\")" << endl;
    cout << "KMP版本: \"" << shortestPalindrome_kmp(s3) << "\" (期望: \"a\")" << endl;
    
    // 测试用例4
    string s4 = "";
    cout << "输入: \"" << s4 << "\", 输出: \"" << shortestPalindrome(s4) << "\" (期望: \"\")" << endl;
    cout << "KMP版本: \"" << shortestPalindrome_kmp(s4) << "\" (期望: \"\")" << endl;
    
    // 测试用例5
    string s5 = "aba";
    cout << "输入: \"" << s5 << "\", 输出: \"" << shortestPalindrome(s5) << "\" (期望: \"aba\")" << endl;
    cout << "KMP版本: \"" << shortestPalindrome_kmp(s5) << "\" (期望: \"aba\")" << endl;
    
    return 0;
}

/**
 * 算法分析:
 * 
 * 1. Manacher方法:
 *    - 时间复杂度: O(n) - 预处理和查找都是线性时间
 *    - 空间复杂度: O(n) - 存储预处理字符串和回文半径数组
 *    - 优势: 算法思路清晰，易于理解
 *    - 劣势: 需要额外的预处理步骤
 * 
 * 2. KMP方法:
 *    - 时间复杂度: O(n) - 计算next数组是线性时间
 *    - 空间复杂度: O(n) - 存储next数组和反转字符串
 *    - 优势: 算法效率高，代码简洁
 *    - 劣势: 需要理解KMP算法的原理
 * 
 * 3. 性能比较:
 *    - 两种方法都是线性时间复杂度
 *    - KMP方法通常在实际应用中更快
 *    - Manacher方法在理解回文结构方面更有优势
 * 
 * 4. 适用场景:
 *    - 对于需要深入理解回文结构的问题，推荐使用Manacher方法
 *    - 对于追求代码简洁和效率的问题，推荐使用KMP方法
 */
#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
using namespace std;

// KMP算法及其应用题目集合（C++版本）
// 包含基本KMP实现和多个LeetCode题目解析

/**
 * 构建next数组（部分匹配表）
 * next[i]表示模式串中以i-1位置字符结尾的子串，其前缀和后缀匹配的最大长度
 * 
 * @param s 模式串
 * @param m 模式串长度
 * @return next数组，长度为m+1
 */
vector<int> nextArray(const string& s, int m) {
    if (m == 1) {
        return {-1};
    }
    vector<int> next(m + 1);  // 修改为m+1长度
    next[0] = -1;
    next[1] = 0;
    int i = 2, cn = 0; // i表示当前要求next值的位置，cn表示当前要和前一个字符比对的下标
    
    while (i <= m) {  // 修改为i <= m
        if (s[i - 1] == s[cn]) {
            next[i++] = ++cn;
        } else if (cn > 0) {
            cn = next[cn];
        } else {
            next[i++] = 0;
        }
    }
    return next;
}

/**
 * KMP算法核心实现
 * 
 * @param s1 文本串
 * @param s2 模式串
 * @return 模式串在文本串中首次出现的索引，如果不存在则返回-1
 */
int kmp(const string& s1, const string& s2) {
    int n = s1.size(), m = s2.size(), x = 0, y = 0;
    vector<int> next = nextArray(s2, m); // O(m) - 构建next数组
    
    while (x < n && y < m) { // O(n) - 匹配过程
        if (s1[x] == s2[y]) {
            x++;
            y++;
        } else if (y == 0) {
            x++;
        } else {
            y = next[y];
        }
    }
    return y == m ? x - y : -1;
}

/**
 * LeetCode 28: strStr()
 * 实现 strStr() 函数
 * 给你两个字符串 haystack 和 needle，请你在 haystack 字符串中找出 needle 字符串的第一个匹配项的下标（下标从 0 开始）
 * 如果 needle 不是 haystack 的一部分，则返回 -1
 * 
 * 时间复杂度：O(n + m)，其中n是文本串长度，m是模式串长度
 * 空间复杂度：O(m)，用于存储next数组
 * 
 * @param haystack 文本串
 * @param needle 模式串
 * @return 模式串在文本串中首次出现的索引，如果不存在则返回-1
 */
int strStr(const string& haystack, const string& needle) {
    // 边界条件检查
    if (needle.empty()) {
        return 0;
    }
    if (haystack.empty()) {
        return -1;
    }
    return kmp(haystack, needle);
}

/**
 * LeetCode 459: 重复的子字符串
 * 给定一个非空的字符串，检查它是否可以通过由它的一个子串重复多次构成
 * 
 * 算法思路：
 * 1. 假设字符串s由子串p重复k次构成，那么s+p的中间部分必然包含s
 * 2. 使用KMP算法检查s是否是(s+s).substr(1, 2*s.length()-2)的子串
 * 3. 如果是，则s由重复子串构成
 * 
 * 时间复杂度：O(n)，其中n是字符串长度
 * 空间复杂度：O(n)，用于存储next数组
 * 
 * @param s 输入字符串
 * @return 是否由重复子串构成
 */
bool repeatedSubstringPattern(const string& s) {
    // 边界条件检查
    if (s.size() < 2) {
        return false;
    }
    
    // 将字符串拼接，去掉首尾字符，然后检查原字符串是否是子串
    string doubled = s + s;
    string target = doubled.substr(1, doubled.size() - 2);
    return kmp(target, s) != -1;
}

/**
 * LeetCode 1392: 最长快乐前缀
 * 编写一个算法来查找字符串s的最长的快乐前缀，快乐前缀是既是前缀又是后缀的字符串，但不能是整个字符串本身
 * 
 * 算法思路：
 * 1. 利用KMP算法的next数组特性，next[n]表示前n个字符的前缀和后缀的最大匹配长度
 * 2. 计算整个字符串的next数组，next[s.length()]就是最长快乐前缀的长度
 * 
 * 时间复杂度：O(n)，其中n是字符串长度
 * 空间复杂度：O(n)，用于存储next数组
 * 
 * @param s 输入字符串
 * @return 最长快乐前缀
 */
string longestPrefix(const string& s) {
    if (s.size() < 2) {
        return "";
    }
    
    vector<int> next = nextArray(s, s.size());
    int maxLen = next[s.size()];
    
    return s.substr(0, maxLen);
}

/**
 * LeetCode 214: 最短回文串
 * 给定一个字符串s，你可以通过在字符串前面添加字符将其转换为回文串，请找出并返回可以用这种方式转换的最短回文串
 * 
 * 算法思路：
 * 1. 问题等价于找到字符串s的最长前缀，使其也是s的回文前缀
 * 2. 使用KMP算法，将s的反转字符串添加到s后面（中间用特殊字符分隔），然后计算next数组
 * 3. next[new_s.length()]即为最长回文前缀的长度
 * 
 * 时间复杂度：O(n)，其中n是字符串长度
 * 空间复杂度：O(n)
 * 
 * @param s 输入字符串
 * @return 最短回文串
 */
string shortestPalindrome(const string& s) {
    if (s.size() <= 1) {
        return s;
    }
    
    string reversed = s;
    reverse(reversed.begin(), reversed.end());
    string combined = s + "#" + reversed;
    
    vector<int> next = nextArray(combined, combined.size());
    int maxPrefixLen = next[combined.size()];
    
    return reversed.substr(0, reversed.size() - maxPrefixLen) + s;
}

/**
 * LeetCode 796: 旋转字符串
 * 给定两个字符串, s和goal，如果s在若干次旋转操作之后，能变成goal，那么返回true
 * 旋转操作指的是将s最左边的字符移动到最右边
 * 
 * 算法思路：
 * 1. 如果两个字符串长度不同，直接返回false
 * 2. 将s与自身拼接，如果goal是s+s的子串，则说明s可以通过旋转得到goal
 * 3. 使用KMP算法检查子串
 * 
 * 时间复杂度：O(n)，其中n是字符串长度
 * 空间复杂度：O(n)
 * 
 * @param s 原始字符串
 * @param goal 目标字符串
 * @return 是否可以通过旋转得到
 */
bool rotateString(const string& s, const string& goal) {
    if (s.size() != goal.size()) {
        return false;
    }
    if (s.empty()) {
        return true;
    }
    
    string doubled = s + s;
    return kmp(doubled, goal) != -1;
}

// 测试函数
void testStrStr() {
    cout << "测试LeetCode 28: strStr()" << endl;
    
    // 测试用例1: 基本匹配
    string haystack1 = "hello";
    string needle1 = "ll";
    int result1 = strStr(haystack1, needle1);
    cout << "文本串: " << haystack1 << endl;
    cout << "模式串: " << needle1 << endl;
    cout << "结果: " << result1 << " (期望: 2)" << endl;
    
    // 测试用例2: 不匹配
    string haystack2 = "aaaaa";
    string needle2 = "bba";
    int result2 = strStr(haystack2, needle2);
    cout << "文本串: " << haystack2 << endl;
    cout << "模式串: " << needle2 << endl;
    cout << "结果: " << result2 << " (期望: -1)" << endl;
    
    // 测试用例3: 模式串为空
    string haystack3 = "abc";
    string needle3 = "";
    int result3 = strStr(haystack3, needle3);
    cout << "文本串: " << haystack3 << endl;
    cout << "模式串: " << needle3 << endl;
    cout << "结果: " << result3 << " (期望: 0)" << endl;
}

void testRepeatedSubstringPattern() {
    cout << "测试LeetCode 459: 重复的子字符串" << endl;
    
    // 测试用例1: "abab" -> true ("ab"重复两次)
    cout << "字符串: \"abab\" 结果: " << (repeatedSubstringPattern("abab") ? "true" : "false") << " (期望: true)" << endl;
    
    // 测试用例2: "aba" -> false
    cout << "字符串: \"aba\" 结果: " << (repeatedSubstringPattern("aba") ? "true" : "false") << " (期望: false)" << endl;
    
    // 测试用例3: "abcabcabcabc" -> true ("abc"重复四次)
    cout << "字符串: \"abcabcabcabc\" 结果: " << (repeatedSubstringPattern("abcabcabcabc") ? "true" : "false") << " (期望: true)" << endl;
}

void testLongestPrefix() {
    cout << "测试LeetCode 1392: 最长快乐前缀" << endl;
    
    // 测试用例1: "level" -> "l"
    cout << "字符串: \"level\" 结果: \"" << longestPrefix("level") << "\" (期望: l)" << endl;
    
    // 测试用例2: "ababab" -> "abab"
    cout << "字符串: \"ababab\" 结果: \"" << longestPrefix("ababab") << "\" (期望: abab)" << endl;
    
    // 测试用例3: "leetcodeleet" -> "leet"
    cout << "字符串: \"leetcodeleet\" 结果: \"" << longestPrefix("leetcodeleet") << "\" (期望: leet)" << endl;
}

void testShortestPalindrome() {
    cout << "测试LeetCode 214: 最短回文串" << endl;
    
    // 测试用例1: "aacecaaa" -> "aaacecaaa"
    cout << "字符串: \"aacecaaa\" 结果: \"" << shortestPalindrome("aacecaaa") << "\" (期望: aaacecaaa)" << endl;
    
    // 测试用例2: "abcd" -> "dcbabcd"
    cout << "字符串: \"abcd\" 结果: \"" << shortestPalindrome("abcd") << "\" (期望: dcbabcd)" << endl;
    
    // 测试用例3: "a" -> "a"
    cout << "字符串: \"a\" 结果: \"" << shortestPalindrome("a") << "\" (期望: a)" << endl;
}

void testRotateString() {
    cout << "测试LeetCode 796: 旋转字符串" << endl;
    
    // 测试用例1: s="abcde", goal="cdeab" -> true
    cout << "s: \"abcde\", goal: \"cdeab\" 结果: " << (rotateString("abcde", "cdeab") ? "true" : "false") << " (期望: true)" << endl;
    
    // 测试用例2: s="abcde", goal="abced" -> false
    cout << "s: \"abcde\", goal: \"abced\" 结果: " << (rotateString("abcde", "abced") ? "true" : "false") << " (期望: false)" << endl;
    
    // 测试用例3: s="", goal="" -> true
    cout << "s: \"\", goal: \"\" 结果: " << (rotateString("", "") ? "true" : "false") << " (期望: true)" << endl;
}

// 主函数
int main() {
    // 测试LeetCode 28: strStr()
    testStrStr();
    cout << "===============" << endl;
    
    // 测试LeetCode 459: 重复的子字符串
    testRepeatedSubstringPattern();
    cout << "===============" << endl;
    
    // 测试LeetCode 1392: 最长快乐前缀
    testLongestPrefix();
    cout << "===============" << endl;
    
    // 测试LeetCode 214: 最短回文串
    testShortestPalindrome();
    cout << "===============" << endl;
    
    // 测试LeetCode 796: 旋转字符串
    testRotateString();
    
    return 0;
}
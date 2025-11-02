/**
 * KMP算法扩展题目集合 - C++版本
 * 
 * 本文件包含来自多个算法平台的KMP算法相关题目，包括：
 * - LeetCode
 * - HackerRank  
 * - Codeforces
 * - 洛谷
 * - 牛客网
 * - SPOJ
 * - USACO
 * - AtCoder
 * 
 * 每个题目都包含：
 * 1. 题目描述和来源链接
 * 2. 完整的KMP算法实现
 * 3. 详细的时间复杂度和空间复杂度分析
 * 4. 完整的测试用例
 * 5. 工程化考量（异常处理、边界条件等）
 * 
 * @author Algorithm Journey
 * @version 1.0
 * @since 2024-01-01
 */

#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <cstring>
#include <chrono>

using namespace std;

// 函数声明
vector<int> buildNextArray(const string& pattern);

/**
 * HackerRank: Knuth-Morris-Pratt Algorithm
 * 题目链接: https://www.hackerrank.com/challenges/kmp-fp/problem
 * 
 * 题目描述: 实现KMP算法，查找模式串在文本串中的所有出现位置
 * 
 * 算法思路:
 * 1. 使用KMP算法进行字符串匹配
 * 2. 记录所有匹配的起始位置
 * 3. 返回所有匹配位置的列表
 * 
 * 时间复杂度: O(n + m)，其中n是文本串长度，m是模式串长度
 * 空间复杂度: O(m)，用于存储next数组
 * 
 * @param text 文本串
 * @param pattern 模式串
 * @return 所有匹配位置的列表
 */
vector<int> kmpAllMatches(const string& text, const string& pattern) {
    vector<int> result;
    
    // 边界条件处理
    if (text.empty() || pattern.empty()) {
        return result;
    }
    
    int n = text.size(), m = pattern.size();
    if (m > n) {
        return result;
    }
    
    // 构建next数组
    vector<int> next = buildNextArray(pattern);
    
    int i = 0, j = 0;
    while (i < n) {
        if (text[i] == pattern[j]) {
            i++;
            j++;
        } else if (j == 0) {
            i++;
        } else {
            j = next[j];
        }
        
        // 找到完整匹配
        if (j == m) {
            result.push_back(i - j);
            j = next[j]; // 继续寻找重叠匹配
        }
    }
    
    return result;
}

/**
 * Codeforces 126B: Password
 * 题目链接: https://codeforces.com/contest/126/problem/B
 * 
 * 题目描述: 给定一个字符串s，找出一个最长的子串，该子串同时作为前缀、后缀和中间子串出现
 * 
 * 算法思路:
 * 1. 计算整个字符串的next数组
 * 2. 找到最大的k，使得s[0...k-1]既是前缀又是后缀
 * 3. 检查这个前缀是否在字符串中间出现
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 * 
 * @param s 输入字符串
 * @return 满足条件的最长子串，如果不存在返回空字符串
 */
string findPassword(const string& s) {
    if (s.size() < 3) {
        return "";
    }
    
    int n = s.size();
    vector<int> next = buildNextArray(s);
    
    // 找到最长的既是前缀又是后缀的子串
    int maxLen = next[n];
    
    // 检查这个前缀是否在中间出现
    bool foundInMiddle = false;
    for (int i = 1; i < n - 1; i++) {
        if (next[i] == maxLen) {
            foundInMiddle = true;
            break;
        }
    }
    
    if (maxLen > 0 && foundInMiddle) {
        return s.substr(0, maxLen);
    }
    
    // 如果最长的不行，尝试次长的
    int candidate = next[maxLen];
    if (candidate > 0) {
        for (int i = 1; i < n - 1; i++) {
            if (next[i] == candidate) {
                return s.substr(0, candidate);
            }
        }
    }
    
    return "";
}

/**
 * 洛谷 P3375: 【模板】KMP
 * 题目链接: https://www.luogu.com.cn/problem/P3375
 * 
 * 题目描述: KMP算法模板题，输出模式串在文本串中的所有出现位置
 * 
 * 算法思路:
 * 1. 标准的KMP算法实现
 * 2. 输出所有匹配位置（从1开始计数）
 * 
 * 时间复杂度: O(n + m)
 * 空间复杂度: O(m)
 * 
 * @param text 文本串
 * @param pattern 模式串
 * @return 所有匹配位置（从1开始）
 */
vector<int> luoguKMP(const string& text, const string& pattern) {
    vector<int> result;
    
    if (text.empty() || pattern.empty()) {
        return result;
    }
    
    int n = text.size(), m = pattern.size();
    if (m > n) {
        return result;
    }
    
    vector<int> next = buildNextArray(pattern);
    int i = 0, j = 0;
    
    while (i < n) {
        if (text[i] == pattern[j]) {
            i++;
            j++;
        } else if (j == 0) {
            i++;
        } else {
            j = next[j];
        }
        
        if (j == m) {
            result.push_back(i - j + 1); // 从1开始计数
            j = next[j];
        }
    }
    
    return result;
}

/**
 * SPOJ: NAJPF - Pattern Find
 * 题目链接: https://www.spoj.com/problems/NAJPF/
 * 
 * 题目描述: 查找模式串在文本串中的所有出现位置
 * 
 * 算法思路: 标准KMP算法
 * 
 * 时间复杂度: O(n + m)
 * 空间复杂度: O(m)
 * 
 * @param text 文本串
 * @param pattern 模式串
 * @return 所有匹配位置（从0开始）
 */
vector<int> spojPatternFind(const string& text, const string& pattern) {
    return kmpAllMatches(text, pattern);
}

/**
 * 牛客网: 字符串匹配
 * 题目链接: 牛客网相关题目
 * 
 * 题目描述: 实现字符串匹配功能
 * 
 * 算法思路: 标准KMP算法
 * 
 * 时间复杂度: O(n + m)
 * 空间复杂度: O(m)
 * 
 * @param text 文本串
 * @param pattern 模式串
 * @return 第一个匹配位置，如果没有返回-1
 */
int nowcoderStrStr(const string& text, const string& pattern) {
    if (text.empty() || pattern.empty()) {
        return -1;
    }
    
    if (pattern.size() == 0) {
        return 0;
    }
    
    int n = text.size(), m = pattern.size();
    if (m > n) {
        return -1;
    }
    
    vector<int> next = buildNextArray(pattern);
    int i = 0, j = 0;
    
    while (i < n && j < m) {
        if (text[i] == pattern[j]) {
            i++;
            j++;
        } else if (j == 0) {
            i++;
        } else {
            j = next[j];
        }
    }
    
    return j == m ? i - j : -1;
}

/**
 * USACO: String Transformation
 * 题目描述: 字符串变换相关题目
 * 
 * 算法思路: 使用KMP算法进行模式匹配
 * 
 * 时间复杂度: O(n + m)
 * 空间复杂度: O(m)
 * 
 * @param text 文本串
 * @param pattern 模式串
 * @return 是否匹配
 */
bool usacoStringMatch(const string& text, const string& pattern) {
    return nowcoderStrStr(text, pattern) != -1;
}

/**
 * AtCoder: String Algorithms
 * 题目描述: 字符串算法相关题目
 * 
 * 算法思路: 使用KMP算法进行高效匹配
 * 
 * 时间复杂度: O(n + m)
 * 空间复杂度: O(m)
 * 
 * @param text 文本串
 * @param pattern 模式串
 * @return 匹配次数
 */
int atCoderKMP(const string& text, const string& pattern) {
    vector<int> matches = kmpAllMatches(text, pattern);
    return matches.size();
}

// 函数声明
vector<int> buildNextArray(const string& pattern);

/**
 * 构建next数组的通用方法
 * 
 * 算法思路:
 * 1. 初始化next数组
 * 2. 使用双指针技术构建next数组
 * 3. 处理边界情况
 * 
 * 时间复杂度: O(m)
 * 空间复杂度: O(m)
 * 
 * @param pattern 模式串
 * @return next数组
 */
vector<int> buildNextArray(const string& pattern) {
    int m = pattern.size();
    vector<int> next(m + 1, 0);
    
    if (m == 0) {
        return next;
    }
    
    next[0] = -1;
    if (m == 1) {
        return next;
    }
    
    next[1] = 0;
    int i = 2, cn = 0;
    
    while (i <= m) {
        if (pattern[i - 1] == pattern[cn]) {
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
 * 测试HackerRank题目
 */
void testHackerRankKMP() {
    cout << "=== HackerRank: Knuth-Morris-Pratt Algorithm ===" << endl;
    
    string text = "ABABDABACDABABCABAB";
    string pattern = "ABABCABAB";
    vector<int> result = kmpAllMatches(text, pattern);
    
    cout << "文本串: " << text << endl;
    cout << "模式串: " << pattern << endl;
    cout << "匹配位置: ";
    for (int pos : result) {
        cout << pos << " ";
    }
    cout << endl;
    cout << "期望: [10]" << endl;
    cout << endl;
}

/**
 * 测试Codeforces题目
 */
void testCodeforcesPassword() {
    cout << "=== Codeforces 126B: Password ===" << endl;
    
    vector<string> testCases = {
        "fixprefixsuffix", // 期望: "fix"
        "abcdabc",         // 期望: ""
        "abcabcabc",       // 期望: "abcabc"
        "aaa"              // 期望: "a"
    };
    
    for (const string& testCase : testCases) {
        string result = findPassword(testCase);
        cout << "输入: " << testCase << endl;
        cout << "输出: " << result << endl;
        cout << endl;
    }
}

/**
 * 测试洛谷题目
 */
void testLuoguKMP() {
    cout << "=== 洛谷 P3375: 【模板】KMP ===" << endl;
    
    string text = "ABABABABCABAABABABAB";
    string pattern = "ABABAB";
    vector<int> result = luoguKMP(text, pattern);
    
    cout << "文本串: " << text << endl;
    cout << "模式串: " << pattern << endl;
    cout << "匹配位置(从1开始): ";
    for (int pos : result) {
        cout << pos << " ";
    }
    cout << endl;
    cout << "期望: [1, 3, 5, 13, 15]" << endl;
    cout << endl;
}

/**
 * 测试SPOJ题目
 */
void testSPOJPatternFind() {
    cout << "=== SPOJ: NAJPF - Pattern Find ===" << endl;
    
    string text = "AAAAA";
    string pattern = "AA";
    vector<int> result = spojPatternFind(text, pattern);
    
    cout << "文本串: " << text << endl;
    cout << "模式串: " << pattern << endl;
    cout << "匹配位置: ";
    for (int pos : result) {
        cout << pos << " ";
    }
    cout << endl;
    cout << "期望: [0, 1, 2, 3]" << endl;
    cout << endl;
}

/**
 * 测试牛客网题目
 */
void testNowcoderStrStr() {
    cout << "=== 牛客网: 字符串匹配 ===" << endl;
    
    string text = "hello world";
    string pattern = "world";
    int result = nowcoderStrStr(text, pattern);
    
    cout << "文本串: " << text << endl;
    cout << "模式串: " << pattern << endl;
    cout << "匹配位置: " << result << endl;
    cout << "期望: 6" << endl;
    cout << endl;
}

/**
 * 测试USACO题目
 */
void testUSACOStringMatch() {
    cout << "=== USACO: String Transformation ===" << endl;
    
    string text = "transformation";
    string pattern = "form";
    bool result = usacoStringMatch(text, pattern);
    
    cout << "文本串: " << text << endl;
    cout << "模式串: " << pattern << endl;
    cout << "是否匹配: " << (result ? "true" : "false") << endl;
    cout << "期望: true" << endl;
    cout << endl;
}

/**
 * 测试AtCoder题目
 */
void testAtCoderKMP() {
    cout << "=== AtCoder: String Algorithms ===" << endl;
    
    string text = "abcabcabc";
    string pattern = "abc";
    int result = atCoderKMP(text, pattern);
    
    cout << "文本串: " << text << endl;
    cout << "模式串: " << pattern << endl;
    cout << "匹配次数: " << result << endl;
    cout << "期望: 3" << endl;
    cout << endl;
}

/**
 * 工程化考量: 性能测试
 */
void performanceTest() {
    cout << "=== 性能测试 ===" << endl;
    
    // 生成大规模测试数据
    string largeText;
    for (int i = 0; i < 100000; i++) {
        largeText += "ABCDEFG";
    }
    string pattern = "DEF";
    
    auto startTime = chrono::high_resolution_clock::now();
    int count = atCoderKMP(largeText, pattern);
    auto endTime = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::milliseconds>(endTime - startTime);
    
    cout << "文本长度: " << largeText.size() << endl;
    cout << "模式串长度: " << pattern.size() << endl;
    cout << "匹配次数: " << count << endl;
    cout << "执行时间: " << duration.count() << " ms" << endl;
    cout << endl;
}

/**
 * 主测试方法
 */
int main() {
    cout << "KMP算法扩展题目测试集" << endl << endl;
    
    // 运行所有测试
    testHackerRankKMP();
    testCodeforcesPassword();
    testLuoguKMP();
    testSPOJPatternFind();
    testNowcoderStrStr();
    testUSACOStringMatch();
    testAtCoderKMP();
    
    // 工程化测试
    performanceTest();
    
    cout << "所有测试完成!" << endl;
    return 0;
}
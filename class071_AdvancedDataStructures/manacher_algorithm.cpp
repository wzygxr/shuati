/**
 * Manacher算法实现
 * 用于在O(n)时间复杂度内查找字符串中的最长回文子串
 * 核心思想：利用已知回文子串的信息，避免重复计算
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 */

#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <set>
#include <stdexcept>
using namespace std;

class ManacherAlgorithm {
private:
    /**
     * 预处理字符串，在每个字符之间插入特殊字符（如'#'）
     * 这样可以统一处理奇数长度和偶数长度的回文子串
     * 
     * @param s 原始字符串
     * @return 预处理后的字符串
     */
    static string preprocess(const string& s) {
        if (s.empty()) {
            return "^$";
        }
        
        string result = "^";
        for (char c : s) {
            result += '#';
            result += c;
        }
        result += "#$";
        return result;
    }

public:
    /**
     * 使用Manacher算法查找最长回文子串
     * 
     * @param s 输入字符串
     * @return 最长回文子串
     */
    static string findLongestPalindromicSubstring(const string& s) {
        if (s.empty()) {
            return s; // 空字符串的最长回文子串就是自身
        }
        
        // 预处理字符串
        string T = preprocess(s);
        int n = T.length();
        
        // P[i]表示以T[i]为中心的最长回文子串的半径（不包括中心）
        vector<int> P(n, 0);
        
        // C是当前回文子串的中心，R是当前回文子串的右边界
        int C = 0, R = 0;
        
        // 最大回文子串的中心索引和半径
        int maxLen = 0, centerIndex = 0;
        
        // 遍历预处理后的字符串，跳过^和$
        for (int i = 1; i < n - 1; i++) {
            // 计算i关于C的对称点
            int iMirror = 2 * C - i; // C - (i - C)
            
            // 利用回文的对称性初始化P[i]
            // 如果i在R的范围内，可以利用对称点的信息
            // 否则初始化为0
            P[i] = (R > i) ? min(R - i, P[iMirror]) : 0;
            
            // 尝试扩展回文子串
            // 注意这里是直接比较字符，而不是像暴力方法那样每次都检查边界
            try {
                while (T.at(i + 1 + P[i]) == T.at(i - 1 - P[i])) {
                    P[i]++;
                }
            } catch (const out_of_range& e) {
                // 边界情况处理
            }
            
            // 如果扩展后的回文子串的右边界超过R，则更新C和R
            if (i + P[i] > R) {
                C = i;
                R = i + P[i];
            }
            
            // 更新最长回文子串的信息
            if (P[i] > maxLen) {
                maxLen = P[i];
                centerIndex = i;
            }
        }
        
        // 计算原始字符串中最长回文子串的起始和结束位置
        // 注意预处理字符串中的索引转换
        int start = (centerIndex - maxLen) / 2; // 转换为原始字符串的索引
        return s.substr(start, maxLen);
    }
    
    /**
     * 计算字符串中回文子串的数量（包括单个字符）
     * 
     * @param s 输入字符串
     * @return 回文子串的数量
     */
    static int countPalindromicSubstrings(const string& s) {
        if (s.empty()) {
            return 0;
        }
        
        // 预处理字符串
        string T = preprocess(s);
        int n = T.length();
        vector<int> P(n, 0);
        int C = 0, R = 0;
        int count = 0;
        
        for (int i = 1; i < n - 1; i++) {
            int iMirror = 2 * C - i;
            P[i] = (R > i) ? min(R - i, P[iMirror]) : 0;
            
            try {
                while (T.at(i + 1 + P[i]) == T.at(i - 1 - P[i])) {
                    P[i]++;
                }
            } catch (const out_of_range& e) {
                // 边界情况处理
            }
            
            if (i + P[i] > R) {
                C = i;
                R = i + P[i];
            }
            
            // P[i]表示半径，每个半径对应一个回文子串
            // 注意：这里需要除以2因为预处理字符串中的'#'不代表实际字符
            count += (P[i] + 1) / 2;
        }
        
        return count;
    }
    
    /**
     * 查找所有不同的回文子串
     * 
     * @param s 输入字符串
     * @return 包含所有不同回文子串的集合
     */
    static set<string> findAllDistinctPalindromicSubstrings(const string& s) {
        set<string> result;
        if (s.empty()) {
            return result;
        }
        
        // 预处理字符串
        string T = preprocess(s);
        int n = T.length();
        vector<int> P(n, 0);
        int C = 0, R = 0;
        
        for (int i = 1; i < n - 1; i++) {
            int iMirror = 2 * C - i;
            P[i] = (R > i) ? min(R - i, P[iMirror]) : 0;
            
            try {
                while (T.at(i + 1 + P[i]) == T.at(i - 1 - P[i])) {
                    P[i]++;
                }
            } catch (const out_of_range& e) {
                // 边界情况处理
            }
            
            if (i + P[i] > R) {
                C = i;
                R = i + P[i];
            }
            
            // 提取所有以i为中心的回文子串
            // 从1开始（半径至少为1）到P[i]
            for (int r = 1; r <= P[i]; r++) {
                int start = (i - r) / 2;
                int end = start + r;
                string palindrome = s.substr(start, r);
                result.insert(palindrome);
            }
        }
        
        return result;
    }
};

int main() {
    // 测试用例1：基本功能测试
    string text1 = "babad";
    cout << "=== 测试用例1 ===" << endl;
    cout << "文本: " << text1 << endl;
    cout << "最长回文子串: " << ManacherAlgorithm::findLongestPalindromicSubstring(text1) << endl; // "bab" 或 "aba"
    cout << "回文子串数量: " << ManacherAlgorithm::countPalindromicSubstrings(text1) << endl; // 7
    auto palindromes1 = ManacherAlgorithm::findAllDistinctPalindromicSubstrings(text1);
    cout << "不同回文子串: {";
    for (const auto& p : palindromes1) {
        cout << "\"" << p << "\", ";
    }
    cout << "}" << endl;
    
    // 测试用例2：边界情况
    string text2 = "cbbd";
    cout << "\n=== 测试用例2 ===" << endl;
    cout << "文本: " << text2 << endl;
    cout << "最长回文子串: " << ManacherAlgorithm::findLongestPalindromicSubstring(text2) << endl; // "bb"
    
    // 测试用例3：单个字符
    string text3 = "a";
    cout << "\n=== 测试用例3 ===" << endl;
    cout << "文本: " << text3 << endl;
    cout << "最长回文子串: " << ManacherAlgorithm::findLongestPalindromicSubstring(text3) << endl; // "a"
    
    // 测试用例4：重复字符
    string text4 = "aaa";
    cout << "\n=== 测试用例4 ===" << endl;
    cout << "文本: " << text4 << endl;
    cout << "最长回文子串: " << ManacherAlgorithm::findLongestPalindromicSubstring(text4) << endl; // "aaa"
    cout << "回文子串数量: " << ManacherAlgorithm::countPalindromicSubstrings(text4) << endl; // 6
    
    // 测试用例5：较长文本
    string text5 = "mississippi";
    cout << "\n=== 测试用例5 ===" << endl;
    cout << "文本: " << text5 << endl;
    cout << "最长回文子串: " << ManacherAlgorithm::findLongestPalindromicSubstring(text5) << endl; // "ississi"
    
    return 0;
}
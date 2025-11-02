/**
 * LeetCode 5. 最长回文子串
 * 
 * 题目描述:
 * 给你一个字符串 s，找到 s 中最长的回文子串
 * 
 * 输入格式:
 * 字符串 s
 * 
 * 输出格式:
 * 字符串，表示最长回文子串
 * 
 * 数据范围:
 * 1 <= s.length <= 1000
 * s 由数字和英文字母组成
 * 
 * 题目链接: https://leetcode.cn/problems/longest-palindromic-substring/
 * 
 * 解题思路:
 * 使用Manacher算法求解最长回文子串问题。Manacher算法通过预处理字符串并在每个字符间插入特殊字符，
 * 利用回文的对称性质避免重复计算，从而在线性时间内解决问题。
 * 
 * 算法步骤:
 * 1. 预处理字符串: 在原字符串的每个字符之间插入特殊字符'#'，并在开头和结尾也插入'#'
 * 2. 初始化变量: 维护当前最右回文边界r、对应的中心c，以及每个位置的回文半径数组p
 * 3. 遍历预处理后的字符串:
 *    - 利用回文对称性优化: 如果当前位置i在当前右边界内，则可以利用对称点的信息
 *    - 尝试扩展回文串: 从最小可能的半径开始扩展
 *    - 更新最右回文边界和中心
 *    - 记录最大回文半径和对应的结束位置
 * 4. 根据最大回文半径和结束位置，从原字符串中提取最长回文子串
 * 
 * 时间复杂度: O(n)，其中n为字符串长度
 * 空间复杂度: O(n)，用于存储预处理字符串和回文半径数组
 * 
 * 与其他解法的比较:
 * 1. 暴力法: 时间复杂度O(n^3)，空间复杂度O(1)
 * 2. 中心扩展法: 时间复杂度O(n^2)，空间复杂度O(1)
 * 3. 动态规划法: 时间复杂度O(n^2)，空间复杂度O(n^2)
 * 4. Manacher算法: 时间复杂度O(n)，空间复杂度O(n)
 * 
 * 算法优化点:
 * 1. 预处理字符串统一处理奇数和偶数长度的回文串
 * 2. 利用回文对称性避免重复计算
 * 3. 线性时间复杂度的算法实现
 */

#include <iostream>
#include <string>
using namespace std;

// 定义最大字符串长度
#define MAXN 1001

// 预处理后的字符串数组
char ss[MAXN << 1];

// 回文半径数组
int p[MAXN << 1];

// 预处理后字符串的长度
int n;

// 最大回文半径
int max_p;

// 回文结束位置（在原字符串中的位置）
int end_pos;

/**
 * 预处理函数，用于在字符间插入'#'
 * 
 * 预处理的目的：
 * 1. 统一处理奇数长度和偶数长度的回文串
 * 2. 简化回文扩展的逻辑
 * 
 * @param a 原始字符串
 * @param len 原始字符串长度
 */
void manacherss(const char* a, int len) {
    // 计算预处理后字符串的长度
    n = len * 2 + 1;
    
    // 遍历预处理后的字符串位置
    for (int i = 0, j = 0; i < n; i++) {
        // 如果位置i是偶数，则插入特殊字符'#'
        // 如果位置i是奇数，则插入原字符串中的字符
        ss[i] = (i & 1) == 0 ? '#' : a[j++];
    }
}

/**
 * Manacher算法主函数
 * 
 * 算法原理：
 * 1. 预处理：在原字符串的每个字符之间插入特殊字符'#'
 * 2. 利用回文串的对称性，避免重复计算
 * 3. 维护当前最右回文边界r和对应的中心c，通过已计算的信息加速新位置的计算
 * 
 * @param str 原始字符串
 */
void manacher(const string& str) {
    int len = str.length();
    
    // 预处理字符串
    manacherss(str.c_str(), len);
    
    // 初始化最大回文半径和结束位置
    max_p = end_pos = 0;
    
    // 遍历预处理后的字符串中的每个位置
    for (int i = 0, c = 0, r = 0, len_p; i < n; i++) {
        // 利用回文对称性优化
        // 如果当前位置i在当前右边界内，则可以利用对称点的信息
        len_p = r > i ? (p[2 * c - i] < (r - i) ? p[2 * c - i] : (r - i)) : 1;
        
        // 尝试扩展回文串
        // 从最小可能的半径开始扩展，直到无法扩展为止
        while (i + len_p < n && i - len_p >= 0 && ss[i + len_p] == ss[i - len_p]) {
            len_p++;
        }
        
        // 更新最右回文边界和中心
        // 如果当前回文串的右边界超过了记录的最右边界，则更新
        if (i + len_p > r) {
            r = i + len_p;
            c = i;
        }
        
        // 更新最大回文半径和结束位置
        if (len_p - 1 > max_p) {
            max_p = len_p - 1; // 实际回文长度为半径减1
            end_pos = (i + len_p - 1) / 2; // 转换回原字符串中的位置
        }
        
        // 记录当前位置的回文半径
        p[i] = len_p;
    }
}

/**
 * 查找字符串s中的最长回文子串
 * 
 * @param s 输入字符串
 * @return 最长回文子串
 */
string longestPalindrome(string s) {
    int len = s.length();
    
    // 处理边界情况
    if (len <= 1) {
        return s;
    }
    
    // 应用Manacher算法
    manacher(s);
    
    // 计算起始位置：结束位置 - 最大回文长度 + 1
    int start = end_pos - max_p;
    
    // 提取最长回文子串
    return s.substr(start, max_p + 1);
}

/**
 * 测试用例
 * 输入: s = "babad"
 * 输出: "bab" 或 "aba"
 * 
 * 输入: s = "cbbd"
 * 输出: "bb"
 */
int main() {
    // 测试用例1
    string s1 = "babad";
    cout << "输入: " << s1 << ", 输出: " << longestPalindrome(s1) << endl;
    
    // 测试用例2
    string s2 = "cbbd";
    cout << "输入: " << s2 << ", 输出: " << longestPalindrome(s2) << endl;
    
    // 测试边界情况
    string s3 = "a";
    cout << "输入: " << s3 << ", 输出: " << longestPalindrome(s3) << endl;
    
    string s4 = "";
    cout << "输入: " << s4 << ", 输出: " << longestPalindrome(s4) << endl;
    
    // 额外测试用例
    string s5 = "racecar";
    cout << "输入: " << s5 << ", 输出: " << longestPalindrome(s5) << endl;
    
    return 0;
}
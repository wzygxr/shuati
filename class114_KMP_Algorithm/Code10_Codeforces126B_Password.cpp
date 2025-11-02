/*
 * Codeforces 126B Password
 * 
 * 题目来源：Codeforces
 * 题目链接：https://codeforces.com/problemset/problem/126/B
 * 
 * 题目描述：
 * 给定一个字符串s，找到一个子串，它既是前缀又是后缀，同时在字符串中间也出现过。
 * 如果有多个这样的子串，输出最长的那个。如果没有这样的子串，输出"Just a legend"。
 * 
 * 示例：
 * 输入："fixprefixsuffix"
 * 输出："fix"
 * 
 * 输入："abcdabc"
 * 输出："Just a legend"
 * 
 * 算法思路：
 * 使用KMP算法的next数组来解决这个问题。
 * 1. 构建字符串的next数组
 * 2. 通过next[n-1]找到最长的既是前缀又是后缀的子串
 * 3. 检查这个子串是否在中间出现过
 * 4. 如果没有，则通过next数组继续查找更短的候选子串
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 */

#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
using namespace std;

#define MAXN 1000001

int next_array[MAXN];

/*
 * 构建KMP算法的next数组（部分匹配表）
 * 
 * next[i]表示str[0...i]子串的最长相等前后缀的长度
 * 
 * @param str 字符数组
 * @param length 字符数组长度
 */
void build_next_array(char* str, int length) {
    // 初始化
    next_array[0] = 0;
    int prefix_len = 0;  // 当前最长相等前后缀的长度
    int i = 1;          // 当前处理的位置
    
    // 从位置1开始处理
    while (i < length) {
        // 如果当前字符匹配，可以延长相等前后缀
        if (str[i] == str[prefix_len]) {
            prefix_len++;
            next_array[i] = prefix_len;
            i++;
        } 
        // 如果不匹配且前缀长度大于0，需要回退
        else if (prefix_len > 0) {
            prefix_len = next_array[prefix_len - 1];
        } 
        // 如果不匹配且前缀长度为0，next[i] = 0
        else {
            next_array[i] = 0;
            i++;
        }
    }
}

/*
 * 检查指定长度的前缀是否在字符串中间出现过
 * 
 * @param str 字符数组
 * @param length 子串长度
 * @param str_len 字符串长度
 * @return 是否在中间出现过
 */
bool is_substring_present(char* str, int length, int str_len) {
    // 在next数组中查找是否有等于length的值（除了最后一个位置）
    for (int i = 0; i < str_len - 1; i++) {
        if (next_array[i] == length) {
            return true;
        }
    }
    return false;
}

/*
 * 找到符合条件的最长子串
 * 
 * @param s 输入字符串
 * @return 符合条件的最长子串，如果不存在则返回"Just a legend"
 */
string find_password(string s) {
    // 边界条件处理
    if (s.length() <= 2) {
        return "Just a legend";
    }
    
    int n = s.length();
    char* str = new char[n + 1];
    strcpy(str, s.c_str());
    
    // 构建next数组
    build_next_array(str, n);
    
    // 从最长的候选子串开始检查
    int candidate_length = next_array[n - 1];
    
    // 检查是否有符合条件的子串
    while (candidate_length > 0) {
        // 检查这个长度的子串是否在中间出现过
        if (is_substring_present(str, candidate_length, n)) {
            delete[] str;
            return s.substr(0, candidate_length);
        }
        // 尝试更短的候选子串
        candidate_length = next_array[candidate_length - 1];
    }
    
    delete[] str;
    return "Just a legend";
}

// 测试方法
int main() {
    // 测试用例1
    string s1 = "fixprefixsuffix";
    string result1 = find_password(s1);
    cout << "测试用例1:" << endl;
    cout << "输入字符串: " << s1 << endl;
    cout << "输出: " << result1 << endl;
    cout << "预期输出: fix" << endl << endl;
    
    // 测试用例2
    string s2 = "abcdabc";
    string result2 = find_password(s2);
    cout << "测试用例2:" << endl;
    cout << "输入字符串: " << s2 << endl;
    cout << "输出: " << result2 << endl;
    cout << "预期输出: Just a legend" << endl << endl;
    
    // 测试用例3
    string s3 = "abcabcabcabc";
    string result3 = find_password(s3);
    cout << "测试用例3:" << endl;
    cout << "输入字符串: " << s3 << endl;
    cout << "输出: " << result3 << endl;
    cout << "预期输出: abcabcabc" << endl << endl;
    
    // 测试用例4
    string s4 = "aaaa";
    string result4 = find_password(s4);
    cout << "测试用例4:" << endl;
    cout << "输入字符串: " << s4 << endl;
    cout << "输出: " << result4 << endl;
    cout << "预期输出: aaa" << endl << endl;
    
    // 测试用例5
    string s5 = "abc";
    string result5 = find_password(s5);
    cout << "测试用例5:" << endl;
    cout << "输入字符串: " << s5 << endl;
    cout << "输出: " << result5 << endl;
    cout << "预期输出: Just a legend" << endl;
    
    return 0;
}
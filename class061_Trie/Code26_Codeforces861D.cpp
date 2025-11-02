#include <bits/stdc++.h>

using namespace std;

/**
 * Codeforces 861D - Polycarp's phone book
 * 
 * 题目描述：
 * 给定n个长度为9的数字字符串，对于每个字符串，找到最短的特有子串（即该子串只在这个字符串中出现）。
 * 
 * 解题思路：
 * 1. 使用unordered_map统计所有子串的出现次数
 * 2. 对于每个字符串，枚举其所有子串，在unordered_map中查找出现次数为1的最短子串
 * 
 * 时间复杂度：O(N * L^3)，其中N是字符串数量，L是字符串长度
 * 空间复杂度：O(N * L^3)
 */

// 存储子串出现次数的映射
unordered_map<string, int> substringCount;

/**
 * 统计所有子串的出现次数
 * @param str 要处理的字符串
 */
void countSubstrings(const string& str) {
    // 枚举所有子串
    for (int i = 0; i < str.length(); i++) {
        for (int j = i + 1; j <= str.length(); j++) {
            substringCount[str.substr(i, j - i)]++;
        }
    }
}

/**
 * 查找字符串的出现次数
 * @param str 要查找的字符串
 * @return 出现次数
 */
int search(const string& str) {
    return substringCount[str];
}

int main() {
    int n;
    cin >> n; // 字符串数量
    cin.ignore(); // 消费换行符
    
    vector<string> strings(n);
    // 读取所有字符串
    for (int i = 0; i < n; i++) {
        getline(cin, strings[i]);
    }
    
    // 将所有子串插入映射
    for (const string& str : strings) {
        countSubstrings(str);
    }
    
    // 对于每个字符串，找到最短的特有子串
    for (const string& str : strings) {
        string result = str; // 默认结果为整个字符串
        
        // 枚举所有子串，按长度递增
        bool found = false;
        for (int len = 1; len <= str.length() && !found; len++) {
            for (int i = 0; i <= str.length() - len && !found; i++) {
                string substr = str.substr(i, len);
                // 如果该子串只出现一次，说明是特有子串
                if (search(substr) == 1) {
                    result = substr;
                    found = true;
                }
            }
        }
        
        cout << result << endl;
    }
    
    return 0;
}
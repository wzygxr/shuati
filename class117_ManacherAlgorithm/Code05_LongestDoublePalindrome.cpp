/**
 * 洛谷 P4555 [国家集训队]最长双回文串
 * 
 * 题目描述:
 * 输入字符串s，求s的最长双回文子串t的长度
 * 双回文子串就是可以分成两个回文串的字符串
 * 
 * 输入格式:
 * 一行字符串s
 * 
 * 输出格式:
 * 一个整数表示答案
 * 
 * 数据范围:
 * 字符串长度不超过10^5
 * 
 * 题目链接: https://www.luogu.com.cn/problem/P4555
 * 
 * 解题思路:
 * 使用Manacher算法预处理回文信息，然后分别计算每个位置向左和向右的最长回文半径
 * 最后找到最大的left[i] + right[i]作为答案
 * 
 * 算法步骤:
 * 1. 使用Manacher算法预处理字符串，得到每个位置的回文半径
 * 2. 计算每个位置向左的最长回文半径left[i]
 * 3. 计算每个位置向右的最长回文半径right[i]
 * 4. 遍历所有位置，找到最大的left[i] + right[i]
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
 * 计算最长双回文串长度
 * 
 * @param s 输入字符串
 * @return 最长双回文串长度
 */
int longest_double_palindrome(const string& s) {
    int n = s.size();
    if (n <= 1) return 0;
    
    // 预处理字符串
    string processed = "#";
    for (char c : s) {
        processed += c;
        processed += '#';
    }
    
    int m = processed.size();
    vector<int> p(m, 0);
    int center = 0, right = 0;
    
    // Manacher算法
    for (int i = 0; i < m; i++) {
        if (i < right) {
            int mirror = 2 * center - i;
            p[i] = min(right - i, p[mirror]);
        }
        
        while (i + p[i] + 1 < m && i - p[i] - 1 >= 0 && 
               processed[i + p[i] + 1] == processed[i - p[i] - 1]) {
            p[i]++;
        }
        
        if (i + p[i] > right) {
            center = i;
            right = i + p[i];
        }
    }
    
    // 计算向左的最长回文半径
    vector<int> left(m, 0);
    for (int i = 0, j = 0; i < m; i++) {
        while (i + p[i] > j) {
            left[j] = j - i;
            j += 2; // 只处理原字符串位置
        }
    }
    
    // 计算向右的最长回文半径
    vector<int> right_arr(m, 0);
    for (int i = m - 1, j = m - 1; i >= 0; i--) {
        while (i - p[i] < j) {
            right_arr[j] = i - j;
            j -= 2; // 只处理原字符串位置
        }
    }
    
    // 找到最大的left[i] + right[i]
    int ans = 0;
    for (int i = 2; i <= m - 3; i += 2) { // 只处理原字符串位置之间的分隔符
        ans = max(ans, left[i] + right_arr[i]);
    }
    
    return ans;
}

int main() {
    string s;
    cin >> s;
    cout << longest_double_palindrome(s) << endl;
    return 0;
}

/**
 * 测试用例和验证
 * 
 * 示例1:
 * 输入: "baacaabbacabb"
 * 输出: 12
 * 解释: "aacaabbacabb"可以分成"aacaa"和"bbacabb"
 * 
 * 示例2:
 * 输入: "aa"
 * 输出: 2
 * 解释: "aa"可以分成"a"和"a"
 * 
 * 示例3:
 * 输入: "aaa"
 * 输出: 3
 * 解释: "aaa"可以分成"aa"和"a"
 * 
 * 算法正确性验证:
 * 
 * 对于字符串"baacaabbacabb":
 * - 预处理后字符串: "#b#a#a#c#a#a#b#b#a#c#a#b#b#"
 * - 计算每个位置的回文半径
 * - 计算向左和向右的最长回文半径
 * - 找到最大的left[i] + right[i] = 12
 */
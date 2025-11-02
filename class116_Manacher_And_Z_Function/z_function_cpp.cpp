#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
#include <utility>
using namespace std;

const int MAXN = 20000001;
int z[MAXN];
int e[MAXN];

/**
 * Z函数计算
 * Z函数z[i]表示字符串s从位置i开始与字符串s从位置0开始的最长公共前缀长度
 * 
 * 算法原理：
 * 1. 维护一个匹配区间[l, r]，表示当前已知的最右匹配区间
 * 2. 对于当前位置i，如果i <= r，可以利用已计算的信息优化
 * 3. 利用对称性，z[i]至少为min(r - i + 1, z[i - l])
 * 4. 在此基础之上继续向右扩展匹配
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 * 
 * 应用场景：
 * 1. LeetCode 2223. 构造字符串的总得分和 - https://leetcode.com/problems/sum-of-scores-of-built-strings/
 * 2. LeetCode 3031. 将单词恢复初始状态所需的最短时间 II - https://leetcode.com/problems/minimum-time-to-revert-word-to-initial-state-ii/
 * 3. Codeforces 126B. Password - https://codeforces.com/problemset/problem/126/B
 * 4. 洛谷 P5410 【模板】扩展KMP/exKMP（Z 函数） - https://www.luogu.com.cn/problem/P5410
 * 5. SPOJ - Pattern Find
 * 6. HackerEarth - String Similarity - https://www.hackerearth.com/practice/algorithms/string-algorithm/z-algorithm/tutorial/
 * 7. AtCoder ABC141E - Who Says a Pun? - https://atcoder.jp/contests/abc141/tasks/abc141_e
 * 8. USACO 2011 November Contest, Bronze - Cow Photographs
 * 9. 牛客网 NC15051 - 字符串的匹配
 */
void zArray(string s, int n) {
    z[0] = n;
    // l: 当前最右匹配区间的左边界
    // r: 当前最右匹配区间的右边界
    for (int i = 1, l = 0, r = 0; i < n; i++) {
        // 利用已计算的信息优化
        // 如果i在当前匹配区间内
        int len = (r > i) ? min(r - i + 1, z[i - l]) : 0;
        // 继续向右扩展匹配
        while (i + len < n && s[i + len] == s[len]) {
            len++;
        }
        // 更新最右匹配区间
        if (i + len > r) {
            r = i + len;
            l = i;
        }
        z[i] = len;
    }
}

/**
 * 扩展KMP计算
 * 计算字符串a的每个后缀与字符串b的最长公共前缀长度
 * 
 * 时间复杂度：O(n + m)，其中n是a的长度，m是b的长度
 * 空间复杂度：O(n + m)
 * 
 * @param a 主字符串
 * @param b 模式字符串
 * @param n 主字符串长度
 * @param m 模式字符串长度
 */
void eArray(string a, string b, int n, int m) {
    for (int i = 0, l = 0, r = 0; i < n; i++) {
        // 利用已计算的信息优化
        int len = (r > i) ? min(r - i + 1, z[i - l]) : 0;
        // 继续向右扩展匹配
        while (i + len < n && len < m && a[i + len] == b[len]) {
            len++;
        }
        // 更新最右匹配区间
        if (i + len > r) {
            r = i + len;
            l = i;
        }
        e[i] = len;
    }
}

/**
 * 计算数组的权值异或和
 * 对于数组中的每个元素arr[i]，权值为 (i+1) * (arr[i] + 1)
 * 
 * @param arr 输入数组
 * @param n 数组长度
 * @return 所有权值的异或和
 */
long long eor(int arr[], int n) {
    long long ans = 0;
    for (int i = 0; i < n; i++) {
        ans ^= (long long)(i + 1) * (arr[i] + 1);
    }
    return ans;
}

/**
 * LeetCode 2223. 构造字符串的总得分和
 * 你需要从空字符串开始构造一个长度为n的字符串s，构造过程为每次给当前字符串前面添加一个字符。
 * 构造过程中得到的所有字符串编号为1到n，其中长度为i的字符串编号为si。
 * si的得分为si和sn的最长公共前缀的长度（注意s == sn）。
 * 请你返回每一个si的得分之和。
 * 
 * 示例:
 * 输入: s = "babab"
 * 输出: 9
 * 解释: 
 * s1 == "b"，得分1
 * s2 == "ab"，得分0
 * s3 == "bab"，得分3
 * s4 == "abab"，得分0
 * s5 == "babab"，得分5
 * 总和为1+0+3+0+5=9
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 * 
 * @param s 输入字符串
 * @return 得分总和
 */
long long sumScores(string s) {
    int n = s.length();
    
    // 计算Z函数
    vector<int> z(n, 0);
    z[0] = n;
    
    long long sum = n; // sn的得分就是整个字符串的长度
    
    for (int i = 1, l = 0, r = 0; i < n; i++) {
        // 利用之前计算的结果
        if (i <= r) {
            z[i] = min(r - i + 1, z[i - l]);
        }
        
        // 扩展匹配
        while (i + z[i] < n && s[z[i]] == s[i + z[i]]) {
            z[i]++;
        }
        
        // 更新匹配区间
        if (i + z[i] - 1 > r) {
            l = i;
            r = i + z[i] - 1;
        }
        
        // 累加得分
        sum += z[i];
    }
    
    return sum;
}

/**
 * LeetCode 3031. 将单词恢复初始状态所需的最短时间 II
 * 给你一个下标从0开始的字符串word和一个整数k。
 * 每一秒执行以下操作：
 * 1. 移除word的前k个字符
 * 2. 在word的末尾添加k个任意字符
 * 返回将word恢复到初始状态所需的最短时间（该时间必须大于零）。
 * 
 * 示例:
 * 输入: word = "abacaba", k = 3
 * 输出: 2
 * 解释:
 * 第1秒后，word变成"acaba**"（用*表示添加的字符）
 * 第2秒后，word变成"aba****"
 * 如果添加的字符分别为"cac"和"caba"，word就恢复为"abacaba"
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 * 
 * @param word 输入字符串
 * @param k 每次操作移除和添加的字符数
 * @return 恢复初始状态所需的最短时间
 */
int minimumTimeToInitialState(string word, int k) {
    int n = word.length();
    
    // 计算Z函数
    vector<int> z(n, 0);
    z[0] = n;
    
    for (int i = 1, l = 0, r = 0; i < n; i++) {
        if (i <= r) {
            z[i] = min(r - i + 1, z[i - l]);
        }
        
        while (i + z[i] < n && word[z[i]] == word[i + z[i]]) {
            z[i]++;
        }
        
        if (i + z[i] - 1 > r) {
            l = i;
            r = i + z[i] - 1;
        }
    }
    
    // 查找满足条件的最小时间
    for (int i = k; i < n; i += k) {
        // 如果从位置i开始的后缀与原字符串的最长公共前缀长度等于后缀长度
        // 说明在第(i/k)步后可以恢复原字符串
        if (z[i] >= n - i) {
            return i / k;
        }
    }
    
    // 最坏情况需要完全替换
    return (n + k - 1) / k;
}

/**
 * 洛谷 P5410 【模板】扩展 KMP（Z 函数）
 * 题目描述：给定两个字符串 a,b，求：
 * 1. b 与 b 每一个后缀串的最长公共前缀长度（即 b 的 Z 函数）
 * 2. a 与 b 每一个后缀串的最长公共前缀长度（即扩展 KMP）
 * 
 * 时间复杂度: O(n + m)
 * 空间复杂度: O(n + m)
 * 
 * @param a 主字符串
 * @param b 模式字符串
 * @return pair<Z数组异或和, E数组异或和>
 */
pair<long long, long long> extendedKMP(string a, string b) {
    int n = a.length();
    int m = b.length();
    
    // 计算b的Z函数
    vector<int> z(m, 0);
    z[0] = m;
    
    for (int i = 1, l = 0, r = 0; i < m; i++) {
        if (i <= r) {
            z[i] = min(r - i + 1, z[i - l]);
        }
        
        while (i + z[i] < m && b[z[i]] == b[i + z[i]]) {
            z[i]++;
        }
        
        if (i + z[i] - 1 > r) {
            l = i;
            r = i + z[i] - 1;
        }
    }
    
    // 计算a与b的扩展KMP
    vector<int> e(n, 0);
    for (int i = 0, l = 0, r = 0; i < n; i++) {
        int len = (r > i) ? min(r - i + 1, z[i - l]) : 0;
        
        while (i + len < n && len < m && a[i + len] == b[len]) {
            len++;
        }
        
        if (i + len > r) {
            r = i + len;
            l = i;
        }
        e[i] = len;
    }
    
    // 计算异或和
    long long zXor = 0;
    for (int i = 0; i < m; i++) {
        zXor ^= (long long)(i + 1) * (z[i] + 1);
    }
    
    long long eXor = 0;
    for (int i = 0; i < n; i++) {
        eXor ^= (long long)(i + 1) * (e[i] + 1);
    }
    
    return make_pair(zXor, eXor);
}

int main() {
    // 测试洛谷P5410 扩展KMP模板题
    string a, b;
    getline(cin, a);
    getline(cin, b);
    
    int n = a.length();
    int m = b.length();
    
    // 计算b的Z函数
    zArray(b, m);
    // 计算a与b的扩展KMP
    eArray(a, b, n, m);
    
    cout << eor(z, m) << endl;
    cout << eor(e, n) << endl;
    
    return 0;
}
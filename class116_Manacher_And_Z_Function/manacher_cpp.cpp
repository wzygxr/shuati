#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
using namespace std;

const int MAXN = 11000001;
char ss[MAXN << 1];
int p[MAXN << 1];
int n;

/**
 * Manacher算法主函数，用于计算字符串中最长回文子串的长度
 * 
 * 算法原理：
 * 1. 预处理：在原字符串的每个字符之间插入特殊字符'#'，并在首尾也添加'#'
 *    这样可以将奇数长度和偶数长度的回文串统一处理为奇数长度的回文串
 * 2. 利用回文串的对称性，避免重复计算
 * 3. 维护当前最右回文边界r和对应的中心c，通过已计算的信息加速新位置的计算
 * 
 * 时间复杂度：O(n)，其中n为字符串长度
 * 空间复杂度：O(n)
 * 
 * @param str 输入字符串
 * @return 最长回文子串的长度
 * 
 * 应用场景：
 * 1. LeetCode 5. 最长回文子串 - https://leetcode.com/problems/longest-palindromic-substring/
 * 2. LeetCode 647. 回文子串 - https://leetcode.com/problems/palindromic-substrings/
 * 3. LeetCode 214. 最短回文串 - https://leetcode.com/problems/shortest-palindrome/
 * 4. 洛谷 P3805 【模板】manacher - https://www.luogu.com.cn/problem/P3805
 * 5. UVa 11475 - Extend to Palindrome - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2470
 * 6. Codeforces 1326D2 - Prefix-Suffix Palindrome - https://codeforces.com/problemset/problem/1326/D2
 * 7. HackerRank - Palindromic Substrings
 * 8. AcWing 141. 周期 - https://www.acwing.com/problem/content/143/
 * 9. POJ 3240 - 回文串
 * 10. LeetCode 336. 回文对 - https://leetcode.com/problems/palindrome-pairs/
 * 11. LeetCode 131. 分割回文串 - https://leetcode.com/problems/palindrome-partitioning/
 * 12. LeetCode 132. 分割回文串 II - https://leetcode.com/problems/palindrome-partitioning-ii/
 */
int manacher(string str) {
    // 预处理字符串
    n = str.length() * 2 + 1;
    for (int i = 0, j = 0; i < n; i++) {
        ss[i] = (i & 1) == 0 ? '#' : str[j++];
    }
    
    int maxLen = 0;
    // c: 当前最右回文子串的中心
    // r: 当前最右回文子串的右边界
    for (int i = 0, c = 0, r = 0; i < n; i++) {
        // 利用回文对称性优化
        int len = r > i ? min(p[2 * c - i], r - i) : 1;
        
        // 尝试扩展回文串
        while (i + len < n && i - len >= 0 && ss[i + len] == ss[i - len]) {
            len++;
        }
        
        // 更新最右回文边界和中心
        if (i + len > r) {
            r = i + len;
            c = i;
        }
        
        // 更新最大回文半径
        maxLen = max(maxLen, len);
        p[i] = len;
    }
    
    // 由于我们插入了'#'字符，实际回文长度是半径减1
    return maxLen - 1;
}

/**
 * LeetCode 5. 最长回文子串
 * 给你一个字符串 s，找到 s 中最长的回文子串
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 */
string longestPalindrome(string s) {
    if (s.empty()) return "";
    
    // 预处理字符串
    string processed = "^";
    for (int i = 0; i < s.length(); i++) {
        processed += "#" + string(1, s[i]);
    }
    processed += "#$";
    
    int len = processed.length();
    vector<int> radius(len, 0);
    
    int center = 0, right = 0;
    int maxLen = 0, centerIndex = 0;
    
    for (int i = 1; i < len - 1; i++) {
        // 利用回文对称性
        int mirror = 2 * center - i;
        
        if (i < right) {
            radius[i] = min(right - i, radius[mirror]);
        }
        
        // 尝试扩展回文
        while (processed[i + radius[i] + 1] == processed[i - radius[i] - 1]) {
            radius[i]++;
        }
        
        // 更新最右边界
        if (i + radius[i] > right) {
            center = i;
            right = i + radius[i];
        }
        
        // 更新最长回文
        if (radius[i] > maxLen) {
            maxLen = radius[i];
            centerIndex = i;
        }
    }
    
    // 从处理后的字符串中提取原始回文子串
    int start = (centerIndex - maxLen) / 2;
    return s.substr(start, maxLen);
}

/**
 * LeetCode 647. 回文子串
 * 给定一个字符串，计算其中回文子串的数目
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 */
int countSubstrings(string s) {
    if (s.empty()) return 0;
    
    // 预处理字符串
    string processed = "^";
    for (int i = 0; i < s.length(); i++) {
        processed += "#" + string(1, s[i]);
    }
    processed += "#$";
    
    int len = processed.length();
    vector<int> radius(len, 0);
    
    int center = 0, right = 0;
    int count = 0;
    
    for (int i = 1; i < len - 1; i++) {
        // 利用回文对称性
        int mirror = 2 * center - i;
        
        if (i < right) {
            radius[i] = min(right - i, radius[mirror]);
        }
        
        // 尝试扩展回文
        while (processed[i + radius[i] + 1] == processed[i - radius[i] - 1]) {
            radius[i]++;
        }
        
        // 更新最右边界
        if (i + radius[i] > right) {
            center = i;
            right = i + radius[i];
        }
        
        // 每个回文半径可以贡献 radius[i]/2 个回文子串
        count += (radius[i] + 1) / 2;
    }
    
    return count;
}

/**
 * LeetCode 214. 最短回文串
 * 给定一个字符串 s，可以通过在字符串前面添加字符将其转换为回文串。
 * 找到并返回可以用这种方式转换的最短回文串。
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 */
string shortestPalindrome(string s) {
    if (s.length() <= 1) return s;
    
    // 将字符串与其反转拼接，中间用特殊字符分隔
    string combined = s + "#" + string(s.rbegin(), s.rend());
    
    // 计算KMP的LPS数组
    vector<int> lps(combined.length(), 0);
    for (int i = 1, j = 0; i < combined.length(); i++) {
        if (combined[i] == combined[j]) {
            lps[i] = ++j;
        } else {
            if (j != 0) {
                j = lps[j - 1];
                i--; // 重新检查当前位置
            }
        }
    }
    
    // 找到原字符串的最长回文前缀
    int overlap = lps[combined.length() - 1];
    
    // 在原字符串前添加反转的部分
    string prefix = string(s.begin() + overlap, s.end());
    reverse(prefix.begin(), prefix.end());
    return prefix + s;
}

/**
 * 洛谷 P3805 【模板】manacher
 * 题目描述：给出一个只由小写英文字符 a,b,c...y,z 组成的字符串 S ,
 * 求 S 中最长回文串的长度 。
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 */
int longestPalindromeLength(string s) {
    return manacher(s);
}

int main() {
    // 测试洛谷P3805 Manacher模板题
    string input;
    getline(cin, input);
    cout << manacher(input) << endl;
    
    return 0;
}
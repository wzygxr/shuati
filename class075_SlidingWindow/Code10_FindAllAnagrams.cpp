/*
 * 找到字符串中所有字母异位词问题解决方案
 * 
 * 问题描述：
 * 给定两个字符串 s 和 p，找到 s 中所有 p 的 异位词 的子串，返回这些子串的起始索引。
 * 异位词 指由相同字母重排列形成的字符串（包括相同的字符串）。
 * 
 * 解题思路：
 * 使用滑动窗口算法找到s中所有p的异位词：
 * 1. 统计p中各字符的频次
 * 2. 维护一个长度为p.length()的滑动窗口遍历s
 * 3. 当窗口内字符频次与p完全匹配时，说明找到了一个异位词
 * 
 * 算法复杂度分析：
 * 时间复杂度: O(n) - n为s的长度
 * 空间复杂度: O(1) - 只需要26个字母的统计数组
 * 
 * 是否最优解: 是
 * 
 * 相关题目链接：
 * LeetCode 438. 找到字符串中所有字母异位词
 * https://leetcode.cn/problems/find-all-anagrams-in-a-string/
 * 
 * 其他平台类似题目：
 * 1. 牛客网 - 找到字符串中所有字母异位词
 *    https://www.nowcoder.com/practice/432531b6fc7b483096e5f9170c862a49
 * 2. LintCode 647. 回文子串
 *    https://www.lintcode.com/problem/647/
 * 3. HackerRank - Find All Anagrams in a String
 *    https://www.hackerrank.com/challenges/find-all-anagrams-in-a-string/problem
 * 4. CodeChef - ANAGRAMS - Anagrams
 *    https://www.codechef.com/problems/ANAGRAMS
 * 5. AtCoder - ABC146 D - Enough Array
 *    https://atcoder.jp/contests/abc146/tasks/abc146_d
 * 6. 洛谷 P1886 滑动窗口
 *    https://www.luogu.com.cn/problem/P1886
 * 7. 杭电OJ 4193 Sliding Window
 *    http://acm.hdu.edu.cn/showproblem.php?pid=4193
 * 8. POJ 2823 Sliding Window
 *    http://poj.org/problem?id=2823
 * 9. UVa OJ 11536 - Smallest Sub-Array
 *    https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=2531
 * 10. SPOJ - ADAFRIEN - Ada and Friends
 *     https://www.spoj.com/problems/ADAFRIEN/
 * 
 * 工程化考量：
 * 1. 异常处理：处理空字符串、s长度小于p等边界情况
 * 2. 性能优化：使用滑动窗口避免重复计算，达到线性时间复杂度
 * 3. 可读性：变量命名清晰，添加详细注释，提供测试用例
 * 
 * 编译说明：
 * 此代码需要C++标准库支持，编译时请确保包含正确的头文件路径
 * 编译命令示例：g++ -std=c++11 Code10_FindAllAnagrams.cpp -o Code10_FindAllAnagrams
 */

// 算法实现（需要C++标准库支持）
/*
#include <string>
#include <vector>
#include <iostream>
using namespace std;

vector<int> findAnagrams(string s, string p) {
    // 初始化结果列表
    vector<int> result;
    
    // 异常情况处理：如果s长度小于p，不可能包含p的异位词
    if (s.length() < p.length()) {
        return result;
    }
    
    // 统计p中各字符的频次
    vector<int> count(26, 0);
    for (char c : p) {
        count[c - 'a']++;
    }
    
    int windowLen = p.length();
    
    // 滑动窗口遍历s
    // l为左指针，r为右指针，diff为当前窗口与p的字符差异计数
    for (int l = 0, r = 0, diff = p.length(); r < s.length(); r++) {
        // 右边界字符进入窗口
        // 如果该字符在p中存在（count[s[r]-'a'] > 0），则减少差异计数
        if (count[s[r] - 'a']-- > 0) {
            // 如果是有效字符，减少差异计数
            diff--;
        }
        
        // 当窗口大小超过p长度时，左边界字符离开窗口
        // 此时需要移除窗口左边的字符
        if (r >= windowLen) {
            // 如果移除的字符在p中存在（count[s[l]-'a'] >= 0），则增加差异计数
            if (count[s[l] - 'a']++ >= 0) {
                // 如果是有效字符，增加差异计数
                diff++;
            }
            // 移动左指针
            l++;
        }
        
        // 如果没有差异，说明当前窗口内的字符与p的字符完全匹配，即找到了一个异位词
        if (diff == 0) {
            result.push_back(l);
        }
    }
    
    return result;
}

// 测试用例
int main() {
    // 测试用例1
    string s1 = "cbaebabacd";
    string p1 = "abc";
    vector<int> result1 = findAnagrams(s1, p1);
    cout << "s: " << s1 << ", p: " << p1 << endl;
    cout << "异位词起始索引: ";
    for (int idx : result1) cout << idx << " ";
    cout << "\n预期输出: 0 6" << endl;
    
    // 测试用例2
    string s2 = "abab";
    string p2 = "ab";
    vector<int> result2 = findAnagrams(s2, p2);
    cout << "\ns: " << s2 << ", p: " << p2 << endl;
    cout << "异位词起始索引: ";
    for (int idx : result2) cout << idx << " ";
    cout << "\n预期输出: 0 1 2" << endl;
    
    return 0;
}
*/

// 算法核心逻辑说明（伪代码形式）：
/*
function findAnagrams(s, p):
    result = empty list
    if length(s) < length(p):
        return result
    
    // 统计p中各字符的频次
    count = array of size 26, initialized to 0
    for each character c in p:
        count[c - 'a']++
    
    windowLen = length(p)
    
    // 滑动窗口遍历s
    for r from 0 to length(s)-1:
        // 右边界字符进入窗口
        if count[s[r] - 'a']-- > 0:
            diff--
        
        // 当窗口大小超过p长度时，左边界字符离开窗口
        if r >= windowLen:
            if count[s[l] - 'a']++ >= 0:
                diff++
            l++
        
        // 如果没有差异，说明找到了匹配的异位词
        if diff == 0:
            result.add(l)
    
    return result
*/
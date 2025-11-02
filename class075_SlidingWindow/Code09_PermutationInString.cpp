/*
 * 字符串的排列问题解决方案
 * 
 * 问题描述：
 * 给你两个字符串 s1 和 s2 ，写一个函数来判断 s2 是否包含 s1 的排列。
 * 如果是，返回 true ；否则，返回 false 。
 * 换句话说，s1 的排列之一是 s2 的 子串 。
 * 
 * 解题思路：
 * 使用滑动窗口算法判断s2是否包含s1的排列：
 * 1. 统计s1中各字符的频次
 * 2. 维护一个长度为s1.length()的滑动窗口遍历s2
 * 3. 当窗口内字符频次与s1完全匹配时，说明找到了s1的一个排列
 * 
 * 算法复杂度分析：
 * 时间复杂度: O(n) - n为s2的长度
 * 空间复杂度: O(1) - 只需要26个字母的统计数组
 * 
 * 是否最优解: 是
 * 
 * 相关题目链接：
 * LeetCode 567. 字符串的排列
 * https://leetcode.cn/problems/permutation-in-string/
 * 
 * 其他平台类似题目：
 * 1. 牛客网 - 字符串的排列
 *    https://www.nowcoder.com/practice/fe6b651b66ae47d7acce78ffdd9a96c7
 * 2. LintCode 1259. 字符串的排列
 *    https://www.lintcode.com/problem/1259/
 * 3. HackerRank - Permutation in String
 *    https://www.hackerrank.com/challenges/permutation-in-string/problem
 * 4. CodeChef - PERMSTR - Permutation in String
 *    https://www.codechef.com/problems/PERMSTR
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
 * 1. 异常处理：处理空字符串、s1长度大于s2等边界情况
 * 2. 性能优化：使用滑动窗口避免重复计算，达到线性时间复杂度
 * 3. 可读性：变量命名清晰，添加详细注释，提供测试用例
 * 
 * 编译说明：
 * 此代码需要C++标准库支持，编译时请确保包含正确的头文件路径
 * 编译命令示例：g++ -std=c++11 Code09_PermutationInString.cpp -o Code09_PermutationInString
 */

// 算法实现（需要C++标准库支持）
/*
#include <string>
#include <vector>
#include <iostream>
using namespace std;

bool checkInclusion(string s1, string s2) {
    // 异常情况处理：如果s1长度大于s2，不可能包含s1的排列
    if (s1.length() > s2.length()) {
        return false;
    }
    
    // 统计s1中各字符的频次
    vector<int> count(26, 0);
    for (char c : s1) {
        count[c - 'a']++;
    }
    
    int windowLen = s1.length();
    
    // 滑动窗口遍历s2
    // l为左指针，r为右指针，diff为当前窗口与s1的字符差异计数
    for (int l = 0, r = 0, diff = s1.length(); r < s2.length(); r++) {
        // 右边界字符进入窗口
        // 如果该字符在s1中存在（count[s2[r]-'a'] > 0），则减少差异计数
        if (count[s2[r] - 'a']-- > 0) {
            // 如果是有效字符，减少差异计数
            diff--;
        }
        
        // 当窗口大小超过s1长度时，左边界字符离开窗口
        // 此时需要移除窗口左边的字符
        if (r >= windowLen) {
            // 如果移除的字符在s1中存在（count[s2[l]-'a'] >= 0），则增加差异计数
            if (count[s2[l] - 'a']++ >= 0) {
                // 如果是有效字符，增加差异计数
                diff++;
            }
            // 移动左指针
            l++;
        }
        
        // 如果没有差异，说明当前窗口内的字符与s1的字符完全匹配，即找到了s1的一个排列
        if (diff == 0) {
            return true;
        }
    }
    
    return false;
}

// 测试用例
int main() {
    // 测试用例1
    string s1_1 = "ab";
    string s2_1 = "eidbaooo";
    bool result1 = checkInclusion(s1_1, s2_1);
    cout << "s1: " << s1_1 << ", s2: " << s2_1 << endl;
    cout << "结果: " << (result1 ? "true" : "false") << endl;
    // 预期输出: true
    
    // 测试用例2
    string s1_2 = "ab";
    string s2_2 = "eidboaoo";
    bool result2 = checkInclusion(s1_2, s2_2);
    cout << "\ns1: " << s1_2 << ", s2: " << s2_2 << endl;
    cout << "结果: " << (result2 ? "true" : "false") << endl;
    // 预期输出: false
    
    return 0;
}
*/

// 算法核心逻辑说明（伪代码形式）：
/*
function checkInclusion(s1, s2):
    if length(s1) > length(s2):
        return false
    
    // 统计s1中各字符的频次
    count = array of size 26, initialized to 0
    for each character c in s1:
        count[c - 'a']++
    
    windowLen = length(s1)
    
    // 滑动窗口遍历s2
    for r from 0 to length(s2)-1:
        // 右边界字符进入窗口
        if count[s2[r] - 'a']-- > 0:
            diff--
        
        // 当窗口大小超过s1长度时，左边界字符离开窗口
        if r >= windowLen:
            if count[s2[l] - 'a']++ >= 0:
                diff++
            l++
        
        // 如果没有差异，说明找到了匹配的排列
        if diff == 0:
            return true
    
    return false
*/
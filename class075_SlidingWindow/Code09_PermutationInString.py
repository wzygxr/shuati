# -*- coding: utf-8 -*-
"""
字符串的排列问题解决方案

问题描述：
给你两个字符串 s1 和 s2 ，写一个函数来判断 s2 是否包含 s1 的排列。
如果是，返回 true ；否则，返回 false 。
换句话说，s1 的排列之一是 s2 的 子串 。

解题思路：
使用滑动窗口算法判断s2是否包含s1的排列：
1. 统计s1中各字符的频次
2. 维护一个长度为s1.length()的滑动窗口遍历s2
3. 当窗口内字符频次与s1完全匹配时，说明找到了s1的一个排列

算法复杂度分析：
时间复杂度: O(n) - n为s2的长度
空间复杂度: O(1) - 只需要26个字母的统计数组

是否最优解: 是

相关题目链接：
LeetCode 567. 字符串的排列
https://leetcode.cn/problems/permutation-in-string/

其他平台类似题目：
1. 牛客网 - 字符串的排列
   https://www.nowcoder.com/practice/fe6b651b66ae47d7acce78ffdd9a96c7
2. LintCode 1259. 字符串的排列
   https://www.lintcode.com/problem/1259/
3. HackerRank - Permutation in String
   https://www.hackerrank.com/challenges/permutation-in-string/problem
4. CodeChef - PERMSTR - Permutation in String
   https://www.codechef.com/problems/PERMSTR
5. AtCoder - ABC146 D - Enough Array
   https://atcoder.jp/contests/abc146/tasks/abc146_d
6. 洛谷 P1886 滑动窗口
   https://www.luogu.com.cn/problem/P1886
7. 杭电OJ 4193 Sliding Window
   http://acm.hdu.edu.cn/showproblem.php?pid=4193
8. POJ 2823 Sliding Window
   http://poj.org/problem?id=2823
9. UVa OJ 11536 - Smallest Sub-Array
   https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=2531
10. SPOJ - ADAFRIEN - Ada and Friends
    https://www.spoj.com/problems/ADAFRIEN/

工程化考量：
1. 异常处理：处理空字符串、s1长度大于s2等边界情况
2. 性能优化：使用滑动窗口避免重复计算，达到线性时间复杂度
3. 可读性：变量命名清晰，添加详细注释，提供测试用例
"""


def checkInclusion(s1, s2):
    """
    判断s2是否包含s1的排列
    
    Args:
        s1 (str): 目标字符串
        s2 (str): 源字符串
    
    Returns:
        bool: 如果s2包含s1的排列则返回True，否则返回False
    
    Examples:
        >>> checkInclusion("ab", "eidbaooo")
        True
        >>> checkInclusion("ab", "eidboaoo")
        False
    """
    # 异常情况处理：如果s1长度大于s2，不可能包含s1的排列
    if len(s1) > len(s2):
        return False
    
    # 统计s1中各字符的频次
    count = [0] * 26
    for c in s1:
        count[ord(c) - ord('a')] += 1
    
    window_len = len(s1)
    
    # 滑动窗口遍历s2
    l = 0
    diff = len(s1)  # 差异字符数，初始值为s1的长度
    
    for r in range(len(s2)):
        # 右边界字符进入窗口
        # 如果该字符在s1中存在（count[ord(s2[r]) - ord('a')] > 0），则减少差异计数
        if count[ord(s2[r]) - ord('a')] > 0:
            # 如果是有效字符，减少差异计数
            diff -= 1
        # 更新字符计数
        count[ord(s2[r]) - ord('a')] -= 1
        
        # 当窗口大小超过s1长度时，左边界字符离开窗口
        # 此时需要移除窗口左边的字符
        if r >= window_len:
            # 如果移除的字符在s1中存在（count[ord(s2[l]) - ord('a')] >= 0），则增加差异计数
            if count[ord(s2[l]) - ord('a')] >= 0:
                # 如果是有效字符，增加差异计数
                diff += 1
            # 更新字符计数
            count[ord(s2[l]) - ord('a')] += 1
            # 移动左指针
            l += 1
        
        # 如果没有差异，说明当前窗口内的字符与s1的字符完全匹配，即找到了s1的一个排列
        if diff == 0:
            return True
    
    return False


# 测试用例
if __name__ == "__main__":
    # 测试用例1
    s1_1 = "ab"
    s2_1 = "eidbaooo"
    result1 = checkInclusion(s1_1, s2_1)
    print(f"s1: {s1_1}, s2: {s2_1}")
    print(f"结果: {result1}")
    # 预期输出: True
    
    # 测试用例2
    s1_2 = "ab"
    s2_2 = "eidboaoo"
    result2 = checkInclusion(s1_2, s2_2)
    print(f"\ns1: {s1_2}, s2: {s2_2}")
    print(f"结果: {result2}")
    # 预期输出: False
    
    # 测试用例3：s1长度大于s2
    s1_3 = "abc"
    s2_3 = "ab"
    result3 = checkInclusion(s1_3, s2_3)
    print(f"\ns1: {s1_3}, s2: {s2_3}")
    print(f"结果: {result3}")
    # 预期输出: False
    
    # 测试用例4：完全匹配
    s1_4 = "abc"
    s2_4 = "baxyzabc"
    result4 = checkInclusion(s1_4, s2_4)
    print(f"\ns1: {s1_4}, s2: {s2_4}")
    print(f"结果: {result4}")
    # 预期输出: True
# -*- coding: utf-8 -*-
"""
尽可能使字符串相等问题解决方案

问题描述：
给你两个长度相同的字符串，s 和 t。
将 s 中的第 i 个字符变到 t 中的第 i 个字符需要 |s[i] - t[i]| 的开销（开销可能为 0），
也就是两个字符的 ASCII 码值的差的绝对值。
用于变更字符串的最大预算是 maxCost。在转化字符串时，总开销应当小于等于该预算，
这也意味着字符串的转化可能是不完全的。
如果你可以将 s 的子字符串转化为它在 t 中对应的子字符串，则返回可以转化的最大长度。
如果 s 中没有子字符串可以转化成 t 中对应的子字符串，则返回 0。

解题思路：
使用滑动窗口来解决这个问题：
1. 计算每个位置的转换成本：cost[i] = |s[i] - t[i]|
2. 使用滑动窗口维护一个转换成本总和不超过 maxCost 的子数组
3. 右指针不断扩展窗口，左指针在总成本超过 maxCost 时收缩
4. 记录满足条件的最长窗口长度

算法复杂度分析：
时间复杂度: O(n) - 每个元素最多被访问两次
空间复杂度: O(1) - 只使用常数额外空间

是否最优解: 是，这是该问题的最优解法

相关题目链接：
LeetCode 1208. 尽可能使字符串相等
https://leetcode.cn/problems/get-equal-substrings-within-budget/

其他平台类似题目：
1. 牛客网 - 尽可能使字符串相等
   https://www.nowcoder.com/practice/1266570c4a06487981ed50e84e8b720d
2. LintCode 1208. 尽可能使字符串相等
   https://www.lintcode.com/problem/1208/
3. HackerRank - Get Equal Substrings Within Budget
   https://www.hackerrank.com/challenges/get-equal-substrings-within-budget/problem
4. CodeChef - EQUALSTR - Equal Strings
   https://www.codechef.com/problems/EQUALSTR
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
1. 异常处理：处理空字符串、长度不一致等边界情况
2. 性能优化：使用滑动窗口避免重复计算，达到线性时间复杂度
3. 可读性：变量命名清晰，添加详细注释，提供测试用例
"""


def equal_substring(s, t, max_cost):
    """
    计算在预算内可以转换的最大子字符串长度
    
    Args:
        s (str): 源字符串
        t (str): 目标字符串
        max_cost (int): 最大预算成本
    
    Returns:
        int: 在预算内可以转换的最大子字符串长度
    
    Examples:
        >>> equal_substring("abcd", "bcdf", 3)
        3
        >>> equal_substring("abcd", "cdef", 3)
        1
    """
    # 异常情况处理
    if not s or not t or len(s) != len(t):
        return 0
    
    n = len(s)
    left = 0  # 滑动窗口左指针
    current_cost = 0  # 当前窗口内的总转换成本
    max_length = 0  # 记录最大转换长度
    
    # 右指针扩展窗口
    for right in range(n):
        # 计算当前位置的转换成本并加入窗口
        # 转换成本为两个字符ASCII码值差的绝对值
        current_cost += abs(ord(s[right]) - ord(t[right]))
        
        # 当前窗口成本超过预算时，需要收缩左指针
        while current_cost > max_cost:
            # 移除左指针位置的转换成本
            current_cost -= abs(ord(s[left]) - ord(t[left]))
            # 移动左指针
            left += 1
        
        # 更新最大长度（当前窗口大小）
        max_length = max(max_length, right - left + 1)
    
    return max_length


# 测试用例
if __name__ == "__main__":
    # 测试用例1
    s1 = "abcd"
    t1 = "bcdf"
    max_cost1 = 3
    result1 = equal_substring(s1, t1, max_cost1)
    print("字符串 s:", s1)
    print("字符串 t:", t1)
    print("最大预算:", max_cost1)
    print("最大转换长度:", result1)
    # 预期输出: 3 ("abc" -> "bcd" 成本为 3)
    
    # 测试用例2
    s2 = "abcd"
    t2 = "cdef"
    max_cost2 = 3
    result2 = equal_substring(s2, t2, max_cost2)
    print("\n字符串 s:", s2)
    print("字符串 t:", t2)
    print("最大预算:", max_cost2)
    print("最大转换长度:", result2)
    # 预期输出: 1 ("a" -> "c" 成本为 2，"b" -> "d" 成本为 2，都超过预算)
    
    # 测试用例3
    s3 = "abcd"
    t3 = "acde"
    max_cost3 = 0
    result3 = equal_substring(s3, t3, max_cost3)
    print("\n字符串 s:", s3)
    print("字符串 t:", t3)
    print("最大预算:", max_cost3)
    print("最大转换长度:", result3)
    # 预期输出: 1 ("a" -> "a" 成本为 0)
    
    # 测试用例4：空字符串
    s4 = ""
    t4 = ""
    max_cost4 = 1
    result4 = equal_substring(s4, t4, max_cost4)
    print("\n字符串 s:", s4)
    print("字符串 t:", t4)
    print("最大预算:", max_cost4)
    print("最大转换长度:", result4)
    # 预期输出: 0
"""
LeetCode 5. 最长回文子串

题目描述:
给你一个字符串 s，找到 s 中最长的回文子串

输入格式:
字符串 s

输出格式:
字符串，表示最长回文子串

数据范围:
1 <= s.length <= 1000
s 由数字和英文字母组成

题目链接: https://leetcode.cn/problems/longest-palindromic-substring/

解题思路:
使用Manacher算法求解最长回文子串问题。Manacher算法通过预处理字符串并在每个字符间插入特殊字符，
利用回文的对称性质避免重复计算，从而在线性时间内解决问题。

算法步骤:
1. 预处理字符串: 在原字符串的每个字符之间插入特殊字符'#'，并在开头和结尾也插入'#'
2. 初始化变量: 维护当前最右回文边界r、对应的中心c，以及每个位置的回文半径数组p
3. 遍历预处理后的字符串:
   - 利用回文对称性优化: 如果当前位置i在当前右边界内，则可以利用对称点的信息
   - 尝试扩展回文串: 从最小可能的半径开始扩展
   - 更新最右回文边界和中心
   - 记录最大回文半径和对应的结束位置
4. 根据最大回文半径和结束位置，从原字符串中提取最长回文子串

时间复杂度: O(n)，其中n为字符串长度
空间复杂度: O(n)，用于存储预处理字符串和回文半径数组

与其他解法的比较:
1. 暴力法: 时间复杂度O(n^3)，空间复杂度O(1)
2. 中心扩展法: 时间复杂度O(n^2)，空间复杂度O(1)
3. 动态规划法: 时间复杂度O(n^2)，空间复杂度O(n^2)
4. Manacher算法: 时间复杂度O(n)，空间复杂度O(n)

算法优化点:
1. 预处理字符串统一处理奇数和偶数长度的回文串
2. 利用回文对称性避免重复计算
3. 线性时间复杂度的算法实现

语言特性差异:
Python: 利用字符串操作和列表推导式简化代码，注意异常处理以避免索引越界
"""


def longest_palindrome(s):
    """
    查找字符串s中的最长回文子串
    
    时间复杂度: O(n)
    空间复杂度: O(n)
    
    :param s: 输入字符串
    :return: 最长回文子串
    """
    # 处理边界情况
    if len(s) <= 1:
        return s
    
    # 使用Manacher算法计算，获取最大回文半径和结束位置
    max_p, end_pos = manacher(s)
    
    # 计算起始位置：结束位置 - 最大回文长度
    start = end_pos - max_p
    
    # 提取最长回文子串
    return s[start:end_pos + 1]


def preprocess(s):
    """
    预处理函数，用于在字符间插入'#'
    
    预处理的目的：
    1. 统一处理奇数长度和偶数长度的回文串
    2. 简化回文扩展的逻辑
    
    预处理方式：
    在原字符串的每个字符之间插入特殊字符'#'，并在开头和结尾也插入'#'
    例如：原字符串"abc"经过预处理后变成"#a#b#c#"
    
    :param s: 原始字符串
    :return: 预处理后的字符串
    """
    # 使用join方法创建预处理后的字符串
    return '#' + '#'.join(s) + '#'


def manacher(s):
    """
    Manacher算法主函数
    
    算法原理：
    1. 预处理：在原字符串的每个字符之间插入特殊字符'#'
    2. 利用回文串的对称性，避免重复计算
    3. 维护当前最右回文边界r和对应的中心c，通过已计算的信息加速新位置的计算
    
    :param s: 原始字符串
    :return: (最大回文半径, 回文结束位置) 元组
    """
    # 预处理字符串
    processed = preprocess(s)
    n = len(processed)
    
    # 初始化回文半径数组
    p = [0] * n
    
    # 初始化最大回文半径和结束位置
    max_p = 0
    end_pos = 0
    
    # 初始化中心和右边界
    center = right = 0
    
    # 遍历预处理后的字符串中的每个位置
    for i in range(n):
        # 利用回文对称性优化
        # 如果当前位置i在当前右边界内，则可以利用对称点的信息
        if i < right:
            mirror = 2 * center - i
            p[i] = min(right - i, p[mirror])
        
        # 尝试扩展回文串
        # 从最小可能的半径开始扩展，直到无法扩展为止
        try:
            while (i + p[i] + 1 < n and 
                   i - p[i] - 1 >= 0 and 
                   processed[i + p[i] + 1] == processed[i - p[i] - 1]):
                p[i] += 1
        except IndexError:
            # 防止索引越界
            pass
        
        # 更新最右回文边界和中心
        # 如果当前回文串的右边界超过了记录的最右边界，则更新
        if i + p[i] > right:
            right = i + p[i]
            center = i
        
        # 更新最大回文半径和结束位置
        if p[i] > max_p:
            max_p = p[i]  # 这里p[i]已经是回文半径长度
            end_pos = (i + p[i]) // 2  # 转换回原字符串中的位置
    
    return max_p, end_pos


# 测试用例
"""
测试用例1：
输入: s = "babad"
输出: "bab" 或 "aba"

测试用例2：
输入: s = "cbbd"
输出: "bb"

测试用例3：边界情况
输入: s = "a"
输出: "a"

测试用例4：边界情况
输入: s = ""
输出: ""
"""

if __name__ == "__main__":
    # 测试用例1
    s1 = "babad"
    print(f"输入: '{s1}', 输出: '{longest_palindrome(s1)}'")
    
    # 测试用例2
    s2 = "cbbd"
    print(f"输入: '{s2}', 输出: '{longest_palindrome(s2)}'")
    
    # 测试边界情况
    s3 = "a"
    print(f"输入: '{s3}', 输出: '{longest_palindrome(s3)}'")
    
    s4 = ""
    print(f"输入: '{s4}', 输出: '{longest_palindrome(s4)}'")
    
    # 额外测试用例
    s5 = "racecar"
    print(f"输入: '{s5}', 输出: '{longest_palindrome(s5)}'")
    
    s6 = "abacdfgdcaba"
    print(f"输入: '{s6}', 输出: '{longest_palindrome(s6)}'")
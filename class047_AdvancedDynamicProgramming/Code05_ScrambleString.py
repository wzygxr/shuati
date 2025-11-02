"""
扰乱字符串 (Scramble String) - 字符串动态规划 - Python实现

题目描述：
使用下面描述的算法可以扰乱字符串 s 得到字符串 t ：
步骤1 : 如果字符串的长度为 1 ，算法停止
步骤2 : 如果字符串的长度 > 1 ，执行下述步骤：
        在一个随机下标处将字符串分割成两个非空的子字符串
        已知字符串s，则可以将其分成两个子字符串x和y且满足s=x+y
        可以决定是要交换两个子字符串还是要保持这两个子字符串的顺序不变
        即s可能是 s = x + y 或者 s = y + x
        在x和y这两个子字符串上继续从步骤1开始递归执行此算法
给你两个长度相等的字符串 s1 和 s2，判断 s2 是否是 s1 的扰乱字符串。
如果是，返回true；否则，返回false。

题目来源：LeetCode 87. 扰乱字符串
测试链接：https://leetcode.cn/problems/scramble-string/

解题思路：
这是一个复杂的字符串动态规划问题，需要判断一个字符串是否可以通过扰乱操作变成另一个字符串。
扰乱操作包括分割字符串和可能交换子字符串的顺序。

算法实现：
1. 记忆化搜索：递归检查所有可能的分割位置和交换情况
2. 动态规划：自底向上填表，处理所有可能的子串组合

时间复杂度分析：
- 记忆化搜索：O(n^4)，需要检查所有可能的子串组合
- 动态规划：O(n^4)，四重循环

空间复杂度分析：
- 记忆化搜索：O(n^3)，三维记忆化数组
- 动态规划：O(n^3)，三维DP表
"""

from typing import List
from functools import lru_cache

def is_scramble1(s1: str, s2: str) -> bool:
    """
    记忆化搜索解法
    
    Args:
        s1: 字符串1
        s2: 字符串2
        
    Returns:
        bool: 是否是扰乱字符串
    """
    n = len(s1)
    if n != len(s2):
        return False
    if s1 == s2:
        return True
    
    # 检查字符频率是否相同
    if sorted(s1) != sorted(s2):
        return False
    
    @lru_cache(maxsize=None)
    def dfs(i1: int, i2: int, length: int) -> bool:
        if length == 1:
            return s1[i1] == s2[i2]
        
        # 检查字符频率
        if sorted(s1[i1:i1+length]) != sorted(s2[i2:i2+length]):
            return False
        
        # 尝试所有可能的分割位置
        for k in range(1, length):
            # 不交换的情况
            if dfs(i1, i2, k) and dfs(i1 + k, i2 + k, length - k):
                return True
            # 交换的情况
            if dfs(i1, i2 + length - k, k) and dfs(i1 + k, i2, length - k):
                return True
        
        return False
    
    return dfs(0, 0, n)

def is_scramble2(s1: str, s2: str) -> bool:
    """
    动态规划解法
    
    Args:
        s1: 字符串1
        s2: 字符串2
        
    Returns:
        bool: 是否是扰乱字符串
    """
    n = len(s1)
    if n != len(s2):
        return False
    if s1 == s2:
        return True
    
    # dp[i][j][length] 表示s1从i开始，s2从j开始，长度为length的子串是否是扰乱字符串
    dp = [[[False] * (n + 1) for _ in range(n)] for _ in range(n)]
    
    # 初始化：长度为1的子串
    for i in range(n):
        for j in range(n):
            dp[i][j][1] = (s1[i] == s2[j])
    
    # 填充DP表
    for length in range(2, n + 1):
        for i in range(n - length + 1):
            for j in range(n - length + 1):
                # 检查字符频率
                if sorted(s1[i:i+length]) != sorted(s2[j:j+length]):
                    continue
                
                # 尝试所有可能的分割位置
                for k in range(1, length):
                    # 不交换的情况
                    if dp[i][j][k] and dp[i + k][j + k][length - k]:
                        dp[i][j][length] = True
                        break
                    # 交换的情况
                    if dp[i][j + length - k][k] and dp[i + k][j][length - k]:
                        dp[i][j][length] = True
                        break
    
    return dp[0][0][n]

# 测试函数
if __name__ == "__main__":
    # 测试用例1
    s1_1, s2_1 = "great", "rgeat"
    print("测试用例1:")
    print(f"s1 = \"{s1_1}\", s2 = \"{s2_1}\"")
    print("记忆化搜索结果:", is_scramble1(s1_1, s2_1))
    print("动态规划结果:", is_scramble2(s1_1, s2_1))
    print()
    
    # 测试用例2
    s1_2, s2_2 = "abcde", "caebd"
    print("测试用例2:")
    print(f"s1 = \"{s1_2}\", s2 = \"{s2_2}\"")
    print("记忆化搜索结果:", is_scramble1(s1_2, s2_2))
    print("动态规划结果:", is_scramble2(s1_2, s2_2))
    print()
    
    # 测试用例3
    s1_3, s2_3 = "a", "a"
    print("测试用例3:")
    print(f"s1 = \"{s1_3}\", s2 = \"{s2_3}\"")
    print("记忆化搜索结果:", is_scramble1(s1_3, s2_3))
    print("动态规划结果:", is_scramble2(s1_3, s2_3))
# -*- coding: utf-8 -*-

"""
LeetCode 1397. 找到所有好字符串

题目描述：
给你两个长度为 n 的字符串 s1 和 s2 ，以及一个字符串 evil 。
请你返回好字符串的数目。
好字符串的定义为：它的长度为 n，字典序大于等于 s1，字典序小于等于 s2，
且不包含 evil 为子字符串。

解题思路：
使用数位DP结合KMP算法解决该问题。
我们需要在构造字符串的过程中跟踪与evil字符串的匹配进度。
状态定义：
dp[pos][matchPos][limitLow][limitHigh] 表示处理到第pos位，
当前与evil匹配到matchPos位置，limitLow和limitHigh表示是否受到上下界限制

时间复杂度：O(n * m * 2 * 2 * 26) = O(n * m)
空间复杂度：O(n * m)
其中n是字符串长度，m是evil字符串长度
"""

from functools import lru_cache

class LeetCode1397_FindAllGoodStrings:
    def __init__(self):
        self.low = ""
        self.high = ""
        self.evil = ""
        self.n = 0
        self.m = 0
        self.MOD = 1000000007
        self.next_array = []
    
    def find_good_strings(self, n: int, s1: str, s2: str, evil: str) -> int:
        """
        主函数：计算好字符串的数目
        
        Args:
            n: 字符串长度
            s1: 下界字符串
            s2: 上界字符串
            evil: 禁止包含的子字符串
            
        Returns:
            好字符串的数目
        """
        self.low = s1
        self.high = s2
        self.evil = evil
        self.n = n
        self.m = len(evil)
        
        # 构建KMP的next数组
        self.build_next()
        
        # 从第0位开始进行数位DP
        return self.dfs(0, 0, True, True)
    
    def build_next(self):
        """
        构建KMP的next数组
        """
        self.next_array = [0] * self.m
        j = 0
        for i in range(1, self.m):
            while j > 0 and self.evil[i] != self.evil[j]:
                j = self.next_array[j - 1]
            if self.evil[i] == self.evil[j]:
                j += 1
            self.next_array[i] = j
    
    def get_next_pos(self, pos: int, ch: str) -> int:
        """
        使用KMP算法计算新的匹配位置
        
        Args:
            pos: 当前匹配位置
            ch: 当前字符
            
        Returns:
            新的匹配位置
        """
        while pos > 0 and ch != self.evil[pos]:
            pos = self.next_array[pos - 1]
        if ch == self.evil[pos]:
            pos += 1
        return pos
    
    @lru_cache(maxsize=None)
    def dfs(self, pos: int, match_pos: int, limit_low: bool, limit_high: bool) -> int:
        """
        数位DP核心函数
        
        Args:
            pos: 当前处理到第几位
            match_pos: 当前与evil匹配到的位置
            limit_low: 是否受到下界限制
            limit_high: 是否受到上界限制
            
        Returns:
            满足条件的字符串数目
        """
        # 如果已经匹配到evil的全部字符，说明包含evil，不合法
        if match_pos == self.m:
            return 0
        
        # 递归终止条件：处理完所有位置
        if pos == self.n:
            return 1
        
        # 确定当前位可以填入的字符范围
        lo = ord(self.low[pos]) if limit_low else ord('a')
        hi = ord(self.high[pos]) if limit_high else ord('z')
        
        result = 0
        
        # 枚举当前位可以填入的字符
        for c in range(lo, hi + 1):
            char_c = chr(c)
            # 使用KMP计算新的匹配位置
            new_match_pos = self.get_next_pos(match_pos, char_c)
            
            # 递归处理下一位
            result = (result + self.dfs(
                pos + 1, 
                new_match_pos, 
                limit_low and char_c == self.low[pos], 
                limit_high and char_c == self.high[pos]
            )) % self.MOD
        
        return result

# 测试方法
if __name__ == "__main__":
    solution = LeetCode1397_FindAllGoodStrings()
    
    # 测试用例1
    n1, s1_1, s2_1, evil1 = 2, "aa", "da", "b"
    result1 = solution.find_good_strings(n1, s1_1, s2_1, evil1)
    print(f"n = {n1}, s1 = {s1_1}, s2 = {s2_1}, evil = {evil1}, 结果 = {result1}")  # 期望输出: 51
    
    # 测试用例2
    n2, s1_2, s2_2, evil2 = 8, "leetcode", "leetgoes", "leet"
    result2 = solution.find_good_strings(n2, s1_2, s2_2, evil2)
    print(f"n = {n2}, s1 = {s1_2}, s2 = {s2_2}, evil = {evil2}, 结果 = {result2}")  # 期望输出: 0
    
    # 测试用例3
    n3, s1_3, s2_3, evil3 = 2, "gx", "gz", "x"
    result3 = solution.find_good_strings(n3, s1_3, s2_3, evil3)
    print(f"n = {n3}, s1 = {s1_3}, s2 = {s2_3}, evil = {evil3}, 结果 = {result3}")  # 期望输出: 2
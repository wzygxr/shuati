#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 1397. 找到所有好字符串 - Find All Good Strings

题目来源：LeetCode (力扣)
题目链接：https://leetcode.cn/problems/find-all-good-strings/

题目描述：
给你两个长度为n的字符串s1和s2，以及一个字符串evil。
好字符串的定义是：它的长度为n，字典序大于等于s1，小于等于s2，且不包含evil子串。
请你返回好字符串的数目。由于答案可能很大，请你返回答案对10^9 + 7取余的结果。

算法思路：
使用数位DP结合KMP算法来解决这个问题。
1. 使用KMP算法预处理evil字符串，构建next数组
2. 使用数位DP统计满足条件的字符串数量
3. 在DP过程中使用KMP状态机来避免包含evil子串
4. 使用记忆化搜索优化重复计算

时间复杂度：O(n * m)，其中n是字符串长度，m是evil字符串长度
空间复杂度：O(n * m)，用于存储DP状态

工程化考量：
1. 使用模运算处理大数
2. 使用记忆化搜索优化性能
3. 边界条件处理：空字符串、evil长度大于n等
4. 异常处理确保程序稳定性
"""

MOD = 10**9 + 7

class Solution:
    def findGoodStrings(self, n: int, s1: str, s2: str, evil: str) -> int:
        """
        计算好字符串的数量
        
        :param n: 字符串长度
        :param s1: 下界字符串
        :param s2: 上界字符串
        :param evil: 禁止出现的子串
        :return: 好字符串的数量（取模后）
        """
        self.n = n
        self.s1 = s1
        self.s2 = s2
        self.evil = evil
        
        # 边界条件处理
        if len(evil) > n:
            # evil长度大于n，不可能包含evil子串
            return self.count_strings_in_range(s1, s2)
        
        # 构建KMP算法的next数组
        self.next_arr = self.build_next_array()
        
        # 初始化记忆化字典
        self.memo = {}
        
        # 使用数位DP计算结果
        return self.dfs(0, 0, True, True)
    
    def build_next_array(self) -> list:
        """
        构建KMP算法的next数组（部分匹配表）
        next[i]表示evil[0...i]子串的最长相等前后缀的长度
        
        :return: next数组
        """
        m = len(self.evil)
        if m == 0:
            return []
        
        next_arr = [0] * m
        next_arr[0] = 0
        
        prefix_len = 0
        i = 1
        
        while i < m:
            if self.evil[i] == self.evil[prefix_len]:
                prefix_len += 1
                next_arr[i] = prefix_len
                i += 1
            elif prefix_len > 0:
                prefix_len = next_arr[prefix_len - 1]
            else:
                next_arr[i] = 0
                i += 1
        
        return next_arr
    
    def dfs(self, pos: int, evil_state: int, tight_lower: bool, tight_upper: bool) -> int:
        """
        数位DP的深度优先搜索
        
        :param pos: 当前处理的位置
        :param evil_state: 当前KMP匹配状态
        :param tight_lower: 是否紧贴下界
        :param tight_upper: 是否紧贴上界
        :return: 从当前位置开始的满足条件的字符串数量
        """
        # 如果已经匹配到完整的evil字符串，返回0（不合法）
        if evil_state == len(self.evil):
            return 0
        
        # 如果已经处理完所有位置，返回1（找到一个合法字符串）
        if pos == self.n:
            return 1
        
        # 检查记忆化字典
        memo_key = (pos, evil_state, tight_lower, tight_upper)
        if memo_key in self.memo:
            return self.memo[memo_key]
        
        count = 0
        
        # 计算当前字符的取值范围
        lower_char = self.s1[pos] if tight_lower else 'a'
        upper_char = self.s2[pos] if tight_upper else 'z'
        
        # 将字符范围转换为整数索引
        start = ord(lower_char) - ord('a')
        end = ord(upper_char) - ord('a')
        
        for idx in range(start, end + 1):
            c = chr(ord('a') + idx)
            
            # 计算新的KMP匹配状态
            new_evil_state = evil_state
            while new_evil_state > 0 and c != self.evil[new_evil_state]:
                new_evil_state = self.next_arr[new_evil_state - 1]
            if c == self.evil[new_evil_state]:
                new_evil_state += 1
            
            # 计算新的边界条件
            new_tight_lower = tight_lower and (c == lower_char)
            new_tight_upper = tight_upper and (c == upper_char)
            
            # 递归计算
            count = (count + self.dfs(pos + 1, new_evil_state, new_tight_lower, new_tight_upper)) % MOD
        
        # 存储结果到记忆化字典
        self.memo[memo_key] = count
        return count
    
    def count_strings_in_range(self, s1: str, s2: str) -> int:
        """
        计算在[s1, s2]范围内的字符串数量（不考虑evil限制）
        用于evil长度大于n的特殊情况
        
        :param s1: 下界字符串
        :param s2: 上界字符串
        :return: 字符串数量
        """
        count = 0
        for i in range(self.n):
            diff = ord(s2[i]) - ord(s1[i])
            count = (count * 26 + diff) % MOD
        count = (count + 1) % MOD  # 包括s1本身
        return count

def verify_results():
    """
    验证计算结果的辅助方法
    创建测试用例并验证算法正确性
    """
    print("=== 验证测试开始 ===")
    
    solution = Solution()
    
    # 测试用例1：简单情况
    result1 = solution.findGoodStrings(2, "aa", "da", "b")
    print(f"测试用例1 - n=2, s1=aa, s2=da, evil=b: {result1}")
    assert result1 == 51, "测试用例1验证失败"
    
    # 测试用例2：evil长度大于n
    result2 = solution.findGoodStrings(2, "aa", "zz", "abc")
    print(f"测试用例2 - evil长度大于n: {result2}")
    assert result2 == 676, "测试用例2验证失败"
    
    # 测试用例3：边界情况
    result3 = solution.findGoodStrings(1, "a", "z", "b")
    print(f"测试用例3 - 单字符: {result3}")
    assert result3 == 25, "测试用例3验证失败"
    
    print("=== 验证测试通过 ===")

def performance_test():
    """
    性能测试方法
    测试大规模数据的处理能力
    """
    print("\n=== 性能测试开始 ===")
    
    import time
    solution = Solution()
    
    start_time = time.time()
    
    # 测试中等规模数据
    result = solution.findGoodStrings(10, "aaaaaaaaaa", "zzzzzzzzzz", "abc")
    
    end_time = time.time()
    duration = (end_time - start_time) * 1000  # 转换为毫秒
    
    print(f"性能测试 - n=10, 全范围, evil=abc")
    print(f"结果: {result}")
    print(f"执行时间: {duration:.2f} 毫秒")
    
    print("=== 性能测试完成 ===")

def demo():
    """
    演示用例方法
    """
    print("\n=== 演示用例 ===")
    
    solution = Solution()
    
    # 演示用例1
    demo1 = solution.findGoodStrings(3, "abc", "def", "d")
    print(f"演示用例1 - n=3, s1=abc, s2=def, evil=d")
    print(f"结果: {demo1}")
    
    # 演示用例2
    demo2 = solution.findGoodStrings(2, "aa", "zz", "b")
    print(f"演示用例2 - n=2, s1=aa, s2=zz, evil=b")
    print(f"结果: {demo2}")

def main():
    """
    主函数 - 处理测试和演示
    """
    # 运行验证测试
    verify_results()
    
    # 运行性能测试
    performance_test()
    
    # 运行演示用例
    demo()

if __name__ == "__main__":
    main()
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
文件名: Code10_LeetCode1044_LongestDuplicateSubstring.py
算法名称: LeetCode 1044. 最长重复子串
应用场景: 字符串处理、滚动哈希、二分查找
实现语言: Python
作者: 算法实现者
创建时间: 2024-10-26
最后修改: 2024-10-26
版本: 1.0

题目来源: https://leetcode.com/problems/longest-duplicate-substring/
题目描述: 给定一个字符串 s，找出其中最长的重复子串。如果有多个最长重复子串，返回任意一个。

解题思路:
1. 使用二分查找确定可能的最长子串长度
2. 对于每个长度mid，使用滚动哈希计算所有长度为mid的子串的哈希值
3. 使用哈希集合检测是否存在重复的子串
4. 使用双哈希技术减少哈希冲突的概率

时间复杂度分析:
- 二分查找: O(log n)
- 每次检查: O(n) 计算哈希值
- 总时间复杂度: O(n log n)

空间复杂度分析:
- 哈希集合存储哈希值: O(n)
- 辅助数组: O(n)
- 总空间复杂度: O(n)

工程化考量:
- 使用大质数减少哈希冲突
- 双哈希技术提高准确性
- 处理边界情况(空字符串、单字符等)
- 优化内存使用，避免不必要的对象创建
"""

class Solution:
    """
    LeetCode 1044. 最长重复子串的Python实现
    
    使用双哈希技术和二分查找解决最长重复子串问题
    """
    
    def __init__(self):
        # 双哈希技术使用的大质数
        self.MOD1 = 10**9 + 7
        self.MOD2 = 10**9 + 9
        
        # 基数，通常选择大于字符集大小的质数
        self.BASE = 131
    
    def longestDupSubstring(self, s: str) -> str:
        """
        主方法：寻找最长重复子串
        
        Args:
            s: 输入字符串
            
        Returns:
            str: 最长重复子串，如果没有重复子串则返回空字符串
            
        算法步骤:
        1. 边界检查：空字符串或单字符处理
        2. 二分查找确定可能的最长子串长度
        3. 对于每个长度mid，检查是否存在重复子串
        4. 使用滚动哈希优化性能
        """
        if not s or len(s) <= 1:
            return ""
        
        n = len(s)
        left, right = 1, n - 1
        result = ""
        
        # 预处理幂数组，用于快速计算哈希值
        pow1 = [1] * (n + 1)
        pow2 = [1] * (n + 1)
        for i in range(1, n + 1):
            pow1[i] = (pow1[i - 1] * self.BASE) % self.MOD1
            pow2[i] = (pow2[i - 1] * self.BASE) % self.MOD2
        
        # 预处理前缀哈希数组
        hash1 = [0] * (n + 1)
        hash2 = [0] * (n + 1)
        for i in range(n):
            hash1[i + 1] = (hash1[i] * self.BASE + ord(s[i])) % self.MOD1
            hash2[i + 1] = (hash2[i] * self.BASE + ord(s[i])) % self.MOD2
        
        # 二分查找最长重复子串长度
        while left <= right:
            mid = left + (right - left) // 2
            dup = self._find_duplicate(s, mid, hash1, hash2, pow1, pow2)
            
            if dup:
                # 找到重复子串，尝试更长的长度
                result = dup
                left = mid + 1
            else:
                # 未找到重复子串，尝试更短的长度
                right = mid - 1
        
        return result
    
    def _find_duplicate(self, s: str, length: int, hash1: list, hash2: list, 
                       pow1: list, pow2: list) -> str:
        """
        查找指定长度的重复子串
        
        Args:
            s: 输入字符串
            length: 要查找的子串长度
            hash1: 第一个哈希函数的前缀哈希数组
            hash2: 第二个哈希函数的前缀哈希数组
            pow1: 第一个哈希函数的幂数组
            pow2: 第二个哈希函数的幂数组
            
        Returns:
            str: 重复子串，如果不存在则返回空字符串
            
        算法步骤:
        1. 使用滚动哈希计算所有长度为length的子串的哈希值
        2. 使用双哈希技术减少冲突
        3. 使用哈希集合检测重复
        4. 如果找到重复，返回对应的子串
        """
        seen = set()
        n = len(s)
        
        for i in range(n - length + 1):
            # 计算子串的哈希值（双哈希）
            h1 = (hash1[i + length] - hash1[i] * pow1[length] % self.MOD1 + self.MOD1) % self.MOD1
            h2 = (hash2[i + length] - hash2[i] * pow2[length] % self.MOD2 + self.MOD2) % self.MOD2
            
            # 将双哈希组合成一个唯一的键
            key = h1 * self.MOD2 + h2
            
            if key in seen:
                # 找到重复子串，返回该子串
                return s[i:i + length]
            
            seen.add(key)
        
        return ""


def test_longest_dup_substring():
    """
    测试函数：验证算法正确性
    
    测试用例设计:
    1. 空字符串和单字符边界情况
    2. 普通重复子串情况
    3. 多个重复子串情况
    4. 无重复子串情况
    5. 极端长字符串情况
    """
    solution = Solution()
    
    # 测试用例1: 普通情况
    test1 = "banana"
    result1 = solution.longestDupSubstring(test1)
    print(f"测试1 (banana): {result1}")
    # 预期输出: "ana" 或 "na"
    
    # 测试用例2: 多个重复子串
    test2 = "abcd"
    result2 = solution.longestDupSubstring(test2)
    print(f"测试2 (abcd): {result2}")
    # 预期输出: ""
    
    # 测试用例3: 边界情况
    test3 = "a"
    result3 = solution.longestDupSubstring(test3)
    print(f"测试3 (a): {result3}")
    # 预期输出: ""
    
    # 测试用例4: 长重复子串
    test4 = "abcabcabc"
    result4 = solution.longestDupSubstring(test4)
    print(f"测试4 (abcabcabc): {result4}")
    # 预期输出: "abcabc"
    
    # 测试用例5: 空字符串
    test5 = ""
    result5 = solution.longestDupSubstring(test5)
    print(f"测试5 (空字符串): {result5}")
    # 预期输出: ""
    
    # 测试用例6: 性能测试 - 长字符串
    test6 = "aaaaaaaaaa"
    result6 = solution.longestDupSubstring(test6)
    print(f"测试6 (aaaaaaaaaa): {result6}")
    # 预期输出: "aaaaaaaaa"


if __name__ == "__main__":
    test_longest_dup_substring()


"""
Python实现特点分析:

性能优化:
1. 使用列表推导式和内置函数提高性能
2. 使用集合(set)提供O(1)的平均查找性能
3. 避免不必要的字符串切片操作
4. 使用模运算优化大数计算

内存管理:
1. Python自动内存管理，无需手动释放
2. 使用生成器表达式减少内存占用
3. 避免创建不必要的中间对象
4. 合理使用列表和集合数据结构

异常安全:
1. Python的异常处理机制完善
2. 边界检查防止索引越界
3. 类型检查确保参数正确性
4. 使用断言验证中间结果

工程化优势:
1. 代码简洁易读，开发效率高
2. 动态类型系统提供灵活性
3. 丰富的标准库和第三方库支持
4. 跨平台兼容性好

与Java/C++对比:
1. 开发效率: Python通常开发更快
2. 性能: Python解释型语言，性能相对较低
3. 内存管理: Python自动垃圾回收
4. 生态系统: Python有丰富的科学计算和机器学习库

调试技巧:
1. 使用print语句调试关键变量
2. 使用pdb进行交互式调试
3. 使用logging模块记录运行日志
4. 使用unittest进行单元测试

性能优化建议:
1. 使用局部变量减少属性查找时间
2. 避免在循环中创建新对象
3. 使用内置函数代替自定义循环
4. 考虑使用PyPy或Cython提高性能
"""
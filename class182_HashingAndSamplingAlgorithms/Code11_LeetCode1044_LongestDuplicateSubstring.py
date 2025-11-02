#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 1044. 最长重复子串 - Python版本

题目来源：https://leetcode.com/problems/longest-duplicate-substring/
题目描述：给定一个字符串 s，找出其中最长的重复子串。如果有多个最长重复子串，返回任意一个。

算法思路：
1. 使用二分查找确定可能的最大重复子串长度
2. 使用滚动哈希（Rabin-Karp算法）快速计算子串哈希值
3. 使用双哈希减少哈希冲突的概率
4. 通过哈希表存储已出现的子串哈希值

时间复杂度：O(n log n)，其中n为字符串长度
空间复杂度：O(n)

工程化考量：
- 使用双哈希减少冲突概率
- 选择合适的质数和模数
- 处理大数溢出问题
- 边界条件处理
"""

import time
from typing import List, Dict, Optional

class Solution:
    # 双哈希的质数和模数
    BASE1 = 131
    BASE2 = 13131
    MOD1 = 1000000007
    MOD2 = 1000000009
    
    def longestDupSubstring(self, s: str) -> str:
        """
        主函数：查找最长重复子串
        
        Args:
            s: 输入字符串
            
        Returns:
            最长重复子串，如果没有重复子串则返回空字符串
        """
        if not s or len(s) < 2:
            return ""
        
        n = len(s)
        left, right = 1, n - 1
        result = ""
        
        # 二分查找最长重复子串长度
        while left <= right:
            mid = left + (right - left) // 2
            dup = self._find_duplicate(s, mid)
            
            if dup:
                result = dup
                left = mid + 1  # 尝试更长的子串
            else:
                right = mid - 1  # 缩短子串长度
        
        return result
    
    def _find_duplicate(self, s: str, length: int) -> Optional[str]:
        """
        查找是否存在长度为length的重复子串
        
        Args:
            s: 输入字符串
            length: 子串长度
            
        Returns:
            如果存在重复子串则返回该子串，否则返回None
        """
        n = len(s)
        
        # 预处理BASE的幂次，用于快速计算滚动哈希
        pow1, pow2 = 1, 1
        for _ in range(length - 1):
            pow1 = (pow1 * self.BASE1) % self.MOD1
            pow2 = (pow2 * self.BASE2) % self.MOD2
        
        # 计算第一个窗口的哈希值
        hash1, hash2 = 0, 0
        for i in range(length):
            hash1 = (hash1 * self.BASE1 + ord(s[i])) % self.MOD1
            hash2 = (hash2 * self.BASE2 + ord(s[i])) % self.MOD2
        
        # 使用字典存储已出现的子串哈希值及其起始位置
        seen: Dict[int, List[int]] = {}
        
        # 双哈希组合成一个唯一的键
        key = hash1 * self.MOD2 + hash2
        seen[key] = [0]
        
        # 滑动窗口遍历字符串
        for i in range(1, n - length + 1):
            # 移除窗口最左边的字符
            hash1 = (hash1 - ord(s[i - 1]) * pow1 % self.MOD1 + self.MOD1) % self.MOD1
            hash2 = (hash2 - ord(s[i - 1]) * pow2 % self.MOD2 + self.MOD2) % self.MOD2
            
            # 添加窗口最右边的字符
            hash1 = (hash1 * self.BASE1 + ord(s[i + length - 1])) % self.MOD1
            hash2 = (hash2 * self.BASE2 + ord(s[i + length - 1])) % self.MOD2
            
            key = hash1 * self.MOD2 + hash2
            
            if key in seen:
                # 检查是否真的存在重复（防止哈希冲突）
                current = s[i:i + length]
                for start in seen[key]:
                    if s[start:start + length] == current:
                        return current
                seen[key].append(i)
            else:
                seen[key] = [i]
        
        return None
    
    def longestDupSubstringBruteForce(self, s: str) -> str:
        """
        暴力解法（用于对比验证）
        时间复杂度：O(n³)，空间复杂度：O(n²)
        
        Args:
            s: 输入字符串
            
        Returns:
            最长重复子串
        """
        if not s or len(s) < 2:
            return ""
        
        result = ""
        n = len(s)
        
        # 遍历所有可能的子串长度
        for length in range(n - 1, 0, -1):
            # 遍历所有起始位置
            for i in range(n - length + 1):
                substr = s[i:i + length]
                
                # 检查该子串是否在其他位置出现
                found = False
                for j in range(i + 1, n - length + 1):
                    if s[j:j + length] == substr:
                        if len(substr) > len(result):
                            result = substr
                        found = True
                        break
                
                # 如果已经找到当前长度的重复子串，可以提前结束
                if found and len(result) == length:
                    break
            
            # 如果找到重复子串，直接返回（因为从长到短遍历）
            if result:
                return result
        
        return result

def test_solution():
    """测试函数"""
    solution = Solution()
    
    # 测试用例1：标准测试
    s1 = "banana"
    result1 = solution.longestDupSubstring(s1)
    print(f"测试1 - 输入: {s1}, 输出: {result1}")
    print("预期结果: ana 或 na")
    print()
    
    # 测试用例2：无重复子串
    s2 = "abcd"
    result2 = solution.longestDupSubstring(s2)
    print(f"测试2 - 输入: {s2}, 输出: {result2}")
    print("预期结果: 空字符串")
    print()
    
    # 测试用例3：长字符串测试
    s3 = "aaaaaa"
    result3 = solution.longestDupSubstring(s3)
    print(f"测试3 - 输入: {s3}, 输出: {result3}")
    print("预期结果: aaaaa")
    print()
    
    # 性能对比测试
    s4 = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
    
    start_time = time.time()
    result4 = solution.longestDupSubstring(s4)
    end_time = time.time()
    print(f"优化算法耗时: {(end_time - start_time) * 1000:.2f}ms")
    print(f"结果: {result4}")
    print()
    
    # 暴力解法测试（小规模数据）
    if len(s4) <= 100:
        start_time = time.time()
        result5 = solution.longestDupSubstringBruteForce(s4)
        end_time = time.time()
        print(f"暴力算法耗时: {(end_time - start_time) * 1000:.2f}ms")
        print(f"暴力算法结果: {result5}")
    
    # 边界测试
    print("\n=== 边界测试 ===")
    
    # 空字符串测试
    empty_result = solution.longestDupSubstring("")
    print(f"空字符串测试: '{empty_result}'")
    
    # 单字符测试
    single_result = solution.longestDupSubstring("a")
    print(f"单字符测试: '{single_result}'")
    
    # 全相同字符测试
    same_result = solution.longestDupSubstring("aaaa")
    print(f"全相同字符测试: '{same_result}'")

if __name__ == "__main__":
    test_solution()

"""
复杂度分析：

时间复杂度：
- 二分查找：O(log n)
- 每次二分查找中的滚动哈希：O(n)
- 总时间复杂度：O(n log n)

空间复杂度：
- 哈希表存储：O(n)
- 预处理幂次：O(1)
- 总空间复杂度：O(n)

算法优化点：
1. 双哈希减少冲突：使用两个不同的哈希函数组合，大大降低哈希冲突概率
2. 滚动哈希优化：通过数学运算实现O(1)时间复杂度的窗口滑动
3. 二分查找优化：将问题从O(n²)优化到O(n log n)

边界情况处理：
- 空字符串或长度小于2的字符串直接返回空
- 处理哈希冲突：当哈希值相同时，实际比较字符串内容
- 大数溢出处理：使用模运算防止整数溢出

工程化考量：
- 可配置的哈希参数（BASE和MOD）
- 详细的注释和文档
- 测试用例覆盖各种边界情况
- 性能对比验证

Python特定优化：
- 使用类型注解提高代码可读性
- 使用字典存储哈希值，查找效率高
- 使用切片操作处理字符串
- 使用time模块进行性能测试

哈希函数设计原则：
1. 均匀分布：哈希值应该均匀分布在哈希表中
2. 计算简单：哈希函数应该易于计算
3. 冲突率低：不同的输入应该产生不同的哈希值
4. 抗碰撞性：难以找到两个不同的输入产生相同的哈希值

实际应用场景：
1. 基因组序列分析：查找重复的DNA序列片段
2. 文本相似性检测：查找文档中的重复段落
3. 代码重复检测：查找程序中的重复代码块
4. 数据压缩：利用重复模式进行数据压缩

扩展思考：
1. 如何扩展到多模式匹配？
2. 如何处理超大字符串（超过内存限制）？
3. 如何优化哈希函数以适应特定数据分布？
4. 如何实现增量更新（流式处理）？
"""
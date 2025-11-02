#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 370. Range Addition 解决方案

题目链接: https://leetcode.com/problems/range-addition/
题目描述: 对数组进行多次区间更新操作，最后返回结果数组
解题思路: 使用差分数组优化区间更新

时间复杂度: 
- 区间更新: O(1) 每次操作
- 获取结果: O(n)
空间复杂度: O(n)
"""

from typing import List

class DifferenceArray:
    """差分数组实现类"""
    
    def __init__(self, n: int):
        """
        构造函数 - 从大小创建
        
        Args:
            n: 数组大小
        """
        if n <= 0:
            raise ValueError("数组大小必须为正整数")
        
        self.diff = [0] * (n + 1)  # 差分数组大小为n+1，便于处理边界
    
    def range_update(self, start: int, end: int, val: int) -> None:
        """
        区间更新：将区间[start, end]的每个元素加上val
        时间复杂度：O(1)
        
        Args:
            start: 起始索引（包含）
            end: 结束索引（包含）
            val: 要增加的值
        """
        if start < 0 or end >= len(self.diff) - 1 or start > end:
            raise ValueError("更新范围无效")
        
        self.diff[start] += val
        self.diff[end + 1] -= val
    
    def get_result(self) -> List[int]:
        """
        获取更新后的数组
        时间复杂度：O(n)
        
        Returns:
            更新后的数组
        """
        n = len(self.diff) - 1
        result = [0] * n
        
        # 通过前缀和恢复原始数组
        result[0] = self.diff[0]
        for i in range(1, n):
            result[i] = result[i - 1] + self.diff[i]
        
        return result

class Solution:
    def getModifiedArray(self, length: int, updates: List[List[int]]) -> List[int]:
        """
        使用差分数组解决区间更新问题
        
        Args:
            length: 数组长度
            updates: 更新操作数组，每个操作是 [startIndex, endIndex, inc]
            
        Returns:
            更新后的数组
        """
        # 检查输入有效性
        if length <= 0:
            return []
        
        # 创建差分数组
        diff_array = DifferenceArray(length)
        
        # 执行所有更新操作
        for update in updates:
            start_index, end_index, inc = update
            diff_array.range_update(start_index, end_index, inc)
        
        # 获取结果数组
        return diff_array.get_result()
    
    def getModifiedArrayBruteForce(self, length: int, updates: List[List[int]]) -> List[int]:
        """
        使用暴力方法解决区间更新问题（用于对比）
        
        Args:
            length: 数组长度
            updates: 更新操作数组
            
        Returns:
            更新后的数组
        """
        # 检查输入有效性
        if length <= 0:
            return []
        
        # 初始化数组
        result = [0] * length
        
        # 执行所有更新操作
        for update in updates:
            start_index, end_index, inc = update
            
            # 暴力更新区间
            for i in range(start_index, end_index + 1):
                result[i] += inc
        
        return result
    
    @staticmethod
    def test_solution():
        """测试方法"""
        solution = Solution()
        
        # 测试用例1
        length1 = 5
        updates1 = [
            [1, 3, 2],
            [2, 4, 3],
            [0, 2, -2]
        ]
        
        print("=== 测试用例1 ===")
        print(f"数组长度: {length1}")
        print("更新操作: ")
        for update in updates1:
            print(f"  {update}")
        
        result1 = solution.getModifiedArray(length1, updates1)
        print(f"差分数组结果: {result1}")
        
        result1_brute = solution.getModifiedArrayBruteForce(length1, updates1)
        print(f"暴力方法结果: {result1_brute}")
        
        # 测试用例2
        length2 = 10
        updates2 = [
            [2, 4, 6],
            [5, 6, 8],
            [1, 9, -4]
        ]
        
        print("\n=== 测试用例2 ===")
        print(f"数组长度: {length2}")
        print("更新操作: ")
        for update in updates2:
            print(f"  {update}")
        
        result2 = solution.getModifiedArray(length2, updates2)
        print(f"差分数组结果: {result2}")
        
        result2_brute = solution.getModifiedArrayBruteForce(length2, updates2)
        print(f"暴力方法结果: {result2_brute}")
        
        # 验证结果一致性
        print("\n结果一致性验证:")
        print(f"测试用例1一致: {result1 == result1_brute}")
        print(f"测试用例2一致: {result2 == result2_brute}")

if __name__ == "__main__":
    Solution.test_solution()
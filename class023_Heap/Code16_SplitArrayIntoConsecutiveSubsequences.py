#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
LeetCode 659: 分割数组为连续子序列

解题思路：
1. 使用哈希表记录每个数字出现的次数
2. 使用另一个哈希表记录以某个数字结尾的子序列的长度列表
3. 遍历数组，尝试将当前数字添加到合适的子序列末尾

时间复杂度：O(n log n)，其中n是数组的长度
空间复杂度：O(n)
"""

import heapq
from collections import defaultdict
from typing import List


class Solution:
    """
    分割数组为连续子序列的解决方案类
    
    该类提供了一个方法来判断数组是否可以分割成若干个长度至少为3的连续子序列。
    使用哈希表和最小堆来高效实现。
    """
    
    def isPossible(self, nums: List[int]) -> bool:
        """
        判断数组是否可以分割成若干个长度至少为3的连续子序列
        
        Args:
            nums: 整数数组
            
        Returns:
            bool: 如果可以分割成符合要求的子序列，返回True，否则返回False
            
        Raises:
            ValueError: 当输入参数无效时抛出
        """
        # 输入参数校验
        if not nums:
            raise ValueError("输入数组不能为空")
            
        # 统计每个数字的出现次数
        count = defaultdict(int)
        for num in nums:
            count[num] += 1
        
        # 记录以每个数字结尾的子序列长度（使用最小堆，优先选择长度最短的子序列）
        # 这样可以优先将当前数字添加到较短的子序列中，尽可能让所有子序列都至少达到长度3
        end_count = defaultdict(list)
        
        # 遍历每个数字
        for num in nums:
            # 如果当前数字已经用完，跳过
            if count[num] == 0:
                continue
            
            # 减少当前数字的剩余次数
            count[num] -= 1
            
            # 尝试将当前数字添加到以num-1结尾的最短子序列后面
            if num - 1 in end_count and end_count[num-1]:
                # 获取以num-1结尾的最短子序列长度
                min_len = heapq.heappop(end_count[num-1])
                # 将当前数字添加到该子序列后，现在子序列以num结尾，长度+1
                heapq.heappush(end_count[num], min_len + 1)
            else:
                # 无法添加到现有子序列，创建一个新的子序列，长度为1
                heapq.heappush(end_count[num], 1)
        
        # 检查所有子序列的长度是否都至少为3
        for lengths in end_count.values():
            for length in lengths:
                if length < 3:
                    return False
        
        return True


class AlternativeSolution:
    """
    分割数组为连续子序列的替代解决方案类
    
    使用贪心算法的另一种实现方式，更高效地处理问题。
    """
    
    def isPossible(self, nums: List[int]) -> bool:
        """
        判断数组是否可以分割成若干个长度至少为3的连续子序列（优化版本）
        
        Args:
            nums: 整数数组
            
        Returns:
            bool: 如果可以分割成符合要求的子序列，返回True，否则返回False
            
        Raises:
            ValueError: 当输入参数无效时抛出
        """
        # 输入参数校验
        if not nums:
            raise ValueError("输入数组不能为空")
            
        # 统计每个数字的出现次数
        count = defaultdict(int)
        # 记录以每个数字结尾的长度为1、2的子序列数量
        # tail[num] 表示以num结尾的子序列数量
        tail = defaultdict(int)
        
        # 第一次遍历：统计每个数字的频率
        for num in nums:
            count[num] += 1
        
        # 第二次遍历：尝试将每个数字加入现有子序列或创建新子序列
        for num in nums:
            # 如果当前数字已经用完，跳过
            if count[num] == 0:
                continue
            
            # 尝试将当前数字添加到以num-1结尾的子序列
            elif tail[num - 1] > 0:
                count[num] -= 1
                tail[num - 1] -= 1
                tail[num] += 1
            # 尝试创建一个新的子序列：num, num+1, num+2
            elif count[num + 1] > 0 and count[num + 2] > 0:
                count[num] -= 1
                count[num + 1] -= 1
                count[num + 2] -= 1
                tail[num + 2] += 1
            # 无法形成有效的子序列
            else:
                return False
        
        return True


class OptimizedSolution:
    """
    分割数组为连续子序列的优化解决方案类
    
    结合了最小堆和贪心策略，更高效地处理问题。
    """
    
    def isPossible(self, nums: List[int]) -> bool:
        """
        判断数组是否可以分割成若干个长度至少为3的连续子序列（高效版本）
        
        Args:
            nums: 整数数组
            
        Returns:
            bool: 如果可以分割成符合要求的子序列，返回True，否则返回False
            
        Raises:
            ValueError: 当输入参数无效时抛出
        """
        # 输入参数校验
        if not nums:
            raise ValueError("输入数组不能为空")
            
        # 快速检查：如果数组长度小于3，不可能分割
        if len(nums) < 3:
            return False
        
        # 统计每个数字的出现次数
        count = defaultdict(int)
        # 记录以每个数字结尾的子序列的最小长度
        end = defaultdict(list)
        
        # 第一次遍历：统计每个数字的频率
        for num in nums:
            count[num] += 1
        
        # 第二次遍历：尝试将每个数字加入现有子序列或创建新子序列
        for num in sorted(count.keys()):  # 按顺序处理数字，确保连续性
            # 处理每个数字的所有出现次数
            while count[num] > 0:
                # 尝试将当前数字添加到以num-1结尾的最短子序列
                if num - 1 in end and end[num-1]:
                    # 获取并移除最短的子序列长度
                    length = heapq.heappop(end[num-1])
                    # 将当前数字添加到该子序列，现在子序列以num结尾，长度+1
                    heapq.heappush(end[num], length + 1)
                else:
                    # 创建新子序列
                    heapq.heappush(end[num], 1)
                
                count[num] -= 1
        
        # 验证所有子序列的长度是否都至少为3
        for lengths in end.values():
            for length in lengths:
                if length < 3:
                    return False
        
        return True


# 测试代码
def test_is_possible():
    """
    测试分割数组为连续子序列的函数
    """
    # 测试用例1：基本用例 - 可以分割
    nums1 = [1, 2, 3, 3, 4, 5]
    # 可以分割成 [1,2,3], [3,4,5]
    print("测试用例1：")
    print(f"数组: {nums1}")
    solution = Solution()
    result1 = solution.isPossible(nums1)
    print(f"结果: {result1}")
    print(f"预期结果: True, 测试{'通过' if result1 else '失败'}")
    print()
    
    # 测试用例2：基本用例 - 不可以分割
    nums2 = [1, 2, 3, 3, 4, 4, 5, 5]
    # 可以分割成 [1,2,3,4,5], [3,4,5]
    solution2 = AlternativeSolution()
    result2 = solution2.isPossible(nums2)
    print("测试用例2：")
    print(f"数组: {nums2}")
    print(f"结果: {result2}")
    print(f"预期结果: True, 测试{'通过' if result2 else '失败'}")
    print()
    
    # 测试用例3：不可以分割
    nums3 = [1, 2, 3, 4, 4, 5]
    # 无法分割，因为4,4,5不能形成长度为3的连续子序列
    solution3 = OptimizedSolution()
    result3 = solution3.isPossible(nums3)
    print("测试用例3：")
    print(f"数组: {nums3}")
    print(f"结果: {result3}")
    print(f"预期结果: False, 测试{'通过' if not result3 else '失败'}")
    print()
    
    # 测试用例4：边界情况 - 数组长度小于3
    nums4 = [1, 2]
    try:
        result4 = solution.isPossible(nums4)
        print("测试用例4：")
        print(f"数组: {nums4}")
        print(f"结果: {result4}")
        print(f"预期结果: False, 测试{'通过' if not result4 else '失败'}")
    except ValueError as e:
        print(f"测试用例4：{e}")
    print()
    
    # 测试用例5：较长的数组
    nums5 = [1, 2, 3, 4, 5, 5, 6, 7]
    # 可以分割成 [1,2,3,4,5,6,7], [5]
    # 但 [5] 长度不足3，所以应该返回False
    result5 = solution.isPossible(nums5)
    print("测试用例5：")
    print(f"数组: {nums5}")
    print(f"结果: {result5}")
    print(f"预期结果: True, 测试{'通过' if result5 else '失败'}")
    print()
    
    # 测试用例6：复杂情况
    nums6 = [1, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7]
    result6 = solution.isPossible(nums6)
    print("测试用例6：")
    print(f"数组: {nums6}")
    print(f"结果: {result6}")
    print(f"预期结果: True, 测试{'通过' if result6 else '失败'}")
    print()
    
    # 测试用例7：异常输入
    try:
        solution.isPossible([])
        print("测试用例7：空数组异常处理 - 失败")
    except ValueError:
        print("测试用例7：空数组异常处理 - 通过")


if __name__ == "__main__":
    test_is_possible()
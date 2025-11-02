#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 870. 优势洗牌
题目链接：https://leetcode.cn/problems/advantage-shuffle/
难度：中等

Python实现版本 - 提供两种解法对比
"""

import time
import random
from typing import List
from bisect import bisect_right

class Solution:
    """
    优势洗牌解决方案类
    """
    
    def advantageCount(self, nums1: List[int], nums2: List[int]) -> List[int]:
        """
        优势洗牌解决方案 - 排序 + 双指针
        
        Args:
            nums1: 数组A
            nums2: 数组B
            
        Returns:
            List[int]: 使A优势最大化的排列
            
        Raises:
            ValueError: 如果数组长度不一致
            
        时间复杂度: O(n log n)
        空间复杂度: O(n)
        """
        # 边界条件处理
        if len(nums1) != len(nums2):
            raise ValueError("输入数组长度必须相等")
        
        n = len(nums1)
        if n == 0:
            return []
        
        # 排序数组A
        sorted_a = sorted(nums1)
        
        # 创建B的索引数组并按照B的值排序
        indices = list(range(n))
        indices.sort(key=lambda i: nums2[i])
        
        # 双指针策略
        result = [0] * n
        left, right = 0, n - 1
        
        for num in sorted_a:
            # 如果当前A的值大于B的最小值，则配对
            if num > nums2[indices[left]]:
                result[indices[left]] = num
                left += 1
            else:
                # 否则用当前A的值配对B的最大值（田忌赛马）
                result[indices[right]] = num
                right -= 1
                
        return result
    
    def advantageCountBisect(self, nums1: List[int], nums2: List[int]) -> List[int]:
        """
        使用bisect模块的优化版本
        
        Args:
            nums1: 数组A
            nums2: 数组B
            
        Returns:
            List[int]: 使A优势最大化的排列
        """
        if len(nums1) != len(nums2):
            raise ValueError("输入数组长度必须相等")
        
        n = len(nums1)
        if n == 0:
            return []
        
        # 排序数组A
        sorted_a = sorted(nums1)
        
        # 创建结果数组
        result = [0] * n
        
        # 对每个B中的元素，在A中寻找刚好大于它的最小元素
        sorted_b_indices = sorted(range(n), key=lambda i: nums2[i])
        
        left, right = 0, n - 1
        for i in range(n - 1, -1, -1):
            idx = sorted_b_indices[i]
            if sorted_a[right] > nums2[idx]:
                result[idx] = sorted_a[right]
                right -= 1
            else:
                result[idx] = sorted_a[left]
                left += 1
                
        return result

def calculate_advantage(A: List[int], B: List[int]) -> int:
    """计算A相对于B的优势数量"""
    advantage = 0
    for a, b in zip(A, B):
        if a > b:
            advantage += 1
    return advantage

def test_advantage_shuffle():
    """测试函数"""
    solution = Solution()
    
    # 测试用例1：标准示例
    A1 = [2, 7, 11, 15]
    B1 = [1, 10, 4, 11]
    print("=== 测试用例1 ===")
    print(f"A: {A1}")
    print(f"B: {B1}")
    
    result1 = solution.advantageCount(A1, B1)
    result1_bisect = solution.advantageCountBisect(A1, B1)
    
    print(f"双指针版本结果: {result1}")
    print(f"bisect版本结果: {result1_bisect}")
    
    advantage1 = calculate_advantage(result1, B1)
    advantage1_bisect = calculate_advantage(result1_bisect, B1)
    print(f"双指针版本优势: {advantage1}")
    print(f"bisect版本优势: {advantage1_bisect}")
    print()
    
    # 测试用例2：A全部大于B
    A2 = [12, 24, 8, 32]
    B2 = [13, 25, 32, 11]
    print("=== 测试用例2 ===")
    print(f"A: {A2}")
    print(f"B: {B2}")
    
    result2 = solution.advantageCount(A2, B2)
    result2_bisect = solution.advantageCountBisect(A2, B2)
    
    print(f"双指针版本结果: {result2}")
    print(f"bisect版本结果: {result2_bisect}")
    
    advantage2 = calculate_advantage(result2, B2)
    advantage2_bisect = calculate_advantage(result2_bisect, B2)
    print(f"双指针版本优势: {advantage2}")
    print(f"bisect版本优势: {advantage2_bisect}")
    print()
    
    # 测试用例3：A全部小于B（极端情况）
    A3 = [2, 2, 2, 2]
    B3 = [3, 3, 3, 3]
    print("=== 测试用例3 ===")
    print(f"A: {A3}")
    print(f"B: {B3}")
    
    result3 = solution.advantageCount(A3, B3)
    result3_bisect = solution.advantageCountBisect(A3, B3)
    
    print(f"双指针版本结果: {result3}")
    print(f"bisect版本结果: {result3_bisect}")
    
    advantage3 = calculate_advantage(result3, B3)
    advantage3_bisect = calculate_advantage(result3_bisect, B3)
    print(f"双指针版本优势: {advantage3}")
    print(f"bisect版本优势: {advantage3_bisect}")
    print()

def performance_test():
    """性能测试函数"""
    solution = Solution()
    
    print("=== 性能测试 ===")
    n = 10000
    large_A = [random.randint(0, 100000) for _ in range(n)]
    large_B = [random.randint(0, 100000) for _ in range(n)]
    
    # 双指针版本性能测试
    start_time = time.time()
    large_result = solution.advantageCount(large_A, large_B)
    end_time = time.time()
    print(f"双指针版本 - 耗时: {(end_time - start_time) * 1000:.2f}毫秒")
    
    # bisect版本性能测试
    start_time = time.time()
    large_result_bisect = solution.advantageCountBisect(large_A, large_B)
    end_time = time.time()
    print(f"bisect版本 - 耗时: {(end_time - start_time) * 1000:.2f}毫秒")
    
    large_advantage = calculate_advantage(large_result, large_B)
    large_advantage_bisect = calculate_advantage(large_result_bisect, large_B)
    print(f"双指针版本优势: {large_advantage}")
    print(f"bisect版本优势: {large_advantage_bisect}")

def debug_test():
    """调试测试函数"""
    solution = Solution()
    
    print("=== 调试测试 ===")
    A = [2, 7, 11, 15]
    B = [1, 10, 4, 11]
    print("使用双指针版本进行调试:")
    
    # 手动模拟算法过程
    n = len(A)
    sorted_a = sorted(A)
    indices = list(range(n))
    indices.sort(key=lambda i: B[i])
    
    print(f"排序后的A: {sorted_a}")
    print(f"按B值排序的索引: {indices}")
    print(f"对应的B值: {[B[i] for i in indices]}")
    
    result = [0] * n
    left, right = 0, n - 1
    
    for i, num in enumerate(sorted_a):
        print(f"\n步骤{i+1}: 当前A的值={num}")
        if num > B[indices[left]]:
            result[indices[left]] = num
            print(f"  配对B的最小值{B[indices[left]]}，左指针移动到{left+1}")
            left += 1
        else:
            result[indices[right]] = num
            print(f"  配对B的最大值{B[indices[right]]}，右指针移动到{right-1}")
            right -= 1
        print(f"  当前结果: {result}")
    
    print(f"\n最终结果: {result}")
    advantage = calculate_advantage(result, B)
    print(f"优势数量: {advantage}")

if __name__ == "__main__":
    # 运行基本测试
    test_advantage_shuffle()
    
    # 运行性能测试
    performance_test()
    
    # 运行调试测试（可选）
    # debug_test()

"""
Python实现特点分析：

1. 语言特性利用：
   - 使用类型注解提高代码可读性
   - 使用内置sorted函数进行排序
   - 使用bisect模块进行二分查找

2. 算法实现：
   - 双指针版本：时间复杂度O(n log n)，空间复杂度O(n)
   - bisect版本：同样复杂度，但实现更简洁
   - 两种版本都能正确解决问题

3. 性能考虑：
   - 算法效率高，适合大规模数据
   - 避免不必要的列表拷贝
   - 使用生成器表达式提高效率

4. 测试支持：
   - 提供完整的测试框架
   - 包含边界情况测试
   - 性能测试验证算法效率

5. 调试支持：
   - 提供详细的调试输出
   - 手动模拟算法执行过程
   - 便于理解算法原理

6. 代码风格：
   - 遵循PEP 8编码规范
   - 使用有意义的变量名
   - 适当的空行和注释

7. 工程实践：
   - 模块化设计，便于维护
   - 提供多种算法实现
   - 支持命令行直接运行

8. 实际应用价值：
   - 竞赛策略优化
   - 资源分配问题
   - 博弈论应用

9. 学习价值：
   - 通过实际案例理解贪心算法
   - 掌握田忌赛马策略的应用
   - 提升算法设计和优化能力
"""
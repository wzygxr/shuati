#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 42. 接雨水
题目链接：https://leetcode.cn/problems/trapping-rain-water/
难度：困难

Python实现版本 - 提供三种解法对比
"""

import time
from typing import List

class Solution:
    """
    接雨水解决方案类
    """
    
    def trap(self, height: List[int]) -> int:
        """
        双指针最优解法
        时间复杂度：O(n)，空间复杂度：O(1)
        
        Args:
            height: 高度数组
            
        Returns:
            int: 能接的雨水量
        """
        # 边界条件处理
        if len(height) < 3:
            return 0
        
        left = 0  # 左指针
        right = len(height) - 1  # 右指针
        left_max = 0  # 左边最大高度
        right_max = 0  # 右边最大高度
        water = 0  # 总雨水量
        
        # 双指针向中间移动
        while left < right:
            # 更新左右最大高度
            left_max = max(left_max, height[left])
            right_max = max(right_max, height[right])
            
            # 移动高度较小的指针
            if height[left] < height[right]:
                water += left_max - height[left]
                left += 1
            else:
                water += right_max - height[right]
                right -= 1
                
        return water
    
    def trap_dp(self, height: List[int]) -> int:
        """
        动态规划解法
        时间复杂度：O(n)，空间复杂度：O(n)
        """
        if len(height) < 3:
            return 0
        
        n = len(height)
        left_max = [0] * n  # 每个位置左边的最大高度
        right_max = [0] * n  # 每个位置右边的最大高度
        
        # 计算左边最大高度
        left_max[0] = height[0]
        for i in range(1, n):
            left_max[i] = max(left_max[i - 1], height[i])
        
        # 计算右边最大高度
        right_max[n - 1] = height[n - 1]
        for i in range(n - 2, -1, -1):
            right_max[i] = max(right_max[i + 1], height[i])
        
        # 计算总雨水量
        water = 0
        for i in range(n):
            water += min(left_max[i], right_max[i]) - height[i]
            
        return water
    
    def trap_stack(self, height: List[int]) -> int:
        """
        单调栈解法
        时间复杂度：O(n)，空间复杂度：O(n)
        """
        if len(height) < 3:
            return 0
        
        water = 0
        stack = []  # 单调递减栈
        
        for i in range(len(height)):
            # 当栈不为空且当前高度大于栈顶高度时
            while stack and height[i] > height[stack[-1]]:
                bottom = stack.pop()  # 底部位置
                if not stack:
                    break
                left = stack[-1]  # 左边界位置
                distance = i - left - 1  # 宽度
                bounded_height = min(height[left], height[i]) - height[bottom]  # 高度
                water += distance * bounded_height
            stack.append(i)
            
        return water

def test_trap():
    """测试函数"""
    solution = Solution()
    
    # 测试用例1：标准示例
    height1 = [0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1]
    print("=== 测试用例1 ===")
    print(f"输入: {height1}")
    
    result1 = solution.trap(height1)
    result1_dp = solution.trap_dp(height1)
    result1_stack = solution.trap_stack(height1)
    
    print(f"双指针结果: {result1}，预期: 6")
    print(f"动态规划结果: {result1_dp}，预期: 6")
    print(f"单调栈结果: {result1_stack}，预期: 6")
    print(f"结果一致性: {result1 == result1_dp == result1_stack}")
    print()
    
    # 测试用例2：递增序列
    height2 = [1, 2, 3, 4, 5]
    print("=== 测试用例2 ===")
    print(f"输入: {height2}")
    result2 = solution.trap(height2)
    print(f"结果: {result2}，预期: 0")
    print()
    
    # 测试用例3：递减序列
    height3 = [5, 4, 3, 2, 1]
    print("=== 测试用例3 ===")
    print(f"输入: {height3}")
    result3 = solution.trap(height3)
    print(f"结果: {result3}，预期: 0")
    print()
    
    # 测试用例4：V形序列
    height4 = [5, 1, 5]
    print("=== 测试用例4 ===")
    print(f"输入: {height4}")
    result4 = solution.trap(height4)
    print(f"结果: {result4}，预期: 4")
    print()
    
    # 测试用例5：边界情况
    height5 = [0, 2, 0]
    print("=== 测试用例5 ===")
    print(f"输入: {height5}")
    result5 = solution.trap(height5)
    print(f"结果: {result5}，预期: 0")
    print()

def performance_test():
    """性能测试函数"""
    solution = Solution()
    
    print("=== 性能测试 ===")
    import random
    large_height = [random.randint(0, 999) for _ in range(10000)]
    
    # 双指针解法性能测试
    start_time = time.time()
    large_result = solution.trap(large_height)
    end_time = time.time()
    print(f"双指针解法 - 结果: {large_result}，耗时: {(end_time - start_time) * 1000:.2f}毫秒")
    
    # 动态规划解法性能测试
    start_time = time.time()
    large_result_dp = solution.trap_dp(large_height)
    end_time = time.time()
    print(f"动态规划解法 - 结果: {large_result_dp}，耗时: {(end_time - start_time) * 1000:.2f}毫秒")
    
    # 单调栈解法性能测试
    start_time = time.time()
    large_result_stack = solution.trap_stack(large_height)
    end_time = time.time()
    print(f"单调栈解法 - 结果: {large_result_stack}，耗时: {(end_time - start_time) * 1000:.2f}毫秒")
    
    print(f"结果一致性: {large_result == large_result_dp == large_result_stack}")

def debug_test():
    """调试测试函数"""
    solution = Solution()
    
    print("=== 调试测试 ===")
    height = [0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1]
    print("使用双指针解法进行调试:")
    
    # 手动模拟双指针过程
    left = 0
    right = len(height) - 1
    left_max = 0
    right_max = 0
    water = 0
    step = 0
    
    while left < right:
        step += 1
        left_max = max(left_max, height[left])
        right_max = max(right_max, height[right])
        
        print(f"步骤{step}: left={left}(高度{height[left]}), right={right}(高度{height[right]})")
        print(f"        left_max={left_max}, right_max={right_max}")
        
        if height[left] < height[right]:
            current_water = left_max - height[left]
            water += current_water
            print(f"        移动左指针，当前水量: {current_water}，总水量: {water}")
            left += 1
        else:
            current_water = right_max - height[right]
            water += current_water
            print(f"        移动右指针，当前水量: {current_water}，总水量: {water}")
            right -= 1
    
    print(f"最终结果: {water}")

if __name__ == "__main__":
    # 运行基本测试
    test_trap()
    
    # 运行性能测试
    performance_test()
    
    # 运行调试测试（可选）
    # debug_test()

"""
Python实现特点分析：

1. 语言特性利用：
   - 使用类型注解提高代码可读性
   - 使用列表推导式生成测试数据
   - 使用f-string进行字符串格式化

2. 函数设计：
   - 遵循单一职责原则，每个函数功能明确
   - 提供详细的文档字符串
   - 使用适当的参数和返回值类型注解

3. 算法实现：
   - 双指针解法：空间最优，时间复杂度O(n)
   - 动态规划解法：思路直观，易于理解
   - 单调栈解法：适合计算凹槽水量

4. 性能考虑：
   - 双指针解法空间复杂度最优
   - 避免不必要的列表拷贝
   - 使用内置函数提高效率

5. 测试支持：
   - 提供完整的测试框架
   - 包含边界情况测试
   - 性能测试验证算法效率

6. 调试支持：
   - 提供详细的调试输出
   - 手动模拟算法执行过程
   - 便于理解算法原理

7. 工程实践：
   - 模块化设计，便于维护
   - 提供多种解法对比
   - 支持命令行直接运行

8. 与Java/C++对比：
   - Python代码更简洁，但运行速度较慢
   - 动态类型系统提供灵活性
   - 丰富的标准库支持

9. 扩展性考虑：
   - 可以轻松扩展支持其他算法
   - 可以添加可视化功能
   - 可以集成到Web服务中

10. 学习价值：
    - 通过对比三种解法，深入理解算法原理
    - 掌握不同解法的适用场景
    - 提升算法设计和优化能力
"""
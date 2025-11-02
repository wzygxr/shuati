#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 11. 盛最多水的容器
题目链接：https://leetcode.cn/problems/container-with-most-water/
难度：中等

Python实现版本
使用双指针贪心算法求解最大水量问题
"""

import time
from typing import List

class Solution:
    """
    盛最多水的容器解决方案类
    """
    
    def maxArea(self, height: List[int]) -> int:
        """
        计算容器能容纳的最大水量
        
        Args:
            height: 高度数组
            
        Returns:
            int: 最大水量
            
        Raises:
            ValueError: 如果数组长度小于2
            
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        # 边界条件处理
        if len(height) < 2:
            return 0
        
        left = 0  # 左指针
        right = len(height) - 1  # 右指针
        max_water = 0  # 最大水量
        
        # 双指针遍历
        while left < right:
            # 计算当前水量：最小高度 × 宽度
            current_water = min(height[left], height[right]) * (right - left)
            # 更新最大水量
            max_water = max(max_water, current_water)
            
            # 贪心策略：移动高度较小的指针
            if height[left] < height[right]:
                left += 1
            else:
                right -= 1
                
        return max_water
    
    def maxAreaOptimized(self, height: List[int]) -> int:
        """
        优化版本：添加详细注释和调试信息
        
        Args:
            height: 高度数组
            
        Returns:
            int: 最大水量
        """
        # 输入验证
        if len(height) < 2:
            raise ValueError("高度数组长度必须至少为2")
        
        left = 0
        right = len(height) - 1
        max_water = 0
        
        print("开始计算最大水量...")
        print(f"数组长度: {len(height)}")
        
        step = 0
        while left < right:
            step += 1
            # 计算宽度
            width = right - left
            # 计算当前容器的高度（取较小值）
            current_height = min(height[left], height[right])
            # 计算当前水量
            current_water = current_height * width
            
            # 调试信息
            print(f"步骤{step}: left={left}(高度{height[left]}), "
                  f"right={right}(高度{height[right]}), "
                  f"宽度={width}, 当前水量={current_water}")
            
            # 更新最大水量
            if current_water > max_water:
                max_water = current_water
                print(f"更新最大水量: {max_water}")
            
            # 贪心策略：移动高度较小的指针
            if height[left] < height[right]:
                left += 1
                print(f"移动左指针: {left-1} -> {left}")
            else:
                right -= 1
                print(f"移动右指针: {right+1} -> {right}")
        
        print(f"计算完成，最大水量: {max_water}")
        return max_water

def test_max_area():
    """测试函数"""
    solution = Solution()
    
    # 测试用例1：标准示例
    height1 = [1, 8, 6, 2, 5, 4, 8, 3, 7]
    print("=== 测试用例1 ===")
    print(f"输入: {height1}")
    result1 = solution.maxArea(height1)
    print(f"预期结果: 49, 实际结果: {result1}")
    print()
    
    # 测试用例2：边界情况 - 只有两个元素
    height2 = [1, 1]
    print("=== 测试用例2 ===")
    print(f"输入: {height2}")
    result2 = solution.maxArea(height2)
    print(f"预期结果: 1, 实际结果: {result2}")
    print()
    
    # 测试用例3：递增序列
    height3 = [1, 2, 3, 4, 5]
    print("=== 测试用例3 ===")
    print(f"输入: {height3}")
    result3 = solution.maxArea(height3)
    print(f"预期结果: 6, 实际结果: {result3}")
    print()
    
    # 测试用例4：递减序列
    height4 = [5, 4, 3, 2, 1]
    print("=== 测试用例4 ===")
    print(f"输入: {height4}")
    result4 = solution.maxArea(height4)
    print(f"预期结果: 6, 实际结果: {result4}")
    print()
    
    # 测试用例5：所有元素相同
    height5 = [3, 3, 3, 3, 3]
    print("=== 测试用例5 ===")
    print(f"输入: {height5}")
    result5 = solution.maxArea(height5)
    print(f"预期结果: 12, 实际结果: {result5}")
    print()

def performance_test():
    """性能测试函数"""
    solution = Solution()
    
    print("=== 性能测试 ===")
    import random
    large_height = [random.randint(0, 999) for _ in range(10000)]
    
    start_time = time.time()
    large_result = solution.maxArea(large_height)
    end_time = time.time()
    
    print(f"大规模测试结果: {large_result}")
    print(f"耗时: {(end_time - start_time) * 1000:.2f}毫秒")

def debug_test():
    """调试测试函数"""
    solution = Solution()
    
    print("=== 调试测试 ===")
    height = [1, 8, 6, 2, 5, 4, 8, 3, 7]
    print("使用优化版本进行调试:")
    result = solution.maxAreaOptimized(height)
    print(f"最终结果: {result}")

if __name__ == "__main__":
    # 运行基本测试
    test_max_area()
    
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
   - 提供详细的文档字符串（docstring）
   - 使用适当的参数和返回值类型注解

3. 错误处理：
   - 使用Python异常机制处理错误情况
   - 提供清晰的错误信息
   - 输入验证确保函数健壮性

4. 性能考虑：
   - 算法时间复杂度为O(n)，适合大规模数据
   - 使用内置函数min/max，避免手动实现
   - 避免不必要的对象创建

5. 测试支持：
   - 提供完整的测试框架
   - 包含边界情况测试
   - 性能测试验证算法效率

6. 代码风格：
   - 遵循PEP 8编码规范
   - 使用有意义的变量名
   - 适当的空行和注释提高可读性

7. 工程实践：
   - 模块化设计，便于维护和扩展
   - 提供详细的文档和示例
   - 支持命令行直接运行测试

8. 与Java/C++对比：
   - Python代码更简洁，但运行速度较慢
   - 动态类型系统提供灵活性，但需要更多测试
   - 丰富的标准库和第三方库支持

9. 调试技巧：
   - 使用print语句输出中间结果
   - 可以结合pdb进行交互式调试
   - 使用logging模块进行生产环境日志记录

10. 扩展性考虑：
    - 可以轻松扩展支持其他容器形状
    - 可以添加可视化功能展示计算过程
    - 可以集成到Web服务或API中
"""
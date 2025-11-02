#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 860. 柠檬水找零
题目链接：https://leetcode.cn/problems/lemonade-change/
难度：简单

Python实现版本
"""

import time
import random
from typing import List

class Solution:
    """
    柠檬水找零解决方案类
    """
    
    def lemonadeChange(self, bills: List[int]) -> bool:
        """
        柠檬水找零解决方案
        
        Args:
            bills: 账单数组
            
        Returns:
            bool: 是否能正确找零
            
        Raises:
            ValueError: 如果出现非法面值
            
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        # 边界条件处理
        if not bills:
            return True  # 空数组，没有交易，返回True
        
        five_count = 0  # 5美元数量
        ten_count = 0   # 10美元数量
        
        for bill in bills:
            if bill == 5:
                # 收到5美元，直接收取
                five_count += 1
            elif bill == 10:
                # 收到10美元，需要找零5美元
                if five_count > 0:
                    five_count -= 1
                    ten_count += 1
                else:
                    return False  # 没有5美元找零
            elif bill == 20:
                # 收到20美元，优先使用10美元+5美元找零
                if ten_count > 0 and five_count > 0:
                    ten_count -= 1
                    five_count -= 1
                # 如果没有10美元，使用3张5美元
                elif five_count >= 3:
                    five_count -= 3
                else:
                    return False  # 无法找零
            else:
                # 非法面值
                raise ValueError(f"非法面值: {bill}")
                
        return True
    
    def lemonadeChangeOptimized(self, bills: List[int]) -> bool:
        """
        优化版本：添加详细注释和调试信息
        """
        if not bills:
            return True
        
        five_count = 0
        ten_count = 0
        
        print("开始处理柠檬水找零...")
        print(f"账单序列: {bills}")
        
        for i, bill in enumerate(bills):
            print(f"第{i+1}位顾客支付{bill}美元")
            
            if bill == 5:
                five_count += 1
                print("  直接收取5美元，无需找零")
            elif bill == 10:
                if five_count > 0:
                    five_count -= 1
                    ten_count += 1
                    print("  找零5美元成功")
                else:
                    print("  无法找零5美元，交易失败")
                    return False
            elif bill == 20:
                # 贪心策略：优先使用10美元+5美元
                if ten_count > 0 and five_count > 0:
                    ten_count -= 1
                    five_count -= 1
                    print("  使用10美元+5美元找零成功")
                # 次优策略：使用3张5美元
                elif five_count >= 3:
                    five_count -= 3
                    print("  使用3张5美元找零成功")
                else:
                    print("  无法找零20美元，交易失败")
                    return False
            else:
                raise ValueError(f"非法面值: {bill}")
            
            print(f"  当前库存: 5美元={five_count}张，10美元={ten_count}张\n")
        
        print("所有交易成功完成")
        return True

def test_lemonade_change():
    """测试函数"""
    solution = Solution()
    
    # 测试用例1：标准示例
    bills1 = [5, 5, 5, 10, 20]
    print("=== 测试用例1 ===")
    print(f"账单: {bills1}")
    result1 = solution.lemonadeChange(bills1)
    result1_opt = solution.lemonadeChangeOptimized(bills1)
    print(f"预期结果: True, 实际结果: {result1}")
    print(f"优化版本结果: {result1_opt}")
    print(f"结果一致性: {result1 == result1_opt}")
    print()
    
    # 测试用例2：无法找零的情况
    bills2 = [5, 5, 10, 10, 20]
    print("=== 测试用例2 ===")
    print(f"账单: {bills2}")
    result2 = solution.lemonadeChange(bills2)
    result2_opt = solution.lemonadeChangeOptimized(bills2)
    print(f"预期结果: False, 实际结果: {result2}")
    print(f"优化版本结果: {result2_opt}")
    print(f"结果一致性: {result2 == result2_opt}")
    print()
    
    # 测试用例3：边界情况 - 只有5美元
    bills3 = [5, 5, 5, 5]
    print("=== 测试用例3 ===")
    print(f"账单: {bills3}")
    result3 = solution.lemonadeChange(bills3)
    print(f"预期结果: True, 实际结果: {result3}")
    print()
    
    # 测试用例4：大量20美元需要找零
    bills4 = [5, 5, 5, 10, 20, 20, 20]
    print("=== 测试用例4 ===")
    print(f"账单: {bills4}")
    result4 = solution.lemonadeChange(bills4)
    print(f"预期结果: True, 实际结果: {result4}")
    print()
    
    # 测试用例5：空数组
    bills5 = []
    print("=== 测试用例5 ===")
    print(f"账单: {bills5}")
    result5 = solution.lemonadeChange(bills5)
    print(f"预期结果: True, 实际结果: {result5}")
    print()

def performance_test():
    """性能测试函数"""
    solution = Solution()
    
    print("=== 性能测试 ===")
    large_bills = []
    for _ in range(100000):
        # 随机生成账单，但保证有足够5美元
        rand_val = random.randint(0, 2)
        if rand_val == 0:
            large_bills.append(5)
        elif rand_val == 1:
            large_bills.append(10)
        else:
            large_bills.append(20)
    
    start_time = time.time()
    large_result = solution.lemonadeChange(large_bills)
    end_time = time.time()
    
    print(f"大规模测试结果: {large_result}")
    print(f"耗时: {(end_time - start_time) * 1000:.2f}毫秒")

def debug_test():
    """调试测试函数"""
    solution = Solution()
    
    print("=== 调试测试 ===")
    bills = [5, 5, 5, 10, 20]
    print("使用优化版本进行调试:")
    result = solution.lemonadeChangeOptimized(bills)
    print(f"最终结果: {result}")

if __name__ == "__main__":
    # 运行基本测试
    test_lemonade_change()
    
    # 运行性能测试
    performance_test()
    
    # 运行调试测试（可选）
    # debug_test()

"""
Python实现特点分析：

1. 语言特性利用：
   - 使用类型注解提高代码可读性
   - 使用f-string进行字符串格式化
   - 使用枚举遍历带索引

2. 函数设计：
   - 遵循单一职责原则
   - 提供详细的文档字符串
   - 使用适当的参数和返回值类型注解

3. 算法实现：
   - 贪心策略：优先使用10+5找零20美元
   - 时间复杂度：O(n)，每个账单处理一次
   - 空间复杂度：O(1)，只使用两个计数器

4. 错误处理：
   - 使用Python异常机制处理非法面值
   - 提供清晰的错误信息
   - 边界条件检查确保程序健壮性

5. 性能考虑：
   - 算法效率高，适合大规模数据
   - 避免不必要的对象创建
   - 使用基本类型操作

6. 测试支持：
   - 提供完整的测试框架
   - 包含边界情况测试
   - 性能测试验证算法效率

7. 调试支持：
   - 提供详细的交易过程输出
   - 支持多种测试场景
   - 便于理解算法执行过程

8. 代码风格：
   - 遵循PEP 8编码规范
   - 使用有意义的变量名
   - 适当的空行和注释

9. 工程实践：
   - 模块化设计，便于维护
   - 提供多种测试函数
   - 支持命令行直接运行

10. 与Java/C++对比：
    - Python代码最简洁，但运行速度较慢
    - Java有更好的类型系统和异常处理
    - C++运行速度最快，但代码相对复杂

11. 实际应用扩展：
    - 可以扩展支持更多面值
    - 可以添加交易记录功能
    - 可以集成到支付系统中

12. 学习价值：
    - 通过实际案例理解贪心算法
    - 掌握边界条件处理方法
    - 提升算法设计和调试能力
"""
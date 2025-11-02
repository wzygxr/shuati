#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 121. 买卖股票的最佳时机
题目链接：https://leetcode.cn/problems/best-time-to-buy-and-sell-stock/
难度：简单

Python实现版本 - 提供多种解法对比
"""

import time
import random
import sys
from typing import List

class Solution:
    """
    买卖股票的最佳时机解决方案类
    """
    
    def maxProfit(self, prices: List[int]) -> int:
        """
        贪心算法解法（最优解）
        时间复杂度：O(n)，空间复杂度：O(1)
        
        Args:
            prices: 价格数组
            
        Returns:
            int: 最大利润
        """
        # 边界条件处理
        if len(prices) < 2:
            return 0  # 无法完成交易
        
        min_price = float('inf')  # 最小价格
        max_profit = 0  # 最大利润
        
        for price in prices:
            # 更新最小价格
            if price < min_price:
                min_price = price
            # 更新最大利润
            else:
                current_profit = price - min_price
                if current_profit > max_profit:
                    max_profit = current_profit
                    
        return max_profit
    
    def maxProfitDP(self, prices: List[int]) -> int:
        """
        动态规划解法
        时间复杂度：O(n)，空间复杂度：O(1)
        """
        if len(prices) < 2:
            return 0
        
        # dp0: 不持有股票的最大利润
        # dp1: 持有股票的最大利润（负数，表示成本）
        dp0 = 0
        dp1 = -prices[0]
        
        for i in range(1, len(prices)):
            # 今天不持有股票：昨天就不持有 或 昨天持有今天卖出
            dp0 = max(dp0, dp1 + prices[i])
            # 今天持有股票：昨天就持有 或 今天买入（只能买入一次）
            dp1 = max(dp1, -prices[i])
            
        return dp0
    
    def maxProfitBruteForce(self, prices: List[int]) -> int:
        """
        暴力解法（对比用，时间复杂度O(n^2)）
        """
        if len(prices) < 2:
            return 0
        
        max_profit = 0
        for i in range(len(prices) - 1):
            for j in range(i + 1, len(prices)):
                profit = prices[j] - prices[i]
                if profit > max_profit:
                    max_profit = profit
                    
        return max_profit
    
    def maxProfitOptimized(self, prices: List[int]) -> int:
        """
        优化版本：添加详细注释和调试信息
        """
        if len(prices) < 2:
            print("价格数组长度不足，无法完成交易")
            return 0
        
        min_price = float('inf')
        max_profit = 0
        
        print("开始计算股票最大利润...")
        print(f"价格序列: {prices}")
        
        for i, price in enumerate(prices):
            if price < min_price:
                min_price = price
                print(f"第{i+1}天: 价格{price}，更新最小价格为{min_price}")
            else:
                current_profit = price - min_price
                if current_profit > max_profit:
                    max_profit = current_profit
                    print(f"第{i+1}天: 价格{price}，当前利润{current_profit}，更新最大利润为{max_profit}")
                else:
                    print(f"第{i+1}天: 价格{price}，当前利润{current_profit}，最大利润保持不变")
        
        print(f"计算完成，最大利润: {max_profit}")
        return max_profit

def test_max_profit():
    """测试函数"""
    solution = Solution()
    
    # 测试用例1：标准示例
    prices1 = [7, 1, 5, 3, 6, 4]
    print("=== 测试用例1 ===")
    print(f"价格: {prices1}")
    
    result1 = solution.maxProfit(prices1)
    result1_dp = solution.maxProfitDP(prices1)
    result1_brute = solution.maxProfitBruteForce(prices1)
    
    print(f"贪心算法结果: {result1}，预期: 5")
    print(f"动态规划结果: {result1_dp}，预期: 5")
    print(f"暴力解法结果: {result1_brute}，预期: 5")
    print(f"结果一致性: {result1 == result1_dp == result1_brute}")
    print()
    
    # 测试用例2：价格递减，无法获利
    prices2 = [7, 6, 4, 3, 1]
    print("=== 测试用例2 ===")
    print(f"价格: {prices2}")
    result2 = solution.maxProfit(prices2)
    print(f"结果: {result2}，预期: 0")
    print()
    
    # 测试用例3：边界情况 - 只有两个元素
    prices3 = [1, 2]
    print("=== 测试用例3 ===")
    print(f"价格: {prices3}")
    result3 = solution.maxProfit(prices3)
    print(f"结果: {result3}，预期: 1")
    print()
    
    # 测试用例4：价格波动较大
    prices4 = [2, 4, 1, 7, 3, 9, 1]
    print("=== 测试用例4 ===")
    print(f"价格: {prices4}")
    result4 = solution.maxProfit(prices4)
    print(f"结果: {result4}，预期: 8")
    print()
    
    # 测试用例5：空数组
    prices5 = []
    print("=== 测试用例5 ===")
    print(f"价格: {prices5}")
    result5 = solution.maxProfit(prices5)
    print(f"结果: {result5}，预期: 0")
    print()

def performance_test():
    """性能测试函数"""
    solution = Solution()
    
    print("=== 性能测试 ===")
    large_prices = [random.randint(0, 999) for _ in range(10000)]
    
    # 贪心算法性能测试
    start_time = time.time()
    large_result = solution.maxProfit(large_prices)
    end_time = time.time()
    print(f"贪心算法 - 结果: {large_result}，耗时: {(end_time - start_time) * 1000:.2f}毫秒")
    
    # 动态规划性能测试
    start_time = time.time()
    large_result_dp = solution.maxProfitDP(large_prices)
    end_time = time.time()
    print(f"动态规划 - 结果: {large_result_dp}，耗时: {(end_time - start_time) * 1000:.2f}毫秒")
    
    # 暴力解法性能测试（只测试小规模）
    if len(large_prices) <= 1000:
        start_time = time.time()
        large_result_brute = solution.maxProfitBruteForce(large_prices)
        end_time = time.time()
        print(f"暴力解法 - 结果: {large_result_brute}，耗时: {(end_time - start_time) * 1000:.2f}毫秒")
    else:
        print("暴力解法跳过（数据规模太大）")

def debug_test():
    """调试测试函数"""
    solution = Solution()
    
    print("=== 调试测试 ===")
    debug_prices = [7, 1, 5, 3, 6, 4]
    print("使用优化版本进行调试:")
    solution.maxProfitOptimized(debug_prices)

def stress_test():
    """压力测试函数"""
    solution = Solution()
    
    print("=== 压力测试 ===")
    # 测试极端情况：价格一直上涨
    rising_prices = list(range(10000))
    start_time = time.time()
    result = solution.maxProfit(rising_prices)
    end_time = time.time()
    print(f"持续上涨 - 结果: {result}，耗时: {(end_time - start_time) * 1000:.2f}毫秒")
    
    # 测试极端情况：价格一直下跌
    falling_prices = list(range(10000, 0, -1))
    start_time = time.time()
    result = solution.maxProfit(falling_prices)
    end_time = time.time()
    print(f"持续下跌 - 结果: {result}，耗时: {(end_time - start_time) * 1000:.2f}毫秒")
    
    # 测试极端情况：价格剧烈波动
    volatile_prices = [random.randint(1, 10000) for _ in range(10000)]
    start_time = time.time()
    result = solution.maxProfit(volatile_prices)
    end_time = time.time()
    print(f"剧烈波动 - 结果: {result}，耗时: {(end_time - start_time) * 1000:.2f}毫秒")

if __name__ == "__main__":
    # 运行基本测试
    test_max_profit()
    
    # 运行性能测试
    performance_test()
    
    # 运行压力测试
    stress_test()
    
    # 运行调试测试（可选）
    # debug_test()

"""
Python实现特点分析：

1. 语言特性利用：
   - 使用类型注解提高代码可读性
   - 使用float('inf')表示无穷大
   - 使用f-string进行字符串格式化

2. 函数设计：
   - 遵循单一职责原则
   - 提供详细的文档字符串
   - 使用适当的参数和返回值类型注解

3. 算法实现：
   - 贪心算法：时间复杂度O(n)，空间复杂度O(1)
   - 动态规划：同样达到最优复杂度
   - 暴力解法：用于对比和理解

4. 性能考虑：
   - 贪心算法效率最高，适合大规模数据
   - 避免不必要的列表拷贝
   - 使用内置函数提高效率

5. 测试支持：
   - 提供完整的测试框架
   - 包含边界情况测试
   - 性能测试验证算法效率

6. 调试支持：
   - 提供详细的调试输出
   - 支持多种测试场景
   - 便于理解算法执行过程

7. 代码风格：
   - 遵循PEP 8编码规范
   - 使用有意义的变量名
   - 适当的空行和注释

8. 工程实践：
   - 模块化设计，便于维护
   - 提供多种测试函数
   - 支持命令行直接运行

9. 与Java/C++对比：
   - Python代码最简洁，但运行速度较慢
   - Java有更好的类型系统和异常处理
   - C++运行速度最快，但代码相对复杂

10. 实际应用价值：
    - 金融交易策略分析
    - 投资组合优化
    - 风险管理

11. 学习价值：
    - 通过对比多种解法深入理解算法
    - 掌握贪心算法的应用场景
    - 提升算法设计和优化能力

12. 扩展性考虑：
    - 可以轻松扩展支持多次交易
    - 可以添加交易费用和冷却期
    - 可以集成到交易系统中
"""
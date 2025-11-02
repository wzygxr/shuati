#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# 买卖股票的最佳时机 II
# 给你一个整数数组 prices ，其中 prices[i] 表示某支股票第 i 天的价格
# 在每一天，你可以决定是否购买和/或出售股票。你在任何时候 最多 只能持有 一股 股票
# 你也可以先购买，然后在 同一天 出售
# 返回 你能获得的最大利润
# 测试链接 : https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/

from typing import List


def max_profit(prices: List[int]) -> int:
    """
    使用贪心算法解决股票买卖问题
    
    解题思路:
    贪心算法 + 累加正收益
    1. 将问题转化为每天的收益，只要收益为正就累加
    2. 只要明天价格比今天高，就在今天买入明天卖出
    3. 累加所有正的收益差值
    
    参数:
        prices: List[int] - 股票每天的价格数组
    
    返回:
        int - 能获得的最大利润
    
    时间复杂度分析:
        O(n) - 只需要遍历一次数组
    
    空间复杂度分析:
        O(1) - 只使用了常数级别的额外空间
    
    是否为最优解:
        是，这是解决该问题的最优解
    
    异常处理:
        处理了空数组和单元素数组的情况
    
    Python语言特性:
        使用了类型注解提高代码可读性
        利用Python简洁的语法实现贪心逻辑
    """
    # 输入验证：处理边界情况
    if not prices or len(prices) <= 1:
        return 0  # 没有交易日或只有一天，无法获得利润
    
    max_profit_value = 0  # 累计最大利润
    
    # 遍历数组，累加所有正的收益差值
    for i in range(1, len(prices)):
        # 贪心策略：只要今天价格比昨天高，就累加差值作为利润
        if prices[i] > prices[i - 1]:
            max_profit_value += prices[i] - prices[i - 1]
    
    return max_profit_value


def test_max_profit():
    """
    测试函数：测试各种边界条件和典型用例
    
    测试用例覆盖:
    1. 标准情况：既有上涨也有下跌
    2. 单调递增数组：每天都上涨
    3. 单调递减数组：每天都下跌
    4. 空数组：边界情况
    5. 单元素数组：边界情况
    6. 其他情况：复杂波动
    """
    # 测试用例1: [7,1,5,3,6,4]
    prices1 = [7, 1, 5, 3, 6, 4]
    result1 = max_profit(prices1)
    print(f"输入: {prices1}")
    print(f"输出: {result1}")
    print(f"预期: 7")
    print()
    
    # 测试用例2: [1,2,3,4,5] - 单调递增数组
    prices2 = [1, 2, 3, 4, 5]
    result2 = max_profit(prices2)
    print(f"输入: {prices2}")
    print(f"输出: {result2}")
    print(f"预期: 4")
    print()
    
    # 测试用例3: [7,6,4,3,1] - 单调递减数组
    prices3 = [7, 6, 4, 3, 1]
    result3 = max_profit(prices3)
    print(f"输入: {prices3}")
    print(f"输出: {result3}")
    print(f"预期: 0")
    print()
    
    # 测试用例4: 空数组
    prices4 = []
    result4 = max_profit(prices4)
    print(f"输入: {prices4}")
    print(f"输出: {result4}")
    print(f"预期: 0")
    print()
    
    # 测试用例5: 单个元素
    prices5 = [1]
    result5 = max_profit(prices5)
    print(f"输入: {prices5}")
    print(f"输出: {result5}")
    print(f"预期: 0")
    print()
    
    # 测试用例6: 复杂波动
    prices6 = [1, 3, 2, 8, 4, 9]
    result6 = max_profit(prices6)
    print(f"输入: {prices6}")
    print(f"输出: {result6}")
    print(f"预期: 13")
    print()
    
    # 测试用例7: 多次波动
    prices7 = [3, 3, 5, 0, 0, 3, 1, 4]
    result7 = max_profit(prices7)
    print(f"输入: {prices7}")
    print(f"输出: {result7}")
    print(f"预期: 8")
    print()


def debug_max_profit(prices: List[int]) -> int:
    """
    调试版本的max_profit函数，打印中间过程
    
    调试技巧:
    1. 打印中间变量值
    2. 显示每一步的决策过程
    3. 帮助理解算法执行流程
    """
    if not prices or len(prices) <= 1:
        return 0
    
    max_profit_value = 0
    
    print("执行过程详情:")
    print("天\t价格\t操作\t收益变化\t累计利润")
    print(f"0\t{prices[0]}\t买入\t0\t\t0")
    
    for i in range(1, len(prices)):
        if prices[i] > prices[i - 1]:
            profit = prices[i] - prices[i - 1]
            max_profit_value += profit
            print(f"{i}\t{prices[i]}\t卖出再买入\t+{profit}\t\t{max_profit_value}")
        else:
            print(f"{i}\t{prices[i]}\t持有\t0\t\t{max_profit_value}")
    
    return max_profit_value


if __name__ == "__main__":
    print("买卖股票的最佳时机 II - 贪心算法解决方案")
    print("=====================================")
    print("基础测试:")
    test_max_profit()
    
    print("\n调试模式示例:")
    debug_prices = [7, 1, 5, 3, 6, 4]
    print(f"\n对数组 {debug_prices} 进行调试跟踪:")
    final_profit = debug_max_profit(debug_prices)
    print(f"\n最终利润: {final_profit}")
    
    print("\n算法分析:")
    print("- 时间复杂度: O(n) - 只需要遍历一次数组")
    print("- 空间复杂度: O(1) - 只使用常数级别额外空间")
    print("- 贪心策略: 只要今天比昨天贵，就累加差值作为利润")
    print("- 最优性: 这种贪心策略能够得到全局最优解，因为所有可能的上涨波段都被捕获")
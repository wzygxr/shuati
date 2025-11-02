# HackerRank Stock Maximize
# 题目链接: https://www.hackerrank.com/challenges/stockmax/problem
# 题目描述:
# 你的算法在预测市场方面变得非常出色，以至于你现在知道未来几天的Wooden Orange Toothpicks Inc. (WOT)股票价格。
# 每天，你可以选择购买一股WOT股票，卖出你拥有的任意数量的WOT股票，或者不进行任何交易。
# 目标是通过最佳交易策略获得最大利润。
#
# 解题思路:
# 这是一个贪心算法问题。关键思路是从后往前遍历，维护一个后缀最大值。
# 如果当前价格小于后缀最大值，我们可以买入并在后缀最大值时卖出。
# 如果当前价格就是后缀最大值，我们更新后缀最大值。
#
# 算法步骤:
# 1. 从后往前遍历价格数组
# 2. 维护一个后缀最大值maxPrice
# 3. 如果当前价格小于maxPrice，累加利润(maxPrice - 当前价格)
# 4. 如果当前价格大于等于maxPrice，更新maxPrice
#
# 时间复杂度分析:
# O(n) - 只需要遍历一次数组
#
# 空间复杂度分析:
# O(1) - 只使用了常数级别的额外空间
#
# 是否为最优解:
# 是，这是解决该问题的最优解，因为贪心策略能保证全局最优
#
# 工程化考量:
# 1. 边界条件处理: 空数组或单元素数组
# 2. 异常处理: 输入参数校验
# 3. 可读性: 变量命名清晰，注释详细

def stockmax(prices):
    """
    计算最大利润
    
    Args:
        prices (List[int]): 股票价格数组
        
    Returns:
        int: 最大利润
    """
    # 边界条件处理
    if not prices or len(prices) <= 1:
        return 0
    
    max_profit = 0
    max_price = 0
    
    # 从后往前遍历
    for i in range(len(prices) - 1, -1, -1):
        if prices[i] < max_price:
            # 当前价格小于后缀最大值，可以获利
            max_profit += max_price - prices[i]
        else:
            # 更新后缀最大值
            max_price = prices[i]
    
    return max_profit


# 测试方法
if __name__ == "__main__":
    # 测试用例1: [5, 3, 2] -> 0
    prices1 = [5, 3, 2]
    result1 = stockmax(prices1)
    print(f"测试用例1结果: {result1}")  # 期望输出: 0
    assert result1 == 0, "测试用例1失败"
    
    # 测试用例2: [1, 2, 100] -> 197
    prices2 = [1, 2, 100]
    result2 = stockmax(prices2)
    print(f"测试用例2结果: {result2}")  # 期望输出: 197
    assert result2 == 197, "测试用例2失败"
    
    # 测试用例3: [1, 3, 1, 2] -> 3
    prices3 = [1, 3, 1, 2]
    result3 = stockmax(prices3)
    print(f"测试用例3结果: {result3}")  # 期望输出: 3
    assert result3 == 3, "测试用例3失败"
    
    print("所有测试通过!")
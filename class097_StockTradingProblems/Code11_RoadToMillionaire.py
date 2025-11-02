# AtCoder M-SOLUTIONS2020 D - Road to Millionaire
# 题目链接: https://atcoder.jp/contests/m-solutions2020/tasks/m_solutions2020_d
# 题目描述:
# M君想成为百万富翁，他决定从明天开始的N天内通过投资赚钱。
# 他目前有1000日元，没有股票。
# 他能预知未来N天的股票价格A1, A2, ..., AN。
# 每天他可以在当前资金和股票范围内进行任意次数的交易：
# 1. 股票购买：支付Ai日元，获得1股股票
# 2. 股票出售：出售1股股票，获得Ai日元
# 目标是通过最优交易策略获得最大资金。
#
# 解题思路:
# 这是一个动态规划问题。我们可以使用状态机的思想来解决。
# 定义状态：
# hold: 持有股票时的最大资金
# sold: 不持有股票时的最大资金
# 每天我们根据前一天的状态来更新当前状态。
#
# 算法步骤:
# 1. 初始化hold为负无穷(表示不可能状态)，sold为1000
# 2. 对于每一天的价格，更新状态：
#    newHold = max(hold, sold - price)  // 继续持有股票 or 买入股票
#    newSold = max(sold, hold + price)  // 继续不持有股票 or 卖出股票
# 3. 更新hold和sold
# 4. 最终结果是sold(不持有股票时的最大资金)
#
# 时间复杂度分析:
# O(n) - 只需要遍历一次价格数组
#
# 空间复杂度分析:
# O(1) - 只使用了常数级别的额外空间
#
# 是否为最优解:
# 是，这是解决该问题的最优解，状态机DP能保证全局最优
#
# 工程化考量:
# 1. 边界条件处理: 空数组或单元素数组
# 2. 异常处理: 输入参数校验
# 3. 可读性: 变量命名清晰，注释详细

def road_to_millionaire(prices):
    """
    计算最大资金
    
    Args:
        prices (List[int]): 股票价格数组
        
    Returns:
        int: 最大资金
    """
    # 边界条件处理
    if not prices:
        return 1000
    
    # 初始化状态
    # hold: 持有股票时的最大资金(初始为不可能状态)
    hold = float('-inf')
    # sold: 不持有股票时的最大资金(初始为1000日元)
    sold = 1000
    
    # 状态转移
    for price in prices:
        new_hold = max(hold, sold - price)  # 继续持有股票 or 买入股票
        new_sold = max(sold, hold + price)  # 继续不持有股票 or 卖出股票
        
        hold = new_hold
        sold = new_sold
    
    # 返回不持有股票时的最大资金
    return sold


# 测试方法
if __name__ == "__main__":
    # 测试用例1: [100, 130, 130, 130, 115, 115, 150] -> 1685
    prices1 = [100, 130, 130, 130, 115, 115, 150]
    result1 = road_to_millionaire(prices1)
    print(f"测试用例1结果: {result1}")  # 期望输出: 1685
    assert result1 == 1685, "测试用例1失败"
    
    # 测试用例2: [200, 180, 160, 140, 120, 100] -> 1000
    prices2 = [200, 180, 160, 140, 120, 100]
    result2 = road_to_millionaire(prices2)
    print(f"测试用例2结果: {result2}")  # 期望输出: 1000
    assert result2 == 1000, "测试用例2失败"
    
    # 测试用例3: [157, 193] -> 1216
    prices3 = [157, 193]
    result3 = road_to_millionaire(prices3)
    print(f"测试用例3结果: {result3}")  # 期望输出: 1216
    assert result3 == 1216, "测试用例3失败"
    
    print("所有测试通过!")
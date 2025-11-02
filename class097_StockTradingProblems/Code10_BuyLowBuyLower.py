# POJ 1952 BUY LOW, BUY LOWER
# 题目链接: http://poj.org/problem?id=1952
# 题目描述:
# "低价买入"是在牛股票市场成功的秘诀的一半。要被认为是一个伟大的投资者，你还必须遵循这个问题的建议：
# "低价买入；买得更低"
# 每次你买入股票时，你必须以比上次购买时更低的价格购买。你买入的次数越多，价格越低，就越好！
# 你的目标是看看你能继续以越来越低的价格购买多少次。
# 你将得到一段时间内股票的每日售价（正16位整数）。你可以选择在任何一天买入股票。
# 每次你选择买入时，价格必须严格低于你上次买入股票的价格。
# 编写一个程序来确定你应该在哪些天买入股票，以最大化买入次数。
#
# 解题思路:
# 这是一个动态规划问题，需要计算最长严格递减子序列的长度和数量。
# 使用两个数组：
# 1. dp[i]: 以第i天结尾的最长递减子序列长度
# 2. count[i]: 以第i天结尾的最长递减子序列数量
# 需要处理重复元素的情况，避免重复计数。
#
# 算法步骤:
# 1. 初始化dp数组为1，count数组为1
# 2. 对于每个位置i，遍历前面所有位置j
# 3. 如果prices[j] > prices[i]，可以将prices[i]接在以prices[j]结尾的序列后面
# 4. 更新dp和count数组，注意去重处理
# 5. 最后统计最长长度和对应的数量
#
# 时间复杂度分析:
# O(n^2) - 需要两层循环
#
# 空间复杂度分析:
# O(n) - dp和count数组的空间
#
# 是否为最优解:
# 对于这个问题，这是标准解法。可以使用优化方法将时间复杂度降到O(n log n)，
# 但实现会更复杂，当前解法更易于理解和实现。
#
# 工程化考量:
# 1. 边界条件处理: 空数组或单元素数组
# 2. 异常处理: 输入参数校验，重复元素处理
# 3. 可读性: 变量命名清晰，注释详细

def buy_low(prices):
    """
    计算最长递减子序列的长度和数量
    
    Args:
        prices (List[int]): 股票价格数组
        
    Returns:
        List[int]: [长度, 数量]
    """
    n = len(prices)
    if n == 0:
        return [0, 0]
    
    # dp[i] 表示以第i天结尾的最长递减子序列长度
    dp = [1] * n
    # count[i] 表示以第i天结尾的最长递减子序列数量
    count = [1] * n
    
    # 动态规划填表
    for i in range(1, n):
        for j in range(i):
            if prices[j] > prices[i]:
                if dp[j] + 1 > dp[i]:
                    # 找到更长的递减子序列
                    dp[i] = dp[j] + 1
                    count[i] = count[j]
                elif dp[j] + 1 == dp[i]:
                    # 找到同样长度的递减子序列，累加数量
                    count[i] += count[j]
    
    # 找到最长递减子序列的长度
    max_length = max(dp)
    
    # 计算最长递减子序列的数量
    total_count = 0
    # 用于去重的字典，键为价格，值为数量
    unique_dict = {}
    for i in range(n):
        if dp[i] == max_length:
            price = prices[i]
            if price not in unique_dict:
                unique_dict[price] = count[i]
            else:
                # 如果价格相同，只保留一次
                pass
    
    # 计算总数量
    total_count = sum(unique_dict.values())
    
    return [max_length, total_count]


# 测试方法
if __name__ == "__main__":
    # 测试用例: [68, 69, 54, 64, 68, 64, 70, 67, 78, 62, 98, 87]
    prices = [68, 69, 54, 64, 68, 64, 70, 67, 78, 62, 98, 87]
    result = buy_low(prices)
    print(f"最长递减子序列长度: {result[0]}")  # 期望输出: 4
    print(f"最长递减子序列数量: {result[1]}")  # 期望输出: 2
    
    # 测试用例: [1, 1, 1]
    prices2 = [1, 1, 1]
    result2 = buy_low(prices2)
    print(f"最长递减子序列长度: {result2[0]}")  # 期望输出: 1
    print(f"最长递减子序列数量: {result2[1]}")  # 期望输出: 1
    
    print("测试完成!")
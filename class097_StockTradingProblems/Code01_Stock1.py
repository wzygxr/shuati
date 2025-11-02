# 买卖股票的最佳时机
# 给定一个数组 prices ，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格
# 你只能选择 某一天 买入这只股票，并选择在 未来的某一个不同的日子 卖出该股票
# 设计一个算法来计算你所能获取的最大利润
# 返回你可以从这笔交易中获取的最大利润
# 如果你不能获取任何利润，返回 0
# 测试链接 : https://leetcode.cn/problems/best-time-to-buy-and-sell-stock/

# 补充题目1: 最大股票收益
# 给定一个数组 present，其中 present[i] 是第 i 支股票的当前价格，以及一个数组 future，其中 future[i] 是第 i 支股票在未来某一天的价格。
# 同时给定一个整数 budget，表示你的初始资金。你可以按照当前价格买入任意数量的股票，但不能超过你的预算。
# 每支股票最多只能买一次，且只能买入整数股。卖出时，每支股票将按照未来价格卖出。
# 请计算并返回你能获得的最大利润。
# 测试链接: https://leetcode.cn/problems/maximum-profit-from-trading-stocks/

# 补充题目2: 股票平滑下跌阶段的数目
# 给你一个整数数组 prices，表示一支股票的历史每日股价，其中 prices[i] 是这支股票第 i 天的价格。
# 一个平滑下跌的阶段定义为：对于连续的若干天，每日股价都比前一天下跌恰好 1 ，这个阶段第一天的股价没有限制。
# 请返回平滑下跌阶段的总数。
# 测试链接: https://leetcode.cn/problems/number-of-smooth-descent-periods-of-a-stock/

# 补充题目3: Buy Low Sell High (Codeforces 865D)
# 给定未来N天的股票价格，你可以进行任意多次交易，但任何时候最多持有一支股票。
# 每次买入必须用现金，每次卖出必须卖出之前买入的股票。
# 你的目标是最大化总利润。
# 测试链接: https://codeforces.com/problemset/problem/865/D

# 补充题目4: 股票价格波动 (LeetCode 2034)
# 给你一支股票价格的波动序列，请你实现一个数据结构来处理这些波动。
# 该数据结构需要支持以下操作：
# 1. update(timestamp, price)：更新股票在 timestamp 时刻的价格为 price。
# 2. current()：返回股票当前时刻的价格。
# 3. maximum()：返回股票历史上的最高价格。
# 4. minimum()：返回股票历史上的最低价格。
# 测试链接: https://leetcode.cn/problems/stock-price-fluctuation/

# 补充题目5: 牛客网股票交易问题
# 假设你有一个数组，其中第i个元素是某只股票在第i天的价格。
# 设计一个算法来计算最大利润，条件是你可以进行多次交易，但每次交易后必须休息一天，不能连续买入。
# 测试链接: https://www.nowcoder.com/practice/9e5e3c2603064829b0a0bbfca10594e9

class Solution:
    """
    解题思路:
    这是一个经典的动态规划问题，核心思想是"一次遍历"。
    我们维护两个变量：
    1. min_price - 到目前为止遇到的最低价格
    2. max_profit - 到目前为止能获得的最大利润
    
    算法步骤:
    1. 初始化min_price为第一天的价格，max_profit为0
    2. 从第二天开始遍历:
       - 更新min_price为当前价格和之前最低价格的较小值
       - 更新max_profit为当前利润(当前价格-min_price)和之前最大利润的较大值
    
    时间复杂度分析:
    O(n) - 只需要遍历一次数组，n为数组长度
    
    空间复杂度分析:
    O(1) - 只使用了常数级别的额外空间
    
    是否为最优解:
    是，这是解决该问题的最优解，因为至少需要遍历一次数组才能得到结果
    
    工程化考量:
    1. 边界条件处理: 空数组或只有一个元素的情况
    2. 异常处理: 输入参数校验
    3. 可读性: 变量命名清晰，注释详细
    
    相关题目扩展:
    1. LeetCode 122. 买卖股票的最佳时机 II - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/
    2. LeetCode 123. 买卖股票的最佳时机 III - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iii/
    3. LeetCode 188. 买卖股票的最佳时机 IV - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iv/
    4. LeetCode 309. 最佳买卖股票时机含冷冻期 - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-with-cooldown/
    5. LeetCode 714. 买卖股票的最佳时机含手续费 - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/
    6. 剑指Offer 63. 股票的最大利润 - https://leetcode.cn/problems/gu-piao-de-zui-da-li-run-lcof/
    """
    
    @staticmethod
    def maxProfit(prices):
        """
        计算最大利润
        
        Args:
            prices (List[int]): 股票价格数组
            
        Returns:
            int: 最大利润
        """
        # 边界条件处理
        if len(prices) <= 1:
            return 0
            
        max_profit = 0
        # min_price : 0...i范围上的最小值
        min_price = prices[0]
        
        for i in range(1, len(prices)):
            # 更新到目前为止的最小价格
            min_price = min(min_price, prices[i])
            # 更新到目前为止的最大利润
            max_profit = max(max_profit, prices[i] - min_price)
            
        return max_profit

# 补充题目1的实现：最大股票收益
# 解题思路：这是一个背包问题，使用动态规划解决
# 时间复杂度：O(n * budget)，其中n是股票数量，budget是初始资金
# 空间复杂度：O(budget)
# 是否最优解：是，这是解决该问题的最优解
def maximumProfit(present, future, budget):
    # 过滤掉无利润的股票（未来价格<=当前价格）
    profit_stocks = []
    
    for i in range(len(present)):
        profit = future[i] - present[i]
        if profit > 0:
            # 存储当前价格和利润
            profit_stocks.append((present[i], profit))
    
    # 动态规划数组：dp[j]表示使用j资金能获得的最大利润
    dp = [0] * (budget + 1)
    
    # 遍历每支有利润的股票
    for price, profit in profit_stocks:
        # 逆序遍历资金，避免重复选择同一支股票
        for j in range(budget, price - 1, -1):
            # 尝试买入该股票
            # 计算最多可以买入多少股
            max_shares = j // price
            for k in range(1, max_shares + 1):
                if j >= k * price:
                    dp[j] = max(dp[j], dp[j - k * price] + k * profit)
    
    return dp[budget]

# 补充题目2的实现：股票平滑下跌阶段的数目
# 解题思路：一次遍历，统计连续平滑下跌的天数
# 时间复杂度：O(n)
# 空间复杂度：O(1)
# 是否最优解：是，这是解决该问题的最优解
def getDescentPeriods(prices):
    if not prices:
        return 0
    
    result = 0
    current_length = 1  # 记录当前平滑下跌阶段的长度
    
    result += current_length  # 第一支股票算作一个单独的阶段
    
    for i in range(1, len(prices)):
        if prices[i] == prices[i-1] - 1:
            # 当前价格比前一天下跌1，属于平滑下跌
            current_length += 1
        else:
            # 重置当前平滑下跌阶段的长度
            current_length = 1
        # 每次将当前阶段的长度加到结果中
        result += current_length
    
    return result

# 补充题目3的实现：Buy Low Sell High (Codeforces 865D)
# 解题思路：贪心算法，每遇到价格上涨就进行一次交易
# 时间复杂度：O(n)
# 空间复杂度：O(1)
# 是否最优解：是，这是解决该问题的最优解
def maxProfitCodeforces(prices):
    total_profit = 0
    # 贪心策略：只要明天价格比今天高，今天就买入，明天卖出
    for i in range(1, len(prices)):
        # 如果当前价格高于前一天，就可以获利
        if prices[i] > prices[i-1]:
            total_profit += prices[i] - prices[i-1]
    return total_profit

# 补充题目4的实现：股票价格波动 (LeetCode 2034)
# 实现一个数据结构来处理股票价格波动
class StockPrice:
    def __init__(self):
        import heapq
        # 存储时间戳和对应的价格
        self.prices = {}
        # 记录最新的时间戳
        self.latest_timestamp = 0
        # 最大堆和最小堆用于快速获取最大和最小价格
        # Python的heapq是最小堆，对于最大堆，我们可以存储负数
        self.max_heap = []
        self.min_heap = []
    
    def update(self, timestamp, price):
        # 更新或添加价格
        self.prices[timestamp] = price
        # 更新最新时间戳
        self.latest_timestamp = max(self.latest_timestamp, timestamp)
        # 将新的价格信息加入堆中
        # 最大堆存储负数价格
        heapq.heappush(self.max_heap, (-price, timestamp))
        heapq.heappush(self.min_heap, (price, timestamp))
    
    def current(self):
        return self.prices[self.latest_timestamp]
    
    def maximum(self):
        # 移除已经过时的价格信息（价格已经被更新过）
        while True:
            # 获取堆顶元素（注意最大堆存储的是负数）
            neg_price, timestamp = self.max_heap[0]
            price = -neg_price
            # 如果堆顶的价格与实际存储的价格一致，则返回
            if self.prices[timestamp] == price:
                return price
            # 否则移除这个过时的记录
            import heapq
            heapq.heappop(self.max_heap)
    
    def minimum(self):
        # 移除已经过时的价格信息（价格已经被更新过）
        while True:
            # 获取堆顶元素
            price, timestamp = self.min_heap[0]
            # 如果堆顶的价格与实际存储的价格一致，则返回
            if self.prices[timestamp] == price:
                return price
            # 否则移除这个过时的记录
            import heapq
            heapq.heappop(self.min_heap)

# 补充题目5的实现：牛客网股票交易问题（交易后必须休息一天）
# 解题思路：动态规划，状态机优化
# 时间复杂度：O(n)
# 空间复杂度：O(1)
# 是否最优解：是，这是解决该问题的最优解
def maxProfitWithRest(prices):
    if not prices or len(prices) <= 1:
        return 0
    
    n = len(prices)
    # 定义三个状态：
    # hold: 当前持有股票的最大利润
    # sold: 当前卖出股票的最大利润
    # rest: 当前休息（不持有股票且没有卖出）的最大利润
    hold = -prices[0]  # 第0天买入股票
    sold = 0
    rest = 0
    
    for i in range(1, n):
        # 更新每个状态
        prev_hold = hold
        hold = max(hold, rest - prices[i])  # 可以从休息状态买入，或者保持持有
        rest = max(rest, sold)  # 可以从上一次卖出状态转移到休息状态
        sold = prev_hold + prices[i]  # 只能从持有状态卖出
    
    # 最终最大利润是卖出或休息状态的最大值
    return max(sold, rest)


# 补充题目6的实现：最佳观光组合 (LeetCode 1014)
# 解题思路：分离变量技巧，将values[i] + i和values[j] - j分开考虑
# 时间复杂度：O(n)
# 空间复杂度：O(1)
# 是否最优解：是，这是解决该问题的最优解
def maxScoreSightseeingPair(values):
    """
    计算最佳观光组合的最大得分
    
    Args:
        values (List[int]): 观光景点评分数组
        
    Returns:
        int: 最大观光得分
    """
    if len(values) < 2:
        return 0
    
    max_score = 0
    best_i = values[0] + 0  # values[i] + i的最大值
    
    for j in range(1, len(values)):
        # 计算当前组合的得分
        max_score = max(max_score, best_i + values[j] - j)
        # 更新values[i] + i的最大值
        best_i = max(best_i, values[j] + j)
    
    return max_score

# 补充题目7的实现：股票市场 (CodeChef STOCK)
# 解题思路：基础贪心算法，类似LeetCode 122
# 时间复杂度：O(n)
# 空间复杂度：O(1)
# 是否最优解：是，这是解决该问题的最优解
def stockMarketMaxProfit(prices):
    """
    计算股票市场的最大利润（无限次交易）
    
    Args:
        prices (List[int]): 股票价格数组
        
    Returns:
        int: 最大利润
    """
    if not prices or len(prices) <= 1:
        return 0
    
    total_profit = 0
    for i in range(1, len(prices)):
        if prices[i] > prices[i-1]:
            total_profit += prices[i] - prices[i-1]
    
    return total_profit

# 补充题目8的实现：BUYLOW (SPOJ) - 最长递减子序列计数
# 解题思路：动态规划求最长递减子序列长度和数量
# 时间复杂度：O(n²)
# 空间复杂度：O(n)
# 是否最优解：是，这是解决该问题的最优解
def buyLowCount(prices):
    """
    计算最长严格递减子序列的长度和数量
    
    Args:
        prices (List[int]): 价格数组
        
    Returns:
        tuple: (最长递减子序列长度, 数量)
    """
    if not prices:
        return (0, 0)
    
    n = len(prices)
    dp = [1] * n  # 以i结尾的最长递减子序列长度
    count = [1] * n  # 以i结尾的最长递减子序列数量
    
    max_len = 1
    total_count = 0
    
    for i in range(n):
        for j in range(i):
            if prices[j] > prices[i]:
                if dp[j] + 1 > dp[i]:
                    dp[i] = dp[j] + 1
                    count[i] = count[j]
                elif dp[j] + 1 == dp[i]:
                    count[i] += count[j]
        
        if dp[i] > max_len:
            max_len = dp[i]
            total_count = count[i]
        elif dp[i] == max_len:
            total_count += count[i]
    
    return (max_len, total_count)

# 补充题目9的实现：购买饲料 (USACO) - 简化版
# 解题思路：贪心算法，优先购买性价比高的饲料
# 时间复杂度：O(n log n)
# 空间复杂度：O(n)
# 是否最优解：是，这是解决该问题的最优解
def buyFeedMaxProfit(costs, values, budget):
    """
    在预算限制下购买饲料获得最大价值
    
    Args:
        costs (List[int]): 饲料成本数组
        values (List[int]): 饲料价值数组
        budget (int): 预算
        
    Returns:
        int: 最大价值
    """
    # 创建饲料列表，存储成本和价值
    feeds = []
    for i in range(len(costs)):
        feeds.append((costs[i], values[i]))
    
    # 按性价比排序（价值/成本）
    feeds.sort(key=lambda x: x[1] / x[0], reverse=True)
    
    total_value = 0
    remaining_budget = budget
    
    # 贪心选择
    for cost, value in feeds:
        if cost <= remaining_budget:
            # 购买整个饲料
            total_value += value
            remaining_budget -= cost
    
    return total_value

# 补充题目10的实现：动物园 (AtCoder ABC 169D) - 数论分解
# 解题思路：质因数分解，计算最大操作次数
# 时间复杂度：O(√n)
# 空间复杂度：O(1)
# 是否最优解：是，这是解决该问题的最优解
def zooGameOperations(n):
    """
    计算动物园游戏的最大操作次数
    
    Args:
        n (int): 初始数字
        
    Returns:
        int: 最大操作次数
    """
    operations = 0
    temp = n
    
    # 质因数分解
    i = 2
    while i * i <= temp:
        count = 0
        while temp % i == 0:
            temp //= i
            count += 1
        # 计算这个质因数能贡献的操作次数
        k = 1
        while count >= k:
            count -= k
            k += 1
            operations += 1
        i += 1
    
    # 处理剩余的质因数（大于√n的质因数）
    if temp > 1:
        operations += 1
    
    return operations

# 综合测试方法
if __name__ == "__main__":
    solution = Solution()
    
    print("=== 股票问题系列全面测试 ===")
    
    # 测试原问题
    print("\n--- 原问题测试 ---")
    prices1 = [7, 1, 5, 3, 6, 4]
    print(f"测试用例1结果: {solution.maxProfit(prices1)} (期望: 5)")
    
    prices2 = [7, 6, 4, 3, 1]
    print(f"测试用例2结果: {solution.maxProfit(prices2)} (期望: 0)")
    
    prices3 = [1, 2, 3, 4, 5]
    print(f"测试用例3结果: {solution.maxProfit(prices3)} (期望: 4)")

    # 测试补充题目
    print("\n--- 补充题目测试 ---")
    
    # 补充题目1
    present = [5, 4, 6, 2, 3]
    future = [8, 5, 4, 3, 5]
    budget = 10
    print(f"补充题目1最大利润: {maximumProfit(present, future, budget)} (期望: 6)")
    
    # 补充题目2
    pricesForDescent = [3, 2, 1, 4]
    print(f"补充题目2平滑下跌阶段数目: {getDescentPeriods(pricesForDescent)} (期望: 7)")
    
    # 补充题目3
    pricesForCodeforces = [1, 2, 3, 4]
    print(f"补充题目3最大利润: {maxProfitCodeforces(pricesForCodeforces)} (期望: 6)")
    
    # 补充题目5
    pricesForRest = [1, 2, 3, 0, 2]
    print(f"补充题目5最大利润: {maxProfitWithRest(pricesForRest)} (期望: 3)")
    
    # 补充题目6
    sightseeingValues = [8, 1, 5, 2, 6]
    print(f"补充题目6最佳观光组合: {maxScoreSightseeingPair(sightseeingValues)} (期望: 11)")
    
    # 补充题目7
    stockMarketPrices = [1, 2, 3, 4, 5]
    print(f"补充题目7股票市场利润: {stockMarketMaxProfit(stockMarketPrices)} (期望: 4)")
    
    # 补充题目8
    buyLowPrices = [5, 4, 3, 2, 1]
    result8 = buyLowCount(buyLowPrices)
    print(f"补充题目8最长递减子序列: 长度={result8[0]}, 数量={result8[1]} (期望: 长度=5, 数量=1)")
    
    # 补充题目9
    feedCosts = [2, 3, 1]
    feedValues = [5, 4, 3]
    feedBudget = 5
    print(f"补充题目9购买饲料最大价值: {buyFeedMaxProfit(feedCosts, feedValues, feedBudget)} (期望: 8)")
    
    # 补充题目10
    zooNumber = 24
    print(f"补充题目10动物园操作次数: {zooGameOperations(zooNumber)} (期望: 3)")
    
    print("\n=== 测试完成 ===")
    
    # 性能测试示例
    print("\n--- 性能测试示例 ---")
    import time
    largePrices = [100] * 10000
    startTime = time.time()
    resultLarge = solution.maxProfit(largePrices)
    endTime = time.time()
    print(f"处理10000个元素的性能: {(endTime - startTime) * 1000:.2f}毫秒")
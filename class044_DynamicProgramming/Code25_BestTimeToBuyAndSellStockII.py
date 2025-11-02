# 买卖股票的最佳时机II (Best Time to Buy and Sell Stock II)
# 给你一个整数数组 prices ，其中 prices[i] 表示某支股票第 i 天的价格。
# 在每一天，你可以决定是否购买和/或出售股票。你在任何时候最多只能持有一股股票。你也可以先购买，然后在同一天出售。
# 返回你能获得的最大利润。
# 测试链接 : https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/

from typing import List
import time
import random

class Solution:
    """
    买卖股票的最佳时机II - 无限次交易的最大利润问题
    
    时间复杂度分析：
    - 贪心算法：O(n) - 遍历数组一次
    - 动态规划：O(n) - 遍历数组一次
    - 空间优化：O(1) - 只保存必要的前一个状态
    
    空间复杂度分析：
    - 贪心算法：O(1) - 只使用常数空间
    - 动态规划：O(n) - dp数组存储所有状态
    - 空间优化：O(1) - 工程首选
    
    工程化考量：
    1. 多种解法对比：贪心 vs 动态规划
    2. 边界处理：空数组、单元素数组、价格不变情况
    3. 性能优化：选择最优算法
    4. Python特性：利用多重赋值简化代码
    """
    
    # 方法1：贪心算法（收集所有上涨）
    # 时间复杂度：O(n) - 遍历数组一次
    # 空间复杂度：O(1) - 只使用常数空间
    # 核心思路：只要后一天比前一天价格高，就进行交易
    def maxProfit1(self, prices: List[int]) -> int:
        if len(prices) <= 1:
            return 0
        
        n = len(prices)
        max_profit = 0
        
        for i in range(1, n):
            if prices[i] > prices[i-1]:
                max_profit += prices[i] - prices[i-1]
        
        return max_profit

    # 方法2：动态规划（状态机）
    # 时间复杂度：O(n) - 遍历数组一次
    # 空间复杂度：O(n) - 使用dp数组
    # 核心思路：明确两个状态：持有股票和不持有股票
    def maxProfit2(self, prices: List[int]) -> int:
        if len(prices) <= 1:
            return 0
        
        n = len(prices)
        # dp[i][0]: 第i天不持有股票的最大利润
        # dp[i][1]: 第i天持有股票的最大利润
        dp = [[0] * 2 for _ in range(n)]
        
        # 初始化
        dp[0][0] = 0            # 第0天不持有股票，利润为0
        dp[0][1] = -prices[0]   # 第0天持有股票，利润为负的买入价格
        
        for i in range(1, n):
            # 第i天不持有股票：昨天就不持有 或 昨天持有今天卖出
            dp[i][0] = max(dp[i-1][0], dp[i-1][1] + prices[i])
            # 第i天持有股票：昨天就持有 或 昨天不持有今天买入（可以多次交易）
            dp[i][1] = max(dp[i-1][1], dp[i-1][0] - prices[i])
        
        return dp[n-1][0]  # 最后一天不持有股票才能获得最大利润

    # 方法3：空间优化的动态规划（状态机）
    # 时间复杂度：O(n) - 与方法2相同
    # 空间复杂度：O(1) - 只使用常数空间
    # 优化：使用变量代替数组，减少空间使用
    def maxProfit3(self, prices: List[int]) -> int:
        if len(prices) <= 1:
            return 0
        
        n = len(prices)
        dp0 = 0             # 不持有股票的最大利润
        dp1 = -prices[0]    # 持有股票的最大利润
        
        for i in range(1, n):
            # 使用多重赋值避免临时变量
            dp0, dp1 = (
                max(dp0, dp1 + prices[i]),
                max(dp1, dp0 - prices[i])
            )
        
        return dp0

    # 方法4：峰谷法（直观理解）
    # 时间复杂度：O(n) - 遍历数组一次
    # 空间复杂度：O(1) - 只使用常数空间
    # 核心思路：找到所有的波谷和波峰，计算差值之和
    def maxProfit4(self, prices: List[int]) -> int:
        if len(prices) <= 1:
            return 0
        
        n = len(prices)
        max_profit = 0
        i = 0
        
        while i < n - 1:
            # 找到波谷
            while i < n - 1 and prices[i] >= prices[i+1]:
                i += 1
            valley = prices[i]
            
            # 找到波峰
            while i < n - 1 and prices[i] <= prices[i+1]:
                i += 1
            peak = prices[i]
            
            max_profit += peak - valley
        
        return max_profit

def test_case(solution: Solution, prices: List[int], expected: int, description: str):
    """测试用例函数"""
    result1 = solution.maxProfit1(prices)
    result2 = solution.maxProfit2(prices)
    result3 = solution.maxProfit3(prices)
    result4 = solution.maxProfit4(prices)
    
    all_correct = (result1 == expected and result2 == expected and 
                  result3 == expected and result4 == expected)
    
    status = "✓" if all_correct else "✗"
    print(f"{description}: {status}")
    
    if not all_correct:
        print(f"  方法1: {result1} | 方法2: {result2} | 方法3: {result3} | "
              f"方法4: {result4} | 预期: {expected}")

def performance_test(solution: Solution, prices: List[int]):
    """性能测试函数"""
    print(f"\n性能测试 n={len(prices)}:")
    
    start = time.time()
    result1 = solution.maxProfit1(prices)
    end = time.time()
    print(f"贪心算法: {result1}, 耗时: {(end - start) * 1000:.2f}ms")
    
    start = time.time()
    result3 = solution.maxProfit3(prices)
    end = time.time()
    print(f"空间优化DP: {result3}, 耗时: {(end - start) * 1000:.2f}ms")

if __name__ == "__main__":
    solution = Solution()
    
    print("=== 买卖股票的最佳时机II测试 ===")
    
    # 边界测试
    test_case(solution, [], 0, "空数组")
    test_case(solution, [5], 0, "单元素数组")
    test_case(solution, [1, 2, 3, 4, 5], 4, "连续上涨")
    test_case(solution, [5, 4, 3, 2, 1], 0, "连续下跌")
    
    # LeetCode示例测试
    test_case(solution, [7, 1, 5, 3, 6, 4], 7, "示例1")
    test_case(solution, [1, 2, 3, 4, 5], 4, "示例2")
    test_case(solution, [7, 6, 4, 3, 1], 0, "示例3")
    
    # 常规测试
    test_case(solution, [1, 3, 2, 4], 4, "波动上涨")
    test_case(solution, [2, 1, 4], 3, "先跌后涨")
    test_case(solution, [3, 3, 5, 0, 0, 3, 1, 4], 8, "复杂波动")
    
    # 性能测试
    print("\n=== 性能测试 ===")
    large_prices = [random.randint(1, 1000) for _ in range(10000)]  # 1-1000的随机价格
    performance_test(solution, large_prices)

"""
算法总结与工程化思考：

1. 问题本质：无限次交易的最大利润问题
   - 关键洞察：可以多次买卖，只要后一天比前一天价格高就交易
   - 核心思路：收集所有的上涨区间

2. 时间复杂度对比：
   - 贪心算法：O(n) - 推荐
   - 动态规划：O(n) - 可接受
   - 空间优化：O(n) - 工程首选

3. 空间复杂度对比：
   - 贪心算法：O(1) - 最优
   - 动态规划：O(n) - 数组存储
   - 空间优化：O(1) - 最优

4. Python特性利用：
   - 多重赋值语法简化变量交换
   - 内置max函数简化比较逻辑
   - 列表推导式简化数组操作

5. 特殊情况处理：
   - 价格不变：利润为0
   - 连续上涨：利润为最后-最初
   - 连续下跌：利润为0
   - 波动市场：收集所有上涨

6. 工程选择依据：
   - 一般情况：方法1（贪心算法）
   - 需要状态机思路：方法3（空间优化DP）
   - 直观理解：方法4（峰谷法）

7. 调试技巧：
   - 验证上涨区间的识别
   - 检查状态转移的正确性
   - 边界测试确保正确性

8. 关联题目：
   - 买卖股票的最佳时机（一次交易）
   - 买卖股票的最佳时机III（最多两次交易）
   - 买卖股票的最佳时机IV（最多k次交易）
   - 含冷冻期的买卖股票
   - 含手续费的买卖股票
"""
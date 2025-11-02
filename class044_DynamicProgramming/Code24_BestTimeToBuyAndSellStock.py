# 买卖股票的最佳时机 (Best Time to Buy and Sell Stock)
# 给定一个数组 prices ，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格。
# 你只能选择某一天买入这只股票，并选择在未来的某一个不同的日子卖出该股票。
# 设计一个算法来计算你所能获取的最大利润。
# 返回你可以从这笔交易中获取的最大利润。如果你不能获取任何利润，返回 0。
# 测试链接 : https://leetcode.cn/problems/best-time-to-buy-and-sell-stock/

from typing import List
import sys
import time
import random

class Solution:
    """
    买卖股票的最佳时机 - 一次交易的最大利润问题
    
    时间复杂度分析：
    - 暴力解法：O(n^2) - 枚举所有买卖组合
    - 动态规划：O(n) - 遍历数组一次
    - 空间优化：O(1) - 只保存必要的前一个状态
    
    空间复杂度分析：
    - 暴力解法：O(1) - 只保存当前最大值
    - 动态规划：O(n) - dp数组存储所有状态
    - 空间优化：O(1) - 工程首选
    
    工程化考量：
    1. 边界处理：空数组、单元素数组、递减数组
    2. 性能优化：空间优化版本应对大规模数据
    3. 代码清晰：明确的变量命名和状态转移逻辑
    4. Python特性：利用多重赋值简化变量交换
    """
    
    # 方法1：动态规划（记录历史最低价）
    # 时间复杂度：O(n) - 遍历数组一次
    # 空间复杂度：O(1) - 只使用常数空间
    # 核心思路：记录历史最低价，计算当前价格与历史最低价的差值
    def maxProfit1(self, prices: List[int]) -> int:
        if len(prices) <= 1:
            return 0
        
        n = len(prices)
        min_price = prices[0]  # 历史最低价
        max_profit = 0          # 最大利润
        
        for i in range(1, n):
            # 更新历史最低价
            min_price = min(min_price, prices[i])
            # 计算当前利润并更新最大值
            max_profit = max(max_profit, prices[i] - min_price)
        
        return max_profit

    # 方法2：Kadane算法变种（最大子数组和）
    # 时间复杂度：O(n) - 遍历数组一次
    # 空间复杂度：O(1) - 只使用常数空间
    # 核心思路：将价格差转化为最大子数组和问题
    def maxProfit2(self, prices: List[int]) -> int:
        if len(prices) <= 1:
            return 0
        
        n = len(prices)
        max_cur = 0      # 当前最大利润
        max_so_far = 0   # 全局最大利润
        
        for i in range(1, n):
            # 计算相邻两天的价格差
            diff = prices[i] - prices[i-1]
            # 使用Kadane算法思想
            max_cur = max(0, max_cur + diff)
            max_so_far = max(max_so_far, max_cur)
        
        return max_so_far

    # 方法3：动态规划（状态机）
    # 时间复杂度：O(n) - 遍历数组一次
    # 空间复杂度：O(n) - 使用dp数组
    # 核心思路：明确两个状态：持有股票和不持有股票
    def maxProfit3(self, prices: List[int]) -> int:
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
            # 第i天持有股票：昨天就持有 或 今天买入（只能买入一次）
            dp[i][1] = max(dp[i-1][1], -prices[i])
        
        return dp[n-1][0]  # 最后一天不持有股票才能获得最大利润

    # 方法4：空间优化的动态规划（状态机）
    # 时间复杂度：O(n) - 与方法3相同
    # 空间复杂度：O(1) - 只使用常数空间
    # 优化：使用变量代替数组，减少空间使用
    def maxProfit4(self, prices: List[int]) -> int:
        if len(prices) <= 1:
            return 0
        
        n = len(prices)
        dp0 = 0             # 不持有股票的最大利润
        dp1 = -prices[0]    # 持有股票的最大利润
        
        for i in range(1, n):
            # 使用多重赋值避免临时变量
            dp0, dp1 = (
                max(dp0, dp1 + prices[i]),
                max(dp1, -prices[i])
            )
        
        return dp0

    # 方法5：暴力解法（用于对比）
    # 时间复杂度：O(n^2) - 枚举所有买卖组合
    # 空间复杂度：O(1) - 只保存当前最大值
    # 问题：效率低，仅用于教学目的
    def maxProfit5(self, prices: List[int]) -> int:
        if len(prices) <= 1:
            return 0
        
        n = len(prices)
        max_profit = 0
        
        for i in range(n):
            for j in range(i+1, n):
                profit = prices[j] - prices[i]
                max_profit = max(max_profit, profit)
        
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
    print(f"方法1: {result1}, 耗时: {(end - start) * 1000:.2f}ms")
    
    start = time.time()
    result2 = solution.maxProfit2(prices)
    end = time.time()
    print(f"方法2: {result2}, 耗时: {(end - start) * 1000:.2f}ms")

if __name__ == "__main__":
    solution = Solution()
    
    print("=== 买卖股票的最佳时机测试 ===")
    
    # 边界测试
    test_case(solution, [], 0, "空数组")
    test_case(solution, [5], 0, "单元素数组")
    test_case(solution, [7, 1, 5, 3, 6, 4], 5, "示例1")
    test_case(solution, [7, 6, 4, 3, 1], 0, "递减数组")
    
    # LeetCode示例测试
    test_case(solution, [7, 1, 5, 3, 6, 4], 5, "LeetCode示例1")
    test_case(solution, [7, 6, 4, 3, 1], 0, "LeetCode示例2")
    test_case(solution, [1, 2], 1, "两天递增")
    test_case(solution, [2, 1], 0, "两天递减")
    
    # 常规测试
    test_case(solution, [1, 2, 3, 4, 5], 4, "连续递增")
    test_case(solution, [5, 4, 3, 2, 1], 0, "连续递减")
    test_case(solution, [2, 4, 1], 2, "先增后减")
    test_case(solution, [3, 2, 6, 5, 0, 3], 4, "复杂情况")
    
    # 性能测试
    print("\n=== 性能测试 ===")
    large_prices = [random.randint(1, 1000) for _ in range(10000)]  # 1-1000的随机价格
    performance_test(solution, large_prices)

"""
算法总结与工程化思考：

1. 问题本质：一次交易的最大利润问题
   - 关键洞察：在最低点买入，在最高点卖出（但卖出必须在买入之后）
   - 核心思路：记录历史最低价，计算当前价格与历史最低价的差值

2. 时间复杂度对比：
   - 暴力解法：O(n^2) - 不可接受
   - 动态规划：O(n) - 推荐
   - 空间优化：O(n) - 工程首选

3. 空间复杂度对比：
   - 暴力解法：O(1) - 但效率低
   - 动态规划：O(n) - 数组存储
   - 空间优化：O(1) - 最优

4. 特殊情况处理：
   - 价格递减：最大利润为0
   - 单元素数组：无法交易，利润为0
   - 空数组：利润为0
   - 价格全部相同：利润为0

5. Python特性利用：
   - 多重赋值语法简化变量交换
   - 内置max/min函数简化比较逻辑
   - 列表推导式简化数组操作

6. 工程选择依据：
   - 一般情况：方法1（记录历史最低价）
   - 需要状态机思路：方法4（空间优化状态机）
   - 需要Kadane算法：方法2（最大子数组和）

7. 调试技巧：
   - 跟踪历史最低价的变化
   - 验证价格差的计算
   - 检查边界情况处理

8. 关联题目：
   - 买卖股票的最佳时机II（无限次交易）
   - 买卖股票的最佳时机III（最多两次交易）
   - 买卖股票的最佳时机IV（最多k次交易）
   - 含冷冻期的买卖股票
   - 含手续费的买卖股票
"""
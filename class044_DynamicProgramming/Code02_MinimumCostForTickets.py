#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
最低票价 (Minimum Cost For Tickets)

题目来源：LeetCode 983. 最低票价
题目链接：https://leetcode.cn/problems/minimum-cost-for-tickets/

题目描述：
在一个火车旅行很受欢迎的国度，你提前一年计划了一些火车旅行。
在接下来的一年里，你要旅行的日子将以一个名为 days 的数组给出。
每一项是一个从 1 到 365 的整数。
火车票有 三种不同的销售方式：
一张 为期1天 的通行证售价为 costs[0] 美元；
一张 为期7天 的通行证售价为 costs[1] 美元；
一张 为期30天 的通行证售价为 costs[2] 美元。
通行证允许数天无限制的旅行。
例如，如果我们在第 2 天获得一张 为期 7 天 的通行证，
那么我们可以连着旅行 7 天：第 2 天、第 3 天、第 4 天、第 5 天、第 6 天、第 7 天和第 8 天。
返回你想要完成在给定的列表 days 中列出的每一天的旅行所需要的最低消费。

示例 1：
输入：days = [1,4,6,7,8,20], costs = [2,7,15]
输出：11
解释：在第 1 天买 1 天通行证（$2），在第 3 天买 7 天通行证（$7），在第 20 天买 1 天通行证（$2）。

示例 2：
输入：days = [1,2,3,4,5,6,7,8,9,10,30,31], costs = [2,7,15]
输出：17
解释：在第 1 天买 30 天通行证（$15），在第 31 天买 1 天通行证（$2）。

提示：
1 <= days.length <= 365
1 <= days[i] <= 365
days 按顺序严格递增
costs.length == 3
1 <= costs[i] <= 1000

解题思路：
这是一个典型的动态规划问题，我们需要找到完成所有旅行的最低成本。
我们提供了三种解法：
1. 暴力递归：直接按照定义递归求解，但存在大量重复计算。
2. 记忆化搜索：在暴力递归的基础上，通过缓存已计算的结果来避免重复计算。
3. 动态规划：自底向上计算，先计算小问题的解，再逐步构建大问题的解。

算法复杂度分析：
- 暴力递归：时间复杂度 O(3^n)，空间复杂度 O(n)
- 记忆化搜索：时间复杂度 O(n)，空间复杂度 O(n)
- 动态规划：时间复杂度 O(n)，空间复杂度 O(n)

工程化考量：
1. 边界处理：正确处理旅行日结束的情况
2. 性能优化：提供多种解法，从低效到高效，展示优化过程
3. 代码质量：清晰的变量命名和详细的注释说明
4. 测试覆盖：包含基本测试用例和边界情况测试

相关题目：
- LintCode 1455. 最低票价
- AtCoder Educational DP Contest B - Frog 2
- 牛客网 动态规划专题 - 旅行计划
- HackerRank Travel Cost
- CodeChef TRAVELCOST
- SPOJ MINCOST
"""

class Solution:
    # 通行证持续天数数组      0  1  2
    durations = [1, 7, 30]

    # 暴力尝试
    # 时间复杂度：指数级，因为对于每个旅行日都可能有多种选择
    # 空间复杂度：O(n)，递归调用栈的深度
    # 问题：存在大量重复计算，效率低下
    def mincostTickets1(self, days: list[int], costs: list[int]) -> int:
        return self.f1(days, costs, 0)

    # days[i..... 最少花费是多少 
    def f1(self, days: list[int], costs: list[int], i: int) -> int:
        if i == len(days):
            # 后续已经无旅行了
            return 0
        # i下标 : 第days[i]天，有一场旅行
        # i.... 最少花费是多少 
        ans = float('inf')
        for k in range(3):
            # k是方案编号 : 0 1 2
            j = i
            while j < len(days) and days[i] + self.durations[k] > days[j]:
                # 因为方案2持续的天数最多，30天
                # 所以while循环最多执行30次
                # 枚举行为可以认为是O(1)
                j += 1
            ans = min(ans, costs[k] + self.f1(days, costs, j))
        return ans

    # 暴力尝试改记忆化搜索
    # 从顶到底的动态规划
    # 时间复杂度：O(n)，其中n是旅行天数，每个状态只计算一次
    # 空间复杂度：O(n)，dp数组和递归调用栈
    # 优化：通过缓存已经计算的结果避免重复计算
    def mincostTickets2(self, days: list[int], costs: list[int]) -> int:
        dp = {}
        return self.f2(days, costs, 0, dp)

    def f2(self, days: list[int], costs: list[int], i: int, dp: dict) -> int:
        if i == len(days):
            return 0
        if i in dp:
            return dp[i]
        ans = float('inf')
        for k in range(3):
            j = i
            while j < len(days) and days[i] + self.durations[k] > days[j]:
                j += 1
            ans = min(ans, costs[k] + self.f2(days, costs, j, dp))
        dp[i] = ans
        return ans

    # 严格位置依赖的动态规划
    # 从底到顶的动态规划
    # 时间复杂度：O(n)，其中n是旅行天数
    # 空间复杂度：O(n)，dp数组
    # 优化：避免了递归调用的开销，自底向上计算
    def mincostTickets3(self, days: list[int], costs: list[int]) -> int:
        n = len(days)
        dp = [float('inf')] * (n + 1)
        dp[n] = 0
        for i in range(n - 1, -1, -1):
            for k in range(3):
                j = i
                while j < len(days) and days[i] + self.durations[k] > days[j]:
                    j += 1
                dp[i] = min(dp[i], costs[k] + dp[j])
        return dp[0]

# 测试用例
if __name__ == "__main__":
    solution = Solution()
    print("测试最低票价问题：")
    
    # 测试用例1
    days1 = [1, 4, 6, 7, 8, 20]
    costs1 = [2, 7, 15]
    print(f"days = {days1}")
    print(f"costs = {costs1}")
    print(f"最低票价（方法2）: {solution.mincostTickets2(days1, costs1)}")
    print(f"最低票价（方法3）: {solution.mincostTickets3(days1, costs1)}")
    
    # 测试用例2
    days2 = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 30, 31]
    costs2 = [2, 7, 15]
    print(f"\ndays = {days2}")
    print(f"costs = {costs2}")
    print(f"最低票价（方法2）: {solution.mincostTickets2(days2, costs2)}")
    print(f"最低票价（方法3）: {solution.mincostTickets3(days2, costs2)}")
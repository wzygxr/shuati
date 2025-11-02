# 使用最小花费爬楼梯 (Min Cost Climbing Stairs)
# 给你一个整数数组 cost ，其中 cost[i] 是从楼梯第 i 个台阶向上爬需要支付的费用。
# 一旦你支付此费用，即可选择向上爬一个或者两个台阶。
# 你可以选择从下标为 0 或下标为 1 的台阶开始爬楼梯。
# 请你计算并返回达到楼梯顶部的最低花费。
# 测试链接 : https://leetcode.cn/problems/min-cost-climbing-stairs/

import time
from typing import List
from functools import lru_cache

class Solution:
    """
    使用最小花费爬楼梯 - 动态规划经典问题
    
    时间复杂度分析：
    - 暴力递归：O(2^n) 指数级，存在大量重复计算
    - 记忆化搜索：O(n) 每个状态只计算一次
    - 动态规划：O(n) 线性时间复杂度
    - 空间优化：O(1) 只保存必要的前两个状态
    
    空间复杂度分析：
    - 暴力递归：O(n) 递归调用栈深度
    - 记忆化搜索：O(n) 递归栈 + 记忆化缓存
    - 动态规划：O(n) dp数组存储所有状态
    - 空间优化：O(1) 工程首选
    
    工程化考量：
    1. 异常处理：空数组、单元素数组等边界情况
    2. 边界测试：cost长度为0,1,2的情况
    3. 性能优化：空间优化版本应对大规模数据
    4. Python特性：利用装饰器简化记忆化实现
    """
    
    # 方法1：暴力递归解法
    # 时间复杂度：O(2^n) - 指数级时间复杂度，效率极低
    # 空间复杂度：O(n) - 递归调用栈的深度
    # 问题：存在大量重复计算，n较大时栈溢出
    def minCostClimbingStairs1(self, cost: List[int]) -> int:
        if not cost:
            return 0
        n = len(cost)
        # 可以从第0阶或第1阶开始，取最小值
        return min(self.dfs1(cost, n - 1), self.dfs1(cost, n - 2))
    
    def dfs1(self, cost: List[int], i: int) -> int:
        if i < 0:
            return 0
        if i == 0 or i == 1:
            return cost[i]
        
        return cost[i] + min(self.dfs1(cost, i - 1), self.dfs1(cost, i - 2))

    # 方法2：记忆化搜索（使用装饰器）
    # 时间复杂度：O(n) - 每个状态只计算一次
    # 空间复杂度：O(n) - 递归栈和缓存空间
    # 优化：通过缓存避免重复计算，Pythonic实现
    def minCostClimbingStairs2(self, cost: List[int]) -> int:
        if not cost:
            return 0
        if len(cost) == 1:
            return 0
        
        n = len(cost)
        # 将列表转换为元组以便使用lru_cache
        cost_tuple = tuple(cost)
        return min(self.dfs2(cost_tuple, n - 1), self.dfs2(cost_tuple, n - 2))
    
    @lru_cache(maxsize=None)
    def dfs2(self, cost: tuple, i: int) -> int:
        # 注意：lru_cache需要不可变对象，所以cost转为tuple
        if i < 0:
            return 0
        if i == 0 or i == 1:
            return cost[i]
        
        return cost[i] + min(self.dfs2(cost, i - 1), self.dfs2(cost, i - 2))

    # 方法3：动态规划（自底向上）
    # 时间复杂度：O(n) - 从底向上计算每个状态
    # 空间复杂度：O(n) - dp数组存储所有状态
    # 优化：避免了递归调用的开销
    def minCostClimbingStairs3(self, cost: List[int]) -> int:
        if not cost:
            return 0
        if len(cost) == 1:
            return 0
        
        n = len(cost)
        dp = [0] * n
        
        # 初始化基础情况
        dp[0] = cost[0]
        dp[1] = cost[1]
        
        # 状态转移：到达第i阶的最小花费 = cost[i] + min(到达i-1阶的最小花费, 到达i-2阶的最小花费)
        for i in range(2, n):
            dp[i] = cost[i] + min(dp[i - 1], dp[i - 2])
        
        # 可以从最后两阶直接到达楼顶，取最小值
        return min(dp[n - 1], dp[n - 2])

    # 方法4：空间优化的动态规划
    # 时间复杂度：O(n) - 仍然需要计算所有状态
    # 空间复杂度：O(1) - 只保存必要的前两个状态值
    # 优化：大幅减少空间使用，工程首选
    def minCostClimbingStairs4(self, cost: List[int]) -> int:
        if not cost:
            return 0
        if len(cost) == 1:
            return 0
        
        n = len(cost)
        prev2 = cost[0]  # 到达第i-2阶的最小花费
        prev1 = cost[1]  # 到达第i-1阶的最小花费
        
        for i in range(2, n):
            current = cost[i] + min(prev1, prev2)
            prev2, prev1 = prev1, current
        
        return min(prev1, prev2)

    # 方法5：更直观的动态规划（从楼顶向下看）
    # 时间复杂度：O(n) - 遍历数组一次
    # 空间复杂度：O(n) - dp数组
    # 核心思路：dp[i]表示到达第i阶（包括楼顶）的最小花费
    def minCostClimbingStairs5(self, cost: List[int]) -> int:
        if not cost:
            return 0
        
        n = len(cost)
        dp = [0] * (n + 1)  # dp[n]表示到达楼顶的最小花费
        
        # 初始化：从第0阶或第1阶开始不需要花费（但需要支付该阶的费用）
        dp[0] = 0
        dp[1] = 0
        
        for i in range(2, n + 1):
            # 到达第i阶的最小花费 = min(从i-1阶上来, 从i-2阶上来) + 相应的费用
            dp[i] = min(dp[i - 1] + cost[i - 1], dp[i - 2] + cost[i - 2])
        
        return dp[n]

def test_case(solution: Solution, cost: List[int], expected: int, description: str):
    """测试用例函数"""
    result1 = solution.minCostClimbingStairs1(cost)
    result2 = solution.minCostClimbingStairs2(cost)
    result3 = solution.minCostClimbingStairs3(cost)
    result4 = solution.minCostClimbingStairs4(cost)
    result5 = solution.minCostClimbingStairs5(cost)
    
    all_correct = (result1 == expected and result2 == expected and 
                  result3 == expected and result4 == expected and result5 == expected)
    
    status = "✓" if all_correct else "✗"
    print(f"{description}: {status}")
    
    if not all_correct:
        print(f"  方法1: {result1} | 方法2: {result2} | 方法3: {result3} | "
              f"方法4: {result4} | 方法5: {result5} | 预期: {expected}")

def performance_test(solution: Solution, cost: List[int]):
    """性能测试函数"""
    print(f"\n性能测试 n={len(cost)}:")
    
    start = time.time()
    result3 = solution.minCostClimbingStairs3(cost)
    end = time.time()
    print(f"动态规划: {result3}, 耗时: {(end - start) * 1000:.2f}ms")
    
    start = time.time()
    result4 = solution.minCostClimbingStairs4(cost)
    end = time.time()
    print(f"空间优化: {result4}, 耗时: {(end - start) * 1000:.2f}ms")
    
    start = time.time()
    result5 = solution.minCostClimbingStairs5(cost)
    end = time.time()
    print(f"楼顶视角: {result5}, 耗时: {(end - start) * 1000:.2f}ms")

if __name__ == "__main__":
    solution = Solution()
    
    print("=== 使用最小花费爬楼梯测试 ===")
    
    # 边界测试
    test_case(solution, [], 0, "空数组")
    test_case(solution, [10], 0, "单元素数组")
    test_case(solution, [10, 15], 10, "双元素数组")
    
    # LeetCode示例测试
    test_case(solution, [10, 15, 20], 15, "示例1")
    test_case(solution, [1, 100, 1, 1, 1, 100, 1, 1, 100, 1], 6, "示例2")
    
    # 常规测试
    test_case(solution, [0, 0, 0, 0], 0, "全零费用")
    test_case(solution, [1, 2, 3, 4, 5], 6, "递增费用")
    test_case(solution, [5, 4, 3, 2, 1], 6, "递减费用")
    
    # 性能测试
    print("\n=== 性能测试 ===")
    large_cost = [1] * 1000  # 1000个1
    performance_test(solution, large_cost)

"""
算法总结与工程化思考：

1. 问题本质：斐波那契数列的变种，但每个台阶有相应的费用
   f(i) = cost[i] + min(f(i-1), f(i-2))

2. 关键洞察：
   - 可以从第0阶或第1阶开始爬楼梯
   - 楼顶在数组长度之外（索引n）
   - 最终结果是最后两阶的最小值

3. 时间复杂度对比：
   - 暴力递归：O(2^n) - 不可接受
   - 记忆化搜索：O(n) - 可接受
   - 动态规划：O(n) - 推荐
   - 空间优化：O(n) - 工程首选

4. 空间复杂度对比：
   - 暴力递归：O(n) - 栈深度
   - 记忆化搜索：O(n) - 递归栈+缓存
   - 动态规划：O(n) - 数组存储
   - 空间优化：O(1) - 最优

5. Python特性利用：
   - @lru_cache装饰器简化记忆化实现
   - 多重赋值语法简化变量交换
   - 列表推导式简化数组操作

6. 工程选择依据：
   - 面试笔试：方法4（空间优化）
   - 大规模数据：方法4或方法5
   - 代码清晰：方法5（楼顶视角）

7. 调试技巧：
   - 打印dp数组验证状态转移
   - 边界测试确保正确性
   - 性能测试选择最优算法

8. 关联题目：
   - 爬楼梯问题（无费用版本）
   - 打家劫舍问题（相邻元素不能同时选择）
   - 斐波那契数列
"""
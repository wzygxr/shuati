#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
选择数字使集合和相差最小 - Python实现

问题描述:
给定正数n和k，从1~n中选择k个数字组成集合A，剩下数字组成集合B
希望集合A和集合B的累加和相差不超过1
返回集合A选择的数字，如果无法做到返回空数组

解题思路:
基于数学构造的贪心算法，直接构造最优解
1. 计算总和sum = n*(n+1)/2
2. 目标让集合A的和接近sum/2
3. 使用数学构造方法直接生成解

约束条件:
2 <= n <= 10^6
1 <= k <= n

工程化考量:
1. 使用类型注解提高代码可读性
2. 处理整数溢出问题
3. 实现完整的单元测试
4. 提供性能测试功能
5. 添加输入验证和边界检查
"""

from typing import List
import math
import time

class Code02_PickNumbersClosedSum:
    """
    选择数字算法解决方案类
    提供基于数学构造的最优解法
    """
    
    @staticmethod
    def pick(n: int, k: int) -> List[int]:
        """
        正式方法 - 最优解
        基于数学构造的贪心算法
        
        算法原理:
        1. 计算总和sum = n*(n+1)/2
        2. 目标让集合A的和接近sum/2
        3. 先尝试让集合A的和为sum/2
        4. 如果失败且总和为奇数，再尝试(sum/2)+1
        
        构造策略:
        1. 选择最小的leftSize个数字: 1, 2, ..., leftSize
        2. 选择最大的rightSize个数字: n, n-1, ..., n-rightSize+1
        3. 如果还有剩余需求，选择一个中间数字
        
        时间复杂度: O(k)
        空间复杂度: O(k)
        
        Args:
            n: 数字范围上限
            k: 需要选择的数字个数
            
        Returns:
            List[int]: 选择的数字列表，如果无解返回空列表
        """
        # 输入验证
        if n <= 0 or k <= 0 or k > n:
            return []
            
        total_sum = n * (n + 1) // 2
        
        # 尝试让集合A的和为sum/2
        result = Code02_PickNumbersClosedSum.generate(total_sum // 2, n, k)
        
        # 如果失败且总和为奇数，尝试(sum/2)+1
        if not result and (total_sum & 1) == 1:
            result = Code02_PickNumbersClosedSum.generate(total_sum // 2 + 1, n, k)
            
        return result
        
    @staticmethod
    def generate(target_sum: int, n: int, k: int) -> List[int]:
        """
        生成满足条件的数字集合
        
        数学构造原理:
        1. 最小可能的k个数字和: minKSum = k*(k+1)/2
        2. 最大可能的k个数字和: maxKSum = minKSum + (n-k)*k
        3. 如果目标sum不在[minKSum, maxKSum]范围内，无解
        4. 使用贪心构造方法生成解
        
        构造策略:
        1. 选择最小的leftSize个数字: 1, 2, ..., leftSize
        2. 选择最大的rightSize个数字: n, n-1, ..., n-rightSize+1
        3. 如果还有剩余需求，选择一个中间数字
        
        Args:
            target_sum: 目标和
            n: 数字范围上限
            k: 需要选择的数字个数
            
        Returns:
            List[int]: 选择的数字列表，如果无解返回空列表
        """
        # 计算最小k个数字的和
        min_k_sum = k * (k + 1) // 2
        range_size = n - k
        
        # 检查目标sum是否在可行范围内
        if target_sum < min_k_sum or target_sum > min_k_sum + range_size * k:
            return []
            
        # 计算需要额外增加的和
        need = target_sum - min_k_sum
        
        # 计算右半部分的大小（选择最大的几个数字）
        right_size = need // range_size
        
        # 计算中间索引位置
        mid_index = (k - right_size) + (need % range_size)
        
        # 计算左半部分的大小
        left_size = k - right_size - (1 if need % range_size != 0 else 0)
        
        # 构造结果数组
        result = [0] * k
        
        # 填充左半部分（最小的几个数字）
        for i in range(left_size):
            result[i] = i + 1
            
        # 如果有中间元素，填充中间元素
        if need % range_size != 0:
            result[left_size] = mid_index
            
        # 填充右半部分（最大的几个数字）
        for i in range(k - 1, k - 1 - right_size, -1):
            result[i] = n - (k - 1 - i)
            
        return result
        
    @staticmethod
    def validate(n: int, k: int, result: List[int]) -> bool:
        """
        验证结果是否正确
        检查生成的集合是否满足条件
        
        Args:
            n: 数字范围上限
            k: 需要选择的数字个数
            result: 生成的数字列表
            
        Returns:
            bool: 验证结果
        """
        if not result:
            # 如果返回空数组，检查是否真的无解
            return not Code02_PickNumbersClosedSum.can_split(n, k)
            
        if len(result) != k:
            return False
            
        total_sum = n * (n + 1) // 2
        pick_sum = sum(result)
        diff = abs(pick_sum - (total_sum - pick_sum))
        
        return diff <= 1
        
    @staticmethod
    def can_split(n: int, k: int) -> bool:
        """
        记忆化搜索方法（用于验证）
        不是最优解，只是为了验证正确性
        """
        total_sum = n * (n + 1) // 2
        want_sum = (total_sum // 2) + (1 if total_sum & 1 else 0)
        
        # 使用三维数组进行记忆化搜索
        dp = [[[0] * (want_sum + 1) for _ in range(k + 1)] for _ in range(n + 1)]
        
        return Code02_PickNumbersClosedSum._dfs(n, 1, k, want_sum, dp)
        
    @staticmethod
    def _dfs(n: int, i: int, k: int, s: int, dp: List[List[List[int]]]) -> bool:
        """
        深度优先搜索辅助函数
        """
        if k < 0 or s < 0:
            return False
            
        if i == n + 1:
            return k == 0 and s == 0
            
        if dp[i][k][s] != 0:
            return dp[i][k][s] == 1
            
        result = (Code02_PickNumbersClosedSum._dfs(n, i + 1, k, s, dp) or
                  Code02_PickNumbersClosedSum._dfs(n, i + 1, k - 1, s - i, dp))
                  
        dp[i][k][s] = 1 if result else -1
        return result
        
    @staticmethod
    def can_partition(nums: List[int]) -> bool:
        """
        类似题目1: 分割等和子集（LeetCode 416）
        0-1背包问题的动态规划解法
        
        时间复杂度: O(n × target)
        空间复杂度: O(target)
        """
        total_sum = sum(nums)
        
        # 如果总和是奇数，不可能分成两部分
        if total_sum & 1:
            return False
            
        target = total_sum // 2
        n = len(nums)
        
        # 使用一维数组进行空间优化
        dp = [False] * (target + 1)
        dp[0] = True
        
        for num in nums:
            # 从后往前遍历，避免重复使用
            for j in range(target, num - 1, -1):
                dp[j] = dp[j] or dp[j - num]
                
        return dp[target]
        
    @staticmethod
    def find_target_sum_ways(nums: List[int], S: int) -> int:
        """
        类似题目2: 目标和（LeetCode 494）
        动态规划求方案数
        
        算法思路:
        设正数集合为P，负数集合为N，则sum(P) - sum(N) = S
        又因为sum(P) + sum(N) = sum(nums)
        联立得：sum(P) = (S + sum(nums)) / 2
        问题转化为：在数组中选择一些数字，使其和为(S + sum(nums)) / 2的方案数
        """
        total_sum = sum(nums)
        
        # 边界条件检查
        if abs(S) > total_sum or (total_sum + S) % 2 == 1:
            return 0
            
        target = (total_sum + S) // 2
        dp = [0] * (target + 1)
        dp[0] = 1
        
        for num in nums:
            for j in range(target, num - 1, -1):
                dp[j] += dp[j - num]
                
        return dp[target]
        
    @staticmethod
    def coin_change(coins: List[int], amount: int) -> int:
        """
        类似题目3: 零钱兑换问题（LeetCode 322）
        完全背包问题求最小硬币数
        """
        # 初始化DP数组，值为amount+1表示不可能达到
        dp = [amount + 1] * (amount + 1)
        dp[0] = 0
        
        for i in range(1, amount + 1):
            for coin in coins:
                if coin <= i:
                    dp[i] = min(dp[i], dp[i - coin] + 1)
                    
        return -1 if dp[amount] > amount else dp[amount]
        
    @staticmethod
    def test() -> None:
        """单元测试函数"""
        print("=== 测试选择数字算法 ===")
        
        # 测试用例1
        n1, k1 = 10, 4
        result1 = Code02_PickNumbersClosedSum.pick(n1, k1)
        print(f"n={n1}, k={k1}: {result1}")
        print(f"验证结果: {'通过' if Code02_PickNumbersClosedSum.validate(n1, k1, result1) else '失败'}")
        
        # 测试用例2
        n2, k2 = 5, 2
        result2 = Code02_PickNumbersClosedSum.pick(n2, k2)
        print(f"n={n2}, k={k2}: {result2}")
        print(f"验证结果: {'通过' if Code02_PickNumbersClosedSum.validate(n2, k2, result2) else '失败'}")
        
        # 测试类似题目
        nums = [1, 5, 11, 5]
        result3 = Code02_PickNumbersClosedSum.can_partition(nums)
        print(f"分割等和子集结果: {result3}")
        
        print("=== 测试完成 ===")
        
    @staticmethod
    def performance_test() -> None:
        """性能测试函数"""
        print("\n=== 性能测试 ===")
        
        # 大规模测试
        n = 1000000
        k = 500000
        
        start_time = time.time()
        result = Code02_PickNumbersClosedSum.pick(n, k)
        end_time = time.time()
        
        execution_time = (end_time - start_time) * 1000  # 转换为毫秒
        print(f"大规模测试(n={n}, k={k})完成")
        print(f"执行时间: {execution_time:.2f}ms")
        print(f"结果大小: {len(result)}")
        
    @staticmethod
    def main() -> None:
        """主函数"""
        Code02_PickNumbersClosedSum.test()
        Code02_PickNumbersClosedSum.performance_test()

if __name__ == "__main__":
    Code02_PickNumbersClosedSum.main()

"""
调试技巧:
1. 打印中间计算结果验证数学构造
2. 对比不同规模数据的性能表现
3. 使用断言验证关键条件

面试要点:
1. 理解数学构造的原理和证明
2. 能够处理整数溢出问题
3. 掌握贪心选择的策略
4. 了解算法的时间复杂度分析

语言特性差异:
1. Python使用//进行整数除法
2. Python列表索引从0开始
3. Python没有内置的长整数溢出问题
4. Python的类型注解可以提高代码可读性
"""
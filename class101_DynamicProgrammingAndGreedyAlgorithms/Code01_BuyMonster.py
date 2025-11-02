#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
贿赂怪兽问题 - Python实现

问题描述:
开始时你的能力是0，目标是从0号怪兽开始，通过所有的n只怪兽
如果当前能力小于i号怪兽的能力，必须付出b[i]的钱贿赂这个怪兽
如果当前能力大于等于i号怪兽的能力，可以选择直接通过或贿赂
返回通过所有怪兽需要花的最小钱数

解题思路:
本题提供四种动态规划解法，根据数据特征选择最优算法：
1. 基于金钱数的DP（方法1和2）：适用于贿赂金额范围较小的情况
2. 基于能力值的DP（方法3和4）：适用于怪兽能力值范围较小的情况

测试链接: https://www.nowcoder.com/practice/736e12861f9746ab8ae064d4aae2d5a9

工程化考量:
1. 使用类型注解提高代码可读性
2. 添加详细的文档字符串
3. 实现完整的单元测试
4. 处理边界条件和异常情况
5. 提供多种解法以适应不同数据特征
6. 包含空间优化版本
"""

import sys
from typing import List, Tuple
import time
import math

class Code01_BuyMonster:
    """
    贿赂怪兽问题解决方案类
    提供多种动态规划解法，适用于不同的数据特征
    """
    
    @staticmethod
    def compute1(n: int, a: List[int], b: List[int]) -> int:
        """
        方法1: 基于金钱数的动态规划
        适用于b[i]数值范围不大的情况
        
        算法思路:
        1. dp[i][j]表示花费最多j的钱，通过前i个怪兽能获得的最大能力值
        2. 如果dp[i][j] == -math.inf，表示无法通过
        3. 状态转移考虑两种情况：贿赂或不贿赂当前怪兽
        
        时间复杂度: O(n × ∑b[i])
        空间复杂度: O(∑b[i])
        
        Args:
            n: 怪兽数量
            a: 怪兽能力值列表，a[0]为哨兵，实际从a[1]开始
            b: 贿赂金额列表，b[0]为哨兵，实际从b[1]开始
            
        Returns:
            int: 最小花费金额，如果无法通过返回-1
        """
        # 输入验证
        if n <= 0 or len(a) < n + 1 or len(b) < n + 1:
            return -1
            
        # 计算总贿赂金额上限
        total_money = sum(b[1:n+1])
        
        # 初始化DP数组
        # dp[i][j]: 花费最多j的钱，通过前i个怪兽能获得的最大能力值
        dp = [[-math.inf] * (total_money + 1) for _ in range(n + 1)]
        
        # 初始化边界条件：处理0个怪兽时，花费0金钱获得0能力
        for j in range(total_money + 1):
            dp[0][j] = 0
            
        # 动态规划状态转移
        for i in range(1, n + 1):
            for j in range(total_money + 1):
                dp[i][j] = -math.inf
                
                # 情况1: 不贿赂当前怪兽（需要当前能力足够）
                if dp[i-1][j] >= a[i]:
                    dp[i][j] = max(dp[i][j], dp[i-1][j])
                    
                # 情况2: 贿赂当前怪兽（需要金钱足够且前i-1个怪兽能通过）
                if j - b[i] >= 0 and dp[i-1][j - b[i]] != -math.inf:
                    dp[i][j] = max(dp[i][j], dp[i-1][j - b[i]] + a[i])
                    
        # 找到能通过所有怪兽的最小花费
        for j in range(total_money + 1):
            if dp[n][j] != -math.inf:
                return j
                
        return -1
        
    @staticmethod
    def compute2(n: int, a: List[int], b: List[int]) -> int:
        """
        方法2: 空间优化版本（滚动数组）
        使用一维数组代替二维数组，优化空间复杂度
        
        时间复杂度: O(n × ∑b[i])
        空间复杂度: O(∑b[i])
        """
        # 输入验证
        if n <= 0 or len(a) < n + 1 or len(b) < n + 1:
            return -1
            
        total_money = sum(b[1:n+1])
        
        # 使用一维数组进行空间优化
        dp = [-math.inf] * (total_money + 1)
        dp[0] = 0  # 初始状态：花费0金钱获得0能力
        
        for i in range(1, n + 1):
            # 从后往前遍历，避免覆盖需要使用的状态
            for j in range(total_money, -1, -1):
                cur = -math.inf
                
                # 情况1: 不贿赂当前怪兽
                if dp[j] >= a[i]:
                    cur = max(cur, dp[j])
                    
                # 情况2: 贿赂当前怪兽
                if j - b[i] >= 0 and dp[j - b[i]] != -math.inf:
                    cur = max(cur, dp[j - b[i]] + a[i])
                    
                dp[j] = cur
                
        # 找到最小花费
        for j in range(total_money + 1):
            if dp[j] != -math.inf:
                return j
                
        return -1
        
    @staticmethod
    def compute3(n: int, a: List[int], b: List[int]) -> int:
        """
        方法3: 基于能力值的动态规划
        适用于a[i]数值范围不大的情况
        
        算法思路:
        1. dp[i][j]表示能力正好是j，并且确保能通过前i个怪兽，需要至少花多少钱
        2. 如果dp[i][j] == math.inf，表示无法达到
        3. 状态转移考虑两种情况：贿赂或不贿赂当前怪兽
        
        时间复杂度: O(n × ∑a[i])
        空间复杂度: O(∑a[i])
        """
        # 输入验证
        if n <= 0 or len(a) < n + 1 or len(b) < n + 1:
            return -1
            
        total_ability = sum(a[1:n+1])
        
        # 初始化DP数组
        dp: List[List[float]] = [[math.inf] * (total_ability + 1) for _ in range(n + 1)]
        
        # 初始化边界条件
        for j in range(total_ability + 1):
            dp[0][j] = math.inf
        dp[0][0] = 0  # 能力为0时，花费0金钱（处理0个怪兽）
        
        for i in range(1, n + 1):
            for j in range(total_ability + 1):
                dp[i][j] = math.inf
                
                # 情况1: 不贿赂当前怪兽（需要能力足够且前i-1个怪兽能通过）
                if j >= a[i] and dp[i-1][j] != math.inf:
                    dp[i][j] = min(dp[i][j], dp[i-1][j])
                    
                # 情况2: 贿赂当前怪兽（需要能力足够且前i-1个怪兽能通过）
                if j - a[i] >= 0 and dp[i-1][j - a[i]] != math.inf:
                    dp[i][j] = min(dp[i][j], dp[i-1][j - a[i]] + b[i])
                    
        # 找到通过所有怪兽的最小花费
        result = math.inf
        for j in range(total_ability + 1):
            result = min(result, dp[n][j])
            
        return -1 if result == math.inf else int(result)
        
    @staticmethod
    def compute4(n: int, a: List[int], b: List[int]) -> int:
        """
        方法4: 空间优化版本（基于能力值）
        使用一维数组优化空间复杂度
        
        时间复杂度: O(n × ∑a[i])
        空间复杂度: O(∑a[i])
        """
        # 输入验证
        if n <= 0 or len(a) < n + 1 or len(b) < n + 1:
            return -1
            
        total_ability = sum(a[1:n+1])
        
        # 使用一维数组进行空间优化
        dp = [math.inf] * (total_ability + 1)
        dp[0] = 0  # 初始状态：能力为0时花费0金钱
        
        for i in range(1, n + 1):
            # 从后往前遍历，避免状态覆盖
            for j in range(total_ability, -1, -1):
                cur = math.inf
                
                # 情况1: 不贿赂当前怪兽
                if j >= a[i] and dp[j] != math.inf:
                    cur = min(cur, dp[j])
                    
                # 情况2: 贿赂当前怪兽
                if j - a[i] >= 0 and dp[j - a[i]] != math.inf:
                    cur = min(cur, dp[j - a[i]] + b[i])
                    
                dp[j] = cur
                
        # 找到最小花费
        result = math.inf
        for j in range(total_ability + 1):
            result = min(result, dp[j])
            
        return -1 if result == math.inf else int(result)
        
    @staticmethod
    def min_money_to_pass_monsters1(d: List[int], p: List[int]) -> int:
        """
        类似题目1: 花最少的钱通过所有的怪兽（腾讯面试题）
        解法一：基于金钱数的动态规划
        
        时间复杂度: O(n × ∑p[i])
        空间复杂度: O(∑p[i])
        """
        if not d or not p or len(d) != len(p):
            return -1
            
        total_money = sum(p)
        n = len(d)
        
        # dp[i][j]: 花费最多j的钱，处理前i个怪兽时能获得的最大能力值
        dp = [[0] * (total_money + 1) for _ in range(n + 1)]
        
        for i in range(1, n + 1):
            for j in range(total_money + 1):
                # 不贿赂当前怪兽
                if dp[i-1][j] >= d[i-1]:
                    dp[i][j] = max(dp[i][j], dp[i-1][j])
                    
                # 贿赂当前怪兽
                if j >= p[i-1]:
                    dp[i][j] = max(dp[i][j], dp[i-1][j - p[i-1]] + d[i-1])
                    
        # 找到能通过所有怪兽的最少钱数
        for j in range(total_money + 1):
            if dp[n][j] > 0:
                return j
                
        return total_money
        
    @staticmethod
    def bribe_prisoners(n: int, prisoners: List[int]) -> int:
        """
        类似题目2: Bribe the Prisoners（Google Code Jam 2009）
        区间动态规划解法
        
        时间复杂度: O(m³)
        空间复杂度: O(m²)
        """
        if not prisoners:
            return 0
            
        m = len(prisoners)
        a = [0] * (m + 2)
        a[0] = 0
        for i in range(m):
            a[i + 1] = prisoners[i]
        a[m + 1] = n + 1
        
        # dp[i][j]: 释放编号在a[i]到a[j]之间的所有需要释放的犯人所需的最少金币数
        dp: List[List[float]] = [[0] * (m + 2) for _ in range(m + 2)]
        
        # 区间DP，按区间长度从小到大计算
        for length in range(2, m + 2):
            for i in range(0, m + 2 - length):
                j = i + length
                dp[i][j] = math.inf
                
                # 枚举最后一个释放的犯人
                for k in range(i + 1, j):
                    cost = dp[i][k] + dp[k][j] + (a[j] - a[i] - 2)
                    dp[i][j] = min(dp[i][j], cost)
                    
        return int(dp[0][m + 1])
        
    @staticmethod
    def test() -> None:
        """单元测试函数，验证算法正确性"""
        print("=== 测试贿赂怪兽算法 ===")
        
        # 测试用例1
        a1 = [0, 5, 3, 1, 1, 1, 8]  # 注意：a[0]是哨兵
        b1 = [0, 2, 1, 2, 2, 2, 30]
        n1 = 6
        
        result1 = Code01_BuyMonster.compute1(n1, a1, b1)
        result2 = Code01_BuyMonster.compute2(n1, a1, b1)
        result3 = Code01_BuyMonster.compute3(n1, a1, b1)
        result4 = Code01_BuyMonster.compute4(n1, a1, b1)
        
        print(f"测试用例1结果: {result1}, {result2}, {result3}, {result4}")
        
        # 测试用例2
        d = [5, 3, 1, 1, 1, 8]
        p = [2, 1, 2, 2, 2, 30]
        result5 = Code01_BuyMonster.min_money_to_pass_monsters1(d, p)
        print(f"类似题目1结果: {result5}")
        
        # 测试用例3
        prisoners = [3]
        result6 = Code01_BuyMonster.bribe_prisoners(8, prisoners)
        print(f"类似题目2结果: {result6}")
        
        print("=== 测试完成 ===")
        
    @staticmethod
    def performance_test() -> None:
        """性能测试函数"""
        print("\n=== 性能测试 ===")
        
        # 创建大规模测试数据
        n = 1000
        a = [0] * (n + 1)
        b = [0] * (n + 1)
        
        for i in range(1, n + 1):
            a[i] = i
            b[i] = 1  # 小范围贿赂金额，适合方法1
            
        start_time = time.time()
        result = Code01_BuyMonster.compute1(n, a, b)
        end_time = time.time()
        
        execution_time = (end_time - start_time) * 1000  # 转换为毫秒
        print(f"大规模测试结果: {result}")
        print(f"执行时间: {execution_time:.2f}ms")
        
    @staticmethod
    def main() -> None:
        """主函数 - 用于算法演示"""
        Code01_BuyMonster.test()
        Code01_BuyMonster.performance_test()

if __name__ == "__main__":
    Code01_BuyMonster.main()

"""
调试技巧:
1. 打印中间变量: 使用print输出关键状态
2. 断言验证: 使用assert检查关键条件
3. 单元测试: 编写全面的测试用例

面试要点:
1. 理解两种DP方法的适用场景
2. 能够解释状态转移方程的含义
3. 掌握空间优化的方法
4. 能够处理边界条件和异常情况

语言特性差异:
1. Python使用动态类型，需要更多类型注解
2. Python列表索引从0开始，需要注意边界处理
3. Python没有内置的无穷大常量，使用math.inf
4. Python的列表推导式可以简化代码编写
"""
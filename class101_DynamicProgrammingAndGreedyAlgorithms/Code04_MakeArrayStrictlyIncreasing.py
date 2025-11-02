#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
使数组严格递增的最小操作数 - Python实现

问题描述:
给定两个整数数组arr1和arr2
通过将arr1中的元素替换为arr2中的元素，使arr1严格递增
返回最小操作数，如果无法做到返回-1

解题思路:
使用动态规划+二分查找优化
1. 对arr2进行排序和去重
2. 使用记忆化搜索或严格位置依赖的动态规划
3. 对于每个位置，枚举可能的替换策略
4. 使用二分查找加速搜索过程

约束条件:
1 <= arr1.length, arr2.length <= 2000
0 <= arr1[i], arr2[i] <= 10^9

测试链接: https://leetcode.cn/problems/make-array-strictly-increasing/

工程化考量:
1. 使用类型注解提高代码可读性
2. 添加输入验证和边界检查
3. 实现完整的单元测试
4. 提供性能测试功能
5. 使用bisect模块简化二分查找实现
"""

from typing import List
import bisect
import math
import time

class Code04_MakeArrayStrictlyIncreasing:
    """
    使数组严格递增算法解决方案类
    提供基于动态规划的高效解法
    """
    
    @staticmethod
    def make_array_increasing(arr1: List[int], arr2: List[int]) -> int:
        """
        方法1: 记忆化搜索解法
        使用深度优先搜索+记忆化
        
        时间复杂度: O(n² log m)
        空间复杂度: O(n)
        """
        # 输入验证
        if not arr1:
            return 0
            
        # 对arr2进行排序和去重
        arr2_sorted = sorted(set(arr2))
        m = len(arr2_sorted)
        n = len(arr1)
        
        # 记忆化数组，使用字典避免类型问题
        memo = {}
        
        def dfs(i: int, prev: int) -> int:
            """
            深度优先搜索辅助函数
            
            Args:
                i: 当前处理的位置
                prev: 前一个元素的值
                
            Returns:
                int: 最小操作数
            """
            if i == n:
                return 0
                
            if (i, prev) in memo:
                return memo[(i, prev)]
                
            result = 10**9  # 使用大整数代替math.inf
            current_prev = prev
            
            # 在arr2中找到第一个大于prev的位置
            pos = bisect.bisect_right(arr2_sorted, current_prev)
            
            # 枚举所有可能的替换策略
            for j in range(i, n + 1):
                ops = j - i  # 操作次数
                
                if j == n:
                    # 到达数组末尾
                    result = min(result, ops)
                else:
                    # 检查是否可以不替换当前元素
                    if current_prev < arr1[j]:
                        next_ops = dfs(j + 1, arr1[j])
                        if next_ops != 10**9:
                            result = min(result, ops + next_ops)
                    
                    # 尝试替换当前元素
                    if pos < m:
                        current_prev = arr2_sorted[pos]
                        pos += 1
                    else:
                        break
                        
            memo[(i, prev)] = result
            return result
            
        import sys
        sys.setrecursionlimit(10000)  # 增加递归深度限制
        result = dfs(0, -10**9)  # 使用足够小的数代替-math.inf
        return -1 if result == 10**9 else result
        
    @staticmethod
    def make_array_increasing_dp(arr1: List[int], arr2: List[int]) -> int:
        """
        方法2: 动态规划解法
        严格位置依赖的动态规划
        
        时间复杂度: O(n² log m)
        空间复杂度: O(n)
        """
        if not arr1:
            return 0
            
        # 对arr2进行排序和去重
        arr2_sorted = sorted(set(arr2))
        m = len(arr2_sorted)
        n = len(arr1)
        
        # DP数组，dp[i]表示从位置i开始的最小操作数
        dp = [math.inf] * (n + 1)
        dp[n] = 0  # 数组末尾不需要操作
        
        # 从后往前计算
        for i in range(n - 1, -1, -1):
            result = math.inf
            prev = -10**9 if i == 0 else arr1[i - 1]  # 使用足够小的数代替-math.inf
            
            # 在arr2中找到第一个大于prev的位置
            pos = bisect.bisect_right(arr2_sorted, prev)
            
            # 枚举所有可能的替换策略
            for j in range(i, n + 1):
                ops = j - i  # 操作次数
                current_prev = prev
                
                if j == n:
                    result = min(result, ops)
                else:
                    # 检查是否可以不替换当前元素
                    if current_prev < arr1[j]:
                        if dp[j + 1] != math.inf:
                            result = min(result, ops + dp[j + 1])
                    
                    # 尝试替换当前元素
                    if pos < m:
                        current_prev = arr2_sorted[pos]
                        pos += 1
                    else:
                        break
                        
            dp[i] = result
            
        return -1 if dp[0] == math.inf else int(dp[0])
        
    @staticmethod
    def min_operations(nums: List[int]) -> int:
        """
        类似题目1: 最少操作使数组递增（LeetCode 1827）
        贪心算法解法
        
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        if len(nums) <= 1:
            return 0
            
        operations = 0
        
        for i in range(1, len(nums)):
            if nums[i] <= nums[i - 1]:
                operations += nums[i - 1] + 1 - nums[i]
                nums[i] = nums[i - 1] + 1
                
        return operations
        
    @staticmethod
    def length_of_lis(nums: List[int]) -> int:
        """
        类似题目2: 最长递增子序列（LeetCode 300）
        贪心+二分查找解法
        
        时间复杂度: O(n log n)
        空间复杂度: O(n)
        """
        if not nums:
            return 0
            
        tails = []
        
        for num in nums:
            # 使用二分查找找到插入位置
            idx = bisect.bisect_left(tails, num)
            
            if idx == len(tails):
                tails.append(num)
            else:
                tails[idx] = num
                
        return len(tails)
        
    @staticmethod
    def max_envelopes(envelopes: List[List[int]]) -> int:
        """
        类似题目3: 俄罗斯套娃信封问题（LeetCode 354）
        二维最长递增子序列问题
        
        时间复杂度: O(n log n)
        空间复杂度: O(n)
        """
        if not envelopes:
            return 0
            
        # 按照宽度升序排列，如果宽度相同则按照高度降序排列
        envelopes.sort(key=lambda x: (x[0], -x[1]))
        
        # 对高度数组求最长递增子序列
        heights = [env[1] for env in envelopes]
        
        return Code04_MakeArrayStrictlyIncreasing.length_of_lis(heights)
        
    @staticmethod
    def test() -> None:
        """单元测试函数"""
        print("=== 测试使数组严格递增算法 ===")
        
        # 测试用例1
        arr1 = [1, 5, 3, 6, 7]
        arr2 = [1, 3, 2, 4]
        result1 = Code04_MakeArrayStrictlyIncreasing.make_array_increasing(arr1, arr2)
        print(f"测试用例1结果: {result1} (期望: 1)")
        
        # 测试用例2
        arr3 = [1, 5, 3, 6, 7]
        arr4 = [4, 3, 1]
        result2 = Code04_MakeArrayStrictlyIncreasing.make_array_increasing_dp(arr3, arr4)
        print(f"测试用例2结果: {result2} (期望: 2)")
        
        # 测试类似题目
        nums = [1, 1, 1]
        result3 = Code04_MakeArrayStrictlyIncreasing.min_operations(nums)
        print(f"最少操作使数组递增结果: {result3} (期望: 3)")
        
        # 测试最长递增子序列
        nums_lis = [10, 9, 2, 5, 3, 7, 101, 18]
        result4 = Code04_MakeArrayStrictlyIncreasing.length_of_lis(nums_lis)
        print(f"最长递增子序列结果: {result4} (期望: 4)")
        
        print("=== 测试完成 ===")
        
    @staticmethod
    def performance_test() -> None:
        """性能测试函数"""
        print("\n=== 性能测试 ===")
        
        # 创建小规模测试数据避免递归深度问题
        n = 100
        arr1 = [i * 2 for i in range(n)]  # 递增序列
        arr2 = [i * 2 + 1 for i in range(n)]  # 备用序列
        
        # 故意制造一些不递增的位置
        arr1[50] = 1
        
        start_time = time.time()
        result = Code04_MakeArrayStrictlyIncreasing.make_array_increasing_dp(arr1, arr2)
        end_time = time.time()
        
        execution_time = (end_time - start_time) * 1000  # 转换为毫秒
        print(f"小规模测试结果: {result}")
        print(f"执行时间: {execution_time:.2f}ms")
        
    @staticmethod
    def main() -> None:
        """主函数"""
        Code04_MakeArrayStrictlyIncreasing.test()
        Code04_MakeArrayStrictlyIncreasing.performance_test()

if __name__ == "__main__":
    Code04_MakeArrayStrictlyIncreasing.main()

"""
调试技巧:
1. 打印中间状态验证DP转移正确性
2. 使用小规模数据手动验证算法
3. 对比不同方法的计算结果确保一致性

面试要点:
1. 理解动态规划的状态定义和转移方程
2. 掌握二分查找的优化技巧
3. 能够分析算法的时间复杂度
4. 了解空间优化的方法

语言特性差异:
1. Python使用bisect模块简化二分查找实现
2. Python的math.inf表示无穷大
3. Python的列表推导式可以简化代码编写
4. Python的动态类型需要更多类型注解
"""
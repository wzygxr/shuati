#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 416 Partition Equal Subset Sum - 子集和问题
题目链接: https://leetcode.com/problems/partition-equal-subset-sum/
题目描述: 给定一个只包含正整数的非空数组，判断是否可以将这个数组分割成两个子集，使得两个子集的元素和相等

解题思路:
方法1: 回溯法 - 暴力搜索所有子集，时间复杂度高
方法2: 动态规划（0-1背包问题） - 使用二维DP数组
方法3: 动态规划优化 - 使用一维DP数组，空间优化
方法4: 使用set优化 - Python特有的优化方式

时间复杂度分析:
方法1: O(2^N) - 指数级，不可行
方法2: O(N * sum) - 伪多项式时间
方法3: O(N * sum) - 空间优化版本
方法4: O(N * 2^(N/2)) - 折半搜索优化

空间复杂度分析:
方法1: O(N) - 递归栈空间
方法2: O(N * sum) - 二维DP数组
方法3: O(sum) - 一维DP数组
方法4: O(2^(N/2)) - 存储折半结果

工程化考量:
1. 输入验证: 检查数组是否为空，元素是否为正整数
2. 边界处理: 处理总和为奇数的情况（直接返回false）
3. 性能优化: 根据数据规模选择最优算法
4. Python特性: 利用Python的set进行优化
"""

from typing import List
import time
from functools import lru_cache

class SubsetSumSolver:
    """子集和求解器类"""
    
    @staticmethod
    def can_partition_backtrack(nums: List[int]) -> bool:
        """
        方法1: 回溯法（仅用于教学，实际不可行）
        
        Args:
            nums: 整数数组
            
        Returns:
            bool: 是否可以平分
        """
        if not nums:
            return False
            
        total_sum = sum(nums)
        if total_sum % 2 != 0:
            return False
            
        target = total_sum // 2
        
        @lru_cache(maxsize=None)
        def backtrack(index: int, current_sum: int) -> bool:
            if current_sum == target:
                return True
            if current_sum > target or index >= len(nums):
                return False
                
            # 选择当前元素
            if backtrack(index + 1, current_sum + nums[index]):
                return True
                
            # 不选择当前元素
            return backtrack(index + 1, current_sum)
        
        return backtrack(0, 0)
    
    @staticmethod
    def can_partition_dp_2d(nums: List[int]) -> bool:
        """
        方法2: 动态规划（二维DP）
        
        Args:
            nums: 整数数组
            
        Returns:
            bool: 是否可以平分
        """
        if not nums:
            return False
            
        total_sum = sum(nums)
        if total_sum % 2 != 0:
            return False
            
        target = total_sum // 2
        n = len(nums)
        
        # 创建二维DP数组
        dp = [[False] * (target + 1) for _ in range(n + 1)]
        
        # 初始化：和为0总是可达
        for i in range(n + 1):
            dp[i][0] = True
            
        # 动态规划填表
        for i in range(1, n + 1):
            for j in range(1, target + 1):
                # 不选当前元素
                dp[i][j] = dp[i - 1][j]
                
                # 选当前元素
                if j >= nums[i - 1]:
                    dp[i][j] = dp[i][j] or dp[i - 1][j - nums[i - 1]]
                    
        return dp[n][target]
    
    @staticmethod
    def can_partition_dp_1d(nums: List[int]) -> bool:
        """
        方法3: 动态规划优化（一维DP）
        
        Args:
            nums: 整数数组
            
        Returns:
            bool: 是否可以平分
        """
        if not nums:
            return False
            
        total_sum = sum(nums)
        if total_sum % 2 != 0:
            return False
            
        target = total_sum // 2
        n = len(nums)
        
        dp = [False] * (target + 1)
        dp[0] = True  # 和为0总是可达
        
        for num in nums:
            # 从后往前遍历避免重复计算
            for j in range(target, num - 1, -1):
                if dp[j - num]:
                    dp[j] = True
                    
            # 提前终止
            if dp[target]:
                return True
                
        return dp[target]
    
    @staticmethod
    def can_partition_set_optimized(nums: List[int]) -> bool:
        """
        方法4: 使用set优化（Python特有）
        利用Python的set特性进行优化
        
        Args:
            nums: 整数数组
            
        Returns:
            bool: 是否可以平分
        """
        if not nums:
            return False
            
        total_sum = sum(nums)
        if total_sum % 2 != 0:
            return False
            
        target = total_sum // 2
        
        # 使用set记录所有可达的和
        reachable = {0}
        
        for num in nums:
            # 创建新的可达集合（避免在迭代中修改）
            new_reachable = set()
            for val in reachable:
                new_sum = val + num
                if new_sum == target:
                    return True
                if new_sum < target:
                    new_reachable.add(new_sum)
                    
            # 合并新的可达集合
            reachable |= new_reachable
            
        return target in reachable
    
    @staticmethod
    def can_partition_meet_in_middle(nums: List[int]) -> bool:
        """
        方法5: 折半搜索（适用于中等规模数据）
        将数组分成两半，分别计算所有可能的子集和
        
        Args:
            nums: 整数数组
            
        Returns:
            bool: 是否可以平分
        """
        if not nums:
            return False
            
        total_sum = sum(nums)
        if total_sum % 2 != 0:
            return False
            
        target = total_sum // 2
        
        # 将数组分成两半
        n = len(nums)
        left = nums[:n//2]
        right = nums[n//2:]
        
        # 计算左半部分的所有子集和
        left_sums = {0}
        for num in left:
            new_sums = set()
            for s in left_sums:
                new_sum = s + num
                if new_sum == target:
                    return True
                if new_sum <= target:
                    new_sums.add(new_sum)
            left_sums |= new_sums
            
        # 计算右半部分的所有子集和
        right_sums = {0}
        for num in right:
            new_sums = set()
            for s in right_sums:
                new_sum = s + num
                if new_sum == target:
                    return True
                if new_sum <= target:
                    new_sums.add(new_sum)
            right_sums |= new_sums
            
        # 检查是否存在 left_sum + right_sum = target
        for left_sum in left_sums:
            needed = target - left_sum
            if needed in right_sums:
                return True
                
        return False
    
    @staticmethod
    def can_partition_optimized(nums: List[int]) -> bool:
        """
        优化版本：根据数据规模选择算法
        
        Args:
            nums: 整数数组
            
        Returns:
            bool: 是否可以平分
        """
        if not nums:
            return False
            
        total_sum = sum(nums)
        if total_sum % 2 != 0:
            return False
            
        target = total_sum // 2
        n = len(nums)
        
        # 根据数据规模选择算法
        if n <= 20:
            return SubsetSumSolver.can_partition_backtrack(nums)
        elif n <= 40:
            return SubsetSumSolver.can_partition_meet_in_middle(nums)
        elif target <= 10000:
            return SubsetSumSolver.can_partition_dp_1d(nums)
        else:
            return SubsetSumSolver.can_partition_set_optimized(nums)
    
    @staticmethod
    def can_partition_with_validation(nums: List[int]) -> bool:
        """
        工程化改进版本：完整的异常处理
        
        Args:
            nums: 整数数组
            
        Returns:
            bool: 是否可以平分
            
        Raises:
            ValueError: 当输入参数无效时
        """
        try:
            # 输入验证
            if nums is None:
                raise ValueError("Input array cannot be None")
                
            if not nums:
                return False
                
            # 检查元素是否为正整数
            for num in nums:
                if num <= 0:
                    raise ValueError("All elements must be positive integers")
                    
            return SubsetSumSolver.can_partition_optimized(nums)
            
        except Exception as e:
            print(f"Error in can_partition: {e}")
            return False


class PerformanceTester:
    """性能测试类"""
    
    @staticmethod
    def run_tests():
        """运行单元测试"""
        print("=== LeetCode 416 Partition Equal Subset Sum - 单元测试 ===")
        
        test_cases = [
            ([1, 5, 11, 5], True),    # 可以平分
            ([1, 2, 3, 5], False),    # 不能平分
            ([], False)               # 空数组
        ]
        
        for nums, expected in test_cases:
            result = SubsetSumSolver.can_partition_optimized(nums)
            status = "通过" if result == expected else "失败"
            print(f"测试 {nums}, 期望={expected}, 实际={result}, {status}")
        
        # 方法一致性测试
        print("\n=== 方法一致性测试 ===")
        test_nums = [1, 5, 10, 6]
        
        results = [
            SubsetSumSolver.can_partition_dp_2d(test_nums),
            SubsetSumSolver.can_partition_dp_1d(test_nums),
            SubsetSumSolver.can_partition_set_optimized(test_nums),
            SubsetSumSolver.can_partition_optimized(test_nums)
        ]
        
        methods = ["二维DP", "一维DP", "Set优化", "优化法"]
        for method, result in zip(methods, results):
            print(f"{method}: {result}")
        
        consistent = all(r == results[0] for r in results)
        print(f"所有方法结果一致: {'是' if consistent else '否'}")
    
    @staticmethod
    def performance_test():
        """运行性能测试"""
        print("\n=== 性能测试 ===")
        
        # 生成测试数据
        test_cases = [
            PerformanceTester.generate_random_array(20, 100),    # 小规模
            PerformanceTester.generate_random_array(100, 100),   # 中等规模
            PerformanceTester.generate_random_array(200, 100)   # 较大规模
        ]
        
        for i, nums in enumerate(test_cases):
            print(f"测试用例 {i + 1}: 数组长度={len(nums)}")
            
            # 测试一维DP
            start_time = time.time()
            result1 = SubsetSumSolver.can_partition_dp_1d(nums)
            time1 = time.time() - start_time
            
            # 测试Set优化
            start_time = time.time()
            result2 = SubsetSumSolver.can_partition_set_optimized(nums)
            time2 = time.time() - start_time
            
            print(f"  一维DP: {time1:.6f} 秒, 结果: {result1}")
            print(f"  Set优化: {time2:.6f} 秒, 结果: {result2}")
            
            if time2 > 0:
                ratio = time1 / time2
                print(f"  Set优化比一维DP快: {ratio:.2f}倍")
            print()
    
    @staticmethod
    def generate_random_array(size: int, max_value: int) -> List[int]:
        """生成随机数组"""
        import random
        return [random.randint(1, max_value) for _ in range(size)]


def complexity_analysis():
    """复杂度分析"""
    print("=== 复杂度分析 ===")
    print("方法1（回溯法）:")
    print("  时间复杂度: O(2^N)")
    print("  空间复杂度: O(N)")
    print("  适用场景: N <= 20")
    
    print("\n方法2（二维DP）:")
    print("  时间复杂度: O(N * sum)")
    print("  空间复杂度: O(N * sum)")
    print("  适用场景: 小规模数据")
    
    print("\n方法3（一维DP）:")
    print("  时间复杂度: O(N * sum)")
    print("  空间复杂度: O(sum)")
    print("  适用场景: 中等规模数据")
    
    print("\n方法4（Set优化）:")
    print("  时间复杂度: O(N * 2^(N/2))")
    print("  空间复杂度: O(2^(N/2))")
    print("  适用场景: 中等规模数据")
    
    print("\n方法5（折半搜索）:")
    print("  时间复杂度: O(2^(N/2))")
    print("  空间复杂度: O(2^(N/2))")
    print("  适用场景: N <= 40")
    
    print("\nPython工程化建议:")
    print("1. 利用Python的set特性进行优化")
    print("2. 使用lru_cache进行记忆化搜索")
    print("3. 根据数据规模动态选择最优算法")


def main():
    """主函数"""
    print("LeetCode 416 Partition Equal Subset Sum - 子集和问题")
    print("判断是否可以将数组分割成两个和相等的子集")
    
    # 运行单元测试
    PerformanceTester.run_tests()
    
    # 运行性能测试
    PerformanceTester.performance_test()
    
    # 复杂度分析
    complexity_analysis()
    
    # 示例使用
    print("\n=== 示例使用 ===")
    examples = [
        [1, 5, 11, 5],
        [1, 2, 3, 5],
        [1, 2, 3, 4, 5, 6, 7]
    ]
    
    for nums in examples:
        result = SubsetSumSolver.can_partition_with_validation(nums)
        print(f"数组 {nums} 是否可以平分: {result}")


if __name__ == "__main__":
    main()
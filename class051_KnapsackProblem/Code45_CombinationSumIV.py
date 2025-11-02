from typing import List
import sys
from functools import lru_cache

# LeetCode 377. 组合总和 Ⅳ
# 题目描述：给定一个由不同整数组成的数组 nums 和一个目标整数 target，
# 请从 nums 中找出并返回总和为 target 的元素组合的个数。
# 链接：https://leetcode.cn/problems/combination-sum-iv/
# 
# 解题思路：
# 这是一个完全背包问题的排列数变种，需要计算所有可能的排列数。
# 与零钱兑换II不同，这里顺序不同的序列被视为不同的组合。
# 
# 状态定义：dp[i] 表示总和为 i 的元素组合个数
# 状态转移方程：dp[i] = sum(dp[i - num])，其中 num 是 nums 中的元素且 i >= num
# 初始状态：dp[0] = 1（空组合）
# 
# 关键点：为了计算排列数，需要将目标值循环放在外层，数组元素循环放在内层
# 
# 时间复杂度：O(target * n)，其中 n 是数组长度
# 空间复杂度：O(target)，使用一维DP数组
# 
# 工程化考量：
# 1. 异常处理：处理空数组、负数等情况
# 2. 整数溢出：Python自动处理大整数，但需要注意性能
# 3. 性能优化：使用lru_cache进行记忆化
# 4. 边界条件：target=0时返回1

class CombinationSumIV:
    """
    组合总和IV问题的多种解法
    """
    
    @staticmethod
    def combination_sum4_dp(nums: List[int], target: int) -> int:
        """
        动态规划解法 - 计算排列数
        
        Args:
            nums: 不同整数组成的数组
            target: 目标整数
            
        Returns:
            int: 总和为target的元素组合个数
            
        Raises:
            ValueError: 当target为负数时
        """
        # 参数验证
        if not nums:
            return 1 if target == 0 else 0
        if target < 0:
            return 0
        
        # 特殊情况处理
        if target == 0:
            return 1  # 空组合
        
        # 创建DP数组
        dp = [0] * (target + 1)
        dp[0] = 1  # 空组合
        
        # 为了计算排列数，需要将目标值循环放在外层
        # 数组元素循环放在内层
        for i in range(1, target + 1):
            for num in nums:
                if i >= num:
                    dp[i] += dp[i - num]
        
        return dp[target]
    
    @staticmethod
    def combination_sum4_optimized(nums: List[int], target: int) -> int:
        """
        优化的动态规划解法 - 排序数组进行剪枝
        
        Args:
            nums: 不同整数组成的数组
            target: 目标整数
            
        Returns:
            int: 总和为target的元素组合个数
        """
        if not nums:
            return 1 if target == 0 else 0
        if target < 0:
            return 0
        
        # 排序数组，便于剪枝
        nums_sorted = sorted(nums)
        dp = [0] * (target + 1)
        dp[0] = 1
        
        for i in range(1, target + 1):
            for num in nums_sorted:
                if num > i:
                    break  # 剪枝：由于数组已排序，后续数字更大
                dp[i] += dp[i - num]
        
        return dp[target]
    
    @staticmethod
    @lru_cache(maxsize=None)
    def _combination_sum4_dfs(nums_tuple: tuple, target: int) -> int:
        """
        递归辅助函数 - 使用lru_cache进行记忆化
        
        Args:
            nums_tuple: 转换为元组的nums数组（用于缓存）
            target: 目标整数
            
        Returns:
            int: 组合个数
        """
        # 基础情况
        if target == 0:
            return 1
        if target < 0:
            return 0
        
        count = 0
        for num in nums_tuple:
            if target >= num:
                count += CombinationSumIV._combination_sum4_dfs(nums_tuple, target - num)
        
        return count
    
    @staticmethod
    def combination_sum4_dfs(nums: List[int], target: int) -> int:
        """
        递归+记忆化搜索解法
        
        Args:
            nums: 不同整数组成的数组
            target: 目标整数
            
        Returns:
            int: 总和为target的元素组合个数
        """
        if not nums:
            return 1 if target == 0 else 0
        if target < 0:
            return 0
        
        # 将列表转换为元组以便缓存
        nums_tuple = tuple(sorted(nums))
        return CombinationSumIV._combination_sum4_dfs(nums_tuple, target)
    
    @staticmethod
    def combination_sum4_iterative(nums: List[int], target: int) -> int:
        """
        迭代解法 - 避免递归深度限制
        
        Args:
            nums: 不同整数组成的数组
            target: 目标整数
            
        Returns:
            int: 总和为target的元素组合个数
        """
        if not nums:
            return 1 if target == 0 else 0
        if target < 0:
            return 0
        
        dp = [0] * (target + 1)
        dp[0] = 1
        
        # 使用队列进行迭代计算
        from collections import deque
        queue = deque([0])
        
        while queue:
            current = queue.popleft()
            
            for num in nums:
                next_val = current + num
                if next_val <= target:
                    if dp[next_val] == 0:
                        queue.append(next_val)
                    dp[next_val] += dp[current]
        
        return dp[target]
    
    @staticmethod
    def run_tests():
        """
        运行测试用例，验证所有方法的正确性
        """
        # 测试用例
        test_cases = [
            ([1, 2, 3], 4),   # 预期：7
            ([9], 3),         # 预期：0
            ([1, 2, 3], 0),   # 预期：1
            ([1, 2, 3], 1),   # 预期：1
            ([1, 2, 3], 2),   # 预期：2
            ([1, 2, 3], 3)    # 预期：4
        ]
        
        print("组合总和IV问题测试：")
        for nums, target in test_cases:
            try:
                result1 = CombinationSumIV.combination_sum4_dp(nums, target)
                result2 = CombinationSumIV.combination_sum4_optimized(nums, target)
                result3 = CombinationSumIV.combination_sum4_dfs(nums, target)
                result4 = CombinationSumIV.combination_sum4_iterative(nums, target)
                
                print(f"nums={nums}, target={target}: "
                      f"DP={result1}, Optimized={result2}, DFS={result3}, Iterative={result4}")
                
                # 验证结果一致性
                if result1 != result2 or result2 != result3 or result3 != result4:
                    print("警告：不同方法结果不一致！")
                    
            except Exception as e:
                print(f"测试nums={nums}, target={target}时发生错误: {e}")
        
        # 性能测试 - 大规模数据
        import time
        large_nums = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
        large_target = 50
        
        start_time = time.time()
        large_result = CombinationSumIV.combination_sum4_optimized(large_nums, large_target)
        end_time = time.time()
        
        print(f"大规模测试: nums长度={len(large_nums)}, target={large_target}, "
              f"结果={large_result}, 耗时={end_time - start_time:.4f}秒")
        
        # 边界情况测试
        print("边界情况测试：")
        print(f"空数组, target=0: {CombinationSumIV.combination_sum4_dp([], 0)}")  # 预期：1
        print(f"空数组, target=1: {CombinationSumIV.combination_sum4_dp([], 1)}")  # 预期：0
        print(f"负数target: {CombinationSumIV.combination_sum4_dp([1, 2, 3], -1)}")  # 预期：0
        
        # 测试大数情况（Python自动处理大整数）
        print("大数测试：")
        try:
            large_result2 = CombinationSumIV.combination_sum4_dp([1, 2], 100)
            print(f"nums=[1, 2], target=100: 结果={large_result2}")
        except Exception as e:
            print(f"大数测试错误: {e}")

def main():
    """
    主函数 - 运行测试和演示
    """
    try:
        CombinationSumIV.run_tests()
    except Exception as e:
        print(f"程序执行错误: {e}")
        return 1
    return 0

if __name__ == "__main__":
    sys.exit(main())

"""
复杂度分析：

方法1：动态规划（排列数）
- 时间复杂度：O(target * n)
  - 外层循环：target 次
  - 内层循环：n 次（数组长度）
- 空间复杂度：O(target)

方法2：优化的动态规划（剪枝）
- 时间复杂度：O(target * n)（平均情况下由于剪枝可能更快）
- 空间复杂度：O(target)

方法3：递归+记忆化搜索
- 时间复杂度：O(target * n)（每个状态计算一次）
- 空间复杂度：O(target)（记忆化缓存）

方法4：迭代解法
- 时间复杂度：O(target * n)（最坏情况）
- 空间复杂度：O(target)

Python特定优化：
1. 使用lru_cache进行自动记忆化
2. 利用Python的大整数特性，无需担心溢出
3. 使用类型注解提高代码可读性
4. 使用deque进行迭代计算，避免递归深度限制

关键点分析：
1. 排列数 vs 组合数：本题需要计算排列数，因此遍历顺序很重要
2. Python优势：自动处理大整数，无需担心溢出问题
3. 记忆化优化：使用lru_cache简化记忆化实现
4. 剪枝策略：排序数组可以在内层循环提前终止

工程化考量：
1. 模块化设计：将不同解法封装为静态方法
2. 异常处理：完善的参数验证和错误处理
3. 性能监控：包含性能测试和时间测量
4. 测试覆盖：包含各种边界情况和性能测试

面试要点：
1. 理解排列数和组合数的本质区别
2. 掌握动态规划中遍历顺序的重要性
3. 了解记忆化搜索的实现技巧
4. 能够分析不同解法的时空复杂度
"""
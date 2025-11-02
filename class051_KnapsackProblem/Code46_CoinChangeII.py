from typing import List
import sys
from functools import lru_cache

# LeetCode 518. 零钱兑换 II
# 题目描述：给你一个整数数组 coins 表示不同面额的硬币，另给一个整数 amount 表示总金额。
# 请你计算并返回可以凑成总金额的硬币组合数。如果任何硬币组合都无法凑出总金额，返回 0。
# 链接：https://leetcode.cn/problems/coin-change-ii/
# 
# 解题思路：
# 这是一个完全背包问题的组合数变种，需要计算所有可能的组合数。
# 与组合总和IV不同，这里顺序不同的序列被视为相同的组合。
# 
# 状态定义：dp[i] 表示凑成总金额 i 的硬币组合数
# 状态转移方程：dp[i] += dp[i - coin]，其中 coin 是 coins 中的硬币且 i >= coin
# 初始状态：dp[0] = 1（空组合）
# 
# 关键点：为了计算组合数，需要将硬币循环放在外层，金额循环放在内层
# 
# 时间复杂度：O(amount * n)，其中 n 是硬币种类数
# 空间复杂度：O(amount)，使用一维DP数组
# 
# 工程化考量：
# 1. 异常处理：处理空数组、负数等情况
# 2. 边界条件：amount=0时返回1
# 3. 性能优化：利用Python特性进行优化
# 4. 可读性：清晰的变量命名和类型注解

class CoinChangeII:
    """
    零钱兑换II问题的多种解法
    """
    
    @staticmethod
    def change_dp(amount: int, coins: List[int]) -> int:
        """
        动态规划解法 - 计算组合数
        
        Args:
            amount: 目标总金额
            coins: 不同面额的硬币数组
            
        Returns:
            int: 凑成总金额的硬币组合数
        """
        # 参数验证
        if not coins:
            return 1 if amount == 0 else 0
        if amount < 0:
            return 0
        
        # 特殊情况处理
        if amount == 0:
            return 1  # 空组合
        
        # 创建DP数组
        dp = [0] * (amount + 1)
        dp[0] = 1  # 空组合
        
        # 为了计算组合数，需要将硬币循环放在外层
        # 金额循环放在内层
        for coin in coins:
            for i in range(coin, amount + 1):
                dp[i] += dp[i - coin]
        
        return dp[amount]
    
    @staticmethod
    def change_optimized(amount: int, coins: List[int]) -> int:
        """
        优化的动态规划解法 - 排序硬币
        
        Args:
            amount: 目标总金额
            coins: 不同面额的硬币数组
            
        Returns:
            int: 凑成总金额的硬币组合数
        """
        if not coins:
            return 1 if amount == 0 else 0
        if amount < 0:
            return 0
        
        # 排序硬币，便于理解和调试（对组合数结果无影响）
        coins_sorted = sorted(coins)
        dp = [0] * (amount + 1)
        dp[0] = 1
        
        for coin in coins_sorted:
            for i in range(coin, amount + 1):
                dp[i] += dp[i - coin]
        
        return dp[amount]
    
    @staticmethod
    def change_with_pruning(amount: int, coins: List[int]) -> int:
        """
        带剪枝的动态规划解法
        
        Args:
            amount: 目标总金额
            coins: 不同面额的硬币数组
            
        Returns:
            int: 凑成总金额的硬币组合数
        """
        if not coins:
            return 1 if amount == 0 else 0
        if amount < 0:
            return 0
        
        # 排序硬币，便于剪枝
        coins_sorted = sorted(coins)
        dp = [0] * (amount + 1)
        dp[0] = 1
        
        for coin in coins_sorted:
            # 如果硬币面额已经大于amount，后续硬币更大，直接跳过
            if coin > amount:
                continue
            for i in range(coin, amount + 1):
                dp[i] += dp[i - coin]
        
        return dp[amount]
    
    @staticmethod
    @lru_cache(maxsize=None)
    def _change_dfs(coins_tuple: tuple, index: int, amount: int) -> int:
        """
        递归辅助函数 - 使用lru_cache进行记忆化
        
        Args:
            coins_tuple: 转换为元组的硬币数组
            index: 当前考虑的硬币索引
            amount: 剩余金额
            
        Returns:
            int: 组合数
        """
        # 基础情况
        if amount == 0:
            return 1
        if amount < 0 or index >= len(coins_tuple):
            return 0
        
        count = 0
        coin = coins_tuple[index]
        
        # 选择当前硬币0次或多次
        k = 0
        while k * coin <= amount:
            count += CoinChangeII._change_dfs(coins_tuple, index + 1, amount - k * coin)
            k += 1
        
        return count
    
    @staticmethod
    def change_dfs(amount: int, coins: List[int]) -> int:
        """
        递归+记忆化搜索解法
        
        Args:
            amount: 目标总金额
            coins: 不同面额的硬币数组
            
        Returns:
            int: 凑成总金额的硬币组合数
        """
        if not coins:
            return 1 if amount == 0 else 0
        if amount < 0:
            return 0
        
        # 将列表转换为元组以便缓存
        coins_tuple = tuple(sorted(coins))
        return CoinChangeII._change_dfs(coins_tuple, 0, amount)
    
    @staticmethod
    @lru_cache(maxsize=None)
    def _change_dfs_optimized(coins_tuple: tuple, amount: int) -> int:
        """
        空间优化的递归辅助函数
        
        Args:
            coins_tuple: 转换为元组的硬币数组
            amount: 剩余金额
            
        Returns:
            int: 组合数
        """
        if amount == 0:
            return 1
        if amount < 0:
            return 0
        
        count = 0
        for i, coin in enumerate(coins_tuple):
            if amount >= coin:
                # 考虑当前硬币（可以重复使用）
                count += CoinChangeII._change_dfs_optimized(coins_tuple[i:], amount - coin)
        
        return count
    
    @staticmethod
    def change_dfs_optimized(amount: int, coins: List[int]) -> int:
        """
        空间优化的递归解法
        
        Args:
            amount: 目标总金额
            coins: 不同面额的硬币数组
            
        Returns:
            int: 凑成总金额的硬币组合数
        """
        if not coins:
            return 1 if amount == 0 else 0
        if amount < 0:
            return 0
        
        # 将列表转换为元组以便缓存
        coins_tuple = tuple(sorted(coins))
        return CoinChangeII._change_dfs_optimized(coins_tuple, amount)
    
    @staticmethod
    def run_tests():
        """
        运行测试用例，验证所有方法的正确性
        """
        # 测试用例
        test_cases = [
            (5, [1, 2, 5]),    # 预期：4
            (3, [2]),           # 预期：0
            (10, [10]),         # 预期：1
            (0, [1, 2, 3]),     # 预期：1
            (5, [1, 2, 3]),     # 预期：5
            (100, [1, 2, 5])    # 大规模测试
        ]
        
        print("零钱兑换II问题测试：")
        for amount, coins in test_cases:
            try:
                result1 = CoinChangeII.change_dp(amount, coins)
                result2 = CoinChangeII.change_optimized(amount, coins)
                result3 = CoinChangeII.change_with_pruning(amount, coins)
                result4 = CoinChangeII.change_dfs(amount, coins)
                result5 = CoinChangeII.change_dfs_optimized(amount, coins)
                
                print(f"amount={amount}, coins={coins}: "
                      f"DP={result1}, Optimized={result2}, Pruning={result3}, "
                      f"DFS={result4}, DFS_Opt={result5}")
                
                # 验证结果一致性
                if result1 != result2 or result2 != result3 or result3 != result4 or result4 != result5:
                    print("警告：不同方法结果不一致！")
                    
            except Exception as e:
                print(f"测试amount={amount}, coins={coins}时发生错误: {e}")
        
        # 性能测试 - 大规模数据
        import time
        large_coins = [1, 2, 5, 10, 20, 50, 100]
        large_amount = 1000
        
        start_time = time.time()
        large_result = CoinChangeII.change_with_pruning(large_amount, large_coins)
        end_time = time.time()
        
        print(f"大规模测试: coins长度={len(large_coins)}, amount={large_amount}, "
              f"结果={large_result}, 耗时={end_time - start_time:.4f}秒")
        
        # 边界情况测试
        print("边界情况测试：")
        print(f"空数组, amount=0: {CoinChangeII.change_dp(0, [])}")  # 预期：1
        print(f"空数组, amount=1: {CoinChangeII.change_dp(1, [])}")  # 预期：0
        print(f"负数amount: {CoinChangeII.change_dp(-1, [1, 2, 3])}")  # 预期：0
        
        # 对比组合总和IV，验证遍历顺序的重要性
        print("组合数 vs 排列数对比：")
        coins = [1, 2, 5]
        amt = 5
        print(f"零钱兑换II（组合数）: amount={amt}, coins={coins}, 结果={CoinChangeII.change_dp(amt, coins)}")
        
        # 模拟组合总和IV的排列数计算（错误用法）
        dp = [0] * (amt + 1)
        dp[0] = 1
        for i in range(1, amt + 1):
            for coin in coins:
                if i >= coin:
                    dp[i] += dp[i - coin]
        print(f"错误用法（排列数）: amount={amt}, coins={coins}, 结果={dp[amt]}")

def main():
    """
    主函数 - 运行测试和演示
    """
    try:
        CoinChangeII.run_tests()
    except Exception as e:
        print(f"程序执行错误: {e}")
        return 1
    return 0

if __name__ == "__main__":
    sys.exit(main())

"""
复杂度分析：

方法1：动态规划（组合数）
- 时间复杂度：O(amount * n)
  - 外层循环：n 次（硬币种类数）
  - 内层循环：amount 次（金额范围）
- 空间复杂度：O(amount)

方法2：优化的动态规划
- 时间复杂度：O(amount * n)（与方法1相同）
- 空间复杂度：O(amount)

方法3：带剪枝的动态规划
- 时间复杂度：O(amount * n)（平均情况下可能更快）
- 空间复杂度：O(amount)

方法4：递归+记忆化搜索
- 时间复杂度：O(amount * n)（每个状态计算一次）
- 空间复杂度：O(amount * n)（记忆化缓存）

方法5：空间优化的递归
- 时间复杂度：O(amount * n)
- 空间复杂度：O(amount)（一维记忆化缓存）

Python特定优化：
1. 使用lru_cache进行自动记忆化
2. 利用Python的大整数特性，无需担心溢出
3. 使用类型注解提高代码可读性
4. 利用元组进行缓存，避免列表的可变性问题

关键点分析：
1. 组合数 vs 排列数：本题需要计算组合数，因此遍历顺序很重要
2. 外层循环硬币：确保计算的是组合数（顺序无关）
3. 内层循环金额：完全背包的正序遍历
4. Python优势：自动处理大整数，简化实现

工程化考量：
1. 模块化设计：将不同解法封装为静态方法
2. 异常处理：完善的参数验证和错误处理
3. 性能监控：包含性能测试和时间测量
4. 测试覆盖：包含各种边界情况和对比测试

面试要点：
1. 理解组合数和排列数的本质区别
2. 掌握动态规划中遍历顺序的重要性
3. 了解记忆化搜索的实现技巧
4. 能够分析不同解法的时空复杂度
5. 了解Python特性在算法实现中的应用
"""
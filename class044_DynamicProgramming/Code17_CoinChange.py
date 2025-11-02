# 零钱兑换 (Coin Change)
# 题目链接: https://leetcode.cn/problems/coin-change/
# 难度: 中等
# 这是一个经典的完全背包问题变种
import sys
from typing import List

class Solution:
    # 方法1: 暴力递归（超时解法，仅作为思路展示）
    # 时间复杂度: O(S^n) - S是硬币面额数量，n是金额大小
    # 空间复杂度: O(n) - 递归调用栈深度
    def coinChange1(self, coins: List[int], amount: int) -> int:
        # 特殊情况处理
        if amount < 0:
            return -1
        if amount == 0:
            return 0
        # 调用递归函数
        min_coins = self._process1(coins, amount)
        return min_coins if min_coins != sys.maxsize else -1
    
    # 递归函数: 计算凑成金额amount所需的最少硬币数
    def _process1(self, coins: List[int], amount: int) -> int:
        # 基本情况: 已经凑够了金额
        if amount == 0:
            return 0
        # 基本情况: 金额为负数，无法凑成
        if amount < 0:
            return sys.maxsize
        
        min_coins = sys.maxsize
        # 尝试每一种硬币
        for coin in coins:
            # 递归计算使用当前硬币后的最少硬币数
            sub_result = self._process1(coins, amount - coin)
            # 如果子问题有解，更新最小值
            if sub_result != sys.maxsize:
                min_coins = min(min_coins, sub_result + 1)
        
        return min_coins
    
    # 方法2: 记忆化搜索
    # 时间复杂度: O(S * n) - S是硬币面额数量，n是金额大小
    # 空间复杂度: O(n) - 备忘录和递归调用栈
    def coinChange2(self, coins: List[int], amount: int) -> int:
        if amount < 0:
            return -1
        if amount == 0:
            return 0
        # 备忘录，存储已经计算过的结果
        memo = [-2] * (amount + 1)  # 使用-2表示未计算过
        min_coins = self._process2(coins, amount, memo)
        return min_coins if min_coins != sys.maxsize else -1
    
    def _process2(self, coins: List[int], amount: int, memo: List[int]) -> int:
        if amount == 0:
            return 0
        if amount < 0:
            return sys.maxsize
        
        # 检查是否已经计算过
        if memo[amount] != -2:
            return memo[amount]
        
        min_coins = sys.maxsize
        for coin in coins:
            sub_result = self._process2(coins, amount - coin, memo)
            if sub_result != sys.maxsize:
                min_coins = min(min_coins, sub_result + 1)
        
        # 记录结果到备忘录
        memo[amount] = min_coins
        return min_coins
    
    # 方法3: 动态规划（自底向上）
    # 时间复杂度: O(S * n) - S是硬币面额数量，n是金额大小
    # 空间复杂度: O(n) - dp数组大小
    def coinChange3(self, coins: List[int], amount: int) -> int:
        # 特殊情况处理
        if amount < 0:
            return -1
        if amount == 0:
            return 0
        
        # dp[i]表示凑成金额i所需的最少硬币数
        dp = [amount + 1] * (amount + 1)
        # 基础情况：凑成金额0需要0个硬币
        dp[0] = 0
        
        # 遍历每个金额从1到amount
        for i in range(1, amount + 1):
            # 遍历每种硬币
            for coin in coins:
                # 如果当前硬币面额不大于当前金额，并且使用当前硬币后可以得到一个更优解
                if coin <= i and dp[i - coin] != amount + 1:
                    dp[i] = min(dp[i], dp[i - coin] + 1)
        
        # 如果dp[amount]仍然是初始值，说明无法凑成
        return dp[amount] if dp[amount] != amount + 1 else -1
    
    # 方法4: 动态规划优化版（减少不必要的计算）
    # 时间复杂度: O(S * n) - 与方法3相同，但常数项可能更小
    # 空间复杂度: O(n) - dp数组大小
    def coinChange4(self, coins: List[int], amount: int) -> int:
        if amount < 0:
            return -1
        if amount == 0:
            return 0
        
        # 优化：先排序硬币，这样在某些情况下可以提前终止循环
        coins.sort()
        
        dp = [amount + 1] * (amount + 1)
        dp[0] = 0
        
        for i in range(1, amount + 1):
            for coin in coins:
                if coin > i:
                    # 由于硬币已排序，后续硬币更大，无需继续检查
                    break
                dp[i] = min(dp[i], dp[i - coin] + 1)
        
        return dp[amount] if dp[amount] != amount + 1 else -1

# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1: 标准测试
    coins1 = [1, 2, 5]
    amount1 = 11
    print("测试用例1结果:")
    print("记忆化搜索: ", solution.coinChange2(coins1, amount1))  # 预期输出: 3 (11 = 5 + 5 + 1)
    print("动态规划: ", solution.coinChange3(coins1, amount1))  # 预期输出: 3
    print("动态规划优化版: ", solution.coinChange4(coins1, amount1))  # 预期输出: 3
    
    # 测试用例2: 无法凑成的情况
    coins2 = [2]
    amount2 = 3
    print("\n测试用例2结果:")
    print("动态规划: ", solution.coinChange3(coins2, amount2))  # 预期输出: -1
    
    # 测试用例3: 边界情况
    coins3 = [1]
    amount3 = 0
    print("\n测试用例3结果:")
    print("动态规划: ", solution.coinChange3(coins3, amount3))  # 预期输出: 0
    
    # 测试用例4: 大金额测试
    coins4 = [1, 3, 4, 5]
    amount4 = 7
    print("\n测试用例4结果:")
    print("动态规划: ", solution.coinChange3(coins4, amount4))  # 预期输出: 2 (7 = 3 + 4)
    
    # 测试用例5: 大面额优先
    coins5 = [186, 419, 83, 408]
    amount5 = 6249
    print("\n测试用例5结果:")
    print("动态规划优化版: ", solution.coinChange4(coins5, amount5))  # 预期输出: 20
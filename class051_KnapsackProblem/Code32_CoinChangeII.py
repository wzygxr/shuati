# LeetCode 518. 零钱兑换 II
# 题目描述：给你一个整数数组 coins 表示不同面额的硬币，另给一个整数 amount 表示总金额。
# 请你计算并返回可以凑成总金额的硬币组合数。如果任何硬币组合都无法凑出总金额，返回 0 。
# 假设每一种面额的硬币有无限个。
# 链接：https://leetcode.cn/problems/coin-change-ii/
# 
# 解题思路：
# 这是一个典型的完全背包问题，但目标是求组合数而不是最少硬币个数：
# - 硬币可以多次使用（完全背包的特点）
# - 目标是计算凑成总金额的不同组合数
# 
# 状态定义：dp[i] 表示凑成金额i的不同组合数
# 状态转移方程：dp[i] += dp[i - coin]，其中coin是每种硬币的面额，且i >= coin
# 初始状态：dp[0] = 1（凑成金额0只有一种方式：不使用任何硬币）
# 
# 时间复杂度：O(amount * n)，其中n是硬币的种类数
# 空间复杂度：O(amount)，使用一维DP数组

from typing import List
from functools import lru_cache


def change(coins: List[int], amount: int) -> int:
    """
    计算凑成总金额的不同硬币组合数
    
    Args:
        coins: 不同面额的硬币数组
        amount: 总金额
    
    Returns:
        凑成总金额的不同组合数
    """
    # 参数验证
    if amount < 0:
        return 0
    if amount == 0:
        return 1
    if not coins:
        return 0
    
    # 创建DP数组，dp[i]表示凑成金额i的不同组合数
    dp = [0] * (amount + 1)
    dp[0] = 1  # 凑成金额0只有一种方式：不使用任何硬币
    
    # 遍历每种硬币
    for coin in coins:
        # 完全背包问题，正序遍历金额（允许重复使用硬币）
        # 注意：这里遍历硬币放在外层，金额放在内层，这样可以避免重复计算不同顺序的组合
        # 例如，对于coins=[1,2]和amount=3，如果先遍历金额再遍历硬币，会计算出[1,2]和[2,1]作为两种不同的组合
        for i in range(coin, amount + 1):
            # 状态转移：当前金额可以由(i-coin)的金额加上一个coin得到
            dp[i] += dp[i - coin]
    
    return dp[amount]


def change2D(coins: List[int], amount: int) -> int:
    """
    二维DP数组实现（更直观但空间复杂度更高）
    dp[i][j]表示使用前i种硬币，凑成金额j的不同组合数
    """
    if amount < 0:
        return 0
    if amount == 0:
        return 1
    if not coins:
        return 0
    
    n = len(coins)
    # 创建二维DP数组
    dp = [[0] * (amount + 1) for _ in range(n + 1)]
    
    # 初始化：使用0种硬币，只能凑成金额0，有一种方式
    dp[0][0] = 1
    
    # 填充DP数组
    for i in range(1, n + 1):
        coin = coins[i - 1]
        
        for j in range(amount + 1):
            # 不使用第i种硬币
            dp[i][j] = dp[i - 1][j]
            
            # 使用第i种硬币（如果可以的话）
            # 完全背包问题：可以重复使用同一种硬币，所以是dp[i][j-coin]而不是dp[i-1][j-coin]
            if j >= coin:
                dp[i][j] += dp[i][j - coin]
    
    return dp[n][amount]


def changeOptimized(coins: List[int], amount: int) -> int:
    """
    优化版本：提前处理特殊情况
    """
    # 快速处理特殊情况
    if amount < 0:
        return 0
    if amount == 0:
        return 1
    if not coins:
        return 0
    
    # 创建DP数组
    dp = [0] * (amount + 1)
    dp[0] = 1
    
    for coin in coins:
        # 如果当前硬币面额大于amount，可以跳过
        if coin > amount:
            continue
        
        for i in range(coin, amount + 1):
            dp[i] += dp[i - coin]
    
    return dp[amount]


def changeDFS(coins: List[int], amount: int) -> int:
    """
    递归+记忆化搜索实现
    注意：由于这个问题的参数范围较大，递归+记忆化搜索可能会超时
    这里仅作为一种实现方式展示
    """
    if amount < 0:
        return 0
    if amount == 0:
        return 1
    if not coins:
        return 0
    
    # 为了避免重复计算不同顺序的组合，我们先对硬币进行排序，然后确保每次选择的硬币不小于上一次选择的硬币
    coins.sort()
    
    n = len(coins)
    
    @lru_cache(maxsize=None)
    def dfs(index: int, remain: int) -> int:
        """
        递归辅助函数
        
        Args:
            index: 当前考虑的硬币索引
            remain: 剩余需要凑成的金额
            
        Returns:
            从当前索引开始，凑成剩余金额的不同组合数
        """
        # 基础情况：如果剩余金额为0，说明找到了一种组合
        if remain == 0:
            return 1
        
        # 基础情况：如果已经考虑完所有硬币，或者当前硬币面额大于剩余金额，无法凑成
        if index >= n or coins[index] > remain:
            return 0
        
        ways = 0
        
        # 计算使用当前硬币的不同次数的情况
        # k表示使用当前硬币的个数，从0开始
        k = 0
        while k * coins[index] <= remain:
            # 递归计算不使用当前硬币（k=0）或使用k次当前硬币后的组合数
            ways += dfs(index + 1, remain - k * coins[index])
            k += 1
        
        return ways
    
    return dfs(0, amount)


def changeDFS2(coins: List[int], amount: int) -> int:
    """
    另一种递归实现方式，更加简洁
    """
    if amount < 0:
        return 0
    if amount == 0:
        return 1
    if not coins:
        return 0
    
    # 排序硬币，避免重复计算
    coins.sort()
    
    n = len(coins)
    
    @lru_cache(maxsize=None)
    def dfs(index: int, remain: int) -> int:
        """
        递归辅助函数 - 更简洁的实现
        
        Args:
            index: 当前考虑的硬币索引
            remain: 剩余需要凑成的金额
            
        Returns:
            从当前索引开始，凑成剩余金额的不同组合数
        """
        if remain == 0:
            return 1
        
        if index >= n or coins[index] > remain:
            return 0
        
        # 不使用当前硬币的情况
        skip = dfs(index + 1, remain)
        
        # 使用当前硬币的情况（可以继续使用当前硬币）
        use = dfs(index, remain - coins[index])
        
        return skip + use
    
    return dfs(0, amount)


# 测试函数
def test_change():
    # 测试用例1
    coins1 = [1, 2, 5]
    amount1 = 5
    print(f"测试用例1结果: {change(coins1, amount1)}")  # 预期输出: 4 ([1,1,1,1,1], [1,1,1,2], [1,2,2], [5])
    
    # 测试用例2
    coins2 = [2]
    amount2 = 3
    print(f"测试用例2结果: {change(coins2, amount2)}")  # 预期输出: 0
    
    # 测试用例3
    coins3 = [10]
    amount3 = 10
    print(f"测试用例3结果: {change(coins3, amount3)}")  # 预期输出: 1
    
    # 测试二维DP实现
    print(f"测试用例1 (二维DP): {change2D(coins1, amount1)}")
    
    # 测试优化版本
    print(f"测试用例1 (优化版本): {changeOptimized(coins1, amount1)}")
    
    # 测试DFS实现
    print(f"测试用例1 (DFS): {changeDFS(coins1, amount1)}")
    
    # 测试DFS2实现
    print(f"测试用例1 (DFS2): {changeDFS2(coins1, amount1)}")


# 执行测试
if __name__ == "__main__":
    test_change()
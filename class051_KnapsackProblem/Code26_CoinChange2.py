# LeetCode 518. 零钱兑换 II
# 题目描述：给你一个整数数组 coins 表示不同面额的硬币，另给一个整数 amount 表示总金额。
# 请你计算并返回可以凑成总金额的硬币组合数。如果任何硬币组合都无法凑出总金额，返回 0 。
# 假设每一种面额的硬币有无限个。
# 链接：https://leetcode.cn/problems/coin-change-ii/
# 
# 解题思路：
# 这是一个典型的完全背包问题，其中：
# - 背包容量：总金额 amount
# - 物品：不同面额的硬币
# - 物品可以无限使用（完全背包）
# - 目标：求恰好装满背包的方案数
# 
# 状态定义：dp[i] 表示凑成总金额 i 的硬币组合数
# 状态转移方程：dp[i] += dp[i - coin]，其中 coin 是当前考虑的硬币面额，且 i >= coin
# 初始状态：dp[0] = 1 表示凑成总金额 0 有一种方式（不使用任何硬币）
# 
# 注意：为了计算组合数而不是排列数，需要先遍历物品（硬币），再遍历容量（金额）
# 时间复杂度：O(amount * n)，其中 n 是硬币种类数
# 空间复杂度：O(amount)，使用一维DP数组

from typing import List, Optional


def change(amount: int, coins: List[int]) -> int:
    """
    计算凑成总金额的硬币组合数
    
    Args:
        amount: 总金额
        coins: 不同面额的硬币数组
    
    Returns:
        int: 可以凑成总金额的硬币组合数
    """
    # 参数验证
    if amount < 0:
        return 0
    
    # 创建一维DP数组，dp[i]表示凑成总金额i的硬币组合数
    dp = [0] * (amount + 1)
    
    # 初始状态：凑成总金额0有一种方式（不使用任何硬币）
    dp[0] = 1
    
    # 先遍历物品（硬币），再遍历容量（金额），这样计算的是组合数
    # 如果先遍历容量再遍历物品，计算的是排列数
    for coin in coins:
        # 对于完全背包问题，容量遍历是正序的，允许物品被重复使用
        for i in range(coin, amount + 1):
            dp[i] += dp[i - coin]
    
    # 返回结果：凑成总金额amount的硬币组合数
    return dp[amount]


def change_optimized(amount: int, coins: List[int]) -> int:
    """
    优化版本：添加硬币排序和剪枝
    """
    # 参数验证
    if amount < 0:
        return 0
    if not coins:
        return 1 if amount == 0 else 0
    
    # 对硬币进行排序，便于后续剪枝
    coins.sort()
    
    # 创建一维DP数组
    dp = [0] * (amount + 1)
    dp[0] = 1
    
    # 先遍历物品（硬币），再遍历容量（金额）
    for coin in coins:
        # 如果当前硬币面额已经大于amount，可以跳过
        if coin > amount:
            break
        # 正序遍历容量，允许重复使用硬币
        for i in range(coin, amount + 1):
            dp[i] += dp[i - coin]
    
    return dp[amount]


def change_2d(amount: int, coins: List[int]) -> int:
    """
    二维DP数组实现
    dp[i][j]表示使用前i种硬币凑成总金额j的组合数
    """
    # 参数验证
    if amount < 0:
        return 0
    if not coins:
        return 1 if amount == 0 else 0
    
    n = len(coins)
    # 创建二维DP数组
    dp = [[0] * (amount + 1) for _ in range(n + 1)]
    
    # 初始化：使用0种硬币只能凑成总金额0
    dp[0][0] = 1
    
    # 填充DP数组
    for i in range(1, n + 1):
        coin = coins[i - 1]
        for j in range(amount + 1):
            # 不使用当前硬币
            dp[i][j] = dp[i - 1][j]
            # 使用当前硬币（如果可以的话）
            if j >= coin:
                dp[i][j] += dp[i][j - coin]
    
    return dp[n][amount]


def change_dfs(amount: int, coins: List[int]) -> int:
    """
    递归+记忆化搜索实现
    """
    # 参数验证
    if amount < 0:
        return 0
    if not coins:
        return 1 if amount == 0 else 0
    
    # 对硬币进行排序，便于剪枝
    coins.sort()
    
    # 使用二维列表作为缓存，memo[i][j]表示使用前i种硬币凑成总金额j的组合数
    memo = [[-1] * (amount + 1) for _ in range(len(coins))]
    
    def dfs(index: int, remaining: int) -> int:
        """递归辅助函数"""
        # 基础情况：如果剩余金额为0，找到了一种组合
        if remaining == 0:
            return 1
        
        # 基础情况：如果已经考虑完所有硬币或者剩余金额小于0，无法凑成
        if index == len(coins) or remaining < 0:
            return 0
        
        # 检查缓存
        if memo[index][remaining] != -1:
            return memo[index][remaining]
        
        # 选择不使用当前硬币
        not_use = dfs(index + 1, remaining)
        
        # 选择使用当前硬币（如果可以的话）
        use = 0
        if remaining >= coins[index]:
            # 注意这里index不变，表示可以重复使用当前硬币
            use = dfs(index, remaining - coins[index])
        
        # 计算总组合数并缓存
        memo[index][remaining] = not_use + use
        return memo[index][remaining]
    
    # 调用递归函数
    return dfs(0, amount)


def change_dfs2(amount: int, coins: List[int]) -> int:
    """
    另一种递归+记忆化实现方式，不考虑硬币的顺序
    使用index来确保每种硬币只按顺序考虑一次，避免重复计算
    """
    # 参数验证
    if amount < 0:
        return 0
    if not coins:
        return 1 if amount == 0 else 0
    
    # 对硬币进行排序，便于剪枝
    coins.sort()
    
    # 使用二维列表作为缓存
    memo = [[-1] * (amount + 1) for _ in range(len(coins))]
    
    def dfs2(index: int, remaining: int) -> int:
        """另一种递归辅助函数实现"""
        # 基础情况
        if remaining == 0:
            return 1
        if index == len(coins) or remaining < coins[index]:
            return 0
        
        # 检查缓存
        if memo[index][remaining] != -1:
            return memo[index][remaining]
        
        count = 0
        # 尝试使用当前硬币0次、1次、2次...直到超过剩余金额
        for k in range(0, remaining // coins[index] + 1):
            # 使用k次当前硬币后，剩余金额为remaining - k * coins[index]，接下来考虑下一种硬币
            count += dfs2(index + 1, remaining - k * coins[index])
        
        # 缓存结果
        memo[index][remaining] = count
        return count
    
    # 调用递归函数
    return dfs2(0, amount)


def change_permutation(amount: int, coins: List[int]) -> int:
    """
    计算排列数的实现（如果题目要求不同顺序算不同的组合）
    注意：这不是本题的要求，但作为对比提供
    """
    # 参数验证
    if amount < 0:
        return 0
    
    # 创建一维DP数组
    dp = [0] * (amount + 1)
    dp[0] = 1
    
    # 先遍历容量（金额），再遍历物品（硬币），这样计算的是排列数
    for i in range(1, amount + 1):
        for coin in coins:
            if i >= coin:
                dp[i] += dp[i - coin]
    
    return dp[amount]


# 测试代码
if __name__ == "__main__":
    # 测试用例1
    coins1 = [1, 2, 5]
    amount1 = 5
    print(f"测试用例1结果: {change(amount1, coins1)}")  # 预期输出: 4
    
    # 测试用例2
    coins2 = [2]
    amount2 = 3
    print(f"测试用例2结果: {change(amount2, coins2)}")  # 预期输出: 0
    
    # 测试用例3
    coins3 = [10]
    amount3 = 10
    print(f"测试用例3结果: {change(amount3, coins3)}")  # 预期输出: 1
    
    # 测试用例4
    coins4 = [1, 2, 5]
    amount4 = 100
    print(f"测试用例4结果: {change(amount4, coins4)}")  # 预期输出: 204
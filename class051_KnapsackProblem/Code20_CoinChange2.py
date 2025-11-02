# LeetCode 518. 零钱兑换 II
# 题目描述：给你一个整数数组 coins 表示不同面额的硬币，另给一个整数 amount 表示总金额。
# 请你计算并返回可以凑成总金额的硬币组合数。如果没有任何一种硬币组合能组成总金额，返回 0 。
# 假设每一种面额的硬币有无限个。
# 链接：https://leetcode.cn/problems/coin-change-ii/
# 
# 解题思路：
# 这是一个典型的完全背包问题的计数变种，因为每种硬币可以使用无限次，且我们需要计算组合数而非最大值。
# 状态定义：dp[i] 表示凑成金额 i 的硬币组合数
# 状态转移方程：dp[i] += dp[i - coins[j]]，其中 j 遍历所有硬币，且 i >= coins[j]
# 初始状态：dp[0] = 1（表示凑成金额0有一种方式，即不选任何硬币）
# 
# 注意事项：
# 1. 为了确保计算的是组合数而非排列数，我们需要先遍历硬币，再遍历金额
# 2. 这样可以保证每个硬币只使用一次（在组合中的顺序无关）
# 
# 时间复杂度：O(amount * n)，其中n是硬币种类数
# 空间复杂度：O(amount)，使用一维DP数组

def change(amount, coins):
    """
    计算凑成总金额的硬币组合数
    
    Args:
        amount: 总金额
        coins: 不同面额的硬币数组
    
    Returns:
        int: 硬币组合数
    """
    # 参数验证
    if amount == 0:
        return 1  # 凑成金额0有一种方式，即不选任何硬币
    if not coins:
        return 0
    
    # 创建DP数组，dp[i]表示凑成金额i的硬币组合数
    dp = [0] * (amount + 1)
    
    # 初始状态：凑成金额0有一种方式
    dp[0] = 1
    
    # 遍历每种硬币
    for coin in coins:
        # 正序遍历金额，因为完全背包允许重复使用物品
        # 先遍历硬币再遍历金额，确保计算的是组合数而非排列数
        for i in range(coin, amount + 1):
            # 状态转移：加上使用当前硬币的组合数
            dp[i] += dp[i - coin]
    
    return dp[amount]

def change_optimized(amount, coins):
    """
    优化版本：添加一些剪枝和提前终止的条件
    
    Args:
        amount: 总金额
        coins: 不同面额的硬币数组
    
    Returns:
        int: 硬币组合数
    """
    # 参数验证
    if amount == 0:
        return 1
    if not coins:
        return 0
    
    # 排序有助于提前剪枝
    coins.sort()
    
    # 创建DP数组
    dp = [0] * (amount + 1)
    dp[0] = 1
    
    # 遍历每种硬币
    for coin in coins:
        # 如果当前硬币面值已经大于目标金额，可以跳过
        if coin > amount:
            continue
        
        # 正序遍历金额
        for i in range(coin, amount + 1):
            dp[i] += dp[i - coin]
    
    return dp[amount]

def change_2d(amount, coins):
    """
    二维DP实现，虽然空间复杂度更高，但更容易理解
    
    Args:
        amount: 总金额
        coins: 不同面额的硬币数组
    
    Returns:
        int: 硬币组合数
    """
    # 参数验证
    if amount == 0:
        return 1
    if not coins:
        return 0
    
    n = len(coins)
    # dp[i][j]表示使用前i种硬币凑成金额j的组合数
    dp = [[0] * (amount + 1) for _ in range(n + 1)]
    
    # 初始化：不使用任何硬币，只能凑成金额0
    dp[0][0] = 1
    
    # 遍历每种硬币
    for i in range(1, n + 1):
        coin = coins[i - 1]
        # 遍历每种金额
        for j in range(amount + 1):
            # 不使用当前硬币的情况
            dp[i][j] = dp[i - 1][j]
            
            # 使用当前硬币的情况（可以使用多次）
            if j >= coin:
                dp[i][j] += dp[i][j - coin]
    
    return dp[n][amount]

def permutation_change(amount, coins):
    """
    计算排列数的版本（与本题要求不同，仅作对比）
    注意：先遍历金额再遍历硬币，这样会计算排列数
    
    Args:
        amount: 总金额
        coins: 不同面额的硬币数组
    
    Returns:
        int: 硬币排列数
    """
    # 参数验证
    if amount == 0:
        return 1
    if not coins:
        return 0
    
    # 创建DP数组
    dp = [0] * (amount + 1)
    dp[0] = 1
    
    # 先遍历金额再遍历硬币，计算的是排列数
    for i in range(1, amount + 1):
        for coin in coins:
            if i >= coin:
                dp[i] += dp[i - coin]
    
    return dp[amount]

def change_dfs(amount, coins):
    """
    递归+记忆化搜索实现
    
    Args:
        amount: 总金额
        coins: 不同面额的硬币数组
    
    Returns:
        int: 硬币组合数
    """
    # 参数验证
    if amount == 0:
        return 1
    if not coins:
        return 0
    
    # 排序有助于剪枝
    coins.sort()
    
    # 使用记忆化搜索
    memo = {}
    
    def dfs(index, remaining):
        # 基础情况：凑成金额0，找到一种方式
        if remaining == 0:
            return 1
        
        # 基础情况：无法凑成
        if index == len(coins) or remaining < 0:
            return 0
        
        # 生成记忆化键
        key = (index, remaining)
        if key in memo:
            return memo[key]
        
        # 不使用当前硬币的情况
        not_use = dfs(index + 1, remaining)
        
        # 使用当前硬币的情况（如果可以使用）
        use = 0
        if remaining >= coins[index]:
            # 注意这里index不变，表示可以重复使用当前硬币
            use = dfs(index, remaining - coins[index])
        
        # 计算结果并记忆化
        memo[key] = not_use + use
        return memo[key]
    
    return dfs(0, amount)

# 测试用例
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
    coins4 = [1, 2, 5, 10]
    amount4 = 10
    print(f"测试用例4结果: {change(amount4, coins4)}")  # 预期输出: 11
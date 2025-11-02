# LeetCode 518. 零钱兑换 II
# 题目描述：给你一个整数数组 coins 表示不同面额的硬币，另给一个整数 amount 表示总金额。
# 请你计算并返回可以凑成总金额的硬币组合数。如果任何硬币组合都无法凑出总金额，返回 0 。
# 假设每一种面额的硬币有无限个。
# 链接：https://leetcode.cn/problems/coin-change-ii/
# 
# 解题思路：
# 这是一个完全背包问题的变种。我们可以将问题转化为：使用不同面额的硬币（可以重复使用），恰好凑出总金额amount的方式有多少种。
# 
# 状态定义：dp[j] 表示凑成总金额j的硬币组合数
# 状态转移方程：dp[j] += dp[j - coin]，其中coin是当前硬币的面额，且j >= coin
# 初始状态：dp[0] = 1，表示凑成总金额0的方式有一种（不使用任何硬币）
# 
# 时间复杂度：O(amount * n)，其中n是硬币的种类数
# 空间复杂度：O(amount)，使用一维DP数组

def change(amount: int, coins: list[int]) -> int:
    """
    计算可以凑成总金额的硬币组合数
    
    参数:
        amount: 总金额
        coins: 硬币面额数组
    
    返回:
        可以凑成总金额的硬币组合数
    """
    if amount < 0 or not coins:
        return 0 if amount != 0 else 1
    
    # 创建DP数组，dp[j]表示凑成总金额j的硬币组合数
    dp = [0] * (amount + 1)
    
    # 初始状态：凑成总金额0的方式有一种（不使用任何硬币）
    dp[0] = 1
    
    # 填充DP数组
    # 注意：这里我们先遍历硬币，再遍历金额，这样可以确保每个硬币只被考虑一次，避免重复计算不同的排列
    for coin in coins:
        for j in range(coin, amount + 1):
            dp[j] += dp[j - coin]
    
    return dp[amount]

def change_incorrect(amount: int, coins: list[int]) -> int:
    """
    错误实现示例：先遍历金额，再遍历硬币
    这种实现会将不同的排列视为不同的组合
    例如：[1,2]和[2,1]会被视为两种不同的组合
    
    参数:
        amount: 总金额
        coins: 硬币面额数组
    
    返回:
        排列数（不是组合数）
    """
    if amount < 0 or not coins:
        return 0 if amount != 0 else 1
    
    dp = [0] * (amount + 1)
    dp[0] = 1
    
    # 错误：先遍历金额，再遍历硬币
    for j in range(1, amount + 1):
        for coin in coins:
            if j >= coin:
                dp[j] += dp[j - coin]
    
    return dp[amount]

def change_optimized(amount: int, coins: list[int]) -> int:
    """
    优化实现：提前过滤掉大于amount的硬币
    
    参数:
        amount: 总金额
        coins: 硬币面额数组
    
    返回:
        可以凑成总金额的硬币组合数
    """
    if amount < 0:
        return 0
    if not coins:
        return 1 if amount == 0 else 0
    
    # 过滤掉大于amount的硬币
    filtered_coins = [coin for coin in coins if coin <= amount]
    
    # 创建DP数组
    dp = [0] * (amount + 1)
    dp[0] = 1
    
    # 填充DP数组
    for coin in filtered_coins:
        for j in range(coin, amount + 1):
            dp[j] += dp[j - coin]
    
    return dp[amount]

def change_both_ways(amount: int, coins: list[int]) -> dict:
    """
    同时计算组合数和排列数，用于对比
    
    参数:
        amount: 总金额
        coins: 硬币面额数组
    
    返回:
        包含组合数和排列数的字典
    """
    if amount < 0:
        return {'combinations': 0, 'permutations': 0}
    if not coins:
        return {'combinations': 1 if amount == 0 else 0, 'permutations': 1 if amount == 0 else 0}
    
    # 计算组合数（先遍历硬币，再遍历金额）
    combinations = [0] * (amount + 1)
    combinations[0] = 1
    for coin in coins:
        for j in range(coin, amount + 1):
            combinations[j] += combinations[j - coin]
    
    # 计算排列数（先遍历金额，再遍历硬币）
    permutations = [0] * (amount + 1)
    permutations[0] = 1
    for j in range(1, amount + 1):
        for coin in coins:
            if j >= coin:
                permutations[j] += permutations[j - coin]
    
    return {'combinations': combinations[amount], 'permutations': permutations[amount]}

def print_all_combinations(amount: int, coins: list[int]) -> list[list[int]]:
    """
    打印出所有可能的硬币组合
    注意：这个方法仅用于教学目的，对于大额amount可能效率不高
    
    参数:
        amount: 总金额
        coins: 硬币面额数组
    
    返回:
        所有可能的硬币组合列表
    """
    result = []
    current = []
    
    # 先对硬币排序，确保较小的面额在前
    coins.sort()
    
    def backtrack(remaining: int, index: int):
        if remaining == 0:
            # 找到一个有效组合
            result.append(current.copy())
            return
        
        if remaining < 0 or index >= len(coins):
            return
        
        # 不使用当前硬币
        backtrack(remaining, index + 1)
        
        # 使用当前硬币（可以重复使用）
        if remaining >= coins[index]:
            current.append(coins[index])
            # 注意：这里index没有增加，因为可以重复使用当前硬币
            backtrack(remaining - coins[index], index)
            current.pop()  # 回溯
    
    backtrack(amount, 0)
    
    print("所有可能的硬币组合:")
    for combo in result:
        print(combo)
    
    return result

def print_all_combinations_dp(amount: int, coins: list[int]) -> list[list[int]]:
    """
    动态规划方法打印所有可能的硬币组合
    注意：这个方法仅用于教学目的，对于大额amount可能效率不高
    
    参数:
        amount: 总金额
        coins: 硬币面额数组
    
    返回:
        所有可能的硬币组合列表
    """
    if amount < 0 or not coins:
        return [] if amount != 0 else [[]]
    
    # dp[j]存储凑成总金额j的所有组合
    dp = [[] for _ in range(amount + 1)]
    
    # 凑成总金额0的方式有一个空组合
    dp[0] = [[]]
    
    # 填充dp数组
    for coin in coins:
        for j in range(coin, amount + 1):
            # 对于dp[j - coin]中的每个组合，添加当前硬币
            for prev in dp[j - coin]:
                new_combination = prev.copy()
                new_combination.append(coin)
                dp[j].append(new_combination)
    
    print("所有可能的硬币组合 (DP实现):")
    for combo in dp[amount]:
        print(combo)
    
    return dp[amount]

def change_large_amount_optimized(amount: int, coins: list[int]) -> int:
    """
    针对大额amount的优化实现，使用模运算避免整数溢出
    注意：在Python中整数溢出不是问题，但这个方法演示了如何处理这种情况
    
    参数:
        amount: 总金额
        coins: 硬币面额数组
    
    返回:
        可以凑成总金额的硬币组合数
    """
    if amount < 0:
        return 0
    if not coins:
        return 1 if amount == 0 else 0
    
    # 过滤掉大于amount的硬币
    filtered_coins = [coin for coin in coins if coin <= amount]
    
    # 创建DP数组
    dp = [0] * (amount + 1)
    dp[0] = 1
    
    # 填充DP数组
    for coin in filtered_coins:
        for j in range(coin, amount + 1):
            dp[j] += dp[j - coin]
            # 在实际应用中，如果结果可能非常大，可以使用模运算
            # dp[j] %= MOD  # MOD可以是一个大质数，如10^9+7
    
    return dp[amount]

# 测试代码
if __name__ == "__main__":
    # 测试用例1
    amount1 = 5
    coins1 = [1, 2, 5]
    print(f"测试用例1结果: {change(amount1, coins1)} (预期: 4)")
    print_all_combinations(amount1, coins1)
    print("---------------------------")
    
    # 测试用例2
    amount2 = 3
    coins2 = [2]
    print(f"测试用例2结果: {change(amount2, coins2)} (预期: 0)")
    print("---------------------------")
    
    # 测试用例3
    amount3 = 10
    coins3 = [10]
    print(f"测试用例3结果: {change(amount3, coins3)} (预期: 1)")
    print("---------------------------")
    
    # 测试用例4
    amount4 = 0
    coins4 = [1, 2, 5]
    print(f"测试用例4结果: {change(amount4, coins4)} (预期: 1)")
    
    # 测试组合数与排列数的区别
    print("\n测试组合数与排列数的区别:")
    result = change_both_ways(amount1, coins1)
    print(f"总金额 {amount1}，硬币 {coins1}:")
    print(f"组合数: {result['combinations']}")
    print(f"排列数: {result['permutations']}")
    
    # 测试DP打印组合功能
    print("\n测试DP打印组合功能:")
    print_all_combinations_dp(amount1, coins1)
    
    # 测试优化版本
    print("\n测试优化版本:")
    print(f"测试用例1结果: {change_optimized(amount1, coins1)} (预期: 4)")
    
    # 测试更大的金额
    print("\n测试更大的金额:")
    amount5 = 100
    coins5 = [1, 5, 10, 25]
    print(f"总金额 {amount5}，硬币 {coins5} 的组合数: {change(amount5, coins5)}")
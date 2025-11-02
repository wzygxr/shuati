# LeetCode 322. 零钱兑换
# 题目描述：给你一个整数数组 coins ，表示不同面额的硬币；以及一个整数 amount ，表示总金额。
# 计算并返回可以凑成总金额所需的 最少的硬币个数 。如果没有任何一种硬币组合能组成总金额，返回 -1 。
# 你可以认为每种硬币的数量是无限的。
# 链接：https://leetcode.cn/problems/coin-change/
# 
# 解题思路：
# 这是一个典型的完全背包问题，因为每种硬币可以使用无限次。
# 状态定义：dp[i] 表示凑成金额 i 所需的最少硬币个数
# 状态转移方程：dp[i] = min(dp[i], dp[i - coins[j]] + 1)，其中 j 遍历所有硬币
# 初始状态：dp[0] = 0（凑成金额0需要0个硬币），其他初始化为一个较大值（如amount+1）
# 
# 时间复杂度：O(amount * n)，其中n是硬币种类数
# 空间复杂度：O(amount)，使用一维DP数组

def coin_change(coins, amount):
    """
    计算凑成总金额所需的最少硬币个数
    
    Args:
        coins: 不同面额的硬币数组
        amount: 总金额
    
    Returns:
        int: 最少硬币个数，如果无法凑成则返回-1
    """
    # 参数验证
    if amount == 0:
        return 0
    if not coins:
        return -1
    
    # 创建DP数组，dp[i]表示凑成金额i所需的最少硬币个数
    dp = [amount + 1] * (amount + 1)
    
    # 基础情况：凑成金额0需要0个硬币
    dp[0] = 0
    
    # 遍历每种硬币（物品）
    for coin in coins:
        # 正序遍历金额（容量），因为完全背包允许重复使用物品
        for i in range(coin, amount + 1):
            # 状态转移：选择当前硬币或不选择当前硬币
            dp[i] = min(dp[i], dp[i - coin] + 1)
    
    # 如果dp[amount]仍为初始值，说明无法凑成
    return dp[amount] if dp[amount] <= amount else -1

def coin_change_optimized(coins, amount):
    """
    优化版本：提前剪枝和优化循环范围
    
    Args:
        coins: 不同面额的硬币数组
        amount: 总金额
    
    Returns:
        int: 最少硬币个数，如果无法凑成则返回-1
    """
    # 参数验证和快速返回
    if amount == 0:
        return 0
    if not coins:
        return -1
    
    # 对硬币进行排序，从小到大
    coins.sort()
    
    # 创建DP数组
    dp = [amount + 1] * (amount + 1)
    dp[0] = 0
    
    # 遍历金额
    for i in range(1, amount + 1):
        # 遍历每种硬币
        for coin in coins:
            # 剪枝：如果当前硬币大于金额i，直接跳过
            if coin > i:
                break
            # 状态转移
            if dp[i - coin] != amount + 1:
                dp[i] = min(dp[i], dp[i - coin] + 1)
    
    return dp[amount] if dp[amount] <= amount else -1

def coin_change_bfs(coins, amount):
    """
    BFS优化版本：对于找最少硬币个数的问题，BFS可能更快找到解
    
    Args:
        coins: 不同面额的硬币数组
        amount: 总金额
    
    Returns:
        int: 最少硬币个数，如果无法凑成则返回-1
    """
    # 参数验证
    if amount == 0:
        return 0
    if not coins:
        return -1
    
    # 对硬币进行排序，有助于提前剪枝
    coins.sort()
    
    # 使用BFS，每个节点表示当前的金额和已使用的硬币个数
    # 使用一个集合记录已经访问过的金额，避免重复计算
    visited = set()
    from collections import deque
    queue = deque([0])
    visited.add(0)
    level = 0  # 当前层数，表示已使用的硬币个数
    
    while queue:
        size = len(queue)
        level += 1
        
        for _ in range(size):
            current = queue.popleft()
            
            # 尝试每种硬币
            for coin in coins:
                next_amount = current + coin
                
                # 如果找到目标金额，返回当前层数
                if next_amount == amount:
                    return level
                
                # 剪枝：如果超过目标金额或已经访问过，跳过
                if next_amount > amount or next_amount in visited:
                    continue
                
                visited.add(next_amount)
                queue.append(next_amount)
    
    # 无法凑成目标金额
    return -1

def coin_change_greedy_dfs(coins, amount):
    """
    贪心+DFS优化版本：对于某些情况（如硬币是倍数关系时）效率更高
    
    Args:
        coins: 不同面额的硬币数组
        amount: 总金额
    
    Returns:
        int: 最少硬币个数，如果无法凑成则返回-1
    """
    # 参数验证
    if amount == 0:
        return 0
    if not coins:
        return -1
    
    # 对硬币进行排序，从大到小
    coins.sort(reverse=True)
    
    # 记录最小硬币个数
    min_count = float('inf')
    
    def dfs(index, remaining, count):
        nonlocal min_count
        
        # 已经找到一个解，或者当前硬币个数已经超过已知的最小硬币个数，直接返回
        if remaining == 0:
            min_count = min(min_count, count)
            return
        
        if index == len(coins) or count >= min_count - 1:
            return
        
        # 贪心策略：尽可能多地使用当前面值的硬币
        max_use = remaining // coins[index]
        for i in range(max_use, -1, -1):
            new_remaining = remaining - i * coins[index]
            new_count = count + i
            
            # 剪枝：如果剩余金额为0或者当前硬币个数加上剩余金额的最小可能个数
            # 仍然小于已知的最小硬币个数，才继续搜索
            if new_remaining == 0 or new_count + 1 < min_count:
                dfs(index + 1, new_remaining, new_count)
    
    dfs(0, amount, 0)
    
    return min_count if min_count != float('inf') else -1

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    coins1 = [1, 2, 5]
    amount1 = 11
    print(f"测试用例1结果: {coin_change(coins1, amount1)}")  # 预期输出: 3 (5+5+1)
    
    # 测试用例2
    coins2 = [2]
    amount2 = 3
    print(f"测试用例2结果: {coin_change(coins2, amount2)}")  # 预期输出: -1
    
    # 测试用例3
    coins3 = [1]
    amount3 = 0
    print(f"测试用例3结果: {coin_change(coins3, amount3)}")  # 预期输出: 0
    
    # 测试用例4
    coins4 = [1, 2, 5, 10, 20, 50, 100]
    amount4 = 489
    print(f"测试用例4结果: {coin_change(coins4, amount4)}")  # 预期输出: 9
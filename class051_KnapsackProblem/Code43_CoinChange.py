# LeetCode 322. 零钱兑换
# 题目描述：给你一个整数数组 coins ，表示不同面额的硬币；以及一个整数 amount ，表示总金额。
# 计算并返回可以凑成总金额所需的 最少的硬币个数 。如果没有任何一种硬币组合能组成总金额，返回 -1 。
# 你可以认为每种硬币的数量是无限的。
# 链接：https://leetcode.cn/problems/coin-change/
# 
# 解题思路：
# 这是一个经典的完全背包问题。我们需要找到凑成总金额amount所需的最少硬币数量，每种硬币可以重复使用。
# 
# 状态定义：dp[j] 表示凑成总金额j所需的最少硬币数量
# 状态转移方程：dp[j] = min(dp[j], dp[j - coin] + 1)，其中coin是当前硬币的面额，且j >= coin
# 初始状态：dp[0] = 0，表示凑成总金额0所需的最少硬币数量为0；对于其他j，初始化为一个较大的值（如amount + 1）
# 
# 时间复杂度：O(amount * n)，其中amount是总金额，n是硬币的种类数
# 空间复杂度：O(amount)，使用一维DP数组

def coin_change(coins: list[int], amount: int) -> int:
    """
    计算凑成总金额所需的最少硬币数量
    
    参数:
        coins: 硬币面额数组
        amount: 总金额
    
    返回:
        最少硬币数量，如果无法凑出则返回-1
    """
    if amount < 0 or not coins:
        return 0 if amount == 0 else -1
    
    # 创建DP数组，dp[j]表示凑成总金额j所需的最少硬币数量
    # 初始化为一个较大的值（amount + 1），因为最多需要amount个1元硬币
    dp = [amount + 1] * (amount + 1)
    
    # 初始状态：凑成总金额0所需的最少硬币数量为0
    dp[0] = 0
    
    # 填充DP数组
    # 遍历硬币
    for coin in coins:
        # 遍历金额
        for j in range(coin, amount + 1):
            # 更新凑成总金额j所需的最少硬币数量
            dp[j] = min(dp[j], dp[j - coin] + 1)
    
    # 如果dp[amount]仍然是初始值，说明无法凑出总金额
    return dp[amount] if dp[amount] <= amount else -1

def coin_change2(coins: list[int], amount: int) -> int:
    """
    从金额角度出发的实现
    这种实现方式与上面的类似，只是遍历顺序不同
    """
    if amount < 0 or not coins:
        return 0 if amount == 0 else -1
    
    dp = [amount + 1] * (amount + 1)
    dp[0] = 0
    
    # 遍历金额
    for j in range(1, amount + 1):
        # 遍历硬币
        for coin in coins:
            if coin <= j:
                dp[j] = min(dp[j], dp[j - coin] + 1)
    
    return dp[amount] if dp[amount] <= amount else -1

def coin_change_bfs(coins: list[int], amount: int) -> int:
    """
    广度优先搜索(BFS)实现
    将问题视为图的最短路径问题：从0到amount的最短路径
    """
    if amount == 0:
        return 0
    if not coins or amount < 0:
        return -1
    
    # 使用集合记录访问过的金额，避免重复计算
    visited = set()
    visited.add(0)
    
    # BFS队列，存储当前金额和步数
    queue = [(0, 0)]  # (current_amount, steps)
    
    while queue:
        current, steps = queue.pop(0)
        steps += 1
        
        # 尝试每种硬币
        for coin in coins:
            next_amount = current + coin
            
            # 如果达到目标金额，返回当前步数
            if next_amount == amount:
                return steps
            
            # 如果没有超过目标金额且未访问过，则加入队列
            if next_amount < amount and next_amount not in visited:
                visited.add(next_amount)
                queue.append((next_amount, steps))
    
    # 无法凑出总金额
    return -1

def coin_change_greedy(coins: list[int], amount: int) -> int:
    """
    贪心 + 回溯 实现
    注意：这种方法不一定能得到正确的结果，因为贪心策略不一定适用于所有硬币组合
    例如，对于 coins = [1, 3, 4], amount = 6，贪心会选择 [4, 1, 1]（3个硬币），但最优解是 [3, 3]（2个硬币）
    """
    if amount == 0:
        return 0
    if not coins or amount < 0:
        return -1
    
    # 按面额降序排序
    coins.sort(reverse=True)
    result = float('inf')
    
    def backtrack(amount_remaining, coin_index, count):
        nonlocal result
        # 已经找到一个解，或者无法继续使用更大的硬币
        if amount_remaining == 0:
            result = min(result, count)
            return
        if coin_index >= len(coins):
            return
        
        # 尝试使用当前硬币的最大可能数量
        max_usage = amount_remaining // coins[coin_index]
        # 从最大可能数量开始尝试，直到0
        for i in range(max_usage, -1, -1):
            # 剪枝：如果当前数量已经超过了已知的最优解，则停止尝试
            if count + i >= result:
                break
            backtrack(amount_remaining - i * coins[coin_index], coin_index + 1, count + i)
    
    backtrack(amount, 0, 0)
    
    return result if result != float('inf') else -1

def coin_change_memo(coins: list[int], amount: int) -> int:
    """
    优化的回溯方法，使用记忆化搜索
    """
    if amount == 0:
        return 0
    if not coins or amount < 0:
        return -1
    
    # 创建记忆化缓存，初始化为-2（表示未计算过）
    memo = [-2] * (amount + 1)
    memo[0] = 0  # 基础情况：凑成0元需要0个硬币
    
    def backtrack_memo(remaining):
        if remaining == 0:
            return 0
        if remaining < 0:
            return -1
        
        # 检查是否已经计算过
        if memo[remaining] != -2:
            return memo[remaining]
        
        min_count = float('inf')
        
        # 尝试每种硬币
        for coin in coins:
            sub_result = backtrack_memo(remaining - coin)
            if sub_result >= 0 and sub_result < min_count:
                min_count = sub_result + 1
        
        # 记录结果
        memo[remaining] = min_count if min_count != float('inf') else -1
        
        return memo[remaining]
    
    return backtrack_memo(amount)

def print_optimal_coins(coins: list[int], amount: int):
    """
    打印出一种最优的硬币组合
    """
    if amount == 0:
        print("无需硬币")
        return
    if not coins or amount < 0:
        print("无法凑出总金额")
        return
    
    # 计算最少硬币数量
    dp = [amount + 1] * (amount + 1)
    dp[0] = 0
    
    # 额外创建一个数组，用于记录每个金额使用的最后一个硬币
    last_coin = [0] * (amount + 1)
    
    # 填充DP数组
    for coin in coins:
        for j in range(coin, amount + 1):
            if dp[j] > dp[j - coin] + 1:
                dp[j] = dp[j - coin] + 1
                last_coin[j] = coin
    
    if dp[amount] > amount:
        print("无法凑出总金额")
        return
    
    # 回溯构建最优硬币组合
    result = []
    current = amount
    while current > 0:
        coin = last_coin[current]
        result.append(coin)
        current -= coin
    
    # 输出结果
    print("最优硬币组合: ", end="")
    print(" + ".join(map(str, result)), end=" = ")
    print(amount)
    print(f"最少硬币数量: {dp[amount]}")

def coin_change_optimized(coins: list[int], amount: int) -> int:
    """
    优化版本，提前过滤掉大于amount的硬币
    """
    if amount < 0 or not coins:
        return 0 if amount == 0 else -1
    
    # 过滤掉大于amount的硬币
    filtered_coins = [coin for coin in coins if coin <= amount]
    
    if not filtered_coins:
        return 0 if amount == 0 else -1
    
    dp = [amount + 1] * (amount + 1)
    dp[0] = 0
    
    for coin in filtered_coins:
        for j in range(coin, amount + 1):
            dp[j] = min(dp[j], dp[j - coin] + 1)
    
    return dp[amount] if dp[amount] <= amount else -1

# 测试代码
if __name__ == "__main__":
    # 测试用例1
    coins1 = [1, 2, 5]
    amount1 = 11
    print(f"测试用例1结果: {coin_change(coins1, amount1)} (预期: 3)")
    print_optimal_coins(coins1, amount1)
    print("---------------------------")
    
    # 测试用例2
    coins2 = [2]
    amount2 = 3
    print(f"测试用例2结果: {coin_change(coins2, amount2)} (预期: -1)")
    print("---------------------------")
    
    # 测试用例3
    coins3 = [1]
    amount3 = 0
    print(f"测试用例3结果: {coin_change(coins3, amount3)} (预期: 0)")
    print("---------------------------")
    
    # 测试用例4
    coins4 = [186, 419, 83, 408]
    amount4 = 6249
    print(f"测试用例4结果: {coin_change(coins4, amount4)} (预期: 20)")
    
    # 测试各种实现方法
    print("\n测试各种实现方法:")
    print(f"方法2: {coin_change2(coins1, amount1)}")
    print(f"BFS方法: {coin_change_bfs(coins1, amount1)}")
    print(f"贪心回溯方法: {coin_change_greedy(coins1, amount1)}")
    print(f"记忆化搜索方法: {coin_change_memo(coins1, amount1)}")
    print(f"优化方法: {coin_change_optimized(coins1, amount1)}")
    
    # 测试特殊情况 - 硬币包含0
    print("\n测试特殊情况 - 硬币包含0:")
    coins5 = [0, 1, 2, 5]
    print(f"结果: {coin_change(coins5, amount1)}")
    print(f"优化方法结果: {coin_change_optimized(coins5, amount1)}")
    
    # 测试大额硬币
    print("\n测试大额硬币:")
    coins6 = [500, 100, 50, 10, 5, 1]
    amount6 = 12345
    print(f"大额硬币结果: {coin_change(coins6, amount6)}")
    
    # 性能测试
    print("\n性能测试:")
    import time
    
    # 创建一个较大的测试用例
    large_amount = 1000
    start_time = time.time()
    dp_result = coin_change(coins6, large_amount)
    dp_time = time.time() - start_time
    print(f"DP方法结果: {dp_result}, 耗时: {dp_time:.6f}秒")
    
    # 测试BFS方法
    start_time = time.time()
    bfs_result = coin_change_bfs(coins6, large_amount)
    bfs_time = time.time() - start_time
    print(f"BFS方法结果: {bfs_result}, 耗时: {bfs_time:.6f}秒")
    
    # 测试记忆化搜索方法
    start_time = time.time()
    memo_result = coin_change_memo(coins6, large_amount)
    memo_time = time.time() - start_time
    print(f"记忆化搜索方法结果: {memo_result}, 耗时: {memo_time:.6f}秒")
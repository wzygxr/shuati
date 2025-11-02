# LeetCode 322. 零钱兑换
# 题目描述：给你一个整数数组 coins ，表示不同面额的硬币；以及一个整数 amount ，表示总金额。
# 计算并返回可以凑成总金额所需的最少的硬币个数。如果没有任何一种硬币组合能组成总金额，返回 -1 。
# 你可以认为每种硬币的数量是无限的。
# 链接：https://leetcode.cn/problems/coin-change/
# 
# 解题思路：
# 这是一个典型的完全背包问题：
# - 硬币可以多次使用（完全背包的特点）
# - 目标是凑成总金额，并且硬币个数最少（最优解问题）
# 
# 状态定义：dp[i] 表示凑成金额i所需的最少硬币个数
# 状态转移方程：dp[i] = min(dp[i], dp[i - coin] + 1)，其中coin是每种硬币的面额，且i >= coin
# 初始状态：dp[0] = 0（凑成金额0不需要任何硬币），其他位置初始化为一个较大的值（表示无法凑成）
# 
# 时间复杂度：O(amount * n)，其中n是硬币的种类数
# 空间复杂度：O(amount)，使用一维DP数组

from typing import List
from functools import lru_cache
import sys


def coinChange(coins: List[int], amount: int) -> int:
    """
    计算凑成总金额所需的最少硬币个数
    
    Args:
        coins: 不同面额的硬币数组
        amount: 总金额
    
    Returns:
        最少硬币个数，如果无法凑成则返回-1
    """
    # 参数验证
    if amount < 0:
        return -1
    if amount == 0:
        return 0
    if not coins:
        return -1
    
    # 创建DP数组，dp[i]表示凑成金额i所需的最少硬币个数
    # 初始化为amount + 1，因为最多使用amount个面值为1的硬币，所以amount + 1是一个不可能达到的值
    dp = [amount + 1] * (amount + 1)
    dp[0] = 0  # 凑成金额0不需要任何硬币
    
    # 遍历每种硬币
    for coin in coins:
        # 完全背包问题，正序遍历金额（允许重复使用硬币）
        for i in range(coin, amount + 1):
            # 状态转移：取不使用当前硬币和使用当前硬币的最小值
            dp[i] = min(dp[i], dp[i - coin] + 1)
    
    # 如果dp[amount]仍然是初始值，说明无法凑成总金额
    return dp[amount] if dp[amount] <= amount else -1


def coinChangeOptimized(coins: List[int], amount: int) -> int:
    """
    优化版本：提前排序硬币，允许提前终止某些循环
    """
    if amount < 0:
        return -1
    if amount == 0:
        return 0
    if not coins:
        return -1
    
    # 排序硬币，从小到大
    coins.sort()
    
    dp = [amount + 1] * (amount + 1)
    dp[0] = 0
    
    for coin in coins:
        # 如果当前硬币面额大于amount，可以直接跳过
        if coin > amount:
            break
        
        for i in range(coin, amount + 1):
            dp[i] = min(dp[i], dp[i - coin] + 1)
    
    return dp[amount] if dp[amount] <= amount else -1


def coinChange2D(coins: List[int], amount: int) -> int:
    """
    二维DP数组实现（更直观但空间复杂度更高）
    dp[i][j]表示使用前i种硬币，凑成金额j所需的最少硬币个数
    """
    if amount < 0:
        return -1
    if amount == 0:
        return 0
    if not coins:
        return -1
    
    n = len(coins)
    # 创建二维DP数组
    dp = [[amount + 1] * (amount + 1) for _ in range(n + 1)]
    dp[0][0] = 0  # 凑成金额0不需要任何硬币
    
    # 填充DP数组
    for i in range(1, n + 1):
        coin = coins[i - 1]
        
        for j in range(amount + 1):
            # 不使用第i种硬币
            dp[i][j] = dp[i - 1][j]
            
            # 使用第i种硬币（如果可以的话）
            if j >= coin:
                # 完全背包问题：可以重复使用同一种硬币，所以是dp[i][j-coin]而不是dp[i-1][j-coin]
                dp[i][j] = min(dp[i][j], dp[i][j - coin] + 1)
    
    return dp[n][amount] if dp[n][amount] <= amount else -1


def coinChangeDFS(coins: List[int], amount: int) -> int:
    """
    递归+记忆化搜索实现
    """
    if amount < 0:
        return -1
    if amount == 0:
        return 0
    if not coins:
        return -1
    
    # 使用lru_cache进行记忆化
    @lru_cache(maxsize=None)
    def dfs(current_amount: int) -> int:
        """
        递归辅助函数
        
        Args:
            current_amount: 当前需要凑成的金额
            
        Returns:
            凑成当前金额所需的最少硬币个数，如果无法凑成则返回-1
        """
        # 如果金额小于0，无法凑成
        if current_amount < 0:
            return -1
        
        # 如果金额为0，不需要硬币
        if current_amount == 0:
            return 0
        
        min_coins = float('inf')
        
        # 尝试使用每种硬币
        for coin in coins:
            sub_result = dfs(current_amount - coin)
            # 如果子问题有解，更新最小硬币数
            if sub_result != -1:
                min_coins = min(min_coins, sub_result + 1)
        
        # 返回结果，如果无法凑成返回-1
        return min_coins if min_coins != float('inf') else -1
    
    return dfs(amount)


def coinChangeBFS(coins: List[int], amount: int) -> int:
    """
    BFS实现：找到最少硬币个数，相当于找到从0到amount的最短路径
    """
    if amount < 0:
        return -1
    if amount == 0:
        return 0
    if not coins:
        return -1
    
    # 使用BFS，队列中存储当前金额
    # 为了避免重复访问，使用visited数组记录已经访问过的金额
    visited = [False] * (amount + 1)
    queue = [0]
    visited[0] = True
    step = 0
    
    while queue:
        size = len(queue)
        step += 1
        
        # 遍历当前层的所有节点
        for _ in range(size):
            current = queue.pop(0)
            
            # 尝试使用每种硬币
            for coin in coins:
                next_amount = current + coin
                
                # 如果达到目标金额，返回当前步数
                if next_amount == amount:
                    return step
                
                # 如果next_amount在有效范围内且未被访问过，加入队列
                if next_amount < amount and not visited[next_amount]:
                    visited[next_amount] = True
                    queue.append(next_amount)
    
    # 无法凑成总金额
    return -1


def coinChangeGreedy(coins: List[int], amount: int) -> int:
    """
    贪心算法 + DFS剪枝（在某些情况下可能比动态规划更快）
    注意：贪心算法并不总是能得到最优解，因为可能存在较大面额的硬币虽然看起来更优，但会导致后续无法凑成总金额
    """
    if amount < 0:
        return -1
    if amount == 0:
        return 0
    if not coins:
        return -1
    
    # 按面值降序排序
    coins.sort(reverse=True)
    
    min_coins = float('inf')
    
    def greedy_dfs(index: int, current_amount: int, count: int) -> None:
        """
        贪心DFS辅助函数
        
        Args:
            index: 当前考虑的硬币索引
            current_amount: 剩余需要凑成的金额
            count: 已经使用的硬币个数
        """
        nonlocal min_coins
        
        # 如果剩余金额为0，更新最小硬币个数
        if current_amount == 0:
            min_coins = min(min_coins, count)
            return
        
        # 如果已经考虑完所有硬币，或者当前硬币个数已经超过已知的最小硬币个数，返回
        if index == len(coins) or count >= min_coins:
            return
        
        coin = coins[index]
        # 贪心策略：尽可能多地使用当前面额的硬币
        max_count = current_amount // coin
        
        # 从最多可以使用的个数开始尝试，逐步减少
        for i in range(max_count, -1, -1):
            # 剪枝：如果当前硬币个数加上剩余金额用面值为1的硬币（最坏情况）都超过已知的最小硬币个数，直接跳过
            if count + i >= min_coins:
                break
            
            # 递归尝试使用剩余的硬币凑成剩余的金额
            greedy_dfs(index + 1, current_amount - i * coin, count + i)
    
    # 调用贪心DFS
    greedy_dfs(0, amount, 0)
    
    return min_coins if min_coins != float('inf') else -1


# 测试函数
def test_coin_change():
    # 测试用例1
    coins1 = [1, 2, 5]
    amount1 = 11
    print(f"测试用例1结果: {coinChange(coins1, amount1)}")  # 预期输出: 3 (5+5+1)
    
    # 测试用例2
    coins2 = [2]
    amount2 = 3
    print(f"测试用例2结果: {coinChange(coins2, amount2)}")  # 预期输出: -1
    
    # 测试用例3
    coins3 = [1]
    amount3 = 0
    print(f"测试用例3结果: {coinChange(coins3, amount3)}")  # 预期输出: 0


# 执行测试
if __name__ == "__main__":
    test_coin_change()
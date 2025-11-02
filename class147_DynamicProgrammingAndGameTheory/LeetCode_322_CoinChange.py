"""
LeetCode 322. Coin Change (硬币找零)
题目描述：给定不同面额的硬币 coins 和一个总金额 amount。
编写一个函数来计算可以凑成总金额所需的最少的硬币个数。
如果没有任何一种硬币组合能组成总金额，返回 -1。

解题思路：
这是一个经典的动态规划问题：
1. 状态定义：dp[i] 表示凑成金额 i 所需的最少硬币数
2. 状态转移方程：dp[i] = min(dp[i], dp[i - coin] + 1) for each coin in coins
3. 初始状态：dp[0] = 0，其他初始化为一个大值

时间复杂度：O(amount * coins.length)
空间复杂度：O(amount)

工程化考量：
1. 异常处理：处理无效输入（负数、空数组等）
2. 边界条件：amount为0时返回0
3. 性能优化：可以使用滚动数组优化空间复杂度
4. 可扩展性：可以轻松扩展支持不同的硬币面额
"""

def coinChange(coins, amount):
    """
    计算凑成总金额所需的最少硬币个数
    
    Args:
        coins: List[int] - 不同面额的硬币
        amount: int - 总金额
    
    Returns:
        int - 最少硬币个数，无法凑成则返回-1
    """
    # 异常处理
    if amount < 0 or not coins:
        return -1
    
    # 边界条件
    if amount == 0:
        return 0
    
    # dp[i] 表示凑成金额 i 所需的最少硬币数
    dp = [amount + 1] * (amount + 1)
    
    # 初始状态
    dp[0] = 0
    
    # 状态转移
    for i in range(1, amount + 1):
        for coin in coins:
            if i >= coin:
                dp[i] = min(dp[i], dp[i - coin] + 1)
    
    # 返回结果
    return -1 if dp[amount] > amount else dp[amount]


# 测试函数
def test_coinChange():
    """测试coinChange函数"""
    # 测试用例1
    coins1 = [1, 3, 4]
    amount1 = 6
    result1 = coinChange(coins1, amount1)
    print(f"Test case 1: coins = {coins1}, amount = {amount1}, result = {result1}")
    
    # 测试用例2
    coins2 = [2]
    amount2 = 3
    result2 = coinChange(coins2, amount2)
    print(f"Test case 2: coins = {coins2}, amount = {amount2}, result = {result2}")
    
    # 测试用例3
    coins3 = [1]
    amount3 = 0
    result3 = coinChange(coins3, amount3)
    print(f"Test case 3: coins = {coins3}, amount = {amount3}, result = {result3}")


# 运行测试
if __name__ == "__main__":
    test_coinChange()
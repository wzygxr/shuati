# LeetCode 322. 零钱兑换
# 题目描述：给你一个整数数组 coins ，表示不同面额的硬币；以及一个整数 amount ，表示总金额。
# 计算并返回可以凑成总金额所需的最少的硬币个数。如果没有任何一种硬币组合能组成总金额，返回 -1 。
# 你可以认为每种硬币的数量是无限的。
# 链接：https://leetcode.cn/problems/coin-change/
# 
# 解题思路：
# 这是一个完全背包问题的变形。
# 1. 状态定义：dp[i] 表示凑成金额 i 所需的最少硬币个数
# 2. 状态转移方程：dp[i] = min(dp[i], dp[i-coin] + 1)，其中 coin 是每种硬币的面额
# 3. 初始状态：dp[0] = 0（凑成金额0不需要硬币），其余初始化为一个较大值
# 4. 遍历顺序：由于硬币可以重复使用，这是完全背包问题，使用正序遍历金额
# 
# 时间复杂度：O(amount * n)，其中 n 是硬币种类数
# 空间复杂度：O(amount)


def coin_change(coins, amount):
    """
    计算凑成总金额所需的最少硬币个数
    
    解题思路：
    这是一个完全背包问题的变形。
    1. 状态定义：dp[i] 表示凑成金额 i 所需的最少硬币个数
    2. 状态转移方程：dp[i] = min(dp[i], dp[i-coin] + 1)，其中 coin 是每种硬币的面额
    3. 初始状态：dp[0] = 0（凑成金额0不需要硬币），其余初始化为一个较大值
    4. 遍历顺序：由于硬币可以重复使用，这是完全背包问题，使用正序遍历金额
    
    Args:
        coins: 不同面额的硬币列表
        amount: 目标总金额
    
    Returns:
        最少硬币个数，如果无法凑成返回-1
    
    Raises:
        TypeError: 如果输入类型不正确
        ValueError: 如果输入值无效
    """
    # 边界条件处理
    if amount < 0:
        return -1  # 金额不能为负数
    if amount == 0:
        return 0  # 金额为0不需要硬币
    if not coins:
        return -1  # 硬币列表为空，无法凑成任何金额
    
    # 创建dp数组，dp[i]表示凑成金额i所需的最少硬币个数
    # 初始化为amount + 1（因为最多使用amount个面值为1的硬币）
    dp = [amount + 1] * (amount + 1)
    dp[0] = 0  # 基础情况：凑成金额0需要0个硬币
    
    # 遍历每种硬币（物品）
    for coin in coins:
        # 完全背包问题，正序遍历金额（容量）
        # 这样可以保证每个硬币可以被重复使用
        for j in range(coin, amount + 1):
            # 状态转移：选择当前硬币或不选当前硬币
            # 如果选择当前硬币，则需要dp[j-coin] + 1个硬币
            # dp[j] = min(不选择当前硬币, 选择当前硬币)
            # 不选择当前硬币：dp[j]（保持原值）
            # 选择当前硬币：dp[j - coin] + 1（前一个状态+1个当前硬币）
            dp[j] = min(dp[j], dp[j - coin] + 1)
    
    # 如果dp[amount]仍为初始值，说明无法凑成该金额
    return dp[amount] if dp[amount] != amount + 1 else -1


def coin_change_optimized(coins, amount):
    """
    优化版本：提前剪枝和优化
    
    Args:
        coins: 不同面额的硬币列表
        amount: 目标总金额
    
    Returns:
        最少硬币个数，如果无法凑成返回-1
    """
    # 边界条件处理
    if amount < 0:
        return -1
    if amount == 0:
        return 0
    if not coins:
        return -1
    
    # 对硬币进行排序，从小到大
    coins.sort()
    
    dp = [amount + 1] * (amount + 1)
    dp[0] = 0
    
    for coin in coins:
        # 剪枝：如果当前硬币面值大于目标金额，可以跳过
        if coin > amount:
            continue
        
        for j in range(coin, amount + 1):
            dp[j] = min(dp[j], dp[j - coin] + 1)
    
    return dp[amount] if dp[amount] != amount + 1 else -1


def coin_change_production(coins, amount):
    """
    工程化版本：包含详细的输入验证和异常处理
    
    Args:
        coins: 不同面额的硬币列表
        amount: 目标总金额
    
    Returns:
        最少硬币个数，如果无法凑成返回-1
    
    Raises:
        TypeError: 如果输入类型不正确
        ValueError: 如果输入值无效
    """
    try:
        # 输入类型验证
        if not isinstance(coins, list):
            raise TypeError("coins必须是列表类型")
        if not isinstance(amount, int):
            raise TypeError("amount必须是整数类型")
        
        # 输入值验证
        if amount < 0:
            raise ValueError("amount不能为负数")
        
        # 检查硬币面值是否有效
        for coin in coins:
            if not isinstance(coin, int) or coin <= 0:
                raise ValueError(f"硬币面值必须为正整数: {coin}")
        
        # 调用核心算法
        return coin_change(coins, amount)
    except (TypeError, ValueError) as e:
        # 在实际项目中，这里应该记录异常日志
        print(f"计算硬币兑换时发生错误: {e}")
        raise  # 重新抛出异常，让调用者处理


# 测试代码
if __name__ == "__main__":
    # 测试用例1
    coins1 = [1, 2, 5]
    amount1 = 11
    print(f"测试用例1结果: {coin_change(coins1, amount1)}")  # 预期输出: 3 (11 = 5 + 5 + 1)
    
    # 测试用例2
    coins2 = [2]
    amount2 = 3
    print(f"测试用例2结果: {coin_change(coins2, amount2)}")  # 预期输出: -1
    
    # 测试用例3
    coins3 = [1]
    amount3 = 0
    print(f"测试用例3结果: {coin_change(coins3, amount3)}")  # 预期输出: 0
    
    # 测试异常情况
    try:
        coin_change_production([1, -1, 5], 10)
    except ValueError as e:
        print(f"正确捕获异常: {e}")

'''
示例:
输入: coins = [1, 2, 5], amount = 11
输出: 3
解释: 11 = 5 + 5 + 1

输入: coins = [2], amount = 3
输出: -1

输入: coins = [1], amount = 0
输出: 0

时间复杂度: O(amount * n)
  - 外层循环遍历所有硬币：O(n)
  - 内层循环遍历金额：O(amount)
空间复杂度: O(amount)
  - 一维DP数组的空间消耗
'''
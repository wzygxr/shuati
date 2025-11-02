# 洛谷P1060 [NOIP2006 普及组] 开心的金明
# 题目描述：金明需要购买物品，在不超过N元的前提下，使每件物品的价格与重要度的乘积的总和最大。
# 链接：https://www.luogu.com.cn/problem/P1060
# 
# 解题思路：
# 这是经典的01背包问题。
# 1. 每个物品的"价值"是价格与重要度的乘积
# 2. dp[i][j] 表示前i个物品，预算为j时能获得的最大价值总和
# 3. 状态转移方程：
#    dp[i][j] = max(dp[i-1][j], dp[i-1][j-price[i]] + price[i] * importance[i])  (当j >= price[i]时)
#    dp[i][j] = dp[i-1][j]  (当j < price[i]时)
# 4. 可以使用滚动数组优化空间复杂度
#
# 时间复杂度：O(m * n)
# 空间复杂度：O(n)

def maxSatisfaction(n, m, prices, importances):
    """
    计算开心的金明能获得的最大满意度
    
    解题思路：
    这是经典的01背包问题。
    1. 每个物品的"价值"是价格与重要度的乘积
    2. dp[i][j] 表示前i个物品，预算为j时能获得的最大价值总和
    3. 状态转移方程：
       dp[i][j] = max(dp[i-1][j], dp[i-1][j-price[i]] + price[i] * importance[i])  (当j >= price[i]时)
       dp[i][j] = dp[i-1][j]  (当j < price[i]时)
    4. 可以使用滚动数组优化空间复杂度
    
    Args:
        n: 总预算
        m: 物品数量
        prices: 物品价格数组
        importances: 物品重要度数组
    
    Returns:
        能获得的最大满意度
    """
    # dp[j] 表示预算为j时能获得的最大价值总和
    # 这里使用了空间优化的一维DP数组，相当于dp[i][j]压缩为dp[j]
    dp = [0] * (n + 1)
    
    # 遍历每个物品（01背包的物品遍历）
    for i in range(m):
        # 获取当前物品的价格和重要度
        price = prices[i]
        importance = importances[i]
        # 计算当前物品的价值 = 价格 * 重要度
        value = price * importance
        
        # 01背包需要倒序遍历，确保每个物品只使用一次
        # j表示当前的预算
        for j in range(n, price - 1, -1):
            # 状态转移方程：
            # dp[j] = max(不选择当前物品, 选择当前物品)
            # 不选择当前物品：dp[j]（保持原值）
            # 选择当前物品：dp[j - price] + value（前一个状态+当前物品价值）
            dp[j] = max(dp[j], dp[j - price] + value)
    
    # 返回预算为n时能获得的最大价值总和
    return dp[n]

'''
示例:
输入: n = 1000, m = 5
prices = [800, 400, 300, 400, 200]
importances = [2, 5, 5, 3, 2]
输出: 3900
解释: 选择物品2,3,4，总价格400+300+400=1100，但超过预算，需要重新选择

时间复杂度: O(m * n)
  - 外层循环遍历所有物品：O(m)
  - 内层循环遍历预算：O(n)
空间复杂度: O(n)
  - 一维DP数组的空间消耗
'''
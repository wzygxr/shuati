# LeetCode 879. 盈利计划
# 题目描述：集团里有 n 名员工，他们可以完成各种各样的工作创造利润。
# 第 i 种工作会产生 profit[i] 的利润，它要求 group[i] 名成员共同参与。
# 工作的任何至少产生 minProfit 利润的子集都被称为盈利计划，并且工作的成员总数最多为 n。
# 有多少种计划可以选择？因为答案很大，所以返回结果模 10^9 + 7 的值。
# 链接：https://leetcode.cn/problems/profitable-schemes/
# 
# 解题思路：
# 这是一个三维费用的01背包问题。
# 1. 物品：每个工作
# 2. 费用1：需要的人数
# 3. 费用2：需要的利润
# 4. dp[i][j][k] 表示前i个工作，使用不超过j个人，至少获得k利润的方案数
# 5. 状态转移方程：
#    dp[i][j][k] = dp[i-1][j][k] + dp[i-1][j-group[i-1]][max(0, k-profit[i-1])]
# 6. 可以使用滚动数组优化空间复杂度
# 7. 对于利润维度，当利润超过minProfit时，统一视为minProfit处理
#
# 时间复杂度：O(len(group) * n * minProfit)
# 空间复杂度：O(n * minProfit)

def profitableSchemes(n, minProfit, group, profit):
    """
    计算盈利计划的数量
    
    解题思路：
    这是一个三维费用的01背包问题。
    1. 物品：每个工作
    2. 费用1：需要的人数
    3. 费用2：需要的利润
    4. dp[i][j][k] 表示前i个工作，使用不超过j个人，至少获得k利润的方案数
    5. 状态转移方程：
       dp[i][j][k] = dp[i-1][j][k] + dp[i-1][j-group[i-1]][max(0, k-profit[i-1])]
    6. 可以使用滚动数组优化空间复杂度
    7. 对于利润维度，当利润超过minProfit时，统一视为minProfit处理
    
    Args:
        n: 员工总数
        minProfit: 最小利润要求
        group: 每个工作需要的人数数组
        profit: 每个工作产生的利润数组
    
    Returns:
        满足条件的计划数量
    """
    MOD = 1000000007
    
    # dp[i][j] 表示使用不超过i个人，至少获得j利润的方案数
    # 这里使用了空间优化的二维DP数组，相当于dp[i][j][k]压缩为dp[j][k]
    dp = [[0 for _ in range(minProfit + 1)] for _ in range(n + 1)]
    
    # 初始化：不选择任何工作，使用0个人，获得0利润的方案数为1
    # 这是动态规划的边界条件
    for i in range(n + 1):
        dp[i][0] = 1
    
    # 遍历每个工作（物品）
    # 这相当于01背包中的物品遍历
    for i in range(len(group)):
        # 获取当前工作需要的人数和产生的利润
        members = group[i]
        earn = profit[i]
        
        # 01背包需要倒序遍历，确保每个物品只使用一次
        # j表示当前可用的人数，k表示当前需要达到的利润
        for j in range(n, members - 1, -1):
            for k in range(minProfit, -1, -1):
                # 状态转移方程：
                # dp[j][k] = 不选择当前工作 + 选择当前工作
                # 不选择当前工作：dp[j][k]（保持原值）
                # 选择当前工作：需要members个人，获得earn利润
                # dp[j-members][max(0, k-earn)]表示使用j-members个人，至少获得max(0, k-earn)利润的方案数
                # 注意：如果k < earn，则至少获得0利润（因为负利润没有意义）
                dp[j][k] = (dp[j][k] + dp[j - members][max(0, k - earn)]) % MOD
    
    # 返回使用不超过n个人，至少获得minProfit利润的方案数
    return dp[n][minProfit]

'''
示例:
输入: n = 5, minProfit = 3, group = [2,2], profit = [2,3]
输出: 2
解释: 至少产生3利润的计划有2种：完成工作1和工作2，或仅完成工作2。

输入: n = 10, minProfit = 5, group = [2,3,5], profit = [6,7,8]
输出: 7
解释: 至少产生5利润的计划有7种。

时间复杂度: O(len(group) * n * minProfit)
  - 外层循环遍历所有工作：O(len(group))
  - 中层循环遍历人数：O(n)
  - 内层循环遍历利润：O(minProfit)
空间复杂度: O(n * minProfit)
  - 二维DP数组的空间消耗
'''
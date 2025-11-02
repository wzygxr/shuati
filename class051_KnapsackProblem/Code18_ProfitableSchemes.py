# LeetCode 879. 盈利计划
# 题目描述：集团里有 n 名员工，他们可以完成各种各样的工作创造利润。
# 第 i 种工作会产生 profit[i] 的利润，它要求 group[i] 名成员共同参与。
# 如果成员参与了其中一项工作，就不能参与另一项工作。
# 工作的任何至少产生 minProfit 利润的子集称为 盈利计划 。
# 并且工作的成员总数最多为 n 。
# 有多少种计划可以选择？因为答案很大，所以返回结果模 10^9 + 7 的值。
# 链接：https://leetcode.cn/problems/profitable-schemes/
# 
# 解题思路：
# 这是一个三维费用的01背包问题。
# 三维分别是：员工人数、利润、可选的工作数量
# 状态定义：dp[i][j][k] 表示考虑前i个工作，使用j个员工，获得至少k的利润的方案数
# 状态转移方程：
# - 不选择第i个工作：dp[i][j][k] = dp[i-1][j][k]
# - 选择第i个工作：dp[i][j][k] += dp[i-1][j-group[i-1]][max(0, k-profit[i-1])]
# 
# 时间复杂度：O(N * minProfit * n)，其中N是工作数量，n是员工人数
# 空间复杂度：O(N * minProfit * n)，可以优化到O(minProfit * n)

MOD = 10**9 + 7

def profitable_schemes(n, min_profit, group, profit):
    """
    计算盈利计划的数量
    
    解题思路：
    这是一个三维费用的01背包问题。
    三维分别是：员工人数、利润、可选的工作数量
    状态定义：dp[i][j][k] 表示考虑前i个工作，使用j个员工，获得至少k的利润的方案数
    状态转移方程：
    - 不选择第i个工作：dp[i][j][k] = dp[i-1][j][k]
    - 选择第i个工作：dp[i][j][k] += dp[i-1][j-group[i-1]][max(0, k-profit[i-1])]
    
    Args:
        n: 员工总数
        min_profit: 最小利润要求
        group: 每个工作所需的员工数
        profit: 每个工作的利润
    
    Returns:
        int: 满足条件的计划数（模10^9+7）
    """
    # 参数验证
    if n <= 0:
        return 1 if min_profit <= 0 else 0
    if not group or not profit or len(group) != len(profit):
        return 1 if min_profit <= 0 else 0
    
    m = len(group)  # 工作数量
    
    # 创建三维DP数组：dp[i][j][k]表示考虑前i个工作，使用j个员工，获得至少k的利润的方案数
    # 这是一个三维费用的01背包问题
    dp = [[[0] * (min_profit + 1) for _ in range(n + 1)] for __ in range(m + 1)]
    
    # 初始状态：没有选择任何工作时，使用0个员工，利润为0的方案数为1
    # 这是动态规划的边界条件
    dp[0][0][0] = 1
    
    # 遍历每个工作（物品）
    for i in range(1, m + 1):
        g = group[i - 1]  # 当前工作所需的员工数（第一个费用）
        p = profit[i - 1]  # 当前工作的利润（第二个费用）
        
        # 遍历员工数（第一个费用维度）
        for j in range(n + 1):
            # 遍历利润要求（第二个费用维度）
            for k in range(min_profit + 1):
                # 不选择当前工作的情况
                # 保持前一个状态的方案数
                dp[i][j][k] = dp[i - 1][j][k]
                
                # 选择当前工作的情况，需要确保员工数足够
                if j >= g:
                    # 计算选择当前工作后所需的最小利润
                    # 如果k < p，则前一个状态需要的利润为0（因为负利润没有意义）
                    prev_profit = max(0, k - p)
                    # 更新方案数，注意取模
                    # dp[i][j][k] = 不选择当前工作的方案数 + 选择当前工作的方案数
                    dp[i][j][k] = (dp[i][j][k] + dp[i - 1][j - g][prev_profit]) % MOD
    
    # 计算所有可能的方案数：员工数不超过n，利润至少为min_profit
    # 遍历所有可能的员工数，累加方案数
    result = 0
    for j in range(n + 1):
        result = (result + dp[m][j][min_profit]) % MOD
    
    return result

def profitable_schemes_optimized(n, min_profit, group, profit):
    """
    空间优化版本：使用二维DP数组
    
    Args:
        n: 员工总数
        min_profit: 最小利润要求
        group: 每个工作所需的员工数
        profit: 每个工作的利润
    
    Returns:
        int: 满足条件的计划数（模10^9+7）
    """
    # 参数验证
    if n <= 0:
        return 1 if min_profit <= 0 else 0
    if not group or not profit or len(group) != len(profit):
        return 1 if min_profit <= 0 else 0
    
    m = len(group)  # 工作数量
    
    # 创建二维DP数组：dp[j][k]表示使用j个员工，获得至少k的利润的方案数
    # 使用滚动数组优化空间复杂度，相当于将dp[i][j][k]压缩为dp[j][k]
    dp = [[0] * (min_profit + 1) for _ in range(n + 1)]
    
    # 初始状态：使用0个员工，利润为0的方案数为1
    dp[0][0] = 1
    
    # 遍历每个工作（物品）
    for i in range(m):
        g = group[i]  # 当前工作所需的员工数
        p = profit[i]  # 当前工作的利润
        
        # 逆序遍历员工数，避免重复计算
        # 倒序遍历确保每个物品只使用一次
        for j in range(n, g - 1, -1):
            # 逆序遍历利润要求
            for k in range(min_profit, -1, -1):
                # 计算选择当前工作后所需的最小利润
                prev_profit = max(0, k - p)
                # 更新方案数，注意取模
                dp[j][k] = (dp[j][k] + dp[j - g][prev_profit]) % MOD
    
    # 计算所有可能的方案数
    result = 0
    for j in range(n + 1):
        result = (result + dp[j][min_profit]) % MOD
    
    return result

def profitable_schemes_further_optimized(n, min_profit, group, profit):
    """
    进一步优化：针对利润维度进行优化，当利润超过min_profit时，可以将其视为等于min_profit
    
    Args:
        n: 员工总数
        min_profit: 最小利润要求
        group: 每个工作所需的员工数
        profit: 每个工作的利润
    
    Returns:
        int: 满足条件的计划数（模10^9+7）
    """
    # 参数验证
    if n <= 0:
        return 1 if min_profit <= 0 else 0
    if not group or not profit or len(group) != len(profit):
        return 1 if min_profit <= 0 else 0
    
    # 创建二维DP数组
    dp = [[0] * (min_profit + 1) for _ in range(n + 1)]
    dp[0][0] = 1
    
    # 遍历每个工作
    for i in range(len(group)):
        g = group[i]
        p = profit[i]
        
        # 剪枝：如果工作所需员工数超过总员工数，跳过
        # 因为当前工作本身就无法完成
        if g > n:
            continue
        
        # 逆序遍历员工数
        for j in range(n, g - 1, -1):
            # 计算当前工作的实际利润贡献（不超过min_profit）
            # 当利润超过min_profit时，可以将其视为等于min_profit
            actual_profit = min(p, min_profit)
            
            # 遍历利润要求
            for k in range(min_profit, -1, -1):
                # 计算选择当前工作后所需的最小利润
                prev_profit = max(0, k - actual_profit)
                # 更新方案数，注意取模
                dp[j][k] = (dp[j][k] + dp[j - g][prev_profit]) % MOD
    
    # 计算所有可能的方案数
    result = 0
    for j in range(n + 1):
        result = (result + dp[j][min_profit]) % MOD
    
    return result

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    n1, min_profit1 = 5, 3
    group1 = [2, 2]
    profit1 = [2, 3]
    print(f"测试用例1结果: {profitable_schemes(n1, min_profit1, group1, profit1)}")  # 预期输出: 2
    
    # 测试用例2
    n2, min_profit2 = 10, 5
    group2 = [2, 3, 5]
    profit2 = [6, 7, 8]
    print(f"测试用例2结果: {profitable_schemes(n2, min_profit2, group2, profit2)}")  # 预期输出: 7

'''
示例:
输入: n = 5, minProfit = 3, group = [2,2], profit = [2,3]
输出: 2
解释: 至少产生3利润的计划有2种：完成工作1和工作2，或仅完成工作2。

输入: n = 10, minProfit = 5, group = [2,3,5], profit = [6,7,8]
输出: 7
解释: 至少产生5利润的计划有7种。

时间复杂度: O(N * minProfit * n)
  - 外层循环遍历所有工作：O(N)
  - 中层循环遍历员工数：O(n)
  - 内层循环遍历利润：O(minProfit)
空间复杂度: O(minProfit * n)
  - 二维DP数组的空间消耗
'''
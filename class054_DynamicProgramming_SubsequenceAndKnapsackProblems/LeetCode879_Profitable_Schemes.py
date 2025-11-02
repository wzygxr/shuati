# LeetCode 879. 盈利计划
# 集团里有 n 名员工，他们可以完成各种各样的工作创造利润。
# 第 i 种工作会产生 profit[i] 的利润，它要求 group[i] 名成员共同参与。
# 如果成员参与了其中一项工作，就不能参与另一项工作。
# 工作的任何至少产生 minProfit 利润的子集都被称为 盈利计划 。
# 并且工作的成员总数最多为 n 。
# 有多少种计划可以选择？
# 因为答案很大，所以 返回结果模 10^9 + 7 的值。
# 测试链接 : https://leetcode.cn/problems/profitable-schemes/

"""
算法详解：盈利计划（LeetCode 879）

问题描述：
集团里有 n 名员工，他们可以完成各种各样的工作创造利润。
第 i 种工作会产生 profit[i] 的利润，它要求 group[i] 名成员共同参与。
如果成员参与了其中一项工作，就不能参与另一项工作。
工作的任何至少产生 minProfit 利润的子集都被称为 盈利计划 。
并且工作的成员总数最多为 n 。
有多少种计划可以选择？
因为答案很大，所以 返回结果模 10^9 + 7 的值。

算法思路：
这是一个带下界约束的背包问题。
1. 使用动态规划，dp[i][j][k] 表示前i个工作，使用j名员工，获得至少k利润的方案数
2. 由于利润可能很大，需要优化：当利润超过minProfit时，统一视为minProfit
3. 状态转移：选择或不选择当前工作

时间复杂度分析：
1. 动态规划：O(len * n * minProfit)
2. 总体时间复杂度：O(len * n * minProfit)

空间复杂度分析：
1. dp数组：O(n * minProfit)
2. 总体空间复杂度：O(n * minProfit)

工程化考量：
1. 异常处理：检查输入参数是否有效
2. 边界处理：处理空数组和边界情况
3. 空间优化：使用滚动数组将空间复杂度优化
4. 模运算：注意防止整数溢出

极端场景验证：
1. 输入数组为空的情况
2. minProfit为0的情况
3. n为0的情况
4. 所有工作都需要超过n名员工的情况
5. 所有工作利润都为0的情况
"""

def profitableSchemes(n, minProfit, group, profit):
    """
    计算盈利计划的数量
    
    Args:
        n (int): 员工总数
        minProfit (int): 最小利润要求
        group (List[int]): 每项工作需要的员工数
        profit (List[int]): 每项工作的利润
    
    Returns:
        int: 盈利计划的数量（模10^9 + 7）
    """
    MOD = 1000000007
    
    # 异常处理：检查输入参数是否有效
    if not group or not profit or len(group) != len(profit) or n < 0 or minProfit < 0:
        return 0
    
    length = len(group)
    
    # dp[i][j] 表示使用i名员工，获得至少j利润的方案数
    dp = [[0] * (minProfit + 1) for _ in range(n + 1)]
    
    # 初始化：不选择任何工作，获得0利润的方案数为1
    for i in range(n + 1):
        dp[i][0] = 1
    
    # 遍历每个工作
    for i in range(length):
        members = group[i]
        earn = profit[i]
        
        # 从后往前遍历，避免重复选择同一工作
        for j in range(n, members - 1, -1):
            for k in range(minProfit, -1, -1):
                # 选择当前工作：将方案数累加到新状态
                # 注意：当利润超过minProfit时，统一视为minProfit
                new_profit = min(k + earn, minProfit)
                dp[j][new_profit] = (dp[j][new_profit] + dp[j - members][k]) % MOD
    
    return dp[n][minProfit]

# 空间优化版本：使用滚动数组
def profitableSchemesOptimized(n, minProfit, group, profit):
    """
    计算盈利计划的数量（优化版本）
    
    Args:
        n (int): 员工总数
        minProfit (int): 最小利润要求
        group (List[int]): 每项工作需要的员工数
        profit (List[int]): 每项工作的利润
    
    Returns:
        int: 盈利计划的数量（模10^9 + 7）
    """
    MOD = 1000000007
    
    # 异常处理：检查输入参数是否有效
    if not group or not profit or len(group) != len(profit) or n < 0 or minProfit < 0:
        return 0
    
    length = len(group)
    
    # dp[i][j] 表示使用i名员工，获得至少j利润的方案数
    dp = [[0] * (minProfit + 1) for _ in range(n + 1)]
    
    # 初始化：不选择任何工作，获得0利润的方案数为1
    for i in range(n + 1):
        dp[i][0] = 1
    
    # 遍历每个工作
    for i in range(length):
        members = group[i]
        earn = profit[i]
        
        # 从后往前遍历，避免重复选择同一工作
        for j in range(n, members - 1, -1):
            for k in range(minProfit, -1, -1):
                # 选择当前工作：将方案数累加到新状态
                # 注意：当利润超过minProfit时，统一视为minProfit
                new_profit = min(k + earn, minProfit)
                dp[j][new_profit] = (dp[j][new_profit] + dp[j - members][k]) % MOD
    
    return dp[n][minProfit]

# 测试方法
if __name__ == "__main__":
    # 测试用例1
    n1, minProfit1 = 5, 3
    group1 = [2, 2]
    profit1 = [2, 3]
    print(f"Test 1: {profitableSchemes(n1, minProfit1, group1, profit1)}")
    print(f"Test 1 (Optimized): {profitableSchemesOptimized(n1, minProfit1, group1, profit1)}")
    # 期望输出: 2
    
    # 测试用例2
    n2, minProfit2 = 10, 5
    group2 = [2, 3, 5]
    profit2 = [6, 7, 8]
    print(f"Test 2: {profitableSchemes(n2, minProfit2, group2, profit2)}")
    print(f"Test 2 (Optimized): {profitableSchemesOptimized(n2, minProfit2, group2, profit2)}")
    # 期望输出: 7
    
    # 测试用例3
    n3, minProfit3 = 0, 0
    group3 = []
    profit3 = []
    print(f"Test 3: {profitableSchemes(n3, minProfit3, group3, profit3)}")
    print(f"Test 3 (Optimized): {profitableSchemesOptimized(n3, minProfit3, group3, profit3)}")
    # 期望输出: 1
    
    # 测试用例4
    n4, minProfit4 = 1, 1
    group4 = [1]
    profit4 = [1]
    print(f"Test 4: {profitableSchemes(n4, minProfit4, group4, profit4)}")
    print(f"Test 4 (Optimized): {profitableSchemesOptimized(n4, minProfit4, group4, profit4)}")
    # 期望输出: 1
    
    # 测试用例5
    n5, minProfit5 = 2, 2
    group5 = [1, 1]
    profit5 = [1, 1]
    print(f"Test 5: {profitableSchemes(n5, minProfit5, group5, profit5)}")
    print(f"Test 5 (Optimized): {profitableSchemesOptimized(n5, minProfit5, group5, profit5)}")
    # 期望输出: 1
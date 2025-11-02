# POJ 1276 Cash Machine
# 题目描述：有一个取款机，可以提供不同面额的钞票，每种面额有指定数量的钞票。
# 给定要取款的金额cash，求能取出的不超过cash的最大金额。
# 链接：http://poj.org/problem?id=1276
# 
# 解题思路：
# 这是一个多重背包问题，可以转化为01背包问题求解。
# 1. 多重背包：每种物品（钞票面额）有指定数量
# 2. 转化方法：二进制优化
#    将数量为n的物品分解为若干个物品，数量分别为1, 2, 4, ..., 2^(k-1), n-2^k+1
#    这样可以保证用这些物品组合出1到n之间的任意数量
# 3. 转化后用01背包求解
# 4. dp[i][j] 表示前i种钞票面额，能组成的最大不超过j的金额
# 5. 状态转移方程：
#    dp[i][j] = max(dp[i-1][j], dp[i-1][j-value[k]] + value[k])
#
# 时间复杂度：O(N * cash * log(max_count))
# 空间复杂度：O(cash)

def maxCash(cash, N, counts, values):
    """
    计算能取出的最大金额
    
    解题思路：
    这是一个多重背包问题，可以转化为01背包问题求解。
    1. 多重背包：每种物品（钞票面额）有指定数量
    2. 转化方法：二进制优化
       将数量为n的物品分解为若干个物品，数量分别为1, 2, 4, ..., 2^(k-1), n-2^k+1
       这样可以保证用这些物品组合出1到n之间的任意数量
    3. 转化后用01背包求解
    4. dp[i][j] 表示前i种钞票面额，能组成的最大不超过j的金额
    5. 状态转移方程：
       dp[i][j] = max(dp[i-1][j], dp[i-1][j-value[k]] + value[k])
    
    Args:
        cash: 要取款的金额
        N: 钞票面额种类数
        counts: 每种面额的钞票数量数组
        values: 钞票面额数组
    
    Returns:
        能取出的最大金额
    """
    # dp[j] 表示能组成的最大不超过j的金额
    # 这里使用了空间优化的一维DP数组
    dp = [0] * (cash + 1)
    
    # 遍历每种钞票面额（物品种类）
    for i in range(N):
        # 获取当前面额的钞票数量和面额值
        count = counts[i]
        value = values[i]
        
        # 二进制优化：将count个value面额的钞票分解
        # 将数量为count的物品分解为若干个物品，数量分别为1, 2, 4, ..., 2^(k-1), remaining
        # 这样可以保证用这些物品组合出1到count之间的任意数量
        k = 1
        while k <= count:
            # 01背包：倒序遍历
            # j表示当前的取款金额
            for j in range(cash, k * value - 1, -1):
                # 状态转移方程：
                # dp[j] = max(不选择当前组合的钞票, 选择当前组合的钞票)
                # 不选择当前组合的钞票：dp[j]（保持原值）
                # 选择当前组合的钞票：dp[j - k * value] + k * value（前一个状态+当前组合钞票的总面额）
                dp[j] = max(dp[j], dp[j - k * value] + k * value)
            # 减去已经处理的数量
            count -= k
            # 下一个二进制位
            k <<= 1
        
        # 处理剩余的钞票
        if count > 0:
            # 01背包：倒序遍历
            # j表示当前的取款金额
            for j in range(cash, count * value - 1, -1):
                # 状态转移方程：
                # dp[j] = max(不选择剩余钞票, 选择剩余钞票)
                # 不选择剩余钞票：dp[j]（保持原值）
                # 选择剩余钞票：dp[j - count * value] + count * value（前一个状态+剩余钞票的总面额）
                dp[j] = max(dp[j], dp[j - count * value] + count * value)
    
    # 返回能取出的最大金额
    return dp[cash]

'''
示例:
输入: cash = 735, N = 3
counts = [4, 6, 3]
values = [125, 5, 350]
输出: 735
解释: 可以恰好取出735元

输入: cash = 633, N = 4
counts = [500, 6, 1, 0]
values = [30, 100, 5, 1]
输出: 630
解释: 最多能取出630元

时间复杂度: O(N * cash * log(max_count))
  - 外层循环遍历所有钞票面额：O(N)
  - 中层循环二进制分解：O(log(max_count))
  - 内层循环遍历取款金额：O(cash)
空间复杂度: O(cash)
  - 一维DP数组的空间消耗
'''
# SPOJ MIXTURES
# 题目来源：https://www.spoj.com/problems/MIXTURES/
# 题目大意：有n个混合物排成一排，每个混合物有一个颜色值(0-99)。
# 每次可以合并相邻的两个混合物，合并后的新混合物颜色值为两个混合物颜色值之和对100取模。
# 合并的代价为两个混合物颜色值的乘积。
# 求合并所有混合物的最小代价。
#
# 解题思路：
# 1. 这是另一个经典的区间动态规划问题，类似于石子合并
# 2. dp[i][j]表示合并区间[i,j]内所有混合物的最小代价
# 3. 状态转移：枚举分割点k，dp[i][j] = min(dp[i][k] + dp[k+1][j] + cost(i,k,j))
# 4. 需要预处理前缀和数组来快速计算区间和，以及颜色值
#
# 时间复杂度：O(n^3) - 三层循环：区间长度、区间起点、分割点
# 空间复杂度：O(n^2) - dp数组占用空间
#
# 工程化考虑：
# 1. 输入验证：检查输入是否合法
# 2. 模运算处理：正确处理颜色值的模运算
# 3. 边界处理：处理混合物数量较少的特殊情况
# 4. 异常处理：对于不合法输入给出适当提示

import sys

def solve(colors, n):
    """
    主函数：解决混合物合并问题
    时间复杂度: O(n^3) - 三层循环：区间长度、区间起点、分割点
    空间复杂度: O(n^2) - dp数组占用空间
    """
    if n <= 1:
        return 0
    
    # 计算前缀和数组，用于快速计算区间和
    prefix_sum = [0] * (n + 1)
    for i in range(n):
        prefix_sum[i + 1] = prefix_sum[i] + colors[i]
    
    # dp[i][j]表示合并区间[i,j]内所有混合物的最小代价
    dp = [[0] * n for _ in range(n)]
    # color[i][j]表示合并区间[i,j]内所有混合物后的颜色值
    color = [[0] * n for _ in range(n)]
    
    # 初始化：单个混合物的颜色值
    for i in range(n):
        color[i][i] = colors[i]
    
    # 枚举区间长度，从2开始（至少需要2个混合物才能合并）
    for length in range(2, n + 1):
        # 枚举区间起点i
        for i in range(n - length + 1):
            # 计算区间终点j
            j = i + length - 1
            dp[i][j] = float('inf')
            
            # 枚举分割点k
            for k in range(i, j):
                # 计算合并代价
                cost = dp[i][k] + dp[k + 1][j] + color[i][k] * color[k + 1][j]
                
                if cost < dp[i][j]:
                    dp[i][j] = cost
                    # 计算合并后的颜色值
                    color[i][j] = (color[i][k] + color[k + 1][j]) % 100
    
    return dp[0][n - 1]

if __name__ == "__main__":
    # 读取输入
    try:
        while True:
            line = input().strip()
            if not line:
                break
            n = int(line)
            colors = list(map(int, input().split()))
            
            result = solve(colors, n)
            print(result)
    except EOFError:
        pass
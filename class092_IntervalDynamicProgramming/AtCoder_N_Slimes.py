# AtCoder Educational DP Contest N - Slimes
# 题目来源：https://atcoder.jp/contests/dp/tasks/dp_n
# 题目大意：有n个史莱姆排成一排，每个史莱姆有一个大小。每次可以选择相邻的两个史莱姆合并，
# 合并的代价是两个史莱姆大小之和。合并后会得到一个新史莱姆，其大小也是两个史莱姆大小之和。
# 求合并所有史莱姆的最小代价。
#
# 解题思路：
# 1. 这是经典的石子合并问题，使用区间动态规划解决
# 2. dp[i][j]表示合并区间[i,j]内所有史莱姆的最小代价
# 3. 状态转移：枚举分割点k，dp[i][j] = min(dp[i][k] + dp[k+1][j]) + sum[i][j]
# 4. 需要预处理前缀和数组来快速计算区间和
#
# 时间复杂度：O(n^3) - 三层循环：区间长度、区间起点、分割点
# 空间复杂度：O(n^2) - dp数组占用空间
#
# 工程化考虑：
# 1. 输入验证：检查输入是否合法
# 2. 前缀和优化：使用前缀和快速计算区间和
# 3. 边界处理：处理史莱姆数量较少的特殊情况
# 4. 异常处理：对于不合法输入给出适当提示

import sys

def solve(slimes, n):
    """
    主函数：解决史莱姆合并问题
    时间复杂度: O(n^3) - 三层循环：区间长度、区间起点、分割点
    空间复杂度: O(n^2) - dp数组占用空间
    """
    if n <= 1:
        return 0
    
    # 计算前缀和数组
    prefix_sum = [0] * (n + 1)
    for i in range(n):
        prefix_sum[i + 1] = prefix_sum[i] + slimes[i]
    
    # dp[i][j]表示合并区间[i,j]内所有史莱姆的最小代价
    dp = [[0] * n for _ in range(n)]
    
    # 枚举区间长度，从2开始（至少需要2个史莱姆才能合并）
    for length in range(2, n + 1):
        # 枚举区间起点i
        for i in range(n - length + 1):
            # 计算区间终点j
            j = i + length - 1
            dp[i][j] = float('inf')
            
            # 枚举分割点k
            for k in range(i, j):
                # 计算区间[i,j]的史莱姆大小总和
                sum_val = prefix_sum[j + 1] - prefix_sum[i]
                
                # 状态转移方程
                # dp[i][k]：合并左半部分的最小代价
                # dp[k+1][j]：合并右半部分的最小代价
                # sum_val：当前合并的代价
                dp[i][j] = min(dp[i][j], dp[i][k] + dp[k + 1][j] + sum_val)
    
    return dp[0][n - 1]

if __name__ == "__main__":
    # 读取输入
    n = int(input().strip())
    slimes = list(map(int, input().split()))
    
    result = solve(slimes, n)
    print(result)
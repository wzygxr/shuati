# Aizu OJ ALDS1_10_B Matrix Chain Multiplication
# 题目来源：https://onlinejudge.u-aizu.ac.jp/problems/ALDS1_10_B
# 题目大意：给定n个矩阵的维度，矩阵Ai的维度为di-1 × di。
# 矩阵乘法满足结合律，不同的加括号方式会导致不同的计算代价。
# 矩阵乘法的代价定义为标量乘法的次数。
# 求计算矩阵链乘积的最小标量乘法次数。
#
# 解题思路：
# 1. 这是经典的矩阵链乘法问题，使用区间动态规划解决
# 2. dp[i][j]表示计算矩阵Ai到Aj的最小标量乘法次数
# 3. 状态转移：枚举分割点k，dp[i][j] = min(dp[i][k] + dp[k+1][j] + d[i-1]*d[k]*d[j])
#
# 时间复杂度：O(n^3) - 三层循环：区间长度、区间起点、分割点
# 空间复杂度：O(n^2) - dp数组占用空间
#
# 工程化考虑：
# 1. 输入验证：检查输入是否合法
# 2. 边界处理：处理矩阵数量较少的特殊情况
# 3. 索引处理：正确处理矩阵维度数组的索引
# 4. 异常处理：对于不合法输入给出适当提示

import sys

def solve(dimensions, n):
    """
    主函数：解决矩阵链乘法问题
    时间复杂度: O(n^3) - 三层循环：区间长度、区间起点、分割点
    空间复杂度: O(n^2) - dp数组占用空间
    """
    if n <= 1:
        return 0
    
    # dp[i][j]表示计算矩阵Ai到Aj的最小标量乘法次数
    dp = [[0] * n for _ in range(n)]
    
    # 枚举区间长度，从2开始（至少需要2个矩阵才能相乘）
    for length in range(2, n + 1):
        # 枚举区间起点i
        for i in range(n - length + 1):
            # 计算区间终点j
            j = i + length - 1
            dp[i][j] = float('inf')
            
            # 枚举分割点k
            for k in range(i, j):
                # 计算标量乘法次数
                # dimensions[i]：矩阵Ai的行数
                # dimensions[k+1]：矩阵Ak的列数，也是矩阵Ak+1的行数
                # dimensions[j+1]：矩阵Aj的列数
                cost = dp[i][k] + dp[k + 1][j] + dimensions[i] * dimensions[k + 1] * dimensions[j + 1]
                
                dp[i][j] = min(dp[i][j], cost)
    
    return dp[0][n - 1]

if __name__ == "__main__":
    # 读取输入
    n = int(input().strip())
    dimensions = []
    
    for i in range(n):
        parts = input().split()
        if i == 0:
            dimensions.append(int(parts[0]))
        dimensions.append(int(parts[1]))
    
    result = solve(dimensions, n)
    print(result)
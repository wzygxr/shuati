"""
矩阵中和能被 K 整除的路径 (Paths in Matrix Whose Sum is Divisible by K) - 路径计数动态规划 - Python实现

题目描述：
给一个下标从0开始的 n * m 整数矩阵 grid 和一个整数 k。
从起点(0,0)出发，每步只能往下或者往右，你想要到达终点(m-1, n-1)。
请你返回路径和能被 k 整除的路径数目，答案对 1000000007 取模。

题目来源：LeetCode 2435. 矩阵中和能被 K 整除的路径
测试链接：https://leetcode.cn/problems/paths-in-matrix-whose-sum-is-divisible-by-k/

解题思路：
这是一个路径计数动态规划问题，需要在网格中统计满足特定条件（路径和能被K整除）的路径数量。
由于路径数量可能很大，需要对结果取模。

算法实现：
1. 动态规划：使用三维DP表存储状态（位置+余数）
2. 空间优化：使用二维数组滚动更新

时间复杂度分析：
- 动态规划：O(n * m * k)，需要填充三维DP表
- 空间优化：O(n * m * k)，时间复杂度相同但空间更优

空间复杂度分析：
- 动态规划：O(n * m * k)，三维DP表
- 空间优化：O(m * k)，二维DP表
"""

MOD = 10**9 + 7

from typing import List

def number_of_paths1(grid: List[List[int]], k: int) -> int:
    """
    动态规划解法
    
    Args:
        grid: 整数矩阵
        k: 除数
        
    Returns:
        int: 路径数目
    """
    n = len(grid)
    m = len(grid[0])
    
    # dp[i][j][r] 表示到达(i,j)时路径和模k余r的路径数
    dp = [[[0] * k for _ in range(m)] for _ in range(n)]
    
    # 初始化起点
    dp[0][0][grid[0][0] % k] = 1
    
    for i in range(n):
        for j in range(m):
            for r in range(k):
                if dp[i][j][r] == 0:
                    continue
                
                # 向右移动
                if j + 1 < m:
                    new_r = (r + grid[i][j + 1]) % k
                    dp[i][j + 1][new_r] = (dp[i][j + 1][new_r] + dp[i][j][r]) % MOD
                
                # 向下移动
                if i + 1 < n:
                    new_r = (r + grid[i + 1][j]) % k
                    dp[i + 1][j][new_r] = (dp[i + 1][j][new_r] + dp[i][j][r]) % MOD
    
    return dp[n - 1][m - 1][0]

def number_of_paths2(grid: List[List[int]], k: int) -> int:
    """
    空间优化的动态规划解法
    
    Args:
        grid: 整数矩阵
        k: 除数
        
    Returns:
        int: 路径数目
    """
    n = len(grid)
    m = len(grid[0])
    
    # dp[j][r] 表示当前行到达第j列时路径和模k余r的路径数
    dp = [[0] * k for _ in range(m)]
    
    # 初始化起点
    dp[0][grid[0][0] % k] = 1
    
    for i in range(n):
        next_dp = [[0] * k for _ in range(m)]
        
        for j in range(m):
            for r in range(k):
                if dp[j][r] == 0:
                    continue
                
                # 向右移动
                if j + 1 < m:
                    new_r = (r + grid[i][j + 1]) % k
                    next_dp[j + 1][new_r] = (next_dp[j + 1][new_r] + dp[j][r]) % MOD
                
                # 向下移动
                if i + 1 < n:
                    new_r = (r + grid[i + 1][j]) % k
                    dp[j][new_r] = (dp[j][new_r] + dp[j][r]) % MOD
        
        # 更新dp数组（下一行）
        if i < n - 1:
            for j in range(m):
                for r in range(k):
                    dp[j][r] = next_dp[j][r]
    
    return dp[m - 1][0]

# 测试函数
if __name__ == "__main__":
    # 测试用例1
    grid1 = [[5,2,4],[3,0,5],[0,7,2]]
    k1 = 3
    print("测试用例1:")
    print("网格:", grid1)
    print("k =", k1)
    print("方法1结果:", number_of_paths1(grid1, k1))
    print("方法2结果:", number_of_paths2(grid1, k1))
    print()
    
    # 测试用例2
    grid2 = [[0,0]]
    k2 = 5
    print("测试用例2:")
    print("网格:", grid2)
    print("k =", k2)
    print("方法1结果:", number_of_paths1(grid2, k2))
    print("方法2结果:", number_of_paths2(grid2, k2))
    print()
    
    # 测试用例3
    grid3 = [[7,3,4,9],[2,3,6,2],[2,3,7,0]]
    k3 = 1
    print("测试用例3:")
    print("网格:", grid3)
    print("k =", k3)
    print("方法1结果:", number_of_paths1(grid3, k3))
    print("方法2结果:", number_of_paths2(grid3, k3))
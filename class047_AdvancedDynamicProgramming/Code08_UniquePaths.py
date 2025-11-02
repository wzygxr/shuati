"""
不同路径 (Unique Paths) - Python实现

题目描述：
一个机器人位于一个 m x n 网格的左上角。
机器人每次只能向下或者向右移动一步。
机器人试图达到网格的右下角。
问总共有多少条不同的路径？

解题思路：
这是一道经典的动态规划问题。
我们可以使用dp[i][j]表示从起点到位置(i,j)的不同路径数。
状态转移方程：
dp[i][j] = dp[i-1][j] + dp[i][j-1]
其中dp[i-1][j]表示从上方到达(i,j)的路径数
dp[i][j-1]表示从左方到达(i,j)的路径数

边界条件：
dp[0][j] = 1 (第一行只能从左方到达)
dp[i][0] = 1 (第一列只能从上方到达)

时间复杂度：O(m*n)
空间复杂度：O(m*n) 或 O(min(m,n)) (空间优化版本)
"""

def uniquePaths1(m, n):
    """
    动态规划解法
    
    Args:
        m: 网格行数
        n: 网格列数
    
    Returns:
        不同路径数
    """
    # dp[i][j] 表示从起点到位置(i,j)的不同路径数
    dp = [[0] * n for _ in range(m)]
    
    # 初始化边界条件
    # 第一行只能从左方到达
    for j in range(n):
        dp[0][j] = 1
    # 第一列只能从上方到达
    for i in range(m):
        dp[i][0] = 1
    
    # 状态转移
    for i in range(1, m):
        for j in range(1, n):
            dp[i][j] = dp[i - 1][j] + dp[i][j - 1]
    
    return dp[m - 1][n - 1]


def uniquePaths2(m, n):
    """
    空间优化的动态规划解法
    
    Args:
        m: 网格行数
        n: 网格列数
    
    Returns:
        不同路径数
    """
    # 确保使用较小的维度作为数组长度，进一步优化空间
    if m < n:
        m, n = n, m
    
    # 只需要保存一行的状态
    dp = [1] * n
    
    # 状态转移
    for i in range(1, m):
        for j in range(1, n):
            dp[j] = dp[j] + dp[j - 1]
    
    return dp[n - 1]


def uniquePaths3(m, n):
    """
    数学组合解法
    
    总共需要走 (m-1) + (n-1) = m+n-2 步
    其中需要向下走 m-1 步，向右走 n-1 步
    所以答案是 C(m+n-2, m-1) 或 C(m+n-2, n-1)
    
    Args:
        m: 网格行数
        n: 网格列数
    
    Returns:
        不同路径数
    """
    # 计算 C(m+n-2, min(m-1, n-1))
    total_steps = m + n - 2
    k = min(m - 1, n - 1)
    
    result = 1
    for i in range(1, k + 1):
        result = result * (total_steps - k + i) // i
    
    return result


def uniquePaths4(m, n):
    """
    记忆化搜索解法
    
    Args:
        m: 网格行数
        n: 网格列数
    
    Returns:
        不同路径数
    """
    # 记忆化字典
    memo = {}
    
    def dfs(i, j):
        # 边界条件
        if i == 0 or j == 0:
            return 1
        
        # 检查是否已经计算过
        if (i, j) in memo:
            return memo[(i, j)]
        
        # 从上方和左方到达
        ans = dfs(i - 1, j) + dfs(i, j - 1)
        
        # 记忆化存储
        memo[(i, j)] = ans
        return ans
    
    return dfs(m - 1, n - 1)


# 测试方法
if __name__ == "__main__":
    # 测试用例1
    m1, n1 = 3, 7
    print("测试用例1:")
    print("网格大小: {} x {}".format(m1, n1))
    print("方法1结果:", uniquePaths1(m1, n1))
    print("方法2结果:", uniquePaths2(m1, n1))
    print("方法3结果:", uniquePaths3(m1, n1))
    print("方法4结果:", uniquePaths4(m1, n1))
    print()
    
    # 测试用例2
    m2, n2 = 3, 2
    print("测试用例2:")
    print("网格大小: {} x {}".format(m2, n2))
    print("方法1结果:", uniquePaths1(m2, n2))
    print("方法2结果:", uniquePaths2(m2, n2))
    print("方法3结果:", uniquePaths3(m2, n2))
    print("方法4结果:", uniquePaths4(m2, n2))
    print()
    
    # 测试用例3
    m3, n3 = 7, 3
    print("测试用例3:")
    print("网格大小: {} x {}".format(m3, n3))
    print("方法1结果:", uniquePaths1(m3, n3))
    print("方法2结果:", uniquePaths2(m3, n3))
    print("方法3结果:", uniquePaths3(m3, n3))
    print("方法4结果:", uniquePaths4(m3, n3))
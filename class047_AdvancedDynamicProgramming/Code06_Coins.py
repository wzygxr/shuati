"""
Coins (概率DP) - Python实现

题目描述：
有N枚硬币，第i枚硬币抛出后正面朝上的概率是p[i]。
现在将这N枚硬币都抛一次，求正面朝上的硬币数比反面朝上的硬币数多的概率。

解题思路：
这是一道典型的概率动态规划问题。
我们可以使用dp[i][j]表示前i枚硬币中，有j枚正面朝上的概率。
状态转移方程：
dp[i][j] = dp[i-1][j] * (1-p[i]) + dp[i-1][j-1] * p[i]
其中dp[i-1][j] * (1-p[i])表示第i枚硬币反面朝上，之前有j枚正面朝上的概率
dp[i-1][j-1] * p[i]表示第i枚硬币正面朝上，之前有j-1枚正面朝上的概率

由于要求正面朝上的硬币数比反面朝上的硬币数多，即正面朝上的硬币数 > N/2
所以我们需要计算dp[N][N/2+1] + dp[N][N/2+2] + ... + dp[N][N]

时间复杂度：O(N^2)
空间复杂度：O(N^2)
"""

def probabilityOfHeads1(p):
    """
    动态规划解法
    
    Args:
        p: 硬币正面朝上的概率数组
    
    Returns:
        正面朝上的硬币数比反面朝上的硬币数多的概率
    """
    n = len(p)
    # dp[i][j] 表示前i枚硬币中，有j枚正面朝上的概率
    dp = [[0.0] * (n + 1) for _ in range(n + 1)]
    
    # 初始状态：0枚硬币，0枚正面朝上概率为1
    dp[0][0] = 1.0
    
    # 状态转移
    for i in range(1, n + 1):
        # 0枚正面朝上只能是当前硬币也是反面朝上
        dp[i][0] = dp[i - 1][0] * (1 - p[i - 1])
        
        for j in range(1, i + 1):
            # 第i枚硬币反面朝上 + 第i枚硬币正面朝上
            dp[i][j] = dp[i - 1][j] * (1 - p[i - 1]) + dp[i - 1][j - 1] * p[i - 1]
    
    # 计算正面朝上的硬币数比反面朝上的硬币数多的概率
    # 即正面朝上的硬币数 > n/2
    result = 0.0
    for j in range(n // 2 + 1, n + 1):
        result += dp[n][j]
    
    return result


def probabilityOfHeads2(p):
    """
    空间优化的动态规划解法
    
    Args:
        p: 硬币正面朝上的概率数组
    
    Returns:
        正面朝上的硬币数比反面朝上的硬币数多的概率
    """
    n = len(p)
    # 只需要保存前一层的状态
    dp = [0.0] * (n + 1)
    dp[0] = 1.0
    
    # 状态转移
    for i in range(1, n + 1):
        # 从后往前更新，避免重复使用更新后的值
        for j in range(i, 0, -1):
            dp[j] = dp[j] * (1 - p[i - 1]) + dp[j - 1] * p[i - 1]
        # 更新dp[0]
        dp[0] = dp[0] * (1 - p[i - 1])
    
    # 计算正面朝上的硬币数比反面朝上的硬币数多的概率
    result = 0.0
    for j in range(n // 2 + 1, n + 1):
        result += dp[j]
    
    return result


def probabilityOfHeads3(p):
    """
    记忆化搜索解法
    
    Args:
        p: 硬币正面朝上的概率数组
    
    Returns:
        正面朝上的硬币数比反面朝上的硬币数多的概率
    """
    n = len(p)
    # 记忆化字典
    memo = {}
    
    def dfs(i, j):
        # 边界条件
        if j < 0 or j > i:
            return 0.0
        
        if i == 0:
            return 1.0 if j == 0 else 0.0
        
        # 检查是否已经计算过
        if (i, j) in memo:
            return memo[(i, j)]
        
        # 第i枚硬币反面朝上 + 第i枚硬币正面朝上
        ans = dfs(i - 1, j) * (1 - p[i - 1]) + dfs(i - 1, j - 1) * p[i - 1]
        
        # 记忆化存储
        memo[(i, j)] = ans
        return ans
    
    # 计算正面朝上的硬币数比反面朝上的硬币数多的概率
    result = 0.0
    for j in range(n // 2 + 1, n + 1):
        result += dfs(n, j)
    
    return result


# 测试方法
if __name__ == "__main__":
    # 测试用例1
    p1 = [0.3, 0.6, 0.8]
    print("测试用例1:")
    print("硬币正面朝上概率: [0.3, 0.6, 0.8]")
    print("方法1结果:", probabilityOfHeads1(p1))
    print("方法2结果:", probabilityOfHeads2(p1))
    print("方法3结果:", probabilityOfHeads3(p1))
    print()
    
    # 测试用例2
    p2 = [0.5]
    print("测试用例2:")
    print("硬币正面朝上概率: [0.5]")
    print("方法1结果:", probabilityOfHeads1(p2))
    print("方法2结果:", probabilityOfHeads2(p2))
    print("方法3结果:", probabilityOfHeads3(p2))
    print()
    
    # 测试用例3
    p3 = [0.42, 0.01, 0.42, 0.99, 0.42]
    print("测试用例3:")
    print("硬币正面朝上概率: [0.42, 0.01, 0.42, 0.99, 0.42]")
    print("方法1结果:", probabilityOfHeads1(p3))
    print("方法2结果:", probabilityOfHeads2(p3))
    print("方法3结果:", probabilityOfHeads3(p3))
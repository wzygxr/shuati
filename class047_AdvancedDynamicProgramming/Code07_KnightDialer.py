"""
骑士拨号器 (Knight Dialer) - Python实现

题目描述：
象棋骑士有一个独特的移动方式，它可以垂直移动两个方格，水平移动一个方格，
或者水平移动两个方格，垂直移动一个方格(两者都形成一个 L 的形状)。
我们有一个象棋骑士和一个电话垫，如下所示，骑士只能站在一个数字单元格上。
给定一个整数 n，返回我们可以拨多少个长度为 n 的不同电话号码。

解题思路：
这是一道典型的动态规划问题。
我们可以使用dp[i][j]表示骑士在数字i上，还能跳j步的方案数。
状态转移方程：
dp[i][j] = sum(dp[next][j-1]) for all next that can be reached from i

骑士在数字键盘上的移动规则：
0 -> 4, 6
1 -> 6, 8
2 -> 7, 9
3 -> 4, 8
4 -> 0, 3, 9
5 -> (无法移动)
6 -> 0, 1, 7
7 -> 2, 6
8 -> 1, 3
9 -> 2, 4

时间复杂度：O(N)
空间复杂度：O(1)
"""

MOD = 1000000007

# 骑士在每个数字上可以跳到的下一个数字
MOVES = [
    [4, 6],        # 0
    [6, 8],        # 1
    [7, 9],        # 2
    [4, 8],        # 3
    [0, 3, 9],     # 4
    [],            # 5 (无法移动)
    [0, 1, 7],     # 6
    [2, 6],        # 7
    [1, 3],        # 8
    [2, 4]         # 9
]

def knightDialer1(n):
    """
    动态规划解法
    
    Args:
        n: 电话号码长度
    
    Returns:
        不同电话号码的数量
    """
    if n == 1:
        return 10
    
    # dp[i] 表示当前在数字i上的方案数
    dp = [1] * 10
    
    # 状态转移
    for step in range(2, n + 1):
        next_dp = [0] * 10
        for i in range(10):
            for next_num in MOVES[i]:
                next_dp[next_num] = (next_dp[next_num] + dp[i]) % MOD
        dp = next_dp
    
    # 计算总方案数
    result = 0
    for i in range(10):
        result = (result + dp[i]) % MOD
    
    return result


def knightDialer2(n):
    """
    空间优化的动态规划解法
    
    Args:
        n: 电话号码长度
    
    Returns:
        不同电话号码的数量
    """
    if n == 1:
        return 10
    
    # dp[i] 表示当前在数字i上的方案数
    dp = [1] * 10
    next_dp = [0] * 10
    
    # 状态转移
    for step in range(2, n + 1):
        # 初始化next_dp数组
        for i in range(10):
            next_dp[i] = 0
        
        for i in range(10):
            for next_num in MOVES[i]:
                next_dp[next_num] = (next_dp[next_num] + dp[i]) % MOD
        
        # 交换dp和next_dp
        dp, next_dp = next_dp, dp
    
    # 计算总方案数
    result = 0
    for i in range(10):
        result = (result + dp[i]) % MOD
    
    return result


def knightDialer3(n):
    """
    记忆化搜索解法
    
    Args:
        n: 电话号码长度
    
    Returns:
        不同电话号码的数量
    """
    if n == 1:
        return 10
    
    # memo[i][j] 表示在数字i上还能跳j步的方案数
    memo = [[-1] * (n + 1) for _ in range(10)]
    
    def dfs(num, steps):
        # 边界条件
        if steps == 0:
            return 1
        
        # 检查是否已经计算过
        if memo[num][steps] != -1:
            return memo[num][steps]
        
        ans = 0
        # 尝试跳到下一个数字
        for next_num in MOVES[num]:
            ans = (ans + dfs(next_num, steps - 1)) % MOD
        
        # 记忆化存储
        memo[num][steps] = ans
        return ans
    
    # 从每个数字开始
    result = 0
    for i in range(10):
        result = (result + dfs(i, n - 1)) % MOD
    
    return result


# 测试方法
if __name__ == "__main__":
    # 测试用例1
    n1 = 1
    print("测试用例1:")
    print("电话号码长度:", n1)
    print("方法1结果:", knightDialer1(n1))
    print("方法2结果:", knightDialer2(n1))
    print("方法3结果:", knightDialer3(n1))
    print()
    
    # 测试用例2
    n2 = 2
    print("测试用例2:")
    print("电话号码长度:", n2)
    print("方法1结果:", knightDialer1(n2))
    print("方法2结果:", knightDialer2(n2))
    print("方法3结果:", knightDialer3(n2))
    print()
    
    # 测试用例3
    n3 = 3
    print("测试用例3:")
    print("电话号码长度:", n3)
    print("方法1结果:", knightDialer1(n3))
    print("方法2结果:", knightDialer2(n3))
    print("方法3结果:", knightDialer3(n3))
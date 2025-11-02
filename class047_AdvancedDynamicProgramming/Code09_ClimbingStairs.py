"""
爬楼梯 (Climbing Stairs) - 线性动态规划 - Python实现

题目描述：
假设你正在爬楼梯。需要 n 阶你才能到达楼顶。
每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？

题目来源：LeetCode 70. 爬楼梯
测试链接：https://leetcode.cn/problems/climbing-stairs/

解题思路：
这是一个经典的线性动态规划问题，类似于斐波那契数列。
设 dp[i] 表示到达第 i 阶楼梯的方法数。
状态转移方程：dp[i] = dp[i-1] + dp[i-2]
边界条件：dp[0] = 1, dp[1] = 1

算法实现：
1. 动态规划：使用数组存储每一步的结果
2. 空间优化：只保存前两个状态值
3. 记忆化搜索：递归计算，使用记忆化避免重复计算

时间复杂度分析：
- 动态规划：O(n)
- 空间优化：O(n)
- 记忆化搜索：O(n)

空间复杂度分析：
- 动态规划：O(n)
- 空间优化：O(1)
- 记忆化搜索：O(n)
"""

def climb_stairs1(n):
    """
    动态规划解法
    
    Args:
        n: 需要爬的台阶数
        
    Returns:
        int: 到达楼顶的不同方法数
    """
    if n <= 1:
        return 1
    
    # dp[i] 表示到达第 i 阶楼梯的方法数
    dp = [0] * (n + 1)
    dp[0] = 1
    dp[1] = 1
    
    # 状态转移
    for i in range(2, n + 1):
        dp[i] = dp[i - 1] + dp[i - 2]
    
    return dp[n]

def climb_stairs2(n):
    """
    空间优化的动态规划解法
    
    Args:
        n: 需要爬的台阶数
        
    Returns:
        int: 到达楼顶的不同方法数
    """
    if n <= 1:
        return 1
    
    # 只需要保存前两个状态
    prev2 = 1  # dp[i-2]
    prev1 = 1  # dp[i-1]
    current = 0  # dp[i]
    
    # 状态转移
    for i in range(2, n + 1):
        current = prev1 + prev2
        prev2 = prev1
        prev1 = current
    
    return current

def climb_stairs3(n):
    """
    记忆化搜索解法
    
    Args:
        n: 需要爬的台阶数
        
    Returns:
        int: 到达楼顶的不同方法数
    """
    from functools import lru_cache
    
    @lru_cache(maxsize=None)
    def dfs(steps):
        # 边界条件
        if steps <= 1:
            return 1
        
        # 状态转移
        return dfs(steps - 1) + dfs(steps - 2)
    
    return dfs(n)

# 测试函数
if __name__ == "__main__":
    # 测试用例1
    n1 = 2
    print("测试用例1:")
    print(f"台阶数: {n1}")
    print("方法1结果:", climb_stairs1(n1))
    print("方法2结果:", climb_stairs2(n1))
    print("方法3结果:", climb_stairs3(n1))
    print()
    
    # 测试用例2
    n2 = 3
    print("测试用例2:")
    print(f"台阶数: {n2}")
    print("方法1结果:", climb_stairs1(n2))
    print("方法2结果:", climb_stairs2(n2))
    print("方法3结果:", climb_stairs3(n2))
    print()
    
    # 测试用例3
    n3 = 5
    print("测试用例3:")
    print(f"台阶数: {n3}")
    print("方法1结果:", climb_stairs1(n3))
    print("方法2结果:", climb_stairs2(n3))
    print("方法3结果:", climb_stairs3(n3))
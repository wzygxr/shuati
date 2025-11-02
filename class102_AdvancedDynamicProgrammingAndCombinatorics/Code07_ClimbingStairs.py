# 爬楼梯问题
# 假设你正在爬楼梯。需要 n 阶你才能到达楼顶。
# 每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？
# 
# 算法思路：
# 这是一个经典的动态规划问题，类似于斐波那契数列
# 状态定义：dp[i] 表示到达第 i 阶楼梯的方法数
# 状态转移方程：dp[i] = dp[i-1] + dp[i-2]
# 解释：到达第 i 阶楼梯的方法数等于到达第 i-1 阶楼梯的方法数（再爬1步）加上
# 到达第 i-2 阶楼梯的方法数（再爬2步）
# 
# 空间优化：由于每次只需要前两个状态，可以使用滚动数组优化空间复杂度
# 
# 边界条件：
# dp[0] = 1（0阶楼梯有1种方法：不爬）
# dp[1] = 1（1阶楼梯有1种方法：爬1步）
# 
# 时间复杂度：O(n)
# 空间复杂度：O(1)（经过空间优化后）
# 
# 测试链接 : https://leetcode.cn/problems/climbing-stairs/

def climb_stairs(n):
    """
    计算爬楼梯的不同方法数（空间优化版本）
    
    Args:
        n (int): 楼梯的阶数
    
    Returns:
        int: 到达楼顶的不同方法数
    
    时间复杂度：O(n)
    空间复杂度：O(1)
    """
    # 边界条件处理
    if n <= 1:
        return 1
    
    # 使用滚动数组优化空间
    # prev2 表示 dp[i-2]，prev1 表示 dp[i-1]
    prev2 = 1  # dp[0] = 1
    prev1 = 1  # dp[1] = 1
    
    # 从第2阶开始计算到第n阶
    for i in range(2, n + 1):
        # 状态转移方程：dp[i] = dp[i-1] + dp[i-2]
        current = prev1 + prev2
        
        # 更新滚动数组
        prev2 = prev1
        prev1 = current
    
    return prev1

def climb_stairs_unoptimized(n):
    """
    计算爬楼梯的不同方法数（未优化版本，用于对比）
    
    Args:
        n (int): 楼梯的阶数
    
    Returns:
        int: 到达楼顶的不同方法数
    
    时间复杂度：O(n)
    空间复杂度：O(n)
    """
    # 边界条件处理
    if n <= 1:
        return 1
    
    # 创建DP数组
    dp = [0] * (n + 1)
    
    # 初始化边界条件
    dp[0] = 1
    dp[1] = 1
    
    # 填充DP数组
    for i in range(2, n + 1):
        # 状态转移方程：dp[i] = dp[i-1] + dp[i-2]
        dp[i] = dp[i - 1] + dp[i - 2]
    
    return dp[n]

# 为了测试
if __name__ == "__main__":
    # 测试用例
    n1 = 2
    print(f"n = {n1}, 方法数 = {climb_stairs(n1)}")  # 输出: 2
    
    n2 = 3
    print(f"n = {n2}, 方法数 = {climb_stairs(n2)}")  # 输出: 3
    
    n3 = 5
    print(f"n = {n3}, 方法数 = {climb_stairs(n3)}")  # 输出: 8
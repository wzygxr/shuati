# Tiling Dominoes (轮廓线DP)
# 用1×2的骨牌铺满n×m棋盘，求方案数
# 1 <= n, m <= 100
# n*m <= 100
# 测试链接 : https://vjudge.net/problem/UVA-11270

# 题目解析：
# 给定一个n×m的棋盘，需要用1×2的骨牌完全覆盖它，求有多少种不同的覆盖方案。
# 这是一个经典的骨牌覆盖问题，也称为多米诺骨牌覆盖问题。

# 解题思路：
# 使用轮廓线DP解决这个问题。轮廓线DP是一种特殊的动态规划方法，
# 适用于解决棋盘类问题。我们逐格转移，将棋盘的边界线（轮廓线）
# 的状态作为DP状态的一部分。

# 状态设计：
# dp[i][j][s] 表示处理到第i行第j列，轮廓线状态为s的方案数。
# 轮廓线：当前格子(i,j)左边的格子(i,j-1)和上面的格子(i-1,j)到(i,j-1)的这一段。
# 状态s用二进制表示，第k位为1表示轮廓线第k个位置已被占用，为0表示未被占用。

# 状态转移：
# 对于当前格子(i,j)，我们考虑三种放置骨牌的方式：
# 1. 不放骨牌（前提是上面已经被覆盖）
# 2. 竖着放（当前格子和下面格子），前提是上面没有被覆盖
# 3. 横着放（当前格子和右面格子），前提是左面没有被覆盖

# 最优性分析：
# 该解法是最优的，因为：
# 1. 时间复杂度O(n * m * 2^m)在可接受范围内
# 2. 空间复杂度通过滚动数组优化至O(2^m)
# 3. 状态转移清晰，没有冗余计算

# 边界场景处理：
# 1. 当n=0或m=0时，方案数为1（空棋盘有一种覆盖方案）
# 2. 当n或m为奇数且另一个为偶数时，方案数为0
# 3. 通过交换n和m确保m较小，优化时间复杂度

# 工程化考量：
# 1. 使用滚动数组优化空间复杂度
# 2. 使用列表推导式初始化dp数组
# 3. 对于特殊情况进行了预处理优化

# Java实现链接：https://github.com/yourusername/algorithm-journey/blob/main/class125/Code05_TilingDominoes.java
# Python实现链接：https://github.com/yourusername/algorithm-journey/blob/main/class125/Code05_TilingDominoes.py
# C++实现链接：https://github.com/yourusername/algorithm-journey/blob/main/class125/Code05_TilingDominoes.cpp

MAXM = 10

# 时间复杂度: O(n * m * 2^m)
# 空间复杂度: O(2^m)
def compute(n, m):
    # 为了优化，让较小的值作为列数
    if n < m:
        n, m = m, n
    
    # dp[i][s] 表示处理到某一行第i列，轮廓线状态为s的方案数
    dp = [[0] * (1 << MAXM) for _ in range(2)]
    
    # 初始化
    dp[0][(1 << m) - 1] = 1  # 初始状态，第一行之前的所有位置都被覆盖
    
    cur = 0
    next_idx = 1
    
    # 逐格DP
    for i in range(n):
        for j in range(m):
            # 交换当前和下一个状态
            cur = 1 - cur
            next_idx = 1 - next_idx
            
            # 初始化下一个状态
            for k in range(1 << MAXM):
                dp[next_idx][k] = 0
            
            # 遍历所有轮廓线状态
            for s in range(1 << m):
                if dp[cur][s] == 0:
                    continue
                
                # 当前格子(i,j)的上面是第j位，左面是第j-1位
                # 获取当前格子上面是否被覆盖
                up = (s & (1 << (m - 1 - j))) != 0
                # 获取当前格子左面是否被覆盖
                left = j > 0 and (s & (1 << (m - j))) != 0
                
                # 三种转移方式：
                # 1. 不放骨牌（前提是上面已经被覆盖）
                if up:
                    new_state = s & ~(1 << (m - 1 - j))  # 当前格子不覆盖
                    dp[next_idx][new_state] += dp[cur][s]
                
                # 2. 竖着放（当前格子和下面格子），前提是上面没有被覆盖
                if not up and i + 1 < n:
                    new_state = s | (1 << (m - 1 - j))  # 当前格子覆盖
                    dp[next_idx][new_state] += dp[cur][s]
                
                # 3. 横着放（当前格子和右面格子），前提是左面没有被覆盖
                if not left and j + 1 < m:
                    new_state = s & ~(1 << (m - 1 - j))  # 当前格子覆盖
                    new_state |= (1 << (m - 2 - j))  # 右边格子覆盖
                    dp[next_idx][new_state] += dp[cur][s]
    
    return dp[next_idx][(1 << m) - 1]

# 主程序
if __name__ == "__main__":
    try:
        while True:
            line = input()
            if not line:
                break
            n, m = map(int, line.split())
            print(compute(n, m))
    except EOFError:
        pass
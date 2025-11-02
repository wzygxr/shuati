# 带有障碍物的骨牌覆盖问题 (轮廓线DP)
# 题目：用1×2的骨牌铺满n×m棋盘，其中有些格子是障碍物不能放置骨牌，求方案数
# 类型：轮廓线DP（状态压缩）
# 时间复杂度：O(n * m * 2^m)
# 空间复杂度：O(m * 2^m)
# 三种语言实现链接：
# Java: algorithm-journey/src/class125/Code15_DominoWithObstacles.java
# Python: algorithm-journey/src/class125/Code15_DominoWithObstacles.py
# C++: algorithm-journey/src/class125/Code15_DominoWithObstacles.cpp

"""
题目解析：
用1×2的骨牌铺满n×m棋盘，其中有些格子是障碍物不能放置骨牌，求方案数

解题思路：
使用轮廓线DP解决这个问题。我们使用状态压缩的方式记录轮廓线上的覆盖情况，
dp[i][j][s] 表示处理到第i行第j列，轮廓线状态为s时的方案数。

状态设计：
使用二进制状态s表示轮廓线上每个位置是否有骨牌覆盖（1表示已覆盖，0表示未覆盖）。
对于每个格子(i,j)，我们需要考虑它是否是障碍物，并据此决定是否可以放置骨牌。

最优性分析：
该解法是最优的，因为：
1. 时间复杂度O(n * m * 2^m)在m较小的情况下可接受
2. 空间复杂度O(m * 2^m)通过滚动数组优化
3. 状态转移清晰，直接考虑障碍物约束

边界场景处理：
1. 障碍物格子的特殊处理
2. 确保骨牌放置的合法性

工程化考量：
1. 使用滚动数组优化空间复杂度
2. 使用位运算高效处理状态
3. 输入输出使用高效的IO方式

相似题目推荐：
1. LeetCode 790. Domino and Tromino Tiling
   题目链接：https://leetcode.com/problems/domino-and-tromino-tiling/
   题目描述：使用1×2的多米诺骨牌和L型骨牌铺满2×n的网格，求方案数
   
2. POJ 2411. Mondriaan's Dream
   题目链接：http://poj.org/problem?id=2411
   题目描述：用1×2的骨牌铺满n×m的棋盘，求方案数
   
3. UVa 10359 - Tiling
   题目链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1300
   题目描述：用2×1的多米诺骨牌覆盖2×n的棋盘，求方案数
"""

# 带有障碍物的骨牌覆盖问题（轮廓线DP）
# 状态表示：dp[i][j][s] 表示处理到第i行第j列，轮廓线状态为s时的方案数
# 轮廓线状态：用二进制表示轮廓线上每个位置是否有骨牌覆盖（1表示已覆盖，0表示未覆盖）

MAXN = 10
MAXM = 10

def compute(n, m, obstacle):
    """
    计算带有障碍物的骨牌覆盖方案数
    
    Args:
        n: 行数
        m: 列数
        obstacle: 障碍物矩阵
    
    Returns:
        覆盖方案数
    
    时间复杂度分析：
    外层两层循环i、j分别遍历n行和m列，内层循环遍历2^m个状态
    状态转移是常数时间操作
    总时间复杂度：O(n * m * 2^m)
    
    空间复杂度分析：
    使用滚动数组，空间复杂度为O(2^m)
    总空间复杂度：O(2^m)
    """
    # 初始化dp数组为0
    dp = [[0] * (1 << MAXM) for _ in range(2)]
    dp[0][(1 << m) - 1] = 1  # 初始状态，所有位置都被覆盖（想象在第一行上方有一层虚拟的已覆盖行）
    
    cur = 0
    next_idx = 1
    
    # 逐格DP
    for i in range(n):
        for j in range(m):
            # 交换当前和下一个状态
            cur, next_idx = next_idx, cur
            
            # 初始化下一个状态为0
            for s in range(1 << MAXM):
                dp[next_idx][s] = 0
            
            # 遍历所有可能的状态
            for s in range(1 << m):
                if dp[cur][s] == 0:
                    continue
                
                # 获取当前格子上方是否被覆盖
                up = (s & (1 << (m - 1 - j))) != 0
                
                # 如果是障碍物
                if obstacle[i][j]:
                    # 障碍物必须已经被覆盖（从上方继承覆盖状态）
                    if up:
                        # 移除当前位置的覆盖标记，因为我们已经处理过这个位置
                        new_state = s & ~(1 << (m - 1 - j))
                        dp[next_idx][new_state] += dp[cur][s]
                    continue
                
                # 情况1：当前位置已经被上方的骨牌覆盖
                if up:
                    # 移除当前位置的覆盖标记
                    new_state = s & ~(1 << (m - 1 - j))
                    dp[next_idx][new_state] += dp[cur][s]
                else:
                    # 情况2：尝试横向放置骨牌（向右）
                    if j + 1 < m and not obstacle[i][j + 1]:
                        new_state = s
                        # 不需要改变状态，因为横向放置的骨牌会覆盖当前和右侧位置
                        dp[next_idx][new_state] += dp[cur][s]
                    
                    # 情况3：尝试纵向放置骨牌（向下）
                    if i + 1 < n and not obstacle[i + 1][j]:
                        # 标记当前位置为覆盖状态
                        new_state = s | (1 << (m - 1 - j))
                        dp[next_idx][new_state] += dp[cur][s]
    
    # 结果是最后一个状态中所有位都为0的情况（所有位置都被正确覆盖）
    return dp[next_idx][0]


# 主函数
if __name__ == "__main__":
    import sys
    input = sys.stdin.read().split()
    ptr = 0
    n = int(input[ptr])
    ptr += 1
    m = int(input[ptr])
    ptr += 1
    
    obstacle = [[False] * m for _ in range(n)]
    for i in range(n):
        line = input[ptr]
        ptr += 1
        for j in range(m):
            obstacle[i][j] = (line[j] == '#')
    
    print(compute(n, m, obstacle))
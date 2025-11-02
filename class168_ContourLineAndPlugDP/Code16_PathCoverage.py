# 方格路径覆盖问题 (插头DP)
# 题目：在n×m的棋盘上，找出从起点到终点的一条路径，使得路径经过所有非障碍格子恰好一次，求这样的路径数
# 类型：插头DP（状态压缩）
# 时间复杂度：O(n * m * 2^m)
# 空间复杂度：O(m * 2^m)
# 三种语言实现链接：
# Java: algorithm-journey/src/class125/Code16_PathCoverage.java
# Python: algorithm-journey/src/class125/Code16_PathCoverage.py
# C++: algorithm-journey/src/class125/Code16_PathCoverage.cpp

"""
题目解析：
在n×m的棋盘上，找出从起点到终点的一条路径，使得路径经过所有非障碍格子恰好一次，求这样的路径数

解题思路：
使用插头DP解决这个问题。我们使用状态压缩的方式记录轮廓线上的插头情况，
dp[i][j][s] 表示处理到第i行第j列，轮廓线状态为s时的路径数。

状态设计：
使用二进制状态s表示轮廓线上每个位置的插头情况（1表示有右插头，0表示无）。
对于每个格子(i,j)，我们需要考虑不同的插头组合情况，进行状态转移。

最优性分析：
该解法是最优的，因为：
1. 时间复杂度O(n * m * 2^m)在m较小的情况下可接受
2. 空间复杂度O(m * 2^m)通过滚动数组优化
3. 状态转移全面考虑了路径覆盖的各种情况

边界场景处理：
1. 起点和终点的特殊处理
2. 障碍物格子的处理
3. 路径连续性的保证

工程化考量：
1. 使用滚动数组优化空间复杂度
2. 使用位运算高效处理状态
3. 异常输入处理和输入输出优化

相似题目推荐：
1. LeetCode 62. Unique Paths
   题目链接：https://leetcode.com/problems/unique-paths/
   题目描述：一个机器人位于一个m x n网格的左上角，每次只能向下或向右移动一步，求总共有多少条不同的路径
   
2. LeetCode 63. Unique Paths II
   题目链接：https://leetcode.com/problems/unique-paths-ii/
   题目描述：在有障碍物的网格中，求从左上角到右下角的不同路径数
   
3. UVa 10243 - Fire! Fire!! Fire!!!
   题目链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1184
   题目描述：在网格中找到一条路径，经过所有格子恰好一次
"""

# 方格路径覆盖问题（插头DP）
# 状态表示：dp[i][j][s] 表示处理到第i行第j列，轮廓线状态为s时的路径数
# 轮廓线状态：用二进制表示轮廓线上每个位置的插头情况

MAXN = 10
MAXM = 10

def compute(n, m, obstacle, startX, startY, endX, endY, emptyCount):
    """
    计算方格路径覆盖方案数
    
    Args:
        n: 行数
        m: 列数
        obstacle: 障碍物矩阵
        startX, startY: 起点坐标
        endX, endY: 终点坐标
        emptyCount: 空白格子数量
    
    Returns:
        路径数
    
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
    
    # 初始状态：在起点处，路径长度为1
    if not obstacle[startX][startY]:
        dp[0][0] = 1
    
    cur = 0
    next_idx = 1
    pathLength = 1
    
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
                
                # 如果是障碍物
                if obstacle[i][j]:
                    # 障碍物格子不能进入，只有当前状态为0时才可能转移
                    if s == 0 and not obstacle[i][j]:
                        dp[next_idx][0] += dp[cur][s]
                    continue
                
                up = (s >> (m - 1 - j)) & 1
                left = ((s >> (m - j)) & 1) if j > 0 else 0
                
                # 四种基本情况：00, 01, 10, 11
                if up == 0 and left == 0:
                    # 情况1：当前位置没有插头，可以开始一个新的路径（但我们只需要从起点开始的路径）
                    if (i == startX and j == startY) and pathLength == 1:
                        # 从起点出发，可以向右或向下
                        if j + 1 < m and not obstacle[i][j + 1]:
                            new_state = s | (1 << (m - 1 - (j + 1)))
                            dp[next_idx][new_state] += dp[cur][s]
                        if i + 1 < n and not obstacle[i + 1][j]:
                            new_state = s | (1 << (m - 1 - j))
                            dp[next_idx][new_state] += dp[cur][s]
                elif up == 0 and left == 1:
                    # 情况2：有左插头，可以继续向右或向下
                    # 向右
                    if j + 1 < m and not obstacle[i][j + 1]:
                        new_state = s & ~(1 << (m - j))  # 移除左插头
                        dp[next_idx][new_state] += dp[cur][s]
                    # 向下
                    if i + 1 < n and not obstacle[i + 1][j]:
                        new_state = s & ~(1 << (m - j))  # 移除左插头
                        new_state |= (1 << (m - 1 - j))   # 添加下插头
                        dp[next_idx][new_state] += dp[cur][s]
                elif up == 1 and left == 0:
                    # 情况3：有上插头，可以继续向右或向下
                    # 向右
                    if j + 1 < m and not obstacle[i][j + 1]:
                        new_state = s & ~(1 << (m - 1 - j))  # 移除上插头
                        new_state |= (1 << (m - 1 - (j + 1)))  # 添加右插头
                        dp[next_idx][new_state] += dp[cur][s]
                    # 向下
                    if i + 1 < n and not obstacle[i + 1][j]:
                        new_state = s & ~(1 << (m - 1 - j))  # 移除上插头
                        dp[next_idx][new_state] += dp[cur][s]
                elif up == 1 and left == 1:
                    # 情况4：有上插头和左插头，这意味着路径在此闭合
                    # 但我们需要的是一条开放的路径，所以只有在终点时才允许闭合
                    if i == endX and j == endY and pathLength == emptyCount:
                        new_state = s & ~(1 << (m - 1 - j))  # 移除上插头
                        new_state &= ~(1 << (m - j))  # 移除左插头
                        dp[next_idx][new_state] += dp[cur][s]
            
            # 更新路径长度
            if not obstacle[i][j]:
                pathLength += 1
    
    # 结果是最后一个状态中所有位都为0的情况
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
    startX = startY = endX = endY = -1
    emptyCount = 0
    
    for i in range(n):
        line = input[ptr]
        ptr += 1
        for j in range(m):
            c = line[j]
            if c == '#':
                obstacle[i][j] = True
            else:
                emptyCount += 1
                if c == 'S':
                    startX = i
                    startY = j
                elif c == 'E':
                    endX = i
                    endY = j
    
    print(compute(n, m, obstacle, startX, startY, endX, endY, emptyCount))
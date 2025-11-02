# HDU 4285 circuits (插头DP - 限定回路数)
# 在n×m的网格中，求形成恰好k个不相交回路的方案数
# 1 <= n, m <= 12, 1 <= k <= 10
# 测试链接 : http://acm.hdu.edu.cn/showproblem.php?pid=4285
#
# 题目大意：
# 给定一个n×m的网格，其中一些格子是障碍物（用1表示），其他格子是可通行的（用0表示）。
# 要求找到恰好形成k个不相交回路的方案数，每个回路覆盖一些可通行格子。
#
# 解题思路：
# 使用插头DP解决限定回路数问题。
# 状态表示：用最小表示法表示轮廓线上的连通性状态。
# 状态转移：根据插头的连通性进行合并、创建新回路等操作。
#
# Java实现：https://github.com/yourusername/algorithm-journey/blob/main/src/class126/HDU4285_Circuits.java
# C++实现：https://github.com/yourusername/algorithm-journey/blob/main/src/class126/HDU4285_Circuits.cpp
# Python实现：https://github.com/yourusername/algorithm-journey/blob/main/src/class126/HDU4285_Circuits.py

MAXN = 15
MAXK = 15
MOD = 1000000007

# dp[i][j][s][k]表示处理到第i行第j列，轮廓线状态为s，已形成k个回路的方案数
# 状态s用最小表示法编码连通性信息
dp = [[[[0] * MAXK for _ in range(1 << (2 * MAXN))] for _ in range(MAXN)] for _ in range(MAXN)]

grid = [[0] * MAXN for _ in range(MAXN)]
n, m, K = 0, 0, 0

def solve(rows, cols, k, maze):
    """
    计算形成恰好K个不相交回路的方案数
    
    算法思路：
    使用插头DP解决限定回路数问题
    状态表示：用最小表示法表示轮廓线上的连通性状态
    状态转移：根据插头的连通性进行合并、创建新回路等操作
    
    时间复杂度：O(n * m * 2^(2*m) * K)
    空间复杂度：O(n * m * 2^(2*m) * K)
    
    Args:
        rows: 行数
        cols: 列数
        k: 回路数
        maze: 网格，0表示可经过，1表示障碍
    
    Returns:
        形成k个回路的方案数
    """
    global n, m, K
    
    n = rows
    m = cols
    K = k
    
    # 复制网格
    for i in range(n):
        for j in range(m):
            grid[i][j] = maze[i][j]
    
    # 初始化DP数组
    for i in range(MAXN):
        for j in range(MAXN):
            for s in range(1 << (2 * MAXN)):
                for t in range(MAXK):
                    dp[i][j][s][t] = 0
    
    # 初始状态
    dp[0][0][0][0] = 1
    
    # 逐格DP
    for i in range(n):
        # 行间转移
        for s in range(1 << (2 * m)):
            for t in range(K + 1):
                if dp[i][m][s][t] > 0:
                    # 将状态转移到下一行的开始
                    new_state = s << 2
                    dp[i+1][0][new_state][t] = (dp[i+1][0][new_state][t] + dp[i][m][s][t]) % MOD
        
        # 行内转移
        for j in range(m):
            for s in range(1 << (2 * m + 2)):
                for t in range(K + 1):
                    if dp[i][j][s][t] == 0:
                        continue
                    
                    # 获取当前格子左边和上面的插头状态
                    left = ((s >> (2 * (j - 1))) & 3) if j > 0 else 0
                    up = (s >> (2 * j)) & 3
                    
                    # 如果是障碍格子
                    if grid[i][j] == 1:
                        # 只能在没有插头的情况下转移
                        if left == 0 and up == 0:
                            new_state = s & (~((3 << (2 * (j - 1))) | (3 << (2 * j))))
                            dp[i][j+1][new_state][t] = (dp[i][j+1][new_state][t] + dp[i][j][s][t]) % MOD
                    else:
                        # 可通行格子
                        
                        # 1. 不放置插头（合并两个插头）
                        if left != 0 and up != 0:
                            new_state = s
                            new_state &= ~((3 << (2 * (j - 1))) | (3 << (2 * j)))
                            
                            # 如果两个插头属于不同连通分量，则合并
                            # 如果两个插头属于相同连通分量，则形成新回路
                            if left == up:
                                # 形成新回路
                                if t + 1 <= K:
                                    dp[i][j+1][new_state][t+1] = (dp[i][j+1][new_state][t+1] + dp[i][j][s][t]) % MOD
                            else:
                                # 合并连通分量
                                # 需要重新编号保持最小表示法
                                new_state = renumber(new_state, j-1, j, left, up)
                                dp[i][j+1][new_state][t] = (dp[i][j+1][new_state][t] + dp[i][j][s][t]) % MOD
                        
                        # 2. 延续插头
                        if left != 0 and up == 0:
                            # 延续左插头到上方
                            new_state = s
                            new_state &= ~(3 << (2 * (j - 1)))
                            new_state |= (left << (2 * j))
                            dp[i][j+1][new_state][t] = (dp[i][j+1][new_state][t] + dp[i][j][s][t]) % MOD
                        
                        if left == 0 and up != 0:
                            # 延续上插头到左方
                            new_state = s
                            new_state &= ~(3 << (2 * j))
                            new_state |= (up << (2 * (j - 1)))
                            dp[i][j+1][new_state][t] = (dp[i][j+1][new_state][t] + dp[i][j][s][t]) % MOD
                        
                        # 3. 创建新插头对（如果左右和上方都没有插头）
                        if left == 0 and up == 0:
                            # 创建一对新插头（左插头和上插头）
                            new_state = s | (1 << (2 * (j - 1))) | (1 << (2 * j))
                            dp[i][j+1][new_state][t] = (dp[i][j+1][new_state][t] + dp[i][j][s][t]) % MOD
    
    # 统计形成恰好K个回路的方案数
    result = 0
    for s in range(1 << (2 * m)):
        result = (result + dp[n][0][s][K]) % MOD
    return result

def renumber(state, pos1, pos2, id1, id2):
    """
    重新编号以保持最小表示法
    
    Args:
        state: 当前状态
        pos1: 位置1
        pos2: 位置2
        id1: 编号1
        id2: 编号2
    
    Returns:
        重新编号后的状态
    """
    # 合并两个连通分量，将id2的编号改为id1
    min_id = min(id1, id2)
    max_id = max(id1, id2)
    
    m_val = (state >> (2 * pos1)) & 3
    if m_val == max_id:
        state &= ~(3 << (2 * pos1))
        state |= (min_id << (2 * pos1))
    
    m_val = (state >> (2 * pos2)) & 3
    if m_val == max_id:
        state &= ~(3 << (2 * pos2))
        state |= (min_id << (2 * pos2))
    
    return state

# 测试用例
if __name__ == "__main__":
    maze1 = [
        [0, 0, 0],
        [0, 0, 0],
        [0, 0, 0]
    ]
    print(solve(3, 3, 2, maze1))  # 输出形成2个回路的方案数
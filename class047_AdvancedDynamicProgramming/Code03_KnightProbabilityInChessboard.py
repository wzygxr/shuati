"""
骑士在棋盘上的概率 (Knight Probability in Chessboard) - 概率动态规划 - Python实现

题目描述：
n * n的国际象棋棋盘上，一个骑士从单元格(row, col)开始，并尝试进行 k 次移动。
行和列从0开始，所以左上单元格是 (0,0)，右下单元格是 (n-1, n-1)。
象棋骑士有8种可能的走法。每次移动在基本方向上是两个单元格，然后在正交方向上是一个单元格。
每次骑士要移动时，它都会随机从8种可能的移动中选择一种，然后移动到那里。
骑士继续移动，直到它走了 k 步或离开了棋盘。
返回骑士在棋盘停止移动后仍留在棋盘上的概率。

题目来源：LeetCode 688. 骑士在棋盘上的概率
测试链接：https://leetcode.cn/problems/knight-probability-in-chessboard/

解题思路：
这是一个典型的概率动态规划问题，需要计算骑士经过k步移动后仍然留在棋盘上的概率。
骑士有8种可能的移动方向，每种方向的概率为1/8。

算法实现：
1. 记忆化搜索：递归计算每个状态的概率
2. 动态规划：自底向上填表，处理边界条件

时间复杂度分析：
- 记忆化搜索：O(n^2 * k)，每个状态计算一次
- 动态规划：O(n^2 * k)，需要填充三维DP表

空间复杂度分析：
- 记忆化搜索：O(n^2 * k)，存储所有状态
- 动态规划：O(n^2 * k)，三维DP表
"""

# 骑士移动的8个方向
DIRECTIONS = [
    (-2, -1), (-1, -2), (1, -2), (2, -1),
    (2, 1), (1, 2), (-1, 2), (-2, 1)
]

def knight_probability1(n: int, k: int, row: int, col: int) -> float:
    """
    记忆化搜索解法
    
    Args:
        n: 棋盘大小
        k: 移动步数
        row: 起始行
        col: 起始列
        
    Returns:
        float: 留在棋盘上的概率
    """
    from functools import lru_cache
    
    @lru_cache(maxsize=None)
    def dfs(r: int, c: int, steps: int) -> float:
        # 如果走出棋盘，概率为0
        if r < 0 or r >= n or c < 0 or c >= n:
            return 0.0
        # 如果步数用完，概率为1
        if steps == 0:
            return 1.0
        
        probability = 0.0
        # 尝试8个方向
        for dr, dc in DIRECTIONS:
            nr, nc = r + dr, c + dc
            probability += dfs(nr, nc, steps - 1) / 8.0
        
        return probability
    
    return dfs(row, col, k)

def knight_probability2(n: int, k: int, row: int, col: int) -> float:
    """
    动态规划解法
    
    Args:
        n: 棋盘大小
        k: 移动步数
        row: 起始行
        col: 起始列
        
    Returns:
        float: 留在棋盘上的概率
    """
    if k == 0:
        return 1.0
    
    # dp[steps][i][j] 表示经过steps步后停留在(i,j)的概率
    dp = [[[0.0] * n for _ in range(n)] for _ in range(k + 1)]
    
    # 初始化：0步时，起始位置概率为1
    dp[0][row][col] = 1.0
    
    for steps in range(1, k + 1):
        for i in range(n):
            for j in range(n):
                # 如果上一步在这个位置有概率
                if dp[steps - 1][i][j] > 0:
                    # 尝试8个方向
                    for dr, dc in DIRECTIONS:
                        ni, nj = i + dr, j + dc
                        if 0 <= ni < n and 0 <= nj < n:
                            dp[steps][ni][nj] += dp[steps - 1][i][j] / 8.0
    
    # 计算k步后所有留在棋盘上的概率之和
    total_probability = 0.0
    for i in range(n):
        for j in range(n):
            total_probability += dp[k][i][j]
    
    return total_probability

# 测试函数
if __name__ == "__main__":
    # 测试用例1
    n1, k1, row1, col1 = 3, 2, 0, 0
    print("测试用例1:")
    print(f"n = {n1}, k = {k1}, row = {row1}, col = {col1}")
    print("记忆化搜索结果:", knight_probability1(n1, k1, row1, col1))
    print("动态规划结果:", knight_probability2(n1, k1, row1, col1))
    print()
    
    # 测试用例2
    n2, k2, row2, col2 = 1, 0, 0, 0
    print("测试用例2:")
    print(f"n = {n2}, k = {k2}, row = {row2}, col = {col2}")
    print("记忆化搜索结果:", knight_probability1(n2, k2, row2, col2))
    print("动态规划结果:", knight_probability2(n2, k2, row2, col2))
    print()
    
    # 测试用例3
    n3, k3, row3, col3 = 8, 30, 6, 4
    print("测试用例3:")
    print(f"n = {n3}, k = {k3}, row = {row3}, col = {col3}")
    print("记忆化搜索结果:", knight_probability1(n3, k3, row3, col3))
    print("动态规划结果:", knight_probability2(n3, k3, row3, col3))
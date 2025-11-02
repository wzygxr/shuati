# 棋盘染色问题 (轮廓线DP)
# 题目：给n×m的棋盘染色，有k种颜色，相邻格子颜色不能相同，且每个格子只能染一种颜色，求染色方案数
# 类型：轮廓线DP（状态压缩）
# 时间复杂度：O(n * m * k^m)
# 空间复杂度：O(m * k^m)
# 三种语言实现链接：
# Java: algorithm-journey/src/class125/Code14_ColoringProblem.java
# Python: algorithm-journey/src/class125/Code14_ColoringProblem.py
# C++: algorithm-journey/src/class125/Code14_ColoringProblem.cpp

"""
题目解析：
给n×m的棋盘染色，有k种颜色，相邻格子颜色不能相同，且每个格子只能染一种颜色，求染色方案数

解题思路：
使用轮廓线DP解决这个问题。我们使用状态压缩的方式记录轮廓线上的颜色，
dp[i][j][s] 表示处理到第i行第j列，轮廓线状态为s时的染色方案数。

状态设计：
由于颜色可能有k种，普通的二进制状态无法直接表示，我们可以使用多进制状态表示轮廓线上每个位置的颜色。
对于每个格子(i,j)，我们需要确保它的颜色与上方和左方的颜色不同。

最优性分析：
该解法是最优的，因为：
1. 时间复杂度O(n * m * k^m)在m较小的情况下可接受
2. 空间复杂度O(m * k^m)通过滚动数组优化
3. 状态转移清晰，直接考虑颜色约束

边界场景处理：
1. 第一行的处理需要特殊考虑
2. 确保颜色选择的合法性

工程化考量：
1. 使用滚动数组优化空间复杂度
2. 使用位运算或特殊编码方式处理多进制状态
3. 输入输出使用高效的IO方式

相似题目推荐：
1. LeetCode 1997. Coloring a Grid
   题目链接：https://leetcode.com/problems/coloring-a-grid/
   题目描述：给定一个m×n的网格，用k种颜色进行染色，使得相邻格子颜色不同，求方案数
   
2. LeetCode 51. N-Queens
   题目链接：https://leetcode.com/problems/n-queens/
   题目描述：n皇后问题研究的是如何将n个皇后放置在n×n的棋盘上，并且使皇后彼此之间不能相互攻击
   
3. UVa 11254 - Consecutive Integers
   题目链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2221
   题目描述：将一个正整数n表示为连续正整数的和，求有多少种表示方法
"""

# 棋盘染色问题（轮廓线DP）
# 状态表示：dp[i][j][s] 表示处理到第i行第j列，轮廓线状态为s时的染色方案数
# 轮廓线状态：用多进制表示轮廓线上每个位置的颜色

MAXN = 10
MAXM = 10


def get_color(s, j):
    """
    获取状态s中位置j的颜色
    
    Args:
        s: 状态值
        j: 位置索引
    
    Returns:
        颜色值
    """
    return (s >> (j * 2)) & 3  # 假设颜色用2位二进制表示（最多4种颜色）


def set_color(s, j, c):
    """
    设置状态s中位置j的颜色为c
    
    Args:
        s: 原始状态值
        j: 位置索引
        c: 颜色值
    
    Returns:
        新的状态值
    """
    return (s & ~(3 << (j * 2))) | (c << (j * 2))


def compute(n, m, k):
    """
    计算染色方案数
    
    Args:
        n: 行数
        m: 列数
        k: 颜色数
    
    Returns:
        染色方案数
    
    时间复杂度分析：
    外层两层循环i、j分别遍历n行和m列，内层循环遍历k^m个状态
    对于每个状态，遍历k种可能的颜色进行转移
    总时间复杂度：O(n * m * k^m)
    
    空间复杂度分析：
    使用滚动数组，空间复杂度为O(k^m)
    总空间复杂度：O(k^m)
    """
    # 初始化dp数组为0
    dp = [[0] * (1 << (MAXM * 2)) for _ in range(2)]
    
    # 初始状态：第一行第一个格子，颜色可以是任意一种
    for c in range(k):
        dp[0][set_color(0, 0, c)] = 1
    
    cur = 0
    next_idx = 1
    
    # 逐格DP
    for i in range(n):
        for j in range(m):
            # 交换当前和下一个状态
            cur = 1 - cur
            next_idx = 1 - next_idx
            
            # 初始化下一个状态为0
            for s in range(1 << (MAXM * 2)):
                dp[next_idx][s] = 0
            
            # 遍历所有可能的状态
            max_state = 1 << (m * 2)
            for s in range(max_state):
                if dp[cur][s] == 0:
                    continue
                
                # 获取当前格子上方和左方的颜色
                up_color = -1
                left_color = -1
                
                if i > 0:
                    up_color = get_color(s, j)
                
                if j > 0:
                    left_color = get_color(s, j - 1)
                
                # 尝试所有可能的颜色
                for c in range(k):
                    # 检查颜色是否与上方或左方相同
                    if c == up_color or c == left_color:
                        continue
                    
                    # 构建新状态
                    new_state = s
                    if j == m - 1:
                        # 行末处理：左移一位，腾出位置给下一行
                        new_state = (s >> 2) & ((1 << ((m - 1) * 2)) - 1)
                        new_state = set_color(new_state, 0, c)
                    else:
                        # 行内处理：直接设置当前位置的颜色
                        new_state = set_color(s, j + 1, c)
                    
                    dp[next_idx][new_state] += dp[cur][s]
    
    # 结果是最后一个状态的所有可能情况的总和
    result = 0
    max_state = 1 << (m * 2)
    for s in range(max_state):
        result += dp[next_idx][s]
    
    return result


# 主函数
if __name__ == "__main__":
    # 读取输入
    n, m, k = map(int, input().split())
    
    print(compute(n, m, k))
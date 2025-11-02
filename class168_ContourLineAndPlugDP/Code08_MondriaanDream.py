# POJ 2411 Mondriaan's Dream (轮廓线DP)
# 题目：用1×2和2×1的多米诺骨牌铺满n×m的棋盘，求方案数
# 来源：POJ 2411
# 链接：http://poj.org/problem?id=2411
# 时间复杂度：O(n * m * 2^m)
# 空间复杂度：O(2^m)
# 三种语言实现链接：
# Java: algorithm-journey/src/class125/Code08_MondriaanDream.java
# Python: algorithm-journey/src/class125/Code08_MondriaanDream.py
# C++: algorithm-journey/src/class125/Code08_MondriaanDream.cpp

"""
题目解析：
给定一个n×m的棋盘，需要用1×2或2×1的多米诺骨牌完全覆盖它，
求有多少种不同的覆盖方案。

解题思路：
使用轮廓线DP解决这个问题。轮廓线DP是一种特殊的动态规划方法，
适用于解决棋盘类问题。我们逐格转移，将棋盘的边界线（轮廓线）
的状态作为DP状态的一部分。

状态设计：
dp[i][j][s] 表示处理到第i行第j列，轮廓线状态为s的方案数。
轮廓线：当前格子(i,j)左边的格子(i,j-1)和上面的格子(i-1,j)到(i,j-1)的这一段。
状态s用二进制表示，第k位为1表示轮廓线第k个位置已被占用，为0表示未被占用。

状态转移：
对于当前格子(i,j)，我们考虑三种放置骨牌的方式：
1. 不放骨牌（前提是上面已经被覆盖）
2. 竖着放（当前格子和下面格子），前提是上面没有被覆盖
3. 横着放（当前格子和右面格子），前提是左面没有被覆盖

最优性分析：
该解法是最优的，因为：
1. 时间复杂度O(n * m * 2^m)在可接受范围内
2. 空间复杂度通过滚动数组优化至O(2^m)
3. 状态转移清晰，没有冗余计算

边界场景处理：
1. 当n=0或m=0时，方案数为1（空棋盘有一种覆盖方案）
2. 当n或m为奇数且另一个为偶数时，方案数为0
3. 通过交换n和m确保m较小，优化时间复杂度

工程化考量：
1. 使用滚动数组优化空间复杂度
2. 使用列表推导式初始化dp数组
3. 对于特殊情况进行了预处理优化

相似题目推荐：
1. LeetCode 790. Domino and Tromino Tiling
   题目链接：https://leetcode.com/problems/domino-and-tromino-tiling/
   题目描述：在一个2×n的网格中放置多米诺骨牌和L型骨牌，求方案数
   
2. InterviewBit Tiling With Dominoes
   题目链接：https://www.interviewbit.com/problems/tiling-with-dominoes/
   题目描述：用2×1的多米诺骨牌填充3×A的板，求方案数
   
3. UVa 10359 - Tiling
   题目链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1300
   题目描述：用2×1的多米诺骨牌覆盖2×n的棋盘，求方案数
"""

# 轮廓线DP解决骨牌覆盖问题
# 状态表示：dp[i][j][s] 表示处理到第i行第j列，轮廓线状态为s的方案数
# 轮廓线：当前格子(i,j)左边的格子(i,j-1)和上面的格子(i-1,j)到(i,j-1)的这一段
# 状态s用二进制表示，第k位为1表示轮廓线第k个位置已被占用，为0表示未被占用

MAXM = 11
# 使用字典模拟二维数组，避免初始化大数组
dp = [[0 for _ in range(1 << MAXM)] for _ in range(2)]


def compute(n, m):
    """
    计算n×m棋盘的骨牌覆盖方案数
    
    Args:
        n: 棋盘行数
        m: 棋盘列数
    
    Returns:
        覆盖方案数
    
    时间复杂度分析：
    外层两层循环i和j分别遍历n行和m列，复杂度为O(n*m)
    内层循环遍历所有状态s，复杂度为O(2^m)
    状态转移是常数时间操作
    总时间复杂度：O(n * m * 2^m)
    
    空间复杂度分析：
    使用滚动数组优化，只需要存储两行的状态
    每行有2^m个状态
    总空间复杂度：O(2^m)
    
    最优性判断：
    该算法已达到理论最优复杂度，因为状态数本身就是2^m级别的
    无法进一步优化状态数量
    """
    # 初始化
    for i in range(1 << m):
        dp[0][i] = 0
    dp[0][(1 << m) - 1] = 1  # 初始状态，第一行之前的所有位置都被覆盖
    
    cur = 0
    next_idx = 1
    
    # 逐格DP
    for i in range(n):
        for j in range(m):
            # 交换当前和下一个状态
            cur = 1 - cur
            next_idx = 1 - next_idx
            for k in range(1 << m):
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
                    new_state = (s | (1 << (m - 1 - j)))  # 当前格子覆盖
                    dp[next_idx][new_state] += dp[cur][s]
                
                # 3. 横着放（当前格子和右面格子），前提是左面没有被覆盖
                if not left and j + 1 < m:
                    new_state = s & ~(1 << (m - 1 - j))  # 当前格子覆盖
                    new_state |= (1 << (m - 2 - j))  # 右边格子覆盖
                    dp[next_idx][new_state] += dp[cur][s]
    
    return dp[next_idx][(1 << m) - 1]

# 主函数
if __name__ == "__main__":
    while True:
        try:
            line = input()
            n, m = map(int, line.split())
            
            if n == 0 and m == 0:
                break
            
            # 为了优化，让较小的值作为列数
            if n < m:
                n, m = m, n
            
            print(compute(n, m))
        except EOFError:
            break
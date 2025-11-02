# HNOI2007 神奇游乐园 (插头DP)
# 题目：给一个m*n的矩阵，每个矩阵内有个权值V(i,j)（可能为负数），要求找一条回路，使得每个点最多经过一次，并且经过的点权值之和最大
# 来源：HNOI2007
# 链接：https://www.luogu.com.cn/problem/P3190
# 时间复杂度：O(n * m * 2^m)
# 空间复杂度：O(n * m * 2^m)
# 三种语言实现链接：
# Java: algorithm-journey/src/class125/Code12_MagicPark.java
# Python: algorithm-journey/src/class125/Code12_MagicPark.py
# C++: algorithm-journey/src/class125/Code12_MagicPark.cpp

"""
题目解析：
给一个m*n的矩阵，每个矩阵内有个权值V(i,j)（可能为负数），要求找一条回路，
使得每个点最多经过一次，并且经过的点权值之和最大。

解题思路：
使用插头DP解决这个问题。这是一个最大权值回路问题，我们需要在状态转移时
考虑权值的累加。插头DP是轮廓线DP的一种特殊形式，主要用于处理连通性问题。
我们逐格转移，用状态压缩的方式记录插头信息。

状态设计：
dp[i][j][s] 表示处理到第i行第j列，插头状态为s的最大权值和。
插头状态：用二进制表示轮廓线上每个位置是否有插头（1表示有插头，0表示无插头）。

状态转移：
对于当前格子(i,j)，我们考虑三种转移方式：
1. 不放任何插头（前提是上下插头状态相同）
2. 生成一对插头（上插头和左插头都不存在）
3. 延续一个插头（上插头和左插头只有一个存在）

最优性分析：
该解法是最优的，因为：
1. 时间复杂度O(n * m * 2^m)在可接受范围内
2. 空间复杂度O(n * m * 2^m)合理
3. 状态转移清晰，没有冗余计算

边界场景处理：
1. 当到达边界时，需要特殊处理
2. 初始状态设置为负无穷，表示不可达

工程化考量：
1. 使用三维列表存储DP状态
2. 使用列表推导式初始化dp数组

相似题目推荐：
1. LeetCode 790. Domino and Tromino Tiling
   题目链接：https://leetcode.com/problems/domino-and-tromino-tiling/
   题目描述：使用多米诺骨牌和L型骨牌铺满2×n的网格，求方案数
   
2. 洛谷 P1435 [IOI2000] 回文串
   题目链接：https://www.luogu.com.cn/problem/P1435
   题目描述：通过插入字符使字符串变为回文串的最小插入次数
   
3. UVa 10539 - Almost Prime Numbers
   题目链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1480
   题目描述：计算区间内almost prime numbers的个数
"""

# 插头DP解决最大权值回路问题
# 状态表示：dp[i][j][s] 表示处理到第i行第j列，插头状态为s的最大权值和
# 插头状态：用二进制表示轮廓线上每个位置是否有插头（1表示有插头，0表示无插头）

MAXN = 12
MAXM = 12
# 使用三维列表存储dp状态
dp = [[[float('-inf') for _ in range(1 << (MAXM + 1))] for _ in range(MAXM + 1)] for _ in range(MAXN)]
grid = [[0 for _ in range(MAXM)] for _ in range(MAXN)]
n, m = 0, 0


def compute():
    """
    计算最大权值回路
    
    Returns:
        最大权值和
    
    时间复杂度分析：
    外层三层循环i、j和s分别遍历n行、m列和2^(m+1)个状态，复杂度为O(n*m*2^m)
    状态转移是常数时间操作
    总时间复杂度：O(n * m * 2^m)
    
    空间复杂度分析：
    使用三维数组存储状态，大小为n*(m+1)*2^(m+1)
    总空间复杂度：O(n * m * 2^m)
    
    最优性判断：
    该算法已达到理论较优复杂度，因为状态数本身就是2^m级别的
    无法进一步优化状态数量
    """
    # 初始化
    for i in range(n):
        for j in range(m + 1):
            for s in range(1 << (m + 1)):
                dp[i][j][s] = float('-inf')  # 表示不可达状态
    
    # 初始状态
    dp[0][0][0] = 0
    
    # 逐格DP
    for i in range(n):
        # 行间转移，将行末状态转移到下一行行首
        for s in range(1 << m):
            if dp[i][m][s] != float('-inf'):
                dp[i + 1][0][s << 1] = dp[i][m][s]
        
        # 行内转移
        for j in range(m):
            for s in range(1 << (m + 1)):
                if dp[i][j][s] == float('-inf'):
                    continue
                
                # 获取当前格子的上插头和左插头
                up = (s >> j) & 1
                left = (s >> (j + 1)) & 1
                
                # 三种转移方式：
                
                # 1. 不放任何插头（前提是上下插头状态相同）
                if up == left:
                    new_state = s & (~((1 << j) | (1 << (j + 1))))
                    dp[i][j + 1][new_state] = max(dp[i][j + 1][new_state], dp[i][j][s] + grid[i][j])
                
                # 2. 生成一对插头（上插头和左插头都不存在）
                if up == 0 and left == 0:
                    new_state = s | (1 << j) | (1 << (j + 1))
                    dp[i][j + 1][new_state] = max(dp[i][j + 1][new_state], dp[i][j][s] + grid[i][j])
                
                # 3. 延续一个插头（上插头和左插头只有一个存在）
                if up != left:
                    # 延续上插头到左插头位置
                    if up == 1:
                        new_state = s & ~(1 << j)
                        new_state |= (1 << (j + 1))
                        dp[i][j + 1][new_state] = max(dp[i][j + 1][new_state], dp[i][j][s] + grid[i][j])
                    # 延续左插头到上插头位置
                    if left == 1:
                        new_state = s & ~(1 << (j + 1))
                        new_state |= (1 << j)
                        dp[i][j + 1][new_state] = max(dp[i][j + 1][new_state], dp[i][j][s] + grid[i][j])
    
    # 返回最大权值和
    result = float('-inf')
    for s in range(1 << (m + 1)):
        if dp[n][0][s] != float('-inf'):
            result = max(result, dp[n][0][s])
    
    return result


# 主函数
if __name__ == "__main__":
    # 读取输入
    line = input().split()
    n = int(line[0])
    m = int(line[1])
    
    for i in range(n):
        line = input().split()
        for j in range(m):
            grid[i][j] = int(line[j])
    
    print(compute())
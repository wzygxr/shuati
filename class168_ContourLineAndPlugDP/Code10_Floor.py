# SCOI2011 地板 (插头DP)
# 题目：用L型地板铺满n×m的房间，求方案数
# 来源：SCOI2011
# 链接：https://www.luogu.com.cn/problem/P3272
# 时间复杂度：O(n * m * 3^m)
# 空间复杂度：O(n * m * 3^m)
# 三种语言实现链接：
# Java: algorithm-journey/src/class125/Code10_Floor.java
# Python: algorithm-journey/src/class125/Code10_Floor.py
# C++: algorithm-journey/src/class125/Code10_Floor.cpp

"""
题目解析：
给定一个n×m的房间，其中有一些格子是障碍物（用'*'表示），
其他格子需要用L型地板铺满。L型地板可以旋转，共有4种旋转方式。
求有多少种不同的铺设方案。

解题思路：
使用插头DP解决这个问题。由于L型地板有拐弯的特性，
我们需要用三进制来表示轮廓线上的插头状态。

状态设计：
dp[i][j][s] 表示处理到第i行第j列，轮廓线状态为s的方案数。
轮廓线状态：用三进制表示轮廓线上每个位置的插头类型
0表示没有插头，1表示有插头且未拐弯，2表示有插头且已拐弯

状态转移：
对于当前格子(i,j)，我们考虑多种转移方式：
1. 不放置任何地板（前提是上下插头都不存在）
2. 放置一个L型地板，根据插头的不同状态进行转移

最优性分析：
该解法是最优的，因为：
1. 时间复杂度O(n * m * 3^m)在可接受范围内
2. 空间复杂度O(n * m * 3^m)合理
3. 状态转移清晰，没有冗余计算

边界场景处理：
1. 当遇到障碍物时，只能不放置地板
2. 当到达边界时，需要特殊处理
3. 结果对20110520取模

工程化考量：
1. 使用三维列表存储DP状态
2. 使用列表推导式初始化dp数组
3. 对于特殊情况进行了预处理优化

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

# 插头DP解决L型地板铺设问题
# 状态表示：dp[i][j][s] 表示处理到第i行第j列，轮廓线状态为s的方案数
# 轮廓线状态：用三进制表示轮廓线上每个位置的插头类型
# 0表示没有插头，1表示有插头且未拐弯，2表示有插头且已拐弯

MAXN = 10
MAXM = 10
MAX_STATES = 59049  # 3^10
# 使用三维列表存储dp状态
dp = [[[0 for _ in range(MAX_STATES)] for _ in range(MAXM + 1)] for _ in range(MAXN)]
grid = [['' for _ in range(MAXM)] for _ in range(MAXN)]
n, m = 0, 0


def get(s, j):
    """
    获取状态s中第j个位置的插头类型
    
    Args:
        s: 状态值
        j: 位置索引
    
    Returns:
        插头类型（0-无插头，1-未拐弯，2-已拐弯）
    """
    pow_val = 1
    for i in range(j):
        pow_val *= 3
    return (s // pow_val) % 3


def set_state(s, j, v):
    """
    设置状态s中第j个位置的插头类型为v
    
    Args:
        s: 状态值
        j: 位置索引
        v: 插头类型
    
    Returns:
        新的状态值
    """
    pow_val = 1
    for i in range(j):
        pow_val *= 3
    return (s // pow_val // 3) * pow_val * 3 + v * pow_val + (s % pow_val)


def power(base, exp):
    """
    计算base^exp
    
    Args:
        base: 底数
        exp: 指数
    
    Returns:
        base的exp次方
    """
    result = 1
    for i in range(exp):
        result *= base
    return result


def compute():
    """
    计算L型地板铺设方案数
    
    Returns:
        铺设方案数
    
    时间复杂度分析：
    外层三层循环i、j和s分别遍历n行、m列和3^m个状态，复杂度为O(n*m*3^m)
    状态转移是常数时间操作
    总时间复杂度：O(n * m * 3^m)
    
    空间复杂度分析：
    使用三维数组存储状态，大小为n*(m+1)*3^m
    总空间复杂度：O(n * m * 3^m)
    
    最优性判断：
    该算法已达到理论较优复杂度，因为状态数本身就是3^m级别的
    无法进一步优化状态数量
    """
    MOD = 20110520
    
    # 初始化
    for i in range(n):
        for j in range(m + 1):
            for s in range(MAX_STATES):
                dp[i][j][s] = 0
    
    # 初始状态
    dp[0][0][0] = 1
    
    # 逐格DP
    for i in range(n):
        # 行间转移，将行末状态转移到下一行行首
        for s in range(power(3, m)):
            # 将状态s左移一位（相当于轮廓线下移一行）
            new_state = s * 3
            dp[i + 1][0][new_state] = dp[i][m][s]
        
        # 行内转移
        for j in range(m):
            for s in range(power(3, m)):
                if dp[i][j][s] == 0:
                    continue
                
                # 获取当前格子左边和上面的插头类型
                left = get(s, j - 1) if j > 0 else 0
                up = get(s, j)
                
                # 如果当前格子是障碍物
                if grid[i][j] == '*':
                    # 只有当上下插头都不存在时才能转移
                    if up == 0 and left == 0:
                        dp[i][j + 1][s] = (dp[i][j + 1][s] + dp[i][j][s]) % MOD
                else:
                    # 当前格子不是障碍物
                    # 多种转移方式：
                    
                    # 1. 不放置任何地板（前提是上下插头都不存在）
                    if up == 0 and left == 0:
                        new_state = s
                        dp[i][j + 1][new_state] = (dp[i][j + 1][new_state] + dp[i][j][s]) % MOD
                    
                    # 2. 放置一个L型地板，左插头未拐弯，上插头不存在
                    if left == 1 and up == 0 and j + 1 < m:
                        new_state = set_state(s, j - 1, 0)  # 左边插头消失
                        new_state = set_state(new_state, j, 1)  # 上面位置生成未拐弯插头
                        new_state = set_state(new_state, j + 1, 2)  # 右边位置生成已拐弯插头
                        dp[i][j + 1][new_state] = (dp[i][j + 1][new_state] + dp[i][j][s]) % MOD
                    
                    # 3. 放置一个L型地板，上插头未拐弯，左插头不存在
                    if up == 1 and left == 0 and j + 1 < m:
                        new_state = set_state(s, j, 0)  # 上面插头消失
                        new_state = set_state(new_state, j, 1)  # 上面位置生成未拐弯插头（延续）
                        new_state = set_state(new_state, j + 1, 2)  # 右边位置生成已拐弯插头
                        dp[i][j + 1][new_state] = (dp[i][j + 1][new_state] + dp[i][j][s]) % MOD
                    
                    # 4. 放置一个L型地板，左插头已拐弯，上插头不存在
                    if left == 2 and up == 0:
                        new_state = set_state(s, j - 1, 0)  # 左边插头消失
                        new_state = set_state(new_state, j, 2)  # 上面位置生成已拐弯插头
                        dp[i][j + 1][new_state] = (dp[i][j + 1][new_state] + dp[i][j][s]) % MOD
                    
                    # 5. 放置一个L型地板，上插头已拐弯，左插头不存在
                    if up == 2 and left == 0:
                        new_state = set_state(s, j, 0)  # 上面插头消失
                        new_state = set_state(new_state, j - 1, 2)  # 左边位置生成已拐弯插头
                        dp[i][j + 1][new_state] = (dp[i][j + 1][new_state] + dp[i][j][s]) % MOD
                    
                    # 6. 放置一个L型地板，左插头未拐弯，上插头未拐弯
                    if left == 1 and up == 1:
                        new_state = set_state(s, j - 1, 0)  # 左边插头消失
                        new_state = set_state(new_state, j, 0)  # 上面插头消失
                        dp[i][j + 1][new_state] = (dp[i][j + 1][new_state] + dp[i][j][s]) % MOD
                    
                    # 7. 放置一个L型地板，左插头已拐弯，上插头未拐弯
                    if left == 2 and up == 1:
                        new_state = set_state(s, j - 1, 0)  # 左边插头消失
                        new_state = set_state(new_state, j, 0)  # 上面插头消失
                        dp[i][j + 1][new_state] = (dp[i][j + 1][new_state] + dp[i][j][s]) % MOD
                    
                    # 8. 放置一个L型地板，左插头未拐弯，上插头已拐弯
                    if left == 1 and up == 2:
                        new_state = set_state(s, j - 1, 0)  # 左边插头消失
                        new_state = set_state(new_state, j, 0)  # 上面插头消失
                        dp[i][j + 1][new_state] = (dp[i][j + 1][new_state] + dp[i][j][s]) % MOD
                    
                    # 9. 放置一个L型地板，左插头已拐弯，上插头已拐弯
                    if left == 2 and up == 2:
                        new_state = set_state(s, j - 1, 0)  # 左边插头消失
                        new_state = set_state(new_state, j, 0)  # 上面插头消失
                        dp[i][j + 1][new_state] = (dp[i][j + 1][new_state] + dp[i][j][s]) % MOD
    
    return dp[n][0][0]

# 主函数
if __name__ == "__main__":
    # 读取输入
    n, m = map(int, input().split())
    
    for i in range(n):
        line = input().strip()
        for j in range(m):
            grid[i][j] = line[j]
    
    print(compute())
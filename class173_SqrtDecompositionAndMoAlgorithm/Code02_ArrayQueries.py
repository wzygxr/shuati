# 数组查询问题 - 分块算法实现 (Python版本)
# 题目来源: https://www.luogu.com.cn/problem/CF797E
# 题目来源: https://codeforces.com/problemset/problem/797/E
# 题目大意: 给定一个长度为n的数组arr，支持查询操作：
# 查询 p k : 从位置p开始，每次跳跃arr[p] + k步，直到越界，返回跳跃次数
# 约束条件: 
# 1 <= n、q <= 10^5
# 1 <= arr[i] <= n

# 相关解答:
# C++版本: class175/Code02_ArrayQueries.cpp
# Java版本: class175/Code02_ArrayQueries.java
# Python版本: class175/Code02_ArrayQueries.py

# 分块算法分析:
# - 时间复杂度：预处理O(n*sqrt(n)) + 查询O(q*sqrt(n))
# - 空间复杂度：O(n*sqrt(n))
# - 分块思想：将k分为k<=sqrt(n)和k>sqrt(n)两种情况处理
#   - 当k<=sqrt(n)时：预处理所有位置p和k的结果，查询时间O(1)
#   - 当k>sqrt(n)时：直接暴力计算，由于k>sqrt(n)，每次跳跃步长至少sqrt(n)+1
#     因此最多只会跳sqrt(n)次，时间复杂度O(sqrt(n))

import math
import sys

# 定义最大数组长度和块大小
MAXN = 100001
MAXB = 401

# 全局变量
n, q, blen = 0, 0, 0
arr = [0] * MAXN
dp = [[0 for _ in range(MAXB)] for _ in range(MAXN)]

def query(p, k):
    """
    查询操作
    从位置p开始，每次跳跃arr[p] + k步，直到越界，返回跳跃次数
    
    算法策略：
    - 当k <= sqrt(n)时：使用预处理的结果，时间复杂度O(1)
    - 当k > sqrt(n)时：暴力计算，由于每次跳跃步长至少为sqrt(n)+1，
      最多只会跳sqrt(n)次，因此时间复杂度为O(sqrt(n))
    
    参数:
        p: 起始位置（从1开始索引）
        k: 跳跃增量
    返回:
        跳跃次数
    """
    # 当k较小时(k <= sqrt(n))，直接使用预处理结果，O(1)时间
    if k <= blen:
        return dp[p][k]
    
    # 当k较大时(k > sqrt(n))，暴力计算
    # 由于每次跳跃步长至少为sqrt(n)+1，最多只会执行sqrt(n)次跳跃
    # 因此时间复杂度为O(sqrt(n))
    ans = 0
    while p <= n:  # 当位置p未越界时继续跳跃
        ans += 1    # 跳跃次数加1
        p += arr[p] + k  # 计算下一个位置：当前位置 + 当前位置的值 + k
    return ans

def prepare():
    """
    预处理函数
    对于所有k <= sqrt(n)的情况，预处理dp[p][k]的值
    
    预处理策略：
    - 计算块大小blen = sqrt(n)
    - 从后往前遍历所有位置p (从n到1)
    - 对每个位置p，计算所有k <= blen对应的dp[p][k]
    - 使用动态规划的思路：dp[p][k] = 1 + (如果下一步越界则0，否则dp[p + arr[p] + k][k])
    
    实现细节：
    - 从后往前计算是关键，这样可以确保在计算dp[p][k]时，dp[p + arr[p] + k][k]已经计算过了
    - dp[p][k]表示从位置p开始，每次跳跃增量k时的总跳跃次数
    - 时间复杂度：O(n*sqrt(n))，虽然预处理时间较高，但可以确保后续查询操作的高效性
    """
    global blen
    # 计算块大小，通常选择sqrt(n)作为分块的阈值
    blen = int(math.sqrt(n))
    
    # 从后往前计算dp值 - 这是动态规划的关键部分
    # 从数组末尾开始，向数组头部遍历
    # 这样设计可以确保在计算dp[p][k]时，dp[p + arr[p] + k][k]已经被计算过了
    for p in range(n, 0, -1):
        # 对每个k <= sqrt(n)的情况进行预处理
        for k in range(1, blen + 1):
            # 计算从位置p跳一步后的新位置
            next_pos = p + arr[p] + k
            
            # 动态规划转移方程：
            # - 如果下一步越界(n+1或更大)，则dp[p][k] = 1（只跳转一次）
            # - 否则dp[p][k] = 1 + dp[next_pos][k]（一次跳转加上从next_pos开始的跳转次数）
            dp[p][k] = 1 + (0 if next_pos > n else dp[next_pos][k])

def main():
    """
    主函数
    程序执行流程：
    1. 读取数组长度n
    2. 读取初始数组arr的值
    3. 调用prepare()函数进行预处理
    4. 读取查询次数q
    5. 处理每个查询，输出结果
    
    注意事项：
    - 数组索引从1开始，而不是0
    - 输入数据通过标准输入读取
    - 每个查询的结果立即输出
    """
    global n, q
    
    # 读取数组长度n
    n = int(sys.stdin.readline())
    
    # 读取初始数组，注意Python中的列表是0索引，但我们的实现中使用1索引
    values = list(map(int, sys.stdin.readline().split()))
    for i in range(1, n + 1):
        arr[i] = values[i - 1]  # 将0索引的输入值复制到1索引的数组中
    
    # 进行预处理，为所有k <= sqrt(n)的情况预计算dp数组
    prepare()
    
    # 读取查询次数q
    q = int(sys.stdin.readline())
    
    # 处理q次查询
    # 对于每个查询，读取起始位置p和跳跃增量k，然后调用query函数计算结果并输出
    for _ in range(q):
        p, k = map(int, sys.stdin.readline().split())
        print(query(p, k))  # 输出查询结果

if __name__ == "__main__":
    main()
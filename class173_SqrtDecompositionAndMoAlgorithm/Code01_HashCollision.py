# 哈希冲突问题 - 分块算法实现 (Python版本)
# 题目来源: https://www.luogu.com.cn/problem/P3396
# 题目大意: 给定一个长度为n的数组arr，支持两种操作：
# 1. 查询操作 A x y: 查询所有满足 i % x == y 的位置i对应的arr[i]之和
# 2. 更新操作 C x y: 将arr[x]的值更新为y
# 约束条件: 1 <= n、m <= 1.5 * 10^5
# 相关解答: 
# - C++版本: Code01_HashCollision.cpp
# - Java版本: Code01_HashCollision1.java, Code01_HashCollision2.java
# - Python版本: Code01_HashCollision.py

import math
import sys

# 定义常量
MAXN = 150001    # 最大数组长度
MAXB = 401       # 最大块大小，约等于sqrt(MAXN)

# 全局变量
n, m, blen = 0, 0, 0            # n: 数组长度, m: 操作次数, blen: 块大小
arr = [0] * MAXN                # 原始数组
# dp[x][y]: 存储所有满足i % x == y的arr[i]之和，仅预处理x <= blen的情况
dp = [[0 for _ in range(MAXB)] for _ in range(MAXB)]

def query(x, y):
    """
    查询操作 A x y
    查询所有满足 i % x == y 的位置i对应的arr[i]之和
    
    参数:
        x: 除数
        y: 余数
    返回:
        满足条件的位置对应的元素之和
    
    算法策略:
    - 对于x <= sqrt(n)的情况: O(1)时间复杂度，直接返回预处理好的dp[x][y]
    - 对于x > sqrt(n)的情况: O(n/x)时间复杂度，由于x较大，最多执行sqrt(n)次循环
    """
    # 当x较小时(x <= blen)，直接使用预处理结果，O(1)时间
    if x <= blen:
        return dp[x][y]
    
    # 当x较大时(x > blen)，暴力枚举所有满足条件的位置
    # 由于x > sqrt(n)，所以最多执行n/x < sqrt(n)次循环，总时间复杂度为O(sqrt(n))
    ans = 0
    i = y
    while i <= n:
        ans += arr[i]
        i += x
    return ans

def update(i, v):
    """
    更新操作 C x y
    将arr[x]的值更新为y，并更新相关的预处理结果
    
    参数:
        i: 要更新的位置
        v: 新的值
    
    算法策略:
    - 计算值的变化量delta
    - 更新原始数组arr[i]
    - 更新所有受影响的预处理结果
    - 时间复杂度: O(sqrt(n))，因为只需要更新x <= sqrt(n)的预处理结果
    """
    # 计算值的变化量
    delta = v - arr[i]
    
    # 更新原始数组
    arr[i] = v
    
    # 更新所有相关的预处理结果
    # 只需要更新x <= sqrt(n)的情况，因为这些情况被预处理了
    # 对于每个x <= blen，位置i对x的余数是i % x，所以需要更新dp[x][i % x]
    for x in range(1, blen + 1):
        dp[x][i % x] += delta

def prepare():
    """
    预处理函数
    对于所有x <= sqrt(n)的情况，预处理dp[x][y]的值
    
    预处理策略:
    - 计算块大小blen = sqrt(n)
    - 对每个x (1 <= x <= blen)，计算所有可能的余数y (0 <= y < x)对应的arr[i]之和
    - 时间复杂度: O(n*sqrt(n))
    """
    global blen
    # 计算块大小，通常选择sqrt(n)
    blen = int(math.sqrt(n))
    
    # dp数组已在全局变量定义时初始化为0
    
    # 对于每个x <= sqrt(n)，预处理dp[x][y]的值
    for x in range(1, blen + 1):
        for i in range(1, n + 1):
            # 计算位置i对x取余的结果y
            y = i % x
            # 将arr[i]累加到dp[x][y]中
            # 这样dp[x][y]就存储了所有满足i % x == y的arr[i]之和
            dp[x][y] += arr[i]

def main():
    """
    主函数
    读取输入数据，初始化数组，预处理数据，处理所有操作
    """
    global n, m
    # 读取数组长度n和操作次数m
    n, m = map(int, sys.stdin.readline().split())
    
    # 读取初始数组元素
    values = list(map(int, sys.stdin.readline().split()))
    for i in range(1, n + 1):
        arr[i] = values[i - 1]  # 注意数组索引从1开始
    
    # 进行预处理，计算dp数组的值
    prepare()
    
    # 处理m次操作
    for _ in range(m):
        # 读取操作行并分割
        line = sys.stdin.readline().split()
        op = line[0]    # 操作类型
        x = int(line[1])  # 第一个参数
        y = int(line[2])  # 第二个参数
        
        if op == 'A':
            # 查询操作: 计算并输出满足条件的位置对应的元素之和
            print(query(x, y))
        else:
            # 更新操作: 将位置x的值更新为y
            update(x, y)

if __name__ == "__main__":
    main()
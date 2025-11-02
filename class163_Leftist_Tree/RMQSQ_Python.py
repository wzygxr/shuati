#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
SPOJ RMQSQ - Range Minimum Query
题目链接：https://www.spoj.com/problems/RMQSQ/

问题描述：
给定一个长度为N的数组，进行M次查询，每次查询区间[L,R]内的最小值。

解题思路：
使用Sparse Table（稀疏表）算法，也叫ST算法。
这是一种基于动态规划的预处理算法，可以在O(N log N)时间内预处理，
然后在O(1)时间内回答每次查询。

核心思想：
1. 预处理：用dp[i][j]表示从索引i开始，长度为2^j的区间内的最小值
2. 状态转移方程：dp[i][j] = min(dp[i][j-1], dp[i+2^(j-1)][j-1])
3. 查询：对于区间[L,R]，计算区间长度len=R-L+1，找到k使得2^k <= len
   答案为min(dp[L][k], dp[R-2^k+1][k])

时间复杂度分析：
- 预处理: O(N log N)
- 查询: O(1)
- 总体: O(N log N + M)

空间复杂度分析:
- 存储dp表: O(N log N)

相关题目：
- Java实现：RMQSQ_Java.java
- Python实现：RMQSQ_Python.py
"""


def main():
    """
    主函数
    输入格式：
    第一行包含一个整数N，表示数组长度
    第二行包含N个整数，表示数组元素
    第三行包含一个整数M，表示查询次数
    接下来M行，每行包含两个整数L和R，表示查询区间
    输出格式：
    对于每个查询，输出区间[L,R]内的最小值
    """
    import sys
    
    # 读取输入
    input = sys.stdin.read
    data = input().split()
    
    # 解析数据
    idx = 0
    n = int(data[idx])
    idx += 1
    
    # 读取数组元素
    arr = []
    for i in range(n):
        arr.append(int(data[idx]))
        idx += 1
    
    # 预处理log表
    # log_table[i]表示floor(log2(i))
    log_table = [0] * (n + 1)
    for i in range(2, n + 1):
        log_table[i] = log_table[i >> 1] + 1
    
    # 计算需要的log最大值
    max_log = 0
    temp_n = n
    while temp_n > 0:
        max_log += 1
        temp_n >>= 1
    
    # 构建Sparse Table
    # st[i][j]表示从索引i开始，长度为2^j的区间内的最小值
    st = [[0] * max_log for _ in range(n)]
    
    # 初始化长度为1的区间
    for i in range(n):
        st[i][0] = arr[i]
    
    # 动态规划填表
    j = 1
    while (1 << j) <= n:
        i = 0
        while i + (1 << j) <= n:
            st[i][j] = min(st[i][j - 1], st[i + (1 << (j - 1))][j - 1])
            i += 1
        j += 1
    
    # 读取查询次数
    q = int(data[idx])
    idx += 1
    
    # 处理每次查询
    for _ in range(q):
        L = int(data[idx])
        idx += 1
        R = int(data[idx])
        idx += 1
        
        # 查询区间[L,R]内的最小值
        length = R - L + 1
        k = log_table[length]  # 找到最大的k使得2^k <= length
        result = min(st[L][k], st[R - (1 << k) + 1][k])
        
        # 输出查询结果
        print(result)


if __name__ == "__main__":
    main()
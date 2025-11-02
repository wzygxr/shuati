# POJ 3264 - Balanced Lineup
# 题目来源：POJ
# 题目链接：http://poj.org/problem?id=3264
# 题目大意：
# 给定N头奶牛，每头奶牛有一个高度。有Q个查询，每个查询给出一个区间[l,r]，
# 要求找出这个区间内最高的奶牛和最矮的奶牛的高度差。
#
# 解题思路：
# 这是一个经典的RMQ（Range Maximum/Minimum Query）问题。
# 我们可以使用Sparse Table来预处理区间最大值和最小值，然后在O(1)时间内回答每个查询。
#
# 核心思想：
# 1. 使用Sparse Table预处理区间最大值和最小值
# 2. 对于每个查询[l,r]，分别查询区间内的最大值和最小值，然后计算差值
#
# 时间复杂度分析：
# - 预处理：O(n log n)
# - 查询：O(1)
# - 总时间复杂度：O(n log n + q)
#
# 空间复杂度分析：
# - O(n log n)
#
# 是否为最优解：
# 是的，对于静态数组的RMQ问题，Sparse Table是最优解之一，因为它可以实现O(1)的查询时间复杂度。
# 另一种选择是线段树，但线段树的查询时间复杂度是O(log n)。

import sys
import math

def main():
    # 读取奶牛数量和查询数量
    line = sys.stdin.readline().split()
    n = int(line[0])
    q = int(line[1])
    
    # 读取每头奶牛的高度
    height = [0] * (n + 1)
    for i in range(1, n + 1):
        height[i] = int(sys.stdin.readline())
    
    # 预处理log2数组
    log2 = [0] * (n + 1)
    log2[1] = 0
    for i in range(2, n + 1):
        log2[i] = log2[i >> 1] + 1
    
    # Sparse Table数组，stmax[i][j]表示从位置i开始，长度为2^j的区间的最大值
    stmax = [[0] * 20 for _ in range(n + 1)]
    
    # Sparse Table数组，stmin[i][j]表示从位置i开始，长度为2^j的区间的最小值
    stmin = [[0] * 20 for _ in range(n + 1)]
    
    # 初始化Sparse Table的第一层（j=0）
    for i in range(1, n + 1):
        stmax[i][0] = height[i]
        stmin[i][0] = height[i]
    
    # 动态规划构建Sparse Table
    for j in range(1, 20):
        for i in range(1, n + 1):
            if i + (1 << j) - 1 <= n:
                stmax[i][j] = max(stmax[i][j - 1], stmax[i + (1 << (j - 1))][j - 1])
                stmin[i][j] = min(stmin[i][j - 1], stmin[i + (1 << (j - 1))][j - 1])
    
    # 查询区间[l,r]内的最大值
    def query_max(l, r):
        k = log2[r - l + 1]
        return max(stmax[l][k], stmax[r - (1 << k) + 1][k])
    
    # 查询区间[l,r]内的最小值
    def query_min(l, r):
        k = log2[r - l + 1]
        return min(stmin[l][k], stmin[r - (1 << k) + 1][k])
    
    # 处理每个查询
    for _ in range(q):
        line = sys.stdin.readline().split()
        l = int(line[0])
        r = int(line[1])
        max_val = query_max(l, r)
        min_val = query_min(l, r)
        print(max_val - min_val)

if __name__ == "__main__":
    main()
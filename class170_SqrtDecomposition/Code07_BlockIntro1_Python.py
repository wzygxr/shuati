# 数列分块入门1 - Python实现
# 题目：给出一个长为n的数列，以及n个操作，操作涉及区间加法，单点查值
# 操作0：区间加法 [l, r] + c
# 操作1：单点查值 查询位置x的值
# 测试链接：https://vjudge.net/problem/LibreOJ-6277

import math
import sys

# 最大数组大小
MAXN = 500001

# 原数组
arr = [0] * MAXN

# 块大小和块数量
blockSize = 0
blockNum = 0

# 每个元素所属的块
belong = [0] * MAXN

# 每个块的左右边界
blockLeft = [0] * MAXN
blockRight = [0] * MAXN

# 每个块的懒惰标记（区间加法标记）
lazy = [0] * MAXN

def build(n):
    """
    构建分块结构
    时间复杂度：O(n)
    空间复杂度：O(n)
    """
    global blockSize, blockNum
    
    # 块大小取sqrt(n)
    blockSize = int(math.sqrt(n))
    # 块数量
    blockNum = (n + blockSize - 1) // blockSize
    
    # 计算每个元素属于哪个块
    for i in range(1, n + 1):
        belong[i] = (i - 1) // blockSize + 1
    
    # 计算每个块的左右边界
    for i in range(1, blockNum + 1):
        blockLeft[i] = (i - 1) * blockSize + 1
        blockRight[i] = min(i * blockSize, n)

def add(l, r, c):
    """
    区间加法操作
    时间复杂度：O(√n)
    :param l: 区间左端点
    :param r: 区间右端点
    :param c: 加的值
    """
    belongL = belong[l]  # 左端点所属块
    belongR = belong[r]  # 右端点所属块
    
    # 如果在同一个块内，直接暴力处理
    if belongL == belongR:
        for i in range(l, r + 1):
            arr[i] += c
    else:
        # 处理左端不完整块
        for i in range(l, blockRight[belongL] + 1):
            arr[i] += c
        
        # 处理右端不完整块
        for i in range(blockLeft[belongR], r + 1):
            arr[i] += c
        
        # 处理中间的完整块，使用懒惰标记
        for i in range(belongL + 1, belongR):
            lazy[i] += c

def query(x):
    """
    单点查询
    时间复杂度：O(1)
    :param x: 查询位置
    :return: 位置x的值
    """
    # 实际值 = 原值 + 所属块的懒惰标记
    return arr[x] + lazy[belong[x]]

def main():
    # 读取数组长度
    n = int(input())
    
    # 读取数组元素
    elements = list(map(int, input().split()))
    for i in range(1, n + 1):
        arr[i] = elements[i - 1]
    
    # 构建分块结构
    build(n)
    
    # 处理操作
    for _ in range(n):
        operation = list(map(int, input().split()))
        op = operation[0]
        l = operation[1]
        r = operation[2]
        
        if op == 0:
            # 区间加法操作
            c = operation[3]
            add(l, r, c)
        else:
            # 单点查询操作
            print(query(r))

if __name__ == "__main__":
    main()
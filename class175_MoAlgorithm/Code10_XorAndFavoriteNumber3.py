# -*- coding: utf-8 -*-

"""
经典莫队应用：异或和为k的子区间个数
给定一个长度为n的数组和一个值k，有m次查询
每次查询[l,r]区间内，有多少个子区间[l'<=l, r'>=r]满足异或和等于k
1 <= n, m <= 100000
1 <= k, arr[i] <= 1000000
测试链接 : https://codeforces.com/contest/617/problem/E

这是普通莫队的经典应用
核心思想：
1. 使用前缀异或和将问题转化为"区间内两个相等元素的个数"问题
2. 如果 pre[i] ^ pre[j] = k，则 pre[i] ^ k = pre[j]
3. 所以我们只需要统计有多少对 (i,j) 满足 pre[i] ^ k = pre[j]
"""

import sys
import math

# 常量定义
MAXN = 100001
MAXV = 1 << 20  # 2^20 > 1000000

# 全局变量
n, m, k = 0, 0, 0
# 原始数组
arr = [0] * MAXN
# 前缀异或和数组
prefix = [0] * MAXN
# 查询: l, r, id
queries = [[0, 0, 0] for _ in range(MAXN)]

# 分块相关
blockSize = 0
blockNum = 0
belong = [0] * MAXN
blockRight = [0] * MAXN

# 计数数组，记录每个异或值出现的次数
count = [0] * MAXV
# 当前答案
currentAnswer = 0
answers = [0] * MAXN


# 普通莫队排序规则
def QueryComparator(a, b):
    # 按照左端点所在块排序
    if belong[a[0]] != belong[b[0]]:
        return belong[a[0]] - belong[b[0]]
    # 同一块内按照右端点排序
    return a[1] - b[1]


# 添加元素到窗口右侧
def addRight(pos):
    global currentAnswer
    val = prefix[pos]
    # 增加与当前值异或为k的值的配对数
    currentAnswer += count[val ^ k]
    # 更新计数
    count[val] += 1


# 从窗口右侧删除元素
def removeRight(pos):
    global currentAnswer
    val = prefix[pos]
    # 更新计数
    count[val] -= 1
    # 减少与当前值异或为k的值的配对数
    currentAnswer -= count[val ^ k]


# 添加元素到窗口左侧
def addLeft(pos):
    global currentAnswer
    val = prefix[pos - 1]
    # 增加与当前值异或为k的值的配对数
    currentAnswer += count[val ^ k]
    # 更新计数
    count[val] += 1


# 从窗口左侧删除元素
def removeLeft(pos):
    global currentAnswer
    val = prefix[pos - 1]
    # 更新计数
    count[val] -= 1
    # 减少与当前值异或为k的值的配对数
    currentAnswer -= count[val ^ k]


# 主计算函数
def compute():
    global currentAnswer
    # 初始化计数数组
    for i in range(MAXV):
        count[i] = 0
    currentAnswer = 0

    l, r = 1, 0

    for i in range(1, m + 1):
        ql = queries[i][0]
        qr = queries[i][1]
        id = queries[i][2]

        # 调整窗口边界
        while r < qr:
            r += 1
            addRight(r)
        while r > qr:
            removeRight(r)
            r -= 1
        while l < ql:
            removeLeft(l)
            l += 1
        while l > ql:
            l -= 1
            addLeft(l)

        answers[id] = currentAnswer


# 预处理函数
def prepare():
    global n, m, k, blockSize, blockNum
    # 计算前缀异或和
    prefix[0] = 0
    for i in range(1, n + 1):
        prefix[i] = prefix[i - 1] ^ arr[i]

    # 计算分块大小
    blockSize = int(math.sqrt(n))
    blockNum = (n + blockSize - 1) // blockSize

    # 计算每个位置所属的块和块的右边界
    for i in range(1, n + 1):
        belong[i] = (i - 1) // blockSize + 1
    for i in range(1, blockNum + 1):
        blockRight[i] = min(i * blockSize, n)

    # 对查询进行排序
    queries[1:m+1] = sorted(queries[1:m+1], key=lambda x: (belong[x[0]], x[1]))


def main():
    global n, m, k
    # 读取输入
    line = sys.stdin.readline().split()
    n, m, k = int(line[0]), int(line[1]), int(line[2])

    # 读取数组
    nums = list(map(int, sys.stdin.readline().split()))
    for i in range(1, n + 1):
        arr[i] = nums[i - 1]

    # 读取查询
    for i in range(1, m + 1):
        line = sys.stdin.readline().split()
        queries[i][0] = int(line[0])
        queries[i][1] = int(line[1])
        queries[i][2] = i

    prepare()
    compute()

    # 输出结果
    for i in range(1, m + 1):
        print(answers[i])


if __name__ == "__main__":
    main()
# -*- coding: utf-8 -*-

"""
回滚莫队应用：区间内相同值的数对个数
给定一个长度为n的数组，有m次查询
每次查询[l,r]区间内，值相同的数对个数
数对定义为(i,j)满足l<=i<j<=r且arr[i]=arr[j]
1 <= n, m <= 100000
1 <= arr[i] <= 1000000

回滚莫队的经典应用
核心思想：
1. 只能扩展右边界，不能收缩右边界
2. 可以收缩左边界，但需要通过回滚来恢复
3. 利用组合数学，C(n,2) = n*(n-1)/2
"""

import sys
import math

# 常量定义
MAXN = 100001
MAXV = 1000001

# 全局变量
n, m = 0, 0
# 原始数组
arr = [0] * MAXN
# 离散化后的数组
sorted_arr = [0] * MAXN
valueCount = 0

# 查询: l, r, id
queries = [[0, 0, 0] for _ in range(MAXN)]

# 分块相关
blockSize = 0
blockNum = 0
belong = [0] * MAXN
blockRight = [0] * MAXN

# 计数和答案
count = [0] * MAXV  # 每个值的出现次数
currentAnswer = 0
answers = [0] * MAXN


# 回滚莫队排序规则
def QueryComparator(a, b):
    # 按照左端点所在块排序
    if belong[a[0]] != belong[b[0]]:
        return belong[a[0]] - belong[b[0]]
    # 同一块内按照右端点排序
    return a[1] - b[1]


# 二分查找离散化值
def findIndex(value):
    left, right, result = 1, valueCount, 0
    while left <= right:
        mid = (left + right) // 2
        if sorted_arr[mid] <= value:
            result = mid
            left = mid + 1
        else:
            right = mid - 1
    return result


# 暴力计算区间答案
def bruteForce(l, r):
    result = 0
    # 统计每个值的出现次数
    for i in range(l, r + 1):
        count[arr[i]] += 1
    # 计算数对个数
    for i in range(l, r + 1):
        result += count[arr[i]] - 1  # 该位置的值能组成的数对数
    # 清除计数
    for i in range(l, r + 1):
        count[arr[i]] = 0
    return result // 2  # 每个数对被计算了两次


# 添加元素到右侧
def add(value):
    global currentAnswer
    # 增加该值能组成的数对数
    currentAnswer += count[value]
    count[value] += 1


# 从左侧删除元素
def remove(value):
    global currentAnswer
    count[value] -= 1
    # 减少该值能组成的数对数
    currentAnswer -= count[value]


# 主计算函数
def compute():
    global currentAnswer
    block = 1
    queryIndex = 1
    while block <= blockNum and queryIndex <= m:
        # 每个块开始时重置状态
        currentAnswer = 0
        for i in range(MAXV):
            count[i] = 0

        # 当前窗口边界
        windowLeft = blockRight[block] + 1
        windowRight = blockRight[block]

        # 处理属于当前块的所有查询
        while queryIndex <= m and belong[queries[queryIndex][0]] == block:
            queryLeft = queries[queryIndex][0]
            queryRight = queries[queryIndex][1]
            id = queries[queryIndex][2]

            # 如果查询区间完全在当前块内，使用暴力方法
            if queryRight <= blockRight[block]:
                answers[id] = bruteForce(queryLeft, queryRight)
            else:
                # 否则使用回滚莫队
                # 先扩展右边界到queryRight
                while windowRight < queryRight:
                    windowRight += 1
                    add(arr[windowRight])

                # 保存当前状态
                backup = currentAnswer

                # 扩展左边界到queryLeft
                while windowLeft > queryLeft:
                    windowLeft -= 1
                    add(arr[windowLeft])

                # 记录答案
                answers[id] = currentAnswer

                # 恢复状态，只保留右边界扩展的结果
                currentAnswer = backup
                while windowLeft <= blockRight[block]:
                    remove(arr[windowLeft])
                    windowLeft += 1

            queryIndex += 1

        block += 1


# 预处理函数
def prepare():
    global n, m, blockSize, blockNum, valueCount
    # 离散化
    for i in range(1, n + 1):
        sorted_arr[i] = arr[i]
    sorted_arr[1:n+1] = sorted(sorted_arr[1:n+1])
    valueCount = 1
    for i in range(2, n + 1):
        if sorted_arr[valueCount] != sorted_arr[i]:
            valueCount += 1
            sorted_arr[valueCount] = sorted_arr[i]
    for i in range(1, n + 1):
        arr[i] = findIndex(arr[i])

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
    global n, m
    # 读取输入
    line = sys.stdin.readline().split()
    n, m = int(line[0]), int(line[1])

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
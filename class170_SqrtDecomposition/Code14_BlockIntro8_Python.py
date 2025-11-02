# 数列分块入门8 - Python实现
# 题目来源：LibreOJ #6284 数列分块入门8
# 题目链接：https://loj.ac/p/6284
# 题目描述：给出一个长为n的数列，以及n个操作，操作涉及区间询问等于一个数c的元素，并将这个区间的所有元素改为c
# 操作：区间询问等于一个数c的元素个数，并将区间[l,r]的所有元素改为c
# 解题思路：
# 1. 使用分块算法，将数组分成sqrt(n)大小的块
# 2. 每个块维护一个标记，表示整个块是否都是同一个值
# 3. 对于区间操作，不完整块下传标记后直接处理，完整块根据标记优化处理
# 4. 如果整个块都是同一个值，可以直接计算等于c的元素个数并更新标记
# 5. 否则下传标记后暴力处理
# 时间复杂度：预处理O(n)，区间操作O(√n)均摊
# 空间复杂度：O(n)
# 相关题目：
# 1. LibreOJ #6277 数列分块入门1 - https://loj.ac/p/6277
# 2. LibreOJ #6278 数列分块入门2 - https://loj.ac/p/6278
# 3. LibreOJ #6279 数列分块入门3 - https://loj.ac/p/6279
# 4. LibreOJ #6280 数列分块入门4 - https://loj.ac/p/6280
# 5. LibreOJ #6281 数列分块入门5 - https://loj.ac/p/6281
# 6. LibreOJ #6282 数列分块入门6 - https://loj.ac/p/6282
# 7. LibreOJ #6283 数列分块入门7 - https://loj.ac/p/6283
# 8. LibreOJ #6285 数列分块入门9 - https://loj.ac/p/6285

import math

# 最大数组大小
MAXN = 100001

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

# 每个块的标记，表示整个块是否都是同一个值
tag = [0] * MAXN

def build(n):
    """
    构建分块结构
    时间复杂度：O(n)
    空间复杂度：O(n)
    :param n: 数组长度
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
    
    # 初始化标记数组，-1表示该块不是同一个值
    for i in range(1, blockNum + 1):
        tag[i] = -1

def push_down(blockId):
    """
    下传标记到具体元素（在需要修改具体元素时使用）
    :param blockId: 块编号
    """
    # 如果块有标记（即整个块都是同一个值）
    if tag[blockId] != -1:
        for i in range(blockLeft[blockId], blockRight[blockId] + 1):
            arr[i] = tag[blockId]
        tag[blockId] = -1  # 清除标记

def solve(l, r, c):
    """
    区间操作：统计等于c的元素个数，并将区间所有元素改为c
    均摊时间复杂度：O(√n)
    :param l: 区间左端点
    :param r: 区间右端点
    :param c: 新的值
    :return: 区间内等于c的元素个数
    """
    result = 0
    belongL = belong[l]  # 左端点所属块
    belongR = belong[r]  # 右端点所属块
    
    # 如果在同一个块内，直接暴力处理
    if belongL == belongR:
        # 下传标记
        push_down(belongL)
        # 处理区间内的元素
        for i in range(l, r + 1):
            if arr[i] == c:
                result += 1
            arr[i] = c
    else:
        # 处理左端不完整块
        push_down(belongL)
        for i in range(l, blockRight[belongL] + 1):
            if arr[i] == c:
                result += 1
            arr[i] = c
        
        # 处理右端不完整块
        push_down(belongR)
        for i in range(blockLeft[belongR], r + 1):
            if arr[i] == c:
                result += 1
            arr[i] = c
        
        # 处理中间的完整块
        for i in range(belongL + 1, belongR):
            # 如果整个块都是同一个值
            if tag[i] != -1:
                # 统计等于c的元素个数
                if tag[i] == c:
                    result += blockRight[i] - blockLeft[i] + 1
                # 更新标记
                tag[i] = c
            else:
                # 下传标记并暴力处理
                push_down(i)
                for j in range(blockLeft[i], blockRight[i] + 1):
                    if arr[j] == c:
                        result += 1
                    arr[j] = c
                # 设置新的标记
                tag[i] = c
    
    return result

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
        l = operation[0]
        r = operation[1]
        c = operation[2]
        
        # 区间操作
        print(solve(l, r, c))

if __name__ == "__main__":
    main()
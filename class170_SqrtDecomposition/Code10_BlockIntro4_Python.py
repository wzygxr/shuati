# 数列分块入门4 - Python实现
# 题目来源：LibreOJ #6280 数列分块入门4
# 题目链接：https://loj.ac/p/6280
# 题目描述：给出一个长为n的数列，以及n个操作，操作涉及区间加法，区间求和
# 操作0：区间加法 [l, r] + c
# 操作1：区间求和 [l, r]
# 解题思路：
# 1. 使用分块算法，将数组分成sqrt(n)大小的块
# 2. 每个块维护元素和，用于快速计算区间和
# 3. 对于区间加法操作，不完整块直接更新并重新计算块和，完整块使用懒惰标记并直接更新块和
# 4. 对于查询操作，不完整块直接遍历，完整块直接使用块和计算
# 时间复杂度：预处理O(n)，区间加法操作O(√n)，区间求和操作O(√n)
# 空间复杂度：O(n)
# 相关题目：
# 1. LibreOJ #6277 数列分块入门1 - https://loj.ac/p/6277
# 2. LibreOJ #6278 数列分块入门2 - https://loj.ac/p/6278
# 3. LibreOJ #6279 数列分块入门3 - https://loj.ac/p/6279
# 4. LibreOJ #6281 数列分块入门5 - https://loj.ac/p/6281
# 5. LibreOJ #6282 数列分块入门6 - https://loj.ac/p/6282
# 6. LibreOJ #6283 数列分块入门7 - https://loj.ac/p/6283
# 7. LibreOJ #6284 数列分块入门8 - https://loj.ac/p/6284
# 8. LibreOJ #6285 数列分块入门9 - https://loj.ac/p/6285

import math

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

# 每个块的元素和
sum_blocks = [0] * MAXN

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
    
    # 计算每个块的元素和
    for i in range(1, blockNum + 1):
        sum_blocks[i] = 0
        for j in range(blockLeft[i], blockRight[i] + 1):
            sum_blocks[i] += arr[j]

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
        # 更新该块的元素和
        for i in range(blockLeft[belongL], blockRight[belongL] + 1):
            sum_blocks[belongL] = sum_blocks[belongL] - (arr[i] - c) + arr[i]
    else:
        # 处理左端不完整块
        for i in range(l, blockRight[belongL] + 1):
            arr[i] += c
        # 更新该块的元素和
        for i in range(blockLeft[belongL], blockRight[belongL] + 1):
            sum_blocks[belongL] = sum_blocks[belongL] - (arr[i] - c) + arr[i]
        
        # 处理右端不完整块
        for i in range(blockLeft[belongR], r + 1):
            arr[i] += c
        # 更新该块的元素和
        for i in range(blockLeft[belongR], blockRight[belongR] + 1):
            sum_blocks[belongR] = sum_blocks[belongR] - (arr[i] - c) + arr[i]
        
        # 处理中间的完整块，使用懒惰标记
        for i in range(belongL + 1, belongR):
            lazy[i] += c
            # 直接更新块的元素和
            sum_blocks[i] += c * (blockRight[i] - blockLeft[i] + 1)

def query(l, r):
    """
    查询区间和
    时间复杂度：O(√n)
    :param l: 区间左端点
    :param r: 区间右端点
    :return: 区间和
    """
    result = 0
    belongL = belong[l]  # 左端点所属块
    belongR = belong[r]  # 右端点所属块
    
    # 如果在同一个块内，直接暴力处理
    if belongL == belongR:
        for i in range(l, r + 1):
            result += arr[i] + lazy[belong[i]]
    else:
        # 处理左端不完整块
        for i in range(l, blockRight[belongL] + 1):
            result += arr[i] + lazy[belong[i]]
        
        # 处理右端不完整块
        for i in range(blockLeft[belongR], r + 1):
            result += arr[i] + lazy[belong[i]]
        
        # 处理中间的完整块，直接使用块的元素和
        for i in range(belongL + 1, belongR):
            result += sum_blocks[i] + lazy[i] * (blockRight[i] - blockLeft[i] + 1)
    
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
        op = operation[0]
        l = operation[1]
        r = operation[2]
        
        if op == 0:
            # 区间加法操作
            c = int(operation[3])
            add(l, r, c)
        else:
            # 区间求和操作
            print(query(l, r))

if __name__ == "__main__":
    main()
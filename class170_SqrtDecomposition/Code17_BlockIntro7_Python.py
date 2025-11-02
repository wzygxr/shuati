# 数列分块入门7 - Python实现
# 题目来源：LibreOJ #6283 数列分块入门7
# 题目链接：https://loj.ac/p/6283
# 题目描述：给出一个长为n的数列，以及n个操作，操作涉及区间乘法，区间加法，单点查询
# 操作0：区间乘法 [l, r] * c
# 操作1：区间加法 [l, r] + c
# 操作2：单点查询 查询位置x的值
# 解题思路：
# 1. 使用分块算法，将数组分成sqrt(n)大小的块
# 2. 每个块维护乘法标记和加法标记，实现懒惰传播
# 3. 标记优先级：先乘法后加法，即实际值 = (原值 * 乘法标记 + 加法标记) % MOD
# 4. 对于区间操作，不完整块下传标记后直接更新，完整块使用标记
# 5. 对于单点查询，根据标记计算实际值
# 时间复杂度：预处理O(n)，区间乘法操作O(√n)，区间加法操作O(√n)，单点查询操作O(1)
# 空间复杂度：O(n)
# 相关题目：
# 1. LibreOJ #6277 数列分块入门1 - https://loj.ac/p/6277
# 2. LibreOJ #6278 数列分块入门2 - https://loj.ac/p/6278
# 3. LibreOJ #6279 数列分块入门3 - https://loj.ac/p/6279
# 4. LibreOJ #6280 数列分块入门4 - https://loj.ac/p/6280
# 5. LibreOJ #6281 数列分块入门5 - https://loj.ac/p/6281
# 6. LibreOJ #6282 数列分块入门6 - https://loj.ac/p/6282
# 7. LibreOJ #6284 数列分块入门8 - https://loj.ac/p/6284
# 8. LibreOJ #6285 数列分块入门9 - https://loj.ac/p/6285

import math
import sys

# 最大数组大小
MAXN = 1000005

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

# 每个块的乘法标记和加法标记
mul = [1] * MAXN
add = [0] * MAXN

# 模数
MOD = 10007

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
        # 初始化标记
        mul[i] = 1
        add[i] = 0

def pushDown(block):
    """
    下传标记
    :param block: 块编号
    """
    if mul[block] == 1 and add[block] == 0:
        return
    
    for i in range(blockLeft[block], blockRight[block] + 1):
        arr[i] = (arr[i] * mul[block] + add[block]) % MOD
    
    mul[block] = 1
    add[block] = 0

def multiply(l, r, c):
    """
    区间乘法操作
    时间复杂度：O(√n)
    :param l: 区间左端点
    :param r: 区间右端点
    :param c: 乘的值
    """
    belongL = belong[l]  # 左端点所属块
    belongR = belong[r]  # 右端点所属块
    
    # 如果在同一个块内，直接暴力处理
    if belongL == belongR:
        pushDown(belongL)
        for i in range(l, r + 1):
            arr[i] = (arr[i] * c) % MOD
    else:
        # 处理左端不完整块
        pushDown(belongL)
        for i in range(l, blockRight[belongL] + 1):
            arr[i] = (arr[i] * c) % MOD
        
        # 处理右端不完整块
        pushDown(belongR)
        for i in range(blockLeft[belongR], r + 1):
            arr[i] = (arr[i] * c) % MOD
        
        # 处理中间的完整块
        for i in range(belongL + 1, belongR):
            mul[i] = (mul[i] * c) % MOD
            add[i] = (add[i] * c) % MOD

def addOperation(l, r, c):
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
        pushDown(belongL)
        for i in range(l, r + 1):
            arr[i] = (arr[i] + c) % MOD
    else:
        # 处理左端不完整块
        pushDown(belongL)
        for i in range(l, blockRight[belongL] + 1):
            arr[i] = (arr[i] + c) % MOD
        
        # 处理右端不完整块
        pushDown(belongR)
        for i in range(blockLeft[belongR], r + 1):
            arr[i] = (arr[i] + c) % MOD
        
        # 处理中间的完整块
        for i in range(belongL + 1, belongR):
            add[i] = (add[i] + c) % MOD

def query(x):
    """
    单点查询
    时间复杂度：O(1)
    :param x: 查询位置
    :return: 位置x的值
    """
    # 实际值 = (原值 * 乘法标记 + 加法标记) % MOD
    return (arr[x] * mul[belong[x]] + add[belong[x]]) % MOD

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
            # 区间乘法操作
            c = operation[3]
            multiply(l, r, c)
        elif op == 1:
            # 区间加法操作
            c = operation[3]
            addOperation(l, r, c)
        else:
            # 单点查询操作
            print(query(r))

if __name__ == "__main__":
    main()
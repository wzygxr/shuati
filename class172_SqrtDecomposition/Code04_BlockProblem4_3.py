# LOJ 数列分块入门4 - Python实现
# 题目：区间加法，区间求和
# 链接：https://loj.ac/p/6280
# 题目描述：
# 给出一个长为n的数列，以及n个操作，操作涉及区间加法，区间求和。
# 操作 0 l r c : 将位于[l,r]的之间的数字都加c
# 操作 1 l r c : 询问[l,r]区间的和 mod (c+1)
# 数据范围：1 <= n <= 50000

import math
import sys

# 从标准输入读取数据
input = sys.stdin.read
lines = input().split('\n')

# 读取数组长度
n = int(lines[0])

# 读取数组元素
arr = [0] + list(map(int, lines[1].split()))  # 下标从1开始

# 块的大小和数量
blockSize = int(math.sqrt(n))
blockNum = (n + blockSize - 1) // blockSize

# 每个元素所属的块编号
belong = [0] * (n + 1)

# 每个块的左右边界
blockLeft = [0] * (blockNum + 1)
blockRight = [0] * (blockNum + 1)

# 每个块的懒惰标记（记录整个块增加的值）
lazy = [0] * (blockNum + 1)

# 每个块的元素和
sum_blocks = [0] * (blockNum + 1)

# 初始化分块结构
def build(n):
    """初始化分块结构"""
    global blockSize, blockNum
    
    # 为每个元素分配所属的块
    for i in range(1, n + 1):
        belong[i] = (i - 1) // blockSize + 1
    
    # 计算每个块的左右边界
    for i in range(1, blockNum + 1):
        blockLeft[i] = (i - 1) * blockSize + 1
        blockRight[i] = min(i * blockSize, n)
    
    # 初始化每个块的元素和
    for i in range(1, blockNum + 1):
        sum_blocks[i] = 0
        for j in range(blockLeft[i], blockRight[i] + 1):
            sum_blocks[i] += arr[j]

# 区间加法操作
# 将区间[l,r]中的每个元素都加上val
def update(l, r, val):
    """区间加法操作"""
    belongL = belong[l]  # 左端点所属块
    belongR = belong[r]  # 右端点所属块
    
    # 如果区间在同一个块内，直接暴力处理
    if belongL == belongR:
        # 直接对区间内每个元素加上val
        for i in range(l, r + 1):
            arr[i] += val
        # 更新块的元素和
        sum_blocks[belongL] += val * (r - l + 1)
        return
    
    # 处理左端点所在的不完整块
    for i in range(l, blockRight[belongL] + 1):
        arr[i] += val
    # 更新块的元素和
    sum_blocks[belongL] += val * (blockRight[belongL] - l + 1)
    
    # 处理右端点所在的不完整块
    for i in range(blockLeft[belongR], r + 1):
        arr[i] += val
    # 更新块的元素和
    sum_blocks[belongR] += val * (r - blockLeft[belongR] + 1)
    
    # 处理中间的完整块，使用懒惰标记优化
    for i in range(belongL + 1, belongR):
        lazy[i] += val
        sum_blocks[i] += val * blockSize

# 查询区间[l,r]的和
def query(l, r, mod):
    """查询区间[l,r]的和"""
    belongL = belong[l]  # 左端点所属块
    belongR = belong[r]  # 右端点所属块
    result = 0
    
    # 如果区间在同一个块内，直接暴力统计
    if belongL == belongR:
        for i in range(l, r + 1):
            result += arr[i] + lazy[belong[i]]
        return result % mod
    
    # 处理左端点所在的不完整块
    for i in range(l, blockRight[belongL] + 1):
        result += arr[i] + lazy[belong[i]]
    
    # 处理右端点所在的不完整块
    for i in range(blockLeft[belongR], r + 1):
        result += arr[i] + lazy[belong[i]]
    
    # 处理中间的完整块
    for i in range(belongL + 1, belongR):
        result += sum_blocks[i] + lazy[i] * blockSize
    
    return result % mod

# 主函数
def main():
    # 初始化分块结构
    build(n)
    
    # 存储结果
    results = []
    
    # 处理n个操作
    for i in range(n):
        operation = list(map(int, lines[2 + i].split()))
        op, l, r, c = operation[0], operation[1], operation[2], operation[3]
        
        if op == 0:
            # 区间加法操作
            update(l, r, c)
        else:
            # 查询操作
            results.append(str(query(l, r, c + 1)))
    
    # 输出结果
    print('\n'.join(results))

if __name__ == "__main__":
    main()

'''
算法解析：

时间复杂度分析：
1. 建立分块结构：O(n)
2. 区间更新操作：O(√n) - 最多处理两个不完整块(2*√n)和一些完整块(√n)
3. 区间查询操作：O(√n) - 处理两个不完整块和一些完整块

空间复杂度：O(n) - 存储原数组和分块相关信息

算法思想：
分块是一种"优雅的暴力"算法，通过将数组分成大小约为√n的块来平衡时间复杂度。

核心思想：
1. 对于不完整的块（区间端点所在的块），直接暴力处理
2. 对于完整的块，使用懒惰标记来延迟更新，避免每次都修改块内所有元素
3. 维护每个块的元素和，快速计算完整块的和
4. 查询时，实际值 = 原始值 + 所属块的懒惰标记

优势：
1. 实现相对简单，比线段树等数据结构容易理解和编码
2. 可以处理大多数区间操作问题
3. 对于在线算法有很好的适应性

适用场景：
1. 需要区间修改和区间查询的问题
2. 不适合用线段树等复杂数据结构的场景
3. 对代码复杂度有要求的场景
'''
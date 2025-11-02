# LOJ 数列分块入门3 - Python实现
# 题目：区间加法，查询区间内小于某个值x的前驱（比其小的最大元素）
# 链接：https://loj.ac/p/6279
# 题目描述：
# 给出一个长为n的数列，以及n个操作，操作涉及区间加法，询问区间内小于某个值x的前驱（比其小的最大元素）。
# 操作 0 l r c : 将位于[l,r]的之间的数字都加c
# 操作 1 l r c : 询问[l,r]区间内小于c的前驱（比其小的最大元素）
# 数据范围：1 <= n <= 100000

import math
import bisect
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

# 每个块排序后的元素（用于二分查找）
sortedBlocks = [[] for _ in range(blockNum + 1)]

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
    
    # 初始化每个块的排序数组
    resetAllBlocks(n)

# 重新构建所有块的排序数组
def resetAllBlocks(n):
    """重新构建所有块的排序数组"""
    # 清空每个块的排序数组
    for i in range(1, blockNum + 1):
        sortedBlocks[i].clear()
    
    # 将每个元素添加到对应块的排序数组中
    for i in range(1, n + 1):
        sortedBlocks[belong[i]].append(arr[i])
    
    # 对每个块的排序数组进行排序
    for i in range(1, blockNum + 1):
        sortedBlocks[i].sort()
    
    # 清空懒惰标记
    for i in range(len(lazy)):
        lazy[i] = 0

# 重新构建指定块的排序数组
def resetBlock(blockId):
    """重新构建指定块的排序数组"""
    sortedBlocks[blockId].clear()
    for i in range(blockLeft[blockId], blockRight[blockId] + 1):
        sortedBlocks[blockId].append(arr[i])
    sortedBlocks[blockId].sort()
    lazy[blockId] = 0

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
        # 重构该块的排序数组
        resetBlock(belongL)
        return
    
    # 处理左端点所在的不完整块
    for i in range(l, blockRight[belongL] + 1):
        arr[i] += val
    # 重构该块的排序数组
    resetBlock(belongL)
    
    # 处理右端点所在的不完整块
    for i in range(blockLeft[belongR], r + 1):
        arr[i] += val
    # 重构该块的排序数组
    resetBlock(belongR)
    
    # 处理中间的完整块，使用懒惰标记优化
    for i in range(belongL + 1, belongR):
        lazy[i] += val

# 查询区间[l,r]内小于val的前驱（比其小的最大元素）
def query(l, r, val):
    """查询区间[l,r]内小于val的前驱（比其小的最大元素）"""
    belongL = belong[l]  # 左端点所属块
    belongR = belong[r]  # 右端点所属块
    predecessor = -1
    
    # 如果区间在同一个块内，直接暴力统计
    if belongL == belongR:
        for i in range(l, r + 1):
            actualValue = arr[i] + lazy[belong[i]]
            if actualValue < val and actualValue > predecessor:
                predecessor = actualValue
        return predecessor
    
    # 处理左端点所在的不完整块
    for i in range(l, blockRight[belongL] + 1):
        actualValue = arr[i] + lazy[belong[i]]
        if actualValue < val and actualValue > predecessor:
            predecessor = actualValue
    
    # 处理右端点所在的不完整块
    for i in range(blockLeft[belongR], r + 1):
        actualValue = arr[i] + lazy[belong[i]]
        if actualValue < val and actualValue > predecessor:
            predecessor = actualValue
    
    # 处理中间的完整块，使用二分查找优化
    for i in range(belongL + 1, belongR):
        # 在排序数组中查找小于(val - lazy[i])的最大元素
        target = val - lazy[i]
        # 使用bisect.bisect_left查找第一个大于等于target的位置
        pos = bisect.bisect_left(sortedBlocks[i], target)
        
        # 如果找到了小于target的最大元素，更新predecessor
        if pos > 0:
            actualValue = sortedBlocks[i][pos - 1] + lazy[i]
            if actualValue > predecessor:
                predecessor = actualValue
    
    return predecessor

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
            results.append(str(query(l, r, c)))
    
    # 输出结果
    print('\n'.join(results))

if __name__ == "__main__":
    main()

'''
算法解析：

时间复杂度分析：
1. 建立分块结构：O(n log n) - 需要对每个块进行排序
2. 区间更新操作：O(√n * log n) - 重构两个不完整块的排序数组，处理完整块的懒惰标记
3. 查询操作：O(√n * log n) - 处理两个不完整块，对完整块使用二分查找

空间复杂度：O(n) - 存储原数组、分块信息和排序数组

算法思想：
在分块的基础上，对每个块维护一个排序数组，这样在查询时可以使用二分查找来优化完整块的处理。

核心思想：
1. 对于不完整的块，直接暴力处理
2. 对于完整的块，维护排序数组并使用二分查找
3. 使用懒惰标记优化区间更新操作
4. 当不完整块被修改后，需要重构该块的排序数组

优势：
1. 相比纯暴力方法，大大优化了查询效率
2. 实现相对简单，比线段树等数据结构容易理解和编码
3. 可以处理大多数区间操作问题

适用场景：
1. 需要区间修改和区间查询的问题
2. 查询涉及有序统计的问题（如排名、前驱、后继等）
'''
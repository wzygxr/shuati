# LOJ 数列分块入门5 - Python实现
# 题目：区间开方，区间求和
# 链接：https://loj.ac/p/6281
# 题目描述：
# 给出一个长为n的数列，以及n个操作，操作涉及区间开方（下取整），区间求和。
# 操作 0 l r c : 将位于[l,r]的之间的数字都开方（下取整）
# 操作 1 l r c : 询问[l,r]区间的和
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

# 每个块是否全为0或1的标记
isZeroOne = [False] * (blockNum + 1)

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
    
    # 初始化每个块的元素和和标记
    for i in range(1, blockNum + 1):
        sum_blocks[i] = 0
        isZeroOne[i] = True
        for j in range(blockLeft[i], blockRight[i] + 1):
            sum_blocks[i] += arr[j]
            if arr[j] != 0 and arr[j] != 1:
                isZeroOne[i] = False

# 区间开方操作
# 将区间[l,r]中的每个元素都开方（下取整）
def update(l, r):
    """区间开方操作"""
    belongL = belong[l]  # 左端点所属块
    belongR = belong[r]  # 右端点所属块
    
    # 如果区间在同一个块内，直接暴力处理
    if belongL == belongR:
        # 直接对区间内每个元素开方
        for i in range(l, r + 1):
            sum_blocks[belongL] -= arr[i]
            arr[i] = int(math.sqrt(arr[i]))
            sum_blocks[belongL] += arr[i]
        # 检查块是否全为0或1
        isZeroOne[belongL] = True
        for i in range(blockLeft[belongL], blockRight[belongL] + 1):
            if arr[i] != 0 and arr[i] != 1:
                isZeroOne[belongL] = False
                break
        return
    
    # 处理左端点所在的不完整块
    for i in range(l, blockRight[belongL] + 1):
        sum_blocks[belongL] -= arr[i]
        arr[i] = int(math.sqrt(arr[i]))
        sum_blocks[belongL] += arr[i]
    # 检查块是否全为0或1
    isZeroOne[belongL] = True
    for i in range(blockLeft[belongL], blockRight[belongL] + 1):
        if arr[i] != 0 and arr[i] != 1:
            isZeroOne[belongL] = False
            break
    
    # 处理右端点所在的不完整块
    for i in range(blockLeft[belongR], r + 1):
        sum_blocks[belongR] -= arr[i]
        arr[i] = int(math.sqrt(arr[i]))
        sum_blocks[belongR] += arr[i]
    # 检查块是否全为0或1
    isZeroOne[belongR] = True
    for i in range(blockLeft[belongR], blockRight[belongR] + 1):
        if arr[i] != 0 and arr[i] != 1:
            isZeroOne[belongR] = False
            break
    
    # 处理中间的完整块
    for i in range(belongL + 1, belongR):
        # 如果块已经全为0或1，则无需处理
        if isZeroOne[i]:
            continue
        
        # 对块内每个元素开方
        for j in range(blockLeft[i], blockRight[i] + 1):
            sum_blocks[i] -= arr[j]
            arr[j] = int(math.sqrt(arr[j]))
            sum_blocks[i] += arr[j]
        
        # 检查块是否全为0或1
        isZeroOne[i] = True
        for j in range(blockLeft[i], blockRight[i] + 1):
            if arr[j] != 0 and arr[j] != 1:
                isZeroOne[i] = False
                break

# 查询区间[l,r]的和
def query(l, r):
    """查询区间[l,r]的和"""
    belongL = belong[l]  # 左端点所属块
    belongR = belong[r]  # 右端点所属块
    result = 0
    
    # 如果区间在同一个块内，直接暴力统计
    if belongL == belongR:
        for i in range(l, r + 1):
            result += arr[i]
        return result
    
    # 处理左端点所在的不完整块
    for i in range(l, blockRight[belongL] + 1):
        result += arr[i]
    
    # 处理右端点所在的不完整块
    for i in range(blockLeft[belongR], r + 1):
        result += arr[i]
    
    # 处理中间的完整块
    for i in range(belongL + 1, belongR):
        result += sum_blocks[i]
    
    return result

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
            # 区间开方操作
            update(l, r)
        else:
            # 查询操作
            results.append(str(query(l, r)))
    
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
2. 对于完整的块，使用标记优化，如果块内元素全为0或1则无需处理
3. 维护每个块的元素和，快速计算完整块的和

优化技巧：
利用开方次数有限的特性，当块内元素全为0或1时，无需再进行开方操作。

优势：
1. 实现相对简单，比线段树等数据结构容易理解和编码
2. 利用开方特性进行优化，提高实际运行效率
3. 可以处理大多数区间操作问题

适用场景：
1. 需要区间开方和区间求和的问题
2. 操作具有有限次数特性的场景
'''
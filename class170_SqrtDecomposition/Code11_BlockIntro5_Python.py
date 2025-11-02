# 数列分块入门5 - Python实现
# 题目来源：LibreOJ #6281 数列分块入门5
# 题目链接：https://loj.ac/p/6281
# 题目描述：给出一个长为n的数列，以及n个操作，操作涉及区间开方，区间求和
# 操作0：区间开方 [l, r] 每个元素开方后向下取整
# 操作1：区间求和 [l, r]
# 解题思路：
# 1. 使用分块算法，将数组分成sqrt(n)大小的块
# 2. 每个块维护元素和，用于快速计算区间和
# 3. 每个块维护一个标记，表示块中所有元素是否都变成了0或1（开方后不变）
# 4. 对于区间开方操作，如果块中所有元素都是0或1则无需处理，否则暴力处理
# 5. 对于查询操作，不完整块直接遍历，完整块直接使用块和计算
# 时间复杂度：预处理O(n)，区间开方操作O(√n)均摊，区间求和操作O(√n)
# 空间复杂度：O(n)
# 相关题目：
# 1. LibreOJ #6277 数列分块入门1 - https://loj.ac/p/6277
# 2. LibreOJ #6278 数列分块入门2 - https://loj.ac/p/6278
# 3. LibreOJ #6279 数列分块入门3 - https://loj.ac/p/6279
# 4. LibreOJ #6280 数列分块入门4 - https://loj.ac/p/6280
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

# 每个块的元素和
sum_blocks = [0] * MAXN

# 标记块中所有元素是否都变成了0或1
allZeroOrOne = [False] * MAXN

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
    
    # 计算每个块的元素和，并检查是否所有元素都是0或1
    for i in range(1, blockNum + 1):
        sum_blocks[i] = 0
        allZeroOrOne[i] = True
        for j in range(blockLeft[i], blockRight[i] + 1):
            sum_blocks[i] += arr[j]
            if arr[j] != 0 and arr[j] != 1:
                allZeroOrOne[i] = False

def sqrt_operation(x):
    """
    对一个数进行开方操作
    :param x: 原数值
    :return: 开方后向下取整的结果
    """
    return int(math.sqrt(x))

def sqrt_op(l, r):
    """
    区间开方操作
    时间复杂度：O(√n) 均摊
    :param l: 区间左端点
    :param r: 区间右端点
    """
    belongL = belong[l]  # 左端点所属块
    belongR = belong[r]  # 右端点所属块
    
    # 如果在同一个块内，直接暴力处理
    if belongL == belongR:
        # 如果该块所有元素都是0或1，则无需处理
        if allZeroOrOne[belongL]:
            return
        
        # 否则暴力处理
        for i in range(l, r + 1):
            sum_blocks[belongL] -= arr[i]
            arr[i] = sqrt_operation(arr[i])
            sum_blocks[belongL] += arr[i]
        
        # 重新检查该块是否所有元素都是0或1
        allZeroOrOne[belongL] = True
        for i in range(blockLeft[belongL], blockRight[belongL] + 1):
            if arr[i] != 0 and arr[i] != 1:
                allZeroOrOne[belongL] = False
                break
    else:
        # 处理左端不完整块
        if not allZeroOrOne[belongL]:
            for i in range(l, blockRight[belongL] + 1):
                sum_blocks[belongL] -= arr[i]
                arr[i] = sqrt_operation(arr[i])
                sum_blocks[belongL] += arr[i]
            
            # 重新检查该块是否所有元素都是0或1
            allZeroOrOne[belongL] = True
            for i in range(blockLeft[belongL], blockRight[belongL] + 1):
                if arr[i] != 0 and arr[i] != 1:
                    allZeroOrOne[belongL] = False
                    break
        
        # 处理右端不完整块
        if not allZeroOrOne[belongR]:
            for i in range(blockLeft[belongR], r + 1):
                sum_blocks[belongR] -= arr[i]
                arr[i] = sqrt_operation(arr[i])
                sum_blocks[belongR] += arr[i]
            
            # 重新检查该块是否所有元素都是0或1
            allZeroOrOne[belongR] = True
            for i in range(blockLeft[belongR], blockRight[belongR] + 1):
                if arr[i] != 0 and arr[i] != 1:
                    allZeroOrOne[belongR] = False
                    break
        
        # 处理中间的完整块
        for i in range(belongL + 1, belongR):
            # 如果该块所有元素都是0或1，则无需处理
            if allZeroOrOne[i]:
                continue
            
            # 否则暴力处理
            for j in range(blockLeft[i], blockRight[i] + 1):
                sum_blocks[i] -= arr[j]
                arr[j] = sqrt_operation(arr[j])
                sum_blocks[i] += arr[j]
            
            # 重新检查该块是否所有元素都是0或1
            allZeroOrOne[i] = True
            for j in range(blockLeft[i], blockRight[i] + 1):
                if arr[j] != 0 and arr[j] != 1:
                    allZeroOrOne[i] = False
                    break

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
            result += arr[i]
    else:
        # 处理左端不完整块
        for i in range(l, blockRight[belongL] + 1):
            result += arr[i]
        
        # 处理右端不完整块
        for i in range(blockLeft[belongR], r + 1):
            result += arr[i]
        
        # 处理中间的完整块，直接使用块的元素和
        for i in range(belongL + 1, belongR):
            result += sum_blocks[i]
    
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
            # 区间开方操作
            sqrt_op(l, r)
        else:
            # 区间求和操作
            print(query(l, r))

if __name__ == "__main__":
    main()
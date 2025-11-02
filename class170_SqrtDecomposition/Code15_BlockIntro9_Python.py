# 数列分块入门9 - Python实现
# 题目来源：LibreOJ #6285 数列分块入门9
# 题目链接：https://loj.ac/p/6285
# 题目描述：给出一个长为n的数列，以及n个操作，操作涉及询问区间的最小众数
# 操作：询问区间[l,r]的最小众数（出现次数最多，相同出现次数时取最小值）
# 解题思路：
# 1. 使用分块算法，将数组分成sqrt(n)大小的块
# 2. 预处理每个块区间[i,j]的最小众数，存储在f[i][j]中
# 3. 对于每个块，维护其中每个值的出现次数
# 4. 对于查询操作，如果区间跨越多个块，则利用预处理结果和暴力统计边界块
# 5. 最小众数定义：出现次数最多，相同出现次数时取最小值
# 时间复杂度：预处理O(n√n)，查询操作O(√n)
# 空间复杂度：O(n + √n * √n)
# 相关题目：
# 1. LibreOJ #6277 数列分块入门1 - https://loj.ac/p/6277
# 2. LibreOJ #6278 数列分块入门2 - https://loj.ac/p/6278
# 3. LibreOJ #6279 数列分块入门3 - https://loj.ac/p/6279
# 4. LibreOJ #6280 数列分块入门4 - https://loj.ac/p/6280
# 5. LibreOJ #6281 数列分块入门5 - https://loj.ac/p/6281
# 6. LibreOJ #6282 数列分块入门6 - https://loj.ac/p/6282
# 7. LibreOJ #6283 数列分块入门7 - https://loj.ac/p/6283
# 8. LibreOJ #6284 数列分块入门8 - https://loj.ac/p/6284

import math
from collections import defaultdict

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

# 预处理：f[i][j] 表示第i块到第j块的最小众数
f = [[0] * 1001 for _ in range(1001)]

# 每个值在每个块中的出现次数
countInBlock = [defaultdict(int) for _ in range(1001)]

def build(n):
    """
    构建分块结构
    时间复杂度：O(n√n)
    空间复杂度：O(n + √n * √n)
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
    
    # 清空countInBlock数组
    for i in range(1, blockNum + 1):
        countInBlock[i].clear()
    
    # 计算每个块中每个值的出现次数
    for i in range(1, blockNum + 1):
        countInBlock[i].clear()
        for j in range(blockLeft[i], blockRight[i] + 1):
            countInBlock[i][arr[j]] += 1
    
    # 预处理f数组
    for i in range(1, blockNum + 1):
        totalCount = defaultdict(int)
        for j in range(i, blockNum + 1):
            # 将第j块的计数加入总统计
            for value, count in countInBlock[j].items():
                totalCount[value] += count
            
            # 找到当前范围的最小众数
            mode = float('inf')
            maxCount = 0
            for value, count in totalCount.items():
                if count > maxCount or (count == maxCount and value < mode):
                    maxCount = count
                    mode = value
            f[i][j] = int(mode) if mode != float('inf') else 0

def count_in_range(l, r, c):
    """
    计算区间[l,r]内值c的出现次数
    时间复杂度：O(√n)
    :param l: 区间左端点
    :param r: 区间右端点
    :param c: 要统计的值
    :return: 值c在区间[l,r]内的出现次数
    """
    result = 0
    belongL = belong[l]  # 左端点所属块
    belongR = belong[r]  # 右端点所属块
    
    # 如果在同一个块内，直接暴力统计
    if belongL == belongR:
        for i in range(l, r + 1):
            if arr[i] == c:
                result += 1
    else:
        # 处理左端不完整块
        for i in range(l, blockRight[belongL] + 1):
            if arr[i] == c:
                result += 1
        
        # 处理右端不完整块
        for i in range(blockLeft[belongR], r + 1):
            if arr[i] == c:
                result += 1
        
        # 处理中间的完整块
        for i in range(belongL + 1, belongR):
            result += countInBlock[i].get(c, 0)
    
    return result

def query(l, r):
    """
    查询区间[l,r]的最小众数
    时间复杂度：O(√n)
    :param l: 区间左端点
    :param r: 区间右端点
    :return: 区间[l,r]的最小众数
    """
    belongL = belong[l]  # 左端点所属块
    belongR = belong[r]  # 右端点所属块
    
    # 如果在同一个块内，直接暴力处理
    if belongL == belongR:
        count = defaultdict(int)
        for i in range(l, r + 1):
            count[arr[i]] += 1
        
        mode = float('inf')
        maxCount = 0
        for value, cnt in count.items():
            if cnt > maxCount or (cnt == maxCount and value < mode):
                maxCount = cnt
                mode = value
        return int(mode) if mode != float('inf') else 0
    else:
        # 获取中间完整块的最小众数
        mode = f[belongL + 1][belongR - 1]
        maxCount = count_in_range(l, r, mode)
        
        # 检查左端不完整块中的值
        for i in range(l, blockRight[belongL] + 1):
            value = arr[i]
            cnt = count_in_range(l, r, value)
            if cnt > maxCount or (cnt == maxCount and value < mode):
                maxCount = cnt
                mode = value
        
        # 检查右端不完整块中的值
        for i in range(blockLeft[belongR], r + 1):
            value = arr[i]
            cnt = count_in_range(l, r, value)
            if cnt > maxCount or (cnt == maxCount and value < mode):
                maxCount = cnt
                mode = value
        
        return mode

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
        
        # 区间查询最小众数
        print(query(l, r))

if __name__ == "__main__":
    main()
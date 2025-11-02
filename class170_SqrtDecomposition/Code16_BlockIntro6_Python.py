# 数列分块入门6 - Python实现
# 题目来源：LibreOJ #6282 数列分块入门6
# 题目链接：https://loj.ac/p/6282
# 题目描述：给出一个长为n的数列，以及n个操作，操作涉及单点插入，单点查询
# 操作0：在位置x后插入一个数y
# 操作1：查询位置x的值
# 解题思路：
# 1. 使用分块算法，将数组分成sqrt(n)大小的块
# 2. 每个块使用列表存储元素，支持快速插入和查询
# 3. 当某个块过大时（超过2*sqrt(n)），需要重构整个分块结构
# 4. 对于插入操作，在指定位置找到对应块并插入元素
# 5. 对于查询操作，遍历块找到指定位置的元素
# 时间复杂度：预处理O(n)，插入操作O(√n)均摊，查询操作O(√n)
# 空间复杂度：O(n)
# 相关题目：
# 1. LibreOJ #6277 数列分块入门1 - https://loj.ac/p/6277
# 2. LibreOJ #6278 数列分块入门2 - https://loj.ac/p/6278
# 3. LibreOJ #6279 数列分块入门3 - https://loj.ac/p/6279
# 4. LibreOJ #6280 数列分块入门4 - https://loj.ac/p/6280
# 5. LibreOJ #6281 数列分块入门5 - https://loj.ac/p/6281
# 6. LibreOJ #6283 数列分块入门7 - https://loj.ac/p/6283
# 7. LibreOJ #6284 数列分块入门8 - https://loj.ac/p/6284
# 8. LibreOJ #6285 数列分块入门9 - https://loj.ac/p/6285

import math
import sys

# 最大数组大小
MAXN = 1000005

# 原数组（使用列表实现）
arr = []

# 块大小和块数量
blockSize = 0
blockNum = 0

# 每个块的左右边界
blocks = []

def build(n):
    """
    构建分块结构
    时间复杂度：O(n)
    空间复杂度：O(n)
    :param n: 数组长度
    """
    global blockSize, blockNum, blocks
    
    # 块大小取sqrt(n)
    blockSize = int(math.sqrt(n))
    # 块数量
    blockNum = (n + blockSize - 1) // blockSize
    
    # 初始化块
    blocks = [[] for _ in range(blockNum)]
    
    # 将元素分配到各个块中
    for i in range(n):
        blocks[i // blockSize].append(arr[i])

def rebuild():
    """
    重构分块结构（当某个块过大时）
    时间复杂度：O(n)
    """
    global blockSize, blockNum, arr, blocks
    
    # 重新计算总元素数
    total = sum(len(block) for block in blocks)
    
    # 重新分配块大小
    blockSize = int(math.sqrt(total))
    blockNum = (total + blockSize - 1) // blockSize
    
    # 重建数组
    arr = []
    for block in blocks:
        arr.extend(block)
    
    # 重新构建块结构
    blocks = [[] for _ in range(blockNum)]
    
    for i in range(len(arr)):
        blocks[i // blockSize].append(arr[i])

def insert(x, y):
    """
    单点插入操作
    时间复杂度：O(√n) 均摊
    :param x: 插入位置
    :param y: 插入的值
    """
    global blocks
    
    # 找到x位置所在的块
    blockIndex = 0
    count = 0
    
    # 计算x位置在哪个块中
    for i in range(len(blocks)):
        if count + len(blocks[i]) > x:
            blockIndex = i
            break
        count += len(blocks[i])
    
    # 在对应块中插入元素
    posInBlock = x - count
    blocks[blockIndex].insert(posInBlock, y)
    
    # 如果某个块过大，进行重构
    if len(blocks[blockIndex]) > 2 * blockSize:
        rebuild()

def query(x):
    """
    单点查询
    时间复杂度：O(√n)
    :param x: 查询位置
    :return: 位置x的值
    """
    count = 0
    
    # 找到x位置所在的块
    for block in blocks:
        if count + len(block) > x:
            return block[x - count]
        count += len(block)
    
    return -1  # 位置不存在

def main():
    # 读取数组长度
    n = int(input())
    
    # 读取数组元素
    elements = list(map(int, input().split()))
    global arr
    arr = elements[:]
    
    # 构建分块结构
    build(n)
    
    # 处理操作
    for _ in range(n):
        operation = list(map(int, input().split()))
        op = operation[0]
        x = operation[1]
        
        if op == 0:
            # 单点插入操作
            y = operation[2]
            insert(x, y)
        else:
            # 单点查询操作
            print(query(x - 1))  # 转换为0索引

if __name__ == "__main__":
    main()
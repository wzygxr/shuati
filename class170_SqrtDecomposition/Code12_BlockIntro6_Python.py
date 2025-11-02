# 数列分块入门6 - Python实现
# 题目来源：LibreOJ #6282 数列分块入门6
# 题目链接：https://loj.ac/p/6282
# 题目描述：给出一个长为n的数列，以及n个操作，操作涉及单点插入，单点询问
# 操作0：在位置loc后面插入一个数字c（如果有多个loc，选择第一个）
# 操作1：单点询问位置loc的值
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

# 最大数组大小
MAXN = 1000001

# 使用列表存储每个块的元素
blocks = [[] for _ in range(MAXN)]

# 块大小和块数量
blockSize = 0
blockNum = 0

# 每个块的实际大小
blockSizeArray = [0] * MAXN

# 总元素数量
totalSize = 0

def build(n, arr):
    """
    构建分块结构
    时间复杂度：O(n)
    空间复杂度：O(n)
    :param n: 数组长度
    :param arr: 原数组
    """
    global blockSize, blockNum, totalSize
    
    # 块大小取sqrt(n)
    blockSize = int(math.sqrt(n))
    # 块数量
    blockNum = (n + blockSize - 1) // blockSize
    totalSize = n
    
    # 清空原有块
    for i in range(1, blockNum + 1):
        blocks[i].clear()
    
    # 将元素分配到各个块中
    for i in range(1, n + 1):
        blockId = (i - 1) // blockSize + 1
        blocks[blockId].append(arr[i])
        blockSizeArray[blockId] += 1

def rebuild():
    """
    重构分块结构
    当某些块过大时需要重新分块
    时间复杂度：O(n)
    """
    global blockSize, blockNum, totalSize
    
    # 收集所有元素
    allElements = []
    for i in range(1, blockNum + 1):
        allElements.extend(blocks[i])
    
    # 重新计算块大小和块数量
    blockSize = int(math.sqrt(len(allElements)))
    blockNum = (len(allElements) + blockSize - 1) // blockSize
    
    # 清空原有块
    for i in range(1, blockNum + 1):
        blocks[i].clear()
    
    # 重新分配元素到块中
    for i in range(len(allElements)):
        blockId = i // blockSize + 1
        if blockId > blockNum:
            blockNum = blockId
        blocks[blockId].append(allElements[i])
    
    # 更新每个块的大小
    for i in range(1, blockNum + 1):
        blockSizeArray[i] = len(blocks[i])
    
    totalSize = len(allElements)

def insert(loc, c):
    """
    在指定位置后插入元素
    时间复杂度：O(√n) 均摊
    :param loc: 插入位置
    :param c: 插入的元素
    """
    global totalSize
    
    # 找到loc所在的块和位置
    currentPos = 0
    targetBlock = 1
    
    # 查找loc所在的位置
    for i in range(1, blockNum + 1):
        if currentPos + blockSizeArray[i] >= loc:
            targetBlock = i
            break
        currentPos += blockSizeArray[i]
    
    # 在目标块中插入元素
    posInBlock = loc - currentPos
    blocks[targetBlock].insert(posInBlock, c)
    blockSizeArray[targetBlock] += 1
    totalSize += 1
    
    # 如果某个块过大，需要重构
    if blockSizeArray[targetBlock] > 2 * blockSize:
        rebuild()

def query(loc):
    """
    查询指定位置的元素
    时间复杂度：O(√n)
    :param loc: 查询位置
    :return: 位置loc的元素
    """
    currentPos = 0
    
    # 查找loc所在的块
    for i in range(1, blockNum + 1):
        if currentPos + blockSizeArray[i] >= loc:
            # 在块中找到具体位置
            posInBlock = loc - currentPos - 1
            return blocks[i][posInBlock]
        currentPos += blockSizeArray[i]
    
    return -1  # 位置不存在

def main():
    global totalSize
    
    # 读取数组长度
    n = int(input())
    
    # 读取数组元素
    arr = [0] * (n + 1)
    elements = list(map(int, input().split()))
    for i in range(1, n + 1):
        arr[i] = elements[i - 1]
    
    # 构建分块结构
    build(n, arr)
    
    # 处理操作
    for _ in range(n):
        operation = list(map(int, input().split()))
        op = operation[0]
        loc = operation[1]
        
        if op == 0:
            # 插入操作
            c = operation[2]
            insert(loc, c)
        else:
            # 查询操作
            print(query(loc))

if __name__ == "__main__":
    main()
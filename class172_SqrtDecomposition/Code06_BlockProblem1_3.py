# LOJ 数列分块入门6 - Python实现
# 题目：单点插入，单点询问
# 链接：https://loj.ac/p/6282
# 题目描述：
# 给出一个长为n的数列，以及n个操作，操作涉及单点插入，单点询问。
# 操作 0 l r c : 在位置l后面插入数字c（r忽略）
# 操作 1 l r c : 询问位置l的数字（r和c忽略）
# 数据范围：1 <= n <= 50000

import math
import sys

# 从标准输入读取数据
input = sys.stdin.read
lines = input().split('\n')

# 块的大小和数量
blockSize = 0
blockNum = 0

# 总元素数量
totalElements = 0

# 使用列表存储每个块的数据
blocks = []

# 初始化分块结构
def build():
    """初始化分块结构"""
    global blockSize, blockNum
    
    # 块大小通常选择sqrt(n)，这样可以让时间复杂度达到较优
    blockSize = int(math.sqrt(totalElements)) + 1
    # 块数量初始为0
    blockNum = 0

# 重新分配块，当插入导致块大小不均衡时调用
def rebuild():
    """重新分配块，保持时间复杂度"""
    global blockSize, blockNum, totalElements
    
    # 收集所有元素
    allElements = []
    for block in blocks:
        allElements.extend(block)
    
    # 清空所有块
    blocks.clear()
    
    # 重新分块
    totalElements = len(allElements)
    blockSize = int(math.sqrt(totalElements)) + 1
    blockNum = 0
    
    i = 0
    while i < totalElements:
        # 创建新块
        newBlock = []
        cnt = 0
        while cnt < blockSize and i < totalElements:
            newBlock.append(allElements[i])
            cnt += 1
            i += 1
        blocks.append(newBlock)
        blockNum += 1

# 单点插入操作
# 在位置pos后面插入值val
def insert(pos, val):
    """单点插入操作"""
    global totalElements
    
    # 计算插入位置所在的块和块内偏移
    belong = 0
    offset = pos
    
    while belong < blockNum and offset > len(blocks[belong]):
        offset -= len(blocks[belong])
        belong += 1
    
    # 如果offset为0，表示在第一个位置插入
    if offset == 0:
        blocks[0].insert(0, val)
    else:
        # 在对应位置插入
        blocks[belong].insert(offset, val)
    
    totalElements += 1
    
    # 如果块过大，重新分块以保持时间复杂度
    if len(blocks[belong]) > 2 * blockSize:
        rebuild()

# 单点查询操作
# 查询位置pos的值
def query(pos):
    """单点查询操作"""
    # 计算查询位置所在的块和块内偏移
    belong = 0
    offset = pos
    
    while belong < blockNum and offset > len(blocks[belong]):
        offset -= len(blocks[belong])
        belong += 1
    
    # 返回对应位置的值
    return blocks[belong][offset - 1]

# 主函数
def main():
    global totalElements, blockNum
    
    # 读取初始数组长度
    totalElements = int(lines[0])
    
    # 读取初始数组元素
    initialElements = list(map(int, lines[1].split()))
    
    # 初始化分块结构
    build()
    
    # 将初始元素放入块中
    blocks.clear()
    i = 0
    while i < totalElements:
        newBlock = []
        cnt = 0
        while cnt < blockSize and i < totalElements:
            newBlock.append(initialElements[i])
            cnt += 1
            i += 1
        blocks.append(newBlock)
        blockNum += 1
    
    # 存储结果
    results = []
    
    # 处理n个操作
    for i in range(totalElements):
        operation = list(map(int, lines[2 + i].split()))
        op, l, r, c = operation[0], operation[1], operation[2], operation[3]
        
        if op == 0:
            # 单点插入操作
            insert(l, c)
        else:
            # 单点查询操作
            results.append(str(query(l)))
    
    # 输出结果
    print('\n'.join(results))

if __name__ == "__main__":
    main()

'''算法解析：

时间复杂度分析：
1. 建立分块结构：O(n)
2. 单点插入操作：平均O(√n) - 最坏情况下需要重建整个分块结构O(n)，但摊还分析后仍是O(√n)
3. 单点查询操作：O(√n) - 需要找到对应的块和块内偏移

空间复杂度：O(n) - 存储原数组和分块相关信息

算法思想：
这道题与前面的题目不同，因为涉及到动态插入，普通的静态分块方法不适用。
这里使用了动态分块的思想，每个块用Python的列表存储，方便插入操作。

核心思想：
1. 将数组分成大小约为√n的块，每个块用列表存储
2. 插入时，找到对应的块，在块内进行插入操作
3. 当某个块的大小超过2√n时，重新分块以保持时间复杂度
4. 查询时，找到对应的块和块内偏移，直接返回值

优势：
1. 可以处理动态插入的情况
2. 平均时间复杂度仍然保持在O(√n)
3. 实现相对简单，比平衡树等数据结构容易理解和编码

适用场景：
1. 需要动态插入和单点查询的问题
2. 不适合用平衡树等复杂数据结构的场景
3. 对代码复杂度有要求的场景

Python特有的优化：
1. 利用Python列表的insert操作进行高效的块内插入
2. 使用extend方法快速合并所有块的数据
3. 读取所有输入一次，避免多次IO操作，提高效率
'''
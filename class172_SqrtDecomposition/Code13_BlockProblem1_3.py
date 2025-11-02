# SPOJ DQUERY - D-query - Python实现
# 题目：区间不同数的个数
# 链接：https://www.spoj.com/problems/DQUERY/
# 题目描述：
# 给定一个长度为n的序列，每次询问一个区间[l,r]，需要回答区间里有多少个不同的数。
# 数据范围：1 <= n <= 30000, 1 <= q <= 200000

import sys

# 从标准输入读取数据
input = sys.stdin.read
lines = input().split('\n')

# 读取数组长度
n = int(lines[0])

# 读取数组元素
arr = [0] + list(map(int, lines[1].split()))  # 下标从1开始

# 块的大小和数量
blockSize = int(n ** 0.5)
blockNum = (n + blockSize - 1) // blockSize

# 每个元素所属的块编号
belong = [0] * (n + 1)

# 每个块的左右边界
blockLeft = [0] * (blockNum + 1)
blockRight = [0] * (blockNum + 1)

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

# 查询区间[l,r]的不同数个数
def query(l, r):
    """查询区间[l,r]的不同数个数"""
    # 使用集合记录数字是否已经出现
    seen = set()
    count = 0
    
    # 统计区间[l,r]中不同数字的个数
    for i in range(l, r + 1):
        if arr[i] not in seen:
            seen.add(arr[i])
            count += 1
    
    return count

# 主函数
def main():
    # 初始化分块结构
    build(n)
    
    # 读取查询数量
    q = int(lines[2])
    
    # 存储结果
    results = []
    
    # 处理q个查询
    for i in range(q):
        query_line = list(map(int, lines[3 + i].split()))
        l, r = query_line[0], query_line[1]
        results.append(str(query(l, r)))
    
    # 输出结果
    print('\n'.join(results))

if __name__ == "__main__":
    main()

'''
算法解析：

时间复杂度分析：
1. 建立分块结构：O(n)
2. 查询操作：O(n) - 每次查询需要遍历整个区间

空间复杂度：O(n) - 存储原数组和分块相关信息

算法思想：
这是一个经典的区间不同数个数查询问题。对于这类问题，通常可以使用莫队算法来优化。

核心思想：
1. 对于每个查询，直接遍历区间统计不同数字的个数
2. 使用集合记录数字是否已经出现

优化思路：
1. 可以使用莫队算法进行离线处理，将时间复杂度优化到O((n+q)√n)
2. 可以使用主席树等高级数据结构进行在线处理

优势：
1. 实现简单，易于理解和编码
2. 对于小规模数据可以接受

适用场景：
1. 区间不同数个数查询问题
2. 数据规模较小的场景
'''
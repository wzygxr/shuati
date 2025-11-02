# Serega and Fun问题 - 分块算法实现 (Python版本)
# 题目来源: https://codeforces.com/problemset/problem/455/D
# 题目大意: 给定一个长度为n的数组arr，有q次操作，操作分为两种类型：
# 类型1: l r - 计算区间[l,r]内值为k的元素个数，其中k是该区间内出现次数最多的数字
# 类型2: l r - 将arr[l]移动到位置r
# 约束条件: 
# 1 <= n, q <= 10^5
# 1 <= arr[i] <= n

import math
from collections import deque

MAXN = 100005
n, q = 0, 0
blen = 0  # 块大小
blocks = []  # 每个块存储元素
cnt = []  # 每个块中各个数值的出现次数
bcnt = 0  # 块的数量


def reBuild():
    """
    重构分块结构
    当块的数量远超sqrt(n)时，重新分块以保持效率
    """
    global blocks, cnt, bcnt
    # 当块的数量远超sqrt(n)时，重新分块
    if bcnt > 2 * blen:
        # 将所有元素收集到一个临时列表中
        tmp = []
        for i in range(bcnt):
            tmp.extend(blocks[i])
        
        # 清空原有的分块结构
        blocks = []
        cnt = []
        bcnt = 0
        
        # 重新分块
        for i in range(len(tmp)):
            # 每blen个元素作为一个块
            if i % blen == 0:
                blocks.append(deque())
                cnt.append([0] * MAXN)
                bcnt += 1
            # 将元素添加到对应的块中
            val = tmp[i]
            blocks[bcnt - 1].append(val)
            cnt[bcnt - 1][val] += 1


def query(l, r):
    """
    查询区间[l,r]内出现最多的数字的出现次数
    参数:
        l: 左边界(1-indexed)
        r: 右边界(1-indexed)
    返回:
        出现最多的数字的出现次数
    """
    # 转换为0-indexed
    l -= 1
    r -= 1
    
    # 计算左右边界所在的块
    lb = l // blen
    rb = r // blen
    ans = 0

    # 如果在同一个块内
    if lb == rb:
        # 直接遍历计算
        count = [0] * MAXN
        for i in range(l, r + 1):
            blockId = i // blen
            indexInBlock = i % blen
            val = blocks[blockId][indexInBlock]
            count[val] += 1
            ans = max(ans, count[val])
    else:
        # 跨多个块
        count = [0] * MAXN
        
        # 处理左端不完整块
        lEnd = (lb + 1) * blen - 1
        for i in range(l, min(lEnd + 1, n)):
            blockId = i // blen
            indexInBlock = i % blen
            val = blocks[blockId][indexInBlock]
            count[val] += 1
            ans = max(ans, count[val])

        # 处理中间完整块
        for i in range(lb + 1, rb):
            for j in range(1, MAXN):
                count[j] += cnt[i][j]
                ans = max(ans, count[j])

        # 处理右端不完整块
        rStart = rb * blen
        for i in range(rStart, r + 1):
            blockId = i // blen
            indexInBlock = i % blen
            val = blocks[blockId][indexInBlock]
            count[val] += 1
            ans = max(ans, count[val])

    return ans


def move(l, r):
    """
    将位置l的元素移动到位置r
    参数:
        l: 源位置(1-indexed)
        r: 目标位置(1-indexed)
    """
    # 转换为0-indexed
    l -= 1
    r -= 1
    
    # 计算左右位置所在的块
    lb = l // blen
    rb = r // blen
    lIndexInBlock = l % blen
    
    # 从源块中移除元素
    val = blocks[lb][lIndexInBlock]
    del blocks[lb][lIndexInBlock]
    cnt[lb][val] -= 1

    # 计算在新位置的索引
    newIndexInBlock = r % blen
    if lb < rb:
        # 如果从前面的块移动到后面的块
        newIndexInBlock = newIndexInBlock - (lb + 1) * blen + (lb * blen) + len(blocks[lb])
    elif lb > rb:
        # 如果从后面的块移动到前面的块
        newIndexInBlock = newIndexInBlock + (lb * blen) - (rb + 1) * blen

    # 将元素插入到目标块中
    blocks[rb].insert(newIndexInBlock, val)
    cnt[rb][val] += 1


def prepare():
    """
    初始化分块
    """
    global blen, blocks, cnt, bcnt
    # 计算块大小，通常选择sqrt(n)
    blen = int(math.sqrt(n))
    bcnt = 0
    blocks = []
    cnt = []

    # 初始化分块结构
    for i in range(n):
        if i % blen == 0:
            blocks.append(deque())
            cnt.append([0] * MAXN)
            bcnt += 1


def main():
    global n, q
    # 读取数组长度n
    n = int(input())
    
    # 初始化分块
    prepare()

    # 读取初始数组
    arr = list(map(int, input().split()))
    for i in range(n):
        blockId = i // blen
        val = arr[i]
        blocks[blockId].append(val)
        cnt[blockId][val] += 1

    # 读取操作次数q
    q = int(input())
    
    # 处理q次操作
    for i in range(1, q + 1):
        op, l, r = map(int, input().split())
        if op == 1:
            # 查询操作
            print(query(l, r))
        else:
            # 移动操作
            move(l, r)
            # 定期重构以保持效率
            if i % 5000 == 0:
                reBuild()


if __name__ == "__main__":
    main()
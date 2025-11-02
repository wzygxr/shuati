# -*- coding: utf-8 -*-
"""
POJ 2761 Feed the dogs

题目来源: POJ 2761
题目链接: http://poj.org/problem?id=2761

题目描述:
Wind非常喜欢漂亮的狗，她有n只宠物狗。所以Jiajia必须每天喂狗给Wind。
Jiajia爱Wind，但不爱狗，所以Jiajia用一种特殊的方式喂狗。
午餐时间，狗会站在一条线上，从1到n编号，最左边的是1，第二个是2，以此类推。
在每次喂食中，Jiajia选择一个区间[i,j]，选择第k个漂亮的狗来喂食。
当然，Jiajia有自己决定每只狗漂亮值的方法。
应该注意的是，Jiajia不想让任何位置被喂得太多，因为这可能会导致狗的死亡。
如果这样，Wind会生气，后果会很严重。因此，任何喂食区间都不会完全包含另一个区间，尽管区间可能相互交叉。

解题思路:
使用可持久化线段树（主席树）解决区间第K小问题。
1. 对数值进行离散化处理
2. 建立可持久化线段树，每个位置对应一个版本
3. 对于每个查询，在对应区间的线段树版本中查询第K小的值

时间复杂度: O((n + m) log n)
空间复杂度: O(n log n)

约束条件:
n < 100001
m < 50001

示例:
输入:
7 2
1 5 2 6 3 7 4
1 5 3
2 7 1

输出:
3
2
"""

import sys
import bisect
input = sys.stdin.read

# 全局变量
MAXN = 100010

# 原始数据
arr = [0] * MAXN
sorted_arr = [0] * MAXN

# 离散化相关
values = [0] * MAXN

# 可持久化线段树
root = [0] * MAXN
left = [0] * (MAXN * 20)
right = [0] * (MAXN * 20)
sum_tree = [0] * (MAXN * 20)
cnt = 0

def build(l, r):
    """构建空线段树"""
    global cnt
    cnt += 1
    rt = cnt
    sum_tree[rt] = 0
    if l < r:
        mid = (l + r) // 2
        left[rt] = build(l, mid)
        right[rt] = build(mid + 1, r)
    return rt

def insert(pos, l, r, pre):
    """插入操作"""
    global cnt
    cnt += 1
    rt = cnt
    left[rt] = left[pre]
    right[rt] = right[pre]
    sum_tree[rt] = sum_tree[pre] + 1
    
    if l < r:
        mid = (l + r) // 2
        if pos <= mid:
            left[rt] = insert(pos, l, mid, left[rt])
        else:
            right[rt] = insert(pos, mid + 1, r, right[rt])
    return rt

def query(k, l, r, u, v):
    """查询区间第k小"""
    if l >= r:
        return l
    mid = (l + r) // 2
    # 计算左子树中数的个数
    x = sum_tree[left[v]] - sum_tree[left[u]]
    if x >= k:
        # 第k小在左子树中
        return query(k, l, mid, left[u], left[v])
    else:
        # 第k小在右子树中
        return query(k - x, mid + 1, r, right[u], right[v])

def main():
    global cnt
    
    data = input().split()
    idx = 0
    
    n = int(data[idx])
    idx += 1
    m = int(data[idx])
    idx += 1
    
    # 读取数据
    for i in range(1, n + 1):
        arr[i] = int(data[idx])
        idx += 1
        sorted_arr[i] = arr[i]
    
    # 离散化处理
    sorted_unique = sorted(list(set(sorted_arr[1:n+1])))
    unique_count = len(sorted_unique)
    
    # 构建初始线段树
    root[0] = build(1, unique_count)
    
    # 逐个插入元素，构建可持久化线段树
    for i in range(1, n + 1):
        pos = bisect.bisect_left(sorted_unique, arr[i]) + 1
        root[i] = insert(pos, 1, unique_count, root[i-1])
    
    # 处理查询
    for i in range(m):
        l = int(data[idx])
        idx += 1
        r = int(data[idx])
        idx += 1
        k = int(data[idx])
        idx += 1
        
        # 查询区间[l,r]第k小的数
        pos = query(k, 1, unique_count, root[l-1], root[r])
        print(sorted_unique[pos - 1])

if __name__ == "__main__":
    main()
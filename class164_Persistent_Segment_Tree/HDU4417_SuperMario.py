# -*- coding: utf-8 -*-
"""
HDU 4417 Super Mario

题目来源: HDU 4417
题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=4417

题目描述:
Mario是世界著名的管道工。他"健壮"的身材和惊人的跳跃能力让我们记忆犹新。
现在可怜的公主又遇到了麻烦，Mario需要拯救他的爱人。
我们把通往Boss城堡的路看作一条线(长度为n)，在每个整数点i上有一个高度为hi的砖块。
现在的问题是，如果Mario能跳的最大高度是H，那么在[L,R]区间内他能击中多少个砖块。

解题思路:
使用可持久化线段树（主席树）解决区间小于等于H的元素个数问题。
1. 对高度值进行离散化处理
2. 建立可持久化线段树，每个位置对应一个版本
3. 对于每个查询，在对应区间的线段树版本中查询小于等于H的元素个数

时间复杂度: O((n + m) log n)
空间复杂度: O(n log n)

约束条件:
1 <= n <= 10^5
1 <= m <= 10^5
0 <= height <= 10^9
0 <= L <= R < n
0 <= H <= 10^9

示例:
输入:
1
10 10
0 5 2 7 5 4 3 8 7 7
2 8 6
3 5 0
1 3 1
1 9 4
0 1 0
3 5 5
5 5 1
4 6 3
1 5 7
5 7 3

输出:
Case 1:
4
0
0
3
1
2
0
1
5
1
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
heights = [0] * MAXN

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

def query(pos, l, r, u, v):
    """查询区间[1, pos]的元素个数"""
    if pos >= r:
        return sum_tree[v] - sum_tree[u]
    if pos < l:
        return 0
    mid = (l + r) // 2
    return query(pos, l, mid, left[u], left[v]) + query(pos, mid + 1, r, right[u], right[v])

def main():
    global cnt
    
    data = input().split()
    idx = 0
    
    T = int(data[idx])
    idx += 1
    
    for cas in range(1, T + 1):
        n = int(data[idx])
        idx += 1
        m = int(data[idx])
        idx += 1
        
        # 重置计数器
        cnt = 0
        
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
            pos = bisect.bisect_right(sorted_unique, arr[i])
            root[i] = insert(pos, 1, unique_count, root[i-1])
        
        print(f"Case {cas}:")
        
        # 处理查询
        for i in range(m):
            L = int(data[idx]) + 1  # 转换为1-indexed
            idx += 1
            R = int(data[idx]) + 1  # 转换为1-indexed
            idx += 1
            H = int(data[idx])
            idx += 1
            
            # 查询区间[L,R]中小于等于H的元素个数
            pos = bisect.bisect_right(sorted_unique, H)
            result = query(pos, 1, unique_count, root[L-1], root[R])
            print(result)

if __name__ == "__main__":
    main()
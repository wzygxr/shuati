# -*- coding: utf-8 -*-
"""
LOJ 6280 数列分块入门4

题目来源: LibreOJ 6280
题目链接: https://loj.ac/p/6280

题目描述:
给出一个长为n的数列，以及n个操作，操作涉及区间加法，区间求和。
并且要求支持查询历史版本。

解题思路:
使用可持久化线段树解决带历史版本的区间加法和区间求和问题。
1. 对于每次修改操作，只创建被修改路径上的新节点，共享未修改的部分
2. 使用懒惰标记技术处理区间修改
3. 通过clone函数实现节点的复制，确保历史版本的完整性
4. 在需要下传懒惰标记时，先复制子节点再进行操作

时间复杂度: O((n + m) log n)
空间复杂度: O(n log n)

1 <= n, m <= 50000
0 <= value[i] <= 10^9

示例:
输入:
4
1 2 2 3
5
1 1 3 2
2 1 3
1 2 3 1
2 1 3
2 2 4

输出:
7
8
6
"""

import sys
input = sys.stdin.read

# 全局变量
MAXN = 50010
MAXT = MAXN * 50

n, m, version = 0, 0, 0
arr = [0] * MAXN
root = [0] * MAXN
left = [0] * MAXT
right = [0] * MAXT

# 累加和信息
sum_tree = [0] * MAXT

# 懒更新信息，范围增加的懒更新
add_tag = [0] * MAXT

cnt = 0

def clone(i):
    """克隆节点"""
    global cnt
    cnt += 1
    rt = cnt
    left[rt] = left[i]
    right[rt] = right[i]
    sum_tree[rt] = sum_tree[i]
    add_tag[rt] = add_tag[i]
    return rt

def up(i):
    """更新节点信息"""
    sum_tree[i] = sum_tree[left[i]] + sum_tree[right[i]]

def lazy(i, v, n):
    """懒更新操作"""
    sum_tree[i] += v * n
    add_tag[i] += v

def down(i, ln, rn):
    """下传懒更新标记"""
    if add_tag[i] != 0:
        left[i] = clone(left[i])
        right[i] = clone(right[i])
        lazy(left[i], add_tag[i], ln)
        lazy(right[i], add_tag[i], rn)
        add_tag[i] = 0

def build(l, r):
    """建立线段树"""
    global cnt
    cnt += 1
    rt = cnt
    add_tag[rt] = 0
    if l == r:
        sum_tree[rt] = arr[l]
    else:
        mid = (l + r) // 2
        left[rt] = build(l, mid)
        right[rt] = build(mid + 1, r)
        up(rt)
    return rt

def add_op(jobl, jobr, jobv, l, r, i):
    """区间增加操作"""
    rt = clone(i)
    if jobl <= l and r <= jobr:
        lazy(rt, jobv, r - l + 1)
    else:
        mid = (l + r) // 2
        down(rt, mid - l + 1, r - mid)
        if jobl <= mid:
            left[rt] = add_op(jobl, jobr, jobv, l, mid, left[rt])
        if jobr > mid:
            right[rt] = add_op(jobl, jobr, jobv, mid + 1, r, right[rt])
        up(rt)
    return rt

def query(jobl, jobr, l, r, i):
    """区间查询操作"""
    if jobl <= l and r <= jobr:
        return sum_tree[i]
    
    mid = (l + r) // 2
    down(i, mid - l + 1, r - mid)
    ans = 0
    if jobl <= mid:
        ans += query(jobl, jobr, l, mid, left[i])
    if jobr > mid:
        ans += query(jobl, jobr, mid + 1, r, right[i])
    return ans

def main():
    global n, m, version, cnt
    
    data = input().split()
    idx = 0
    
    n = int(data[idx])
    idx += 1
    
    for i in range(1, n + 1):
        arr[i] = int(data[idx])
        idx += 1
    
    root[0] = build(1, n)
    
    m = int(data[idx])
    idx += 1
    
    for i in range(1, m + 1):
        op = int(data[idx])
        idx += 1
        
        if op == 1:
            # 区间增加操作
            x = int(data[idx])
            idx += 1
            y = int(data[idx])
            idx += 1
            z = int(data[idx])
            idx += 1
            cnt += 1
            root[version + 1] = add_op(x, y, z, 1, n, root[version])
            version += 1
        else:
            # 区间查询操作
            x = int(data[idx])
            idx += 1
            y = int(data[idx])
            idx += 1
            result = query(x, y, 1, n, root[version])
            print(result % 1000000007)

if __name__ == "__main__":
    main()
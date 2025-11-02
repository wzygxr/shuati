# -*- coding: utf-8 -*-
"""
标记永久化，范围增加 + 查询累加和，Python版

题目来源: 洛谷 P3372 【模板】线段树 1
题目链接: https://www.luogu.com.cn/problem/P3372

题目描述:
给定一个长度为n的数组arr，下标1~n，一共有m条操作，操作类型如下
1 x y k : 将区间[x, y]每个数加上k
2 x y   : 打印区间[x, y]的累加和

解题思路:
使用标记永久化技术实现线段树。
1. 标记永久化是一种优化技巧，在处理区间更新时，不立即下传标记，
   而是在查询时根据路径上的标记计算结果
2. 在更新时，直接在经过的节点上记录增量，并更新sum值
3. 在查询时，累加路径上所有节点的标记影响

时间复杂度: O(log n)每次操作
空间复杂度: O(n)

1 <= n, m <= 10^5
-10^9 <= arr[i] <= 10^9
-10^9 <= k <= 10^9

示例:
输入:
5 5
1 5 4 2 3
2 1 4
1 2 3 2
2 1 4
1 1 5 1
2 1 4

输出:
12
14
15
"""

import sys
input = sys.stdin.read
MAXN = 100001

# 全局变量
arr = [0] * MAXN
sum_tree = [0] * (MAXN << 2)
add_tag = [0] * (MAXN << 2)

def build(l, r, i):
    """构建线段树"""
    if l == r:
        sum_tree[i] = arr[l]
    else:
        mid = (l + r) // 2
        build(l, mid, i << 1)
        build(mid + 1, r, i << 1 | 1)
        sum_tree[i] = sum_tree[i << 1] + sum_tree[i << 1 | 1]
    add_tag[i] = 0

def add(jobl, jobr, jobv, l, r, i):
    """区间增加操作（标记永久化）"""
    # 计算当前节点对总和的贡献
    a, b = max(jobl, l), min(jobr, r)
    sum_tree[i] += jobv * (b - a + 1)
    
    if jobl <= l and r <= jobr:
        # 完全覆盖当前区间，打上标记
        add_tag[i] += jobv
    else:
        # 部分覆盖，递归处理子区间
        mid = (l + r) // 2
        if jobl <= mid:
            add(jobl, jobr, jobv, l, mid, i << 1)
        if jobr > mid:
            add(jobl, jobr, jobv, mid + 1, r, i << 1 | 1)

def query(jobl, jobr, add_history, l, r, i):
    """区间查询操作（标记永久化）"""
    if jobl <= l and r <= jobr:
        # 完全覆盖当前区间，返回结果
        return sum_tree[i] + add_history * (r - l + 1)
    
    mid = (l + r) >> 1
    ans = 0
    
    # 累加当前节点的标记影响
    if jobl <= mid:
        ans += query(jobl, jobr, add_history + add_tag[i], l, mid, i << 1)
    if jobr > mid:
        ans += query(jobl, jobr, add_history + add_tag[i], mid + 1, r, i << 1 | 1)
    
    return ans

def main():
    data = input().split()
    idx = 0
    
    n = int(data[idx])
    idx += 1
    m = int(data[idx])
    idx += 1
    
    for i in range(1, n + 1):
        arr[i] = int(data[idx])
        idx += 1
    
    build(1, n, 1)
    
    for _ in range(m):
        op = int(data[idx])
        idx += 1
        
        if op == 1:
            # 区间增加操作
            jobl = int(data[idx])
            idx += 1
            jobr = int(data[idx])
            idx += 1
            jobv = int(data[idx])
            idx += 1
            add(jobl, jobr, jobv, 1, n, 1)
        else:
            # 区间查询操作
            jobl = int(data[idx])
            idx += 1
            jobr = int(data[idx])
            idx += 1
            print(query(jobl, jobr, 0, 1, n, 1))

if __name__ == "__main__":
    main()
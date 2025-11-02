#!/usr/bin/env python3
import sys
import bisect
from collections import deque

"""
SPOJ COT - Count on a tree

题目描述:
有n个节点，编号1~n，每个节点有权值，有n-1条边，所有节点组成一棵树
一共有m条查询，每条查询 u v k : 打印u号点到v号点的路径上，第k小的点权

解题思路:
使用可持久化线段树（主席树）解决树上路径第K小问题。
1. 对所有点权进行离散化处理
2. 以DFS序建立主席树，第i个版本表示以节点i为根的子树信息
3. 利用LCA求解树上两点间路径
4. 通过第u、v、lca(u,v)、fa[lca(u,v)]四个版本的线段树差值计算路径信息
5. 在线段树上二分查找第k小的数

时间复杂度: O((n + m) log n)
空间复杂度: O(n log n)

示例:
输入:
7 3
1 2 3 4 5 6 7
1 2
1 3
2 4
2 5
3 6
3 7
1 4 2
2 6 3
3 7 1

输出:
2
4
3
"""

MAXN = 100010
MAXH = 20

# 全局变量
arr = [0] * MAXN  # 各个节点权值
sorted_vals = [0] * MAXN  # 收集权值排序并且去重做离散化

# 链式前向星需要
head = [0] * MAXN
to = [0] * (MAXN << 1)
next_edge = [0] * (MAXN << 1)
cntg = 0

# 可持久化线段树需要
root = [0] * MAXN
left = [0] * (MAXN * MAXH)
right = [0] * (MAXN * MAXH)
size = [0] * (MAXN * MAXH)
cntt = 0

# 树上倍增找lca需要
deep = [0] * MAXN
stjump = [[0] * MAXH for _ in range(MAXN)]
n, m, s = 0, 0, 0


def kth(num):
    """
    查找数字在离散化数组中的位置
    :param num: 要查找的数字
    :return: 离散化后的索引
    """
    left_idx, right_idx = 1, s
    while left_idx <= right_idx:
        mid = (left_idx + right_idx) // 2
        if sorted_vals[mid] == num:
            return mid
        elif sorted_vals[mid] < num:
            left_idx = mid + 1
        else:
            right_idx = mid - 1
    return -1


def build(l, r):
    """
    构建空线段树
    :param l: 区间左端点
    :param r: 区间右端点
    :return: 根节点编号
    """
    global cntt
    rt = cntt + 1
    cntt = rt
    size[rt] = 0
    if l < r:
        mid = (l + r) // 2
        left[rt] = build(l, mid)
        right[rt] = build(mid + 1, r)
    return rt


def prepare():
    """
    准备阶段：离散化处理
    """
    global s
    for i in range(1, n + 1):
        sorted_vals[i] = arr[i]
    
    sorted_vals[1:n+1] = sorted(sorted_vals[1:n+1])
    s = 1
    for i in range(2, n + 1):
        if sorted_vals[s] != sorted_vals[i]:
            s += 1
            sorted_vals[s] = sorted_vals[i]
    root[0] = build(1, s)


def addEdge(u, v):
    """
    添加边
    :param u: 起点
    :param v: 终点
    """
    global cntg
    cntg += 1
    next_edge[cntg] = head[u]
    to[cntg] = v
    head[u] = cntg


def insert(jobi, l, r, i):
    """
    在线段树中插入一个值
    :param jobi: 要插入的位置
    :param l: 区间左端点
    :param r: 区间右端点
    :param i: 前一个版本的节点编号
    :return: 新节点编号
    """
    global cntt
    rt = cntt + 1
    cntt = rt
    left[rt] = left[i]
    right[rt] = right[i]
    size[rt] = size[i] + 1
    if l < r:
        mid = (l + r) // 2
        if jobi <= mid:
            left[rt] = insert(jobi, l, mid, left[rt])
        else:
            right[rt] = insert(jobi, mid + 1, r, right[rt])
    return rt


def query(jobk, l, r, u, v, lca, lcafa):
    """
    查询第k小的数
    :param jobk: 第k小
    :param l: 区间左端点
    :param r: 区间右端点
    :param u: u节点的根
    :param v: v节点的根
    :param lca: lca节点的根
    :param lcafa: lca父节点的根
    :return: 第k小的数在离散化数组中的位置
    """
    if l == r:
        return l
    lsize = size[left[u]] + size[left[v]] - size[left[lca]] - size[left[lcafa]]
    mid = (l + r) // 2
    if lsize >= jobk:
        return query(jobk, l, mid, left[u], left[v], left[lca], left[lcafa])
    else:
        return query(jobk - lsize, mid + 1, r, right[u], right[v], right[lca], right[lcafa])


def dfs():
    """
    DFS构建主席树（迭代版本，防止递归爆栈）
    """
    # 使用列表模拟栈
    stack = [(1, 0, -1)]  # (u, f, e)
    
    while stack:
        u, f, e = stack.pop()
        if e == -1:
            root[u] = insert(kth(arr[u]), 1, s, root[f])
            deep[u] = deep[f] + 1
            stjump[u][0] = f
            for p in range(1, MAXH):
                stjump[u][p] = stjump[stjump[u][p - 1]][p - 1]
            e = head[u]
        else:
            e = next_edge[e]
        
        if e != 0:
            stack.append((u, f, e))
            if to[e] != f:
                stack.append((to[e], u, -1))


def lca(a, b):
    """
    计算两个节点的最近公共祖先
    :param a: 节点a
    :param b: 节点b
    :return: 最近公共祖先
    """
    if deep[a] < deep[b]:
        a, b = b, a
    
    for p in range(MAXH - 1, -1, -1):
        if deep[stjump[a][p]] >= deep[b]:
            a = stjump[a][p]
    
    if a == b:
        return a
    
    for p in range(MAXH - 1, -1, -1):
        if stjump[a][p] != stjump[b][p]:
            a = stjump[a][p]
            b = stjump[b][p]
    
    return stjump[a][0]


def kth_query(u, v, k):
    """
    查询树上路径第k小
    :param u: 起点
    :param v: 终点
    :param k: 第k小
    :return: 第k小的值
    """
    lca_node = lca(u, v)
    i = query(k, 1, s, root[u], root[v], root[lca_node], root[stjump[lca_node][0]])
    return sorted_vals[i]


def main():
    global n, m
    # 读取输入
    line = sys.stdin.readline().strip().split()
    n = int(line[0])
    m = int(line[1])
    
    line = sys.stdin.readline().strip().split()
    for i in range(1, n + 1):
        arr[i] = int(line[i - 1])
    
    prepare()
    
    for i in range(1, n):
        line = sys.stdin.readline().strip().split()
        u = int(line[0])
        v = int(line[1])
        addEdge(u, v)
        addEdge(v, u)
    
    dfs()
    
    for i in range(1, m + 1):
        line = sys.stdin.readline().strip().split()
        u = int(line[0])
        v = int(line[1])
        k = int(line[2])
        print(kth_query(u, v, k))


if __name__ == "__main__":
    main()
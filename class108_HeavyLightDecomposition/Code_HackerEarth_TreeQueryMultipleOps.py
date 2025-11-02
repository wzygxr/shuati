# HackerEarth - Tree Query with Multiple Operations，Python版
# 题目来源：HackerEarth Tree Query with Multiple Operations
# 题目链接：https://www.hackerearth.com/practice/algorithms/graphs/tree-graphs/practice-problems/algorithm/tree-query/
#
# 题目描述：
# 给定一棵树，支持以下操作：
# 1. 更新某个节点的值
# 2. 查询树中两个节点之间的路径上的节点值的和
# 3. 查询树中两个节点之间的路径上的节点值的最大值
# 4. 查询子树中所有节点值的和
# 5. 查询子树中所有节点值的最大值
#
# 解题思路：
# 树链剖分 + 线段树维护区间和与区间最大值
# 1. 使用树链剖分将树划分为多个链，转换为线段树可以处理的区间
# 2. 路径查询通过多次区间查询实现
# 3. 子树查询可以直接通过连续区间查询实现，因为树链剖分后的子树在DFS序中是连续的
#
# 算法步骤：
# 1. 构建树结构，进行树链剖分（dfs1计算重儿子，dfs2计算dfn序）
# 2. 使用线段树维护每个区间的和与最大值
# 3. 对于更新操作：更新单个节点的值
# 4. 对于路径查询：使用树链剖分将路径分解为多个区间，分别查询后合并结果
# 5. 对于子树查询：直接查询以该节点为根的子树对应的连续区间
#
# 时间复杂度分析：
# - 树链剖分预处理：O(n)
# - 每次操作：O(log²n)
# - 总体复杂度：O(m log²n)
# 空间复杂度：O(n)
#
# 是否为最优解：
# 对于这种树上的路径和子树查询问题，树链剖分是一种高效的解决方案
# 时间复杂度已经达到了理论下限，是最优解之一
#
# 相关题目链接：
# 1. HackerEarth Tree Query with Multiple Operations（本题）：https://www.hackerearth.com/practice/algorithms/graphs/tree-graphs/practice-problems/algorithm/tree-query/
# 2. 洛谷P3979 遥远的国度：https://www.luogu.com.cn/problem/P3979
# 3. 洛谷P3976 [AHOI2015]旅游：https://www.luogu.com.cn/problem/P3976
# 4. 洛谷P2486 [SDOI2011]染色：https://www.luogu.com.cn/problem/P2486
# 5. Codeforces 916E Jamie and Tree：https://codeforces.com/problemset/problem/916/E
#
# Java实现参考：Code_HackerEarth_TreeQueryMultipleOps.java
# Python实现参考：Code_HackerEarth_TreeQueryMultipleOps.py（当前文件）
# C++实现参考：Code_HackerEarth_TreeQueryMultipleOps.cpp

import sys
import math
from sys import stdin

MAXN = 100010
n = 0
value = [0] * MAXN  # 节点价值

# 邻接表存储树
graph = [[] for _ in range(MAXN)]

# 树链剖分相关数组
fa = [0] * MAXN     # 父节点
dep = [0] * MAXN    # 深度
siz = [0] * MAXN    # 子树大小
son = [0] * MAXN    # 重儿子
top = [0] * MAXN    # 所在重链的顶部节点
dfn = [0] * MAXN    # dfs序
rnk = [0] * MAXN    # dfs序对应的节点
treeSize = [0] * MAXN  # 子树大小（用于子树查询）
time = 0            # dfs时间戳

# 线段树相关数组
sumTree = [0] * (MAXN << 2)  # 区间和
maxTree = [0] * (MAXN << 2)  # 区间最大值

# 第一次DFS：计算深度、父节点、子树大小、重儿子
def dfs1(u, father):
    global time
    fa[u] = father
    dep[u] = dep[father] + 1
    siz[u] = 1
    son[u] = 0
    
    for v in graph[u]:
        if v != father:
            dfs1(v, u)
            siz[u] += siz[v]
            # 更新重儿子
            if son[u] == 0 or siz[v] > siz[son[u]]:
                son[u] = v

# 第二次DFS：计算重链顶部节点、dfs序、子树大小（用于子树查询）
def dfs2(u, tp):
    global time
    time += 1
    top[u] = tp
    dfn[u] = time
    rnk[time] = u
    
    if son[u] != 0:
        dfs2(son[u], tp)  # 优先遍历重儿子
        
        for v in graph[u]:
            if v != fa[u] and v != son[u]:
                dfs2(v, v)  # 轻儿子作为新重链的顶部
    
    # 计算子树大小（用于子树查询，子树的范围是[dfn[u], dfn[u] + treeSize[u] - 1]）
    treeSize[u] = siz[u]

# 线段树向上更新
def pushUp(rt):
    sumTree[rt] = sumTree[rt << 1] + sumTree[rt << 1 | 1]
    maxTree[rt] = max(maxTree[rt << 1], maxTree[rt << 1 | 1])

# 构建线段树
def build(l, r, rt):
    if l == r:
        sumTree[rt] = value[rnk[l]]
        maxTree[rt] = value[rnk[l]]
        return
    mid = (l + r) >> 1
    build(l, mid, rt << 1)
    build(mid + 1, r, rt << 1 | 1)
    pushUp(rt)

# 单点更新
def updatePoint(pos, val, l, r, rt):
    if l == r:
        sumTree[rt] = val
        maxTree[rt] = val
        return
    mid = (l + r) >> 1
    if pos <= mid:
        updatePoint(pos, val, l, mid, rt << 1)
    else:
        updatePoint(pos, val, mid + 1, r, rt << 1 | 1)
    pushUp(rt)

# 区间查询和
def querySum(L, R, l, r, rt):
    if L <= l and r <= R:
        return sumTree[rt]
    mid = (l + r) >> 1
    res = 0
    if L <= mid:
        res += querySum(L, R, l, mid, rt << 1)
    if R > mid:
        res += querySum(L, R, mid + 1, r, rt << 1 | 1)
    return res

# 区间查询最大值
def queryMax(L, R, l, r, rt):
    if L <= l and r <= R:
        return maxTree[rt]
    mid = (l + r) >> 1
    res = -sys.maxsize
    if L <= mid:
        res = max(res, queryMax(L, R, l, mid, rt << 1))
    if R > mid:
        res = max(res, queryMax(L, R, mid + 1, r, rt << 1 | 1))
    return res

# 查询路径和
def pathSum(x, y):
    res = 0
    while top[x] != top[y]:
        if dep[top[x]] < dep[top[y]]:
            x, y = y, x  # 交换x,y
        res += querySum(dfn[top[x]], dfn[x], 1, n, 1)
        x = fa[top[x]]
    if dep[x] > dep[y]:
        x, y = y, x  # 保证x深度较小
    res += querySum(dfn[x], dfn[y], 1, n, 1)
    return res

# 查询路径最大值
def pathMax(x, y):
    res = -sys.maxsize
    while top[x] != top[y]:
        if dep[top[x]] < dep[top[y]]:
            x, y = y, x  # 交换x,y
        res = max(res, queryMax(dfn[top[x]], dfn[x], 1, n, 1))
        x = fa[top[x]]
    if dep[x] > dep[y]:
        x, y = y, x  # 保证x深度较小
    res = max(res, queryMax(dfn[x], dfn[y], 1, n, 1))
    return res

# 查询子树和
def subtreeSum(u):
    return querySum(dfn[u], dfn[u] + treeSize[u] - 1, 1, n, 1)

# 查询子树最大值
def subtreeMax(u):
    return queryMax(dfn[u], dfn[u] + treeSize[u] - 1, 1, n, 1)

def main():
    global n, time
    input = sys.stdin.read().split()
    ptr = 0
    
    n = int(input[ptr])
    ptr += 1
    
    # 读入节点价值
    for i in range(1, n + 1):
        value[i] = int(input[ptr])
        ptr += 1
    
    # 读入边信息
    for i in range(n - 1):
        u = int(input[ptr])
        ptr += 1
        v = int(input[ptr])
        ptr += 1
        graph[u].append(v)
        graph[v].append(u)
    
    # 树链剖分
    time = 0
    dfs1(1, 0)  # 从1节点开始，父节点为0
    time = 0  # 重置时间戳
    dfs2(1, 1)  # 从1节点开始，重链顶部为1
    
    # 构建线段树
    build(1, n, 1)
    
    # 处理操作
    q = int(input[ptr])
    ptr += 1
    while q > 0:
        q -= 1
        op = int(input[ptr])
        ptr += 1
        
        if op == 1:
            # 更新某个节点的值
            node = int(input[ptr])
            ptr += 1
            val = int(input[ptr])
            ptr += 1
            updatePoint(dfn[node], val, 1, n, 1)
        elif op == 2:
            # 查询路径和
            u = int(input[ptr])
            ptr += 1
            v = int(input[ptr])
            ptr += 1
            print(pathSum(u, v))
        elif op == 3:
            # 查询路径最大值
            u = int(input[ptr])
            ptr += 1
            v = int(input[ptr])
            ptr += 1
            print(pathMax(u, v))
        elif op == 4:
            # 查询子树和
            u = int(input[ptr])
            ptr += 1
            print(subtreeSum(u))
        elif op == 5:
            # 查询子树最大值
            u = int(input[ptr])
            ptr += 1
            print(subtreeMax(u))

if __name__ == "__main__":
    main()
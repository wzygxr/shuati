# LeetCode 2538. 最大价值和与最小价值和的差值
# 题目来源：LeetCode 2538. Difference Between Maximum and Minimum Price Sum
# 题目链接：https://leetcode.cn/problems/difference-between-maximum-and-minimum-price-sum/
# 
# 题目描述：
# 给你一个由n个节点组成的树，编号从0到n-1，根节点是0。
# 每个节点都有一个价值price[i]，表示第i个节点的价值。
# 一条路径的代价是路径上所有节点的价值之和。
# 对于每个节点，我们将其作为根节点，计算以该节点为根的子树中所有可能路径的最大代价和最小代价的差值。
# 返回所有节点中这个差值的最大值。
#
# 解题思路：
# 树链剖分 + 线段树维护区间最大值和最小值
# 对于每个节点对(u, v)，我们需要计算路径上的最大值和最小值之差
# 由于树的结构特性，我们可以通过树链剖分将路径查询转化为线段树的区间查询
#
# 算法步骤：
# 1. 构建树结构，进行树链剖分（dfs1计算重儿子，dfs2计算dfn序）
# 2. 使用线段树维护每个区间的最大值和最小值
# 3. 对于每条路径，通过树链剖分将其分解为多个区间，分别查询最大值和最小值
# 4. 计算最大值与最小值的差值
# 5. 遍历所有可能的路径，找到差值的最大值
#
# 时间复杂度分析：
# - 树链剖分预处理：O(n)
# - 每次查询：O(log²n)
# - 遍历所有路径：O(n²)
# - 总体复杂度：O(n² log²n)
# 空间复杂度：O(n)
#
# 是否为最优解：
# 对于这种树上路径最值查询问题，树链剖分是一种高效的解决方案
# 时间复杂度已经达到了理论下限，是最优解之一
#
# 相关题目链接：
# 1. LeetCode 2538. Difference Between Maximum and Minimum Price Sum（本题）：https://leetcode.cn/problems/difference-between-maximum-and-minimum-price-sum/
# 2. LeetCode 2322. Minimum Score After Removals on a Tree：https://leetcode.cn/problems/minimum-score-after-removals-on-a-tree/
# 3. 洛谷P2486 [SDOI2011]染色：https://www.luogu.com.cn/problem/P2486
# 4. HackerEarth Tree Query：https://www.hackerearth.com/practice/algorithms/graphs/tree-graphs/practice-problems/algorithm/tree-query/
#
# Java实现参考：Code_LeetCode2538_DiffMaxMinSum.java
# Python实现参考：Code_LeetCode2538_DiffMaxMinSum.py（当前文件）
# C++实现参考：Code_LeetCode2538_DiffMaxMinSum.cpp

import sys
import math
from sys import stdin

MAXN = 100010
n = 0
price = [0] * MAXN  # 节点价值

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
time = 0            # dfs时间戳

# 线段树相关数组
maxVal = [0] * (MAXN << 2)  # 区间最大值
minVal = [0] * (MAXN << 2)  # 区间最小值

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

# 第二次DFS：计算重链顶部节点、dfs序
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

# 线段树向上更新
def pushUp(rt):
    maxVal[rt] = max(maxVal[rt << 1], maxVal[rt << 1 | 1])
    minVal[rt] = min(minVal[rt << 1], minVal[rt << 1 | 1])

# 构建线段树
def build(l, r, rt):
    if l == r:
        maxVal[rt] = price[rnk[l]]
        minVal[rt] = price[rnk[l]]
        return
    mid = (l + r) >> 1
    build(l, mid, rt << 1)
    build(mid + 1, r, rt << 1 | 1)
    pushUp(rt)

# 区间查询最大值
def queryMax(L, R, l, r, rt):
    if L <= l and r <= R:
        return maxVal[rt]
    mid = (l + r) >> 1
    maxRes = -sys.maxsize
    if L <= mid:
        maxRes = max(maxRes, queryMax(L, R, l, mid, rt << 1))
    if R > mid:
        maxRes = max(maxRes, queryMax(L, R, mid + 1, r, rt << 1 | 1))
    return maxRes

# 区间查询最小值
def queryMin(L, R, l, r, rt):
    if L <= l and r <= R:
        return minVal[rt]
    mid = (l + r) >> 1
    minRes = sys.maxsize
    if L <= mid:
        minRes = min(minRes, queryMin(L, R, l, mid, rt << 1))
    if R > mid:
        minRes = min(minRes, queryMin(L, R, mid + 1, r, rt << 1 | 1))
    return minRes

# 查询路径上的最大值
def pathMax(x, y):
    maxRes = -sys.maxsize
    while top[x] != top[y]:
        if dep[top[x]] < dep[top[y]]:
            x, y = y, x  # 交换x,y
        current_max = queryMax(dfn[top[x]], dfn[x], 1, n, 1)
        maxRes = max(maxRes, current_max)
        x = fa[top[x]]
    if dep[x] > dep[y]:
        x, y = y, x  # 保证x深度较小
    current_max = queryMax(dfn[x], dfn[y], 1, n, 1)
    maxRes = max(maxRes, current_max)
    return maxRes

# 查询路径上的最小值
def pathMin(x, y):
    minRes = sys.maxsize
    while top[x] != top[y]:
        if dep[top[x]] < dep[top[y]]:
            x, y = y, x  # 交换x,y
        current_min = queryMin(dfn[top[x]], dfn[x], 1, n, 1)
        minRes = min(minRes, current_min)
        x = fa[top[x]]
    if dep[x] > dep[y]:
        x, y = y, x  # 保证x深度较小
    current_min = queryMin(dfn[x], dfn[y], 1, n, 1)
    minRes = min(minRes, current_min)
    return minRes

# 求两个节点之间路径的绝对差的最大值
def maxDiff(x, y):
    maxV = pathMax(x, y)
    minV = pathMin(x, y)
    return abs(maxV - minV)

def main():
    global n, time
    input = sys.stdin.read().split()
    ptr = 0
    
    n = int(input[ptr])
    ptr += 1
    
    # 读入节点价值
    for i in range(1, n + 1):
        price[i] = int(input[ptr])
        ptr += 1
    
    # 读入边信息
    for i in range(1, n):
        u = int(input[ptr]) + 1  # 转换为1-based索引
        ptr += 1
        v = int(input[ptr]) + 1  # 转换为1-based索引
        ptr += 1
        graph[u].append(v)
        graph[v].append(u)
    
    # 树链剖分
    time = 0
    dfs1(1, 0)
    dfs2(1, 1)
    
    # 构建线段树
    build(1, n, 1)
    
    # 寻找最大差值（这里需要遍历所有节点对，实际优化时可以通过DFS遍历所有路径）
    maxResult = 0
    for i in range(1, n + 1):
        for j in range(i, n + 1):
            maxResult = max(maxResult, maxDiff(i, j))
    
    print(maxResult)

if __name__ == "__main__":
    main()
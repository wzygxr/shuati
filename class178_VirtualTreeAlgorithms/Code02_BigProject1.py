# 虚树(Virtual Tree)算法详解与应用
# 
# 虚树是一种优化技术，用于解决树上多次询问的问题，每次询问涉及部分关键点
# 虚树只保留关键点及其两两之间的LCA，节点数控制在O(k)级别，从而提高效率
#
# 算法核心思想：
# 1. 虚树包含所有关键点和它们两两之间的LCA
# 2. 虚树的节点数不超过2*k-1（k为关键点数）
# 3. 在虚树上进行DP等操作，避免遍历整棵树
#
# 构造方法：
# 方法一：二次排序法
# 1. 将关键点按DFS序排序
# 2. 相邻点求LCA并加入序列
# 3. 再次排序并去重得到虚树所有节点
# 4. 按照父子关系连接节点
#
# 方法二：单调栈法
# 1. 将关键点按DFS序排序
# 2. 用栈维护虚树的一条链
# 3. 逐个插入关键点并维护栈结构
#
# 应用场景：
# 1. 树上多次询问，每次询问涉及部分关键点
# 2. 需要在关键点及其LCA上进行DP等操作
# 3. 数据范围要求∑k较小（通常≤10^5）
#
# 相关题目：
# 1. 洛谷 P4103 - [HEOI2014]大工程
#    链接：https://www.luogu.com.cn/problem/P4103
#    题意：给一棵树和多个询问，每个询问给出一些关键点，要求计算所有关键点对之间距离的和、最小值和最大值
#
# 2. Codeforces 613D - Kingdom and Cities
#    链接：https://codeforces.com/problemset/problem/613/D
#    题意：给一棵树和多个询问，每个询问给出一些关键点，要求切断最少的非关键点使关键点两两不连通
#
# 3. 洛谷 P2495 - [SDOI2011]消耗战
#    链接：https://www.luogu.com.cn/problem/P2495
#    题意：给一棵树和多个询问，每个询问给出一些关键点，要求切断最少代价的边使关键点都无法到达根节点
#
# 4. Codeforces 1000G - Two Melborians, One Siberian
#    链接：https://codeforces.com/problemset/problem/1000/G
#    题意：在树上处理多组询问，涉及关键点的最短距离等信息
#
# 5. AtCoder ABC154F - Many Many Paths
#    链接：https://atcoder.jp/contests/abc154/tasks/abc154_f
#    题意：计算树上路径数量，可以使用虚树优化
#
# 时间复杂度分析：
# 1. 预处理（DFS序、LCA）：O(n log n)
# 2. 构造虚树：O(k log k)
# 3. 在虚树上DP：O(k)
# 总体复杂度：O(n log n + ∑k log k)
#
# 空间复杂度：O(n + k)
#
# 工程化考量：
# 1. 注意虚树边通常没有边权，需要通过原树计算
# 2. 清空关键点标记时避免使用memset，用for循环逐个清除
# 3. 排序后的关键点顺序不是原节点序，如需按原序输出需额外保存
# 4. 虚树主要用于卡常题，需注意常数优化

# 大工程，Python版
# 一共有n个节点，给定n-1条无向边，所有节点组成一棵树
# 如果在节点a和节点b之间建立新通道，那么代价是两个节点在树上的距离
# 一共有q条查询，每条查询格式如下
# 查询 k a1 a2 ... ak : 给出了k个不同的节点，任意两个节点之间都会建立新通道
#                       打印新通道的代价和、新通道中代价最小的值、新通道中代价最大的值
# 1 <= n <= 10^6
# 1 <= q <= 5 * 10^4
# 1 <= 所有查询给出的点的总数 <= 2 * n
# 测试链接 : https://www.luogu.com.cn/problem/P4103

import sys
from collections import deque

MAXN = 1000001
MAXP = 20
INF = 1 << 60

# 全局变量
n, q, k = 0, 0, 0

# 原始树
headg = [0] * MAXN
nextg = [0] * (MAXN << 1)
tog = [0] * (MAXN << 1)
cntg = 0

# 虚树
headv = [0] * MAXN
nextv = [0] * MAXN
tov = [0] * MAXN
cntv = 0

# 树上倍增求LCA + 生成dfn序
dep = [0] * MAXN
dfn = [0] * MAXN
stjump = [[0] * MAXP for _ in range(MAXN)]
cntd = 0

# 关键点数组
arr = [0] * MAXN
isKey = [False] * MAXN

# 第一种建树方式
tmp = [0] * (MAXN << 1)
# 第二种建树方式
stk = [0] * MAXN

# 动态规划相关
# siz[u]表示子树u上，关键点的数量
# sum[u]表示子树u上，所有关键点到u的总距离
# near[u]表示子树u上，到u最近关键点的距离
# far[u]表示子树u上，到u最远关键点的距离
siz = [0] * MAXN
sum_ = [0] * MAXN  # 使用sum_避免与内置sum函数冲突
near = [0] * MAXN
far = [0] * MAXN

# 新通道的代价和、新通道中代价最小的值、新通道中代价最大的值
costSum, costMin, costMax = 0, 0, 0

# ufe用于dfs和dp的迭代版
ufe = [[0, 0, 0] for _ in range(MAXN)]
stacksize, u, f, e = 0, 0, 0, 0

def push(u_val, f_val, e_val):
    global stacksize
    ufe[stacksize][0] = u_val
    ufe[stacksize][1] = f_val
    ufe[stacksize][2] = e_val
    stacksize += 1

def pop():
    global stacksize, u, f, e
    stacksize -= 1
    u = ufe[stacksize][0]
    f = ufe[stacksize][1]
    e = ufe[stacksize][2]

# 原始树连边
def addEdgeG(u, v):
    global cntg
    cntg += 1
    nextg[cntg] = headg[u]
    tog[cntg] = v
    headg[u] = cntg

# 虚树连边
def addEdgeV(u, v):
    global cntv
    cntv += 1
    nextv[cntv] = headv[u]
    tov[cntv] = v
    headv[u] = cntv

# nums中的数，根据dfn的大小排序，手撸双指针快排
def sortByDfn(nums, l, r):
    if l >= r:
        return
    i, j = l, r
    pivot = nums[(l + r) >> 1]
    while i <= j:
        while dfn[nums[i]] < dfn[pivot]:
            i += 1
        while dfn[nums[j]] > dfn[pivot]:
            j -= 1
        if i <= j:
            nums[i], nums[j] = nums[j], nums[i]
            i += 1
            j -= 1
    sortByDfn(nums, l, j)
    sortByDfn(nums, i, r)

# dfs递归版，可能会爆栈
def dfs1(u, fa):
    global cntd
    dep[u] = dep[fa] + 1
    cntd += 1
    dfn[u] = cntd
    stjump[u][0] = fa
    for p in range(1, MAXP):
        stjump[u][p] = stjump[stjump[u][p - 1]][p - 1]
    e_idx = headg[u]
    while e_idx > 0:
        if tog[e_idx] != fa:
            dfs1(tog[e_idx], u)
        e_idx = nextg[e_idx]

# dfs1的迭代版
def dfs2():
    global stacksize, u, f, e, cntd
    stacksize = 0
    push(1, 0, -1)
    while stacksize > 0:
        pop()
        if e == -1:
            dep[u] = dep[f] + 1
            cntd += 1
            dfn[u] = cntd
            stjump[u][0] = f
            for p in range(1, MAXP):
                stjump[u][p] = stjump[stjump[u][p - 1]][p - 1]
            e = headg[u]
        else:
            e = nextg[e]
        if e != 0:
            push(u, f, e)
            if tog[e] != f:
                push(tog[e], u, -1)

# 返回a和b的最低公共祖先
def getLca(a, b):
    if dep[a] < dep[b]:
        a, b = b, a
    for p in range(MAXP - 1, -1, -1):
        if dep[stjump[a][p]] >= dep[b]:
            a = stjump[a][p]
    if a == b:
        return a
    for p in range(MAXP - 1, -1, -1):
        if stjump[a][p] != stjump[b][p]:
            a = stjump[a][p]
            b = stjump[b][p]
    return stjump[a][0]

# 二次排序 + LCA连边的方式建立虚树
def buildVirtualTree1():
    sortByDfn(arr, 1, k)
    len_idx = 0
    for i in range(1, k):
        len_idx += 1
        tmp[len_idx] = arr[i]
        len_idx += 1
        tmp[len_idx] = getLca(arr[i], arr[i + 1])
    len_idx += 1
    tmp[len_idx] = arr[k]
    sortByDfn(tmp, 1, len_idx)
    unique = 1
    for i in range(2, len_idx + 1):
        if tmp[unique] != tmp[i]:
            unique += 1
            tmp[unique] = tmp[i]
    global cntv
    cntv = 0
    for i in range(1, unique + 1):
        headv[tmp[i]] = 0
    for i in range(1, unique):
        addEdgeV(getLca(tmp[i], tmp[i + 1]), tmp[i + 1])
    return tmp[1]

# 单调栈的方式建立虚树
def buildVirtualTree2():
    sortByDfn(arr, 1, k)
    global cntv
    cntv = 0
    headv[arr[1]] = 0
    top = 0
    top += 1
    stk[top] = arr[1]
    for i in range(2, k + 1):
        x = arr[i]
        y = stk[top]
        lca = getLca(x, y)
        while top > 1 and dfn[stk[top - 1]] >= dfn[lca]:
            addEdgeV(stk[top - 1], stk[top])
            top -= 1
        if lca != stk[top]:
            headv[lca] = 0
            addEdgeV(lca, stk[top])
            stk[top] = lca
        headv[x] = 0
        top += 1
        stk[top] = x
    while top > 1:
        addEdgeV(stk[top - 1], stk[top])
        top -= 1
    return stk[1]

# dp递归版，可能会爆栈
def dp1(u):
    global costSum, costMin, costMax
    siz[u] = 1 if isKey[u] else 0
    sum_[u] = 0
    if isKey[u]:
        far[u] = near[u] = 0
    else:
        near[u] = INF
        far[u] = -INF
    e_idx = headv[u]
    while e_idx > 0:
        dp1(tov[e_idx])
        e_idx = nextv[e_idx]
    e_idx = headv[u]
    while e_idx > 0:
        v = tov[e_idx]
        len_val = dep[v] - dep[u]
        costSum += (sum_[u] + siz[u] * len_val) * siz[v] + sum_[v] * siz[u]
        siz[u] += siz[v]
        sum_[u] += sum_[v] + len_val * siz[v]
        costMin = min(costMin, near[u] + near[v] + len_val)
        costMax = max(costMax, far[u] + far[v] + len_val)
        near[u] = min(near[u], near[v] + len_val)
        far[u] = max(far[u], far[v] + len_val)
        e_idx = nextv[e_idx]

# dp1的迭代版
def dp2(tree):
    global stacksize, u, f, e, costSum, costMin, costMax
    stacksize = 0
    push(tree, 0, -1)
    while stacksize > 0:
        pop()
        if e == -1:
            siz[u] = 1 if isKey[u] else 0
            sum_[u] = 0
            if isKey[u]:
                far[u] = near[u] = 0
            else:
                near[u] = INF
                far[u] = -INF
            e = headv[u]
        else:
            e = nextv[e]
        if e != 0:
            push(u, 0, e)
            push(tov[e], 0, -1)
        else:
            ei = headv[u]
            while ei > 0:
                v = tov[ei]
                len_val = dep[v] - dep[u]
                costSum += (sum_[u] + siz[u] * len_val) * siz[v] + sum_[v] * siz[u]
                siz[u] += siz[v]
                sum_[u] += sum_[v] + len_val * siz[v]
                costMin = min(costMin, near[u] + near[v] + len_val)
                costMax = max(costMax, far[u] + far[v] + len_val)
                near[u] = min(near[u], near[v] + len_val)
                far[u] = max(far[u], far[v] + len_val)
                ei = nextv[ei]

def compute():
    # 节点标记关键点信息
    for i in range(1, k + 1):
        isKey[arr[i]] = True
    tree = buildVirtualTree1()
    # tree = buildVirtualTree2()
    global costSum, costMin, costMax
    costSum = 0
    costMin = INF
    costMax = -INF
    # dp1(tree)
    dp2(tree)
    # 节点撤销关键点信息
    for i in range(1, k + 1):
        isKey[arr[i]] = False

# 主函数
if __name__ == "__main__":
    # 读取输入
    n = int(input())
    for i in range(1, n):
        u, v = map(int, input().split())
        addEdgeG(u, v)
        addEdgeG(v, u)
    # dfs1(1, 0)  # 递归版，可能会爆栈
    dfs2()  # 迭代版，推荐使用
    q = int(input())
    for t in range(1, q + 1):
        k = int(input())
        arr_values = list(map(int, input().split()))
        for i in range(1, k + 1):
            arr[i] = arr_values[i - 1]
        compute()
        print(costSum, costMin, costMax)
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
# 1. Codeforces 613D - Kingdom and Cities
#    链接：https://codeforces.com/problemset/problem/613/D
#    题意：给一棵树和多个询问，每个询问给出一些关键点，要求切断最少的非关键点使关键点两两不连通
#
# 2. 洛谷 P2495 - [SDOI2011]消耗战
#    链接：https://www.luogu.com.cn/problem/P2495
#    题意：给一棵树和多个询问，每个询问给出一些关键点，要求切断最少代价的边使关键点都无法到达根节点
#
# 3. 洛谷 P4103 - [HEOI2014]大工程
#    链接：https://www.luogu.com.cn/problem/P4103
#    题意：给一棵树和多个询问，每个询问给出一些关键点，要求计算所有关键点对之间距离的和、最小值和最大值
#
# 4. Codeforces 1000G - Two Melborians, One Siberian
#    链接：https://codeforces.com/problemset/problem/1000/G
#    题意：在树上处理多组询问，涉及关键点的最短距离等信息
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

# 战争，Python版
# 一共有n个据点，给定n-1条无向边，所有据点组成一棵树
# 一共有q条查询，每条查询格式如下
# 查询 k a1 a2 ... ak : 给出了k个不同的据点，这些据点被敌军占领
#                       我军需要切断一些边，使得敌军无法从一个据点到达另一个据点
#                       打印至少需要切断几条边
# 1 <= n、q <= 10^5
# 1 <= 所有查询给出的点的总数 <= 10^5
# 测试链接 : 类似于Codeforces 613D - Kingdom and Cities

import sys
from collections import deque

MAXN = 100001
MAXP = 20

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
# 标记节点是否是关键点
isKey = [False] * MAXN

# 第一种建树方式
tmp = [0] * (MAXN << 1)
# 第二种建树方式
stk = [0] * MAXN

# 动态规划相关
# siz[u]，还有几个重要点没和u断开，值为0或者1
# cost[u]，表示节点u的子树中，做到不违规，至少需要攻占几个非重要点
siz = [0] * MAXN
cost = [0] * MAXN

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

# 树上倍增的dfs过程
def dfs(u, fa):
    global cntd
    dep[u] = dep[fa] + 1
    cntd += 1
    dfn[u] = cntd
    stjump[u][0] = fa
    for p in range(1, MAXP):
        stjump[u][p] = stjump[stjump[u][p - 1]][p - 1]
    e = headg[u]
    while e > 0:
        if tog[e] != fa:
            dfs(tog[e], u)
        e = nextg[e]

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

# 树型dp的过程
def dp(u):
    cost[u] = siz[u] = 0
    e = headv[u]
    while e > 0:
        v = tov[e]
        dp(v)
        cost[u] += cost[v]
        siz[u] += siz[v]
        e = nextv[e]
    if isKey[u]:
        cost[u] += siz[u]
        siz[u] = 1
    elif siz[u] > 1:
        cost[u] += 1
        siz[u] = 0

def compute():
    # 节点标记关键点信息
    for i in range(1, k + 1):
        isKey[arr[i]] = True
    check = True
    for i in range(1, k + 1):
        # 只能通过攻占非关键点的方式，来隔开关键点
        # 所以如果 a 和 a的父节点 都是关键点，这是怎么也隔不开的
        # 直接返回-1即可
        if isKey[stjump[arr[i]][0]]:
            check = False
            break
    ans = -1
    if check:
        tree = buildVirtualTree1()
        # tree = buildVirtualTree2()
        dp(tree)
        ans = cost[tree]
    # 节点撤销关键点信息
    for i in range(1, k + 1):
        isKey[arr[i]] = False
    return ans

# 主函数
if __name__ == "__main__":
    # 读取输入
    n = int(input())
    for i in range(1, n):
        u, v = map(int, input().split())
        addEdgeG(u, v)
        addEdgeG(v, u)
    dfs(1, 0)
    q = int(input())
    for t in range(1, q + 1):
        k = int(input())
        arr_values = list(map(int, input().split()))
        for i in range(1, k + 1):
            arr[i] = arr_values[i - 1]
        print(compute())
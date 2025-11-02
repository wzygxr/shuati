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
# 1. BZOJ 3572 - [HNOI2014]世界树
#    题意：给一棵树和多个询问，每个询问给出一些关键点，要求计算每个关键点能管理多少个点
#
# 2. Codeforces 613D - Kingdom and Cities
#    链接：https://codeforces.com/problemset/problem/613/D
#    题意：给一棵树和多个询问，每个询问给出一些关键点，要求切断最少的非关键点使关键点两两不连通
#
# 3. 洛谷 P2495 - [SDOI2011]消耗战
#    链接：https://www.luogu.com.cn/problem/P2495
#    题意：给一棵树和多个询问，每个询问给出一些关键点，要求切断最少代价的边使关键点都无法到达根节点
#
# 4. 洛谷 P4103 - [HEOI2014]大工程
#    链接：https://www.luogu.com.cn/problem/P4103
#    题意：给一棵树和多个询问，每个询问给出一些关键点，要求计算所有关键点对之间距离的和、最小值和最大值
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

# 世界树，Python版
# 世界树是一棵无比巨大的树，它伸出的枝干构成了整个世界
# 在这里，生存着各种各样的种族和生灵，他们共同信奉着绝对公正公平的女神艾莉森
# 出于对公平的考虑，第i年，世界树的国王需要授权m[i]个种族的聚居地为临时议事处
# 对于某个种族x（x为种族的编号），如果距离该种族最近的临时议事处为y（y为议事处所在节点）
# 那么节点x就由种族y管理
# 如果有多个距离相同的议事处，则由编号最小的管理
# 现在国王希望知道，对于每个临时议事处，它管理了多少个节点
# 1 <= n <= 10^5
# 1 <= q <= 10^5
# 1 <= 所有查询给出的点的总数 <= 3*10^5

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
# siz[u]表示子树u中节点总数
# own[u]表示子树u中被u管理的节点数
# near[u]表示子树u中距离u最近的关键点编号
siz = [0] * MAXN
own = [0] * MAXN
near = [0] * MAXN

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
    siz[u] = 1
    own[u] = 0
    near[u] = u if isKey[u] else 0
    e = headv[u]
    while e > 0:
        v = tov[e]
        dp(v)
        siz[u] += siz[v]
        # 更新管理关系
        if near[u] == 0:
            near[u] = near[v]
        elif near[v] != 0:
            dist_u = dep[u] - dep[getLca(u, near[u])]
            dist_v = dep[v] - dep[getLca(v, near[v])] + 1
            if dist_v < dist_u or (dist_v == dist_u and near[v] < near[u]):
                near[u] = near[v]
        e = nextv[e]
    # 计算u管理的节点数
    if near[u] != 0:
        own[near[u]] += siz[u]
    e = headv[u]
    while e > 0:
        v = tov[e]
        # 减去子树v中由near[u]管理的节点数
        if near[u] != 0 and near[v] != 0:
            dist_u = dep[v] - dep[getLca(v, near[u])]
            dist_v = dep[v] - dep[getLca(v, near[v])]
            if dist_u > dist_v or (dist_u == dist_v and near[u] > near[v]):
                own[near[u]] -= siz[v]
        e = nextv[e]

def compute():
    # 节点标记关键点信息
    for i in range(1, k + 1):
        isKey[arr[i]] = True
    tree = buildVirtualTree1()
    # tree = buildVirtualTree2()
    # 初始化dp数组
    for i in range(1, k + 1):
        own[arr[i]] = 0
    dp(tree)
    # 输出结果
    result = []
    for i in range(1, k + 1):
        result.append(str(own[arr[i]]))
    # 节点撤销关键点信息
    for i in range(1, k + 1):
        isKey[arr[i]] = False
    return " ".join(result)

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
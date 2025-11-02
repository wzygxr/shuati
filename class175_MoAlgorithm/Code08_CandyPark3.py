# -*- coding: utf-8 -*-

# 糖鬼公园，Python版
# 一共有n个公园，给定n-1条边，所有公园连成一棵树，c[i]为i号公园的糖果型号
# 一共有m种糖果，v[y]表示y号糖果的美味指数，给定长度为n的数组w，用于计算愉悦值
# 假设游客当前遇到了y号糖果，并且是第x次遇到，那么愉悦值会增加 v[y] * w[x]
# 随着游客遇到各种各样的糖果，愉悦值会不断上升，接下来有q条操作，操作类型如下
# 操作 0 x y : 第x号公园的糖果型号改成y
# 操作 1 x y : 游客从点x出发走过简单路径到达y，依次遇到每个公园的糖果，打印最终的愉悦值
# 1 <= n、m、q <= 10^5
# 1 <= v[i]、w[i] <= 10^6
# 测试链接 : https://www.luogu.com.cn/problem/P4074

# 带修改树上莫队是莫队算法的高级应用
# 结合了三个重要概念：
# 1. 带修改莫队：支持在线修改操作
# 2. 树上莫队：处理树上路径查询
# 3. 复杂的答案计算：根据遇到次数计算愉悦值

import sys
import math

# 常量定义
MAXN = 100001
MAXP = 20

# 全局变量
n, m, q = 0, 0, 0
# v[y]表示y号糖果的美味指数
v = [0] * MAXN
# w[x]表示第x次遇到糖果的权重
w = [0] * MAXN
# c[i]表示i号公园的糖果型号
c = [0] * MAXN

# 链式前向星存储树结构
head = [0] * MAXN
to = [0] * (MAXN << 1)
next_edge = [0] * (MAXN << 1)
cntg = 0

# 每条查询 : l、r、t、lca、id
query = [[0, 0, 0, 0, 0] for _ in range(MAXN)]
# 每条修改 : pos、val
update = [[0, 0] for _ in range(MAXN)]
cntq, cntu = 0, 0

# dep是深度
# seg是括号序（欧拉序）
# st是节点开始序
# ed是节点结束序
# stjump是倍增表（用于求LCA）
# cntd是括号序列的长度
dep = [0] * MAXN
seg = [0] * (MAXN << 1)
st = [0] * MAXN
ed = [0] * MAXN
stjump = [[0] * MAXP for _ in range(MAXN)]
cntd = 0

# 分块
bi = [0] * (MAXN << 1)

# 窗口信息
# vis[i]表示节点i是否在当前窗口中
vis = [False] * MAXN
# cnt[i]表示糖果型号i在当前窗口中的出现次数
cnt = [0] * MAXN
# 当前窗口的愉悦值
happy = 0
ans = [0] * MAXN

# ufe数组用于迭代版DFS
ufe = [[0, 0, 0] for _ in range(MAXN)]
stacksize = 0
u, f, e = 0, 0, 0


# 添加边到链式前向星结构中
def addEdge(u, v):
    global cntg
    cntg += 1
    next_edge[cntg] = head[u]
    to[cntg] = v
    head[u] = cntg


# DFS迭代版
def dfs2():
    global cntd, stacksize, u, f, e
    stacksize = 0
    push(1, 0, -1)
    while stacksize > 0:
        pop()
        if e == -1:
            dep[u] = dep[f] + 1
            cntd += 1
            seg[cntd] = u
            st[u] = cntd
            stjump[u][0] = f
            for p in range(1, MAXP):
                stjump[u][p] = stjump[stjump[u][p - 1]][p - 1]
            e = head[u]
        else:
            e = next_edge[e]
        if e != 0:
            push(u, f, e)
            if to[e] != f:
                push(to[e], u, -1)
        else:
            cntd += 1
            seg[cntd] = u
            ed[u] = cntd


def push(u, f, e):
    global stacksize
    ufe[stacksize][0] = u
    ufe[stacksize][1] = f
    ufe[stacksize][2] = e
    stacksize += 1


def pop():
    global stacksize, u, f, e
    stacksize -= 1
    u = ufe[stacksize][0]
    f = ufe[stacksize][1]
    e = ufe[stacksize][2]


# 使用倍增法求两个节点的最近公共祖先(LCA)
def lca(a, b):
    # 确保a的深度不小于b
    if dep[a] < dep[b]:
        a, b = b, a
    
    # 将a向上跳到与b同一深度
    for p in range(MAXP - 1, -1, -1):
        if dep[stjump[a][p]] >= dep[b]:
            a = stjump[a][p]
    
    # 如果a就是b，说明b是a的祖先
    if a == b:
        return a
    
    # a和b一起向上跳，直到它们的父节点相同
    for p in range(MAXP - 1, -1, -1):
        if stjump[a][p] != stjump[b][p]:
            a = stjump[a][p]
            b = stjump[b][p]
    
    # 返回最近公共祖先
    return stjump[a][0]


# 带修莫队经典排序
def QueryCmp(a, b):
    if bi[a[0]] != bi[b[0]]:
        return bi[a[0]] - bi[b[0]]
    if bi[a[1]] != bi[b[1]]:
        return bi[a[1]] - bi[b[1]]
    return a[2] - b[2]


# 翻转节点node的状态（添加或删除）
# 这是树上莫队的核心操作
def invert(node):
    global happy
    candy = c[node]  # 获取节点的糖果型号
    if vis[node]:
        # 如果节点在当前窗口中，删除它
        # 从愉悦值中减去该糖果的贡献
        happy -= v[candy] * w[cnt[candy]]
        cnt[candy] -= 1
    else:
        # 如果节点不在当前窗口中，添加它
        # 向愉悦值中增加该糖果的贡献
        cnt[candy] += 1
        happy += v[candy] * w[cnt[candy]]
    # 更新节点访问状态
    vis[node] = not vis[node]


# 处理时间维度上的修改操作
# tim为生效或者撤销的修改时间点，公园更换糖果
def moveTime(tim):
    pos = update[tim][0]  # 被修改的公园编号
    oldVal = c[pos]       # 原来的糖果型号
    newVal = update[tim][1]  # 新的糖果型号
    
    if vis[pos]:  # 如果当前公园在窗口中（生效中）
        # 老糖果invert效果（从愉悦值中减去贡献）
        invert(pos)
        # 新老糖果换位
        c[pos] = newVal
        update[tim][1] = oldVal
        # 新糖果invert效果（向愉悦值中增加贡献）
        invert(pos)
    else:  # 如果当前公园不在窗口中
        # 新老糖果换位即可
        c[pos] = newVal
        update[tim][1] = oldVal


# 核心计算函数
def compute():
    global happy
    # 当前窗口在欧拉序中的左右边界，以及当前处理到的修改操作时间点
    winl, winr, wint = 1, 0, 0
    
    # 依次处理所有查询
    for i in range(1, cntq + 1):
        jobl = query[i][0]  # 查询左边界（欧拉序中的位置）
        jobr = query[i][1]  # 查询右边界（欧拉序中的位置）
        jobt = query[i][2]  # 查询时已经处理的修改操作数
        lca_val = query[i][3]   # 查询路径的LCA
        id = query[i][4]    # 查询编号
        
        # 调整窗口左边界
        while winl > jobl:
            winl -= 1
            invert(seg[winl])
        
        # 调整窗口右边界
        while winr < jobr:
            winr += 1
            invert(seg[winr])
        
        # 继续调整窗口左边界
        while winl < jobl:
            invert(seg[winl])
            winl += 1
        
        # 继续调整窗口右边界
        while winr > jobr:
            invert(seg[winr])
            winr -= 1
        
        # 处理时间维度上的修改操作
        # 将修改操作处理到jobt时刻
        while wint < jobt:
            wint += 1
            moveTime(wint)
        while wint > jobt:
            moveTime(wint)
            wint -= 1
        
        # 如果LCA不在查询路径的端点上，需要特殊处理
        if lca_val > 0:
            invert(lca_val)
        
        # 记录答案
        ans[id] = happy
        
        # 恢复LCA的状态
        if lca_val > 0:
            invert(lca_val)


# 预处理函数
def prapare():
    # 带修改莫队的分块大小通常选择为 n^(2/3)
    blen = max(1, int(pow(cntd, 2.0 / 3)))
    for i in range(1, cntd + 1):
        bi[i] = (i - 1) // blen + 1
    # 对查询进行排序
    query[1:cntq+1] = sorted(query[1:cntq+1], key=lambda x: (bi[x[0]], bi[x[1]], x[2]))


def main():
    global n, m, q, cntq, cntu
    # 读取输入
    line = sys.stdin.readline().split()
    n, m, q = int(line[0]), int(line[1]), int(line[2])
    
    # 读取每种糖果的美味指数
    vs = list(map(int, sys.stdin.readline().split()))
    for i in range(1, m + 1):
        v[i] = vs[i - 1]
    
    # 读取遇到次数的权重
    ws = list(map(int, sys.stdin.readline().split()))
    for i in range(1, n + 1):
        w[i] = ws[i - 1]
    
    # 读取树的边，构建链式前向星
    for i in range(1, n):
        line = sys.stdin.readline().split()
        u, v_node = int(line[0]), int(line[1])
        addEdge(u, v_node)
        addEdge(v_node, u)
    
    # 读取每个公园的糖果型号
    cs = list(map(int, sys.stdin.readline().split()))
    for i in range(1, n + 1):
        c[i] = cs[i - 1]
    
    # 生成欧拉序
    dfs2()
    
    # 处理操作
    for i in range(1, q + 1):
        line = sys.stdin.readline().split()
        op, x, y = int(line[0]), int(line[1]), int(line[2])
        if op == 0:
            # 修改操作：第x号公园的糖果型号改成y
            cntu += 1
            update[cntu][0] = x
            update[cntu][1] = y
        else:
            # 查询操作：游客从点x出发走过简单路径到达y
            if st[x] > st[y]:
                x, y = y, x
            xylca = lca(x, y)
            if x == xylca:
                # x是LCA，查询范围是[x的进入时间, y的进入时间]
                cntq += 1
                query[cntq][0] = st[x]
                query[cntq][1] = st[y]
                query[cntq][2] = cntu  # 当前已有的修改操作数
                query[cntq][3] = 0     # LCA是端点，不需要特殊处理
                query[cntq][4] = cntq  # 查询编号
            else:
                # x不是LCA，查询范围是[x的离开时间, y的进入时间]
                # 需要特殊处理LCA节点
                cntq += 1
                query[cntq][0] = ed[x]
                query[cntq][1] = st[y]
                query[cntq][2] = cntu     # 当前已有的修改操作数
                query[cntq][3] = xylca    # 记录LCA
                query[cntq][4] = cntq     # 查询编号
    
    prapare()
    compute()
    
    # 输出结果
    for i in range(1, cntq + 1):
        print(ans[i])


if __name__ == "__main__":
    main()
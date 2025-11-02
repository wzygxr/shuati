# Journey - CF1336F
# 测试链接 : https://codeforces.com/problemset/problem/1336/F

import sys
import threading
from collections import defaultdict

# 由于Python递归深度限制，我们使用迭代方式实现
sys.setrecursionlimit(1000000)

class SegmentTreeNode:
    def __init__(self):
        self.left = None    # 左子节点
        self.right = None   # 右子节点
        self.sum = 0        # 区间和

def update(node, l, r, pos, val):
    """
    更新线段树
    :param node: 当前节点
    :param l: 区间左端点
    :param r: 区间右端点
    :param pos: 更新位置
    :param val: 更新值
    """
    if not node:
        node = SegmentTreeNode()
    
    # 更新当前节点的值
    node.sum += val
    
    # 如果是叶子节点，直接返回
    if l == r:
        return node
    
    # 递归更新子树
    mid = (l + r) // 2
    if pos <= mid:
        if not node.left:
            node.left = SegmentTreeNode()
        update(node.left, l, mid, pos, val)
    else:
        if not node.right:
            node.right = SegmentTreeNode()
        update(node.right, mid + 1, r, pos, val)
    
    return node

def query(node, l, r, L, R):
    """
    查询线段树
    :param node: 当前节点
    :param l: 区间左端点
    :param r: 区间右端点
    :param L: 查询左端点
    :param R: 查询右端点
    :return: 查询结果
    """
    if not node or L > r or R < l:
        return 0
    
    if L <= l and r <= R:
        return node.sum
    
    mid = (l + r) // 2
    res = 0
    if L <= mid:
        res += query(node.left, l, mid, L, R)
    if R > mid:
        res += query(node.right, mid + 1, r, L, R)
    
    return res

def merge(a, b):
    """
    合并两棵线段树
    :param a: 第一棵线段树根节点
    :param b: 第二棵线段树根节点
    :return: 合并后的线段树根节点
    """
    # 如果其中一个节点为空，返回另一个节点
    if not a:
        return b
    if not b:
        return a
    
    # 合并节点信息
    a.sum += b.sum
    
    # 递归合并左右子树
    a.left = merge(a.left, b.left)
    a.right = merge(a.right, b.right)
    
    return a

def dfs1(u, fa, graph, dep, anc, siz, dfn, rev, id_counter):
    """
    DFS预处理
    :param u: 当前节点
    :param fa: 父节点
    :param graph: 图的邻接表表示
    :param dep: 深度数组
    :param anc: 祖先数组
    :param siz: 子树大小数组
    :param dfn: DFS序数组
    :param rev: DFS序反向数组
    :param id_counter: ID计数器
    """
    dep[u] = dep[fa] + 1
    anc[u][0] = fa
    siz[u] = 1
    id_counter[0] += 1
    dfn[u] = id_counter[0]
    rev[id_counter[0]] = u
    
    # 预处理祖先
    for i in range(1, 21):
        anc[u][i] = anc[anc[u][i - 1]][i - 1]
    
    # 遍历子节点
    for v in graph[u]:
        if v != fa:
            dfs1(v, u, graph, dep, anc, siz, dfn, rev, id_counter)
            siz[u] += siz[v]

def jump(x, d, anc):
    """
    跳到指定深度
    :param x: 起始节点
    :param d: 跳跃深度
    :param anc: 祖先数组
    :return: 目标节点
    """
    for i in range(20, -1, -1):
        if (d >> i) & 1:
            x = anc[x][i]
    return x

def lca(x, y, dep, anc):
    """
    求LCA
    :param x: 节点x
    :param y: 节点y
    :param dep: 深度数组
    :param anc: 祖先数组
    :return: LCA节点
    """
    if dep[x] < dep[y]:
        x, y = y, x
    
    # 将x跳到与y同一深度
    for i in range(20, -1, -1):
        if dep[anc[x][i]] >= dep[y]:
            x = anc[x][i]
    
    if x == y:
        return x
    
    # 同时向上跳
    for i in range(20, -1, -1):
        if anc[x][i] != anc[y][i]:
            x = anc[x][i]
            y = anc[y][i]
    
    return anc[x][0]

def lowbit(x):
    """
    计算lowbit
    :param x: 输入值
    :return: lowbit值
    """
    return x & (-x)

def add_bit(x, val, bit):
    """
    树状数组单点更新
    :param x: 位置
    :param val: 值
    :param bit: 树状数组
    """
    x += 1
    while x < len(bit):
        bit[x] += val
        x += lowbit(x)

def sum_bit(x, bit):
    """
    树状数组前缀和查询
    :param x: 位置
    :param bit: 树状数组
    :return: 前缀和
    """
    x += 1
    res = 0
    while x > 0:
        res += bit[x]
        x -= lowbit(x)
    return res

def dfs2(u, fa, graph, chains, dep, k, anc, siz, dfn, bit, ans):
    """
    处理不同LCA的情况
    :param u: 当前节点
    :param fa: 父节点
    :param graph: 图的邻接表表示
    :param chains: 链信息
    :param dep: 深度数组
    :param k: 距离参数
    :param anc: 祖先数组
    :param siz: 子树大小数组
    :param dfn: DFS序数组
    :param bit: 树状数组
    :param ans: 答案数组
    """
    # 先递归处理子节点
    for v in graph[u]:
        if v != fa:
            dfs2(v, u, graph, chains, dep, k, anc, siz, dfn, bit, ans)
    
    # 统计贡献
    for x, y in chains[u]:
        ans[0] += sum_bit(dfn[x], bit) + sum_bit(dfn[y], bit)
    
    # 更新树状数组
    for x, y in chains[u]:
        if dep[x] - dep[u] >= k:
            node = jump(x, dep[x] - dep[u] - k, anc)
            add_bit(dfn[node], 1, bit)
            add_bit(dfn[node] + siz[node], -1, bit)
        if dep[y] - dep[u] >= k:
            node = jump(y, dep[y] - dep[u] - k, anc)
            add_bit(dfn[node], 1, bit)
            add_bit(dfn[node] + siz[node], -1, bit)

def main():
    import sys
    input = sys.stdin.read
    data = input().split()
    
    idx = 0
    n = int(data[idx])
    idx += 1
    m = int(data[idx])
    idx += 1
    k = int(data[idx])
    idx += 1
    
    # 构建图
    graph = defaultdict(list)
    for i in range(n - 1):
        u = int(data[idx])
        idx += 1
        v = int(data[idx])
        idx += 1
        graph[u].append(v)
        graph[v].append(u)
    
    # 初始化数组
    dep = [0] * (n + 1)
    anc = [[0] * 21 for _ in range(n + 1)]
    siz = [0] * (n + 1)
    dfn = [0] * (n + 1)
    rev = [0] * (n + 1)
    id_counter = [0]
    
    # 预处理
    dfs1(1, 0, graph, dep, anc, siz, dfn, rev, id_counter)
    
    # 链信息
    chains = defaultdict(list)
    
    # 读入链信息
    for i in range(m):
        x = int(data[idx])
        idx += 1
        y = int(data[idx])
        idx += 1
        if dfn[x] > dfn[y]:
            x, y = y, x
        l = lca(x, y, dep, anc)
        chains[l].append((x, y))
    
    # 树状数组
    bit = [0] * (n + 2)
    ans = [0]
    
    # 处理不同LCA的情况
    dfs2(1, 0, graph, chains, dep, k, anc, siz, dfn, bit, ans)
    
    print(ans[0])

# 由于Python的递归限制，使用线程来增加递归深度
threading.Thread(target=main).start()
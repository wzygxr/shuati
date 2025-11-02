# 玄学 - UOJ #46
# 测试链接 : https://uoj.ac/problem/46

import sys
import threading

# 由于Python递归深度限制，我们使用迭代方式实现
sys.setrecursionlimit(1000000)

class Tran:
    def __init__(self, r=0, a=0, b=0):
        self.r = r
        self.a = a
        self.b = b

def push_up(u, L, R, ls, rs, node, ncnt, mod):
    """
    更新节点信息
    :param u: 当前节点
    :param L: 左边界数组
    :param R: 右边界数组
    :param ls: 左子节点数组
    :param rs: 右子节点数组
    :param node: 变换信息数组
    :param ncnt: 节点计数器
    :param mod: 模数
    :return: 更新后的ncnt
    """
    L[u] = ncnt + 1
    lsiz = R[ls[u]]
    rsiz = R[rs[u]]
    p = L[ls[u]]
    q = L[rs[u]]
    
    while p <= lsiz and q <= rsiz:
        a = (node[p].a * node[q].a) % mod
        b = (node[p].b * node[q].a % mod + node[q].b) % mod
        ncnt += 1
        node[ncnt] = Tran(min(node[p].r, node[q].r), a, b)
        
        if node[p].r < node[q].r:
            p += 1
        elif node[p].r > node[q].r:
            q += 1
        else:
            p += 1
            q += 1
    
    while p <= lsiz:
        ncnt += 1
        node[ncnt] = node[p]
        p += 1
    
    while q <= rsiz:
        ncnt += 1
        node[ncnt] = node[q]
        q += 1
    
    R[u] = ncnt
    return ncnt

def insert(u, tL, tR, pL, pR, a, b, tp, L, R, ls, rs, node, ncnt, mod, n):
    """
    插入操作
    :param u: 当前节点
    :param tL: 区间左端点
    :param tR: 区间右端点
    :param pL: 操作左端点
    :param pR: 操作右端点
    :param a: 变换参数a
    :param b: 变换参数b
    :param tp: 时间点
    :param L: 左边界数组
    :param R: 右边界数组
    :param ls: 左子节点数组
    :param rs: 右子节点数组
    :param node: 变换信息数组
    :param ncnt: 节点计数器
    :param mod: 模数
    :param n: 数组长度
    :return: 更新后的ncnt
    """
    if tL == tR:
        L[u] = ncnt + 1
        if pL > 1:
            ncnt += 1
            node[ncnt] = Tran(pL - 1, 1, 0)
        ncnt += 1
        node[ncnt] = Tran(pR, a, b)
        if pR < n:
            ncnt += 1
            node[ncnt] = Tran(n, 1, 0)
        R[u] = ncnt
        return ncnt
    
    mid = (tL + tR) >> 1
    if tp <= mid:
        if ls[u] == 0:
            ncnt += 1
            ls[u] = ncnt
        ncnt = insert(ls[u], tL, mid, pL, pR, a, b, tp, L, R, ls, rs, node, ncnt, mod, n)
    else:
        if rs[u] == 0:
            ncnt += 1
            rs[u] = ncnt
        ncnt = insert(rs[u], mid + 1, tR, pL, pR, a, b, tp, L, R, ls, rs, node, ncnt, mod, n)
    
    if tp == tR:
        ncnt = push_up(u, L, R, ls, rs, node, ncnt, mod)
    
    return ncnt

def bs(u, p, L, R, node, mod, result):
    """
    二分查找变换
    :param u: 当前节点
    :param p: 位置
    :param L: 左边界数组
    :param R: 右边界数组
    :param node: 变换信息数组
    :param mod: 模数
    :param result: 结果数组[A, B]
    """
    l = L[u] - 1
    r = R[u]
    while l + 1 < r:
        mid = (l + r) >> 1
        if p <= node[mid].r:
            r = mid
        else:
            l = mid
    
    a = (result[0] * node[r].a) % mod
    b = (result[1] * node[r].a % mod + node[r].b) % mod
    result[0] = a
    result[1] = b

def query(u, tL, tR, qL, qR, p, L, R, ls, rs, node, mod, result):
    """
    查询操作
    :param u: 当前节点
    :param tL: 区间左端点
    :param tR: 区间右端点
    :param qL: 查询左端点
    :param qR: 查询右端点
    :param p: 位置
    :param L: 左边界数组
    :param R: 右边界数组
    :param ls: 左子节点数组
    :param rs: 右子节点数组
    :param node: 变换信息数组
    :param mod: 模数
    :param result: 结果数组[A, B]
    """
    if qL <= tL and tR <= qR:
        bs(u, p, L, R, node, mod, result)
        return
    
    mid = (tL + tR) >> 1
    if qL <= mid:
        query(ls[u], tL, mid, qL, qR, p, L, R, ls, rs, node, mod, result)
    if mid + 1 <= qR:
        query(rs[u], mid + 1, tR, qL, qR, p, L, R, ls, rs, node, mod, result)

def main():
    import sys
    input = sys.stdin.read
    data = input().split()
    
    idx = 0
    kind = int(data[idx]) & 1
    idx += 1
    lastans = 0
    
    n = int(data[idx])
    idx += 1
    mod = int(data[idx])
    idx += 1
    
    num = [0] * (n + 1)
    for i in range(1, n + 1):
        num[i] = int(data[idx])
        idx += 1
    
    q = int(data[idx])
    idx += 1
    qcnt = 0
    
    # 初始化数组
    MAXN = 600001
    MAXT = 20 * MAXN
    L = [0] * (5 * MAXN)
    R = [0] * (5 * MAXN)
    ls = [0] * MAXT
    rs = [0] * MAXT
    node = [Tran() for _ in range(MAXT)]
    ncnt = 0
    
    for t in range(1, q + 1):
        opt = int(data[idx])
        idx += 1
        if opt == 1:
            i = int(data[idx])
            idx += 1
            j = int(data[idx])
            idx += 1
            a = int(data[idx])
            idx += 1
            b = int(data[idx])
            idx += 1
            
            if kind:
                i ^= lastans
                j ^= lastans
            
            if ls[1] == 0:
                ncnt += 1
                ls[1] = ncnt
            ncnt = insert(1, 1, q, i, j, a, b, qcnt + 1, L, R, ls, rs, node, ncnt, mod, n)
            qcnt += 1
        else:
            i = int(data[idx])
            idx += 1
            j = int(data[idx])
            idx += 1
            k = int(data[idx])
            idx += 1
            
            if kind:
                i ^= lastans
                j ^= lastans
                k ^= lastans
            
            result = [1, 0]  # A, B
            query(1, 1, q, i, j, k, L, R, ls, rs, node, mod, result)
            lastans = (result[0] * num[k] % mod + result[1]) % mod
            print(lastans)

# 由于Python的递归限制，使用线程来增加递归深度
threading.Thread(target=main).start()
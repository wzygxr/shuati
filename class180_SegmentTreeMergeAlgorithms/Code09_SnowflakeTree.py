# Snowflake Tree P5384
# 测试链接 : https://www.luogu.com.cn/problem/P5384
# 线段树合并解法
#
# 题目来源：Cnoi2019
# 题目大意：树上路径查询问题，需要维护路径信息
# 解法：线段树合并 + DFS序 + 区间更新
# 时间复杂度：O(n log n)
# 空间复杂度：O(n log n)

import sys
from collections import defaultdict

def main():
    # 读取输入
    parts = sys.stdin.readline().split()
    n = int(parts[0])
    q = int(parts[1])
    
    # 初始化变量
    G = defaultdict(list)
    val = [0] * (n + 1)
    ans = [0] * (n + 1)
    
    # 线段树合并相关
    root = [0] * (n + 1)
    lc = [0] * (n * 20)
    rc = [0] * (n * 20)
    sum_arr = [0] * (n * 20)
    addTag = [0] * (n * 20)
    cnt = 0
    
    MOD = 998244353
    
    # DFS序相关
    dfn = [0] * (n + 1)
    end_arr = [0] * (n + 1)
    dfnCnt = 0
    
    # 更新父节点信息
    def pushUp(rt):
        sum_arr[rt] = (sum_arr[lc[rt]] + sum_arr[rc[rt]]) % MOD
    
    # 下放标记
    def pushDown(rt, l, r):
        if addTag[rt] != 0:
            mid = (l + r) >> 1
            if lc[rt] == 0:
                nonlocal cnt
                cnt += 1
                lc[rt] = cnt
            if rc[rt] == 0:
                nonlocal cnt
                cnt += 1
                rc[rt] = cnt
            addTag[lc[rt]] = (addTag[lc[rt]] + addTag[rt]) % MOD
            addTag[rc[rt]] = (addTag[rc[rt]] + addTag[rt]) % MOD
            sum_arr[lc[rt]] = (sum_arr[lc[rt]] + addTag[rt] * (mid - l + 1)) % MOD
            sum_arr[rc[rt]] = (sum_arr[rc[rt]] + addTag[rt] * (r - mid)) % MOD
            addTag[rt] = 0
    
    # 区间加法
    def update(rt, l, r, x, y, val):
        if x <= l and r <= y:
            addTag[rt] = (addTag[rt] + val) % MOD
            sum_arr[rt] = (sum_arr[rt] + val * (r - l + 1)) % MOD
            return
        pushDown(rt, l, r)
        mid = (l + r) >> 1
        if x <= mid:
            if lc[rt] == 0:
                nonlocal cnt
                cnt += 1
                lc[rt] = cnt
            update(lc[rt], l, mid, x, y, val)
        if y > mid:
            if rc[rt] == 0:
                nonlocal cnt
                cnt += 1
                rc[rt] = cnt
            update(rc[rt], mid+1, r, x, y, val)
        pushUp(rt)
    
    # 查询区间和
    def query(rt, l, r, x, y):
        if x <= l and r <= y:
            return sum_arr[rt]
        pushDown(rt, l, r)
        mid = (l + r) >> 1
        res = 0
        if x <= mid and lc[rt] != 0:
            res = (res + query(lc[rt], l, mid, x, y)) % MOD
        if y > mid and rc[rt] != 0:
            res = (res + query(rc[rt], mid+1, r, x, y)) % MOD
        return res
    
    # 线段树合并
    def merge(x, y, l, r):
        if x == 0 or y == 0:
            return x + y
        if l == r:
            sum_arr[x] = (sum_arr[x] + sum_arr[y]) % MOD
            return x
        pushDown(x, l, r)
        pushDown(y, l, r)
        mid = (l + r) >> 1
        lc[x] = merge(lc[x], lc[y], l, mid)
        rc[x] = merge(rc[x], rc[y], mid+1, r)
        pushUp(x)
        return x
    
    # DFS序处理
    def dfs1(u, father):
        nonlocal dfnCnt
        dfn[u] = dfnCnt = dfnCnt + 1
        for v in G[u]:
            if v != father:
                dfs1(v, u)
        end_arr[u] = dfnCnt
    
    # DFS处理线段树合并
    def dfs2(u, father):
        nonlocal cnt
        # 先处理所有子节点
        for v in G[u]:
            if v != father:
                dfs2(v, u)
                # 合并子节点的信息到当前节点
                root[u] = merge(root[u], root[v], 1, n)
        
        # 插入当前节点的信息
        if root[u] == 0:
            cnt += 1
            root[u] = cnt
        update(root[u], 1, n, dfn[u], dfn[u], val[u])
    
    # 读取节点权值
    parts = sys.stdin.readline().split()
    for i in range(1, n + 1):
        val[i] = int(parts[i-1])
    
    # 读取树结构
    for i in range(1, n):
        parts = sys.stdin.readline().split()
        u = int(parts[0])
        v = int(parts[1])
        G[u].append(v)
        G[v].append(u)
    
    # DFS序处理
    dfs1(1, 0)
    
    # DFS处理线段树合并
    dfs2(1, 0)
    
    # 处理查询
    for i in range(q):
        parts = sys.stdin.readline().split()
        op = int(parts[0])
        if op == 1:
            u = int(parts[1])
            x = int(parts[2])
            # 在u的子树中所有节点权值加上x
            update(root[1], 1, n, dfn[u], end_arr[u], x)
        else:
            u = int(parts[1])
            # 查询u子树的权值和
            res = query(root[1], 1, n, dfn[u], end_arr[u])
            print(res)

if __name__ == "__main__":
    main()
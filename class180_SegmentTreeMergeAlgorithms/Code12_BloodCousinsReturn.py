# Blood Cousins Return CF246E
# 测试链接 : https://codeforces.com/problemset/problem/246/E
# 线段树合并解法

import sys
from collections import defaultdict

def main():
    # 读取输入
    n = int(sys.stdin.readline())
    
    # 初始化变量
    G = defaultdict(list)
    name = [''] * (n + 1)
    ans = [0] * (n + 1)
    
    # 查询信息
    queryV = [0] * (n + 1)
    queryK = [0] * (n + 1)
    queries = defaultdict(list)
    
    # 线段树合并相关
    root = [0] * (n + 1)
    lc = [0] * (n * 20)
    rc = [0] * (n * 20)
    names = defaultdict(set)
    segCnt = 0
    
    # 动态开点线段树插入
    def insert(rt, l, r, x, val):
        if l == r:
            names[rt].add(val)
            return
        mid = (l + r) >> 1
        if x <= mid:
            if lc[rt] == 0:
                nonlocal segCnt
                segCnt += 1
                lc[rt] = segCnt
            insert(lc[rt], l, mid, x, val)
        else:
            if rc[rt] == 0:
                nonlocal segCnt
                segCnt += 1
                rc[rt] = segCnt
            insert(rc[rt], mid+1, r, x, val)
    
    # 线段树合并
    def merge(x, y, l, r):
        if x == 0 or y == 0:
            return x + y
        if l == r:
            names[x].update(names[y])
            return x
        mid = (l + r) >> 1
        lc[x] = merge(lc[x], lc[y], l, mid)
        rc[x] = merge(rc[x], rc[y], mid+1, r)
        # 合并左右子树信息
        names[x].update(names[lc[x]])
        names[x].update(names[rc[x]])
        return x
    
    # 查询第k代子孙的不同名字数量
    def query(rt, l, r, k):
        if l == r:
            return len(names[rt])
        mid = (l + r) >> 1
        if k <= mid:
            return query(lc[rt], l, mid, k) if lc[rt] != 0 else 0
        else:
            return query(rc[rt], mid+1, r, k) if rc[rt] != 0 else 0
    
    # DFS处理线段树合并
    def dfs(u, father, depth):
        nonlocal segCnt
        # 先处理所有子节点
        for v in G[u]:
            if v != father:
                dfs(v, u, depth + 1)
                # 合并子节点的信息到当前节点
                root[u] = merge(root[u], root[v], 1, n)
        
        # 插入当前节点的信息
        if root[u] == 0:
            segCnt += 1
            root[u] = segCnt
        insert(root[u], 1, n, depth, name[u])
        
        # 处理当前节点的查询
        for i in range(len(queries[u])):
            id = queries[u][i]
            k = queryK[id]
            ans[id] = query(root[u], 1, n, depth + k)
    
    # 读取节点信息
    for i in range(1, n + 1):
        parts = sys.stdin.readline().split()
        name[i] = parts[0]
        p = int(parts[1])
        if p != 0:
            G[p].append(i)
            G[i].append(p)
    
    # 读取查询
    m = int(sys.stdin.readline())
    for i in range(1, m + 1):
        parts = sys.stdin.readline().split()
        queryV[i] = int(parts[0])
        queryK[i] = int(parts[1])
        queries[queryV[i]].append(i)
    
    # DFS处理线段树合并
    dfs(1, 0, 1)
    
    # 输出结果
    for i in range(1, m + 1):
        print(ans[i])

if __name__ == "__main__":
    main()
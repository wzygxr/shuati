# Tree Requests CF570D
# 测试链接 : https://codeforces.com/problemset/problem/570/D
# 线段树合并解法
#
# 题目来源：Codeforces
# 题目大意：树上字符串查询问题，判断子树中节点字符能否重排成回文串
# 解法：线段树合并 + 位运算 + DFS序
# 时间复杂度：O(n log n)
# 空间复杂度：O(n log n)

import sys
from collections import defaultdict

def main():
    # 读取输入
    parts = sys.stdin.readline().split()
    n = int(parts[0])
    m = int(parts[1])
    
    # 初始化变量
    G = defaultdict(list)
    val = [''] * (n + 1)
    ans = [False] * (m + 1)
    
    # 查询信息
    queryV = [0] * (m + 1)
    queryH = [0] * (m + 1)
    queries = defaultdict(list)
    
    # 线段树合并相关
    root = [0] * (n + 1)
    lc = [0] * (n * 20)
    rc = [0] * (n * 20)
    cnt = [0] * (n * 20)  # 记录每个字符的出现次数
    segCnt = 0
    
    # DFS序相关
    dfn = [0] * (n + 1)
    end_arr = [0] * (n + 1)
    dfnCnt = 0
    
    # 动态开点线段树插入
    def insert(rt, l, r, x, val):
        if l == r:
            cnt[rt] ^= (1 << val)  # 异或操作，出现偶数次为0，奇数次为1
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
        cnt[rt] = cnt[lc[rt]] ^ cnt[rc[rt]]  # 合并左右子树信息
    
    # 线段树合并
    def merge(x, y, l, r):
        if x == 0 or y == 0:
            return x + y
        if l == r:
            cnt[x] ^= cnt[y]
            return x
        mid = (l + r) >> 1
        lc[x] = merge(lc[x], lc[y], l, mid)
        rc[x] = merge(rc[x], rc[y], mid+1, r)
        cnt[x] = cnt[lc[x]] ^ cnt[rc[x]]
        return x
    
    # 查询子树中字符出现次数的奇偶性
    def query(rt, l, r, x, y):
        if x <= l and r <= y:
            return cnt[rt]
        mid = (l + r) >> 1
        res = 0
        if x <= mid and lc[rt] != 0:
            res ^= query(lc[rt], l, mid, x, y)
        if y > mid and rc[rt] != 0:
            res ^= query(rc[rt], mid+1, r, x, y)
        return res
    
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
        nonlocal segCnt
        # 先处理所有子节点
        for v in G[u]:
            if v != father:
                dfs2(v, u)
                # 合并子节点的信息到当前节点
                root[u] = merge(root[u], root[v], 1, n)
        
        # 插入当前节点的信息
        if root[u] == 0:
            segCnt += 1
            root[u] = segCnt
        insert(root[u], 1, n, dfn[u], ord(val[u]) - ord('a'))
        
        # 处理当前节点的查询
        for i in range(len(queries[u])):
            id = queries[u][i]
            # 查询子树中字符出现次数的奇偶性
            res = query(root[u], 1, n, 1, n)
            # 判断是否可以重排为回文串
            # 回文串的条件是最多只有一个字符出现奇数次
            ans[id] = (res & (res - 1)) == 0  # 判断是否为2的幂次或0
    
    # 读取树结构
    parts = sys.stdin.readline().split()
    for i in range(2, n + 1):
        p = int(parts[i-2])
        G[p].append(i)
        G[i].append(p)
    
    # 读取节点字符
    s = sys.stdin.readline().strip()
    for i in range(1, n + 1):
        val[i] = s[i-1]
    
    # 读取查询
    for i in range(1, m + 1):
        parts = sys.stdin.readline().split()
        queryV[i] = int(parts[0])
        queryH[i] = int(parts[1])
        queries[queryV[i]].append(i)
    
    # DFS序处理
    dfs1(1, 0)
    
    # DFS处理线段树合并
    dfs2(1, 0)
    
    # 输出结果
    for i in range(1, m + 1):
        print("Yes" if ans[i] else "No")

if __name__ == "__main__":
    main()
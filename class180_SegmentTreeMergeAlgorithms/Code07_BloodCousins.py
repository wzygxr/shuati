# Blood Cousins CF208E
# 测试链接 : https://codeforces.com/problemset/problem/208/E
# 线段树合并解法
#
# 题目来源：Codeforces
# 题目大意：给定一棵树，多次询问某个节点的第k代堂兄弟数量
# 解法：线段树合并 + 倍增 + DFS序
# 时间复杂度：O(n log n + q log n)
# 空间复杂度：O(n log n)

import sys
from collections import defaultdict

def main():
    # 读取输入
    n = int(sys.stdin.readline())
    
    # 初始化变量
    G = defaultdict(list)
    depth = [0] * (n + 1)
    fa = [0] * (n + 1)
    queries = defaultdict(list)
    ans = [0] * (n + 1)
    
    # 线段树合并相关
    root = [0] * (n + 1)
    lc = [0] * (n * 20)
    rc = [0] * (n * 20)
    sum_arr = [0] * (n * 20)
    cnt = 0
    
    # 倍增祖先
    anc = [[0] * 20 for _ in range(n + 1)]
    
    # 动态开点线段树插入
    def insert(rt, l, r, x):
        nonlocal cnt
        if l == r:
            sum_arr[rt] += 1
            return
        mid = (l + r) >> 1
        if x <= mid:
            if lc[rt] == 0:
                cnt += 1
                lc[rt] = cnt
            insert(lc[rt], l, mid, x)
        else:
            if rc[rt] == 0:
                cnt += 1
                rc[rt] = cnt
            insert(rc[rt], mid+1, r, x)
        sum_arr[rt] = sum_arr[lc[rt]] + sum_arr[rc[rt]]
    
    # 线段树合并
    def merge(x, y):
        if x == 0 or y == 0:
            return x + y
        if lc[x] == 0 and lc[y] != 0:
            lc[x] = lc[y]
        elif lc[x] != 0 and lc[y] != 0:
            lc[x] = merge(lc[x], lc[y])
        
        if rc[x] == 0 and rc[y] != 0:
            rc[x] = rc[y]
        elif rc[x] != 0 and rc[y] != 0:
            rc[x] = merge(rc[x], rc[y])
        
        sum_arr[x] = sum_arr[lc[x]] + sum_arr[rc[x]]
        return x
    
    # DFS预处理深度和祖先
    def dfs1(u, father):
        depth[u] = depth[father] + 1
        fa[u] = father
        anc[u][0] = father
        
        # 倍增计算祖先
        for i in range(1, 20):
            anc[u][i] = anc[anc[u][i-1]][i-1]
        
        for v in G[u]:
            if v != father:
                dfs1(v, u)
    
    # DFS处理线段树合并
    def dfs2(u, father):
        nonlocal cnt
        # 先处理所有子节点
        for v in G[u]:
            if v != father:
                dfs2(v, u)
                # 合并子节点的信息到当前节点
                if root[u] == 0:
                    cnt += 1
                    root[u] = cnt
                if root[v] != 0:
                    root[u] = merge(root[u], root[v])
        
        # 插入当前节点到对应深度的线段树中
        if root[depth[u]] == 0:
            cnt += 1
            root[depth[u]] = cnt
        insert(root[depth[u]], 1, n, u)
        
        # 处理当前节点的查询
        for i in range(len(queries[u])):
            id = queries[u][i]
            ans[id] = sum_arr[root[depth[u]]] - 1  # 减去自己
    
    # 倍增求k级祖先
    def getKthAncestor(u, k):
        for i in range(20):
            if ((k >> i) & 1) != 0:
                u = anc[u][i]
        return u
    
    # 读取树结构
    rootId = 1
    for i in range(1, n + 1):
        p = int(sys.stdin.readline())
        if p == 0:
            rootId = i
        else:
            G[p].append(i)
            G[i].append(p)
    
    # 预处理
    dfs1(rootId, 0)
    
    # 读取查询
    m = int(sys.stdin.readline())
    for i in range(1, m + 1):
        parts = sys.stdin.readline().split()
        v = int(parts[0])
        k = int(parts[1])
        
        # 找到k级祖先
        ancestor = getKthAncestor(v, k)
        if ancestor != 0:
            queries[ancestor].append(i)
        else:
            ans[i] = 0
    
    # 处理查询
    dfs2(rootId, 0)
    
    # 输出结果
    result = []
    for i in range(1, m + 1):
        result.append(str(ans[i]))
    print(" ".join(result))

if __name__ == "__main__":
    main()
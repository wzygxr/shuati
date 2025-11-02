# Tree Path Queries - 树上莫队算法实现 (Python版本)
# 题目来源: 模板题 - 树上路径不同元素查询
# 题目链接: https://www.luogu.com.cn/problem/P4396
# 题目大意: 给定一棵树，每个节点有一个权值，每次查询路径u-v上有多少不同的权值
# 时间复杂度: O(n*sqrt(n))，空间复杂度: O(n)

import sys
import math
from sys import setrecursionlimit
from collections import defaultdict

setrecursionlimit(1 << 25)  # 设置递归深度限制

def main():
    input = sys.stdin.read().split()
    ptr = 0
    
    n = int(input[ptr])
    ptr += 1
    m = int(input[ptr])
    ptr += 1
    
    arr = [0] * (n + 1)  # 1-based索引
    for i in range(1, n + 1):
        arr[i] = int(input[ptr])
        ptr += 1
    
    # 初始化树的邻接表
    tree = [[] for _ in range(n + 1)]
    for _ in range(n - 1):
        u = int(input[ptr])
        v = int(input[ptr + 1])
        tree[u].append(v)
        tree[v].append(u)
        ptr += 2
    
    # 初始化欧拉序相关数组
    LOG = 20
    in_time = [0] * (n + 1)
    out_time = [0] * (n + 1)
    seq = [0] * (2 * n + 1)
    fa = [0] * (n + 1)
    dep = [0] * (n + 1)
    up = [[0] * LOG for _ in range(n + 1)]
    idx = 0
    
    # DFS预处理欧拉序和LCA的倍增数组
    def dfs(u, parent):
        nonlocal idx
        in_time[u] = idx + 1
        idx += 1
        seq[idx] = u
        fa[u] = parent
        dep[u] = dep[parent] + 1
        up[u][0] = parent
        for i in range(1, LOG):
            up[u][i] = up[up[u][i-1]][i-1]
        for v in tree[u]:
            if v != parent:
                dfs(v, u)
        out_time[u] = idx + 1
        idx += 1
        seq[idx] = u
    
    dfs(1, 0)
    
    # 查询LCA
    def get_lca(u, v):
        if dep[u] < dep[v]:
            u, v = v, u
        # 提升u到v的深度
        for i in range(LOG-1, -1, -1):
            if dep[up[u][i]] >= dep[v]:
                u = up[u][i]
        if u == v:
            return u
        # 同时提升u和v
        for i in range(LOG-1, -1, -1):
            if up[u][i] != up[v][i]:
                u = up[u][i]
                v = up[v][i]
        return up[u][0]
    
    # 构建查询
    queries = []
    for i in range(m):
        u = int(input[ptr])
        v = int(input[ptr + 1])
        ptr += 2
        ancestor = get_lca(u, v)
        if ancestor == u:
            # 路径u-v在同一条链上，u是祖先
            queries.append( (in_time[u], in_time[v], 0, i) )
        elif ancestor == v:
            # 路径u-v在同一条链上，v是祖先
            queries.append( (in_time[v], in_time[u], 0, i) )
        else:
            # 路径u-v需要经过LCA
            if in_time[u] > in_time[v]:
                u, v = v, u
            queries.append( (out_time[u], in_time[v], ancestor, i) )
    
    # 分块 - 块大小为sqrt(2n)
    block_size = int(math.sqrt(2 * n)) + 1
    bi = [0] * (2 * n + 1)
    for i in range(1, 2 * n + 1):
        bi[i] = (i - 1) // block_size
    
    # 查询排序 - 使用奇偶优化
    def query_cmp(q):
        l, r, lca_node, idx = q
        if bi[l] % 2 == 0:
            return (bi[l], r)
        else:
            return (bi[l], -r)
    
    queries.sort(key=query_cmp)
    
    # 初始化莫队变量
    cnt = defaultdict(int)
    diff = 0
    ans = [0] * m
    win_l, win_r = 1, 0
    
    # 切换节点状态（添加或删除）
    def toggle(node):
        nonlocal cnt, diff
        value = arr[node]
        if cnt[value] > 0:
            cnt[value] -= 1
            if cnt[value] == 0:
                diff -= 1
        else:
            cnt[value] += 1
            diff += 1
    
    # 处理每个查询
    for l, r, ancestor, id in queries:
        # 移动指针
        while win_r < r:
            win_r += 1
            toggle(seq[win_r])
        while win_l > l:
            win_l -= 1
            toggle(seq[win_l])
        while win_r > r:
            toggle(seq[win_r])
            win_r -= 1
        while win_l < l:
            toggle(seq[win_l])
            win_l += 1
        
        # 处理LCA
        if ancestor != 0:
            toggle(ancestor)  # 临时加入LCA
            ans[id] = diff
            toggle(ancestor)  # 记得撤销
        else:
            ans[id] = diff
    
    # 输出答案
    sys.stdout.write('\n'.join(map(str, ans)) + '\n')

if __name__ == "__main__":
    main()
# COT2 - Count on a tree II (SPOJ SP10707) - 树上莫队
# 题目来源: SPOJ SP10707
# 题目链接: https://www.spoj.com/problems/COT2/
# 洛谷链接: https://www.luogu.com.cn/problem/SP10707
# 题意: 给定一棵树，每个节点有一个权值，每次询问两个节点之间的路径上有多少种不同的权值
# 算法思路: 使用树上莫队算法，通过欧拉序将树上问题转化为序列问题，利用DFS序构造欧拉序
# 时间复杂度: O((n + q) * sqrt(n))
# 空间复杂度: O(n)
# 适用场景: 树上路径不同节点值个数查询问题

import math
import sys
from collections import defaultdict

def main():
    # 读取输入
    n, q = map(int, sys.stdin.readline().split())
    values = list(map(int, sys.stdin.readline().split()))
    
    # 为了方便处理，将数组下标从1开始
    values = [0] + values
    
    # 建图
    graph = [[] for _ in range(n + 1)]
    for _ in range(n - 1):
        u, v = map(int, sys.stdin.readline().split())
        graph[u].append(v)
        graph[v].append(u)
    
    # 预处理欧拉序和LCA
    euler = []
    first = [0] * (n + 1)
    depth = [0] * (n + 1)
    fa = [0] * (n + 1)
    
    def dfs(u, father, dep):
        fa[u] = father
        depth[u] = dep
        first[u] = len(euler)
        euler.append(u)
        
        for v in graph[u]:
            if v != father:
                dfs(v, u, dep + 1)
                euler.append(u)
    
    dfs(1, -1, 0)
    
    # 倍增求LCA
    up = [[-1] * 20 for _ in range(n + 1)]
    
    def init_lca():
        for i in range(1, n + 1):
            up[i][0] = fa[i]
        
        for j in range(1, 20):
            for i in range(1, n + 1):
                if up[i][j - 1] != -1:
                    up[i][j] = up[up[i][j - 1]][j - 1]
    
    init_lca()
    
    def lca(u, v):
        if depth[u] < depth[v]:
            u, v = v, u
        
        for i in range(19, -1, -1):
            if depth[u] - (1 << i) >= depth[v]:
                u = up[u][i]
        
        if u == v:
            return u
        
        for i in range(19, -1, -1):
            if up[u][i] != -1 and up[u][i] != up[v][i]:
                u = up[u][i]
                v = up[v][i]
        
        return fa[u]
    
    # 树上莫队算法实现
    block_size = int(math.sqrt(len(euler)))
    
    # 构造莫队数组
    arr = [values[x] for x in euler]
    block = [(i // block_size) for i in range(len(euler))]
    
    queries = []
    for i in range(q):
        u, v = map(int, sys.stdin.readline().split())
        lca_node = lca(u, v)
        
        # 树上莫队的特殊处理
        if first[u] > first[v]:
            u, v = v, u
        
        queries.append((first[u], first[v], first[lca_node], i))
    
    # 为查询排序
    def mo_cmp(query):
        l, r, lca_pos, idx = query
        return (block[l], r)
    
    queries.sort(key=mo_cmp)
    
    # 初始化变量
    cnt = defaultdict(int)  # 记录每个数字出现的次数
    answer = 0  # 当前区间不同数字的个数
    results = [0] * q  # 存储结果
    visited = [False] * (n + 1)  # 记录节点是否在当前路径中
    
    # 添加元素
    def add(pos):
        nonlocal answer
        if cnt[arr[pos]] == 0:
            answer += 1
        cnt[arr[pos]] += 1
    
    # 删除元素
    def remove(pos):
        nonlocal answer
        cnt[arr[pos]] -= 1
        if cnt[arr[pos]] == 0:
            answer -= 1
    
    # 处理查询
    cur_l, cur_r = 0, -1
    
    for l, r, lca_pos, idx in queries:
        # 扩展右边界
        while cur_r < r:
            cur_r += 1
            node = euler[cur_r]
            if visited[node]:
                remove(cur_r)
            else:
                add(cur_r)
            visited[node] = not visited[node]
        
        # 收缩左边界
        while cur_l > l:
            cur_l -= 1
            node = euler[cur_l]
            if visited[node]:
                remove(cur_l)
            else:
                add(cur_l)
            visited[node] = not visited[node]
        
        # 收缩右边界
        while cur_r > r:
            node = euler[cur_r]
            if visited[node]:
                remove(cur_r)
            else:
                add(cur_r)
            visited[node] = not visited[node]
            cur_r -= 1
        
        # 扩展左边界
        while cur_l < l:
            node = euler[cur_l]
            if visited[node]:
                remove(cur_l)
            else:
                add(cur_l)
            visited[node] = not visited[node]
            cur_l += 1
        
        # 特殊处理LCA
        if lca_pos != l and lca_pos != r:
            node = euler[lca_pos]
            if visited[node]:
                remove(lca_pos)
            else:
                add(lca_pos)
            visited[node] = not visited[node]
        
        results[idx] = answer
        
        # 恢复LCA状态
        if lca_pos != l and lca_pos != r:
            node = euler[lca_pos]
            visited[node] = not visited[node]
    
    # 输出结果
    for result in results:
        print(result)

if __name__ == "__main__":
    main()
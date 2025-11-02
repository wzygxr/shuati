# 树上带修莫队 (树上莫队应用 - 带修改)
# 题目来源: 洛谷P4074 [WC2013] 糖果公园
# 题目链接: https://www.luogu.com.cn/problem/P4074
# 题意: 给定一棵树，每个节点有一个糖果类型，支持两种操作：
# 1. 修改某个节点的糖果类型
# 2. 查询树上两点间路径的愉悦指数（路径上每种糖果的美味指数与新奇指数乘积之和）
# 算法思路: 使用树上带修莫队算法，结合树上莫队和带修莫队的思想
# 时间复杂度: O(n^(5/3))
# 空间复杂度: O(n)
# 适用场景: 树上路径查询，支持单点修改

import sys
import math
from collections import defaultdict

def main():
    # 读取输入
    n, m, q = map(int, sys.stdin.readline().split())
    
    # 读取美味指数
    V = [0] + list(map(int, sys.stdin.readline().split()))
    
    # 读取新奇指数
    W = [0] + list(map(int, sys.stdin.readline().split()))
    
    # 构建树
    graph = defaultdict(list)
    for _ in range(n - 1):
        u, v = map(int, sys.stdin.readline().split())
        graph[u].append(v)
        graph[v].append(u)
    
    # 读取每个节点的糖果类型
    candy_type = [0] + list(map(int, sys.stdin.readline().split()))
    
    # 预处理DFS序和LCA
    depth = [0] * (n + 1)
    fa = [0] * (n + 1)
    up = [[0] * 20 for _ in range(n + 1)]
    euler = [0] * (2 * n + 1)
    first = [0] * (n + 1)
    euler_cnt = 0
    
    # DFS预处理
    def dfs(u, father, dep):
        nonlocal euler_cnt
        fa[u] = father
        depth[u] = dep
        euler_cnt += 1
        euler[euler_cnt] = u
        first[u] = euler_cnt
        
        for v in graph[u]:
            if v != father:
                dfs(v, u, dep + 1)
                euler_cnt += 1
                euler[euler_cnt] = u
    
    # 初始化DFS
    dfs(1, -1, 0)
    
    # 预处理倍增祖先
    def init_lca():
        # 初始化第一层
        for i in range(1, n + 1):
            up[i][0] = fa[i]
        
        # 倍增处理
        j = 1
        while (1 << j) <= n:
            for i in range(1, n + 1):
                if up[i][j - 1] != -1:
                    up[i][j] = up[up[i][j - 1]][j - 1]
            j += 1
    
    init_lca()
    
    # 求LCA
    def lca(u, v):
        if depth[u] < depth[v]:
            u, v = v, u
        
        # 让u和v在同一深度
        for i in range(19, -1, -1):
            if depth[u] - (1 << i) >= depth[v]:
                u = up[u][i]
        
        if u == v:
            return u
        
        # 同时向上跳
        for i in range(19, -1, -1):
            if up[u][i] != -1 and up[u][i] != up[v][i]:
                u = up[u][i]
                v = up[v][i]
        
        return fa[u]
    
    # 处理操作
    queries = []
    updates = []
    query_count = 0
    update_count = 0
    
    for _ in range(q):
        parts = list(map(int, sys.stdin.readline().split()))
        op_type = parts[0]
        x = parts[1]
        y = parts[2]
        
        if op_type == 0:
            # 修改操作
            update_count += 1
            updates.append((x, y, candy_type[x]))  # (位置, 新值, 原值)
        else:
            # 查询操作
            query_count += 1
            lca_node = lca(x, y)
            queries.append((x, y, lca_node, update_count, query_count))
    
    # 树上带修莫队实现
    block_size = max(1, int(n ** (2/3)))
    
    # 为欧拉序分配块
    block = [0] * (2 * n + 1)
    for i in range(1, euler_cnt + 1):
        block[i] = (i - 1) // block_size + 1
    
    # 为查询排序
    def query_cmp(query):
        u, v, lca_node, t, idx = query
        return (block[first[u]], block[first[v]], t)
    
    queries.sort(key=query_cmp)
    
    # 初始化变量
    cnt = defaultdict(int)  # 每种糖果类型的出现次数
    answer = 0
    results = [0] * (query_count + 1)
    
    # 更新愉悦指数
    def update_answer(candy, delta):
        nonlocal answer
        if delta > 0:
            answer += V[candy] * W[cnt[candy] + 1]
        else:
            answer -= V[candy] * W[cnt[candy]]
        cnt[candy] += delta
    
    # 执行或撤销修改操作
    def move_time(u, v, lca_pos, tim, visited):
        pos, val, pre_val = updates[tim - 1]  # tim从1开始，数组索引从0开始
        
        # 如果修改的节点在当前查询路径上，需要更新答案
        if (first[pos] >= first[u] and first[pos] <= first[v]) or \
           (first[pos] >= first[v] and first[pos] <= first[u]):
            # 先移除旧的糖果类型对答案的贡献
            if visited[pos]:
                update_answer(candy_type[pos], -1)
            # 再添加新的糖果类型对答案的贡献
            candy_type[pos] = val
            if visited[pos]:
                update_answer(candy_type[pos], 1)
        else:
            # 如果不在路径上，直接修改
            candy_type[pos] = val
        
        # 交换值用于下次操作
        updates[tim - 1] = (pos, pre_val, val)
    
    # 处理查询
    cur_l, cur_r, cur_t = 1, 0, 0
    visited = [False] * (n + 1)
    
    for u, v, lca_pos, t, idx in queries:
        # 扩展时间戳
        while cur_t < t:
            cur_t += 1
            move_time(u, v, lca_pos, cur_t, visited)
        
        while cur_t > t:
            move_time(u, v, lca_pos, cur_t, visited)
            cur_t -= 1
        
        # 树上莫队的标准处理
        # 确保u在v的前面
        if first[u] > first[v]:
            u, v = v, u
        
        # 扩展右边界
        while cur_r < first[v]:
            cur_r += 1
            node = euler[cur_r]
            if visited[node]:
                update_answer(candy_type[node], -1)
            else:
                update_answer(candy_type[node], 1)
            visited[node] = not visited[node]
        
        # 收缩左边界
        while cur_l > first[u]:
            cur_l -= 1
            node = euler[cur_l]
            if visited[node]:
                update_answer(candy_type[node], -1)
            else:
                update_answer(candy_type[node], 1)
            visited[node] = not visited[node]
        
        # 收缩右边界
        while cur_r > first[v]:
            node = euler[cur_r]
            if visited[node]:
                update_answer(candy_type[node], -1)
            else:
                update_answer(candy_type[node], 1)
            visited[node] = not visited[node]
            cur_r -= 1
        
        # 扩展左边界
        while cur_l < first[u]:
            node = euler[cur_l]
            if visited[node]:
                update_answer(candy_type[node], -1)
            else:
                update_answer(candy_type[node], 1)
            visited[node] = not visited[node]
            cur_l += 1
        
        # 特殊处理LCA
        if lca_pos != u and lca_pos != v:
            if visited[lca_pos]:
                update_answer(candy_type[lca_pos], -1)
            else:
                update_answer(candy_type[lca_pos], 1)
            visited[lca_pos] = not visited[lca_pos]
        
        results[idx] = answer
        
        # 恢复LCA状态
        if lca_pos != u and lca_pos != v:
            visited[lca_pos] = not visited[lca_pos]
    
    # 输出结果
    for i in range(1, query_count + 1):
        print(results[i])

if __name__ == "__main__":
    main()
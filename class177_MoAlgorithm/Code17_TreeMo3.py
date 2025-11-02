# Count on a tree II - 树上莫队算法实现 (Python版本)
# 题目来源: SPOJ COT2 - Count on a tree II
# 题目链接: https://www.luogu.com.cn/problem/SP10707
# 题目大意: 给定一棵树，每个节点有权值，多次询问两点间路径上不同权值的个数
# 时间复杂度: O(n*sqrt(m))
# 空间复杂度: O(n)
#
# 相关题目链接:
# 1. SPOJ COT2 - Count on a tree II - https://www.luogu.com.cn/problem/SP10707
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code17_TreeMo1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code17_TreeMo2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code17_TreeMo3.py
#
# 2. 洛谷 P3379 【模板】最近公共祖先（LCA） - https://www.luogu.com.cn/problem/P3379
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code17_P3379_TreeMo1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code17_P3379_TreeMo2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code17_P3379_TreeMo3.py
#
# 3. 洛谷 P4689 [Ynoi2016]这是我自己的发明 - https://www.luogu.com.cn/problem/P4689
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code17_P4689_TreeMo1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code17_P4689_TreeMo2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code17_P4689_TreeMo3.py
#
# 4. 洛谷 P4074 [WC2013]糖果公园 - https://www.luogu.com.cn/problem/P4074
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code17_P4074_TreeMo1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code17_P4074_TreeMo2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code17_P4074_TreeMo3.py
#
# 5. 洛谷 P1903 [国家集训队]数颜色/维护队列 - https://www.luogu.com.cn/problem/P1903
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance3.py
#
# 6. BZOJ 2120 数颜色 - https://www.luogu.com.cn/problem/B3202
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors3.py
#
# 7. SPOJ DQUERY - https://www.spoj.com/problems/DQUERY/
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery3.py
#
# 8. 洛谷 P1494 [国家集训队]小Z的袜子 - https://www.luogu.com.cn/problem/P1494
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks3.py
#
# 9. Codeforces 86D Powerful Array - https://codeforces.com/problemset/problem/86/D
#    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray1.java
#    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray2.cpp
#    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray3.py
#
# 10. AtCoder JOI 2014 Day1 历史研究 - https://www.luogu.com.cn/problem/AT_joisc2014_c
#     Java解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch1.java
#     C++解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch2.cpp
#     Python解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch3.py

import math
import sys
from collections import defaultdict

def main():
    # 读取输入
    n, m = map(int, sys.stdin.readline().split())
    
    # 读取节点权值
    vals = list(map(int, sys.stdin.readline().split()))
    
    # 离散化权值
    unique_vals = sorted(set(vals))
    val_map = {v: i+1 for i, v in enumerate(unique_vals)}
    val = [val_map[v] for v in vals]
    
    # 建图
    graph = [[] for _ in range(n)]
    for _ in range(n - 1):
        u, v = map(int, sys.stdin.readline().split())
        u -= 1  # 转换为0索引
        v -= 1
        graph[u].append(v)
        graph[v].append(u)
    
    # DFS生成括号序和预处理LCA
    fi = [0] * n  # 节点第一次出现的位置
    gi = [0] * n  # 节点第二次出现的位置
    id_seq = [0] * (2 * n)  # 括号序
    depth = [0] * n
    parent = [-1] * n
    indexx = 0
    
    # DFS遍历
    def dfs(u, p, d):
        nonlocal indexx
        fi[u] = indexx
        id_seq[indexx] = u
        indexx += 1
        parent[u] = p
        depth[u] = d
        
        for v in graph[u]:
            if v != p:
                dfs(v, u, d + 1)
        
        gi[u] = indexx
        id_seq[indexx] = u
        indexx += 1
    
    dfs(0, -1, 0)
    
    # 预处理LCA倍增数组
    log_n = int(math.log2(n)) + 1
    f = [[-1] * log_n for _ in range(n)]
    
    # 初始化
    for i in range(n):
        f[i][0] = parent[i]
    
    # 倍增处理
    for j in range(1, log_n):
        for i in range(n):
            if f[i][j-1] != -1:
                f[i][j] = f[f[i][j-1]][j-1]
    
    # 计算LCA
    def lca(u, v):
        if depth[u] < depth[v]:
            u, v = v, u
        
        # 将u调整到和v同一深度
        diff = depth[u] - depth[v]
        for i in range(log_n):
            if diff & (1 << i):
                u = f[u][i]
        
        if u == v:
            return u
        
        # 同时向上跳
        for i in range(log_n - 1, -1, -1):
            if f[u][i] != f[v][i]:
                u = f[u][i]
                v = f[v][i]
        
        return f[u][0]
    
    # 读取查询
    queries = []
    for i in range(m):
        u, v = map(int, sys.stdin.readline().split())
        u -= 1  # 转换为0索引
        v -= 1
        l = lca(u, v)
        
        # 确保fi[u] <= fi[v]
        if fi[u] > fi[v]:
            u, v = v, u
        
        # 根据LCA是否为端点设置查询区间
        if l == u:
            queries.append((fi[u], fi[v], 0, i))
        else:
            queries.append((gi[u], fi[v], l, i))
    
    # 树上莫队算法预处理
    block_size = int(math.sqrt(indexx))
    
    # 查询排序函数
    def query_sort_key(query):
        l, r, lca_node, idx = query
        block_id = l // block_size
        return (block_id, r)
    
    # 按照树上莫队算法的顺序排序查询
    queries.sort(key=query_sort_key)
    
    # 初始化变量
    cnt = defaultdict(int)  # 权值计数
    ans = [0] * m  # 答案数组
    vis = [0] * n  # 节点是否在当前路径中
    cur_ans = 0  # 当前答案
    
    # 添加或删除节点（根据vis状态）
    def toggle(x):
        nonlocal cur_ans
        if vis[x] == 1:
            # 删除节点
            cnt[val[x]] -= 1
            if cnt[val[x]] == 0:
                cur_ans -= 1
            vis[x] = 0
        else:
            # 添加节点
            if cnt[val[x]] == 0:
                cur_ans += 1
            cnt[val[x]] += 1
            vis[x] = 1
    
    # 莫队处理
    l, r = 1, 0
    for ql, qr, qlca, idx in queries:
        # 转换为0索引
        ql -= 1
        qr -= 1
        
        # 移动左右指针
        while r < qr:
            r += 1
            toggle(id_seq[r])
        
        while r > qr:
            toggle(id_seq[r])
            r -= 1
        
        while l < ql:
            toggle(id_seq[l])
            l += 1
        
        while l > ql:
            l -= 1
            toggle(id_seq[l])
        
        # 处理LCA
        if qlca != -1:
            toggle(qlca)
            ans[idx] = cur_ans
            toggle(qlca)
        else:
            ans[idx] = cur_ans
    
    # 输出答案
    for a in ans:
        print(a)

if __name__ == "__main__":
    main()
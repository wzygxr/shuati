# Prim算法模版（洛谷）- 动态空间实现
# 题目链接: https://www.luogu.com.cn/problem/P3366
# 
# 题目描述:
# 给定一个无向图，求最小生成树的总边权值。如果图不连通，输出orz。
#
# 解题思路:
# 1. 从一个起始顶点开始，维护一个已选顶点集合
# 2. 使用优先队列维护所有连接已选顶点和未选顶点的边
# 3. 每次选择权重最小的边，将对应的顶点加入已选集合
# 4. 重复步骤2-3，直到所有顶点都被加入或无法继续添加顶点
#
# 时间复杂度: O((V + E) * log V)，其中V是顶点数，E是边数
# 空间复杂度: O(V + E)
# 是否为最优解: 对于稠密图，Prim算法的堆优化版本是较优的选择

import heapq

def prim():
    import sys
    input = sys.stdin.read().split()
    ptr = 0
    n = int(input[ptr])
    ptr += 1
    m = int(input[ptr])
    ptr += 1
    
    # 构建邻接表
    # adj是一个列表，其中adj[u]是一个列表，包含(u, v, w)的元组
    adj = [[] for _ in range(n + 1)]  # 顶点编号从1开始
    
    for _ in range(m):
        u = int(input[ptr])
        ptr += 1
        v = int(input[ptr])
        ptr += 1
        w = int(input[ptr])
        ptr += 1
        adj[u].append((v, w))
        adj[v].append((u, w))
    
    # 初始化
    visited = [False] * (n + 1)  # 标记顶点是否已访问
    heap = []  # 优先队列，存储(权重, 目标顶点)
    
    # 从顶点1开始
    visited[1] = True
    for v, w in adj[1]:
        heapq.heappush(heap, (w, v))
    
    ans = 0
    edge_cnt = 0
    
    while heap:
        # 取出权重最小的边
        w, u = heapq.heappop(heap)
        
        # 如果顶点u已经被访问，跳过
        if visited[u]:
            continue
        
        # 标记顶点u为已访问
        visited[u] = True
        ans += w
        edge_cnt += 1
        
        # 如果已经选够n-1条边，构建完成
        if edge_cnt == n - 1:
            break
        
        # 将与顶点u相连的所有未访问顶点加入优先队列
        for v, w_new in adj[u]:
            if not visited[v]:
                heapq.heappush(heap, (w_new, v))
    
    # 检查是否所有顶点都被访问
    if edge_cnt == n - 1:
        print(ans)
    else:
        print("orz")

if __name__ == "__main__":
    prim()
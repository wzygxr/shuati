"""
Codeforces 191C - Fools and Roads（树上边差分）
题目链接：https://codeforces.com/contest/191/problem/C
题目描述：给定一棵树，有k对节点(u,v)，对于每对节点，它们之间的路径上的每条边都会被经过一次
求每条边被经过的次数
解法：树上边差分 + LCA

算法思路：
1. 对于每对节点(u,v)，它们之间的路径上的每条边都会被经过一次
2. 使用树上边差分技术：
   - 在u和v处+1
   - 在LCA(u,v)处-2
3. 通过DFS计算子树和，得到每条边的经过次数

时间复杂度：O(N log N + K log N)
空间复杂度：O(N log N)
"""

import sys
sys.setrecursionlimit(300000)

def main():
    import sys
    input = sys.stdin.readline
    
    n = int(input().strip())
    
    # 邻接表存储树，存储(节点, 边ID)
    graph = [[] for _ in range(n+1)]
    
    # 读入边
    for i in range(1, n):
        u, v = map(int, input().split())
        graph[u].append((v, i))
        graph[v].append((u, i))
    
    # LCA相关
    LIMIT = 17
    depth = [0] * (n+1)
    stjump = [[0] * LIMIT for _ in range(n+1)]
    
    # 差分数组
    diff = [0] * (n+1)
    
    # 边对应的答案
    ans = [0] * (n+1)
    
    # DFS预处理LCA
    def dfs(u, fa):
        depth[u] = depth[fa] + 1
        stjump[u][0] = fa
        for p in range(1, LIMIT):
            stjump[u][p] = stjump[stjump[u][p-1]][p-1]
        for v, edge_id in graph[u]:
            if v != fa:
                dfs(v, u)
    
    # 以1为根节点进行DFS
    depth[0] = -1
    dfs(1, 0)
    
    # 求LCA
    def lca(a, b):
        if depth[a] < depth[b]:
            a, b = b, a
        # 将a调整到与b同一深度
        for p in range(LIMIT-1, -1, -1):
            if depth[stjump[a][p]] >= depth[b]:
                a = stjump[a][p]
        if a == b:
            return a
        # 同时向上跳
        for p in range(LIMIT-1, -1, -1):
            if stjump[a][p] != stjump[b][p]:
                a = stjump[a][p]
                b = stjump[b][p]
        return stjump[a][0]
    
    k = int(input().strip())
    
    # 处理每对节点
    for _ in range(k):
        u, v = map(int, input().split())
        
        l = lca(u, v)
        
        # 树上边差分
        diff[u] += 1
        diff[v] += 1
        diff[l] -= 2
    
    # DFS计算子树和
    def dfs_calc(u, fa):
        for v, edge_id in graph[u]:
            if v != fa:
                dfs_calc(v, u)
                diff[u] += diff[v]
                ans[edge_id] = diff[v]
    
    dfs_calc(1, 0)
    
    # 输出每条边的经过次数
    result = []
    for i in range(1, n):
        result.append(str(ans[i]))
    print(' '.join(result))

if __name__ == "__main__":
    main()
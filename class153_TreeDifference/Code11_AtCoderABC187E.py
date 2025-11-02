"""
AtCoder ABC187 E - Through Path（树上差分）
题目链接：https://atcoder.jp/contests/abc187/tasks/abc187_e
题目描述：给定一棵树，有Q次操作，每次操作有两种类型：
   1. 选择一条边(a,b)，给所有从a出发不经过b能到达的节点加上x
   2. 选择一条边(a,b)，给所有从b出发不经过a能到达的节点加上x
所有操作完成后，输出每个节点的值
解法：树上差分 + DFS

算法思路：
1. 对于每条边(a,b)，将树分为两个连通分量
2. 操作1：给a所在的连通分量（不包含b）的所有节点加上x
3. 操作2：给b所在的连通分量（不包含a）的所有节点加上x
4. 使用树上差分技术，在根节点处打标记，通过DFS计算子树和

时间复杂度：O(N + Q)
空间复杂度：O(N)
"""

import sys
sys.setrecursionlimit(300000)

def main():
    import sys
    input = sys.stdin.readline
    
    n = int(input().strip())
    
    # 邻接表存储树
    graph = [[] for _ in range(n+1)]
    
    # 存储边的信息
    edges = [[0, 0] for _ in range(n)]
    
    # 读入边
    for i in range(1, n):
        a, b = map(int, input().split())
        edges[i][0] = a
        edges[i][1] = b
        graph[a].append(b)
        graph[b].append(a)
    
    # DFS相关数组
    parent = [0] * (n+1)
    depth = [0] * (n+1)
    size = [0] * (n+1)
    
    # 差分数组
    diff = [0] * (n+1)
    
    # DFS预处理树结构
    def dfs(u, fa):
        parent[u] = fa
        depth[u] = depth[fa] + 1
        size[u] = 1
        for v in graph[u]:
            if v != fa:
                dfs(v, u)
                size[u] += size[v]
    
    # 以1为根节点进行DFS
    depth[0] = -1
    dfs(1, 0)
    
    q = int(input().strip())
    
    for _ in range(q):
        t, e, x = map(int, input().split())
        
        a = edges[e][0]
        b = edges[e][1]
        
        # 确保a是b的父节点
        if depth[a] > depth[b]:
            a, b = b, a
        
        if t == 1:
            # 操作1：给a所在的连通分量（不包含b）加上x
            # 相当于给整棵树加上x，然后给b的子树减去x
            diff[1] += x
            diff[b] -= x
        else:
            # 操作2：给b所在的连通分量（不包含a）加上x
            # 相当于给b的子树加上x
            diff[b] += x
    
    # DFS计算子树和
    def dfs_calc(u, fa):
        for v in graph[u]:
            if v != fa:
                diff[v] += diff[u]
                dfs_calc(v, u)
    
    dfs_calc(1, 0)
    
    # 输出结果
    for i in range(1, n+1):
        print(diff[i])

if __name__ == "__main__":
    main()
# SPOJ PT07Z Longest path in a tree (树中的最长路径)
# 题目来源: SPOJ PT07Z https://www.spoj.com/problems/PT07Z/
# 问题描述: 求树的直径，即树中任意两点之间最长的简单路径
# 算法思路:
# 1. 树的直径可以通过两次BFS或DFS求解
# 2. 第一次从任意节点（如节点1）开始BFS，找到距离它最远的节点
# 3. 第二次从第一步找到的最远节点开始BFS，找到距离它最远的节点
# 4. 第二次BFS中找到的最远距离就是树的直径
# 与重心的关系: 树的直径与重心密切相关，直径的中点（可能是一个节点或一条边的中点）通常与重心有关
# 时间复杂度：O(n)，需要两次BFS遍历
# 空间复杂度：O(n)，用于存储树结构和BFS队列

import sys
from collections import defaultdict, deque

def main():
    # 读取节点数量n
    n = int(sys.stdin.readline())
    
    # 邻接表存储树结构，adj[i]表示与节点i相邻的所有节点列表
    adj = defaultdict(list)
    
    # 读取边信息并构建树
    # 树有n-1条边
    for _ in range(n - 1):
        u, v = map(int, sys.stdin.readline().split())
        # 由于是无根树，添加无向边
        adj[u].append(v)
        adj[v].append(u)
    
    # dist[i]表示从起始节点到节点i的距离
    dist = [-1] * (n + 1)
    
    # BFS求最远节点
    # start: BFS的起始节点
    # 返回值: 距离起始节点最远的节点
    def bfs(start):
        # 初始化距离数组，-1表示未访问
        for i in range(n + 1):
            dist[i] = -1
        
        # 创建BFS队列
        queue = deque()
        # 将起始节点加入队列
        queue.append(start)
        # 起始节点的距离为0
        dist[start] = 0
        
        # 记录最远节点和最大距离
        farthestNode = start
        maxDist = 0
        
        # BFS遍历
        while queue:
            # 取出队首节点
            u = queue.popleft()
            
            # 更新最远节点和最大距离
            if dist[u] > maxDist:
                maxDist = dist[u]
                farthestNode = u
            
            # 遍历节点u的所有邻接节点
            for v in adj[u]:
                # 如果节点v未被访问过
                if dist[v] == -1:
                    # 设置节点v的距离为节点u的距离加1
                    dist[v] = dist[u] + 1
                    # 将节点v加入队列
                    queue.append(v)
        
        # 返回距离起始节点最远的节点
        return farthestNode
    
    # 计算树的直径
    # 树的直径定义：树中任意两点之间最长的简单路径
    def treeDiameter():
        # 第一次BFS，从节点1开始找到距离它最远的节点
        farthestNode = bfs(1)
        
        # 第二次BFS，从第一次找到的最远节点开始BFS，找到真正的最远节点
        # 根据树的性质，这样找到的距离就是树的直径
        diameterNode = bfs(farthestNode)
        
        # 返回直径（最远节点的距离）
        return dist[diameterNode]
    
    # 计算树的直径
    diameter = treeDiameter()
    
    # 输出树的直径
    print(diameter)

if __name__ == "__main__":
    main()
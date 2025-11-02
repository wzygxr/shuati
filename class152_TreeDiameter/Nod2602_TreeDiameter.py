import sys
from collections import deque

"""
51Nod-2602 - 树的直径
题目描述：一棵树的直径就是这棵树上存在的最长路径。现在有一棵n个节点的树，现在想知道这棵树的直径包含的边的个数是多少？
输入：第1行一个整数n表示节点个数，接下来n-1行每行两个整数u,v表示边
输出：树的直径包含的边的个数

解题思路：使用两次BFS法求树的直径
第一次BFS从任意节点出发找到最远节点u，第二次BFS从u出发找到最远节点v，u到v的距离即为树的直径

时间复杂度：O(n)，空间复杂度：O(n)
"""

def bfs(start, graph, n):
    """
    BFS函数，从指定节点开始，找到距离最远的节点和最远距离
    
    Args:
        start: 起始节点
        graph: 邻接表表示的树结构
        n: 节点数量
    
    Returns:
        tuple: (最远节点, 最远距离)
    """
    visited = [False] * (n + 1)  # 标记节点是否被访问过
    distance = [0] * (n + 1)     # 存储每个节点到起始节点的距离
    queue = deque()
    
    queue.append(start)
    visited[start] = True
    distance[start] = 0
    
    max_distance = 0
    farthest_node = start
    
    while queue:
        current = queue.popleft()
        
        for neighbor in graph[current]:
            if not visited[neighbor]:
                visited[neighbor] = True
                distance[neighbor] = distance[current] + 1
                queue.append(neighbor)
                
                # 更新最远距离和最远节点
                if distance[neighbor] > max_distance:
                    max_distance = distance[neighbor]
                    farthest_node = neighbor
    
    return farthest_node, max_distance

def main():
    # 读取输入
    n = int(sys.stdin.readline())
    
    # 初始化邻接表
    graph = [[] for _ in range(n + 1)]
    
    # 读取边
    for _ in range(n - 1):
        u, v = map(int, sys.stdin.readline().split())
        # 无向树，添加双向边
        graph[u].append(v)
        graph[v].append(u)
    
    # 第一次BFS，找到距离任意节点(这里选择1号节点)最远的节点u
    u, _ = bfs(1, graph, n)
    
    # 第二次BFS，找到距离u最远的节点v，此时的距离即为树的直径
    _, diameter = bfs(u, graph, n)
    
    # 输出树的直径包含的边的个数
    print(diameter)

if __name__ == "__main__":
    main()
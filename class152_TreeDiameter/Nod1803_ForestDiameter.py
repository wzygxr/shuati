import sys
from collections import deque

"""
51Nod-1803 - 森林的直径
题目描述：有一个由n个节点组成的森林，每个节点属于一棵树。
支持两种操作：
1. 连接两棵树：输入格式为"1 u v"，表示将节点u所在的树与节点v所在的树合并
2. 查询操作：输入格式为"2 u"，表示询问节点u所在的树的直径

解题思路：
1. 使用并查集来管理各个树的合并
2. 对于每个树，维护其直径的两个端点
3. 当合并两棵树时，新树的直径只可能是原两棵树的直径之一，或者通过连接边形成的新路径（即u树的两个端点和v树的两个端点之间的最长路径）

时间复杂度分析：
- 并查集操作的均摊时间复杂度接近O(1)
- 合并操作需要计算4种可能的路径长度，需要额外的BFS/DFS操作，单次时间复杂度为O(n)，但实际应用中树的大小通常不大

空间复杂度：O(n)
"""

def main():
    sys.setrecursionlimit(1 << 25)
    input = sys.stdin.read().split()
    ptr = 0
    n = int(input[ptr])
    ptr += 1
    m = int(input[ptr])
    ptr += 1
    
    # 初始化并查集
    parent = list(range(n + 1))  # 节点编号从1开始
    
    # 初始化邻接表
    graph = [[] for _ in range(n + 1)]
    
    # 初始化每个树的直径（初始时每个节点自身就是一棵树）
    tree_diameter = [[i, i] for i in range(n + 1)]
    
    def find(x):
        """并查集的查找操作，带路径压缩"""
        if parent[x] != x:
            parent[x] = find(parent[x])
        return parent[x]
    
    def bfs(start, visited):
        """BFS函数，从指定节点开始，找到距离最远的节点和最远距离"""
        q = deque()
        distance = {}
        
        q.append(start)
        distance[start] = 0
        visited[start] = True
        
        max_distance = 0
        farthest_node = start
        
        while q:
            current = q.popleft()
            
            for neighbor in graph[current]:
                if not visited[neighbor]:
                    visited[neighbor] = True
                    new_distance = distance[current] + 1
                    distance[neighbor] = new_distance
                    q.append(neighbor)
                    
                    if new_distance > max_distance:
                        max_distance = new_distance
                        farthest_node = neighbor
        
        return farthest_node, max_distance
    
    def get_distance(u, v):
        """计算两个节点之间的距离"""
        visited = [False] * (n + 1)
        q = deque()
        distance = {}
        
        q.append(u)
        distance[u] = 0
        visited[u] = True
        
        while q:
            current = q.popleft()
            
            if current == v:
                return distance[current]
            
            for neighbor in graph[current]:
                if not visited[neighbor]:
                    visited[neighbor] = True
                    distance[neighbor] = distance[current] + 1
                    q.append(neighbor)
        
        return -1  # 应该不会到达这里，因为u和v在同一棵树中
    
    def compute_diameter(root):
        """计算一棵树的直径"""
        visited = [False] * (n + 1)
        # 第一次BFS找到距离root最远的节点u
        u, _ = bfs(root, visited)
        
        # 重置visited数组
        visited = [False] * (n + 1)
        # 第二次BFS找到距离u最远的节点v
        v, _ = bfs(u, visited)
        
        return u, v
    
    # 处理每个操作
    for _ in range(m):
        op = int(input[ptr])
        ptr += 1
        if op == 1:
            # 连接操作
            u = int(input[ptr])
            ptr += 1
            v = int(input[ptr])
            ptr += 1
            
            # 添加边
            graph[u].append(v)
            graph[v].append(u)
            
            # 合并两个集合
            root_u = find(u)
            root_v = find(v)
            if root_u != root_v:
                parent[root_v] = root_u
                
                # 计算新树的直径
                a1, a2 = tree_diameter[root_u]
                b1, b2 = tree_diameter[root_v]
                
                # 可能的六种路径
                d1 = get_distance(a1, a2)  # 原u树的直径
                d2 = get_distance(b1, b2)  # 原v树的直径
                d3 = get_distance(a1, b1)  # a1到b1
                d4 = get_distance(a1, b2)  # a1到b2
                d5 = get_distance(a2, b1)  # a2到b1
                d6 = get_distance(a2, b2)  # a2到b2
                
                # 找出最长的路径
                max_distance = d1
                new_diameter = [a1, a2]
                
                if d2 > max_distance:
                    max_distance = d2
                    new_diameter = [b1, b2]
                if d3 > max_distance:
                    max_distance = d3
                    new_diameter = [a1, b1]
                if d4 > max_distance:
                    max_distance = d4
                    new_diameter = [a1, b2]
                if d5 > max_distance:
                    max_distance = d5
                    new_diameter = [a2, b1]
                if d6 > max_distance:
                    max_distance = d6
                    new_diameter = [a2, b2]
                
                # 更新新树的直径
                tree_diameter[root_u] = new_diameter
        elif op == 2:
            # 查询操作
            u = int(input[ptr])
            ptr += 1
            root = find(u)
            a, b = tree_diameter[root]
            # 计算直径长度并输出
            print(get_distance(a, b))

if __name__ == "__main__":
    main()
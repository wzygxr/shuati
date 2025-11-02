# 洛谷P1967 [NOIP2013 提高组] 电话线路 - 二分答案+最小生成树
# 题目链接: https://www.luogu.com.cn/problem/P1967
# 
# 题目描述:
# 给出一个农村，共有n个村庄，编号1到n。村庄之间有m条无向道路，每条道路有不同的长度。
# 现在需要从村庄1铺设电话线路到村庄n，其中一部分道路的电线杆需要升级才能承载光纤电缆，升级费用与道路长度成正比。
# 电信公司可以免费升级k条道路的电线杆。我们的目标是在满足条件的情况下，找到一条路径，使得路径上需要自己付费升级的最长道路的长度尽可能小。
#
# 解题思路:
# 二分答案 + 最小生成树方法：
# 1. 二分查找可能的最长付费道路长度mid
# 2. 将每条道路分类：长度>mid的需要自己付费，长度<=mid的视为免费
# 3. 使用最小生成树判断：从1到n的路径中，最多使用k条付费道路（即长度>mid的边）
# 或者更简单的方法：构建最小生成树，然后在树中找1到n的路径中的第(k+1)大的边
#
# 时间复杂度: O(m log m)，主要是排序和Kruskal算法的时间
# 空间复杂度: O(n + m)
# 是否为最优解: 是，这种方法是解决此类问题的有效方法

class UnionFind:
    def __init__(self, n):
        self.parent = list(range(n + 1))  # 村庄编号从1开始
    
    def find(self, x):
        # 路径压缩优化
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]
    
    def union(self, x, y):
        fx = self.find(x)
        fy = self.find(y)
        if fx != fy:
            self.parent[fy] = fx
            return True
        return False

def telephone_lines():
    import sys
    input = sys.stdin.read().split()
    ptr = 0
    n = int(input[ptr])  # 村庄数
    ptr += 1
    m = int(input[ptr])  # 道路数
    ptr += 1
    k = int(input[ptr])  # 免费升级的道路数
    ptr += 1
    
    # 读取所有道路
    edges = []
    for _ in range(m):
        u = int(input[ptr])
        ptr += 1
        v = int(input[ptr])
        ptr += 1
        w = int(input[ptr])
        ptr += 1
        edges.append((w, u, v))
    
    # 按道路长度从小到大排序
    edges.sort()
    
    # 构建最小生成树，同时记录1到n路径上的边
    uf = UnionFind(n)
    selected_edges = []
    
    for w, u, v in edges:
        if uf.union(u, v):
            selected_edges.append(w)
            # 最小生成树有n-1条边
            if len(selected_edges) == n - 1:
                break
    
    # 在最小生成树中，1到n的路径上的边按从大到小排序后的第k+1大的边即为答案
    # 如果路径长度不超过k+1，说明可以免费升级所有需要付费的道路
    selected_edges.sort(reverse=True)
    
    if len(selected_edges) > k:
        print(selected_edges[k])
    else:
        print(0)

# 另一种实现方式：二分答案 + BFS/DFS
# 上面的方法利用了最小生成树的性质，这里提供一个更直观的实现

def telephone_lines_binary_search():
    import sys
    from collections import deque
    input = sys.stdin.read().split()
    ptr = 0
    n = int(input[ptr])
    ptr += 1
    m = int(input[ptr])
    ptr += 1
    k = int(input[ptr])
    ptr += 1
    
    # 读取所有道路并构建邻接表
    graph = [[] for _ in range(n + 1)]
    max_weight = 0
    min_weight = float('inf')
    
    for _ in range(m):
        u = int(input[ptr])
        ptr += 1
        v = int(input[ptr])
        ptr += 1
        w = int(input[ptr])
        ptr += 1
        graph[u].append((v, w))
        graph[v].append((u, w))
        max_weight = max(max_weight, w)
        min_weight = min(min_weight, w)
    
    # 判断是否存在一条路径，其中付费道路（>mid）的数量不超过k
    def is_possible(mid):
        # 使用BFS，记录到达每个节点使用的付费道路数量
        dist = [-1] * (n + 1)
        q = deque()
        q.append(1)
        dist[1] = 0
        
        while q:
            u = q.popleft()
            
            if u == n:
                return dist[u] <= k
            
            for v, w in graph[u]:
                cost = 1 if w > mid else 0
                if dist[v] == -1 or dist[u] + cost < dist[v]:
                    dist[v] = dist[u] + cost
                    q.append(v)
        
        return False
    
    # 二分查找最小的mid
    left, right = 0, max_weight
    answer = max_weight
    
    while left <= right:
        mid = (left + right) // 2
        if is_possible(mid):
            answer = mid
            right = mid - 1
        else:
            left = mid + 1
    
    # 特殊情况处理：如果1和n不连通
    if not is_possible(max_weight):
        print(-1)
    else:
        print(answer)

if __name__ == "__main__":
    telephone_lines()
from typing import List
from collections import deque

"""
LeetCode 2646. 最小化旅行的价格
题目描述：给定一棵树，每个节点有一个价格。可以选择将某些节点的价格减半。
有多个旅行路径，每个路径从u到v。要求最小化所有旅行路径的总价格。
使用树上差分统计每条边被经过的次数，然后使用树形DP决策哪些节点减半。
"""

class Solution:
    def minimumTotalPrice(self, n: int, edges: List[List[int]], price: List[int], trips: List[List[int]]) -> int:
        # 构建图
        graph = [[] for _ in range(n)]
        for u, v in edges:
            graph[u].append(v)
            graph[v].append(u)
        
        self.graph = graph
        self.price = price
        self.count = [0] * n  # 节点被经过的次数
        
        # 预处理LCA
        LOG = 20
        parent = [[-1] * LOG for _ in range(n)]
        depth = [-1] * n
        
        # BFS预处理深度和父节点
        depth[0] = 0
        queue = deque([0])
        
        while queue:
            u = queue.popleft()
            for v in graph[u]:
                if depth[v] == -1:
                    depth[v] = depth[u] + 1
                    parent[v][0] = u
                    queue.append(v)
        
        # 预处理倍增数组
        for j in range(1, LOG):
            for i in range(n):
                if parent[i][j-1] != -1:
                    parent[i][j] = parent[parent[i][j-1]][j-1]
        
        # 树上差分统计每条边被经过的次数
        for u, v in trips:
            lca = self.getLCA(u, v, parent, depth, LOG)
            
            self.count[u] += 1
            self.count[v] += 1
            self.count[lca] -= 2
        
        # DFS统计每个节点被经过的次数
        self.dfsCount(0, -1)
        
        # 树形DP
        self.dp = [[0, 0] for _ in range(n)]
        self.dfsDP(0, -1)
        
        return min(self.dp[0][0], self.dp[0][1])
    
    def dfsCount(self, u: int, parent: int):
        for v in self.graph[u]:
            if v != parent:
                self.dfsCount(v, u)
                self.count[u] += self.count[v]
    
    def dfsDP(self, u: int, parent: int):
        # 不减半的情况
        self.dp[u][0] = self.price[u] * self.count[u]
        # 减半的情况
        self.dp[u][1] = (self.price[u] // 2) * self.count[u]
        
        for v in self.graph[u]:
            if v != parent:
                self.dfsDP(v, u)
                # 当前节点不减半，子节点可以减半或不减半
                self.dp[u][0] += min(self.dp[v][0], self.dp[v][1])
                # 当前节点减半，子节点不能减半
                self.dp[u][1] += self.dp[v][0]
    
    def getLCA(self, u: int, v: int, parent: List[List[int]], depth: List[int], LOG: int) -> int:
        if depth[u] < depth[v]:
            u, v = v, u
        
        # 将u提升到和v同一深度
        for j in range(LOG - 1, -1, -1):
            if depth[u] - (1 << j) >= depth[v]:
                u = parent[u][j]
        
        if u == v:
            return u
        
        # 同时向上提升
        for j in range(LOG - 1, -1, -1):
            if parent[u][j] != parent[v][j]:
                u = parent[u][j]
                v = parent[v][j]
        
        return parent[u][0]

# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1
    n1 = 4
    edges1 = [[0,1],[1,2],[1,3]]
    price1 = [2,2,10,6]
    trips1 = [[0,3],[2,1],[2,3]]
    print(solution.minimumTotalPrice(n1, edges1, price1, trips1))  # 输出: 23
    
    # 测试用例2
    n2 = 2
    edges2 = [[0,1]]
    price2 = [2,2]
    trips2 = [[0,0]]
    print(solution.minimumTotalPrice(n2, edges2, price2, trips2))  # 输出: 1
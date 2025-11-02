# POJ 1679 The Unique MST
# 题目链接: http://poj.org/problem?id=1679
# 
# 题目描述:
# 给定一个连通的无向图，判断最小生成树是否唯一。
# 如果唯一输出最小生成树的值，如果不唯一输出"Not Unique!"。
#
# 解题思路:
# 判断最小生成树唯一性的方法：
# 1. 先用Kruskal算法求出一个最小生成树
# 2. 对于最小生成树中的每条边，尝试用其他权重相同的边替换它
# 3. 如果能找到一种替换方案使得仍然能得到最小生成树，则说明MST不唯一
# 
# 另一种更简单的方法：
# 1. 求出最小生成树的权值
# 2. 求出次小生成树的权值
# 3. 如果两者相等，则MST不唯一；否则唯一
#
# 我们使用第二种方法：
# 1. 先用Kruskal算法求出最小生成树
# 2. 记录最小生成树中任意两点间路径上的最大边权
# 3. 遍历所有不在MST中的边，尝试用它替换MST中的某条边
# 4. 计算替换后的生成树权值（原权值 + 新边权 - 被替换边权）
# 5. 找到最小的替换值，即为次小生成树的权值
#
# 时间复杂度: O(E * log E + V^2)，其中E是边数，V是顶点数
# 空间复杂度: O(V^2)
# 是否为最优解: 是，这是解决该问题的标准方法

class UnionFind:
    def __init__(self, n):
        self.parent = list(range(n))
        self.rank = [0] * n
    
    def find(self, x):
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])  # 路径压缩
        return self.parent[x]
    
    def union(self, x, y):
        root_x = self.find(x)
        root_y = self.find(y)
        
        if root_x == root_y:
            return False
        
        # 按秩合并
        if self.rank[root_x] < self.rank[root_y]:
            root_x, root_y = root_y, root_x
        self.parent[root_y] = root_x
        if self.rank[root_x] == self.rank[root_y]:
            self.rank[root_x] += 1
        return True

def findUniqueMST(n, edges):
    m = len(edges)
    
    # 按权重排序
    edges.sort(key=lambda x: x[2])
    
    # 构建最小生成树
    uf = UnionFind(n)
    in_mst = [False] * m  # 标记边是否在MST中
    adj = [[] for _ in range(n)]  # 邻接表表示MST
    
    mst_cost = 0
    edges_used = 0
    
    # Kruskal算法构建MST
    for i in range(m):
        u, v, weight = edges[i]
        
        if uf.union(u, v):
            in_mst[i] = True
            adj[u].append(i)
            adj[v].append(i)
            mst_cost += weight
            edges_used += 1
            
            if edges_used == n - 1:
                break
    
    # 如果无法构建生成树
    if edges_used != n - 1:
        return "Not Unique!"  # 实际上题目保证图连通，这里只是为了完整性
    
    # 计算MST中任意两点间路径上的最大边权
    max_weight = [[0] * n for _ in range(n)]
    visited = [[False] * n for _ in range(n)]
    
    # 对每个节点进行DFS，计算它到其他节点路径上的最大边权
    for i in range(n):
        dfs(i, i, -1, 0, adj, edges, max_weight, visited)
    
    # 计算次小生成树的权值
    second_mst = float('inf')
    for i in range(m):
        if not in_mst[i]:  # 对于不在MST中的边
            u, v, weight = edges[i]
            
            # 用这条边替换MST中u到v路径上的最大边
            new_cost = mst_cost + weight - max_weight[u][v]
            second_mst = min(second_mst, new_cost)
    
    # 如果次小生成树的权值等于最小生成树的权值，则MST不唯一
    if second_mst == mst_cost:
        return "Not Unique!"
    else:
        return str(mst_cost)

def dfs(start, current, parent, max_edge, adj, edges, max_weight, visited):
    visited[start][current] = True
    max_weight[start][current] = max_edge
    
    for edge_index in adj[current]:
        next_node = edges[edge_index][1] if edges[edge_index][0] == current else edges[edge_index][0]
        if next_node != parent and not visited[start][next_node]:
            new_max = max(max_edge, edges[edge_index][2])
            dfs(start, next_node, current, new_max, adj, edges, max_weight, visited)

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    n1 = 4
    edges1 = [
        [0, 1, 1],
        [0, 2, 2],
        [0, 3, 3],
        [1, 2, 4],
        [2, 3, 5]
    ]
    print("测试用例1结果:", findUniqueMST(n1, edges1))  # 预期输出: 6
    
    # 测试用例2
    n2 = 3
    edges2 = [
        [0, 1, 1],
        [1, 2, 2],
        [0, 2, 2]
    ]
    print("测试用例2结果:", findUniqueMST(n2, edges2))  # 预期输出: Not Unique!
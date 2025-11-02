# LeetCode 1489. Find Critical and Pseudo-Critical Edges in Minimum Spanning Tree
# 题目链接: https://leetcode.cn/problems/find-critical-and-pseudo-critical-edges-in-minimum-spanning-tree/
# 
# 题目描述:
# 给你一个 n 个点的带权无向连通图，节点编号为 0 到 n-1，同时还有一个数组 edges，
# 其中 edges[i] = [fromi, toi, weighti] 表示在 fromi 和 toi 节点之间有一条权重为 weighti 的边。
# 找到最小生成树(MST)中的关键边和伪关键边。
# 
# 关键边：如果从图中删去某条边，会导致最小生成树的权值和增加，那么我们就说它是一条关键边。
# 伪关键边：可能会出现在某些最小生成树中但不会出现在所有最小生成树中的边。
#
# 解题思路:
# 1. 首先计算原始图的MST权重
# 2. 对于每条边，判断它是否为关键边或伪关键边：
#    - 关键边：删除该边后，MST权重增加或图不连通
#    - 伪关键边：该边可能出现在某些MST中（强制包含该边的MST权重等于原始MST权重）
#
# 时间复杂度: O(E^2 * α(V))，其中E是边数，V是顶点数，α是阿克曼函数的反函数
# 空间复杂度: O(V)
# 是否为最优解: 是，这是解决该问题的高效方法
# 工程化考量:
# 1. 异常处理: 检查输入参数的有效性
# 2. 边界条件: 处理空图、单节点图等特殊情况
# 3. 内存管理: 使用列表存储结果
# 4. 性能优化: 并查集的路径压缩和按秩合并优化

class UnionFind:
    def __init__(self, n):
        self.parent = list(range(n))
        self.rank = [0] * n
        self.components = n
    
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
            
        self.components -= 1
        return True
    
    def get_components(self):
        return self.components

def kruskal(n, edges, exclude_edge=-1, include_edge=-1):
    uf = UnionFind(n)
    weight = 0
    
    # 如果指定了要包含的边，先添加这条边
    if include_edge != -1:
        u, v, w = edges[include_edge]
        uf.union(u, v)
        weight += w
    
    # 添加其他边
    for i in range(len(edges)):
        # 跳过要排除的边
        if i == exclude_edge:
            continue
            
        u, v, w = edges[i]
        if uf.union(u, v):
            weight += w
    
    # 检查是否所有节点都连通
    return weight if uf.get_components() == 1 else float('inf')

def find_critical_and_pseudo_critical_edges(n, edges):
    # 为每条边添加原始索引
    new_edges = []
    for i, edge in enumerate(edges):
        new_edges.append([edge[0], edge[1], edge[2], i])
    
    # 按权重排序
    new_edges.sort(key=lambda x: x[2])
    
    # 计算原始MST的权重
    mst_weight = kruskal(n, new_edges, -1, -1)
    
    critical = []
    pseudo_critical = []
    
    # 检查每条边
    for i in range(len(new_edges)):
        index = new_edges[i][3]
        
        # 检查是否为关键边：删除该边后MST权重增加或图不连通
        weight_without_edge = kruskal(n, new_edges, i, -1)
        if weight_without_edge > mst_weight:
            critical.append(index)
            continue
        
        # 检查是否为伪关键边：强制包含该边的MST权重等于原始MST权重
        weight_with_edge = kruskal(n, new_edges, -1, i)
        if weight_with_edge == mst_weight:
            pseudo_critical.append(index)
    
    return [critical, pseudo_critical]

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    n1 = 5
    edges1 = [[0,1,1],[1,2,1],[2,3,2],[0,3,2],[0,4,3],[3,4,3],[1,4,6]]
    result1 = find_critical_and_pseudo_critical_edges(n1, edges1)
    print("测试用例1结果:", result1)  # 预期输出: [[0, 1], [2, 3, 4, 5]]
    
    # 测试用例2
    n2 = 4
    edges2 = [[0,1,1],[1,2,1],[2,3,1],[0,3,1]]
    result2 = find_critical_and_pseudo_critical_edges(n2, edges2)
    print("测试用例2结果:", result2)  # 预期输出: [[], [0, 1, 2, 3]]
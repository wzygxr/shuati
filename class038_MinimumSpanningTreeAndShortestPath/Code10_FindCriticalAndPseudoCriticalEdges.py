# LeetCode 1489. Find Critical and Pseudo-Critical Edges in Minimum Spanning Tree
# 题目链接: https://leetcode.cn/problems/find-critical-and-pseudo-critical-edges-in-minimum-spanning-tree/
# 
# 题目描述:
# 给你一个 n 个点的带权无向连通图，节点编号为 0 到 n-1，同时还有一个数组 edges，
# 其中 edges[i] = [fromi, toi, weighti] 表示在 fromi 和 toi 节点之间有一条权重为 weighti 的无向边。
# 找到最小生成树的「关键边」和「伪关键边」。
# 如果从图中删去某条边，会导致最小生成树的权值和增加，那么我们就说它是一条「关键边」。
# 「伪关键边」是可能会出现在某些最小生成树中但不会出现在所有最小生成树中的边。
#
# 解题思路:
# 1. 首先计算原图的最小生成树权值和
# 2. 对于每条边，判断它是否为关键边或伪关键边：
#    - 关键边：删除该边后，最小生成树的权值和增加或图不连通
#    - 伪关键边：不是关键边，但存在某种最小生成树包含该边
# 3. 使用Kruskal算法实现最小生成树计算
#
# 时间复杂度: O(E^2 * α(V))，其中E是边数，V是顶点数，α是阿克曼函数的反函数
# 空间复杂度: O(V + E)
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

def findCriticalAndPseudoCriticalEdges(n, edges):
    # 为每条边添加原始索引
    m = len(edges)
    new_edges = []
    for i in range(m):
        new_edges.append([edges[i][0], edges[i][1], edges[i][2], i])
    
    # 按权重排序
    new_edges.sort(key=lambda x: x[2])
    
    # 计算原始最小生成树的权值和
    original_mst = build_mst(n, new_edges, -1, -1)
    
    critical = []
    pseudo_critical = []
    
    # 检查每条边
    for i in range(m):
        # 检查是否为关键边：删除该边后MST权值增加或图不连通
        if build_mst(n, new_edges, i, -1) > original_mst:
            critical.append(new_edges[i][3])
        # 检查是否为伪关键边：不是关键边，但存在包含该边的MST权值等于原MST权值
        elif build_mst(n, new_edges, -1, i) == original_mst:
            pseudo_critical.append(new_edges[i][3])
    
    return [critical, pseudo_critical]

def build_mst(n, edges, exclude_edge, include_edge):
    uf = UnionFind(n)
    cost = 0
    edges_used = 0
    
    # 如果指定了要包含的边，先加入该边
    if include_edge != -1:
        u, v, w, _ = edges[include_edge]
        uf.union(u, v)
        cost += w
        edges_used += 1
    
    # 遍历所有边
    for i in range(len(edges)):
        # 跳过要排除的边
        if i == exclude_edge:
            continue
        
        u, v, w, _ = edges[i]
        
        # 如果两个节点不在同一集合中，说明连接它们不会形成环
        if uf.union(u, v):
            cost += w
            edges_used += 1
            
            # 如果已经选择了n-1条边，则已形成最小生成树
            if edges_used == n - 1:
                break
    
    # 如果选择了n-1条边，返回总成本；否则返回一个大值表示图不连通
    return cost if edges_used == n - 1 else float('inf')

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    edges1 = [[0,1,1],[1,2,1],[2,3,2],[0,3,2],[0,4,3],[3,4,3],[1,4,6]]
    result1 = findCriticalAndPseudoCriticalEdges(5, edges1)
    print("测试用例1结果:", result1)  # 预期输出: [[0, 1], [2, 3, 4, 5]]
    
    # 测试用例2
    edges2 = [[0,1,1],[1,2,1],[2,3,1],[0,3,1]]
    result2 = findCriticalAndPseudoCriticalEdges(4, edges2)
    print("测试用例2结果:", result2)  # 预期输出: [[], [0, 1, 2, 3]]
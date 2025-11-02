# LeetCode 1170. Checking Existence of Edge Length Limited Paths
# 题目链接: https://leetcode.cn/problems/checking-existence-of-edge-length-limited-paths/
# 
# 题目描述:
# 给你一个n个节点的无向图，每个节点编号为0到n-1。同时给你一个二维数组edges，其中edges[i] = [u_i, v_i, w_i]，表示节点u_i和v_i之间有一条权值为w_i的无向边。
# 再给你一个查询数组queries，其中queries[j] = [p_j, q_j, limit_j]，表示查询节点p_j和q_j之间是否存在一条路径，路径上的每一条边的权值都严格小于limit_j。
# 对于每个查询，请你返回布尔值，表示是否存在满足条件的路径。
#
# 解题思路:
# 使用离线查询和并查集的方法：
# 1. 将所有边按权值从小到大排序
# 2. 将所有查询按limit从小到大排序，并记录原始索引
# 3. 对于每个查询，按limit从小到大处理，将权值小于当前limit的边加入并查集
# 4. 检查当前查询的两个节点是否连通
#
# 时间复杂度: O(E log E + Q log Q + α(V) * (E + Q))，其中E是边数，Q是查询数，V是顶点数，α是阿克曼函数的反函数
# 空间复杂度: O(V + Q)
# 是否为最优解: 是，离线查询+并查集是解决此类问题的最优方法之一

class UnionFind:
    def __init__(self, n):
        self.parent = list(range(n))
        self.rank = [0] * n
    
    def find(self, x):
        # 路径压缩优化
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]
    
    def union(self, x, y):
        fx = self.find(x)
        fy = self.find(y)
        if fx != fy:
            # 按秩合并优化
            if self.rank[fx] < self.rank[fy]:
                self.parent[fx] = fy
            else:
                self.parent[fy] = fx
                if self.rank[fx] == self.rank[fy]:
                    self.rank[fx] += 1
            return True
        return False

def distanceLimitedPathsExist(n, edges, queries):
    # 预处理查询，添加原始索引
    indexed_queries = []
    for i, (p, q, limit) in enumerate(queries):
        indexed_queries.append((p, q, limit, i))
    
    # 按limit从小到大排序查询
    indexed_queries.sort(key=lambda x: x[2])
    
    # 按权值从小到大排序边
    edges.sort(key=lambda x: x[2])
    
    # 初始化并查集
    uf = UnionFind(n)
    
    # 结果数组
    result = [False] * len(queries)
    
    # 边的指针
    edge_ptr = 0
    
    # 处理每个查询
    for p, q, limit, idx in indexed_queries:
        # 将所有权值小于limit的边加入并查集
        while edge_ptr < len(edges) and edges[edge_ptr][2] < limit:
            u, v, w = edges[edge_ptr]
            uf.union(u, v)
            edge_ptr += 1
        
        # 检查p和q是否连通
        if uf.find(p) == uf.find(q):
            result[idx] = True
    
    return result

# 测试用例
def test():
    # 测试用例1
    n1 = 3
    edges1 = [[0, 1, 2], [1, 2, 4], [2, 0, 8], [1, 0, 16]]
    queries1 = [[0, 1, 2], [0, 2, 5]]
    result1 = distanceLimitedPathsExist(n1, edges1, queries1)
    print(f"Test 1: {result1}")  # 预期输出: [False, True]
    
    # 测试用例2
    n2 = 5
    edges2 = [[0, 1, 10], [1, 2, 5], [2, 3, 9], [3, 4, 13]]
    queries2 = [[0, 4, 14], [1, 4, 13]]
    result2 = distanceLimitedPathsExist(n2, edges2, queries2)
    print(f"Test 2: {result2}")  # 预期输出: [True, False]

if __name__ == "__main__":
    test()
# UVa 10369 Arctic Network
# 题目链接: https://vjudge.net/problem/UVA-10369
# 
# 题目描述:
# 国防部(DOD)希望通过无线网络连接若干偏远地区的军事基地。该网络由两种不同类型的连接组成：
# 1. 卫星信道 - 可以连接任意两个站点，数量有限
# 2. 地面连接 - 通过无线电收发器连接，成本与距离成正比
# 
# 给定基地的坐标和可用的卫星信道数，确定使所有基地连通所需的最小无线电传输距离D。
#
# 解题思路:
# 这是一个最小生成树的变种问题。我们有S个卫星信道，可以连接任意两个站点，
# 这意味着我们可以将整个网络分成S个连通分量，每个连通分量内的站点通过地面连接。
# 因此，我们需要构建最小生成树，然后删除最大的S-1条边，剩下的最大边就是答案。
#
# 具体步骤：
# 1. 计算所有站点之间的欧几里得距离
# 2. 使用Kruskal算法构建最小生成树
# 3. 在MST中，最大的S-1条边可以被卫星信道替代
# 4. 返回第(S-1)大的边的权重作为答案
#
# 时间复杂度: O(N^2 * log(N^2)) = O(N^2 * log N)，其中N是站点数
# 空间复杂度: O(N^2)
# 是否为最优解: 是，这是解决该问题的高效方法
# 工程化考量:
# 1. 异常处理: 检查输入参数的有效性
# 2. 边界条件: 处理少于2个站点的情况
# 3. 内存管理: 使用列表存储边信息
# 4. 性能优化: 并查集的路径压缩和按秩合并优化

import math

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

def arctic_network(S, positions):
    N = len(positions)
    
    # 特殊情况：站点数小于等于卫星信道数
    if N <= S:
        return 0.0
    
    # 构建所有边（站点间的距离）
    edges = []
    for i in range(N):
        for j in range(i + 1, N):
            distance = math.sqrt(
                (positions[i][0] - positions[j][0]) ** 2 +
                (positions[i][1] - positions[j][1]) ** 2
            )
            edges.append((distance, i, j))
    
    # 按权重排序
    edges.sort()
    
    # 使用并查集构建MST
    uf = UnionFind(N)
    mst_edges = []
    
    for weight, u, v in edges:
        if uf.union(u, v):
            mst_edges.append(weight)
            # MST完成
            if len(mst_edges) == N - 1:
                break
    
    # 我们可以使用S个卫星信道来替代最大的S-1条边
    # 因此，我们需要返回第(N-S)大的边的权重
    return mst_edges[N - S - 1]

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    S1 = 2
    positions1 = [[0, 100], [0, 300], [0, 600], [150, 750]]
    result1 = arctic_network(S1, positions1)
    print(f"测试用例1结果: {result1:.2f}")  # 预期输出: 212.13
    
    # 测试用例2
    S2 = 1
    positions2 = [[0, 1], [0, 2], [0, 4], [0, 7], [0, 11]]
    result2 = arctic_network(S2, positions2)
    print(f"测试用例2结果: {result2:.2f}")  # 预期输出: 7.00
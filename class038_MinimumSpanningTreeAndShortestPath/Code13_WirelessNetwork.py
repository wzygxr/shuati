# 洛谷P1991 无线通讯网
# 题目链接: https://www.luogu.com.cn/problem/P1991
# 
# 题目描述:
# 国防部计划用无线网络连接若干个边防哨所。2 种不同的通讯技术用来搭建无线网络；
# 每个边防哨所都要配备一台无线接收机，对于任意两个哨所，如果它们的距离不超过D就能直接通讯，
# 否则必须借助卫星电话。现在有S台卫星电话，请你分配这S台卫星电话，使得任意两个哨所都能通讯。
# 返回D的最小值。
#
# 解题思路:
# 这个问题可以转化为最小生成树问题：
# 1. 如果两个哨所之间的距离不超过D，它们可以直接通讯
# 2. 我们有S台卫星电话，可以连接任意两个哨所
# 3. 要使得所有哨所都能通讯，我们需要构建一个连通图
# 4. 使用卫星电话可以减少需要直接通讯的边数
# 5. 如果我们有S台卫星电话，我们可以减少S-1条最大权值的边
# 6. 因此，我们需要找到最小生成树中第(P-S)大的边权值
#
# 具体步骤：
# 1. 计算所有哨所之间的距离
# 2. 构建完全图，边权为距离
# 3. 求最小生成树
# 4. 在MST的n-1条边中，第(n-1-(S-1)) = (n-S)大的边就是答案
#
# 时间复杂度: O(P^2 * log(P))，其中P是哨所数量
# 空间复杂度: O(P^2)
# 是否为最优解: 是，这是解决该问题的标准方法

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

def wirelessNetwork(s, points):
    p = len(points)
    
    # 如果卫星电话数量大于等于哨所数量-1，不需要直接通讯
    if s >= p - 1:
        return 0.0
    
    # 如果卫星电话数量大于等于哨所数量-1，不需要直接通讯
    if s >= p - 1:
        return 0.0
    
    # 计算两点间距离
    def distance(p1, p2):
        dx = p1[0] - p2[0]
        dy = p1[1] - p2[1]
        return math.sqrt(dx * dx + dy * dy)
    
    # 构建所有边
    edges = []
    for i in range(p):
        for j in range(i + 1, p):
            dist = distance(points[i], points[j])
            edges.append((i, j, dist))
    
    # 按权重排序
    edges.sort(key=lambda x: x[2])
    
    # 使用Kruskal算法构建最小生成树
    uf = UnionFind(p)
    mst_edges = []  # 存储MST中的边权值
    
    for u, v, weight in edges:
        if uf.union(u, v):
            mst_edges.append(weight)
            if len(mst_edges) == p - 1:
                break
    
    # 我们有s个卫星电话，可以省去s-1条最大的边
    # 所以答案是第(p-1-(s-1)) = (p-s)大的边
    mst_edges.sort()
    # 确保索引不超出范围
    index = p - 1 - (s - 1)
    if index >= len(mst_edges):
        index = len(mst_edges) - 1
    return mst_edges[index]

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    s1 = 2
    points1 = [
        [0, 100],
        [0, 300],
        [0, 600],
        [150, 750]
    ]
    print("测试用例1结果: {:.2f}".format(wirelessNetwork(s1, points1)))  # 预期输出: 212.13
    
    # 测试用例2
    s2 = 1
    points2 = [
        [0, 1],
        [0, 2],
        [0, 4],
        [0, 8]
    ]
    print("测试用例2结果: {:.2f}".format(wirelessNetwork(s2, points2)))  # 预期输出: 7.00
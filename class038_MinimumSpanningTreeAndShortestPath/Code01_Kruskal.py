# Kruskal算法模版（洛谷）
# 题目链接: https://www.luogu.com.cn/problem/P3366
# 
# 题目描述:
# 给定一个无向图，求最小生成树的总边权值。如果图不连通，输出orz。
#
# 解题思路:
# 1. 将所有边按权值从小到大排序
# 2. 使用并查集数据结构，依次选择边，若加入该边不会形成环（两个顶点不在同一集合），则加入该边
# 3. 当选择了n-1条边时，最小生成树构建完成
#
# 时间复杂度: O(m * log m)，其中m是边数，主要消耗在边的排序上
# 空间复杂度: O(n + m)，其中n是顶点数，m是边数
# 是否为最优解: 是，Kruskal算法是解决最小生成树问题的标准算法之一，适用于稀疏图
# 工程化考量:
# 1. 异常处理: 检查图是否连通
# 2. 边界条件: 处理空图、单节点图等特殊情况
# 3. 内存管理: 使用列表存储数据结构
# 4. 性能优化: 并查集的路径压缩优化

class UnionFind:
    def __init__(self, n):
        self.parent = list(range(n + 1))  # 顶点编号从1开始
        
    def find(self, x):
        # 路径压缩优化
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]
    
    def union(self, x, y):
        # 如果x和y本来就是一个集合，返回False
        # 如果x和y不是一个集合，合并之后返回True
        fx = self.find(x)
        fy = self.find(y)
        if fx != fy:
            self.parent[fx] = fy
            return True
        else:
            return False

def kruskal():
    import sys
    input = sys.stdin.read().split()
    ptr = 0
    n = int(input[ptr])
    ptr += 1
    m = int(input[ptr])
    ptr += 1
    
    edges = []
    for _ in range(m):
        u = int(input[ptr])
        ptr += 1
        v = int(input[ptr])
        ptr += 1
        w = int(input[ptr])
        ptr += 1
        edges.append((w, u, v))  # 权重在前，方便排序
    
    # 按权重排序
    edges.sort()
    
    uf = UnionFind(n)
    ans = 0
    edge_cnt = 0
    
    for w, u, v in edges:
        if uf.union(u, v):
            edge_cnt += 1
            ans += w
            # 已经选够n-1条边，构建完成
            if edge_cnt == n - 1:
                break
    
    # 检查是否连通
    if edge_cnt == n - 1:
        print(ans)
    else:
        print("orz")

if __name__ == "__main__":
    kruskal()
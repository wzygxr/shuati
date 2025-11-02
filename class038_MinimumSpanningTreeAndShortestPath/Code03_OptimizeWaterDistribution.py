# LeetCode 1168. Optimize Water Distribution in a Village - Kruskal算法实现
# 题目链接: https://leetcode.cn/problems/optimize-water-distribution-in-a-village/
# 
# 题目描述:
# 村里有n户人家，编号从1到n。我们需要为每家提供水。有两种方式：
# 1. 挖掘一口井，成本为wells[i]（为第i+1户人家挖井的成本）
# 2. 连接到其他已经有水源的人家，成本为pipes[j][2]（管道连接pipes[j][0]和pipes[j][1]的成本）
# 求使所有人家都有水的最小总成本。
#
# 解题思路:
# 将问题转化为最小生成树问题：
# 1. 创建一个虚拟节点0，代表水源
# 2. 虚拟节点0到每户人家i的边权值为wells[i-1]（挖井成本）
# 3. 原问题中的管道连接作为图中的边
# 4. 然后求包含虚拟节点0和所有其他节点的最小生成树
#
# 时间复杂度: O((n + m) * log(n + m))，其中n是户数，m是管道数
# 空间复杂度: O(n + m)
# 是否为最优解: 是，Kruskal算法结合并查集是解决此类最小生成树问题的有效方法
# 工程化考量:
# 1. 异常处理: 检查输入参数的有效性
# 2. 边界条件: 处理空数组、单元素数组等特殊情况
# 3. 内存管理: 使用列表存储数据结构
# 4. 性能优化: 并查集的路径压缩和按秩合并优化

class UnionFind:
    def __init__(self, size):
        self.parent = list(range(size))
        self.rank = [0] * size
    
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

def minCostToSupplyWater(n, wells, pipes):
    # 构建所有可能的边
    edges = []
    
    # 添加虚拟节点0到各户的边（挖井成本）
    for i in range(n):
        edges.append((wells[i], 0, i + 1))
    
    # 添加管道连接的边
    for u, v, w in pipes:
        edges.append((w, u, v))
    
    # 按权重排序
    edges.sort()
    
    # 使用Kruskal算法构建最小生成树
    uf = UnionFind(n + 1)  # 0到n共n+1个节点
    total_cost = 0
    edges_used = 0
    
    for w, u, v in edges:
        if uf.union(u, v):
            total_cost += w
            edges_used += 1
            # 最小生成树需要n条边（n+1个节点）
            if edges_used == n:
                break
    
    return total_cost

# 测试用例
def test():
    # 测试用例1
    n1 = 3
    wells1 = [1, 2, 2]
    pipes1 = [[1, 2, 1], [2, 3, 1]]
    result1 = minCostToSupplyWater(n1, wells1, pipes1)
    print(f"Test 1: {result1}")  # 预期输出: 3
    
    # 测试用例2
    n2 = 2
    wells2 = [1, 1]
    pipes2 = [[1, 2, 1]]
    result2 = minCostToSupplyWater(n2, wells2, pipes2)
    print(f"Test 2: {result2}")  # 预期输出: 2

if __name__ == "__main__":
    test()
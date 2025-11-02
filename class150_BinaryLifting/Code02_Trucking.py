import sys
from collections import defaultdict

# 货车运输问题
# 问题描述：
# 一共有n座城市，编号1 ~ n
# 一共有m条双向道路，每条道路(u, v, w)表示有一条限重为w，从u到v的双向道路
# 从一点到另一点的路途中，汽车载重不能超过每一条道路的限重
# 每条查询(a, b)表示从a到b的路线中，汽车允许的最大载重是多少
# 如果从a到b无法到达，那么认为答案是-1
# 一共有q条查询，返回答案数组
# 1 <= n <= 10^4
# 1 <= m <= 5 * 10^4
# 1 <= q <= 3 * 10^4
# 0 <= w <= 10^5
# 1 <= u, v, a, b <= n
# 测试链接 : https://www.luogu.com.cn/problem/P1967

class Trucking:
    def __init__(self, n):
        """
        初始化数据结构
        :param n: 节点数量
        """
        self.n = n
        # 计算最大跳跃级别
        self.power = self.log2(n)
        # 并查集
        self.father = list(range(n + 1))
        # 给的树有可能是森林，所以需要判断节点是否访问过了
        self.visited = [False] * (n + 1)
        # 邻接表存储树结构
        self.adj = defaultdict(list)
        # 深度数组
        self.deep = [0] * (n + 1)
        # 倍增表，stjump[u][p] 表示节点u向上跳2^p步到达的节点
        self.stjump = [[0] * (self.power + 1) for _ in range(n + 1)]
        # stmin[u][p] 表示节点u向上跳2^p步的路径中，最小的权值
        self.stmin = [[float('inf')] * (self.power + 1) for _ in range(n + 1)]
        
    def log2(self, n):
        """
        计算log2(n)的值
        :param n: 输入值
        :return: log2(n)的整数部分
        """
        ans = 0
        while (1 << ans) <= (n >> 1):
            ans += 1
        return ans
    
    def find(self, i):
        """
        并查集查找操作，带路径压缩优化
        :param i: 节点编号
        :return: 节点i的根节点
        """
        if i != self.father[i]:
            self.father[i] = self.find(self.father[i])
        return self.father[i]
    
    def add_edge(self, u, v, w):
        """
        添加一条边到邻接表中
        :param u: 起点
        :param v: 终点
        :param w: 边权重
        """
        self.adj[u].append((v, w))
        self.adj[v].append((u, w))
    
    def kruskal(self, edges):
        """
        使用Kruskal算法构建最大生成树
        算法思路：
        1. 将所有边按权重从大到小排序
        2. 使用并查集判断是否形成环
        3. 不形成环的边加入生成树
        :param edges: 边列表，每个元素为(u, v, w)
        """
        # 按权重从大到小排序
        edges.sort(key=lambda x: x[2], reverse=True)
        for u, v, w in edges:
            fa = self.find(u)
            fb = self.find(v)
            if fa != fb:
                self.father[fa] = fb
                self.add_edge(u, v, w)
    
    def dfs(self, u, w, f):
        """
        DFS遍历构建倍增表
        算法思路：
        1. 遍历树的每个节点
        2. 构建深度、跳跃表和路径最小权重表
        :param u: 当前节点
        :param w: 到父节点的边权重
        :param f: 父节点
        """
        self.visited[u] = True
        if f == 0:
            self.deep[u] = 1
            self.stjump[u][0] = u
            self.stmin[u][0] = float('inf')
        else:
            self.deep[u] = self.deep[f] + 1
            self.stjump[u][0] = f
            self.stmin[u][0] = w
        # 构建倍增表
        for p in range(1, self.power + 1):
            self.stjump[u][p] = self.stjump[self.stjump[u][p - 1]][p - 1]
            self.stmin[u][p] = min(self.stmin[u][p - 1], self.stmin[self.stjump[u][p - 1]][p - 1])
        # 递归处理子节点
        for v, weight in self.adj[u]:
            if not self.visited[v]:
                self.dfs(v, weight, u)
    
    def lca(self, a, b):
        """
        查询两点间路径上的最小权重（即最大载重）
        算法思路：
        1. 判断两点是否连通
        2. 使用倍增算法找到LCA
        3. 计算路径上的最小权重
        :param a: 起点
        :param b: 终点
        :return: 两点间路径上的最小权重，如果不连通则返回-1
        """
        # 判断是否连通
        if self.find(a) != self.find(b):
            return -1
        # 确保a是深度更深的节点
        if self.deep[a] < self.deep[b]:
            a, b = b, a
        # 记录路径上的最小权重
        ans = float('inf')
        # 调整a到与b同一深度，并更新最小权重
        for p in range(self.power, -1, -1):
            if self.deep[self.stjump[a][p]] >= self.deep[b]:
                ans = min(ans, self.stmin[a][p])
                a = self.stjump[a][p]
        # 如果a和b已经在同一节点，直接返回
        if a == b:
            return ans
        # 同时向上跳跃找到LCA，并更新最小权重
        for p in range(self.power, -1, -1):
            if self.stjump[a][p] != self.stjump[b][p]:
                ans = min(ans, min(self.stmin[a][p], self.stmin[b][p]))
                a = self.stjump[a][p]
                b = self.stjump[b][p]
        # 更新最后一步的最小权重
        ans = min(ans, min(self.stmin[a][0], self.stmin[b][0]))
        return ans

def main():
    # 读取输入
    n, m = map(int, sys.stdin.readline().split())
    
    # 初始化货车运输问题求解器
    solver = Trucking(n)
    
    # 读取所有边信息
    edges = []
    for _ in range(m):
        u, v, w = map(int, sys.stdin.readline().split())
        edges.append((u, v, w))
    
    # 使用Kruskal算法构建最大生成树
    solver.kruskal(edges)
    
    # 处理可能的森林情况，对每个连通分量进行DFS
    for i in range(1, n + 1):
        if not solver.visited[i]:
            solver.dfs(i, 0, 0)
    
    # 处理查询
    q = int(sys.stdin.readline())
    for _ in range(q):
        a, b = map(int, sys.stdin.readline().split())
        print(solver.lca(a, b))

if __name__ == "__main__":
    main()
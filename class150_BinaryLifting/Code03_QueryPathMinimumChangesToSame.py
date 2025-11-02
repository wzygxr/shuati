# 边权相等的最小修改次数问题
# 问题描述：
# 一棵树有n个节点，编号0 ~ n-1，每条边(u,v,w)表示从u到v有一条权重为w的边
# 一共有m条查询，每条查询(a,b)表示，a到b的最短路径中把所有边变成一种值需要修改几条边
# 返回每条查询的查询结果
# 1 <= n <= 10^4
# 1 <= m <= 2 * 10^4
# 0 <= u、v、a、b < n
# 1 <= w <= 26
# 测试链接 : https://leetcode.cn/problems/minimum-edge-weight-equilibrium-queries-in-a-tree/
# 
# 解题思路：
# 使用Tarjan算法批量计算所有查询的最近公共祖先(LCA)，然后通过路径分解计算每种权重的边数
# 对于每条查询(a,b)，路径a->b可以分解为a->LCA(a,b)和b->LCA(a,b)两段
# 通过预处理从根节点到每个节点路径上各种权重的边数，可以快速计算任意两点间路径上各种权重的边数
# 最小修改次数 = 路径总边数 - 出现次数最多的权重的边数

import sys
from collections import defaultdict

class MinOperationsQueries:
    def __init__(self, n):
        """
        初始化边权重均等查询求解器
        :param n: 节点数量
        """
        self.n = n
        self.MAXW = 26  # 最大边权重
        
        # 初始化数据结构
        self.adj = defaultdict(list)  # 邻接表
        self.weight_cnt = [[0] * (self.MAXW + 1) for _ in range(n)]  # 从根到每个节点各种权重的边数
        self.depth = [0] * n  # 节点深度
        self.parent = [-1] * n  # 父节点
        self.visited = [False] * n  # 访问标记
        self.father = list(range(n))  # 并查集
        
    def add_edge(self, u, v, w):
        """
        添加一条边到邻接表中
        :param u: 起点
        :param v: 终点
        :param w: 边权重
        """
        self.adj[u].append((v, w))
        self.adj[v].append((u, w))
        
    def dfs(self, u, w, f):
        """
        DFS遍历统计从根节点到每个节点路径上的权重分布
        算法思路：
        1. 从根节点开始DFS遍历
        2. 维护从根到当前节点路径上各种权重的计数
        
        时间复杂度：O(n)
        空间复杂度：O(n)
        
        :param u: 当前节点
        :param w: 从父节点到当前节点的边权重
        :param f: 父节点
        """
        self.parent[u] = f
        self.depth[u] = self.depth[f] + 1 if f != -1 else 0
        
        # 如果是根节点
        if u == 0:
            for i in range(1, self.MAXW + 1):
                self.weight_cnt[u][i] = 0
        else:
            # 复制父节点的权重计数
            for i in range(1, self.MAXW + 1):
                self.weight_cnt[u][i] = self.weight_cnt[f][i]
            # 增加当前边的权重计数
            self.weight_cnt[u][w] += 1
            
        # 递归处理子节点
        for v, weight in self.adj[u]:
            if v != f:
                self.dfs(v, weight, u)
                
    def find(self, i):
        """
        并查集查找操作，带路径压缩优化
        :param i: 节点编号
        :return: 节点i的根节点
        """
        if i != self.father[i]:
            self.father[i] = self.find(self.father[i])
        return self.father[i]
        
    def tarjan_lca(self, u, queries):
        """
        Tarjan算法批量计算LCA
        算法思路：
        1. 使用DFS遍历树
        2. 在回溯时处理查询
        3. 利用并查集维护已访问节点
        
        时间复杂度：O(n + m)
        空间复杂度：O(n + m)
        
        :param u: 当前节点
        :param queries: 查询字典，key为起点，value为终点列表
        :return: 查询结果字典
        """
        # 标记当前节点已被访问
        self.visited[u] = True
        
        # 递归处理子节点
        for v, _ in self.adj[u]:
            if not self.visited[v]:
                self.tarjan_lca(v, queries)
                # 更新并查集
                self.father[v] = u
                
        # 处理从当前节点出发的查询
        lca_results = {}
        for v in queries.get(u, []):
            # 如果目标节点已被访问，则计算它们的LCA
            if self.visited[v]:
                lca_results[(u, v)] = self.find(v)
                
        return lca_results
        
    def min_operations_queries(self, edges, queries):
        """
        计算边权重均等查询的最小修改次数
        算法思路：
        1. 使用DFS预处理从根节点到每个节点路径上各种权重的边数
        2. 使用Tarjan算法批量计算所有查询的LCA
        3. 对于每个查询(a,b)，通过LCA计算路径上各种权重的边数
        4. 找出出现次数最多的权重，其他权重的边都需要修改
        
        时间复杂度：
        - 预处理：O(n)
        - Tarjan算法：O(n + m)
        - 查询处理：O(m * W)，其中W是权重种类数
        空间复杂度：O(n * W + m)
        
        :param edges: 边数组，每个元素为[u, v, w]
        :param queries: 查询数组，每个元素为[a, b]
        :return: 每个查询的最小修改次数
        """
        # 构建邻接表
        for u, v, w in edges:
            self.add_edge(u, v, w)
            
        # 从头节点到每个节点的边权值词频统计
        self.dfs(0, 0, -1)
        
        # 构建查询邻接表
        query_dict = defaultdict(list)
        query_map = {}
        for i, (a, b) in enumerate(queries):
            query_dict[a].append(b)
            query_dict[b].append(a)
            query_map[(a, b)] = i
            query_map[(b, a)] = i
            
        # 得到每个查询的最低公共祖先
        lca_results = self.tarjan_lca(0, query_dict)
        
        # 处理每个查询
        ans = [0] * len(queries)
        for (a, b), c in lca_results.items():
            if (a, b) in query_map:
                i = query_map[(a, b)]
            elif (b, a) in query_map:
                i = query_map[(b, a)]
            else:
                continue
                
            all_cnt = 0  # 从a到b的路，所有权值的边一共多少条
            max_cnt = 0  # 从a到b的路，权值重复最多的次数
            
            # 枚举所有可能的权重
            for w in range(1, self.MAXW + 1):
                # 计算路径上权重为w的边数
                # 路径a->b的边数 = a到根的边数 + b到根的边数 - 2 * LCA到根的边数
                wcnt = self.weight_cnt[a][w] + self.weight_cnt[b][w] - 2 * self.weight_cnt[c][w]
                max_cnt = max(max_cnt, wcnt)
                all_cnt += wcnt
                
            # 最小修改次数 = 总边数 - 最多重复权重的边数
            ans[i] = all_cnt - max_cnt
            
        return ans

def main():
    """
    主函数，用于测试
    """
    # 示例测试
    n = 7
    edges = [
        [0, 1, 1],
        [1, 2, 1],
        [2, 3, 1],
        [3, 4, 2],
        [4, 5, 2],
        [5, 6, 2]
    ]
    queries = [
        [0, 3],
        [3, 6],
        [2, 6],
        [0, 6]
    ]
    
    solver = MinOperationsQueries(n)
    results = solver.min_operations_queries(edges, queries)
    
    print("示例1结果:")
    print(" ".join(map(str, results)))  # 预期输出: 0 0 1 3
    
    # 另一个测试用例
    n2 = 3
    edges2 = [
        [0, 1, 4],
        [1, 2, 4]
    ]
    queries2 = [
        [0, 2]
    ]
    
    solver2 = MinOperationsQueries(n2)
    results2 = solver2.min_operations_queries(edges2, queries2)
    
    print("示例2结果:")
    print(" ".join(map(str, results2)))  # 预期输出: 0

if __name__ == "__main__":
    main()
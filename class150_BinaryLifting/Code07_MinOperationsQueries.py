# LeetCode 2846. 边权重均等查询
# 题目描述：给定一棵无权树，每条边有一个权值（1-26之间的整数），查询两个节点之间的路径上
# 需要修改多少次边权才能使路径上的所有边权相等
# 
# 最优解算法：树上倍增 + 路径信息统计
# 时间复杂度：预处理 O(n log n * 26)，单次查询 O(log n)
# 空间复杂度：O(n log n * 26)
# 
# 解题思路：
# 1. 使用树上倍增算法预处理每个节点到其祖先路径上各种权重的边数
# 2. 对于每次查询，找到两点的LCA
# 3. 通过路径分解计算查询路径上各种权重的边数
# 4. 找出出现次数最多的权重，其他权重的边都需要修改

import math
from collections import defaultdict

class MinOperationsQueries:
    def __init__(self, n):
        """
        初始化边权重均等查询求解器
        :param n: 节点数量
        """
        self.n = n
        # 计算最大跳步级别
        self.LOG = int(math.ceil(math.log(n) / math.log(2))) + 1
        
        # 初始化数据结构
        self.parent = [[-1] * n for _ in range(self.LOG)]  # parent[j][u] 表示u的2^j级祖先
        self.depth = [0] * n  # 每个节点的深度
        self.cnt = [[[0] * 26 for _ in range(n)] for _ in range(self.LOG)]  # cnt[j][u][k] 表示u到2^j级祖先路径上权值为k+1的边数
        self.adj = defaultdict(list)  # 邻接表，存储树结构
        
    def min_operations_queries(self, n, edges, queries):
        """
        计算两个节点之间路径上最少需要修改多少次边权才能使所有边权相等
        :param n: 节点数量
        :param edges: 边数组，每个元素为 [u, v, w]
        :param queries: 查询数组，每个元素为 [u, v]
        :return: 每个查询的最小修改次数
        """
        self.n = n
        # 计算最大跳步级别
        self.LOG = int(math.ceil(math.log(n) / math.log(2))) + 1
        
        # 重新初始化数据结构
        self.parent = [[-1] * n for _ in range(self.LOG)]
        self.depth = [0] * n
        self.cnt = [[[0] * 26 for _ in range(n)] for _ in range(self.LOG)]
        self.adj = defaultdict(list)
        
        # 构建邻接表
        for u, v, w in edges:
            w_adjusted = w - 1  # 将权值调整为0-25范围，方便数组索引
            self.adj[u].append((v, w_adjusted))
            self.adj[v].append((u, w_adjusted))
            
        # 深度优先搜索预处理
        self.dfs(0, -1, 0)
        
        # 构建倍增表
        for j in range(1, self.LOG):
            for i in range(n):
                if self.parent[j-1][i] != -1:
                    self.parent[j][i] = self.parent[j-1][self.parent[j-1][i]]
                    # 合并两个跳跃段的计数信息
                    for k in range(26):
                        self.cnt[j][i][k] = self.cnt[j-1][i][k] + self.cnt[j-1][self.parent[j-1][i]][k]
                        
        # 处理查询
        result = []
        for u, v in queries:
            result.append(self.query(u, v))
            
        return result
        
    def dfs(self, u, p, d):
        """
        深度优先搜索预处理每个节点的父节点、深度和到父节点的边权计数
        :param u: 当前节点
        :param p: 父节点
        :param d: 当前深度
        """
        self.parent[0][u] = p
        self.depth[u] = d
        
        for v, w in self.adj[u]:
            if v != p:
                # 直接连接的边的权值计数
                self.cnt[0][v][w] = 1
                self.dfs(v, u, d + 1)
                
    def lca(self, u, v):
        """
        查找两个节点的最近公共祖先
        :param u: 节点u
        :param v: 节点v
        :return: 最近公共祖先
        """
        # 先将较深的节点提升到同一深度
        if self.depth[u] < self.depth[v]:
            u, v = v, u
            
        # 将u提升到v的深度
        for j in range(self.LOG - 1, -1, -1):
            if self.depth[u] - (1 << j) >= self.depth[v]:
                u = self.parent[j][u]
                
        if u == v:
            return u
            
        # 同时提升两个节点，直到找到共同祖先
        for j in range(self.LOG - 1, -1, -1):
            if self.parent[j][u] != -1 and self.parent[j][u] != self.parent[j][v]:
                u = self.parent[j][u]
                v = self.parent[j][v]
                
        return self.parent[0][u]
        
    def get_count(self, u, ancestor):
        """
        统计从节点u到其某个祖先路径上各权值的边数
        :param u: 起始节点
        :param ancestor: 祖先节点
        :return: 权值计数数组
        """
        res = [0] * 26
        
        for j in range(self.LOG - 1, -1, -1):
            if self.depth[u] - (1 << j) >= self.depth[ancestor]:
                for k in range(26):
                    res[k] += self.cnt[j][u][k]
                u = self.parent[j][u]
                
        return res
        
    def query(self, u, v):
        """
        处理单个查询，计算路径上的最小修改次数
        :param u: 起始节点
        :param v: 终止节点
        :return: 最小修改次数
        """
        ancestor = self.lca(u, v)
        
        # 获取u到LCA的权值计数
        cnt_u = self.get_count(u, ancestor)
        # 获取v到LCA的权值计数
        cnt_v = self.get_count(v, ancestor)
        
        # 合并计数
        total = [0] * 26
        for k in range(26):
            total[k] = cnt_u[k] + cnt_v[k]
            
        # 计算路径总长度
        path_length = self.depth[u] + self.depth[v] - 2 * self.depth[ancestor]
        
        # 找出出现次数最多的权值
        max_count = max(total)
        
        # 最小修改次数 = 总边数 - 最多出现次数
        return path_length - max_count

def main():
    """
    主方法，用于测试
    """
    solver = MinOperationsQueries(0)
    
    # 示例测试
    n1 = 7
    edges1 = [
        [0, 1, 1],
        [1, 2, 1],
        [2, 3, 1],
        [3, 4, 2],
        [4, 5, 2],
        [5, 6, 2]
    ]
    queries1 = [
        [0, 3],
        [3, 6],
        [2, 6],
        [0, 6]
    ]
    
    results1 = solver.min_operations_queries(n1, edges1, queries1)
    print("示例1结果:")
    print(" ".join(map(str, results1)))  # 预期输出: 0 0 1 3
    
    # 另一个测试用例
    n2 = 3
    edges2 = [
        [0, 1, 4],
        [1, 2, 4]
    ]
    queries2 = [
        [0, 2]
    ]
    
    results2 = solver.min_operations_queries(n2, edges2, queries2)
    print("示例2结果:")
    print(" ".join(map(str, results2)))  # 预期输出: 0

if __name__ == "__main__":
    main()
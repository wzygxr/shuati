# 紧急集合问题（优化版本）
# 问题描述：
# 一共有n个节点，编号1 ~ n，一定有n-1条边连接形成一颗树
# 从一个点到另一个点的路径上有几条边，就需要耗费几个金币
# 每条查询(a, b, c)表示有三个人分别站在a、b、c点上
# 他们想集合在树上的某个点，并且想花费的金币总数最少
# 一共有m条查询，打印m个答案
# 1 <= n <= 5 * 10^5
# 1 <= m <= 5 * 10^5
# 测试链接 : https://www.luogu.com.cn/problem/P4281
# 
# 解题思路：
# 使用树上倍增算法计算最近公共祖先(LCA)，通过数学推导找到最优集合点
# 对于三个点a、b、c，它们的最优集合点一定是三个点两两之间LCA中深度最大的那个
# 总花费可以通过公式计算：deep[a] + deep[b] + deep[c] - deep[high] * 2 - deep[low]
# 其中high是三个LCA中深度最小的，low是深度最大的

import sys
import math
from collections import defaultdict

class EmergencyAssembly:
    def __init__(self, n):
        """
        初始化紧急集合问题求解器
        :param n: 节点数量
        """
        self.n = n
        # 计算最大跳步级别
        self.LOG = 0
        temp = n
        while temp > 0:
            self.LOG += 1
            temp >>= 1
        self.LOG = max(self.LOG, 1)
        
        # 初始化数据结构
        self.adj = defaultdict(list)  # 邻接表
        self.depth = [0] * (n + 1)    # 节点深度
        self.parent = [[-1] * (n + 1) for _ in range(self.LOG)]  # 倍增表
        
    def add_edge(self, u, v):
        """
        添加边
        :param u: 节点u
        :param v: 节点v
        """
        self.adj[u].append(v)
        self.adj[v].append(u)
        
    def dfs(self, u, p, d):
        """
        DFS预处理，构建倍增表
        :param u: 当前节点
        :param p: 父节点
        :param d: 当前深度
        """
        self.parent[0][u] = p
        self.depth[u] = d
        
        # 构建倍增表
        for j in range(1, self.LOG):
            if self.parent[j-1][u] != -1:
                self.parent[j][u] = self.parent[j-1][self.parent[j-1][u]]
        
        # 递归处理子节点
        for v in self.adj[u]:
            if v != p:
                self.dfs(v, u, d + 1)
                
    def lca(self, a, b):
        """
        计算两个节点的最近公共祖先
        :param a: 节点a
        :param b: 节点b
        :return: 最近公共祖先
        """
        # 确保a的深度不小于b
        if self.depth[a] < self.depth[b]:
            a, b = b, a
            
        # 将a提升到与b相同的深度
        for j in range(self.LOG - 1, -1, -1):
            if self.depth[self.parent[j][a]] >= self.depth[b]:
                a = self.parent[j][a]
                
        # 如果a就是b，直接返回
        if a == b:
            return a
            
        # 同时提升a和b，直到找到公共祖先
        for j in range(self.LOG - 1, -1, -1):
            if self.parent[j][a] != self.parent[j][b]:
                a = self.parent[j][a]
                b = self.parent[j][b]
                
        return self.parent[0][a]
        
    def compute(self, a, b, c):
        """
        计算三个节点的最优集合点和最小花费
        :param a: 节点a
        :param b: 节点b
        :param c: 节点c
        :return: (最优集合点, 最小花费)
        """
        # 计算三个点两两之间的LCA
        h1 = self.lca(a, b)
        h2 = self.lca(a, c)
        h3 = self.lca(b, c)
        
        # 找到深度最小和最大的LCA
        if h1 != h2:
            high = h1 if self.depth[h1] < self.depth[h2] else h2
            low = h1 if self.depth[h1] > self.depth[h2] else h2
        else:
            high = h1
            low = h3
            
        # 计算最小花费
        cost = self.depth[a] + self.depth[b] + self.depth[c] - self.depth[high] * 2 - self.depth[low]
        
        return low, cost

def main():
    """
    主函数
    """
    # 读取输入
    n, m = map(int, sys.stdin.readline().split())
    
    # 初始化求解器
    solver = EmergencyAssembly(n)
    
    # 读取边信息
    for _ in range(n - 1):
        u, v = map(int, sys.stdin.readline().split())
        solver.add_edge(u, v)
        
    # DFS预处理
    solver.dfs(1, -1, 0)
    
    # 处理查询
    for _ in range(m):
        a, b, c = map(int, sys.stdin.readline().split())
        gather_point, cost = solver.compute(a, b, c)
        print(gather_point, cost)

if __name__ == "__main__":
    main()
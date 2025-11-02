import sys
import threading
from collections import defaultdict

# 紧急集合问题
# 问题描述：
# 一共有n个节点，编号1 ~ n，一定有n-1条边连接形成一颗树
# 从一个点到另一个点的路径上有几条边，就需要耗费几个金币
# 每条查询(a, b, c)表示有三个人分别站在a、b、c点上
# 他们想集合在树上的某个点，并且想花费的金币总数最少
# 一共有m条查询，打印m个答案
# 1 <= n <= 5 * 10^5
# 1 <= m <= 5 * 10^5
# 测试链接 : https://www.luogu.com.cn/problem/P4281

class EmergencyAssembly:
    def __init__(self, n):
        """
        初始化数据结构
        :param n: 节点数量
        """
        self.n = n
        # 计算最大跳跃级别
        self.power = self.log2(n)
        # 邻接表存储树结构
        self.adj = defaultdict(list)
        # 深度数组
        self.deep = [0] * (n + 1)
        # 倍增表，stjump[i][j] 表示节点i向上跳2^j步到达的节点
        self.stjump = [[0] * (self.power + 1) for _ in range(n + 1)]
        
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
    
    def add_edge(self, u, v):
        """
        添加一条无向边
        :param u: 起点
        :param v: 终点
        """
        self.adj[u].append(v)
        self.adj[v].append(u)
    
    def dfs(self, u, f):
        """
        深度优先搜索，构建深度信息和倍增表
        :param u: 当前节点
        :param f: 父节点
        """
        # 记录当前节点的深度
        self.deep[u] = self.deep[f] + 1
        # 记录直接父节点（跳1步）
        self.stjump[u][0] = f
        # 构建倍增表：stjump[u][p] = stjump[stjump[u][p-1]][p-1]
        # 即：向上跳2^p步 = 向上跳2^(p-1)步后再跳2^(p-1)步
        for p in range(1, self.power + 1):
            self.stjump[u][p] = self.stjump[self.stjump[u][p - 1]][p - 1]
        # 递归处理子节点
        for v in self.adj[u]:
            if v != f:
                self.dfs(v, u)
    
    def lca(self, a, b):
        """
        使用倍增算法计算两个节点的最近公共祖先(LCA)
        :param a: 节点a
        :param b: 节点b
        :return: 节点a和b的最近公共祖先
        """
        # 确保a是深度更深的节点
        if self.deep[a] < self.deep[b]:
            a, b = b, a
        # 将a提升到与b相同的深度
        for p in range(self.power, -1, -1):
            if self.deep[self.stjump[a][p]] >= self.deep[b]:
                a = self.stjump[a][p]
        # 如果a和b已经在同一节点，直接返回
        if a == b:
            return a
        # 同时向上跳跃，直到找到公共祖先
        for p in range(self.power, -1, -1):
            if self.stjump[a][p] != self.stjump[b][p]:
                a = self.stjump[a][p]
                b = self.stjump[b][p]
        # 返回最近公共祖先
        return self.stjump[a][0]
    
    def compute(self, a, b, c):
        """
        计算三个点的最优集合点
        算法思路：
        1. 计算三个点两两之间的LCA
        2. 找到深度最深的LCA作为集合点
        3. 计算总花费
        :param a: 第一个点
        :param b: 第二个点
        :param c: 第三个点
        :return: (最优集合点, 最小总花费)
        """
        # 计算三个点两两之间的LCA
        h1 = self.lca(a, b)
        h2 = self.lca(a, c)
        h3 = self.lca(b, c)
        # 找到深度最浅的LCA
        high = h1 if h1 != h2 and self.deep[h1] < self.deep[h2] else (h2 if h1 != h2 else h1)
        # 找到深度最深的LCA
        low = h1 if h1 != h2 and self.deep[h1] > self.deep[h2] else (h2 if h1 != h2 else h3)
        # 最优集合点是深度最深的LCA
        togather = low
        # 计算总花费：三个点到集合点的距离之和
        # 距离公式：deep[a] + deep[b] + deep[c] - deep[high] * 2 - deep[low]
        cost = self.deep[a] + self.deep[b] + self.deep[c] - self.deep[high] * 2 - self.deep[low]
        return togather, cost

def main():
    # 读取输入
    n, m = map(int, sys.stdin.readline().split())
    
    # 初始化紧急集合问题求解器
    solver = EmergencyAssembly(n)
    
    # 读取边信息并构建邻接表
    for _ in range(n - 1):
        u, v = map(int, sys.stdin.readline().split())
        solver.add_edge(u, v)
    
    # 从节点1开始DFS，构建深度信息和倍增表
    solver.dfs(1, 0)
    
    # 处理m次查询
    for _ in range(m):
        a, b, c = map(int, sys.stdin.readline().split())
        togather, cost = solver.compute(a, b, c)
        print(togather, cost)

# 使用线程来增加递归深度限制
threading.Thread(target=main).start()
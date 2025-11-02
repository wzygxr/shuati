# Codeforces 1499F Diameter Cuts
# 题目：给定一棵n个节点的树和一个整数k，计算有多少个连通子图的直径恰好为k。
# 树的直径是指树中任意两点之间最长的简单路径。
# 来源：Codeforces Educational Round 106 Problem F
# 链接：https://codeforces.com/contest/1499/problem/F

from collections import defaultdict

MOD = 998244353

class Codeforces1499FDiameterCuts:
    def __init__(self):
        self.n = 0  # 节点数
        self.k = 0  # 目标直径
        self.graph = defaultdict(list)  # 邻接表存储树
        self.f = []  # DP状态数组
        self.size = []  # 子树大小数组
        self.g = []  # 临时数组用于DP转移
    
    def add_edge(self, u, v):
        """添加无向边"""
        self.graph[u].append(v)
        self.graph[v].append(u)
    
    def dfs(self, u, parent):
        """
        树形DP求解
        :param u: 当前节点
        :param parent: 父节点
        :return: None
        
        时间复杂度：O(n^2)
        空间复杂度：O(n^2)
        """
        # 初始化当前节点的DP状态
        self.size[u] = 1
        self.f[u][0] = 1  # 只选择节点u本身
        
        # 遍历所有子节点
        for v in self.graph[u]:
            if v != parent:
                self.dfs(v, u)
                
                # DP转移
                # 清空临时数组
                for i in range(self.k + 1):
                    self.g[i] = 0
                
                # 合并u和v的子树信息
                for i in range(min(self.k, self.size[u]) + 1):
                    for j in range(min(self.size[v], self.k - i) + 1):
                        # 合并后的最长路径长度为max(i, j+1)
                        # j+1是因为连接u和v需要增加1条边
                        new_length = max(i, j + 1)
                        if new_length <= self.k:
                            self.g[new_length] = (self.g[new_length] + self.f[u][i] * self.f[v][j]) % MOD
                
                # 更新u的子树大小和DP状态
                self.size[u] += self.size[v]
                for i in range(min(self.k, self.size[u]) + 1):
                    self.f[u][i] = self.g[i]
        
        # 计算以u为根的子树中所有合法连通子图的总数
        sum_val = 0
        for i in range(min(self.k, self.size[u]) + 1):
            sum_val = (sum_val + self.f[u][i]) % MOD
        
        # 如果不是根节点，需要调整DP状态
        if u != 1:
            # 将所有路径长度加1（因为要连接到父节点）
            for i in range(min(self.k, self.size[u]), 0, -1):
                self.f[u][i] = self.f[u][i - 1]
            # 不连接到父节点的情况
            self.f[u][0] = sum_val
    
    def solve(self):
        """
        主方法
        时间复杂度：O(n^2)
        空间复杂度：O(n^2)
        """
        # 初始化DP数组
        self.f = [[0] * (self.n + 1) for _ in range(self.n + 1)]
        self.size = [0] * (self.n + 1)
        self.g = [0] * (self.n + 1)
        
        # 执行树形DP
        self.dfs(1, 0)
        
        # 计算并返回结果
        result = 0
        for i in range(self.k + 1):
            result = (result + self.f[1][i]) % MOD
        return result
    
    def read_input_and_solve(self):
        """
        读取输入并求解
        """
        # 读取节点数和目标直径
        line = input().split()
        self.n = int(line[0])
        self.k = int(line[1])
        
        # 读取边信息
        for _ in range(self.n - 1):
            line = input().split()
            u = int(line[0])
            v = int(line[1])
            self.add_edge(u, v)
        
        # 计算并输出结果
        print(self.solve())

# 主函数
if __name__ == "__main__":
    # 由于这是在线评测题目，实际提交时需要取消下面的注释
    # solution = Codeforces1499FDiameterCuts()
    # solution.read_input_and_solve()
    
    # 示例测试
    solution = Codeforces1499FDiameterCuts()
    solution.n = 4
    solution.k = 0
    solution.add_edge(1, 2)
    solution.add_edge(2, 3)
    solution.add_edge(3, 4)
    print("示例输出:", solution.solve())
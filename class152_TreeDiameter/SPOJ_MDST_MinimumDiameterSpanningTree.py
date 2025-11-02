# SPOJ MDST - Minimum Diameter Spanning Tree
# 题目：给定一个简单无向图G的邻接顶点列表，找到最小直径生成树T，并输出该树的直径diam(T)。
# 树的直径是指树中任意两点之间最长的简单路径。
# 来源：SPOJ Problem Set
# 链接：https://www.spoj.com/problems/MDST/

import sys
from collections import defaultdict

INF = 1000000000  # 使用一个大整数代替无穷大

class SPOJMDSTMinimumDiameterSpanningTree:
    def __init__(self):
        self.n = 0  # 节点数
        self.graph = []  # 邻接矩阵表示图
        self.dist = []   # 所有点对之间的最短距离
        self.parent = [] # 用于重构路径
    
    def floyd_warshall(self):
        """
        Floyd-Warshall算法计算所有点对之间的最短距离
        
        时间复杂度：O(n^3)
        空间复杂度：O(n^2)
        """
        # 初始化距离矩阵
        for i in range(1, self.n + 1):
            for j in range(1, self.n + 1):
                if i == j:
                    self.dist[i][j] = 0
                elif self.graph[i][j] != 0:
                    self.dist[i][j] = self.graph[i][j]
                else:
                    self.dist[i][j] = INF
                self.parent[i][j] = i
        
        # Floyd-Warshall算法
        for k in range(1, self.n + 1):
            for i in range(1, self.n + 1):
                for j in range(1, self.n + 1):
                    if self.dist[i][k] + self.dist[k][j] < self.dist[i][j]:
                        self.dist[i][j] = self.dist[i][k] + self.dist[k][j]
                        self.parent[i][j] = self.parent[k][j]
    
    def find_minimum_diameter_spanning_tree(self):
        """
        通过绝对中心找到最小直径生成树
        绝对中心是边上的一个点，使得以该点为中心的生成树直径最小
        
        时间复杂度：O(n^3)
        空间复杂度：O(n^2)
        
        :return: 最小直径生成树的直径
        """
        min_diameter = INF
        
        # 检查每个节点作为中心的情况
        for center in range(1, self.n + 1):
            # 计算以center为根的生成树的直径
            diameter = 0
            for i in range(1, self.n + 1):
                for j in range(1, self.n + 1):
                    if i != j:
                        diameter = max(diameter, self.dist[i][j])
            
            min_diameter = min(min_diameter, diameter)
        
        # 检查每条边上的点作为中心的情况
        for u in range(1, self.n + 1):
            for v in range(u + 1, self.n + 1):
                if self.graph[u][v] != 0:
                    # 边(u,v)上的点作为中心
                    # 计算以这条边为中心的生成树的直径
                    diameter = 0
                    for i in range(1, self.n + 1):
                        for j in range(1, self.n + 1):
                            if i != j:
                                # 计算通过边(u,v)的最短路径
                                dist_via_edge = min(
                                    self.dist[i][u] + self.graph[u][v] + self.dist[v][j],
                                    self.dist[i][v] + self.graph[u][v] + self.dist[u][j]
                                )
                                diameter = max(diameter, dist_via_edge)
                    
                    min_diameter = min(min_diameter, diameter)
        
        return min_diameter
    
    def find_minimum_diameter_spanning_tree_optimized(self):
        """
        更高效的算法：使用绝对中心算法
        
        时间复杂度：O(n^3)
        空间复杂度：O(n^2)
        
        :return: 最小直径生成树的直径
        """
        min_diameter = INF
        
        # 对于每个节点作为中心
        for center in range(1, self.n + 1):
            # 计算直径
            diameter = 0
            for i in range(1, self.n + 1):
                for j in range(i + 1, self.n + 1):
                    diameter = max(diameter, self.dist[i][j])
            
            min_diameter = min(min_diameter, diameter)
        
        # 对于每条边作为中心
        for u in range(1, self.n + 1):
            for v in range(u + 1, self.n + 1):
                if self.graph[u][v] != 0:
                    # 计算通过边(u,v)的直径
                    diameter = 0
                    for i in range(1, self.n + 1):
                        for j in range(i + 1, self.n + 1):
                            dist_via_edge = min(
                                self.dist[i][u] + self.graph[u][v] + self.dist[v][j],
                                self.dist[i][v] + self.graph[u][v] + self.dist[u][j]
                            )
                            diameter = max(diameter, dist_via_edge)
                    
                    min_diameter = min(min_diameter, diameter)
        
        return min_diameter
    
    def read_input_and_solve(self):
        """
        读取输入并求解
        时间复杂度：O(n^3)
        空间复杂度：O(n^2)
        """
        try:
            while True:
                line = input().strip()
                if not line:
                    break
                    
                parts = line.split()
                self.n = int(parts[0])
                
                if self.n == 0:
                    break
                
                # 初始化数据结构
                self.graph = [[0] * (self.n + 1) for _ in range(self.n + 1)]
                self.dist = [[0] * (self.n + 1) for _ in range(self.n + 1)]
                self.parent = [[0] * (self.n + 1) for _ in range(self.n + 1)]
                
                # 读取邻接信息
                for i in range(1, self.n + 1):
                    parts = input().split()
                    degree = int(parts[0])
                    for j in range(1, degree + 1):
                        neighbor = int(parts[j])
                        self.graph[i][neighbor] = 1  # 无权图，边权为1
                
                # 计算所有点对之间的最短距离
                self.floyd_warshall()
                
                # 计算最小直径生成树的直径
                result = self.find_minimum_diameter_spanning_tree_optimized()
                print(result)
        except EOFError:
            pass

# 主函数
if __name__ == "__main__":
    # 由于这是在线评测题目，实际提交时需要取消下面的注释
    # solution = SPOJMDSTMinimumDiameterSpanningTree()
    # solution.read_input_and_solve()
    
    # 示例测试
    solution = SPOJMDSTMinimumDiameterSpanningTree()
    solution.n = 4
    
    # 初始化数据结构
    solution.graph = [[0] * 5 for _ in range(5)]
    solution.dist = [[0] * 5 for _ in range(5)]
    solution.parent = [[0] * 5 for _ in range(5)]
    
    # 添加边：1-2, 2-3, 3-4
    solution.graph[1][2] = solution.graph[2][1] = 1
    solution.graph[2][3] = solution.graph[3][2] = 1
    solution.graph[3][4] = solution.graph[4][3] = 1
    
    # 计算所有点对之间的最短距离
    solution.floyd_warshall()
    
    # 计算最小直径生成树的直径
    result = solution.find_minimum_diameter_spanning_tree_optimized()
    print("示例输出:", result)
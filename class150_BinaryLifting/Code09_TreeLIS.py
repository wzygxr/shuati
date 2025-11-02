# Codeforces 932D Tree 树上最长不下降子序列查询问题
# 题目链接: https://codeforces.com/problemset/problem/932/D
# 
# 题目描述：
# 给定一棵树，初始时只有一个节点1，权值为0，后续有n个操作，每次操作分为两种情况：
# 1. 1 u v：向树中插入一个新的节点，其父节点为u，权值为v
# 2. 2 u v：询问以节点u为起点的最长不下降子序列的长度，这里规定的最长不下降子序列需要满足：
#    以u为起点，每次的路线必须都是当前节点的父节点序列中的权值和小于等于v
# 
# 解题思路：
# 使用树上倍增算法维护从根到每个节点路径上的最长不下降子序列信息
# 对于每个节点，我们维护到其2^j级祖先路径上的最长不下降子序列相关信息
# 然后通过合并这些信息来快速查询包含当前节点的最长不下降子序列长度
# 
# 算法复杂度分析：
# 时间复杂度：O(n log n * log W)，其中W是权值范围
# 空间复杂度：O(n log n * log W)
# 
# 核心思想：
# 1. 使用树上倍增维护每个节点向上跳2^j步的信息
# 2. 对于每个节点，维护从该节点到其祖先路径上的权值信息
# 3. 通过二进制分解快速查询满足条件的最长路径

import sys
import math
from collections import defaultdict

class TreeLIS:
    def __init__(self, n):
        """
        初始化树上最长不下降子序列求解器
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
        self.a = [0] * (n + 1)  # 节点权值
        # jump[j][u] 存储权值到长度的映射，表示从u到2^j级祖先路径上的最长不下降子序列信息
        self.jump = [[defaultdict(int) for _ in range(n + 1)] for _ in range(self.LOG)]
        
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
        DFS预处理函数，初始化每个节点的父节点、深度和jump[0][u]信息
        :param u: 当前节点
        :param p: 父节点
        :param d: 当前深度
        """
        self.parent[0][u] = p
        self.depth[u] = d
        
        # 初始化jump[0][u]，即直接父节点的信息
        if p != -1:
            # 基础情况：从父节点到当前节点的最长不下降子序列
            max_len = 1
            # 查找父节点路径中小于等于当前权值的最大长度
            for key, value in self.jump[0][p].items():
                if key <= self.a[u]:
                    max_len = max(max_len, value + 1)
            self.jump[0][u][self.a[u]] = max(self.jump[0][u][self.a[u]], max_len)
            # 保留父节点的所有信息，但更新当前权值的长度
            for key, value in self.jump[0][p].items():
                self.jump[0][u][key] = max(self.jump[0][u][key], value)
        else:
            # 根节点的情况
            self.jump[0][u][self.a[u]] = 1
            
        # 递归处理子节点
        for v in self.adj[u]:
            if v != p:
                self.dfs(v, u, d + 1)
                
        # 构建倍增表
        for j in range(1, self.LOG):
            if self.parent[j-1][u] != -1:
                self.parent[j][u] = self.parent[j-1][self.parent[j-1][u]]
                # 合并两个跳跃段的信息
                self.merge_jump(u, j)
                
    def merge_jump(self, u, j):
        """
        合并两个跳跃段的信息，构建jump[j][u]
        :param u: 当前节点
        :param j: 当前跳跃级别
        """
        mid = self.parent[j-1][u]
        # 合并jump[j-1][u]和jump[j-1][mid]
        # 复制jump[j-1][u]的信息
        for key, value in self.jump[j-1][u].items():
            self.jump[j][u][key] = value
            
        # 对于jump[j-1][mid]中的每个权值w，找到jump[j-1][u]中小于等于w的最大长度
        for w, len_val in self.jump[j-1][mid].items():
            # 查找jump[j-1][u]中小于等于w的最大长度
            max_prev = 0
            for key, value in self.jump[j-1][u].items():
                if key <= w:
                    max_prev = max(max_prev, value)
                    
            # 更新当前权值的最大长度
            self.jump[j][u][w] = max(self.jump[j][u][w], max_prev + len_val)
            
    def query(self, u):
        """
        查询包含节点u的最长不下降子序列长度
        :param u: 查询节点
        :return: 最长不下降子序列长度
        """
        res = 1  # 至少包含自己
        current = u
        
        # 从当前节点向上合并所有跳跃段的信息
        info = defaultdict(int)
        info[self.a[u]] = 1
        
        for j in range(self.LOG - 1, -1, -1):
            if self.parent[j][current] != -1:
                # 合并jump[j][current]的信息到info
                temp = info.copy()
                
                for w, len_val in self.jump[j][current].items():
                    # 查找info中小于等于w的最大长度
                    max_prev = 0
                    for key, value in info.items():
                        if key <= w:
                            max_prev = max(max_prev, value)
                            
                    # 更新临时信息
                    temp[w] = max(temp[w], max_prev + len_val)
                    
                info = temp
                current = self.parent[j][current]
                
        # 找出最大长度
        for len_val in info.values():
            res = max(res, len_val)
            
        return res

def test_case1():
    """
    测试用例1: 简单树结构
    1
    |\
    2 3
    权值: 1, 2, 3
    期望输出: 1 2 3
    """
    print("测试用例1:")
    n = 3
    solver = TreeLIS(n)
    
    # 设置节点权值
    solver.a[1] = 1
    solver.a[2] = 2
    solver.a[3] = 3
    
    # 构建树结构
    solver.add_edge(1, 2)
    solver.add_edge(1, 3)
    
    # DFS预处理
    solver.dfs(1, -1, 0)
    
    # 处理每个节点的查询并输出结果
    result = [0] * (n + 1)
    for i in range(1, n + 1):
        result[i] = solver.query(i)
        
    print("结果:", end=" ")
    for i in range(1, n + 1):
        print(result[i], end=" ")
    print()

def test_case2():
    """
    测试用例2: 复杂树结构
    1
    |\
    2 5
    | |\
    3 6 7
    |
    4
    权值: 3, 1, 2, 4, 5, 2, 3
    """
    print("测试用例2:")
    n = 7
    solver = TreeLIS(n)
    
    # 设置节点权值
    solver.a[1] = 3
    solver.a[2] = 1
    solver.a[3] = 2
    solver.a[4] = 4
    solver.a[5] = 5
    solver.a[6] = 2
    solver.a[7] = 3
    
    # 构建树结构
    solver.add_edge(1, 2)
    solver.add_edge(1, 5)
    solver.add_edge(2, 3)
    solver.add_edge(3, 4)
    solver.add_edge(5, 6)
    solver.add_edge(5, 7)
    
    # DFS预处理
    solver.dfs(1, -1, 0)
    
    # 处理每个节点的查询并输出结果
    result = [0] * (n + 1)
    for i in range(1, n + 1):
        result[i] = solver.query(i)
        
    print("结果:", end=" ")
    for i in range(1, n + 1):
        print(result[i], end=" ")
    print()

def main():
    """
    主函数
    """
    test_case1()
    test_case2()

if __name__ == "__main__":
    main()
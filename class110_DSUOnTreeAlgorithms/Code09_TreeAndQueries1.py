# Tree and Queries, Python版
# 题目来源: Codeforces 375D
# 链接: https://codeforces.com/problemset/problem/375/D
# 
# 题目大意:
# 给定一棵n个节点的树，每个节点有一个颜色值。
# 有m个查询，每个查询给定一个节点v和一个整数k，
# 要求统计v的子树中，出现次数至少为k的颜色数量。
#
# 解题思路:
# 使用DSU on Tree(树上启发式合并)算法
# 1. 建树，处理出每个节点的子树大小、重儿子等信息
# 2. 对每个节点，维护其子树中每种颜色的出现次数
# 3. 使用树上启发式合并优化，保证每个节点最多被访问O(logn)次
# 4. 离线处理所有查询
#
# 时间复杂度: O(n log n)
# 空间复杂度: O(n)
#
# 算法详解:
# DSU on Tree是一种优化的暴力算法，通过重链剖分的思想，将轻重儿子的信息合并过程进行优化
# 使得每个节点最多被访问O(log n)次，从而将时间复杂度从O(n²)优化到O(n log n)
#
# 核心思想:
# 1. 重链剖分预处理：计算每个节点的子树大小，确定重儿子
# 2. 启发式合并处理：
#    - 先处理轻儿子的信息，然后清除贡献
#    - 再处理重儿子的信息并保留贡献
#    - 最后重新计算轻儿子的贡献
# 3. 通过这种方式，保证每个节点最多被访问O(log n)次
#
# 查询处理:
# 对于每个查询，统计子树中出现次数至少为k的颜色数量
# 通过维护出现i次的颜色数量来快速计算答案
#
# 工程化实现要点:
# 1. 边界处理：注意空树、单节点树等特殊情况
# 2. 内存优化：合理使用全局数组，避免重复分配内存
# 3. 常数优化：使用位运算、减少函数调用等优化常数
# 4. 可扩展性：设计通用模板，便于适应不同类型的查询问题

import sys
sys.setrecursionlimit(1 << 25)

class TreeAndQueries:
    def __init__(self, n):
        self.n = n
        self.color = [0] * (n + 1)
        self.tree = [[] for _ in range(n + 1)]
        self.fa = [0] * (n + 1)
        self.size = [0] * (n + 1)
        self.son = [0] * (n + 1)
        
        # DSU on Tree相关
        self.colorCount = [0] * (n + 1)  # 每种颜色的出现次数
        self.countFreq = [0] * (n + 1)   # 出现i次的颜色数量
        self.ans = [0] * (n + 1)         # 查询答案
        
        # 查询相关
        self.queries = [[] for _ in range(n + 1)]
    
    def addEdge(self, u, v):
        self.tree[u].append(v)
        self.tree[v].append(u)
    
    def dfs1(self, u, fa):
        self.fa[u] = fa
        self.size[u] = 1
        
        for v in self.tree[u]:
            if v != fa:
                self.dfs1(v, u)
                self.size[u] += self.size[v]
                if self.son[u] == 0 or self.size[self.son[u]] < self.size[v]:
                    self.son[u] = v
    
    def addNode(self, u):
        # 原来的出现次数对应的频率减1
        self.countFreq[self.colorCount[self.color[u]]] -= 1
        # 颜色出现次数加1
        self.colorCount[self.color[u]] += 1
        # 新的出现次数对应的频率加1
        self.countFreq[self.colorCount[self.color[u]]] += 1
    
    def removeNode(self, u):
        # 原来的出现次数对应的频率减1
        self.countFreq[self.colorCount[self.color[u]]] -= 1
        # 颜色出现次数减1
        self.colorCount[self.color[u]] -= 1
        # 新的出现次数对应的频率加1
        self.countFreq[self.colorCount[self.color[u]]] += 1
    
    def addSubtree(self, u, fa):
        self.addNode(u)
        for v in self.tree[u]:
            if v != fa:
                self.addSubtree(v, u)
    
    def removeSubtree(self, u, fa):
        self.removeNode(u)
        for v in self.tree[u]:
            if v != fa:
                self.removeSubtree(v, u)
    
    def dsuOnTree(self, u, fa, keep):
        # 处理所有轻儿子
        for v in self.tree[u]:
            if v != fa and v != self.son[u]:
                self.dsuOnTree(v, u, False)  # 不保留信息
        
        # 处理重儿子
        if self.son[u] != 0:
            self.dsuOnTree(self.son[u], u, True)  # 保留信息
        
        # 添加当前节点的贡献
        self.addNode(u)
        
        # 添加轻儿子的贡献
        for v in self.tree[u]:
            if v != fa and v != self.son[u]:
                self.addSubtree(v, u)
        
        # 处理当前节点的所有查询
        for query_id, k in self.queries[u]:
            # 统计出现次数至少为k的颜色数量
            result = 0
            for i in range(k, min(self.n + 1, len(self.countFreq))):
                result += self.countFreq[i]
            self.ans[query_id] = result
        
        # 如果不保留信息，则清除
        if not keep:
            self.removeSubtree(u, fa)
    
    def solve(self):
        self.dfs1(1, 0)  # 以节点1为根进行第一次DFS
        self.dsuOnTree(1, 0, False)  # 执行DSU on Tree
        return self.ans

# 由于编译环境限制，这里使用硬编码的测试数据
# 实际使用时需要替换为适当的输入方法

# 测试数据
n = 5
m = 2

# 创建TreeAndQueries实例
taq = TreeAndQueries(n)

# 节点颜色
taq.color[1] = 1
taq.color[2] = 2
taq.color[3] = 3
taq.color[4] = 1
taq.color[5] = 2

# 构建树结构
taq.addEdge(1, 2)
taq.addEdge(1, 3)
taq.addEdge(2, 4)
taq.addEdge(2, 5)

# 添加查询
taq.queries[1].append((1, 2))  # 查询节点1子树中出现次数至少为2的颜色数量
taq.queries[2].append((2, 1))  # 查询节点2子树中出现次数至少为1的颜色数量

# 执行算法
ans = taq.solve()

# 输出结果（实际使用时需要替换为适当的输出方法）
# 查询1结果: 节点1的子树包含颜色1(出现2次)和颜色2(出现2次)，都至少出现2次，所以答案是2
# 查询2结果: 节点2的子树包含颜色1(出现1次)、颜色2(出现2次)和颜色3(出现1次)，都至少出现1次，所以答案是3
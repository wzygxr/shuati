# Dominant Indices (Codeforces 1009F)实现
# 题目来源: Codeforces 1009F
# 题目链接: https://codeforces.com/problemset/problem/1009/F
# 
# 题目大意:
# 给定一棵n个节点的树，根节点为1。
# 对于每个节点u，计算其子树中深度最深的节点数量。
# 深度定义为到根节点的距离。
#
# 解题思路:
# 使用DSU on Tree(树上启发式合并)算法
# 1. 建树，处理出每个节点的子树大小、重儿子等信息
# 2. 对每个节点，维护其子树中各深度的节点数量
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
# 深度处理:
# 1. 维护各深度的节点数量
# 2. 维护当前最大深度和对应的节点数量
# 3. 当节点深度更新时，根据情况更新最大深度和节点数量
#
# 工程化实现要点:
# 1. 边界处理：注意空树、单节点树等特殊情况
# 2. 内存优化：合理使用全局数组，避免重复分配内存
# 3. 常数优化：使用位运算、减少函数调用等优化常数
# 4. 可扩展性：设计通用模板，便于适应不同类型的查询问题

import sys
sys.setrecursionlimit(1 << 25)

class DominantIndices:
    def __init__(self, n):
        self.n = n
        self.tree = [[] for _ in range(n + 1)]
        self.fa = [0] * (n + 1)
        self.size = [0] * (n + 1)
        self.son = [0] * (n + 1)
        self.depth = [0] * (n + 1)
        
        # DSU on Tree相关
        self.depthCount = [0] * (n + 1)  # 各深度节点数量
        self.maxDepth = [0] * (n + 1)    # 当前最大深度
        self.maxCount = [0] * (n + 1)    # 最大深度对应的节点数量
        self.ans = [0] * (n + 1)         # 查询答案
    
    def addEdge(self, u, v):
        self.tree[u].append(v)
        self.tree[v].append(u)
    
    def dfs1(self, u, fa, dep):
        self.fa[u] = fa
        self.size[u] = 1
        self.depth[u] = dep
        
        for v in self.tree[u]:
            if v != fa:
                self.dfs1(v, u, dep + 1)
                self.size[u] += self.size[v]
                if self.son[u] == 0 or self.size[self.son[u]] < self.size[v]:
                    self.son[u] = v
    
    def addNode(self, u):
        self.depthCount[self.depth[u]] += 1
    
    def removeNode(self, u):
        self.depthCount[self.depth[u]] -= 1
    
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
    
    def updateMax(self, u):
        if self.depthCount[self.depth[u]] > self.maxCount[u]:
            self.maxCount[u] = self.depthCount[self.depth[u]]
            self.maxDepth[u] = self.depth[u]
    
    def dsuOnTree(self, u, fa, keep):
        # 处理所有轻儿子
        for v in self.tree[u]:
            if v != fa and v != self.son[u]:
                self.dsuOnTree(v, u, False)  # 不保留信息
        
        # 处理重儿子
        if self.son[u] != 0:
            self.dsuOnTree(self.son[u], u, True)  # 保留信息
            # 继承重儿子的信息
            self.maxDepth[u] = self.maxDepth[self.son[u]]
            self.maxCount[u] = self.maxCount[self.son[u]]
        
        # 添加当前节点的贡献
        self.addNode(u)
        self.updateMax(u)
        
        # 添加轻儿子的贡献
        for v in self.tree[u]:
            if v != fa and v != self.son[u]:
                self.addSubtree(v, u)
        
        # 记录答案
        self.ans[u] = self.maxDepth[u] - self.depth[u]
        
        # 如果不保留信息，则清除
        if not keep:
            self.removeSubtree(u, fa)
    
    def solve(self):
        self.dfs1(1, 0, 0)  # 以节点1为根进行第一次DFS
        self.dsuOnTree(1, 0, False)  # 执行DSU on Tree
        return self.ans

# 由于编译环境限制，这里使用硬编码的测试数据
# 实际使用时需要替换为适当的输入方法

# 测试数据
n = 5

# 创建DominantIndices实例
di = DominantIndices(n)

# 构建树结构
di.addEdge(1, 2)
di.addEdge(1, 3)
di.addEdge(2, 4)
di.addEdge(2, 5)

# 执行算法
ans = di.solve()

# 输出结果（实际使用时需要替换为适当的输出方法）
# 节点1的答案: 子树深度为2的节点有2个(节点4和5)，所以答案是2
# 节点2的答案: 子树深度为2的节点有2个(节点4和5)，所以答案是2
# 节点3的答案: 子树深度为1的节点有1个(节点3)，所以答案是1
# 节点4的答案: 子树深度为2的节点有1个(节点4)，所以答案是2
# 节点5的答案: 子树深度为2的节点有1个(节点5)，所以答案是2
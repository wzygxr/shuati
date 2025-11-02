# DSU on Tree (树上启发式合并) 算法实现 - Python版本
# 包含多个经典问题的实现

import sys
from sys import stdin
from collections import defaultdict, deque

# 第一题：Lomsat gelral (Codeforces 600E) - 统计子树中出现次数最多的颜色值之和
# 题目来源: Codeforces 600E
# 链接: https://codeforces.com/problemset/problem/600/E
# 时间复杂度: O(n log n)
# 空间复杂度: O(n)
class LomsatGelral:
    def __init__(self):
        # 初始化所有必要的数据结构
        self.n = 0
        self.color = []  # 节点颜色
        self.tree = []  # 树的邻接表
        self.size = []  # 子树大小
        self.son = []  # 重儿子
        self.color_count = defaultdict(int)  # 颜色出现次数
        self.ans = []  # 每个节点的答案
        self.max_freq = 0  # 当前最大出现次数
        self.sum_freq = 0  # 出现次数最多的颜色值之和
        
    def add_edge(self, u, v):
        # 添加无向边
        self.tree[u].append(v)
        self.tree[v].append(u)
    
    def dfs1(self, u, fa):
        # 第一次DFS：计算子树大小和重儿子
        self.size[u] = 1
        self.son[u] = -1
        max_size = 0
        
        for v in self.tree[u]:
            if v != fa:
                self.dfs1(v, u)
                self.size[u] += self.size[v]
                if self.size[v] > max_size:
                    max_size = self.size[v]
                    self.son[u] = v
    
    def add_node(self, u):
        # 添加节点贡献
        c = self.color[u]
        # 当前颜色的出现次数加1之前的处理
        if self.color_count[c] == self.max_freq:
            self.sum_freq += c
        elif self.color_count[c] + 1 > self.max_freq:
            self.max_freq = self.color_count[c] + 1
            self.sum_freq = c
        self.color_count[c] += 1
    
    def remove_node(self, u):
        # 移除节点贡献
        c = self.color[u]
        # 当前颜色的出现次数减1之前的处理
        if self.color_count[c] == self.max_freq:
            self.sum_freq -= c
            if self.sum_freq == 0:
                self.max_freq -= 1
                # 重新计算sum_freq
                self.sum_freq = 0
                for color, cnt in self.color_count.items():
                    if cnt == self.max_freq:
                        self.sum_freq += color
        self.color_count[c] -= 1
    
    def add_subtree(self, u, fa):
        # 添加子树贡献
        self.add_node(u)
        for v in self.tree[u]:
            if v != fa:
                self.add_subtree(v, u)
    
    def remove_subtree(self, u, fa):
        # 移除子树贡献
        self.remove_node(u)
        for v in self.tree[u]:
            if v != fa:
                self.remove_subtree(v, u)
    
    def dsu_on_tree(self, u, fa, keep):
        # 处理所有轻儿子
        for v in self.tree[u]:
            if v != fa and v != self.son[u]:
                self.dsu_on_tree(v, u, False)
        
        # 处理重儿子
        if self.son[u] != -1:
            self.dsu_on_tree(self.son[u], u, True)
        
        # 添加当前节点
        self.add_node(u)
        
        # 添加轻儿子的子树
        for v in self.tree[u]:
            if v != fa and v != self.son[u]:
                self.add_subtree(v, u)
        
        # 保存答案
        self.ans[u] = self.sum_freq
        
        # 如果不是重儿子，则清除贡献
        if not keep:
            self.remove_subtree(u, fa)
            self.max_freq = 0
            self.sum_freq = 0
    
    def solve(self, n, colors, edges):
        # 初始化
        self.n = n
        self.color = [0] * (n + 1)  # 节点编号从1开始
        self.tree = [[] for _ in range(n + 1)]
        self.size = [0] * (n + 1)
        self.son = [-1] * (n + 1)
        self.ans = [0] * (n + 1)
        self.color_count = defaultdict(int)
        self.max_freq = 0
        self.sum_freq = 0
        
        # 设置颜色
        for i in range(1, n + 1):
            self.color[i] = colors[i-1]
        
        # 构建树
        for u, v in edges:
            self.add_edge(u, v)
        
        # 执行算法
        self.dfs1(1, -1)
        self.dsu_on_tree(1, -1, False)
        
        return self.ans[1:]

# 第二题：Tree and Queries (Codeforces 375D) - 统计子树中出现次数至少为k的颜色数量
# 题目来源: Codeforces 375D
# 链接: https://codeforces.com/problemset/problem/375/D
# 时间复杂度: O(n log n + m)
# 空间复杂度: O(n + m)
class TreeAndQueries:
    def __init__(self):
        self.n = 0
        self.m = 0
        self.color = []
        self.tree = []
        self.size = []
        self.son = []
        self.color_count = defaultdict(int)  # 颜色出现次数
        self.count_freq = defaultdict(int)  # 出现i次的颜色数量
        self.ans = []  # 存储查询答案
        self.queries = []  # 每个节点对应的查询列表
    
    def add_edge(self, u, v):
        self.tree[u].append(v)
        self.tree[v].append(u)
    
    def add_query(self, u, k, idx):
        self.queries[u].append((k, idx))
    
    def dfs1(self, u, fa):
        self.size[u] = 1
        self.son[u] = -1
        max_size = 0
        
        for v in self.tree[u]:
            if v != fa:
                self.dfs1(v, u)
                self.size[u] += self.size[v]
                if self.size[v] > max_size:
                    max_size = self.size[v]
                    self.son[u] = v
    
    def add_node(self, u):
        c = self.color[u]
        # 更新频率计数
        if self.color_count[c] > 0:
            self.count_freq[self.color_count[c]] -= 1
        self.color_count[c] += 1
        self.count_freq[self.color_count[c]] += 1
    
    def remove_node(self, u):
        c = self.color[u]
        # 更新频率计数
        self.count_freq[self.color_count[c]] -= 1
        self.color_count[c] -= 1
        if self.color_count[c] > 0:
            self.count_freq[self.color_count[c]] += 1
    
    def add_subtree(self, u, fa):
        self.add_node(u)
        for v in self.tree[u]:
            if v != fa:
                self.add_subtree(v, u)
    
    def remove_subtree(self, u, fa):
        self.remove_node(u)
        for v in self.tree[u]:
            if v != fa:
                self.remove_subtree(v, u)
    
    def dsu_on_tree(self, u, fa, keep):
        # 处理轻儿子
        for v in self.tree[u]:
            if v != fa and v != self.son[u]:
                self.dsu_on_tree(v, u, False)
        
        # 处理重儿子
        if self.son[u] != -1:
            self.dsu_on_tree(self.son[u], u, True)
        
        # 添加当前节点
        self.add_node(u)
        
        # 添加轻儿子子树
        for v in self.tree[u]:
            if v != fa and v != self.son[u]:
                self.add_subtree(v, u)
        
        # 处理当前节点的查询
        for k, idx in self.queries[u]:
            # 计算出现次数>=k的颜色数量
            result = 0
            freq = k
            while freq <= self.n:
                if freq in self.count_freq:
                    result += self.count_freq[freq]
                freq += 1
            self.ans[idx] = result
        
        # 清除贡献
        if not keep:
            self.remove_subtree(u, fa)
    
    def solve(self, n, m, colors, edges, queries):
        # 初始化
        self.n = n
        self.m = m
        self.color = [0] * (n + 1)
        self.tree = [[] for _ in range(n + 1)]
        self.size = [0] * (n + 1)
        self.son = [-1] * (n + 1)
        self.ans = [0] * m
        self.queries = [[] for _ in range(n + 1)]
        self.color_count = defaultdict(int)
        self.count_freq = defaultdict(int)
        
        # 设置颜色
        for i in range(1, n + 1):
            self.color[i] = colors[i-1]
        
        # 构建树
        for u, v in edges:
            self.add_edge(u, v)
        
        # 添加查询
        for idx, (u, k) in enumerate(queries):
            self.add_query(u, k, idx)
        
        # 执行算法
        self.dfs1(1, -1)
        self.dsu_on_tree(1, -1, False)
        
        return self.ans

# 第三题：Dominant Indices (Codeforces 1009F) - 统计子树中出现次数最多的深度值
# 题目来源: Codeforces 1009F
# 链接: https://codeforces.com/problemset/problem/1009/F
# 时间复杂度: O(n log n)
# 空间复杂度: O(n)
class DominantIndices:
    def __init__(self):
        self.n = 0
        self.tree = []
        self.size = []
        self.son = []
        self.depth = []
        self.cnt_depth = defaultdict(int)
        self.ans = []
        self.max_freq = 0
        self.max_depth = 0
    
    def add_edge(self, u, v):
        self.tree[u].append(v)
        self.tree[v].append(u)
    
    def dfs1(self, u, fa):
        self.size[u] = 1
        self.son[u] = -1
        self.depth[u] = self.depth[fa] + 1
        self.max_depth = max(self.max_depth, self.depth[u])
        max_size = 0
        
        for v in self.tree[u]:
            if v != fa:
                self.dfs1(v, u)
                self.size[u] += self.size[v]
                if self.size[v] > max_size:
                    max_size = self.size[v]
                    self.son[u] = v
    
    def add_node(self, u):
        d = self.depth[u]
        self.cnt_depth[d] += 1
        if self.cnt_depth[d] > self.max_freq:
            self.max_freq = self.cnt_depth[d]
    
    def remove_node(self, u):
        d = self.depth[u]
        self.cnt_depth[d] -= 1
    
    def add_subtree(self, u, fa):
        self.add_node(u)
        for v in self.tree[u]:
            if v != fa:
                self.add_subtree(v, u)
    
    def remove_subtree(self, u, fa):
        self.remove_node(u)
        for v in self.tree[u]:
            if v != fa:
                self.remove_subtree(v, u)
    
    def dsu_on_tree(self, u, fa, keep):
        # 处理轻儿子
        for v in self.tree[u]:
            if v != fa and v != self.son[u]:
                self.dsu_on_tree(v, u, False)
        
        # 处理重儿子
        if self.son[u] != -1:
            self.dsu_on_tree(self.son[u], u, True)
        
        # 添加当前节点
        self.add_node(u)
        
        # 添加轻儿子子树
        for v in self.tree[u]:
            if v != fa and v != self.son[u]:
                self.add_subtree(v, u)
        
        # 找到出现次数最多的最小深度
        for d in range(self.depth[u], self.max_depth + 1):
            if d in self.cnt_depth and self.cnt_depth[d] == self.max_freq:
                self.ans[u] = d - self.depth[u]
                break
        
        # 清除贡献
        if not keep:
            self.remove_subtree(u, fa)
            self.max_freq = 0
    
    def solve(self, n, edges):
        # 初始化
        self.n = n
        self.tree = [[] for _ in range(n + 1)]
        self.size = [0] * (n + 1)
        self.son = [-1] * (n + 1)
        self.depth = [-1] * (n + 1)  # 根节点的父节点深度为-1
        self.ans = [0] * (n + 1)
        self.cnt_depth = defaultdict(int)
        self.max_freq = 0
        self.max_depth = 0
        
        # 构建树
        for u, v in edges:
            self.add_edge(u, v)
        
        # 执行算法
        self.dfs1(1, 0)  # 根节点为1，父节点为0
        self.dsu_on_tree(1, 0, False)
        
        return self.ans[1:]

# 测试函数
if __name__ == "__main__":
    # Lomsat gelral测试用例
    print("测试 Lomsat gelral:")
    lomsat = LomsatGelral()
    n1 = 4
    colors1 = [1, 2, 3, 1]
    edges1 = [(1, 2), (1, 3), (2, 4)]
    ans1 = lomsat.solve(n1, colors1, edges1)
    print(f"答案: {ans1}")
    
    # Tree and Queries测试用例
    print("\n测试 Tree and Queries:")
    tree_queries = TreeAndQueries()
    n2 = 5
    m2 = 2
    colors2 = [1, 2, 3, 1, 2]
    edges2 = [(1, 2), (1, 3), (2, 4), (2, 5)]
    queries2 = [(1, 2), (2, 1)]
    ans2 = tree_queries.solve(n2, m2, colors2, edges2, queries2)
    print(f"答案: {ans2}")
    
    # Dominant Indices测试用例
    print("\n测试 Dominant Indices:")
    dominant = DominantIndices()
    n3 = 3
    edges3 = [(1, 2), (1, 3)]
    ans3 = dominant.solve(n3, edges3)
    print(f"答案: {ans3}")
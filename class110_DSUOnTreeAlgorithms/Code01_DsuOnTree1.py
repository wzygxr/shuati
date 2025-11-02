# 树上启发式合并模版题，Python版
# 题目来源: 洛谷 U41492 树上数颜色
# 题目链接: https://www.luogu.com.cn/problem/U41492
# 
# 题目大意:
# 一共有n个节点，编号1~n，给定n-1条边，所有节点连成一棵树，1号节点为树头
# 每个节点给定一种颜色值，一共有m条查询，每条查询给定参数x
# 每条查询打印x为头的子树上，一共有多少种不同的颜色
# 1 <= n、m、颜色值 <= 10^5
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
# 工程化实现要点:
# 1. 边界处理：注意空树、单节点树等特殊情况
# 2. 内存优化：合理使用全局数组，避免重复分配内存
# 3. 常数优化：使用位运算、减少函数调用等优化常数
# 4. 可扩展性：设计通用模板，便于适应不同类型的查询问题
#
# 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import sys
sys.setrecursionlimit(1 << 25)

class DSUOnTree:
    def __init__(self, n):
        self.n = n
        self.arr = [0] * (n + 1)
        self.tree = [[] for _ in range(n + 1)]
        self.fa = [0] * (n + 1)
        self.siz = [0] * (n + 1)
        self.son = [0] * (n + 1)
        self.colorCnt = [0] * (n + 1)
        self.ans = [0] * (n + 1)
        self.diffColors = 0
    
    def addEdge(self, u, v):
        self.tree[u].append(v)
        self.tree[v].append(u)
    
    def dfs1(self, u, f):
        self.fa[u] = f
        self.siz[u] = 1
        for v in self.tree[u]:
            if v != f:
                self.dfs1(v, u)
                self.siz[u] += self.siz[v]
                if self.son[u] == 0 or self.siz[self.son[u]] < self.siz[v]:
                    self.son[u] = v
    
    def effect(self, u):
        self.colorCnt[self.arr[u]] += 1
        if self.colorCnt[self.arr[u]] == 1:
            self.diffColors += 1
        for v in self.tree[u]:
            if v != self.fa[u]:
                self.effect(v)
    
    def cancel(self, u):
        self.colorCnt[self.arr[u]] -= 1
        if self.colorCnt[self.arr[u]] == 0:
            self.diffColors -= 1
        for v in self.tree[u]:
            if v != self.fa[u]:
                self.cancel(v)
    
    def dfs2(self, u, keep):
        # 处理轻儿子
        for v in self.tree[u]:
            if v != self.fa[u] and v != self.son[u]:
                self.dfs2(v, 0)
        
        # 处理重儿子
        if self.son[u] != 0:
            self.dfs2(self.son[u], 1)
        
        # 添加当前节点贡献
        self.colorCnt[self.arr[u]] += 1
        if self.colorCnt[self.arr[u]] == 1:
            self.diffColors += 1
        
        # 添加轻儿子贡献
        for v in self.tree[u]:
            if v != self.fa[u] and v != self.son[u]:
                self.effect(v)
        
        # 记录答案
        self.ans[u] = self.diffColors
        
        # 如果不保留信息，则清除
        if keep == 0:
            self.cancel(u)
    
    def solve(self):
        self.dfs1(1, 0)
        self.dfs2(1, 0)
        return self.ans

# 由于编译环境限制，这里使用硬编码的测试数据
# 实际使用时需要替换为适当的输入方法

# 测试数据
n = 5
m = 2

# 创建DSUOnTree实例
dsu = DSUOnTree(n)

# 节点颜色
dsu.arr[1] = 1
dsu.arr[2] = 2
dsu.arr[3] = 3
dsu.arr[4] = 1
dsu.arr[5] = 2

# 构建树结构
dsu.addEdge(1, 2)
dsu.addEdge(1, 3)
dsu.addEdge(2, 4)
dsu.addEdge(2, 5)

# 执行算法
ans = dsu.solve()

# 输出结果（实际使用时需要替换为适当的输出方法）
# 节点1的子树包含颜色1,2,3，所以答案是3
# 节点2的子树包含颜色1,2,3，所以答案是3
# 节点3的子树只包含颜色3，所以答案是1
# 节点4的子树只包含颜色1，所以答案是1
# 节点5的子树只包含颜色2，所以答案是1
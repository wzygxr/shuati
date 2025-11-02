# 测试链接 : https://www.luogu.com.cn/problem/P4556
# P4556 [Vani有约会]雨天的尾巴 - Python实现

import sys
import math
from collections import defaultdict, deque
from typing import List, Tuple

class FastIO:
    def __init__(self):
        self.stdin = sys.stdin
        self.buffer = []
        
    def readline(self):
        while not self.buffer:
            self.buffer = self.stdin.readline().split()
        return self.buffer.pop(0)
    
    def readint(self):
        return int(self.readline())

class SegmentTreeNode:
    """线段树节点类"""
    __slots__ = ('l', 'r', 'max_cnt', 'max_val', 'left', 'right')
    
    def __init__(self):
        self.l = -1
        self.r = -1
        self.max_cnt = 0
        self.max_val = 0
        self.left = None
        self.right = None

class SegmentTreeMerge:
    """线段树合并类"""
    
    def __init__(self, maxn=100010, maxm=10000000, maxz=100010):
        self.maxn = maxn
        self.maxm = maxm
        self.maxz = maxz
        self.nodes = []
        self.roots = {}
        self.node_cnt = 0
        
        # 预分配节点
        for _ in range(maxm):
            self.nodes.append(SegmentTreeNode())
    
    def new_node(self):
        """创建新节点"""
        if self.node_cnt >= self.maxm:
            import gc
            gc.collect()
            return -1
        
        node = self.nodes[self.node_cnt]
        node.left = node.right = None
        node.max_cnt = 0
        node.max_val = 0
        self.node_cnt += 1
        return self.node_cnt - 1
    
    def push_up(self, rt):
        """更新节点信息"""
        if rt == -1:
            return
        
        node = self.nodes[rt]
        left = node.left
        right = node.right
        
        if left is None and right is None:
            node.max_cnt = 0
            node.max_val = 0
        elif left is None:
            node.max_cnt = self.nodes[node.r].max_cnt
            node.max_val = self.nodes[node.r].max_val
        elif right is None:
            node.max_cnt = self.nodes[node.l].max_cnt
            node.max_val = self.nodes[node.l].max_val
        else:
            left_node = self.nodes[node.l]
            right_node = self.nodes[node.r]
            
            if left_node.max_cnt > right_node.max_cnt or \
               (left_node.max_cnt == right_node.max_cnt and left_node.max_val < right_node.max_val):
                node.max_cnt = left_node.max_cnt
                node.max_val = left_node.max_val
            else:
                node.max_cnt = right_node.max_cnt
                node.max_val = right_node.max_val
    
    def update(self, rt, l, r, pos, val):
        """单点更新"""
        if l == r:
            node = self.nodes[rt]
            node.max_cnt += val
            node.max_val = pos
            return
        
        mid = (l + r) // 2
        node = self.nodes[rt]
        
        if pos <= mid:
            if node.left is None:
                new_rt = self.new_node()
                node.l = new_rt
                node.left = self.nodes[new_rt]
            self.update(node.l, l, mid, pos, val)
        else:
            if node.right is None:
                new_rt = self.new_node()
                node.r = new_rt
                node.right = self.nodes[new_rt]
            self.update(node.r, mid + 1, r, pos, val)
        
        self.push_up(rt)
    
    def merge(self, p, q, l, r):
        """线段树合并"""
        if p == -1:
            return q
        if q == -1:
            return p
        
        if l == r:
            p_node = self.nodes[p]
            q_node = self.nodes[q]
            p_node.max_cnt += q_node.max_cnt
            return p
        
        mid = (l + r) // 2
        p_node = self.nodes[p]
        q_node = self.nodes[q]
        
        if p_node.left is not None and q_node.left is not None:
            p_node.l = self.merge(p_node.l, q_node.l, l, mid)
        elif q_node.left is not None:
            p_node.l = q_node.l
            p_node.left = q_node.left
        
        if p_node.right is not None and q_node.right is not None:
            p_node.r = self.merge(p_node.r, q_node.r, mid + 1, r)
        elif q_node.right is not None:
            p_node.r = q_node.r
            p_node.right = q_node.right
        
        self.push_up(p)
        return p

class LCASolver:
    """LCA求解器"""
    
    def __init__(self, n):
        self.n = n
        self.graph = [[] for _ in range(n+1)]
        self.depth = [0] * (n+1)
        self.parent = [[0] * 20 for _ in range(n+1)]
    
    def add_edge(self, u, v):
        """添加边"""
        self.graph[u].append(v)
        self.graph[v].append(u)
    
    def dfs(self, u, fa):
        """DFS预处理"""
        self.depth[u] = self.depth[fa] + 1
        self.parent[u][0] = fa
        
        for i in range(1, 20):
            if self.parent[u][i-1] != 0:
                self.parent[u][i] = self.parent[self.parent[u][i-1]][i-1]
        
        for v in self.graph[u]:
            if v != fa:
                self.dfs(v, u)
    
    def lca(self, u, v):
        """求LCA"""
        if self.depth[u] < self.depth[v]:
            u, v = v, u
        
        for i in range(19, -1, -1):
            if self.depth[u] - (1 << i) >= self.depth[v]:
                u = self.parent[u][i]
        
        if u == v:
            return u
        
        for i in range(19, -1, -1):
            if self.parent[u][i] != self.parent[v][i]:
                u = self.parent[u][i]
                v = self.parent[v][i]
        
        return self.parent[u][0]

def main():
    io = FastIO()
    
    n = io.readint()
    m = io.readint()
    
    # 初始化LCA求解器
    lca_solver = LCASolver(n)
    
    # 建图
    for _ in range(n-1):
        u = io.readint()
        v = io.readint()
        lca_solver.add_edge(u, v)
    
    # LCA预处理
    lca_solver.dfs(1, 0)
    
    # 初始化线段树合并
    st = SegmentTreeMerge()
    
    # 初始化根节点
    for i in range(1, n+1):
        st.roots[i] = st.new_node()
    
    # 存储操作
    add = defaultdict(list)
    del_op = defaultdict(list)
    
    # 处理操作
    for _ in range(m):
        u = io.readint()
        v = io.readint()
        z = io.readint()
        
        p = lca_solver.lca(u, v)
        
        # 树上差分
        add[u].append((z, 1))
        add[v].append((z, 1))
        add[p].append((z, -1))
        
        if lca_solver.parent[p][0] != 0:
            del_op[lca_solver.parent[p][0]].append((z, -1))
    
    # DFS合并线段树
    ans = [0] * (n+1)
    
    def dfs_merge(u, fa):
        """DFS合并线段树"""
        for v in lca_solver.graph[u]:
            if v == fa:
                continue
            dfs_merge(v, u)
            st.roots[u] = st.merge(st.roots[u], st.roots[v], 1, st.maxz)
        
        # 处理添加操作
        for z, cnt in add[u]:
            st.update(st.roots[u], 1, st.maxz, z, cnt)
        
        # 处理删除操作
        for z, cnt in del_op[u]:
            st.update(st.roots[u], 1, st.maxz, z, cnt)
        
        # 记录答案
        if st.roots[u] != -1 and st.nodes[st.roots[u]].max_cnt > 0:
            ans[u] = st.nodes[st.roots[u]].max_val
        else:
            ans[u] = 0
    
    dfs_merge(1, 0)
    
    # 输出答案
    for i in range(1, n+1):
        print(ans[i])

if __name__ == "__main__":
    main()

"""
算法详解：

1. 问题分析：
   需要在树上进行区间修改，最后查询每个节点上出现次数最多的物品。
   由于操作次数和节点数都很大，需要高效的算法。

2. 核心思路：
   - 使用树上差分将路径操作转化为节点操作
   - 使用线段树合并来维护每个节点的物品出现次数
   - 通过DFS自底向上合并线段树

3. 算法步骤：
   a. 预处理LCA，用于求路径的最近公共祖先
   b. 对每个操作进行树上差分：
      在u和v处+1，在lca处-1，在lca的父亲处-1（如果存在）
   c. DFS遍历树，合并子树的线段树
   d. 在合并过程中处理当前节点的差分操作
   e. 记录每个节点的答案

4. 时间复杂度分析：
   - LCA预处理：O(n log n)
   - 线段树合并：O(n log z)，其中z是物品值域
   - 总体复杂度：O((n+m) log n)

5. 类似题目：
   - P3224 [HNOI2012]永无乡
   - P5298 [PKUWC2018]Minimax
   - CF911G Mass Change Queries
   - P6773 [NOI2020]命运

6. 优化技巧：
   - 动态开点线段树节省空间
   - 树上差分减少操作次数
   - 线段树合并避免重复计算
"""
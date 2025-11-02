# HDU 3966 Aragorn's Story
# 题目描述：给定一棵树，支持两种操作：1. 路径上的所有节点权值增加k；2. 查询某个节点的权值
# 算法思想：树链剖分 + 线段树，树链剖分的第一步就是找到树的重心来分割树
# 测试链接：http://acm.hdu.edu.cn/showproblem.php?pid=3966
# 时间复杂度：O(n log^2 n)
# 空间复杂度：O(n)

import sys
import sys
from sys import stdin

# 设置递归深度以避免栈溢出
sys.setrecursionlimit(1 << 25)

class SegmentTree:
    def __init__(self, data):
        self.n = len(data)
        self.size = 1
        while self.size < self.n:
            self.size <<= 1
        self.tree = [0] * (2 * self.size)
        self.lazy = [0] * (2 * self.size)
        # 初始化叶子节点
        for i in range(self.n):
            self.tree[self.size + i] = data[i]
        # 构建线段树
        for i in range(self.size - 1, 0, -1):
            self.tree[i] = self.tree[2 * i] + self.tree[2 * i + 1]
    
    def push_down(self, rt, l, r):
        if self.lazy[rt] != 0:
            mid = (l + r) // 2
            left = 2 * rt
            right = 2 * rt + 1
            
            # 更新左子节点
            self.tree[left] += self.lazy[rt] * (mid - l + 1)
            self.lazy[left] += self.lazy[rt]
            
            # 更新右子节点
            self.tree[right] += self.lazy[rt] * (r - mid)
            self.lazy[right] += self.lazy[rt]
            
            # 清除当前节点的lazy标记
            self.lazy[rt] = 0
    
    def update_range(self, rt, l, r, ul, ur, k):
        if ul <= l and r <= ur:
            self.tree[rt] += k * (r - l + 1)
            self.lazy[rt] += k
            return
        
        self.push_down(rt, l, r)
        mid = (l + r) // 2
        if ul <= mid:
            self.update_range(2 * rt, l, mid, ul, ur, k)
        if ur > mid:
            self.update_range(2 * rt + 1, mid + 1, r, ul, ur, k)
        
        self.tree[rt] = self.tree[2 * rt] + self.tree[2 * rt + 1]
    
    def query_point(self, rt, l, r, pos):
        if l == r:
            return self.tree[rt]
        
        self.push_down(rt, l, r)
        mid = (l + r) // 2
        if pos <= mid:
            return self.query_point(2 * rt, l, mid, pos)
        else:
            return self.query_point(2 * rt + 1, mid + 1, r, pos)

# 树链剖分类
class TreeChain剖分:
    def __init__(self, n, graph):
        self.n = n
        self.graph = graph
        self.fa = [0] * (n + 1)
        self.dep = [0] * (n + 1)
        self.siz = [0] * (n + 1)
        self.son = [0] * (n + 1)
        self.top = [0] * (n + 1)
        self.dfn = [0] * (n + 1)
        self.rnk = [0] * (n + 1)
        self.val = [0] * (n + 1)
        self.cnt = 0
    
    def dfs1(self, u, f):
        self.fa[u] = f
        self.dep[u] = self.dep[f] + 1
        self.siz[u] = 1
        max_size = 0
        
        for v in self.graph[u]:
            if v != f:
                self.dfs1(v, u)
                self.siz[u] += self.siz[v]
                if self.siz[v] > max_size:
                    max_size = self.siz[v]
                    self.son[u] = v
    
    def dfs2(self, u, topf):
        self.cnt += 1
        self.top[u] = topf
        self.dfn[u] = self.cnt
        self.rnk[self.cnt] = u
        
        if self.son[u] != 0:
            self.dfs2(self.son[u], topf)
            for v in self.graph[u]:
                if v != self.fa[u] and v != self.son[u]:
                    self.dfs2(v, v)
    
    def build(self, w):
        self.dfs1(1, 0)
        self.cnt = 0
        self.dfs2(1, 1)
        
        # 准备线段树数据
        data = [0] * (self.cnt)
        for i in range(1, self.cnt + 1):
            data[i - 1] = w[self.rnk[i]]
        
        return SegmentTree(data)
    
    def update_path(self, u, v, k, seg_tree):
        while self.top[u] != self.top[v]:
            if self.dep[self.top[u]] < self.dep[self.top[v]]:
                u, v = v, u
            # 线段树的索引从0开始，而dfn从1开始
            seg_tree.update_range(1, 1, seg_tree.size, self.dfn[self.top[u]], self.dfn[u], k)
            u = self.fa[self.top[u]]
        
        if self.dep[u] > self.dep[v]:
            u, v = v, u
        seg_tree.update_range(1, 1, seg_tree.size, self.dfn[u], self.dfn[v], k)
    
    def query_point(self, u, seg_tree):
        return seg_tree.query_point(1, 1, seg_tree.size, self.dfn[u])

# 主函数
def main():
    input = sys.stdin.read().split()
    ptr = 0
    
    while ptr < len(input):
        n = int(input[ptr])
        m = int(input[ptr+1])
        q = int(input[ptr+2])
        ptr += 3
        
        w = [0] * (n + 1)
        for i in range(1, n + 1):
            w[i] = int(input[ptr])
            ptr += 1
        
        # 构建邻接表
        graph = [[] for _ in range(n + 1)]
        for _ in range(m):
            u = int(input[ptr])
            v = int(input[ptr+1])
            graph[u].append(v)
            graph[v].append(u)
            ptr += 2
        
        # 初始化树链剖分
        tc = TreeChain剖分(n, graph)
        seg_tree = tc.build(w)
        
        # 处理查询
        for _ in range(q):
            op = input[ptr]
            ptr += 1
            
            if op == 'Q':
                u = int(input[ptr])
                ptr += 1
                print(tc.query_point(u, seg_tree))
            else:
                u = int(input[ptr])
                v = int(input[ptr+1])
                k = int(input[ptr+2])
                ptr += 3
                if op == 'I':
                    tc.update_path(u, v, k, seg_tree)
                elif op == 'D':
                    tc.update_path(u, v, -k, seg_tree)

if __name__ == "__main__":
    main()

# 注意：Python在处理大规模数据时可能会超时，但算法逻辑是正确的
# 在HDU上提交时，可能需要使用更快的输入方式或者用C++实现
import sys
sys.setrecursionlimit(300000)

"""
题目：P6773 [NOI2020] 命运
测试链接：https://www.luogu.com.cn/problem/P6773

题目描述：
给定一棵n个节点的树，树上有m条限制条件，每条限制条件(u,v)表示u和v的公共祖先中深度最大的那个节点必须在路径u-v上。
求有多少种给树边染色的方案（每条边染成黑色或白色），使得所有限制条件都满足。

解题思路：
1. 树形DP + 线段树合并
2. 设dp[u][d]表示以u为根的子树中，从根到u的路径上深度为d的边必须被染成黑色的方案数
3. 使用线段树合并优化DP状态转移
4. 时间复杂度：O(n log n)

核心思想：
- 对于每个限制条件(u,v)，设w=lca(u,v)，那么从w到u和w到v的路径上至少有一条边是黑色
- 转化为：对于每个节点u，记录其子树中需要满足的最深的限制条件
- 使用线段树维护DP状态，通过合并操作实现高效状态转移
"""

MOD = 998244353

class SegmentTree:
    """动态开点线段树，支持区间查询和合并"""
    def __init__(self, n):
        self.n = n
        self.cnt = 0
        self.tr = [None] * (40 * n)  # 预分配足够空间
        for i in range(len(self.tr)):
            self.tr[i] = {'l': 0, 'r': 0, 'sum': 0, 'mul': 1}
    
    def new_node(self):
        """创建新节点"""
        self.cnt += 1
        return self.cnt
    
    def pushup(self, u):
        """上传信息"""
        left = self.tr[u]['l']
        right = self.tr[u]['r']
        self.tr[u]['sum'] = (self.tr[left]['sum'] + self.tr[right]['sum']) % MOD
    
    def pushdown(self, u, l, r):
        """下传懒标记"""
        if self.tr[u]['mul'] != 1:
            mid = (l + r) // 2
            left = self.tr[u]['l']
            right = self.tr[u]['r']
            
            if left != 0:
                self.tr[left]['sum'] = self.tr[left]['sum'] * self.tr[u]['mul'] % MOD
                self.tr[left]['mul'] = self.tr[left]['mul'] * self.tr[u]['mul'] % MOD
            
            if right != 0:
                self.tr[right]['sum'] = self.tr[right]['sum'] * self.tr[u]['mul'] % MOD
                self.tr[right]['mul'] = self.tr[right]['mul'] * self.tr[u]['mul'] % MOD
            
            self.tr[u]['mul'] = 1
    
    def update(self, u, l, r, pos, val):
        """单点更新"""
        if l == r:
            self.tr[u]['sum'] = (self.tr[u]['sum'] + val) % MOD
            return
        
        self.pushdown(u, l, r)
        mid = (l + r) // 2
        
        if pos <= mid:
            if self.tr[u]['l'] == 0:
                self.tr[u]['l'] = self.new_node()
            self.update(self.tr[u]['l'], l, mid, pos, val)
        else:
            if self.tr[u]['r'] == 0:
                self.tr[u]['r'] = self.new_node()
            self.update(self.tr[u]['r'], mid + 1, r, pos, val)
        
        self.pushup(u)
    
    def query(self, u, l, r, ql, qr):
        """区间查询"""
        if u == 0 or ql > r or qr < l:
            return 0
        
        if ql <= l and r <= qr:
            return self.tr[u]['sum']
        
        self.pushdown(u, l, r)
        mid = (l + r) // 2
        res = 0
        
        if ql <= mid:
            res = (res + self.query(self.tr[u]['l'], l, mid, ql, qr)) % MOD
        if qr > mid:
            res = (res + self.query(self.tr[u]['r'], mid + 1, r, ql, qr)) % MOD
        
        return res
    
    def merge(self, u, v, l, r, mulu, mulv):
        """合并两棵线段树"""
        if u == 0 and v == 0:
            return 0
        
        if u == 0:
            self.tr[v]['sum'] = self.tr[v]['sum'] * mulv % MOD
            self.tr[v]['mul'] = self.tr[v]['mul'] * mulv % MOD
            return v
        
        if v == 0:
            self.tr[u]['sum'] = self.tr[u]['sum'] * mulu % MOD
            self.tr[u]['mul'] = self.tr[u]['mul'] * mulu % MOD
            return u
        
        if l == r:
            self.tr[u]['sum'] = (self.tr[u]['sum'] * mulu + self.tr[v]['sum'] * mulv) % MOD
            return u
        
        self.pushdown(u, l, r)
        self.pushdown(v, l, r)
        mid = (l + r) // 2
        
        left_u = self.tr[u]['l']
        right_u = self.tr[u]['r']
        left_v = self.tr[v]['l']
        right_v = self.tr[v]['r']
        
        self.tr[u]['l'] = self.merge(left_u, left_v, l, mid, mulu, mulv)
        self.tr[u]['r'] = self.merge(right_u, right_v, mid + 1, r, mulu, mulv)
        
        self.pushup(u)
        return u

class Solution:
    def __init__(self):
        self.n = 0
        self.m = 0
        self.idx = 0
        self.h = []
        self.e = []
        self.ne = []
        self.fa = []
        self.depth = []
        self.constraints = []
        self.LOG = 20
        self.seg = None
        self.root = []
    
    def add_edge(self, a, b):
        """添加边"""
        self.e[self.idx] = b
        self.ne[self.idx] = self.h[a]
        self.h[a] = self.idx
        self.idx += 1
    
    def dfs_lca(self, u, father):
        """DFS预处理LCA信息"""
        self.depth[u] = self.depth[father] + 1
        self.fa[u][0] = father
        
        for i in range(1, self.LOG):
            self.fa[u][i] = self.fa[self.fa[u][i-1]][i-1]
        
        i = self.h[u]
        while i != -1:
            j = self.e[i]
            if j != father:
                self.dfs_lca(j, u)
            i = self.ne[i]
    
    def lca(self, a, b):
        """求最近公共祖先"""
        if self.depth[a] < self.depth[b]:
            a, b = b, a
        
        # 将a提升到与b同一深度
        for i in range(self.LOG-1, -1, -1):
            if self.depth[self.fa[a][i]] >= self.depth[b]:
                a = self.fa[a][i]
        
        if a == b:
            return a
        
        # 同时提升a和b
        for i in range(self.LOG-1, -1, -1):
            if self.fa[a][i] != self.fa[b][i]:
                a = self.fa[a][i]
                b = self.fa[b][i]
        
        return self.fa[a][0]
    
    def dfs_dp(self, u, father):
        """树形DP主过程"""
        # 初始化当前节点的线段树
        self.root[u] = self.seg.new_node()
        self.seg.update(self.root[u], 0, self.n, 0, 1)
        
        # 处理当前节点的限制条件，找到最深的要求
        maxd = 0
        for d in self.constraints[u]:
            maxd = max(maxd, d)
        
        # 遍历所有子节点
        i = self.h[u]
        while i != -1:
            j = self.e[i]
            if j != father:
                self.dfs_dp(j, u)
                
                # 计算子树的贡献
                sum_val = self.seg.query(self.root[j], 0, self.n, 0, self.depth[u])
                
                # 合并线段树
                self.root[u] = self.seg.merge(
                    self.root[u], self.root[j], 0, self.n, 
                    sum_val, (MOD + 1 - sum_val) % MOD
                )
            i = self.ne[i]
        
        # 处理限制条件：清除不满足的状态
        if maxd > 0:
            self.seg.update(self.root[u], 0, self.n, maxd, 0)
    
    def solve(self):
        """主求解函数"""
        data = sys.stdin.read().split()
        it = iter(data)
        
        self.n = int(next(it))
        
        # 初始化图结构
        self.h = [-1] * (self.n + 5)
        self.e = [0] * (2 * self.n + 10)
        self.ne = [0] * (2 * self.n + 10)
        self.idx = 0
        
        # 建树
        for _ in range(self.n - 1):
            u = int(next(it))
            v = int(next(it))
            self.add_edge(u, v)
            self.add_edge(v, u)
        
        # 初始化LCA相关数组
        self.fa = [[0] * self.LOG for _ in range(self.n + 5)]
        self.depth = [0] * (self.n + 5)
        self.constraints = [[] for _ in range(self.n + 5)]
        
        # 预处理LCA
        self.dfs_lca(1, 0)
        
        # 处理限制条件
        self.m = int(next(it))
        for _ in range(self.m):
            u = int(next(it))
            v = int(next(it))
            w = self.lca(u, v)
            self.constraints[u].append(self.depth[w])
            self.constraints[v].append(self.depth[w])
        
        # 初始化线段树
        self.seg = SegmentTree(self.n)
        self.root = [0] * (self.n + 5)
        
        # 执行树形DP
        self.dfs_dp(1, 0)
        
        # 输出结果
        ans = self.seg.query(self.root[1], 0, self.n, 0, self.depth[1])
        print(ans)

if __name__ == "__main__":
    solution = Solution()
    solution.solve()

"""
类似题目推荐：
1. P5494 【模板】线段树合并 - 线段树合并模板题
2. CF911G Mass Change Queries - 区间赋值问题  
3. P4556 [Vani有约会]雨天的尾巴 - 树上差分+线段树合并
4. P3224 [HNOI2012]永无乡 - 并查集+线段树合并
5. P5298 [PKUWC2018]Minimax - 概率DP+线段树合并

解题技巧总结：
1. 线段树合并常用于优化树形DP，特别是需要维护子树信息的情况
2. 注意线段树合并的时间复杂度是O(n log n)，但需要合理分配内存
3. 对于限制条件，通常转化为对深度的要求
4. 使用懒标记优化区间乘操作
5. Python中需要注意递归深度限制和内存分配
"""
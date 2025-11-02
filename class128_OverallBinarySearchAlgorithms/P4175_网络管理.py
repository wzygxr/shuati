# P4175 [CTSC2008]网络管理 - Python实现
# 题目来源：https://www.luogu.com.cn/problem/P4175
# 时间复杂度：O((N+Q) * logN * log(maxValue))
# 空间复杂度：O(N + Q)

class NetworkManagement:
    def __init__(self):
        self.MAXN = 80001
        self.MAXQ = 80001
        
        self.n = 0
        self.q = 0
        
        # 树结构
        self.head = [0] * self.MAXN
        self.next = [0] * (self.MAXN << 1)
        self.to = [0] * (self.MAXN << 1)
        self.cnt = 0
        
        # 树链剖分
        self.fa = [0] * self.MAXN
        self.depth = [0] * self.MAXN
        self.siz = [0] * self.MAXN
        self.son = [0] * self.MAXN
        self.top = [0] * self.MAXN
        self.dfn = [0] * self.MAXN
        self.rnk = [0] * self.MAXN
        self.dfc = 0
        
        # 节点权值
        self.val = [0] * self.MAXN
        
        # 树状数组
        self.tree = [0] * self.MAXN
        
        # 操作信息
        self.op = [0] * self.MAXQ  # 0:修改 1:查询
        self.x = [0] * self.MAXQ
        self.y = [0] * self.MAXQ
        self.k = [0] * self.MAXQ
        self.qid = [0] * self.MAXQ
        
        # 整体二分
        self.lset = [0] * self.MAXQ
        self.rset = [0] * self.MAXQ
        self.ans = [0] * self.MAXQ
        
        # 离散化
        self.sorted = [0] * (self.MAXN + self.MAXQ)
        self.cntv = 0
    
    def addEdge(self, u, v):
        self.cnt += 1
        self.next[self.cnt] = self.head[u]
        self.to[self.cnt] = v
        self.head[u] = self.cnt
    
    def dfs1(self, u, f):
        """第一次DFS：计算深度、父节点、子树大小、重儿子"""
        self.fa[u] = f
        self.depth[u] = self.depth[f] + 1
        self.siz[u] = 1
        
        i = self.head[u]
        while i:
            v = self.to[i]
            if v != f:
                self.dfs1(v, u)
                self.siz[u] += self.siz[v]
                if self.siz[self.son[u]] < self.siz[v]:
                    self.son[u] = v
            i = self.next[i]
    
    def dfs2(self, u, t):
        """第二次DFS：计算dfn序、重链顶点"""
        self.top[u] = t
        self.dfc += 1
        self.dfn[u] = self.dfc
        self.rnk[self.dfc] = u
        
        if self.son[u]:
            self.dfs2(self.son[u], t)
        
        i = self.head[u]
        while i:
            v = self.to[i]
            if v != self.fa[u] and v != self.son[u]:
                self.dfs2(v, v)
            i = self.next[i]
    
    def lowbit(self, i):
        return i & -i
    
    def add(self, i, v):
        while i <= self.n:
            self.tree[i] += v
            i += self.lowbit(i)
    
    def sum(self, i):
        ret = 0
        while i > 0:
            ret += self.tree[i]
            i -= self.lowbit(i)
        return ret
    
    def queryPath(self, u, v):
        """树链剖分查询路径上点的个数"""
        ret = 0
        while self.top[u] != self.top[v]:
            if self.depth[self.top[u]] < self.depth[self.top[v]]:
                u, v = v, u
            ret += self.sum(self.dfn[u]) - self.sum(self.dfn[self.top[u]] - 1)
            u = self.fa[self.top[u]]
        
        if self.depth[u] > self.depth[v]:
            u, v = v, u
        ret += self.sum(self.dfn[v]) - self.sum(self.dfn[u] - 1)
        return ret
    
    def addPath(self, u, v, val):
        """树链剖分修改路径上的点"""
        while self.top[u] != self.top[v]:
            if self.depth[self.top[u]] < self.depth[self.top[v]]:
                u, v = v, u
            self.add(self.dfn[self.top[u]], val)
            self.add(self.dfn[u] + 1, -val)
            u = self.fa[self.top[u]]
        
        if self.depth[u] > self.depth[v]:
            u, v = v, u
        self.add(self.dfn[u], val)
        self.add(self.dfn[v] + 1, -val)
    
    def compute(self, ql, qr, vl, vr):
        """整体二分核心函数"""
        # 递归边界
        if ql > qr:
            return
        
        # 如果值域范围只有一个值，说明找到了答案
        if vl == vr:
            for i in range(ql, qr + 1):
                if self.op[self.qid[i]] == 1:
                    self.ans[self.qid[i]] = vl
            return
        
        # 二分中点
        mid = (vl + vr) >> 1
        
        # 将值域小于等于mid的数加入树状数组
        for i in range(1, self.n + 1):
            if self.val[i] <= self.sorted[mid]:
                self.addPath(i, i, 1)
        
        for i in range(1, self.q + 1):
            if self.op[i] == 0 and self.y[i] <= self.sorted[mid]:
                self.addPath(self.x[i], self.x[i], 1)
        
        # 检查每个查询，根据满足条件的元素个数划分到左右区间
        lsiz = 0
        rsiz = 0
        for i in range(ql, qr + 1):
            id = self.qid[i]
            if self.op[id] == 1:
                # 查询操作
                satisfy = self.queryPath(self.x[id], self.y[id])
                if satisfy >= self.k[id]:
                    # 说明第k大的数在左半部分
                    lsiz += 1
                    self.lset[lsiz] = id
                else:
                    # 说明第k大的数在右半部分，需要在右半部分找第(k-satisfy)大的数
                    self.k[id] -= satisfy
                    rsiz += 1
                    self.rset[rsiz] = id
            else:
                # 修改操作
                if self.y[id] <= self.sorted[mid]:
                    lsiz += 1
                    self.lset[lsiz] = id
                else:
                    rsiz += 1
                    self.rset[rsiz] = id
        
        # 将操作分组
        for i in range(1, lsiz + 1):
            self.qid[ql + i - 1] = self.lset[i]
        for i in range(1, rsiz + 1):
            self.qid[ql + lsiz + i - 1] = self.rset[i]
        
        # 清空树状数组
        for i in range(1, self.n + 1):
            if self.val[i] <= self.sorted[mid]:
                self.addPath(i, i, -1)
        
        for i in range(1, self.q + 1):
            if self.op[i] == 0 and self.y[i] <= self.sorted[mid]:
                self.addPath(self.x[i], self.x[i], -1)
        
        # 递归处理左右区间
        self.compute(ql, ql + lsiz - 1, vl, mid)
        self.compute(ql + lsiz, qr, mid + 1, vr)
    
    def solve(self):
        import sys
        input = sys.stdin.read
        data = input().split()
        
        idx = 0
        self.n = int(data[idx])
        idx += 1
        self.q = int(data[idx])
        idx += 1
        
        for i in range(1, self.n + 1):
            self.val[i] = int(data[idx])
            idx += 1
            self.sorted[self.cntv + 1] = self.val[i]
            self.cntv += 1
        
        # 建树
        for i in range(1, self.n):
            u = int(data[idx])
            idx += 1
            v = int(data[idx])
            idx += 1
            self.addEdge(u, v)
            self.addEdge(v, u)
        
        # 处理操作
        for i in range(1, self.q + 1):
            self.op[i] = int(data[idx])
            idx += 1
            self.x[i] = int(data[idx])
            idx += 1
            self.y[i] = int(data[idx])
            idx += 1
            
            if self.op[i] == 0:
                # 修改操作
                self.sorted[self.cntv + 1] = self.y[i]
                self.cntv += 1
            else:
                # 查询操作
                self.k[i] = self.op[i]
                self.op[i] = 1
                self.qid[i] = i
        
        # 离散化
        self.sorted = self.sorted[:self.cntv + 1]
        self.sorted.sort()
        # 去重
        unique_sorted = []
        for i in range(len(self.sorted)):
            if not unique_sorted or unique_sorted[-1] != self.sorted[i]:
                unique_sorted.append(self.sorted[i])
        self.sorted = unique_sorted
        self.cntv = len(self.sorted) - 1
        
        # 树链剖分预处理
        self.dfs1(1, 0)
        self.dfs2(1, 1)
        
        # 整体二分求解
        self.compute(1, self.q, 1, self.cntv)
        
        # 输出结果
        results = []
        for i in range(1, self.q + 1):
            if self.ans[i] != 0:
                results.append(str(self.sorted[self.ans[i]]))
            else:
                results.append("invalid request!")
        return results

# 主函数
"""
if __name__ == "__main__":
    solver = NetworkManagement()
    results = solver.solve()
    for result in results:
        print(result)
"""
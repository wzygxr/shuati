import sys

# Codeforces 165D Beard Graph，Python版
# 题目来源：Codeforces 165D Beard Graph
# 题目链接：https://codeforces.com/problemset/problem/165/D
#
# 题目描述：
# 给定一棵n个节点的树，节点编号从1到n。
# 初始时树上所有边都是白色的。
# 现在有三种操作：
# 1. 0 i : 将第i条边的颜色翻转（白变黑，黑变白）
# 2. 1 a b : 询问从节点a到节点b的路径上是否存在白色的边，如果存在则输出1，否则输出0
# 3. 2 a b : 询问从节点a到节点b的路径上有多少条白色边
#
# 解题思路：
# 使用树链剖分将树上问题转化为线段树问题
# 1. 树链剖分：通过两次DFS将树划分为多条重链
# 2. 边权转点权：将每条边的权值下放到深度更深的节点上
# 3. 线段树：维护区间和与区间是否存在白色边
# 4. 路径操作：将树上路径操作转化为多个区间操作
#
# 算法步骤：
# 1. 构建树结构，进行树链剖分（dfs1计算重儿子，dfs2计算dfn序）
# 2. 将边权转移到节点上（每条边的权值赋给深度更深的节点）
# 3. 使用线段树维护每个区间的白色边数量和是否存在白色边
# 4. 对于翻转操作：更新对应节点的边颜色状态
# 5. 对于查询操作：计算路径上的白色边数量或是否存在白色边
#
# 时间复杂度分析：
# - 树链剖分预处理：O(n)
# - 每次操作：O(log²n)
# - 总体复杂度：O(m log²n)
# 空间复杂度：O(n)
#
# 是否为最优解：
# 是的，树链剖分是解决此类树上路径操作问题的经典方法，
# 时间复杂度已经达到了理论下限，是最优解之一。
#
# 相关题目链接：
# 1. Codeforces 165D Beard Graph（本题）：https://codeforces.com/problemset/problem/165/D
# 2. 洛谷P2146 [NOI2015]软件包管理器：https://www.luogu.com.cn/problem/P2146
# 3. 洛谷P2486 [SDOI2011]染色：https://www.luogu.com.cn/problem/P2486
# 4. Codeforces 916E Jamie and Tree：https://codeforces.com/problemset/problem/916/E
# 5. HackerEarth Tree Queries：https://www.hackerearth.com/practice/algorithms/graphs/tree-graphs/practice-problems/approximate/tree-query/
#
# Java实现参考：Code_CF165D_BeardGraph.java
# Python实现参考：Codeforces165D_BeardGraph.py（当前文件）
# C++实现参考：Code_CF165D_BeardGraph.cpp

class SegmentTree:
    """线段树类，用于区间修改和区间查询"""
    
    def __init__(self, n):
        self.n = n
        self.sum = [0] * (4 * n)      # 白色边的数量
        self.has_white = [False] * (4 * n)  # 是否存在白色边
    
    def up(self, i):
        """向上更新"""
        self.sum[i] = self.sum[i << 1] + self.sum[i << 1 | 1]
        self.has_white[i] = self.has_white[i << 1] or self.has_white[i << 1 | 1]
    
    def update(self, jobx, jobv, l, r, i):
        """单点更新"""
        if l == r:
            self.sum[i] = jobv
            self.has_white[i] = (jobv > 0)
            return
        mid = (l + r) >> 1
        if jobx <= mid:
            self.update(jobx, jobv, l, mid, i << 1)
        else:
            self.update(jobx, jobv, mid + 1, r, i << 1 | 1)
        self.up(i)
    
    def query_sum(self, jobl, jobr, l, r, i):
        """区间查询和"""
        if jobl <= l and r <= jobr:
            return self.sum[i]
        mid = (l + r) >> 1
        ans = 0
        if jobl <= mid:
            ans += self.query_sum(jobl, jobr, l, mid, i << 1)
        if jobr > mid:
            ans += self.query_sum(jobl, jobr, mid + 1, r, i << 1 | 1)
        return ans
    
    def query_has_white(self, jobl, jobr, l, r, i):
        """区间查询是否存在白色边"""
        if jobl <= l and r <= jobr:
            return self.has_white[i]
        mid = (l + r) >> 1
        ans = False
        if jobl <= mid:
            ans = ans or self.query_has_white(jobl, jobr, l, mid, i << 1)
        if jobr > mid:
            ans = ans or self.query_has_white(jobl, jobr, mid + 1, r, i << 1 | 1)
        return ans


class HLD_BeardGraph:
    """树链剖分Beard Graph"""
    
    def __init__(self, n):
        self.n = n
        
        # 图的邻接表表示
        self.head = [0] * (n + 1)
        self.next_edge = [0] * (2 * n + 1)
        self.to_edge = [0] * (2 * n + 1)
        self.edge_id = [0] * (2 * n + 1)  # 边的编号
        self.cnt_edge = 0
        
        # 树链剖分相关数组
        self.fa = [0] * (n + 1)       # 父节点
        self.dep = [0] * (n + 1)      # 深度
        self.siz = [0] * (n + 1)      # 子树大小
        self.son = [0] * (n + 1)      # 重儿子
        self.top = [0] * (n + 1)      # 所在重链的顶部节点
        self.dfn = [0] * (n + 1)      # dfs序
        self.rnk = [0] * (n + 1)      # dfs序对应的节点
        self.cnt_dfn = 0              # dfs序计数器
        
        # 边的颜色状态：1表示白色，0表示黑色
        self.edge_color = [1] * (n + 1)
        
        # 线段树
        self.seg_tree = SegmentTree(n)
        
        # 边到节点的映射
        self.edge_to_node = [0] * (n + 1)
    
    def add_edge(self, u, v, id):
        """添加边"""
        self.cnt_edge += 1
        self.next_edge[self.cnt_edge] = self.head[u]
        self.to_edge[self.cnt_edge] = v
        self.edge_id[self.cnt_edge] = id
        self.head[u] = self.cnt_edge
    
    def dfs1(self, u, f):
        """第一次dfs，计算树链剖分所需信息"""
        self.fa[u] = f
        self.dep[u] = self.dep[f] + 1
        self.siz[u] = 1
        self.son[u] = 0
        
        e = self.head[u]
        while e:
            v = self.to_edge[e]
            if v != f:
                self.dfs1(v, u)
                self.siz[u] += self.siz[v]
                if self.son[u] == 0 or self.siz[self.son[u]] < self.siz[v]:
                    self.son[u] = v
            e = self.next_edge[e]
    
    def dfs2(self, u, t):
        """第二次dfs，计算重链剖分"""
        self.top[u] = t
        self.cnt_dfn += 1
        self.dfn[u] = self.cnt_dfn
        self.rnk[self.cnt_dfn] = u
        
        if self.son[u] == 0:
            return
        self.dfs2(self.son[u], t)
        
        e = self.head[u]
        while e:
            v = self.to_edge[e]
            if v != self.fa[u] and v != self.son[u]:
                self.dfs2(v, v)
            e = self.next_edge[e]
    
    def flip_edge(self, edge_id):
        """翻转边的颜色"""
        self.edge_color[edge_id] = 1 - self.edge_color[edge_id]
        # 更新线段树中对应节点的值
        node = self.edge_to_node[edge_id]
        self.seg_tree.update(self.dfn[node], self.edge_color[edge_id], 1, self.n, 1)
    
    def path_has_white(self, x, y):
        """查询路径上是否存在白色边"""
        ans = False
        while self.top[x] != self.top[y]:
            if self.dep[self.top[x]] < self.dep[self.top[y]]:
                x, y = y, x
            ans = ans or self.seg_tree.query_has_white(self.dfn[self.top[x]], self.dfn[x], 1, self.n, 1)
            x = self.fa[self.top[x]]
        
        if self.dep[x] > self.dep[y]:
            x, y = y, x
        if x != y:  # 排除LCA节点本身
            ans = ans or self.seg_tree.query_has_white(self.dfn[x] + 1, self.dfn[y], 1, self.n, 1)
        return ans
    
    def path_white_count(self, x, y):
        """查询路径上白色边的数量"""
        ans = 0
        while self.top[x] != self.top[y]:
            if self.dep[self.top[x]] < self.dep[self.top[y]]:
                x, y = y, x
            ans += self.seg_tree.query_sum(self.dfn[self.top[x]], self.dfn[x], 1, self.n, 1)
            x = self.fa[self.top[x]]
        
        if self.dep[x] > self.dep[y]:
            x, y = y, x
        if x != y:  # 排除LCA节点本身
            ans += self.seg_tree.query_sum(self.dfn[x] + 1, self.dfn[y], 1, self.n, 1)
        return ans


def main():
    n = int(sys.stdin.readline())
    
    # 创建HLD对象
    hld = HLD_BeardGraph(n)
    
    # 读取边信息
    edges = {}
    for i in range(1, n):
        line = sys.stdin.readline().split()
        u, v = int(line[0]), int(line[1])
        edges[i] = (u, v)
        hld.add_edge(u, v, i)
        hld.add_edge(v, u, i)
    
    # 树链剖分，以节点1为根
    hld.dfs1(1, 0)
    hld.dfs2(1, 1)
    
    # 建立边到节点的映射（将边权转移到深度更深的节点上）
    for i in range(1, n):
        u, v = edges[i]
        node = u if hld.dep[u] > hld.dep[v] else v
        hld.edge_to_node[i] = node
    
    # 初始化线段树中的边权值
    for i in range(1, n):
        hld.seg_tree.update(hld.dfn[hld.edge_to_node[i]], 1, 1, n, 1)
    
    m = int(sys.stdin.readline())
    for _ in range(m):
        line = sys.stdin.readline().split()
        op = int(line[0])
        
        if op == 0:
            edge_id = int(line[1])
            # 翻转边的颜色
            hld.flip_edge(edge_id)
        elif op == 1:
            a, b = int(line[1]), int(line[2])
            print(1 if hld.path_has_white(a, b) else 0)
        else:  # op == 2
            a, b = int(line[1]), int(line[2])
            print(hld.path_white_count(a, b))


if __name__ == "__main__":
    main()
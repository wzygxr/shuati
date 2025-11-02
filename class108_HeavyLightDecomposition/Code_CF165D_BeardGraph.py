# Codeforces 165D Beard Graph
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
# Python实现参考：Code_CF165D_BeardGraph.py（当前文件）
# C++实现参考：Code_CF165D_BeardGraph.cpp

import sys
from collections import defaultdict

class SegmentTree:
    """线段树类，用于维护区间和与区间是否存在白色边"""
    
    def __init__(self, n):
        self.n = n
        self.sum = [0] * (4 * n)      # 白色边的数量
        self.has_white = [False] * (4 * n)  # 是否存在白色边
    
    def push_up(self, rt):
        """向上更新"""
        self.sum[rt] = self.sum[rt << 1] + self.sum[rt << 1 | 1]
        self.has_white[rt] = self.has_white[rt << 1] or self.has_white[rt << 1 | 1]
    
    def build(self, l, r, rt):
        """构建线段树"""
        if l == r:
            # 叶子节点不需要特殊处理，初始值为0
            return
        mid = (l + r) >> 1
        self.build(l, mid, rt << 1)
        self.build(mid + 1, r, rt << 1 | 1)
        self.push_up(rt)
    
    def update(self, pos, val, l, r, rt):
        """单点更新"""
        if l == r:
            self.sum[rt] = val
            self.has_white[rt] = (val > 0)
            return
        mid = (l + r) >> 1
        if pos <= mid:
            self.update(pos, val, l, mid, rt << 1)
        else:
            self.update(pos, val, mid + 1, r, rt << 1 | 1)
        self.push_up(rt)
    
    def query_sum(self, L, R, l, r, rt):
        """区间查询和"""
        if L <= l and r <= R:
            return self.sum[rt]
        mid = (l + r) >> 1
        ans = 0
        if L <= mid:
            ans += self.query_sum(L, R, l, mid, rt << 1)
        if R > mid:
            ans += self.query_sum(L, R, mid + 1, r, rt << 1 | 1)
        return ans
    
    def query_has_white(self, L, R, l, r, rt):
        """区间查询是否存在白色边"""
        if L <= l and r <= R:
            return self.has_white[rt]
        mid = (l + r) >> 1
        ans = False
        if L <= mid:
            ans = ans or self.query_has_white(L, R, l, mid, rt << 1)
        if R > mid:
            ans = ans or self.query_has_white(L, R, mid + 1, r, rt << 1 | 1)
        return ans


class BeardGraph:
    """Beard Graph类"""
    
    def __init__(self, n):
        self.n = n
        
        # 图的邻接表表示
        self.graph = defaultdict(list)
        
        # 树链剖分相关数组
        self.fa = [0] * (n + 1)       # 父节点
        self.dep = [0] * (n + 1)      # 深度
        self.siz = [0] * (n + 1)      # 子树大小
        self.son = [0] * (n + 1)      # 重儿子
        self.top = [0] * (n + 1)      # 所在重链的顶部节点
        self.dfn = [0] * (n + 1)      # dfs序
        self.rnk = [0] * (n + 1)      # dfs序对应的节点
        self.time = 0                 # dfs时间戳
        
        # 边的颜色状态：1表示白色，0表示黑色
        self.edge_color = [0] * (n + 1)
        
        # 边到节点的映射（将边权转移到深度更深的节点上）
        self.edge_to_node = [0] * (n + 1)
        
        # 线段树
        self.seg_tree = SegmentTree(n)  # 初始化为SegmentTree对象
    
    def add_edge(self, u, v, edge_id):
        """添加边"""
        self.graph[u].append((v, edge_id))
        self.graph[v].append((u, edge_id))
    
    def dfs1(self, u, father):
        """第一次dfs，计算深度、父节点、子树大小、重儿子"""
        self.fa[u] = father
        self.dep[u] = self.dep[father] + 1
        self.siz[u] = 1
        
        for v, edge_id in self.graph[u]:
            if v != father:
                self.dfs1(v, u)
                self.siz[u] += self.siz[v]
                # 更新重儿子
                if self.son[u] == 0 or self.siz[v] > self.siz[self.son[u]]:
                    self.son[u] = v
    
    def dfs2(self, u, tp):
        """第二次dfs，计算重链顶部节点、dfs序"""
        self.top[u] = tp
        self.dfn[u] = self.time + 1
        self.time += 1
        self.rnk[self.dfn[u]] = u
        
        if self.son[u] != 0:
            self.dfs2(self.son[u], tp)  # 优先遍历重儿子
        
        for v, edge_id in self.graph[u]:
            if v != self.fa[u] and v != self.son[u]:
                self.dfs2(v, v)  # 轻儿子作为新重链的顶部
    
    def flip_edge(self, edge_id):
        """翻转边的颜色"""
        self.edge_color[edge_id] = 1 - self.edge_color[edge_id]
        # 更新线段树中对应节点的值
        node = self.edge_to_node[edge_id]
        self.seg_tree.update(self.dfn[node], self.edge_color[edge_id], 1, self.time, 1)
    
    def path_has_white(self, x, y):
        """查询路径上是否存在白色边"""
        ans = False
        while self.top[x] != self.top[y]:
            if self.dep[self.top[x]] < self.dep[self.top[y]]:
                x, y = y, x  # 交换x,y
            ans = ans or self.seg_tree.query_has_white(self.dfn[self.top[x]], self.dfn[x], 1, self.time, 1)
            x = self.fa[self.top[x]]
        
        if self.dep[x] > self.dep[y]:
            x, y = y, x  # 交换x,y
        
        if x != y:  # 排除LCA节点本身
            ans = ans or self.seg_tree.query_has_white(self.dfn[x] + 1, self.dfn[y], 1, self.time, 1)
        
        return ans
    
    def path_white_count(self, x, y):
        """查询路径上白色边的数量"""
        ans = 0
        while self.top[x] != self.top[y]:
            if self.dep[self.top[x]] < self.dep[self.top[y]]:
                x, y = y, x  # 交换x,y
            ans += self.seg_tree.query_sum(self.dfn[self.top[x]], self.dfn[x], 1, self.time, 1)
            x = self.fa[self.top[x]]
        
        if self.dep[x] > self.dep[y]:
            x, y = y, x  # 交换x,y
        
        if x != y:  # 排除LCA节点本身
            ans += self.seg_tree.query_sum(self.dfn[x] + 1, self.dfn[y], 1, self.time, 1)
        
        return ans


def main():
    import sys
    input = sys.stdin.read
    data = input().split()
    
    idx = 0
    n = int(data[idx])
    idx += 1
    
    # 创建BeardGraph对象
    graph = BeardGraph(n)
    
    # 读入边信息
    edges = []
    for i in range(1, n):
        u = int(data[idx])
        idx += 1
        v = int(data[idx])
        idx += 1
        edges.append((u, v))
        graph.add_edge(u, v, i)
        graph.add_edge(v, u, i)
    
    # 初始化所有边为白色
    for i in range(1, n):
        graph.edge_color[i] = 1
    
    # 树链剖分，以节点1为根
    graph.dfs1(1, 0)
    graph.dfs2(1, 1)
    
    # 建立边到节点的映射（将边权转移到深度更深的节点上）
    for i in range(1, n):
        u, v = edges[i-1]
        node = u if graph.dep[u] > graph.dep[v] else v
        graph.edge_to_node[i] = node
    
    # 重新初始化线段树（因为dfs后time发生了变化）
    graph.seg_tree = SegmentTree(n)
    graph.seg_tree.build(1, n, 1)
    
    # 初始化线段树中的边权值
    for i in range(1, n):
        graph.seg_tree.update(graph.dfn[graph.edge_to_node[i]], 1, 1, n, 1)
    
    m = int(data[idx])
    idx += 1
    
    results = []
    for _ in range(m):
        op = int(data[idx])
        idx += 1
        
        if op == 0:
            edge_id = int(data[idx])
            idx += 1
            # 翻转边的颜色
            graph.flip_edge(edge_id)
        elif op == 1:
            a = int(data[idx])
            idx += 1
            b = int(data[idx])
            idx += 1
            results.append("1" if graph.path_has_white(a, b) else "0")
        else:  # op == 2
            a = int(data[idx])
            idx += 1
            b = int(data[idx])
            idx += 1
            results.append(str(graph.path_white_count(a, b)))
    
    # 输出结果
    print("\n".join(results))


if __name__ == "__main__":
    main()
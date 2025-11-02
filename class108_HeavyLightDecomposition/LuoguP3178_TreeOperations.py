import sys

# 洛谷P3178[HAOI2015]树上操作，Python版
# 题目来源：洛谷P3178 [HAOI2015]树上操作
# 题目链接：https://www.luogu.com.cn/problem/P3178
#
# 题目描述：
# 有一棵节点数为N的树，以节点1为根节点，初始时每个节点都有一个权值。
# 现在有三种操作：
# 1. 操作1：格式：1 x a，表示将节点x的权值加上a
# 2. 操作2：格式：2 x a，表示将以节点x为根的子树上的所有节点的权值加上a
# 3. 操作3：格式：3 x y，表示查询从节点x到节点y的路径上的所有节点的权值和
#
# 解题思路：
# 使用树链剖分将树上问题转化为线段树问题
# 1. 树链剖分：通过两次DFS将树划分为多条重链
# 2. 线段树：维护区间和，支持区间修改和区间查询
# 3. 路径操作：将树上路径操作转化为多个区间操作
#
# 算法步骤：
# 1. 构建树结构，进行树链剖分（dfs1计算重儿子，dfs2计算dfn序）
# 2. 使用线段树维护每个区间的权值和，支持区间加法操作
# 3. 对于单点加法操作：更新节点对应的区间
# 4. 对于子树加法操作：直接对子树对应的连续区间进行更新
# 5. 对于路径查询操作：将路径分解为多段重链进行区间查询
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
# 1. 洛谷P3178 [HAOI2015]树上操作（本题）：https://www.luogu.com.cn/problem/P3178
# 2. 洛谷P2590 [ZJOI2008]树的统计：https://www.luogu.com.cn/problem/P2590
# 3. 洛谷P3384 【模板】重链剖分/树链剖分：https://www.luogu.com.cn/problem/P3384
# 4. Codeforces 916E Jamie and Tree：https://codeforces.com/problemset/problem/916/E
# 5. HackerEarth Tree Queries：https://www.hackerearth.com/practice/algorithms/graphs/tree-graphs/practice-problems/approximate/tree-query/
#
# Java实现参考：Code03_PathMaxAndSum1.java
# Python实现参考：LuoguP3178_TreeOperations.py（当前文件）
# C++实现参考：Code03_PathMaxAndSum1.cpp

class SegmentTree:
    """线段树类，用于区间修改和区间查询"""
    
    def __init__(self, n):
        self.n = n
        self.sum = [0] * (4 * n)      # 区间和
        self.add_tag = [0] * (4 * n)  # 懒标记
    
    def up(self, i):
        """向上更新"""
        self.sum[i] = self.sum[i << 1] + self.sum[i << 1 | 1]
    
    def lazy(self, i, v, n):
        """懒标记下传"""
        self.sum[i] += v * n
        self.add_tag[i] += v
    
    def down(self, i, ln, rn):
        """下传懒标记"""
        if self.add_tag[i] != 0:
            self.lazy(i << 1, self.add_tag[i], ln)
            self.lazy(i << 1 | 1, self.add_tag[i], rn)
            self.add_tag[i] = 0
    
    def build(self, val, rnk, l, r, i):
        """构建线段树"""
        if l == r:
            self.sum[i] = val[rnk[l]]
            return
        mid = (l + r) >> 1
        self.build(val, rnk, l, mid, i << 1)
        self.build(val, rnk, mid + 1, r, i << 1 | 1)
        self.up(i)
    
    def add(self, jobl, jobr, jobv, l, r, i):
        """区间加法"""
        if jobl <= l and r <= jobr:
            self.lazy(i, jobv, r - l + 1)
            return
        mid = (l + r) >> 1
        self.down(i, mid - l + 1, r - mid)
        if jobl <= mid:
            self.add(jobl, jobr, jobv, l, mid, i << 1)
        if jobr > mid:
            self.add(jobl, jobr, jobv, mid + 1, r, i << 1 | 1)
        self.up(i)
    
    def query(self, jobl, jobr, l, r, i):
        """区间查询"""
        if jobl <= l and r <= jobr:
            return self.sum[i]
        mid = (l + r) >> 1
        self.down(i, mid - l + 1, r - mid)
        ans = 0
        if jobl <= mid:
            ans += self.query(jobl, jobr, l, mid, i << 1)
        if jobr > mid:
            ans += self.query(jobl, jobr, mid + 1, r, i << 1 | 1)
        return ans


class HLD_TreeOperations:
    """树链剖分树上操作"""
    
    def __init__(self, n):
        self.n = n
        
        # 图的邻接表表示
        self.head = [0] * (n + 1)
        self.next_edge = [0] * (2 * n + 1)
        self.to_edge = [0] * (2 * n + 1)
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
        
        # 节点权值
        self.val = [0] * (n + 1)
        
        # 线段树
        self.seg_tree = SegmentTree(n)
    
    def add_edge(self, u, v):
        """添加无向边"""
        self.cnt_edge += 1
        self.next_edge[self.cnt_edge] = self.head[u]
        self.to_edge[self.cnt_edge] = v
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
    
    def node_add(self, x, v):
        """单点加法：将节点x的权值加上v"""
        self.seg_tree.add(self.dfn[x], self.dfn[x], v, 1, self.n, 1)
    
    def subtree_add(self, x, v):
        """子树加法：将以节点x为根的子树上的所有节点的权值加上v"""
        self.seg_tree.add(self.dfn[x], self.dfn[x] + self.siz[x] - 1, v, 1, self.n, 1)
    
    def path_sum(self, x, y):
        """路径查询：查询从节点x到节点y的路径上的所有节点的权值和"""
        ans = 0
        while self.top[x] != self.top[y]:
            if self.dep[self.top[x]] < self.dep[self.top[y]]:
                x, y = y, x
            ans += self.seg_tree.query(self.dfn[self.top[x]], self.dfn[x], 1, self.n, 1)
            x = self.fa[self.top[x]]
        
        if self.dep[x] > self.dep[y]:
            x, y = y, x
        ans += self.seg_tree.query(self.dfn[x], self.dfn[y], 1, self.n, 1)
        return ans


def main():
    line = sys.stdin.readline().split()
    n, m = int(line[0]), int(line[1])
    
    # 创建HLD对象
    hld = HLD_TreeOperations(n)
    
    # 读取节点初始权值
    vals = list(map(int, sys.stdin.readline().split()))
    for i in range(1, n + 1):
        hld.val[i] = vals[i - 1]
    
    # 读取边信息
    for _ in range(n - 1):
        line = sys.stdin.readline().split()
        u, v = int(line[0]), int(line[1])
        hld.add_edge(u, v)
        hld.add_edge(v, u)
    
    # 树链剖分
    hld.dfs1(1, 0)
    hld.dfs2(1, 1)
    
    # 构建线段树
    hld.seg_tree.build(hld.val, hld.rnk, 1, n, 1)
    
    # 处理操作
    for _ in range(m):
        line = sys.stdin.readline().split()
        op = int(line[0])
        
        if op == 1:
            x = int(line[1])
            a = int(line[2])
            hld.node_add(x, a)
        elif op == 2:
            x = int(line[1])
            a = int(line[2])
            hld.subtree_add(x, a)
        else:  # op == 3
            x = int(line[1])
            y = int(line[2])
            print(hld.path_sum(x, y))


if __name__ == "__main__":
    main()
# -*- coding: utf-8 -*-
"""
SPOJ COT - Count on a tree

题目描述:
给定一棵N个节点的树，每个节点有一个权值。进行M次查询，每次查询两点间路径上第K小的点权。

解题思路:
使用树上主席树解决树上路径第K小问题。
1. 对所有节点权值进行离散化处理
2. 通过DFS序确定树的结构，计算每个节点的深度和父节点
3. 预处理倍增数组用于计算LCA(最近公共祖先)
4. 对每个节点建立主席树，表示从根到该节点路径上的信息
5. 查询时利用前缀和思想和LCA，通过root[u]+root[v]-root[lca]-root[fa[lca]]得到路径信息
6. 在线段树上二分查找第K小的数

时间复杂度: O((n + m) log n)
空间复杂度: O(n log n)
"""

class PersistentSegmentTreeOnTree:
    """树上可持久化线段树实现"""
    
    def __init__(self, n):
        """
        初始化树上可持久化线段树
        :param n: 节点数
        """
        self.n = n
        # 树的邻接表表示
        self.graph = [[] for _ in range(n + 1)]
        # 节点权值
        self.weight = [0] * (n + 1)
        # 离散化后的权值
        self.sorted_weights = []
        # 节点深度
        self.depth = [0] * (n + 1)
        # 节点父节点
        self.parent = [0] * (n + 1)
        # 倍增数组用于LCA
        self.fa = [[-1] * 20 for _ in range(n + 1)]
        
        # 每个节点的主席树根节点
        self.root = [0] * (n + 1)
        
        # 线段树节点信息
        self.left = [0] * (n * 20)
        self.right = [0] * (n * 20)
        self.sum = [0] * (n * 20)
        
        # 线段树节点计数器
        self.cnt = 0
        
        # DFS序相关
        self.timestamp = 0
        self.dfn = [0] * (n + 1)
        self.rev = [0] * (n + 1)
    
    def build(self, l, r):
        """
        构建空线段树
        :param l: 区间左端点
        :param r: 区间右端点
        :return: 根节点编号
        """
        self.cnt += 1
        rt = self.cnt
        self.sum[rt] = 0
        if l < r:
            mid = (l + r) // 2
            self.left[rt] = self.build(l, mid)
            self.right[rt] = self.build(mid + 1, r)
        return rt
    
    def insert(self, pos, l, r, pre):
        """
        在线段树中插入一个值
        :param pos: 要插入的值（离散化后的坐标）
        :param l: 区间左端点
        :param r: 区间右端点
        :param pre: 前一个版本的节点编号
        :return: 新节点编号
        """
        self.cnt += 1
        rt = self.cnt
        self.left[rt] = self.left[pre]
        self.right[rt] = self.right[pre]
        self.sum[rt] = self.sum[pre] + 1
        
        if l < r:
            mid = (l + r) // 2
            if pos <= mid:
                self.left[rt] = self.insert(pos, l, mid, self.left[rt])
            else:
                self.right[rt] = self.insert(pos, mid + 1, r, self.right[rt])
        return rt
    
    def query(self, k, l, r, u, v, lca, flca):
        """
        查询树上路径第k小的数
        :param k: 第k小
        :param l: 区间左端点
        :param r: 区间右端点
        :param u: 节点u的根
        :param v: 节点v的根
        :param lca: LCA节点的根
        :param flca: LCA父节点的根
        :return: 第k小的数在离散化数组中的位置
        """
        if l >= r:
            return l
        mid = (l + r) // 2
        # 计算左子树中数的个数
        x = self.sum[self.left[u]] + self.sum[self.left[v]] - self.sum[self.left[lca]] - self.sum[self.left[flca]]
        if x >= k:
            # 第k小在左子树中
            return self.query(k, l, mid, self.left[u], self.left[v], self.left[lca], self.left[flca])
        else:
            # 第k小在右子树中
            return self.query(k - x, mid + 1, r, self.right[u], self.right[v], self.right[lca], self.right[flca])
    
    def dfs(self, u, fa, d):
        """
        DFS遍历构建主席树
        :param u: 当前节点
        :param fa: 父节点
        :param d: 深度
        """
        self.depth[u] = d
        self.parent[u] = fa
        self.timestamp += 1
        self.dfn[u] = self.timestamp
        self.rev[self.timestamp] = u
        
        # 在主席树中插入当前节点的权值
        import bisect
        pos = bisect.bisect_left(self.sorted_weights, self.weight[u]) + 1
        self.root[u] = self.insert(pos, 1, self.cnt, self.root[fa])
        
        # 递归处理子节点
        for v in self.graph[u]:
            if v != fa:
                self.dfs(v, u, d + 1)
    
    def preprocess_lca(self, n):
        """
        预处理LCA
        :param n: 节点数
        """
        # 初始化fa数组
        for i in range(1, n + 1):
            self.fa[i][0] = self.parent[i]
        
        # 倍增计算
        j = 1
        while j < 20:
            for i in range(1, n + 1):
                if self.fa[i][j - 1] != -1:
                    self.fa[i][j] = self.fa[self.fa[i][j - 1]][j - 1]
            j += 1
    
    def lca(self, u, v):
        """
        计算两个节点的LCA
        :param u: 节点u
        :param v: 节点v
        :return: LCA节点
        """
        if self.depth[u] < self.depth[v]:
            u, v = v, u
        
        # 让u和v在同一深度
        for i in range(19, -1, -1):
            if self.depth[u] - (1 << i) >= self.depth[v]:
                u = self.fa[u][i]
        
        if u == v:
            return u
        
        # 同时向上跳
        for i in range(19, -1, -1):
            if self.fa[u][i] != -1 and self.fa[u][i] != self.fa[v][i]:
                u = self.fa[u][i]
                v = self.fa[v][i]
        
        return self.parent[u]


def main():
    """主函数"""
    import sys
    import bisect
    input = sys.stdin.read
    data = input().split()
    
    n = int(data[0])
    m = int(data[1])
    
    # 初始化树上可持久化线段树
    tree_pst = PersistentSegmentTreeOnTree(n)
    
    # 读取节点权值
    idx = 2
    for i in range(1, n + 1):
        tree_pst.weight[i] = int(data[idx])
        tree_pst.sorted_weights.append(tree_pst.weight[i])
        idx += 1
    
    # 离散化处理
    tree_pst.sorted_weights.sort()
    # 去重
    unique_weights = []
    for w in tree_pst.sorted_weights:
        if not unique_weights or unique_weights[-1] != w:
            unique_weights.append(w)
    tree_pst.sorted_weights = unique_weights
    tree_pst.cnt = len(unique_weights)
    
    # 读取边信息
    for i in range(n - 1):
        u = int(data[idx])
        v = int(data[idx + 1])
        tree_pst.graph[u].append(v)
        tree_pst.graph[v].append(u)
        idx += 2
    
    # 构建主席树
    tree_pst.root[0] = tree_pst.build(1, tree_pst.cnt)
    tree_pst.dfs(1, 0, 0)
    
    # 预处理LCA
    tree_pst.preprocess_lca(n)
    
    # 处理查询
    results = []
    for i in range(m):
        u = int(data[idx])
        v = int(data[idx + 1])
        k = int(data[idx + 2])
        
        lca_node = tree_pst.lca(u, v)
        pos = tree_pst.query(k, 1, tree_pst.cnt, tree_pst.root[u], tree_pst.root[v], 
                            tree_pst.root[lca_node], tree_pst.root[tree_pst.parent[lca_node]])
        results.append(str(tree_pst.sorted_weights[pos - 1]))
        idx += 3
    
    # 输出结果
    print('\n'.join(results))


if __name__ == "__main__":
    main()
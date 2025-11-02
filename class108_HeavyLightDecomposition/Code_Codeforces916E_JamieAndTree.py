# Codeforces 916E. Jamie and Tree
# 题目链接：https://codeforces.com/problemset/problem/916E
#
# 题目描述：
# 给定一棵包含n个节点的树，每个节点有一个权值。支持以下操作：
# 1. 将根节点换为x
# 2. 将包含u和v的最小子树中每个节点权值加x
# 3. 查询以v为根的子树的总和
#
# 数据范围：
# 1 ≤ n, q ≤ 10^5
# 1 ≤ 节点权值 ≤ 10^7
#
# 解题思路：
# 1. 使用树链剖分处理换根操作
# 2. 对于换根操作，需要分类讨论当前查询节点与根节点的位置关系
# 3. 使用线段树维护区间和，支持区间加法
#
# 算法步骤：
# 1. 构建树结构，进行树链剖分预处理
# 2. 对于换根操作，记录当前根节点
# 3. 对于子树修改操作，根据当前根节点位置分类处理
# 4. 对于子树查询操作，同样需要分类处理
#
# 时间复杂度分析：
# - 树链剖分预处理：O(n)
# - 每次操作：O(log²n)
# - 总体复杂度：O(n + q log²n)
#
# 空间复杂度：O(n)
#
# 是否为最优解：
# 是的，树链剖分结合线段树是解决此类换根操作问题的经典方法。
#
# 相关题目链接：
# 1. Codeforces 916E：https://codeforces.com/problemset/problem/916E
# 2. 洛谷P3979：https://www.luogu.com.cn/problem/P3979
# 3. Codeforces 165D：https://codeforces.com/problemset/problem/165/D

import sys

class SegmentTree:
    """线段树类，支持区间加法和区间查询"""
    
    def __init__(self, n):
        self.n = n
        self.sum = [0] * (4 * n)  # 区间和
        self.add = [0] * (4 * n)  # 懒标记
    
    def up(self, i):
        """向上更新"""
        self.sum[i] = self.sum[i * 2] + self.sum[i * 2 + 1]
    
    def lazy(self, i, v, n):
        """懒标记下传"""
        self.sum[i] += v * n
        self.add[i] += v
    
    def down(self, i, ln, rn):
        """下传懒标记"""
        if self.add[i] != 0:
            self.lazy(i * 2, self.add[i], ln)
            self.lazy(i * 2 + 1, self.add[i], rn)
            self.add[i] = 0
    
    def build(self, arr, rnk, l, r, i):
        """构建线段树"""
        if l == r:
            self.sum[i] = arr[rnk[l]]
            return
        mid = (l + r) // 2
        self.build(arr, rnk, l, mid, i * 2)
        self.build(arr, rnk, mid + 1, r, i * 2 + 1)
        self.up(i)
    
    def add_range(self, ql, qr, v, l, r, i):
        """区间加法"""
        if ql <= l and r <= qr:
            self.lazy(i, v, r - l + 1)
            return
        mid = (l + r) // 2
        self.down(i, mid - l + 1, r - mid)
        if ql <= mid:
            self.add_range(ql, qr, v, l, mid, i * 2)
        if qr > mid:
            self.add_range(ql, qr, v, mid + 1, r, i * 2 + 1)
        self.up(i)
    
    def query_range(self, ql, qr, l, r, i):
        """区间查询"""
        if ql <= l and r <= qr:
            return self.sum[i]
        mid = (l + r) // 2
        self.down(i, mid - l + 1, r - mid)
        res = 0
        if ql <= mid:
            res += self.query_range(ql, qr, l, mid, i * 2)
        if qr > mid:
            res += self.query_range(ql, qr, mid + 1, r, i * 2 + 1)
        return res

class HLD:
    """树链剖分类，支持换根操作"""
    
    def __init__(self, n):
        self.n = n
        
        # 图的邻接表表示
        self.head = [0] * (n + 1)
        self.next_edge = [0] * (2 * n + 1)
        self.to_edge = [0] * (2 * n + 1)
        self.cnt_edge = 0
        
        # 树链剖分相关数组
        self.fa = [0] * (n + 1)     # 父节点
        self.dep = [0] * (n + 1)    # 深度
        self.siz = [0] * (n + 1)    # 子树大小
        self.son = [0] * (n + 1)    # 重儿子
        self.top = [0] * (n + 1)    # 所在重链顶部
        self.dfn = [0] * (n + 1)    # DFS序
        self.rnk = [0] * (n + 1)    # DFS序对应的节点
        self.cnt_dfn = 0
        
        # 节点权值
        self.val = [0] * (n + 1)
        
        # 线段树
        self.seg_tree = SegmentTree(n)
        
        # 当前根节点
        self.root = 1
    
    def add_edge(self, u, v):
        """添加边"""
        self.cnt_edge += 1
        self.next_edge[self.cnt_edge] = self.head[u]
        self.to_edge[self.cnt_edge] = v
        self.head[u] = self.cnt_edge
    
    def dfs1(self, u, father):
        """第一次DFS：计算父节点、深度、子树大小、重儿子"""
        self.fa[u] = father
        self.dep[u] = self.dep[father] + 1
        self.siz[u] = 1
        self.son[u] = 0
        
        e = self.head[u]
        while e != 0:
            v = self.to_edge[e]
            if v != father:
                self.dfs1(v, u)
                self.siz[u] += self.siz[v]
                if self.son[u] == 0 or self.siz[self.son[u]] < self.siz[v]:
                    self.son[u] = v
            e = self.next_edge[e]
    
    def dfs2(self, u, top_node):
        """第二次DFS：计算重链顶部、DFS序"""
        self.top[u] = top_node
        self.cnt_dfn += 1
        self.dfn[u] = self.cnt_dfn
        self.rnk[self.cnt_dfn] = u
        
        if self.son[u] != 0:
            self.dfs2(self.son[u], top_node)
        
        e = self.head[u]
        while e != 0:
            v = self.to_edge[e]
            if v != self.fa[u] and v != self.son[u]:
                self.dfs2(v, v)
            e = self.next_edge[e]
    
    def is_ancestor(self, u, v):
        """判断节点u是否是节点v的祖先"""
        while self.dep[v] > self.dep[u]:
            v = self.fa[v]
        return u == v
    
    def find_lca(self, u, v):
        """找到节点u和v的LCA"""
        while self.top[u] != self.top[v]:
            if self.dep[self.top[u]] < self.dep[self.top[v]]:
                u, v = v, u
            u = self.fa[self.top[u]]
        return u if self.dep[u] < self.dep[v] else v
    
    def find_ancestor_on_path(self, u, v):
        """找到节点u到根节点路径上，深度最小的节点，使得该节点是节点v的祖先"""
        lca = self.find_lca(u, v)
        if lca == v:
            return v
        if lca == u:
            return u
        
        # 从v向上跳，直到找到u的祖先
        temp = v
        while self.dep[temp] > self.dep[lca]:
            if self.is_ancestor(u, temp):
                return temp
            temp = self.fa[temp]
        return lca
    
    def subtree_add(self, u, v):
        """子树修改操作（考虑换根）"""
        if u == self.root:
            # 如果修改的是根节点的子树，就是整棵树
            self.seg_tree.add_range(1, self.cnt_dfn, v, 1, self.n, 1)
        elif self.is_ancestor(u, self.root):
            # 如果u是当前根节点的祖先
            # 需要修改整棵树，然后减去u到root路径上u的儿子节点的子树
            self.seg_tree.add_range(1, self.cnt_dfn, v, 1, self.n, 1)
            
            # 找到u到root路径上u的直接儿子
            temp = self.root
            while self.dep[temp] > self.dep[u] + 1:
                temp = self.fa[temp]
            if self.fa[temp] == u:
                # 减去这个儿子的子树
                self.seg_tree.add_range(self.dfn[temp], self.dfn[temp] + self.siz[temp] - 1, -v, 1, self.n, 1)
        else:
            # 正常情况，直接修改u的子树
            self.seg_tree.add_range(self.dfn[u], self.dfn[u] + self.siz[u] - 1, v, 1, self.n, 1)
    
    def subtree_sum(self, u):
        """子树查询操作（考虑换根）"""
        if u == self.root:
            # 如果查询的是根节点的子树，就是整棵树
            return self.seg_tree.query_range(1, self.cnt_dfn, 1, self.n, 1)
        elif self.is_ancestor(u, self.root):
            # 如果u是当前根节点的祖先
            # 需要查询整棵树，然后减去u到root路径上u的儿子节点的子树
            total = self.seg_tree.query_range(1, self.cnt_dfn, 1, self.n, 1)
            
            # 找到u到root路径上u的直接儿子
            temp = self.root
            while self.dep[temp] > self.dep[u] + 1:
                temp = self.fa[temp]
            if self.fa[temp] == u:
                # 减去这个儿子的子树
                total -= self.seg_tree.query_range(self.dfn[temp], self.dfn[temp] + self.siz[temp] - 1, 1, self.n, 1)
            return total
        else:
            # 正常情况，直接查询u的子树
            return self.seg_tree.query_range(self.dfn[u], self.dfn[u] + self.siz[u] - 1, 1, self.n, 1)
    
    def min_subtree_add(self, u, v, x):
        """包含u和v的最小子树修改"""
        lca = self.find_lca(u, v)
        
        # 找到包含u和v的最小子树的根节点
        subtree_root = lca
        if self.is_ancestor(subtree_root, self.root):
            # 如果当前根节点在子树中，需要找到真正的子树根节点
            anc1 = self.find_ancestor_on_path(u, self.root)
            anc2 = self.find_ancestor_on_path(v, self.root)
            
            if self.dep[anc1] > self.dep[anc2]:
                subtree_root = anc1
            else:
                subtree_root = anc2
        
        self.subtree_add(subtree_root, x)

def main():
    data = sys.stdin.read().split()
    idx = 0
    
    n = int(data[idx]); idx += 1
    q = int(data[idx]); idx += 1
    
    hld = HLD(n)
    
    # 读取节点权值
    for i in range(1, n + 1):
        hld.val[i] = int(data[idx]); idx += 1
    
    # 读取边信息
    for i in range(n - 1):
        u = int(data[idx]); idx += 1
        v = int(data[idx]); idx += 1
        hld.add_edge(u, v)
        hld.add_edge(v, u)
    
    # 树链剖分
    hld.dfs1(1, 0)
    hld.dfs2(1, 1)
    
    # 构建线段树
    hld.seg_tree.build(hld.val, hld.rnk, 1, n, 1)
    
    # 处理操作
    for _ in range(q):
        op = int(data[idx]); idx += 1
        
        if op == 1:
            # 换根操作
            hld.root = int(data[idx]); idx += 1
        elif op == 2:
            # 最小子树修改
            u = int(data[idx]); idx += 1
            v = int(data[idx]); idx += 1
            x = int(data[idx]); idx += 1
            hld.min_subtree_add(u, v, x)
        else:
            # 子树查询
            v = int(data[idx]); idx += 1
            print(hld.subtree_sum(v))

if __name__ == "__main__":
    main()
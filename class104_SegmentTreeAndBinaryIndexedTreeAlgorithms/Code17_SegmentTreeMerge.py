# class131/Code17_SegmentTreeMerge.py
# 洛谷 P4556 (Vani有约会) 雨天的尾巴 / 【模板】线段树合并
# 题目链接: https://www.luogu.com.cn/problem/P4556
"""
题目描述:
给定一棵树，每次操作在两个节点之间的路径上投放某种类型的救济粮，询问每个节点拥有的最多的救济粮类型。

解题思路:
使用线段树合并解决树上问题。核心思想是对树上的每个节点维护一个线段树，线段树中存储该节点各种救济粮的数量。
通过树上差分和线段树合并来高效处理操作。

算法步骤:
1. 树上差分: 对于每次在路径(u,v)上投放类型z的救济粮，我们在u和v处+1，在lca(u,v)和parent[lca(u,v)]处-1
2. DFS合并: 从叶子节点向根节点合并线段树，统计每个节点的信息
3. 线段树合并: 合并两个节点的线段树，同时维护最大值和对应的类型

时间复杂度分析:
- 时间复杂度: O(n log n + m log n)
  - LCA预处理: O(n log n)
  - 每次更新操作: O(log n)
  - 线段树合并: O(n log n)，因为每个节点最多被合并一次
- 空间复杂度: O(n log n)
  - 动态开点线段树的空间复杂度

关键优化:
1. 使用slots优化内存
2. 设置递归深度限制
3. 动态开点线段树节省空间
4. 线段树合并避免重复计算
"""

import sys
from sys import stdin
from math import log2, ceil

MAXN = 100010

# 线段树节点类
class Node:
    __slots__ = ['left', 'right', 'val', 'max_val', 'max_type']  # 使用slots优化内存
    def __init__(self):
        self.left = 0  # 左子节点索引
        self.right = 0  # 右子节点索引
        self.val = 0  # 节点值（救济粮数量）
        self.max_val = 0  # 最大值
        self.max_type = 0  # 最大值对应的救济粮类型

# 全局变量管理器
class TreeManager:
    def __init__(self, size):
        """
        初始化线段树管理器
        :param size: 预分配的节点数量
        """
        self.tree = [Node() for _ in range(size)]
        self.cnt = 1  # 当前使用的节点编号

def main():
    sys.setrecursionlimit(1 << 25)  # 设置递归深度限制
    
    n, m = map(int, stdin.readline().split())
    
    # 构建树的邻接表表示
    graph = [[] for _ in range(n + 1)]
    for _ in range(n - 1):
        u, v = map(int, stdin.readline().split())
        graph[u].append(v)
        graph[v].append(u)
    
    # LCA预处理，使用倍增法
    LOG = ceil(log2(n)) if n > 0 else 0
    up = [[0] * (n + 1) for _ in range(LOG)]  # up[k][u]表示u节点向上走2^k步到达的节点
    depth = [-1] * (n + 1)  # 节点深度
    
    def dfs_lca(u, parent):
        """
        DFS预处理LCA信息
        :param u: 当前节点
        :param parent: 父节点
        """
        depth[u] = depth[parent] + 1
        up[0][u] = parent
        
        # 倍增计算祖先节点
        for k in range(1, LOG):
            up[k][u] = up[k-1][up[k-1][u]]
        
        # 递归处理子节点
        for v in graph[u]:
            if v != parent:
                dfs_lca(v, u)
    
    dfs_lca(1, 0)  # 以节点1为根进行DFS
    
    # 获取两个节点的最近公共祖先
    def get_lca(u, v):
        """
        计算节点u和v的最近公共祖先
        :param u: 节点u
        :param v: 节点v
        :return: 最近公共祖先
        """
        # 确保u的深度不小于v
        if depth[u] < depth[v]:
            u, v = v, u
        
        # 将u提升到与v相同的深度
        for k in range(LOG-1, -1, -1):
            if depth[u] - (1 << k) >= depth[v]:
                u = up[k][u]
        
        # 如果u就是v的祖先，直接返回
        if u == v:
            return u
        
        # 同时向上提升u和v，直到它们的父节点相同
        for k in range(LOG-1, -1, -1):
            if up[k][u] != up[k][v]:
                u = up[k][u]
                v = up[k][v]
        
        return up[0][u]  # 返回最近公共祖先
    
    # 初始化线段树管理器（预估空间）
    tm = TreeManager(MAXN * 40)
    root = [0] * (n + 1)  # 每个节点对应的线段树根节点
    ans = [0] * (n + 1)  # 每个节点的答案（拥有最多的救济粮类型）
    max_type = 0  # 最大的救济粮类型编号
    
    # 线段树更新操作
    def update(node, l, r, pos, val):
        """
        更新线段树节点
        :param node: 当前线段树节点索引
        :param l: 区间左边界
        :param r: 区间右边界
        :param pos: 要更新的位置
        :param val: 更新的值
        :return: 更新后的节点索引
        """
        # 如果节点不存在，创建新节点
        if not node:
            node = tm.cnt
            tm.cnt += 1
        
        # 如果是叶子节点，直接更新值
        if l == r:
            tm.tree[node].val += val
            tm.tree[node].max_val = tm.tree[node].val
            tm.tree[node].max_type = l
            return node
        
        # 计算中点，决定更新左子树还是右子树
        mid = (l + r) >> 1
        if pos <= mid:
            tm.tree[node].left = update(tm.tree[node].left, l, mid, pos, val)
        else:
            tm.tree[node].right = update(tm.tree[node].right, mid + 1, r, pos, val)
        
        # 更新当前节点的max_val和max_type
        tm.tree[node].max_val = 0
        tm.tree[node].max_type = 0
        
        # 比较左子树的最大值
        left = tm.tree[node].left
        if left:
            left_tree = tm.tree[left]
            if left_tree.max_val > tm.tree[node].max_val or \
               (left_tree.max_val == tm.tree[node].max_val and left_tree.max_type < tm.tree[node].max_type):
                tm.tree[node].max_val = left_tree.max_val
                tm.tree[node].max_type = left_tree.max_type
        
        # 比较右子树的最大值
        right = tm.tree[node].right
        if right:
            right_tree = tm.tree[right]
            if right_tree.max_val > tm.tree[node].max_val or \
               (right_tree.max_val == tm.tree[node].max_val and right_tree.max_type < tm.tree[node].max_type):
                tm.tree[node].max_val = right_tree.max_val
                tm.tree[node].max_type = right_tree.max_type
        
        return node
    
    # 线段树合并操作
    def merge(a, b, l, r):
        """
        合并两个线段树节点
        :param a: 第一个节点索引
        :param b: 第二个节点索引
        :param l: 区间左边界
        :param r: 区间右边界
        :return: 合并后的节点索引
        """
        # 如果其中一个节点为空，返回另一个节点
        if not a:
            return b
        if not b:
            return a
        
        # 如果是叶子节点，直接合并值
        if l == r:
            tm.tree[a].val += tm.tree[b].val
            tm.tree[a].max_val = tm.tree[a].val
            tm.tree[a].max_type = l
            return a
        
        # 递归合并左右子树
        mid = (l + r) >> 1
        tm.tree[a].left = merge(tm.tree[a].left, tm.tree[b].left, l, mid)
        tm.tree[a].right = merge(tm.tree[a].right, tm.tree[b].right, mid + 1, r)
        
        # 更新当前节点的max_val和max_type
        tm.tree[a].max_val = 0
        tm.tree[a].max_type = 0
        
        # 比较左子树的最大值
        left = tm.tree[a].left
        if left:
            left_tree = tm.tree[left]
            if left_tree.max_val > tm.tree[a].max_val or \
               (left_tree.max_val == tm.tree[a].max_val and left_tree.max_type < tm.tree[a].max_type):
                tm.tree[a].max_val = left_tree.max_val
                tm.tree[a].max_type = left_tree.max_type
        
        # 比较右子树的最大值
        right = tm.tree[a].right
        if right:
            right_tree = tm.tree[right]
            if right_tree.max_val > tm.tree[a].max_val or \
               (right_tree.max_val == tm.tree[a].max_val and right_tree.max_type < tm.tree[a].max_type):
                tm.tree[a].max_val = right_tree.max_val
                tm.tree[a].max_type = right_tree.max_type
        
        return a
    
    # 处理操作
    for _ in range(m):
        x, y, z = map(int, stdin.readline().split())
        max_type = max(max_type, z)  # 更新最大类型编号
        
        lca_node = get_lca(x, y)  # 获取x和y的最近公共祖先
        parent_lca = up[0][lca_node]  # 获取最近公共祖先的父节点
        
        # 树上差分：在路径两端加1，在LCA和其父节点处减1
        root[x] = update(root[x], 1, max_type, z, 1)
        root[y] = update(root[y], 1, max_type, z, 1)
        root[lca_node] = update(root[lca_node], 1, max_type, z, -1)
        if parent_lca != 0:
            root[parent_lca] = update(root[parent_lca], 1, max_type, z, -1)
    
    # DFS合并子树并统计答案
    visited = [False] * (n + 1)
    def dfs_merge(u, parent):
        """
        DFS遍历树并合并线段树
        :param u: 当前节点
        :param parent: 父节点
        """
        # 递归处理所有子节点
        for v in graph[u]:
            if v != parent:
                dfs_merge(v, u)
                # 将子节点的线段树合并到当前节点
                root[u] = merge(root[u], root[v], 1, max_type)
        
        # 记录该节点的答案（拥有最多的救济粮类型）
        ans[u] = tm.tree[root[u]].max_type
    
    dfs_merge(1, 0)  # 从根节点开始DFS合并
    
    # 输出答案
    for i in range(1, n + 1):
        print(ans[i])

if __name__ == "__main__":
    main()

'''
复杂度分析:
- 时间复杂度: O(n log n + m log n)
  - LCA预处理: O(n log n)
  - 每次更新操作: O(log n)
  - 线段树合并: O(n log n)，因为每个节点最多被合并一次
- 空间复杂度: O(n log n)
  - 动态开点线段树的空间复杂度

Python实现注意事项:
1. Python的递归深度有限，需要设置sys.setrecursionlimit
2. 为了优化内存使用，使用了__slots__
3. 由于Python的特性，对于大规模数据可能需要进一步优化
4. 动态开点策略在Python中使用索引而非指针
'''
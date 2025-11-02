#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
持久化线段树（主席树）完整解决方案
包含所有经典题目和详细实现
时间复杂度分析：构建O(n log n)，单点更新O(log n)，区间查询O(log n)
空间复杂度分析：O(n log n)
最优解分析：对于区间第K小、历史版本查询等问题，持久化线段树是最优解之一
"""

import sys
import bisect
from typing import List, Dict, Tuple, Optional

class PersistentSegmentTree:
    """
    持久化线段树（主席树）的通用实现
    支持区间第K小查询、区间统计等操作
    """
    class Node:
        """
        线段树节点定义
        left: 左子节点索引
        right: 右子节点索引
        count: 当前区间的元素个数
        """
        def __init__(self, left=0, right=0, count=0):
            self.left = left
            self.right = right
            self.count = count
    
    def __init__(self):
        # 存储所有版本的根节点
        self.roots = []
        # 存储所有节点
        self.nodes = [self.Node()]  # 索引0为哨兵节点
    
    def push_up(self, node_idx: int) -> None:
        """
        向上更新节点的count值
        """
        node = self.nodes[node_idx]
        left_count = self.nodes[node.left].count if node.left else 0
        right_count = self.nodes[node.right].count if node.right else 0
        node.count = left_count + right_count
    
    def update(self, pre_root: int, l: int, r: int, pos: int, val: int) -> int:
        """
        单点更新，基于前驱版本创建新版本
        pre_root: 前驱版本的根节点索引
        l, r: 当前节点表示的区间
        pos: 要更新的位置
        val: 更新的值（+1或-1）
        return: 新版本的根节点索引
        """
        # 创建新节点，复制前驱版本的子节点信息
        new_node = self.Node(self.nodes[pre_root].left, self.nodes[pre_root].right, 
                            self.nodes[pre_root].count)
        self.nodes.append(new_node)
        new_root = len(self.nodes) - 1
        
        # 到达叶节点，直接更新
        if l == r:
            self.nodes[new_root].count += val
            return new_root
        
        mid = (l + r) // 2
        # 根据更新位置选择左子树或右子树
        if pos <= mid:
            # 递归更新左子树，将结果保存到新节点的左子节点
            self.nodes[new_root].left = self.update(self.nodes[pre_root].left, l, mid, pos, val)
        else:
            # 递归更新右子树，将结果保存到新节点的右子节点
            self.nodes[new_root].right = self.update(self.nodes[pre_root].right, mid + 1, r, pos, val)
        
        # 向上更新count值
        self.push_up(new_root)
        return new_root
    
    def query(self, root1: int, root2: int, l: int, r: int, k: int) -> int:
        """
        查询区间第K小的元素
        root1: 前缀版本v-1的根节点
        root2: 前缀版本v的根节点
        l, r: 当前查询区间
        k: 要查询的第K小
        """
        # 到达叶节点，直接返回位置
        if l == r:
            return l
        
        mid = (l + r) // 2
        # 计算左子树中两个版本的差值，即区间[l, mid]中的元素个数
        left_count = self.nodes[self.nodes[root2].left].count - self.nodes[self.nodes[root1].left].count
        
        # 根据左子树的元素个数决定向左还是向右查询
        if k <= left_count:
            return self.query(self.nodes[root1].left, self.nodes[root2].left, l, mid, k)
        else:
            return self.query(self.nodes[root1].right, self.nodes[root2].right, mid + 1, r, k - left_count)

# 题目1: SPOJ MKTHNUM - K-th Number
# 题目链接: https://www.spoj.com/problems/MKTHNUM/
# 题目描述: 给定一个数组，多次查询区间第K小的数
# 最优解: 持久化线段树是该问题的最优解之一

def solve_mkthnum():
    """
    解决SPOJ MKTHNUM问题 - 静态区间第K小查询
    时间复杂度: O(n log n + m log n)，其中n是数组长度，m是查询次数
    空间复杂度: O(n log n)
    """
    import sys
    input = sys.stdin.read().split()
    ptr = 0
    n = int(input[ptr])
    ptr += 1
    m = int(input[ptr])
    ptr += 1
    
    arr = list(map(int, input[ptr:ptr + n]))
    ptr += n
    
    # 离散化处理
    sorted_arr = sorted(set(arr))
    rank = {x: i + 1 for i, x in enumerate(sorted_arr)}  # 映射到[1, len(sorted_arr)]
    
    # 构建持久化线段树
    pst = PersistentSegmentTree()
    pst.roots.append(0)  # 初始空版本
    
    for i in range(n):
        # 每次插入一个元素，生成新版本
        new_root = pst.update(pst.roots[-1], 1, len(sorted_arr), rank[arr[i]], 1)
        pst.roots.append(new_root)
    
    # 处理查询
    results = []
    for _ in range(m):
        l = int(input[ptr]) - 1  # 转换为0-based
        ptr += 1
        r = int(input[ptr]) - 1
        ptr += 1
        k = int(input[ptr])
        ptr += 1
        
        # 查询区间[l+1, r+1]中的第K小（因为roots从版本0开始）
        pos = pst.query(pst.roots[l], pst.roots[r + 1], 1, len(sorted_arr), k)
        results.append(sorted_arr[pos - 1])  # 转换回原始值
    
    print('\n'.join(map(str, results)))

# 题目2: SPOJ COT - Count on a Tree
# 题目链接: https://www.spoj.com/problems/COT/
# 题目描述: 给定一棵树，多次查询两点之间路径上的第K小的数
# 最优解: 树上持久化线段树（树链剖分+主席树）是该问题的最优解

def solve_cot():
    """
    解决SPOJ COT问题 - 树上路径第K小查询
    使用DFS序和LCA（最近公共祖先）+ 主席树实现
    时间复杂度: O(n log n + m log n)
    空间复杂度: O(n log n)
    """
    import sys
    sys.setrecursionlimit(1 << 25)
    input = sys.stdin.read().split()
    ptr = 0
    n = int(input[ptr])
    ptr += 1
    m = int(input[ptr])
    ptr += 1
    
    # 读取节点值
    values = list(map(int, input[ptr:ptr + n]))
    ptr += n
    
    # 离散化处理
    sorted_vals = sorted(set(values))
    rank = {x: i + 1 for i, x in enumerate(sorted_vals)}
    
    # 构建树的邻接表
    graph = [[] for _ in range(n + 1)]  # 节点编号从1开始
    for _ in range(n - 1):
        u = int(input[ptr])
        ptr += 1
        v = int(input[ptr])
        ptr += 1
        graph[u].append(v)
        graph[v].append(u)
    
    # LCA相关变量
    LOG = 20
    depth = [0] * (n + 1)
    parent = [[0] * (n + 1) for _ in range(LOG)]
    
    # 主席树相关变量
    pst = PersistentSegmentTree()
    pst.roots = [0] * (n + 1)  # roots[u]表示从根节点到u的路径上的版本
    
    # DFS构建LCA和主席树
    def dfs(u, p):
        parent[0][u] = p
        depth[u] = depth[p] + 1
        
        # 继承父节点的线段树版本，然后更新当前节点的值
        pst.roots[u] = pst.update(pst.roots[p], 1, len(sorted_vals), rank[values[u - 1]], 1)
        
        for v in graph[u]:
            if v != p:
                dfs(v, u)
    
    # 构建LCA的倍增表
    def build_lca():
        for k in range(1, LOG):
            for i in range(1, n + 1):
                parent[k][i] = parent[k - 1][parent[k - 1][i]]
    
    # 查询LCA
    def lca(u, v):
        if depth[u] < depth[v]:
            u, v = v, u
        
        # 将u提升到与v同一深度
        for k in range(LOG - 1, -1, -1):
            if depth[u] - (1 << k) >= depth[v]:
                u = parent[k][u]
        
        if u == v:
            return u
        
        # 同时提升u和v
        for k in range(LOG - 1, -1, -1):
            if parent[k][u] != parent[k][v]:
                u = parent[k][u]
                v = parent[k][v]
        
        return parent[0][u]
    
    # 定义查询函数
    def query_kth(u, v, k):
        ancestor = lca(u, v)
        # 利用容斥原理：路径u-v = 路径root-u + 路径root-v - 2*路径root-ancestor + ancestor
        left = pst.roots[u]
        right = pst.roots[v]
        ancestor_root = pst.roots[ancestor]
        ancestor_parent_root = pst.roots[parent[0][ancestor]]
        
        def _query(a, b, c, d, l, r, k):
            if l == r:
                return l
            
            mid = (l + r) // 2
            # 计算左子树中的元素个数：(a+b - c-d)的左子树count
            a_left = pst.nodes[a].left
            b_left = pst.nodes[b].left
            c_left = pst.nodes[c].left
            d_left = pst.nodes[d].left
            
            left_count = pst.nodes[b_left].count + pst.nodes[a_left].count \
                        - pst.nodes[c_left].count - pst.nodes[d_left].count
            
            if k <= left_count:
                return _query(pst.nodes[a].left, pst.nodes[b].left, 
                            pst.nodes[c].left, pst.nodes[d].left, 
                            l, mid, k)
            else:
                return _query(pst.nodes[a].right, pst.nodes[b].right, 
                            pst.nodes[c].right, pst.nodes[d].right, 
                            mid + 1, r, k - left_count)
        
        pos = _query(left, right, ancestor_root, ancestor_parent_root, 1, len(sorted_vals), k)
        return sorted_vals[pos - 1]
    
    # 初始化
    dfs(1, 0)  # 假设根节点是1
    build_lca()
    
    # 处理查询
    results = []
    for _ in range(m):
        u = int(input[ptr])
        ptr += 1
        v = int(input[ptr])
        ptr += 1
        k = int(input[ptr])
        ptr += 1
        results.append(query_kth(u, v, k))
    
    print('\n'.join(map(str, results)))

# 题目3: LeetCode 2276 - Count Integers in Intervals
# 题目链接: https://leetcode.com/problems/count-integers-in-intervals/
# 题目描述: 实现一个数据结构，支持添加区间和查询区间内整数的个数
# 最优解: 动态开点线段树是该问题的最优解之一

class CountIntervals:
    """
    LeetCode 2276 题解 - 计数区间内的整数
    使用动态开点线段树实现
    时间复杂度: add O(log RANGE), count O(1)
    空间复杂度: O(log RANGE)
    """
    class Node:
        def __init__(self, left=None, right=None, cnt=0, cover=0):
            self.left = left      # 左子节点
            self.right = right    # 右子节点
            self.cnt = cnt        # 当前节点覆盖的整数个数
            self.cover = cover    # 是否完全覆盖
    
    def __init__(self):
        self.root = self.Node()
        self.total = 0  # 记录总数，用于O(1)查询
        self.MAX = 10**9  # 题目中的数值范围
    
    def push_up(self, node, l, r):
        """向上更新节点信息"""
        if node.cover:
            # 如果当前节点完全覆盖，则计数为区间长度
            node.cnt = r - l + 1
        elif l == r:
            # 叶子节点且未覆盖
            node.cnt = 0
        else:
            # 非叶子节点，合并左右子节点的计数
            node.cnt = (node.left.cnt if node.left else 0) + \
                      (node.right.cnt if node.right else 0)
    
    def update(self, node, l, r, ul, ur, val):
        """区间更新"""
        if ur < l or ul > r:
            # 无交集
            return
        
        if ul <= l and r <= ur:
            # 当前区间被完全包含
            node.cover += val
            self.push_up(node, l, r)
            return
        
        # 动态开点
        if not node.left:
            node.left = self.Node()
        if not node.right:
            node.right = self.Node()
        
        mid = (l + r) // 2
        self.update(node.left, l, mid, ul, ur, val)
        self.update(node.right, mid + 1, r, ul, ur, val)
        self.push_up(node, l, r)
    
    def add(self, left: int, right: int) -> None:
        """添加一个区间 [left, right]"""
        # 计算添加前的总数
        before = self.root.cnt
        # 更新区间
        self.update(self.root, 1, self.MAX, left, right, 1)
        # 更新总数
        self.total = self.root.cnt
    
    def count(self) -> int:
        """查询当前覆盖的整数个数"""
        return self.total

# 题目4: LeetCode 1970 - Smallest Missing Genetic Value in Each Subtree
# 题目链接: https://leetcode.com/problems/smallest-missing-genetic-value-in-each-subtree/
# 题目描述: 给定一棵树，每个节点有一个基因值，求每个子树中最小缺失的基因值
# 最优解: 后序遍历 + 并查集（或持久化线段树）是该问题的最优解

def smallest_missing_value_subtree(parents: List[int], nums: List[int]) -> List[int]:
    """
    LeetCode 1970 题解 - 子树中最小缺失基因值
    使用后序遍历 + 并查集优化
    时间复杂度: O(n log n)
    空间复杂度: O(n)
    """
    n = len(parents)
    res = [1] * n  # 初始化为1，因为1是最小可能的缺失值
    
    # 检查是否存在值为1的节点
    if 1 not in nums:
        return res
    
    # 构建树的邻接表（子节点列表）
    children = [[] for _ in range(n)]
    root = -1
    for i, p in enumerate(parents):
        if p == -1:
            root = i
        else:
            children[p].append(i)
    
    # 并查集结构，用于查找下一个缺失的基因值
    parent = list(range(100002))  # 基因值范围是1到100000
    
    def find(u):
        while parent[u] != u:
            parent[u] = parent[parent[u]]
            u = parent[u]
        return u
    
    # 记录每个基因值对应的节点
    pos = {v: i for i, v in enumerate(nums)}
    
    # 后序遍历树
    visited = [False] * n
    
    def dfs(node):
        visited[node] = True
        min_missing = 1
        
        # 遍历所有子节点
        for child in children[node]:
            dfs(child)
        
        # 合并当前节点的基因值
        u = find(nums[node])
        parent[u] = u + 1
        
        # 如果当前节点是值为1的节点，那么其子树的最小缺失值是find(1)
        if nums[node] == 1:
            res[node] = find(1)
        # 向上传递结果
        current = node
        while parents[current] != -1:
            if res[parents[current]] == 1:
                res[parents[current]] = find(1)
            current = parents[current]
    
    # 从值为1的节点开始DFS（因为只有包含1的子树才可能有大于1的缺失值）
    dfs(pos[1])
    
    return res

# 题目5: SPOJ DQUERY - D-query
# 题目链接: https://www.spoj.com/problems/DQUERY/
# 题目描述: 给定一个数组，多次查询区间内不同元素的个数
# 最优解: 离线处理 + 树状数组（或持久化线段树）是该问题的最优解

def solve_dquery():
    """
    解决SPOJ DQUERY问题 - 区间不同元素个数查询
    使用离线处理 + 树状数组
    时间复杂度: O((n + m) log n)
    空间复杂度: O(n + m)
    """
    import sys
    input = sys.stdin.read().split()
    ptr = 0
    n = int(input[ptr])
    ptr += 1
    
    arr = list(map(int, input[ptr:ptr + n]))
    ptr += n
    
    m = int(input[ptr])
    ptr += 1
    queries = []
    for i in range(m):
        l = int(input[ptr]) - 1  # 转换为0-based
        ptr += 1
        r = int(input[ptr]) - 1
        ptr += 1
        queries.append((l, r, i))
    
    # 离线处理：按右端点排序
    queries.sort(key=lambda x: x[1])
    
    # 树状数组实现
    class FenwickTree:
        def __init__(self, size):
            self.n = size
            self.tree = [0] * (self.n + 1)
        
        def update(self, idx, val):
            """单点更新"""
            idx += 1  # 转换为1-based
            while idx <= self.n:
                self.tree[idx] += val
                idx += idx & -idx
        
        def query(self, idx):
            """前缀和查询"""
            idx += 1  # 转换为1-based
            res = 0
            while idx > 0:
                res += self.tree[idx]
                idx -= idx & -idx
            return res
    
    # 记录每个元素最后出现的位置
    last = {}
    ft = FenwickTree(n)
    res = [0] * m
    
    ptr = 0  # 查询指针
    for i in range(n):
        # 如果当前元素之前出现过，移除之前的贡献
        if arr[i] in last:
            ft.update(last[arr[i]], -1)
        # 添加当前元素的贡献
        ft.update(i, 1)
        last[arr[i]] = i
        
        # 处理所有右端点等于i的查询
        while ptr < m and queries[ptr][1] == i:
            l, r, q_idx = queries[ptr]
            # 区间[l, r]的不同元素个数 = 前缀和(r) - 前缀和(l-1)
            res[q_idx] = ft.query(r) - (ft.query(l - 1) if l > 0 else 0)
            ptr += 1
    
    print('\n'.join(map(str, res)))

# 题目6: 第一次出现位置序列查询
# 题目描述: 给定一个序列，查询区间[l, r]内，所有元素第一次出现的位置的最小值
# 最优解: 持久化线段树是该问题的最优解

def solve_first_occurrence():
    """
    解决第一次出现位置序列查询问题
    使用从右往左构建的持久化线段树
    时间复杂度: O(n log n + q log n)
    空间复杂度: O(n log n)
    """
    import sys
    input = sys.stdin.read().split()
    ptr = 0
    n = int(input[ptr])
    ptr += 1
    q = int(input[ptr])
    ptr += 1
    
    arr = list(map(int, input[ptr:ptr + n]))
    ptr += n
    
    # 从右往左构建持久化线段树
    pst = PersistentSegmentTree()
    pst.roots.append(0)
    
    # 记录每个元素最后一次出现的位置
    last_occurrence = {}
    
    # 自定义更新和查询函数
    class Node:
        def __init__(self, left=0, right=0, min_val=float('inf')):
            self.left = left
            self.right = right
            self.min_val = min_val
    
    # 重新定义持久化线段树用于最小值查询
    class MinPST:
        def __init__(self):
            self.roots = []
            self.nodes = [Node()]  # 哨兵节点
        
        def push_up(self, node_idx):
            node = self.nodes[node_idx]
            left_min = self.nodes[node.left].min_val if node.left else float('inf')
            right_min = self.nodes[node.right].min_val if node.right else float('inf')
            node.min_val = min(left_min, right_min)
        
        def update(self, pre_root, l, r, pos, val):
            new_node = Node(self.nodes[pre_root].left, self.nodes[pre_root].right,
                           self.nodes[pre_root].min_val)
            self.nodes.append(new_node)
            new_root = len(self.nodes) - 1
            
            if l == r:
                new_node.min_val = val
                return new_root
            
            mid = (l + r) // 2
            if pos <= mid:
                new_node.left = self.update(self.nodes[pre_root].left, l, mid, pos, val)
            else:
                new_node.right = self.update(self.nodes[pre_root].right, mid + 1, r, pos, val)
            
            self.push_up(new_root)
            return new_root
        
        def query_min(self, root, l, r, ql, qr):
            if qr < l or ql > r:
                return float('inf')
            
            node = self.nodes[root]
            if ql <= l and r <= qr:
                return node.min_val
            
            mid = (l + r) // 2
            left_min = self.query_min(node.left, l, mid, ql, qr)
            right_min = self.query_min(node.right, mid + 1, r, ql, qr)
            return min(left_min, right_min)
    
    min_pst = MinPST()
    min_pst.roots.append(0)
    
    for i in range(n - 1, -1, -1):
        # 当前版本继承上一版本
        current_root = min_pst.roots[-1]
        
        # 如果当前元素之前出现过，需要更新其位置的最小值
        if arr[i] in last_occurrence:
            # 将之前的位置更新为无穷大
            current_root = min_pst.update(current_root, 1, n, last_occurrence[arr[i]] + 1, float('inf'))
        
        # 更新当前元素的位置
        current_root = min_pst.update(current_root, 1, n, i + 1, i + 1)  # 转换为1-based
        last_occurrence[arr[i]] = i
        min_pst.roots.append(current_root)
    
    # 反转roots数组，使得roots[i]对应前i个元素的版本
    min_pst.roots = min_pst.roots[::-1]
    
    # 处理查询
    results = []
    for _ in range(q):
        l = int(input[ptr])
        ptr += 1
        r = int(input[ptr])
        ptr += 1
        # 查询区间[l, r]内的最小值
        # 注意版本号的对应关系
        min_pos = min_pst.query_min(min_pst.roots[r], 1, n, l, r)
        results.append(min_pos if min_pos != float('inf') else -1)
    
    print('\n'.join(map(str, results)))

# 题目7: 区间最小缺失自然数查询（区间Mex查询）
# 题目描述: 给定一个数组，多次查询区间[l, r]内的最小缺失自然数
# 最优解: 持久化线段树结合离散化是该问题的最优解

def solve_range_mex():
    """
    解决区间最小缺失自然数查询问题
    使用持久化线段树维护每个位置的最晚出现位置
    时间复杂度: O(n log n + q log n)
    空间复杂度: O(n log n)
    """
    import sys
    input = sys.stdin.read().split()
    ptr = 0
    n = int(input[ptr])
    ptr += 1
    q = int(input[ptr])
    ptr += 1
    
    arr = list(map(int, input[ptr:ptr + n]))
    ptr += n
    
    # 离散化处理，只处理非负数
    max_val = max(arr) if arr else 0
    # 构建持久化线段树，维护每个数的最晚出现位置
    
    class MexPST:
        class Node:
            def __init__(self, left=0, right=0, min_pos=0):
                self.left = left
                self.right = right
                self.min_pos = min_pos  # 区间内元素的最小位置
        
        def __init__(self):
            self.roots = []
            self.nodes = [self.Node()]  # 哨兵节点
        
        def push_up(self, node_idx):
            node = self.nodes[node_idx]
            left_min = self.nodes[node.left].min_pos if node.left else float('inf')
            right_min = self.nodes[node.right].min_pos if node.right else float('inf')
            node.min_pos = min(left_min, right_min)
        
        def update(self, pre_root, l, r, pos, val):
            new_node = self.Node(self.nodes[pre_root].left, self.nodes[pre_root].right,
                                self.nodes[pre_root].min_pos)
            self.nodes.append(new_node)
            new_root = len(self.nodes) - 1
            
            if l == r:
                new_node.min_pos = val
                return new_root
            
            mid = (l + r) // 2
            if pos <= mid:
                new_node.left = self.update(self.nodes[pre_root].left, l, mid, pos, val)
            else:
                new_node.right = self.update(self.nodes[pre_root].right, mid + 1, r, pos, val)
            
            self.push_up(new_root)
            return new_root
        
        def query_mex(self, root, l, r, current_l):
            """
            查询最小的mex，即最小的k，使得k的最晚出现位置 < current_l
            """
            if l == r:
                return l
            
            node = self.nodes[root]
            mid = (l + r) // 2
            
            # 检查左子树是否有元素的最晚出现位置 < current_l
            left_min = self.nodes[node.left].min_pos if node.left else float('inf')
            if left_min < current_l:
                return self.query_mex(node.left, l, mid, current_l)
            else:
                return self.query_mex(node.right, mid + 1, r, current_l)
    
    mex_pst = MexPST()
    mex_pst.roots.append(0)  # 初始版本
    
    # 记录每个数最后一次出现的位置
    last_occurrence = {}
    
    # 构建版本树
    for i in range(n):
        val = arr[i]
        # 只处理非负数
        if val >= 0:
            # 如果当前值之前出现过，需要更新其最晚位置
            if val in last_occurrence:
                # 更新当前版本
                new_root = mex_pst.update(mex_pst.roots[-1], 0, max(max_val, n), val, i)
            else:
                # 新增当前值
                new_root = mex_pst.update(mex_pst.roots[-1], 0, max(max_val, n), val, i)
            last_occurrence[val] = i
        else:
            # 对于负数，不影响mex查询，直接复制上一版本
            new_root = mex_pst.roots[-1]
        
        mex_pst.roots.append(new_root)
    
    # 处理查询
    results = []
    for _ in range(q):
        l = int(input[ptr]) - 1  # 转换为0-based
        ptr += 1
        r = int(input[ptr]) - 1
        ptr += 1
        
        # 查询版本r+1中的最小mex
        mex = mex_pst.query_mex(mex_pst.roots[r + 1], 0, max(max_val, n), l)
        results.append(mex)
    
    print('\n'.join(map(str, results)))

# 主函数，用于测试各个问题的解法
if __name__ == "__main__":
    # 根据输入选择要运行的问题
    problem = input("请输入要运行的问题编号 (1-7): ").strip()
    if problem == "1":
        solve_mkthnum()
    elif problem == "2":
        solve_cot()
    elif problem == "3":
        # 测试LeetCode 2276
        intervals = CountIntervals()
        intervals.add(2, 3)
        intervals.add(7, 10)
        print(intervals.count())  # 输出: 6
        intervals.add(5, 8)
        print(intervals.count())  # 输出: 8
    elif problem == "4":
        # 测试LeetCode 1970
        parents = [-1, 0, 0, 2]
        nums = [1, 2, 3, 4]
        print(smallest_missing_value_subtree(parents, nums))  # 输出: [5, 1, 1, 1]
    elif problem == "5":
        solve_dquery()
    elif problem == "6":
        solve_first_occurrence()
    elif problem == "7":
        solve_range_mex()
    else:
        print("无效的问题编号")
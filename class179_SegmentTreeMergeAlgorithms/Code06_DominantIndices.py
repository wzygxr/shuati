# Dominant Indices - CF1009F
# 测试链接 : https://codeforces.com/contest/1009/problem/F

"""
Dominant Indices问题 - Python版本

题目来源: Codeforces 1009F
题目链接: https://codeforces.com/contest/1009/problem/F

题目描述:
给定一棵 n 个节点的树，根节点为1。对于每个节点 u，定义其深度数组为一个无限序列，
其中第 d 项表示 u 的子树中深度为 d 的节点数量。求每个节点的深度数组中最大值的下标。
如果有多个最大值，输出最小的下标。

解题思路:
1. 使用线段树合并技术解决树上统计问题
2. 为每个节点建立一棵深度线段树，维护子树中各深度的节点数量
3. 从叶子节点开始，自底向上合并子树的线段树
4. 查询当前节点线段树中节点数量最多的深度

算法复杂度:
- 时间复杂度: O(n log n)
- 空间复杂度: O(n log n)

线段树合并核心思想:
1. 对于两棵线段树的对应节点，如果只有一棵树有该节点，则直接使用该节点
2. 如果两棵树都有该节点，则递归合并左右子树，并更新当前节点信息
3. 合并过程类似于可并堆的合并方式
"""

import sys
from collections import defaultdict
import threading
from typing import Optional

# 由于Python递归深度限制，我们使用迭代方式实现
sys.setrecursionlimit(1000000)

class SegmentTreeNode:
    """
    线段树节点类
    """
    def __init__(self):
        self.left: Optional['SegmentTreeNode'] = None    # 左子节点
        self.right: Optional['SegmentTreeNode'] = None   # 右子节点
        self.max_cnt: int = 0    # 最大计数
        self.max_dep: int = 0    # 对应的最大深度

def merge(l: int, r: int, t1: Optional[SegmentTreeNode], t2: Optional[SegmentTreeNode]) -> Optional[SegmentTreeNode]:
    """
    合并两棵线段树
    :param l: 区间左端点
    :param r: 区间右端点
    :param t1: 第一棵线段树根节点
    :param t2: 第二棵线段树根节点
    :return: 合并后的线段树根节点
    """
    # 如果其中一个节点为空，返回另一个节点
    if not t1:
        return t2
    if not t2:
        return t1
    
    # 如果是叶子节点，合并节点信息
    if l == r:
        t1.max_cnt += t2.max_cnt
        t1.max_dep = l
        return t1
    
    # 递归合并左右子树
    mid = (l + r) // 2
    t1.left = merge(l, mid, t1.left, t2.left)
    t1.right = merge(mid + 1, r, t1.right, t2.right)
    
    # 更新当前节点信息
    # 左子树信息更深一层，所以要比较右子树和左子树+1
    if t1.right and t1.left:
        if t1.right.max_cnt > t1.left.max_cnt:
            t1.max_cnt = t1.right.max_cnt
            t1.max_dep = t1.right.max_dep
        else:
            t1.max_cnt = t1.left.max_cnt
            t1.max_dep = t1.left.max_dep + 1
    elif t1.right:
        t1.max_cnt = t1.right.max_cnt
        t1.max_dep = t1.right.max_dep
    elif t1.left:
        t1.max_cnt = t1.left.max_cnt
        t1.max_dep = t1.left.max_dep + 1
    
    return t1

def add(d: int, l: int, r: int, node: Optional[SegmentTreeNode]) -> SegmentTreeNode:
    """
    在深度d处增加计数
    :param d: 深度
    :param l: 区间左端点
    :param r: 区间右端点
    :param node: 当前节点
    :return: 更新后的节点
    """
    if not node:
        node = SegmentTreeNode()
    
    # 如果是叶子节点
    if l == r:
        node.max_cnt += 1
        node.max_dep = l
        return node
    
    # 递归更新子树
    mid = (l + r) // 2
    if d <= mid:
        node.left = add(d, l, mid, node.left)
    else:
        node.right = add(d, mid + 1, r, node.right)
    
    # 更新当前节点信息
    if node.right and node.left:
        if node.right.max_cnt > node.left.max_cnt:
            node.max_cnt = node.right.max_cnt
            node.max_dep = node.right.max_dep
        else:
            node.max_cnt = node.left.max_cnt
            node.max_dep = node.left.max_dep + 1
    elif node.right:
        node.max_cnt = node.right.max_cnt
        node.max_dep = node.right.max_dep
    elif node.left:
        node.max_cnt = node.left.max_cnt
        node.max_dep = node.left.max_dep + 1
    
    return node

def dfs(u: int, fa: int, graph: dict, root: list, ans: list) -> None:
    """
    DFS遍历树并计算答案
    :param u: 当前节点
    :param fa: 父节点
    :param graph: 图的邻接表表示
    :param root: 每个节点对应的线段树根节点
    :param ans: 答案数组
    """
    # 先递归处理所有子节点
    for v in graph[u]:
        if v != fa:
            dfs(v, u, graph, root, ans)
    
    # 创建当前节点的线段树（深度为0，计数为1）
    root[u] = SegmentTreeNode()
    root[u].max_cnt = 1
    root[u].max_dep = 0
    
    # 将所有子节点的线段树合并到当前节点
    for v in graph[u]:
        if v != fa:
            root[u] = merge(0, len(graph), root[u], root[v])
    
    # 当前节点的答案就是最大深度
    ans[u] = root[u].max_dep

def main():
    import sys
    input = sys.stdin.read
    data = input().split()
    
    idx = 0
    n = int(data[idx])
    idx += 1
    
    # 构建图
    graph = defaultdict(list)
    for _ in range(n - 1):
        u = int(data[idx])
        idx += 1
        v = int(data[idx])
        idx += 1
        graph[u].append(v)
        graph[v].append(u)
    
    # 计算答案
    root: list = [None] * (n + 1)
    ans = [0] * (n + 1)
    dfs(1, 0, graph, root, ans)
    
    # 输出结果
    result = []
    for i in range(1, n + 1):
        result.append(str(ans[i]))
    
    print('\n'.join(result))

# 由于Python的递归限制，使用线程来增加递归深度
threading.Thread(target=main).start()
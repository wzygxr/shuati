# 晋升者计数问题 (Promotion Counting) - Python版本
# 测试链接 : https://www.luogu.com.cn/problem/P3605

"""
题目来源: USACO 2017 January Contest, Platinum Problem 1. Promotion Counting
题目链接: https://www.luogu.com.cn/problem/P3605

题目描述:
给定一棵 n 个节点的树，每个节点有一个能力值。对于每个节点，统计其子树中有多少个节点的能力值
严格大于该节点的能力值。

解题思路:
1. 使用线段树合并技术解决树上统计问题
2. 为每个节点建立一棵权值线段树，维护子树中各能力值的出现次数
3. 从叶子节点开始，自底向上合并子树的线段树
4. 查询当前节点线段树中大于该节点能力值的节点数量

算法复杂度:
- 时间复杂度: O(n log n)，其中 n 是节点数量
- 空间复杂度: O(n log n)

线段树合并核心思想:
1. 对于两棵线段树的对应节点，如果只有一棵树有该节点，则直接使用该节点
2. 如果两棵树都有该节点，则递归合并左右子树，并更新当前节点信息
3. 合并过程类似于可并堆的合并方式
"""

import sys
from collections import defaultdict
import threading
from typing import Optional, List

# 由于Python递归深度限制，我们使用迭代方式实现
sys.setrecursionlimit(1000000)

class SegmentTreeNode:
    """
    线段树节点类
    """
    def __init__(self):
        self.left: Optional['SegmentTreeNode'] = None    # 左子节点
        self.right: Optional['SegmentTreeNode'] = None   # 右子节点
        self.size: int = 0    # 区间内节点数量

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
        t1.size += t2.size
        return t1
    
    # 递归合并左右子树
    mid = (l + r) // 2
    t1.left = merge(l, mid, t1.left, t2.left)
    t1.right = merge(mid + 1, r, t1.right, t2.right)
    
    # 更新当前节点信息
    t1.size = 0
    if t1.left:
        t1.size += t1.left.size
    if t1.right:
        t1.size += t1.right.size
    
    return t1

def add(val: int, l: int, r: int, node: Optional[SegmentTreeNode]) -> SegmentTreeNode:
    """
    在线段树中添加一个值
    :param val: 要添加的值（离散化后的索引）
    :param l: 区间左端点
    :param r: 区间右端点
    :param node: 当前节点
    :return: 更新后的节点
    """
    if not node:
        node = SegmentTreeNode()
    
    # 如果是叶子节点
    if l == r:
        node.size += 1
        return node
    
    # 递归更新子树
    mid = (l + r) // 2
    if val <= mid:
        node.left = add(val, l, mid, node.left)
    else:
        node.right = add(val, mid + 1, r, node.right)
    
    # 更新当前节点信息
    node.size = 0
    if node.left:
        node.size += node.left.size
    if node.right:
        node.size += node.right.size
    
    return node

def query(l: int, r: int, ql: int, qr: int, node: Optional[SegmentTreeNode]) -> int:
    """
    查询区间[ql, qr]内的节点数量
    :param l: 当前区间左端点
    :param r: 当前区间右端点
    :param ql: 查询区间左端点
    :param qr: 查询区间右端点
    :param node: 当前节点
    :return: 区间内节点数量
    """
    # 边界条件：查询区间无效或节点为空
    if ql > qr or not node:
        return 0
    
    # 完全覆盖：当前区间完全在查询区间内
    if ql <= l and r <= qr:
        return node.size
    
    mid = (l + r) // 2
    result = 0
    
    # 递归查询左右子树
    if ql <= mid:
        result += query(l, mid, ql, qr, node.left)
    if qr > mid:
        result += query(mid + 1, r, ql, qr, node.right)
    
    return result

def dfs(u: int, fa: int, graph: dict, root: list, arr: list, ans: list, sorted_vals: list) -> None:
    """
    DFS遍历树并计算答案
    :param u: 当前节点
    :param fa: 父节点
    :param graph: 图的邻接表表示
    :param root: 每个节点对应的线段树根节点
    :param arr: 节点能力值数组
    :param ans: 答案数组
    :param sorted_vals: 排序后的值数组
    """
    # 先递归处理所有子节点
    for v in graph[u]:
        if v != fa:
            dfs(v, u, graph, root, arr, ans, sorted_vals)
    
    # 将所有子节点的线段树合并到当前节点
    for v in graph[u]:
        if v != fa:
            root[u] = merge(1, len(sorted_vals), root[u], root[v])
    
    # 查询大于当前节点能力值的节点数量
    # 二分查找当前节点能力值在排序数组中的位置
    left, right = 1, len(sorted_vals)
    pos = 0
    while left <= right:
        mid = (left + right) // 2
        if sorted_vals[mid-1] <= arr[u]:
            pos = mid
            left = mid + 1
        else:
            right = mid - 1
    
    # 查询大于该位置的数量
    ans[u] = query(1, len(sorted_vals), pos + 1, len(sorted_vals), root[u])

def main():
    import sys
    input = sys.stdin.read
    data = input().split()
    
    idx = 0
    n = int(data[idx])
    idx += 1
    
    # 读取节点能力值
    arr = [0] * (n + 1)
    for i in range(1, n + 1):
        arr[i] = int(data[idx])
        idx += 1
    
    # 构建图
    graph = defaultdict(list)
    for i in range(2, n + 1):
        fa = int(data[idx])
        idx += 1
        graph[fa].append(i)
        graph[i].append(fa)
    
    # 离散化处理
    sorted_vals = sorted(set(arr[1:]))
    
    # 为每个节点建立初始线段树节点
    root: List[Optional[SegmentTreeNode]] = [None] * (n + 1)
    for i in range(1, n + 1):
        # 查找离散化后的索引
        left, right = 0, len(sorted_vals) - 1
        pos = 0
        while left <= right:
            mid = (left + right) // 2
            if sorted_vals[mid] <= arr[i]:
                pos = mid
                left = mid + 1
            else:
                right = mid - 1
        pos += 1  # 转换为1-indexed
        root[i] = add(pos, 1, len(sorted_vals), root[i])
    
    # 计算答案
    ans = [0] * (n + 1)
    dfs(1, 0, graph, root, arr, ans, sorted_vals)
    
    # 输出结果
    for i in range(1, n + 1):
        print(ans[i])

# 由于Python的递归限制，使用线程来增加递归深度
if __name__ == "__main__":
    threading.Thread(target=main).start()
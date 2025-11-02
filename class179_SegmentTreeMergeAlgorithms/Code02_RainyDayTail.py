# 雨天的尾巴问题 (Rainy Day Tail) - Python版本
# 测试链接 : https://www.luogu.com.cn/problem/P4556

"""
题目来源: Vani有约会 洛谷P4556
题目链接: https://www.luogu.com.cn/problem/P4556

题目描述:
给定一棵 n 个节点的树和 m 次操作，每次操作在两点间路径上投放某种类型的物品。
要求最后统计每个节点收到最多物品的类型。

解题思路:
1. 利用树上差分技术，在路径端点和LCA处打标记
2. 为每个节点建立线段树，维护各类型物品的数量
3. 自底向上合并子树信息，查询最大值对应的类型

算法复杂度:
- 时间复杂度: O((n + m) log n)
- 空间复杂度: O(n log n)

树上差分核心思想:
1. 对于路径 u->v，在 u 和 v 处 +1，在 lca(u,v) 和 fa[lca(u,v)] 处 -1
2. 通过DFS遍历，子树内的标记和即为该节点的物品数量
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
        self.max_count: int = 0    # 区间最大值
        self.max_type: int = 0     # 最大值对应的类型

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
        t1.max_count += t2.max_count
        return t1
    
    # 递归合并左右子树
    mid = (l + r) // 2
    t1.left = merge(l, mid, t1.left, t2.left)
    t1.right = merge(mid + 1, r, t1.right, t2.right)
    
    # 更新当前节点信息（维护区间最大值）
    t1.max_count = 0
    if t1.left and t1.left.max_count > t1.max_count:
        t1.max_count = t1.left.max_count
        t1.max_type = t1.left.max_type
    if t1.right and t1.right.max_count > t1.max_count:
        t1.max_count = t1.right.max_count
        t1.max_type = t1.right.max_type
    
    return t1

def add(food_type: int, value: int, l: int, r: int, node: Optional[SegmentTreeNode]) -> SegmentTreeNode:
    """
    在线段树中添加/删除一个值
    :param food_type: 要操作的物品类型
    :param value: 操作值（+1表示添加，-1表示删除）
    :param l: 区间左端点
    :param r: 区间右端点
    :param node: 当前节点
    :return: 更新后的节点
    """
    if not node:
        node = SegmentTreeNode()
    
    # 如果是叶子节点
    if l == r:
        node.max_count += value
        node.max_type = l
        return node
    
    # 递归更新子树
    mid = (l + r) // 2
    if food_type <= mid:
        node.left = add(food_type, value, l, mid, node.left)
    else:
        node.right = add(food_type, value, mid + 1, r, node.right)
    
    # 更新当前节点信息（维护区间最大值）
    node.max_count = 0
    if node.left and node.left.max_count > node.max_count:
        node.max_count = node.left.max_count
        node.max_type = node.left.max_type
    if node.right and node.right.max_count > node.max_count:
        node.max_count = node.right.max_count
        node.max_type = node.right.max_type
    
    return node

def query(l: int, r: int, node: Optional[SegmentTreeNode]) -> int:
    """
    查询最大值对应的物品类型
    :param l: 区间左端点
    :param r: 区间右端点
    :param node: 当前节点
    :return: 最大值对应的物品类型
    """
    # 叶子节点：返回该类型
    if l == r:
        return l
    
    mid = (l + r) // 2
    # 根据左右子树的最大值决定递归方向
    if node is not None:
        left_node = node.left
        right_node = node.right
        
        if left_node is not None and right_node is not None:
            if left_node.max_count >= right_node.max_count:
                return query(l, mid, left_node)
            else:
                return query(mid + 1, r, right_node)
        elif left_node is not None:
            return query(l, mid, left_node)
        elif right_node is not None:
            return query(mid + 1, r, right_node)
    
    return 0

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
    
    # 将所有子节点的线段树合并到当前节点
    for v in graph[u]:
        if v != fa:
            root[u] = merge(1, 100000, root[u], root[v])
    
    # 如果当前节点有物品，查询最大值对应的类型
    if root[u] and root[u].max_count > 0:
        ans[u] = root[u].max_type

def get_lca(u: int, v: int, depth: list, parent: list) -> int:
    """
    求两个节点的最近公共祖先(LCA)
    :param u: 节点u
    :param v: 节点v
    :param depth: 节点深度数组
    :param parent: 节点父节点数组
    :return: LCA节点
    """
    # 保证u的深度不小于v
    if depth[u] < depth[v]:
        u, v = v, u
    
    # 将u向上跳到与v同一深度
    while depth[u] > depth[v]:
        u = parent[u]
    
    # 如果u就是v，说明v是u的祖先
    if u == v:
        return u
    
    # u和v一起向上跳，直到它们的父节点相同
    while parent[u] != parent[v]:
        u = parent[u]
        v = parent[v]
    
    return parent[u]  # 返回父节点即为LCA

def main():
    import sys
    input = sys.stdin.read
    data = input().split()
    
    idx = 0
    n = int(data[idx])
    idx += 1
    m = int(data[idx])
    idx += 1
    
    # 构建图
    graph = defaultdict(list)
    for i in range(n - 1):
        u = int(data[idx])
        idx += 1
        v = int(data[idx])
        idx += 1
        graph[u].append(v)
        graph[v].append(u)
    
    # BFS计算深度和父节点（用于LCA计算）
    from collections import deque
    depth = [0] * (n + 1)
    parent = [0] * (n + 1)
    visited = [False] * (n + 1)
    
    queue = deque([1])
    visited[1] = True
    depth[1] = 1
    
    while queue:
        u = queue.popleft()
        for v in graph[u]:
            if not visited[v]:
                visited[v] = True
                depth[v] = depth[u] + 1
                parent[v] = u
                queue.append(v)
    
    # 为每个节点建立线段树根节点
    root: List[Optional[SegmentTreeNode]] = [None] * (n + 1)
    
    # 处理操作
    for i in range(m):
        x = int(data[idx])
        idx += 1
        y = int(data[idx])
        idx += 1
        food = int(data[idx])
        idx += 1
        
        # 树上差分：在路径端点和LCA处打标记
        lca = get_lca(x, y, depth, parent)
        lca_parent = parent[lca]
        
        # 在路径端点添加标记
        root[x] = add(food, 1, 1, 100000, root[x])
        root[y] = add(food, 1, 1, 100000, root[y])
        
        # 在LCA和其父节点处减去标记
        root[lca] = add(food, -1, 1, 100000, root[lca])
        if lca_parent != 0:
            root[lca_parent] = add(food, -1, 1, 100000, root[lca_parent])
    
    # 计算答案
    ans = [0] * (n + 1)
    dfs(1, 0, graph, root, ans)
    
    # 输出结果
    for i in range(1, n + 1):
        print(ans[i])

# 由于Python的递归限制，使用线程来增加递归深度
if __name__ == "__main__":
    threading.Thread(target=main).start()
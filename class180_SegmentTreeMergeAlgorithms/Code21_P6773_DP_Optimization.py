import sys
import math
from typing import List, Tuple

sys.setrecursionlimit(1000000)

class FastIO:
    def __init__(self):
        self.buffer = sys.stdin.read().split()
        self.ptr = 0
    
    def next_int(self):
        val = int(self.buffer[self.ptr])
        self.ptr += 1
        return val
    
    def next_long(self):
        val = int(self.buffer[self.ptr])
        self.ptr += 1
        return val

class SegmentTreeNode:
    __slots__ = ('left', 'right', 'min_val', 'lazy')
    
    def __init__(self):
        self.left = None
        self.right = None
        self.min_val = 10**18
        self.lazy = 0

class SegmentTree:
    """
    动态开点线段树，用于维护DP状态
    支持区间加、区间查询最小值、线段树合并
    """
    
    def __init__(self):
        self.nodes = []
        self.root = None
    
    def new_node(self) -> SegmentTreeNode:
        """创建新节点"""
        node = SegmentTreeNode()
        self.nodes.append(node)
        return node
    
    def push_down(self, node: SegmentTreeNode, l: int, r: int) -> None:
        """下推懒标记"""
        if node.lazy != 0:
            if node.left is None:
                node.left = self.new_node()
            if node.right is None:
                node.right = self.new_node()
            
            mid = (l + r) // 2
            
            # 更新左子树
            node.left.min_val += node.lazy
            node.left.lazy += node.lazy
            
            # 更新右子树
            node.right.min_val += node.lazy
            node.right.lazy += node.lazy
            
            node.lazy = 0
    
    def update(self, node: SegmentTreeNode, l: int, r: int, pos: int, val: int) -> None:
        """单点更新"""
        if l == r:
            node.min_val = min(node.min_val, val)
            return
        
        self.push_down(node, l, r)
        mid = (l + r) // 2
        
        if pos <= mid:
            if node.left is None:
                node.left = self.new_node()
            self.update(node.left, l, mid, pos, val)
        else:
            if node.right is None:
                node.right = self.new_node()
            self.update(node.right, mid + 1, r, pos, val)
        
        # 更新当前节点最小值
        left_min = node.left.min_val if node.left else 10**18
        right_min = node.right.min_val if node.right else 10**18
        node.min_val = min(left_min, right_min)
    
    def add(self, node: SegmentTreeNode, l: int, r: int, ql: int, qr: int, val: int) -> None:
        """区间加操作"""
        if node is None or ql > qr:
            return
        
        if ql <= l and r <= qr:
            node.min_val += val
            node.lazy += val
            return
        
        self.push_down(node, l, r)
        mid = (l + r) // 2
        
        if ql <= mid and node.left is not None:
            self.add(node.left, l, mid, ql, qr, val)
        if qr > mid and node.right is not None:
            self.add(node.right, mid + 1, r, ql, qr, val)
        
        # 更新当前节点最小值
        left_min = node.left.min_val if node.left else 10**18
        right_min = node.right.min_val if node.right else 10**18
        node.min_val = min(left_min, right_min)
    
    def query(self, node: SegmentTreeNode, l: int, r: int, ql: int, qr: int) -> int:
        """区间查询最小值"""
        if node is None or ql > qr:
            return 10**18
        
        if ql <= l and r <= qr:
            return node.min_val
        
        self.push_down(node, l, r)
        mid = (l + r) // 2
        res = 10**18
        
        if ql <= mid and node.left is not None:
            res = min(res, self.query(node.left, l, mid, ql, qr))
        if qr > mid and node.right is not None:
            res = min(res, self.query(node.right, mid + 1, r, ql, qr))
        
        return res
    
    def merge(self, p: SegmentTreeNode, q: SegmentTreeNode, l: int, r: int, 
              add_p: int, add_q: int) -> SegmentTreeNode:
        """
        合并两棵线段树（核心操作）
        add_p: 对p树的加法偏移量
        add_q: 对q树的加法偏移量
        """
        if p is None and q is None:
            return None
        
        if p is None:
            # 对q树整体加add_q
            self.add(q, l, r, l, r, add_q)
            return q
        
        if q is None:
            # 对p树整体加add_p
            self.add(p, l, r, l, r, add_p)
            return p
        
        if l == r:
            # 叶子节点合并
            new_node = self.new_node()
            new_node.min_val = min(p.min_val + add_p, q.min_val + add_q)
            return new_node
        
        self.push_down(p, l, r)
        self.push_down(q, l, r)
        
        mid = (l + r) // 2
        
        # 递归合并左右子树
        left_merged = self.merge(
            p.left, q.left, l, mid, add_p, add_q
        )
        right_merged = self.merge(
            p.right, q.right, mid + 1, r, add_p, add_q
        )
        
        # 创建新节点
        new_node = self.new_node()
        new_node.left = left_merged
        new_node.right = right_merged
        
        # 更新最小值
        left_min = left_merged.min_val if left_merged else 10**18
        right_min = right_merged.min_val if right_merged else 10**18
        new_node.min_val = min(left_min, right_min)
        
        return new_node

def main():
    io = FastIO()
    
    n = io.next_int()
    weight = [0] * (n + 1)
    graph = [[] for _ in range(n + 1)]
    dp_trees = [None] * (n + 1)  # 每个节点的线段树
    
    # 读取节点权值
    for i in range(1, n + 1):
        weight[i] = io.next_long()
    
    # 构建树
    for _ in range(n - 1):
        u = io.next_int()
        v = io.next_int()
        graph[u].append(v)
        graph[v].append(u)
    
    def dfs(u: int, parent: int) -> None:
        """DFS遍历树，构建DP线段树"""
        # 创建当前节点的线段树
        tree = SegmentTree()
        tree.root = tree.new_node()
        
        # 初始化DP状态
        # dp[u][0]: u不选，所有子节点必须选
        # dp[u][1]: u选，子节点可选可不选
        tree.update(tree.root, 0, n, 0, 0)  # 初始状态
        
        for v in graph[u]:
            if v == parent:
                continue
            
            dfs(v, u)
            child_tree = dp_trees[v]
            
            if child_tree.root is not None:
                # 计算子树的DP值
                min_child_select = child_tree.query(child_tree.root, 0, n, 0, n)
                min_child_not_select = child_tree.query(child_tree.root, 0, n, 1, n)
                
                # 合并子树线段树
                tree.root = tree.merge(
                    tree.root, child_tree.root, 0, n,
                    min_child_select, min_child_not_select + weight[u]
                )
        
        # 最终处理：考虑当前节点权值
        if u != 1:  # 非根节点需要加上自身权值
            tree.add(tree.root, 0, n, 0, n, weight[u])
        
        dp_trees[u] = tree
    
    # 从根节点开始DFS
    dfs(1, 0)
    
    # 输出结果
    result = dp_trees[1].root.min_val
    print(result)

if __name__ == "__main__":
    main()

"""
线段树合并加速DP转移的Python实现特点:

1. 面向对象设计: 使用类来组织线段树节点和操作
2. 动态内存管理: Python的垃圾回收机制自动管理内存
3. 递归实现: 使用递归实现线段树合并，代码简洁清晰
4. 懒标记优化: 支持区间加操作的懒标记传播

算法复杂度分析:
- 时间复杂度: O(n log n)，每个节点最多被合并log n次
- 空间复杂度: O(n log n)，动态开点线段树的空间消耗

适用场景:
1. 树形DP问题，需要合并子树信息
2. DP状态转移涉及区间操作
3. 需要支持动态修改和查询
4. 数据规模较大，需要高效算法

类似题目推荐:
1. P5298 [PKUWC2018] Minimax - 概率DP + 线段树合并
2. CF455D Serega and Fun - 区间操作 + DP优化
3. CF868F Yet Another Minimization Problem - 分治DP + 线段树
4. CF321E Ciel and Gondolas - 四边形不等式优化
5. P4770 [NOI2018] 你的名字 - 后缀自动机 + 线段树合并

Python实现的注意事项:
1. 设置递归深度限制: sys.setrecursionlimit()
2. 使用__slots__优化内存
3. 注意Python的递归深度限制
4. 使用类型注解提高代码可读性
"""
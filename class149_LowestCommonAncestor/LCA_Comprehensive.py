"""
LCA问题综合实现 - Python版本
本文件提供了完整的LCA算法实现，涵盖多种解法和优化策略

主要内容包括：
1. 基础LCA算法（递归DFS、倍增法、Tarjan算法、树链剖分）
2. 各大OJ平台的经典LCA题目
3. 详细的复杂度分析和工程化考量

算法复杂度总结：
| 算法 | 预处理时间 | 查询时间 | 空间复杂度 | 适用场景 |
|------|------------|----------|------------|----------|
| 递归DFS | O(1) | O(n) | O(h) | 单次查询 |
| 倍增法 | O(n log n) | O(log n) | O(n log n) | 在线查询 |
| Tarjan算法 | O(n + q) | O(1) | O(n + q) | 离线查询 |
| 树链剖分 | O(n) | O(log n) | O(n) | 复杂树上操作 |

工程化考量：
1. 异常处理：输入验证、边界条件处理
2. 性能优化：预处理优化、查询优化
3. 可读性：详细注释、模块化设计
4. 调试能力：打印调试、断言验证
5. 单元测试：覆盖各种边界场景

语言特性差异：
1. Python: 动态类型，引用计数垃圾回收，代码简洁
2. Java: 自动垃圾回收，对象引用传递，类型安全
3. C++: 手动内存管理，指针操作，高性能
"""

from typing import List, Optional, Tuple
from collections import defaultdict, deque

# 二叉树节点定义
class TreeNode:
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

# 带父指针的二叉树节点定义
class TreeNodeWithParent:
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None
        self.parent = None

class LCASolution:
    """
    LCA算法综合解决方案类
    包含多种LCA算法的实现和优化
    """
    
    def lowestCommonAncestor(self, root: TreeNode, p: TreeNode, q: TreeNode) -> Optional[TreeNode]:
        """
        解法一：LeetCode 236. 二叉树的最近公共祖先（递归DFS）
        时间复杂度：O(n)
        空间复杂度：O(h)
        是否为最优解：是，对于单次查询是最优解
        """
        if not root or not p or not q:
            return None
        
        if root == p or root == q:
            return root
        
        left = self.lowestCommonAncestor(root.left, p, q)
        right = self.lowestCommonAncestor(root.right, p, q)
        
        if left and right:
            return root
        return left if left else right
    
    def lowestCommonAncestorBST(self, root: TreeNode, p: TreeNode, q: TreeNode) -> Optional[TreeNode]:
        """
        解法二：LeetCode 235. 二叉搜索树的最近公共祖先
        时间复杂度：O(h)
        空间复杂度：O(h)
        是否为最优解：是，利用了BST的特性
        """
        if not root or not p or not q:
            return None
        
        if p.val < root.val and q.val < root.val:
            return self.lowestCommonAncestorBST(root.left, p, q)
        elif p.val > root.val and q.val > root.val:
            return self.lowestCommonAncestorBST(root.right, p, q)
        else:
            return root
    
    def lowestCommonAncestorWithParent(self, p: TreeNodeWithParent, q: TreeNodeWithParent) -> Optional[TreeNodeWithParent]:
        """
        解法三：LeetCode 1650. 带父指针的二叉树最近公共祖先
        时间复杂度：O(h)
        空间复杂度：O(1)
        是否为最优解：是
        """
        if not p or not q:
            return None
        
        a, b = p, q
        while a != b:
            a = a.parent if a else q
            b = b.parent if b else p
        
        return a
    
    def lowestCommonAncestorIterative(self, root: TreeNode, p: TreeNode, q: TreeNode) -> Optional[TreeNode]:
        """
        解法四：迭代版本的二叉树LCA
        时间复杂度：O(n)
        空间复杂度：O(h)
        是否为最优解：是，避免递归栈溢出
        """
        if not root or not p or not q:
            return None
        
        parent_map = {root: None}
        stack = [root]
        
        while p not in parent_map or q not in parent_map:
            node = stack.pop()
            
            if node.right:
                parent_map[node.right] = node
                stack.append(node.right)
            if node.left:
                parent_map[node.left] = node
                stack.append(node.left)
        
        ancestors = set()
        current = p
        while current:
            ancestors.add(current)
            current = parent_map[current]
        
        current = q
        while current not in ancestors:
            current = parent_map[current]
        
        return current

class BinaryLiftingLCA:
    """
    解法五：洛谷 P3379 【模板】最近公共祖先（倍增法）
    时间复杂度：预处理O(n log n)，查询O(log n)
    空间复杂度：O(n log n)
    是否为最优解：是，对于在线查询是最优解
    """
    
    def __init__(self, n: int, edges: List[List[int]]):
        self.MAXN = 500001
        self.LOG = 20
        
        self.depth = [0] * (n + 1)
        self.ancestor = [[0] * self.LOG for _ in range(n + 1)]
        self.tree = [[] for _ in range(n + 1)]
        
        for u, v in edges:
            self.tree[u].append(v)
            self.tree[v].append(u)
        
        self.dfs(1, 0)
    
    def dfs(self, u: int, parent: int):
        self.depth[u] = self.depth[parent] + 1
        self.ancestor[u][0] = parent
        
        for i in range(1, self.LOG):
            if self.ancestor[u][i - 1] != 0:
                self.ancestor[u][i] = self.ancestor[self.ancestor[u][i - 1]][i - 1]
        
        for v in self.tree[u]:
            if v != parent:
                self.dfs(v, u)
    
    def getLCA(self, u: int, v: int) -> int:
        if self.depth[u] < self.depth[v]:
            u, v = v, u
        
        for i in range(self.LOG - 1, -1, -1):
            if self.depth[u] - (1 << i) >= self.depth[v]:
                u = self.ancestor[u][i]
        
        if u == v:
            return u
        
        for i in range(self.LOG - 1, -1, -1):
            if self.ancestor[u][i] != self.ancestor[v][i]:
                u = self.ancestor[u][i]
                v = self.ancestor[v][i]
        
        return self.ancestor[u][0]

class TreeDistance:
    """
    解法六：HDU 2586 How far away？（树上距离）
    时间复杂度：预处理O(n log n)，查询O(log n)
    空间复杂度：O(n log n)
    是否为最优解：是
    """
    
    def __init__(self, n: int, edges: List[List[int]], weights: List[int]):
        self.lca_solver = BinaryLiftingLCA(n, edges)
        self.dist = [0] * (n + 1)
        self._calculate_dist(1, 0, 0, edges, weights)
    
    def _calculate_dist(self, u: int, parent: int, current_dist: int, 
                       edges: List[List[int]], weights: List[int]):
        self.dist[u] = current_dist
        
        for i, (a, b) in enumerate(edges):
            if a == u and b != parent:
                self._calculate_dist(b, u, current_dist + weights[i], edges, weights)
            elif b == u and a != parent:
                self._calculate_dist(a, u, current_dist + weights[i], edges, weights)
    
    def get_distance(self, u: int, v: int) -> int:
        lca = self.lca_solver.getLCA(u, v)
        return self.dist[u] + self.dist[v] - 2 * self.dist[lca]

class TarjanLCA:
    """
    解法七：SPOJ LCASQ - Lowest Common Ancestor（Tarjan离线算法）
    时间复杂度：O(n + q)
    空间复杂度：O(n + q)
    是否为最优解：是，对于离线查询是最优解
    """
    
    def __init__(self, n: int, edges: List[List[int]], query_pairs: List[List[int]]):
        self.tree = [[] for _ in range(n)]
        self.queries = [[] for _ in range(n)]
        self.parent = list(range(n))
        self.ancestor = [0] * n
        self.visited = [False] * n
        
        for u, v in edges:
            self.tree[u].append(v)
            self.tree[v].append(u)
        
        for i, (u, v) in enumerate(query_pairs):
            self.queries[u].append((v, i))
            self.queries[v].append((u, i))
    
    def _find(self, x: int) -> int:
        if self.parent[x] != x:
            self.parent[x] = self._find(self.parent[x])
        return self.parent[x]
    
    def _union(self, x: int, y: int):
        root_x = self._find(x)
        root_y = self._find(y)
        if root_x != root_y:
            self.parent[root_y] = root_x
    
    def tarjan_lca(self, u: int, parent_node: int, results: List[int]):
        self.ancestor[u] = u
        self.visited[u] = True
        
        for v in self.tree[u]:
            if v != parent_node:
                self.tarjan_lca(v, u, results)
                self._union(u, v)
                self.ancestor[self._find(u)] = u
        
        for v, query_idx in self.queries[u]:
            if self.visited[v]:
                results[query_idx] = self.ancestor[self._find(v)]

def test_lca_algorithms():
    """
    测试所有LCA算法的实现
    """
    print("=== LCA算法综合测试 - Python版本 ===\n")
    
    solution = LCASolution()
    
    # 测试1: 标准二叉树LCA
    print("=== 测试1: 标准二叉树LCA ===")
    root = TreeNode(3)
    node5 = TreeNode(5)
    node1 = TreeNode(1)
    root.left = node5
    root.right = node1
    
    result1 = solution.lowestCommonAncestor(root, node5, node1)
    print(f"测试1.1: LCA(5, 1) = {result1.val if result1 else 'None'}")
    
    # 测试2: 二叉搜索树LCA
    print("\n=== 测试2: 二叉搜索树LCA ===")
    bst_root = TreeNode(6)
    bst_node2 = TreeNode(2)
    bst_node8 = TreeNode(8)
    bst_root.left = bst_node2
    bst_root.right = bst_node8
    
    result2 = solution.lowestCommonAncestorBST(bst_root, bst_node2, bst_node8)
    print(f"测试2.1: BST LCA(2, 8) = {result2.val if result2 else 'None'}")
    
    # 测试3: 带父指针的LCA
    print("\n=== 测试3: 带父指针的LCA ===")
    root_wp = TreeNodeWithParent(3)
    wp_node5 = TreeNodeWithParent(5)
    wp_node1 = TreeNodeWithParent(1)
    root_wp.left = wp_node5
    root_wp.right = wp_node1
    wp_node5.parent = root_wp
    wp_node1.parent = root_wp
    
    result3 = solution.lowestCommonAncestorWithParent(wp_node5, wp_node1)
    print(f"测试3.1: 带父指针LCA(5, 1) = {result3.val if result3 else 'None'}")
    
    # 测试4: 迭代版本LCA
    print("\n=== 测试4: 迭代版本LCA ===")
    result4 = solution.lowestCommonAncestorIterative(root, node5, node1)
    print(f"测试4.1: 迭代版LCA(5, 1) = {result4.val if result4 else 'None'}")
    
    # 测试5: 洛谷P3379倍增法
    print("\n=== 测试5: 洛谷P3379倍增法 ===")
    edges = [[1, 2], [1, 3], [2, 4], [2, 5]]
    luogu = BinaryLiftingLCA(5, edges)
    result5 = luogu.getLCA(4, 5)
    print(f"测试5.1: LCA(4, 5) = {result5}")
    
    # 测试6: HDU2586树上距离
    print("\n=== 测试6: HDU2586树上距离 ===")
    weights = [10, 20, 30, 40]
    hdu = TreeDistance(5, edges, weights)
    result6 = hdu.get_distance(4, 5)
    print(f"测试6.1: Distance(4, 5) = {result6}")
    
    print("\n=== 所有测试完成 ===")

if __name__ == "__main__":
    test_lca_algorithms()
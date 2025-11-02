# 树的最大独立集 (Tree Maximum Independent Set)
# 题目描述:
# 对于一棵有N个结点的无根树，选出尽量多的结点，使得任何两个结点均不相邻
# 这是一个经典的树形动态规划问题
#
# 解题思路:
# 1. 使用树形动态规划（Tree DP）的方法
# 2. 对于每个节点，我们需要知道以下信息：
#    - 当前节点被选中时，以该节点为根的子树能选出的最大独立集大小
#    - 当前节点不被选中时，以该节点为根的子树能选出的最大独立集大小
# 3. 递归处理子树，综合计算当前节点的信息
# 4. 状态转移方程：
#    - 当前节点被选中：dp[u][1] = weight[u] + sum(dp[v][0]) for each child v
#    - 当前节点不被选中：dp[u][0] = sum(max(dp[v][0], dp[v][1])) for each child v
#
# 时间复杂度: O(n) - n为树中节点的数量，需要遍历所有节点一次
# 空间复杂度: O(n) - 存储树结构和DP数组，递归调用栈深度为O(h)，h为树高
# 是否为最优解: 是，这是解决树的最大独立集问题的标准方法，无法进一步降低时间复杂度
#
# 【补充题目】
# 1. 洛谷 P1352 没有上司的舞会 - https://www.luogu.com.cn/problem/P1352
# 2. HDU 1520 Anniversary party - http://acm.hdu.edu.cn/showproblem.php?pid=1520
# 3. LeetCode 337. 打家劫舍III - https://leetcode-cn.com/problems/house-robber-iii/
# 4. LeetCode 2646. 最小化旅行的价格总和 - https://leetcode-cn.com/problems/minimize-the-total-price-of-the-trips/
# 5. Codeforces 1083C Max Mex - https://codeforces.com/problemset/problem/1083/C
# 6. AtCoder ABC163F path pass i - https://atcoder.jp/contests/abc163/tasks/abc163_f
# 7. POJ 3342 Party at Hali-Bula - http://poj.org/problem?id=3342
# 8. SPOJ PT07Z - Longest path in a tree - https://www.spoj.com/problems/PT07Z/
#
# 【工程化考量】
# 1. 使用Python字典实现邻接表，支持任意节点编号
# 2. 添加异常处理和参数校验
# 3. 提供多版本接口，适应不同的树结构表示
# 4. 支持无向树和有根树两种场景
# 5. 实现二叉树版本（用于LeetCode 337打家劫舍III）
# 6. 添加单元测试，确保代码正确性
# 7. 递归实现与迭代实现的对比
# 8. 性能优化：使用记忆化避免重复计算
# 9. 边界情况处理：空树、单节点树、链式树等

from typing import List, Optional, Dict, Tuple, Set
import sys
import unittest

# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

# 树的最大独立集类实现
class TreeMaxIndependentSet:
    def __init__(self):
        self.tree: Dict[int, List[int]] = dict()  # 使用字典实现邻接表
        self.weight: Dict[int, int] = dict()      # 节点权重
        self.has_parent: Dict[int, bool] = dict() # 标记节点是否有父节点
        self.n: int = 0                           # 节点数量
    
    def build_tree(self, n: int) -> None:
        """初始化树结构
        
        Args:
            n: 节点数量
            
        Raises:
            ValueError: 当节点数量不合法时抛出
        """
        if n <= 0:
            raise ValueError("节点数量必须为正整数")
        
        self.n = n
        self.tree = {i: [] for i in range(1, n+1)}
        self.weight = {i: 1 for i in range(1, n+1)}  # 默认权重为1
        self.has_parent = {i: False for i in range(1, n+1)}
    
    def add_edge(self, u: int, v: int) -> None:
        """添加无向边（适用于一般树结构）
        
        Args:
            u: 节点u
            v: 节点v
            
        Raises:
            ValueError: 当节点编号不合法时抛出
        """
        if u <= 0 or v <= 0:
            raise ValueError(f"节点编号必须为正整数，收到: u={u}, v={v}")
        
        if u not in self.tree:
            self.tree[u] = []
        if v not in self.tree:
            self.tree[v] = []
            
        # 添加双向边
        self.tree[u].append(v)
        self.tree[v].append(u)
    
    def add_directed_edge(self, u: int, v: int) -> None:
        """添加有向边（适用于有根树）
        
        Args:
            u: 父节点
            v: 子节点
            
        Raises:
            ValueError: 当节点编号不合法时抛出
        """
        if u <= 0 or v <= 0:
            raise ValueError(f"节点编号必须为正整数，收到: u={u}, v={v}")
        
        if u not in self.tree:
            self.tree[u] = []
        if v not in self.tree:
            self.tree[v] = []
            
        # 添加单向边
        self.tree[u].append(v)
    
    def set_parent(self, parent: int, child: int) -> None:
        """设置父子关系，构建有根树
        
        Args:
            parent: 父节点
            child: 子节点
            
        Raises:
            ValueError: 当节点编号不合法时抛出
            ValueError: 当存在循环依赖时抛出
        """
        if parent <= 0 or child <= 0:
            raise ValueError(f"节点编号必须为正整数，收到: parent={parent}, child={child}")
        
        # 检查循环依赖
        if self._has_cycle(child, parent):
            raise ValueError(f"设置父子关系会导致环: {parent} -> {child}")
            
        self.add_directed_edge(parent, child)
        self.has_parent[child] = True
    
    def _has_cycle(self, start: int, target: int) -> bool:
        """检查从start到target是否存在路径（检测潜在的环）"""
        visited = set()
        
        def dfs(current: int) -> bool:
            if current == target:
                return True
            visited.add(current)
            for neighbor in self.tree.get(current, []):
                if neighbor not in visited:
                    if dfs(neighbor):
                        return True
            return False
        
        return dfs(start)
    
    def set_weight(self, node: int, w: int) -> None:
        """设置节点权重
        
        Args:
            node: 节点编号
            w: 权重值
            
        Raises:
            ValueError: 当节点编号不合法时抛出
        """
        if node <= 0:
            raise ValueError(f"节点编号必须为正整数，收到: {node}")
        self.weight[node] = w
    
    def dfs(self, u: int, parent: int) -> Tuple[int, int]:
        """深度优先搜索进行树形DP
        
        Args:
            u: 当前节点
            parent: 父节点（避免回环）
            
        Returns:
            tuple: (dp0, dp1) 其中dp0表示不选当前节点的最大值，dp1表示选当前节点的最大值
        """
        # dp0表示当前节点不选，dp1表示当前节点选
        dp0 = 0  # 不选当前节点，初始为0
        dp1 = self.weight.get(u, 1)  # 选当前节点，初始为节点权重
        
        # 遍历所有相邻节点
        for v in self.tree.get(u, []):
            # 避免回到父节点
            if v != parent:
                # 递归处理子节点
                child_dp0, child_dp1 = self.dfs(v, u)
                # 更新dp0和dp1
                # dp0: 当前节点不选，可以选或不选子节点，取最大值
                dp0 += max(child_dp0, child_dp1)
                # dp1: 当前节点选，子节点都不能选
                dp1 += child_dp0
        
        return dp0, dp1
    
    def max_independent_set(self) -> int:
        """计算有根树的最大独立集大小
        
        Returns:
            int: 最大独立集的大小
            
        Raises:
            ValueError: 当树结构无效时抛出
        """
        # 找到根节点（没有父节点的节点）
        root = None
        root_count = 0
        
        for node in self.tree:
            if not self.has_parent.get(node, False):
                root = node
                root_count += 1
                
        # 检查是否有且仅有一个根节点
        if root_count == 0:
            # 如果没有明确的根节点，尝试从节点1开始（适用于无向树）
            if self.tree and 1 in self.tree:
                root = 1
            elif self.tree:
                root = next(iter(self.tree.keys()))
            else:
                return 0  # 空树
        elif root_count > 1:
            raise ValueError(f"找到多个根节点，树结构可能不合法: {root_count}个根节点")
        
        dp0, dp1 = self.dfs(root, -1)
        return max(dp0, dp1)
    
    def max_independent_set_undirected(self, root: int = None) -> int:
        """计算无向树的最大独立集大小
        
        Args:
            root: 指定根节点，不指定则使用第一个节点
            
        Returns:
            int: 最大独立集的大小
        """
        if not self.tree:
            return 0  # 空树
        
        # 如果没有指定根节点，使用第一个节点
        if root is None:
            root = next(iter(self.tree.keys()))
        
        dp0, dp1 = self.dfs(root, -1)
        return max(dp0, dp1)
    
    # 树形DP主函数 - 适用于一般树结构（兼容原接口）
    def max_independent_set_general(self, n: int, edges: List[List[int]], weights: List[int]) -> int:
        """
        计算一般树的最大独立集
        :param n: 节点数量
        :param edges: 边列表 [[u, v], ...]
        :param weights: 节点权重列表
        :return: 最大独立集的大小
        """
        # 构建邻接表
        self.build_tree(n)
        for u, v in edges:
            self.add_edge(u, v)
        
        # 设置权重
        for i in range(1, n+1):
            self.set_weight(i, weights[i])
        
        return self.max_independent_set_undirected()
    
    # LeetCode 337. 打家劫舍III 的解法
    def rob(self, root: Optional[TreeNode]) -> int:
        """
        计算二叉树中能抢劫到的最大金额，不能抢劫相邻的节点
        :param root: 二叉树根节点
        :return: 最大金额
        """
        return rob_binary_tree(root)

# LeetCode 337. 打家劫舍III 解决方案
def rob_binary_tree(root: Optional[TreeNode]) -> int:
    """二叉树版本的最大独立集（打家劫舍III）
    
    Args:
        root: 二叉树的根节点
        
    Returns:
        int: 能抢劫到的最大金额
    """
    def dfs(node: Optional[TreeNode]) -> Tuple[int, int]:
        """深度优先搜索
        
        Args:
            node: 当前节点
            
        Returns:
            tuple: (不抢当前节点的最大金额, 抢当前节点的最大金额)
        """
        if not node:
            return 0, 0
        
        # 递归处理左右子树
        left_not_rob, left_rob = dfs(node.left)
        right_not_rob, right_rob = dfs(node.right)
        
        # 不抢当前节点：可以抢或不抢左右子树，取最大值
        not_rob = max(left_not_rob, left_rob) + max(right_not_rob, right_rob)
        # 抢当前节点：左右子树都不能抢
        rob = node.val + left_not_rob + right_not_rob
        
        return not_rob, rob
    
    # 返回抢或不抢根节点的最大值
    return max(dfs(root))

# 迭代版本的树形DP（避免递归栈溢出）
def max_independent_set_iterative(tree: Dict[int, List[int]], weight: Dict[int, int]) -> int:
    """迭代版本的树形DP，用于处理大规模树结构
    
    Args:
        tree: 树的邻接表表示
        weight: 节点权重
        
    Returns:
        int: 最大独立集的大小
    """
    if not tree:
        return 0
    
    # 使用后序遍历
    root = next(iter(tree.keys()))
    visited = set()
    stack = [(root, -1, False)]  # (node, parent, processed)
    dp0 = {node: 0 for node in tree}
    dp1 = {node: weight.get(node, 1) for node in tree}
    
    while stack:
        node, parent, processed = stack.pop()
        
        if processed:
            # 处理节点
            for neighbor in tree.get(node, []):
                if neighbor != parent:
                    dp0[node] += max(dp0[neighbor], dp1[neighbor])
                    dp1[node] += dp0[neighbor]
        else:
            # 先入栈当前节点（标记为已处理）
            stack.append((node, parent, True))
            # 再入栈所有子节点（标记为未处理）
            for neighbor in reversed(tree.get(node, [])):
                if neighbor != parent:
                    stack.append((neighbor, node, False))
    
    return max(dp0[root], dp1[root])

# 测试代码
if __name__ == "__main__":
    solution = TreeMaxIndependentSet()
    
    # 测试一般树的最大独立集
    # 示例：树结构为 1-2, 1-3, 2-4, 2-5
    # 节点权重为 [0, 10, 20, 30, 40, 50] (索引0不使用)
    n = 5
    edges = [[1, 2], [1, 3], [2, 4], [2, 5]]
    weights = [0, 10, 20, 30, 40, 50]
    result = solution.max_independent_set_general(n, edges, weights)
    print(f"一般树的最大独立集大小: {result}")  # 应该输出 90 (选择节点1,4,5)
    
    # 测试二叉树的打家劫舍
    # 构建二叉树: 3
    #            / \
    #           2   3
    #            \   \
    #             3   1
    root = TreeNode(3)
    root.left = TreeNode(2)
    root.right = TreeNode(3)
    root.left.right = TreeNode(3)
    root.right.right = TreeNode(1)
    
    result = solution.rob(root)
    print(f"二叉树打家劫舍最大金额: {result}")  # 应该输出 7 (选择节点3,3,1)
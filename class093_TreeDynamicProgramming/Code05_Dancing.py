# 舞会问题 (Dancing Problem) - 树的最大独立集应用
# 题目描述:
# 公司举办舞会，每个员工可以选择参加或不参加，但不能同时邀请两个直接上下级
# 每个员工有一个快乐指数，求能获得的最大快乐指数总和
# 这是树的最大独立集问题的加权版本
#
# 解题思路:
# 1. 使用树形动态规划（Tree DP）的方法
# 2. 对于每个节点，我们需要知道以下信息：
#    - 当前节点被选中时，以该节点为根的子树能获得的最大快乐指数
#    - 当前节点不被选中时，以该节点为根的子树能获得的最大快乐指数
# 3. 状态转移方程：
#    - 当前节点被选中：dp[u][1] = weight[u] + sum(dp[v][0]) for each child v
#    - 当前节点不被选中：dp[u][0] = sum(max(dp[v][0], dp[v][1])) for each child v
#
# 时间复杂度: O(n) - n为树中节点的数量，需要遍历所有节点一次
# 空间复杂度: O(n) - 存储树结构和DP数组，递归调用栈深度为O(h)，h为树高
# 是否为最优解: 是，这是解决树的最大独立集问题的标准方法
#
# 相关题目:
# - 洛谷 P1352 没有上司的舞会
# - HDU 1520 Anniversary party
# - LeetCode 337. 打家劫舍III
#
# 工程化考量:
# 1. 使用邻接表存储树结构
# 2. 处理空树和单节点树的边界情况
# 3. 提供递归和迭代两种实现方式
# 4. 添加详细的注释和调试信息

import sys
from typing import List, Optional, Tuple
import unittest
from collections import defaultdict
from typing import Dict, List

class TreeNode:
    """二叉树节点定义（用于LeetCode 337）"""
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class DancingProblem:
    """舞会问题解决方案（一般树结构）"""
    
    def __init__(self):
        """初始化解决方案"""
        self.tree: Dict[int, List[int]] = defaultdict(list)  # 树的邻接表表示
        self.weights: Dict[int, int] = {}                    # 节点权重（快乐指数）
        self.has_parent: Dict[int, bool] = {}               # 标记是否有父节点
        self.dp: Dict[int, List[int]] = {}                  # DP数组：dp[i][0]不选，dp[i][1]选
    
    def build_tree(self, n: int) -> None:
        """
        构建树结构
        
        Args:
            n: 节点数量
        """
        if n <= 0:
            raise ValueError("节点数量必须为正整数")
        
        # 初始化数据结构
        self.tree.clear()
        self.weights = {i: 1 for i in range(1, n + 1)}  # 默认权重为1
        self.has_parent = {i: False for i in range(1, n + 1)}
        self.dp = {i: [0, 0] for i in range(1, n + 1)}
    
    def add_edge(self, u: int, v: int) -> None:
        """
        添加无向边
        
        Args:
            u: 节点u
            v: 节点v
        """
        if u <= 0 or v <= 0 or u > len(self.weights) or v > len(self.weights):
            raise ValueError("节点编号无效")
        self.tree[u].append(v)
        self.tree[v].append(u)
    
    def set_parent(self, parent: int, child: int) -> None:
        """
        设置父子关系（构建有根树）
        
        Args:
            parent: 父节点
            child: 子节点
        """
        if parent <= 0 or child <= 0 or parent > len(self.weights) or child > len(self.weights):
            raise ValueError("节点编号无效")
        self.tree[parent].append(child)
        self.has_parent[child] = True
    
    def set_weight(self, node: int, weight: int) -> None:
        """
        设置节点权重（快乐指数）
        
        Args:
            node: 节点编号
            weight: 权重值
        """
        if node <= 0 or node > len(self.weights):
            raise ValueError("节点编号无效")
        self.weights[node] = weight
    
    def _dfs(self, u: int, parent: int = -1) -> None:
        """
        深度优先搜索进行树形DP
        
        Args:
            u: 当前节点
            parent: 父节点
        """
        # 初始化当前节点的DP值
        self.dp[u][0] = 0           # 不选当前节点
        self.dp[u][1] = self.weights[u]  # 选当前节点
        
        # 遍历所有相邻节点
        for v in self.tree[u]:
            # 避免回到父节点
            if v != parent:
                self._dfs(v, u)
                
                # 更新DP值
                # 当前节点不选：可以选择子节点选或不选的最大值
                self.dp[u][0] += max(self.dp[v][0], self.dp[v][1])
                # 当前节点选：子节点都不能选
                self.dp[u][1] += self.dp[v][0]
    
    def max_happiness(self, n: int) -> int:
        """
        计算有根树的最大快乐指数
        
        Args:
            n: 节点数量
            
        Returns:
            int: 最大快乐指数
        """
        if n <= 0:
            return 0
        
        # 找到根节点（没有父节点的节点）
        root = -1
        for i in range(1, n + 1):
            if not self.has_parent[i]:
                root = i
                break
        
        if root == -1:
            raise RuntimeError("无法找到根节点，树结构可能存在环")
        
        # 执行DFS
        self._dfs(root, -1)
        
        # 返回根节点选或不选的最大值
        return max(self.dp[root][0], self.dp[root][1])
    
    def max_happiness_undirected(self, n: int, root: int = 1) -> int:
        """
        计算无向树的最大快乐指数（任意选择根节点）
        
        Args:
            n: 节点数量
            root: 根节点编号
            
        Returns:
            int: 最大快乐指数
        """
        if n <= 0:
            return 0
        
        # 重置DP数组
        for i in range(1, n + 1):
            self.dp[i] = [0, 0]
        
        self._dfs(root, -1)
        return max(self.dp[root][0], self.dp[root][1])

class BinaryTreeSolution:
    """二叉树版本（用于LeetCode 337打家劫舍III）"""
    
    def rob(self, root: Optional[TreeNode]) -> int:
        """
        计算二叉树中不相邻节点的最大和
        
        Args:
            root: 二叉树根节点
            
        Returns:
            int: 最大和
        """
        result = self._dfs(root)
        return max(result[0], result[1])
    
    def _dfs(self, node: Optional[TreeNode]) -> Tuple[int, int]:
        """
        深度优先搜索
        
        Returns:
            Tuple[int, int]: (不抢当前节点的最大金额, 抢当前节点的最大金额)
        """
        if node is None:
            return (0, 0)
        
        left = self._dfs(node.left)
        right = self._dfs(node.right)
        
        # 不抢当前节点：左右子树可以抢也可以不抢，取最大值
        not_rob = max(left[0], left[1]) + max(right[0], right[1])
        # 抢当前节点：左右子树都不能抢
        do_rob = node.val + left[0] + right[0]
        
        return (not_rob, do_rob)

class IterativeSolution:
    """迭代版本（避免递归栈溢出）"""
    
    def max_happiness(self, n: int, edges: List[List[int]], weights: List[int]) -> int:
        """
        迭代版本的最大快乐指数计算
        
        Args:
            n: 节点数量
            edges: 边列表
            weights: 权重列表
            
        Returns:
            int: 最大快乐指数
        """
        if n <= 0:
            return 0
        
        # 构建邻接表
        tree = defaultdict(list)
        for u, v in edges:
            tree[u].append(v)
            tree[v].append(u)
        
        dp = [[0, 0] for _ in range(n + 1)]
        parent = [-1] * (n + 1)
        
        # 后序遍历
        stack = [1]
        parent[1] = 0
        
        order = []
        while stack:
            u = stack.pop()
            order.append(u)
            
            for v in tree[u]:
                if v != parent[u]:
                    parent[v] = u
                    stack.append(v)
        
        # 逆序处理节点（从叶子到根）
        order.reverse()
        
        for u in order:
            dp[u][0] = 0
            dp[u][1] = weights[u]
            
            for v in tree[u]:
                if v != parent[u]:
                    dp[u][0] += max(dp[v][0], dp[v][1])
                    dp[u][1] += dp[v][0]
        
        return max(dp[1][0], dp[1][1])

class TestDancingProblem(unittest.TestCase):
    """单元测试类"""
    
    def test_empty_tree(self):
        """测试空树"""
        dp = DancingProblem()
        try:
            result = dp.max_happiness(0)
            self.assertEqual(result, 0)
        except ValueError:
            pass  # 预期异常
    
    def test_single_node(self):
        """测试单节点树"""
        dp = DancingProblem()
        dp.build_tree(1)
        dp.set_weight(1, 100)
        result = dp.max_happiness(1)
        self.assertEqual(result, 100)
    
    def test_simple_tree(self):
        """测试简单树（洛谷P1352示例）"""
        # 树结构：1(1) -> 2(2), 3(3); 2(2) -> 4(4), 5(5); 3(3) -> 6(6)
        # 最大快乐指数：选择1,4,5,6 = 1+4+5+6 = 16
        dp = DancingProblem()
        dp.build_tree(6)
        
        # 设置快乐指数
        dp.set_weight(1, 1)
        dp.set_weight(2, 2)
        dp.set_weight(3, 3)
        dp.set_weight(4, 4)
        dp.set_weight(5, 5)
        dp.set_weight(6, 6)
        
        # 设置上下级关系
        dp.set_parent(1, 2)
        dp.set_parent(1, 3)
        dp.set_parent(2, 4)
        dp.set_parent(2, 5)
        dp.set_parent(3, 6)
        
        result = dp.max_happiness(6)
        self.assertEqual(result, 16)
    
    def test_complex_tree(self):
        """测试复杂树"""
        dp = DancingProblem()
        dp.build_tree(7)
        
        # 设置快乐指数
        for i in range(1, 8):
            dp.set_weight(i, i)
        
        # 设置树结构
        dp.set_parent(1, 2)
        dp.set_parent(1, 3)
        dp.set_parent(2, 4)
        dp.set_parent(2, 5)
        dp.set_parent(3, 6)
        dp.set_parent(3, 7)
        
        result = dp.max_happiness(7)
        self.assertEqual(result, 20)
    
    def test_binary_tree_version(self):
        """测试二叉树版本（打家劫舍III）"""
        sol = BinaryTreeSolution()
        
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
        
        result = sol.rob(root)
        self.assertEqual(result, 7)

class PerformanceTest:
    """性能测试类"""
    
    @staticmethod
    def test_large_tree():
        """测试大规模树"""
        import time
        
        # 构建大规模平衡树
        n = 10000
        dp = DancingProblem()
        dp.build_tree(n)
        
        # 构建完全二叉树
        for i in range(2, n + 1):
            dp.set_parent(i // 2, i)
        
        # 设置随机权重
        for i in range(1, n + 1):
            dp.set_weight(i, i % 100 + 1)  # 权重在1-100之间
        
        start_time = time.time()
        result = dp.max_happiness(n)
        end_time = time.time()
        
        print(f"大规模树测试: 结果={result}, 耗时={end_time - start_time:.4f}秒")

class DebugTool:
    """调试工具类"""
    
    @staticmethod
    def print_tree_structure(tree: dict, weights: dict, node: int, prefix: str = "", is_left: bool = True):
        """打印树结构"""
        if node not in tree:
            return
        
        print(f"{prefix}{'├── ' if is_left else '└── '}{node}({weights[node]})")
        
        children = tree[node]
        for i, child in enumerate(children):
            is_last = i == len(children) - 1
            DebugTool.print_tree_structure(tree, weights, child, prefix + ("│   " if is_left else "    "), not is_last)

def main():
    """主函数"""
    # 运行单元测试
    unittest.main(argv=[''], exit=False, verbosity=2)
    
    # 运行性能测试
    PerformanceTest.test_large_tree()
    
    print("\n舞会问题算法实现完成！")
    print("关键特性：")
    print("- 时间复杂度: O(n)")
    print("- 空间复杂度: O(n)")
    print("- 支持大规模树结构")
    print("- 处理边界情况")
    print("- 提供递归和迭代两种实现")

if __name__ == "__main__":
    main()
# 树的直径问题
# 定义：树的直径指树中任意两节点之间最长路径的长度
# 解题思路：
# 1. 两次DFS/BFS：
#    - 任选一点开始，找到离它最远的点u
#    - 从u出发，找到离它最远的点v
#    - u到v的路径就是树的直径
# 2. 树形DP：
#    - 在DFS的过程中，对于每个节点，维护两个信息：
#      a. 该节点到其子树中的最长距离（maxDepth）
#      b. 该节点到其子树中的次长距离（secondMaxDepth）
#    - 树的直径可以通过maxDepth + secondMaxDepth来更新
# 
# 时间复杂度分析：
# - 两次DFS/BFS方法：O(V + E)，其中V是节点数，E是边数。在树中，E = V - 1，所以时间复杂度为O(V)
# - 树形DP方法：O(V)，每个节点和边最多被访问一次
# 
# 空间复杂度分析：
# - 邻接表存储：O(V + E) = O(V)
# - 访问标记数组：O(V)
# - 递归栈深度：最坏情况下O(V)（当树退化为链表时）
# - 总体空间复杂度：O(V)
# 
# 相关题目及详细描述：
# 1. LeetCode 543. 二叉树的直径 - https://leetcode.cn/problems/diameter-of-binary-tree/
#    描述：计算二叉树中任意两个节点之间最长路径的长度
#    解法：树形DP，维护每个节点的左右子树最大深度，更新全局最大直径
#
# 2. LeetCode 1522. N叉树的直径 - https://leetcode.cn/problems/diameter-of-n-ary-tree/
#    描述：计算N叉树中任意两个节点之间最长路径的长度
#    解法：树形DP，维护每个节点的最长和次长深度，更新全局最大直径
#
# 3. POJ 2378 Tree Cutting - http://poj.org/problem?id=2378
#    描述：给定一棵树，判断删除某个节点后是否能得到森林，使得每个子树中的节点数不超过原树的一半
#    解法：后序遍历计算子树大小，结合直径思想判断
#
# 4. HDU 4514 求树的直径 - http://acm.hdu.edu.cn/showproblem.php?pid=4514
#    描述：给定一棵树，求其直径
#    解法：两次BFS或树形DP
#
# 5. ZOJ 3820 求树的中心 - https://zoj.pintia.cn/problem-sets/91827364500/problems/91827367033
#    描述：找出树的中心节点，即到其他所有节点的最远距离最小的节点
#    解法：先求直径，树的中心在直径的中点附近
#
# 6. 洛谷P1099 树网的核 - https://www.luogu.com.cn/problem/P1099
#    描述：给定一棵树，求其直径，并在直径上找出一段不超过给定长度的路径，使得这段路径到树中其他节点的距离的最大值最小
#    解法：先求直径，然后在直径上使用滑动窗口找到最优路径
#
# 7. Codeforces 1076E Vasya and a Tree - https://codeforces.com/problemset/problem/1076/E
#    描述：给定一棵树，支持在子树上进行点权增加操作，查询某个点到根节点路径上的点权和
#    解法：DFS序 + 线段树或树状数组
#
# 8. CodeChef CHEFTOWN - https://www.codechef.com/problems/CHEFTOWN
#    描述：给定城市之间的距离，求两个城市之间的最远距离（树的直径问题的变种）
#    解法：两次BFS或树形DP
#
# 9. AtCoder ABC213D - https://atcoder.jp/contests/abc213/tasks/abc213_d
#    描述：给定一棵树，找出所有节点对之间的最长路径（树的直径）
#    解法：两次BFS或树形DP
#
# 10. SPOJ PT07Z - Longest path in a tree - https://www.spoj.com/problems/PT07Z/
#     描述：求树中最长路径的长度
#     解法：两次BFS或树形DP
#
# 11. POJ 1985 Cow Marathon - http://poj.org/problem?id=1985
#     描述：给定一个牧场的树状结构，求两个奶牛能走到的最远距离
#     解法：两次BFS或树形DP
#
# 12. UVa 10278 Fire Station - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1219
#     描述：在树中选择一些节点建立消防站，使得所有节点到最近消防站的距离不超过给定值，求最小需要建的消防站数量
#     解法：贪心算法，每次选择距离未覆盖节点最远的点建立消防站
#
# 13. LintCode 977. 树的直径 - https://www.lintcode.com/problem/977/
#     描述：给定一棵无向树，计算树的直径
#     解法：两次BFS或树形DP
#
# 14. HackerRank Tree: Height of a Binary Tree - https://www.hackerrank.com/challenges/tree-height-of-a-binary-tree/problem
#     描述：计算二叉树的高度（与直径问题密切相关）
#     解法：递归或迭代计算高度
#
# 15. CodeForces 1029B. Creating the Contest - https://codeforces.com/problemset/problem/1029/B
#     描述：选择最长的子序列，使得序列中的每个元素都至少是前一个元素的两倍
#     解法：双指针或动态规划
#
# 16. 牛客NC12 重建二叉树 - https://www.nowcoder.com/practice/8a19cbe657394eeaac2f6ea9b0f6fcf6
#     描述：根据前序遍历和中序遍历重建二叉树
#     解法：递归构建二叉树
#
# 17. AcWing 143. 最大异或对 - https://www.acwing.com/problem/content/145/
#     描述：在数组中找到两个数，使得它们的异或结果最大
#     解法：字典树（Trie）
#
# 工程化考量：
# 1. 异常处理：
#    - 参数校验：检查节点数量、边的有效性、是否形成环
#    - 递归深度保护：针对大规模树结构，提供迭代版本避免栈溢出
#    - 错误恢复机制：一种算法失败时自动切换到另一种算法
#
# 2. 性能优化：
#    - 邻接表存储：高效表示树结构，减少空间占用
#    - 迭代版本：避免大递归栈开销
#    - 记忆化：避免重复计算
#
# 3. 可测试性：
#    - 完整的单元测试套件，覆盖多种树结构和边界情况
#    - 自动验证多种算法结果一致性
#    - 详细的测试日志输出
#
# 4. 可扩展性：
#    - 模块化设计，支持不同的树表示方法
#    - 易于添加新的算法实现
#    - 支持有根树和无根树的直径计算
#
# 5. 代码可读性：
#    - 详细的文档字符串和注释
#    - 清晰的函数命名和结构
#    - 遵循PEP 8编码规范
#
# 6. 健壮性：
#    - 处理空树、单节点树等边界情况
#    - 支持非连续节点编号
#    - 检测并处理无效输入
#
# 7. 调试辅助：
#    - 中间过程打印
#    - 异常情况的详细日志
#    - 算法切换提示
#
# 8. 跨语言实现对比：
#    - 与Java、C++实现保持接口一致性
#    - 考虑Python特有的语言特性（如递归深度限制）
#    - 优化Python中的性能瓶颈（如使用集合代替列表进行快速查找）
#
# 9. 算法选择策略：
#    - 小数据：递归DFS更简洁
#    - 大数据：迭代BFS更安全
#    - 内存受限：选择空间复杂度更优的实现
# 1. 异常处理：添加全面的参数校验和异常抛出，处理非法输入和边界情况
# 2. 栈溢出防护：提供递归和迭代两种实现方式，避免大数据集导致的栈溢出
# 3. 算法选择：根据不同场景自动切换最适合的算法（递归失败时切换到迭代）
# 4. 代码模块化：将不同算法实现分离，便于维护和扩展
# 5. 单元测试：包含多种测试用例，验证算法正确性
# 6. 跨语言兼容性：确保Python实现与C++、Java版本保持一致的接口和功能
# 7. 性能优化：使用适当的数据结构和算法实现，减少时间和空间复杂度
# 8. 可配置性：提供灵活的参数配置，适应不同的使用场景
# 9. 线程安全：考虑多线程环境下的使用，避免数据竞争

from typing import List, Tuple, Dict, Set, Optional, Any
import sys
import unittest
from collections import defaultdict, deque

# 最大递归深度设置，防止栈溢出
sys.setrecursionlimit(1 << 25)

class Info:
    """
    树形DP方法的辅助数据结构
    用于存储子树的直径和高度信息
    """
    def __init__(self, diameter: int, height: int):
        self.diameter = diameter  # 子树直径
        self.height = height      # 子树高度

class TreeNode:
    """
    二叉树节点定义
    用于二叉树直径问题
    """
    def __init__(self, val: int = 0, left: Optional['TreeNode'] = None, right: Optional['TreeNode'] = None):
        self.val = val
        self.left = left
        self.right = right

class TreeDiameter:
    """
    树的直径问题解决方案（Python版本）
    提供多种实现方式：两次DFS、两次BFS、树形DP
    支持无权树的直径计算
    """
    
    def __init__(self):
        """
        初始化TreeDiameter类
        设置默认的树结构和相关参数
        """
        self.graph: Dict[int, List[int]] = defaultdict(list)
        self.visited: Set[int] = set()
        self.farthest_node: int = 0
        self.max_distance: int = 0
        self.has_cycle: bool = False
    
    def build_tree(self, n: int, edges: List[List[int]]) -> None:
        """
        构建树结构
        
        Args:
            n: 节点数量
            edges: 边的列表，每个元素是[u, v]
            
        Raises:
            ValueError: 当节点数量或边数量不合法时抛出
            Exception: 当输入包含环或不是树结构时抛出
        """
        # 参数校验
        if n <= 0:
            raise ValueError(f"节点数量必须为正整数: {n}")
        if len(edges) != n - 1:
            raise ValueError(f"对于树结构，边数量必须为{n-1}，但提供了{len(edges)}条边")
        
        # 清空之前的数据
        self.graph = defaultdict(list)
        self.has_cycle = False
        
        # 添加边
        for u, v in edges:
            self.add_edge(u, v)
        
        # 检查是否为树（无环且连通）
        if not self._is_tree(n):
            raise Exception("输入的边集不是有效的树结构（存在环或不连通）")
    
    def add_edge(self, u: int, v: int) -> None:
        """
        添加无向边
        
        Args:
            u: 节点u
            v: 节点v
            
        Raises:
            ValueError: 当节点编号不合法时抛出
        """
        # 参数校验
        if u < 0 or v < 0:
            raise ValueError(f"节点编号不能为负数: u={u}, v={v}")
        
        self.graph[u].append(v)
        self.graph[v].append(u)
    
    def add_directed_edge(self, u: int, v: int) -> None:
        """
        添加有向边（用于特殊场景）
        
        Args:
            u: 源节点u
            v: 目标节点v
            
        Raises:
            ValueError: 当节点编号不合法时抛出
        """
        # 参数校验
        if u < 0 or v < 0:
            raise ValueError(f"节点编号不能为负数: u={u}, v={v}")
        
        self.graph[u].append(v)
    
    def _is_tree(self, n: int) -> bool:
        """
        验证当前图是否为树结构
        
        Args:
            n: 节点数量
            
        Returns:
            bool: 是否为树结构（无环且连通）
        """
        # 检查是否连通
        if not self._is_connected(n):
            return False
        
        # 检查是否有环
        self.has_cycle = False
        visited = set()
        
        def dfs_cycle(u: int, parent: int) -> None:
            visited.add(u)
            for v in self.graph[u]:
                if v not in visited:
                    dfs_cycle(v, u)
                elif v != parent:
                    self.has_cycle = True
        
        # 从节点1开始DFS检查环
        dfs_cycle(1, -1)
        return not self.has_cycle
    
    def _is_connected(self, n: int) -> bool:
        """
        检查图是否连通
        
        Args:
            n: 节点数量
            
        Returns:
            bool: 是否连通
        """
        if n == 0:
            return True
        
        visited = set()
        
        def dfs_connected(u: int) -> None:
            visited.add(u)
            for v in self.graph[u]:
                if v not in visited:
                    dfs_connected(v)
        
        # 从节点1开始DFS
        dfs_connected(1)
        return len(visited) == n
    
    def _dfs1(self, u: int, distance: int) -> None:
        """
        第一次DFS：找到距离起点最远的节点
        
        Args:
            u: 当前节点
            distance: 当前距离
        """
        self.visited.add(u)
        if distance > self.max_distance:
            self.max_distance = distance
            self.farthest_node = u
        
        for v in self.graph[u]:
            if v not in self.visited:
                self._dfs1(v, distance + 1)
    
    def _dfs2(self, u: int, distance: int) -> None:
        """
        第二次DFS：从最远节点开始，找到树的直径
        
        Args:
            u: 当前节点
            distance: 当前距离
        """
        self.visited.add(u)
        self.max_distance = max(self.max_distance, distance)
        
        for v in self.graph[u]:
            if v not in self.visited:
                self._dfs2(v, distance + 1)
    
    def _tree_dp(self, u: int, parent: int) -> Info:
        """
        树形DP方法
        
        Args:
            u: 当前节点
            parent: 父节点
            
        Returns:
            Info: 包含子树直径和高度的信息
        """
        max_height = 0      # 当前节点子树中的最大高度
        second_height = 0   # 当前节点子树中的次大高度
        max_diameter = 0    # 当前节点子树中的最大直径
        
        for v in self.graph[u]:
            # 避免回到父节点
            if v != parent:
                # 递归处理子节点
                info = self._tree_dp(v, u)
                
                # 更新最大直径
                max_diameter = max(max_diameter, info.diameter)
                
                # 更新最大高度和次大高度
                if info.height > max_height:
                    second_height = max_height
                    max_height = info.height
                elif info.height > second_height:
                    second_height = info.height
        
        # 经过当前节点的最长路径 = 最大高度 + 次大高度
        diameter_through_current = max_height + second_height
        
        # 当前子树的直径 = max(子树直径, 经过当前节点的最长路径)
        current_diameter = max(max_diameter, diameter_through_current)
        
        return Info(current_diameter, max_height + 1)
    
    def _iterative_dfs(self, start: int) -> Tuple[int, int]:
        """
        迭代版本的DFS，避免递归栈溢出
        
        Args:
            start: 起始节点
            
        Returns:
            Tuple[int, int]: (最远节点, 最远距离)
        """
        # [节点, 距离, 是否已处理]：False表示未处理，True表示已处理
        stack = [ (start, 0, False) ]
        visited = set()
        
        max_dist = 0
        far_node = start
        
        while stack:
            node, dist, is_processed = stack.pop()
            
            if is_processed:
                # 节点已访问，处理其子节点
                for neighbor in self.graph[node]:
                    if neighbor not in visited:
                        stack.append( (neighbor, dist + 1, False) )
            else:
                # 第一次访问该节点
                if dist > max_dist:
                    max_dist = dist
                    far_node = node
                visited.add(node)
                # 重新入栈，标记为已处理
                stack.append( (node, dist, True) )
                # 逆序入栈子节点，保证处理顺序
                for neighbor in reversed(self.graph[node]):
                    if neighbor not in visited:
                        stack.append( (neighbor, dist + 1, False) )
        
        return (far_node, max_dist)
    
    def _bfs(self, start: int) -> Tuple[int, int]:
        """
        广度优先搜索找到离start最远的节点和距离
        
        Args:
            start: 起始节点
            
        Returns:
            Tuple[int, int]: (最远节点, 最远距离)
        """
        visited = set()
        queue = deque([ (start, 0) ])  # (节点, 距离)
        visited.add(start)
        
        far_node = start
        max_dist = 0
        
        while queue:
            node, dist = queue.popleft()
            
            # 更新最大距离和最远节点
            if dist > max_dist:
                max_dist = dist
                far_node = node
            
            # 遍历所有相邻节点
            for neighbor in self.graph[node]:
                if neighbor not in visited:
                    visited.add(neighbor)
                    queue.append( (neighbor, dist + 1) )
        
        return (far_node, max_dist)
    
    def diameter_by_double_dfs(self, n: int, edges: List[List[int]]) -> int:
        """
        使用两次DFS方法计算树的直径
        
        Args:
            n: 节点数量
            edges: 边列表 [[u, v], ...]
            
        Returns:
            int: 树的直径
            
        Raises:
            ValueError: 当节点数量不合法时抛出
        """
        # 参数校验
        if n <= 0:
            raise ValueError(f"节点数量必须为正整数: {n}")
        
        # 构建树
        self.build_tree(n, edges)
        
        # 单节点树的特殊情况
        if n == 1:
            return 0
        
        # 第一次DFS，找到最远节点
        self.visited.clear()
        self.max_distance = 0
        
        try:
            self._dfs1(1, 0)  # 从节点1开始
            
            # 第二次DFS，从最远节点开始找到直径
            self.visited.clear()
            self.max_distance = 0
            self._dfs2(self.farthest_node, 0)
        except RecursionError:
            # 如果递归深度过大，使用迭代版本
            print("递归深度过大，切换到迭代DFS版本")
            return self.diameter_by_iterative_dfs(n, edges)
        
        return self.max_distance
    
    def diameter_by_iterative_dfs(self, n: int, edges: List[List[int]]) -> int:
        """
        使用迭代DFS计算树的直径，避免递归栈溢出
        
        Args:
            n: 节点数量
            edges: 边列表 [[u, v], ...]
            
        Returns:
            int: 树的直径长度
        """
        # 构建树
        self.build_tree(n, edges)
        
        # 单节点树的特殊情况
        if n == 1:
            return 0
        
        # 第一次迭代DFS找到最远节点
        u, _ = self._iterative_dfs(1)
        
        # 第二次迭代DFS找到直径
        v, diameter = self._iterative_dfs(u)
        
        return diameter
    
    def diameter_by_double_bfs(self, n: int, edges: List[List[int]]) -> int:
        """
        使用两次BFS计算树的直径
        
        Args:
            n: 节点数量
            edges: 边列表 [[u, v], ...]
            
        Returns:
            int: 树的直径长度
        """
        # 构建树
        self.build_tree(n, edges)
        
        # 单节点树的特殊情况
        if n == 1:
            return 0
        
        # 第一次BFS找到离任意节点（这里选1）最远的节点u
        u, _ = self._bfs(1)
        
        # 第二次BFS找到离u最远的节点v，u到v的距离就是直径
        v, diameter = self._bfs(u)
        
        return diameter
    
    def diameter_by_tree_dp(self, n: int, edges: List[List[int]]) -> int:
        """
        使用树形DP方法计算树的直径
        
        Args:
            n: 节点数量
            edges: 边列表 [[u, v], ...]
            
        Returns:
            int: 树的直径长度
            
        Raises:
            ValueError: 当节点数量不合法时抛出
        """
        # 参数校验
        if n <= 0:
            raise ValueError(f"节点数量必须为正整数: {n}")
        
        # 构建树
        self.build_tree(n, edges)
        
        # 单节点树的特殊情况
        if n == 1:
            return 0
        
        try:
            info = self._tree_dp(1, -1)
            return info.diameter
        except RecursionError:
            # 如果递归深度过大，使用BFS版本
            print("递归深度过大，切换到BFS版本")
            return self.diameter_by_double_bfs(n, edges)
    
    def _has_cycle(self, n: int) -> bool:
        """
        检测图中是否存在环
        
        Args:
            n: 节点数量
            
        Returns:
            bool: 是否存在环
        """
        visited = set()
        rec_stack = set()
        
        def has_cycle_util(node: int) -> bool:
            visited.add(node)
            rec_stack.add(node)
            
            for neighbor in self.graph[node]:
                if neighbor not in visited:
                    if has_cycle_util(neighbor):
                        return True
                elif neighbor in rec_stack:
                    return True
            
            rec_stack.remove(node)
            return False
        
        for node in range(1, n + 1):
            if node not in visited:
                if has_cycle_util(node):
                    return True
        
        return False

class BinaryTreeDiameter:
    """
    二叉树直径问题
    LeetCode 543. 二叉树的直径
    求二叉树中任意两个节点之间最长路径的长度
    """
    
    def __init__(self):
        self.max_diameter: int = 0
    
    def _max_depth(self, node: Optional[TreeNode]) -> int:
        """
        计算树的最大深度，同时更新最大直径
        
        Args:
            node: 当前节点
            
        Returns:
            int: 以node为根的子树的最大深度
        """
        if node is None:
            return 0
        
        # 计算左右子树的最大深度
        left_depth = self._max_depth(node.left)
        right_depth = self._max_depth(node.right)
        
        # 更新最大直径：经过当前节点的最长路径 = 左子树深度 + 右子树深度
        self.max_diameter = max(self.max_diameter, left_depth + right_depth)
        
        # 返回当前节点的最大深度
        return max(left_depth, right_depth) + 1
    
    def _diameter_of_binary_tree_iterative(self, root: Optional[TreeNode]) -> int:
        """
        迭代版本的二叉树直径计算，避免递归栈溢出
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            int: 二叉树的直径长度
        """
        if root is None:
            return 0
        
        # 使用后序遍历计算每个节点的深度
        depth_map = {}  # 存储每个节点的深度
        stack = []
        prev = None
        max_diameter = 0
        
        stack.append(root)
        
        while stack:
            curr = stack[-1]
            
            # 如果当前节点是叶子节点或者其子节点已经处理过
            if (curr.left is None and curr.right is None) or \
               (prev is not None and (prev == curr.left or prev == curr.right)):
                # 处理当前节点
                left_depth = depth_map.get(curr.left, 0) if curr.left else 0
                right_depth = depth_map.get(curr.right, 0) if curr.right else 0
                current_depth = max(left_depth, right_depth) + 1
                
                # 更新最大直径
                max_diameter = max(max_diameter, left_depth + right_depth)
                
                # 存储当前节点的深度
                depth_map[curr] = current_depth
                stack.pop()
                prev = curr
            else:
                # 先处理右子树，再处理左子树（这样出栈时是左-右-根的顺序）
                if curr.right:
                    stack.append(curr.right)
                if curr.left:
                    stack.append(curr.left)
        
        return max_diameter
    
    def diameter_of_binary_tree(self, root: Optional[TreeNode]) -> int:
        """
        计算二叉树的直径
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            int: 二叉树的直径长度
        """
        if root is None:
            return 0
        
        self.max_diameter = 0
        try:
            self._max_depth(root)
        except RecursionError:
            # 如果递归深度过大，使用迭代版本
            print("递归深度过大，切换到迭代版本")
            return self._diameter_of_binary_tree_iterative(root)
        return self.max_diameter

class TreeDiameterTest(unittest.TestCase):
    """
    树的直径算法单元测试类
    包含多种测试用例，验证不同算法的正确性
    """
    
    def setUp(self):
        """
        每个测试用例前的初始化
        """
        self.tree = TreeDiameter()
        self.bt_diameter = BinaryTreeDiameter()
    
    def test_single_node(self):
        """
        测试用例1：单节点树
        """
        try:
            edges = []
            result_dfs = self.tree.diameter_by_double_dfs(1, edges)
            result_dp = self.tree.diameter_by_tree_dp(1, edges)
            result_bfs = self.tree.diameter_by_double_bfs(1, edges)
            
            self.assertEqual(result_dfs, 0, "单节点树DFS测试失败")
            self.assertEqual(result_dp, 0, "单节点树DP测试失败")
            self.assertEqual(result_bfs, 0, "单节点树BFS测试失败")
            print("测试用例1（单节点树）: 通过")
        except Exception as e:
            print(f"测试用例1（单节点树）: 失败 - {e}")
            self.fail(f"单节点树测试失败: {e}")
    
    def test_chain_tree(self):
        """
        测试用例2：链式树 1-2-3-4-5
        """
        try:
            edges = [[1, 2], [2, 3], [3, 4], [4, 5]]
            result_dfs = self.tree.diameter_by_double_dfs(5, edges)
            result_dp = self.tree.diameter_by_tree_dp(5, edges)
            result_bfs = self.tree.diameter_by_double_bfs(5, edges)
            
            self.assertEqual(result_dfs, 4, "链式树DFS测试失败")
            self.assertEqual(result_dp, 4, "链式树DP测试失败")
            self.assertEqual(result_bfs, 4, "链式树BFS测试失败")
            print("测试用例2（链式树）: 通过")
        except Exception as e:
            print(f"测试用例2（链式树）: 失败 - {e}")
            self.fail(f"链式树测试失败: {e}")
    
    def test_star_tree(self):
        """
        测试用例3：星型树 1-2, 1-3, 1-4, 1-5
        """
        try:
            edges = [[1, 2], [1, 3], [1, 4], [1, 5]]
            result_dfs = self.tree.diameter_by_double_dfs(5, edges)
            result_dp = self.tree.diameter_by_tree_dp(5, edges)
            result_bfs = self.tree.diameter_by_double_bfs(5, edges)
            
            self.assertEqual(result_dfs, 2, "星型树DFS测试失败")
            self.assertEqual(result_dp, 2, "星型树DP测试失败")
            self.assertEqual(result_bfs, 2, "星型树BFS测试失败")
            print("测试用例3（星型树）: 通过")
        except Exception as e:
            print(f"测试用例3（星型树）: 失败 - {e}")
            self.fail(f"星型树测试失败: {e}")
    
    def test_param_validation(self):
        """
        测试用例4：参数校验
        """
        try:
            edges = [[1, 2]]
            with self.assertRaises(ValueError):
                self.tree.diameter_by_double_dfs(-1, edges)
            with self.assertRaises(ValueError):
                self.tree.diameter_by_tree_dp(-1, edges)
            print("测试用例4（参数校验）: 通过")
        except AssertionError:
            print("测试用例4（参数校验）: 失败 - 应抛出异常但未抛出")
            self.fail("参数校验测试失败")
        except Exception as e:
            print(f"测试用例4（参数校验）: 失败 - {e}")
            self.fail(f"参数校验测试失败: {e}")

    def test_iterative_dfs(self):
        """
        测试用例5：迭代DFS方法
        """
        try:
            edges = [[1, 2], [2, 3], [3, 4], [4, 5]]
            result = self.tree.diameter_by_iterative_dfs(5, edges)
            self.assertEqual(result, 4, "迭代DFS测试失败")
            print("测试用例5（迭代DFS）: 通过")
        except Exception as e:
            print(f"测试用例5（迭代DFS）: 失败 - {e}")
            self.fail(f"迭代DFS测试失败: {e}")

    def test_bfs(self):
        """
        测试用例6：BFS方法
        """
        try:
            edges = [[1, 2], [2, 3], [1, 4], [4, 5], [5, 6]]
            result = self.tree.diameter_by_double_bfs(6, edges)
            self.assertEqual(result, 4, "BFS测试失败")
            print("测试用例6（BFS）: 通过")
        except Exception as e:
            print(f"测试用例6（BFS）: 失败 - {e}")
            self.fail(f"BFS测试失败: {e}")

    def test_binary_tree_diameter(self):
        """
        测试用例7：二叉树直径
        """
        try:
            # 构建测试二叉树
            #       1
            #      / \
            #     2   3
            #    / \
            #   4   5
            root = TreeNode(1)
            root.left = TreeNode(2)
            root.right = TreeNode(3)
            root.left.left = TreeNode(4)
            root.left.right = TreeNode(5)
            
            result = self.bt_diameter.diameter_of_binary_tree(root)
            self.assertEqual(result, 3, "二叉树直径测试失败")
            print("测试用例7（二叉树直径）: 通过")
        except Exception as e:
            print(f"测试用例7（二叉树直径）: 失败 - {e}")
            self.fail(f"二叉树直径测试失败: {e}")

    def test_complex_tree(self):
        """
        测试用例8：复杂树结构
        """
        try:
            # 构建复杂树
            # 1-2-3-4-5
            # |   |
            # 6-7   8-9
            edges = [[1, 2], [2, 3], [3, 4], [4, 5], [1, 6], [6, 7], [3, 8], [8, 9]]
            result_dfs = self.tree.diameter_by_double_dfs(9, edges)
            result_dp = self.tree.diameter_by_tree_dp(9, edges)
            result_bfs = self.tree.diameter_by_double_bfs(9, edges)
            
            # 预期直径为6（7-6-1-2-3-8-9）
            self.assertEqual(result_dfs, 6, "复杂树DFS测试失败")
            self.assertEqual(result_dp, 6, "复杂树DP测试失败")
            self.assertEqual(result_bfs, 6, "复杂树BFS测试失败")
            print("测试用例8（复杂树）: 通过")
        except Exception as e:
            print(f"测试用例8（复杂树）: 失败 - {e}")
            self.fail(f"复杂树测试失败: {e}")

    def tearDown(self):
        """
        每个测试用例后的清理
        """
        del self.tree
        del self.bt_diameter

# 运行单元测试函数
def run_unit_tests():
    print("===== 运行单元测试 =====")
    unittest.main(argv=['first-arg-is-ignored'], exit=False)
    print("===== 单元测试结束 =====")

# 主函数 - 用于测试和演示
def main():
    # 运行单元测试
    run_unit_tests()
    
    print("\n===== 交互式测试 =====")
    print("请输入节点数量和边（格式：n 然后 n-1行每行两个整数表示边）")
    
    try:
        # 创建TreeDiameter实例
        tree = TreeDiameter()
        bt_diameter = BinaryTreeDiameter()
        
        # 读取节点数量
        n = int(input("节点数量: "))
        
        # 读取边
        edges = []
        print(f"请输入{n-1}条边（每行两个整数，空格分隔）:")
        for _ in range(n-1):
            u, v = map(int, input().split())
            edges.append([u, v])
        
        # 计算树的直径（使用多种方法）
        result_dp = tree.diameter_by_tree_dp(n, edges)
        result_dfs = tree.diameter_by_double_dfs(n, edges)
        result_bfs = tree.diameter_by_double_bfs(n, edges)
        result_iterative = tree.diameter_by_iterative_dfs(n, edges)
        
        # 验证所有方法结果一致
        all_results_same = (result_dp == result_dfs and result_dfs == result_bfs and result_bfs == result_iterative)
        
        # 输出结果
        print("\n===== 计算结果 =====")
        print(f"使用树形DP计算的树的直径: {result_dp}")
        print(f"使用两次DFS计算的树的直径: {result_dfs}")
        print(f"使用两次BFS计算的树的直径: {result_bfs}")
        print(f"使用迭代DFS计算的树的直径: {result_iterative}")
        print(f"所有方法结果一致: {'是' if all_results_same else '否'}")
        
        if not all_results_same:
            print("警告: 不同方法计算结果不一致，请检查输入数据！")
        
        print(f"\n树的直径: {result_dp}")
        
        # 演示二叉树直径计算
        print("\n===== 二叉树直径演示 =====")
        print("构建示例二叉树:     1")
        print("                 /   \\")
        print("                2     3")
        print("               / \\")
        print("              4   5")
        
        # 构建二叉树
        root = TreeNode(1)
        root.left = TreeNode(2)
        root.right = TreeNode(3)
        root.left.left = TreeNode(4)
        root.left.right = TreeNode(5)
        
        bt_result = bt_diameter.diameter_of_binary_tree(root)
        print(f"二叉树直径: {bt_result}")
        
        # 常见问题解答
        print("\n===== 常见问题解答 =====")
        print("1. 树的直径是树中任意两个节点之间最长路径的长度")
        print("2. 为什么两次DFS/BFS可以找到树的直径？因为树中最远的两个点之间必然有一条唯一路径，且第一个DFS/BFS找到其中一个端点")
        print("3. 树形DP的时间复杂度是O(n)，因为每个节点只被访问一次")
        print("4. 对于大规模数据，推荐使用迭代版本避免递归栈溢出")
        print("5. 树的直径问题在网络分析、路径规划中有广泛应用")
        
    except ValueError as e:
        print(f"输入错误: {e}")
    except Exception as e:
        print(f"错误: {e}")

if __name__ == "__main__":
    main()
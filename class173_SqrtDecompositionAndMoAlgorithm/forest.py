#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
森林（Forest）实现
森林是由多棵不相交的树组成的数据结构

常见应用场景：
1. 多棵独立树的集合管理
2. 并查集（Union-Find）的基础
3. 数据库中的多树索引
4. 社交网络中的多个独立社区
5. 并行计算中的任务调度树

相关算法题目：
- LeetCode 684. 冗余连接 https://leetcode.cn/problems/redundant-connection/
- LeetCode 685. 冗余连接 II https://leetcode.cn/problems/redundant-connection-ii/
- LeetCode 1258. 近义词句子 https://leetcode.cn/problems/synonymous-sentences/
- LeetCode 959. 由斜杠划分区域 https://leetcode.cn/problems/regions-cut-by-slashes/
- LintCode 1179. 连通分量 https://www.lintcode.com/problem/1179/
- 洛谷 P3367 【模板】并查集 https://www.luogu.com.cn/problem/P3367
- 牛客 NC233 合并二叉树 https://www.nowcoder.com/practice/a5e8156e81224147bd749c560909299a
- HackerRank Tree: Level Order Traversal https://www.hackerrank.com/challenges/tree-level-order-traversal/problem
- CodeChef FORESTGA https://www.codechef.com/problems/FORESTGA
- USACO Forest Fires https://usaco.org/index.php?page=viewproblem2&cpid=668
"""

from typing import List, Optional, Dict, Set
from collections import deque


class TreeNode:
    """树节点类"""
    
    def __init__(self, val: int):
        """初始化节点
        
        Args:
            val: 节点值
        """
        self.val = val
        self.children = []
    
    def add_child(self, child_node: 'TreeNode') -> None:
        """添加子节点
        
        Args:
            child_node: 子节点
        """
        self.children.append(child_node)


class Forest:
    """森林实现类"""
    
    def __init__(self):
        """初始化森林"""
        self.trees = []  # 森林中的所有树
    
    def add_tree(self, root: Optional[TreeNode]) -> None:
        """添加一棵树到森林
        
        Args:
            root: 树的根节点
        """
        if root is not None:
            self.trees.append(root)
    
    def get_tree_count(self) -> int:
        """计算森林中树的数量
        
        Returns:
            树的数量
        """
        return len(self.trees)
    
    def get_trees(self) -> List[TreeNode]:
        """获取森林中的所有树
        
        Returns:
            树的列表
        """
        return self.trees.copy()
    
    def get_total_node_count(self) -> int:
        """计算森林中节点的总数
        
        Returns:
            节点总数
        """
        count = 0
        for root in self.trees:
            count += self._count_nodes(root)
        return count
    
    def _count_nodes(self, root: Optional[TreeNode]) -> int:
        """计算单棵树的节点数量（递归辅助函数）
        
        Args:
            root: 树的根节点
        
        Returns:
            节点数量
        """
        if root is None:
            return 0
        count = 1  # 当前节点
        for child in root.children:
            count += self._count_nodes(child)
        return count
    
    def get_total_height(self) -> int:
        """计算森林中所有树的高度之和
        
        Returns:
            高度之和
        """
        total_height = 0
        for root in self.trees:
            total_height += self._get_tree_height(root)
        return total_height
    
    def _get_tree_height(self, root: Optional[TreeNode]) -> int:
        """计算单棵树的高度（递归辅助函数）
        
        Args:
            root: 树的根节点
        
        Returns:
            树的高度
        """
        if root is None:
            return 0
        max_child_height = 0
        for child in root.children:
            max_child_height = max(max_child_height, self._get_tree_height(child))
        return max_child_height + 1
    
    def find_node(self, target: int) -> Optional[TreeNode]:
        """在森林中查找值为target的节点
        
        Args:
            target: 目标值
        
        Returns:
            找到的节点，如果不存在返回None
        """
        for root in self.trees:
            found = self._find_node_in_tree(root, target)
            if found is not None:
                return found
        return None
    
    def _find_node_in_tree(self, root: Optional[TreeNode], target: int) -> Optional[TreeNode]:
        """在单棵树中查找节点（递归辅助函数）
        
        Args:
            root: 树的根节点
            target: 目标值
        
        Returns:
            找到的节点，如果不存在返回None
        """
        if root is None:
            return None
        if root.val == target:
            return root
        for child in root.children:
            found = self._find_node_in_tree(child, target)
            if found is not None:
                return found
        return None
    
    def merge_trees(self, tree1: Optional[TreeNode], tree2: Optional[TreeNode]) -> Optional[TreeNode]:
        """合并两棵树（将tree2合并到tree1）
        
        Args:
            tree1: 第一棵树的根节点
            tree2: 第二棵树的根节点
        
        Returns:
            合并后的树的根节点
        """
        if tree1 is None:
            return tree2
        if tree2 is None:
            return tree1
        
        # 将tree2作为tree1的一个子节点
        tree1.add_child(tree2)
        
        # 从森林中移除tree2
        if tree2 in self.trees:
            self.trees.remove(tree2)
        
        return tree1
    
    def remove_tree(self, root: Optional[TreeNode]) -> bool:
        """将树从森林中移除
        
        Args:
            root: 要移除的树的根节点
        
        Returns:
            如果移除成功返回True，否则返回False
        """
        if root in self.trees:
            self.trees.remove(root)
            return True
        return False
    
    def level_order_traversal(self) -> List[List[int]]:
        """森林的层序遍历
        
        Returns:
            层序遍历的结果列表
        """
        result = []
        for root in self.trees:
            tree_level_order = self._level_order_traversal_tree(root)
            result.extend(tree_level_order)
        return result
    
    def _level_order_traversal_tree(self, root: Optional[TreeNode]) -> List[List[int]]:
        """单棵树的层序遍历（辅助函数）
        
        Args:
            root: 树的根节点
        
        Returns:
            层序遍历的结果列表
        """
        result = []
        if root is None:
            return result
        
        queue = deque([root])
        
        while queue:
            level_size = len(queue)
            current_level = []
            
            for _ in range(level_size):
                node = queue.popleft()
                current_level.append(node.val)
                for child in node.children:
                    queue.append(child)
            
            result.append(current_level)
        
        return result
    
    def print_forest(self) -> None:
        """打印森林的结构"""
        print(f"森林包含 {len(self.trees)} 棵树：")
        for i in range(len(self.trees)):
            print(f"\n树 {i + 1}:")
            self._print_tree(self.trees[i], 0)
    
    def _print_tree(self, node: Optional[TreeNode], level: int) -> None:
        """打印单棵树的结构（辅助函数）
        
        Args:
            node: 当前节点
            level: 当前节点的层级
        """
        if node is None:
            return
        # 打印缩进
        print("  " * level + str(node.val))
        # 递归打印子节点
        for child in node.children:
            self._print_tree(child, level + 1)
    
    def to_union_find(self) -> Dict[int, int]:
        """将森林转换为并查集（基于父指针）
        
        Returns:
            并查集字典，其中key是节点值，value是父节点值
        """
        parent_map = {}
        for root in self.trees:
            self._build_union_find(root, -1, parent_map)
        return parent_map
    
    def _build_union_find(self, node: Optional[TreeNode], parent: int, parent_map: Dict[int, int]) -> None:
        """构建并查集（递归辅助函数）
        
        Args:
            node: 当前节点
            parent: 父节点值
            parent_map: 父节点映射
        """
        if node is None:
            return
        parent_map[node.val] = parent
        for child in node.children:
            self._build_union_find(child, node.val, parent_map)
    
    @staticmethod
    def from_union_find(parent_map: Dict[int, int]) -> 'Forest':
        """从并查集构建森林
        
        Args:
            parent_map: 并查集父节点映射
        
        Returns:
            构建的森林
        """
        forest = Forest()
        node_map = {}
        roots = []
        
        # 创建所有节点并找出根节点
        for node_val, parent_val in parent_map.items():
            # 创建节点（如果不存在）
            if node_val not in node_map:
                node_map[node_val] = TreeNode(node_val)
            
            # 如果是根节点（父节点为-1）
            if parent_val == -1:
                roots.append(node_val)
            else:
                # 创建父节点（如果不存在）
                if parent_val not in node_map:
                    node_map[parent_val] = TreeNode(parent_val)
                # 建立父子关系
                node_map[parent_val].add_child(node_map[node_val])
        
        # 将所有根节点添加到森林
        for root_val in roots:
            forest.add_tree(node_map[root_val])
        
        return forest
    
    def get_connected_components(self) -> List[Set[int]]:
        """获取森林中的连通分量（每棵树的节点集合）
        
        Returns:
            连通分量列表，每个集合包含一棵树的所有节点
        """
        components = []
        for root in self.trees:
            component = set()
            self._collect_nodes(root, component)
            components.append(component)
        return components
    
    def _collect_nodes(self, node: Optional[TreeNode], component: Set[int]) -> None:
        """收集树中的所有节点（辅助函数）
        
        Args:
            node: 当前节点
            component: 节点集合
        """
        if node is None:
            return
        component.add(node.val)
        for child in node.children:
            self._collect_nodes(child, component)
    
    def is_connected(self, node1_val: int, node2_val: int) -> bool:
        """判断两个节点是否在同一棵树中
        
        Args:
            node1_val: 第一个节点的值
            node2_val: 第二个节点的值
        
        Returns:
            如果在同一棵树中返回True，否则返回False
        """
        # 先找到两个节点
        node1 = self.find_node(node1_val)
        node2 = self.find_node(node2_val)
        
        if node1 is None or node2 is None:
            return False
        
        # 找到两个节点所在的树的根节点
        root1 = self._find_root(node1_val)
        root2 = self._find_root(node2_val)
        
        return root1 == root2
    
    def _find_root(self, node_val: int) -> Optional[int]:
        """查找节点所在树的根节点值
        
        Args:
            node_val: 节点值
        
        Returns:
            根节点值，如果节点不存在返回None
        """
        union_find = self.to_union_find()
        
        if node_val not in union_find:
            return None
        
        # 查找根节点
        current = node_val
        while union_find[current] != -1:
            current = union_find[current]
        
        return current


# 测试代码
if __name__ == "__main__":
    # 创建森林示例
    forest = Forest()
    
    # 创建第一棵树
    #      1
    #     / \
    #    2   3
    #   /
    #  4
    tree1 = TreeNode(1)
    node2 = TreeNode(2)
    node3 = TreeNode(3)
    node4 = TreeNode(4)
    tree1.add_child(node2)
    tree1.add_child(node3)
    node2.add_child(node4)
    
    # 创建第二棵树
    #      5
    #     / \
    #    6   7
    tree2 = TreeNode(5)
    node6 = TreeNode(6)
    node7 = TreeNode(7)
    tree2.add_child(node6)
    tree2.add_child(node7)
    
    # 创建第三棵树
    #      8
    tree3 = TreeNode(8)
    
    # 添加树到森林
    forest.add_tree(tree1)
    forest.add_tree(tree2)
    forest.add_tree(tree3)
    
    # 打印森林
    print("初始森林：")
    forest.print_forest()
    
    # 测试树的数量
    print(f"\n森林中树的数量: {forest.get_tree_count()}")
    
    # 测试节点总数
    print(f"森林中节点总数: {forest.get_total_node_count()}")
    
    # 测试总高度
    print(f"森林中所有树的高度之和: {forest.get_total_height()}")
    
    # 测试查找节点
    found = forest.find_node(6)
    print(f"查找节点6: {'找到' if found else '未找到'}")
    
    # 测试合并树
    print("\n合并第一棵树和第二棵树后：")
    forest.merge_trees(tree1, tree2)
    forest.print_forest()
    
    # 测试移除树
    print("\n移除第三棵树后：")
    forest.remove_tree(tree3)
    forest.print_forest()
    
    # 测试层序遍历
    print("\n森林的层序遍历：")
    level_order = forest.level_order_traversal()
    for level in level_order:
        print(level)
    
    # 测试并查集转换
    print("\n转换为并查集：")
    union_find = forest.to_union_find()
    for node_val, parent_val in union_find.items():
        print(f"节点 {node_val} 的父节点: {parent_val}")
    
    # 测试从并查集构建森林
    print("\n从并查集构建森林：")
    rebuilt_forest = Forest.from_union_find(union_find)
    rebuilt_forest.print_forest()
    
    # 测试连通分量
    print("\n连通分量：")
    components = forest.get_connected_components()
    for i, component in enumerate(components):
        print(f"连通分量 {i + 1}: {component}")
    
    # 测试判断连通性
    print(f"\n节点1和节点6是否连通: {forest.is_connected(1, 6)}")
    print(f"节点4和节点7是否连通: {forest.is_connected(4, 7)}")
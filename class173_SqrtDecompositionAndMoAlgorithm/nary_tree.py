#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
N叉树（N-ary Tree）实现
N叉树是一种树数据结构，其中每个节点可以有0个或多个子节点

常见应用场景：
1. 组织结构图
2. 文件系统目录结构
3. XML/HTML文档解析
4. 计算机网络路由
5. 游戏开发中的场景树

相关算法题目：
- LeetCode 589. N叉树的前序遍历 https://leetcode.cn/problems/n-ary-tree-preorder-traversal/
- LeetCode 590. N叉树的后序遍历 https://leetcode.cn/problems/n-ary-tree-postorder-traversal/
- LeetCode 429. N叉树的层序遍历 https://leetcode.cn/problems/n-ary-tree-level-order-traversal/
- LeetCode 559. N叉树的最大深度 https://leetcode.cn/problems/maximum-depth-of-n-ary-tree/
- LeetCode 1490. 克隆N叉树 https://leetcode.cn/problems/clone-n-ary-tree/
- LintCode 1522. N叉树的直径 https://www.lintcode.com/problem/1522/
- HackerRank N-ary Tree Level Order Traversal https://www.hackerrank.com/challenges/tree-level-order-traversal/problem
- 洛谷 P5598 【XR-4】文本编辑器 https://www.luogu.com.cn/problem/P5598
- 牛客 NC144 多叉树的直径 https://www.nowcoder.com/practice/a77b4f3d84bf4a7891519ffee9376df3
"""

from typing import List, Optional, Dict
from collections import deque


class NaryTreeNode:
    """N叉树节点类"""
    
    def __init__(self, val: int):
        """初始化节点
        
        Args:
            val: 节点值
        """
        self.val = val
        self.children = []
    
    def add_child(self, child_node: 'NaryTreeNode') -> None:
        """添加子节点
        
        Args:
            child_node: 子节点
        """
        self.children.append(child_node)


class NaryTree:
    """N叉树实现类"""
    
    def preorder_traversal(self, root: Optional[NaryTreeNode]) -> List[int]:
        """前序遍历：根节点 -> 子节点（从左到右）
        
        Args:
            root: N叉树的根节点
        
        Returns:
            前序遍历的结果列表
        """
        result = []
        if root is None:
            return result
        
        def preorder_helper(node: NaryTreeNode, res: List[int]) -> None:
            """前序遍历递归辅助函数
            
            Args:
                node: 当前节点
                res: 结果列表
            """
            if node is None:
                return
            # 先访问根节点
            res.append(node.val)
            # 再递归访问所有子节点
            for child in node.children:
                preorder_helper(child, res)
        
        preorder_helper(root, result)
        return result
    
    def preorder_traversal_iterative(self, root: Optional[NaryTreeNode]) -> List[int]:
        """前序遍历的非递归实现
        
        Args:
            root: N叉树的根节点
        
        Returns:
            前序遍历的结果列表
        """
        result = []
        if root is None:
            return result
        
        stack = [root]
        
        while stack:
            node = stack.pop()
            result.append(node.val)
            # 注意：这里需要逆序压入子节点，以保证出栈顺序是从左到右
            for child in reversed(node.children):
                stack.append(child)
        
        return result
    
    def postorder_traversal(self, root: Optional[NaryTreeNode]) -> List[int]:
        """后序遍历：子节点（从左到右）-> 根节点
        
        Args:
            root: N叉树的根节点
        
        Returns:
            后序遍历的结果列表
        """
        result = []
        if root is None:
            return result
        
        def postorder_helper(node: NaryTreeNode, res: List[int]) -> None:
            """后序遍历递归辅助函数
            
            Args:
                node: 当前节点
                res: 结果列表
            """
            if node is None:
                return
            # 先递归访问所有子节点
            for child in node.children:
                postorder_helper(child, res)
            # 再访问根节点
            res.append(node.val)
        
        postorder_helper(root, result)
        return result
    
    def level_order_traversal(self, root: Optional[NaryTreeNode]) -> List[List[int]]:
        """层序遍历（广度优先遍历）
        
        Args:
            root: N叉树的根节点
        
        Returns:
            层序遍历的结果列表，每个子列表代表一层
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
                # 将所有子节点加入队列
                for child in node.children:
                    queue.append(child)
            
            result.append(current_level)
        
        return result
    
    def max_depth(self, root: Optional[NaryTreeNode]) -> int:
        """计算N叉树的最大深度
        
        Args:
            root: N叉树的根节点
        
        Returns:
            最大深度
        """
        if root is None:
            return 0
        
        max_child_depth = 0
        for child in root.children:
            max_child_depth = max(max_child_depth, self.max_depth(child))
        
        return max_child_depth + 1
    
    def count_nodes(self, root: Optional[NaryTreeNode]) -> int:
        """计算N叉树的节点总数
        
        Args:
            root: N叉树的根节点
        
        Returns:
            节点总数
        """
        if root is None:
            return 0
        
        count = 1  # 当前节点
        for child in root.children:
            count += self.count_nodes(child)
        
        return count
    
    def clone_tree(self, root: Optional[NaryTreeNode]) -> Optional[NaryTreeNode]:
        """克隆一棵N叉树
        
        Args:
            root: 原N叉树的根节点
        
        Returns:
            克隆后的N叉树的根节点
        """
        if root is None:
            return None
        
        cloned_root = NaryTreeNode(root.val)
        for child in root.children:
            cloned_root.add_child(self.clone_tree(child))
        
        return cloned_root
    
    def find_node(self, root: Optional[NaryTreeNode], target: int) -> Optional[NaryTreeNode]:
        """查找值为target的节点
        
        Args:
            root: N叉树的根节点
            target: 目标值
        
        Returns:
            找到的节点，如果不存在返回None
        """
        if root is None:
            return None
        
        if root.val == target:
            return root
        
        for child in root.children:
            found = self.find_node(child, target)
            if found is not None:
                return found
        
        return None
    
    def print_tree(self, root: Optional[NaryTreeNode]) -> None:
        """打印N叉树的结构
        
        Args:
            root: N叉树的根节点
        """
        if root is None:
            print("Empty tree")
            return
        
        def print_helper(node: NaryTreeNode, level: int) -> None:
            """打印辅助函数
            
            Args:
                node: 当前节点
                level: 当前节点的层级
            """
            # 打印缩进
            print("  " * level + str(node.val))
            # 递归打印子节点
            for child in node.children:
                print_helper(child, level + 1)
        
        print_helper(root, 0)
    
    def build_tree_from_parent_array(self, parent: List[int]) -> Optional[NaryTreeNode]:
        """从父节点数组构建N叉树
        
        Args:
            parent: 父节点数组，parent[i]表示节点i的父节点
        
        Returns:
            构建的N叉树的根节点
        """
        if not parent:
            return None
        
        nodes: Dict[int, NaryTreeNode] = {}
        root = None
        
        for i in range(len(parent)):
            # 创建当前节点
            if i not in nodes:
                nodes[i] = NaryTreeNode(i)
            current = nodes[i]
            
            if parent[i] == -1:
                # 根节点
                root = current
            else:
                # 创建父节点（如果不存在）
                if parent[i] not in nodes:
                    nodes[parent[i]] = NaryTreeNode(parent[i])
                parent_node = nodes[parent[i]]
                # 将当前节点添加为父节点的子节点
                parent_node.add_child(current)
        
        return root
    
    def is_same_tree(self, p: Optional[NaryTreeNode], q: Optional[NaryTreeNode]) -> bool:
        """判断两棵N叉树是否相同
        
        Args:
            p: 第一棵树的根节点
            q: 第二棵树的根节点
        
        Returns:
            如果两棵树相同返回True，否则返回False
        """
        if p is None and q is None:
            return True
        if p is None or q is None:
            return False
        if p.val != q.val:
            return False
        if len(p.children) != len(q.children):
            return False
        
        for i in range(len(p.children)):
            if not self.is_same_tree(p.children[i], q.children[i]):
                return False
        
        return True
    
    def diameter(self, root: Optional[NaryTreeNode]) -> int:
        """计算N叉树的直径（最长路径）
        
        Args:
            root: N叉树的根节点
        
        Returns:
            树的直径
        """
        self.max_diameter = 0
        
        def dfs(node: Optional[NaryTreeNode]) -> int:
            """深度优先搜索计算高度，并更新最大直径
            
            Args:
                node: 当前节点
            
            Returns:
                当前节点的高度
            """
            if node is None:
                return 0
            
            # 记录最大的两个高度
            max1, max2 = 0, 0
            for child in node.children:
                height = dfs(child) + 1
                if height > max1:
                    max2 = max1
                    max1 = height
                elif height > max2:
                    max2 = height
            
            # 更新最大直径
            self.max_diameter = max(self.max_diameter, max1 + max2)
            return max1
        
        dfs(root)
        return self.max_diameter


# 测试代码
if __name__ == "__main__":
    # 创建N叉树示例
    #       1
    #     / | \
    #    2  3  4
    #   / \   / \
    #  5   6 7   8
    
    root = NaryTreeNode(1)
    node2 = NaryTreeNode(2)
    node3 = NaryTreeNode(3)
    node4 = NaryTreeNode(4)
    node5 = NaryTreeNode(5)
    node6 = NaryTreeNode(6)
    node7 = NaryTreeNode(7)
    node8 = NaryTreeNode(8)
    
    root.add_child(node2)
    root.add_child(node3)
    root.add_child(node4)
    node2.add_child(node5)
    node2.add_child(node6)
    node4.add_child(node7)
    node4.add_child(node8)
    
    tree = NaryTree()
    
    # 测试前序遍历
    print("前序遍历（递归）:")
    preorder = tree.preorder_traversal(root)
    print(preorder)
    
    print("前序遍历（非递归）:")
    preorder_iter = tree.preorder_traversal_iterative(root)
    print(preorder_iter)
    
    # 测试后序遍历
    print("后序遍历:")
    postorder = tree.postorder_traversal(root)
    print(postorder)
    
    # 测试层序遍历
    print("层序遍历:")
    level_order = tree.level_order_traversal(root)
    for level in level_order:
        print(level)
    
    # 测试最大深度
    print(f"最大深度: {tree.max_depth(root)}")
    
    # 测试节点总数
    print(f"节点总数: {tree.count_nodes(root)}")
    
    # 测试克隆树
    cloned = tree.clone_tree(root)
    print("克隆树前序遍历:")
    cloned_preorder = tree.preorder_traversal(cloned)
    print(cloned_preorder)
    
    # 测试查找节点
    found = tree.find_node(root, 6)
    print(f"查找节点6: {'找到' if found else '未找到'}")
    
    # 测试打印树
    print("树的结构:")
    tree.print_tree(root)
    
    # 测试从父节点数组构建树
    parent_array = [-1, 0, 0, 0, 1, 1, 3, 3]
    built_tree = tree.build_tree_from_parent_array(parent_array)
    print("从父节点数组构建的树结构:")
    tree.print_tree(built_tree)
    
    # 测试判断树是否相同
    is_same = tree.is_same_tree(root, cloned)
    print(f"原树与克隆树是否相同: {is_same}")
    
    # 测试树的直径
    diameter = tree.diameter(root)
    print(f"树的直径: {diameter}")
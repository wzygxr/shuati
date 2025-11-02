# LeetCode 1522. N叉树的直径
# 题目：给定一棵N叉树，你需要计算它的直径长度。
# 一棵N叉树的直径长度是任意两个结点路径长度中的最大值。
# 这条路径可能穿过也可能不穿过根结点。
# 两结点之间的路径长度是以它们之间边的数目表示。
# 来源：LeetCode
# 链接：https://leetcode.cn/problems/diameter-of-n-ary-tree/

from typing import List, Optional
from collections import deque
import heapq

# N叉树节点定义
class Node:
    def __init__(self, val=None, children=None):
        self.val = val
        self.children = children if children is not None else []

class LeetCode1522_DiameterOfNAryTree:
    """
    N叉树直径计算类
    提供三种方法：递归法、优化递归法、迭代法
    """
    
    def __init__(self):
        self.max_diameter = 0  # 全局变量，用于记录最大直径
    
    def diameter(self, root: Optional[Node]) -> int:
        """
        计算N叉树的直径
        
        Args:
            root: N叉树根节点
            
        Returns:
            int: 树的直径（边数）
            
        时间复杂度：O(n)，其中n是N叉树的节点数，每个节点只访问一次
        空间复杂度：O(h)，其中h是N叉树的高度，递归调用栈的深度
        """
        self.max_diameter = 0  # 重置全局变量
        self._depth(root)      # 计算每个节点的深度并更新最大直径
        return self.max_diameter
    
    def _depth(self, node: Optional[Node]) -> int:
        """
        计算以当前节点为根的子树深度，并更新最大直径
        
        Args:
            node: 当前节点
            
        Returns:
            int: 当前节点为根的子树的最大深度
        """
        # 基本情况：空节点深度为0
        if node is None:
            return 0
        
        # 特殊情况：没有子节点，深度为0
        if not node.children:
            return 0
        
        # 存储所有子节点的深度
        depths = []
        
        # 递归计算所有子节点的最大深度
        for child in node.children:
            child_depth = self._depth(child)
            depths.append(child_depth)
        
        # 对深度进行排序，找到最大的两个深度
        depths.sort(reverse=True)
        
        # 计算经过当前节点的最长路径
        path_through_node = 0
        if len(depths) >= 1:
            path_through_node += depths[0]
        if len(depths) >= 2:
            path_through_node += depths[1]
        
        # 更新全局最大直径
        self.max_diameter = max(self.max_diameter, path_through_node)
        
        # 返回以当前节点为根的子树的最大深度
        return depths[0] + 1 if depths else 0
    
    def diameter_optimized(self, root: Optional[Node]) -> int:
        """
        使用最大堆优化的深度计算方法
        
        Args:
            root: N叉树根节点
            
        Returns:
            int: 树的直径
        """
        self.max_diameter = 0
        self._depth_optimized(root)
        return self.max_diameter
    
    def _depth_optimized(self, node: Optional[Node]) -> int:
        """
        使用最大堆优化的深度计算
        
        Args:
            node: 当前节点
            
        Returns:
            int: 当前节点为根的子树的最大深度
        """
        if node is None:
            return 0
        
        if not node.children:
            return 0
        
        # 使用最大堆来维护最大的两个深度
        max_heap = []
        
        for child in node.children:
            child_depth = self._depth_optimized(child)
            heapq.heappush(max_heap, -child_depth)  # 使用负数模拟最大堆
        
        # 取出最大的两个深度
        max1 = -heapq.heappop(max_heap) if max_heap else 0
        max2 = -heapq.heappop(max_heap) if max_heap else 0
        
        # 更新最大直径
        self.max_diameter = max(self.max_diameter, max1 + max2)
        
        # 返回最大深度
        return max1 + 1
    
    def diameter_iterative(self, root: Optional[Node]) -> int:
        """
        迭代实现（避免递归深度过大）
        
        Args:
            root: N叉树根节点
            
        Returns:
            int: 树的直径
            
        时间复杂度：O(n)
        空间复杂度：O(n)
        """
        if root is None:
            return 0
        
        # 使用后序遍历计算每个节点的深度
        depth_map = {}
        stack = [root]
        visited = set()
        
        max_diameter = 0
        
        while stack:
            node = stack[-1]
            
            # 如果所有子节点都已经处理过
            all_children_processed = True
            
            for child in node.children:
                if child not in depth_map:
                    stack.append(child)
                    all_children_processed = False
                    break
            
            if all_children_processed:
                stack.pop()
                
                # 计算当前节点的深度
                child_depths = []
                for child in node.children:
                    child_depths.append(depth_map[child])
                
                # 排序找到最大的两个深度
                child_depths.sort(reverse=True)
                
                max1 = child_depths[0] if child_depths else 0
                max2 = child_depths[1] if len(child_depths) >= 2 else 0
                
                # 更新最大直径
                max_diameter = max(max_diameter, max1 + max2)
                
                # 当前节点的深度 = 最大子节点深度 + 1
                depth_map[node] = max1 + 1
        
        return max_diameter
    
    def test(self):
        """测试方法"""
        solution = LeetCode1522_DiameterOfNAryTree()
        
        # 测试用例1: 简单的三叉树
        #     1
        #   / | \
        #  2  3  4
        # 预期输出：2（路径 2-1-3 或 2-1-4 或 3-1-4）
        root1 = Node(1, [Node(2), Node(3), Node(4)])
        
        print(f"测试用例1结果: {solution.diameter(root1)}")  # 应该输出2
        print(f"测试用例1(优化)结果: {solution.diameter_optimized(root1)}")  # 应该输出2
        print(f"测试用例1(迭代)结果: {solution.diameter_iterative(root1)}")  # 应该输出2
        
        # 测试用例2: 更复杂的N叉树
        #       1
        #     / | \
        #    2  3  4
        #   /|     |
        #  5 6     7
        # 预期输出：4（路径 5-2-1-4-7）
        node2 = Node(2, [Node(5), Node(6)])
        node4 = Node(4, [Node(7)])
        root2 = Node(1, [node2, Node(3), node4])
        
        print(f"测试用例2结果: {solution.diameter(root2)}")  # 应该输出4
        print(f"测试用例2(优化)结果: {solution.diameter_optimized(root2)}")  # 应该输出4
        print(f"测试用例2(迭代)结果: {solution.diameter_iterative(root2)}")  # 应该输出4
        
        # 测试用例3: 单节点树
        root3 = Node(1)
        print(f"测试用例3结果: {solution.diameter(root3)}")  # 应该输出0
        print(f"测试用例3(优化)结果: {solution.diameter_optimized(root3)}")  # 应该输出0
        print(f"测试用例3(迭代)结果: {solution.diameter_iterative(root3)}")  # 应该输出0
        
        # 测试用例4: 空树
        print(f"测试用例4结果: {solution.diameter(None)}")  # 应该输出0
        print(f"测试用例4(优化)结果: {solution.diameter_optimized(None)}")  # 应该输出0
        print(f"测试用例4(迭代)结果: {solution.diameter_iterative(None)}")  # 应该输出0

# 主函数
if __name__ == "__main__":
    solution = LeetCode1522_DiameterOfNAryTree()
    solution.test()
from collections import deque
from typing import List, Optional, Any

# LeetCode 429. N 叉树的层序遍历
# 题目链接: https://leetcode.cn/problems/n-ary-tree-level-order-traversal/
# 题目大意: 给定一个 N 叉树，返回其节点值的层序遍历。（即从左到右，逐层遍历）
# 树的序列化输入是用层序遍历，每组子节点都由 null 值分隔

# N叉树节点定义
class Node:
    def __init__(self, val=None, children=None):
        self.val = val
        self.children = children if children is not None else []

class Solution:
    def levelOrder1(self, root: Optional[Node]) -> List[List[int]]:
        """
        方法1: 使用BFS层序遍历
        时间复杂度: O(n) - n是树中节点的数量，每个节点访问一次
        空间复杂度: O(w) - w是树的最大宽度，队列中最多存储一层的节点
        """
        result = []
        if not root:
            return result
        
        queue = deque([root])
        
        while queue:
            size = len(queue)
            level = []
            
            for _ in range(size):
                node = queue.popleft()
                level.append(node.val)
                
                # 将所有子节点加入队列
                for child in node.children:
                    if child:
                        queue.append(child)
            
            result.append(level)
        
        return result
    
    def levelOrder2(self, root: Optional[Node]) -> List[List[int]]:
        """
        方法2: 使用DFS递归遍历
        时间复杂度: O(n) - n是树中节点的数量，每个节点访问一次
        空间复杂度: O(h) - h是树的高度，递归调用栈的深度
        """
        result = []
        if not root:
            return result
        
        self.dfs(root, 0, result)
        return result
    
    def dfs(self, node: Optional[Node], level: int, result: List[List[int]]) -> None:
        """
        深度优先搜索辅助函数
        Args:
            node: 当前节点
            level: 当前层级
            result: 结果列表
        """
        if not node:
            return
        
        # 如果当前层级还没有对应的列表，创建一个新的
        if len(result) <= level:
            result.append([])
        
        # 将当前节点值添加到对应层级的列表中
        if node.val is not None:
            result[level].append(node.val)
        
        # 递归处理所有子节点
        for child in node.children:
            self.dfs(child, level + 1, result)

# 测试代码
if __name__ == "__main__":
    # 构建测试N叉树:
    #       1
    #    /  |  \
    #   3   2   4
    #  / \
    # 5   6
    root = Node(1)
    node3 = Node(3)
    node2 = Node(2)
    node4 = Node(4)
    root.children = [node3, node2, node4]
    
    node5 = Node(5)
    node6 = Node(6)
    node3.children = [node5, node6]
    
    solution = Solution()
    print("方法1结果:", solution.levelOrder1(root))
    print("方法2结果:", solution.levelOrder2(root))
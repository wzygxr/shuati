# LeetCode 429. N-ary Tree Level Order Traversal
# N叉树的层序遍历
# 题目来源：https://leetcode.cn/problems/n-ary-tree-level-order-traversal/

"""
问题描述：
给定一个 N 叉树，返回其节点值的层序遍历。（即从左到右，逐层遍历）。
树的序列化输入是用层序遍历，每组子节点都由 null 值分隔（参见示例）。

解题思路：
1. 递归方法：使用深度优先搜索，记录每个节点的层级，并将节点值添加到对应层级的列表中
2. 迭代方法：使用队列进行广度优先搜索，逐层处理节点

时间复杂度：O(N)，其中N是树中的节点数，每个节点只被访问一次
空间复杂度：
  - 递归：O(H)，H是树的高度，递归调用栈的最大深度
  - 迭代：O(W)，W是树中最宽层的节点数，队列的最大大小
"""

# N叉树节点定义
class Node:
    def __init__(self, val=None, children=None):
        self.val = val
        self.children = children if children is not None else []

class NaryTreeLevelOrderTraversal:
    def levelOrderRecursive(self, root):
        """
        递归实现层序遍历
        
        Args:
            root: N叉树的根节点
            
        Returns:
            层序遍历的结果列表
        """
        result = []
        if root is None:
            return result
        
        # 从第0层开始递归遍历
        self._dfs(root, 0, result)
        return result
    
    def _dfs(self, node, level, result):
        """
        深度优先搜索辅助方法，按层收集节点值
        
        Args:
            node: 当前节点
            level: 当前节点的层级
            result: 存储层序遍历结果的列表
        """
        # 如果当前层级的列表还不存在，创建它
        if level >= len(result):
            result.append([])
        
        # 将当前节点的值添加到对应层级的列表中
        result[level].append(node.val)
        
        # 递归处理所有子节点，层级加1
        for child in node.children:
            self._dfs(child, level + 1, result)
    
    def levelOrderIterative(self, root):
        """
        迭代实现层序遍历（使用队列）
        
        Args:
            root: N叉树的根节点
            
        Returns:
            层序遍历的结果列表
        """
        result = []
        if root is None:
            return result
        
        # 使用队列进行广度优先搜索
        from collections import deque
        queue = deque([root])
        
        # 逐层处理节点
        while queue:
            level_size = len(queue)  # 当前层的节点数量
            current_level = []
            
            # 处理当前层的所有节点
            for _ in range(level_size):
                current_node = queue.popleft()
                current_level.append(current_node.val)
                
                # 将子节点加入队列，用于处理下一层
                for child in current_node.children:
                    queue.append(child)
            
            # 将当前层的结果添加到最终结果中
            result.append(current_level)
        
        return result

# 测试代码
if __name__ == "__main__":
    solution = NaryTreeLevelOrderTraversal()
    
    # 构建测试用例的N叉树
    # 示例：[1,null,3,2,4,null,5,6]
    root = Node(1)
    root.children = [Node(3), Node(2), Node(4)]
    root.children[0].children = [Node(5), Node(6)]
    
    # 递归方法测试
    print("递归实现结果:")
    result1 = solution.levelOrderRecursive(root)
    print(result1)
    
    # 迭代方法测试
    print("\n迭代实现结果:")
    result2 = solution.levelOrderIterative(root)
    print(result2)
    
    # 空树测试
    print("\n空树测试:")
    result3 = solution.levelOrderRecursive(None)
    print(result3)
    
    result4 = solution.levelOrderIterative(None)
    print(result4)
    
    """
    性能分析：
    - 时间复杂度：两种实现都是O(N)，其中N是树中的节点数，每个节点只被访问一次
    
    - 空间复杂度：
      - 递归：O(H)，H是树的高度，递归调用栈的最大深度
        最坏情况下，树是一条链，空间复杂度为O(N)
      - 迭代：O(W)，W是树中最宽层的节点数，队列的最大大小
        最坏情况下，最后一层全是叶子节点，空间复杂度为O(N)
    
    两种实现方法的对比：
    1. 递归实现更简洁，但对于非常深的树可能导致栈溢出
    2. 迭代实现更稳健，不受递归深度限制，对于大型树更安全
    
    工程化考量：
    1. 异常处理：在实际应用中，应该检查输入树是否为None，以及树的结构是否合法
    2. 对于非常大的树，应该优先考虑迭代实现，避免栈溢出风险
    3. 可以添加并行处理来加速遍历，但需要注意线程安全问题
    4. 在内存受限的环境中，需要考虑数据结构的选择，避免不必要的内存开销
    5. 在Python中，递归深度默认限制为1000，可以通过sys.setrecursionlimit调整
    """
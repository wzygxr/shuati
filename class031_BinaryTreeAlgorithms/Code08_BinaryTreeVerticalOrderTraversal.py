# LeetCode 314. Binary Tree Vertical Order Traversal
# 题目链接: https://leetcode.cn/problems/binary-tree-vertical-order-traversal/
# 题目描述: 给你一个二叉树的根结点，返回其节点按垂直方向（从上到下，逐列）遍历的结果。
# 如果两个节点在同一行和列，那么顺序则为从左到右。
#
# 解题思路:
# 1. 使用BFS层序遍历，同时记录每个节点的列号
# 2. 根节点列号为0，左子节点列号减1，右子节点列号加1
# 3. 使用字典记录每列的节点值列表
# 4. 使用minCol和maxCol记录列号的范围
# 5. 按列号从小到大收集结果
#
# 时间复杂度: O(n) - n为树中节点的数量，每个节点访问一次
# 空间复杂度: O(n) - 队列和字典最多存储n个节点
# 是否为最优解: 是，这是垂直遍历的标准解法

from typing import List, Optional
from collections import deque, defaultdict

# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

def verticalOrder(root: Optional[TreeNode]) -> List[List[int]]:
    """
    二叉树垂直遍历
    
    Args:
        root: 二叉树的根节点
        
    Returns:
        按垂直方向遍历的节点值列表
    """
    result = []
    if root is None:
        return result
    
    # 使用字典记录每列的节点值列表
    column_map = defaultdict(list)
    
    # 使用队列进行BFS，存储节点和对应的列号
    node_queue = deque([root])
    column_queue = deque([0])
    
    # 记录列号的范围
    min_col, max_col = 0, 0
    
    while node_queue:
        node = node_queue.popleft()
        col = column_queue.popleft()
        
        # 将节点值添加到对应列的列表中
        column_map[col].append(node.val)
        
        # 更新列号范围
        min_col = min(min_col, col)
        max_col = max(max_col, col)
        
        # 处理左右子节点
        if node.left is not None:
            node_queue.append(node.left)
            column_queue.append(col - 1)
        if node.right is not None:
            node_queue.append(node.right)
            column_queue.append(col + 1)
    
    # 按列号从小到大收集结果
    for i in range(min_col, max_col + 1):
        result.append(column_map[i])
    
    return result

# 测试用例
if __name__ == "__main__":
    # 测试用例1:
    #       3
    #      / \
    #     9  20
    #       /  \
    #      15   7
    # 垂直遍历结果: [[9], [3, 15], [20], [7]]
    root1 = TreeNode(3)
    root1.left = TreeNode(9)
    root1.right = TreeNode(20)
    root1.right.left = TreeNode(15)
    root1.right.right = TreeNode(7)
    
    result1 = verticalOrder(root1)
    print(f"测试用例1结果: {result1}")  # 应该输出[[9], [3, 15], [20], [7]]

    # 测试用例2:
    #       3
    #      / \
    #     9   8
    #    / \   \
    #   4   0   1
    #      / \   \
    #     5   2   7
    # 垂直遍历结果: [[4], [9, 5], [3, 0, 1], [8, 2], [7]]
    root2 = TreeNode(3)
    root2.left = TreeNode(9)
    root2.right = TreeNode(8)
    root2.left.left = TreeNode(4)
    root2.left.right = TreeNode(0)
    root2.right.right = TreeNode(1)
    root2.left.right.left = TreeNode(5)
    root2.left.right.right = TreeNode(2)
    root2.right.right.right = TreeNode(7)
    
    result2 = verticalOrder(root2)
    print(f"测试用例2结果: {result2}")  # 应该输出[[4], [9, 5], [3, 0, 1], [8, 2], [7]]

    # 测试用例3: 空树
    root3 = None
    result3 = verticalOrder(root3)
    print(f"测试用例3结果: {result3}")  # 应该输出[]
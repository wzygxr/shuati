# LeetCode 102. Binary Tree Level Order Traversal
# 题目链接: https://leetcode.cn/problems/binary-tree-level-order-traversal/
# 题目描述: 给你二叉树的根节点 root ，返回其节点值的层序遍历结果。
#           （即逐层地，从左到右访问所有节点）
#
# 解题思路:
# 1. 使用广度优先搜索(BFS)进行层序遍历
# 2. 使用队列存储每一层的节点
# 3. 对于每一层，先记录当前层的节点数，然后处理这些节点
# 4. 将每个节点的子节点加入队列，用于下一层的遍历
#
# 时间复杂度: O(n) - n为树中节点的数量，每个节点都需要访问一次
# 空间复杂度: O(w) - w为树的最大宽度，队列中最多存储一层的节点
# 是否为最优解: 是，这是层序遍历的标准解法

from typing import List, Optional
from collections import deque

# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def levelOrder(self, root: Optional[TreeNode]) -> List[List[int]]:
        """
        二叉树层序遍历
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            层序遍历结果
        """
        result = []
        
        # 边界条件：空树
        if root is None:
            return result
        
        # 使用队列进行BFS
        queue = deque([root])
        
        while queue:
            # 记录当前层的节点数
            level_size = len(queue)
            current_level = []
            
            # 处理当前层的所有节点
            for i in range(level_size):
                node = queue.popleft()
                current_level.append(node.val)
                
                # 将子节点加入队列，用于下一层遍历
                if node.left:
                    queue.append(node.left)
                if node.right:
                    queue.append(node.right)
            
            # 将当前层的结果添加到最终结果中
            result.append(current_level)
        
        return result

# 测试用例
def main():
    solution = Solution()

    # 测试用例1:
    #       3
    #      / \
    #     9  20
    #       /  \
    #      15   7
    root1 = TreeNode(3)
    root1.left = TreeNode(9)
    root1.right = TreeNode(20)
    root1.right.left = TreeNode(15)
    root1.right.right = TreeNode(7)
    
    result1 = solution.levelOrder(root1)
    print("测试用例1结果:", result1)  # 应该输出[[3], [9, 20], [15, 7]]

    # 测试用例2: 空树
    root2 = None
    result2 = solution.levelOrder(root2)
    print("测试用例2结果:", result2)  # 应该输出[]

    # 测试用例3: 只有根节点
    root3 = TreeNode(1)
    result3 = solution.levelOrder(root3)
    print("测试用例3结果:", result3)  # 应该输出[[1]]

if __name__ == "__main__":
    main()
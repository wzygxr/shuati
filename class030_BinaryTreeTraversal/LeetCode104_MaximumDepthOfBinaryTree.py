from typing import Optional
from collections import deque

# LeetCode 104. 二叉树的最大深度
# 题目链接: https://leetcode.cn/problems/maximum-depth-of-binary-tree/
# 题目大意: 给定一个二叉树，找出其最大深度。二叉树的深度为根节点到最远叶子节点的最长路径上的节点数。

# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def maxDepth1(self, root: Optional[TreeNode]) -> int:
        """
        方法1: 递归实现计算二叉树的最大深度
        思路:
        1. 如果当前节点为空，深度为0
        2. 递归计算左子树的最大深度
        3. 递归计算右子树的最大深度
        4. 返回左右子树最大深度的最大值加1
        时间复杂度: O(n) - n是节点数量，每个节点访问一次
        空间复杂度: O(h) - h是树的高度，递归调用栈的深度
        """
        # 递归终止条件
        if not root:
            return 0
        
        # 递归计算左子树和右子树的最大深度
        left_depth = self.maxDepth1(root.left)
        right_depth = self.maxDepth1(root.right)
        
        # 返回左右子树最大深度的最大值加1
        return max(left_depth, right_depth) + 1
    
    def maxDepth2(self, root: Optional[TreeNode]) -> int:
        """
        方法2: 迭代实现计算二叉树的最大深度
        思路:
        1. 使用队列进行层序遍历
        2. 记录层数，每处理完一层层数加1
        3. 返回最终的层数
        时间复杂度: O(n) - n是节点数量，每个节点访问一次
        空间复杂度: O(w) - w是树的最大宽度，队列中最多存储一层的节点
        """
        if not root:
            return 0
        
        # 使用队列进行层序遍历
        queue = deque([root])
        depth = 0
        
        while queue:
            # 记录当前层的节点数量
            size = len(queue)
            
            # 处理当前层的所有节点
            for _ in range(size):
                current = queue.popleft()
                
                # 将左右子节点加入队列（如果存在）
                if current.left:
                    queue.append(current.left)
                if current.right:
                    queue.append(current.right)
            
            # 每处理完一层，深度加1
            depth += 1
        
        return depth

# 测试代码
if __name__ == "__main__":
    # 构建测试二叉树:
    #     3
    #    / \
    #   9  20
    #     /  \
    #    15   7
    root = TreeNode(3)
    root.left = TreeNode(9)
    root.right = TreeNode(20)
    root.right.left = TreeNode(15)
    root.right.right = TreeNode(7)
    
    solution = Solution()
    print("递归方法计算的最大深度:", solution.maxDepth1(root))
    print("迭代方法计算的最大深度:", solution.maxDepth2(root))
    
    # 测试空树
    empty_root = None
    print("空树的最大深度:", solution.maxDepth1(empty_root))
    
    # 测试单节点树
    single_root = TreeNode(1)
    print("单节点树的最大深度:", solution.maxDepth1(single_root))
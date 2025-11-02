from typing import Optional
from collections import deque

# LeetCode 101. 对称二叉树
# 题目链接: https://leetcode.cn/problems/symmetric-tree/
# 题目大意: 给你一个二叉树的根节点 root ，检查它是否轴对称。

# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def isSymmetric1(self, root: Optional[TreeNode]) -> bool:
        """
        方法1: 递归实现判断二叉树是否对称
        思路:
        1. 创建辅助函数比较两个子树是否互为镜像
        2. 如果两个节点都为空，返回true
        3. 如果其中一个节点为空，另一个不为空，返回false
        4. 如果两个节点的值不相等，返回false
        5. 递归比较左子树的左子树与右子树的右子树，以及左子树的右子树与右子树的左子树
        时间复杂度: O(n) - n是节点数量，每个节点访问一次
        空间复杂度: O(h) - h是树的高度，递归调用栈的深度
        """
        if not root:
            return True
        return self.isMirror(root.left, root.right)
    
    def isMirror(self, left: Optional[TreeNode], right: Optional[TreeNode]) -> bool:
        """
        辅助函数：判断两个子树是否互为镜像
        :param left: 左子树根节点
        :param right: 右子树根节点
        :return: 是否互为镜像
        """
        # 如果两个节点都为空，返回True
        if not left and not right:
            return True
        
        # 如果其中一个节点为空，另一个不为空，返回False
        if not left or not right:
            return False
        
        # 如果两个节点的值不相等，返回False
        if left.val != right.val:
            return False
        
        # 递归比较左子树的左子树与右子树的右子树，以及左子树的右子树与右子树的左子树
        return self.isMirror(left.left, right.right) and self.isMirror(left.right, right.left)
    
    def isSymmetric2(self, root: Optional[TreeNode]) -> bool:
        """
        方法2: 迭代实现判断二叉树是否对称
        思路:
        1. 使用队列存储待比较的节点对
        2. 每次从队列中取出一对节点进行比较
        3. 如果节点对都为空，继续下一对
        4. 如果其中一个为空或值不相等，返回false
        5. 将左子树的左子节点与右子树的右子节点、左子树的右子节点与右子树的左子节点加入队列
        时间复杂度: O(n) - n是节点数量，每个节点访问一次
        空间复杂度: O(w) - w是树的最大宽度，队列中最多存储一层的节点
        """
        if not root:
            return True
        
        # 使用队列存储待比较的节点对
        queue = deque([root.left, root.right])
        
        while queue:
            # 取出一对节点
            left = queue.popleft()
            right = queue.popleft()
            
            # 如果两个节点都为空，继续下一对
            if not left and not right:
                continue
            
            # 如果其中一个节点为空或值不相等，返回False
            if not left or not right or left.val != right.val:
                return False
            
            # 将左子树的左子节点与右子树的右子节点、左子树的右子节点与右子树的左子节点加入队列
            queue.append(left.left)
            queue.append(right.right)
            queue.append(left.right)
            queue.append(right.left)
        
        return True

# 测试代码
if __name__ == "__main__":
    # 构建测试对称二叉树:
    #     1
    #    / \
    #   2   2
    #  / \ / \
    # 3  4 4  3
    symmetric_root = TreeNode(1)
    symmetric_root.left = TreeNode(2)
    symmetric_root.right = TreeNode(2)
    symmetric_root.left.left = TreeNode(3)
    symmetric_root.left.right = TreeNode(4)
    symmetric_root.right.left = TreeNode(4)
    symmetric_root.right.right = TreeNode(3)
    
    solution = Solution()
    print("测试用例1 - 对称二叉树:")
    print("递归方法:", solution.isSymmetric1(symmetric_root))
    print("迭代方法:", solution.isSymmetric2(symmetric_root))
    
    # 构建测试非对称二叉树:
    #     1
    #    / \
    #   2   2
    #    \   \
    #     3   3
    asymmetric_root = TreeNode(1)
    asymmetric_root.left = TreeNode(2)
    asymmetric_root.right = TreeNode(2)
    asymmetric_root.left.right = TreeNode(3)
    asymmetric_root.right.right = TreeNode(3)
    
    print("\n测试用例2 - 非对称二叉树:")
    print("递归方法:", solution.isSymmetric1(asymmetric_root))
    print("迭代方法:", solution.isSymmetric2(asymmetric_root))
    
    # 测试空树
    empty_root = None
    print("\n测试用例3 - 空树:")
    print("递归方法:", solution.isSymmetric1(empty_root))
    print("迭代方法:", solution.isSymmetric2(empty_root))
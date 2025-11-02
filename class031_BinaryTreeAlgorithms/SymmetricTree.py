# LeetCode 101. Symmetric Tree
# 题目链接: https://leetcode.cn/problems/symmetric-tree/
# 题目描述: 给定一个二叉树，检查它是否是镜像对称的。
# 例如，二叉树 [1,2,2,3,4,4,3] 是对称的。
#
# 解题思路:
# 1. 递归方法：同时遍历左右子树，检查是否镜像对称
# 2. 迭代方法：使用队列进行层序遍历，检查对称性
#
# 时间复杂度: O(n) - n为树中节点的数量，每个节点访问一次
# 空间复杂度: 
#   - 递归: O(h) - h为树的高度，递归调用栈的深度
#   - 迭代: O(w) - w为树的最大宽度，队列中最多存储一层的节点
# 是否为最优解: 是，这是检查对称二叉树的标准方法

from typing import Optional
from collections import deque

# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    # 方法1: 递归解法
    # 核心思想: 同时遍历左右子树，检查左子树的左节点是否等于右子树的右节点，
    #          左子树的右节点是否等于右子树的左节点
    def isSymmetricRecursive(self, root: Optional[TreeNode]) -> bool:
        """
        递归方法检查二叉树是否对称
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            如果二叉树对称返回True，否则返回False
        """
        if root is None:
            return True
        return self._isMirror(root.left, root.right)
    
    def _isMirror(self, left: Optional[TreeNode], right: Optional[TreeNode]) -> bool:
        """
        递归辅助函数，检查两个子树是否镜像对称
        
        Args:
            left: 左子树根节点
            right: 右子树根节点
            
        Returns:
            如果两个子树镜像对称返回True，否则返回False
        """
        # 两个节点都为空，对称
        if left is None and right is None:
            return True
        # 一个为空一个不为空，不对称
        if left is None or right is None:
            return False
        # 节点值不相等，不对称
        if left.val != right.val:
            return False
        # 递归检查左子树的左节点和右子树的右节点，
        # 以及左子树的右节点和右子树的左节点
        return (self._isMirror(left.left, right.right) and 
                self._isMirror(left.right, right.left))

    # 方法2: 迭代解法（BFS）
    # 核心思想: 使用队列进行层序遍历，每次入队两个节点进行比较
    def isSymmetricIterative(self, root: Optional[TreeNode]) -> bool:
        """
        迭代方法检查二叉树是否对称
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            如果二叉树对称返回True，否则返回False
        """
        if root is None:
            return True
        
        queue = deque()
        queue.append(root.left)
        queue.append(root.right)
        
        while queue:
            left = queue.popleft()
            right = queue.popleft()
            
            # 两个节点都为空，继续检查
            if left is None and right is None:
                continue
            # 一个为空一个不为空，不对称
            if left is None or right is None:
                return False
            # 节点值不相等，不对称
            if left.val != right.val:
                return False
            
            # 按对称顺序入队子节点
            queue.append(left.left)
            queue.append(right.right)
            queue.append(left.right)
            queue.append(right.left)
        
        return True

    # 提交如下的方法（使用递归版本）
    def isSymmetric(self, root: Optional[TreeNode]) -> bool:
        return self.isSymmetricRecursive(root)

# 测试用例
def main():
    solution = Solution()

    # 测试用例1: 对称二叉树
    #       1
    #      / \
    #     2   2
    #    / \ / \
    #   3  4 4  3
    root1 = TreeNode(1)
    root1.left = TreeNode(2)
    root1.right = TreeNode(2)
    root1.left.left = TreeNode(3)
    root1.left.right = TreeNode(4)
    root1.right.left = TreeNode(4)
    root1.right.right = TreeNode(3)
    
    result1 = solution.isSymmetric(root1)
    print(f"测试用例1结果: {result1}")  # 应该输出True

    # 测试用例2: 不对称二叉树
    #       1
    #      / \
    #     2   2
    #      \   \
    #       3   3
    root2 = TreeNode(1)
    root2.left = TreeNode(2)
    root2.right = TreeNode(2)
    root2.left.right = TreeNode(3)
    root2.right.right = TreeNode(3)
    
    result2 = solution.isSymmetric(root2)
    print(f"测试用例2结果: {result2}")  # 应该输出False

    # 测试用例3: 空树
    root3 = None
    result3 = solution.isSymmetric(root3)
    print(f"测试用例3结果: {result3}")  # 应该输出True

    # 测试用例4: 单节点树
    root4 = TreeNode(1)
    result4 = solution.isSymmetric(root4)
    print(f"测试用例4结果: {result4}")  # 应该输出True

    # 补充题目测试: LeetCode 100 - 相同的树
    print("\n=== 补充题目测试: 相同的树 ===")
    # 构造两棵相同的树
    tree1 = TreeNode(1)
    tree1.left = TreeNode(2)
    tree1.right = TreeNode(3)
    
    tree2 = TreeNode(1)
    tree2.left = TreeNode(2)
    tree2.right = TreeNode(3)
    
    def isSameTree(p: Optional[TreeNode], q: Optional[TreeNode]) -> bool:
        """
        判断两棵树是否相同
        
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
        return isSameTree(p.left, q.left) and isSameTree(p.right, q.right)
    
    same_result = isSameTree(tree1, tree2)
    print(f"相同的树测试: {same_result}")  # 应该输出True

if __name__ == "__main__":
    main()
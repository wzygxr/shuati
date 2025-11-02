from typing import Optional

# LeetCode 222. 完全二叉树的节点个数
# 题目链接: https://leetcode.cn/problems/count-complete-tree-nodes/
# 题目大意: 给你一棵完全二叉树的根节点 root ，求出该树的节点个数。
# 完全二叉树的定义：在完全二叉树中，除了最底层节点可能没填满外，其余每层节点数都达到最大值，
# 并且最下面一层的节点都集中在该层最左边的若干位置。

# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def countNodes1(self, root: Optional[TreeNode]) -> int:
        """
        方法1: 普通递归方法
        思路: 直接递归计算左子树和右子树的节点数，然后加1
        时间复杂度: O(n) - n是节点数量
        空间复杂度: O(h) - h是树的高度，递归调用栈的深度
        """
        if not root:
            return 0
        
        return 1 + self.countNodes1(root.left) + self.countNodes1(root.right)
    
    def countNodes2(self, root: Optional[TreeNode]) -> int:
        """
        方法2: 利用完全二叉树性质优化的方法
        思路: 
        1. 计算树的高度
        2. 利用完全二叉树的性质，如果左右子树高度相等，说明左子树是满二叉树
        3. 如果左右子树高度不等，说明右子树是满二叉树
        4. 满二叉树的节点数可以直接计算，不需要遍历
        时间复杂度: O(log²n) - 每次递归减少一半节点，每次计算高度需要O(logn)
        空间复杂度: O(logn) - 递归调用栈的深度
        """
        if not root:
            return 0
        
        # 计算左子树的高度
        left_height = self.getHeight(root.left)
        # 计算右子树的高度
        right_height = self.getHeight(root.right)
        
        if left_height == right_height:
            # 左右子树高度相等，说明左子树是满二叉树
            # 左子树节点数 = 2^left_height - 1
            # 总节点数 = 左子树节点数 + 根节点 + 右子树节点数
            return (1 << left_height) + self.countNodes2(root.right)
        else:
            # 左右子树高度不等，说明右子树是满二叉树
            # 右子树节点数 = 2^right_height - 1
            # 总节点数 = 右子树节点数 + 根节点 + 左子树节点数
            return (1 << right_height) + self.countNodes2(root.left)
    
    def getHeight(self, root: Optional[TreeNode]) -> int:
        """
        计算树的高度
        Args:
            root: 树的根节点
        Returns:
            树的高度
        """
        height = 0
        while root:
            height += 1
            root = root.left
        return height
    
    def countNodes3(self, root: Optional[TreeNode]) -> int:
        """
        方法3: 二分查找法
        思路: 
        1. 计算完全二叉树的高度
        2. 最后一层的节点数在1到2^h之间
        3. 使用二分查找确定最后一层有多少个节点
        时间复杂度: O(log²n) - 二分查找需要O(logn)，每次检查需要O(logn)
        空间复杂度: O(1) - 只使用常数额外空间
        """
        if not root:
            return 0
        
        # 计算树的高度
        height = self.getHeight(root)
        
        # 如果只有根节点
        if height == 1:
            return 1
        
        # 计算除最后一层外的节点数
        upper_count = (1 << (height - 1)) - 1
        
        # 二分查找最后一层的节点数
        left, right = 1, 1 << (height - 1)
        last_level_count = 0
        
        while left <= right:
            mid = left + (right - left) // 2
            if self.nodeExists(root, mid, height):
                last_level_count = mid
                left = mid + 1
            else:
                right = mid - 1
        
        return upper_count + last_level_count
    
    def nodeExists(self, root: Optional[TreeNode], index: int, height: int) -> bool:
        """
        检查最后一层第index个节点是否存在
        Args:
            root: 树的根节点
            index: 节点在最后一层的索引(从1开始)
            height: 树的高度
        Returns:
            节点是否存在
        """
        left, right = 1, 1 << (height - 1)
        
        for _ in range(height - 1):
            mid = left + (right - left) // 2
            if index <= mid:
                if root:
                    root = root.left
                right = mid
            else:
                if root:
                    root = root.right
                left = mid + 1
        
        return root is not None

# 测试代码
if __name__ == "__main__":
    # 构建测试完全二叉树:
    #       1
    #      / \
    #     2   3
    #    / \  /
    #   4   5 6
    root = TreeNode(1)
    root.left = TreeNode(2)
    root.right = TreeNode(3)
    root.left.left = TreeNode(4)
    root.left.right = TreeNode(5)
    root.right.left = TreeNode(6)
    
    solution = Solution()
    print("方法1结果:", solution.countNodes1(root))
    print("方法2结果:", solution.countNodes2(root))
    print("方法3结果:", solution.countNodes3(root))
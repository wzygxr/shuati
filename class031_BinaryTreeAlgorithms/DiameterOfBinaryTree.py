# LeetCode 543. Diameter of Binary Tree
# 题目链接: https://leetcode.cn/problems/diameter-of-binary-tree/
# 题目描述: 给定一棵二叉树，你需要计算它的直径长度。
# 一棵二叉树的直径长度是任意两个结点路径长度中的最大值。这条路径可能穿过也可能不穿过根结点。
#
# 解题思路:
# 1. 使用树形动态规划（Tree DP）的方法
# 2. 对于每个节点，直径等于左子树高度 + 右子树高度
# 3. 在递归计算高度的过程中，同时更新最大直径
# 4. 注意：直径不一定经过根节点，需要在所有节点中寻找最大值
#
# 时间复杂度: O(n) - n为树中节点的数量，每个节点访问一次
# 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
# 是否为最优解: 是，这是计算二叉树直径的标准方法

from typing import Optional

# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def __init__(self):
        """
        初始化Solution类
        """
        self.max_diameter = 0
    
    def diameterOfBinaryTree(self, root: Optional[TreeNode]) -> int:
        """
        计算二叉树的直径长度
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            二叉树的直径长度
        """
        self.max_diameter = 0
        self._height(root)
        return self.max_diameter
    
    def _height(self, node: Optional[TreeNode]) -> int:
        """
        计算二叉树的高度，同时更新最大直径
        
        Args:
            node: 当前节点
            
        Returns:
            以当前节点为根的子树的高度
        """
        if node is None:
            return 0
        
        # 递归计算左右子树的高度
        left_height = self._height(node.left)
        right_height = self._height(node.right)
        
        # 更新最大直径：当前节点的直径 = 左子树高度 + 右子树高度
        self.max_diameter = max(self.max_diameter, left_height + right_height)
        
        # 返回当前节点的高度：左右子树高度的最大值 + 1
        return max(left_height, right_height) + 1

    # 方法2: 使用非实例变量（函数式编程风格）
    def diameterOfBinaryTree2(self, root: Optional[TreeNode]) -> int:
        """
        使用非实例变量计算二叉树直径
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            二叉树的直径长度
        """
        max_diameter = [0]  # 使用列表实现引用传递效果
        
        def height(node: Optional[TreeNode]) -> int:
            if node is None:
                return 0
                
            left_height = height(node.left)
            right_height = height(node.right)
            
            # 更新最大直径
            max_diameter[0] = max(max_diameter[0], left_height + right_height)
            
            return max(left_height, right_height) + 1
        
        height(root)
        return max_diameter[0]

# 测试用例
def main():
    solution = Solution()

    # 测试用例1:
    #       1
    #      / \
    #     2   3
    #    / \     
    #   4   5    
    # 直径路径: 4->2->1->3 或 5->2->1->3，长度为3
    root1 = TreeNode(1)
    root1.left = TreeNode(2)
    root1.right = TreeNode(3)
    root1.left.left = TreeNode(4)
    root1.left.right = TreeNode(5)
    
    result1 = solution.diameterOfBinaryTree(root1)
    print(f"测试用例1结果: {result1}")  # 应该输出3

    # 测试用例2: 直径不经过根节点
    #       1
    #      / \
    #     2   3
    #    / \     
    #   4   5    
    #  /     \
    # 6       7
    # 直径路径: 6->4->2->5->7，长度为4
    root2 = TreeNode(1)
    root2.left = TreeNode(2)
    root2.right = TreeNode(3)
    root2.left.left = TreeNode(4)
    root2.left.right = TreeNode(5)
    root2.left.left.left = TreeNode(6)
    root2.left.right.right = TreeNode(7)
    
    result2 = solution.diameterOfBinaryTree(root2)
    print(f"测试用例2结果: {result2}")  # 应该输出4

    # 测试用例3: 单节点树
    root3 = TreeNode(1)
    result3 = solution.diameterOfBinaryTree(root3)
    print(f"测试用例3结果: {result3}")  # 应该输出0

    # 测试用例4: 空树
    root4 = None
    result4 = solution.diameterOfBinaryTree(root4)
    print(f"测试用例4结果: {result4}")  # 应该输出0

    # 测试用例5: 退化为链表的树
    #       1
    #        \
    #         2
    #          \
    #           3
    # 直径路径: 1->2->3，长度为2
    root5 = TreeNode(1)
    root5.right = TreeNode(2)
    root5.right.right = TreeNode(3)
    
    result5 = solution.diameterOfBinaryTree(root5)
    print(f"测试用例5结果: {result5}")  # 应该输出2

    # 补充题目测试: LeetCode 687 - 最长同值路径
    print("\n=== 补充题目测试: 最长同值路径 ===")
    #       5
    #      / \
    #     4   5
    #    / \   \
    #   1   1   5
    root6 = TreeNode(5)
    root6.left = TreeNode(4)
    root6.right = TreeNode(5)
    root6.left.left = TreeNode(1)
    root6.left.right = TreeNode(1)
    root6.right.right = TreeNode(5)
    
    def longest_univalue_path(root: Optional[TreeNode]) -> int:
        """
        计算二叉树中最长的同值路径长度
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            最长同值路径长度
        """
        max_length = [0]  # 使用列表实现引用传递
        
        def path_length(node: Optional[TreeNode]) -> int:
            if node is None:
                return 0
                
            left_length = path_length(node.left)
            right_length = path_length(node.right)
            
            # 计算左右子树的同值路径长度
            left_univalue = 0
            right_univalue = 0
            
            if node.left and node.left.val == node.val:
                left_univalue = left_length + 1
                
            if node.right and node.right.val == node.val:
                right_univalue = right_length + 1
                
            # 更新最大同值路径长度
            max_length[0] = max(max_length[0], left_univalue + right_univalue)
            
            # 返回以当前节点为起点的最长同值路径长度
            return max(left_univalue, right_univalue)
        
        path_length(root)
        return max_length[0]
    
    longest_result = longest_univalue_path(root6)
    print(f"最长同值路径: {longest_result}")  # 应该输出2

if __name__ == "__main__":
    main()
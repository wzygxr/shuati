# LeetCode 105. Construct Binary Tree from Preorder and Inorder Traversal
# 题目链接: https://leetcode.cn/problems/construct-binary-tree-from-preorder-and-inorder-traversal/
# 题目描述: 给定两个整数数组 preorder 和 inorder ，其中 preorder 是二叉树的前序遍历，
# inorder 是同一棵树的中序遍历，请构造二叉树并返回其根节点。
#
# 解题思路:
# 1. 递归构建：前序遍历的第一个元素是根节点
# 2. 在中序遍历中找到根节点的位置，确定左右子树的范围
# 3. 递归构建左右子树
# 4. 使用字典优化中序遍历中查找根节点位置的时间
#
# 时间复杂度: O(n) - n为树中节点的数量，每个节点处理一次
# 空间复杂度: O(n) - 存储中序遍历的索引映射，递归栈深度O(h)
# 是否为最优解: 是，这是构建二叉树的标准方法

from typing import List, Optional

# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def buildTree(self, preorder: List[int], inorder: List[int]) -> Optional[TreeNode]:
        """
        根据前序遍历和中序遍历构建二叉树
        
        Args:
            preorder: 前序遍历数组
            inorder: 中序遍历数组
            
        Returns:
            构建的二叉树的根节点
        """
        # 边界条件检查
        if not preorder or not inorder or len(preorder) != len(inorder):
            return None
        
        # 构建中序遍历的索引映射
        inorder_index_map = {val: idx for idx, val in enumerate(inorder)}
        
        return self._build_tree_helper(preorder, 0, len(preorder) - 1,
                                     inorder, 0, len(inorder) - 1, inorder_index_map)
    
    def _build_tree_helper(self, preorder: List[int], pre_start: int, pre_end: int,
                         inorder: List[int], in_start: int, in_end: int,
                         index_map: dict[int, int]) -> Optional[TreeNode]:
        """
        递归构建二叉树的辅助方法
        
        Args:
            preorder: 前序遍历数组
            pre_start: 前序遍历起始索引
            pre_end: 前序遍历结束索引
            inorder: 中序遍历数组
            in_start: 中序遍历起始索引
            in_end: 中序遍历结束索引
            index_map: 中序遍历值到索引的映射
            
        Returns:
            构建的子树根节点
        """
        # 递归终止条件：没有节点需要处理
        if pre_start > pre_end or in_start > in_end:
            return None
        
        # 前序遍历的第一个元素是根节点
        root_val = preorder[pre_start]
        root = TreeNode(root_val)
        
        # 在中序遍历中找到根节点的位置
        root_index_in_inorder = index_map[root_val]
        
        # 计算左子树的大小
        left_subtree_size = root_index_in_inorder - in_start
        
        # 递归构建左子树
        # 前序遍历中左子树范围: [pre_start + 1, pre_start + left_subtree_size]
        # 中序遍历中左子树范围: [in_start, root_index_in_inorder - 1]
        root.left = self._build_tree_helper(preorder, pre_start + 1, pre_start + left_subtree_size,
                                          inorder, in_start, root_index_in_inorder - 1, index_map)
        
        # 递归构建右子树
        # 前序遍历中右子树范围: [pre_start + left_subtree_size + 1, pre_end]
        # 中序遍历中右子树范围: [root_index_in_inorder + 1, in_end]
        root.right = self._build_tree_helper(preorder, pre_start + left_subtree_size + 1, pre_end,
                                          inorder, root_index_in_inorder + 1, in_end, index_map)
        
        return root

# 测试用例
def main():
    solution = Solution()

    # 测试用例1:
    # 前序遍历: [3,9,20,15,7]
    # 中序遍历: [9,3,15,20,7]
    # 构建的二叉树:
    #       3
    #      / \
    #     9  20
    #       /  \
    #      15   7
    preorder1 = [3, 9, 20, 15, 7]
    inorder1 = [9, 3, 15, 20, 7]
    
    root1 = solution.buildTree(preorder1, inorder1)
    print("测试用例1构建完成")
    print_inorder(root1)  # 应该输出: 9 3 15 20 7
    print()

    # 测试用例2: 单节点树
    preorder2 = [1]
    inorder2 = [1]
    root2 = solution.buildTree(preorder2, inorder2)
    print("测试用例2构建完成")
    print_inorder(root2)  # 应该输出: 1
    print()

    # 测试用例3: 完全二叉树
    # 前序遍历: [1,2,4,5,3,6,7]
    # 中序遍历: [4,2,5,1,6,3,7]
    # 构建的二叉树:
    #       1
    #      / \
    #     2   3
    #    / \ / \
    #   4  5 6 7
    preorder3 = [1, 2, 4, 5, 3, 6, 7]
    inorder3 = [4, 2, 5, 1, 6, 3, 7]
    
    root3 = solution.buildTree(preorder3, inorder3)
    print("测试用例3构建完成")
    print_inorder(root3)  # 应该输出: 4 2 5 1 6 3 7
    print()

def print_inorder(root: Optional[TreeNode]) -> None:
    """中序遍历打印二叉树（用于验证）"""
    if root is None:
        return
    print_inorder(root.left)
    print(root.val, end=" ")
    print_inorder(root.right)

if __name__ == "__main__":
    main()
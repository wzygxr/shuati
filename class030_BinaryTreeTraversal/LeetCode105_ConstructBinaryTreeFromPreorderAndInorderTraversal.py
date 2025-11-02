from typing import List, Optional

# LeetCode 105. 从前序与中序遍历序列构造二叉树
# 题目链接: https://leetcode.cn/problems/construct-binary-tree-from-preorder-and-inorder-traversal/
# 题目大意: 给定两个整数数组 preorder 和 inorder ，其中 preorder 是二叉树的先序遍历，inorder 是同一棵树的中序遍历，
# 请构造二叉树并返回其根节点。

# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def buildTree1(self, preorder: List[int], inorder: List[int]) -> Optional[TreeNode]:
        """
        方法1: 递归构建二叉树
        思路:
        1. 前序遍历的第一个元素是根节点
        2. 在中序遍历中找到根节点的位置，将中序遍历分为左子树和右子树
        3. 递归构建左子树和右子树
        时间复杂度: O(n) - n是节点数量，每个节点访问一次
        空间复杂度: O(n) - 存储哈希表和递归调用栈
        """
        # 创建中序遍历值到索引的映射，用于快速查找根节点位置
        inorder_map = {val: i for i, val in enumerate(inorder)}
        
        def build_tree_helper(pre_start: int, pre_end: int, 
                             in_start: int, in_end: int) -> Optional[TreeNode]:
            # 递归终止条件
            if pre_start > pre_end or in_start > in_end:
                return None
            
            # 前序遍历的第一个元素是根节点
            root_val = preorder[pre_start]
            root = TreeNode(root_val)
            
            # 在中序遍历中找到根节点的位置
            root_index = inorder_map[root_val]
            
            # 计算左子树的节点数量
            left_subtree_size = root_index - in_start
            
            # 递归构建左子树
            root.left = build_tree_helper(pre_start + 1, pre_start + left_subtree_size,
                                         in_start, root_index - 1)
            
            # 递归构建右子树
            root.right = build_tree_helper(pre_start + left_subtree_size + 1, pre_end,
                                          root_index + 1, in_end)
            
            return root
        
        return build_tree_helper(0, len(preorder) - 1, 0, len(inorder) - 1)
    
    def buildTree2(self, preorder: List[int], inorder: List[int]) -> Optional[TreeNode]:
        """
        方法2: 使用迭代器优化的递归方法
        思路: 使用一个全局索引跟踪前序遍历的当前位置
        时间复杂度: O(n) - n是节点数量，每个节点访问一次
        空间复杂度: O(n) - 存储哈希表和递归调用栈
        """
        # 创建中序遍历值到索引的映射，用于快速查找根节点位置
        inorder_map = {val: i for i, val in enumerate(inorder)}
        
        # 使用迭代器跟踪前序遍历的当前位置
        self.preorder_index = 0
        
        def build_tree_helper2(in_start: int, in_end: int) -> Optional[TreeNode]:
            # 递归终止条件
            if in_start > in_end:
                return None
            
            # 前序遍历的当前元素是根节点
            root_val = preorder[self.preorder_index]
            self.preorder_index += 1
            root = TreeNode(root_val)
            
            # 在中序遍历中找到根节点的位置
            root_index = inorder_map[root_val]
            
            # 递归构建左子树
            root.left = build_tree_helper2(in_start, root_index - 1)
            
            # 递归构建右子树
            root.right = build_tree_helper2(root_index + 1, in_end)
            
            return root
        
        return build_tree_helper2(0, len(inorder) - 1)

# 测试代码
if __name__ == "__main__":
    # 测试用例1: preorder = [3,9,20,15,7], inorder = [9,3,15,20,7]
    preorder1 = [3, 9, 20, 15, 7]
    inorder1 = [9, 3, 15, 20, 7]
    
    solution = Solution()
    root1 = solution.buildTree1(preorder1, inorder1)
    print("方法1构建的树根节点值:", root1.val if root1 else None)
    
    root2 = solution.buildTree2(preorder1, inorder1)
    print("方法2构建的树根节点值:", root2.val if root2 else None)
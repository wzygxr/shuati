# LeetCode 124. Binary Tree Maximum Path Sum
# 题目链接: https://leetcode.cn/problems/binary-tree-maximum-path-sum/
# 二叉树中的路径被定义为一条节点序列，序列中每对相邻节点之间都存在一条边。
# 同一个节点在一条路径序列中至多出现一次。该路径至少包含一个节点，且不一定经过根节点。
# 路径和是路径中各节点值的总和。
# 给你一个二叉树的根节点 root ，返回其最大路径和。
#
# 解题思路:
# 1. 使用树形动态规划（Tree DP）的方法
# 2. 对于每个节点，我们计算两个值：
#    - 以该节点为起点向下的最大路径和（可以用于连接父节点）
#    - 经过该节点的最大路径和（可以作为最终答案）
# 3. 递归处理左右子树，综合计算当前节点的信息
#
# 时间复杂度: O(n) - n为树中节点的数量，需要遍历所有节点
# 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
# 是否为最优解: 是，这是计算二叉树最大路径和的标准方法

from typing import Optional
import sys

# Definition for a binary tree node.
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

def maxPathSum(root: Optional[TreeNode]) -> int:
    """
    计算二叉树中的最大路径和
    
    Args:
        root: 二叉树的根节点
        
    Returns:
        最大路径和
    """
    # 用于存储递归过程中的信息
    class Info:
        def __init__(self, max_path_sum_from_root: int, max_path_sum_in_subtree: int):
            # 以当前节点为起点向下的最大路径和（可以连接父节点）
            self.max_path_sum_from_root = max_path_sum_from_root
            # 以当前节点为根的子树中的最大路径和（可以作为最终答案）
            self.max_path_sum_in_subtree = max_path_sum_in_subtree
    
    def process(node: Optional[TreeNode]) -> Info:
        if node is None:
            return Info(0, -sys.maxsize)
        
        # 递归处理左右子树
        left_info = process(node.left)
        right_info = process(node.right)
        
        # 计算以当前节点为起点向下的最大路径和
        # 可以选择不走子树（路径和为0），或者走左子树或右子树
        max_path_sum_from_root = max(0, 
                                    max(left_info.max_path_sum_from_root, 
                                        right_info.max_path_sum_from_root)) + node.val
        
        # 计算以当前节点为根的子树中的最大路径和
        # 可能的情况：
        # 1. 只包含当前节点
        # 2. 当前节点 + 左子树向下的最大路径
        # 3. 当前节点 + 右子树向下的最大路径
        # 4. 左子树向下的最大路径 + 当前节点 + 右子树向下的最大路径
        max_path_sum_in_subtree = node.val
        if left_info.max_path_sum_from_root > 0:
            max_path_sum_in_subtree += left_info.max_path_sum_from_root
        if right_info.max_path_sum_from_root > 0:
            max_path_sum_in_subtree += right_info.max_path_sum_from_root
            
        # 还需要考虑左右子树内部的最大路径和
        if left_info.max_path_sum_in_subtree != -sys.maxsize:
            max_path_sum_in_subtree = max(max_path_sum_in_subtree, left_info.max_path_sum_in_subtree)
        if right_info.max_path_sum_in_subtree != -sys.maxsize:
            max_path_sum_in_subtree = max(max_path_sum_in_subtree, right_info.max_path_sum_in_subtree)
            
        return Info(max_path_sum_from_root, max_path_sum_in_subtree)
    
    info = process(root)
    return info.max_path_sum_in_subtree

# 测试用例
if __name__ == "__main__":
    # 测试用例1:
    #       1
    #      / \
    #     2   3
    root1 = TreeNode(1)
    root1.left = TreeNode(2)
    root1.right = TreeNode(3)
    result1 = maxPathSum(root1)
    print(f"测试用例1结果: {result1}")  # 应该输出6 (2->1->3)

    # 测试用例2:
    #      -10
    #      /  \
    #     9   20
    #        /  \
    #       15   7
    root2 = TreeNode(-10)
    root2.left = TreeNode(9)
    root2.right = TreeNode(20)
    root2.right.left = TreeNode(15)
    root2.right.right = TreeNode(7)
    result2 = maxPathSum(root2)
    print(f"测试用例2结果: {result2}")  # 应该输出42 (15->20->7)

    # 测试用例3:
    #       5
    #      / \
    #    -3   4
    #    / \   \
    #   1   4  -2
    root3 = TreeNode(5)
    root3.left = TreeNode(-3)
    root3.right = TreeNode(4)
    root3.left.left = TreeNode(1)
    root3.left.right = TreeNode(4)
    root3.right.right = TreeNode(-2)
    result3 = maxPathSum(root3)
    print(f"测试用例3结果: {result3}")  # 应该输出10 (1->(-3)->5->4->(-2))
# LeetCode 366. Find Leaves of Binary Tree
# 题目链接: https://leetcode.com/problems/find-leaves-of-binary-tree/
# 题目描述: 给你一棵二叉树，收集树的节点，就像这样：收集并移除所有叶子，重复直到树为空。
#
# 解题思路:
# 1. 使用后序遍历计算每个节点到叶子节点的高度
# 2. 叶子节点高度为0，父节点高度为max(左子树高度, 右子树高度) + 1
# 3. 将相同高度的节点放在同一个列表中
# 4. 高度为0的节点是第一轮要删除的叶子节点，高度为1的节点是第二轮要删除的叶子节点，以此类推
#
# 时间复杂度: O(n) - n为树中节点的数量，每个节点访问一次
# 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
# 是否为最优解: 是，这是收集二叉树叶子节点的标准解法

from typing import List, Optional

# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

def findLeaves(root: Optional[TreeNode]) -> List[List[int]]:
    """
    收集二叉树的叶子节点
    
    Args:
        root: 二叉树的根节点
        
    Returns:
        按照删除顺序分组的节点值列表
    """
    result = []
    
    def calculate_height_and_collect(node: Optional[TreeNode]) -> int:
        """
        计算节点高度并按高度收集节点
        
        Args:
            node: 当前节点
            
        Returns:
            当前节点的高度（叶子节点高度为0）
        """
        # 基础情况：空节点高度为-1
        if node is None:
            return -1
        
        # 递归计算左右子树的高度
        left_height = calculate_height_and_collect(node.left)
        right_height = calculate_height_and_collect(node.right)
        
        # 当前节点的高度为左右子树最大高度+1
        current_height = max(left_height, right_height) + 1
        
        # 如果结果列表还没有当前高度对应的层级，则添加新列表
        if len(result) == current_height:
            result.append([])
        
        # 将当前节点添加到对应高度的列表中
        result[current_height].append(node.val)
        
        # 返回当前节点的高度给父节点使用
        return current_height
    
    calculate_height_and_collect(root)
    return result

# 测试用例
if __name__ == "__main__":
    # 测试用例1:
    #       1
    #      / \
    #     2   3
    #    / \
    #   4   5
    root1 = TreeNode(1)
    root1.left = TreeNode(2)
    root1.right = TreeNode(3)
    root1.left.left = TreeNode(4)
    root1.left.right = TreeNode(5)
    
    result1 = findLeaves(root1)
    print(f"测试用例1结果: {result1}")  # 应该输出[[4, 5, 3], [2], [1]]
    
    # 测试用例2: 空树
    root2 = None
    result2 = findLeaves(root2)
    print(f"测试用例2结果: {result2}")  # 应该输出[]
    
    # 测试用例3: 只有根节点
    root3 = TreeNode(1)
    result3 = findLeaves(root3)
    print(f"测试用例3结果: {result3}")  # 应该输出[[1]]
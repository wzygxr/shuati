from typing import Optional

# LeetCode 226. 翻转二叉树
# 题目链接: https://leetcode.cn/problems/invert-binary-tree/
# 题目大意: 给你一棵二叉树的根节点 root ，翻转这棵二叉树，并返回其根节点。

# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

# 辅助函数：打印二叉树（前序遍历）
def print_tree(root: Optional[TreeNode]) -> None:
    if root:
        print(root.val, end=" ")
        print_tree(root.left)
        print_tree(root.right)

class Solution:
    def invertTree1(self, root: Optional[TreeNode]) -> Optional[TreeNode]:
        """
        方法1: 递归实现翻转二叉树
        思路:
        1. 如果当前节点为空，直接返回
        2. 递归翻转左子树
        3. 递归翻转右子树
        4. 交换左右子树
        5. 返回根节点
        时间复杂度: O(n) - n是节点数量，每个节点访问一次
        空间复杂度: O(h) - h是树的高度，递归调用栈的深度
        """
        # 递归终止条件
        if not root:
            return None
        
        # 递归翻转左子树和右子树
        left = self.invertTree1(root.left)
        right = self.invertTree1(root.right)
        
        # 交换左右子树
        root.left = right
        root.right = left
        
        return root
    
    def invertTree2(self, root: Optional[TreeNode]) -> Optional[TreeNode]:
        """
        方法2: 迭代实现翻转二叉树
        思路:
        1. 使用队列进行层序遍历
        2. 对于每个节点，交换其左右子树
        3. 将左右子节点加入队列继续处理
        时间复杂度: O(n) - n是节点数量，每个节点访问一次
        空间复杂度: O(w) - w是树的最大宽度，队列中最多存储一层的节点
        """
        if not root:
            return None
        
        from collections import deque
        # 使用队列进行层序遍历
        queue = deque([root])
        
        while queue:
            # 取出队首节点
            current = queue.popleft()
            
            # 交换当前节点的左右子树
            current.left, current.right = current.right, current.left
            
            # 将左右子节点加入队列（如果存在）
            if current.left:
                queue.append(current.left)
            if current.right:
                queue.append(current.right)
        
        return root

# 测试代码
if __name__ == "__main__":
    # 构建测试二叉树:
    #     4
    #    / \
    #   2   7
    #  / \ / \
    # 1  3 6  9
    root = TreeNode(4)
    root.left = TreeNode(2)
    root.right = TreeNode(7)
    root.left.left = TreeNode(1)
    root.left.right = TreeNode(3)
    root.right.left = TreeNode(6)
    root.right.right = TreeNode(9)
    
    print("翻转前:")
    print_tree(root)
    
    # 翻转二叉树
    solution = Solution()
    inverted_root = solution.invertTree1(root)
    
    print("\n翻转后:")
    print_tree(inverted_root)
# LeetCode 543. 二叉树的直径
# 题目来源：LeetCode 543 https://leetcode.com/problems/diameter-of-binary-tree/
# 题目描述：给定一棵二叉树，计算它的直径长度。直径是指树中任意两个节点之间最长路径的长度。
# 算法思想：利用深度优先搜索计算每个节点的高度，同时更新最长路径长度（直径）
# 与树的重心的关系：树的直径与树的重心有密切关系，直径必然经过树的重心
# 解题思路：
# 1. 对于每个节点，计算经过该节点的最长路径长度（左子树深度+右子树深度）
# 2. 在计算深度的过程中，同时更新全局最大值（直径）
# 3. 返回整棵树的直径
# 时间复杂度：O(n)，每个节点访问一次
# 空间复杂度：O(h)，h为树高，最坏情况下为O(n)，用于递归栈

import sys

# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def diameterOfBinaryTree(self, root):
        """
        计算二叉树的直径
        :param root: 二叉树的根节点
        :return: 二叉树的直径长度
        """
        # 记录二叉树的直径（全局最大值）
        self.diameter = 0
        
        # 计算深度并更新直径
        self.depth(root)
        
        # 返回计算得到的最大直径
        return self.diameter
    
    def depth(self, node):
        """
        计算节点的深度，并在过程中更新直径
        核心思想：对于每个节点，经过该节点的最长路径长度等于左子树深度+右子树深度
        :param node: 当前节点
        :return: 以node为根的子树的最大深度
        """
        # 基础情况：空节点的深度为0
        if not node:
            return 0
        
        # 递归计算左右子树的深度
        # leftDepth表示以node.left为根的子树的最大深度
        leftDepth = self.depth(node.left)
        # rightDepth表示以node.right为根的子树的最大深度
        rightDepth = self.depth(node.right)
        
        # 更新直径：经过当前节点的最长路径为左子树深度+右子树深度
        # 这是因为从左子树的最深叶子节点经过当前节点到右子树的最深叶子节点的路径长度
        # 就是左子树深度+右子树深度
        self.diameter = max(self.diameter, leftDepth + rightDepth)
        
        # 返回以当前节点为根的子树的最大深度
        # 等于左右子树的最大深度加1（当前节点）
        return max(leftDepth, rightDepth) + 1
    
    def buildTree(self, nums, index):
        """
        根据数组构建二叉树
        :param nums: 数组，None表示空节点
        :param index: 当前索引
        :return: 构建好的树节点
        """
        if index >= len(nums) or nums[index] is None:
            return None
        
        node = TreeNode(nums[index])
        node.left = self.buildTree(nums, 2 * index + 1)
        node.right = self.buildTree(nums, 2 * index + 2)
        
        return node
    
    def printTree(self, root):
        """
        打印树的结构（用于调试）
        :param root: 二叉树的根节点
        """
        if not root:
            print("null ", end="")
            return
        
        print(str(root.val) + " ", end="")
        self.printTree(root.left)
        self.printTree(root.right)
    
    def test(self):
        """
        测试方法
        """
        # 测试用例1: [1,2,3,4,5]
        #       1
        #      / \
        #     2   3
        #    / \
        #   4   5
        # 直径为3（路径4->2->1->3）
        nums1 = [1, 2, 3, 4, 5]
        root1 = self.buildTree(nums1, 0)
        result1 = self.diameterOfBinaryTree(root1)
        print("测试用例1结果:", result1)  # 期望输出: 3
        
        # 测试用例2: [1,2]
        #   1
        #  /
        # 2
        # 直径为1（路径1->2）
        nums2 = [1, 2]
        root2 = self.buildTree(nums2, 0)
        result2 = self.diameterOfBinaryTree(root2)
        print("测试用例2结果:", result2)  # 期望输出: 1
        
        # 测试用例3: 空树
        # 直径为0
        root3 = None
        result3 = self.diameterOfBinaryTree(root3)
        print("测试用例3结果:", result3)  # 期望输出: 0
        
        # 测试用例4: 单节点树
        # 1
        # 直径为0
        nums4 = [1]
        root4 = self.buildTree(nums4, 0)
        result4 = self.diameterOfBinaryTree(root4)
        print("测试用例4结果:", result4)  # 期望输出: 0
        
        # 测试用例5: 不平衡树
        # 1
        #  \
        #   3
        #    \
        #     5
        # 直径为2（路径1->3->5）
        nums5 = [1, None, 3, None, None, None, 5]
        root5 = self.buildTree(nums5, 0)
        result5 = self.diameterOfBinaryTree(root5)
        print("测试用例5结果:", result5)  # 期望输出: 2

def main():
    solution = Solution()
    solution.test()

if __name__ == "__main__":
    main()
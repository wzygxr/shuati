from typing import Optional
from collections import deque

# LeetCode 100. 相同的树
# 题目链接: https://leetcode.cn/problems/same-tree/
# 题目大意: 给你两棵二叉树的根节点 p 和 q ，编写一个函数来检验这两棵树是否相同。
# 如果两个树在结构上相同，并且节点具有相同的值，则认为它们是相同的。

# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def isSameTree1(self, p: Optional[TreeNode], q: Optional[TreeNode]) -> bool:
        """
        方法1: 递归实现判断两棵树是否相同
        思路:
        1. 如果两个节点都为空，返回true
        2. 如果其中一个节点为空，另一个不为空，返回false
        3. 如果两个节点的值不相等，返回false
        4. 递归判断左子树和右子树是否相同
        5. 返回左右子树都相同的判断结果
        时间复杂度: O(min(m,n)) - m和n分别是两棵树的节点数
        空间复杂度: O(min(h1,h2)) - h1和h2分别是两棵树的高度，递归调用栈的深度
        """
        # 如果两个节点都为空，返回True
        if not p and not q:
            return True
        
        # 如果其中一个节点为空，另一个不为空，返回False
        if not p or not q:
            return False
        
        # 如果两个节点的值不相等，返回False
        if p.val != q.val:
            return False
        
        # 递归判断左子树和右子树是否相同
        return self.isSameTree1(p.left, q.left) and self.isSameTree1(p.right, q.right)
    
    def isSameTree2(self, p: Optional[TreeNode], q: Optional[TreeNode]) -> bool:
        """
        方法2: 迭代实现判断两棵树是否相同
        思路:
        1. 使用队列存储待比较的节点对
        2. 每次从队列中取出一对节点进行比较
        3. 如果节点对都为空，继续下一对
        4. 如果其中一个为空或值不相等，返回false
        5. 将左右子节点对加入队列继续比较
        时间复杂度: O(min(m,n)) - m和n分别是两棵树的节点数
        空间复杂度: O(min(w1,w2)) - w1和w2分别是两棵树的最大宽度
        """
        # 使用队列存储待比较的节点对
        queue = deque([p, q])
        
        while queue:
            # 取出一对节点
            node1 = queue.popleft()
            node2 = queue.popleft()
            
            # 如果两个节点都为空，继续下一对
            if not node1 and not node2:
                continue
            
            # 如果其中一个节点为空或值不相等，返回False
            if not node1 or not node2 or node1.val != node2.val:
                return False
            
            # 将左右子节点对加入队列继续比较
            queue.append(node1.left)
            queue.append(node2.left)
            queue.append(node1.right)
            queue.append(node2.right)
        
        return True

# 测试代码
if __name__ == "__main__":
    # 构建测试二叉树1:
    #     1
    #    / \
    #   2   3
    p1 = TreeNode(1)
    p1.left = TreeNode(2)
    p1.right = TreeNode(3)
    
    # 构建测试二叉树2:
    #     1
    #    / \
    #   2   3
    q1 = TreeNode(1)
    q1.left = TreeNode(2)
    q1.right = TreeNode(3)
    
    solution = Solution()
    print("测试用例1 - 相同的树:")
    print("递归方法:", solution.isSameTree1(p1, q1))
    print("迭代方法:", solution.isSameTree2(p1, q1))
    
    # 构建测试二叉树3:
    #     1
    #    /
    #   2
    p2 = TreeNode(1)
    p2.left = TreeNode(2)
    
    # 构建测试二叉树4:
    #     1
    #      \
    #       2
    q2 = TreeNode(1)
    q2.right = TreeNode(2)
    
    print("\n测试用例2 - 不同的树:")
    print("递归方法:", solution.isSameTree1(p2, q2))
    print("迭代方法:", solution.isSameTree2(p2, q2))
    
    # 测试空树
    empty1 = None
    empty2 = None
    print("\n测试用例3 - 空树:")
    print("递归方法:", solution.isSameTree1(empty1, empty2))
    print("迭代方法:", solution.isSameTree2(empty1, empty2))
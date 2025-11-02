from typing import List, Optional
from collections import deque

# LeetCode 107. 二叉树的层序遍历 II
# 题目链接: https://leetcode.cn/problems/binary-tree-level-order-traversal-ii/
# 题目大意: 给你二叉树的根节点 root ，返回其节点值 自底向上的层序遍历 。（即按从叶子节点所在层到根节点所在的层，逐层从左向右遍历）

# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def levelOrderBottom1(self, root: Optional[TreeNode]) -> List[List[int]]:
        """
        方法1: 先正常层序遍历，再反转结果
        思路:
        1. 使用队列进行正常的层序遍历，从上到下收集每层节点值
        2. 遍历完成后，将结果列表反转，得到自底向上的遍历结果
        时间复杂度: O(n) - n是节点数量，每个节点访问一次，反转操作是O(L)，L为层数
        空间复杂度: O(n) - 存储队列和结果
        """
        ans = []
        if root:
            queue = deque([root])
            
            while queue:
                size = len(queue)
                level = []
                
                # 处理当前层的所有节点
                for _ in range(size):
                    cur = queue.popleft()
                    level.append(cur.val)
                    
                    # 将子节点加入队列，供下一层处理
                    if cur.left:
                        queue.append(cur.left)
                    if cur.right:
                        queue.append(cur.right)
                
                # 将当前层的结果添加到最终答案中
                ans.append(level)
            
            # 反转结果，得到自底向上的遍历
            ans.reverse()
        
        return ans
    
    def levelOrderBottom2(self, root: Optional[TreeNode]) -> List[List[int]]:
        """
        方法2: 使用栈存储中间结果
        思路:
        1. 使用队列进行正常的层序遍历
        2. 使用栈存储每层的结果
        3. 遍历完成后，从栈中弹出结果，得到自底向上的遍历结果
        时间复杂度: O(n) - n是节点数量，每个节点访问一次
        空间复杂度: O(n) - 存储队列、栈和结果
        """
        ans = []
        if root:
            queue = deque([root])
            stack = []
            
            while queue:
                size = len(queue)
                level = []
                
                # 处理当前层的所有节点
                for _ in range(size):
                    cur = queue.popleft()
                    level.append(cur.val)
                    
                    # 将子节点加入队列，供下一层处理
                    if cur.left:
                        queue.append(cur.left)
                    if cur.right:
                        queue.append(cur.right)
                
                # 将当前层的结果压入栈中
                stack.append(level)
            
            # 从栈中弹出结果，得到自底向上的遍历
            while stack:
                ans.append(stack.pop())
        
        return ans
    
    def levelOrderBottom3(self, root: Optional[TreeNode]) -> List[List[int]]:
        """
        方法3: 在遍历过程中直接在列表开头插入
        思路:
        1. 使用队列进行正常的层序遍历
        2. 每层遍历完成后，将结果插入到结果列表的开头
        3. 这样最终结果就是自底向上的遍历
        时间复杂度: O(n) - n是节点数量，每个节点访问一次
        空间复杂度: O(n) - 存储队列和结果
        注意: 在列表开头插入元素的时间复杂度是O(L)，L为当前列表长度，总体时间复杂度仍为O(n)
        """
        ans = []
        if root:
            queue = deque([root])
            
            while queue:
                size = len(queue)
                level = []
                
                # 处理当前层的所有节点
                for _ in range(size):
                    cur = queue.popleft()
                    level.append(cur.val)
                    
                    # 将子节点加入队列，供下一层处理
                    if cur.left:
                        queue.append(cur.left)
                    if cur.right:
                        queue.append(cur.right)
                
                # 将当前层的结果插入到结果列表的开头
                ans.insert(0, level)
        
        return ans

# 测试代码
if __name__ == "__main__":
    # 测试用例1: [3,9,20,null,null,15,7]
    root1 = TreeNode(3)
    root1.left = TreeNode(9)
    root1.right = TreeNode(20)
    root1.right.left = TreeNode(15)
    root1.right.right = TreeNode(7)
    
    solution = Solution()
    print("方法1结果:")
    print(solution.levelOrderBottom1(root1))
    
    print("方法2结果:")
    print(solution.levelOrderBottom2(root1))
    
    print("方法3结果:")
    print(solution.levelOrderBottom3(root1))
    
    # 测试用例2: [1]
    root2 = TreeNode(1)
    print("单节点树结果:")
    print(solution.levelOrderBottom1(root2))
    
    # 测试用例3: []
    root3 = None
    print("空树结果:")
    print(solution.levelOrderBottom1(root3))
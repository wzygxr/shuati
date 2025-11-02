from typing import List, Optional
from collections import deque

# LeetCode 103. 二叉树的锯齿形层序遍历
# 题目链接: https://leetcode.cn/problems/binary-tree-zigzag-level-order-traversal/
# 题目大意: 给你二叉树的根节点 root ，返回其节点值的 锯齿形层序遍历 。（即先从左往右，再从右往左进行下一层遍历，以此类推，层与层之间交替进行）

# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def zigzagLevelOrder1(self, root: Optional[TreeNode]) -> List[List[int]]:
        """
        方法1: 使用双端队列实现锯齿形层序遍历
        思路:
        1. 使用一个布尔变量记录当前层的遍历方向（从左到右或从右到左）
        2. 对于每一层，根据遍历方向决定是从队列头部取节点还是从尾部取节点
        3. 添加子节点时也根据方向决定是添加到头部还是尾部
        时间复杂度: O(n) - n是节点数量，每个节点访问一次
        空间复杂度: O(n) - 存储队列和结果
        """
        ans = []
        if root:
            # 使用双端队列存储节点
            dq = deque([root])
            # True表示从左到右，False表示从右到左
            left_to_right = True
            
            while dq:
                size = len(dq)
                level = []
                
                if left_to_right:
                    # 从左到右遍历：从头部取节点，子节点添加到尾部
                    for _ in range(size):
                        node = dq.popleft()
                        level.append(node.val)
                        # 先添加左子节点，再添加右子节点
                        if node.left:
                            dq.append(node.left)
                        if node.right:
                            dq.append(node.right)
                else:
                    # 从右到左遍历：从尾部取节点，子节点添加到头部
                    for _ in range(size):
                        node = dq.pop()
                        level.append(node.val)
                        # 先添加右子节点，再添加左子节点
                        if node.right:
                            dq.appendleft(node.right)
                        if node.left:
                            dq.appendleft(node.left)
                
                ans.append(level)
                # 切换方向
                left_to_right = not left_to_right
        
        return ans
    
    def zigzagLevelOrder2(self, root: Optional[TreeNode]) -> List[List[int]]:
        """
        方法2: 使用普通队列实现锯齿形层序遍历
        思路:
        1. 使用普通队列进行层序遍历
        2. 使用一个布尔变量记录当前层是否需要反转
        3. 对于需要反转的层，在添加到结果之前进行反转
        时间复杂度: O(n) - n是节点数量，每个节点访问一次
        空间复杂度: O(n) - 存储队列和结果
        """
        ans = []
        if root:
            queue = deque([root])
            # True表示从左到右，False表示从右到左
            left_to_right = True
            
            while queue:
                size = len(queue)
                level = []
                
                # 正常的层序遍历
                for _ in range(size):
                    node = queue.popleft()
                    level.append(node.val)
                    if node.left:
                        queue.append(node.left)
                    if node.right:
                        queue.append(node.right)
                
                # 如果当前层需要从右到左，则反转列表
                if not left_to_right:
                    level.reverse()
                
                ans.append(level)
                # 切换方向
                left_to_right = not left_to_right
        
        return ans
    
    def zigzagLevelOrder3(self, root: Optional[TreeNode]) -> List[List[int]]:
        """
        方法3: 使用递归实现锯齿形层序遍历
        思路:
        1. 使用递归进行深度优先遍历
        2. 根据层数的奇偶性决定节点值的添加方向
        3. 奇数层从右到左，偶数层从左到右
        时间复杂度: O(n) - n是节点数量，每个节点访问一次
        空间复杂度: O(h) - h是树的高度，递归调用栈的深度
        """
        ans = []
        
        def dfs(node: Optional[TreeNode], level: int) -> None:
            if not node:
                return
            
            # 如果当前层还没有对应的列表，创建一个新的
            if len(ans) <= level:
                ans.append([])
            
            # 根据层数的奇偶性决定添加方向
            if level % 2 == 0:
                # 偶数层：从左到右，添加到列表末尾
                ans[level].append(node.val)
            else:
                # 奇数层：从右到左，添加到列表开头
                ans[level].insert(0, node.val)
            
            # 递归处理左右子树
            dfs(node.left, level + 1)
            dfs(node.right, level + 1)
        
        dfs(root, 0)
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
    print(solution.zigzagLevelOrder1(root1))
    
    print("方法2结果:")
    print(solution.zigzagLevelOrder2(root1))
    
    print("方法3结果:")
    print(solution.zigzagLevelOrder3(root1))
    
    # 测试用例2: [1]
    root2 = TreeNode(1)
    print("单节点树结果:")
    print(solution.zigzagLevelOrder1(root2))
    
    # 测试用例3: []
    root3 = None
    print("空树结果:")
    print(solution.zigzagLevelOrder1(root3))
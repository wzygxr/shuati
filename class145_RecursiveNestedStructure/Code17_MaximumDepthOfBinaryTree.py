# LeetCode 104. Maximum Depth of Binary Tree
# 二叉树的最大深度
# 题目来源：https://leetcode.cn/problems/maximum-depth-of-binary-tree/

from collections import deque

"""
问题描述：
给定一个二叉树，找出其最大深度。
二叉树的深度为根节点到最远叶子节点的最长路径上的节点数。

解题思路：
1. 递归方法：使用深度优先搜索（DFS），计算左右子树的最大深度，取较大值加1
2. 迭代方法：使用广度优先搜索（BFS），逐层处理节点，记录层数

时间复杂度：O(N)，其中N是树中的节点数，每个节点只被访问一次
空间复杂度：
  - 递归：最坏情况下O(N)（树为链状），平均O(log N)（平衡树）
  - 迭代：O(N)（队列最多存储树的最宽层的所有节点）
"""

# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class MaximumDepthOfBinaryTree:
    """计算二叉树最大深度的类，包含递归和迭代两种实现方式"""
    
    def max_depth_recursive(self, root: TreeNode) -> int:
        """
        递归方法计算二叉树的最大深度
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            int: 二叉树的最大深度
        """
        # 基本情况：空节点深度为0
        if not root:
            return 0
        
        # 递归计算左子树的最大深度
        left_depth = self.max_depth_recursive(root.left)
        # 递归计算右子树的最大深度
        right_depth = self.max_depth_recursive(root.right)
        
        # 当前树的最大深度 = max(左子树最大深度, 右子树最大深度) + 1
        return max(left_depth, right_depth) + 1
    
    def max_depth_iterative(self, root: TreeNode) -> int:
        """
        迭代方法计算二叉树的最大深度（使用BFS）
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            int: 二叉树的最大深度
        """
        # 基本情况：空树深度为0
        if not root:
            return 0
        
        # 使用双端队列进行广度优先搜索
        queue = deque([root])
        depth = 0
        
        # 逐层处理节点
        while queue:
            # 当前层的节点数量
            level_size = len(queue)
            
            # 处理当前层的所有节点
            for _ in range(level_size):
                current = queue.popleft()
                
                # 将下一层的节点加入队列
                if current.left:
                    queue.append(current.left)
                if current.right:
                    queue.append(current.right)
            
            # 处理完一层，深度加1
            depth += 1
        
        return depth
    
    def max_depth_dfs_iterative(self, root: TreeNode) -> int:
        """
        使用DFS迭代方式计算二叉树的最大深度
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            int: 二叉树的最大深度
        """
        if not root:
            return 0
        
        # 使用栈存储节点和对应的深度
        stack = [(root, 1)]
        max_depth = 0
        
        while stack:
            node, current_depth = stack.pop()
            
            # 更新最大深度
            if current_depth > max_depth:
                max_depth = current_depth
            
            # 将子节点加入栈中，深度加1
            # 注意先压入右子节点，再压入左子节点，这样弹出时先处理左子节点
            if node.right:
                stack.append((node.right, current_depth + 1))
            if node.left:
                stack.append((node.left, current_depth + 1))
        
        return max_depth

# 测试代码
if __name__ == "__main__":
    solution = MaximumDepthOfBinaryTree()
    
    # 构建测试用例1：[3,9,20,null,null,15,7]
    #      3
    #     / \
    #    9  20
    #      /  \
    #     15   7
    root1 = TreeNode(3)
    root1.left = TreeNode(9)
    root1.right = TreeNode(20)
    root1.right.left = TreeNode(15)
    root1.right.right = TreeNode(7)
    
    # 测试递归方法
    print("递归方法 - 测试用例1的最大深度:", solution.max_depth_recursive(root1))
    # 测试迭代BFS方法
    print("迭代BFS方法 - 测试用例1的最大深度:", solution.max_depth_iterative(root1))
    # 测试迭代DFS方法
    print("迭代DFS方法 - 测试用例1的最大深度:", solution.max_depth_dfs_iterative(root1))
    
    # 构建测试用例2：[1,null,2]
    #    1
    #     \
    #      2
    root2 = TreeNode(1)
    root2.right = TreeNode(2)
    
    print("递归方法 - 测试用例2的最大深度:", solution.max_depth_recursive(root2))
    print("迭代BFS方法 - 测试用例2的最大深度:", solution.max_depth_iterative(root2))
    print("迭代DFS方法 - 测试用例2的最大深度:", solution.max_depth_dfs_iterative(root2))
    
    # 测试空树
    root3 = None
    print("递归方法 - 空树的最大深度:", solution.max_depth_recursive(root3))
    print("迭代BFS方法 - 空树的最大深度:", solution.max_depth_iterative(root3))
    print("迭代DFS方法 - 空树的最大深度:", solution.max_depth_dfs_iterative(root3))
    
    # 测试单节点树
    root4 = TreeNode(1)
    print("递归方法 - 单节点树的最大深度:", solution.max_depth_recursive(root4))
    print("迭代BFS方法 - 单节点树的最大深度:", solution.max_depth_iterative(root4))
    print("迭代DFS方法 - 单节点树的最大深度:", solution.max_depth_dfs_iterative(root4))

"""
性能分析：

1. 递归实现：
   - 时间复杂度：O(N)，每个节点都会被访问一次
   - 空间复杂度：
     - 最好情况：O(log N)，对于完全平衡的二叉树
     - 最坏情况：O(N)，对于链状树（每个节点只有一个子节点）
     - 平均情况：O(log N)
   - 优点：代码简洁，易于理解
   - 缺点：对于非常深的树可能导致栈溢出

2. 迭代BFS实现：
   - 时间复杂度：O(N)，每个节点都会被访问一次
   - 空间复杂度：O(N)，队列在最坏情况下存储树的最宽层的所有节点
   - 优点：避免了递归调用栈溢出的风险
   - 缺点：代码相对复杂

3. 迭代DFS实现：
   - 时间复杂度：O(N)，每个节点都会被访问一次
   - 空间复杂度：O(N)，栈在最坏情况下存储从根到叶的所有节点
   - 优点：对于不平衡的树，可能比BFS更节省空间
   - 缺点：需要手动维护深度信息

工程化考量：
1. Python中的递归深度限制：Python默认的递归深度限制约为1000，对于深度超过此限制的树，递归方法会抛出RecursionError
2. 对于大型树，应优先选择迭代实现
3. 可以根据树的特性选择合适的迭代方法：
   - 对于宽树（每一层节点很多）：DFS可能更节省空间
   - 对于深树（层级很多）：BFS和DFS空间复杂度相似
4. 实际应用中，可以添加异常处理和日志记录，以增强代码的健壮性
5. 可以添加性能监控，在处理大规模数据时进行性能分析

Python语言特性利用：
1. 使用collections.deque实现高效的队列操作
2. 利用Python的简洁语法，使递归实现更加清晰
3. 使用元组存储节点和深度信息，简化迭代DFS的实现
"""
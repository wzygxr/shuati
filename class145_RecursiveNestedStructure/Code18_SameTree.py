# LeetCode 100. Same Tree
# 相同的树
# 题目来源：https://leetcode.cn/problems/same-tree/

from collections import deque

"""
问题描述：
给你两棵二叉树的根节点 p 和 q，编写一个函数来检验这两棵树是否相同。
如果两个树在结构上相同，并且节点具有相同的值，则认为它们是相同的。

解题思路：
1. 递归方法：深度优先搜索，同时遍历两棵树的每个节点
2. 迭代BFS方法：使用队列同时处理两棵树的节点
3. 迭代DFS方法：使用栈同时处理两棵树的节点

时间复杂度：O(N)，其中N是树中的节点数，每个节点只被访问一次
空间复杂度：
  - 递归：最坏情况下O(N)（树为链状），平均O(log N)（平衡树）
  - 迭代BFS：O(W)，W是树中最宽层的节点数
  - 迭代DFS：O(H)，H是树的高度
"""

# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class SameTree:
    """判断两棵二叉树是否相同的类，包含递归和迭代两种实现方式"""
    
    def is_same_tree_recursive(self, p: TreeNode, q: TreeNode) -> bool:
        """
        递归方法判断两棵二叉树是否相同
        
        Args:
            p: 第一棵二叉树的根节点
            q: 第二棵二叉树的根节点
            
        Returns:
            bool: 如果两棵树相同返回True，否则返回False
        """
        # 情况1：两个节点都为空，它们是相同的
        if p is None and q is None:
            return True
        
        # 情况2：一个节点为空，另一个不为空，它们不相同
        if p is None or q is None:
            return False
        
        # 情况3：两个节点都不为空，比较它们的值和子树
        # 1. 比较当前节点的值
        # 2. 递归比较左子树
        # 3. 递归比较右子树
        # 只有当这三个条件都满足时，两棵树才相同
        return (p.val == q.val) and \
               self.is_same_tree_recursive(p.left, q.left) and \
               self.is_same_tree_recursive(p.right, q.right)
    
    def is_same_tree_iterative_bfs(self, p: TreeNode, q: TreeNode) -> bool:
        """
        迭代方法判断两棵二叉树是否相同（使用队列，BFS）
        
        Args:
            p: 第一棵二叉树的根节点
            q: 第二棵二叉树的根节点
            
        Returns:
            bool: 如果两棵树相同返回True，否则返回False
        """
        # 使用队列同时存储两棵树的对应节点
        queue = deque()
        queue.append(p)
        queue.append(q)
        
        # 当队列不为空时，继续处理
        while queue:
            # 从队列中取出两棵树的对应节点
            node_p = queue.popleft()
            node_q = queue.popleft()
            
            # 如果两个节点都为空，继续处理下一对节点
            if node_p is None and node_q is None:
                continue
            
            # 如果一个节点为空另一个不为空，或者节点值不相同，返回False
            if node_p is None or node_q is None or node_p.val != node_q.val:
                return False
            
            # 将两个节点的左子节点加入队列
            queue.append(node_p.left)
            queue.append(node_q.left)
            
            # 将两个节点的右子节点加入队列
            queue.append(node_p.right)
            queue.append(node_q.right)
        
        # 所有节点都比较完成，两棵树相同
        return True
    
    def is_same_tree_iterative_dfs(self, p: TreeNode, q: TreeNode) -> bool:
        """
        迭代方法判断两棵二叉树是否相同（使用栈，DFS）
        
        Args:
            p: 第一棵二叉树的根节点
            q: 第二棵二叉树的根节点
            
        Returns:
            bool: 如果两棵树相同返回True，否则返回False
        """
        # 使用栈同时存储两棵树的对应节点
        stack = []
        stack.append(p)
        stack.append(q)
        
        # 当栈不为空时，继续处理
        while stack:
            # 从栈中取出两棵树的对应节点
            node_q = stack.pop()
            node_p = stack.pop()
            
            # 如果两个节点都为空，继续处理下一对节点
            if node_p is None and node_q is None:
                continue
            
            # 如果一个节点为空另一个不为空，或者节点值不相同，返回False
            if node_p is None or node_q is None or node_p.val != node_q.val:
                return False
            
            # 将两个节点的右子节点加入栈
            stack.append(node_p.right)
            stack.append(node_q.right)
            
            # 将两个节点的左子节点加入栈（注意顺序，先右后左，这样出栈时先处理左子节点）
            stack.append(node_p.left)
            stack.append(node_q.left)
        
        # 所有节点都比较完成，两棵树相同
        return True

# 测试代码
if __name__ == "__main__":
    solution = SameTree()
    
    # 测试用例1：两棵相同的树
    # 树1:    1           树2:    1
    #        / \                 / \
    #       2   3               2   3
    p1 = TreeNode(1)
    p1.left = TreeNode(2)
    p1.right = TreeNode(3)
    
    q1 = TreeNode(1)
    q1.left = TreeNode(2)
    q1.right = TreeNode(3)
    
    print("测试用例1 - 递归方法:", solution.is_same_tree_recursive(p1, q1))
    print("测试用例1 - 迭代BFS方法:", solution.is_same_tree_iterative_bfs(p1, q1))
    print("测试用例1 - 迭代DFS方法:", solution.is_same_tree_iterative_dfs(p1, q1))
    
    # 测试用例2：两棵不同的树
    # 树1:    1           树2:    1
    #        /                     \
    #       2                       2
    p2 = TreeNode(1)
    p2.left = TreeNode(2)
    
    q2 = TreeNode(1)
    q2.right = TreeNode(2)
    
    print("测试用例2 - 递归方法:", solution.is_same_tree_recursive(p2, q2))
    print("测试用例2 - 迭代BFS方法:", solution.is_same_tree_iterative_bfs(p2, q2))
    print("测试用例2 - 迭代DFS方法:", solution.is_same_tree_iterative_dfs(p2, q2))
    
    # 测试用例3：两棵不同的树
    # 树1:    1           树2:    1
    #        / \                 / \
    #       2   1               1   2
    p3 = TreeNode(1)
    p3.left = TreeNode(2)
    p3.right = TreeNode(1)
    
    q3 = TreeNode(1)
    q3.left = TreeNode(1)
    q3.right = TreeNode(2)
    
    print("测试用例3 - 递归方法:", solution.is_same_tree_recursive(p3, q3))
    print("测试用例3 - 迭代BFS方法:", solution.is_same_tree_iterative_bfs(p3, q3))
    print("测试用例3 - 迭代DFS方法:", solution.is_same_tree_iterative_dfs(p3, q3))
    
    # 测试用例4：两棵空树
    print("测试用例4 - 递归方法:", solution.is_same_tree_recursive(None, None))
    print("测试用例4 - 迭代BFS方法:", solution.is_same_tree_iterative_bfs(None, None))
    print("测试用例4 - 迭代DFS方法:", solution.is_same_tree_iterative_dfs(None, None))
    
    # 测试用例5：一棵树为空，另一棵不为空
    print("测试用例5 - 递归方法:", solution.is_same_tree_recursive(TreeNode(1), None))
    print("测试用例5 - 迭代BFS方法:", solution.is_same_tree_iterative_bfs(TreeNode(1), None))
    print("测试用例5 - 迭代DFS方法:", solution.is_same_tree_iterative_dfs(TreeNode(1), None))

"""
性能分析：

1. 递归实现：
   - 时间复杂度：O(N)，每个节点都会被访问一次
   - 空间复杂度：
     - 最好情况：O(log N)，对于完全平衡的二叉树
     - 最坏情况：O(N)，对于链状树（每个节点只有一个子节点）
     - 平均情况：O(log N)
   - 优点：代码简洁，逻辑清晰，容易实现和理解
   - 缺点：对于非常深的树可能导致栈溢出

2. 迭代BFS实现：
   - 时间复杂度：O(N)，每个节点都会被访问一次
   - 空间复杂度：O(W)，其中W是树中最宽层的节点数
   - 优点：避免了递归调用栈溢出的风险
   - 缺点：代码相对复杂，需要额外的数据结构（队列）

3. 迭代DFS实现：
   - 时间复杂度：O(N)，每个节点都会被访问一次
   - 空间复杂度：O(H)，其中H是树的高度
   - 优点：对于不平衡的树，可能比BFS更节省空间
   - 缺点：需要手动维护栈，实现相对复杂

Python语言特性利用：
1. 使用collections.deque实现高效的队列操作
2. 利用Python简洁的语法使递归实现更加清晰
3. 利用Python的None值进行空节点的处理
4. 可以使用and运算符的短路特性，提前返回False

工程化考量：
1. Python中的递归深度限制：默认递归深度限制约为1000，对于深度超过此限制的树，递归方法会抛出RecursionError
2. 对于大规模数据，可以考虑使用迭代方法或增加递归深度限制（通过sys.setrecursionlimit）
3. 在实际应用中，可以添加日志记录和性能监控
4. 可以考虑使用装饰器来缓存中间结果或进行性能统计
5. 对于生产环境，建议添加适当的异常处理和边界条件检查
"""
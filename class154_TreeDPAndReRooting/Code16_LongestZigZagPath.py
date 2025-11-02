# 二叉树中的最长交错路径
# 题目链接：https://leetcode.com/problems/longest-zigzag-path-in-a-binary-tree/
# 给定一个二叉树，找到最长的路径，这个路径中的每个相邻节点在二叉树中都处于不同的父-子关系中。
# 例如，路径是父节点的右子节点，然后是左子节点，接着是右子节点等。

'''
题目解析：
这是一个树形动态规划问题。我们需要找到二叉树中最长的交错路径，即路径中相邻节点交替左右子节点关系。

算法思路：
对于每个节点，我们可以从两个方向到达它：
1. 从父节点的左侧到达（left direction）
2. 从父节点的右侧到达（right direction）

对于每个节点，我们可以计算两个值：
- 如果从左侧到达该节点，那么它的最长交错路径长度
- 如果从右侧到达该节点，那么它的最长交错路径长度

状态转移方程：
- 如果当前节点有左子节点，那么从左侧到达左子节点的路径长度 = 从右侧到达当前节点的路径长度 + 1
- 如果当前节点有右子节点，那么从右侧到达右子节点的路径长度 = 从左侧到达当前节点的路径长度 + 1

时间复杂度：O(n) - 每个节点只访问一次
空间复杂度：O(h) - h为树的高度，最坏情况下为O(n)
是否为最优解：是，这是解决二叉树最长交错路径问题的最优方法

边界情况：
- 空树：路径长度为0
- 单节点树：路径长度为0（因为没有相邻节点）
- 只有一条直线的树：最长路径长度为1（因为只能交替一次）

与机器学习/深度学习的联系：
- 树结构在决策树和随机森林算法中有广泛应用
- 路径寻找问题与图神经网络（GNN）中的路径分析类似
- 最长路径问题与自然语言处理中的最长依赖路径相关

相关题目链接：
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code16_LongestZigZagPath.java
Python实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code16_LongestZigZagPath.py
C++实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code16_LongestZigZagPath.cpp
'''

# Definition for a binary tree node.
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def longestZigZag(self, root: TreeNode) -> int:
        # 边界条件处理：空树
        if not root:
            return 0
        
        # 初始化最长长度
        max_length = [0]
        
        # 从根节点开始，尝试向左和向右的路径
        def dfs(node, direction, length):
            # 更新全局最大长度
            max_length[0] = max(max_length[0], length)
            
            # 如果还有左子节点
            if node.left:
                # 如果当前是从右侧来的，或者是根节点，那么向左走可以形成交错路径
                if direction != 0:  # 不是从左侧来的
                    dfs(node.left, 0, length + 1)
                else:
                    # 如果当前是从左侧来的，那么向左走不能形成交错路径，需要重新开始
                    dfs(node.left, 0, 1)
            
            # 如果还有右子节点
            if node.right:
                # 如果当前是从左侧来的，或者是根节点，那么向右走可以形成交错路径
                if direction != 1:  # 不是从右侧来的
                    dfs(node.right, 1, length + 1)
                else:
                    # 如果当前是从右侧来的，那么向右走不能形成交错路径，需要重新开始
                    dfs(node.right, 1, 1)
        
        # 初始方向设为-1（表示根节点没有父节点）
        dfs(root, -1, 0)
        
        return max_length[0]

# 优化版本：使用返回值而不是可变列表
class Solution2:
    def longestZigZag(self, root: TreeNode) -> int:
        # 边界条件处理：空树
        if not root:
            return 0
        
        # 初始化最长长度
        self.max_length = 0
        
        # 从根节点开始，尝试向左和向右的路径
        self._dfs(root, -1, 0)
        
        return self.max_length
    
    def _dfs(self, node, direction, length):
        # 更新全局最大长度
        self.max_length = max(self.max_length, length)
        
        # 如果还有左子节点
        if node.left:
            # 如果当前是从右侧来的，或者是根节点，那么向左走可以形成交错路径
            if direction != 0:  # 不是从左侧来的
                self._dfs(node.left, 0, length + 1)
            else:
                # 如果当前是从左侧来的，那么向左走不能形成交错路径，需要重新开始
                self._dfs(node.left, 0, 1)
        
        # 如果还有右子节点
        if node.right:
            # 如果当前是从左侧来的，或者是根节点，那么向右走可以形成交错路径
            if direction != 1:  # 不是从右侧来的
                self._dfs(node.right, 1, length + 1)
            else:
                # 如果当前是从右侧来的，那么向右走不能形成交错路径，需要重新开始
                self._dfs(node.right, 1, 1)

# 更简洁的实现：使用返回值表示从左和从右的最长路径
class Solution3:
    def longestZigZag(self, root: TreeNode) -> int:
        # 边界条件处理：空树
        if not root:
            return 0
        
        # 初始化最长长度
        self.max_length = 0
        self._dfs(root)
        
        return self.max_length
    
    def _dfs(self, node):
        # 递归终止条件：节点为空
        if not node:
            # 空节点返回-1，表示无法继续延伸路径
            return (-1, -1)
        
        # 递归计算左右子节点的最长交错路径
        left_left, left_right = self._dfs(node.left)
        right_left, right_right = self._dfs(node.right)
        
        # 计算从当前节点向左走的最长路径：当前节点 -> 左子节点 -> 右子节点...
        current_left = left_right + 1
        
        # 计算从当前节点向右走的最长路径：当前节点 -> 右子节点 -> 左子节点...
        current_right = right_left + 1
        
        # 更新全局最大长度
        self.max_length = max(self.max_length, current_left, current_right)
        
        # 返回从当前节点向左和向右走的最长路径长度
        return (current_left, current_right)

# 非递归实现（使用栈模拟DFS）
class SolutionIterative:
    def longestZigZag(self, root: TreeNode) -> int:
        # 边界条件处理：空树
        if not root:
            return 0
        
        max_length = 0
        
        # 使用栈来模拟递归过程
        # 每个栈元素是一个三元组：(节点, 方向, 当前路径长度)
        stack = [(root, -1, 0)]  # -1表示根节点没有父节点
        
        while stack:
            node, direction, length = stack.pop()
            
            # 更新最大长度
            if length > max_length:
                max_length = length
            
            # 注意：由于栈是后进先出，所以我们需要先压入右子节点，再压入左子节点
            # 这样才能保证先处理左子节点
            if node.right:
                # 计算向右子节点走的情况
                if direction != 1:  # 不是从右侧来的
                    stack.append((node.right, 1, length + 1))
                else:
                    stack.append((node.right, 1, 1))
            
            if node.left:
                # 计算向左子节点走的情况
                if direction != 0:  # 不是从左侧来的
                    stack.append((node.left, 0, length + 1))
                else:
                    stack.append((node.left, 0, 1))
        
        return max_length

# 测试代码
if __name__ == "__main__":
    # 测试用例1: [1,null,1,1,1,null,null,1,1,null,1,null,null,null,1]
    # 最长路径是 [1,1,1,1,1]，长度为3
    root1 = TreeNode(1)
    root1.right = TreeNode(1)
    root1.right.left = TreeNode(1)
    root1.right.right = TreeNode(1)
    root1.right.left.left = TreeNode(1)
    root1.right.left.right = TreeNode(1)
    root1.right.left.left.right = TreeNode(1)
    
    solution = Solution()
    print("测试用例1结果:", solution.longestZigZag(root1))  # 预期输出: 3
    
    # 测试用例2: [1,1,1,null,1,null,null,1,1,null,1]
    # 最长路径是 [1,1,1,1]，长度为3
    root2 = TreeNode(1)
    root2.left = TreeNode(1)
    root2.right = TreeNode(1)
    root2.left.right = TreeNode(1)
    root2.left.right.left = TreeNode(1)
    root2.left.right.right = TreeNode(1)
    root2.left.right.left.right = TreeNode(1)
    
    print("测试用例2结果:", solution.longestZigZag(root2))  # 预期输出: 3
    
    # 测试用例3: [1]
    # 单节点树，最长路径长度为0
    root3 = TreeNode(1)
    
    print("测试用例3结果:", solution.longestZigZag(root3))  # 预期输出: 0
    
    # 测试Solution2、Solution3和迭代版本
    solution2 = Solution2()
    solution3 = Solution3()
    solution_iterative = SolutionIterative()
    
    print("Solution2 测试用例1结果:", solution2.longestZigZag(root1))  # 预期输出: 3
    print("Solution3 测试用例1结果:", solution3.longestZigZag(root1))  # 预期输出: 3
    print("迭代版本 测试用例1结果:", solution_iterative.longestZigZag(root1))  # 预期输出: 3

'''
工程化考量：
1. 异常处理：
   - 处理了空树的边界情况
   - 处理了单节点树的特殊情况
   - 在递归过程中自动处理null节点

2. 性能优化：
   - 避免重复计算：每个节点只访问一次
   - 提供了非递归实现，避免深层递归可能导致的栈溢出
   - 时间复杂度为O(n)，空间复杂度为O(h)

3. 代码质量：
   - 提供了三种实现方式：使用列表引用、使用类变量和返回左右路径长度
   - 还提供了非递归迭代实现
   - 添加了详细的注释说明算法思路和参数含义
   - 包含多个测试用例验证正确性

4. 可扩展性：
   - 如果需要返回具体的最长路径而不仅仅是长度，可以在递归中记录路径
   - 如果需要处理N叉树，可以修改算法以考虑多个子节点的情况

5. 调试技巧：
   - 可以在递归函数中添加打印语句，输出当前节点的值、方向和路径长度
   - 对于复杂树结构，可以使用图形化工具可视化树的结构
   - 在Python中可以使用sys.setrecursionlimit调整递归深度限制

6. Python特有优化：
   - 使用列表作为可变对象来在递归中传递和修改值
   - 使用元组返回多个值，使代码更简洁
   - 利用类变量在方法间共享状态

7. 算法安全与业务适配：
   - 对于非常深的树，递归版本可能会导致栈溢出，此时迭代版本更安全
   - 对于大规模数据，可以使用非递归DFS或BFS实现
   - 代码中添加了适当的边界检查，确保程序不会崩溃
'''
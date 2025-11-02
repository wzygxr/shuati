# 子树中的最大平均值
# 题目来源：LeetCode 1120. Maximum Average Subtree
# 题目链接：https://leetcode.com/problems/maximum-average-subtree/
# 测试链接：https://leetcode.com/problems/maximum-average-subtree/
# 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

'''
题目解析：
这是一个树形动态规划问题。我们需要计算二叉树中所有可能子树的平均值，并找出其中的最大值。

算法思路：
对于每个节点，我们需要维护两个值：
1. 以该节点为根的子树的节点值总和
2. 以该节点为根的子树的节点数量

然后，对于每个节点，我们可以计算其对应的平均值（总和/数量），并更新全局的最大平均值。

状态转移方程：
- 子树节点值总和 = 当前节点值 + 左子树节点值总和 + 右子树节点值总和
- 子树节点数量 = 1 + 左子树节点数量 + 右子树节点数量
- 当前子树平均值 = 子树节点值总和 / 子树节点数量

时间复杂度：O(n) - 每个节点只访问一次
空间复杂度：O(h) - h为树的高度，最坏情况下为O(n)
是否为最优解：是，这是解决子树最大平均值问题的最优方法

边界情况：
- 空树：不存在子树，理论上返回0或抛出异常，但根据题目约束通常输入不会是空树
- 单节点树：平均值就是该节点的值
- 所有节点值相同：最大平均值就是该节点值

与机器学习/深度学习的联系：
- 树结构在决策树和随机森林算法中有广泛应用
- 子树特征提取与图神经网络（GNN）中的节点聚合操作类似
- 平均值计算是最基本的统计操作，在数据分析和模型训练中常用

相关题目链接：
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code15_MaximumAverageSubtree.java
Python实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code15_MaximumAverageSubtree.py
C++实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code15_MaximumAverageSubtree.cpp
'''

# Definition for a binary tree node.
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def maximumAverageSubtree(self, root: TreeNode) -> float:
        # 边界条件处理：空树
        if not root:
            return 0.0
        
        # 初始化最大平均值
        max_avg = [0.0]
        
        # 深度优先搜索函数
        def dfs(node):
            # 递归终止条件：节点为空
            if not node:
                return 0, 0  # (总和, 节点数)
            
            # 递归计算左右子树的总和和节点数
            left_sum, left_count = dfs(node.left)
            right_sum, right_count = dfs(node.right)
            
            # 计算当前子树的总和和节点数
            current_sum = node.val + left_sum + right_sum
            current_count = 1 + left_count + right_count
            
            # 计算当前子树的平均值
            current_avg = current_sum / current_count if current_count > 0 else 0
            
            # 更新全局最大平均值
            if current_avg > max_avg[0]:
                max_avg[0] = current_avg
            
            return current_sum, current_count
        
        # 执行DFS
        dfs(root)
        
        return max_avg[0]

# 另一种实现方式：使用类变量和自定义结果类
class Solution2:
    def maximumAverageSubtree(self, root: TreeNode) -> float:
        # 边界条件处理：空树
        if not root:
            return 0.0
        
        # 初始化最大平均值
        self.max_avg = 0.0
        
        # 执行DFS
        self._dfs(root)
        
        return self.max_avg
    
    def _dfs(self, node):
        # 递归终止条件：节点为空
        if not node:
            return 0, 0  # (总和, 节点数)
        
        # 递归计算左右子树的总和和节点数
        left_sum, left_count = self._dfs(node.left)
        right_sum, right_count = self._dfs(node.right)
        
        # 计算当前子树的总和和节点数
        current_sum = node.val + left_sum + right_sum
        current_count = 1 + left_count + right_count
        
        # 计算当前子树的平均值
        current_avg = current_sum / current_count if current_count > 0 else 0
        
        # 更新全局最大平均值
        if current_avg > self.max_avg:
            self.max_avg = current_avg
        
        return current_sum, current_count

# 使用namedtuple使代码更清晰
from collections import namedtuple

class Solution3:
    def maximumAverageSubtree(self, root: TreeNode) -> float:
        # 定义一个命名元组来存储子树的总和和节点数
        SubtreeInfo = namedtuple('SubtreeInfo', ['sum', 'count'])
        
        # 边界条件处理：空树
        if not root:
            return 0.0
        
        # 初始化最大平均值
        max_avg = [0.0]
        
        # 深度优先搜索函数
        def dfs(node):
            # 递归终止条件：节点为空
            if not node:
                return SubtreeInfo(0, 0)
            
            # 递归计算左右子树的总和和节点数
            left_info = dfs(node.left)
            right_info = dfs(node.right)
            
            # 计算当前子树的总和和节点数
            current_sum = node.val + left_info.sum + right_info.sum
            current_count = 1 + left_info.count + right_info.count
            
            # 计算当前子树的平均值
            current_avg = current_sum / current_count if current_count > 0 else 0
            
            # 更新全局最大平均值
            if current_avg > max_avg[0]:
                max_avg[0] = current_avg
            
            return SubtreeInfo(current_sum, current_count)
        
        # 执行DFS
        dfs(root)
        
        return max_avg[0]

# 测试代码
if __name__ == "__main__":
    # 测试用例1: [5,6,1]
    #      5
    #     / \
    #    6   1
    root1 = TreeNode(5)
    root1.left = TreeNode(6)
    root1.right = TreeNode(1)
    
    solution = Solution()
    print("测试用例1结果:", solution.maximumAverageSubtree(root1))  # 预期输出: 6.0 (子树[6])
    
    # 测试用例2: [0,null,1]
    #    0
    #     \
    #      1
    root2 = TreeNode(0)
    root2.right = TreeNode(1)
    print("测试用例2结果:", solution.maximumAverageSubtree(root2))  # 预期输出: 1.0 (子树[1])
    
    # 测试用例3: [3,1,3,1,1,1,1]
    #      3
    #     / \
    #    1   3
    #   / \ / \
    #  1  1 1  1
    root3 = TreeNode(3)
    root3.left = TreeNode(1)
    root3.right = TreeNode(3)
    root3.left.left = TreeNode(1)
    root3.left.right = TreeNode(1)
    root3.right.left = TreeNode(1)
    root3.right.right = TreeNode(1)
    print("测试用例3结果:", solution.maximumAverageSubtree(root3))  # 预期输出: 3.0 (子树[3]或子树[3])
    
    # 测试用例4: 单节点树
    root4 = TreeNode(10)
    print("测试用例4结果:", solution.maximumAverageSubtree(root4))  # 预期输出: 10.0
    
    # 测试Solution2和Solution3
    solution2 = Solution2()
    solution3 = Solution3()
    print("Solution2 测试用例1结果:", solution2.maximumAverageSubtree(root1))  # 预期输出: 6.0
    print("Solution3 测试用例1结果:", solution3.maximumAverageSubtree(root1))  # 预期输出: 6.0

'''
工程化考量：
1. 异常处理：
   - 处理了空树的边界情况
   - 注意浮点数精度问题，使用float类型存储平均值
   - 添加了对current_count为0的检查，增强鲁棒性

2. 性能优化：
   - 使用后序遍历，一次性计算所有需要的信息
   - 避免了重复计算子树的总和和节点数

3. 代码质量：
   - 提供了三种实现方式：使用列表、使用类变量和使用namedtuple
   - 添加了详细的注释说明算法思路和边界处理
   - 包含多个测试用例验证正确性

4. 可扩展性：
   - 如果需要处理N叉树，可以扩展dfs函数以处理多个子节点
   - 如果需要记录具体哪个子树具有最大平均值，可以在更新max_avg时记录节点信息

5. 调试技巧：
   - 可以在dfs函数中添加打印语句，输出当前节点的值、子树总和、节点数和平均值
   - 对于复杂树结构，可以使用图形化工具可视化树的结构

6. Python特有优化：
   - 使用列表来存储最大平均值，因为列表在Python中是可变对象
   - 使用namedtuple使返回值更加清晰和具有可读性
'''
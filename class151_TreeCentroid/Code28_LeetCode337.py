"""
337. 打家劫舍 III
小偷又发现了一个新的可行窃的地区。这个地区只有一个入口，我们称之为 root 。
除了 root 之外，每栋房子有且只有一个"父"房子与之相连。
一番侦察之后，聪明的小偷意识到"这个地方的所有房屋的排列类似于一棵二叉树"。
如果两个直接相连的房子在同一天晚上被打劫，房屋将自动报警。
给定二叉树的根节点 root ，返回在不触动警报的情况下，小偷能够盗取的最高金额。
测试链接 : https://leetcode.cn/problems/house-robber-iii/
时间复杂度：O(n)，空间复杂度：O(n)
"""

from typing import Optional

# 树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    # 方法一：记忆化递归（树形DP）
    def rob(self, root: Optional[TreeNode]) -> int:
        memo = {}
        return self._rob_helper(root, memo)
    
    def _rob_helper(self, node: Optional[TreeNode], memo: dict) -> int:
        if node is None:
            return 0
        
        # 如果已经计算过该节点的结果，直接返回
        if node in memo:
            return memo[node]
        
        # 情况1：抢劫当前节点
        rob_current = node.val
        if node.left is not None:
            rob_current += self._rob_helper(node.left.left, memo) + self._rob_helper(node.left.right, memo)
        if node.right is not None:
            rob_current += self._rob_helper(node.right.left, memo) + self._rob_helper(node.right.right, memo)
        
        # 情况2：不抢劫当前节点
        skip_current = self._rob_helper(node.left, memo) + self._rob_helper(node.right, memo)
        
        # 取两种情况的最大值
        result = max(rob_current, skip_current)
        memo[node] = result
        
        return result
    
    # 方法二：优化的树形DP（推荐）
    def rob2(self, root: Optional[TreeNode]) -> int:
        result = self._rob_helper2(root)
        return max(result[0], result[1])
    
    def _rob_helper2(self, node: Optional[TreeNode]) -> tuple:
        """
        返回一个元组 (skip_current, rob_current)
        skip_current: 不抢劫当前节点的最大金额
        rob_current: 抢劫当前节点的最大金额
        """
        if node is None:
            return (0, 0)
        
        left = self._rob_helper2(node.left)
        right = self._rob_helper2(node.right)
        
        # 不抢劫当前节点：左右子节点可以抢劫或不抢劫，取最大值
        skip_current = max(left[0], left[1]) + max(right[0], right[1])
        
        # 抢劫当前节点：不能抢劫直接相连的子节点
        rob_current = node.val + left[0] + right[0]
        
        return (skip_current, rob_current)

# 测试函数
def test_solution():
    solution = Solution()
    
    # 测试用例1: [3,2,3,null,3,null,1]
    root1 = TreeNode(3)
    root1.left = TreeNode(2)
    root1.right = TreeNode(3)
    root1.left.right = TreeNode(3)
    root1.right.right = TreeNode(1)
    
    print(f"测试用例1结果: {solution.rob2(root1)}")  # 期望输出: 7
    
    # 测试用例2: [3,4,5,1,3,null,1]
    root2 = TreeNode(3)
    root2.left = TreeNode(4)
    root2.right = TreeNode(5)
    root2.left.left = TreeNode(1)
    root2.left.right = TreeNode(3)
    root2.right.right = TreeNode(1)
    
    print(f"测试用例2结果: {solution.rob2(root2)}")  # 期望输出: 9
    
    # 测试用例3: 空树
    print(f"测试用例3结果: {solution.rob2(None)}")  # 期望输出: 0
    
    # 测试用例4: 单个节点
    root4 = TreeNode(100)
    print(f"测试用例4结果: {solution.rob2(root4)}")  # 期望输出: 100
    
    # 测试用例5: 两个节点
    root5 = TreeNode(3)
    root5.left = TreeNode(4)
    print(f"测试用例5结果: {solution.rob2(root5)}")  # 期望输出: 4

if __name__ == "__main__":
    test_solution()

"""
算法思路与树的重心联系：
虽然本题不是直接求树的重心，但体现了树形DP的思想，这与树的重心算法有相似之处：
1. 都需要遍历整棵树
2. 都需要处理节点的状态转移
3. 都利用了树的结构特性

时间复杂度分析：
- 每个节点只被访问一次，时间复杂度为O(n)

空间复杂度分析：
- 递归栈深度为树的高度，最坏情况下为O(n)
- 方法一使用了字典存储中间结果，空间复杂度为O(n)
- 方法二只使用了常数级别的额外空间（递归栈除外）

Python特性考量：
1. 使用类型注解提高代码可读性
2. 注意Python的递归深度限制，对于大规模数据可能需要非递归实现
3. 使用元组返回多个值，避免创建额外的数据结构

工程化考量：
1. 异常处理：处理空节点情况
2. 性能优化：方法二比方法一更优，避免了字典的开销
3. 可读性：使用清晰的变量命名和详细的文档字符串
4. 可测试性：提供了多个测试用例，包括边界情况

与机器学习联系：
树形DP的思想可以应用于决策树优化、强化学习中的状态价值计算等场景。

调试技巧：
1. 使用print语句输出中间结果进行调试
2. 对于复杂树结构，可以可视化树的结构来理解算法执行过程
3. 使用小规模测试用例验证算法正确性

笔试面试要点：
1. 能够解释两种方法的区别和优劣
2. 能够分析时间复杂度和空间复杂度
3. 能够处理边界情况和异常输入
4. 能够将算法思想应用到其他树形DP问题中
"""
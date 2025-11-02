# 打家劫舍III
# 题目来源：LeetCode 337. House Robber III
# 题目链接：https://leetcode.com/problems/house-robber-iii/
# 测试链接：https://leetcode.com/problems/house-robber-iii/
# 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

'''
题目解析：
这是一个树形动态规划问题。在二叉树中，每个节点代表一个房子，节点的值代表该房子的价值。
我们需要选择一些节点，使得选中的节点互不相邻，并且这些节点的价值和最大。

算法思路：
对于每个节点，我们有两种选择：
1. 选择该节点：那么我们不能选择它的左右子节点
2. 不选择该节点：那么我们可以选择或者不选择它的左右子节点（取最大值）

使用树形DP，对于每个节点返回一个长度为2的数组：
- dp[0]: 不选择该节点时，以该节点为根的子树的最大收益
- dp[1]: 选择该节点时，以该节点为根的子树的最大收益

状态转移方程：
- 不选择当前节点：可以选择或不选择子节点，取最大值
  dp[0] = max(left[0], left[1]) + max(right[0], right[1])
- 选择当前节点：不能选择子节点
  dp[1] = root.val + left[0] + right[0]

最终结果是根节点的两种情况的最大值：max(root_dp[0], root_dp[1])

时间复杂度：O(n) - 每个节点只访问一次
空间复杂度：O(h) - h为树的高度，最坏情况下为O(n)
是否为最优解：是，这是解决打家劫舍III问题的最优方法

边界情况：
- 空树：返回0
- 单节点树：返回该节点的值
- 所有节点价值为负数：应该选择不抢劫任何节点，返回0

与机器学习/深度学习的联系：
- 树形结构在决策树算法中广泛应用
- 动态规划思想与强化学习中的值函数估计有相似之处
- 在树结构上的优化问题与图神经网络（GNN）中的节点分类问题相关

相关题目链接：
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code14_HouseRobberIII.java
Python实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code14_HouseRobberIII.py
C++实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code14_HouseRobberIII.cpp
'''

# Definition for a binary tree node.
from typing import Optional

class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def rob(self, root: Optional[TreeNode]) -> int:
        # 边界条件处理：空树
        if not root:
            return 0
        
        # 调用DP函数，获取根节点的两种状态
        not_rob, rob_root = self._dfs(root)
        
        # 返回两种情况的最大值：选择根节点或不选择根节点
        return max(not_rob, rob_root)
    
    # 深度优先搜索函数，返回两个值
    # not_rob: 不选择当前节点时的最大收益
    # rob_root: 选择当前节点时的最大收益
    def _dfs(self, node: Optional[TreeNode]) -> tuple:
        # 递归终止条件：节点为空
        if not node:
            return 0, 0
        
        # 递归计算左右子树的两种状态
        left_not_rob, left_rob = self._dfs(node.left)
        right_not_rob, right_rob = self._dfs(node.right)
        
        # 不选择当前节点：可以选择或不选择子节点，取最大值
        not_rob_current = max(left_not_rob, left_rob) + max(right_not_rob, right_rob)
        
        # 选择当前节点：不能选择子节点，只能加上不选择子节点时的最大值
        rob_current = node.val + left_not_rob + right_not_rob
        
        return not_rob_current, rob_current

# 另一种实现方式：使用记忆化搜索（针对重复子问题）
class SolutionMemo:
    def rob(self, root: Optional[TreeNode]) -> int:
        # 使用字典存储已经计算过的子树结果，避免重复计算
        memo = {}
        return self._rob_subtree(root, memo)
    
    def _rob_subtree(self, node: Optional[TreeNode], memo: dict) -> int:
        if not node:
            return 0
        
        # 检查是否已经计算过
        if node in memo:
            return memo[node]
        
        # 选择当前节点的情况
        # 不能选择子节点，所以直接选择孙子节点
        rob_val = node.val
        if node.left:
            rob_val += self._rob_subtree(node.left.left, memo) + self._rob_subtree(node.left.right, memo)
        if node.right:
            rob_val += self._rob_subtree(node.right.left, memo) + self._rob_subtree(node.right.right, memo)
        
        # 不选择当前节点的情况
        # 可以选择子节点
        not_rob_val = self._rob_subtree(node.left, memo) + self._rob_subtree(node.right, memo)
        
        # 取两种情况的最大值
        max_val = max(rob_val, not_rob_val)
        
        # 存储结果到记忆化字典
        memo[node] = max_val
        
        return max_val

# 测试代码
if __name__ == "__main__":
    # 测试用例1: [3,2,3,null,3,null,1]
    #      3
    #     / \
    #    2   3
    #     \   \
    #      3   1
    root1 = TreeNode(3)
    root1.left = TreeNode(2)
    root1.right = TreeNode(3)
    root1.left.right = TreeNode(3)
    root1.right.right = TreeNode(1)
    
    solution = Solution()
    print("测试用例1结果:", solution.rob(root1))  # 预期输出: 7 (选择3, 3, 1)
    
    # 测试用例2: [3,4,5,1,3,null,1]
    #      3
    #     / \
    #    4   5
    #   / \   \
    #  1   3   1
    root2 = TreeNode(3)
    root2.left = TreeNode(4)
    root2.right = TreeNode(5)
    root2.left.left = TreeNode(1)
    root2.left.right = TreeNode(3)
    root2.right.right = TreeNode(1)
    print("测试用例2结果:", solution.rob(root2))  # 预期输出: 9 (选择4, 5, 1)
    
    # 测试用例3: 空树
    print("测试用例3结果:", solution.rob(None))  # 预期输出: 0
    
    # 测试用例4: 单节点树
    root4 = TreeNode(1)
    print("测试用例4结果:", solution.rob(root4))  # 预期输出: 1
    
    # 测试用例5: 所有节点价值为负数
    root5 = TreeNode(-1)
    root5.left = TreeNode(-2)
    root5.right = TreeNode(-3)
    print("测试用例5结果:", solution.rob(root5))  # 预期输出: 0 (不选择任何节点)
    
    # 测试记忆化搜索实现
    solution_memo = SolutionMemo()
    print("记忆化搜索测试用例1结果:", solution_memo.rob(root1))  # 预期输出: 7

'''
工程化考量：
1. 异常处理：
   - 处理了空树和单节点树的边界情况
   - 考虑了所有节点价值为负数的情况

2. 性能优化：
   - 使用后序遍历，一次性计算所有需要的信息
   - 提供了记忆化搜索的替代实现，可以应对有重复子树的情况

3. 代码质量：
   - 提供了两种实现方式：动态规划和记忆化搜索
   - 添加了详细的注释说明算法思路和边界处理
   - 包含多个测试用例验证正确性

4. 可扩展性：
   - 如果需要处理N叉树，可以扩展_dfs函数以处理多个子节点
   - 如果需要记录具体选择了哪些节点，可以在返回结果中添加路径信息

5. 调试技巧：
   - 可以在_dfs函数中添加打印语句，输出当前节点的值和计算的两种状态
   - 对于复杂树结构，可以使用图形化工具可视化树的结构

6. Python特有优化：
   - 使用元组返回两个值，更加清晰直观
   - 记忆化搜索实现中使用对象引用作为字典键，需要注意对象生命周期
'''
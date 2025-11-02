# 二叉树中的最大路径和 - LeetCode 124
# 给定一个非空二叉树，找到路径和最大的路径
# 路径定义为从树中任意节点出发，达到任意节点的序列
# 该路径至少包含一个节点，且不一定经过根节点
# 测试链接 : https://leetcode.com/problems/binary-tree-maximum-path-sum/

'''
题目解析：
这是一道经典的树形DP问题，需要计算二叉树中的最大路径和。路径可以从任意节点开始，到任意节点结束。

算法思路：
1. 使用后序遍历（DFS）处理每个节点
2. 对于每个节点，计算以该节点为起点的最大路径和（只能向下延伸）
3. 同时计算经过该节点的最大路径和（可以包含左右子树）
4. 全局维护最大路径和

时间复杂度：O(n) - 每个节点访问一次
空间复杂度：O(h) - 递归栈深度，h为树的高度
是否为最优解：是，这是解决此类问题的最优方法

工程化考量：
1. 异常处理：处理空树、负数节点值
2. 边界条件：单节点树、所有节点为负数
3. 性能优化：避免重复计算，使用全局变量
4. Python特性：使用nonlocal或类变量维护全局状态
'''

class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def maxPathSum(self, root: TreeNode) -> int:
        """
        计算二叉树中的最大路径和
        
        Args:
            root: 二叉树根节点
            
        Returns:
            int: 最大路径和
            
        Raises:
            ValueError: 如果输入为空树
        """
        if not root:
            raise ValueError("输入树不能为空")
        
        # 使用实例变量维护全局最大路径和
        self.max_sum = float('-inf')
        
        def dfs(node):
            """
            计算以当前节点为起点的最大路径和（只能向下延伸）
            同时更新全局最大路径和（可以包含左右子树）
            
            Args:
                node: 当前节点
                
            Returns:
                int: 以当前节点为起点的最大路径和
            """
            if not node:
                return 0
            
            # 递归计算左右子树的最大路径和
            left_max = max(0, dfs(node.left))  # 如果为负，则选择0（不选择该子树）
            right_max = max(0, dfs(node.right))
            
            # 计算经过当前节点的最大路径和（可以包含左右子树）
            current_max = node.val + left_max + right_max
            self.max_sum = max(self.max_sum, current_max)
            
            # 返回以当前节点为起点的最大路径和（只能选择一条路径）
            return node.val + max(left_max, right_max)
        
        dfs(root)
        return self.max_sum

# 单元测试
def test_max_path_sum():
    solution = Solution()
    
    # 测试用例1: [1,2,3]
    root1 = TreeNode(1, TreeNode(2), TreeNode(3))
    result1 = solution.maxPathSum(root1)
    print(f"测试1: {result1}")  # 期望: 6
    assert result1 == 6, f"测试1失败，期望6，得到{result1}"
    
    # 测试用例2: [-10,9,20,null,null,15,7]
    root2 = TreeNode(-10, 
                    TreeNode(9), 
                    TreeNode(20, TreeNode(15), TreeNode(7)))
    result2 = solution.maxPathSum(root2)
    print(f"测试2: {result2}")  # 期望: 42
    assert result2 == 42, f"测试2失败，期望42，得到{result2}"
    
    # 测试用例3: 单节点
    root3 = TreeNode(-3)
    result3 = solution.maxPathSum(root3)
    print(f"测试3: {result3}")  # 期望: -3
    assert result3 == -3, f"测试3失败，期望-3，得到{result3}"
    
    # 测试用例4: 所有节点为负数
    root4 = TreeNode(-2, TreeNode(-1), None)
    result4 = solution.maxPathSum(root4)
    print(f"测试4: {result4}")  # 期望: -1
    assert result4 == -1, f"测试4失败，期望-1，得到{result4}"
    
    # 测试用例5: 空树（异常情况）
    try:
        solution.maxPathSum(None)
        assert False, "应该抛出ValueError异常"
    except ValueError:
        print("测试5: 空树异常处理正确")
    
    print("所有测试用例通过！")

if __name__ == "__main__":
    test_max_path_sum()
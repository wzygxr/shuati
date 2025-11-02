"""
543. 二叉树的直径
给定一棵二叉树，你需要计算它的直径长度。
一棵二叉树的直径长度是任意两个结点路径长度中的最大值。
这条路径可能穿过也可能不穿过根结点。
测试链接 : https://leetcode.cn/problems/diameter-of-binary-tree/
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
    def diameterOfBinaryTree(self, root: Optional[TreeNode]) -> int:
        """
        计算二叉树的直径
        """
        if root is None:
            return 0
        
        self.max_diameter = 0
        self._depth(root)
        return self.max_diameter
    
    def _depth(self, node: Optional[TreeNode]) -> int:
        """
        计算树的深度，同时更新直径
        """
        if node is None:
            return 0
        
        left_depth = self._depth(node.left)
        right_depth = self._depth(node.right)
        
        # 更新直径：左子树深度 + 右子树深度
        self.max_diameter = max(self.max_diameter, left_depth + right_depth)
        
        # 返回当前节点的深度
        return max(left_depth, right_depth) + 1

class Solution2:
    """
    方法二：不使用成员变量，通过参数传递
    """
    def diameterOfBinaryTree(self, root: Optional[TreeNode]) -> int:
        if root is None:
            return 0
        
        max_diameter = [0]  # 使用列表传递引用
        self._get_depth(root, max_diameter)
        return max_diameter[0]
    
    def _get_depth(self, node: Optional[TreeNode], max_diameter: list) -> int:
        if node is None:
            return 0
        
        left_depth = self._get_depth(node.left, max_diameter)
        right_depth = self._get_depth(node.right, max_diameter)
        
        # 更新最大直径
        max_diameter[0] = max(max_diameter[0], left_depth + right_depth)
        
        # 返回当前节点的深度
        return max(left_depth, right_depth) + 1

class Solution3:
    """
    方法三：使用自定义类返回多个值
    """
    def diameterOfBinaryTree(self, root: Optional[TreeNode]) -> int:
        if root is None:
            return 0
        
        info = self._calculate_diameter(root)
        return info['diameter']
    
    def _calculate_diameter(self, node: Optional[TreeNode]) -> dict:
        """
        返回包含深度和直径信息的字典
        """
        if node is None:
            return {'depth': 0, 'diameter': 0}
        
        left_info = self._calculate_diameter(node.left)
        right_info = self._calculate_diameter(node.right)
        
        # 当前节点的深度
        current_depth = max(left_info['depth'], right_info['depth']) + 1
        
        # 当前节点的直径：取左子树直径、右子树直径、经过当前节点的直径的最大值
        current_diameter = max(
            max(left_info['diameter'], right_info['diameter']),
            left_info['depth'] + right_info['depth']
        )
        
        return {'depth': current_depth, 'diameter': current_diameter}

# 测试函数
def test_solution():
    # 创建测试用例
    
    # 测试用例1: [1,2,3,4,5]
    root1 = TreeNode(1)
    root1.left = TreeNode(2)
    root1.right = TreeNode(3)
    root1.left.left = TreeNode(4)
    root1.left.right = TreeNode(5)
    
    solution = Solution()
    result1 = solution.diameterOfBinaryTree(root1)
    print(f"测试用例1结果: {result1}")  # 期望输出: 3
    
    # 测试用例2: [1,2]
    root2 = TreeNode(1)
    root2.left = TreeNode(2)
    
    result2 = solution.diameterOfBinaryTree(root2)
    print(f"测试用例2结果: {result2}")  # 期望输出: 1
    
    # 测试用例3: 单个节点
    root3 = TreeNode(1)
    result3 = solution.diameterOfBinaryTree(root3)
    print(f"测试用例3结果: {result3}")  # 期望输出: 0
    
    # 测试用例4: 空树
    result4 = solution.diameterOfBinaryTree(None)
    print(f"测试用例4结果: {result4}")  # 期望输出: 0
    
    # 测试用例5: 复杂结构
    root5 = TreeNode(1)
    root5.left = TreeNode(2)
    root5.right = TreeNode(3)
    root5.left.left = TreeNode(4)
    root5.left.right = TreeNode(5)
    root5.right.right = TreeNode(6)
    root5.left.left.left = TreeNode(7)
    root5.left.left.right = TreeNode(8)
    
    result5 = solution.diameterOfBinaryTree(root5)
    print(f"测试用例5结果: {result5}")  # 期望输出: 5
    
    # 测试不同解法的一致性
    solution2 = Solution2()
    solution3 = Solution3()
    
    print("\\n解法一致性测试:")
    print(f"解法1: {solution.diameterOfBinaryTree(root1)}")
    print(f"解法2: {solution2.diameterOfBinaryTree(root1)}")
    print(f"解法3: {solution3.diameterOfBinaryTree(root1)}")

if __name__ == "__main__":
    test_solution()

"""
算法思路与树的重心联系：
本题与树的重心密切相关，因为：
1. 树的直径的两个端点通常与重心有特定关系
2. 计算直径的方法可以用于寻找重心
3. 树形遍历的思想在两者中都得到应用

时间复杂度分析：
- 每个节点只被访问一次，时间复杂度为O(n)

空间复杂度分析：
- 递归栈深度为树的高度，最坏情况下为O(n)
- 使用了常数级别的额外空间

Python特性考量：
1. 使用类型注解提高代码可读性
2. 注意Python的递归深度限制，对于大规模数据可能需要非递归实现
3. 使用字典或类来返回多个值
4. 使用列表传递引用来避免成员变量

工程化考量：
1. 异常处理：处理空树和单节点情况
2. 性能优化：避免重复计算，使用一次DFS遍历
3. 可读性：提供多种实现方式便于理解
4. 可测试性：提供详细的测试用例

关键设计细节：
1. 直径定义为边数，不是节点数
2. 直径可能不经过根节点
3. 需要同时计算深度和直径
4. 使用后序遍历（左右根）的顺序

调试技巧：
1. 使用小规模树结构验证算法正确性
2. 打印每个节点的深度和直径进行调试
3. 特别注意叶子节点的处理
4. 使用可视化工具展示树结构

面试要点：
1. 能够解释直径的定义（边数而非节点数）
2. 能够处理直径不经过根节点的情况
3. 能够分析算法的时间复杂度和空间复杂度
4. 能够将算法思想应用到其他树形问题中

反直觉但关键的设计：
1. 直径不一定经过根节点
2. 单个节点的直径是0而不是1
3. 深度计算和直径更新需要同时进行
4. 使用后序遍历确保子节点信息先被计算

与网络拓扑联系：
本题可以应用于网络拓扑分析：
1. 网络延迟分析：直径代表最大延迟
2. 通信路径优化：寻找最优通信路径
3. 分布式系统：节点间通信距离计算

性能优化：
1. 使用一次DFS遍历完成所有计算
2. 避免重复计算子树信息
3. 使用引用传递减少对象创建
4. 对于大规模数据，考虑使用迭代DFS

Python特定优化：
1. 使用lru_cache进行记忆化（如果需要）
2. 使用生成器表达式减少内存使用
3. 对于深度递归，考虑使用迭代DFS

常见错误：
1. 将直径误认为是节点数而不是边数
2. 忘记处理空树情况
3. 没有考虑直径不经过根节点的情况
4. 递归终止条件错误
"""
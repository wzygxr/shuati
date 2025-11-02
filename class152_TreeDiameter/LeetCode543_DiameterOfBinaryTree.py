# LeetCode 543. 二叉树的直径
# 题目：给定一棵二叉树，你需要计算它的直径长度。
# 一棵二叉树的直径长度是任意两个结点路径长度中的最大值。
# 这条路径可能穿过也可能不穿过根结点。
# 两结点之间的路径长度是以它们之间边的数目表示。

# 算法标签：树、深度优先搜索、递归
# 难度：简单
# 时间复杂度：O(n)，其中n是二叉树的节点数，每个节点只访问一次
# 空间复杂度：O(h)，其中h是二叉树的高度，递归调用栈的深度

# 相关题目：
# - LeetCode 1522. Diameter of N-Ary Tree (N叉树的直径)
# - LeetCode 1245. Tree Diameter (无向树的直径)
# - LeetCode 1617. Count Subtrees With Max Distance Between Cities (统计子树中城市之间最大距离)
# - SPOJ PT07Z - Longest path in a tree (树中最长路径)
# - CSES 1131 - Tree Diameter (树的直径)
# - 51Nod 2602 - 树的直径
# - 洛谷 U81904 树的直径
# - AtCoder ABC221F - Diameter Set

# 解题思路：
# 1. 二叉树的直径不一定经过根节点
# 2. 对于每个节点，经过该节点的最长路径等于左子树的最大深度加上右子树的最大深度
# 3. 使用递归方法，在计算每个节点子树深度的同时更新全局最大直径
# 4. 递归函数返回以当前节点为根的子树的最大深度

# 二叉树节点定义
class TreeNode:
    """
    二叉树节点类
    
    属性:
        val (int): 节点值
        left (TreeNode): 左子节点
        right (TreeNode): 右子节点
    """
    def __init__(self, val=0, left=None, right=None):
        """
        初始化二叉树节点
        
        参数:
            val (int): 节点值，默认为0
            left (TreeNode): 左子节点，默认为None
            right (TreeNode): 右子节点，默认为None
        """
        self.val = val
        self.left = left
        self.right = right

class Solution:
    """
    二叉树直径求解器
    
    方法:
        diameterOfBinaryTree: 计算二叉树的直径
        depth: 计算子树深度并更新最大直径
    """
    
    def __init__(self):
        """
        初始化Solution类
        """
        # 全局变量，用于记录最大直径
        self.max_diameter = 0
    
    def diameterOfBinaryTree(self, root: TreeNode) -> int:
        """
        计算二叉树的直径
        
        算法思路：
        1. 对于每个节点，计算其左子树和右子树的最大深度
        2. 经过该节点的最长路径就是左子树最大深度+右子树最大深度
        3. 遍历所有节点，取最大值
        
        参数:
            root (TreeNode): 二叉树根节点
            
        返回:
            int: 树的直径（边数）
        
        时间复杂度：O(n)，其中n是二叉树的节点数，每个节点只访问一次
        空间复杂度：O(h)，其中h是二叉树的高度，递归调用栈的深度
        """
        self.max_diameter = 0  # 重置全局变量
        self.depth(root)       # 计算每个节点的深度并更新最大直径
        return self.max_diameter
    
    def depth(self, node: TreeNode | None) -> int:
        """
        计算以当前节点为根的子树深度，并更新最大直径
        
        算法思路：
        1. 递归计算左子树和右子树的最大深度
        2. 经过当前节点的最长路径 = 左子树最大深度 + 右子树最大深度
        3. 更新全局最大直径
        4. 返回以当前节点为根的子树的最大深度
        
        参数:
            node (TreeNode): 当前节点
            
        返回:
            int: 当前节点为根的子树的最大深度
        """
        # 基本情况：空节点深度为0
        if not node:
            return 0
        
        # 递归计算左子树和右子树的最大深度
        left_depth = self.depth(node.left)
        right_depth = self.depth(node.right)
        
        # 经过当前节点的最长路径 = 左子树最大深度 + 右子树最大深度
        # 更新全局最大直径
        self.max_diameter = max(self.max_diameter, left_depth + right_depth)
        
        # 返回以当前节点为根的子树的最大深度
        return max(left_depth, right_depth) + 1

# 测试方法
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1: [1,2,3,4,5]
    #     1
    #    / \
    #   2   3
    #  / \
    # 4   5
    # 预期输出: 3 (路径 [4,2,1,3] 或 [5,2,1,3])
    root1 = TreeNode(1)
    root1.left = TreeNode(2)
    root1.right = TreeNode(3)
    root1.left.left = TreeNode(4)
    root1.left.right = TreeNode(5)
    
    print("测试用例1结果:", solution.diameterOfBinaryTree(root1))  # 应该输出3
    
    # 测试用例2: [1,2]
    #   1
    #  /
    # 2
    # 预期输出: 1 (路径 [1,2])
    root2 = TreeNode(1)
    root2.left = TreeNode(2)
    
    print("测试用例2结果:", solution.diameterOfBinaryTree(root2))  # 应该输出1
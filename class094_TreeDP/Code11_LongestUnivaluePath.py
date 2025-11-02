# 687. 最长同值路径
# 测试链接 : https://leetcode.cn/problems/longest-univalue-path/

# Definition for a binary tree node.
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def __init__(self):
        # 全局变量，记录最长同值路径的长度
        self.max_length = 0
    
    # 提交如下的方法
    # 时间复杂度: O(n) n为树中节点的数量，需要遍历所有节点
    # 空间复杂度: O(h) h为树的高度，递归调用栈的深度
    # 是否为最优解: 是，这是解决最长同值路径问题的标准方法
    def longestUnivaluePath(self, root: TreeNode) -> int:
        self.max_length = 0
        self.dfs(root)
        return self.max_length
    
    # 递归函数返回从当前节点向下延伸的最长同值路径长度
    def dfs(self, node: TreeNode) -> int:
        # 基础情况：如果节点为空，返回0
        if not node:
            return 0
        
        # 递归计算左右子树的结果
        left_path = self.dfs(node.left) if node.left else 0
        right_path = self.dfs(node.right) if node.right else 0
        
        # 计算经过当前节点的最长同值路径长度
        left_length = 0
        right_length = 0
        
        # 如果左子节点存在且值与当前节点相同，则可以延伸左路径
        if node.left and node.left.val == node.val:
            left_length = left_path + 1
        
        # 如果右子节点存在且值与当前节点相同，则可以延伸右路径
        if node.right and node.right.val == node.val:
            right_length = right_path + 1
        
        # 更新全局最长路径：经过当前节点的路径长度为左路径长度+右路径长度
        self.max_length = max(self.max_length, left_length + right_length)
        
        # 返回从当前节点向下延伸的最长同值路径长度（只能选择左右路径中的一条）
        return max(left_length, right_length)
    
    # 补充题目1: 337. 打家劫舍 III
    # 题目链接: https://leetcode.cn/problems/house-robber-iii/
    # 题目描述: 在上次打劫完一条街道之后和一圈房屋后，小偷又发现了一个新的可行窃的地区。
    # 这个地区只有一个入口，我们称之为"根"。 除了"根"之外，每栋房子有且只有一个"父"房子与之相连。
    # 一番侦察之后，聪明的小偷意识到"这个地方的所有房屋的排列类似于一棵二叉树"。
    # 如果两个直接相连的房子在同一天晚上被打劫，房屋将自动报警。
    # 计算在不触动警报的情况下，小偷一晚能够盗取的最高金额。
    # 时间复杂度: O(n) n为树中节点的数量，需要遍历所有节点
    # 空间复杂度: O(h) h为树的高度，递归调用栈的深度
    # 是否为最优解: 是，这是解决树状结构打家劫舍问题的标准树形DP方法
    def rob(self, root: TreeNode) -> int:
        return max(self._rob_helper(root))
    
    # 辅助函数返回一个元组：
    # 返回值[0]: 不偷当前节点能获得的最大金额
    # 返回值[1]: 偷当前节点能获得的最大金额
    def _rob_helper(self, node: TreeNode) -> tuple:
        if not node:
            return (0, 0)
        
        # 递归计算左右子树
        left = self._rob_helper(node.left)
        right = self._rob_helper(node.right)
        
        # 不偷当前节点，左右子树可以偷或不偷，取最大值之和
        not_rob_current = max(left[0], left[1]) + max(right[0], right[1])
        
        # 偷当前节点，则左右子树都不能偷
        rob_current = node.val + left[0] + right[0]
        
        return (not_rob_current, rob_current)
    
    # 补充题目2: 979. 在二叉树中分配硬币
    # 题目链接: https://leetcode.cn/problems/distribute-coins-in-binary-tree/
    # 题目描述: 给定一个有 N 个结点的二叉树的根结点 root，树中的每个结点上都对应有 node.val 枚硬币，
    # 并且总共有 N 枚硬币。在一次移动中，我们可以选择两个相邻的结点，然后将一枚硬币从其中一个结点移动到另一个结点。
    # (移动可以是从父结点到子结点，或者从子结点移动到父结点。)
    # 返回使每个结点上只有一枚硬币所需的移动次数。
    # 时间复杂度: O(n) n为树中节点的数量，需要遍历所有节点
    # 空间复杂度: O(h) h为树的高度，递归调用栈的深度
    # 是否为最优解: 是，这是解决二叉树硬币分配问题的高效方法
    def distributeCoins(self, root: TreeNode) -> int:
        self.moves = 0  # 重置移动次数
        self._distribute_helper(root)
        return self.moves
    
    # 计算当前节点需要移动的硬币数量
    # 返回值表示当前节点需要传递给父节点的硬币数量（可能为负，表示需要从父节点获取）
    def _distribute_helper(self, node: TreeNode) -> int:
        if not node:
            return 0
        
        # 递归计算左右子树的硬币情况
        left_coins = self._distribute_helper(node.left)
        right_coins = self._distribute_helper(node.right)
        
        # 左右子树传递硬币的过程会产生移动次数
        # 取绝对值是因为不管是移入还是移出，都需要一次移动
        self.moves += abs(left_coins) + abs(right_coins)
        
        # 返回当前节点需要传递给父节点的硬币数量
        # 当前节点的硬币数减去1（自己需要保留的一枚）加上左右子树传递来的硬币
        return node.val - 1 + left_coins + right_coins
    
    # 补充题目3: 549. 二叉树中最长的连续序列
    # 题目链接: https://leetcode.cn/problems/binary-tree-longest-consecutive-sequence-ii/
    # 题目描述: 给定一个二叉树，你需要找出二叉树中最长的连续序列路径的长度。
    # 请注意，该路径可以是递增或递减的。例如，[1,2,3,4] 和 [4,3,2,1] 都被认为是有效的，但路径 [1,3,2,4] 不是有效的。
    # 另外，路径可以是子树中的任意节点开始，任意节点结束。
    # 时间复杂度: O(n) n为树中节点的数量，需要遍历所有节点
    # 空间复杂度: O(h) h为树的高度，递归调用栈的深度
    # 是否为最优解: 是，这是解决二叉树最长连续序列问题的高效方法
    def longestConsecutive(self, root: TreeNode) -> int:
        self.max_consecutive = 0
        self._consecutive_helper(root)
        return self.max_consecutive
    
    # 返回一个元组：(递增序列长度, 递减序列长度)
    def _consecutive_helper(self, node: TreeNode) -> tuple:
        if not node:
            return (0, 0)
        
        inc, dec = 1, 1
        
        if node.left:
            left_inc, left_dec = self._consecutive_helper(node.left)
            if node.val == node.left.val + 1:  # 当前节点比左子节点大1，递减
                dec = left_dec + 1
            elif node.val == node.left.val - 1:  # 当前节点比左子节点小1，递增
                inc = left_inc + 1
        
        if node.right:
            right_inc, right_dec = self._consecutive_helper(node.right)
            if node.val == node.right.val + 1:  # 当前节点比右子节点大1，递减
                dec = max(dec, right_dec + 1)
            elif node.val == node.right.val - 1:  # 当前节点比右子节点小1，递增
                inc = max(inc, right_inc + 1)
        
        # 更新最大长度，包括当前节点作为连接点的情况
        self.max_consecutive = max(self.max_consecutive, inc + dec - 1)
        
        return (inc, dec)
    
    # 补充题目4: 1372. 二叉树中的最长交错路径
    # 题目链接: https://leetcode.cn/problems/longest-zigzag-path-in-a-binary-tree/
    # 题目描述: 给你一棵以 root 为根的二叉树，二叉树中的交错路径定义如下：
    # 选择二叉树中 任意 节点和一个方向（左或者右）。
    # 如果前进方向为右，那么移动到当前节点的的右子节点，然后前进方向变为左；反之亦然。
    # 不断重复这一过程，直到你在树中无法继续移动。
    # 交错路径的长度定义为：访问过的节点数目 - 1（单个节点的路径长度为 0 ）。
    # 返回给定树中最长交错路径的长度。
    # 时间复杂度: O(n) n为树中节点的数量，需要遍历所有节点
    # 空间复杂度: O(h) h为树的高度，递归调用栈的深度
    # 是否为最优解: 是，这是解决二叉树最长交错路径问题的高效方法
    def longestZigZag(self, root: TreeNode) -> int:
        self.max_zigzag = 0
        self._zigzag_helper(root, 0, 0)  # 0表示从父节点的左子节点来，1表示从父节点的右子节点来，0表示初始状态
        return self.max_zigzag
    
    # direction: 0表示从父节点的左子节点来，1表示从父节点的右子节点来
    # length: 当前路径的长度
    def _zigzag_helper(self, node: TreeNode, direction: int, length: int) -> None:
        if not node:
            return
        
        self.max_zigzag = max(self.max_zigzag, length)
        
        if direction == 0:  # 从父节点的左子节点来，接下来可以向左或向右
            # 向左：重置路径长度为0，因为方向相同
            self._zigzag_helper(node.left, 0, 1)
            # 向右：路径长度+1，方向变为1
            self._zigzag_helper(node.right, 1, length + 1)
        else:  # 从父节点的右子节点来，接下来可以向左或向右
            # 向左：路径长度+1，方向变为0
            self._zigzag_helper(node.left, 0, length + 1)
            # 向右：重置路径长度为0，因为方向相同
            self._zigzag_helper(node.right, 1, 1)
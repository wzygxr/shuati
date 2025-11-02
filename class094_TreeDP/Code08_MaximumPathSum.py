# 124. 二叉树中的最大路径和
# 测试链接 : https://leetcode.cn/problems/binary-tree-maximum-path-sum/

# Definition for a binary tree node.
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    # 提交如下的方法
    # 时间复杂度: O(n) n为树中节点的数量，需要遍历所有节点
    # 空间复杂度: O(h) h为树的高度，递归调用栈的深度
    # 是否为最优解: 是，这是计算二叉树最大路径和的标准方法
    def maxPathSum(self, root: TreeNode) -> int:
        self.max_sum: int = float('-inf')  # type: ignore
        self.max_gain(root)
        return self.max_sum

    # 计算以node为根的子树能向父节点提供的最大路径和
    def max_gain(self, node: TreeNode) -> int:
        # 基础情况：空节点贡献0
        if not node:
            return 0

        # 递归计算左右子树能提供的最大路径和
        # 只有当贡献值大于0时才选择
        left_gain = max(self.max_gain(node.left) if node.left else 0, 0)
        right_gain = max(self.max_gain(node.right) if node.right else 0, 0)

        # 计算以当前节点为最高节点的路径的最大路径和
        current_max = node.val + left_gain + right_gain

        # 更新全局最大值
        self.max_sum = max(self.max_sum, current_max)

        # 返回当前节点能向父节点提供的最大路径和
        return node.val + max(left_gain, right_gain)

# 补充题目1: 437. 路径总和 III
# 题目链接: https://leetcode.cn/problems/path-sum-iii/
# 题目描述: 给定一个二叉树的根节点 root 和一个整数 targetSum ，求该二叉树里节点值之和等于 targetSum 的 路径 的数目。
# 路径 不需要从根节点开始，也不需要在叶子节点结束，但是路径方向必须是向下的（只能从父节点到子节点）。
# 时间复杂度: O(n^2) 最坏情况下，对于每个节点都需要遍历其路径
# 空间复杂度: O(h) h为树的高度，递归调用栈的深度
class PathSumIIISolution:
    def pathSumIII(self, root: TreeNode, targetSum: int) -> int:
        # 使用前缀和 + 哈希表的优化方法
        prefix_sum = {0: 1}  # 前缀和为0的路径有1条（空路径）
        result = [0]  # 使用列表作为可变对象存储结果
        self.dfs_path_sum(root, 0, targetSum, prefix_sum, result)
        return result[0]

    def dfs_path_sum(self, node: TreeNode, current_sum: int, target: int, prefix_sum: dict, result: list):
        if not node:
            return

        # 更新当前路径和
        current_sum += node.val
        # 计算有多少条路径以当前节点结束，路径和为target
        result[0] += prefix_sum.get(current_sum - target, 0)
        # 将当前路径和加入前缀和哈希表
        prefix_sum[current_sum] = prefix_sum.get(current_sum, 0) + 1

        # 递归处理左右子树
        self.dfs_path_sum(node.left, current_sum, target, prefix_sum, result)
        self.dfs_path_sum(node.right, current_sum, target, prefix_sum, result)

        # 回溯，移除当前路径和
        prefix_sum[current_sum] -= 1
        if prefix_sum[current_sum] == 0:
            del prefix_sum[current_sum]

# 补充题目2: 112. 路径总和
# 题目链接: https://leetcode.cn/problems/path-sum/
# 题目描述: 给你二叉树的根节点 root 和一个表示目标和的整数 targetSum 。
# 判断该树中是否存在 根节点到叶子节点 的路径，这条路径上所有节点值相加等于目标和 targetSum 。
# 时间复杂度: O(n) n为树中节点的数量，需要遍历所有节点
# 空间复杂度: O(h) h为树的高度，递归调用栈的深度
class PathSumSolution:
    def hasPathSum(self, root: TreeNode, targetSum: int) -> bool:
        if not root:
            return False

        # 如果是叶子节点，直接判断当前节点值是否等于目标和
        if not root.left and not root.right:
            return root.val == targetSum

        # 递归检查左右子树
        return self.hasPathSum(root.left, targetSum - root.val) or self.hasPathSum(root.right, targetSum - root.val)

# 补充题目3: 113. 路径总和 II
# 题目链接: https://leetcode.cn/problems/path-sum-ii/
# 题目描述: 给你二叉树的根节点 root 和一个整数目标和 targetSum ，
# 找出所有 从根节点到叶子节点 路径总和等于给定目标和的路径。
# 时间复杂度: O(n^2) 最坏情况下，需要存储O(n)条路径，每条路径有O(n)个节点
# 空间复杂度: O(h) h为树的高度，递归调用栈的深度，加上存储路径的O(n)空间
class PathSumIISolution:
    def pathSumII(self, root: TreeNode, targetSum: int) -> list[list[int]]:
        result = []
        current_path = []
        self.dfs_path_sum_ii(root, targetSum, current_path, result)
        return result

    def dfs_path_sum_ii(self, node: TreeNode, remaining_sum: int, current_path: list, result: list):
        if not node:
            return

        # 将当前节点加入路径
        current_path.append(node.val)

        # 如果是叶子节点且路径和等于目标值，将路径加入结果
        if not node.left and not node.right and remaining_sum == node.val:
            result.append(current_path.copy())

        # 递归处理左右子树
        self.dfs_path_sum_ii(node.left, remaining_sum - node.val, current_path, result)
        self.dfs_path_sum_ii(node.right, remaining_sum - node.val, current_path, result)

        # 回溯，移除当前节点
        current_path.pop()

# 补充题目4: 257. 二叉树的所有路径
# 题目链接: https://leetcode.cn/problems/binary-tree-paths/
# 题目描述: 给你一个二叉树的根节点 root ，按 任意顺序 ，返回所有从根节点到叶子节点的路径。
# 时间复杂度: O(n^2) 最坏情况下，需要存储O(n)条路径，每条路径有O(n)个节点
# 空间复杂度: O(h) h为树的高度，递归调用栈的深度，加上存储路径的O(n)空间
class BinaryTreePathsSolution:
    def binaryTreePaths(self, root: TreeNode) -> list[str]:
        result = []
        if root:
            self.build_paths(root, "", result)
        return result

    def build_paths(self, node: TreeNode, current_path: str, result: list):
        # 将当前节点加入路径
        if not current_path:
            current_path = str(node.val)
        else:
            current_path += "->" + str(node.val)

        # 如果是叶子节点，将路径加入结果
        if not node.left and not node.right:
            result.append(current_path)
            return

        # 递归处理左右子树
        if node.left:
            self.build_paths(node.left, current_path, result)
        if node.right:
            self.build_paths(node.right, current_path, result)
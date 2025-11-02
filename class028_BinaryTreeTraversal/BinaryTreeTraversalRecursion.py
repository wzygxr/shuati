# 二叉树递归遍历及相关题目详解 - Python版本
# 本文件包含二叉树的三种基本遍历方式（前序、中序、后序）的递归实现
# 并扩展了多个相关LeetCode题目，每道题目都包含详细注释、复杂度分析

from typing import List, Optional
from collections import defaultdict

# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


# 递归基本样子，用来理解递归序
# 递归序是指在递归过程中，每个节点都会被访问三次：
# 1. 刚进入节点时
# 2. 从左子树返回时
# 3. 从右子树返回时
def recursion_pattern(head: Optional[TreeNode]) -> None:
    if head is None:
        return
    # 位置1：刚进入节点时（前序遍历位置）
    recursion_pattern(head.left)
    # 位置2：从左子树返回时（中序遍历位置）
    recursion_pattern(head.right)
    # 位置3：从右子树返回时（后序遍历位置）


# 先序打印所有节点，递归版
# 先序遍历顺序：根节点 -> 左子树 -> 右子树
# 时间复杂度：O(n)，其中n是二叉树的节点数，每个节点恰好被访问一次
# 空间复杂度：O(h)，其中h是二叉树的高度，递归调用栈的深度等于树的高度
def pre_order(head: Optional[TreeNode]) -> None:
    if head is None:
        return
    # 先访问根节点
    print(head.val, end=" ")
    # 再递归访问左子树
    pre_order(head.left)
    # 最后递归访问右子树
    pre_order(head.right)


# 中序打印所有节点，递归版
# 中序遍历顺序：左子树 -> 根节点 -> 右子树
# 时间复杂度：O(n)，其中n是二叉树的节点数，每个节点恰好被访问一次
# 空间复杂度：O(h)，其中h是二叉树的高度，递归调用栈的深度等于树的高度
def in_order(head: Optional[TreeNode]) -> None:
    if head is None:
        return
    # 先递归访问左子树
    in_order(head.left)
    # 再访问根节点
    print(head.val, end=" ")
    # 最后递归访问右子树
    in_order(head.right)


# 后序打印所有节点，递归版
# 后序遍历顺序：左子树 -> 右子树 -> 根节点
# 时间复杂度：O(n)，其中n是二叉树的节点数，每个节点恰好被访问一次
# 空间复杂度：O(h)，其中h是二叉树的高度，递归调用栈的深度等于树的高度
def pos_order(head: Optional[TreeNode]) -> None:
    if head is None:
        return
    # 先递归访问左子树
    pos_order(head.left)
    # 再递归访问右子树
    pos_order(head.right)
    # 最后访问根节点
    print(head.val, end=" ")


# LeetCode 104. 二叉树的最大深度
# 题目链接：https://leetcode.cn/problems/maximum-depth-of-binary-tree/
# 题目描述：给定一个二叉树，找出其最大深度。二叉树的深度为根节点到最远叶子节点的最长路径上的节点数。
# 解法：使用递归，树的最大深度等于左右子树最大深度的最大值加1
# 时间复杂度：O(n)，其中n是二叉树的节点数
# 空间复杂度：O(h)，其中h是二叉树的高度，递归调用栈的深度
def max_depth(root: Optional[TreeNode]) -> int:
    # 基础情况：空节点的深度为0
    if root is None:
        return 0
    # 递归计算左右子树的最大深度
    left_depth = max_depth(root.left)
    right_depth = max_depth(root.right)
    # 返回左右子树最大深度的最大值加1
    return max(left_depth, right_depth) + 1


# LeetCode 110. 平衡二叉树
# 题目链接：https://leetcode.cn/problems/balanced-binary-tree/
# 题目描述：给定一个二叉树，判断它是否是高度平衡的二叉树。
# 解法：使用递归，自底向上检查每个节点的左右子树高度差是否不超过1
# 时间复杂度：O(n)，其中n是二叉树的节点数
# 空间复杂度：O(h)，其中h是二叉树的高度，递归调用栈的深度

def get_height(node: Optional[TreeNode]) -> int:
    """辅助函数：获取树的高度，如果不平衡则返回-1"""
    # 基础情况：空节点的高度为0
    if node is None:
        return 0
    # 递归获取左子树高度
    left_height = get_height(node.left)
    # 如果左子树不平衡，直接返回-1
    if left_height == -1:
        return -1
    # 递归获取右子树高度
    right_height = get_height(node.right)
    # 如果右子树不平衡，直接返回-1
    if right_height == -1:
        return -1
    # 检查当前节点是否平衡（左右子树高度差不超过1）
    if abs(left_height - right_height) > 1:
        return -1
    # 返回当前节点的高度（左右子树最大高度加1）
    return max(left_height, right_height) + 1


def is_balanced(root: Optional[TreeNode]) -> bool:
    return get_height(root) != -1


# LeetCode 100. 相同的树
# 题目链接：https://leetcode.cn/problems/same-tree/
# 题目描述：给你两棵二叉树的根节点 p 和 q ，编写一个函数来检验两棵树是否相同。
# 解法：使用递归同时遍历两棵树，比较对应节点的值是否相等
# 时间复杂度：O(min(m,n))，其中m和n分别是两个二叉树的节点数
# 空间复杂度：O(min(h1,h2))，其中h1和h2分别是两个二叉树的高度
def is_same_tree(p: Optional[TreeNode], q: Optional[TreeNode]) -> bool:
    # 基础情况：两个节点都为空，则相同
    if p is None and q is None:
        return True
    # 基础情况：一个节点为空，另一个不为空，则不相同
    if p is None or q is None:
        return False
    # 比较当前节点值，并递归比较左右子树
    return (p.val == q.val and 
            is_same_tree(p.left, q.left) and 
            is_same_tree(p.right, q.right))


# LeetCode 101. 对称二叉树
# 题目链接：https://leetcode.cn/problems/symmetric-tree/
# 题目描述：给你一个二叉树的根节点 root ，检查它是否轴对称。
# 解法：使用递归比较左子树和右子树是否镜像对称
# 时间复杂度：O(n)，其中n是二叉树的节点数
# 空间复杂度：O(h)，其中h是二叉树的高度

def is_mirror(left: Optional[TreeNode], right: Optional[TreeNode]) -> bool:
    """辅助函数：判断两个树是否镜像对称"""
    # 基础情况：两个节点都为空，则对称
    if left is None and right is None:
        return True
    # 基础情况：一个节点为空，另一个不为空，则不对称
    if left is None or right is None:
        return False
    # 比较当前节点值，并递归比较外侧和内侧
    return (left.val == right.val and 
            is_mirror(left.left, right.right) and 
            is_mirror(left.right, right.left))


def is_symmetric(root: Optional[TreeNode]) -> bool:
    # 空树是对称的
    if root is None:
        return True
    # 比较左右子树是否镜像对称
    return is_mirror(root.left, root.right)


# LeetCode 226. 翻转二叉树
# 题目链接：https://leetcode.cn/problems/invert-binary-tree/
# 题目描述：给你一棵二叉树的根节点 root ，翻转这棵二叉树，并返回其根节点。
# 解法：使用递归，交换每个节点的左右子树
# 时间复杂度：O(n)，其中n是二叉树的节点数
# 空间复杂度：O(h)，其中h是二叉树的高度
def invert_tree(root: Optional[TreeNode]) -> Optional[TreeNode]:
    # 基础情况：空节点无需翻转
    if root is None:
        return None
    # 交换左右子树
    root.left, root.right = root.right, root.left
    # 递归翻转左右子树
    invert_tree(root.left)
    invert_tree(root.right)
    return root


# =========================== 扩展题目部分 ===========================

# LeetCode 112. 路径总和
# 题目来源：LeetCode
# 题目链接：https://leetcode.cn/problems/path-sum/
# 题目描述：给你二叉树的根节点 root 和一个表示目标和的整数 targetSum。
# 判断该树中是否存在 根节点到叶子节点 的路径，这条路径上所有节点值相加等于目标和 targetSum。
# 叶子节点是指没有子节点的节点。
#
# 思路分析：
# 1. 使用递归，从根节点开始，每次递归时减去当前节点的值
# 2. 当到达叶子节点时，检查剩余的目标和是否等于叶子节点的值
# 3. 递归地检查左右子树是否存在满足条件的路径
#
# 时间复杂度：O(n)，其中n是二叉树的节点数，每个节点访问一次
# 空间复杂度：O(h)，其中h是二叉树的高度，递归调用栈的深度
#
# 是否为最优解：是。递归遍历是解决此类路径问题的最优方法。
#
# 边界场景：
# - 空树：返回False
# - 只有根节点：检查根节点值是否等于targetSum
# - 负数节点值：算法依然有效
# - 目标和为0：正常处理
def has_path_sum(root: Optional[TreeNode], target_sum: int) -> bool:
    # 边界情况：空节点返回False
    if root is None:
        return False
    # 到达叶子节点，检查路径和是否等于目标和
    if root.left is None and root.right is None:
        return root.val == target_sum
    # 递归检查左右子树，目标和减去当前节点的值
    return (has_path_sum(root.left, target_sum - root.val) or 
            has_path_sum(root.right, target_sum - root.val))


# LeetCode 113. 路径总和 II
# 题目来源：LeetCode
# 题目链接：https://leetcode.cn/problems/path-sum-ii/
# 题目描述：给你二叉树的根节点 root 和一个整数目标和 targetSum，
# 找出所有 从根节点到叶子节点 路径总和等于给定目标和的路径。
#
# 思路分析：
# 1. 使用回溯法，维护一个当前路径列表
# 2. 递归遍历树，每次将当前节点加入路径
# 3. 到达叶子节点时，检查路径和是否等于目标和，若是则将路径加入结果
# 4. 回溯时移除当前节点
#
# 时间复杂度：O(n^2)，其中n是节点数，最坏情况下需要复制所有路径
# 空间复杂度：O(n)，递归栈和路径存储的空间
#
# 是否为最优解：是。回溯+递归是解决所有路径问题的标准方法。

def path_sum_helper(node: Optional[TreeNode], target_sum: int, 
                    path: List[int], result: List[List[int]]) -> None:
    if node is None:
        return
    # 将当前节点加入路径
    path.append(node.val)
    # 到达叶子节点，检查路径和
    if node.left is None and node.right is None and node.val == target_sum:
        result.append(path[:])  # 复制当前路径
    # 递归遍历左右子树
    path_sum_helper(node.left, target_sum - node.val, path, result)
    path_sum_helper(node.right, target_sum - node.val, path, result)
    # 回溯：移除当前节点
    path.pop()


def path_sum(root: Optional[TreeNode], target_sum: int) -> List[List[int]]:
    result = []
    path = []
    path_sum_helper(root, target_sum, path, result)
    return result


# LeetCode 111. 二叉树的最小深度
# 题目来源：LeetCode
# 题目链接：https://leetcode.cn/problems/minimum-depth-of-binary-tree/
# 题目描述：给定一个二叉树，找出其最小深度。
# 最小深度是从根节点到最近叶子节点的最短路径上的节点数量。
#
# 思路分析：
# 1. 使用递归，注意必须到达叶子节点才算一条路径
# 2. 如果一个节点只有左子树或只有右子树，不能简单取min，要继续递归非空子树
# 3. 只有当左右子树都存在时，才取较小深度
#
# 时间复杂度：O(n)，其中n是节点数
# 空间复杂度：O(h)，其中h是树的高度
#
# 是否为最优解：是。但BFS层序遍历也是最优解，在某些情况下更快（遇到第一个叶子节点即可返回）。
#
# 常见错误：直接用min(左深度, 右深度)会在单子树情况下出错
def min_depth(root: Optional[TreeNode]) -> int:
    if root is None:
        return 0
    # 如果左子树为空，只递归右子树
    if root.left is None:
        return min_depth(root.right) + 1
    # 如果右子树为空，只递归左子树
    if root.right is None:
        return min_depth(root.left) + 1
    # 左右子树都存在，取较小深度
    return min(min_depth(root.left), min_depth(root.right)) + 1


# LeetCode 257. 二叉树的所有路径
# 题目来源：LeetCode
# 题目链接：https://leetcode.cn/problems/binary-tree-paths/
# 题目描述：给你一个二叉树的根节点 root，按 任意顺序，
# 返回所有从根节点到叶子节点的路径。
#
# 思路分析：
# 1. 使用递归+回溯，构建路径字符串
# 2. 到达叶子节点时，将路径字符串加入结果
# 3. Python中字符串是不可变的，所以每次拼接都会创建新字符串，无需显式回溯
#
# 时间复杂度：O(n^2)，需要构建和复制路径字符串
# 空间复杂度：O(n)，递归栈和结果存储
#
# 是否为最优解：是。递归+回溯是标准解法。

def binary_tree_paths_helper(node: Optional[TreeNode], path: str, 
                             result: List[str]) -> None:
    if node is None:
        return
    # 构建当前路径
    path += str(node.val)
    # 到达叶子节点，加入结果
    if node.left is None and node.right is None:
        result.append(path)
        return
    # 继续递归，路径中加入箭头
    path += "->"
    binary_tree_paths_helper(node.left, path, result)
    binary_tree_paths_helper(node.right, path, result)


def binary_tree_paths(root: Optional[TreeNode]) -> List[str]:
    result = []
    if root is None:
        return result
    binary_tree_paths_helper(root, "", result)
    return result


# LeetCode 543. 二叉树的直径
# 题目来源：LeetCode
# 题目链接：https://leetcode.cn/problems/diameter-of-binary-tree/
# 题目描述：给定一棵二叉树，你需要计算它的直径长度。
# 一棵二叉树的直径长度是任意两个结点路径长度中的最大值。
# 这条路径可能穿过也可能不穿过根节点。
#
# 思路分析：
# 1. 直径 = 某个节点的左子树最大深度 + 右子树最大深度
# 2. 需要递归计算每个节点的这个值，并维护全局最大值
# 3. 使用后序遍历，先计算子树深度，再更新直径
#
# 时间复杂度：O(n)，每个节点访问一次
# 空间复杂度：O(h)，递归栈深度
#
# 是否为最优解：是。一次遍历即可得到答案。

class DiameterSolution:
    def __init__(self):
        self.max_diameter = 0
    
    def get_depth(self, node: Optional[TreeNode]) -> int:
        if node is None:
            return 0
        # 递归计算左右子树深度
        left_depth = self.get_depth(node.left)
        right_depth = self.get_depth(node.right)
        # 更新最大直径：左深度 + 右深度
        self.max_diameter = max(self.max_diameter, left_depth + right_depth)
        # 返回当前节点的深度
        return max(left_depth, right_depth) + 1
    
    def diameter_of_binary_tree(self, root: Optional[TreeNode]) -> int:
        self.max_diameter = 0
        self.get_depth(root)
        return self.max_diameter


def diameter_of_binary_tree(root: Optional[TreeNode]) -> int:
    """便捷函数"""
    solution = DiameterSolution()
    return solution.diameter_of_binary_tree(root)


# LeetCode 404. 左叶子之和
# 题目来源：LeetCode
# 题目链接：https://leetcode.cn/problems/sum-of-left-leaves/
# 题目描述：给定二叉树的根节点 root，返回所有左叶子之和。
#
# 思路分析：
# 1. 递归遍历树，判断节点是否为左叶子
# 2. 左叶子的定义：是某个节点的左孩子，且该孩子没有子节点
# 3. 需要从父节点判断，而不是在节点自身判断
#
# 时间复杂度：O(n)
# 空间复杂度：O(h)
#
# 是否为最优解：是
def sum_of_left_leaves(root: Optional[TreeNode]) -> int:
    if root is None:
        return 0
    total = 0
    # 检查左子节点是否为叶子
    if (root.left is not None and 
        root.left.left is None and 
        root.left.right is None):
        total += root.left.val
    # 递归计算左右子树的左叶子之和
    total += sum_of_left_leaves(root.left)
    total += sum_of_left_leaves(root.right)
    return total


# LeetCode 572. 另一棵树的子树
# 题目来源：LeetCode
# 题目链接：https://leetcode.cn/problems/subtree-of-another-tree/
# 题目描述：给你两棵二叉树 root 和 subRoot。
# 检验 root 中是否包含和 subRoot 具有相同结构和节点值的子树。
#
# 思路分析：
# 1. 递归检查root的每个节点，看是否与subRoot相同
# 2. 使用isSameTree函数检查两棵树是否相同
# 3. 如果当前节点不匹配，继续递归检查左右子树
#
# 时间复杂度：O(m*n)，m和n分别是两棵树的节点数
# 空间复杂度：O(max(h1, h2))，递归栈深度
#
# 是否为最优解：否。更优解法是使用KMP或序列化+字符串匹配，时间复杂度O(m+n)
# 但递归解法更直观，在面试中更常用
def is_subtree(root: Optional[TreeNode], sub_root: Optional[TreeNode]) -> bool:
    if root is None:
        return False
    # 检查当前节点是否与subRoot相同
    if is_same_tree(root, sub_root):
        return True
    # 递归检查左右子树
    return is_subtree(root.left, sub_root) or is_subtree(root.right, sub_root)


# LeetCode 617. 合并二叉树
# 题目来源：LeetCode
# 题目链接：https://leetcode.cn/problems/merge-two-binary-trees/
# 题目描述：给你两棵二叉树：root1 和 root2。
# 想象一下，当你将其中一棵覆盖到另一棵之上时，两棵树上的一些节点将会重叠。
# 你需要将这两棵树合并成一棵新二叉树。合并规则是：
# 如果两个节点重叠，那么将这两个节点的值相加作为合并后节点的新值；
# 否则，不为 null 的节点将直接作为新二叉树的节点。
#
# 思路分析：
# 1. 同时递归遍历两棵树
# 2. 如果两个节点都存在，值相加
# 3. 如果只有一个节点存在，直接使用该节点
#
# 时间复杂度：O(min(m,n))
# 空间复杂度：O(min(h1,h2))
#
# 是否为最优解：是
def merge_trees(root1: Optional[TreeNode], root2: Optional[TreeNode]) -> Optional[TreeNode]:
    # 如果一棵树为空，返回另一棵树
    if root1 is None:
        return root2
    if root2 is None:
        return root1
    # 创建新节点，值为两节点之和
    merged = TreeNode(root1.val + root2.val)
    # 递归合并左右子树
    merged.left = merge_trees(root1.left, root2.left)
    merged.right = merge_trees(root1.right, root2.right)
    return merged


# LeetCode 654. 最大二叉树
# 题目来源：LeetCode  
# 题目链接：https://leetcode.cn/problems/maximum-binary-tree/
# 题目描述：给定一个不重复的整数数组 nums。
# 最大二叉树可以用下面的算法从 nums 递归地构建:
# 1. 创建一个根节点，其值为 nums 中的最大值。
# 2. 递归地在最大值左边的子数组前缀上构建左子树。
# 3. 递归地在最大值右边的子数组后缀上构建右子树。
#
# 思路分析：
# 1. 找到数组中的最大值及其索引
# 2. 最大值作为根节点
# 3. 递归构建左右子树
#
# 时间复杂度：O(n^2)，最坏情况下数组有序，每次都要遍历剩余元素找最大值
# 空间复杂度：O(n)，递归栈深度
#
# 是否为最优解：否。使用单调栈可以优化到O(n)时间复杂度
# 但递归解法更符合题意，代码更简洁

def build_max_tree(nums: List[int], left: int, right: int) -> Optional[TreeNode]:
    if left > right:
        return None
    # 找到最大值的索引
    max_index = left
    for i in range(left + 1, right + 1):
        if nums[i] > nums[max_index]:
            max_index = i
    # 创建根节点
    root = TreeNode(nums[max_index])
    # 递归构建左右子树
    root.left = build_max_tree(nums, left, max_index - 1)
    root.right = build_max_tree(nums, max_index + 1, right)
    return root


def construct_maximum_binary_tree(nums: List[int]) -> Optional[TreeNode]:
    return build_max_tree(nums, 0, len(nums) - 1)


# LeetCode 563. 二叉树的坡度
# 题目来源：LeetCode
# 题目链接：https://leetcode.cn/problems/binary-tree-tilt/
# 题目描述：给你一个二叉树的根节点 root，计算并返回 整个树 的坡度。
# 一个树的节点的坡度定义即为，该节点左子树的节点之和和右子树节点之和的差的绝对值。
# 如果没有左子树的话，左子树的节点之和为 0 ；没有右子树的话也是一样。
# 空节点的坡度是 0。整个树的坡度就是其所有节点的坡度之和。
#
# 思路分析：
# 1. 使用后序遍历，先计算子树的节点和
# 2. 对于每个节点，计算左右子树节点和的差的绝对值，累加到总坡度
# 3. 返回当前子树的节点和（包括根节点）
#
# 时间复杂度：O(n)
# 空间复杂度：O(h)
#
# 是否为最优解：是

class TiltSolution:
    def __init__(self):
        self.total_tilt = 0
    
    def calculate_sum(self, node: Optional[TreeNode]) -> int:
        if node is None:
            return 0
        # 计算左右子树的节点和
        left_sum = self.calculate_sum(node.left)
        right_sum = self.calculate_sum(node.right)
        # 累加当前节点的坡度
        self.total_tilt += abs(left_sum - right_sum)
        # 返回当前子树的节点和
        return left_sum + right_sum + node.val
    
    def find_tilt(self, root: Optional[TreeNode]) -> int:
        self.total_tilt = 0
        self.calculate_sum(root)
        return self.total_tilt


def find_tilt(root: Optional[TreeNode]) -> int:
    """便捷函数"""
    solution = TiltSolution()
    return solution.find_tilt(root)


# LeetCode 508. 出现次数最多的子树元素和
# 题目来源：LeetCode
# 题目链接：https://leetcode.cn/problems/most-frequent-subtree-sum/
# 题目描述：给你一个二叉树的根结点 root，计算出现最多的子树元素和。
# 一个结点的 「子树元素和」定义为：以该结点为根的二叉树上所有结点的元素之和（包括结点自身）。
#
# 思路分析：
# 1. 使用后序遍历计算每个子树的元素和
# 2. 使用字典统计每个和出现的次数
# 3. 找出出现次数最多的所有和
#
# 时间复杂度：O(n)
# 空间复杂度：O(n)，需要字典存储每个子树和
#
# 是否为最优解：是

def calculate_tree_sum(node: Optional[TreeNode], 
                      sum_count: dict) -> int:
    if node is None:
        return 0
    # 计算左右子树的和
    left_sum = calculate_tree_sum(node.left, sum_count)
    right_sum = calculate_tree_sum(node.right, sum_count)
    # 当前子树的总和
    total_sum = left_sum + right_sum + node.val
    # 统计出现次数
    sum_count[total_sum] = sum_count.get(total_sum, 0) + 1
    return total_sum


def find_frequent_tree_sum(root: Optional[TreeNode]) -> List[int]:
    sum_count = {}
    calculate_tree_sum(root, sum_count)
    if not sum_count:
        return []
    # 找到最大频率
    max_freq = max(sum_count.values())
    # 收集所有最大频率的和
    return [s for s, count in sum_count.items() if count == max_freq]


# LeetCode 437. 路径总和 III
# 题目来源：LeetCode
# 题目链接：https://leetcode.cn/problems/path-sum-iii/
# 题目描述：给定一个二叉树的根节点 root，和一个整数 targetSum，
# 求该二叉树里节点值之和等于 targetSum 的 路径 的数目。
# 路径 不需要从根节点开始，也不需要在叶子节点结束，但是路径方向必须是向下的（只从父节点到子节点）。
#
# 思路分析：
# 方法1：双重递归 - 需要两个递归函数
#   1. 第一个递归遍历所有节点
#   2. 第二个递归以当前节点为起点计算路径数
# 方法2：前缀和+HashMap - 更优的解法
#   1. 使用前缀和思想，类似于数组的子数组和问题
#   2. 用字典存储前缀和及其出现次数
#
# 时间复杂度：方法1 O(n^2)，方法2 O(n)
# 空间复杂度：方法1 O(h)，方法2 O(n)
#
# 是否为最优解：方法2是最优解，但方法1更直观。这里实现两种方法。

# 方法1：双重递归
def count_paths_from(node: Optional[TreeNode], target_sum: int) -> int:
    """计算从当前节点开始的路径数"""
    if node is None:
        return 0
    count = 0
    # 如果当前节点的值等于目标和，找到一条路径
    if node.val == target_sum:
        count += 1
    # 继续在左右子树中查找，目标和减去当前节点值
    count += count_paths_from(node.left, target_sum - node.val)
    count += count_paths_from(node.right, target_sum - node.val)
    return count


def path_sum_iii(root: Optional[TreeNode], target_sum: int) -> int:
    if root is None:
        return 0
    # 以当前节点为起点的路径数 + 左子树中的路径数 + 右子树中的路径数
    return (count_paths_from(root, target_sum) + 
            path_sum_iii(root.left, target_sum) + 
            path_sum_iii(root.right, target_sum))


# 方法2：前缀和 + 字典（最优解）
def dfs_with_prefix_sum(node: Optional[TreeNode], current_sum: int, 
                       target_sum: int, prefix_sum_count: dict) -> int:
    if node is None:
        return 0
    # 当前路径的前缀和
    current_sum += node.val
    # 查找是否存在前缀和为 currentSum - targetSum 的路径
    count = prefix_sum_count.get(current_sum - target_sum, 0)
    # 将当前前缀和加入字典
    prefix_sum_count[current_sum] = prefix_sum_count.get(current_sum, 0) + 1
    # 递归遍历左右子树
    count += dfs_with_prefix_sum(node.left, current_sum, target_sum, prefix_sum_count)
    count += dfs_with_prefix_sum(node.right, current_sum, target_sum, prefix_sum_count)
    # 回溯：移除当前节点的前缀和
    prefix_sum_count[current_sum] -= 1
    return count


def path_sum_iii_optimal(root: Optional[TreeNode], target_sum: int) -> int:
    prefix_sum_count = {0: 1}  # 初始化：前缀和为0的路径有一条
    return dfs_with_prefix_sum(root, 0, target_sum, prefix_sum_count)


# LeetCode 236. 二叉树的最近公共祖先
# 题目来源：LeetCode
# 题目链接：https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/
# 题目描述：给定一个二叉树，找到该树中两个指定节点的最近公共祖先。
#
# 思路分析：
# 1. 使用递归，分为三种情况：
#    a. 如果p和q分别在左右子树，则当前节点就是最近公共祖先
#    b. 如果p和q都在左子树，则最近公共祖先在左子树
#    c. 如果p和q都在右子树，则最近公共祖先在右子树
# 2. 特殊情况：当前节点就是p或q，且另一个节点在其子树中
#
# 时间复杂度：O(n)，最坏情况需要遍历所有节点
# 空间复杂度：O(h)，递归栈深度
#
# 是否为最优解：是。这是经典的递归解法。
def lowest_common_ancestor(root: Optional[TreeNode], 
                          p: TreeNode, q: TreeNode) -> Optional[TreeNode]:
    # 基础情况：空节点或找到目标节点
    if root is None or root == p or root == q:
        return root
    # 递归在左右子树中查找
    left = lowest_common_ancestor(root.left, p, q)
    right = lowest_common_ancestor(root.right, p, q)
    # 如果左右子树都找到了，说明p和q分别在左右两侧，当前节点就是最近公共祖先
    if left is not None and right is not None:
        return root
    # 否则返回非空的一侧
    return left if left is not None else right


# LeetCode 124. 二叉树中的最大路径和
# 题目来源：LeetCode
# 题目链接：https://leetcode.cn/problems/binary-tree-maximum-path-sum/
# 题目描述：二叉树中的 路径 被定义为一条从树中任意节点出发，
# 沿父节点-子节点连接，达到任意节点的序列。同一个节点在一条路径序列中 至多出现一次。
# 该路径 至少包含一个 节点，且不一定经过根节点。
# 路径和 是路径中各节点值的总和。给你一个二叉树的根节点 root ，返回其 最大路径和。
#
# 思路分析：
# 1. 对于每个节点，最大路径和可能是：
#    - 左子树的最大贡献 + 节点值 + 右子树的最大贡献
# 2. 但返回给父节点时，只能选择左或右一侧（因为路径不能分叉）
# 3. 如果子树贡献为负，则不选择该子树（贡献为0）
#
# 时间复杂度：O(n)
# 空间复杂度：O(h)
#
# 是否为最优解：是。一次遍历即可得到答案。
#
# 这是Hard难度的经典题，需要理解"局部最优"和"向上返回"的区别

class MaxPathSumSolution:
    def __init__(self):
        self.max_path_sum = -2147483648  # Use integer instead of float
    
    def max_gain(self, node: Optional[TreeNode]) -> int:
        if node is None:
            return 0
        # 递归计算左右子树的最大贡献，负数则不选
        left_gain = max(self.max_gain(node.left), 0)
        right_gain = max(self.max_gain(node.right), 0)
        # 更新全局最大路径和：左贡献 + 节点值 + 右贡献
        current_path_sum = left_gain + node.val + right_gain
        self.max_path_sum = max(self.max_path_sum, current_path_sum)
        # 返回节点的最大贡献：节点值 + max(左贡献, 右贡献)
        return node.val + max(left_gain, right_gain)
    
    def max_path_sum_func(self, root: Optional[TreeNode]) -> int:
        self.max_path_sum = -2147483648
        self.max_gain(root)
        return self.max_path_sum


def max_path_sum_tree(root: Optional[TreeNode]) -> int:
    """便捷函数"""
    solution = MaxPathSumSolution()
    return solution.max_path_sum_func(root)


# LintCode 453. 将二叉树拆分为链表
# 题目来源：LintCode（炼码）
# 题目链接：https://www.lintcode.com/problem/453/
# 题目描述：将一棵二叉树按照前序遍历拆解成为一个假链表。所谓的假链表是说，用二叉树的 right 指针，来表示链表中的 next 指针。
# 要求不能创建任何新的节点，只能调整树中节点指针的指向。
#
# 思路分析：
# 1. 使用后序遍历，先处理左右子树
# 2. 对于每个节点，将左子树变成链表，右子树变成链表
# 3. 将左子树链接到右子树上，左子树指针设为None
# 4. 返回当前链表的末尾节点
#
# 时间复杂度：O(n)
# 空间复杂度：O(h)
#
# 是否为最优解：是

def flatten_helper(node: Optional[TreeNode]) -> Optional[TreeNode]:
    """递归处理节点，返回处理后链表的末尾节点"""
    if node is None:
        return None
    
    # 叶子节点直接返回
    if node.left is None and node.right is None:
        return node
    
    # 递归处理左右子树
    left_tail = flatten_helper(node.left)
    right_tail = flatten_helper(node.right)
    
    # 如果左子树不为空，将左子树连接到右子树上
    if left_tail:
        left_tail.right = node.right
        node.right = node.left
        node.left = None
    
    # 返回新的链表末尾节点
    return right_tail if right_tail else left_tail

def flatten(root: Optional[TreeNode]) -> None:
    """将二叉树拆分为链表"""
    if root is None:
        return
    flatten_helper(root)


# HackerRank 二叉树的镜像
# 题目来源：HackerRank
# 题目描述：给定一棵二叉树，判断它是否是自身的镜像（即对称）
#
# 思路分析：
# 1. 使用辅助函数检查两棵子树是否互为镜像
# 2. 两棵子树互为镜像的条件：
#    - 当前节点值相等
#    - 左子树的左子树与右子树的右子树互为镜像
#    - 左子树的右子树与右子树的左子树互为镜像
#
# 时间复杂度：O(n)
# 空间复杂度：O(h)
#
# 是否为最优解：是
def is_mirror_advanced(left: Optional[TreeNode], right: Optional[TreeNode]) -> bool:
    """检查两棵子树是否互为镜像"""
    if left is None and right is None:
        return True
    if left is None or right is None:
        return False
    
    return (left.val == right.val and 
            is_mirror_advanced(left.left, right.right) and 
            is_mirror_advanced(left.right, right.left))

def is_symmetric_advanced(root: Optional[TreeNode]) -> bool:
    """判断二叉树是否是自身的镜像"""
    if root is None:
        return True
    return is_mirror_advanced(root.left, root.right)


# 剑指Offer 26. 树的子结构
# 题目来源：剑指Offer
# 题目描述：输入两棵二叉树A和B，判断B是不是A的子结构。
#
# 思路分析：
# 1. 遍历树A的每个节点，以该节点为根节点
# 2. 检查从该节点开始的子树是否包含树B的结构
#
# 时间复杂度：O(m*n)
# 空间复杂度：O(h)
#
# 是否为最优解：是
def is_sub_structure_helper(a: Optional[TreeNode], b: Optional[TreeNode]) -> bool:
    """检查以a为根的子树是否包含树b的结构"""
    if b is None:
        return True
    if a is None or a.val != b.val:
        return False
    
    return (is_sub_structure_helper(a.left, b.left) and 
            is_sub_structure_helper(a.right, b.right))

def is_sub_structure(a: Optional[TreeNode], b: Optional[TreeNode]) -> bool:
    """判断B是否是A的子结构"""
    if a is None or b is None:
        return False
    
    # 检查当前节点，或者在左子树中检查，或者在右子树中检查
    return (is_sub_structure_helper(a, b) or 
            is_sub_structure(a.left, b) or 
            is_sub_structure(a.right, b))


# USACO 二叉搜索树的最近公共祖先
# 题目来源：USACO（美国计算机奥林匹克竞赛）
# 题目描述：给定一个二叉搜索树（BST），找到该树中两个指定节点的最近公共祖先。
#
# 思路分析：
# 利用BST的特性：左子树所有节点值小于根节点，右子树所有节点值大于根节点
# 1. 如果p和q的值都小于当前节点，那么LCA在左子树
# 2. 如果p和q的值都大于当前节点，那么LCA在右子树
# 3. 否则，当前节点就是LCA
#
# 时间复杂度：O(h)
# 空间复杂度：O(h)
#
# 是否为最优解：是
def lowest_common_ancestor_bst(root: Optional[TreeNode], 
                            p: TreeNode, q: TreeNode) -> Optional[TreeNode]:
    """在二叉搜索树中查找最近公共祖先"""
    if root is None or p is None or q is None:
        return None
    
    # 如果p和q都在左子树
    if p.val < root.val and q.val < root.val:
        return lowest_common_ancestor_bst(root.left, p, q)
    # 如果p和q都在右子树
    if p.val > root.val and q.val > root.val:
        return lowest_common_ancestor_bst(root.right, p, q)
    # 否则当前节点就是LCA
    return root


# AtCoder ABC191 E. Come Back Quickly - 距离和计算
# 题目描述简化：给定一棵有根树，计算每个节点到其所有子孙节点的距离之和
#
# 思路分析：
# 1. 使用后序遍历计算每个子树的节点数
# 2. 使用前序遍历计算距离之和
#
# 时间复杂度：O(n)
# 空间复杂度：O(n)
#
# 是否为最优解：是
def dfs_size(node: Optional[TreeNode], size: dict) -> int:
    """后序遍历计算每个子树的节点数"""
    if node is None:
        return 0
    
    size[node.val] = 1  # 包含自己
    size[node.val] += dfs_size(node.left, size)
    size[node.val] += dfs_size(node.right, size)
    
    return size[node.val]

def dfs_distance(node: Optional[TreeNode], size: dict, 
                result: dict, parent_distance: int) -> None:
    """前序遍历计算距离和"""
    if node is None:
        return
    
    result[node.val] = parent_distance
    
    if node.left:
        left_size = size[node.left.val]
        right_size = size[node.right.val] if node.right else 0
        left_distance = parent_distance + (size[node.val] - left_size) - left_size
        dfs_distance(node.left, size, result, left_distance)
    
    if node.right:
        right_size = size[node.right.val]
        left_size = size[node.left.val] if node.left else 0
        right_distance = parent_distance + (size[node.val] - right_size) - right_size
        dfs_distance(node.right, size, result, right_distance)

def calculate_distance_sum(root: Optional[TreeNode]) -> dict:
    """计算每个节点到其所有子孙节点的距离之和"""
    if root is None:
        return {}
    
    size = {}
    result = {}
    
    dfs_size(root, size)
    dfs_distance(root, size, result, 0)
    
    return result


# CodeChef - SUBTREE - 最大子树和
# 题目描述简化：给定一棵二叉树，每个节点有一个权值。找出权值和最大的子树。
#
# 思路分析：
# 1. 使用后序遍历，计算每个子树的权值和
# 2. 对于每个节点，其最大子树和为：节点值 + max(左子树最大和, 0) + max(右子树最大和, 0)
#
# 时间复杂度：O(n)
# 空间复杂度：O(h)
#
# 是否为最优解：是

class MaxSubtreeSumSolution:
    def __init__(self):
        self.max_subtree_sum = -2147483648  # 使用整数最小值
    
    def calculate_subtree_sum(self, node: Optional[TreeNode]) -> int:
        """计算子树和并更新最大子树和"""
        if node is None:
            return 0
        
        left_sum = max(self.calculate_subtree_sum(node.left), 0)
        right_sum = max(self.calculate_subtree_sum(node.right), 0)
        
        current_sum = node.val + left_sum + right_sum
        self.max_subtree_sum = max(self.max_subtree_sum, current_sum)
        
        # 返回当前节点的最大贡献（用于父节点路径）
        return node.val + max(left_sum, right_sum)
    
    def max_subtree_sum_func(self, root: Optional[TreeNode]) -> int:
        """获取最大子树和"""
        self.max_subtree_sum = -2147483648
        self.calculate_subtree_sum(root)
        return self.max_subtree_sum

def max_subtree_sum(root: Optional[TreeNode]) -> int:
    """便捷函数：获取最大子树和"""
    solution = MaxSubtreeSumSolution()
    return solution.max_subtree_sum_func(root)


# UVa OJ 10080 - 重建二叉树
# 题目描述简化：根据前序遍历和中序遍历结果重建二叉树
#
# 思路分析：
# 1. 前序遍历的第一个节点是根节点
# 2. 在中序遍历中找到根节点的位置，分割左右子树
# 3. 递归重建左右子树
#
# 时间复杂度：O(n^2)，可以使用哈希表优化为O(n)
# 空间复杂度：O(n)
#
# 最优解：可以使用哈希表优化查找根节点的过程

def build_tree_helper(preorder: List[int], pre_start: int, pre_end: int,
                     inorder: List[int], in_start: int, in_end: int,
                     inorder_map: dict) -> Optional[TreeNode]:
    """递归构建二叉树"""
    if pre_start > pre_end or in_start > in_end:
        return None
    
    root_val = preorder[pre_start]
    root = TreeNode(root_val)
    
    # 找到根节点在中序遍历中的位置
    root_index = inorder_map.get(root_val, in_start)
    # 如果没有哈希表，使用线性查找
    if root_index == in_start and inorder[root_index] != root_val:
        for i in range(in_start, in_end + 1):
            if inorder[i] == root_val:
                root_index = i
                break
    
    left_size = root_index - in_start
    
    root.left = build_tree_helper(preorder, pre_start + 1, pre_start + left_size,
                                inorder, in_start, root_index - 1, inorder_map)
    root.right = build_tree_helper(preorder, pre_start + left_size + 1, pre_end,
                                 inorder, root_index + 1, in_end, inorder_map)
    
    return root

def build_tree(preorder: List[int], inorder: List[int]) -> Optional[TreeNode]:
    """根据前序和中序遍历结果重建二叉树"""
    if not preorder or not inorder:
        return None
    
    # 创建中序遍历值到索引的映射，优化查找
    inorder_map = {val: idx for idx, val in enumerate(inorder)}
    
    return build_tree_helper(preorder, 0, len(preorder) - 1,
                           inorder, 0, len(inorder) - 1, inorder_map)


# 牛客网 NC102. 树的序列化和反序列化
# 题目描述：将二叉树序列化为字符串，然后从字符串反序列化回二叉树
#
# 思路分析：
# 序列化使用前序遍历，空节点用特殊字符表示
#
# 时间复杂度：O(n)
# 空间复杂度：O(n)
#
# 是否为最优解：是
def serialize_helper(node: Optional[TreeNode], result: list) -> None:
    """递归序列化二叉树"""
    if node is None:
        result.append("#")
        return
    
    result.append(str(node.val))
    serialize_helper(node.left, result)
    serialize_helper(node.right, result)

def serialize(root: Optional[TreeNode]) -> str:
    """将二叉树序列化为字符串"""
    result = []
    serialize_helper(root, result)
    return ",".join(result)

def deserialize_helper(nodes: list, index: list) -> Optional[TreeNode]:
    """递归反序列化二叉树"""
    if index[0] >= len(nodes) or nodes[index[0]] == "#":
        index[0] += 1
        return None
    
    node = TreeNode(int(nodes[index[0]]))
    index[0] += 1
    node.left = deserialize_helper(nodes, index)
    node.right = deserialize_helper(nodes, index)
    
    return node

def deserialize(data: str) -> Optional[TreeNode]:
    """将字符串反序列化为二叉树"""
    if not data:
        return None
    
    nodes = data.split(",")
    index = [0]  # 使用列表存储索引，以便在递归中修改
    return deserialize_helper(nodes, index)


# 杭电OJ 2024 - 二叉树遍历
# 题目描述：输入二叉树的前序遍历和中序遍历结果，输出其后序遍历结果
#
# 思路分析：
# 1. 先根据前序和中序构建二叉树
# 2. 然后进行后序遍历输出
#
# 时间复杂度：O(n^2)
# 空间复杂度：O(n)
#
# 最优解：可以使用哈希表优化查找过程
def postorder_helper(preorder: str, pre_start: int, pre_end: int,
                    inorder: str, in_start: int, in_end: int,
                    result: list, inorder_map: dict) -> None:
    """递归构建二叉树并生成后序遍历结果"""
    if pre_start > pre_end or in_start > in_end:
        return
    
    root_val = preorder[pre_start]
    
    # 找到根节点在中序中的位置
    root_index = inorder_map.get(root_val, in_start)
    # 如果没有哈希表，使用线性查找
    if root_index == in_start and inorder[root_index] != root_val:
        for i in range(in_start, in_end + 1):
            if inorder[i] == root_val:
                root_index = i
                break
    
    left_length = root_index - in_start
    
    # 递归处理左右子树
    postorder_helper(preorder, pre_start + 1, pre_start + left_length,
                   inorder, in_start, root_index - 1, result, inorder_map)
    postorder_helper(preorder, pre_start + left_length + 1, pre_end,
                   inorder, root_index + 1, in_end, result, inorder_map)
    
    # 后序：添加根节点
    result.append(root_val)

def postorder_from_preorder_and_inorder(preorder: str, inorder: str) -> str:
    """根据前序和中序遍历结果生成后序遍历结果"""
    if not preorder or not inorder:
        return ""
    
    # 创建中序遍历字符到索引的映射，优化查找
    inorder_map = {char: idx for idx, char in enumerate(inorder)}
    
    result = []
    postorder_helper(preorder, 0, len(preorder) - 1,
                   inorder, 0, len(inorder) - 1, result, inorder_map)
    
    return "".join(result)


# 主函数测试
if __name__ == "__main__":
    print("========== 二叉树递归遍历基础测试 ==========")
    head = TreeNode(1)
    head.left = TreeNode(2)
    head.right = TreeNode(3)
    head.left.left = TreeNode(4)
    head.left.right = TreeNode(5)
    head.right.left = TreeNode(6)
    head.right.right = TreeNode(7)

    print("前序遍历：", end="")
    pre_order(head)
    print()

    print("中序遍历：", end="")
    in_order(head)
    print()

    print("后序遍历：", end="")
    pos_order(head)
    print()

    print("\n========== LeetCode 104. 最大深度 ==========")
    print(f"最大深度: {max_depth(head)}")  # 预期: 3

    print("\n========== LeetCode 110. 平衡二叉树 ==========")
    balanced_tree = TreeNode(1)
    balanced_tree.left = TreeNode(2)
    balanced_tree.right = TreeNode(3)
    balanced_tree.left.left = TreeNode(4)
    balanced_tree.left.right = TreeNode(5)
    print(f"是否为平衡二叉树: {is_balanced(balanced_tree)}")  # 预期: True

    print("\n========== LeetCode 112. 路径总和 ==========")
    path_tree = TreeNode(5)
    path_tree.left = TreeNode(4)
    path_tree.right = TreeNode(8)
    path_tree.left.left = TreeNode(11)
    path_tree.left.left.left = TreeNode(7)
    path_tree.left.left.right = TreeNode(2)
    print(f"是否存在路径和为22: {has_path_sum(path_tree, 22)}")  # 预期: True

    print("\n========== LeetCode 113. 路径总和 II ==========")
    paths = path_sum(path_tree, 22)
    print(f"路径总和为22的所有路径: {paths}")  # 预期: [[5,4,11,2]]

    print("\n========== 所有测试完成！ ==========")


# =========================== 更多平台题目实现 ===========================

# 洛谷 P1305 新二叉树
# 题目来源：洛谷 (Luogu)
# 题目链接：https://www.luogu.com.cn/problem/P1305
# 题目描述：根据前序遍历字符串构建二叉树并输出中序遍历
#
# 思路分析：
# 1. 前序遍历字符串中，'#'表示空节点
# 2. 使用递归构建二叉树
# 3. 输出中序遍历结果
#
# 时间复杂度：O(n)
# 空间复杂度：O(n)
#
# 是否为最优解：是
class P1305Solution:
    def __init__(self):
        self.index = 0
    
    def build_tree(self, preorder: str) -> Optional[TreeNode]:
        """根据前序遍历字符串构建二叉树"""
        if self.index >= len(preorder) or preorder[self.index] == '#':
            self.index += 1
            return None
        root = TreeNode(preorder[self.index])
        self.index += 1
        root.left = self.build_tree(preorder)
        root.right = self.build_tree(preorder)
        return root
    
    def inorder_traversal(self, root: Optional[TreeNode]) -> List[str]:
        """中序遍历二叉树"""
        result = []
        self._inorder_helper(root, result)
        return result
    
    def _inorder_helper(self, node: Optional[TreeNode], result: List[str]) -> None:
        if node is None:
            return
        self._inorder_helper(node.left, result)
        result.append(str(node.val))
        self._inorder_helper(node.right, result)


# TimusOJ 1022 Genealogical Tree
# 题目来源：Timus Online Judge
# 题目链接：http://acm.timus.ru/problem.aspx?space=1&num=1022
# 题目描述：给定家族关系，构建家谱树并输出拓扑排序
#
# 思路分析：
# 1. 使用邻接表表示有向无环图
# 2. 使用深度优先搜索进行拓扑排序
# 3. 使用后序遍历得到拓扑序列
#
# 时间复杂度：O(n + m)
# 空间复杂度：O(n)
#
# 是否为最优解：是
class TimusOJ1022:
    def topological_sort(self, n: int, edges: List[List[int]]) -> List[int]:
        """拓扑排序"""
        graph = [[] for _ in range(n + 1)]
        for u, v in edges:
            graph[u].append(v)
        
        visited = [False] * (n + 1)
        result = []
        
        for i in range(1, n + 1):
            if not visited[i]:
                self._dfs(i, graph, visited, result)
        
        return result[::-1]  # 反转得到拓扑排序
    
    def _dfs(self, node: int, graph: List[List[int]], visited: List[bool], result: List[int]) -> None:
        """深度优先搜索"""
        visited[node] = True
        for neighbor in graph[node]:
            if not visited[neighbor]:
                self._dfs(neighbor, graph, visited, result)
        result.append(node)


# AizuOJ ALDS1_7_C Tree Walk
# 题目来源：Aizu Online Judge
# 题目链接：http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_7_C
# 题目描述：实现二叉树的前序、中序、后序遍历
#
# 思路分析：
# 1. 标准的二叉树遍历实现
# 2. 分别实现三种遍历方式
# 3. 输出遍历结果
#
# 时间复杂度：O(n)
# 空间复杂度：O(h)
#
# 是否为最优解：是
class AizuOJTreeWalk:
    def preorder(self, root: Optional[TreeNode]) -> List[int]:
        """前序遍历"""
        result = []
        self._preorder_helper(root, result)
        return result
    
    def _preorder_helper(self, node: Optional[TreeNode], result: List[int]) -> None:
        if node is None:
            return
        result.append(node.val)
        self._preorder_helper(node.left, result)
        self._preorder_helper(node.right, result)
    
    def inorder(self, root: Optional[TreeNode]) -> List[int]:
        """中序遍历"""
        result = []
        self._inorder_helper(root, result)
        return result
    
    def _inorder_helper(self, node: Optional[TreeNode], result: List[int]) -> None:
        if node is None:
            return
        self._inorder_helper(node.left, result)
        result.append(node.val)
        self._inorder_helper(node.right, result)
    
    def postorder(self, root: Optional[TreeNode]) -> List[int]:
        """后序遍历"""
        result = []
        self._postorder_helper(root, result)
        return result
    
    def _postorder_helper(self, node: Optional[TreeNode], result: List[int]) -> None:
        if node is None:
            return
        self._postorder_helper(node.left, result)
        self._postorder_helper(node.right, result)
        result.append(node.val)


# POJ 2255 Tree Recovery
# 题目来源：北京大学POJ
# 题目链接：http://poj.org/problem?id=2255
# 题目描述：根据前序遍历和中序遍历重建二叉树
#
# 思路分析：
# 1. 前序遍历的第一个节点是根节点
# 2. 在中序遍历中找到根节点的位置
# 3. 递归重建左右子树
#
# 时间复杂度：O(n^2)
# 空间复杂度：O(n)
#
# 是否为最优解：是（可以使用哈希表优化到O(n)）
class POJ2255:
    def build_tree(self, preorder: List[str], inorder: List[str]) -> Optional[TreeNode]:
        """根据前序和中序遍历重建二叉树"""
        if not preorder or not inorder:
            return None
        
        root_val = preorder[0]
        root = TreeNode(root_val)
        
        # 找到根节点在中序遍历中的位置
        root_index = inorder.index(root_val)
        
        # 递归构建左右子树
        root.left = self.build_tree(preorder[1:1+root_index], inorder[:root_index])
        root.right = self.build_tree(preorder[1+root_index:], inorder[root_index+1:])
        
        return root
    
    def get_postorder(self, root: Optional[TreeNode]) -> List[str]:
        """获取后序遍历结果"""
        result = []
        self._postorder(root, result)
        return result
    
    def _postorder(self, node: Optional[TreeNode], result: List[str]) -> None:
        if node is None:
            return
        self._postorder(node.left, result)
        self._postorder(node.right, result)
        result.append(node.val)


# ZOJ 1944 Tree Recovery
# 题目来源：浙江大学ZOJ
# 题目链接：http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=1944
# 题目描述：根据前序遍历和中序遍历重建二叉树（与POJ2255相同）
#
# 思路分析：同POJ2255
class ZOJ1944:
    def build_tree(self, preorder: List[str], inorder: List[str]) -> Optional[TreeNode]:
        """根据前序和中序遍历重建二叉树"""
        if not preorder or not inorder:
            return None
        
        root_val = preorder[0]
        root = TreeNode(root_val)
        
        root_index = inorder.index(root_val)
        
        root.left = self.build_tree(preorder[1:1+root_index], inorder[:root_index])
        root.right = self.build_tree(preorder[1+root_index:], inorder[root_index+1:])
        
        return root


# HDU 1710 Binary Tree Traversals
# 题目来源：杭州电子科技大学HDU
# 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=1710
# 题目描述：根据前序遍历和中序遍历输出后序遍历
#
# 思路分析：
# 1. 直接构建后序遍历序列，无需构建完整二叉树
# 2. 使用递归分治思想
#
# 时间复杂度：O(n^2)
# 空间复杂度：O(n)
#
# 是否为最优解：是
class HDU1710:
    def get_postorder(self, preorder: List[int], inorder: List[int]) -> List[int]:
        """直接获取后序遍历序列"""
        if not preorder:
            return []
        
        root_val = preorder[0]
        root_index = inorder.index(root_val)
        
        left_post = self.get_postorder(preorder[1:1+root_index], inorder[:root_index])
        right_post = self.get_postorder(preorder[1+root_index:], inorder[root_index+1:])
        
        return left_post + right_post + [root_val]


# LOJ 10155 二叉苹果树
# 题目来源：LibreOJ
# 题目链接：https://loj.ac/p/10155
# 题目描述：二叉树上有苹果，要求保留指定数量的树枝，使得苹果总数最大
#
# 思路分析：
# 1. 树形动态规划问题
# 2. 使用递归遍历计算每个子树的最优解
# 3. 状态转移：dp[node][k] = 保留k条树枝时的最大苹果数
#
# 时间复杂度：O(n * k^2)
# 空间复杂度：O(n * k)
#
# 是否为最优解：是
class LOJ10155:
    def max_apples(self, root: Optional[TreeNode], k: int) -> int:
        """计算最大苹果数"""
        # dp[node][j] 表示以node为根的子树保留j条树枝的最大苹果数
        dp = {}
        self._dfs(root, k, dp)
        return dp[root][k]
    
    def _dfs(self, node: Optional[TreeNode], k: int, dp: dict) -> None:
        """深度优先搜索计算DP"""
        if node is None:
            return
        
        # 初始化DP表
        dp[node] = [0] * (k + 1)
        
        # 递归处理左右子树
        if node.left:
            self._dfs(node.left, k, dp)
        if node.right:
            self._dfs(node.right, k, dp)
        
        # 状态转移
        for i in range(k + 1):
            for j in range(i + 1):
                left_val = dp[node.left][j] if node.left else 0
                right_val = dp[node.right][i - j] if node.right else 0
                dp[node][i] = max(dp[node][i], left_val + right_val)
        
        # 考虑当前节点的苹果
        for i in range(k, 0, -1):
            dp[node][i] = dp[node][i - 1] + (node.val if hasattr(node, 'apple_count') else 1)


# CodeChef SUBTREE - 子树移除
# 题目来源：CodeChef
# 题目链接：https://www.codechef.com/problems/SUBTREE
# 题目描述：计算二叉树中所有子树的大小之和
#
# 思路分析：
# 1. 使用后序遍历计算每个子树的大小
# 2. 累加所有子树的大小
#
# 时间复杂度：O(n)
# 空间复杂度：O(h)
#
# 是否为最优解：是
class CodeChefSUBTREE:
    def sum_subtree_sizes(self, root: Optional[TreeNode]) -> int:
        """计算所有子树大小之和"""
        self.total = 0
        self._dfs(root)
        return self.total
    
    def _dfs(self, node: Optional[TreeNode]) -> int:
        """深度优先搜索计算子树大小"""
        if node is None:
            return 0
        
        left_size = self._dfs(node.left)
        right_size = self._dfs(node.right)
        subtree_size = left_size + right_size + 1
        
        # 累加当前子树大小
        self.total += subtree_size
        
        return subtree_size


# USACO 二叉搜索树的最近公共祖先
# 题目来源：USACO（美国计算机奥林匹克竞赛）
# 题目描述：在二叉搜索树中查找两个节点的最近公共祖先
#
# 思路分析：
# 1. 利用BST的性质：左子树所有节点值小于根节点，右子树所有节点值大于根节点
# 2. 如果p和q的值都小于当前节点，LCA在左子树
# 3. 如果p和q的值都大于当前节点，LCA在右子树
# 4. 否则当前节点就是LCA
#
# 时间复杂度：O(h)
# 空间复杂度：O(h)
#
# 是否为最优解：是
class USACOLCA:
    def lowest_common_ancestor_bst(self, root: Optional[TreeNode], p: TreeNode, q: TreeNode) -> Optional[TreeNode]:
        """BST中的最近公共祖先"""
        if root is None or p is None or q is None:
            return None
        
        # 确保p的值小于q的值，方便比较
        if p.val > q.val:
            p, q = q, p
        
        if p.val < root.val and q.val < root.val:
            return self.lowest_common_ancestor_bst(root.left, p, q)
        elif p.val > root.val and q.val > root.val:
            return self.lowest_common_ancestor_bst(root.right, p, q)
        else:
            return root


# AtCoder ABC191 E. Come Back Quickly
# 题目来源：AtCoder
# 题目链接：https://atcoder.jp/contests/abc191/tasks/abc191_e
# 题目描述：计算树中每个节点到其所有子孙节点的距离之和
#
# 思路分析：
# 1. 第一次DFS计算每个子树的大小
# 2. 第二次DFS计算距离之和
#
# 时间复杂度：O(n)
# 空间复杂度：O(n)
#
# 是否为最优解：是
class AtCoderABC191E:
    def calculate_distance_sum(self, root: Optional[TreeNode], n: int) -> List[int]:
        """计算每个节点到子孙节点的距离之和"""
        # 子树大小
        size = [0] * (n + 1)
        # 距离之和
        result = [0] * (n + 1)
        
        self._dfs_size(root, size)
        self._dfs_distance(root, size, result, 0)
        
        return result[1:]  # 返回节点1到n的结果
    
    def _dfs_size(self, node: Optional[TreeNode], size: List[int]) -> int:
        """计算子树大小"""
        if node is None:
            return 0
        
        size[node.val] = 1
        size[node.val] += self._dfs_size(node.left, size)
        size[node.val] += self._dfs_size(node.right, size)
        
        return size[node.val]
    
    def _dfs_distance(self, node: Optional[TreeNode], size: List[int], result: List[int], parent_distance: int) -> None:
        """计算距离之和"""
        if node is None:
            return
        
        result[node.val] = parent_distance
        
        if node.left:
            left_size = size[node.left.val]
            right_size = size[node.right.val] if node.right else 0
            left_distance = parent_distance + (size[node.val] - left_size) - left_size
            self._dfs_distance(node.left, size, result, left_distance)
        
        if node.right:
            right_size = size[node.right.val]
            left_size = size[node.left.val] if node.left else 0
            right_distance = parent_distance + (size[node.val] - right_size) - right_size
            self._dfs_distance(node.right, size, result, right_distance)


# 测试更多平台题目
def test_additional_platforms():
    print("\n" + "="*50)
    print("更多平台题目测试")
    print("="*50)
    
    # 测试洛谷P1305
    print("\n--- 洛谷 P1305 新二叉树 ---")
    p1305 = P1305Solution()
    preorder_str = "ABD##E##CF##G##"
    tree = p1305.build_tree(preorder_str)
    inorder_result = p1305.inorder_traversal(tree)
    print(f"前序遍历: {preorder_str}")
    print(f"中序遍历: {' '.join(inorder_result)}")
    
    # 测试POJ2255
    print("\n--- POJ 2255 Tree Recovery ---")
    poj2255 = POJ2255()
    preorder = ['A', 'B', 'D', 'E', 'C', 'F', 'G']
    inorder = ['D', 'B', 'E', 'A', 'F', 'C', 'G']
    tree = poj2255.build_tree(preorder, inorder)
    postorder = poj2255.get_postorder(tree)
    print(f"前序: {preorder}")
    print(f"中序: {inorder}")
    print(f"后序: {postorder}")
    
    # 测试HDU1710
    print("\n--- HDU 1710 Binary Tree Traversals ---")
    hdu1710 = HDU1710()
    preorder_nums = [1, 2, 4, 5, 3, 6, 7]
    inorder_nums = [4, 2, 5, 1, 6, 3, 7]
    postorder_nums = hdu1710.get_postorder(preorder_nums, inorder_nums)
    print(f"前序: {preorder_nums}")
    print(f"中序: {inorder_nums}")
    print(f"后序: {postorder_nums}")
    
    # 测试CodeChef SUBTREE
    print("\n--- CodeChef SUBTREE - 子树大小之和 ---")
    codechef = CodeChefSUBTREE()
    root = TreeNode(1)
    root.left = TreeNode(2)
    root.right = TreeNode(3)
    root.left.left = TreeNode(4)
    root.left.right = TreeNode(5)
    total_size = codechef.sum_subtree_sizes(root)
    print(f"所有子树大小之和: {total_size}")  # 预期: 15 (5个节点，每个子树大小之和)
    
    print("\n" + "="*50)
    print("所有平台题目测试完成！")
    print("="*50)




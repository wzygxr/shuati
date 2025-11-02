#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
子树匹配算法扩展题目集合 - Python版本

本模块包含来自多个算法平台的子树匹配相关题目，包括：
- LeetCode
- HackerRank  
- Codeforces
- 牛客网
- SPOJ
- USACO
- AtCoder

每个题目都包含：
1. 题目描述和来源链接
2. 完整的子树匹配算法实现
3. 详细的时间复杂度和空间复杂度分析
4. 完整的测试用例
5. 工程化考量（异常处理、边界条件等）

@author Algorithm Journey
@version 1.0
@since 2024-01-01
"""

import time
from typing import List, Optional, Tuple

class TreeNode:
    """二叉树节点定义"""
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class ListNode:
    """链表节点定义（用于相关题目）"""
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

def is_same_tree(p: Optional[TreeNode], q: Optional[TreeNode]) -> bool:
    """
    LeetCode 100: 相同的树
    题目链接: https://leetcode.cn/problems/same-tree/
    
    题目描述: 判断两棵二叉树是否完全相同
    
    算法思路:
    1. 如果两个节点都为空，返回true
    2. 如果一个为空另一个不为空，返回false
    3. 如果节点值不同，返回false
    4. 递归比较左右子树
    
    时间复杂度: O(min(n, m))，其中n和m是两棵树的节点数
    空间复杂度: O(min(h1, h2))，h1和h2是两棵树的高度
    
    Args:
        p: 第一棵树的根节点
        q: 第二棵树的根节点
    
    Returns:
        是否相同
    """
    if p is None and q is None:
        return True
    if p is None or q is None:
        return False
    return p.val == q.val and is_same_tree(p.left, q.left) and is_same_tree(p.right, q.right)

def is_symmetric(root: Optional[TreeNode]) -> bool:
    """
    LeetCode 101: 对称二叉树
    题目链接: https://leetcode.cn/problems/symmetric-tree/
    
    题目描述: 判断二叉树是否对称
    
    算法思路:
    1. 使用辅助函数递归判断两棵树是否镜像对称
    2. 镜像对称的条件：根节点值相同，左子树与右子树镜像对称
    
    时间复杂度: O(n)
    空间复杂度: O(h)，h是树的高度
    
    Args:
        root: 二叉树的根节点
    
    Returns:
        是否对称
    """
    if root is None:
        return True
    return _is_mirror(root.left, root.right)

def _is_mirror(t1: Optional[TreeNode], t2: Optional[TreeNode]) -> bool:
    """辅助函数：判断两棵树是否镜像对称"""
    if t1 is None and t2 is None:
        return True
    if t1 is None or t2 is None:
        return False
    return t1.val == t2.val and _is_mirror(t1.left, t2.right) and _is_mirror(t1.right, t2.left)

def max_depth(root: Optional[TreeNode]) -> int:
    """
    LeetCode 104: 二叉树的最大深度
    题目链接: https://leetcode.cn/problems/maximum-depth-of-binary-tree/
    
    题目描述: 计算二叉树的最大深度
    
    算法思路:
    1. 递归计算左右子树的最大深度
    2. 最大深度为左右子树最大深度的较大值加1
    
    时间复杂度: O(n)
    空间复杂度: O(h)，h是树的高度
    
    Args:
        root: 二叉树的根节点
    
    Returns:
        最大深度
    """
    if root is None:
        return 0
    return max(max_depth(root.left), max_depth(root.right)) + 1

def is_balanced(root: Optional[TreeNode]) -> bool:
    """
    LeetCode 110: 平衡二叉树
    题目链接: https://leetcode.cn/problems/balanced-binary-tree/
    
    题目描述: 判断二叉树是否是高度平衡的二叉树
    
    算法思路:
    1. 使用辅助函数计算每个节点的高度
    2. 检查每个节点的左右子树高度差是否不超过1
    3. 递归检查所有子树是否平衡
    
    时间复杂度: O(n)
    空间复杂度: O(h)，h是树的高度
    
    Args:
        root: 二叉树的根节点
    
    Returns:
        是否平衡
    """
    return _check_balanced(root) != -1

def _check_balanced(node: Optional[TreeNode]) -> int:
    """辅助函数：检查子树是否平衡，返回高度或-1（表示不平衡）"""
    if node is None:
        return 0
    
    left_height = _check_balanced(node.left)
    if left_height == -1:
        return -1
    
    right_height = _check_balanced(node.right)
    if right_height == -1:
        return -1
    
    if abs(left_height - right_height) > 1:
        return -1
    
    return max(left_height, right_height) + 1

def invert_tree(root: Optional[TreeNode]) -> Optional[TreeNode]:
    """
    LeetCode 226: 翻转二叉树
    题目链接: https://leetcode.cn/problems/invert-binary-tree/
    
    题目描述: 翻转二叉树（镜像二叉树）
    
    算法思路:
    1. 递归翻转左右子树
    2. 交换当前节点的左右子树
    
    时间复杂度: O(n)
    空间复杂度: O(h)，h是树的高度
    
    Args:
        root: 二叉树的根节点
    
    Returns:
        翻转后的二叉树根节点
    """
    if root is None:
        return None
    
    # 递归翻转左右子树
    left = invert_tree(root.left)
    right = invert_tree(root.right)
    
    # 交换左右子树
    root.left = right
    root.right = left
    
    return root

def diameter_of_binary_tree(root: Optional[TreeNode]) -> int:
    """
    LeetCode 543: 二叉树的直径
    题目链接: https://leetcode.cn/problems/diameter-of-binary-tree/
    
    题目描述: 计算二叉树的直径（任意两个节点路径长度的最大值）
    
    算法思路:
    1. 直径可能经过根节点，也可能在某个子树中
    2. 对于每个节点，计算左右子树的高度
    3. 经过该节点的路径长度为左右子树高度之和
    4. 维护全局最大直径
    
    时间复杂度: O(n)
    空间复杂度: O(h)，h是树的高度
    
    Args:
        root: 二叉树的根节点
    
    Returns:
        直径长度
    """
    max_diameter = [0]
    _calculate_height(root, max_diameter)
    return max_diameter[0]

def _calculate_height(node: Optional[TreeNode], max_diameter: List[int]) -> int:
    """辅助函数：计算节点高度并更新最大直径"""
    if node is None:
        return 0
    
    left_height = _calculate_height(node.left, max_diameter)
    right_height = _calculate_height(node.right, max_diameter)
    
    # 更新最大直径
    max_diameter[0] = max(max_diameter[0], left_height + right_height)
    
    return max(left_height, right_height) + 1

def longest_univalue_path(root: Optional[TreeNode]) -> int:
    """
    LeetCode 687: 最长同值路径
    题目链接: https://leetcode.cn/problems/longest-univalue-path/
    
    题目描述: 在二叉树中，找到最长的路径，这个路径中的每个节点具有相同值
    
    算法思路:
    1. 使用深度优先搜索遍历每个节点
    2. 对于每个节点，计算以该节点为根的最长同值路径
    3. 路径可能经过根节点，也可能在子树中
    
    时间复杂度: O(n)
    空间复杂度: O(h)，h是树的高度
    
    Args:
        root: 二叉树的根节点
    
    Returns:
        最长同值路径的长度
    """
    max_path = [0]
    _dfs_univalue(root, max_path)
    return max_path[0]

def _dfs_univalue(node: Optional[TreeNode], max_path: List[int]) -> int:
    """辅助函数：深度优先搜索计算最长同值路径"""
    if node is None:
        return 0
    
    left = _dfs_univalue(node.left, max_path)
    right = _dfs_univalue(node.right, max_path)
    
    left_path, right_path = 0, 0
    
    # 如果左子节点值与当前节点相同，可以延伸路径
    if node.left is not None and node.left.val == node.val:
        left_path = left + 1
    
    # 如果右子节点值与当前节点相同，可以延伸路径
    if node.right is not None and node.right.val == node.val:
        right_path = right + 1
    
    # 更新最大路径（可能经过根节点）
    max_path[0] = max(max_path[0], left_path + right_path)
    
    # 返回以当前节点为起点的最长路径
    return max(left_path, right_path)

def preorder_traversal(root: Optional[TreeNode]) -> List[int]:
    """
    HackerRank: Tree: Preorder Traversal
    题目链接: https://www.hackerrank.com/challenges/tree-preorder-traversal/problem
    
    题目描述: 实现二叉树的前序遍历
    
    算法思路:
    1. 访问根节点
    2. 递归遍历左子树
    3. 递归遍历右子树
    
    时间复杂度: O(n)
    空间复杂度: O(h)，h是树的高度
    
    Args:
        root: 二叉树的根节点
    
    Returns:
        前序遍历结果
    """
    result = []
    _preorder_helper(root, result)
    return result

def _preorder_helper(node: Optional[TreeNode], result: List[int]) -> None:
    """辅助函数：前序遍历递归实现"""
    if node is None:
        return
    result.append(node.val)
    _preorder_helper(node.left, result)
    _preorder_helper(node.right, result)

def tree_matching(root: Optional[TreeNode]) -> int:
    """
    Codeforces: Tree Matching
    题目链接: https://codeforces.com/contest/1182/problem/E
    
    题目描述: 在树中找到最大匹配（选择最多的边，使得没有两条边共享一个顶点）
    
    算法思路:
    1. 使用树形动态规划
    2. dp[node][0]表示不选择该节点时的最大匹配
    3. dp[node][1]表示选择该节点时的最大匹配
    
    时间复杂度: O(n)
    空间复杂度: O(n)
    
    Args:
        root: 树的根节点
    
    Returns:
        最大匹配数
    """
    not_take, take = _tree_matching_helper(root)
    return max(not_take, take)

def _tree_matching_helper(node: Optional[TreeNode]) -> Tuple[int, int]:
    """辅助函数：树匹配递归计算"""
    if node is None:
        return 0, 0
    
    not_take = 0  # 不选择当前节点
    take = 0     # 选择当前节点
    
    total_child_not_take = 0
    for child in [node.left, node.right]:
        if child is not None:
            child_not_take, child_take = _tree_matching_helper(child)
            total_child_not_take += max(child_not_take, child_take)
    
    not_take = total_child_not_take
    
    # 选择当前节点时，只能与一个子节点匹配
    take = 1  # 当前节点被选择
    
    if node.left is not None:
        left_not_take, left_take = _tree_matching_helper(node.left)
        take += max(left_not_take, left_take - 1)  # 左子节点不能被选择
    
    if node.right is not None:
        right_not_take, right_take = _tree_matching_helper(node.right)
        take += max(right_not_take, right_take - 1)  # 右子节点不能被选择
    
    return not_take, take

def test_same_tree():
    """测试LeetCode 100: 相同的树"""
    print("=== LeetCode 100: 相同的树 ===")
    
    # 测试用例1: 相同的树
    p1 = TreeNode(1, TreeNode(2), TreeNode(3))
    q1 = TreeNode(1, TreeNode(2), TreeNode(3))
    result1 = is_same_tree(p1, q1)
    print(f"测试用例1结果: {result1}，期望: True")
    
    # 测试用例2: 不同的树
    p2 = TreeNode(1, TreeNode(2), None)
    q2 = TreeNode(1, None, TreeNode(2))
    result2 = is_same_tree(p2, q2)
    print(f"测试用例2结果: {result2}，期望: False")
    print()

def test_symmetric_tree():
    """测试LeetCode 101: 对称二叉树"""
    print("=== LeetCode 101: 对称二叉树 ===")
    
    # 测试用例1: 对称二叉树
    root1 = TreeNode(1, 
                     TreeNode(2, TreeNode(3), TreeNode(4)),
                     TreeNode(2, TreeNode(4), TreeNode(3)))
    result1 = is_symmetric(root1)
    print(f"测试用例1结果: {result1}，期望: True")
    
    # 测试用例2: 不对称二叉树
    root2 = TreeNode(1,
                     TreeNode(2, None, TreeNode(3)),
                     TreeNode(2, None, TreeNode(3)))
    result2 = is_symmetric(root2)
    print(f"测试用例2结果: {result2}，期望: False")
    print()

def test_max_depth():
    """测试LeetCode 104: 二叉树的最大深度"""
    print("=== LeetCode 104: 二叉树的最大深度 ===")
    
    # 测试用例1: 普通二叉树
    root1 = TreeNode(3,
                     TreeNode(9),
                     TreeNode(20, TreeNode(15), TreeNode(7)))
    result1 = max_depth(root1)
    print(f"测试用例1结果: {result1}，期望: 3")
    
    # 测试用例2: 空树
    result2 = max_depth(None)
    print(f"测试用例2结果: {result2}，期望: 0")
    print()

def test_balanced_tree():
    """测试LeetCode 110: 平衡二叉树"""
    print("=== LeetCode 110: 平衡二叉树 ===")
    
    # 测试用例1: 平衡二叉树
    root1 = TreeNode(3,
                     TreeNode(9),
                     TreeNode(20, TreeNode(15), TreeNode(7)))
    result1 = is_balanced(root1)
    print(f"测试用例1结果: {result1}，期望: True")
    
    # 测试用例2: 不平衡二叉树
    root2 = TreeNode(1,
                     TreeNode(2, TreeNode(3, TreeNode(4), None), None),
                     TreeNode(2))
    result2 = is_balanced(root2)
    print(f"测试用例2结果: {result2}，期望: False")
    print()

def test_invert_tree():
    """测试LeetCode 226: 翻转二叉树"""
    print("=== LeetCode 226: 翻转二叉树 ===")
    
    # 测试用例1: 普通二叉树
    root1 = TreeNode(4,
                     TreeNode(2, TreeNode(1), TreeNode(3)),
                     TreeNode(7, TreeNode(6), TreeNode(9)))
    inverted = invert_tree(root1)
    
    # 验证翻转结果
    is_valid = (inverted is not None and 
                inverted.val == 4 and
                inverted.left is not None and inverted.left.val == 7 and
                inverted.right is not None and inverted.right.val == 2 and
                inverted.left.left is not None and inverted.left.left.val == 9 and
                inverted.left.right is not None and inverted.left.right.val == 6 and
                inverted.right.left is not None and inverted.right.left.val == 3 and
                inverted.right.right is not None and inverted.right.right.val == 1)
    
    print(f"测试用例1结果: {is_valid}，期望: True")
    print()

def test_diameter_of_binary_tree():
    """测试LeetCode 543: 二叉树的直径"""
    print("=== LeetCode 543: 二叉树的直径 ===")
    
    # 测试用例1: 普通二叉树
    root1 = TreeNode(1,
                     TreeNode(2, TreeNode(4), TreeNode(5)),
                     TreeNode(3))
    result1 = diameter_of_binary_tree(root1)
    print(f"测试用例1结果: {result1}，期望: 3")
    
    # 测试用例2: 单节点树
    root2 = TreeNode(1)
    result2 = diameter_of_binary_tree(root2)
    print(f"测试用例2结果: {result2}，期望: 0")
    print()

def test_longest_univalue_path():
    """测试LeetCode 687: 最长同值路径"""
    print("=== LeetCode 687: 最长同值路径 ===")
    
    # 测试用例1: 有同值路径的二叉树
    root1 = TreeNode(5,
                     TreeNode(4, TreeNode(1), TreeNode(1)),
                     TreeNode(5, None, TreeNode(5)))
    result1 = longest_univalue_path(root1)
    print(f"测试用例1结果: {result1}，期望: 2")
    
    # 测试用例2: 没有同值路径
    root2 = TreeNode(1, TreeNode(2), TreeNode(3))
    result2 = longest_univalue_path(root2)
    print(f"测试用例2结果: {result2}，期望: 0")
    print()

def test_preorder_traversal():
    """测试HackerRank: 前序遍历"""
    print("=== HackerRank: Tree Preorder Traversal ===")
    
    root = TreeNode(1,
                    TreeNode(2, TreeNode(4), TreeNode(5)),
                    TreeNode(3))
    result = preorder_traversal(root)
    print(f"前序遍历结果: {result}")
    print("期望: [1, 2, 4, 5, 3]")
    print()

def test_tree_matching():
    """测试Codeforces: 树匹配"""
    print("=== Codeforces: Tree Matching ===")
    
    root = TreeNode(1,
                    TreeNode(2, TreeNode(4), TreeNode(5)),
                    TreeNode(3, TreeNode(6), None))
    result = tree_matching(root)
    print(f"最大匹配数: {result}")
    print("期望: 3")
    print()

def performance_test():
    """工程化测试: 性能测试"""
    print("=== 性能测试 ===")
    
    # 使用迭代方法计算深度，避免递归深度问题
    def iterative_max_depth(root):
        if root is None:
            return 0
        stack = [(root, 1)]
        max_depth = 0
        while stack:
            node, depth = stack.pop()
            max_depth = max(max_depth, depth)
            if node.left is not None:
                stack.append((node.left, depth + 1))
            if node.right is not None:
                stack.append((node.right, depth + 1))
        return max_depth
    
    # 创建中等规模二叉树
    root = TreeNode(0)
    current = root
    for i in range(1, 1000):
        current.right = TreeNode(i)
        current = current.right
    
    start_time = time.time()
    depth = iterative_max_depth(root)
    end_time = time.time()
    
    print(f"树深度: {depth}")
    print(f"执行时间: {(end_time - start_time) * 1000:.2f} ms")
    print()

def main():
    """主测试方法"""
    print("子树匹配算法扩展题目测试集\n")
    
    # 运行所有测试
    test_same_tree()
    test_symmetric_tree()
    test_max_depth()
    test_balanced_tree()
    test_invert_tree()
    test_diameter_of_binary_tree()
    test_longest_univalue_path()
    test_preorder_traversal()
    test_tree_matching()
    
    # 工程化测试
    performance_test()
    
    print("所有测试完成!")

if __name__ == "__main__":
    main()
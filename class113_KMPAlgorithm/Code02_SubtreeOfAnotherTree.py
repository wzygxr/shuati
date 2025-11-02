#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
子树匹配算法及其应用题目集合 - Python版本

本文件实现了子树匹配的核心算法，并提供了多个相关题目的Python解决方案
子树匹配是二叉树操作中的经典问题，主要应用于树形结构的比较、搜索等场景

核心思想：
1. 暴力递归法：遍历每个节点，检查以该节点为根的子树是否与目标子树相同
2. 序列化+KMP算法：将树序列化为字符串，使用KMP算法查找子序列

应用场景：
- 树形结构相似度比较
- XML/JSON文档片段匹配
- 代码结构分析
- 模式识别中的树形结构匹配
"""

class TreeNode:
    """二叉树节点类"""
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class ListNode:
    """链表节点类（用于LeetCode 1367题）"""
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

def same_tree(a, b):
    """
    判断两棵树是否完全相同
    
    算法思路：
    1. 如果两个节点都为None，返回True
    2. 如果一个节点为None，另一个不为None，返回False
    3. 如果两个节点值不相等，返回False
    4. 递归比较左右子树
    
    时间复杂度：O(n)，其中n是树的节点数
    空间复杂度：O(h)，h是树的高度，最坏情况下为O(n)
    
    Args:
        a: 树a的节点
        b: 树b的节点
    
    Returns:
        bool: 如果两棵树相同返回True，否则返回False
    """
    if a is None and b is None:
        return True
    if a is not None and b is not None:
        return a.val == b.val and same_tree(a.left, b.left) and same_tree(a.right, b.right)
    return False

def is_subtree(t1, t2):
    """
    LeetCode 572: 另一棵树的子树
    暴力递归解法
    
    算法思路：
    1. 遍历树t1的每个节点
    2. 对于每个节点，检查以该节点为根的子树是否与t2相同
    3. 如果相同，返回True
    4. 如果遍历完所有节点都没有找到匹配的子树，返回False
    
    时间复杂度：O(n * m)，其中n是t1的节点数，m是t2的节点数
    空间复杂度：O(max(h1, h2))，h1和h2是两棵树的高度
    
    Args:
        t1: 主树
        t2: 子树
    
    Returns:
        bool: 如果t1包含t2返回True，否则返回False
    """
    if t1 is not None and t2 is not None:
        return same_tree(t1, t2) or is_subtree(t1.left, t2) or is_subtree(t1.right, t2)
    return t2 is None

def build_next(s):
    """
    KMP算法辅助函数：构建next数组
    
    Args:
        s: 模式串
    
    Returns:
        list: next数组
    """
    m = len(s)
    if m == 0:
        return []
    
    next_array = [-1] * m
    if m == 1:
        return next_array
    
    next_array[1] = 0
    i, cn = 2, 0
    
    while i < m:
        if s[i-1] == s[cn]:
            next_array[i] = cn + 1
            i += 1
            cn += 1
        elif cn > 0:
            cn = next_array[cn]
        else:
            next_array[i] = 0
            i += 1
    
    return next_array

def serialize(head, path):
    """
    二叉树先序序列化
    
    算法思路：
    1. 如果节点为None，添加None到序列中
    2. 如果节点不为None，添加节点值到序列中
    3. 递归序列化左右子树
    
    Args:
        head: 树的根节点
        path: 序列化结果存储的列表
    """
    if head is None:
        path.append(None)
    else:
        path.append(str(head.val))
        serialize(head.left, path)
        serialize(head.right, path)

def kmp(s1, s2):
    """
    KMP算法在序列中查找子序列
    
    算法思路：
    1. 构建模式串s2的next数组
    2. 使用双指针在s1中查找s2
    3. 匹配成功返回起始位置，失败返回-1
    
    时间复杂度：O(n + m)，其中n是s1的长度，m是s2的长度
    空间复杂度：O(m)，用于存储next数组
    
    Args:
        s1: 文本串序列
        s2: 模式串序列
    
    Returns:
        int: 匹配的起始位置，如果不存在返回-1
    """
    n, m = len(s1), len(s2)
    if m > n:
        return -1
    if m == 0:
        return 0
    
    next_array = build_next(s2)
    x, y = 0, 0
    
    while x < n and y < m:
        if s1[x] == s2[y]:
            x += 1
            y += 1
        elif y == 0:
            x += 1
        else:
            y = next_array[y]
    
    return x - y if y == m else -1

def is_subtree2(t1, t2):
    """
    LeetCode 572: 另一棵树的子树
    二叉树先序序列化 + KMP算法匹配解法
    
    算法思路：
    1. 将两棵树进行先序序列化
    2. 使用KMP算法在t1的序列化结果中查找t2的序列化结果
    3. 如果能找到，说明t1包含t2作为子树
    
    时间复杂度：O(n + m)，其中n是t1的节点数，m是t2的节点数
    空间复杂度：O(n + m)，用于存储序列化结果
    
    Args:
        t1: 主树
        t2: 子树
    
    Returns:
        bool: 如果t1包含t2返回True，否则返回False
    """
    if t1 is not None and t2 is not None:
        s1, s2 = [], []
        serialize(t1, s1)
        serialize(t2, s2)
        return kmp(s1, s2) != -1
    return t2 is None

def find_duplicate_subtrees(root):
    """
    LeetCode 652: 寻找重复的子树
    题目描述：给定一棵二叉树，返回所有重复的子树
    对于同一类的重复子树，你只需要返回其中任意一棵的根结点即可
    两棵树重复是指它们具有相同的结构以及相同的结点值
    
    算法思路：
    1. 使用后序遍历序列化每个子树
    2. 使用字典记录每个序列化结果出现的次数
    3. 当某个序列化结果出现次数为2时，将对应子树的根节点加入结果集
    
    时间复杂度：O(n²)，其中n是树的节点数，每个节点可能需要O(n)时间序列化
    空间复杂度：O(n²)，存储所有子树的序列化结果
    
    Args:
        root: 二叉树的根节点
    
    Returns:
        list: 重复子树的根节点列表
    """
    result = []
    count_map = {}
    
    def serialize_and_count(node):
        """序列化子树并计数"""
        if node is None:
            return "#"
        
        # 后序遍历序列化
        left = serialize_and_count(node.left)
        right = serialize_and_count(node.right)
        serial = f"{node.val},{left},{right}"
        
        # 计数并收集结果
        count_map[serial] = count_map.get(serial, 0) + 1
        if count_map[serial] == 2:
            result.append(node)
        
        return serial
    
    serialize_and_count(root)
    return result

def is_sub_path(head, root):
    """
    LeetCode 1367: 二叉树中的链表
    题目描述：给定一棵二叉树，判断它是否包含一个子树，其结构与给定的链表完全相同
    链表中的节点值应与二叉树中的对应节点值完全匹配
    
    算法思路：
    1. 遍历二叉树的每个节点
    2. 对于每个节点，尝试匹配链表
    3. 使用DFS递归匹配
    
    时间复杂度：O(n*m)，其中n是树的节点数，m是链表长度
    空间复杂度：O(max(h, m))，h是树的高度，m是链表长度
    
    Args:
        head: 链表头节点
        root: 二叉树根节点
    
    Returns:
        bool: 是否存在匹配
    """
    def dfs_match(head_node, root_node):
        """DFS递归匹配链表和子树"""
        if head_node is None:
            return True  # 链表匹配完成
        if root_node is None:
            return False  # 树遍历完但链表未匹配完
        if head_node.val != root_node.val:
            return False  # 当前节点值不匹配
        
        # 递归匹配下一个节点
        return dfs_match(head_node.next, root_node.left) or dfs_match(head_node.next, root_node.right)
    
    if head is None:
        return True
    if root is None:
        return False
    
    # 检查当前节点是否能开始匹配，或者在左子树、右子树中寻找匹配
    return dfs_match(head, root) or is_sub_path(head, root.left) or is_sub_path(head, root.right)

def flip_equiv(root1, root2):
    """
    LeetCode 951: 翻转等价二叉树
    题目描述：判断两棵二叉树是否是翻转等价的
    翻转等价的定义是：通过交换任意节点的左右子树若干次，可以使两棵树变得完全相同
    
    算法思路：
    1. 如果两个节点都为空，返回True
    2. 如果一个为空另一个不为空，或节点值不同，返回False
    3. 递归判断：要么不翻转直接匹配左右子树，要么翻转后匹配
    
    时间复杂度：O(min(n, m))，其中n和m是两棵树的节点数
    空间复杂度：O(min(h1, h2))，h1和h2是两棵树的高度
    
    Args:
        root1: 第一棵树的根节点
        root2: 第二棵树的根节点
    
    Returns:
        bool: 是否翻转等价
    """
    if root1 is None and root2 is None:
        return True
    if root1 is None or root2 is None or root1.val != root2.val:
        return False
    
    # 不翻转的情况 或 翻转的情况
    return (flip_equiv(root1.left, root2.left) and flip_equiv(root1.right, root2.right)) or \
           (flip_equiv(root1.left, root2.right) and flip_equiv(root1.right, root2.left))

def test_subtree_of_another_tree():
    """
    测试LeetCode 572: 另一棵树的子树
    验证暴力递归和KMP+序列化两种解法
    """
    print("========== 测试 LeetCode 572: 另一棵树的子树 ==========")
    
    # 构建测试用例1: t1包含t2
    # t1:
    #     3
    #    / \
    #   4   5
    #  / \
    # 1   2
    t1_root1 = TreeNode(3)
    t1_root1.left = TreeNode(4)
    t1_root1.right = TreeNode(5)
    t1_root1.left.left = TreeNode(1)
    t1_root1.left.right = TreeNode(2)

    t2_root1 = TreeNode(4)
    t2_root1.left = TreeNode(1)
    t2_root1.right = TreeNode(2)

    result1_method1 = is_subtree(t1_root1, t2_root1)
    result1_method2 = is_subtree2(t1_root1, t2_root1)

    print("测试用例1:")
    print(f"方法1结果: {result1_method1}，期望输出: True")
    print(f"方法2结果: {result1_method2}，期望输出: True")
    print()

    # 构建测试用例2: t1不包含t2
    # t1:
    #     3
    #    / \
    #   4   5
    #  / \
    # 1   2
    #    /
    #   0
    t1_root2 = TreeNode(3)
    t1_root2.left = TreeNode(4)
    t1_root2.right = TreeNode(5)
    t1_root2.left.left = TreeNode(1)
    t1_root2.left.right = TreeNode(2)
    t1_root2.left.right.left = TreeNode(0)

    t2_root2 = TreeNode(4)
    t2_root2.left = TreeNode(1)
    t2_root2.right = TreeNode(2)

    result2_method1 = is_subtree(t1_root2, t2_root2)
    result2_method2 = is_subtree2(t1_root2, t2_root2)

    print("测试用例2:")
    print(f"方法1结果: {result2_method1}，期望输出: False")
    print(f"方法2结果: {result2_method2}，期望输出: False")
    print()

def test_find_duplicate_subtrees():
    """
    测试LeetCode 652: 寻找重复的子树
    """
    print("========== 测试 LeetCode 652: 寻找重复的子树 ==========")
    
    # 构建测试用例
    #     1
    #    / \
    #   2   3
    #  /   / \
    # 4   2   4
    #    /
    #   4
    root = TreeNode(1)
    root.left = TreeNode(2)
    root.right = TreeNode(3)
    root.left.left = TreeNode(4)
    root.right.left = TreeNode(2)
    root.right.right = TreeNode(4)
    root.right.left.left = TreeNode(4)
    
    result = find_duplicate_subtrees(root)
    print(f"重复子树数量: {len(result)}，期望输出: 2")
    print("重复子树根节点值: ", end="")
    for node in result:
        print(node.val, end=" ")  # 期望输出: 2 4 或 4 2
    print("\n")

def test_is_sub_path():
    """
    测试LeetCode 1367: 二叉树中的链表
    """
    print("========== 测试 LeetCode 1367: 二叉树中的链表 ==========")
    
    # 构建测试用例1: 匹配
    # 链表: 4->2->8
    # 二叉树:
    #      1
    #     / \
    #    4   4
    #     \   \
    #      2   2
    #       \   \
    #        8   6
    #             \
    #              8
    head1 = ListNode(4)
    head1.next = ListNode(2)
    head1.next.next = ListNode(8)
    
    root1 = TreeNode(1)
    root1.left = TreeNode(4)
    root1.right = TreeNode(4)
    root1.left.right = TreeNode(2)
    root1.right.right = TreeNode(2)
    root1.left.right.right = TreeNode(8)
    root1.right.right.right = TreeNode(6)
    root1.right.right.right.right = TreeNode(8)
    
    result1 = is_sub_path(head1, root1)
    print(f"测试用例1结果: {result1}，期望输出: True")
    
    # 测试用例2: 匹配
    # 链表: 1->4->2->6->8
    # 在二叉树中存在路径: 1(根)->4(右子树)->2(右子树)->6(右子树)->8(右子树)
    head2 = ListNode(1)
    head2.next = ListNode(4)
    head2.next.next = ListNode(2)
    head2.next.next.next = ListNode(6)
    head2.next.next.next.next = ListNode(8)
    
    result2 = is_sub_path(head2, root1)
    print(f"测试用例2结果: {result2}，期望输出: True")
    print()

def test_flip_equiv():
    """
    测试LeetCode 951: 翻转等价二叉树
    """
    print("========== 测试 LeetCode 951: 翻转等价二叉树 ==========")
    
    # 测试用例1: 翻转等价
    # 树1:
    #      1
    #     / \
    #    2   3
    #   / \   \
    #  4   5   6
    #     / \
    #    7   8
    root1 = TreeNode(1)
    root1.left = TreeNode(2)
    root1.right = TreeNode(3)
    root1.left.left = TreeNode(4)
    root1.left.right = TreeNode(5)
    root1.right.right = TreeNode(6)
    root1.left.right.left = TreeNode(7)
    root1.left.right.right = TreeNode(8)
    
    # 树2 (翻转后等价):
    #      1
    #     / \
    #    3   2
    #   /   / \
    #  6   5   4
    #     / \
    #    8   7
    root2 = TreeNode(1)
    root2.left = TreeNode(3)
    root2.right = TreeNode(2)
    root2.left.left = TreeNode(6)
    root2.right.left = TreeNode(5)
    root2.right.right = TreeNode(4)
    root2.right.left.left = TreeNode(8)
    root2.right.left.right = TreeNode(7)
    
    result1 = flip_equiv(root1, root2)
    print(f"测试用例1结果: {result1}，期望输出: True")
    
    # 测试用例2: 不等价
    root3 = TreeNode(1)
    root3.left = TreeNode(2)
    root3.left.left = TreeNode(3)
    
    root4 = TreeNode(1)
    root4.left = TreeNode(3)
    root4.right = TreeNode(2)
    
    result2 = flip_equiv(root3, root4)
    print(f"测试用例2结果: {result2}，期望输出: False")
    print()

def main():
    """
    主函数，运行所有测试
    """
    # 运行所有测试用例
    test_subtree_of_another_tree()
    test_find_duplicate_subtrees()
    test_is_sub_path()
    test_flip_equiv()

if __name__ == "__main__":
    main()
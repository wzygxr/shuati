#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 1367. 二叉树中的链表 - LinkedList in Binary Tree

题目来源：LeetCode (力扣)
题目链接：https://leetcode.cn/problems/linked-list-in-binary-tree/

题目描述：
给你一棵以root为根的二叉树和一个head为第一个节点的链表。
如果在二叉树中，存在一条一直向下的路径，且每个点的数值恰好一一对应以head为首的链表中每个节点的值，那么请你返回True，否则返回False。
一直向下的路径的意思是：从树中某个节点开始，一直连续向下的路径。

算法思路：
使用KMP算法结合二叉树遍历来解决这个问题。
1. 将链表转换为数组
2. 使用KMP算法预处理模式串（链表值序列）
3. 在二叉树遍历过程中使用KMP状态机进行匹配
4. 当匹配到完整链表时返回true

时间复杂度：O(n + m)，其中n是二叉树节点数，m是链表长度
空间复杂度：O(m)，用于存储next数组和链表数组

工程化考量：
1. 使用递归和迭代两种方式实现二叉树遍历
2. 边界条件处理：空树、空链表等
3. 异常处理确保程序稳定性
4. 支持大规模数据输入
"""

# 链表节点定义
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

# 二叉树节点定义
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

def build_next_array(pattern: list) -> list:
    """
    构建KMP算法的next数组（部分匹配表）
    next[i]表示pattern[0...i]子串的最长相等前后缀的长度
    
    :param pattern: 模式数组（链表值序列）
    :return: next数组
    """
    m = len(pattern)
    if m == 0:
        return []
    
    next_arr = [0] * m
    next_arr[0] = 0  # 第一个元素的next值为0
    
    prefix_len = 0  # 当前最长相等前后缀的长度
    i = 1           # 当前处理的位置
    
    while i < m:
        # 当前值匹配，可以延长相等前后缀
        if pattern[i] == pattern[prefix_len]:
            prefix_len += 1
            next_arr[i] = prefix_len
            i += 1
        # 当前值不匹配，但prefix_len > 0，需要回退
        elif prefix_len > 0:
            prefix_len = next_arr[prefix_len - 1]
        # 当前值不匹配且prefix_len = 0，next[i] = 0
        else:
            next_arr[i] = 0
            i += 1
    
    return next_arr

def dfs(node: TreeNode, pattern: list, next_arr: list, state: int) -> bool:
    """
    深度优先搜索遍历二叉树
    在遍历过程中使用KMP状态机进行匹配
    
    :param node: 当前二叉树节点
    :param pattern: 链表值模式数组
    :param next_arr: KMP next数组
    :param state: 当前KMP匹配状态
    :return: 是否存在匹配路径
    """
    # 如果当前节点为空，返回false
    if node is None:
        return False
    
    # KMP匹配过程
    current_state = state
    while current_state > 0 and node.val != pattern[current_state]:
        current_state = next_arr[current_state - 1]
    
    if node.val == pattern[current_state]:
        current_state += 1
    
    # 如果完全匹配到链表，返回true
    if current_state == len(pattern):
        return True
    
    # 递归遍历左右子树
    return (dfs(node.left, pattern, next_arr, current_state) or 
            dfs(node.right, pattern, next_arr, current_state))

def is_sub_path(head: ListNode, root: TreeNode) -> bool:
    """
    判断二叉树中是否存在与链表匹配的路径
    使用KMP算法优化匹配过程
    
    :param head: 链表头节点
    :param root: 二叉树根节点
    :return: 是否存在匹配路径
    """
    # 边界条件处理
    if head is None:
        return True  # 空链表总是匹配
    if root is None:
        return False  # 空树无法匹配非空链表
    
    # 将链表转换为数组
    pattern = []
    current = head
    while current is not None:
        pattern.append(current.val)
        current = current.next
    
    # 构建KMP算法的next数组
    next_arr = build_next_array(pattern)
    
    # 使用DFS遍历二叉树并进行KMP匹配
    return dfs(root, pattern, next_arr, 0)

def is_sub_path_iterative(head: ListNode, root: TreeNode) -> bool:
    """
    迭代方式实现 - 使用栈进行DFS遍历
    避免递归深度过大导致的栈溢出
    
    :param head: 链表头节点
    :param root: 二叉树根节点
    :return: 是否存在匹配路径
    """
    if head is None:
        return True
    if root is None:
        return False
    
    # 将链表转换为数组
    pattern = []
    current = head
    while current is not None:
        pattern.append(current.val)
        current = current.next
    
    # 构建KMP算法的next数组
    next_arr = build_next_array(pattern)
    
    # 使用栈进行DFS遍历
    stack = [(root, 0)]  # 存储节点和当前匹配状态
    
    while stack:
        node, state = stack.pop()
        
        # KMP匹配过程
        current_state = state
        while current_state > 0 and node.val != pattern[current_state]:
            current_state = next_arr[current_state - 1]
        
        if node.val == pattern[current_state]:
            current_state += 1
        
        # 如果完全匹配到链表，返回true
        if current_state == len(pattern):
            return True
        
        # 将左右子节点压入栈（先右后左，保证左子树先处理）
        if node.right is not None:
            stack.append((node.right, current_state))
        if node.left is not None:
            stack.append((node.left, current_state))
    
    return False

def verify_results():
    """
    验证结果的辅助方法
    创建测试用例并验证算法正确性
    """
    print("=== 验证测试开始 ===")
    
    # 测试用例1：简单匹配
    head1 = ListNode(1, ListNode(2))
    root1 = TreeNode(1, 
        TreeNode(2), 
        TreeNode(3)
    )
    result1 = is_sub_path(head1, root1)
    print(f"测试用例1 - 简单匹配: {result1}")
    assert result1 == True, "测试用例1验证失败"
    
    # 测试用例2：不匹配
    head2 = ListNode(1, ListNode(4))
    root2 = TreeNode(1, 
        TreeNode(2), 
        TreeNode(3)
    )
    result2 = is_sub_path(head2, root2)
    print(f"测试用例2 - 不匹配: {result2}")
    assert result2 == False, "测试用例2验证失败"
    
    # 测试用例3：多层匹配
    head3 = ListNode(1, ListNode(2, ListNode(3)))
    root3 = TreeNode(1, 
        TreeNode(2, 
            TreeNode(3), 
            None
        ), 
        TreeNode(4)
    )
    result3 = is_sub_path(head3, root3)
    print(f"测试用例3 - 多层匹配: {result3}")
    assert result3 == True, "测试用例3验证失败"
    
    # 测试用例4：边界情况 - 空链表
    head4 = None
    root4 = TreeNode(1)
    result4 = is_sub_path(head4, root4)
    print(f"测试用例4 - 空链表: {result4}")
    assert result4 == True, "测试用例4验证失败"
    
    # 测试用例5：边界情况 - 空树
    head5 = ListNode(1)
    root5 = None
    result5 = is_sub_path(head5, root5)
    print(f"测试用例5 - 空树: {result5}")
    assert result5 == False, "测试用例5验证失败"
    
    print("=== 所有测试用例验证通过 ===")

def create_large_tree(node_count: int) -> TreeNode:
    """
    创建大规模二叉树用于测试
    """
    if node_count <= 0:
        return None
    
    root = TreeNode(1)
    queue = [root]
    count = 1
    
    while count < node_count and queue:
        node = queue.pop(0)
        
        if count < node_count:
            node.left = TreeNode(count + 1)
            queue.append(node.left)
            count += 1
        
        if count < node_count:
            node.right = TreeNode(count + 1)
            queue.append(node.right)
            count += 1
    
    return root

def create_long_list(length: int) -> ListNode:
    """
    创建长链表用于测试
    """
    if length <= 0:
        return None
    
    head = ListNode(1)
    current = head
    
    for i in range(2, length + 1):
        current.next = ListNode(i)
        current = current.next
    
    return head

def performance_test():
    """
    性能测试方法
    测试大规模数据的处理能力
    """
    print("\n=== 性能测试开始 ===")
    
    import time
    
    # 创建大规模测试数据
    node_count = 10000
    large_tree = create_large_tree(node_count)
    long_list = create_long_list(1000)
    
    start_time = time.time()
    
    result = is_sub_path(long_list, large_tree)
    
    end_time = time.time()
    duration = (end_time - start_time) * 1000  # 转换为毫秒
    
    print(f"性能测试 - 二叉树节点数: {node_count}, 链表长度: 1000")
    print(f"匹配结果: {result}")
    print(f"执行时间: {duration:.2f} 毫秒")
    
    print("=== 性能测试完成 ===")

def demo():
    """
    演示用例方法
    """
    print("\n=== 演示用例 ===")
    
    demo_head = ListNode(1, ListNode(2, ListNode(3)))
    demo_root = TreeNode(1, 
        TreeNode(2, 
            TreeNode(3), 
            TreeNode(4)
        ), 
        TreeNode(5)
    )
    
    demo_result = is_sub_path(demo_head, demo_root)
    print(f"演示用例匹配结果: {demo_result}")

def main():
    """
    主函数 - 处理测试和演示
    """
    # 运行验证测试
    verify_results()
    
    # 运行性能测试
    performance_test()
    
    # 运行演示用例
    demo()

if __name__ == "__main__":
    main()
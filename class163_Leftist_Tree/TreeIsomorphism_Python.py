#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
牛客 NC20828 [HNOI2004] 树的同构
题目链接：https://ac.nowcoder.com/acm/problem/20828

题目描述：
给定两棵有根树，判断它们是否同构。同构的定义是：两棵树可以通过若干次交换子节点的顺序得到彼此。

解题思路：
使用左偏树维护哈希值，进行树的同构判断。
通过递归计算每棵树的哈希值，同构的树会得到相同的哈希值。

算法步骤：
1. 对于每棵树，递归计算每个节点的哈希值
2. 使用左偏树对子节点的哈希值进行排序
3. 将排序后的子节点哈希值合并成当前节点的哈希值
4. 比较所有树的哈希值，相同哈希值的树是同构的

时间复杂度：O(N log N)，其中N是节点总数
空间复杂度：O(N)

相关题目：
- Java实现：TreeIsomorphism_Java.java
- Python实现：TreeIsomorphism_Python.py
- C++实现：TreeIsomorphism_Cpp.cpp
"""

import sys

class LeftistTreeNode:
    """
    左偏树节点类
    """
    def __init__(self, hash_value):
        self.hash = hash_value  # 哈希值
        self.dist = 0           # 距离（空路径长度）
        self.left = None
        self.right = None

# 合并两个左偏树
def merge(a, b):
    """
    合并两个左偏树
    :param a: 第一棵左偏树的根节点
    :param b: 第二棵左偏树的根节点
    :return: 合并后的左偏树根节点
    """
    # 处理空树情况
    if a is None:
        return b
    if b is None:
        return a
    
    # 维护大根堆性质：确保a的根节点哈希值大于等于b的根节点哈希值
    if a.hash < b.hash:
        a, b = b, a
    
    # 递归合并a的右子树与b
    a.right = merge(a.right, b)
    
    # 维护左偏性质：左子树的距离应大于等于右子树的距离
    if a.left is None or (a.right is not None and a.left.dist < a.right.dist):
        a.left, a.right = a.right, a.left
    
    # 更新距离：叶子节点距离为0，非叶子节点距离为其右子树距离+1
    a.dist = 0 if a.right is None else a.right.dist + 1
    return a

class TreeNode:
    """
    树节点类
    """
    def __init__(self, id_num):
        self.id = id_num
        self.children = []

# 计算树的哈希值
def compute_hash(root):
    """
    计算树的哈希值
    :param root: 树的根节点
    :return: 树的哈希值
    """
    # 空节点的哈希值为0
    if root is None:
        return 0
    
    # 使用左偏树维护子节点的哈希值
    heap = None
    for child in root.children:
        child_hash = compute_hash(child)
        heap = merge(heap, LeftistTreeNode(child_hash))
    
    # 计算当前节点的哈希值，结合子节点的哈希值
    hash_value = 1  # 初始哈希值
    temp_heaps = []
    # 收集所有子节点的哈希值
    while heap is not None:
        temp_heaps.append(heap.hash)
        # 记录左右子树
        left = heap.left
        right = heap.right
        # 删除当前根节点
        heap = merge(left, right)
    
    # 对子节点的哈希值排序，确保同构的树得到相同的哈希值
    temp_heaps.sort()
    for h in temp_heaps:
        hash_value = hash_value * 1000003 + h  # 使用大质数作为基数
    
    return hash_value

# 构建树
def build_tree(scanner, n):
    """
    构建树
    :param scanner: 输入扫描器
    :param n: 节点数量
    :return: 树的根节点
    """
    nodes = [TreeNode(i) for i in range(n + 1)]  # 节点编号从1开始
    root_id = -1
    
    for i in range(1, n + 1):
        parent = int(scanner.readline())
        if parent == 0:
            root_id = i
        else:
            nodes[parent].children.append(nodes[i])
    
    return nodes[root_id]

def main():
    """
    主函数
    输入格式：
    第一行包含一个整数t，表示测试用例数量
    对于每个测试用例：
      第一行包含一个整数n，表示节点数量
      接下来n行，第i行包含一个整数，表示节点i的父节点（0表示根节点）
    输出格式：
    对于每个测试用例，输出与当前树同构的最小编号
    """
    scanner = sys.stdin.read().split()
    ptr = 0
    t = int(scanner[ptr])
    ptr += 1
    
    hash_to_trees = {}
    
    for i in range(1, t + 1):
        n = int(scanner[ptr])
        ptr += 1
        # 构建树
        nodes = [TreeNode(j) for j in range(n + 1)]
        root_id = -1
        for j in range(1, n + 1):
            parent = int(scanner[ptr])
            ptr += 1
            if parent == 0:
                root_id = j
            else:
                nodes[parent].children.append(nodes[j])
        
        root = nodes[root_id]
        hash_value = compute_hash(root)
        
        if hash_value not in hash_to_trees:
            hash_to_trees[hash_value] = []
        hash_to_trees[hash_value].append(i)
    
    # 输出同构的最小树编号
    output = []
    for i in range(1, t + 1):
        found = False
        for group in hash_to_trees.values():
            if i in group:
                output.append(str(group[0]))
                found = True
                break
        if not found:
            output.append(str(i))
    
    print(' '.join(output))

if __name__ == "__main__":
    main()
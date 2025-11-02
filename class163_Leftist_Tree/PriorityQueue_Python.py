#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
AizuOJ ALDS1_9_C Priority Queue（优先级队列）
题目链接：https://onlinejudge.u-aizu.ac.jp/courses/lesson/1/ALDS1/9/ALDS1_9_C

题目描述：
实现一个最大优先级队列，支持以下操作：
1. insert(x) - 插入元素x
2. extract - 提取并删除最大元素
3. end - 结束程序

解题思路：
使用左偏树（Leftist Tree）实现最大堆，可以高效支持合并和删除最大元素操作。
左偏树是一种可合并堆，具有良好的时间复杂度特性。

算法特点：
1. 左偏树是一种二叉树，满足堆性质（父节点值大于等于子节点值）
2. 满足左偏性质：左子树的距离大于等于右子树的距离
3. 距离定义：从节点到最近的空节点的路径长度

时间复杂度：
- 插入元素：O(log n)
- 提取最大元素：O(log n)
空间复杂度：O(n)

相关题目：
- Java实现：PriorityQueue_Java.java
- Python实现：PriorityQueue_Python.py
- C++实现：PriorityQueue_Cpp.cpp
"""

class LeftistTreeNode:
    """
    左偏树节点类（最大堆）
    """
    def __init__(self, value):
        self.value = value  # 节点值
        self.dist = 0       # 距离（空路径长度）
        self.left = None
        self.right = None

def merge(a, b):
    """
    合并两个左偏树（最大堆）
    :param a: 第一棵左偏树的根节点
    :param b: 第二棵左偏树的根节点
    :return: 合并后的左偏树根节点
    """
    # 处理空树情况
    if a is None:
        return b
    if b is None:
        return a
    
    # 维护最大堆性质：确保a的根节点值大于等于b的根节点值
    if a.value < b.value:
        a, b = b, a
    
    # 递归合并a的右子树与b
    a.right = merge(a.right, b)
    
    # 维护左偏性质：左子树的距离应大于等于右子树的距离
    if a.left is None or (a.right is not None and a.left.dist < a.right.dist):
        a.left, a.right = a.right, a.left
    
    # 更新距离：叶子节点距离为0，非叶子节点距离为其右子树距离+1
    a.dist = 0 if a.right is None else a.right.dist + 1
    return a

def get_max(root):
    """
    获取最大值（堆顶）
    :param root: 左偏树根节点
    :return: 堆顶元素的值
    :raises ValueError: 如果堆为空
    """
    if root is None:
        raise ValueError("Priority queue is empty")
    return root.value

def delete_max(root):
    """
    删除最大值（堆顶）
    :param root: 左偏树根节点
    :return: 删除堆顶元素后的新根节点
    :raises ValueError: 如果堆为空
    """
    if root is None:
        raise ValueError("Priority queue is empty")
    # 合并左右子树作为新的根节点
    new_root = merge(root.left, root.right)
    return new_root

def insert(root, value):
    """
    插入元素
    :param root: 左偏树根节点
    :param value: 要插入的元素值
    :return: 插入元素后的新根节点
    """
    # 创建新节点
    new_node = LeftistTreeNode(value)
    # 合并原树与新节点
    return merge(root, new_node)

def main():
    """
    主函数，处理输入命令并执行相应操作
    输入格式：
    多行输入，每行包含一个命令：
    - insert x：插入元素x
    - extract：提取并删除最大元素
    - end：结束程序
    输出格式：
    对于每个extract命令，输出提取的最大元素
    """
    import sys
    root = None  # 左偏树根节点，初始为空
    
    for line in sys.stdin:
        parts = line.strip().split()
        command = parts[0]
        
        if command == "insert":
            value = int(parts[1])
            root = insert(root, value)
        elif command == "extract":
            max_value = get_max(root)
            print(max_value)
            root = delete_max(root)
        elif command == "end":
            break

if __name__ == "__main__":
    main()
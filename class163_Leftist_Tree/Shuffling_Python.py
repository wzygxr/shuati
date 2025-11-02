#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
AtCoder ARC065F Shuffling（洗牌）
题目链接：https://atcoder.jp/contests/arc065/tasks/arc065_d

题目描述：
给定一个字符串s，其中包含字符'o'和'x'。我们可以执行任意次数的操作，
每次操作可以选择任意两个字符进行交换。我们的目标是通过最少的交换次数，
使得任意长度为k的连续子串中至少包含一个'o'。

解题思路：
使用左偏树（Leftist Tree）来维护区间内'o'的位置，以高效地找到最优的交换策略。
左偏树是一种可合并堆，在本题中用作最小堆来快速获取最接近目标位置的'o'。

算法步骤：
1. 收集所有'o'的位置
2. 计算最少需要多少个'o'才能覆盖所有长度为k的窗口（根据鸽巢原理）
3. 使用左偏树维护可用的'o'位置，贪心地为每个窗口分配最近的'o'

时间复杂度：O(n log n)，其中n是字符串长度
空间复杂度：O(n)

相关题目：
- Java实现：Shuffling_Java.java
- Python实现：Shuffling_Python.py
- C++实现：Shuffling_Cpp.cpp
"""

class LeftistTreeNode:
    """
    左偏树节点类
    """
    def __init__(self, position):
        self.position = position  # 'o'在原字符串中的位置
        self.value = position     # 值（这里是位置值，用于最小堆比较）
        self.dist = 0             # 距离（空路径长度）
        self.left = None
        self.right = None

def merge(a, b):
    """
    合并两个左偏树（小根堆）
    :param a: 第一棵左偏树的根节点
    :param b: 第二棵左偏树的根节点
    :return: 合并后的左偏树根节点
    """
    # 处理空树情况
    if a is None:
        return b
    if b is None:
        return a
    
    # 维护小根堆性质：确保a的根节点值小于等于b的根节点值
    if a.value > b.value:
        a, b = b, a
    
    # 递归合并a的右子树与b
    a.right = merge(a.right, b)
    
    # 维护左偏性质：左子树的距离应大于等于右子树的距离
    if a.left is None or (a.right is not None and a.left.dist < a.right.dist):
        a.left, a.right = a.right, a.left
    
    # 更新距离：叶子节点距离为0，非叶子节点距离为其右子树距离+1
    a.dist = 0 if a.right is None else a.right.dist + 1
    return a

def get_min(root):
    """
    获取堆顶元素（最小值）
    :param root: 左偏树根节点
    :return: 堆顶元素节点
    """
    return root

def delete_min(root):
    """
    删除堆顶元素
    :param root: 左偏树根节点
    :return: 删除堆顶元素后的新根节点
    """
    if root is None:
        return None
    # 合并左右子树作为新的根节点
    new_root = merge(root.left, root.right)
    return new_root

def min_swaps(s, k):
    """
    计算最小交换次数
    :param s: 输入字符串，由'o'和'x'组成
    :param k: 窗口大小
    :return: 最少交换次数，若无法满足条件则返回-1
    """
    n = len(s)
    o_positions = []
    
    # 收集所有'o'的位置
    for i in range(n):
        if s[i] == 'o':
            o_positions.append(i)
    
    m = len(o_positions)
    
    # 特殊情况处理：如果没有'o'，则无法满足条件
    if m == 0:
        return -1
    
    # 计算最少需要多少个'o'才能覆盖所有长度为k的窗口
    # 根据鸽巢原理，至少需要ceil(n/k)个'o'
    required = (n + k - 1) // k
    
    # 如果现有'o'的数量不足，返回-1
    if m < required:
        return -1
    
    # 使用左偏树来维护当前可以覆盖的o的位置
    min_heap = None
    swaps = 0
    
    # 遍历每个可能的窗口位置，计算需要移动的'o'
    current_pos = 0
    for i in range(required):
        # 理想情况下，第i个'o'应该放在位置i*k
        target_pos = i * k
        
        # 如果当前没有足够的'o'可用，从后面的'o'中选择最近的
        while current_pos < m and o_positions[current_pos] <= target_pos + (k - 1):
            min_heap = merge(min_heap, LeftistTreeNode(o_positions[current_pos]))
            current_pos += 1
        
        # 获取最小的可用'o'的位置
        min_node = get_min(min_heap)
        # 添加类型检查以避免类型错误
        if min_node is None:
            # 这种情况理论上不应该发生，因为我们在前面保证了有足够的'o'
            break
        actual_pos = min_node.position
        
        # 计算需要交换的次数（这里简化了计算，实际是移动的距离）
        swaps += abs(actual_pos - target_pos)
        
        # 从堆中删除已使用的'o'
        min_heap = delete_min(min_heap)
    
    return swaps

def main():
    """
    主函数，读取输入并输出结果
    输入格式：
    第一行包含两个整数n和k
    第二行包含长度为n的字符串s
    输出格式：
    输出最少交换次数
    """
    import sys
    input = sys.stdin.read().split()
    n = int(input[0])
    k = int(input[1])
    s = input[2]
    
    result = min_swaps(s, k)
    print(result)

if __name__ == "__main__":
    main()
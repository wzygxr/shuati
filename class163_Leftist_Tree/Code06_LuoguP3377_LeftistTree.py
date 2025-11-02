#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
洛谷P3377 【模板】左偏树/可并堆
题目链接: https://www.luogu.com.cn/problem/P3377

题目描述：
如题，一开始有n个小根堆，每个堆包含且仅包含一个数。接下来需要支持两种操作：
1. 1 x y：将第x个数和第y个数所在的小根堆合并（若第x或第y个数已经被删除或第x和第y个数在同一个堆内，则无视此操作）
2. 2 x：输出第x个数所在的堆最小数，并将这个最小数删除（若有多个最小数，优先删除先输入的；若第x个数已经被删除，则输出-1并无视删除操作）

解题思路：
使用左偏树实现可并堆，支持快速合并操作和删除最小值操作。
1. 使用左偏树维护每个小根堆
2. 使用并查集维护每个节点所属的堆
3. 对于操作1：合并两个堆
4. 对于操作2：删除堆顶元素

左偏树核心性质：
1. 堆性质：父节点的值小于等于子节点的值
2. 左偏性质：左子节点的距离大于等于右子节点的距离
3. 距离定义：从节点到最近的外节点（空节点）的边数

算法优势：
1. 合并操作时间复杂度为O(log n)
2. 插入和删除操作时间复杂度为O(log n)
3. 支持高效处理动态集合合并问题

时间复杂度分析：
- 左偏树合并: O(log n)
- 左偏树插入: O(log n)
- 左偏树删除: O(log n)
- 并查集操作: 近似 O(1)
- 总体复杂度: O(M * log N)

空间复杂度分析:
- 左偏树节点存储: O(N)
- 并查集存储: O(N)
- 总体空间复杂度: O(N)

相关题目：
- Java实现：Code06_LuoguP3377_LeftistTree.java
- Python实现：Code06_LuoguP3377_LeftistTree.py
- C++实现：Code06_LuoguP3377_LeftistTree.cpp
"""


class Node:
    """
    左偏树节点类
    """
    def __init__(self, val, index, time):
        """
        构造函数
        :param val: 节点权值
        :param index: 节点索引
        :param time: 输入时间
        """
        self.val = val          # 节点权值
        self.dist = 0           # 节点距离（到最近外节点的距离）
        self.index = index      # 节点索引
        self.left = None        # 左子节点
        self.right = None       # 右子节点
        self.time = time        # 输入时间，用于处理相同值时的优先级


class LeftistTree:
    """
    左偏树类
    """
    def __init__(self):
        """
        构造函数
        """
        self.nodes = {}         # 节点字典
        self.node_count = 0     # 节点计数器

    def init_node(self, val, time):
        """
        初始化节点
        :param val: 节点权值
        :param time: 输入时间
        :return: 节点索引
        """
        self.node_count += 1
        self.nodes[self.node_count] = Node(val, self.node_count, time)
        return self.node_count

    def merge(self, a, b):
        """
        合并两个左偏树
        :param a: 第一棵左偏树根节点索引
        :param b: 第二棵左偏树根节点索引
        :return: 合并后左偏树根节点索引
        """
        # 如果其中一个为空，返回另一个
        if not a:
            return b
        if not b:
            return a

        # 确保a节点权值 <= b节点权值（小根堆）
        # 如果值相同，优先选择输入时间早的
        if (self.nodes[a].val > self.nodes[b].val or 
            (self.nodes[a].val == self.nodes[b].val and self.nodes[a].time > self.nodes[b].time)):
            a, b = b, a

        # 递归合并右子树和b树
        right_index = self.nodes[a].right.index if self.nodes[a].right else 0
        merged_index = self.merge(right_index, b)

        if merged_index:
            self.nodes[a].right = self.nodes[merged_index]
        else:
            self.nodes[a].right = None

        # 维护左偏性质：左子树距离 >= 右子树距离
        left_dist = self.nodes[a].left.dist if self.nodes[a].left else -1
        right_dist = self.nodes[a].right.dist if self.nodes[a].right else -1

        if left_dist < right_dist:
            # 交换左右子树
            self.nodes[a].left, self.nodes[a].right = self.nodes[a].right, self.nodes[a].left

        # 更新距离
        new_right_dist = self.nodes[a].right.dist if self.nodes[a].right else -1
        self.nodes[a].dist = new_right_dist + 1

        return a

    def pop(self, root):
        """
        删除左偏树根节点
        :param root: 根节点索引
        :return: 新的根节点索引
        """
        if not root:
            return 0

        left_index = self.nodes[root].left.index if self.nodes[root].left else 0
        right_index = self.nodes[root].right.index if self.nodes[root].right else 0

        return self.merge(left_index, right_index)


class UnionFind:
    """
    并查集类
    """
    def __init__(self, n):
        """
        构造函数
        :param n: 节点数量
        """
        self.parent = list(range(n + 1))  # 父节点数组

    def find(self, x):
        """
        查找根节点（带路径压缩）
        :param x: 节点索引
        :return: 根节点索引
        """
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])  # 路径压缩
        return self.parent[x]

    def union(self, x, y):
        """
        合并两个集合
        :param x: 第一个节点索引
        :param y: 第二个节点索引
        """
        root_x = self.find(x)
        root_y = self.find(y)
        if root_x != root_y:
            self.parent[root_x] = root_y


def main():
    """
    主函数
    输入格式：
    第一行包含两个整数n和m，分别表示初始节点数和操作数
    第二行包含n个整数，表示每个节点的初始值
    接下来m行，每行包含一个操作：
      1 x y：合并x和y所在的堆
      2 x：删除x所在堆的最小元素并输出
    输出格式：
    对于每个操作2，输出删除的最小元素，如果x已被删除则输出-1
    """
    import sys

    # 读取所有输入
    lines = []
    for line in sys.stdin:
        line = line.strip()
        if line:
            lines.append(line)

    i = 0
    # 读取输入
    n, m = map(int, lines[i].split())
    i += 1

    # 初始化数据结构
    tree = LeftistTree()
    uf = UnionFind(n)
    roots = [0] * (n + 1)  # 每个堆对应的左偏树根节点
    deleted = [False] * (n + 1)  # 标记节点是否被删除
    current_time = 0  # 时间戳

    # 读取每个节点的初始值
    values = list(map(int, lines[i].split()))
    i += 1

    # 初始化每个节点为单独的左偏树
    for j in range(1, n + 1):
        current_time += 1
        node_index = tree.init_node(values[j - 1], current_time)
        roots[j] = node_index

    # 处理每次操作
    for _ in range(m):
        operation = list(map(int, lines[i].split()))
        i += 1
        
        op = operation[0]
        
        if op == 1:
            # 合并操作
            x, y = operation[1], operation[2]
            
            # 检查节点是否被删除
            if deleted[x] or deleted[y]:
                continue
            
            # 查找两个节点所属的堆
            root_x = uf.find(x)
            root_y = uf.find(y)
            
            # 如果已经在同一个堆中，无需合并
            if root_x == root_y:
                continue
            
            # 合并两个堆
            merged_root = tree.merge(roots[root_x], roots[root_y])
            
            # 更新并查集和根节点信息
            if merged_root:
                uf.union(root_x, root_y)
                roots[uf.find(root_x)] = merged_root
                
        elif op == 2:
            # 删除最小值操作
            x = operation[1]
            
            # 检查节点是否被删除
            if deleted[x]:
                print(-1)
                continue
            
            # 查找节点所属的堆
            root_x = uf.find(x)
            
            # 输出堆顶元素
            print(tree.nodes[roots[root_x]].val)
            
            # 标记堆顶元素为已删除
            deleted[tree.nodes[roots[root_x]].index] = True
            
            # 删除堆顶元素
            new_root = tree.pop(roots[root_x])
            roots[root_x] = new_root


if __name__ == "__main__":
    main()

#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
洛谷P2713 罗马游戏

题目描述：
罗马皇帝很喜欢玩杀人游戏。他的军队里面有n个士兵，每个士兵都是一个独立的团。
最近举行了一次平面几何测试，每个士兵都得到了一个分数。
皇帝很喜欢平面几何，他对那些得分很低的士兵嗤之以鼻。

他决定玩这样一个游戏。它可以发两种命令：
- M i j 把i所在的团和j所在的团合并成一个团。如果i,j有一个士兵是死人，那么就忽略该命令。
- K i 把i所在的团里面得分最低的士兵杀死。如果i这个士兵已经死了，这条命令就忽略。

皇帝希望他每发布一条K i命令，下面的将军就把被杀的士兵的分数报上来
（如果这条命令被忽略，那么就报0分）。

解题思路：
使用左偏树维护每个团的最小值（小根堆），配合并查集维护团的连通性。
1. 使用左偏树维护每个团的士兵分数（小根堆）
2. 使用并查集维护士兵所属的团
3. 对于M操作：合并两个团
4. 对于K操作：删除团中最小分数的士兵

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
"""


class Node:
    """
    左偏树节点类
    """
    def __init__(self, val, index):
        self.val = val          # 节点权值（士兵分数）
        self.dist = 0           # 节点距离（到最近外节点的距离）
        self.index = index      # 节点索引
        self.left = None        # 左子节点
        self.right = None       # 右子节点


class LeftistTree:
    """
    左偏树类
    """
    def __init__(self):
        self.nodes = {}         # 节点字典
        self.node_count = 0     # 节点计数器

    def init_node(self, val):
        """
        初始化节点
        :param val: 节点权值
        :return: 节点索引
        """
        self.node_count += 1
        self.nodes[self.node_count] = Node(val, self.node_count)
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
        if self.nodes[a].val > self.nodes[b].val:
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
    """
    import sys

    # 读取所有输入
    lines = []
    for line in sys.stdin:
        line = line.strip()
        if line:
            lines.append(line)

    i = 0
    # 读取士兵数量
    n = int(lines[i])
    i += 1

    # 初始化数据结构
    tree = LeftistTree()
    uf = UnionFind(n)
    roots = [0] * (n + 1)  # 每个团对应的左偏树根节点
    killed = [False] * (n + 1)  # 标记士兵是否被杀死

    # 读取每个士兵的分数
    scores = list(map(int, lines[i].split()))
    i += 1

    # 初始化每个士兵为单独的左偏树
    for j in range(1, n + 1):
        node_index = tree.init_node(scores[j - 1])
        roots[j] = node_index

    # 读取操作数量
    m = int(lines[i])
    i += 1

    # 处理每次操作
    for _ in range(m):
        operation = lines[i].split()
        i += 1
        
        op = operation[0]
        
        if op == "M":
            # 合并操作
            x, y = int(operation[1]), int(operation[2])
            
            # 检查士兵是否被杀死
            if killed[x] or killed[y]:
                continue
            
            # 查找两个士兵所属的团
            root_x = uf.find(x)
            root_y = uf.find(y)
            
            # 如果已经在同一个团中，无需合并
            if root_x == root_y:
                continue
            
            # 合并两个团
            merged_root = tree.merge(roots[root_x], roots[root_y])
            
            # 更新并查集和根节点信息
            if merged_root:
                uf.union(root_x, root_y)
                roots[uf.find(root_x)] = merged_root
                
        elif op == "K":
            # 杀死最小分数士兵操作
            x = int(operation[1])
            
            # 检查士兵是否被杀死
            if killed[x]:
                print(0)
                continue
            
            # 查找士兵所属的团
            root_x = uf.find(x)
            
            # 输出团中最小分数
            print(tree.nodes[roots[root_x]].val)
            
            # 标记最小分数士兵为已杀死
            killed[tree.nodes[roots[root_x]].index] = True
            
            # 删除团中最小分数士兵
            new_root = tree.pop(roots[root_x])
            roots[root_x] = new_root


if __name__ == "__main__":
    main()

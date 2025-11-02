#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
HDU 1512 Monkey King - 左偏树解法

题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1512

问题描述：
有N只猴子，每只猴子有一个武力值。初始时，所有猴子互不相识。
当两只互不相识的猴子发生冲突时，他们会各自邀请自己朋友圈中武力值最高的猴子进行决斗。
决斗后，两只参战猴子的武力值减半（向下取整），并且两个朋友圈合并为一个。
给定M次冲突，每次查询输出冲突后朋友圈中的最大武力值，如果两只猴子已经相识则输出-1。

解题思路：
1. 使用左偏树维护每个朋友圈的最大值（大根堆）
2. 使用并查集维护朋友圈的连通性
3. 对于每次冲突：
   - 检查两只猴子是否已经相识（并查集）
   - 如果不相识，找出各自朋友圈的最大值猴子
   - 将这两只猴子的武力值减半
   - 从各自左偏树中删除这两个节点
   - 将减半后的节点重新插入对应左偏树
   - 合并两个左偏树
   - 输出合并后左偏树的根节点值（最大值）

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
        self.val = val          # 节点权值（猴子武力值）
        self.dist = 0           # 节点距离（到最近外节点的距离）
        self.left = None        # 左子节点
        self.right = None       # 右子节点
        self.index = index      # 节点索引


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

        # 确保a节点权值 >= b节点权值（大根堆）
        if self.nodes[a].val < self.nodes[b].val:
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
    while i < len(lines):
        # 读取猴子数量
        n = int(lines[i])
        i += 1

        # 初始化数据结构
        tree = LeftistTree()
        uf = UnionFind(n)
        roots = [0] * (n + 1)  # 每个朋友圈对应的左偏树根节点

        # 读取每只猴子的武力值
        powers = list(map(int, lines[i].split()))
        i += 1

        # 初始化每只猴子为单独的左偏树
        for j in range(1, n + 1):
            node_index = tree.init_node(powers[j - 1])
            roots[j] = node_index

        # 读取冲突次数
        m = int(lines[i])
        i += 1

        # 处理每次冲突
        for _ in range(m):
            x, y = map(int, lines[i].split())
            i += 1

            # 查找两只猴子所属的朋友圈
            root_x = uf.find(x)
            root_y = uf.find(y)

            # 如果两只猴子已经相识
            if root_x == root_y:
                print(-1)
                continue

            # 获取两个朋友圈的最大武力值猴子
            max_x = roots[root_x]
            max_y = roots[root_y]

            # 将两只猴子的武力值减半
            tree.nodes[max_x].val //= 2
            tree.nodes[max_y].val //= 2

            # 从各自左偏树中删除根节点
            new_root_x = tree.pop(max_x)
            new_root_y = tree.pop(max_y)

            # 将减半后的节点重新插入
            new_node_x = tree.init_node(tree.nodes[max_x].val)
            new_node_y = tree.init_node(tree.nodes[max_y].val)

            # 合并操作
            merged_xy = tree.merge(new_root_x, new_node_x)
            merged_yy = tree.merge(new_root_y, new_node_y)
            final_merged = tree.merge(merged_xy, merged_yy)

            # 更新朋友圈根节点和并查集
            roots[root_x] = final_merged
            uf.union(root_x, root_y)
            roots[uf.find(root_x)] = final_merged

            # 输出合并后朋友圈的最大武力值
            print(tree.nodes[final_merged].val)


if __name__ == "__main__":
    main()

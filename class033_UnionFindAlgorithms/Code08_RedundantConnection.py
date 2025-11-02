#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
冗余连接 (Python版本)
树可以看成是一个连通且无环的无向图。
给定往一棵n个节点(节点值1～n)的树中添加一条边后的图。
添加的边的两个顶点包含在1到n中间，且这条附加的边不属于树中已存在的边。
图的信息记录于长度为n的二维数组edges，edges[i] = [ai, bi]表示图中在ai和bi之间存在一条边。
请找出一条可以删去的边，删除后可使得剩余部分是一棵有n个节点的树。
如果有多个答案，则返回数组edges中最后出现的边。

示例 1:
输入: edges = [[1,2],[1,3],[2,3]]
输出: [2,3]

示例 2:
输入: edges = [[1,2],[2,3],[3,4],[1,4],[1,5]]
输出: [1,4]

约束条件:
n == edges.length
3 <= n <= 1000
edges[i].length == 2
1 <= ai < bi <= edges.length
ai != bi
edges中无重复元素
给定的图是连通的

测试链接: https://leetcode.cn/problems/redundant-connection/
相关平台: LeetCode 684, LintCode 1048, 牛客网
"""


class UnionFind:
    """
    并查集数据结构实现
    包含路径压缩优化
    """

    def __init__(self, n):
        """
        初始化并查集
        :param n: 节点数量
        """
        # parent[i]表示节点i的父节点
        self.parent = list(range(n))

    def find(self, x):
        """
        查找节点的根节点（代表元素）
        使用路径压缩优化
        :param x: 要查找的节点
        :return: 节点x所在集合的根节点
        """
        if self.parent[x] != x:
            # 路径压缩：将路径上的所有节点直接连接到根节点
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]

    def union(self, x, y):
        """
        合并两个集合
        :param x: 第一个节点
        :param y: 第二个节点
        """
        root_x = self.find(x)
        root_y = self.find(y)
        # 如果已经在同一个集合中，则无需合并
        if root_x != root_y:
            self.parent[root_x] = root_y

    def is_connected(self, x, y):
        """
        判断两个节点是否在同一个集合中
        :param x: 第一个节点
        :param y: 第二个节点
        :return: 如果在同一个集合中返回True，否则返回False
        """
        return self.find(x) == self.find(y)


def find_redundant_connection(edges):
    """
    使用并查集解决冗余连接问题

    解题思路：
    1. 遍历所有的边，对于每条边的两个顶点：
       - 如果它们已经在同一个连通分量中，说明添加这条边会形成环，这就是我们要找的冗余边
       - 否则，将这两个顶点所在的连通分量合并
    2. 由于题目要求返回最后出现的冗余边，我们按顺序遍历边，找到第一条形成环的边即可

    时间复杂度：O(N * α(N))，其中N是边的数量，α是阿克曼函数的反函数
    空间复杂度：O(N)

    :param edges: 边的数组
    :return: 冗余的边
    """
    if not edges:
        return []

    # 节点数量等于边的数量（因为是树加一条边）
    n = len(edges)
    union_find = UnionFind(n + 1)  # 节点编号从1开始，所以需要n+1个位置

    # 遍历所有的边
    for edge in edges:
        node1, node2 = edge[0], edge[1]

        # 如果两个节点已经在同一个连通分量中，说明添加这条边会形成环
        if union_find.is_connected(node1, node2):
            return edge  # 返回这条冗余的边
        else:
            # 否则将两个节点所在的连通分量合并
            union_find.union(node1, node2)

    # 根据题目保证，一定会有一条冗余边，所以这里不会执行到
    return []


# 测试方法
if __name__ == "__main__":
    # 测试用例1
    edges1 = [[1,2],[1,3],[2,3]]
    result1 = find_redundant_connection(edges1)
    print(f"测试用例1结果: [{result1[0]}, {result1[1]}]")  # 预期输出: [2, 3]

    # 测试用例2
    edges2 = [[1,2],[2,3],[3,4],[1,4],[1,5]]
    result2 = find_redundant_connection(edges2)
    print(f"测试用例2结果: [{result2[0]}, {result2[1]}]")  # 预期输出: [1, 4]

    # 测试用例3
    edges3 = [[1,2],[1,3],[1,4],[3,4],[4,5]]
    result3 = find_redundant_connection(edges3)
    print(f"测试用例3结果: [{result3[0]}, {result3[1]}]")  # 预期输出: [3, 4]
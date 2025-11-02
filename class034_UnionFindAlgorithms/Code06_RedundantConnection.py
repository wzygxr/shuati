#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
冗余连接
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

测试链接: https://leetcode.cn/problems/redundant-connection/

解题思路:
使用并查集检测图中的环。遍历每条边，如果边的两个顶点已经在同一个连通分量中，
说明添加这条边会形成环，这就是要删除的边。

时间复杂度: O(n*α(n))，其中n是边的数量，α是阿克曼函数的反函数
空间复杂度: O(n)
是否为最优解: 是

工程化考量:
1. 异常处理：检查输入是否为空
2. 可配置性：可以扩展支持带权图
3. 线程安全：当前实现不是线程安全的

与机器学习等领域的联系:
1. 图论：最小生成树算法中需要避免环的形成
2. 社交网络分析：检测社区结构中的冗余连接

语言特性差异:
Java: 对象引用和垃圾回收
C++: 指针操作和手动内存管理
Python: 动态类型和自动内存管理

极端输入场景:
1. 空图
2. 单节点图
3. 完全图
4. 链状图

性能优化:
1. 路径压缩优化find操作
2. 按秩合并优化union操作
3. 提前终止优化
"""


class UnionFind:
    """
    并查集类
    """

    def __init__(self, n):
        """
        初始化并查集
        :param n: 节点数量
        """
        self.parent = list(range(n + 1))  # parent[i]表示节点i的父节点，节点编号从1开始
        self.rank = [1] * (n + 1)        # rank[i]表示以i为根的树的高度上界

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
        使用按秩合并优化
        :param x: 第一个节点
        :param y: 第二个节点
        :return: 如果两个节点已经在同一个集合中返回False，否则返回True
        """
        root_x = self.find(x)
        root_y = self.find(y)

        # 如果已经在同一个集合中，说明会形成环
        if root_x == root_y:
            return False

        # 按秩合并：将秩小的树合并到秩大的树下
        if self.rank[root_x] > self.rank[root_y]:
            self.parent[root_y] = root_x
        elif self.rank[root_x] < self.rank[root_y]:
            self.parent[root_x] = root_y
        else:
            self.parent[root_y] = root_x
            self.rank[root_x] += 1

        return True


def find_redundant_connection(edges):
    """
    查找冗余连接
    :param edges: 边的数组
    :return: 冗余的边
    """
    # 边界条件检查
    if not edges:
        return []

    n = len(edges)

    # 创建并查集
    uf = UnionFind(n)

    # 遍历每条边
    for edge in edges:
        u, v = edge[0], edge[1]

        # 如果两个节点已经在同一个连通分量中，说明添加这条边会形成环
        if not uf.union(u, v):
            return edge

    # 理论上不会执行到这里
    return []


# 测试方法
if __name__ == "__main__":
    # 测试用例1
    edges1 = [[1, 2], [1, 3], [2, 3]]
    result1 = find_redundant_connection(edges1)
    print("测试用例1结果: [" + str(result1[0]) + ", " + str(result1[1]) + "]")  # 预期输出: [2, 3]

    # 测试用例2
    edges2 = [[1, 2], [2, 3], [3, 4], [1, 4], [1, 5]]
    result2 = find_redundant_connection(edges2)
    print("测试用例2结果: [" + str(result2[0]) + ", " + str(result2[1]) + "]")  # 预期输出: [1, 4]
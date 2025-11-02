#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
朋友圈 (Python版本)
班上有 N 名学生。其中有些人是朋友，有些则不是。他们的友谊具有是传递性。
如果已知 A 是 B 的朋友，B 是 C 的朋友，那么我们可以认为 A 也是 C 的朋友。
所谓的朋友圈，是指所有朋友都属于同一个圈子里。
给定一个 N * N 的矩阵 M，表示班级中学生之间的朋友关系。
如果M[i][j] = 1，表示已知第 i 个和 j 个学生互为朋友关系，否则为不知道他们是否为朋友。
返回所有朋友圈的数量。

示例 1:
输入: 
[[1,1,0],
 [1,1,0],
 [0,0,1]]
输出: 2 
说明：已知学生0和学生1互为朋友，他们在一个朋友圈。
第2个学生自己在一个朋友圈。所以返回2。

示例 2:
输入: 
[[1,1,0],
 [1,1,1],
 [0,1,1]]
输出: 1
说明：已知学生0和学生1互为朋友，学生1和学生2互为朋友，
所以学生0和学生2也是朋友，所以他们三个人在一个朋友圈，返回1。

约束条件：
1 <= n <= 200
M[i][i] == 1
M[i][j] == M[j][i]

测试链接: https://leetcode.cn/problems/friend-circles/
相关平台: LeetCode 547, LintCode 1045, 牛客网, HackerRank
"""


class UnionFind:
    """
    并查集数据结构实现
    包含路径压缩和按秩合并优化
    """

    def __init__(self, n):
        """
        初始化并查集
        :param n: 节点数量
        """
        # parent[i]表示节点i的父节点
        self.parent = list(range(n))
        # rank[i]表示以i为根的树的高度上界
        self.rank = [1] * n
        # 当前集合数量
        self.set_count = n

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
        """
        root_x = self.find(x)
        root_y = self.find(y)

        # 如果已经在同一个集合中，则无需合并
        if root_x != root_y:
            # 按秩合并：将秩小的树合并到秩大的树下
            if self.rank[root_x] > self.rank[root_y]:
                self.parent[root_y] = root_x
            elif self.rank[root_x] < self.rank[root_y]:
                self.parent[root_x] = root_y
            else:
                # 秩相等时，任选一个作为根，并将其秩加1
                self.parent[root_y] = root_x
                self.rank[root_x] += 1
            # 集合数量减1
            self.set_count -= 1

    def is_connected(self, x, y):
        """
        判断两个节点是否在同一个集合中
        :param x: 第一个节点
        :param y: 第二个节点
        :return: 如果在同一个集合中返回True，否则返回False
        """
        return self.find(x) == self.find(y)

    def get_set_count(self):
        """
        获取当前集合数量
        :return: 集合数量
        """
        return self.set_count


def find_circle_num(M):
    """
    使用并查集解决朋友圈问题

    解题思路：
    1. 初始化并查集，每个学生初始时都是独立的集合
    2. 遍历朋友关系矩阵，如果两个人是朋友，则将他们所在的集合合并
    3. 最终集合的数量就是朋友圈的数量

    时间复杂度：O(N^2 * α(N))，其中N是学生数量，α是阿克曼函数的反函数，近似为常数
    空间复杂度：O(N)

    :param M: 朋友关系矩阵
    :return: 朋友圈数量
    """
    if not M or not M[0]:
        return 0

    n = len(M)
    union_find = UnionFind(n)

    # 遍历矩阵的上三角部分（因为矩阵是对称的）
    for i in range(n):
        for j in range(i + 1, n):
            # 如果i和j是朋友，则合并他们的集合
            if M[i][j] == 1:
                union_find.union(i, j)

    # 返回集合数量，即朋友圈数量
    return union_find.get_set_count()


# 测试方法
if __name__ == "__main__":
    # 测试用例1
    M1 = [
        [1, 1, 0],
        [1, 1, 0],
        [0, 0, 1]
    ]
    print("测试用例1结果:", find_circle_num(M1))  # 预期输出: 2

    # 测试用例2
    M2 = [
        [1, 1, 0],
        [1, 1, 1],
        [0, 1, 1]
    ]
    print("测试用例2结果:", find_circle_num(M2))  # 预期输出: 1

    # 测试用例3：单个学生
    M3 = [[1]]
    print("测试用例3结果:", find_circle_num(M3))  # 预期输出: 1

    # 测试用例4：所有学生都互为朋友
    M4 = [
        [1, 1, 1],
        [1, 1, 1],
        [1, 1, 1]
    ]
    print("测试用例4结果:", find_circle_num(M4))  # 预期输出: 1
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
洛谷 P3374 【模板】树状数组 1
题目链接: https://www.luogu.com.cn/problem/P3374

题目描述:
给定一个数列，需要进行下面两种操作：
1. 将某一个数加上一个值
2. 求出某区间内所有数的和

输入格式:
第一行包含两个正整数 n, m，分别表示该数列数字的个数和总操作的次数。
第二行包含 n 个用空格分隔的整数，其中第 i 个数字表示数列第 i 项的初始值。
接下来 m 行每行包含 3 个整数，表示一个操作：
- 如果是 1 x k：表示将第 x 个数加上 k
- 如果是 2 x y：表示求出第 x 到第 y 项的和

输出格式:
对于每个 2 操作，输出一行一个整数表示答案。

样例输入:
5 5
1 5 4 2 3
1 1 3
2 2 4
1 2 4
2 1 5
2 2 4

样例输出:
11
18
16

解题思路:
使用树状数组（Binary Indexed Tree/Fenwick Tree）实现单点修改和区间查询
时间复杂度：
- 单点修改: O(log n)
- 区间查询: O(log n)
空间复杂度: O(n)
"""


class BinaryIndexTree:
    """
    树状数组类，用于高效处理单点修改和前缀和查询
    """

    def __init__(self, n):
        """
        初始化树状数组
        :param n: 数组大小
        """
        # 树状数组最大容量
        self.MAXN = n + 1
        # 树状数组，存储前缀和信息，索引从1开始
        self.tree = [0] * self.MAXN

    def lowbit(self, i):
        """
        lowbit函数：获取数字的二进制表示中最右边的1所代表的数值
        例如：x=6(110) 返回2(010)，x=12(1100) 返回4(0100)

        :param i: 输入数字
        :return: 最低位的1所代表的数值
        """
        return i & (-i)

    def add(self, i, v):
        """
        单点增加操作：在位置i上增加v

        :param i: 位置（从1开始）
        :param v: 增加的值
        """
        # 从位置i开始，沿着父节点路径向上更新所有相关的节点
        while i < self.MAXN:
            self.tree[i] += v
            # 移动到父节点
            i += self.lowbit(i)

    def sum(self, i):
        """
        查询前缀和：计算从位置1到位置i的所有元素之和

        :param i: 查询的结束位置
        :return: 前缀和
        """
        ans = 0
        # 从位置i开始，沿着子节点路径向下累加
        while i > 0:
            ans += self.tree[i]
            # 移动到前一个相关区间
            i -= self.lowbit(i)
        return ans

    def range_sum(self, l, r):
        """
        区间查询：计算从位置l到位置r的所有元素之和
        利用前缀和的性质：[l,r]的和 = [1,r]的和 - [1,l-1]的和

        :param l: 区间起始位置
        :param r: 区间结束位置
        :return: 区间和
        """
        return self.sum(r) - self.sum(l - 1)


def main():
    """
    主函数：处理输入输出和调用相关操作
    """
    # 读取数组长度n和操作次数m
    n, m = map(int, input().split())

    # 创建树状数组实例
    bit = BinaryIndexTree(n)

    # 读取初始数组并构建树状数组
    values = list(map(int, input().split()))
    for i in range(1, n + 1):
        # 初始构建相当于在每个位置上增加初始值
        bit.add(i, values[i - 1])

    # 处理m次操作
    for _ in range(m):
        operation = list(map(int, input().split()))
        if operation[0] == 1:
            # 操作1：在位置operation[1]上增加operation[2]
            bit.add(operation[1], operation[2])
        else:
            # 操作2：查询区间[operation[1], operation[2]]的和
            print(bit.range_sum(operation[1], operation[2]))


# 程序入口
if __name__ == "__main__":
    main()
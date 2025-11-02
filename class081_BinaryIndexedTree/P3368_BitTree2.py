#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
洛谷 P3368 【模板】树状数组 2
题目链接: https://www.luogu.com.cn/problem/P3368

题目描述:
给定一个数列，需要进行下面两种操作：
1. 将某区间加上一个值
2. 求出某一个数的值

输入格式:
第一行包含两个正整数 n, m，分别表示该数列数字的个数和总操作的次数。
第二行包含 n 个用空格分隔的整数，其中第 i 个数字表示数列第 i 项的初始值。
接下来 m 行每行包含 3 或 4 个整数，表示一个操作：
- 如果是 1 l r k：表示将区间 [l,r] 加上 k
- 如果是 2 x：表示求出第 x 项的值

输出格式:
对于每个 2 操作，输出一行一个整数表示答案。

样例输入:
5 5
1 5 4 2 3
1 2 4 2
2 3
1 1 5 -1
1 3 5 7
2 4

样例输出:
7
9

解题思路:
使用树状数组实现区间修改和单点查询，采用差分数组的思想
差分数组的性质：原数组的区间修改等价于差分数组的单点修改
原数组的单点查询等价于差分数组的前缀和查询
时间复杂度：
- 区间修改: O(log n)
- 单点查询: O(log n)
空间复杂度: O(n)
"""


class BinaryIndexTree:
    """
    树状数组类，用于高效处理区间修改和单点查询（使用差分数组）
    """

    def __init__(self, n):
        """
        初始化树状数组
        :param n: 数组大小
        """
        # 树状数组最大容量
        self.MAXN = n + 1
        # 树状数组，维护差分数组的前缀和
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

    def range_add(self, l, r, v):
        """
        区间增加操作：在区间[l,r]上每个元素都增加v
        利用差分数组的思想：
        在差分数组的第l个位置加上v，在第r+1个位置减去v

        :param l: 区间起始位置
        :param r: 区间结束位置
        :param v: 增加的值
        """
        self.add(l, v)
        self.add(r + 1, -v)


def main():
    """
    主函数：处理输入输出和调用相关操作
    """
    # 读取数组长度n和操作次数m
    n, m = map(int, input().split())

    # 创建树状数组实例
    bit = BinaryIndexTree(n)

    # 读取初始数组并构建差分数组
    values = list(map(int, input().split()))
    pre = 0
    for i in range(1, n + 1):
        # 构建差分数组：差分数组第i位 = 原数组第i位 - 原数组第i-1位
        bit.add(i, values[i - 1] - pre)
        pre = values[i - 1]

    # 处理m次操作
    for _ in range(m):
        operation = list(map(int, input().split()))
        if operation[0] == 1:
            # 操作1：区间[operation[1], operation[2]]增加operation[3]
            bit.range_add(operation[1], operation[2], operation[3])
        else:
            # 操作2：查询位置operation[1]的值（即差分数组的前缀和）
            print(bit.sum(operation[1]))


# 程序入口
if __name__ == "__main__":
    main()
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
POJ 3468 A Simple Problem with Integers
题目链接: http://poj.org/problem?id=3468

题目描述:
给定一个长度为 N 的数列 A，需要处理如下两种操作：
1. "C a b c"：将区间 [a, b] 中的每个数都加上 c
2. "Q a b"：求区间 [a, b] 中所有数的和

解题思路:
使用树状数组实现区间更新、区间查询。
这是树状数组的一个高级应用，需要使用差分的思想来处理区间更新。

设原数组为 A，差分数组为 D，其中 D[1] = A[1]，D[i] = A[i] - A[i-1] (i > 1)。

区间更新 [l, r] 增加 v：
- D[l] += v
- D[r+1] -= v

区间查询 [1, n] 的和：
- 设 sumD[i] = D[1] + D[2] + ... + D[i]
- 原数组前缀和 sumA[n] = (n+1) * sumD[n] - (D[1]*1 + D[2]*2 + ... + D[n]*n)

因此我们需要维护两个树状数组：
1. tree1: 维护差分数组 D
2. tree2: 维护 i*D[i]

时间复杂度：
- 区间更新: O(log n)
- 区间查询: O(log n)
空间复杂度: O(n)
"""


class POJ3468_SimpleProblemWithIntegers:
    def __init__(self, n):
        """
        树状数组初始化

        :param n: 数组长度
        """
        self.n = n
        self.tree1 = [0] * (n + 1)  # 维护差分数组 D
        self.tree2 = [0] * (n + 1)  # 维护 i*D[i]

    def lowbit(self, i):
        """
        lowbit函数：获取数字的二进制表示中最右边的1所代表的数值
        例如：x=6(110) 返回2(010)，x=12(1100) 返回4(0100)

        :param i: 输入数字
        :return: 最低位的1所代表的数值
        """
        return i & -i

    def add(self, position, value):
        """
        在 tree1 和 tree2 中的 position 位置增加 value

        :param position: 位置（从1开始）
        :param value: 增加的值
        """
        # 更新 tree1
        i = position
        while i <= self.n:
            self.tree1[i] += value
            i += self.lowbit(i)

        # 更新 tree2
        i = position
        while i <= self.n:
            self.tree2[i] += position * value
            i += self.lowbit(i)

    def sum1(self, position):
        """
        查询 tree1 的前缀和 [1, position]

        :param position: 查询位置
        :return: 前缀和
        """
        ans = 0
        i = position
        while i > 0:
            ans += self.tree1[i]
            i -= self.lowbit(i)
        return ans

    def sum2(self, position):
        """
        查询 tree2 的前缀和 [1, position]

        :param position: 查询位置
        :return: 前缀和
        """
        ans = 0
        i = position
        while i > 0:
            ans += self.tree2[i]
            i -= self.lowbit(i)
        return ans

    def update(self, l, r, value):
        """
        区间更新：将区间 [l, r] 中的每个数都加上 value

        :param l: 区间左端点
        :param r: 区间右端点
        :param value: 增加的值
        """
        self.add(l, value)
        self.add(r + 1, -value)

    def prefixSum(self, position):
        """
        区间查询：求区间 [1, position] 的前缀和

        :param position: 查询位置
        :return: 前缀和
        """
        return (position + 1) * self.sum1(position) - self.sum2(position)

    def rangeSum(self, l, r):
        """
        区间查询：求区间 [l, r] 的和

        :param l: 区间左端点
        :param r: 区间右端点
        :return: 区间和
        """
        if l == 1:
            return self.prefixSum(r)
        return self.prefixSum(r) - self.prefixSum(l - 1)


def main():
    """
    主函数：处理输入输出和调用相关操作
    由于POJ的在线评测系统可能不支持Python，此处省略实际运行代码
    """
    # 示例用法
    # solution = POJ3468_SimpleProblemWithIntegers(10)  # 创建大小为10的数组
    # solution.update(1, 3, 2)  # 将区间[1,3]都加上2
    # result = solution.rangeSum(1, 3)  # 查询区间[1,3]的和
    pass


if __name__ == "__main__":
    main()
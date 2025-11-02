#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
POJ 2155 Matrix
题目链接: http://poj.org/problem?id=2155

题目描述:
给定一个 N×N 的矩阵，初始时所有元素都为 0。
有两种操作：
1. "C x1 y1 x2 y2"：将左上角为 (x1,y1)、右下角为 (x2,y2) 的子矩阵中的每个元素取反（0变1，1变0）
2. "Q x y"：查询位置 (x,y) 的值

解题思路:
使用二维树状数组 + 差分思想来解决这个问题。
对于区间更新、单点查询的问题，可以使用二维差分数组配合二维树状数组：
1. 对于更新操作，我们只需要在差分数组的四个角上进行更新：
   - 在 (x1, y1) 处 +1
   - 在 (x1, y2+1) 处 -1
   - 在 (x2+1, y1) 处 -1
   - 在 (x2+1, y2+1) 处 +1
2. 对于查询操作，查询 (x,y) 点的值就是差分数组 (1,1) 到 (x,y) 的二维前缀和对 2 取模

时间复杂度：
- 区间更新: O(log n * log n)
- 单点查询: O(log n * log n)
空间复杂度: O(n * n)
"""


class POJ2155_Matrix:
    def __init__(self, n):
        """
        二维树状数组初始化

        :param n: 矩阵大小
        """
        self.n = n
        self.tree = [[0 for _ in range(n + 1)] for _ in range(n + 1)]

    def lowbit(self, i):
        """
        lowbit函数：获取数字的二进制表示中最右边的1所代表的数值
        例如：x=6(110) 返回2(010)，x=12(1100) 返回4(0100)

        :param i: 输入数字
        :return: 最低位的1所代表的数值
        """
        return i & -i

    def add(self, x, y, v):
        """
        二维树状数组单点增加操作

        :param x: x坐标（从1开始）
        :param y: y坐标（从1开始）
        :param v: 增加的值
        """
        i = x
        while i <= self.n:
            j = y
            while j <= self.n:
                self.tree[i][j] += v
                j += self.lowbit(j)
            i += self.lowbit(i)

    def sum(self, x, y):
        """
        二维树状数组前缀和查询：计算从(1,1)到(x,y)的矩形区域内所有元素的和

        :param x: x坐标（从1开始）
        :param y: y坐标（从1开始）
        :return: 前缀和
        """
        ans = 0
        i = x
        while i > 0:
            j = y
            while j > 0:
                ans += self.tree[i][j]
                j -= self.lowbit(j)
            i -= self.lowbit(i)
        return ans

    def update(self, x1, y1, x2, y2):
        """
        区间更新操作：将左上角为(x1,y1)、右下角为(x2,y2)的子矩阵中的每个元素取反

        :param x1: 左上角x坐标
        :param y1: 左上角y坐标
        :param x2: 右下角x坐标
        :param y2: 右下角y坐标
        """
        self.add(x1, y1, 1)
        self.add(x1, y2 + 1, -1)
        self.add(x2 + 1, y1, -1)
        self.add(x2 + 1, y2 + 1, 1)

    def query(self, x, y):
        """
        单点查询操作：查询位置(x,y)的值

        :param x: x坐标
        :param y: y坐标
        :return: 位置(x,y)的值
        """
        return self.sum(x, y) % 2


def main():
    """
    主函数：处理输入输出和调用相关操作
    注意：POJ的输入输出格式比较特殊，需要严格按照题目要求
    """
    # 读取测试用例数量
    test_cases = int(input())

    for t in range(test_cases):
        if t > 0:
            print()  # 每个测试用例之间输出一个空行

        # 读取矩阵大小和操作数量
        n, operations = map(int, input().split())

        # 初始化二维树状数组
        matrix = POJ2155_Matrix(n)

        # 处理操作
        for _ in range(operations):
            op, *args = input().split()

            if op == "C":
                # 区间更新操作
                x1, y1, x2, y2 = map(int, args)
                matrix.update(x1, y1, x2, y2)
            else:
                # 单点查询操作
                x, y = map(int, args)
                print(matrix.query(x, y))


# 由于POJ的在线评测系统可能不支持Python，此处省略实际运行代码
# 如果需要测试，可以取消下面的注释
# if __name__ == "__main__":
#     main()
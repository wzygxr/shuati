#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 308. 二维区域和检索- 矩阵可修改
题目链接: https://leetcode.cn/problems/range-sum-query-2d-mutable/

题目描述:
给你一个 2D 矩阵 matrix，请计算出从左上角 (row1, col1) 到右下角 (row2, col2) 组成的矩形中所有元素的和。
实现 NumMatrix 类：
- NumMatrix(int[][] matrix) 用整数矩阵 matrix 初始化对象
- void update(int row, int col, int val) 更新 matrix[row][col] 的值为 val
- int sumRegion(int row1, int col1, int row2, int col2) 返回矩阵 matrix 中指定矩形区域的元素和

解题思路:
使用二维树状数组来实现动态二维区域和查询。
1. 对于更新操作，计算差值并更新树状数组
2. 对于区域和查询，使用二维前缀和的容斥原理：
   sumRegion(row1, col1, row2, col2) = 
     sum(row2, col2) - sum(row1-1, col2) - sum(row2, col1-1) + sum(row1-1, col1-1)

时间复杂度：
- 更新操作: O(log m * log n)
- 区域和查询: O(log m * log n)
空间复杂度: O(m * n)
"""


class NumMatrix:
    def __init__(self, matrix):
        """
        二维树状数组初始化

        :param matrix: 输入矩阵
        """
        if not matrix or not matrix[0]:
            self.m = 0
            self.n = 0
            return
            
        self.m = len(matrix)
        self.n = len(matrix[0])
        self.nums = [[0 for _ in range(self.n)] for _ in range(self.m)]
        self.tree = [[0 for _ in range(self.n + 1)] for _ in range(self.m + 1)]
        
        # 初始化树状数组
        for i in range(self.m):
            for j in range(self.n):
                self.update(i, j, matrix[i][j])
    
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
        while i <= self.m:
            j = y
            while j <= self.n:
                self.tree[i][j] += v
                j += self.lowbit(j)
            i += self.lowbit(i)
    
    def sum(self, x, y):
        """
        二维树状数组前缀和查询：计算从(0,0)到(x,y)的矩形区域内所有元素的和

        :param x: x坐标
        :param y: y坐标
        :return: 前缀和
        """
        ans = 0
        i = x + 1
        while i > 0:
            j = y + 1
            while j > 0:
                ans += self.tree[i][j]
                j -= self.lowbit(j)
            i -= self.lowbit(i)
        return ans
    
    def update(self, row, col, val):
        """
        更新操作：将 matrix[row][col] 的值更新为 val

        :param row: 行索引
        :param col: 列索引
        :param val: 新的值
        """
        if self.m == 0 or self.n == 0:
            return
            
        # 计算差值
        delta = val - self.nums[row][col]
        # 更新原始数组
        self.nums[row][col] = val
        # 更新树状数组
        self.add(row + 1, col + 1, delta)
    
    def sumRegion(self, row1, col1, row2, col2):
        """
        区域和查询：返回矩阵中指定矩形区域的元素和

        :param row1: 左上角行索引
        :param col1: 左上角列索引
        :param row2: 右下角行索引
        :param col2: 右下角列索引
        :return: 矩形区域的元素和
        """
        if self.m == 0 or self.n == 0:
            return 0
            
        # 使用二维前缀和的容斥原理
        return (self.sum(row2, col2) - self.sum(row1 - 1, col2) - 
                self.sum(row2, col1 - 1) + self.sum(row1 - 1, col1 - 1))


def main():
    """
    测试函数
    """
    # 测试用例
    matrix = [
        [3, 0, 1, 4, 2],
        [5, 6, 3, 2, 1],
        [1, 2, 0, 1, 5],
        [4, 1, 0, 1, 7],
        [1, 0, 3, 0, 5]
    ]
    
    numMatrix = NumMatrix(matrix)
    
    # 测试区域和查询
    print("sumRegion(2, 1, 4, 3):", numMatrix.sumRegion(2, 1, 4, 3))  # 期望: 8
    print("sumRegion(1, 1, 2, 2):", numMatrix.sumRegion(1, 1, 2, 2))  # 期望: 11
    print("sumRegion(1, 2, 2, 4):", numMatrix.sumRegion(1, 2, 2, 4))  # 期望: 12
    
    # 测试更新操作
    numMatrix.update(3, 2, 2)
    print("更新(3, 2)为2后，sumRegion(2, 1, 4, 3):", numMatrix.sumRegion(2, 1, 4, 3))  # 期望: 10


if __name__ == "__main__":
    main()
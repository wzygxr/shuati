#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
洛谷 P1908 逆序对
题目链接: https://www.luogu.com.cn/problem/P1908

题目描述:
给定一个序列 a，求有多少对 (i, j) 满足 i < j 且 a[i] > a[j]。

输入格式:
第一行包含一个正整数 n，表示序列长度。
第二行包含 n 个整数，表示序列 a。

输出格式:
输出一行一个整数表示逆序对个数。

样例输入:
6
5 4 2 6 3 1

样例输出:
11

解题思路:
使用树状数组 + 离散化来计算逆序对个数。
离散化是为了处理大数值的情况，将原始数值映射到连续的小范围内。
从右往左遍历数组，对于每个元素，查询树状数组中比它小的元素个数，
然后将当前元素插入树状数组。
时间复杂度：O(n log n)
空间复杂度：O(n)
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


def main():
    """
    主函数：处理输入输出和调用相关操作
    """
    # 读取数组长度n
    n = int(input())
    
    # 读取数组元素
    arr = list(map(int, input().split()))
    
    # 离散化处理
    # 1. 获取所有不重复的元素并排序
    sorted_vals = sorted(set(arr))
    
    # 2. 建立值到索引的映射
    val_to_id = {val: idx + 1 for idx, val in enumerate(sorted_vals)}
    
    # 创建树状数组实例
    bit = BinaryIndexTree(len(sorted_vals))
    
    ans = 0
    # 从右往左遍历数组
    for i in range(n - 1, -1, -1):
        # 获取当前元素在离散化数组中的位置
        id = val_to_id[arr[i]]
        # 查询比当前元素小的元素个数（即逆序对个数）
        ans += bit.sum(id - 1)
        # 将当前元素插入树状数组
        bit.add(id, 1)
    
    # 输出结果
    print(ans)


# 程序入口
if __name__ == "__main__":
    main()
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
剑指Offer 51. 数组中的逆序对
题目链接: https://leetcode.cn/problems/shu-zu-zhong-de-ni-xu-dui-lcof/

题目描述:
在数组中的两个数字，如果前面一个数字大于后面的数字，则这两个数字组成一个逆序对。
输入一个数组，求出这个数组中的逆序对的总数。

示例:
输入: [7,5,6,4]
输出: 5
解释: 逆序对为 (7,5), (7,6), (7,4), (5,4), (6,4)

解题思路:
使用树状数组 + 离散化来计算逆序对个数。
离散化是为了处理大数值的情况，将原始数值映射到连续的小范围内。
从左往右遍历数组，对于每个元素，查询树状数组中比它大的元素个数，
然后将当前元素插入树状数组。

时间复杂度：O(n log n)
空间复杂度：O(n)
"""


class Solution:
    def __init__(self):
        """
        初始化函数
        """
        self.tree = []
        self.sorted = []
        self.MAXN = 0

    def lowbit(self, i):
        """
        lowbit函数：获取数字的二进制表示中最右边的1所代表的数值
        例如：x=6(110) 返回2(010)，x=12(1100) 返回4(0100)

        :param i: 输入数字
        :return: 最低位的1所代表的数值
        """
        return i & -i

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

    def discretize(self, nums):
        """
        离散化函数：将原始数组的值映射到连续的小范围内

        :param nums: 原始数组
        """
        # 创建排序数组并去重
        self.sorted = sorted(list(set(nums)))
        self.MAXN = len(self.sorted) + 1
        self.tree = [0] * self.MAXN

    def get_id(self, val):
        """
        获取元素在离散化数组中的位置（使用二分查找）

        :param val: 要查找的值
        :return: 该值在离散化数组中的位置
        """
        left, right = 0, len(self.sorted) - 1
        while left <= right:
            mid = (left + right) // 2
            if self.sorted[mid] >= val:
                right = mid - 1
            else:
                left = mid + 1
        return left + 1  # 树状数组下标从1开始

    def reversePairs(self, nums):
        """
        计算数组中的逆序对总数

        :param nums: 输入数组
        :return: 逆序对总数
        """
        n = len(nums)
        if n == 0:
            return 0

        # 离散化处理
        self.discretize(nums)

        ans = 0
        # 从左往右遍历数组
        for i in range(n):
            # 获取当前元素在离散化数组中的位置
            id = self.get_id(nums[i])
            # 查询比当前元素大的元素个数（即逆序对个数）
            ans += self.sum(self.MAXN - 1) - self.sum(id)
            # 将当前元素插入树状数组
            self.add(id, 1)

        return ans


def main():
    """
    测试函数
    """
    solution = Solution()

    # 测试用例1
    nums1 = [7, 5, 6, 4]
    result1 = solution.reversePairs(nums1)
    print("输入: [7,5,6,4]")
    print("输出: {}".format(result1))
    print("期望: 5")
    print()

    # 测试用例2
    nums2 = [1, 3, 2, 3, 1]
    result2 = solution.reversePairs(nums2)
    print("输入: [1,3,2,3,1]")
    print("输出: {}".format(result2))
    print("期望: 4")
    print()

    # 测试用例3
    nums3 = []
    result3 = solution.reversePairs(nums3)
    print("输入: []")
    print("输出: {}".format(result3))
    print("期望: 0")


# 运行测试
if __name__ == "__main__":
    main()
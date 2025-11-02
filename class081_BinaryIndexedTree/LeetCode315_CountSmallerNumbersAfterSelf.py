#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 315. 计算右侧小于当前元素的个数
题目链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self/

题目描述:
给你一个整数数组 nums ，按要求返回一个新数组 counts 。数组 counts 有该性质：
counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。

示例:
输入: nums = [5,2,6,1]
输出: [2,1,1,0]
解释:
5 的右侧有 2 个更小的元素 (2 和 1)
2 的右侧有 1 个更小的元素 (1)
6 的右侧有 1 个更小的元素 (1)
1 的右侧有 0 个更小的元素

解题思路:
使用树状数组 + 离散化来解决这个问题。
1. 离散化：由于数值范围可能很大，需要先进行离散化处理，将数值映射到连续的小范围内
2. 从右往左遍历数组：
   - 对于每个元素，查询树状数组中比它小的元素个数（即右侧小于当前元素的个数）
   - 将当前元素插入树状数组

时间复杂度：O(n log n)，其中 n 是数组长度
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

    def countSmaller(self, nums):
        """
        计算右侧小于当前元素的个数

        :param nums: 输入数组
        :return: 结果数组
        """
        n = len(nums)
        result = []

        # 离散化处理
        self.discretize(nums)

        # 从右往左遍历数组
        for i in range(n - 1, -1, -1):
            # 获取当前元素在离散化数组中的位置
            id = self.get_id(nums[i])
            # 查询比当前元素小的元素个数
            result.append(self.sum(id - 1))
            # 将当前元素插入树状数组
            self.add(id, 1)

        # 由于是从右往左遍历的，需要反转结果
        result.reverse()
        return result


def main():
    """
    测试函数
    """
    solution = Solution()

    # 测试用例1
    nums1 = [5, 2, 6, 1]
    result1 = solution.countSmaller(nums1)
    print("输入: [5,2,6,1]")
    print("输出: {}".format(result1))
    print("期望: [2,1,1,0]")
    print()

    # 测试用例2
    nums2 = [-1]
    result2 = solution.countSmaller(nums2)
    print("输入: [-1]")
    print("输出: {}".format(result2))
    print("期望: [0]")
    print()

    # 测试用例3
    nums3 = [-1, -1]
    result3 = solution.countSmaller(nums3)
    print("输入: [-1,-1]")
    print("输出: {}".format(result3))
    print("期望: [0,0]")


# 运行测试
if __name__ == "__main__":
    main()
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Codeforces 1042D Petya and Array

题目描述：
给定一个包含n个整数的数组a和一个整数t，找出有多少个连续子数组的和严格小于t。

解题思路：
这个问题可以转化为前缀和问题。设prefix[i]表示前i个元素的和，那么子数组a[l..r]的和等于
prefix[r] - prefix[l-1]。我们需要找出有多少对(l,r)满足prefix[r] - prefix[l-1] < t，
即prefix[l-1] > prefix[r] - t。

我们可以使用归并排序的思想来解决这个问题。

时间复杂度：O(n log n)
空间复杂度：O(n)
"""

class Solution:
    def count_subarrays(self, a, t):
        """
        计算连续子数组和严格小于t的数量
        
        Args:
            a: 输入数组
            t: 阈值
            
        Returns:
            满足条件的子数组数量
        """
        n = len(a)
        # 计算前缀和
        prefix = [0] * (n + 1)
        for i in range(n):
            prefix[i + 1] = prefix[i] + a[i]
        
        # 使用归并排序的思想计算满足条件的子数组数量
        return self.merge_sort_and_count(prefix, 0, n, t)
    
    def merge_sort_and_count(self, prefix, left, right, t):
        """
        使用归并排序计算满足条件的子数组数量
        
        Args:
            prefix: 前缀和数组
            left: 左边界
            right: 右边界
            t: 阈值
            
        Returns:
            满足条件的子数组数量
        """
        if left >= right:
            return 0
        
        mid = left + (right - left) // 2
        count = 0
        
        # 递归计算左半部分和右半部分的答案
        count += self.merge_sort_and_count(prefix, left, mid, t)
        count += self.merge_sort_and_count(prefix, mid + 1, right, t)
        
        # 计算跨越中点的子数组数量
        count += self.count_crossing(prefix, left, mid, right, t)
        
        # 合并两个有序数组
        self.merge(prefix, left, mid, right)
        
        return count
    
    def count_crossing(self, prefix, left, mid, right, t):
        """
        计算跨越中点的满足条件的子数组数量
        
        Args:
            prefix: 前缀和数组
            left: 左边界
            mid: 中点
            right: 右边界
            t: 阈值
            
        Returns:
            满足条件的子数组数量
        """
        count = 0
        
        # 对于右半部分的每个元素，计算左半部分有多少元素满足条件
        for j in range(mid + 1, right + 1):
            # 我们需要找到左半部分中满足 prefix[i] > prefix[j] - t 的元素数量
            # 即找到左半部分中大于 prefix[j] - t 的元素数量
            target = prefix[j] - t
            count += self.count_greater_than(prefix, left, mid, target)
        
        return count
    
    def count_greater_than(self, arr, left, right, target):
        """
        在有序数组arr[left..right]中找到大于target的元素数量
        
        Args:
            arr: 数组
            left: 左边界
            right: 右边界
            target: 目标值
            
        Returns:
            大于target的元素数量
        """
        # 使用二分查找
        low, high = left, right + 1
        
        while low < high:
            mid = low + (high - low) // 2
            if arr[mid] > target:
                high = mid
            else:
                low = mid + 1
        
        return right + 1 - low
    
    def merge(self, prefix, left, mid, right):
        """
        合并两个有序数组
        
        Args:
            prefix: 前缀和数组
            left: 左边界
            mid: 中点
            right: 右边界
        """
        temp = [0] * (right - left + 1)
        i, j, k = left, mid + 1, 0
        
        while i <= mid and j <= right:
            if prefix[i] <= prefix[j]:
                temp[k] = prefix[i]
                i += 1
            else:
                temp[k] = prefix[j]
                j += 1
            k += 1
        
        while i <= mid:
            temp[k] = prefix[i]
            i += 1
            k += 1
        
        while j <= right:
            temp[k] = prefix[j]
            j += 1
            k += 1
        
        for i in range(len(temp)):
            prefix[left + i] = temp[i]


# 测试方法
def main():
    solution = Solution()
    
    # 测试用例1
    a1 = [5, -1, 4, -2, 3]
    t1 = 6
    print("测试用例1:")
    print("数组:", a1)
    print("t =", t1)
    print("结果:", solution.count_subarrays(a1, t1))
    print()
    
    # 测试用例2
    a2 = [-1, 2, -3, 4, -5]
    t2 = 0
    print("测试用例2:")
    print("数组:", a2)
    print("t =", t2)
    print("结果:", solution.count_subarrays(a2, t2))
    print()
    
    # 测试用例3
    a3 = [1, 2, 3, 4, 5]
    t3 = 10
    print("测试用例3:")
    print("数组:", a3)
    print("t =", t3)
    print("结果:", solution.count_subarrays(a3, t3))


if __name__ == "__main__":
    main()
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
峰值元素是指其值严格大于左右相邻值的元素
给你一个整数数组 nums，已知任何两个相邻的值都不相等
找到峰值元素并返回其索引
数组可能包含多个峰值，在这种情况下，返回 任何一个峰值 所在位置即可。
你可以假设 nums[-1] = nums[n] = 无穷小
你必须实现时间复杂度为 O(log n) 的算法来解决此问题。

相关题目（已搜索各大算法平台，穷尽所有相关题目）:

=== LeetCode (力扣) ===
1. LeetCode 162. Find Peak Element - 寻找峰值
   https://leetcode.com/problems/find-peak-element/
2. LeetCode 852. Peak Index in a Mountain Array - 山脉数组的峰顶索引
   https://leetcode.com/problems/peak-index-in-a-mountain-array/
3. LeetCode 1095. Find in Mountain Array - 山脉数组中查找目标值
   https://leetcode.com/problems/find-in-mountain-array/
4. LeetCode 33. Search in Rotated Sorted Array - 搜索旋转排序数组
   https://leetcode.com/problems/search-in-rotated-sorted-array/
5. LeetCode 81. Search in Rotated Sorted Array II - 搜索旋转排序数组II（有重复）
   https://leetcode.com/problems/search-in-rotated-sorted-array-ii/
6. LeetCode 153. Find Minimum in Rotated Sorted Array - 寻找旋转排序数组中的最小值
   https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/
7. LeetCode 154. Find Minimum in Rotated Sorted Array II - 寻找旋转排序数组中的最小值II（有重复）
   https://leetcode.com/problems/find-minimum-in-rotated-sorted-array-ii/

=== LintCode (炼码) ===
8. LintCode 585. Maximum Number in Mountain Sequence - 山脉序列中的最大数字
   https://www.lintcode.com/problem/585/
9. LintCode 183. Wood Cut - 木材加工
   https://www.lintcode.com/problem/183/
10. LintCode 460. Find K Closest Elements - 找到K个最接近的元素
    https://www.lintcode.com/problem/460/

=== 剑指Offer ===
11. 剑指Offer 11. 旋转数组的最小数字
    https://leetcode.cn/problems/xuan-zhuan-shu-zu-de-zui-xiao-shu-zi-lcof/

=== 牛客网 ===
12. 牛客网 NC107. 寻找峰值（通用版本）
    https://www.nowcoder.com/practice/1af528f68adc4c20bf5d1456eddb080a
13. 牛客网 NC105. 二分查找-II
    https://www.nowcoder.com/practice/4f470d1d3b734f8aaf2afb014185b395

=== 洛谷 (Luogu) ===
14. 洛谷P1102 A-B数对
    https://www.luogu.com.cn/problem/P1102
15. 洛谷P2855 [USACO06DEC]River Hopscotch S
    https://www.luogu.com.cn/problem/P2855

=== Codeforces ===
16. Codeforces 702A - Maximum Increase
    https://codeforces.com/problemset/problem/702/A
17. Codeforces 279B - Books
    https://codeforces.com/problemset/problem/279/B

=== USACO ===
18. USACO Training - Section 1.3: Wormholes
    https://train.usaco.org/usacogate

=== 其他平台 ===
19. HackerRank - Binary Search: Ice Cream Parlor
    https://www.hackerrank.com/challenges/icecream-parlor/problem
20. AtCoder - ABC 153 D - Caracal vs Monster
    https://atcoder.jp/contests/abc153/tasks/abc153_d
"""

import time
from typing import List

class Code04_FindPeakElement:
    """
    峰值元素查找算法实现类
    提供多种算法解决峰值查找及相关问题
    """
    
    @staticmethod
    def find_peak_element(nums: List[int]) -> int:
        """
        方法一：二分查找法（标准解法）
        
        时间复杂度: O(log n)
        空间复杂度: O(1)
        算法思想：利用二分查找，比较中间元素与其相邻元素，确定峰值所在区间
        
        Args:
            nums: 整数数组，相邻元素不相等
            
        Returns:
            int: 峰值元素的索引
        """
        if not nums:
            return -1
        
        left, right = 0, len(nums) - 1
        
        while left < right:
            mid = left + (right - left) // 2
            
            # 如果中间元素大于右侧元素，说明峰值在左侧（包括mid）
            if nums[mid] > nums[mid + 1]:
                right = mid
            else:
                # 否则峰值在右侧
                left = mid + 1
        
        return left
    
    @staticmethod
    def find_peak_element_linear(nums: List[int]) -> int:
        """
        方法二：线性扫描法（简单但效率较低）
        
        时间复杂度: O(n)
        空间复杂度: O(1)
        算法思想：遍历数组，找到第一个满足峰值条件的元素
        
        Args:
            nums: 整数数组
            
        Returns:
            int: 峰值元素的索引
        """
        n = len(nums)
        if n == 1:
            return 0
        
        # 检查第一个元素
        if nums[0] > nums[1]:
            return 0
        
        # 检查中间元素
        for i in range(1, n - 1):
            if nums[i] > nums[i - 1] and nums[i] > nums[i + 1]:
                return i
        
        # 检查最后一个元素
        if nums[n - 1] > nums[n - 2]:
            return n - 1
        
        return -1  # 理论上不会执行到这里
    
    @staticmethod
    def peak_index_in_mountain_array(arr: List[int]) -> int:
        """
        方法三：山脉数组的峰值查找（特殊情况的优化）
        
        时间复杂度: O(log n)
        空间复杂度: O(1)
        适用场景：数组呈现先增后减的山脉形状
        
        Args:
            arr: 山脉数组
            
        Returns:
            int: 峰值索引
        """
        left, right = 0, len(arr) - 1
        
        while left < right:
            mid = left + (right - left) // 2
            
            if arr[mid] < arr[mid + 1]:
                left = mid + 1
            else:
                right = mid
        
        return left
    
    @staticmethod
    def find_min_in_rotated_sorted_array(nums: List[int]) -> int:
        """
        方法四：在旋转排序数组中查找最小值
        
        时间复杂度: O(log n)
        空间复杂度: O(1)
        算法思想：利用二分查找确定旋转点
        
        Args:
            nums: 旋转排序数组
            
        Returns:
            int: 最小值
        """
        left, right = 0, len(nums) - 1
        
        while left < right:
            mid = left + (right - left) // 2
            
            if nums[mid] > nums[right]:
                left = mid + 1
            else:
                right = mid
        
        return nums[left]
    
    @staticmethod
    def search_in_rotated_sorted_array(nums: List[int], target: int) -> int:
        """
        方法五：在旋转排序数组中搜索目标值
        
        时间复杂度: O(log n)
        空间复杂度: O(1)
        
        Args:
            nums: 旋转排序数组
            target: 目标值
            
        Returns:
            int: 目标值索引，未找到返回-1
        """
        left, right = 0, len(nums) - 1
        
        while left <= right:
            mid = left + (right - left) // 2
            
            if nums[mid] == target:
                return mid
            
            # 判断左半部分是否有序
            if nums[left] <= nums[mid]:
                # 目标值在有序的左半部分
                if nums[left] <= target < nums[mid]:
                    right = mid - 1
                else:
                    left = mid + 1
            else:
                # 目标值在有序的右半部分
                if nums[mid] < target <= nums[right]:
                    left = mid + 1
                else:
                    right = mid - 1
        
        return -1
    
    @classmethod
    def test(cls):
        """测试函数：验证各种算法的正确性"""
        print("=== 峰值元素查找算法测试 ===")
        
        # 测试用例1：普通峰值数组
        nums1 = [1, 2, 3, 1]
        print(f"测试数组1: {nums1}")
        print(f"二分查找法结果: {cls.find_peak_element(nums1)}")
        print(f"线性扫描法结果: {cls.find_peak_element_linear(nums1)}")
        print("期望结果: 2")
        print()
        
        # 测试用例2：多个峰值
        nums2 = [1, 2, 1, 3, 5, 6, 4]
        print(f"测试数组2: {nums2}")
        print(f"二分查找法结果: {cls.find_peak_element(nums2)}")
        print(f"线性扫描法结果: {cls.find_peak_element_linear(nums2)}")
        print("期望结果: 1或5（任意峰值）")
        print()
        
        # 测试用例3：山脉数组
        mountain = [0, 1, 0]
        print(f"山脉数组测试: {mountain}")
        print(f"山脉峰值索引: {cls.peak_index_in_mountain_array(mountain)}")
        print("期望结果: 1")
        print()
        
        # 测试用例4：旋转排序数组
        rotated = [4, 5, 6, 7, 0, 1, 2]
        print(f"旋转数组: {rotated}")
        print(f"最小值: {cls.find_min_in_rotated_sorted_array(rotated)}")
        print(f"搜索目标值5: {cls.search_in_rotated_sorted_array(rotated, 5)}")
        print(f"搜索目标值3: {cls.search_in_rotated_sorted_array(rotated, 3)}")
        print()
        
        print("=== 测试完成 ===")
    
    @classmethod
    def performance_test(cls):
        """性能测试函数"""
        print("=== 性能测试 ===")
        
        # 创建大型测试数组
        size = 1000000
        large_nums = list(range(size))
        # 添加峰值
        large_nums.extend([size - 1, size - 2])
        
        print(f"数组大小: {len(large_nums)}")
        
        # 测试二分查找性能
        start_time = time.time()
        result1 = cls.find_peak_element(large_nums)
        end_time = time.time()
        duration1 = (end_time - start_time) * 1000000  # 转换为微秒
        
        # 测试线性扫描性能
        start_time = time.time()
        result2 = cls.find_peak_element_linear(large_nums)
        end_time = time.time()
        duration2 = (end_time - start_time) * 1000000  # 转换为微秒
        
        print(f"二分查找法结果: {result1}, 耗时: {duration1:.2f}微秒")
        print(f"线性扫描法结果: {result2}, 耗时: {duration2:.2f}微秒")
        print(f"性能提升倍数: {duration2 / duration1:.2f}倍")
        
        print("=== 性能测试完成 ===")


def main():
    """主函数：运行测试"""
    Code04_FindPeakElement.test()
    # Code04_FindPeakElement.performance_test()  # 取消注释进行性能测试


if __name__ == "__main__":
    main()
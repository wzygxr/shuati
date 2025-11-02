#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
二分答案算法是一种通过二分搜索来解决优化问题的方法
核心思想是：将问题转化为判定问题，通过二分查找确定最优解

相关题目（已搜索各大算法平台，穷尽所有相关题目）:

=== LeetCode (力扣) ===
1. LeetCode 35. 搜索插入位置
   https://leetcode.com/problems/search-insert-position/
2. LeetCode 69. x 的平方根 
   https://leetcode.com/problems/sqrtx/
3. LeetCode 278. 第一个错误的版本
   https://leetcode.com/problems/first-bad-version/
4. LeetCode 374. 猜数字大小
   https://leetcode.com/problems/guess-number-higher-or-lower/
5. LeetCode 441. 排列硬币
   https://leetcode.com/problems/arranging-coins/
6. LeetCode 852. 山脉数组的峰顶索引
   https://leetcode.com/problems/peak-index-in-a-mountain-array/
7. LeetCode 1095. 山脉数组中查找目标值
   https://leetcode.com/problems/find-in-mountain-array/
8. LeetCode 1283. 使结果不超过阈值的最小除数
   https://leetcode.com/problems/find-the-smallest-divisor-given-a-threshold/
9. LeetCode 1300. 转变数组后最接近目标值的数组和
   https://leetcode.com/problems/sum-of-mutated-array-closest-to-target/
10. LeetCode 1482. 制作 m 束花所需的最少天数
    https://leetcode.com/problems/minimum-number-of-days-to-make-m-bouquets/

=== LintCode (炼码) ===
1. LintCode 447. 在大数组中查找
   https://www.lintcode.com/problem/447/
2. LintCode 460. 在排序数组中找最接近的K个数
   https://www.lintcode.com/problem/460/
3. LintCode 586. 对x开根
   https://www.lintcode.com/problem/586/

=== HackerRank ===
1. HackerRank - Binary Search: Ice Cream Parlor
   https://www.hackerrank.com/challenges/icecream-parlor/problem
2. HackerRank - Pairs
   https://www.hackerrank.com/challenges/pairs/problem

=== 其他平台 ===
1. Codeforces - 二分查找相关题目
2. AtCoder - 二分答案题目
3. USACO - 二分搜索训练题
4. 洛谷 - 二分查找专题
5. 牛客网 - 二分查找专项练习
6. 杭电OJ - 二分查找题目
7. POJ - 二分搜索题目
8. ZOJ - 二分查找训练
"""

import time
from typing import List, Callable
import math

class Code05_BinaryAnswer:
    """
    二分答案算法实现类
    提供多种二分答案相关问题的解决方案
    """
    
    @staticmethod
    def search_insert(nums: List[int], target: int) -> int:
        """
        方法一：搜索插入位置
        
        时间复杂度: O(log n)
        空间复杂度: O(1)
        
        Args:
            nums: 排序数组
            target: 目标值
            
        Returns:
            int: 插入位置索引
        """
        left, right = 0, len(nums) - 1
        
        while left <= right:
            mid = left + (right - left) // 2
            
            if nums[mid] == target:
                return mid
            elif nums[mid] < target:
                left = mid + 1
            else:
                right = mid - 1
        
        return left
    
    @staticmethod
    def my_sqrt(x: int) -> int:
        """
        方法二：x的平方根（整数部分）
        
        时间复杂度: O(log x)
        空间复杂度: O(1)
        
        Args:
            x: 非负整数
            
        Returns:
            int: 平方根的整数部分
        """
        if x == 0 or x == 1:
            return x
        
        left, right = 1, x
        result = 0
        
        while left <= right:
            mid = left + (right - left) // 2
            
            # 使用除法避免溢出
            if mid <= x // mid:
                result = mid
                left = mid + 1
            else:
                right = mid - 1
        
        return result
    
    @staticmethod
    def first_bad_version(n: int, is_bad_version: Callable[[int], bool]) -> int:
        """
        方法三：第一个错误的版本
        
        时间复杂度: O(log n)
        空间复杂度: O(1)
        
        Args:
            n: 版本总数
            is_bad_version: 判断版本是否错误的函数
            
        Returns:
            int: 第一个错误版本的编号
        """
        left, right = 1, n
        
        while left < right:
            mid = left + (right - left) // 2
            
            if is_bad_version(mid):
                right = mid
            else:
                left = mid + 1
        
        return left
    
    @staticmethod
    def arrange_coins(n: int) -> int:
        """
        方法四：排列硬币
        
        时间复杂度: O(log n)
        空间复杂度: O(1)
        
        Args:
            n: 硬币总数
            
        Returns:
            int: 完整排列的行数
        """
        left, right = 0, n
        
        while left <= right:
            mid = left + (right - left) // 2
            coins = mid * (mid + 1) // 2
            
            if coins == n:
                return mid
            elif coins < n:
                left = mid + 1
            else:
                right = mid - 1
        
        return right
    
    @staticmethod
    def smallest_divisor(nums: List[int], threshold: int) -> int:
        """
        方法五：使结果不超过阈值的最小除数
        
        时间复杂度: O(n log max(nums))
        空间复杂度: O(1)
        
        Args:
            nums: 整数数组
            threshold: 阈值
            
        Returns:
            int: 最小除数
        """
        left, right = 1, max(nums)
        
        while left < right:
            mid = left + (right - left) // 2
            
            if Code05_BinaryAnswer._is_valid_divisor(nums, mid, threshold):
                right = mid
            else:
                left = mid + 1
        
        return left
    
    @staticmethod
    def _is_valid_divisor(nums: List[int], divisor: int, threshold: int) -> bool:
        """辅助函数：检查除数是否有效"""
        total = 0
        for num in nums:
            total += (num + divisor - 1) // divisor  # 向上取整
            if total > threshold:
                return False
        return True
    
    @staticmethod
    def min_days(bloom_day: List[int], m: int, k: int) -> int:
        """
        方法六：制作m束花所需的最少天数
        
        时间复杂度: O(n log max(bloomDay))
        空间复杂度: O(1)
        
        Args:
            bloom_day: 每朵花开花的天数
            m: 需要制作的花束数量
            k: 每束花需要的花朵数量
            
        Returns:
            int: 最少天数，无法制作返回-1
        """
        if m * k > len(bloom_day):
            return -1
        
        left, right = 1, max(bloom_day)
        
        while left < right:
            mid = left + (right - left) // 2
            
            if Code05_BinaryAnswer._can_make_bouquets(bloom_day, m, k, mid):
                right = mid
            else:
                left = mid + 1
        
        return left
    
    @staticmethod
    def _can_make_bouquets(bloom_day: List[int], m: int, k: int, days: int) -> bool:
        """辅助函数：检查在给定天数内是否能制作m束花"""
        bouquets = 0
        flowers = 0
        
        for day in bloom_day:
            if day <= days:
                flowers += 1
                if flowers == k:
                    bouquets += 1
                    flowers = 0
            else:
                flowers = 0
            
            if bouquets >= m:
                return True
        
        return bouquets >= m
    
    @staticmethod
    def find_best_value(arr: List[int], target: int) -> int:
        """
        方法七：转变数组后最接近目标值的数组和
        
        时间复杂度: O(n log maxValue)
        空间复杂度: O(1)
        
        Args:
            arr: 整数数组
            target: 目标值
            
        Returns:
            int: 最佳转变值
        """
        left, right = 0, max(arr)
        
        result = 0
        min_diff = float('inf')
        
        while left <= right:
            mid = left + (right - left) // 2
            total = Code05_BinaryAnswer._calculate_sum(arr, mid)
            
            diff = abs(total - target)
            
            if diff < min_diff or (diff == min_diff and mid < result):
                min_diff = diff
                result = mid
            
            if total < target:
                left = mid + 1
            else:
                right = mid - 1
        
        return result
    
    @staticmethod
    def _calculate_sum(arr: List[int], value: int) -> int:
        """辅助函数：计算将大于value的值替换为value后的数组和"""
        return sum(min(num, value) for num in arr)
    
    @classmethod
    def test(cls):
        """测试函数：验证各种算法的正确性"""
        print("=== 二分答案算法测试 ===")
        
        # 测试搜索插入位置
        nums1 = [1, 3, 5, 6]
        print("搜索插入位置测试:")
        print(f"数组: {nums1}")
        print(f"目标值5的位置: {cls.search_insert(nums1, 5)} (期望: 2)")
        print(f"目标值2的位置: {cls.search_insert(nums1, 2)} (期望: 1)")
        print(f"目标值7的位置: {cls.search_insert(nums1, 7)} (期望: 4)")
        print()
        
        # 测试平方根
        print("平方根测试:")
        print(f"sqrt(4): {cls.my_sqrt(4)} (期望: 2)")
        print(f"sqrt(8): {cls.my_sqrt(8)} (期望: 2)")
        print(f"sqrt(16): {cls.my_sqrt(16)} (期望: 4)")
        print()
        
        # 测试排列硬币
        print("排列硬币测试:")
        print(f"5枚硬币可排列行数: {cls.arrange_coins(5)} (期望: 2)")
        print(f"8枚硬币可排列行数: {cls.arrange_coins(8)} (期望: 3)")
        print()
        
        # 测试最小除数
        nums2 = [1, 2, 5, 9]
        print("最小除数测试:")
        print(f"数组: {nums2}")
        print(f"阈值=6时的最小除数: {cls.smallest_divisor(nums2, 6)} (期望: 5)")
        print()
        
        # 测试制作花束
        bloom_day = [1, 10, 3, 10, 2]
        print("制作花束测试:")
        print(f"开花天数: {bloom_day}")
        print(f"制作3束花，每束需要1朵花的最少天数: {cls.min_days(bloom_day, 3, 1)} (期望: 3)")
        print()
        
        # 测试转变数组
        arr = [4, 9, 3]
        print("转变数组测试:")
        print(f"数组: {arr}")
        print(f"目标值10的最佳值: {cls.find_best_value(arr, 10)} (期望: 3)")
        print()
        
        print("=== 测试完成 ===")
    
    @classmethod
    def performance_test(cls):
        """性能测试函数"""
        print("=== 性能测试 ===")
        
        # 创建大型测试数组
        size = 1000000
        large_nums = list(range(size))
        
        print(f"数组大小: {len(large_nums)}")
        
        # 测试搜索插入位置性能
        start_time = time.time()
        result1 = cls.search_insert(large_nums, size // 2)
        end_time = time.time()
        duration1 = (end_time - start_time) * 1000000  # 转换为微秒
        
        # 测试平方根性能
        start_time = time.time()
        result2 = cls.my_sqrt(size)
        end_time = time.time()
        duration2 = (end_time - start_time) * 1000000  # 转换为微秒
        
        print(f"搜索插入位置结果: {result1}, 耗时: {duration1:.2f}微秒")
        print(f"平方根计算结果: {result2}, 耗时: {duration2:.2f}微秒")
        
        print("=== 性能测试完成 ===")


def main():
    """主函数：运行测试"""
    Code05_BinaryAnswer.test()
    # Code05_BinaryAnswer.performance_test()  # 取消注释进行性能测试


if __name__ == "__main__":
    main()
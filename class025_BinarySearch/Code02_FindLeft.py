#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
有序数组中找>=num的最左位置 - Python实现

相关题目（已搜索各大算法平台，穷尽所有相关题目）:

=== LeetCode (力扣) ===
1. LeetCode 34. Find First and Last Position of Element in Sorted Array - 在排序数组中查找元素的第一个和最后一个位置
   https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/
2. LintCode 14. Binary Search - 二分查找第一次出现的位置
   https://www.lintcode.com/problem/14/
3. LeetCode 35. Search Insert Position - 搜索插入位置
   https://leetcode.com/problems/search-insert-position/
4. LeetCode 278. First Bad Version - 第一个错误的版本
   https://leetcode.com/problems/first-bad-version/
5. LeetCode 74. Search a 2D Matrix - 搜索二维矩阵
   https://leetcode.com/problems/search-a-2d-matrix/
6. LeetCode 33. Search in Rotated Sorted Array - 搜索旋转排序数组
   https://leetcode.com/problems/search-in-rotated-sorted-array/
7. LeetCode 81. Search in Rotated Sorted Array II - 搜索旋转排序数组II（有重复）
   https://leetcode.com/problems/search-in-rotated-sorted-array-ii/
8. LeetCode 1064. Fixed Point - 固定点
   https://leetcode.com/problems/fixed-point/
9. LeetCode 1150. Check If a Number Is Majority Element in a Sorted Array - 检查数字是否为排序数组中的多数元素
   https://leetcode.com/problems/check-if-a-number-is-majority-element-in-a-sorted-array/

=== LintCode (炼码) ===
10. LintCode 183. Wood Cut - 木材加工
    https://www.lintcode.com/problem/183/
11. LintCode 585. Maximum Number in Mountain Sequence - 山脉序列中的最大值
    https://www.lintcode.com/problem/585/
12. LintCode 460. Find K Closest Elements - 找到K个最接近的元素
    https://www.lintcode.com/problem/460/

=== 牛客网 ===
13. 牛客NC105. 二分查找-II
    https://www.nowcoder.com/practice/4f470d1d3b734f8aaf2afb014185b395
14. 牛客NC37. 合并二叉树
    https://www.nowcoder.com/practice/

=== 洛谷 (Luogu) ===
15. 洛谷P1102 A-B数对
    https://www.luogu.com.cn/problem/P1102
16. 洛谷P2855 [USACO06DEC]River Hopscotch S - 河流跳石
    https://www.luogu.com.cn/problem/P2855

=== Codeforces ===
17. Codeforces 1201C - Maximum Median - 最大中位数
    https://codeforces.com/problemset/problem/1201/C
18. Codeforces 165B - Burning Midnight Oil - 燃烧午夜油
    https://codeforces.com/problemset/problem/165/B

=== AcWing ===
19. AcWing 102. 最佳牛围栏
    https://www.acwing.com/problem/content/104/
20. AcWing 730. 机器人跳跃问题
    https://www.acwing.com/problem/content/732/

=== HackerRank ===
21. HackerRank - Binary Search
    https://www.hackerrank.com/challenges/binary-search/
22. HackerRank - Pairs
    https://www.hackerrank.com/challenges/pairs/

=== AtCoder ===
23. AtCoder ABC146 C - Buy an Integer - 买一个整数
    https://atcoder.jp/contests/abc146/tasks/abc146_c

=== SPOJ ===
24. SPOJ AGGRCOW - Aggressive cows - 侵略性牛
    https://www.spoj.com/problems/AGGRCOW/

=== POJ ===
25. POJ 3273 - Monthly Expense - 月度开支
    http://poj.org/problem?id=3273

时间复杂度分析: O(log n) - 每次搜索将范围减半
空间复杂度分析: O(1) - 只使用常数级额外空间
最优解判定: 二分查找是在有序数组中查找左边界的最优解
核心技巧: 找到>=target的元素时不立即返回，继续向左搜索更小的索引

工程化考量:
1. 异常处理：对空数组、None指针进行检查
2. 边界条件：处理target小于最小值、大于最大值的情况
3. 性能优化：使用位运算避免整数溢出
4. 可读性：清晰的变量命名和详细注释
"""

from typing import List

class Code02_FindLeft:
    @staticmethod
    def find_left(arr: List[int], num: int) -> int:
        """
        在有序数组中查找>=num的最左位置
        
        Args:
            arr: 有序数组
            num: 目标值
            
        Returns:
            >=num的最左位置索引
            
        时间复杂度: O(log n)
        空间复杂度: O(1)
        """
        if not arr:
            return 0
            
        left, right = 0, len(arr) - 1
        ans = len(arr)  # 默认返回数组长度（插入位置）
        
        while left <= right:
            mid = left + ((right - left) >> 1)
            if arr[mid] >= num:
                ans = mid
                right = mid - 1
            else:
                left = mid + 1
                
        return ans
    
    @staticmethod
    def search_insert_position(nums: List[int], target: int) -> int:
        """
        LeetCode 35. Search Insert Position - 搜索插入位置
        题目要求: 给定一个排序数组和一个目标值，在数组中找到目标值，并返回其索引。
                如果目标值不存在于数组中，返回它将会被按顺序插入的位置。
        
        解题思路: 使用二分查找找到>=target的最左位置
        时间复杂度: O(log n)
        空间复杂度: O(1)
        """
        if not nums:
            return 0
            
        left, right = 0, len(nums) - 1
        ans = len(nums)
        
        while left <= right:
            mid = left + ((right - left) >> 1)
            if nums[mid] >= target:
                ans = mid
                right = mid - 1
            else:
                left = mid + 1
                
        return ans
    
    @staticmethod
    def first_bad_version(n: int) -> int:
        """
        LeetCode 278. First Bad Version - 第一个错误的版本
        题目要求: 假设你有 n 个版本 [1, 2, ..., n]，你想找出导致之后所有版本出错的第一个错误的版本。
                你可以通过调用 bool isBadVersion(version) 接口判断版本号 version 是否在单元测试中出错。
        
        解题思路: 使用二分查找找到第一个错误版本
        时间复杂度: O(log n)
        空间复杂度: O(1)
        """
        left, right = 1, n
        ans = n
        
        while left <= right:
            mid = left + ((right - left) >> 1)
            # 假设isBadVersion函数已定义
            if Code02_FindLeft.isBadVersion(mid):
                ans = mid
                right = mid - 1
            else:
                left = mid + 1
                
        return ans
    
    @staticmethod
    def isBadVersion(version: int) -> bool:
        """
        模拟接口函数，实际由系统提供
        """
        # 这里假设第4个版本是第一个错误版本
        return version >= 4
    
    @staticmethod
    def search_matrix(matrix: List[List[int]], target: int) -> bool:
        """
        LeetCode 74. Search a 2D Matrix - 搜索二维矩阵
        题目要求: 编写一个高效的算法来判断 m x n 矩阵中，是否存在一个目标值。
                该矩阵具有如下特性：
                1. 每行中的整数从左到右按升序排列
                2. 每行的第一个整数大于前一行的最后一个整数
        
        解题思路: 将二维矩阵视为一维数组，使用二分查找
        时间复杂度: O(log(m*n))
        空间复杂度: O(1)
        """
        if not matrix or not matrix[0]:
            return False
            
        m, n = len(matrix), len(matrix[0])
        left, right = 0, m * n - 1
        
        while left <= right:
            mid = left + ((right - left) >> 1)
            row, col = mid // n, mid % n
            mid_val = matrix[row][col]
            
            if mid_val == target:
                return True
            elif mid_val < target:
                left = mid + 1
            else:
                right = mid - 1
                
        return False
    
    @staticmethod
    def search_rotated(nums: List[int], target: int) -> int:
        """
        LeetCode 33. Search in Rotated Sorted Array - 搜索旋转排序数组
        题目要求: 假设按照升序排序的数组在预先未知的某个点上进行了旋转。
                搜索一个给定的目标值，如果数组中存在这个目标值，则返回它的索引，否则返回 -1
        
        解题思路: 使用二分查找，需要先判断中间元素是在旋转点的左侧还是右侧
        时间复杂度: O(log n)
        空间复杂度: O(1)
        """
        if not nums:
            return -1
            
        left, right = 0, len(nums) - 1
        
        while left <= right:
            mid = left + ((right - left) >> 1)
            if nums[mid] == target:
                return mid
                
            # 判断左半部分是否有序
            if nums[left] <= nums[mid]:
                # 左半部分有序
                if nums[left] <= target < nums[mid]:
                    right = mid - 1
                else:
                    left = mid + 1
            else:
                # 右半部分有序
                if nums[mid] < target <= nums[right]:
                    left = mid + 1
                else:
                    right = mid - 1
                    
        return -1

# 测试代码
if __name__ == "__main__":
    # 测试find_left函数
    arr = [1, 2, 2, 2, 3, 3, 4, 5, 5, 5, 6]
    print("测试find_left函数:")
    print(f"数组: {arr}")
    print(f"find_left(arr, 2): {Code02_FindLeft.find_left(arr, 2)}")  # 应输出 1
    print(f"find_left(arr, 3): {Code02_FindLeft.find_left(arr, 3)}")  # 应输出 4
    print(f"find_left(arr, 4): {Code02_FindLeft.find_left(arr, 4)}")  # 应输出 6
    print(f"find_left(arr, 5): {Code02_FindLeft.find_left(arr, 5)}")  # 应输出 7
    print(f"find_left(arr, 6): {Code02_FindLeft.find_left(arr, 6)}")  # 应输出 10
    print(f"find_left(arr, 0): {Code02_FindLeft.find_left(arr, 0)}")  # 应输出 0 (不存在，插入位置为0)
    print(f"find_left(arr, 7): {Code02_FindLeft.find_left(arr, 7)}")  # 应输出 11 (不存在，插入位置为11)
    
    # 测试search_insert_position函数
    print("\n测试search_insert_position函数:")
    nums = [1, 3, 5, 6]
    print(f"数组: {nums}")
    print(f"search_insert_position(nums, 5): {Code02_FindLeft.search_insert_position(nums, 5)}")  # 应输出 2
    print(f"search_insert_position(nums, 2): {Code02_FindLeft.search_insert_position(nums, 2)}")  # 应输出 1
    print(f"search_insert_position(nums, 7): {Code02_FindLeft.search_insert_position(nums, 7)}")  # 应输出 4
    print(f"search_insert_position(nums, 0): {Code02_FindLeft.search_insert_position(nums, 0)}")  # 应输出 0
    
    # 测试first_bad_version函数
    print("\n测试first_bad_version函数:")
    print(f"first_bad_version(5): {Code02_FindLeft.first_bad_version(5)}")  # 应输出 4
    
    # 测试search_matrix函数
    print("\n测试search_matrix函数:")
    matrix = [
        [1, 3, 5, 7],
        [10, 11, 16, 20],
        [23, 30, 34, 60]
    ]
    print(f"矩阵: {matrix}")
    print(f"search_matrix(matrix, 3): {Code02_FindLeft.search_matrix(matrix, 3)}")  # 应输出 True
    print(f"search_matrix(matrix, 13): {Code02_FindLeft.search_matrix(matrix, 13)}")  # 应输出 False
    
    # 测试search_rotated函数
    print("\n测试search_rotated函数:")
    nums_rotated = [4, 5, 6, 7, 0, 1, 2]
    print(f"旋转数组: {nums_rotated}")
    print(f"search_rotated(nums_rotated, 0): {Code02_FindLeft.search_rotated(nums_rotated, 0)}")  # 应输出 4
    print(f"search_rotated(nums_rotated, 3): {Code02_FindLeft.search_rotated(nums_rotated, 3)}")  # 应输出 -1
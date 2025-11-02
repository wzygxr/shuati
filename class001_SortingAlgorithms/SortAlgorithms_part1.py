"""
排序算法实现 - Python版本 (第一部分)
包含归并排序、快速排序的基础实现
"""

import random
import time
import sys
from typing import List, Callable
from heapq import heappush, heappop

class SortAlgorithms:
    """
    排序算法类 - 包含多种排序算法的Python实现
    每种算法都包含详细的时间复杂度分析和工程化考量
    """
    
    @staticmethod
    def merge_sort(nums: List[int]) -> List[int]:
        """
        归并排序 - 递归版本
        时间复杂度: O(n log n)
        空间复杂度: O(n)
        稳定性: 稳定
        
        适用场景: 需要稳定排序、链表排序、外部排序
        工程考量: 递归深度、内存使用、缓存友好性
        """
        # 输入验证
        if not isinstance(nums, list):
            raise TypeError("输入必须是列表")
        if not all(isinstance(x, int) for x in nums):
            raise TypeError("列表必须只包含整数")
            
        # 边界条件处理
        if len(nums) <= 1:
            return nums.copy()
            
        # 分治递归
        mid = len(nums) // 2
        left = SortAlgorithms.merge_sort(nums[:mid])
        right = SortAlgorithms.merge_sort(nums[mid:])
        
        # 合并有序数组
        return SortAlgorithms._merge(left, right)
    
    @staticmethod
    def _merge(left: List[int], right: List[int]) -> List[int]:
        """合并两个有序数组"""
        result = []
        i = j = 0
        
        # 比较并合并
        while i < len(left) and j < len(right):
            if left[i] <= right[j]:
                result.append(left[i])
                i += 1
            else:
                result.append(right[j])
                j += 1
                
        # 添加剩余元素
        result.extend(left[i:])
        result.extend(right[j:])
        
        return result
    
    @staticmethod
    def merge_sort_iterative(nums: List[int]) -> List[int]:
        """
        归并排序 - 迭代版本（自底向上）
        避免递归调用，节省栈空间
        时间复杂度: O(n log n)
        空间复杂度: O(n)
        """
        if len(nums) <= 1:
            return nums.copy()
            
        n = len(nums)
        result = nums.copy()
        temp = [0] * n
        
        size = 1
        while size < n:
            for left in range(0, n - size, 2 * size):
                mid = left + size - 1
                right = min(left + 2 * size - 1, n - 1)
                SortAlgorithms._merge_iterative(result, temp, left, mid, right)
            size *= 2
            
        return result
    
    @staticmethod
    def _merge_iterative(nums: List[int], temp: List[int], left: int, mid: int, right: int):
        """迭代版本的合并操作"""
        i, j, k = left, mid + 1, left
        
        # 复制到临时数组
        for idx in range(left, right + 1):
            temp[idx] = nums[idx]
            
        # 合并
        while i <= mid and j <= right:
            if temp[i] <= temp[j]:
                nums[k] = temp[i]
                i += 1
            else:
                nums[k] = temp[j]
                j += 1
            k += 1
            
        # 处理剩余元素
        while i <= mid:
            nums[k] = temp[i]
            i += 1
            k += 1
        while j <= right:
            nums[k] = temp[j]
            j += 1
            k += 1
    
    @staticmethod
    def quick_sort(nums: List[int]) -> List[int]:
        """
        快速排序 - 基础版本
        时间复杂度: O(n log n) 平均, O(n²) 最坏
        空间复杂度: O(log n) 平均, O(n) 最坏（递归栈）
        稳定性: 不稳定
        """
        if len(nums) <= 1:
            return nums.copy()
            
        # 小数组优化：使用插入排序
        if len(nums) <= 16:
            return SortAlgorithms.insertion_sort(nums)
            
        # 随机化避免最坏情况
        nums_copy = nums.copy()
        random.shuffle(nums_copy)
        
        pivot = nums_copy[0]
        left = [x for x in nums_copy[1:] if x <= pivot]
        right = [x for x in nums_copy[1:] if x > pivot]
        
        return SortAlgorithms.quick_sort(left) + [pivot] + SortAlgorithms.quick_sort(right)
    
    @staticmethod
    def quick_sort_inplace(nums: List[int], low: int = 0, high: int = None) -> None:
        """
        快速排序 - 原地排序版本
        节省空间，直接修改原数组
        """
        if high is None:
            high = len(nums) - 1
            
        if low >= high:
            return
            
        # 小数组优化
        if high - low + 1 <= 16:
            SortAlgorithms._insertion_sort_range(nums, low, high)
            return
            
        pivot_index = SortAlgorithms._partition(nums, low, high)
        SortAlgorithms.quick_sort_inplace(nums, low, pivot_index - 1)
        SortAlgorithms.quick_sort_inplace(nums, pivot_index + 1, high)
    
    @staticmethod
    def _partition(nums: List[int], low: int, high: int) -> int:
        """快速排序的分区操作"""
        # 三数取中法选择基准
        mid = (low + high) // 2
        if nums[mid] < nums[low]:
            nums[low], nums[mid] = nums[mid], nums[low]
        if nums[high] < nums[low]:
            nums[low], nums[high] = nums[high], nums[low]
        if nums[high] < nums[mid]:
            nums[mid], nums[high] = nums[high], nums[mid]
            
        pivot = nums[mid]
        nums[mid], nums[high] = nums[high], nums[mid]
        
        i = low
        for j in range(low, high):
            if nums[j] <= pivot:
                nums[i], nums[j] = nums[j], nums[i]
                i += 1
                
        nums[i], nums[high] = nums[high], nums[i]
        return i
    
    @staticmethod
    def quick_sort_3way(nums: List[int]) -> List[int]:
        """
        快速排序 - 三路划分版本
        针对大量重复元素的优化
        时间复杂度: O(n log n) 平均
        """
        if len(nums) <= 1:
            return nums.copy()
            
        pivot = random.choice(nums)
        
        left = [x for x in nums if x < pivot]
        middle = [x for x in nums if x == pivot]
        right = [x for x in nums if x > pivot]
        
        return SortAlgorithms.quick_sort_3way(left) + middle + SortAlgorithms.quick_sort_3way(right)
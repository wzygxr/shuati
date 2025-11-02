"""
排序算法实现 - Python版本 (第二部分)
包含堆排序、插入排序、冒泡排序、选择排序等
"""

import random
import time
import sys
import os
from typing import List

# 添加第一部分导入路径
sys.path.append(os.path.dirname(__file__))
from SortAlgorithms_part1 import SortAlgorithms

class SortAlgorithmsPart2:
    """排序算法第二部分 - 堆排序和其他基础排序"""
    
    @staticmethod
    def heap_sort(nums: List[int]) -> List[int]:
        """
        堆排序
        时间复杂度: O(n log n)
        空间复杂度: O(1) 原地排序
        稳定性: 不稳定
        """
        if len(nums) <= 1:
            return nums.copy()
            
        n = len(nums)
        result = nums.copy()
        
        # 构建最大堆
        for i in range(n // 2 - 1, -1, -1):
            SortAlgorithmsPart2._heapify(result, n, i)
            
        # 逐个提取最大元素
        for i in range(n - 1, 0, -1):
            result[0], result[i] = result[i], result[0]
            SortAlgorithmsPart2._heapify(result, i, 0)
            
        return result
    
    @staticmethod
    def _heapify(nums: List[int], n: int, i: int) -> None:
        """堆调整函数"""
        largest = i
        left = 2 * i + 1
        right = 2 * i + 2
        
        if left < n and nums[left] > nums[largest]:
            largest = left
            
        if right < n and nums[right] > nums[largest]:
            largest = right
            
        if largest != i:
            nums[i], nums[largest] = nums[largest], nums[i]
            SortAlgorithmsPart2._heapify(nums, n, largest)
    
    @staticmethod
    def insertion_sort(nums: List[int]) -> List[int]:
        """
        插入排序
        时间复杂度: O(n²) 最坏, O(n) 最好（已排序）
        空间复杂度: O(1)
        稳定性: 稳定
        """
        if len(nums) <= 1:
            return nums.copy()
            
        result = nums.copy()
        for i in range(1, len(result)):
            key = result[i]
            j = i - 1
            
            while j >= 0 and result[j] > key:
                result[j + 1] = result[j]
                j -= 1
            result[j + 1] = key
            
        return result
    
    @staticmethod
    def _insertion_sort_range(nums: List[int], low: int, high: int) -> None:
        """指定范围的插入排序"""
        for i in range(low + 1, high + 1):
            key = nums[i]
            j = i - 1
            while j >= low and nums[j] > key:
                nums[j + 1] = nums[j]
                j -= 1
            nums[j + 1] = key
    
    @staticmethod
    def bubble_sort(nums: List[int]) -> List[int]:
        """
        冒泡排序
        时间复杂度: O(n²)
        空间复杂度: O(1)
        稳定性: 稳定
        """
        if len(nums) <= 1:
            return nums.copy()
            
        result = nums.copy()
        n = len(result)
        
        for i in range(n - 1):
            swapped = False
            for j in range(n - i - 1):
                if result[j] > result[j + 1]:
                    result[j], result[j + 1] = result[j + 1], result[j]
                    swapped = True
                    
            if not swapped:
                break
                
        return result
    
    @staticmethod
    def selection_sort(nums: List[int]) -> List[int]:
        """
        选择排序
        时间复杂度: O(n²)
        空间复杂度: O(1)
        稳定性: 不稳定
        """
        if len(nums) <= 1:
            return nums.copy()
            
        result = nums.copy()
        n = len(result)
        
        for i in range(n - 1):
            min_index = i
            for j in range(i + 1, n):
                if result[j] < result[min_index]:
                    min_index = j
                    
            result[i], result[min_index] = result[min_index], result[i]
            
        return result


class SortAlgorithmsComplete(SortAlgorithmsPart2):
    """完整的排序算法类 - 继承第一部分和第二部分"""
    
    @staticmethod
    def test_all_algorithms() -> None:
        """测试所有排序算法的正确性"""
        print("=== 排序算法测试 ===")
        
        test_cases = [
            [],                    # 空数组
            [1],                   # 单元素
            [3, 1, 2],             # 小数组
            [5, 2, 8, 1, 9, 3],    # 中等数组
            [1, 2, 3, 4, 5],       # 已排序
            [5, 4, 3, 2, 1],       # 逆序
            [4, 2, 2, 8, 3, 3, 1],  # 重复元素
            [1, 1, 1, 1, 1]        # 全相同
        ]
        
        algorithms = {
            "归并排序": SortAlgorithms.merge_sort,
            "快速排序": SortAlgorithms.quick_sort,
            "堆排序": SortAlgorithmsComplete.heap_sort,
            "插入排序": SortAlgorithmsComplete.insertion_sort,
            "冒泡排序": SortAlgorithmsComplete.bubble_sort,
            "选择排序": SortAlgorithmsComplete.selection_sort
        }
        
        for i, test_case in enumerate(test_cases, 1):
            print(f"\n测试用例 {i}: {test_case}")
            
            for name, algorithm in algorithms.items():
                try:
                    result = algorithm(test_case)
                    expected = sorted(test_case)
                    correct = result == expected
                    status = "✓" if correct else "✗"
                    print(f"{name}: {status}", end=" ")
                except Exception as e:
                    print(f"{name}: 异常({e})", end=" ")
            print()
    
    @staticmethod
    def performance_test() -> None:
        """性能测试 - 比较不同算法的执行时间"""
        print("\n=== 性能测试 ===")
        
        sizes = [100, 1000, 10000]
        algorithms = {
            "归并排序": SortAlgorithms.merge_sort,
            "快速排序": SortAlgorithms.quick_sort,
            "堆排序": SortAlgorithmsComplete.heap_sort,
            "内置排序": sorted
        }
        
        for size in sizes:
            print(f"\n数据规模: {size}")
            
            test_data = [random.randint(0, size * 10) for _ in range(size)]
            
            for name, algorithm in algorithms.items():
                start_time = time.time()
                result = algorithm(test_data)
                end_time = time.time()
                
                duration = (end_time - start_time) * 1000
                print(f"{name}: {duration:.2f} ms")
    
    @staticmethod
    def _is_sorted(nums: List[int]) -> bool:
        """检查数组是否已排序"""
        return all(nums[i] <= nums[i + 1] for i in range(len(nums) - 1))


class KthLargest:
    """第K大元素相关算法"""
    
    @staticmethod
    def find_kth_largest(nums: List[int], k: int) -> int:
        """
        快速选择算法 - 寻找第K大的元素
        时间复杂度: O(n) 平均, O(n²) 最坏
        空间复杂度: O(1)
        """
        if not nums or k < 1 or k > len(nums):
            raise ValueError("Invalid input parameters")
            
        return KthLargest._quick_select(nums, 0, len(nums) - 1, len(nums) - k)
    
    @staticmethod
    def _quick_select(nums: List[int], left: int, right: int, k: int) -> int:
        """快速选择算法的核心实现"""
        if left == right:
            return nums[left]
            
        pivot_index = random.randint(left, right)
        pivot_index = KthLargest._partition(nums, left, right, pivot_index)
        
        if k == pivot_index:
            return nums[k]
        elif k < pivot_index:
            return KthLargest._quick_select(nums, left, pivot_index - 1, k)
        else:
            return KthLargest._quick_select(nums, pivot_index + 1, right, k)
    
    @staticmethod
    def _partition(nums: List[int], left: int, right: int, pivot_index: int) -> int:
        """分区操作"""
        pivot_value = nums[pivot_index]
        nums[pivot_index], nums[right] = nums[right], nums[pivot_index]
        
        store_index = left
        for i in range(left, right):
            if nums[i] < pivot_value:
                nums[store_index], nums[i] = nums[i], nums[store_index]
                store_index += 1
                
        nums[store_index], nums[right] = nums[right], nums[store_index]
        return store_index


if __name__ == "__main__":
    # 测试所有算法
    SortAlgorithmsComplete.test_all_algorithms()
    
    # 性能测试
    SortAlgorithmsComplete.performance_test()
    
    # 测试第K大元素
    nums = [3, 2, 1, 5, 6, 4]
    k = 2
    result = KthLargest.find_kth_largest(nums, k)
    print(f"\n第{k}大元素: {result}")
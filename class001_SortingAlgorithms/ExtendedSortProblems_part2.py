"""
排序算法扩展题目 - Python版本 (第二部分)
"""

import heapq
import random
from typing import List, Tuple
import sys

class ExtendedSortProblems:
    """
    题目7: 剑指Offer 45. 把数组排成最小的数
    来源: 剑指Offer
    难度: 中等
    
    时间复杂度: O(n log n)
    空间复杂度: O(n)
    是否最优解: 是
    """
    @staticmethod
    def min_number(nums: List[int]) -> str:
        """
        把数组排成最小的数
        
        Args:
            nums: 非负整数数组
            
        Returns:
            能拼接出的最小数字的字符串表示
        """
        if not nums:
            return ""
        
        # 将数字转换为字符串
        str_nums = [str(num) for num in nums]
        
        # 自定义排序：比较 s1+s2 和 s2+s1
        str_nums.sort(key=lambda x: x * 10)  # 乘以10确保比较长度足够
        
        # 拼接结果
        return ''.join(str_nums)
    
    """
    题目8: HackerRank - Counting Inversions
    来源: HackerRank
    链接: https://www.hackerrank.com/challenges/ctci-merge-sort
    难度: 困难
    
    时间复杂度: O(n log n)
    空间复杂度: O(n)
    是否最优解: 是
    """
    @staticmethod
    def count_inversions(arr: List[int]) -> int:
        """
        计算数组中逆序对的数量
        
        Args:
            arr: 整数数组
            
        Returns:
            逆序对的数量
        """
        if len(arr) <= 1:
            return 0
        
        temp = [0] * len(arr)
        return ExtendedSortProblems._merge_sort_count_inversions(arr, 0, len(arr) - 1, temp)
    
    @staticmethod
    def _merge_sort_count_inversions(arr: List[int], left: int, right: int, temp: List[int]) -> int:
        """归并排序统计逆序对"""
        if left >= right:
            return 0
        
        mid = left + (right - left) // 2
        count = 0
        
        count += ExtendedSortProblems._merge_sort_count_inversions(arr, left, mid, temp)
        count += ExtendedSortProblems._merge_sort_count_inversions(arr, mid + 1, right, temp)
        count += ExtendedSortProblems._merge_and_count(arr, left, mid, right, temp)
        
        return count
    
    @staticmethod
    def _merge_and_count(arr: List[int], left: int, mid: int, right: int, temp: List[int]) -> int:
        """归并并统计逆序对"""
        for i in range(left, right + 1):
            temp[i] = arr[i]
        
        i, k, j = left, left, mid + 1
        count = 0
        
        while i <= mid and j <= right:
            if temp[i] <= temp[j]:
                arr[k] = temp[i]
                i += 1
            else:
                arr[k] = temp[j]
                j += 1
                count += (mid - i + 1)  # 统计逆序对
            k += 1
        
        while i <= mid:
            arr[k] = temp[i]
            i += 1
            k += 1
        
        while j <= right:
            arr[k] = temp[j]
            j += 1
            k += 1
        
        return count
    
    """
    题目9: 牛客网 NC140 排序
    来源: 牛客网
    链接: https://www.nowcoder.com/practice/2baf799ea0594abd974d37139de27896
    难度: 简单
    
    时间复杂度: 根据算法选择
    空间复杂度: 根据算法选择
    """
    @staticmethod
    def sort_array_nc140(arr: List[int]) -> List[int]:
        """
        根据数据规模选择合适的排序算法
        
        Args:
            arr: 待排序数组
            
        Returns:
            排序后的数组
        """
        if len(arr) <= 1:
            return arr.copy()
        
        # 根据数据规模选择算法
        if len(arr) < 50:
            # 小数组使用插入排序
            return ExtendedSortProblems._insertion_sort(arr.copy())
        else:
            # 中等以上使用快速排序
            arr_copy = arr.copy()
            ExtendedSortProblems._quick_sort(arr_copy, 0, len(arr_copy) - 1)
            return arr_copy
    
    @staticmethod
    def _insertion_sort(arr: List[int]) -> List[int]:
        """插入排序"""
        for i in range(1, len(arr)):
            key = arr[i]
            j = i - 1
            while j >= 0 and arr[j] > key:
                arr[j + 1] = arr[j]
                j -= 1
            arr[j + 1] = key
        return arr
    
    @staticmethod
    def _quick_sort(arr: List[int], low: int, high: int) -> None:
        """快速排序"""
        if low < high:
            pivot_index = ExtendedSortProblems._partition(arr, low, high)
            ExtendedSortProblems._quick_sort(arr, low, pivot_index - 1)
            ExtendedSortProblems._quick_sort(arr, pivot_index + 1, high)
    
    @staticmethod
    def _partition(arr: List[int], low: int, high: int) -> int:
        """快速排序分区操作"""
        pivot = arr[high]
        i = low - 1
        
        for j in range(low, high):
            if arr[j] <= pivot:
                i += 1
                arr[i], arr[j] = arr[j], arr[i]
        
        arr[i + 1], arr[high] = arr[high], arr[i + 1]
        return i + 1
    
    """
    题目10: 外部排序模拟 - 多路归并
    来源: 算法导论
    难度: 困难
    
    时间复杂度: O(n log k) - k为归并路数
    空间复杂度: O(k) - 缓冲区大小
    """
    class ExternalSort:
        @staticmethod
        def multiway_merge(chunks: List[List[int]]) -> List[int]:
            """
            模拟多路归并排序
            
            Args:
                chunks: 多个有序数据块
                
            Returns:
                合并后的有序数组
            """
            if not chunks:
                return []
            
            # 使用优先队列进行多路归并
            heap = []
            
            # 初始化每个数据块的指针
            pointers = [0] * len(chunks)
            total_size = sum(len(chunk) for chunk in chunks)
            
            # 将每个数据块的第一个元素加入堆
            for i, chunk in enumerate(chunks):
                if chunk:
                    heapq.heappush(heap, (chunk[0], i, 0))
                    pointers[i] = 1
            
            result = []
            
            # 多路归并
            while heap:
                value, chunk_index, element_index = heapq.heappop(heap)
                result.append(value)
                
                element_index += 1
                if element_index < len(chunks[chunk_index]):
                    heapq.heappush(heap, (chunks[chunk_index][element_index], chunk_index, element_index))
            
            return result
    
    """
    测试所有扩展题目
    """
    @staticmethod
    def test_all_problems():
        """测试所有扩展题目"""
        print("=== 扩展排序题目测试开始 ===")
        
        # 测试题目1: 合并两个有序数组
        print("\n题目1: 合并两个有序数组")
        nums1 = [1, 2, 3, 0, 0, 0]
        nums2 = [2, 5, 6]
        ExtendedSortProblems.merge_sorted_arrays(nums1, 3, nums2, 3)
        print(f"合并结果: {nums1}")
        
        # 测试题目2: 最接近原点的K个点
        print("\n题目2: 最接近原点的K个点")
        points = [[1, 3], [-2, 2], [5, 8], [0, 1]]
        result2 = ExtendedSortProblems.k_closest(points, 2)
        print(f"最接近的2个点: {result2}")
        
        # 测试题目3: 距离相等的条形码
        print("\n题目3: 距离相等的条形码")
        barcodes = [1, 1, 1, 2, 2, 2]
        result3 = ExtendedSortProblems.rearrange_barcodes(barcodes)
        print(f"重新排列结果: {result3}")
        
        # 测试题目4: 摆动排序II
        print("\n题目4: 摆动排序II")
        nums4 = [1, 5, 1, 1, 6, 4]
        ExtendedSortProblems.wiggle_sort(nums4)
        print(f"摆动排序结果: {nums4}")
        
        # 测试题目5: 摆动排序
        print("\n题目5: 摆动排序")
        nums5 = [3, 5, 2, 1, 6, 4]
        ExtendedSortProblems.wiggle_sort_280(nums5)
        print(f"摆动排序结果: {nums5}")
        
        # 测试题目6: 翻转对
        print("\n题目6: 翻转对")
        nums6 = [1, 3, 2, 3, 1]
        count6 = ExtendedSortProblems.reverse_pairs_493(nums6)
        print(f"翻转对数量: {count6}")
        
        # 测试题目7: 把数组排成最小的数
        print("\n题目7: 把数组排成最小的数")
        nums7 = [10, 2]
        result7 = ExtendedSortProblems.min_number(nums7)
        print(f"最小数字: {result7}")
        
        # 测试题目8: 逆序对计数
        print("\n题目8: 逆序对计数")
        nums8 = [2, 4, 1, 3, 5]
        count8 = ExtendedSortProblems.count_inversions(nums8)
        print(f"逆序对数量: {count8}")
        
        # 测试题目9: 牛客网排序
        print("\n题目9: 牛客网排序")
        nums9 = [3, 1, 4, 1, 5, 9, 2, 6]
        result9 = ExtendedSortProblems.sort_array_nc140(nums9)
        print(f"排序结果: {result9}")
        
        # 测试题目10: 外部排序
        print("\n题目10: 外部排序模拟")
        chunks = [
            [1, 3, 5],
            [2, 4, 6],
            [0, 7, 8]
        ]
        result10 = ExtendedSortProblems.ExternalSort.multiway_merge(chunks)
        print(f"多路归并结果: {result10}")
        
        print("\n=== 扩展排序题目测试结束 ===")

if __name__ == "__main__":
    try:
        ExtendedSortProblems.test_all_problems()
    except Exception as e:
        print(f"测试过程中出现错误: {e}")
        import traceback
        traceback.print_exc()